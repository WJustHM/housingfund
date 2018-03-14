package com.handge.housingfund.common.service.collection.allochthonous.model;

import com.handge.housingfund.common.service.util.*;

import java.util.*;
import java.io.*;
import javax.xml.bind.annotation.*;




@SuppressWarnings({"WeakerAccess", "unused", "SpellCheckingInspection","serial"})
@XmlRootElement(name = "TransferOutPostModle")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransferOutPostModle  implements Serializable{
  

  private String YWLSH;//业务流水号

  private String LXHBH;//联系函编号

  private TransferOutPostModleAccountMsg AccountMsg;//

  private TransferOutPostModleTurnOutFundsAccountMsg TurnOutFundsAccountMsg;//

  @SuppressWarnings("unused")
  private void defaultConstructor(){

      this.setYWLSH(null/*业务流水号*/);
      this.setLXHBH(null/*联系函编号*/);
      this.setAccountMsg(new TransferOutPostModleAccountMsg(){
        {

        }
      }/**/);
      this.setTurnOutFundsAccountMsg(new TransferOutPostModleTurnOutFundsAccountMsg(){
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
  public TransferOutPostModleAccountMsg getAccountMsg() { return AccountMsg;}

  public void setAccountMsg(TransferOutPostModleAccountMsg AccountMsg) {this.AccountMsg = AccountMsg;}

  /**
   **/
  public TransferOutPostModleTurnOutFundsAccountMsg getTurnOutFundsAccountMsg() { return TurnOutFundsAccountMsg;}

  public void setTurnOutFundsAccountMsg(TransferOutPostModleTurnOutFundsAccountMsg TurnOutFundsAccountMsg) {this.TurnOutFundsAccountMsg = TurnOutFundsAccountMsg;}


  @Override
  public String toString()  {

      @SuppressWarnings("StringBufferReplaceableByString")
      StringBuilder sb = new StringBuilder();
      sb.append("class TransferOutPostModle {\n");
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
