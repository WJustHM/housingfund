package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "ListUnitDepositRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListUnitDepositRes  implements Serializable{

   private  ArrayList<ListUnitDepositResRes>    Res;  // 

   public  ArrayList<ListUnitDepositResRes> getRes(){ 

       return this.Res;   

   }


   public  void setRes(ArrayList<ListUnitDepositResRes>  Res){ 

       this.Res = Res;   

   }


   public String toString(){ 

       return "ListUnitDepositRes{" +  
 
              "Res='" + this.Res + '\'' + 

      "}";

  } 
}