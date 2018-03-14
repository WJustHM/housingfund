package com.handge.housingfund.collection.service.unitdeposit;

import com.handge.housingfund.collection.service.common.BaseService;
import com.handge.housingfund.collection.utils.AssertUtils;
import com.handge.housingfund.collection.utils.BusUtils;
import com.handge.housingfund.collection.utils.ComUtils;
import com.handge.housingfund.common.service.ISaveAuditHistory;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNotice;
import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNoticeFile;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessStatus;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.collection.enumeration.CommonFieldType;
import com.handge.housingfund.common.service.collection.enumeration.UnitAccountStatus;
import com.handge.housingfund.common.service.collection.model.BusCommonRetrun;
import com.handge.housingfund.common.service.collection.model.deposit.*;
import com.handge.housingfund.common.service.collection.service.common.WorkCondition;
import com.handge.housingfund.common.service.collection.service.trader.ICollectionTrader;
import com.handge.housingfund.common.service.collection.service.unitdeposit.UnitRemittance;
import com.handge.housingfund.common.service.finance.IVoucherAutoService;
import com.handge.housingfund.common.service.finance.IVoucherManagerService;
import com.handge.housingfund.common.service.finance.model.VoucherRes;
import com.handge.housingfund.common.service.finance.model.enums.VoucherBusinessType;
import com.handge.housingfund.common.service.finance.model.enums.WFTLY;
import com.handge.housingfund.common.service.loan.model.CommonResponses;
import com.handge.housingfund.common.service.others.IDictionaryService;
import com.handge.housingfund.common.service.others.IPdfService;
import com.handge.housingfund.common.service.others.IUploadImagesService;
import com.handge.housingfund.common.service.others.model.SingleDictionaryDetail;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.*;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by 凡 on 2017/7/25.
 * 单位汇缴
 */
@Service
public class UnitRemittanceImpl extends BaseService implements UnitRemittance {

    private static final String SKFS_ZZ = "转账收款";   //01
    private static final String SKFS_ZS = "暂收收款";   //02
    private static final String SKFS_WT = "委托收款";   //04

    private String busType = BusinessSubType.归集_汇缴记录.getSubType();
    private String ywlx = CollectionBusinessType.汇缴.getCode();
    private String ywlxmc = CollectionBusinessType.汇缴.getName();

    @Autowired
    private ICCollectionUnitRemittanceViceDAO remittanceViceDAO;

    @Autowired
    private ICCollectionUnitDepositInventoryViceDAO unitDepositInventoryViceDAO;

    @Autowired
    private ICCollectionUnitDepositInventoryViceDAO inventoryViceDAO;

    @Autowired
    private IStCommonPersonDAO personDAO;

    @Autowired
    private IStCommonUnitDAO unitDAO;

    @Autowired
    private IStCollectionUnitAccountDAO unitAccountDAO;

    @Autowired
    private ICollectionTrader trader;

    @Autowired
    private IPdfService pdfService;

    @Autowired
    private IStCollectionPersonalBusinessDetailsDAO personalBusinessDAO;

    @Autowired
    private IVoucherManagerService voucherManagerService;

    @Autowired
    IUploadImagesService iUploadImagesService;
    @Autowired
    private IDictionaryService iDictionaryService;

    @Autowired
    private ICAccountNetworkDAO cAccountNetworkDAO;

    @Autowired
    private ICCollectionUnitPayholdViceDAO payholdDAO;

    @Autowired
    private ISaveAuditHistory iSaveAuditHistory;

    @Autowired
    private IVoucherAutoService voucherAutoService;

    @Autowired
    private ICFinanceRecordUnitDAO icFinanceRecordUnitDAO;

    /**
     * 汇缴申请时，根据账号+汇补缴年月查询清册信息
     */
    @Override
    public ListRemittanceInventoryRes getUnitRemittanceInventory(TokenContext tokenContext, String dwzh, String hbjny) {
        //判断单位是否有未进行汇缴的清册信息，判断后得到qcqrdh
        String qcyf = ComUtils.parseToYYYYMM(hbjny);
        CCollectionUnitDepositInventoryVice unitInventoryMsg = unitDepositInventoryViceDAO.getUnitInventoryMsg(dwzh, qcyf);
        if (null == unitInventoryMsg) {
            throw new ErrorException("当前单位要汇缴的月份没有清册信息！");
        }
        ListRemittanceInventoryRes result = new ListRemittanceInventoryRes();
        StCollectionUnitBusinessDetails dwywmx = unitInventoryMsg.getDwywmx();
        ListRemittanceInventoryResDWJCXX dwjcxx = new ListRemittanceInventoryResDWJCXX();
        dwjcxx.setFSE(unitInventoryMsg.getQcfse().toString());
        dwjcxx.setFSRS(unitInventoryMsg.getDwywmx().getFsrs().toString());
        dwjcxx.setGRYJCEZHJ(unitInventoryMsg.getGryjcehj().toString());
        dwjcxx.setDWYJCEZHJ(unitInventoryMsg.getDwyjcehj().toString());
        BigDecimal zje = unitInventoryMsg.getDwyjcehj().add(unitInventoryMsg.getDwyjcehj());
        dwjcxx.setZHJ(zje.toString());
        dwjcxx.setHBJNY(ComUtils.parseToYYYYMM2(unitInventoryMsg.getQcny()));
        dwjcxx.setQCQRDH(unitInventoryMsg.getQcqrdh());
        result.setDWJCXX(dwjcxx);
        //清册详情
        ArrayList<ListRemittanceInventoryResDWHJQC> list = new ArrayList<ListRemittanceInventoryResDWHJQC>();
        Set<CCollectionUnitDepositInventoryDetailVice> qcxqList = unitInventoryMsg.getQcxq();
        for (CCollectionUnitDepositInventoryDetailVice perDetail : qcxqList) {
            ListRemittanceInventoryResDWHJQC perView = new ListRemittanceInventoryResDWHJQC();
            BigDecimal dwyjce = perDetail.getDwyjce();
            BigDecimal gryjce = perDetail.getGryjce();
            BigDecimal heji = dwyjce.add(gryjce);
            perView.setDWYJCE(dwyjce.toString());
            perView.setGRYJCE(gryjce.toString());
            perView.setHeJi(heji.toString());
            perView.setGRJCJS(perDetail.getGrjcjs().toString());
            perView.setGRZH(perDetail.getGrzh());
            //查询个人信息 以后考虑冗余
            StCommonPerson person = personDAO.getByGrzh(perDetail.getGrzh());
            perView.setXingMing(person.getXingMing());
            perView.setZJHM(person.getZjhm());
            perView.setZJLX(person.getZjlx());
            list.add(perView);
        }
        result.setDWHJQC(list);
        return result;
    }

    /**
     * 查询单位汇缴业务记录列表
     */
    public PageRes<ListUnitRemittanceResRes> getUnitRemittanceList(TokenContext tokenContext, String dwmc, String dwzh, String ywzt, String czy, String yhmc, String YWWD, String kssj, String jssj, String ywpch, String pageNumber, String pageSize) {
        //增加凭证关联查询 要求ywpzh非空
        if (!ComUtils.isEmpty(ywpch)) {
            return getRemittanceList(ywpch);
        }
        //参数验证
        int pageno = ComUtils.parstPageNo(pageNumber);
        int pagesize = ComUtils.parstPageSize(pageSize);

        IBaseDAO.CriteriaExtension ce = getCriteriaExtension(dwmc, dwzh, ywzt, czy, yhmc, YWWD);

        Date startDate = ComUtils.parseToDate(kssj, "yyyy-MM-dd HH:mm");
        Date endDate = ComUtils.parseToDate(jssj, "yyyy-MM-dd HH:mm");
        PageResults<CCollectionUnitRemittanceVice> pageResults = remittanceViceDAO.listWithPage(null, startDate, endDate, "created_at", Order.DESC, null, SearchOption.REFINED, pageno, pagesize, ce);
        List<CCollectionUnitRemittanceVice> results = pageResults.getResults();

        ArrayList<ListUnitRemittanceResRes> resultList = new ArrayList<>();
        for (CCollectionUnitRemittanceVice rowSource : results) {
            ListUnitRemittanceResRes rowView = getRowView(rowSource);
            resultList.add(rowView);
        }
        PageRes<ListUnitRemittanceResRes> pageres = new PageRes<>();
        pageres.setResults(resultList);
        pageres.setCurrentPage(pageResults.getCurrentPage());
        pageres.setNextPageNo(pageResults.getPageNo());
        pageres.setPageSize(pageResults.getPageSize());
        pageres.setPageCount(pageResults.getPageCount());
        pageres.setTotalCount(pageResults.getTotalCount());

        return pageres;
    }

    private PageRes<ListUnitRemittanceResRes> getRemittanceList(String ywpch) {

        CCollectionUnitDepositInventoryBatchVice batchInventory = inventoryViceDAO.getBatchInventory(ywpch);

        if (!ComUtils.isEmpty(batchInventory)) {
            return batchList(batchInventory);
        } else {
            return singleRemittance(ywpch);
        }

    }

    private PageRes<ListUnitRemittanceResRes> singleRemittance(String ywlsh) {
        ArrayList<ListUnitRemittanceResRes> resultList = new ArrayList<>();
        CCollectionUnitRemittanceVice rowSource = remittanceViceDAO.getByYwlsh(ywlsh);
        AssertUtils.notEmpty(rowSource, "业务流水号有误，不存在对应的汇缴清册业务！");
        ListUnitRemittanceResRes rowView = getRowView(rowSource);
        resultList.add(rowView);

        PageRes<ListUnitRemittanceResRes> pageres = new PageRes<>();
        pageres.setResults(resultList);
        pageres.setCurrentPage(1);
        pageres.setNextPageNo(1);
        pageres.setPageSize(1);
        pageres.setPageCount(1);
        pageres.setTotalCount(1);
        return pageres;
    }

