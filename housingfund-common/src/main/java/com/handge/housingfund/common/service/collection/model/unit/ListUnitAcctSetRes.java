package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "ListUnitAcctSetRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListUnitAcctSetRes  implements Serializable{

   private  ArrayList<ListUnitAcctSetResRes>    Res;  // 

   public  ArrayList<ListUnitAcctSetResRes> getRes(){ 

       return this.Res;   

   }


   public  void setRes(ArrayList<ListUnitAcctSetResRes>  Res){ 

       this.Res = Res;   

   }


   public String toString(){ 

       return "ListUnitAcctSetRes{" +  
 
              "Res='" + this.Res + '\'' + 

      "}";

  } 
}