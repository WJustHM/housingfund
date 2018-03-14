package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "GetUnitAcctUnsealedRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetUnitAcctUnsealedRes  implements Serializable{

   private  GetUnitAcctUnsealedResDWQFXX    DWQFXX;  // 

   public  GetUnitAcctUnsealedResDWQFXX getDWQFXX(){ 

       return this.DWQFXX;   

   }


   public  void setDWQFXX(GetUnitAcctUnsealedResDWQFXX  DWQFXX){ 

       this.DWQFXX = DWQFXX;   

   }


   public String toString(){ 

       return "GetUnitAcctUnsealedRes{" +  
 
              "DWQFXX='" + this.DWQFXX + '\'' + 

      "}";

  } 
}