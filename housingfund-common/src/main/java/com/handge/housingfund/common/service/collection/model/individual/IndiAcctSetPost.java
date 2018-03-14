package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "IndiAcctSetPost")
@XmlAccessorType(XmlAccessType.FIELD)
public class IndiAcctSetPost  implements Serializable{

   private  IndiAcctSetPostGRZHXX    GRZHXX;  //个人账户信息

   private  String    JBRZJLX;  //经办人证件类型 

   private  String    BLZL;  //办理资料 

   private  String    JBRXM;  //经办人姓名 

   private  IndiAcctSetPostGRXX    GRXX;  //个人信息 

   private  String    YWWD;  //业务网点 

   private  String    JBRZJHM;  //经办人证件号码 

   private  IndiAcctSetPostDWXX    DWXX;  //单位信息 

   private  String    CZY;  //操作员 

   private  String    CZLX;  //操作类型（0:保存；1:提交；） 

   public  IndiAcctSetPostGRZHXX getGRZHXX(){

       return this.GRZHXX;

   }


   public  void setGRZHXX(IndiAcctSetPostGRZHXX  GRZHXX){

       this.GRZHXX = GRZHXX;

   }


   public  String getJBRZJLX(){ 

       return this.JBRZJLX;   

   }


   public  void setJBRZJLX(String  JBRZJLX){ 

       this.JBRZJLX = JBRZJLX;   

   }


   public  String getBLZL(){ 

       return this.BLZL;   

   }


   public  void setBLZL(String  BLZL){ 

       this.BLZL = BLZL;   

   }


   public  String getJBRXM(){ 

       return this.JBRXM;   

   }


   public  void setJBRXM(String  JBRXM){ 

       this.JBRXM = JBRXM;   

   }


   public  IndiAcctSetPostGRXX getGRXX(){ 

       return this.GRXX;   

   }


   public  void setGRXX(IndiAcctSetPostGRXX  GRXX){ 

       this.GRXX = GRXX;   

   }


   public  String getYWWD(){ 

       return this.YWWD;   

   }


   public  void setYWWD(String  YWWD){ 

       this.YWWD = YWWD;   

   }


   public  String getJBRZJHM(){ 

       return this.JBRZJHM;   

   }


   public  void setJBRZJHM(String  JBRZJHM){ 

       this.JBRZJHM = JBRZJHM;   

   }


   public  IndiAcctSetPostDWXX getDWXX(){ 

       return this.DWXX;   

   }


   public  void setDWXX(IndiAcctSetPostDWXX  DWXX){ 

       this.DWXX = DWXX;   

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


   public String toString(){ 

       return "IndiAcctSetPost{" +  
 
              "GRZHXX='" + this.GRZHXX + '\'' + "," +
              "JBRZJLX='" + this.JBRZJLX + '\'' + "," +
              "BLZL='" + this.BLZL + '\'' + "," +
              "JBRXM='" + this.JBRXM + '\'' + "," +
              "GRXX='" + this.GRXX + '\'' + "," +
              "YWWD='" + this.YWWD + '\'' + "," +
              "JBRZJHM='" + this.JBRZJHM + '\'' + "," +
              "DWXX='" + this.DWXX + '\'' + "," +
              "CZY='" + this.CZY + '\'' + "," +
              "CZLX='" + this.CZLX + '\'' + 

      "}";

  } 
}