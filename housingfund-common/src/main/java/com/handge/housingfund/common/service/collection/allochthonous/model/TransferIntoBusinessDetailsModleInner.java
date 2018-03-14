package com.handge.housingfund.common.service.collection.allochthonous.model;

import com.handge.housingfund.common.service.util.*;

import java.util.*;
import java.io.*;
import javax.xml.bind.annotation.*;




@SuppressWarnings({"WeakerAccess", "unused", "SpellCheckingInspection","serial"})
@XmlRootElement(name = "TransferIntoBusinessDetailsModleInner")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransferIntoBusinessDetailsModleInner  implements Serializable{
  

  private String LXHBH;//联系函编号

  private String CaoZuo;//操作

  private String CZJG;//操作机构

  private String CZRY;//操作人员

  private String CZSJ;//操作时间

  private String CZYJ;//操作意见

  private String CZHZT;//操作后状态

  @SuppressWarnings("unused")
  private void defaultConstructor(){

      this.setLXHBH(null/*联系函编号*/);
      this.setCaoZuo(null/*操作*/);
      this.setCZJG(null/*操作机构*/);
      this.setCZRY(null/*操作人员*/);
      this.setCZSJ(null/*操作时间*/);
      this.setCZYJ(null/*操作意见*/);
      this.setCZHZT(null/*操作后状态*/);
  }

  /**
   * 联系函编号
   **/
  public String getLXHBH() { return LXHBH;}

  public void setLXHBH(String LXHBH) {this.LXHBH = LXHBH;}

  /**
   * 操作
   **/
  public String getCaoZuo() { return CaoZuo;}

  public void setCaoZuo(String CaoZuo) {this.CaoZuo = CaoZuo;}

  /**
   * 操作机构
   **/
  public String getCZJG() { return CZJG;}

  public void setCZJG(String CZJG) {this.CZJG = CZJG;}

  /**
   * 操作人员
   **/
  public String getCZRY() { return CZRY;}

  public void setCZRY(String CZRY) {this.CZRY = CZRY;}

  /**
   * 操作时间
   **/
  public String getCZSJ() { return CZSJ;}

  public void setCZSJ(String CZSJ) {this.CZSJ = CZSJ;}

  /**
   * 操作意见
   **/
  public String getCZYJ() { return CZYJ;}

  public void setCZYJ(String CZYJ) {this.CZYJ = CZYJ;}

  /**
   * 操作后状态
   **/
  public String getCZHZT() { return CZHZT;}

  public void setCZHZT(String CZHZT) {this.CZHZT = CZHZT;}


  @Override
  public String toString()  {

      @SuppressWarnings("StringBufferReplaceableByString")
      StringBuilder sb = new StringBuilder();
      sb.append("class TransferIntoBusinessDetailsModleInner {\n");
      sb.append("LXHBH:").append(LXHBH).append("\n");
      sb.append("CaoZuo:").append(CaoZuo).append("\n");
      sb.append("CZJG:").append(CZJG).append("\n");
      sb.append("CZRY:").append(CZRY).append("\n");
      sb.append("CZSJ:").append(CZSJ).append("\n");
      sb.append("CZYJ:").append(CZYJ).append("\n");
      sb.append("CZHZT:").append(CZHZT).append("\n");
      sb.append("}\n");
      return sb.toString();

  }

  public void checkValidation(){
      
  }
}
