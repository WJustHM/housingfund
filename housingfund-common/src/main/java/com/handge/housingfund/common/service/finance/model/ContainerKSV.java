package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuefei_wang on 17-9-1.
 */
@XmlRootElement(name = "ContainerKSV")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContainerKSV<K,V> implements Serializable {

        private List<K> list =new ArrayList<K>();

        private V V;

        public List<K> getList() {
            return list;
        }

        public void setList(List<K> list) {
            this.list = list;
        }

        public V getV() {
            return V;
        }

        public void setV(V v) {
            this.V = v;
        }

        public  void  addK(K k){

            this.list.add(k);
        }
}
