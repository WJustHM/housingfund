package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "ListPersonRadixResRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListPersonRadixResRes  implements Serializable{

    private static final long serialVersionUID = 3595385280320292773L;
    private  String    JZNY;  //缴至年月

   private  String    SLSJ;  //受理时间 

   private  String    YWLSH;  //业务流水号 

   private  String    YWWD;  //业务网点 

   private  String    YWZT;  //业务状态 

   private  String    DWMC;  //单位名称 

   private  String    DWZH;  //单位账号 

   private  String    CZY;  //操作员

    private String  id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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


   public  String getYWLSH(){ 

       return this.YWLSH;   

   }


   public  void setYWLSH(String  YWLSH){ 

       this.YWLSH = YWLSH;   

   }


   public  String getYWWD(){ 

       return this.YWWD;   

   }


   public  void setYWWD(String  YWWD){ 

       this.YWWD = YWWD;   

   }


   public  String getYWZT(){ 

       return this.YWZT;   

   }


   public  void setYWZT(String  YWZT){ 

       this.YWZT = YWZT;   

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


   public String toString(){ 

       return "ListPersonRadixResRes{" +  
 
              "JZNY='" + this.JZNY + '\'' + "," +
              "SLSJ='" + this.SLSJ + '\'' + "," +
              "YWLSH='" + this.YWLSH + '\'' + "," +
              "YWWD='" + this.YWWD + '\'' + "," +
              "YWZT='" + this.YWZT + '\'' + "," +
              "DWMC='" + this.DWMC + '\'' + "," +
              "DWZH='" + this.DWZH + '\'' + "," +
              "CZY='" + this.CZY + '\'' + 

      "}";

  } 
}