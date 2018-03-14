package com.handge.housingfund.common.service.collection.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Created by 凡 on 2017/10/7.
 */
@XmlRootElement(name = "UnitBaseMessage")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnitBaseMessage {

    private String DWMC;    //单位名称
    private String DWZH;    //单位账号
    private String DWDZ;    //单位地址

    private String DWFRDBXM;    //单位法人代表姓名
    private String DWFRDBZJLX;  //单位法人代表证件类型
    private String DWFRDBZJHM;  //单位法人代表证件号码

    private String DWLSGX;  //单位隶属关系
    private String DWJJLX;  //单位经济类型
    private String DWSSHY;  //单位所属行业

    private String DWYB;    //单位邮编
    private String DWDZXX;  //单位电子信箱
    private String DWFXR;   //单位发薪日

    private String JBRXM;   //经办人姓名
    private String JBRGDDHHM;   //经办人固定电话号码
    private String JBRSJHM; //经办人手机号码
    private String JBRZJLX; //经办人证件类型
    private String JBRZJHM; //经办人证件号码

    private String ZZJGDM;  //组织机构代码
    private Date DWSLRQ;    //单位设立日期
    private Date DWKHRQ;    //单位开户日期

    private String STYHMC;  //受托银行名称
    private String STYHDM;  //受托银行代码

    //private CCommonUnitExtension cCommonUnitExtension;

}
