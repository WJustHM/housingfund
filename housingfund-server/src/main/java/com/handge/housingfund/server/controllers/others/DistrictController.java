package com.handge.housingfund.server.controllers.others;

import com.handge.housingfund.common.service.others.IDistrictService;
import com.handge.housingfund.common.service.others.model.CommonDistrict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sjw on 2017/10/11.
 */
@Controller
@Path("/district")
public class DistrictController {
    @Autowired
    private IDistrictService districtService;

    @GET
    @Consumes("application/json; charset=utf-8")
    @Produces("application/json; charset=utf-8")
    @Path(value = "ProvinceList")
    public Response getProvinceList() throws IOException {
        System.out.println("获取省列表详情");
        HashMap<String, List<CommonDistrict>> result = districtService.getProvinceList();
        return Response.ok("getDistrict OK").status(200).entity(result).build();
    }
    @GET
    @Consumes("application/json; charset=utf-8")
    @Produces("application/json; charset=utf-8")
    @Path(value = "CityList/{parent}")
    public Response getCityList(final @PathParam("parent") String parent) throws IOException {
        System.out.println("获取市列表详情");
        HashMap<String, List<CommonDistrict>> result = districtService.getCityList(parent);
        return Response.ok("getDistrict OK").status(200).entity(result).build();
    }
    @GET
    @Consumes("application/json; charset=utf-8")
    @Produces("application/json; charset=utf-8")
    @Path(value = "AreaList/{parent}")
    public Response getAreaList(final @PathParam("parent") String parent) throws IOException {
        System.out.println("获取区/县列表详情");
        HashMap<String, List<CommonDistrict>> result = districtService.getAreaList(parent);
        return Response.ok("getDistrict OK").status(200).entity(result).build();
    }
    @GET
    @Consumes("application/json; charset=utf-8")
    @Produces("application/json; charset=utf-8")
    @Path(value = "DistrictFullName/{id}")
    public Response getDistrictFullName(final @PathParam("id") String id) throws IOException {
        System.out.println("获取全名（省+市+区）");
        CommonDistrict result = districtService.getDistrictFullName(id);
        return Response.ok("getDistrict OK").status(200).entity(result).build();
    }
    @GET
    @Consumes("application/json; charset=utf-8")
    @Produces("application/json; charset=utf-8")
    @Path(value = "DistrictName")
    public Response getDistrictName(final @QueryParam("id") String id,final @QueryParam("name") String name) throws IOException {
        System.out.println("获取名字");
        CommonDistrict result = districtService.getDistrictName(id,name);
        return Response.ok("getDistrict OK").status(200).entity(result).build();
    }
}
