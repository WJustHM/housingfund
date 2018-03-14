package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "ListUnitAcctSetResRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListUnitAcctSetResRes  implements Serializable{

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
   private  String    YWLSH;  //业务流水号

   private  String    DWLB;  //单位类别

   private  String    SLSJ;  //受理时间 

   private  String    ZZJGDM;  //组织机构代码 

   private  String    ZhuangTai;  //状态 

   private  String    XZQY;  //行政区域 

   private  String    DWMC;  //单位名称 

   private  String    DWZH;  //单位账号 

   private  String    CZY;  //操作员 

   public  String getYWLSH(){ 

       return this.YWLSH;   

   }


   public  void setYWLSH(String  YWLSH){ 

       this.YWLSH = YWLSH;   

   }



   public  String getDWLB(){ 

       return this.DWLB;   

   }


   public  void setDWLB(String  DWLB){ 

       this.DWLB = DWLB;   

   }



   public  String getSLSJ(){ 

       return this.SLSJ;   

   }


   public  void setSLSJ(String  SLSJ){ 

       this.SLSJ = SLSJ;   

   }


   public  String getZZJGDM(){ 

       return this.ZZJGDM;   

   }


   public  void setZZJGDM(String  ZZJGDM){ 

       this.ZZJGDM = ZZJGDM;   

   }


   public  String getZhuangTai(){ 

       return this.ZhuangTai;   

   }


   public  void setZhuangTai(String  ZhuangTai){ 

       this.ZhuangTai = ZhuangTai;   

   }


   public  String getXZQY(){ 

       return this.XZQY;   

   }


   public  void setXZQY(String  XZQY){ 

       this.XZQY = XZQY;   

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


   public  String getCZY(){ 

       return this.CZY;   

   }


   public  void setCZY(String  CZY){ 

       this.CZY = CZY;   

   }


   public String toString(){ 

       return "ListUnitAcctSetResRes{" +
              "YWLSH='" + this.YWLSH + '\'' + "," +
              "DWLB='" + this.DWLB + '\'' + "," +
              "SLSJ='" + this.SLSJ + '\'' + "," +
              "ZZJGDM='" + this.ZZJGDM + '\'' + "," +
              "ZhuangTai='" + this.ZhuangTai + '\'' + "," +
              "XZQY='" + this.XZQY + '\'' + "," +
              "DWMC='" + this.DWMC + '\'' + "," +
              "DWZH='" + this.DWZH + '\'' + "," +
              "CZY='" + this.CZY + '\'' + 

      "}";

  } 
}