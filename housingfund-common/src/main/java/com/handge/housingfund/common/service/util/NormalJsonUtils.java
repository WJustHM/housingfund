package com.handge.housingfund.common.service.util;

import com.handge.housingfund.common.service.review.model.MultiReviewConfig;
import org.json.JSONArray;

import java.util.HashSet;

/**
 * Created by Liujuhao on 2017/9/18.
 */

public class NormalJsonUtils {

    /**
     * 把审核配置对象转化为json字符串（入库）
     *
     * @param multiReviewConfig
     * @return
     */
/*    public static String toJson4Review(MultiReviewConfig multiReviewConfig) {
        String json;
        if (multiReviewConfig == null)
            return null;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("LSSHYBH", multiReviewConfig.getLSSHYBH());
            jsonObject.put("DQSHY", multiReviewConfig.getDQSHY());
            jsonObject.put("SHJB", multiReviewConfig.getSHJB());
            jsonObject.put("DQXM", multiReviewConfig.getDQXM());
            jsonObject.put("SCSHY", multiReviewConfig.getSCSHY());
            json = jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            json = null;
        }
        return json;
    }*/
    public static String toJson4Review(MultiReviewConfig multiReviewConfig) {
        String json;
        if (multiReviewConfig == null)
            return null;
        try {
            SortJSONObject jsonObject = new SortJSONObject();
            jsonObject.put("LSSHYBH", multiReviewConfig.getLSSHYBH());
            jsonObject.put("DQSHY", multiReviewConfig.getDQSHY());
            jsonObject.put("SHJB", multiReviewConfig.getSHJB());
            jsonObject.put("DQXM", multiReviewConfig.getDQXM());
            jsonObject.put("SCSHY", multiReviewConfig.getSCSHY());
            json = jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            json = null;
        }
        return json;
    }

/*    *
     * 把审核配置的json字符串转化为配置对象（出库解析）
     *
     * @param json
     * @return*/

/*    public static MultiReviewConfig toObj4Review(String json) {
        MultiReviewConfig multiReviewConfig = new MultiReviewConfig();
        if (StringUtil.isEmpty(json))
            return new MultiReviewConfig() {
                {
                    this.setLSSHYBH(new HashSet<>());
                    this.setDQSHY(null);
                    this.setSCSHY(null);
                    this.setSHJB(null);
                    this.setDQXM(null);
                }
            };
        try {
            JSONObject jsonObject = new JSONObject(json);
            multiReviewConfig.setDQSHY(jsonObject.optString("DQSHY", null));
            multiReviewConfig.setSHJB(jsonObject.optString("SHJB", null));
            multiReviewConfig.setDQXM(jsonObject.optString("DQXM", null));
            multiReviewConfig.setSCSHY(jsonObject.optString("SCSHY", null));
            JSONArray jsonArray = jsonObject.optJSONArray("LSSHYBH");
            multiReviewConfig.setLSSHYBH(new HashSet<String>() {
                {
                    for (int j = 0; j < jsonArray.length(); j++) {
                        this.add(jsonArray.getString(j));
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            multiReviewConfig = null;
        }
        return multiReviewConfig;
    }*/
    public static MultiReviewConfig toObj4Review(String json) {
        MultiReviewConfig multiReviewConfig = new MultiReviewConfig();
        if (StringUtil.isEmpty(json))
            return new MultiReviewConfig() {
                {
                    this.setLSSHYBH(new HashSet<>());
                    this.setDQSHY(null);
                    this.setSCSHY(null);
                    this.setSHJB(null);
                    this.setDQXM(null);
                }
            };
        try {
            SortJSONObject jsonObject = new SortJSONObject(json);
            multiReviewConfig.setDQSHY(jsonObject.optString("DQSHY", null));
            multiReviewConfig.setSHJB(jsonObject.optString("SHJB", null));
            multiReviewConfig.setDQXM(jsonObject.optString("DQXM", null));
            multiReviewConfig.setSCSHY(jsonObject.optString("SCSHY", null));
            JSONArray jsonArray = jsonObject.optJSONArray("LSSHYBH");

            if (jsonArray == null)
                jsonArray = new JSONArray();

            HashSet set = new HashSet<String>();
            for (int j = 0 ; j < jsonArray.length(); j++) {
                set.add(jsonArray.getString(j));
            }

            multiReviewConfig.setLSSHYBH(set);

        } catch (Exception e) {
            e.printStackTrace();
            multiReviewConfig = null;
        }
        return multiReviewConfig;
    }
}
