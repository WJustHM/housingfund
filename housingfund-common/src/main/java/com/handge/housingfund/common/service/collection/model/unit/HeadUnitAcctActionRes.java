package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "HeadUnitAcctActionRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class HeadUnitAcctActionRes  implements Serializable{

   private  HeadUnitAcctActionResDWGJXX    DWGJXX;  // 

   public  HeadUnitAcctActionResDWGJXX getDWGJXX(){ 

       return this.DWGJXX;   

   }


   public  void setDWGJXX(HeadUnitAcctActionResDWGJXX  DWGJXX){ 

       this.DWGJXX = DWGJXX;   

   }


   public String toString(){ 

       return "HeadUnitAcctActionRes{" +  
 
              "DWGJXX='" + this.DWGJXX + '\'' + 

      "}";

  } 
}