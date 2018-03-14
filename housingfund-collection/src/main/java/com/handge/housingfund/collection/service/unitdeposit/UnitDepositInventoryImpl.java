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
import com.handge.housingfund.common.service.collection.model.*;
import com.handge.housingfund.common.service.collection.model.deposit.*;
import com.handge.housingfund.common.service.collection.service.trader.ICollectionTrader;
import com.handge.housingfund.common.service.collection.service.unitdeposit.UnitDepositInventory;
import com.handge.housingfund.common.service.collection.service.unitdeposit.UnitRemittance;
import com.handge.housingfund.common.service.loan.model.CommonResponses;
import com.handge.housingfund.common.service.others.IPdfService;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.BusinessSubType;
import com.handge.housingfund.database.enums.ListAction;
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.enums.SearchOption;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by 凡 on 2017/7/25.
 * 单位清册
 */
@Service("unitDepositInventory")
public class UnitDepositInventoryImpl extends BaseService implements UnitDepositInventory{

    private String ywlx = CollectionBusinessType.单位清册.getCode();
    private String ywlxmc = CollectionBusinessType.单位清册.getName();

    private String busType = BusinessSubType.归集_缴存清册记录.getSubType();

    @Autowired
    private ICCollectionUnitDepositInventoryViceDAO inventoryDAO;

    @Autowired
    private IStCommonUnitDAO unitDAO;

    @Autowired
    private IStCommonPersonDAO personDAO;

    @Autowired
    private ICCollectionUnitRemittanceViceDAO remittanceViceDAO;

    @Autowired
    private ICCollectionIndividualAccountBasicViceDAO personAccSetDao;

    @Autowired
    private ICCollectionIndividualAccountActionViceDAO sealOrUnDAO;

    @Autowired
    private UnitRemittance unitRemittance;

    @Autowired
    private IStCollectionUnitBusinessDetailsDAO unitBusinessDetailsDAO;

    @Autowired
    private IPdfService pdfService;

    @Autowired
    private ICAccountNetworkDAO cAccountNetworkDAO;

    @Autowired
    private ICollectionTrader trader;

    @Autowired
    private ICCollectionIndividualAccountTransferNewViceDAO transferDAO;

    @Autowired
    private ICCollectionUnitDepositRatioViceDAO unitDepositRatioDAO;

    @Autowired
    private ICCollectionPersonRadixViceDAO personRadixDAO;

    /**
     * 提交单位清册信息
     * 增加清册类、调基调比在途限制
     */
    @Override
    public AddInventoryConfirmRes postUnitDepositInventory(TokenContext tokenContext, InventoryConfirmPost inventoryConfirmPost) {
        //过时
        AddInventoryConfirmRes result = new AddInventoryConfirmRes();
        result.setStatus("success");
        result.setYWLSH("不需要的API");
        return result;
    }

    /**
     *业务验证：
     * 需增加对清册类业务的在途验证、调基调比的业务验证
     */
    private void businessCheck(String dwzh,String qcyf) {
        //验证单位是否存在未办结的汇缴业务
        boolean isExist = remittanceViceDAO.checkIsExistDoing(dwzh);
        if(isExist){
            throw new ErrorException("单位存在未办结的汇缴业务！");
        }
        //查看单位清册月份是否已经生成，一个月只能有一笔清册
        CCollectionUnitDepositInventoryVice unitInventoryMsg = inventoryDAO.getUnitInventoryMsg(dwzh, qcyf);
        if(unitInventoryMsg !=null){
            throw new ErrorException("当前单位该月:"+qcyf+",清册信息已存在");
        }
        //清册类业务在途验证
        List<StCollectionPersonalBusinessDetails> personalBusiness= inventoryDAO.checkInventoryIsDoing(dwzh);
        if(personalBusiness != null && personalBusiness.size() > 0){
            String errorMsg = getPersonalBusinessMsg(personalBusiness);
            throw new ErrorException(errorMsg);
        }
        //调基调比业务在途验证
        boolean flag = unitBusinessDetailsDAO.isExistDoingUnitBus(dwzh, CollectionBusinessType.基数调整.getCode());
        if(flag){
            throw new ErrorException("该单位下存在基数调整业务，且正在办理！");
        }
        flag = unitBusinessDetailsDAO.isExistDoingUnitBus(dwzh,CollectionBusinessType.比例调整.getCode());
        if(flag){
            throw new ErrorException("该单位下存在比例调整业务，且正在办理！");
        }
    }

