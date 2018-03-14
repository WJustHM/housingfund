package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "GetUnitAcctSealedRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetUnitAcctSealedRes  implements Serializable{

   private  GetUnitAcctSealedResDWFCXX    DWFCXX;  // 

   public  GetUnitAcctSealedResDWFCXX getDWFCXX(){ 

       return this.DWFCXX;   

   }


   public  void setDWFCXX(GetUnitAcctSealedResDWFCXX  DWFCXX){ 

       this.DWFCXX = DWFCXX;   

   }


   public String toString(){ 

       return "GetUnitAcctSealedRes{" +  
 
              "DWFCXX='" + this.DWFCXX + '\'' + 

      "}";

  } 
}