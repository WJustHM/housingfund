package com.handge.housingfund.common.service.collection.allochthonous.model;

import com.handge.housingfund.common.service.util.*;

import java.util.*;
import java.io.*;
import javax.xml.bind.annotation.*;




@SuppressWarnings({"WeakerAccess", "unused", "SpellCheckingInspection","serial"})
@XmlRootElement(name = "TransferOutConfirmationModleReviewInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransferOutConfirmationModleReviewInfo  implements Serializable{
  

  private String YYYJ;//原因

  private String BeiZhu;//备注

  @SuppressWarnings("unused")
  private void defaultConstructor(){

      this.setYYYJ(null/*原因*/);
      this.setBeiZhu(null/*备注*/);
  }

  /**
   * 原因
   **/
  public String getYYYJ() { return YYYJ;}

  public void setYYYJ(String YYYJ) {this.YYYJ = YYYJ;}

  /**
   * 备注
   **/
  public String getBeiZhu() { return BeiZhu;}

  public void setBeiZhu(String BeiZhu) {this.BeiZhu = BeiZhu;}


  @Override
  public String toString()  {

      @SuppressWarnings("StringBufferReplaceableByString")
      StringBuilder sb = new StringBuilder();
      sb.append("class TransferOutConfirmationModleReviewInfo {\n");
      sb.append("YYYJ:").append(YYYJ).append("\n");
      sb.append("BeiZhu:").append(BeiZhu).append("\n");
      sb.append("}\n");
      return sb.toString();

  }

  public void checkValidation(){
      
  }
}
