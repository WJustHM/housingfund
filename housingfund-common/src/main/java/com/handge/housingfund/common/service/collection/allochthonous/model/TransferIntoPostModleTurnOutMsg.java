package com.handge.housingfund.common.service.collection.allochthonous.model;

import com.handge.housingfund.common.service.util.*;

import java.util.*;
import java.io.*;
import javax.xml.bind.annotation.*;




@SuppressWarnings({"WeakerAccess", "unused", "SpellCheckingInspection","serial"})
@XmlRootElement(name = "TransferIntoPostModleTurnOutMsg")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransferIntoPostModleTurnOutMsg  implements Serializable{
  

  private String ZCJGBH;//转出机构编号

  private String ZCGJJZXMC;//转出公积金中心名称

  private String YGRZFGJJZH;//原个人住房公积金账号

  private String YGZDWMC;//原工作单位名称

  @SuppressWarnings("unused")
  private void defaultConstructor(){

      this.setZCJGBH(null/*转出机构编号*/);
      this.setZCGJJZXMC(null/*转出公积金中心名称*/);
      this.setYGRZFGJJZH(null/*原个人住房公积金账号*/);
      this.setYGZDWMC(null/*原工作单位名称*/);
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
   * 原工作单位名称
   **/
  public String getYGZDWMC() { return YGZDWMC;}

  public void setYGZDWMC(String YGZDWMC) {this.YGZDWMC = YGZDWMC;}


  @Override
  public String toString()  {

      @SuppressWarnings("StringBufferReplaceableByString")
      StringBuilder sb = new StringBuilder();
      sb.append("class TransferIntoPostModleTurnOutMsg {\n");
      sb.append("ZCJGBH:").append(ZCJGBH).append("\n");
      sb.append("ZCGJJZXMC:").append(ZCGJJZXMC).append("\n");
      sb.append("YGRZFGJJZH:").append(YGRZFGJJZH).append("\n");
      sb.append("YGZDWMC:").append(YGZDWMC).append("\n");
      sb.append("}\n");
      return sb.toString();

  }

  public void checkValidation(){
      

      if (!StringUtil.notEmpty(ZCJGBH)){

         throw new ErrorException(ReturnEnumeration.Parameter_MISS,"转出机构编号");
      }

      if (!StringUtil.notEmpty(ZCGJJZXMC)){

         throw new ErrorException(ReturnEnumeration.Parameter_MISS,"转出公积金中心名称");
      }

      if (!StringUtil.notEmpty(YGRZFGJJZH)){

         throw new ErrorException(ReturnEnumeration.Parameter_MISS,"原个人住房公积金账号");
      }

      if (!StringUtil.notEmpty(YGZDWMC)){

         throw new ErrorException(ReturnEnumeration.Parameter_MISS,"原工作单位名称");
      }
  }
}
