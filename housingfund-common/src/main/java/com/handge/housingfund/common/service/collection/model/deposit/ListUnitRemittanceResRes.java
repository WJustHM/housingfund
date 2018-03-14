package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "ListUnitRemittanceResRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListUnitRemittanceResRes  implements Serializable{

   private  String id;

   private  String    SLSJ;  //受理时间 

   private  String    YWLSH;  //业务流水号 

   private  String    FSE;  //发生额

   private  Integer    FSRS;  //发生人数 

   private  String    YWWD;  //业务网点 

   private  String    HBJNY;  //汇补缴年月 

   private  String    DWMC;  //单位名称 

   private  String    DWZH;  //单位账号 

   private  String    CZY;  //操作员 

   private  String    YWZT;  //业务状态 

   private  String   JZPZH;  //凭证号

   private String SBYY; //失败原因

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public  String getSLSJ(){

       return this.SLSJ;   

   }


   public  void setSLSJ(String  SLSJ){ 

       this.SLSJ = SLSJ;   

   }


   public  String getYWLSH(){ 

       return this.YWLSH;   

   }


   public  void setYWLSH(String  YWLSH){ 

       this.YWLSH = YWLSH;   

   }


   public  String getFSE(){

       return this.FSE;   

   }


   public  void setFSE(String  FSE){

       this.FSE = FSE;   

   }


   public  Integer getFSRS(){ 

       return this.FSRS;   

   }


   public  void setFSRS(Integer  FSRS){ 

       this.FSRS = FSRS;   

   }


   public  String getYWWD(){ 

       return this.YWWD;   

   }


   public  void setYWWD(String  YWWD){ 

       this.YWWD = YWWD;   

   }


   public  String getHBJNY(){ 

       return this.HBJNY;   

   }


   public  void setHBJNY(String  HBJNY){ 

       this.HBJNY = HBJNY;   

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


   public  String getCZY(){ 

       return this.CZY;   

   }


   public  void setCZY(String  CZY){ 

       this.CZY = CZY;   

   }


   public  String getYWZT(){ 

       return this.YWZT;   

   }


   public  void setYWZT(String  YWZT){ 

       this.YWZT = YWZT;   

   }


   public  String getJZPZH(){

       return this.JZPZH;

   }


   public  void setJZPZH(String JZPZH){

       this.JZPZH = JZPZH;

   }

    public String getSBYY() {
        return SBYY;
    }

    public void setSBYY(String SBYY) {
        this.SBYY = SBYY;
    }

    public String toString(){

       return "ListUnitRemittanceResRes{" +  
 
              "SLSJ='" + this.SLSJ + '\'' + "," +
              "YWLSH='" + this.YWLSH + '\'' + "," +
              "FSE='" + this.FSE + '\'' + "," +
              "FSRS='" + this.FSRS + '\'' + "," +
              "YWWD='" + this.YWWD + '\'' + "," +
              "HBJNY='" + this.HBJNY + '\'' + "," +
              "DWMC='" + this.DWMC + '\'' + "," +
              "DWZH='" + this.DWZH + '\'' + "," +
              "CZY='" + this.CZY + '\'' + "," +
              "YWZT='" + this.YWZT + '\'' + "," +
              "JZPZH='" + this.JZPZH + '\'' +

      "}";

  } 
}