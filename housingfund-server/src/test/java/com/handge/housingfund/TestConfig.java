package com.handge.housingfund;

import com.handge.housingfund.common.configure.Configure;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by xuefei_wang on 17-7-14.
 */
public class TestConfig {
    public static void  main(String[] args) throws ConfigurationException, IOException {

        System.out.println(TestConfig.class.getClassLoader().getResource(""));
        File directory = new File("");

        Configure configure = Configure.getInstance();
        Configuration config = configure.getConfiguration("test13");
        Iterator<String> keys = config.getKeys();
        while (keys.hasNext()){
            String key = keys.next();
            System.out.println(key + "  :  " + config.getString(key));
        }

        System.out.println(String.format("%02d",1));
        System.out.println(String.format("%02d",12));
    }
}
