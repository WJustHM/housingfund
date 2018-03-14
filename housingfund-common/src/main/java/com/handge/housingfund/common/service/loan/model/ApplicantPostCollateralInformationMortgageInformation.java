package com.handge.housingfund.common.service.loan.model;

import com.handge.housingfund.common.annotation.Annotation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "ApplicantPostCollateralInformationMortgageInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicantPostCollateralInformationMortgageInformation  implements Serializable {

   private  String    DYWFWZL;  //抵押物房屋坐落 

   private  String    FWJG;  //房屋结构（0：框架 1：砖混 2：土木 3：其他） 

   private  String    DYWGYQRLXDH;  //抵押物共有权人联系电话 

   private  String    DYWGYQRXM;  //抵押物共有权人姓名 

   private  String    DYWSYQRLXDH;  //抵押物所有权人联系电话 

   private  String    FWMJ;  //房屋面积 

   private  String    QSZSBH;  //权属证书编号 

   private  String    DYWSYQRXM;  //抵押物所有权人姓名 

   private  String    DYWSYQRSFZHM;  //抵押物所有权人身份证号码 

   private  String    DYFWXS;  //抵押房屋形式（0：住宅 （期房） 1：住宅（现房） 2：商铺 3：其他） 

   private  String    DYWMC;  //抵押物名称 

   @Annotation.Money(name = "抵押物评估价值")
   private  String    DYWPGJZ;  //抵押物评估价值 

   private  String    DYWGYQRSFZHM;  //抵押物共有权人身份证号码 

   public  String getDYWFWZL(){ 

       return this.DYWFWZL;   

   }


   public  void setDYWFWZL(String  DYWFWZL){ 

       this.DYWFWZL = DYWFWZL;   

   }


   public  String getFWJG(){ 

       return this.FWJG;   

   }


   public  void setFWJG(String  FWJG){ 

       this.FWJG = FWJG;   

   }


   public  String getDYWGYQRLXDH(){ 

       return this.DYWGYQRLXDH;   

   }


   public  void setDYWGYQRLXDH(String  DYWGYQRLXDH){ 

       this.DYWGYQRLXDH = DYWGYQRLXDH;   

   }


   public  String getDYWGYQRXM(){ 

       return this.DYWGYQRXM;   

   }


   public  void setDYWGYQRXM(String  DYWGYQRXM){ 

       this.DYWGYQRXM = DYWGYQRXM;   

   }


   public  String getDYWSYQRLXDH(){ 

       return this.DYWSYQRLXDH;   

   }


   public  void setDYWSYQRLXDH(String  DYWSYQRLXDH){ 

       this.DYWSYQRLXDH = DYWSYQRLXDH;   

   }


   public  String getFWMJ(){ 

       return this.FWMJ;   

   }


   public  void setFWMJ(String  FWMJ){ 

       this.FWMJ = FWMJ;   

   }


   public  String getQSZSBH(){ 

       return this.QSZSBH;   

   }


   public  void setQSZSBH(String  QSZSBH){ 

       this.QSZSBH = QSZSBH;   

   }


   public  String getDYWSYQRXM(){ 

       return this.DYWSYQRXM;   

   }


   public  void setDYWSYQRXM(String  DYWSYQRXM){ 

       this.DYWSYQRXM = DYWSYQRXM;   

   }


   public  String getDYWSYQRSFZHM(){ 

       return this.DYWSYQRSFZHM;   

   }


   public  void setDYWSYQRSFZHM(String  DYWSYQRSFZHM){ 

       this.DYWSYQRSFZHM = DYWSYQRSFZHM;   

   }


   public  String getDYFWXS(){ 

       return this.DYFWXS;   

   }


   public  void setDYFWXS(String  DYFWXS){ 

       this.DYFWXS = DYFWXS;   

   }


   public  String getDYWMC(){ 

       return this.DYWMC;   

   }


   public  void setDYWMC(String  DYWMC){ 

       this.DYWMC = DYWMC;   

   }


   public  String getDYWPGJZ(){ 

       return this.DYWPGJZ;   

   }


   public  void setDYWPGJZ(String  DYWPGJZ){ 

       this.DYWPGJZ = DYWPGJZ;   

   }


   public  String getDYWGYQRSFZHM(){ 

       return this.DYWGYQRSFZHM;   

   }


   public  void setDYWGYQRSFZHM(String  DYWGYQRSFZHM){ 

       this.DYWGYQRSFZHM = DYWGYQRSFZHM;   

   }


   public String toString(){ 

       return "ApplicantPostCollateralInformationMortgageInformation{" +  
 
              "DYWFWZL='" + this.DYWFWZL + '\'' + "," +
              "FWJG='" + this.FWJG + '\'' + "," +
              "DYWGYQRLXDH='" + this.DYWGYQRLXDH + '\'' + "," +
              "DYWGYQRXM='" + this.DYWGYQRXM + '\'' + "," +
              "DYWSYQRLXDH='" + this.DYWSYQRLXDH + '\'' + "," +
              "FWMJ='" + this.FWMJ + '\'' + "," +
              "QSZSBH='" + this.QSZSBH + '\'' + "," +
              "DYWSYQRXM='" + this.DYWSYQRXM + '\'' + "," +
              "DYWSYQRSFZHM='" + this.DYWSYQRSFZHM + '\'' + "," +
              "DYFWXS='" + this.DYFWXS + '\'' + "," +
              "DYWMC='" + this.DYWMC + '\'' + "," +
              "DYWPGJZ='" + this.DYWPGJZ + '\'' + "," +
              "DYWGYQRSFZHM='" + this.DYWGYQRSFZHM + '\'' + 

      "}";

  } 
}