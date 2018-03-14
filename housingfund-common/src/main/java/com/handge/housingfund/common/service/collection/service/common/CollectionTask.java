package com.handge.housingfund.common.service.collection.service.common;

/**
 * Created by 凡 on 2017/10/19.
 */
public interface CollectionTask {

    void doCollectionTask();

    void doPayCallTask();

    void balanceInterestFinal();

    void doSMSPayCall();
}
