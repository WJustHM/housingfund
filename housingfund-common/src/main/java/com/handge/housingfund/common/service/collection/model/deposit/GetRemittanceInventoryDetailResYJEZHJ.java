package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "GetRemittanceInventoryDetailResYJEZHJ")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetRemittanceInventoryDetailResYJEZHJ  implements Serializable{

   private  String    DWYJCEZHJ;  //单位月缴存额合计

   private  String    GRYJCEZHJ;  //个人月缴存额合计

   private  String    ZHJ;  //总合计

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


   public  String getZHJ(){

       return this.ZHJ;   

   }


   public  void setZHJ(String  ZHJ){

       this.ZHJ = ZHJ;   

   }


   public String toString(){ 

       return "GetRemittanceInventoryDetailResYJEZHJ{" +  
 
              "DWYJCEZHJ='" + this.DWYJCEZHJ + '\'' + "," +
              "GRYJCEZHJ='" + this.GRYJCEZHJ + '\'' + "," +
              "ZHJ='" + this.ZHJ + '\'' + 

      "}";

  } 
}