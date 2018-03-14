package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "HeadUnitPayBackReceiptResBJXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class HeadUnitPayBackReceiptResBJXX  implements Serializable{

   private  String    BJYY;  //补缴原因 

   private  String    GRBJE;  //个人补缴额 

   private  String    FSE;  //发生额 

   private  String    ZJHM;  //证件号码 

   private  String    ZJLX;  //证件类型 

   private  String    GRZH;  //个人账号 

   private  String    XingMing;  //姓名 

   private  String    DWBJE;  //单位补缴额 

   public  String getBJYY(){ 

       return this.BJYY;   

   }


   public  void setBJYY(String  BJYY){ 

       this.BJYY = BJYY;   

   }


   public  String getGRBJE(){ 

       return this.GRBJE;   

   }


   public  void setGRBJE(String  GRBJE){ 

       this.GRBJE = GRBJE;   

   }


   public  String getFSE(){ 

       return this.FSE;   

   }


   public  void setFSE(String  FSE){ 

       this.FSE = FSE;   

   }


   public  String getZJHM(){ 

       return this.ZJHM;   

   }


   public  void setZJHM(String  ZJHM){ 

       this.ZJHM = ZJHM;   

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


   public  String getDWBJE(){ 

       return this.DWBJE;   

   }


   public  void setDWBJE(String  DWBJE){ 

       this.DWBJE = DWBJE;   

   }


   public String toString(){ 

       return "HeadUnitPayBackReceiptResBJXX{" +  
 
              "BJYY='" + this.BJYY + '\'' + "," +
              "GRBJE='" + this.GRBJE + '\'' + "," +
              "FSE='" + this.FSE + '\'' + "," +
              "ZJHM='" + this.ZJHM + '\'' + "," +
              "ZJLX='" + this.ZJLX + '\'' + "," +
              "GRZH='" + this.GRZH + '\'' + "," +
              "XingMing='" + this.XingMing + '\'' + "," +
              "DWBJE='" + this.DWBJE + '\'' + 

      "}";

  } 
}