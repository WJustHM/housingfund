package com.handge.housingfund.task.jobs;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Component
public class UpdateFixedBalanceJobs extends QuartzJobBean {
	@Autowired
	Jobs jobs;

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		try {
			String tasksStr = (String) arg0.getJobDetail().getJobDataMap().get("UpdateFixedBalance");
			String[] tasks = tasksStr.split(",");
			ExecutorService pool = Executors.newCachedThreadPool();
			for (String task : tasks) {
				Runnable r = jobs.getTask(task);
				pool.execute(r);
			}
			pool.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
