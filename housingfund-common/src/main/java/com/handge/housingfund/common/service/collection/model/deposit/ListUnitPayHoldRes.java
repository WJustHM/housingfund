package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "ListUnitPayHoldRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListUnitPayHoldRes  implements Serializable{

   private  ArrayList<ListUnitPayHoldResRes>    Res;  // 

   public  ArrayList<ListUnitPayHoldResRes> getRes(){ 

       return this.Res;   

   }


   public  void setRes(ArrayList<ListUnitPayHoldResRes>  Res){ 

       this.Res = Res;   

   }


   public String toString(){ 

       return "ListUnitPayHoldRes{" +  
 
              "Res='" + this.Res + '\'' + 

      "}";

  } 
}