package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "ListUnitPayCallHistoryRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListUnitPayCallHistoryRes  implements Serializable{

   private  ArrayList<ListUnitPayCallHistoryResRes>    Res;  // 

   public  ArrayList<ListUnitPayCallHistoryResRes> getRes(){ 

       return this.Res;   

   }


   public  void setRes(ArrayList<ListUnitPayCallHistoryResRes>  Res){ 

       this.Res = Res;   

   }


   public String toString(){ 

       return "ListUnitPayCallHistoryRes{" +  
 
              "Res='" + this.Res + '\'' + 

      "}";

  } 
}