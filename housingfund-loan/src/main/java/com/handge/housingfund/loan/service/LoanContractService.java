package com.handge.housingfund.loan.service;

import com.handge.housingfund.common.service.ISaveAuditHistory;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.loan.ILoanContractService;
import com.handge.housingfund.common.service.loan.enums.LoanBusinessType;
import com.handge.housingfund.common.service.loan.enums.LoanBussinessStatus;
import com.handge.housingfund.common.service.loan.model.*;
import com.handge.housingfund.common.service.loan.model.ResponseEntity;
import com.handge.housingfund.common.service.others.IPdfService;
import com.handge.housingfund.common.service.others.ISMSCommon;
import com.handge.housingfund.common.service.others.IUploadImagesService;
import com.handge.housingfund.common.service.others.enums.UploadFileBusinessType;
import com.handge.housingfund.common.service.others.enums.UploadFileModleType;
import com.handge.housingfund.common.service.sms.enums.SMSTemp;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.BusinessSubType;
import com.handge.housingfund.database.enums.Events;
import com.handge.housingfund.database.enums.ListAction;
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.utils.DAOBuilder;
import com.handge.housingfund.loan.utils.StateMachineUtils;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import com.handge.housingfund.statemachineV2.IStateMachineService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Funnyboy on 2017/8/16.
 */
@SuppressWarnings("Duplicates")
@Component
public class LoanContractService implements ILoanContractService {
    private final SimpleDateFormat simm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private final SimpleDateFormat simall = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat simM = new SimpleDateFormat("yyyyMM");
    private final SimpleDateFormat simview = new SimpleDateFormat("yyyy-MM");
    @Autowired
    ICLoanHousingBusinessProcessDAO cloanHousingBusinessProcess;
    @Autowired
    private ISMSCommon ismsCommon;
    @Autowired
    ICLoanHousingPersonInformationBasicDAO cloanHousingPersonInformationBasic;
    @Autowired
    IStHousingPersonalAccountDAO istHousingPersonalAccountDAO;
    @Autowired
    IStHousingPersonalLoanDAO isthousingpersonalloandao;
    @Autowired
    protected ISaveAuditHistory iSaveAuditHistory;
    @Autowired
    IUploadImagesService iUploadImagesService;
    @Autowired
    IStCommonPersonDAO common_person;
    @Autowired
    ICBankBankInfoDAO icBankBankInfoDAO;
    @Autowired
    @Resource(name = "stateMachineServiceV2")
    private IStateMachineService iStateMachineService;
    @Autowired
    private IPdfService pdfService;
    @Autowired
    private ICAccountNetworkDAO cAccountNetworkDAO;

