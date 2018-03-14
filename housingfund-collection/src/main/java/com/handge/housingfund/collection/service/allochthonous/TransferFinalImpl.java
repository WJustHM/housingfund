package com.handge.housingfund.collection.service.allochthonous;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;
import com.handge.housingfund.common.service.bank.bean.transfer.SingleTransInApplIn;
import com.handge.housingfund.common.service.bank.bean.transfer.SingleTransInApplOut;
import com.handge.housingfund.common.service.bank.ibank.ITransfer;
import com.handge.housingfund.common.service.collection.allochthonous.model.CommonResponses;
import com.handge.housingfund.common.service.collection.allochthonous.service.TransferFinalInterface;
import com.handge.housingfund.common.service.collection.enumeration.AllochthonousStatus;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.ICAccountNetworkDAO;
import com.handge.housingfund.database.dao.ICBankCenterInfoDAO;
import com.handge.housingfund.database.dao.IStCollectionPersonalBusinessDetailsDAO;
import com.handge.housingfund.database.entities.CAccountNetwork;
import com.handge.housingfund.database.entities.CCollectionAllochthounousTransferProcess;
import com.handge.housingfund.database.entities.CCollectionAllochthounousTransferVice;
import com.handge.housingfund.database.entities.StCollectionPersonalBusinessDetails;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Liujuhao on 2017/12/12.
 */
@Service(value = "final.final")
public class TransferFinalImpl implements TransferFinalInterface {
    @Autowired
    private IStCollectionPersonalBusinessDetailsDAO collectionPersonalBusinessDetailsDAO;
    @Autowired
    private ITransfer iTransfer;
    @Autowired
    private ICAccountNetworkDAO cAccountNetworkDAO;
    @Autowired
    private ICBankCenterInfoDAO bankCenterInfoDAO;

