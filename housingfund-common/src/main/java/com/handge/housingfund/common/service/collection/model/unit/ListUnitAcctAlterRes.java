package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "ListUnitAcctAlterRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListUnitAcctAlterRes  implements Serializable{

   private  ArrayList<ListUnitAcctAlterResRes>    Res;  // 

   public  ArrayList<ListUnitAcctAlterResRes> getRes(){ 

       return this.Res;   

   }


   public  void setRes(ArrayList<ListUnitAcctAlterResRes>  Res){ 

       this.Res = Res;   

   }


   public String toString(){ 

       return "ListUnitAcctAlterRes{" +  
 
              "Res='" + this.Res + '\'' + 

      "}";

  } 
}