    /**
     * 合同变更申请列表
     * //
     */
    @Override
    public final PageRes<LoanContractListResponseRes> getLoanContractReviewList(TokenContext tokenContext, String YWWD, String JKRXM, String JKRZJHM, String status, String JKHTBH, String pageSize, String page, String KSSJ, String JSSJ, String YHDM, String ZJHM) {
        PageResults<CLoanHousingBusinessProcess> housingBusinessProcess = null;
        try {
            housingBusinessProcess = cloanHousingBusinessProcess.listWithPage(
                    new HashMap<String, Object>() {{
                        this.put("cznr", LoanBusinessType.合同变更.getCode());

                        if (StringUtil.notEmpty(YWWD)) {
                            this.put("ywwd.id", YWWD);
                        }
                        if (StringUtil.notEmpty(status)) {
                            if (status.equals(LoanBussinessStatus.新建.getName())) {
                                this.put("step", LoanBussinessStatus.新建.getName());
                            } else if (status.equals(LoanBussinessStatus.待审核.getName())) {
                                this.put("step", LoanBussinessStatus.待审核.getName());
                            } else if (status.equals(LoanBussinessStatus.审核不通过.getName())) {
                                this.put("step", LoanBussinessStatus.审核不通过.getName());
                            } else if (status.equals(LoanBussinessStatus.办结.getName())) {
                                this.put("step", LoanBussinessStatus.办结.getName());
                            }
                        }
                    }}, !StringUtil.notEmpty(KSSJ) ? null : simm.parse(KSSJ),
                    !StringUtil.notEmpty(JSSJ) ? null : simm.parse(JSSJ), "created_at", Order.DESC, null, null, !StringUtil.notEmpty(page) ? null : Integer.parseInt(page), !StringUtil.notEmpty(pageSize) ? null : Integer.parseInt(pageSize), new IBaseDAO.CriteriaExtension() {
                        @Override
                        public void extend(Criteria criteria) {
                            criteria.createAlias("loanHousingPersonInformationVice", "loanHousingPersonInformationVice");

                            if (StringUtil.notEmpty(JKRXM.replace(" ", ""))) {
                                criteria.add(Restrictions.like("loanHousingPersonInformationVice.jkrxm", "%" + JKRXM + "%"));
                            }
                            if (StringUtil.notEmpty(JKHTBH.replace(" ", ""))) {
                                criteria.add(Restrictions.like("loanHousingPersonInformationVice.jkhtbh", "%" + JKHTBH + "%"));
                            }
                            if (StringUtil.notEmpty(ZJHM.replace(" ", ""))) {
                                criteria.add(Restrictions.like("loanHousingPersonInformationVice.jkrzjhm", "%" + ZJHM + "%"));
                            }
                        }
                    });
        } catch (ParseException e) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "时间格式有误");
        } catch (Exception e) {
            throw new ErrorException(e);
        }

        PageRes<LoanContractListResponseRes> pageres = new PageRes<>();
        List<CLoanHousingBusinessProcess> cLoanHousingBusinessProcess = housingBusinessProcess.getResults();
        ArrayList<LoanContractListResponseRes> res = new ArrayList<>();
        LoanContractListResponseRes loanContractListResponseRes = null;
        StHousingPersonalAccount byDKZH = null;
        for (CLoanHousingBusinessProcess businessProcess : cLoanHousingBusinessProcess) {
            byDKZH = istHousingPersonalAccountDAO.getByDkzh(businessProcess.getDkzh());
            if (StringUtil.notEmpty(YHDM) && byDKZH != null && byDKZH.getStHousingPersonalLoan() != null) {
                if (StringUtil.isEmpty(byDKZH.getStHousingPersonalLoan().getSwtyhdm()) || !byDKZH.getStHousingPersonalLoan().getSwtyhdm().equals(YHDM))
                    continue;
            }
            loanContractListResponseRes = new LoanContractListResponseRes();
            loanContractListResponseRes.setDKZH(businessProcess.getDkzh());
            loanContractListResponseRes.setYWLSH(businessProcess.getYwlsh());
            loanContractListResponseRes.setZhuangTai(businessProcess.getStep());
            loanContractListResponseRes.setCZY(businessProcess.getCzy());
            loanContractListResponseRes.setJKRXM(businessProcess.getLoanHousingPersonInformationVice().getJkrxm());
            loanContractListResponseRes.setZJHM(businessProcess.getLoanHousingPersonInformationVice().getJkrzjhm());
            loanContractListResponseRes.setYWWD(businessProcess.getYwwd().getMingCheng());
            loanContractListResponseRes.setSLSJ(simm.format(businessProcess.getCreated_at()));
            try {
                loanContractListResponseRes.setJKHTBH(businessProcess.getLoanHousingPersonInformationVice().getJkhtbh());
            } catch (Exception e) {
                loanContractListResponseRes.setJKHTBH("");
            }
            res.add(loanContractListResponseRes);
        }
        pageres.setResults(res);
        pageres.setPageCount(housingBusinessProcess.getPageCount());
        pageres.setTotalCount(housingBusinessProcess.getTotalCount());
        pageres.setPageSize(housingBusinessProcess.getPageSize());
        pageres.setNextPageNo(housingBusinessProcess.getPageNo());
        pageres.setCurrentPage(housingBusinessProcess.getCurrentPage());
        return pageres;
    }

    /**
     * 新增变更请求 0保存1提交
     */
    @Override
    public final CommonResponses putBorrowerInformationApplication(TokenContext tokenContext, String action, GetBorrowerInformation getborr) {
        if (action.equals("1")) {
            if (!iUploadImagesService.validateUploadFile(UploadFileModleType.贷款.getCode(),
                    UploadFileBusinessType.合同变更.getCode(), getborr.getJKRXX().getBLZL())) {
                throw new ErrorException(ReturnEnumeration.Data_MISS, "资料未上传");
            }

            if ("20".equals(getborr.getJKRXX().getBorrowingInfo().getHYZK())) {
                if (!iUploadImagesService.validateUploadFile(UploadFileModleType.贷款.getCode(),
                        UploadFileBusinessType.合同变更.getCode(), getborr.getGTJKRXX().getBLZL())) {
                    throw new ErrorException(ReturnEnumeration.Data_MISS, "资料未上传");
                }
            }
        }

        if (!StringUtil.notEmpty(getborr.getJKRGJGZH()))
            throw new ErrorException(ReturnEnumeration.Data_MISS, "公积金账号不能为空");
//        List<StCommonPerson> stCommonPeople = common_person.list(new HashMap<String, Object>() {{
//            this.put("grzh", getborr.getJKRGJGZH());
//        }}, null, null, null, null, null, null);
//        if (stCommonPeople.size() == 0) throw new ErrorException("不存在此公积金账号");
//        if (!tokenContext.getUserInfo().getYWWD().equals(stCommonPeople.get(0).getUnit().getExtension().getKhwd()))
//            throw new ErrorException("该业务不在当前网点受理范围内");

        List<CLoanHousingPersonInformationBasic> jkrgjjzh = cloanHousingPersonInformationBasic.list(new HashMap<String, Object>() {{
            this.put("dkzh", getborr.getDKZH());
        }}, null, null, null, null, null, null);
        if (jkrgjjzh.size() == 0) throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "不存在此贷款账号");

        CLoanHousingBusinessProcess cloanHousingBusinessProcessByYWLSH = cloanHousingBusinessProcess.getByYWLSHJKRZJHM(jkrgjjzh.get(0).getYwlsh(), jkrgjjzh.get(0).getJkrzjhm());
        if (cloanHousingBusinessProcessByYWLSH.getStep() != null && !cloanHousingBusinessProcessByYWLSH.getStep().equals(LoanBussinessStatus.已入账.getName())) {
            throw new ErrorException(ReturnEnumeration.Business_FAILED, "未放款的贷款不能做变更");
        }

        for (CLoanHousingPersonInformationBasic jkr : jkrgjjzh) {
            if (!LoanBusinessType.结清.getCode().equals(jkr.getDkzhzt())) {
                List<CLoanHousingBusinessProcess> cLoanHousingBusinessProcesses = cloanHousingBusinessProcess.list(new HashMap<String, Object>() {{
                    this.put("dkzh", jkr.getDkzh());
                    this.put("cznr", LoanBusinessType.贷款发放.getCode());
                }}, null, null, null, null, null, null);
                if (cLoanHousingBusinessProcesses.size() == 0)
                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "业务记录不存在");
                for (CLoanHousingBusinessProcess cLoanHousing : cLoanHousingBusinessProcesses) {
                    if (cLoanHousing.getLoanContract() != null) {
                        if (!tokenContext.getUserInfo().getYWWD().equals(cLoanHousing.getYwwd().getId()))
                            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "该业务不在当前网点受理范围内");
                    }
                }
            }
        }

        //数据验证
        BorrowerInformation body = getborr.getJKRXX();//借款人信息
        CommonBorrowerInformation information = getborr.getGTJKRXX();//共同借款人信息
        if (body == null) throw new ErrorException(ReturnEnumeration.Data_MISS, "借款人信息为空");
        if (body.getBorrowingInfo() == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "借款人信息不能为空");
        if (body.getProvidentFundAccountInfo() == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "公积金账户信息不能为空");
        if (body.getUnitinfo() == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "单位信息不能为空");
        if (body.getOtherinfo() == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "其他信息不能为空");
        if ("20".equals(body.getBorrowingInfo().getHYZK())) {
            if (information == null) throw new ErrorException(ReturnEnumeration.Data_MISS, "共同借款人信息为空");
            if (information.getCommonBorrowerInformation() == null)
                throw new ErrorException(ReturnEnumeration.Data_MISS, "共同借款人信息不能为空");
            if (information.getCommonProvidentFund() == null)
                throw new ErrorException(ReturnEnumeration.Data_MISS, "公积金账户信息不能为空");
            if (information.getCommonUnitInfo() == null)
                throw new ErrorException(ReturnEnumeration.Data_MISS, "共同借款人单位信息不能为空");
        }

        ObjectAttributeCheck.checkObjects(new HashMap<String, String>() {{
            this.put("借款人姓名", body.getBorrowingInfo().getJKRXM());
            this.put("借款人证件类型", body.getBorrowingInfo().getJKRZJLX());
            this.put("借款人证件号码", body.getBorrowingInfo().getJKRZJHM());
            this.put("出生年月", body.getBorrowingInfo().getCSNY());
            this.put("性别", body.getBorrowingInfo().getXingBie());
            this.put("婚姻状况", body.getBorrowingInfo().getHYZK());
            this.put("月收入", body.getBorrowingInfo().getYSR());
            this.put("手机号码", body.getBorrowingInfo().getSJHM());
            this.put("个人缴存基数", body.getProvidentFundAccountInfo().getGRJCJS());
            this.put("家庭月收入", body.getBorrowingInfo().getJTYSR());
            this.put("缴至年月", body.getBorrowingInfo().getJTZZ());
            this.put("缴存地", body.getJCD());
            this.put("借款人公积金账号", getborr.getJKRGJGZH());
            this.put("单位名称", body.getUnitinfo().getDWMC());
            this.put("单位账号", body.getUnitinfo().getDWZH());
            this.put("单位类别", body.getUnitinfo().getDWLB());
            this.put("还款账号", body.getOtherinfo().getHKZH());
            this.put("账号开户银行名称", body.getOtherinfo().getZHKHYHMC());
            this.put("委托扣划月缴存额", body.getOtherinfo().getWTKHYJCE());
            if ("20".equals(body.getBorrowingInfo().getHYZK())) {
                this.put("共同借款人姓名", information.getCommonBorrowerInformation().getGTJKRXM());
                this.put("共同借款人证件类型：", information.getCommonBorrowerInformation().getGTJKRZJLX());
                this.put("共同借款人证件号码", information.getCommonBorrowerInformation().getGTJKRZJHM());
                this.put("共同借款人手机号码", information.getCommonBorrowerInformation().getSJHM());
                this.put("参贷关系", information.getCDGX());
//                this.put("固定电话号码", information.getCommonBorrowerInformation().getGDDHHM());
            }
        }});

        ObjectAttributeCheck.checkDataType(new HashMap<String, String>() {{
            this.put("月收入", body.getBorrowingInfo().getYSR());
            this.put("家庭月收入", body.getBorrowingInfo().getJTYSR());
            if (StringUtil.notEmpty(body.getProvidentFundAccountInfo().getYJCE())) {
                this.put("月缴存额", body.getProvidentFundAccountInfo().getYJCE());
            }
            this.put("个人缴存基数", body.getProvidentFundAccountInfo().getGRJCJS());
            if (StringUtil.notEmpty(body.getProvidentFundAccountInfo().getGRZHYE())) {
                this.put("个人账户余额", body.getProvidentFundAccountInfo().getGRZHYE());
            }
            if (StringUtil.notEmpty(body.getProvidentFundAccountInfo().getLXZCJCYS())) {
                this.put("连续正常缴存月数", body.getProvidentFundAccountInfo().getLXZCJCYS());
            }
            if ("20".equals(body.getBorrowingInfo().getHYZK())) {
                if (StringUtil.notEmpty(information.getCommonBorrowerInformation().getYSR())) {
                    this.put("共同借款人月收入", information.getCommonBorrowerInformation().getYSR());
                }
                if (StringUtil.notEmpty(information.getCommonProvidentFund().getLXZCJCYS())) {
                    this.put("共同借款人连续正常缴存月数", information.getCommonProvidentFund().getLXZCJCYS());
                }
                if (StringUtil.notEmpty(information.getCommonProvidentFund().getYJCE())) {
                    this.put("共同借款人月缴存额", information.getCommonProvidentFund().getYJCE());
                }
                if (StringUtil.notEmpty(information.getCommonProvidentFund().getGRJCJS())) {
                    this.put("共同借款人个人缴存基数", information.getCommonProvidentFund().getGRJCJS());
                }
                if (StringUtil.notEmpty(information.getCommonProvidentFund().getGRZHYE())) {
                    this.put("共同借款人个人账户余额", information.getCommonProvidentFund().getGRZHYE());
                }
            }
        }});

        try {
            if (body.getBorrowingInfo().getSJHM() == null || body.getBorrowingInfo().getSJHM().length() != 11)
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "手机号码");
            if (StringUtil.isEmpty(body.getBorrowingInfo().getJKRXM()) || body.getBorrowingInfo().getJKRXM().length() < 2)
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "借款人姓名不能小于两位");
            if (body.getBorrowingInfo().getYSR() == null || Double.parseDouble(body.getBorrowingInfo().getYSR()) < 500)
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "月收入必须大于等于500");
            if (body.getBorrowingInfo().getJKRZJLX() == null || !body.getBorrowingInfo().getJKRZJLX().equals("01"))
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "借款人证件类型");
            if (body.getUnitinfo().getDWDZ().trim().length() < 10) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "借款人单位地址长度应大于10");
            }
        } catch (Exception e) {
            throw new ErrorException(e);
        }

        CBankBankInfo bankBankInfo = DAOBuilder.instance(this.icBankBankInfoDAO).searchFilter(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(body.getOtherinfo().getZHKHYHMC()))
                this.put("bank_name", body.getOtherinfo().getZHKHYHMC());
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (bankBankInfo == null) throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "没有此银行名称代码");

        //业务唯一性判断
        List<CLoanHousingBusinessProcess> anHousingBusinessProcess = cloanHousingBusinessProcess.list(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(getborr.getDKZH()))
                this.put("dkzh", getborr.getDKZH());
            this.put("cznr", LoanBusinessType.合同变更.getCode());
        }}, null, null, null, null, null, null);
        for (CLoanHousingBusinessProcess hsingBusinessProcess : anHousingBusinessProcess) {
            if (!LoanBussinessStatus.办结.getName().equals(hsingBusinessProcess.getStep())) {
                throw new ErrorException(ReturnEnumeration.Business_In_Process, hsingBusinessProcess.getYwlsh());
            }
        }

        String id = null;
        CLoanHousingBusinessProcess loanHousingBusinessProcess = new CLoanHousingBusinessProcess();
        loanHousingBusinessProcess.setCzy(tokenContext.getUserInfo().getCZY());//操作员
        CAccountNetwork network = DAOBuilder.instance(cAccountNetworkDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("id", tokenContext.getUserInfo().getYWWD());
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        loanHousingBusinessProcess.setYwwd(network);//业务网点
        loanHousingBusinessProcess.setBlsj(new Date());//办理时间
        loanHousingBusinessProcess.setStep(LoanBussinessStatus.初始状态.getName());
        loanHousingBusinessProcess.setCznr(LoanBusinessType.合同变更.getCode());//业务类型
        loanHousingBusinessProcess.setDkzh(getborr.getDKZH());//贷款账号
        CLoanHousingPersonInformationVice loanHousingPersonInformationVice = new CLoanHousingPersonInformationVice();
        CLoanFundsVice fundsVice = null;
        try {
            loanHousingPersonInformationVice.setJkrxm(body.getBorrowingInfo().getJKRXM()); //姓名
            loanHousingPersonInformationVice.setJkrzjlx(body.getBorrowingInfo().getJKRZJLX());//身份证
            if (IdcardValidator.isValidatedAllIdcard(body.getBorrowingInfo().getJKRZJHM())) {
                loanHousingPersonInformationVice.setJkrzjhm(body.getBorrowingInfo().getJKRZJHM());
            } else {
                throw new ErrorException(ReturnEnumeration.Data_MISS, "借款人身份证格式有问题");
            }
            loanHousingPersonInformationVice.setXingBie(body.getBorrowingInfo().getXingBie().charAt(0));//性别
            loanHousingPersonInformationVice.setXueLi(body.getBorrowingInfo().getXueLi());//学历
            loanHousingPersonInformationVice.setHyzk(body.getBorrowingInfo().getHYZK());//婚姻状况
            loanHousingPersonInformationVice.setZhiCheng(body.getBorrowingInfo().getZhiCheng());//职称
            loanHousingPersonInformationVice.setZhiWu(body.getBorrowingInfo().getZhiWu());//职务
            loanHousingPersonInformationVice.setGddhhm(body.getBorrowingInfo().getGDDHHM());//固定电话
            loanHousingPersonInformationVice.setSjhm(body.getBorrowingInfo().getSJHM());//手机号码
            loanHousingPersonInformationVice.setJtzz(body.getBorrowingInfo().getJTZZ());//家庭地址
            loanHousingPersonInformationVice.setHkszd(body.getBorrowingInfo().getHKSZD());//户口所在地
            loanHousingPersonInformationVice.setJcd(body.getJCD());//缴存地
            loanHousingPersonInformationVice.setNianLing(body.getBorrowingInfo().getNinaLing());//年龄
            loanHousingPersonInformationVice.setJkzk(body.getBorrowingInfo().getJKZK());//健康状况
            loanHousingPersonInformationVice.setYgxz(body.getBorrowingInfo().getYGXZ());//用工性质
            loanHousingPersonInformationVice.setZyjjly(body.getBorrowingInfo().getZYJJLY());//主要经济来源
            loanHousingPersonInformationVice.setYsr(StringUtil.notEmpty(body.getBorrowingInfo().getYSR()) ? new BigDecimal(body.getBorrowingInfo().getYSR()) : BigDecimal.ZERO);//月收入
            loanHousingPersonInformationVice.setJtysr(StringUtil.notEmpty(body.getBorrowingInfo().getJTYSR()) ? new BigDecimal(body.getBorrowingInfo().getJTYSR()) : BigDecimal.ZERO);//家庭月收入
            loanHousingPersonInformationVice.setBlzl(body.getBLZL());//办理资料
            loanHousingPersonInformationVice.setJkrgjjzh(getborr.getJKRGJGZH());//借款人公积金账号
            loanHousingPersonInformationVice.setJkhtbh(getborr.getJKHTBH());//借款合同编号
            loanHousingPersonInformationVice.setGrzhzt(body.getProvidentFundAccountInfo().getGRZHZT());//个人账户状态
            try {
                loanHousingPersonInformationVice.setJzny(simM.format(simview.parse(body.getProvidentFundAccountInfo().getJZNY())));//交织年月
            } catch (Exception e) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "借款人缴至年月");
            }
            try {
                loanHousingPersonInformationVice.setCsny(simM.format(simview.parse(body.getBorrowingInfo().getCSNY())));
            } catch (Exception e) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "借款人出生年月");
            }
            loanHousingPersonInformationVice.setYjce(StringUtil.notEmpty(body.getProvidentFundAccountInfo().getYJCE()) ? new BigDecimal(body.getProvidentFundAccountInfo().getYJCE()) : BigDecimal.ZERO);//月缴存额
            loanHousingPersonInformationVice.setGrjcjs(StringUtil.notEmpty(body.getProvidentFundAccountInfo().getGRJCJS()) ? new BigDecimal(body.getProvidentFundAccountInfo().getGRJCJS()) : BigDecimal.ZERO);//个人缴存基数
            loanHousingPersonInformationVice.setGrzhye(StringUtil.notEmpty(body.getProvidentFundAccountInfo().getGRZHYE()) ? new BigDecimal(body.getProvidentFundAccountInfo().getGRZHYE()) : BigDecimal.ZERO);//个人账户余额
            loanHousingPersonInformationVice.setLxzcjcys(StringUtil.notEmpty(body.getProvidentFundAccountInfo().getLXZCJCYS()) ? new BigDecimal(body.getProvidentFundAccountInfo().getLXZCJCYS()) : BigDecimal.ZERO);//连续缴存约束
            loanHousingPersonInformationVice.setDwmc(body.getUnitinfo().getDWMC());//单位名称
            loanHousingPersonInformationVice.setDwzh(body.getUnitinfo().getDWZH());//单位账号
            loanHousingPersonInformationVice.setDwdh(body.getUnitinfo().getDWDH());//单位电话
            loanHousingPersonInformationVice.setDwxz(body.getUnitinfo().getDWLB());//单位性质
            loanHousingPersonInformationVice.setDwdz(body.getUnitinfo().getDWDZ());//单位地址
            loanHousingPersonInformationVice.setBlzl(body.getBLZL());//办理资料
            loanHousingPersonInformationVice.setHkzh(body.getOtherinfo().getHKZH());
            loanHousingPersonInformationVice.setZhkhyhmc(body.getOtherinfo().getZHKHYHMC());//账户开户银行名称
            if (StringUtil.isEmpty(body.getOtherinfo().getHKZHHM())) throw new ErrorException("还款账号户名缺失");
            loanHousingPersonInformationVice.setHkzhhm(body.getOtherinfo().getHKZHHM());

            //其他信息
            fundsVice = new CLoanFundsVice();//
            fundsVice.setWtkhyjce("1".equals(body.getOtherinfo().getWTKHYJCE()) ? true : false);//是否委托扣划

            if ("20".equals(body.getBorrowingInfo().getHYZK())) {
                CLoanHousingCoborrowerVice coanHousingCoborrowerVice = new CLoanHousingCoborrowerVice();//贷款发放-共同借款人信息表
                coanHousingCoborrowerVice.setCdgx(information.getCDGX());
                coanHousingCoborrowerVice.setGtjkrgjjzh(information.getGTJKRGJJZH());
                coanHousingCoborrowerVice.setJcd(information.getJCD());
                if (information.getCommonBorrowerInformation().getGTJKRXM() == null || information.getCommonBorrowerInformation().getGTJKRXM().equals(body.getBorrowingInfo().getJKRXM()))
                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "共同借款人姓名");
                if (information.getCommonBorrowerInformation().getGTJKRZJHM() == null || information.getCommonBorrowerInformation().getGTJKRZJHM().equals(body.getBorrowingInfo().getJKRZJHM()))
                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "共同借款人证件号码");
                if (information.getCommonBorrowerInformation().getGTJKRZJLX() == null || !information.getCommonBorrowerInformation().getGTJKRZJLX().equals("01"))
                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "共同借款人证件类型");
                if (information.getCommonUnitInfo().getDWDZ() != null && !information.getCommonUnitInfo().getDWDZ().trim().equals("") && information.getCommonUnitInfo().getDWDZ().trim().length() < 10) {
                    throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "共同借款人单位地址长度应大于10");
                }
                coanHousingCoborrowerVice.setGtjkrxm(information.getCommonBorrowerInformation().getGTJKRXM());
                coanHousingCoborrowerVice.setGtjkrzjlx(information.getCommonBorrowerInformation().getGTJKRZJLX());
                if (IdcardValidator.isValidatedAllIdcard(information.getCommonBorrowerInformation().getGTJKRZJHM())) {
                    coanHousingCoborrowerVice.setGtjkrzjhm(information.getCommonBorrowerInformation().getGTJKRZJHM());
                } else {
                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "共同借款人身份证格式有问题");
                }
                coanHousingCoborrowerVice.setGddhhm(information.getCommonBorrowerInformation().getGDDHHM());
                if (information.getCommonBorrowerInformation().getSJHM() == null || information.getCommonBorrowerInformation().getSJHM().length() != 11)
                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "共同借款人手机号码");
                coanHousingCoborrowerVice.setSjhm(information.getCommonBorrowerInformation().getSJHM());
                if (information.getCommonBorrowerInformation().getYSR() == null || Double.parseDouble(information.getCommonBorrowerInformation().getYSR()) < 0)
                    throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "共同借款人月收入必须大于等于0");
                coanHousingCoborrowerVice.setYsr(new BigDecimal(information.getCommonBorrowerInformation().getYSR()));
                coanHousingCoborrowerVice.setHkszd(information.getCommonBorrowerInformation().getHKSZD());
                coanHousingCoborrowerVice.setDwmc(information.getCommonUnitInfo().getDWMC());
                coanHousingCoborrowerVice.setDwzh(information.getCommonUnitInfo().getDWZH());
                coanHousingCoborrowerVice.setDwdh(information.getCommonUnitInfo().getDWDH());
                coanHousingCoborrowerVice.setDwxz(information.getCommonUnitInfo().getDWLB());
                coanHousingCoborrowerVice.setDwdz(information.getCommonUnitInfo().getDWDZ());
                coanHousingCoborrowerVice.setGrzhzt(information.getCommonProvidentFund().getGRZHZT());
                try {
                    if (StringUtil.notEmpty(information.getCommonProvidentFund().getJZNY())) {
                        coanHousingCoborrowerVice.setJzny(simM.format(simview.parse(information.getCommonProvidentFund().getJZNY())));
                    }
                } catch (Exception e) {
                    throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "共同借款人缴至年月");
                }
                coanHousingCoborrowerVice.setLxzcjcys(StringUtil.notEmpty(information.getCommonProvidentFund().getLXZCJCYS()) ? new BigDecimal(information.getCommonProvidentFund().getLXZCJCYS()) : BigDecimal.ZERO);
                coanHousingCoborrowerVice.setYjce(StringUtil.notEmpty(information.getCommonProvidentFund().getYJCE()) ? new BigDecimal(information.getCommonProvidentFund().getYJCE()) : BigDecimal.ZERO);
                coanHousingCoborrowerVice.setGrjcjs(StringUtil.notEmpty(information.getCommonProvidentFund().getGRJCJS()) ? new BigDecimal(information.getCommonProvidentFund().getGRJCJS()) : BigDecimal.ZERO);
                coanHousingCoborrowerVice.setGrzhye(StringUtil.notEmpty(information.getCommonProvidentFund().getGRZHYE()) ? new BigDecimal(information.getCommonProvidentFund().getGRZHYE()) : BigDecimal.ZERO);
                coanHousingCoborrowerVice.setBlzl(information.getBLZL());
                loanHousingBusinessProcess.setLoanHousingCoborrowerVice(coanHousingCoborrowerVice);
                coanHousingCoborrowerVice.setGrywmx(loanHousingBusinessProcess);
            }
        } catch (Exception e) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, e.getMessage());
        }

        fundsVice.setGrywmx(loanHousingBusinessProcess);
        loanHousingPersonInformationVice.setGrywmx(loanHousingBusinessProcess);
        loanHousingBusinessProcess.setLoanFundsVice(fundsVice);
        loanHousingBusinessProcess.setLoanHousingPersonInformationVice(loanHousingPersonInformationVice);
        id = cloanHousingBusinessProcess.save(loanHousingBusinessProcess);

        CLoanHousingBusinessProcess hingBusinessProcess = cloanHousingBusinessProcess.get(id);

        iSaveAuditHistory.saveNormalBusiness(hingBusinessProcess.getYwlsh(), tokenContext, LoanBusinessType.合同变更.getName(), "新建");

        StateMachineUtils.updateState(iStateMachineService, action.equals("0") ? Events.通过.getEvent() : Events.提交.getEvent(),
                new TaskEntity() {{
                    this.setOperator(tokenContext.getUserInfo().getCZY());
                    this.setStatus(hingBusinessProcess.getStep());
                    this.setTaskId(hingBusinessProcess.getYwlsh());
                    this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                    this.setSubtype(BusinessSubType.贷款_合同变更申请.getSubType());
                    this.setType(com.handge.housingfund.database.enums.BusinessType.Loan);
                    this.setWorkstation(tokenContext.getUserInfo().getYWWD());
                }}, new StateMachineUtils.StateChangeHandler() {
                    @Override
                    public void onStateChange(boolean succeed, String next, Exception e) {
                        if (e != null) {
                            throw new ErrorException(e);
                        }
                        if (!succeed || next == null) {
                            return;
                        }
                        if (succeed) {
                            if (StringUtil.isIntoReview(next, null))
                                hingBusinessProcess.setDdsj(new Date());
                            if (LoanBussinessStatus.办结.getName().equals(next))
                                doAction(tokenContext, hingBusinessProcess.getYwlsh());
                            hingBusinessProcess.setStep(next);
                            cloanHousingBusinessProcess.update(hingBusinessProcess);

                        }
                    }
                });

        CommonResponses comm = new CommonResponses();
        comm.setId(hingBusinessProcess.getYwlsh());
        comm.setState("sucess");
        return comm;
    }


    private HashMap<String, ArrayList<String>> showDiff(CLoanHousingBusinessProcess cLoanHousingBusinessProcess) {
        HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
        ArrayList<String> jkrarray = new ArrayList<>();
        ArrayList<String> gtjkrarray = new ArrayList<>();
        CLoanHousingPersonInformationVice loanHousingPersonInformationVice = cLoanHousingBusinessProcess.getLoanHousingPersonInformationVice();
        CLoanHousingCoborrowerVice loanHousingCoborrowerVice = cLoanHousingBusinessProcess.getLoanHousingCoborrowerVice();
        CLoanFundsVice loanFundsVice = cLoanHousingBusinessProcess.getLoanFundsVice();

        List<CLoanHousingPersonInformationBasic> list = cloanHousingPersonInformationBasic.list(new HashMap<String, Object>() {{
            this.put("dkzh", cLoanHousingBusinessProcess.getDkzh());
        }}, null, null, null, null, null, null);

        if (list.size() == 0)
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "贷款账号不存在：" + cLoanHousingBusinessProcess.getDkzh());

        CLoanHousingPersonInformationBasic personInformationBasic = list.get(0);
        StHousingCoborrower coborrower = personInformationBasic.getCoborrower();
        CLoanFundsInformationBasic funds = personInformationBasic.getFunds();
        StHousingPersonalLoan loanContract = personInformationBasic.getLoanContract();

        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getJcd(), personInformationBasic.getJcd()))
            jkrarray.add("JCD");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getJkrzjhm(), personInformationBasic.getJkrzjhm()))
            jkrarray.add("JKRZJHM");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getJkrgjjzh(), personInformationBasic.getJkrgjjzh()))
            jkrarray.add("JKRGJJZH");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getJkrxm(), personInformationBasic.getJkrxm()))
            jkrarray.add("JKRXM");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getJkrzjlx(), personInformationBasic.getJkrzjlx()))
            jkrarray.add("JKRZJLX");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getJkrzjhm(), personInformationBasic.getJkrzjhm()))
            jkrarray.add("JKRZJHM");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getCsny(), personInformationBasic.getCsny()))
            jkrarray.add("CSNY");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getXingBie(), personInformationBasic.getXingBie()))
            jkrarray.add("XingBie");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getXueLi(), personInformationBasic.getXueLi()))
            jkrarray.add("XueLi");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getNianLing(), personInformationBasic.getNianLing()))
            jkrarray.add("NianLing");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getJkzk(), personInformationBasic.getJkzk()))
            jkrarray.add("JKZK");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getHyzk(), personInformationBasic.getHyzk()))
            jkrarray.add("HYZK");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getZhiCheng(), personInformationBasic.getZhiCheng()))
            jkrarray.add("ZhiCheng");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getZhiWu(), personInformationBasic.getZhiWu()))
            jkrarray.add("ZhiWu");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getYgxz(), personInformationBasic.getYgxz()))
            jkrarray.add("ygxz");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getZyjjly(), personInformationBasic.getZyjjly()))
            jkrarray.add("ZYJJLY");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getYsr(), personInformationBasic.getYsr()))
            jkrarray.add("YSR");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getJtysr(), personInformationBasic.getJtysr()))
            jkrarray.add("JTYSR");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getGddhhm(), personInformationBasic.getGddhhm()))
            jkrarray.add("GDDHHM");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getSjhm(), personInformationBasic.getSjhm()))
            jkrarray.add("SJHM");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getJtzz(), personInformationBasic.getJtzz()))
            jkrarray.add("JTZZ");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getHkszd(), personInformationBasic.getHkszd()))
            jkrarray.add("HKSZD");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getJzny(), personInformationBasic.getJzny()))
            jkrarray.add("JZNY");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getGrzhzt(), personInformationBasic.getGrzhzt()))
            jkrarray.add("GRZHZT");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getLxzcjcys(), personInformationBasic.getLxzcjcys()))
            jkrarray.add("LXZCJCYS");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getYjce(), personInformationBasic.getYjce()))
            jkrarray.add("YJCE");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getGrjcjs(), personInformationBasic.getGrjcjs()))
            jkrarray.add("GRJCJS");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getGrzhye(), personInformationBasic.getGrzhye()))
            jkrarray.add("GRZHYE");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getDwmc(), personInformationBasic.getDwmc()))
            jkrarray.add("DWMC");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getDwzh(), personInformationBasic.getDwzh()))
            jkrarray.add("DWZH");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getDwdh(), personInformationBasic.getDwdh()))
            jkrarray.add("DWDH");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getDwxz(), personInformationBasic.getDwxz()))
            jkrarray.add("DWXZ");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getDwdz(), personInformationBasic.getDwdz()))
            jkrarray.add("DWDZ");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getZhkhyhmc(), loanContract.getZhkhyhmc()))
            jkrarray.add("ZHKHYHMC");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getHkzh(), loanContract.getHkzh()))
            jkrarray.add("HKZH");
        if (!StringUtil.stringEquals(loanHousingPersonInformationVice.getHkzhhm(), personInformationBasic.getLoanContract().getcLoanHousingPersonalLoanExtension().getHkzhhm()))
            jkrarray.add("HKZHHM");
        if (!StringUtil.stringEquals(loanFundsVice.getWtkhyjce(), funds.getWtkhyjce())) jkrarray.add("WTKHYJCE");


        if ("20".equals(cLoanHousingBusinessProcess.getLoanHousingPersonInformationVice().getHyzk()) && "20".equals(personInformationBasic.getHyzk())) {
            if (!StringUtil.stringEquals(loanHousingCoborrowerVice.getGtjkrzjhm(), coborrower.getGtjkrzjhm()))
                gtjkrarray.add("GTJKRZJHM");
            if (!StringUtil.stringEquals(loanHousingCoborrowerVice.getGtjkrgjjzh(), coborrower.getGtjkrgjjzh()))
                gtjkrarray.add("GTJKRGJJZH");
            if (!StringUtil.stringEquals(loanHousingCoborrowerVice.getCdgx(), coborrower.getCdgx()))
                gtjkrarray.add("CDGX");
            if (!StringUtil.stringEquals(loanHousingCoborrowerVice.getGtjkrxm(), coborrower.getGtjkrxm()))
                gtjkrarray.add("GTJKRXM");
            if (!StringUtil.stringEquals(loanHousingCoborrowerVice.getGtjkrzjlx(), coborrower.getGtjkrzjlx()))
                gtjkrarray.add("GTJKRZJLX");
            if (!StringUtil.stringEquals(loanHousingCoborrowerVice.getGtjkrzjhm(), coborrower.getGtjkrzjhm()))
                gtjkrarray.add("GTJKRZJHM");
            if (!StringUtil.stringEquals(loanHousingCoborrowerVice.getYsr(), coborrower.getYsr()))
                gtjkrarray.add("YSR");
            if (!StringUtil.stringEquals(loanHousingCoborrowerVice.getGddhhm(), coborrower.getGddhhm()))
                gtjkrarray.add("GDDHHM");
            if (!StringUtil.stringEquals(loanHousingCoborrowerVice.getSjhm(), coborrower.getSjhm()))
                gtjkrarray.add("SJHM");
            if (!StringUtil.stringEquals(loanHousingCoborrowerVice.getHkszd(), coborrower.getExtension().getHkszd()))
                gtjkrarray.add("HKSZD");
            if (!StringUtil.stringEquals(loanHousingCoborrowerVice.getJcd(), coborrower.getExtension().getJcd()))
                gtjkrarray.add("JCD");
            if (!StringUtil.stringEquals(loanHousingCoborrowerVice.getDwmc(), coborrower.getExtension().getDwmc()))
                gtjkrarray.add("DWMC");
            if (!StringUtil.stringEquals(loanHousingCoborrowerVice.getDwzh(), coborrower.getExtension().getDwzh()))
                gtjkrarray.add("DWZH");
            if (!StringUtil.stringEquals(loanHousingCoborrowerVice.getDwdh(), coborrower.getExtension().getDwdh()))
                gtjkrarray.add("DWDH");
            if (!StringUtil.stringEquals(loanHousingCoborrowerVice.getDwxz(), coborrower.getExtension().getDwxz()))
                gtjkrarray.add("DWXZ");
            if (!StringUtil.stringEquals(loanHousingCoborrowerVice.getDwdz(), coborrower.getExtension().getDwdz()))
                gtjkrarray.add("DWDZ");
            if (!StringUtil.stringEquals(loanHousingCoborrowerVice.getJzny(), coborrower.getExtension().getJzny()))
                gtjkrarray.add("JZNY");
            if (!StringUtil.stringEquals(loanHousingCoborrowerVice.getGrzhzt(), coborrower.getExtension().getGrzhzt()))
                gtjkrarray.add("GRZHZT");
            if (!StringUtil.stringEquals(loanHousingCoborrowerVice.getLxzcjcys(), coborrower.getExtension().getLxzcjcys()))
                gtjkrarray.add("LXZCJCYS");
            if (!StringUtil.stringEquals(loanHousingCoborrowerVice.getYjce(), coborrower.getExtension().getYjce()))
                gtjkrarray.add("YJCE");
            if (!StringUtil.stringEquals(loanHousingCoborrowerVice.getGrjcjs(), coborrower.getExtension().getGrjcjs()))
                gtjkrarray.add("GRJCJS");
            if (!StringUtil.stringEquals(loanHousingCoborrowerVice.getGrzhye(), coborrower.getExtension().getGrzhye()))
                gtjkrarray.add("GRZHYE");
        }
        map.put("jkr", jkrarray);
        map.put("gtjkr", gtjkrarray);
        return map;
    }

    /**
     * 借款人信息(共同借款人)详情2（先查询）
     * //
     */
    @Override
    public final GetBorrowerInformation getBorrowerInformation(TokenContext tokenContext, String DKZH) {
        List<CLoanHousingPersonInformationBasic> lanHousingPersonInformationBasic = cloanHousingPersonInformationBasic.list(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(DKZH)) this.put("dkzh", DKZH);
        }}, null, null, null, null, null, null);
        if (lanHousingPersonInformationBasic.size() == 0)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "不存在此贷款账号" + ":" + DKZH);
        //asc升序
        CLoanHousingPersonInformationBasic loanHousingPersonInformationBasic = lanHousingPersonInformationBasic.get(lanHousingPersonInformationBasic.size() - 1);//借款人信息,得到最新的记录
        StHousingCoborrower stHousingCoborrower = loanHousingPersonInformationBasic.getCoborrower();//共同借款人
        if ("20".equals(loanHousingPersonInformationBasic.getHyzk())) {
            if (stHousingCoborrower == null) throw new ErrorException(ReturnEnumeration.Data_MISS, "原合同共同借款人信息丢失");
            if (stHousingCoborrower.getExtension() == null)
                throw new ErrorException(ReturnEnumeration.Data_MISS, "原合同共同借款人拓展信息丢失");
        }
        if (loanHousingPersonInformationBasic.getFunds() == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "原合同资金信息丢失");
        if (loanHousingPersonInformationBasic.getGuaranteeContract() == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "原担保合同信息信息丢失");
        if (loanHousingPersonInformationBasic.getLoanContract() == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "原合同借款合同信息丢失");
        if (loanHousingPersonInformationBasic.getPersonalAccount() == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "原合同贷款账号信息丢失");
        if ("0".equals(loanHousingPersonInformationBasic.getDkyt())) {
            if (loanHousingPersonInformationBasic.getPurchasing() == null)
                throw new ErrorException(ReturnEnumeration.Data_MISS, "原基础-房屋购买信息表信息丢失");
        } else if ("2".equals(loanHousingPersonInformationBasic.getDkyt())) {
            if (loanHousingPersonInformationBasic.getOverhaul() == null)
                throw new ErrorException(ReturnEnumeration.Data_MISS, "原基础-房屋大修信息信息丢失");
        } else if ("1".equals(loanHousingPersonInformationBasic.getDkyt())) {
            if (loanHousingPersonInformationBasic.getBuild() == null)
                throw new ErrorException(ReturnEnumeration.Data_MISS, "原基础-房屋自建、翻修信息丢失");
        }

        return new GetBorrowerInformation() {{
            this.setJKRXX(new BorrowerInformation() {{
                this.setJCD(loanHousingPersonInformationBasic.getJcd());
                this.setBLZL(loanHousingPersonInformationBasic.getBlzl());
                //借款人信息
                this.setBorrowingInfo(new BorrowingInfo() {{
                    this.setJKRXM(loanHousingPersonInformationBasic.getJkrxm());
                    this.setJKRZJLX(loanHousingPersonInformationBasic.getJkrzjlx());
                    this.setJKRZJHM(loanHousingPersonInformationBasic.getJkrzjhm());
                    try {
                        this.setCSNY(simview.format(simM.parse(loanHousingPersonInformationBasic.getCsny())));
                    } catch (Exception e) {
                        this.setCSNY("");
                    }
                    this.setXingBie(loanHousingPersonInformationBasic.getXingBie() + "");
                    this.setXueLi(loanHousingPersonInformationBasic.getXueLi());
                    this.setNinaLing(loanHousingPersonInformationBasic.getNianLing());
                    this.setJKZK(loanHousingPersonInformationBasic.getJkzk());
                    this.setHYZK(loanHousingPersonInformationBasic.getHyzk());
                    this.setZhiCheng(loanHousingPersonInformationBasic.getZhiCheng());
                    this.setZhiWu(loanHousingPersonInformationBasic.getZhiWu());
                    this.setYGXZ(loanHousingPersonInformationBasic.getYgxz());
                    this.setZYJJLY(loanHousingPersonInformationBasic.getZyjjly());
                    this.setYSR(loanHousingPersonInformationBasic.getYsr() + "");
                    this.setJTYSR(loanHousingPersonInformationBasic.getJtysr() + "");
                    this.setGDDHHM(loanHousingPersonInformationBasic.getGddhhm());
                    this.setSJHM(loanHousingPersonInformationBasic.getSjhm());
                    this.setJTZZ(loanHousingPersonInformationBasic.getJtzz());
                    this.setHKSZD(loanHousingPersonInformationBasic.getHkszd());
                }});
                //公积金账户信息
                this.setProvidentFundAccountInfo(new ProvidentFundAccountInfo() {{
                    try {
                        this.setJZNY(simview.format(simM.parse(loanHousingPersonInformationBasic.getJzny())));
                    } catch (Exception e) {
                        this.setJZNY("");
                    }
                    if (StringUtil.notEmpty(loanHousingPersonInformationBasic.getJkrgjjzh()) && common_person.getByGrzh(loanHousingPersonInformationBasic.getJkrgjjzh()) != null) {
                        this.setGRZHZT(common_person.getByGrzh(loanHousingPersonInformationBasic.getJkrgjjzh()).getCollectionPersonalAccount().getGrzhzt());
                    }
                    this.setLXZCJCYS(loanHousingPersonInformationBasic.getLxzcjcys() + "");
                    this.setYJCE(loanHousingPersonInformationBasic.getYjce() + "");
                    this.setGRJCJS(loanHousingPersonInformationBasic.getGrjcjs() + "");
                    this.setGRZHYE(loanHousingPersonInformationBasic.getGrzhye() + "");
                }});
                //单位信息
                this.setUnitinfo(new UnitInfos() {{
                    this.setDWMC(loanHousingPersonInformationBasic.getDwmc());
                    this.setDWZH(loanHousingPersonInformationBasic.getDwzh());
                    this.setDWDH(loanHousingPersonInformationBasic.getDwdh());
                    this.setDWLB(loanHousingPersonInformationBasic.getDwxz());
                    this.setDWDZ(loanHousingPersonInformationBasic.getDwdz());
                }});
                //其他信息
                this.setOtherinfo(new OthersInfo() {{
                    this.setHKZH(loanHousingPersonInformationBasic.getLoanContract().getHkzh());
                    this.setZHKHYHMC(loanHousingPersonInformationBasic.getLoanContract().getZhkhyhmc());
                    this.setHKZHHM(loanHousingPersonInformationBasic.getLoanContract().getcLoanHousingPersonalLoanExtension().getHkzhhm());
                    this.setWTKHYJCE(true == loanHousingPersonInformationBasic.getFunds().getWtkhyjce() ? "1" : "0");
                }});
            }});
            if ("20".equals(loanHousingPersonInformationBasic.getHyzk())) {
                this.setGTJKRXX(new CommonBorrowerInformation() {{
                    this.setCDGX(stHousingCoborrower.getCdgx());
                    this.setGTJKRGJJZH(stHousingCoborrower.getGtjkrgjjzh());
                    this.setJCD(stHousingCoborrower.getExtension().getJcd());
                    this.setBLZL(stHousingCoborrower.getExtension().getBlzl());
                    //共同借款人信息
                    this.setCommonBorrowerInformation(new CommonBorrowerInfo() {{
                        this.setGTJKRXM(stHousingCoborrower.getGtjkrxm());
                        this.setGTJKRZJLX(stHousingCoborrower.getGtjkrzjlx());
                        this.setGTJKRZJHM(stHousingCoborrower.getGtjkrzjhm());
                        this.setGDDHHM(stHousingCoborrower.getGddhhm());
                        this.setSJHM(stHousingCoborrower.getSjhm());
                        this.setYSR(stHousingCoborrower.getYsr() + "");
                        this.setHKSZD(stHousingCoborrower.getExtension().getHkszd());
                    }});
                    //单位信息
                    this.setCommonUnitInfo(new CommonUnitInfo() {{
                        this.setDWMC(stHousingCoborrower.getExtension().getDwmc());
                        this.setDWDH(stHousingCoborrower.getExtension().getDwdh());
                        this.setDWZH(stHousingCoborrower.getExtension().getDwzh());
                        this.setDWLB(stHousingCoborrower.getExtension().getDwxz());
                        this.setDWDZ(stHousingCoborrower.getExtension().getDwdz());
                    }});
                    //公积金账户信息
                    this.setCommonProvidentFund(new CommonProvidentFund() {{
                        try {
                            this.setJZNY(simview.format(simM.parse(stHousingCoborrower.getExtension().getJzny())));
                        } catch (Exception e) {
                            this.setJZNY("");
                        }
                        if (StringUtil.notEmpty(stHousingCoborrower.getGtjkrgjjzh()) && common_person.getByGrzh(stHousingCoborrower.getGtjkrgjjzh()) != null) {
                            this.setGRZHZT(common_person.getByGrzh(stHousingCoborrower.getGtjkrgjjzh()).getCollectionPersonalAccount().getGrzhzt());
                        }
                        this.setLXZCJCYS(stHousingCoborrower.getExtension().getLxzcjcys() + "");
                        this.setYJCE(stHousingCoborrower.getExtension().getYjce() + "");
                        this.setGRJCJS(stHousingCoborrower.getExtension().getGrjcjs() + "");
                        this.setGRZHYE(stHousingCoborrower.getExtension().getGrzhye() + "");
                    }});
                }});
            }
//            this.setCZY(loanHousingPersonInformationBasic.getCzy());
//            this.setYWWD(loanHousingPersonInformationBasic.getYwwd());
            this.setJKRGJGZH(loanHousingPersonInformationBasic.getJkrgjjzh());
            this.setJKHTBH(loanHousingPersonInformationBasic.getLoanContract().getJkhtbh());
        }};

    }

    /**
     * 借款人信息(共同借款人)变更（修改请求）
     * //
     */
    @Override
    public final CommonResponses putBorrowerInformation(TokenContext tokenContext, String action, String YWLSH, GetBorrowerInformation getborr) {
        if (action.equals("1")) {
            if (!iUploadImagesService.validateUploadFile(UploadFileModleType.贷款.getCode(),
                    UploadFileBusinessType.合同变更.getCode(), getborr.getJKRXX().getBLZL())) {
                throw new ErrorException(ReturnEnumeration.Data_MISS, "资料未上传");
            }

            if (getborr.getGTJKRXX() != null) {
                if ("20".equals(getborr.getJKRXX().getBorrowingInfo().getHYZK())) {
                    if (!iUploadImagesService.validateUploadFile(UploadFileModleType.贷款.getCode(),
                            UploadFileBusinessType.合同变更.getCode(), getborr.getGTJKRXX().getBLZL())) {
                        throw new ErrorException(ReturnEnumeration.Data_MISS, "资料未上传");
                    }
                }
            }
        }
        //数据验证
        BorrowerInformation body = getborr.getJKRXX();//借款人信息
        CommonBorrowerInformation information = getborr.getGTJKRXX();//共同借款人信息
        if (body == null) throw new ErrorException(ReturnEnumeration.Data_MISS, "借款人信息为空");
        if (body.getBorrowingInfo() == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "借款人信息不能为空");
        if (body.getProvidentFundAccountInfo() == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "公积金账户信息不能为空");
        if (body.getUnitinfo() == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "单位信息不能为空");
        if (body.getOtherinfo() == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "其他信息不能为空");
        if ("20".equals(body.getBorrowingInfo().getHYZK())) {
            if (information == null) throw new ErrorException(ReturnEnumeration.Data_MISS, "共同借款人信息为空");
            if (information.getCommonBorrowerInformation() == null)
                throw new ErrorException(ReturnEnumeration.Data_MISS, "共同借款人信息不能为空");
            if (information.getCommonProvidentFund() == null)
                throw new ErrorException(ReturnEnumeration.Data_MISS, "不能为空");
            if (information.getCommonUnitInfo() == null)
                throw new ErrorException(ReturnEnumeration.Data_MISS, "共同借款人单位信息不能为空");
        }

        ObjectAttributeCheck.checkObjects(new HashMap<String, String>() {{
            this.put("借款人姓名", body.getBorrowingInfo().getJKRXM());
            this.put("借款人证件类型", body.getBorrowingInfo().getJKRZJLX());
            this.put("借款人证件号码", body.getBorrowingInfo().getJKRZJHM());
            this.put("出生年月", body.getBorrowingInfo().getCSNY());
            this.put("性别", body.getBorrowingInfo().getXingBie());
            this.put("婚姻状况", body.getBorrowingInfo().getHYZK());
            this.put("月收入", body.getBorrowingInfo().getYSR());
            this.put("手机号码", body.getBorrowingInfo().getSJHM());
            this.put("个人缴存基数", body.getProvidentFundAccountInfo().getGRJCJS());
            this.put("家庭月收入", body.getBorrowingInfo().getJTYSR());
            this.put("缴至年月", body.getBorrowingInfo().getJTZZ());
            this.put("缴存地", body.getJCD());
            this.put("借款人公积金账号", getborr.getJKRGJGZH());
            this.put("单位名称", body.getUnitinfo().getDWMC());
            this.put("单位账号", body.getUnitinfo().getDWZH());
            this.put("单位类别", body.getUnitinfo().getDWLB());
            this.put("还款账号", body.getOtherinfo().getHKZH());
            this.put("账号开户银行名称", body.getOtherinfo().getZHKHYHMC());
            this.put("委托扣划月缴存额", body.getOtherinfo().getWTKHYJCE());
            if ("20".equals(body.getBorrowingInfo().getHYZK())) {
                this.put("共同借款人姓名", information.getCommonBorrowerInformation().getGTJKRXM());
                this.put("共同借款人证件类型：", information.getCommonBorrowerInformation().getGTJKRZJLX());
                this.put("共同借款人证件号码", information.getCommonBorrowerInformation().getGTJKRZJHM());
                this.put("共同借款人手机号码", information.getCommonBorrowerInformation().getSJHM());
                this.put("参贷关系", information.getCDGX());
//                this.put("固定电话号码", information.getCommonBorrowerInformation().getGDDHHM());
            }
        }});

        ObjectAttributeCheck.checkDataType(new HashMap<String, String>() {{
            this.put("月收入", body.getBorrowingInfo().getYSR());
            this.put("家庭月收入", body.getBorrowingInfo().getJTYSR());
            if (StringUtil.notEmpty(body.getProvidentFundAccountInfo().getYJCE())) {
                this.put("月缴存额", body.getProvidentFundAccountInfo().getYJCE());
            }
            this.put("个人缴存基数", body.getProvidentFundAccountInfo().getGRJCJS());
            if (StringUtil.notEmpty(body.getProvidentFundAccountInfo().getGRZHYE())) {
                this.put("个人账户余额", body.getProvidentFundAccountInfo().getGRZHYE());
            }
            if (StringUtil.notEmpty(body.getProvidentFundAccountInfo().getLXZCJCYS())) {
                this.put("连续正常缴存月数", body.getProvidentFundAccountInfo().getLXZCJCYS());
            }
            if ("20".equals(body.getBorrowingInfo().getHYZK())) {
                if (StringUtil.notEmpty(information.getCommonBorrowerInformation().getYSR())) {
                    this.put("共同借款人月收入", information.getCommonBorrowerInformation().getYSR());
                }
                if (StringUtil.notEmpty(information.getCommonProvidentFund().getLXZCJCYS())) {
                    this.put("共同借款人连续正常缴存月数", information.getCommonProvidentFund().getLXZCJCYS());
                }
                if (StringUtil.notEmpty(information.getCommonProvidentFund().getYJCE())) {
                    this.put("共同借款人月缴存额", information.getCommonProvidentFund().getYJCE());
                }
                if (StringUtil.notEmpty(information.getCommonProvidentFund().getGRJCJS())) {
                    this.put("共同借款人个人缴存基数", information.getCommonProvidentFund().getGRJCJS());
                }
                if (StringUtil.notEmpty(information.getCommonProvidentFund().getGRZHYE())) {
                    this.put("共同借款人个人账户余额", information.getCommonProvidentFund().getGRZHYE());
                }
            }
        }});


        try {
            if (body.getBorrowingInfo().getSJHM() == null || body.getBorrowingInfo().getSJHM().length() != 11)
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "手机号码");
            if (body.getBorrowingInfo().getYSR() == null || Double.parseDouble(body.getBorrowingInfo().getYSR()) < 500)
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "月收入必须大于等于500");
            if (body.getBorrowingInfo().getJKRZJLX() == null || !body.getBorrowingInfo().getJKRZJLX().equals("01"))
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "借款人证件类型");
            if (body.getUnitinfo().getDWDZ().trim().length() < 10) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "借款人单位地址格式长应大于10");
            }
        } catch (Exception e) {
            throw new ErrorException(e);
        }

        CBankBankInfo bankBankInfo = DAOBuilder.instance(this.icBankBankInfoDAO).searchFilter(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(body.getOtherinfo().getZHKHYHMC()))
                this.put("bank_name", body.getOtherinfo().getZHKHYHMC());
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (bankBankInfo == null) throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "没有此银行名称代码");


        List<CLoanHousingBusinessProcess> loanHousingBusinessProcess = cloanHousingBusinessProcess.list(new HashMap<String, Object>() {{
            this.put("ywlsh", YWLSH);
            this.put("cznr", LoanBusinessType.合同变更.getCode());
        }}, null, null, null, null, null, null);


        if (!tokenContext.getUserInfo().getCZY().equals(loanHousingBusinessProcess.get(0).getCzy()) ||
                !tokenContext.getUserInfo().getYWWD().equals(loanHousingBusinessProcess.get(0).getYwwd().getId()))
            throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务(" + YWLSH + ")不是由您受理的，不能修改");

        if (loanHousingBusinessProcess.size() == 0)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务记录不存在");

        if (!tokenContext.getUserInfo().getCZY().equals(loanHousingBusinessProcess.get(0).getCzy()) ||
                !tokenContext.getUserInfo().getYWWD().equals(loanHousingBusinessProcess.get(0).getYwwd().getId()))
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "操作员或业务网点不匹配");

        CLoanHousingBusinessProcess housingBusinessProcess = loanHousingBusinessProcess.get(0);

        CLoanHousingPersonInformationVice loanHousingPersonInformationVice = housingBusinessProcess.getLoanHousingPersonInformationVice();
        loanHousingPersonInformationVice.setJkrxm(body.getBorrowingInfo().getJKRXM()); //姓名
        loanHousingPersonInformationVice.setJkrzjlx(body.getBorrowingInfo().getJKRZJLX());//身份证
        if (IdcardValidator.isValidatedAllIdcard(body.getBorrowingInfo().getJKRZJHM())) {
            loanHousingPersonInformationVice.setJkrzjhm(body.getBorrowingInfo().getJKRZJHM());
        } else {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "身份证格式有问题");
        }
        loanHousingPersonInformationVice.setXingBie(body.getBorrowingInfo().getXingBie().charAt(0));//性别
        loanHousingPersonInformationVice.setXueLi(body.getBorrowingInfo().getXueLi());//学历
        loanHousingPersonInformationVice.setHyzk(body.getBorrowingInfo().getHYZK());//婚姻状况
        loanHousingPersonInformationVice.setZhiCheng(body.getBorrowingInfo().getZhiCheng());//职称
        loanHousingPersonInformationVice.setZhiWu(body.getBorrowingInfo().getZhiWu());//职务
        loanHousingPersonInformationVice.setDwdh(body.getBorrowingInfo().getGDDHHM());//固定电话
        loanHousingPersonInformationVice.setSjhm(body.getBorrowingInfo().getSJHM());//手机号码
        loanHousingPersonInformationVice.setJtzz(body.getBorrowingInfo().getJTZZ());//家庭地址
        loanHousingPersonInformationVice.setHkszd(body.getBorrowingInfo().getHKSZD());//户口所在地
        loanHousingPersonInformationVice.setJcd(body.getJCD());//缴存地
        loanHousingPersonInformationVice.setGddhhm(body.getBorrowingInfo().getGDDHHM());//固定电话号码
        loanHousingPersonInformationVice.setNianLing(body.getBorrowingInfo().getNinaLing());//年龄
        loanHousingPersonInformationVice.setJkzk(body.getBorrowingInfo().getJKZK());//健康状况
        loanHousingPersonInformationVice.setYgxz(body.getBorrowingInfo().getYGXZ());//用工性质
        loanHousingPersonInformationVice.setZyjjly(body.getBorrowingInfo().getZYJJLY());//主要经济来源
        loanHousingPersonInformationVice.setYsr(StringUtil.notEmpty(body.getBorrowingInfo().getYSR()) ? new BigDecimal(body.getBorrowingInfo().getYSR()) : BigDecimal.ZERO);//月收入
        loanHousingPersonInformationVice.setJtysr(StringUtil.notEmpty(body.getBorrowingInfo().getJTYSR()) ? new BigDecimal(body.getBorrowingInfo().getJTYSR()) : BigDecimal.ZERO);//家庭月收入
        loanHousingPersonInformationVice.setBlzl(body.getBLZL());//办理资料
        loanHousingPersonInformationVice.setGrzhzt(body.getProvidentFundAccountInfo().getGRZHZT());//个人账户状态
        try {
            loanHousingPersonInformationVice.setJzny(simM.format(simview.parse(body.getProvidentFundAccountInfo().getJZNY())));//交织年月
        } catch (Exception e) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "借款人缴至年月");
        }
        try {
            loanHousingPersonInformationVice.setCsny(simM.format(simview.parse(body.getBorrowingInfo().getCSNY())));
        } catch (Exception e) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "借款人出生年月");
        }
        loanHousingPersonInformationVice.setYjce(StringUtil.notEmpty(body.getProvidentFundAccountInfo().getYJCE()) ? new BigDecimal(body.getProvidentFundAccountInfo().getYJCE()) : BigDecimal.ZERO);//月缴存额
        loanHousingPersonInformationVice.setGrjcjs(StringUtil.notEmpty(body.getProvidentFundAccountInfo().getGRJCJS()) ? new BigDecimal(body.getProvidentFundAccountInfo().getGRJCJS()) : BigDecimal.ZERO);//个人缴存基数
        loanHousingPersonInformationVice.setGrzhye(StringUtil.notEmpty(body.getProvidentFundAccountInfo().getGRZHYE()) ? new BigDecimal(body.getProvidentFundAccountInfo().getGRZHYE()) : BigDecimal.ZERO);//个人账户余额
        loanHousingPersonInformationVice.setLxzcjcys(StringUtil.notEmpty(body.getProvidentFundAccountInfo().getLXZCJCYS()) ? new BigDecimal(body.getProvidentFundAccountInfo().getLXZCJCYS()) : BigDecimal.ZERO);//连续缴存约束
        loanHousingPersonInformationVice.setDwmc(body.getUnitinfo().getDWMC());//单位名称
        loanHousingPersonInformationVice.setDwzh(body.getUnitinfo().getDWZH());//单位账号
        loanHousingPersonInformationVice.setDwdh(body.getUnitinfo().getDWDH());//单位电话
        loanHousingPersonInformationVice.setDwxz(body.getUnitinfo().getDWLB());//单位性质
        loanHousingPersonInformationVice.setDwdz(body.getUnitinfo().getDWDZ());//单位地址
        loanHousingPersonInformationVice.setHkzh(body.getOtherinfo().getHKZH());
        loanHousingPersonInformationVice.setZhkhyhmc(body.getOtherinfo().getZHKHYHMC());
        if (StringUtil.isEmpty(body.getOtherinfo().getHKZHHM())) throw new ErrorException("还款账号户名缺失");
        loanHousingPersonInformationVice.setHkzhhm(body.getOtherinfo().getHKZHHM());

        CLoanHousingCoborrowerVice coanHousingCoborrowerVice = housingBusinessProcess.getLoanHousingCoborrowerVice();
        if ("20".equals(body.getBorrowingInfo().getHYZK())) {
            if (coanHousingCoborrowerVice == null) {
                coanHousingCoborrowerVice = new CLoanHousingCoborrowerVice();
            }
            if (information.getCommonBorrowerInformation().getGTJKRXM() == null || information.getCommonBorrowerInformation().getGTJKRXM().equals(body.getBorrowingInfo().getJKRXM()))
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "共同借款人姓名");
            if (information.getCommonBorrowerInformation().getGTJKRZJHM() == null || information.getCommonBorrowerInformation().getGTJKRZJHM().equals(body.getBorrowingInfo().getJKRZJHM()))
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "共同借款人证件号码");
            if (information.getCommonBorrowerInformation().getGTJKRZJLX() == null || !information.getCommonBorrowerInformation().getGTJKRZJLX().equals("01"))
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "共同借款人证件类型");
            if (information.getCommonUnitInfo().getDWDZ() != null && !information.getCommonUnitInfo().getDWDZ().trim().equals("") && information.getCommonUnitInfo().getDWDZ().trim().length() < 10) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "共同借款人单位地址长度应大于10");
            }
            coanHousingCoborrowerVice.setCdgx(information.getCDGX());
            coanHousingCoborrowerVice.setGtjkrgjjzh(information.getGTJKRGJJZH());
            coanHousingCoborrowerVice.setJcd(information.getJCD());
            coanHousingCoborrowerVice.setGtjkrxm(information.getCommonBorrowerInformation().getGTJKRXM());
            coanHousingCoborrowerVice.setGtjkrzjlx(information.getCommonBorrowerInformation().getGTJKRZJLX());
            if (IdcardValidator.isValidatedAllIdcard(information.getCommonBorrowerInformation().getGTJKRZJHM())) {
                coanHousingCoborrowerVice.setGtjkrzjhm(information.getCommonBorrowerInformation().getGTJKRZJHM());
            } else {
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "共同借款人身份证格式有问题");
            }
            coanHousingCoborrowerVice.setGddhhm(information.getCommonBorrowerInformation().getGDDHHM());
            if (information.getCommonBorrowerInformation().getSJHM() == null || information.getCommonBorrowerInformation().getSJHM().length() != 11)
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "共同借款人手机号码");
            coanHousingCoborrowerVice.setSjhm(information.getCommonBorrowerInformation().getSJHM());
            if (information.getCommonBorrowerInformation().getYSR() == null || Double.parseDouble(information.getCommonBorrowerInformation().getYSR()) < 0)
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "共同借款人月收入必须大于等于0");
            coanHousingCoborrowerVice.setYsr(new BigDecimal(information.getCommonBorrowerInformation().getYSR()));
            coanHousingCoborrowerVice.setHkszd(information.getCommonBorrowerInformation().getHKSZD());
            coanHousingCoborrowerVice.setDwmc(information.getCommonUnitInfo().getDWMC());
            coanHousingCoborrowerVice.setDwzh(information.getCommonUnitInfo().getDWZH());
            coanHousingCoborrowerVice.setDwdh(information.getCommonUnitInfo().getDWDH());
            coanHousingCoborrowerVice.setDwxz(information.getCommonUnitInfo().getDWLB());
            coanHousingCoborrowerVice.setDwdz(information.getCommonUnitInfo().getDWDZ());
            coanHousingCoborrowerVice.setGrzhzt(information.getCommonProvidentFund().getGRZHZT());
            coanHousingCoborrowerVice.setLxzcjcys(new BigDecimal(information.getCommonProvidentFund().getLXZCJCYS()));
            try {
                if (StringUtil.notEmpty(information.getCommonProvidentFund().getJZNY())) {
                    coanHousingCoborrowerVice.setJzny(simM.format(simview.parse(information.getCommonProvidentFund().getJZNY())));
                }
            } catch (Exception e) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "共同借款人缴至年月");
            }
            coanHousingCoborrowerVice.setYjce(StringUtil.notEmpty(information.getCommonProvidentFund().getYJCE()) ? new BigDecimal(information.getCommonProvidentFund().getYJCE()) : BigDecimal.ZERO);
            coanHousingCoborrowerVice.setGrjcjs(StringUtil.notEmpty(information.getCommonProvidentFund().getGRJCJS()) ? new BigDecimal(information.getCommonProvidentFund().getGRJCJS()) : BigDecimal.ZERO);
            coanHousingCoborrowerVice.setGrzhye(StringUtil.notEmpty(information.getCommonProvidentFund().getGRZHYE()) ? new BigDecimal(information.getCommonProvidentFund().getGRZHYE()) : BigDecimal.ZERO);
            coanHousingCoborrowerVice.setBlzl(information.getBLZL());
            housingBusinessProcess.setLoanHousingCoborrowerVice(coanHousingCoborrowerVice);
            coanHousingCoborrowerVice.setGrywmx(housingBusinessProcess);
        }
        //其他信息
        CLoanFundsVice loanFundsVice = housingBusinessProcess.getLoanFundsVice();
        loanFundsVice.setWtkhyjce("1".equals(body.getOtherinfo().getWTKHYJCE()) ? true : false);
        if ("1".equals(action)) {
            iSaveAuditHistory.saveNormalBusiness(housingBusinessProcess.getYwlsh(), tokenContext, LoanBusinessType.合同变更.getName(), "修改");
        }

        StateMachineUtils.updateState(iStateMachineService, action.equals("0") ? Events.保存.getEvent() : Events.通过.getEvent(),
                new TaskEntity() {{
                    this.setOperator(tokenContext.getUserInfo().getCZY());
                    this.setStatus(housingBusinessProcess.getStep());
                    this.setTaskId(YWLSH);
                    this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
                    this.setSubtype(BusinessSubType.贷款_合同变更申请.getSubType());
                    this.setType(com.handge.housingfund.database.enums.BusinessType.Loan);
                    this.setWorkstation(tokenContext.getUserInfo().getYWWD());
                }}, new StateMachineUtils.StateChangeHandler() {
                    @Override
                    public void onStateChange(boolean succeed, String next, Exception e) {
                        if (e != null) {
                            throw new ErrorException(e);
                        }
                        if (!succeed || next == null) {
                            return;
                        }
                        if (succeed) {
                            if (StringUtil.isIntoReview(next, null))
                                housingBusinessProcess.setDdsj(new Date());
                            if (LoanBussinessStatus.办结.getName().equals(next)) doAction(tokenContext, YWLSH);
                            housingBusinessProcess.setStep(next);
                            cloanHousingBusinessProcess.update(housingBusinessProcess);
                        }
                    }
                });

        return new CommonResponses() {{
            this.setId(YWLSH);
            this.setState("success");
        }};
    }

    /**
     * （合同）借款人信息(共同借款人)查询详情
     * //
     */
    @Override
    public final GetBorrowerInformation getBorrowerInformationChange(TokenContext tokenContext, String YWLSH) {
        List<CLoanHousingBusinessProcess> loanHousingBusinessProcess = cloanHousingBusinessProcess.list(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(YWLSH)) this.put("ywlsh", YWLSH);
            this.put("cznr", LoanBusinessType.合同变更.getCode());
        }}, null, null, null, null, null, null);
        if (loanHousingBusinessProcess.size() == 0)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务记录不存在:" + YWLSH);

        CLoanHousingBusinessProcess housingBusinessProcess = loanHousingBusinessProcess.get(0);
        HashMap<String, ArrayList<String>> map = showDiff(housingBusinessProcess);

        return new GetBorrowerInformation() {{
            this.setJKRXX(new BorrowerInformation() {{
                if (housingBusinessProcess.getLoanHousingPersonInformationVice() != null) {
                    this.setJCD(housingBusinessProcess.getLoanHousingPersonInformationVice().getJcd());//put
                    this.setBLZL(housingBusinessProcess.getLoanHousingPersonInformationVice().getBlzl());//put
                    //借款人信息
                    this.setBorrowingInfo(new BorrowingInfo() {{
                        this.setJKRXM(housingBusinessProcess.getLoanHousingPersonInformationVice().getJkrxm());
                        this.setJKRZJLX(housingBusinessProcess.getLoanHousingPersonInformationVice().getJkrzjlx());
                        this.setJKRZJHM(housingBusinessProcess.getLoanHousingPersonInformationVice().getJkrzjhm());
                        try {
                            if (StringUtil.notEmpty(housingBusinessProcess.getLoanHousingPersonInformationVice().getCsny())) {
                                this.setCSNY(simview.format(simM.parse(housingBusinessProcess.getLoanHousingPersonInformationVice().getCsny())));
                            }
                        } catch (Exception e) {
                            this.setCSNY("");
                        }
                        this.setXingBie(housingBusinessProcess.getLoanHousingPersonInformationVice().getXingBie() + "");
                        this.setXueLi(housingBusinessProcess.getLoanHousingPersonInformationVice().getXueLi());
                        this.setNinaLing(housingBusinessProcess.getLoanHousingPersonInformationVice().getNianLing());//put
                        this.setJKZK(housingBusinessProcess.getLoanHousingPersonInformationVice().getJkzk());//put
                        this.setHYZK(housingBusinessProcess.getLoanHousingPersonInformationVice().getHyzk());
                        this.setZhiCheng(housingBusinessProcess.getLoanHousingPersonInformationVice().getZhiCheng());
                        this.setZhiWu(housingBusinessProcess.getLoanHousingPersonInformationVice().getZhiWu());
                        this.setYGXZ(housingBusinessProcess.getLoanHousingPersonInformationVice().getYgxz());//put
                        this.setZYJJLY(housingBusinessProcess.getLoanHousingPersonInformationVice().getZyjjly());//put
                        this.setYSR(housingBusinessProcess.getLoanHousingPersonInformationVice().getYsr() + "");//put
                        this.setJTYSR(housingBusinessProcess.getLoanHousingPersonInformationVice().getJtysr() + "");//put
                        this.setGDDHHM(housingBusinessProcess.getLoanHousingPersonInformationVice().getGddhhm());
                        this.setSJHM(housingBusinessProcess.getLoanHousingPersonInformationVice().getSjhm());
                        this.setJTZZ(housingBusinessProcess.getLoanHousingPersonInformationVice().getJtzz());
                        this.setHKSZD(housingBusinessProcess.getLoanHousingPersonInformationVice().getHkszd());
                    }});
                    //公积金账户信息
                    this.setProvidentFundAccountInfo(new ProvidentFundAccountInfo() {{
                        try {
                            if (StringUtil.notEmpty(housingBusinessProcess.getLoanHousingPersonInformationVice().getJzny())) {
                                this.setJZNY(simview.format(simM.parse(housingBusinessProcess.getLoanHousingPersonInformationVice().getJzny())));
                            }
                        } catch (Exception e) {
                            this.setJZNY("");
                        }
                        if (StringUtil.notEmpty(housingBusinessProcess.getLoanHousingPersonInformationVice().getJkrgjjzh()) && common_person.getByGrzh(housingBusinessProcess.getLoanHousingPersonInformationVice().getJkrgjjzh()) != null) {
                            this.setGRZHZT(common_person.getByGrzh(housingBusinessProcess.getLoanHousingPersonInformationVice().getJkrgjjzh()).getCollectionPersonalAccount().getGrzhzt());
                        }
                        this.setLXZCJCYS(housingBusinessProcess.getLoanHousingPersonInformationVice().getLxzcjcys() + "");
                        this.setYJCE(housingBusinessProcess.getLoanHousingPersonInformationVice().getYjce() + "");
                        this.setGRJCJS(housingBusinessProcess.getLoanHousingPersonInformationVice().getGrjcjs() + "");
                        this.setGRZHYE(housingBusinessProcess.getLoanHousingPersonInformationVice().getGrzhye() + "");
                    }});
                    //单位信息
                    this.setUnitinfo(new UnitInfos() {{
                        this.setDWMC(housingBusinessProcess.getLoanHousingPersonInformationVice().getDwmc());
                        this.setDWZH(housingBusinessProcess.getLoanHousingPersonInformationVice().getDwzh());
                        this.setDWDH(housingBusinessProcess.getLoanHousingPersonInformationVice().getDwdh());
                        this.setDWLB(housingBusinessProcess.getLoanHousingPersonInformationVice().getDwxz());
                        this.setDWDZ(housingBusinessProcess.getLoanHousingPersonInformationVice().getDwdz());
                    }});
                }

                //其他信息
                this.setOtherinfo(new OthersInfo() {{
                    try {
                        this.setHKZH(housingBusinessProcess.getLoanHousingPersonInformationVice().getHkzh());//put
                        this.setZHKHYHMC(housingBusinessProcess.getLoanHousingPersonInformationVice().getZhkhyhmc());//put
                        this.setHKZHHM(housingBusinessProcess.getLoanHousingPersonInformationVice().getHkzhhm());
                        this.setWTKHYJCE(true == housingBusinessProcess.getLoanFundsVice().getWtkhyjce() ? "1" : "0");//put
                    } catch (Exception e) {
                        if (housingBusinessProcess.getLoanFundsVice() == null) this.setWTKHYJCE(null);

                    }
                }});
            }});
            if ("20".equals(housingBusinessProcess.getLoanHousingPersonInformationVice().getHyzk())) {
                this.setGTJKRXX(new CommonBorrowerInformation() {
                                    {
                                        if (housingBusinessProcess.getLoanHousingCoborrowerVice() != null) {
                                            this.setCDGX(housingBusinessProcess.getLoanHousingCoborrowerVice().getCdgx());
                                            this.setGTJKRGJJZH(housingBusinessProcess.getLoanHousingCoborrowerVice().getGtjkrgjjzh());
                                            this.setJCD(housingBusinessProcess.getLoanHousingCoborrowerVice().getJcd());
                                            this.setBLZL(housingBusinessProcess.getLoanHousingCoborrowerVice().getBlzl());
                                            //共同借款人信息
                                            this.setCommonBorrowerInformation(new CommonBorrowerInfo() {{
                                                this.setGTJKRXM(housingBusinessProcess.getLoanHousingCoborrowerVice().getGtjkrxm());
                                                this.setGTJKRZJLX(housingBusinessProcess.getLoanHousingCoborrowerVice().getGtjkrzjlx());
                                                this.setGTJKRZJHM(housingBusinessProcess.getLoanHousingCoborrowerVice().getGtjkrzjhm());
                                                this.setGDDHHM(housingBusinessProcess.getLoanHousingCoborrowerVice().getGddhhm());
                                                this.setSJHM(housingBusinessProcess.getLoanHousingCoborrowerVice().getSjhm());
                                                this.setYSR(housingBusinessProcess.getLoanHousingCoborrowerVice().getYsr() + "");
                                                this.setHKSZD(housingBusinessProcess.getLoanHousingCoborrowerVice().getHkszd());
                                            }});
                                            //单位信息
                                            this.setCommonUnitInfo(new CommonUnitInfo() {{
                                                this.setDWMC(housingBusinessProcess.getLoanHousingCoborrowerVice().getDwmc());
                                                this.setDWDH(housingBusinessProcess.getLoanHousingCoborrowerVice().getDwdh());
                                                this.setDWZH(housingBusinessProcess.getLoanHousingCoborrowerVice().getDwzh());
                                                this.setDWLB(housingBusinessProcess.getLoanHousingCoborrowerVice().getDwxz());
                                                this.setDWDZ(housingBusinessProcess.getLoanHousingCoborrowerVice().getDwdz());
                                            }});
                                            //公积金账户信息
                                            this.setCommonProvidentFund(new CommonProvidentFund() {{
                                                try {
                                                    if (StringUtil.notEmpty(housingBusinessProcess.getLoanHousingCoborrowerVice().getJzny())) {
                                                        this.setJZNY(simview.format(simview.parse(housingBusinessProcess.getLoanHousingCoborrowerVice().getJzny())));
                                                    }
                                                } catch (Exception e) {
                                                    this.setJZNY("");
                                                }
                                                if (StringUtil.notEmpty(housingBusinessProcess.getLoanHousingCoborrowerVice().getGtjkrgjjzh()) && common_person.getByGrzh(housingBusinessProcess.getLoanHousingCoborrowerVice().getGtjkrgjjzh()) != null) {
                                                    this.setGRZHZT(common_person.getByGrzh(housingBusinessProcess.getLoanHousingCoborrowerVice().getGtjkrgjjzh()).getCollectionPersonalAccount().getGrzhzt());
                                                }
                                                this.setLXZCJCYS(housingBusinessProcess.getLoanHousingCoborrowerVice().getLxzcjcys() + "");
                                                this.setYJCE(housingBusinessProcess.getLoanHousingCoborrowerVice().getYjce() + "");
                                                this.setGRJCJS(housingBusinessProcess.getLoanHousingCoborrowerVice().getGrjcjs() + "");
                                                this.setGRZHYE(housingBusinessProcess.getLoanHousingCoborrowerVice().getGrzhye() + "");
                                            }});
                                        }
                                    }
                                }

                );

            }
            this.setDKZH(housingBusinessProcess.getDkzh());
            this.setCZY(housingBusinessProcess.getCzy());
            this.setYWWD(housingBusinessProcess.getYwwd().getMingCheng());
            this.setJKRGJGZH(housingBusinessProcess.getLoanHousingPersonInformationVice().getJkrgjjzh());//put
            this.setJKHTBH(housingBusinessProcess.getLoanHousingPersonInformationVice().getJkhtbh());
            this.setDELTAJKR(map.get("jkr"));
            this.setDELTAGTJKR(map.get("gtjkr"));

        }};
    }

    /**
     * 合同变更审核列表
     * //
     *
     * @param status 0 待处理 1 已处理
     */
    @Override
    @Deprecated
    public final PageRes<ContractAlterReviewRes> getRepaymentReview(TokenContext tokenContext, String status, String JKTHBH, String stime, String etime) {

        Date date = new Date();
        if (!StringUtil.notEmpty(stime)) stime = "2017-01-01";
        if (!StringUtil.notEmpty(etime)) etime = sim.format(date);
        PageResults<CLoanHousingBusinessProcess> housingBusinessProcess = null;
        try {
            housingBusinessProcess = cloanHousingBusinessProcess.listWithPage(new HashMap<String, Object>() {{
                if (StringUtil.notEmpty(JKTHBH)) this.put("loanContract.jkhtbh", JKTHBH);
                if (status.equals("0")) this.put("step", LoanBussinessStatus.待审核.getName());
                this.put("cznr", LoanBusinessType.合同变更.getCode());
            }}, sim.parse(stime), sim.parse(etime), null, null, null, null, 1, 10);
        } catch (Exception e) {
            throw new ErrorException(e);
        }

        if (housingBusinessProcess == null) throw new ErrorException(ReturnEnumeration.Data_MISS, "业务记录不存在");

        PageRes<ContractAlterReviewRes> pageres = new PageRes<>();
        List<CLoanHousingBusinessProcess> cLoanHousingBusinessProcess = housingBusinessProcess.getResults();
        ArrayList<ContractAlterReviewRes> list = new ArrayList<>();
        ContractAlterReviewRes contractAlterReviewRes = null;
        for (CLoanHousingBusinessProcess housingBusinessProcesslist : cLoanHousingBusinessProcess) {
            if (housingBusinessProcesslist.getStep().equals(LoanBussinessStatus.审核不通过) || housingBusinessProcesslist.getStep().equals(LoanBussinessStatus.办结)) {
                try {
                    contractAlterReviewRes = new ContractAlterReviewRes();
                    contractAlterReviewRes.setYWLSH(housingBusinessProcesslist.getYwlsh());
                    contractAlterReviewRes.setJKHTBH(housingBusinessProcesslist.getLoanHousingPersonInformationVice().getJkhtbh());
                    contractAlterReviewRes.setCZY(housingBusinessProcesslist.getCzy());
                    contractAlterReviewRes.setSLSJ(sim.format(housingBusinessProcesslist.getBlsj()));
                    contractAlterReviewRes.setYWWD(housingBusinessProcesslist.getYwwd().getMingCheng());
                    contractAlterReviewRes.setZhuangTai(housingBusinessProcesslist.getStep());
                    list.add(contractAlterReviewRes);
                } catch (Exception e) {
                    continue;
                }
            }
        }
        pageres.setResults(list);
        pageres.setCurrentPage(housingBusinessProcess.getCurrentPage());
        pageres.setNextPageNo(housingBusinessProcess.getPageNo());
        pageres.setPageSize(housingBusinessProcess.getPageSize());
        pageres.setTotalCount(housingBusinessProcess.getTotalCount());
        pageres.setPageCount(housingBusinessProcess.getPageCount());

        return pageres;
    }

    /**
     * 办结操作
     * //
     */
    @Override
    public final synchronized ResponseEntity doAction(TokenContext tokenContext, String YWLSH) {

        List<CLoanHousingBusinessProcess> housingBusinessProcessvice = cloanHousingBusinessProcess.list(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(YWLSH)) this.put("ywlsh", YWLSH);
            this.put("cznr", LoanBusinessType.合同变更.getCode());
        }}, null, null, null, null, null, null);

        if (housingBusinessProcessvice.size() == 0)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息不存在");
        iSaveAuditHistory.saveNormalBusiness(YWLSH, tokenContext, LoanBusinessType.合同变更.getName(), "办结");
        if (housingBusinessProcessvice.get(0).getLoanHousingPersonInformationVice() == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "个人信息丢失");
        if ("20".equals(housingBusinessProcessvice.get(0).getLoanHousingPersonInformationVice().getHyzk())) {
            if (housingBusinessProcessvice.get(0).getLoanHousingCoborrowerVice() == null)
                throw new ErrorException(ReturnEnumeration.Data_MISS, "业务共同借款人信息丢失");
        }

        CLoanHousingPersonInformationVice informationVice = housingBusinessProcessvice.get(0).getLoanHousingPersonInformationVice();
        CLoanHousingCoborrowerVice coanHousingCoborrowerVice = housingBusinessProcessvice.get(0).getLoanHousingCoborrowerVice();

        List<StHousingPersonalAccount> stHousingPersonalAccount = istHousingPersonalAccountDAO.list(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(housingBusinessProcessvice.get(0).getDkzh()))
                this.put("dkzh", housingBusinessProcessvice.get(0).getDkzh());
        }}, null, null, null, null, null, null);
        if (stHousingPersonalAccount.size() == 0)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "贷款账号不存在" + housingBusinessProcessvice.get(0).getDkzh());

        //原始数据信息
        List<CLoanHousingPersonInformationBasic> housingPersonInformationBasic = cloanHousingPersonInformationBasic.list(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(housingBusinessProcessvice.get(0).getDkzh()))
                this.put("dkzh", housingBusinessProcessvice.get(0).getDkzh());
        }}, null, null, null, Order.DESC, null, null);
        if (housingPersonInformationBasic.size() == 0)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "变更贷款账号不存在：" + housingBusinessProcessvice.get(0).getDkzh());
        CLoanHousingPersonInformationBasic hingPersonInformationBasic = housingPersonInformationBasic.get(0);//借款人信息最新的得到

        //基础数据判断
        if ("20".equals(housingPersonInformationBasic.get(0).getHyzk())) {
            if (hingPersonInformationBasic.getCoborrower() == null)
                throw new ErrorException(ReturnEnumeration.Data_MISS, "原合同共同借款人信息丢失");
            if (hingPersonInformationBasic.getCoborrower().getExtension() == null)
                throw new ErrorException(ReturnEnumeration.Data_MISS, "原合同共同借款人拓展信息丢失");
        }
        if (hingPersonInformationBasic.getFunds() == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "原合同资金信息丢失");
        if (hingPersonInformationBasic.getGuaranteeContract() == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "原担保合同信息丢失");
        if (hingPersonInformationBasic.getLoanContract() == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "原合同借款合同信息丢失");
        if (hingPersonInformationBasic.getPersonalAccount() == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "原合同贷款账号信息丢失");
        if ("0".equals(hingPersonInformationBasic.getDkyt())) {
            if (hingPersonInformationBasic.getPurchasing() == null)
                throw new ErrorException(ReturnEnumeration.Data_MISS, "原基础-房屋购买信息丢失");
        } else if ("2".equals(hingPersonInformationBasic.getDkyt())) {
            if (hingPersonInformationBasic.getOverhaul() == null)
                throw new ErrorException(ReturnEnumeration.Data_MISS, "原基础-房屋大修信息丢失");
        } else if ("1".equals(hingPersonInformationBasic.getDkyt())) {
            if (hingPersonInformationBasic.getBuild() == null)
                throw new ErrorException(ReturnEnumeration.Data_MISS, "原基础-房屋自建、翻修信息丢失");
        }

        //入库
        CLoanHousingPersonInformationBasic personInformationBasic = housingPersonInformationBasic.get(0);

