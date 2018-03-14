package com.handge.housingfund.common.service.util;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by xuefei_wang on 17-9-14.
 */
public  final class FixMap<K,V> {
    public static HashMap ignoreNullValue(HashMap map){
        HashMap finalMap = new HashMap();
        Set keys = map.keySet();
        for (Object k : keys){
            Object value = map.get(k);
            if (null != value) {
                finalMap.put(k,value);
            }
        }
        return finalMap;
    }
}
