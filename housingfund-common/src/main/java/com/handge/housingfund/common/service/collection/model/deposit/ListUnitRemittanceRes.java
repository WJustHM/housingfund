package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "ListUnitRemittanceRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListUnitRemittanceRes  implements Serializable{

   private  ArrayList<ListUnitRemittanceResRes>    Res;  // 

   public  ArrayList<ListUnitRemittanceResRes> getRes(){ 

       return this.Res;   

   }


   public  void setRes(ArrayList<ListUnitRemittanceResRes>  Res){ 

       this.Res = Res;   

   }


   public String toString(){ 

       return "ListUnitRemittanceRes{" +  
 
              "Res='" + this.Res + '\'' + 

      "}";

  } 
}