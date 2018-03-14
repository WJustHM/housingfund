package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "ApplicantPostCommonBorrowerInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicantPostCommonBorrowerInformation  implements Serializable {

   private  ApplicantPostCommonBorrowerInformationCoBorrowerInformation    CoBorrowerInformation;  // 

   private  String    BLZL;   //办理资料  //

   private  ApplicantPostCommonBorrowerInformationAccountInformation    AccountInformation;  // 

   private  ApplicantPostCommonBorrowerInformationUnitInformation    UnitInformation;  // 

   public  ApplicantPostCommonBorrowerInformationCoBorrowerInformation getCoBorrowerInformation(){ 

       return this.CoBorrowerInformation;   

   }


   public  void setCoBorrowerInformation(ApplicantPostCommonBorrowerInformationCoBorrowerInformation  CoBorrowerInformation){ 

       this.CoBorrowerInformation = CoBorrowerInformation;   

   }


   public String  getSCZL(){

       return this.BLZL;

   }


   public  void setSCZL( String SCZL){

       this.BLZL = SCZL;

   }


   public  ApplicantPostCommonBorrowerInformationAccountInformation getAccountInformation(){ 

       return this.AccountInformation;   

   }


   public  void setAccountInformation(ApplicantPostCommonBorrowerInformationAccountInformation  AccountInformation){ 

       this.AccountInformation = AccountInformation;   

   }


   public  ApplicantPostCommonBorrowerInformationUnitInformation getUnitInformation(){ 

       return this.UnitInformation;   

   }


   public  void setUnitInformation(ApplicantPostCommonBorrowerInformationUnitInformation  UnitInformation){ 

       this.UnitInformation = UnitInformation;   

   }


   public String toString(){ 

       return "ApplicantPostCommonBorrowerInformation{" +  
 
              "CoBorrowerInformation='" + this.CoBorrowerInformation + '\'' + "," +
              "BLZL='" + this.BLZL + '\'' + "," +
              "AccountInformation='" + this.AccountInformation + '\'' + "," +
              "UnitInformation='" + this.UnitInformation + '\'' + 

      "}";

  } 
}