package com.handge.housingfund.common.service.collection.model.deposit;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "AutoRemittanceInventoryRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class AutoRemittanceInventoryRes  implements Serializable{

   private  String    FSE;  //发生额 

   private  String    JBRXM;  //经办人姓名 

   private  String    QCNY;  //清册年月 

   private  String JBRZJLX;  //经办人证件类型

   private  String    YWWD;  //业务网点 

   private  AutoRemittanceInventoryResYJEZHJ    YJEZHJ;  //月缴存额合计 

   private  ArrayList<AutoRemittanceInventoryResQCXX>    QCXX;  //清册信息 

   private  Integer    DWFCRS;  //单位封存人数 

   private  Integer    FSRS;  //发生人数 

   private  String JBRZJHM;  //经办人证件号码

   private  String    DWMC;  //单位名称 

   private  String    DWZH;  //单位账号 

   private  Integer    DWJCRS;  //单位缴存人数 

   private  String    CZY;  //操作员 

   private  String    BLZL;  // 

   public  String getFSE(){ 

       return this.FSE;   

   }


   public  void setFSE(String  FSE){ 

       this.FSE = FSE;   

   }


   public  String getJBRXM(){ 

       return this.JBRXM;   

   }


   public  void setJBRXM(String  JBRXM){ 

       this.JBRXM = JBRXM;   

   }


   public  String getQCNY(){ 

       return this.QCNY;   

   }


   public  void setQCNY(String  QCNY){ 

       this.QCNY = QCNY;   

   }


   public  String getJBRZJLX(){

       return this.JBRZJLX;

   }


   public  void setJBRZJLX(String JBRZJLX){

       this.JBRZJLX = JBRZJLX;

   }


   public  String getYWWD(){ 

       return this.YWWD;   

   }


   public  void setYWWD(String  YWWD){ 

       this.YWWD = YWWD;   

   }


   public  AutoRemittanceInventoryResYJEZHJ getYJEZHJ(){ 

       return this.YJEZHJ;   

   }


   public  void setYJEZHJ(AutoRemittanceInventoryResYJEZHJ  YJEZHJ){ 

       this.YJEZHJ = YJEZHJ;   

   }


   public  ArrayList<AutoRemittanceInventoryResQCXX> getQCXX(){ 

       return this.QCXX;   

   }


   public  void setQCXX(ArrayList<AutoRemittanceInventoryResQCXX>  QCXX){ 

       this.QCXX = QCXX;   

   }


   public  Integer getDWFCRS(){ 

       return this.DWFCRS;   

   }


   public  void setDWFCRS(Integer  DWFCRS){ 

       this.DWFCRS = DWFCRS;   

   }


   public  Integer getFSRS(){ 

       return this.FSRS;   

   }


   public  void setFSRS(Integer  FSRS){ 

       this.FSRS = FSRS;   

   }


   public  String getJBRZJHM(){

       return this.JBRZJHM;

   }


   public  void setJBRZJHM(String JBRZJHM){

       this.JBRZJHM = JBRZJHM;

   }


   public  String getDWMC(){ 

       return this.DWMC;   

   }


   public  void setDWMC(String  DWMC){ 

       this.DWMC = DWMC;   

   }


   public  String getDWZH(){ 

       return this.DWZH;   

   }


   public  void setDWZH(String  DWZH){ 

       this.DWZH = DWZH;   

   }


   public  Integer getDWJCRS(){ 

       return this.DWJCRS;   

   }


   public  void setDWJCRS(Integer  DWJCRS){ 

       this.DWJCRS = DWJCRS;   

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

       return "AutoRemittanceInventoryRes{" +  
 
              "FSE='" + this.FSE + '\'' + "," +
              "JBRXM='" + this.JBRXM + '\'' + "," +
              "QCNY='" + this.QCNY + '\'' + "," +
              "JBRZJLX='" + this.JBRZJLX + '\'' + "," +
              "YWWD='" + this.YWWD + '\'' + "," +
              "YJEZHJ='" + this.YJEZHJ + '\'' + "," +
              "QCXX='" + this.QCXX + '\'' + "," +
              "DWFCRS='" + this.DWFCRS + '\'' + "," +
              "FSRS='" + this.FSRS + '\'' + "," +
              "JBRZJHM='" + this.JBRZJHM + '\'' + "," +
              "DWMC='" + this.DWMC + '\'' + "," +
              "DWZH='" + this.DWZH + '\'' + "," +
              "DWJCRS='" + this.DWJCRS + '\'' + "," +
              "CZY='" + this.CZY + '\'' + "," +
              "BLZL='" + this.BLZL + '\'' + 

      "}";

  } 
}