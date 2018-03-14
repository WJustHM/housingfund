package com.handge.housingfund.common.service.account.model;

import com.handge.housingfund.database.enums.ListAction;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Liujuhao on 2017/11/27.
 */

@XmlRootElement(name = "PageResNew")
@XmlAccessorType(XmlAccessType.FIELD)
public class PageResNew<T> implements Serializable {

    // 记录
    private ArrayList<T> results;

    private String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public ArrayList<T> getResults() {
        return results;
    }

    public void setResults(ArrayList<T> results) {
        this.results = results;
    }

    public PageResNew() {
        this.setResults(new ArrayList<>());
    }

    public void setResults(String action, ArrayList<T> results) {
        this.results = results;

        if(results==null||results.size()==0){
            if(action.equals(ListAction.Next.getCode())){
                this.setTag("L");
            }else if(action.equals(ListAction.Previous.getCode())){
                this.setTag("F");
            }
        }
    }
    @Override
    public String toString() {
        return "PageResNew{" +
                "results=" + results +
                ", tag='" + tag + '\'' +
                '}';
    }
}
