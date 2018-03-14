package com.handge.housingfund.collection.service.unitdeposit;

import com.handge.housingfund.collection.utils.BusUtils;
import com.handge.housingfund.collection.utils.ComUtils;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessStatus;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.collection.enumeration.UnitAccountStatus;
import com.handge.housingfund.common.service.collection.model.deposit.GetUnitDepositDetailRes;
import com.handge.housingfund.common.service.collection.model.deposit.ListUnitDepositDeatilResRes;
import com.handge.housingfund.common.service.collection.model.deposit.ListUnitDepositResRes;
import com.handge.housingfund.common.service.collection.model.deposit.SumBalances;
import com.handge.housingfund.common.service.collection.service.unitdeposit.UnitDepositInfo;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.dao.IBaseDAO;
import com.handge.housingfund.database.dao.ICAccountNetworkDAO;
import com.handge.housingfund.database.dao.IStCollectionUnitBusinessDetailsDAO;
import com.handge.housingfund.database.dao.IStCommonUnitDAO;
import com.handge.housingfund.database.entities.CAccountNetwork;
import com.handge.housingfund.database.entities.StCollectionUnitBusinessDetails;
import com.handge.housingfund.database.entities.StCommonUnit;
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@SuppressWarnings({"Convert2Lambda", "Anonymous2MethodRef", "SpringAutowiredFieldsWarningInspection", "SpringJavaAutowiredMembersInspection", "ConstantConditions", "ResultOfMethodCallIgnored", "Duplicates", "unchecked"})
public class UnitDepositInfoImpl implements UnitDepositInfo {

    @Autowired
    private IStCommonUnitDAO commonUnitDAO;

    @Autowired
    private IStCollectionUnitBusinessDetailsDAO collectionUnitBusinessDetailsDAO;

    @Autowired
    private ICAccountNetworkDAO cAccountNetworkDAO;

    private static String format = "yyyy-MM-dd";

    private static String formatN = "yyyy";

    private static String formatNY = "yyyy-MM";

    private static String formatNYRSF = "yyyy-MM-dd HH:mm";

    @Override
    public GetUnitDepositDetailRes getUnitDepositDetail(TokenContext tokenContext, String DWZH) {


        //region  //检查参数
        if (DWZH == null) {

            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "单位账号");
        }

        //endregion