//        List<CLoanHousingBusinessProcess> cLoanHousingBusinessProcessList = cloanHousingBusinessProcess.list(new HashMap<String, Object>() {{
//            this.put("ywlsh", personInformationBasic.getYwlsh());
//        }}, null, null, null, null, null, null);
//        if (cLoanHousingBusinessProcessList.size() == 0)
//            throw new ErrorException(ReturnEnumeration.Business_FAILED, "原合同业务信息丢失:" + personInformationBasic.getYwlsh());
//        CLoanHousingBusinessProcess loanHousingBusinessProcess = cLoanHousingBusinessProcessList.get(0);
//        //填充历史数据
////        CLoanHousingBusinessProcess processs = new CLoanHousingBusinessProcess();
////        processs.setDkzh(loanHousingBusinessProcess.getDkzh());
////        processs.setDkyt(loanHousingBusinessProcess.getDkyt());
////        processs.setDkdblx(loanHousingBusinessProcess.getDkdblx());
////        processs.setStep(loanHousingBusinessProcess.getStep());
////        processs.setCznr(loanHousingBusinessProcess.getCznr());
////        processs.setYwwd(loanHousingBusinessProcess.getYwwd());
//////        processs.setLoanContract(loanHousingBusinessProcess.getLoanContract());
//////        processs.setLoanFundsVice(loanHousingBusinessProcess.getLoanFundsVice());
//////        processs.setLoanHouseBuildVice(loanHousingBusinessProcess.getLoanHouseBuildVice());
//////        processs.setLoanHouseOverhaulVice(loanHousingBusinessProcess.getLoanHouseOverhaulVice());
//////        processs.setLoanHousePurchasingVice(loanHousingBusinessProcess.getLoanHousePurchasingVice());
//////        processs.setLoanHousingGuaranteeContractVice(loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice());
////        CLoanHousingPersonInformationVice infomationvice = new CLoanHousingPersonInformationVice();
////        infomationvice.setJkrxm(personInformationBasic.getJkrxm());
////        infomationvice.setJkrzjhm(personInformationBasic.getJkrzjhm());
////        infomationvice.setSjhm(personInformationBasic.getSjhm());
////        infomationvice.setJtzz(personInformationBasic.getJtzz());
////        processs.setLoanHousingPersonInformationVice(infomationvice);
////        infomationvice.setGrywmx(processs);
////        if ("20".equals(hingPersonInformationBasic.getHyzk()) && personInformationBasic.getCoborrower() != null) {
////            CLoanHousingCoborrowerVice cobbrowvice = new CLoanHousingCoborrowerVice();
////            cobbrowvice.setGtjkrxm(personInformationBasic.getCoborrower().getGtjkrxm());
////            cobbrowvice.setGtjkrzjhm(personInformationBasic.getCoborrower().getGtjkrzjhm());
////            cobbrowvice.setSjhm(personInformationBasic.getCoborrower().getSjhm());
////            processs.setLoanHousingCoborrowerVice(cobbrowvice);
////            cobbrowvice.setGrywmx(processs);
////        }
////        String id = cloanHousingBusinessProcess.save(processs);
////        CLoanHousingBusinessProcess businessProcess = cloanHousingBusinessProcess.get(id);
////        personInformationBasic.setYwlsh(businessProcess.getYwlsh());
//
//        CLoanHousingPersonInformationVice loanHousingPersonInformationVice = loanHousingBusinessProcess.getLoanHousingPersonInformationVice();
//        loanHousingPersonInformationVice.setJkrxm(personInformationBasic.getJkrxm());
//        loanHousingPersonInformationVice.setJkrzjhm(personInformationBasic.getJkrzjhm());
//        loanHousingPersonInformationVice.setSjhm(personInformationBasic.getSjhm());
//        loanHousingPersonInformationVice.setJtzz(personInformationBasic.getJtzz());
//
//        if ("20".equals(hingPersonInformationBasic.getHyzk()) && personInformationBasic.getCoborrower() != null) {
//            CLoanHousingCoborrowerVice loanHousingCoborrowerVice = loanHousingBusinessProcess.getLoanHousingCoborrowerVice();
//            loanHousingCoborrowerVice.setGtjkrxm(personInformationBasic.getCoborrower().getGtjkrxm());
//            loanHousingCoborrowerVice.setGtjkrzjhm(personInformationBasic.getCoborrower().getGtjkrzjhm());
//            loanHousingCoborrowerVice.setSjhm(personInformationBasic.getCoborrower().getSjhm());
//        } else {
//            CLoanHousingCoborrowerVice cobbrowvice = new CLoanHousingCoborrowerVice();
//            cobbrowvice.setGtjkrxm(personInformationBasic.getCoborrower().getGtjkrxm());
//            cobbrowvice.setGtjkrzjhm(personInformationBasic.getCoborrower().getGtjkrzjhm());
//            cobbrowvice.setSjhm(personInformationBasic.getCoborrower().getSjhm());
//            loanHousingBusinessProcess.setLoanHousingCoborrowerVice(cobbrowvice);
//            cobbrowvice.setGrywmx(loanHousingBusinessProcess);
//        }
//        cloanHousingBusinessProcess.update(loanHousingBusinessProcess);


        CLoanFundsInformationBasic cloanFundsInformationBasics = hingPersonInformationBasic.getFunds();//1  资金信息表
        try {
            cloanFundsInformationBasics.setWtkhyjce(housingBusinessProcessvice.get(0).getLoanFundsVice().getWtkhyjce());//是否委托扣划
        } catch (Exception e) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "资金信息丢失");
        }

        CBankBankInfo bankBankInfo = DAOBuilder.instance(this.icBankBankInfoDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("bank_name", informationVice.getZhkhyhmc());
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (bankBankInfo == null) throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "没有此账号开户银行名称");
        StHousingPersonalLoan sthousingPersonalLoan = hingPersonInformationBasic.getLoanContract();//3借款合同
        try {
            sthousingPersonalLoan.setHkzh(informationVice.getHkzh());//还款账号
            sthousingPersonalLoan.setZhkhyhmc(informationVice.getZhkhyhmc());//账户开户银行名称
            sthousingPersonalLoan.setJkrxm(informationVice.getJkrxm());
            sthousingPersonalLoan.setJkrzjh(informationVice.getJkrzjhm());
            sthousingPersonalLoan.setJkrzjlx(informationVice.getJkrzjlx());
            sthousingPersonalLoan.setJkrgjjzh(informationVice.getJkrgjjzh());
            sthousingPersonalLoan.setZhkhyhdm(bankBankInfo.getCode());

        } catch (Exception e) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "合同信息丢失");
        }

        //借款人信息
        personInformationBasic.setQtblzl(informationVice.getQtblzl());//其他办理资料
        personInformationBasic.setJkrxm(informationVice.getJkrxm()); //姓名
        personInformationBasic.setJkrzjlx(informationVice.getJkrzjlx());//身份证
        personInformationBasic.setJkrzjhm(informationVice.getJkrzjhm());//证件号码
        personInformationBasic.setGddhhm(informationVice.getGddhhm());
        personInformationBasic.setCsny(informationVice.getCsny());//出身年月
        personInformationBasic.setXingBie(informationVice.getXingBie());//性别
        personInformationBasic.setXueLi(informationVice.getXueLi());//学历
        personInformationBasic.setHyzk(informationVice.getHyzk());//婚姻状况
        personInformationBasic.setZhiCheng(informationVice.getZhiCheng());//职称
        personInformationBasic.setZhiWu(informationVice.getZhiWu());//职务
        personInformationBasic.setDwdh(informationVice.getGddhhm());//固定电话
        personInformationBasic.setSjhm(informationVice.getSjhm());//手机号码
        personInformationBasic.setJtzz(informationVice.getJtzz());//家庭地址
        personInformationBasic.setHkszd(informationVice.getHkszd());//户口所在地
        personInformationBasic.setJcd(informationVice.getJcd());//缴存地
        personInformationBasic.setNianLing(informationVice.getNianLing());//年龄
        personInformationBasic.setJkzk(informationVice.getJkzk());//健康状况
        personInformationBasic.setYgxz(informationVice.getYgxz());//用工性质
        personInformationBasic.setZyjjly(informationVice.getZyjjly());//主要经济来源
        personInformationBasic.setYsr(informationVice.getYsr());//月收入
        personInformationBasic.setJtysr(informationVice.getJtysr());//家庭月收入
        personInformationBasic.setBlzl(informationVice.getBlzl());//办理资料
        personInformationBasic.setJkrgjjzh(informationVice.getJkrgjjzh());//借款人公积金账号
        personInformationBasic.setJzny(informationVice.getJzny());//缴至年月
        personInformationBasic.setGrzhzt(informationVice.getGrzhzt());//个人账户状态
        personInformationBasic.setLxzcjcys(informationVice.getLxzcjcys());//连续正常教训月数
        personInformationBasic.setYjce(informationVice.getYjce());//月缴存额
        personInformationBasic.setGrjcjs(informationVice.getGrjcjs());//个人缴存基数
        personInformationBasic.setGrzhye(informationVice.getGrzhye());//个人账户余额
        personInformationBasic.setDwmc(informationVice.getDwmc());//单位名称
        personInformationBasic.setDwzh(informationVice.getDwzh());//单位账号
        personInformationBasic.setDwdh(informationVice.getDwdh());//单位电话
        personInformationBasic.setDwxz(informationVice.getDwxz());//单位性质
        personInformationBasic.setDwdz(informationVice.getDwdz());//单位地址
        personInformationBasic.setYwwd(housingBusinessProcessvice.get(0).getYwwd().getId());
        personInformationBasic.getLoanContract().getcLoanHousingPersonalLoanExtension().setHkzhhm(informationVice.getHkzhhm());

        //共同借款人拓展信息
        if ("20".equals(housingBusinessProcessvice.get(0).getLoanHousingPersonInformationVice().getHyzk())) {
            CLoanHousingCoborrowerExtension cloanHousingCoborrowerExtension = new CLoanHousingCoborrowerExtension();
            cloanHousingCoborrowerExtension.setHkszd(coanHousingCoborrowerVice.getHkszd());
            cloanHousingCoborrowerExtension.setDwmc(coanHousingCoborrowerVice.getDwmc());
            cloanHousingCoborrowerExtension.setDwzh(coanHousingCoborrowerVice.getDwzh());
            cloanHousingCoborrowerExtension.setDwdh(coanHousingCoborrowerVice.getDwdh());
            cloanHousingCoborrowerExtension.setDwxz(coanHousingCoborrowerVice.getDwxz());
            cloanHousingCoborrowerExtension.setDwdz(coanHousingCoborrowerVice.getDwdz());
            cloanHousingCoborrowerExtension.setJzny(coanHousingCoborrowerVice.getJzny());
            cloanHousingCoborrowerExtension.setGrzhzt(coanHousingCoborrowerVice.getGrzhzt());
            cloanHousingCoborrowerExtension.setLxzcjcys(coanHousingCoborrowerVice.getLxzcjcys());
            cloanHousingCoborrowerExtension.setYjce(coanHousingCoborrowerVice.getYjce());
            cloanHousingCoborrowerExtension.setGrjcjs(coanHousingCoborrowerVice.getGrjcjs());
            cloanHousingCoborrowerExtension.setGrzhye(coanHousingCoborrowerVice.getGrzhye());
            cloanHousingCoborrowerExtension.setBlzl(coanHousingCoborrowerVice.getBlzl());
            cloanHousingCoborrowerExtension.setJcd(coanHousingCoborrowerVice.getJcd());
            //共同借款人信息
            StHousingCoborrower stHousingCoborrower = new StHousingCoborrower();
            stHousingCoborrower.setCdgx(coanHousingCoborrowerVice.getCdgx());
            stHousingCoborrower.setGtjkrgjjzh(coanHousingCoborrowerVice.getGtjkrgjjzh());
            stHousingCoborrower.setGtjkrxm(coanHousingCoborrowerVice.getGtjkrxm());
            stHousingCoborrower.setGtjkrzjlx(coanHousingCoborrowerVice.getGtjkrzjlx());
            stHousingCoborrower.setGtjkrzjhm(coanHousingCoborrowerVice.getGtjkrzjhm());
            stHousingCoborrower.setGddhhm(coanHousingCoborrowerVice.getGddhhm());
            stHousingCoborrower.setSjhm(coanHousingCoborrowerVice.getSjhm());
            stHousingCoborrower.setYsr(coanHousingCoborrowerVice.getYsr());
            stHousingCoborrower.setcExtension(cloanHousingCoborrowerExtension);
            hingPersonInformationBasic.setCoborrower(stHousingCoborrower);
        } else {
            hingPersonInformationBasic.setCoborrower(null);
        }
        cloanHousingPersonInformationBasic.update(personInformationBasic);
        //短信
        try {
            Calendar c = Calendar.getInstance();
            ismsCommon.sendSingleSMSWithTemp(personInformationBasic.getSjhm(), SMSTemp.贷款信息变更.getCode(), new ArrayList<String>() {{
                this.add(personInformationBasic.getJkrxm());
                this.add(c.get(Calendar.MONTH) + 1 + "");
                this.add(c.get(Calendar.DATE) + "");
            }});
        } catch (Exception e) {
        }

        return null;
    }

    @Override
    public final CommonResponses headLoanContract(TokenContext tokenContext, String ywlsh) {
        //本次申请业务新的
        List<CLoanHousingBusinessProcess> businessProcessLists = cloanHousingBusinessProcess.list(new HashMap<String, Object>() {{
            this.put("ywlsh", ywlsh);
        }}, null, null, null, null, null, null);
        if (businessProcessLists.get(0) == null) throw new ErrorException(ReturnEnumeration.Data_MISS, "业务不存在");
        if (businessProcessLists.get(0).getLoanHousingPersonInformationVice() == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "个人信息丢失");

        //新的
        List<CLoanHousingPersonInformationBasic> cLoanHousingPersonInformationBasics = cloanHousingPersonInformationBasic.list(new HashMap<String, Object>() {{
            this.put("dkzh", businessProcessLists.get(0).getDkzh());
        }}, null, null, null, null, null, null);
        if (cLoanHousingPersonInformationBasics.size() == 0)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "不存在此贷款账号:" + businessProcessLists.get(0).getDkzh());
        //旧的
        List<CLoanHousingBusinessProcess> businessProcessList = cloanHousingBusinessProcess.list(new HashMap<String, Object>() {{
            this.put("ywlsh", cLoanHousingPersonInformationBasics.get(0).getYwlsh());
        }}, null, null, null, null, null, null);
        if (businessProcessList.size() == 0) throw new ErrorException(ReturnEnumeration.Data_MISS, "业务不存在");

        //新的
        CLoanHousingPersonInformationBasic cLoanHousingPersonInformationBasic = cLoanHousingPersonInformationBasics.get(0);
        StHousingCoborrower coborrower = cLoanHousingPersonInformationBasic.getCoborrower();

        //旧的
        CLoanHousingBusinessProcess cLoanHousingBusinessProcess = businessProcessList.get(0);
        CLoanHousingCoborrowerVice loanHousingCoborrowerVice = cLoanHousingBusinessProcess.getLoanHousingCoborrowerVice();
        CLoanHousingPersonInformationVice loanHousingPersonInformationVice = cLoanHousingBusinessProcess.getLoanHousingPersonInformationVice();
        if (loanHousingPersonInformationVice == null) throw new ErrorException(ReturnEnumeration.Data_MISS, "个人信息丢失");


        //审核人，该条记录审核通过的操作员
        HeadLoanContract headLoanContract = new HeadLoanContract();
        LoanContractChangeAfter loanContractChangeAfter = new LoanContractChangeAfter();
        LoanContractChangeBefore loanContractChangeBefore = new LoanContractChangeBefore();

        BorrowerInfomation borrowerInfomationbefore = new BorrowerInfomation();
        BorrowerInfomation borrowerInfomationafter = new BorrowerInfomation();
        CommonBorrowerInfomation commonBorrowerInfomationbefore = new CommonBorrowerInfomation();
        CommonBorrowerInfomation commonBorrowerInfomationafter = new CommonBorrowerInfomation();

        borrowerInfomationafter.setXingMing(cLoanHousingPersonInformationBasic.getJkrxm());
        borrowerInfomationafter.setJTZZ(cLoanHousingPersonInformationBasic.getJtzz());
        borrowerInfomationafter.setSJHM(cLoanHousingPersonInformationBasic.getSjhm());
        borrowerInfomationafter.setZJHM(cLoanHousingPersonInformationBasic.getJkrzjhm());
        loanContractChangeAfter.setBorrowerInfomation(borrowerInfomationafter);

        borrowerInfomationbefore.setXingMing(loanHousingPersonInformationVice.getJkrxm());
        borrowerInfomationbefore.setJTZZ(loanHousingPersonInformationVice.getJtzz());
        borrowerInfomationbefore.setSJHM(loanHousingPersonInformationVice.getSjhm());
        borrowerInfomationbefore.setZJHM(loanHousingPersonInformationVice.getJkrzjhm());
        loanContractChangeBefore.setBorrowerInfomation(borrowerInfomationbefore);


        if (coborrower != null) {
            commonBorrowerInfomationafter.setXingMing(coborrower.getGtjkrxm());
            commonBorrowerInfomationafter.setZJHM(coborrower.getGtjkrzjhm());
            commonBorrowerInfomationafter.setSJHM(coborrower.getSjhm());
            commonBorrowerInfomationafter.setJTZZ(cLoanHousingPersonInformationBasic.getJtzz());
            System.out.println(commonBorrowerInfomationafter.toString());
            loanContractChangeAfter.setCommonBorrowerInfomation(commonBorrowerInfomationafter);
        }

        if (loanHousingCoborrowerVice != null) {
            commonBorrowerInfomationbefore.setXingMing(loanHousingCoborrowerVice.getGtjkrxm());
            commonBorrowerInfomationbefore.setZJHM(loanHousingCoborrowerVice.getGtjkrzjhm());
            commonBorrowerInfomationbefore.setSJHM(loanHousingCoborrowerVice.getSjhm());
            commonBorrowerInfomationbefore.setJTZZ(loanHousingPersonInformationVice.getJtzz());
            System.out.println(commonBorrowerInfomationbefore.toString());
            loanContractChangeBefore.setCommonBorrowerInfomation(commonBorrowerInfomationbefore);
        }

        LoanContractChangeQTXX loanContractChangeQTXXAfter = new LoanContractChangeQTXX();
        LoanContractChangeQTXX loanContractChangeQTXXBefore = new LoanContractChangeQTXX();

        loanContractChangeQTXXBefore.setHKZH_1(cLoanHousingBusinessProcess.getLoanHousingPersonInformationVice().getHkzh());
        loanContractChangeQTXXBefore.setKHYH_1(cLoanHousingBusinessProcess.getLoanHousingPersonInformationVice().getZhkhyhmc());
        loanContractChangeQTXXBefore.setZHHM_1(cLoanHousingBusinessProcess.getLoanHousingPersonInformationVice().getHkzhhm());
        loanContractChangeQTXXBefore.setYJCE_1(cLoanHousingBusinessProcess.getLoanFundsVice().getWtkhyjce() == true ? "是" : "否");
        loanContractChangeBefore.setLoanContractChangeQTXX(loanContractChangeQTXXBefore);
        System.out.println(loanContractChangeQTXXBefore.toString());

        loanContractChangeQTXXAfter.setHKZH_1(cLoanHousingPersonInformationBasic.getLoanContract().getHkzh());
        loanContractChangeQTXXAfter.setKHYH_1(cLoanHousingPersonInformationBasic.getLoanContract().getZhkhyhmc());
        loanContractChangeQTXXAfter.setZHHM_1(cLoanHousingPersonInformationBasic.getLoanContract().getcLoanHousingPersonalLoanExtension().getHkzhhm());
        loanContractChangeQTXXAfter.setYJCE_1(cLoanHousingPersonInformationBasic.getFunds().getWtkhyjce() == true ? "是" : "否");
        loanContractChangeAfter.setLoanContractChangeQTXX(loanContractChangeQTXXAfter);
        System.out.println(loanContractChangeQTXXAfter.toString());


        headLoanContract.setLoanContractChangeAfter(loanContractChangeAfter);
        headLoanContract.setLoanContractChangeBefore(loanContractChangeBefore);
        headLoanContract.setYWLSH(cLoanHousingPersonInformationBasic.getYwlsh());
        headLoanContract.setYWWD(cAccountNetworkDAO.get(cLoanHousingPersonInformationBasic.getYwwd()).getMingCheng());
        headLoanContract.setTZRQ(simall.format(new Date()));
        headLoanContract.setJKHTBH(cLoanHousingPersonInformationBasic.getPersonalAccount().getJkhtbh());
        headLoanContract.setCZY(cLoanHousingPersonInformationBasic.getCzy());

        System.out.println(headLoanContract.toString());
