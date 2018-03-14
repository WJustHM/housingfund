package com.handge.housingfund.common.service.collection.model.deposit;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "ListInventoryResRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListInventoryResRes  implements Serializable{

   private String id;

   private  String    SLSJ;  //受理时间 

   private  String    QCNY;  //清册年月 

   private  String    YWWD;  //业务网点 

   private  String    QCQRDH;  //清册确认单号 

   private  String    DWMC;  //单位名称 

   private  String    CZY;  //操作员 

   private  String    DWZH;  //单位账号

   private String FSRS; //发生人数

   private String FSE;  //发生额

   private boolean SFYC; //是否异常

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public  String getSLSJ(){

       return this.SLSJ;   

   }


   public  void setSLSJ(String  SLSJ){ 

       this.SLSJ = SLSJ;   

   }


   public  String getQCNY(){ 

       return this.QCNY;   

   }


   public  void setQCNY(String  QCNY){ 

       this.QCNY = QCNY;   

   }


   public  String getYWWD(){ 

       return this.YWWD;   

   }


   public  void setYWWD(String  YWWD){ 

       this.YWWD = YWWD;   

   }


   public  String getQCQRDH(){ 

       return this.QCQRDH;   

   }


   public  void setQCQRDH(String  QCQRDH){ 

       this.QCQRDH = QCQRDH;   

   }


   public  String getDWMC(){ 

       return this.DWMC;   

   }


   public  void setDWMC(String  DWMC){ 

       this.DWMC = DWMC;   

   }


   public  String getCZY(){ 

       return this.CZY;   

   }


   public  void setCZY(String  CZY){ 

       this.CZY = CZY;   

   }


   public  String getDWZH(){ 

       return this.DWZH;   

   }


   public  void setDWZH(String  DWZH){ 

       this.DWZH = DWZH;   

   }

    public String getFSRS() {
        return FSRS;
    }

    public void setFSRS(String FSRS) {
        this.FSRS = FSRS;
    }

    public String getFSE() {
        return FSE;
    }

    public void setFSE(String FSE) {
        this.FSE = FSE;
    }

    public boolean isSFYC() {
        return SFYC;
    }

    public void setSFYC(boolean SFYC) {
        this.SFYC = SFYC;
    }

    public String toString(){

       return "ListInventoryResRes{" +  
 
              "SLSJ='" + this.SLSJ + '\'' + "," +
              "QCNY='" + this.QCNY + '\'' + "," +
              "YWWD='" + this.YWWD + '\'' + "," +
              "QCQRDH='" + this.QCQRDH + '\'' + "," +
              "DWMC='" + this.DWMC + '\'' + "," +
              "CZY='" + this.CZY + '\'' + "," +
              "DWZH='" + this.DWZH + '\'' + 

      "}";

  } 
}