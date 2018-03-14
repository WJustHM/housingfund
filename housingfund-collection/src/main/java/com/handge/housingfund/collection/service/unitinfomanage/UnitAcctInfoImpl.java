package com.handge.housingfund.collection.service.unitinfomanage;


import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessStatus;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.collection.enumeration.PersonAccountStatus;
import com.handge.housingfund.common.service.collection.enumeration.UnitAccountStatus;
import com.handge.housingfund.common.service.collection.model.InventoryDetail;
import com.handge.housingfund.common.service.collection.model.individual.ListEmployeeRes;
import com.handge.housingfund.common.service.collection.model.individual.ListIndiAcctsResRes;
import com.handge.housingfund.common.service.collection.model.unit.ListUnitAcctsResRes;
import com.handge.housingfund.common.service.collection.model.unit.UnitEmployeeExcelRes;
import com.handge.housingfund.common.service.collection.service.unitinfomanage.UnitAcctInfo;
import com.handge.housingfund.common.service.loan.enums.LoanAccountType;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.tools.jconsole.Plotter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.handge.housingfund.database.utils.DAOBuilder.instance;


/*
 * Created by lian on 2017/7/19.
 */
@SuppressWarnings({"Convert2Lambda", "Anonymous2MethodRef", "SpringAutowiredFieldsWarningInspection", "Duplicates", "serial"})
@Component
public class UnitAcctInfoImpl implements UnitAcctInfo {

    @Autowired
    private IStCommonUnitDAO commonUnitDAO;

    @Autowired
    private IStCollectionUnitBusinessDetailsDAO collectionUnitBusinessDetailsDAO;
    @Autowired
    private IStCommonPersonDAO commonPersonDAO;
    @Autowired
    private ICAccountNetworkDAO icAccountNetworkDAO;
    @Autowired
    private ICLoanHousingPersonInformationBasicDAO loanHousingPersonInformationBasicDAO;

    private static String format = "yyyy-MM-dd HH:mm";

    private static String formatNYR = "yyyy-MM-dd";

