package com.handge.housingfund.collection.service.businessdetail;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessStatus;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.collection.model.deposit.GetIndiBusnissDetailListResRes;
import com.handge.housingfund.common.service.collection.service.businessdetail.IndiBusiness;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.IBaseDAO;
import com.handge.housingfund.database.dao.IStCollectionPersonalAccountDAO;
import com.handge.housingfund.database.dao.IStCollectionPersonalBusinessDetailsDAO;
import com.handge.housingfund.database.entities.PageResults;
import com.handge.housingfund.database.entities.StCollectionPersonalAccount;
import com.handge.housingfund.database.entities.StCollectionPersonalBusinessDetails;
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
public class IndiBusinessImpl implements IndiBusiness {

    private Logger logger = LogManager.getLogger(IndiBusinessImpl.class);
    //单位业务明细
    @Autowired
    private IStCollectionPersonalBusinessDetailsDAO collection_person_business_details;
    @Autowired
    private IStCollectionPersonalAccountDAO iStCollectionPersonalAccountDAO;

    private SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat simymd = new SimpleDateFormat("yyyy-MM-dd HH:mm");


    @Override
    public PageRes<GetIndiBusnissDetailListResRes> showIndiBusinessDetails(TokenContext tokenContext, final String XingMing, final String GRZH, final String YWMXLX, final String pageNumber, final String pageSize,
                                                                           String start, String end,String ZJHM) {
        if (!StringUtil.notEmpty(pageNumber) || !StringUtil.notEmpty(pageSize)) throw new ErrorException("页码不能为空");


        //格式检查以及条件查询
        HashMap<String, Object> search_map = new HashMap<>();
        if (StringUtil.notEmpty(YWMXLX) && !YWMXLX.equals(CollectionBusinessType.所有.getCode())) {
            search_map.put("cCollectionPersonalBusinessDetailsExtension.czmc", YWMXLX);
        }
        if (StringUtil.notEmpty(tokenContext.getUserInfo().getYWWD()) && !tokenContext.getUserInfo().getYWWD().equals("1")) {
            search_map.put("cCollectionPersonalBusinessDetailsExtension.ywwd.id", tokenContext.getUserInfo().getYWWD());
        }
        search_map.put("cCollectionPersonalBusinessDetailsExtension.step", Arrays.asList(CollectionBusinessStatus.办结.getName(), CollectionBusinessStatus.已入账.getName(), CollectionBusinessStatus.已入账分摊.getName()));

        PageResults<StCollectionPersonalBusinessDetails> stCollectionPersonalBusinessDetails = null;
        try {
            stCollectionPersonalBusinessDetails = collection_person_business_details.listWithPage(search_map,
                    null, null, "created_at", Order.DESC, null, null, Integer.parseInt(pageNumber), Integer.parseInt(pageSize), new IBaseDAO.CriteriaExtension() {
                        @Override
                        public void extend(Criteria criteria) {
                            try {
                                criteria.createAlias("person", "person");

                                criteria.add(
                                        Restrictions.between(
                                                "created_at",
                                                !StringUtil.notEmpty(start) ? simymd.parse("1970-01-01 00:01") : simymd.parse(start),
                                                !StringUtil.notEmpty(end) ? new Date() : simymd.parse(end)));

                                if (StringUtil.notEmpty(GRZH)) {
                                    criteria.add(Restrictions.like("grzh", "%" + GRZH + "%"));
                                }
                                if (StringUtil.notEmpty(XingMing)) {
                                    criteria.add(Restrictions.like("person.xingMing", "%" + XingMing + "%"));
                                }
                                if (StringUtil.notEmpty(ZJHM)) {
                                    criteria.add(Restrictions.like("person.zjhm", "%" + ZJHM + "%"));
                                }

                            } catch (ParseException e) {
                                throw new ErrorException(e + ":yyyy-MM-dd");
                            }
                        }
                    });
        } catch (NumberFormatException e) {
            throw new ErrorException(e);
        } catch (Exception e) {
            throw new ErrorException(e);
        }
        List<StCollectionPersonalBusinessDetails> collectionPersonalBusinessDetails = stCollectionPersonalBusinessDetails.getResults();
        PageRes<GetIndiBusnissDetailListResRes> pageres = new PageRes<>();
        ArrayList<GetIndiBusnissDetailListResRes> list = new ArrayList<GetIndiBusnissDetailListResRes>();
        GetIndiBusnissDetailListResRes getIndiBusnissDetailListResRes = null;
        for (StCollectionPersonalBusinessDetails ectionPersonalBusinessDetails : collectionPersonalBusinessDetails) {
            getIndiBusnissDetailListResRes = new GetIndiBusnissDetailListResRes();
            getIndiBusnissDetailListResRes.setYWLSH(ectionPersonalBusinessDetails.getYwlsh());
            getIndiBusnissDetailListResRes.setCZBS(ectionPersonalBusinessDetails.getCzbz());
            getIndiBusnissDetailListResRes.setGRZH(ectionPersonalBusinessDetails.getGrzh());
            if (ectionPersonalBusinessDetails.getPerson() == null) {
                StCollectionPersonalAccount stCollectionPersonalAccount = iStCollectionPersonalAccountDAO.getByGrzh(ectionPersonalBusinessDetails.getGrzh());
                if (stCollectionPersonalAccount != null) {
                    getIndiBusnissDetailListResRes.setXingMing(stCollectionPersonalAccount.getGrckzhhm());

                }
            } else {
                getIndiBusnissDetailListResRes.setXingMing(ectionPersonalBusinessDetails.getPerson().getXingMing());
            }


            if (ectionPersonalBusinessDetails.getExtension() != null) {
                getIndiBusnissDetailListResRes.setYWMXLX(ectionPersonalBusinessDetails.getExtension().getCzmc());
            }
            getIndiBusnissDetailListResRes.setFSE(ectionPersonalBusinessDetails.getFse() == null ? null : ectionPersonalBusinessDetails.getFse().abs() + "");
            getIndiBusnissDetailListResRes.setDNGJFSE(ectionPersonalBusinessDetails.getDngjfse().abs() + "");
            getIndiBusnissDetailListResRes.setSNJZFSE(ectionPersonalBusinessDetails.getSnjzfse().abs() + "");
            if(ectionPersonalBusinessDetails.getPerson()!=null) {
                getIndiBusnissDetailListResRes.setZJHM(ectionPersonalBusinessDetails.getPerson().getZjhm());
            }
            getIndiBusnissDetailListResRes.setFSLXE(ectionPersonalBusinessDetails.getFslxe().abs() + "");
            getIndiBusnissDetailListResRes.setTQYY(ectionPersonalBusinessDetails.getTqyy());
            getIndiBusnissDetailListResRes.setTQFS(ectionPersonalBusinessDetails.getTqfs());
            getIndiBusnissDetailListResRes.setJZRQ(sim.format(ectionPersonalBusinessDetails.getCreated_at()));
            list.add(getIndiBusnissDetailListResRes);
        }
        pageres.setResults(list);
        pageres.setCurrentPage(stCollectionPersonalBusinessDetails.getCurrentPage());
        pageres.setNextPageNo(stCollectionPersonalBusinessDetails.getPageNo());
        pageres.setPageSize(stCollectionPersonalBusinessDetails.getPageSize());
        pageres.setTotalCount(stCollectionPersonalBusinessDetails.getTotalCount());
        pageres.setPageCount(stCollectionPersonalBusinessDetails.getPageCount());

        return pageres;
    }

