package com.handge.housingfund.task;

import com.handge.housingfund.common.service.loan.ILoanTaskService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskMain {

	private static SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd");

	public static void main(String[] args) throws Exception {
		com.alibaba.dubbo.container.Main.main(args);
//		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
//				new String[]{"META-INF/spring/dubbo.xml"});
//		context.start();
// 		ILoanTaskService demoService = (ILoanTaskService) context.getBean("iloanTaskService"); // obtain proxy object for remote invocation
////		demoService.repamentNormal("301",sim.parse("2017-12-01")); // execute remote invocation
////		demoService.repayment("103",sim.parse("2017-12-01"),sim.parse("2017-12-15"));
////		demoService.overdueRepaymentChange();
////		demoService.debitSend();
////        demoService.overdueAutomatic("104");
//		demoService.remainingPeriod();
//		System.out.println("god"); // show the result
	}
}