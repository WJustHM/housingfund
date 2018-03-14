package com.handge.housingfund.database.utils;

import org.hibernate.Criteria;

import java.util.ArrayList;
import java.util.List;

public class CriteriaUtils {

    public static String addAlias(Criteria criteria, String aliasString){

        String[] splits = aliasString.split("\\.");

        List<String> relation = new ArrayList<String>();

        if (splits.length == 1) {

            return  aliasString;

        } else if (splits.length == 2) {

            if (!relation.contains(splits[0])) {
                criteria.createAlias(splits[0], splits[0]);
                relation.add(splits[0]);
            }

            return aliasString;

        } else {
            if (!relation.contains(splits[0])) {
                criteria.createAlias(splits[0], splits[0]);
                relation.add(splits[0]);
            }
            if (!relation.contains(splits[0] + "." + splits[1])) {
                criteria.createAlias(splits[0] + "." + splits[1], splits[1]);
                relation.add(splits[0] + "." + splits[1]);
            }
            for (int i = 2; i < splits.length; i++) {
                if (!relation.contains(splits[i - 2] + "." + splits[i - 1])) {
                    criteria.createAlias(splits[i - 2] + "." + splits[i - 1], splits[i - 1]);
                    relation.add(splits[i - 2] + "." + splits[i - 1]);
                }
            }

            return  splits[splits.length - 2] + "." + splits[splits.length - 1];


        }
    }
}
