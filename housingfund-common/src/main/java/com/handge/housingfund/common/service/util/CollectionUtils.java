package com.handge.housingfund.common.service.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;


/*
 * Created by lian on 2017/7/11.
 */
@SuppressWarnings({"unchecked", "Convert2Lambda", "unused"})
public class CollectionUtils {

    public interface Predicate<T>{

        boolean evaluate(T var1);
    }

    /*
      从数组中过滤出满足条件的第一个元素
      例如 {"a"，"ab"，"cd"，"c"} 查找包含c的 元素 结果为 cd

      @param collection 需要被过滤的数组

      @param predicate 过滤器 man 当对象满足条件时返回true

      使用范例 IndiAcctAlterImpl - getAcctsSetInfo
     */
    public static <T> T find(List<T> collection, final Predicate<T> predicate){

        return (T) org.apache.commons.collections.CollectionUtils.find(collection, new org.apache.commons.collections.Predicate() {


            @Override
            public boolean evaluate(Object o) {

                boolean value;
                try {

                    value = predicate.evaluate((T)o);
                }catch (Exception e){
                    value = false;
                }
                return value;
            }
        });
    }

    public static <T> T find(List<T> collection,T defaultValue, final Predicate<T> predicate){

        T object = CollectionUtils.find(collection,predicate);

        return object==null?defaultValue:object;
    }
    /*
       从数组中过滤出满足条件的元素
       例如 {"a"，"ab"，"cd"，"c"} 查找包含c的 元素 结果为 {"cd","c"}

       @param collection 需要被过滤的数组

       @param predicate 过滤器 man 当对象满足条件时返回true

       使用范例 暂无
      */
    public static <T> List<T> filter(List<T> collection, final Predicate<T> predicate){

        return (List<T>) org.apache.commons.collections.CollectionUtils.select(collection, new org.apache.commons.collections.Predicate() {


            @Override
            public boolean evaluate(Object o) {

                return predicate.evaluate((T)o);
            }
        });
    }

    public interface  Transformer<K,T>{

        T tansform(K var1);
    }

    /*
       转换数组中的对象
       例如 {"a123"，"ab123"，"cd1"，"c3dd"} 将所有元素都取第一个字母 结果为 {"a"，"a"，"c"，"c"}

       如果转换结果为null 结果将不会被加入新数组中

       @param collection 需要被过滤的数组

       @param predicate 转换器 返回新对象

       使用范例 IndiAcctASetImpl - getAcctsSetInfo
    */
    public static <T,K> List<K> flatmap(final List<T> collection, final Transformer<T, K> transformer){

        return new ArrayList<K>(){{

            for(T t:collection){

                if(t!=null){ this.add(transformer.tansform(t));}
            }
        }};


    }

    /*
      获取数组的第一个元素
      例如 {"a"，"ab"，"cd"，"c"} 查找包含c的 元素 结果为 "a"
      做了非空判断和越界处理

      @param collection 数组

      使用范例 IndiAcctSetImpl - reAcctAlter
 */
    public static <T> T getFirst(List<T> collection){

        return collection == null ? null : (collection.size() == 0 ? null : collection.get(0));
    }


    public static <T> List<T> merge(List<T> collectionA,List<T> collectionB){

        collectionA.addAll(collectionB);

        return collectionA;
    }

    public static <T> List<T> merge(List<T> ... collectionA ){

        if(collectionA.length>0){

            List<T> list = collectionA[0];

            for (int i=1;i<collectionA.length;i++){

                list.addAll(collectionA[i]);
            }

            return list;
        }
        return new ArrayList<>();

    }

    public interface Reducer<T,K>{

        public K reducer(K sum ,T obj);
    }
    public static <T,K> K reduce(Collection<T>collection, K start, Reducer<T,K>reducer){

        HashMap<String,K> map = new HashMap<String, K>();

        map.put("key",start);

        collection.forEach(new Consumer<T>() {
            @Override
            public void accept(T t) {

                map.put("key",reducer.reducer(map.get("key"),t));
            }
        });

        return map.get("key");
    }
}
