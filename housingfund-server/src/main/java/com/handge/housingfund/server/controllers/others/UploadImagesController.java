package com.handge.housingfund.server.controllers.others;

import com.handge.housingfund.common.service.account.model.Msg;
import com.handge.housingfund.common.service.account.model.Success;
import com.handge.housingfund.common.service.others.IUploadImagesService;
import com.handge.housingfund.common.service.others.model.UploadFileReq;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.common.service.util.ReturnCode;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.server.controllers.ResUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * Created by tanyi on 2017/8/18.
 */
@SuppressWarnings("restriction")
@Controller
@Path(value = "/getUploadImages")
public class UploadImagesController {

    @Autowired
    private IUploadImagesService uploadImagesService;

    @GET
    @Path("")
    @Produces("application/json; charset=utf-8")
    public Response getFileSet(@QueryParam("modle") String modle, @QueryParam("business") String business) {
        try {
            return ResUtils.wrapEntityIfNeeded(Response.status(200).entity(uploadImagesService.getUploadFile(modle, business)).build());
        } catch (Exception e) {
            return ResUtils.wrapEntityIfNeeded(Response.status(500).entity(new Msg() {{
                this.setCode(ReturnCode.Error);
                this.setMsg(e.getMessage());
            }}).build());
        }

    }

    @POST
    @Path("")
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addFileSet(UploadFileReq uploadFileReq) {
        if (!StringUtil.notEmpty(uploadFileReq.getReq().getBusiness()) ||
                !StringUtil.notEmpty(uploadFileReq.getReq().getCertificateName()) ||
                !StringUtil.notEmpty(uploadFileReq.getReq().getModle())) {
            return ResUtils.wrapEntityIfNeeded(Response.status(500).entity(new Msg() {{
                this.setCode(ReturnCode.Error);
                this.setMsg("参数错误");
            }}).build());
        }
        try {
            String id = uploadImagesService.addFileSet(uploadFileReq.getReq());
            if (id != null) {
                Success success = new Success();
                success.setId(id);
                success.setState("添加成功");
                return ResUtils.wrapEntityIfNeeded(Response.status(200).entity(success).build());
            } else {
                return ResUtils.wrapEntityIfNeeded(Response.status(500).entity(new Msg() {{
                    this.setCode(ReturnCode.Error);
                    this.setMsg("添加失败");
                }}).build());
            }

        } catch (Exception e) {
            return ResUtils.wrapEntityIfNeeded(Response.status(500).entity(new Msg() {{
                this.setCode(ReturnCode.Error);
                this.setMsg(e.getMessage());
            }}).build());
        }

    }

    @PUT
    @Path("/{id}")
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putFileSet(@PathParam("id") String id, UploadFileReq uploadFileReq) {
        if (!StringUtil.notEmpty(uploadFileReq.getReq().getBusiness()) ||
                !StringUtil.notEmpty(uploadFileReq.getReq().getCertificateName()) ||
                !StringUtil.notEmpty(uploadFileReq.getReq().getModle())) {
            return ResUtils.wrapEntityIfNeeded(Response.status(500).entity(new Msg() {{
                this.setCode(ReturnCode.Error);
                this.setMsg("参数错误");
            }}).build());
        }
        try {
            String resid = uploadImagesService.putFileSet(id, uploadFileReq.getReq());
            if (resid != null) {
                Success success = new Success();
                success.setId(resid);
                success.setState("修改成功");
                return ResUtils.wrapEntityIfNeeded(Response.status(200).entity(success).build());
            } else {
                return ResUtils.wrapEntityIfNeeded(Response.status(500).entity(new Msg() {{
                    this.setCode(ReturnCode.Error);
                    this.setMsg("修改失败");
                }}).build());
            }

        } catch (Exception e) {
            return ResUtils.wrapEntityIfNeeded(Response.status(500).entity(new Msg() {{
                this.setCode(ReturnCode.Error);
                this.setMsg(e.getMessage());
            }}).build());
        }

    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response delFileSet(@PathParam("id") String id) {
        try {
            String resid = uploadImagesService.deleteFileSet(id);
            if (resid != null) {
                Success success = new Success();
                success.setId(resid);
                success.setState("删除成功");
                return ResUtils.wrapEntityIfNeeded(Response.status(200).entity(success).build());
            } else {
                return ResUtils.wrapEntityIfNeeded(Response.status(500).entity(new Msg() {{
                    this.setCode(ReturnCode.Error);
                    this.setMsg("删除失败");
                }}).build());
            }

        } catch (Exception e) {
            return ResUtils.wrapEntityIfNeeded(Response.status(500).entity(new Msg() {{
                this.setCode(ReturnCode.Error);
                this.setMsg(e.getMessage());
            }}).build());
        }

    }


    @DELETE
    @Path("/deleteFiles")
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response delFileSets(RequestWrapper<ArrayList<String>> ids){

        try {

            uploadImagesService.deleteFilesSet(ids == null ? new ArrayList<>():ids.getReq());

            return ResUtils.wrapEntityIfNeeded(Response.status(200).entity(new Success(){{
                    this.setState("删除成功");
                }}).build());


        } catch (Exception e) {

            return ResUtils.wrapEntityIfNeeded(Response.status(500).entity(new Msg() {{
                this.setCode(ReturnCode.Error);
                this.setMsg(e.getMessage());
            }}).build());
        }
    }

}
