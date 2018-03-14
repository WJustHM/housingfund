package com.handge.housingfund.common.service.collection.allochthonous.model;

import com.handge.housingfund.common.service.util.*;

import java.util.*;
import java.io.*;
import javax.xml.bind.annotation.*;




@SuppressWarnings({"WeakerAccess", "unused", "SpellCheckingInspection","serial"})
@XmlRootElement(name = "TransferOutConfirmationModleReq")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransferOutConfirmationModleReq  implements Serializable{
  

  private TransferOutConfirmationModleReqReviewInfo reviewInfo;//

  private ArrayList<String> ids;//

  @SuppressWarnings("unused")
  private void defaultConstructor(){

      this.setReviewInfo(new TransferOutConfirmationModleReqReviewInfo(){
        {

        }
      }/**/);
      this.setIds(null/**/);
  }

  /**
   **/
  public TransferOutConfirmationModleReqReviewInfo getReviewInfo() { return reviewInfo;}

  public void setReviewInfo(TransferOutConfirmationModleReqReviewInfo reviewInfo) {this.reviewInfo = reviewInfo;}

  /**
   **/
  public ArrayList<String> getIds() { return ids;}

  public void setIds(ArrayList<String> ids) {this.ids = ids;}


  @Override
  public String toString()  {

      @SuppressWarnings("StringBufferReplaceableByString")
      StringBuilder sb = new StringBuilder();
      sb.append("class TransferOutConfirmationModleReq {\n");
      sb.append("reviewInfo:").append(reviewInfo).append("\n");
      sb.append("ids:").append(ids).append("\n");
      sb.append("}\n");
      return sb.toString();

  }

  public void checkValidation(){
      

      if(reviewInfo != null){ reviewInfo.checkValidation(); }
      
  }
}
