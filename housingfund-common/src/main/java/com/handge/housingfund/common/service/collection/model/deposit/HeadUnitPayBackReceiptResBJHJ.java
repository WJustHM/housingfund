package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "HeadUnitPayBackReceiptResBJHJ")
@XmlAccessorType(XmlAccessType.FIELD)
public class HeadUnitPayBackReceiptResBJHJ  implements Serializable{

   private  String    FSEHJ;  //发生额合计

   public  String getFSEHJ(){

       return this.FSEHJ;   

   }


   public  void setFSEHJ(String  FSEHJ){

       this.FSEHJ = FSEHJ;   

   }


   public String toString(){ 

       return "HeadUnitPayBackReceiptResBJHJ{" +  
 
              "FSEHJ='" + this.FSEHJ + '\'' + 

      "}";

  } 
}