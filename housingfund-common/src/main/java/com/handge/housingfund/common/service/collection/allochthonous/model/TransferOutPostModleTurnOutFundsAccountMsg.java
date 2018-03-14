package com.handge.housingfund.common.service.collection.allochthonous.model;

import com.handge.housingfund.common.service.util.*;

import java.util.*;
import java.io.*;
import javax.xml.bind.annotation.*;




@SuppressWarnings({"WeakerAccess", "unused", "SpellCheckingInspection","serial"})
@XmlRootElement(name = "TransferOutPostModleTurnOutFundsAccountMsg")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransferOutPostModleTurnOutFundsAccountMsg  implements Serializable{
  

  private String FKYH;//付款银行

  private String FKZH;//付款账号

  private String YHHM;//银行户名

  @SuppressWarnings("unused")
  private void defaultConstructor(){

      this.setFKYH(null/*付款银行*/);
      this.setFKZH(null/*付款账号*/);
      this.setYHHM(null/*银行户名*/);
  }

  /**
   * 付款银行
   **/
  public String getFKYH() { return FKYH;}

  public void setFKYH(String FKYH) {this.FKYH = FKYH;}

  /**
   * 付款账号
   **/
  public String getFKZH() { return FKZH;}

  public void setFKZH(String FKZH) {this.FKZH = FKZH;}

  /**
   * 银行户名
   **/
  public String getYHHM() { return YHHM;}

  public void setYHHM(String YHHM) {this.YHHM = YHHM;}


  @Override
  public String toString()  {

      @SuppressWarnings("StringBufferReplaceableByString")
      StringBuilder sb = new StringBuilder();
      sb.append("class TransferOutPostModleTurnOutFundsAccountMsg {\n");
      sb.append("FKYH:").append(FKYH).append("\n");
      sb.append("FKZH:").append(FKZH).append("\n");
      sb.append("YHHM:").append(YHHM).append("\n");
      sb.append("}\n");
      return sb.toString();

  }

  public void checkValidation(){
      

      if (!StringUtil.notEmpty(FKYH)){

         throw new ErrorException(ReturnEnumeration.Parameter_MISS,"付款银行");
      }

      if (!StringUtil.notEmpty(FKZH)){

         throw new ErrorException(ReturnEnumeration.Parameter_MISS,"付款账号");
      }

      if (!StringUtil.notEmpty(YHHM)){

         throw new ErrorException(ReturnEnumeration.Parameter_MISS,"银行户名");
      }
  }
}
