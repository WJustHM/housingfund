package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "ListUnitDepositRatioRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListUnitDepositRatioRes  implements Serializable{

   private  ArrayList<ListUnitDepositRatioResRes>    Res;  // 

   public  ArrayList<ListUnitDepositRatioResRes> getRes(){ 

       return this.Res;   

   }


   public  void setRes(ArrayList<ListUnitDepositRatioResRes>  Res){ 

       this.Res = Res;   

   }


   public String toString(){ 

       return "ListUnitDepositRatioRes{" +  
 
              "Res='" + this.Res + '\'' + 

      "}";

  } 
}