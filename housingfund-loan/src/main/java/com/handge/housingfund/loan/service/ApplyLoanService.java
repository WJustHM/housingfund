package com.handge.housingfund.loan.service;

import com.handge.housingfund.common.service.ISaveAuditHistory;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessStatus;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.collection.enumeration.PersonCertificateType;
import com.handge.housingfund.common.service.collection.service.unitdeposit.UnitRemittance;
import com.handge.housingfund.common.service.loan.IApplyLoanService;
import com.handge.housingfund.common.service.loan.ILoanReviewService;
import com.handge.housingfund.common.service.loan.enums.LoanAccountType;
import com.handge.housingfund.common.service.loan.enums.LoanBusinessType;
import com.handge.housingfund.common.service.loan.enums.LoanBussinessStatus;
import com.handge.housingfund.common.service.loan.enums.LoanGuaranteeType;
import com.handge.housingfund.common.service.loan.model.*;
import com.handge.housingfund.common.service.others.IDictionaryService;
import com.handge.housingfund.common.service.others.IPdfService;
import com.handge.housingfund.common.service.others.IUploadImagesService;
import com.handge.housingfund.common.service.others.enums.UploadFileBusinessType;
import com.handge.housingfund.common.service.others.enums.UploadFileModleType;
import com.handge.housingfund.common.service.others.model.CommonDictionary;
import com.handge.housingfund.common.service.others.model.Review;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.BusinessSubType;
import com.handge.housingfund.database.enums.BusinessType;
import com.handge.housingfund.database.enums.Events;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.database.utils.CriteriaUtils;
import com.handge.housingfund.database.utils.DAOBuilder;
import com.handge.housingfund.loan.utils.StateMachineUtils;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import com.handge.housingfund.statemachineV2.IStateMachineService;
import org.apache.poi.ss.formula.functions.T;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Consumer;

@Component
@SuppressWarnings("ALL")
public class ApplyLoanService implements IApplyLoanService {


    @Autowired
    private ICLoanHousePurchasingViceDAO loanHousePurchasingViceDAO;

    @Autowired
    private ICLoanGuaranteeMortgageViceDAO loanGuaranteeMortgageViceDAO;

    @Autowired
    private ICLoanHouseBuildViceDAO loanHouseBuildViceDAO;

    @Autowired
    private ICLoanFundsViceDAO loanFundsViceDAO;

    @Autowired
    private ICLoanGuaranteeViceDAO loanGuaranteeViceDAO;

    @Autowired
    private ICLoanGuaranteePledgeViceDAO loanGuaranteePledgeViceDAO;

    @Autowired
    private ICLoanHouseOverhaulViceDAO loanHouseOverhaulViceDAO;

    @Autowired
    private ICLoanHousingBusinessProcessDAO loanHousingBusinessProcessDAO;

    @Autowired
    private ICLoanHousingCoborrowerViceDAO loanHousingCoborrowerViceDAO;

    @Autowired
    private IStCommonPersonDAO commonPersonDAO;

    @Autowired
    private ILoanReviewService loanReviewService;

    @Autowired
    private IDictionaryService iDictionaryService;

    @Autowired
    @Resource(name = "stateMachineServiceV2")
    private IStateMachineService iStateMachineService;

    @Autowired
    private ICBankBankInfoDAO bankBankInfoDAO;

    @Autowired
    private IPdfService iPdfService;

    @Autowired
    private ISaveAuditHistory iSaveAuditHistory;

    @Autowired
    private UnitRemittance unitRemittance;

    @Autowired
    private IStCommonPolicyDAO commonPolicyDAO;

    @Autowired
    private ICLoanHousingPersonInformationBasicDAO loanHousingPersonInformationBasicDAO;

    @Autowired
    protected IStCollectionPersonalBusinessDetailsDAO collectionPersonalBusinessDetailsDAO;

    private static String format = "yyyy-MM-dd";

    @Autowired
    private IUploadImagesService iUploadImagesService;

    private static String formatNY = "yyyy-MM";

    private static String formatM = "yyyy-MM-dd HH:mm";
    private static String formatNYRSF = "yyyy-MM-dd HH:mm";
    @Autowired
    private ICAccountNetworkDAO cAccountNetworkDAO;

    /*
    *  completed
    *
    * !逻辑已完成
    *
    * 已测试 lian
    *
    * */
    @Override
    public CommonResponses putModifyApplication(TokenContext tokenContext, String status, String YWLSH, ApplicantPost body) {

        //region //参数验证

        boolean hasCoborrower = "20".equals(body.getApplicantInformation().getInformation().getBorrowerInformation().getHYZK());// StringUtil.notEmpty(body.getCommonBorrowerInformation().getCoBorrowerInformation().getGTJKRGJJZH());//是否有共同借款人

        boolean allowNull = status == null || status.equals("0");//允许为空

        boolean isBuild = body.getHouseInformation().getDKYT(/*贷款用途*/).equals("1");//自建

        boolean isOverhaul = (!isBuild) && body.getHouseInformation().getDKYT(/*贷款用途*/).equals("2");//大修

        boolean isSecondHands = (!isBuild) && (!isOverhaul) && body.getHouseInformation().getDKYT(/*贷款用途*/).equals("0") && body.getHouseInformation().getSFWESF(/*是否为二手房*/).equals("1");//购买二手

        boolean isFirstHands = (!isSecondHands) && (!isBuild) && (!isOverhaul) && body.getHouseInformation().getDKYT(/*贷款用途*/).equals("0") && body.getHouseInformation().getSFWESF(/*是否为二手房*/).equals("0");//购买一手

        boolean isGuaranteeMortgage = body.getCollateralInformation().getDKDBLX(/*贷款担保类型*/).equals(LoanGuaranteeType.抵押.getCode());//抵押

        boolean isGuaranteePledge = (!isGuaranteeMortgage) && body.getCollateralInformation().getDKDBLX(/*贷款担保类型*/).equals("1");//质押


        //时间

        if (!DateUtil.isFollowFormat(body.getApplicantInformation().getInformation().getBorrowerInformation().getCSNY(), formatNY, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "出生年月");
        }

        if (!DateUtil.isFollowFormat(body.getApplicantInformation().getInformation().getAccountInformation().getJZNY(), formatNY, true)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "缴至年月");
        }

        if (!DateUtil.isFollowFormat(body.getCommonBorrowerInformation().getAccountInformation().getJZNY(), formatNY, (!hasCoborrower) || true)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "缴至年月 ");
        }

        if (!DateUtil.isFollowFormat(body.getHouseInformation().getPurchaseSecondInformation().getFWJGRQ(), format, (!isSecondHands) || true)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "房屋竣工日期");
        }

        if (!DateUtil.isFollowFormat(body.getHouseInformation().getPurchaseFirstInformation().getFWJGRQ(), format, (!isFirstHands) || true)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "房屋竣工日期");
        }

        if (!DateUtil.isFollowFormat(body.getHouseInformation().getBuildInformation().getJHKGRQ(), format, (!isBuild) || true)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "计划开工日期");
        }

        if (!DateUtil.isFollowFormat(body.getHouseInformation().getOverhaulInformation().getJHKGRQ(), format, (!isOverhaul) || true)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "计划开工日期");
        }

        if (!DateUtil.isFollowFormat(body.getHouseInformation().getOverhaulInformation().getJHJGRQ(), format, (!isOverhaul) || true)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "计划竣工日期");
        }


        //number

        if (!StringUtil.isDigits(body.getApplicantInformation().getInformation().getBorrowerInformation().getYSR(), true)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "月收入");
        }

        if (!StringUtil.isDigits(body.getApplicantInformation().getInformation().getBorrowerInformation().getJTYSR(), allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "家庭月收入");
        }

//        if (!StringUtil.isDigits(body.getApplicantInformation().getInformation().getAccountInformation().getYJCE(), true)) {
//            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "月缴存额");
//        }

        if (!StringUtil.isDigits(body.getApplicantInformation().getInformation().getAccountInformation().getGRJCJS(), allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "个人缴存基数");
        }

        if (!StringUtil.isDigits(body.getCommonBorrowerInformation().getCoBorrowerInformation().getYSR(), (!hasCoborrower) || true)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "月收入 ");
        }

//        if (!StringUtil.isDigits(body.getCommonBorrowerInformation().getAccountInformation().getYJCE(), (!hasCoborrower) || true)) {
//            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "月缴存额 ");
//        }

        if (!StringUtil.isDigits(body.getCommonBorrowerInformation().getAccountInformation().getGRJCJS(), (!hasCoborrower) || true)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "个人缴存基数 ");
        }

//        if (!StringUtil.isDigits(body.getCommonBorrowerInformation().getAccountInformation().getLXZCJCYS(), (!hasCoborrower) || true)) {
//            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "连续正常缴存月数 ");
//        }

        if (!StringUtil.isDigits(body.getHouseInformation().getPurchaseSecondInformation().getFWJZMJ(), (!isSecondHands) || allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "房屋建筑面积");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getPurchaseSecondInformation().getFWTNMJ(), (!isSecondHands) || allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "房屋套内面积");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getPurchaseSecondInformation().getFWZJ(), (!isSecondHands) || allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "房屋总价");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getPurchaseSecondInformation().getHTJE(), (!isSecondHands) || allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "合同金额");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getPurchaseSecondInformation().getYFK(), (!isSecondHands) || allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "已付款（全部付完）");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getPurchaseSecondInformation().getDanJia(), (!isSecondHands) || true)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "单价");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getSFWESF(), ((!isFirstHands && !isSecondHands)) || allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "是否为二手房");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getPurchaseFirstInformation().getFWJZMJ(), (!isFirstHands) || allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "房屋建筑面积");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getPurchaseFirstInformation().getDanJia(), (!isFirstHands) || true)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "单价");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getPurchaseFirstInformation().getFWTNMJ(), (!isFirstHands) || allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "房屋套内面积");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getPurchaseFirstInformation().getFWZJ(), (!isFirstHands) || allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "房屋总价");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getPurchaseFirstInformation().getHTJE(), (!isFirstHands) || allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "合同金额");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getPurchaseFirstInformation().getSFK(), (!isFirstHands) || allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "首付款");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getBuildInformation().getGRSYZJ(), (!isBuild) || allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "个人使用资金");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getBuildInformation().getJHJZFY(), (!isBuild) || allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "计划建造费用");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getBuildInformation().getJCHJZMJ(), (!isBuild) || allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "建成后建造面积");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getOverhaulInformation().getDXGCYS(), (!isOverhaul) || allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "大修工程预算");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getOverhaulInformation().getYJZMJ(), (!isOverhaul) || true)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "原建筑面积");
        }

