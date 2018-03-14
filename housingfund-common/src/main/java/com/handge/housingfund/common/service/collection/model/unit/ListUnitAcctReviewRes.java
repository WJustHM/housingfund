package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "ListUnitAcctReviewRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListUnitAcctReviewRes  implements Serializable{

   private  ArrayList<ListUnitAcctReviewResRes>    Res;  // 

   public  ArrayList<ListUnitAcctReviewResRes> getRes(){ 

       return this.Res;   

   }


   public  void setRes(ArrayList<ListUnitAcctReviewResRes>  Res){ 

       this.Res = Res;   

   }


   public String toString(){ 

       return "ListUnitAcctReviewRes{" +  
 
              "Res='" + this.Res + '\'' + 

      "}";

  } 
}