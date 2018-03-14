package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "ReviewIndiAcctRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReviewIndiAcctRes  implements Serializable{

   private  String    Status;  //状态(success\fail) 

   public  String getStatus(){ 

       return this.Status;   

   }


   public  void setStatus(String  Status){ 

       this.Status = Status;   

   }


   public String toString(){ 

       return "ReviewIndiAcctRes{" +  
 
              "Status='" + this.Status + '\'' + 

      "}";

  } 
}