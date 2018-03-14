package com.handge.housingfund.common.service.loan.model;

import com.handge.housingfund.common.annotation.Annotation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "SignContractPostGuaranteeContractInformationGuaranteeInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class SignContractPostGuaranteeContractInformationGuaranteeInformation  implements Serializable {

   private  String    FHBZJRQ;  //返还保证金日期 

   private  String    BZHTBH;  //保证合同编号 

   private  String    BZJGMC;  //保证机构名称 

   @Annotation.Money(name = "贷款保证金")
   private  String    DKBZJ;  //贷款保证金 

    private String    id;//
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
   public  String getFHBZJRQ(){ 

       return this.FHBZJRQ;   

   }


   public  void setFHBZJRQ(String  FHBZJRQ){ 

       this.FHBZJRQ = FHBZJRQ;   

   }


   public  String getBZHTBH(){ 

       return this.BZHTBH;   

   }


   public  void setBZHTBH(String  BZHTBH){ 

       this.BZHTBH = BZHTBH;   

   }


   public  String getBZJGMC(){ 

       return this.BZJGMC;   

   }


   public  void setBZJGMC(String  BZJGMC){ 

       this.BZJGMC = BZJGMC;   

   }


   public  String getDKBZJ(){ 

       return this.DKBZJ;   

   }


   public  void setDKBZJ(String  DKBZJ){ 

       this.DKBZJ = DKBZJ;   

   }


   public String toString(){ 

       return "SignContractPostGuaranteeContractInformationGuaranteeInformation{" +  
 
              "FHBZJRQ='" + this.FHBZJRQ + '\'' + "," +
              "BZHTBH='" + this.BZHTBH + '\'' + "," +
              "BZJGMC='" + this.BZJGMC + '\'' + "," +
              "DKBZJ='" + this.DKBZJ + '\'' + 

      "}";

  } 
}