//        if(!StringUtil.isDigits(body.getCollateralInformation().getMortgageInformation().getDYWPGJZ(),(!isGuaranteeMortgage)||allowNull)){throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"抵押物评估价值");}
//
//        if(!StringUtil.isDigits(body.getCollateralInformation().getMortgageInformation().getFWMJ(),(!isGuaranteeMortgage)||allowNull)){throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"房屋面积");}
//
//        if(!StringUtil.isDigits(body.getCollateralInformation().getPledgeInformation().getZYWJZ(),(!isGuaranteePledge)||allowNull)){throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"质押物价值");}
//
        if (!StringUtil.isDigits(body.getCapitalInformation().getHTDKJE(), allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "合同贷款金额");
        }

        if (!StringUtil.isDigits(body.getCapitalInformation().getLLFSBL(), allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "利率浮动比例");
        }

        if (!StringUtil.isDigits(body.getCapitalInformation().getJKHTLL(), allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "借款合同利率");
        }

        if (!StringUtil.isDigits(body.getCapitalInformation().getWTKHYJCE(), allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "委托划扣月缴存额");
        }

        // if (!StringUtil.isDigits(body.getCapitalInformation().getZXLL(), allowNull)) {  throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "执行利率");   }


        //endregion

        //region //查询必要字段

        CLoanHousingBusinessProcess loanHousingBusinessProcess = DAOBuilder.instance(this.loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("ywlsh", YWLSH);

            this.put("cznr", LoanBusinessType.贷款发放.getCode());

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        //endregion

        //region //数据完整性验证

        if (loanHousingBusinessProcess == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }

        if (loanHousingBusinessProcess.getLoanHousingPersonInformationVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }

        if (loanHousingBusinessProcess.getLoanHousePurchasingVice() == null && loanHousingBusinessProcess.getLoanHouseBuildVice() == null && loanHousingBusinessProcess.getLoanHouseOverhaulVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }

        if (loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }

        if (loanHousingBusinessProcess.getLoanFundsVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }

        if(!tokenContext.getUserInfo().getCZY().equals(loanHousingBusinessProcess.getCzy())){

            throw new ErrorException(ReturnEnumeration.Permission_Denied);

        }
        //endregion

        //region //必要数据声明&关系配置
        CLoanHousingPersonInformationVice loanHousingPersonInformationVice = loanHousingBusinessProcess.getLoanHousingPersonInformationVice() == null ? new CLoanHousingPersonInformationVice() : loanHousingBusinessProcess.getLoanHousingPersonInformationVice();
        loanHousingPersonInformationVice.setGrywmx(loanHousingBusinessProcess);
        if (loanHousingBusinessProcess.getLoanHousingPersonInformationVice() == null) {

            loanHousingBusinessProcess.setLoanHousingPersonInformationVice(loanHousingPersonInformationVice);
        }

        CLoanHousePurchasingVice loanHousePurchasingVice = loanHousingBusinessProcess.getLoanHousePurchasingVice() == null ? new CLoanHousePurchasingVice() : loanHousingBusinessProcess.getLoanHousePurchasingVice();
        loanHousePurchasingVice.setGrywmx(loanHousingBusinessProcess);
        if (loanHousingBusinessProcess.getLoanHousePurchasingVice() == null) {
            loanHousingBusinessProcess.setLoanHousePurchasingVice(loanHousePurchasingVice);
        }

        CLoanHouseOverhaulVice loanHouseOverhaulVice = loanHousingBusinessProcess.getLoanHouseOverhaulVice() == null ? new CLoanHouseOverhaulVice() : loanHousingBusinessProcess.getLoanHouseOverhaulVice();
        loanHouseOverhaulVice.setGrywmx(loanHousingBusinessProcess);
        if (loanHousingBusinessProcess.getLoanHouseOverhaulVice() == null) {

            loanHousingBusinessProcess.setLoanHouseOverhaulVice(loanHouseOverhaulVice);
        }

        CLoanHousingCoborrowerVice loanHousingCoborrowerVice = loanHousingBusinessProcess.getLoanHousingCoborrowerVice() == null ? new CLoanHousingCoborrowerVice() : loanHousingBusinessProcess.getLoanHousingCoborrowerVice();
        loanHousingCoborrowerVice.setGrywmx(loanHousingBusinessProcess);
        if (loanHousingBusinessProcess.getLoanHousingCoborrowerVice() == null) {

            loanHousingBusinessProcess.setLoanHousingCoborrowerVice(loanHousingCoborrowerVice);
        }


        CLoanHouseBuildVice loanHouseBuildVice = loanHousingBusinessProcess.getLoanHouseBuildVice() == null ? new CLoanHouseBuildVice() : loanHousingBusinessProcess.getLoanHouseBuildVice();
        loanHouseBuildVice.setGrywmx(loanHousingBusinessProcess);
        if (loanHousingBusinessProcess.getLoanHouseBuildVice() == null) {

            loanHousingBusinessProcess.setLoanHouseBuildVice(loanHouseBuildVice);
        }


        CLoanFundsVice loanFundsVice = loanHousingBusinessProcess.getLoanFundsVice() == null ? new CLoanFundsVice() : loanHousingBusinessProcess.getLoanFundsVice();
        loanFundsVice.setGrywmx(loanHousingBusinessProcess);
        if (loanHousingBusinessProcess.getLoanFundsVice() == null) {

            loanHousingBusinessProcess.setLoanFundsVice(loanFundsVice);
        }

        CLoanHousingGuaranteeContractVice loanHousingGuaranteeContractVice = loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice() == null ? new CLoanHousingGuaranteeContractVice() : loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice();
        loanHousingGuaranteeContractVice.setGrywmx(loanHousingBusinessProcess);
        if (loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice() == null) {
            loanHousingBusinessProcess.setLoanHousingGuaranteeContractVice(loanHousingGuaranteeContractVice);
        }


        List<CLoanGuaranteeMortgageVice> list_loanGuaranteeMortgageVice = loanHousingGuaranteeContractVice.getGuaranteeMortgageVices().size() == 0 ? new ArrayList<CLoanGuaranteeMortgageVice>() : loanHousingGuaranteeContractVice.getGuaranteeMortgageVices();
        loanHousingGuaranteeContractVice.setGuaranteeMortgageVices(list_loanGuaranteeMortgageVice);


        List<CLoanGuaranteeVice> list_loanGuaranteeVice = loanHousingGuaranteeContractVice.getGuaranteeVices().size() == 0 ? new ArrayList<CLoanGuaranteeVice>() : loanHousingGuaranteeContractVice.getGuaranteeVices();
        loanHousingGuaranteeContractVice.setGuaranteeVices(list_loanGuaranteeVice);


        List<CLoanGuaranteePledgeVice> list_loanGuaranteePledgeVice = loanHousingGuaranteeContractVice.getGuaranteePledgeVices().size() == 0 ? new ArrayList<CLoanGuaranteePledgeVice>() : loanHousingGuaranteeContractVice.getGuaranteePledgeVices();
        loanHousingGuaranteeContractVice.setGuaranteePledgeVices(list_loanGuaranteePledgeVice);


        DAOBuilder.instance(this.loanGuaranteeViceDAO).entities(list_loanGuaranteeVice).delete(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        DAOBuilder.instance(this.loanGuaranteePledgeViceDAO).entities(list_loanGuaranteePledgeVice).delete(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        DAOBuilder.instance(this.loanGuaranteeMortgageViceDAO).entities(list_loanGuaranteeMortgageVice).delete(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });


        //endregion

        //region //字段填充

        //common
        loanHousingBusinessProcess.setCznr(LoanBusinessType.贷款发放.getCode());
        //loanHousingBusinessProcess.setBlsj(new Date());


        //region//申请人信息
        loanHousingPersonInformationVice.setHyzk(body.getApplicantInformation().getInformation().getBorrowerInformation().getHYZK(/*婚姻状态（0：已婚 1：未婚 2：丧偶 3：离婚 4：未说明的婚姻状况）*/));
        loanHousingPersonInformationVice.setNianLing(body.getApplicantInformation().getInformation().getBorrowerInformation().getNianLing(/*年龄*/));
        loanHousingPersonInformationVice.setJtzz(body.getApplicantInformation().getInformation().getBorrowerInformation().getJTZZ(/*家庭住址*/));
        loanHousingPersonInformationVice.setCsny(DateUtil.safeStr2DBDate(formatNY, body.getApplicantInformation().getInformation().getBorrowerInformation().getCSNY(/*出生年月*/), DateUtil.dbformatYear_Month));
        loanHousingPersonInformationVice.setJkrzjhm(body.getApplicantInformation().getInformation().getBorrowerInformation().getJKRZJHM(/*借款人证件号码*/));
        loanHousingPersonInformationVice.setYgxz(body.getApplicantInformation().getInformation().getBorrowerInformation().getYGXZ(/*用工性质（0：正式职工 1：合同制 2：聘用制）*/));
        loanHousingPersonInformationVice.setYsr(StringUtil.safeBigDecimal(body.getApplicantInformation().getInformation().getBorrowerInformation().getYSR(/*月收入*/)));
        loanHousingPersonInformationVice.setJkrxm(body.getApplicantInformation().getInformation().getBorrowerInformation().getJKRXM(/*借款人姓名*/));
        loanHousingPersonInformationVice.setHkszd(body.getApplicantInformation().getInformation().getBorrowerInformation().getHKSZD(/*户口所在地*/));
        loanHousingPersonInformationVice.setZhiCheng(body.getApplicantInformation().getInformation().getBorrowerInformation().getZhiCheng(/*职称*/));
        loanHousingPersonInformationVice.setJkrzjlx(body.getApplicantInformation().getInformation().getBorrowerInformation().getJKRZJLX(/*借款人证件类型*/));
        loanHousingPersonInformationVice.setSjhm(body.getApplicantInformation().getInformation().getBorrowerInformation().getSJHM(/*手机号码*/));
        loanHousingPersonInformationVice.setJkzk(body.getApplicantInformation().getInformation().getBorrowerInformation().getJKZK(/*健康状态（0：良好 1：一般 2：差）*/));
        loanHousingPersonInformationVice.setGddhhm(body.getApplicantInformation().getInformation().getBorrowerInformation().getGDDHHM(/*固定电话号码*/));
        loanHousingPersonInformationVice.setZyjjly(body.getApplicantInformation().getInformation().getBorrowerInformation().getZYJJLY(/*主要经济来源（0：工资收入 1：个体经营收入 2：其他非工资收入）*/));
        loanHousingPersonInformationVice.setXingBie(body.getApplicantInformation().getInformation().getBorrowerInformation().getXingBie(/*性别*/).toCharArray()[0]);
        loanHousingPersonInformationVice.setJtysr(StringUtil.safeBigDecimal(body.getApplicantInformation().getInformation().getBorrowerInformation().getJTYSR(/*家庭月收入*/)));
        loanHousingPersonInformationVice.setXueLi(body.getApplicantInformation().getInformation().getBorrowerInformation().getXueLi(/*学历*/));
        loanHousingPersonInformationVice.setZhiWu(body.getApplicantInformation().getInformation().getBorrowerInformation().getZhiWu(/*职务*/));


        loanHousingPersonInformationVice.setBlzl(body.getApplicantInformation().getInformation().getSCZL(/* 办理资料*/));


        loanHousingPersonInformationVice.setGrzhzt(body.getApplicantInformation().getInformation().getAccountInformation().getGRZHZT(/*个人账户状态*/));
        loanHousingPersonInformationVice.setJzny(DateUtil.safeStr2DBDate(formatNY, body.getApplicantInformation().getInformation().getAccountInformation().getJZNY(/*缴至年月*/), DateUtil.dbformatYear_Month));
        loanHousingPersonInformationVice.setYjce(StringUtil.safeBigDecimal(body.getApplicantInformation().getInformation().getAccountInformation().getYJCE(/*月缴存额*/)));
        loanHousingPersonInformationVice.setGrzhye(StringUtil.safeBigDecimal(body.getApplicantInformation().getInformation().getAccountInformation().getGRZHYE(/*个人账户余额*/)));
        loanHousingPersonInformationVice.setGrjcjs(StringUtil.safeBigDecimal(body.getApplicantInformation().getInformation().getAccountInformation().getGRJCJS(/*个人缴存基数*/)));
        loanHousingPersonInformationVice.setLxzcjcys(StringUtil.safeBigDecimal(body.getApplicantInformation().getInformation().getAccountInformation().getLXZCJCYS(/*连续正常缴存月数*/)));


        loanHousingPersonInformationVice.setDwdh(body.getApplicantInformation().getInformation().getUnitInformation().getDWDH(/*单位电话*/));
        loanHousingPersonInformationVice.setDwmc(body.getApplicantInformation().getInformation().getUnitInformation().getDWMC(/*单位名称*/));
        loanHousingPersonInformationVice.setDwzh(body.getApplicantInformation().getInformation().getUnitInformation().getDWZH(/*单位账号*/));
        loanHousingPersonInformationVice.setDwxz(body.getApplicantInformation().getInformation().getUnitInformation().getDWXZ(/*单位性质*/));
        loanHousingPersonInformationVice.setDwdz(body.getApplicantInformation().getInformation().getUnitInformation().getDWDZ(/*单位地址*/));


        loanHousingPersonInformationVice.setJkrgjjzh(body.getApplicantInformation().getJKRGJJZH(/*借款人公积金账号*/));
        loanHousingPersonInformationVice.setJcd(body.getApplicantInformation().getJCD(/*缴存地*/));

        //endregion

        //region //共同借款人信息

        loanHousingCoborrowerVice.setGtjkrgjjzh(body.getCommonBorrowerInformation().getCoBorrowerInformation().getGTJKRGJJZH(/*共同借款人公积金账号 */));
        loanHousingCoborrowerVice.setCdgx(body.getCommonBorrowerInformation().getCoBorrowerInformation().getCHGX(/*参货关系 （0：本人或户主 1：配偶 2：子 3：女 4：孙子、孙女或外孙子、外孙女 5：父母 6：祖父母或外祖父母 7：兄、弟、姐、妹 8：其他） */));
        loanHousingCoborrowerVice.setSjhm(body.getCommonBorrowerInformation().getCoBorrowerInformation().getSJHM(/*手机号码 */));
        loanHousingCoborrowerVice.setJcd(body.getCommonBorrowerInformation().getCoBorrowerInformation().getJCD(/*缴存地 */));
        loanHousingCoborrowerVice.setGddhhm(body.getCommonBorrowerInformation().getCoBorrowerInformation().getGDDHHM(/*固定电话号码 */));
        loanHousingCoborrowerVice.setGtjkrxm(body.getCommonBorrowerInformation().getCoBorrowerInformation().getGTJKRXM(/*共同借款人姓名 */));
        loanHousingCoborrowerVice.setGtjkrzjlx(body.getCommonBorrowerInformation().getCoBorrowerInformation().getGTJKRZJLX(/*共同借款人证件类型 */));
        loanHousingCoborrowerVice.setYsr(StringUtil.safeBigDecimal(body.getCommonBorrowerInformation().getCoBorrowerInformation().getYSR(/*月收入 */)));
        loanHousingCoborrowerVice.setGtjkrzjhm(body.getCommonBorrowerInformation().getCoBorrowerInformation().getGTJKRZJHM(/*共同借款人证件号码 */));
        loanHousingCoborrowerVice.setHkszd(body.getCommonBorrowerInformation().getCoBorrowerInformation().getHKSZD()/*户口所在地*/);

        loanHousingCoborrowerVice.setBlzl(body.getCommonBorrowerInformation().getSCZL(/*提交资料*/));


        loanHousingCoborrowerVice.setGrzhzt(body.getCommonBorrowerInformation().getAccountInformation().getGRZHZT(/*个人账户状态 */));
        loanHousingCoborrowerVice.setJzny(DateUtil.safeStr2DBDate(formatNY, body.getCommonBorrowerInformation().getAccountInformation().getJZNY(/*缴至年月 */), DateUtil.dbformatYear_Month));
        loanHousingCoborrowerVice.setYjce(StringUtil.safeBigDecimal(body.getCommonBorrowerInformation().getAccountInformation().getYJCE(/*月缴存额 */)));
        loanHousingCoborrowerVice.setGrzhye(StringUtil.safeBigDecimal(body.getCommonBorrowerInformation().getAccountInformation().getGRZHYE(/*个人账户余额 */)));
        loanHousingCoborrowerVice.setGrjcjs(StringUtil.safeBigDecimal(body.getCommonBorrowerInformation().getAccountInformation().getGRJCJS(/*个人缴存基数 */)));
        loanHousingCoborrowerVice.setLxzcjcys(StringUtil.safeBigDecimal(body.getCommonBorrowerInformation().getAccountInformation().getLXZCJCYS(/*连续正常缴存月数 */)));


        loanHousingCoborrowerVice.setDwdh(body.getCommonBorrowerInformation().getUnitInformation().getDWDH(/*单位电话 */));
        loanHousingCoborrowerVice.setDwmc(body.getCommonBorrowerInformation().getUnitInformation().getDWMC(/*单位名称 */));
        loanHousingCoborrowerVice.setDwzh(body.getCommonBorrowerInformation().getUnitInformation().getDWZH(/*单位账号 */));
        loanHousingCoborrowerVice.setDwxz(body.getCommonBorrowerInformation().getUnitInformation().getDWXZ(/*单位性质 */));
        loanHousingCoborrowerVice.setDwdz(body.getCommonBorrowerInformation().getUnitInformation().getDWDZ(/*单位地址 */));

        if (!hasCoborrower) {

            loanHousingBusinessProcess.setLoanHousingCoborrowerVice(null);

            DAOBuilder.instance(this.loanHousingCoborrowerViceDAO).entity(loanHousingCoborrowerVice).delete(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) { throw new ErrorException(e); }
            });

            loanHousingBusinessProcess.setLoanHousingCoborrowerVice(null);
        }
        //endregion

        //region //房屋信息

        loanHousingBusinessProcess.setDkyt(body.getHouseInformation().getDKYT(/*贷款用途*/));

        if (body.getHouseInformation().getDKYT(/*贷款用途*/).equals("0")) {

            //region //购买

            if (body.getHouseInformation().getSFWESF(/*是否为二手房*/).equals("1")) {

                //region //二手房

                loanHousePurchasingVice.setFwjzmj(StringUtil.safeBigDecimal(body.getHouseInformation().getPurchaseSecondInformation().getFWJZMJ(/*房屋建筑面积*/)));
                loanHousePurchasingVice.setGrskyhzh(body.getHouseInformation().getPurchaseSecondInformation().getGRSKYHZH(/*个人收款银行账号*/));
                loanHousePurchasingVice.setFwtnmj(StringUtil.safeBigDecimal(body.getHouseInformation().getPurchaseSecondInformation().getFWTNMJ(/*房屋套内面积*/)));
                loanHousePurchasingVice.setFwzj(StringUtil.safeBigDecimal(body.getHouseInformation().getPurchaseSecondInformation().getFWZJ(/*房屋总价*/)));
                loanHousePurchasingVice.setFwxz(body.getHouseInformation().getPurchaseSecondInformation().getFWXZ(/*房屋性质（0：商品房 1：障碍性住房 2：自建房 3：两限房 4：集资房 5：危改房 6：经济适用房 7：房改房 8：其他）*/));
                loanHousePurchasingVice.setKhyhmc(body.getHouseInformation().getPurchaseSecondInformation().getKHYHMC(/*开户银行名称*/));
                loanHousePurchasingVice.setFwjgrq(DateUtil.safeStr2Date(format, body.getHouseInformation().getPurchaseSecondInformation().getFWJGRQ(/*房屋竣工日期*/)));
                loanHousePurchasingVice.setFwjg(body.getHouseInformation().getPurchaseSecondInformation().getFWJG(/*房屋结构（0：框架 1：砖混 2：其他）*/));
                loanHousePurchasingVice.setHtje(StringUtil.safeBigDecimal(body.getHouseInformation().getPurchaseSecondInformation().getHTJE(/*合同金额*/)));
                loanHousePurchasingVice.setYhkhm(body.getHouseInformation().getPurchaseSecondInformation().getYHKHM(/*银行开户名*/));
                loanHousePurchasingVice.setGfhtbh(body.getHouseInformation().getPurchaseSecondInformation().getGFHTBH(/*购房合同编号*/));
                loanHousePurchasingVice.setBlzl(body.getHouseInformation().getPurchaseSecondInformation().getSCZL(/*资料*/));
                loanHousePurchasingVice.setYfk(StringUtil.safeBigDecimal(body.getHouseInformation().getPurchaseSecondInformation().getYFK(/*已付款（全部付完）*/)));
                loanHousePurchasingVice.setDanJia(StringUtil.safeBigDecimal(body.getHouseInformation().getPurchaseSecondInformation().getDanJia()));
                loanHousePurchasingVice.setLxfs(body.getHouseInformation().getPurchaseSecondInformation().getLXFS()); //联系方式
                loanHousePurchasingVice.setSfrmc(body.getHouseInformation().getPurchaseSecondInformation().getSFRMC());//售房人名称
                loanHousePurchasingVice.setFwzl(body.getHouseInformation().getPurchaseSecondInformation().getFWZL());//房屋坐落
                loanHousePurchasingVice.setFwxs(body.getHouseInformation().getPurchaseSecondInformation().getFWXS());


                loanHousePurchasingVice.setSfrkhyhmc(body.getHouseInformation().getPurchaseSecondInformation().getKHYHMC(/*开户银行名称*/));
                loanHousePurchasingVice.setSfrzhhm(body.getHouseInformation().getPurchaseSecondInformation().getGRSKYHZH(/*个人收款银行账号*/));
                loanHousePurchasingVice.setSfryhkhm(body.getHouseInformation().getPurchaseSecondInformation().getYHKHM(/*银行开户名*/));
                loanHousePurchasingVice.setSfk(StringUtil.safeBigDecimal(body.getHouseInformation().getPurchaseSecondInformation().getYFK(/*已付款（全部付完）*/)));
                //endregion
            }


            loanHousePurchasingVice.setSfwesf(StringUtil.safeBigDecimal(body.getHouseInformation().getSFWESF(/*是否为二手房*/)).intValue() == 1);

            if (body.getHouseInformation().getSFWESF(/*是否为二手房*/).equals("0")) {

                //region //非二手房
                loanHousePurchasingVice.setFwjzmj(StringUtil.safeBigDecimal(body.getHouseInformation().getPurchaseFirstInformation().getFWJZMJ(/*房屋建筑面积*/)));
                loanHousePurchasingVice.setFwtnmj(StringUtil.safeBigDecimal(body.getHouseInformation().getPurchaseFirstInformation().getFWTNMJ(/*房屋套内面积*/)));
                loanHousePurchasingVice.setFwxs(body.getHouseInformation().getPurchaseFirstInformation().getFWXS(/*房屋形式（0：在建房  1：现房）*/));
                loanHousePurchasingVice.setFwxz(body.getHouseInformation().getPurchaseFirstInformation().getFWXZ(/*房屋性质（0：商品房 1：障碍性住房 2：自建房 3：两限房 4：集资房 5：危改房 6：经济适用房 7：房改房 8：其他）*/));
                loanHousePurchasingVice.setFwzj(StringUtil.safeBigDecimal(body.getHouseInformation().getPurchaseFirstInformation().getFWZJ(/*房屋总价*/)));
                loanHousePurchasingVice.setFwjgrq(DateUtil.safeStr2Date(format, body.getHouseInformation().getPurchaseFirstInformation().getFWJGRQ(/*房屋竣工日期*/)));
                loanHousePurchasingVice.setSfrkhyhmc(body.getHouseInformation().getPurchaseFirstInformation().getSFRKHYHMC(/*售房人开户银行名称*/));
                loanHousePurchasingVice.setFwjg(body.getHouseInformation().getPurchaseFirstInformation().getFWJG(/*房屋结构（0：框架 1：砖混 2：其他）*/));
                loanHousePurchasingVice.setHtje(StringUtil.safeBigDecimal(body.getHouseInformation().getPurchaseFirstInformation().getHTJE(/*合同金额*/)));
                loanHousePurchasingVice.setSfrzhhm(body.getHouseInformation().getPurchaseFirstInformation().getSFRZHHM(/*售房人账户号码*/));
                loanHousePurchasingVice.setLpmc(body.getHouseInformation().getPurchaseFirstInformation().getLPMC(/*楼盘名称*/));
                loanHousePurchasingVice.setGfhtbh(body.getHouseInformation().getPurchaseFirstInformation().getGFHTBH(/*购房合同编号*/));
                loanHousePurchasingVice.setBlzl(body.getHouseInformation().getPurchaseFirstInformation().getSCZL(/*办理资料*/));
                loanHousePurchasingVice.setSfk(StringUtil.safeBigDecimal(body.getHouseInformation().getPurchaseFirstInformation().getSFK(/*首付款*/)));
                loanHousePurchasingVice.setLxfs(body.getHouseInformation().getPurchaseFirstInformation().getLXFS()); //联系方式
                loanHousePurchasingVice.setSfrmc(body.getHouseInformation().getPurchaseFirstInformation().getSFRMC());//售房人名称
                loanHousePurchasingVice.setFwzl(body.getHouseInformation().getPurchaseFirstInformation().getFWZL());//房屋坐落
                loanHousePurchasingVice.setDanJia(StringUtil.safeBigDecimal(body.getHouseInformation().getPurchaseFirstInformation().getDanJia()));//单价
                loanHousePurchasingVice.setSfryhkhm(body.getHouseInformation().getPurchaseFirstInformation().getSFRYHKHM()/*售房人银行开户名*/);
                loanHousePurchasingVice.setSpfysxkbh(body.getHouseInformation().getPurchaseFirstInformation().getSPFYSXKBH(/*商品房预售许可编号*/));
                //endregion
            }


            //endregion

            loanHousingBusinessProcess.setLoanHouseBuildVice(null);
            loanHousingBusinessProcess.setLoanHouseOverhaulVice(null);

            loanHouseBuildVice.setGrywmx(null);
            loanHouseOverhaulVice.setGrywmx(null);

            DAOBuilder.instance(this.loanHouseBuildViceDAO).entity(loanHouseBuildVice).delete(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });

            DAOBuilder.instance(this.loanHouseOverhaulViceDAO).entity(loanHouseOverhaulVice).delete(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });

        }

        if (body.getHouseInformation().getDKYT(/*贷款用途*/).equals("1")) {

            //region//自建翻修

            loanHouseBuildVice.setGrskyhzh(body.getHouseInformation().getBuildInformation().getGRSKYHZH(/*个人收款银行账号*/));
            loanHouseBuildVice.setJhjgrq(DateUtil.safeStr2Date(format, body.getHouseInformation().getBuildInformation().getJHJGRQ(/*计划竣工日期*/)));
            loanHouseBuildVice.setFwxz(body.getHouseInformation().getBuildInformation().getFWXZ(/*房屋性质（0：商品房 1：障碍性住房 2：自建房 3：两限房 4：集资房 5：危改房 6：经济适用房 7：房改房 8：其他）*/));
            loanHouseBuildVice.setFwjg(body.getHouseInformation().getBuildInformation().getFWJG(/*房屋结构（0：框架 1：砖混 2：其他）*/));
            loanHouseBuildVice.setJhjgrq(DateUtil.safeStr2Date(format, body.getHouseInformation().getBuildInformation().getJHJGRQ(/*计划竣工日期*/)));
            loanHouseBuildVice.setFwzl(body.getHouseInformation().getBuildInformation().getFWZL(/*房屋坐落地址*/));
            loanHouseBuildVice.setTdsyzh(body.getHouseInformation().getBuildInformation().getTDSYZH(/*土地使用证号*/));
            loanHouseBuildVice.setGrsyzj(StringUtil.safeBigDecimal(body.getHouseInformation().getBuildInformation().getGRSYZJ(/*个人使用资金*/)));
            loanHouseBuildVice.setJzcs(body.getHouseInformation().getBuildInformation().getJZCS(/*建造层数*/));
            loanHouseBuildVice.setYhkhm(body.getHouseInformation().getBuildInformation().getYHKHM(/*银行开户名*/));
            loanHouseBuildVice.setJzydghxkzh(body.getHouseInformation().getBuildInformation().getJZYDGHXKZH(/*建造用地规划许可证号*/));
            loanHouseBuildVice.setPzjgwh(body.getHouseInformation().getBuildInformation().getPZJGWH(/*批准机关文号*/));
            loanHouseBuildVice.setJhjzfy(StringUtil.safeBigDecimal(body.getHouseInformation().getBuildInformation().getJHJZFY(/*计划建造费用*/)));
            loanHouseBuildVice.setKhhyhmc(body.getHouseInformation().getBuildInformation().getKHYHMC(/*开户行银行名称*/));
            loanHouseBuildVice.setBlzl(body.getHouseInformation().getBuildInformation().getSCZL(/* 办理资料*/));
            loanHouseBuildVice.setJzgcghxkzh(body.getHouseInformation().getBuildInformation().getJZGCGHXKZH(/*建造工程规划许可证号*/));
            loanHouseBuildVice.setJchjzmj(StringUtil.safeBigDecimal(body.getHouseInformation().getBuildInformation().getJCHJZMJ(/*建成后建造面积*/)));
            loanHouseBuildVice.setJhkgrq(DateUtil.safeStr2Date(format, body.getHouseInformation().getBuildInformation().getJHKGRQ(/*计划开工日期*/)));
            loanHouseBuildVice.setJsgcsgxkzh(body.getHouseInformation().getBuildInformation().getJSGCSGXKZH()/*建设工程施工许可证号*/);
            //endregion

            loanHousingBusinessProcess.setLoanHousePurchasingVice(null);
            loanHousingBusinessProcess.setLoanHouseOverhaulVice(null);

            loanHousePurchasingVice.setGrywmx(null);
            loanHouseOverhaulVice.setGrywmx(null);

            DAOBuilder.instance(this.loanHouseOverhaulViceDAO).entity(loanHouseOverhaulVice).delete(new DAOBuilder.ErrorHandler() {

                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });

            DAOBuilder.instance(this.loanHousePurchasingViceDAO).entity(loanHousePurchasingVice).delete(new DAOBuilder.ErrorHandler() {

                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
        }

        if (body.getHouseInformation().getDKYT(/*贷款用途*/).equals("2")) {

            // region//大修

            loanHouseOverhaulVice.setGrskyhzh(body.getHouseInformation().getOverhaulInformation().getGRSKYHZH(/*个人收款银行账号*/));
            loanHouseOverhaulVice.setBlzl(body.getHouseInformation().getOverhaulInformation().getSCZL(/*资料*/));
            loanHouseOverhaulVice.setFwzl(body.getHouseInformation().getOverhaulInformation().getFWZL(/*房屋坐落地址*/));
            loanHouseOverhaulVice.setJhkgrq(DateUtil.safeStr2Date(format, body.getHouseInformation().getOverhaulInformation().getJHKGRQ(/*计划开工日期*/)));
            loanHouseOverhaulVice.setYhkhm(body.getHouseInformation().getOverhaulInformation().getYHKHM(/*银行开户名*/));
            loanHouseOverhaulVice.setYbdczh(body.getHouseInformation().getOverhaulInformation().getYBDCZH());//原不动产证号
            loanHouseOverhaulVice.setDxgcys(StringUtil.safeBigDecimal(body.getHouseInformation().getOverhaulInformation().getDXGCYS()));
            loanHouseOverhaulVice.setYjzmj(StringUtil.safeBigDecimal(body.getHouseInformation().getOverhaulInformation().getYJZMJ()));
            loanHouseOverhaulVice.setTdsyzh(body.getHouseInformation().getOverhaulInformation().getFXHCS());
            loanHouseOverhaulVice.setJhjgrq(DateUtil.safeStr2Date(format, body.getHouseInformation().getOverhaulInformation().getJHJGRQ()));
            loanHouseOverhaulVice.setFwzjbgjgmcjbh(body.getHouseInformation().getOverhaulInformation().getFWZJBGJGMCJBH());
            loanHouseOverhaulVice.setKhyhmc(body.getHouseInformation().getOverhaulInformation().getKHYHMC());
            //endregion

            loanHousingBusinessProcess.setLoanHousePurchasingVice(null);
            loanHousingBusinessProcess.setLoanHouseBuildVice(null);

            loanHousePurchasingVice.setGrywmx(null);
            loanHouseBuildVice.setGrywmx(null);

            DAOBuilder.instance(this.loanHouseBuildViceDAO).entity(loanHouseBuildVice).delete(new DAOBuilder.ErrorHandler() {

                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });

            DAOBuilder.instance(this.loanHousePurchasingViceDAO).entity(loanHousePurchasingVice).delete(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
        }

        //endregion

        CAccountNetwork network = DAOBuilder.instance(cAccountNetworkDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("id",tokenContext.getUserInfo().getYWWD());

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e); }

        });
        loanHousingBusinessProcess.setYwwd(network);

        loanHousingBusinessProcess.setBlzl(body.getSCZL(/* 办理资料*/));

        loanHousingBusinessProcess.setCzy(tokenContext.getUserInfo().getCZY()/*操作员*/);
        //region//担保信息

        BigDecimal dywjz = BigDecimal.ZERO;

        loanHousingGuaranteeContractVice.setBlzl(body.getCollateralInformation().getSCZL(/* 办理资料*/));

        loanHousingBusinessProcess.setDkdblx(body.getCollateralInformation().getDKDBLX(/*贷款担保类型*/));

        if (body.getCollateralInformation().getDKDBLX(/*贷款担保类型*/).equals(LoanGuaranteeType.抵押.getCode())) {

            //region //抵押

            list_loanGuaranteeMortgageVice.forEach(new Consumer<CLoanGuaranteeMortgageVice>() {
                @Override
                public void accept(CLoanGuaranteeMortgageVice cLoanGuaranteeMortgageVice) { cLoanGuaranteeMortgageVice.setcLoanHousingGuaranteeContractVice(null); }
            });

            DAOBuilder.instance(this.loanGuaranteeMortgageViceDAO).entities(list_loanGuaranteeMortgageVice).delete(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) { throw new ErrorException(e); }
            });

            list_loanGuaranteeMortgageVice.clear();

            for (ApplicantPostCollateralInformationMortgageInformation applicantPostCollateralInformationMortgageInformation : body.getCollateralInformation().getMortgageInformation()) {

                CLoanGuaranteeMortgageVice loanGuaranteeMortgageVice = new CLoanGuaranteeMortgageVice();

                dywjz = dywjz.add(StringUtil.safeBigDecimal(applicantPostCollateralInformationMortgageInformation.getDYWPGJZ(/*抵押物评估价值*/)));

                loanGuaranteeMortgageVice.setDywsyqrsfzhm(applicantPostCollateralInformationMortgageInformation.getDYWSYQRSFZHM(/*抵押物所有权人身份证号码*/));
                loanGuaranteeMortgageVice.setDyfwxs(applicantPostCollateralInformationMortgageInformation.getDYFWXS(/*抵押房屋形式（0：住宅 （期房） 1：住宅（现房） 2：商铺 3：其他）*/));
                loanGuaranteeMortgageVice.setDywfwzl(applicantPostCollateralInformationMortgageInformation.getDYWFWZL(/*抵押物房屋坐落*/));
                loanGuaranteeMortgageVice.setDywmc(applicantPostCollateralInformationMortgageInformation.getDYWMC(/*抵押物名称*/));
                loanGuaranteeMortgageVice.setFwjg(applicantPostCollateralInformationMortgageInformation.getFWJG(/*房屋结构（0：框架 1：砖混 2：土木 3：其他）*/));
                loanGuaranteeMortgageVice.setDywgyqrlxdh(applicantPostCollateralInformationMortgageInformation.getDYWGYQRLXDH(/*抵押物共有权人联系电话*/));
                loanGuaranteeMortgageVice.setDywpgjz(StringUtil.safeBigDecimal(applicantPostCollateralInformationMortgageInformation.getDYWPGJZ(/*抵押物评估价值*/)));
                loanGuaranteeMortgageVice.setDywgyqrsfzhm(applicantPostCollateralInformationMortgageInformation.getDYWGYQRSFZHM(/*抵押物共有权人身份证号码*/));
                loanGuaranteeMortgageVice.setDywsyqrxm(applicantPostCollateralInformationMortgageInformation.getDYWSYQRXM(/*抵押物所有权人姓名*/));
                loanGuaranteeMortgageVice.setDywsyqrlxdh(applicantPostCollateralInformationMortgageInformation.getDYWSYQRLXDH(/*抵押物所有权人联系电话*/));
                loanGuaranteeMortgageVice.setFwmj(StringUtil.safeBigDecimal(applicantPostCollateralInformationMortgageInformation.getFWMJ(/*房屋面积*/)));
                loanGuaranteeMortgageVice.setQszsbh(applicantPostCollateralInformationMortgageInformation.getQSZSBH(/*权属证书编号*/));
                loanGuaranteeMortgageVice.setDywgyqrxm(applicantPostCollateralInformationMortgageInformation.getDYWGYQRXM(/*抵押物共有权人姓名*/));

                list_loanGuaranteeMortgageVice.add(loanGuaranteeMortgageVice);
            }
            loanHousingGuaranteeContractVice.setBlzl(body.getCollateralInformation().getSCZL(/* 办理资料*/));

            //endregion

            loanHousingGuaranteeContractVice.setGuaranteeVices(null);
            loanHousingGuaranteeContractVice.setGuaranteePledgeVices(null);


        }

        if (body.getCollateralInformation().getDKDBLX(/*贷款担保类型*/).equals(LoanGuaranteeType.质押.getCode())) {

            //region //质押

            list_loanGuaranteePledgeVice.forEach(new Consumer<CLoanGuaranteePledgeVice>() {
                @Override
                public void accept(CLoanGuaranteePledgeVice cLoanGuaranteePledgeVice) { cLoanGuaranteePledgeVice.setcLoanHousingGuaranteeContractVice(null); }
            });

            DAOBuilder.instance(this.loanGuaranteePledgeViceDAO).entities(list_loanGuaranteePledgeVice).delete(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) { throw new ErrorException(e); }
            });

            list_loanGuaranteePledgeVice.clear();

            for (ApplicantPostCollateralInformationPledgeInformation applicantPostCollateralInformationPledgeInformation : body.getCollateralInformation().getPledgeInformation()) {

                CLoanGuaranteePledgeVice loanGuaranteePledgeVice = new CLoanGuaranteePledgeVice();
                dywjz = dywjz.add(StringUtil.safeBigDecimal(applicantPostCollateralInformationPledgeInformation.getZYWJZ(/*质押物价值*/)));
                loanGuaranteePledgeVice.setZywsyqrsfzhm(applicantPostCollateralInformationPledgeInformation.getZYWSYQRSFZHM(/*质押物所有权人身份证号码*/));
                loanGuaranteePledgeVice.setZywsyqrxm(applicantPostCollateralInformationPledgeInformation.getZYWSYQRXM(/*质押物所有权人姓名*/));
                loanGuaranteePledgeVice.setZywjz(StringUtil.safeBigDecimal(applicantPostCollateralInformationPledgeInformation.getZYWJZ(/*质押物价值*/)));
                loanGuaranteePledgeVice.setZywsyqrlxdh(applicantPostCollateralInformationPledgeInformation.getZYWSYQRLXDH(/*质押物所有权人联系电话*/));
                loanGuaranteePledgeVice.setZywmc(applicantPostCollateralInformationPledgeInformation.getZYWMC(/*质押物名称*/));

                list_loanGuaranteePledgeVice.add(loanGuaranteePledgeVice);
            }

            loanHousingGuaranteeContractVice.setBlzl(body.getCollateralInformation().getSCZL(/* 办理资料*/));

            //endregion

            loanHousingGuaranteeContractVice.setGuaranteeMortgageVices(null);
            loanHousingGuaranteeContractVice.setGuaranteeVices(null);

        }

        if (body.getCollateralInformation().getDKDBLX(/*贷款担保类型*/).equals(LoanGuaranteeType.保证.getCode())) {

            //region//保证

            list_loanGuaranteeVice.forEach(new Consumer<CLoanGuaranteeVice>() {
                @Override
                public void accept(CLoanGuaranteeVice cLoanGuaranteeVice) { cLoanGuaranteeVice.setcLoanHousingGuaranteeContractVice(null); }
            });

            DAOBuilder.instance(this.loanGuaranteeViceDAO).entities(list_loanGuaranteeVice).delete(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) { throw new ErrorException(e); }
            });

            list_loanGuaranteeVice.clear();
            for (ApplicantPostCollateralInformationGuaranteeInformation applicantPostCollateralInformationGuaranteeInformation : body.getCollateralInformation().getGuaranteeInformation()) {

                CLoanGuaranteeVice loanGuaranteeVice = new CLoanGuaranteeVice();

                loanGuaranteeVice.setYzbm(applicantPostCollateralInformationGuaranteeInformation.getYZBM(/*邮政编码*/));
                loanGuaranteeVice.setFrdbxjzdz(applicantPostCollateralInformationGuaranteeInformation.getFRDBXJZDZ(/*保证人现居住地址*/));
                loanGuaranteeVice.setFrdbsfzhm(applicantPostCollateralInformationGuaranteeInformation.getFRDBSFZHM(/*保证人身份证号码*/));
                loanGuaranteeVice.setFrdbxm(applicantPostCollateralInformationGuaranteeInformation.getFRDBXM(/*保证人姓名*/));
                loanGuaranteeVice.setFrdblxdh(applicantPostCollateralInformationGuaranteeInformation.getFRDBLXDH(/*保证人联系电话*/));
                loanGuaranteeVice.setTxdz(applicantPostCollateralInformationGuaranteeInformation.getTXDZ(/*通讯地址*/));
                loanGuaranteeVice.setBzflx(applicantPostCollateralInformationGuaranteeInformation.getBZFLX(/*保证方类型（0：个人 1：机构）*/));

                list_loanGuaranteeVice.add(loanGuaranteeVice);
            }
            dywjz = StringUtil.safeBigDecimal(body.getCapitalInformation().getHTDKJE(/*合同贷款金额*/));
            loanHousingGuaranteeContractVice.setBlzl(body.getCollateralInformation().getSCZL(/* 办理资料*/));

            //endregion

            loanHousingGuaranteeContractVice.setGuaranteeMortgageVices(null);
            loanHousingGuaranteeContractVice.setGuaranteePledgeVices(null);

        }

        //endregion

        //region//资金信息
        loanFundsVice.setHkfs(body.getCapitalInformation().getHKFS(/*还款方式（0：等额本息 1：等额本金 2：一次还款付息 3：自由还款方式 4：其他）*/));
        loanFundsVice.setHtdkje(StringUtil.safeBigDecimal(body.getCapitalInformation().getHTDKJE(/*合同贷款金额*/)));
        loanFundsVice.setLlfsbl(StringUtil.safeBigDecimal(body.getCapitalInformation().getLLFSBL(/*利率浮动比例*/)));
        loanFundsVice.setHtdkjedx(body.getCapitalInformation().getHTDKJEDX(/*合同贷款金额大写*/));
        loanFundsVice.setDklx(body.getCapitalInformation().getDKLX(/*贷款类型（0：公积金贷款 1：组合贷款 2：贴息贷款 3：其他）*/));
        loanFundsVice.setJkhtll(StringUtil.safeBigDecimal(body.getCapitalInformation().getJKHTLL(/*借款合同利率*/)));
        loanFundsVice.setDkqs(StringUtil.safeBigDecimal(body.getCapitalInformation().getDKQS(/*贷款期数*/)));
        loanFundsVice.setDkdblx(body.getCapitalInformation().getDKDBLX(/*贷款担保类型  （0：抵押 1：质押 2：保证 3：其他）*/));
        loanFundsVice.setWtkhyjce(StringUtil.safeBigDecimal(body.getCapitalInformation().getWTKHYJCE(/*委托划扣月缴存额*/)).intValue() == 1);
        //loanFundsVice.setZxll(StringUtil.safeBigDecimal(body.getCapitalInformation().getZXLL(/*执行利率*/)));
        loanFundsVice.setFwts(body.getCapitalInformation().getFWTS(/*房屋套数（0：一套 1：二套 2：三套 3：四套  5：五套及以上）*/));

        loanHousingBusinessProcess.setCzy(tokenContext.getUserInfo().getCZY()/*操作员*/);

        //endregion

        //endregion

        //region //贷款账号状态验证

        if (("1".equals(status) && !DAOBuilder.instance(loanHousingPersonInformationBasicDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("jkrzjhm", body.getApplicantInformation().getInformation().getBorrowerInformation().getJKRZJHM());

        }}).extension(new IBaseDAO.CriteriaExtension() {

            @Override
            public void extend(Criteria criteria) {

                criteria.add(Restrictions.ne("dkzhzt", LoanAccountType.已结清.getCode()));
            }
        }).isUnique(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }

        }))) {

            throw new ErrorException(ReturnEnumeration.Business_In_Process, "存在未结清的贷款 不能提交");
        }

        //endregion

        //region  //其他

        CLoanHousingBusinessProcess savedProcess = DAOBuilder.instance(this.loanHousingBusinessProcessDAO).entity(loanHousingBusinessProcess).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (status.equals("1")) {

            if(!hasCoborrower){  loanHousingBusinessProcess.setLoanHousingCoborrowerVice(null); }

            this.checkApplicationAvailable(loanHousingBusinessProcess);

            if(dywjz.compareTo(loanFundsVice.getHtdkje())<0){ throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"抵押物价值过小");}

            //this.iSaveAuditHistory.saveNormalBusiness(savedProcess.getYwlsh(),tokenContext,LoanBusinessType.贷款发放.getName(),"修改");
            String id = this.iPdfService.getReviewTable(ResUtils.noneAdductionValue(GetApplicantResponse.class, this.getApplyDetails(tokenContext, savedProcess.getYwlsh())));

            id = iPdfService.putReviewTable(id, new ArrayList<>(Collections.singleton(ResUtils.noneAdductionValue(Review.class, new Review() {{

                this.setAction("新建"/*操作*/);

                this.setCZY(tokenContext.getUserInfo().getCZY()/*操作员及职务*/);

                this.setYWWD(loanHousingBusinessProcess.getYwwd().getMingCheng()/*业务网点*/);

                this.setCZQD(tokenContext.getChannel()/*操作渠道*/);

                this.setSLRYJ("同意受理"/*受理人意见*/);

                this.setSLR(loanHousingBusinessProcess.getCzy()/*受理人*/);

                this.setSPSJ(DateUtil.date2Str(loanHousingBusinessProcess.getCreated_at(), "yyyy-MM-dd HH:mm") /*审批时间*/);

                this.setType(0/*审批级别（0：新建，1：一级审批，2：二级审批，3：三级审批）*/);

            }}))));

            if (id != null && savedProcess.getLoanHousingPersonInformationVice() != null) {

                savedProcess.getLoanHousingPersonInformationVice().setSpbwj(id);
            }

            DAOBuilder.instance(this.loanHousingBusinessProcessDAO).entity(savedProcess).save(new DAOBuilder.ErrorHandler() {

                @Override
                public void error(Exception e) { throw new ErrorException(e); }
            });

        }

        //endregion

        //region  //修改状态

        StateMachineUtils.updateState(this.iStateMachineService, new HashMap<String, String>() {{

            this.put("0", Events.保存.getEvent());
            this.put("1", Events.通过.getEvent());

        }}.get(status), new TaskEntity() {{

            this.setStatus(savedProcess.getStep() == null ? "初始状态" : savedProcess.getStep());
            this.setTaskId(savedProcess.getYwlsh());
            this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
            this.setNote("");
            this.setSubtype(BusinessSubType.贷款_个人贷款申请.getSubType());
            this.setType(com.handge.housingfund.database.enums.BusinessType.Loan);
            this.setOperator(savedProcess.getCzy());
            this.setWorkstation(savedProcess.getYwwd().getId());

        }}, new StateMachineUtils.StateChangeHandler() {

            @Override
            public void onStateChange(boolean succeed, String next, Exception e) {

                if (e != null) {
                    throw new ErrorException(e);
                }

                if (!succeed || next == null || e != null) {
                    return;
                }

                savedProcess.setStep(next);


                if (StringUtil.isIntoReview(next, null)) {

                    savedProcess.setDdsj(new Date());

                }

                if (LoanBussinessStatus.待签合同.getName().equals(next)) {

                    loanReviewService.postLoanReviewReason(tokenContext, savedProcess.getYwlsh());

                    savedProcess.setStep(LoanBussinessStatus.待签合同.getName());
                }

                DAOBuilder.instance(loanHousingBusinessProcessDAO).entity(savedProcess).save(new DAOBuilder.ErrorHandler() {

                    @Override

                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });
            }
        });
        //endregion

        //region //在途验证

        if (("1".equals(status) && !DAOBuilder.instance(loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("loanHousingPersonInformationVice.jkrzjhm", body.getApplicantInformation().getInformation().getBorrowerInformation().getJKRZJHM());

        }}).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {

                criteria.add(Restrictions.not(Restrictions.in(CriteriaUtils.addAlias(criteria, "step"), (Collection) CollectionUtils.flatmap(CollectionUtils.merge(CollectionBusinessStatus.办结.getSubTypes(), CollectionBusinessStatus.新建.getSubTypes(),CollectionBusinessStatus.已作废.getSubTypes()), new CollectionUtils.Transformer<CollectionBusinessStatus, String>() {

                    @Override
                    public String tansform(CollectionBusinessStatus var1) {
                        return var1.getName();
                    }

                }))));
                criteria.add(Restrictions.ne("ywlsh", YWLSH));
                criteria.add(Restrictions.eq("cznr", LoanBusinessType.贷款发放.getCode()));
            }
        }).isUnique(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }

        }))) {

            throw new ErrorException(ReturnEnumeration.Business_In_Process);
        }

        //endregion

        return new CommonResponses() {{

            this.setId(YWLSH);

            this.setState("success");
        }};
    }


    @Override
    public GetApplicantResponseApplicantInformation getPersonalAccountInfo(TokenContext tokenContext, String GRZH) {

        StCommonPerson commonPerson = DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("zjhm", GRZH);

        }}).extension(new IBaseDAO.CriteriaExtension() {

            @Override
            public void extend(Criteria criteria) {

                criteria.createAlias("collectionPersonalAccount","collectionPersonalAccount");

                criteria.add(Restrictions.isNull("collectionPersonalAccount.xhrq"));
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) { throw new ErrorException(e); }
        });

        if (commonPerson == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "个人信息");
        }

        if (commonPerson.getExtension() == null || commonPerson.getCollectionPersonalAccount() == null || commonPerson.getUnit() == null || commonPerson.getUnit().getExtension() == null) {

            throw new ErrorException(ReturnEnumeration.Data_MISS, "个人信息不全");
        }

        Integer consecutiveDepositMonths = new BigDecimal(this.unitRemittance.getConsecutiveDepositMonths(commonPerson.getGrzh()).getId()).intValue();

        CCommonPersonExtension commonPersonExtension = commonPerson.getExtension();

        StCollectionPersonalAccount collectionPersonalAccount = commonPerson.getCollectionPersonalAccount();

        StCommonUnit commonUnit = commonPerson.getUnit();

        CCommonUnitExtension cCommonUnitExtension = commonUnit.getExtension();

        return new GetApplicantResponseApplicantInformation() {{
            this.setBorrowerInformation(new GetApplicantResponseApplicantInformationBorrowerInformation() {{
                this.setHYZK(commonPerson.getHyzk() + ""/*婚姻状态（0：已婚 1：未婚 2：丧偶 3：离婚 4：未说明的婚姻状况）*/);

                this.setNianLing(DateUtil.getAge(DateUtil.safeStr2Date(DateUtil.dbformatYear_Month, commonPerson.getCsny())) + ""/*年龄*/);
                this.setJTZZ(commonPerson.getJtzz()/*家庭住址*/);
                this.setCSNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, commonPerson.getCsny(), formatNY)/*出生年月*/);
                this.setJKRZJHM(commonPerson.getZjhm()/*借款人证件号码*/);
                //this.setYGXZ(commonPerson.getYgxz() + ""/*用工性质（0：正式职工 1：合同制 2：聘用制）*/);
                this.setYSR(commonPerson.getJtysr() + ""/*月收入*/);//?
                this.setJKRXM(commonPerson.getXingMing()/*借款人姓名*/);
                //this.setHKSZD(loanHousingPersonInformationVice.getHkszd()/*户口所在地*/);
                this.setZhiCheng(commonPerson.getZhiChen()/*职称*/);
                this.setJKRZJLX(commonPerson.getZjlx() + ""/*借款人证件类型*/);
                this.setSJHM(commonPerson.getSjhm()/*手机号码*/);
                //this.setJKZK(commonPerson.getj+ ""/*健康状态（0：良好 1：一般 2：差）*/);
                this.setGDDHHM(commonPerson.getGddhhm()/*固定电话号码*/);
                //this.setZYJJLY(commonPersonExtension.getZyjjly() + ""/*主要经济来源（0：工资收入 1：个体经营收入 2：其他非工资收入）*/);
                this.setXingBie(commonPerson.getXingBie() + ""/*性别*/);
                this.setJTYSR(commonPerson.getJtysr() + ""/*家庭月收入*/);
                this.setXueLi(commonPerson.getXueLi()/*学历*/);
                this.setZhiWu(commonPerson.getZhiWu()/*职务*/);
            }});
            this.setJKRGJJZH(commonPerson.getGrzh()/*借款人公积金账号*/);
            this.setUnitInformation(new GetApplicantResponseApplicantInformationUnitInformation() {{
                this.setDWDH(cCommonUnitExtension.getDwlxdh()/*单位电话*/);
                this.setDWMC(commonUnit.getDwmc()/*单位名称*/);
                this.setDWZH(commonUnit.getDwzh()/*单位账号*/);
                this.setDWXZ(commonUnit.getExtension().getDwlb() + ""/*单位性质*/);
                this.setDWDZ(commonUnit.getDwdz()/*单位地址*/);
            }});
            //this.setJCD(loanHousingPersonInformationVice.getJcd()/*缴存地*/);
            //this.setSCZL(loanHousingPersonInformationVice.getBlzl()/* 办理资料*/);
            this.setAccountInformation(new GetApplicantResponseApplicantInformationAccountInformation() {{
                this.setGRZHZT(collectionPersonalAccount.getGrzhzt() + ""/*个人账户状态*/);
                this.setJZNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, commonPersonExtension.getGrjzny(), formatNY)/*缴至年月*/);
                this.setYJCE(collectionPersonalAccount.getGryjce().add(collectionPersonalAccount.getDwyjce()) + ""/*月缴存额*/);
                this.setGRZHYE(collectionPersonalAccount.getGrzhye() + ""/*个人账户余额*/);
                this.setGRJCJS(collectionPersonalAccount.getGrjcjs() + ""/*个人缴存基数*/);
                this.setLXZCJCYS(consecutiveDepositMonths == null ? "0" : (consecutiveDepositMonths + "")/*连续正常缴存月数*/);
            }});
        }};

    }

    public ForeignLoanProof getForeignLoanProof(TokenContext tokenContext,String GRZH){
        StCommonPerson commonPerson = DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("grzh", GRZH);

        }}).extension(new IBaseDAO.CriteriaExtension() {

            @Override
            public void extend(Criteria criteria) {

                criteria.createAlias("collectionPersonalAccount","collectionPersonalAccount");

                criteria.add(Restrictions.isNull("collectionPersonalAccount.xhrq"));
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) { throw new ErrorException(e); }
        });

        if (commonPerson == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "销户状态不能打印回执单");
        }

        if (commonPerson.getExtension() == null || commonPerson.getCollectionPersonalAccount() == null || commonPerson.getUnit() == null || commonPerson.getUnit().getExtension() == null) {

            throw new ErrorException(ReturnEnumeration.Data_MISS, "个人信息不全");
        }

        Integer consecutiveDepositMonths = new BigDecimal(this.unitRemittance.getConsecutiveDepositMonths(commonPerson.getGrzh()).getId()).intValue();

        CCommonPersonExtension commonPersonExtension = commonPerson.getExtension();

        StCollectionPersonalAccount collectionPersonalAccount = commonPerson.getCollectionPersonalAccount();

        StCommonUnit commonUnit = commonPerson.getUnit();

        CCommonUnitExtension cCommonUnitExtension = commonUnit.getExtension();

        List<CLoanHousingPersonInformationBasic> basic = DAOBuilder.instance(this.loanHousingPersonInformationBasicDAO).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                criteria.createAlias("coborrower","coborrower", JoinType.LEFT_OUTER_JOIN);

                criteria.add(Restrictions.or(
                        Restrictions.and(
                                Restrictions.isNull("coborrower"),
                                Restrictions.eq("jkrgjjzh",commonPerson.getGrzh())
                        ),

                        Restrictions.and(
                                Restrictions.isNotNull("coborrower"),
                                Restrictions.or(
                                        Restrictions.eq("jkrgjjzh",commonPerson.getGrzh()),
                                        Restrictions.eq("coborrower.gtjkrgjjzh",commonPerson.getGrzh())
                                ))
                ));
                criteria.add(Restrictions.isNotNull("loanContract"));
                criteria.add(Restrictions.isNotNull("personalAccount"));
                criteria.add(Restrictions.ne("dkzhzt",LoanAccountType.已作废.getCode()));
            }
        }).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) { throw new ErrorException(e);}
        });

        CAccountNetwork network = DAOBuilder.instance(this.cAccountNetworkDAO).searchFilter(new HashMap<String, Object>(){{
            this.put("id",tokenContext.getUserInfo().getYWWD());
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) { throw new ErrorException(e);}
        });
        ForeignLoanProof foreignLoanProof = new ForeignLoanProof();

        foreignLoanProof.setZGXM(commonPerson.getXingMing());
        foreignLoanProof.setSFZH(commonPerson.getZjhm());
        foreignLoanProof.setGJJZH(commonPerson.getGrzh());
        foreignLoanProof.setKHSJ(DateUtil.date2Str(commonPerson.getCreated_at(),formatNY));
        foreignLoanProof.setZHZT(collectionPersonalAccount.getGrzhzt());
        foreignLoanProof.setJCJS(collectionPersonalAccount.getGrjcjs().setScale(2,RoundingMode.HALF_UP)+"");
        foreignLoanProof.setDWJCBL(commonUnit.getCollectionUnitAccount().getDwjcbl().multiply(new BigDecimal("100")).setScale(2,RoundingMode.HALF_UP)+"");
        foreignLoanProof.setGRJCBL(commonUnit.getCollectionUnitAccount().getGrjcbl().multiply(new BigDecimal("100")).setScale(2,RoundingMode.HALF_UP)+"");
        foreignLoanProof.setYJCE(collectionPersonalAccount.getGryjce().add(collectionPersonalAccount.getDwyjce()).setScale(2,RoundingMode.HALF_UP)+"");
        foreignLoanProof.setJCYE(collectionPersonalAccount.getGrzhye().setScale(2,RoundingMode.HALF_UP)+"");
        List<String> MonthList = collectionPersonalBusinessDetailsDAO.getDepositContinuousMonth(GRZH);
        if(MonthList.size()>0) {
            try {
                List<String> ms = DepositContinuousMonthUtil.getMonths(MonthList);
                if (ms != null && ms.size()==2) {
                    foreignLoanProof.setLXJCKS(ms.get(0));
                    foreignLoanProof.setLXJCJS(ms.get(1));
                }
            } catch (Exception e) {
                throw new ErrorException(e);
            }
        }
        foreignLoanProof.setSFDK(basic.size()==0?"0":"1");
        foreignLoanProof.setDKCS(basic.size()+"");
        foreignLoanProof.setLJJE(CollectionUtils.reduce(basic, BigDecimal.ZERO, new CollectionUtils.Reducer<CLoanHousingPersonInformationBasic, BigDecimal>() {

            @Override
            public BigDecimal reducer(BigDecimal sum, CLoanHousingPersonInformationBasic obj) {
                return sum.add(obj.getLoanContract().getHtdkje());
            }
        }).setScale(2,RoundingMode.HALF_UP)+"");
        foreignLoanProof.setWQCDKYE(CollectionUtils.reduce(basic, BigDecimal.ZERO, new CollectionUtils.Reducer<CLoanHousingPersonInformationBasic, BigDecimal>() {

            @Override
            public BigDecimal reducer(BigDecimal sum, CLoanHousingPersonInformationBasic obj) {
                return sum.add(obj.getPersonalAccount().getDkye());
            }
        }).setScale(2,RoundingMode.HALF_UP)+"");
        foreignLoanProof.setDWJBR(tokenContext.getUserInfo().getCZY());
        foreignLoanProof.setLXDH(network.getLXDH());
        return foreignLoanProof;
    }

    /*
    *  completed
    *
    * !逻辑已完成
    *
    * 已测试 lian
    *
    * */
    @Override
    public GetApplicantResponse getApplyDetails(TokenContext tokenContext, String YWLSH) {


        //region //查询必要字段

        CLoanHousingBusinessProcess loanHousingBusinessProcess = DAOBuilder.instance(this.loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("ywlsh", YWLSH);

            this.put("cznr", LoanBusinessType.贷款发放.getCode());

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        //endregion

        //region //数据完整性验证

        if (loanHousingBusinessProcess == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }

        if (loanHousingBusinessProcess.getLoanHousingPersonInformationVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }

        if (loanHousingBusinessProcess.getLoanHousePurchasingVice() == null && loanHousingBusinessProcess.getLoanHouseBuildVice() == null && loanHousingBusinessProcess.getLoanHouseOverhaulVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }

        if (loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }

        if (loanHousingBusinessProcess.getLoanFundsVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }

        //endregion

        //region //必要数据声明

        CLoanHousingPersonInformationVice loanHousingPersonInformationVice = loanHousingBusinessProcess.getLoanHousingPersonInformationVice();

        CLoanHousePurchasingVice loanHousePurchasingVice = loanHousingBusinessProcess.getLoanHousePurchasingVice();

        CLoanHouseBuildVice loanHouseBuildVice = loanHousingBusinessProcess.getLoanHouseBuildVice();

        CLoanFundsVice loanFundsVice = loanHousingBusinessProcess.getLoanFundsVice();

        CLoanHousingCoborrowerVice loanHousingCoborrowerVice = loanHousingBusinessProcess.getLoanHousingCoborrowerVice();

        CLoanHouseOverhaulVice loanHouseOverhaulVice = loanHousingBusinessProcess.getLoanHouseOverhaulVice();

        CLoanHousingGuaranteeContractVice loanHousingGuaranteeContractVice = loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice();

        List<CLoanGuaranteeMortgageVice> list_loanGuaranteeMortgageVice = loanHousingGuaranteeContractVice.getGuaranteeMortgageVices() == null ? new ArrayList<CLoanGuaranteeMortgageVice>() : loanHousingGuaranteeContractVice.getGuaranteeMortgageVices();

        List<CLoanGuaranteeVice> list_loanGuaranteeVice = loanHousingGuaranteeContractVice.getGuaranteeVices() == null ? new ArrayList<CLoanGuaranteeVice>() : loanHousingGuaranteeContractVice.getGuaranteeVices();

        List<CLoanGuaranteePledgeVice> list_loanGuaranteePledgeVice = loanHousingGuaranteeContractVice.getGuaranteePledgeVices() == null ? new ArrayList<CLoanGuaranteePledgeVice>() : loanHousingGuaranteeContractVice.getGuaranteePledgeVices();

        //endregion

        return new GetApplicantResponse() {{

            this.setCommonBorrowerInformation(loanHousingCoborrowerVice == null ? new GetApplicantResponseCommonBorrowerInformation() : new GetApplicantResponseCommonBorrowerInformation() {{

                this.setHKSZD(loanHousingCoborrowerVice.getHkszd()/*户口所在地*/);
                this.setSCZL(loanHousingCoborrowerVice.getBlzl()/*提交资料*/);
                this.setGTJKRGJJZH(loanHousingCoborrowerVice.getGtjkrgjjzh()/*共同借款人公积金账号*/);
                this.setCHGX(loanHousingCoborrowerVice.getCdgx()/*参货关系 （0：本人或户主 1：配偶 2：子 3：女 4：孙子、孙女或外孙子、外孙女 5：父母 6：祖父母或外祖父母 7：兄、弟、姐、妹 8：其他）*/);
                this.setSJHM(loanHousingCoborrowerVice.getSjhm()/*手机号码*/);
                this.setJCD(loanHousingCoborrowerVice.getJcd()/*缴存地*/);
                this.setGDDHHM(loanHousingCoborrowerVice.getGddhhm()/*固定电话号码*/);
                this.setGTJKRXM(loanHousingCoborrowerVice.getGtjkrxm()/*共同借款人姓名*/);
                this.setGTJKRZJLX(loanHousingCoborrowerVice.getGtjkrzjlx()/*共同借款人证件类型*/);
                this.setYSR(loanHousingCoborrowerVice.getYsr() + ""/*月收入*/);
                this.setGTJKRZJHM(loanHousingCoborrowerVice.getGtjkrzjhm()/*共同借款人证件号码*/);
                this.setUnitInformation(new GetApplicantResponseApplicantInformationUnitInformation() {{
                    this.setDWDH(loanHousingCoborrowerVice.getDwdh()/*单位电话*/);
                    this.setDWMC(loanHousingCoborrowerVice.getDwmc()/*单位名称*/);
                    this.setDWZH(loanHousingCoborrowerVice.getDwzh()/*单位账号*/);
                    this.setDWXZ(loanHousingCoborrowerVice.getDwxz() + ""/*单位性质*/);
                    this.setDWDZ(loanHousingCoborrowerVice.getDwdz()/*单位地址*/);
                }});
                this.setAccountInformation(new GetApplicantResponseApplicantInformationAccountInformation() {{
                    this.setGRZHZT(loanHousingCoborrowerVice.getGrzhzt() + ""/*个人账户状态*/);
                    this.setJZNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, loanHousingCoborrowerVice.getJzny(), formatNY)/*缴至年月*/);
                    this.setYJCE(loanHousingCoborrowerVice.getYjce() + ""/*月缴存额*/);
                    this.setGRZHYE(loanHousingCoborrowerVice.getGrzhye() + ""/*个人账户余额*/);
                    this.setGRJCJS(loanHousingCoborrowerVice.getGrjcjs() + ""/*个人缴存基数*/);
                    this.setLXZCJCYS(loanHousingCoborrowerVice.getLxzcjcys() == null ? null : (loanHousingCoborrowerVice.getLxzcjcys().intValue() + "")/*连续正常缴存月数*/);
                }});
            }});

            this.setSCZL(loanHousingBusinessProcess.getBlzl()/*提交资料*/);

            this.setSQSJ(DateUtil.date2Str(loanHousingBusinessProcess.getBlsj(), format)/*申请时间*/);

            this.setHouseInformation(new GetApplicantResponseHouseInformation() {{

                this.setPurchaseSecondInformation((loanHousePurchasingVice == null || !loanHousePurchasingVice.getSfwesf()) ? new GetApplicantResponseHouseInformationPurchaseSecondInformation() : (loanHousePurchasingVice == null ? new GetApplicantResponseHouseInformationPurchaseSecondInformation() : new GetApplicantResponseHouseInformationPurchaseSecondInformation() {{

                    this.setFWJZMJ(loanHousePurchasingVice.getFwjzmj() + ""/*房屋建筑面积*/);
                    this.setGRSKYHZH(loanHousePurchasingVice.getGrskyhzh()/*个人收款银行账号*/);
                    this.setFWZL(loanHousePurchasingVice.getFwzl()/*房屋坐落*/);
                    this.setFWTNMJ(loanHousePurchasingVice.getFwtnmj() + ""/*房屋套内面积*/);
                    this.setFWXS(loanHousePurchasingVice.getFwxs() + ""/*房屋形式（0：在建房  1：现房）*/);
                    this.setFWXZ(loanHousePurchasingVice.getFwxz() + ""/*房屋性质（0：商品房 1：障碍性住房 2：自建房 3：两限房 4：集资房 5：危改房 6：经济适用房 7：房改房 8：其他）*/);
                    this.setFWZJ(loanHousePurchasingVice.getFwzj() + ""/*房屋总价*/);
                    this.setKHYHMC(loanHousePurchasingVice.getKhyhmc()/*开户银行名称*/);
                    this.setFWJGRQ(DateUtil.date2Str(loanHousePurchasingVice.getFwjgrq(), format)/*房屋竣工日期*/);
                    this.setFWJG(loanHousePurchasingVice.getFwjg() + ""/*房屋结构（0：框架 1：砖混 2：其他）*/);
                    this.setHTJE(loanHousePurchasingVice.getHtje() + ""/*合同金额*/);
                    this.setYHKHM(loanHousePurchasingVice.getYhkhm()/*银行开户名*/);
                    this.setGFHTBH(loanHousePurchasingVice.getGfhtbh()/*购房合同编号*/);
                    this.setDanJia(loanHousePurchasingVice.getDanJia() + "");
                    this.setLXFS(loanHousePurchasingVice.getLxfs()/*联系方式*/);
                    this.setSFRMC(loanHousePurchasingVice.getSfrmc()/*售房人名称*/);
                    this.setSFK(loanHousePurchasingVice.getYfk() + ""/*首付款*/);
                    this.setSCZL(loanHousePurchasingVice.getBlzl()/*上传资料*/);
                }}));

                this.setDKYT(loanHousingBusinessProcess.getDkyt() + ""/*贷款用途*/);

                this.setSFWESF(loanHousePurchasingVice == null ? null : loanHousePurchasingVice.getSfwesf() ? "1" : "0"/*是否为二手房*/);

                this.setPurchaseFirstInformation((loanHousePurchasingVice == null || loanHousePurchasingVice.getSfwesf()) ? new GetApplicantResponseHouseInformationPurchaseFirstInformation() : (loanHousePurchasingVice == null ? new GetApplicantResponseHouseInformationPurchaseFirstInformation() : new GetApplicantResponseHouseInformationPurchaseFirstInformation() {{
                    this.setFWJZMJ(loanHousePurchasingVice.getFwjzmj() + ""/*房屋建筑面积*/);
                    this.setFWZL(loanHousePurchasingVice.getFwzl()/*房屋坐落*/);
                    this.setFWTNMJ(loanHousePurchasingVice.getFwtnmj() + ""/*房屋套内面积*/);
                    this.setFWXS(loanHousePurchasingVice.getFwxs() + ""/*房屋形式（0：在建房  1：现房）*/);
                    this.setFWXZ(loanHousePurchasingVice.getFwxz() + ""/*房屋性质（0：商品房 1：障碍性住房 2：自建房 3：两限房 4：集资房 5：危改房 6：经济适用房 7：房改房 8：其他）*/);
                    this.setFWZJ(loanHousePurchasingVice.getFwzj() + ""/*房屋总价*/);
                    this.setFWJG(loanHousePurchasingVice.getFwjg() + ""/*房屋结构（0：框架 1：砖混 2：其他）*/);
                    this.setFWJGRQ(DateUtil.date2Str(loanHousePurchasingVice.getFwjgrq(), format)/*房屋竣工日期*/);
                    this.setSFRKHYHMC(loanHousePurchasingVice.getSfrkhyhmc()/*售房人开户银行名称*/);
                    this.setHTJE(loanHousePurchasingVice.getHtje() + ""/*合同金额*/);
                    this.setSFRZHHM(loanHousePurchasingVice.getSfrzhhm()/*售房人账户号码*/);
                    this.setLPMC(loanHousePurchasingVice.getLpmc()/*楼盘名称*/);
                    this.setGFHTBH(loanHousePurchasingVice.getGfhtbh()/*购房合同编号*/);
                    this.setDanJia(loanHousePurchasingVice.getDanJia() + ""/*单价*/);
                    this.setLXFS(loanHousePurchasingVice.getLxfs()/*联系方式*/);
                    this.setSFRMC(loanHousePurchasingVice.getSfrmc()/*售房人名称*/);
                    this.setSPFYSXKBH(loanHousePurchasingVice.getSpfysxkbh()/*商品房预售许可编号*/);
                    this.setSFK(loanHousePurchasingVice.getSfk() + ""/*首付款*/);
                    this.setSFRKHYHMC(loanHousePurchasingVice.getSfrkhyhmc()/*售房人银行开户名*/);
                    this.setSFRYHKHM(loanHousePurchasingVice.getSfryhkhm());
                    this.setSCZL(loanHousePurchasingVice.getBlzl()/*上传资料*/);
                }}));

                this.setOverhaulInformation(loanHouseOverhaulVice == null ? new GetApplicantResponseHouseInformationOverhaulInformation() : new GetApplicantResponseHouseInformationOverhaulInformation() {{
                    this.setDXGCYS(loanHouseOverhaulVice.getDxgcys() + ""/*大修工程预算*/);
                    this.setGRSKYHZH(loanHouseOverhaulVice.getGrskyhzh()/*个人收款银行账号*/);
                    this.setFWZJBGJGMCJBH(loanHouseOverhaulVice.getFwzjbgjgmcjbh(/*房屋质检报告机关名称及编号*/));
                    this.setFXHCS(loanHouseOverhaulVice.getTdsyzh()/*土地使用证号*/);
                    this.setFWZL(loanHouseOverhaulVice.getFwzl()/*房屋坐落*/);
                    this.setYJZMJ(loanHouseOverhaulVice.getYjzmj() + ""/*原建筑面积*/);
                    this.setKHYHMC(loanHouseOverhaulVice.getKhyhmc()/*开户银行名称*/);
                    this.setSCZL(loanHouseOverhaulVice.getBlzl()/*提交资料*/);
                    this.setYBDCZH(loanHouseOverhaulVice.getYbdczh()/*原不动产证号*/);
                    this.setJHJGRQ(DateUtil.date2Str(loanHouseOverhaulVice.getJhjgrq(), format)/*计划竣工日期*/);
                    this.setJHKGRQ(DateUtil.date2Str(loanHouseOverhaulVice.getJhkgrq(), format)/*计划开工日期*/);
                    this.setYHKHM(loanHouseOverhaulVice.getYhkhm()/*银行开户名*/);
                    this.setSCZL(loanHouseOverhaulVice.getBlzl()/*上传资料*/);
                }});

                this.setBuildInformation(loanHouseBuildVice == null ? new GetApplicantResponseHouseInformationBuildInformation() : new GetApplicantResponseHouseInformationBuildInformation() {{

                    this.setGRSKYHZH(loanHouseBuildVice.getGrskyhzh()/*个人收款银行账号*/);
                    this.setFWXZ(loanHouseBuildVice.getFwxz() + ""/*房屋性质（0：商品房 1：障碍性住房 2：自建房 3：两限房 4：集资房 5：危改房 6：经济适用房 7：房改房 8：其他）*/);
                    this.setFWJG(loanHouseBuildVice.getFwjg() + ""/*房屋结构（0：框架 1：砖混 2：其他）*/);
                    this.setJHJGRQ(DateUtil.date2Str(loanHouseBuildVice.getJhjgrq(), format)/*计划竣工日期*/);
                    this.setFWZLDZ(loanHouseBuildVice.getFwzl()/*房屋坐落地址*/);
                    this.setTDSYZH(loanHouseBuildVice.getTdsyzh()/*土地使用证号*/);
                    this.setGRSYZJ(loanHouseBuildVice.getGrsyzj() + ""/*个人使用资金*/);
                    this.setJZCS(loanHouseBuildVice.getJzcs()/*建造层数*/);
                    this.setSCZL(loanHouseBuildVice.getBlzl()/*提交资料*/);
                    this.setYHKHM(loanHouseBuildVice.getYhkhm()/*银行开户名*/);
                    this.setJZYDGHXKZH(loanHouseBuildVice.getJzydghxkzh()/*建造用地规划许可证号*/);
                    this.setPZJGWH(loanHouseBuildVice.getPzjgwh()/*批准机关文号*/);
                    this.setJHJZFY(loanHouseBuildVice.getJhjzfy() + ""/*计划建造费用*/);
                    this.setKHHYHMC(loanHouseBuildVice.getKhhyhmc()/*开户行银行名称*/);
                    this.setJZGCGHXKZH(loanHouseBuildVice.getJzgcghxkzh()/*建造工程规划许可证号*/);
                    this.setJCHJZMJ(loanHouseBuildVice.getJchjzmj() + ""/*建成后建造面积*/);
                    this.setJHKGRQ(DateUtil.date2Str(loanHouseBuildVice.getJhkgrq(), format)/*计划开工日期*/);
                    this.setJSGCSGXKZH(loanHouseBuildVice.getJsgcsgxkzh()/*建设工程施工许可证号*/);
                    this.setSCZL(loanHouseBuildVice.getBlzl()/*上传资料*/);
                }});
            }});

            this.setYWLSH(YWLSH/*业务流水号*/);

//            this.setreviewInformation(new ArrayList<GetApplicantResponseReviewInformation>() {{//审核信息
//
//
//                for (ReviewInfo reviewInfo : (List<ReviewInfo>) iSaveAuditHistory.getAuditHistoryList(YWLSH)) {
//
//                    this.add(new GetApplicantResponseReviewInformation() {{
//                        String ywwdmc = cAccountNetworkDAO.get(reviewInfo.getYWWD()).getMingCheng();
//                        this.setYWWD(ywwdmc);
//                        this.setShiJian(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, reviewInfo.getSHSJ(), format));
//                        this.setQuDao(reviewInfo.getCZQD());
//                        this.setZhiWu(reviewInfo.getZhiWu());
//                        this.setYiJian(reviewInfo.getYYYJ());
//                        this.setCZY(reviewInfo.getCZY());
//
//                    }});
//                }
//            }});

            this.setCollateralInformation(new GetApplicantResponseCollateralInformation() {{

                this.setMortgageInformation(new ArrayList<GetApplicantResponseCollateralInformationMortgageInformation>() {{

                    for (CLoanGuaranteeMortgageVice loanGuaranteeMortgageVice : list_loanGuaranteeMortgageVice) {

                        this.add(new GetApplicantResponseCollateralInformationMortgageInformation() {{
                            this.setDYWSYQRSFZHM(loanGuaranteeMortgageVice.getDywsyqrsfzhm()/*抵押物所有权人身份证号码*/);
                            this.setDYFWXS(loanGuaranteeMortgageVice.getDyfwxs() + ""/*抵押房屋形式（0：住宅 （期房） 1：住宅（现房） 2：商铺 3：其他）*/);
                            this.setDYWFWZL(loanGuaranteeMortgageVice.getDywfwzl()/*抵押物房屋坐落*/);
                            this.setDYWMC(loanGuaranteeMortgageVice.getDywmc()/*抵押物名称*/);
                            this.setFWJG(loanGuaranteeMortgageVice.getFwjg() + ""/*房屋结构（0：框架 1：砖混 2：土木 3：其他）*/);
                            this.setDYWGYQRLXDH(loanGuaranteeMortgageVice.getDywgyqrlxdh()/*抵押物共有权人联系电话*/);
                            this.setDYWPGJZ(loanGuaranteeMortgageVice.getDywpgjz() + ""/*抵押物评估价值*/);
                            this.setDYWGYQRSFZHM(loanGuaranteeMortgageVice.getDywgyqrsfzhm()/*抵押物共有权人身份证号码*/);
                            this.setDYWSYQRXM(loanGuaranteeMortgageVice.getDywsyqrxm()/*抵押物所有权人姓名*/);
                            this.setDYWSYQRLXDH(loanGuaranteeMortgageVice.getDywsyqrlxdh()/*抵押物所有权人联系电话*/);
                            this.setFWMJ(loanGuaranteeMortgageVice.getFwmj() + ""/*房屋面积1*/);
                            this.setQSZSBH(loanGuaranteeMortgageVice.getQszsbh()/*权属证书编号*/);
                            this.setDYWGYQRXM(loanGuaranteeMortgageVice.getDywgyqrxm()/*抵押物共有权人姓名*/);
                        }});

                    }
                }});

                this.setPledgeInformation(new ArrayList<GetApplicantResponseCollateralInformationPledgeInformation>() {{

                    for (CLoanGuaranteePledgeVice loanGuaranteePledgeVice : list_loanGuaranteePledgeVice) {

                        this.add(new GetApplicantResponseCollateralInformationPledgeInformation() {{
                            this.setZYWSYQRSFZHM(loanGuaranteePledgeVice.getZywsyqrsfzhm()/*质押物所有权人身份证号码*/);
                            this.setZYWSYQRXM(loanGuaranteePledgeVice.getZywsyqrxm()/*质押物所有权人姓名*/);
                            this.setZYWJZ(loanGuaranteePledgeVice.getZywjz() + ""/*质押物价值*/);
                            this.setZYWSYQRLXDH(loanGuaranteePledgeVice.getZywsyqrlxdh()/*质押物所有权人联系电话*/);
                            this.setZYWMC(loanGuaranteePledgeVice.getZywmc()/*质押物名称*/);
                        }});

                    }
                }});

                this.setDKDBLX(loanHousingBusinessProcess.getDkdblx()/*贷款担保类型*/);

                this.setGuaranteeInformation(new ArrayList<GetApplicantResponseCollateralInformationGuaranteeInformation>() {{

                    for (CLoanGuaranteeVice loanGuaranteeVice : list_loanGuaranteeVice) {

                        this.add(new GetApplicantResponseCollateralInformationGuaranteeInformation() {{
                            this.setYZBM(loanGuaranteeVice.getYzbm()/*邮政编码*/);
                            this.setBZRXJZDZ(loanGuaranteeVice.getFrdbxjzdz()/*保证人现居住地址*/);
                            this.setBZRSFZHM(loanGuaranteeVice.getFrdbsfzhm()/*保证人身份证号码*/);
                            this.setBZRXM(loanGuaranteeVice.getFrdbxm()/*保证人姓名*/);
                            this.setBZRLXDH(loanGuaranteeVice.getFrdblxdh()/*保证人联系电话*/);
                            this.setTXDZ(loanGuaranteeVice.getTxdz()/*通讯地址*/);
                            this.setBZFLX(loanGuaranteeVice.getBzflx()/*保证方类型（0：个人 1：机构）*/);
                        }});

                    }
                }});

                this.setSCZL(loanHousingGuaranteeContractVice.getBlzl()/*提交资料*/);
            }});

            this.setCapitalInformation(new GetApplicantResponseCapitalInformation() {{
                this.setHTDKJEDX(loanFundsVice.getHtdkjedx()/*合同贷款金额大写*/);
                this.setDKLX(loanFundsVice.getDklx() + ""/*贷款类型（0：公积金贷款 1：组合贷款 2：贴息贷款 3：其他）*/);
                this.setDKQS(loanFundsVice.getDkqs() + ""/*贷款期数*/);
                this.setHKFS(loanFundsVice.getHkfs() + ""/*还款方式（0：等额本息 1：等额本金 2：一次还款付息 3：自由还款方式 4：其他）*/);
                this.setHTDKJE(loanFundsVice.getHtdkje() + ""/*合同贷款金额*/);
                this.setLLFSBL((loanFundsVice.getLlfsbl() == null ? "0" : loanFundsVice.getLlfsbl()) + ""/*利率浮动比例*/);
                this.setJKHTLL(loanFundsVice.getJkhtll()==null?"0":(loanFundsVice.getJkhtll().doubleValue() + "")/*借款合同利率*/);
                //this.setZXLL(loanFundsVice.getZxll() + ""/*执行利率*/);
                this.setWTKHYJCE(loanFundsVice.getWtkhyjce() ? "1" : "0"/*委托扣划月缴存额*/);
                this.setDKDBLX(loanFundsVice.getDkdblx() + ""/*贷款担保类型  （0：抵押 1：质押 2：保证 3：其他）*/);
                this.setFWTS(loanFundsVice.getFwts() + ""/*房屋套数（0：一套 1：二套 2：三套 3：四套  5：五套及以上）*/);
            }});

            this.setApplicantInformation(new GetApplicantResponseApplicantInformation() {{
                this.setBorrowerInformation(new GetApplicantResponseApplicantInformationBorrowerInformation() {{
                    this.setHYZK(loanHousingPersonInformationVice.getHyzk() + ""/*婚姻状态（0：已婚 1：未婚 2：丧偶 3：离婚 4：未说明的婚姻状况）*/);
                    this.setNianLing(loanHousingPersonInformationVice.getNianLing()/*年龄*/);
                    this.setJTZZ(loanHousingPersonInformationVice.getJtzz()/*家庭住址*/);
                    this.setCSNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, loanHousingPersonInformationVice.getCsny(), formatNY)/*出生年月*/);
                    this.setJKRZJHM(loanHousingPersonInformationVice.getJkrzjhm()/*借款人证件号码*/);
                    this.setYGXZ(loanHousingPersonInformationVice.getYgxz() + ""/*用工性质（0：正式职工 1：合同制 2：聘用制）*/);
                    this.setYSR(loanHousingPersonInformationVice.getYsr() + ""/*月收入*/);
                    this.setJKRXM(loanHousingPersonInformationVice.getJkrxm()/*借款人姓名*/);
                    this.setHKSZD(loanHousingPersonInformationVice.getHkszd()/*户口所在地*/);
                    this.setZhiCheng(loanHousingPersonInformationVice.getZhiCheng()/*职称*/);
                    this.setJKRZJLX(loanHousingPersonInformationVice.getJkrzjlx() + ""/*借款人证件类型*/);
                    this.setSJHM(loanHousingPersonInformationVice.getSjhm()/*手机号码*/);
                    this.setJKZK(loanHousingPersonInformationVice.getJkzk() + ""/*健康状态（0：良好 1：一般 2：差）*/);
                    this.setGDDHHM(loanHousingPersonInformationVice.getGddhhm()/*固定电话号码*/);
                    this.setZYJJLY(loanHousingPersonInformationVice.getZyjjly() + ""/*主要经济来源（0：工资收入 1：个体经营收入 2：其他非工资收入）*/);
                    this.setXingBie(loanHousingPersonInformationVice.getXingBie() + ""/*性别*/);
                    this.setJTYSR(loanHousingPersonInformationVice.getJtysr() + ""/*家庭月收入*/);
                    this.setXueLi(loanHousingPersonInformationVice.getXueLi()/*学历*/);
                    this.setZhiWu(loanHousingPersonInformationVice.getZhiWu()/*职务*/);
                }});
                this.setJKRGJJZH(loanHousingPersonInformationVice.getJkrgjjzh()/*借款人公积金账号*/);
                this.setUnitInformation(new GetApplicantResponseApplicantInformationUnitInformation() {{
                    this.setDWDH(loanHousingPersonInformationVice.getDwdh()/*单位电话*/);
                    this.setDWMC(loanHousingPersonInformationVice.getDwmc()/*单位名称*/);
                    this.setDWZH(loanHousingPersonInformationVice.getDwzh()/*单位账号*/);
                    this.setDWXZ(loanHousingPersonInformationVice.getDwxz() + ""/*单位性质*/);
                    this.setDWDZ(loanHousingPersonInformationVice.getDwdz()/*单位地址*/);
                }});
                this.setJCD(loanHousingPersonInformationVice.getJcd()/*缴存地*/);
                this.setSCZL(loanHousingPersonInformationVice.getBlzl()/* 办理资料*/);
                this.setAccountInformation(new GetApplicantResponseApplicantInformationAccountInformation() {{
                    this.setGRZHZT(loanHousingPersonInformationVice.getGrzhzt() + ""/*个人账户状态*/);
                    this.setJZNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, loanHousingPersonInformationVice.getJzny(), formatNY)/*缴至年月*/);
                    this.setYJCE(loanHousingPersonInformationVice.getYjce() + ""/*月缴存额*/);
                    this.setGRZHYE(loanHousingPersonInformationVice.getGrzhye() + ""/*个人账户余额*/);
                    this.setGRJCJS(loanHousingPersonInformationVice.getGrjcjs() + ""/*个人缴存基数*/);
                    this.setLXZCJCYS((loanHousingPersonInformationVice.getLxzcjcys() == null ? 0 : loanHousingPersonInformationVice.getLxzcjcys().intValue()) + ""/*连续正常缴存月数*/);
                }});
            }});

            this.setYWWD(loanHousingBusinessProcess.getYwwd().getMingCheng());

            this.setCZY(loanHousingBusinessProcess.getCzy());
        }};
    }


    /*
     * 获取审批表id
     *
     * @param YWLSH 业务流水号
     **/
    public CommonResponses getSPBId(TokenContext tokenContext, String YWLSH) {

        //region //查询必要字段

        CLoanHousingBusinessProcess loanHousingBusinessProcess = DAOBuilder.instance(this.loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("ywlsh", YWLSH);

            this.put("cznr", LoanBusinessType.贷款发放.getCode());

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        //endregion

        //region //数据完整性验证

        if (loanHousingBusinessProcess == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }

        if (loanHousingBusinessProcess.getLoanHousingPersonInformationVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }

        if (loanHousingBusinessProcess.getLoanHousePurchasingVice() == null && loanHousingBusinessProcess.getLoanHouseBuildVice() == null && loanHousingBusinessProcess.getLoanHouseOverhaulVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }

        if (loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }

        if (loanHousingBusinessProcess.getLoanFundsVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }

        //endregion

        return new CommonResponses() {{

            this.setId(loanHousingBusinessProcess.getLoanHousingPersonInformationVice().getSpbwj());

            this.setState("success");
        }};
    }

    /*
    *  completed
    *
    * !逻辑已完成
    *
    * 已测试 lian
    *
    * */
    @Override
    public CommonResponses postApplyLoan(TokenContext tokenContext, String status, ApplicantPost body) {

        //region //参数验证

        boolean hasCoborrower = "20".equals(body.getApplicantInformation().getInformation().getBorrowerInformation().getHYZK());// StringUtil.notEmpty(body.getCommonBorrowerInformation().getCoBorrowerInformation().getGTJKRGJJZH());//是否有共同借款人

        boolean allowNull = status == null || status.equals("0");//允许为空

        boolean isBuild = body.getHouseInformation().getDKYT(/*贷款用途*/).equals("1");//自建

        boolean isOverhaul = (!isBuild) && body.getHouseInformation().getDKYT(/*贷款用途*/).equals("2");//大修

        boolean isSecondHands = (!isBuild) && (!isOverhaul) && body.getHouseInformation().getDKYT(/*贷款用途*/).equals("0") && body.getHouseInformation().getSFWESF(/*是否为二手房*/).equals("1");//购买二手

        boolean isFirstHands = (!isSecondHands) && (!isBuild) && (!isOverhaul) && body.getHouseInformation().getDKYT(/*贷款用途*/).equals("0") && body.getHouseInformation().getSFWESF(/*是否为二手房*/).equals("0");//购买一手

        boolean isGuaranteeMortgage = body.getCollateralInformation().getDKDBLX(/*贷款担保类型*/).equals(LoanGuaranteeType.抵押.getCode());//抵押

        boolean isGuaranteePledge = (!isGuaranteeMortgage) && body.getCollateralInformation().getDKDBLX(/*贷款担保类型*/).equals("1");//质押


        //时间

        if (!DateUtil.isFollowFormat(body.getApplicantInformation().getInformation().getBorrowerInformation().getCSNY(), formatNY, allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "出生年月");
        }

        if (!DateUtil.isFollowFormat(body.getApplicantInformation().getInformation().getAccountInformation().getJZNY(), formatNY, true)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "缴至年月");
        }

        if (!DateUtil.isFollowFormat(body.getCommonBorrowerInformation().getAccountInformation().getJZNY(), formatNY, (!hasCoborrower) || true)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "缴至年月 ");
        }

        if (!DateUtil.isFollowFormat(body.getHouseInformation().getPurchaseSecondInformation().getFWJGRQ(), format, (!isSecondHands) || true)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "房屋竣工日期");
        }

        if (!DateUtil.isFollowFormat(body.getHouseInformation().getPurchaseFirstInformation().getFWJGRQ(), format, (!isFirstHands) || true)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "房屋竣工日期");
        }

        if (!DateUtil.isFollowFormat(body.getHouseInformation().getBuildInformation().getJHKGRQ(), format, (!isBuild) || true)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "计划开工日期");
        }

        if (!DateUtil.isFollowFormat(body.getHouseInformation().getOverhaulInformation().getJHKGRQ(), format, (!isOverhaul) || true)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "计划开工日期");
        }

        if (!DateUtil.isFollowFormat(body.getHouseInformation().getOverhaulInformation().getJHJGRQ(), format, (!isOverhaul) || true)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "计划竣工日期");
        }


        //number

        if (!StringUtil.isDigits(body.getApplicantInformation().getInformation().getBorrowerInformation().getYSR(), true)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "月收入");
        }

        if (!StringUtil.isDigits(body.getApplicantInformation().getInformation().getBorrowerInformation().getJTYSR(), allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "家庭月收入");
        }

