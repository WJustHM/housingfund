package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "ListUnitDepositReviewRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListUnitDepositReviewRes  implements Serializable{

   private  ArrayList<ListUnitDepositReviewResRes>    Res;  // 

   public  ArrayList<ListUnitDepositReviewResRes> getRes(){ 

       return this.Res;   

   }


   public  void setRes(ArrayList<ListUnitDepositReviewResRes>  Res){ 

       this.Res = Res;   

   }


   public String toString(){ 

       return "ListUnitDepositReviewRes{" +  
 
              "Res='" + this.Res + '\'' + 

      "}";

  } 
}