//        System.out.println(borrowerInfomation.toString());
//        System.out.println(commonBorrowerInfomation.toString());

        String id = pdfService.getContractAlterPdf(headLoanContract);
        System.out.println("生成id的值：" + id);
        CommonResponses commonResponses = new CommonResponses();
        commonResponses.setId(id);
        commonResponses.setState("success");
        return commonResponses;
    }

    @Override
    public PageResNew<LoanContractListResponseRes> getLoanContractReviewListnew(TokenContext tokenContext, String JKRXM, String JKRZJHM, String status, String JKHTBH, String pageSize, String marker, String KSSJ, String JSSJ, String action, String YHDM) {
        PageResults<CLoanHousingBusinessProcess> housingBusinessProcess = null;
        try {
            housingBusinessProcess = cloanHousingBusinessProcess.listWithMarker(
                    new HashMap<String, Object>() {{
                        this.put("cznr", LoanBusinessType.合同变更.getCode());
                        if (StringUtil.notEmpty(tokenContext.getUserInfo().getYWWD())) {
                            this.put("ywwd.id", tokenContext.getUserInfo().getYWWD());
                        }
                        if (StringUtil.notEmpty(status)) {
                            if (status.equals(LoanBussinessStatus.新建.getName())) {
                                this.put("step", LoanBussinessStatus.新建.getName());
                            } else if (status.equals(LoanBussinessStatus.待审核.getName())) {
                                this.put("step", LoanBussinessStatus.待审核.getName());
                            } else if (status.equals(LoanBussinessStatus.审核不通过.getName())) {
                                this.put("step", LoanBussinessStatus.审核不通过.getName());
                            } else if (status.equals(LoanBussinessStatus.办结.getName())) {
                                this.put("step", LoanBussinessStatus.办结.getName());
                            }
                        }
                    }}, !StringUtil.notEmpty(KSSJ) ? null : simm.parse(KSSJ),
                    !StringUtil.notEmpty(JSSJ) ? null : simm.parse(JSSJ), "created_at", Order.DESC, null, null, marker, !StringUtil.notEmpty(pageSize) ? null : Integer.parseInt(pageSize), ListAction.pageType(action), new IBaseDAO.CriteriaExtension() {
                        @Override
                        public void extend(Criteria criteria) {
                            criteria.createAlias("loanHousingPersonInformationVice", "loanHousingPersonInformationVice");

                            if (StringUtil.notEmpty(JKRXM.replace(" ", ""))) {
                                criteria.add(Restrictions.like("loanHousingPersonInformationVice.jkrxm", "%" + JKRXM + "%"));
                            }
                            if (StringUtil.notEmpty(JKHTBH.replace(" ", ""))) {
                                criteria.add(Restrictions.like("loanHousingPersonInformationVice.jkhtbh", "%" + JKHTBH + "%"));
                            }
                        }
                    });
        } catch (ParseException ex) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "时间格式有误");
        } catch (Exception e) {
            throw new ErrorException(e);
        }

        PageResNew<LoanContractListResponseRes> pageres = new PageResNew<>();
        List<CLoanHousingBusinessProcess> cLoanHousingBusinessProcess = housingBusinessProcess.getResults();
        ArrayList<LoanContractListResponseRes> res = new ArrayList<>();
        LoanContractListResponseRes loanContractListResponseRes = null;
        StHousingPersonalAccount byDKZH = null;
        for (CLoanHousingBusinessProcess businessProcess : cLoanHousingBusinessProcess) {
            byDKZH = istHousingPersonalAccountDAO.getByDkzh(businessProcess.getDkzh());
            if (StringUtil.notEmpty(YHDM) && byDKZH != null && byDKZH.getStHousingPersonalLoan() != null) {
                if (StringUtil.isEmpty(byDKZH.getStHousingPersonalLoan().getSwtyhdm()) || !byDKZH.getStHousingPersonalLoan().getSwtyhdm().equals(YHDM))
                    continue;
            }
            loanContractListResponseRes = new LoanContractListResponseRes();
            loanContractListResponseRes.setId(businessProcess.getId());
            loanContractListResponseRes.setYWLSH(businessProcess.getYwlsh());
            loanContractListResponseRes.setZhuangTai(businessProcess.getStep());
            loanContractListResponseRes.setCZY(businessProcess.getCzy());
            loanContractListResponseRes.setYWWD(businessProcess.getYwwd().getMingCheng());
            loanContractListResponseRes.setSLSJ(simm.format(businessProcess.getCreated_at()));
            try {
                loanContractListResponseRes.setJKHTBH(businessProcess.getLoanHousingPersonInformationVice().getJkhtbh());
            } catch (Exception e) {
                loanContractListResponseRes.setJKHTBH("");
            }
            res.add(loanContractListResponseRes);
        }
        pageres.setResults(action, res);
        return pageres;
    }

    /**
     * @param tokenContext
     * @param grzh         业务流水号
     * @return
     */
    public CommonResponses getLoanEntrustDeductPdf(TokenContext tokenContext, String grzh) {
        EntrustDeductInfos entrustDeductInfos = new EntrustDeductInfos();
        CLoanHousingBusinessProcess obj = cloanHousingBusinessProcess.getByYWLSH(grzh);
        entrustDeductInfos.setWTRXM(obj.getLoanHousingPersonInformationVice().getJkrxm());
        entrustDeductInfos.setGRZFGJJZH(obj.getLoanHousingPersonInformationVice().getJkrgjjzh());
        entrustDeductInfos.setSFZH(obj.getLoanHousingPersonInformationVice().getJkrzjhm());
        entrustDeductInfos.setJKHTBH(obj.getLoanHousingPersonInformationVice().getJkhtbh());
        entrustDeductInfos.setDKZH(obj.getDkzh());
        entrustDeductInfos.setSWTYWWD(obj.getYwwd().getMingCheng());
        if (obj.getLoanHousingCoborrowerVice() != null) {
            entrustDeductInfos.setGTJKRXM_1(obj.getLoanHousingCoborrowerVice().getGtjkrxm());
            entrustDeductInfos.setGTJKRGJJZH_1(obj.getLoanHousingCoborrowerVice().getGtjkrgjjzh());
            entrustDeductInfos.setGTJKRSFZH_1(obj.getLoanHousingCoborrowerVice().getGtjkrzjhm());
        }
        String id = pdfService.getEntrustDeductPdf(entrustDeductInfos);
        System.out.println("生成id的值：" + id);
        CommonResponses commonResponses = new CommonResponses();
        commonResponses.setId(id);
        commonResponses.setState("success");
        return commonResponses;
    }
}

