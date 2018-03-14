package com.handge.housingfund.collection.service.unitdeposit;

import com.handge.housingfund.collection.service.common.BaseService;
import com.handge.housingfund.collection.utils.AssertUtils;
import com.handge.housingfund.collection.utils.BusUtils;
import com.handge.housingfund.collection.utils.ComUtils;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNotice;
import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNoticeFile;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessStatus;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.collection.enumeration.CommonFieldType;
import com.handge.housingfund.common.service.collection.model.BusCommonRetrun;
import com.handge.housingfund.common.service.collection.model.InventoryDetail;
import com.handge.housingfund.common.service.collection.model.InventoryMessage;
import com.handge.housingfund.common.service.collection.model.LatestInventory;
import com.handge.housingfund.common.service.collection.model.deposit.*;
import com.handge.housingfund.common.service.collection.service.common.WorkCondition;
import com.handge.housingfund.common.service.collection.service.trader.ICollectionTrader;
import com.handge.housingfund.common.service.collection.service.unitdeposit.UnitDepositInventory;
import com.handge.housingfund.common.service.collection.service.unitdeposit.UnitPayback;
import com.handge.housingfund.common.service.finance.IVoucherAutoService;
import com.handge.housingfund.common.service.finance.IVoucherManagerService;
import com.handge.housingfund.common.service.finance.model.VoucherRes;
import com.handge.housingfund.common.service.finance.model.enums.VoucherBusinessType;
import com.handge.housingfund.common.service.finance.model.enums.WFTLY;
import com.handge.housingfund.common.service.loan.model.CommonResponses;
import com.handge.housingfund.common.service.others.IDictionaryService;
import com.handge.housingfund.common.service.others.IPdfService;
import com.handge.housingfund.common.service.others.IUploadImagesService;
import com.handge.housingfund.common.service.others.enums.UploadFileBusinessType;
import com.handge.housingfund.common.service.others.enums.UploadFileModleType;
import com.handge.housingfund.common.service.others.model.SingleDictionaryDetail;
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
 * Created by 凡 on 2017/7/27.
 * 单位补缴
 * 办理条件：1、某月已汇缴 2、对未交款的员工启封或开户，且生效年月为该月
 */
@Service
public class UnitPaybackImpl extends BaseService implements UnitPayback {

    private static final String SKFS_ZZ = "转账收款";   //01
    private static final String SKFS_ZS = "暂收收款";   //02
    private static final String SKFS_WT = "委托收款";   //04

    private String ywlx = CollectionBusinessType.补缴.getCode();
    private String ywlxmc = CollectionBusinessType.补缴.getName();

    private String busType = BusinessSubType.归集_补缴记录.getSubType();

    @Autowired
    private ICCollectionUnitPaybackViceDAO paybackViceDAO;

    @Autowired
    private ICCollectionUnitDepositInventoryViceDAO inventoryViceDAO;

    @Autowired
    private ICCollectionUnitRemittanceViceDAO remittanceViceDAO;

    @Autowired
    private IStCommonPersonDAO personDAO;

    @Autowired
    private IStCommonUnitDAO unitDAO;

    @Autowired
    private ICCollectionIndividualAccountActionViceDAO collectionIndividualAccountActionViceDAO;

    @Autowired
    private ICCollectionPersonRadixViceDAO personRadixViceDAO;

    @Autowired
    private ICollectionTrader trader;

    @Autowired
    private IPdfService pdfService;

    @Autowired
    private IStCollectionPersonalBusinessDetailsDAO personalBusinessDAO;

    @Autowired
    private IVoucherManagerService voucherManagerService;

    @Autowired
    private IDictionaryService iDictionaryService;

    @Autowired
    IUploadImagesService iUploadImagesService;

    @Autowired
    private ICAccountNetworkDAO cAccountNetworkDAO;

    @Autowired
    private UnitDepositInventory inventory;

    @Autowired
    private IVoucherAutoService voucherAutoService;

    @Autowired
    private ICFinanceRecordUnitDAO icFinanceRecordUnitDAO;

    /**
     * 获取补缴记录列表
     */
    public PageRes<ListPayBackResRes> getUnitPayBackList(TokenContext tokenContext, String dwmc, String dwzh, String ywzt, String czy, String ywwd, String kssj, String jssj, String pageNumber, String pageSize) {
        //参数验证
        int pageno = ComUtils.parstPageNo(pageNumber);
        int pagesize = ComUtils.parstPageSize(pageSize);

        IBaseDAO.CriteriaExtension ce = getCriteriaExtension(dwmc, dwzh, ywzt, czy, ywwd);

        Date startDate = ComUtils.parseToDate(kssj, "yyyy-MM-dd HH:mm");
        Date endDate = ComUtils.parseToDate(jssj, "yyyy-MM-dd HH:mm");
        PageResults<CCollectionUnitPaybackVice> pageResults = paybackViceDAO.listWithPage(null, startDate, endDate, "created_at", Order.DESC, null, SearchOption.REFINED, pageno, pagesize, ce);
        List<CCollectionUnitPaybackVice> results = pageResults.getResults();
        ArrayList<ListPayBackResRes> resultList = getResultList(results);

        PageRes<ListPayBackResRes> pageres = new PageRes<>();
        pageres.setResults(resultList);
        pageres.setCurrentPage(pageResults.getCurrentPage());
        pageres.setNextPageNo(pageResults.getPageNo());
        pageres.setPageSize(pageResults.getPageSize());
        pageres.setPageCount(pageResults.getPageCount());
        pageres.setTotalCount(pageResults.getTotalCount());

        return pageres;
    }

    private BigDecimal getFse(CCollectionUnitPaybackVice payback) {
        StCollectionUnitBusinessDetails dwywmx = payback.getDwywmx();
        CCollectionUnitBusinessDetailsExtension extension = dwywmx.getExtension();
        if ("已入账分摊".equals(extension.getStep())) {
            return dwywmx.getFse();
        }
        return payback.getFse();
    }

