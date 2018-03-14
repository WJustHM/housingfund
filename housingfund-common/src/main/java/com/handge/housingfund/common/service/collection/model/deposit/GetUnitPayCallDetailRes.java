package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "GetUnitPayCallDetailRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetUnitPayCallDetailRes  implements Serializable{

   private  String    CJR;  //催缴人 

   private  String    YWLSH;  //业务流水号 

   private  String    DWZH;  //单位账号 

   private  String    DWMC;  //单位名称 

   private  String    CJSJ;  //催缴时间 

   public  String getCJR(){ 

       return this.CJR;   

   }


   public  void setCJR(String  CJR){ 

       this.CJR = CJR;   

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


   public  String getCJSJ(){ 

       return this.CJSJ;   

   }


   public  void setCJSJ(String  CJSJ){ 

       this.CJSJ = CJSJ;   

   }


   public String toString(){ 

       return "GetUnitPayCallDetailRes{" +  
 
              "CJR='" + this.CJR + '\'' + "," +
              "YWLSH='" + this.YWLSH + '\'' + "," +
              "DWZH='" + this.DWZH + '\'' + "," +
              "DWMC='" + this.DWMC + '\'' + "," +
              "CJSJ='" + this.CJSJ + '\'' + 

      "}";

  } 
}