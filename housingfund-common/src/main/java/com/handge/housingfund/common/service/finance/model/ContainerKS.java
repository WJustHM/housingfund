package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuefei_wang on 17-8-31.
 */

@XmlRootElement(name = "ContainerKS")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContainerKS<K> implements Serializable{

    private long currentPage;

    private long nextPage;

    private long pageSize;

    private long totalCount;

    private List<K> list = new ArrayList<K>() ;

    private String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<K> getList() {
        return list;
    }
    public void setList(List<K> list) {
        this.list = list;
    }

    public long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }

    public long getNextPage() {
        return nextPage;
    }

    public void setNextPage(long nextPage) {
        this.nextPage = nextPage;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public void addK(K k){
        this.list.add(k);
    }
    public boolean remove(K k){
       return list.remove(k);
    }
}
