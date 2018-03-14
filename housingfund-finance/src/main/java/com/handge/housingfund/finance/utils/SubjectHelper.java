package com.handge.housingfund.finance.utils;

import com.handge.housingfund.common.service.finance.model.SubjectModel;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.entities.StFinanceSubjects;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanyi on 2017/8/28.
 */
public class SubjectHelper {


    public static List<SubjectModel> getSubjectByJson(String json) {
        if (!StringUtil.notEmpty(json)) {
            return null;
        }
        List<SubjectModel> res = new ArrayList<>();
        try {
            JSONArray files = new JSONArray(json);
            for (int i = 0; i < files.length(); i++) {
                SubjectModel subjectModel = new SubjectModel();
                JSONObject file = files.optJSONObject(i);
                subjectModel.setId(file.getString("id"));
                subjectModel.setKMMC(file.getString("KMMC"));
                subjectModel.setKMBH(file.getString("KMBH"));
                subjectModel.setKMYEFX(file.getString("KMYEFX"));
                res.add(subjectModel);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            res = null;
        }
        return res;
    }

    public static SubjectModel getSubjectByKMMC(List<StFinanceSubjects> stFinanceSubjects, String KMMC) {
        if (!StringUtil.notEmpty(KMMC)) {
            return null;
        }
        for (StFinanceSubjects subjects : stFinanceSubjects) {
            if (KMMC.equals(subjects.getKmmc())) {
                SubjectModel subjectModel = new SubjectModel();
                subjectModel.setId(subjects.getId());
                subjectModel.setKMMC(subjects.getKmmc());
                subjectModel.setKMBH(subjects.getKmbh());
                subjectModel.setKMYEFX(subjects.getKmyefx());
                return subjectModel;
            }
            if (KMMC.equals(subjects.getKmbh())) {
                SubjectModel subjectModel = new SubjectModel();
                subjectModel.setId(subjects.getId());
                subjectModel.setKMMC(subjects.getKmmc());
                subjectModel.setKMBH(subjects.getKmbh());
                subjectModel.setKMYEFX(subjects.getKmyefx());
                return subjectModel;
            }
        }
        return null;
    }

}
