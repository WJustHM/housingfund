package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "AutoUnitAcctActionRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class AutoUnitAcctActionRes  implements Serializable{

   private  AutoUnitAcctActionResDWGJXX    DWGJXX;  // 

   public  AutoUnitAcctActionResDWGJXX getDWGJXX(){ 

       return this.DWGJXX;   

   }


   public  void setDWGJXX(AutoUnitAcctActionResDWGJXX  DWGJXX){ 

       this.DWGJXX = DWGJXX;   

   }


   public String toString(){ 

       return "AutoUnitAcctActionRes{" +  
 
              "DWGJXX='" + this.DWGJXX + '\'' + 

      "}";

  } 
}