package com.handge.housingfund.common.service.collection.model.unit;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "AutoUnitAcctActionResDWGJXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class AutoUnitAcctActionResDWGJXX  implements Serializable{

   private  String    JBRZJLX;  //经办人证件类型 

   private  String    YWLSH;  //业务流水号 

   private  String    DWFRDBXM;  //单位法人代表姓名 

   private  String    DWFRDBZJHM;  //单位法人代表证件号码 

   private  String    JBRXM;  //经办人姓名 

   private  String    DJSYYZ;  //登记使用印章 

   private  String    JBRGDDHHM;  //经办人固定电话号码 

   private  String    DWLB;  //单位类别 

   private  String    JBRSJHM;  //经办人手机号码 

   private  String    ZZJGDM;  //组织机构代码 

   private  String    JBRZJHM;  //经办人证件号码 

   private  String    DWMC;  //单位名称 

   private  String    DWZH;  //单位账号

    private String JZNY; //缴至年月

   private String BLZL; //办理资料

   public  String getJBRZJLX(){ 

       return this.JBRZJLX;   

   }


   public  void setJBRZJLX(String  JBRZJLX){ 

       this.JBRZJLX = JBRZJLX;   

   }


   public  String getYWLSH(){ 

       return this.YWLSH;   

   }


   public  void setYWLSH(String  YWLSH){ 

       this.YWLSH = YWLSH;   

   }


   public  String getDWFRDBXM(){ 

       return this.DWFRDBXM;   

   }


   public  void setDWFRDBXM(String  DWFRDBXM){ 

       this.DWFRDBXM = DWFRDBXM;   

   }


   public  String getDWFRDBZJHM(){ 

       return this.DWFRDBZJHM;   

   }


   public  void setDWFRDBZJHM(String  DWFRDBZJHM){ 

       this.DWFRDBZJHM = DWFRDBZJHM;   

   }


   public  String getJBRXM(){ 

       return this.JBRXM;   

   }


   public  void setJBRXM(String  JBRXM){ 

       this.JBRXM = JBRXM;   

   }


   public  String getDJSYYZ(){ 

       return this.DJSYYZ;   

   }


   public  void setDJSYYZ(String  DJSYYZ){ 

       this.DJSYYZ = DJSYYZ;   

   }


   public  String getJBRGDDHHM(){ 

       return this.JBRGDDHHM;   

   }


   public  void setJBRGDDHHM(String  JBRGDDHHM){ 

       this.JBRGDDHHM = JBRGDDHHM;   

   }


   public  String getDWLB(){ 

       return this.DWLB;   

   }


   public  void setDWLB(String  DWLB){ 

       this.DWLB = DWLB;   

   }


   public  String getJBRSJHM(){ 

       return this.JBRSJHM;   

   }


   public  void setJBRSJHM(String  JBRSJHM){ 

       this.JBRSJHM = JBRSJHM;   

   }


   public  String getZZJGDM(){ 

       return this.ZZJGDM;   

   }


   public  void setZZJGDM(String  ZZJGDM){ 

       this.ZZJGDM = ZZJGDM;   

   }


   public  String getJBRZJHM(){ 

       return this.JBRZJHM;   

   }


   public  void setJBRZJHM(String  JBRZJHM){ 

       this.JBRZJHM = JBRZJHM;   

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

    public String getJZNY() {
        return JZNY;
    }

    public void setJZNY(String JZNY) {
        this.JZNY = JZNY;
    }

    public String getBLZL() {
        return BLZL;
    }

    public void setBLZL(String BLZL) {
        this.BLZL = BLZL;
    }

    @Override
    public String toString() {
        return "AutoUnitAcctActionResDWGJXX{" +
                "JBRZJLX='" + JBRZJLX + '\'' +
                ", YWLSH='" + YWLSH + '\'' +
                ", DWFRDBXM='" + DWFRDBXM + '\'' +
                ", DWFRDBZJHM='" + DWFRDBZJHM + '\'' +
                ", JBRXM='" + JBRXM + '\'' +
                ", DJSYYZ='" + DJSYYZ + '\'' +
                ", JBRGDDHHM='" + JBRGDDHHM + '\'' +
                ", DWLB='" + DWLB + '\'' +
                ", JBRSJHM='" + JBRSJHM + '\'' +
                ", ZZJGDM='" + ZZJGDM + '\'' +
                ", JBRZJHM='" + JBRZJHM + '\'' +
                ", DWMC='" + DWMC + '\'' +
                ", DWZH='" + DWZH + '\'' +
                ", JZNY='" + JZNY + '\'' +
                '}';
    }
}