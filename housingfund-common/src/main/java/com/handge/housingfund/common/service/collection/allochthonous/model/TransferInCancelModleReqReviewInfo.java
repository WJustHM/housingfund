package com.handge.housingfund.common.service.collection.allochthonous.model;

import com.handge.housingfund.common.service.util.*;

import java.util.*;
import java.io.*;
import javax.xml.bind.annotation.*;




@SuppressWarnings({"WeakerAccess", "unused", "SpellCheckingInspection","serial"})
@XmlRootElement(name = "TransferInCancelModleReqReviewInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransferInCancelModleReqReviewInfo  implements Serializable{
  

  private String CXJG;//撤销结果

  private String YYYJ;//原因（不通过）或意见（通过）

  private String CZY;//操作员

  private String YWWD;//业务网点

  private String zhiWu;//职务（操作员角色）

  private String CZQD;//操作渠道

  private String beiZhu;//备注

  @SuppressWarnings("unused")
  private void defaultConstructor(){

      this.setCXJG(null/*撤销结果*/);
      this.setYYYJ(null/*原因（不通过）或意见（通过）*/);
      this.setCZY(null/*操作员*/);
      this.setYWWD(null/*业务网点*/);
      this.setZhiWu(null/*职务（操作员角色）*/);
      this.setCZQD(null/*操作渠道*/);
      this.setBeiZhu(null/*备注*/);
  }

  /**
   * 撤销结果
   **/
  public String getCXJG() { return CXJG;}

  public void setCXJG(String CXJG) {this.CXJG = CXJG;}

  /**
   * 原因（不通过）或意见（通过）
   **/
  public String getYYYJ() { return YYYJ;}

  public void setYYYJ(String YYYJ) {this.YYYJ = YYYJ;}

  /**
   * 操作员
   **/
  public String getCZY() { return CZY;}

  public void setCZY(String CZY) {this.CZY = CZY;}

  /**
   * 业务网点
   **/
  public String getYWWD() { return YWWD;}

  public void setYWWD(String YWWD) {this.YWWD = YWWD;}

  /**
   * 职务（操作员角色）
   **/
  public String getZhiWu() { return zhiWu;}

  public void setZhiWu(String zhiWu) {this.zhiWu = zhiWu;}

  /**
   * 操作渠道
   **/
  public String getCZQD() { return CZQD;}

  public void setCZQD(String CZQD) {this.CZQD = CZQD;}

  /**
   * 备注
   **/
  public String getBeiZhu() { return beiZhu;}

  public void setBeiZhu(String beiZhu) {this.beiZhu = beiZhu;}


  @Override
  public String toString()  {

      @SuppressWarnings("StringBufferReplaceableByString")
      StringBuilder sb = new StringBuilder();
      sb.append("class TransferInCancelModleReqReviewInfo {\n");
      sb.append("CXJG:").append(CXJG).append("\n");
      sb.append("YYYJ:").append(YYYJ).append("\n");
      sb.append("CZY:").append(CZY).append("\n");
      sb.append("YWWD:").append(YWWD).append("\n");
      sb.append("zhiWu:").append(zhiWu).append("\n");
      sb.append("CZQD:").append(CZQD).append("\n");
      sb.append("beiZhu:").append(beiZhu).append("\n");
      sb.append("}\n");
      return sb.toString();

  }

  public void checkValidation(){
      
  }
}
