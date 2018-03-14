package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "SignContractPostGuaranteeContractInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class SignContractPostGuaranteeContractInformation  implements Serializable {

   private ArrayList<SignContractPostGuaranteeContractInformationMortgageInformation>    MortgageInformation;  //

   private ArrayList<SignContractPostGuaranteeContractInformationGuaranteeInformation>    GuaranteeInformation;  //

   private  String    DKDBLX;  //贷款担保类型（0：抵押 1：质押 2：担保 3：其他） 

   private ArrayList<SignContractPostGuaranteeContractInformationPledgeInformation>    PledgeInformation;  //

   private  String    DBJGMC;  //担保机构名称 

   private  String    DBHTBH;  //担保合同编号 

   public  ArrayList<SignContractPostGuaranteeContractInformationMortgageInformation> getMortgageInformation(){

       return this.MortgageInformation;   

   }


   public  void setMortgageInformation(ArrayList<SignContractPostGuaranteeContractInformationMortgageInformation>  MortgageInformation){

       this.MortgageInformation = MortgageInformation;   

   }


   public  ArrayList<SignContractPostGuaranteeContractInformationGuaranteeInformation> getGuaranteeInformation(){

       return this.GuaranteeInformation;   

   }


   public  void setGuaranteeInformation(ArrayList<SignContractPostGuaranteeContractInformationGuaranteeInformation>  GuaranteeInformation){

       this.GuaranteeInformation = GuaranteeInformation;   

   }


   public  String getDKDBLX(){ 

       return this.DKDBLX;   

   }


   public  void setDKDBLX(String  DKDBLX){ 

       this.DKDBLX = DKDBLX;   

   }


   public  ArrayList<SignContractPostGuaranteeContractInformationPledgeInformation> getPledgeInformation(){

       return this.PledgeInformation;   

   }


   public  void setPledgeInformation(ArrayList<SignContractPostGuaranteeContractInformationPledgeInformation>  PledgeInformation){

       this.PledgeInformation = PledgeInformation;   

   }


   public  String getDBJGMC(){ 

       return this.DBJGMC;   

   }


   public  void setDBJGMC(String  DBJGMC){ 

       this.DBJGMC = DBJGMC;   

   }


   public  String getDBHTBH(){ 

       return this.DBHTBH;   

   }


   public  void setDBHTBH(String  DBHTBH){ 

       this.DBHTBH = DBHTBH;   

   }


   public String toString(){ 

       return "SignContractPostGuaranteeContractInformation{" +  
 
              "MortgageInformation='" + this.MortgageInformation + '\'' + "," +
              "GuaranteeInformation='" + this.GuaranteeInformation + '\'' + "," +
              "DKDBLX='" + this.DKDBLX + '\'' + "," +
              "PledgeInformation='" + this.PledgeInformation + '\'' + "," +
              "DBJGMC='" + this.DBJGMC + '\'' + "," +
              "DBHTBH='" + this.DBHTBH + '\'' + 

      "}";

  } 
}