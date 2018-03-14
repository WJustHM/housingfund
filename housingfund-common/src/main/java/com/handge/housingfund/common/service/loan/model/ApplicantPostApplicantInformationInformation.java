package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "ApplicantPostApplicantInformationInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicantPostApplicantInformationInformation  implements Serializable {

   private  ApplicantPostApplicantInformationInformationBorrowerInformation    BorrowerInformation;  // 

   private  String    BLZL;   //办理资料  //

   private  ApplicantPostApplicantInformationInformationAccountInformation    AccountInformation;  // 

   private  ApplicantPostApplicantInformationInformationUnitInformation    UnitInformation;  // 

   public  ApplicantPostApplicantInformationInformationBorrowerInformation getBorrowerInformation(){ 

       return this.BorrowerInformation;   

   }


   public  void setBorrowerInformation(ApplicantPostApplicantInformationInformationBorrowerInformation  BorrowerInformation){ 

       this.BorrowerInformation = BorrowerInformation;   

   }


   public  String getSCZL(){

       return this.BLZL;

   }


   public  void setSCZL(String  SCZL){

       this.BLZL = SCZL;

   }


   public  ApplicantPostApplicantInformationInformationAccountInformation getAccountInformation(){ 

       return this.AccountInformation;   

   }


   public  void setAccountInformation(ApplicantPostApplicantInformationInformationAccountInformation  AccountInformation){ 

       this.AccountInformation = AccountInformation;   

   }


   public  ApplicantPostApplicantInformationInformationUnitInformation getUnitInformation(){ 

       return this.UnitInformation;   

   }


   public  void setUnitInformation(ApplicantPostApplicantInformationInformationUnitInformation  UnitInformation){ 

       this.UnitInformation = UnitInformation;   

   }


   public String toString(){ 

       return "ApplicantPostApplicantInformationInformation{" +  
 
              "BorrowerInformation='" + this.BorrowerInformation + '\'' + "," +
              "BLZL='" + this.BLZL + '\'' + "," +
              "AccountInformation='" + this.AccountInformation + '\'' + "," +
              "UnitInformation='" + this.UnitInformation + '\'' + 

      "}";

  } 
}