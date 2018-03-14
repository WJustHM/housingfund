package com.handge.housingfund.server.controllers.collection.api.impl;


import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.collection.model.individual.*;
import com.handge.housingfund.common.service.collection.service.indiacctmanage.*;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.collection.api.IndiAcctManageApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.Arrays;

@SuppressWarnings("Duplicates")
@Component
public class IndiAcctManageService implements IndiAcctManageApi<Response> {

    @Autowired
    private IndiAcctMerge indiAcctMerge;

    @Autowired
    private IndiAcctAlter indiAcctAlter;

    @Autowired
    private IndiAcctSet indiAcctSet;

    @Autowired
    private IndiInnerTransfer indiInnerTransfer;

    @Autowired
    private IndiAcctsInfo indiAcctsInfo;

    @Autowired
    private IndiAcctFreeze indiAcctFreeze;

    @Autowired
    private IndiAcctUnfreeze indiAcctUnFreeze;

    @Autowired
    private IndiAcctUnseal indiAcctUnseal;

    @Autowired
    private IndiAcctSeal indiAcctSeal;


    /**
     * 修改合并个人账户
     *
     * @param YWLSH
     **/
    public Response putIndiAcctMerge(TokenContext tokenContext, final String YWLSH, final IndiAcctMergePut body) {

        System.out.println("修改合并个人账户");

        if (StringUtil.isEmpty(YWLSH) || body == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {
            return Response.status(200).entity(this.indiAcctMerge.reAcctMerge(YWLSH, body)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * 合并个人账户回执单
     *
     * @param YWLSH
     **/
    public Response headIndiAcctMerge(TokenContext tokenContext, final String YWLSH) {

        System.out.println("合并个人账户回执单");

        if (StringUtil.isEmpty(YWLSH)) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {
            return Response.status(200).entity(this.indiAcctMerge.headAcctMerge(YWLSH)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * 查看合并个人账户详情
     *
     * @param YWLSH
     **/
    public Response getIndiAcctMerge(TokenContext tokenContext, final String YWLSH) {

        System.out.println("查看合并个人账户详情");

        if (StringUtil.isEmpty(YWLSH)) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {
            return Response.status(200).entity(this.indiAcctMerge.showAcctMerge(YWLSH)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * 新建合并个人账户
     **/
    public Response postIndiAcctMerge(TokenContext tokenContext, final IndiAcctMergePost body) {

        System.out.println("新建合并个人账户");

        if (body == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {
            return Response.status(200).entity(this.indiAcctMerge.addAcctMerge(body)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }
    }


    /**
     * Action（封存、启封、冻结、解冻、托管）个人账户业务新建
     *
     * @param GRZH 个人账号
     **/
    public Response postIndiAcctAction(final TokenContext tokenContext, final String GRZH, final String CZMC, final IndiAcctActionPost body) {

        System.out.println("Action（封存、启封、冻结、解冻）个人账户业务新建");

        if (StringUtil.isEmpty(GRZH) || body == null || !Arrays.asList(
                CollectionBusinessType.封存.getCode(),
                CollectionBusinessType.启封.getCode(),
                CollectionBusinessType.冻结.getCode(),
                CollectionBusinessType.解冻.getCode()).contains(CZMC)) {
            return ResUtils.buildParametersErrorResponse();
        }

        if (CZMC.equals(CollectionBusinessType.封存.getCode())) {
            try {
                return Response.status(200).entity(this.indiAcctSeal.addAcctAction(tokenContext, GRZH, body)).build();
            } catch (ErrorException e) {
                return Response.status(200).entity(e.getError()).build();
            }
        }
        if (CZMC.equals(CollectionBusinessType.启封.getCode())) {
            try {
                return Response.status(200).entity(this.indiAcctUnseal.addAcctAction(tokenContext, GRZH, body)).build();
            } catch (ErrorException e) {
                return Response.status(200).entity(e.getError()).build();
            }
        }
        if (CZMC.equals(CollectionBusinessType.冻结.getCode())) {
            try {
                return Response.status(200).entity(this.indiAcctFreeze.addAcctAction(tokenContext, GRZH, body)).build();
            } catch (ErrorException e) {
                return Response.status(200).entity(e.getError()).build();
            }
        }
        if (CZMC.equals(CollectionBusinessType.解冻.getCode())) {
            try {
                return Response.status(200).entity(this.indiAcctUnFreeze.addAcctAction(tokenContext, GRZH, body)).build();
            } catch (ErrorException e) {
                return Response.status(200).entity(e.getError()).build();
            }
        }
        return null;
    }

    /**
     * 条件查询个人内部转移信息列表
     *
     * @param ZCDW      转出单位
     * @param ZhuangTai 状态
     **/
    public Response getIndiAcctsTransferList(TokenContext tokenContext, final String ZCDW, final String ZhuangTai) {

        System.out.println("条件查询个人内部转移信息列表");

        try {
            return Response.status(200).entity(this.indiInnerTransfer.getTransferInfo(ZCDW, ZhuangTai)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }
    }


    /**
     * Action（封存、启封、冻结、解冻）个人账户修改（71:合并;03:开户;06:内部转移;05:封存;04:启封;14:冻结;15:解冻;70:变更）
     *
     * @param YWLSH 业务流水号
     **/
    public Response putIndiAcctAction(final TokenContext tokenContext, final String YWLSH, final String CZMC, final IndiAcctActionPut body) {

        System.out.println("Action（封存、启封、冻结、解冻）个人账户修改");

        if (StringUtil.isEmpty(YWLSH) || body == null || !Arrays.asList(
                CollectionBusinessType.封存.getCode(),
                CollectionBusinessType.启封.getCode(),
                CollectionBusinessType.冻结.getCode(),
                CollectionBusinessType.解冻.getCode()).contains(CZMC)) {
            return ResUtils.buildParametersErrorResponse();
        }

        if (CZMC.equals(CollectionBusinessType.封存.getCode())) {
            try {
                return Response.status(200).entity(this.indiAcctSeal.reAcctAction(tokenContext, YWLSH, body)).build();
            } catch (ErrorException e) {
                return Response.status(200).entity(e.getError()).build();
            }
        }
        if (CZMC.equals(CollectionBusinessType.启封.getCode())) {
            try {
                return Response.status(200).entity(this.indiAcctUnseal.reAcctAction(tokenContext, YWLSH, body)).build();
            } catch (ErrorException e) {
                return Response.status(200).entity(e.getError()).build();
            }
        }
        if (CZMC.equals(CollectionBusinessType.冻结.getCode())) {
            try {
                return Response.status(200).entity(this.indiAcctFreeze.reAcctAction(tokenContext, YWLSH, body)).build();
            } catch (ErrorException e) {
                return Response.status(200).entity(e.getError()).build();
            }
        }
        if (CZMC.equals(CollectionBusinessType.解冻.getCode())) {
            try {
                return Response.status(200).entity(this.indiAcctUnFreeze.reAcctAction(tokenContext, YWLSH, body)).build();
            } catch (ErrorException e) {
                return Response.status(200).entity(e.getError()).build();
            }
        }
        return null;
    }

    /**
     * 个人账户Action（封存、启封、冻结、解冻）回执单（71:合并;03:开户;06:内部转移;05:封存;04:启封;14:冻结;15:解冻;70:变更）
     *
     * @param YWLSH 业务流水号
     **/
    public Response headIndiAcctAction(final TokenContext tokenContext, final String YWLSH, final String CZMC) {

        System.out.println("个人账户Action（封存、启封、冻结、解冻）回执单");

        if (StringUtil.isEmpty(YWLSH) || !Arrays.asList(
                CollectionBusinessType.封存.getCode(),
                CollectionBusinessType.启封.getCode(),
                CollectionBusinessType.冻结.getCode(),
                CollectionBusinessType.解冻.getCode()).contains(CZMC)) {
            return ResUtils.buildParametersErrorResponse();
        }

        if (CZMC.equals(CollectionBusinessType.封存.getCode())) {
            try {
                return Response.status(200).entity(this.indiAcctSeal.headAcctAction(tokenContext,YWLSH)).build();
            } catch (ErrorException e) {
                return Response.status(200).entity(e.getError()).build();
            }
        }
        if (CZMC.equals(CollectionBusinessType.启封.getCode())) {
            try {
                return Response.status(200).entity(this.indiAcctUnseal.headAcctAction(tokenContext,YWLSH)).build();
            } catch (ErrorException e) {
                return Response.status(200).entity(e.getError()).build();
            }
        }
        if (CZMC.equals(CollectionBusinessType.冻结.getCode())) {
            try {
                return Response.status(200).entity(this.indiAcctFreeze.headAcctAction(tokenContext,YWLSH)).build();
            } catch (ErrorException e) {
                return Response.status(200).entity(e.getError()).build();
            }
        }
        if (CZMC.equals(CollectionBusinessType.解冻.getCode())) {
            try {
                return Response.status(200).entity(this.indiAcctUnFreeze.headAcctAction(tokenContext,YWLSH)).build();
            } catch (ErrorException e) {
                return Response.status(200).entity(e.getError()).build();
            }
        }
        return null;
    }

    /**
     * 查看Action（封存、启封、冻结、解冻）个人账户详情（71:合并;03:开户;06:内部转移;05:封存;04:启封;14:冻结;15:解冻;70:变更）
     *
     * @param YWLSH 业务流水号
     **/
    public Response getIndiAcctAction(TokenContext tokenContext, final String YWLSH, final String CZMC) {

        System.out.println("查看Action（封存、启封、冻结、解冻）个人账户详情");

        if (StringUtil.isEmpty(YWLSH) || !Arrays.asList(
                CollectionBusinessType.封存.getCode(),
                CollectionBusinessType.启封.getCode(),
                CollectionBusinessType.冻结.getCode(),
                CollectionBusinessType.解冻.getCode()).contains(CZMC)) {
            return ResUtils.buildParametersErrorResponse();
        }

        if (CZMC.equals(CollectionBusinessType.封存.getCode())) {
            try {
                return Response.status(200).entity(this.indiAcctSeal.showAcctAction(YWLSH)).build();
            } catch (ErrorException e) {
                return Response.status(200).entity(e.getError()).build();
            }
        }
        if (CZMC.equals(CollectionBusinessType.启封.getCode())) {
            try {
                return Response.status(200).entity(this.indiAcctUnseal.showAcctAction(YWLSH)).build();
            } catch (ErrorException e) {
                return Response.status(200).entity(e.getError()).build();
            }
        }
        if (CZMC.equals(CollectionBusinessType.冻结.getCode())) {

            try {

                return Response.status(200).entity(this.indiAcctFreeze.showAcctAction(YWLSH)).build();

            } catch (ErrorException e) {

                return Response.status(200).entity(e.getError()).build();
            }
        }
        if (CZMC.equals(CollectionBusinessType.解冻.getCode())) {

            try {

                return Response.status(200).entity(this.indiAcctUnFreeze.showAcctAction(YWLSH)).build();

            } catch (ErrorException e) {

                return Response.status(200).entity(e.getError()).build();
            }
        }
        return null;
    }

    /**
     * 修改个人账户内部转移
     *
     * @param YWLSH 业务流水号
     **/
    public Response putIndiAcctTransfer(TokenContext tokenContext, final String YWLSH, final IndiAcctTransferPut body) {

        System.out.println("修改个人账户内部转移");

        if (StringUtil.isEmpty(YWLSH) || body == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.indiInnerTransfer.reAcctTransfer(YWLSH, body)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * 个人账户内部转移回执单
     *
     * @param YWLSH
     **/
    public Response headIndiAcctTransfer(TokenContext tokenContext, final String YWLSH) {

        System.out.println("个人账户内部转移回执单");

        if (StringUtil.isEmpty(YWLSH)) {
            return ResUtils.buildParametersErrorResponse();
        }
        try {

            return Response.status(200).entity(this.indiInnerTransfer.headIndiAcctTransfer(YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * 查看个人账户内部转移详情
     *
     * @param YWLSH 业务流水号
     **/
    public Response getIndiAcctTransfer(TokenContext tokenContext, final String YWLSH) {

        System.out.println("查看个人账户内部转移详情");

        if (StringUtil.isEmpty(YWLSH)) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.indiInnerTransfer.showAcctTransfer(YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * 新建个人账户变更
     **/
    public Response postIndiAcctAlter(TokenContext tokenContext, final IndiAcctAlterPost body) {

        System.out.println("新建个人账户变更");

        if (body == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.indiAcctAlter.addAcctAlter(tokenContext, body)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * 根据业务类型，获取该业务下的账户列表（以默认参数查询）
     *
     * @param XingMing  姓名
     * @param ZJHM      证件号码
     * @param GRZH      个人账号（开户业务时无此项）
     * @param DWMC      单位名称
     * @param ZhuangTai 业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)
     * @param CZMC      根据业务名称查询（71:合并;03:开户;06:内部转移;05:封存;04:启封;14:冻结;15:解冻;70:变更）
     **/
    public Response getOperationAcctsList(TokenContext tokenContext,final String YWWD, final String XingMing, final String ZJHM, final String GRZH,
                                          final String DWMC, final String ZhuangTai, final String CZMC, final String CZYY,
                                          String page, String pageSize,String KSSJ,String JSSJ) {

        System.out.println("根据业务类型，获取该业务下的账户列表（以默认参数查询）");

        if (StringUtil.notEmpty(CZMC) && !Arrays.asList(
                CollectionBusinessType.所有.getCode(),
                CollectionBusinessType.封存.getCode(),
                CollectionBusinessType.启封.getCode(),
                CollectionBusinessType.冻结.getCode(),
                CollectionBusinessType.解冻.getCode(),
                CollectionBusinessType.开户.getCode(),
                CollectionBusinessType.变更.getCode(),
                CollectionBusinessType.合并.getCode(),
                CollectionBusinessType.内部转移.getCode()).contains(CZMC)) {

            return ResUtils.buildParametersErrorResponse();
        }
        if (CZMC.equals(CollectionBusinessType.开户.getCode())) {

            try {

                return Response.status(200).entity(this.indiAcctSet.getAcctsSetInfo(tokenContext, DWMC, ZhuangTai, XingMing, ZJHM, CZMC, page, pageSize,KSSJ,JSSJ)).build();

            } catch (ErrorException e) {

                return Response.status(200).entity(e.getError()).build();
            }
        }
        if (CZMC.equals(CollectionBusinessType.变更.getCode())) {

            try {

                return Response.status(200).entity(this.indiAcctAlter.getAlterInfo(tokenContext,YWWD, DWMC, ZhuangTai, XingMing, ZJHM, GRZH, CZMC, page, pageSize,KSSJ,JSSJ)).build();

            } catch (ErrorException e) {

                return Response.status(200).entity(e.getError()).build();
            }

        }
        if (CZMC.equals(CollectionBusinessType.启封.getCode())) {

            try {

                return Response.status(200).entity(this.indiAcctUnseal.getAcctAcionInfo(tokenContext,DWMC, ZhuangTai, XingMing, ZJHM, GRZH, CZMC, CZYY,KSSJ,JSSJ, page, pageSize)).build();

            } catch (ErrorException e) {

                return Response.status(200).entity(e.getError()).build();
            }

        }
        if (CZMC.equals(CollectionBusinessType.封存.getCode())) {

            try {

                return Response.status(200).entity(this.indiAcctSeal.getAcctAcionInfo(tokenContext,DWMC, ZhuangTai, XingMing, ZJHM, GRZH, CZMC, CZYY,KSSJ,JSSJ, page, pageSize)).build();

            } catch (ErrorException e) {

                return Response.status(200).entity(e.getError()).build();
            }

        }
        if (CZMC.equals(CollectionBusinessType.冻结.getCode())) {

            try {

                return Response.status(200).entity(this.indiAcctFreeze.getAcctAcionInfo(tokenContext,DWMC, ZhuangTai, XingMing, ZJHM, GRZH, CZMC, CZYY,KSSJ,JSSJ, page, pageSize)).build();

            } catch (ErrorException e) {

                return Response.status(200).entity(e.getError()).build();
            }

        }
        if (CZMC.equals(CollectionBusinessType.解冻.getCode())) {

            try {

                return Response.status(200).entity(this.indiAcctUnFreeze.getAcctAcionInfo(tokenContext,DWMC, ZhuangTai, XingMing, ZJHM, GRZH, CZMC, CZYY,KSSJ,JSSJ, page, pageSize)).build();

            } catch (ErrorException e) {

                return Response.status(200).entity(e.getError()).build();
            }

        }
        if (CZMC.equals(CollectionBusinessType.合并.getCode())) {

            try {

                return Response.status(200).entity(this.indiAcctMerge.getMergeInfo(DWMC, ZhuangTai, XingMing, ZJHM, GRZH, CZMC)).build();

            } catch (ErrorException e) {

                return Response.status(200).entity(e.getError()).build();
            }

        }

        return null;

    }

    /**
     * 根据业务类型，获取该业务下的账户列表（以默认参数查询）
     *
     * @param XingMing  姓名
     * @param ZJHM      证件号码
     * @param GRZH      个人账号（开户业务时无此项）
     * @param DWMC      单位名称
     * @param ZhuangTai 业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)
     * @param CZMC      根据业务名称查询（71:合并;03:开户;06:内部转移;05:封存;04:启封;14:冻结;15:解冻;70:变更）
     **/
    public Response getOperationAcctsList(TokenContext tokenContext,final String YWWD, final String XingMing, final String ZJHM, final String GRZH,
                                          final String DWMC, final String ZhuangTai, final String CZMC, final String CZYY,
                                          String marker,String action, String pageSize,String KSSJ,String JSSJ) {

        System.out.println("根据业务类型，获取该业务下的账户列表（以默认参数查询）");

        if (StringUtil.notEmpty(CZMC) && !Arrays.asList(
                CollectionBusinessType.所有.getCode(),
                CollectionBusinessType.封存.getCode(),
                CollectionBusinessType.启封.getCode(),
                CollectionBusinessType.冻结.getCode(),
                CollectionBusinessType.解冻.getCode(),
                CollectionBusinessType.开户.getCode(),
                CollectionBusinessType.变更.getCode(),
                CollectionBusinessType.合并.getCode(),
                CollectionBusinessType.内部转移.getCode()).contains(CZMC)) {

            return ResUtils.buildParametersErrorResponse();
        }
        if (CZMC.equals(CollectionBusinessType.开户.getCode())) {

            try {

                return Response.status(200).entity(this.indiAcctSet.getAcctsSetInfo(tokenContext, DWMC, ZhuangTai, XingMing, ZJHM, CZMC, marker,action, pageSize,KSSJ,JSSJ)).build();

            } catch (ErrorException e) {

                return Response.status(200).entity(e.getError()).build();
            }
        }
        if (CZMC.equals(CollectionBusinessType.变更.getCode())) {

            try {

                return Response.status(200).entity(this.indiAcctAlter.getAlterInfo(tokenContext,YWWD, DWMC, ZhuangTai, XingMing, ZJHM, GRZH, CZMC, marker,action, pageSize,KSSJ,JSSJ)).build();

            } catch (ErrorException e) {

                return Response.status(200).entity(e.getError()).build();
            }

        }
        if (CZMC.equals(CollectionBusinessType.启封.getCode())) {

            try {

                return Response.status(200).entity(this.indiAcctUnseal.getAcctAcionInfoNew(tokenContext,DWMC, ZhuangTai, XingMing, ZJHM, GRZH, CZMC, CZYY,KSSJ,JSSJ, marker,pageSize, action)).build();

            } catch (ErrorException e) {

                return Response.status(200).entity(e.getError()).build();
            }

        }
        if (CZMC.equals(CollectionBusinessType.封存.getCode())) {

            try {

                return Response.status(200).entity(this.indiAcctSeal.getAcctAcionInfoNew(tokenContext,DWMC, ZhuangTai, XingMing, ZJHM, GRZH, CZMC, CZYY,KSSJ,JSSJ, marker,pageSize, action)).build();

            } catch (ErrorException e) {

                return Response.status(200).entity(e.getError()).build();
            }

        }
        if (CZMC.equals(CollectionBusinessType.冻结.getCode())) {

            try {

                return Response.status(200).entity(this.indiAcctFreeze.getAcctAcionInfoNew(tokenContext,DWMC, ZhuangTai, XingMing, ZJHM, GRZH, CZMC, CZYY,KSSJ,JSSJ, marker,pageSize, action)).build();

            } catch (ErrorException e) {

                return Response.status(200).entity(e.getError()).build();
            }

        }
        if (CZMC.equals(CollectionBusinessType.解冻.getCode())) {

            try {

                return Response.status(200).entity(this.indiAcctUnFreeze.getAcctAcionInfoNew(tokenContext,DWMC, ZhuangTai, XingMing, ZJHM, GRZH, CZMC, CZYY,KSSJ,JSSJ, marker,pageSize, action)).build();

            } catch (ErrorException e) {

                return Response.status(200).entity(e.getError()).build();
            }

        }
        if (CZMC.equals(CollectionBusinessType.合并.getCode())) {

            try {

                return Response.status(200).entity(this.indiAcctMerge.getMergeInfo(DWMC, ZhuangTai, XingMing, ZJHM, GRZH, CZMC)).build();

            } catch (ErrorException e) {

                return Response.status(200).entity(e.getError()).build();
            }

        }

        return null;

    }

    /**
     * 个人账户Action（封存、启封、冻结、解冻）创建情时，根据个人账号自动获取相关信息
     *
     * @param GRZH
     **/
    public Response getIndiAcctActionAuto(TokenContext tokenContext, final String GRZH, final String CZMC) {

        System.out.println("个人账户Action（封存、启封、冻结、解冻）创建情时，根据个人账号自动获取相关信息");

        if (StringUtil.isEmpty(GRZH) || !Arrays.asList(
                CollectionBusinessType.封存.getCode(),
                CollectionBusinessType.启封.getCode(),
                CollectionBusinessType.冻结.getCode(),
                CollectionBusinessType.解冻.getCode(),
                CollectionBusinessType.开户.getCode(),
                CollectionBusinessType.变更.getCode()).contains(CZMC)) {

            return ResUtils.buildParametersErrorResponse();
        }

        if (CZMC.equals(CollectionBusinessType.封存.getCode())) {
            try {

                return Response.status(200).entity(this.indiAcctSeal.AutoIndiAcctAction(tokenContext,GRZH)).build();

            } catch (ErrorException e) {

                return Response.status(200).entity(e.getError()).build();
            }
        }
        if (CZMC.equals(CollectionBusinessType.启封.getCode())) {
            try {

                return Response.status(200).entity(this.indiAcctUnseal.AutoIndiAcctAction(tokenContext,GRZH)).build();

            } catch (ErrorException e) {

                return Response.status(200).entity(e.getError()).build();
            }
        }
        if (CZMC.equals(CollectionBusinessType.冻结.getCode())) {
            try {

                return Response.status(200).entity(this.indiAcctFreeze.AutoIndiAcctAction(tokenContext,GRZH)).build();

            } catch (ErrorException e) {

                return Response.status(200).entity(e.getError()).build();
            }
        }
        if (CZMC.equals(CollectionBusinessType.解冻.getCode())) {
            try {

                return Response.status(200).entity(this.indiAcctUnFreeze.AutoIndiAcctAction(tokenContext,GRZH)).build();

            } catch (ErrorException e) {

                return Response.status(200).entity(e.getError()).build();
            }
        }
        return null;
    }

    public Response getIndiAcctMergeAuto(TokenContext tokenContext, final String XingMing, final String ZJLX, final String ZJHM) {

        System.out.println("个人账户合并业务新增/修改时，根据参数查询账号信息");

        if (StringUtil.isEmpty(ZJHM)) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {
            return Response.status(200).entity(this.indiAcctMerge.AutoIndiAcctMerge(XingMing, ZJLX, ZJHM)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * 新建个人账户内部转移
     **/
    public Response postAcctTransfer(TokenContext tokenContext, final IndiAcctTransferPost body) {

        System.out.println("新建个人账户内部转移");

        if (body == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.indiInnerTransfer.addAcctTransfer(body)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * 个人账户设立修改
     *
     * @param YWLSH 业务流水号
     **/
    public Response putIndiAcctSet(TokenContext tokenContext, final String YWLSH, final IndiAcctSetPut body) {

        System.out.println("个人账户设立修改");

        if (StringUtil.isEmpty(YWLSH) || body == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {
            return Response.status(200).entity(this.indiAcctSet.reAcctSet(tokenContext, YWLSH, body)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * 个人登记（开户）回执单
     *
     * @param YWLSH 业务流水号
     **/
    public Response headIndiAcctSet(TokenContext tokenContext, final String YWLSH) {

        System.out.println("个人登记（开户）回执单");

        if (StringUtil.isEmpty(YWLSH)) {

            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "业务流水号");
        }

        try {

            return Response.status(200).entity(this.indiAcctSet.headAcctSet(tokenContext, YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }

    }

    /**
     * 设立个人账户详情
     *
     * @param YWLSH 业务流水号
     **/
    public Response getIndiAcctSet(TokenContext tokenContext, final String YWLSH) {

        System.out.println("设立个人账户详情");

        if (StringUtil.isEmpty(YWLSH)) {
            return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS, "业务流水号");
        }

        try {

            return Response.status(200).entity(this.indiAcctSet.showAcctSet(tokenContext, YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }

    }


    /**
     * 内部转移
     **/
    public Response doInnerTransfer(TokenContext tokenContext, InnerTransferPost innerTransferPost) {

        System.out.println("设立个人账户详情");


        try {

            return Response.status(200).entity(this.indiAcctSet.doInnerTransfer(tokenContext, innerTransferPost)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }

    }


    /**
     * 新建设立个人账户
     **/
    public Response postIndiAcctSet(TokenContext tokenContext, final IndiAcctSetPost body) {

        System.out.println("新建设立个人账户");

        if (body == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {
            return Response.status(200).entity(this.indiAcctSet.addAcctSet(tokenContext, body)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * 查询个人账户信息列表
     *
     * @param DWMC     单位名称
     * @param GRZH     个人账号
     * @param XingMing 姓名
     * @param ZJHM     证件号码
     * @param GRZHZT   个人账户状态(00:所有；01：正常；:02:封存；03:合并销户；04:外部转出销户；05：提取销户；06：冻结；99：其他)
     **/
    public Response getIndiAcctsList(TokenContext tokenContext, final String DWMC, final String GRZH, final String XingMing, final String ZJHM,
                                     final String GRZHZT,final String YWWD,final String SFDJ ,String startTime, String endTime, String page, String pageSize) {

        System.out.println("查询个人账户信息列表");

        try {

            return Response.status(200).entity(this.indiAcctsInfo.getAcctsInfo(tokenContext, DWMC, GRZH, XingMing, ZJHM, GRZHZT,YWWD,SFDJ,startTime,endTime, page, pageSize)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * 查询个人账户信息列表
     *
     * @param DWMC     单位名称
     * @param GRZH     个人账号
     * @param XingMing 姓名
     * @param ZJHM     证件号码
     * @param GRZHZT   个人账户状态(00:所有；01：正常；:02:封存；03:合并销户；04:外部转出销户；05：提取销户；06：冻结；99：其他)
     **/
    public Response getIndiAcctsList_page(TokenContext tokenContext, final String DWMC, final String GRZH, final String XingMing, final String ZJHM,
                                     final String GRZHZT,final String YWWD,final String SFDJ ,String startTime, String endTime, String page, String pageSize) {

        System.out.println("查询个人账户信息列表");

        try {

            return Response.status(200).entity(this.indiAcctsInfo.getAcctsInfo(tokenContext, DWMC, GRZH, XingMing, ZJHM, GRZHZT,YWWD,SFDJ,startTime,endTime, page, pageSize)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }
    /**
     * 修改个人账户变更
     *
     * @param YWLSH 业务流水号
     **/
    public Response putIndiAcctAlter(TokenContext tokenContext, final String YWLSH, final IndiAcctAlterPut body) {

        System.out.println("修改个人账户变更");

        if (body == null) {

            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.indiAcctAlter.reAcctAlter(tokenContext, YWLSH, body)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * 个人信息变更回执单
     *
     * @param YWLSH 业务流水号
     **/
    public Response headIndiAcctAlter(TokenContext tokenContext, final String YWLSH) {

        System.out.println("个人信息变更回执单");

        if (StringUtil.isEmpty(YWLSH)) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.indiAcctAlter.headAcctAlter(tokenContext, YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * 变更个人账户详情
     *
     * @param YWLSH 业务流水号
     **/
    public Response getIndiAcctAlter(TokenContext tokenContext, final String YWLSH) {

        System.out.println("变更个人账户详情");

        if (StringUtil.isEmpty(YWLSH)) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {

            return Response.status(200).entity(this.indiAcctAlter.showAcctAlter(tokenContext, YWLSH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }


    /**
     * 个人账户冻结提交
     **/
    public Response putIndiAcctFreezeSubmit(TokenContext tokenContext, final IndiAcctFreezeSubmitPost body) {

        System.out.println("个人账户冻结提交");


        try {

            return Response.status(200).entity(this.indiAcctFreeze.submitIndiAcctAction(tokenContext,body.getYWLSHJH())).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }


    /**
     * 个人账户封存提交
     **/
    public Response postIndiAcctSealedSubmit(TokenContext tokenContext, final IndiAcctSealedSubmitPost body) {

        System.out.println("个人账户封存提交");

        try {

            return Response.status(200).entity(this.indiAcctSeal.submitIndiAcctAction(tokenContext,body.getYWLSHJH())).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }


    /**
     * 个人账户设立提交
     **/
    public Response postIndiAcctSetSubmit(TokenContext tokenContext, final IndiAcctSetSubmitPost body) {

        System.out.println("个人账户设立提交");

        try {

            return Response.status(200).entity(this.indiAcctSet.submitAcctSet(tokenContext, body.getYWLSHJH())).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }


    /**
     * 个人账户内部转移提交
     **/
    public Response postIndiAcctTransferSubmit(TokenContext tokenContext, final IndiAcctTransferSubmitPost body) {

        System.out.println("个人账户内部转移提交");

        try {

            return Response.status(200).entity(this.indiInnerTransfer.submitIndiAcctTransfer(body.getYWLSHJH())).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }


    /**
     * 个人账户合并提交
     **/
    @Deprecated
    public Response postIndiAccttMergeSubmit(TokenContext tokenContext, final IndiAccttMergeSubmitPost body) {

        System.out.println("个人账户合并提交");

        return null;
    }

    /**
     * 个人账户变更提交
     **/
    public Response postAlterIndiAcctSubmit(TokenContext tokenContext, final AlterIndiAcctSubmitPost body) {

        System.out.println("个人账户变更提交");

        try {

            return Response.status(200).entity(this.indiAcctAlter.submitAcctAlter(tokenContext, body.getYWLSHJH())).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * 合户
     **/
    public Response doMerge(TokenContext tokenContext,String YZJHM,String XZJHM,String XINGMING,String GRCKZHHM) {

        System.out.println("合户");

        try {

            return Response.status(200).entity(this.indiAcctAlter.doMerge(tokenContext,YZJHM,XZJHM,XINGMING,GRCKZHHM)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * 个人账户启封提交
     **/
    public Response postIndiAcctUnsealedSubmit(TokenContext tokenContext, final IndiAcctUnsealedSubmitPost body) {

        System.out.println("个人账户启封提交");

        try {

            return Response.status(200).entity(this.indiAcctUnseal.submitIndiAcctAction(tokenContext,body.getYWLSHJH())).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }


    /**
     * 个人账户解冻提交
     **/
    public Response postIndiAcctUnfreezeSubmit(TokenContext tokenContext, final IndiAcctUnfreezeSubmitPost body) {

        System.out.println("个人账户解冻提交");

        try {

            return Response.status(200).entity(this.indiAcctUnFreeze.submitIndiAcctAction(tokenContext,body.getYWLSHJH())).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

    @Override
    public Response getAcctsInfoDetails(TokenContext tokenContext, String GRZH) {
        System.out.println("账号查询个人账户信息");

        try {

            return Response.status(200).entity(this.indiAcctsInfo.getAcctsInfoDetails(tokenContext, GRZH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

    @Override
    public Response getBanks(TokenContext tokenContext,final String Code , final String  Name, final  String pageNo, final String pageSize) {

        System.out.println("银行列表");

        try {

            return Response.status(200).entity(this.indiAcctsInfo.getBanks(tokenContext,Code, Name, pageNo,pageSize)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }
    @Override
    public Response getBanks(TokenContext tokenContext,final String Code , final String  Name, String marker,String action, final String pageSize) {

        System.out.println("银行列表");

        try {

            return Response.status(200).entity(this.indiAcctsInfo.getBanks(tokenContext,Code, Name, marker,action,pageSize)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }
    @Override
    public Response getIndiAcctSetCheck(TokenContext tokenContext, String xingMing, String zjlx, String zjhm) {
        System.out.println("个人开户时检查个人信息");

        try {
            return Response.status(200).entity(this.indiAcctSet.getIndiAcctSetCheck(tokenContext, xingMing, zjlx, zjhm)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }
    }

    public Response getPersonDepositDetails(TokenContext tokenContext, String grzh, String pageNo,String pageSize){
        System.out.println("个人缴存明细");
        if(StringUtil.isEmpty(grzh)) return ResUtils.buildCommonErrorResponse(ReturnEnumeration.Parameter_MISS,"个人账户");
        try {
            return Response.status(200).entity(this.indiAcctsInfo.getPersonDepositDetails(tokenContext, grzh, pageNo, pageSize)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }
    }


    @Override
    public Response getAccounts(TokenContext tokenContext, String zjhm,String GLXH) {

        System.out.println("根据证件号码获取个人信息");

        try {

            return Response.status(200).entity(this.indiAcctsInfo.getAccounts(tokenContext,zjhm,GLXH)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }


    @Override
    public Response getTransferList(TokenContext tokenContext, String Xingming, String GRZH, String ZCDWM, String ZRDWM, String ZJHM, String page, String pagesize, String KSSJ, String JSSJ){

        System.out.println("转移列表");

        try {

            return Response.status(200).entity(this.indiAcctsInfo.getTransferList(tokenContext,Xingming,GRZH, ZCDWM,ZRDWM, ZJHM, page,pagesize,KSSJ,JSSJ)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

    @Override
    public Response getTransferList(TokenContext tokenContext, String Xingming, String GRZH, String ZCDWM, String ZRDWM, String ZJHM, String marker,String action, String pagesize, String KSSJ, String JSSJ ){

        System.out.println("转移列表");

        try {

            return Response.status(200).entity(this.indiAcctsInfo.getTransferList(tokenContext,Xingming,GRZH, ZCDWM,ZRDWM,ZJHM,marker,action,pagesize,KSSJ,JSSJ)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }


    @Override
    public Response  getIndiAcctMergeReceipt(TokenContext tokenContext, String ywlsh){

        System.out.println("打印合并转移回执单");

        try {

            return Response.status(200).entity(this.indiAcctsInfo.getTransferDetails(tokenContext,ywlsh)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }

    @Override
    public Response  getPersonDepositPdfDetails(TokenContext tokenContext, String grzh, String hjnys, String hjnye){

        System.out.println("打印缴存明细回执单");

        try {
            if(!StringUtil.notEmpty(hjnys) || !StringUtil.notEmpty(hjnye) ){
                throw new ErrorException(ReturnEnumeration.Parameter_MISS, "查询时间缺失");
            }
            if(!DateUtil.isFollowFormat(hjnys,"yyyy-MM" , false)){
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "开始时间格式不正确");
            }
            if(!DateUtil.isFollowFormat(hjnye,"yyyy-MM" , false)){
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "结束时间格式不正确");
            }
            int result = hjnys.compareTo(hjnye);

            if (result >0){
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "开始月份不能大于结束月份");
            }

            return Response.status(200).entity(this.indiAcctsInfo.getPersonDepositPdfDetails(tokenContext,grzh,hjnys,hjnye)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }
    @Override
    public Response getDiffTerritoryLoadProvePdf(TokenContext tokenContext, String grzh) {
        System.out.println("打印异地贷款缴存证明回执单");
        try {
            return Response.status(200).entity(this.indiAcctsInfo.getDiffTerritoryLoadProvePdf(tokenContext,grzh)).build();

        } catch (ErrorException e) {

            return Response.status(200).entity(e.getError()).build();
        }
    }
}