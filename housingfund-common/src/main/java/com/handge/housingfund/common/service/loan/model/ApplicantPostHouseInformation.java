package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "ApplicantPostHouseInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicantPostHouseInformation  implements Serializable {

   private  ApplicantPostHouseInformationPurchaseSecondInformation    PurchaseSecondInformation;  // 

   private  String    DKYT;  //贷款用途 

   private  String    SFWESF;  //是否为二手房 

   private  ApplicantPostHouseInformationPurchaseFirstInformation    PurchaseFirstInformation;  // 

   private  ApplicantPostHouseInformationOverhaulInformation    OverhaulInformation;  // 

   private  ApplicantPostHouseInformationBuildInformation    BuildInformation;  // 

   public  ApplicantPostHouseInformationPurchaseSecondInformation getPurchaseSecondInformation(){ 

       return this.PurchaseSecondInformation;   

   }


   public  void setPurchaseSecondInformation(ApplicantPostHouseInformationPurchaseSecondInformation  PurchaseSecondInformation){ 

       this.PurchaseSecondInformation = PurchaseSecondInformation;   

   }


   public  String getDKYT(){ 

       return this.DKYT;   

   }


   public  void setDKYT(String  DKYT){ 

       this.DKYT = DKYT;   

   }


   public  String getSFWESF(){ 

       return this.SFWESF;   

   }


   public  void setSFWESF(String  SFWESF){ 

       this.SFWESF = SFWESF;   

   }


   public  ApplicantPostHouseInformationPurchaseFirstInformation getPurchaseFirstInformation(){ 

       return this.PurchaseFirstInformation;   

   }


   public  void setPurchaseFirstInformation(ApplicantPostHouseInformationPurchaseFirstInformation  PurchaseFirstInformation){ 

       this.PurchaseFirstInformation = PurchaseFirstInformation;   

   }


   public  ApplicantPostHouseInformationOverhaulInformation getOverhaulInformation(){ 

       return this.OverhaulInformation;   

   }


   public  void setOverhaulInformation(ApplicantPostHouseInformationOverhaulInformation  OverhaulInformation){ 

       this.OverhaulInformation = OverhaulInformation;   

   }


   public  ApplicantPostHouseInformationBuildInformation getBuildInformation(){ 

       return this.BuildInformation;   

   }


   public  void setBuildInformation(ApplicantPostHouseInformationBuildInformation  BuildInformation){ 

       this.BuildInformation = BuildInformation;   

   }


   public String toString(){ 

       return "ApplicantPostHouseInformation{" +  
 
              "PurchaseSecondInformation='" + this.PurchaseSecondInformation + '\'' + "," +
              "DKYT='" + this.DKYT + '\'' + "," +
              "SFWESF='" + this.SFWESF + '\'' + "," +
              "PurchaseFirstInformation='" + this.PurchaseFirstInformation + '\'' + "," +
              "OverhaulInformation='" + this.OverhaulInformation + '\'' + "," +
              "BuildInformation='" + this.BuildInformation + '\'' + 

      "}";

  } 
}