package com.handge.housingfund.collection.utils;

import com.handge.housingfund.common.service.ISaveAuditHistory;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.StCollectionUnitAccount;
import com.handge.housingfund.database.entities.StCommonUnit;
import com.handge.housingfund.database.entities.StSettlementSpecialBankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by 凡 on 2017/9/6.
 * 业务相关的工具类
 */
@Component
public class BusUtils {

    private static BusUtils busUtils;

    @Autowired
    private IStCommonPersonDAO personDAO;
    @Autowired
    private IStCommonUnitDAO unitDAO;
    @Autowired
    private ICCollectionUnitRemittanceViceDAO remittanceDAO;
    @Autowired
    private ICCollectionUnitPaybackViceDAO paybackDAO;
    @Autowired
    private ISaveAuditHistory iSaveAuditHistory;
    @Autowired
    private IStSettlementDaybookDAO stSettlementDaybookDAO;


    @PostConstruct
    public void init(){
        busUtils = this;
        busUtils.personDAO = this.personDAO;
        busUtils.unitDAO = this.unitDAO;
        busUtils.remittanceDAO = this.remittanceDAO;
        busUtils.iSaveAuditHistory = this.iSaveAuditHistory;
        busUtils.stSettlementDaybookDAO = this.stSettlementDaybookDAO;
    }

    private static IStCommonUnitDAO getUnitDAO(){
        return busUtils.unitDAO;
    }

    private static IStCommonPersonDAO getPersonDAO(){
        return busUtils.personDAO;
    }

    private static ICCollectionUnitRemittanceViceDAO getRemittanceDAO(){
        return busUtils.remittanceDAO;
    }

    private static ISaveAuditHistory getISaveAuditHistory(){
        return busUtils.iSaveAuditHistory;
    }


    /**
     * 注意：简单处理，不可靠（引起错误或会掩盖错误）
     * 更新单位的账户信息，用于清册类业务及汇缴相关
     * //TODO 账户状态错误会产生异常,前置条件：冻结将不改变个人账户状态
     *   否则将出现数据错误
     * //TODO 是否新增单位流水表、或个人流水表记录流水变化
     * //单位账户余额更新前都要验证：个人账户余额之和与单位账户余额是否相同
     */
    public static void refreshUnitAcount(String dwzh){
        long unitPersonNomal = getPersonDAO().getUnitPersonCount(dwzh, "01");
        long unitPersonSeal = getPersonDAO().getUnitPersonCount(dwzh, "02");
        if(ComUtils.isEmpty(unitPersonNomal)){
            unitPersonNomal = 0;
        }
        if(ComUtils.isEmpty(unitPersonSeal)){
            unitPersonSeal = 0;
        }
        StCommonUnit unit = getUnitDAO().getUnit(dwzh);
        StCollectionUnitAccount unitAccount = unit.getCollectionUnitAccount();
        unitAccount.setDwjcrs(unitPersonNomal);
        unitAccount.setDwfcrs(unitPersonSeal);
        /*BigDecimal unitAmountCount = getUnitDAO().getUnitAmountCount(dwzh);
        if(ComUtils.isEmpty(unitAmountCount)){
            unitAmountCount = BigDecimal.ZERO;
        }
        unitAccount.setDwzhye(unitAmountCount);*/
        getUnitDAO().update(unit);
    }

    public static int getConsecutiveDepositMonths(String grzh){
        //降序得到该人的汇缴历史
        List<String> hbjnys = getRemittanceDAO().getConsecutiveDepositMonths(grzh);
        if(hbjnys == null || hbjnys.size() == 0){
            return 0;
        }
        Date month = ComUtils.getfirstDayOfMonth(new Date());
        Date hbjny = ComUtils.parseToDate(hbjnys.get(0),"yyyyMM");
        //最近一次的汇补缴年月，如果比上个月的时间小，返回0
        if(ComUtils.getNextMonth(hbjny).compareTo(month) < 0){
            return 0;
        }
        //计算连续汇缴的次数
        int count = 0;
        for(String hbjny2 : hbjnys){
            Date hbjny2Date = ComUtils.parseToDate(hbjny2,"yyyyMM");
            if(ComUtils.getNextMonth(hbjny2Date).compareTo(hbjny) < 0){
                break;
            }
            count++;
            hbjny = hbjny2Date;
        }
        return count;
    }

