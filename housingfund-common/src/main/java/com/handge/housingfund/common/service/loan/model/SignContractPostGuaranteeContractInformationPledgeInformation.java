package com.handge.housingfund.common.service.loan.model;

import com.handge.housingfund.common.annotation.Annotation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "SignContractPostGuaranteeContractInformationPledgeInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class SignContractPostGuaranteeContractInformationPledgeInformation  implements Serializable {

   private  String    ZYHTKSRQ;  //质押合同开始日期 

   private  String    ZYWSYQRSFHM;  //质押物所有权人身份证号码 

   private  String    ZYWBM;  //质押物编码 

   private  String    ZYWSYQRXM;  //质押物所有权人姓名 

   @Annotation.Money(name = "质押物价值")
   private  String    ZYWJZ;  //质押物价值 

   private  String    ZYHTBH;  //质押合同编号 

   private  String    ZYWSYQRLXDH;  //质押物所有权人联系电话 

   private  String    ZYHTJSRQ;  //质押合同结束日期 

   private  String    ZYWMC;  //质押物名称 

    private String    id;//
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
   public  String getZYHTKSRQ(){ 

       return this.ZYHTKSRQ;   

   }


   public  void setZYHTKSRQ(String  ZYHTKSRQ){ 

       this.ZYHTKSRQ = ZYHTKSRQ;   

   }


   public  String getZYWSYQRSFHM(){ 

       return this.ZYWSYQRSFHM;   

   }


   public  void setZYWSYQRSFHM(String  ZYWSYQRSFHM){ 

       this.ZYWSYQRSFHM = ZYWSYQRSFHM;   

   }


   public  String getZYWBM(){ 

       return this.ZYWBM;   

   }


   public  void setZYWBM(String  ZYWBM){ 

       this.ZYWBM = ZYWBM;   

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


   public  String getZYHTBH(){ 

       return this.ZYHTBH;   

   }


   public  void setZYHTBH(String  ZYHTBH){ 

       this.ZYHTBH = ZYHTBH;   

   }


   public  String getZYWSYQRLXDH(){ 

       return this.ZYWSYQRLXDH;   

   }


   public  void setZYWSYQRLXDH(String  ZYWSYQRLXDH){ 

       this.ZYWSYQRLXDH = ZYWSYQRLXDH;   

   }


   public  String getZYHTJSRQ(){ 

       return this.ZYHTJSRQ;   

   }


   public  void setZYHTJSRQ(String  ZYHTJSRQ){ 

       this.ZYHTJSRQ = ZYHTJSRQ;   

   }


   public  String getZYWMC(){ 

       return this.ZYWMC;   

   }


   public  void setZYWMC(String  ZYWMC){ 

       this.ZYWMC = ZYWMC;   

   }


   public String toString(){ 

       return "SignContractPostGuaranteeContractInformationPledgeInformation{" +  
 
              "ZYHTKSRQ='" + this.ZYHTKSRQ + '\'' + "," +
              "ZYWSYQRSFHM='" + this.ZYWSYQRSFHM + '\'' + "," +
              "ZYWBM='" + this.ZYWBM + '\'' + "," +
              "ZYWSYQRXM='" + this.ZYWSYQRXM + '\'' + "," +
              "ZYWJZ='" + this.ZYWJZ + '\'' + "," +
              "ZYHTBH='" + this.ZYHTBH + '\'' + "," +
              "ZYWSYQRLXDH='" + this.ZYWSYQRLXDH + '\'' + "," +
              "ZYHTJSRQ='" + this.ZYHTJSRQ + '\'' + "," +
              "ZYWMC='" + this.ZYWMC + '\'' + 

      "}";

  } 
}