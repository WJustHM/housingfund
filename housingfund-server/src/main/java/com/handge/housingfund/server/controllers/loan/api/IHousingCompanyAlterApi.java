package com.handge.housingfund.server.controllers.loan.api;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.individual.AlterIndiAcctSubmitPost;
import com.handge.housingfund.common.service.loan.model.HousingCompanyPost;
import com.handge.housingfund.common.service.loan.model.HousingCompanyPut;

/**
 * Created by 向超 on 2017/8/24.
 */
public interface IHousingCompanyAlterApi<T> {

    /**
     * 添加变更房开信息
     * @param CZLX 操作类型 0 保存 1 提交
     * @param FKZH 房开账号
     * @param housingCompanyInfo 添加变更房开公司信息
     * @return CommonResponses
     */
    public T addHousingCompanyAlter(TokenContext tokenContext,String CZLX,String FKZH, HousingCompanyPost housingCompanyInfo);

    /**
     * 修改变更房开信息
     * @param YWLSH 业务流水号
     * @param CZLX 操作类型 0 保存 1 提交
     * @param housingCompanyInfo 修改变更房开公司信息
     * @return CommonResponses
     */
    public T reHousingCompanyAlterInfo(TokenContext tokenContext, String YWLSH, String CZLX, HousingCompanyPut housingCompanyInfo);

    /**
     * 变更房开详情
     * @param YWLSH 业务流水号
     * @return HousingIdGet
     */
    public T showHousingCompanyAlterInfo(String YWLSH);



    /**
     * 受理变更房开列表
     * @param FKZH 房开公司账号
     * @param FKGS 房开公司名称
     * @param pageNo 页码
     * @param pageSize 一页最大条数
     * @return PageRes
     */
    public T getHousingCompanyAlterInfoAccept(TokenContext tokenContext,String FKZH, String FKGS,String ZHUANGTAI,String KSSJ,String JSSJ, String pageNo, String pageSize);

    /**
     * 受理变更房开列表
     * @param FKZH 房开公司账号
     * @param FKGS 房开公司名称
     * @param marker 页码
     * @param pageSize 一页最大条数
     * @return PageRes
     */
    public T getHousingCompanyAlterInfoAcceptNew(TokenContext tokenContext,String FKZH, String FKGS,String ZHUANGTAI,String KSSJ,String JSSJ, String marker, String pageSize,String action);

    /**
     * 房开回执单
     * @param YWLSH 业务流水号
     * @return ApplyHousingCompanyReceipt
     */
    public T receiptHousingCompanyAlter(String YWLSH);



    /**
     * 批量提交操作
     * @param YWLSHS 业务流水号集合
     * @return CommonResponses
     */
    public T submitHousingCompanyAlter(TokenContext tokenContext,AlterIndiAcctSubmitPost YWLSHS);
}
