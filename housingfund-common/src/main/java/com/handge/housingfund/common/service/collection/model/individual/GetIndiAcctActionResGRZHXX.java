package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "GetIndiAcctActionResGRZHXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetIndiAcctActionResGRZHXX  implements Serializable{

   private  String    GRJCJS;  //个人缴存基数

   private  String    GRZHSNJZYE;  //个人账户上年结转余额

   private  String    GRZHYE;  //个人账户余额

   private  String    GRYJCE;  //个人月缴存额

   private  String    ZJLX;  //证件类型 

   private  String    YJCE;  //月缴存额

   private  String    SXNY;  //生效年月 

   private  String    GRZH;  //个人账号 

   private  String    CZMC;  //操作名称(05:封存;04:启封；14:冻结;15:解冻) 

   private  String    GRJCBL;  //个人缴存比例

   private  String    DWYJCE;  //单位月缴存额

   private  String    GRJZNY;  //个人缴至年月 

   private  String    ZJHM;  //证件号码 

   private  String    BeiZhu;  //备注 

   private  String    GRZHDNGJYE;  //个人账户当年归集余额

   private  String    GRZHZT;  //个人账户状态 

   private  String    DWJCBL;  //单位缴存比例

   private  String    CZYY;  //操作原因 

   private  String    XingMing;  //姓名 

   private  String    DJJE;  //冻结金额（冻结/解冻）

   public  String getGRJCJS(){

       return this.GRJCJS;   

   }


   public  void setGRJCJS(String  GRJCJS){

       this.GRJCJS = GRJCJS;   

   }


   public  String getGRZHSNJZYE(){

       return this.GRZHSNJZYE;   

   }


   public  void setGRZHSNJZYE(String  GRZHSNJZYE){

       this.GRZHSNJZYE = GRZHSNJZYE;   

   }


   public  String getGRZHYE(){

       return this.GRZHYE;   

   }


   public  void setGRZHYE(String  GRZHYE){

       this.GRZHYE = GRZHYE;   

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


   public  String getYJCE(){

       return this.YJCE;   

   }


   public  void setYJCE(String  YJCE){

       this.YJCE = YJCE;   

   }


   public  String getSXNY(){ 

       return this.SXNY;   

   }


   public  void setSXNY(String  SXNY){ 

       this.SXNY = SXNY;   

   }


   public  String getGRZH(){ 

       return this.GRZH;   

   }


   public  void setGRZH(String  GRZH){ 

       this.GRZH = GRZH;   

   }


   public  String getCZMC(){ 

       return this.CZMC;   

   }


   public  void setCZMC(String  CZMC){ 

       this.CZMC = CZMC;   

   }


   public  String getGRJCBL(){

       return this.GRJCBL;   

   }


   public  void setGRJCBL(String  GRJCBL){

       this.GRJCBL = GRJCBL;   

   }


   public  String getDWYJCE(){

       return this.DWYJCE;   

   }


   public  void setDWYJCE(String  DWYJCE){

       this.DWYJCE = DWYJCE;   

   }


   public  String getGRJZNY(){ 

       return this.GRJZNY;   

   }


   public  void setGRJZNY(String  GRJZNY){ 

       this.GRJZNY = GRJZNY;   

   }


   public  String getZJHM(){ 

       return this.ZJHM;   

   }


   public  void setZJHM(String  ZJHM){ 

       this.ZJHM = ZJHM;   

   }


   public  String getBeiZhu(){ 

       return this.BeiZhu;   

   }


   public  void setBeiZhu(String  BeiZhu){ 

       this.BeiZhu = BeiZhu;   

   }


   public  String getGRZHDNGJYE(){

       return this.GRZHDNGJYE;   

   }


   public  void setGRZHDNGJYE(String  GRZHDNGJYE){

       this.GRZHDNGJYE = GRZHDNGJYE;   

   }


   public  String getGRZHZT(){ 

       return this.GRZHZT;   

   }


   public  void setGRZHZT(String  GRZHZT){ 

       this.GRZHZT = GRZHZT;   

   }


   public  String getDWJCBL(){

       return this.DWJCBL;   

   }


   public  void setDWJCBL(String  DWJCBL){

       this.DWJCBL = DWJCBL;   

   }


   public  String getCZYY(){ 

       return this.CZYY;   

   }


   public  void setCZYY(String  CZYY){ 

       this.CZYY = CZYY;   

   }


   public  String getXingMing(){ 

       return this.XingMing;   

   }


   public  void setXingMing(String  XingMing){ 

       this.XingMing = XingMing;   

   }


   public  String getDJJE(){

       return this.DJJE;   

   }


   public  void setDJJE(String  DJJE){

       this.DJJE = DJJE;   

   }


   public String toString(){ 

       return "GetIndiAcctActionResGRZHXX{" +  
 
              "GRJCJS='" + this.GRJCJS + '\'' + "," +
              "GRZHSNJZYE='" + this.GRZHSNJZYE + '\'' + "," +
              "GRZHYE='" + this.GRZHYE + '\'' + "," +
              "GRYJCE='" + this.GRYJCE + '\'' + "," +
              "ZJLX='" + this.ZJLX + '\'' + "," +
              "YJCE='" + this.YJCE + '\'' + "," +
              "SXNY='" + this.SXNY + '\'' + "," +
              "GRZH='" + this.GRZH + '\'' + "," +
              "CZMC='" + this.CZMC + '\'' + "," +
              "GRJCBL='" + this.GRJCBL + '\'' + "," +
              "DWYJCE='" + this.DWYJCE + '\'' + "," +
              "GRJZNY='" + this.GRJZNY + '\'' + "," +
              "ZJHM='" + this.ZJHM + '\'' + "," +
              "BeiZhu='" + this.BeiZhu + '\'' + "," +
              "GRZHDNGJYE='" + this.GRZHDNGJYE + '\'' + "," +
              "GRZHZT='" + this.GRZHZT + '\'' + "," +
              "DWJCBL='" + this.DWJCBL + '\'' + "," +
              "CZYY='" + this.CZYY + '\'' + "," +
              "XingMing='" + this.XingMing + '\'' + "," +
              "DJJE='" + this.DJJE + '\'' + 

      "}";

  } 
}