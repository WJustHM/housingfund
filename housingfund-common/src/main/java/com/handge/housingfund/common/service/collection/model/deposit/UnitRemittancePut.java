package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "UnitRemittancePut")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnitRemittancePut  implements Serializable{

   private  String    WTYHZH;  //委托银行账户 

   private  String    WTYHMC;  //委托银行名称 

   private  String    FSE;  //发生额

   private  String    JBRXM;  //经办人姓名 

   private  String    JBRZJLX;  //经办人证件类型 

   private  String    YWWD;  //业务网点 

   private  String    GRYJCE;  //个人月缴存额

   private  String    CZLX;  //操作类型（0:保存；1:提交） 

   private  String    DWYJCE;  //单位月缴存额

   private  String    WTYHDM;  //委托银行代码 

   private  ArrayList<UnitRemittancePutQCXX>    QCXX;  //清册信息 

   private  String JBRZJHM;  //经办人证件号码

   private  Integer    FSRS;  //发生人数 

   private  String    HJFS;  //汇缴方式 

   private  String    JXRQ;  //计息日期 

   private  String    HBJNY;  //汇补缴年月 

   private  String    DWZH;  //单位账号 

   private  String    WTYHHM;  //委托银行户名 

   private  String    QCQRDH;  //清册确认单号 

   private  String    CZY;  //操作员 

   private  String    BLZL;  //办理资料 

   public  String getWTYHZH(){ 

       return this.WTYHZH;   

   }


   public  void setWTYHZH(String  WTYHZH){ 

       this.WTYHZH = WTYHZH;   

   }


   public  String getWTYHMC(){ 

       return this.WTYHMC;   

   }


   public  void setWTYHMC(String  WTYHMC){ 

       this.WTYHMC = WTYHMC;   

   }


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


   public  String getJBRZJLX(){ 

       return this.JBRZJLX;   

   }


   public  void setJBRZJLX(String  JBRZJLX){ 

       this.JBRZJLX = JBRZJLX;   

   }


   public  String getYWWD(){ 

       return this.YWWD;   

   }


   public  void setYWWD(String  YWWD){ 

       this.YWWD = YWWD;   

   }


   public  String getGRYJCE(){

       return this.GRYJCE;   

   }


   public  void setGRYJCE(String  GRYJCE){

       this.GRYJCE = GRYJCE;   

   }


   public  String getCZLX(){ 

       return this.CZLX;   

   }


   public  void setCZLX(String  CZLX){ 

       this.CZLX = CZLX;   

   }


   public  String getDWYJCE(){

       return this.DWYJCE;   

   }


   public  void setDWYJCE(String  DWYJCE){

       this.DWYJCE = DWYJCE;   

   }


   public  String getWTYHDM(){ 

       return this.WTYHDM;   

   }


   public  void setWTYHDM(String  WTYHDM){ 

       this.WTYHDM = WTYHDM;   

   }


   public  ArrayList<UnitRemittancePutQCXX> getQCXX(){ 

       return this.QCXX;   

   }


   public  void setQCXX(ArrayList<UnitRemittancePutQCXX>  QCXX){ 

       this.QCXX = QCXX;   

   }


   public  String getJBRZJHM(){

       return this.JBRZJHM;

   }


   public  void setJBRZJHM(String JBRZJHM){

       this.JBRZJHM = JBRZJHM;

   }


   public  Integer getFSRS(){ 

       return this.FSRS;   

   }


   public  void setFSRS(Integer  FSRS){ 

       this.FSRS = FSRS;   

   }


   public  String getHJFS(){ 

       return this.HJFS;   

   }


   public  void setHJFS(String  HJFS){ 

       this.HJFS = HJFS;   

   }


   public  String getJXRQ(){ 

       return this.JXRQ;   

   }


   public  void setJXRQ(String  JXRQ){ 

       this.JXRQ = JXRQ;   

   }


   public  String getHBJNY(){ 

       return this.HBJNY;   

   }


   public  void setHBJNY(String  HBJNY){ 

       this.HBJNY = HBJNY;   

   }


   public  String getDWZH(){ 

       return this.DWZH;   

   }


   public  void setDWZH(String  DWZH){ 

       this.DWZH = DWZH;   

   }


   public  String getWTYHHM(){ 

       return this.WTYHHM;   

   }


   public  void setWTYHHM(String  WTYHHM){ 

       this.WTYHHM = WTYHHM;   

   }


   public  String getQCQRDH(){ 

       return this.QCQRDH;   

   }


   public  void setQCQRDH(String  QCQRDH){ 

       this.QCQRDH = QCQRDH;   

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

       return "UnitRemittancePut{" +  
 
              "WTYHZH='" + this.WTYHZH + '\'' + "," +
              "WTYHMC='" + this.WTYHMC + '\'' + "," +
              "FSE='" + this.FSE + '\'' + "," +
              "JBRXM='" + this.JBRXM + '\'' + "," +
              "JBRZJLX='" + this.JBRZJLX + '\'' + "," +
              "YWWD='" + this.YWWD + '\'' + "," +
              "GRYJCE='" + this.GRYJCE + '\'' + "," +
              "CZLX='" + this.CZLX + '\'' + "," +
              "DWYJCE='" + this.DWYJCE + '\'' + "," +
              "WTYHDM='" + this.WTYHDM + '\'' + "," +
              "QCXX='" + this.QCXX + '\'' + "," +
              "JBRZJHM='" + this.JBRZJHM + '\'' + "," +
              "FSRS='" + this.FSRS + '\'' + "," +
              "HJFS='" + this.HJFS + '\'' + "," +
              "JXRQ='" + this.JXRQ + '\'' + "," +
              "HBJNY='" + this.HBJNY + '\'' + "," +
              "DWZH='" + this.DWZH + '\'' + "," +
              "WTYHHM='" + this.WTYHHM + '\'' + "," +
              "QCQRDH='" + this.QCQRDH + '\'' + "," +
              "CZY='" + this.CZY + '\'' + "," +
              "BLZL='" + this.BLZL + '\'' + 

      "}";

  } 
}