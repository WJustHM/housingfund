package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "ListOperationAcctsResRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListOperationAcctsResRes  implements Serializable{
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
   private  String    GRJCJS;  //个人缴存基数

   private  String    Xingming;  //姓名 

   private  String    YWLSH;  //业务流水号 

   private  String    GRZHYE;  //个人账户余额（合并、封存、启封、冻结、解冻）

   private  String    YJCE;  //月缴存额（开户）

   private  String    GRZH;  //个人账号(开户时无此项) 

   private  String    JZNY;  //缴至年月（合并、封存、启封、冻结、解冻） 

   private  String    SLSJ;  //受理时间 

   private  String    ZhuangTai;  //状态 

   private  String    ZJHM;  //证件号码 

   private  String    GRZHZT;  //个人账户状态 

   private  String    DWMC;  //单位名称 

   private  String    CZYY;  //操作原因（封存、启封、冻结、解冻） 
    private String    YWWD; //业务网点
    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }


   public  String getGRJCJS(){

       return this.GRJCJS;   

   }


   public  void setGRJCJS(String  GRJCJS){

       this.GRJCJS = GRJCJS;   

   }


   public  String getXingming(){ 

       return this.Xingming;   

   }


   public  void setXingming(String  Xingming){ 

       this.Xingming = Xingming;   

   }


   public  String getYWLSH(){ 

       return this.YWLSH;   

   }


   public  void setYWLSH(String  YWLSH){ 

       this.YWLSH = YWLSH;   

   }


   public  String getGRZHYE(){

       return this.GRZHYE;   

   }


   public  void setGRZHYE(String GRZHYE){

       this.GRZHYE = GRZHYE;   

   }


   public  String getYJCE(){

       return this.YJCE;   

   }


   public  void setYJCE(String  YJCE){

       this.YJCE = YJCE;   

   }


   public  String getGRZH(){ 

       return this.GRZH;   

   }


   public  void setGRZH(String  GRZH){ 

       this.GRZH = GRZH;   

   }


   public  String getJZNY(){ 

       return this.JZNY;   

   }


   public  void setJZNY(String  JZNY){ 

       this.JZNY = JZNY;   

   }


   public  String getSLSJ(){ 

       return this.SLSJ;   

   }


   public  void setSLSJ(String  SLSJ){ 

       this.SLSJ = SLSJ;   

   }


   public  String getZhuangTai(){ 

       return this.ZhuangTai;   

   }


   public  void setZhuangTai(String  ZhuangTai){ 

       this.ZhuangTai = ZhuangTai;   

   }


   public  String getZJHM(){ 

       return this.ZJHM;   

   }


   public  void setZJHM(String  ZJHM){ 

       this.ZJHM = ZJHM;   

   }


   public  String getGRZHZT(){ 

       return this.GRZHZT;   

   }


   public  void setGRZHZT(String  GRZHZT){ 

       this.GRZHZT = GRZHZT;   

   }


   public  String getDWMC(){ 

       return this.DWMC;   

   }


   public  void setDWMC(String  DWMC){ 

       this.DWMC = DWMC;   

   }


   public  String getCZYY(){ 

       return this.CZYY;   

   }


   public  void setCZYY(String  CZYY){ 

       this.CZYY = CZYY;   

   }


   public String toString(){ 

       return "ListOperationAcctsResRes{" +  
 
              "GRJCJS='" + this.GRJCJS + '\'' + "," +
              "Xingming='" + this.Xingming + '\'' + "," +
              "YWLSH='" + this.YWLSH + '\'' + "," +
              "GRZHYE='" + this.GRZHYE + '\'' + "," +
              "YJCE='" + this.YJCE + '\'' + "," +
              "GRZH='" + this.GRZH + '\'' + "," +
              "JZNY='" + this.JZNY + '\'' + "," +
              "SLSJ='" + this.SLSJ + '\'' + "," +
              "ZhuangTai='" + this.ZhuangTai + '\'' + "," +
              "ZJHM='" + this.ZJHM + '\'' + "," +
              "GRZHZT='" + this.GRZHZT + '\'' + "," +
              "DWMC='" + this.DWMC + '\'' + "," +
              "CZYY='" + this.CZYY + '\'' + 

      "}";

  } 
}