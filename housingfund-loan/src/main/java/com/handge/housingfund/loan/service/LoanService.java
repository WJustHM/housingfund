package com.handge.housingfund.loan.service;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.ISettlementSpecialBankAccountManageService;
import com.handge.housingfund.common.service.account.model.CenterAccountInfo;
import com.handge.housingfund.common.service.bank.bean.center.SinglePaymentIn;
import com.handge.housingfund.common.service.bank.bean.center.SinglePaymentOut;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;
import com.handge.housingfund.common.service.bank.enums.BusTypeEnums;
import com.handge.housingfund.common.service.bank.ibank.IBank;
import com.handge.housingfund.common.service.loan.ILoanService;
import com.handge.housingfund.common.service.loan.enums.LoanBusinessType;
import com.handge.housingfund.common.service.loan.model.CommonResponses;
import com.handge.housingfund.common.service.loan.model.ConfirmPaymentGet;
import com.handge.housingfund.common.service.loan.model.ConfirmPaymentPut;
import com.handge.housingfund.common.service.loan.model.ConfirmPaymentResponse;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.dao.ICAccountNetworkDAO;
import com.handge.housingfund.database.dao.ICLoanHousingBusinessProcessDAO;
import com.handge.housingfund.database.dao.ICLoanHousingLoanDAO;
import com.handge.housingfund.database.dao.ICLoanHousingPersonInformationBasicDAO;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.BusinessSubType;
import com.handge.housingfund.database.enums.Events;
import com.handge.housingfund.database.utils.DAOBuilder;
import com.handge.housingfund.loan.utils.StateMachineUtils;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import com.handge.housingfund.statemachineV2.IStateMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;


@Component
@SuppressWarnings("ALL")
public class LoanService implements ILoanService{

    private static String format = "yyyy-MM-dd";

    @Autowired
    private IBank iBankService ;

    @Autowired
    private ICLoanHousingBusinessProcessDAO loanHousingBusinessProcessDAO;

    @Autowired
    private ICLoanHousingPersonInformationBasicDAO loanHousingPersonInformationBasicDAO;

    @Autowired
    private ICLoanHousingLoanDAO loanHousingLoanDAO;

    @Autowired
    private ISettlementSpecialBankAccountManageService settlementSpecialBankAccountManageService;

    @Autowired
    @Resource(name = "stateMachineServiceV2")
    private IStateMachineService iStateMachineService;

    @Autowired
    private ICAccountNetworkDAO cAccountNetworkDAO;


