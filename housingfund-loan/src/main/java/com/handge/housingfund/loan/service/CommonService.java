package com.handge.housingfund.loan.service;

import com.handge.housingfund.common.service.ISaveAuditHistory;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessStatus;
import com.handge.housingfund.common.service.collection.model.deposit.BatchSubmission;
import com.handge.housingfund.common.service.loan.*;
import com.handge.housingfund.common.service.loan.enums.LoanBusinessType;
import com.handge.housingfund.common.service.loan.enums.LoanBussinessStatus;
import com.handge.housingfund.common.service.loan.model.CommonResponses;
import com.handge.housingfund.common.service.loan.model.DelList;
import com.handge.housingfund.common.service.loan.model.MultipleTestRes;
import com.handge.housingfund.common.service.others.IUploadImagesService;
import com.handge.housingfund.common.service.others.enums.UploadFileBusinessType;
import com.handge.housingfund.common.service.others.enums.UploadFileModleType;
import com.handge.housingfund.common.service.review.model.MultiReviewConfig;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.NormalJsonUtils;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.ICLoanHousingBusinessProcessDAO;
import com.handge.housingfund.database.dao.ICLoanHousingPersonInformationBasicDAO;
import com.handge.housingfund.database.dao.ICStateMachineConfigurationDAO;
import com.handge.housingfund.database.dao.IStHousingPersonalLoanDAO;
import com.handge.housingfund.database.entities.CLoanHousingBusinessProcess;
import com.handge.housingfund.database.entities.CLoanHousingPersonInformationBasic;
import com.handge.housingfund.database.enums.BusinessSubType;
import com.handge.housingfund.database.enums.BusinessType;
import com.handge.housingfund.database.enums.Events;
import com.handge.housingfund.database.enums.ListDeleted;
import com.handge.housingfund.database.utils.DAOBuilder;
import com.handge.housingfund.loan.utils.StateMachineUtils;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import com.handge.housingfund.statemachineV2.IStateMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by Liujuhao on 2017/9/1.
 */

// TODO: 2017/9/1 把公共操作（例如删除、撤回等等）写到此类中 

@SuppressWarnings("Convert2Lambda")
@Component
public class CommonService implements ICommonService {

    @Autowired
    protected ICStateMachineConfigurationDAO icStateMachineConfigurationDAO;


    @Autowired
    private ICLoanHousingBusinessProcessDAO loanHousingBusinessProcessDAO;
    @Autowired
    ICLoanHousingBusinessProcessDAO cloanHousingBusinessProcess;
    @Autowired
    private  ISaveAuditHistory iSaveAuditHistory;
    @Autowired
    private IUploadImagesService iUploadImagesService;
    @Autowired
    @Qualifier("housingCompanyImpl")
    private IHousingCompany iHousingCompany;
    @Autowired
    @Qualifier("housingCompanyAlter")
    private IHousingCompanyAlter ihousingCompanyAlter;
    @Autowired
    @Qualifier("estateProjectImpl")
    private IEstateProject iestateProject;
    @Autowired
    @Qualifier("estateProjectAlter")
    private IEstateProjectAlter iestateProjectAlter;
    @Autowired
    @Resource(name = "stateMachineServiceV2")
    private IStateMachineService iStateMachineService;

    @Autowired
    private ICLoanHousingPersonInformationBasicDAO loanHousingPersonInformationBasicDAO;

    private IStHousingPersonalLoanDAO personalLoanDAO;

