package com.handge.housingfund.common.service.loan.model;

import com.handge.housingfund.common.annotation.Annotation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "ApplicantPostCollateralInformationPledgeInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicantPostCollateralInformationPledgeInformation  implements Serializable {

   private  String    ZYWSYQRSFZHM;  //质押物所有权人身份证号码 

   private  String    ZYWSYQRXM;  //质押物所有权人姓名 

   @Annotation.Money(name = "质押物价值")
   private  String    ZYWJZ;  //质押物价值 

   private  String    ZYWSYQRLXDH;  //质押物所有权人联系电话 

   private  String    ZYWMC;  //质押物名称 

   public  String getZYWSYQRSFZHM(){ 

       return this.ZYWSYQRSFZHM;   

   }


   public  void setZYWSYQRSFZHM(String  ZYWSYQRSFZHM){ 

       this.ZYWSYQRSFZHM = ZYWSYQRSFZHM;   

   }


   public  String getZYWSYQRXM(){ 

       return this.ZYWSYQRXM;   

   }


   public  void setZYWSYQRXM(String  ZYWSYQRXM){ 

       this.ZYWSYQRXM = ZYWSYQRXM;   

   }


   public  String getZYWJZ(){ 

       return this.ZYWJZ;   

   }


   public  void setZYWJZ(String  ZYWJZ){ 

       this.ZYWJZ = ZYWJZ;   

   }


   public  String getZYWSYQRLXDH(){ 

       return this.ZYWSYQRLXDH;   

   }


   public  void setZYWSYQRLXDH(String  ZYWSYQRLXDH){ 

       this.ZYWSYQRLXDH = ZYWSYQRLXDH;   

   }


   public  String getZYWMC(){ 

       return this.ZYWMC;   

   }


   public  void setZYWMC(String  ZYWMC){ 

       this.ZYWMC = ZYWMC;   

   }


   public String toString(){ 

       return "ApplicantPostCollateralInformationPledgeInformation{" +  
 
              "ZYWSYQRSFZHM='" + this.ZYWSYQRSFZHM + '\'' + "," +
              "ZYWSYQRXM='" + this.ZYWSYQRXM + '\'' + "," +
              "ZYWJZ='" + this.ZYWJZ + '\'' + "," +
              "ZYWSYQRLXDH='" + this.ZYWSYQRLXDH + '\'' + "," +
              "ZYWMC='" + this.ZYWMC + '\'' + 

      "}";

  } 
}