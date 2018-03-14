package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "ListReviewIndiAcctRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListReviewIndiAcctRes  implements Serializable{

   private  ArrayList<ListReviewIndiAcctResRes>    Res;  // 

   public  ArrayList<ListReviewIndiAcctResRes> getRes(){ 

       return this.Res;   

   }


   public  void setRes(ArrayList<ListReviewIndiAcctResRes>  Res){ 

       this.Res = Res;   

   }


   public String toString(){ 

       return "ListReviewIndiAcctRes{" +  
 
              "Res='" + this.Res + '\'' + 

      "}";

  } 
}