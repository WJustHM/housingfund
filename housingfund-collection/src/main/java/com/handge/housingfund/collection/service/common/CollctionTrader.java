package com.handge.housingfund.collection.service.common;

import com.handge.housingfund.collection.utils.AssertUtils;
import com.handge.housingfund.collection.utils.BusUtils;
import com.handge.housingfund.collection.utils.ComUtils;
import com.handge.housingfund.common.service.bank.bean.center.SingleCollectionIn;
import com.handge.housingfund.common.service.bank.bean.center.SingleCollectionOut;
import com.handge.housingfund.common.service.bank.bean.center.SinglePaymentIn;
import com.handge.housingfund.common.service.bank.bean.center.SinglePaymentOut;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadOut;
import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNotice;
import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNoticeFile;
import com.handge.housingfund.common.service.bank.ibank.IBank;
import com.handge.housingfund.common.service.collection.service.trader.ICollectionTrader;
import com.handge.housingfund.common.service.collection.service.unitdeposit.UnitPayWrong;
import com.handge.housingfund.common.service.collection.service.unitdeposit.UnitPayback;
import com.handge.housingfund.common.service.collection.service.unitdeposit.UnitRemittance;
import com.handge.housingfund.common.service.finance.IVoucherAutoService;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.database.dao.ICCollectionUnitDepositInventoryViceDAO;
import com.handge.housingfund.database.dao.IStCollectionUnitBusinessDetailsDAO;
import com.handge.housingfund.database.entities.CCollectionUnitDepositInventoryBatchVice;
import com.handge.housingfund.database.entities.StCollectionUnitBusinessDetails;
import com.handge.housingfund.database.entities.StSettlementSpecialBankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by 凡 on 2017/8/10.
 * 发送/接受暂时都放在一起
 */
@Component
public class CollctionTrader implements ICollectionTrader {

    @Autowired
    private UnitRemittance unitRemittance;

    @Autowired
    private UnitPayback unitPayback;

    @Autowired
    private UnitPayWrong unitPayWrong;

    @Autowired
    private IBank iBank;

    @Autowired
    private IStCollectionUnitBusinessDetailsDAO unitBusinesDAO;

    @Autowired
    private ICCollectionUnitDepositInventoryViceDAO inventoryViceDAO;

    @Autowired
    private IVoucherAutoService voucherAutoService;

