package com.handge.housingfund.collection.service.businessdetail;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessStatus;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.collection.model.deposit.GetUnitBusnissDetailListResRes;
import com.handge.housingfund.common.service.collection.service.businessdetail.UnitBusiness;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.IBaseDAO;
import com.handge.housingfund.database.dao.IStCollectionUnitBusinessDetailsDAO;
import com.handge.housingfund.database.dao.IStCommonUnitDAO;
import com.handge.housingfund.database.entities.PageResults;
import com.handge.housingfund.database.entities.StCollectionUnitBusinessDetails;
import com.handge.housingfund.database.entities.StCommonUnit;
import com.handge.housingfund.database.enums.ListAction;
import com.handge.housingfund.database.enums.Order;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Liujuhao on 2017/7/5.
 */
@Component
public class UnitBusinessImpl implements UnitBusiness {

    private Logger logger = LogManager.getLogger(UnitBusinessImpl.class);
    //单位业务明细
    @Autowired
    private IStCollectionUnitBusinessDetailsDAO collection_unit_business_details;
    @Autowired
    private IStCommonUnitDAO istCommonUnitDAO;

    private SimpleDateFormat simymd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat simM = new SimpleDateFormat("yyyyMM");
    private SimpleDateFormat simym = new SimpleDateFormat("yyyy-MM");