    /**
     * 新增补缴申请
     */
    public AddUnitPayBackRes postUnitPayBack(TokenContext tokenContext, UnitPayBackPost unitPayBackPost) {
        // 验证
        paramCheck(unitPayBackPost);
        payBackCheck(unitPayBackPost);

        String ywlsh = unitPayBackPost.getYWLSH();
        CCollectionUnitPaybackVice payBack = paybackViceDAO.getByYwlsh(ywlsh);
        StCollectionUnitBusinessDetails dwywmx = payBack.getDwywmx();
        dwywmx.setCzbz(CommonFieldType.非冲账.getCode());

        CCollectionUnitBusinessDetailsExtension extension = dwywmx.getExtension();
        StCommonUnit unit = dwywmx.getUnit();
        if (!unit.getExtension().getKhwd().equals(tokenContext.getUserInfo().getYWWD())) {
            throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务不在当前网点受理范围内");
        }

        String bjfs = unitPayBackPost.getBJFS();

        //TODO 找到对应的月份的清册，保存
        payBack.setBjfs(bjfs);

        extension.setCzy(tokenContext.getUserInfo().getCZY());
        CAccountNetwork network = cAccountNetworkDAO.get(tokenContext.getUserInfo().getYWWD());
        extension.setYwwd(network);
        extension.setJbrzjhm(unit.getJbrzjhm());
        extension.setJbrzjlx(unit.getJbrzjlx());
        extension.setJbrxm(unit.getJbrxm());
        extension.setSlsj(new Date());
        extension.setBlzl(unitPayBackPost.getBLZL());
        dwywmx.setExtension(extension);
        dwywmx.setUnitPaybackVice(payBack);

        Set<CCollectionUnitPaybackDetailVice> bjmx = payBack.getBjmx();
        ArrayList<UnitPayBackPostBJXX> bjxxView = unitPayBackPost.getBJXX();
        for (UnitPayBackPostBJXX detailView : bjxxView) {
            for (CCollectionUnitPaybackDetailVice vice : bjmx) {
                if (vice.getGrzh().equals(detailView.getGRZH())) {
                    vice.setBjyy(detailView.getBJYY());
                }
            }
        }
        paybackViceDAO.update(payBack);
        //保存历史
        BusUtils.saveAuditHistory(ywlsh, tokenContext, ywlxmc, "新建");

        String event = null;
        if ("01".equals(bjfs)) {
            event = SKFS_ZZ;
        } else if ("02".equals(bjfs)) {
            event = SKFS_ZS;
        } else {
            throw new ErrorException("暂时只支持转账缴款、未分摊缴款！");
        }

        //流程调用
        WorkCondition condition = new WorkCondition();
        condition.setYwlsh(ywlsh);
        condition.setCzy(tokenContext.getUserInfo().getCZY());
        condition.setYwwd(tokenContext.getUserInfo().getYWWD());
        condition.setStatus("待确认");
        condition.setEvent(event);
        condition.setZtlx("1");
        condition.setType(BusinessType.Collection);
        condition.setSubType(busType);    //补缴)
        condition.setRole(tokenContext.getRoleList().get(0));
        condition.setRoleList(tokenContext.getRoleList());
        doWork(condition);

        //发送数据到结算平台
        if ("01|04".indexOf(bjfs) >= 0) {
            HashMap map = new HashMap();
            map.put("ywlsh", dwywmx.getYwlsh());
            map.put("dwzh", dwywmx.getDwzh());
            map.put("fse", payBack.getFse());
            map.put("yhhb", unit.getStyhdm());
            map.put("dwmc", unit.getDwmc());
            map.put("fxzh", unit.getCollectionUnitAccount().getExtension().getFxzh());
            trader.sendRemittanceMSg(map, bjfs);
        } else if ("02".equals(bjfs)) {
            //暂收入账
            createVoucher(ywlsh);
        }

        AddUnitPayBackRes result = new AddUnitPayBackRes();
        result.setYWLSH(ywlsh);
        result.setStatus("success");
        return result;
    }

    private void paramCheck(UnitPayBackPost unitPayBackPost) {
        if (ComUtils.isEmpty(unitPayBackPost)) {
            throw new ErrorException("传入参数不能为空！");
        } else if (ComUtils.isEmpty(unitPayBackPost.getDWZH())) {
            throw new ErrorException("单位账号不能为空！");
        } else if (ComUtils.isEmpty(unitPayBackPost.getYWLSH())) {
            throw new ErrorException("业务流水号不能为空！");
        } else if (ComUtils.isEmpty(unitPayBackPost.getBJFS())) {
            throw new ErrorException("补缴方式不能为空！");
        }

    }

    private void payBackCheck(UnitPayBackPost unitPayBackPost) {

        String ywlsh = unitPayBackPost.getYWLSH();
        CCollectionUnitPaybackVice payBack = paybackViceDAO.getByYwlsh(ywlsh);
        if (payBack == null) {
            throw new ErrorException("业务流水号对应的补缴业务不存在！");
        }
        StCollectionUnitBusinessDetails dwywmx = payBack.getDwywmx();
        CCollectionUnitBusinessDetailsExtension extension = dwywmx.getExtension();
        StCommonUnit unit = dwywmx.getUnit();

        if (!"待确认".equals(extension.getStep())) {
            throw new ErrorException("补缴业务应处于待确认状态，当前补缴业务状态为：" + extension.getStep());
        }

        String bjfs = unitPayBackPost.getBJFS();
        if ("01|02".indexOf(bjfs) < 0) {
            throw new ErrorException("暂时只支持转账缴款、未分摊缴款！");
        }
        if ("02".equals(bjfs)) {
            BigDecimal fse = payBack.getFse();
            BigDecimal zsye = unit.getCollectionUnitAccount().getExtension().getZsye();
            if (zsye == null) {
                throw new ErrorException("数据异常，单位的暂收余额为空！");
            }
            if (fse.compareTo(zsye) > 0) {
                throw new ErrorException("暂收余额不足，请选择其他缴款方式！");
            }
        }

        //办理资料验证
        if (!iUploadImagesService.validateUploadFile(UploadFileModleType.归集.getCode(), UploadFileBusinessType.单位补缴.getCode(), unitPayBackPost.getBLZL())) {
            throw new ErrorException("上传资料缺失");
        }
    }

    /**
     * 根据单位/汇补缴年月获取补缴信息
     * 1、根据当前单位的清册情况，要补缴的月份单位已汇缴（个人没缴）
     * 2、启封的情况,启封的月份/或启封后没缴款的月份都可
     * 4、办理开户（开户变更）、根据首次汇缴年月
     * 5、生成补缴数据
     * 注意：当前的限制很大，不能适用多种情况（一般情况的补缴/补充比例），且与其他业务
     * 耦合在一起，增加难度及出错率
     */
    public AutoUnitPayBackRes getUnitPayBackAuto(TokenContext tokenContext, String dwzh, String hbjny) {

        AutoUnitPayBackRes result = new AutoUnitPayBackRes();

        //1、判断当前单位该月是否汇缴
        ComUtils.FlagMessage flagMessage = checkIsExistUnitRemittance(dwzh, hbjny);
        if (!flagMessage.flag) {
            //该单位当前月份没有汇缴记录，或未办结
            throw new ErrorException(flagMessage.message);
        }
        //找到单位下需要补缴的员工
        //2、(暂时只做开户)
        //Date hbjnyDate = ComUtils.parseToDate(hbjny,"yyyy-MM");
        //得到符合补缴的人员信息
        List<StCommonPerson> personList = getPayBackPerson(dwzh, hbjny);
        //3、根据当前的员工数据生成数据,如果没有直接返回
        if (personList.size() == 0) {
            //当前单位该月没有需要补缴的员工！
            throw new ErrorException("当前单位该月没有需要补缴的员工！");
        }

        ArrayList<AutoUnitPayBackResBJXX> bjxx = new ArrayList<AutoUnitPayBackResBJXX>();
        for (StCommonPerson person : personList) {
            AutoUnitPayBackResBJXX detailRow = new AutoUnitPayBackResBJXX();
            detailRow.setGRZH(person.getGrzh());
            detailRow.setXingMing(person.getXingMing());
            detailRow.setZJHM(person.getZjhm());
            BigDecimal dwbje = person.getCollectionPersonalAccount().getDwyjce();
            BigDecimal grbje = person.getCollectionPersonalAccount().getGryjce();
            BigDecimal hjfse = dwbje.add(grbje);
            detailRow.setDWBJE(ComUtils.moneyFormat(dwbje));
            detailRow.setGRBJE(ComUtils.moneyFormat(grbje));
            detailRow.setFSE(ComUtils.moneyFormat(hjfse));
            bjxx.add(detailRow);
        }
        result.setBJXX(bjxx);

        StCommonUnit unit = unitDAO.getUnit(dwzh);

        result.setDWMC(unit.getDwmc());
        result.setDWZH(unit.getDwzh());
        String jzny = ComUtils.parseToYYYYMM2(unit.getCollectionUnitAccount().getJzny());
        result.setJZNY(jzny);
        result.setDWYHJNY(BusUtils.getDWYHJNY(dwzh));
        // TODO
        result.setYWWD(tokenContext.getUserInfo().getCZY());
        result.setCZY(tokenContext.getUserInfo().getYWWD());
        result.setJBRXM(unit.getJbrxm());
        result.setJBRZJHM(unit.getJbrzjhm());
        result.setJBRZJLX(unit.getJbrzjlx());

        result.setDWZHYE(ComUtils.moneyFormat(unit.getCollectionUnitAccount().getDwzhye()));
        /*        result.setLSYLWFT(unit.getCollectionUnitAccount().getExtension().getLsylwft());*/
        result.setZSYE(ComUtils.moneyFormat(unit.getCollectionUnitAccount().getExtension().getZsye()));

        return result;
    }

