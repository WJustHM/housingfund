package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "UnitAcctReviewPost")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnitAcctReviewPost  implements Serializable{

   private  String    YuanYin;  //原因 

   private  String    SHZT;  //审核状态（01：通过；02：不通过） 

   public  String getYuanYin(){ 

       return this.YuanYin;   

   }


   public  void setYuanYin(String  YuanYin){ 

       this.YuanYin = YuanYin;   

   }


   public  String getSHZT(){ 

       return this.SHZT;   

   }


   public  void setSHZT(String  SHZT){ 

       this.SHZT = SHZT;   

   }


   public String toString(){ 

       return "UnitAcctReviewPost{" +  
 
              "YuanYin='" + this.YuanYin + '\'' + "," +
              "SHZT='" + this.SHZT + '\'' + 

      "}";

  } 
}