package com.handge.housingfund.common.service.collection.allochthonous.model;

import com.handge.housingfund.common.service.util.*;

import java.util.*;
import java.io.*;
import javax.xml.bind.annotation.*;




@SuppressWarnings({"WeakerAccess", "unused", "SpellCheckingInspection","serial"})
@XmlRootElement(name = "ReceiveContactLetterListModleResults")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReceiveContactLetterListModleResults  implements Serializable{
  

  private String YWLSH;//业务流水号

  private String LXHBH;//联系函编号

  private String ZRGJJZXMC;//转入公积金中心名称

  private String ZGXM;//职工姓名

  private String ZJLX;//证件类型

  private String ZJHM;//证件号码

  private String YGZDWMC;//原工作单位名称

  private String YGRZFGJJZH;//原个人住房公积金账号

  private String ZRZJZH;//转入资金账号

  private String ZRZJZHHM;//转入资金账户户名

  private String ZRZJZHYHMC;//转入资金账户银行名称

  private String ZRGJJZXLXFS;//转入公积金中心联系方式

  private String LXDSCRQ;//联系单生成日期

  private String ZhuangTai;//状态

  @SuppressWarnings("unused")
  private void defaultConstructor(){

      this.setYWLSH(null/*业务流水号*/);
      this.setLXHBH(null/*联系函编号*/);
      this.setZRGJJZXMC(null/*转入公积金中心名称*/);
      this.setZGXM(null/*职工姓名*/);
      this.setZJLX(null/*证件类型*/);
      this.setZJHM(null/*证件号码*/);
      this.setYGZDWMC(null/*原工作单位名称*/);
      this.setYGRZFGJJZH(null/*原个人住房公积金账号*/);
      this.setZRZJZH(null/*转入资金账号*/);
      this.setZRZJZHHM(null/*转入资金账户户名*/);
      this.setZRZJZHYHMC(null/*转入资金账户银行名称*/);
      this.setZRGJJZXLXFS(null/*转入公积金中心联系方式*/);
      this.setLXDSCRQ(null/*联系单生成日期*/);
      this.setZhuangTai(null/*状态*/);
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
   * 转入公积金中心名称
   **/
  public String getZRGJJZXMC() { return ZRGJJZXMC;}

  public void setZRGJJZXMC(String ZRGJJZXMC) {this.ZRGJJZXMC = ZRGJJZXMC;}

  /**
   * 职工姓名
   **/
  public String getZGXM() { return ZGXM;}

  public void setZGXM(String ZGXM) {this.ZGXM = ZGXM;}

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
   * 原工作单位名称
   **/
  public String getYGZDWMC() { return YGZDWMC;}

  public void setYGZDWMC(String YGZDWMC) {this.YGZDWMC = YGZDWMC;}

  /**
   * 原个人住房公积金账号
   **/
  public String getYGRZFGJJZH() { return YGRZFGJJZH;}

  public void setYGRZFGJJZH(String YGRZFGJJZH) {this.YGRZFGJJZH = YGRZFGJJZH;}

  /**
   * 转入资金账号
   **/
  public String getZRZJZH() { return ZRZJZH;}

  public void setZRZJZH(String ZRZJZH) {this.ZRZJZH = ZRZJZH;}

  /**
   * 转入资金账户户名
   **/
  public String getZRZJZHHM() { return ZRZJZHHM;}

  public void setZRZJZHHM(String ZRZJZHHM) {this.ZRZJZHHM = ZRZJZHHM;}

  /**
   * 转入资金账户银行名称
   **/
  public String getZRZJZHYHMC() { return ZRZJZHYHMC;}

  public void setZRZJZHYHMC(String ZRZJZHYHMC) {this.ZRZJZHYHMC = ZRZJZHYHMC;}

  /**
   * 转入公积金中心联系方式
   **/
  public String getZRGJJZXLXFS() { return ZRGJJZXLXFS;}

  public void setZRGJJZXLXFS(String ZRGJJZXLXFS) {this.ZRGJJZXLXFS = ZRGJJZXLXFS;}

  /**
   * 联系单生成日期
   **/
  public String getLXDSCRQ() { return LXDSCRQ;}

  public void setLXDSCRQ(String LXDSCRQ) {this.LXDSCRQ = LXDSCRQ;}

  /**
   * 状态
   **/
  public String getZhuangTai() { return ZhuangTai;}

  public void setZhuangTai(String ZhuangTai) {this.ZhuangTai = ZhuangTai;}


  @Override
  public String toString()  {

      @SuppressWarnings("StringBufferReplaceableByString")
      StringBuilder sb = new StringBuilder();
      sb.append("class ReceiveContactLetterListModleResults {\n");
      sb.append("YWLSH:").append(YWLSH).append("\n");
      sb.append("LXHBH:").append(LXHBH).append("\n");
      sb.append("ZRGJJZXMC:").append(ZRGJJZXMC).append("\n");
      sb.append("ZGXM:").append(ZGXM).append("\n");
      sb.append("ZJLX:").append(ZJLX).append("\n");
      sb.append("ZJHM:").append(ZJHM).append("\n");
      sb.append("YGZDWMC:").append(YGZDWMC).append("\n");
      sb.append("YGRZFGJJZH:").append(YGRZFGJJZH).append("\n");
      sb.append("ZRZJZH:").append(ZRZJZH).append("\n");
      sb.append("ZRZJZHHM:").append(ZRZJZHHM).append("\n");
      sb.append("ZRZJZHYHMC:").append(ZRZJZHYHMC).append("\n");
      sb.append("ZRGJJZXLXFS:").append(ZRGJJZXLXFS).append("\n");
      sb.append("LXDSCRQ:").append(LXDSCRQ).append("\n");
      sb.append("ZhuangTai:").append(ZhuangTai).append("\n");
      sb.append("}\n");
      return sb.toString();

  }

  public void checkValidation(){
      
  }
}
