package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "AutoIndiAcctMergeRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class AutoIndiAcctMergeRes  implements Serializable{

   private  ArrayList<AutoIndiAcctMergeResRes>    Res;  // 

   public  ArrayList<AutoIndiAcctMergeResRes> getRes(){ 

       return this.Res;   

   }


   public  void setRes(ArrayList<AutoIndiAcctMergeResRes>  Res){ 

       this.Res = Res;   

   }


   public String toString(){ 

       return "AutoIndiAcctMergeRes{" +  
 
              "Res='" + this.Res + '\'' + 

      "}";

  } 
}