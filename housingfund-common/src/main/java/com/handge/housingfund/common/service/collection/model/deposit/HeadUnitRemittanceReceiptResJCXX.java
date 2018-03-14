package com.handge.housingfund.common.service.collection.model.deposit;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "HeadUnitRemittanceReceiptResJCXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class HeadUnitRemittanceReceiptResJCXX  implements Serializable{


   private  String    FSE;  //发生额

   private  String    HJFS;  //汇缴方式 

   private  String    STYHHM;  //受托银行户名

   private  Integer    FSRS;  //发生人数

   private  String    STYHZH;  //受托银行账号

   private  String    HBJNY;  //汇补缴年月

   private  String    STYHMC;  //受托银行名称 

   private  String    JZRQ;  //记账日期 

   private  String    QCQRDH;  //清册确认单号 

   private  String    STYHDM;  //受托银行代码 

   public  String getFSE(){

       return this.FSE;   

   }


   public  void setFSE(String  FSE){

       this.FSE = FSE;   

   }


   public  String getHJFS(){ 

       return this.HJFS;   

   }


   public  void setHJFS(String  HJFS){ 

       this.HJFS = HJFS;   

   }


   public  String getSTYHHM(){

       return this.STYHHM;

   }


   public  void setSTYHHM(String  STYHHM){

       this.STYHHM = STYHHM;

   }


   public  Integer getFSRS(){ 

       return this.FSRS;   

   }


   public  void setFSRS(Integer  FSRS){ 

       this.FSRS = FSRS;   

   }


   public  String getSTYHZH(){

       return this.STYHZH;

   }


   public  void setSTYHZH(String  STYHZH){

       this.STYHZH = STYHZH;

   }


   public  String getHBJNY(){ 

       return this.HBJNY;   

   }


   public  void setHBJNY(String  HBJNY){ 

       this.HBJNY = HBJNY;   

   }


   public  String getSTYHMC(){ 

       return this.STYHMC;   

   }


   public  void setSTYHMC(String  STYHMC){ 

       this.STYHMC = STYHMC;   

   }


   public  String getJZRQ(){ 

       return this.JZRQ;   

   }


   public  void setJZRQ(String  JZRQ){ 

       this.JZRQ = JZRQ;   

   }


   public  String getQCQRDH(){ 

       return this.QCQRDH;   

   }


   public  void setQCQRDH(String  QCQRDH){ 

       this.QCQRDH = QCQRDH;   

   }


   public  String getSTYHDM(){ 

       return this.STYHDM;   

   }


   public  void setSTYHDM(String  STYHDM){ 

       this.STYHDM = STYHDM;   

   }


   public String toString(){ 

       return "HeadUnitRemittanceReceiptResJCXX{" +  
 
              "FSE='" + this.FSE + '\'' + "," +
              "HJFS='" + this.HJFS + '\'' + "," +
              "STYHHM='" + this.STYHHM + '\'' + "," +
              "FSRS='" + this.FSRS + '\'' + "," +
              "STYHZH='" + this.STYHZH + '\'' + "," +
              "HBJNY='" + this.HBJNY + '\'' + "," +
              "STYHMC='" + this.STYHMC + '\'' + "," +
              "JZRQ='" + this.JZRQ + '\'' + "," +
              "QCQRDH='" + this.QCQRDH + '\'' + "," +
              "STYHDM='" + this.STYHDM + '\'' + 

      "}";

  } 
}