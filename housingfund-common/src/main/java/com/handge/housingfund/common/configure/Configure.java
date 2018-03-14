package com.handge.housingfund.common.configure;

import com.handge.housingfund.common.Constant;
import org.apache.commons.configuration2.*;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by xuefei_wang on 17-7-13.
 *
 * 一些额外的初始化：
 * 1.预先指定环境变量HOUSINGFUND_CONF_DIR（默认/etc/housingfund/）．
 * 2.加载的配置文件是ｐroperties格式，后缀名为　.conf　如collection.conf
 * 3.默认的加载中使用文件名作为配置单元名字，配置单元名字不包含后缀名　如　collection
 * 4.可通过指定单元配置名字获取该单元中的配置getConfiguration(String name)
 * 5.可获取所有配置getConfiguration()
 *
 * 6.如需要自己添加配置，参见addConfiguration系列方法
 *
 * 加载优先顺序　：　环境变量HOUSINGFUND_CONF_DIR指定的文件夹　*.conf　－－（如果环境变量没有指定）－－＞　系统/etc/housingfund/　文件下　*.conf　－－（如果文件夹不存在）--> classpath 下　*.conf
 */
public class Configure {

    public final static String  BASEPTH ="/etc/housingfund/";
    public final static String  Config_Env = "HOUSINGFUND_CONF_DIR";
    public final static String  Stuff_conf = ".conf";
    public final static String  Stuff_properties = ".properties";
    public final static String  Stuff_xml = ".xml";
    public  static String confdir;
    public  static  File configs;

    private static volatile Configure configure = null;
    private static volatile CombinedConfiguration combinedConfiguration = new CombinedConfiguration();

    private Configure()  {
        EnvironmentConfiguration environmentConfiguration = new EnvironmentConfiguration();
        combinedConfiguration.addConfiguration(environmentConfiguration,"ENV");
        confdir  = combinedConfiguration.getString(Config_Env,BASEPTH);
        configs = new File(confdir);
        if (!configs.exists()){
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            URL resource = classLoader.getResource("");
            if (resource != null){
                configs = new File( resource.getPath());
            } else{
                configs = new File("");
            }
        }
        System.out.println("load config from "+ configs.getAbsolutePath());
       parse(configs);
    }

    /**
     * 递归加载配置
     * @param file
     */
    private void parse(File file ){
        if (file.isFile()){
            if( file.exists()){
                String fileName = file.getName();
                if (fileName.startsWith("log4j")) return;
                String configName  = fileName.replace(Stuff_conf,"").replace(Stuff_properties,"").replace(Stuff_xml,"").trim();
                if (this.combinedConfiguration.getConfiguration(configName) != null){
                    System.out.println(configName + "  already exist in configuration tree");
                    return;
                }
                try {
                    if (fileName.endsWith(Stuff_conf)){
                        addConfiguration(file);
                        System.out.println("add config  "+file.getAbsolutePath());
                    }else if (fileName.endsWith(Stuff_properties)){
                        addPropertiesConfiguration(file,fileName);
                        System.out.println("add config  "+file.getAbsolutePath());
                    }else if (fileName.endsWith(Stuff_xml)){
//                        addXMLConfiguration(file,fileName);
                        System.out.println("ignore config  "+file.getAbsolutePath());
                    }
                } catch (ConfigurationException e) {
                    e.printStackTrace();
                }
            }
        }else {
            File[] files = file.listFiles();
            if (files == null || files.length == 0) return;
            for (File f : files){
                parse(f);
            }
        }
    }
    

    public static Configure getInstance()  {
      if (null == configure){
          synchronized (Configure.class){
              if ( null == configure ){
                  configure = new Configure();
                  Set<String> names = combinedConfiguration.getConfigurationNames();
                  for (String name : names){
                      System.out.println("==================================================");
                      System.out.println(name + "  :");
                      System.out.println("=======");
                      Configuration configuration = combinedConfiguration.getConfiguration(name);
                      Iterator<String> keys = configuration.getKeys();
                      while (keys.hasNext()){
                          String key = keys.next();
                          String[] values = configuration.getStringArray(key);
                          System.out.println(key + "  :  "+ String.join(" , " ,values));
                      }
                  }
              }
          }
      }

      return configure;
     }


    /**
     *
     * @param configuration
     * @param name　配置单元名字
     */
    public void addConfiguration(Configuration configuration , String name){
        combinedConfiguration.addConfiguration( configuration , name);
    }

    /**
     *
     * @param file
     * @throws ConfigurationException
     */
    public void addConfiguration(File file) throws ConfigurationException {
        if (file.isFile()){
            Parameters params = new Parameters();
            FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
                    new FileBasedConfigurationBuilder<PropertiesConfiguration>(PropertiesConfiguration.class)
                            .configure(params.properties()
                                    .setFile(file).setEncoding("UTF-8"));
            String name = file.getName().replace(Stuff_conf,"").trim();
            combinedConfiguration.addConfiguration( builder.getConfiguration(), name);
        }
    }

    /**
     *
     * @param file
     * @param name　配置单元名字
     * @throws ConfigurationException
     */
    public void addPropertiesConfiguration(File file , String name) throws ConfigurationException {
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
                new FileBasedConfigurationBuilder<PropertiesConfiguration>(PropertiesConfiguration.class)
                        .configure(params.properties()
                                .setFile(file).setEncoding("UTF-8"));
            combinedConfiguration.addConfiguration(builder.getConfiguration(),name.replace(Stuff_properties,"").trim());
    }

    /**
     *
     * @param file
     * @param name　配置单元名字
     * @throws ConfigurationException
     */
    public void addXMLConfiguration(File file,String name) throws ConfigurationException {
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<XMLConfiguration> builder =
                new FileBasedConfigurationBuilder<XMLConfiguration>(XMLConfiguration.class)
                        .configure(params.xml()
                                .setFile(file)
                                .setValidating(true).setEncoding("UTF-8"));
        combinedConfiguration.addConfiguration(builder.getConfiguration() ,name.replace(Stuff_xml,"").trim());

    }

    public Configuration getConfiguration(){
        return this.combinedConfiguration;
    }

    /**
     * 指定单元　优于全配扫描
     * @param name 配置单元名字
     * @return
     */
    public Configuration getConfiguration(String name){
        Configuration conf = this.combinedConfiguration.getConfiguration(name);
        if (null == conf){
            System.out.println(" please set your config in dir :  "+ configs.getAbsolutePath() + "  with  > " +name);
            System.exit(0);
        }
        return conf;
    }

    /**
     *
     * @return
     */
    @Deprecated
    public Configuration getServerConfiguration(){
        return getConfiguration(Constant.SERVER_CONF);
    }
}
