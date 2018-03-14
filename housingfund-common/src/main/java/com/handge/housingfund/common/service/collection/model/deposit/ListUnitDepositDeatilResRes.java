package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "ListUnitDepositDeatilResRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListUnitDepositDeatilResRes implements Serializable{

   private  String    SLSJ;  //受理时间 

   private  String    YWLSH;  //业务流水号 

   private  String    FSE;  //发生额

   private  String    JBR;  //经办人 

   private  Integer    FSRS;  //发生人数 

   private  String    YWWD;  //业务网点 

   private  String    YWLX;  //业务类型 

   private  String    HBJNY;  //汇补缴年月 

   private  String    CZY;  //操作员

   private  String    HJFS;  //汇缴方式

    public String getHJFS() {
        return HJFS;
    }

    public void setHJFS(String HJFS) {
        this.HJFS = HJFS;
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


   public  String getJBR(){ 

       return this.JBR;   

   }


   public  void setJBR(String  JBR){ 

       this.JBR = JBR;   

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


   public  String getYWLX(){ 

       return this.YWLX;   

   }


   public  void setYWLX(String  YWLX){ 

       this.YWLX = YWLX;   

   }


   public  String getHBJNY(){ 

       return this.HBJNY;   

   }


   public  void setHBJNY(String  HBJNY){ 

       this.HBJNY = HBJNY;   

   }


   public  String getCZY(){ 

       return this.CZY;   

   }


   public  void setCZY(String  CZY){ 

       this.CZY = CZY;   

   }


    @Override
    public String toString() {
        return "ListUnitDepositDeatilResRes{" +
                "SLSJ='" + SLSJ + '\'' +
                ", YWLSH='" + YWLSH + '\'' +
                ", FSE=" + FSE +
                ", JBR='" + JBR + '\'' +
                ", FSRS=" + FSRS +
                ", YWWD='" + YWWD + '\'' +
                ", YWLX='" + YWLX + '\'' +
                ", HBJNY='" + HBJNY + '\'' +
                ", CZY='" + CZY + '\'' +
                ", HJFS='" + HJFS + '\'' +
                '}';
    }
}