package com.handge.housingfund.loan.service;

import com.handge.housingfund.common.service.ISaveAuditHistory;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.bank.enums.BankName;
import com.handge.housingfund.common.service.collection.enumeration.PersonCertificateType;
import com.handge.housingfund.common.service.loan.ILoanRecordService;
import com.handge.housingfund.common.service.loan.ISignContractService;
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
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.BusinessSubType;
import com.handge.housingfund.database.enums.Events;
import com.handge.housingfund.database.enums.SearchOption;
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
import java.util.*;

@Component
@SuppressWarnings({"", "SpringAutowiredFieldsWarningInspection", "Convert2Lambda", "Duplicates", "Anonymous2MethodRef"})
public class SignContractService implements ISignContractService {

    private static String format = "yyyy-MM-dd";

    private static String formatM = "yyyy-MM-dd HH:mm";

    @Autowired
    private ICLoanHousingBusinessProcessDAO loanHousingBusinessProcessDAO;

    @Autowired
    private ICLoanHousingPersonInformationBasicDAO loanHousingPersonInformationBasicDAO;

    @Autowired
    private IStHousingPersonalLoanDAO housingPersonalLoanDAO;


    @Autowired
    private ICLoanHousingLoanDAO loanHousingLoanDAO;

    @Autowired
    private ICBankContractDAO bankContractDAO;

    @Autowired
    @Resource(name = "stateMachineServiceV2")
    private IStateMachineService iStateMachineService;

    @Autowired
    private IDictionaryService iDictionaryService;
    @Autowired
    private IStCommonPolicyDAO commonPolicyDAO;

    @Autowired
    private ILoanRecordService loanRecordService;

    @Autowired
    private IPdfService iPdfService;

    @Autowired
    private IUploadImagesService iUploadImagesService;

    @Autowired
    private ICFileDAO fileDAO;

    @Autowired
    private ICBankBankInfoDAO bankBankInfoDAO;

    @Autowired
    private ICAccountNetworkDAO cAccountNetworkDAO;
    @Autowired
    private ISaveAuditHistory iSaveAuditHistory;
    @Autowired
    private IStCommonPersonDAO commonPersonDAO;
    @Autowired
    private ICAccountNetworkDAO icAccountNetworkDAO;
    private static String formatNYRSF = "yyyy-MM-dd HH:mm";
    /*
      *  completed
      *
      * !逻辑已完成
      *
      *  已测试 lian
      *
      * !存在问题
      *
      * ~swagger 不应该有保存操作
      * */
    @Override
    public CommonResponses postPurchaseContract(TokenContext tokenContext, final String status,String YWLSH, SignContractPost body) {

        //region //参数检查

        boolean allowNull = "0".equals(status);

        boolean isBuild = body.getContractInformation().getDKYT().equals("1");

        boolean isOverhaul = (!isBuild) && body.getContractInformation().getDKYT().equals("2");

        boolean isPurch = (!isBuild) && (!isOverhaul) && body.getContractInformation().getDKYT(/*贷款用途*/).equals("0");//购买一手

        boolean isGuaranteeMortgage = body.getGuaranteeContractInformation().getDKDBLX(/*贷款担保类型*/).equals(LoanGuaranteeType.抵押.getCode());//抵押

        boolean isGuaranteePledge = (!isGuaranteeMortgage) && body.getGuaranteeContractInformation().getDKDBLX(/*贷款担保类型*/).equals(LoanGuaranteeType.质押.getCode());//质押

        boolean isGuarantee = (!isGuaranteePledge) && (!isGuaranteeMortgage) && body.getGuaranteeContractInformation().getDKDBLX(/*贷款担保类型*/).equals(LoanGuaranteeType.保证.getCode());//保证


        //时间
//        if (!DateUtil.isFollowFormat(body.getGuaranteeContractInformation().getMortgageInformation().getDYQJCRQ(), format, (!isGuaranteeMortgage))) { throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "抵押权解除日期"); }
//
//        if (!DateUtil.isFollowFormat(body.getGuaranteeContractInformation().getMortgageInformation().getDYQJLRQ(), format, (!isGuaranteeMortgage))) { throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "抵押权建立日期"); }
//
//        if (!DateUtil.isFollowFormat(body.getGuaranteeContractInformation().getGuaranteeInformation().getFHBZJRQ(), format, (!isGuarantee))) { throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "返还保证金日期"); }
//
//        if (!DateUtil.isFollowFormat(body.getGuaranteeContractInformation().getPledgeInformation().getZYHTKSRQ(), format, (!isGuaranteePledge))) { throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "质押合同开始日期"); }
//
//        if (!DateUtil.isFollowFormat(body.getGuaranteeContractInformation().getPledgeInformation().getZYHTJSRQ(), format, (!isGuaranteePledge))) { throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "质押合同结束日期"); }

        if (!DateUtil.isFollowFormat(body.getContractInformation().getYDFKRQ(), format, allowNull)) { throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "约定放款日期"); }

        if (!DateUtil.isFollowFormat(body.getContractInformation().getJKHTQDRQ(), format, allowNull)) { throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "借款合同签订日期"); }

        if (!DateUtil.isFollowFormat(body.getContractInformation().getYDDQRQ(), format, allowNull)) { throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "约定到期日期"); }

        if (!StringUtil.isDigits(body.getContractInformation().getYDHKR(), allowNull)) { throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "约定还款日"); }

        //number
