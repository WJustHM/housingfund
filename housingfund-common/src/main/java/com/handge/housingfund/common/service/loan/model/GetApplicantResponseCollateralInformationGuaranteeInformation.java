package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "GetApplicantResponseCollateralInformationGuaranteeInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetApplicantResponseCollateralInformationGuaranteeInformation  implements Serializable {

   private  String    YZBM;  //邮政编码 

   private  String    BZRXJZDZ;  //保证人现居住地址 

   private  String    BZRSFZHM;  //保证人身份证号码 

   private  String    BZRXM;  //保证人姓名 

   private  String    BZRLXDH;  //保证人联系电话 

   private  String    TXDZ;  //通讯地址 

   private  String    BZFLX;  //保证方类型（0：个人 1：机构） 

   public  String getYZBM(){ 

       return this.YZBM;   

   }


   public  void setYZBM(String  YZBM){ 

       this.YZBM = YZBM;   

   }


   public  String getBZRXJZDZ(){ 

       return this.BZRXJZDZ;   

   }


   public  void setBZRXJZDZ(String  BZRXJZDZ){ 

       this.BZRXJZDZ = BZRXJZDZ;   

   }


   public  String getBZRSFZHM(){ 

       return this.BZRSFZHM;   

   }


   public  void setBZRSFZHM(String  BZRSFZHM){ 

       this.BZRSFZHM = BZRSFZHM;   

   }


   public  String getBZRXM(){ 

       return this.BZRXM;   

   }


   public  void setBZRXM(String  BZRXM){ 

       this.BZRXM = BZRXM;   

   }


   public  String getBZRLXDH(){ 

       return this.BZRLXDH;   

   }


   public  void setBZRLXDH(String  BZRLXDH){ 

       this.BZRLXDH = BZRLXDH;   

   }


   public  String getTXDZ(){ 

       return this.TXDZ;   

   }


   public  void setTXDZ(String  TXDZ){ 

       this.TXDZ = TXDZ;   

   }


   public  String getBZFLX(){ 

       return this.BZFLX;   

   }


   public  void setBZFLX(String  BZFLX){ 

       this.BZFLX = BZFLX;   

   }


   public String toString(){ 

       return "GetApplicantResponseCollateralInformationGuaranteeInformation{" +  
 
              "YZBM='" + this.YZBM + '\'' + "," +
              "BZRXJZDZ='" + this.BZRXJZDZ + '\'' + "," +
              "BZRSFZHM='" + this.BZRSFZHM + '\'' + "," +
              "BZRXM='" + this.BZRXM + '\'' + "," +
              "BZRLXDH='" + this.BZRLXDH + '\'' + "," +
              "TXDZ='" + this.TXDZ + '\'' + "," +
              "BZFLX='" + this.BZFLX + '\'' + 

      "}";

  } 
}