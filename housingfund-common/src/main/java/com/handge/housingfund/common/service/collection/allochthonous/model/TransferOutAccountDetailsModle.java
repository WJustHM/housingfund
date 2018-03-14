package com.handge.housingfund.common.service.collection.allochthonous.model;

import com.handge.housingfund.common.service.util.*;

import java.util.*;
import java.io.*;
import javax.xml.bind.annotation.*;




@SuppressWarnings({"WeakerAccess", "unused", "SpellCheckingInspection","serial"})
@XmlRootElement(name = "TransferOutAccountDetailsModle")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransferOutAccountDetailsModle  implements Serializable{
  

  private String YWLSH;//业务流水号

  private String LXHBH;//联系函编号

  private TransferOutAccountDetailsModleAccountMsg AccountMsg;//

  private TransferOutAccountDetailsModleTurnOutFundsAccountMsg TurnOutFundsAccountMsg;//

  @SuppressWarnings("unused")
  private void defaultConstructor(){

      this.setYWLSH(null/*业务流水号*/);
      this.setLXHBH(null/*联系函编号*/);
      this.setAccountMsg(new TransferOutAccountDetailsModleAccountMsg(){
        {

        }
      }/**/);
      this.setTurnOutFundsAccountMsg(new TransferOutAccountDetailsModleTurnOutFundsAccountMsg(){
        {

        }
      }/**/);
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
   **/
  public TransferOutAccountDetailsModleAccountMsg getAccountMsg() { return AccountMsg;}

  public void setAccountMsg(TransferOutAccountDetailsModleAccountMsg AccountMsg) {this.AccountMsg = AccountMsg;}

  /**
   **/
  public TransferOutAccountDetailsModleTurnOutFundsAccountMsg getTurnOutFundsAccountMsg() { return TurnOutFundsAccountMsg;}

  public void setTurnOutFundsAccountMsg(TransferOutAccountDetailsModleTurnOutFundsAccountMsg TurnOutFundsAccountMsg) {this.TurnOutFundsAccountMsg = TurnOutFundsAccountMsg;}


  @Override
  public String toString()  {

      @SuppressWarnings("StringBufferReplaceableByString")
      StringBuilder sb = new StringBuilder();
      sb.append("class TransferOutAccountDetailsModle {\n");
      sb.append("YWLSH:").append(YWLSH).append("\n");
      sb.append("LXHBH:").append(LXHBH).append("\n");
      sb.append("AccountMsg:").append(AccountMsg).append("\n");
      sb.append("TurnOutFundsAccountMsg:").append(TurnOutFundsAccountMsg).append("\n");
      sb.append("}\n");
      return sb.toString();

  }

  public void checkValidation(){
      

      if(AccountMsg != null){ AccountMsg.checkValidation(); }
      

      if(TurnOutFundsAccountMsg != null){ TurnOutFundsAccountMsg.checkValidation(); }
      
  }
}
