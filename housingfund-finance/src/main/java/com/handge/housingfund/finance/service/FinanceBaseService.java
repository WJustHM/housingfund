package com.handge.housingfund.finance.service;

import com.google.gson.Gson;
import com.handge.housingfund.common.service.ISaveAuditHistory;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.ISettlementSpecialBankAccountManageService;
import com.handge.housingfund.common.service.account.model.CenterAccountInfo;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;
import com.handge.housingfund.common.service.bank.ibank.IBank;
import com.handge.housingfund.common.service.finance.IFinanceBaseService;
import com.handge.housingfund.common.service.finance.IVoucherManagerService;
import com.handge.housingfund.common.service.finance.model.*;
import com.handge.housingfund.common.service.finance.model.base.BusinessStatus;
import com.handge.housingfund.common.service.finance.model.base.FinanceBusinessTypeDictionary;
import com.handge.housingfund.common.service.finance.model.base.General;
import com.handge.housingfund.common.service.finance.model.base.TableRow;
import com.handge.housingfund.common.service.finance.model.enums.FinanceBusinessStatus;
import com.handge.housingfund.common.service.finance.model.enums.FinanceBusinessType;
import com.handge.housingfund.common.service.finance.model.enums.ReturnStatus;
import com.handge.housingfund.common.service.finance.model.enums.WFTLY;
import com.handge.housingfund.common.service.others.model.UFile;
import com.handge.housingfund.common.service.review.model.AuditInfo;
import com.handge.housingfund.common.service.review.model.MultiReviewConfig;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.BusinessType;
import com.handge.housingfund.database.enums.ListAction;
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.database.utils.DAOBuilder;
import com.handge.housingfund.finance.utils.StateMachineUtils;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import com.handge.housingfund.statemachine.repository.StateConstants;
import com.handge.housingfund.statemachine.repository.StateMachineEvent;
import com.handge.housingfund.statemachineV2.IStateMachineService;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by xuefei_wang on 17-8-24.
 */
@Component
public class FinanceBaseService implements IFinanceBaseService {


    @Autowired
    ICFinanceDailyBusinessViceDAO financeDailyBusinessViceDAO;

    @Autowired
    IStateMachineService stateMachineService;

    @Autowired
    ICStateMachineConfigurationDAO stateMachineConfigurationDAO;

    @Autowired
    private IStCommonUnitDAO commonUnitDAO;

    @Autowired
    GenerateReportService generateReportService;

    @Autowired
    ICFinanceBusinessVoucherSetsDAO financeBusinessVoucherSetsDAO;

    @Autowired
    private IStCollectionUnitAccountDAO iStCollectionUnitAccountDAO;


    @Autowired
    ICFinanceDailyBusinessBaseDAO financeDailyBusinessBaseDAO;

    @Autowired
    ICFinanceBusinessProcessDAO financeBusinessProcessDAO;  //3

    @Autowired
    ICAuditHistoryDAO auditHistoryDAO;

    @Autowired
    IVoucherManagerService voucherManagerService;

    @Autowired
    ICFinanceBusinessVoucherSetsDAO icFinanceBusinessVoucherSetsDAO;

    @Autowired
    ICFinanceDailyBusinessSetsDAO icFinanceDailyBusinessSetsDAO;

    @Autowired
    IBank iBank;

    @Autowired
    ISettlementSpecialBankAccountManageService iSettlementSpecialBankAccountManageService;

    @Autowired
    ICAccountNetworkDAO icAccountNetworkDAO;

    @Autowired
    BaseFinanceTrader bankFianceHandler;

    @Autowired
    protected ISaveAuditHistory iSaveAuditHistory;

    @Autowired
    protected ICStateMachineConfigurationDAO icStateMachineConfigurationDAO;

    @Autowired
    private ICBankBankInfoDAO bankBankInfoDAO;

    @Autowired
    private ICFinanceTemporaryRecordDAO icFinanceTemporaryRecordDAO;

    @Autowired
    private IStFinanceRecordingVoucherDAO financeRecordingVoucherDAO;


    @Autowired
    private ICFinanceRecordUnitDAO icFinanceRecordUnitDAO;

    @Autowired
    private ICBankAccChangeNoticeDAO icBankAccChangeNoticeDAO;

    @Autowired
    IFinanceBaseService financeBaseService;


    static List flexibleTask = Arrays.asList("通用", "计提个人贷款手续费", "计提项目贷款手续费", "计提住房公积金归集手续费", "暂收通用");

    static String diedStatus = "办结";

    public static Gson gson = new Gson();

    /**
     * 创建日常财务作业，
     *
     * @param tokenContext
     * @param jobInfo      　{@link BaseFinanceModel}
     * @return ContainerKV: k是业务流水号，　V是业务创建情况
     */
    private ContainerKV<String, Object> doCoreCreate(TokenContext tokenContext, final BaseFinanceModel jobInfo) {
        String subType = "日常" + jobInfo.getYWMC();
        String dataJson = gson.toJson(jobInfo.getTASKCONTENT());
        General general = gson.fromJson(dataJson, General.class);
        Map content = gson.fromJson(dataJson, Map.class);

        HashMap<String, Object> filtert = new HashMap<String, Object>();
        filtert.put("type", BusinessType.Finance);
        filtert.put("workstation", tokenContext.getUserInfo().getYWWD());
        filtert.put("flag", true);
        filtert.put("subType", subType);
        filtert.put("source", "初始状态");
        filtert.put("event", "通过");
        List<CStateMachineConfiguration> confs = icStateMachineConfigurationDAO.list(fixHashMap(filtert), null, null, null, null, null, null);
        if (confs.size() >= 1) {
            List<String> cfRoles = Arrays.asList(confs.get(0).getRole().split(","));
            List<String> uroles = tokenContext.getRoleList();
            boolean isok = false;
            for (String s : uroles) {
                if (cfRoles.contains(s)) {
                    isok = true;
                }
            }
            if (!isok) {
                throw new ErrorException("您无权限创建该业务！");
            }
        }

        ContainerKV<String, Object> result = new ContainerKV();
        HashMap filter = new HashMap();
        filter.put("cznr", FinanceBusinessType.日常财务处理.getCode());
        filter.put("czy", tokenContext.getUserInfo().getCZY());
        filter.put("ywwd", tokenContext.getUserInfo().getYWWD());
        filter.put("cFinanceDailyBusinessVice.zjywlx", jobInfo.getYWMC());
        filter.put("cFinanceDailyBusinessVice.ywsj", gson.toJson(jobInfo.getTASKCONTENT()));
        filter.put("step", BusinessStatus.新建.toString());

        try {
            List<CFinanceBusinessProcess> rs = financeBusinessProcessDAO.list(fixHashMap(filter), null, null, "blsj", null, null, null);
            if (rs.isEmpty()) {
                CFinanceBusinessProcess cFinanceBusinessProcess = new CFinanceBusinessProcess();
                cFinanceBusinessProcess.setCznr(FinanceBusinessType.日常财务处理.getCode());
                cFinanceBusinessProcess.setBlsj(DateUtil.getCurrentDateTime());
                cFinanceBusinessProcess.setCzy(tokenContext.getUserInfo().getCZY());
                cFinanceBusinessProcess.setYwwd(tokenContext.getUserInfo().getYWWD());
                cFinanceBusinessProcess.setStep(BusinessStatus.新建.toString());
                cFinanceBusinessProcess.setZhclry(tokenContext.getUserInfo().getCZY());
                cFinanceBusinessProcess.setDdsj(DateUtil.getCurrentDateTime());
                CFinanceDailyBusinessVice financeDailyBusinessVice = new CFinanceDailyBusinessVice();
                financeDailyBusinessVice.setZjywlx(jobInfo.getYWMC());

                financeDailyBusinessVice.setYwsj(dataJson);
                financeDailyBusinessVice.setYwmcid(jobInfo.getYWMCID());
                financeDailyBusinessVice.setMbbh(jobInfo.getMBBH());
                financeDailyBusinessVice.setBlzl(gson.toJson(general.getFuJian()));
                financeDailyBusinessVice.setRccwcl(cFinanceBusinessProcess);
                cFinanceBusinessProcess.setcFinanceDailyBusinessVice(financeDailyBusinessVice);


                String id = financeBusinessProcessDAO.save(cFinanceBusinessProcess);
                result.setK(financeBusinessProcessDAO.get(id).getYwlsh());
                result.setV(ReturnStatus.CREATE_OK.getMsg());
            } else {
                CFinanceBusinessProcess cFinanceBusinessProcess = rs.get(0);
                result.setK("业务" + cFinanceBusinessProcess.getYwlsh() + ReturnStatus.ALREADY_EXISTS.getMsg());
                result.setV(false);
            }
        } catch (Exception e) {
            throw new ErrorException(e);
        }
        return result;
    }


    private CBankBankInfo getStSettlementSpecialBankAccount(List<CBankBankInfo> accounts, String YHMC) {
        if (!StringUtil.notEmpty(YHMC)) {
            return null;
        }
        for (CBankBankInfo account : accounts) {
            if (YHMC.equals(account.getBank_name())) {
                return account;
            }
        }
        return null;
    }

    @Override
    public ContainerKV<String, Object> createTask(TokenContext tokenContext, BaseFinanceModel jobInfo, boolean submit) {
        ContainerKV<String, Object> rs = doCoreCreate(tokenContext, jobInfo);
        try {
            if (submit) {
                ReturnStatus status = submitJob(tokenContext, rs.getK());
                rs.setV(status.getMsg());

            }
            return rs;
        } catch (Exception e) {
            throw new ErrorException(e);
        }

    }


