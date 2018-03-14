package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "ListUnitPayCallRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListUnitPayCallRes  implements Serializable{

   private  ArrayList<ListUnitPayCallResRes>    Res;  // 

   public  ArrayList<ListUnitPayCallResRes> getRes(){ 

       return this.Res;   

   }


   public  void setRes(ArrayList<ListUnitPayCallResRes>  Res){ 

       this.Res = Res;   

   }


   public String toString(){ 

       return "ListUnitPayCallRes{" +  
 
              "Res='" + this.Res + '\'' + 

      "}";

  } 
}