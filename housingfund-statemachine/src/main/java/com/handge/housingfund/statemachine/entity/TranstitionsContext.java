package com.handge.housingfund.statemachine.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xuefei_wang on 17-7-13.
 */
public class TranstitionsContext implements Serializable{
    private static final long serialVersionUID = 834685499259845442L;
    private String taskId;

    private ArrayList<TransEntity> transtionEntitys ;


    public TranstitionsContext(){
        transtionEntitys = new ArrayList<TransEntity>();
    }


    public void  addEntity(TransEntity transEntity){
        this.transtionEntitys.add(transEntity);
    }

    public ArrayList<TransEntity> getTranstionEntitys() {
        return transtionEntitys;
    }

    public void setTranstionEntitys(ArrayList<TransEntity> transtionEntitys) {
        this.transtionEntitys = transtionEntitys;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "TranstitionsContext{" +
                "taskId='" + taskId + '\'' +
                ", transtionEntitys=" + transtionEntitys +
                '}';
    }
}
