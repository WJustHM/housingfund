package com.handge.housingfund.common.service.collection.allochthonous.model;

import com.handge.housingfund.common.service.util.*;

import java.util.*;
import java.io.*;
import javax.xml.bind.annotation.*;




@SuppressWarnings({"WeakerAccess", "unused", "SpellCheckingInspection","serial"})
@XmlRootElement(name = "TransferOutAuditedListModleResults")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransferOutAuditedListModleResults  implements Serializable{
  

  private String YWLSH;//业务流水号

  private String LXHBH;//联系函编号

  private String ZGXM;//职工姓名

  private String ZJHM;//证件号码

  private String ZhuangTai;//状态

  private String ZRGJJZXMC;//转入公积金中心名称

  private String YDWMC;//原单位名称

  private String XGRZFGJJZH;//现个人住房公积金账号

  private String XDWMC;//现单位名称

  private String SLSJ;//受理时间

  private String SCSHY;//上次审核员

    private String CZY;

    private String YWWD;

    public String getCZY() {
        return CZY;
    }

    public void setCZY(String CZY) {
        this.CZY = CZY;
    }

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    @Override
    public String toString() {
        return "TransferOutAuditedListModleResults{" +
                "YWLSH='" + YWLSH + '\'' +
                ", LXHBH='" + LXHBH + '\'' +
                ", ZGXM='" + ZGXM + '\'' +
                ", ZJHM='" + ZJHM + '\'' +
                ", ZhuangTai='" + ZhuangTai + '\'' +
                ", ZRGJJZXMC='" + ZRGJJZXMC + '\'' +
                ", YDWMC='" + YDWMC + '\'' +
                ", XGRZFGJJZH='" + XGRZFGJJZH + '\'' +
                ", XDWMC='" + XDWMC + '\'' +
                ", SLSJ='" + SLSJ + '\'' +
                ", SCSHY='" + SCSHY + '\'' +
                ", CZY='" + CZY + '\'' +
                ", YWWD='" + YWWD + '\'' +
                '}';
    }

    @SuppressWarnings("unused")
  private void defaultConstructor(){

      this.setYWLSH(null/*业务流水号*/);
      this.setLXHBH(null/*联系函编号*/);
      this.setZGXM(null/*职工姓名*/);
      this.setZJHM(null/*证件号码*/);
      this.setZhuangTai(null/*状态*/);
      this.setZRGJJZXMC(null/*转入公积金中心名称*/);
      this.setYDWMC(null/*原单位名称*/);
      this.setXGRZFGJJZH(null/*现个人住房公积金账号*/);
      this.setXDWMC(null/*现单位名称*/);
      this.setSLSJ(null/*受理时间*/);
      this.setSCSHY(null/*上次审核员*/);
  }

  /**
   * 业务流水号
   **/
  public String getYWLSH() { return YWLSH;}

  public void setYWLSH(String YWLSH) {this.YWLSH = YWLSH;}

  /**
   * 联系函编号
   **/
  public String getLXHBH() { return LXHBH;}

  public void setLXHBH(String LXHBH) {this.LXHBH = LXHBH;}

  /**
   * 职工姓名
   **/
  public String getZGXM() { return ZGXM;}

  public void setZGXM(String ZGXM) {this.ZGXM = ZGXM;}

  /**
   * 证件号码
   **/
  public String getZJHM() { return ZJHM;}

  public void setZJHM(String ZJHM) {this.ZJHM = ZJHM;}

  /**
   * 状态
   **/
  public String getZhuangTai() { return ZhuangTai;}

  public void setZhuangTai(String ZhuangTai) {this.ZhuangTai = ZhuangTai;}

  /**
   * 转入公积金中心名称
   **/
  public String getZRGJJZXMC() { return ZRGJJZXMC;}

  public void setZRGJJZXMC(String ZRGJJZXMC) {this.ZRGJJZXMC = ZRGJJZXMC;}

  /**
   * 原单位名称
   **/
  public String getYDWMC() { return YDWMC;}

  public void setYDWMC(String YDWMC) {this.YDWMC = YDWMC;}

  /**
   * 现个人住房公积金账号
   **/
  public String getXGRZFGJJZH() { return XGRZFGJJZH;}

  public void setXGRZFGJJZH(String XGRZFGJJZH) {this.XGRZFGJJZH = XGRZFGJJZH;}

  /**
   * 现单位名称
   **/
  public String getXDWMC() { return XDWMC;}

  public void setXDWMC(String XDWMC) {this.XDWMC = XDWMC;}

  /**
   * 受理时间
   **/
  public String getSLSJ() { return SLSJ;}

  public void setSLSJ(String SLSJ) {this.SLSJ = SLSJ;}

  /**
   * 上次审核员
   **/
  public String getSCSHY() { return SCSHY;}

  public void setSCSHY(String SCSHY) {this.SCSHY = SCSHY;}


    public void checkValidation(){
      
  }
}