    /*
     *  completed
     *
     * !逻辑未完成
     *
     * !存在问题
     * ~参数验证没做完
     * ~数据库字段缺失 贷款发放日期
     * ~数据库字段缺失 放款银行名称
     * ~数据库字段缺失 收款账号
     ** ~数据库字段缺失 收款账号户名
     * ~数据库字段缺失 放款账户户名
     ** ~数据库字段缺失 款发放额
     * ~数据库字段缺失 放款账户
     * #~数据库字段缺失 贷款账号
     *
     * */
    @Override
    public ConfirmPaymentResponse getConfirmLoans(TokenContext tokenContext, String YWLSH) {

        //region //查询必要字段&完整性验证
        CLoanHousingBusinessProcess loanHousingBusinessProcess = DAOBuilder.instance(this.loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("ywlsh", YWLSH);

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e); }
        });

        if (loanHousingBusinessProcess == null) {

            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务记录");
        }

        CLoanHousingPersonInformationBasic loanHousingPersonInformationBasic = DAOBuilder.instance(this.loanHousingPersonInformationBasicDAO).searchFilter(new HashMap<String, Object>(){{

            this.put("ywlsh",loanHousingBusinessProcess.getYwlsh());

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e);}
        });

        if (loanHousingPersonInformationBasic == null) {

            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务记录");
        }

        StHousingPersonalLoan housingPersonalLoan = loanHousingBusinessProcess.getLoanContract();

        CLoanFundsInformationBasic loanFundsInformationBasic = loanHousingPersonInformationBasic.getFunds();

        if (housingPersonalLoan == null){ throw new ErrorException(ReturnEnumeration.Data_MISS,"合同信息"); }

        //endregion

        return new ConfirmPaymentResponse() {{
            //*this.setDKFFRQ(housingPersonalLoan.getcLoanHousingPersonalLoanExtension()/*贷款发放日期*/);
            this.setYWLSH(loanHousingBusinessProcess.getYwlsh()/*业务流水号*/);
            //*this.setFKYHMC(housingPersonalLoan.get/*放款银行名称*/);
            this.setYWWD(loanHousingBusinessProcess.getYwwd().getMingCheng()/*业务网点*/);
            this.setJKHTBH(housingPersonalLoan.getJkhtbh()/*借款合同编号*/);
            this.setJKRZJHM(loanHousingPersonInformationBasic.getJkrzjhm()/*借款人证件号码*/);
            this.setSKZH(new HashMap<String,String>(){{
                this.put("0",loanHousingPersonInformationBasic.getPurchasing() == null? null : (loanHousingPersonInformationBasic.getPurchasing().getSfwesf()?loanHousingPersonInformationBasic.getPurchasing().getGrskyhzh() : loanHousingPersonInformationBasic.getPurchasing().getSfrzhhm()));
                this.put("1",loanHousingPersonInformationBasic.getBuild()== null ? null :loanHousingPersonInformationBasic.getBuild().getGrskyhzh());
                this.put("2",loanHousingPersonInformationBasic.getOverhaul()==null?null:loanHousingPersonInformationBasic.getOverhaul().getGrskyhzh());
            }}.get(loanHousingPersonInformationBasic.getDkyt())/*收款账号*/);
            this.setSKZHHM(new HashMap<String,String>(){{
                this.put("0",loanHousingPersonInformationBasic.getPurchasing() == null? null : (loanHousingPersonInformationBasic.getPurchasing().getSfwesf()?loanHousingPersonInformationBasic.getPurchasing().getYhkhm():loanHousingPersonInformationBasic.getPurchasing().getSfryhkhm()));
                this.put("1",loanHousingPersonInformationBasic.getBuild()== null ? null :loanHousingPersonInformationBasic.getBuild().getYhkhm());
                this.put("2",loanHousingPersonInformationBasic.getOverhaul()==null?null:loanHousingPersonInformationBasic.getOverhaul().getYhkhm());
            }}.get(loanHousingPersonInformationBasic.getDkyt())/*收款账号户名*/);
            // this.setFKZHHM(housingPersonalLoan.getcLoanHousingPersonalLoanExtension().f/*放款账户户名*/);
            this.setDKFFE(loanFundsInformationBasic.getHtdkje()+""/*贷款发放额*/);
            this.setJKRXM(loanHousingPersonInformationBasic.getJkrxm()/*借款人姓名*/);
            this.setJKRZJLX(loanHousingPersonInformationBasic.getJkrzjlx()/*借款人证件类型*/);
            this.setSKYHMC(new HashMap<String,String>(){{
                this.put("0",loanHousingPersonInformationBasic.getPurchasing() == null? null : (loanHousingPersonInformationBasic.getPurchasing().getSfwesf()?loanHousingPersonInformationBasic.getPurchasing().getKhyhmc():loanHousingPersonInformationBasic.getPurchasing().getSfrkhyhmc()));
                this.put("1",loanHousingPersonInformationBasic.getBuild()== null ? null :loanHousingPersonInformationBasic.getBuild().getKhhyhmc());
                this.put("2",loanHousingPersonInformationBasic.getOverhaul()==null?null:loanHousingPersonInformationBasic.getOverhaul().getKhyhmc());
            }}.get(loanHousingPersonInformationBasic.getDkyt())/*收款账号户名*//*收款银行名称*/);
            //*this.setFKZH(null/*放款账户*/);
            this.setDKZH(loanHousingPersonInformationBasic.getDkzh()/*贷款账号*/);
            this.setCZY(tokenContext.getUserInfo().getCZY()/*操作员*/);
        }};
    }


    /*
      *  completed
      *
      * !逻辑未完成
      *
      * !存在问题
      *
      * 调结算平台？
      *
      * */
    @Override
    public CommonResponses putConfirmLoansSubmit(TokenContext tokenContext,String YWLSH, ConfirmPaymentPut body) {

        //region //查询必要字段

        CLoanHousingBusinessProcess loanHousingBusinessProcess = DAOBuilder.instance(this.loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("ywlsh", YWLSH);

            this.put("cznr", LoanBusinessType.贷款发放.getCode());

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e); }
        });

        if (loanHousingBusinessProcess == null) {

            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务记录");
        }
        CLoanHousingPersonInformationBasic loanHousingPersonInformationBasic = DAOBuilder.instance(this.loanHousingPersonInformationBasicDAO).searchFilter(new HashMap<String, Object>(){{

            this.put("ywlsh",loanHousingBusinessProcess.getYwlsh());

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e);}
        });

        if (loanHousingPersonInformationBasic == null) {

            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务记录");
        }
        CLoanHousingLoan loanHousingLoanSearch = DAOBuilder.instance(this.loanHousingLoanDAO).searchFilter(new HashMap<String,Object>(){{

            this.put("ywlsh",YWLSH);

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e);}
        });

        CLoanHousingLoan loanHousingLoan = loanHousingLoanSearch == null ? new CLoanHousingLoan():loanHousingLoanSearch;

        //endregion

        //region  //字段填充
        // common

        loanHousingLoan.setDkffrq(DateUtil.safeStr2Date(format, body.getDKFFRQ(/*贷款发放日期*/)));

        loanHousingLoan.setYwlsh(YWLSH/*业务流水号*/);

        loanHousingLoan.setFkyhmc(body.getFKYHMC(/*放款银行名称（0：建设银行 1：中国银行 2：交通银行 3：农业银行 4：工商银行）*/));

        loanHousingLoan.setYwwd(tokenContext.getUserInfo().getYWWD()/*业务网点*/);

        loanHousingLoan.setJkhtbh(body.getJKHTBH(/*借款合同编号*/));

        if(IdcardValidator.isValidatedAllIdcard(body.getJKRZJHM(/*借款人证件号码*/))){
            loanHousingLoan.setJkrzjhm(body.getJKRZJHM(/*借款人证件号码*/));
        }else{
            throw new ErrorException("身份证格式有问题");
        }

        loanHousingLoan.setSkzh(body.getSKZH(/*收款账号*/));

        loanHousingLoan.setSkzhhm(body.getSKZHHM(/*收款账号户名*/));

        loanHousingLoan.setFkzhhm(body.getFKZHHM(/*放款账户户名*/));

        loanHousingLoan.setDkffe(body.getDKFFE(/*贷款发放额*/));

        loanHousingLoan.setJkrxm(body.getJKRXM(/*借款人姓名*/));

        loanHousingLoan.setJkrzjlx(body.getJKRZJLX(/*借款人证件类型*/));

        loanHousingLoan.setSkyhmc(body.getSKYHMC(/*收款银行名称*/));

        loanHousingLoan.setFkzh(body.getFKZH(/*放款账户*/));

        loanHousingLoan.setDkzh(body.getDKZH(/*贷款账号*/));

        loanHousingLoan.setCzy(body.getCZY(/*操作员*/));

        loanHousingLoan.setState(0);

        //endregion

        if(loanHousingLoan.getFkyhmc()==null||!loanHousingLoan.getFkyhmc().equals(loanHousingLoan.getSkyhmc())){ throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"放款银行和收款银行不一致"); }


        DAOBuilder.instance(this.loanHousingLoanDAO).entity(loanHousingLoan).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) { throw new ErrorException(e); }
        });

        CenterAccountInfo centerAccountInfo = this.settlementSpecialBankAccountManageService.getSpecialAccountByZHHM(loanHousingLoan.getFkzh());

        if(centerAccountInfo == null || centerAccountInfo.getNode() == null){

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"放款账户");
        }

        if(!this.iStateMachineService.checkpermission(new TaskEntity(){{

            this.setStatus(loanHousingBusinessProcess.getStep());
            this.setTaskId(loanHousingBusinessProcess.getYwlsh());
            this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));;
            this.setNote("");
            this.setSubtype(BusinessSubType.贷款_个人贷款申请.getSubType());
            this.setType(com.handge.housingfund.database.enums.BusinessType.Loan);
            this.setOperator(tokenContext.getUserInfo().getCZY()/*操作员*/);
            this.setWorkstation(tokenContext.getUserInfo().getYWWD(/*业务网点*/));

        }})){
            throw new ErrorException(ReturnEnumeration.Permission_Denied,"");
        }

        //region //结算平台

        if(!iBankService.checkYWLSH(YWLSH)){ throw new ErrorException(ReturnEnumeration.Business_In_Process,"请勿重复操作,请刷新页面确认"); }

        SinglePaymentOut singlePaymentOut = null;

        boolean succeed = false;

        SettlementHandler settlementHandler = SettlementHandler.instance(iBankService);

        try {

            SinglePaymentIn singlePaymentIn = ResUtils.noneAdductionValue(SinglePaymentIn.class, new SinglePaymentIn(){{

                this.setCenterHeadIn(new CenterHeadIn(/*CenterHeadIn(required)*/){{

//                    String[] array = DateUtil.getDatetime();

//                    this.setSendDate(array[0]/*发送日期(required)*/);
//
//                    this.setSendTime(array[1]/*发送方时间(required)*/);

                    this.setSendSeqNo(YWLSH/*发送方流水号(required)*/);

//                     this.setTxUnitNo("0"/*交易机构号(required)*/);//*
//
//                     this.setSendNode("0"/*发送方节点号(required)*/);//*
//
//                     this.setTxCode("0"/*交易代码(required)*/);//*

                    this.setCustNo(centerAccountInfo.getKHBH()); //客户编号

                    this.setReceiveNode(centerAccountInfo.getNode()/*接收方节点号(required) 根据银行名称查询*/);//*

                    this.setOperNo("0"/*操作员编号(required) 待定*/);//*

                }});
                this.setSettleType("1"/*结算模式(required) 1：本行 2：跨行-实时 3：跨行-非实时*/);//*
                this.setBusType(BusTypeEnums.划拨委贷基金.getCode()/*业务类型(required) 待定*/);//*
                this.setDeAcctNo(loanHousingLoan.getFkzh()/*付方账号(required)*/);
                this.setDeAcctName(loanHousingLoan.getFkzhhm()/*付方户名(required)*/);
                this.setDeAcctClass("2"/*付方账户类别(required)*/);
                this.setCapAmt(new BigDecimal(loanHousingLoan.getDkffe())/*本金发生额(required)*/);
                this.setCrAcctNo(loanHousingLoan.getSkzh()/*收方账号(required)*/);
                this.setCrAcctName(loanHousingLoan.getSkzhhm()/*收方户名(required)*/);
                this.setCrAcctClass((loanHousingPersonInformationBasic.getPurchasing()!=null&&!loanHousingPersonInformationBasic.getPurchasing().getSfwesf())?"2":"1"/*收方账户类别(required)*/);
                this.setCrBankClass("0"/*收方账户行别(required) 0：本行 1：跨行*/);//(
                this.setAmt(new BigDecimal(loanHousingLoan.getDkffe())/*金额(required)*/);
                this.setRefSeqNo1(YWLSH/*业务明细流水号1(required)*/);
                this.setSummary(LoanBusinessType.贷款发放.getName()/*摘要(required)*/);
                this.setRemark(LoanBusinessType.贷款发放.getName()/*备注(required)*/);
                if ("104".equals(centerAccountInfo.getCode())) {
                    this.setCrChgNo("104100000004");
                }
            }});

            settlementHandler.setCenterHeadIn(singlePaymentIn.getCenterHeadIn())
                    .setSuccess(new SettlementHandler.Success() {
                        @Override
                        public void handle() { System.out.println("贷款放款：结算平台请求成功"); }
                    }).setFail(new SettlementHandler.Fail() {
                        @Override
                        public void handle(String sbyy) { updateSBYY(YWLSH,  sbyy); }
                    }).setManualProcess(new SettlementHandler.ManualProcess() {
                        @Override
                        public void handle() { updateSBYY(YWLSH,  "状态未知,需人工线下查询"); }
                    });

            singlePaymentOut =  this.iBankService.sendMsg(singlePaymentIn);

            settlementHandler.setCenterHeadOut(singlePaymentOut.getCenterHeadOut()).handle();

            if(!singlePaymentOut.getCenterHeadOut().getTxStatus().equals("0")){

                succeed = false;
            }else {

                succeed = true;
            }
        } catch (Exception e) {

            succeed = false;

            settlementHandler.setSendException(new SettlementHandler.SendException() {
                @Override
                public void handle(String sbyy) { updateSBYY(YWLSH,  sbyy); }

            }).handleException(e);

            throw new ErrorException(e);
        }


        //endregion

        //region//修改状态
        StateMachineUtils.updateState((IStateMachineService) this.iStateMachineService, succeed ? Events.通过.getEvent():Events.不通过.getEvent(),new TaskEntity(){{

            this.setStatus(loanHousingBusinessProcess.getStep());
            this.setTaskId(loanHousingBusinessProcess.getYwlsh());
            this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));;
            this.setNote("");
            this.setSubtype(BusinessSubType.贷款_个人贷款申请.getSubType());
            this.setType(com.handge.housingfund.database.enums.BusinessType.Loan);
            this.setOperator(tokenContext.getUserInfo().getCZY()/*操作员*/);
            this.setWorkstation(tokenContext.getUserInfo().getYWWD(/*业务网点*/));

        }}, new StateMachineUtils.StateChangeHandler() {

            @Override
            public void onStateChange(boolean succeed, String next, Exception e) {

                if (e != null) { throw new ErrorException(e); }

                if (!succeed || next == null) { return; }

                loanHousingBusinessProcess.setStep(next);

                if(StringUtil.isIntoReview(next,null)){

                    loanHousingBusinessProcess.setDdsj(new Date());

                }

                loanHousingLoan.setState(0);

                DAOBuilder.instance(loanHousingBusinessProcessDAO).entity(loanHousingBusinessProcess).save(new DAOBuilder.ErrorHandler() {

                    @Override
                    public void error(Exception e) { throw new ErrorException(e); }
                });
            }
        });

        //endregion

        boolean finalSucceed = succeed;

        return new CommonResponses() {{

            this.setId(YWLSH);

            this.setState(finalSucceed ?"success":"fail");
        }};
    }

    private void updateSBYY(String YWLSH,String sbyy){



        CLoanHousingLoan loanHousingLoanSearch = DAOBuilder.instance(this.loanHousingLoanDAO).searchFilter(new HashMap<String,Object>(){{

            this.put("ywlsh",YWLSH);

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e);}
        });

       if(loanHousingLoanSearch == null){ throw new ErrorException(ReturnEnumeration.Data_MISS,"放款记录");}

       loanHousingLoanSearch.setSbyy(sbyy);

       DAOBuilder.instance(this.loanHousingLoanDAO).entity(loanHousingLoanSearch).save(new DAOBuilder.ErrorHandler() {

           @Override
           public void error(Exception e) { throw new ErrorException(e); }

       });


    }
    /*
      *  completed
      *
      * !逻辑已完成
      *
      * !存在问题
      *
      * #~swagger 没有该接口
      *
      * */
    public ConfirmPaymentGet getConfirmLoansSubmit(TokenContext tokenContext,String YWLSH) {

        CLoanHousingLoan loanHousingLoan = DAOBuilder.instance(this.loanHousingLoanDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("ywlsh", YWLSH);

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e); }

        });

        if (loanHousingLoan == null) { throw new ErrorException(ReturnEnumeration.Data_MISS, "业务记录"); }

        return new ConfirmPaymentGet() {{

            this.setDKFFRQ(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month,loanHousingLoan.getDkffrq(/*贷款发放日期*/), format));

            this.setYWLSH(loanHousingLoan.getYwlsh(/*业务流水号*/));

            this.setFKYHMC(loanHousingLoan.getFkyhmc(/*放款银行名称（0：建设银行 1：中国银行 2：交通银行 3：农业银行 4：工商银行）*/));

            CAccountNetwork network = DAOBuilder.instance(cAccountNetworkDAO).searchFilter(new HashMap<String, Object>() {{

                this.put("id",loanHousingLoan.getYwwd());

            }}).getObject(new DAOBuilder.ErrorHandler() {

                @Override
                public void error(Exception e) { throw new ErrorException(e); }

            });
            this.setYWWD(network==null?"":network.getMingCheng()/*业务网点*/);

            this.setJKHTBH(loanHousingLoan.getJkhtbh(/*借款合同编号*/));

            this.setJKRZJHM(loanHousingLoan.getJkrzjhm(/*借款人证件号码*/));

            this.setSKZH(loanHousingLoan.getSkzh(/*收款账号*/));

            this.setSKZHHM(loanHousingLoan.getSkzhhm(/*收款账号户名*/));

            this.setFKZHHM(loanHousingLoan.getFkzhhm(/*放款账户户名*/));

            this.setDKFFE(loanHousingLoan.getDkffe(/*贷款发放额*/));

            this.setJKRXM(loanHousingLoan.getJkrxm(/*借款人姓名*/));

            this.setJKRZJLX(loanHousingLoan.getJkrzjlx(/*借款人证件类型*/));

            this.setSKYHMC(loanHousingLoan.getSkyhmc(/*收款银行名称*/));

            this.setFKZH(loanHousingLoan.getFkzh(/*放款账户*/));

            this.setDKZH(loanHousingLoan.getDkzh(/*贷款账号*/));

            this.setCZY(loanHousingLoan.getCzy(/*操作员*/));
        }};

    }


}