    private PageRes<ListUnitRemittanceResRes> batchList(CCollectionUnitDepositInventoryBatchVice batchInventory) {
        ArrayList<ListUnitRemittanceResRes> resultList = new ArrayList<>();
        Set<CCollectionUnitDepositInventoryVice> qclb = batchInventory.getQclb();
        for (CCollectionUnitDepositInventoryVice inventory : qclb) {
            String qcqrdh = inventory.getQcqrdh();
            CCollectionUnitRemittanceVice rowSource = remittanceViceDAO.getByQcqrdh(qcqrdh);
            ListUnitRemittanceResRes rowView = getRowView(rowSource);
            resultList.add(rowView);
        }

        int size = qclb.size();
        PageRes<ListUnitRemittanceResRes> pageres = new PageRes<>();
        pageres.setResults(resultList);
        pageres.setCurrentPage(1);
        pageres.setNextPageNo(1);
        pageres.setPageSize(1);
        pageres.setPageCount(size);
        pageres.setTotalCount(size);
        return pageres;

    }

    private ListUnitRemittanceResRes getRowView(CCollectionUnitRemittanceVice rowSource) {
        ListUnitRemittanceResRes rowView = new ListUnitRemittanceResRes();
        StCollectionUnitBusinessDetails dwywmx = rowSource.getDwywmx();
        CCollectionUnitBusinessDetailsExtension extension = dwywmx.getExtension();
        rowView.setYWLSH(dwywmx.getYwlsh());
        rowView.setDWMC(dwywmx.getUnit().getDwmc());
        rowView.setDWZH(dwywmx.getDwzh());
        rowView.setFSRS(dwywmx.getFsrs().intValue());
        rowView.setFSE(getFse(rowSource).toString());
        rowView.setHBJNY(ComUtils.parseToYYYYMM2(rowSource.getHbjny()));
        rowView.setYWZT(extension.getStep());
        rowView.setJZPZH(extension.getJzpzh());
        rowView.setCZY(extension.getCzy());
        rowView.setSLSJ(ComUtils.parseToString(extension.getSlsj(), "yyyy-MM-dd HH:mm"));
        rowView.setYWWD(extension.getYwwd().getMingCheng());
        rowView.setId(rowSource.getId());
        rowView.setSBYY(filterNull(extension.getBeizhu(), extension.getStep()));
        return rowView;
    }

    private String filterNull(String beizhu, String step) {
        if ("入账失败".equals(step)) {
            return ComUtils.filterNull(beizhu);
        } else {
            return "";
        }
    }

    private BigDecimal getFse(CCollectionUnitRemittanceVice rowSource) {
        StCollectionUnitBusinessDetails dwywmx = rowSource.getDwywmx();
        CCollectionUnitBusinessDetailsExtension extension = dwywmx.getExtension();
        if ("已入账分摊".equals(extension.getStep())) {
            return dwywmx.getFse();
        }
        return rowSource.getFse();
    }

    /**
     * 新建汇缴申请
     * 提交
     * 现在改变行为，增加参数业务流水号
     * 如果业务流水号存在更新即可，不存在则新建一笔业务
     */
    public AddUnitRemittanceRes postUnitRemittance(TokenContext tokenContext, UnitRemittancePost unitRemittancePost) {
        //验证
        postCheck(unitRemittancePost);

        String ywlsh = unitRemittancePost.getYWLSH();
        String dwzh = unitRemittancePost.getDWZH();
        String hbjny = ComUtils.parseToYYYYMM(unitRemittancePost.getHBJNY());
        CCollectionUnitRemittanceVice result = remittanceViceDAO.getByYwlsh(ywlsh);
        CCollectionUnitDepositInventoryVice unitInventoryMsg = unitDepositInventoryViceDAO.getUnitInventoryMsg(dwzh, hbjny);
        StCommonUnit unit = unitDAO.getUnit(dwzh);

        if (!unit.getExtension().getKhwd().equals(tokenContext.getUserInfo().getYWWD())) {
            throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务不在当前网点受理范围内");
        }

        String hjfs = unitRemittancePost.getHJFS();
        //result.setQcqrdh(unitInventoryMsg.getQcqrdh());
        //result.setHbjny(hbjny);
        result.setHjfs(hjfs);
        result.setStyhdm(unitRemittancePost.getSTYHDM());
        result.setStyhmc(unitRemittancePost.getSTYHMC());

        StSettlementSpecialBankAccount bankAcount = BusUtils.getBankAcount(unit.getStyhdm(), "01");
        result.setStyhzh(bankAcount.getYhzhhm());
        result.setFse(unitInventoryMsg.getQcfse());
        StCollectionUnitBusinessDetails dwywmx = result.getDwywmx();
        //dwywmx.setFsrs(unitInventoryMsg.getDwywmx().getFsrs());
        //dwywmx.setDwzh(dwzh);
        dwywmx.setFse(BigDecimal.ZERO);
        dwywmx.setYwmxlx(ywlx);     // 01表示汇缴
        dwywmx.setCzbz(CommonFieldType.非冲账.getCode());

        CCollectionUnitBusinessDetailsExtension extension = dwywmx.getExtension();
        //extension.setStep("初始状态");
        extension.setCzmc(ywlx);    //表示汇缴
        CAccountNetwork network = cAccountNetworkDAO.get(tokenContext.getUserInfo().getYWWD());
        extension.setYwwd(network);
        extension.setJbrzjlx(unit.getJbrzjlx());
        extension.setJbrxm(unit.getJbrxm());
        extension.setJbrzjhm(unit.getJbrzjhm());
        extension.setCzy(tokenContext.getUserInfo().getCZY());
        extension.setSlsj(new Date());
        extension.setBlzl(unitRemittancePost.getBLZL());

        remittanceViceDAO.update(result);
        //保存历史
        BusUtils.saveAuditHistory(ywlsh, tokenContext, ywlxmc, "新建");

        //根据收款方式，判断办结调用事件类型，01转账收款/02暂收收款/04委托收款
        String event = null;
        if ("01".equals(hjfs)) {
            event = SKFS_ZZ;
        } else if ("02".equals(hjfs)) {
            event = SKFS_ZS;
        } else if ("04".equals(hjfs)) {
            event = SKFS_WT;
        } else {
            throw new ErrorException("暂时只支持:01转账收款/02暂收收款/04委托收款！");
        }
        //流程调用  //TODO 暂时这样处理 业务状态要修正
        WorkCondition condition = new WorkCondition();
        condition.setYwlsh(ywlsh);
        condition.setCzy(tokenContext.getUserInfo().getCZY());
        condition.setYwwd(tokenContext.getUserInfo().getYWWD());
        condition.setStatus("待确认");
        condition.setEvent(event);
        condition.setZtlx("1");
        condition.setType(BusinessType.Collection);
        condition.setSubType(busType);    //汇缴

        doWork(condition);

        // TODO 暂时这样处理
        //简单处理 委托收款方式将发送报文
        if ("01".equals(hjfs)) {
            //throw new ErrorException("暂不支持委托收款!");
            sendRemittance(ywlsh, "01");
        } else if ("04".equals(hjfs)) {
            sendRemittance(ywlsh, "04");
        }
        //返回成功消息
        AddUnitRemittanceRes result2 = new AddUnitRemittanceRes();
        result2.setStatus("success");
        result2.setYWLSH(ywlsh);
        return result2;
    }

    //判断单位庄户状态是否处于可缴款的状态:正常/开户/缓缴
    private boolean checkUnitStateIsTrue(StCommonUnit unit) {
        return "01|02|03".indexOf(unit.getCollectionUnitAccount().getDwzhzt()) >= 0;
    }

    private void postCheck(UnitRemittancePost unitRemittancePost) {

        String ywlsh = unitRemittancePost.getYWLSH();
        CCollectionUnitRemittanceVice remittance = remittanceViceDAO.getByYwlsh(ywlsh);
        if (remittance == null) {
            throw new ErrorException("ywlsh出现错误，该业务不存在！");
        }
        String dwzh = unitRemittancePost.getDWZH();
        String hbjny = ComUtils.parseToYYYYMM(unitRemittancePost.getHBJNY());
        if (ComUtils.isEmpty(dwzh) || ComUtils.isEmpty(hbjny)) {
            throw new ErrorException("参数未输入：单位账号和汇补缴年月不能为空！");
        }
        StCollectionUnitBusinessDetails dwywmx = remittance.getDwywmx();
        if (!dwzh.equals(dwywmx.getDwzh())) {
            throw new ErrorException("参数传入的单位账号" + dwzh + "，与业务已生成的单位账号" + dwywmx.getDwzh() + "不匹配！");
        }
        if (!hbjny.equals(dwywmx.getHbjny())) {
            throw new ErrorException("参数传入的汇补缴年月" + unitRemittancePost.getHBJNY() + "，与业务已生成的汇补缴年月" +
                    ComUtils.parseToYYYYMM2(dwywmx.getHbjny()) + "不匹配！");
        }
        //业务状态验证
        String step = dwywmx.getExtension().getStep();
        if (!"待确认".equals(step)) {
            throw new ErrorException("汇缴提交，该笔业务只能为待确认状态！当前业务状态为：" + step);
        }
        //单位存在验证
        StCommonUnit unit = unitDAO.getUnit(dwzh);
        if (null == unit) {
            throw new ErrorException("该单位账号对应的单位不存在");
        }
        //得到该月的清册信息
        CCollectionUnitDepositInventoryVice unitInventoryMsg = unitDepositInventoryViceDAO.getUnitInventoryMsg(dwzh, hbjny);
        if (null == unitInventoryMsg) {
            throw new ErrorException("单位该月清册不存在");
        }
        //如果是暂收收款,需要判断暂收款大于等于发生额
        if ("02".equals(unitRemittancePost.getHJFS())) {
            BigDecimal fse = unitInventoryMsg.getQcfse();
            BigDecimal zsye = unit.getCollectionUnitAccount().getExtension().getZsye();
            if (zsye == null) {
                throw new ErrorException("数据异常，单位的未分摊余额为空！");
            }
            if (fse.compareTo(zsye) > 0) {
                throw new ErrorException("未分摊余额不足，请选择其他缴款方式！");
            }
        }
        //判断单位账户状态是否为正常
        if (!(checkUnitStateIsTrue(unit))) {
            throw new ErrorException("单位账户状态只能为可缴存状态：正常/开户/缓缴!");
        }

        /*//办理资料验证
        if(!iUploadImagesService.validateUploadFile(UploadFileModleType.归集.getCode(), UploadFileBusinessType.汇缴申请.getCode(),unitRemittancePost.getBLZL())){
            throw new ErrorException("上传资料缺失");
        }*/
    }