    @Override
    public PageRes<GetUnitBusnissDetailListResRes> showUnitBusinessDetails(TokenContext tokenContext, String DWMC, String DWZH, String YWMXLX, String pageNumber, String pageSize, String start, String end) {
        //格式检查以及条件查询
        if (!StringUtil.notEmpty(pageSize)) throw new ErrorException("页码不能为空");

        HashMap<String, Object> search_map = new HashMap<>();
        if (StringUtil.notEmpty(YWMXLX) && !CollectionBusinessType.所有.getCode().equals(YWMXLX)) {
            search_map.put("cCollectionUnitBusinessDetailsExtension.czmc", YWMXLX);
        }
        if (StringUtil.notEmpty(tokenContext.getUserInfo().getYWWD()) && !tokenContext.getUserInfo().getYWWD().equals("1")) {
            search_map.put("cCollectionUnitBusinessDetailsExtension.ywwd.id", tokenContext.getUserInfo().getYWWD());
        }
        search_map.put("cCollectionUnitBusinessDetailsExtension.step", Arrays.asList(CollectionBusinessStatus.办结.getName(), CollectionBusinessStatus.已入账.getName(), CollectionBusinessStatus.已入账分摊.getName()));
        PageResults<StCollectionUnitBusinessDetails> stCollectionUnitBusinessDetails = null;
        try {
            stCollectionUnitBusinessDetails = collection_unit_business_details.listWithPage(search_map,
                    null, null, "created_at", Order.DESC, null, null, Integer.parseInt(pageNumber), Integer.parseInt(pageSize), new IBaseDAO.CriteriaExtension() {
                        @Override
                        public void extend(Criteria criteria) {
                            try {
                                criteria.createAlias("unit", "unit");

                                criteria.add(
                                        Restrictions.between(
                                                "created_at",
                                                !StringUtil.notEmpty(start) ? simymd.parse("1970-01-01") : simymd.parse(start),
                                                !StringUtil.notEmpty(end) ? new Date() : simymd.parse(end)));

                                if (StringUtil.notEmpty(DWZH)) {
                                    criteria.add(Restrictions.like("dwzh", "%" + DWZH + "%"));
                                }
                                if (StringUtil.notEmpty(DWMC)) {
                                    criteria.add(Restrictions.like("unit.dwmc", "%" + DWMC + "%"));
                                }
                            } catch (ParseException e) {
                                throw new ErrorException(e + ":yyyy-MM-dd");
                            }
                        }
                    });
        } catch (NumberFormatException e) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "页码格式有误");
        } catch (Exception e) {
            throw new ErrorException(e);
        }
        List<StCollectionUnitBusinessDetails> collectionUnitBusinessDetails = stCollectionUnitBusinessDetails.getResults();
        PageRes<GetUnitBusnissDetailListResRes> pageres = new PageRes<>();
        ArrayList<GetUnitBusnissDetailListResRes> list = new ArrayList<>();
        GetUnitBusnissDetailListResRes getUnitBusnissDetailListResRes = null;
        for (StCollectionUnitBusinessDetails ectionUnitBusinessDetails : collectionUnitBusinessDetails) {
            getUnitBusnissDetailListResRes = new GetUnitBusnissDetailListResRes();
            getUnitBusnissDetailListResRes.setYWLSH(ectionUnitBusinessDetails.getYwlsh());
            getUnitBusnissDetailListResRes.setDWZH(ectionUnitBusinessDetails.getDwzh());
            if (ectionUnitBusinessDetails.getUnit() == null) {
                StCommonUnit sollectionUnitAccount = istCommonUnitDAO.getUnit(ectionUnitBusinessDetails.getDwzh());
                if (sollectionUnitAccount != null) {
                    getUnitBusnissDetailListResRes.setDWMC(sollectionUnitAccount.getDwmc());
                }
            }else {
                getUnitBusnissDetailListResRes.setDWMC(ectionUnitBusinessDetails.getUnit().getDwmc());
            }
            if (ectionUnitBusinessDetails.getExtension() != null) {
                getUnitBusnissDetailListResRes.setYWMXLX(ectionUnitBusinessDetails.getExtension().getCzmc());
            }
            getUnitBusnissDetailListResRes.setFSE(ectionUnitBusinessDetails.getFse() == null ? null : ectionUnitBusinessDetails.getFse().abs()+"");
            getUnitBusnissDetailListResRes.setFSLXE(ectionUnitBusinessDetails.getFslxe() == null ? null : ectionUnitBusinessDetails.getFslxe().abs()+"");
            getUnitBusnissDetailListResRes.setFSRS(ectionUnitBusinessDetails.getFsrs().abs() + "");
            try {
                getUnitBusnissDetailListResRes.setHBJNY(simym.format(simM.parse(ectionUnitBusinessDetails.getHbjny())));
            } catch (Exception e) {
                e.printStackTrace();
            }
            getUnitBusnissDetailListResRes.setJZRQ(sim.format(ectionUnitBusinessDetails.getCreated_at()));
            getUnitBusnissDetailListResRes.setCZBS(ectionUnitBusinessDetails.getCzbz());
            list.add(getUnitBusnissDetailListResRes);
        }
        pageres.setResults(list);
        pageres.setPageCount(stCollectionUnitBusinessDetails.getPageCount());
        pageres.setCurrentPage(stCollectionUnitBusinessDetails.getCurrentPage());
        pageres.setNextPageNo(stCollectionUnitBusinessDetails.getPageNo());
        pageres.setPageSize(stCollectionUnitBusinessDetails.getPageSize());
        pageres.setTotalCount(stCollectionUnitBusinessDetails.getTotalCount());

        return pageres;
    }

    @Override
    public PageResNew<GetUnitBusnissDetailListResRes> showUnitBusinessDetailsnew(TokenContext tokenContext, String DWMC, String DWZH, String YWMXLX, String marker, String pageSize, String start, String end, String action) {
        //格式检查以及条件查询
        if (!StringUtil.notEmpty(pageSize)) throw new ErrorException("页码不能为空");

        HashMap<String, Object> search_map = new HashMap<>();
        if (StringUtil.notEmpty(YWMXLX) && !CollectionBusinessType.所有.getCode().equals(YWMXLX)) {
            search_map.put("cCollectionUnitBusinessDetailsExtension.czmc", YWMXLX);
        }
        if (StringUtil.notEmpty(tokenContext.getUserInfo().getYWWD()) && !tokenContext.getUserInfo().getYWWD().equals("1")) {
            search_map.put("cCollectionUnitBusinessDetailsExtension.ywwd.id", tokenContext.getUserInfo().getYWWD());
        }
        search_map.put("cCollectionUnitBusinessDetailsExtension.step", Arrays.asList(CollectionBusinessStatus.办结.getName(), CollectionBusinessStatus.已入账.getName(), CollectionBusinessStatus.已入账分摊.getName()));
        PageResults<StCollectionUnitBusinessDetails> stCollectionUnitBusinessDetails = null;
        try {
            stCollectionUnitBusinessDetails = collection_unit_business_details.listWithMarker(search_map, StringUtil.isEmpty(start) ? null : simymd.parse(start), StringUtil.isEmpty(end) ? null : simymd.parse(end), "created_at", Order.DESC, null, null, marker, Integer.parseInt(pageSize), ListAction.pageType(action), new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    criteria.createAlias("unit", "unit");

//                        criteria.add(
//                                Restrictions.between(
//                                        "created_at",
//                                        !StringUtil.notEmpty(start) ? simymd.parse("1970-01-01") : simymd.parse(start),
//                                        !StringUtil.notEmpty(end) ? new Date() : simymd.parse(end)));

                    if (StringUtil.notEmpty(DWZH)) {
                        criteria.add(Restrictions.like("dwzh", "%" + DWZH + "%"));
                    }
                    if (StringUtil.notEmpty(DWMC)) {
                        criteria.add(Restrictions.like("unit.dwmc", "%" + DWMC + "%"));
                    }
                }

            });
        } catch (ParseException e) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, ":yyyy-MM-dd");
        } catch (NumberFormatException e) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "页码格式有误");
        } catch (Exception e) {
            throw new ErrorException(e);
        }
        List<StCollectionUnitBusinessDetails> collectionUnitBusinessDetails = stCollectionUnitBusinessDetails.getResults();
        PageResNew<GetUnitBusnissDetailListResRes> pageres = new PageResNew<>();
        ArrayList<GetUnitBusnissDetailListResRes> list = new ArrayList<>();
        GetUnitBusnissDetailListResRes getUnitBusnissDetailListResRes = null;
        for (StCollectionUnitBusinessDetails ectionUnitBusinessDetails : collectionUnitBusinessDetails) {
            getUnitBusnissDetailListResRes = new GetUnitBusnissDetailListResRes();
            getUnitBusnissDetailListResRes.setId(ectionUnitBusinessDetails.getId());
            getUnitBusnissDetailListResRes.setYWLSH(ectionUnitBusinessDetails.getYwlsh());
            getUnitBusnissDetailListResRes.setDWZH(ectionUnitBusinessDetails.getDwzh());
            if (ectionUnitBusinessDetails.getUnit() == null) {
                StCommonUnit sollectionUnitAccount = istCommonUnitDAO.getUnit(ectionUnitBusinessDetails.getDwzh());
                if (sollectionUnitAccount != null) {
                    getUnitBusnissDetailListResRes.setDWMC(sollectionUnitAccount.getDwmc());
                }
            }else {
                getUnitBusnissDetailListResRes.setDWMC(ectionUnitBusinessDetails.getUnit().getDwmc());
            }


            if (ectionUnitBusinessDetails.getExtension() != null) {
                getUnitBusnissDetailListResRes.setYWMXLX(ectionUnitBusinessDetails.getExtension().getCzmc());
            }
            getUnitBusnissDetailListResRes.setFSE(ectionUnitBusinessDetails.getFse() == null ? null : ectionUnitBusinessDetails.getFse().abs()+"");
            getUnitBusnissDetailListResRes.setFSLXE(ectionUnitBusinessDetails.getFslxe() == null ? null : ectionUnitBusinessDetails.getFslxe().abs()+"");
            getUnitBusnissDetailListResRes.setFSRS(ectionUnitBusinessDetails.getFsrs().abs() + "");
            try {
                getUnitBusnissDetailListResRes.setHBJNY(simym.format(simM.parse(ectionUnitBusinessDetails.getHbjny())));
            } catch (Exception e) {
                e.printStackTrace();
            }
            getUnitBusnissDetailListResRes.setJZRQ(sim.format(ectionUnitBusinessDetails.getCreated_at()));
            getUnitBusnissDetailListResRes.setCZBS(ectionUnitBusinessDetails.getCzbz());
            list.add(getUnitBusnissDetailListResRes);
        }
        pageres.setResults(action, list);
        return pageres;
    }
}
