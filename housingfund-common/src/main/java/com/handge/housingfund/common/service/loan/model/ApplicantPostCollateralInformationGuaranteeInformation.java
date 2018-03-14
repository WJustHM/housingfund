package com.handge.housingfund.common.service.loan.model;

import com.handge.housingfund.common.annotation.Annotation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "ApplicantPostCollateralInformationGuaranteeInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicantPostCollateralInformationGuaranteeInformation  implements Serializable {

   @Annotation.Postalcode(name = "邮政编码")
   private  String    YZBM;  //邮政编码 

   private  String    FRDBXM;  //法人代表姓名 

   private  String    FRDBXJZDZ;  //法人代表现居住地址 

   private  String    FRDBLXDH;  //法人代表联系电话 

   private  String    BZFLX;  //保证方类型（0：个人 1：机构） 

   private  String    TXDZ;  //通讯地址 

   private  String    FRDBSFZHM;  //法人代表身份证号码 

   public  String getYZBM(){ 

       return this.YZBM;   

   }


   public  void setYZBM(String  YZBM){ 

       this.YZBM = YZBM;   

   }


   public  String getFRDBXM(){ 

       return this.FRDBXM;   

   }


   public  void setFRDBXM(String  FRDBXM){ 

       this.FRDBXM = FRDBXM;   

   }


   public  String getFRDBXJZDZ(){ 

       return this.FRDBXJZDZ;   

   }


   public  void setFRDBXJZDZ(String  FRDBXJZDZ){ 

       this.FRDBXJZDZ = FRDBXJZDZ;   

   }


   public  String getFRDBLXDH(){ 

       return this.FRDBLXDH;   

   }


   public  void setFRDBLXDH(String  FRDBLXDH){ 

       this.FRDBLXDH = FRDBLXDH;   

   }


   public  String getBZFLX(){ 

       return this.BZFLX;   

   }


   public  void setBZFLX(String  BZFLX){ 

       this.BZFLX = BZFLX;   

   }


   public  String getTXDZ(){ 

       return this.TXDZ;   

   }


   public  void setTXDZ(String  TXDZ){ 

       this.TXDZ = TXDZ;   

   }


   public  String getFRDBSFZHM(){ 

       return this.FRDBSFZHM;   

   }


   public  void setFRDBSFZHM(String  FRDBSFZHM){ 

       this.FRDBSFZHM = FRDBSFZHM;   

   }


   public String toString(){ 

       return "ApplicantPostCollateralInformationGuaranteeInformation{" +  
 
              "YZBM='" + this.YZBM + '\'' + "," +
              "FRDBXM='" + this.FRDBXM + '\'' + "," +
              "FRDBXJZDZ='" + this.FRDBXJZDZ + '\'' + "," +
              "FRDBLXDH='" + this.FRDBLXDH + '\'' + "," +
              "BZFLX='" + this.BZFLX + '\'' + "," +
              "TXDZ='" + this.TXDZ + '\'' + "," +
              "FRDBSFZHM='" + this.FRDBSFZHM + '\'' + 

      "}";

  } 
}