    /**
     * 根据单位账号获取汇缴申请的单位信息
     */
    public AutoUnitRemittanceRes getUnitRemittanceAuto(TokenContext tokenContext, String dwzh) {

        AutoUnitRemittanceRes result = new AutoUnitRemittanceRes();
        StCommonUnit unit = unitDAO.getUnit(dwzh);
        StCollectionUnitAccount unitAccount = unit.getCollectionUnitAccount();
        result.setDWFCRS(unitAccount.getDwfcrs().intValue());
        result.setDWJCRS(unitAccount.getDwjcrs().intValue());
        result.setDWMC(unit.getDwmc());
        result.setZSYE(unitAccount.getExtension().getZsye().toString());
        // TODO
        result.setYWWD(tokenContext.getUserInfo().getYWWD());
        result.setCZY(tokenContext.getUserInfo().getCZY());
        result.setDWZH(dwzh);
        result.setDWYHJNY(BusUtils.getDWYHJNY(dwzh));
        result.setDWZHYE(unitAccount.getDwzhye().toString());
        result.setJBRZJHM(unit.getJbrzjhm());
        result.setJBRXM(unit.getJbrxm());
        result.setJBRZJLX(unit.getJbrzjlx());
        result.setJZNY(ComUtils.parseToYYYYMM2(unitAccount.getJzny()));
        result.setSTYHDM(unit.getStyhdm());
        result.setSTYHMC(unit.getStyhmc());
        StSettlementSpecialBankAccount bankAcount = BusUtils.getBankAcount(unit.getStyhdm(), "01");
        result.setSTYHZH(bankAcount.getYhzhhm());

        return result;
    }

    /**
     * 根据ywlsh得到汇缴申请详情
     */
    public GetUnitRemittanceRes getUnitRemittance(TokenContext tokenContext, String ywlsh) {
        GetUnitRemittanceRes result = new GetUnitRemittanceRes();

        CCollectionUnitRemittanceVice remittanceMsg = remittanceViceDAO.getByYwlsh(ywlsh);
        CCollectionUnitDepositInventoryVice unitInventoryMsg = inventoryViceDAO.getUnitInventoryMsg(remittanceMsg.getQcqrdh());

        StCollectionUnitBusinessDetails dwywmx = remittanceMsg.getDwywmx();
        StCommonUnit unit = dwywmx.getUnit();
        CCollectionUnitBusinessDetailsExtension extension = dwywmx.getExtension();
        String dwzh = dwywmx.getDwzh();
        result.setDWZH(dwzh);
        result.setDWMC(unit.getDwmc());
        result.setDWJCRS(unitInventoryMsg.getDwjcrs().intValue());
        result.setDWFCRS(unitInventoryMsg.getDwfcrs().intValue());

        result.setDWZHYE(unitInventoryMsg.getQcpc().getDwzhye().toString());
        // TODO 这里等财务设计出来修改，财务账户不显示暴露
        result.setSTYHDM(unit.getStyhdm());
        result.setSTYHMC(unit.getStyhmc());
        result.setSTYHHM(unit.getStyhmc());

        StSettlementSpecialBankAccount bankAcount = BusUtils.getBankAcount(unit.getStyhdm(), "01");
        result.setSTYHZH(bankAcount.getYhzhhm());

        result.setFSRS(dwywmx.getFsrs().toString());
        result.setFSE(ComUtils.moneyFormat(getFse(remittanceMsg)));

        result.setYWWD(extension.getYwwd().getMingCheng());
        result.setCZY(extension.getCzy());

        result.setJBRZJLX(extension.getJbrzjlx());
        result.setJBRXM(extension.getJbrxm());
        result.setJBRZJHM(extension.getJbrzjhm());

        result.setZSYE(unitInventoryMsg.getQcpc().getWftye().toString());
        String jzny = ComUtils.parseToYYYYMM2(unitInventoryMsg.getQcpc().getJzny());
        if (!ComUtils.isEmpty(jzny)) {
            result.setJZNY(jzny);
        } else {
            result.setJZNY("首次汇缴");
        }
        result.setDWYHJNY(ComUtils.parseToYYYYMM2(dwywmx.getHbjny()));
        result.setHBJNY(ComUtils.parseToYYYYMM2(dwywmx.getHbjny()));
        result.setHJFS(remittanceMsg.getHjfs());
        result.setQCQRDH(remittanceMsg.getQcqrdh());
        result.setYWLSH(dwywmx.getYwlsh());

        String blzl = dwywmx.getExtension().getBlzl();
        if (ComUtils.isEmpty(blzl)) {
            blzl = "";
        }
        result.setBLZL(blzl);
        //result.setJXRQ(); 取入账日期

        ArrayList<GetUnitRemittanceResQCQR> qcqrList = new ArrayList<GetUnitRemittanceResQCQR>();
        Set<CCollectionUnitDepositInventoryDetailVice> qcxq = unitInventoryMsg.getQcxq();
        for (CCollectionUnitDepositInventoryDetailVice detailDate : qcxq) {
            if ("01".equals(detailDate.getGrzhzt())) {
                GetUnitRemittanceResQCQR detailView = new GetUnitRemittanceResQCQR();
                StCommonPerson person = personDAO.getByGrzh(detailDate.getGrzh());
                detailView.setXingMing(person.getXingMing());
                detailView.setZJHM(person.getZjhm());
                detailView.setGRZH(detailDate.getGrzh());
                detailView.setGRJCJS(ComUtils.moneyFormat(detailDate.getGrjcjs()));
                detailView.setDWYJCE(ComUtils.moneyFormat(detailDate.getDwyjce()));
                detailView.setGRYJCE(ComUtils.moneyFormat(detailDate.getGryjce()));
                BigDecimal heji = detailDate.getDwyjce().add(detailDate.getGryjce());
                detailView.setHeJi(ComUtils.moneyFormat(heji));
                qcqrList.add(detailView);
            }
        }
        result.setQCQR(qcqrList);
        return result;
    }

    /**
     * 汇缴申请修改,只能修改缴款方式/缴存的银行账户
     */
    public ReUnitRemittanceRes putUnitRemittance(TokenContext tokenContext, String ywlsh, UnitRemittancePut unitRemittancePut) {

        CCollectionUnitRemittanceVice remittanceMsg = remittanceViceDAO.getByYwlsh(ywlsh);
        if (null == remittanceMsg) {
            throw new ErrorException("要修改的汇缴业务不存在");
        }
        remittanceMsg.setHjfs(unitRemittancePut.getHJFS());

        //remittanceMsg.setStyhzh(unitRemittancePut.getWTYHZH());
        //remittanceMsg.setStyhmc(unitRemittancePut.getWTYHMC());
        //remittanceMsg.setStyhdm(unitRemittancePut.getWTYHDM());
        //remittanceViceDAO.save(remittanceMsg);

        ReUnitRemittanceRes result = new ReUnitRemittanceRes();
        result.setStatus("success");
        result.setYWLSH(ywlsh);
        return result;
    }

