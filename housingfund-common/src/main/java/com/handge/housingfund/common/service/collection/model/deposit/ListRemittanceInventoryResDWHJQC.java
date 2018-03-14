package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "ListRemittanceInventoryResDWHJQC")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListRemittanceInventoryResDWHJQC  implements Serializable{

   private  String    GRJCJS;  //个人缴存基数 

   private  String    DWYJCE;  //单位月缴存额 

   private  String    HeJi;  //合计 

   private  String    ZJHM;  //证件号码 

   private  String    GRYJCE;  //个人月缴存额 

   private  String    ZJLX;  //证件类型 

   private  String    GRZH;  //个人账号 

   private  String    XingMing;  //姓名 

   public  String getGRJCJS(){ 

       return this.GRJCJS;   

   }


   public  void setGRJCJS(String  GRJCJS){ 

       this.GRJCJS = GRJCJS;   

   }


   public  String getDWYJCE(){ 

       return this.DWYJCE;   

   }


   public  void setDWYJCE(String  DWYJCE){ 

       this.DWYJCE = DWYJCE;   

   }


   public  String getHeJi(){ 

       return this.HeJi;   

   }


   public  void setHeJi(String  HeJi){ 

       this.HeJi = HeJi;   

   }


   public  String getZJHM(){ 

       return this.ZJHM;   

   }


   public  void setZJHM(String  ZJHM){ 

       this.ZJHM = ZJHM;   

   }


   public  String getGRYJCE(){ 

       return this.GRYJCE;   

   }


   public  void setGRYJCE(String  GRYJCE){ 

       this.GRYJCE = GRYJCE;   

   }


   public  String getZJLX(){ 

       return this.ZJLX;   

   }


   public  void setZJLX(String  ZJLX){ 

       this.ZJLX = ZJLX;   

   }


   public  String getGRZH(){ 

       return this.GRZH;   

   }


   public  void setGRZH(String  GRZH){ 

       this.GRZH = GRZH;   

   }


   public  String getXingMing(){ 

       return this.XingMing;   

   }


   public  void setXingMing(String  XingMing){ 

       this.XingMing = XingMing;   

   }


   public String toString(){ 

       return "ListRemittanceInventoryResDWHJQC{" +  
 
              "GRJCJS='" + this.GRJCJS + '\'' + "," +
              "DWYJCE='" + this.DWYJCE + '\'' + "," +
              "HeJi='" + this.HeJi + '\'' + "," +
              "ZJHM='" + this.ZJHM + '\'' + "," +
              "GRYJCE='" + this.GRYJCE + '\'' + "," +
              "ZJLX='" + this.ZJLX + '\'' + "," +
              "GRZH='" + this.GRZH + '\'' + "," +
              "XingMing='" + this.XingMing + '\'' + 

      "}";

  } 
}