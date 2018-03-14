package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "UnitAcctSealedPut")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnitAcctSealedPut  implements Serializable{

   private  UnitAcctSealedPutDWGJXX    DWGJXX;  // 

   public  UnitAcctSealedPutDWGJXX getDWGJXX(){ 

       return this.DWGJXX;   

   }


   public  void setDWGJXX(UnitAcctSealedPutDWGJXX  DWGJXX){ 

       this.DWGJXX = DWGJXX;   

   }


   public String toString(){ 

       return "UnitAcctSealedPut{" +  
 
              "DWGJXX='" + this.DWGJXX + '\'' + 

      "}";

  } 
}