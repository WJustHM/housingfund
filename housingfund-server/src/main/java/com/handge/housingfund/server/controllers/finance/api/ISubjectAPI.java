package com.handge.housingfund.server.controllers.finance.api;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.finance.model.SubjectModel;

import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/24.
 */
public interface ISubjectAPI {
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
    public Response searchSubjects(String subjectId,
                                   String subjectName,
                                   String subjectAttribute,
                                   int pageNo,
                                   int pageSize);

    /**
     * 获取会计科目列表（三级）
     *
     * @return
     */
    Response getSubjectList();

    /**
     * 创建会计科目
     *
     * @param subjectModel
     */
    public Response createSubject(SubjectModel subjectModel);

    /**
     * 获取科目详情
     *
     * @param id
     * @return
     */
    public Response getSubject(String id);

    /**
     * 修改会计科目
     *
     * @param id
     * @param subjectModel
     * @return
     */
    public Response updateSubject(String id, SubjectModel subjectModel);

    /**
     * 删除会计科目
     *
     * @param id
     * @return
     */
    public Response deleteSubject(String id);

    /**
     * 批量删除科目信息
     *
     * @param delList
     * @return
     */
    public Response delSubjects(ArrayList<String> delList);

    /**
     * 根据科目编号或名称模糊查询
     *
     * @return
     */
    public Response getSubjects(String param);

    /**
     * 判断科目编号是否已存在
     *
     * @param param
     * @return
     */
    public Response isExist(String param);

    Response getSubjectByBid(TokenContext tokenContext, String bidno);
}
