package com.handge.housingfund.collection.service.unitdeposit;

import com.handge.housingfund.collection.service.common.BaseService;
import com.handge.housingfund.collection.utils.AssertUtils;
import com.handge.housingfund.collection.utils.BusUtils;
import com.handge.housingfund.collection.utils.ComUtils;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessStatus;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.collection.model.InventoryDetail;
import com.handge.housingfund.common.service.collection.model.InventoryMessage;
import com.handge.housingfund.common.service.collection.model.LatestInventory;
import com.handge.housingfund.common.service.collection.model.deposit.*;
import com.handge.housingfund.common.service.collection.service.common.WorkCondition;
import com.handge.housingfund.common.service.collection.service.trader.ICollectionTrader;
import com.handge.housingfund.common.service.collection.service.unitdeposit.UnitDepositInventory;
import com.handge.housingfund.common.service.collection.service.unitdeposit.UnitPayWrong;
import com.handge.housingfund.common.service.finance.IVoucherManagerService;
import com.handge.housingfund.common.service.finance.model.VoucherRes;
import com.handge.housingfund.common.service.finance.model.enums.VoucherBusinessType;
import com.handge.housingfund.common.service.finance.model.enums.WFTLY;
import com.handge.housingfund.common.service.loan.model.CommonResponses;
import com.handge.housingfund.common.service.others.IPdfService;
import com.handge.housingfund.common.service.others.IUploadImagesService;
import com.handge.housingfund.common.service.others.enums.UploadFileBusinessType;
import com.handge.housingfund.common.service.others.enums.UploadFileModleType;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.*;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

import static com.handge.housingfund.database.utils.DAOBuilder.instance;

/**
 * Created by 向超 on 2017/7/27.
 * 问题：
 * 1、错缴生成规则是否符合需求
 * 2、错缴是否可以取消
 * 3、错缴生成时，如果员工金额不足该如何处理
 */
@Component
public class UnitPayWrongImpl extends BaseService implements UnitPayWrong {

    private static final String ywlx = CollectionBusinessType.错缴更正.getCode();
    private String ywlxmc = CollectionBusinessType.错缴更正.getName();

    private String busType = BusinessSubType.归集_错缴更正.getSubType();

    @Autowired
    private IStCommonUnitDAO commonUnitDAO;
    @Autowired
    private IStCommonPersonDAO commonPersonDAO;
    @Autowired
    private IStCollectionUnitBusinessDetailsDAO collectionUnitBusinessDetailsDAO;
    @Autowired
    private ICCollectionUnitPayWrongViceDAO payWrongDAO;
    @Autowired
    private ICCollectionIndividualAccountActionViceDAO collectionIndividualAccountActionViceDAO;

    private static String format = "yyyy-MM-dd HH:mm";
    private static String formatNY = "yyyyMM";

    @Autowired
    private ICCollectionUnitRemittanceViceDAO remittanceViceDAO;

    @Autowired
    private ICCollectionUnitDepositInventoryViceDAO unitDepositInventoryViceDAO;

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
    private ICAccountNetworkDAO cAccountNetworkDAO;

    @Autowired
    private UnitDepositInventory inventory;

    @Autowired
    private IStCollectionPersonalBusinessDetailsDAO grywmxDao;

    @Autowired
    private ICFinanceRecordUnitDAO icFinanceRecordUnitDAO;

    /**
     * 获取单位错缴列表
     */
    @Override
    public PageRes<ListUnitPayWrongResRes> getUnitPayWrongList(TokenContext tokenContext, String dwmc, String dwzh, String ywzt, String czy, String kssj, String jssj, String pageNumber, String pageSize) {
        //参数验证
        int pageno = ComUtils.parstPageNo(pageNumber);
        int pagesize = ComUtils.parstPageSize(pageSize);

        String ywwd = tokenContext.getUserInfo().getYWWD();
        IBaseDAO.CriteriaExtension ce = getCriteriaExtension(dwmc, dwzh, ywzt, czy, ywwd);

        Date startDate = ComUtils.parseToDate(kssj, "yyyy-MM-dd HH:mm");
        Date endDate = ComUtils.parseToDate(jssj, "yyyy-MM-dd HH:mm");
        PageResults<CCollectionUnitPayWrongVice> pageResults = payWrongDAO.listWithPage(null, startDate, endDate, "created_at", Order.DESC, null, SearchOption.REFINED, pageno, pagesize, ce);
        List<CCollectionUnitPayWrongVice> results = pageResults.getResults();

        ArrayList<ListUnitPayWrongResRes> resultList = getResultList(results);

        PageRes<ListUnitPayWrongResRes> pageres = new PageRes<>();
        pageres.setResults(resultList);
        pageres.setCurrentPage(pageResults.getCurrentPage());
        pageres.setNextPageNo(pageResults.getPageNo());
        pageres.setPageSize(pageResults.getPageSize());
        pageres.setPageCount(pageResults.getPageCount());
        pageres.setTotalCount(pageResults.getTotalCount());

        return pageres;
    }

