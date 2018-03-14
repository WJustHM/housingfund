package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "AddIndiAcctSetRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class AddIndiAcctSetRes  implements Serializable{

   private  String    Status;  //状态（成功:success/失败：fail） 

   private  String    YWLSH;  //业务流水号 

   private  String    GRZH;  //个人账号 

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


   public  String getGRZH(){ 

       return this.GRZH;   

   }


   public  void setGRZH(String  GRZH){ 

       this.GRZH = GRZH;   

   }


   public String toString(){ 

       return "AddIndiAcctSetRes{" +  
 
              "Status='" + this.Status + '\'' + "," +
              "YWLSH='" + this.YWLSH + '\'' + "," +
              "GRZH='" + this.GRZH + '\'' + 

      "}";

  } 
}