    private String getPersonalBusinessMsg(List<StCollectionPersonalBusinessDetails> personalBusiness) {
        StringBuilder sb = new StringBuilder();
        sb.append("当前单位下存在未办结的个人业务：\n");
        for(StCollectionPersonalBusinessDetails detail : personalBusiness){
            String ywlsh = detail.getYwlsh();
            String grzh = detail.getGrzh();
            String czmc = detail.getExtension().getCzmc();
            String step = detail.getExtension().getStep();
            String name =  CollectionBusinessType.getNameByCode(czmc);
            sb.append("个人");
            sb.append(name);
            sb.append("业务，业务流水号：");
            sb.append(ywlsh);
            sb.append(",该业务正在办理中，业务状态为:");
            sb.append(step);
            sb.append("。");
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * 获取单位缴存清册列表
     */
    @Override
    public PageRes<ListInventoryResRes> getUnitInventoryInfo(TokenContext tokenContext,String dwmc, String dwzh,String czy, String ywwd, String kssj, String jssj,String pageNumber, String pageSIZE) {
        //参数验证
        int pageNo = ComUtils.parstPageNo(pageNumber);
        int pageSize = ComUtils.parstPageSize(pageSIZE);
        //验证受理的开始时间与结束时间
        IBaseDAO.CriteriaExtension ce = getCriteriaExtension(dwmc, dwzh, czy, ywwd);

        Date startDate  = ComUtils.parseToDate(kssj,"yyyy-MM-dd HH:mm");
        Date endDate  = ComUtils.parseToDate(jssj,"yyyy-MM-dd HH:mm");
        PageResults<CCollectionUnitDepositInventoryVice> pageResults = inventoryDAO.listWithPage(null, startDate, endDate, "created_at", Order.DESC, null, SearchOption.REFINED, pageNo, pageSize,ce);

        List<CCollectionUnitDepositInventoryVice> results = pageResults.getResults();
        ArrayList<ListInventoryResRes> resultList = new ArrayList<>();
        for(CCollectionUnitDepositInventoryVice dwqcMsg : results){
            StCollectionUnitBusinessDetails dwywmx = dwqcMsg.getDwywmx();
            String qcny = dwqcMsg.getQcny();
            Date bjsj = dwqcMsg.getDwywmx().getExtension().getBjsj();

            ListInventoryResRes dwqcView = new ListInventoryResRes();
            dwqcView.setDWZH(dwywmx.getDwzh());
            dwqcView.setDWMC(dwywmx.getUnit().getDwmc());
            dwqcView.setQCNY(ComUtils.parseToYYYYMM2(qcny));
            dwqcView.setQCQRDH(dwqcMsg.getQcqrdh());
            dwqcView.setFSE(dwqcMsg.getQcfse().toString());
            dwqcView.setFSRS(dwywmx.getFsrs().toString());
            dwqcView.setSLSJ(ComUtils.parseToString(dwywmx.getExtension().getSlsj(),"yyyy-MM-dd HH:mm"));
            dwqcView.setCZY(dwywmx.getExtension().getCzy());
            dwqcView.setYWWD(dwywmx.getExtension().getYwwd().getMingCheng());

            boolean sfyc = ckeckSFYC(dwywmx.getDwzh(), qcny, bjsj);
            dwqcView.setSFYC(sfyc);

            resultList.add(dwqcView);
        }
        PageRes<ListInventoryResRes> pageres=new PageRes<>();
        pageres.setResults(resultList);
        pageres.setCurrentPage(pageResults.getCurrentPage());
        pageres.setNextPageNo(pageResults.getPageNo());
        pageres.setPageSize(pageResults.getPageSize());
        pageres.setPageCount(pageResults.getPageCount());
        pageres.setTotalCount(pageResults.getTotalCount());

        return pageres;
    }

    private boolean ckeckSFYC(String dwzh, String qcny, Date bjsj) {
        List<Object[]> sealAfter = inventoryDAO.getSealAfter(dwzh, qcny, bjsj);
        if(!ComUtils.isEmpty(sealAfter) && sealAfter.size() > 0){
            return true;
        }
        ArrayList<InventoryDetail> personAccSetOf = inventoryDAO.getPersonAccSetOf(dwzh, qcny, bjsj);
        if(!ComUtils.isEmpty(personAccSetOf) && personAccSetOf.size() > 0){
            return true;
        }
        return false;
    }


    /**
     * 汇缴申请时，根据账号+汇补缴年月查询清册信息
     * 1、根据条件查询单位该月的清册信息，清册信息的生成是根据单位下【当前】的人员情况，
     * 显示出正常缴存的人员
     * 2、单位清册为逐月清册，必须为缴至月份的下一个月
     * 3、如何单位当前月份已有清册
     * 注意：这里有2种情况，一种是
     * 1》清册受理页面是从查询单位、个人信息中查询显示，（产生）
     * 2》查看页面则是是从业务表中取出来（获取已持久化的数据）
     * 下面2个方法来区分
     * 这里是第1种根据单位账号及其缴至年月，找出当前的清册月份，如果缴至年月为空，
     * 则表示单位为新开户，再去找首次汇缴年月,来确认清册月份
     */
    @Override
    public AutoRemittanceInventoryRes getUnitRemittanceInventoryAuto(TokenContext tokenContext,String dwzh) {

        AutoRemittanceInventoryRes result= getNewInventory(tokenContext,dwzh);

        return result;
    }

    private AutoRemittanceInventoryRes getNewInventory(TokenContext tokenContext,String dwzh) {
        StCommonUnit unitBase = unitDAO.getUnit(dwzh);
        if(null == unitBase){
            throw new ErrorException("单位账号("+dwzh+")对应的单位不存在，不能办理清册业务！");
        }
        StCollectionUnitAccount collectionUnitAccount = unitBase.getCollectionUnitAccount();
        // 确认清册年月
        Date qcyf;
        Date jzny = ComUtils.parseToDate(collectionUnitAccount.getJzny(),"yyyyMM");
        if(null == jzny){
            qcyf = ComUtils.parseToDate(unitBase.getExtension().getDwschjny(),"yyyyMM");
        }else{
            qcyf = ComUtils.getNextMonth(jzny);
        }
        AutoRemittanceInventoryRes result = new AutoRemittanceInventoryRes();
        //这是先设置单位部分的信息
        result.setDWZH(unitBase.getDwzh());
        result.setDWMC(unitBase.getDwmc());
        result.setDWFCRS(collectionUnitAccount.getDwfcrs().intValue());
        result.setDWJCRS(collectionUnitAccount.getDwjcrs().intValue());
        // TODO test 根据当前登录人员自动读取
        result.setCZY(tokenContext.getUserInfo().getCZY());
        result.setYWWD(tokenContext.getUserInfo().getYWWD());

        result.setQCNY(ComUtils.parseToString(qcyf,"yyyy-MM"));
        result.setJBRXM(unitBase.getJbrxm());
        result.setJBRZJHM(unitBase.getJbrzjhm());
        result.setJBRZJLX(unitBase.getJbrzjlx());

        //得到应该缴存的人员信息
        ArrayList<AutoRemittanceInventoryResQCXX> personlist = getInventoryList2(qcyf,dwzh);
        BigDecimal fse;   //计算发生额、合计
        BigDecimal dwyhjehj = BigDecimal.ZERO;
        BigDecimal gryhjehj = BigDecimal.ZERO;
        for(AutoRemittanceInventoryResQCXX person  : personlist){
            BigDecimal dwyjce = ComUtils.toBigDec(person.getDWYJCE());
            BigDecimal gryjce = ComUtils.toBigDec(person.getGRYJCE());
            dwyhjehj = dwyhjehj.add(dwyjce);
            gryhjehj = gryhjehj.add(gryjce);
        }
        fse = dwyhjehj.add(gryhjehj);

        result.setQCXX(personlist);
        result.setFSE(fse.toString());
        result.setFSRS(personlist.size());

        AutoRemittanceInventoryResYJEZHJ yjhj = new AutoRemittanceInventoryResYJEZHJ();
        yjhj.setDWYJCEZHJ(dwyhjehj.toString());
        yjhj.setGRYJCEZHJ(gryhjehj.toString());
        yjhj.setZHJ(fse.toString());
        result.setYJEZHJ(yjhj);

        return result;
    }

    /**
     * //TODO 销户类需排除
     */
    private ArrayList<AutoRemittanceInventoryResQCXX> getInventoryList2(Date qcyf, String dwzh) {
        ArrayList<AutoRemittanceInventoryResQCXX> personlist = null;
        Date currentMouth = getCurrentMouth();
        //清册月份是否是当前月份的标志 大于0清册月份是往月清册，等于是当月，小于0是对未来月份的清册
        int flag = currentMouth.compareTo(qcyf);
        CCollectionUnitDepositInventoryVice nearLyInventory = inventoryDAO.getNearLyInventory(dwzh);
        if(ComUtils.isEmpty(nearLyInventory)){
            flag = 0;
        }
        if(flag == 0){
            //当月可直接根据当前人员信息生成清册
            personlist = personDAO.getListByDwzhNormalDeposit2(dwzh);
        }else {
            //得到基础清册中人员信息,2种情况：1、单位最近一次清册 2、单位首次汇缴清册(不包括销户类人员)
            personlist = getBaseInventory(dwzh);
            //对比启封封存信息,增加或删除人员信息
            sealFirter(dwzh,qcyf,personlist);
        }
        return personlist;
    }

    private void sealFirter(String dwzh, Date qcyf,final ArrayList<AutoRemittanceInventoryResQCXX> personlist) {
        String qcny = ComUtils.parseToString(qcyf, "yyyyMM");
        List<Object[]> sealBus = inventoryDAO.getSealBus(dwzh, qcny);
        for(Object[] obj : sealBus){
            change(personlist,obj);
        }
    }

    private void change(ArrayList<AutoRemittanceInventoryResQCXX> personlist, Object[] obj) {
        if("04".equals(obj[6].toString())){     //启封,加入
            AutoRemittanceInventoryResQCXX inventoryDetail = new AutoRemittanceInventoryResQCXX(obj);
            personlist.add(inventoryDetail);
        }
        if("05".equals(obj[6].toString())){     //封存，删除
            Iterator<AutoRemittanceInventoryResQCXX> iterator = personlist.iterator();
            while(iterator.hasNext()){
                AutoRemittanceInventoryResQCXX next = iterator.next();
                if(next.getGRZH().equals(obj[0])){
                    iterator.remove();
                    return;
                }
            }
        }
    }

    /**
     * 得到基础清册中人员信息,2种情况：1、单位最近一次清册 2、单位首次汇缴清册
     * //TODO 限制1：个人开户：个人首次汇缴年月不能大于单位首次汇缴年月
     * //限制2：个人开户：单位未汇缴时，首次汇缴年月必须与单位首次汇缴年月一致
     */
    private ArrayList<AutoRemittanceInventoryResQCXX> getBaseInventory(String dwzh) {
        ArrayList<AutoRemittanceInventoryResQCXX> result = new ArrayList<AutoRemittanceInventoryResQCXX>();
        //根据缴至年月来判断是否汇缴过
        StCommonUnit unit = unitDAO.getUnit(dwzh);
        StCollectionUnitAccount collectionUnitAccount = unit.getCollectionUnitAccount();
        String jzny = collectionUnitAccount.getJzny();
        if(ComUtils.isEmpty(jzny)){
            //得到单位首次汇缴的清册
            result = getFirstInventoryOfUnit(dwzh);
        }else{
            //得到最近一次清册，以及清册之后开户的人员信息
            result = getNearlyInventoryOfUnit(dwzh);
        }
        return result;
    }

    private ArrayList<AutoRemittanceInventoryResQCXX> getNearlyInventoryOfUnit(String dwzh) {
        //先得到最近一次清册业务信息
        CCollectionUnitDepositInventoryVice nearLyInventory = inventoryDAO.getNearLyInventory(dwzh);
        Date bjsj = nearLyInventory.getDwywmx().getExtension().getBjsj();
        String ywlsh = nearLyInventory.getDwywmx().getYwlsh();
        //得到最近一次清册的具体人员信息，封装成列表
        ArrayList<AutoRemittanceInventoryResQCXX> result = inventoryDAO.getNearLyInventory2(ywlsh);
        //得到清册之后开户的人员信息，加入列表中
        ArrayList<AutoRemittanceInventoryResQCXX> result2 = inventoryDAO.getPersonAccSetAfterInventory(dwzh,bjsj);
        result.addAll(result2);
        return result;
    }

    private ArrayList<AutoRemittanceInventoryResQCXX> getFirstInventoryOfUnit(String dwzh) {
        return inventoryDAO.getFirstInventoryOfUnit(dwzh);
    }

    /**
     * 通过清册确认单号得到汇缴清册详情
     * OK
     */
    @Override
    public GetRemittanceInventoryDetailRes getUnitRemittanceInventoryDetail(TokenContext tokenContext,String qcqrdh) {
        GetRemittanceInventoryDetailRes result = new GetRemittanceInventoryDetailRes();

        CCollectionUnitDepositInventoryVice unitInventoryMsg = inventoryDAO.getUnitInventoryMsg(qcqrdh);
        if(null == unitInventoryMsg){
            //查询为空,返回错误信息
            throw new ErrorException("清册信息不存在！");
        }
        StCollectionUnitBusinessDetails dwywmx = unitInventoryMsg.getDwywmx();

        result.setYWLSH(dwywmx.getYwlsh());
        result.setDWZH(dwywmx.getDwzh());
        result.setDWMC(dwywmx.getUnit().getDwmc());
        result.setDWFCRS(unitInventoryMsg.getDwfcrs().toString());
        result.setDWJCRS(unitInventoryMsg.getDwjcrs().toString());
        result.setFSE(unitInventoryMsg.getQcfse().toString());
        result.setFSRS(dwywmx.getFsrs().toString());
        result.setQCNY(ComUtils.parseToYYYYMM2(unitInventoryMsg.getQcny()));
        result.setCZY(dwywmx.getExtension().getCzy());
        result.setYWWD(dwywmx.getExtension().getYwwd().getMingCheng());

        String blzl = dwywmx.getExtension().getBlzl();
        if(ComUtils.isEmpty(blzl)){
            blzl = "";
        }
        result.setBLZL(blzl);
        //TODO 未定义下面两字段
        //result.setTZSJ();
        //result.setYZM();
        result.setJBRXM(dwywmx.getExtension().getJbrxm());
        result.setJBRZJHM(dwywmx.getExtension().getJbrzjhm());
        result.setJBRZJLX(dwywmx.getExtension().getJbrzjlx());
        //清册详情
        ArrayList<GetRemittanceInventoryDetailResDWHJQCXX> list = new ArrayList<GetRemittanceInventoryDetailResDWHJQCXX>();
        Set<CCollectionUnitDepositInventoryDetailVice> qcxqList = unitInventoryMsg.getQcxq();
        //计算总合计
        BigDecimal zhj = BigDecimal.ZERO;
        BigDecimal dwyhjze = BigDecimal.ZERO;
        BigDecimal gryhjze = BigDecimal.ZERO;

        for(CCollectionUnitDepositInventoryDetailVice perDetail : qcxqList){
            GetRemittanceInventoryDetailResDWHJQCXX perView = new GetRemittanceInventoryDetailResDWHJQCXX();
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
            list.add(perView);
            dwyhjze = dwyhjze.add(dwyjce);
            gryhjze = gryhjze.add(gryjce);
            zhj = zhj.add(heji);
        }
        result.setDWHJQCXX(list);

        GetRemittanceInventoryDetailResYJEZHJ heji = new GetRemittanceInventoryDetailResYJEZHJ();
        //以后做冗余
        heji.setDWYJCEZHJ(dwyhjze.toString());
        heji.setGRYJCEZHJ(gryhjze.toString());
        heji.setZHJ(zhj.toString());
        result.setYJEZHJ(heji);
        return result;
    }

    /**
     *打印清册回执单
     *ok
     */
    @Override
    public CommonResponses getReceipt(String qcqrdh) {
        HeadRemittanceInventoryRes result = new HeadRemittanceInventoryRes();
        CCollectionUnitDepositInventoryVice unitInventoryMsg = inventoryDAO.getUnitInventoryMsg(qcqrdh);
        if(unitInventoryMsg == null){
            throw new ErrorException("清册信息不存在！");
        }
        StCollectionUnitBusinessDetails dwywmx = unitInventoryMsg.getDwywmx();
        result.setYWLSH(dwywmx.getYwlsh());
        result.setDWZH(dwywmx.getDwzh());
        result.setDWMC(dwywmx.getUnit().getDwmc());
        result.setDWFCRS(unitInventoryMsg.getDwfcrs().intValue());
        result.setDWJCRS(unitInventoryMsg.getDwjcrs().intValue());
        result.setFSE(unitInventoryMsg.getQcfse().toString());
        result.setFSRS(dwywmx.getFsrs().intValue());
        result.setQCNY(ComUtils.parseToYYYYMM2(unitInventoryMsg.getQcny()));
        result.setYWWD(dwywmx.getExtension().getYwwd().getMingCheng());
        result.setCZY(dwywmx.getExtension().getCzy());
        //TODO 未定义下面两字段
        result.setTZSJ(ComUtils.parseToString(new Date(),"yyyy-MM-dd HH:mm"));
        result.setYZM("");

        result.setJBRXM(dwywmx.getExtension().getJbrxm());
        result.setJBRZJHM(dwywmx.getExtension().getJbrzjhm());
        result.setJBRZJLX(dwywmx.getExtension().getJbrzjlx());
        //清册详情
        ArrayList<HeadRemittanceInventoryResDWHJQC> list = new ArrayList<HeadRemittanceInventoryResDWHJQC>();
        Set<CCollectionUnitDepositInventoryDetailVice> qcxqList = unitInventoryMsg.getQcxq();
        //计算总合计
        BigDecimal zhj = BigDecimal.ZERO;
        BigDecimal dwyhjze = BigDecimal.ZERO;
        BigDecimal gryhjze = BigDecimal.ZERO;

        for(CCollectionUnitDepositInventoryDetailVice perDetail : qcxqList){
            if("01".equals(perDetail.getGrzhzt())){
                HeadRemittanceInventoryResDWHJQC perView = new HeadRemittanceInventoryResDWHJQC();
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

                dwyhjze = dwyhjze.add(dwyjce);
                gryhjze = gryhjze.add(gryjce);
                zhj = zhj.add(heji);
            }
        }
        result.setDWHJQC(list);

        HeadRemittanceInventoryResYJEZHJ heji = new HeadRemittanceInventoryResYJEZHJ();
        heji.setDWYJCEZHJ(dwyhjze.toString());
        heji.setGRYJCEZHJ(gryhjze.toString());
        heji.setZHJ(zhj.toString());
        result.setYJEZHJ(heji);
        String id = pdfService.getRemittanceInventoryReceiptPdf(result);
        System.out.println("生成id的值："+id);
        CommonResponses commonResponses = new CommonResponses();
        commonResponses.setId(id);
        commonResponses.setState("success");
        return commonResponses;

    }

    /**
     * 得到系统的当前时间，需要更加稳妥的时间
     */
    private Date getCurrentMouth(){
        Date date = new Date();
        String yyyyMM = ComUtils.parseToString(date, "yyyy-MM");
        Date currentMouth = ComUtils.parseToDate(yyyyMM,"yyyy-MM");
        return currentMouth;
    }

    /**
     * 清册办结处理，保存
     */
    @Override
    public void doFinal(String ywlsh) {
        unitRemittance.saveRemittance(ywlsh);
    }

    /**
     * 获取单位清册信息，一或多月
     */
    @Override
    public InventoryBatchMessage getInventoryBatchList2(TokenContext tokenContext, String dwzh, String qcsjq, String qcsjz, String YWLSH) {

        if(ComUtils.isEmpty(YWLSH)){
            return getInventoryBatchList(tokenContext,dwzh,qcsjq,qcsjz);
        }else{
            //YWLSH为汇缴的业务流水号，重新获取该批次的清册业务
            // bug修改：这里应该从数据库中获取数据而不是再次生成
            CCollectionUnitRemittanceVice remittance = remittanceViceDAO.getByYwlsh(YWLSH);
            String qcqrdh = remittance.getQcqrdh();

            CCollectionUnitDepositInventoryVice inventory = inventoryDAO.getByYwlsh(qcqrdh);
            CCollectionUnitDepositInventoryBatchVice qcpc = inventory.getQcpc();

            return getInventoryBatchMessage(tokenContext,qcpc);

        }

    }

    /**
     * 清册受理页面，清除不需要的个人详情信息
     */
    @Override
    public InventoryBatchMessage getInventoryBatchList(TokenContext tokenContext, String dwzh, String qcsjq, String qcsjz, String YWLSH) {
        InventoryBatchMessage inventoryBatchList = getInventoryBatchList2(tokenContext, dwzh, qcsjq, qcsjz,YWLSH);
        ArrayList<InventoryMessage> qclb = inventoryBatchList.getQCLB();
        for(InventoryMessage inventory : qclb){
            inventory.setQCXQ(null);
        }
        return inventoryBatchList;
    }

    private InventoryBatchMessage getInventoryBatchMessage(TokenContext tokenContext,CCollectionUnitDepositInventoryBatchVice qcpc) {

        InventoryBatchMessage batchInventory = new InventoryBatchMessage();

        ArrayList<InventoryMessage> qclb = getInventoryList(qcpc);   //多月的清册列表

        String dwzh = qcpc.getDwzh();
        StCommonUnit unit = unitDAO.getUnit(dwzh);

        batchInventory.setDWZH(dwzh);
        batchInventory.setDWMC(unit.getDwmc());
        batchInventory.setJZNY(ComUtils.parseToYYYYMM2(qcpc.getJzny()));

        batchInventory.setQCNYQ(ComUtils.parseToYYYYMM2(qcpc.getQcnyq()));
        batchInventory.setQCNYZ(ComUtils.parseToYYYYMM2(qcpc.getQcnyz()));
        batchInventory.setQCZFSE(ComUtils.moneyFormat(qcpc.getQczfse()));
        batchInventory.setQCLB(qclb);

        batchInventory.setWFTYE(ComUtils.moneyFormat(qcpc.getWftye()));

        CAccountNetwork network = cAccountNetworkDAO.get(tokenContext.getUserInfo().getYWWD());
        batchInventory.setJBRXM(unit.getJbrxm());
        batchInventory.setJBRZJLX(unit.getJbrzjlx());
        batchInventory.setJBRZJHM(unit.getJbrzjhm());
        batchInventory.setCZY(tokenContext.getUserInfo().getCZY());
        batchInventory.setYWWD(network.getMingCheng());

        return batchInventory;


    }

    private ArrayList<InventoryMessage> getInventoryList(CCollectionUnitDepositInventoryBatchVice qcpc) {
        ArrayList<InventoryMessage> result = new ArrayList<>();

        Set<CCollectionUnitDepositInventoryVice> qclb = qcpc.getQclb();
        for(CCollectionUnitDepositInventoryVice inventoryVice : qclb){
            InventoryMessage inventory = new InventoryMessage();

            String dwjcrs = inventoryVice.getDwjcrs().setScale(0).toString();
            inventory.setFSRS(dwjcrs);
            inventory.setDWFCRS(inventoryVice.getDwfcrs().setScale(0).toString());
            inventory.setDWJCRS(dwjcrs);

            inventory.setDWJCBL(ComUtils.moneyFormat(inventoryVice.getDwjcbl()));
            inventory.setGRJCBL(ComUtils.moneyFormat(inventoryVice.getGrjcbl()));

            inventory.setDWYJCEHJ(inventoryVice.getDwyjcehj().toString());
            inventory.setGRYJCEHJ(inventoryVice.getGryjcehj().toString());
            inventory.setFSE(ComUtils.moneyFormat(inventoryVice.getQcfse()));
            inventory.setQCNY(ComUtils.parseToYYYYMM2(inventoryVice.getQcny()));
            //inventory.setQCXQ(detail);

            result.add(inventory);
        }


        return result;
    }

    public InventoryBatchMessage getInventoryBatchList(TokenContext tokenContext, String dwzh, String qcsjq, String qcsjz){
        //参数验证
        paramCheck(dwzh,qcsjq,qcsjz);
        String yhjny = BusUtils.getDWYHJNY(dwzh);
        //参数默认设置
        if(ComUtils.isEmpty(qcsjq)){
            qcsjq = yhjny;
        }
        if(ComUtils.isEmpty(qcsjz)){
            qcsjz = yhjny;
        }

        //业务验证
        busCheck(dwzh,qcsjq,qcsjz);

        //封装清册信息
        InventoryBatchMessage batchInventory = new InventoryBatchMessage();

        BigDecimal qcfse = BigDecimal.ZERO;
        ArrayList<InventoryMessage> qclb = getInventoryList(dwzh,qcsjq,qcsjz);   //多月的清册列表
        for(InventoryMessage  inventory: qclb){
            qcfse = qcfse.add(ComUtils.toBigDec(inventory.getFSE()));
        }
        StCommonUnit unit = unitDAO.getUnit(dwzh);

        batchInventory.setDWZH(dwzh);
        batchInventory.setDWMC(unit.getDwmc());
        String jzny = ComUtils.parseToYYYYMM2(unit.getCollectionUnitAccount().getJzny());
        batchInventory.setJZNY(jzny);

        batchInventory.setQCNYQ(qcsjq);
        batchInventory.setQCNYZ(qcsjz);
        batchInventory.setQCZFSE(qcfse.toString());
        batchInventory.setQCLB(qclb);

        BigDecimal zsye = unit.getCollectionUnitAccount().getExtension().getZsye();
        batchInventory.setWFTYE(zsye.toString());

        CAccountNetwork network = cAccountNetworkDAO.get(tokenContext.getUserInfo().getYWWD());
        batchInventory.setJBRXM(unit.getJbrxm());
        batchInventory.setJBRZJLX(unit.getJbrzjlx());
        batchInventory.setJBRZJHM(unit.getJbrzjhm());
        batchInventory.setCZY(tokenContext.getUserInfo().getCZY());
        batchInventory.setYWWD(network.getMingCheng());

        return batchInventory;
    }

    private ArrayList<InventoryMessage> getInventoryList(String dwzh, String qcsjq, String qcsjz) {
        ArrayList<InventoryMessage> inventoryList = new ArrayList<InventoryMessage>();
        Date start = ComUtils.parseToDate(qcsjq,"yyyy-MM");
        Date end = ComUtils.parseToDate(qcsjz,"yyyy-MM");
        //依次产生每个月的清册数据
        while(start.compareTo(end) <= 0){
            InventoryMessage  inventory = getInventory2(dwzh,start);
            //inventory.setQCXQ(null);    //清册批次列表不显示详情，防止数据过多时卡顿,当前修改会引起提交的bug，提交时需要详情数据
            inventoryList.add(inventory);
            start = ComUtils.getNextMonth(start);
        }
        return inventoryList;
    }

    /**
     * 得到单位该月的清册信息
     */
    private InventoryMessage getInventory(String dwzh, Date qcyf) {

        //得到该单位该月的缴存比例
        Map map = getJCBL2(dwzh,qcyf);
        String dwjcbl = changeToView(map.get("dwjcbl").toString());
        String grjcbl = changeToView(map.get("grjcbl").toString());

        InventoryMessage inventory = new InventoryMessage();

        //得到单位这个月的清册详情
        ArrayList<InventoryDetail> detail = createInventoryListOf(dwzh, qcyf, map);
        sortList(detail);
        Long dwjcrs = getDWJCRS(detail);
        Long dwfcrs = getDWFCRS(detail);

        BigDecimal dwyjcehj = getDWYJCEHJ(detail);
        BigDecimal gryjcehj = getGRYJCEHJ(detail);
        BigDecimal fse = dwyjcehj.add(gryjcehj);//getFse(detail);
        String qcny = ComUtils.parseToString(qcyf,"yyyy-MM");

        inventory.setFSRS(dwjcrs.toString());
        inventory.setDWFCRS(dwfcrs.toString());
        inventory.setDWJCRS(dwjcrs.toString());

        inventory.setDWJCBL(dwjcbl);
        inventory.setGRJCBL(grjcbl);

        inventory.setDWYJCEHJ(dwyjcehj.toString());
        inventory.setGRYJCEHJ(gryjcehj.toString());
        inventory.setFSE(fse.toString());

        inventory.setQCNY(qcny);
        inventory.setQCXQ(detail);

        return inventory;
    }

    private BigDecimal getGRYJCEHJ(ArrayList<InventoryDetail> list) {
        BigDecimal fse = BigDecimal.ZERO;
        for(InventoryDetail detail : list){
            if("01".equals(detail.getGRZHZT())){
                fse = fse.add(ComUtils.toBigDec(detail.getGRYJCE()));
            }
        }
        return fse;
    }

    private BigDecimal getDWYJCEHJ(ArrayList<InventoryDetail> list) {
        BigDecimal fse = BigDecimal.ZERO;
        for(InventoryDetail detail : list){
            if("01".equals(detail.getGRZHZT())){
                fse = fse.add(ComUtils.toBigDec(detail.getDWYJCE()));
            }
        }
        return fse;
    }

    /**
     * 计算清册列表的发生额
     */
    private BigDecimal getFse(ArrayList<InventoryDetail> list) {
        BigDecimal fse = BigDecimal.ZERO;
        for(InventoryDetail detail : list){
            if("01".equals(detail.getGRZHZT())){
                fse = fse.add(ComUtils.toBigDec(detail.getQCFSE()));
            }
        }
        return fse;
    }

    /**
     * 计算清册列表的封存人数
     */
    private Long getDWFCRS(ArrayList<InventoryDetail> list) {
        long dwfcrs = 0L;
        for(InventoryDetail detail : list){
            if("02".equals(detail.getGRZHZT())){
                dwfcrs++;
            }
        }
        return dwfcrs;
    }

    /**
     * 计算清册列表的缴存人数
     */
    private Long getDWJCRS(ArrayList<InventoryDetail> list) {
        long dwjcrs = 0L;
        for(InventoryDetail detail : list){
            if("01".equals(detail.getGRZHZT())){
                dwjcrs++;
            }
        }
        return dwjcrs;
    }

    /**
     *得到单位在某月的缴存比例
     */
    private Map getJCBL2(String dwzh, Date qcyf) {
        //1、如果是当月，则直接读取单位当前的缴存比例
        Date month = ComUtils.getfirstDayOfMonth(new Date());
        if(month.compareTo(qcyf) == 0){
            return getCurrentRatio(dwzh);
        }

        //2、如果不是当月，根据基础比例（单位开户、清册中获取），对比调比业务后，来确定该月比例
        return getHistoryRatio2(dwzh,qcyf);
    }

    private Map getHistoryRatio2(String dwzh, Date qcyf) {

        CCollectionUnitDepositInventoryVice nearLyInventory = inventoryDAO.getNearLyInventory(dwzh);
        Date bjsj = null;
        String jzny = "";
        if(!ComUtils.isEmpty(nearLyInventory)){
            bjsj = nearLyInventory .getDwywmx().getExtension().getBjsj();
            jzny = unitDAO.getUnit(dwzh).getCollectionUnitAccount().getJzny();
        }
        //1.查3种情况询上一次清册后，发生的比例调整业务 (可能零条或多条)
        List<CCollectionUnitDepositRatioVice> ratios = unitDepositRatioDAO.getDepositRatio(dwzh,jzny,bjsj);
        if(ratios==null || ratios.size() == 0){
            return getCurrentRatio(dwzh);
        }

        BigDecimal dwjcbl = null;
        BigDecimal grjcbl = null;

        //以第一次调整的记录作为基准
        CCollectionUnitDepositRatioVice firstDepositRatio = ratios.get(0);
        String sxnyStr = firstDepositRatio.getDwywmx().getExtension().getSxny();
        Date sxny = ComUtils.parseToDate(sxnyStr,"yyyyMM");
        if(sxny.compareTo(qcyf) <= 0){
            dwjcbl = firstDepositRatio.getTzhdwbl();
            grjcbl = firstDepositRatio.getTzhgrbl();
        }else{
            dwjcbl = firstDepositRatio.getTzqdwbl();
            grjcbl = firstDepositRatio.getTzqgrbl();
        }
        //依次读取调比的值
        for(int i =1;i<ratios.size();i++){
            CCollectionUnitDepositRatioVice aRatio = ratios.get(i);
            sxnyStr = aRatio.getDwywmx().getExtension().getSxny();
            sxny = ComUtils.parseToDate(sxnyStr,"yyyyMM");
            if(sxny.compareTo(qcyf) <= 0){
                dwjcbl = aRatio.getTzhdwbl();
                grjcbl = aRatio.getTzhgrbl();
            }
        }

        Map result = new HashMap();
        result.put("dwjcbl",dwjcbl);
        result.put("grjcbl",grjcbl);
        result.put("flag","1");     //与当前不同：不一定，但按不同处理
        return result;
    }


    private Map getHistoryRatios(Date qcyf,CCollectionUnitDepositInventoryVice nearLyInventory, List<CCollectionUnitDepositRatioVice> ratios,String dwzh) {
        Map result = new HashMap();

        BigDecimal dwjcbl = null, grjcbl = null;
        //判断清册是否为空
        if(!ComUtils.isEmpty(nearLyInventory)){
            //先获取最近一次清册中的缴存比例基数
            dwjcbl = nearLyInventory.getDwjcbl();
            grjcbl = nearLyInventory.getGrjcbl();
        }else{
            //TODO
            return getCurrentRatio(dwzh);
        }
        for(CCollectionUnitDepositRatioVice ratio : ratios){
            String sxnyStr = ratio.getDwywmx().getExtension().getSxny();
            Date sxny = ComUtils.parseToDate(sxnyStr,"yyyyMM");
            if(sxny.compareTo(qcyf) <= 0){
                dwjcbl = ratio.getTzhdwbl();
                grjcbl = ratio.getTzhgrbl();
            }
        }
        result.put("dwjcbl",dwjcbl);
        result.put("grjcbl",grjcbl);
        result.put("flag","1");     //与当前不同：不一定，但按不同处理
        return result;
    }

    private Map getHistoryRatio(CCollectionUnitDepositRatioVice ratio) {
        Map result = new HashMap();
        BigDecimal tzqdwbl = ratio.getTzqdwbl();
        BigDecimal tzqgrbl = ratio.getTzqgrbl();
        result.put("dwjcbl",tzqdwbl);
        result.put("grjcbl",tzqgrbl);
        result.put("flag","1");     //与当前不同
        return result;
    }

    private Map getCurrentRatio(String dwzh) {
        Map map =new HashMap();
        StCommonUnit unit = unitDAO.getUnit(dwzh);
        StCollectionUnitAccount collectionUnitAccount = unit.getCollectionUnitAccount();
        BigDecimal dwjcbl = collectionUnitAccount.getDwjcbl();
        BigDecimal grjcbl = collectionUnitAccount.getGrjcbl();
        map.put("dwjcbl",dwjcbl);
        map.put("grjcbl",grjcbl);
        map.put("flag","0");     //相比当前，没有比例变动
        return map;
    }

    private void busCheck(String dwzh, String qcsjq, String qcsjz) {
        //1、起始时间验证
        Date start = ComUtils.parseToDate(qcsjq, "yyyy-MM");
        Date end = ComUtils.parseToDate(qcsjz, "yyyy-MM");
        if(start.compareTo(end) > 0){
            throw new ErrorException("清册月份开始时间不能大于结束时间");
        }
        Date currentMouth = getCurrentMouth();
        if(end.compareTo(currentMouth) > 0){
            throw new ErrorException("清册月份结束时间不能大于当前月份,暂不支持提前清册");
        }

        //2、验证起始年月是否为单位应汇缴年月
        String dwyhjny = BusUtils.getDWYHJNY(dwzh);
        if(!dwyhjny.equals(qcsjq)){
            throw new ErrorException("单位清册的起始年月错误，单位当前的应汇缴起始年月为"+dwyhjny);
        }

        //3、单位账户状态必须为正常或开户状态
        StCommonUnit unit = unitDAO.getUnit(dwzh);
        String dwzhzt = unit.getCollectionUnitAccount().getDwzhzt();
        if("01|02|03".indexOf(dwzhzt) < 0){
            throw new ErrorException("单位账户状态只能为正常、开户、缓缴状态");
        }
        //汇缴在途验证
//        BusUtils.checkRemittanceDoing(dwzh);

    }

    private void paramCheck(String dwzh, String qcsjq, String qcsjz) {
        AssertUtils.notEmpty(dwzh,"单位账号不能为空");
    }

    /**
     * 得到单位这个月的清册详情
     * 1、首次缴存：每月的数据：根据个人开户的首次汇缴年月，找到每个人，再对比封存启封数据
     * 2、已存在清册： 该月的生效月份为XX
     *  1.获取最近的一次清册列表 list
     *  2.获取清册办结时间之前，个人开户首次汇缴时间为XX的人，加入其中list
     *  3.获取清册办结时间后，首次汇缴小于等于XX的人，加入list
     *  4.对比内部转移信息
     *  5.根据启封封存业务更改状态
     */
    private ArrayList<InventoryDetail> createInventoryListOf(String dwzh,Date qcyf,Map map) {
        ArrayList<InventoryDetail> personlist = null;

        //清册月份是否是当前月份的标志 大于0清册月份是往月清册，等于是当月，小于0是对未来月份的清册
        Date currentMouth = getCurrentMouth();
        int flag = currentMouth.compareTo(qcyf);
        if(flag == 0){
            //当月可直接根据当前人员信息生成清册，可提升效率 （发现预封存冲突bug）
            personlist = inventoryDAO.getListByDwzhNormalDeposit3(dwzh,ComUtils.getDQYF());
        }else{
            //清册的月份不是当前月份
            personlist = getInventoryNotCurrentMouth(dwzh,qcyf);

            //转移与封存业务，调整人员
            transferAndSeal(dwzh,qcyf,personlist);

            //查询比例调整业务/基数调整业务，是否造成修改,
            computeDeposi2(dwzh,qcyf,personlist,map);

            //对比启封封存信息,对人员的信息进行修改
            //sealorUnseal(dwzh,qcyf,personlist);

            //针对老数据过滤重复
            personlist = distinctList(personlist);
        }

        return personlist;
    }

    private void transferAndSeal(String dwzh, Date qcyf, ArrayList<InventoryDetail> personlist) {

        CCollectionUnitDepositInventoryVice nearLyInventory = inventoryDAO.getNearLyInventory(dwzh);
        Date bjsj = null;
        String jzny = "";
        if(!ComUtils.isEmpty(nearLyInventory)){
            bjsj = nearLyInventory.getDwywmx().getExtension().getBjsj();
            jzny = unitDAO.getUnit(dwzh).getCollectionUnitAccount().getJzny();
        }
        //找出所有的的 转移/启封封存 业务
        String qcyfStr = ComUtils.parseToString(qcyf, "yyyyMM");
        List<Object[]> transferAndSeals = inventoryDAO.getTransferAndSeal(dwzh, qcyfStr ,bjsj, jzny);
        if(transferAndSeals.size() == 0 ){
            return;
        }

        //对列表根据个人账号排序
        personlist = distinctList(personlist);
        Collections.sort(personlist,new InventoryDetail.GrzhCompator());
        for(Object[] bus : transferAndSeals){
            String ywlx = bus[8].toString();
            //该笔业务为转入业务加入该人
            if("01".equals(ywlx)){
                addToInventoryList(bus,personlist);
            }else if("02".equals(ywlx)){
                //该笔业务为转出业务，可能会移除该人
                removeFromInventoryList(bus,personlist,qcyf);
            }else{
                //04、05照原来的处理，另外会查出所有的业务，后面再解决
                changeInventoryState(personlist,bus,dwzh);
            }
        }
    }

    /**
     * 重构
     * 搜寻后-》删除或修改
     */
    private void removeFromInventoryList(Object[] transfer, ArrayList<InventoryDetail> personlist, Date qcyf) {
        //列表为空时的bug
        if(personlist.size() == 0){
            return;
        }
        String grzh = transfer[0].toString();
        String sxnyStr = (String) transfer[1];

        int index = serchInventoryList(personlist,grzh);
        InventoryDetail detail = personlist.get(index);
        if(grzh.equals(detail.getGRZH())){
            if (isRemove(sxnyStr, qcyf)) {
                personlist.remove(index);
            } else {
                //预封存情况的处理，将缴存额设置为转移时的钱
                updateInventoryDetail(detail,transfer);
            }
        }
    }

    private void addToInventoryList(Object[] transfer, ArrayList<InventoryDetail> personlist) {
        String grzh = transfer[0].toString();
        //二分查找是否有该人，如果没有则加入
        int index = serchInventoryList(personlist,grzh);
        InventoryDetail detail = new InventoryDetail(transfer);
        //查看已指定的位置是否相同，相同说明已存在，小于则插入该位置，大于则插入后一个位置
        if(personlist.size()==0){   //列表为空时的bug
            personlist.add(detail);
            return;
        }
        int flag = grzh.compareTo(personlist.get(index).getGRZH());
        if(flag > 0){
            personlist.add(index+1,detail);
        }else if(flag < 0){
            personlist.add(index,detail);
        }else{
            //相同时，查看状态是否为正常，如果是封存状态，则改变其状态为正常（多次转移bug）
            detail = personlist.get(index);
            String grzhzt = detail.getGRZHZT();
            if("02".equals(grzhzt)){    //封存
                detail.setGRZHZT("01");
                detail.setQCFSE(detail.getYJCE());
            }
        }
    }

    private int serchInventoryList(ArrayList<InventoryDetail> personlist, String grzh) {
        int start = 0;
        int end = personlist.size()-1;
        int middle = 0;
        while(start <= end){
            middle = (start+end)/2;
            InventoryDetail detail = personlist.get(middle);
            int i = grzh.compareTo(detail.getGRZH());
            if(i < 0){
                end = middle -1;
            }else if(i == 0){
                return middle;
            }else{
                start = middle + 1;
            }
        }
        return middle;
    }

    /**
     *重新计算清册的缴存额
     */
    private void computeDeposi2(String dwzh, Date qcyf, ArrayList<InventoryDetail> personlist, Map map) {

        //针对老数据过滤重复
        personlist = distinctList(personlist);

        CCollectionUnitDepositInventoryVice nearLyInventory = inventoryDAO.getNearLyInventory(dwzh);
        Date bjsj = null;
        String jzny = "";
        if(!ComUtils.isEmpty(nearLyInventory)){
            bjsj = nearLyInventory.getDwywmx().getExtension().getBjsj();
            jzny = unitDAO.getUnit(dwzh).getCollectionUnitAccount().getJzny();
        }
        //查询上次清册后是否发生基数调整的业务
        List<CCollectionPersonRadixVice> radixs = personRadixDAO.getPersonRadix(dwzh,jzny,bjsj);
        //1、没有发生基数业务
        if(radixs == null || radixs.size() == 0){
            //比例有变化时,重新计算缴存额，没有变化则直接返回
            if("1".equals(map.get("flag"))){
                computeMoney2(personlist,map);
            }
        }else if(radixs.size() >= 1){
            //根据基数业务调整基数
            updateInventoryDetail(personlist,radixs,qcyf);
            //重新计算缴存额
            computeMoney2(personlist,map);
        }
    }

    private void updateInventoryDetail(ArrayList<InventoryDetail> personlist, List<CCollectionPersonRadixVice> radixs, Date qcyf) {
        CCollectionPersonRadixVice firstRadix = radixs.get(0);

        //1、先对清册列表按grzh排序
        Collections.sort(personlist,new InventoryDetail.GrzhCompator());

        //2、从首次基数调整中，把调整前的基数复制到清册的列表中
        copyToInventoryList(firstRadix,personlist);

        //3、根据生效年月与清册月份 更新缴存基数
        updateInventoryDetail2(personlist,radixs,qcyf);

    }

    private void copyToInventoryList(CCollectionPersonRadixVice firstRadix, ArrayList<InventoryDetail> personlist) {
        List<CCollectionPersonRadixDetailVice> jstzxq = new ArrayList<>(firstRadix.getJstzxq());
        Collections.sort(jstzxq);
        //双排序，再检索更新
        for(int i=0,j=0;i<jstzxq.size() && j< personlist.size();){
            CCollectionPersonRadixDetailVice radixDetail = jstzxq.get(i);
            InventoryDetail inventoryDetail = personlist.get(j);
            String grzh = radixDetail.getGrzh();
            String grzh2 = inventoryDetail.getGRZH();
            if(grzh.compareTo(grzh2) == 0){
                inventoryDetail.setGRJCJS(radixDetail.getTzqgrjs().toString());
                i++;j++;
            }else if(grzh.compareTo(grzh2) < 0){
                i++;
            }else{
                j++;
            }
        }

    }


    private void updateInventoryDetail2(ArrayList<InventoryDetail> personlist, List<CCollectionPersonRadixVice> radixs, Date qcyf) {
        //遍历基数业务
        for(CCollectionPersonRadixVice aRadixs : radixs){
            String sxnyStr = aRadixs.getDwywmx().getExtension().getSxny();
            Date sxny = ComUtils.parseToDate(sxnyStr, "yyyyMM");
            if(sxny.compareTo(qcyf)<=0){    //调基业务的生效年月比清册小（包括等于）时，将生效,即更新基数信息
                updateInventoryDetail3(personlist,aRadixs,qcyf);
            }
        }
        //TODO 是否修复历史清册与当前值不同，但没有发生业务的情况，默认应该以当前为准
    }

    private void updateInventoryDetail3(ArrayList<InventoryDetail> personlist, CCollectionPersonRadixVice aRadixs, Date qcyf) {
        List<CCollectionPersonRadixDetailVice> jstzxq = new ArrayList<>(aRadixs.getJstzxq());
        Collections.sort(jstzxq);
        //双排序，再检索更新
        for(int i=0,j=0;i<jstzxq.size() && j< personlist.size();){
            CCollectionPersonRadixDetailVice radixDetail = jstzxq.get(i);
            BigDecimal tzhgrjs = radixDetail.getTzhgrjs();
            if(tzhgrjs != null && tzhgrjs.compareTo(BigDecimal.ZERO) > 0){
                InventoryDetail inventoryDetail = personlist.get(j);
                String grzh = radixDetail.getGrzh();
                String grzh2 = inventoryDetail.getGRZH();
                if(grzh.compareTo(grzh2) == 0){
                    inventoryDetail.setGRJCJS(tzhgrjs.toString());
                    i++;j++;
                }else if(grzh.compareTo(grzh2) < 0){
                    i++;
                }else{
                    j++;
                }
            }else{
                i++;
            }
        }

    }

    /**
     * 通过列表中的基数，和map中的比例重新计算缴存额
     */
    private void computeMoney2(ArrayList<InventoryDetail> personlist, Map map) {
        BigDecimal dwjcbl = (BigDecimal) map.get("dwjcbl");
        BigDecimal grjcbl = (BigDecimal) map.get("grjcbl");
        for(InventoryDetail inventoryDetail : personlist){
            BigDecimal grjcjs = ComUtils.toBigDec(inventoryDetail.getGRJCJS());
            BigDecimal dwjyce = BusUtils.computeDeposit(grjcjs, dwjcbl);
            BigDecimal grjyce = BusUtils.computeDeposit(grjcjs, grjcbl);
            BigDecimal yjce = dwjyce.add(grjyce);

            inventoryDetail.setGRJCJS(grjcjs.toString());
            inventoryDetail.setDWYJCE(dwjyce.toString());
            inventoryDetail.setGRYJCE(grjyce.toString());
            inventoryDetail.setYJCE(yjce.toString());
            if("01".equals(inventoryDetail.getGRZHZT())){
                inventoryDetail.setQCFSE(yjce.toString());
            }
        }



    }

    private CCollectionPersonRadixVice getCurrentRadixs(List<CCollectionPersonRadixVice> radixs, Date qcyf) {
        if(radixs == null || radixs.size() == 0){
            return null;
        }else if(radixs.size() == 1){
            return radixs.get(0);
        }else{
            return null;
        }
    }

    private void refresh(InventoryDetail tnventoryDetail, BigDecimal grjs, Map map) {
        BigDecimal dwjcbl = (BigDecimal) map.get("dwjcbl");
        BigDecimal grjcbl = (BigDecimal) map.get("grjcbl");
        BigDecimal dwyjce = BusUtils.computeDeposit(grjs,dwjcbl);
        BigDecimal gryjce = BusUtils.computeDeposit(grjs,grjcbl);
        BigDecimal yjce = dwyjce.add(gryjce);
        tnventoryDetail.setGRJCJS(grjs.toString());
        tnventoryDetail.setDWYJCE(dwyjce.toString());
        tnventoryDetail.setGRYJCE(gryjce.toString());
        tnventoryDetail.setYJCE(yjce.toString());
        if(!BigDecimal.ZERO.equals(tnventoryDetail.getQCFSE())){
            tnventoryDetail.setQCFSE(yjce.toString());
        }
    }

    /**
     * 检查基数业务，查看就是调整业务是否与当前的一致
     * true : 一致
     * //TODO 这样的 集合与非集，是否满足业务
     */
    private boolean checkRadix(CCollectionPersonRadixVice radix,Date qcyf) {
        if(radix == null){
            return true;
        }
        String sxnyStr = radix.getDwywmx().getExtension().getSxny();
        if(ComUtils.isEmpty(sxnyStr)){
            return true;
        }
        Date sxny = ComUtils.parseToDate(sxnyStr, "yyyyMM");
        if(qcyf.compareTo(sxny) >= 0){
            return true;
        }
        return false;
    }

    private void updateInventoryDetail(InventoryDetail next, Object[] transfer) {
        String grjcjs = (String) transfer[3];
        String dwyjce = (String) transfer[4];
        String gryjce = (String) transfer[5];
        String yjce = ComUtils.addMoney(dwyjce,gryjce);
        String qcfse = "01".equals(next.getGRZHZT())  ? yjce : "0.00" ;
        next.setGRJCJS(grjcjs);   //转移时的基数
        next.setDWYJCE(dwyjce);   //转移时的单位月缴存额
        next.setGRYJCE(gryjce);   //转移时的个人月缴存额
        next.setYJCE(yjce);
        next.setQCFSE(qcfse);
    }

    private boolean isRemove(String sxnyStr, Date qcyf) {
        if(ComUtils.isEmpty(sxnyStr)){
            return true;
        }else{
            Date sxny = ComUtils.parseToDate(sxnyStr,"yyyyMM");
            if(sxny.compareTo(qcyf)<= 0){
                return true;
            }
        }
        return false;
    }

    /**
     * 清册的月份不是当前月份，得到人员的存在与否信息
     */
    private ArrayList<InventoryDetail> getInventoryNotCurrentMouth(String dwzh, Date qcyf) {
        ArrayList<InventoryDetail> personlist = null;
        //是否是首次汇缴
        CCollectionUnitDepositInventoryVice nearLyInventory = inventoryDAO.getNearLyInventory(dwzh);
        if(ComUtils.isEmpty(nearLyInventory)){
            personlist = getSchjqc(dwzh,qcyf);
        }else {
            //得到基础清册中人员信息,2种情况：1、单位最近一次清册
            personlist = getBaseInventory3(dwzh,qcyf);
        }
        return personlist;
    }

    /**
     *
     */
    private ArrayList<InventoryDetail> getSchjqc(String dwzh, Date qcyf) {
        String qcny = ComUtils.parseToString(qcyf, "yyyyMM");
        ArrayList<InventoryDetail> list = inventoryDAO.getSchjqc(dwzh,qcny);
        return list;

    }

    /**
     * 对清册中的人员进行启封封存处理,
     * 后面需完善
     */
    private void sealorUnseal(String dwzh, Date qcyf, ArrayList<InventoryDetail> personlist) {
        String qcny = ComUtils.parseToString(qcyf, "yyyyMM");
        List<Object[]> sealBus = inventoryDAO.getSealBus3(dwzh, qcny);
        for(Object[] obj : sealBus){
            changeInventoryState(personlist,obj,dwzh);
        }

    }

    /**
     * 根据当前该笔业务，在清册显示启封或封存，并修改清册发生额
     * 如果当前此人
     */
    private void changeInventoryState(ArrayList<InventoryDetail> personlist, Object[] obj, String dwzh) {
        String grzh = obj[0].toString();
        String ywlx = obj[8].toString();
        String grzhzt = obj[6].toString();

        if(personlist.size() > 0){
            int index = serchInventoryList(personlist,grzh);
            InventoryDetail detail = personlist.get(index);
            if(grzh.equals(detail.getGRZH())){
                if("03|04|05".indexOf(grzhzt) >= 0){
                    personlist.remove(index);
                    return;
                }
                grzhzt = "04".equals(ywlx) ? "01" : "02";
                String yjce = "04".equals(ywlx) ? detail.getYJCE() : ComUtils.moneyFormat(BigDecimal.ZERO);
                detail.setGRZHZT(grzhzt);
                detail.setQCFSE(yjce);
            }else if("03|04|05".indexOf(grzhzt) < 0){
                //兼容老数据，将正常，或封存的人加入清册（排除销户人员）
                StCommonPerson person = personDAO.getByGrzh(grzh);
                if(dwzh.equals(person.getUnit().getDwzh())){
                    InventoryDetail inventoryDetail = getInventoryDetail(obj,ywlx);
                    int flag = grzh.compareTo(detail.getGRZH());
                    if(flag > 0){
                        personlist.add(index+1,inventoryDetail);
                    }else if(flag < 0){
                        personlist.add(index,inventoryDetail);
                    }

                }
            }
        }
    }

    private InventoryDetail getInventoryDetail(Object[] obj, String ywlx) {
        InventoryDetail inventoryDetail = new InventoryDetail(obj);
        String grzhzt = "04".equals(ywlx) ? "01" : "02" ;
        String qcfse = "04".equals(ywlx) ? inventoryDetail.getYJCE() : ComUtils.moneyFormat(BigDecimal.ZERO);
        inventoryDetail.setGRZHZT(grzhzt);
        inventoryDetail.setQCFSE(qcfse);
        return inventoryDetail;
    }


    /**
     * 1.获取最近的一次清册列表 list
     * 2.获取清册办结时间之前，个人开户首次汇缴时间为XX的人，加入其中list
     * 3.获取清册办结时间后，首次汇缴小于等于XX的人，加入list
     */
    private ArrayList<InventoryDetail> getBaseInventory3(String dwzh,Date qcyf) {

        CCollectionUnitDepositInventoryVice nearLyInventory = inventoryDAO.getNearLyInventory(dwzh);
        Date bjsj = nearLyInventory.getDwywmx().getExtension().getBjsj();
        String nearLyqcny = nearLyInventory.getQcny();

        String ywlsh = nearLyInventory.getDwywmx().getYwlsh();
        //1
        ArrayList<InventoryDetail> result = inventoryDAO.getNearLyInventory3(ywlsh);

        //2
        String qcyfStr = ComUtils.parseToString(qcyf,"yyyyMM");
        ArrayList<InventoryDetail> result2 = inventoryDAO.getPersonAccSetBeforeBJSJ(dwzh,nearLyqcny,qcyfStr,bjsj);

        //3
        ArrayList<InventoryDetail> result3 = inventoryDAO.getPersonAccSetAfterBJSJ(dwzh,qcyfStr,bjsj);

        result.addAll(result2);
        result.addAll(result3);

        //去重
        result = distinctList(result);

        return result;
    }

    private ArrayList<InventoryDetail> distinctList(ArrayList<InventoryDetail> result) {
        if(result == null || result.size() <= 1){
            return result;
        }
        Set<InventoryDetail> set = new HashSet<>(result);

        result.clear();
        result.addAll(set);

        return result;
    }

    /**
     * 提交清册信息
     */
    @Override
    public BusCommonRetrun addInventoryBatch(TokenContext tokenContext, String dwzh, String qcsjq, String qcsjz, String jkfs, String cxqc) {

        if("01".equals(cxqc)){
            //汇缴入账失败后，重新发起清册业务
            unitRemittance.remittanceAgain(dwzh,jkfs);
            //5、消息返回
            BusCommonRetrun result = new BusCommonRetrun();
            result.setStatus("success");
            result.setYWLSH("");
            result.setMessage("已清册");
            return result;
        }

        String qcsjq2 = ComUtils.parseToYYYYMM(qcsjq);
        businessCheck(dwzh,qcsjq2);

        //1、调用生成清册数据的方法，得到数据
        InventoryBatchMessage inventoryBatchList = getInventoryBatchList2(tokenContext,dwzh, qcsjq, qcsjz, null);

        StCommonUnit unit = unitDAO.getUnit(dwzh);
        StCollectionUnitAccount unitAccount = unit.getCollectionUnitAccount();
        String jzny = unitAccount.getJzny();
        BigDecimal zsye = unitAccount.getExtension().getZsye();

        //2、验证
        if (!unit.getExtension().getKhwd().equals(tokenContext.getUserInfo().getYWWD()))
            throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务不在当前网点受理范围内");

        //3、保存数据
        CCollectionUnitDepositInventoryBatchVice inventoryBatch = new CCollectionUnitDepositInventoryBatchVice();

        inventoryBatch.setDwzh(dwzh);
        inventoryBatch.setJzny(jzny);
        inventoryBatch.setQcnyq(ComUtils.parseToYYYYMM(qcsjq));
        inventoryBatch.setQcnyz(ComUtils.parseToYYYYMM(qcsjz));
        inventoryBatch.setJkfs(jkfs);
        inventoryBatch.setDwzhye(unit.getCollectionUnitAccount().getDwzhye());

        BigDecimal qczfse = ComUtils.toBigDec(inventoryBatchList.getQCZFSE());
        AssertUtils.isTrue(qczfse.compareTo(BigDecimal.ZERO) > 0 , "清册发生额不能为零！");

        inventoryBatch.setQczfse(ComUtils.toBigDec(qczfse));
        inventoryBatch.setWftye(zsye);

        //保存多个月的清册信息
        Set<CCollectionUnitDepositInventoryVice> qclb = createInventory(tokenContext,inventoryBatch,inventoryBatchList);
        inventoryBatch.setQclb(qclb);

        inventoryDAO.saveBatch(inventoryBatch);

        //保存后验证，当出现异常情况下验重
        try{
            inventoryDAO.getInventory(dwzh,qcsjq2);
        }catch (RuntimeException e){
            throw new ErrorException("清册办理出现重复错误，不能再次清册！");
        }

        String ywpch = inventoryBatch.getYwpch();

        //4、根据缴款方式，进行汇缴
        unitRemittance.saveRemittance3(tokenContext,ywpch);

        //5、消息返回
        BusCommonRetrun result = new BusCommonRetrun();
        result.setStatus("success");
        result.setYWLSH(ywpch);
        result.setMessage("已清册");
        return result;
    }


    private Set<CCollectionUnitDepositInventoryVice> createInventory(TokenContext tokenContext,CCollectionUnitDepositInventoryBatchVice inventoryBatch,InventoryBatchMessage inventoryBatchList) {
        Set<CCollectionUnitDepositInventoryVice> list = new HashSet<>();
        ArrayList<InventoryMessage> qclb = inventoryBatchList.getQCLB();
        for(InventoryMessage  inventoryMessage : qclb){
            CCollectionUnitDepositInventoryVice inventoryVice = getInventoryVice(tokenContext,inventoryBatchList.getDWZH(),inventoryMessage,inventoryBatch);
            list.add(inventoryVice);
        }
        return list;
    }

    private CCollectionUnitDepositInventoryVice getInventoryVice(TokenContext tokenContext,String dwzh,InventoryMessage inventoryMessage,CCollectionUnitDepositInventoryBatchVice inventoryBatch) {
        StCommonUnit unit = unitDAO.getUnit(dwzh);

        CCollectionUnitDepositInventoryVice result = new CCollectionUnitDepositInventoryVice();
        String qcny = ComUtils.parseToYYYYMM(inventoryMessage.getQCNY());
        result.setQcny(qcny);
        result.setGrjcbl(new BigDecimal(inventoryMessage.getGRJCBL()).divide(new BigDecimal("100")));
        result.setDwjcbl(new BigDecimal(inventoryMessage.getDWJCBL()).divide(new BigDecimal("100")));
        result.setDwfcrs(ComUtils.toBigDec(inventoryMessage.getDWFCRS()));
        result.setDwjcrs(ComUtils.toBigDec(inventoryMessage.getDWJCRS()));
        result.setQcfse(ComUtils.toBigDec(inventoryMessage.getFSE()));

        StCollectionUnitBusinessDetails dwywmx = new StCollectionUnitBusinessDetails();
        dwywmx.setFse(BigDecimal.ZERO);
        dwywmx.setDwzh(dwzh);
        dwywmx.setFsrs(ComUtils.toBigDec(inventoryMessage.getDWJCRS()));
        // TODO: 2017/10/11 杨凡检查修改业务类型后是否引起问题
        dwywmx.setYwmxlx(CollectionBusinessType.其他.getCode());
        dwywmx.setUnit(unit);

        CCollectionUnitBusinessDetailsExtension extension = new CCollectionUnitBusinessDetailsExtension();

        extension.setSlsj(new Date());
        extension.setBjsj(new Date());
        CAccountNetwork network = cAccountNetworkDAO.get(tokenContext.getUserInfo().getYWWD());
        extension.setYwwd(network);
        extension.setCzy(tokenContext.getUserInfo().getCZY());
        extension.setJbrxm(unit.getJbrxm());
        extension.setJbrzjlx(unit.getJbrzjlx());
        extension.setJbrzjhm(unit.getJbrzjhm());

        extension.setStep(CollectionBusinessStatus.办结.getName());
        extension.setCzmc(ywlx);
        //
        dwywmx.setExtension(extension);

        result.setDwywmx(dwywmx);

        Set<CCollectionUnitDepositInventoryDetailVice> qcxq = getInventoryDetailViceSet(result,inventoryMessage.getQCXQ());

        BigDecimal dwyjcehj = getDwyjcehj(qcxq);
        BigDecimal gryjcehj = getGryjcehj(qcxq);
        result.setQcxq(qcxq);
        result.setDwyjcehj(dwyjcehj);
        result.setGryjcehj(gryjcehj);

        result.setQcpc(inventoryBatch);
        return result;

    }

    private BigDecimal getGryjcehj(Set<CCollectionUnitDepositInventoryDetailVice> qcxq) {
        BigDecimal gryjcehj = BigDecimal.ZERO;
        for(CCollectionUnitDepositInventoryDetailVice vice : qcxq){
            if("01".equals(vice.getGrzhzt())){
                gryjcehj = gryjcehj.add(vice.getGryjce());
            }
        }
        return gryjcehj;
    }

    private BigDecimal getDwyjcehj(Set<CCollectionUnitDepositInventoryDetailVice> qcxq) {
        BigDecimal dwyjcehj = BigDecimal.ZERO;
        for(CCollectionUnitDepositInventoryDetailVice vice : qcxq){
            if("01".equals(vice.getGrzhzt())){
                dwyjcehj = dwyjcehj.add(vice.getDwyjce());
            }
        }
        return dwyjcehj;
    }

    private Set<CCollectionUnitDepositInventoryDetailVice> getInventoryDetailViceSet(CCollectionUnitDepositInventoryVice result,ArrayList<InventoryDetail> qcxq) {
        Set<CCollectionUnitDepositInventoryDetailVice> detailViceSet = new HashSet<CCollectionUnitDepositInventoryDetailVice>();
        for(InventoryDetail  inventoryDetail: qcxq){
            CCollectionUnitDepositInventoryDetailVice vice = getInventoryDetailVice(result,inventoryDetail);
            detailViceSet.add(vice);
        }
        return detailViceSet;
    }

    private CCollectionUnitDepositInventoryDetailVice getInventoryDetailVice(CCollectionUnitDepositInventoryVice result,InventoryDetail inventoryDetail) {
        CCollectionUnitDepositInventoryDetailVice vice = new CCollectionUnitDepositInventoryDetailVice();
        //vice.setQcqrdh();
        vice.setGrzh(inventoryDetail.getGRZH());

        vice.setGrjcjs(ComUtils.toBigDec(inventoryDetail.getGRJCJS()));
        vice.setDwyjce(ComUtils.toBigDec(inventoryDetail.getDWYJCE()));
        vice.setGryjce(ComUtils.toBigDec(inventoryDetail.getGRYJCE()));

        vice.setQcfse(ComUtils.toBigDec(inventoryDetail.getQCFSE()));
        vice.setYjce(ComUtils.toBigDec(inventoryDetail.getYJCE()));

        vice.setGrzhzt(inventoryDetail.getGRZHZT());
        vice.setGrzhye(ComUtils.toBigDec(inventoryDetail.getGRZHYE()));
        vice.setInventory(result);
        return vice;
    }

    /**
     * 获取单位该月的最新清册详情,通过ywlsh
     */
    @Override
    public LatestInventory getLatestInventoryOfMonth(TokenContext tokenContext,String ywlsh) {

        LatestInventory latestInventory = new LatestInventory();

        //1、先获取单位首次清册
        FirstInventory firstInventory = getFirstInventoryOfMonth(tokenContext, ywlsh);
        InventoryMessage inventoryMessage = firstInventory.getInventoryMessage();
        //2、对比业务修改，记录变更
        InventoryChangeList changeList = compareBus(inventoryMessage,ywlsh);

        latestInventory.setQCQRDH(firstInventory.getQCQRDH());
        latestInventory.setDWZH(firstInventory.getDWZH());
        latestInventory.setDWMC(firstInventory.getDWMC());
        latestInventory.setJZNY(firstInventory.getJZNY());

        latestInventory.setQCNYQ(firstInventory.getQCNYQ());
        latestInventory.setQCNYZ(firstInventory.getQCNYZ());
        latestInventory.setQCZFSE(inventoryMessage.getFSE());
        latestInventory.setWFTYE(firstInventory.getWFTYE());

        latestInventory.setJBRXM(firstInventory.getJBRXM());
        latestInventory.setJBRZJLX(firstInventory.getJBRZJLX());
        latestInventory.setJBRZJHM(firstInventory.getJBRZJHM());

        latestInventory.setCZY(firstInventory.getCZY());
        latestInventory.setYWWD(firstInventory.getYWWD());

        latestInventory.setChangeMessage(changeList);
        latestInventory.setInventoryMessage(inventoryMessage);

        return latestInventory;
    }

    /**
     * 对最初的清册列表对比开户、转移、启封封存业务，得到最新的清册列表
     */
    private InventoryChangeList compareBus(InventoryMessage inventoryMessage, String ywlsh) {
        CCollectionUnitDepositInventoryVice inventory = inventoryDAO.getByYwlsh(ywlsh);

        InventoryChangeList changeList = new InventoryChangeList();
        ArrayList<InventoryChangeDetail> changeListdetail = new ArrayList<>();
        changeList.setChangeDetail(changeListdetail);

        Date bjsj = inventory.getDwywmx().getExtension().getBjsj();
        String dwzh = inventory.getDwywmx().getDwzh();
        String qcny = inventory.getQcny();

        ArrayList<InventoryDetail> qcxq = inventoryMessage.getQCXQ();
        //1、先查询清册办结后的开户的业务,首次汇缴年月小于等于该月份，将其加入其中
        ArrayList<InventoryDetail> personAccSetList = inventoryDAO.getPersonAccSetOf(dwzh, qcny, bjsj);//要求数据都正常，缴存额暂按当前处理，后面再细化

        //2、得到转移列表人员
        ArrayList<InventoryDetail> transferInBus = inventoryDAO.getTransferInBus(dwzh, qcny, bjsj);
        personAccSetList.addAll(transferInBus);

        //获取当月的缴存比例重新计算金额
        computeMoney(dwzh,qcny,personAccSetList,inventoryMessage);

        qcxq.addAll(personAccSetList);

        //开户信息加入变更列表中
        BigDecimal bghj = addToChangeList(changeList,personAccSetList);

        //2、按时间顺序对比启封封存业务，修改清册
        //得到清册办结时间后的启封封存业务，生效月份小于等于清册办结时间，按时间升序(假设数据没问题，上面的列表都已存在这些人)
        List<Object[]> sealAfter = inventoryDAO.getSealAfter(dwzh, qcny, bjsj);
        //
        change(qcxq,changeList,sealAfter,bghj);

        //3、根据详情更新清册数据:inventoryMessage
        refresh(inventoryMessage);

        return changeList;
    }

    private void computeMoney(String dwzh, String qcny, ArrayList<InventoryDetail> personAccSetList, InventoryMessage inventoryMessage) {
        for(InventoryDetail detail : personAccSetList){
            BigDecimal grjcjs = ComUtils.toBigDec(detail.getGRJCJS());
            BigDecimal dwjcbl = new BigDecimal(inventoryMessage.getDWJCBL()).divide(new BigDecimal("100"));
            BigDecimal grjcbl = new BigDecimal(inventoryMessage.getGRJCBL()).divide(new BigDecimal("100"));
            BigDecimal dwyjce = BusUtils.computeDeposit(grjcjs, dwjcbl);
            BigDecimal gryjce = BusUtils.computeDeposit(grjcjs, grjcbl);
            BigDecimal yjce = dwyjce.add(gryjce);
            detail.setYJCE(yjce.toString());
            detail.setDWYJCE(dwyjce.toString());
            detail.setGRYJCE(gryjce.toString());
            if(!BigDecimal.ZERO.equals(detail.getQCFSE())){
                detail.setQCFSE(yjce.toString());
            }
        }
    }

    private void change(ArrayList<InventoryDetail> qcxq, InventoryChangeList changeList, List<Object[]> sealAfter, BigDecimal bghj) {
        BigDecimal bgfsze = bghj;
        outer : for(Object[] obj : sealAfter){
            String ywlx = obj[8].toString();
            String grzh = obj[0].toString();
            if("04".equals(ywlx)){  //启封
                for(InventoryDetail detail : qcxq){
                    if(grzh.equals(detail.getGRZH())){
                        detail.setGRZHZT("01"); //正常
                        detail.setQCFSE(detail.getYJCE());
                        ArrayList<InventoryChangeDetail> changeDetail = changeList.getChangeDetail();
                        changeDetail.add(getChangeDetail(detail,ywlx));
                        bgfsze = bgfsze.add(ComUtils.toBigDec(detail.getYJCE()));
                        continue outer;
                    }
                }
            }
            if("05".equals(ywlx)){   //封存
                for(InventoryDetail detail : qcxq){
                    if(grzh.equals(detail.getGRZH())){
                        detail.setGRZHZT("02"); //正常
                        detail.setQCFSE("0.00");
                        ArrayList<InventoryChangeDetail> changeDetail = changeList.getChangeDetail();
                        changeDetail.add(getChangeDetail(detail,ywlx));
                        bgfsze = bgfsze.subtract(ComUtils.toBigDec(detail.getYJCE()));
                        continue outer;
                    }
                }
            }
        }

        changeList.setFSEHJ(bgfsze);
    }

    private InventoryChangeDetail getChangeDetail(InventoryDetail detail,String ywlx) {
        String bgyy = "04".equals(ywlx) ? "启封补缴" : "封存错缴";
        InventoryChangeDetail changeDetail = new InventoryChangeDetail();
        changeDetail.setGRZH(detail.getGRZH());
        changeDetail.setXingMing(detail.getXingMing());
        changeDetail.setZJHM(detail.getZJHM());
        changeDetail.setFSE(detail.getYJCE());
        changeDetail.setBGYY(bgyy);
        return changeDetail;
    }

    /**
     * 更新清册的发生额、人数等
     */
    private void refresh(InventoryMessage inventoryMessage) {
        ArrayList<InventoryDetail> qcxq = inventoryMessage.getQCXQ();
        Long dwjcrs = getDWJCRS(qcxq);
        Long dwfcrs = getDWFCRS(qcxq);
        BigDecimal fse = getFse(qcxq);
        BigDecimal dwyjcehj = getDWYJCEHJ(qcxq);
        BigDecimal gryjcehj = getGRYJCEHJ(qcxq);

        inventoryMessage.setDWYJCEHJ(dwyjcehj.toString());
        inventoryMessage.setGRYJCEHJ(gryjcehj.toString());
        inventoryMessage.setFSE(fse.toString());

        inventoryMessage.setDWJCRS(dwjcrs.toString());
        inventoryMessage.setFSRS(dwjcrs.toString());
        inventoryMessage.setDWFCRS(dwfcrs.toString());
    }

    private BigDecimal addToChangeList(InventoryChangeList changeList, ArrayList<InventoryDetail> indiAccSetMsg) {
        BigDecimal hghj = BigDecimal.ZERO;
        ArrayList<InventoryChangeDetail> changeDetailList = changeList.getChangeDetail();
        for(InventoryDetail  inventoryDetail: indiAccSetMsg){
            InventoryChangeDetail changeDetail = new InventoryChangeDetail();
            changeDetail.setGRZH(inventoryDetail.getGRZH());
            changeDetail.setXingMing(inventoryDetail.getXingMing());
            changeDetail.setZJHM(inventoryDetail.getZJHM());
            changeDetail.setFSE(inventoryDetail.getQCFSE());
            changeDetail.setBGYY("开户补缴");
            changeDetailList.add(changeDetail);
            hghj = hghj.add(ComUtils.toBigDec(inventoryDetail.getQCFSE()));
        }
        return hghj;
    }

    /**
     * 获取单位该月的最初的汇缴清册详情
     */
    @Override
    public FirstInventory getFirstInventoryOfMonth(TokenContext tokenContext,String ywlsh) {

        AssertUtils.notEmpty(ywlsh,"业务流水号不能为空！");
        CCollectionUnitDepositInventoryVice inventory = inventoryDAO.getByYwlsh(ywlsh);
        AssertUtils.notEmpty(inventory,"该业务流水号对应的清册信息不存在！");

        StCollectionUnitBusinessDetails dwywmx = inventory.getDwywmx();
        Set<CCollectionUnitDepositInventoryDetailVice> qcxq = inventory.getQcxq();
        String dwzh = dwywmx.getDwzh();
        StCommonUnit unit = dwywmx.getUnit();

        FirstInventory firstInventory = new FirstInventory();

        firstInventory.setQCQRDH(ywlsh);
        firstInventory.setDWZH(dwzh);
        firstInventory.setDWMC(unit.getDwmc());

        String jznyStr = getJzny(inventory.getQcny(),unit);
        String qcyf = ComUtils.parseToYYYYMM2(inventory.getQcny());

        firstInventory.setJZNY(jznyStr);
        firstInventory.setQCNYQ(qcyf);
        firstInventory.setQCNYZ(qcyf);
        firstInventory.setQCZFSE(inventory.getQcfse().toString());
        if(!ComUtils.isEmpty(inventory.getQcpc())){
            firstInventory.setWFTYE(inventory.getQcpc().getWftye().toString());
        }else{
            firstInventory.setWFTYE("0.00");
        }

        firstInventory.setJBRXM(unit.getJbrxm());
        firstInventory.setJBRZJLX(unit.getJbrzjlx());
        firstInventory.setJBRZJHM(unit.getJbrzjhm());

        if(!ComUtils.isEmpty(tokenContext)){
            firstInventory.setCZY(tokenContext.getUserInfo().getCZY());
            CAccountNetwork network = cAccountNetworkDAO.get(tokenContext.getUserInfo().getYWWD());
            firstInventory.setYWWD(network.getMingCheng());
        }

        InventoryMessage inventoryMessage= getInventoryMessage(inventory);
        firstInventory.setInventoryMessage(inventoryMessage);


        return firstInventory;
    }

    private String getJzny(String qcyf, StCommonUnit unit) {
        String dwschjny = unit.getExtension().getDwschjny();
        if(qcyf.equals(dwschjny)){
            return "";
        }else{
            Date jzny = ComUtils.getLastMonth(ComUtils.parseToDate(qcyf,"yyyyMM"));
            return ComUtils.parseToString(jzny,"yyyy-MM");
        }
    }

    @Override
    public InventoryInit getUnitInventoryInit(TokenContext tokenContext,String dwzh) {
        AssertUtils.notEmpty(dwzh,"单位账号不能为空！");
        StCommonUnit unit = unitDAO.getUnit(dwzh);
        AssertUtils.notEmpty(unit,"单位账号对应的单位信息不存在！");

        InventoryInit inventoryInit = new InventoryInit();
        inventoryInit.setDWZH(unit.getDwzh());
        inventoryInit.setDWMC(unit.getDwmc());
        String jzny1 = unit.getCollectionUnitAccount().getJzny();
        String jzny = ComUtils.isEmpty(jzny1) ? "" : ComUtils.parseToYYYYMM2(jzny1) ;
        inventoryInit.setJZNY(jzny);

        String yhjny = BusUtils.getDWYHJNY(dwzh);
        inventoryInit.setQCNYQ(yhjny);
        inventoryInit.setQCNYZ(yhjny);
        inventoryInit.setWFTYE(unit.getCollectionUnitAccount().getExtension().getZsye().toString());

        inventoryInit.setJBRXM(unit.getJbrxm());
        inventoryInit.setJBRZJLX(unit.getJbrzjlx());
        inventoryInit.setJBRZJHM(unit.getJbrzjhm());

        inventoryInit.setCZY(tokenContext.getUserInfo().getCZY());
        CAccountNetwork network = cAccountNetworkDAO.get(tokenContext.getUserInfo().getYWWD());
        inventoryInit.setYWWD(network.getMingCheng());

        return inventoryInit;
    }

    @Override
    public LatestInventory getLatestInventoryOfMonth(String dwzh, Date sxyf) {
        String qcyf = ComUtils.parseToString(sxyf,"yyyyMM");
        //1、查询出单位当月的清册
        CCollectionUnitDepositInventoryVice inventory = inventoryDAO.getInventory(dwzh, qcyf);
        String ywlsh = inventory.getDwywmx().getYwlsh();
        LatestInventory latestInventory = getLatestInventoryOfMonth(null, ywlsh);
        return latestInventory;
    }

    private InventoryMessage getInventoryMessage(CCollectionUnitDepositInventoryVice inventory) {

        String qcny = ComUtils.parseToYYYYMM2(inventory.getQcny());
        StCollectionUnitBusinessDetails dwywmx = inventory.getDwywmx();
        Set<CCollectionUnitDepositInventoryDetailVice> qcxq = inventory.getQcxq();

        InventoryMessage inventoryMessage = new InventoryMessage();

        inventoryMessage.setQCNY(qcny);
        inventoryMessage.setFSRS(dwywmx.getFsrs().toString());

        inventoryMessage.setFSE(inventory.getQcfse().toString());
        inventoryMessage.setDWYJCEHJ(inventory.getDwyjcehj().toString());
        inventoryMessage.setGRYJCEHJ(inventory.getGryjcehj().toString());

        inventoryMessage.setDWFCRS(inventory.getDwfcrs().toString());
        inventoryMessage.setDWJCRS(inventory.getDwjcrs().toString());

        inventoryMessage.setDWJCBL(changeToView(inventory.getDwjcbl().toString()));
        inventoryMessage.setGRJCBL(changeToView(inventory.getGrjcbl().toString()));

        ArrayList<InventoryDetail> qcxqView = new ArrayList<InventoryDetail>();
        for(CCollectionUnitDepositInventoryDetailVice detailVice : qcxq){
            String grzh = detailVice.getGrzh();
            StCommonPerson person = personDAO.getByGrzh(grzh);
            InventoryDetail inventoryDetail= new InventoryDetail();

            inventoryDetail.setGRZH(grzh);
            inventoryDetail.setXingMing(person.getXingMing());
            inventoryDetail.setZJHM(person.getZjhm());

            inventoryDetail.setGRJCJS(detailVice.getGrjcjs().toString());
            inventoryDetail.setDWYJCE(detailVice.getDwyjce().toString());
            inventoryDetail.setGRYJCE(detailVice.getGryjce().toString());
            inventoryDetail.setYJCE(detailVice.getYjce().toString());
            inventoryDetail.setQCFSE(detailVice.getQcfse().toString());
            inventoryDetail.setGRZHZT(detailVice.getGrzhzt());
            inventoryDetail.setGRZHYE(detailVice.getGrzhye().toString());

            qcxqView.add(inventoryDetail);
        }

        inventoryMessage.setQCXQ(qcxqView);

        return inventoryMessage;
    }

    /**
     * 缴存比例乘100,显示
     */
    private String changeToView(String jcbl) {
        return new BigDecimal(jcbl).multiply(new BigDecimal("100")).toString();
    }


    public PageRes<InventoryDetail>  getInventoryDetail(String dwzh,String qcyf,String xingMing, String pageNumber, String pageSize){

        //得到基础清册列表
        ArrayList<InventoryDetail> detailList = getInventoryDetail(dwzh, qcyf);

        AssertUtils.notEmpty(detailList,"当前清册中没有职工信息！");

        //根据姓名，对列表进行模糊匹配
        nameMatch(detailList,xingMing);

        //对列表进行排序、封存的人至后
        sortList(detailList);

        //对清册进行分页
        PageRes<InventoryDetail> inventoryPage = getInventoryPage(detailList,pageNumber,pageSize);

        return inventoryPage;
    }

    /**
     * 兼容已生成清册的情况
     * 月份格式的不统一，很蛋疼！
     */
    private ArrayList<InventoryDetail> getInventoryDetail(String dwzh, String qcyf) {
        String qcyf2 = ComUtils.parseToYYYYMM(qcyf);
        CCollectionUnitDepositInventoryVice inventory = inventoryDAO.getInventory(dwzh, qcyf2);
        if(inventory != null){
            ArrayList<InventoryDetail> inventoryDetailList = inventoryDAO.getInventoryDetailList(inventory.getId());
            return inventoryDetailList;
        }else{
            Date qcny = ComUtils.parseToDate(qcyf, "yyyy-MM");
            //得到该单位该月的缴存比例
            Map map = getJCBL2(dwzh,qcny);
            return createInventoryListOf(dwzh, qcny, map);
        }
    }

    @Override
    public PageResNew<ListInventoryResRes> getUnitInventoryInfo(TokenContext tokenContext, String dwmc, String dwzh, String czy, String ywwd, String kssj, String jssj, String marker, String pageSIZE, String action) {

        ListAction listAction = ListAction.pageType(action);
        int pageSize = ComUtils.parstPageSize(pageSIZE);
        //验证受理的开始时间与结束时间
        IBaseDAO.CriteriaExtension ce = getCriteriaExtension(dwmc,dwzh,czy,ywwd);

        Date startDate  = ComUtils.parseToDate(kssj,"yyyy-MM-dd HH:mm");
        Date endDate  = ComUtils.parseToDate(jssj,"yyyy-MM-dd HH:mm");

        PageResults<CCollectionUnitDepositInventoryVice> pages = null;
        try {
             pages = inventoryDAO.listWithMarker(null, startDate, endDate, "created_at", Order.DESC, null, SearchOption.REFINED, marker, pageSize, listAction,ce);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorException(e);
        }

        PageResNew<ListInventoryResRes> result = new PageResNew<>();
        ArrayList<ListInventoryResRes> list = getResultList(pages.getResults());
        result.setResults(action,list);

        return result;
    }

    private IBaseDAO.CriteriaExtension getCriteriaExtension(String dwmc, String dwzh, String czy, String ywwd) {
        return  new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                criteria.createAlias("dwywmx", "dwywmx");
                criteria.createAlias("dwywmx.cCollectionUnitBusinessDetailsExtension", "cCollectionUnitBusinessDetailsExtension");
                if(!ComUtils.isEmpty(dwmc)){
                    criteria.createAlias("dwywmx.unit", "unit");
                    criteria.add(Restrictions.like("unit.dwmc","%"+dwmc+"%"));
                }
                if(!ComUtils.isEmpty(dwzh)){
                    criteria.add(Restrictions.like("dwywmx.dwzh","%"+dwzh+"%"));
                }
                if(!ComUtils.isEmpty(czy)){
                    criteria.add(Restrictions.like("cCollectionUnitBusinessDetailsExtension.czy","%"+czy+"%"));
                }

                if(!ComUtils.isEmpty(ywwd)){
                    criteria.createAlias("cCollectionUnitBusinessDetailsExtension.ywwd", "ywwd");
                    criteria.add(Restrictions.eq("ywwd.id",ywwd));
                }
            }
        };
    }