    /**
     * 汇缴信息回执单
     */
    public CommonResponses headUnitRemittance(String ywlsh) {

        HeadUnitRemittanceReceiptRes result = new HeadUnitRemittanceReceiptRes();

        CCollectionUnitRemittanceVice remittanceMsg = remittanceViceDAO.getByYwlsh(ywlsh);
        if (null == remittanceMsg) {
            throw new ErrorException("该笔汇缴业务不存在");
        }
        String step = remittanceMsg.getDwywmx().getExtension().getStep();
        if (!"已入账分摊".equals(step)) {
            throw new ErrorException("该笔汇缴业务未办结，当前业务状态：" + step);
        }

        CCollectionUnitDepositInventoryVice unitInventoryMsg = inventoryViceDAO.getUnitInventoryMsg(remittanceMsg.getQcqrdh());
        StCollectionUnitBusinessDetails dwywmx = remittanceMsg.getDwywmx();
        StCommonUnit unit = dwywmx.getUnit();
        StCollectionUnitAccount unitAccount = unit.getCollectionUnitAccount();
        String dwzh = unit.getDwzh();
        result.setDWZH(dwzh);
        result.setDWMC(unit.getDwmc());
        result.setDWFCRS(unitAccount.getDwfcrs().intValue());
        result.setDWJCRS(unitAccount.getDwjcrs().intValue());
        result.setZSYE(ComUtils.moneyFormat(unitAccount.getExtension().getZsye()));

        result.setDWYHJNY(BusUtils.getDWYHJNY(dwzh));
        result.setDWZHYE(ComUtils.moneyFormat(unitAccount.getDwzhye()));
        result.setHJNY(ComUtils.parseToYYYYMM2(remittanceMsg.getHbjny()));
        result.setCZY(dwywmx.getExtension().getCzy());
        result.setJBRXM(dwywmx.getExtension().getJbrxm());
        result.setJBRZJHM(dwywmx.getExtension().getJbrzjhm());
        result.setJBRZJLX(dwywmx.getExtension().getJbrzjlx());
        result.setTZSJ(ComUtils.parseToString(new Date(), "yyyy-MM"));
        result.setYWLSH(ywlsh);
        result.setYWWD(dwywmx.getExtension().getYwwd().getMingCheng());
        //TODO
        result.setYZM(""); //验证码
        result.setZSYE(ComUtils.moneyFormat(unitAccount.getExtension().getZsye()));

        BigDecimal jszhj = BigDecimal.ZERO; //基数总合计

        ArrayList<HeadUnitRemittanceReceiptResDWHJQC> dqhjxq = new ArrayList<HeadUnitRemittanceReceiptResDWHJQC>();
        Set<CCollectionUnitDepositInventoryDetailVice> qcxq = unitInventoryMsg.getQcxq();

        BigDecimal dwyjcehj = BigDecimal.ZERO;
        BigDecimal gryjcehj = BigDecimal.ZERO;

        for (CCollectionUnitDepositInventoryDetailVice detail : qcxq) {
            if ("01".equals(detail.getGrzhzt())) {
                HeadUnitRemittanceReceiptResDWHJQC view = new HeadUnitRemittanceReceiptResDWHJQC();
                String grzh = detail.getGrzh();
                StCommonPerson peron = personDAO.getByGrzh(grzh);
                StCollectionPersonalAccount personalAccount = peron.getCollectionPersonalAccount();
                view.setXingMing(peron.getXingMing());
                view.setGRZH(grzh);
                view.setZJLX(peron.getZjlx());
                view.setZJHM(peron.getZjhm());
                view.setGRJCJS(ComUtils.moneyFormat(detail.getGrjcjs()));
                view.setDWYJCE(ComUtils.moneyFormat(detail.getDwyjce()));
                view.setGRYJCE(ComUtils.moneyFormat(detail.getGryjce()));
                BigDecimal heji = detail.getDwyjce().add(detail.getGryjce());
                view.setHeJi(ComUtils.moneyFormat(heji));
                dqhjxq.add(view);
                jszhj = jszhj.add(detail.getGrjcjs());

                dwyjcehj = dwyjcehj.add(detail.getDwyjce());
                gryjcehj = gryjcehj.add(detail.getGryjce());
            }
        }
        result.setDWHJQC(dqhjxq);
        HeadUnitRemittanceReceiptResYJEZHJ yjezhj = new HeadUnitRemittanceReceiptResYJEZHJ();
        yjezhj.setGRYJJSZHJ(ComUtils.moneyFormat(jszhj));
        yjezhj.setDWYJCEZHJ(ComUtils.moneyFormat(dwyjcehj));
        yjezhj.setGRYJCEZHJ(ComUtils.moneyFormat(gryjcehj));
        BigDecimal heji = dwyjcehj.add(gryjcehj);
        yjezhj.setZHJ(ComUtils.moneyFormat(heji));
        result.setYJEZHJ(yjezhj);

        HeadUnitRemittanceReceiptResJCXX jcxx = new HeadUnitRemittanceReceiptResJCXX();
        jcxx.setFSE(ComUtils.moneyFormat(getFse(remittanceMsg)));
        jcxx.setFSRS(dwywmx.getFsrs().intValue());
        jcxx.setHBJNY(ComUtils.parseToYYYYMM2(dwywmx.getHbjny()));
        if (remittanceMsg.getHjfs() != null) {
            SingleDictionaryDetail hjfs = iDictionaryService.getSingleDetail(remittanceMsg.getHjfs(), "RemitPayment");
            jcxx.setHJFS(hjfs != null ? hjfs.getName() : "");
        }
        StSettlementSpecialBankAccount bankAcount = BusUtils.getBankAcount(unit.getStyhdm(), "01");
        jcxx.setQCQRDH(remittanceMsg.getQcqrdh());
        jcxx.setSTYHDM(unit.getStyhdm());//受托银行代码
        jcxx.setSTYHHM(bankAcount.getYhzhmc());
        jcxx.setSTYHMC(unit.getStyhmc());//受托银行名称
        jcxx.setSTYHZH(remittanceMsg.getStyhzh());
        result.setJCXX(jcxx);

        String id = pdfService.getUnitRemittanceReceiptPdf(result);
        System.out.println("生成id的值：" + id);
        CommonResponses commonResponses = new CommonResponses();
        commonResponses.setId(id);
        commonResponses.setState("success");
        return commonResponses;
    }

    /**
     * 汇缴办结时处理,这里暂时只能写出简略的版本，财务/流水信息/入账都未开发
     * 1、单位缴款后，流水入账匹配，金额暂按缴存的发生额来分摊,再把钱分摊到每个人的账户上
     * 2、然后更新单位、个人账户信息
     * 3、单位、个人入账
     */
    public void doFinal(String ywlsh) {

        CCollectionUnitRemittanceVice dwhj = remittanceViceDAO.getByYwlsh(ywlsh);
        StCollectionUnitBusinessDetails dwywmx = dwhj.getDwywmx();

        String dwzh = dwywmx.getDwzh();
        String hbjny = dwywmx.getHbjny(); //对缴至年月进行更新
        if (null == dwhj) {
            throw new ErrorException("该笔汇缴业务不存在");
        }
        CCollectionUnitDepositInventoryVice inventory = unitDepositInventoryViceDAO.getUnitInventoryMsg(dwhj.getQcqrdh());
        ensure(inventory);

        StCommonUnit unit = unitDAO.getUnit(dwzh);

        StCollectionUnitAccount unitAccount = unit.getCollectionUnitAccount();

        //更新员工账户信息,包括金额增加
        Set<CCollectionUnitDepositInventoryDetailVice> qcxq = inventory.getQcxq();
        batchUpdatePeson(qcxq, hbjny, unit);

        //更新单位账户信息
        String hjfs = dwhj.getHjfs();   //根据汇缴方式的不同对单位账号进行处理
        BigDecimal fse = inventory.getQcfse();
        //01委托收款/02暂收收款
        if ("02".equals(hjfs)) {
            CCollectionUnitAccountExtension accountExtension = unitAccount.getExtension();
            //暂收款验证，必须大于或等于缴存的发生额,否则直接返回
            BigDecimal zsye = accountExtension.getZsye();
            if (fse.compareTo(zsye) > 0) {
                //返回错误信息或抛出异常
                throw new ErrorException("发生额大于当前单位的可用未分摊余额！");
            }
            zsye = zsye.subtract(fse);     //// 减少未分摊余额
            accountExtension.setZsye(zsye);
        }
        //单位账户余额
        BigDecimal dwzhye = unitAccount.getDwzhye();
        dwzhye = dwzhye.add(fse);
        unitAccount.setDwzhye(dwzhye);

        //预封存处理
        doBeforehandSealCheck(dwhj, unitAccount);

        //单位账户状态更新
        unitStateChange(unitAccount, hbjny);
        unitAccountDAO.update(unitAccount); //更新单位信息

        //发生额更新
        dwywmx.setFse(dwhj.getFse());
        remittanceViceDAO.update(dwywmx);

        //汇缴办结历史记录
        BusUtils.saveAuditHistory(ywlsh, "汇缴", "办结");

        BusUtils.refreshUnitAcount(dwzh);

    }

    private void ensure(CCollectionUnitDepositInventoryVice inventory) {
        Set<CCollectionUnitDepositInventoryDetailVice> qcxq = inventory.getQcxq();
        if (qcxq == null || qcxq.size() == 0) {
            List<CCollectionUnitDepositInventoryDetailVice> list = unitDepositInventoryViceDAO.getQcxq(inventory);
            inventory.setQcxq(new HashSet<>(list));
        }
        //unitDepositInventoryViceDAO.update(inventory);
    }

    private void batchUpdatePeson(Set<CCollectionUnitDepositInventoryDetailVice> qcxq, String hbjny, StCommonUnit unit) {
        int i = 0;
        for (CCollectionUnitDepositInventoryDetailVice qcDetail : qcxq) {
            if ("01".equals(qcDetail.getGrzhzt())) {
                updatePersonAcount(qcDetail, hbjny, unit);
                i++;
                if (i % 30 == 0) {
                    remittanceViceDAO.flushAndClear();
                }
            }
        }
        remittanceViceDAO.flushAndClear();
    }

    private void updatePersonAcount(CCollectionUnitDepositInventoryDetailVice qcDetail, String hbjny, StCommonUnit unit) {
        String grzh = qcDetail.getGrzh();
        BigDecimal dwyjce = qcDetail.getDwyjce();   //单位月缴额
        BigDecimal gryjce = qcDetail.getGryjce();   //个人月缴额
        BigDecimal gryjze = dwyjce.add(gryjce);     //个人月缴总额

        StCommonPerson person = personDAO.getByGrzh(grzh);
        StCollectionPersonalAccount personalAccount = person.getCollectionPersonalAccount();
        CCommonPersonExtension extension = person.getExtension();

        BigDecimal grzhye = personalAccount.getGrzhye(); //个人账户余额
        grzhye = grzhye.add(gryjze);
        personalAccount.setGrzhye(grzhye);  //设置个人账户余额更新
        extension.setGrjzny(hbjny);
        BigDecimal grzhdngjye = personalAccount.getGrzhdngjye(); //个人当年归集额
        grzhdngjye = grzhdngjye.add(gryjze);
        personalAccount.setGrzhdngjye(grzhdngjye);  //设置个人账户当年归集余额更新
        personDAO.updateNormal(person); //更新每个人的信息

        //TODO 汇缴产生个人业务表流水
        createPersonBus(grzh, gryjze, qcDetail, unit);

    }

    private void unitStateChange(StCollectionUnitAccount unitAccount, String hbjnyStr) {
        //1、判断是否是首次汇缴，若首次汇缴，单位账户状态为02开户，更新为01正常即可
        String dwzhzt = unitAccount.getDwzhzt();
        String dwzh = unitAccount.getDwzh();
        if ("02".equals(dwzhzt)) {
            unitAccount.setDwzhzt("01");
        }
        unitAccount.setJzny(hbjnyStr);
        //2、判断单位账户状态是否是缓缴
        if (UnitAccountStatus.缓缴.getCode().equals(dwzhzt)) {
            CCollectionUnitPayholdVice payHold = payholdDAO.getNearlyPayhold(dwzh);
            Date hjjssj = payHold.getHjjssj();
            Date hbjny = ComUtils.parseToDate(hbjnyStr, "yyyyMM");
            if (hbjny.compareTo(hjjssj) >= 0) {
                unitAccount.setDwzhzt("01");
            }
        }
    }