    private static String formatNYRSF = "yyyy-MM-dd HH:mm";
    private static String formatNY = "yyyy-MM";
    @Override
    public PageRes<ListUnitAcctsResRes> getUnitAcctsInfo(TokenContext tokenContext, String SFXH,String DWMC, String DWZH, String DWLB, String DWZHZT,String YWWD, String startTime, String endTime, String page, String pagesize) {

        //region //参数检查

        int page_number = 0;

        int pagesize_number = 0;

        try {

            if (page != null) {
                page_number = Integer.parseInt(page);
            }

            if (pagesize != null) {
                pagesize_number = Integer.parseInt(pagesize);
            }
        } catch (Exception e) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "页码");
        }

        //endregion

        //region //必要字段查询
        PageRes pageRes = new PageRes();

        List<StCommonUnit> list_commonUnit = DAOBuilder.instance(this.commonUnitDAO).searchFilter(new HashMap<String, Object>() {{

            if (StringUtil.notEmpty(DWMC)) { this.put("dwmc", DWMC); }

            if (StringUtil.notEmpty(DWZH)) { this.put("dwzh", DWZH); }

        }}).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {

                //criteria.add(Restrictions.not(Restrictions.isNull("dwmc")));

                criteria.createAlias("cCommonUnitExtension","cCommonUnitExtension");

                if(StringUtil.notEmpty(YWWD)){
                    criteria.add(Restrictions.eq("cCommonUnitExtension.khwd",YWWD));
                }
                if (StringUtil.notEmpty(DWLB)) {
                    criteria.add(Restrictions.eq( "cCommonUnitExtension.dwlb", DWLB));
                }

                criteria.createAlias("collectionUnitAccount","collectionUnitAccount");

                if(StringUtil.notEmpty(SFXH)){

                    criteria.add(Restrictions.ne("collectionUnitAccount.dwzhzt",UnitAccountStatus.销户.getCode()));
                }
                if (StringUtil.notEmpty(DWZHZT) && !DWZHZT.equals(UnitAccountStatus.所有.getCode())) {

                    criteria.add(Restrictions.eq("collectionUnitAccount.dwzhzt", DWZHZT));
                }

                if (DateUtil.safeStr2Date(format, startTime) != null) {
                    criteria.add(Restrictions.ge("created_at", DateUtil.safeStr2Date(format, startTime)));
                }

                if (DateUtil.safeStr2Date(format, endTime) != null) {
                    criteria.add(Restrictions.lt("created_at", DateUtil.safeStr2Date(format, endTime)));
                }

            }
        }).pageOption(pageRes, pagesize_number, page_number).searchOption(SearchOption.FUZZY).getList(new DAOBuilder.ErrorHandler() {


            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });


        List<StCollectionUnitBusinessDetails> list_collectionUnitBusinessDetails = list_commonUnit.size() == 0 ? new ArrayList<>(): DAOBuilder.instance(this.collectionUnitBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{


            this.put("dwzh", CollectionUtils.flatmap(list_commonUnit, new CollectionUtils.Transformer<StCommonUnit, String>() {
                @Override
                public String tansform(StCommonUnit var1) {
                    return var1.getDwzh();
                }
            }));

            this.put("cCollectionUnitBusinessDetailsExtension.step", CollectionBusinessStatus.办结.getName());

            this.put("cCollectionUnitBusinessDetailsExtension.czmc", CollectionBusinessType.开户.getCode());

        }}).getList(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }

        });

        //endregion

        return new PageRes<ListUnitAcctsResRes>() {{

            this.setResults(new ArrayList<ListUnitAcctsResRes>() {{

                for (StCommonUnit commonUnit : list_commonUnit) {

                    this.add(new ListUnitAcctsResRes() {{

                        this.setId(commonUnit.getId());
                        this.setDWJBRXM(commonUnit.getJbrxm());
                        this.setZZJGDM(commonUnit.getZzjgdm());

                        System.err.println(commonUnit.getDwzh() + "======>" + "=====>" + list_collectionUnitBusinessDetails + CollectionUtils.flatmap(list_collectionUnitBusinessDetails, new CollectionUtils.Transformer<StCollectionUnitBusinessDetails, String>() {
                            @Override
                            public String tansform(StCollectionUnitBusinessDetails var1) {
                                return var1.getDwzh() + "";
                            }
                        }));

                        StCollectionUnitBusinessDetails collectionUnitBusinessDetails = CollectionUtils.find(list_collectionUnitBusinessDetails, new CollectionUtils.Predicate<StCollectionUnitBusinessDetails>() {
                            @Override
                            public boolean evaluate(StCollectionUnitBusinessDetails var1) {

                                return var1.getDwzh() != null && var1.getDwzh().equals(commonUnit.getDwzh());
                            }
                        });

                        this.setSLSJ(DateUtil.date2Str(commonUnit.getDwkhrq(), format));

                        if (collectionUnitBusinessDetails != null && collectionUnitBusinessDetails.getExtension() != null) {

                            this.setCZY(collectionUnitBusinessDetails.getExtension().getCzy());
                            this.setXZQY(collectionUnitBusinessDetails.getExtension().getYwwd().getMingCheng());
                        }
                        //String khwdmc = icAccountNetworkDAO.get(commonUnit.getExtension().getKhwd()).getMingCheng();


                        this.setDWZHZT(commonUnit.getCollectionUnitAccount().getDwzhzt());

                        this.setDWMC(commonUnit.getDwmc());

                        this.setDWZH(commonUnit.getDwzh());

                        this.setDWLB(commonUnit.getExtension().getDwlb());

                        this.setDWJCBL(String.valueOf(commonUnit.getCollectionUnitAccount().getDwjcbl()));

                        this.setGRJCBL(String.valueOf(commonUnit.getCollectionUnitAccount().getGrjcbl()));

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
    public PageResNew<ListUnitAcctsResRes> getUnitAcctsInfo(TokenContext tokenContext, String SFXH, String DWMC, String DWZH, String DWLB, String DWZHZT, String YWWD, String startTime, String endTime, String marker, String action, String pagesize) {
        //region //参数检查

        int pagesize_number = 0;

        try {

            if (pagesize != null) { pagesize_number = Integer.parseInt(pagesize); }

        } catch (Exception e) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "页码");
        }

        //endregion

        //region //必要字段查询
        PageRes pageRes = new PageRes();

        List<StCommonUnit> list_commonUnit = DAOBuilder.instance(this.commonUnitDAO).searchFilter(new HashMap<String, Object>() {{

            if (StringUtil.notEmpty(DWMC)) { this.put("dwmc", DWMC); }

            if (StringUtil.notEmpty(DWZH)) { this.put("dwzh", DWZH); }

        }}).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {

                //criteria.add(Restrictions.not(Restrictions.isNull("dwmc")));

                criteria.createAlias("cCommonUnitExtension","cCommonUnitExtension");

                if(StringUtil.notEmpty(YWWD)){
                    criteria.add(Restrictions.eq("cCommonUnitExtension.khwd",YWWD));
                }
                if (StringUtil.notEmpty(DWLB)) {
                    criteria.add(Restrictions.eq( "cCommonUnitExtension.dwlb", DWLB));
                }

                criteria.createAlias("collectionUnitAccount","collectionUnitAccount");

                if(StringUtil.notEmpty(SFXH)){

                    criteria.add(Restrictions.ne("collectionUnitAccount.dwzhzt",UnitAccountStatus.销户.getCode()));
                }
                if (StringUtil.notEmpty(DWZHZT) && !DWZHZT.equals(UnitAccountStatus.所有.getCode())) {

                    criteria.add(Restrictions.eq("collectionUnitAccount.dwzhzt", DWZHZT));
                }

                if (DateUtil.safeStr2Date(format, startTime) != null) {
                    criteria.add(Restrictions.ge("created_at", DateUtil.safeStr2Date(format, startTime)));
                }

                if (DateUtil.safeStr2Date(format, endTime) != null) {
                    criteria.add(Restrictions.lt("created_at", DateUtil.safeStr2Date(format, endTime)));
                }

            }
        }).pageOption(marker,action, pagesize_number).searchOption(SearchOption.FUZZY).getList(new DAOBuilder.ErrorHandler() {


            @Override
            public void error(Exception e) { throw new ErrorException(e); }
        });


        List<StCollectionUnitBusinessDetails> list_collectionUnitBusinessDetails = list_commonUnit.size() == 0 ? new ArrayList<>(): DAOBuilder.instance(this.collectionUnitBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{


            this.put("dwzh", CollectionUtils.flatmap(list_commonUnit, new CollectionUtils.Transformer<StCommonUnit, String>() {
                @Override
                public String tansform(StCommonUnit var1) {
                    return var1.getDwzh();
                }
            }));

            this.put("cCollectionUnitBusinessDetailsExtension.step", CollectionBusinessStatus.办结.getName());

            this.put("cCollectionUnitBusinessDetailsExtension.czmc", CollectionBusinessType.开户.getCode());

        }}).getList(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e); }

        });

        //endregion

        return new PageResNew<ListUnitAcctsResRes>() {{

            this.setResults(action,new ArrayList<ListUnitAcctsResRes>() {{

                for (StCommonUnit commonUnit : list_commonUnit) {

                    this.add(new ListUnitAcctsResRes() {{

                        this.setId(commonUnit.getId());
                        this.setDWJBRXM(commonUnit.getJbrxm());
                        this.setZZJGDM(commonUnit.getZzjgdm());

                        System.err.println(commonUnit.getDwzh() + "======>" + "=====>" + list_collectionUnitBusinessDetails + CollectionUtils.flatmap(list_collectionUnitBusinessDetails, new CollectionUtils.Transformer<StCollectionUnitBusinessDetails, String>() {
                            @Override
                            public String tansform(StCollectionUnitBusinessDetails var1) {
                                return var1.getDwzh() + "";
                            }
                        }));

                        StCollectionUnitBusinessDetails collectionUnitBusinessDetails = CollectionUtils.find(list_collectionUnitBusinessDetails, new CollectionUtils.Predicate<StCollectionUnitBusinessDetails>() {
                            @Override
                            public boolean evaluate(StCollectionUnitBusinessDetails var1) {

                                return var1.getDwzh() != null && var1.getDwzh().equals(commonUnit.getDwzh());
                            }
                        });

                        this.setSLSJ(DateUtil.date2Str(commonUnit.getDwkhrq(), format));

                        if (collectionUnitBusinessDetails != null && collectionUnitBusinessDetails.getExtension() != null) {

                            this.setCZY(collectionUnitBusinessDetails.getExtension().getCzy());
                            this.setXZQY(collectionUnitBusinessDetails.getExtension().getYwwd().getMingCheng());
                        }
                        //String khwdmc = icAccountNetworkDAO.get(commonUnit.getExtension().getKhwd()).getMingCheng();


                        this.setDWZHZT(commonUnit.getCollectionUnitAccount().getDwzhzt());

                        this.setDWMC(commonUnit.getDwmc());

                        this.setDWZH(commonUnit.getDwzh());

                        this.setDWLB(commonUnit.getExtension().getDwlb());

                        this.setDWJCBL(String.valueOf(commonUnit.getCollectionUnitAccount().getDwjcbl()));

                        this.setGRJCBL(String.valueOf(commonUnit.getCollectionUnitAccount().getGrjcbl()));

                    }});
                }
            }});

        }};
    }

    public PageRes<ListEmployeeRes> getEmployeeList(TokenContext tokenContext, String DWZH,String XingMing,String page, String pagesize){

        //region //参数检查
        int page_number = 0;

        int pagesize_number = 0;

        try {

            if (page != null) {
                page_number = Integer.parseInt(page);
            }

            if (pagesize != null) {
                pagesize_number = Integer.parseInt(pagesize);
            }
        } catch (Exception e) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "页码");
        }
        //endregion

        //region //必要字段查询
        PageRes pageRes = new PageRes();

        List<StCommonPerson> list_commonPerson = instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {{


            if (StringUtil.notEmpty(XingMing)) { this.put("xingMing", XingMing); }


        }}).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {

                criteria.createAlias("unit","unit");
                criteria.add(Restrictions.eq("unit.dwzh",DWZH));

            }
        }).pageOption(pageRes, pagesize_number, page_number).searchOption(SearchOption.FUZZY).getList(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e); }
        });

