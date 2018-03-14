package com.handge.housingfund.bank.server;

import com.handge.housingfund.bank.server.service.HandleAccChangeNotice;
import com.handge.housingfund.common.ServiceContainer;
import com.handge.housingfund.common.configure.Configure;
import org.apache.commons.cli.*;
import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;

/**
 * Created by xuefei_wang on 17-10-30.
 */
public class BankServer {

    private static final Logger logger = LogManager.getLogger(BankServer.class);
    @SuppressWarnings("resource")
    public static void main(String[] args) throws Exception {
        Configuration configuration = Configure.getInstance().getConfiguration();
        Options options = new Options();
        Option configdir = new Option("C", "config", true, "配置文件地址");
        configdir.setRequired(false);
        options.addOption(configdir);
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            formatter.printHelp("use -C or --config set your spring config dirs ", options);
        }
        String configDir = null;
        if (cmd.hasOption("C")) {
            configDir = cmd.getOptionValue("C");
        } else {
            logger.info("您可以通过命令行　-C 　指定spring服务配置文件的地址路径   以此覆盖service_config制定的路径");
            configDir = configuration.getString("service_config", null);
        }

        if (configdir == null) {
            logger.info("建议　：　请指定服务配置路径！！！　");
            logger.info("否则　：　采用默认配置！！！　");
        } else {
            logger.info("Use connfigs (" + configDir + ") to run dubbo serivce.");
        }
        ServiceContainer serviceContainer = new ServiceContainer(configDir);
        ApplicationContext ac = serviceContainer.start();
        HandleAccChangeNotice.init(ac);
        CRFCenterServer server = ac.getBean(CRFCenterServer.class);
        server.start();
    }
}
