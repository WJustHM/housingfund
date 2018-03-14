package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "SignContractPostContractInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class SignContractPostContractInformation  implements Serializable {

   private  String    YDFKRQ;  //约定放款日期 

   private  SignContractPostContractInformationBuildInformation    BuildInformation;  // 

   private  SignContractPostContractInformationPurchaseInformation    PurchaseInformation;  // 

   private  String    LLFDBL;  //利率浮动比例 

   private  String    DKYT;  //贷款用途 

   private  String    YDHKR;  //约定还款日 

   private  String    YDDQRQ;  //约定到期日期 

   private  String    ZHKHYHMC;  //账号开户银行名称 

   private  String    SWTYHMC;  //受委托银行名称 

   private  String    SWTYHDM;  //受委托银行代码 

   private  String    JKHTLL;  //借款合同利率 

   private  String    HKZH;  //还款账号 

    public String getHKZHHM() {
        return HKZHHM;
    }

    public void setHKZHHM(String HKZHHM) {
        this.HKZHHM = HKZHHM;
    }

    private  String    HKZHHM;//还款账户户名

   private  String    JKHTQDRQ;  //借款合同签订日期 

   private  SignContractPostContractInformationOverhaulInformation    OverhaulInformation;  // 

   private  String    ZHKHYHDM;  //账号开户银行代码 

   public  String getYDFKRQ(){ 

       return this.YDFKRQ;   

   }


   public  void setYDFKRQ(String  YDFKRQ){ 

       this.YDFKRQ = YDFKRQ;   

   }


   public  SignContractPostContractInformationBuildInformation getBuildInformation(){ 

       return this.BuildInformation;   

   }


   public  void setBuildInformation(SignContractPostContractInformationBuildInformation  BuildInformation){ 

       this.BuildInformation = BuildInformation;   

   }


   public  SignContractPostContractInformationPurchaseInformation getPurchaseInformation(){ 

       return this.PurchaseInformation;   

   }


   public  void setPurchaseInformation(SignContractPostContractInformationPurchaseInformation  PurchaseInformation){ 

       this.PurchaseInformation = PurchaseInformation;   

   }


   public  String getLLFDBL(){ 

       return this.LLFDBL;   

   }


   public  void setLLFDBL(String  LLFDBL){ 

       this.LLFDBL = LLFDBL;   

   }


   public  String getDKYT(){ 

       return this.DKYT;   

   }


   public  void setDKYT(String  DKYT){ 

       this.DKYT = DKYT;   

   }


   public  String getYDHKR(){ 

       return this.YDHKR;   

   }


   public  void setYDHKR(String  YDHKR){ 

       this.YDHKR = YDHKR;   

   }


   public  String getYDDQRQ(){ 

       return this.YDDQRQ;   

   }


   public  void setYDDQRQ(String  YDDQRQ){ 

       this.YDDQRQ = YDDQRQ;   

   }


   public  String getZHKHYHMC(){ 

       return this.ZHKHYHMC;   

   }


   public  void setZHKHYHMC(String  ZHKHYHMC){ 

       this.ZHKHYHMC = ZHKHYHMC;   

   }


   public  String getSWTYHMC(){ 

       return this.SWTYHMC;   

   }


   public  void setSWTYHMC(String  SWTYHMC){ 

       this.SWTYHMC = SWTYHMC;   

   }


   public  String getSWTYHDM(){ 

       return this.SWTYHDM;   

   }


   public  void setSWTYHDM(String  SWTYHDM){ 

       this.SWTYHDM = SWTYHDM;   

   }


   public  String getJKHTLL(){ 

       return this.JKHTLL;   

   }


   public  void setJKHTLL(String  JKHTLL){ 

       this.JKHTLL = JKHTLL;   

   }


   public  String getHKZH(){ 

       return this.HKZH;   

   }


   public  void setHKZH(String  HKZH){ 

       this.HKZH = HKZH;   

   }


   public  String getJKHTQDRQ(){ 

       return this.JKHTQDRQ;   

   }


   public  void setJKHTQDRQ(String  JKHTQDRQ){ 

       this.JKHTQDRQ = JKHTQDRQ;   

   }


   public  SignContractPostContractInformationOverhaulInformation getOverhaulInformation(){ 

       return this.OverhaulInformation;   

   }


   public  void setOverhaulInformation(SignContractPostContractInformationOverhaulInformation  OverhaulInformation){ 

       this.OverhaulInformation = OverhaulInformation;   

   }


   public  String getZHKHYHDM(){ 

       return this.ZHKHYHDM;   

   }


   public  void setZHKHYHDM(String  ZHKHYHDM){ 

       this.ZHKHYHDM = ZHKHYHDM;   

   }


   public String toString(){ 

       return "SignContractPostContractInformation{" +  
 
              "YDFKRQ='" + this.YDFKRQ + '\'' + "," +
              "BuildInformation='" + this.BuildInformation + '\'' + "," +
              "PurchaseInformation='" + this.PurchaseInformation + '\'' + "," +
              "LLFDBL='" + this.LLFDBL + '\'' + "," +
              "DKYT='" + this.DKYT + '\'' + "," +
              "YDHKR='" + this.YDHKR + '\'' + "," +
              "YDDQRQ='" + this.YDDQRQ + '\'' + "," +
              "ZHKHYHMC='" + this.ZHKHYHMC + '\'' + "," +
              "SWTYHMC='" + this.SWTYHMC + '\'' + "," +
              "SWTYHDM='" + this.SWTYHDM + '\'' + "," +
              "JKHTLL='" + this.JKHTLL + '\'' + "," +
              "HKZH='" + this.HKZH + '\'' + "," +
              "JKHTQDRQ='" + this.JKHTQDRQ + '\'' + "," +
              "OverhaulInformation='" + this.OverhaulInformation + '\'' + "," +
              "ZHKHYHDM='" + this.ZHKHYHDM + '\'' + 

      "}";

  } 
}