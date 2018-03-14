package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "ApplicantPostApplicantInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicantPostApplicantInformation  implements Serializable {

   private  String    JCD;  //缴存地 

   private  ApplicantPostApplicantInformationInformation    Information;  // 

   private  String    JKRGJJZH;  //借款人公积金账号 

   public  String getJCD(){ 

       return this.JCD;   

   }


   public  void setJCD(String  JCD){ 

       this.JCD = JCD;   

   }


   public  ApplicantPostApplicantInformationInformation getInformation(){ 

       return this.Information;   

   }


   public  void setInformation(ApplicantPostApplicantInformationInformation  Information){ 

       this.Information = Information;   

   }


   public  String getJKRGJJZH(){ 

       return this.JKRGJJZH;   

   }


   public  void setJKRGJJZH(String  JKRGJJZH){ 

       this.JKRGJJZH = JKRGJJZH;   

   }


   public String toString(){ 

       return "ApplicantPostApplicantInformation{" +  
 
              "JCD='" + this.JCD + '\'' + "," +
              "Information='" + this.Information + '\'' + "," +
              "JKRGJJZH='" + this.JKRGJJZH + '\'' + 

      "}";

  } 
}