    /*
    *  completed
    *
    * !逻辑已完成
    *
    * 已测试 tanyi
    *
    * */
    @Override
    public CommonResponses deleteApplication(TokenContext tokenContext,String YWWD, String CZY, DelList body) {

        for (String ywlsh : body.getDellist()) {

            CLoanHousingBusinessProcess cLoanHousingBusinessProcess = DAOBuilder.instance(this.loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{

                this.put("ywlsh", ywlsh);

            }}).getObject(new DAOBuilder.ErrorHandler() {

                @Override
                public void error(Exception e) { throw new ErrorException(e); }

            });
            
            if(cLoanHousingBusinessProcess == null){

                return  new CommonResponses(){{

                    this.setId(ywlsh);
                    this.setState("error");
                }};
            }
            if(!tokenContext.getUserInfo().getYWWD().equals(cLoanHousingBusinessProcess.getYwwd().getId())||!tokenContext.getUserInfo().getCZY().equals(cLoanHousingBusinessProcess.getCzy())){
                throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务(" + ywlsh + ")不是由您受理的，不能删除");
            }

            if(cLoanHousingBusinessProcess.getCznr().equals(LoanBusinessType.结清.getCode())||cLoanHousingBusinessProcess.getCznr().equals(LoanBusinessType.提前还款.getCode())){
                if(!cLoanHousingBusinessProcess.getStep().equals(LoanBussinessStatus.入账失败.getName())&&
                        !cLoanHousingBusinessProcess.getStep().equals(LoanBussinessStatus.扣款已发送.getName())&&
                        !cLoanHousingBusinessProcess.getStep().equals(LoanBussinessStatus.已入账.getName())
                        ){
                    cLoanHousingBusinessProcess.setDeleted(true);
                    cLoanHousingBusinessProcess.setStep(LoanBussinessStatus.已作废.getName());
                    loanHousingBusinessProcessDAO.update(cLoanHousingBusinessProcess);
                }else{
                    throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "该业务(" + ywlsh + ")不能被删除");
                }
            }


            if (Arrays.asList(CollectionBusinessStatus.新建.getName(), CollectionBusinessStatus.审核不通过.getName(), CollectionBusinessStatus.已作废.getName()).contains(cLoanHousingBusinessProcess.getStep())) {

                try {


                    cLoanHousingBusinessProcess.setLoanContract(null);

                    DAOBuilder.instance(this.loanHousingBusinessProcessDAO).entity(cLoanHousingBusinessProcess).save(new DAOBuilder.ErrorHandler() {

                        @Override
                        public void error(Exception e) { throw new ErrorException(e); }
                    });

                    DAOBuilder.instance(this.loanHousingBusinessProcessDAO).entity(cLoanHousingBusinessProcess).delete(new DAOBuilder.ErrorHandler() {

                        @Override
                        public void error(Exception e) { throw new ErrorException(e); }
                    });

                    CLoanHousingPersonInformationBasic loanHousingPersonInformationBasic = DAOBuilder.instance(this.loanHousingPersonInformationBasicDAO).searchFilter(new HashMap<String, Object>() {{

                        this.put("ywlsh", ywlsh);

                    }}).getObject(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) { throw new ErrorException(e); }
                    });

                    if(loanHousingPersonInformationBasic != null){

                        loanHousingPersonInformationBasic.setLoanContract(null);

                        DAOBuilder.instance(this.loanHousingPersonInformationBasicDAO).entity(loanHousingPersonInformationBasic).save(new DAOBuilder.ErrorHandler() {
                            @Override
                            public void error(Exception e) { throw new ErrorException(e); }
                        });

                        DAOBuilder.instance(this.loanHousingPersonInformationBasicDAO).entity(loanHousingPersonInformationBasic).delete(new DAOBuilder.ErrorHandler() {
                            @Override
                            public void error(Exception e) { throw new ErrorException(e); }
                        });
                    }
                    DAOBuilder.instance(this.personalLoanDAO).entity(cLoanHousingBusinessProcess.getLoanContract()).delete(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) { throw new ErrorException(e);}
                    });

                }catch (Exception e){
                    return  new CommonResponses(){{

                        this.setId(ywlsh);
                        this.setState("error");
                    }};
                }
            } else {
              throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "该业务(" + ywlsh + ")不能被删除");
            }

        }

