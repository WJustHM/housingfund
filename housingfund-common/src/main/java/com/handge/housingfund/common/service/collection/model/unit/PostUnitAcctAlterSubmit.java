package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "PostUnitAcctAlterSubmit")
@XmlAccessorType(XmlAccessType.FIELD)
public class PostUnitAcctAlterSubmit  implements Serializable{

   private  ArrayList<String>    YWLSHJH;  // 

   public  ArrayList<String> getYWLSHJH(){ 

       return this.YWLSHJH;   

   }


   public  void setYWLSHJH(ArrayList<String>  YWLSHJH){ 

       this.YWLSHJH = YWLSHJH;   

   }


   public String toString(){ 

       return "PostUnitAcctAlterSubmit{" +  
 
              "YWLSHJH='" + this.YWLSHJH + '\'' + 

      "}";

  } 
}