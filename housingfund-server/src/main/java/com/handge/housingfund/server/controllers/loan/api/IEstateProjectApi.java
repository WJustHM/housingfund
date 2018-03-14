package com.handge.housingfund.server.controllers.loan.api;


import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.individual.AlterIndiAcctSubmitPost;
import com.handge.housingfund.common.service.loan.model.BatchEstateProjectInfo;
import com.handge.housingfund.common.service.loan.model.EstateProjectBatchReviewInfo;
import com.handge.housingfund.common.service.loan.model.EstateProjectInfo;

public interface IEstateProjectApi<T> {


    /**
     * 新增变更楼盘项目
     *
     * @param action 状态（0：保存 1：提交）
     * @param LPBH   楼盘编号
     **/
    public T postModifyEstate(final String action, final String LPBH, final EstateProjectInfo estateprojectinfo);


    /**
     * 批量审核楼盘信息
     *
     * @param action 0审核通过，1审核不通过
     **/
    public T putEstateProjectBatchReview(final String action, final EstateProjectBatchReviewInfo estateprojectbatchreviewinfo);


    /**
     * 楼盘项目列表
     *
     * @param LPMC 楼盘名称
     * @param FKGS 房开公司
     **/
    public T getEstateList(TokenContext tokenContext,final String LPMC, final String FKGS,final String SFQY,final String YWWD, String pageNo, String pageSize);

    /**
     * 受理楼盘列表
     *
     * @param LPMC      楼盘名称
     * @param ZHUANGTAI 状态
     **/
    public T getEstateProjectInfoAccept(TokenContext tokenContext,String LPMC, String ZHUANGTAI,String KSSJ,String JSSJ, String pageNo, String pageSize);


    /**
     * 受理楼盘列表
     *
     * @param LPMC      楼盘名称
     * @param ZHUANGTAI 状态
     **/
    public T getEstateProjectInfoAcceptNew(TokenContext tokenContext,String LPMC, String ZHUANGTAI,String KSSJ,String JSSJ, String pageNo, String pageSize,String action);

    /**
     * 批量提交楼盘
     * @param YWLSHS
     * @return
     */
    public T submitEstateProject(TokenContext tokenContext,AlterIndiAcctSubmitPost YWLSHS);

    /**
     * 新增楼盘项目
     *
     * @param action 状态（0：保存 1：提交）
     **/
    public T postEstate(TokenContext tokenContext, final String action, final EstateProjectInfo estateprojectinfo);

    /**
     * 查询审核后房开信息
     *
     * @param LPBH    楼盘名称
     * @param SFQY    是否启用
     **/
    public T reEstateProjectSFQY(TokenContext tokenContext,String LPBH,String SFQY);


    /**
     * 单独修改状态（提交与撤回）
     *
     * @param LPBH   楼盘编号
     * @param status 状态（0：提交 1：撤回）
     **/
    public T putEstateStatusSubmits(final String LPBH, final String status);


    /**
     * 批量操作楼盘信息
     *
     * @param TYPE 类型(0申请，1变更)
     **/
    public T putBatchEstateStatusInfos(final String TYPE, final BatchEstateProjectInfo batchestateprojectinfo);

    /**
     * 打印回执
     *
     * @param YWLSH 类型(0申请，1变更)
     **/
    public T getEstateProjectsReceipts(final String YWLSH);


}