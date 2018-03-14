package com.handge.housingfund.collection.service.unitinfomanage;

import com.handge.housingfund.collection.utils.ComUtils;
import com.handge.housingfund.collection.utils.ResponseUtils;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessStatus;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.collection.model.unit.*;
import com.handge.housingfund.common.service.collection.service.unitinfomanage.UnitAcctCommon;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.database.dao.IStCollectionUnitBusinessDetailsDAO;
import com.handge.housingfund.database.dao.IStCommonUnitDAO;
import com.handge.housingfund.database.entities.StCollectionUnitBusinessDetails;
import com.handge.housingfund.database.entities.StCommonUnit;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 向超 on 2017/8/1.
 */
@SuppressWarnings({"Duplicates", "SpringAutowiredFieldsWarningInspection", "Convert2Lambda", "Anonymous2MethodRef"})
@Service
public class UnitAcctCommonImpl implements UnitAcctCommon{

    @Autowired
    private IStCollectionUnitBusinessDetailsDAO unitBusDAO;

    @Autowired
    private IStCommonUnitDAO commonUnitDAO;

    private  static String formatNY = "yyyy-MM";

    private static String format = "yyyy-MM-dd";

    @Override
    public AutoUnitAcctActionRes getUnitAcctActionAuto(String DWZH) {
        ArrayList<Exception> exceptions = new ArrayList<Exception>();
        StCommonUnit commonUnit = DAOBuilder.instance(commonUnitDAO).searchFilter(new HashMap<String, Object>() {{//获取单位明细信息
            this.put("dwzh", DWZH);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                exceptions.add(e);
            }
        });
        if(commonUnit==null){
         throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"单位账号不存在");
        }
        String jzny =null;
        if(commonUnit.getCollectionUnitAccount().getJzny()!=null){
            try{
            jzny =new SimpleDateFormat("yyyyMM").format(commonUnit.getCollectionUnitAccount().getJzny());
            }catch (Exception e){
                ResponseUtils.buildParametersFormatErrorResponse();
            }
        }
        String finalJzny = jzny;
         return new AutoUnitAcctActionRes(){{
            this.setDWGJXX(new AutoUnitAcctActionResDWGJXX(){{
                  this.setJBRZJLX(commonUnit.getJbrzjlx());//经办人证件类型

                  this.setDWFRDBXM(commonUnit.getDwfrdbxm());//单位法人代表姓名

                  this.setDWFRDBZJHM(commonUnit.getDwfrdbzjhm());//单位法人代表证件号码

                  this.setJBRXM(commonUnit.getJbrxm());//经办人姓名

                  this.setDJSYYZ(commonUnit.getExtension().getDjsyyz());//登记使用印章

                  this.setJBRGDDHHM(commonUnit.getJbrgddhhm());//经办人固定电话号码

                  this.setDWLB(commonUnit.getExtension().getDwlb());//单位类别

                  this.setJBRSJHM(commonUnit.getJbrsjhm());//经办人手机号码

                  this.setZZJGDM(commonUnit.getZzjgdm());//组织机构代码

                  this.setJBRZJHM(commonUnit.getJbrzjhm());//经办人证件号码

                  this.setDWMC(commonUnit.getDwmc());//单位名称

                  this.setDWZH(commonUnit.getDwzh());//单位账号

                  this.setJZNY(DateUtil.str2str(commonUnit.getCollectionUnitAccount().getJzny(),6));

                  this.setBLZL(commonUnit.getExtension().getDwzl());
            }});
        }};
    }

    @Override
    public GetUnitAcctInfoDetail getUnitInfoAuto(TokenContext tokenContext,String DWZH) {


        StCommonUnit commonUnit = DAOBuilder.instance(this.commonUnitDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("dwzh",DWZH);

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override

            public void error(Exception e) { throw new ErrorException(e); }
        });

        if((commonUnit==null||commonUnit.getCollectionUnitAccount()==null||commonUnit.getCollectionUnitAccount().getExtension() == null)) {

            throw new ErrorException(ReturnEnumeration.Data_MISS,"单位账号对应的单位不存在！");
        }

        StCollectionUnitBusinessDetails collectionUnitBusinessDetails = DAOBuilder.instance(this.unitBusDAO).searchFilter(new HashMap<String, Object>(){{


            this.put("dwzh",commonUnit.getDwzh());

            this.put("cCollectionUnitBusinessDetailsExtension.step", CollectionBusinessStatus.办结.getName());

            this.put("cCollectionUnitBusinessDetailsExtension.czmc", CollectionBusinessType.开户.getCode());

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e);}

        });
        return new GetUnitAcctInfoDetail() {{


            this.setDWDJXX(new GetUnitAcctInfoDetailDWDJXX(){{
                this.setGRJCBL((commonUnit.getCollectionUnitAccount().getGrjcbl()==null?"0":commonUnit.getCollectionUnitAccount().getGrjcbl().multiply(new BigDecimal("100")))+"");
                this.setFXZHKHYH(commonUnit.getCollectionUnitAccount().getExtension().getFxzhkhyh());
                this.setDWFXR(commonUnit.getDwfxr());
                this.setBeiZhu(commonUnit.getExtension().getBeiZhu());
                this.setDWJCBL((commonUnit.getCollectionUnitAccount().getDwjcbl()==null?"0":commonUnit.getCollectionUnitAccount().getDwjcbl().multiply(new BigDecimal("100")))+"");
                this.setDWSCHJNY(DateUtil.str2str(commonUnit.getExtension().getDwschjny(),6));
                this.setFXZH(commonUnit.getCollectionUnitAccount().getExtension().getFxzh());
                this.setDWSLRQ(DateUtil.date2Str(commonUnit.getDwslrq(),format));
                this.setJZNY(DateUtil.str2str(commonUnit.getCollectionUnitAccount().getJzny(),6));
                this.setFXZHHM(commonUnit.getCollectionUnitAccount().getExtension().getFxzhhm());
                this.setDWKHRQ(ComUtils.parseToString(commonUnit.getDwkhrq(),"yyyy-MM-dd"));
            }});

            this.setDWLXFS(new GetUnitAcctInfoDetailDWLXFS(){{
                this.setCZHM(commonUnit.getExtension().getCzhm());
                this.setDWLXDH(commonUnit.getExtension().getDwlxdh());
                this.setDWDZXX(commonUnit.getDwdzxx());
                this.setDWYB(commonUnit.getDwyb());
            }});

            this.setDWGJXX(new GetUnitAcctInfoDetailDWGJXX(){{
                this.setPZJGJB(commonUnit.getExtension().getPzjgjb());
                this.setDWFRDBZJHM(commonUnit.getDwfrdbzjhm());
                this.setDWJJLX(commonUnit.getDwjjlx());
                this.setDWFRDBXM(commonUnit.getDwfrdbxm());
                this.setDWXZQY(commonUnit.getExtension().getDwxzqy());
                this.setDJSYYZ(commonUnit.getExtension().getDjsyyz());
                this.setDWLB(commonUnit.getExtension().getDwlb());
                this.setDWFRDBZJLX(commonUnit.getDwfrdbzjlx());
                this.setPZJGMC(commonUnit.getExtension().getPzjgmc());
                this.setKGQK(commonUnit.getExtension().getKgqk());
                this.setDJZCH(commonUnit.getExtension().getDjzch());
                this.setZZJGDM(commonUnit.getZzjgdm());
                this.setDWLSGX(commonUnit.getDwlsgx());
                this.setDWDZ(commonUnit.getDwdz());
                this.setDWMC(commonUnit.getDwmc());
                this.setDWZH(commonUnit.getDwzh());
                this.setDWSSHY(commonUnit.getDwsshy());
                this.setDWZHZT(commonUnit.getCollectionUnitAccount().getDwzhzt());
            }});

            this.setJBRXX(new GetUnitAcctInfoDetailJBRXX(){{
                this.setJBRZJLX(commonUnit.getJbrzjlx());
                this.setJBRGDDHHM(commonUnit.getJbrgddhhm());
                this.setJBRXM(commonUnit.getJbrxm());
                this.setJBRZJHM(commonUnit.getJbrzjhm());
                this.setJBRSJHM(commonUnit.getJbrsjhm());
            }});

            this.setWTYHXX(new GetUnitAcctInfoDetailWTYHXX(){{

                this.setSTYHDM(commonUnit.getStyhdm());
                this.setSTYHMC(commonUnit.getStyhmc());
            }});

            if(collectionUnitBusinessDetails!=null&&collectionUnitBusinessDetails.getExtension()!=null){

                this.setCZY(collectionUnitBusinessDetails.getExtension().getCzy());

                this.setYWWD(collectionUnitBusinessDetails.getExtension().getYwwd().getMingCheng());
            }

            this.setBLZL(commonUnit.getExtension().getDwzl());
        }};
    }

}
