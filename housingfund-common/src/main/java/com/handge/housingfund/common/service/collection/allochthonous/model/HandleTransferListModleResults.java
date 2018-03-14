package com.handge.housingfund.common.service.collection.allochthonous.model;

import com.handge.housingfund.common.service.util.*;

import java.util.*;
import java.io.*;
import javax.xml.bind.annotation.*;




@SuppressWarnings({"WeakerAccess", "unused", "SpellCheckingInspection","serial"})
@XmlRootElement(name = "HandleTransferListModleResults")
@XmlAccessorType(XmlAccessType.FIELD)
public class HandleTransferListModleResults  implements Serializable{
  

  private String YWLSH;//业务流水号

  private String LXHBH;//联系函编号

  private String ZGXM;//职工姓名

  private String ZJHM;//证件号码

  private String ZhuangTai;//状态

  private String ZCFKXX;//转出反馈信息

  private String ZCGJJZXMC;//转出公积金中心名称

  private String ZYJE;//转移金额

  private String XGZDWMC;//现工作单位名称

  private String XGRZFGJJZH;//现个人住房公积金账号

  private String LXDSCRQ;//联系单生成日期

  @SuppressWarnings("unused")
  private void defaultConstructor(){

      this.setYWLSH(null/*业务流水号*/);
      this.setLXHBH(null/*联系函编号*/);
      this.setZGXM(null/*职工姓名*/);
      this.setZJHM(null/*证件号码*/);
      this.setZhuangTai(null/*状态*/);
      this.setZCFKXX(null/*转出反馈信息*/);
      this.setZCGJJZXMC(null/*转出公积金中心名称*/);
      this.setZYJE(null/*转移金额*/);
      this.setXGZDWMC(null/*现工作单位名称*/);
      this.setXGRZFGJJZH(null/*现个人住房公积金账号*/);
      this.setLXDSCRQ(null/*联系单生成日期*/);
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
   * 转出反馈信息
   **/
  public String getZCFKXX() { return ZCFKXX;}

  public void setZCFKXX(String ZCFKXX) {this.ZCFKXX = ZCFKXX;}

  /**
   * 转出公积金中心名称
   **/
  public String getZCGJJZXMC() { return ZCGJJZXMC;}

  public void setZCGJJZXMC(String ZCGJJZXMC) {this.ZCGJJZXMC = ZCGJJZXMC;}

  /**
   * 转移金额
   **/
  public String getZYJE() { return ZYJE;}

  public void setZYJE(String ZYJE) {this.ZYJE = ZYJE;}

  /**
   * 现工作单位名称
   **/
  public String getXGZDWMC() { return XGZDWMC;}

  public void setXGZDWMC(String XGZDWMC) {this.XGZDWMC = XGZDWMC;}

  /**
   * 现个人住房公积金账号
   **/
  public String getXGRZFGJJZH() { return XGRZFGJJZH;}

  public void setXGRZFGJJZH(String XGRZFGJJZH) {this.XGRZFGJJZH = XGRZFGJJZH;}

  /**
   * 联系单生成日期
   **/
  public String getLXDSCRQ() { return LXDSCRQ;}

  public void setLXDSCRQ(String LXDSCRQ) {this.LXDSCRQ = LXDSCRQ;}


  @Override
  public String toString()  {

      @SuppressWarnings("StringBufferReplaceableByString")
      StringBuilder sb = new StringBuilder();
      sb.append("class HandleTransferListModleResults {\n");
      sb.append("YWLSH:").append(YWLSH).append("\n");
      sb.append("LXHBH:").append(LXHBH).append("\n");
      sb.append("ZGXM:").append(ZGXM).append("\n");
      sb.append("ZJHM:").append(ZJHM).append("\n");
      sb.append("ZhuangTai:").append(ZhuangTai).append("\n");
      sb.append("ZCFKXX:").append(ZCFKXX).append("\n");
      sb.append("ZCGJJZXMC:").append(ZCGJJZXMC).append("\n");
      sb.append("ZYJE:").append(ZYJE).append("\n");
      sb.append("XGZDWMC:").append(XGZDWMC).append("\n");
      sb.append("XGRZFGJJZH:").append(XGRZFGJJZH).append("\n");
      sb.append("LXDSCRQ:").append(LXDSCRQ).append("\n");
      sb.append("}\n");
      return sb.toString();

  }

  public void checkValidation(){
      
  }
}
