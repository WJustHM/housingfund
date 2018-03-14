package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "ListIndiAcctsTransferRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListIndiAcctsTransferRes  implements Serializable{

   private  ArrayList<ListIndiAcctsTransferResRes>    Res;  // 

   public  ArrayList<ListIndiAcctsTransferResRes> getRes(){ 

       return this.Res;   

   }


   public  void setRes(ArrayList<ListIndiAcctsTransferResRes>  Res){ 

       this.Res = Res;   

   }


   public String toString(){ 

       return "ListIndiAcctsTransferRes{" +  
 
              "Res='" + this.Res + '\'' + 

      "}";

  } 
}