    /**
     * 1、查询汇补缴月份 hbjny 的当月的清册记录，找到清册的生成时间 slsj
     * 2、找到slsj后面的开户记录
     * 3、如果开户的首次汇补缴年月小于或等于hbjny，且当前没有产生过补缴记录
     */
    private List<StCommonPerson> getPayBackPerson(String dwzh, String hbjny) {
        CCollectionUnitDepositInventoryVice unitInventoryMsg = inventoryViceDAO.getUnitInventoryMsg(dwzh, hbjny);
        Date slsj = unitInventoryMsg.getDwywmx().getExtension().getSlsj();
        //得到补缴人员数据
        Date hbjnyDate = ComUtils.parseToDate(hbjny, "yyyyMM");
        List<StCommonPerson> acctSetlist = paybackViceDAO.getIndiAcctSetAfterSlsj(dwzh, hbjnyDate, slsj);
        //如果人员中该月已补缴，从列表中移除
        //暂时先逐个搜索，后面优化
        Iterator<StCommonPerson> iterator = acctSetlist.iterator();
        while (iterator.hasNext()) {
            String grzh = iterator.next().getGrzh();
            if (true == paybackViceDAO.isExistPayBack(dwzh, grzh, hbjny)) {
                iterator.remove();
            }
        }
        return acctSetlist;
    }

    private ComUtils.FlagMessage checkIsExistUnitRemittance(String dwzh, String hbjny) {
        String hbjny2 = ComUtils.parseToYYYYMM(hbjny);
        CCollectionUnitRemittanceVice vice = remittanceViceDAO.getRemittance(dwzh, hbjny2);
        if (null == vice) {
            return ComUtils.checkMessage(false, "该单位该月没有汇缴信息");
        }
        CCollectionUnitBusinessDetailsExtension extension = vice.getDwywmx().getExtension();
        if (!"已入账分摊".equals(extension.getStep())) {
            //如果该单位汇缴没办结，不能补缴
            return ComUtils.checkMessage(false, "该单位该月存在汇缴业务,且未办结");
        }
        return ComUtils.checkMessage(true);
    }

    /**
     * 修改补缴信息,只能修改补缴方式及补缴原因，汇补缴年月不能修改，若修改则注销该业务，重新发起
     */
    public ReUnitPayBackRes putUnitPayBack(TokenContext tokenContext, String ywlsh, UnitPayBackPut unitPayBackPut) {

        CCollectionUnitPaybackVice updateDate = paybackViceDAO.getByYwlsh(ywlsh);
        updateDate.setBjfs(unitPayBackPut.getBJFS());

        Set<CCollectionUnitPaybackDetailVice> bjmx = updateDate.getBjmx();
        ArrayList<UnitPayBackPutBJXX> bjxx = unitPayBackPut.getBJXX();
        for (UnitPayBackPutBJXX detailView : bjxx) {
            for (CCollectionUnitPaybackDetailVice detailData : bjmx) {
                if (detailView.getGRZH().equals(detailData.getGrzh())) {
                    detailData.setBjyy(detailView.getBJYY());
                }
            }
        }
        paybackViceDAO.update(updateDate);

        ReUnitPayBackRes result = new ReUnitPayBackRes();
        result.setStatus("success");
        result.setYWLSH(ywlsh);
        return result;
    }

    /**
     * 获取补缴信息详情
     */
    public GetUnitPayBackRes getUnitPayBack(TokenContext tokenContext, String ywlsh) {

        CCollectionUnitPaybackVice bjxx = paybackViceDAO.getByYwlsh(ywlsh);
        if (null == bjxx) {
            throw new ErrorException("业务流水号对应的业务不存在");
        }
        String step = bjxx.getDwywmx().getExtension().getStep();

        StCollectionUnitBusinessDetails dwywmx = bjxx.getDwywmx();
        Set<CCollectionUnitPaybackDetailVice> bjmx = bjxx.getBjmx();
        String dwzh = dwywmx.getDwzh();
        StCommonUnit unit = unitDAO.getUnit(dwzh);

        GetUnitPayBackRes result = new GetUnitPayBackRes();
        result.setYWLSH(dwywmx.getYwlsh());
        result.setBJFS(bjxx.getBjfs());
        result.setDWZH(dwywmx.getDwzh());
        result.setDWMC(dwywmx.getUnit().getDwmc());
        result.setDWZHYE(ComUtils.moneyFormat(dwywmx.getUnit().getCollectionUnitAccount().getDwzhye()));
        String jzny = ComUtils.parseToYYYYMM2(dwywmx.getUnit().getCollectionUnitAccount().getJzny());
        result.setJZNY(jzny);
        result.setHBJNY(ComUtils.parseToYYYYMM2(dwywmx.getHbjny()));
        result.setYWWD(dwywmx.getExtension().getYwwd().getMingCheng());
        result.setDWYHJNY(BusUtils.getDWYHJNY(dwzh));
        result.setZSYE(ComUtils.moneyFormat(dwywmx.getUnit().getCollectionUnitAccount().getExtension().getZsye()));
        if ("待确认".equals(step)) {
            result.setJBRXM(unit.getJbrxm());
            result.setJBRZJLX(unit.getJbrzjlx());
            result.setJBRZJHM(unit.getJbrzjhm());

            String ywwd = tokenContext.getUserInfo().getYWWD();
            CAccountNetwork cAccountNetwork = cAccountNetworkDAO.get(ywwd);
            result.setYWWD(cAccountNetwork.getMingCheng());
            result.setCZY(tokenContext.getUserInfo().getCZY());
        } else {
            result.setJBRXM(dwywmx.getExtension().getJbrxm());
            result.setJBRZJLX(dwywmx.getExtension().getJbrzjlx());
            result.setJBRZJHM(dwywmx.getExtension().getJbrzjhm());
            result.setYWWD(dwywmx.getExtension().getYwwd().getMingCheng());
            result.setCZY(dwywmx.getExtension().getCzy());
        }

        result.setSTYHDM(unit.getStyhdm());
        result.setSTYHMC(unit.getStyhmc());
        StSettlementSpecialBankAccount bankAcount = BusUtils.getBankAcount(unit.getStyhdm(), "01");
        result.setSTYHZH(bankAcount.getYhzhhm());

        String blzl = dwywmx.getExtension().getBlzl();
        if (ComUtils.isEmpty(blzl)) {
            blzl = "";
        }
        result.setBLZL(blzl);
        ArrayList<GetUnitPayBackResBJXX> detailList = new ArrayList<GetUnitPayBackResBJXX>();
        //表格显示金额的一个统计
        BigDecimal grbjehj = BigDecimal.ZERO;
        BigDecimal dwbjehj = BigDecimal.ZERO;
        BigDecimal heji;
        for (CCollectionUnitPaybackDetailVice detailData : bjmx) {
            GetUnitPayBackResBJXX personDetail = new GetUnitPayBackResBJXX();
            personDetail.setGRZH(detailData.getGrzh());
            StCommonPerson person = personDAO.getByGrzh(detailData.getGrzh());
            personDetail.setXingMing(person.getXingMing());
            personDetail.setZJHM(person.getZjhm());
            personDetail.setBJYY(detailData.getBjyy());
            BigDecimal gryjce = detailData.getGryjce();
            BigDecimal dwyjce = detailData.getDwyjce();
            BigDecimal grfse = gryjce.add(dwyjce);
            personDetail.setGRBJE(ComUtils.moneyFormat(gryjce));
            personDetail.setDWBJE(ComUtils.moneyFormat(dwyjce));
            personDetail.setFSE(ComUtils.moneyFormat(grfse));
            //统计
            grbjehj = grbjehj.add(gryjce);
            dwbjehj = dwbjehj.add(dwyjce);
            detailList.add(personDetail);
        }
        result.setBJXX(detailList);
        GetUnitPayBackResBJHJ bjhj = new GetUnitPayBackResBJHJ();
        bjhj.setDWBJEHJ(ComUtils.moneyFormat(dwbjehj));
        bjhj.setGRBJEHJ(ComUtils.moneyFormat(grbjehj));
        heji = grbjehj.add(dwbjehj);
        bjhj.setFSEHJ(ComUtils.moneyFormat(heji));
        result.setBJHJ(bjhj);

        return result;
    }

