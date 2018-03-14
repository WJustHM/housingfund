package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "ApplicantPostCommonBorrowerInformationUnitInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicantPostCommonBorrowerInformationUnitInformation  implements Serializable {

   private  String    DWDH;  //单位电话 

   private  String    DWMC;  //单位名称 

   private  String    DWZH;  //单位账号 

   private  String    DWLB;  //单位性质

   private  String    DWDZ;  //单位地址 

   public  String getDWDH(){ 

       return this.DWDH;   

   }


   public  void setDWDH(String  DWDH){ 

       this.DWDH = DWDH;   

   }


   public  String getDWMC(){ 

       return this.DWMC;   

   }


   public  void setDWMC(String  DWMC){ 

       this.DWMC = DWMC;   

   }


   public  String getDWZH(){ 

       return this.DWZH;   

   }


   public  void setDWZH(String  DWZH){ 

       this.DWZH = DWZH;   

   }


   public  String getDWXZ(){ 

       return this.DWLB;

   }


   public  void setDWXZ(String  DWXZ){ 

       this.DWLB = DWXZ;

   }


   public  String getDWDZ(){ 

       return this.DWDZ;   

   }


   public  void setDWDZ(String  DWDZ){ 

       this.DWDZ = DWDZ;   

   }


   public String toString(){ 

       return "ApplicantPostCommonBorrowerInformationUnitInformation{" +  
 
              "DWDH='" + this.DWDH + '\'' + "," +
              "DWMC='" + this.DWMC + '\'' + "," +
              "DWZH='" + this.DWZH + '\'' + "," +
              "DWXZ='" + this.DWLB + '\'' + "," +
              "DWDZ='" + this.DWDZ + '\'' + 

      "}";

  } 
}