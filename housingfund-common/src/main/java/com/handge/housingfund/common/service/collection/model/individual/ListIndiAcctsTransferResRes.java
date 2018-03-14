package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "ListIndiAcctsTransferResRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListIndiAcctsTransferResRes  implements Serializable{

   private  String    SLSJ;  //受理时间 

   private  String    YWLSH;  //业务流水号 

   private  String    ZRSXRQ;  //转入生效日期 

   private  String    ZhuangTai;  //状态 

   private  String    ZCDW;  //转出单位 

   public  String getSLSJ(){ 

       return this.SLSJ;   

   }


   public  void setSLSJ(String  SLSJ){ 

       this.SLSJ = SLSJ;   

   }


   public  String getYWLSH(){ 

       return this.YWLSH;   

   }


   public  void setYWLSH(String  YWLSH){ 

       this.YWLSH = YWLSH;   

   }


   public  String getZRSXRQ(){ 

       return this.ZRSXRQ;   

   }


   public  void setZRSXRQ(String  ZRSXRQ){ 

       this.ZRSXRQ = ZRSXRQ;   

   }


   public  String getZhuangTai(){ 

       return this.ZhuangTai;   

   }


   public  void setZhuangTai(String  ZhuangTai){ 

       this.ZhuangTai = ZhuangTai;   

   }


   public  String getZCDW(){ 

       return this.ZCDW;   

   }


   public  void setZCDW(String  ZCDW){ 

       this.ZCDW = ZCDW;   

   }


   public String toString(){ 

       return "ListIndiAcctsTransferResRes{" +  
 
              "SLSJ='" + this.SLSJ + '\'' + "," +
              "YWLSH='" + this.YWLSH + '\'' + "," +
              "ZRSXRQ='" + this.ZRSXRQ + '\'' + "," +
              "ZhuangTai='" + this.ZhuangTai + '\'' + "," +
              "ZCDW='" + this.ZCDW + '\'' + 

      "}";

  } 
}