package com.handge.housingfund.common;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.io.File;
import java.util.Collection;

/**
 * Created by xuefei_wang on 17-10-11.
 */
public class ServiceContainer {

    private String configDir;

    private static final Logger logger = LogManager.getLogger(ServiceContainer.class);

    static AbstractXmlApplicationContext context;

    static String extension = "xml";

    static String[] extensions = {extension};

    public static final String DEFAULT_SPRING_CONFIG = "classpath*:META-INF/spring/*.xml";

    public ServiceContainer(String configDir){
        this.configDir = configDir;
    }


    public AbstractApplicationContext start() {
        if (configDir != null){
            File dir = new File(configDir);
            if (dir.isDirectory()){
                Collection<File> configsSet = FileUtils.listFiles(dir, extensions, true);
                int size = configsSet.size();
                if (size < 1){
                    System.out.println("con not find any xml(spring) config in dir :" + configDir);
                    System.exit(0);
                }
                File[] files = configsSet.toArray(new File[size]);
                String[] configs = new String[size];
                for (int i = 0; i <files.length ; i++) {
                    configs[i] = "file:"+files[i].getAbsolutePath();
                }
                context = new FileSystemXmlApplicationContext(configs);
            }
        }else {
            context = new ClassPathXmlApplicationContext(DEFAULT_SPRING_CONFIG.split("[,\\s]+"));
        }
        context.start();
        return  context;
    }

    public void stop() {
        try {
            if (context != null) {
                context.stop();
                context.close();
                context = null;
            }
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
    }
}
