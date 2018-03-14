package com.handge.housingfund.common.service.collection.allochthonous.model;

import com.handge.housingfund.common.service.util.*;

import java.util.*;
import java.io.*;
import javax.xml.bind.annotation.*;




@SuppressWarnings({"WeakerAccess", "unused", "SpellCheckingInspection","serial"})
@XmlRootElement(name = "TransferIntoPostModleTurnIntoMsg")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransferIntoPostModleTurnIntoMsg  implements Serializable{
  

  private String ZRGJJZXZJZHSSYHMC;//转入公积金中心资金账户所属银行名称

  private String ZRGJJZXZJZH;//转入公积金中心资金账号

  private String ZRGJJZXZJZHHM;//转入公积金中心资金账号户名

  private String LXDHHCZ;//联系电话或传真

  @SuppressWarnings("unused")
  private void defaultConstructor(){

      this.setZRGJJZXZJZHSSYHMC(null/*转入公积金中心资金账户所属银行名称*/);
      this.setZRGJJZXZJZH(null/*转入公积金中心资金账号*/);
      this.setZRGJJZXZJZHHM(null/*转入公积金中心资金账号户名*/);
      this.setLXDHHCZ(null/*联系电话或传真*/);
  }

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
      sb.append("class TransferIntoPostModleTurnIntoMsg {\n");
      sb.append("ZRGJJZXZJZHSSYHMC:").append(ZRGJJZXZJZHSSYHMC).append("\n");
      sb.append("ZRGJJZXZJZH:").append(ZRGJJZXZJZH).append("\n");
      sb.append("ZRGJJZXZJZHHM:").append(ZRGJJZXZJZHHM).append("\n");
      sb.append("LXDHHCZ:").append(LXDHHCZ).append("\n");
      sb.append("}\n");
      return sb.toString();

  }

  public void checkValidation(){
      

      if (!StringUtil.notEmpty(ZRGJJZXZJZHSSYHMC)){

         throw new ErrorException(ReturnEnumeration.Parameter_MISS,"转入公积金中心资金账户所属银行名称");
      }

      if (!StringUtil.notEmpty(ZRGJJZXZJZH)){

         throw new ErrorException(ReturnEnumeration.Parameter_MISS,"转入公积金中心资金账号");
      }

      if (!StringUtil.notEmpty(LXDHHCZ)){

         throw new ErrorException(ReturnEnumeration.Parameter_MISS,"联系电话或传真");
      }
  }
}