    /**
     * 查询日常财务作业
     *
     * @param tokenContext
     * @param businessName 　业务名称
     * @param status       　　业务状态
     * @param operator     　　操作员
     * @param begin        　起始时间
     * @param end          　结束时间
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public ContainerKS<BaseFinanceModel> searchJobs(TokenContext tokenContext, String businessName, String status, String operator, Date begin, Date end, String HeJi, String BeiZhu, int pageNo, int pageSize) {
        ContainerKS<BaseFinanceModel> result = new ContainerKS<>();
        HashMap<String, Object> filter = new HashMap<>();
        if (status != null && !status.equals(FinanceBusinessStatus.所有.getName()) && !status.equals(FinanceBusinessStatus.待审核.getName())) {
            filter.put("step", status);
        }
        if (StringUtil.notEmpty(operator))
            filter.put("czy", operator);
        if (StringUtil.notEmpty(tokenContext.getUserInfo().getYWWD()))
            filter.put("ywwd", tokenContext.getUserInfo().getYWWD());
        try {
            PageResults pageInfo = financeBusinessProcessDAO.listWithPage(fixHashMap(filter), begin, end, "blsj", Order.DESC, null, SearchOption.FUZZY, pageNo, pageSize, new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    if (FinanceBusinessStatus.待审核.getName().equals(status)) {
                        criteria.add(Restrictions.like("step", FinanceBusinessStatus.待某人审核.getName()));
                    }
                    criteria.createAlias("cFinanceDailyBusinessVice", "cFinanceDailyBusinessVice");
                    if (StringUtil.notEmpty(BeiZhu)) {
                        criteria.add(Restrictions.or(Restrictions.like("cFinanceDailyBusinessVice.ywsj", "%,\"BZ\":\"" + BeiZhu + "%\",%"),
                                Restrictions.like("cFinanceDailyBusinessVice.ywsj", "%,\"ZY\":\"" + BeiZhu + "%\",%")));
                    }
                    if (StringUtil.notEmpty(businessName)) {
                        criteria.add(Restrictions.eq("cFinanceDailyBusinessVice.zjywlx", businessName));
                    }
                    if (StringUtil.notEmpty(HeJi)) {
                        criteria.add(Restrictions.or(Restrictions.like("cFinanceDailyBusinessVice.ywsj", "%,\"FSE\":\"" + HeJi + "%\",%"),
                                Restrictions.like("cFinanceDailyBusinessVice.ywsj", "%,\"FSE\":" + HeJi + "%,%")));
                    }
                }
            });
            List<CFinanceBusinessProcess> rs = pageInfo.getResults();
            List<CAccountNetwork> networks = DAOBuilder.instance(icAccountNetworkDAO).getList(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            for (CFinanceBusinessProcess cFinanceBusinessProcess : rs) {
                CFinanceDailyBusinessVice financeDailyBusinessVice = cFinanceBusinessProcess.getcFinanceDailyBusinessVice();
                BaseFinanceModel baseFinanceModel = new BaseFinanceModel();
                baseFinanceModel.setSLSJ(DateUtil.date2Str(cFinanceBusinessProcess.getBlsj()));
                if (financeDailyBusinessVice != null) {
                    baseFinanceModel.setYWMC(financeDailyBusinessVice.getZjywlx() == null ? "none" : financeDailyBusinessVice.getZjywlx());
                    baseFinanceModel.setYWMCID(financeDailyBusinessVice.getYwmcid());
                    baseFinanceModel.setMBBH(financeDailyBusinessVice.getMbbh());
                    String json = financeDailyBusinessVice.getYwsj();
                    Map content = gson.fromJson(json, Map.class);
                    Set<String> keys = content.keySet();
                    if (keys.contains("ZY")) {
                        baseFinanceModel.setBeiZhu(content.get("ZY").toString());
                    }
                    if (keys.contains("BZ")) {
                        baseFinanceModel.setBeiZhu(content.get("BZ").toString());
                    }
                    if (keys.contains("FSE")) {
                        baseFinanceModel.setHeJi(content.get("FSE").toString());
                    }
                }
                baseFinanceModel.setCZY(cFinanceBusinessProcess.getCzy());
                baseFinanceModel.setSTATUS(cFinanceBusinessProcess.getStep());
                baseFinanceModel.setYWLSH(cFinanceBusinessProcess.getYwlsh());
                CAccountNetwork workstation = searchCAccountNetwork(networks, cFinanceBusinessProcess.getYwwd());
                baseFinanceModel.setYWWD(workstation == null ? "" : workstation.getMingCheng());
                baseFinanceModel.setJZPZH(cFinanceBusinessProcess.getJzpzh());
                baseFinanceModel.setSBYY(cFinanceBusinessProcess.getSbyy());
                result.addK(wrapEnable(baseFinanceModel));
            }
            result.setCurrentPage(pageInfo.getCurrentPage());
            result.setNextPage(pageInfo.getPageNo());
            result.setPageSize(pageInfo.getPageSize());
            result.setTotalCount(pageInfo.getTotalCount());
        } catch (Exception e) {
            throw new ErrorException(e);
        }
        return result;
    }

    /**
     * 查询日常财务作业
     *
     * @param tokenContext
     * @param businessName 　业务名称
     * @param status       　　业务状态
     * @param operator     　　操作员
     * @param begin        　起始时间
     * @param end          　结束时间
     * @return
     */
    @Override
    public ContainerKS<BaseFinanceModel> searchJobs(TokenContext tokenContext, String businessName, String status, String operator, Date begin, Date end, String HeJi, String marker, String action, int pageSize) {
        ContainerKS<BaseFinanceModel> result = new ContainerKS<>();
        HashMap<String, Object> filter = new HashMap<>();
        if (StringUtil.notEmpty(businessName)) filter.put("cFinanceDailyBusinessVice.zjywlx", businessName);
        if (status != null && !status.equals(FinanceBusinessStatus.所有.getName()) && !status.equals(FinanceBusinessStatus.待审核.getName())) {
            filter.put("step", status);
        }
        if (StringUtil.notEmpty(operator))
            filter.put("czy", operator);
        if (StringUtil.notEmpty(tokenContext.getUserInfo().getYWWD()))
            filter.put("ywwd", tokenContext.getUserInfo().getYWWD());

        try {
            PageResults pageInfo = financeBusinessProcessDAO.listWithMarker(fixHashMap(filter), begin, end, "blsj", Order.DESC, null, SearchOption.FUZZY, marker, pageSize, ListAction.pageType(action), new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    if (FinanceBusinessStatus.待审核.getName().equals(status)) {
                        criteria.add(Restrictions.like("step", FinanceBusinessStatus.待某人审核.getName()));
                    }
                }
            });
            List<CFinanceBusinessProcess> rs = pageInfo.getResults();
            List<CAccountNetwork> networks = DAOBuilder.instance(icAccountNetworkDAO).getList(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            for (CFinanceBusinessProcess cFinanceBusinessProcess : rs) {
                CFinanceDailyBusinessVice financeDailyBusinessVice = cFinanceBusinessProcess.getcFinanceDailyBusinessVice();
                BaseFinanceModel baseFinanceModel = new BaseFinanceModel();
                baseFinanceModel.setSLSJ(DateUtil.date2Str(cFinanceBusinessProcess.getBlsj()));
                if (financeDailyBusinessVice != null) {
                    baseFinanceModel.setYWMC(financeDailyBusinessVice.getZjywlx() == null ? "none" : financeDailyBusinessVice.getZjywlx());
                    baseFinanceModel.setYWMCID(financeDailyBusinessVice.getYwmcid());
                    baseFinanceModel.setMBBH(financeDailyBusinessVice.getMbbh());
                    String json = financeDailyBusinessVice.getYwsj();
                    Map content = gson.fromJson(json, Map.class);
                    Set<String> keys = content.keySet();
                    if (keys.contains("ZY")) {
                        baseFinanceModel.setBeiZhu(content.get("ZY").toString());
                    }
                    if (keys.contains("BZ")) {
                        baseFinanceModel.setBeiZhu(content.get("BZ").toString());
                    }
                    if (keys.contains("FSE")) {
                        baseFinanceModel.setHeJi(content.get("FSE").toString());
                    }
                }
                baseFinanceModel.setCZY(cFinanceBusinessProcess.getCzy());
                baseFinanceModel.setSTATUS(cFinanceBusinessProcess.getStep());
                baseFinanceModel.setYWLSH(cFinanceBusinessProcess.getYwlsh());
                CAccountNetwork workstation = searchCAccountNetwork(networks, cFinanceBusinessProcess.getYwwd());
                baseFinanceModel.setYWWD(workstation == null ? "" : workstation.getMingCheng());
                baseFinanceModel.setJZPZH(cFinanceBusinessProcess.getJzpzh());
                baseFinanceModel.setSBYY(cFinanceBusinessProcess.getSbyy());
                result.addK(wrapEnable(baseFinanceModel));
            }
            result.setCurrentPage(pageInfo.getCurrentPage());
            result.setNextPage(pageInfo.getPageNo());
            result.setPageSize(pageInfo.getPageSize());
            result.setTotalCount(pageInfo.getTotalCount());
            result.setTag(pageInfo.getTag());
        } catch (Exception e) {
            throw new ErrorException(e);
        }
        return result;
    }

    private CAccountNetwork searchCAccountNetwork(List<CAccountNetwork> networks, String id) {
        if (!StringUtil.notEmpty(id)) {
            return null;
        }
        for (CAccountNetwork network : networks) {
            if (network.getId().equals(id)) {
                return network;
            }
        }
        return null;
    }


    /**
     * 　删除日常财务作业
     *
     * @param tokenContext
     * @param ywlsh        　业务流水号
     * @return BaseFinanceModel
     */
    public ReturnStatus deleteJob(TokenContext tokenContext, final String ywlsh) {
        CFinanceBusinessProcess cFinanceBusinessProcess = new CFinanceBusinessProcess();
        cFinanceBusinessProcess.setYwlsh(ywlsh);
        try {
            HashMap parameter = new HashMap();
            parameter.put("ywlsh", ywlsh);
            List<CFinanceBusinessProcess> rs = financeBusinessProcessDAO.list(parameter, null, null, null, null, null, null);
            if (rs.size() == 1) {
                CFinanceBusinessProcess cFinanceBusinessProcessTemp = rs.get(0);

                if (!tokenContext.getUserInfo().getCZY().equals(cFinanceBusinessProcessTemp.getCzy())) {
                    throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务(" + ywlsh + ")不是由您受理的，不能删除");
                }

                if (cFinanceBusinessProcessTemp.getStep().equalsIgnoreCase(BusinessStatus.新建.toString())) {
                    financeBusinessProcessDAO.delete(cFinanceBusinessProcessTemp);
                    financeDailyBusinessViceDAO.delete(cFinanceBusinessProcessTemp.getcFinanceDailyBusinessVice());
                    return ReturnStatus.DELETE_OK;
                } else {
                    return ReturnStatus.FORBID;
                }
            } else if (rs.size() < 1) {
                return ReturnStatus.ERRO_NOTFOUND;
            } else {
                return ReturnStatus.ERRO;
            }
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }


    /**
     * 批量删除日产财务作业
     *
     * @param tokenContext
     * @param idsContainer 　{@link ContainerKS}
     * @return
     */
    @Override
    public ContainerKS<ContainerKSV<String, String>> deleteJobs(TokenContext tokenContext, final ContainerKS<String> idsContainer) {
        try {


            ContainerKS<ContainerKSV<String, String>> result = new ContainerKS<>();
            List<String> ids = idsContainer.getList();
            HashMap<String, List<String>> response = new HashMap();
            for (String id : ids) {
                ReturnStatus status = deleteJob(tokenContext, id);
                List<String> idArrays = response.getOrDefault(status, new ArrayList<String>());
                if (idArrays.add(id)) {
                    response.put(status.getMsg(), idArrays);
                } else {
                    throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "删除业务(" + id + ")失败");
                }
            }
            Set<Map.Entry<String, List<String>>> entrys = response.entrySet();
            for (Map.Entry<String, List<String>> entry : entrys) {
                ContainerKSV<String, String> container = new ContainerKSV<>();
                container.setList(entry.getValue());
                container.setV(entry.getKey());
                result.addK(container);
            }
            return result;
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }


    /**
     * 提交一个日常财务作业
     *
     * @param tokenContext
     * @param ywlsh
     * @return
     */
    public ReturnStatus submitJob(TokenContext tokenContext, final String ywlsh) {
        CFinanceBusinessProcess cFinanceBusinessProcess = new CFinanceBusinessProcess();
        cFinanceBusinessProcess.setYwlsh(ywlsh);
        HashMap parameter = new HashMap();
        parameter.put("ywlsh", ywlsh);
        List<CFinanceBusinessProcess> rs = financeBusinessProcessDAO.list(parameter, null, null, null, null, null, null);
        if (rs.size() == 1) {
            CFinanceBusinessProcess cFinanceBusinessProcessTemp = rs.get(0);

            if (!tokenContext.getUserInfo().getCZY().equals(cFinanceBusinessProcessTemp.getCzy())) {
                throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务(" + ywlsh + ")不是由您受理的，不能提交");
            }
            CFinanceDailyBusinessVice vice = cFinanceBusinessProcessTemp.getcFinanceDailyBusinessVice();
            Map content = gson.fromJson(vice.getYwsj(), Map.class);
            General general = gson.fromJson(vice.getYwsj(), General.class);
            checkParamters(vice.getZjywlx(), content, general);

            if (cFinanceBusinessProcessTemp.getStep().equalsIgnoreCase(BusinessStatus.新建.toString()) || cFinanceBusinessProcessTemp.getStep().equalsIgnoreCase(BusinessStatus.审核不通过.toString())) {
                AuditInfo message = new AuditInfo();
                message.setEvent(StateMachineEvent.SUCCESS);
                message.setNote("操作员提交业务到审核");
                return auditJob(tokenContext, cFinanceBusinessProcessTemp, message);
            } else if (isEnable(cFinanceBusinessProcessTemp.getcFinanceDailyBusinessVice().getZjywlx(), cFinanceBusinessProcessTemp.getYwlsh())) {

                updateVoucher(ywlsh, content, general, vice);
                return ReturnStatus.UPDATE_OK;

            } else {
                return ReturnStatus.FORBID;
            }
        } else if (rs.size() < 1) {
            return ReturnStatus.ERRO_NOTFOUND;
        } else {
            return ReturnStatus.ERRO;
        }
    }


    public void updateVoucher(String ywlsh, Map content, General general, CFinanceDailyBusinessVice vice) {
        //region 生成财务记账凭证
        List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
        List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据

        List<UFile> uFiles = UploadImagesUtil.getFileByJson(content.get("FuJian").toString());
        int djsl = uFiles != null ? uFiles.size() : 0;

        String YHZHH = null;
        if (content.containsKey("YHZH")) {
            YHZHH = content.get("YHZH").toString();
        }
        String YHDM = null;
        if (content.containsKey("YHDM")) {
            YHDM = content.get("YHDM").toString();
        }

        String zy = "";
        if (content.containsKey("ZY")) {
            zy = (String) content.get("ZY");
        } else if (content.containsKey("BZ")) {
            zy = (String) content.get("BZ");
        }

        StCommonUnit zcdwxx = null;
        StCommonUnit zrdwxx = null;
        StCommonUnit dwxx = null;


        for (TableRow tableRow : general.getTableRows()) {
            boolean isjf;
            BigDecimal jf = new BigDecimal(tableRow.getDebitAmount());//借方金额
            BigDecimal df = new BigDecimal(tableRow.getCreditAmount());//贷方金额
            if (df.compareTo(BigDecimal.ZERO) == 0 && jf.compareTo(BigDecimal.ZERO) == 0) {
                isjf = tableRow.isJFKM();
            } else {
                isjf = df.compareTo(BigDecimal.ZERO) == 0;
            }
            if (!isjf) {
                VoucherAmount amount = new VoucherAmount();
                String credit = tableRow.getCreditAmount();
                if (NumberUtils.isNumber(credit)) {
                    amount.setJinE(new BigDecimal(credit).setScale(2, BigDecimal.ROUND_HALF_UP));
                } else {
                    amount.setJinE(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
                }
                amount.setZhaiYao(StringUtil.notEmpty(zy) ? zy : tableRow.getRowName());
                amount.setRemark(tableRow.getRowName());
                if (vice.getZjywlx().equals("暂收分摊") && dwxx != null) {
                    amount.setZhaiYao(dwxx.getDwmc() + " 暂收分摊");
                }
                if (vice.getZjywlx().equals("未分摊转移") && zcdwxx != null) {
                    amount.setZhaiYao(zcdwxx.getDwmc() + " 转出");
                }
                DFSJ.add(amount);
            } else {
                VoucherAmount amount = new VoucherAmount();
                String debit = tableRow.getDebitAmount();
                if (NumberUtils.isNumber(debit)) {
                    amount.setJinE(new BigDecimal(debit).setScale(2, BigDecimal.ROUND_HALF_UP));
                } else {
                    amount.setJinE(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
                }
                amount.setZhaiYao(StringUtil.notEmpty(zy) ? zy : tableRow.getRowName());
                amount.setRemark(tableRow.getRowName());
                if (vice.getZjywlx().equals("暂收分摊") && dwxx != null) {
                    amount.setZhaiYao(dwxx.getDwmc() + " 暂收分摊");
                }
                if (vice.getZjywlx().equals("未分摊转移") && zrdwxx != null) {
                    amount.setZhaiYao(zrdwxx.getDwmc() + " 转入");
                }
                JFSJ.add(amount);
            }
        }

        voucherManagerService.reVoucher(ywlsh, JFSJ, DFSJ, String.valueOf(djsl), YHZHH, YHDM);
    }

    /**
     * 提交一批日常财务作业
     *
     * @param tokenContext
     * @param idsContainer
     * @return
     */
    @Override
    public ContainerKS<ContainerKSV<String, String>> submitJobs(TokenContext tokenContext, final ContainerKS<String> idsContainer) {
        ContainerKS<ContainerKSV<String, String>> result = new ContainerKS<>();
        List<String> ids = idsContainer.getList();
        HashMap<String, List<String>> response = new HashMap();
        for (String id : ids) {
            ReturnStatus status = submitJob(tokenContext, id);
            List<String> idArrays = response.getOrDefault(status, new ArrayList<>());
            if (idArrays.add(id)) {
                response.put(status.getMsg(), idArrays);
            } else {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "提交业务(" + id + ")失败");
            }
        }
        Set<Map.Entry<String, List<String>>> entrys = response.entrySet();
        for (Map.Entry<String, List<String>> entry : entrys) {
            ContainerKSV<String, String> container = new ContainerKSV<>();
            container.setList(entry.getValue());
            container.setV(entry.getKey());
            result.addK(container);
        }
        return result;
    }

    /**
     * 更新一个日常财务财务作业信息
     *
     * @param tokenContext
     * @param ywlsh
     * @param jobInfo
     * @return
     */
    @Override
    public ContainerKV<String, String> updateJob(TokenContext tokenContext, final String ywlsh, final BaseFinanceModel jobInfo, boolean submit) {
        CFinanceBusinessProcess cFinanceBusinessProcess = new CFinanceBusinessProcess();
        cFinanceBusinessProcess.setYwlsh(ywlsh);
        ContainerKV<String, String> msg = new ContainerKV<>();
        msg.setK(ywlsh);
        try {
            HashMap parameter = new HashMap();
            parameter.put("ywlsh", ywlsh);
            List<CFinanceBusinessProcess> rs = financeBusinessProcessDAO.list(parameter, null, null, null, null, null, null);
            if (rs.size() == 1) {
                CFinanceBusinessProcess cFinanceBusinessProcessTemp = rs.get(0);

                if (!tokenContext.getUserInfo().getCZY().equals(cFinanceBusinessProcessTemp.getCzy())) {
                    throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务(" + ywlsh + ")不是由您受理的，不能修改");
                }

                if (cFinanceBusinessProcessTemp.getStep().equalsIgnoreCase(BusinessStatus.新建.toString()) ||
                        cFinanceBusinessProcessTemp.getStep().equalsIgnoreCase(BusinessStatus.审核不通过.toString()) ||
                        isEnable(cFinanceBusinessProcessTemp.getcFinanceDailyBusinessVice().getZjywlx(), cFinanceBusinessProcessTemp.getYwlsh())) {
                    CFinanceDailyBusinessVice financeDailyBusinessVice = cFinanceBusinessProcessTemp.getcFinanceDailyBusinessVice();
                    String dataJson = gson.toJson(jobInfo.getTASKCONTENT());
                    General general = gson.fromJson(dataJson, General.class);
                    financeDailyBusinessVice.setYwsj(dataJson);
                    financeDailyBusinessVice.setBlzl(gson.toJson(general.getFuJian()));
                    financeDailyBusinessVice.setRccwcl(cFinanceBusinessProcessTemp);
                    financeDailyBusinessViceDAO.update(financeDailyBusinessVice);
                    msg.setV(ReturnStatus.UPDATE_OK.getMsg());
                } else {
                    msg.setV(ReturnStatus.FORBID.getMsg());
                }
            } else if (rs.size() < 1) {
                msg.setV(ReturnStatus.ERRO_NOTFOUND.getMsg());
            } else {
                msg.setV(ReturnStatus.ERRO.getMsg());
            }

            if (submit) {
                ReturnStatus status = submitJob(tokenContext, ywlsh);
                msg.setV(status.getMsg());
            }
        } catch (Exception e) {
            throw new ErrorException(e);
        }
        return msg;
    }


    /**
     * 获取作业详情
     *
     * @param tokenContext
     * @param ywlsh
     * @return
     */
    @Override
    public BaseFinanceModel getJob(TokenContext tokenContext, String ywlsh) {

        BaseFinanceModel baseFinanceModel = new BaseFinanceModel();
        try {
            HashMap parameter = new HashMap();
            parameter.put("ywlsh", ywlsh);
            List<CFinanceBusinessProcess> rs = financeBusinessProcessDAO.list(fixHashMap(parameter), null, null, null, null, null, null);
            if (rs.size() == 1) {
                CFinanceBusinessProcess cFinanceBusinessProcessTemp = rs.get(0);
                baseFinanceModel.setSTATUS(cFinanceBusinessProcessTemp.getStep());
                baseFinanceModel.setCZY(cFinanceBusinessProcessTemp.getCzy());
                baseFinanceModel.setSLSJ(DateUtil.date2Str(cFinanceBusinessProcessTemp.getBlsj()));
                baseFinanceModel.setYWLSH(cFinanceBusinessProcessTemp.getYwlsh());
                baseFinanceModel.setYWMC(cFinanceBusinessProcessTemp.getcFinanceDailyBusinessVice().getZjywlx());
                baseFinanceModel.setYWMCID(cFinanceBusinessProcessTemp.getcFinanceDailyBusinessVice().getYwmcid());
                baseFinanceModel.setMBBH(cFinanceBusinessProcessTemp.getcFinanceDailyBusinessVice().getMbbh());
                baseFinanceModel.setTASKCONTENT(gson.fromJson(cFinanceBusinessProcessTemp.getcFinanceDailyBusinessVice().getYwsj(), Map.class));
                baseFinanceModel.setZHCLRY(cFinanceBusinessProcessTemp.getZhclry());
                CAccountNetwork workstation = icAccountNetworkDAO.get(cFinanceBusinessProcessTemp.getYwwd());
                baseFinanceModel.setYWWD(workstation.getMingCheng());
                return wrapEnable(baseFinanceModel);
            } else if (rs.size() < 1) {
                return null;
            } else {
                throw new ErrorException(ReturnEnumeration.Data_MISS, ReturnStatus.ERRO.getMsg());
            }

        } catch (Exception e) {
            throw new ErrorException(e);
        }

    }

    /**
     * 查询审核中的作业进程
     *
     * @param tokenContext
     * @param type
     * @param operator
     * @param begin
     * @param end
     * @param complete
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public ContainerKS<BaseFinanceModel> searcheAuditJobs(TokenContext tokenContext, final String type, final String operator, final Date begin, final Date end, boolean complete, int pageNo, int pageSize) {
        ContainerKS<BaseFinanceModel> result = new ContainerKS<>();
        List<String> land = new ArrayList<>();
        HashMap filter = new HashMap();
        filter.put("type", BusinessType.Finance);
        filter.put("subType", FinanceBusinessTypeDictionary.stateMachineSubtype);
        filter.put("role", tokenContext.getRoleList().get(0));
        filter.put("workstation", tokenContext.getUserInfo().getYWWD());
        filter.put("flag", true);
        try {
            PageResults pageInfo = stateMachineConfigurationDAO.listWithPage(fixHashMap(filter), null, null, null, null, null, SearchOption.FUZZY, pageNo, pageSize);
            List<CStateMachineConfiguration> sts = pageInfo.getResults();
            result.setCurrentPage(pageInfo.getCurrentPage());
            result.setNextPage(pageInfo.getPageNo());
            result.setPageSize(pageInfo.getPageSize());
            result.setTotalCount(pageInfo.getTotalCount());
            for (CStateMachineConfiguration cs : sts) {
                if (complete) {
                    land.add(cs.getTarget());
                } else {
                    land.add(cs.getSource());
                }
            }
            HashMap parameter = new HashMap();
            parameter.put("ywwd", tokenContext.getUserInfo().getYWWD());
            parameter.put("cFinanceDailyBusinessVice.zjywlx", type);
            parameter.put("zhclry", operator);
            List<CFinanceBusinessProcess> rs = financeBusinessProcessDAO.list(fixHashMap(parameter), begin, end, null, null, null, null);
            for (CFinanceBusinessProcess cFinanceBusinessProcess : rs) {
                if (land.contains(cFinanceBusinessProcess.getStep())) {
                    BaseFinanceModel baseFinanceModel = new BaseFinanceModel();
                    baseFinanceModel.setYWLSH(cFinanceBusinessProcess.getYwlsh());
                    baseFinanceModel.setSLSJ(DateUtil.date2Str(cFinanceBusinessProcess.getBlsj()));
                    baseFinanceModel.setCZY(cFinanceBusinessProcess.getCzy());
                    baseFinanceModel.setSTATUS(cFinanceBusinessProcess.getStep());
                    baseFinanceModel.setYWMC(cFinanceBusinessProcess.getcFinanceDailyBusinessVice().getZjywlx());
                    baseFinanceModel.setYWMCID(cFinanceBusinessProcess.getcFinanceDailyBusinessVice().getYwmcid());
                    baseFinanceModel.setMBBH(cFinanceBusinessProcess.getcFinanceDailyBusinessVice().getMbbh());
                    baseFinanceModel.setTASKCONTENT(gson.fromJson(cFinanceBusinessProcess.getcFinanceDailyBusinessVice().getYwsj(), Map.class));
                    baseFinanceModel.setZHCLRY(cFinanceBusinessProcess.getZhclry());
                    CAccountNetwork workstation = icAccountNetworkDAO.get(cFinanceBusinessProcess.getYwwd());
                    baseFinanceModel.setYWWD(workstation.getMingCheng());
                    result.addK(wrapEnable(baseFinanceModel));
                }
            }
            return result;
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }


    /**
     * 获取一个审核作业的详情
     *
     * @param tokenContext
     * @param ywlsh
     * @return
     */
    @Override
    public BaseFinanceModel getAuditJob(TokenContext tokenContext, final String ywlsh) {
        HashMap parameter = new HashMap();
        parameter.put("ywlsh", ywlsh);
        parameter.put("ywwd", tokenContext.getUserInfo().getYWWD());
        try {
            List<CFinanceBusinessProcess> rs = financeBusinessProcessDAO.list(parameter, null, null, null, null, null, null);
            if (rs.size() == 1) {
                CFinanceBusinessProcess cFinanceBusinessProcess = rs.get(0);
                BaseFinanceModel baseFinanceModel = new BaseFinanceModel();
                baseFinanceModel.setYWLSH(ywlsh);
                baseFinanceModel.setSTATUS(cFinanceBusinessProcess.getStep());
                baseFinanceModel.setSLSJ(DateUtil.date2Str(cFinanceBusinessProcess.getBlsj()));
                baseFinanceModel.setCZY(cFinanceBusinessProcess.getCzy());
                baseFinanceModel.setYWMC(cFinanceBusinessProcess.getcFinanceDailyBusinessVice().getZjywlx());
                baseFinanceModel.setZHCLRY(cFinanceBusinessProcess.getZhclry());
                baseFinanceModel.setMBBH(cFinanceBusinessProcess.getcFinanceDailyBusinessVice().getMbbh());
                baseFinanceModel.setYWMCID(cFinanceBusinessProcess.getcFinanceDailyBusinessVice().getYwmcid());
                baseFinanceModel.setTASKCONTENT(gson.fromJson(cFinanceBusinessProcess.getcFinanceDailyBusinessVice().getYwsj(), Map.class));
                CAccountNetwork workstation = icAccountNetworkDAO.get(cFinanceBusinessProcess.getYwwd());
                baseFinanceModel.setYWWD(workstation.getMingCheng());
                return wrapEnable(baseFinanceModel);
            } else if (rs.size() > 1) {
                throw new Exception(ywlsh + "   " + ReturnStatus.ERRO.getMsg());
            }
        } catch (Exception e) {
            throw new ErrorException(e);
        }
        return null;
    }

    /**
     * 批量审核作业
     *
     * @param tokenContext
     * @param containerAudit ids : auditinfo  {@link AuditInfo}
     * @return
     */
    @Override
    public ContainerKS<ContainerKSV<String, String>> auditJobs(TokenContext tokenContext, ContainerKSV<String, AuditInfo> containerAudit) {
        try {
            ContainerKS<ContainerKSV<String, String>> result = new ContainerKS<>();
            List<String> ids = containerAudit.getList();
            AuditInfo auditInfo = containerAudit.getV();
            HashMap<String, List<String>> response = new HashMap();
            for (String id : ids) {
                ReturnStatus status = auditJob(tokenContext, id, auditInfo);
                List<String> idArrays = response.getOrDefault(status, new ArrayList<String>());
                if (idArrays.add(id)) {
                    response.put(status.getMsg(), idArrays);
                } else {
                    throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "审核业务(" + id + ")失败");
                }
            }
            Set<Map.Entry<String, List<String>>> entrys = response.entrySet();
            for (Map.Entry<String, List<String>> entry : entrys) {
                ContainerKSV<String, String> container = new ContainerKSV<>();
                container.setList(entry.getValue());
                container.setV(entry.getKey());
                result.addK(container);
            }
            return result;
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }


    /**
     * 审核一个流程
     *
     * @param tokenContext
     * @param ywlsh
     * @param auditInfo    　{@link AuditInfo}
     * @return
     */
    public ReturnStatus auditJob(TokenContext tokenContext, final String ywlsh, final AuditInfo auditInfo) {
        HashMap parameter = new HashMap();
        parameter.put("ywlsh", ywlsh);
        List<CFinanceBusinessProcess> rs = financeBusinessProcessDAO.list(parameter, null, null, null, null, null, null);
        if (rs.size() == 1) {
            CFinanceBusinessProcess cFinanceBusinessProcess = rs.get(0);
            return auditJob(tokenContext, cFinanceBusinessProcess, auditInfo);
        } else if (rs.size() < 1) {
            return ReturnStatus.ERRO_NOTFOUND;
        } else {
            return ReturnStatus.ERRO;
        }
    }


    /**
     * @param tokenContext
     * @param cFinanceBusinessProcess
     * @param auditInfo
     * @return
     */
    private ReturnStatus auditJob(TokenContext tokenContext, final CFinanceBusinessProcess cFinanceBusinessProcess, final AuditInfo auditInfo) {
        String name = cFinanceBusinessProcess.getcFinanceDailyBusinessVice().getZjywlx();
        String subType = "日常" + name;
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(cFinanceBusinessProcess.getStep());
        taskEntity.setTaskId(cFinanceBusinessProcess.getYwlsh());
        taskEntity.setOperator(tokenContext.getUserInfo().getCZY());
        taskEntity.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
        taskEntity.setNote(auditInfo.getNote());
        taskEntity.setWorkstation(tokenContext.getUserInfo().getYWWD());
        taskEntity.setType(BusinessType.Finance);
        taskEntity.setSubtype(subType);
        taskEntity.setObject(cFinanceBusinessProcess);
        taskEntity.setTokenContext(tokenContext);

        StateMachineUtils.updateState(stateMachineService, auditInfo.getEvent(), taskEntity, new StateMachineUtils.StateChangeHandler() {
            @Override
            public void onStateChange(boolean succeed, String next, Exception e) {
                if (e != null) {
                    throw new ErrorException(e);
                }
                if (!succeed || next == null) {
                    return;
                }
                String finalState = next;
                TaskEntity task = taskEntity;
                CFinanceBusinessProcess cFinanceBusinessProcessTemp = (CFinanceBusinessProcess) task.getObject();
                cFinanceBusinessProcessTemp.setStep(finalState);
                cFinanceBusinessProcessTemp.setZhclry(task.getOperator());

                iSaveAuditHistory.saveNormalBusiness(task.getTaskId(), task.getTokenContext(), FinanceBusinessType.日常财务处理.getName(), "新建");

                financeBusinessProcessDAO.update(cFinanceBusinessProcessTemp);
                if (next.equalsIgnoreCase(StateConstants.endState.getId())) {
                    financeBaseService.financeJobFinished(cFinanceBusinessProcessTemp.getId());
                }
            }
        });
        return ReturnStatus.AUDIT_FINISHED;
    }

    /**
     * ignore null value in the map
     *
     * @param hashMap
     * @return
     */
    private HashMap fixHashMap(HashMap hashMap) {
        HashMap okMap = new HashMap();
        Set keys = hashMap.keySet();
        for (Object k : keys) {
            if (hashMap.get(k) != null && !(hashMap.get(k).toString().trim()).isEmpty()) {
                okMap.put(k, hashMap.get(k));
            }
        }
        return okMap;
    }

    @Override
    public ContainerKV revokeJobs(TokenContext tokenContext, ContainerKS<String> idsContainer) {
        ContainerKV result = new ContainerKV();
        List<String> ids = idsContainer.getList();
        for (String id : ids) {
            result.setK(id);
            HashMap parameter = new HashMap();
            parameter.put("ywlsh", id);
            try {
                List<CFinanceBusinessProcess> rs = financeBusinessProcessDAO.list(parameter, null, null, null, null, null, null);
                if (rs.size() == 1) {
                    CFinanceBusinessProcess cFinanceBusinessProcess = rs.get(0);
                    MultiReviewConfig config = NormalJsonUtils.toObj4Review(cFinanceBusinessProcess.getShybh());
                    String CaoZuo = "不通过撤回";
                    String step = checkByReivew(config, tokenContext);
                    if (FinanceBusinessStatus.新建.getName().equals(step)) {
                        String ywwd = cFinanceBusinessProcess.getYwwd();
                        String czy = cFinanceBusinessProcess.getCzy();
                        if (!tokenContext.getUserInfo().getYWWD().equals(ywwd) || !tokenContext.getUserInfo().getCZY().equals(czy)) {
                            throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务(" + id + ")不是您受理的，不能撤回");
                        }
                        CaoZuo = "撤回";
                    }
                    cFinanceBusinessProcess.setStep(step);
                    cFinanceBusinessProcess.setShybh(NormalJsonUtils.toJson4Review(config));
                    cFinanceBusinessProcess.setZhclry(tokenContext.getUserId());
                    financeBusinessProcessDAO.update(cFinanceBusinessProcess);
                    iSaveAuditHistory.saveNormalBusiness(id, tokenContext, FinanceBusinessType.getNameByCode(cFinanceBusinessProcess.getCznr()), CaoZuo);
                    result.setV(true);
                }
            } catch (Exception e) {
                result.setV(false);
            }
        }
        return result;
    }

    //基于审核配置的验证，并返回正确的step状态
    public String checkByReivew(MultiReviewConfig config, TokenContext tokenContext) {
/*        if (StringUtil.notEmpty(config.getDQSHY())) {

            throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "业务正在被审核员:" + config.getDQSHY() + "审核中");
        }*/
        String step;
        if (config == null) {
            step = FinanceBusinessStatus.新建.getName();
        } else {
            //审核级别（0：普审，1：特审）
            if ("S".equals(config.getSHJB())) {
                throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务已进入特审阶段，不能撤回");
            }
            if (StringUtil.notEmpty(config.getSCSHY())) {
                if (!tokenContext.getUserId().equals(config.getSCSHY())) {
                    throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务的上一级审核非您操作，不能撤回");
                }
                step = FinanceBusinessStatus.审核不通过.getName();
            } else {
                step = FinanceBusinessStatus.新建.getName();
            }
            config.setLSSHYBH(null);
            config.setSHJB(null);
            config.setSCSHY(null);
            config.setDQSHY(null);
            config.setDQXM(null);
        }
        return step;
    }


    @Override
    public boolean financeJobFinished(String id) {
        CFinanceBusinessProcess cFinanceBusinessProcessTemp = financeBusinessProcessDAO.get(id);
        return financeJobFinished(cFinanceBusinessProcessTemp);
    }

    @Override
    public boolean financeJobFinished(String id, TokenContext tokenContext) {
        CFinanceBusinessProcess cFinanceBusinessProcessTemp = financeBusinessProcessDAO.get(id);
        cFinanceBusinessProcessTemp.setZhclry(tokenContext.getUserInfo().getCZY());
        return financeJobFinished(cFinanceBusinessProcessTemp);
    }


    @Override
    public void savaToBase(String ywlsh, String jzpzh) {
        CFinanceBusinessProcess cFinanceBusinessProcess = DAOBuilder.instance(financeBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("ywlsh", ywlsh);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (cFinanceBusinessProcess == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "未找到业务流水号为" + ywlsh + "的活期转定期业务");
        }
        cFinanceBusinessProcess.setJzpzh(jzpzh);
        savaToBase(cFinanceBusinessProcess);

    }

    public void savaToBase(final CFinanceBusinessProcess financeBusinessProcess) {
        //处理完成　写入基础表
        CFinanceDailyBusinessBase financeDailyBusinessBase = new CFinanceDailyBusinessBase();
        financeDailyBusinessBase.setYwsj(financeBusinessProcess.getcFinanceDailyBusinessVice().getYwsj());
        financeDailyBusinessBase.setBlzl(financeBusinessProcess.getcFinanceDailyBusinessVice().getBlzl());
        financeDailyBusinessBase.setCzy(financeBusinessProcess.getCzy());
        financeDailyBusinessBase.setSlsj(financeBusinessProcess.getBlsj());
        financeDailyBusinessBase.setYwlsh(financeBusinessProcess.getYwlsh());
        financeDailyBusinessBase.setZjywlx(financeBusinessProcess.getcFinanceDailyBusinessVice().getZjywlx());
        financeDailyBusinessBaseDAO.save(financeDailyBusinessBase);
        financeBusinessProcessDAO.update(financeBusinessProcess);
    }

    public void updateStatus(final CFinanceBusinessProcess financeBusinessProcess, String stauts, String SBYY) {
        financeBusinessProcess.setStep(stauts);
        financeBusinessProcess.setSbyy(SBYY);
        financeBusinessProcessDAO.update(financeBusinessProcess);
    }

    @Override
    public void updateStatus(String ywlsh, String status, String sbyy) {
        CFinanceBusinessProcess cFinanceBusinessProcess = DAOBuilder.instance(financeBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("ywlsh", ywlsh);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (cFinanceBusinessProcess == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "未找到业务流水号为" + ywlsh + "的业务");
        }

        updateStatus(cFinanceBusinessProcess, status, sbyy);
    }


    public boolean financeJobFinished(final CFinanceBusinessProcess financeBusinessProcess) {
        try {
            if (financeBusinessProcess != null) {

                //region 生成财务记账凭证
                List<VoucherAmount> JFSJ = new ArrayList<>();//借方数据
                List<VoucherAmount> DFSJ = new ArrayList<>();//贷方数据
                CFinanceDailyBusinessVice vice = financeBusinessProcess.getcFinanceDailyBusinessVice();

                Map content = gson.fromJson(vice.getYwsj(), Map.class);

                List<UFile> uFiles = UploadImagesUtil.getFileByJson(content.get("FuJian").toString());
                int djsl = uFiles != null ? uFiles.size() : 0;
                General general = gson.fromJson(vice.getYwsj(), General.class);

                /**
                 * 需要走结算平台业务：
                 *
                 * 支付住房公积金归集手续费，2c92941e5e2e0a99015e310cb5b00038，026   f
                 * 支付个人贷款手续费，2c92941e5e2e0a99015e310cb5b00039，027 f
                 * 支付项目贷款手续费，2c92941e5e2e0a99015e310cb5b0003a，028 f
                 * 同行转账，2c92941e5e2e0a99015e310ddd7f003e
                 * 跨行转账，2c92941e5e2e0a99015e310ddd7f003f
                 * 支付补息利息支出，2c92941e5e2e0a99015e310bc2820035，032 f
                 * 支付专项应付款，2c92941f5ec31709015ec34700890005，041 f
                 * 其他支付，2c92941e5e2e0a99015e310bc2820034，031 f
                 *
                 */
                StCommonUnit dwxx = null;
                List<TableRow> tableRows = general.getTableRows();

                if (vice.getZjywlx().equals("暂收分摊")) {
                    String dwzh = content.get("DWZH").toString();
                    String FSE = content.get("FSE").toString();
                    dwxx = getStCommonUnit(dwzh);
                    if (dwxx != null) {
                        StCollectionUnitAccount account = dwxx.getCollectionUnitAccount();
                        BigDecimal zsye = account.getExtension().getZsye();
                        account.getExtension().setZsye(zsye.add(new BigDecimal(FSE)));
                        DAOBuilder.instance(iStCollectionUnitAccountDAO).entity(account).save(new DAOBuilder.ErrorHandler() {
                            @Override
                            public void error(Exception e) {
                                throw new ErrorException(e);
                            }
                        });
                        //region 记录暂收分摊到单位
                        String zsid = content.get("ZSID").toString();
                        if (zsid != null) {
                            CFinanceTemporaryRecord record = DAOBuilder.instance(icFinanceTemporaryRecordDAO).UID(zsid).getObject(new DAOBuilder.ErrorHandler() {
                                @Override
                                public void error(Exception e) {
                                    throw new ErrorException(e);
                                }
                            });
                            if (record != null) {
                                StFinanceRecordingVoucher stFinanceRecordingVoucher = DAOBuilder.instance(financeRecordingVoucherDAO).searchFilter(new HashMap<String, Object>() {{
                                    this.put("jzpzh", record.getYjzpzh());
                                    this.put("cFinanceRecordingVoucherExtension.sfzjl", true);
                                }}).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
                                    @Override
                                    public void error(Exception e) {
                                        throw new ErrorException(e);
                                    }
                                });
                                if (stFinanceRecordingVoucher != null) {
                                    List<CBankAccChangeNotice> notices = stFinanceRecordingVoucher.getcFinanceRecordingVoucherExtension().getcBankAccChangeNotices();
                                    CBankAccChangeNotice notice = notices.get(0);
                                    CFinanceRecordUnit cFinanceRecordUnit = new CFinanceRecordUnit();
                                    cFinanceRecordUnit.setFse(notice.getAmt());
                                    cFinanceRecordUnit.setJzpzh(stFinanceRecordingVoucher.getJzpzh());
                                    cFinanceRecordUnit.setRemark(notice.getRemark());
                                    cFinanceRecordUnit.setSummary(notice.getSummary());
                                    cFinanceRecordUnit.setZjly(WFTLY.暂收分摊.getName());
                                    cFinanceRecordUnit.setDwzh(account.getDwzh());
                                    cFinanceRecordUnit.setWftye(account.getExtension().getZsye());
                                    icFinanceRecordUnitDAO.save(cFinanceRecordUnit);
                                } else {
                                    System.out.println("凭证不存在");
                                }
                            } else {
                                System.out.println("该暂收不存在");
                            }

                        } else {
                            System.out.println("暂收记录不存在");
                        }
                    } else {
                        throw new ErrorException(ReturnEnumeration.Data_MISS, "单位信息不存在");
                    }
                }
                StCommonUnit zcdwxx = null;
                StCommonUnit zrdwxx = null;


                String zy = "";
                if (content.containsKey("ZY")) {
                    zy = (String) content.get("ZY");
                } else if (content.containsKey("BZ")) {
                    zy = (String) content.get("BZ");
                }

                for (TableRow tableRow : tableRows) {
                    boolean isjf;
                    BigDecimal jf = new BigDecimal(tableRow.getDebitAmount());//借方金额
                    BigDecimal df = new BigDecimal(tableRow.getCreditAmount());//贷方金额
                    if (df.compareTo(BigDecimal.ZERO) == 0 && jf.compareTo(BigDecimal.ZERO) == 0) {
                        isjf = tableRow.isJFKM();
                    } else {
                        isjf = df.compareTo(BigDecimal.ZERO) == 0;
                    }
                    if (!isjf) {
                        VoucherAmount amount = new VoucherAmount();
                        String credit = tableRow.getCreditAmount();
                        if (NumberUtils.isNumber(credit)) {
                            amount.setJinE(new BigDecimal(credit).setScale(2, BigDecimal.ROUND_HALF_UP));
                        } else {
                            amount.setJinE(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
                        }
                        amount.setZhaiYao(StringUtil.notEmpty(zy) ? zy : tableRow.getRowName());
                        amount.setRemark(tableRow.getRowName());
                        if (vice.getZjywlx().equals("暂收分摊") && dwxx != null) {
                            amount.setZhaiYao(dwxx.getDwmc() + " 暂收分摊");
                        }
                        if (vice.getZjywlx().equals("未分摊转移")) {
                            if (content.containsKey("ZCDWZH")) {
                                String zcdwzh = content.get("ZCDWZH").toString();
                                zcdwxx = getStCommonUnit(zcdwzh);
                                amount.setZhaiYao(zcdwxx.getDwmc() + " 转出");
                            }
                        }
                        DFSJ.add(amount);
                    } else {
                        VoucherAmount amount = new VoucherAmount();
                        String debit = tableRow.getDebitAmount();
                        if (NumberUtils.isNumber(debit)) {
                            amount.setJinE(new BigDecimal(debit).setScale(2, BigDecimal.ROUND_HALF_UP));
                        } else {
                            amount.setJinE(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
                        }
                        amount.setZhaiYao(StringUtil.notEmpty(zy) ? zy : tableRow.getRowName());
                        amount.setRemark(tableRow.getRowName());
                        if (vice.getZjywlx().equals("暂收分摊") && dwxx != null) {
                            amount.setZhaiYao(dwxx.getDwmc() + " 暂收分摊");
                        }
                        if (vice.getZjywlx().equals("未分摊转移")) {
                            if (content.containsKey("ZRDWZH")) {
                                String zrdwzh = content.get("ZRDWZH").toString();
                                zrdwxx = getStCommonUnit(zrdwzh);
                                amount.setZhaiYao(zrdwxx.getDwmc() + " 转入");
                            }
                        }
                        JFSJ.add(amount);
                    }
                }

                //业务设置
                CFinanceDailyBusinessSets sets = DAOBuilder.instance(icFinanceDailyBusinessSetsDAO).UID(vice.getYwmcid()).getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });
                System.out.println(" ///////////////////////////////////////////////////////////////////////////////////////////////////////");
                System.out.println(content.toString());
                System.out.println("-----------------------------------------------");
                System.out.println(general);
                System.out.println(" ///////////////////////////////////////////////////////////////////////////////////////////////////////");

                if (sets.getSfjspt()) {
                    updateStatus(financeBusinessProcess, "待入账", "");
                    CenterAccountInfo ff = iSettlementSpecialBankAccountManageService.getSpecialAccountByZHHM(content.get("FFYHZH").toString());
                    if (ff == null) {
                        throw new ErrorException(ReturnEnumeration.Data_MISS, "专户未找到");
                    }
                    CenterHeadIn centerHeadIn = new CenterHeadIn();
                    centerHeadIn.setSendSeqNo(financeBusinessProcess.getYwlsh());
                    centerHeadIn.setReceiveNode(ff.getNode());
                    centerHeadIn.setOperNo(financeBusinessProcess.getZhclry());
                    centerHeadIn.setCustNo(ff.getKHBH());
                    String ywbh = sets.getYwbh();
                    SettlementHandler.Success success = new SettlementHandler.Success() {
                        @Override
                        public void handle() {
                            System.out.println("日常财务处理：结算平台请求成功");
                        }
                    };
                    SettlementHandler.Fail fail = new SettlementHandler.Fail() {
                        @Override
                        public void handle(String sbyy) {
                            updateStatus(financeBusinessProcess, "入账失败", sbyy);
                        }
                    };
                    SettlementHandler.ManualProcess process = new SettlementHandler.ManualProcess() {

                        @Override
                        public void handle() {
                            updateStatus(financeBusinessProcess, "入账失败", "状态未知,需人工线下查询");
                        }
                    };
                    SettlementHandler.SendException exception = new SettlementHandler.SendException() {
                        @Override
                        public void handle(String sbyy) {
                            updateStatus(financeBusinessProcess, "入账失败", sbyy);
                        }
                    };
                    if (!iBank.checkYWLSH(financeBusinessProcess.getYwlsh())) {
                        throw new ErrorException(ReturnEnumeration.Business_In_Process, "请勿重复操作,请刷新页面确认");
                    }
                    switch (ywbh) {
                        case "9527": //"同行转帐"
                            bankFianceHandler.handleTHZZ(content, centerHeadIn, ff, success, fail, process, exception);
                            break;
                        case "9528": //跨行转帐
                            bankFianceHandler.handleKHZZ(content, centerHeadIn, ff, success, fail, process, exception);
                            break;
                        case "9529": //支付住房公积金归集手续费
                            bankFianceHandler.handleZFZFGJJGJSXF(content, centerHeadIn, ff, success, fail, process, exception);
                            break;
                        case "9530": //支付个人贷款手续费
                            bankFianceHandler.handleGRDKSXF(content, centerHeadIn, ff, success, fail, process, exception);
                            break;
                        case "9531": //支付项目贷款手续费
                            bankFianceHandler.handleXMDKSXF(content, centerHeadIn, ff, success, fail, process, exception);
                            break;
                        case "9532": //支付补息贷款利息
                            bankFianceHandler.handleZFBXLXZC(content, centerHeadIn, ff, success, fail, process, exception);
                            break;
                        case "9533": //支付管理费用
                            bankFianceHandler.handleZFZXYFK(content, centerHeadIn, ff, success, fail, process, exception);
                            break;
                        case "9534": //其他支付
                            bankFianceHandler.handleZFQTYFK(content, centerHeadIn, ff, success, fail, process, exception);
                            break;
                        case "9536"://支付廉租房补充资金
                            bankFianceHandler.handleZFLZBCZJ(content, centerHeadIn, ff, success, fail, process, exception);
                            break;
                        case "9535": //通用
                            bankFianceHandler.handleOther(content, centerHeadIn, ff, success, fail, process, exception);
                            break;
                    }

                } else {
                    HashMap<String, Object> filter = new HashMap<>();
                    filter.put("ywmcid", sets.getId());
                    List<CFinanceBusinessVoucherSets> cFinanceBusinessVoucherSetses = icFinanceBusinessVoucherSetsDAO.list(filter, null, null, null, null, null, null);
                    if (cFinanceBusinessVoucherSetses.size() != 1) {
                        throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "没有该资金业务类型(" + vice.getZjywlx() + ")对应的凭证模板");
                    }
                    String YHZHH = null;
                    if (content.containsKey("YHZH")) {
                        YHZHH = content.get("YHZH").toString();
                    }
                    String YHDM = null;
                    if (content.containsKey("YHDM")) {
                        YHDM = content.get("YHDM").toString();
                    }
                    CFinanceBusinessVoucherSets cFinanceBusinessVoucherSets = cFinanceBusinessVoucherSetses.get(0);
                    VoucherRes voucherRes = voucherManagerService.addVoucher("毕节市住房公积金管理中心", financeBusinessProcess.getCzy(), financeBusinessProcess.getZhclry(), "", "",
                            cFinanceBusinessVoucherSets.getMbbh(), cFinanceBusinessVoucherSets.getMbbh(), financeBusinessProcess.getYwlsh(), JFSJ,
                            DFSJ, String.valueOf(djsl), YHZHH, YHDM);
                    if (voucherRes.getJZPZH() != null) {
                        financeBusinessProcess.setJzpzh(voucherRes.getJZPZH());
                    } else {
                        financeBusinessProcess.setStep("入账失败");
                        financeBusinessProcess.setSbyy(voucherRes.getMSG());
                    }
                    //更新暂收记录
                    if ("暂收分摊".equals(vice.getZjywlx()) || "暂收通用".equals(vice.getZjywlx())) {
                        if (!content.containsKey("ZSID")) {
                            throw new ErrorException(ReturnEnumeration.Data_MISS, "暂收记录不存在");
                        }
                        String zsid = content.get("ZSID").toString();
                        if (zsid != null) {
                            String zs = voucherManagerService.updateTemporaryRecord(zsid, true, voucherRes.getJZPZH());
                            System.out.println(zs);
                        } else {
                            throw new ErrorException(ReturnEnumeration.Data_MISS, "暂收记录不存在");
                        }
                    }
                    if ("未分摊转移".equals(vice.getZjywlx())) {
                        if (!content.containsKey("ZCDWZH")) {
                            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "转出单位账号不能为空");
                        }
                        if (!content.containsKey("ZRDWZH")) {
                            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "转入单位账号不能为空");
                        }
                        String zcdwzh = content.get("ZCDWZH").toString();
                        String zrdwzh = content.get("ZRDWZH").toString();
                        if (zcdwzh.equals(zrdwzh)) {
                            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "转出单位和转入单位不能相同");
                        }
                        String fse = content.get("FSE").toString();
                        zcdwxx = getStCommonUnit(zcdwzh);
                        zrdwxx = getStCommonUnit(zrdwzh);
                        if (zcdwxx != null && zrdwxx != null) {
                            List<StCollectionUnitAccount> accounts = new ArrayList<>();
                            StCollectionUnitAccount zcaccount = zcdwxx.getCollectionUnitAccount();
                            BigDecimal zczsye = zcaccount.getExtension().getZsye();
                            zcaccount.getExtension().setZsye(zczsye.subtract(new BigDecimal(fse)));
                            accounts.add(zcaccount);

                            StCollectionUnitAccount zraccount = zrdwxx.getCollectionUnitAccount();
                            BigDecimal zrzsye = zraccount.getExtension().getZsye();
                            zraccount.getExtension().setZsye(zrzsye.add(new BigDecimal(fse)));
                            accounts.add(zraccount);

                            DAOBuilder.instance(iStCollectionUnitAccountDAO).entities(accounts).save(new DAOBuilder.ErrorHandler() {
                                @Override
                                public void error(Exception e) {
                                    throw new ErrorException(e);
                                }
                            });
                            List<CFinanceRecordUnit> unitList = new ArrayList<>();
                            CFinanceRecordUnit zccFinanceRecordUnit = new CFinanceRecordUnit();
                            zccFinanceRecordUnit.setFse(new BigDecimal(fse).abs().negate());
                            zccFinanceRecordUnit.setJzpzh(voucherRes.getJZPZH());
                            if (content.containsKey("ZY")) {
                                zccFinanceRecordUnit.setSummary((String) content.get("ZY"));
                            } else if (content.containsKey("BZ")) {
                                zccFinanceRecordUnit.setRemark((String) content.get("BZ"));
                            }
                            zccFinanceRecordUnit.setZjly(WFTLY.未分摊转移.getName());
                            zccFinanceRecordUnit.setDwzh(zcaccount.getDwzh());
                            zccFinanceRecordUnit.setWftye(zcaccount.getExtension().getZsye());
                            unitList.add(zccFinanceRecordUnit);

                            CFinanceRecordUnit zrcFinanceRecordUnit = new CFinanceRecordUnit();
                            zrcFinanceRecordUnit.setFse(new BigDecimal(fse).abs());
                            zrcFinanceRecordUnit.setJzpzh(voucherRes.getJZPZH());
                            if (content.containsKey("ZY")) {
                                zrcFinanceRecordUnit.setSummary((String) content.get("ZY"));
                            } else if (content.containsKey("BZ")) {
                                zrcFinanceRecordUnit.setRemark((String) content.get("BZ"));
                            }
                            zrcFinanceRecordUnit.setZjly(WFTLY.未分摊转移.getName());
                            zrcFinanceRecordUnit.setDwzh(zraccount.getDwzh());
                            zrcFinanceRecordUnit.setWftye(zraccount.getExtension().getZsye());
                            unitList.add(zrcFinanceRecordUnit);

                            DAOBuilder.instance(icFinanceRecordUnitDAO).entities(unitList).save(new DAOBuilder.ErrorHandler() {
                                @Override
                                public void error(Exception e) {
                                    throw new ErrorException(e);
                                }
                            });

                        } else {
                            throw new ErrorException(ReturnEnumeration.Data_MISS, "单位信息不存在");
                        }
                    }
                    if ("住房公积金利息收入".equals(vice.getZjywlx()) || "增值收益利息收入".equals(vice.getZjywlx()) || "国家债券利息收入".equals(vice.getZjywlx())) {
                        if (content.containsKey("ZSID")) {
                            String zsid = content.get("ZSID").toString();
                            if (zsid != null) {
                                String zs = voucherManagerService.updateTemporaryRecord(zsid, true, voucherRes.getJZPZH());
                                System.out.println(zs);
                            } else {
                                throw new ErrorException(ReturnEnumeration.Data_MISS, "暂收记录不存在");
                            }
                        } else {
                            throw new ErrorException(ReturnEnumeration.Data_MISS, "暂收记录不存在");
                        }
                    }
                    if ("通用".equals(vice.getZjywlx())) {
                        if (content.containsKey("ACCID")) {
                            String accid = content.get("ACCID").toString();
                            if (accid != null) {
                                CBankAccChangeNotice notice = DAOBuilder.instance(icBankAccChangeNoticeDAO).UID(accid).getObject(new DAOBuilder.ErrorHandler() {
                                    @Override
                                    public void error(Exception e) {
                                        throw new ErrorException(e);
                                    }
                                });
                                if (notice != null) {
                                    StFinanceRecordingVoucher voucher = DAOBuilder.instance(financeRecordingVoucherDAO).searchFilter(new HashMap<String, Object>() {{
                                        this.put("jzpzh", voucherRes.getJZPZH());
                                        this.put("cFinanceRecordingVoucherExtension.sfzjl", true);
                                    }}).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
                                        @Override
                                        public void error(Exception e) {
                                            throw new ErrorException(e);
                                        }
                                    });
                                    if (voucher != null) {
                                        notice.setIs_make_acc("1");
                                        notice.setJzpzh(voucherRes.getJZPZH());
                                        notice.setcFinanceRecordingVoucherExtension(voucher.getcFinanceRecordingVoucherExtension());
                                        DAOBuilder.instance(icBankAccChangeNoticeDAO).entity(notice).save(new DAOBuilder.ErrorHandler() {
                                            @Override
                                            public void error(Exception e) {
                                                throw new ErrorException(e);
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }

                }
                savaToBase(financeBusinessProcess);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }

    private StCommonUnit getStCommonUnit(String dwzh) {
        try {
            StCommonUnit dwxx = DAOBuilder.instance(commonUnitDAO).searchFilter(new HashMap<String, Object>() {{
                this.put("dwzh", dwzh);
            }}).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            return dwxx;
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }

    public void checkParamters(String YWLX, Map map, General general) {
        List<TableRow> tableRows = general.getTableRows();
        BigDecimal debitAmount = BigDecimal.ZERO;
        BigDecimal creditAmount = BigDecimal.ZERO;
        for (TableRow row : tableRows) {
            BigDecimal debit = NumberUtils.isNumber(row.getDebitAmount()) ? new BigDecimal(row.getDebitAmount()) : BigDecimal.ZERO;
            BigDecimal credit = NumberUtils.isNumber(row.getCreditAmount()) ? new BigDecimal(row.getCreditAmount()) : BigDecimal.ZERO;
            if (debit.multiply(credit).compareTo(BigDecimal.ZERO) != 0) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "模板不正确");
            }
            if (debit.compareTo(BigDecimal.ZERO) < 0) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "借方金额不能为负数");
            }
            if (credit.compareTo(BigDecimal.ZERO) < 0) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "贷方金额不能为负数");
            }
            debitAmount = debitAmount.add(debit);
            creditAmount = creditAmount.add(credit);
        }
        checkArgument(debitAmount.equals(creditAmount), "借方贷方金额合计不相同");
        Set<String> keys = map.keySet();
        if (keys.contains("FSE")) {
            if (new BigDecimal(map.get("FSE").toString()).compareTo(BigDecimal.ZERO) <= 0) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "发生额不能小于或等于0");
            }
            checkArgument(NumberUtils.isNumber(map.get("FSE").toString()), "发生额异常");
        }
        if (YWLX.equals("跨行转账")) {
            String ffyhzh = map.get("FFYHMC").toString();
            String sfyhzh = map.get("SFYHMC").toString();
            if (bankFianceHandler.bankSimilar(ffyhzh, sfyhzh)) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "收付方银行不能相同");
            }
        }
        if (YWLX.equals("同行转账")) {
            String ffyhzh = map.get("FFYHZH").toString();
            String sfyhzh = map.get("SFYHZH").toString();
            if (ffyhzh.equals(sfyhzh)) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "收付方账号不能相同");
            }
        }

        StCommonUnit zcdwxx;
        if (YWLX.equals("未分摊转移")) {
            String zcdwzh = map.get("ZCDWZH").toString();
            String zrdwzh = map.get("ZRDWZH").toString();
            if (zcdwzh.equals(zrdwzh)) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "转出单位和转入单位不能相同");
            }
            String fse = map.get("FSE").toString();
            if (new BigDecimal(fse).compareTo(BigDecimal.ZERO) <= 0) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "转出金额不能小于或等于0");
            }
            zcdwxx = getStCommonUnit(zcdwzh);
            if (zcdwxx != null) {
                StCollectionUnitAccount zcaccount = zcdwxx.getCollectionUnitAccount();
                BigDecimal zczsye = zcaccount.getExtension().getZsye();
                if (zczsye.compareTo(new BigDecimal(fse)) < 0) {
                    throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "转出金额不能大于余额");
                }
            } else {
                throw new ErrorException(ReturnEnumeration.Data_MISS, "单位信息不存在");
            }
        }

        if (keys.contains("ZSJL")) checkArgument(checkString(map.get("ZSJL").toString()), "暂收记录异常");
        if (keys.contains("YHMC")) checkArgument(checkString(map.get("YHMC").toString()), "银行名称异常");
        if (keys.contains("YHZH")) checkArgument(checkString(map.get("YHZH").toString()), "银行账号异常");
        if (keys.contains("FSRQ")) checkArgument(checkString(map.get("FSRQ").toString()), "发生日期异常");
        if (keys.contains("SSDW")) checkArgument(checkString(map.get("SSDW").toString()), "所属单位异常");
        if (keys.contains("FFYHZH")) checkArgument(checkString(map.get("FFYHZH").toString()), "付方银行账号异常");
        if (keys.contains("SFYHZH")) checkArgument(checkString(map.get("SFYHZH").toString()), "收方银行账号异常");
        if (keys.contains("FFYHHM")) checkArgument(checkString(map.get("FFYHHM").toString()), "付方银行户名异常");
        if (keys.contains("SFYHHM")) checkArgument(checkString(map.get("SFYHHM").toString()), "收方银行户名异常");
        if (keys.contains("FFYHMC")) checkArgument(checkString(map.get("FFYHMC").toString()), "付方银行名称异常");
        if (keys.contains("SFYHMC")) checkArgument(checkString(map.get("SFYHMC").toString()), "收方银行名称异常");
        if (keys.contains("JTNY")) checkArgument(checkString(map.get("JTNY").toString()), "计提年月异常");
        if (keys.contains("ZFN")) checkArgument(checkString(map.get("ZFN").toString()), "支付年异常");
        if (keys.contains("ZFNY")) checkArgument(checkString(map.get("ZFNY").toString()), "支付年月异常");
        if (keys.contains("ZCDW")) checkArgument(checkString(map.get("ZCDW").toString()), "转出单位异常");
        if (keys.contains("ZRDW")) checkArgument(checkString(map.get("ZRDW").toString()), "转入单位异常");
        if (keys.contains("ZYJE")) checkArgument(NumberUtils.isNumber(map.get("ZYJE").toString()), "转移金额异常");
        if (keys.contains("FSSJ")) checkArgument(checkString(map.get("FSSJ").toString()), "发生时间异常");
        if (keys.contains("ZY")) checkArgument(checkString(map.get("ZY").toString()), "摘要异常");
        if (keys.contains("NF")) checkArgument(checkString(map.get("NF").toString()), "年份异常");
        if (keys.contains("YHMC")) checkArgument(checkString(map.get("YHMC").toString()), "银行名称异常");
    }

    public boolean checkString(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public void checkArgument(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, (String.valueOf(errorMessage)));
        }
    }


    /**
     * @param taskName
     * @param ywlsh
     * @return
     */
    public boolean isEnable(String taskName, String ywlsh) {
        if (flexibleTask.contains(taskName)) {
            StFinanceRecordingVoucher stFinanceRecordingVoucher = DAOBuilder.instance(financeRecordingVoucherDAO).searchFilter(new HashMap<String, Object>() {{
                this.put("cFinanceRecordingVoucherExtension.sfjz", false);
                this.put("cFinanceRecordingVoucherExtension.ywlsh", ywlsh);
            }}).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            return (stFinanceRecordingVoucher != null);
        }
        return false;
    }

    /**
     * check wither task can enable
     *
     * @param baseFinanceModel
     * @return
     */
    public BaseFinanceModel wrapEnable(BaseFinanceModel baseFinanceModel) {
        boolean enable = isEnable(baseFinanceModel.getYWMC(), baseFinanceModel.getYWLSH());
        baseFinanceModel.setEnable(enable);

        return baseFinanceModel;
    }

}
