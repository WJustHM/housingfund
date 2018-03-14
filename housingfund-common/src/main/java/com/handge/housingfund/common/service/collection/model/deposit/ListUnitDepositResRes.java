package com.handge.housingfund.common.service.collection.model.deposit;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "ListUnitDepositResRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListUnitDepositResRes implements Serializable{

   private  String    GRJCBL;  //个人缴存比例 

   private  String    ZZJGDM;  //组织机构代码 

   private  String    JZNY;  //缴至年月 

   private  String   DWJCBL;  //单位缴存比例

   private  String    DWZHYE;  //单位账户余额 

   private  String    DWMC;  //单位名称 

   private  String    DWZH;  //单位账号

   private  String    WFTYE;  //未分摊余额

   private String DWZHZT;   //单位账户状态

    public String getKHWD() {
        return KHWD;
    }

    public void setKHWD(String KHWD) {
        this.KHWD = KHWD;
    }

    private String KHWD;    //开户网点
    public String getWFTYE() {
        return WFTYE;
    }

    public void setWFTYE(String WFTYE) {
        this.WFTYE = WFTYE;
    }

    public  String getGRJCBL(){

       return this.GRJCBL;   

   }


   public  void setGRJCBL(String  GRJCBL){ 

       this.GRJCBL = GRJCBL;   

   }

    public String getDWZHZT() {
        return DWZHZT;
    }

    public void setDWZHZT(String DWZHZT) {
        this.DWZHZT = DWZHZT;
    }

    public  String getZZJGDM(){

       return this.ZZJGDM;   

   }


   public  void setZZJGDM(String  ZZJGDM){ 

       this.ZZJGDM = ZZJGDM;   

   }


   public  String getJZNY(){ 

       return this.JZNY;   

   }


   public  void setJZNY(String  JZNY){ 

       this.JZNY = JZNY;   

   }


   public  String getDWJCBL(){

       return this.DWJCBL;   

   }


   public  void setDWJCBL(String  DWJCBL){

       this.DWJCBL = DWJCBL;   

   }


   public  String getDWZHYE(){ 

       return this.DWZHYE;   

   }


   public  void setDWZHYE(String  DWZHYE){ 

       this.DWZHYE = DWZHYE;   

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
        return "ListUnitDepositResRes{" +
                "GRJCBL='" + GRJCBL + '\'' +
                ", ZZJGDM='" + ZZJGDM + '\'' +
                ", JZNY='" + JZNY + '\'' +
                ", DWJCBL='" + DWJCBL + '\'' +
                ", DWZHYE='" + DWZHYE + '\'' +
                ", DWMC='" + DWMC + '\'' +
                ", DWZH='" + DWZH + '\'' +
                ", WFTYE='" + WFTYE + '\'' +
                '}';
    }
}