    @Override
    public PageResNew<GetIndiBusnissDetailListResRes> showIndiBusinessDetailsnew(TokenContext tokenContext, String XingMing, String GRZH, String YWMXLX, String marker, String pageSize, String start, String end, String action) {
        if (!StringUtil.notEmpty(pageSize)) throw new ErrorException("页码不能为空");


        //格式检查以及条件查询
        HashMap<String, Object> search_map = new HashMap<>();
        if (StringUtil.notEmpty(YWMXLX) && !YWMXLX.equals(CollectionBusinessType.所有.getCode())) {
            search_map.put("cCollectionPersonalBusinessDetailsExtension.czmc", YWMXLX);
        }
        if (StringUtil.notEmpty(tokenContext.getUserInfo().getYWWD()) && !tokenContext.getUserInfo().getYWWD().equals("1")) {
            search_map.put("cCollectionPersonalBusinessDetailsExtension.ywwd.id", tokenContext.getUserInfo().getYWWD());
        }
        search_map.put("cCollectionPersonalBusinessDetailsExtension.step", Arrays.asList(CollectionBusinessStatus.办结.getName(), CollectionBusinessStatus.已入账.getName(), CollectionBusinessStatus.已入账分摊.getName()));

        PageResults<StCollectionPersonalBusinessDetails> stCollectionPersonalBusinessDetails = null;
        try {
            stCollectionPersonalBusinessDetails = collection_person_business_details.listWithMarker(search_map, StringUtil.isEmpty(start) ? null : simymd.parse(start), StringUtil.isEmpty(end) ? null : simymd.parse(end), "created_at", Order.DESC, null, null, marker, Integer.parseInt(pageSize), ListAction.pageType(action), new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {

                    criteria.createAlias("person", "person");

//                        criteria.add(
//                                Restrictions.between(
//                                        "created_at",
//                                        !StringUtil.notEmpty(start) ? simymd.parse("1970-01-01 00:01") : simymd.parse(start),
//                                        !StringUtil.notEmpty(end) ? new Date() : simymd.parse(end)));

                    if (StringUtil.notEmpty(GRZH)) {
                        criteria.add(Restrictions.like("grzh", "%" + GRZH + "%"));
                    }
                    if (StringUtil.notEmpty(XingMing)) {
                        criteria.add(Restrictions.like("person.xingMing", "%" + XingMing + "%"));
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
        List<StCollectionPersonalBusinessDetails> collectionPersonalBusinessDetails = stCollectionPersonalBusinessDetails.getResults();
        PageResNew<GetIndiBusnissDetailListResRes> pageres = new PageResNew<>();
        ArrayList<GetIndiBusnissDetailListResRes> list = new ArrayList<GetIndiBusnissDetailListResRes>();
        GetIndiBusnissDetailListResRes getIndiBusnissDetailListResRes = null;
        for (StCollectionPersonalBusinessDetails ectionPersonalBusinessDetails : collectionPersonalBusinessDetails) {
            getIndiBusnissDetailListResRes = new GetIndiBusnissDetailListResRes();
            getIndiBusnissDetailListResRes.setId(ectionPersonalBusinessDetails.getId());
            getIndiBusnissDetailListResRes.setYWLSH(ectionPersonalBusinessDetails.getYwlsh());
            getIndiBusnissDetailListResRes.setCZBS(ectionPersonalBusinessDetails.getCzbz());
            getIndiBusnissDetailListResRes.setGRZH(ectionPersonalBusinessDetails.getGrzh());
            if (ectionPersonalBusinessDetails.getPerson() == null) {
                StCollectionPersonalAccount stCollectionPersonalAccount = iStCollectionPersonalAccountDAO.getByGrzh(ectionPersonalBusinessDetails.getGrzh());
                if (stCollectionPersonalAccount != null) {
                    getIndiBusnissDetailListResRes.setXingMing(stCollectionPersonalAccount.getGrckzhhm());
                }
            } else {
                getIndiBusnissDetailListResRes.setXingMing(ectionPersonalBusinessDetails.getPerson().getXingMing());
            }


            if (ectionPersonalBusinessDetails.getExtension() != null) {
                getIndiBusnissDetailListResRes.setYWMXLX(ectionPersonalBusinessDetails.getExtension().getCzmc());
            }
            getIndiBusnissDetailListResRes.setFSE(ectionPersonalBusinessDetails.getFse() == null ? null : ectionPersonalBusinessDetails.getFse().abs() + "");
            getIndiBusnissDetailListResRes.setDNGJFSE(ectionPersonalBusinessDetails.getDngjfse().abs() + "");
            getIndiBusnissDetailListResRes.setSNJZFSE(ectionPersonalBusinessDetails.getSnjzfse().abs() + "");
            getIndiBusnissDetailListResRes.setFSLXE(ectionPersonalBusinessDetails.getFslxe().abs() + "");
            getIndiBusnissDetailListResRes.setTQYY(ectionPersonalBusinessDetails.getTqyy());
            getIndiBusnissDetailListResRes.setTQFS(ectionPersonalBusinessDetails.getTqfs());
            getIndiBusnissDetailListResRes.setJZRQ(sim.format(ectionPersonalBusinessDetails.getCreated_at()));
            list.add(getIndiBusnissDetailListResRes);
        }
        pageres.setResults(action, list);
        return pageres;
    }
}
