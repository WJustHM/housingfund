package com.handge.housingfund.server.controllers.collection.api;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.unit.*;

import javax.ws.rs.core.Response;

public interface UnitInfoManageApi<T> {


    /**
     * 单位封存列表
     *
     * @param DWMC      单位名称
     * @param DWLB      单位类别
     * @param DWZH      单位账号
     * @param ZhuangTai 状态
     **/
    public T getUnitAcctsSealedInfo(TokenContext tokenContext, final String DWMC, final String DWLB, final String DWZH, final String ZhuangTai,String KSSJ,String JSSJ, final String page, final String pageSize);


    /**
     * 单位封存列表
     *
     * @param DWMC      单位名称
     * @param DWLB      单位类别
     * @param DWZH      单位账号
     * @param ZhuangTai 状态
     **/
    public T getUnitAcctsSealedInfoNew(TokenContext tokenContext, final String DWMC, final String DWLB, final String DWZH, final String ZhuangTai,String KSSJ,String JSSJ, final String page, final String pageSize,final String action);


    /**
     * 单位账号启封修改
     *
     * @param YWLSH 业务流水号
     **/
    public T putUnitAcctUnsealed(TokenContext tokenContext,final String YWLSH, final UnitAcctUnsealedPut unitacctunsealedput);


    /**
     * 单位账号启封详情
     *
     * @param YWLSH 业务流水号
     **/
    public T getUnitAcctUnsealed(TokenContext tokenContext,final String YWLSH);


    /**
     * 单位变更列表
     *
     * @param DWMC      单位名称
     * @param DWLB      单位类别
     * @param DWZH      单位账号
     * @param ZhuangTai 状态
     **/
    public T getUnitAcctsAlterInfo(TokenContext tokenContext,final String DWMC, final String DWLB, final String DWZH, final String ZhuangTai,
                                   String page, String pageSize,String KSSJ,String JSSJ);


    /**
     * 单位变更列表
     *
     * @param DWMC      单位名称
     * @param DWLB      单位类别
     * @param DWZH      单位账号
     * @param ZhuangTai 状态
     **/
    public T getUnitAcctsAlterInfo(TokenContext tokenContext,final String DWMC, final String DWLB, final String DWZH, final String ZhuangTai,
                                   String marker,String action, String pageSize,String KSSJ,String JSSJ);
    /**
     * 单位启封列表
     *
     * @param DWMC      单位名称
     * @param DWLB      单位类别
     * @param DWZH      单位账号
     * @param ZhuangTai 状态
     **/
    public T getUnitAcctsUnsealedInfo(TokenContext tokenContext,final String DWMC, final String DWLB, final String DWZH, final String ZhuangTai, final String page, final String pageSize,String KSSJ,String JSSJ);

    /**
     * 单位启封列表
     *
     * @param DWMC      单位名称
     * @param DWLB      单位类别
     * @param DWZH      单位账号
     * @param ZhuangTai 状态
     **/
    public T getUnitAcctsUnsealedInfoNew(TokenContext tokenContext, final String DWMC, final String DWLB, final String DWZH, final String ZhuangTai, final String marker, final String pageSize, String listAction, String KSSJ, String JSSJ);


    /**
     * 查询单位账户信息列表
     *
     * @param DWZH   单位账号
     * @param DWMC   单位名称
     * @param DWLB   单位类别
     * @param DWZHZT 单位账号状态
     **/
    public T getUnitAcctsInfo(TokenContext tokenContext,final String SFXH,final String DWZH, final String DWMC, final String DWLB, final String DWZHZT,String YWWD, String startTime, String endTime,String page, String pageSize);


    /**
     * 查询单位账户信息列表
     *
     * @param DWZH   单位账号
     * @param DWMC   单位名称
     * @param DWLB   单位类别
     * @param DWZHZT 单位账号状态
     **/
    public T getUnitAcctsInfo(TokenContext tokenContext,final String SFXH,final String DWZH, final String DWMC, final String DWLB, final String DWZHZT,String YWWD, String startTime, String endTime,String marker,String action, String pageSize);

    public T getEmployeeList(TokenContext tokenContext, String DWZH, String XingMing, String page, String pagesize);

    /**
     * 单位账号封存修改
     *
     * @param YWLSH 业务流水号
     **/
    public T putUnitAcctSealed(TokenContext tokenContext,final String YWLSH, final UnitAcctSealedPut unitacctsealedput);


    /**
     * 单位账号封存详情
     *
     * @param YWLSH 业务流水号
     **/
    public T getUnitAcctSealed(TokenContext tokenContext,final String YWLSH);


    /**
     * 单位开户信息登记和变更回执单
     *
     * @param YWLSH 业务流水号
     **/
    public Response headUnitBasicReceipt(TokenContext tokenContext,final String YWLSH, final String YWLX);


    /**
     * 单位开户修改
     *
     * @param YWLSH 业务流水号
     **/
    public T putUnitAcctSet(TokenContext tokenContext,final String YWLSH, final UnitAcctSetPut unitacctsetput);


