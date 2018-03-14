package com.handge.housingfund.common.service.collection.allochthonous.model;

import com.handge.housingfund.common.service.util.*;

import java.util.*;
import java.io.*;
import javax.xml.bind.annotation.*;




@SuppressWarnings({"WeakerAccess", "unused", "SpellCheckingInspection","serial"})
@XmlRootElement(name = "TransferIntoPostModle")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransferIntoPostModle  implements Serializable{
  

  private TransferIntoPostModleTurnOutMsg TurnOutMsg;//

  private TransferIntoPostModleWorkerMsg WorkerMsg;//

  private TransferIntoPostModleTurnIntoMsg TurnIntoMsg;//

  private String BLZL;//办理资料

  @SuppressWarnings("unused")
  private void defaultConstructor(){

      this.setTurnOutMsg(new TransferIntoPostModleTurnOutMsg(){
        {

        }
      }/**/);
      this.setWorkerMsg(new TransferIntoPostModleWorkerMsg(){
        {

        }
      }/**/);
      this.setTurnIntoMsg(new TransferIntoPostModleTurnIntoMsg(){
        {

        }
      }/**/);
      this.setBLZL(null/*办理资料*/);
  }

  /**
   **/
  public TransferIntoPostModleTurnOutMsg getTurnOutMsg() { return TurnOutMsg;}

  public void setTurnOutMsg(TransferIntoPostModleTurnOutMsg TurnOutMsg) {this.TurnOutMsg = TurnOutMsg;}

  /**
   **/
  public TransferIntoPostModleWorkerMsg getWorkerMsg() { return WorkerMsg;}

  public void setWorkerMsg(TransferIntoPostModleWorkerMsg WorkerMsg) {this.WorkerMsg = WorkerMsg;}

  /**
   **/
  public TransferIntoPostModleTurnIntoMsg getTurnIntoMsg() { return TurnIntoMsg;}

  public void setTurnIntoMsg(TransferIntoPostModleTurnIntoMsg TurnIntoMsg) {this.TurnIntoMsg = TurnIntoMsg;}

  /**
   * 办理资料
   **/
  public String getBLZL() { return BLZL;}

  public void setBLZL(String BLZL) {this.BLZL = BLZL;}


  @Override
  public String toString()  {

      @SuppressWarnings("StringBufferReplaceableByString")
      StringBuilder sb = new StringBuilder();
      sb.append("class TransferIntoPostModle {\n");
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
