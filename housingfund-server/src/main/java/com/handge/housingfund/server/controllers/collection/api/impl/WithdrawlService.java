package com.handge.housingfund.server.controllers.collection.api.impl;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.withdrawlModel.BatchWithdrawlsInfo;
import com.handge.housingfund.common.service.collection.model.withdrawlModel.ReWithdrawlsInfo;
import com.handge.housingfund.common.service.collection.model.withdrawlModel.WithdrawlsDetailInfo;
import com.handge.housingfund.common.service.collection.service.common.CommonOps;
import com.handge.housingfund.common.service.collection.service.withdrawl.WithdrawlTasks;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.collection.api.WithdrawlApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by xuefei_wang on 17-6-21.
 */


/**
 * Service 是线程安全的单例实现
 */
@SuppressWarnings("Duplicates")
@Component
public class WithdrawlService implements WithdrawlApi<Response> {

    @Autowired
    private CommonOps commonOps;

    @Autowired
    private WithdrawlTasks withdrawlTasks;


    /**
     * @param taskId 业务流水号
     * @return 提取详细信息 DetailInfo
     * 　查询指定业务流水号的提取详细信息
     */
    //complete
    public Response getWithdrawlDetails(TokenContext tokenContext, String taskId) {
        if (taskId == null) {
            return ResUtils.buildParametersErrorResponse();
        }
        try {
            return Response.status(200).entity(this.withdrawlTasks.getWithdrawlDetails(tokenContext, taskId)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }

    }

    /**
     * @param taskId 业务流水号
     * @param infos  修改后的提取任务基本信息
     * @return true/false
     */
    //complete
    public Response updateWithdrawlsTask(TokenContext tokenContext, String op, String taskId, ArrayList<ReWithdrawlsInfo> infos) {
        if (op == null || taskId == null || infos == null) {
            return ResUtils.buildParametersErrorResponse();
        }
        if (!Arrays.asList("0", "1").contains(op)) {
            return ResUtils.buildParametersErrorResponse();
        }
        try {
            return Response.status(200).entity(this.withdrawlTasks.updateWithdrawl(tokenContext, op, taskId, infos)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }

    }

    /**
     * @param taskId 业务流水号
     * @return true/false
     * 　撤销指定业务流水号的提取任务
     */
    //待完善
    public Response revokeWithdrawlsTask(TokenContext tokenContext, String taskId) {
        if (taskId == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {
            return Response.status(200).entity(this.commonOps.revokeOperation(tokenContext, taskId, "01")).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }
    }


    /**
     * @param info 提取信息
     * @return true/false
     * 　新建时，保存或提交提取任务
     */
    //complete
    public Response saveOrSubmitWithdrawlsTask(TokenContext tokenContext, String op, ArrayList<WithdrawlsDetailInfo> info) {
        if (op == null || info == null || !Arrays.asList("0", "1").contains(op)) {
            return ResUtils.buildParametersErrorResponse();
        }
        try {
            return Response.status(200).entity(withdrawlTasks.saveOrSubmitWithdrawl(tokenContext, op, info)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }

    }


    /**
     * 如果taskStatus为null　则　缺省为　all
     * 如果begain为null　则　缺省值从配置文件获取
     * 如果end为null 　则　缺省值为当前日期
     *
     * @param xm     　姓名
     * @param dwmc   　单位名称
     * @param grzh   　个人帐号
     * @param ywzt   　审核状态
     * @param begain 　起始时间戳
     * @param end    　　　结束时间戳
     * @return　TaskInfo 根据输入的条件查询提取记录
     * 1.根据获取到的字段，设置查询条件
     * 3.异常判断，具体处理统一待定
     * 4.返回查询结果
     */
    //complete
    public Response searchWithdrawlsTasks(TokenContext tokenContext, String xm, String dwmc, String grzh, String ywzt,String ywwd,String yhmc, String begain, String end, String pageNo, String pageSize,String zjhm,String ZongE,String tqyy) {

        try {
            return Response.status(200).entity(withdrawlTasks.searchWithdrawl(tokenContext, xm, dwmc, grzh, ywzt,ywwd,yhmc, begain, end, pageNo, pageSize,zjhm,ZongE,tqyy)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }
    }
    /**
     * 如果taskStatus为null　则　缺省为　all
     * 如果begain为null　则　缺省值从配置文件获取
     * 如果end为null 　则　缺省值为当前日期
     *
     * @param xm     　姓名
     * @param dwmc   　单位名称
     * @param grzh   　个人帐号
     * @param ywzt   　审核状态
     * @param begain 　起始时间戳
     * @param end    　　　结束时间戳
     * @return　TaskInfo 根据输入的条件查询提取记录
     * 1.根据获取到的字段，设置查询条件
     * 3.异常判断，具体处理统一待定
     * 4.返回查询结果
     */
    //complete
    public Response searchWithdrawlsTasksNew(TokenContext tokenContext, String xm, String dwmc, String grzh, String ywzt,String ywwd,String yhmc,  String begain, String end, String action,String marker, String pageSize,String zjhm,String ZongE) {

        try {
            return Response.status(200).entity(withdrawlTasks.searchWithdrawlNew(tokenContext, xm, dwmc, grzh, ywzt,ywwd,yhmc, begain, end, action,marker,pageSize,zjhm,ZongE)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * @param zjhm
     * @return ReadOnly
     * 获取指定账号的只读部分信息
     * 1.根据输入的‘个人账号’，查询“提取业务基础信息”，“提取业务任务”相关表
     * 2.异常判断，具体处理统一待定
     * 3.返回查询结果
     */
    public Response getWithdrawlsReadOnly(TokenContext tokenContext, String zjhm,String type) {
        try {
            return Response.status(200).entity(withdrawlTasks.getWithdrawlsReadOnly(tokenContext, zjhm,type)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * 批量操作提取记录 0提交 1删除 2打印回执
     *
     * @param batchWithdrawlsInfo
     * @return
     */
    //complete
    public Response batchOpWithdrawls(TokenContext tokenContext, BatchWithdrawlsInfo batchWithdrawlsInfo) {
        if (batchWithdrawlsInfo == null) {
            return ResUtils.buildParametersErrorResponse();
        }
        if (!Arrays.asList("0", "1", "2").contains(batchWithdrawlsInfo.getAction())) {
            return ResUtils.buildParametersErrorResponse();
        }
        try {
            return Response.status(200).entity(withdrawlTasks.batchOpWithdrawls(tokenContext, batchWithdrawlsInfo)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }

    }


    /**
     * 打印个人提取记录
     *
     * @param zjhm 证件号码
     * @return
     */
    //complete
    public Response printWithdrawlsRecords(TokenContext tokenContext, String zjhm, String begain, String end, String pageNo, String pageSize,String grzh,String XingMing) {

        try {
            return Response.status(200).entity(withdrawlTasks.printWithdrawlsRecords(tokenContext, zjhm, begain, end, pageNo, pageSize,grzh,XingMing)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * 搜索个人提取记录
     *
     * @param zjhm
     * @param begain
     * @param end
     * @return
     */
    public Response searchWithdrawlsRecords(TokenContext tokenContext, String zjhm, String begain, String end, String pageNo, String pageSize,String grzh,String XingMing) {

        try {
            return Response.status(200).entity(withdrawlTasks.searchWithdrawlsRecords(tokenContext, zjhm, begain, end, pageNo, pageSize, grzh, XingMing)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * 计算下次提取日期，当前提取日期为20170101，若当前余额为10000，月还款额为1000，则当前余额可还月份为10，下次提取日期为20171102
     *
     * @param yhke
     * @param fse  发生额
     * @return
     */
    public Response getNextDate(TokenContext tokenContext, String fse, String yhke) {
        if (yhke == null || fse == null) {
            return ResUtils.buildParametersErrorResponse();
        }

        try {
            return Response.status(200).entity(withdrawlTasks.getNextDate(tokenContext, fse, yhke)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }
    }

    /**
     * 获取发生利息额
     *
     * @param grzh
     * @return
     */
    public Response getFslxe(TokenContext tokenContext, String grzh) {
        if (grzh == null) {
            return ResUtils.buildParametersErrorResponse();
        }
        try {
            return Response.status(200).entity(withdrawlTasks.getFslxe(tokenContext, grzh)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(e.getError()).build();
        }
    }

    @Override
    public Response doFailedWithdrawl(TokenContext tokenContext, String ywlsh, String operation) {
        try{
            return Response.status(200).entity(withdrawlTasks.doFailedWithdrawl(tokenContext,ywlsh,operation)).build();
        }catch (ErrorException e){
            return Response.status(200).entity(e.getError()).build();
        }
    }
}