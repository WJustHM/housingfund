package com.handge.housingfund.common.service.collection.allochthonous.model;

import com.handge.housingfund.common.service.util.*;

import java.util.*;
import java.io.*;
import javax.xml.bind.annotation.*;




@SuppressWarnings({"WeakerAccess", "unused", "SpellCheckingInspection","serial"})
@XmlRootElement(name = "TransferIntoBusinessDetailsModle")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransferIntoBusinessDetailsModle extends ArrayList<TransferIntoBusinessDetailsModleInner> implements Serializable{
  

  @SuppressWarnings("unused")
  private void defaultConstructor(){

  }


  @Override
  public String toString()  {

      @SuppressWarnings("StringBufferReplaceableByString")
      StringBuilder sb = new StringBuilder();
      sb.append("class TransferIntoBusinessDetailsModle {\n");
      sb.append("  " + super.toString()).append("\n");
      sb.append("}\n");
      return sb.toString();

  }

  public void checkValidation(){
      
  }
}