//        List<StCollectionPersonalBusinessDetails> list_collectionPersonalBusinessDetails = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>(){{
//
//
//            this.put("grzh", CollectionUtils.flatmap(list_commonPerson, new CollectionUtils.Transformer<StCommonPerson, String>() {
//                @Override
//                public String tansform(StCommonPerson var1) { return var1.getGrzh(); }
//            }));
//
//            this.put("cCollectionPersonalBusinessDetailsExtension.step", CollectionBusinessStatus.办结.getName());
//
//            this.put("cCollectionPersonalBusinessDetailsExtension.czmc", CollectionBusinessType.开户.getCode());
//
//        }}).getList(new DAOBuilder.ErrorHandler() {
//
//            @Override
//            public void error(Exception e) { throw new ErrorException(e);}
//
//        });
        //endregion

        return new PageRes<ListEmployeeRes>() {{

            this.setResults(new ArrayList<ListEmployeeRes>() {{

                for (StCommonPerson commonPerson : list_commonPerson) {

                    this.add(new ListEmployeeRes() {{

                        this.setXingMing(commonPerson.getXingMing());
                        this.setGRZH(commonPerson.getGrzh());
                        this.setZJHM(commonPerson.getZjhm());
                        if (commonPerson.getCollectionPersonalAccount() != null) {

                            this.setGRZHZT(commonPerson.getCollectionPersonalAccount().getGrzhzt());
                            this.setGRJCJS(String.valueOf(commonPerson.getCollectionPersonalAccount().getGrjcjs().setScale(2, BigDecimal.ROUND_HALF_UP)));
                            this.setGRZHYE(String.valueOf(commonPerson.getCollectionPersonalAccount().getGrzhye().setScale(2, BigDecimal.ROUND_HALF_UP)));

                            if (commonPerson.getCollectionPersonalAccount().getDwyjce() != null && commonPerson.getCollectionPersonalAccount().getGryjce() != null) {

                                this.setGRYJCE(String.valueOf(commonPerson.getCollectionPersonalAccount().getGryjce().setScale(2, BigDecimal.ROUND_HALF_UP)));
                                this.setDWYJCE(String.valueOf(commonPerson.getCollectionPersonalAccount().getDwyjce().setScale(2, BigDecimal.ROUND_HALF_UP)));
                                this.setYJCE(String.valueOf(commonPerson.getCollectionPersonalAccount().getDwyjce().add(commonPerson.getCollectionPersonalAccount().getGryjce()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
                            }
                        }

                        if (commonPerson.getExtension() != null) {

                            this.setJZNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, commonPerson.getExtension().getGrjzny(), formatNY));
                        }

                        List<CLoanHousingPersonInformationBasic> list_loanHousingPersonInformationBasic = instance(loanHousingPersonInformationBasicDAO).searchFilter(new HashMap<String, Object>() {{


                            this.put("jkrzjhm", commonPerson.getZjhm());

                        }}).extension(new IBaseDAO.CriteriaExtension() {
                            @Override
                            public void extend(Criteria criteria) {
                                criteria.add(Restrictions.isNotNull("loanContract"));
                                criteria.add(Restrictions.isNotNull("personalAccount"));
                                criteria.add(Restrictions.not(Restrictions.in("dkzhzt", LoanAccountType.已结清.getCode(),LoanAccountType.已作废.getCode())));
                            }
                        }).getList(new DAOBuilder.ErrorHandler() {
                            @Override
                            public void error(Exception e) { throw new ErrorException(e); }

                        });
                        this.setSFDK(list_loanHousingPersonInformationBasic.size()==0?"0":"1");

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


//	public ResponseEntity getUnitDetails(TokenContext tokenContext,String DWZH){
//
//		ArrayList<Exception> exceptions = new ArrayList<>();
//
//		StCommonUnit commonUnit = DAOBuilder.instance(this.commonUnitDAO).searchFilter(new HashMap<String, Object>() {{
//
//			this.put("dwzh",DWZH);
//
//		}}).getObject(new DAOBuilder.ErrorHandler() {
//
//			@Override
//
//			public void error(Exception e) { throw new ErrorException(e); }
//		});
//
//		return ResponseUtils.buildCommonResponse(exceptions, exceptions.size()!=0 ? null : new GetUnitAcctInfoDetail() {{
//
//			this.setDWKHBLZL(new GetUnitAcctInfoDetailDWKHBLZL(){{
//				//this.setBLZL(commonUnit.getCollectionUnitAccount().get);
//			}});
//			this.setDWDJXX(new GetUnitAcctInfoDetailDWDJXX(){{
//				this.setDWJCBL(commonUnit.getCollectionUnitAccount().getDwjcbl()+"");
//				this.setFXZHKHYH(commonUnit.getCollectionUnitAccount().getDwxhyy());
//				//this.setDWFXR(commonUnit.getCollectionUnitAccount());
//				//this.setBeiZhu(commonUnit.getCollectionUnitAccount().);
//
//			}});
////				this.setDWJBRXM(commonUnit.getZzjgdm());
////
////				this.setSLSJ(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month,commonUnit.getUpdated_at() != null ? commonUnit.getUpdated_at() : commonUnit.getCreated_at(), format));
////
////				this.setXZQY(commonUnit.getExtension().getDwxzqy());
////
////				this.setDWZHZT(commonUnit.getCollectionUnitAccount().getDwzhzt());
////
////				this.setDWMC(commonUnit.getDwmc());
////
////				this.setDWZH(commonUnit.getDwzh());
////
////				this.setCZY(null/*todo 操作员*/);
////
////				this.setDWLB(commonUnit.getExtension().getDwlb());
//		}});
//
//	}
    public  UnitEmployeeExcelRes getEmployeeAllData(String DWZH){
        //region //必要字段查询
        ArrayList<ListEmployeeRes> listEmployeeRes = commonPersonDAO.getEmployeeInfo(DWZH);
        UnitEmployeeExcelRes unitEmployeeExcelRes = new UnitEmployeeExcelRes();
        if(listEmployeeRes.size()<=0){
            throw new ErrorException(ReturnEnumeration.Data_MISS, "该单位中没有个人信息");
        }
        StCommonUnit commonUnit = DAOBuilder.instance(commonUnitDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("dwzh",DWZH);
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        unitEmployeeExcelRes.setDWMC(commonUnit.getDwmc());
        unitEmployeeExcelRes.setDWZH(DWZH);
        Collections.sort(listEmployeeRes,new ListEmployeeRes.GrzhztCompator());
        unitEmployeeExcelRes.setListEmployeeRes(listEmployeeRes);
        return unitEmployeeExcelRes;
    }
}
