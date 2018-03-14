package com.handge.housingfund.common.service.collection.allochthonous.model;

import com.handge.housingfund.common.service.util.*;

import java.util.*;
import java.io.*;
import javax.xml.bind.annotation.*;




@SuppressWarnings({"WeakerAccess", "unused", "SpellCheckingInspection","serial"})
@XmlRootElement(name = "TransferIntoDetailsModle")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransferIntoDetailsModle  implements Serializable{
  

  private String YWLSH;//业务流水号

  private String LXHBH;//联系函编号

  private String LXDSCRQ;//联系单生成日期

  private TransferIntoDetailsModleTurnOutMsg TurnOutMsg;//

  private TransferIntoDetailsModleWorkerMsg WorkerMsg;//

  private TransferIntoDetailsModleTurnIntoMsg TurnIntoMsg;//

  private String BLZL;//办理资料

  @SuppressWarnings("unused")
  private void defaultConstructor(){

      this.setYWLSH(null/*业务流水号*/);
      this.setLXHBH(null/*联系函编号*/);
      this.setLXDSCRQ(null/*联系单生成日期*/);
      this.setTurnOutMsg(new TransferIntoDetailsModleTurnOutMsg(){
        {

        }
      }/**/);
      this.setWorkerMsg(new TransferIntoDetailsModleWorkerMsg(){
        {

        }
      }/**/);
      this.setTurnIntoMsg(new TransferIntoDetailsModleTurnIntoMsg(){
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
   * 联系单生成日期
   **/
  public String getLXDSCRQ() { return LXDSCRQ;}

  public void setLXDSCRQ(String LXDSCRQ) {this.LXDSCRQ = LXDSCRQ;}

  /**
   **/
  public TransferIntoDetailsModleTurnOutMsg getTurnOutMsg() { return TurnOutMsg;}

  public void setTurnOutMsg(TransferIntoDetailsModleTurnOutMsg TurnOutMsg) {this.TurnOutMsg = TurnOutMsg;}

  /**
   **/
  public TransferIntoDetailsModleWorkerMsg getWorkerMsg() { return WorkerMsg;}

  public void setWorkerMsg(TransferIntoDetailsModleWorkerMsg WorkerMsg) {this.WorkerMsg = WorkerMsg;}

  /**
   **/
  public TransferIntoDetailsModleTurnIntoMsg getTurnIntoMsg() { return TurnIntoMsg;}

  public void setTurnIntoMsg(TransferIntoDetailsModleTurnIntoMsg TurnIntoMsg) {this.TurnIntoMsg = TurnIntoMsg;}

  /**
   * 办理资料
   **/
  public String getBLZL() { return BLZL;}

  public void setBLZL(String BLZL) {this.BLZL = BLZL;}


  @Override
  public String toString()  {

      @SuppressWarnings("StringBufferReplaceableByString")
      StringBuilder sb = new StringBuilder();
      sb.append("class TransferIntoDetailsModle {\n");
      sb.append("YWLSH:").append(YWLSH).append("\n");
      sb.append("LXHBH:").append(LXHBH).append("\n");
      sb.append("LXDSCRQ:").append(LXDSCRQ).append("\n");
      sb.append("TurnOutMsg:").append(TurnOutMsg).append("\n");
      sb.append("WorkerMsg:").append(WorkerMsg).append("\n");
      sb.append("TurnIntoMsg:").append(TurnIntoMsg).append("\n");
      sb.append("BLZL:").append(BLZL).append("\n");
      sb.append("}\n");
      return sb.toString();

  }

  public void checkValidation(){
      

      if(TurnOutMsg != null){ TurnOutMsg.checkValidation(); }
      

      if(WorkerMsg != null){ WorkerMsg.checkValidation(); }
      

      if(TurnIntoMsg != null){ TurnIntoMsg.checkValidation(); }
      
  }
}
