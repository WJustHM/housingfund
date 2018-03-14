package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "IndiAcctSetPostDWXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class IndiAcctSetPostDWXX  implements Serializable{

   private  String    DWZH;  //单位账号 

   public  String getDWZH(){ 

       return this.DWZH;   

   }


   public  void setDWZH(String  DWZH){ 

       this.DWZH = DWZH;   

   }


   public String toString(){ 

       return "IndiAcctSetPostDWXX{" +  
 
              "DWZH='" + this.DWZH + '\'' + 

      "}";

  } 
}