    /**
     * 获取补缴回执单
     */
    public CommonResponses HeadUnitPayBack(String ywlsh) {

        CCollectionUnitPaybackVice bjxx = paybackViceDAO.getByYwlsh(ywlsh);
        if (null == bjxx) {
            throw new ErrorException("业务流水号对应的业务不存在");
        }
        String step = bjxx.getDwywmx().getExtension().getStep();
        if (!"已入账分摊".equals(step)) {
            throw new ErrorException("只有已入账分摊状态能打印回执单！当前业务状态为：" + step);
        }

        StCollectionUnitBusinessDetails dwywmx = bjxx.getDwywmx();
        Set<CCollectionUnitPaybackDetailVice> bjmx = bjxx.getBjmx();
        String dwzh = dwywmx.getDwzh();
        HeadUnitPayBackReceiptRes result = new HeadUnitPayBackReceiptRes();
        result.setYWLSH(dwywmx.getYwlsh());
        if (bjxx.getBjfs() != null) {
            SingleDictionaryDetail Bjfs_info = iDictionaryService.getSingleDetail(bjxx.getBjfs(), "RemitPayment");
            result.setBJFS(Bjfs_info != null ? Bjfs_info.getName() : "");//补缴方式
        }
        result.setDWZH(dwywmx.getDwzh());
        result.setDWMC(dwywmx.getUnit().getDwmc());
        result.setDWZHYE(ComUtils.moneyFormat(dwywmx.getUnit().getCollectionUnitAccount().getDwzhye()));
        String jzny = ComUtils.parseToYYYYMM2(dwywmx.getUnit().getCollectionUnitAccount().getJzny());
        result.setJZNY(jzny);
        result.setDWYHJNY(BusUtils.getDWYHJNY(dwzh));
        result.setZSYE(ComUtils.moneyFormat(dwywmx.getUnit().getCollectionUnitAccount().getExtension().getZsye()));
        // result.setYZM("验证码");
        result.setYWWD(dwywmx.getExtension().getYwwd().getMingCheng());
        result.setTZSJ(ComUtils.parseToString(new Date(), "yyyy-MM-dd HH:mm"));

        result.setJBRXM(dwywmx.getExtension().getJbrxm());
        result.setJBRZJLX(dwywmx.getExtension().getJbrzjlx());
        result.setJBRZJHM(dwywmx.getExtension().getJbrzjhm());
        result.setJZRQ(ComUtils.parseToString(dwywmx.getJzrq(), "yyyy-MM-dd"));
        result.setBJNY(ComUtils.parseToYYYYMM2(dwywmx.getHbjny()));
        result.setCZY(dwywmx.getExtension().getCzy());
        ArrayList<HeadUnitPayBackReceiptResBJXX> detailList = new ArrayList<HeadUnitPayBackReceiptResBJXX>();
        //表格显示金额的一个统计
        BigDecimal grbjehj = BigDecimal.ZERO;
        BigDecimal dwbjehj = BigDecimal.ZERO;
        BigDecimal heji;

        for (CCollectionUnitPaybackDetailVice detailData : bjmx) {
            HeadUnitPayBackReceiptResBJXX personDetail = new HeadUnitPayBackReceiptResBJXX();
            personDetail.setGRZH(detailData.getGrzh());
            StCommonPerson person = personDAO.getByGrzh(detailData.getGrzh());
            personDetail.setXingMing(person.getXingMing());
            personDetail.setZJHM(person.getZjhm());
            personDetail.setBJYY(detailData.getBjyy());
            BigDecimal gryjce = detailData.getGryjce();
            BigDecimal dwyjce = detailData.getDwyjce();
            BigDecimal grfse = gryjce.add(dwyjce);
            personDetail.setGRBJE(gryjce.toString());
            personDetail.setDWBJE(dwyjce.toString());
            personDetail.setFSE(grfse.toString());
            //统计
            grbjehj = grbjehj.add(gryjce);
            dwbjehj = dwbjehj.add(dwyjce);
            detailList.add(personDetail);
        }

        heji = grbjehj.add(dwbjehj);
        result.setBJXX(detailList);
        HeadUnitPayBackReceiptResBJHJ bjhj = new HeadUnitPayBackReceiptResBJHJ();
        bjhj.setFSEHJ(ComUtils.moneyFormat(heji));
        result.setBJHJ(bjhj);
        String id = pdfService.getUnitPaybackReceipt(result);
        System.out.println("生成id的值：" + id);
        CommonResponses commonResponses = new CommonResponses();
        commonResponses.setId(id);
        commonResponses.setState("success");
        return commonResponses;
    }

    /**
     * 公积金补缴通知单
     */
    public CommonResponses headUnitPayBackNotice(String ywlsh) {

        CCollectionUnitPaybackVice bjxx = paybackViceDAO.getByYwlsh(ywlsh);
        if (null == bjxx) {
            throw new ErrorException("业务流水号对应的业务不存在");
        }
        StCollectionUnitBusinessDetails dwywmx = bjxx.getDwywmx();
        CCollectionUnitBusinessDetailsExtension extension = dwywmx.getExtension();
        StCommonUnit unit = dwywmx.getUnit();
        HeadUnitPayBackNoticeRes result = new HeadUnitPayBackNoticeRes();

        result.setYWLSH(dwywmx.getYwlsh());
        result.setDWZH(dwywmx.getDwzh());
        result.setDWMC(unit.getDwmc());

        result.setJBRXM(unit.getJbrxm());
        result.setYWWD(extension.getYwwd().getMingCheng());
        result.setTZRQ(ComUtils.parseToString(new Date(), "yyyy-MM-dd HH:mm"));
        Date jzsj = ComUtils.getNextDate(bjxx.getCreated_at(), 5);    //5天内
        result.setJZSJ(ComUtils.parseToString(jzsj, "yyyy-MM-dd"));
        result.setBJNY(ComUtils.parseToYYYYMM2(dwywmx.getHbjny()));
        String bjfs = "";
        if ("01".equals(bjxx.getBjfs())) {
            bjfs = "转账";
        } else if ("02".equals(bjxx.getBjfs())) {
            bjfs = "暂收";
        }
        result.setBJFS(bjfs);
        result.setCZY(extension.getCzy());

        result.setFSE(ComUtils.moneyFormat(getFse(bjxx)));
        result.setFSRS(dwywmx.getFsrs().intValue());

        StSettlementSpecialBankAccount bankAcount = BusUtils.getBankAcount(unit.getStyhdm(), "01");
        result.setSKZH(bankAcount.getYhzhhm());
        result.setSKZHMC(bankAcount.getYhzhmc());
        result.setSTYHDM(unit.getStyhdm());
        result.setSTYHMC(unit.getStyhmc());

        BigDecimal fse = BigDecimal.ZERO;
        for (CCollectionUnitPaybackDetailVice detail : bjxx.getBjmx()) {
            fse = fse.add(detail.getDwyjce().add(detail.getGryjce()));
        }
        result.setSLSJ(ComUtils.parseToString(extension.getSlsj(), "yyyy-MM-dd HH:mm"));
        result.setZJE(ComUtils.toMoneyUpper(fse.toString()));
        String id = pdfService.getUnitPayBackNoticePdf(result);
        System.out.println("生成id的值：" + id);
        CommonResponses commonResponses = new CommonResponses();
        commonResponses.setId(id);
        commonResponses.setState("success");
        return commonResponses;
    }