        //region //必要参数查询&完整性验证
        StCommonUnit commonUnit = DAOBuilder.instance(this.commonUnitDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("dwzh", DWZH);

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (commonUnit == null) {

            throw new ErrorException(ReturnEnumeration.Data_MISS, "单位信息");
        }

        //endregion

        return new GetUnitDepositDetailRes() {{

            this.setDWZHYE("" + commonUnit.getCollectionUnitAccount().getDwzhye());  //单位账户余额

            this.setZSYE("" + commonUnit.getCollectionUnitAccount().getExtension().getZsye());  //暂收余额

            this.setGRJCBL(changeToView(commonUnit.getCollectionUnitAccount().getGrjcbl().toString()));  //个人缴存比例

            this.setZZJGDM(commonUnit.getZzjgdm());  //组织机构代码

            this.setJZNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, commonUnit.getCollectionUnitAccount().getJzny(), formatNY));  //缴至年月

            /*            this.setLSYLWFT(commonUnit.getCollectionUnitAccount().getExtension().getLsylwft());  //历史遗留未分摊*/

            this.setDWFCRS(commonUnit.getCollectionUnitAccount().getDwfcrs() + "");  //单位封存人数

            this.setDWJCBL(changeToView(commonUnit.getCollectionUnitAccount().getDwjcbl().toString()));  //单位缴存比例

            this.setDWMC(commonUnit.getDwmc());  //单位名称

            this.setDWYHJNY(BusUtils.getDWYHJNY(commonUnit.getDwzh()));  //单位应汇缴年月

            this.setDWZH(commonUnit.getDwzh());  //单位账号

            this.setDWJCRS(commonUnit.getCollectionUnitAccount().getDwjcrs() + "");  //单位缴存人数

        }};
    }

    @Override
    public PageRes<ListUnitDepositResRes> getUnitDepositListRes(TokenContext tokenContext, String DWMC, String DWZH, final String pageSize, final String page, final boolean GLWD, final boolean GLXH, String khwd, String SFYWFTYE,String JZNY) {


        //region //参数检查

        int page_number = 0;

        int pagesize_number = 0;

        try {

            if (page != null) {
                page_number = Integer.parseInt(page);
            }

            if (pageSize != null) {
                pagesize_number = Integer.parseInt(pageSize);
            }
        } catch (Exception e) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "页码");
        }

        //endregion

        //region //必要字段查询
        PageRes pageRes = new PageRes();

        DAOBuilder<StCommonUnit, IStCommonUnitDAO> builder = DAOBuilder.instance(this.commonUnitDAO).searchFilter(new HashMap<String, Object>() {{

            if (StringUtil.notEmpty(DWMC)) {
                this.put("dwmc", DWMC);
            }

            if (StringUtil.notEmpty(DWZH)) {
                this.put("dwzh", DWZH);
            }

        }}).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                criteria.createAlias("collectionUnitAccount", "collectionUnitAccount");
                criteria.createAlias("cCommonUnitExtension", "cCommonUnitExtension");
                if (GLXH) {
                    criteria.add(Restrictions.ne("collectionUnitAccount.dwzhzt", UnitAccountStatus.销户.getCode()));
                }
                if (GLWD) {
                    criteria.add(Restrictions.eq("cCommonUnitExtension.khwd", tokenContext.getUserInfo().getYWWD()));
                }
                if (StringUtil.notEmpty(khwd)) {
                    criteria.add(Restrictions.eq("cCommonUnitExtension.khwd", khwd));
                }
                if (StringUtil.notEmpty(SFYWFTYE) && !"00".equals(SFYWFTYE)) {
                    criteria.createAlias("collectionUnitAccount.cCollectionUnitAccountExtension", "accountExtension");
                    if ("01".equals(SFYWFTYE)) {
                        criteria.add(Restrictions.gt("accountExtension.zsye", BigDecimal.ZERO));
                    } else if ("02".equals(SFYWFTYE)) {
                        criteria.add(Restrictions.eq("accountExtension.zsye", BigDecimal.ZERO));
                    }
                }
                if (StringUtil.notEmpty(JZNY)) {
                    String jzny = ComUtils.parseToYYYYMM(JZNY);
                    criteria.add(Restrictions.eq("collectionUnitAccount.jzny", jzny));
                }

            }
        }).pageOption(pageRes, pagesize_number, page_number).searchOption(SearchOption.FUZZY);

        if("01".equals(SFYWFTYE)){
            builder.orderOption("accountExtension.zsye",Order.DESC);
        }

        List<StCommonUnit> list_commonUnit = builder.getList(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        List<CAccountNetwork> list_network = list_commonUnit.size() == 0 ? new ArrayList<>() : DAOBuilder.instance(cAccountNetworkDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("id", CollectionUtils.flatmap(list_commonUnit, new CollectionUtils.Transformer<StCommonUnit, String>() {
                @Override
                public String tansform(StCommonUnit var1) {
                    return var1.getExtension().getKhwd();
                }

            }));

        }}).getList(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        //endregion

        Object[] balances = commonUnitDAO.getUnitSumBalances(khwd, DWZH, DWMC, JZNY, SFYWFTYE);

        return new SumBalances<ListUnitDepositResRes>() {{


            this.setResults(new ArrayList<ListUnitDepositResRes>() {{

                for (StCommonUnit commonUnit : list_commonUnit) {

                    if (commonUnit.getCollectionUnitAccount() == null) {
                        continue;
                    }

                    this.add(new ListUnitDepositResRes() {{
                        BigDecimal grjcbl = commonUnit.getCollectionUnitAccount().getGrjcbl();
                        this.setGRJCBL(changeToView(grjcbl.toString()));  //个人缴存比例
                        BigDecimal dwjcbl = commonUnit.getCollectionUnitAccount().getDwjcbl();
                        this.setDWJCBL(changeToView(dwjcbl.toString()));  //单位缴存比例

                        BigDecimal dwzhye = commonUnit.getCollectionUnitAccount().getDwzhye();
                        if (!ComUtils.isEmpty(dwzhye)) {
                            this.setDWZHYE(ComUtils.moneyFormat(dwzhye));  //单位账户余额
                        } else {
                            this.setDWZHYE("0.00");  //为空，默认0
                        }

                        this.setZZJGDM(commonUnit.getZzjgdm());  //组织机构代码

                        this.setJZNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, commonUnit.getCollectionUnitAccount().getJzny(), formatNY));  //缴至年月

                        this.setDWMC(commonUnit.getDwmc());  //单位名称

                        this.setDWZHZT(commonUnit.getCollectionUnitAccount().getDwzhzt());

                        CAccountNetwork network = CollectionUtils.find(list_network, new CollectionUtils.Predicate<CAccountNetwork>() {

                            @Override
                            public boolean evaluate(CAccountNetwork var1) {
                                return var1.getId().equals(commonUnit.getExtension().getKhwd());
                            }

                        });
                        this.setKHWD(network == null ? null : network.getMingCheng());

                        this.setDWZH(commonUnit.getDwzh());  //单位账号

                        BigDecimal zsye = commonUnit.getCollectionUnitAccount().getExtension().getZsye();
                        if (!ComUtils.isEmpty(zsye)) {
                            this.setWFTYE(ComUtils.moneyFormat(zsye));  //单位账户余额
                        } else {
                            this.setWFTYE("0.00");  //为空，默认0
                        }
                    }});
                }
            }});

            this.setCurrentPage(pageRes.getCurrentPage());

            this.setNextPageNo(pageRes.getNextPageNo());

            this.setPageCount(pageRes.getPageCount());

            this.setTotalCount(pageRes.getTotalCount());

            this.setPageSize(pageRes.getPageSize());

            this.setDWZHYEHJ(balances[0] != null ? balances[0].toString() : "0.00");

            this.setWFTHJ(balances[1] != null ? balances[1].toString() : "0.00");
        }};

    }

    public PageRes<ListUnitDepositDeatilResRes> getUnitDepositInfoList(TokenContext tokenContext, String DWZH, String date, String pageSize, String page) {

        //region //参数检查
        if (!StringUtil.notEmpty(DWZH)) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "单位账号");
        }

        if (!DateUtil.isFollowFormat(date, formatN, true)) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "查询年限");
        }

        int page_number = 0;

        int pagesize_number = 0;

        try {

            if (page != null) {
                page_number = Integer.parseInt(page);
            }

            if (pageSize != null) {
                pagesize_number = Integer.parseInt(pageSize);
            }
        } catch (Exception e) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "页码");
        }

        //endregion
        PageRes pageRes = new PageRes();
        List<StCollectionUnitBusinessDetails> list_business = DAOBuilder.instance(collectionUnitBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("dwzh", DWZH);

            this.put("ywmxlx", Arrays.asList(CollectionBusinessType.汇缴.getCode(), CollectionBusinessType.补缴.getCode()));

            this.put("cCollectionUnitBusinessDetailsExtension.step", CollectionBusinessStatus.已入账分摊.getName());

        }}).extension(new IBaseDAO.CriteriaExtension() {

            @Override
            public void extend(Criteria criteria) {

                if (DateUtil.safeStr2Date(formatN, date) != null) {

                    criteria.add(Restrictions.between("hbjny", DateUtil.safeStr2DBDate(formatN, date, DateUtil.dbformatYear_Month), DateUtil.date2Str(DateUtils.addDays(DateUtils.addYears(DateUtil.safeStr2Date(formatN, date), 1), -1), DateUtil.dbformatYear_Month)));
                }
            }

        }).pageOption(pageRes, pagesize_number, page_number).orderOption("cCollectionUnitBusinessDetailsExtension.bjsj", Order.DESC).getList(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }

        });
        Collections.sort(list_business);

        return new PageRes<ListUnitDepositDeatilResRes>() {{

            this.setResults(new ArrayList<ListUnitDepositDeatilResRes>(CollectionUtils.flatmap(list_business, new CollectionUtils.Transformer<StCollectionUnitBusinessDetails, ListUnitDepositDeatilResRes>() {

                @Override
                public ListUnitDepositDeatilResRes tansform(StCollectionUnitBusinessDetails collectionUnitBusinessDetails) {

                    return new ListUnitDepositDeatilResRes() {{

                        this.setYWLSH(collectionUnitBusinessDetails.getYwlsh()/*业务流水号*/);
                        this.setFSE("" + collectionUnitBusinessDetails.getFse()/*发生额*/);
                        this.setFSRS(collectionUnitBusinessDetails.getFsrs().intValue()/*发生人数*/);
                        this.setYWLX(collectionUnitBusinessDetails.getYwmxlx()/*业务类型*/);
                        this.setHBJNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, collectionUnitBusinessDetails.getHbjny(), formatNY)/*汇补缴年月*/);
                        String jkfs = null;
                        if ("01".equals(collectionUnitBusinessDetails.getYwmxlx())) {
                            jkfs = collectionUnitBusinessDetails.getUnitRemittanceVice().getHjfs();
                        } else {
                            jkfs = collectionUnitBusinessDetails.getUnitPaybackVice().getBjfs();
                        }
                        this.setHJFS(jkfs);//汇缴方式
                        if (collectionUnitBusinessDetails.getExtension() != null) {
                            this.setYWWD(collectionUnitBusinessDetails.getExtension().getYwwd().getMingCheng()/*业务网点*/);
                            this.setSLSJ(DateUtil.date2Str(collectionUnitBusinessDetails.getExtension().getSlsj(), formatNYRSF)/*受理时间*/);
                            this.setJBR(collectionUnitBusinessDetails.getExtension().getJbrxm()/*经办人*/);
                            this.setCZY(collectionUnitBusinessDetails.getExtension().getCzy()/*操作员*/);
                        }
                    }};
                }

            })));

            this.setCurrentPage(pageRes.getCurrentPage());

            this.setNextPageNo(pageRes.getNextPageNo());

            this.setPageCount(pageRes.getPageCount());

            this.setTotalCount(pageRes.getTotalCount());

            this.setPageSize(pageRes.getPageSize());
        }};
    }


    /**
     * 缴存比例乘100,显示
     */
    private String changeToView(String jcbl) {
        return new BigDecimal(jcbl).multiply(new BigDecimal("100")).toString();
    }
}
