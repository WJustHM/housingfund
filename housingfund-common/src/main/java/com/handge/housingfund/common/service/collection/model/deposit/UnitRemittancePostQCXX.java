package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "UnitRemittancePostQCXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnitRemittancePostQCXX  implements Serializable{

   private  String    GRJCJS;  //个人缴存基数

   private  String    DWYJCE;  //单位月缴存额

   private  String    GRZH;  //个人账号 

   private  String    GRYJCE;  //个人月缴存额

   public  String getGRJCJS(){

       return this.GRJCJS;   

   }


   public  void setGRJCJS(String  GRJCJS){

       this.GRJCJS = GRJCJS;   

   }


   public  String getDWYJCE(){

       return this.DWYJCE;   

   }


   public  void setDWYJCE(String  DWYJCE){

       this.DWYJCE = DWYJCE;   

   }


   public  String getGRZH(){ 

       return this.GRZH;   

   }


   public  void setGRZH(String  GRZH){ 

       this.GRZH = GRZH;   

   }


   public  String getGRYJCE(){

       return this.GRYJCE;   

   }


   public  void setGRYJCE(String  GRYJCE){

       this.GRYJCE = GRYJCE;   

   }


   public String toString(){ 

       return "UnitRemittancePostQCXX{" +  
 
              "GRJCJS='" + this.GRJCJS + '\'' + "," +
              "DWYJCE='" + this.DWYJCE + '\'' + "," +
              "GRZH='" + this.GRZH + '\'' + "," +
              "GRYJCE='" + this.GRYJCE + '\'' + 

      "}";

  } 
}