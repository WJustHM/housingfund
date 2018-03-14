package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "ListOperationAcctsRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListOperationAcctsRes  implements Serializable{

   private  ArrayList<ListOperationAcctsResRes>    Res;  // 

   public  ArrayList<ListOperationAcctsResRes> getRes(){ 

       return this.Res;   

   }


   public  void setRes(ArrayList<ListOperationAcctsResRes>  Res){ 

       this.Res = Res;   

   }


   public String toString(){ 

       return "ListOperationAcctsRes{" +  
 
              "Res='" + this.Res + '\'' + 

      "}";

  } 
}