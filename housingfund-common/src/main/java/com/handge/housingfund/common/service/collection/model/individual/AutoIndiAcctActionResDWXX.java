package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "AutoIndiAcctActionResDWXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class AutoIndiAcctActionResDWXX  implements Serializable{

   private  String    JZNY;  //缴至年月 

   private  String    DWMC;  //单位名称 

   private  String    DWZH;  //单位账号 

   public  String getJZNY(){ 

       return this.JZNY;   

   }


   public  void setJZNY(String  JZNY){ 

       this.JZNY = JZNY;   

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


   public String toString(){ 

       return "AutoIndiAcctActionResDWXX{" +  
 
              "JZNY='" + this.JZNY + '\'' + "," +
              "DWMC='" + this.DWMC + '\'' + "," +
              "DWZH='" + this.DWZH + '\'' + 

      "}";

  } 
}