    @Override
    public void doFinal(String ywlsh) {

        CCollectionUnitPaybackVice payBack = paybackViceDAO.getByYwlsh(ywlsh);
        Set<CCollectionUnitPaybackDetailVice> bjmx = payBack.getBjmx();
        StCollectionUnitBusinessDetails dwywmx = payBack.getDwywmx();

        StCommonUnit unit = unitDAO.getUnit(dwywmx.getDwzh());
        StCollectionUnitAccount unitAccount = unit.getCollectionUnitAccount();

        //补缴方式为暂收时
        BigDecimal fse = payBack.getFse();
        String bjfs = payBack.getBjfs();
        if ("02".equals(bjfs)) {
            BigDecimal zsye = unitAccount.getExtension().getZsye();   //暂收余额
            zsye = zsye.subtract(fse);
            if (zsye.compareTo(BigDecimal.ZERO) < 0) {
                throw new ErrorException("单位暂收余额不足！");
            }
            unitAccount.getExtension().setZsye(zsye);
        }
        //单位账户余额
        BigDecimal dwzhye = unitAccount.getDwzhye();
        dwzhye = dwzhye.add(fse);
        unitAccount.setDwzhye(dwzhye);
        unitDAO.update(unit);

        String hbjnyStr = dwywmx.getHbjny();
        Date hbjny = ComUtils.parseToDate(hbjnyStr, "yyyyMM");

        for (CCollectionUnitPaybackDetailVice detail : bjmx) {
            String grzh = detail.getGrzh();
            StCommonPerson person = personDAO.getByGrzh(detail.getGrzh());
            StCollectionPersonalAccount personalAccount = person.getCollectionPersonalAccount();

            BigDecimal grzhye = personalAccount.getGrzhye();       //个人账户余额
            BigDecimal heji = detail.getDwyjce().add(detail.getGryjce());
            grzhye = grzhye.add(heji);
            personalAccount.setGrzhye(grzhye);

            BigDecimal grzhdngjye = personalAccount.getGrzhdngjye();    //个人账户当年归集余额
            grzhdngjye = grzhdngjye.add(heji);
            personalAccount.setGrzhdngjye(grzhdngjye);
            //个人缴至年月
            CCommonPersonExtension extension = person.getExtension();
            Date grjzny = ComUtils.parseToDate(extension.getGrjzny(), "yyyyMM");
            if (grjzny == null || grjzny.compareTo(hbjny) < 0) {
                extension.setGrjzny(hbjnyStr);
            }
            personDAO.update(person);

            // 补缴产生个人业务表流水
            createPersonBus(grzh, heji, payBack, detail);

        }

        //办结时写入业务明细表
        dwywmx.setFse(fse);
        paybackViceDAO.save(payBack);

        String dwzh = unit.getDwzh();
        BusUtils.refreshUnitAcount(dwzh);
    }

    private void createPersonBus(String grzh, BigDecimal fse, CCollectionUnitPaybackVice payBack, CCollectionUnitPaybackDetailVice detail) {

        StCollectionPersonalBusinessDetails personalBusiness = new StCollectionPersonalBusinessDetails();
        StCommonPerson person = personDAO.getByGrzh(grzh);
        StCommonUnit unit = person.getUnit();

        personalBusiness.setFse(fse);
        personalBusiness.setUnit(unit);
        personalBusiness.setGrzh(grzh);
        personalBusiness.setPerson(person);
        personalBusiness.setGjhtqywlx(ywlx);
        personalBusiness.setJzrq(new Date());

        CCollectionPersonalBusinessDetailsExtension extension = new CCollectionPersonalBusinessDetailsExtension();
        extension.setCzmc(ywlx);
        extension.setStep("办结");
        extension.setBjsj(new Date());
        CCollectionUnitBusinessDetailsExtension unitBusExtension = payBack.getDwywmx().getExtension();
        extension.setYwwd(unitBusExtension.getYwwd());
        extension.setJbrxm(unitBusExtension.getJbrxm());
        extension.setJbrzjlx(unitBusExtension.getJbrzjlx());
        extension.setJbrzjhm(unitBusExtension.getJbrzjhm());

        BigDecimal dwyjce = detail.getDwyjce();
        BigDecimal gryjce = detail.getGryjce();
        String hbjny = payBack.getDwywmx().getHbjny();
        BigDecimal grzhye = person.getCollectionPersonalAccount().getGrzhye();

        extension.setDwfse(dwyjce);
        extension.setGrfse(gryjce);
        extension.setDqye(grzhye);
        extension.setFsny(hbjny);

        personalBusiness.setExtension(extension);

        personalBusinessDAO.save(personalBusiness);
    }

    /**
     * 1、如果生效月份在最新清册生成之后的月份，正常处理，不生成补缴数据
     * 2、如果生效月份与清册是同一个月，表示该人当月需要缴存，1、受理时间在清册生成之前，说明清册已包含    2、启封受理时间在清册生成之后，说明清册未包含此人，需生成补缴
     * 3、如果生效月份在最近的清册月份之前，将生成多个月补缴数据
     * 注：受理时也需要调用该方法，如果将生成补缴数据来提示用户
     */
    @Override
    public void checkUnseal(String ywlsh, Map map) {
        //启封业务
        CCollectionIndividualAccountActionVice seal = collectionIndividualAccountActionViceDAO.getByYWLSH(ywlsh);
        Date sxny = ComUtils.parseToDate(seal.getSxny(), "yyyyMM");
        String grzh = seal.getGrzh();
        StCommonPerson person = personDAO.getByGrzh(grzh);
        StCommonUnit unit = person.getUnit();
        if (unit == null) {
            throw new ErrorException("当前职工没有单位信息！");
        }
        String dwzh = unit.getDwzh();

        //得到单位最近一次清册记录
        CCollectionUnitDepositInventoryVice inventory = inventoryViceDAO.getNearLyInventory(dwzh);
        if (inventory != null) {
            Date qcyf = ComUtils.parseToDate(inventory.getQcny(), "yyyyMM");
            //根据最近一次的清册月份和启封业务的生效年月来判断
            //生效年月小于或等于清册月份，可能产生补缴数据,补缴额以当前状态为准
            //修改为只有sxny的那个月产生补缴数据
            if (sxny.compareTo(qcyf) <= 0) {
                //inventoryCheck(grzh, dwzh, sxny, qcyf);
                createPaybackBatch(dwzh, grzh, sxny, qcyf, map);
            }
        }
    }

    /**
     * 启封时检查清册
     */
    private void inventoryCheck(String grzh, String dwzh, Date sxny, Date lastqcny) {
        Date sxyf = (Date) sxny.clone();
        //从sxny往后检查最新清册
        while (sxyf.compareTo(lastqcny) <= 0) {
            LatestInventory latestInventory = inventory.getLatestInventoryOfMonth(dwzh, sxyf);
            //如果个人在清册中不存在，或账户状态为正常，抛出异常信息
            checkLatestInventory(grzh, latestInventory);
            sxyf = ComUtils.getNextMonth(sxyf);
        }
    }

