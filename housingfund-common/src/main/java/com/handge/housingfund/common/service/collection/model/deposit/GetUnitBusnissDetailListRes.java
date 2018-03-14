package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "GetUnitBusnissDetailListRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetUnitBusnissDetailListRes  implements Serializable{

   private  ArrayList<GetUnitBusnissDetailListResRes>    Res;  // 

   public  ArrayList<GetUnitBusnissDetailListResRes> getRes(){ 

       return this.Res;   

   }


   public  void setRes(ArrayList<GetUnitBusnissDetailListResRes>  Res){ 

       this.Res = Res;   

   }


   public String toString(){ 

       return "GetUnitBusnissDetailListRes{" +  
 
              "Res='" + this.Res + '\'' + 

      "}";

  } 
}