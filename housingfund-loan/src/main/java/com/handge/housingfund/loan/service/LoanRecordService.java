package com.handge.housingfund.loan.service;

import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.enumeration.ReviewSubModule;
import com.handge.housingfund.common.service.loan.ILoanRecordService;
import com.handge.housingfund.common.service.loan.enums.LoanAccountType;
import com.handge.housingfund.common.service.loan.model.*;
import com.handge.housingfund.common.service.loan.model.Error;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.apache.poi.ss.formula.functions.T;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Liujuhao on 2017/8/9.
 */

@SuppressWarnings({"Duplicates", "Convert2Lambda"})
@Service
public class LoanRecordService implements ILoanRecordService {

    @Autowired
    private ICLoanHousingPersonInformationBasicDAO icLoanHousingPersonInformationBasicDAO;

    @Autowired
    private ICLoanHousingBusinessProcessDAO icLoanHousingBusinessProcessDAO;

    @Autowired
    private ICAuditHistoryDAO auditHistoryDAO;

    @Autowired
    private ICAccountNetworkDAO icAccountNetworkDAO;

    String format1 = "yyyy-MM-dd";

    // completed: 2017/8/10  贷款记录详情
    @Override
    public GetLoanRecordDetailsResponses getLoanDetails(String DKZH) {

        CLoanHousingPersonInformationBasic personInformationBasic = DAOBuilder.instance(this.icLoanHousingPersonInformationBasicDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("dkzh", DKZH);
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (personInformationBasic == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有该贷款账号的相关信息");
        }

        //该接口返回对象
        GetLoanRecordDetailsResponses getLoanRecordDetailsResponses = new GetLoanRecordDetailsResponses();

        //业务办理信息（操作员、网点）
        GetLoanRecordDetailsResponsesManagerInformation managerInformation = new GetLoanRecordDetailsResponsesManagerInformation();
        managerInformation.setCZY(personInformationBasic.getCzy());
        String ywwdmc = icAccountNetworkDAO.get(personInformationBasic.getYwwd()).getMingCheng();
        managerInformation.setYWWD(ywwdmc);
        getLoanRecordDetailsResponses.setmanagerInformation(managerInformation);

        //申请人信息
        GetLoanRecordDetailsResponsesApplicantInformation applicantInformation = new GetLoanRecordDetailsResponsesApplicantInformation();

        applicantInformation.setDKZH(personInformationBasic.getDkzh());
        applicantInformation.setJCD(personInformationBasic.getJcd());
        applicantInformation.setJKRGJJZH(personInformationBasic.getJkrgjjzh());
        boolean hasCoborrower = true;
        if (personInformationBasic.getCoborrower() == null) {
            applicantInformation.setGTJKR("1");
            hasCoborrower = false;
        } else {
            applicantInformation.setGTJKR("0");
        }
        applicantInformation.setBLZL(personInformationBasic.getBlzl() + "");
        //申请人信息--借款人信息
        GetLoanRecordDetailsResponsesApplicantInformationBorrowerInformation borrowerInformation = new GetLoanRecordDetailsResponsesApplicantInformationBorrowerInformation();
        borrowerInformation.setJKRXM(personInformationBasic.getJkrxm());
        borrowerInformation.setJKRZJLX(personInformationBasic.getJkrzjlx());
        borrowerInformation.setJKRZJHM(personInformationBasic.getJkrzjhm());
        // completed: 2017/9/11 时间格式加“-”
        borrowerInformation.setCSNY(DateUtil.str2str(personInformationBasic.getCsny(), 6));
        borrowerInformation.setXingBie(personInformationBasic.getXingBie().toString());
        borrowerInformation.setXueLi(personInformationBasic.getXueLi());
        borrowerInformation.setNianLing(personInformationBasic.getNianLing());
        borrowerInformation.setJKZK(personInformationBasic.getJkzk());
        borrowerInformation.setHYZK(personInformationBasic.getHyzk());
        borrowerInformation.setZhiCheng(personInformationBasic.getZhiCheng());
        borrowerInformation.setZhiWu(personInformationBasic.getZhiWu());
        borrowerInformation.setYGXZ(personInformationBasic.getYgxz());
        borrowerInformation.setZYJJLY(personInformationBasic.getZyjjly());
        borrowerInformation.setYSR(personInformationBasic.getYsr() + "");
        borrowerInformation.setJTYSR(personInformationBasic.getJtysr() + "");
        borrowerInformation.setGDDHHM(personInformationBasic.getGddhhm());
        borrowerInformation.setSJHM(personInformationBasic.getSjhm());
        borrowerInformation.setJTZZ(personInformationBasic.getJtzz());
        borrowerInformation.setHKSZD(personInformationBasic.getHkszd());
        applicantInformation.setBorrowerInformation(borrowerInformation);
        //申请人信息--公积金账户信息
        GetLoanRecordDetailsResponsesApplicantInformationAccountInformation accountInformation = new GetLoanRecordDetailsResponsesApplicantInformationAccountInformation();
        // completed: 2017/9/11 时间格式加“-”
        accountInformation.setJZNY(DateUtil.str2str(personInformationBasic.getJzny(), 6));
        accountInformation.setGRZHZT(personInformationBasic.getGrzhzt());
        accountInformation.setYJCE(personInformationBasic.getYjce() == null ? null : personInformationBasic.getYjce() + "");
        accountInformation.setGRJCJS(personInformationBasic.getGrjcjs() == null ? null : personInformationBasic.getGrjcjs() + "");
        accountInformation.setGRZHYE(personInformationBasic.getGrzhye() == null ? null : personInformationBasic.getGrzhye() + "");
        accountInformation.setLXZCJCYS(personInformationBasic.getLxzcjcys() == null ? null : personInformationBasic.getLxzcjcys() + "");
        applicantInformation.setAccountInformation(accountInformation);
        //申请人信息--单位信息
        GetLoanRecordDetailsResponsesApplicantInformationUnitInformation unitInformation = new GetLoanRecordDetailsResponsesApplicantInformationUnitInformation();
        unitInformation.setDWMC(personInformationBasic.getDwmc());
        unitInformation.setDWZH(personInformationBasic.getDwzh());
        unitInformation.setDWDH(personInformationBasic.getDwdh());
        unitInformation.setDWLB(personInformationBasic.getDwxz());
        unitInformation.setDWDZ(personInformationBasic.getDwdz());
        applicantInformation.setUnitInformation(unitInformation);

        getLoanRecordDetailsResponses.setApplicantInformation(applicantInformation);

        //共同借款人信息（如果有）
        if (hasCoborrower) {
            GetLoanRecordDetailsResponsesCommonBorrowerInformation commonBorrowerInformation = new GetLoanRecordDetailsResponsesCommonBorrowerInformation();

            commonBorrowerInformation.setCHGX(personInformationBasic.getCoborrower().getCdgx());
            commonBorrowerInformation.setGTJKRGJJZH(personInformationBasic.getCoborrower().getGtjkrgjjzh());
            commonBorrowerInformation.setJCD(personInformationBasic.getCoborrower().getExtension().getJcd());
            commonBorrowerInformation.setTJZL(personInformationBasic.getCoborrower().getExtension().getBlzl() + "");

            commonBorrowerInformation.setGTJKRXM(personInformationBasic.getCoborrower().getGtjkrxm());
            commonBorrowerInformation.setGTJKRZJLX(personInformationBasic.getCoborrower().getGtjkrzjlx());
            commonBorrowerInformation.setGTJKRZJHM(personInformationBasic.getCoborrower().getGtjkrzjhm());
            commonBorrowerInformation.setGDDHHM(personInformationBasic.getCoborrower().getGddhhm());
            commonBorrowerInformation.setSJHM(personInformationBasic.getCoborrower().getSjhm());
            commonBorrowerInformation.setYSR(personInformationBasic.getCoborrower().getYsr() == null ? null : personInformationBasic.getCoborrower().getYsr() + "");
            commonBorrowerInformation.setHKSZD(personInformationBasic.getCoborrower().getExtension().getHkszd());

            //共同借款人信息--单位信息
            CoborrowerUnitInformation coborrowerUnitInformation = new CoborrowerUnitInformation();
            coborrowerUnitInformation.setDWMC(personInformationBasic.getCoborrower().getExtension().getDwmc());
            coborrowerUnitInformation.setDWZH(personInformationBasic.getCoborrower().getExtension().getDwzh());
            coborrowerUnitInformation.setDWDH(personInformationBasic.getCoborrower().getExtension().getDwdh());
            coborrowerUnitInformation.setDWLB(personInformationBasic.getCoborrower().getExtension().getDwxz());
            coborrowerUnitInformation.setDWDZ(personInformationBasic.getCoborrower().getExtension().getDwdz());
            commonBorrowerInformation.setDWXX(coborrowerUnitInformation);

            //共同借款人信息-公积金账户信息
            CoborrowerAccountInformation coborrowerAccountInformation = new CoborrowerAccountInformation();
            // completed: 2017/9/11 时间格式加“-”
            coborrowerAccountInformation.setJZNY(DateUtil.str2str(personInformationBasic.getCoborrower().getExtension().getJzny(), 6));
            coborrowerAccountInformation.setGRZHZT(personInformationBasic.getCoborrower().getExtension().getGrzhzt());
            coborrowerAccountInformation.setLXZCJCYS(personInformationBasic.getCoborrower().getExtension().getLxzcjcys() == null ? null : personInformationBasic.getCoborrower().getExtension().getLxzcjcys() + "");
            coborrowerAccountInformation.setYJCE(personInformationBasic.getCoborrower().getExtension().getYjce() == null ? null : personInformationBasic.getCoborrower().getExtension().getYjce() + "");
            coborrowerAccountInformation.setGRJCJS(personInformationBasic.getCoborrower().getExtension().getGrjcjs() == null ? null : personInformationBasic.getCoborrower().getExtension().getGrjcjs() + "");
            coborrowerAccountInformation.setGRZHYE(personInformationBasic.getCoborrower().getExtension().getGrzhye() == null ? null : personInformationBasic.getCoborrower().getExtension().getGrzhye() + "");
            commonBorrowerInformation.setGJJZHXX(coborrowerAccountInformation);

            getLoanRecordDetailsResponses.setCommonBorrowerInformation(commonBorrowerInformation);
        } else {
            getLoanRecordDetailsResponses.setCommonBorrowerInformation(new GetLoanRecordDetailsResponsesCommonBorrowerInformation() {{

                this.setDWXX(new CoborrowerUnitInformation());
                this.setGJJZHXX(new CoborrowerAccountInformation());
            }});
        }

        //房屋信息
        GetLoanRecordDetailsResponsesHouseInformation houseInformation = new GetLoanRecordDetailsResponsesHouseInformation();
        String dkyt = personInformationBasic.getDkyt();
        houseInformation.setDKYT(dkyt);

        //贷款用途（00：所有 0：购买 1：自建、翻修 2：大修）
        if ("0".equals(dkyt)) {
            CLoanHousePurchasingBasic purchasingBasic = personInformationBasic.getPurchasing();
            if (purchasingBasic != null) {
//                houseInformation.setBLZL(purchasingBasic.getBlzl());
                boolean isSecondHouse = (purchasingBasic.getSfwesf() == null ? false : purchasingBasic.getSfwesf());  //是否为二手房，重点测试
                if (isSecondHouse) {
                    houseInformation.setSFWESF("1");
                    GetLoanRecordDetailsResponsesHouseInformationPurchaseSecondInformation purchaseSecondInformation = new GetLoanRecordDetailsResponsesHouseInformationPurchaseSecondInformation();
                    purchaseSecondInformation.setLPMC(purchasingBasic.getLpmc());
                    purchaseSecondInformation.setDanJia(purchasingBasic.getDanJia());
                    purchaseSecondInformation.setHTJE(purchasingBasic.getHtje());
                    purchaseSecondInformation.setFWZJ(purchasingBasic.getFwzj());
                    purchaseSecondInformation.setGFHTBH(purchasingBasic.getGfhtbh());
                    purchaseSecondInformation.setFWJZMJ(purchasingBasic.getFwjzmj() == null ? null : purchasingBasic.getFwjzmj() + "");
                    purchaseSecondInformation.setFWTNMJ(purchasingBasic.getFwtnmj() == null ? null : purchasingBasic.getFwtnmj() + "");
                    purchaseSecondInformation.setYFK(purchasingBasic.getYfk() == null ? null : purchasingBasic.getYfk() + "");
                    purchaseSecondInformation.setFWXZ(purchasingBasic.getFwxz());
                    purchaseSecondInformation.setFWJG(purchasingBasic.getFwjg());
                    purchaseSecondInformation.setFWXS(purchasingBasic.getFwxs());
                    purchaseSecondInformation.setFWJGRQ(DateUtil.date2Str(purchasingBasic.getFwjgrq(), format1));
                    purchaseSecondInformation.setFWZL(purchasingBasic.getFwzl());
                    purchaseSecondInformation.setSFRMC(purchasingBasic.getSfrmc());
                    purchaseSecondInformation.setLXFS(purchasingBasic.getLxfs());
                    purchaseSecondInformation.setGRSKYHZH(purchasingBasic.getGrskyhzh());
                    purchaseSecondInformation.setKHYHMC(purchasingBasic.getKhyhmc());
                    purchaseSecondInformation.setYHKHM(purchasingBasic.getYhkhm());
                    purchaseSecondInformation.setBLZL(purchasingBasic.getBlzl() + "");
                    houseInformation.setPurchaseSecondInformation(purchaseSecondInformation);
                } else {
                    houseInformation.setSFWESF("0");
                    GetLoanRecordDetailsResponsesHouseInformationPurchaseFirstInformation purchaseFirstInformation = new GetLoanRecordDetailsResponsesHouseInformationPurchaseFirstInformation();
                    purchaseFirstInformation.setLPMC(purchasingBasic.getLpmc());
                    purchaseFirstInformation.setDanJia(purchasingBasic.getDanJia() == null ? null : purchasingBasic.getDanJia() + "");
                    purchaseFirstInformation.setHTJE(purchasingBasic.getHtje() == null ? null : purchasingBasic.getHtje() + "");
                    purchaseFirstInformation.setFWZJ(purchasingBasic.getFwzj() == null ? null : purchasingBasic.getFwzj() + "");
                    purchaseFirstInformation.setGFHTBH(purchasingBasic.getGfhtbh());
                    purchaseFirstInformation.setFWJZMJ(purchasingBasic.getFwjzmj() == null ? null : purchasingBasic.getFwjzmj() + "");
                    purchaseFirstInformation.setFWTNMJ(purchasingBasic.getFwtnmj() == null ? null : purchasingBasic.getFwtnmj() + "");
                    purchaseFirstInformation.setSFK(purchasingBasic.getSfk() == null ? null : purchasingBasic.getSfk() + "");
                    purchaseFirstInformation.setFWXZ(purchasingBasic.getFwxz());
                    purchaseFirstInformation.setFWJG(purchasingBasic.getFwjg());
                    purchaseFirstInformation.setFWXS(purchasingBasic.getFwxs());
                    purchaseFirstInformation.setFWJGRQ(DateUtil.date2Str(purchasingBasic.getFwjgrq(), format1));
                    purchaseFirstInformation.setFWZL(purchasingBasic.getFwzl());
                    purchaseFirstInformation.setSFRMC(purchasingBasic.getSfrmc());
                    purchaseFirstInformation.setLXFS(purchasingBasic.getLxfs());
                    purchaseFirstInformation.setSPFYSXKBH(purchasingBasic.getSpfysxkbh());
                    purchaseFirstInformation.setSFRZHHM(purchasingBasic.getSfrzhhm());
                    purchaseFirstInformation.setSFRKHYHMC(purchasingBasic.getSfrkhyhmc());
                    purchaseFirstInformation.setSFRYHKHM(purchasingBasic.getSfryhkhm());
                    purchaseFirstInformation.setBLZL(purchasingBasic.getBlzl() + "");
                    houseInformation.setPurchaseFirstInformation(purchaseFirstInformation);
                }
            }
        }
        if ("1".equals(dkyt)) {
            CLoanHouseBuildBasic buildBasic = personInformationBasic.getBuild();
            if (buildBasic != null) {
//                houseInformation.setBLZL(buildBasic.getBlzl());
                GetLoanRecordDetailsResponsesHouseInformationBuildInformation buildInformation = new GetLoanRecordDetailsResponsesHouseInformationBuildInformation();
                buildInformation.setPZJGWH(buildBasic.getPzjgwh());
                buildInformation.setJHJZFY(buildBasic.getJhjzfy() == null ? null : buildBasic.getJhjzfy() + "");
                buildInformation.setJZCS(buildBasic.getJzcs() == null ? null : buildBasic.getJzcs() + "");
                buildInformation.setJHKGRQ(DateUtil.date2Str(buildBasic.getJhkgrq(), format1));
                buildInformation.setJHJGRQ(DateUtil.date2Str(buildBasic.getJhjgrq(), format1));
                buildInformation.setTDSYZH(buildBasic.getTdsyzh());
                buildInformation.setJZGCGHXKZH(buildBasic.getJzgcghxkzh());
                buildInformation.setJZYDGHXKZH(buildBasic.getJzydghxkzh());
                buildInformation.setJSGCSGXKZH(buildBasic.getJsgcsgxkzh());
                buildInformation.setFWZLDZ(buildBasic.getFwzl());
                buildInformation.setGRSYZJ(buildBasic.getGrsyzj() == null ? null : buildBasic.getGrsyzj() + "");
                buildInformation.setJCHJZMJ(buildBasic.getJchjzmj() == null ? null : buildBasic.getJchjzmj() + "");
                buildInformation.setFWJG(buildBasic.getFwjg());
                buildInformation.setFWXZ(buildBasic.getFwxz());
                buildInformation.setGRSKYHZH(buildBasic.getGrskyhzh());
                buildInformation.setKHHYHMC(buildBasic.getKhhyhmc());
                buildInformation.setYHKHM(buildBasic.getYhkhm());
                buildInformation.setTJZL(buildBasic.getBlzl() + "");
                houseInformation.setBuildInformation(buildInformation);
            }
        }
        if ("2".equals(dkyt)) {
            CLoanHouseOverhaulBasic overhaulBasic = personInformationBasic.getOverhaul();
            if (overhaulBasic != null) {
//                houseInformation.setBLZL(overhaulBasic.getBlzl());
                GetLoanRecordDetailsResponsesHouseInformationOverhaulInformation overhaulInformation = new GetLoanRecordDetailsResponsesHouseInformationOverhaulInformation();
                overhaulInformation.setYBDCZH(overhaulBasic.getYbdczh());
                overhaulInformation.setDXGCYS(overhaulBasic.getDxgcys() == null ? null : overhaulBasic.getDxgcys() + "");
                overhaulInformation.setYJZMJ(overhaulBasic.getYjzmj() == null ? null : overhaulBasic.getYjzmj() + "");
                overhaulInformation.setTDSYZH(overhaulBasic.getTdsyzh());
                overhaulInformation.setJHKGRQ(DateUtil.date2Str(overhaulBasic.getJhkgrq(), format1));
                overhaulInformation.setJHJGRQ(DateUtil.date2Str(overhaulBasic.getJhjgrq(), format1));
                overhaulInformation.setFWZJBGJGMCJBH(overhaulBasic.getFwzjbgjgmcjbh());
                overhaulInformation.setFWZLDZ(overhaulBasic.getFwzl());
                overhaulInformation.setGRSKYHZH(overhaulBasic.getGrskyhzh());
                overhaulInformation.setKHHYHMC(overhaulBasic.getKhyhmc());
                overhaulInformation.setYHKHM(overhaulBasic.getYhkhm());
                overhaulInformation.setTJZL(overhaulBasic.getBlzl() + "");
                houseInformation.setOverhaulInformation(overhaulInformation);
            }
        }
        getLoanRecordDetailsResponses.setHouseInformation(houseInformation);

        //资金信息
        CLoanFundsInformationBasic funds = personInformationBasic.getFunds();
        if (funds != null) {
            GetLoanRecordDetailsResponsesCapitalInformation capitalInformation = new GetLoanRecordDetailsResponsesCapitalInformation();
            capitalInformation.setDKLX(funds.getDklx());
            capitalInformation.setDKDBLX(funds.getDkdblx());
            capitalInformation.setHKFS(funds.getHkfs());
            capitalInformation.setHTDKJE(funds.getHtdkje() == null ? null : funds.getHtdkje() + "");
            capitalInformation.setHTDKJEDX(funds.getHtdkjedx());
            capitalInformation.setDKQS(funds.getDkqs() == null ? null : funds.getDkqs() + "");
            capitalInformation.setFWTS(funds.getFwts());
            capitalInformation.setJKHTLL(funds.getJkhtll() == null ? null : funds.getJkhtll() + "");
            capitalInformation.setLLFSBL(funds.getLlfsbl() == null ? null : funds.getLlfsbl() + "");   //利率浮动比例
            capitalInformation.setZXLL(funds.getZxll() == null ? null : funds.getZxll() + "");
            capitalInformation.setWTKHYJCE(funds.getWtkhyjce() ? "1" : "0");
            getLoanRecordDetailsResponses.setCapitalInformation(capitalInformation);
        }

        //担保信息
        StHousingGuaranteeContract guaranteeContract = personInformationBasic.getGuaranteeContract();
        if (guaranteeContract != null) {
            GetLoanRecordDetailsResponsesCollateralInformation collateralInformation = new GetLoanRecordDetailsResponsesCollateralInformation();
            String dkdblx = guaranteeContract.getDkdblx();
            collateralInformation.setDKDBLX(dkdblx);
            //贷款担保类型 01 抵押 02质押 03保证
            if ("01".equals(dkdblx)) {

                List<CLoanGuaranteeMortgageExtension> mortgageExtensions = guaranteeContract.getcLoanGuaranteeMortgageExtensions();

                if (mortgageExtensions != null) {

                    ArrayList<GetLoanRecordDetailsResponsesCollateralInformationMortgageInformation> mortgageInformationList = new ArrayList<>();

                    for (CLoanGuaranteeMortgageExtension mortgageExtension : mortgageExtensions) {

                        GetLoanRecordDetailsResponsesCollateralInformationMortgageInformation mortgageInformation = new GetLoanRecordDetailsResponsesCollateralInformationMortgageInformation();

                        mortgageInformation.setDYWSYQRSFZHM(mortgageExtension.getDywsyqrsfzhm()/*抵押物所有权人身份证号码*/);
                        mortgageInformation.setDYFWXS(mortgageExtension.getDyfwxs() == null ? null : mortgageExtension.getDyfwxs() + ""/*抵押房屋形式（0：住宅 （期房） 1：住宅（现房） 2：商铺 3：其他）*/);
                        mortgageInformation.setDYWFWZL(mortgageExtension.getDywfwzl()/*抵押物房屋坐落*/);
                        mortgageInformation.setDYWMC(mortgageExtension.getDywmc()/*抵押物名称*/);
                        mortgageInformation.setFWJG(mortgageExtension.getFwjg() == null ? null : mortgageExtension.getFwjg() + ""/*房屋结构（0：框架 1：砖混 2：土木 3：其他）*/);
                        mortgageInformation.setDYWGYQRLXDH(mortgageExtension.getDywgyqrlxdh()/*抵押物共有权人联系电话*/);
                        mortgageInformation.setDYWPGJZ(mortgageExtension.getDywpgjz() == null ? null : mortgageExtension.getDywpgjz() + ""/*抵押物评估价值*/);
                        mortgageInformation.setDYWGYQRSFZHM(mortgageExtension.getDywgyqrsfzhm()/*抵押物共有权人身份证号码*/);
                        mortgageInformation.setDYWGYQRXM(mortgageExtension.getDywgyqrxm()); //抵押物共有权人姓名
                        mortgageInformation.setDYWSYQRXM(mortgageExtension.getDywsyqrxm()/*抵押物所有权人姓名*/);
                        mortgageInformation.setDYWSYQRLXDH(mortgageExtension.getDywsyqrlxdh()/*抵押物所有权人联系电话*/);
                        mortgageInformation.setFWMJ(mortgageExtension.getFwmj() == null ? null : mortgageExtension.getFwmj() + ""/*房屋面积*/);
                        mortgageInformation.setQSZSBH(mortgageExtension.getQszsbh()/*权属证书编号*/);
                        mortgageInformation.setUUID(mortgageExtension.getId());/*签订合同时根据该ID查询旧的已有的担保信息*/

                        mortgageInformationList.add(mortgageInformation);
                    }

                    collateralInformation.setMortgageInformations(mortgageInformationList);
                }

            } else if ("02".equals(dkdblx)) {

                List<CLoanGuaranteePledgeExtension> pledgeExtensions = guaranteeContract.getcLoanGuaranteePledgeExtensions();

                if (pledgeExtensions != null) {

                    ArrayList<GetLoanRecordDetailsResponsesCollateralInformationPledgeInformation> pledgeInformationList = new ArrayList<>();

                    for (CLoanGuaranteePledgeExtension pledgeExtension : pledgeExtensions) {

                        GetLoanRecordDetailsResponsesCollateralInformationPledgeInformation pledgeInformation = new GetLoanRecordDetailsResponsesCollateralInformationPledgeInformation();

                        pledgeInformation.setZYWSYQRSFZHM(pledgeExtension.getZywsyqrsfzhm()/*质押物所有权人身份证号码*/);
                        pledgeInformation.setZYWSYQRXM(pledgeExtension.getZywsyqrxm()/*质押物所有权人姓名*/);
                        pledgeInformation.setZYWJZ(pledgeExtension.getZywjz() == null ? null : pledgeExtension.getZywjz() + ""/*质押物价值*/);
                        pledgeInformation.setZYWSYQRLXDH(pledgeExtension.getZywsyqrlxdh()/*质押物所有权人联系电话*/);
                        pledgeInformation.setZYWMC(pledgeExtension.getZywmc()/*质押物名称*/);
                        pledgeInformation.setUUID(pledgeExtension.getId());/*签订合同时根据该ID查询旧的已有的担保信息*/

                        pledgeInformationList.add(pledgeInformation);
                    }

                    collateralInformation.setPledgeInformations(pledgeInformationList);
                }
            } else if ("03".equals(dkdblx)) {

                List<CLoanGuaranteeExtension> guaranteeExtensions = guaranteeContract.getcLoanGuaranteeExtensions();

                if (guaranteeExtensions != null) {

                    ArrayList<GetLoanRecordDetailsResponsesCollateralInformationGuaranteeInformation> guaranteeInformationList = new ArrayList<>();

                    for (CLoanGuaranteeExtension guaranteeExtension : guaranteeExtensions) {

                        GetLoanRecordDetailsResponsesCollateralInformationGuaranteeInformation guaranteeInformation = new GetLoanRecordDetailsResponsesCollateralInformationGuaranteeInformation();

                        guaranteeInformation.setYZBM(guaranteeExtension.getYzbm()/*邮政编码*/);
                        guaranteeInformation.setBZRXJZDZ(guaranteeExtension.getFrdbxjzdz()/*保证人现居住地址*/);
                        guaranteeInformation.setBZRSFZHM(guaranteeExtension.getFrdbsfzhm()/*保证人身份证号码*/);
                        guaranteeInformation.setBZRXM(guaranteeExtension.getFrdbxm()/*保证人姓名*/);
                        guaranteeInformation.setBZRLXDH(guaranteeExtension.getFrdblxdh()/*保证人联系电话*/);
                        guaranteeInformation.setTXDZ(guaranteeExtension.getTxdz()/*通讯地址*/);
                        guaranteeInformation.setBZFLX(guaranteeExtension.getBzflx()/*保证方类型（0：个人 1：机构）*/);
                        guaranteeInformation.setUUID(guaranteeExtension.getId());/*签订合同时根据该ID查询旧的已有的担保信息*/

                        guaranteeInformationList.add(guaranteeInformation);
                    }

                    collateralInformation.setGuaranteeInformations(guaranteeInformationList);
                }
            }
            if (guaranteeContract.getExtension() != null) {

                collateralInformation.setBLZL(guaranteeContract.getExtension().getBlzl() + ""/*提交资料*/);

            }

            getLoanRecordDetailsResponses.setCollateralInformation(collateralInformation);
        }

        //其他资料
        CLoanHousingBusinessProcess process = DAOBuilder.instance(icLoanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("ywlsh", personInformationBasic.getYwlsh());
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        getLoanRecordDetailsResponses.setQTZL(process != null ? process.getBlzl() + "" : ""/*其他资料*/);

        //购买合同和担保合同(PDF文件）
        if (personInformationBasic.getLoanContract() != null && personInformationBasic.getLoanContract().getcLoanHousingPersonalLoanExtension() != null) {
            String contractId = personInformationBasic.getLoanContract().getcLoanHousingPersonalLoanExtension().getDkhtwj();
            getLoanRecordDetailsResponses.setHTXX(contractId);
        }

        //贷款账户
        StHousingPersonalAccount personalAccount = personInformationBasic.getPersonalAccount();
        if (personalAccount != null) {
            GetLoanRecordDetailsResponsesLoanAccountInformation loanAccountInformation = new GetLoanRecordDetailsResponsesLoanAccountInformation();
            loanAccountInformation.setJKRXM(personInformationBasic.getJkrxm());
            loanAccountInformation.setJKRZJLX(personInformationBasic.getJkrzjlx());
            loanAccountInformation.setJKRZJHM(personInformationBasic.getJkrzjhm());
            loanAccountInformation.setDKZH(personalAccount.getDkzh());
            loanAccountInformation.setJKHTBH(personalAccount.getJkhtbh());
            loanAccountInformation.setDKFXDJ(personalAccount.getDkfxdj());
            loanAccountInformation.setStatus(personInformationBasic.getDkzhzt());
            loanAccountInformation.setDKFFE(personalAccount.getDkffe() == null ? null : personalAccount.getDkffe() + "");
            loanAccountInformation.setDKFFRQ(DateUtil.date2Str(personalAccount.getDkffrq(), format1));
            loanAccountInformation.setDKYE(personalAccount.getDkye() == null ? null : personalAccount.getDkye() + "");
            loanAccountInformation.setDKLL(personalAccount.getDkll() == null ? null : personalAccount.getDkll() + "");
            loanAccountInformation.setLLFDBL(personalAccount.getLlfdbl() == null ? null : personalAccount.getLlfdbl() + "");
            loanAccountInformation.setDKQS(personalAccount.getDkqs().intValue());
            if (personalAccount.getDkqs() != null && personInformationBasic.getYhqs() != null) {
                loanAccountInformation.setSYQS(personalAccount.getDkqs().intValue() - personInformationBasic.getYhqs().intValue()); //剩余期数 = 贷款期数 - 已还期数，重点验证
            }
            if (personInformationBasic.getDqyqqs() != null) {
                loanAccountInformation.setDQYQQS(personInformationBasic.getDqyqqs().intValue());    //当前逾期期数，重点验证
            }
            loanAccountInformation.setDQJHHKJE(personalAccount.getDqjhhkje() == null ? null : personalAccount.getDqjhhkje() + "");
            loanAccountInformation.setDQJHGHBJ(personalAccount.getDqjhghbj() == null ? null : personalAccount.getDqjhghbj() + "");
            loanAccountInformation.setDQJHGHLX(personalAccount.getDqjhghlx() == null ? null : personalAccount.getDqjhghlx() + "");
            loanAccountInformation.setDQYHJE(personalAccount.getDqyhje() == null ? null : personalAccount.getDqyhje() + "");
            loanAccountInformation.setDQYHBJ(personalAccount.getDqyhbj() == null ? null : personalAccount.getDqyhbj() + "");
            loanAccountInformation.setDQYHLX(personalAccount.getDqyhlx() == null ? null : personalAccount.getDqyhlx() + "");
            loanAccountInformation.setDQYHFX(personalAccount.getDqyhfx() == null ? null : personalAccount.getDqyhfx() + "");
            if (personalAccount.getLjyqqs() != null) {
                loanAccountInformation.setLJYQQS(personalAccount.getLjyqqs().intValue());
            }
            loanAccountInformation.setHSBJZE(personalAccount.getHsbjze() == null ? null : personalAccount.getHsbjze() + "");
            loanAccountInformation.setHSLXZE(personalAccount.getHslxze() == null ? null : personalAccount.getHslxze() + "");
            loanAccountInformation.setTQGHBJZE(personalAccount.getTqghbjze() == null ? null : personalAccount.getTqghbjze() + "");
            loanAccountInformation.setYQBJZE(personalAccount.getYqbjze() == null ? null : personalAccount.getYqbjze() + "");
            loanAccountInformation.setYQLXZE(personalAccount.getYqlxze() == null ? null : personalAccount.getYqlxze() + "");
            loanAccountInformation.setFXZE(personalAccount.getFxze() == null ? null : personalAccount.getFxze() + "");
            loanAccountInformation.setDKJQRQ(DateUtil.date2Str(personalAccount.getDkjqrq(), format1));

            getLoanRecordDetailsResponses.setLoanAccountInformation(loanAccountInformation);
        }

        return getLoanRecordDetailsResponses;
    }

    // completed: 2017/8/10 贷款记录列表 已完成，待测试
    @Override
    public PageRes<LoanRecord> listLoanRecord(String JKRXM, String JKRZJHM, String DKYT, String pageSize, String page, String Module, String YWWD, String SWTYH, String DKZH, String HTJE) {

        PageRes pageRes = new PageRes();

        List<CLoanHousingPersonInformationBasic> loanHousingPersonInformationBasicList = DAOBuilder.instance(this.icLoanHousingPersonInformationBasicDAO).searchFilter(new HashMap<String, Object>() {
            {

                if (StringUtil.notEmpty(DKYT)) {
                    this.put("dkyt", DKYT);
                }
                if (StringUtil.notEmpty(YWWD)) {
                    this.put("ywwd", YWWD);
                }
                if (StringUtil.notEmpty(SWTYH)) {
                    this.put("loanContract.swtyhdm", SWTYH);
                }
                if (StringUtil.notEmpty(HTJE)&&StringUtil.isDigits(HTJE,false)) {
                    this.put("loanContract.htdkje", new BigDecimal(HTJE));
                }
            }
        }).betweenDate(null, null).pageOption(pageRes, Integer.parseInt(pageSize), Integer.parseInt(page)).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {

                if (StringUtil.notEmpty(JKRZJHM)) {
                    if (ReviewSubModule.贷款_放款.getCode().equals(Module)){
                        criteria.add(Restrictions.like("jkrzjhm", "%" + JKRZJHM + "%"));
                    }
                    if (ReviewSubModule.归集_提取.getCode().equals(Module)){

                        criteria.createAlias("coborrower","coborrower", JoinType.LEFT_OUTER_JOIN);

                        criteria.add(Restrictions.or(
                                Restrictions.and(
                                        Restrictions.isNull("coborrower"),
                                        Restrictions.eq("jkrzjhm",  JKRZJHM )
                                ),

                                Restrictions.and(
                                        Restrictions.isNotNull("coborrower"),
                                        Restrictions.or(
                                                Restrictions.eq("jkrzjhm",  JKRZJHM ),
                                                Restrictions.eq("coborrower.gtjkrzjhm",  JKRZJHM )
                                        )
                                )
                        ));
                        // criteria.add(Restrictions.or(Restrictions.and(Restrictions.isNull(""))));
                        //criteria.add(Restrictions.eq("jkrzjhm", JKRZJHM));
                    }
                }

                if (StringUtil.notEmpty(JKRXM)) {
                    criteria.add(Restrictions.like("jkrxm", "%" + JKRXM + "%"));
                }

                if (StringUtil.notEmpty(DKZH)) {
                    criteria.add(Restrictions.like("dkzh", "%" + DKZH + "%"));
                }
            }
        }).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        List<CAccountNetwork>list_network = loanHousingPersonInformationBasicList.size()==0?new ArrayList<>():DAOBuilder.instance(this.icAccountNetworkDAO).searchFilter(new HashMap<String, Object>(){{

            this.put("id", (Collection)CollectionUtils.flatmap(loanHousingPersonInformationBasicList, new CollectionUtils.Transformer<CLoanHousingPersonInformationBasic, String>() {
                @Override
                public String tansform(CLoanHousingPersonInformationBasic var1) { return var1.getYwwd(); }

            }));
        }}).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) { throw new ErrorException(e);}
        });

        return new PageRes<LoanRecord>() {
            {
                this.setResults(new ArrayList<LoanRecord>() {
                    {
                        for (CLoanHousingPersonInformationBasic informationBasic : loanHousingPersonInformationBasicList) {

                            StHousingPersonalLoan contract = informationBasic.getLoanContract();

                            this.add(new LoanRecord() {
                                {
                                    this.setSPBWJ(informationBasic.getSpbwj()); //审批表文件
                                    this.setJKRXM(informationBasic.getJkrxm());
                                    this.setJKRZJHM(informationBasic.getJkrzjhm());
                                    this.setDKZH(informationBasic.getDkzh());
                                    this.setDKYT(informationBasic.getDkyt());
                                    this.setCZY(informationBasic.getCzy());
                                    this.setDKZHZT(informationBasic.getDkzhzt());
                                    this.setYWWD(CollectionUtils.find(list_network,new CAccountNetwork(), new CollectionUtils.Predicate<CAccountNetwork>() {
                                        @Override
                                        public boolean evaluate(CAccountNetwork var1) { return informationBasic.getYwwd().equals(var1.getId()); }
                                    }).getMingCheng());
                                    this.setSWTYH(informationBasic.getLoanContract() == null ? null : informationBasic.getLoanContract().getSwtyhmc());
                                    if (contract != null) {
                                        this.setHTWJ(contract.getcLoanHousingPersonalLoanExtension().getDkhtwj());
                                        this.setJKHTH(contract.getJkhtbh());
                                        this.setHTDKJE(contract.getHtdkje() == null ? null : contract.getHtdkje() + "");
                                        this.setDKQS(contract.getDkqs() == null ? null : contract.getDkqs() + "");
                                        this.setFKRQ(DateUtil.date2Str(contract.getYdfkrq(),format1));
                                        this.setDQRQ(DateUtil.date2Str(contract.getYddqrq(),format1));
                                    }

                                    this.setGTJKRXM(informationBasic.getCoborrower() == null? null:informationBasic.getCoborrower().getGtjkrxm());
                                    this.setGTJKRZJHM(informationBasic.getCoborrower() == null? null:informationBasic.getCoborrower().getGtjkrzjhm());

                                }
                            });
                        }
                    }
                });
                this.setCurrentPage(pageRes.getCurrentPage());
                this.setNextPageNo(pageRes.getNextPageNo());
                this.setPageCount(pageRes.getPageCount());
                this.setPageSize(pageRes.getPageSize());
                this.setTotalCount(pageRes.getTotalCount());
            }
        };
    }

    // completed: 2017/8/10 获取签订合同-借款人信息 已完成，待测试
    @Override
    public GetLoanContractRes getLoanContract(String DKZH) {

        CLoanHousingPersonInformationBasic personInformationBasic = DAOBuilder.instance(this.icLoanHousingPersonInformationBasicDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("dkzh", DKZH);
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (personInformationBasic == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有该贷款账号的相关信息");
        }

        return new GetLoanContractRes() {
            {
                this.setContractResPerson(new GetLoanContractResPerson() {
                    {
                        this.setJCD(personInformationBasic.getJcd());
                        this.setJKRGJJZH(personInformationBasic.getJkrgjjzh());
                        this.setBorrowerInformation(new GetApplicantResponseApplicantInformationBorrowerInformation() {
                            {
                                this.setJKRXM(personInformationBasic.getJkrxm());
                                this.setJKRZJLX(personInformationBasic.getJkrzjlx());
                                this.setJKRZJHM(personInformationBasic.getJkrzjhm());
                                // completed: 2017/9/11 时间格式加“-”
                                this.setCSNY(DateUtil.str2str(personInformationBasic.getCsny(), 6));
                                this.setXingBie(personInformationBasic.getXingBie() + "");
                                this.setXueLi(personInformationBasic.getXueLi());
                                this.setNianLing(personInformationBasic.getNianLing());
                                this.setJKZK(personInformationBasic.getJkzk());
                                this.setHYZK(personInformationBasic.getHyzk());
                                this.setZhiCheng(personInformationBasic.getZhiCheng());
                                this.setZhiWu(personInformationBasic.getZhiWu());
                                this.setYGXZ(personInformationBasic.getYgxz());
                                this.setZYJJLY(personInformationBasic.getZyjjly());
                                this.setYSR(personInformationBasic.getYsr() + "");
                                this.setJTYSR(personInformationBasic.getJtysr() + "");
                                this.setGDDHHM(personInformationBasic.getGddhhm());
                                this.setJTZZ(personInformationBasic.getJtzz());
                                this.setHKSZD(personInformationBasic.getHkszd());
                            }
                        });
                        this.setAccountInformation(new GetApplicantResponseApplicantInformationAccountInformation() {
                            {
                                // completed: 2017/9/11 时间格式加“-”
                                this.setJZNY(DateUtil.str2str(personInformationBasic.getJzny(), 6));
                                this.setGRZHZT(personInformationBasic.getGrzhzt());
                                this.setLXZCJCYS(personInformationBasic.getLxzcjcys() + "");    //连续正常缴存月数
                                this.setYJCE(personInformationBasic.getYjce() + "");
                                this.setGRJCJS(personInformationBasic.getGrjcjs() + "");
                                this.setGRZHYE(personInformationBasic.getGrzhye() + "");
                            }
                        });
                        this.setUnitInformation(new GetApplicantResponseApplicantInformationUnitInformation() {
                            {
                                this.setDWMC(personInformationBasic.getDwmc());
                                this.setDWZH(personInformationBasic.getDwzh());
                                this.setDWDH(personInformationBasic.getDwdh());
                                this.setDWXZ(personInformationBasic.getDwxz());
                                this.setDWDZ(personInformationBasic.getDwdz());
                            }
                        });
                        if (personInformationBasic.getLoanContract() != null) {
                            this.setHKZH(personInformationBasic.getLoanContract().getHkzh());
                            this.setZHKHYHMC(personInformationBasic.getLoanContract().getZhkhyhmc());
                        }
                        if (personInformationBasic.getFunds() != null) {
                            this.setWTKHYJCE(personInformationBasic.getFunds().getWtkhyjce() ? "1" : "0"); //委托扣划月缴存额，boolean
                        }
                        this.setBLZL(personInformationBasic.getBlzl() + "");
                    }
                });
                StHousingCoborrower coborrower = personInformationBasic.getCoborrower();
                if (coborrower != null) {
                    this.setContractResCoborrower(new GetLoanContractResCoborrower() {
                        {
                            this.setCDGX(coborrower.getCdgx());
                            this.setGTJKRGJJZH(coborrower.getGtjkrgjjzh());
                            this.setGTJKRXM(coborrower.getGtjkrxm());
                            this.setGTJKRZJLX(coborrower.getGtjkrzjlx());
                            this.setGTJKRZJHM(coborrower.getGtjkrzjhm());
                            this.setGDDHHM(coborrower.getGddhhm());
                            this.setSJHM(coborrower.getSjhm());
                            this.setYSR(coborrower.getYsr() + "");
                            if (coborrower.getExtension() != null) {
                                this.setJCD(coborrower.getExtension().getJcd());
                                this.setHKSZD(coborrower.getExtension().getHkszd());
                                this.setTJZL(coborrower.getExtension().getBlzl() + "");
                                this.setDWXX(new CoborrowerUnitInformation() {
                                    {
                                        this.setDWMC(coborrower.getExtension().getDwmc());
                                        this.setDWZH(coborrower.getExtension().getDwzh());
                                        this.setDWDH(coborrower.getExtension().getDwdh());
                                        this.setDWLB(coborrower.getExtension().getDwxz());
                                        this.setDWDZ(coborrower.getExtension().getDwdz());
                                    }
                                });
                                this.setGJJZHXX(new CoborrowerAccountInformation() {
                                    {
                                        this.setJZNY(coborrower.getExtension().getJzny());
                                        this.setGRZHZT(coborrower.getExtension().getGrzhzt());
                                        this.setLXZCJCYS(coborrower.getExtension().getLxzcjcys() + "");
                                        this.setYJCE(coborrower.getExtension().getYjce() + "");
                                        this.setGRJCJS(coborrower.getExtension().getGrjcjs() + "");
                                        this.setGRZHYE(coborrower.getExtension().getGrzhye() + "");
                                    }
                                });
                            }
                        }
                    });
                }
            }
        };
    }

    @Override
    public void GetLoanReviewPDF() {

    }

    @Override
    public PageRes<GetCommonHistory> getLoanRecordHistory(String DKZH, String pageSize, String pageNo) {

        ArrayList<GetCommonHistory> results = new ArrayList();

        PageRes pageRes = new PageRes();

        List<CLoanHousingBusinessProcess> entities = DAOBuilder.instance(icLoanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("dkzh", DKZH);
            }
        }).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        ArrayList<String> ywlshs = new ArrayList();

        for (CLoanHousingBusinessProcess entity : entities) {
            if (StringUtil.notEmpty(entity.getYwlsh()))
                ywlshs.add(entity.getYwlsh());
        }

        if (!ywlshs.isEmpty()) {
            List<CAuditHistory> histories = DAOBuilder.instance(auditHistoryDAO).searchFilter(new HashMap<String, Object>() {
                {
                    this.put("ywlsh", ywlshs);
                }
            }).pageOption(pageRes, Integer.parseInt(pageSize), Integer.parseInt(pageNo)).getList(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {

                    throw new ErrorException(e);
                }
            });

            for (CAuditHistory history : histories) {
                GetCommonHistory getCommonHistory = new GetCommonHistory();
                getCommonHistory.setYWLX(history.getYwlx());
                getCommonHistory.setSLSJ(DateUtil.date2Str(history.getShsj() == null ? history.getDdsj() : history.getShsj(), "yyyy-MM-dd HH:mm"));
                getCommonHistory.setCZNR(history.getCaoZuo());
                getCommonHistory.setCZY(history.getCzy());
                try {
                    String ywwdmc = icAccountNetworkDAO.get(history.getYwwd()).getMingCheng();
                    getCommonHistory.setYWWD(ywwdmc);
                } catch (Exception e) {
                    getCommonHistory.setYWWD("缺失");
                }
                getCommonHistory.setCZQD(history.getCzqd());
                getCommonHistory.setYWLSH(history.getYwlsh());
                results.add(getCommonHistory);
            }
        }

        pageRes.setResults(results);
        pageRes.setCurrentPage(pageRes.getCurrentPage());
        pageRes.setNextPageNo(pageRes.getNextPageNo());
        pageRes.setPageCount(pageRes.getPageCount());
        pageRes.setPageSize(pageRes.getPageSize());
        pageRes.setTotalCount(pageRes.getTotalCount());

        return pageRes;
    }
}