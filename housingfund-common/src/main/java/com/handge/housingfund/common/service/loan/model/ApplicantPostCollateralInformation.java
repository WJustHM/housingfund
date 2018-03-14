package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "ApplicantPostCollateralInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicantPostCollateralInformation  implements Serializable {

   private ArrayList<ApplicantPostCollateralInformationMortgageInformation>    MortgageInformation;  //

   private  String    BLZL;   //办理资料  //

   private  String    DKDBLX;  //贷款担保类型 

   private  ArrayList<ApplicantPostCollateralInformationPledgeInformation>    PledgeInformation;  //

   private  ArrayList<ApplicantPostCollateralInformationGuaranteeInformation>    GuaranteeInformation;  //

   public  ArrayList<ApplicantPostCollateralInformationMortgageInformation> getMortgageInformation(){

       return this.MortgageInformation;   

   }


   public  void setMortgageInformation(ArrayList<ApplicantPostCollateralInformationMortgageInformation>  MortgageInformation){

       this.MortgageInformation = MortgageInformation;   

   }


   public  String getSCZL(){

       return this.BLZL;

   }


   public  void setSCZL( String SCZL){

       this.BLZL = SCZL;

   }


   public  String getDKDBLX(){ 

       return this.DKDBLX;   

   }


   public  void setDKDBLX(String  DKDBLX){ 

       this.DKDBLX = DKDBLX;   

   }


   public ArrayList<ApplicantPostCollateralInformationPledgeInformation> getPledgeInformation(){

       return this.PledgeInformation;   

   }


   public  void setPledgeInformation(ArrayList<ApplicantPostCollateralInformationPledgeInformation> PledgeInformation){

       this.PledgeInformation = PledgeInformation;   

   }


   public ArrayList<ApplicantPostCollateralInformationGuaranteeInformation> getGuaranteeInformation(){

       return this.GuaranteeInformation;   

   }


   public  void setGuaranteeInformation(ArrayList<ApplicantPostCollateralInformationGuaranteeInformation> GuaranteeInformation){

       this.GuaranteeInformation = GuaranteeInformation;   

   }


   public String toString(){ 

       return "ApplicantPostCollateralInformation{" +  
 
              "MortgageInformation='" + this.MortgageInformation + '\'' + "," +
              "BLZL='" + this.BLZL + '\'' + "," +
              "DKDBLX='" + this.DKDBLX + '\'' + "," +
              "PledgeInformation='" + this.PledgeInformation + '\'' + "," +
              "GuaranteeInformation='" + this.GuaranteeInformation + '\'' + 

      "}";

  } 
}