    private void createPersonBus(String grzh, BigDecimal fse, CCollectionUnitDepositInventoryDetailVice inventoryDetail, StCommonUnit unit) {

        StCollectionPersonalBusinessDetails personalBusiness = new StCollectionPersonalBusinessDetails();
        StCommonPerson person = personDAO.getByGrzh(grzh);

        personalBusiness.setFse(fse);
        personalBusiness.setUnit(unit);
        personalBusiness.setGrzh(grzh);
        personalBusiness.setPerson(person);
        personalBusiness.setGjhtqywlx(ywlx);
        personalBusiness.setCzbz(CommonFieldType.非冲账.getCode());
        personalBusiness.setJzrq(new Date());

        CCollectionPersonalBusinessDetailsExtension extension = new CCollectionPersonalBusinessDetailsExtension();
        extension.setCzmc(ywlx);
        extension.setStep("办结");
        extension.setBjsj(new Date());
        CCollectionUnitBusinessDetailsExtension unitBusExtension = inventoryDetail.getInventory().getDwywmx().getExtension();
        extension.setYwwd(unitBusExtension.getYwwd());
        extension.setJbrxm(unitBusExtension.getJbrxm());
        extension.setJbrzjlx(unitBusExtension.getJbrzjlx());
        extension.setJbrzjhm(unitBusExtension.getJbrzjhm());

        BigDecimal dwyjce = inventoryDetail.getDwyjce();
        BigDecimal gryjce = inventoryDetail.getGryjce();
        String qcny = inventoryDetail.getInventory().getQcny();
        BigDecimal grzhye = person.getCollectionPersonalAccount().getGrzhye();

        extension.setDwfse(dwyjce);
        extension.setGrfse(gryjce);
        extension.setDqye(grzhye);
        extension.setFsny(qcny);

        personalBusiness.setExtension(extension);
        personalBusinessDAO.saveNormal(personalBusiness);
    }

    /**
     * 委托扣款，需签约，从单位的发薪账号上划扣
     * 这里读取业务信息发送单位扣款消息
     * 暂时只做同行
     */
    // TODO 需进行整理，且使用统一的方式管理与结算平台的交互
    public void sendRemittance(String ywlsh, String code) {
        //1、得到业务数据
        CCollectionUnitRemittanceVice remittance = remittanceViceDAO.getByYwlsh(ywlsh);
        StCollectionUnitBusinessDetails dwywmx = remittance.getDwywmx();
        //String fxzhkhyh = dwywmx.getUnit().getCollectionUnitAccount().getExtension().getFxzhkhyh();  //根据行别判断
        String dwzh = remittance.getDwywmx().getDwzh();
        StCommonUnit unit = unitDAO.getUnit(dwzh);
        HashMap map = new HashMap();
        map.put("ywlsh", dwywmx.getYwlsh());
        map.put("dwzh", dwywmx.getDwzh());
        map.put("fse", remittance.getFse());
        map.put("yhhb", unit.getStyhdm());
        map.put("dwmc", unit.getDwmc());
        map.put("fxzh", unit.getCollectionUnitAccount().getExtension().getFxzh());
        trader.sendRemittanceMSg(map, code);

    }

    /**
     * 由清册业务发起，直接生成汇缴记录(新建状态)
     * ywlsh：清册业务的业务流水号
     */
    @Override
    public void saveRemittance(String ywlsh) {
        //直接生成汇缴业务，列表上查询出新建的汇缴，点击直接进入
        //得到该月的清册信息
        CCollectionUnitDepositInventoryVice unitInventoryMsg = unitDepositInventoryViceDAO.getByYwlsh(ywlsh);
        if (null == unitInventoryMsg) {
            throw new ErrorException("单位该月清册不存在");
        }
        String dwzh = unitInventoryMsg.getDwywmx().getDwzh();
        StCommonUnit unit = unitDAO.getUnit(dwzh);

        String hbjny = unitInventoryMsg.getQcny();
        CCollectionUnitRemittanceVice result = new CCollectionUnitRemittanceVice();
        result.setQcqrdh(unitInventoryMsg.getQcqrdh());
        result.setHbjny(hbjny);
        //TODO
        result.setStyhdm(unit.getStyhdm());
        result.setStyhmc(unit.getStyhmc());
        StSettlementSpecialBankAccount bankAcount = BusUtils.getBankAcount(unit.getStyhdm(), "01");
        result.setStyhzh(bankAcount.getYhzhhm());
        result.setFse(unitInventoryMsg.getQcfse());

        StCollectionUnitBusinessDetails dwywmx = new StCollectionUnitBusinessDetails();
        dwywmx.setFsrs(unitInventoryMsg.getDwywmx().getFsrs());
        dwywmx.setDwzh(dwzh);
        dwywmx.setFse(BigDecimal.ZERO);
        dwywmx.setHbjny(hbjny);
        dwywmx.setYwmxlx(ywlx);     // 01表示汇缴
        dwywmx.setUnit(unit);
        result.setDwywmx(dwywmx);
        CCollectionUnitBusinessDetailsExtension extension = new CCollectionUnitBusinessDetailsExtension();
        extension.setStep("初始状态");
        extension.setCzmc(ywlx);    //表示汇缴
        extension.setYwwd(unitInventoryMsg.getDwywmx().getExtension().getYwwd());
        extension.setJbrzjlx(unit.getJbrzjlx());
        extension.setJbrxm(unit.getJbrxm());
        extension.setJbrzjhm(unit.getJbrzjhm());
        extension.setCzy(unitInventoryMsg.getDwywmx().getExtension().getCzy());
        extension.setSlsj(new Date());
        extension.setBlzl(unitInventoryMsg.getDwywmx().getExtension().getBlzl());
        dwywmx.setExtension(extension);

        Set<CCollectionUnitRemittanceDetailVice> hjxq = new HashSet<CCollectionUnitRemittanceDetailVice>();
        Set<CCollectionUnitDepositInventoryDetailVice> qcxxSet = unitInventoryMsg.getQcxq();
        //汇缴详情这张表暂时不太需要
        for (CCollectionUnitDepositInventoryDetailVice qcxxdetail : qcxxSet) {
            CCollectionUnitRemittanceDetailVice detail = new CCollectionUnitRemittanceDetailVice();
            detail.setGrzh(qcxxdetail.getGrzh());
            detail.setQcqrdh(unitInventoryMsg.getQcqrdh());
            hjxq.add(detail);
        }
        result.setHjxq(hjxq);
        remittanceViceDAO.save(result);

        String ywlshNew = dwywmx.getYwlsh();
        //流程调用
        WorkCondition condition = new WorkCondition();
        condition.setYwlsh(ywlshNew);
        condition.setCzy(unitInventoryMsg.getDwywmx().getExtension().getCzy());
        condition.setYwwd(unitInventoryMsg.getDwywmx().getExtension().getYwwd().getId());
        condition.setStatus("初始状态");
        condition.setEvent("保存");
        condition.setZtlx("1");
        condition.setType(BusinessType.Collection);
        condition.setSubType(busType);    //汇缴
        doWork(condition);
    }

    /**
     * 汇缴变动通知处理
     * 增加异常处理
     */
    private void getPaymentNotice(String ywlsh) {

        CCollectionUnitRemittanceVice remittance = remittanceViceDAO.getByYwlsh(ywlsh);

        CCollectionUnitBusinessDetailsExtension extension = remittance.getDwywmx().getExtension();
        extension.setStep("已入账分摊");

        remittanceViceDAO.update(remittance);

        doFinal(ywlsh);
    }


    /**
     * 缴存通知单
     */
    @Override
    public CommonResponses headUnitRemittanceNotice(String ywlsh) {

        UnitRemittanceNotice result = new UnitRemittanceNotice();
        CCollectionUnitRemittanceVice remittance = remittanceViceDAO.getByYwlsh(ywlsh);
        if (remittance == null) {
            throw new ErrorException("当前业务流水号:" + ywlsh + ",对应的汇缴业务不存在！");
        }
        String qcqrdh = remittance.getQcqrdh();
        CCollectionUnitDepositInventoryVice inventory = inventoryViceDAO.getByYwlsh(qcqrdh);
        CCollectionUnitDepositInventoryBatchVice qcpc = inventory.getQcpc();
        if (ComUtils.isEmpty(qcpc)) {
            throw new ErrorException("老数据不能打印汇缴通知单！");
        }

        result.setYWLSH(qcpc.getYwpch());
        result.setFSE(ComUtils.moneyFormat(qcpc.getQczfse()));
        result.setZJE(ComUtils.toMoneyUpper(qcpc.getQczfse().toString()));

        StCollectionUnitBusinessDetails dwywmx = remittance.getDwywmx();
        StCommonUnit unit = dwywmx.getUnit();
        CCollectionUnitBusinessDetailsExtension extension = dwywmx.getExtension();
        result.setYWWD(extension.getYwwd().getMingCheng());
        result.setTZRQ(ComUtils.parseToString(new Date(), "yyyy-MM-dd HH:mm"));
        result.setDWMC(unit.getDwmc());
        result.setDWZH(dwywmx.getDwzh());

        String hbjny = qcpc.getQcnyq() + "至" + qcpc.getQcnyz();
        result.setHBJNY(hbjny);
        if (remittance.getHjfs() != null) {
            SingleDictionaryDetail hjfs = iDictionaryService.getSingleDetail(remittance.getHjfs(), "RemitPayment");
            result.setHJFS(hjfs != null ? hjfs.getName() : "");
        }
        result.setFSRS(dwywmx.getFsrs().toString());

        StSettlementSpecialBankAccount bankAcount = BusUtils.getBankAcount(unit.getStyhdm(), "01");
        result.setSKZHMC(bankAcount.getYhzhmc());
        result.setSKZH(bankAcount.getYhzhhm());
        result.setSTYHMC(unit.getStyhmc());
        result.setSTYHDM(unit.getStyhdm());

        result.setBJSJ(DateUtil.date2Str(dwywmx.getExtension().getBjsj()));
        result.setCZY(dwywmx.getExtension().getCzy());
        result.setDWJBR(extension.getJbrxm());

        Date jzsj = ComUtils.getNextDate(remittance.getCreated_at(), 5);    //5天内
        result.setJZSJ(ComUtils.parseToString(jzsj, "yyyy-MM-dd"));
        String id = pdfService.getUnitRemittanceNoticePdf(result);
        System.out.println("生成id的值：" + id);
        CommonResponses commonResponses = new CommonResponses();
        commonResponses.setId(id);
        commonResponses.setState("success");
        return commonResponses;
    }

