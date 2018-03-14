package com.handge.housingfund.database.dao;

import com.handge.housingfund.common.service.finance.model.WithdrawlReportResult;
import com.handge.housingfund.database.entities.PageResults;
import com.handge.housingfund.database.entities.StCollectionPersonalBusinessDetails;
import com.handge.housingfund.database.entities.StCommonPerson;
import com.handge.housingfund.database.enums.ListDeleted;
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.enums.SearchOption;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface IStCollectionPersonalBusinessDetailsDAO extends IBaseDAO<StCollectionPersonalBusinessDetails> {

	public PageResults<StCollectionPersonalBusinessDetails> getReviewedList(HashMap<String, Object> filters, Date start,
																			Date end, String orderby, Order order, ListDeleted listDeleted, SearchOption searchOption,
																			int pageNo, int pageSize);

	public PageResults<StCollectionPersonalBusinessDetails> listByCZMC(HashMap<String, Object> filters, Date start,
			Date end, String orderby, Order order, ListDeleted listDeleted, SearchOption searchOption, int pageNo,
			int pageSize, List<String> czmcList);

	StCollectionPersonalBusinessDetails getByYwlsh(String ywlsh);

    boolean isExistDoingPersonBus(String grzh, String ywlx);

    boolean isCouldModifyPersonBus(String ywlsh);

    public void updateGRZH(String GRZH_current, String GRZH_original, StCommonPerson stCommonPerson);

	public BigDecimal getTotalBusinessFSE(Date date);

	void saveNormal(StCollectionPersonalBusinessDetails personalBusiness);

	public BigDecimal getTotalBusinessFSEBetweenDate(Date start,Date end);

	public BigDecimal getSumZHYE();
	/**
	 * 获取个人的流水信息：汇缴、补缴、错缴
	 */
	List<StCollectionPersonalBusinessDetails> getPersonDeposits(String grzh);

	List<StCollectionPersonalBusinessDetails> getPersonDepositsAfter(String grzh, String sxnyStr);

	List<StCollectionPersonalBusinessDetails> getPersonDepositsChange(String grzh);

	BigDecimal getGrywmxHj();

    boolean hasWithdrawl(String grzh);

	/**
	 * 查询指定年月的提取记录，并按照提取原因分类
	 * @param kssj
	 * @param jssj
	 * @return
	 */

	ArrayList<WithdrawlReportResult> getWithdrawlReport(Date kssj,Date jssj);

	/**
	 * 通过业务流水号->查询批次号->查询该批次号下的业务流水号集合
	 * @param ywlsh
	 * @return
	 */
	List<String> getYwlshListByPchByYwlsh(String ywlsh);

	/**
	 * 返回该人该月的缴存合计信息
	 * Object[]:grzh/dwyjcehj/gryjcehj/fsehj
	 */
    Object[] getPersonDepositSUM(String grzh, String cjny);

    List<String> getDepositContinuousMonth(String grzh);

    void updateDQYE(String id, BigDecimal dqye);
}