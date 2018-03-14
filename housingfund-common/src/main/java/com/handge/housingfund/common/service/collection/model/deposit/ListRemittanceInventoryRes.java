package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "ListRemittanceInventoryRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListRemittanceInventoryRes  implements Serializable{

   private  ArrayList<ListRemittanceInventoryResDWHJQC>    DWHJQC;  //单位汇缴清册 

   private  ListRemittanceInventoryResDWJCXX    DWJCXX;  //单位缴存信息 

   public  ArrayList<ListRemittanceInventoryResDWHJQC> getDWHJQC(){ 

       return this.DWHJQC;   

   }


   public  void setDWHJQC(ArrayList<ListRemittanceInventoryResDWHJQC>  DWHJQC){ 

       this.DWHJQC = DWHJQC;   

   }


   public  ListRemittanceInventoryResDWJCXX getDWJCXX(){ 

       return this.DWJCXX;   

   }


   public  void setDWJCXX(ListRemittanceInventoryResDWJCXX  DWJCXX){ 

       this.DWJCXX = DWJCXX;   

   }


   public String toString(){ 

       return "ListRemittanceInventoryRes{" +  
 
              "DWHJQC='" + this.DWHJQC + '\'' + "," +
              "DWJCXX='" + this.DWJCXX + '\'' + 

      "}";

  } 
}