    /**
     * 错缴业务提交
     * 1、单笔业务新建：按单位和错缴更正月份，可能有多个月多笔，是否限制只能一笔处于办理中
     * 2、提交后发送结算平台信息，进入待入账状态
     * 无视保存
     */
    @Override
    public AddUnitPayWrongPostRes addUnitPayWrong(TokenContext tokenContext, UnitPayWrongPost unitPayWrongPost) {

        CAccountNetwork network = cAccountNetworkDAO.get(tokenContext.getUserInfo().getYWWD());

        //办理资料验证
        if (!iUploadImagesService.validateUploadFile(UploadFileModleType.归集.getCode(),
                UploadFileBusinessType.单位错缴.getCode(), unitPayWrongPost.getBLZL())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "上传资料");
        }
        //参数检查
        checkNewAdd(unitPayWrongPost);
        //办理资料验证
        iUploadImagesService.validateUploadFile(UploadFileModleType.归集.getCode(), UploadFileBusinessType.清册确认.getCode(), unitPayWrongPost.getBLZL());

        String ywlsh = unitPayWrongPost.getYWLSH();
        String dwzh = unitPayWrongPost.getDWZH();
        StCommonUnit unit = commonUnitDAO.getUnit(dwzh);

        if (!unit.getExtension().getKhwd().equals(tokenContext.getUserInfo().getYWWD())) {
            throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务不在当前网点受理范围内");
        }
        //更新错缴信息
        //验证业务信息
        CCollectionUnitPayWrongVice payWrong = payWrongDAO.getByYwlsh(ywlsh);
        if (payWrong == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务(" + ywlsh + ")不存在！");
        }
        CCollectionUnitBusinessDetailsExtension extension = payWrong.getDwywmx().getExtension();
        //验证业务状态是否为新建
        if (!CollectionBusinessStatus.新建.getName().equals(extension.getStep())) {
            throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "业务不处于新建状态，当前状态为" + extension.getStep());
        }

        /*payWrong.setSkyhhm(unitPayWrongPost.getSKYHHM());
        payWrong.setSkyhdm(unitPayWrongPost.getSKYHDM());
        payWrong.setSkyhmc(unitPayWrongPost.getSKYHMC());
        payWrong.setSkyhzh(unitPayWrongPost.getSKYHZH());*/

        //添加错缴原因
        Set<CCollectionUnitPayWrongDetailVice> cjxq = payWrong.getCjxq();
        ArrayList<UnitPayWrongPostGZXX> gzxx = unitPayWrongPost.getGZXX();
        outer:
        for (UnitPayWrongPostGZXX view : gzxx) {
            for (CCollectionUnitPayWrongDetailVice cuojiao : cjxq) {
                if (cuojiao.getGrzh().equals(view.getGRZH())) {
                    cuojiao.setCjyy(view.getYuanYin());
                    continue outer;
                }
            }
        }

        extension.setSlsj(new Date());
        extension.setBlzl(unitPayWrongPost.getBLZL());
        extension.setCzy(unitPayWrongPost.getCZY());

        extension.setYwwd(network);
        extension.setJbrxm(unit.getJbrxm());
        extension.setJbrzjlx(unit.getJbrzjlx());
        extension.setJbrzjhm(unit.getJbrzjhm());
        payWrongDAO.save(payWrong);

        //保存历史
        BusUtils.saveAuditHistory(ywlsh, tokenContext, ywlxmc, "新建");

        //流程调用
        WorkCondition condition = new WorkCondition();
        condition.setYwlsh(ywlsh);
        condition.setCzy(tokenContext.getUserInfo().getCZY());
        condition.setYwwd(tokenContext.getUserInfo().getYWWD());
        condition.setStatus("新建");
        condition.setEvent("提交");
        condition.setZtlx("1");
        condition.setYwwd(unitPayWrongPost.getYWWD());
        condition.setType(BusinessType.Collection);
        condition.setSubType(busType);    //错缴
        doWork(condition);

        //发送数据到结算平台
        sendPayWrongMsg(ywlsh, dwzh);

        AddUnitPayWrongPostRes result = new AddUnitPayWrongPostRes();
        result.setStatus("success");
        result.setYWLSH(ywlsh);
        return result;
    }

    private void checkNewAdd(UnitPayWrongPost unitPayWrongPost) {
        if (ComUtils.isEmpty(unitPayWrongPost)) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "");
        } else if (ComUtils.isEmpty(unitPayWrongPost.getYWLSH())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "业务流水号");
        } else if (ComUtils.isEmpty(unitPayWrongPost.getDWZH())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "单位账号");
        } else if (ComUtils.isEmpty(unitPayWrongPost.getJCGZNY())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "缴存更正年月");
        } else if (ComUtils.isEmpty(unitPayWrongPost.getCZY())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "操作员");
        } else if (ComUtils.isEmpty(unitPayWrongPost.getYWWD())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "业务网点");
        }
    }

    private void sendPayWrongMsg(String ywlsh, String dwzh) {

        HashMap map = new HashMap();
        map.put("ywlsh", ywlsh);
        map.put("fse", new BigDecimal("1"));
        map.put("dwzh", dwzh);
        trader.sendPayWrongMSg(map);

    }

    @Override
    public ReUnitPayWrongPutRes putUnitPayWrong(TokenContext tokenContext, String ywlsh, UnitPayWrongPut unitPayWrongPut) {
        ArrayList<Exception> exceptions = new ArrayList<Exception>();
        StCollectionUnitBusinessDetails collectionUnitBusinessDetails = instance(collectionUnitBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{//获取单位明细信息
            this.put("ywlsh", ywlsh);
            // this.put("cCollectionUnitBusinessDetailsExtension.ywzt", "01");
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        Date jcgzny = ComUtils.parseToDate(unitPayWrongPut.getJCGZNY(), "yyyy-MM");
        if (collectionUnitBusinessDetails == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "该业务(" + ywlsh + ")不存在");
        }
        // collectionUnitBusinessDetails.setDwzh(unitPayWrongPut.getDWZH());
        collectionUnitBusinessDetails.getExtension().setCzy(unitPayWrongPut.getCZY());//操作人
        collectionUnitBusinessDetails.getExtension().setBlzl(unitPayWrongPut.getBLZL());//办理资料
        CAccountNetwork network = cAccountNetworkDAO.get(tokenContext.getUserInfo().getYWWD());
        collectionUnitBusinessDetails.getExtension().setYwwd(network);//业务网点

        //// TODO: 2017/7/28 根据缴存更正年月自动获取更正信息
        collectionUnitBusinessDetails.getUnitPayWrongVice().setQcqrdh("");//清册确认单号
        collectionUnitBusinessDetails.getUnitPayWrongVice().setJcgzny(jcgzny);//缴存更正年月
        collectionUnitBusinessDetails.getUnitPayWrongVice().setSkyhzh(unitPayWrongPut.getSKYHZH());//收款银行账户
        collectionUnitBusinessDetails.getUnitPayWrongVice().setSkyhhm(unitPayWrongPut.getSKYHHM());//收款银行户名
        collectionUnitBusinessDetails.getUnitPayWrongVice().setSkyhdm(unitPayWrongPut.getSKYHDM());//收款银行代码
        collectionUnitBusinessDetails.getUnitPayWrongVice().setSkyhmc(unitPayWrongPut.getSKYHMC());

        return new ReUnitPayWrongPutRes() {{
            this.setStatus("sucess");
            this.setYWLSH(collectionUnitBusinessDetails.getYwlsh());
        }};
    }

    /**
     * 获取错缴信息
     */
    @Override
    public GetUnitPayWrongRes getUnitPayWrong(TokenContext tokenContext, String ywlsh) {

        CCollectionUnitPayWrongVice payWrong = payWrongDAO.getByYwlsh(ywlsh);
        StCollectionUnitBusinessDetails dwywmx = payWrong.getDwywmx();
        CCollectionUnitBusinessDetailsExtension extension = dwywmx.getExtension();

        GetUnitPayWrongRes view = new GetUnitPayWrongRes();
        view.setDWZH(dwywmx.getDwzh());
        view.setDWMC(dwywmx.getUnit().getDwmc());
        view.setJCGZNY(ComUtils.parseToString(payWrong.getJcgzny(), "yyyy-MM"));

        //TODO
        view.setSKYHHM(payWrong.getSkyhhm());
        view.setSKYHMC(payWrong.getSkyhmc());
        view.setSKYHZH(payWrong.getSkyhzh());
        view.setSKYHDM(payWrong.getSkyhdm());

        view.setJBRXM(extension.getJbrxm());
        view.setJBRZJLX(extension.getJbrzjlx());
        view.setJBRZJHM(extension.getJbrzjhm());
        view.setCZY(extension.getCzy());
        view.setYWWD(extension.getYwwd().getMingCheng());
        String blzl = extension.getBlzl();
        if (ComUtils.isEmpty(extension.getBlzl())) {
            blzl = "";
        }
        view.setBLZL(blzl);

        Set<CCollectionUnitPayWrongDetailVice> cjxq = payWrong.getCjxq();
        ArrayList<GetUnitPayWrongResGZXX> gzlist = new ArrayList<GetUnitPayWrongResGZXX>();
        BigDecimal dwcjehj = BigDecimal.ZERO;
        BigDecimal grcjehj = BigDecimal.ZERO;
        BigDecimal fsehj;
        for (CCollectionUnitPayWrongDetailVice vice : cjxq) {
            GetUnitPayWrongResGZXX gzxx = new GetUnitPayWrongResGZXX();
            String grzh = vice.getGrzh();
            StCommonPerson person = commonPersonDAO.getByGrzh(grzh);
            gzxx.setXingMing(person.getXingMing());
            gzxx.setGRZH(grzh);
            gzxx.setZJHM(person.getZjhm());
            gzxx.setDWCJJE(vice.getDwcjje() + "");
            gzxx.setGRCJJE(vice.getGrcjje() + "");
            gzxx.setFSE(vice.getDwcjje().add(vice.getGrcjje()) + "");
            gzxx.setCJYY(vice.getCjyy());
            gzlist.add(gzxx);

            dwcjehj = dwcjehj.add(vice.getDwcjje());
            grcjehj = grcjehj.add(vice.getGrcjje());
        }
        fsehj = dwcjehj.add(grcjehj);

        view.setGZXX(gzlist);
        GetUnitPayWrongResCJEHJ cjehj = new GetUnitPayWrongResCJEHJ();
        cjehj.setFSEHJ(fsehj + "");
        cjehj.setDWCJEHJ(dwcjehj + "");
        cjehj.setGRCJEHJ(grcjehj + "");
        view.setCJEHJ(cjehj);

        return view;
    }

    @Override
    public CommonResponses headUnitPayWrong(String ywlsh) {
        CCollectionUnitPayWrongVice payWrong = payWrongDAO.getByYwlsh(ywlsh);
        if (payWrong == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "该业务(" + ywlsh + ")不存在");
        }
        StCollectionUnitBusinessDetails dwywmx = payWrong.getDwywmx();
        CCollectionUnitBusinessDetailsExtension extension = dwywmx.getExtension();
        if (!CollectionBusinessStatus.已入账.getName().equals(extension.getStep())) {
            throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "该业务未办结，已入账的业务才能打印回执单");
        }
        HeadUnitPayWrongReceiptRes view = new HeadUnitPayWrongReceiptRes();
        view.setYWLSH(dwywmx.getYwlsh());
        view.setDWZH(dwywmx.getDwzh());
        view.setDWMC(dwywmx.getUnit().getDwmc());
        view.setJCGZNY(ComUtils.parseToString(payWrong.getJcgzny(), "yyyy-MM"));
        //TODO
        view.setSKYHHM(payWrong.getSkyhhm());
        view.setSKYHMC(payWrong.getSkyhmc());
        view.setSKYHDM(payWrong.getSkyhdm());
        view.setSKYHZH(payWrong.getSkyhzh());
        view.setYWWD(extension.getYwwd().getMingCheng());
        view.setJBRXM(extension.getJbrxm());
        view.setJBRZJLX(extension.getJbrzjlx());
        view.setJBRZJHM(extension.getJbrzjhm());
        view.setCZY(extension.getCzy());
        view.setYZM("");
        view.setTZSJ(ComUtils.parseToString(new Date(), "yyyy-MM-dd"));

        Set<CCollectionUnitPayWrongDetailVice> cjxq = payWrong.getCjxq();
        ArrayList<HeadUnitPayWrongReceiptResGZXX> gzlist = new ArrayList<HeadUnitPayWrongReceiptResGZXX>();
        BigDecimal dwcjehj = BigDecimal.ZERO;
        BigDecimal grcjehj = BigDecimal.ZERO;
        BigDecimal fsehj;
        for (CCollectionUnitPayWrongDetailVice vice : cjxq) {
            HeadUnitPayWrongReceiptResGZXX gzxx = new HeadUnitPayWrongReceiptResGZXX();
            String grzh = vice.getGrzh();
            StCommonPerson person = commonPersonDAO.getByGrzh(grzh);
            gzxx.setXingMing(person.getXingMing());
            gzxx.setGRZH(grzh);
            gzxx.setZJHM(person.getZjhm());
            gzxx.setDWCJJE(vice.getDwcjje() + "");
            gzxx.setGRCJJE(vice.getGrcjje() + "");
            gzxx.setFSE(vice.getDwcjje().add(vice.getGrcjje()) + "");
            gzxx.setCJYY(vice.getCjyy());
            gzlist.add(gzxx);
            dwcjehj = dwcjehj.add(vice.getDwcjje());
            grcjehj = grcjehj.add(vice.getGrcjje());
        }
        fsehj = dwcjehj.add(grcjehj);

        view.setGZXX(gzlist);
        HeadUnitPayWrongReceiptResCJEHJ cjehj = new HeadUnitPayWrongReceiptResCJEHJ();
        cjehj.setFSEHJ(fsehj + "");
        cjehj.setDWCJEHJ(dwcjehj + "");
        cjehj.setGRCJEHJ(grcjehj + "");
        view.setCJEHJ(cjehj);
        String id = pdfService.getUnitPayWrongPdf(view);
        System.out.println("生成id的值：" + id);
        CommonResponses commonResponses = new CommonResponses();
        commonResponses.setId(id);
        commonResponses.setState("success");
        return commonResponses;
    }

    //根据单位账号与缴存更正年月自动获得错缴个人账户列表
    @Override
    public HeadUnitPayWrongReceiptRes autoGetPayWrong(String dwzh, String jcgzny) {

        throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "该接口已弃用，请联系管理员");

    }

    /**
     * 错缴办结
     * 1、单位信息更新
     * 2、个人信息更新
     */
    @Override
    public void doUnitPayWrong(String ywlsh) {
        CCollectionUnitPayWrongVice payWrong = payWrongDAO.getByYwlsh(ywlsh);

        //流程调用
        WorkCondition condition = new WorkCondition();
        condition.setYwlsh(ywlsh);
        condition.setCzy(payWrong.getDwywmx().getExtension().getCzy());
        condition.setYwwd(payWrong.getDwywmx().getExtension().getYwwd().getId());
        condition.setStatus(CollectionBusinessStatus.待入账.getName());
        condition.setEvent("入账成功");
        condition.setZtlx("1");
        condition.setType(BusinessType.Collection);
        condition.setSubType(busType);    //错缴
        doWork(condition);
    }


    /**
     * 办结操作
     * 假定员工账户上金额足够
     */
    @Override
    public void doFinal(String ywlsh) {
        CCollectionUnitPayWrongVice payWrong = payWrongDAO.getByYwlsh(ywlsh);
        StCollectionUnitBusinessDetails dwywmx = payWrong.getDwywmx();
        CCollectionUnitBusinessDetailsExtension extension = dwywmx.getExtension();
        BigDecimal fse = payWrong.getFse();
        String dwzh = dwywmx.getDwzh();
        //1、职工账户处理
        Set<CCollectionUnitPayWrongDetailVice> cjxq = payWrong.getCjxq();
        for (CCollectionUnitPayWrongDetailVice detail : cjxq) {
            String grzh = detail.getGrzh();
            BigDecimal cjje = detail.getDwcjje().add(detail.getGrcjje());
            StCommonPerson person = commonPersonDAO.getByGrzh(grzh);
            StCollectionPersonalAccount personalAccount = person.getCollectionPersonalAccount();
            BigDecimal grzhye = personalAccount.getGrzhye();
            grzhye = grzhye.subtract(cjje);
            AssertUtils.isPositiveNnumber(grzhye, "个人账户余额不能小于0！");

            personalAccount.setGrzhye(grzhye);  //TODO 小于0怎么办！以及利息如何计算
            commonPersonDAO.update(person);     //违反迪米特法则,但两者也可以看做一个整体

            createPersonBus(grzh, cjje, payWrong, detail);
        }

        //单位账户余额更新
        StCommonUnit unit = commonUnitDAO.getUnit(dwzh);
        StCollectionUnitAccount unitAccount = unit.getCollectionUnitAccount();
        CCollectionUnitAccountExtension accountExtension = unitAccount.getExtension();
        BigDecimal zsye = accountExtension.getZsye();
        zsye = zsye.add(fse);
        accountExtension.setZsye(zsye);

        BigDecimal dwzhye = unitAccount.getDwzhye();
        dwzhye = dwzhye.subtract(fse);
        unitAccount.setDwzhye(dwzhye);
        commonUnitDAO.update(unit);

        //2、单位账户更新处理:单位缴存人数、单位封存人数、单位账户余额
        BusUtils.refreshUnitAcount(dwzh);

        String ywwd = extension.getYwwd().getMingCheng();
        String czy = extension.getCzy();
        //财务入账
        String zaiYao = unit.getDwmc() + ComUtils.parseToString(payWrong.getJcgzny(), "yyyyMM") + "错缴";
        VoucherRes voucherRes = voucherManagerService.addVoucher(ywwd, czy, czy, "", "",
                VoucherBusinessType.错缴.getCode(), VoucherBusinessType.错缴.getCode(), ywlsh, zaiYao, fse, null, null);

        if (ComUtils.isEmpty(voucherRes.getJZPZH())) {
            throw new ErrorException(voucherRes.getMSG());
        }


        CFinanceRecordUnit cFinanceRecordUnit = new CFinanceRecordUnit();
        cFinanceRecordUnit.setFse(fse);
        cFinanceRecordUnit.setJzpzh(voucherRes.getJZPZH());
        cFinanceRecordUnit.setRemark("错缴");
        cFinanceRecordUnit.setSummary(zaiYao);
        cFinanceRecordUnit.setZjly(WFTLY.错缴.getName());
        cFinanceRecordUnit.setDwzh(dwzh);
        cFinanceRecordUnit.setWftye(accountExtension.getZsye());
        icFinanceRecordUnitDAO.save(cFinanceRecordUnit);

        dwywmx.setJzrq(new Date());
        extension.setJzpzh(voucherRes.getJZPZH());
        payWrongDAO.save(payWrong);
    }

    @Override
    public PageResNew<ListUnitPayWrongResRes> getUnitPayWrongList(TokenContext tokenContext, String dwmc, String dwzh, String ywzt, String czy, String kssj, String jssj, String marker, String pageSize, String action) {

        //参数验证
        ListAction listAction = ListAction.pageType(action);
        int pagesize = ComUtils.parstPageSize(pageSize);
        Date startDate = ComUtils.parseToDate(kssj, "yyyy-MM-dd HH:mm");
        Date endDate = ComUtils.parseToDate(jssj, "yyyy-MM-dd HH:mm");

        String ywwd = tokenContext.getUserInfo().getYWWD();
        IBaseDAO.CriteriaExtension ce = getCriteriaExtension(dwmc, dwzh, ywzt, czy, ywwd);

        PageResults<CCollectionUnitPayWrongVice> pageResults = null;
        try {
            pageResults = payWrongDAO.listWithMarker(null, startDate, endDate, "created_at", Order.DESC, null, SearchOption.REFINED, marker, pagesize, listAction, ce);
        } catch (Exception e) {
            throw new ErrorException(e);
        }

        PageResNew<ListUnitPayWrongResRes> result = new PageResNew<>();
        ArrayList<ListUnitPayWrongResRes> list = getResultList(pageResults.getResults());
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

    private ArrayList<ListUnitPayWrongResRes> getResultList(List<CCollectionUnitPayWrongVice> results) {

        ArrayList<ListUnitPayWrongResRes> resultList = new ArrayList<>();
        for (CCollectionUnitPayWrongVice rowSource : results) {
            ListUnitPayWrongResRes rowView = new ListUnitPayWrongResRes();
            StCollectionUnitBusinessDetails dwywmx = rowSource.getDwywmx();
            CCollectionUnitBusinessDetailsExtension extension = dwywmx.getExtension();
            rowView.setYWLSH(dwywmx.getYwlsh());
            rowView.setDWMC(dwywmx.getUnit().getDwmc());
            rowView.setDWZH(dwywmx.getDwzh());
            rowView.setSLSJ(ComUtils.parseToString(dwywmx.getCreated_at(), "yyyy-MM-dd HH:mm"));
            rowView.setFSE(dwywmx.getFse() + "");
            rowView.setJCGZNY(ComUtils.parseToString(rowSource.getJcgzny(), "yyyy-MM"));
            rowView.setYWZT(extension.getStep());
            rowView.setJZPZH(extension.getJzpzh());
            rowView.setCZY(extension.getCzy());
            rowView.setYWWD(extension.getYwwd().getMingCheng());
            rowView.setId(rowSource.getId());
            resultList.add(rowView);
        }

        return resultList;
    }

    private void createPersonBus(String grzh, BigDecimal fse, CCollectionUnitPayWrongVice payWrong, CCollectionUnitPayWrongDetailVice detail) {

        StCollectionPersonalBusinessDetails personalBusiness = new StCollectionPersonalBusinessDetails();
        StCommonPerson person = commonPersonDAO.getByGrzh(grzh);
        StCommonUnit unit = person.getUnit();

        personalBusiness.setFse(fse.negate());  //注意：错缴的个人fse为负数
        personalBusiness.setUnit(unit);
        personalBusiness.setGrzh(grzh);
        personalBusiness.setPerson(person);
        personalBusiness.setGjhtqywlx(CollectionBusinessType.其他.getCode());
        personalBusiness.setJzrq(new Date());

        CCollectionPersonalBusinessDetailsExtension extension = new CCollectionPersonalBusinessDetailsExtension();
        extension.setCzmc(ywlx);
        extension.setStep("办结");
        extension.setBjsj(new Date());
        CCollectionUnitBusinessDetailsExtension unitBusExtension = payWrong.getDwywmx().getExtension();
        extension.setYwwd(unitBusExtension.getYwwd());
        extension.setJbrxm(unitBusExtension.getJbrxm());
        extension.setJbrzjlx(unitBusExtension.getJbrzjlx());
        extension.setJbrzjhm(unitBusExtension.getJbrzjhm());

        BigDecimal dwyjce = detail.getDwcjje();
        BigDecimal gryjce = detail.getGrcjje();
        String jcgzny = ComUtils.parseToString(payWrong.getJcgzny(), "yyyyMM");
        BigDecimal grzhye = person.getCollectionPersonalAccount().getGrzhye();

        extension.setDwfse(dwyjce);
        extension.setGrfse(gryjce);
        extension.setDqye(grzhye);
        extension.setFsny(jcgzny);

        personalBusiness.setExtension(extension);

        personalBusinessDAO.save(personalBusiness);
    }

    @Override
    public void checkSealCreateWrong(TokenContext tokenContext, String ywlsh) {

        CCollectionIndividualAccountActionVice sealBus = collectionIndividualAccountActionViceDAO.getByYWLSH(ywlsh);
        String grzh = sealBus.getGrzh();
        String dwzh = sealBus.getDwzh();
        CCollectionUnitDepositInventoryVice nearLyInventory = unitDepositInventoryViceDAO.getNearLyInventory(dwzh);
        //如果封存生效月份大于最近的一次清册月份，则不会产生错缴数据，如果小于等于，则代表对过去缴存的某一月或几月进行修正
        //钱退回有一定风险,单位也没有权限随意退回员工的钱,在办理资料这里严格点
        if (nearLyInventory == null) {
            return;
        }
        Date sxny = ComUtils.parseToDate(sealBus.getSxny(), "yyyyMM");
        String qcny = nearLyInventory.getQcny();
        Date lastqcny = ComUtils.parseToDate(qcny, "yyyyMM");
        if (sxny.compareTo(lastqcny) <= 0) {
            //inventoryCheck(grzh, dwzh, sxny, lastqcny);
            doCreatePayWrong(tokenContext, grzh, dwzh, sxny, lastqcny);
        }
    }

    /**
     * 封存时检查清册
     */
    private void inventoryCheck(String grzh, String dwzh, Date sxny, Date lastqcny) {
        Date sxyf = (Date) sxny.clone();
        //从sxny往后检查最新清册
        while (sxyf.compareTo(lastqcny) <= 0) {
            LatestInventory latestInventory = inventory.getLatestInventoryOfMonth(dwzh, sxyf);
            //如果个人在清册中不存在，或账户状态不为正常，抛出异常信息
            //checkLatestInventory(grzh,latestInventory);
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
                if ("01".equals(detail.getGRZHZT())) {
                    return;
                }
                throw new ErrorException(ReturnEnumeration.Account_NOT_MATCH, "该职工的最新清册(" + qcny + ")为封存状态，不能办理封存");
            }
        }
        throw new ErrorException(ReturnEnumeration.Data_MISS, "单位清册(" + qcny + ")中没有该职工");
    }

    private void doCreatePayWrong(TokenContext tokenContext, String grzh, String dwzh, Date sxny, Date lastqcny) {
        //产生多个月的错缴数据，从生效年月到清册月份这个月
        Date createMonth = sxny;
        while (createMonth.compareTo(lastqcny) <= 0) {
            String cjny = ComUtils.parseToString(createMonth, "yyyyMM");
            //获取该人该月的缴存信息合计
            Object[] obj = personalBusinessDAO.getPersonDepositSUM(grzh,cjny);
            if(obj != null && obj[3] != null && BigDecimal.ZERO.compareTo((BigDecimal) obj[3]) < 0){
                doCreatePayWrong2(tokenContext, dwzh, grzh, createMonth, obj);
            }
            createMonth = ComUtils.getNextMonth(createMonth);
        }
    }

    private void doCreatePayWrong2(TokenContext tokenContext, String dwzh, String grzh, Date cjgzny, Object[] obj) {
        //新增
        String ywlsh = payWrongNewAdd(tokenContext, dwzh, grzh, cjgzny, obj);

        BusUtils.saveAuditHistory(ywlsh, "错缴", "办结");

        //办结
        doFinal(ywlsh);

    }


    /**
     * 直接办结处理
     */
    private String payWrongNewAdd(TokenContext tokenContext, String dwzh, String grzh, Date cjgzny, Object[] obj) {
        boolean flag = payWrongDAO.isExistDoing(dwzh, cjgzny);
        if (flag) {
            throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "所属单位有未办结的错缴业务，该笔业务将产生新的错缴记录，需等待上笔业务办结之后才能进行");
        }
        //查看单位该月是否存在新建的错缴信息
        BigDecimal dwcje = (BigDecimal) obj[1];
        BigDecimal grcje = (BigDecimal) obj[2];
        BigDecimal grfse = (BigDecimal) obj[3];

        //错缴额是否小于个人账户余额
        checkPersonAccount(grzh, grfse);

        //新建一笔错缴业务
        CCollectionUnitPayWrongVice newPayWrong = new CCollectionUnitPayWrongVice();
        StCommonUnit unit = commonUnitDAO.getUnit(dwzh);

        newPayWrong.setJcgzny(cjgzny);

        StSettlementSpecialBankAccount bankAcount = BusUtils.getBankAcount(unit.getStyhdm(), "01");
        newPayWrong.setSkyhmc(unit.getStyhmc());
        newPayWrong.setSkyhdm(unit.getStyhdm());
        newPayWrong.setSkyhzh(bankAcount.getYhzhhm());
        newPayWrong.setSkyhhm(bankAcount.getYhzhmc());
        newPayWrong.setFse(grfse);

        StCollectionUnitBusinessDetails dwywmx = new StCollectionUnitBusinessDetails();

        dwywmx.setDwzh(dwzh);
        dwywmx.setFsrs(new BigDecimal("1"));
        dwywmx.setFse(grfse);
        dwywmx.setYwmxlx(CollectionBusinessType.其他.getCode());

        CCollectionUnitBusinessDetailsExtension extension = new CCollectionUnitBusinessDetailsExtension();
        extension.setStep(CollectionBusinessStatus.已入账.getName());
        extension.setCzmc(ywlx);
        extension.setSlsj(new Date());
        extension.setBjsj(new Date());
        extension.setJbrxm(unit.getJbrxm());
        extension.setJbrzjhm(unit.getJbrzjhm());
        extension.setJbrzjlx(unit.getJbrzjlx());

        CAccountNetwork cAccountNetwork = cAccountNetworkDAO.get(tokenContext.getUserInfo().getYWWD());
        extension.setYwwd(cAccountNetwork);
        extension.setCzy(tokenContext.getUserInfo().getCZY());

        dwywmx.setExtension(extension);
        dwywmx.setUnit(unit);
        newPayWrong.setDwywmx(dwywmx);
        dwywmx.setUnitPayWrongVice(newPayWrong);

        Set<CCollectionUnitPayWrongDetailVice> detailSet = new HashSet<CCollectionUnitPayWrongDetailVice>();
        CCollectionUnitPayWrongDetailVice detail = new CCollectionUnitPayWrongDetailVice();
        detail.setGrzh(grzh);
        detail.setDwcjje(dwcje);
        detail.setGrcjje(grcje);
        detail.setCjyy("封存产生错缴");
        detailSet.add(detail);
        newPayWrong.setCjxq(detailSet);

        payWrongDAO.save(newPayWrong);

        return dwywmx.getYwlsh();
    }

    private void checkPersonAccount(String grzh, BigDecimal grfse) {
        StCommonPerson person = commonPersonDAO.getByGrzh(grzh);
        StCollectionPersonalAccount personalAccount = person.getCollectionPersonalAccount();
        BigDecimal grzhye = personalAccount.getGrzhye();
        if (grzhye.compareTo(grfse) < 0) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "当前余额不足，职工姓名为" + person.getXingMing() + "，个人账号为" + grzh + "，个人账户余额为" + grzhye + "，错缴发生额为" + grfse);
        }
    }

}
