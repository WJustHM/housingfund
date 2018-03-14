package com.handge.housingfund.common;


import com.handge.housingfund.common.configure.Configure;
import org.apache.commons.cli.*;
import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xuefei_wang on 17-10-11.
 */
public class BootStrap {

    public static  final String  SHUTDOWN_HOOK_KEY = "housingfund.shutdown.hook";


    private static final Logger logger = LogManager.getLogger(BootStrap.class);

    private static volatile boolean running = true;

    public static void main(String[] args)  {

        Configuration configuration = Configure.getInstance().getConfiguration();
        try {
            Options options = new Options();
            Option configdirOption = new Option("C","config",true,"配置文件地址");
            configdirOption.setRequired(false);
            options.addOption(configdirOption);
            CommandLineParser parser = new DefaultParser();
            HelpFormatter formatter = new HelpFormatter();
            CommandLine cmd = null;
            try {
                cmd = parser.parse(options,args);
            }catch (ParseException e) {
                formatter.printHelp("use -C or --config set your spring config dirs ",options);
            }
            String configDir = null;
            if (cmd.hasOption("C")){
                configDir  = cmd.getOptionValue("C");
            }else {
                configDir  =  configuration.getString("service_config",null);
                System.out.println("您可以通过命令行　-C 　指定spring服务配置文件的地址路径　\n 以此覆盖service_config制定的路径");
            }

            if (configDir == null) {
                System.out.println("未指定Dubbo服务启动配置, 将从classpath加载");
            }else {
                System.out.println("Use connfigs (" + configDir + ") to run dubbo serivce.");
            }

            ServiceContainer serviceContainer = new ServiceContainer(configDir);

            if ("true".equals(System.getProperty(SHUTDOWN_HOOK_KEY))) {
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    public void run() {
                            try {
                                serviceContainer.stop();
                                logger.info("Dubbo " + serviceContainer.getClass().getSimpleName() + " stopped!");
                            } catch (Throwable t) {
                                logger.error(t.getMessage(), t);
                            }
                            synchronized (BootStrap.class) {
                                running = false;
                                BootStrap.class.notify();
                            }
                    }
                });
            }
            serviceContainer.start();
            logger.info("Dubbo " + serviceContainer.getClass().getSimpleName() + " started!");

            System.out.println(new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]").format(new Date()) + " Dubbo service server started!");
        } catch (RuntimeException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            System.exit(1);
        }
        synchronized (ServiceContainer.class) {
            while (running) {
                try {
                    ServiceContainer.class.wait();
                } catch (Throwable e) {
                }
            }
        }
    }

}
