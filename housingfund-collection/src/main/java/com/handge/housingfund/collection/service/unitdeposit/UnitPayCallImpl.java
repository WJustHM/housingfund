package com.handge.housingfund.collection.service.unitdeposit;

import com.handge.housingfund.collection.utils.BusUtils;
import com.handge.housingfund.collection.utils.ComUtils;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.account.model.UserInfo;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessStatus;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.collection.model.deposit.*;
import com.handge.housingfund.common.service.collection.service.unitdeposit.UnitDepositInventory;
import com.handge.housingfund.common.service.collection.service.unitdeposit.UnitPayCall;
import com.handge.housingfund.common.service.loan.model.CommonResponses;
import com.handge.housingfund.common.service.others.IDictionaryService;
import com.handge.housingfund.common.service.others.IPdfService;
import com.handge.housingfund.common.service.others.model.SingleDictionaryDetail;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.ListAction;
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;


/**
 * Created by 向超 on 2017/7/25.
 */
@Component
public class UnitPayCallImpl implements UnitPayCall {
    @Autowired
    private IStCommonUnitDAO commonUnitDAO;
    @Autowired
    private IStCollectionUnitBusinessDetailsDAO collectionUnitBusinessDetailsDAO;
    @Autowired
    private ICCollectionUnitPayCallViceDAO payCallDAO;
    @Autowired
    private IStCollectionUnitAccountDAO collectionUnitAccountDAO;
    @Autowired
    private IPdfService pdfService;
    @Autowired
    private UnitDepositInventory depositInventory;
    @Autowired
    private IDictionaryService iDictionaryService;

    @Autowired
    private ICAccountNetworkDAO cAccountNetworkDAO;

    private static String format = "yyyy-MM-dd HH:mm";


