package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "ApplicantPost")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicantPost  implements Serializable {

   private  ApplicantPostCommonBorrowerInformation    CommonBorrowerInformation;  // 

   private  ApplicantPostHouseInformation    HouseInformation;  // 

   private  String    WD_id;  //网点id 

   private  String    BLZL;   //办理资料  //
    
   private  ApplicantPostCollateralInformation    CollateralInformation;  //

   private  ApplicantPostCapitalInformation    CapitalInformation;  // 

   private  ApplicantPostApplicantInformation    ApplicantInformation;  //

   private String CZY;

    public String getCZY() {
        return CZY;
    }

    public void setCZY(String CZY) {
        this.CZY = CZY;
    }

    public  ApplicantPostCommonBorrowerInformation getCommonBorrowerInformation(){

       return this.CommonBorrowerInformation;   

   }


   public  void setCommonBorrowerInformation(ApplicantPostCommonBorrowerInformation  CommonBorrowerInformation){ 

       this.CommonBorrowerInformation = CommonBorrowerInformation;   

   }


   public  ApplicantPostHouseInformation getHouseInformation(){ 

       return this.HouseInformation;   

   }


   public  void setHouseInformation(ApplicantPostHouseInformation  HouseInformation){ 

       this.HouseInformation = HouseInformation;   

   }


   public  String getWD_id(){ 

       return this.WD_id;   

   }


   public  void setWD_id(String  WD_id){ 

       this.WD_id = WD_id;   

   }


   public  String getSCZL(){

       return this.BLZL;

   }


   public  void setSCZL( String SCZL){

       this.BLZL = SCZL;

   }


   public  ApplicantPostCollateralInformation getCollateralInformation(){ 

       return this.CollateralInformation;   

   }


   public  void setCollateralInformation(ApplicantPostCollateralInformation  CollateralInformation){ 

       this.CollateralInformation = CollateralInformation;   

   }


   public  ApplicantPostCapitalInformation getCapitalInformation(){ 

       return this.CapitalInformation;   

   }


   public  void setCapitalInformation(ApplicantPostCapitalInformation  CapitalInformation){ 

       this.CapitalInformation = CapitalInformation;   

   }


   public  ApplicantPostApplicantInformation getApplicantInformation(){ 

       return this.ApplicantInformation;   

   }


   public  void setApplicantInformation(ApplicantPostApplicantInformation  ApplicantInformation){ 

       this.ApplicantInformation = ApplicantInformation;   

   }


   public String toString(){ 

       return "ApplicantPost{" +  
 
              "CommonBorrowerInformation='" + this.CommonBorrowerInformation + '\'' + "," +
              "HouseInformation='" + this.HouseInformation + '\'' + "," +
              "WD_id='" + this.WD_id + '\'' + "," +
              "BLZL='" + this.BLZL + '\'' + "," +
              "CollateralInformation='" + this.CollateralInformation + '\'' + "," +
              "CapitalInformation='" + this.CapitalInformation + '\'' + "," +
              "ApplicantInformation='" + this.ApplicantInformation + '\'' + 

      "}";

  } 
}