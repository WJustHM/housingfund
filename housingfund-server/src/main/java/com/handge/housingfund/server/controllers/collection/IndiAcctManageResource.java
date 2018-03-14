package com.handge.housingfund.server.controllers.collection;

import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.individual.*;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.collection.api.IndiAcctManageApi;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Controller
@Path("/collection")
public class IndiAcctManageResource {

	@Autowired
	private IndiAcctManageApi<Response> service;

	/**
	 * 修改合并个人账户
	 *
	 * @param YWLSH
	 **/
	@Path("/IndiAcctMerge/{YWLSH}")
	@PUT
	@Produces("application/json; charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putIndiAcctMerge(@Context HttpRequest httpRequest, final @PathParam("YWLSH") String YWLSH,
									 final RequestWrapper<IndiAcctMergePut> indiacctmergeput) {

		System.out.println("修改合并个人账户");

		return ResUtils.wrapEntityIfNeeded(this.service.putIndiAcctMerge((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH, indiacctmergeput == null ? null : indiacctmergeput.getReq()));
	}

	/**
	 *
	 * 合并个人账户回执单
	 *
	 * @param YWLSH
	 **/
	@Path("/IndiAcctMerge/receipt/{YWLSH}")
	@GET
	@Produces("application/json; charset=utf-8")
	public Response headIndiAcctMerge(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH) {

		System.out.println("合并个人账户回执单");

		return ResUtils.wrapEntityIfNeeded(this.service.headIndiAcctMerge((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH));
	}

	/**
	 * 查看合并个人账户详情
	 *
	 * @param YWLSH
	 **/
	@Path("/IndiAcctMerge/{YWLSH}")
	@GET
	@Produces("application/json; charset=utf-8")
	public Response getIndiAcctMerge(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH) {

		System.out.println("查看合并个人账户详情");

		return ResUtils.wrapEntityIfNeeded(this.service.getIndiAcctMerge((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH));
	}

	/**
	 * 新建合并个人账户
	 **/
	@Path("/IndiAcctMerge")
	@POST
	@Produces("application/json; charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postIndiAcctMerge(@Context HttpRequest httpRequest,final RequestWrapper<IndiAcctMergePost> indiacctmergepost) {

		System.out.println("新建合并个人账户");

		return ResUtils.wrapEntityIfNeeded(
				this.service.postIndiAcctMerge((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),indiacctmergepost == null ? null : indiacctmergepost.getReq()));
	}


	/**
	 * Action（封存、启封、冻结、解冻、托管）个人账户业务新建
	 *
	 * @param GRZH
	 *            个人账号
	 **/
	@Path("/IndiAcctAction/{CZMC}")
	@POST
	@Produces("application/json; charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postIndiAcctAction(@Context HttpRequest httpRequest,final @QueryParam("GRZH") String GRZH,
									   final @PathParam("CZMC") String CZMC,
									   final RequestWrapper<IndiAcctActionPost> addindiacctaction) {

		System.out.println("Action（封存、启封、冻结、解冻、托管）个人账户业务新建");

		return ResUtils.wrapEntityIfNeeded(
				this.service.postIndiAcctAction((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),GRZH, CZMC, addindiacctaction == null ? null : addindiacctaction.getReq()));
	}

	/**
	 * 条件查询个人内部转移信息列表
	 *
	 * @param ZCDW
	 *            转出单位
	 * @param ZhuangTai
	 *            状态
	 **/
	@Path("/IndiAcctsTransferList")
	@GET
	@Produces("application/json; charset=utf-8")
	public Response getIndiAcctsTransferList(@Context HttpRequest httpRequest,final @QueryParam("ZCDW") String ZCDW,
											 final @QueryParam("ZhuangTai") String ZhuangTai) {

		System.out.println("条件查询个人内部转移信息列表");

		return ResUtils.wrapEntityIfNeeded(this.service.getIndiAcctsTransferList((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),ZCDW, ZhuangTai));
	}




	/**
	 * Action（封存、启封、冻结、解冻、托管）个人账户修改
	 *
	 * @param YWLSH
	 *            业务流水号
	 **/
	@Path("/IndiAcctAction/{CZMC}/{YWLSH}")
	@PUT
	@Produces("application/json; charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putIndiAcctAction(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH,
									  final @PathParam("CZMC") String CZMC,
									  final RequestWrapper<IndiAcctActionPut> indiacctactionput) {

		System.out.println("Action（封存、启封、冻结、解冻、托管）个人账户修改");

		return ResUtils.wrapEntityIfNeeded(
				this.service.putIndiAcctAction((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH, CZMC, indiacctactionput == null ? null : indiacctactionput.getReq()));
	}

	/**
	 * 个人账户Action（封存、启封、冻结、解冻、托管）回执单
	 *
	 * @param YWLSH
	 *            业务流水号
	 **/
	@Path("/IndiAcctAction/receipt/{CZMC}/{YWLSH}")
	@GET
	@Produces("application/json; charset=utf-8")
	public Response headIndiAcctAction(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH, final @PathParam("CZMC") String CZMC) {

		System.out.println("个人账户Action（封存、启封、冻结、解冻、托管）回执单");

		return ResUtils.wrapEntityIfNeeded(this.service.headIndiAcctAction((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH, CZMC));
	}

	/**
	 * 查看Action（封存、启封、冻结、解冻、托管）个人账户详情
	 *
	 * @param YWLSH
	 *            业务流水号
	 **/
	@Path("/IndiAcctAction/{CZMC}/{YWLSH}")
	@GET
	@Produces("application/json; charset=utf-8")
	public Response getIndiAcctAction(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH, final @PathParam("CZMC") String CZMC) {

		System.out.println("查看Action（封存、启封、冻结、解冻、托管）个人账户详情");

		return ResUtils.wrapEntityIfNeeded(this.service.getIndiAcctAction((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH, CZMC));
	}

	/**
	 * 修改个人账户内部转移
	 *
	 * @param YWLSH
	 *            业务流水号
	 **/
	@Path("/IndiAcctTransfer/{YWLSH}")
	@PUT
	@Produces("application/json; charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putIndiAcctTransfer(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH,
										final RequestWrapper<IndiAcctTransferPut> indiaccttransferput) {

		System.out.println("修改个人账户内部转移");

		return ResUtils.wrapEntityIfNeeded(this.service.putIndiAcctTransfer((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH,
				indiaccttransferput == null ? null : indiaccttransferput.getReq()));
	}

	/**
	 * 个人账户内部转移回执单
	 *
	 * @param YWLSH
	 **/
	@Path("/IndiAcctTransfer/receipt/{YWLSH}")
	@GET
	@Produces("application/json; charset=utf-8")
	public Response headIndiAcctTransfer(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH) {

		System.out.println("个人账户内部转移回执单");

		return ResUtils.wrapEntityIfNeeded(this.service.headIndiAcctTransfer((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH));
	}

	/**
	 * 查看个人账户内部转移详情
	 *
	 * @param YWLSH
	 *            业务流水号
	 **/
	@Path("/IndiAcctTransfer/{YWLSH}")
	@GET
	@Produces("application/json; charset=utf-8")
	public Response getIndiAcctTransfer(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH) {

		System.out.println("查看个人账户内部转移详情");

		return ResUtils.wrapEntityIfNeeded(this.service.getIndiAcctTransfer((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH));
	}

	/**
	 * 新建个人账户变更
	 **/
	@Path("/AlterIndiAcct/")
	@POST
	@Produces("application/json; charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postIndiAcctAlter(@Context HttpRequest httpRequest,final RequestWrapper<IndiAcctAlterPost> indiacctalterpost) {

		System.out.println("新建个人账户变更");

		return ResUtils.wrapEntityIfNeeded(
				this.service.postIndiAcctAlter((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),indiacctalterpost == null ? null : indiacctalterpost.getReq()));
	}

	/**
	 * 根据业务类型，获取该业务下的账户列表（以默认参数查询）
	 *
	 * @param XingMing
	 *            姓名
	 * @param ZJHM
	 *            证件号码
	 * @param GRZH
	 *            个人账号（开户业务时无此项）
	 * @param DWMC
	 *            单位名称
	 * @param CZYY
	 *            操作原因
	 * @param ZhuangTai
	 *            业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)
	 * @param CZMC
	 *            根据业务名称查询（01:设立；02:变更；03:冻结;04:解冻;05:封存;06:启封;07:个人内部转移）
	 **/
	@Path("/operationAcctsList")
	@GET
	@Produces("application/json; charset=utf-8")
	public Response getOperationAcctsList(@Context HttpRequest httpRequest,final @QueryParam("XingMing") String XingMing,
										  final @QueryParam("YWWD") String YWWD,
										  final @QueryParam("ZJHM") String ZJHM, final @QueryParam("GRZH") String GRZH,
										  final @QueryParam("DWMC") String DWMC, final @QueryParam("ZhuangTai") String ZhuangTai,
										  final @QueryParam("CZMC") String CZMC, final @QueryParam("CZYY") String CZYY,
										  final @QueryParam("pageNo") String pageNo, final @QueryParam("pageSize") String pageSize,
										  final @QueryParam("KSSJ") String KSSJ, final @QueryParam("JSSJ") String JSSJ) {

		System.out.println("根据业务类型，获取该业务下的账户列表（以默认参数查询）");

		return ResUtils.wrapEntityIfNeeded(
				this.service.getOperationAcctsList((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWWD,XingMing, ZJHM, GRZH, DWMC, ZhuangTai, CZMC, CZYY, pageNo,pageSize,KSSJ,JSSJ));
	}


	/**
	 * 根据业务类型，获取该业务下的账户列表（以默认参数查询）
	 *
	 * @param XingMing
	 *            姓名
	 * @param ZJHM
	 *            证件号码
	 * @param GRZH
	 *            个人账号（开户业务时无此项）
	 * @param DWMC
	 *            单位名称
	 * @param CZYY
	 *            操作原因
	 * @param ZhuangTai
	 *            业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)
	 * @param CZMC
	 *            根据业务名称查询（01:设立；02:变更；03:冻结;04:解冻;05:封存;06:启封;07:个人内部转移）
	 **/
	@Path("/operationAcctsList/new")
	@GET
	@Produces("application/json; charset=utf-8")
	public Response getOperationAcctsList(@Context HttpRequest httpRequest,final @QueryParam("XingMing") String XingMing,
										  final @QueryParam("YWWD") String YWWD,
										  final @QueryParam("ZJHM") String ZJHM, final @QueryParam("GRZH") String GRZH,
										  final @QueryParam("DWMC") String DWMC, final @QueryParam("ZhuangTai") String ZhuangTai,
										  final @QueryParam("CZMC") String CZMC, final @QueryParam("CZYY") String CZYY,
										  final @QueryParam("marker")String marker,final @QueryParam("action") String action, final @QueryParam("pageSize") String pageSize,
										  final @QueryParam("KSSJ") String KSSJ, final @QueryParam("JSSJ") String JSSJ) {

		System.out.println("根据业务类型，获取该业务下的账户列表（以默认参数查询）");

		return ResUtils.wrapEntityIfNeeded(this.service.getOperationAcctsList((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWWD,XingMing, ZJHM, GRZH, DWMC, ZhuangTai, CZMC, CZYY, marker,action,pageSize,KSSJ,JSSJ));
	}
	/**
	 * 个人账户Action（封存、启封、冻结、解冻）创建时，根据个人账号自动获取相关信息
	 *
	 * @param GRZH
	 **/
	@Path("/IndiAcctActionAuto/{CZMC}/{GRZH}")
	@GET
	@Produces("application/json; charset=utf-8")
	public Response getIndiAcctActionAuto(@Context HttpRequest httpRequest,final @PathParam("GRZH") String GRZH, final @PathParam("CZMC") String CZMC) {

		System.out.println("个人账户Action（封存、启封、冻结、解冻）创建情时，根据个人账号自动获取相关信息");

		return ResUtils.wrapEntityIfNeeded(this.service.getIndiAcctActionAuto((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),GRZH, CZMC));
	}

	/**
	 * 个人账户合并业务时，预添加账户信息查询
	 *
	 * @param XingMing
	 * @param ZJLX
	 * @param ZJHM
	 **/
	@Path("/IndiAcctMergeAuto")
	@GET
	@Produces("application/json; charset=utf-8")
	public Response getIndiAcctMergeAuto(@Context HttpRequest httpRequest,final @QueryParam("XingMing") String XingMing,
										 final @QueryParam("ZJLX") String ZJLX, final @QueryParam("ZJHM") String ZJHM) {

		System.out.println("个人账户合并业务的新建/修改时，根据参数查询相关账户信息");

		return ResUtils.wrapEntityIfNeeded(this.service.getIndiAcctMergeAuto((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),XingMing, ZJLX, ZJHM));
	}

	/**
	 * 新建个人账户内部转移
	 **/
	@Path("/IndiAcctTransfer")
	@POST
	@Produces("application/json; charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postAcctTransfer(@Context HttpRequest httpRequest,final RequestWrapper<IndiAcctTransferPost> indiaccttransferpost) {

		System.out.println("新建个人账户内部转移");

		return ResUtils.wrapEntityIfNeeded(
				this.service.postAcctTransfer((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),indiaccttransferpost == null ? null : indiaccttransferpost.getReq()));
	}

	/**
	 * 个人账户设立修改
	 *
	 * @param YWLSH
	 *            业务流水号
	 **/
	@Path("/IndiAcctSet/{YWLSH}")
	@PUT
	@Produces("application/json; charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putIndiAcctSet(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH,
								   final RequestWrapper<IndiAcctSetPut> indiacctsetput) {

		System.out.println("个人账户设立修改");

		return ResUtils.wrapEntityIfNeeded(
				this.service.putIndiAcctSet((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH, indiacctsetput == null ? null : indiacctsetput.getReq()));
	}

	/**
	 * 个人登记（开户）回执单
	 *
	 * @param YWLSH
	 *            业务流水号
	 **/
	@Path("/IndiAcctSet/receipt/{YWLSH}")
	@GET
	@Produces("application/json; charset=utf-8")
	public Response headIndiAcctSet(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH) {

		System.out.println("个人登记（开户）回执单");

		return ResUtils.wrapEntityIfNeeded(this.service.headIndiAcctSet((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH));
	}

	/**
	 * 设立个人账户时，查询身份信息
	 **/
	@Path("/IndiAcctSetCheck")
	@GET
	@Produces("application/json; charset=utf-8")
	public Response getIndiAcctSetCheck(@Context HttpRequest httpRequest,final @QueryParam("XingMing") String XingMing,
										final @QueryParam("ZJLX") String ZJLX,
										final @QueryParam("ZJHM") String ZJHM) {

		System.out.println("设立个人账户详情");

		return ResUtils.wrapEntityIfNeeded(this.service.getIndiAcctSetCheck((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),XingMing,ZJLX,ZJHM));
	}

	@Path("/getPersonDepositDetails")
	@GET
	@Produces("application/json; charset=utf-8")
	public Response getPersonDepositDetails(@Context HttpRequest httpRequest,final @QueryParam("GRZH") String GRZH,
										final @QueryParam("pageNo") String pageNo,
										final @QueryParam("pageSize") String pageSize) {

		System.out.println("个人缴存明细");

		return ResUtils.wrapEntityIfNeeded(this.service.getPersonDepositDetails((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),GRZH,pageNo,pageSize));
	}

	/**
	 * 银行列表
	 **/
	@Path("/banks")
	@GET
	@Produces("application/json; charset=utf-8")
	public Response getbank(@Context HttpRequest httpRequest,final @QueryParam("Code") String Code , final  @QueryParam("Name") String  Name, final @QueryParam("pageNo") String pageNo, final @QueryParam("pageSize") String pageSize) {

		System.out.println("银行列表");

		return ResUtils.wrapEntityIfNeeded(this.service.getBanks((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),Code, Name, pageNo,pageSize));
	}


	/**
	 * 银行列表
	 **/
	@Path("/banks/new")
	@GET
	@Produces("application/json; charset=utf-8")
	public Response getbank(@Context HttpRequest httpRequest,final @QueryParam("Code") String Code , final  @QueryParam("Name") String  Name, final @QueryParam("marker")String marker,final @QueryParam("action") String action, final @QueryParam("pageSize") String pageSize) {

		System.out.println("银行列表");

		return ResUtils.wrapEntityIfNeeded(this.service.getBanks((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),Code, Name, marker,action,pageSize));
	}
	/**
	 * 获取证件号码相同的账号
	 **/
	@Path("/personaccount")
	@GET
	@Produces("application/json; charset=utf-8")
	public Response getAccounts(@Context HttpRequest httpRequest,final @QueryParam("ZJHM") String ZJHM,@QueryParam("GLXH")String GLXH) {

		System.out.println("获取证件号码相同的账号");

		return ResUtils.wrapEntityIfNeeded(this.service.getAccounts((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),ZJHM,GLXH));
	}

	/*
    *  转移列表
    * */
	@Path("/transferList")
	@GET
	@Produces("application/json; charset=utf-8")
	public Response getTransferList(TokenContext tokenContext, @QueryParam("Xingming") String Xingming,  @QueryParam("GRZH") String GRZH, @QueryParam("ZCDWM") String ZCDWM, @QueryParam("ZRDWM") String ZRDWM, @QueryParam("ZJHM") String ZJHM,@QueryParam("pageNo") String page, @QueryParam("pageSize") String pagesize,final @QueryParam("KSSJ") String KSSJ, final @QueryParam("JSSJ") String JSSJ){

		System.out.println("转移列表");

		return ResUtils.wrapEntityIfNeeded(this.service.getTransferList(tokenContext,Xingming,GRZH, ZCDWM,ZRDWM, ZJHM, page, pagesize, KSSJ, JSSJ));

	}

	/*
    *  转移列表
    * */
	@Path("/transferList/new")
	@GET
	@Produces("application/json; charset=utf-8")
	public Response getTransferList(TokenContext tokenContext, @QueryParam("Xingming") String Xingming,  @QueryParam("GRZH") String GRZH, @QueryParam("ZCDWM") String ZCDWM, @QueryParam("ZRDWM") String ZRDWM, @QueryParam("ZJHM") String ZJHM,final @QueryParam("marker")String marker,final @QueryParam("action") String action, @QueryParam("pageSize") String pagesize,final @QueryParam("KSSJ") String KSSJ, final @QueryParam("JSSJ") String JSSJ){

		System.out.println("转移列表");

		return ResUtils.wrapEntityIfNeeded(this.service.getTransferList(tokenContext,Xingming,GRZH, ZCDWM,ZRDWM,ZJHM ,marker, action,pagesize,KSSJ,JSSJ));

	}
	/**
	 * 设立个人账户详情
	 *
	 * @param YWLSH 业务流水号
	 **/
	@Path("/IndiAcctSet/{YWLSH}")
	@GET
	@Produces("application/json; charset=utf-8")
	public Response getIndiAcctSet(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH) {

		System.out.println("设立个人账户详情");

		return ResUtils.wrapEntityIfNeeded(this.service.getIndiAcctSet((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH));
	}



	/**
	 * 新建设立个人账户
	 **/
	@Path("/IndiAcctSet")
	@POST
	@Produces("application/json; charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postIndiAcctSet(@Context HttpRequest httpRequest,final RequestWrapper<IndiAcctSetPost> indiacctsetpost) {

		System.out.println("新建设立个人账户");

		return ResUtils.wrapEntityIfNeeded(this.service.postIndiAcctSet((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),indiacctsetpost == null ? null : indiacctsetpost.getReq()));
	}

	/**
	 * 内部转移
	 **/
	@Path("/InnerTransfer")
	@POST
	@Produces("application/json; charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response doInnerTransfer(@Context HttpRequest httpRequest, final RequestWrapper<InnerTransferPost> innerTransferPost) {

		System.out.println("内部转移");

		return ResUtils.wrapEntityIfNeeded(this.service.doInnerTransfer((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),innerTransferPost == null ? null : innerTransferPost.getReq()));
	}

	/**
	 * 查询个人账户信息列表
	 *
	 * @param DWMC
	 *            单位名称
	 * @param GRZH
	 *            个人账号
	 * @param XingMing
	 *            姓名
	 * @param ZJHM
	 *            证件号码
	 * @param GRZHZT
	 *            个人账户状态(00:所有；01：正常；:02:封存；03:合并销户；04:外部转出销户；05：提取销户；06：冻结；99：其他)
	 **/
	@Path("/indiAcctsList")
	@GET
	@Produces("application/json; charset=utf-8")
	public Response getIndiAcctsList(@Context HttpRequest httpRequest,final @QueryParam("DWMC") String DWMC, final @QueryParam("GRZH") String GRZH,
									 final @QueryParam("XingMing") String XingMing, final @QueryParam("ZJHM") String ZJHM,
									 final @QueryParam("GRZHZT") String GRZHZT, final @QueryParam("YWWD") String YWWD,
									 final @QueryParam("SFDJ") String SFDJ ,final @QueryParam("START") String startTime, final @QueryParam("END") String endTime,final @QueryParam("pageNo") String pageNo, final @QueryParam("pageSize") String pageSize) {

		System.out.println("查询个人账户信息列表");

		return ResUtils.wrapEntityIfNeeded(this.service.getIndiAcctsList((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),DWMC, GRZH, XingMing, ZJHM, GRZHZT,YWWD,SFDJ,startTime,endTime, pageNo, pageSize));
	}

	/**
	 * 修改个人账户变更
	 *
	 * @param YWLSH
	 *            业务流水号
	 **/
	@Path("/AlterIndiAcct/{YWLSH}")
	@PUT
	@Produces("application/json; charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putIndiAcctAlter(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH,
									 final RequestWrapper<IndiAcctAlterPut> indiacctalterput) {

		System.out.println("修改个人账户变更");

		return ResUtils.wrapEntityIfNeeded(
				this.service.putIndiAcctAlter((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH, indiacctalterput == null ? null : indiacctalterput.getReq()));
	}

	/**
	 * 个人信息变更回执单
	 *
	 * @param YWLSH
	 *            业务流水号
	 **/
	@Path("/AlterIndiAcct/receipt/{YWLSH}")
	@GET
	@Produces("application/json; charset=utf-8")
	public Response headIndiAcctAlter(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH) {

		System.out.println("个人信息变更回执单");

		return ResUtils.wrapEntityIfNeeded(this.service.headIndiAcctAlter((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH));
	}

	/**
	 * 变更个人账户详情
	 *
	 * @param YWLSH
	 *            业务流水号
	 **/
	@Path("/AlterIndiAcct/{YWLSH}")
	@GET
	@Produces("application/json; charset=utf-8")
	public Response getIndiAcctAlter(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH) {

		System.out.println("变更个人账户详情");

		return ResUtils.wrapEntityIfNeeded(this.service.getIndiAcctAlter((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH));
	}

	/**
	 *个人账户冻结提交
	 **/
	@Path("/IndiAcctFreezeSubmit")
	@POST
	@Produces("application/json; charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putIndiAcctFreezeSubmit(@Context HttpRequest httpRequest,final  RequestWrapper<IndiAcctFreezeSubmitPost> indiacctfreezesubmitpost){

		System.out.println("个人账户冻结提交");

		return ResUtils.wrapEntityIfNeeded(this.service.putIndiAcctFreezeSubmit((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY), indiacctfreezesubmitpost == null ? null : indiacctfreezesubmitpost.getReq())) ;
	}

	/**
	 *个人账户封存提交
	 **/
	@Path("/IndiAcctSealedSubmit")
	@POST
	@Produces("application/json; charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postIndiAcctSealedSubmit(@Context HttpRequest httpRequest,final  RequestWrapper<IndiAcctSealedSubmitPost> indiacctsealedsubmitpost){

		System.out.println("个人账户封存提交");

		return ResUtils.wrapEntityIfNeeded(this.service.postIndiAcctSealedSubmit((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY), indiacctsealedsubmitpost == null ? null : indiacctsealedsubmitpost.getReq())) ;
	}

	/**
	 *个人账户设立提交
	 **/
	@Path("/IndiAcctSetSubmit")
	@POST
	@Produces("application/json; charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postIndiAcctSetSubmit(@Context HttpRequest httpRequest,final  RequestWrapper<IndiAcctSetSubmitPost> indiacctsetsubmitpost){

		System.out.println("个人账户设立提交");

		return ResUtils.wrapEntityIfNeeded(this.service.postIndiAcctSetSubmit((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY), indiacctsetsubmitpost == null ? null : indiacctsetsubmitpost.getReq())) ;
	}

	/**
	 *个人账户内部转移提交
	 **/
	@Path("/IndiAcctTransferSubmit")
	@POST
	@Produces("application/json; charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postIndiAcctTransferSubmit(@Context HttpRequest httpRequest,final  RequestWrapper<IndiAcctTransferSubmitPost> indiaccttransfersubmitpost){

		System.out.println("个人账户内部转移提交");

		return ResUtils.wrapEntityIfNeeded(this.service.postIndiAcctTransferSubmit((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY), indiaccttransfersubmitpost == null ? null : indiaccttransfersubmitpost.getReq())) ;
	}

	/**
	 *个人账户合并提交
	 **/
	@Path("/IndiAcctMergeSubmit")
	@POST
	@Produces("application/json; charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postIndiAccttMergeSubmit(@Context HttpRequest httpRequest,final  RequestWrapper<IndiAccttMergeSubmitPost> indiaccttmergesubmitpost){

		System.out.println("个人账户合并提交");

		return ResUtils.wrapEntityIfNeeded(this.service.postIndiAccttMergeSubmit((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY), indiaccttmergesubmitpost == null ? null : indiaccttmergesubmitpost.getReq())) ;
	}

	/**
	 *个人账户变更提交
	 **/
	@Path("/AlterIndiAcctSubmit")
	@POST
	@Produces("application/json; charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postAlterIndiAcctSubmit(@Context HttpRequest httpRequest,final  RequestWrapper<AlterIndiAcctSubmitPost> alterindiacctsubmitpost){

		System.out.println("个人账户变更提交");

		return ResUtils.wrapEntityIfNeeded(this.service.postAlterIndiAcctSubmit((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),alterindiacctsubmitpost == null ? null : alterindiacctsubmitpost.getReq())) ;
	}

	/**
	 *个人账户启封提交
	 **/
	@Path("/IndiAcctUnsealedSubmit")
	@POST
	@Produces("application/json; charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postIndiAcctUnsealedSubmit(@Context HttpRequest httpRequest,final  RequestWrapper<IndiAcctUnsealedSubmitPost> indiacctunsealedsubmitpost){

		System.out.println("个人账户启封提交");

		return ResUtils.wrapEntityIfNeeded(this.service.postIndiAcctUnsealedSubmit((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY), indiacctunsealedsubmitpost == null ? null : indiacctunsealedsubmitpost.getReq())) ;
	}


	/**
	 * 合户
	 **/
	@Path("/mergeAccount")
	@POST
	@Produces("application/json; charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response doMerge(@Context HttpRequest httpRequest,final @QueryParam("YZJHM") String YZJHM,@QueryParam("XZJHM") String XZJHM,@QueryParam("XINGMING") String XINGMING,@QueryParam("GRCKZHHM")String GRCKZHHM){

		System.out.println("合户");

		return ResUtils.wrapEntityIfNeeded(this.service.doMerge((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY), YZJHM,XZJHM,XINGMING,GRCKZHHM)) ;

	}

	/**
	 *个人账户解冻提交
	 **/
	@Path("/IndiAcctUnfreezeSubmit")
	@POST
	@Produces("application/json; charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postIndiAcctUnfreezeSubmit(@Context HttpRequest httpRequest,final  RequestWrapper<IndiAcctUnfreezeSubmitPost> indiacctunfreezesubmitpost){

		System.out.println("个人账户解冻提交");

		return ResUtils.wrapEntityIfNeeded(this.service.postIndiAcctUnfreezeSubmit((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY), indiacctunfreezesubmitpost == null ? null : indiacctunfreezesubmitpost.getReq())) ;
	}

	/**
	 *GRZH查询个人账户信息
	 **/
	@Path("/IndiAcctsInfoDetails/{GRZH}")
	@GET
	@Produces("application/json; charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAcctsInfoDetails(@Context HttpRequest httpRequest,final @PathParam("GRZH") String GRZH){

		System.out.println("账号查询个人账户信息");

		return ResUtils.wrapEntityIfNeeded(this.service.getAcctsInfoDetails((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),GRZH)) ;
	}


	/**
	 *YWLSH
	 **/
	@Path("/IndiAcctMergeReceipt/{YWLSH}")
	@GET
	@Produces("application/json; charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getIndiAcctMergeReceipt(@Context HttpRequest httpRequest,final @PathParam("YWLSH") String YWLSH){

		System.out.println("打印合并转移回执单");

		return ResUtils.wrapEntityIfNeeded(this.service.getIndiAcctMergeReceipt((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),YWLSH)) ;
	}
	/**
	 *grzh
	 **/
	@Path("/PersonDepositPdfDetails/{grzh}")
	@GET
	@Produces("application/json; charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getPersonDepositPdfDetails(@Context HttpRequest httpRequest,final @PathParam("grzh") String grzh,final @QueryParam("HJNYS") String hjnys, final @QueryParam("HJNYE") String hjnye){

		System.out.println("打印缴存明细回执单");

		return ResUtils.wrapEntityIfNeeded(this.service.getPersonDepositPdfDetails((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),grzh,hjnys,hjnye)) ;
	}

	/**
	 *grzh
	 **/
	@Path("/diffTerritoryLoadProvePdf/{grzh}")
	@GET
	@Produces("application/json; charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response printDiffTerritoryLoadProvePdf(@Context HttpRequest httpRequest,final @PathParam("grzh") String grzh){

		System.out.println("打印异地贷款缴存证明回执单");

		return ResUtils.wrapEntityIfNeeded(this.service.getDiffTerritoryLoadProvePdf((TokenContext) httpRequest.getAttribute(Constant.Server.TOKEN_KEY),grzh)) ;
	}
}