    @Override
    public CommonResponses getReceipt(TokenContext tokenContext,String dwzh, String qcnyStr) {
        HeadRemittanceInventoryRes result = getInventoryConfiromationData(tokenContext,dwzh,qcnyStr);
        String id = pdfService.getRemittanceInventoryReceiptPdf(result);
        System.out.println("生成id的值："+id);
        CommonResponses commonResponses = new CommonResponses();
        commonResponses.setId(id);
        commonResponses.setState("success");
        return commonResponses;

    }
    public HeadRemittanceInventoryRes getInventoryConfiromationData(TokenContext tokenContext,String dwzh, String qcnyStr){
        Date qcny = ComUtils.parseToDate(qcnyStr, "yyyy-MM");
        InventoryMessage inventory = getInventory2(dwzh,qcny);

        StCommonUnit unit = unitDAO.getUnit(dwzh);
        String khwd = unit.getExtension().getKhwd();
        CAccountNetwork cAccountNetwork = cAccountNetworkDAO.get(khwd);

        HeadRemittanceInventoryRes result = new HeadRemittanceInventoryRes();
        result.setYWLSH("");
        result.setDWZH(dwzh);
        result.setDWMC(unit.getDwmc());
        result.setDWFCRS(Integer.parseInt(inventory.getDWFCRS()));
        result.setDWJCRS(Integer.parseInt(inventory.getDWJCRS()));
        result.setFSE(inventory.getFSE());
        result.setFSRS(Integer.parseInt(inventory.getDWJCRS()));
        result.setQCNY(qcnyStr);
        result.setYWWD(cAccountNetwork.getMingCheng());
        result.setCZY(tokenContext.getUserInfo().getCZY());

        result.setTZSJ(ComUtils.parseToString(new Date(),"yyyy-MM-dd"));
        result.setYZM("");

        result.setJBRXM(unit.getJbrxm());
        result.setJBRZJHM(unit.getJbrzjhm());
        result.setJBRZJLX(unit.getJbrzjlx());
        //清册详情
        ArrayList<HeadRemittanceInventoryResDWHJQC> list = new ArrayList<HeadRemittanceInventoryResDWHJQC>();
        ArrayList<InventoryDetail> qcxq = inventory.getQCXQ();
        //计算总合计
        String dwyhjze = "0.00";
        String gryhjze = "0.00";

        for(InventoryDetail perDetail : qcxq){
            if("01".equals(perDetail.getGRZHZT())){
                HeadRemittanceInventoryResDWHJQC perView = new HeadRemittanceInventoryResDWHJQC();
                String dwyjce = perDetail.getDWYJCE();
                String gryjce = perDetail.getGRYJCE();
                perView.setDWYJCE(perDetail.getDWYJCE());
                perView.setGRYJCE(perDetail.getGRYJCE());
                perView.setHeJi(perDetail.getYJCE());
                perView.setGRJCJS(perDetail.getGRJCJS());
                perView.setGRZH(perDetail.getGRZH());
                //查询个人信息 以后考虑冗余
                StCommonPerson person = personDAO.getByGrzh(perDetail.getGRZH());
                perView.setXingMing(person.getXingMing());
                perView.setZJHM(person.getZjhm());
                perView.setZJLX(person.getZjlx());
                list.add(perView);

                dwyhjze = ComUtils.addMoney(dwyhjze, dwyjce);
                gryhjze = ComUtils.addMoney(gryhjze, gryjce);
            }
        }
        result.setDWHJQC(list);

        HeadRemittanceInventoryResYJEZHJ heji = new HeadRemittanceInventoryResYJEZHJ();
        heji.setDWYJCEZHJ(dwyhjze.toString());
        heji.setGRYJCEZHJ(gryhjze.toString());
        heji.setZHJ(inventory.getFSE());
        result.setYJEZHJ(heji);
        return result;
    }
    public ExportInventoryConfirmationInfoRes getExportInventoryConfirmationInfo(String dwzh, String qcnyStr){
        Date qcny = ComUtils.parseToDate(qcnyStr, "yyyy-MM");
        InventoryMessage inventory = getInventory2(dwzh,qcny);
        StCommonUnit unit = unitDAO.getUnit(dwzh);
        String khwd = unit.getExtension().getKhwd();
        CAccountNetwork cAccountNetwork = cAccountNetworkDAO.get(khwd);
        ExportInventoryConfirmationInfoRes exportInfo = new ExportInventoryConfirmationInfoRes();
        exportInfo.setDWZH(dwzh);
        exportInfo.setDWMC(unit.getDwmc());
        exportInfo.setFSE(inventory.getFSE());
        exportInfo.setTZSJ(ComUtils.parseToString(new Date(),"yyyy-MM-dd"));
        exportInfo.setQCNY(qcnyStr);
        exportInfo.setFSRS(Integer.parseInt(inventory.getDWJCRS()));
        exportInfo.setYWWD(cAccountNetwork.getMingCheng());
        exportInfo.setInventoryMessage(inventory);
       return exportInfo;
    }
    /**
     * 2种情况：未生成时打印与已生成时打印
     */
    @Override
    public InventoryMessage getInventory2(String dwzh, Date qcny) {
        String qcny2 = ComUtils.parseToString(qcny, "yyyyMM");
        CCollectionUnitDepositInventoryVice inventoryVice = inventoryDAO.getInventory(dwzh, qcny2);
        if(inventoryVice != null){
            //已生成的情况
            InventoryMessage inventory = new InventoryMessage();
            ArrayList<InventoryDetail> detail = getInventoryDetail(dwzh, ComUtils.parseToString(qcny,"yyyy-MM"));
            //对列表进行排序、封存的人至后
            sortList(detail);
            String dwjcrs = inventoryVice.getDwjcrs().setScale(0).toString();
            inventory.setFSRS(dwjcrs);
            inventory.setDWFCRS(inventoryVice.getDwfcrs().setScale(0).toString());
            inventory.setDWJCRS(dwjcrs);

            inventory.setDWJCBL(changeToView(inventoryVice.getDwjcbl().toString()));
            inventory.setGRJCBL(changeToView(inventoryVice.getGrjcbl().toString()));

            inventory.setDWYJCEHJ(inventoryVice.getDwyjcehj().toString());
            inventory.setGRYJCEHJ(inventoryVice.getGryjcehj().toString());
            inventory.setFSE(ComUtils.moneyFormat(inventoryVice.getQcfse()));

            inventory.setQCNY(ComUtils.parseToString(qcny, "yyyy-MM"));
            inventory.setQCXQ(detail);
            return inventory;
        }else{
            return getInventory(dwzh,qcny);
        }
    }

