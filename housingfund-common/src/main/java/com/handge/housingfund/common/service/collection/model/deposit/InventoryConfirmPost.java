package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "InventoryConfirmPost")
@XmlAccessorType(XmlAccessType.FIELD)
public class InventoryConfirmPost  implements Serializable{

   private  String    JBRZJLX;  //经办人证件类型 

   private  String    FSE;  //发生额

   private  String    SLSJ;  //受理时间 

   private  String    QCNY;  //清册年月 

   private  Integer    FSRS;  //发生人数 

   private  String    JBRXM;  //经办人姓名 

   private  String    YWWD;  //业务网点 

   private  ArrayList<InventoryConfirmPostList>    list;  // 

   private  String    JBRZJHM;  //经办人证件号码 

   private  String    DWZH;  //单位账号 

   private  String    CZY;  //操作员 

   private  String    BLZL;  //办理资料 

   public  String getJBRZJLX(){ 

       return this.JBRZJLX;   

   }


   public  void setJBRZJLX(String  JBRZJLX){ 

       this.JBRZJLX = JBRZJLX;   

   }


   public  String getFSE(){

       return this.FSE;   

   }


   public  void setFSE(String  FSE){

       this.FSE = FSE;   

   }


   public  String getSLSJ(){ 

       return this.SLSJ;   

   }


   public  void setSLSJ(String  SLSJ){ 

       this.SLSJ = SLSJ;   

   }


   public  String getQCNY(){ 

       return this.QCNY;   

   }


   public  void setQCNY(String  QCNY){ 

       this.QCNY = QCNY;   

   }


   public  Integer getFSRS(){ 

       return this.FSRS;   

   }


   public  void setFSRS(Integer  FSRS){ 

       this.FSRS = FSRS;   

   }


   public  String getJBRXM(){ 

       return this.JBRXM;   

   }


   public  void setJBRXM(String  JBRXM){ 

       this.JBRXM = JBRXM;   

   }


   public  String getYWWD(){ 

       return this.YWWD;   

   }


   public  void setYWWD(String  YWWD){ 

       this.YWWD = YWWD;   

   }


   public  ArrayList<InventoryConfirmPostList> getlist(){ 

       return this.list;   

   }


   public  void setlist(ArrayList<InventoryConfirmPostList>  list){ 

       this.list = list;   

   }


   public  String getJBRZJHM(){ 

       return this.JBRZJHM;   

   }


   public  void setJBRZJHM(String  JBRZJHM){ 

       this.JBRZJHM = JBRZJHM;   

   }


   public  String getDWZH(){ 

       return this.DWZH;   

   }


   public  void setDWZH(String  DWZH){ 

       this.DWZH = DWZH;   

   }


   public  String getCZY(){ 

       return this.CZY;   

   }


   public  void setCZY(String  CZY){ 

       this.CZY = CZY;   

   }


   public  String getBLZL(){ 

       return this.BLZL;   

   }


   public  void setBLZL(String  BLZL){ 

       this.BLZL = BLZL;   

   }


   public String toString(){ 

       return "InventoryConfirmPost{" +  
 
              "JBRZJLX='" + this.JBRZJLX + '\'' + "," +
              "FSE='" + this.FSE + '\'' + "," +
              "SLSJ='" + this.SLSJ + '\'' + "," +
              "QCNY='" + this.QCNY + '\'' + "," +
              "FSRS='" + this.FSRS + '\'' + "," +
              "JBRXM='" + this.JBRXM + '\'' + "," +
              "YWWD='" + this.YWWD + '\'' + "," +
              "list='" + this.list + '\'' + "," +
              "JBRZJHM='" + this.JBRZJHM + '\'' + "," +
              "DWZH='" + this.DWZH + '\'' + "," +
              "CZY='" + this.CZY + '\'' + "," +
              "BLZL='" + this.BLZL + '\'' + 

      "}";

  } 
}