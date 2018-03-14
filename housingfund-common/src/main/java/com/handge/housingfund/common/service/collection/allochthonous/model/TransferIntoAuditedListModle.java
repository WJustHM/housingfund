package com.handge.housingfund.common.service.collection.allochthonous.model;

import com.handge.housingfund.common.service.util.*;

import java.util.*;
import java.io.*;
import javax.xml.bind.annotation.*;




@SuppressWarnings({"WeakerAccess", "unused", "SpellCheckingInspection","serial"})
@XmlRootElement(name = "TransferIntoAuditedListModle")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransferIntoAuditedListModle  implements Serializable{
  

  private String nextPageNo;//下一页

  private String currentPage;//当前页码

  private String pageSize;//当前页码数据条数

  private String totalCount;//总条数

  private String pageCount;//总页数

  private ArrayList<TransferIntoAuditedListModleResults> results;//

  @SuppressWarnings("unused")
  private void defaultConstructor(){

      this.setNextPageNo(null/*下一页*/);
      this.setCurrentPage(null/*当前页码*/);
      this.setPageSize(null/*当前页码数据条数*/);
      this.setTotalCount(null/*总条数*/);
      this.setPageCount(null/*总页数*/);
      this.setResults(new ArrayList<TransferIntoAuditedListModleResults>(){
        {
            this.add(new TransferIntoAuditedListModleResults(){
              {

              }
            });
        }
      }/**/);
  }

  /**
   * 下一页
   **/
  public String getNextPageNo() { return nextPageNo;}

  public void setNextPageNo(String nextPageNo) {this.nextPageNo = nextPageNo;}

  /**
   * 当前页码
   **/
  public String getCurrentPage() { return currentPage;}

  public void setCurrentPage(String currentPage) {this.currentPage = currentPage;}

  /**
   * 当前页码数据条数
   **/
  public String getPageSize() { return pageSize;}

  public void setPageSize(String pageSize) {this.pageSize = pageSize;}

  /**
   * 总条数
   **/
  public String getTotalCount() { return totalCount;}

  public void setTotalCount(String totalCount) {this.totalCount = totalCount;}

  /**
   * 总页数
   **/
  public String getPageCount() { return pageCount;}

  public void setPageCount(String pageCount) {this.pageCount = pageCount;}

  /**
   **/
  public ArrayList<TransferIntoAuditedListModleResults> getResults() { return results;}

  public void setResults(ArrayList<TransferIntoAuditedListModleResults> results) {this.results = results;}


  @Override
  public String toString()  {

      @SuppressWarnings("StringBufferReplaceableByString")
      StringBuilder sb = new StringBuilder();
      sb.append("class TransferIntoAuditedListModle {\n");
      sb.append("nextPageNo:").append(nextPageNo).append("\n");
      sb.append("currentPage:").append(currentPage).append("\n");
      sb.append("pageSize:").append(pageSize).append("\n");
      sb.append("totalCount:").append(totalCount).append("\n");
      sb.append("pageCount:").append(pageCount).append("\n");
      sb.append("results:").append(results).append("\n");
      sb.append("}\n");
      return sb.toString();

  }

  public void checkValidation(){
      

      for(TransferIntoAuditedListModleResults element: results == null ? new ArrayList<TransferIntoAuditedListModleResults>():results){
          element.checkValidation();
      }
  }
}
