package com.handge.housingfund.bank.service;

import com.handge.housingfund.common.configure.Configure;
import com.handge.housingfund.common.service.bank.bean.center.ChgNoList;
import com.handge.housingfund.common.service.bank.bean.center.ChgNoQueryIn;
import com.handge.housingfund.common.service.bank.bean.center.ChgNoQueryOut;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;
import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNoticeFile;
import com.handge.housingfund.common.service.bank.bean.transfer.CRFCenterCodeQueryIn;
import com.handge.housingfund.common.service.bank.bean.transfer.CRFCenterCodeQueryOut;
import com.handge.housingfund.common.service.bank.bean.transfer.CenterInfoFile;
import com.handge.housingfund.common.service.bank.ibank.IBank;
import com.handge.housingfund.common.service.bank.ibank.IBankTask;
import com.handge.housingfund.common.service.bank.ibank.ITransfer;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.LogUtil;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.TransactionFileFactory;
import com.handge.housingfund.database.dao.ICBankBankInfoDAO;
import com.handge.housingfund.database.dao.ICBankCenterInfoDAO;
import com.handge.housingfund.database.dao.ICBankNodeInfoDAO;
import com.handge.housingfund.database.entities.CBankBankInfo;
import com.handge.housingfund.database.entities.CBankCenterInfo;
import com.handge.housingfund.database.entities.CBankNodeInfo;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gxy on 17-10-2.
 */
@Component
public class BankTask implements IBankTask {

    private static Logger logger = LogManager.getLogger(BankTask.class);
    @Autowired
    private ICBankBankInfoDAO icBankBankInfoDAO;
    @Autowired
    private ICBankNodeInfoDAO icBankNodeInfoDAO;
    @Autowired
    private ICBankCenterInfoDAO icBankCenterInfoDAO;
    @Autowired
    private IBank iBank;
    @Autowired
    private ITransfer iTransfer;

    @Override
    public void addChgNo() {
        List<CBankNodeInfo> nodeInfos = DAOBuilder.instance(icBankNodeInfoDAO).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
            }
        });

        Configuration cfg = Configure.getInstance().getConfiguration("BankNoAndAreaNo");
        CenterHeadIn chi = new CenterHeadIn();
        chi.setOperNo("001");
        chi.setReceiveNode("105000");
        String[] bankNos = cfg.getString("BankNo").split("\\|");
        String areaNoStr = cfg.getString("AreaNo").replaceAll("\\|", ",");
        List<String> areaNos = new ArrayList<>();
        int begin = 0;
        int end = 49;
        int length = areaNoStr.length() % 50 == 0 ? areaNoStr.length() / 50 : areaNoStr.length() / 50 + 1;
        for (int i = 0; i < length; i++) {
            if (areaNoStr.length() < end) end = areaNoStr.length();
            areaNos.add(areaNoStr.substring(begin, end));
            begin = end + 1;
            if (i == length - 2)
                end = areaNoStr.length();
            else
                end = end + 50;
        }
        System.out.println(areaNos);

        logger.info("start------------------------------------");
        DAOBuilder<CBankBankInfo, ICBankBankInfoDAO> dao = DAOBuilder.instance(icBankBankInfoDAO);
        for (String bankNo : bankNos) {
            for (String areaNo : areaNos) {
                ChgNoQueryIn chgNoQueryIn = new ChgNoQueryIn(chi, bankNo, areaNo);
                try {
                    ChgNoQueryOut chgNoQueryOut = iBank.sendMsg(chgNoQueryIn);
                    logger.info("联行号： \n" + chgNoQueryOut);
                    List<ChgNoList> chgNoLists = chgNoQueryOut.getChgNoList();
                    for (ChgNoList chgNoList : chgNoLists) {
                        String chgNo = chgNoList.getChgNo();
                        String bankName = chgNoList.getChgName();
                        CBankBankInfo cBankBankInfo;

                        HashMap<String, Object> filter = new HashMap<>();
                        filter.put("chgno", chgNo);
                        cBankBankInfo = dao.searchFilter(filter).getObject(new DAOBuilder.ErrorHandler() {
                            @Override
                            public void error(Exception e) {
                                throw new ErrorException("查询联行号为" + chgNo + "的银行信息失败");
                            }
                        });

                        if (cBankBankInfo == null) cBankBankInfo = new CBankBankInfo();

                        cBankBankInfo.setBank_name(bankName);
                        cBankBankInfo.setChgno(chgNo);
                        cBankBankInfo.setCode(chgNo.substring(0, 3));
                        cBankBankInfo.setArea_code(chgNo.substring(3, 7));

                        for (CBankNodeInfo cBankNodeInfo : nodeInfos) {
                            if (bankName.contains(cBankNodeInfo.getBank_name())) {
                                cBankBankInfo.setNode(cBankNodeInfo.getNode());
                            }
                        }

                        dao.entity(cBankBankInfo).save(new DAOBuilder.ErrorHandler() {
                            @Override
                            public void error(Exception e) {
                                throw new ErrorException("插入bankInfo失败");
                            }
                        });
                    }
                } catch (Exception e) {
                    logger.error(LogUtil.getTrace(e));
                }
            }
        }
        logger.info("end--------------------------------------");
    }

    @Override
    public void addCRFCenterCode() {
        CenterHeadIn centerHeadIn = new CenterHeadIn();
        centerHeadIn.setOperNo("001");
        centerHeadIn.setReceiveNode("D00000");

        CRFCenterCodeQueryIn crfCenterCodeQueryIn = new CRFCenterCodeQueryIn();
        crfCenterCodeQueryIn.setCenterHeadIn(centerHeadIn);

        try {
            CRFCenterCodeQueryOut crfCenterCodeQueryOut = iTransfer.sendMsg(crfCenterCodeQueryIn);
            List<Object> centerInfos = TransactionFileFactory.getObjFromFile(crfCenterCodeQueryOut.getFileList().getDATA(), CenterInfoFile.class.getName());

            DAOBuilder<CBankCenterInfo, ICBankCenterInfoDAO> dao = DAOBuilder.instance(icBankCenterInfoDAO);
            for (Object o : centerInfos) {
                CenterInfoFile centerInfo = (CenterInfoFile) o;

                HashMap<String, Object> filter = new HashMap<>();
                filter.put("unit_no", centerInfo.getUnit_no());

                CBankCenterInfo cBankCenterInfo = dao.searchFilter(filter).getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException("查询中心信息失败");
                    }
                });

                if (cBankCenterInfo == null) {
                    cBankCenterInfo = new CBankCenterInfo();
                    cBankCenterInfo.setUnit_no(centerInfo.getUnit_no());
                    cBankCenterInfo.setUnit_name(centerInfo.getUnit_name());
                    cBankCenterInfo.setParent_unit_no(centerInfo.getParent_unit_no());
                    cBankCenterInfo.setNode(centerInfo.getNode());

                    dao.entity(cBankCenterInfo).save(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
                            throw new ErrorException("插入CenterInfo失败");
                        }
                    });
                }
            }
        } catch (Exception e) {
            logger.error(LogUtil.getTrace(e));
        }
    }
}