    @Override
    public CommonResponses getConsecutiveDepositMonths(String grzh) {
        CommonResponses commonResponses = new CommonResponses();
        int consecutiveDepositMonths = BusUtils.getConsecutiveDepositMonths(grzh);
        commonResponses.setId(Integer.toString(consecutiveDepositMonths));
        return commonResponses;
    }

    @Override
    public void saveRemittance3(TokenContext tokenContext, String ywpch) {
        CCollectionUnitDepositInventoryBatchVice inventoryBatch = inventoryViceDAO.getBatchInventory(ywpch);
        String dwzh = inventoryBatch.getDwzh();
        StCommonUnit unit = unitDAO.getUnit(dwzh);
        String jkfs = inventoryBatch.getJkfs(); //01转账 02暂收
        if ("02".equals(jkfs)) {
            //1、验证未分摊余额
            checkMoney(unit, inventoryBatch);

            Set<CCollectionUnitDepositInventoryVice> qclb = inventoryBatch.getQclb();
            List<CCollectionUnitDepositInventoryVice> sortQclb = new ArrayList<>();
            sortQclb.addAll(qclb);
            Collections.sort(sortQclb);
            //依次处理每个月的汇缴业务
            for (CCollectionUnitDepositInventoryVice inventory : sortQclb) {
                //2、保存汇缴业务（办结），再进行办结处理
                String ywlsh = inventory.getDwywmx().getYwlsh();
                String hjYwlsh = saveRemittance4(ywlsh, jkfs, tokenContext);
                //流程调用
                doBus(tokenContext, hjYwlsh, jkfs);

                doFinal(hjYwlsh);

            }
            createVoucher(ywpch);
        } else {
            //转账收款的方式,使用清册的ywpch作为缴款单的信息

            //1、保存各个月的缴款信息，待入账状态
            Set<CCollectionUnitDepositInventoryVice> qclb = inventoryBatch.getQclb();
            List<CCollectionUnitDepositInventoryVice> sortQclb = new ArrayList<>();
            sortQclb.addAll(qclb);
            Collections.sort(sortQclb);
            //依次处理每个月的汇缴业务
            for (CCollectionUnitDepositInventoryVice inventory : sortQclb) {
                //2、保存汇缴业务（办结），再进行办结处理
                String ywlsh = inventory.getDwywmx().getYwlsh();
                String hjYwlsh = saveRemittance4(ywlsh, jkfs, tokenContext);
                doBus(tokenContext, hjYwlsh, jkfs);
            }
            //2、调用结算平台接口
            sendRemittance2(ywpch); //转账收款
        }

    }

    private void checkMoney(StCommonUnit unit, CCollectionUnitDepositInventoryBatchVice inventoryBatch) {
        BigDecimal zsye = unit.getCollectionUnitAccount().getExtension().getZsye();
        BigDecimal qczfse = inventoryBatch.getQczfse();
        if (zsye.compareTo(qczfse) < 0) {
            throw new ErrorException("未分摊余额不足！");
        }
    }

    private void doBus(TokenContext tokenContext, String ywlsh, String jkfs) {
        CCollectionUnitRemittanceVice remittance = remittanceViceDAO.getByYwlsh(ywlsh);
        CCollectionUnitBusinessDetailsExtension extension = remittance.getDwywmx().getExtension();

        BusUtils.saveAuditHistory(ywlsh, "汇缴", "新建");

        if ("01".equals(jkfs)) {
            extension.setStep(CollectionBusinessStatus.待入账.getName());
            remittanceViceDAO.save(remittance);
        } else {
            extension.setStep("已入账分摊");
            remittanceViceDAO.save(remittance);
        }

    }

    /**
     * ywpch为清册的批量批次
     * <p>
     * 增加入账失败的异常处理
     */
    @Override
    public void getBatchPaymentNotice(String ywpch) {
        CCollectionUnitDepositInventoryBatchVice inventoryBatch = inventoryViceDAO.getBatchInventory(ywpch);

        AssertUtils.notEmpty(inventoryBatch, "当前不存在汇缴清册批次：" + ywpch);

        Set<CCollectionUnitDepositInventoryVice> qclb = inventoryBatch.getQclb();

        List<CCollectionUnitDepositInventoryVice> sortList = new ArrayList<>();
        sortList.addAll(qclb);
        Collections.sort(sortList);

        //依次更新该批次下面的的信息
        for (CCollectionUnitDepositInventoryVice inventory : sortList) {
            String qcqrdh = inventory.getQcqrdh();
            CCollectionUnitRemittanceVice remittance = remittanceViceDAO.getByQcqrdh(qcqrdh);
            String ywlsh = remittance.getDwywmx().getYwlsh();
            getPaymentNotice(ywlsh);
        }
    }

