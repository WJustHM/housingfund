package com.handge.housingfund.common.service.collection.model.withdrawlModel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "Error")
@XmlAccessorType(XmlAccessType.FIELD)
public class Error implements Serializable {

   private  String    Code;  //状态码 

   private  String    Msg;  //信息 

   public  String getCode(){ 

       return this.Code;   

   }


   public  void setCode(String  Code){ 

       this.Code = Code;   

   }


   public  String getMsg(){ 

       return this.Msg;   

   }


   public  void setMsg(String  Msg){ 

       this.Msg = Msg;   

   }


   public String toString(){ 

       return "Error{" +  
 
              "Code='" + this.Code + '\'' + "," +
              "Msg='" + this.Msg + '\'' +

      "}";

  } 
}