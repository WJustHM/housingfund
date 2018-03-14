package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "ListPayBackRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListPayBackRes  implements Serializable{

   private  ArrayList<ListPayBackResRes>    Res;  // 

   public  ArrayList<ListPayBackResRes> getRes(){ 

       return this.Res;   

   }


   public  void setRes(ArrayList<ListPayBackResRes>  Res){ 

       this.Res = Res;   

   }


   public String toString(){ 

       return "ListPayBackRes{" +  
 
              "Res='" + this.Res + '\'' + 

      "}";

  } 
}