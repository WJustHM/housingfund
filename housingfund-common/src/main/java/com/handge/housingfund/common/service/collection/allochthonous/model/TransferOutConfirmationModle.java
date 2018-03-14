package com.handge.housingfund.common.service.collection.allochthonous.model;

import com.handge.housingfund.common.service.util.*;

import java.util.*;
import java.io.*;
import javax.xml.bind.annotation.*;




@SuppressWarnings({"WeakerAccess", "unused", "SpellCheckingInspection","serial"})
@XmlRootElement(name = "TransferOutConfirmationModle")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransferOutConfirmationModle  implements Serializable{
  

  private TransferOutConfirmationModleReviewInfo reviewInfo;//

  @SuppressWarnings("unused")
  private void defaultConstructor(){

      this.setReviewInfo(new TransferOutConfirmationModleReviewInfo(){
        {

        }
      }/**/);
  }

  /**
   **/
  public TransferOutConfirmationModleReviewInfo getReviewInfo() { return reviewInfo;}

  public void setReviewInfo(TransferOutConfirmationModleReviewInfo reviewInfo) {this.reviewInfo = reviewInfo;}


  @Override
  public String toString()  {

      @SuppressWarnings("StringBufferReplaceableByString")
      StringBuilder sb = new StringBuilder();
      sb.append("class TransferOutConfirmationModle {\n");
      sb.append("reviewInfo:").append(reviewInfo).append("\n");
      sb.append("}\n");
      return sb.toString();

  }

  public void checkValidation(){
      

      if(reviewInfo != null){ reviewInfo.checkValidation(); }
      
  }
}
