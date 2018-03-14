package com.handge.housingfund.common.service.collection.allochthonous.model;

import com.handge.housingfund.common.service.util.*;

import java.util.*;
import java.io.*;
import javax.xml.bind.annotation.*;




@SuppressWarnings({"WeakerAccess", "unused", "SpellCheckingInspection","serial"})
@XmlRootElement(name = "TransferIntoDetailsModleWorkerMsg")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransferIntoDetailsModleWorkerMsg  implements Serializable{
  

  private String ZJLX;//证件类型

  private String ZJHM;//证件号码

  private String ZGXM;//职工姓名

  private String SJHM;//手机号码

  private String XGRZFGJJZH;//现个人住房公积金账号

  private String XGZDWMC;//现工作单位名称

  @SuppressWarnings("unused")
  private void defaultConstructor(){

      this.setZJLX(null/*证件类型*/);
      this.setZJHM(null/*证件号码*/);
      this.setZGXM(null/*职工姓名*/);
      this.setSJHM(null/*手机号码*/);
      this.setXGRZFGJJZH(null/*现个人住房公积金账号*/);
      this.setXGZDWMC(null/*现工作单位名称*/);
  }

  /**
   * 证件类型
   **/
  public String getZJLX() { return ZJLX;}

  public void setZJLX(String ZJLX) {this.ZJLX = ZJLX;}

  /**
   * 证件号码
   **/
  public String getZJHM() { return ZJHM;}

  public void setZJHM(String ZJHM) {this.ZJHM = ZJHM;}

  /**
   * 职工姓名
   **/
  public String getZGXM() { return ZGXM;}

  public void setZGXM(String ZGXM) {this.ZGXM = ZGXM;}

  /**
   * 手机号码
   **/
  public String getSJHM() { return SJHM;}

  public void setSJHM(String SJHM) {this.SJHM = SJHM;}

  /**
   * 现个人住房公积金账号
   **/
  public String getXGRZFGJJZH() { return XGRZFGJJZH;}

  public void setXGRZFGJJZH(String XGRZFGJJZH) {this.XGRZFGJJZH = XGRZFGJJZH;}

  /**
   * 现工作单位名称
   **/
  public String getXGZDWMC() { return XGZDWMC;}

  public void setXGZDWMC(String XGZDWMC) {this.XGZDWMC = XGZDWMC;}


  @Override
  public String toString()  {

      @SuppressWarnings("StringBufferReplaceableByString")
      StringBuilder sb = new StringBuilder();
      sb.append("class TransferIntoDetailsModleWorkerMsg {\n");
      sb.append("ZJLX:").append(ZJLX).append("\n");
      sb.append("ZJHM:").append(ZJHM).append("\n");
      sb.append("ZGXM:").append(ZGXM).append("\n");
      sb.append("SJHM:").append(SJHM).append("\n");
      sb.append("XGRZFGJJZH:").append(XGRZFGJJZH).append("\n");
      sb.append("XGZDWMC:").append(XGZDWMC).append("\n");
      sb.append("}\n");
      return sb.toString();

  }

  public void checkValidation(){
      
  }
}