//        if (!StringUtil.isDigits(body.getApplicantInformation().getInformation().getAccountInformation().getYJCE(), true)) {
//            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "月缴存额");
//        }

//        if (!StringUtil.isDigits(body.getApplicantInformation().getInformation().getAccountInformation().getGRZHYE(), allowNull)) {
//            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "个人账户余额");
//        }

        if (!StringUtil.isDigits(body.getApplicantInformation().getInformation().getAccountInformation().getGRJCJS(), allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "个人缴存基数");
        }

        if (!StringUtil.isDigits(body.getCommonBorrowerInformation().getCoBorrowerInformation().getYSR(), (!hasCoborrower) || true)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "月收入 ");
        }

//        if (!StringUtil.isDigits(body.getCommonBorrowerInformation().getAccountInformation().getYJCE(), (!hasCoborrower) || true)) {
//            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "月缴存额 ");
//        }

//        if (!StringUtil.isDigits(body.getCommonBorrowerInformation().getAccountInformation().getGRZHYE(), (!hasCoborrower) || true)) {
//            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "个人账户余额 ");
//        }

        if (!StringUtil.isDigits(body.getCommonBorrowerInformation().getAccountInformation().getGRJCJS(), (!hasCoborrower) || true)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "个人缴存基数 ");
        }

