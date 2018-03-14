package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "HeadUnitAcctBasicResWTYHXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class HeadUnitAcctBasicResWTYHXX  implements Serializable{

   private  String    STYHDM;  //受托银行代码 

   private  String    STYHMC;  //受托银行名称 

   public  String getSTYHDM(){ 

       return this.STYHDM;   

   }


   public  void setSTYHDM(String  STYHDM){ 

       this.STYHDM = STYHDM;   

   }


   public  String getSTYHMC(){ 

       return this.STYHMC;   

   }


   public  void setSTYHMC(String  STYHMC){ 

       this.STYHMC = STYHMC;   

   }


   public String toString(){ 

       return "HeadUnitAcctBasicResWTYHXX{" +  
 
              "STYHDM='" + this.STYHDM + '\'' + "," +
              "STYHMC='" + this.STYHMC + '\'' + 

      "}";

  } 
}