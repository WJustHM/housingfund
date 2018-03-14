package com.handge.housingfund.common.service.collection.allochthonous.model;

import com.handge.housingfund.common.service.util.*;

import java.util.*;
import java.io.*;
import javax.xml.bind.annotation.*;




@SuppressWarnings({"WeakerAccess", "unused", "SpellCheckingInspection","serial"})
@XmlRootElement(name = "TransferOutDetailsModleTurnOutMechanismMsg")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransferOutDetailsModleTurnOutMechanismMsg  implements Serializable{
  

  private String ZCJGBH;//转出机构编号

  private String ZCGJJZXMC;//转出公积金中心名称

  private String YGRZFGJJZH;//原个人住房公积金账号

  private String YDWMC;//原单位名称

  private String FKYHMC;//付款银行名称

  private String FKZH;//付款账号

  private String FKHM;//付款户名

  @SuppressWarnings("unused")
  private void defaultConstructor(){

      this.setZCJGBH(null/*转出机构编号*/);
      this.setZCGJJZXMC(null/*转出公积金中心名称*/);
      this.setYGRZFGJJZH(null/*原个人住房公积金账号*/);
      this.setYDWMC(null/*原单位名称*/);
      this.setFKYHMC(null/*付款银行名称*/);
      this.setFKZH(null/*付款账号*/);
      this.setFKHM(null/*付款户名*/);
  }

  /**
   * 转出机构编号
   **/
  public String getZCJGBH() { return ZCJGBH;}

  public void setZCJGBH(String ZCJGBH) {this.ZCJGBH = ZCJGBH;}

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
   * 付款银行名称
   **/
  public String getFKYHMC() { return FKYHMC;}

  public void setFKYHMC(String FKYHMC) {this.FKYHMC = FKYHMC;}

  /**
   * 付款账号
   **/
  public String getFKZH() { return FKZH;}

  public void setFKZH(String FKZH) {this.FKZH = FKZH;}

  /**
   * 付款户名
   **/
  public String getFKHM() { return FKHM;}

  public void setFKHM(String FKHM) {this.FKHM = FKHM;}


  @Override
  public String toString()  {

      @SuppressWarnings("StringBufferReplaceableByString")
      StringBuilder sb = new StringBuilder();
      sb.append("class TransferOutDetailsModleTurnOutMechanismMsg {\n");
      sb.append("ZCJGBH:").append(ZCJGBH).append("\n");
      sb.append("ZCGJJZXMC:").append(ZCGJJZXMC).append("\n");
      sb.append("YGRZFGJJZH:").append(YGRZFGJJZH).append("\n");
      sb.append("YDWMC:").append(YDWMC).append("\n");
      sb.append("FKYHMC:").append(FKYHMC).append("\n");
      sb.append("FKZH:").append(FKZH).append("\n");
      sb.append("FKHM:").append(FKHM).append("\n");
      sb.append("}\n");
      return sb.toString();

  }

  public void checkValidation(){
      
  }
}