//        if (!StringUtil.isDigits(body.getGuaranteeContractInformation().getMortgageInformation().getDYWPGZ(), (!isGuaranteeMortgage))) {throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "抵押物评估值"); }
//
//        if (!StringUtil.isDigits(body.getGuaranteeContractInformation().getGuaranteeInformation().getDKBZJ(), (!isGuarantee))) {throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "贷款保证金"); }
//
//        if (!StringUtil.isDigits(body.getGuaranteeContractInformation().getPledgeInformation().getZYWJZ(), (!isGuaranteePledge))) {throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "质押物价值"); }

        if (!StringUtil.isDigits(body.getContractInformation().getJKHTLL(), allowNull)) {throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "借款合同利率"); }

        if (!StringUtil.isDigits(body.getContractInformation().getLLFDBL(), allowNull)) {throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "利率浮动比例"); }

        if (!StringUtil.isDigits(body.getContractInformation().getBuildInformation().getFWJZMJ(), allowNull||(!isBuild))) {throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "房屋建筑面积 "); }

        if (!StringUtil.isDigits(body.getContractInformation().getBuildInformation().getDKQS(), allowNull||(!isBuild))) {throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "贷款期数 "); }

        if (!StringUtil.isDigits(body.getContractInformation().getBuildInformation().getGRSYZJ(), allowNull||(!isBuild))) {throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "个人使用资金 "); }

        if (!StringUtil.isDigits(body.getContractInformation().getBuildInformation().getHTDKJE(), allowNull||(!isBuild))) {throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "合同贷款金额 "); }

        if (!StringUtil.isDigits(body.getContractInformation().getBuildInformation().getJHJZZJ(), allowNull||(!isBuild))) {throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "计划建造总价 "); }

        if (!StringUtil.isDigits(body.getContractInformation().getPurchaseInformation().getFWJZMJ(), allowNull||(!isPurch))) {throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "房屋建筑面积 "); }

        if (!StringUtil.isDigits(body.getContractInformation().getPurchaseInformation().getFWTNMJ(), allowNull||(!isPurch))) {throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "房屋套内面积 "); }

        if (!StringUtil.isDigits(body.getContractInformation().getPurchaseInformation().getFWZJ(), allowNull||(!isPurch))) {throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "房屋总价 "); }

        if (!StringUtil.isDigits(body.getContractInformation().getPurchaseInformation().getDKQS(), allowNull||(!isPurch))) {throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "贷款期数 "); }

        if (!StringUtil.isDigits(body.getContractInformation().getPurchaseInformation().getGFSFK(), allowNull||(!isPurch))) {throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "购房首付款 "); }

        if (!StringUtil.isDigits(body.getContractInformation().getPurchaseInformation().getHTDKJE(), allowNull||(!isPurch))) {throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "合同贷款金额 "); }

        if (!StringUtil.isDigits(body.getContractInformation().getOverhaulInformation().getFWJZMJ(), allowNull||(!isOverhaul))) {throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "房屋建筑面积 "); }

        if (!StringUtil.isDigits(body.getContractInformation().getOverhaulInformation().getJHFXFY(), allowNull||(!isOverhaul))) {throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "计划翻修费用 "); }

        if (!StringUtil.isDigits(body.getContractInformation().getOverhaulInformation().getDKQS(), allowNull||(!isOverhaul))) {throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "贷款期数 "); }

        if (!StringUtil.isDigits(body.getContractInformation().getOverhaulInformation().getHTDKJE(), allowNull||(!isOverhaul))) {throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "合同贷款金额 "); }

        if (!StringUtil.notEmpty(body.getContractInformation().getSWTYHDM(),allowNull)){throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "受委托银行代码 "); }

        if (!StringUtil.notEmpty(body.getContractInformation().getSWTYHMC(),allowNull)){throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "受委托银行名称 "); }

        if (!StringUtil.notEmpty(body.getContractInformation().getZHKHYHDM(),allowNull)){throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "账户开户银行代码 "); }

        if (!StringUtil.notEmpty(body.getContractInformation().getZHKHYHMC(),allowNull)){throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "账户开户银行名称 "); }

        if (!StringUtil.notEmpty(body.getContractInformation().getHKZH(),allowNull)){throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "还款账号 "); }

        if("1".equals(status)&&!this.iUploadImagesService.validateUploadFile(UploadFileModleType.贷款.getCode(),UploadFileBusinessType.签订合同.getCode(),body.getBLZL())){

            throw new ErrorException(ReturnEnumeration.Data_MISS,"资料未上传");
        }

        //endregion

        //region //必要字段查询&完整性验证
        CLoanHousingBusinessProcess loanHousingBusinessProcess = DAOBuilder.instance(this.loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("ywlsh", YWLSH);

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e); }

        });


        if (loanHousingBusinessProcess == null) { throw new ErrorException(ReturnEnumeration.Data_MISS, "业务记录"); }

        if(!LoanBussinessStatus.待签合同.getName().equals(loanHousingBusinessProcess.getStep())&&!LoanBussinessStatus.待确认.getName().equals(loanHousingBusinessProcess.getStep())){ throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "业务状态"); }

        CLoanHousingPersonInformationBasic loanHousingPersonInformationBasic = DAOBuilder.instance(this.loanHousingPersonInformationBasicDAO).searchFilter(new HashMap<String, Object>(){{

            this.put("ywlsh",loanHousingBusinessProcess.getYwlsh());

        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) { throw new ErrorException(e);}
        });

        if(loanHousingPersonInformationBasic == null){ throw new ErrorException(ReturnEnumeration.Data_MISS,"业务记录"); }

        if(loanHousingPersonInformationBasic.getPersonalAccount()==null){ throw new  ErrorException(ReturnEnumeration.Data_MISS,"贷款账号");}

        if(!tokenContext.getUserInfo().getCZY().equals(loanHousingBusinessProcess.getCzy())){ throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务("+YWLSH+")不是由您受理的，不能发起签订"); }

        CAccountNetwork network = DAOBuilder.instance(cAccountNetworkDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("id",tokenContext.getUserInfo().getYWWD());

        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) { throw new ErrorException(e); }
        });

        StHousingPersonalLoan housingPersonalLoan = loanHousingBusinessProcess.getLoanContract() == null ? new StHousingPersonalLoan():loanHousingBusinessProcess.getLoanContract();

        if(loanHousingBusinessProcess.getLoanContract()==null){

            loanHousingBusinessProcess.setLoanContract(housingPersonalLoan);
            loanHousingPersonInformationBasic.setLoanContract(housingPersonalLoan);
            loanHousingPersonInformationBasic.getPersonalAccount().setStHousingPersonalLoan(housingPersonalLoan);
        }

        if(housingPersonalLoan.getcLoanHousingPersonalLoanExtension() == null){
            CLoanHousingPersonalLoanExtension housingPersonalLoanExtension = new CLoanHousingPersonalLoanExtension();
            housingPersonalLoan.setcLoanHousingPersonalLoanExtension(housingPersonalLoanExtension);
        }

        CLoanHousingPersonalLoanExtension housingPersonalLoanExtension = housingPersonalLoan.getcLoanHousingPersonalLoanExtension();

        StHousingGuaranteeContract housingGuaranteeContract = loanHousingPersonInformationBasic.getGuaranteeContract();

        List<CLoanGuaranteePledgeExtension> list_loanGuaranteePledgeExtension = housingGuaranteeContract.getcLoanGuaranteePledgeExtensions().size() == 0 ? new ArrayList<>():housingGuaranteeContract.getcLoanGuaranteePledgeExtensions();

        List<CLoanGuaranteeMortgageExtension> list_loanGuaranteeMortgageExtension = housingGuaranteeContract.getcLoanGuaranteeMortgageExtensions().size() == 0 ? new ArrayList<>():housingGuaranteeContract.getcLoanGuaranteeMortgageExtensions();

        List<CLoanGuaranteeExtension> list_loanGuaranteeExtension = housingGuaranteeContract.getcLoanGuaranteeExtensions().size()==0 ? new ArrayList<>():housingGuaranteeContract.getcLoanGuaranteeExtensions();

        //endregion

        //region //字段填充
        //common


        loanHousingBusinessProcess.setYwwd(network);
        loanHousingBusinessProcess.setCzy(tokenContext.getUserInfo().getCZY()/*操作员*/);

        housingGuaranteeContract.setDbjgmc(body.getGuaranteeContractInformation().getDBJGMC(/*担保机构名称*/));
        housingGuaranteeContract.setDbhtbh(body.getGuaranteeContractInformation().getDBHTBH(/*担保合同编号*/));
        housingGuaranteeContract.setDkdblx(body.getGuaranteeContractInformation().getDKDBLX(/*贷款担保类型（0：抵押 1：质押 2：担保 3：其他）*/));


        if (body.getGuaranteeContractInformation().getDKDBLX(/*贷款担保类型（0：抵押 1：质押 2：担保 3：其他）*/).equals(LoanGuaranteeType.抵押.getCode())) {

            for(SignContractPostGuaranteeContractInformationMortgageInformation signContractPostGuaranteeContractInformationMortgageInformation:body.getGuaranteeContractInformation().getMortgageInformation()){

                CLoanGuaranteeMortgageExtension loanGuaranteeMortgageExtension = CollectionUtils.find(list_loanGuaranteeMortgageExtension, new CollectionUtils.Predicate<CLoanGuaranteeMortgageExtension>() {
                    @Override
                    public boolean evaluate(CLoanGuaranteeMortgageExtension var1) { return var1.getId().equals(signContractPostGuaranteeContractInformationMortgageInformation.getId()); }
                });

                if(loanGuaranteeMortgageExtension == null){ throw  new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"担保信息不匹配");}

                housingGuaranteeContract.setDywpgjz(housingGuaranteeContract.getDywpgjz().add(StringUtil.safeBigDecimal(signContractPostGuaranteeContractInformationMortgageInformation.getDYWPGZ(/*抵押物评估值*/))));
                loanGuaranteeMortgageExtension.setDywpgjz(StringUtil.safeBigDecimal(signContractPostGuaranteeContractInformationMortgageInformation.getDYWPGZ(/*抵押物评估值*/)));
                housingGuaranteeContract.setDyqjcrq(DateUtil.safeStr2Date(format, signContractPostGuaranteeContractInformationMortgageInformation.getDYQJCRQ(/*抵押权解除日期*/)));
                loanGuaranteeMortgageExtension.setDywfwzl(signContractPostGuaranteeContractInformationMortgageInformation.getDYFWZL(/*抵押房屋坐落*/));
                housingGuaranteeContract.setDywqzh(signContractPostGuaranteeContractInformationMortgageInformation.getDYWQZH(/*抵押物权证号*/));
                housingGuaranteeContract.setDyqjlrq(DateUtil.safeStr2Date(format, signContractPostGuaranteeContractInformationMortgageInformation.getDYQJLRQ(/*抵押权建立日期*/)));
                housingGuaranteeContract.setDywtxqzh(signContractPostGuaranteeContractInformationMortgageInformation.getDYWTXQZH(/*抵押物他项权证号*/));
            }
        }


        if (body.getGuaranteeContractInformation().getDKDBLX(/*贷款担保类型（0：抵押 1：质押 2：担保 3：其他）*/).equals(LoanGuaranteeType.质押.getCode())) {

            for(SignContractPostGuaranteeContractInformationPledgeInformation signContractPostGuaranteeContractInformationPledgeInformation:body.getGuaranteeContractInformation().getPledgeInformation()){

                CLoanGuaranteePledgeExtension loanGuaranteePledgeExtension = CollectionUtils.find(list_loanGuaranteePledgeExtension, new CollectionUtils.Predicate<CLoanGuaranteePledgeExtension>() {
                    @Override
                    public boolean evaluate(CLoanGuaranteePledgeExtension var1) { return var1.getId().equals(signContractPostGuaranteeContractInformationPledgeInformation.getId()); }
                });

                if(loanGuaranteePledgeExtension == null){ throw  new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"担保信息不匹配");}
                housingGuaranteeContract.setDywpgjz(housingGuaranteeContract.getDywpgjz().add(StringUtil.safeBigDecimal(signContractPostGuaranteeContractInformationPledgeInformation.getZYWJZ(/*质押物价值*/))));
                housingGuaranteeContract.setZyhtksrq(DateUtil.safeStr2Date(format, signContractPostGuaranteeContractInformationPledgeInformation.getZYHTKSRQ(/*质押合同开始日期*/)));
                loanGuaranteePledgeExtension.setZywsyqrsfzhm(signContractPostGuaranteeContractInformationPledgeInformation.getZYWSYQRSFHM(/*质押物所有权人身份证号码*/));
                housingGuaranteeContract.setZywbh(signContractPostGuaranteeContractInformationPledgeInformation.getZYWBM(/*质押物编码*/));
                loanGuaranteePledgeExtension.setZywsyqrxm(signContractPostGuaranteeContractInformationPledgeInformation.getZYWSYQRXM(/*质押物所有权人姓名*/));
                housingGuaranteeContract.setZywjz(StringUtil.safeBigDecimal(signContractPostGuaranteeContractInformationPledgeInformation.getZYWJZ(/*质押物价值*/)));
                housingGuaranteeContract.setZyhtbh(signContractPostGuaranteeContractInformationPledgeInformation.getZYHTBH(/*质押合同编号*/));
                loanGuaranteePledgeExtension.setZywsyqrlxdh(signContractPostGuaranteeContractInformationPledgeInformation.getZYWSYQRLXDH(/*质押物所有权人联系电话*/));
                housingGuaranteeContract.setZyhtjsrq(DateUtil.safeStr2Date(format, signContractPostGuaranteeContractInformationPledgeInformation.getZYHTJSRQ(/*质押合同结束日期*/)));
                housingGuaranteeContract.setZywmc(signContractPostGuaranteeContractInformationPledgeInformation.getZYWMC(/*质押物名称*/));
            }


        }

        if (body.getGuaranteeContractInformation().getDKDBLX(/*贷款担保类型（0：抵押 1：质押 2：担保 3：其他）*/).equals(LoanGuaranteeType.保证.getCode())) {

            for(SignContractPostGuaranteeContractInformationGuaranteeInformation signContractPostGuaranteeContractInformationGuaranteeInformation:body.getGuaranteeContractInformation().getGuaranteeInformation()){

                CLoanGuaranteeExtension loanGuaranteeExtension = CollectionUtils.find(list_loanGuaranteeExtension, new CollectionUtils.Predicate<CLoanGuaranteeExtension>() {
                    @Override
                    public boolean evaluate(CLoanGuaranteeExtension var1) { return var1.getId().equals(signContractPostGuaranteeContractInformationGuaranteeInformation.getId()); }
                });

                if(loanGuaranteeExtension == null){ throw  new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"担保信息不匹配");}

                housingGuaranteeContract.setDywpgjz(housingGuaranteeContract.getDywpgjz().add(StringUtil.safeBigDecimal(signContractPostGuaranteeContractInformationGuaranteeInformation.getDKBZJ(/*贷款保证金*/))));
                housingGuaranteeContract.setFhbzjrq(DateUtil.safeStr2Date(format, signContractPostGuaranteeContractInformationGuaranteeInformation.getFHBZJRQ(/*返还保证金日期*/)));
                housingGuaranteeContract.setBzjgmc(signContractPostGuaranteeContractInformationGuaranteeInformation.getBZJGMC(/*保证机构名称*/));
                housingGuaranteeContract.setBzhtbh(signContractPostGuaranteeContractInformationGuaranteeInformation.getBZHTBH(/*保证合同编号*/));
                housingGuaranteeContract.setDkbzj(StringUtil.safeBigDecimal(signContractPostGuaranteeContractInformationGuaranteeInformation.getDKBZJ(/*贷款保证金*/)));
            }

        }

        housingPersonalLoan.setYdfkrq(DateUtil.safeStr2Date(format, body.getContractInformation().getYDFKRQ(/*约定放款日期*/)));
        housingPersonalLoan.setZhkhyhmc(body.getContractInformation().getZHKHYHMC(/*账号开户银行名称*/));
        housingPersonalLoan.setSwtyhmc(body.getContractInformation().getSWTYHMC(/*受委托银行名称*/));
        housingPersonalLoan.setSwtyhdm(body.getContractInformation().getSWTYHDM(/*受委托银行代码*/));
        housingPersonalLoan.setJkhtqdrq(DateUtil.safeStr2Date(format, body.getContractInformation().getJKHTQDRQ(/*借款合同签订日期*/)));
        housingPersonalLoan.setHkzh(body.getContractInformation().getHKZH(/*还款账号*/));
        housingPersonalLoan.setJkhtll(StringUtil.safeBigDecimal(body.getContractInformation().getJKHTLL(/*借款合同利率*/)));
        housingPersonalLoan.setLlfdbl(StringUtil.safeBigDecimal(body.getContractInformation().getLLFDBL(/*利率浮动比例*/)));
        housingPersonalLoan.setYddqrq(DateUtil.safeStr2Date(format, body.getContractInformation().getYDDQRQ(/*约定到期日期*/)));
        housingPersonalLoan.setYdhkr(body.getContractInformation().getYDHKR(/*约定还款日*/));
        housingPersonalLoan.setZhkhyhdm(body.getContractInformation().getZHKHYHDM(/*账号开户银行代码*/));
        housingPersonalLoanExtension.setHkzhhm(body.getContractInformation().getHKZHHM()/*还款账户户名*/);
        //housingPersonalLoan.setHtdkje(body.getContractInformation().get);
        housingPersonalLoanExtension.setDkyt(body.getContractInformation().getDKYT());

        if (body.getContractInformation().getDKYT().equals("0")) {

            housingPersonalLoan.setFwjzmj(StringUtil.safeBigDecimal(body.getContractInformation().getPurchaseInformation().getFWJZMJ(/*房屋建筑面积 */)));
            housingPersonalLoan.setSfrzhhm(body.getContractInformation().getPurchaseInformation().getSFRZHHM(/*售房人开户户名 */));
            housingPersonalLoan.setFwzl(body.getContractInformation().getPurchaseInformation().getFWZL(/*房屋坐落 */));
            housingPersonalLoan.setFwtnmj(StringUtil.safeBigDecimal(body.getContractInformation().getPurchaseInformation().getFWTNMJ(/*房屋套内面积 */)));
            housingPersonalLoan.setFwxz(body.getContractInformation().getPurchaseInformation().getFWXZ(/*房屋性质（0：商品房 1：障碍性住房 2：自建房 3：两限房 4：集资房 5：危改房 6：经济适用房 7：房改房 8：其他） */));
            housingPersonalLoan.setFwzj(StringUtil.safeBigDecimal(body.getContractInformation().getPurchaseInformation().getFWZJ(/*房屋总价 */)));
            housingPersonalLoan.setJkrzjh(body.getContractInformation().getPurchaseInformation().getJKRZJHM(/*借款人证件号码 */));
            housingPersonalLoan.setSfrkhyhmc(body.getContractInformation().getPurchaseInformation().getSFRKHYHMC(/*售房人开户银行名称 */));
            housingPersonalLoan.setDkqs(StringUtil.safeBigDecimal(body.getContractInformation().getPurchaseInformation().getDKQS(/*贷款期数 */)));
            housingPersonalLoan.setGfsfk(StringUtil.safeBigDecimal(body.getContractInformation().getPurchaseInformation().getGFSFK(/*购房首付款 */)));
            housingPersonalLoan.setDklx(body.getContractInformation().getPurchaseInformation().getDKLX(/*贷款类型 */));
            housingPersonalLoan.setJkrxm(body.getContractInformation().getPurchaseInformation().getJKRXM(/*借款人姓名 */));
            housingPersonalLoan.setHtdkje(StringUtil.safeBigDecimal(body.getContractInformation().getPurchaseInformation().getHTDKJE(/*合同贷款金额 */)));
//            housingPersonalLoanExtension.setDkgbjhye(housingPersonalLoan.getHtdkje());
//            housingPersonalLoanExtension.setDkgbjhqs(housingPersonalLoan.getDkqs());
            housingPersonalLoan.setSfrzhhm(body.getContractInformation().getPurchaseInformation().getSFRZHHM(/*售房人账户号码 */));
            housingPersonalLoanExtension.setSjhm(body.getContractInformation().getPurchaseInformation().getSJHM(/*手机号码 */));
            housingPersonalLoan.setJkrgjjzh(body.getContractInformation().getPurchaseInformation().getJKRGJJZH(/*借款人公积金账号 */));
            housingPersonalLoan.setJkrzjlx(body.getContractInformation().getPurchaseInformation().getJKRZJLX(/*借款人证件类型 */));
            housingPersonalLoan.setGfhtbh(body.getContractInformation().getPurchaseInformation().getGFHTBH(/*购房合同编号 */));
            housingPersonalLoan.setBlzl(body.getBLZL(/* */));
            housingPersonalLoan.setSfrmc(body.getContractInformation().getPurchaseInformation().getSFRMC(/*售房人名称 */));
            housingPersonalLoan.setDkdblx(body.getContractInformation().getPurchaseInformation().getDKDBLX(/*贷款担保类型 */));
            housingPersonalLoan.setDkhkfs(body.getContractInformation().getPurchaseInformation().getDKHKFS(/*贷款还款方式 */));


        }

        if (body.getContractInformation().getDKYT().equals("1")) {

            housingPersonalLoan.setFwjzmj(StringUtil.safeBigDecimal(body.getContractInformation().getBuildInformation().getFWJZMJ(/*房屋建筑面积 */)));
            housingPersonalLoanExtension.setGrskyhzh(body.getContractInformation().getBuildInformation().getGRSKYHZH(/*个人收款银行账号 */));
            housingPersonalLoan.setFwzl(body.getContractInformation().getBuildInformation().getFWZL(/*房屋坐落 */));
            housingPersonalLoan.setFwxz(body.getContractInformation().getBuildInformation().getFWXZ(/*房屋性质（0：商品房 1：障碍性住房 2：自建房 3：两限房 4：集资房 5：危改房 6：经济适用房 7：房改房 8：其他） */));
            housingPersonalLoan.setJkrzjh(body.getContractInformation().getBuildInformation().getJKRZJHM(/*借款人证件号码 */));
            housingPersonalLoan.getcLoanHousingPersonalLoanExtension().setKhyhmc(body.getContractInformation().getBuildInformation().getKHYHMC(/*开户银行名称 */));
            housingPersonalLoanExtension.setKhhm(body.getContractInformation().getBuildInformation().getKHHM(/*开户户名 */));
            housingPersonalLoan.setDklx(body.getContractInformation().getBuildInformation().getDKLX(/*贷款类型 */));
            housingPersonalLoan.setDkqs(StringUtil.safeBigDecimal(body.getContractInformation().getBuildInformation().getDKQS(/*贷款期数 */)));
            housingPersonalLoan.setJkrxm(body.getContractInformation().getBuildInformation().getJKRXM(/*借款人姓名 */));
            housingPersonalLoanExtension.setGrsyzj(StringUtil.safeBigDecimal(body.getContractInformation().getBuildInformation().getGRSYZJ(/*个人使用资金 */)));
            housingPersonalLoan.setHtdkje(StringUtil.safeBigDecimal(body.getContractInformation().getBuildInformation().getHTDKJE(/*合同贷款金额 */)));
//            housingPersonalLoanExtension.setDkgbjhye(housingPersonalLoan.getHtdkje());
//            housingPersonalLoanExtension.setDkgbjhqs(housingPersonalLoan.getDkqs());
            housingPersonalLoan.setJkrgjjzh(body.getContractInformation().getBuildInformation().getJKRGJJZH(/*借款人公积金账号 */));
            housingPersonalLoan.setJkrzjlx(body.getContractInformation().getBuildInformation().getJKRZJLX(/*借款人证件类型 */));
            housingPersonalLoan.setBlzl(body.getBLZL(/*办理资料*/));
            housingPersonalLoan.setDkdblx(body.getContractInformation().getBuildInformation().getDKDBLX(/*贷款担保类型 */));
            housingPersonalLoan.setDkhkfs(body.getContractInformation().getBuildInformation().getDKHKFS(/*贷款还款方式 */));
            housingPersonalLoanExtension.setJhjzzj(StringUtil.safeBigDecimal(body.getContractInformation().getBuildInformation().getJHJZZJ(/*计划建造总价 */)));
            //housingPersonalLoan.setSfrkhyhdm(loanHousingPersonInformationBasic.getBuild().get);
            housingPersonalLoan.setSfrkhyhmc(body.getContractInformation().getBuildInformation().getKHYHMC(/*开户银行名称 */));
            housingPersonalLoan.setSfrmc(body.getContractInformation().getBuildInformation().getJKRXM(/*借款人姓名 */));
            housingPersonalLoan.setSfrzhhm(body.getContractInformation().getBuildInformation().getGRSKYHZH(/*个人收款银行账号 */));
            housingPersonalLoan.setFwtnmj(StringUtil.safeBigDecimal(body.getContractInformation().getBuildInformation().getFWJZMJ(/*房屋建筑面积 */)));
            housingPersonalLoan.setFwzj(StringUtil.safeBigDecimal(body.getContractInformation().getBuildInformation().getJHJZZJ(/*计划建造总价 */)));
            housingPersonalLoan.setGfsfk(StringUtil.safeBigDecimal(body.getContractInformation().getBuildInformation().getGRSYZJ(/*个人使用资金 */)));
        }



        if (body.getContractInformation().getDKYT().equals("2")) {

            housingPersonalLoan.setFwjzmj(StringUtil.safeBigDecimal(body.getContractInformation().getOverhaulInformation().getFWJZMJ(/*房屋建筑面积 */)));
            housingPersonalLoanExtension.setGrskyhzh(body.getContractInformation().getOverhaulInformation().getGRSKYHZH(/*个人收款银行账号 */));
            housingPersonalLoan.setFwzl(body.getContractInformation().getOverhaulInformation().getFWZL(/*房屋坐落 */));
            housingPersonalLoan.setFwxz(body.getContractInformation().getOverhaulInformation().getFWXZ(/*房屋性质（0：商品房 1：障碍性住房 2：自建房 3：两限房 4：集资房 5：危改房 6：经济适用房 7：房改房 8：其他） */));
            housingPersonalLoan.setJkrzjh(body.getContractInformation().getOverhaulInformation().getJKRZJHM(/*借款人证件号码 */));
            housingPersonalLoanExtension.setKhyhmc(body.getContractInformation().getOverhaulInformation().getKHYHMC(/*开户银行名称 */));
            housingPersonalLoanExtension.setKhhm(body.getContractInformation().getOverhaulInformation().getKHHM(/*开户户名 */));
            housingPersonalLoanExtension.setJhfxfy(StringUtil.safeBigDecimal(body.getContractInformation().getOverhaulInformation().getJHFXFY(/*计划翻修费用 */)));
            housingPersonalLoan.setDklx(body.getContractInformation().getOverhaulInformation().getDKLX(/*贷款类型 */));
            housingPersonalLoan.setDkqs(StringUtil.safeBigDecimal(body.getContractInformation().getOverhaulInformation().getDKQS(/*贷款期数 */)));
            housingPersonalLoan.setJkrxm(body.getContractInformation().getOverhaulInformation().getJKRXM(/*借款人姓名 */));
            housingPersonalLoan.setHtdkje(StringUtil.safeBigDecimal(body.getContractInformation().getOverhaulInformation().getHTDKJE(/*合同贷款金额 */)));
            housingPersonalLoan.setJkrgjjzh(body.getContractInformation().getOverhaulInformation().getJKRGJJZH(/*借款人公积金账号 */));
            housingPersonalLoan.setJkrzjlx(body.getContractInformation().getOverhaulInformation().getJKRZJLX(/*借款人证件类型 */));
            housingPersonalLoan.setBlzl(body.getBLZL(/* */));
            housingPersonalLoan.setDkdblx(body.getContractInformation().getOverhaulInformation().getDKDBLX(/*贷款担保类型 */));
            housingPersonalLoan.setDkhkfs(body.getContractInformation().getOverhaulInformation().getDKHKFS(/*贷款还款方式 */));
            //housingPersonalLoan.setSfrkhyhdm(loanHousingPersonInformationBasic.getOverhaul().get);
            housingPersonalLoan.setSfrkhyhmc(body.getContractInformation().getOverhaulInformation().getKHYHMC(/*开户银行名称 */));
            housingPersonalLoan.setSfrmc(body.getContractInformation().getOverhaulInformation().getJKRXM(/*借款人姓名 */));
            housingPersonalLoan.setSfrzhhm(body.getContractInformation().getOverhaulInformation().getGRSKYHZH(/*个人收款银行账号 */));
            housingPersonalLoan.setFwtnmj(StringUtil.safeBigDecimal(body.getContractInformation().getOverhaulInformation().getFWJZMJ(/*房屋建筑面积 */)));
            housingPersonalLoan.setFwzj(StringUtil.safeBigDecimal(body.getContractInformation().getOverhaulInformation().getJHFXFY(/*计划翻修费用 */)));
            housingPersonalLoan.setGfsfk(housingPersonalLoan.getFwzj().multiply(new BigDecimal("0.2")));
        }




        StHousingPersonalLoan savedPersonalLoan = DAOBuilder.instance(this.housingPersonalLoanDAO).entity(housingPersonalLoan).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) { throw new ErrorException(e); }
        });

        CBankBankInfo bankBankInfo = DAOBuilder.instance(this.bankBankInfoDAO).searchFilter(new HashMap<String, Object>(){{

            this.put("bank_name",housingPersonalLoan.getSfrkhyhmc());

        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) { throw new ErrorException(e);}
        });

        if(bankBankInfo == null || bankBankInfo.getCode()==null || bankBankInfo.getCode().length()!=3){

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"银行号码");
        }
        housingPersonalLoan.setSfrkhyhdm(bankBankInfo.getCode());

        loanHousingBusinessProcess.setLoanContract(savedPersonalLoan);

        loanHousingPersonInformationBasic.setLoanContract(savedPersonalLoan);

        loanHousingPersonInformationBasic.setDkzhzt("1".equals(status)?LoanAccountType.待放款.getCode():LoanAccountType.待确认.getCode());

        if(loanHousingPersonInformationBasic.getCoborrower()!=null){

            loanHousingPersonInformationBasic.getCoborrower().setJkhtbh(savedPersonalLoan.getJkhtbh());
        }

        if(loanHousingPersonInformationBasic.getPersonalAccount()!=null){
            loanHousingPersonInformationBasic.getPersonalAccount().setJkhtbh(savedPersonalLoan.getJkhtbh());
        }
        if(loanHousingPersonInformationBasic.getGuaranteeContract()!=null){

            loanHousingPersonInformationBasic.getGuaranteeContract().setJkhtbh(savedPersonalLoan.getJkhtbh());
        }
        if(loanHousingPersonInformationBasic.getLoanContract()!=null){

            loanHousingPersonInformationBasic.getLoanContract().setJkhtbh(savedPersonalLoan.getJkhtbh());
        }
        CLoanHousingPersonInformationBasic savedBasic = DAOBuilder.instance(this.loanHousingPersonInformationBasicDAO).entity(loanHousingPersonInformationBasic).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) { throw new ErrorException(e);}
        });

        loanHousingBusinessProcess.getLoanHousingPersonInformationVice().setHkzh(housingPersonalLoan.getHkzh());
        loanHousingBusinessProcess.getLoanHousingPersonInformationVice().setZhkhyhmc(housingPersonalLoan.getZhkhyhmc());
        loanHousingBusinessProcess.getLoanHousingPersonInformationVice().setHkzhhm(housingPersonalLoan.getcLoanHousingPersonalLoanExtension().getHkzhhm());

        CLoanHousingBusinessProcess savedProcess = DAOBuilder.instance(this.loanHousingBusinessProcessDAO).entity(loanHousingBusinessProcess).saveThenFetchObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e); }

        });

        //endregion

        //region //修改状态
        StateMachineUtils.updateState(this.iStateMachineService, new HashMap<String,String>(){{

            this.put("0",LoanBussinessStatus.待签合同.getName().equals(savedProcess.getStep())? Events.通过.getEvent():Events.保存.getEvent());

            this.put("1",Events.通过.getEvent());

        }}.get(status),new TaskEntity(){{

            this.setStatus(savedProcess.getStep());
            this.setTaskId(savedProcess.getYwlsh());
            this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
            this.setNote("");
            this.setSubtype(BusinessSubType.贷款_个人贷款申请.getSubType());
            this.setType(com.handge.housingfund.database.enums.BusinessType.Loan);
            this.setOperator(savedProcess.getCzy());
            this.setWorkstation(savedProcess.getYwwd().getId());

        }} , new StateMachineUtils.StateChangeHandler() {
            @Override
            public void onStateChange(boolean succeed, String next, Exception e) {

                if (e != null) { throw new ErrorException(e); }

                if (!succeed || next == null) { return; }


                savedProcess.setStep(next);

                if(StringUtil.isIntoReview(next,null)){

                    savedProcess.setDdsj(new Date());
                }

                DAOBuilder.instance(loanHousingBusinessProcessDAO).entity(savedProcess).save(new DAOBuilder.ErrorHandler() {

                    @Override
                    public void error(Exception e) { throw new ErrorException(e); }
                });

                DAOBuilder.instance(loanHousingPersonInformationBasicDAO).entity(savedBasic).save(new DAOBuilder.ErrorHandler() {

                    @Override
                    public void error(Exception e) { throw new ErrorException(e); }
                });
            }
        });

        this.checkContractAvailable(housingPersonalLoan);

        if(!housingPersonalLoan.getSfrkhyhmc().equals(housingPersonalLoan.getSwtyhmc())){ throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"收款银行名称和受委托银行名称不一致"); }

        if(!housingPersonalLoan.getSfrkhyhmc().equals(housingPersonalLoan.getZhkhyhmc())){ throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"收款银行名称和账户开户银行名称不一致"); }

        if(housingGuaranteeContract.getDywpgjz().compareTo(housingPersonalLoan.getHtdkje())<0){

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,new HashMap<String,String>(){

                @Override
                public String get(Object key) { return super.get(key) == null ? "抵押物价值" : super.get(key); }

                {

                    this.put(LoanGuaranteeType.抵押.getCode(),"抵押物价值");
                    this.put(LoanGuaranteeType.质押.getCode(),"质押物价值");
                    this.put(LoanGuaranteeType.保证.getCode(),"贷款保证金");

                }
            }.get(housingGuaranteeContract.getDkdblx()) + "不足");
        }

        if("1".equals(status)){



            if(LoanBussinessStatus.待确认.getName().equals(loanHousingBusinessProcess.getStep())){

                this.iSaveAuditHistory.saveNormalBusiness(savedProcess.getYwlsh(),tokenContext,LoanBusinessType.贷款发放.getName(),"签订合同");
            }
            this.printContractPdf(tokenContext,YWLSH);
        }
        //endregion

        return new CommonResponses() {{

            this.setId(savedProcess.getYwlsh());

            this.setState("success");
        }};
    }


    @Override
    public SignContractPost getContractAuto(TokenContext tokenContext,String YWLSH){


        CLoanHousingPersonInformationBasic loanHousingPersonInformationBasic = DAOBuilder.instance(this.loanHousingPersonInformationBasicDAO).searchFilter(new HashMap<String, Object>(){{

            this.put("ywlsh",YWLSH);

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e);}
        });

        CLoanHousingBusinessProcess loanHousingBusinessProcess = DAOBuilder.instance(this.loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("ywlsh", YWLSH);

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e); }

        });

        if (loanHousingBusinessProcess == null||loanHousingPersonInformationBasic == null) { throw new ErrorException(ReturnEnumeration.Data_MISS, "业务记录"); }

        CLoanHousePurchasingBasic loanHousePurchasingBasic = loanHousingPersonInformationBasic.getPurchasing();

        CLoanHouseBuildBasic loanHouseBuildBasic = loanHousingPersonInformationBasic.getBuild();

        CLoanHouseOverhaulBasic loanHouseOverhaulBasic = loanHousingPersonInformationBasic.getOverhaul();

        CLoanFundsInformationBasic loanFundsInformationBasic = loanHousingPersonInformationBasic.getFunds();

        StHousingGuaranteeContract housingGuaranteeContract = loanHousingPersonInformationBasic.getGuaranteeContract();

        List<CLoanGuaranteeMortgageExtension> list_loanGuaranteeMortgageExtension = housingGuaranteeContract.getcLoanGuaranteeMortgageExtensions();

        List<CLoanGuaranteeExtension> list_loanGuaranteeExtension = housingGuaranteeContract.getcLoanGuaranteeExtensions();

        List<CLoanGuaranteePledgeExtension> list_loanGuaranteePledgeExtension = housingGuaranteeContract.getcLoanGuaranteePledgeExtensions();

        StHousingPersonalLoan housingPersonalLoan = loanHousingPersonInformationBasic.getLoanContract();

        StCommonPolicy commonPolicy = DAOBuilder.instance(commonPolicyDAO).searchFilter(new HashMap<String, Object>(){{

            this.put("lllx","03");

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e);}

        });

        return new SignContractPost(){{

            this.setGuaranteeContractInformation(new SignContractPostGuaranteeContractInformation(){{

                this.setMortgageInformation(new ArrayList<SignContractPostGuaranteeContractInformationMortgageInformation>(){{

                    for (CLoanGuaranteeMortgageExtension loanGuaranteeMortgageExtension:list_loanGuaranteeMortgageExtension){

                        this.add(new SignContractPostGuaranteeContractInformationMortgageInformation(){{

                            this.setDYWQZH(housingGuaranteeContract.getDywqzh()/*抵押物权证号*/);

                            this.setDYWPGZ(loanGuaranteeMortgageExtension.getDywpgjz()+""/*抵押物评估值*/);

                            this.setDYQJLRQ(DateUtil.date2Str(housingGuaranteeContract.getDyqjlrq(),format)/*抵押权建立日期*/);

                            this.setDYWTXQZH(housingGuaranteeContract.getDywtxqzh()/*抵押物他项权证号*/);

                            this.setDYQJCRQ(DateUtil.date2Str(housingGuaranteeContract.getDyqjcrq(),format)/*抵押权解除日期*/);

                            this.setDYFWZL(loanGuaranteeMortgageExtension.getDywfwzl()/*抵押房屋坐落*/);

                            this.setId(loanGuaranteeMortgageExtension.getId());

                        }});
                    }
                }});

                this.setGuaranteeInformation(new ArrayList<SignContractPostGuaranteeContractInformationGuaranteeInformation>(){{

                    for(CLoanGuaranteeExtension loanGuaranteeExtension : list_loanGuaranteeExtension){

                        this.add(new SignContractPostGuaranteeContractInformationGuaranteeInformation(){{

                            this.setFHBZJRQ(DateUtil.date2Str(housingGuaranteeContract.getFhbzjrq(),format)/*返还保证金日期*/);

                            this.setBZHTBH(housingGuaranteeContract.getBzhtbh()/*保证合同编号*/);

                            this.setBZJGMC(housingGuaranteeContract.getBzjgmc()/*保证机构名称*/);

                            this.setDKBZJ(housingGuaranteeContract.getDkbzj().compareTo(BigDecimal.ZERO)==0 ? "":(housingGuaranteeContract.getDkbzj()+"")/*贷款保证金*/);

                            this.setId(loanGuaranteeExtension.getId());

                        }});
                    }
                }});

                this.setPledgeInformation(new ArrayList<SignContractPostGuaranteeContractInformationPledgeInformation>(){{

                    for(CLoanGuaranteePledgeExtension loanGuaranteePledgeExtension : list_loanGuaranteePledgeExtension){

                        this.add(new SignContractPostGuaranteeContractInformationPledgeInformation(){{

                            this.setZYHTKSRQ(DateUtil.date2Str(housingGuaranteeContract.getZyhtksrq(),format)/*质押合同开始日期*/);

                            this.setZYWSYQRSFHM(loanGuaranteePledgeExtension.getZywsyqrsfzhm()/*质押物所有权人身份证号码*/);

                            this.setZYWBM(housingGuaranteeContract.getZywbh()/*质押物编码*/);

                            this.setZYWSYQRXM(loanGuaranteePledgeExtension.getZywsyqrxm()/*质押物所有权人姓名*/);

                            this.setZYWJZ(loanGuaranteePledgeExtension.getZywjz()+""/*质押物价值*/);

                            this.setZYHTBH(housingGuaranteeContract.getZyhtbh()/*质押合同编号*/);

                            this.setZYWSYQRLXDH(loanGuaranteePledgeExtension.getZywsyqrlxdh()/*质押物所有权人联系电话*/);

                            this.setZYHTJSRQ(DateUtil.date2Str(housingGuaranteeContract.getZyhtjsrq(),format)/*质押合同结束日期*/);

                            this.setZYWMC(loanGuaranteePledgeExtension.getZywmc()/*质押物名称*/);

                            this.setId(loanGuaranteePledgeExtension.getId());

                        }});
                    }
                }});  //

                this.setDKDBLX(loanFundsInformationBasic.getDkdblx()/*贷款担保类型（0：抵押*/);

                this.setDBJGMC(housingGuaranteeContract.getDbjgmc()/*担保机构名称*/);

                this.setDBHTBH(housingGuaranteeContract.getDbhtbh()/*担保合同编号*/);

            }});

            this.setContractInformation(new SignContractPostContractInformation(){{

                this.setYDFKRQ(housingPersonalLoan == null? null : DateUtil.date2Str(housingPersonalLoan.getYdfkrq(),format)/*约定放款日期*/);

                this.setBuildInformation(loanHouseBuildBasic == null ? new SignContractPostContractInformationBuildInformation() : new SignContractPostContractInformationBuildInformation(){{

                    this.setFWJZMJ((housingPersonalLoan == null ? loanHouseBuildBasic.getJchjzmj():housingPersonalLoan.getFwjzmj())+""/*房屋建筑面积*/);//?

                    this.setGRSKYHZH(housingPersonalLoan == null ? loanHouseBuildBasic.getGrskyhzh():housingPersonalLoan.getcLoanHousingPersonalLoanExtension().getGrskyhzh()/*个人收款银行账号*/);

                    this.setFWZL(housingPersonalLoan == null ? loanHouseBuildBasic.getFwzl():housingPersonalLoan.getFwzl()/*房屋坐落*/);

                    this.setFWXZ(housingPersonalLoan == null ? loanHouseBuildBasic.getFwxz() : housingPersonalLoan.getFwxz()/*房屋性质（0：商品房*/);

                    this.setJKRZJHM(housingPersonalLoan == null ?loanHousingPersonInformationBasic.getJkrzjhm():housingPersonalLoan.getJkrzjh()/*借款人证件号码*/);

                    this.setKHYHMC(housingPersonalLoan == null ?loanHouseBuildBasic.getKhhyhmc():housingPersonalLoan.getcLoanHousingPersonalLoanExtension().getKhyhmc()/*开户银行名称*/);//?

                    this.setKHHM(housingPersonalLoan == null ?loanHouseBuildBasic.getYhkhm():housingPersonalLoan.getcLoanHousingPersonalLoanExtension().getKhhm()/*开户户名*/);//?

                    this.setDKLX(housingPersonalLoan == null ?loanFundsInformationBasic.getDklx():housingPersonalLoan.getDklx()/*贷款类型*/);

                    this.setDKQS(housingPersonalLoan == null ?loanFundsInformationBasic.getDkqs()+"":(housingPersonalLoan.getDkqs()+"")/*贷款期数*/);

                    this.setJKRXM(housingPersonalLoan == null ?loanHousingPersonInformationBasic.getJkrxm():housingPersonalLoan.getJkrxm()/*借款人姓名*/);

                    this.setGRSYZJ((housingPersonalLoan == null ?loanHouseBuildBasic.getGrsyzj():housingPersonalLoan.getcLoanHousingPersonalLoanExtension().getGrsyzj())+""/*个人使用资金*/);

                    this.setHTDKJE((housingPersonalLoan == null ?loanFundsInformationBasic.getHtdkje():housingPersonalLoan.getHtdkje())+""/*合同贷款金额*/);

                    this.setJKRGJJZH(housingPersonalLoan == null ?loanHousingPersonInformationBasic.getJkrgjjzh():housingPersonalLoan.getJkrgjjzh()/*借款人公积金账号*/);

                    this.setJKRZJLX(housingPersonalLoan == null ?loanHousingPersonInformationBasic.getJkrzjlx():housingPersonalLoan.getJkrzjlx()/*借款人证件类型*/);

                    //this.setSCZL(housingPersonalLoan == null ? null:loanHousingBusinessProcess.getBlzl());

                    this.setDKDBLX(housingPersonalLoan == null ?loanFundsInformationBasic.getDkdblx():housingPersonalLoan.getDkdblx()/*贷款担保类型*/);

                    this.setDKHKFS(housingPersonalLoan == null ?loanFundsInformationBasic.getHkfs():housingPersonalLoan.getDkhkfs()/*贷款还款方式*/);

                    this.setJHJZZJ((housingPersonalLoan == null ?loanHouseBuildBasic.getJhjzfy():housingPersonalLoan.getcLoanHousingPersonalLoanExtension().getJhjzzj())+""/*计划建造总价*/);

                }});

                this.setOverhaulInformation(loanHouseOverhaulBasic == null ? new SignContractPostContractInformationOverhaulInformation():new SignContractPostContractInformationOverhaulInformation(){{

                    this.setFWJZMJ((housingPersonalLoan == null ? loanHouseOverhaulBasic.getYjzmj()+"" :housingPersonalLoan.getFwjzmj())+""/*房屋建筑面积*/ );//?

                    this.setGRSKYHZH(housingPersonalLoan == null ? loanHouseOverhaulBasic.getGrskyhzh() :housingPersonalLoan.getcLoanHousingPersonalLoanExtension().getGrskyhzh()/*个人收款银行账号*/ );

                    this.setFWZL(housingPersonalLoan == null ? loanHouseOverhaulBasic.getFwzl() :housingPersonalLoan.getFwzl()/*房屋坐落*/ );

                    this.setFWXZ(housingPersonalLoan == null ? "99" :housingPersonalLoan.getFwxz()/*房屋性质（0：商品房*/ );

                    this.setJKRZJHM(housingPersonalLoan == null ? loanHousingPersonInformationBasic.getJkrzjhm() :housingPersonalLoan.getJkrzjh()/*借款人证件号码*/ );

                    this.setKHYHMC(housingPersonalLoan == null ? loanHouseOverhaulBasic.getKhyhmc() :housingPersonalLoan.getcLoanHousingPersonalLoanExtension().getKhyhmc()/*开户银行名称*/ );

                    this.setKHHM(housingPersonalLoan == null ? loanHouseOverhaulBasic.getYhkhm() :housingPersonalLoan.getcLoanHousingPersonalLoanExtension().getKhhm()/*开户户名*/ );//?

                    this.setJHFXFY(housingPersonalLoan == null ? loanHouseOverhaulBasic.getDxgcys()+"" :housingPersonalLoan.getcLoanHousingPersonalLoanExtension().getJhfxfy()+""/*计划翻修费用*/ );//?

                    this.setDKLX(housingPersonalLoan == null ? loanFundsInformationBasic.getDklx() :housingPersonalLoan.getDklx()/*贷款类型*/ );

                    this.setDKQS(housingPersonalLoan == null ? loanFundsInformationBasic.getDkqs()+"" :(housingPersonalLoan.getDkqs()+"")/*贷款期数*/ );

                    this.setJKRXM(housingPersonalLoan == null ? loanHousingPersonInformationBasic.getJkrxm() :housingPersonalLoan.getJkrxm()/*借款人姓名*/ );

                    this.setHTDKJE(housingPersonalLoan == null ? loanFundsInformationBasic.getHtdkje()+"" :housingPersonalLoan.getHtdkje()+""/*合同贷款金额*/ );

                    this.setJKRGJJZH(housingPersonalLoan == null ? loanHousingPersonInformationBasic.getJkrgjjzh() :housingPersonalLoan.getJkrgjjzh()/*借款人公积金账号*/ );

                    this.setJKRZJLX(housingPersonalLoan == null ? loanHousingPersonInformationBasic.getJkrzjlx() :housingPersonalLoan.getJkrzjlx()/*借款人证件类型*/ );

                    //this.setSCZL(housingPersonalLoan == null ? null :loanHousingBusinessProcess.getBlzl()/*上传资料*/);

                    this.setDKDBLX(housingPersonalLoan == null ? loanFundsInformationBasic.getDkdblx() :housingPersonalLoan.getDkdblx()/*贷款担保类型*/ );

                    this.setDKHKFS(housingPersonalLoan == null ? loanFundsInformationBasic.getHkfs() :housingPersonalLoan.getDkhkfs()/*贷款还款方式*/ );

                }});

                this.setPurchaseInformation(loanHousePurchasingBasic == null ? new SignContractPostContractInformationPurchaseInformation():new SignContractPostContractInformationPurchaseInformation(){{

                    boolean isSecondHands = loanHousePurchasingBasic.getSfwesf();

                    this.setFWJZMJ(housingPersonalLoan == null ? loanHousePurchasingBasic.getFwjzmj()+"" :housingPersonalLoan.getFwjzmj()+""/*房屋建筑面积*/ );

                    this.setSFRKHHM((isSecondHands?loanHousePurchasingBasic.getYhkhm():loanHousePurchasingBasic.getSfryhkhm())/*售房人开户户名*/ );

                    this.setFWZL(housingPersonalLoan == null ? loanHousePurchasingBasic.getFwzl() :housingPersonalLoan.getFwzl()/*房屋坐落*/ );

                    this.setFWTNMJ(housingPersonalLoan == null ? loanHousePurchasingBasic.getFwtnmj()+"" :housingPersonalLoan.getFwtnmj()+""/*房屋套内面积*/ );

                    this.setFWXZ(housingPersonalLoan == null ? loanHousePurchasingBasic.getFwxz() :housingPersonalLoan.getFwxz()/*房屋性质（0：商品房*/ );

                    this.setFWZJ(housingPersonalLoan == null ? loanHousePurchasingBasic.getFwzj()+"" :housingPersonalLoan.getFwzj()+""/*房屋总价*/ );

                    this.setJKRZJHM(housingPersonalLoan == null ? loanHousingPersonInformationBasic.getJkrzjhm() :housingPersonalLoan.getJkrzjh()/*借款人证件号码*/ );

                    this.setSFRKHYHMC(housingPersonalLoan == null ? (isSecondHands?loanHousePurchasingBasic.getKhyhmc():loanHousePurchasingBasic.getSfrkhyhmc()) :housingPersonalLoan.getSfrkhyhmc()/*售房人开户银行名称*/ );

                    this.setDKQS(housingPersonalLoan == null ? loanFundsInformationBasic.getDkqs()+"" :(housingPersonalLoan.getDkqs()+"")/*贷款期数*/ );

                    this.setGFSFK(housingPersonalLoan == null ? (loanHousePurchasingBasic.getSfwesf()?loanHousePurchasingBasic.getYfk():loanHousePurchasingBasic.getSfk())+"" :housingPersonalLoan.getGfsfk()+""/*购房首付款*/ );

                    this.setDKLX(housingPersonalLoan == null ? loanFundsInformationBasic.getDklx() :housingPersonalLoan.getDklx()/*贷款类型*/ );

                    this.setJKRXM(housingPersonalLoan == null ? loanHousingPersonInformationBasic.getJkrxm() :housingPersonalLoan.getJkrxm()/*借款人姓名*/ );

                    this.setHTDKJE(housingPersonalLoan == null ? loanFundsInformationBasic.getHtdkje()+"" :housingPersonalLoan.getHtdkje()+""/*合同贷款金额*/ );

                    this.setSFRZHHM(housingPersonalLoan == null ? (isSecondHands?loanHousePurchasingBasic.getGrskyhzh():loanHousePurchasingBasic.getSfrzhhm()) :housingPersonalLoan.getSfrzhhm()/*售房人账户号码*/ );

                    this.setSJHM(housingPersonalLoan == null ? loanHousingPersonInformationBasic.getSjhm() :housingPersonalLoan.getcLoanHousingPersonalLoanExtension().getSjhm()/*手机号码*/ );

                    this.setJKRGJJZH(housingPersonalLoan == null ? loanHousingPersonInformationBasic.getJkrgjjzh() :housingPersonalLoan.getJkrgjjzh()/*借款人公积金账号*/ );

                    this.setJKRZJLX(housingPersonalLoan == null ? loanHousingPersonInformationBasic.getJkrzjlx() :housingPersonalLoan.getJkrzjlx()/*借款人证件类型*/ );

                    this.setGFHTBH(housingPersonalLoan == null ? loanHousePurchasingBasic.getGfhtbh() :housingPersonalLoan.getGfhtbh()/*购房合同编号*/ );

                    //this.setSCZL(housingPersonalLoan == null ? null:loanHousingBusinessProcess.getBlzl());

                    this.setSFRMC(housingPersonalLoan == null ? loanHousePurchasingBasic.getSfrmc() :housingPersonalLoan.getSfrmc()/*售房人名称*/ );

                    this.setDKDBLX(housingPersonalLoan == null ? loanFundsInformationBasic.getDkdblx() :housingPersonalLoan.getDkdblx()/*贷款担保类型*/ );

                    this.setDKHKFS(housingPersonalLoan == null ? loanFundsInformationBasic.getHkfs() :housingPersonalLoan.getDkhkfs()/*贷款还款方式*/ );

                }});

                this.setLLFDBL(housingPersonalLoan == null ? (loanFundsInformationBasic.getLlfsbl()==null?0:loanFundsInformationBasic.getLlfsbl().multiply(new BigDecimal("1")))+"" :(housingPersonalLoan.getLlfdbl() == null ? 0 :housingPersonalLoan.getLlfdbl().multiply(new BigDecimal("1")))+""/*利率浮动比例*/ );

                this.setDKYT(housingPersonalLoan == null ? loanHousingPersonInformationBasic.getDkyt() :housingPersonalLoan.getcLoanHousingPersonalLoanExtension().getDkyt()/*贷款用途*/ );

                this.setYDHKR(housingPersonalLoan == null ? null : housingPersonalLoan.getYdhkr()/*约定还款日*/ );

                this.setYDDQRQ(housingPersonalLoan == null ? null :DateUtil.date2Str(housingPersonalLoan.getYddqrq(),format)/*约定到期日期*/ );

                this.setZHKHYHMC(housingPersonalLoan == null ? null :housingPersonalLoan.getZhkhyhmc()/*账号开户银行名称*/ );

                this.setSWTYHMC(housingPersonalLoan == null ? null :housingPersonalLoan.getSwtyhmc()/*受委托银行名称*/ );

                this.setSWTYHDM(housingPersonalLoan == null ? null  :housingPersonalLoan.getSwtyhdm()/*受委托银行代码*/ );

                this.setJKHTLL(housingPersonalLoan == null ? (loanFundsInformationBasic.getJkhtll()+""): housingPersonalLoan.getJkhtll()==null?null:(housingPersonalLoan.getJkhtll().doubleValue())+""/*借款合同利率*/ );

                this.setHKZH(housingPersonalLoan == null ? null  :housingPersonalLoan.getHkzh()/*还款账号*/ );

                this.setJKHTQDRQ(housingPersonalLoan == null ? null :DateUtil.date2Str(housingPersonalLoan.getJkhtqdrq(),format)/*借款合同签订日期*/ );

                this.setZHKHYHDM(housingPersonalLoan == null ? null :housingPersonalLoan.getZhkhyhdm()/*账号开户银行代码*/ );

                this.setHKZHHM(housingPersonalLoan == null ? null : housingPersonalLoan.getcLoanHousingPersonalLoanExtension().getHkzhhm()/*还款账号户名*/);

            }});

            this.setCZY(housingPersonalLoan == null ? null : loanHousingBusinessProcess.getCzy());

            this.setYWWD(loanHousingBusinessProcess.getYwwd().getMingCheng());

            this.setBLZL(housingPersonalLoan == null ? null:housingPersonalLoan.getBlzl());
        }};
    }


    public CommonResponses printContractPdf(TokenContext tokenContext, String YWLSH){

        CLoanHousingPersonInformationBasic loanHousingPersonInformationBasic = DAOBuilder.instance(this.loanHousingPersonInformationBasicDAO).searchFilter(new HashMap<String, Object>(){{

            this.put("ywlsh",YWLSH);

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e);}
        });

        CLoanHousingBusinessProcess loanHousingBusinessProcess = DAOBuilder.instance(this.loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("ywlsh", YWLSH);

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e); }

        });

        if (loanHousingBusinessProcess == null||loanHousingPersonInformationBasic == null) { throw new ErrorException(ReturnEnumeration.Data_MISS, "业务记录"); }

        //if(loanHousingPersonInformationBasic.p){ throw new ErrorException(ReturnEnumeration.Data_MISS,"业务记录");}
        CLoanHousePurchasingBasic loanHousePurchasingBasic = loanHousingPersonInformationBasic.getPurchasing();

        CLoanHouseBuildBasic loanHouseBuildBasic = loanHousingPersonInformationBasic.getBuild();

        CLoanHouseOverhaulBasic loanHouseOverhaulBasic = loanHousingPersonInformationBasic.getOverhaul();

        CLoanFundsInformationBasic loanFundsInformationBasic = loanHousingPersonInformationBasic.getFunds();

        StHousingGuaranteeContract housingGuaranteeContract = loanHousingPersonInformationBasic.getGuaranteeContract();

        List<CLoanGuaranteeMortgageExtension> list_loanGuaranteeMortgageExtension = housingGuaranteeContract.getcLoanGuaranteeMortgageExtensions();

        List<CLoanGuaranteeExtension> list_loanGuaranteeExtension = housingGuaranteeContract.getcLoanGuaranteeExtensions();

        List<CLoanGuaranteePledgeExtension> list_loanGuaranteePledgeExtension = housingGuaranteeContract.getcLoanGuaranteePledgeExtensions();

        StHousingPersonalLoan housingPersonalLoan = loanHousingPersonInformationBasic.getLoanContract();

        if(housingPersonalLoan == null||housingPersonalLoan.getcLoanHousingPersonalLoanExtension()==null){

            throw new ErrorException(ReturnEnumeration.Data_MISS,"合同信息");
        }

        String id = this.iPdfService.getContractRecepit(ResUtils.noneAdductionValue(LoanContractPDFResponse.class,new LoanContractPDFResponse(){{

            this.setOrignal(new SignContractPost(){{

                this.setGuaranteeContractInformation(new SignContractPostGuaranteeContractInformation(){{

                    this.setMortgageInformation(new ArrayList<SignContractPostGuaranteeContractInformationMortgageInformation>(){{

                        for (CLoanGuaranteeMortgageExtension loanGuaranteeMortgageExtension:list_loanGuaranteeMortgageExtension){

                            this.add(new SignContractPostGuaranteeContractInformationMortgageInformation(){{

                                this.setDYWQZH(housingGuaranteeContract.getDywqzh()/*抵押物权证号*/);

                                this.setDYWPGZ(loanGuaranteeMortgageExtension.getDywpgjz()+""/*抵押物评估值*/);

                                this.setDYQJLRQ(DateUtil.date2Str(housingGuaranteeContract.getDyqjlrq(),format)/*抵押权建立日期*/);

                                this.setDYWTXQZH(housingGuaranteeContract.getDywtxqzh()/*抵押物他项权证号*/);

                                this.setDYQJCRQ(DateUtil.date2Str(housingGuaranteeContract.getDyqjcrq(),format)/*抵押权解除日期*/);

                                this.setDYFWZL(loanGuaranteeMortgageExtension.getDywfwzl()/*抵押房屋坐落*/);

                                this.setId(loanGuaranteeMortgageExtension.getId());

                            }});
                        }
                    }});

                    this.setGuaranteeInformation(new ArrayList<SignContractPostGuaranteeContractInformationGuaranteeInformation>(){{

                        for(CLoanGuaranteeExtension loanGuaranteeExtension : list_loanGuaranteeExtension){

                            this.add(new SignContractPostGuaranteeContractInformationGuaranteeInformation(){{

                                this.setFHBZJRQ(DateUtil.date2Str(housingGuaranteeContract.getFhbzjrq(),format)/*返还保证金日期*/);

                                this.setBZHTBH(housingGuaranteeContract.getBzhtbh()/*保证合同编号*/);

                                this.setBZJGMC(housingGuaranteeContract.getBzjgmc()/*保证机构名称*/);

                                this.setDKBZJ(housingGuaranteeContract.getDkbzj()+""/*贷款保证金*/);

                                this.setId(loanGuaranteeExtension.getId());

                            }});
                        }
                    }});

                    this.setPledgeInformation(new ArrayList<SignContractPostGuaranteeContractInformationPledgeInformation>(){{

                        for(CLoanGuaranteePledgeExtension loanGuaranteePledgeExtension : list_loanGuaranteePledgeExtension){

                            this.add(new SignContractPostGuaranteeContractInformationPledgeInformation(){{

                                this.setZYHTKSRQ(DateUtil.date2Str(housingGuaranteeContract.getZyhtksrq())/*质押合同开始日期*/);

                                this.setZYWSYQRSFHM(loanGuaranteePledgeExtension.getZywsyqrsfzhm()/*质押物所有权人身份证号码*/);

                                this.setZYWBM(housingGuaranteeContract.getZywbh()/*质押物编码*/);

                                this.setZYWSYQRXM(loanGuaranteePledgeExtension.getZywsyqrxm()/*质押物所有权人姓名*/);

                                this.setZYWJZ(loanGuaranteePledgeExtension.getZywjz()+""/*质押物价值*/);

                                this.setZYHTBH(housingGuaranteeContract.getZyhtbh()/*质押合同编号*/);

                                this.setZYWSYQRLXDH(loanGuaranteePledgeExtension.getZywsyqrlxdh()/*质押物所有权人联系电话*/);

                                this.setZYHTJSRQ(DateUtil.date2Str(housingGuaranteeContract.getZyhtjsrq(),format)/*质押合同结束日期*/);

                                this.setZYWMC(loanGuaranteePledgeExtension.getZywmc()/*质押物名称*/);

                                this.setId(loanGuaranteePledgeExtension.getId());

                            }});
                        }
                    }});  //

                    this.setDKDBLX(loanFundsInformationBasic.getDkdblx()/*贷款担保类型（0：抵押*/);

                    this.setDBJGMC(housingGuaranteeContract.getDbjgmc()/*担保机构名称*/);

                    this.setDBHTBH(housingGuaranteeContract.getDbhtbh()/*担保合同编号*/);

                }});

                this.setContractInformation(new SignContractPostContractInformation(){{

                    this.setYDFKRQ(DateUtil.date2Str(housingPersonalLoan.getYdfkrq(),format)/*约定放款日期*/);

                    this.setBuildInformation(loanHouseBuildBasic == null ? new SignContractPostContractInformationBuildInformation() : new SignContractPostContractInformationBuildInformation(){{

                        this.setFWJZMJ(housingPersonalLoan.getFwjzmj() + ""/*房屋建筑面积*/);//?

                        this.setGRSKYHZH(housingPersonalLoan.getcLoanHousingPersonalLoanExtension().getGrskyhzh()/*个人收款银行账号*/);

                        this.setFWZL(housingPersonalLoan.getFwzl()/*房屋坐落*/);

                        this.setFWXZ(housingPersonalLoan.getFwxz()/*房屋性质（0：商品房*/);

                        this.setJKRZJHM(housingPersonalLoan.getJkrzjh()/*借款人证件号码*/);
                        CBankContract bankContract =DAOBuilder.instance(bankContractDAO).searchFilter(new HashMap<String, Object>(){{

                            this.put("yhmc",housingPersonalLoan.getcLoanHousingPersonalLoanExtension().getKhyhmc());
                           // this.put("yhdm",housingPersonalLoan.getSwtyhdm());

                        }}).getObject(new DAOBuilder.ErrorHandler() {
                            @Override
                            public void error(Exception e) { throw new ErrorException(e);}
                        });

                        if(bankContract == null){

                            bankContract = (CBankContract) ResUtils.autoNoneAdductionValue(new CBankContract());
                        }
                        this.setKHYHMC(BankName.BankNameWithCode(bankContract.getYhdm())/*开户银行名称*/);//?

                        this.setKHHM(housingPersonalLoan.getcLoanHousingPersonalLoanExtension().getKhhm()/*开户户名*/);//?

                        this.setDKLX(housingPersonalLoan.getDklx()/*贷款类型*/);

                        this.setDKQS((housingPersonalLoan.getDkqs()+"")/*贷款期数*/);

                        this.setJKRXM(housingPersonalLoan.getJkrxm()/*借款人姓名*/);

                        this.setGRSYZJ((housingPersonalLoan.getcLoanHousingPersonalLoanExtension().getGrsyzj())+""/*个人使用资金*/);

                        this.setHTDKJE((housingPersonalLoan.getHtdkje())+""/*合同贷款金额*/);

                        this.setJKRGJJZH(housingPersonalLoan.getJkrgjjzh()/*借款人公积金账号*/);

                        this.setJKRZJLX(housingPersonalLoan.getJkrzjlx()/*借款人证件类型*/);

                        //this.setSCZL(housingPersonalLoan.gets);

                        this.setDKDBLX(housingPersonalLoan.getDkdblx()/*贷款担保类型*/);

                        this.setDKHKFS(housingPersonalLoan.getDkhkfs()/*贷款还款方式*/);

                        this.setJHJZZJ((housingPersonalLoan.getcLoanHousingPersonalLoanExtension().getJhjzzj())+""/*计划建造总价*/);

                    }});

                    this.setOverhaulInformation(loanHouseOverhaulBasic == null ? new SignContractPostContractInformationOverhaulInformation():new SignContractPostContractInformationOverhaulInformation(){{

                        this.setFWJZMJ((housingPersonalLoan.getFwjzmj())+""/*房屋建筑面积*/ );//?

                        this.setGRSKYHZH(housingPersonalLoan.getcLoanHousingPersonalLoanExtension().getGrskyhzh()/*个人收款银行账号*/ );

                        this.setFWZL(housingPersonalLoan.getFwzl()/*房屋坐落*/ );

                        this.setFWXZ(housingPersonalLoan.getFwxz()/*房屋性质（0：商品房*/ );

                        this.setJKRZJHM(housingPersonalLoan.getJkrzjh()/*借款人证件号码*/ );

                        CBankContract bankContract =DAOBuilder.instance(bankContractDAO).searchFilter(new HashMap<String, Object>(){{

                            this.put("yhmc",housingPersonalLoan.getcLoanHousingPersonalLoanExtension().getKhyhmc());
                            // this.put("yhdm",housingPersonalLoan.getSwtyhdm());

                        }}).getObject(new DAOBuilder.ErrorHandler() {
                            @Override
                            public void error(Exception e) { throw new ErrorException(e);}
                        });

                        if(bankContract == null){

                            bankContract = (CBankContract) ResUtils.autoNoneAdductionValue(new CBankContract());
                        }
                        this.setKHYHMC(BankName.BankNameWithCode(bankContract.getYhdm())/*开户银行名称*/);//?

                        this.setKHHM(housingPersonalLoan.getcLoanHousingPersonalLoanExtension().getKhhm()/*开户户名*/ );//?

                        this.setJHFXFY(housingPersonalLoan.getcLoanHousingPersonalLoanExtension().getJhfxfy()+""/*计划翻修费用*/ );//?

                        this.setDKLX(housingPersonalLoan.getDklx()/*贷款类型*/ );

                        this.setDKQS((housingPersonalLoan.getDkqs()+"")/*贷款期数*/ );

                        this.setJKRXM(housingPersonalLoan.getJkrxm()/*借款人姓名*/ );

                        this.setHTDKJE(housingPersonalLoan.getHtdkje()+""/*合同贷款金额*/ );

                        this.setJKRGJJZH(housingPersonalLoan.getJkrgjjzh()/*借款人公积金账号*/ );

                        this.setJKRZJLX(housingPersonalLoan.getJkrzjlx()/*借款人证件类型*/ );

                        this.setSCZL(loanHousingBusinessProcess.getBlzl()/*上传资料*/);

                        this.setDKDBLX(housingPersonalLoan.getDkdblx()/*贷款担保类型*/ );

                        this.setDKHKFS(housingPersonalLoan.getDkhkfs()/*贷款还款方式*/ );

                    }});

                    this.setPurchaseInformation(loanHousePurchasingBasic == null ? new SignContractPostContractInformationPurchaseInformation():new SignContractPostContractInformationPurchaseInformation(){{

                        this.setFWJZMJ(housingPersonalLoan.getFwjzmj()+""/*房屋建筑面积*/ );

                        this.setSFRKHHM((loanHousePurchasingBasic.getSfwesf()?loanHousePurchasingBasic.getYhkhm():loanHousePurchasingBasic.getSfryhkhm())/*售房人开户户名*/ );

                        this.setFWZL(housingPersonalLoan.getFwzl()/*房屋坐落*/ );

                        this.setFWTNMJ(housingPersonalLoan.getFwtnmj()+""/*房屋套内面积*/ );

                        this.setFWXZ(housingPersonalLoan.getFwxz()/*房屋性质（0：商品房*/ );

                        this.setFWZJ(housingPersonalLoan.getFwzj()+""/*房屋总价*/ );

                        this.setJKRZJHM(housingPersonalLoan.getJkrzjh()/*借款人证件号码*/ );

                        CBankContract bankContract =DAOBuilder.instance(bankContractDAO).searchFilter(new HashMap<String, Object>(){{

                            this.put("yhmc",housingPersonalLoan.getSfrkhyhmc());
                            // this.put("yhdm",housingPersonalLoan.getSwtyhdm());

                        }}).getObject(new DAOBuilder.ErrorHandler() {
                            @Override
                            public void error(Exception e) { throw new ErrorException(e);}
                        });

                        if(bankContract == null){

                            bankContract = (CBankContract) ResUtils.autoNoneAdductionValue(new CBankContract());
                        }
                        this.setSFRKHYHMC(BankName.BankNameWithCode(bankContract.getYhdm())/*售房人开户银行名称*/ );

                        this.setDKQS((housingPersonalLoan.getDkqs()+"")/*贷款期数*/ );

                        this.setGFSFK(housingPersonalLoan.getGfsfk()+""/*购房首付款*/ );

                        this.setDKLX(housingPersonalLoan.getDklx()/*贷款类型*/ );

                        this.setJKRXM(housingPersonalLoan.getJkrxm()/*借款人姓名*/ );

                        this.setHTDKJE(housingPersonalLoan.getHtdkje()+""/*合同贷款金额*/ );

                        this.setSFRZHHM(housingPersonalLoan.getSfrzhhm()/*售房人账户号码*/ );

                        this.setSJHM(housingPersonalLoan.getcLoanHousingPersonalLoanExtension().getSjhm()/*手机号码*/ );

                        this.setJKRGJJZH(housingPersonalLoan.getJkrgjjzh()/*借款人公积金账号*/ );

                        this.setJKRZJLX(housingPersonalLoan.getJkrzjlx()/*借款人证件类型*/ );

                        this.setGFHTBH(housingPersonalLoan.getGfhtbh()/*购房合同编号*/ );

                        //this.setSCZL(loanHousePurchasingBasic);  //

                        this.setSFRMC(housingPersonalLoan.getSfrmc()/*售房人名称*/ );

                        this.setDKDBLX(housingPersonalLoan.getDkdblx()/*贷款担保类型*/ );

                        this.setDKHKFS(housingPersonalLoan.getDkhkfs()/*贷款还款方式*/ );

                    }});

                    this.setLLFDBL((housingPersonalLoan.getLlfdbl() == null ? "0" : housingPersonalLoan.getLlfdbl().multiply(new BigDecimal("1")))+""/*利率浮动比例*/ );

                    this.setDKYT(housingPersonalLoan.getcLoanHousingPersonalLoanExtension().getDkyt()/*贷款用途*/ );

                    this.setYDHKR( housingPersonalLoan.getYdhkr()/*约定还款日*/ );

                    this.setYDDQRQ(DateUtil.date2Str(housingPersonalLoan.getYddqrq(),format)/*约定到期日期*/ );

                    this.setZHKHYHMC(housingPersonalLoan.getZhkhyhmc()/*账号开户银行名称*/ );

                    this.setSWTYHMC(BankName.BankNameWithCode(housingPersonalLoan.getSwtyhdm())/*受委托银行名称*/ );

                    this.setSWTYHDM(housingPersonalLoan.getSwtyhdm()/*受委托银行代码*/ );

                    this.setJKHTLL((housingPersonalLoan.getJkhtll().doubleValue())+""/*借款合同利率*/ );

                    this.setHKZH(housingPersonalLoan.getHkzh()/*还款账号*/ );

                    this.setJKHTQDRQ(DateUtil.date2Str(housingPersonalLoan.getJkhtqdrq(),format)/*借款合同签订日期*/ );

                    this.setZHKHYHDM(housingPersonalLoan.getZhkhyhdm()/*账号开户银行代码*/ );

                }});

                this.setCZY( loanHousingBusinessProcess.getCzy());
            }});

            this.setSpbbh(loanHousingPersonInformationBasic.getYwlsh());

            this.setApplicationDetails(loanRecordService.getLoanDetails(loanHousingPersonInformationBasic.getDkzh()));

            CBankContract bankContract =DAOBuilder.instance(bankContractDAO).searchFilter(new HashMap<String, Object>(){{

                this.put("yhmc",housingPersonalLoan.getSwtyhmc());
                this.put("yhdm",housingPersonalLoan.getSwtyhdm());

            }}).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) { throw new ErrorException(e);}
            });

            if(bankContract == null){

                bankContract = (CBankContract) ResUtils.autoNoneAdductionValue(new CBankContract());
            }
            this.setDKRTXDZ(bankContract.getWddz());/*贷款人通讯地址*/;
            this.setDKYHMC(BankName.BankNameWithCode(bankContract.getYhdm()));/*贷款银行名称?*/;
            this.setDKYHQC(BankName.BankNameWithCode(bankContract.getYhdm()));
            this.setLXFS(bankContract.getLxdh());/*贷款人联系方式*/
            BigDecimal dqje = CommLoanAlgorithm.currentBX(loanFundsInformationBasic.getHtdkje(), loanFundsInformationBasic.getDkqs()==null?0:loanFundsInformationBasic.getDkqs().intValue(), loanFundsInformationBasic.getHkfs(), loanFundsInformationBasic.getJkhtll(), 1/*当期应还金额*/);
            BigDecimal dqlx = CommLoanAlgorithm.overdueThisPeriodLX(loanFundsInformationBasic.getHtdkje(), 1, loanFundsInformationBasic.getHkfs(), loanFundsInformationBasic.getJkhtll(), loanFundsInformationBasic.getDkqs() == null ? 0 :loanFundsInformationBasic.getDkqs().intValue()/*当前应还利息*/);
            BigDecimal dqbj = dqje.subtract(dqlx/*当前应还本金*/);
            this.setGHBJJE(dqje.setScale(2,BigDecimal.ROUND_HALF_UP)+"");
            this.setYHKE(dqbj.setScale(2,BigDecimal.ROUND_HALF_UP)+"");
        }}));

        housingPersonalLoan.getcLoanHousingPersonalLoanExtension().setDkhtwj(id);

        DAOBuilder.instance(this.housingPersonalLoanDAO).entity(housingPersonalLoan).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) { throw new ErrorException(e); }
        });

        return new CommonResponses() {{

            this.setId(id);

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
      *
      * ~swagger未分页
      *
      * */
    @Override
    public PageRes<SignContractResponse> getSignContractList(TokenContext tokenContext,String YWZT,String JE,String JKRXM, String JKRZJHM, String DKYT,String YHDM,String YWWD, String pageSize, String page,String KSSJ,String JSSJ) {



        if(!StringUtil.isDigits(page,true)||!StringUtil.isDigits(pageSize,true)){

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "页码");
        }

        PageRes pageRes = new PageRes();

        List<CLoanHousingBusinessProcess> list_process = DAOBuilder.instance(this.loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{

            if (StringUtil.notEmpty(JKRXM)) { this.put("loanHousingPersonInformationVice.jkrxm", JKRXM); }

            if (StringUtil.notEmpty(JKRZJHM)) { this.put("loanHousingPersonInformationVice.jkrzjhm", JKRZJHM); }

        }}).searchOption(SearchOption.FUZZY).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {

                if (DateUtil.isFollowFormat(KSSJ,formatNYRSF,false)){ criteria.add(Restrictions.ge("created_at",DateUtil.safeStr2Date(formatNYRSF,KSSJ)));}
                if (DateUtil.isFollowFormat(JSSJ,formatNYRSF,false)){ criteria.add(Restrictions.lt("created_at",DateUtil.safeStr2Date(formatNYRSF,JSSJ)));}

                if (StringUtil.notEmpty(DKYT) && !DKYT.equals("00")) { criteria.add(Restrictions.eq("dkyt", DKYT)); }


                criteria.add(Restrictions.eq("cznr", LoanBusinessType.贷款发放.getCode()));

                criteria.add(Restrictions.or(Restrictions.isNotNull("loanContract"),Restrictions.in("step",LoanBussinessStatus.待签合同.getName(),LoanBussinessStatus.已作废.getName())));

                if(StringUtil.notEmpty(YWZT)) {
                    criteria.add(Restrictions.eq("step", YWZT));
                }
                criteria.createAlias("loanContract","loanContract", JoinType.LEFT_OUTER_JOIN);
                if(StringUtil.isDigits(JE,false)){
                    
                    criteria.add(Restrictions.eq("loanContract.htdkje",new BigDecimal(JE)));
                }
                if(StringUtil.notEmpty(YHDM)) {
                    List<String> list_bankInfo = CollectionUtils.flatmap(DAOBuilder.instance(bankBankInfoDAO).searchFilter(new HashMap<String, Object>() {{

                        this.put("code", YHDM);

                    }}).getList(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) { throw new ErrorException(e); }

                    }), new CollectionUtils.Transformer<CBankBankInfo, String>() {
                        @Override
                        public String tansform(CBankBankInfo var1) { return var1.getBank_name(); }
                    });
                    criteria.add(Restrictions.in("loanContract.swtyhmc", (Collection)list_bankInfo));
                }
                if(StringUtil.notEmpty(YWWD)){

                    criteria.createAlias("ywwd","ywwd");
                    criteria.add(Restrictions.eq("ywwd.id",YWWD));
                }

            }

        }).pageOption(pageRes, StringUtil.safeBigDecimal(pageSize).intValue(), StringUtil.safeBigDecimal(page).intValue()).searchOption(SearchOption.FUZZY).getList(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e); }

        });

        List<CLoanHousingLoan> list_loan = CollectionUtils.filter(list_process, new CollectionUtils.Predicate<CLoanHousingBusinessProcess>() {

            @Override
            public boolean evaluate(CLoanHousingBusinessProcess var1) { return var1.getStep().contains("失败"); }

        }).size() == 0 ? new ArrayList<>():DAOBuilder.instance(this.loanHousingLoanDAO).searchFilter(new HashMap<String, Object>(){{

            this.put("ywlsh",CollectionUtils.flatmap(CollectionUtils.filter(list_process, new CollectionUtils.Predicate<CLoanHousingBusinessProcess>() {

                @Override
                public boolean evaluate(CLoanHousingBusinessProcess var1) { return var1.getStep().contains("失败"); }

            }),new CollectionUtils.Transformer<CLoanHousingBusinessProcess,String>(){

                @Override
                public String tansform(CLoanHousingBusinessProcess var1) { return var1.getYwlsh(); }

            }));

        }}).getList(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e); }
        });

        return new PageRes<SignContractResponse>() {{

            this.setResults(new ArrayList<SignContractResponse>() {{

                for (CLoanHousingBusinessProcess process : list_process) {

                    this.add(new SignContractResponse() {{

                        this.setSBYY(CollectionUtils.find(list_loan, new CLoanHousingLoan(), new CollectionUtils.Predicate<CLoanHousingLoan>() {

                            @Override
                            public boolean evaluate(CLoanHousingLoan var1) { return process.getYwlsh().equals(var1.getYwlsh()); }

                        }).getSbyy());

                        this.setId(process.getId());
                        this.setYWLSH(process.getYwlsh()/*业务流水号*/);
                        this.setSLSJ(DateUtil.date2Str(process.getCreated_at(), formatM));
                        this.setZhuangTai(process.getStep()/*状态*/);
                        this.setCZY(process.getCzy());
                        this.setFWZJ(new HashMap<String, String>() {{

                            this.put("0", process.getLoanHousePurchasingVice() == null ? null : (process.getLoanHousePurchasingVice().getFwzj() + ""));
                            this.put("1", process.getLoanHouseBuildVice() == null ? null : (process.getLoanHouseBuildVice().getJhjzfy() + ""));
                            this.put("2", process.getLoanHouseOverhaulVice() == null ? null : (process.getLoanHouseOverhaulVice().getDxgcys() + ""));

                        }}.get(process.getDkyt())/*房屋总价/计划费用*/);

                        if (process.getLoanHousingPersonInformationVice() != null) {

                            this.setJKRZJHM(process.getLoanHousingPersonInformationVice().getJkrzjhm()/*借款人证件号码*/);
                            this.setJKRXM(process.getLoanHousingPersonInformationVice().getJkrxm()/*借款人姓名*/);
                        }
                        this.setDKYT(process.getDkyt()/*贷款用途*/);

                        if (process.getLoanFundsVice() != null) {

                            this.setDKQS(process.getLoanFundsVice().getDkqs() + ""/*贷款期数*/);
                            this.setHTDKJE(process.getLoanFundsVice().getHtdkje() + ""/*合同贷款金额*/);
                            this.setHTDKJE(process.getLoanFundsVice().getHtdkje() + ""/*合同贷款金额*/);
                        }

                        this.setFFDKYH(process.getLoanContract()==null?null:process.getLoanContract().getSwtyhmc());

                        this.setYWWD(process.getYwwd() == null ? null :process.getYwwd().getMingCheng());
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
    public PageResNew<SignContractResponse> getSignContractList(TokenContext tokenContext, String JKRXM, String JKRZJHM, String DKYT, String YHMC,String YWWD, String pageSize, String marker, String action, String KSSJ, String JSSJ) {

        if(!StringUtil.isDigits(pageSize,true)){ throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "页码"); }

        List<CLoanHousingBusinessProcess> list_process = DAOBuilder.instance(this.loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{

            if (StringUtil.notEmpty(JKRXM)) { this.put("loanHousingPersonInformationVice.jkrxm", JKRXM); }

            if (StringUtil.notEmpty(JKRZJHM)) { this.put("loanHousingPersonInformationVice.jkrzjhm", JKRZJHM); }

        }}).searchOption(SearchOption.FUZZY).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {

                if (DateUtil.isFollowFormat(KSSJ,formatNYRSF,false)){ criteria.add(Restrictions.ge("created_at",DateUtil.safeStr2Date(formatNYRSF,KSSJ)));}
                if (DateUtil.isFollowFormat(JSSJ,formatNYRSF,false)){ criteria.add(Restrictions.lt("created_at",DateUtil.safeStr2Date(formatNYRSF,JSSJ)));}

                if (StringUtil.notEmpty(DKYT) && !DKYT.equals("00")) { criteria.add(Restrictions.eq("dkyt", DKYT)); }

                criteria.add(Restrictions.eq("cznr", LoanBusinessType.贷款发放.getCode()));

                criteria.add(Restrictions.or(Restrictions.isNotNull("loanContract"),Restrictions.in("step",LoanBussinessStatus.待签合同.getName(),LoanBussinessStatus.已作废.getName())));

                criteria.createAlias("loanContract","loanContract", JoinType.LEFT_OUTER_JOIN);

                if(StringUtil.notEmpty(YHMC)) {
                    criteria.add(Restrictions.eq("loanContract.swtyhmc", YHMC));
                }
                if(StringUtil.notEmpty(YWWD)){

                    criteria.createAlias("ywwd","ywwd");
                    criteria.add(Restrictions.eq("ywwd.id",YWWD));
                }

            }
        }).pageOption(marker,action, StringUtil.safeBigDecimal(pageSize).intValue()).searchOption(SearchOption.FUZZY).getList(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e); }

        });

        return new PageResNew<SignContractResponse>() {{

            this.setResults(action,new ArrayList<SignContractResponse>() {{

                for (CLoanHousingBusinessProcess process : list_process) {

                    this.add(new SignContractResponse() {{

                        this.setId(process.getId());
                        this.setYWLSH(process.getYwlsh()/*业务流水号*/);

                        this.setSLSJ(DateUtil.date2Str(process.getCreated_at(), formatM));

                        this.setZhuangTai(process.getStep()/*状态*/);
                        this.setFWZJ(new HashMap<String, String>() {{

                            this.put("0", process.getLoanHousePurchasingVice() == null ? null : (process.getLoanHousePurchasingVice().getFwzj() + ""));
                            this.put("1", process.getLoanHouseBuildVice() == null ? null : (process.getLoanHouseBuildVice().getJhjzfy() + ""));
                            this.put("2", process.getLoanHouseOverhaulVice() == null ? null : (process.getLoanHouseOverhaulVice().getDxgcys() + ""));

                        }}.get(process.getDkyt())/*房屋总价/计划费用*/);

                        if (process.getLoanHousingPersonInformationVice() != null) {

                            this.setJKRZJHM(process.getLoanHousingPersonInformationVice().getJkrzjhm()/*借款人证件号码*/);
                            this.setJKRXM(process.getLoanHousingPersonInformationVice().getJkrxm()/*借款人姓名*/);
                        }
                        this.setDKYT(process.getDkyt()/*贷款用途*/);

                        if (process.getLoanFundsVice() != null) {

                            this.setDKQS(process.getLoanFundsVice().getDkqs() + ""/*贷款期数*/);
                            this.setHTDKJE(process.getLoanFundsVice().getHtdkje() + ""/*合同贷款金额*/);
                            this.setHTDKJE(process.getLoanFundsVice().getHtdkje() + ""/*合同贷款金额*/);
                        }

                        this.setFFDKYH(process.getLoanContract()==null?null:process.getLoanContract().getSwtyhmc());
                    }});
                }

            }});

        }};
    }


    private void checkContractAvailable(StHousingPersonalLoan housingPersonalLoan){


        if(!StringUtil.notEmpty(housingPersonalLoan.getSwtyhdm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"受委托银行代码"); }

        if(housingPersonalLoan.getSwtyhdm().length()!=3){
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"受委托银行代码");
        }
        if(!StringUtil.notEmpty(housingPersonalLoan.getDkdblx())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"贷款担保类型");}

        if(housingPersonalLoan.getDkdblx().length()!=2){

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"贷款担保类型");
        }

        if(!StringUtil.notEmpty(housingPersonalLoan.getDkhkfs())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"还款方式");}

        if(!CollectionUtils.flatmap(iDictionaryService.getDictionaryByType("LoanPaymentMode"), new CollectionUtils.Transformer<CommonDictionary, String>() {
            @Override
            public String tansform(CommonDictionary var1) { return var1.getCode(); }

        }).contains((housingPersonalLoan.getDkhkfs()))){

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"还款方式");
        }

        if(!StringUtil.notEmpty((housingPersonalLoan.getDklx()))){ throw new ErrorException(ReturnEnumeration.Data_MISS,"贷款类型");}

        if(housingPersonalLoan.getDklx().length()!=2){

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"贷款类型");
        }

        if(!StringUtil.notEmpty(housingPersonalLoan.getJkrzjlx())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"借款人证件类型 ");}

        if(!CollectionUtils.flatmap(iDictionaryService.getDictionaryByType("PersonalCertificate"), new CollectionUtils.Transformer<CommonDictionary, String>() {
            @Override
            public String tansform(CommonDictionary var1) { return var1.getCode(); }

        }).contains(housingPersonalLoan.getJkrzjlx())){

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"借款人证件类型");
        }

        if(!StringUtil.notEmpty(housingPersonalLoan.getJkrzjh())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"借款人证件号码");}

        if(PersonCertificateType.身份证.getCode().equals(housingPersonalLoan.getJkrzjlx())&&!IdcardValidator.isValidatedAllIdcard(housingPersonalLoan.getJkrzjh())){

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"借款人证件号码");
        }

        if(housingPersonalLoan.getJkhtll()==null){ throw new ErrorException(ReturnEnumeration.Data_MISS,"借款合同利率");}

        if(housingPersonalLoan.getJkhtll().compareTo(BigDecimal.ZERO)<=0){

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"借款合同利率");
        }

        if(housingPersonalLoan.getLlfdbl()==null){ throw new ErrorException(ReturnEnumeration.Data_MISS,"利率浮动比例");}

        if(housingPersonalLoan.getLlfdbl().compareTo(BigDecimal.ZERO)<=0){

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"利率浮动比例");
        }

        if(!StringUtil.notEmpty(housingPersonalLoan.getJkrxm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"借款人姓名");}

        if(housingPersonalLoan.getJkrxm().length()<2){

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"借款人姓名");
        }

        if(!StringUtil.notEmpty(housingPersonalLoan.getYdhkr())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"约定还款日");}

        if(housingPersonalLoan.getYdhkr().length()!=2||housingPersonalLoan.getYdhkr().compareTo("01")<0 || housingPersonalLoan.getYdhkr().compareTo("31")>0){

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"约定还款日");
        }


        if (!StringUtil.notEmpty(housingPersonalLoan.getFwzl())) { throw new ErrorException(ReturnEnumeration.Data_MISS, "房屋坐落"); }

        if (housingPersonalLoan.getFwzl().length() < 10) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "房屋坐落长度不能小于10");
        }

        if (housingPersonalLoan.getFwjzmj() == null) { throw new ErrorException(ReturnEnumeration.Data_MISS, "房屋建筑面积"); }

        if (housingPersonalLoan.getFwjzmj().compareTo(new BigDecimal("9")) < 0) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "建筑面积过小");
        }

        if (housingPersonalLoan.getFwtnmj() == null) { throw new ErrorException(ReturnEnumeration.Data_MISS, "套内建筑面积"); }

        if (housingPersonalLoan.getFwtnmj().compareTo(new BigDecimal("9")) < 0) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "套内面积过小");
        }


        if (housingPersonalLoan.getFwzj() == null) { throw new ErrorException(ReturnEnumeration.Data_MISS, "房屋总价"); }

        if (housingPersonalLoan.getFwzj().compareTo(new BigDecimal("10000")) < 0) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "房屋总价过小");
        }

        if (housingPersonalLoan.getGfsfk() == null) { throw new ErrorException(ReturnEnumeration.Data_MISS, "首付款"); }

        if (housingPersonalLoan.getGfsfk().compareTo(BigDecimal.ZERO) <= 0) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "首付款过小");
        }

        if(!StringUtil.notEmpty(housingPersonalLoan.getSfrmc())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"售房人名称");}

        if(housingPersonalLoan.getSfrmc().length()<2){

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"售房人名称");

        }

        if(!StringUtil.notEmpty(housingPersonalLoan.getSfrzhhm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"售房人账户号码");}

        if(housingPersonalLoan.getSfrzhhm().length()<=4){
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"售房人账户号码");
        }

        if(!StringUtil.notEmpty(housingPersonalLoan.getSfrkhyhmc())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"售房人开户银行名称");}

        if(housingPersonalLoan.getSfrkhyhmc().length()<=4){
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"售房人开户银行名称");
        }

        if(!StringUtil.notEmpty(housingPersonalLoan.getSfrkhyhdm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"售房人开户银行代码");}

        if(housingPersonalLoan.getSfrkhyhdm().length()!=3){
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"售房人开户银行代码");
        }

        if(!StringUtil.notEmpty(housingPersonalLoan.getFwxz())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"房屋性质");}

        if(housingPersonalLoan.getFwxz().length()!=2){
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"房屋性质");
        }

        if(housingPersonalLoan.getHtdkje().divide(housingPersonalLoan.getFwzj(),1,BigDecimal.ROUND_HALF_UP).doubleValue()>0.8){

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"合同贷款金额不能超过房屋总价的80%");
        }

        if(housingPersonalLoan.getGfsfk().divide(housingPersonalLoan.getFwzj(),1,BigDecimal.ROUND_DOWN).doubleValue()<0.2){

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"购房首付款不能低于房屋总价的20%");
        }

        if(housingPersonalLoan.getGfsfk().add(housingPersonalLoan.getHtdkje()).compareTo(housingPersonalLoan.getFwzj())>0){

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"购房首付款与合同贷款金额之和不能超过房屋总价");
        }

        if(housingPersonalLoan.getDkqs().compareTo(new BigDecimal("360"))>0){

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"贷款期数不能超过360");
        }

        //region//个人信息合规检查
        StCommonPerson commonPerson_zjh = DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("zjhm", housingPersonalLoan.getJkrzjh());

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

        if(commonPerson_zjh!=null){

            if(commonPerson_zjh.getXingMing()!=null&&commonPerson_zjh.getZjhm()!=null&&commonPerson_zjh.getZjhm().length()>5&&!commonPerson_zjh.getXingMing().equals(housingPersonalLoan.getJkrxm())){

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"个人信息与系统保存的信息不一致");
            }
        }

        StCommonPerson commonPerson_grzh = DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("grzh", housingPersonalLoan.getJkrgjjzh());

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

        if(commonPerson_grzh!=null){

            if(commonPerson_grzh.getGrzh()!=null&&commonPerson_grzh.getGrzh()!=null&&commonPerson_grzh.getGrzh().length()>5&&!commonPerson_grzh.getXingMing().equals(housingPersonalLoan.getJkrxm())){

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"个人信息与系统保存的信息不一致");
            }
        }

        StCommonPerson commonPerson_xingming = DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("grzh", housingPersonalLoan.getJkrgjjzh());

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

        if(commonPerson_xingming!=null){

            if(commonPerson_xingming.getGrzh()!=null&&commonPerson_xingming.getGrzh()!=null&&commonPerson_xingming.getGrzh().length()>5&&!commonPerson_xingming.getZjhm().equals(housingPersonalLoan.getJkrzjh())){

                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"个人信息与系统保存的信息不一致");
            }
        }

        //endregion
    }
}
