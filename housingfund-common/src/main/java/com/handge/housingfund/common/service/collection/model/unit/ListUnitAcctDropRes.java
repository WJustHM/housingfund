package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "ListUnitAcctDropRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListUnitAcctDropRes  implements Serializable{

   private  ArrayList<ListUnitAcctDropResRes>    Res;  // 

   public  ArrayList<ListUnitAcctDropResRes> getRes(){ 

       return this.Res;   

   }


   public  void setRes(ArrayList<ListUnitAcctDropResRes>  Res){ 

       this.Res = Res;   

   }


   public String toString(){ 

       return "ListUnitAcctDropRes{" +  
 
              "Res='" + this.Res + '\'' + 

      "}";

  } 
}