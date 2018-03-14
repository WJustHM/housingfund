package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "AlterIndiAcctSubmitPost")
@XmlAccessorType(XmlAccessType.FIELD)
public class AlterIndiAcctSubmitPost  implements Serializable{

   private  ArrayList<String>    YWLSHJH;  //业务流水号集合 

   public  ArrayList<String> getYWLSHJH(){ 

       return this.YWLSHJH;   

   }


   public  void setYWLSHJH(ArrayList<String>  YWLSHJH){ 

       this.YWLSHJH = YWLSHJH;   

   }


   public String toString(){ 

       return "AlterIndiAcctSubmitPost{" +  
 
              "YWLSHJH='" + this.YWLSHJH + '\'' + 

      "}";

  } 
}