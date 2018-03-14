package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "ListIndiAcctReviewedRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListIndiAcctReviewedRes  implements Serializable{

   private  ArrayList<ListIndiAcctReviewedResRes>    Res;  // 

   public  ArrayList<ListIndiAcctReviewedResRes> getRes(){ 

       return this.Res;   

   }


   public  void setRes(ArrayList<ListIndiAcctReviewedResRes>  Res){ 

       this.Res = Res;   

   }


   public String toString(){ 

       return "ListIndiAcctReviewedRes{" +  
 
              "Res='" + this.Res + '\'' + 

      "}";

  } 
}