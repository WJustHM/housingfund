package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "HeadUnitPayWrongReceiptResCJEHJ")
@XmlAccessorType(XmlAccessType.FIELD)
public class HeadUnitPayWrongReceiptResCJEHJ  implements Serializable{

   private  String    FSEHJ;  //发生额合计

   private  String    DWCJEHJ;  //单位错缴金额合计

   private  String    GRCJEHJ;  //个人错缴金额合计

   public  String getFSEHJ(){

       return this.FSEHJ;   

   }


   public  void setFSEHJ(String  FSEHJ){

       this.FSEHJ = FSEHJ;   

   }


   public  String getDWCJEHJ(){

       return this.DWCJEHJ;   

   }


   public  void setDWCJEHJ(String  DWCJEHJ){

       this.DWCJEHJ = DWCJEHJ;   

   }


   public  String getGRCJEHJ(){

       return this.GRCJEHJ;   

   }


   public  void setGRCJEHJ(String  GRCJEHJ){

       this.GRCJEHJ = GRCJEHJ;   

   }


   public String toString(){ 

       return "HeadUnitPayWrongReceiptResCJEHJ{" +  
 
              "FSEHJ='" + this.FSEHJ + '\'' + "," +
              "DWCJEHJ='" + this.DWCJEHJ + '\'' + "," +
              "GRCJEHJ='" + this.GRCJEHJ + '\'' + 

      "}";

  } 
}