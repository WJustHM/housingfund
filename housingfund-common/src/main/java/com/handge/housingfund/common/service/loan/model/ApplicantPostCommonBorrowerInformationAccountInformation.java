package com.handge.housingfund.common.service.loan.model;

import com.handge.housingfund.common.annotation.Annotation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "ApplicantPostCommonBorrowerInformationAccountInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicantPostCommonBorrowerInformationAccountInformation  implements Serializable {

   private  String    GRZHZT;  //个人账户状态 

   private  String    JZNY;  //缴至年月 

   @Annotation.Money(name = "月缴存额")
   private  String    YJCE;  //月缴存额 

   @Annotation.Money(name = "个人账户余额")
   private  String    GRZHYE;  //个人账户余额 

   @Annotation.Money(name = "个人缴存基数")
   private  String    GRJCJS;  //个人缴存基数 

   private  String    LXZCJCYS;  //连续正常缴存月数 

   public  String getGRZHZT(){ 

       return this.GRZHZT;   

   }


   public  void setGRZHZT(String  GRZHZT){ 

       this.GRZHZT = GRZHZT;   

   }


   public  String getJZNY(){ 

       return this.JZNY;   

   }


   public  void setJZNY(String  JZNY){ 

       this.JZNY = JZNY;   

   }


   public  String getYJCE(){ 

       return this.YJCE;   

   }


   public  void setYJCE(String  YJCE){ 

       this.YJCE = YJCE;   

   }


   public  String getGRZHYE(){ 

       return this.GRZHYE;   

   }


   public  void setGRZHYE(String  GRZHYE){ 

       this.GRZHYE = GRZHYE;   

   }


   public  String getGRJCJS(){ 

       return this.GRJCJS;   

   }


   public  void setGRJCJS(String  GRJCJS){ 

       this.GRJCJS = GRJCJS;   

   }


   public  String getLXZCJCYS(){ 

       return this.LXZCJCYS;   

   }


   public  void setLXZCJCYS(String  LXZCJCYS){ 

       this.LXZCJCYS = LXZCJCYS;   

   }


   public String toString(){ 

       return "ApplicantPostCommonBorrowerInformationAccountInformation{" +  
 
              "GRZHZT='" + this.GRZHZT + '\'' + "," +
              "JZNY='" + this.JZNY + '\'' + "," +
              "YJCE='" + this.YJCE + '\'' + "," +
              "GRZHYE='" + this.GRZHYE + '\'' + "," +
              "GRJCJS='" + this.GRJCJS + '\'' + "," +
              "LXZCJCYS='" + this.LXZCJCYS + '\'' + 

      "}";

  } 
}