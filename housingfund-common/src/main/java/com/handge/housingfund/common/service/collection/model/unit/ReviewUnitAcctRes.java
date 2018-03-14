package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "ReviewUnitAcctRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReviewUnitAcctRes  implements Serializable{

   private  String    Status;  //状态（成功：success；失败：fail） 

   private  ArrayList<String>    YWLSH;  //业务流水号集 

   public  String getStatus(){ 

       return this.Status;   

   }


   public  void setStatus(String  Status){ 

       this.Status = Status;   

   }


   public  ArrayList<String> getYWLSH(){ 

       return this.YWLSH;   

   }


   public  void setYWLSH(ArrayList<String>  YWLSH){ 

       this.YWLSH = YWLSH;   

   }


   public String toString(){ 

       return "ReviewUnitAcctRes{" +  
 
              "Status='" + this.Status + '\'' + "," +
              "YWLSH='" + this.YWLSH + '\'' + 

      "}";

  } 
}