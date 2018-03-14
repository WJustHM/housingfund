package com.handge.housingfund.server.controllers.finance.api.impl;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.Msg;
import com.handge.housingfund.common.service.account.model.Success;
import com.handge.housingfund.common.service.finance.ISubjectService;
import com.handge.housingfund.common.service.finance.model.SubjectModel;
import com.handge.housingfund.common.service.loan.model.Error;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnCode;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.server.controllers.finance.api.ISubjectAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/24.
 */
@Component
public class SubjectAPI implements ISubjectAPI {

    @Autowired
    ISubjectService iSubjectService;

    @Override
    public Response searchSubjects(String subjectId, String subjectName, String subjectAttribute, int pageNo, int pageSize) {
        if (pageNo == 0)
            pageNo = 1;
        if (pageSize == 0)
            pageSize = 10;

        try {
            return Response.status(200).entity(iSubjectService.searchSubjects(subjectId, subjectName, subjectAttribute, pageNo, pageSize)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getSubjectList() {
        try {
            return Response.status(200).entity(iSubjectService.getSubjectList()).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response createSubject(SubjectModel subjectModel) {
        if (subjectModel == null) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("科目内容不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        if (!StringUtil.notEmpty(subjectModel.getKMMC())) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("科目名称不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        if (!StringUtil.notEmpty(subjectModel.getKMSX())) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("科目属性不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        if (!StringUtil.notEmpty(subjectModel.getKMBH())) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("科目编号不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        if (StringUtil.notEmpty(subjectModel.getKMBH())
                && StringUtil.notEmpty(subjectModel.getSJKM())
                && !subjectModel.getKMBH().startsWith(subjectModel.getSJKM()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("科目编号不正确, 子科目编号必须以父科目编号开头");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (StringUtil.notEmpty(subjectModel.getKMBH())
                && StringUtil.notEmpty(subjectModel.getSJKM())
                && (subjectModel.getKMBH().length() > subjectModel.getSJKM().length() + 2
                || subjectModel.getKMBH().length() <= subjectModel.getSJKM().length()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("科目编号不正确, 子科目编号只能在父科目编号基础上加一至两位编码");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(subjectModel.getKMXZ())) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("科目性质不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();

        try {
            return Response.status(200).entity(iSubjectService.createSubject(subjectModel)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getSubject(String id) {
        if (!StringUtil.notEmpty(id)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("科目id不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();

        try {
            return Response.status(200).entity(iSubjectService.getSubject(id)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response updateSubject(String id, SubjectModel subjectModel) {
        if (!StringUtil.notEmpty(id) || subjectModel == null) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("科目id或科目内容不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        if (!StringUtil.notEmpty(subjectModel.getKMMC())) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("科目名称不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        if (!StringUtil.notEmpty(subjectModel.getKMSX())) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("科目属性不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        if (!StringUtil.notEmpty(subjectModel.getKMBH())) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("科目编号不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        if (!StringUtil.notEmpty(subjectModel.getKMXZ())) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("科目性质不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();

        try {
            return Response.status(200).entity(iSubjectService.updateSubject(id, subjectModel) ? new Msg() {{
                this.setCode(ReturnCode.Success);
                this.setMsg("该科目已在使用，不能修改");
            }} : new Success(){{
                this.setState("修改科目成功");
            }}).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response deleteSubject(String id) {
        if (!StringUtil.notEmpty(id)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("科目id不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        try {
            return Response.status(200).entity(iSubjectService.deleteSubject(id) ? new Msg() {{
                this.setCode(ReturnCode.Success);
                this.setMsg("已使用科目/默认科目/有子科目科目，不能删除");
            }} : new Success(){{
                this.setState("删除科目成功");
            }}).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response delSubjects(ArrayList<String> delList) {
        if (delList == null || delList.size() <= 0)
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("科目id不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();

        for (String id : delList) {
            if (!StringUtil.notEmpty(id)) {
                return Response.status(200).entity(new Msg() {
                    {
                        this.setMsg("科目id不能为空");
                        this.setCode(ReturnCode.Error);
                    }
                }).build();
            }
        }

        try {
            return Response.status(200).entity(iSubjectService.delSubjects(delList) ? new Msg() {{
                this.setCode(ReturnCode.Success);
                this.setMsg("科目为默认或已在使用，不能删除");
            }} : new Success(){{
                this.setState("删除科目成功");
            }}).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getSubjects(String param) {
        try {
            return Response.status(200).entity(iSubjectService.getSubjects(param)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response isExist(String param) {
        if (!StringUtil.notEmpty(param)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();

        try {
            return Response.status(200).entity(iSubjectService.isExist(param) ? new Success(){{
                this.setState("已存在");
            }} : new Success(){{
                this.setState("可使用");
            }}).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getSubjectByBid(TokenContext tokenContext, String bidno) {
        if (!StringUtil.notEmpty(bidno)) return Response.status(Response.Status.BAD_REQUEST).entity(new Msg() {
            {
                this.setMsg("银行帐号不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        try {
            return Response.status(200).entity(iSubjectService.getSubjectByBid(bidno)).build();
        } catch (ErrorException e) {
            return Response.status(500).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }
}