    @Override
    public void intoFinal(TokenContext tokenContext, String YWLSH) {
        System.out.println("审核通过后，调结算平台");

        StCollectionPersonalBusinessDetails stCollectionPersonalBusinessDetails = DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("ywlsh", YWLSH);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (stCollectionPersonalBusinessDetails == null || stCollectionPersonalBusinessDetails.getAllochthounousTransferVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应的业务");
        }

        List<CCollectionAllochthounousTransferProcess> cCollectionAllochthounousTransferProcesses = new ArrayList<>();
        CCollectionAllochthounousTransferProcess cCollectionAllochthounousTransferProcess = new CCollectionAllochthounousTransferProcess();
        cCollectionAllochthounousTransferProcess.setCaoZuo("转入方录入联系函复核通过");
        cCollectionAllochthounousTransferProcess.setCZJG("毕节市住房公积金管理中心");
        cCollectionAllochthounousTransferProcess.setCZRY(stCollectionPersonalBusinessDetails.getExtension().getCzy());
        cCollectionAllochthounousTransferProcess.setCZSJ(new Date());
        cCollectionAllochthounousTransferProcess.setCZYJ("同意转入");
        cCollectionAllochthounousTransferProcess.setCZHZT("联系函复核通过");
        cCollectionAllochthounousTransferProcess.setTransferVice(stCollectionPersonalBusinessDetails.getAllochthounousTransferVice());
        cCollectionAllochthounousTransferProcesses.add(cCollectionAllochthounousTransferProcess);
        stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().setProcesses(cCollectionAllochthounousTransferProcesses);

        CCollectionAllochthounousTransferVice cCollectionAllochthounousTransferVice = stCollectionPersonalBusinessDetails.getAllochthounousTransferVice();
        SingleTransInApplIn singleTransInApplIn = new SingleTransInApplIn();

        CenterHeadIn centerHeadIn = new CenterHeadIn(YWLSH,bankCenterInfoDAO.getCenterNodeByNum(cCollectionAllochthounousTransferVice.getZCJGBH()),stCollectionPersonalBusinessDetails.getExtension().getCzy());
        centerHeadIn.setTxUnitNo(cCollectionAllochthounousTransferVice.getZRJGBH());

        /**
         * CenterHeadIn(required), 公积金中心发起交易输入报文头
         */
        singleTransInApplIn.setCenterHeadIn(centerHeadIn);
        /**
         * 联系函编号(required), 公积金中心全局唯一联系函编号规则为“机构代码(前6位)+YYMMDD+4位顺序号”, 当交易类型为1时, 该编号必须是已存在的编号
         */
        singleTransInApplIn.setConNum(cCollectionAllochthounousTransferVice.getLXHBH());
        /**
         * 交易类型(required), 0 新增是用于新增的转移接续, 1 转出方确认受理是用于转出方确认受理转入方的联系函, 反馈受理信息给转入方
         */
        singleTransInApplIn.setTxFunc("0");
        /**
         * 转出公积金中心机构编号(required), 参见公积金中心代码表的机构代码
         */
        singleTransInApplIn.setTranOutUnitNo(cCollectionAllochthounousTransferVice.getZCJGBH());
        /**
         * 转出公积金中心名称(required)
         */
        singleTransInApplIn.setTranCenName(cCollectionAllochthounousTransferVice.getZCGJJZXMC());
        /**
         * 职工姓名(required)
         */
        singleTransInApplIn.setEmpName(cCollectionAllochthounousTransferVice.getZGXM());
        /**
         * 职工证件类型(required), 依据贯标标准代码值: 01-身份证 02-军官证 03-护照 04-外国人永久居留证 99-其他
         */
        singleTransInApplIn.setDocType(cCollectionAllochthounousTransferVice.getZJLX());
        /**
         * 职工证件号码(required)
         */
        singleTransInApplIn.setIdNum(cCollectionAllochthounousTransferVice.getZJHM());
        /**
         * 原工作单位名称(required)
         */
        singleTransInApplIn.setSocUnitName(cCollectionAllochthounousTransferVice.getYGZDWMC());
        /**
         * 原住房公积金账号(required)
         */
        singleTransInApplIn.setSocRefAcctNo(cCollectionAllochthounousTransferVice.getYGRZFGJJZH());
        /**
         * 转入公积金中心资金账号(required), 转入公积金中心资金接收账户的账号
         */
        singleTransInApplIn.setTranRefAcctNo(cCollectionAllochthounousTransferVice.getZRZJZH());
        /**
         * 转入公积金中心资金账号户名(required), 转入公积金中心资金接收账户户名
         */
        singleTransInApplIn.setTranRefAcctName(cCollectionAllochthounousTransferVice.getZRZJZHHM());
        /**
         * 转入公积金中心资金账户所属银行名称(required), 转入公积金资金接收账户所属银行名称
         */
        singleTransInApplIn.setTranBank(cCollectionAllochthounousTransferVice.getZRZJZHYHMC());
        /**
         * 公积金中心委托业务办理银行, 公积金中心委托银行办理异地转移接续业务时填写, 参见银行代码表
         */
//        singleTransInApplIn.setEntBank();
        /**
         * 转入公积金中心联系方式(required)
         */
        singleTransInApplIn.setTrenCenInfo(cCollectionAllochthounousTransferVice.getLXDHHCZ());
        /**
         * 联系单生成日期(required), 格式: YYYYMMDD
         */
        singleTransInApplIn.setGenDate(DateUtil.date2Str(cCollectionAllochthounousTransferVice.getLXDSCRQ(),"yyyyMMdd"));
        /**
         * 反馈信息代码, 0-已完成, 当交易类型为1-反馈结果时, 该项必填, 其他可为空
         */
        singleTransInApplIn.setTranRstCode(null);
        /**
         * 反馈信息
         */
        singleTransInApplIn.setTranRstMsg("联系函复核通过");
        /**
         * 备注
         */
        singleTransInApplIn.setRemark("");
        /**
         * 备用字段
         */
        singleTransInApplIn.setBak1("");
        /**
         * 备用字段
         */
        singleTransInApplIn.setBak2("");
        CommonResponses commonResponses =new CommonResponses();
        try {
            SingleTransInApplOut singleTransInApplOut = iTransfer.sendMsg(singleTransInApplIn);
            if(singleTransInApplOut.getCenterHeadOut().getTxStatus().equals("0")){
                commonResponses.setStatus("200");
                commonResponses.setMsg("success");
            }
            else {
                commonResponses.setStatus("200");
                commonResponses.setMsg("失败:"+singleTransInApplOut.getCenterHeadOut().getRtnMessage());
            }
        } catch (Exception e) {
            throw new ErrorException(e);
        }
        if(StringUtil.notEmpty(commonResponses.getMsg())&&commonResponses.getMsg().equals("success")) {
            stCollectionPersonalBusinessDetails.getAllochthounousTransferVice().setLXHZT(AllochthonousStatus.联系函复核通过.getCode());
            DAOBuilder.instance(collectionPersonalBusinessDetailsDAO).entity(stCollectionPersonalBusinessDetails).save(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
        }
    }
}
