package com.handge.housingfund.server.controllers.loan.api;


import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.individual.AlterIndiAcctSubmitPost;
import com.handge.housingfund.common.service.loan.model.*;

public interface IHousingCompanyApi<T> {

    //查询过程中房开信息
    public T getHouingCompanyAccept(TokenContext tokenContext,String FKGS, String ZHUANGTAI,String KSSJ,String JSSJ, String pageNo, String pageSize);

    //查询过程中房开信息
    public T getHouingCompanyAcceptNew(TokenContext tokenContext,String FKGS, String ZHUANGTAI,String KSSJ,String JSSJ, String pageNo, String pageSize,String action);

    /**
     * 新增变更房开公司信息
     **/
    public T postModifyHousingCompanyInfomation(TokenContext tokenContext,final HousingCompanyPost housingcompanyinfo);

    /**
     * 批量提交房开
     * @param YWLSHS
     * @return
     */
    public T submitHousingCompany(TokenContext tokenContext,AlterIndiAcctSubmitPost YWLSHS);


    /**
     * 批量操作房开信息
     *
     * @param TYPE 类型(0申请，1变更)
     **/
    public T putBatchHousingCompanyInfomation(final String TYPE, final BatchHousingCompanyInfo batchhousingcompanyinfo);


    /**
     * 删除房开公司信息，修改数据库字段状态
     *
     * @param TYPE 类型(0申请，1变更)
     **/
    public T deleteHousingCompanyInformation(final String TYPE, final DelList dellist);


    /**
     * 审核房开信息
     *
     * @param YWLSH 业务流水号
     **/
    public T putHousingCompanyReview(final String YWLSH, final HousingCompanyReviewInfo reviewInfo);


    /**
     * 房开公司列表
     *
     * @param FKGS 房开公司
     * @param FKZH 房开账号
     **/
    public T getHousingCompanyList(TokenContext tokenContext,final String FKGS, final String FKZH, final String SFQY,final String kssj, final String jssj, final String pageNo, final String pageSize);

    /**
     * 房开公司列表
     *
     * @param FKGS 房开公司
     * @param FKZH 房开账号
     **/
    public T getHousingCompanyListNew(TokenContext tokenContext,final String FKGS, final String FKZH, final String SFQY,final String kssj, final String jssj, final String marker, final String pageSize,final String action);


    /**
     * 新增房开公司信息
     **/
    public T postHousingCompanyInfomation(TokenContext tokenContext, final String CZLX, final HousingCompanyPost housingcompanyinfo);


    /**
     * 打印申请回执
     *
     * @param YWLSH 业务流水号
     **/
    public T getApplyHousingCompanyReceipt(final String YWLSH);


    //根据房开公司与房开账号查询房开信息
    public T getHousingCompany(String FKGS, String FKZH, String YWWD,String pageNo, String pageSize);

    /**
     * 提交与撤回
     *
     * @param YWLSH     业务流水号
     * @param operation 状态（0：提交 1：撤回）
     **/
    public T putHousingCompanyStatus(final String YWLSH, final String operation);


    /**
     * 批量审核房开信息
     *
     * @param action 0审核通过，1审核不通过
     **/
    public T putHousingCompanyBatchReview(final String action, final HousingCompanyBatchReviewInfo housingcompanybatchreviewinfo);


    /**
     * 停用/启用房开公司
     *
     * @param id    id
     * @param state (1：启用，2：停用)
     **/
    public T putHousingCompanystate(final String id, final String state);


}