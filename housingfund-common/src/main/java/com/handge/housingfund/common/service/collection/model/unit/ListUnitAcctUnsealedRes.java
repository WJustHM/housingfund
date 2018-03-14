package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "ListUnitAcctUnsealedRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListUnitAcctUnsealedRes  implements Serializable{

   private  ArrayList<ListUnitAcctUnsealedResRes>    Res;  // 

   public  ArrayList<ListUnitAcctUnsealedResRes> getRes(){ 

       return this.Res;   

   }


   public  void setRes(ArrayList<ListUnitAcctUnsealedResRes>  Res){ 

       this.Res = Res;   

   }


   public String toString(){ 

       return "ListUnitAcctUnsealedRes{" +  
 
              "Res='" + this.Res + '\'' + 

      "}";

  } 
}