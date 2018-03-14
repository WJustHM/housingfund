package com.handge.housingfund.common.service.collection.model.deposit;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "ListUnitPayCallResRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListUnitPayCallResRes  implements Serializable{

   private String id;

   private  String    YWLSH;  //业务流水号 

   private  String    JBRXM;  //经办人姓名 

   private  String YHJNY;  //应缴年月

   private  String    FSE;  //发生额

   private  Integer    FSRS;  //发生人数 

   private  String    SDCJ;  //手动催缴次数 

   private  String    ZDCJ;  //自动催缴 

   private  String    DWMC;  //单位名称 

   private  String    DWZH;  //单位账号 

   private  String    JBRSJHM;  //经办人手机号码 

   private  String    JBRGDDH;  //经办人固定电话 

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public  String getYWLSH(){

       return this.YWLSH;   

   }


   public  void setYWLSH(String  YWLSH){ 

       this.YWLSH = YWLSH;   

   }


   public  String getJBRXM(){ 

       return this.JBRXM;   

   }


   public  void setJBRXM(String  JBRXM){ 

       this.JBRXM = JBRXM;   

   }


   public  String getYHJNY(){

       return this.YHJNY;

   }


   public  void setYHJNY(String YHJNY){

       this.YHJNY = YHJNY;

   }


   public  String getFSE(){

       return this.FSE;   

   }


   public  void setFSE(String  FSE){

       this.FSE = FSE;   

   }


   public  Integer getFSRS(){ 

       return this.FSRS;   

   }


   public  void setFSRS(Integer  FSRS){ 

       this.FSRS = FSRS;   

   }


   public  String getSDCJ(){ 

       return this.SDCJ;   

   }


   public  void setSDCJ(String  SDCJ){ 

       this.SDCJ = SDCJ;   

   }


   public  String getZDCJ(){ 

       return this.ZDCJ;   

   }


   public  void setZDCJ(String  ZDCJ){ 

       this.ZDCJ = ZDCJ;   

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


   public  String getJBRSJHM(){ 

       return this.JBRSJHM;   

   }


   public  void setJBRSJHM(String  JBRSJHM){ 

       this.JBRSJHM = JBRSJHM;   

   }


   public  String getJBRGDDH(){ 

       return this.JBRGDDH;   

   }


   public  void setJBRGDDH(String  JBRGDDH){ 

       this.JBRGDDH = JBRGDDH;   

   }


   public String toString(){ 

       return "ListUnitPayCallResRes{" +  
 
              "YWLSH='" + this.YWLSH + '\'' + "," +
              "JBRXM='" + this.JBRXM + '\'' + "," +
              "YHJNY='" + this.YHJNY + '\'' + "," +
              "FSE='" + this.FSE + '\'' + "," +
              "FSRS='" + this.FSRS + '\'' + "," +
              "SDCJ='" + this.SDCJ + '\'' + "," +
              "ZDCJ='" + this.ZDCJ + '\'' + "," +
              "DWMC='" + this.DWMC + '\'' + "," +
              "DWZH='" + this.DWZH + '\'' + "," +
              "JBRSJHM='" + this.JBRSJHM + '\'' + "," +
              "JBRGDDH='" + this.JBRGDDH + '\'' + 

      "}";

  } 
}