    /**
     * 单位开户详情
     *
     * @param YWLSH 业务流水号
     **/
    public T getUnitAcctSet(TokenContext tokenContext,final String YWLSH);


    /**
     * 单位开户列表
     *
     * @param DWMC      单位名称
     * @param DWLB      单位类别
     * @param ZhuangTai 状态
     **/
    public T getUnitAcctsSetInfo(TokenContext tokenContext,final String DWMC, final String DWLB, final String ZhuangTai, String page,
                                 String pageSize,String KSSJ,String JSSJ);


    /**
     * 单位开户列表
     *
     * @param DWMC      单位名称
     * @param DWLB      单位类别
     * @param ZhuangTai 状态
     **/
    public T getUnitAcctsSetInfo(TokenContext tokenContext,final String DWMC, final String DWLB, final String ZhuangTai, String marker,String action,
                                 String pageSize,String KSSJ,String JSSJ);
    /**
     * 新建单位账号启封
     **/
    public T postUnitAcctUnsealed(TokenContext tokenContext,final UnitAcctUnsealedPost unitacctunsealedpost);


    /**
     * 单位销户列表
     *
     * @param DWMC      单位名称
     * @param DWLB      单位类别
     * @param DWZH      单位账号
     * @param ZhuangTai 状态
     **/
    public T getUnitAcctsDropInfo(TokenContext tokenContext,final String DWMC, final String DWLB, final String DWZH, final String ZhuangTai,final String KSSJ,final String JSSJ, final String page, final String pageSize);


    /**
     * 单位销户列表
     *
     * @param DWMC      单位名称
     * @param DWLB      单位类别
     * @param DWZH      单位账号
     * @param ZhuangTai 状态
     **/
    public T getUnitAcctsDropInfoNew(TokenContext tokenContext,final String DWMC, final String DWLB, final String DWZH, final String ZhuangTai,final String KSSJ,final String JSSJ, final String marker, final String pageSize,String action);


    /**
     * 单位账户变更修改
     *
     * @param YWLSH
     **/
    public T putUnitAcctAlter(TokenContext tokenContext,final String YWLSH, final UnitAcctAlterPut unitacctalterput);


    /**
     * 单位账户变更详情
     *
     * @param YWLSH 业务流水号
     **/
    public T getUnitAcctAlter(TokenContext tokenContext,final String YWLSH);


    /**
     * 新建单位账号封存
     **/
    public T postUnitAcctSealed(TokenContext tokenContext,final UnitAcctSealedPost unitacctsealedpost);


    /**
     * 打印单位开户信息销户、启封、封存回执单
     *
     * @param YWLSH 业务流水号
     **/
    public T headUnitUnsealed(TokenContext tokenContext,final String YWLSH, final String YWLX);


    /**
     * 获取单位账号（销户、启封、封存）申请关键信息
     *
     * @param YWLSH 业务流水号
     **/
    public T getUnitAcctKeyword(TokenContext tokenContext,final String YWLSH);


    /**
     * 新建单位开户登记
     **/
    public T postUnitAcctSet(TokenContext tokenContext,final UnitAcctSetPost unitacctsetpost);


    /**
     * 单位账号销户修改
     *
     * @param YWLSH 业务流水号
     **/
    public T putUnitAcctDrop(TokenContext tokenContext,final String YWLSH, final UnitAcctDropPut unitacctdropput);


    /**
     * 单位账号销户详情
     *
     * @param YWLSH 业务流水号
     **/
    public T getUnitAcctDrop(TokenContext tokenContext,final String YWLSH);


    /**
     * 新建单位账户变更
     **/
    public T postUnitAcctAlter(TokenContext tokenContext,final UnitAcctAlterPost unitacctalterpost);


    /**
     * 新建单位账号销户
     **/
    public T postUnitAcctDrop(TokenContext tokenContext,final UnitAcctDropPost unitacctdroppost);


    /**
     * 根据单位账号, 获取单位开户信息
     *
     * @param DWZH 单位账号
     **/
    public T getUnitAcctDetailInfo(TokenContext tokenContext,final String DWZH);


    /**
     * 新建单位开户提交
     **/
    public T postUnitAcctSetSubmit(TokenContext tokenContext,final PostUnitAcctSetSubmit postunitacctsetsubmit);

    /**
     * 新建单位时名称验证
     **/
    public Response getUnitNameCheckMessage(String dwmc);

    /**
     * 单位变更提交
     **/
    public T postUnitAcctAlterSubmit(TokenContext tokenContext,final PostUnitAcctAlterSubmit postunitacctaltersubmit);


    /**
     * 新建单位账号销户提交
     **/
    public T postUnitAcctDropSubmit(TokenContext tokenContext,final PostUnitAcctDropSubmit postunitacctdropsubmit);


    /**
     * 单位账号启封提交
     **/
    public T postUnitAcctUnsealedSubmit(TokenContext tokenContext,final PostUnitAcctUnsealedSubmit postunitacctunsealedsubmit);


    /**
     * 单位账号封存提交
     **/
    public T postUnitAcctSealedSubmit(TokenContext tokenContext,final PostUnitAcctSealedSubmit postunitacctsealedsubmit);

}