package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "ListUnitAcctUnsealedResRes")
@XmlAccessorType(XmlAccessType.FIELD)

public class ListUnitAcctUnsealedResRes  implements Serializable{

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private  String    PageSize;  //每页的数据条数

   private  String    YWLSH;  //业务流水号 

   private  String    NextPageNo;  //下一页的页码 

   private  String    CurrentPage;  //当前页码 

   private  String    DWLB;  //单位类别 

   private  String    TotalCount;  //总数据条数 

   private  String    PageCount;  //总页数 

   private  String    SLSJ;  //受理时间 

   private  String    ZZJGDM;  //组织机构代码 

   private  String    ZhuangTai;  //状态 

   private  String    XZQY;  //行政区域 

   private  String    DWMC;  //单位名称 

   private  String    DWZH;  //单位账号 

   private  String    CZY;  //操作员 

   public  String getPageSize(){ 

       return this.PageSize;   

   }


   public  void setPageSize(String  PageSize){ 

       this.PageSize = PageSize;   

   }


   public  String getYWLSH(){ 

       return this.YWLSH;   

   }


   public  void setYWLSH(String  YWLSH){ 

       this.YWLSH = YWLSH;   

   }


   public  String getNextPageNo(){ 

       return this.NextPageNo;   

   }


   public  void setNextPageNo(String  NextPageNo){ 

       this.NextPageNo = NextPageNo;   

   }


   public  String getCurrentPage(){ 

       return this.CurrentPage;   

   }


   public  void setCurrentPage(String  CurrentPage){ 

       this.CurrentPage = CurrentPage;   

   }


   public  String getDWLB(){ 

       return this.DWLB;   

   }


   public  void setDWLB(String  DWLB){ 

       this.DWLB = DWLB;   

   }


   public  String getTotalCount(){ 

       return this.TotalCount;   

   }


   public  void setTotalCount(String  TotalCount){ 

       this.TotalCount = TotalCount;   

   }


   public  String getPageCount(){ 

       return this.PageCount;   

   }


   public  void setPageCount(String  PageCount){ 

       this.PageCount = PageCount;   

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

       return "ListUnitAcctUnsealedResRes{" +  
 
              "PageSize='" + this.PageSize + '\'' + "," +
              "YWLSH='" + this.YWLSH + '\'' + "," +
              "NextPageNo='" + this.NextPageNo + '\'' + "," +
              "CurrentPage='" + this.CurrentPage + '\'' + "," +
              "DWLB='" + this.DWLB + '\'' + "," +
              "TotalCount='" + this.TotalCount + '\'' + "," +
              "PageCount='" + this.PageCount + '\'' + "," +
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