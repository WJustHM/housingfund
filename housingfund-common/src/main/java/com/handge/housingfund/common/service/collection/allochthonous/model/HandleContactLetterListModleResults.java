package com.handge.housingfund.common.service.collection.allochthonous.model;

import com.handge.housingfund.common.service.util.*;

import java.util.*;
import java.io.*;
import javax.xml.bind.annotation.*;




@SuppressWarnings({"WeakerAccess", "unused", "SpellCheckingInspection","serial"})
@XmlRootElement(name = "HandleContactLetterListModleResults")
@XmlAccessorType(XmlAccessType.FIELD)
public class HandleContactLetterListModleResults  implements Serializable{
  

  private String YWLSH;//业务流水号

  private String LXHBH;//联系函编号

  private String ZRGJJZXMC;//转入公积金中心名称

  private String ZGXM;//职工姓名

  private String ZJLX;//证件类型

  private String ZJHM;//证件号码

  private String ZhuangTai;//状态

  private String SLSJ;//受理时间

  private String ZYJE;//转移金额

  private String YZHBJJE;//原账户本金金额

  private String BNDLX;//本年度利息

  private String KHRQ;//开户日期

  private String JZNY;//缴至年月

  private String JZYFZQLGYSFLXJC;//缴至月份之前6个月是否连续缴存

  private String ZZCDSYZFGJJDKCS;//在转出地使用住房公积金贷款次数

  private String SFYWJQDGJJDK;//是否有未结清的公积金贷款

  private String SFCZPTPDXW;//是否存在骗提骗贷行为

  private String YWBLLXDH;//业务办理联系电话

  @SuppressWarnings("unused")
  private void defaultConstructor(){

      this.setYWLSH(null/*业务流水号*/);
      this.setLXHBH(null/*联系函编号*/);
      this.setZRGJJZXMC(null/*转入公积金中心名称*/);
      this.setZGXM(null/*职工姓名*/);
      this.setZJLX(null/*证件类型*/);
      this.setZJHM(null/*证件号码*/);
      this.setZhuangTai(null/*状态*/);
      this.setSLSJ(null/*受理时间*/);
      this.setZYJE(null/*转移金额*/);
      this.setYZHBJJE(null/*原账户本金金额*/);
      this.setBNDLX(null/*本年度利息*/);
      this.setKHRQ(null/*开户日期*/);
      this.setJZNY(null/*缴至年月*/);
      this.setJZYFZQLGYSFLXJC(null/*缴至月份之前6个月是否连续缴存*/);
      this.setZZCDSYZFGJJDKCS(null/*在转出地使用住房公积金贷款次数*/);
      this.setSFYWJQDGJJDK(null/*是否有未结清的公积金贷款*/);
      this.setSFCZPTPDXW(null/*是否存在骗提骗贷行为*/);
      this.setYWBLLXDH(null/*业务办理联系电话*/);
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
   * 状态
   **/
  public String getZhuangTai() { return ZhuangTai;}

  public void setZhuangTai(String ZhuangTai) {this.ZhuangTai = ZhuangTai;}

  /**
   * 受理时间
   **/
  public String getSLSJ() { return SLSJ;}

  public void setSLSJ(String SLSJ) {this.SLSJ = SLSJ;}

  /**
   * 转移金额
   **/
  public String getZYJE() { return ZYJE;}

  public void setZYJE(String ZYJE) {this.ZYJE = ZYJE;}

  /**
   * 原账户本金金额
   **/
  public String getYZHBJJE() { return YZHBJJE;}

  public void setYZHBJJE(String YZHBJJE) {this.YZHBJJE = YZHBJJE;}

  /**
   * 本年度利息
   **/
  public String getBNDLX() { return BNDLX;}

  public void setBNDLX(String BNDLX) {this.BNDLX = BNDLX;}

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
   * 缴至月份之前6个月是否连续缴存
   **/
  public String getJZYFZQLGYSFLXJC() { return JZYFZQLGYSFLXJC;}

  public void setJZYFZQLGYSFLXJC(String JZYFZQLGYSFLXJC) {this.JZYFZQLGYSFLXJC = JZYFZQLGYSFLXJC;}

  /**
   * 在转出地使用住房公积金贷款次数
   **/
  public String getZZCDSYZFGJJDKCS() { return ZZCDSYZFGJJDKCS;}

  public void setZZCDSYZFGJJDKCS(String ZZCDSYZFGJJDKCS) {this.ZZCDSYZFGJJDKCS = ZZCDSYZFGJJDKCS;}

  /**
   * 是否有未结清的公积金贷款
   **/
  public String getSFYWJQDGJJDK() { return SFYWJQDGJJDK;}

  public void setSFYWJQDGJJDK(String SFYWJQDGJJDK) {this.SFYWJQDGJJDK = SFYWJQDGJJDK;}

  /**
   * 是否存在骗提骗贷行为
   **/
  public String getSFCZPTPDXW() { return SFCZPTPDXW;}

  public void setSFCZPTPDXW(String SFCZPTPDXW) {this.SFCZPTPDXW = SFCZPTPDXW;}

  /**
   * 业务办理联系电话
   **/
  public String getYWBLLXDH() { return YWBLLXDH;}

  public void setYWBLLXDH(String YWBLLXDH) {this.YWBLLXDH = YWBLLXDH;}


  @Override
  public String toString()  {

      @SuppressWarnings("StringBufferReplaceableByString")
      StringBuilder sb = new StringBuilder();
      sb.append("class HandleContactLetterListModleResults {\n");
      sb.append("YWLSH:").append(YWLSH).append("\n");
      sb.append("LXHBH:").append(LXHBH).append("\n");
      sb.append("ZRGJJZXMC:").append(ZRGJJZXMC).append("\n");
      sb.append("ZGXM:").append(ZGXM).append("\n");
      sb.append("ZJLX:").append(ZJLX).append("\n");
      sb.append("ZJHM:").append(ZJHM).append("\n");
      sb.append("ZhuangTai:").append(ZhuangTai).append("\n");
      sb.append("SLSJ:").append(SLSJ).append("\n");
      sb.append("ZYJE:").append(ZYJE).append("\n");
      sb.append("YZHBJJE:").append(YZHBJJE).append("\n");
      sb.append("BNDLX:").append(BNDLX).append("\n");
      sb.append("KHRQ:").append(KHRQ).append("\n");
      sb.append("JZNY:").append(JZNY).append("\n");
      sb.append("JZYFZQLGYSFLXJC:").append(JZYFZQLGYSFLXJC).append("\n");
      sb.append("ZZCDSYZFGJJDKCS:").append(ZZCDSYZFGJJDKCS).append("\n");
      sb.append("SFYWJQDGJJDK:").append(SFYWJQDGJJDK).append("\n");
      sb.append("SFCZPTPDXW:").append(SFCZPTPDXW).append("\n");
      sb.append("YWBLLXDH:").append(YWBLLXDH).append("\n");
      sb.append("}\n");
      return sb.toString();

  }

  public void checkValidation(){
      
  }
}
