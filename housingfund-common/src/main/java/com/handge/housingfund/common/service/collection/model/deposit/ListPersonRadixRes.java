package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "ListPersonRadixRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListPersonRadixRes  implements Serializable{

   private  ArrayList<ListPersonRadixResRes>    Res;  // 

   public  ArrayList<ListPersonRadixResRes> getRes(){ 

       return this.Res;   

   }


   public  void setRes(ArrayList<ListPersonRadixResRes>  Res){ 

       this.Res = Res;   

   }


   public String toString(){ 

       return "ListPersonRadixRes{" +  
 
              "Res='" + this.Res + '\'' + 

      "}";

  } 
}