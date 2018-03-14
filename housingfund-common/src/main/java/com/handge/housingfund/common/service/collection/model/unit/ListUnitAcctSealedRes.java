package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "ListUnitAcctSealedRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListUnitAcctSealedRes  implements Serializable{

   private  ArrayList<ListUnitAcctSealedResRes>    Res;  // 

   public  ArrayList<ListUnitAcctSealedResRes> getRes(){ 

       return this.Res;   

   }


   public  void setRes(ArrayList<ListUnitAcctSealedResRes>  Res){ 

       this.Res = Res;   

   }


   public String toString(){ 

       return "ListUnitAcctSealedRes{" +  
 
              "Res='" + this.Res + '\'' + 

      "}";

  } 
}