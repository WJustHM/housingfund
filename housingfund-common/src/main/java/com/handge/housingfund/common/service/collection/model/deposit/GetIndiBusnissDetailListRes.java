package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "GetIndiBusnissDetailListRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetIndiBusnissDetailListRes  implements Serializable{

   private  ArrayList<GetIndiBusnissDetailListResRes>    Res;  // 

   public  ArrayList<GetIndiBusnissDetailListResRes> getRes(){ 

       return this.Res;   

   }


   public  void setRes(ArrayList<GetIndiBusnissDetailListResRes>  Res){ 

       this.Res = Res;   

   }


   public String toString(){ 

       return "GetIndiBusnissDetailListRes{" +  
 
              "Res='" + this.Res + '\'' + 

      "}";

  } 
}