    private void checkLatestInventory(String grzh, LatestInventory latestInventory) {
        InventoryMessage inventoryMessage = latestInventory.getInventoryMessage();
        ArrayList<InventoryDetail> qcxq = inventoryMessage.getQCXQ();
        String qcny = inventoryMessage.getQCNY();
        //先查询该月清册中是否存在
        for (InventoryDetail detail : qcxq) {
            if (grzh.equals(detail.getGRZH())) {
                if ("02".equals(detail.getGRZHZT())) {
                    return;
                }
                throw new ErrorException("当前职工在" + qcny + "的最新清册为正常状态，不能办理启封！");
            }
        }
        throw new ErrorException("单位" + qcny + "的清册中不存在当前职工！");
    }

    private void createPaybackBatch(String dwzh, String grzh, Date stratMonth, Date endMonth, Map map) {
        if (stratMonth.compareTo(endMonth) <= 0) {
            //产生多个月的补缴数据，从开始年月到结束月份几个月
            Date createMonth = stratMonth;
            while (createMonth.compareTo(endMonth) <= 0) {
                String createMonthStr = ComUtils.parseToString(createMonth, "yyyyMM");
                //boolean isExistInventory = inventoryViceDAO.isExistInventory(grzh, createMonthStr);
                //查看该人该月是否已经生成补缴数据（校验），如果生成过该月补缴数据，不再生成
                // 这里是所有单位
                //boolean isExistPayBack = paybackViceDAO.isExistPayBack(grzh, createMonthStr);
                //没缴过款(该人该月没有清册记录)，并且没有补缴数据
                //if (!isExistInventory && !isExistPayBack) {
                //产生补缴数据
                createPayback(dwzh, grzh, createMonthStr, map);
                //}
                //继续生成下一个月
                createMonth = ComUtils.getNextMonth(createMonth);
            }
        }
    }

    /**
     * 产生补缴数据
     */
    private void createPayback(String dwzh, String grzh, String hbjny, Map map) {
        //查看当月是否存在正在办理的补缴业务，存在抛出异常，等待办结之后再次办理
        //TODO 是否这样限制
        /*boolean isExistDoing = paybackViceDAO.isExistDoing(dwzh);
        if (isExistDoing) {
            throw new ErrorException("当前员工将生成补缴数据,个人账号:" + grzh + "，所在单位:" + dwzh + ",存在未办结的补缴业务，需办结之后才能办理该笔业务！");
        }*/
        //查看单位该月是否存在新建状态的补缴业务
        CCollectionUnitPaybackVice payback = paybackViceDAO.getPaybackNew(dwzh, hbjny);
        if (payback == null) {
            //需新建一笔补缴业务
            CCollectionUnitPaybackVice newPayback = new CCollectionUnitPaybackVice();
            StCommonUnit unit = unitDAO.getUnit(dwzh);
            if (unit == null) {
                throw new ErrorException("单位不存在！");
            }
            CCollectionUnitDepositInventoryVice unitInventoryMsg = inventoryViceDAO.getUnitInventoryMsg(dwzh, hbjny);
            if (unitInventoryMsg == null) {
                return; //该月没有清册记录
            }
            String qcqrdh = unitInventoryMsg.getQcqrdh();
            newPayback.setQcqrdh(qcqrdh);

            Set<CCollectionUnitPaybackDetailVice> bjmx = new HashSet<CCollectionUnitPaybackDetailVice>();
            CCollectionUnitPaybackDetailVice paybackDtaile = getPersonPaybackDetail(grzh, dwzh, hbjny, map);
            bjmx.add(paybackDtaile);
            newPayback.setBjmx(bjmx);

            StCollectionUnitBusinessDetails dwywmx = new StCollectionUnitBusinessDetails();
            CCollectionUnitBusinessDetailsExtension extension = new CCollectionUnitBusinessDetailsExtension();
            extension.setStep("新建");
            extension.setCzmc(ywlx);
            extension.setSlsj(new Date());
            extension.setJbrzjlx(unit.getJbrzjlx());
            extension.setJbrzjhm(unit.getJbrzjhm());
            extension.setJbrxm(unit.getJbrxm());

            CAccountNetwork ywwd = (CAccountNetwork) map.get("ywwd");
            String czy = map.get("czy").toString();
            extension.setYwwd(ywwd);
            extension.setCzy(czy);

            dwywmx.setExtension(extension);
            BigDecimal fse = paybackDtaile.getDwyjce().add(paybackDtaile.getGryjce());
            newPayback.setFse(fse);
            dwywmx.setFse(BigDecimal.ZERO);
            dwywmx.setFsrs(BigDecimal.ONE);
            dwywmx.setDwzh(dwzh);
            dwywmx.setHbjny(hbjny);
            dwywmx.setYwmxlx(ywlx);

            dwywmx.setUnit(unit);

            newPayback.setDwywmx(dwywmx);
            paybackViceDAO.save(newPayback);

            TokenContext token = (TokenContext) map.get("token");
            String ywlsh = dwywmx.getYwlsh();

            //流程调用
            WorkCondition condition = new WorkCondition();
            condition.setYwlsh(ywlsh);
            condition.setCzy(czy);
            condition.setYwwd(ywwd.getId());
            condition.setStatus("新建");
            condition.setEvent("通过");
            condition.setZtlx("1");
            condition.setType(BusinessType.Collection);
            condition.setSubType(busType);    //补缴)
            condition.setRoleList(token.getRoleList());
            doWork(condition);
        } else {
            //加入该笔业务即可
            StCollectionUnitBusinessDetails dwywmx = payback.getDwywmx();
            Set<CCollectionUnitPaybackDetailVice> bjmx = payback.getBjmx();
            CCollectionUnitPaybackDetailVice personPaybackDetail = getPersonPaybackDetail(grzh, dwzh, hbjny, map);
            bjmx.add(personPaybackDetail);
            BigDecimal fse = payback.getFse();
            fse = fse.add(personPaybackDetail.getGryjce()).add(personPaybackDetail.getDwyjce());
            payback.setFse(fse);
            BigDecimal fsrs = dwywmx.getFsrs();
            dwywmx.setFsrs(fsrs.add(BigDecimal.ONE));
            paybackViceDAO.update(payback);
        }
    }

    /**
     * 计算该人该月的缴存额，作为补缴额（不一定是当前的值，该员工该月的实际缴存额）
     * 找该月到现在调基调比业务，没有则视为没有改变，取当前的即可,有则取之前的金额
     */
    private CCollectionUnitPaybackDetailVice getPersonPaybackDetail(String grzh, String dwzh, String hbjny, Map map) {

        CCollectionUnitPaybackDetailVice result = new CCollectionUnitPaybackDetailVice();
        result.setGrzh(grzh);
        //找到当月比例,使用清册的实际比例即可，无清册当然也不用补缴
        CCollectionUnitDepositInventoryVice unitInventoryMsg = inventoryViceDAO.getUnitInventoryMsg(dwzh, hbjny);

        if ("启封补缴".equals(map.get("byjj"))) {
            CCollectionUnitPaybackDetailVice paybackDetail = getPaybackDetail(grzh, unitInventoryMsg);
            if (paybackDetail != null) {
                return paybackDetail;
            }
        }

        BigDecimal dwjcbl = unitInventoryMsg.getDwjcbl();
        BigDecimal grjcbl = unitInventoryMsg.getGrjcbl();
        //找到当月的个人基数,3种情况
        //1、有生效月份为汇补缴年月的调基业务，按办结时间排序降序，取第一条的调整后基数
        //2、有生效月份大于汇补缴年月的数据时，按办结时间排序，取第一条的调整前基数
        //3、无调基业务，说明基数没有改变，直接取当前的即可
        CCollectionPersonRadixDetailVice radixLast = personRadixViceDAO.getRadixLast(dwzh, grzh, hbjny);
        /*BigDecimal grjs = null;
        if (radixLast != null) {
            grjs = radixLast.getTzhgrjs();
        } else {
            CCollectionPersonRadixDetailVice radixAfter = personRadixViceDAO.getRadixAfter(dwzh, grzh, hbjny);
            if (radixAfter != null) {
                grjs = radixAfter.getTzqgrjs();
            } else {
                StCommonPerson person = personDAO.getByGrzh(grzh);
                grjs = person.getCollectionPersonalAccount().getGrjcjs();
            }
        }*/
        StCommonPerson person = personDAO.getByGrzh(grzh);
        BigDecimal grjs = person.getCollectionPersonalAccount().getGrjcjs();

        BigDecimal gryjce = BusUtils.computeDeposit(grjs, grjcbl);
        BigDecimal dwyjce = BusUtils.computeDeposit(grjs, dwjcbl);

        result.setGryjce(gryjce);
        result.setDwyjce(dwyjce);
        result.setBjyy(map.get("bjyy").toString());
        return result;
    }