    /**
     * 汇补缴，发送委托收款请求
     * BDC102 单笔收款
     */
    public void sendRemittanceMSg(HashMap map,String code){

        StSettlementSpecialBankAccount bankAccount = getBackInfo(map);
        AssertUtils.notEmpty(bankAccount,"银行账户不存在！");
        //2、拼接报文体
        SingleCollectionIn msg = new SingleCollectionIn();
        //报文头
        CenterHeadIn head = new CenterHeadIn();
        Date date= new Date();
        head.setSendDate(getDate(date));     //发送方日期
        head.setSendTime(getTime(date));     //发送方时间
        head.setSendSeqNo(map.get("ywlsh").toString());    //发送方流水号？传ywlsh？ mark
        head.setTxUnitNo("522400000000000");     //交易机构号
        head.setSendNode("C52240");     //发送方节点号
        head.setTxCode("BDC102");       //交易代码
        head.setReceiveNode(bankAccount.getYhdm()+"000");  //接收方节点号 mark
        head.setCustNo("000000");       //客户编号
        head.setOperNo("522400000000000000007");       //操作员编号 mark
        msg.setCenterHeadIn(head);
        //报文体
        msg.setSettleType("1");     //结算模式,暂时只做同行
        msg.setBusType("01");       //业务类型,01汇补缴
        //得到单位缴存网点的归集收款户
        msg.setCrAcctNo(bankAccount.getYhzhhm());      //收方账号
        msg.setCrAcctName(bankAccount.getYhzhmc());    //收方户名
        msg.setCrAcctClass("2");   //收方账户类别,公积金账户都是对公
        //根据单位发薪账号
        msg.setDeAcctNo(map.get("fxzh").toString());      //付方账号
        msg.setDeAcctName(map.get("dwmc").toString());    //付方户名
        msg.setDeAcctClass("1");   //付方账户类别,否则对私
        //跨行必输，中国银行交易必输
        //msg.setDeBankName("中国银行");    //付方行名
        //msg.setDeChgNo("104362004010");       //付方联行号
        msg.setDeBankClass("0");   //付方账户行别 暂时不做跨行

        //msg.setConAgrNo();      //多方协议号
        msg.setAmt(new BigDecimal(map.get("fse").toString()));           //金额
        msg.setRefAcctNo(map.get("dwzh").toString());     //业务明细账号
        msg.setRefSeqNo(map.get("ywlsh").toString());      //业务明细流水号
        msg.setRemark("归集收款");        //备注
        msg.setSummary("汇补缴");       //摘要

        //3、发送消息
        SingleCollectionOut singleCollectionOut = null;
        try {
            if("01".equals(code)){
                iBank.sendMsgNotToBDC(msg);
                return;
            }else if("04".equals(code)){
                singleCollectionOut = iBank.sendMsg(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorException("通信出现错误:"+e.getMessage());
        }
        //4、对返回结果进行处理
        CenterHeadOut centerHeadOut = singleCollectionOut.getCenterHeadOut();
        //成功/失败判断
        if("0".equals(centerHeadOut.getTxStatus())){

        }else{
            throw new ErrorException(singleCollectionOut.getCenterHeadOut().getRtnMessage());
        }
    }

    private StSettlementSpecialBankAccount getBackInfo(HashMap map) {
        String yhhb = map.get("yhhb").toString();
        StSettlementSpecialBankAccount accout = BusUtils.getBankAcount(yhhb, "01");
        return accout;
    }

    /**
     * 发送错缴数据
     * 单笔付款 BDC101
     */
    @Override
    public void sendPayWrongMSg(HashMap map) {
        SinglePaymentIn message = new SinglePaymentIn();
        //1、拼接报文头
        CenterHeadIn head = new CenterHeadIn();
        Date date= new Date();
        head.setSendDate(getDate(date));     //发送方日期
        head.setSendTime(getTime(date));     //发送方时间
        head.setSendSeqNo(map.get("ywlsh").toString());    //发送方流水号？传ywlsh？ mark
        head.setTxUnitNo("522400000000000");     //交易机构号
        head.setSendNode("C52240");     //发送方节点号
        head.setTxCode("BDC101");       //交易代码
        head.setReceiveNode("301000");  //接收方节点号 mark
        head.setCustNo("000000");       //客户编号
        head.setOperNo("522400000000000000007");       //操作员编号 mark
        message.setCenterHeadIn(head);
        //2、拼接报问题
        message.setSettleType("1");     //结算模式,暂时只做同行
        message.setBusType("3382");       //业务类型,3382表示错缴

        message.setDeAcctNo("310899991010008580716");      //付方账号
        message.setDeAcctName("公积金中心结算账户67");    //付方户名
        message.setDeAcctClass("2");   //付方账户类别,否则对私

        message.setCrAcctNo("310972090000300338202");      //收方账号
        message.setCrAcctName("李合宽");    //收方户名
        message.setCrAcctClass("1");   //收方账户类别,公积金账户都是对公

        message.setCrBankClass("0");    //收方账户行别
        //跨行必输，中国银行交易必输
        //message.setCrBankName("");    //收方行名
        //message.setCrChgNo("");     //收方联行号

        message.setCapAmt(new BigDecimal("1.00"));           //金额
        message.setAmt(new BigDecimal("1.00"));

        message.setRefAcctNo(map.get("dwzh").toString());     //业务明细账号
        message.setRefSeqNo1(map.get("ywlsh").toString());                 //业务明细账号2,不知道是啥

        message.setSummary("错缴");       //摘要
        message.setRemark("归集付款");        //备注

        //3、发送报文

        SinglePaymentOut singlePaymentOut = null;
        try {
            singlePaymentOut = iBank.sendMsg(message);
        } catch (Exception e) {
            //TODO 异常处理
            e.printStackTrace();
            throw new ErrorException("通信出现错误:"+e.getMessage());
        }
        //4、对返回结果进行处理
        CenterHeadOut centerHeadOut = singlePaymentOut.getCenterHeadOut();
        //成功/失败判断
        if("0".equals(centerHeadOut.getTxStatus())){

        }else{
            throw new ErrorException(centerHeadOut.getRtnMessage());
        }

    }

    /**
     * 汇补缴，账户变动通知
     * @param accChangeNotice
     */
    @Override
    public void sendPaymentNotice(AccChangeNotice accChangeNotice,String id) {

        System.out.print("账户变动通知：汇补缴委托收款办结");
        AccChangeNoticeFile accChangeNoticeFile = accChangeNotice.getAccChangeNoticeFile();
        String ywlsh = accChangeNoticeFile.getNo();

        //1、是否是汇缴的到账通知
        CCollectionUnitDepositInventoryBatchVice batchInventory = inventoryViceDAO.getBatchInventory(ywlsh);
        if(null != batchInventory){
            try {
                unitRemittance.remittancePaymentNotice(ywlsh, accChangeNotice,id);
                return;
            } catch (RuntimeException e) {
                //修改业务状态：入账失败
                unitRemittance.doFail(ywlsh,"入账发生异常，到账金额已入单位未分摊！");
                //只要出现异常，回滚后入暂收
                accChangeNoticeFile.setSummary(batchInventory.getDwzh());
            }
        }

        //2、是否是补缴的到账通知
        StCollectionUnitBusinessDetails unitBusiness = unitBusinesDAO.getByYwlsh(ywlsh);
        if(!ComUtils.isEmpty(unitBusiness)) {
            try{
                unitPayback.payBackckPaymentNotice(ywlsh, accChangeNotice,id);
                return;
            }catch (RuntimeException e){
                //修改业务状态：入账失败
                unitPayback.doFail(ywlsh,"入账发生异常，到账金额已入单位未分摊！");
                //只要出现异常，回滚后入暂收
                accChangeNoticeFile.setSummary(unitBusiness.getDwzh());
            }
        }

        //3、汇补缴都不是，将这笔业务重新进入财务
        accChangeNotice.getAccChangeNoticeFile().setNo(id);
        voucherAutoService.checkBusiness(accChangeNotice);
    }


    /**
     * 错缴业务，账户变动通知
     * @param accChangeNotice
     */
    @Override
    public void sendPayWrongNotice(AccChangeNotice accChangeNotice) {

        System.out.print("账户变动通知：错缴办结");
        String ywlsh = accChangeNotice.getAccChangeNoticeFile().getNo();
        unitPayWrong.doUnitPayWrong(ywlsh);
    }

    private String getTime(Date date) {
        return ComUtils.parseToString(date,"hhmmss");
    }

    private String getDate(Date date) {
        return ComUtils.parseToString(date,"yyyyMMdd");
    }
}