    @Override
    public PageRes<ListUnitPayCallResRes> getUnitPayCallInfo(TokenContext tokenContext, String DWMC, String DWZH, String page, String pagesize, String kssjStr, String jssjStr) {

        PageRes pageRes = new PageRes();
        int page_number = ComUtils.parstPageNo(page);
        int pagesize_number = ComUtils.parstPageSize(pagesize);
        Date kssj = getKssj(kssjStr);
        Date jssj = getJssj(jssjStr);

        String ywwd = tokenContext.getUserInfo().getYWWD();
        IBaseDAO.CriteriaExtension ce =  getCriteriaExtension(DWMC,DWZH,ywwd);

        List<CCollectionUnitPayCallVice> list = DAOBuilder.instance(payCallDAO)
                .searchFilter(new HashMap<>())
                .betweenDate(kssj, jssj)
                .extension(ce)
                .pageOption(pageRes, pagesize_number, page_number)
                .searchOption(SearchOption.FUZZY)
                .getList(
        new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (list == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应的业务");
        }
        return new PageRes<ListUnitPayCallResRes>() {{
            this.setResults(new ArrayList<ListUnitPayCallResRes>() {{
                for (CCollectionUnitPayCallVice payCallVice : list) {
                    StCollectionUnitBusinessDetails dwywmx = payCallVice.getDwywmx();

                    this.add(new ListUnitPayCallResRes() {{
                        this.setYWLSH(dwywmx.getYwlsh());//业务流水号
                        this.setDWMC(dwywmx.getUnit().getDwmc());//单位名称
                        this.setDWZH(dwywmx.getUnit().getDwzh());//单位账号
                        this.setJBRXM(dwywmx.getUnit().getJbrxm());//经办人姓名
                        this.setJBRGDDH(dwywmx.getUnit().getJbrgddhhm());//经办人固定电话
                        this.setJBRSJHM(dwywmx.getUnit().getJbrsjhm());//经办人手机号码
                        this.setYHJNY(DateUtil.dateStrTransform(payCallVice.getYjny()));//应缴年月
                        this.setFSRS(dwywmx.getFsrs().intValue());//发生人数
                        this.setFSE(dwywmx.getFse()+"");//发生额
                        this.setZDCJ(payCallVice.getZdcjxx());//自动催缴
                        this.setSDCJ(String.valueOf(payCallVice.getCjcs()));//手动催缴次数
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
    public ListUnitPayCallHistoryRes getUnitPayCallHistoryInfo(String YWLSH) {

        CCollectionUnitPayCallVice payCall = payCallDAO.getPayCall(YWLSH);
        if (payCall == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应的业务");
        }
        StCollectionUnitBusinessDetails dwywmx = payCall.getDwywmx();
        final List<CCollectionUnitPayCallDetailVice> cjxq = payCallDAO.getPayCallDetail(YWLSH);
        return new ListUnitPayCallHistoryRes() {{
            this.setRes(new ArrayList<ListUnitPayCallHistoryResRes>() {{
                for (CCollectionUnitPayCallDetailVice detailVice : cjxq)
                    this.add(new ListUnitPayCallHistoryResRes() {{
                        this.setCJR(detailVice.getCjr());
                        this.setCJSJ(DateUtil.date2Str(detailVice.getCjsj(), format));
                        this.setCJFS(detailVice.getCjfs());
                        this.setDWMC(dwywmx.getUnit().getDwmc());
                        this.setDWZH(dwywmx.getDwzh());
                    }});
            }});
        }};
    }

    @Override
    public AddUnitPayCallRes addUnitPayCall(TokenContext tokenContext, UnitPayCallPost unitPayCallPosts) {
        ArrayList<String> ywlshjh = unitPayCallPosts.getBatchSubmission().getYWLSHJH();
        for (String ywlsh : ywlshjh) {
            CCollectionUnitPayCallVice payCall = payCallDAO.getPayCall(ywlsh);
            if (payCall == null) {
                throw new ErrorException("业务流水号：" + ywlsh + "对应的催缴业务不存在！");
            }
            StCollectionUnitBusinessDetails dwywmx = payCall.getDwywmx();
            Date jzny = null;
            try {
                jzny = DateUtil.str2Date("yyyyMM", dwywmx.getUnit().getCollectionUnitAccount().getJzny());
            } catch (ParseException e) {
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "缴至年月格式不匹配");
            }
            String yjnyStr = payCall.getYjny();
            Date yjny = ComUtils.parseToDate(yjnyStr, "yyyyMM");
            if (jzny.getTime() >= yjny.getTime()) {
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "该公司当月已汇缴,业务流水号为:" + ywlsh);
            }

            Set<CCollectionUnitPayCallDetailVice> payCallDetail = payCall.getCjxq();
            CCollectionUnitPayCallDetailVice detail = new CCollectionUnitPayCallDetailVice();
            detail.setCjfs(unitPayCallPosts.getCJFS());
            detail.setCjr(unitPayCallPosts.getCJR());
            detail.setCjsj(new Date());
            detail.setZdcj(getPayCallMessage());
            payCallDetail.add(detail);

            Integer cjcs = payCall.getCjcs();
            payCall.setCjcs(++cjcs);
            payCallDAO.save(payCall);

            Message.sendMessage("18244259758", "验证码123456，五舟汉云");
        }
        return new AddUnitPayCallRes() {{
            this.setStatus("sucess");
        }};
    }

    @Override
    public CommonResponses headUnitPayCall(TokenContext tokenContext, String ywlsh) {

        CCollectionUnitPayCallVice payCall = payCallDAO.getPayCall(ywlsh);
        StCollectionUnitBusinessDetails dwywmx = payCall.getDwywmx();
        if (dwywmx == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应的业务");
        }
        HeadUnitPayCallReceiptRes headUnitPayCallReceiptRes = new HeadUnitPayCallReceiptRes();
        ArrayList CJJLArrayList = new ArrayList<HeadUnitPayCallReceiptResCJJL>();

        headUnitPayCallReceiptRes.setYWLSH(ywlsh);//业务流水号
        headUnitPayCallReceiptRes.setTZSJ(ComUtils.parseToString(new Date(), "yyyy-MM-dd HH:mm"));//填制时间
        headUnitPayCallReceiptRes.setDWMC(dwywmx.getUnit().getDwmc());//单位名称
        headUnitPayCallReceiptRes.setDWZH(dwywmx.getUnit().getDwzh());//单位账号
        headUnitPayCallReceiptRes.setJBRXM(dwywmx.getUnit().getJbrxm());//经办人姓名
        headUnitPayCallReceiptRes.setJBRGDDHHM(dwywmx.getUnit().getJbrgddhhm());//经办人固定电话号码
        headUnitPayCallReceiptRes.setJBRSJHM(dwywmx.getUnit().getJbrsjhm());//经办人手机号码
        headUnitPayCallReceiptRes.setCZY(dwywmx.getExtension().getCzy());//操作员
        headUnitPayCallReceiptRes.setYWWD(dwywmx.getExtension().getYwwd().getMingCheng());//业务网点
        //headUnitPayCallReceiptRes.setYZM("验证码123");
        List<CCollectionUnitPayCallDetailVice> cjxq = payCallDAO.getPayCallDetail(ywlsh);
        for (CCollectionUnitPayCallDetailVice detailVice : cjxq) {
            HeadUnitPayCallReceiptResCJJL headUnitPayCallReceiptResCJJL = new HeadUnitPayCallReceiptResCJJL();
            headUnitPayCallReceiptResCJJL.setFSE(dwywmx.getFse().toPlainString());//发生额
            headUnitPayCallReceiptResCJJL.setFSRS(dwywmx.getFsrs().intValue());//发生人数
            headUnitPayCallReceiptResCJJL.setZDCJ(payCall.getZdcjxx());//自动催缴
            headUnitPayCallReceiptResCJJL.setCJR(detailVice.getCjr());//催缴人
            headUnitPayCallReceiptResCJJL.setCZY(detailVice.getCjr());
            String yhjny = DateUtil.getNextMonth(dwywmx.getUnit().getCollectionUnitAccount().getJzny());
            if (StringUtil.notEmpty(yhjny))
                headUnitPayCallReceiptResCJJL.setYHJNY(DateUtil.dateStrTransform(yhjny));//应汇缴年月
            if (!StringUtil.isEmpty(detailVice.getCjfs())) {
                SingleDictionaryDetail cjfs_info = iDictionaryService.getSingleDetail(detailVice.getCjfs(), "RemitExpedite");
                headUnitPayCallReceiptResCJJL.setCJFS(cjfs_info != null ? cjfs_info.getName() : "");//催缴方式
            }
            headUnitPayCallReceiptResCJJL.setCJSJ(DateUtil.date2Str(detailVice.getCjsj(), format));//催缴时间
            CJJLArrayList.add(headUnitPayCallReceiptResCJJL);
        }

        headUnitPayCallReceiptRes.setCJJL(CJJLArrayList);
        String id = pdfService.getUnitPayCallPdf(headUnitPayCallReceiptRes);
        System.out.println("生成id的值：" + id);
        CommonResponses commonResponses = new CommonResponses();
        commonResponses.setId(id);
        commonResponses.setState("success");
        return commonResponses;
    }

    /**
     * 催缴生成：
     * 查询单位上个月是否缴款款
     * 同一单位可能产生多月的催缴
     */
    @Override
    public void doCreateUnitPayCall() {
        //1、查询所有单位(单位账户状态为正常),缴至年月小于当前月份的上个月
        Date nowMonth = ComUtils.getfirstDayOfMonth(new Date());
        String nowDate = ComUtils.parseToString(nowMonth, "yyyyMM");

        List<StCommonUnit> units = payCallDAO.getUnitNeedCall2(nowDate);
        List<Object[]> payCallMags = payCallDAO.getPayCallMag();

        String zdcj = getPayCallMessage();  //自动催缴信息
        //2、依次查询单位上个月是否缴款
        int count = 0;

        int i=0, j = 0;
        int size1 = units.size();
        int size2 = payCallMags.size();
        while(i< size1 && j < size2){
            StCommonUnit unit = units.get(i);
            String dwzh = unit.getDwzh();
            Object[] obj = payCallMags.get(j);
            String dwzh2 = (String)obj[0];
            if (dwzh.equals(dwzh2)){
                String cjzny = (String)obj[1];
                Date creatMonth = getCreatMonth(unit,cjzny);
                doCreateUnitPayCall2(creatMonth,nowMonth,unit,zdcj);
                if(++count % 30 == 0){
                    payCallDAO.flush();
                }
                i++;j++;
            }else if(dwzh.compareTo(dwzh2) < 0){
                i++;
            }else{
                j++;
            }
        }
    }

    private Date getCreatMonth(StCommonUnit unit, String cjzny) {

        String dwyhjny = BusUtils.getDWYHJNY(unit);
        Date yhjny = ComUtils.parseToDate(dwyhjny, "yyyy-MM");

        if(ComUtils.isEmpty(cjzny)) {
            return yhjny;
        }

        String nextMonth = ComUtils.getNextMonth(ComUtils.parseToYYYYMM2(cjzny));
        Date createMonth = ComUtils.parseToDate(nextMonth,"yyyy-MM");

        return createMonth.compareTo(yhjny) >= 0 ? createMonth : yhjny ;
    }

    private void doCreateUnitPayCall2(Date creatMonth, Date nowMonth,StCommonUnit unit,String zdcj) {
        if(creatMonth.compareTo(nowMonth) <= 0){
            //payCallDAO.getDeposit(unit); //催缴人数及金额

            //上个月或之前的催缴产生
            while (creatMonth.compareTo(nowMonth) < 0) {
                createPaycallNoError(unit, creatMonth,zdcj);
                creatMonth = ComUtils.getNextMonth(creatMonth);
            }
            //产生当月的催缴
            if (creatMonth.compareTo(nowMonth) == 0) {
                int dwfxr = Integer.parseInt(unit.getDwfxr());
                int day = ComUtils.getDayofCurrentMonth();
                if (dwfxr <= day) {
                    createPaycallNoError(unit, creatMonth,zdcj);
                }
            }
        }


    }

    @Override
    public PageResNew<ListUnitPayCallResRes> getUnitPayCallInfo(TokenContext tokenContext, String dwmc, String dwzh, String kssjStr, String jssjStr, String marker, String pageSize, String action) {

        ListAction listAction = ListAction.pageType(action);
        int pagesize = ComUtils.parstPageSize(pageSize);
        Date kssj = getKssj(kssjStr);
        Date jssj = getJssj(jssjStr);

        String ywwd = tokenContext.getUserInfo().getYWWD();
        IBaseDAO.CriteriaExtension ce =  getCriteriaExtension(dwmc,dwzh,ywwd);

        PageResults<CCollectionUnitPayCallVice> pageResults = null;
        try {
            pageResults = payCallDAO.listWithMarker(null, kssj, jssj, "created_at", Order.DESC, null, SearchOption.REFINED, marker, pagesize, listAction,ce);
        } catch (Exception e) {
            throw new ErrorException(e);
        }

        PageResNew<ListUnitPayCallResRes> result = new PageResNew<>();
        ArrayList<ListUnitPayCallResRes> list = getResultList(pageResults.getResults());
        result.setResults(action,list);

        return result;
    }

    private IBaseDAO.CriteriaExtension getCriteriaExtension(String dwmc, String dwzh, String ywwd) {
        return new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                criteria.createAlias("dwywmx", "dwywmx");
                criteria.createAlias("dwywmx.unit", "unit");
                criteria.createAlias("dwywmx.cCollectionUnitBusinessDetailsExtension", "cCollectionUnitBusinessDetailsExtension");
                if(!ComUtils.isEmpty(dwmc)){
                    criteria.add(Restrictions.like("unit.dwmc","%"+dwmc+"%"));
                }
                if(!ComUtils.isEmpty(dwzh)){
                    criteria.add(Restrictions.like("unit.dwzh","%"+dwzh+"%"));
                }

                if(!ComUtils.isEmpty(ywwd)){
                    criteria.createAlias("cCollectionUnitBusinessDetailsExtension.ywwd", "ywwd");
                    criteria.add(Restrictions.eq("ywwd.id",ywwd));
                }
            }
        };
    }

    private ArrayList<ListUnitPayCallResRes> getResultList(List<CCollectionUnitPayCallVice> results) {
        ArrayList<ListUnitPayCallResRes> result = new ArrayList<>();
        for(CCollectionUnitPayCallVice payCallVice : results){
            ListUnitPayCallResRes payCallView = new ListUnitPayCallResRes();
            StCollectionUnitBusinessDetails dwywmx = payCallVice.getDwywmx();
            payCallView.setYWLSH(dwywmx.getYwlsh());//业务流水号
            payCallView.setDWMC(dwywmx.getUnit().getDwmc());//单位名称
            payCallView.setDWZH(dwywmx.getUnit().getDwzh());//单位账号
            payCallView.setJBRXM(dwywmx.getUnit().getJbrxm());//经办人姓名
            payCallView.setJBRGDDH(dwywmx.getUnit().getJbrgddhhm());//经办人固定电话
            payCallView.setJBRSJHM(dwywmx.getUnit().getJbrsjhm());//经办人手机号码
            payCallView.setYHJNY(DateUtil.dateStrTransform(payCallVice.getYjny()));//应缴年月
            payCallView.setFSRS(dwywmx.getFsrs().intValue());//发生人数
            payCallView.setFSE(dwywmx.getFse()+"");//发生额
            payCallView.setZDCJ(payCallVice.getZdcjxx());//自动催缴
            payCallView.setSDCJ(String.valueOf(payCallVice.getCjcs()));//手动催缴次数
            payCallView.setId(payCallVice.getId());
            result.add(payCallView);
        }
        return result;
    }

    private void createPaycallNoError(StCommonUnit unit, Date creatMonth,String zdcj) {
        try {
            createPaycall(unit, creatMonth,zdcj);
        } catch (ErrorException e) {
            //不处理
            e.printStackTrace();
        } catch (RuntimeException e) {
            //记录日志
            e.printStackTrace();
        }
    }

    /**
     * 生成催缴业务
     */
    private void createPaycall(StCommonUnit unit, Date creatMonth,String zdcj) {
        CCollectionUnitPayCallVice payCall = new CCollectionUnitPayCallVice();
        //String cjsjMonth = ComUtils.parseToString(new Date,"MM月");
        String yjny = ComUtils.parseToString(creatMonth, "yyyyMM");
        //TODO 待设置时间 默认3天内
        payCall.setZdcjxx(zdcj);
        payCall.setYjny(yjny);
        payCall.setCjcs(0);
        StCollectionUnitBusinessDetails dwywmx = new StCollectionUnitBusinessDetails();
        //TODO 是否如此
        //AutoRemittanceInventoryRes inventoryAuto = depositInventory.getUnitRemittanceInventoryAuto(getSystemToken(), dwzh);

        dwywmx.setHbjny(yjny);
        dwywmx.setFsrs(BigDecimal.valueOf(unit.getCollectionUnitAccount().getDwjcrs()));
        dwywmx.setFse(BigDecimal.ZERO);
        dwywmx.setDwzh(unit.getDwzh());
        // TODO: 2017/10/11 杨凡检查修改业务类型后是否引起问题
        dwywmx.setYwmxlx(CollectionBusinessType.其他.getCode());
        dwywmx.setUnit(unit);

        CCollectionUnitBusinessDetailsExtension extension = new CCollectionUnitBusinessDetailsExtension();
        extension.setCzmc(CollectionBusinessType.催缴.getCode());
        extension.setStep(CollectionBusinessStatus.办结.getName());
        CAccountNetwork cAccountNetwork = cAccountNetworkDAO.get(unit.getExtension().getKhwd());
        extension.setYwwd(cAccountNetwork);
        dwywmx.setExtension(extension);

        payCall.setDwywmx(dwywmx);

        payCallDAO.saveNomal(payCall);
    }

    /**
     * 请于X月X日前完成本月汇缴
     */
    private String getPayCallMessage() {
        //TODO 3天内
        Date nextDate = ComUtils.getNextDate(new Date(), 3);
        Calendar instance = Calendar.getInstance();
        instance.setTime(nextDate);
        int month = instance.get(Calendar.MONTH) + 1;
        int day = instance.get(Calendar.DAY_OF_MONTH);
        return "请于" + month + "月" + day + "日前完成本月汇缴";
    }

    //TODO 需要系统默认
    private TokenContext getSystemToken() {
        TokenContext tokenContext = new TokenContext();
        UserInfo userInfo = new UserInfo();
        userInfo.setCZY("系统");
        userInfo.setYWWD("0");
        tokenContext.setUserInfo(userInfo);
        return tokenContext;
    }

    public Date getKssj(String kssjStr) {
        if(ComUtils.isEmpty(kssjStr)){
            return ComUtils.getNextDate(new Date(),-90);
        }else{
            return ComUtils.parseToDate(kssjStr, format);
        }
    }

    private Date getJssj(String jssjStr) {
        if(ComUtils.isEmpty(jssjStr)){
            return new Date();
        }else{
            return ComUtils.parseToDate(jssjStr,format);
        }
    }
}