    ArrayList<ListInventoryResRes> getResultList(List<CCollectionUnitDepositInventoryVice> results){
        ArrayList<ListInventoryResRes> resultList = new ArrayList<>();
        for(CCollectionUnitDepositInventoryVice dwqcMsg : results){
            StCollectionUnitBusinessDetails dwywmx = dwqcMsg.getDwywmx();
            String qcny = dwqcMsg.getQcny();
            Date bjsj = dwqcMsg.getDwywmx().getExtension().getBjsj();

            ListInventoryResRes dwqcView = new ListInventoryResRes();
            dwqcView.setDWZH(dwywmx.getDwzh());
            dwqcView.setDWMC(dwywmx.getUnit().getDwmc());
            dwqcView.setQCNY(ComUtils.parseToYYYYMM2(qcny));
            dwqcView.setQCQRDH(dwqcMsg.getQcqrdh());
            dwqcView.setFSE(dwqcMsg.getQcfse().toString());
            dwqcView.setFSRS(dwywmx.getFsrs().toString());
            dwqcView.setSLSJ(ComUtils.parseToString(dwywmx.getExtension().getSlsj(),"yyyy-MM-dd HH:mm"));
            dwqcView.setCZY(dwywmx.getExtension().getCzy());
            dwqcView.setYWWD(dwywmx.getExtension().getYwwd().getMingCheng());
            dwqcView.setId(dwqcMsg.getId());

            boolean sfyc = ckeckSFYC(dwywmx.getDwzh(), qcny, bjsj);
            dwqcView.setSFYC(sfyc);

            resultList.add(dwqcView);
        }
        return resultList;
    }


