package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "GetUnitAcctInfoDetailDWGJXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetUnitAcctInfoDetailDWGJXX  implements Serializable{

   private  String    PZJGMC;  //批准机关名称 

   private  String    DWFRDBZJHM;  //单位法人代表证件号码 

   private  String    DWJJLX;  //注册登记类型 

   private  String    DWFRDBXM;  //单位法人代表姓名 

   private  String    DWXZQY;  //单位行政区域 

   private  String    DJSYYZ;  //登记使用印章 

   private  String    DWLB;  //单位类别 

   private  String    DWFRDBZJLX;  //单位法人代表证件类型 

   private  String    PZJGJB;  //批准机关级别 

   private  String    KGQK;  //控股情况 

   private  String    DJZCH;  //登记注册号 

   private  String    ZZJGDM;  //组织机构代码 

   private  String    DWLSGX;  //单位隶属关系 

   private  String    DWDZ;  //单位地址 

   private  String    DWMC;  //单位名称 

   private  String    DWZH;  //单位账号 

   private  String    DWSSHY;  //单位所属行业

   private  String    DWZHZT;  //单位账号状态

   public String getDWZHZT() {

        return DWZHZT;
   }

   public void setDWZHZT(String DWZHZT) {

        this.DWZHZT = DWZHZT;
   }

   public  String getPZJGMC(){ 

       return this.PZJGMC;   

   }


   public  void setPZJGMC(String  PZJGMC){ 

       this.PZJGMC = PZJGMC;   

   }


   public  String getDWFRDBZJHM(){ 

       return this.DWFRDBZJHM;   

   }


   public  void setDWFRDBZJHM(String  DWFRDBZJHM){ 

       this.DWFRDBZJHM = DWFRDBZJHM;   

   }

   public  String getDWJJLX(){ 

       return this.DWJJLX;   

   }


   public  void setDWJJLX(String  DWJJLX){ 

       this.DWJJLX = DWJJLX;   

   }


   public  String getDWFRDBXM(){ 

       return this.DWFRDBXM;   

   }


   public  void setDWFRDBXM(String  DWFRDBXM){ 

       this.DWFRDBXM = DWFRDBXM;   

   }


   public  String getDWXZQY(){ 

       return this.DWXZQY;   

   }


   public  void setDWXZQY(String  DWXZQY){ 

       this.DWXZQY = DWXZQY;   

   }


   public  String getDJSYYZ(){ 

       return this.DJSYYZ;   

   }


   public  void setDJSYYZ(String  DJSYYZ){ 

       this.DJSYYZ = DJSYYZ;   

   }


   public  String getDWLB(){ 

       return this.DWLB;   

   }


   public  void setDWLB(String  DWLB){ 

       this.DWLB = DWLB;   

   }


   public  String getDWFRDBZJLX(){ 

       return this.DWFRDBZJLX;   

   }


   public  void setDWFRDBZJLX(String  DWFRDBZJLX){ 

       this.DWFRDBZJLX = DWFRDBZJLX;   

   }


   public  String getPZJGJB(){ 

       return this.PZJGJB;   

   }


   public  void setPZJGJB(String  PZJGJB){ 

       this.PZJGJB = PZJGJB;   

   }


   public  String getKGQK(){ 

       return this.KGQK;   

   }


   public  void setKGQK(String  KGQK){ 

       this.KGQK = KGQK;   

   }


   public  String getDJZCH(){ 

       return this.DJZCH;   

   }


   public  void setDJZCH(String  DJZCH){ 

       this.DJZCH = DJZCH;   

   }


   public  String getZZJGDM(){ 

       return this.ZZJGDM;   

   }


   public  void setZZJGDM(String  ZZJGDM){ 

       this.ZZJGDM = ZZJGDM;   

   }


   public  String getDWLSGX(){ 

       return this.DWLSGX;   

   }


   public  void setDWLSGX(String  DWLSGX){ 

       this.DWLSGX = DWLSGX;   

   }


   public  String getDWDZ(){ 

       return this.DWDZ;   

   }


   public  void setDWDZ(String  DWDZ){ 

       this.DWDZ = DWDZ;   

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


   public  String getDWSSHY(){ 

       return this.DWSSHY;   

   }


   public  void setDWSSHY(String  DWSSHY){ 

       this.DWSSHY = DWSSHY;   

   }


   public String toString(){ 

       return "GetUnitAcctInfoDetailDWGJXX{" +  
 
              "PZJGMC='" + this.PZJGMC + '\'' + "," +
              "DWFRDBZJHM='" + this.DWFRDBZJHM + '\'' + "," +
              "DWJJLX='" + this.DWJJLX + '\'' + "," +
              "DWFRDBXM='" + this.DWFRDBXM + '\'' + "," +
              "DWXZQY='" + this.DWXZQY + '\'' + "," +
              "DJSYYZ='" + this.DJSYYZ + '\'' + "," +
              "DWLB='" + this.DWLB + '\'' + "," +
              "DWFRDBZJLX='" + this.DWFRDBZJLX + '\'' + "," +
              "PZJGJB='" + this.PZJGJB + '\'' + "," +
              "KGQK='" + this.KGQK + '\'' + "," +
              "DJZCH='" + this.DJZCH + '\'' + "," +
              "ZZJGDM='" + this.ZZJGDM + '\'' + "," +
              "DWLSGX='" + this.DWLSGX + '\'' + "," +
              "DWDZ='" + this.DWDZ + '\'' + "," +
              "DWMC='" + this.DWMC + '\'' + "," +
              "DWZH='" + this.DWZH + '\'' + "," +
              "DWSSHY='" + this.DWSSHY + '\'' +
              "DWZHZT='" + this.DWZHZT + '\'' +

       "}";

  } 
}