//        if (!StringUtil.isDigits(body.getCommonBorrowerInformation().getAccountInformation().getLXZCJCYS(), (!hasCoborrower) || true)) {
//            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "连续正常缴存月数 ");
//        }

        if (!StringUtil.isDigits(body.getHouseInformation().getPurchaseSecondInformation().getFWJZMJ(), (!isSecondHands) || allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "房屋建筑面积");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getPurchaseSecondInformation().getFWTNMJ(), (!isSecondHands) || allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "房屋套内面积");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getPurchaseSecondInformation().getFWZJ(), (!isSecondHands) || allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "房屋总价");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getPurchaseSecondInformation().getHTJE(), (!isSecondHands) || allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "合同金额");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getPurchaseSecondInformation().getYFK(), (!isSecondHands) || allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "已付款（全部付完）");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getPurchaseSecondInformation().getDanJia(), (!isSecondHands) || true)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "单价");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getSFWESF(), ((!isFirstHands && !isSecondHands)) || allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "是否为二手房");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getPurchaseFirstInformation().getFWJZMJ(), (!isFirstHands) || allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "房屋建筑面积");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getPurchaseFirstInformation().getDanJia(), (!isFirstHands) || true)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "单价");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getPurchaseFirstInformation().getFWTNMJ(), (!isFirstHands) || allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "房屋套内面积");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getPurchaseFirstInformation().getFWZJ(), (!isFirstHands) || allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "房屋总价");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getPurchaseFirstInformation().getHTJE(), (!isFirstHands) || allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "合同金额");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getPurchaseFirstInformation().getSFK(), (!isFirstHands) || allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "首付款");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getBuildInformation().getGRSYZJ(), (!isBuild) || allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "个人使用资金");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getBuildInformation().getJHJZFY(), (!isBuild) || allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "计划建造费用");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getBuildInformation().getJCHJZMJ(), (!isBuild) || allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "建成后建造面积");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getOverhaulInformation().getDXGCYS(), (!isOverhaul) || allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "大修工程预算");
        }

        if (!StringUtil.isDigits(body.getHouseInformation().getOverhaulInformation().getYJZMJ(), (!isOverhaul) || true)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "原建筑面积");
        }

