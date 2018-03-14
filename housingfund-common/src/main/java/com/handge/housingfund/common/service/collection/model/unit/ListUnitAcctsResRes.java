package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "ListUnitAcctsResRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListUnitAcctsResRes implements Serializable {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
//   private  String    PageSize;  //每页的数据条数
//
//   private  String    NextPageNo;  //下一页的页码
//
//   private  String    CurrentPage;  //当前页码

    private String DWLB;  //单位类别

//   private  String    TotalCount;  //总数据条数
//
//   private  String    PageCount;  //总页数

    private String SLSJ;  //受理时间

    private String DWJBRXM;  //单位经办人姓名

    private String ZZJGDM;   //组织机构代码

    private String XZQY;  //行政区域

    private String DWZHZT;  //单位账号状态

    private String DWMC;  //单位名称

    private String DWZH;  //单位账号

    private String CZY;  //操作员

    private String DWJCBL;//单位缴存比例

    private String GRJCBL;//个人缴存比例

    private String YWWD;

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }
    //   public  String getPageSize(){
//
//       return this.PageSize;
//
//   }
//
//
//   public  void setPageSize(String  PageSize){
//
//       this.PageSize = PageSize;
//
//   }
//
//
//   public  String getNextPageNo(){
//
//       return this.NextPageNo;
//
//   }
//
//
//   public  void setNextPageNo(String  NextPageNo){
//
//       this.NextPageNo = NextPageNo;
//
//   }
//
//
//   public  String getCurrentPage(){
//
//       return this.CurrentPage;
//
//   }
//
//
//   public  void setCurrentPage(String  CurrentPage){
//
//       this.CurrentPage = CurrentPage;
//
//   }
//

    public String getDWLB() {

        return this.DWLB;

    }


    public void setDWLB(String DWLB) {

        this.DWLB = DWLB;

    }


//   public  String getTotalCount(){
//
//       return this.TotalCount;
//
//   }
//
//
//   public  void setTotalCount(String  TotalCount){
//
//       this.TotalCount = TotalCount;
//
//   }
//
//
//   public  String getPageCount(){
//
//       return this.PageCount;
//
//   }


//   public  void setPageCount(String  PageCount){
//
//       this.PageCount = PageCount;
//
//   }


    public String getSLSJ() {

        return this.SLSJ;

    }


    public void setSLSJ(String SLSJ) {

        this.SLSJ = SLSJ;

    }


    public String getDWJBRXM() {

        return this.DWJBRXM;

    }


    public void setDWJBRXM(String DWJBRXM) {

        this.DWJBRXM = DWJBRXM;

    }


    public String getXZQY() {

        return this.XZQY;

    }


    public void setXZQY(String XZQY) {

        this.XZQY = XZQY;

    }


    public String getDWZHZT() {

        return this.DWZHZT;

    }


    public void setDWZHZT(String DWZHZT) {

        this.DWZHZT = DWZHZT;

    }


    public String getDWMC() {

        return this.DWMC;

    }


    public void setDWMC(String DWMC) {

        this.DWMC = DWMC;

    }


    public String getDWZH() {

        return this.DWZH;

    }


    public void setDWZH(String DWZH) {

        this.DWZH = DWZH;

    }


    public String getCZY() {

        return this.CZY;

    }


    public void setCZY(String CZY) {

        this.CZY = CZY;

    }

    public String getDWJCBL() {
        return DWJCBL;
    }

    public void setDWJCBL(String DWJCBL) {
        this.DWJCBL = DWJCBL;
    }

    public String getGRJCBL() {
        return GRJCBL;
    }

    public void setGRJCBL(String GRJCBL) {
        this.GRJCBL = GRJCBL;
    }

    public String getZZJGDM() {
        return ZZJGDM;
    }

    public void setZZJGDM(String ZZJGDM) {
        this.ZZJGDM = ZZJGDM;
    }

    @Override
    public String toString() {
        return "ListUnitAcctsResRes{" +
                "DWLB='" + DWLB + '\'' +
                ", SLSJ='" + SLSJ + '\'' +
                ", DWJBRXM='" + DWJBRXM + '\'' +
                ", ZZJGDM='" + ZZJGDM + '\'' +
                ", XZQY='" + XZQY + '\'' +
                ", DWZHZT='" + DWZHZT + '\'' +
                ", DWMC='" + DWMC + '\'' +
                ", DWZH='" + DWZH + '\'' +
                ", CZY='" + CZY + '\'' +
                ", DWJCBL='" + DWJCBL + '\'' +
                ", GRJCBL='" + GRJCBL + '\'' +
                '}';
    }
}