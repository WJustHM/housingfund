package com.handge.housingfund.common.service.loan.model;

import com.handge.housingfund.common.annotation.Annotation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "SignContractPostGuaranteeContractInformationMortgageInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class SignContractPostGuaranteeContractInformationMortgageInformation  implements Serializable {





   private  String    DYWQZH;  //抵押物权证号

    @Annotation.Money(name = "抵押物评估值")
   private  String    DYWPGZ;  //抵押物评估值 

   private  String    DYQJLRQ;  //抵押权建立日期 

   private  String    DYWTXQZH;  //抵押物他项权证号 

   private  String    DYQJCRQ;  //抵押权解除日期 

   private  String    DYFWZL;  //抵押房屋坐落 
    private String    id;//
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
   public  String getDYWQZH(){ 

       return this.DYWQZH;   

   }


   public  void setDYWQZH(String  DYWQZH){ 

       this.DYWQZH = DYWQZH;   

   }


   public  String getDYWPGZ(){ 

       return this.DYWPGZ;   

   }


   public  void setDYWPGZ(String  DYWPGZ){ 

       this.DYWPGZ = DYWPGZ;   

   }


   public  String getDYQJLRQ(){ 

       return this.DYQJLRQ;   

   }


   public  void setDYQJLRQ(String  DYQJLRQ){ 

       this.DYQJLRQ = DYQJLRQ;   

   }


   public  String getDYWTXQZH(){ 

       return this.DYWTXQZH;   

   }


   public  void setDYWTXQZH(String  DYWTXQZH){ 

       this.DYWTXQZH = DYWTXQZH;   

   }


   public  String getDYQJCRQ(){ 

       return this.DYQJCRQ;   

   }


   public  void setDYQJCRQ(String  DYQJCRQ){ 

       this.DYQJCRQ = DYQJCRQ;   

   }


   public  String getDYFWZL(){ 

       return this.DYFWZL;   

   }


   public  void setDYFWZL(String  DYFWZL){ 

       this.DYFWZL = DYFWZL;   

   }


   public String toString(){ 

       return "SignContractPostGuaranteeContractInformationMortgageInformation{" +  
 
              "DYWQZH='" + this.DYWQZH + '\'' + "," +
              "DYWPGZ='" + this.DYWPGZ + '\'' + "," +
              "DYQJLRQ='" + this.DYQJLRQ + '\'' + "," +
              "DYWTXQZH='" + this.DYWTXQZH + '\'' + "," +
              "DYQJCRQ='" + this.DYQJCRQ + '\'' + "," +
              "DYFWZL='" + this.DYFWZL + '\'' + 

      "}";

  } 
}