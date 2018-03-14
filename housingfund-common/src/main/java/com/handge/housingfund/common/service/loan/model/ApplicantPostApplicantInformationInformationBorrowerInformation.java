package com.handge.housingfund.common.service.loan.model;

import com.handge.housingfund.common.annotation.Annotation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "ApplicantPostApplicantInformationInformationBorrowerInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicantPostApplicantInformationInformationBorrowerInformation  implements Serializable {

   private  String    HYZK;  //婚姻状态（0：已婚 1：未婚 2：丧偶 3：离婚 4：未说明的婚姻状况） 

   private  String    NianLing;  //年龄 

   private  String    JTZZ;  //家庭住址 

   private  String    CSNY;  //出生年月 

   private  String    JKRZJHM;  //借款人证件号码 

   private  String    YGXZ;  //用工性质（0：正式职工 1：合同制 2：聘用制） 

   @Annotation.Money(name = "月收入")
   private  String    YSR;  //月收入 

   private  String    JKRXM;  //借款人姓名 

   private  String    HKSZD;  //户口所在地 

   private  String    ZhiCheng;  //职称 

   private  String    JKRZJLX;  //借款人证件类型 

   @Annotation.Mobile(name = "手机号码")
   private  String    SJHM;  //手机号码 

   private  String    JKZK;  //健康状态（0：良好 1：一般 2：差） 

   @Annotation.Phone(name = "固定电话号码")
   private  String    GDDHHM;  //固定电话号码 

   private  String    ZYJJLY;  //主要经济来源（0：工资收入 1：个体经营收入 2：其他非工资收入） 

   private  String    XingBie;  //性别 

   @Annotation.Money(name = "家庭月收入")
   private  String    JTYSR;  //家庭月收入 

   private  String    XueLi;  //学历 

   private  String    ZhiWu;  //职务 

   public  String getHYZK(){ 

       return this.HYZK;   

   }


   public  void setHYZK(String  HYZK){ 

       this.HYZK = HYZK;   

   }


   public  String getNianLing(){ 

       return this.NianLing;   

   }


   public  void setNianLing(String  NianLing){ 

       this.NianLing = NianLing;   

   }


   public  String getJTZZ(){ 

       return this.JTZZ;   

   }


   public  void setJTZZ(String  JTZZ){ 

       this.JTZZ = JTZZ;   

   }


   public  String getCSNY(){ 

       return this.CSNY;   

   }


   public  void setCSNY(String  CSNY){ 

       this.CSNY = CSNY;   

   }


   public  String getJKRZJHM(){ 

       return this.JKRZJHM;   

   }


   public  void setJKRZJHM(String  JKRZJHM){ 

       this.JKRZJHM = JKRZJHM;   

   }


   public  String getYGXZ(){ 

       return this.YGXZ;   

   }


   public  void setYGXZ(String  YGXZ){ 

       this.YGXZ = YGXZ;   

   }


   public  String getYSR(){ 

       return this.YSR;   

   }


   public  void setYSR(String  YSR){ 

       this.YSR = YSR;   

   }


   public  String getJKRXM(){ 

       return this.JKRXM;   

   }


   public  void setJKRXM(String  JKRXM){ 

       this.JKRXM = JKRXM;   

   }


   public  String getHKSZD(){ 

       return this.HKSZD;   

   }


   public  void setHKSZD(String  HKSZD){ 

       this.HKSZD = HKSZD;   

   }


   public  String getZhiCheng(){ 

       return this.ZhiCheng;   

   }


   public  void setZhiCheng(String  ZhiCheng){ 

       this.ZhiCheng = ZhiCheng;   

   }


   public  String getJKRZJLX(){ 

       return this.JKRZJLX;   

   }


   public  void setJKRZJLX(String  JKRZJLX){ 

       this.JKRZJLX = JKRZJLX;   

   }


   public  String getSJHM(){ 

       return this.SJHM;   

   }


   public  void setSJHM(String  SJHM){ 

       this.SJHM = SJHM;   

   }


   public  String getJKZK(){ 

       return this.JKZK;   

   }


   public  void setJKZK(String  JKZK){ 

       this.JKZK = JKZK;   

   }


   public  String getGDDHHM(){ 

       return this.GDDHHM;   

   }


   public  void setGDDHHM(String  GDDHHM){ 

       this.GDDHHM = GDDHHM;   

   }


   public  String getZYJJLY(){ 

       return this.ZYJJLY;   

   }


   public  void setZYJJLY(String  ZYJJLY){ 

       this.ZYJJLY = ZYJJLY;   

   }


   public  String getXingBie(){ 

       return this.XingBie;   

   }


   public  void setXingBie(String  XingBie){ 

       this.XingBie = XingBie;   

   }


   public  String getJTYSR(){ 

       return this.JTYSR;   

   }


   public  void setJTYSR(String  JTYSR){ 

       this.JTYSR = JTYSR;   

   }


   public  String getXueLi(){ 

       return this.XueLi;   

   }


   public  void setXueLi(String  XueLi){ 

       this.XueLi = XueLi;   

   }


   public  String getZhiWu(){ 

       return this.ZhiWu;   

   }


   public  void setZhiWu(String  ZhiWu){ 

       this.ZhiWu = ZhiWu;   

   }


   public String toString(){ 

       return "ApplicantPostApplicantInformationInformationBorrowerInformation{" +  
 
              "HYZK='" + this.HYZK + '\'' + "," +
              "NianLing='" + this.NianLing + '\'' + "," +
              "JTZZ='" + this.JTZZ + '\'' + "," +
              "CSNY='" + this.CSNY + '\'' + "," +
              "JKRZJHM='" + this.JKRZJHM + '\'' + "," +
              "YGXZ='" + this.YGXZ + '\'' + "," +
              "YSR='" + this.YSR + '\'' + "," +
              "JKRXM='" + this.JKRXM + '\'' + "," +
              "HKSZD='" + this.HKSZD + '\'' + "," +
              "ZhiCheng='" + this.ZhiCheng + '\'' + "," +
              "JKRZJLX='" + this.JKRZJLX + '\'' + "," +
              "SJHM='" + this.SJHM + '\'' + "," +
              "JKZK='" + this.JKZK + '\'' + "," +
              "GDDHHM='" + this.GDDHHM + '\'' + "," +
              "ZYJJLY='" + this.ZYJJLY + '\'' + "," +
              "XingBie='" + this.XingBie + '\'' + "," +
              "JTYSR='" + this.JTYSR + '\'' + "," +
              "XueLi='" + this.XueLi + '\'' + "," +
              "ZhiWu='" + this.ZhiWu + '\'' + 

      "}";

  } 
}