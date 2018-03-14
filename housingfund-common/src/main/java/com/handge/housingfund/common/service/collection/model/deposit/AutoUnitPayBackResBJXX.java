package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "AutoUnitPayBackResBJXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class AutoUnitPayBackResBJXX  implements Serializable{

   private  String    GRBJE;  //个人补缴额

   private  String    GRZH;  //个人账号 

   private  String    FSE;  //发生额

   private  String    ZJHM;  //证件号码 

   private  String    DWBJE;  //单位补缴额

   private  String    XingMing;  //姓名 

   public  String getGRBJE(){

       return this.GRBJE;   

   }


   public  void setGRBJE(String  GRBJE){

       this.GRBJE = GRBJE;   

   }


   public  String getGRZH(){ 

       return this.GRZH;   

   }


   public  void setGRZH(String  GRZH){ 

       this.GRZH = GRZH;   

   }


   public  String getFSE(){

       return this.FSE;   

   }


   public  void setFSE(String  FSE){

       this.FSE = FSE;   

   }


   public  String getZJHM(){ 

       return this.ZJHM;   

   }


   public  void setZJHM(String  ZJHM){ 

       this.ZJHM = ZJHM;   

   }


   public  String getDWBJE(){

       return this.DWBJE;   

   }


   public  void setDWBJE(String  DWBJE){

       this.DWBJE = DWBJE;   

   }


   public  String getXingMing(){ 

       return this.XingMing;   

   }


   public  void setXingMing(String  XingMing){ 

       this.XingMing = XingMing;   

   }


   public String toString(){ 

       return "AutoUnitPayBackResBJXX{" +  
 
              "GRBJE='" + this.GRBJE + '\'' + "," +
              "GRZH='" + this.GRZH + '\'' + "," +
              "FSE='" + this.FSE + '\'' + "," +
              "ZJHM='" + this.ZJHM + '\'' + "," +
              "DWBJE='" + this.DWBJE + '\'' + "," +
              "XingMing='" + this.XingMing + '\'' + 

      "}";

  } 
}