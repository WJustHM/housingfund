package com.handge.housingfund.collection.service.common;

import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessStatus;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.collection.enumeration.CommonFieldType;
import com.handge.housingfund.common.service.collection.model.BalanceInterestFinalRes;
import com.handge.housingfund.common.service.collection.service.common.ICalculateInterest;
import com.handge.housingfund.common.service.loan.model.CommonResponses;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
@SuppressWarnings({"Convert2Lambda", "SpringAutowiredFieldsWarningInspection"})
public class CalculateInterestImpl implements ICalculateInterest{


    @Autowired
    private IStCommonPersonDAO commonPersonDAO;


    @Autowired
    private IStCollectionPersonalBusinessDetailsDAO collectionPersonalBusinessDetailsDAO;

    @Autowired
    private IStCollectionUnitBusinessDetailsDAO collectionUnitBusinessDetailsDAO;

    @Autowired
    private ICAccountNetworkDAO cAccountNetworkDAO;

    @Override
    public CommonResponses calculateInterestByGrzh(String grzh, Date start, Date end){

        if(!StringUtil.notEmpty(grzh)){

            throw new ErrorException(ReturnEnumeration.Parameter_MISS,"个人账号");
        }

        if(start == null || end == null || start.getTime()>end.getTime()){

            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"时间");
        }
        StCommonPerson commonPerson = DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>(){{

            this.put("grzh",grzh);

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e);}
        });

        if(commonPerson == null||commonPerson.getCollectionPersonalAccount() == null){

            throw new ErrorException(ReturnEnumeration.Data_MISS,"个人信息");
        }


        BigDecimal  currentRest = commonPerson.getCollectionPersonalAccount().getGrzhye() == null ? BigDecimal.ZERO:commonPerson.getCollectionPersonalAccount().getGrzhye();

        BigDecimal  totalIntrest = BigDecimal.ZERO;

        List<StCollectionPersonalBusinessDetails> list_business = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>(){{

            this.put("grzh",grzh);

            this.put("cCollectionPersonalBusinessDetailsExtension.step", Arrays.asList(CollectionBusinessStatus.办结.getName(),CollectionBusinessStatus.已入账.getName(),CollectionBusinessStatus.已入账分摊.getName()));

            this.put("cCollectionPersonalBusinessDetailsExtension.czmc", Arrays.asList(CollectionBusinessType.汇缴.getCode(),CollectionBusinessType.补缴.getCode(),CollectionBusinessType.错缴更正.getCode(),CollectionBusinessType.部分提取.getCode(),CollectionBusinessType.销户提取.getCode(),CollectionBusinessType.结息.getCode()));

        }}).orderOption("cCollectionPersonalBusinessDetailsExtension.bjsj", Order.ASC).extension(new IBaseDAO.CriteriaExtension() {

            @Override
            public void extend(Criteria criteria) {

                criteria.add(Restrictions.gt("cCollectionPersonalBusinessDetailsExtension.bjsj",start));
                criteria.add(Restrictions.lt("cCollectionPersonalBusinessDetailsExtension.bjsj",end));
            }
        }).getList(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e); }

        });

        for(int i = list_business.size()-1;i>=0;i--){

            StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = list_business.get(i);

            Date calculateStart = (i+1) >= list_business.size() ? new Date() : list_business.get(i+1).getExtension().getBjsj();

            Date calculateEnd =  collectionPersonalBusinessDetails.getExtension().getBjsj();

            BigDecimal delta = new BigDecimal((int)((calculateStart.getTime() - calculateEnd.getTime())/(1000.0*60*60*24)));

            totalIntrest = totalIntrest.add(currentRest.multiply(delta.add(new BigDecimal(0))).multiply(new BigDecimal("0.00004167")));

            System.err.println(delta +","+","+collectionPersonalBusinessDetails.getFse()+","+totalIntrest);

            currentRest = currentRest.subtract(collectionPersonalBusinessDetails.getFse());

        }

        Date calculateStart = list_business.size() == 0 ? new Date() : list_business.get(0).getExtension().getBjsj();

        BigDecimal delta = new BigDecimal((int)((calculateStart.getTime() - start.getTime())/(1000.0*60*60*24)));

        totalIntrest = totalIntrest.add(currentRest.multiply(delta.add(new BigDecimal(0))).multiply(new BigDecimal("0.00004167")));

        BigDecimal finalTotalIntrest = totalIntrest;

        System.err.println(start+"到"+end+"的利息为"+totalIntrest);

        return new CommonResponses(){{

            this.setState("success");

            this.setId(finalTotalIntrest.setScale(2, RoundingMode.HALF_UP).doubleValue() + "");
        }};
    }

    @Deprecated
    @Override
    public CommonResponses balanceInterest(String grzh, BigDecimal interest){

        if(!StringUtil.notEmpty(grzh)){

            throw new ErrorException(ReturnEnumeration.Parameter_MISS,"个人账号");
        }

        StCommonPerson commonPerson = DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>(){{

            this.put("grzh",grzh);

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e);}
        });

        if(commonPerson == null||commonPerson.getCollectionPersonalAccount() == null){

            throw new ErrorException(ReturnEnumeration.Data_MISS,"个人信息");
        }

        StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = new StCollectionPersonalBusinessDetails();

        CCollectionPersonalBusinessDetailsExtension collectionUnitBusinessDetailsExtension = new CCollectionPersonalBusinessDetailsExtension();

        collectionPersonalBusinessDetails.setExtension(collectionUnitBusinessDetailsExtension);

        collectionPersonalBusinessDetails.setGrzh(grzh);

        collectionPersonalBusinessDetails.setGjhtqywlx(CollectionBusinessType.结息.getCode());

        collectionPersonalBusinessDetails.setFse(interest);

        collectionPersonalBusinessDetails.setFslxe(BigDecimal.ZERO);

        collectionPersonalBusinessDetails.setPerson(commonPerson);

        collectionPersonalBusinessDetails.setUnit(commonPerson.getUnit());

        collectionUnitBusinessDetailsExtension.setBjsj(new Date());
        collectionUnitBusinessDetailsExtension.setStep(CollectionBusinessStatus.办结.getName());

        commonPerson.getCollectionPersonalAccount().setGrzhye((commonPerson.getCollectionPersonalAccount().getGrzhye()==null?BigDecimal.ZERO:commonPerson.getCollectionPersonalAccount().getGrzhye()).add(collectionPersonalBusinessDetails.getFse()));

        StCollectionPersonalBusinessDetails savedBusiness = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).entity(collectionPersonalBusinessDetails).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) { throw new ErrorException(e); }
        });


        return new CommonResponses(){{

            this.setId(savedBusiness.getYwlsh());
            this.setState("succeed");
        }};
    }

    @Override
    public BalanceInterestFinalRes balanceInterestFinal(String grzh){

        @SuppressWarnings("deprecation") int year = (new Date()).getYear();

        Date start = DateUtil.safeStr2Date((year-1)+"-6-30","yyyy-MM-dd");

        Date end = DateUtil.safeStr2Date(year+"-6-30","yyyy-MM-dd");

        if(start==null||end == null || end.getTime() > new Date().getTime()){

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"未到今年结息时间");
        }

        if(!StringUtil.notEmpty(grzh)){

            throw new ErrorException(ReturnEnumeration.Parameter_MISS,"个人账号");
        }

        StCommonPerson commonPerson = DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>(){{

            this.put("grzh",grzh);

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e);}
        });

        if(commonPerson == null||commonPerson.getCollectionPersonalAccount() == null){

            throw new ErrorException(ReturnEnumeration.Data_MISS,"个人信息");
        }

        start = start.getTime() > commonPerson.getCreated_at().getTime() ? start : commonPerson.getCreated_at();

        CAccountNetwork network = DAOBuilder.instance(cAccountNetworkDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("id","0");

        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) { throw new ErrorException(e); }
        });

        StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = new StCollectionPersonalBusinessDetails();

        CCollectionPersonalBusinessDetailsExtension collectionPersonalBusinessDetailsExtension = new CCollectionPersonalBusinessDetailsExtension();
        collectionPersonalBusinessDetails.setExtension(collectionPersonalBusinessDetailsExtension);

        collectionPersonalBusinessDetails.setGrzh(grzh);
        collectionPersonalBusinessDetails.setGjhtqywlx(CollectionBusinessType.年终结息.getCode());
        collectionPersonalBusinessDetails.setFse(new BigDecimal(this.calculateInterestByGrzh(grzh,start,end).getId()));
        collectionPersonalBusinessDetails.setFslxe(BigDecimal.ZERO);
        collectionPersonalBusinessDetails.setPerson(commonPerson);
        collectionPersonalBusinessDetails.setCzbz(CommonFieldType.非冲账.getCode());
        collectionPersonalBusinessDetails.setJzrq(new Date());
        collectionPersonalBusinessDetails.setUnit(commonPerson.getUnit());


        collectionPersonalBusinessDetailsExtension.setBjsj(new Date());
        collectionPersonalBusinessDetailsExtension.setStep(CollectionBusinessStatus.办结.getName());
        collectionPersonalBusinessDetailsExtension.setDqye((commonPerson.getCollectionPersonalAccount().getGrzhye()==null?BigDecimal.ZERO:commonPerson.getCollectionPersonalAccount().getGrzhye()).add(collectionPersonalBusinessDetails.getFse()));
        collectionPersonalBusinessDetailsExtension.setYwwd(network);
        collectionPersonalBusinessDetailsExtension.setCzy("系统");
        collectionPersonalBusinessDetailsExtension.setCzmc(CollectionBusinessType.年终结息.getCode());

        commonPerson.getCollectionPersonalAccount().setGrzhye((commonPerson.getCollectionPersonalAccount().getGrzhye()==null?BigDecimal.ZERO:commonPerson.getCollectionPersonalAccount().getGrzhye()).add(collectionPersonalBusinessDetails.getFse()));

        StCollectionPersonalBusinessDetails savedBusiness = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).entity(collectionPersonalBusinessDetails).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) { throw new ErrorException(e); }
        });


        StCollectionUnitBusinessDetails collectionUnitBusinessDetails = new StCollectionUnitBusinessDetails();

        CCollectionUnitBusinessDetailsExtension collectionUnitBusinessDetailsExtension = new CCollectionUnitBusinessDetailsExtension();
        collectionUnitBusinessDetails.setExtension(collectionUnitBusinessDetailsExtension);

        collectionUnitBusinessDetails.setDwzh(commonPerson.getUnit().getDwzh());
        collectionUnitBusinessDetails.setUnit(commonPerson.getUnit());
        collectionUnitBusinessDetails.setYwmxlx(CollectionBusinessType.年终结息.getCode());
        collectionUnitBusinessDetails.setFse(collectionPersonalBusinessDetails.getFse());
        collectionUnitBusinessDetails.setFslxe(BigDecimal.ZERO);
        collectionUnitBusinessDetails.setCzbz(CommonFieldType.非冲账.getCode());
        collectionUnitBusinessDetails.setJzrq(new Date());
        collectionUnitBusinessDetails.setUnit(commonPerson.getUnit());


        collectionUnitBusinessDetailsExtension.setBjsj(new Date());
        collectionUnitBusinessDetailsExtension.setStep(CollectionBusinessStatus.办结.getName());
        collectionUnitBusinessDetailsExtension.setBeizhu("年终结息-"+commonPerson.getGrzh());
        collectionUnitBusinessDetailsExtension.setYwwd(network);
        collectionUnitBusinessDetailsExtension.setCzy("系统");
        collectionUnitBusinessDetailsExtension.setCzmc(CollectionBusinessType.年终结息.getCode());

        collectionUnitBusinessDetails.getUnit().getCollectionUnitAccount().setDwzhye(collectionPersonalBusinessDetails.getUnit().getCollectionUnitAccount().getDwzhye().add(collectionPersonalBusinessDetails.getFse()));

        DAOBuilder.instance(this.collectionUnitBusinessDetailsDAO).entity(collectionUnitBusinessDetails).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) { throw new ErrorException(e); }
        });

        return new BalanceInterestFinalRes(){{

            this.setYWLSH(savedBusiness.getYwlsh());

            this.setBalance(savedBusiness.getFse()+"");
        }};
    }
}
