package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "UnitAcctDropPut")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnitAcctDropPut  implements Serializable{

   private  UnitAcctDropPutDWGJXX    DWGJXX;  // 

   public  UnitAcctDropPutDWGJXX getDWGJXX(){ 

       return this.DWGJXX;   

   }


   public  void setDWGJXX(UnitAcctDropPutDWGJXX  DWGJXX){ 

       this.DWGJXX = DWGJXX;   

   }


   public String toString(){ 

       return "UnitAcctDropPut{" +  
 
              "DWGJXX='" + this.DWGJXX + '\'' + 

      "}";

  } 
}