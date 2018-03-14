package com.handge.housingfund.collection.service.common;

import com.handge.housingfund.collection.utils.ComUtils;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.collection.service.common.CollectionTask;
import com.handge.housingfund.common.service.collection.service.common.ICalculateInterest;
import com.handge.housingfund.common.service.collection.service.indiacctmanage.IndiAcctSeal;
import com.handge.housingfund.common.service.collection.service.indiacctmanage.IndiAcctUnseal;
import com.handge.housingfund.common.service.collection.service.unitdeposit.PersonRadix;
import com.handge.housingfund.common.service.collection.service.unitdeposit.UnitDepositRatio;
import com.handge.housingfund.common.service.collection.service.unitdeposit.UnitPayCall;
import com.handge.housingfund.common.service.collection.service.unitdeposit.UnitPayhold;
import com.handge.housingfund.common.service.others.ISMSCommon;
import com.handge.housingfund.common.service.sms.enums.SMSTemp;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.CCollectionTimedTask;
import com.handge.housingfund.database.entities.StCollectionPersonalBusinessDetails;
import com.handge.housingfund.database.entities.StCommonPerson;
import com.handge.housingfund.database.entities.StCommonUnit;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.apache.logging.log4j.LogManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by 凡 on 2017/10/19.
 */
@Component
public class CollectionTaskImpl implements CollectionTask {

    Logger logger = Logger.getLogger(CollectionTaskImpl.class.getName());

    @Autowired
    private IndiAcctSeal indiAcctSeal;
    @Autowired
    private IndiAcctUnseal indiAcctUnseal;
    @Autowired
    private PersonRadix personRadix;
    @Autowired
    private UnitDepositRatio unitDepositRatio;
    @Autowired
    private ICollectionTimedTaskDAO collectionTimedTaskDAO;
    @Autowired
    private UnitPayCall unitPayCall;
    @Autowired
    private ICalculateInterest calculateInterest;
    @Autowired
    private IStCommonPersonDAO commonPersonDAO;
    @Autowired
    private IStCommonUnitDAO commonUnitDAO;
    @Autowired
    private UnitPayhold unitPayhold;
    @Autowired
    private IStCollectionPersonalBusinessDetailsDAO collectionPersonalBusinessDetailsDAO;
    @Autowired
    private ISMSCommon ismsCommon;
    /**
     * 启封、封存、调基、调比业务
     * 使用乐观锁(CAS)的方式更新状态
     */
    @Override
    public void doCollectionTask() {
        //归集定时任务
        List<CCollectionTimedTask> collectionTasks = collectionTimedTaskDAO.getCollectionTask(ComUtils.getDQYF());
        for (CCollectionTimedTask task : collectionTasks) {
            try {
                //任务执行
                doTask(task);
                //执行后更新状态为执行成功
                refresh(task);
            } catch (Exception e) {
                logger.log(Level.WARNING, "归集定时任务执行失败！" + task.getYwms() + ",业务流水号为" + task.getYwlsh());
                //错误处理
                doErrer(task);
            }
        }
    }

    /**
     * CAS的方式更新,如果更新结果为0条记录，进入失败处理(之前的操作怎么办？)
     */
    private void refresh(CCollectionTimedTask task) {
        int count = collectionTimedTaskDAO.updateByCAS(task);
        if (count == 0) {
            throw new ErrorException("更新失败！更新的行数为0。");
        }
    }

    private void doErrer(CCollectionTimedTask task) {
        int zxcs = task.getZxcs();
        task.setZxzt("02");   //失败标识
        task.setZxcs(++zxcs);
        collectionTimedTaskDAO.update(task);
    }

    /**
     * 启封04、封存05、调基10、调比75
     */
    private void doTask(CCollectionTimedTask task) {
        String ywlx = task.getYwlx();
        String ywlsh = task.getYwlsh();
        if ("04".equals(ywlx)) {
            indiAcctUnseal.doUnsealTask(ywlsh);
        }
        if ("05".equals(ywlx)) {
            indiAcctSeal.doSealTask(ywlsh);
        }
        if("13".equals(ywlx)){
            unitPayhold.doUpdateUnitState(ywlsh);
        }
        /*if("10".equals(ywlx)){
            personRadix.doRadixTask(ywlsh);
        }*/
        if ("75".equals(ywlx)) {
            unitDepositRatio.doRatioTask(ywlsh);
        }
    }

    /**
     * 催缴业务定时产生催缴
     */
    public void doPayCallTask() {
        unitPayCall.doCreateUnitPayCall();
    }

