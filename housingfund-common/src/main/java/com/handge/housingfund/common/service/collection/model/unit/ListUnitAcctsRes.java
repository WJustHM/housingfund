package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "ListUnitAcctsRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListUnitAcctsRes  implements Serializable{

   private  ArrayList<ListUnitAcctsResRes>    Res;  // 

   public  ArrayList<ListUnitAcctsResRes> getRes(){ 

       return this.Res;   

   }


   public  void setRes(ArrayList<ListUnitAcctsResRes>  Res){ 

       this.Res = Res;   

   }


   public String toString(){ 

       return "ListUnitAcctsRes{" +  
 
              "Res='" + this.Res + '\'' + 

      "}";

  } 
}