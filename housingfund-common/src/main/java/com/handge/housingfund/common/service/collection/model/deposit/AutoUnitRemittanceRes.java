package com.handge.housingfund.common.service.collection.model.deposit;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "AutoUnitRemittanceRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class AutoUnitRemittanceRes  implements Serializable{

   private  String    JBRZJLX;  //经办人证件类型 

   private  String    JBRXM;  //经办人姓名 

   private  String    YWWD;  //业务网点 

   private  String    DWZHYE;  //单位账户余额

   private  String    ZSYE;  //暂收余额

   private  String    JZNY;  //缴至年月 

   private  String JBRZJHM;  //经办人证件号码

   private  Integer    DWFCRS;  //单位封存人数 

   private  String    DWMC;  //单位名称 

   private  String    DWYHJNY;  //单位应汇缴年月 

   private  String    DWZH;  //单位账号 

   private  String    CZY;  //操作员 

   private  Integer    DWJCRS;  //单位缴存人数

   private String STYHMC;   //受托银行名称

   private String STYHDM;   //受托银行代码

   private String STYHZH;   //

    public String getSTYHMC() {
        return STYHMC;
    }

    public void setSTYHMC(String STYHMC) {
        this.STYHMC = STYHMC;
    }

    public String getSTYHDM() {
        return STYHDM;
    }

    public void setSTYHDM(String STYHDM) {
        this.STYHDM = STYHDM;
    }

    public String getSTYHZH() {
        return STYHZH;
    }

    public void setSTYHZH(String STYHZH) {
        this.STYHZH = STYHZH;
    }

    public  String getJBRZJLX(){

       return this.JBRZJLX;   

   }


   public  void setJBRZJLX(String  JBRZJLX){ 

       this.JBRZJLX = JBRZJLX;   

   }


   public  String getJBRXM(){ 

       return this.JBRXM;   

   }


   public  void setJBRXM(String  JBRXM){ 

       this.JBRXM = JBRXM;   

   }


   public  String getYWWD(){ 

       return this.YWWD;   

   }


   public  void setYWWD(String  YWWD){ 

       this.YWWD = YWWD;   

   }


   public  String getDWZHYE(){

       return this.DWZHYE;   

   }


   public  void setDWZHYE(String  DWZHYE){

       this.DWZHYE = DWZHYE;   

   }


   public  String getZSYE(){

       return this.ZSYE;   

   }


   public  void setZSYE(String  ZSYE){

       this.ZSYE = ZSYE;   

   }


   public  String getJZNY(){ 

       return this.JZNY;   

   }


   public  void setJZNY(String  JZNY){ 

       this.JZNY = JZNY;   

   }


   public  String getJBRZJHM(){

       return this.JBRZJHM;

   }


   public  void setJBRZJHM(String JBRZJHM){

       this.JBRZJHM = JBRZJHM;

   }


   public  Integer getDWFCRS(){ 

       return this.DWFCRS;   

   }


   public  void setDWFCRS(Integer  DWFCRS){ 

       this.DWFCRS = DWFCRS;   

   }


   public  String getDWMC(){ 

       return this.DWMC;   

   }


   public  void setDWMC(String  DWMC){ 

       this.DWMC = DWMC;   

   }


   public  String getDWYHJNY(){ 

       return this.DWYHJNY;   

   }


   public  void setDWYHJNY(String  DWYHJNY){ 

       this.DWYHJNY = DWYHJNY;   

   }


   public  String getDWZH(){ 

       return this.DWZH;   

   }


   public  void setDWZH(String  DWZH){ 

       this.DWZH = DWZH;   

   }


   public  String getCZY(){ 

       return this.CZY;   

   }


   public  void setCZY(String  CZY){ 

       this.CZY = CZY;   

   }


   public  Integer getDWJCRS(){ 

       return this.DWJCRS;   

   }


   public  void setDWJCRS(Integer  DWJCRS){ 

       this.DWJCRS = DWJCRS;   

   }

    @Override
    public String toString() {
        return "AutoUnitRemittanceRes{" +
                "JBRZJLX='" + JBRZJLX + '\'' +
                ", JBRXM='" + JBRXM + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", DWZHYE=" + DWZHYE +
                ", ZSYE=" + ZSYE +
                ", JZNY='" + JZNY + '\'' +
                ", JBRZJHM='" + JBRZJHM + '\'' +
                ", DWFCRS=" + DWFCRS +
                ", DWMC='" + DWMC + '\'' +
                ", DWYHJNY='" + DWYHJNY + '\'' +
                ", DWZH='" + DWZH + '\'' +
                ", CZY='" + CZY + '\'' +
                ", DWJCRS=" + DWJCRS +
                ", STYHMC='" + STYHMC + '\'' +
                ", STYHDM='" + STYHDM + '\'' +
                ", STYHZH='" + STYHZH + '\'' +
                '}';
    }
}