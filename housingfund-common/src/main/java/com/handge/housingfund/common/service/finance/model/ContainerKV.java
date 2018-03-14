package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by xuefei_wang on 17-9-1.
 */
@XmlRootElement(name = "ContainerKV")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContainerKV<K,V> implements Serializable {
    private K K;
    private V V;

    public K getK() {
        return K;
    }

    public void setK(K k) {
        this.K = k;
    }

    public V getV() {
        return V;
    }

    public void setV(V v) {
        this.V = v;
    }
}
