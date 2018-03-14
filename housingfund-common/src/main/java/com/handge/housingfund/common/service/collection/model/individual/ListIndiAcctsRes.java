package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "ListIndiAcctsRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListIndiAcctsRes  implements Serializable{

   private  ArrayList<ListIndiAcctsResRes>    Res;  // 

   public  ArrayList<ListIndiAcctsResRes> getRes(){ 

       return this.Res;   

   }


   public  void setRes(ArrayList<ListIndiAcctsResRes>  Res){ 

       this.Res = Res;   

   }


   public String toString(){ 

       return "ListIndiAcctsRes{" +  
 
              "Res='" + this.Res + '\'' + 

      "}";

  } 
}