    private void sortList(ArrayList<InventoryDetail> detailList) {
        Collections.sort(detailList);
    }

    private PageRes<InventoryDetail> getInventoryPage(ArrayList<InventoryDetail> detailList, String pageNumber, String pageSize) {
        PageRes<InventoryDetail> result = new PageRes<>();
        int totalCount = detailList.size();
        int pageNo = ComUtils.parstPageNo(pageNumber);
        int pageSi = ComUtils.parstPageSize(pageSize);

        int pageCount = getPageCount(totalCount,pageSi);
        pageNo = getPageNo(pageNo,pageCount);

        if(pageNo == 0) return result;

        int strat = pageSi * (pageNo - 1);
        int end = pageSi * pageNo <= totalCount ? pageSi * pageNo - 1 : totalCount - 1;

        ArrayList<InventoryDetail> list = new ArrayList<>();
        for(int i = strat; i <= end; i++){
            list.add(detailList.get(i));
        }

        result.setResults(list);
        result.setPageCount(pageCount);
        result.setPageSize(pageSi);
        result.setNextPageNo(pageNo+1);
        result.setCurrentPage(pageNo);
        result.setTotalCount(totalCount);
        return result;
    }

    private int getPageNo(int pageNo, int pageCount) {
        if(pageNo <= 0 ){
            return 1;
        }else if(pageNo > pageCount){
            return pageCount;
        }else{
            return pageNo;
        }
    }

    private int getPageCount(int totalCount, int pageSi) {
        if(totalCount % pageSi == 0){
            return totalCount/pageSi;
        }
        return totalCount/pageSi + 1;
    }

    private void nameMatch(ArrayList<InventoryDetail> detailList, String xingMing) {
        //非空时进行匹配
        if(!ComUtils.isEmpty(xingMing)){
            Iterator<InventoryDetail> iterator = detailList.iterator();
            while(iterator.hasNext()) {
                InventoryDetail inventoryDetail = iterator.next();
                String xingMing2 = inventoryDetail.getXingMing();
                if (xingMing2.indexOf(xingMing) == -1){
                    iterator.remove();
                }
            }
        }
    }
}
