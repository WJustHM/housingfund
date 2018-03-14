package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "GetPersonRadixResDWGJXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetPersonRadixResDWGJXX  implements Serializable{

   private  String    YWLSH;  //业务流水号 

   private  String    DWJBR;  //单位经办人 

   private  String    FSRS;  //调整人数

   private  String    ZJHM;  //证件号码 

   private  String    ZJLX;  //证件类型 

   private  String    DWMC;  //单位名称 

   private  String    DWZH;  //单位账号 

   public  String getYWLSH(){ 

       return this.YWLSH;   

   }


   public  void setYWLSH(String  YWLSH){ 

       this.YWLSH = YWLSH;   

   }


    public String getFSRS() {
        return FSRS;
    }

    public void setFSRS(String FSRS) {
        this.FSRS = FSRS;
    }

    public  String getDWJBR(){

       return this.DWJBR;   

   }


   public  void setDWJBR(String  DWJBR){ 

       this.DWJBR = DWJBR;   

   }





   public  String getZJHM(){ 

       return this.ZJHM;   

   }


   public  void setZJHM(String  ZJHM){ 

       this.ZJHM = ZJHM;   

   }


   public  String getZJLX(){ 

       return this.ZJLX;   

   }


   public  void setZJLX(String  ZJLX){ 

       this.ZJLX = ZJLX;   

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

    @Override
    public String toString() {
        return "GetPersonRadixResDWGJXX{" +
                "YWLSH='" + YWLSH + '\'' +
                ", DWJBR='" + DWJBR + '\'' +
                ", FSRS='" + FSRS + '\'' +
                ", ZJHM='" + ZJHM + '\'' +
                ", ZJLX='" + ZJLX + '\'' +
                ", DWMC='" + DWMC + '\'' +
                ", DWZH='" + DWZH + '\'' +
                '}';
    }
}