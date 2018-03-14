package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "ListIndiAcctHistoryRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListIndiAcctHistoryRes  implements Serializable{

   private  ArrayList<ListIndiAcctHistoryResRes>    Res;  // 

   public  ArrayList<ListIndiAcctHistoryResRes> getRes(){ 

       return this.Res;   

   }


   public  void setRes(ArrayList<ListIndiAcctHistoryResRes>  Res){ 

       this.Res = Res;   

   }


   public String toString(){ 

       return "ListIndiAcctHistoryRes{" +  
 
              "Res='" + this.Res + '\'' + 

      "}";

  } 
}