package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "UnitDepositPost")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnitDepositPost  implements Serializable{

   private  String    YuanYin;  //通过/不通过 原因 

   private  String    SHZT;  //审核状态（01：通过；00：不通过） 

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

       return "UnitDepositPost{" +  
 
              "YuanYin='" + this.YuanYin + '\'' + "," +
              "SHZT='" + this.SHZT + '\'' + 

      "}";

  } 
}