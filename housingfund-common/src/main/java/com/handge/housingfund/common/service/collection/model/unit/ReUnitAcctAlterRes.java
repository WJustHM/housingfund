package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "ReUnitAcctAlterRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReUnitAcctAlterRes  implements Serializable{

   private  String    Status;  //状态（成功：success；失败：fail） 

   private  String    YWLSH;  //业务流水号 

   public  String getStatus(){ 

       return this.Status;   

   }


   public  void setStatus(String  Status){ 

       this.Status = Status;   

   }


   public  String getYWLSH(){ 

       return this.YWLSH;   

   }


   public  void setYWLSH(String  YWLSH){ 

       this.YWLSH = YWLSH;   

   }


   public String toString(){ 

       return "ReUnitAcctAlterRes{" +  
 
              "Status='" + this.Status + '\'' + "," +
              "YWLSH='" + this.YWLSH + '\'' + 

      "}";

  } 
}