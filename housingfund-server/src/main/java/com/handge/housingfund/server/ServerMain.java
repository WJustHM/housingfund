

package com.handge.housingfund.server;

import com.handge.housingfund.common.ServiceContainer;
import com.handge.housingfund.common.configure.Configure;
import org.apache.commons.cli.*;
import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;

public class ServerMain {

	private static final Logger logger = LogManager.getLogger(ServerMain.class);

	public static void main(String[] args) throws Exception {
		Configuration configuration = Configure.getInstance().getConfiguration();
		Options options = new Options();
		Option configdir = new Option("C","config",true,"配置文件地址");
		configdir.setRequired(false);
		options.addOption(configdir);
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
			System.out.println("您可以通过命令行　-C 　指定spring服务配置文件的地址路径　\n 以此覆盖service_config制定的路径");
			configDir  =  configuration.getString("service_config",null);
		}

		if (configdir == null) {
			System.out.println("建议　：　请指定服务配置路径！！！　");
			System.out.println("否则　：　采用默认配置！！！　");
		}else {
			logger.info("Use connfigs (" + configDir + ") to run dubbo serivce.");
		}
		ServiceContainer serviceContainer = new ServiceContainer(configDir);
		ApplicationContext ac = serviceContainer.start();
		NettyServer netty = ac.getBean(NettyServer.class);
		netty.start();
	}
}