    /**
     *获取单位应汇缴年月
     * 缴至年月的下一个月
     * 缴至年月为空时，为单位的首次汇缴年月
     * 返回格式 yyyy-MM
     */
    public static String getDWYHJNY(String dwzh){
        StCommonUnit unit = getUnitDAO().getUnit(dwzh);
        return getDWYHJNY(unit);
    }

    public static String getDWYHJNY(StCommonUnit unit){
        String dwyhjny;
        AssertUtils.notEmpty(unit,"单位不存在！");
        StCollectionUnitAccount unitAccount = unit.getCollectionUnitAccount();
        Date jzny = ComUtils.parseToDate(unitAccount.getJzny(),"yyyyMM");
        if(!ComUtils.isEmpty(jzny)){
            dwyhjny = ComUtils.parseToString(ComUtils.getNextMonth(jzny),"yyyy-MM");
        }else{
            if(unit.getExtension().getDwschjny()==null) throw new ErrorException("单位首次汇缴年月为空，请修改单位信息");
            dwyhjny = ComUtils.parseToYYYYMM2(unit.getExtension().getDwschjny());
        }
        return dwyhjny;
    }





    /**
     * 根据基数、比例计算缴存额
     * 保留2位小数，四舍五入
     */
    public static BigDecimal computeDeposit(BigDecimal grjs,BigDecimal jcbl){
        BigDecimal result = grjs.multiply(jcbl);
        result = result.divide(BigDecimal.ONE,2,BigDecimal.ROUND_HALF_UP);
        return result;
    }

    /**
     *检查单位是否正在汇缴中,存在则抛出运行时异常
     *
     */
    public static void checkRemittanceDoing(String dwzh){
        String ywlsh = getRemittanceDAO().getYWLSHIsExistDoing(dwzh);
        if(!ComUtils.isEmpty(ywlsh)){
            throw new ErrorException("当前职工所在单位:"+dwzh+"，存在未办结的汇缴业务:"+ywlsh);
        }
    }


    public static void saveAuditHistory(String ywlsh, TokenContext tokenContext, String ywlx, String caozuo) {
        getISaveAuditHistory().saveNormalBusiness(ywlsh,tokenContext,ywlx,caozuo);
    }

    public static void saveAuditHistory(String ywlsh, String ywlx, String caozuo) {
        getISaveAuditHistory().saveNormalBusiness(ywlsh,ywlx,caozuo);
    }

    public static StSettlementSpecialBankAccount getBankAcount(String yhhb,String zhxz){
        StSettlementSpecialBankAccount bankAccount = busUtils.stSettlementDaybookDAO.getSettlementDaybook(yhhb, zhxz);
        AssertUtils.notEmpty(bankAccount,"银行行别："+yhhb+"账户性质："+zhxz+"没有对应的银行账户！");
        return bankAccount;
    }

    /**
     * 检测职工在单位下是否存在汇缴、补缴在途
     * 如果存在抛出异常
     */
    public static void personDepositDoingCheck(String grzh,String dwzh){
        boolean exist1 = busUtils.remittanceDAO.checkIsExistDoing(grzh, dwzh);
        AssertUtils.isTrue(!exist1,"职工:"+grzh+"，存在未办结的汇缴。");
        boolean exist2 = busUtils.paybackDAO.checkIsExistDoing(grzh, dwzh);
        AssertUtils.isTrue(!exist2,"职工:"+grzh+"，存在未办结的补缴。");
    }

}