//        CommonResponses commonResponses = new CommonResponses();
//        commonResponses.setId("");
//        commonResponses.setState(hasNotDelete ? "error" : "success");

        return  new CommonResponses(){{

            this.setId("");
            this.setState("success");
        }};
    }


    // TODO: 2017/9/8 多级审核测试用API，可随时删除
    @Override
    public MultipleTestRes multipleReviewTest(String role, String type, String subModule, String ywwd) {

        Collection subTypes = Arrays.asList("");

        BusinessType typeE = null;
        if (type.equals("WithDrawl")) {
            typeE = BusinessType.WithDrawl;
        } else if (type.equals("Collection")) {
            typeE = BusinessType.Collection;
        } else if (type.equals("Loan")) {
            typeE = BusinessType.Loan;
        } else if (type.equals("Finance")) {
            typeE = BusinessType.Finance;
        } else {
            throw new ErrorException("类型参数错误");
        }

        ArrayList<String> sources = icStateMachineConfigurationDAO.getReviewSources(role, typeE, subModule, null, ywwd);

/*        for (String s : sources) {
            System.out.println(s);
        }*/
        return new MultipleTestRes() {
            {
                this.setResults(sources);
            }
        };
    }

    /**
     * 提交与撤回还款申请
     *
     * @param status 状态（0：提交 1：撤回 2 已作废 ）
     **/
    @Override
    @Deprecated
    public CommonResponses batchSubmit(TokenContext tokenContext, BatchSubmission body, String status, String ywlx) {
        String subtype = "";
        String subywlx = "";
        String subywName = "";
        boolean Verification = true;
        if (!"1".equals(status)) {
            if (!StringUtil.notEmpty(ywlx)) throw new ErrorException("业务类型不能为空");
            switch (ywlx) {
                case "1":
                    if ("0".equals(status)) {
                        if (body.getYWLSHJH() != null) {
                            iHousingCompany.submitHousingCompany(tokenContext, body.getYWLSHJH());
                        }
                        Verification = false;
                    } else {
                        subtype = BusinessSubType.贷款_房开申请受理.getSubType();
                        subywlx = LoanBusinessType.新建房开.getCode();
                        subywName = LoanBusinessType.新建房开.getName();
                    }
                    break;
                case "2":
                    if ("0".equals(status)) {
                        if (body.getYWLSHJH() != null) {
                            ihousingCompanyAlter.submitHousingCompanyAlter(tokenContext, body.getYWLSHJH());
                        }
                        Verification = false;
                    } else {
                        subtype = BusinessSubType.贷款_房开变更受理.getSubType();
                        subywlx = LoanBusinessType.房开变更.getCode();
                        subywName = LoanBusinessType.房开变更.getName();
                    }
                    break;
                case "3":
                    if ("0".equals(status)) {
                        if (body.getYWLSHJH() != null) {
                            iestateProject.submitEstateProject(tokenContext, body.getYWLSHJH());
                        }
                        Verification = false;
                    } else {
                        subtype = BusinessSubType.贷款_楼盘申请受理.getSubType();
                        subywlx = LoanBusinessType.新建楼盘.getCode();
                        subywName = LoanBusinessType.新建楼盘.getName();
                    }
                    break;
                case "4":
                    if ("0".equals(status)) {
                        if (body.getYWLSHJH() != null) {
                            iestateProjectAlter.submitEstateProjectAlter(tokenContext, body.getYWLSHJH());
                        }
                        Verification = false;
                    } else {
                        subtype = BusinessSubType.贷款_楼盘变更受理.getSubType();
                        subywlx = LoanBusinessType.楼盘变更.getCode();
                        subywName = LoanBusinessType.楼盘变更.getCode();
                    }
                    break;
                case "5"://已作废
                    subtype = BusinessSubType.贷款_个人贷款申请.getSubType();
                    subywlx = LoanBusinessType.贷款发放.getCode();
                    subywName = LoanBusinessType.贷款发放.getName();
                    break;
                case "6":
                    subtype = BusinessSubType.贷款_个人还款申请.getSubType();
                    subywlx = LoanBusinessType.逾期还款.getCode();
                    subywName = LoanBusinessType.逾期还款.getName();
                    break;
                case "7":
                    subtype = BusinessSubType.贷款_个人还款申请.getSubType();
                    subywlx = LoanBusinessType.提前还款.getCode();
                    subywName = LoanBusinessType.提前还款.getName();
                    break;
                case "8":
                    subtype = BusinessSubType.贷款_个人还款申请.getSubType();
                    subywlx = LoanBusinessType.结清.getCode();
                    subywName = LoanBusinessType.结清.getName();
                    break;
                case "9":
                    subtype = BusinessSubType.贷款_合同变更申请.getSubType();
                    subywlx = LoanBusinessType.合同变更.getCode();
                    subywName = LoanBusinessType.合同变更.getName();
                    break;
                default:
                    throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "贷款_房开申请受理(1),\n" +
                            "贷款_房开变更受理(2),\n" +
                            "贷款_楼盘申请受理(3),\n" +
                            "贷款_楼盘变更受理(4),\n" +
                            "贷款_个人贷款申请(5),\n" +
                            "贷款_逾期还款(6),\n" +
                            "贷款_提前还款(7),\n" +
                            "贷款_结清(8),\n" +
                            "贷款_合同变更申请(9);");
            }
        }

        if (Verification == false) return new CommonResponses() {
            {
                this.setId("");
                this.setState("success");
            }
        };

        if (body != null) {
            if (!"1".equals(status)) {//提交和作废
                for (String YWLSH : body.getYWLSHJH()) {
                    HashMap<String, Object> hash = new HashMap<>();
                    hash.put("ywlsh", YWLSH);
                    if (!"1".equals(status)) {
                        hash.put("cznr", subywlx);
                    }
                    List<CLoanHousingBusinessProcess> loanHousingBusinessProcess = cloanHousingBusinessProcess.list(hash, null, null, null, null, ListDeleted.NOTDELETED, null);
                    if (loanHousingBusinessProcess.size() == 0)
                        throw new ErrorException(ReturnEnumeration.Data_MISS, "不存在该笔业务记录" + YWLSH);
                    CLoanHousingBusinessProcess housingBusinessProcess = loanHousingBusinessProcess.get(0);
                    if (housingBusinessProcess.getStep() == null)
                        throw new ErrorException(ReturnEnumeration.Business_FAILED, "业务状态(step)不能为空,业务数据丢失");
                    if (!loanHousingBusinessProcess.get(0).getCzy().equals(tokenContext.getUserInfo().getCZY()) ||
                            !loanHousingBusinessProcess.get(0).getYwwd().getId().equals(tokenContext.getUserInfo().getYWWD()))
                        throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务不是您受理的");

                    if ("0".equals(status)) {
                        if (BusinessSubType.贷款_合同变更申请.getSubType().equals(subtype)) {
                            if (!iUploadImagesService.validateUploadFile(UploadFileModleType.贷款.getCode(),
                                    UploadFileBusinessType.合同变更.getCode(), loanHousingBusinessProcess.get(0).getLoanHousingPersonInformationVice().getBlzl())) {
                                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "资料未上传完整");
                            }
                            if ("20".equals(loanHousingBusinessProcess.get(0).getLoanHousingPersonInformationVice().getHyzk())) {
                                if (!iUploadImagesService.validateUploadFile(UploadFileModleType.贷款.getCode(),
                                        UploadFileBusinessType.合同变更.getCode(), loanHousingBusinessProcess.get(0).getLoanHousingCoborrowerVice().getBlzl())) {
                                    throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "资料未上传完整");
                                }
                            }
                        }
                    }

                    String event = null;
                    if ("0".equals(status)) {
                        event = Events.通过.getEvent();
                        iSaveAuditHistory.saveNormalBusiness(YWLSH, tokenContext, subywName, "修改");
                    } else {
                        event = Events.不通过.getEvent();
                    }

                    if (loanHousingBusinessProcess.get(0).getCzy() == null) throw new ErrorException("业务操作员为空");
                    if (loanHousingBusinessProcess.get(0).getStep() == null) throw new ErrorException("业务状态为空");
                    if (loanHousingBusinessProcess.get(0).getYwwd() == null) throw new ErrorException("业务网点为空");

                    TaskEntity taskenti = new TaskEntity();
                    taskenti.setOperator(loanHousingBusinessProcess.get(0).getCzy());
                    taskenti.setStatus(loanHousingBusinessProcess.get(0).getStep());
                    taskenti.setTaskId(loanHousingBusinessProcess.get(0).getYwlsh());
                    taskenti.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                    taskenti.setSubtype(subtype);
                    taskenti.setType(com.handge.housingfund.database.enums.BusinessType.Loan);
                    taskenti.setWorkstation(loanHousingBusinessProcess.get(0).getYwwd().getId());

                    StateMachineUtils.updateState(iStateMachineService, event, taskenti
                            , new StateMachineUtils.StateChangeHandler() {
                                @Override
                                public void onStateChange(boolean succeed, String next, Exception e) {
                                    if (e != null) {
                                        throw new ErrorException(e);
                                    }
                                    if (!succeed || next == null) {
                                        return;
                                    }
                                    if (succeed) {
                                        if (StringUtil.isIntoReview(next, null)) {
                                            loanHousingBusinessProcess.get(0).setDdsj(new Date());
                                        }
                                        loanHousingBusinessProcess.get(0).setStep(next);
                                        cloanHousingBusinessProcess.update(loanHousingBusinessProcess.get(0));
                                    }
                                }
                            });
                }
            } else {//撤回
                for (String YWLSH : body.getYWLSHJH()) {
                    revokeOperation(tokenContext, YWLSH);
                }
            }
        }
        return new CommonResponses() {
            {
                this.setId("");
                this.setState("success");
            }
        };
    }


    public void revokeOperation(TokenContext tokenContext, String YWLSH) {
        CLoanHousingBusinessProcess entity = DAOBuilder.instance(cloanHousingBusinessProcess).searchFilter(new HashMap<String, Object>() {
            {
                this.put("ywlsh", YWLSH);
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Data_MISS, "业务不存在");
            }
        });


        String step = entity.getStep();
        if (step == null || !StringUtil.isIntoReview(step, null) /** 还应考虑是否处于审核中**/) {
            throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH);
        }
        MultiReviewConfig config = NormalJsonUtils.toObj4Review(entity.getShybh());
        step = checkByReivew(config, tokenContext);

        if (LoanBussinessStatus.新建.getName().equals(step)) {
            if (!entity.getCzy().equals(tokenContext.getUserInfo().getCZY()) || !entity.getYwwd().getId().equals(tokenContext.getUserInfo().getYWWD()))
                throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务不是您受理的");
        }

        String ywlx = entity.getCznr();
        entity.setStep(step);
        iSaveAuditHistory.saveNormalBusiness(YWLSH, tokenContext, LoanBusinessType.getNameByCode(ywlx), "撤回");
        cloanHousingBusinessProcess.save(entity);
    }

    //基于审核配置的验证，并返回正确的step状态
    public String checkByReivew(MultiReviewConfig config, TokenContext tokenContext) {
        String step;
        if (config == null) {
            step = LoanBussinessStatus.新建.getName();
        } else {
            //审核级别（0：普审，1：特审）
            if ("S".equals(config.getSHJB())) {
                throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务已进入特审阶段，不能撤回");
            }
            if (StringUtil.notEmpty(config.getSCSHY())) {
                if (!tokenContext.getUserId().equals(config.getSCSHY())) {
                    throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务的上一级审核非您操作，没有撤回权限");
                }
                step = LoanBussinessStatus.审核不通过.getName();
            } else {
                step = LoanBussinessStatus.新建.getName();
            }
            config.setSHJB(null);
            config.setSCSHY(null);
            config.setDQSHY(null);
            config.setDQXM(null);
        }
        return step;
    }

}