    @Override
    public void balanceInterestFinal() {

        List<StCommonUnit> list_unit = DAOBuilder.instance(this.commonUnitDAO).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                criteria.createAlias("collectionUnitAccount","collectionUnitAccount");
                criteria.add(Restrictions.isNull("collectionUnitAccount.dwxhrq"));
            }
        }).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) { throw new ErrorException(e); }
        });


        BigDecimal total = BigDecimal.ZERO;

        for(StCommonUnit stCommonUnit :list_unit){

            try {

                List<StCommonPerson> list_person = DAOBuilder.instance(this.commonPersonDAO).extension(new IBaseDAO.CriteriaExtension() {
                    @Override
                    public void extend(Criteria criteria) {

                        criteria.createAlias("collectionPersonalAccount","collectionPersonalAccount");

                        criteria.createAlias("unit","unit");

                        criteria.add(Restrictions.eq("unit.dwzh",stCommonUnit.getDwzh()));

                        criteria.add(Restrictions.isNull("collectionPersonalAccount.xhrq"));
                    }
                }).getList(new DAOBuilder.ErrorHandler() {

                    @Override
                    public void error(Exception e) { throw new ErrorException(e); }
                });

                for(StCommonPerson commonPerson :list_person) {

                    total = total.add(new BigDecimal(calculateInterest.balanceInterestFinal(commonPerson.getGrzh()).getBalance()));
                }


            }catch (Exception e){
                // logger.log(Level.WARNING,"归集定时任务执行失败！"+task.getYwms()+",业务流水号为"+task.getYwlsh());

                e.printStackTrace();
            }

        }

        this.sendMessage();
    }


    private void sendMessage(){

        List<StCommonUnit>list_unit = DAOBuilder.instance(this.commonUnitDAO).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) { throw new ErrorException(e); }
        });

        for(StCommonUnit commonUnit:list_unit){

            List<StCollectionPersonalBusinessDetails>list_details = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>(){{

                this.put("person.unit.dwzh",commonUnit.getDwzh());
                this.put("gjhtqywlx", CollectionBusinessType.年终结息.getCode());

            }}).betweenDate(DateUtil.safeStr2Date("yyyy",String.valueOf(Calendar.getInstance().get(Calendar.YEAR))),DateUtil.safeStr2Date("yyyy",String.valueOf(Calendar.getInstance().get(Calendar.YEAR)+1))).getList(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) { throw new ErrorException(e);}
            });

            for (StCollectionPersonalBusinessDetails details:list_details){
                //region //短信
                try {
                    this.ismsCommon.sendSingleSMSWithTemp(details.getPerson().getSjhm(), SMSTemp.年度结息.getCode(),
                            new ArrayList<String>() {{
                                this.add(String.valueOf(details.getPerson().getXingMing()));
                                this.add(String.valueOf(details.getCreated_at().getMonth()+1));
                                this.add(String.valueOf(details.getCreated_at().getDate()));
                                this.add(String.valueOf(details.getFse()));
                                this.add(String.valueOf(details.getPerson().getCollectionPersonalAccount().getGrzhye()));
                            }}
                    );
                }catch (Exception e){
                    LogManager.getLogger(this.getClass()).info("年度结息短信发送失败:"+e.getMessage());
                }
                //endregion
            }

        }
    }


    /**
     * 对单位进行短信催缴:
     * 1.定时产生数据
     * 2.需要短信任务发送信息表，记录短信发送情况
     * 3.暂时设定每月一号发送，如何连续2月没缴存的单位将发送短信（一月一次）
     */
    @Override
    public void doSMSPayCall() {
        //得到超过2个月都未汇缴的数据
        Date date = ComUtils.getLastMonth(ComUtils.getLastMonth(new Date()));
        String yyyyMM = ComUtils.parseToString(date, "yyyyMM");
        //根据单位缴至年月进行查询
        List<Object[]> objs = commonUnitDAO.getSMSPayCall(yyyyMM);

        for(Object[] obj: objs){
            String jbrxm = (String)obj[0];
            String dwzh = (String)obj[1];
            String jzny = (String)obj[2];
            String jbrsjhm = (String)obj[3];

            String yuefen = ComUtils.getNextMonth(ComUtils.parseToYYYYMM2(jzny));
            yuefen = ComUtils.parseToYYYYMM(yuefen);

            ArrayList<String> list= new ArrayList<>();
            list.add(jbrxm);
            list.add(yuefen);
            this.ismsCommon.sendSingleSMSWithTemp(jbrsjhm,SMSTemp.汇缴逾期催缴.getCode(),list);
        }
    }

}
