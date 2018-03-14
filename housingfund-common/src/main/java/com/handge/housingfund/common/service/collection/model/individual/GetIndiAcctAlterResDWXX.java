package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "GetIndiAcctAlterResDWXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetIndiAcctAlterResDWXX  implements Serializable{

   private  String    JZNY;  //缴至年月 

   private  String    YWLSH;  //业务流水号 

   private  String    DWZH;  //单位账号 

   private  String    DWMC;  //单位名称 

   public  String getJZNY(){ 

       return this.JZNY;   

   }


   public  void setJZNY(String  JZNY){ 

       this.JZNY = JZNY;   

   }


   public  String getYWLSH(){ 

       return this.YWLSH;   

   }


   public  void setYWLSH(String  YWLSH){ 

       this.YWLSH = YWLSH;   

   }


   public  String getDWZH(){ 

       return this.DWZH;   

   }


   public  void setDWZH(String  DWZH){ 

       this.DWZH = DWZH;   

   }


   public  String getDWMC(){ 

       return this.DWMC;   

   }


   public  void setDWMC(String  DWMC){ 

       this.DWMC = DWMC;   

   }


   public String toString(){ 

       return "GetIndiAcctAlterResDWXX{" +  
 
              "JZNY='" + this.JZNY + '\'' + "," +
              "YWLSH='" + this.YWLSH + '\'' + "," +
              "DWZH='" + this.DWZH + '\'' + "," +
              "DWMC='" + this.DWMC + '\'' + 

      "}";

  } 
}