package com.handge.housingfund.common.service.util;

import java.io.Serializable;

public class ResponseEntity<T> implements Serializable{

    private T entity;

    public void setEntity(T entity) {
        this.entity = entity;
    }
//private Error error;

//    public ResponseEntity(T entity, Error error) {
//
//        this.entity = entity;
//
//        this.error = error;
//    }
//
//    public Error getError() {
//        return error;
//
//    }

    public T getEntity() {
        return entity;
    }

    public ResponseEntity(){

    }

    public ResponseEntity(T entity){

        this.entity = entity;
    }
}
