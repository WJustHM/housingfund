package com.handge.housingfund.common.service.collection.model.deposit;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "GetUnitDepositDetailRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetUnitDepositDetailRes  implements Serializable{

   private  String    DWZHYE;  //单位账户余额

   private  String    ZSYE;  //暂收余额

   private  String    GRJCBL;  //个人缴存比例

   private  String    ZZJGDM;  //组织机构代码 

   private  String    JZNY;  //缴至年月 

   private  String    DWFCRS;  //单位封存人数

   private  String    DWJCBL;  //单位缴存比例

   private  String    DWMC;  //单位名称 

   private  String    DWYHJNY;  //单位应汇缴年月 

   private  String    DWZH;  //单位账号

   private  String    DWJCRS;  //单位缴存人数

   public  String getDWZHYE(){

       return this.DWZHYE;   

   }


   public  void setDWZHYE(String  DWZHYE){

       this.DWZHYE = DWZHYE;   

   }


   public  String getZSYE(){

       return this.ZSYE;   

   }


   public  void setZSYE(String  ZSYE){

       this.ZSYE = ZSYE;   

   }


   public  String getGRJCBL(){

       return this.GRJCBL;   

   }


   public  void setGRJCBL(String  GRJCBL){

       this.GRJCBL = GRJCBL;   

   }


   public  String getZZJGDM(){ 

       return this.ZZJGDM;   

   }


   public  void setZZJGDM(String  ZZJGDM){ 

       this.ZZJGDM = ZZJGDM;   

   }


   public  String getJZNY(){ 

       return this.JZNY;   

   }


   public  void setJZNY(String  JZNY){ 

       this.JZNY = JZNY;   

   }


   public String getDWFCRS(){

       return this.DWFCRS;   

   }


   public  void setDWFCRS(String  DWFCRS){

       this.DWFCRS = DWFCRS;   

   }


   public  String getDWJCBL(){

       return this.DWJCBL;   

   }


   public  void setDWJCBL(String  DWJCBL){

       this.DWJCBL = DWJCBL;   

   }


   public  String getDWMC(){ 

       return this.DWMC;   

   }


   public  void setDWMC(String  DWMC){ 

       this.DWMC = DWMC;   

   }


   public  String getDWYHJNY(){ 

       return this.DWYHJNY;   

   }


   public  void setDWYHJNY(String  DWYHJNY){ 

       this.DWYHJNY = DWYHJNY;   

   }


   public  String getDWZH(){ 

       return this.DWZH;   

   }


   public  void setDWZH(String  DWZH){ 

       this.DWZH = DWZH;   

   }


   public String getDWJCRS(){

       return this.DWJCRS;   

   }


   public  void setDWJCRS(String  DWJCRS){

       this.DWJCRS = DWJCRS;   

   }


   public String toString(){ 

       return "GetUnitDepositDetailRes{" +  
 
              "DWZHYE='" + this.DWZHYE + '\'' + "," +
              "ZSYE='" + this.ZSYE + '\'' + "," +
              "GRJCBL='" + this.GRJCBL + '\'' + "," +
              "ZZJGDM='" + this.ZZJGDM + '\'' + "," +
              "JZNY='" + this.JZNY + '\'' + "," +
              "DWFCRS='" + this.DWFCRS + '\'' + "," +
              "DWJCBL='" + this.DWJCBL + '\'' + "," +
              "DWMC='" + this.DWMC + '\'' + "," +
              "DWYHJNY='" + this.DWYHJNY + '\'' + "," +
              "DWZH='" + this.DWZH + '\'' + "," +
              "DWJCRS='" + this.DWJCRS + '\'' +

      "}";

  } 
}