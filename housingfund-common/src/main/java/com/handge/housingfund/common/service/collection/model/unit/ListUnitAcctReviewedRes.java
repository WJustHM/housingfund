package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "ListUnitAcctReviewedRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListUnitAcctReviewedRes  implements Serializable{

   private  ArrayList<ListUnitAcctReviewedResRes>    Res;  // 

   public  ArrayList<ListUnitAcctReviewedResRes> getRes(){ 

       return this.Res;   

   }


   public  void setRes(ArrayList<ListUnitAcctReviewedResRes>  Res){ 

       this.Res = Res;   

   }


   public String toString(){ 

       return "ListUnitAcctReviewedRes{" +  
 
              "Res='" + this.Res + '\'' + 

      "}";

  } 
}