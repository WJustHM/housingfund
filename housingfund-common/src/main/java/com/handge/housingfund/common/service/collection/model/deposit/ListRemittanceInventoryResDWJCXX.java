package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "ListRemittanceInventoryResDWJCXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListRemittanceInventoryResDWJCXX  implements Serializable{

   private  String    FSE;  //发生额 

   private  String    DWYJCEZHJ;  //单位月缴存额合计

   private  String    GRYJCEZHJ;  //个人月缴存额合计

   private  String    FSRS;  //发生人数 

   private  String    HBJNY;  //汇补缴年月 

   private  String    QCQRDH;  //清册确认单号 

   private  String    ZHJ;  //总合计

   public  String getFSE(){ 

       return this.FSE;   

   }


   public  void setFSE(String  FSE){ 

       this.FSE = FSE;   

   }


   public  String getDWYJCEZHJ(){

       return this.DWYJCEZHJ;   

   }


   public  void setDWYJCEZHJ(String  DWYJCEZHJ){

       this.DWYJCEZHJ = DWYJCEZHJ;   

   }


   public  String getGRYJCEZHJ(){

       return this.GRYJCEZHJ;   

   }


   public  void setGRYJCEZHJ(String  GRYJCEZHJ){

       this.GRYJCEZHJ = GRYJCEZHJ;   

   }


   public  String getFSRS(){ 

       return this.FSRS;   

   }


   public  void setFSRS(String  FSRS){ 

       this.FSRS = FSRS;   

   }


   public  String getHBJNY(){ 

       return this.HBJNY;   

   }


   public  void setHBJNY(String  HBJNY){ 

       this.HBJNY = HBJNY;   

   }


   public  String getQCQRDH(){ 

       return this.QCQRDH;   

   }


   public  void setQCQRDH(String  QCQRDH){ 

       this.QCQRDH = QCQRDH;   

   }


   public  String getZHJ(){

       return this.ZHJ;   

   }


   public  void setZHJ(String  ZHJ){

       this.ZHJ = ZHJ;   

   }


   public String toString(){ 

       return "ListRemittanceInventoryResDWJCXX{" +  
 
              "FSE='" + this.FSE + '\'' + "," +
              "DWYJCEZHJ='" + this.DWYJCEZHJ + '\'' + "," +
              "GRYJCEZHJ='" + this.GRYJCEZHJ + '\'' + "," +
              "FSRS='" + this.FSRS + '\'' + "," +
              "HBJNY='" + this.HBJNY + '\'' + "," +
              "QCQRDH='" + this.QCQRDH + '\'' + "," +
              "ZHJ='" + this.ZHJ + '\'' + 

      "}";

  } 
}