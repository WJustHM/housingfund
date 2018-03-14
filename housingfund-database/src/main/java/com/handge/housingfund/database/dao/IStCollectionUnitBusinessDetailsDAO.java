package com.handge.housingfund.database.dao;

import com.handge.housingfund.database.entities.PageResults;
import com.handge.housingfund.database.entities.StCollectionUnitBusinessDetails;
import com.handge.housingfund.database.enums.ListDeleted;
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.enums.SearchOption;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public interface IStCollectionUnitBusinessDetailsDAO extends IBaseDAO<StCollectionUnitBusinessDetails> {

    public PageResults<StCollectionUnitBusinessDetails> getReviewedList(HashMap<String, Object> filters, Date start,
                                                                        Date end, String orderby, Order order, ListDeleted listDeleted, SearchOption searchOption,
                                                                        int pageNo, int pageSize);
    StCollectionUnitBusinessDetails getByYwlsh(String ywlsh);

    boolean isExistDoingUnitBus(String dwzh, String ywlx);

    boolean isCouldModifyUnitBus(String ywlsh);

    List<StCollectionUnitBusinessDetails> getBusEnd(String dwzh, String ywmxlx);

    //获取全市各地区归集总额
    List<Map> getCityCollection(int year);

    //获取全市各地区提取总额
    List<Map> getCityWithdrawl(int year);

    //获取全市各地区贷款总额
    List<Map> getCityLoan(int year);

    //获取全市各地区还款总额
    List<Map> getCityRepament(int year);

    //获取全市各地区暂收未分摊
    List<Map> getCityZSWFT();

    List<String> getYWWDs();

    String savePerRadix(StCollectionUnitBusinessDetails stCollectionUnitBusinessDetails);

    List<String> detailsList(String dwzh);

    BigInteger getBQSJDWS(String cxny);

    BigInteger getBQSJZGRS(String cxny);

    BigDecimal getBQSJCE(String cxny);

    BigDecimal getBQGRTQE(String cxny);

    BigInteger getBQDWZHKHS(String cxny);

    BigInteger getBQDWZHXHS(String cxny);

    BigInteger getBQGRZHKHS(String cxny);

    BigInteger getBQGRZHXHS(String cxny);

    BigInteger getBQLJTQZGS(String cxny);

    BigInteger getBQMDWZHS(String cxny);

    BigInteger getBQMGRZHS(String cxny);

    BigInteger getBQMFCZHF(String cxny);

    BigInteger getBQMLJGRZHXHS(String cxny);

    String[] getUnitClassificationInformation(String CXNY, String code);



}