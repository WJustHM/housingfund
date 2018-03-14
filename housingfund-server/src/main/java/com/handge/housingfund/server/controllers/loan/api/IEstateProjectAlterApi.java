package com.handge.housingfund.server.controllers.loan.api;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.individual.AlterIndiAcctSubmitPost;
import com.handge.housingfund.common.service.loan.model.EstateProjectInfo;
import com.handge.housingfund.common.service.loan.model.EstatePut;

/**
 * Created by 向超 on 2017/8/24.
 */
public interface IEstateProjectAlterApi<T>{

    /**
     * 添加变更楼盘信息
     * @param CZLX 操作类型 0 保存 1 提交
     * @param LPBH 楼盘编号
     * @param estateProjectInfo 添加变更楼盘信息
     * @return CommonResponses
     */
    public T addEstateProjectAlter(TokenContext tokenContext, String CZLX, String LPBH , EstateProjectInfo estateProjectInfo);

    /**
     * 修改变更楼盘信息
     * @param CZLX 操作类型 0 保存 1 提交
     * @param YWLSH  业务流水号
     * @param estatePut 修改变更楼盘信息
     * @return CommonResponses
     */
    public T reEstateProjectAlterInfo(TokenContext tokenContext,String CZLX, String YWLSH, EstatePut estatePut);

    /**
     * 变更楼盘详情
     * @param YWLSH 业务流水号
     * @return EstateIdGet
     */
    public T showEstateProjectAlterInfo(String YWLSH);



    /**
     * 受理变更楼盘列表
     * @param LPMC 变更楼盘名称
     * @param ZHUANGTAI 业务状态
     * @param pageNo  页码
     * @param pageSize 一页最大条数
     * @return PageRes
     */
    public T getEstateProjectAlterInfoAccept(TokenContext tokenContext,String LPMC, String ZHUANGTAI,String KSSJ,String JSSJ, String pageNo, String pageSize);

    /**
     * 受理变更楼盘列表
     * @param LPMC 变更楼盘名称
     * @param ZHUANGTAI 业务状态
     * @param marker  页码
     * @param pageSize 一页最大条数
     * @return PageRes
     */
    public T getEstateProjectAlterInfoAcceptNew(TokenContext tokenContext,String LPMC, String ZHUANGTAI,String KSSJ,String JSSJ, String marker, String pageSize,String action);


    /**
     * 审核通过后操作
     * @param YWLSH 业务流水号
     * @return ResponseEntity
     */
    public T doEstateProjectAlter(String YWLSH);

    /**
     * 批量提交操作
     * @param YWLSHS 业务流水号集合
     * @return
     */
    public T submitEstateProjectAlter(TokenContext tokenContext,AlterIndiAcctSubmitPost YWLSHS);

}