//        if(!StringUtil.isDigits(body.getCollateralInformation().getMortgageInformation().getDYWPGJZ(),(!isGuaranteeMortgage)||allowNull)){throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"抵押物评估价值");}
//
//        if(!StringUtil.isDigits(body.getCollateralInformation().getMortgageInformation().getFWMJ(),(!isGuaranteeMortgage)||allowNull)){throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"房屋面积");}
//
//        if(!StringUtil.isDigits(body.getCollateralInformation().getPledgeInformation().getZYWJZ(),(!isGuaranteePledge)||allowNull)){throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"质押物价值");}
//
        if (!StringUtil.isDigits(body.getCapitalInformation().getHTDKJE(), allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "合同贷款金额");
        }

        if (!StringUtil.isDigits(body.getCapitalInformation().getLLFSBL(), allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "利率浮动比例");
        }

        if (!StringUtil.isDigits(body.getCapitalInformation().getJKHTLL(), allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "借款合同利率");
        }

        if (!StringUtil.isDigits(body.getCapitalInformation().getWTKHYJCE(), allowNull)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "委托划扣月缴存额");
        }

        // if (!StringUtil.isDigits(body.getCapitalInformation().getZXLL(), allowNull)) {  throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "执行利率");   }


        //endregion

        //region //必要数据声明&关系配置
        CLoanHousingBusinessProcess loanHousingBusinessProcess = new CLoanHousingBusinessProcess();

        CLoanHousingPersonInformationVice loanHousingPersonInformationVice = new CLoanHousingPersonInformationVice();
        loanHousingBusinessProcess.setLoanHousingPersonInformationVice(loanHousingPersonInformationVice);
        loanHousingPersonInformationVice.setGrywmx(loanHousingBusinessProcess);


        CLoanHousePurchasingVice loanHousePurchasingVice = new CLoanHousePurchasingVice();
        loanHousingBusinessProcess.setLoanHousePurchasingVice(loanHousePurchasingVice);
        loanHousePurchasingVice.setGrywmx(loanHousingBusinessProcess);


        CLoanHouseBuildVice loanHouseBuildVice = new CLoanHouseBuildVice();
        loanHousingBusinessProcess.setLoanHouseBuildVice(loanHouseBuildVice);
        loanHouseBuildVice.setGrywmx(loanHousingBusinessProcess);


        CLoanFundsVice loanFundsVice = new CLoanFundsVice();
        loanHousingBusinessProcess.setLoanFundsVice(loanFundsVice);
        loanFundsVice.setGrywmx(loanHousingBusinessProcess);


        CLoanHouseOverhaulVice loanHouseOverhaulVice = new CLoanHouseOverhaulVice();
        loanHousingBusinessProcess.setLoanHouseOverhaulVice(loanHouseOverhaulVice);
        loanHouseOverhaulVice.setGrywmx(loanHousingBusinessProcess);


        CLoanHousingCoborrowerVice loanHousingCoborrowerVice = new CLoanHousingCoborrowerVice();
        loanHousingBusinessProcess.setLoanHousingCoborrowerVice(loanHousingCoborrowerVice);
        loanHousingCoborrowerVice.setGrywmx(loanHousingBusinessProcess);

        CLoanHousingGuaranteeContractVice loanHousingGuaranteeContractVice = new CLoanHousingGuaranteeContractVice();
        loanHousingBusinessProcess.setLoanHousingGuaranteeContractVice(loanHousingGuaranteeContractVice);
        loanHousingGuaranteeContractVice.setGrywmx(loanHousingBusinessProcess);


        List<CLoanGuaranteeMortgageVice> list_loanGuaranteeMortgageVice = new ArrayList<CLoanGuaranteeMortgageVice>();
        loanHousingGuaranteeContractVice.setGuaranteeMortgageVices(list_loanGuaranteeMortgageVice);

        List<CLoanGuaranteeVice> list_loanGuaranteeVice = new ArrayList<CLoanGuaranteeVice>();
        loanHousingGuaranteeContractVice.setGuaranteeVices(list_loanGuaranteeVice);

        List<CLoanGuaranteePledgeVice> list_loanGuaranteePledgeVice = new ArrayList<CLoanGuaranteePledgeVice>();
        loanHousingGuaranteeContractVice.setGuaranteePledgeVices(list_loanGuaranteePledgeVice);

        //endregion

        //region //字段填充

        //common
        loanHousingBusinessProcess.setCznr(LoanBusinessType.贷款发放.getCode());
        loanHousingBusinessProcess.setBlsj(new Date());

        //region//申请人信息
        loanHousingPersonInformationVice.setHyzk(body.getApplicantInformation().getInformation().getBorrowerInformation().getHYZK(/*婚姻状态（0：已婚 1：未婚 2：丧偶 3：离婚 4：未说明的婚姻状况）*/));
        loanHousingPersonInformationVice.setNianLing(body.getApplicantInformation().getInformation().getBorrowerInformation().getNianLing(/*年龄*/));
        loanHousingPersonInformationVice.setJtzz(body.getApplicantInformation().getInformation().getBorrowerInformation().getJTZZ(/*家庭住址*/));
        loanHousingPersonInformationVice.setCsny(DateUtil.safeStr2DBDate(formatNY, body.getApplicantInformation().getInformation().getBorrowerInformation().getCSNY(/*出生年月*/), DateUtil.dbformatYear_Month));
        loanHousingPersonInformationVice.setJkrzjhm(body.getApplicantInformation().getInformation().getBorrowerInformation().getJKRZJHM(/*借款人证件号码*/));
        loanHousingPersonInformationVice.setYgxz(body.getApplicantInformation().getInformation().getBorrowerInformation().getYGXZ(/*用工性质（0：正式职工 1：合同制 2：聘用制）*/));
        loanHousingPersonInformationVice.setYsr(StringUtil.safeBigDecimal(body.getApplicantInformation().getInformation().getBorrowerInformation().getYSR(/*月收入*/)));
        loanHousingPersonInformationVice.setJkrxm(body.getApplicantInformation().getInformation().getBorrowerInformation().getJKRXM(/*借款人姓名*/));
        loanHousingPersonInformationVice.setHkszd(body.getApplicantInformation().getInformation().getBorrowerInformation().getHKSZD(/*户口所在地*/));
        loanHousingPersonInformationVice.setZhiCheng(body.getApplicantInformation().getInformation().getBorrowerInformation().getZhiCheng(/*职称*/));
        loanHousingPersonInformationVice.setJkrzjlx(body.getApplicantInformation().getInformation().getBorrowerInformation().getJKRZJLX(/*借款人证件类型*/));
        loanHousingPersonInformationVice.setSjhm(body.getApplicantInformation().getInformation().getBorrowerInformation().getSJHM(/*手机号码*/));
        loanHousingPersonInformationVice.setJkzk(body.getApplicantInformation().getInformation().getBorrowerInformation().getJKZK(/*健康状态（0：良好 1：一般 2：差）*/));
        loanHousingPersonInformationVice.setGddhhm(body.getApplicantInformation().getInformation().getBorrowerInformation().getGDDHHM(/*固定电话号码*/));
        loanHousingPersonInformationVice.setZyjjly(body.getApplicantInformation().getInformation().getBorrowerInformation().getZYJJLY(/*主要经济来源（0：工资收入 1：个体经营收入 2：其他非工资收入）*/));
        loanHousingPersonInformationVice.setXingBie(body.getApplicantInformation().getInformation().getBorrowerInformation().getXingBie(/*性别*/).toCharArray()[0]);
        loanHousingPersonInformationVice.setJtysr(StringUtil.safeBigDecimal(body.getApplicantInformation().getInformation().getBorrowerInformation().getJTYSR(/*家庭月收入*/)));
        loanHousingPersonInformationVice.setXueLi(body.getApplicantInformation().getInformation().getBorrowerInformation().getXueLi(/*学历*/));
        loanHousingPersonInformationVice.setZhiWu(body.getApplicantInformation().getInformation().getBorrowerInformation().getZhiWu(/*职务*/));


        loanHousingPersonInformationVice.setBlzl(body.getApplicantInformation().getInformation().getSCZL(/* 办理资料*/));


        loanHousingPersonInformationVice.setGrzhzt(body.getApplicantInformation().getInformation().getAccountInformation().getGRZHZT(/*个人账户状态*/));
        loanHousingPersonInformationVice.setJzny(DateUtil.safeStr2DBDate(formatNY, body.getApplicantInformation().getInformation().getAccountInformation().getJZNY(/*缴至年月*/), DateUtil.dbformatYear_Month));
        loanHousingPersonInformationVice.setYjce(StringUtil.safeBigDecimal(body.getApplicantInformation().getInformation().getAccountInformation().getYJCE(/*月缴存额*/)));
        loanHousingPersonInformationVice.setGrzhye(StringUtil.safeBigDecimal(body.getApplicantInformation().getInformation().getAccountInformation().getGRZHYE(/*个人账户余额*/)));
        loanHousingPersonInformationVice.setGrjcjs(StringUtil.safeBigDecimal(body.getApplicantInformation().getInformation().getAccountInformation().getGRJCJS(/*个人缴存基数*/)));
        loanHousingPersonInformationVice.setLxzcjcys(StringUtil.safeBigDecimal(body.getApplicantInformation().getInformation().getAccountInformation().getLXZCJCYS(/*连续正常缴存月数*/)));


        loanHousingPersonInformationVice.setDwdh(body.getApplicantInformation().getInformation().getUnitInformation().getDWDH(/*单位电话*/));
        loanHousingPersonInformationVice.setDwmc(body.getApplicantInformation().getInformation().getUnitInformation().getDWMC(/*单位名称*/));
        loanHousingPersonInformationVice.setDwzh(body.getApplicantInformation().getInformation().getUnitInformation().getDWZH(/*单位账号*/));
        loanHousingPersonInformationVice.setDwxz(body.getApplicantInformation().getInformation().getUnitInformation().getDWXZ(/*单位性质*/));
        loanHousingPersonInformationVice.setDwdz(body.getApplicantInformation().getInformation().getUnitInformation().getDWDZ(/*单位地址*/));


        loanHousingPersonInformationVice.setJkrgjjzh(body.getApplicantInformation().getJKRGJJZH(/*借款人公积金账号*/));
        loanHousingPersonInformationVice.setJcd(body.getApplicantInformation().getJCD(/*缴存地*/));

        //endregion

        //region //共同借款人信息

        loanHousingCoborrowerVice.setGtjkrgjjzh(body.getCommonBorrowerInformation().getCoBorrowerInformation().getGTJKRGJJZH(/*共同借款人公积金账号 */));
        loanHousingCoborrowerVice.setCdgx(body.getCommonBorrowerInformation().getCoBorrowerInformation().getCHGX(/*参货关系 （0：本人或户主 1：配偶 2：子 3：女 4：孙子、孙女或外孙子、外孙女 5：父母 6：祖父母或外祖父母 7：兄、弟、姐、妹 8：其他） */));
        loanHousingCoborrowerVice.setSjhm(body.getCommonBorrowerInformation().getCoBorrowerInformation().getSJHM(/*手机号码 */));
        loanHousingCoborrowerVice.setJcd(body.getCommonBorrowerInformation().getCoBorrowerInformation().getJCD(/*缴存地 */));
        loanHousingCoborrowerVice.setGddhhm(body.getCommonBorrowerInformation().getCoBorrowerInformation().getGDDHHM(/*固定电话号码 */));
        loanHousingCoborrowerVice.setGtjkrxm(body.getCommonBorrowerInformation().getCoBorrowerInformation().getGTJKRXM(/*共同借款人姓名 */));
        loanHousingCoborrowerVice.setGtjkrzjlx(body.getCommonBorrowerInformation().getCoBorrowerInformation().getGTJKRZJLX(/*共同借款人证件类型 */));
        loanHousingCoborrowerVice.setYsr(StringUtil.safeBigDecimal(body.getCommonBorrowerInformation().getCoBorrowerInformation().getYSR(/*月收入 */)));
        loanHousingCoborrowerVice.setGtjkrzjhm(body.getCommonBorrowerInformation().getCoBorrowerInformation().getGTJKRZJHM(/*共同借款人证件号码 */));
        loanHousingCoborrowerVice.setHkszd(body.getCommonBorrowerInformation().getCoBorrowerInformation().getHKSZD()/*户口所在地*/);

        loanHousingCoborrowerVice.setBlzl(body.getCommonBorrowerInformation().getSCZL(/*提交资料*/));


        loanHousingCoborrowerVice.setGrzhzt(body.getCommonBorrowerInformation().getAccountInformation().getGRZHZT(/*个人账户状态 */));
        loanHousingCoborrowerVice.setJzny(DateUtil.safeStr2DBDate(formatNY, body.getCommonBorrowerInformation().getAccountInformation().getJZNY(/*缴至年月 */), DateUtil.dbformatYear_Month));
        loanHousingCoborrowerVice.setYjce(StringUtil.safeBigDecimal(body.getCommonBorrowerInformation().getAccountInformation().getYJCE(/*月缴存额 */)));
        loanHousingCoborrowerVice.setGrzhye(StringUtil.safeBigDecimal(body.getCommonBorrowerInformation().getAccountInformation().getGRZHYE(/*个人账户余额 */)));
        loanHousingCoborrowerVice.setGrjcjs(StringUtil.safeBigDecimal(body.getCommonBorrowerInformation().getAccountInformation().getGRJCJS(/*个人缴存基数 */)));
        loanHousingCoborrowerVice.setLxzcjcys(StringUtil.safeBigDecimal(body.getCommonBorrowerInformation().getAccountInformation().getLXZCJCYS(/*连续正常缴存月数 */)));


        loanHousingCoborrowerVice.setDwdh(body.getCommonBorrowerInformation().getUnitInformation().getDWDH(/*单位电话 */));
        loanHousingCoborrowerVice.setDwmc(body.getCommonBorrowerInformation().getUnitInformation().getDWMC(/*单位名称 */));
        loanHousingCoborrowerVice.setDwzh(body.getCommonBorrowerInformation().getUnitInformation().getDWZH(/*单位账号 */));
        loanHousingCoborrowerVice.setDwxz(body.getCommonBorrowerInformation().getUnitInformation().getDWXZ(/*单位性质 */));
        loanHousingCoborrowerVice.setDwdz(body.getCommonBorrowerInformation().getUnitInformation().getDWDZ(/*单位地址 */));

        if (!hasCoborrower) {

            loanHousingBusinessProcess.setLoanHousingCoborrowerVice(null);

        }
        //endregion

        //region //房屋信息

        loanHousingBusinessProcess.setDkyt(body.getHouseInformation().getDKYT(/*贷款用途*/));

        if (body.getHouseInformation().getDKYT(/*贷款用途*/).equals("0")) {

            //region //购买

            if (body.getHouseInformation().getSFWESF(/*是否为二手房*/).equals("1")) {

                //region //二手房

                loanHousePurchasingVice.setFwjzmj(StringUtil.safeBigDecimal(body.getHouseInformation().getPurchaseSecondInformation().getFWJZMJ(/*房屋建筑面积*/)));
                loanHousePurchasingVice.setGrskyhzh(body.getHouseInformation().getPurchaseSecondInformation().getGRSKYHZH(/*个人收款银行账号*/));
                loanHousePurchasingVice.setFwtnmj(StringUtil.safeBigDecimal(body.getHouseInformation().getPurchaseSecondInformation().getFWTNMJ(/*房屋套内面积*/)));
                loanHousePurchasingVice.setFwzj(StringUtil.safeBigDecimal(body.getHouseInformation().getPurchaseSecondInformation().getFWZJ(/*房屋总价*/)));
                loanHousePurchasingVice.setFwxz(body.getHouseInformation().getPurchaseSecondInformation().getFWXZ(/*房屋性质（0：商品房 1：障碍性住房 2：自建房 3：两限房 4：集资房 5：危改房 6：经济适用房 7：房改房 8：其他）*/));
                loanHousePurchasingVice.setKhyhmc(body.getHouseInformation().getPurchaseSecondInformation().getKHYHMC(/*开户银行名称*/));
                loanHousePurchasingVice.setFwjgrq(DateUtil.safeStr2Date(format, body.getHouseInformation().getPurchaseSecondInformation().getFWJGRQ(/*房屋竣工日期*/)));
                loanHousePurchasingVice.setFwjg(body.getHouseInformation().getPurchaseSecondInformation().getFWJG(/*房屋结构（0：框架 1：砖混 2：其他）*/));
                loanHousePurchasingVice.setHtje(StringUtil.safeBigDecimal(body.getHouseInformation().getPurchaseSecondInformation().getHTJE(/*合同金额*/)));
                loanHousePurchasingVice.setYhkhm(body.getHouseInformation().getPurchaseSecondInformation().getYHKHM(/*银行开户名*/));
                loanHousePurchasingVice.setGfhtbh(body.getHouseInformation().getPurchaseSecondInformation().getGFHTBH(/*购房合同编号*/));
                loanHousePurchasingVice.setBlzl(body.getHouseInformation().getPurchaseSecondInformation().getSCZL(/*资料*/));
                loanHousePurchasingVice.setYfk(StringUtil.safeBigDecimal(body.getHouseInformation().getPurchaseSecondInformation().getYFK(/*已付款（全部付完）*/)));
                loanHousePurchasingVice.setDanJia(StringUtil.safeBigDecimal(body.getHouseInformation().getPurchaseSecondInformation().getDanJia()));
                loanHousePurchasingVice.setLxfs(body.getHouseInformation().getPurchaseSecondInformation().getLXFS()); //联系方式
                loanHousePurchasingVice.setSfrmc(body.getHouseInformation().getPurchaseSecondInformation().getSFRMC());//售房人名称
                loanHousePurchasingVice.setFwzl(body.getHouseInformation().getPurchaseSecondInformation().getFWZL());//房屋坐落
                loanHousePurchasingVice.setFwxs(body.getHouseInformation().getPurchaseSecondInformation().getFWXS());


                loanHousePurchasingVice.setSfrkhyhmc(body.getHouseInformation().getPurchaseSecondInformation().getKHYHMC(/*开户银行名称*/));
                loanHousePurchasingVice.setSfrzhhm(body.getHouseInformation().getPurchaseSecondInformation().getGRSKYHZH(/*个人收款银行账号*/));
                loanHousePurchasingVice.setSfryhkhm(body.getHouseInformation().getPurchaseSecondInformation().getYHKHM(/*银行开户名*/));
                loanHousePurchasingVice.setSfk(StringUtil.safeBigDecimal(body.getHouseInformation().getPurchaseSecondInformation().getYFK(/*已付款（全部付完）*/)));
                //endregion
            }


            loanHousePurchasingVice.setSfwesf(StringUtil.safeBigDecimal(body.getHouseInformation().getSFWESF(/*是否为二手房*/)).intValue() == 1);

            if (body.getHouseInformation().getSFWESF(/*是否为二手房*/).equals("0")) {

                //region //非二手房
                loanHousePurchasingVice.setFwjzmj(StringUtil.safeBigDecimal(body.getHouseInformation().getPurchaseFirstInformation().getFWJZMJ(/*房屋建筑面积*/)));
                loanHousePurchasingVice.setFwtnmj(StringUtil.safeBigDecimal(body.getHouseInformation().getPurchaseFirstInformation().getFWTNMJ(/*房屋套内面积*/)));
                loanHousePurchasingVice.setFwxs(body.getHouseInformation().getPurchaseFirstInformation().getFWXS(/*房屋形式（0：在建房  1：现房）*/));
                loanHousePurchasingVice.setFwxz(body.getHouseInformation().getPurchaseFirstInformation().getFWXZ(/*房屋性质（0：商品房 1：障碍性住房 2：自建房 3：两限房 4：集资房 5：危改房 6：经济适用房 7：房改房 8：其他）*/));
                loanHousePurchasingVice.setFwzj(StringUtil.safeBigDecimal(body.getHouseInformation().getPurchaseFirstInformation().getFWZJ(/*房屋总价*/)));
                loanHousePurchasingVice.setFwjgrq(DateUtil.safeStr2Date(format, body.getHouseInformation().getPurchaseFirstInformation().getFWJGRQ(/*房屋竣工日期*/)));
                loanHousePurchasingVice.setSfrkhyhmc(body.getHouseInformation().getPurchaseFirstInformation().getSFRKHYHMC(/*售房人开户银行名称*/));
                loanHousePurchasingVice.setFwjg(body.getHouseInformation().getPurchaseFirstInformation().getFWJG(/*房屋结构（0：框架 1：砖混 2：其他）*/));
                loanHousePurchasingVice.setHtje(StringUtil.safeBigDecimal(body.getHouseInformation().getPurchaseFirstInformation().getHTJE(/*合同金额*/)));
                loanHousePurchasingVice.setSfrzhhm(body.getHouseInformation().getPurchaseFirstInformation().getSFRZHHM(/*售房人账户号码*/));
                loanHousePurchasingVice.setLpmc(body.getHouseInformation().getPurchaseFirstInformation().getLPMC(/*楼盘名称*/));
                loanHousePurchasingVice.setGfhtbh(body.getHouseInformation().getPurchaseFirstInformation().getGFHTBH(/*购房合同编号*/));
                loanHousePurchasingVice.setBlzl(body.getHouseInformation().getPurchaseFirstInformation().getSCZL(/*办理资料*/));
                loanHousePurchasingVice.setSfk(StringUtil.safeBigDecimal(body.getHouseInformation().getPurchaseFirstInformation().getSFK(/*首付款*/)));
                loanHousePurchasingVice.setLxfs(body.getHouseInformation().getPurchaseFirstInformation().getLXFS()); //联系方式
                loanHousePurchasingVice.setSfrmc(body.getHouseInformation().getPurchaseFirstInformation().getSFRMC());//售房人名称
                loanHousePurchasingVice.setFwzl(body.getHouseInformation().getPurchaseFirstInformation().getFWZL());//房屋坐落
                loanHousePurchasingVice.setDanJia(StringUtil.safeBigDecimal(body.getHouseInformation().getPurchaseFirstInformation().getDanJia()));//单价
                loanHousePurchasingVice.setSfryhkhm(body.getHouseInformation().getPurchaseFirstInformation().getSFRYHKHM()/*售房人银行开户名*/);
                loanHousePurchasingVice.setSpfysxkbh(body.getHouseInformation().getPurchaseFirstInformation().getSPFYSXKBH(/*商品房预售许可编号*/));
                //endregion
            }


            //endregion

            loanHousingBusinessProcess.setLoanHouseBuildVice(null);
            loanHousingBusinessProcess.setLoanHouseOverhaulVice(null);

            loanHouseBuildVice.setGrywmx(null);
            loanHouseOverhaulVice.setGrywmx(null);


        }

        if (body.getHouseInformation().getDKYT(/*贷款用途*/).equals("1")) {

            //region//自建翻修

            loanHouseBuildVice.setGrskyhzh(body.getHouseInformation().getBuildInformation().getGRSKYHZH(/*个人收款银行账号*/));
            loanHouseBuildVice.setJhjgrq(DateUtil.safeStr2Date(format, body.getHouseInformation().getBuildInformation().getJHJGRQ(/*计划竣工日期*/)));
            loanHouseBuildVice.setFwxz(body.getHouseInformation().getBuildInformation().getFWXZ(/*房屋性质（0：商品房 1：障碍性住房 2：自建房 3：两限房 4：集资房 5：危改房 6：经济适用房 7：房改房 8：其他）*/));
            loanHouseBuildVice.setFwjg(body.getHouseInformation().getBuildInformation().getFWJG(/*房屋结构（0：框架 1：砖混 2：其他）*/));
            loanHouseBuildVice.setJhjgrq(DateUtil.safeStr2Date(format, body.getHouseInformation().getBuildInformation().getJHJGRQ(/*计划竣工日期*/)));
            loanHouseBuildVice.setFwzl(body.getHouseInformation().getBuildInformation().getFWZL(/*房屋坐落地址*/));
            loanHouseBuildVice.setTdsyzh(body.getHouseInformation().getBuildInformation().getTDSYZH(/*土地使用证号*/));
            loanHouseBuildVice.setGrsyzj(StringUtil.safeBigDecimal(body.getHouseInformation().getBuildInformation().getGRSYZJ(/*个人使用资金*/)));
            loanHouseBuildVice.setJzcs(body.getHouseInformation().getBuildInformation().getJZCS(/*建造层数*/));
            loanHouseBuildVice.setYhkhm(body.getHouseInformation().getBuildInformation().getYHKHM(/*银行开户名*/));
            loanHouseBuildVice.setJzydghxkzh(body.getHouseInformation().getBuildInformation().getJZYDGHXKZH(/*建造用地规划许可证号*/));
            loanHouseBuildVice.setPzjgwh(body.getHouseInformation().getBuildInformation().getPZJGWH(/*批准机关文号*/));
            loanHouseBuildVice.setJhjzfy(StringUtil.safeBigDecimal(body.getHouseInformation().getBuildInformation().getJHJZFY(/*计划建造费用*/)));
            loanHouseBuildVice.setKhhyhmc(body.getHouseInformation().getBuildInformation().getKHYHMC(/*开户行银行名称*/));
            loanHouseBuildVice.setBlzl(body.getHouseInformation().getBuildInformation().getSCZL(/* 办理资料*/));
            loanHouseBuildVice.setJzgcghxkzh(body.getHouseInformation().getBuildInformation().getJZGCGHXKZH(/*建造工程规划许可证号*/));
            loanHouseBuildVice.setJchjzmj(StringUtil.safeBigDecimal(body.getHouseInformation().getBuildInformation().getJCHJZMJ(/*建成后建造面积*/)));
            loanHouseBuildVice.setJhkgrq(DateUtil.safeStr2Date(format, body.getHouseInformation().getBuildInformation().getJHKGRQ(/*计划开工日期*/)));
            loanHouseBuildVice.setJsgcsgxkzh(body.getHouseInformation().getBuildInformation().getJSGCSGXKZH()/*建设工程施工许可证号*/);
            //endregion

            loanHousingBusinessProcess.setLoanHousePurchasingVice(null);
            loanHousingBusinessProcess.setLoanHouseOverhaulVice(null);

            loanHousePurchasingVice.setGrywmx(null);
            loanHouseOverhaulVice.setGrywmx(null);

        }

        if (body.getHouseInformation().getDKYT(/*贷款用途*/).equals("2")) {

            // region//大修

            loanHouseOverhaulVice.setGrskyhzh(body.getHouseInformation().getOverhaulInformation().getGRSKYHZH(/*个人收款银行账号*/));
            loanHouseOverhaulVice.setBlzl(body.getHouseInformation().getOverhaulInformation().getSCZL(/*资料*/));
            loanHouseOverhaulVice.setFwzl(body.getHouseInformation().getOverhaulInformation().getFWZL(/*房屋坐落地址*/));
            loanHouseOverhaulVice.setJhkgrq(DateUtil.safeStr2Date(format, body.getHouseInformation().getOverhaulInformation().getJHKGRQ(/*计划开工日期*/)));
            loanHouseOverhaulVice.setYhkhm(body.getHouseInformation().getOverhaulInformation().getYHKHM(/*银行开户名*/));
            loanHouseOverhaulVice.setYbdczh(body.getHouseInformation().getOverhaulInformation().getYBDCZH());//原不动产证号
            loanHouseOverhaulVice.setDxgcys(StringUtil.safeBigDecimal(body.getHouseInformation().getOverhaulInformation().getDXGCYS()));
            loanHouseOverhaulVice.setYjzmj(StringUtil.safeBigDecimal(body.getHouseInformation().getOverhaulInformation().getYJZMJ()));
            loanHouseOverhaulVice.setTdsyzh(body.getHouseInformation().getOverhaulInformation().getFXHCS());
            loanHouseOverhaulVice.setJhjgrq(DateUtil.safeStr2Date(format, body.getHouseInformation().getOverhaulInformation().getJHJGRQ()));
            loanHouseOverhaulVice.setFwzjbgjgmcjbh(body.getHouseInformation().getOverhaulInformation().getFWZJBGJGMCJBH());
            loanHouseOverhaulVice.setKhyhmc(body.getHouseInformation().getOverhaulInformation().getKHYHMC());
            //endregion

            loanHousingBusinessProcess.setLoanHousePurchasingVice(null);
            loanHousingBusinessProcess.setLoanHouseBuildVice(null);

            loanHousePurchasingVice.setGrywmx(null);
            loanHouseBuildVice.setGrywmx(null);


        }

        //endregion

        CAccountNetwork network = DAOBuilder.instance(cAccountNetworkDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("id",tokenContext.getUserInfo().getYWWD());

        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) { throw new ErrorException(e); }
        });
        loanHousingBusinessProcess.setYwwd(network);

        loanHousingBusinessProcess.setBlzl(body.getSCZL(/* 办理资料*/));

        loanHousingBusinessProcess.setCzy(tokenContext.getUserInfo().getCZY()/*操作员*/);

        //region//担保信息
        BigDecimal dywjz = BigDecimal.ZERO;
        loanHousingGuaranteeContractVice.setBlzl(body.getCollateralInformation().getSCZL(/* 办理资料*/));

        loanHousingBusinessProcess.setDkdblx(body.getCollateralInformation().getDKDBLX(/*贷款担保类型*/));

        if (body.getCollateralInformation().getDKDBLX(/*贷款担保类型*/).equals(LoanGuaranteeType.抵押.getCode())) {

            //region //抵押

            list_loanGuaranteeMortgageVice.clear();

            for (ApplicantPostCollateralInformationMortgageInformation applicantPostCollateralInformationMortgageInformation : body.getCollateralInformation().getMortgageInformation()) {

                CLoanGuaranteeMortgageVice loanGuaranteeMortgageVice = new CLoanGuaranteeMortgageVice();

                dywjz = dywjz.add(StringUtil.safeBigDecimal(applicantPostCollateralInformationMortgageInformation.getDYWPGJZ(/*抵押物评估价值*/)));

                loanGuaranteeMortgageVice.setDywsyqrsfzhm(applicantPostCollateralInformationMortgageInformation.getDYWSYQRSFZHM(/*抵押物所有权人身份证号码*/));
                loanGuaranteeMortgageVice.setDyfwxs(applicantPostCollateralInformationMortgageInformation.getDYFWXS(/*抵押房屋形式（0：住宅 （期房） 1：住宅（现房） 2：商铺 3：其他）*/));
                loanGuaranteeMortgageVice.setDywfwzl(applicantPostCollateralInformationMortgageInformation.getDYWFWZL(/*抵押物房屋坐落*/));
                loanGuaranteeMortgageVice.setDywmc(applicantPostCollateralInformationMortgageInformation.getDYWMC(/*抵押物名称*/));
                loanGuaranteeMortgageVice.setFwjg(applicantPostCollateralInformationMortgageInformation.getFWJG(/*房屋结构（0：框架 1：砖混 2：土木 3：其他）*/));
                loanGuaranteeMortgageVice.setDywgyqrlxdh(applicantPostCollateralInformationMortgageInformation.getDYWGYQRLXDH(/*抵押物共有权人联系电话*/));
                loanGuaranteeMortgageVice.setDywpgjz(StringUtil.safeBigDecimal(applicantPostCollateralInformationMortgageInformation.getDYWPGJZ(/*抵押物评估价值*/)));
                loanGuaranteeMortgageVice.setDywgyqrsfzhm(applicantPostCollateralInformationMortgageInformation.getDYWGYQRSFZHM(/*抵押物共有权人身份证号码*/));
                loanGuaranteeMortgageVice.setDywsyqrxm(applicantPostCollateralInformationMortgageInformation.getDYWSYQRXM(/*抵押物所有权人姓名*/));
                loanGuaranteeMortgageVice.setDywsyqrlxdh(applicantPostCollateralInformationMortgageInformation.getDYWSYQRLXDH(/*抵押物所有权人联系电话*/));
                loanGuaranteeMortgageVice.setFwmj(StringUtil.safeBigDecimal(applicantPostCollateralInformationMortgageInformation.getFWMJ(/*房屋面积*/)));
                loanGuaranteeMortgageVice.setQszsbh(applicantPostCollateralInformationMortgageInformation.getQSZSBH(/*权属证书编号*/));
                loanGuaranteeMortgageVice.setDywgyqrxm(applicantPostCollateralInformationMortgageInformation.getDYWGYQRXM(/*抵押物共有权人姓名*/));

                list_loanGuaranteeMortgageVice.add(loanGuaranteeMortgageVice);
            }
            loanHousingGuaranteeContractVice.setBlzl(body.getCollateralInformation().getSCZL(/* 办理资料*/));

            //endregion


            loanHousingGuaranteeContractVice.setGuaranteeVices(null);
            loanHousingGuaranteeContractVice.setGuaranteePledgeVices(null);


        }

        if (body.getCollateralInformation().getDKDBLX(/*贷款担保类型*/).equals(LoanGuaranteeType.质押.getCode())) {

            //region //质押

            list_loanGuaranteePledgeVice.clear();
            for (ApplicantPostCollateralInformationPledgeInformation applicantPostCollateralInformationPledgeInformation : body.getCollateralInformation().getPledgeInformation()) {

                CLoanGuaranteePledgeVice loanGuaranteePledgeVice = new CLoanGuaranteePledgeVice();

                dywjz = dywjz.add(StringUtil.safeBigDecimal(applicantPostCollateralInformationPledgeInformation.getZYWJZ(/*质押物价值*/)));

                loanGuaranteePledgeVice.setZywsyqrsfzhm(applicantPostCollateralInformationPledgeInformation.getZYWSYQRSFZHM(/*质押物所有权人身份证号码*/));
                loanGuaranteePledgeVice.setZywsyqrxm(applicantPostCollateralInformationPledgeInformation.getZYWSYQRXM(/*质押物所有权人姓名*/));
                loanGuaranteePledgeVice.setZywjz(StringUtil.safeBigDecimal(applicantPostCollateralInformationPledgeInformation.getZYWJZ(/*质押物价值*/)));
                loanGuaranteePledgeVice.setZywsyqrlxdh(applicantPostCollateralInformationPledgeInformation.getZYWSYQRLXDH(/*质押物所有权人联系电话*/));
                loanGuaranteePledgeVice.setZywmc(applicantPostCollateralInformationPledgeInformation.getZYWMC(/*质押物名称*/));

                list_loanGuaranteePledgeVice.add(loanGuaranteePledgeVice);
            }

            loanHousingGuaranteeContractVice.setBlzl(body.getCollateralInformation().getSCZL(/* 办理资料*/));

            //endregion


            loanHousingGuaranteeContractVice.setGuaranteeMortgageVices(null);
            loanHousingGuaranteeContractVice.setGuaranteeVices(null);

        }

        if (body.getCollateralInformation().getDKDBLX(/*贷款担保类型*/).equals(LoanGuaranteeType.保证.getCode())) {

            //region//保证

            list_loanGuaranteeVice.clear();
            for (ApplicantPostCollateralInformationGuaranteeInformation applicantPostCollateralInformationGuaranteeInformation : body.getCollateralInformation().getGuaranteeInformation()) {

                CLoanGuaranteeVice loanGuaranteeVice = new CLoanGuaranteeVice();

                loanGuaranteeVice.setYzbm(applicantPostCollateralInformationGuaranteeInformation.getYZBM(/*邮政编码*/));
                loanGuaranteeVice.setFrdbxjzdz(applicantPostCollateralInformationGuaranteeInformation.getFRDBXJZDZ(/*保证人现居住地址*/));
                loanGuaranteeVice.setFrdbsfzhm(applicantPostCollateralInformationGuaranteeInformation.getFRDBSFZHM(/*保证人身份证号码*/));
                loanGuaranteeVice.setFrdbxm(applicantPostCollateralInformationGuaranteeInformation.getFRDBXM(/*保证人姓名*/));
                loanGuaranteeVice.setFrdblxdh(applicantPostCollateralInformationGuaranteeInformation.getFRDBLXDH(/*保证人联系电话*/));
                loanGuaranteeVice.setTxdz(applicantPostCollateralInformationGuaranteeInformation.getTXDZ(/*通讯地址*/));
                loanGuaranteeVice.setBzflx(applicantPostCollateralInformationGuaranteeInformation.getBZFLX(/*保证方类型（0：个人 1：机构）*/));

                list_loanGuaranteeVice.add(loanGuaranteeVice);
            }
            dywjz = StringUtil.safeBigDecimal(body.getCapitalInformation().getHTDKJE(/*合同贷款金额*/));
            loanHousingGuaranteeContractVice.setBlzl(body.getCollateralInformation().getSCZL(/* 办理资料*/));

            //endregion

            loanHousingGuaranteeContractVice.setGuaranteeMortgageVices(null);
            loanHousingGuaranteeContractVice.setGuaranteePledgeVices(null);

        }

        //endregion

        //region//资金信息
        loanFundsVice.setHkfs(body.getCapitalInformation().getHKFS(/*还款方式（0：等额本息 1：等额本金 2：一次还款付息 3：自由还款方式 4：其他）*/));
        loanFundsVice.setHtdkje(StringUtil.safeBigDecimal(body.getCapitalInformation().getHTDKJE(/*合同贷款金额*/)));
        loanFundsVice.setLlfsbl(StringUtil.safeBigDecimal(body.getCapitalInformation().getLLFSBL(/*利率浮动比例*/)));
        loanFundsVice.setHtdkjedx(body.getCapitalInformation().getHTDKJEDX(/*合同贷款金额大写*/));
        loanFundsVice.setDklx(body.getCapitalInformation().getDKLX(/*贷款类型（0：公积金贷款 1：组合贷款 2：贴息贷款 3：其他）*/));
        loanFundsVice.setJkhtll(StringUtil.safeBigDecimal(body.getCapitalInformation().getJKHTLL(/*借款合同利率*/)));
        loanFundsVice.setDkqs(StringUtil.safeBigDecimal(body.getCapitalInformation().getDKQS(/*贷款期数*/)));
        loanFundsVice.setDkdblx(body.getCapitalInformation().getDKDBLX(/*贷款担保类型  （0：抵押 1：质押 2：保证 3：其他）*/));
        loanFundsVice.setWtkhyjce(StringUtil.safeBigDecimal(body.getCapitalInformation().getWTKHYJCE(/*委托划扣月缴存额*/)).intValue() == 1);
        //loanFundsVice.setZxll(StringUtil.safeBigDecimal(body.getCapitalInformation().getZXLL(/*执行利率*/)));
        loanFundsVice.setFwts(body.getCapitalInformation().getFWTS(/*房屋套数（0：一套 1：二套 2：三套 3：四套  5：五套及以上）*/));

        loanHousingBusinessProcess.setCzy(tokenContext.getUserInfo().getCZY()/*操作员*/);

        //endregion

        //endregion

        //region //贷款账号状态验证

        if (("1".equals(status) && !DAOBuilder.instance(loanHousingPersonInformationBasicDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("jkrzjhm", body.getApplicantInformation().getInformation().getBorrowerInformation().getJKRZJHM());

        }}).extension(new IBaseDAO.CriteriaExtension() {

            @Override
            public void extend(Criteria criteria) {

                criteria.add(Restrictions.ne("dkzhzt", LoanAccountType.已结清.getCode()));
            }
        }).isUnique(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e); }

        }))){

            throw new ErrorException(ReturnEnumeration.Business_In_Process, "存在未结清的贷款 不能提交");
        }

        //endregion

        //region  //其他
        CLoanHousingBusinessProcess savedProcess = DAOBuilder.instance(this.loanHousingBusinessProcessDAO).entity(loanHousingBusinessProcess).saveThenFetchObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e); }
        });

        this.iSaveAuditHistory.saveNormalBusiness(savedProcess.getYwlsh(),tokenContext,LoanBusinessType.贷款发放.getName(),"新建");

        if (status.equals("1")) {

            if(!hasCoborrower){  loanHousingBusinessProcess.setLoanHousingCoborrowerVice(null); }

            this.checkApplicationAvailable(loanHousingBusinessProcess);

            if(dywjz.compareTo(loanFundsVice.getHtdkje())<0){ throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"抵押物价值过小");}

            String id = this.iPdfService.getReviewTable(ResUtils.noneAdductionValue(GetApplicantResponse.class, this.getApplyDetails(tokenContext, savedProcess.getYwlsh())));



            id = iPdfService.putReviewTable(id, new ArrayList<>(Collections.singleton(ResUtils.noneAdductionValue(Review.class, new Review() {{

                this.setAction("新建"/*操作*/);

                this.setCZY(tokenContext.getUserInfo().getCZY()/*操作员及职务*/);

                this.setYWWD(loanHousingBusinessProcess.getYwwd().getMingCheng()/*业务网点*/);

                this.setCZQD(tokenContext.getChannel()/*操作渠道*/);

                this.setSLRYJ("同意受理"/*受理人意见*/);

                this.setSLR(loanHousingBusinessProcess.getCzy()/*受理人*/);

                this.setSPSJ(DateUtil.date2Str(loanHousingBusinessProcess.getCreated_at(), "yyyy-MM-dd HH:mm") /*审批时间*/);

                this.setType(0/*审批级别（0：新建，1：一级审批，2：二级审批，3：三级审批）*/);

            }}))));

            if (id != null && savedProcess.getLoanHousingPersonInformationVice() != null) {

                savedProcess.getLoanHousingPersonInformationVice().setSpbwj(id);
            }

            DAOBuilder.instance(this.loanHousingBusinessProcessDAO).entity(savedProcess).save(new DAOBuilder.ErrorHandler() {

                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });

        }

        //endregion

        //region //修改状态

        StateMachineUtils.updateState(this.iStateMachineService, new HashMap<String, String>() {{

            this.put("0", Events.通过.getEvent());
            this.put("1", Events.提交.getEvent());

        }}.get(status), new TaskEntity() {{

            this.setStatus(savedProcess.getStep() == null ? "初始状态" : savedProcess.getStep());
            this.setTaskId(savedProcess.getYwlsh());
            this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));;
            this.setNote("");
            this.setSubtype(BusinessSubType.贷款_个人贷款申请.getSubType());
            this.setType(com.handge.housingfund.database.enums.BusinessType.Loan);
            this.setOperator(savedProcess.getCzy());
            this.setWorkstation(savedProcess.getYwwd().getId());

        }}, new StateMachineUtils.StateChangeHandler() {

            @Override
            public void onStateChange(boolean succeed, String next, Exception e) {

                if (e != null) {
                    throw new ErrorException(e);
                }

                if (!succeed || next == null || e != null) {
                    return;
                }

                savedProcess.setStep(next);


                if (StringUtil.isIntoReview(next, null)) {

                    savedProcess.setDdsj(new Date());

                }
                if (LoanBussinessStatus.待签合同.getName().equals(next)) {

                    loanReviewService.postLoanReviewReason(tokenContext, savedProcess.getYwlsh());

                    savedProcess.setStep(LoanBussinessStatus.待签合同.getName());
                }
                DAOBuilder.instance(loanHousingBusinessProcessDAO).entity(savedProcess).save(new DAOBuilder.ErrorHandler() {

                    @Override

                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });
            }
        });
        //endregion

        //region //在途验证

        if (("1".equals(status) && !DAOBuilder.instance(loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("loanHousingPersonInformationVice.jkrzjhm", body.getApplicantInformation().getInformation().getBorrowerInformation().getJKRZJHM());

        }}).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {

                criteria.add(Restrictions.not(Restrictions.in(CriteriaUtils.addAlias(criteria, "step"), (Collection) CollectionUtils.flatmap(CollectionUtils.merge(CollectionBusinessStatus.办结.getSubTypes(), CollectionBusinessStatus.新建.getSubTypes(),CollectionBusinessStatus.已作废.getSubTypes()), new CollectionUtils.Transformer<CollectionBusinessStatus, String>() {

                    @Override
                    public String tansform(CollectionBusinessStatus var1) {
                        return var1.getName();
                    }

                }))));
                criteria.add(Restrictions.ne("ywlsh", savedProcess.getYwlsh() + ""));
                criteria.add(Restrictions.eq("cznr", LoanBusinessType.贷款发放.getCode()));

            }
        }).isUnique(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }

        }))) {

            throw new ErrorException(ReturnEnumeration.Business_In_Process);
        }

        //endregion

        return new CommonResponses() {{

            this.setId(savedProcess.getYwlsh());

            this.setState("success");
        }};
    }


    /*
    *  completed
    *
    * !逻辑已完成
    *
    * 已测试 lian
    *
    * !存在问题
    * ~swagger未分页
    * */
    @Override
    public PageRes<LoanListResponse> getApplyLoanList(TokenContext tokenContext,String YWWD, String JKRXM, String JKRZJHM, String status, String SKYHDM,String pageSize, String page,String KSSJ,String JSSJ) {


        PageRes pageRes = new PageRes();

        int page_number = 0;

        int pagesize_number = 0;

        try {

            if (page != null) {
                page_number = Integer.parseInt(page);
            }

            if (pageSize != null) {
                pagesize_number = Integer.parseInt(pageSize);
            }

        } catch (Exception e) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "页码");
        }

        List<CLoanHousingBusinessProcess> list_process = DAOBuilder.instance(this.loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{

            if (StringUtil.notEmpty(JKRXM)) {
                this.put("loanHousingPersonInformationVice.jkrxm", JKRXM);
            }

            if (StringUtil.notEmpty(JKRZJHM)) {
                this.put("loanHousingPersonInformationVice.jkrzjhm", JKRZJHM);
            }

        }}).searchOption(SearchOption.FUZZY).extension(new IBaseDAO.CriteriaExtension() {

            @Override
            public void extend(Criteria criteria) {

                if (DateUtil.isFollowFormat(KSSJ,formatNYRSF,false)){ criteria.add(Restrictions.ge("created_at",DateUtil.safeStr2Date(formatNYRSF,KSSJ)));}
                if (DateUtil.isFollowFormat(JSSJ,formatNYRSF,false)){ criteria.add(Restrictions.lt("created_at",DateUtil.safeStr2Date(formatNYRSF,JSSJ)));}

                if (StringUtil.notEmpty(status) && !LoanBussinessStatus.所有.getName().equals(status) && !LoanBussinessStatus.待审核.getName().equals(status)) {
                    criteria.add(Restrictions.eq("step", status));
                }

                criteria.add(Restrictions.eq("cznr", LoanBusinessType.贷款发放.getCode()));

                criteria.add(Restrictions.or(Restrictions.in("step", (Collection) Arrays.asList(LoanBussinessStatus.新建.getName(), LoanBussinessStatus.待审核.getName(), LoanBussinessStatus.审核不通过.getName())), Restrictions.like("step", LoanBussinessStatus.待某人审核.getName())));

                if (LoanBussinessStatus.待审核.getName().equals(status)) {

                    criteria.add(Restrictions.like("step", LoanBussinessStatus.待某人审核.getName()));

                }

                if(StringUtil.notEmpty(SKYHDM)){

                    List<String> list_bankInfo = CollectionUtils.flatmap(DAOBuilder.instance(bankBankInfoDAO).searchFilter(new HashMap<String, Object>() {{

                        this.put("code", SKYHDM);

                    }}).getList(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) { throw new ErrorException(e); }

                    }), new CollectionUtils.Transformer<CBankBankInfo, String>() {
                        @Override
                        public String tansform(CBankBankInfo var1) { return var1.getBank_name(); }
                    });

                    criteria.createAlias("loanHousePurchasingVice","loanHousePurchasingVice", JoinType.LEFT_OUTER_JOIN);
                    criteria.createAlias("loanHouseBuildVice","loanHouseBuildVice", JoinType.LEFT_OUTER_JOIN);
                    criteria.createAlias("loanHouseOverhaulVice","loanHouseOverhaulVice", JoinType.LEFT_OUTER_JOIN);
                    criteria.add(Restrictions.or(
                            Restrictions.and(Restrictions.isNotNull("loanHousePurchasingVice"),Restrictions.in("loanHousePurchasingVice.sfrkhyhmc",(Collection)list_bankInfo)),
                            Restrictions.and(Restrictions.isNotNull("loanHouseBuildVice"),Restrictions.in("loanHouseBuildVice.khhyhmc",(Collection)list_bankInfo)),
                            Restrictions.and(Restrictions.isNotNull("loanHouseOverhaulVice"),Restrictions.in("loanHouseOverhaulVice.khyhmc",(Collection)list_bankInfo))
                    ));
                }
                if(StringUtil.notEmpty(YWWD)){

                    criteria.createAlias("ywwd","ywwd");
                    criteria.add(Restrictions.eq("ywwd.id",YWWD));
                }
            }
        }).pageOption(pageRes, pagesize_number, page_number).getList(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }

        });

        return new PageRes<LoanListResponse>() {{

            this.setResults(new ArrayList<LoanListResponse>() {{

                for (CLoanHousingBusinessProcess process : list_process) {

                    this.add(new LoanListResponse() {{

                        this.setId(process.getId());
                        this.setYWLSH(process.getYwlsh()/*业务流水号*/);

                        this.setZhuangTai(process.getStep()/*状态*/);
                        this.setYWWD(process.getYwwd().getMingCheng()/*业务网点*/);

                        this.setDKYT(process.getDkyt()/*贷款用途*/);

                        this.setCZY(process.getCzy()/*操作员*/);

                        this.setSLSJ(DateUtil.date2Str(process.getCreated_at(), formatM));

                        if (process.getLoanHousingPersonInformationVice() != null) {

                            this.setJKRXM(process.getLoanHousingPersonInformationVice().getJkrxm()/*借款人姓名*/);
                            this.setJKRZJHM(process.getLoanHousingPersonInformationVice().getJkrzjhm()/*借款人证件号码*/);
                        }

                        if (process.getLoanFundsVice() != null) {
                            this.setHTDKJE(process.getLoanFundsVice().getHtdkje() + ""/*合同贷款金额*/);
                            this.setDKQS(process.getLoanFundsVice().getDkqs() + ""/*贷款期数*/);
                        }
                    }});
                }

            }});

            this.setCurrentPage(pageRes.getCurrentPage());

            this.setNextPageNo(pageRes.getNextPageNo());

            this.setPageCount(pageRes.getPageCount());

            this.setTotalCount(pageRes.getTotalCount());

            this.setPageSize(pageRes.getPageSize());
        }};

    }

    @Override
    public PageResNew<LoanListResponse> getApplyLoanList(TokenContext tokenContext,String YWWD, String JKRXM, String JKRZJHM, String status, String SKYHDM, String pageSize, String marker, String action, String KSSJ, String JSSJ) {

        int pagesize_number = 0;

        try {

            if (pageSize != null) { pagesize_number = Integer.parseInt(pageSize); }

        } catch (Exception e) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "页码");
        }

        List<CLoanHousingBusinessProcess> list_process = DAOBuilder.instance(this.loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{

            if (StringUtil.notEmpty(JKRXM)) {
                this.put("loanHousingPersonInformationVice.jkrxm", JKRXM);
            }

            if (StringUtil.notEmpty(JKRZJHM)) {
                this.put("loanHousingPersonInformationVice.jkrzjhm", JKRZJHM);
            }

        }}).searchOption(SearchOption.FUZZY).extension(new IBaseDAO.CriteriaExtension() {

            @Override
            public void extend(Criteria criteria) {

                if (DateUtil.isFollowFormat(KSSJ,formatNYRSF,false)){ criteria.add(Restrictions.ge("created_at",DateUtil.safeStr2Date(formatNYRSF,KSSJ)));}
                if (DateUtil.isFollowFormat(JSSJ,formatNYRSF,false)){ criteria.add(Restrictions.lt("created_at",DateUtil.safeStr2Date(formatNYRSF,JSSJ)));}

                if (StringUtil.notEmpty(status) && !LoanBussinessStatus.所有.getName().equals(status) && !LoanBussinessStatus.待审核.getName().equals(status)) {
                    criteria.add(Restrictions.eq("step", status));
                }

                criteria.add(Restrictions.eq("cznr", LoanBusinessType.贷款发放.getCode()));

                criteria.add(Restrictions.or(Restrictions.in("step", (Collection) Arrays.asList(LoanBussinessStatus.新建.getName(), LoanBussinessStatus.待审核.getName(), LoanBussinessStatus.审核不通过.getName())), Restrictions.like("step", LoanBussinessStatus.待某人审核.getName())));

                if (LoanBussinessStatus.待审核.getName().equals(status)) {

                    criteria.add(Restrictions.like("step", LoanBussinessStatus.待某人审核.getName()));

                }

                if(!"1".equals(tokenContext.getUserInfo().getYWWD())) {
                    criteria.createAlias("ywwd", "ywwd");
                    criteria.add(Restrictions.eq("ywwd.id", tokenContext.getUserInfo().getYWWD()));
                }
            }

        }).pageOption(marker,action, pagesize_number).getList(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e); }

        });

        return new PageResNew<LoanListResponse>() {{

            this.setResults(action,new ArrayList<LoanListResponse>() {{

                for (CLoanHousingBusinessProcess process : list_process) {

                    this.add(new LoanListResponse() {{

                        this.setId(process.getId());

                        this.setYWLSH(process.getYwlsh()/*业务流水号*/);

                        this.setZhuangTai(process.getStep()/*状态*/);
                        this.setYWWD(process.getYwwd().getMingCheng()/*业务网点*/);

                        this.setDKYT(process.getDkyt()/*贷款用途*/);

                        this.setCZY(process.getCzy()/*操作员*/);

                        this.setSLSJ(DateUtil.date2Str(process.getCreated_at(), formatM));

                        if (process.getLoanHousingPersonInformationVice() != null) {

                            this.setJKRXM(process.getLoanHousingPersonInformationVice().getJkrxm()/*借款人姓名*/);
                            this.setJKRZJHM(process.getLoanHousingPersonInformationVice().getJkrzjhm()/*借款人证件号码*/);
                        }

                        if (process.getLoanFundsVice() != null) {
                            this.setHTDKJE(process.getLoanFundsVice().getHtdkje() + ""/*合同贷款金额*/);
                            this.setDKQS(process.getLoanFundsVice().getDkqs() + ""/*贷款期数*/);
                        }
                    }});
                }

            }});

        }};
    }


    /*
    *  completed
    *
    * !逻辑已完成
    *
    * !存在问题
    * ~状态字段
    * ~不应该使用id 应该使用业务流水号
    * */
    @Override
    public CommonResponses putApplyLoanStatus(TokenContext tokenContext, String YWLSH, String status) {

        //region //查询必要字段

        CLoanHousingBusinessProcess loanHousingBusinessProcess = DAOBuilder.instance(this.loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("ywlsh", YWLSH);

            this.put("cznr", LoanBusinessType.贷款发放.getCode());

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        //endregion

        //region //数据完整性验证

        if (loanHousingBusinessProcess == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }

        if (loanHousingBusinessProcess.getLoanHousingPersonInformationVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }

        if (loanHousingBusinessProcess.getLoanHousePurchasingVice() == null && loanHousingBusinessProcess.getLoanHouseBuildVice() == null && loanHousingBusinessProcess.getLoanHouseOverhaulVice() == null) {

            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }

        if (loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }

        if (loanHousingBusinessProcess.getLoanFundsVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }

        //endregion

        //region //修改状态

        StateMachineUtils.updateState(this.iStateMachineService, new HashMap<String, String>() {{

            this.put("0", Events.通过.getEvent());
            this.put("1", Events.撤回.getEvent());
            this.put("2", Events.不通过.getEvent());

        }}.get(status), new TaskEntity() {{

            this.setStatus(loanHousingBusinessProcess.getStep());
            this.setTaskId(loanHousingBusinessProcess.getYwlsh());
            this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));;
            this.setNote("");
            this.setSubtype(BusinessSubType.贷款_个人贷款申请.getSubType());
            this.setType(BusinessType.Loan);
            this.setOperator(tokenContext.getUserInfo().getCZY()/*操作员*/);
            this.setWorkstation(tokenContext.getUserInfo().getYWWD(/*业务网点*/));

        }}, new StateMachineUtils.StateChangeHandler() {

            @Override
            public void onStateChange(boolean succeed, String next, Exception e) {

                if (e != null) {
                    throw new ErrorException(e);
                }

                if (!succeed || next == null || e != null) {
                    return;
                }

                loanHousingBusinessProcess.setStep(next);


                if (StringUtil.isIntoReview(next, null)) {

                    loanHousingBusinessProcess.setDdsj(new Date());

                }

                CLoanHousingBusinessProcess savedProcess = DAOBuilder.instance(loanHousingBusinessProcessDAO).entity(loanHousingBusinessProcess).saveThenFetchObject(new DAOBuilder.ErrorHandler() {

                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });

                //region  //其他
                if (status.equals("0")) {

                    String id = iPdfService.getReviewTable(ResUtils.noneAdductionValue(GetApplicantResponse.class, getApplyDetails(tokenContext, savedProcess.getYwlsh())));


                    iSaveAuditHistory.saveNormalBusiness(savedProcess.getYwlsh(),tokenContext,LoanBusinessType.贷款发放.getName(),"修改");


                    id = iPdfService.putReviewTable(id, new ArrayList<>(Collections.singleton(ResUtils.noneAdductionValue(Review.class, new Review() {{

                        this.setAction(null/*操作*/);

                        this.setCZY(null/*操作员及职务*/);

                        this.setYWWD(loanHousingBusinessProcess.getYwwd().getMingCheng()/*业务网点*/);

                        this.setCZQD(null/*操作渠道*/);

                        this.setSLRYJ(null/*受理人意见*/);

                        this.setSLR(loanHousingBusinessProcess.getCzy()/*受理人*/);

                        this.setSPSJ(DateUtil.date2Str(loanHousingBusinessProcess.getCreated_at(), "yyyy-MM-dd hh:mm:ss") /*审批时间*/);

                        this.setType(0/*审批级别（0：新建，1：一级审批，2：二级审批，3：三级审批）*/);

                    }}))));

                    if (id != null && savedProcess.getLoanHousingPersonInformationVice() != null) {

                        savedProcess.getLoanHousingPersonInformationVice().setSpbwj(id);
                    }

                    DAOBuilder.instance(loanHousingBusinessProcessDAO).entity(savedProcess).save(new DAOBuilder.ErrorHandler() {

                        @Override
                        public void error(Exception e) {
                            throw new ErrorException(e);
                        }
                    });

                }

                //endregion
            }
        });

        //endregion

        return new CommonResponses() {{

            this.setId(loanHousingBusinessProcess.getYwlsh());

            this.setState("success");
        }};
    }


    public void checkApplicationAvailable(CLoanHousingBusinessProcess loanHousingBusinessProcess){

        if(!DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                criteria.add(Restrictions.eq("grzh",loanHousingBusinessProcess.getLoanHousingPersonInformationVice().getJkrgjjzh()));
                criteria.createAlias("cCollectionPersonalBusinessDetailsExtension","cCollectionPersonalBusinessDetailsExtension");
                criteria.add(Restrictions.in("cCollectionPersonalBusinessDetailsExtension.czmc", (Collection) Arrays.asList(CollectionBusinessType.部分提取.getCode(),CollectionBusinessType.销户提取.getCode())));
                criteria.add(Restrictions.not(Restrictions.in("cCollectionPersonalBusinessDetailsExtension.step",CollectionBusinessStatus.新建.getName(),CollectionBusinessStatus.已入账.getName(),CollectionBusinessStatus.已作废.getName())));
            }
        }).isUnique(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e); }

        })){

            throw new ErrorException(ReturnEnumeration.Business_In_Process,"提取业务");
        }

        BigDecimal JCJSZH = loanHousingBusinessProcess.getLoanHousingPersonInformationVice().getGrjcjs().add(loanHousingBusinessProcess.getLoanHousingCoborrowerVice()==null?BigDecimal.ZERO:loanHousingBusinessProcess.getLoanHousingCoborrowerVice().getGrjcjs());

        BigDecimal HKE = new HashMap<String,BigDecimal>(){{

            this.put("01",CommLoanAlgorithm.currentBX(loanHousingBusinessProcess.getLoanFundsVice().getHtdkje(), loanHousingBusinessProcess.getLoanFundsVice().getDkqs()==null?0:loanHousingBusinessProcess.getLoanFundsVice().getDkqs().intValue(), loanHousingBusinessProcess.getLoanFundsVice().getHkfs(), loanHousingBusinessProcess.getLoanFundsVice().getJkhtll(), 1/*当期应还金额*/));
            this.put("02",CommLoanAlgorithm.currentBX(loanHousingBusinessProcess.getLoanFundsVice().getHtdkje(), loanHousingBusinessProcess.getLoanFundsVice().getDkqs()==null?0:loanHousingBusinessProcess.getLoanFundsVice().getDkqs().intValue(), loanHousingBusinessProcess.getLoanFundsVice().getHkfs(), loanHousingBusinessProcess.getLoanFundsVice().getJkhtll(), 1/*当期应还金额*/).subtract(CommLoanAlgorithm.overdueThisPeriodLX(loanHousingBusinessProcess.getLoanFundsVice().getHtdkje(), 1, loanHousingBusinessProcess.getLoanFundsVice().getHkfs(), loanHousingBusinessProcess.getLoanFundsVice().getJkhtll(), loanHousingBusinessProcess.getLoanFundsVice().getDkqs() == null ? 0 :loanHousingBusinessProcess.getLoanFundsVice().getDkqs().intValue())));

        }}.get(loanHousingBusinessProcess.getLoanFundsVice().getHkfs());

        StCommonPolicy commonPolicyHKNLLimit = DAOBuilder.instance(commonPolicyDAO).searchFilter(new HashMap<String, Object>(){{

            this.put("id","HKNLXS");

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e);}

        });

        if(commonPolicyHKNLLimit == null || commonPolicyHKNLLimit.getcCommonPolicyExtension()==null||commonPolicyHKNLLimit.getcCommonPolicyExtension().getHknlxs()==null){
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"还款能力系数未设置");
        }
        if(HKE.divide(commonPolicyHKNLLimit.getcCommonPolicyExtension().getHknlxs().divide(new BigDecimal("100"),2,RoundingMode.HALF_UP),2,RoundingMode.HALF_UP).compareTo(JCJSZH)>0){

            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"缴存基数总和过小");
        }

        String index = "";

        if(loanHousingBusinessProcess.getLoanHousePurchasingVice()!=null){

            index = loanHousingBusinessProcess.getLoanHousePurchasingVice().getFwxs();

            if(!StringUtil.notEmpty(loanHousingBusinessProcess.getLoanHousePurchasingVice().getFwxs())){

                throw new ErrorException(ReturnEnumeration.Parameter_MISS,"房屋形式");
            }
        }else if(loanHousingBusinessProcess.getLoanHouseBuildVice() != null){
            index = "2";
        }else if(loanHousingBusinessProcess.getLoanHouseOverhaulVice()!=null){
            index = "3";
        }

        StCommonPolicy commonPolicyratio = DAOBuilder.instance(commonPolicyDAO).searchFilter(new HashMap<String, Object>(){{

            this.put("id","GRZFDKZGDKBL");

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e);}

        });

        StCommonPolicy commonPolicyamount = DAOBuilder.instance(commonPolicyDAO).searchFilter(new HashMap<String, Object>(){{

            this.put("id","GRZFDKZGED");

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e);}

        });

        StCommonPolicy commonPolicyYearLimit = DAOBuilder.instance(commonPolicyDAO).searchFilter(new HashMap<String, Object>(){{

            this.put("id","GRZFDKZCNX");

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e);}

        });

        BigDecimal ratio = commonPolicyratio == null||commonPolicyratio.getGrzfdkzgdkbl()== null ? BigDecimal.ZERO:commonPolicyratio.getGrzfdkzgdkbl();

        if(ratio.compareTo(new BigDecimal("0.8"))>0){

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"贷款比例超标");
        }

        BigDecimal amount = commonPolicyamount == null||commonPolicyamount.getGrzfdkzged()== null ? BigDecimal.ZERO:commonPolicyamount.getGrzfdkzged();

        BigDecimal YearLimit = commonPolicyYearLimit == null||commonPolicyYearLimit.getGrzfdkzcnx()== null ? BigDecimal.ZERO:commonPolicyYearLimit.getGrzfdkzcnx();

        BigDecimal DKEDFlag = new HashMap<String,BigDecimal>(){{

            if(loanHousingBusinessProcess.getDkdblx().equals(LoanGuaranteeType.抵押.getCode())) {

                this.put("01", loanHousingBusinessProcess.getLoanHousePurchasingVice() == null ? BigDecimal.ZERO : loanHousingBusinessProcess.getLoanHousePurchasingVice().getHtje().multiply(ratio));

                this.put("02", loanHousingBusinessProcess.getLoanHousePurchasingVice() == null ? BigDecimal.ZERO : CollectionUtils.reduce(Arrays.asList(loanHousingBusinessProcess.getLoanHousePurchasingVice().getHtje(), (loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice().getGuaranteeMortgageVices() == null ? BigDecimal.ZERO : CollectionUtils.reduce(loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice().getGuaranteeMortgageVices(), BigDecimal.ZERO, new CollectionUtils.Reducer<CLoanGuaranteeMortgageVice, BigDecimal>() {

                    @Override
                    public BigDecimal reducer(BigDecimal sum, CLoanGuaranteeMortgageVice obj) {
                        return sum.add(obj.getDywpgjz() == null ? BigDecimal.ZERO : obj.getDywpgjz());
                    }

                }))), BigDecimal.ZERO, new CollectionUtils.Reducer<BigDecimal, BigDecimal>() {

                    @Override
                    public BigDecimal reducer(BigDecimal sum, BigDecimal obj) {
                        return sum.compareTo(obj) > 0 ? (obj.compareTo(BigDecimal.ZERO) <= 0 ? sum : obj) : (sum.compareTo(BigDecimal.ZERO) <= 0 ? obj : sum);
                    }

                }).multiply(ratio));

                this.put("2", loanHousingBusinessProcess.getLoanHouseBuildVice() == null ? BigDecimal.ZERO : CollectionUtils.reduce(Arrays.asList(loanHousingBusinessProcess.getLoanHouseBuildVice().getJhjzfy(), loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice().getGuaranteeMortgageVices() == null ? BigDecimal.ZERO : CollectionUtils.reduce(loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice().getGuaranteeMortgageVices(), BigDecimal.ZERO, new CollectionUtils.Reducer<CLoanGuaranteeMortgageVice, BigDecimal>() {
                    @Override
                    public BigDecimal reducer(BigDecimal sum, CLoanGuaranteeMortgageVice obj) {
                        return sum.add(obj.getDywpgjz() == null ? BigDecimal.ZERO : obj.getDywpgjz());
                    }

                })), BigDecimal.ZERO, new CollectionUtils.Reducer<BigDecimal, BigDecimal>() {
                    @Override
                    public BigDecimal reducer(BigDecimal sum, BigDecimal obj) {
                        return sum.compareTo(obj) > 0 ? (obj.compareTo(BigDecimal.ZERO) <= 0 ? sum : obj) : (sum.compareTo(BigDecimal.ZERO) <= 0 ? obj : sum);
                    }

                }).multiply(ratio));

                this.put("3", loanHousingBusinessProcess.getLoanHouseOverhaulVice() == null ? BigDecimal.ZERO : CollectionUtils.reduce(Arrays.asList(loanHousingBusinessProcess.getLoanHouseOverhaulVice().getDxgcys(), loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice().getGuaranteeMortgageVices() == null ? BigDecimal.ZERO : CollectionUtils.reduce(loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice().getGuaranteeMortgageVices(), BigDecimal.ZERO, new CollectionUtils.Reducer<CLoanGuaranteeMortgageVice, BigDecimal>() {
                    @Override
                    public BigDecimal reducer(BigDecimal sum, CLoanGuaranteeMortgageVice obj) { return sum.add(obj.getDywpgjz() == null ? BigDecimal.ZERO : obj.getDywpgjz()); }

                })), BigDecimal.ZERO, new CollectionUtils.Reducer<BigDecimal, BigDecimal>() {
                    @Override
                    public BigDecimal reducer(BigDecimal sum, BigDecimal obj) { return sum.compareTo(obj) > 0 ? (obj.compareTo(BigDecimal.ZERO) <= 0 ? sum : obj) : (sum.compareTo(BigDecimal.ZERO) <= 0 ? obj : sum); }

                }).multiply(ratio));
            }

            if(loanHousingBusinessProcess.getDkdblx().equals(LoanGuaranteeType.质押.getCode())) {

                this.put("01", loanHousingBusinessProcess.getLoanHousePurchasingVice() == null ? BigDecimal.ZERO : loanHousingBusinessProcess.getLoanHousePurchasingVice().getHtje().multiply(ratio));

                this.put("02", loanHousingBusinessProcess.getLoanHousePurchasingVice() == null ? BigDecimal.ZERO : CollectionUtils.reduce(Arrays.asList(loanHousingBusinessProcess.getLoanHousePurchasingVice().getHtje(), (loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice().getGuaranteePledgeVices()== null ? BigDecimal.ZERO : CollectionUtils.reduce(loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice().getGuaranteePledgeVices(), BigDecimal.ZERO, new CollectionUtils.Reducer<CLoanGuaranteePledgeVice, BigDecimal>() {

                    @Override
                    public BigDecimal reducer(BigDecimal sum, CLoanGuaranteePledgeVice obj) { return sum.add(obj.getZywjz() == null ? BigDecimal.ZERO : obj.getZywjz()); }

                }))), BigDecimal.ZERO, new CollectionUtils.Reducer<BigDecimal, BigDecimal>() {

                    @Override
                    public BigDecimal reducer(BigDecimal sum, BigDecimal obj) { return sum.compareTo(obj) > 0 ? (obj.compareTo(BigDecimal.ZERO) <= 0 ? sum : obj) : (sum.compareTo(BigDecimal.ZERO) <= 0 ? obj : sum); }

                }).multiply(ratio));

                this.put("2", loanHousingBusinessProcess.getLoanHouseBuildVice() == null ? BigDecimal.ZERO : CollectionUtils.reduce(Arrays.asList(loanHousingBusinessProcess.getLoanHouseBuildVice().getJhjzfy(), loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice().getGuaranteePledgeVices() == null ? BigDecimal.ZERO : CollectionUtils.reduce(loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice().getGuaranteePledgeVices(), BigDecimal.ZERO, new CollectionUtils.Reducer<CLoanGuaranteePledgeVice, BigDecimal>() {
                    @Override
                    public BigDecimal reducer(BigDecimal sum, CLoanGuaranteePledgeVice obj) { return sum.add(obj.getZywjz() == null ? BigDecimal.ZERO : obj.getZywjz()); }

                })), BigDecimal.ZERO, new CollectionUtils.Reducer<BigDecimal, BigDecimal>() {
                    @Override
                    public BigDecimal reducer(BigDecimal sum, BigDecimal obj) { return sum.compareTo(obj) > 0 ? (obj.compareTo(BigDecimal.ZERO) <= 0 ? sum : obj) : (sum.compareTo(BigDecimal.ZERO) <= 0 ? obj : sum); }

                }).multiply(ratio));

                this.put("3", loanHousingBusinessProcess.getLoanHouseOverhaulVice() == null ? BigDecimal.ZERO : CollectionUtils.reduce(Arrays.asList(loanHousingBusinessProcess.getLoanHouseOverhaulVice().getDxgcys(), loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice().getGuaranteePledgeVices() == null ? BigDecimal.ZERO : CollectionUtils.reduce(loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice().getGuaranteePledgeVices(), BigDecimal.ZERO, new CollectionUtils.Reducer<CLoanGuaranteePledgeVice, BigDecimal>() {
                    @Override
                    public BigDecimal reducer(BigDecimal sum, CLoanGuaranteePledgeVice obj) { return sum.add(obj.getZywjz() == null ? BigDecimal.ZERO : obj.getZywjz()); }

                })), BigDecimal.ZERO, new CollectionUtils.Reducer<BigDecimal, BigDecimal>() {
                    @Override
                    public BigDecimal reducer(BigDecimal sum, BigDecimal obj) { return sum.compareTo(obj) > 0 ? (obj.compareTo(BigDecimal.ZERO) <= 0 ? sum : obj) : (sum.compareTo(BigDecimal.ZERO) <= 0 ? obj : sum); }

                }).multiply(ratio));
            }

            if(loanHousingBusinessProcess.getDkdblx().equals(LoanGuaranteeType.保证.getCode())||loanHousingBusinessProcess.getDkdblx().equals(LoanGuaranteeType.其他.getCode())){

                this.put("01", loanHousingBusinessProcess.getLoanHousePurchasingVice() == null ? BigDecimal.ZERO : loanHousingBusinessProcess.getLoanHousePurchasingVice().getHtje().multiply(ratio));

                this.put("02", loanHousingBusinessProcess.getLoanHousePurchasingVice() == null ? BigDecimal.ZERO : loanHousingBusinessProcess.getLoanHousePurchasingVice().getHtje().multiply(ratio));

                this.put("2", loanHousingBusinessProcess.getLoanHouseBuildVice() == null ? BigDecimal.ZERO : loanHousingBusinessProcess.getLoanHouseBuildVice().getJhjzfy().multiply(ratio));

                this.put("3", loanHousingBusinessProcess.getLoanHouseOverhaulVice() == null ? BigDecimal.ZERO : loanHousingBusinessProcess.getLoanHouseOverhaulVice().getDxgcys().multiply(ratio));

            }
        }}.get(index);

        if(YearLimit.multiply(new BigDecimal(12)).compareTo(loanHousingBusinessProcess.getLoanFundsVice().getDkqs())<0){

            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"贷款年限不能超过"+YearLimit+"年");
        }

        if(loanHousingBusinessProcess.getLoanFundsVice().getHtdkje().compareTo(amount)>0){

            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"贷款额度不能超过"+ (amount.compareTo(DKEDFlag)>0? DKEDFlag.setScale(2,BigDecimal.ROUND_HALF_UP): amount.setScale(2,BigDecimal.ROUND_HALF_UP)));
        }


        if(DKEDFlag!=null&&DKEDFlag.compareTo(BigDecimal.ZERO)>0){

            if(loanHousingBusinessProcess.getLoanFundsVice().getHtdkje().compareTo(DKEDFlag)>0){

                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"贷款额度不能超过"+ (amount.compareTo(DKEDFlag)>0? DKEDFlag.setScale(2,BigDecimal.ROUND_HALF_UP): amount.setScale(2,BigDecimal.ROUND_HALF_UP)));
            }
        }



        if(loanHousingBusinessProcess.getLoanFundsVice().getDkqs().compareTo(new BigDecimal("360"))>0){

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"贷款期数");
        }

