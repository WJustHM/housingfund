package com.handge.housingfund.collection.service.allochthonous;

import com.handge.housingfund.common.service.ISaveAuditHistory;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;
import com.handge.housingfund.common.service.bank.bean.transfer.TransInApplCancelIn;
import com.handge.housingfund.common.service.bank.bean.transfer.TransInApplCancelOut;
import com.handge.housingfund.common.service.bank.ibank.ITransfer;
import com.handge.housingfund.common.service.collection.allochthonous.model.CommonResponses;
import com.handge.housingfund.common.service.collection.allochthonous.model.TransferInCancelModle;
import com.handge.housingfund.common.service.collection.allochthonous.service.TransferInCancelInterface;
import com.handge.housingfund.common.service.collection.enumeration.AllochthonousStatus;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessStatus;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ModelUtils;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.database.dao.ICAccountNetworkDAO;
import com.handge.housingfund.database.dao.ICBankCenterInfoDAO;
import com.handge.housingfund.database.dao.IStCollectionPersonalBusinessDetailsDAO;
import com.handge.housingfund.database.entities.CAccountNetwork;
import com.handge.housingfund.database.entities.CCollectionAllochthounousTransferProcess;
import com.handge.housingfund.database.entities.CCollectionAllochthounousTransferVice;
import com.handge.housingfund.database.entities.StCollectionPersonalBusinessDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TransferInCancelImpl implements TransferInCancelInterface {

    @Autowired
    IStCollectionPersonalBusinessDetailsDAO personalBusinessDetailsDAO;

    @Autowired
    ITransfer transfer;

    @Autowired
    ICAccountNetworkDAO accountNetworkDAO;

    @Autowired
    ISaveAuditHistory saveAuditHistory;

    @Autowired
    ICBankCenterInfoDAO bankCenterInfoDAO;

    /**
     * 转入-撤销（只能在“联系函复合通过-01”的状态）
     *
     * @param YWLSH 业务流水号
     */
    public CommonResponses transferInCancel(TokenContext tokenContext, final String YWLSH, final TransferInCancelModle body) {

        if (false) {
            return ModelUtils.randomModel(CommonResponses.class);
        }

        CommonResponses commonResponses = new CommonResponses();

        StCollectionPersonalBusinessDetails personalBusinessDetails = personalBusinessDetailsDAO.getByYwlsh(YWLSH);

        if (personalBusinessDetails == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务不存在");
        }

        CCollectionAllochthounousTransferVice allochthounousTransferVice = personalBusinessDetails.getAllochthounousTransferVice();

        // TODO: 2017/12/8 缺少对STEP的判断，如业务成功执行后，应转为“转入撤销已发送”

        if (AllochthonousStatus.联系函复核通过.getCode().equals(allochthounousTransferVice.getLXHZT())) {
            TransInApplCancelIn transInApplCancelIn = new TransInApplCancelIn();

            CenterHeadIn centerHeadIn = new CenterHeadIn(
                    YWLSH,
                    bankCenterInfoDAO.getCenterNodeByNum(allochthounousTransferVice.getZCJGBH()),
                    tokenContext.getUserInfo().getCZY());
            transInApplCancelIn.setCenterHeadIn(centerHeadIn);

            transInApplCancelIn.setOrConNum(allochthounousTransferVice.getLXHBH());
            transInApplCancelIn.setOrTxFunc("1");
            transInApplCancelIn.setTranOutUnitNo(allochthounousTransferVice.getZCJGBH());
            transInApplCancelIn.setTranCenName(allochthounousTransferVice.getZCGJJZXMC());
            transInApplCancelIn.setEmpName(allochthounousTransferVice.getZGXM());
            transInApplCancelIn.setDocType(allochthounousTransferVice.getZJLX());
            transInApplCancelIn.setIdNum(allochthounousTransferVice.getZJHM());
            try {
                TransInApplCancelOut headOut = transfer.sendMsg(transInApplCancelIn);
                CCollectionAllochthounousTransferProcess allochthounousTransferProcess = new CCollectionAllochthounousTransferProcess();

                if (headOut.getCenterHeadOut().getTxStatus().equals("0")) {

                    personalBusinessDetails.getExtension().setStep(CollectionBusinessStatus.转入撤销业务办结.getName());
                    personalBusinessDetails.getExtension().setBjsj(new Date());
                    allochthounousTransferVice.setLXHZT(AllochthonousStatus.转入撤销业务办结.getCode());

                    allochthounousTransferProcess.setCaoZuo("转入方撤销业务");
                    allochthounousTransferProcess.setCZHZT(AllochthonousStatus.转入撤销业务办结.getCode());
                    allochthounousTransferProcess.setCZJG(bankCenterInfoDAO.getCenterNameByNum(allochthounousTransferVice.getZRJGBH()));
                    allochthounousTransferProcess.setCZRY(tokenContext.getUserInfo().getCZY());
                    allochthounousTransferProcess.setCZSJ(new Date());
                    allochthounousTransferProcess.setCZYJ(body.getReviewInfo().getYYYJ());
                    allochthounousTransferProcess.setCZHZT("转入撤销业务办结");
                    allochthounousTransferProcess.setTransferVice(allochthounousTransferVice);
                    allochthounousTransferVice.getProcesses().add(allochthounousTransferProcess);

                    commonResponses.setStatus("success");
                    commonResponses.setYWLSH(YWLSH);

                } else {

                    personalBusinessDetails.getExtension().setSbyy(headOut.getCenterHeadOut().getRtnMessage());

                    commonResponses.setStatus("fail");
                    commonResponses.setMsg("结算平台处理失败:" + headOut.getCenterHeadOut().getRtnMessage());
                    commonResponses.setYWLSH(YWLSH);
                }
                personalBusinessDetails.getExtension().setCzy(tokenContext.getUserId());
                CAccountNetwork accountNetwork = accountNetworkDAO.get(tokenContext.getUserInfo().getYWWD());
                personalBusinessDetails.getExtension().setYwwd(accountNetwork);
                saveAuditHistory.saveNormalBusiness(YWLSH, tokenContext, CollectionBusinessType.外部转入.getName(), "撤销");
                personalBusinessDetailsDAO.save(personalBusinessDetails);
            } catch (Exception e) {
                throw new ErrorException(e);
            }
        } else {
            throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "联系函状态已不可撤销，请线下协商处理!");
        }

        return commonResponses;
    }

}
