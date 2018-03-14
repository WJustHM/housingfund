package com.handge.housingfund.common.service.collection.model.deposit;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "GetUnitRemittanceRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetUnitRemittanceRes  implements Serializable{

   private  String STYHZH;  //受托银行账户

   private  String STYHMC;  //受托银行名称

   private  String   FSE;  //发生额

   private  String    YWWD;  //业务网点 

   private  String    ZSYE;  //暂收余额

   private  String    JZNY;  //缴至年月 

   private  String STYHDM;  //受托银行代码

   private  String    HJFS;  //汇缴方式 

   private  Integer    DWFCRS;  //单位封存人数

   private  String    DWMC;  //单位名称 

   private  String    QCQRDH;  //清册确认单号 

   private  String    DWZH;  //单位账号 

   private  Integer    DWJCRS;  //单位缴存人数 

   private  String    BLZL;  //办理资料 

   private  String    CZY;  //操作员 

   private  String    JBRZJLX;  //经办人证件类型 

   private  String    YWLSH;  //业务流水号 

   private  String    JBRXM;  //经办人姓名 

   private  ArrayList<GetUnitRemittanceResQCQR>    QCQR;  //清册确认 

   private  String    DWZHYE;  //单位账户余额

   private  String    JXRQ;  //计息日期 

   private  String    FSRS;  //发生人数

   private  String JBRZJHM;  //经办人证件号码

   private  String    HBJNY;  //汇补缴年月 

   private  String    DWYHJNY;  //单位应汇缴年月 

   private  String STYHHM;  //受托银行户名

   public  String getSTYHZH(){

       return this.STYHZH;

   }


   public  void setSTYHZH(String STYHZH){

       this.STYHZH = STYHZH;

   }


   public  String getSTYHMC(){

       return this.STYHMC;

   }


   public  void setSTYHMC(String STYHMC){

       this.STYHMC = STYHMC;

   }


   public  String getFSE(){

       return this.FSE;   

   }


   public  void setFSE(String  FSE){

       this.FSE = FSE;   

   }


   public  String getYWWD(){ 

       return this.YWWD;   

   }


   public  void setYWWD(String  YWWD){ 

       this.YWWD = YWWD;   

   }


   public  String getZSYE(){

       return this.ZSYE;   

   }


   public  void setZSYE(String  ZSYE){

       this.ZSYE = ZSYE;   

   }


   public  String getJZNY(){ 

       return this.JZNY;   

   }


   public  void setJZNY(String  JZNY){ 

       this.JZNY = JZNY;   

   }


   public  String getSTYHDM(){

       return this.STYHDM;

   }


   public  void setSTYHDM(String STYHDM){

       this.STYHDM = STYHDM;

   }


   public  String getHJFS(){ 

       return this.HJFS;   

   }


   public  void setHJFS(String  HJFS){ 

       this.HJFS = HJFS;   

   }


   public  Integer getDWFCRS(){ 

       return this.DWFCRS;   

   }


   public  void setDWFCRS(Integer  DWFCRS){ 

       this.DWFCRS = DWFCRS;   

   }


   public  String getDWMC(){ 

       return this.DWMC;   

   }


   public  void setDWMC(String  DWMC){ 

       this.DWMC = DWMC;   

   }


   public  String getQCQRDH(){ 

       return this.QCQRDH;   

   }


   public  void setQCQRDH(String  QCQRDH){ 

       this.QCQRDH = QCQRDH;   

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


   public  String getBLZL(){ 

       return this.BLZL;   

   }


   public  void setBLZL(String  BLZL){ 

       this.BLZL = BLZL;   

   }


   public  String getCZY(){ 

       return this.CZY;   

   }


   public  void setCZY(String  CZY){ 

       this.CZY = CZY;   

   }


   public  String getJBRZJLX(){ 

       return this.JBRZJLX;   

   }


   public  void setJBRZJLX(String  JBRZJLX){ 

       this.JBRZJLX = JBRZJLX;   

   }


   public  String getYWLSH(){ 

       return this.YWLSH;   

   }


   public  void setYWLSH(String  YWLSH){ 

       this.YWLSH = YWLSH;   

   }


   public  String getJBRXM(){ 

       return this.JBRXM;   

   }


   public  void setJBRXM(String  JBRXM){ 

       this.JBRXM = JBRXM;   

   }


   public  ArrayList<GetUnitRemittanceResQCQR> getQCQR(){ 

       return this.QCQR;   

   }


   public  void setQCQR(ArrayList<GetUnitRemittanceResQCQR>  QCQR){ 

       this.QCQR = QCQR;   

   }


   public  String getDWZHYE(){

       return this.DWZHYE;   

   }


   public  void setDWZHYE(String  DWZHYE){

       this.DWZHYE = DWZHYE;   

   }


   public  String getJXRQ(){ 

       return this.JXRQ;   

   }


   public  void setJXRQ(String  JXRQ){ 

       this.JXRQ = JXRQ;   

   }


   public  String getFSRS(){

       return this.FSRS;   

   }


   public  void setFSRS(String  FSRS){

       this.FSRS = FSRS;   

   }


   public  String getJBRZJHM(){

       return this.JBRZJHM;

   }


   public  void setJBRZJHM(String JBRZJHM){

       this.JBRZJHM = JBRZJHM;

   }


   public  String getHBJNY(){ 

       return this.HBJNY;   

   }


   public  void setHBJNY(String  HBJNY){ 

       this.HBJNY = HBJNY;   

   }


   public  String getDWYHJNY(){ 

       return this.DWYHJNY;   

   }


   public  void setDWYHJNY(String  DWYHJNY){ 

       this.DWYHJNY = DWYHJNY;   

   }


   public  String getSTYHHM(){

       return this.STYHHM;

   }


   public  void setSTYHHM(String STYHHM){

       this.STYHHM = STYHHM;

   }


   public String toString(){ 

       return "GetUnitRemittanceRes{" +  
 
              "STYHZH='" + this.STYHZH + '\'' + "," +
              "STYHMC='" + this.STYHMC + '\'' + "," +
              "FSE='" + this.FSE + '\'' + "," +
              "YWWD='" + this.YWWD + '\'' + "," +
              "ZSYE='" + this.ZSYE + '\'' + "," +
              "JZNY='" + this.JZNY + '\'' + "," +
              "STYHDM='" + this.STYHDM + '\'' + "," +
              "HJFS='" + this.HJFS + '\'' + "," +
              "DWFCRS='" + this.DWFCRS + '\'' + "," +
              "DWMC='" + this.DWMC + '\'' + "," +
              "QCQRDH='" + this.QCQRDH + '\'' + "," +
              "DWZH='" + this.DWZH + '\'' + "," +
              "DWJCRS='" + this.DWJCRS + '\'' + "," +
              "BLZL='" + this.BLZL + '\'' + "," +
              "CZY='" + this.CZY + '\'' + "," +
              "JBRZJLX='" + this.JBRZJLX + '\'' + "," +
              "YWLSH='" + this.YWLSH + '\'' + "," +
              "JBRXM='" + this.JBRXM + '\'' + "," +
              "QCQR='" + this.QCQR + '\'' + "," +
              "DWZHYE='" + this.DWZHYE + '\'' + "," +
              "JXRQ='" + this.JXRQ + '\'' + "," +
              "FSRS='" + this.FSRS + '\'' + "," +
              "JBRZJHM='" + this.JBRZJHM + '\'' + "," +
              "HBJNY='" + this.HBJNY + '\'' + "," +
              "DWYHJNY='" + this.DWYHJNY + '\'' + "," +
              "STYHHM='" + this.STYHHM + '\'' +

      "}";

  } 
}