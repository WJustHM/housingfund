package com.handge.housingfund.common.service.collection.allochthonous.model;

import com.handge.housingfund.common.service.util.*;

import java.util.*;
import java.io.*;
import javax.xml.bind.annotation.*;




@SuppressWarnings({"WeakerAccess", "unused", "SpellCheckingInspection","serial"})
@XmlRootElement(name = "TransferOutDetailsModleTurnIntoMechanismMsg")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransferOutDetailsModleTurnIntoMechanismMsg  implements Serializable{
  

  private String ZRJGBH;//转入机构编号

  private String ZRGJJZXMC;//转入公积金中心名称

  private String XGRZFGJJZH;//现个人住房公积金账号

  private String XDWMC;//现单位名称

  private String ZRGJJZXZJZHSSYHMC;//转入公积金中心资金账户所属银行名称

  private String ZRGJJZXZJZH;//转入公积金中心资金账号

  private String ZRGJJZXZJZHHM;//转入公积金中心资金账号户名

  private String LXDHHCZ;//联系电话或传真

  @SuppressWarnings("unused")
  private void defaultConstructor(){

      this.setZRJGBH(null/*转入机构编号*/);
      this.setZRGJJZXMC(null/*转入公积金中心名称*/);
      this.setXGRZFGJJZH(null/*现个人住房公积金账号*/);
      this.setXDWMC(null/*现单位名称*/);
      this.setZRGJJZXZJZHSSYHMC(null/*转入公积金中心资金账户所属银行名称*/);
      this.setZRGJJZXZJZH(null/*转入公积金中心资金账号*/);
      this.setZRGJJZXZJZHHM(null/*转入公积金中心资金账号户名*/);
      this.setLXDHHCZ(null/*联系电话或传真*/);
  }

  /**
   * 转入机构编号
   **/
  public String getZRJGBH() { return ZRJGBH;}

  public void setZRJGBH(String ZRJGBH) {this.ZRJGBH = ZRJGBH;}

  /**
   * 转入公积金中心名称
   **/
  public String getZRGJJZXMC() { return ZRGJJZXMC;}

  public void setZRGJJZXMC(String ZRGJJZXMC) {this.ZRGJJZXMC = ZRGJJZXMC;}

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
   * 转入公积金中心资金账户所属银行名称
   **/
  public String getZRGJJZXZJZHSSYHMC() { return ZRGJJZXZJZHSSYHMC;}

  public void setZRGJJZXZJZHSSYHMC(String ZRGJJZXZJZHSSYHMC) {this.ZRGJJZXZJZHSSYHMC = ZRGJJZXZJZHSSYHMC;}

  /**
   * 转入公积金中心资金账号
   **/
  public String getZRGJJZXZJZH() { return ZRGJJZXZJZH;}

  public void setZRGJJZXZJZH(String ZRGJJZXZJZH) {this.ZRGJJZXZJZH = ZRGJJZXZJZH;}

  /**
   * 转入公积金中心资金账号户名
   **/
  public String getZRGJJZXZJZHHM() { return ZRGJJZXZJZHHM;}

  public void setZRGJJZXZJZHHM(String ZRGJJZXZJZHHM) {this.ZRGJJZXZJZHHM = ZRGJJZXZJZHHM;}

  /**
   * 联系电话或传真
   **/
  public String getLXDHHCZ() { return LXDHHCZ;}

  public void setLXDHHCZ(String LXDHHCZ) {this.LXDHHCZ = LXDHHCZ;}


  @Override
  public String toString()  {

      @SuppressWarnings("StringBufferReplaceableByString")
      StringBuilder sb = new StringBuilder();
      sb.append("class TransferOutDetailsModleTurnIntoMechanismMsg {\n");
      sb.append("ZRJGBH:").append(ZRJGBH).append("\n");
      sb.append("ZRGJJZXMC:").append(ZRGJJZXMC).append("\n");
      sb.append("XGRZFGJJZH:").append(XGRZFGJJZH).append("\n");
      sb.append("XDWMC:").append(XDWMC).append("\n");
      sb.append("ZRGJJZXZJZHSSYHMC:").append(ZRGJJZXZJZHSSYHMC).append("\n");
      sb.append("ZRGJJZXZJZH:").append(ZRGJJZXZJZH).append("\n");
      sb.append("ZRGJJZXZJZHHM:").append(ZRGJJZXZJZHHM).append("\n");
      sb.append("LXDHHCZ:").append(LXDHHCZ).append("\n");
      sb.append("}\n");
      return sb.toString();

  }

  public void checkValidation(){
      
  }
}
