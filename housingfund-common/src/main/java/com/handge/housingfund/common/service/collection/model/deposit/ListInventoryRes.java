package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "ListInventoryRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListInventoryRes  implements Serializable{

   private  ArrayList<ListInventoryResRes>    Res;  // 

   public  ArrayList<ListInventoryResRes> getRes(){ 

       return this.Res;   

   }


   public  void setRes(ArrayList<ListInventoryResRes>  Res){ 

       this.Res = Res;   

   }


   public String toString(){ 

       return "ListInventoryRes{" +  
 
              "Res='" + this.Res + '\'' + 

      "}";

  } 
}