    private CCollectionUnitPaybackDetailVice getPaybackDetail(String grzh, CCollectionUnitDepositInventoryVice unitInventoryMsg) {
        CCollectionUnitPaybackDetailVice result = new CCollectionUnitPaybackDetailVice();
        result.setGrzh(grzh);
        result.setBjyy("启封补缴");

        Set<CCollectionUnitDepositInventoryDetailVice> qcxq = unitInventoryMsg.getQcxq();
        for (CCollectionUnitDepositInventoryDetailVice inventoryDetail : qcxq) {
            if (grzh.equals(inventoryDetail.getGrzh())) {
                BigDecimal dwyjce = inventoryDetail.getDwyjce();
                BigDecimal gryjce = inventoryDetail.getGryjce();
                result.setDwyjce(dwyjce);
                result.setGryjce(gryjce);
                return result;
            }
        }
        return null;

    }

    /**
     * 个人开户，可能产生补缴业务
     * 当首次汇补缴年月小于等当前的清册月份时，产生补缴
     */
    @Override
    public void checkPersonAccSet(String grzh, Map map) {
        StCommonPerson person = personDAO.getByGrzh(grzh);
        String dwzh = person.getUnit().getDwzh();
        CCommonPersonExtension extension = person.getExtension();
        Date gjjschjny = ComUtils.parseToDate(extension.getGjjschjny(), "yyyyMM");
        CCollectionUnitDepositInventoryVice inventory = inventoryViceDAO.getNearLyInventory(dwzh);
        map.put("bjyy", "开户补缴");
        if (inventory != null) {
            //单位没有清册过（刚开户）
            Date qcny = ComUtils.parseToDate(inventory.getQcny(), "yyyyMM");
            if (gjjschjny.compareTo(qcny) <= 0) {
                createPaybackBatch(dwzh, grzh, gjjschjny, qcny, map);
            }
        }

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void getPaymentNotice(String ywlsh) {

        CCollectionUnitPaybackVice payback = paybackViceDAO.getByYwlsh(ywlsh);

        StCollectionUnitBusinessDetails dwywmx = payback.getDwywmx();
        CCollectionUnitBusinessDetailsExtension extension = dwywmx.getExtension();
        String step = extension.getStep();
        if ("已入账分摊".equals(step)) {
            throw new ErrorException("当前的业务状态为" + step + ",应该未办结!");
        }
        extension.setStep("已入账分摊");

        paybackViceDAO.save(payback);

        doFinal(ywlsh);

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void doFail(String ywlsh, String sbyy) {
        CCollectionUnitPaybackVice payback = paybackViceDAO.getByYwlsh(ywlsh);
        CCollectionUnitBusinessDetailsExtension extension = payback.getDwywmx().getExtension();
        String step = extension.getStep();
        if (CollectionBusinessStatus.待入账.getName().equals(step)) {
            extension.setBeizhu(sbyy);
            extension.setStep(CollectionBusinessStatus.入账失败.getName());
            paybackViceDAO.save(payback);
        }
    }

    @Override
    public BusCommonRetrun accountRecord(String ywlsh, String code) {
        BusCommonRetrun busCommonRetrun = new BusCommonRetrun();
        if ("0".equals(code)) {
            CCollectionUnitPaybackVice payback = paybackViceDAO.getByYwlsh(ywlsh);
            CCollectionUnitBusinessDetailsExtension extension = payback.getDwywmx().getExtension();
            String step = extension.getStep();
            if (CollectionBusinessStatus.入账失败.getName().equals(step)) {
                extension.setStep(CollectionBusinessStatus.已入账分摊.getName());
                paybackViceDAO.save(payback);
                doFinal(ywlsh);

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

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void payBackckPaymentNotice(String ywlsh, AccChangeNotice accChangeNotice, String id) {
        BigDecimal amt = accChangeNotice.getAccChangeNoticeFile().getAmt();
        CCollectionUnitPaybackVice payback = paybackViceDAO.getByYwlsh(ywlsh);
        StCollectionUnitBusinessDetails dwywmx = payback.getDwywmx();
        String dwzh = dwywmx.getDwzh();
        BigDecimal fse = payback.getFse();

        //转账的金额，3种情况：1、小于时入暂收，入账失败 2、相等正常匹配 3、大于时先入暂收，再从暂收中缴款
        int flag = amt.compareTo(fse);
        if ("已入账分摊".equals(dwywmx.getExtension().getStep())) {
            flag = -2;
        }

        if (flag < 0) {
            //入暂收
            if (flag == -1) {
                this.doFail(ywlsh, "到账金额不足，已入单位未分摊。");
            }
            AccChangeNoticeFile accChangeNoticeFile = accChangeNotice.getAccChangeNoticeFile();
            accChangeNoticeFile.setSummary(dwzh);
            accChangeNotice.getAccChangeNoticeFile().setNo(id); //修改业务流水号为id
            voucherAutoService.checkBusiness(accChangeNotice);
        } else if (flag == 0) {
            //转账入账
            getPaymentNotice(ywlsh);
            createVoucher(ywlsh, accChangeNotice);
        } else {
            //先入暂收账，再从暂收分摊入账
            AccChangeNoticeFile accChangeNoticeFile = accChangeNotice.getAccChangeNoticeFile();
            accChangeNoticeFile.setSummary(dwzh);
            accChangeNotice.getAccChangeNoticeFile().setNo(id); //修改业务流水号为id
            voucherAutoService.checkBusiness(accChangeNotice);

            //从暂收进行汇缴,修改缴款的 缴存方式
            payback.setBjfs("02");
            paybackViceDAO.save(payback);

            //业务办结处理
            getPaymentNotice(ywlsh);
            //入账
            createVoucher(ywlsh);
        }

    }

    private void createVoucher(String ywlsh) {
        //入账
        CCollectionUnitPaybackVice payback = paybackViceDAO.getByYwlsh(ywlsh);
        StCollectionUnitBusinessDetails dwywmx = payback.getDwywmx();
        CCollectionUnitBusinessDetailsExtension extension = payback.getDwywmx().getExtension();

        StCommonUnit unit = unitDAO.getUnit(dwywmx.getDwzh());
        String hbjny = dwywmx.getHbjny();

        String code = VoucherBusinessType.汇补缴手动分摊.getCode();
        String ywwd = extension.getYwwd().getMingCheng();
        String czy = extension.getCzy();

        //财务入账
        String zaiYao = unit.getDwmc() + hbjny + "补缴";

        VoucherRes voucherRes = voucherManagerService.addVoucher(ywwd, czy, czy, "", "",
                code, code, ywlsh, zaiYao, payback.getFse(), null, null);

        if (ComUtils.isEmpty(voucherRes.getJZPZH())) {
            throw new ErrorException(voucherRes.getMSG());
        }
        CFinanceRecordUnit cFinanceRecordUnit = new CFinanceRecordUnit();
        cFinanceRecordUnit.setFse(payback.getFse().negate());
        cFinanceRecordUnit.setJzpzh(voucherRes.getJZPZH());
        cFinanceRecordUnit.setRemark("未分摊补缴");
        cFinanceRecordUnit.setSummary(zaiYao);
        cFinanceRecordUnit.setZjly(WFTLY.未分摊汇补缴.getName());
        cFinanceRecordUnit.setDwzh(dwywmx.getDwzh());
        cFinanceRecordUnit.setWftye(unit.getCollectionUnitAccount().getExtension().getZsye());
        icFinanceRecordUnitDAO.save(cFinanceRecordUnit);
        extension.setJzpzh(voucherRes.getJZPZH());
        dwywmx.setJzrq(new Date());

        paybackViceDAO.save(dwywmx);
    }

    private void createVoucher(String ywlsh, AccChangeNotice accChangeNotice) {
        //入账
        CCollectionUnitPaybackVice payback = paybackViceDAO.getByYwlsh(ywlsh);
        StCollectionUnitBusinessDetails dwywmx = payback.getDwywmx();
        CCollectionUnitBusinessDetailsExtension extension = payback.getDwywmx().getExtension();

        StCommonUnit unit = unitDAO.getUnit(dwywmx.getDwzh());
        String hbjny = dwywmx.getHbjny();

        String code = VoucherBusinessType.汇补缴.getCode();
        String ywwd = extension.getYwwd().getMingCheng();
        String czy = extension.getCzy();

        //财务入账
        String zaiYao = unit.getDwmc() + hbjny + "补缴";
        accChangeNotice.getAccChangeNoticeFile().setSummary(zaiYao);

        VoucherRes voucherRes = voucherManagerService.addVoucher(ywwd, czy, czy, "", "",
                code, code, accChangeNotice, null);

        if (ComUtils.isEmpty(voucherRes.getJZPZH())) {
            throw new ErrorException(voucherRes.getMSG());
        }

        extension.setJzpzh(voucherRes.getJZPZH());
        dwywmx.setJzrq(new Date());

        paybackViceDAO.save(dwywmx);
    }

    @Override
    public CommonResponses postPayBackTemporary(TokenContext tokenContext, String ywlsh) {
        CCollectionUnitPaybackVice payback = paybackViceDAO.getByYwlsh(ywlsh);
        AssertUtils.notEmpty(payback, "当前的补缴业务不存在！");
        //1、检查业务状态，不为办结状态
        checkBusNotInFinal(payback);

        //2、验证暂收是否足够
        BigDecimal fse = payback.getFse();
        StCollectionUnitBusinessDetails dwywmx = payback.getDwywmx();
        String dwzh = dwywmx.getDwzh();
        StCommonUnit unit = unitDAO.getUnit(dwzh);
        CCollectionUnitAccountExtension extension = unit.getCollectionUnitAccount().getExtension();
        BigDecimal zsye = extension.getZsye();
        if (fse.compareTo(zsye) > 0) {
            throw new ErrorException("单位的未分摊余额：" + ComUtils.moneyFormat(zsye) + "，小于补缴发生额！");
        }

        //3、验修改补缴方式,暂收
        payback.setBjfs("02");
        paybackViceDAO.save(payback);

        //2、补缴办理
        getPaymentNotice(ywlsh);

        //入账
        createVoucher(ywlsh);

        CommonResponses result = new CommonResponses();
        result.setId(ywlsh);
        result.setState("success");
        return result;
    }

    private void checkBusNotInFinal(CCollectionUnitPaybackVice payback) {
        CCollectionUnitBusinessDetailsExtension extension = payback.getDwywmx().getExtension();
        if ("已入账分摊".equals(extension.getStep())) {
            throw new ErrorException("当前的补缴业务已办结！");
        }
    }

    @Override
    public PageResNew<ListPayBackResRes> getUnitPayBackList(TokenContext tokenContext, String dwmc, String dwzh, String ywzt, String czy, String ywwd, String kssj, String jssj, String marker, String pageSize, String action) {

        ListAction listAction = ListAction.pageType(action);
        int pagesize = ComUtils.parstPageSize(pageSize);

        IBaseDAO.CriteriaExtension ce = getCriteriaExtension(dwmc, dwzh, ywzt, czy, ywwd);

        Date startDate = ComUtils.parseToDate(kssj, "yyyy-MM-dd HH:mm");
        Date endDate = ComUtils.parseToDate(jssj, "yyyy-MM-dd HH:mm");
        PageResults<CCollectionUnitPaybackVice> pageResults = null;
        try {
            pageResults = paybackViceDAO.listWithMarker(null, startDate, endDate, "created_at", Order.DESC, null, SearchOption.REFINED, marker, pagesize, listAction, ce);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorException(e);
        }
        PageResNew<ListPayBackResRes> result = new PageResNew<>();
        ArrayList<ListPayBackResRes> list = getResultList(pageResults.getResults());
        result.setResults(action, list);

        return result;
    }

    private IBaseDAO.CriteriaExtension getCriteriaExtension(String dwmc, String dwzh, String ywzt, String czy, String ywwd) {
        return new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                criteria.createAlias("dwywmx", "dwywmx");
                criteria.createAlias("dwywmx.cCollectionUnitBusinessDetailsExtension", "cCollectionUnitBusinessDetailsExtension");
                if (!ComUtils.isEmpty(dwmc)) {
                    criteria.createAlias("dwywmx.unit", "unit");
                    criteria.add(Restrictions.like("unit.dwmc", "%" + dwmc + "%"));
                }
                if (!ComUtils.isEmpty(dwzh)) {
                    criteria.add(Restrictions.like("dwywmx.dwzh", "%" + dwzh + "%"));
                }
                if (!ComUtils.isEmpty(czy)) {
                    criteria.add(Restrictions.like("cCollectionUnitBusinessDetailsExtension.czy", "%" + czy + "%"));
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

    private ArrayList<ListPayBackResRes> getResultList(List<CCollectionUnitPaybackVice> results) {
        ArrayList<ListPayBackResRes> resultList = new ArrayList<>();
        for (CCollectionUnitPaybackVice rowSource : results) {
            ListPayBackResRes rowView = new ListPayBackResRes();
            StCollectionUnitBusinessDetails dwywmx = rowSource.getDwywmx();
            CCollectionUnitBusinessDetailsExtension extension = dwywmx.getExtension();
            rowView.setYWLSH(dwywmx.getYwlsh());
            rowView.setDWMC(dwywmx.getUnit().getDwmc());
            rowView.setDWZH(dwywmx.getDwzh());
            rowView.setFSRS(dwywmx.getFsrs().intValue());
            rowView.setFSE(ComUtils.moneyFormat(dwywmx.getFse()));
            rowView.setHBJNY(ComUtils.parseToYYYYMM2(dwywmx.getHbjny()));
            rowView.setYWZT(extension.getStep());
            rowView.setJZPZH(extension.getJzpzh());
            rowView.setCZY(extension.getCzy());
            rowView.setSLSJ(ComUtils.parseToString(extension.getSlsj(), "yyyy-MM-dd HH:mm"));
            rowView.setYWWD(extension.getYwwd().getMingCheng());
            rowView.setId(rowSource.getId());
            rowView.setSBYY(filterNull(extension.getBeizhu(), extension.getStep()));
            resultList.add(rowView);
        }
        return resultList;
    }

    private String filterNull(String beizhu, String step) {
        if ("入账失败".equals(step)) {
            return ComUtils.filterNull(beizhu);
        } else {
            return "";
        }
    }

}
