package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "GetUnitPayBackResBJHJ")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetUnitPayBackResBJHJ  implements Serializable{

   private  String    DWBJEHJ;  //单位补缴额合计

   private  String    FSEHJ;  //发生额合计

   private  String    GRBJEHJ;  //个人补缴额合计

   public  String getDWBJEHJ(){

       return this.DWBJEHJ;   

   }


   public  void setDWBJEHJ(String  DWBJEHJ){

       this.DWBJEHJ = DWBJEHJ;   

   }


   public  String getFSEHJ(){

       return this.FSEHJ;   

   }


   public  void setFSEHJ(String  FSEHJ){

       this.FSEHJ = FSEHJ;   

   }


   public  String getGRBJEHJ(){

       return this.GRBJEHJ;   

   }


   public  void setGRBJEHJ(String  GRBJEHJ){

       this.GRBJEHJ = GRBJEHJ;   

   }


   public String toString(){ 

       return "GetUnitPayBackResBJHJ{" +  
 
              "DWBJEHJ='" + this.DWBJEHJ + '\'' + "," +
              "FSEHJ='" + this.FSEHJ + '\'' + "," +
              "GRBJEHJ='" + this.GRBJEHJ + '\'' + 

      "}";

  } 
}