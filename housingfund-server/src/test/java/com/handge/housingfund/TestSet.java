package com.handge.housingfund;

import java.util.HashSet;

/**
 * Created by xuefei_wang on 17-9-22.
 */
public class TestSet {

    public static void main(String[] args) {
        HashSet<String> set1 = new HashSet();
                set1.add("a");
        set1.add("b");
        set1.add("c");

        HashSet<String> set2 = new HashSet();
        set2.add("t");
        set2.add("e");
        set2.add("d");

        System.out.println(set1.contains("b"));
        System.out.println(set1.containsAll(set2));
        System.out.println(set1.retainAll(set2));
        System.out.println(set1);
;    }
}
