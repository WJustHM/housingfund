package com.handge.housingfund.common.service.collection.allochthonous.service;

import com.handge.housingfund.common.service.TokenContext;

/**
 * Created by Liujuhao on 2017/12/12.
 */
public interface TransferFinalInterface {

    //转出-审核办结操作
    default public void outFinal(TokenContext tokenContext, String YLWSH,String YYYJ){};

    //转入-审核办结操作
    default public void intoFinal(TokenContext tokenContext, String YWLSH){};
}
