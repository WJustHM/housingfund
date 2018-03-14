package com.handge.housingfund.common.service.loan.model;

import com.handge.housingfund.common.annotation.Annotation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "ApplicantPostCommonBorrowerInformationCoBorrowerInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicantPostCommonBorrowerInformationCoBorrowerInformation  implements Serializable {

   private  String    GTJKRGJJZH;  //共同借款人公积金账号 

   private  String    CHGX;  //参货关系 （0：本人或户主 1：配偶 2：子 3：女 4：孙子、孙女或外孙子、外孙女 5：父母 6：祖父母或外祖父母 7：兄、弟、姐、妹 8：其他） 

   @Annotation.Mobile(name = "手机号码")
   private  String    SJHM;  //手机号码 

   private  String    JCD;  //缴存地 

   @Annotation.Phone(name = "固定电话号码")
   private  String    GDDHHM;  //固定电话号码 

   private  String    GTJKRXM;  //共同借款人姓名 

   private  String    GTJKRZJLX;  //共同借款人证件类型 

   @Annotation.Money(name = "月收入")
   private  String    YSR;  //月收入 

   private  String    GTJKRZJHM;  //共同借款人证件号码 

   private String HKSZD;   //户口所在地

   public  String getGTJKRGJJZH(){

       return this.GTJKRGJJZH;   

   }


   public  void setGTJKRGJJZH(String  GTJKRGJJZH){ 

       this.GTJKRGJJZH = GTJKRGJJZH;   

   }


   public  String getCHGX(){ 

       return this.CHGX;   

   }


   public  void setCHGX(String  CHGX){ 

       this.CHGX = CHGX;   

   }


   public  String getSJHM(){ 

       return this.SJHM;   

   }


   public  void setSJHM(String  SJHM){ 

       this.SJHM = SJHM;   

   }


   public  String getJCD(){ 

       return this.JCD;   

   }


   public  void setJCD(String  JCD){ 

       this.JCD = JCD;   

   }


   public  String getGDDHHM(){ 

       return this.GDDHHM;   

   }


   public  void setGDDHHM(String  GDDHHM){ 

       this.GDDHHM = GDDHHM;   

   }


   public  String getGTJKRXM(){ 

       return this.GTJKRXM;   

   }


   public  void setGTJKRXM(String  GTJKRXM){ 

       this.GTJKRXM = GTJKRXM;   

   }


   public  String getGTJKRZJLX(){ 

       return this.GTJKRZJLX;   

   }


   public  void setGTJKRZJLX(String  GTJKRZJLX){ 

       this.GTJKRZJLX = GTJKRZJLX;   

   }


   public  String getYSR(){ 

       return this.YSR;   

   }


   public  void setYSR(String  YSR){ 

       this.YSR = YSR;   

   }


   public  String getGTJKRZJHM(){ 

       return this.GTJKRZJHM;   

   }


   public  void setGTJKRZJHM(String  GTJKRZJHM){ 

       this.GTJKRZJHM = GTJKRZJHM;   

   }
    public String getHKSZD() {
        return HKSZD;
    }
    public void setHKSZD(String HKSZD) {
        this.HKSZD = HKSZD;
    }

   public String toString(){ 

       return "ApplicantPostCommonBorrowerInformationCoBorrowerInformation{" +  
 
              "GTJKRGJJZH='" + this.GTJKRGJJZH + '\'' + "," +
              "CHGX='" + this.CHGX + '\'' + "," +
              "SJHM='" + this.SJHM + '\'' + "," +
              "JCD='" + this.JCD + '\'' + "," +
              "GDDHHM='" + this.GDDHHM + '\'' + "," +
              "GTJKRXM='" + this.GTJKRXM + '\'' + "," +
              "GTJKRZJLX='" + this.GTJKRZJLX + '\'' + "," +
              "YSR='" + this.YSR + '\'' + "," +
              "GTJKRZJHM='" + this.GTJKRZJHM + '\'' + 

      "}";

  } 
}