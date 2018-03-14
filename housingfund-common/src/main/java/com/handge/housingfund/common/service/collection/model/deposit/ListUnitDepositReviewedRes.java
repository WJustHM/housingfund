package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "ListUnitDepositReviewedRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListUnitDepositReviewedRes  implements Serializable{

   private  ArrayList<ListUnitDepositReviewedResRes>    Res;  // 

   public  ArrayList<ListUnitDepositReviewedResRes> getRes(){ 

       return this.Res;   

   }


   public  void setRes(ArrayList<ListUnitDepositReviewedResRes>  Res){ 

       this.Res = Res;   

   }


   public String toString(){ 

       return "ListUnitDepositReviewedRes{" +  
 
              "Res='" + this.Res + '\'' + 

      "}";

  } 
}