package com.handge.housingfund.common.service.collection.allochthonous.model;

import com.handge.housingfund.common.service.util.*;

import java.util.*;
import java.io.*;
import javax.xml.bind.annotation.*;




@SuppressWarnings({"WeakerAccess", "unused", "SpellCheckingInspection","serial"})
@XmlRootElement(name = "TransferIntoListModleResults")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransferIntoListModleResults  implements Serializable{
  

  private String YWLSH;//业务流水号

  private String LXHBH;//联系函编号

  private String ZGXM;//职工姓名

  private String ZJHM;//证件号码

  private String SJHM;//手机号码

  private String ZCGJJZXMC;//转出公积金中心名称

  private String YGRZFGJJZH;//原个人住房公积金账号

  private String YDWMC;//原单位名称

  private String XGRZFGJJZH;//现个人住房公积金账号

  private String XDWMC;//现单位名称

  private String LXDSCRQ;//联系单生成日期

  private String ZhuangTai;//状态 (0：联系函新建 1：联系函已录未审 2：联系函审核失败 3：联系函审核通过 4：确认接收联系函 5：驳回联系函)

  @SuppressWarnings("unused")
  private void defaultConstructor(){

      this.setYWLSH(null/*业务流水号*/);
      this.setLXHBH(null/*联系函编号*/);
      this.setZGXM(null/*职工姓名*/);
      this.setZJHM(null/*证件号码*/);
      this.setSJHM(null/*手机号码*/);
      this.setZCGJJZXMC(null/*转出公积金中心名称*/);
      this.setYGRZFGJJZH(null/*原个人住房公积金账号*/);
      this.setYDWMC(null/*原单位名称*/);
      this.setXGRZFGJJZH(null/*现个人住房公积金账号*/);
      this.setXDWMC(null/*现单位名称*/);
      this.setLXDSCRQ(null/*联系单生成日期*/);
      this.setZhuangTai(null/*状态 (0：联系函新建 1：联系函已录未审 2：联系函审核失败 3：联系函审核通过 4：确认接收联系函 5：驳回联系函)*/);
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
   * 手机号码
   **/
  public String getSJHM() { return SJHM;}

  public void setSJHM(String SJHM) {this.SJHM = SJHM;}

  /**
   * 转出公积金中心名称
   **/
  public String getZCGJJZXMC() { return ZCGJJZXMC;}

  public void setZCGJJZXMC(String ZCGJJZXMC) {this.ZCGJJZXMC = ZCGJJZXMC;}

  /**
   * 原个人住房公积金账号
   **/
  public String getYGRZFGJJZH() { return YGRZFGJJZH;}

  public void setYGRZFGJJZH(String YGRZFGJJZH) {this.YGRZFGJJZH = YGRZFGJJZH;}

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
   * 联系单生成日期
   **/
  public String getLXDSCRQ() { return LXDSCRQ;}

  public void setLXDSCRQ(String LXDSCRQ) {this.LXDSCRQ = LXDSCRQ;}

  /**
   * 状态 (0：联系函新建 1：联系函已录未审 2：联系函审核失败 3：联系函审核通过 4：确认接收联系函 5：驳回联系函)
   **/
  public String getZhuangTai() { return ZhuangTai;}

  public void setZhuangTai(String ZhuangTai) {this.ZhuangTai = ZhuangTai;}


  @Override
  public String toString()  {

      @SuppressWarnings("StringBufferReplaceableByString")
      StringBuilder sb = new StringBuilder();
      sb.append("class TransferIntoListModleResults {\n");
      sb.append("YWLSH:").append(YWLSH).append("\n");
      sb.append("LXHBH:").append(LXHBH).append("\n");
      sb.append("ZGXM:").append(ZGXM).append("\n");
      sb.append("ZJHM:").append(ZJHM).append("\n");
      sb.append("SJHM:").append(SJHM).append("\n");
      sb.append("ZCGJJZXMC:").append(ZCGJJZXMC).append("\n");
      sb.append("YGRZFGJJZH:").append(YGRZFGJJZH).append("\n");
      sb.append("YDWMC:").append(YDWMC).append("\n");
      sb.append("XGRZFGJJZH:").append(XGRZFGJJZH).append("\n");
      sb.append("XDWMC:").append(XDWMC).append("\n");
      sb.append("LXDSCRQ:").append(LXDSCRQ).append("\n");
      sb.append("ZhuangTai:").append(ZhuangTai).append("\n");
      sb.append("}\n");
      return sb.toString();

  }

  public void checkValidation(){
      
  }
}