//        if(loanHousingBusinessProcess.getLoanHousingPersonInformationVice().getLxzcjcys().compareTo(new BigDecimal("6"))<0&&(loanHousingBusinessProcess.getLoanHousingCoborrowerVice()==null||loanHousingBusinessProcess.getLoanHousingCoborrowerVice().getLxzcjcys().compareTo(new BigDecimal("6"))<0)){
//
//            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"借款人和共同借款人连续正常缴存月数应至少一人不小于6");
//
//        }f
        if(!this.iUploadImagesService.validateUploadFile(UploadFileModleType.贷款.getCode(), new HashMap<String,String>(){{

            this.put("0",(loanHousingBusinessProcess.getLoanHousePurchasingVice()==null) ? null:(!loanHousingBusinessProcess.getLoanHousePurchasingVice().getSfwesf()?UploadFileBusinessType.贷款申请房屋信息购买商品房.getCode():UploadFileBusinessType.贷款申请房屋信息购买二手房.getCode()));
            this.put("1",UploadFileBusinessType.贷款申请房屋信息自建翻建.getCode());
            this.put("2",UploadFileBusinessType.贷款申请房屋信息大修.getCode());

        }}.get(loanHousingBusinessProcess.getDkyt(/*贷款用途*/)),new HashMap<String,String>(){{

            this.put("0", loanHousingBusinessProcess.getLoanHousePurchasingVice()==null?null:loanHousingBusinessProcess.getLoanHousePurchasingVice().getBlzl());
            this.put("1",loanHousingBusinessProcess.getLoanHouseBuildVice() == null? null:loanHousingBusinessProcess.getLoanHouseBuildVice().getBlzl());
            this.put("2",loanHousingBusinessProcess.getLoanHouseOverhaulVice() == null? null:loanHousingBusinessProcess.getLoanHouseOverhaulVice().getBlzl());

        }}.get(loanHousingBusinessProcess.getDkyt(/*贷款用途*/)))){

            throw new ErrorException(ReturnEnumeration.Data_MISS,"房屋信息办理资料");
        }

        if(!this.iUploadImagesService.validateUploadFile(UploadFileModleType.贷款.getCode(),UploadFileBusinessType.贷款申请申请人.getCode(),loanHousingBusinessProcess.getLoanHousingPersonInformationVice().getBlzl())){

            throw new ErrorException(ReturnEnumeration.Data_MISS,"申请人信息办理资料");
        }

        if(loanHousingBusinessProcess.getLoanHousingCoborrowerVice()!=null&&!this.iUploadImagesService.validateUploadFile(UploadFileModleType.贷款.getCode(),UploadFileBusinessType.贷款申请共同借款人.getCode(),loanHousingBusinessProcess.getLoanHousingCoborrowerVice().getBlzl())){

            throw new ErrorException(ReturnEnumeration.Data_MISS,"共同借款人信息办理资料");
        }

        if(!this.iUploadImagesService.validateUploadFile(UploadFileModleType.贷款.getCode(),UploadFileBusinessType.贷款申请担保信息.getCode(),loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice().getBlzl())){

            throw new ErrorException(ReturnEnumeration.Data_MISS,"担保信息办理资料");
        }

        if(!this.iUploadImagesService.validateUploadFile(UploadFileModleType.贷款.getCode(),UploadFileBusinessType.贷款申请其他资料.getCode(),loanHousingBusinessProcess.getBlzl())){

            throw new ErrorException(ReturnEnumeration.Data_MISS,"其他资料办理资料");
        }


        if(!StringUtil.notEmpty(loanHousingBusinessProcess.getDkdblx())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"贷款担保类型");}

        if(loanHousingBusinessProcess.getDkdblx().length()!=2){

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"贷款担保类型");
        }

        if(!StringUtil.notEmpty(loanHousingBusinessProcess.getLoanFundsVice().getHkfs())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"还款方式");}

        if(!CollectionUtils.flatmap(iDictionaryService.getDictionaryByType("LoanPaymentMode"), new CollectionUtils.Transformer<CommonDictionary, String>() {
            @Override
            public String tansform(CommonDictionary var1) { return var1.getCode(); }

        }).contains(loanHousingBusinessProcess.getLoanFundsVice().getHkfs())){

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"还款方式");
        }

        if(!StringUtil.notEmpty(loanHousingBusinessProcess.getLoanFundsVice().getDklx())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"贷款类型");}

        if(loanHousingBusinessProcess.getLoanFundsVice().getDklx().length()!=2){

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"贷款类型");
        }

        if(!StringUtil.notEmpty(loanHousingBusinessProcess.getLoanHousingPersonInformationVice().getJkrzjlx())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"借款人证件类型 ");}

        if(!CollectionUtils.flatmap(iDictionaryService.getDictionaryByType("PersonalCertificate"), new CollectionUtils.Transformer<CommonDictionary, String>() {
            @Override
            public String tansform(CommonDictionary var1) { return var1.getCode(); }

        }).contains(loanHousingBusinessProcess.getLoanHousingPersonInformationVice().getJkrzjlx())){

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"借款人证件类型");
        }

        if(!StringUtil.notEmpty(loanHousingBusinessProcess.getLoanHousingPersonInformationVice().getJkrzjhm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"借款人证件号码");}

        if(PersonCertificateType.身份证.getCode().equals(loanHousingBusinessProcess.getLoanHousingPersonInformationVice().getJkrzjlx())&&!IdcardValidator.isValidatedAllIdcard(loanHousingBusinessProcess.getLoanHousingPersonInformationVice().getJkrzjhm())){

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"借款人证件号码");
        }

        if(loanHousingBusinessProcess.getLoanFundsVice().getJkhtll()==null){ throw new ErrorException(ReturnEnumeration.Data_MISS,"借款合同利率");}

        if(loanHousingBusinessProcess.getLoanFundsVice().getJkhtll().compareTo(BigDecimal.ZERO)<=0){

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"借款合同利率");
        }

        if(loanHousingBusinessProcess.getLoanFundsVice().getLlfsbl()==null){ throw new ErrorException(ReturnEnumeration.Data_MISS,"利率浮动比例");}

        if(loanHousingBusinessProcess.getLoanFundsVice().getLlfsbl().compareTo(BigDecimal.ZERO)<=0){

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"利率浮动比例");
        }

        if(!StringUtil.notEmpty(loanHousingBusinessProcess.getLoanHousingPersonInformationVice().getJkrxm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"借款人姓名");}

        if(loanHousingBusinessProcess.getLoanHousingPersonInformationVice().getJkrxm().length()<2){

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"借款人姓名");
        }

        if(!StringUtil.notEmpty(loanHousingBusinessProcess.getLoanHousingPersonInformationVice().getSjhm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"借款人手机号码");}

        if(loanHousingBusinessProcess.getLoanHousingPersonInformationVice().getSjhm().length()!=11){

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"借款人手机号码");

        }

        if(DateUtil.safeStr2Date(DateUtil.dbformatYear_Month,loanHousingBusinessProcess.getLoanHousingPersonInformationVice().getCsny())==null){

            throw new ErrorException(ReturnEnumeration.Data_MISS,"出生年月");
        }

        if(DateUtil.getAge(DateUtil.safeStr2Date(DateUtil.dbformatYear_Month,loanHousingBusinessProcess.getLoanHousingPersonInformationVice().getCsny())) +loanHousingBusinessProcess.getLoanFundsVice().getDkqs().divide(new BigDecimal("12"),2, RoundingMode.HALF_UP).doubleValue() >65){

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"年龄和贷款年限之和不能大于65 ");
        }


        //region //抵押
        if(loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice()!=null&&loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice().getGuaranteeMortgageVices()!=null&&LoanGuaranteeType.抵押.getCode().equals(loanHousingBusinessProcess.getDkdblx())){

            for(CLoanGuaranteeMortgageVice loanGuaranteeMortgageVice:loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice().getGuaranteeMortgageVices()){

                if(!StringUtil.notEmpty(loanGuaranteeMortgageVice.getDywsyqrsfzhm())||!IdcardValidator.isValidatedAllIdcard(loanGuaranteeMortgageVice.getDywsyqrsfzhm())){
                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"抵押物所有权人身份证号码");
                }

                if(!StringUtil.notEmpty(loanGuaranteeMortgageVice.getDywsyqrxm())||loanGuaranteeMortgageVice.getDywsyqrxm().length()<2){
                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"抵押物所有权人姓名");
                }

                if(!StringUtil.notEmpty(loanGuaranteeMortgageVice.getDywsyqrlxdh())){
                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"抵押物所有权人联系电话");
                }

                if(!StringUtil.notEmpty(loanGuaranteeMortgageVice.getDywmc())){
                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"抵押物名称");
                }

                if(!StringUtil.notEmpty(loanGuaranteeMortgageVice.getQszsbh())){
                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"权属证书编号");
                }

                if(loanGuaranteeMortgageVice.getDywpgjz().compareTo(BigDecimal.ZERO)<=0){
                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"抵押物评估价值");
                }

                if(loanGuaranteeMortgageVice.getFwmj().compareTo(BigDecimal.ZERO)<=0){
                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"担保信息中房屋面积必须大于0");
                }

                if(!StringUtil.notEmpty(loanGuaranteeMortgageVice.getDywfwzl())){
                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"抵押物房屋坐落");
                }

                if(!StringUtil.notEmpty(loanGuaranteeMortgageVice.getDyfwxs())){
                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"抵押物房屋形式");
                }
            }
        }
        //endregion

        //region //质押
        if(loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice()!=null&&loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice().getGuaranteePledgeVices()!=null&&LoanGuaranteeType.质押.getCode().equals(loanHousingBusinessProcess.getDkdblx())){

            for(CLoanGuaranteePledgeVice loanGuaranteePledgeVice:loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice().getGuaranteePledgeVices()){

                if(!StringUtil.notEmpty(loanGuaranteePledgeVice.getZywsyqrsfzhm())||!IdcardValidator.isValidatedAllIdcard(loanGuaranteePledgeVice.getZywsyqrsfzhm())){
                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"质押物所有权人身份证号");
                }

                if(!StringUtil.notEmpty(loanGuaranteePledgeVice.getZywsyqrxm())||loanGuaranteePledgeVice.getZywsyqrxm().length()<2){
                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"质押物所有权人姓名");
                }


                if(!StringUtil.notEmpty(loanGuaranteePledgeVice.getZywsyqrlxdh())){
                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"质押物所有权人联系电话");
                }

                if(!StringUtil.notEmpty(loanGuaranteePledgeVice.getZywmc())){
                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"质押物名称");
                }

                if(loanGuaranteePledgeVice.getZywjz().compareTo(BigDecimal.ZERO)<=0){
                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"质押物评估价值");
                }
            }
        }
        //endregion

        //region //保证

        if(loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice()!=null&&loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice().getGuaranteeVices()!=null&&LoanGuaranteeType.保证.getCode().equals(loanHousingBusinessProcess.getDkdblx())){

            for(CLoanGuaranteeVice loanGuaranteeVice:loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice().getGuaranteeVices()){

                if(!StringUtil.notEmpty(loanGuaranteeVice.getFrdbsfzhm())||!IdcardValidator.isValidatedAllIdcard(loanGuaranteeVice.getFrdbsfzhm())){
                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,("1".equals(loanGuaranteeVice.getBzflx())?"法人代表" : "保证人") + "身份证号码");
                }

                if(!StringUtil.notEmpty(loanGuaranteeVice.getFrdbxm())||loanGuaranteeVice.getFrdbxm().length()<2){

                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,("1".equals(loanGuaranteeVice.getBzflx())?"法人代表" : "保证人") + "身份证号码");

                }
                if(!StringUtil.notEmpty(loanGuaranteeVice.getFrdblxdh())){
                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,("1".equals(loanGuaranteeVice.getBzflx())?"法人代表" : "保证人") + "联系电话");
                }

                if(!StringUtil.notEmpty(loanGuaranteeVice.getYzbm())){
                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"邮政编码");
                }

                if(!StringUtil.notEmpty(loanGuaranteeVice.getFrdbxjzdz())){
                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,("1".equals(loanGuaranteeVice.getBzflx())?"法人代表" : "保证人") + "现居住地址");
                }

                if(!StringUtil.notEmpty(loanGuaranteeVice.getTxdz())){
                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"通讯地址");
                }
            }
        }
        //endregion

        //region //购买
        if(loanHousingBusinessProcess.getLoanHousePurchasingVice() != null) {

            if (StringUtil.notEmpty(loanHousingBusinessProcess.getLoanHousePurchasingVice().getLxfs())&&loanHousingBusinessProcess.getLoanHousePurchasingVice().getLxfs().length()>11){

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "联系方式");
            }
            if (!StringUtil.notEmpty(loanHousingBusinessProcess.getLoanHousePurchasingVice().getFwzl())) { throw new ErrorException(ReturnEnumeration.Data_MISS, "房屋坐落"); }

            if (loanHousingBusinessProcess.getLoanHousePurchasingVice().getFwzl().length() < 10) {

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "房屋坐落长度不能小于10");
            }

            if (loanHousingBusinessProcess.getLoanHousePurchasingVice().getFwjzmj() == null) { throw new ErrorException(ReturnEnumeration.Data_MISS, "房屋建筑面积"); }

            if (loanHousingBusinessProcess.getLoanHousePurchasingVice().getFwjzmj().compareTo(new BigDecimal("9")) < 0) {

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "建筑面积过小");
            }

            if (loanHousingBusinessProcess.getLoanHousePurchasingVice().getFwtnmj() == null) { throw new ErrorException(ReturnEnumeration.Data_MISS, "套内建筑面积"); }

            if (loanHousingBusinessProcess.getLoanHousePurchasingVice().getFwtnmj().compareTo(new BigDecimal("9")) < 0) {

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "套内面积过小");
            }


            if (loanHousingBusinessProcess.getLoanHousePurchasingVice().getFwzj() == null) { throw new ErrorException(ReturnEnumeration.Data_MISS, "房屋总价"); }

            if (loanHousingBusinessProcess.getLoanHousePurchasingVice().getFwzj().compareTo(new BigDecimal("10000")) < 0) {

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "房屋总价过小");
            }

            if (loanHousingBusinessProcess.getLoanHousePurchasingVice().getSfk() == null) { throw new ErrorException(ReturnEnumeration.Data_MISS, "首付款"); }

            if (loanHousingBusinessProcess.getLoanHousePurchasingVice().getSfk().compareTo(BigDecimal.ZERO) <= 0) {

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "首付款过小");
            }

            if(!StringUtil.notEmpty(loanHousingBusinessProcess.getLoanHousePurchasingVice().getSfrmc())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"售房人名称");}

            if(loanHousingBusinessProcess.getLoanHousePurchasingVice().getSfrmc().length()<2){

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"售房人名称");

            }

            if(!StringUtil.notEmpty(loanHousingBusinessProcess.getLoanHousePurchasingVice().getSfrzhhm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"售房人账户号码");}

            if(loanHousingBusinessProcess.getLoanHousePurchasingVice().getSfrzhhm().length()<=4){
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"售房人账户号码");
            }

            if(!StringUtil.notEmpty(loanHousingBusinessProcess.getLoanHousePurchasingVice().getSfrkhyhmc())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"售房人开户银行名称");}

            if(loanHousingBusinessProcess.getLoanHousePurchasingVice().getSfrkhyhmc().length()<=4){
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"售房人开户银行名称");
            }

            CBankBankInfo bankBankInfo = DAOBuilder.instance(this.bankBankInfoDAO).searchFilter(new HashMap<String, Object>(){{
                this.put("bank_name",loanHousingBusinessProcess.getLoanHousePurchasingVice().getSfrkhyhmc());
            }}).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) { throw new ErrorException(e);}
            });

            if(bankBankInfo == null || bankBankInfo.getCode()==null || bankBankInfo.getCode().length()!=3){

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"银行号码");
            }

            if(loanHousingBusinessProcess.getLoanHousePurchasingVice().getSfk()==null){ throw new ErrorException(ReturnEnumeration.Data_MISS,"首付款");}

            if(loanHousingBusinessProcess.getLoanHousePurchasingVice().getSfk().compareTo(new BigDecimal("0"))<=0){
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"首付款");
            }

            if(loanHousingBusinessProcess.getLoanFundsVice()==null || loanHousingBusinessProcess.getLoanFundsVice().getHtdkje() == null){throw new ErrorException(ReturnEnumeration.Data_MISS,"合同贷款金额");}

            if(loanHousingBusinessProcess.getLoanFundsVice().getHtdkje().add(loanHousingBusinessProcess.getLoanHousePurchasingVice().getSfk()).compareTo(loanHousingBusinessProcess.getLoanHousePurchasingVice().getFwzj())>0){

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"首付款和合同贷款金额之和不能大于房屋总价");
            }

            if(loanHousingBusinessProcess.getLoanHousePurchasingVice().getSfk().divide(loanHousingBusinessProcess.getLoanHousePurchasingVice().getFwzj(),2,BigDecimal.ROUND_HALF_DOWN).compareTo(new BigDecimal("0.2"))<0){

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"首付款过少");
            }

        }

        //endregion

        //region //自建翻修
        if(loanHousingBusinessProcess.getLoanHouseBuildVice()!=null){
            if (!StringUtil.notEmpty(loanHousingBusinessProcess.getLoanHouseBuildVice().getFwzl())) { throw new ErrorException(ReturnEnumeration.Data_MISS, "房屋坐落"); }

            if (loanHousingBusinessProcess.getLoanHouseBuildVice().getFwzl().length() < 10) {

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "房屋坐落长度不能小于10");
            }

            if (loanHousingBusinessProcess.getLoanHouseBuildVice().getJchjzmj() == null) { throw new ErrorException(ReturnEnumeration.Data_MISS, "建成后建筑面积"); }

            if (loanHousingBusinessProcess.getLoanHouseBuildVice().getJchjzmj().compareTo(new BigDecimal("9")) < 0) {

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "建成后建筑面积过小");
            }

            if (loanHousingBusinessProcess.getLoanHouseBuildVice().getJchjzmj() == null) { throw new ErrorException(ReturnEnumeration.Data_MISS, "建成后建筑面积"); }

            if (loanHousingBusinessProcess.getLoanHouseBuildVice().getJchjzmj().compareTo(new BigDecimal("9")) < 0) {

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "建成后建筑面积过小");
            }


            if (loanHousingBusinessProcess.getLoanHouseBuildVice().getJhjzfy() == null) { throw new ErrorException(ReturnEnumeration.Data_MISS, "计划建造费用"); }

            if (loanHousingBusinessProcess.getLoanHouseBuildVice().getJhjzfy().compareTo(new BigDecimal("10000")) < 0) {

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "计划建造费用过小");
            }

            if (loanHousingBusinessProcess.getLoanHouseBuildVice().getGrsyzj() == null) { throw new ErrorException(ReturnEnumeration.Data_MISS, "个人使用资金"); }

            if (loanHousingBusinessProcess.getLoanHouseBuildVice().getGrsyzj().compareTo(BigDecimal.ZERO) <= 0) {

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "个人使用资金");
            }

            if(loanHousingBusinessProcess.getLoanFundsVice().getHtdkje().add(loanHousingBusinessProcess.getLoanHouseBuildVice().getGrsyzj()).compareTo(loanHousingBusinessProcess.getLoanHouseBuildVice().getJhjzfy())>0){

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"个人使用资金和合同贷款金额之和不能大于计划建造费用");
            }

            if(!StringUtil.notEmpty(loanHousingBusinessProcess.getLoanHousingPersonInformationVice().getJkrxm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"借款人姓名");}

            if(loanHousingBusinessProcess.getLoanHousingPersonInformationVice().getJkrxm().length()<2){

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"借款人姓名");

            }

            if(!StringUtil.notEmpty(loanHousingBusinessProcess.getLoanHouseBuildVice().getGrskyhzh())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"个人收款银行账号");}

            if(loanHousingBusinessProcess.getLoanHouseBuildVice().getGrskyhzh().length()<=4){
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"个人收款银行账号");
            }

            if(!StringUtil.notEmpty(loanHousingBusinessProcess.getLoanHouseBuildVice().getKhhyhmc())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"开户银行名称");}

            if(loanHousingBusinessProcess.getLoanHouseBuildVice().getKhhyhmc().length()<=4){
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"开户银行名称");
            }

            CBankBankInfo bankBankInfo = DAOBuilder.instance(this.bankBankInfoDAO).searchFilter(new HashMap<String, Object>(){{
                this.put("bank_name",loanHousingBusinessProcess.getLoanHouseBuildVice().getKhhyhmc());
            }}).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) { throw new ErrorException(e);}
            });

            if(bankBankInfo == null || bankBankInfo.getCode()==null || bankBankInfo.getCode().length()!=3){

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"银行号码");
            }
        }

        //endregion
        
        //region //大修
        if(loanHousingBusinessProcess.getLoanHouseOverhaulVice()!=null){

            if (!StringUtil.notEmpty(loanHousingBusinessProcess.getLoanHouseOverhaulVice().getFwzl())) { throw new ErrorException(ReturnEnumeration.Data_MISS, "房屋坐落"); }

            if (loanHousingBusinessProcess.getLoanHouseOverhaulVice().getFwzl().length() < 10) {

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "房屋坐落长度不能小于10");
            }

            if (loanHousingBusinessProcess.getLoanHouseOverhaulVice().getYjzmj() == null) { throw new ErrorException(ReturnEnumeration.Data_MISS, "原建筑面积"); }

            if (loanHousingBusinessProcess.getLoanHouseOverhaulVice().getYjzmj().compareTo(new BigDecimal("9")) < 0) {

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "原建筑面积过小");
            }

            if (loanHousingBusinessProcess.getLoanHouseOverhaulVice().getYjzmj() == null) { throw new ErrorException(ReturnEnumeration.Data_MISS, "原建筑面积"); }

            if (loanHousingBusinessProcess.getLoanHouseOverhaulVice().getYjzmj().compareTo(new BigDecimal("9")) < 0) {

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "原建筑面积过小");
            }


            if (loanHousingBusinessProcess.getLoanHouseOverhaulVice().getDxgcys() == null) { throw new ErrorException(ReturnEnumeration.Data_MISS, "大修工程预算"); }

            if (loanHousingBusinessProcess.getLoanHouseOverhaulVice().getDxgcys().compareTo(new BigDecimal("10000")) < 0) {

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "大修工程预算过小");
            }

            if(loanHousingBusinessProcess.getLoanFundsVice().getHtdkje().add(loanHousingBusinessProcess.getLoanHouseOverhaulVice().getDxgcys().multiply(new BigDecimal("0.2"))).compareTo(loanHousingBusinessProcess.getLoanHouseOverhaulVice().getDxgcys())>0){

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"合同贷款金额不能超过大修工程预算的80%");
            }

            if(!StringUtil.notEmpty(loanHousingBusinessProcess.getLoanHousingPersonInformationVice().getJkrxm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"借款人姓名");}

            if(loanHousingBusinessProcess.getLoanHousingPersonInformationVice().getJkrxm().length()<2){

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"借款人姓名");

            }

            if(!StringUtil.notEmpty(loanHousingBusinessProcess.getLoanHouseOverhaulVice().getGrskyhzh())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"个人收款银行账号");}

            if(loanHousingBusinessProcess.getLoanHouseOverhaulVice().getGrskyhzh().length()<=4){

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"个人收款银行账号");
            }

            if(!StringUtil.notEmpty(loanHousingBusinessProcess.getLoanHouseOverhaulVice().getKhyhmc())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"售房人开户银行名称");}

            if(loanHousingBusinessProcess.getLoanHouseOverhaulVice().getKhyhmc().length()<=4){

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"开户银行名称");
            }

            CBankBankInfo bankBankInfo = DAOBuilder.instance(this.bankBankInfoDAO).searchFilter(new HashMap<String, Object>(){{
                this.put("bank_name",loanHousingBusinessProcess.getLoanHouseOverhaulVice().getKhyhmc());
            }}).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) { throw new ErrorException(e);}
            });

            if(bankBankInfo == null || bankBankInfo.getCode()==null || bankBankInfo.getCode().length()!=3){

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"银行号码");
            }
        }

        //endregion

        //region//个人信息合规检查
        StCommonPerson commonPerson_zjh = DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("zjhm", loanHousingBusinessProcess.getLoanHousingPersonInformationVice().getJkrzjhm());

        }}).extension(new IBaseDAO.CriteriaExtension() {

            @Override
            public void extend(Criteria criteria) {

                criteria.createAlias("collectionPersonalAccount","collectionPersonalAccount");

                criteria.add(Restrictions.isNull("collectionPersonalAccount.xhrq"));
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) { throw new ErrorException(e); }
        });

        if(commonPerson_zjh!=null){

            if(commonPerson_zjh.getXingMing()!=null&&commonPerson_zjh.getZjhm()!=null&&commonPerson_zjh.getZjhm().length()>5&&!commonPerson_zjh.getXingMing().equals(loanHousingBusinessProcess.getLoanHousingPersonInformationVice().getJkrxm())){

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"个人信息");
            }
        }

        StCommonPerson commonPerson_grzh = DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("grzh", loanHousingBusinessProcess.getLoanHousingPersonInformationVice().getJkrgjjzh());

        }}).extension(new IBaseDAO.CriteriaExtension() {

            @Override
            public void extend(Criteria criteria) {

                criteria.createAlias("collectionPersonalAccount","collectionPersonalAccount");

                criteria.add(Restrictions.isNull("collectionPersonalAccount.xhrq"));
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) { throw new ErrorException(e); }
        });

        if(commonPerson_grzh!=null){

            if(commonPerson_grzh.getGrzh()!=null&&commonPerson_grzh.getGrzh()!=null&&commonPerson_grzh.getGrzh().length()>5&&!commonPerson_grzh.getXingMing().equals(loanHousingBusinessProcess.getLoanHousingPersonInformationVice().getJkrxm())){

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"个人信息");
            }
        }

        StCommonPerson commonPerson_xingming = DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("grzh", loanHousingBusinessProcess.getLoanHousingPersonInformationVice().getJkrgjjzh());

        }}).extension(new IBaseDAO.CriteriaExtension() {

            @Override
            public void extend(Criteria criteria) {

                criteria.createAlias("collectionPersonalAccount","collectionPersonalAccount");

                criteria.add(Restrictions.isNull("collectionPersonalAccount.xhrq"));
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) { throw new ErrorException(e); }
        });

        if(commonPerson_xingming!=null){

            if(commonPerson_xingming.getGrzh()!=null&&commonPerson_xingming.getGrzh()!=null&&commonPerson_xingming.getGrzh().length()>5&&!commonPerson_xingming.getZjhm().equals(loanHousingBusinessProcess.getLoanHousingPersonInformationVice().getJkrzjhm())){

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"个人信息");
            }
        }

        //endregion

        if(loanHousingBusinessProcess.getLoanHousingCoborrowerVice()!=null){

            if(!StringUtil.notEmpty(loanHousingBusinessProcess.getLoanHousingCoborrowerVice().getGtjkrzjlx())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"共同借款人证件类型 ");}

            if(!CollectionUtils.flatmap(iDictionaryService.getDictionaryByType("PersonalCertificate"), new CollectionUtils.Transformer<CommonDictionary, String>() {
                @Override
                public String tansform(CommonDictionary var1) { return var1.getCode(); }

            }).contains(loanHousingBusinessProcess.getLoanHousingCoborrowerVice().getGtjkrzjlx())){

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"共同借款人证件类型");
            }

            if(!StringUtil.notEmpty(loanHousingBusinessProcess.getLoanHousingCoborrowerVice().getGtjkrzjhm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"共同借款人证件号码");}

            if(PersonCertificateType.身份证.getCode().equals(loanHousingBusinessProcess.getLoanHousingCoborrowerVice().getGtjkrzjlx())&&!IdcardValidator.isValidatedAllIdcard(loanHousingBusinessProcess.getLoanHousingCoborrowerVice().getGtjkrzjhm())){

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"共同借款人证件号码");
            }

            if(!StringUtil.notEmpty(loanHousingBusinessProcess.getLoanHousingCoborrowerVice().getSjhm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"共同借款人手机号码");}

            if(loanHousingBusinessProcess.getLoanHousingCoborrowerVice().getSjhm().length()!=11){

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"共同借款人手机号码");

            }

            if(!StringUtil.notEmpty(loanHousingBusinessProcess.getLoanHousingCoborrowerVice().getCdgx())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"参贷关系");}

            if(loanHousingBusinessProcess.getLoanHousingCoborrowerVice().getCdgx().length()!=2){

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"参贷关系");
            }

            if(loanHousingBusinessProcess.getLoanHousingPersonInformationVice().getYsr()==null){ throw new ErrorException(ReturnEnumeration.Data_MISS,"月收入");}

            if(loanHousingBusinessProcess.getLoanHousingPersonInformationVice().getYsr().compareTo(new BigDecimal("500"))<0){
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"月收入");

            }

            //region//共同个人信息合规检查
            StCommonPerson co_commonPerson_zjh = DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {{

                this.put("zjhm", loanHousingBusinessProcess.getLoanHousingCoborrowerVice().getGtjkrzjhm());

            }}).extension(new IBaseDAO.CriteriaExtension() {

                @Override
                public void extend(Criteria criteria) {

                    criteria.createAlias("collectionPersonalAccount","collectionPersonalAccount");

                    //criteria.add(Restrictions.isNull("collectionPersonalAccount.xhrq"));
                }
            }).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) { throw new ErrorException(e); }
            });

            if(co_commonPerson_zjh!=null){

                if(co_commonPerson_zjh.getXingMing()!=null&&co_commonPerson_zjh.getZjhm()!=null&&co_commonPerson_zjh.getZjhm().length()>5&&!co_commonPerson_zjh.getXingMing().equals(loanHousingBusinessProcess.getLoanHousingCoborrowerVice().getGtjkrxm())){

                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"共同借款人信息与系统保存的信息不一致");
                }
            }

            StCommonPerson co_commonPerson_grzh = DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {{

                this.put("grzh", loanHousingBusinessProcess.getLoanHousingCoborrowerVice().getGtjkrgjjzh());

            }}).extension(new IBaseDAO.CriteriaExtension() {

                @Override
                public void extend(Criteria criteria) {

                    criteria.createAlias("collectionPersonalAccount","collectionPersonalAccount");

                    //criteria.add(Restrictions.isNull("collectionPersonalAccount.xhrq"));
                }
            }).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) { throw new ErrorException(e); }
            });

            if(co_commonPerson_grzh!=null){

                if(co_commonPerson_grzh.getGrzh()!=null&&co_commonPerson_grzh.getGrzh()!=null&&co_commonPerson_grzh.getGrzh().length()>5&&!co_commonPerson_grzh.getXingMing().equals(loanHousingBusinessProcess.getLoanHousingCoborrowerVice().getGtjkrxm())){

                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"共同借款人信息与系统保存的信息不一致");
                }
            }

            StCommonPerson co_commonPerson_xingming = DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {{

                this.put("grzh", loanHousingBusinessProcess.getLoanHousingCoborrowerVice().getGtjkrgjjzh());

            }}).extension(new IBaseDAO.CriteriaExtension() {

                @Override
                public void extend(Criteria criteria) {

                    criteria.createAlias("collectionPersonalAccount","collectionPersonalAccount");

                    //criteria.add(Restrictions.isNull("collectionPersonalAccount.xhrq"));
                }
            }).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) { throw new ErrorException(e); }
            });

            if(co_commonPerson_xingming!=null){

                if(co_commonPerson_xingming.getGrzh()!=null&&co_commonPerson_xingming.getGrzh()!=null&&co_commonPerson_xingming.getGrzh().length()>5&&!co_commonPerson_xingming.getZjhm().equals(loanHousingBusinessProcess.getLoanHousingCoborrowerVice().getGtjkrzjhm())){

                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"共同借款人信息与系统保存的信息不一致");
                }
            }

            //endregion
        }
    }

}