    @Override
    public BusCommonRetrun accountRecord(String ywlsh, String code) {
        BusCommonRetrun busCommonRetrun = new BusCommonRetrun();
        if ("0".equals(code)) {
            CCollectionUnitRemittanceVice remittance = remittanceViceDAO.getByYwlsh(ywlsh);
            CCollectionUnitBusinessDetailsExtension extension = remittance.getDwywmx().getExtension();
            String step = extension.getStep();
            if (CollectionBusinessStatus.入账失败.getName().equals(step)) {
                //获取关联的清册批次
                CCollectionUnitDepositInventoryVice inventory = inventoryViceDAO.getByYwlsh(remittance.getQcqrdh());
                CCollectionUnitDepositInventoryBatchVice qcpc = inventory.getQcpc();
                String ywpch = qcpc.getYwpch();

                //getBatchPaymentNotice(ywpch);

                busCommonRetrun.setMessage("入账成功");
                busCommonRetrun.setStatus("01");
                busCommonRetrun.setYWLSH(ywlsh);
            }
        } else {
            busCommonRetrun.setMessage("暂不支持");
            busCommonRetrun.setStatus("02");
            busCommonRetrun.setYWLSH(ywlsh);
        }
        return busCommonRetrun;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void doFail(String ywpch, String sbyy) {
        CCollectionUnitDepositInventoryBatchVice inventoryBatch = inventoryViceDAO.getBatchInventory(ywpch);

        AssertUtils.notEmpty(inventoryBatch, "当前不存在汇缴清册批次：" + ywpch);
        Set<CCollectionUnitDepositInventoryVice> qclb = inventoryBatch.getQclb();
        List<CCollectionUnitDepositInventoryVice> sortList = new ArrayList<>();
        sortList.addAll(qclb);
        Collections.sort(sortList);

        //依次更新该批次下面的的信息
        for (CCollectionUnitDepositInventoryVice inventory : sortList) {
            String qcqrdh = inventory.getQcqrdh();
            CCollectionUnitRemittanceVice remittance = remittanceViceDAO.getByQcqrdh(qcqrdh);
            CCollectionUnitBusinessDetailsExtension extension = remittance.getDwywmx().getExtension();
            String step = extension.getStep();
            if (CollectionBusinessStatus.待入账.getName().equals(step)) {
                extension.setBeizhu(sbyy);
                extension.setStep(CollectionBusinessStatus.入账失败.getName());
                remittanceViceDAO.update(remittance);
            }
        }
    }

    private void createVoucher(String ywpch) {
        CCollectionUnitDepositInventoryBatchVice inventoryBatch = inventoryViceDAO.getBatchInventory(ywpch);
        String dwzh = inventoryBatch.getDwzh();
        BigDecimal qczfse = inventoryBatch.getQczfse();
        StCommonUnit unit = unitDAO.getUnit(dwzh);
        String jkfs = inventoryBatch.getJkfs();
        String code = null;
        if ("01".equals(jkfs)) {
            code = VoucherBusinessType.汇补缴.getCode();
        } else {
            code = VoucherBusinessType.汇补缴手动分摊.getCode();
        }
        Set<CCollectionUnitDepositInventoryVice> qclb = inventoryBatch.getQclb();

        ArrayList<CCollectionUnitDepositInventoryVice> list = new ArrayList<>(qclb);
        CCollectionUnitDepositInventoryVice inventoryVice = list.get(0);
        CCollectionUnitBusinessDetailsExtension extension1 = inventoryVice.getDwywmx().getExtension();
        String czy = extension1.getCzy();
        String ywwd = extension1.getYwwd().getMingCheng();

        //财务入账
        String zaiYao = unit.getDwmc() + inventoryBatch.getQcnyq() + "至" + inventoryBatch.getQcnyz() + "汇缴";
        VoucherRes voucherRes = voucherManagerService.addVoucher("毕节市住房公积金管理中心", czy, czy, "", "制单",
                code, code, ywpch, zaiYao, qczfse, null, null);
        if (ComUtils.isEmpty(voucherRes.getJZPZH())) {
            throw new ErrorException(voucherRes.getMSG());
        }
        if ("02".equals(jkfs)) {
            CFinanceRecordUnit cFinanceRecordUnit = new CFinanceRecordUnit();
            cFinanceRecordUnit.setFse(qczfse.negate());
            cFinanceRecordUnit.setJzpzh(voucherRes.getJZPZH());
            cFinanceRecordUnit.setRemark("未分摊汇缴");
            cFinanceRecordUnit.setSummary(zaiYao);
            cFinanceRecordUnit.setZjly(WFTLY.未分摊汇补缴.getName());
            cFinanceRecordUnit.setDwzh(dwzh);
            cFinanceRecordUnit.setWftye(unit.getCollectionUnitAccount().getExtension().getZsye());
            icFinanceRecordUnitDAO.save(cFinanceRecordUnit);
        }
        //入账后数据更新
        refleshVoucher(inventoryBatch, voucherRes.getJZPZH());
    }

    public void sendRemittance2(String ywpch) {
        //1、得到业务数据
        CCollectionUnitDepositInventoryBatchVice inventoryBatch = inventoryViceDAO.getBatchInventory(ywpch);
        String dwzh = inventoryBatch.getDwzh();
        StCommonUnit unit = unitDAO.getUnit(dwzh);
        HashMap map = new HashMap();
        map.put("ywlsh", ywpch);
        map.put("dwzh", dwzh);
        map.put("fse", inventoryBatch.getQczfse());
        map.put("yhhb", unit.getStyhdm());
        map.put("dwmc", unit.getDwmc());
        map.put("fxzh", unit.getCollectionUnitAccount().getExtension().getFxzh());
        trader.sendRemittanceMSg(map, "01");

    }

    private String saveRemittance4(String ywlsh, String jkfs, TokenContext tokenContext) {

        CCollectionUnitDepositInventoryVice unitInventoryMsg = unitDepositInventoryViceDAO.getByYwlsh(ywlsh);
        if (null == unitInventoryMsg) {
            throw new ErrorException("单位该月清册不存在");
        }
        String dwzh = unitInventoryMsg.getDwywmx().getDwzh();
        StCommonUnit unit = unitDAO.getUnit(dwzh);

        String hbjny = unitInventoryMsg.getQcny();
        CCollectionUnitRemittanceVice result = new CCollectionUnitRemittanceVice();
        result.setQcqrdh(unitInventoryMsg.getQcqrdh());
        result.setHbjny(hbjny);
        result.setHjfs(jkfs);

        result.setStyhdm(unit.getStyhdm());
        result.setStyhmc(unit.getStyhmc());
        StSettlementSpecialBankAccount bankAcount = BusUtils.getBankAcount(unit.getStyhdm(), "01");
        result.setStyhzh(bankAcount.getYhzhhm());
        result.setFse(unitInventoryMsg.getQcfse());

        StCollectionUnitBusinessDetails dwywmx = new StCollectionUnitBusinessDetails();
        dwywmx.setFsrs(unitInventoryMsg.getDwywmx().getFsrs());
        dwywmx.setDwzh(dwzh);
        dwywmx.setFse(BigDecimal.ZERO);
        dwywmx.setHbjny(hbjny);
        dwywmx.setYwmxlx(ywlx);     // 01表示汇缴
        dwywmx.setUnit(unit);
        result.setDwywmx(dwywmx);

        CCollectionUnitBusinessDetailsExtension extension = new CCollectionUnitBusinessDetailsExtension();
        extension.setCzmc(ywlx);    //表示汇缴
        extension.setYwwd(unitInventoryMsg.getDwywmx().getExtension().getYwwd());
        extension.setJbrzjlx(unit.getJbrzjlx());
        extension.setJbrxm(unit.getJbrxm());
        extension.setJbrzjhm(unit.getJbrzjhm());
        extension.setCzy(unitInventoryMsg.getDwywmx().getExtension().getCzy());
        extension.setSlsj(new Date());
        extension.setBlzl(unitInventoryMsg.getDwywmx().getExtension().getBlzl());
        extension.setBeizhu(ComUtils.roleListToString(tokenContext));
        extension.setStep("新建");

        dwywmx.setExtension(extension);
        dwywmx.setUnitRemittanceVice(result);

        remittanceViceDAO.save(result);

        return result.getDwywmx().getYwlsh();

    }

    @Override
    public CommonResponses revokeRemittance(String ywlsh) {
        CCollectionUnitRemittanceVice remittance = remittanceViceDAO.getByYwlsh(ywlsh);
        String qcqrdh = remittance.getQcqrdh();
        CCollectionUnitDepositInventoryVice aInventory = inventoryViceDAO.getByYwlsh(qcqrdh);
        CCollectionUnitDepositInventoryBatchVice qcpc = aInventory.getQcpc();
        Set<CCollectionUnitDepositInventoryVice> qclb = qcpc.getQclb();
        for (CCollectionUnitDepositInventoryVice inventory : qclb) {
            //清册部分
            StCollectionUnitBusinessDetails dwywmx = inventory.getDwywmx();
            Set<CCollectionUnitDepositInventoryDetailVice> qcxq = inventory.getQcxq();
            for (CCollectionUnitDepositInventoryDetailVice inventoryDetail : qcxq) {
                inventoryViceDAO.delete(inventoryDetail);
            }
            inventoryViceDAO.delete(inventory);
            inventoryViceDAO.delete(dwywmx);
            //汇缴部分
            String qcqrdh2 = inventory.getQcqrdh();
            CCollectionUnitRemittanceVice remittance2 = remittanceViceDAO.getByQcqrdh(qcqrdh2);
            StCollectionUnitBusinessDetails dwywmx2 = remittance2.getDwywmx();
            inventoryViceDAO.delete(remittance2);
            inventoryViceDAO.delete(dwywmx2);
        }
        CommonResponses commonResponses = new CommonResponses();
        commonResponses.setId(ywlsh);
        commonResponses.setState("success");
        return commonResponses;
    }

    @Override
    public void remittanceAgain(String dwzh, String jkfs) {
        //1、查询出清册的批次信息，最近一笔
        CCollectionUnitDepositInventoryVice nearLyInventory = inventoryViceDAO.getNearLyInventory(dwzh);
        CCollectionUnitDepositInventoryBatchVice qcpc = nearLyInventory.getQcpc();

        //检查汇缴业务，不能是已入账分摊办结
        checkIsFinal(qcpc);

        //2、根据缴款方式，2种处理方式
        if ("01".equals(jkfs)) {
            //如果依然是转账，那么只改变状态
            stateChangeTo(qcpc, CollectionBusinessStatus.待入账.getName());
        } else {
            //从暂收进行缴款
            StCommonUnit unit = unitDAO.getUnit(dwzh);
            checkMoney(unit, qcpc);

            //从暂收进行汇缴,修改缴款的 缴存方式
            upateBatchInventory(qcpc);
            String ywpch = qcpc.getYwpch();
            getBatchPaymentNotice(ywpch);
            //暂收入账
            createVoucher(ywpch);

        }

    }

    /**
     * 汇缴入账接口
     * 1、到账通知是否匹配
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    @Override
    public void remittancePaymentNotice(String ywlsh, AccChangeNotice accChangeNotice, String id) {
        CCollectionUnitDepositInventoryBatchVice batchInventory = inventoryViceDAO.getBatchInventory(ywlsh);
        BigDecimal amt = accChangeNotice.getAccChangeNoticeFile().getAmt();
        //转账的金额，3种情况：1、小于时入暂收，入账失败 2、相等正常匹配 3、大于时先入暂收，再从暂收中缴款
        int flag = compare(batchInventory, amt);

        if (flag < 0) {
            //入暂收
            if (flag == -1) {
                this.doFail(ywlsh, "到账金额不足");
            }
            AccChangeNoticeFile accChangeNoticeFile = accChangeNotice.getAccChangeNoticeFile();
            accChangeNoticeFile.setSummary(batchInventory.getDwzh());
            accChangeNoticeFile.setRemark("到账金额不足");

            accChangeNotice.getAccChangeNoticeFile().setNo(id); //修改业务流水号为id
            voucherAutoService.checkBusiness(accChangeNotice);
        } else if (flag == 0) {
            //转账入账
            getBatchPaymentNotice(ywlsh);
            createVoucher(ywlsh, accChangeNotice);
        } else {
            //先入暂收账
            AccChangeNoticeFile accChangeNoticeFile = accChangeNotice.getAccChangeNoticeFile();
            accChangeNoticeFile.setSummary(batchInventory.getDwzh());
            accChangeNoticeFile.setRemark("到账金额大于汇缴总额");
            accChangeNotice.getAccChangeNoticeFile().setNo(id); //修改业务流水号为id
            voucherAutoService.checkBusiness(accChangeNotice);

            //从暂收进行汇缴,修改缴款的 缴存方式
            upateBatchInventory(batchInventory);
            getBatchPaymentNotice(ywlsh);

            //暂收分摊入账
            createVoucher(ywlsh);
        }

    }

    @Override
    public PageResNew<ListUnitRemittanceResRes> getUnitRemittanceList(TokenContext tokenContext, String dwmc, String dwzh, String ywzt, String czy, String yhmc, String YWWD, String kssj, String jssj, String ywpch, String marker, String pageSize, String action) {
        ListAction listAction = ListAction.pageType(action);
        //参数验证
        int pagesize = ComUtils.parstPageSize(pageSize);

        Date startDate = ComUtils.parseToDate(kssj, "yyyy-MM-dd HH:mm");
        Date endDate = ComUtils.parseToDate(jssj, "yyyy-MM-dd HH:mm");

        IBaseDAO.CriteriaExtension ce = getCriteriaExtension(dwmc, dwzh, ywzt, czy, yhmc, YWWD);

        PageResults<CCollectionUnitRemittanceVice> pages = null;
        try {
            pages = remittanceViceDAO.listWithMarker(null, startDate, endDate, "created_at", Order.DESC, null, SearchOption.REFINED, marker, pagesize, listAction, ce);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorException(e);
        }
        PageResNew<ListUnitRemittanceResRes> result = new PageResNew<>();
        ArrayList<ListUnitRemittanceResRes> list = getResultList(pages.getResults());
        result.setResults(action, list);

        return result;
    }

    private IBaseDAO.CriteriaExtension getCriteriaExtension(String dwmc, String dwzh, String ywzt, String czy, String yhmc, String ywwd) {
        return new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                criteria.createAlias("dwywmx", "dwywmx");
                criteria.createAlias("dwywmx.unit", "unit");
                criteria.createAlias("dwywmx.cCollectionUnitBusinessDetailsExtension", "cCollectionUnitBusinessDetailsExtension");
                if (!ComUtils.isEmpty(dwmc)) {
                    criteria.add(Restrictions.like("unit.dwmc", "%" + dwmc + "%"));
                }
                if (!ComUtils.isEmpty(dwzh)) {
                    criteria.add(Restrictions.like("dwywmx.dwzh", "%" + dwzh + "%"));
                }
                if (!ComUtils.isEmpty(czy)) {
                    criteria.add(Restrictions.like("cCollectionUnitBusinessDetailsExtension.czy", "%" + czy + "%"));
                }
                if (!ComUtils.isEmpty(yhmc)) {
                    criteria.add(Restrictions.eq("unit.styhmc", yhmc));
                }
                if (!ComUtils.isEmpty(ywwd)) {
                    criteria.createAlias("cCollectionUnitBusinessDetailsExtension.ywwd", "ywwd");
                    criteria.add(Restrictions.eq("ywwd.id", ywwd));
                }
                if (!ComUtils.isEmpty(ywzt) && !CollectionBusinessStatus.所有.getName().equals(ywzt)) {
                    criteria.add(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.step", ywzt));
                }
            }
        };

    }

    private ArrayList<ListUnitRemittanceResRes> getResultList(List<CCollectionUnitRemittanceVice> results) {
        ArrayList<ListUnitRemittanceResRes> resultList = new ArrayList<>();
        for (CCollectionUnitRemittanceVice rowSource : results) {
            ListUnitRemittanceResRes rowView = getRowView(rowSource);
            resultList.add(rowView);
        }
        return resultList;
    }

    /**
     * 1、首先比较到账通知金额与清册发生额
     * 2、查看汇缴业务是否已办结，如果已办结，入暂收款
     */
    private int compare(CCollectionUnitDepositInventoryBatchVice batchInventory, BigDecimal amt) {
        BigDecimal qczfse = batchInventory.getQczfse();
        int flag = amt.compareTo(qczfse);
        Set<CCollectionUnitDepositInventoryVice> qclb = batchInventory.getQclb();
        ArrayList<CCollectionUnitDepositInventoryVice> list = new ArrayList<>(qclb);
        CCollectionUnitDepositInventoryVice inventory = list.get(0);
        CCollectionUnitRemittanceVice remittance = remittanceViceDAO.getByQcqrdh(inventory.getQcqrdh());
        CCollectionUnitBusinessDetailsExtension extension = remittance.getDwywmx().getExtension();
        String step = extension.getStep();
        if ("已入账分摊".equals(step)) {
            flag = -2;  //区分已办结的情况
        }
        return flag;
    }

    private void createVoucher(String ywpch, AccChangeNotice accChangeNotice) {
        CCollectionUnitDepositInventoryBatchVice inventoryBatch = inventoryViceDAO.getBatchInventory(ywpch);
        String dwzh = inventoryBatch.getDwzh();
        StCommonUnit unit = unitDAO.getUnit(dwzh);
        String code = VoucherBusinessType.汇补缴.getCode();

        Set<CCollectionUnitDepositInventoryVice> qclb = inventoryBatch.getQclb();

        ArrayList<CCollectionUnitDepositInventoryVice> list = new ArrayList<>(qclb);
        CCollectionUnitDepositInventoryVice inventoryVice = list.get(0);
        CCollectionUnitBusinessDetailsExtension extension1 = inventoryVice.getDwywmx().getExtension();
        String czy = extension1.getCzy();
        String ywwd = extension1.getYwwd().getMingCheng();

        //财务入账
        String zaiYao = unit.getDwmc() + inventoryBatch.getQcnyq() + "至" + inventoryBatch.getQcnyz() + "汇缴";
        accChangeNotice.getAccChangeNoticeFile().setSummary(zaiYao);

        VoucherRes voucherRes = voucherManagerService.addVoucher(ywwd, czy, czy, "", "制单",
                code, code, accChangeNotice, null);
        if (ComUtils.isEmpty(voucherRes.getJZPZH())) {
            throw new ErrorException(voucherRes.getMSG());
        }
        //入账后数据更新
        refleshVoucher(inventoryBatch, voucherRes.getJZPZH());
    }

    private void refleshVoucher(CCollectionUnitDepositInventoryBatchVice inventoryBatch, String jzpzh) {
        Set<CCollectionUnitDepositInventoryVice> qclb = inventoryBatch.getQclb();
        for (CCollectionUnitDepositInventoryVice inventory : qclb) {
            String qcqrdh = inventory.getQcqrdh();
            CCollectionUnitRemittanceVice remittance = remittanceViceDAO.getByQcqrdh(qcqrdh);
            StCollectionUnitBusinessDetails dwywmx = remittance.getDwywmx();
            dwywmx.setJzrq(new Date());
            CCollectionUnitBusinessDetailsExtension extension = dwywmx.getExtension();
            extension.setJzpzh(jzpzh);
            remittanceViceDAO.update(remittance);
        }
    }

    private void upateBatchInventory(CCollectionUnitDepositInventoryBatchVice batchInventory) {
        batchInventory.setJkfs("02");
        Set<CCollectionUnitDepositInventoryVice> qclb = batchInventory.getQclb();
        for (CCollectionUnitDepositInventoryVice inventory : qclb) {
            String qcqrdh = inventory.getQcqrdh();
            CCollectionUnitRemittanceVice remittance = remittanceViceDAO.getByQcqrdh(qcqrdh);
            remittance.setHjfs("02");
            remittanceViceDAO.update(remittance);
        }
        inventoryViceDAO.update(batchInventory);
    }

    //将状态改为待入账
    private void stateChangeTo(CCollectionUnitDepositInventoryBatchVice qcpc, String step) {
        Set<CCollectionUnitDepositInventoryVice> qclb = qcpc.getQclb();
        for (CCollectionUnitDepositInventoryVice inventory : qclb) {
            String qcqrdh = inventory.getQcqrdh();
            CCollectionUnitRemittanceVice remittance = remittanceViceDAO.getByQcqrdh(qcqrdh);
            CCollectionUnitBusinessDetailsExtension extension = remittance.getDwywmx().getExtension();
            extension.setStep(step);
            remittanceViceDAO.save(remittance);
        }
    }

    private void checkIsFinal(CCollectionUnitDepositInventoryBatchVice qcpc) {

        Set<CCollectionUnitDepositInventoryVice> qclb = qcpc.getQclb();
        for (CCollectionUnitDepositInventoryVice inventory : qclb) {
            String qcqrdh = inventory.getQcqrdh();
            CCollectionUnitRemittanceVice remittance = remittanceViceDAO.getByQcqrdh(qcqrdh);
            CCollectionUnitBusinessDetailsExtension extension = remittance.getDwywmx().getExtension();
            if ("已入账分摊".equals(extension.getStep())) {
                throw new ErrorException("当前关联的汇缴业务已入账分摊！");
            }
        }
    }

    @Override
    public void doBeforehandSealCheck(String ywlsh) {
        CCollectionUnitRemittanceVice dwhj = remittanceViceDAO.getByYwlsh(ywlsh);
        StCollectionUnitBusinessDetails dwywmx = dwhj.getDwywmx();
        doBeforehandSealCheck(dwhj, dwywmx.getUnit().getCollectionUnitAccount());
    }

    /**
     * 针对预封存的入账问题进行bug修改
     * 预先封存后，转移到其他单位，而老单位的缴存会有部分实际进了新单位
     * 这种情况，使用预封存资金转移记账
     * 当前作用：月份
     */
    public void doBeforehandSealCheck(CCollectionUnitRemittanceVice dwhj, StCollectionUnitAccount unitAccount) {
        //1、查找单位清册中是否有含有已转移的人员，即个人关联的单位不是当前单位，找出这样的列表 list
        String dwzh = dwhj.getDwywmx().getDwzh();

        CCollectionUnitDepositInventoryVice inventory = inventoryViceDAO.getByYwlsh(dwhj.getQcqrdh());

        List<StCommonPerson> list = remittanceViceDAO.getListNoInCurrentUnitOfInventory(dwhj.getQcqrdh(), dwzh);
        if (list == null || list.size() == 0)
            return;

        //2、如果人员列表list不为空
        for (StCommonPerson person : list) {
            //对于每个人产生一笔预封存转移业务、然后入账
            createBeforehandSeal(person, inventory, unitAccount);
        }

    }

    /**
     * 对于每个人产生一笔预封存转移业务、然后入账
     */
    public void createBeforehandSeal(StCommonPerson person, CCollectionUnitDepositInventoryVice inventory, StCollectionUnitAccount unitAccount) {

        //1、找到该人当前的单位,更新：新单位及老单位的余额，钱从老单位转移到新单位
        CCollectionUnitDepositInventoryDetailVice inventoryDetail = serchInInventory(inventory, person);
        BigDecimal qcfse = inventoryDetail.getQcfse();
        if (qcfse.compareTo(BigDecimal.ZERO) > 0) {
            //当前单位
            BigDecimal dwzhye = unitAccount.getDwzhye();
            dwzhye = dwzhye.subtract(qcfse);
            unitAccount.setDwzhye(dwzhye);
            unitDAO.update(unitAccount);

            //新单位
            StCommonUnit unit2 = person.getUnit();
            StCollectionUnitAccount unitAccount2 = unit2.getCollectionUnitAccount();
            BigDecimal dwzhye2 = unitAccount2.getDwzhye();
            dwzhye2 = dwzhye2.add(qcfse);
            unitAccount2.setDwzhye(dwzhye2);
            unitDAO.update(unitAccount2);

            //记录业务

            //入账


        }

    }

    private CCollectionUnitDepositInventoryDetailVice serchInInventory(CCollectionUnitDepositInventoryVice inventory, StCommonPerson person) {
        String grzh = person.getGrzh();
        Set<CCollectionUnitDepositInventoryDetailVice> qcxq = inventory.getQcxq();
        for (CCollectionUnitDepositInventoryDetailVice detail : qcxq) {
            if (grzh.equals(detail.getGrzh())) {
                return detail;
            }
        }
        throw new ErrorException("数据异常！");
    }


}