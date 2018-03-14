package com.handge.housingfund.common.service.collection.allochthonous.model;

import com.handge.housingfund.common.service.util.*;

import java.util.*;
import java.io.*;
import javax.xml.bind.annotation.*;




@SuppressWarnings({"WeakerAccess", "unused", "SpellCheckingInspection","serial"})
@XmlRootElement(name = "TransferOutDetailsModle")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransferOutDetailsModle  implements Serializable{
  

  private String YWLSH;//业务流水号

  private String LXHBH;//联系函编号

  private String ZhuangTai;//状态

  private TransferOutDetailsModleAccountMsg AccountMsg;//

  private TransferOutDetailsModleTurnOutMechanismMsg TurnOutMechanismMsg;//

  private TransferOutDetailsModleTurnIntoMechanismMsg TurnIntoMechanismMsg;//

  private String BLZL;//办理资料

  @SuppressWarnings("unused")
  private void defaultConstructor(){

      this.setYWLSH(null/*业务流水号*/);
      this.setLXHBH(null/*联系函编号*/);
      this.setZhuangTai(null/*状态*/);
      this.setAccountMsg(new TransferOutDetailsModleAccountMsg(){
        {

        }
      }/**/);
      this.setTurnOutMechanismMsg(new TransferOutDetailsModleTurnOutMechanismMsg(){
        {

        }
      }/**/);
      this.setTurnIntoMechanismMsg(new TransferOutDetailsModleTurnIntoMechanismMsg(){
        {

        }
      }/**/);
      this.setBLZL(null/*办理资料*/);
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
   * 状态
   **/
  public String getZhuangTai() { return ZhuangTai;}

  public void setZhuangTai(String ZhuangTai) {this.ZhuangTai = ZhuangTai;}

  /**
   **/
  public TransferOutDetailsModleAccountMsg getAccountMsg() { return AccountMsg;}

  public void setAccountMsg(TransferOutDetailsModleAccountMsg AccountMsg) {this.AccountMsg = AccountMsg;}

  /**
   **/
  public TransferOutDetailsModleTurnOutMechanismMsg getTurnOutMechanismMsg() { return TurnOutMechanismMsg;}

  public void setTurnOutMechanismMsg(TransferOutDetailsModleTurnOutMechanismMsg TurnOutMechanismMsg) {this.TurnOutMechanismMsg = TurnOutMechanismMsg;}

  /**
   **/
  public TransferOutDetailsModleTurnIntoMechanismMsg getTurnIntoMechanismMsg() { return TurnIntoMechanismMsg;}

  public void setTurnIntoMechanismMsg(TransferOutDetailsModleTurnIntoMechanismMsg TurnIntoMechanismMsg) {this.TurnIntoMechanismMsg = TurnIntoMechanismMsg;}

  /**
   * 办理资料
   **/
  public String getBLZL() { return BLZL;}

  public void setBLZL(String BLZL) {this.BLZL = BLZL;}


  @Override
  public String toString()  {

      @SuppressWarnings("StringBufferReplaceableByString")
      StringBuilder sb = new StringBuilder();
      sb.append("class TransferOutDetailsModle {\n");
      sb.append("YWLSH:").append(YWLSH).append("\n");
      sb.append("LXHBH:").append(LXHBH).append("\n");
      sb.append("ZhuangTai:").append(ZhuangTai).append("\n");
      sb.append("AccountMsg:").append(AccountMsg).append("\n");
      sb.append("TurnOutMechanismMsg:").append(TurnOutMechanismMsg).append("\n");
      sb.append("TurnIntoMechanismMsg:").append(TurnIntoMechanismMsg).append("\n");
      sb.append("BLZL:").append(BLZL).append("\n");
      sb.append("}\n");
      return sb.toString();

  }

  public void checkValidation(){
      

      if(AccountMsg != null){ AccountMsg.checkValidation(); }
      

      if(TurnOutMechanismMsg != null){ TurnOutMechanismMsg.checkValidation(); }
      

      if(TurnIntoMechanismMsg != null){ TurnIntoMechanismMsg.checkValidation(); }
      
  }
}
