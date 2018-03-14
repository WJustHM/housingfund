package com.handge.housingfund.task.jobs;

import com.handge.housingfund.common.service.account.IPolicyTaskService;
import com.handge.housingfund.common.service.bank.ibank.IBankTask;
import com.handge.housingfund.common.service.collection.service.common.CollectionTask;
import com.handge.housingfund.common.service.collection.service.withdrawl.WithdrawlTasks;
import com.handge.housingfund.common.service.finance.IFinanceReportService;
import com.handge.housingfund.common.service.finance.ITimedFinanceTask;
import com.handge.housingfund.common.service.loan.ILoanTaskService;
import com.handge.housingfund.common.service.others.IActionService;
import com.handge.housingfund.common.service.others.IDictionaryService;
import com.handge.housingfund.common.service.others.IStateMachineService;
import com.handge.housingfund.common.service.schedule.ITimeTask;
import com.handge.housingfund.common.service.schedule.enums.TimeTaskType;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.database.enums.BusinessQuartzType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;

@Component
public class Jobs {
	private static final Logger LOGGER = LogManager.getLogger(Jobs.class);
	@Autowired
	private IDictionaryService dictionaryService;
	@Autowired
	private IActionService actionService;
	@Autowired
	private IBankTask iBankTask;
	@Autowired
	private ITimeTask iTimeTask;
	@Autowired
	private WithdrawlTasks withdrawlTasks;
	@Autowired
	private CollectionTask collectionTask;
	@Autowired
	private ITimedFinanceTask iTimedFinanceTask;
	@Autowired
	private IPolicyTaskService policyTaskService;
	@Autowired
	private IStateMachineService iStateMachineService;
	@Autowired
	private ILoanTaskService iloanTaskService;
	@Autowired
	private IFinanceReportService iFinanceReportService;
	private static   Object lock=new Object();

	class Test implements Runnable {
		@Override
		public void run() {
			System.out.println(dictionaryService.getDictionary().size());
		}
	}

