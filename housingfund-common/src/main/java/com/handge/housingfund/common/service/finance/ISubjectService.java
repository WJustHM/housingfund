package com.handge.housingfund.common.service.finance;


import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.finance.model.SubjectBaseModel;
import com.handge.housingfund.common.service.finance.model.SubjectModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/24.
 */
public interface ISubjectService {
    /**
     * 获取会计科目列表
     *
     * @param subjectId
     * @param subjectName
     * @param subjectAttribute
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageRes<SubjectModel> searchSubjects(String subjectId, String subjectName, String subjectAttribute, int pageNo, int pageSize);

    /**
     * 获取科目列表（三级）
     *
     * @return
     */
    List<SubjectBaseModel> getSubjectList();


    /**
     * 创建会计科目
     *
     * @param subjectModel
     */
    SubjectModel createSubject(SubjectModel subjectModel);

    /**
     * 获取科目详情
     *
     * @param kmbh
     * @return
     */
    SubjectModel getSubject(String kmbh);

    /**
     * 修改会计科目
     *
     * @param id
     * @param subjectModel
     * @return
     */
    boolean updateSubject(String id, SubjectModel subjectModel);

    /**
     * 删除会计科目
     *
     * @param id
     * @return
     */
    boolean deleteSubject(String id);

    /**
     * 批量删除会计科目
     *
     * @param delList
     * @return
     */
    boolean delSubjects(ArrayList<String> delList);

    /**
     * 根据科目编号或名称模糊查询
     *
     * @return
     */
    ArrayList<SubjectModel> getSubjects(String param);

    /**
     * 判断科目编号是否已存在
     *
     * @param param
     * @return
     */
    boolean isExist(String param);

    SubjectModel getSubjectByBid(String bidno);
}
