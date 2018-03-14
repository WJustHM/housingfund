package com.handge.housingfund.common.service.collection.allochthonous.model;

import com.handge.housingfund.common.service.util.*;

import java.util.*;
import java.io.*;
import javax.xml.bind.annotation.*;




@SuppressWarnings({"WeakerAccess", "unused", "SpellCheckingInspection","serial"})
@XmlRootElement(name = "TransferOutDetailsModleAccountMsg")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransferOutDetailsModleAccountMsg  implements Serializable{
  

  private String ZJLX;//证件类型

  private String ZJHM;//证件号码

  private String ZGXM;//职工姓名

  private String YZHBJYE;//原账户本金余额

  private String BNDLX;//本年度利息

  private String ZYJE;//转移金额

  private String KHRQ;//开户日期

  private String JZNY;//缴至年月

  private String LXJC;//连续缴存

  private String GJJDKCS;//公积金贷款次数

  private String JQDK;//结清贷款

  private String SFCZPTPD;//是否存在骗提骗贷

  private String YWLXDH;//业务联系电话

  private String LXDSCRQ;//联系单生成日期

  @SuppressWarnings("unused")
  private void defaultConstructor(){

      this.setZJLX(null/*证件类型*/);
      this.setZJHM(null/*证件号码*/);
      this.setZGXM(null/*职工姓名*/);
      this.setYZHBJYE(null/*原账户本金余额*/);
      this.setBNDLX(null/*本年度利息*/);
      this.setZYJE(null/*转移金额*/);
      this.setKHRQ(null/*开户日期*/);
      this.setJZNY(null/*缴至年月*/);
      this.setLXJC(null/*连续缴存*/);
      this.setGJJDKCS(null/*公积金贷款次数*/);
      this.setJQDK(null/*结清贷款*/);
      this.setSFCZPTPD(null/*是否存在骗提骗贷*/);
      this.setYWLXDH(null/*业务联系电话*/);
      this.setLXDSCRQ(null/*联系单生成日期*/);
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
   * 原账户本金余额
   **/
  public String getYZHBJYE() { return YZHBJYE;}

  public void setYZHBJYE(String YZHBJYE) {this.YZHBJYE = YZHBJYE;}

  /**
   * 本年度利息
   **/
  public String getBNDLX() { return BNDLX;}

  public void setBNDLX(String BNDLX) {this.BNDLX = BNDLX;}

  /**
   * 转移金额
   **/
  public String getZYJE() { return ZYJE;}

  public void setZYJE(String ZYJE) {this.ZYJE = ZYJE;}

  /**
   * 开户日期
   **/
  public String getKHRQ() { return KHRQ;}

  public void setKHRQ(String KHRQ) {this.KHRQ = KHRQ;}

  /**
   * 缴至年月
   **/
  public String getJZNY() { return JZNY;}

  public void setJZNY(String JZNY) {this.JZNY = JZNY;}

  /**
   * 连续缴存
   **/
  public String getLXJC() { return LXJC;}

  public void setLXJC(String LXJC) {this.LXJC = LXJC;}

  /**
   * 公积金贷款次数
   **/
  public String getGJJDKCS() { return GJJDKCS;}

  public void setGJJDKCS(String GJJDKCS) {this.GJJDKCS = GJJDKCS;}

  /**
   * 结清贷款
   **/
  public String getJQDK() { return JQDK;}

  public void setJQDK(String JQDK) {this.JQDK = JQDK;}

  /**
   * 是否存在骗提骗贷
   **/
  public String getSFCZPTPD() { return SFCZPTPD;}

  public void setSFCZPTPD(String SFCZPTPD) {this.SFCZPTPD = SFCZPTPD;}

  /**
   * 业务联系电话
   **/
  public String getYWLXDH() { return YWLXDH;}

  public void setYWLXDH(String YWLXDH) {this.YWLXDH = YWLXDH;}

  /**
   * 联系单生成日期
   **/
  public String getLXDSCRQ() { return LXDSCRQ;}

  public void setLXDSCRQ(String LXDSCRQ) {this.LXDSCRQ = LXDSCRQ;}


  @Override
  public String toString()  {

      @SuppressWarnings("StringBufferReplaceableByString")
      StringBuilder sb = new StringBuilder();
      sb.append("class TransferOutDetailsModleAccountMsg {\n");
      sb.append("ZJLX:").append(ZJLX).append("\n");
      sb.append("ZJHM:").append(ZJHM).append("\n");
      sb.append("ZGXM:").append(ZGXM).append("\n");
      sb.append("YZHBJYE:").append(YZHBJYE).append("\n");
      sb.append("BNDLX:").append(BNDLX).append("\n");
      sb.append("ZYJE:").append(ZYJE).append("\n");
      sb.append("KHRQ:").append(KHRQ).append("\n");
      sb.append("JZNY:").append(JZNY).append("\n");
      sb.append("LXJC:").append(LXJC).append("\n");
      sb.append("GJJDKCS:").append(GJJDKCS).append("\n");
      sb.append("JQDK:").append(JQDK).append("\n");
      sb.append("SFCZPTPD:").append(SFCZPTPD).append("\n");
      sb.append("YWLXDH:").append(YWLXDH).append("\n");
      sb.append("LXDSCRQ:").append(LXDSCRQ).append("\n");
      sb.append("}\n");
      return sb.toString();

  }

  public void checkValidation(){
      
  }
}
