package com.handge.housingfund.common.service.collection.allochthonous.model;

import com.handge.housingfund.common.service.util.*;

import java.util.*;
import java.io.*;
import javax.xml.bind.annotation.*;




@SuppressWarnings({"WeakerAccess", "unused", "SpellCheckingInspection","serial"})
@XmlRootElement(name = "TransferOutPutModleTurnOutFundsAccountMsg")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransferOutPutModleTurnOutFundsAccountMsg  implements Serializable{
  

  private String FKYH;//付款银行

  private String FKZH;//付款账号

  private String FKHM;//付款户名

  @SuppressWarnings("unused")
  private void defaultConstructor(){

      this.setFKYH(null/*付款银行*/);
      this.setFKZH(null/*付款账号*/);
      this.setFKHM(null/*付款户名*/);
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
   * 付款户名
   **/
  public String getFKHM() { return FKHM;}

  public void setFKHM(String FKHM) {this.FKHM = FKHM;}


  @Override
  public String toString()  {

      @SuppressWarnings("StringBufferReplaceableByString")
      StringBuilder sb = new StringBuilder();
      sb.append("class TransferOutPutModleTurnOutFundsAccountMsg {\n");
      sb.append("FKYH:").append(FKYH).append("\n");
      sb.append("FKZH:").append(FKZH).append("\n");
      sb.append("FKHM:").append(FKHM).append("\n");
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

      if (!StringUtil.notEmpty(FKHM)){

         throw new ErrorException(ReturnEnumeration.Parameter_MISS,"付款户名");
      }
  }
}
