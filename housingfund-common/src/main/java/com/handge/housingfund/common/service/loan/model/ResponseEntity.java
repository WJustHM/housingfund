package com.handge.housingfund.common.service.loan.model;

import java.io.Serializable;

public class ResponseEntity<T> implements Serializable {

    private T entity;

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

    public ResponseEntity(T entity) {

        this.entity = entity;
    }
}
