package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "UnitAcctUnsealedPut")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnitAcctUnsealedPut  implements Serializable{

   private  UnitAcctUnsealedPutDWGJXX    DWGJXX;  // 

   public  UnitAcctUnsealedPutDWGJXX getDWGJXX(){ 

       return this.DWGJXX;   

   }


   public  void setDWGJXX(UnitAcctUnsealedPutDWGJXX  DWGJXX){ 

       this.DWGJXX = DWGJXX;   

   }


   public String toString(){ 

       return "UnitAcctUnsealedPut{" +  
 
              "DWGJXX='" + this.DWGJXX + '\'' + 

      "}";

  } 
}