package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "ListIndiAcctHistoryResRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListIndiAcctHistoryResRes  implements Serializable{

   private  String    YWWD;  //业务网点 

   private  String    SLSJ;  //受理时间 

   private  String    CZY;  //操作员 

   private  String    CZQD;  //操作渠道 

   private  String    CZNR;  //操作内容 

   public  String getYWWD(){ 

       return this.YWWD;   

   }


   public  void setYWWD(String  YWWD){ 

       this.YWWD = YWWD;   

   }


   public  String getSLSJ(){ 

       return this.SLSJ;   

   }


   public  void setSLSJ(String  SLSJ){ 

       this.SLSJ = SLSJ;   

   }


   public  String getCZY(){ 

       return this.CZY;   

   }


   public  void setCZY(String  CZY){ 

       this.CZY = CZY;   

   }


   public  String getCZQD(){ 

       return this.CZQD;   

   }


   public  void setCZQD(String  CZQD){ 

       this.CZQD = CZQD;   

   }


   public  String getCZNR(){ 

       return this.CZNR;   

   }


   public  void setCZNR(String  CZNR){ 

       this.CZNR = CZNR;   

   }


   public String toString(){ 

       return "ListIndiAcctHistoryResRes{" +  
 
              "YWWD='" + this.YWWD + '\'' + "," +
              "SLSJ='" + this.SLSJ + '\'' + "," +
              "CZY='" + this.CZY + '\'' + "," +
              "CZQD='" + this.CZQD + '\'' + "," +
              "CZNR='" + this.CZNR + '\'' + 

      "}";

  } 
}