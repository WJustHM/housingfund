package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "IndiAcctMergePost")
@XmlAccessorType(XmlAccessType.FIELD)
public class IndiAcctMergePost  implements Serializable{

   private  String    BLZH;  //保留账号 

   private  String    JBRZJLX;  //经办人证件类型 

   private  ArrayList<String>    HBZHList;  //合并账号列表 

   private  String    JBRXM;  //经办人姓名 

   private  String    ZJHM;  //证件号码 

   private  String    YWWD;  //业务网点 

   private  String    ZJLX;  //证件类型 

   private  String    JBRZJHM;  //经办人证件号码 

   private  String    XingMing;  //姓名 

   private  String    CZY;  //操作员 

   private  String    CZLX;  //操作类型（0:保存；1:提交;） 

   private  String    BLZL;  //办理资料 

   public  String getBLZH(){ 

       return this.BLZH;   

   }


   public  void setBLZH(String  BLZH){ 

       this.BLZH = BLZH;   

   }


   public  String getJBRZJLX(){ 

       return this.JBRZJLX;   

   }


   public  void setJBRZJLX(String  JBRZJLX){ 

       this.JBRZJLX = JBRZJLX;   

   }


   public  ArrayList<String> getHBZHList(){ 

       return this.HBZHList;   

   }


   public  void setHBZHList(ArrayList<String>  HBZHList){ 

       this.HBZHList = HBZHList;   

   }


   public  String getJBRXM(){ 

       return this.JBRXM;   

   }


   public  void setJBRXM(String  JBRXM){ 

       this.JBRXM = JBRXM;   

   }


   public  String getZJHM(){ 

       return this.ZJHM;   

   }


   public  void setZJHM(String  ZJHM){ 

       this.ZJHM = ZJHM;   

   }


   public  String getYWWD(){ 

       return this.YWWD;   

   }


   public  void setYWWD(String  YWWD){ 

       this.YWWD = YWWD;   

   }


   public  String getZJLX(){ 

       return this.ZJLX;   

   }


   public  void setZJLX(String  ZJLX){ 

       this.ZJLX = ZJLX;   

   }


   public  String getJBRZJHM(){ 

       return this.JBRZJHM;   

   }


   public  void setJBRZJHM(String  JBRZJHM){ 

       this.JBRZJHM = JBRZJHM;   

   }


   public  String getXingMing(){ 

       return this.XingMing;   

   }


   public  void setXingMing(String  XingMing){ 

       this.XingMing = XingMing;   

   }


   public  String getCZY(){ 

       return this.CZY;   

   }


   public  void setCZY(String  CZY){ 

       this.CZY = CZY;   

   }


   public  String getCZLX(){ 

       return this.CZLX;   

   }


   public  void setCZLX(String  CZLX){ 

       this.CZLX = CZLX;   

   }


   public  String getBLZL(){ 

       return this.BLZL;   

   }


   public  void setBLZL(String  BLZL){ 

       this.BLZL = BLZL;   

   }


   public String toString(){ 

       return "IndiAcctMergePost{" +  
 
              "BLZH='" + this.BLZH + '\'' + "," +
              "JBRZJLX='" + this.JBRZJLX + '\'' + "," +
              "HBZHList='" + this.HBZHList + '\'' + "," +
              "JBRXM='" + this.JBRXM + '\'' + "," +
              "ZJHM='" + this.ZJHM + '\'' + "," +
              "YWWD='" + this.YWWD + '\'' + "," +
              "ZJLX='" + this.ZJLX + '\'' + "," +
              "JBRZJHM='" + this.JBRZJHM + '\'' + "," +
              "XingMing='" + this.XingMing + '\'' + "," +
              "CZY='" + this.CZY + '\'' + "," +
              "CZLX='" + this.CZLX + '\'' + "," +
              "BLZL='" + this.BLZL + '\'' + 

      "}";

  } 
}