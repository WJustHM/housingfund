package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "ListUnitDepositDeatilRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListUnitDepositDeatilRes implements Serializable{

   private  ArrayList<ListUnitDepositDeatilResRes>    Res;  // 

   public  ArrayList<ListUnitDepositDeatilResRes> getRes(){ 

       return this.Res;   

   }


   public  void setRes(ArrayList<ListUnitDepositDeatilResRes>  Res){ 

       this.Res = Res;   

   }


   public String toString(){ 

       return "ListUnitDepositDeatilRes{" +  
 
              "Res='" + this.Res + '\'' + 

      "}";

  } 
}