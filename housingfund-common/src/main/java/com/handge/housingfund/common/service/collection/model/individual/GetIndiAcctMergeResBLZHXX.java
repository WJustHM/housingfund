package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "GetIndiAcctMergeResBLZHXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetIndiAcctMergeResBLZHXX  implements Serializable{

   private  String    HuMing;  //户名 

   private  String    JZNY;  //缴至年月 

   private  String    ZHJX;  //账号结息 

   private  String    ZHZT;  //账户状态 

   private  String    YHZH;  //银行账号 

   private  String    ZHBJ;  //账号本金

   private  String    DWMC;  //单位名称 

   private  String    ZHYEHJ;  //账号余额合计

   private  String    DWZH;  //单位账号 

   private  String    GRZH;  //个人账号 

   public  String getHuMing(){ 

       return this.HuMing;   

   }


   public  void setHuMing(String  HuMing){ 

       this.HuMing = HuMing;   

   }


   public  String getJZNY(){ 

       return this.JZNY;   

   }


   public  void setJZNY(String  JZNY){ 

       this.JZNY = JZNY;   

   }


   public  String getZHJX(){ 

       return this.ZHJX;   

   }


   public  void setZHJX(String  ZHJX){ 

       this.ZHJX = ZHJX;   

   }


   public  String getZHZT(){ 

       return this.ZHZT;   

   }


   public  void setZHZT(String  ZHZT){ 

       this.ZHZT = ZHZT;   

   }


   public  String getYHZH(){ 

       return this.YHZH;   

   }


   public  void setYHZH(String  YHZH){ 

       this.YHZH = YHZH;   

   }


   public  String getZHBJ(){

       return this.ZHBJ;   

   }


   public  void setZHBJ(String  ZHBJ){

       this.ZHBJ = ZHBJ;   

   }


   public  String getDWMC(){ 

       return this.DWMC;   

   }


   public  void setDWMC(String  DWMC){ 

       this.DWMC = DWMC;   

   }


   public  String getZHYEHJ(){

       return this.ZHYEHJ;   

   }


   public  void setZHYEHJ(String  ZHYEHJ){

       this.ZHYEHJ = ZHYEHJ;   

   }


   public  String getDWZH(){ 

       return this.DWZH;   

   }


   public  void setDWZH(String  DWZH){ 

       this.DWZH = DWZH;   

   }


   public  String getGRZH(){ 

       return this.GRZH;   

   }


   public  void setGRZH(String  GRZH){ 

       this.GRZH = GRZH;   

   }


   public String toString(){ 

       return "GetIndiAcctMergeResBLZHXX{" +  
 
              "HuMing='" + this.HuMing + '\'' + "," +
              "JZNY='" + this.JZNY + '\'' + "," +
              "ZHJX='" + this.ZHJX + '\'' + "," +
              "ZHZT='" + this.ZHZT + '\'' + "," +
              "YHZH='" + this.YHZH + '\'' + "," +
              "ZHBJ='" + this.ZHBJ + '\'' + "," +
              "DWMC='" + this.DWMC + '\'' + "," +
              "ZHYEHJ='" + this.ZHYEHJ + '\'' + "," +
              "DWZH='" + this.DWZH + '\'' + "," +
              "GRZH='" + this.GRZH + '\'' + 

      "}";

  } 
}