package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "ListUnitPayWrongRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListUnitPayWrongRes  implements Serializable{

   private  ArrayList<ListUnitPayWrongResRes>    Res;  // 

   public  ArrayList<ListUnitPayWrongResRes> getRes(){ 

       return this.Res;   

   }


   public  void setRes(ArrayList<ListUnitPayWrongResRes>  Res){ 

       this.Res = Res;   

   }


   public String toString(){ 

       return "ListUnitPayWrongRes{" +  
 
              "Res='" + this.Res + '\'' + 

      "}";

  } 
}