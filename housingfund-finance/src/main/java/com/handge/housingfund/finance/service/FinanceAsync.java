package com.handge.housingfund.finance.service;

import com.google.gson.Gson;
import com.handge.housingfund.common.service.finance.IFinanceAsync;
import com.handge.housingfund.common.service.finance.IVoucherManagerService;
import com.handge.housingfund.common.service.finance.model.ReconciliationModel;
import com.handge.housingfund.common.service.finance.model.SubjectsBalance;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.RedisCache;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.database.dao.IStCollectionPersonalAccountDAO;
import com.handge.housingfund.database.dao.IStCollectionPersonalBusinessDetailsDAO;
import com.handge.housingfund.database.dao.IStCollectionUnitAccountDAO;
import com.handge.housingfund.database.dao.IStFinanceRecordingVoucherDAO;
import com.handge.housingfund.finance.utils.FinanceComputeHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class FinanceAsync implements IFinanceAsync {

    @Autowired
    IVoucherManagerService iVoucherManagerService;

    @Autowired
    private IStFinanceRecordingVoucherDAO financeRecordingVoucherDAO;

    @Autowired
    IStCollectionPersonalAccountDAO iStCollectionPersonalAccountDAO;

    @Autowired
    IStCollectionUnitAccountDAO iStCollectionUnitAccountDAO;

    @Autowired
    IStCollectionPersonalBusinessDetailsDAO iStCollectionPersonalBusinessDetailsDAO;

    public static Gson gson = new Gson();

    RedisCache redisCache = RedisCache.getRedisCacheInstance();

    @Override
    public void getReconciliationAsync(String date) {
        try {
            JedisCluster redis = redisCache.getJedisCluster();
            List<ReconciliationModel> res = new ArrayList<>();
            try {
                //region 借方=贷方
                List<Object[]> list = financeRecordingVoucherDAO.getJDHJ();
                if (list.size() != 1) {
                    throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询错误");
                }
                BigDecimal JFHJ = new BigDecimal(list.get(0)[0].toString());
                BigDecimal DFHJ = new BigDecimal(list.get(0)[1].toString());

                res.add(getReconciliationModel("借方 = 贷方", JFHJ, DFHJ));
                //endregion

            } catch (Exception e) {
                res.add(getReconciliationModel("借方 = 贷方", e.getMessage()));
            }
            try {
                //region  资产 = 负债 + 所有者权益
                System.out.println("开始计算 ->资产 = 负债 + 所有者权益");
                List<SubjectsBalance> subjectsBalances = iVoucherManagerService.getSubjectsCollect(date);

                BigDecimal gjjck = FinanceComputeHelper.searchSubjectsBalanceNotDb(subjectsBalances, SubjectEnum.住房公积金存款.getId());

                BigDecimal zzsyck = FinanceComputeHelper.searchSubjectsBalanceNotDb(subjectsBalances, SubjectEnum.增值收益存款.getId());

                BigDecimal yslx = FinanceComputeHelper.searchSubjectsBalanceNotDb(subjectsBalances, SubjectEnum.应收利息.getId());

                BigDecimal qtyslx = FinanceComputeHelper.searchSubjectsBalanceNotDb(subjectsBalances, SubjectEnum.其他应收款.getId());

                BigDecimal wtdk = FinanceComputeHelper.searchSubjectsBalanceNotDb(subjectsBalances, SubjectEnum.委托贷款.getId());

                BigDecimal yqdk = FinanceComputeHelper.searchSubjectsBalanceNotDb(subjectsBalances, SubjectEnum.逾期贷款.getId());

                BigDecimal gjzq = FinanceComputeHelper.searchSubjectsBalanceNotDb(subjectsBalances, SubjectEnum.国家债券.getId());

                //资产合计
                BigDecimal zche = gjjck.add(zzsyck).add(yslx).add(qtyslx).add(wtdk).add(yqdk).add(gjzq);

                BigDecimal zfgjj = FinanceComputeHelper.searchSubjectsBalanceNotDb(subjectsBalances, SubjectEnum.住房公积金.getId());

                BigDecimal yflx = FinanceComputeHelper.searchSubjectsBalanceNotDb(subjectsBalances, SubjectEnum.应付利息.getId());

                BigDecimal qtyfk = FinanceComputeHelper.searchSubjectsBalanceNotDb(subjectsBalances, SubjectEnum.其他应付款.getId());

                BigDecimal zxyfk = FinanceComputeHelper.searchSubjectsBalanceNotDb(subjectsBalances, SubjectEnum.专项应付款.getId());


                //负债合计
                BigDecimal fzhj = zfgjj.add(yflx).add(qtyfk).add(zxyfk);

                BigDecimal dkfxzb = FinanceComputeHelper.searchSubjectsBalanceNotDb(subjectsBalances, SubjectEnum.贷款风险准备.getId());

                BigDecimal zzsy = FinanceComputeHelper.searchSubjectsBalanceNotDb(subjectsBalances, SubjectEnum.增值收益.getId());

                BigDecimal zzsyfp = FinanceComputeHelper.searchSubjectsBalanceNotDb(subjectsBalances, SubjectEnum.增值收益分配.getId());

                //权益合计
                BigDecimal jzchj = dkfxzb.add(zzsy).add(zzsyfp);

                //业务收入
                BigDecimal ywsr = FinanceComputeHelper.searchSubjectsBalanceNotDb(subjectsBalances, SubjectEnum.业务收入.getId());

                //业务支出
                BigDecimal ywzc = FinanceComputeHelper.searchSubjectsBalanceNotDb(subjectsBalances, SubjectEnum.业务支出.getId());

                BigDecimal fzjzchj = fzhj.add(jzchj).add(ywsr).subtract(ywzc);

                res.add(getReconciliationModel("资产 = 负债 + 所有者权益", zche, fzjzchj));
                //endregion
            } catch (Exception e) {
                res.add(getReconciliationModel("资产 = 负债 + 所有者权益", e.getMessage()));
            }

            BigDecimal gryehj = BigDecimal.ZERO;
            try {
                //region  个人余额合计 = 单位账户余额合计
                System.out.println("开始计算 ->个人余额合计 = 单位账户余额合计");

                String sumsqlgryehj = "SELECT SUM(GRZHYE) FROM st_collection_personal_account";
                long gryehjstart = System.currentTimeMillis();
                //个人余额合计
                gryehj = iStCollectionPersonalAccountDAO.getGrzhyeHj();
                System.out.println("个人余额合计耗时：" + (System.currentTimeMillis() - gryehjstart) + "毫秒");

                long dwzhyehjstart = System.currentTimeMillis();
                //单位账户余额合计
                BigDecimal dwzhyehj = iStCollectionUnitAccountDAO.getDwzhyeHJ();
                System.out.println("单位账户余额合计耗时：" + (System.currentTimeMillis() - dwzhyehjstart) + "毫秒");

                res.add(getReconciliationModel("个人余额合计 = 单位账户余额合计", gryehj, dwzhyehj));
                //endregion
            } catch (Exception e) {
                res.add(getReconciliationModel("个人余额合计 = 单位账户余额合计", e.getMessage()));
            }
            try {
                //region  个人明细发生额合计 = 个人账户余额合计
                System.out.println("开始计算 ->个人明细发生额合计 = 个人账户余额合计");

                String sumsqlgrmxfsehj = "SELECT SUM(FSE) FROM st_collection_personal_business_details";
                long grmxfsehjstart = System.currentTimeMillis();
                //个人明细发生额合计
                BigDecimal grmxfsehj = iStCollectionPersonalBusinessDetailsDAO.getGrywmxHj();
                System.out.println("个人明细发生额合计耗时：" + (System.currentTimeMillis() - grmxfsehjstart) / 1000 + "秒");

                res.add(getReconciliationModel("个人明细发生额合计 = 个人账户余额合计", grmxfsehj, gryehj));

                //endregion
            } catch (Exception e) {
                res.add(getReconciliationModel("个人明细发生额合计 = 个人账户余额合计", e.getMessage()));
            }
            redis.setex("dz_" + date, 60, gson.toJson(res));
            redis.del("dz_state_" + date);
            redis.close();
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }

    private ReconciliationModel getReconciliationModel(String ssx, BigDecimal a, BigDecimal b) {
        ReconciliationModel model = new ReconciliationModel();
        model.setMsgs(new ArrayList<>());
        model.setSSX(ssx);
        if (a.compareTo(b) == 0) {
            model.setSFPH(true);
            model.setSSJG("平衡 (" + a.toString() + " = " + b.toString() + ")");
        } else if (a.compareTo(b) < 0) {
            model.setSFPH(false);
            model.setSSJG("不平衡 (" + a.toString() + " - " + b.toString() + " = " + a.subtract(b).toString() + ")");
        } else {
            model.setSFPH(false);
            model.setSSJG("不平衡 (" + a.toString() + " - " + b.toString() + " = " + a.subtract(b).toString() + ")");
        }
        return model;
    }

    private ReconciliationModel getReconciliationModel(String ssx, String error) {
        ReconciliationModel model = new ReconciliationModel();
        model.setMsgs(new ArrayList<>());
        model.setSSX(ssx);
        model.setSFPH(false);
        model.setSSJG(error);
        return model;
    }

}