	class Action implements Runnable {
		@Override
		public void run() {
			System.out.println(actionService.getActionKeywords().size());
		}
	}
	/**
	 * 启封、封存、调基、调比
	 */
	class DoCollectionTask implements Runnable {
		@Override
		public void run() {
			System.out.println("启封、封存、调基、调比");
			LOGGER.info("启封、封存、调基、调比");
			try{
				collectionTask.doCollectionTask();
				iTimeTask.addTaskLog(BusinessQuartzType.Collection, TimeTaskType.启封封存调基调比,new Date(),true,null);
			}catch (Exception ee){
				if(ee instanceof ErrorException){
					iTimeTask.addTaskLog(BusinessQuartzType.Collection, TimeTaskType.启封封存调基调比,new Date(),false,((ErrorException)ee).getMsg()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}else {
					iTimeTask.addTaskLog(BusinessQuartzType.Collection, TimeTaskType.启封封存调基调比,new Date(),false, ee.getMessage()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}
			}
		}
	}

	/**
	 * 对单位进行短信催缴
	 */
	class DoSMSPayCallTask implements Runnable {
		@Override
		public void run() {
			System.out.println("单位短信催缴");
			LOGGER.info("单位短信催缴");
			try{
				collectionTask.doSMSPayCall();
				iTimeTask.addTaskLog(BusinessQuartzType.Collection, TimeTaskType.单位短信催缴,new Date(),true,null);
			}catch (Exception ee){
				if(ee instanceof ErrorException){
					iTimeTask.addTaskLog(BusinessQuartzType.Collection, TimeTaskType.单位短信催缴,new Date(),false,((ErrorException)ee).getMsg()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}else {
					iTimeTask.addTaskLog(BusinessQuartzType.Collection, TimeTaskType.单位短信催缴,new Date(),false, ee.getMessage()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}
			}
		}
	}


	/**
	 * 获取全国所有银行网点的联行号
	 * 每月月末晚上11点
	 */
	class addChgNo implements Runnable {
		@Override
		public void run() {
			System.out.println("获取全国所有银行网点的联行号,每月月末晚上11点");
			try{
				iBankTask.addChgNo();
				iTimeTask.addTaskLog(BusinessQuartzType.Other, TimeTaskType.获取全国所有银行网点联行号,new Date(),true,null);
			}catch (Exception ee){
				if(ee instanceof ErrorException){
					iTimeTask.addTaskLog(BusinessQuartzType.Other, TimeTaskType.获取全国所有银行网点联行号,new Date(),false,((ErrorException)ee).getMsg()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}else {
					iTimeTask.addTaskLog(BusinessQuartzType.Other, TimeTaskType.获取全国所有银行网点联行号,new Date(),false, ee.getMessage()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}
			}
		}
	}
	/**
	 * 获取全国所有已上线的中心
	 * 每月月初晚上11点
	 */
	class addCRFCenterCode implements Runnable {
		@Override
		public void run() {
			System.out.println("获取全国所有已上线的中心,每月月初晚上11点");
			try{
			    iBankTask.addCRFCenterCode();
				iTimeTask.addTaskLog(BusinessQuartzType.Other, TimeTaskType.获取全国所有已上线的中心,new Date(),true,null);
			}catch (Exception ee){
				if(ee instanceof ErrorException){
				    iTimeTask.addTaskLog(BusinessQuartzType.Other, TimeTaskType.获取全国所有已上线的中心,new Date(),false,((ErrorException)ee).getMsg()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}else {
					iTimeTask.addTaskLog(BusinessQuartzType.Other, TimeTaskType.获取全国所有已上线的中心,new Date(),false, ee.getMessage()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}
			}
		}
	}
	/**
	 * 催缴定时
	 */
//	class DoPayCallTask implements Runnable {
//		@Override
//		public void run() {
//			System.out.println("催缴定时");
//			try{
//				collectionTask.doPayCallTask();
//				iTimeTask.addTaskLog(BusinessQuartzType.Collection, TimeTaskType.催缴定时,new Date(),true,null);
//			}catch (Exception ee){
//				if(ee instanceof ErrorException){
//					iTimeTask.addTaskLog(BusinessQuartzType.Collection, TimeTaskType.催缴定时,new Date(),false,((ErrorException)ee).getMsg()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
//				}else {
//					iTimeTask.addTaskLog(BusinessQuartzType.Collection, TimeTaskType.催缴定时,new Date(),false, ee.getMessage()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
//				}
//			}
//		}
//	}
	/**
	 * 个人年终结息
	 */
	class BalanceInterestFinal implements Runnable {
		@Override
		public void run() {
			System.out.println("个人年终结息");
			try{
				collectionTask.balanceInterestFinal();
				iTimeTask.addTaskLog(BusinessQuartzType.Finance, TimeTaskType.个人年终结息,new Date(),true,null);
			}catch (Exception ee){
				if(ee instanceof ErrorException){
					iTimeTask.addTaskLog(BusinessQuartzType.Finance, TimeTaskType.个人年终结息,new Date(),false,((ErrorException)ee).getMsg()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}else {
					iTimeTask.addTaskLog(BusinessQuartzType.Finance, TimeTaskType.个人年终结息,new Date(),false, ee.getMessage()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}
			}
		}
	}
	/**
	 * 提取入账失败定时任务（每天执行一次）
	 */
	class ClearFailWithdrawlRecord implements Runnable {
		@Override
		public void run() {
			System.out.println("提取入账失败定时任务（每天执行一次）");
			try{
				withdrawlTasks.withdrawlTask("");
				iTimeTask.addTaskLog(BusinessQuartzType.WithDrawl, TimeTaskType.提取入账失败定时任务,new Date(),true,null);
			}catch (Exception ee){
				if(ee instanceof ErrorException){
					iTimeTask.addTaskLog(BusinessQuartzType.WithDrawl, TimeTaskType.提取入账失败定时任务,new Date(),false,((ErrorException)ee).getMsg()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}else {
					iTimeTask.addTaskLog(BusinessQuartzType.WithDrawl, TimeTaskType.提取入账失败定时任务,new Date(),false, ee.getMessage()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}
			}
		}
	}
	/**
	 * 每月月初03：00触发，结账操作，生成科目余额表
	 */
	class CheckVoucher implements Runnable {
		@Override
		public void run() {
			System.out.println("每月月初03：00触发，结账操作，生成科目余额表");
			try{
				iTimedFinanceTask.checkVoucher();
				iTimeTask.addTaskLog(BusinessQuartzType.Finance, TimeTaskType.生成科目余额表,new Date(),true,null);
			}catch (Exception ee){
				if(ee instanceof ErrorException){
					iTimeTask.addTaskLog(BusinessQuartzType.Finance, TimeTaskType.生成科目余额表,new Date(),false,((ErrorException)ee).getMsg()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}else {
					iTimeTask.addTaskLog(BusinessQuartzType.Finance, TimeTaskType.生成科目余额表,new Date(),false, ee.getMessage()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}
			}
		}
	}
	/**
	 * 计算职工公积金利息，并生成结算凭证
	 * 每月月末00：00点触发，
	 */
	class CreateMonthInterestVouncher implements Runnable {
		@Override
		public void run() {
			System.out.println("计算职工公积金利息，并生成结算凭证");
			try{
				iTimedFinanceTask.checkoutMonthInterest();
				iTimeTask.addTaskLog(BusinessQuartzType.Finance, TimeTaskType.计算职工公积金利息,new Date(),true,null);
			}catch (Exception ee){
				if(ee instanceof ErrorException){
					iTimeTask.addTaskLog(BusinessQuartzType.Finance, TimeTaskType.计算职工公积金利息,new Date(),false,((ErrorException)ee).getMsg()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}else {
					iTimeTask.addTaskLog(BusinessQuartzType.Finance, TimeTaskType.计算职工公积金利息,new Date(),false, ee.getMessage()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}
			}
		}
	}
	/**
	 * 每年6月1日03：00执行，结算公积金利息，并生成结算凭证
	 */
	class SettlementInterestVouncher implements Runnable {
		@Override
		public void run() {
			System.out.println("每年6月1日03：00执行，结算公积金利息，并生成结算凭证");
			try{
				iTimedFinanceTask.checkoutYearInterest(BigDecimal.ZERO);
				iTimeTask.addTaskLog(BusinessQuartzType.Finance, TimeTaskType.结算公积金利息,new Date(),true,null);
			}catch (Exception ee){
				if(ee instanceof ErrorException){
					iTimeTask.addTaskLog(BusinessQuartzType.Finance, TimeTaskType.结算公积金利息,new Date(),false,((ErrorException)ee).getMsg()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}else {
					iTimeTask.addTaskLog(BusinessQuartzType.Finance, TimeTaskType.结算公积金利息,new Date(),false, ee.getMessage()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}
			}
		}
	}
	/**
	 * 新增会计期间
	 * 每年1月1日,00：30点触发
	 */
	class AddAccountPeriod implements Runnable {
		@Override
		public void run() {
			System.out.println("新增会计期间");
			try{
				iTimedFinanceTask.addAccountPeriod();
				iTimeTask.addTaskLog(BusinessQuartzType.Finance, TimeTaskType.新增会计期间,new Date(),true,null);
			}catch (Exception ee){
				if(ee instanceof ErrorException){
					iTimeTask.addTaskLog(BusinessQuartzType.Finance, TimeTaskType.新增会计期间,new Date(),false,((ErrorException)ee).getMsg()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}else {
					iTimeTask.addTaskLog(BusinessQuartzType.Finance, TimeTaskType.新增会计期间,new Date(),false, ee.getMessage()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}
			}
		}
	}
	/**
	 * 每年1月1日　05：00　触发，年度结转增值收益，并生成凭证
	 */
	class SetIncomexpenseYear implements Runnable {
		@Override
		public void run() {
			System.out.println("每年1月1日　05：00　触发，年度结转增值收益，并生成凭证");
			try{
				iTimedFinanceTask.checkoutBenefits();
				iTimeTask.addTaskLog(BusinessQuartzType.Finance, TimeTaskType.年度结转增值收益,new Date(),true,null);
			}catch (Exception ee){
				if(ee instanceof ErrorException){
					iTimeTask.addTaskLog(BusinessQuartzType.Finance, TimeTaskType.年度结转增值收益,new Date(),false,((ErrorException)ee).getMsg()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}else {
					iTimeTask.addTaskLog(BusinessQuartzType.Finance, TimeTaskType.年度结转增值收益,new Date(),false, ee.getMessage()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}
			}
		}
	}
	/**
	 * 每月月初04：00触发，在结账之后触发，期末业务收入结转/期末业务支出结转
	 */
	class SetBusinessEndIncomeAndExpenditure implements Runnable {
		@Override
		public void run() {
			System.out.println("每月月初04：00触发，在结账之后触发，期末业务收入结转/期末业务支出结转");
			try{
				iTimedFinanceTask.setBusinessEndIncomeAndExpenditure();
				iTimeTask.addTaskLog(BusinessQuartzType.Finance, TimeTaskType.期末业务收支结转,new Date(),true,null);
			}catch (Exception ee){
				if(ee instanceof ErrorException){
					iTimeTask.addTaskLog(BusinessQuartzType.Finance, TimeTaskType.期末业务收支结转,new Date(),false,((ErrorException)ee).getMsg()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}else {
					iTimeTask.addTaskLog(BusinessQuartzType.Finance, TimeTaskType.期末业务收支结转,new Date(),false, ee.getMessage()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}
			}
		}
	}
	/**
	 * 新增住房公积金银行存款
	 * 每月月初5:40分触发
	 */
	class AddHousingfundBankBalance implements Runnable {
		@Override
		public void run() {
			System.out.println("新增住房公积金银行存款");
			try{
				iTimedFinanceTask.addHousingfundBankBalance();
				iTimeTask.addTaskLog(BusinessQuartzType.Finance, TimeTaskType.新增住房公积金银行存款,new Date(),true,null);
			}catch (Exception ee){
				if(ee instanceof ErrorException){
					iTimeTask.addTaskLog(BusinessQuartzType.Finance, TimeTaskType.新增住房公积金银行存款,new Date(),false,((ErrorException)ee).getMsg()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}else {
					iTimeTask.addTaskLog(BusinessQuartzType.Finance, TimeTaskType.新增住房公积金银行存款,new Date(),false, ee.getMessage()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}
			}
		}
	}
	/**
	 * 更新定期余额
	 * 每天01:00触发
	 */
//	class UpdateFixedBalance implements Runnable {
//		@Override
//		public void run() {
//			System.out.println("更新定期余额");
//			try{
//				iTimedFinanceTask.updateFixedBalance();
//				iTimeTask.addTaskLog(BusinessQuartzType.Finance, TimeTaskType.更新定期余额,new Date(),true,null);
//			}catch (Exception ee){
//				if(ee instanceof ErrorException){
//					iTimeTask.addTaskLog(BusinessQuartzType.Finance, TimeTaskType.更新定期余额,new Date(),false,((ErrorException)ee).getMsg()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
//				}else {
//					iTimeTask.addTaskLog(BusinessQuartzType.Finance, TimeTaskType.更新定期余额,new Date(),false, ee.getMessage()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
//				}
//			}
//		}
//	}
	/**
	 * 计提定期利息收入
	 * 每月月末触发
	 */
	class CkzhFixedIntIncome implements Runnable {
		@Override
		public void run() {
			System.out.println("计提定期利息收入");
			try{
				iTimedFinanceTask.fixedIntIncome();
				iTimeTask.addTaskLog(BusinessQuartzType.Finance, TimeTaskType.计提定期利息收入,new Date(),true,null);
			}catch (Exception ee){
				if(ee instanceof ErrorException){
					iTimeTask.addTaskLog(BusinessQuartzType.Finance, TimeTaskType.计提定期利息收入,new Date(),false,((ErrorException)ee).getMsg()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}else {
					iTimeTask.addTaskLog(BusinessQuartzType.Finance, TimeTaskType.计提定期利息收入,new Date(),false, ee.getMessage()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}
			}
		}
	}

	/**
	 * 定时修改政策信息
	 */
	class UpdateDQZ implements Runnable {
		@Override
		public void run() {
			System.out.println("定时修改政策信息");
			try{
				policyTaskService.updateDQZ();
				iTimeTask.addTaskLog(BusinessQuartzType.Other, TimeTaskType.定时修改政策信息,new Date(),true,null);
			}catch (Exception ee){
				if(ee instanceof ErrorException){
					iTimeTask.addTaskLog(BusinessQuartzType.Other, TimeTaskType.定时修改政策信息,new Date(),false,((ErrorException)ee).getMsg()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}else {
					iTimeTask.addTaskLog(BusinessQuartzType.Other, TimeTaskType.定时修改政策信息,new Date(),false, ee.getMessage()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}
			}
		}
	}

	/**
	 * 还款计划
	 */
	class Repaymentjihua implements Runnable {
		@Override
		public void run() {
			System.out.println("还款计划");
			try{
				synchronized (lock){
				iloanTaskService.rehuankuanjihua();
				}
				iTimeTask.addTaskLog(BusinessQuartzType.Loan, TimeTaskType.还款计划,new Date(),true,null);
			}catch (Exception ee){
				if(ee instanceof ErrorException){
					iTimeTask.addTaskLog(BusinessQuartzType.Loan, TimeTaskType.还款计划,new Date(),false,((ErrorException)ee).getMsg()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}else {
					iTimeTask.addTaskLog(BusinessQuartzType.Loan, TimeTaskType.还款计划,new Date(),false, ee.getMessage()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}
			}
		}
	}

	/**
	 * 正常还款
	 */
	class Repayment implements Runnable {
		@Override
		public void run() {
			System.out.println("正常还款");
			try{
				synchronized (lock){
				iloanTaskService.repayment(null,null,null);
				}
				iTimeTask.addTaskLog(BusinessQuartzType.Loan, TimeTaskType.正常还款,new Date(),true,null);
			}catch (Exception ee){
				if(ee instanceof ErrorException){
					iTimeTask.addTaskLog(BusinessQuartzType.Loan, TimeTaskType.正常还款,new Date(),false,((ErrorException)ee).getMsg()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}else {
					iTimeTask.addTaskLog(BusinessQuartzType.Loan, TimeTaskType.正常还款,new Date(),false, ee.getMessage()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}
			}
		}
	}

	/**
	 * 正常还款转逾期定时监控
	 */
	class OverdueRepaymentChange implements Runnable {
		@Override
		public void run() {
			System.out.println("正常还款转逾期定时监控");
			try{
				synchronized (lock){
					iloanTaskService.overdueRepaymentChange();
				}
				iTimeTask.addTaskLog(BusinessQuartzType.Loan, TimeTaskType.正常还款转逾期定时监控,new Date(),true,null);
			}catch (Exception ee){
				if(ee instanceof ErrorException){
					iTimeTask.addTaskLog(BusinessQuartzType.Loan, TimeTaskType.正常还款转逾期定时监控,new Date(),false,((ErrorException)ee).getMsg()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}else {
					iTimeTask.addTaskLog(BusinessQuartzType.Loan, TimeTaskType.正常还款转逾期定时监控,new Date(),false, ee.getMessage()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}
			}
		}
	}

	/**
	 * 扣款已发送到账通知查询
	 */
	class DebitSend implements Runnable {
		@Override
		public void run() {
			System.out.println("扣款已发送到账通知查询");
			try{
				synchronized (lock){
				iloanTaskService.debitSend();
				}
				iTimeTask.addTaskLog(BusinessQuartzType.Loan, TimeTaskType.扣款已发送到账通知查询,new Date(),true,null);
			}catch (Exception ee){
				if(ee instanceof ErrorException){
					iTimeTask.addTaskLog(BusinessQuartzType.Loan, TimeTaskType.扣款已发送到账通知查询,new Date(),false,((ErrorException)ee).getMsg()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}else {
					iTimeTask.addTaskLog(BusinessQuartzType.Loan, TimeTaskType.扣款已发送到账通知查询,new Date(),false, ee.getMessage()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}
			}
		}
	}

	/**
	 * 剩余期数
	 */
	class RemainingPeriod implements Runnable {
		@Override
		public void run() {
			System.out.println("剩余期数");
			try{
				synchronized (lock){
				iloanTaskService.remainingPeriod();
				}
				iTimeTask.addTaskLog(BusinessQuartzType.Loan, TimeTaskType.剩余期数,new Date(),true,null);
			}catch (Exception ee){
				if(ee instanceof ErrorException){
					iTimeTask.addTaskLog(BusinessQuartzType.Loan, TimeTaskType.剩余期数,new Date(),false,((ErrorException)ee).getMsg()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}else {
					iTimeTask.addTaskLog(BusinessQuartzType.Loan, TimeTaskType.剩余期数,new Date(),false, ee.getMessage()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}
			}
		}
	}

	/**
	 * 还款申请定时发送
	 */
	class OverdueRepaymenTiming implements Runnable {
		@Override
		public void run() {
			System.out.println("还款申请定时发送");
			try{
				synchronized (lock){
				iloanTaskService.overdueRepaymenTiming();
				}
				iTimeTask.addTaskLog(BusinessQuartzType.Loan, TimeTaskType.还款申请定时发送,new Date(),true,null);
			}catch (Exception ee){
				if(ee instanceof ErrorException){
					iTimeTask.addTaskLog(BusinessQuartzType.Loan, TimeTaskType.还款申请定时发送,new Date(),false,((ErrorException)ee).getMsg()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}else {
					iTimeTask.addTaskLog(BusinessQuartzType.Loan, TimeTaskType.还款申请定时发送,new Date(),false, ee.getMessage()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}
			}
		}
	}

	/**
	 * 自动逾期记录扣划
	 */
	class OverdueAutomatic implements Runnable {
		@Override
		public void run() {
			System.out.println("自动逾期记录扣划");
			try{
				synchronized (lock){
					iloanTaskService.overdueAutomatic("104");
				}
				iTimeTask.addTaskLog(BusinessQuartzType.Loan, TimeTaskType.自动逾期记录扣划,new Date(),true,null);
			}catch (Exception ee){
				if(ee instanceof ErrorException){
					iTimeTask.addTaskLog(BusinessQuartzType.Loan, TimeTaskType.自动逾期记录扣划,new Date(),false,((ErrorException)ee).getMsg()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}else {
					iTimeTask.addTaskLog(BusinessQuartzType.Loan, TimeTaskType.自动逾期记录扣划,new Date(),false, ee.getMessage()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}
			}
		}
	}

	/**
	 * 全市各区暂收未分摊
	 */
	class SaveZSWFTTimeTask implements Runnable {
		@Override
		public void run() {
			System.out.println("全市各区暂收未分摊");
			try{
				iFinanceReportService.saveZSWFTTimeTask();
				iTimeTask.addTaskLog(BusinessQuartzType.Finance, TimeTaskType.全市各区暂收未分摊,new Date(),true,null);
			}catch (Exception ee){
				System.out.println(ee);

				if(ee instanceof ErrorException){
					iTimeTask.addTaskLog(BusinessQuartzType.Finance, TimeTaskType.全市各区暂收未分摊,new Date(),false,((ErrorException)ee).getMsg()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());
				}else {
					iTimeTask.addTaskLog(BusinessQuartzType.Finance, TimeTaskType.全市各区暂收未分摊,new Date(),false, ee.getMessage()+"  "+ee.getStackTrace()[0].getFileName()+ "  "+ee.getStackTrace()[0].getClassName()+" "+ee.getStackTrace()[0].getMethodName()+" "+ee.getStackTrace()[0].getLineNumber());

			}
		}
	}
	}
	public Runnable getSaveZSWFTTimeTask() {
		Runnable task = new SaveZSWFTTimeTask();
		return task;
	}
	public Runnable getAddChgNoTask() {
		Runnable task = new addChgNo();
		return task;
	}
	public Runnable getAddCRFCenterCodeTask() {
		Runnable task = new addCRFCenterCode();
		return task;
	}
	public Runnable getTestTask() {
		Runnable task = new Test();
		return task;
	}

	public Runnable getActionTask() {
		Runnable task = new Action();
		return task;
	}
	public Runnable getDoCollectionTask() {
		Runnable task = new DoCollectionTask();
		return task;
	}
//	public Runnable getDoPayCallTask() {
//		Runnable task = new DoPayCallTask();
//		return task;
//	}
	public Runnable getBalanceInterestFinalTask() {
		Runnable task = new BalanceInterestFinal();
		return task;
	}
	public Runnable getClearFailWithdrawlRecordTask() {
		Runnable task = new ClearFailWithdrawlRecord();
		return task;
	}
	public Runnable getCheckVoucherTask() {
		Runnable task = new CheckVoucher();
		return task;
	}
	public Runnable getCreateMonthInterestVouncherTask() {
		Runnable task = new CreateMonthInterestVouncher();
		return task;
	}
	public Runnable getSettlementInterestVouncherTask() {
		Runnable task = new SettlementInterestVouncher();
		return task;
	}
	public Runnable getAddAccountPeriodTask() {
		Runnable task = new AddAccountPeriod();
		return task;
	}
	public Runnable getSetIncomexpenseYearTask() {
		Runnable task = new SetIncomexpenseYear();
		return task;
	}
	public Runnable getSetBusinessEndIncomeAndExpenditureTask() {
		Runnable task = new SetBusinessEndIncomeAndExpenditure();
		return task;
	}
	public Runnable getAddHousingfundBankBalanceTask() {
		Runnable task = new AddHousingfundBankBalance();
		return task;
	}
//	public Runnable getUpdateFixedBalanceTask() {
//		Runnable task = new UpdateFixedBalance();
//		return task;
//	}
	public Runnable getCkzhFixedIntIncomeTask() {
		Runnable task = new CkzhFixedIntIncome();
		return task;
	}
	public Runnable getUpdateDQZTask() {
		Runnable task = new UpdateDQZ();
		return task;
	}
	public Runnable getRepaymentjihuaTask() {
		Runnable task = new Repaymentjihua();
		return task;
	}
	public Runnable getRepaymentTask() {
		Runnable task = new Repayment();
		return task;
	}
	public Runnable getOverdueRepaymentChangeTask() {
		Runnable task = new OverdueRepaymentChange();
		return task;
	}
	public Runnable getDebitSendTask() {
		Runnable task = new DebitSend();
		return task;
	}
	public Runnable getRemainingPeriodTask() {
		Runnable task = new RemainingPeriod();
		return task;
	}
	public Runnable getOverdueRepaymenTimingTask() {
		Runnable task = new OverdueRepaymenTiming();
		return task;
	}
	public Runnable getOverdueAutomaticTask() {
		Runnable task = new OverdueAutomatic();
		return task;
	}
	public Runnable getDoSMSPayCallTask() {
		Runnable task = new DoSMSPayCallTask();
		return task;
	}



	public Runnable getTask(String Task) throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		Runnable task = (Runnable) this.getClass().getMethod("get" + Task + "Task", null).invoke(this, null);
		return task;
	}
}
