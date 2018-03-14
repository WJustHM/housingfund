package com.handge.housingfund.common.service.loan;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.loan.model.CommonResponses;
import com.handge.housingfund.common.service.loan.model.EstateIdGet;
import com.handge.housingfund.common.service.loan.model.EstateProjectInfo;
import com.handge.housingfund.common.service.loan.model.EstatePut;

import java.util.List;

/**
 * Created by 向超 on 2017/8/24.
 */
public interface IEstateProjectAlter {

    /**
     * 添加变更楼盘信息
     * @param CZLX 操作类型 0 保存 1 提交
     *  @param LPBH 楼盘编号
     * @param estateProjectInfo 添加变更楼盘信息
     * @return CommonResponses
     */
    public CommonResponses addEstateProjectAlter(TokenContext tokenContext, String CZLX, String LPBH, EstateProjectInfo estateProjectInfo);

    /**
     * 修改变更楼盘信息
     * @param CZLX 操作类型 0 保存 1 提交
     * @param YWLSH  业务流水号
     * @param estatePut 修改变更楼盘信息
     * @return CommonResponses
     */
    public CommonResponses reEstateProjectAlterInfo(TokenContext tokenContext,String CZLX, String YWLSH, EstatePut estatePut);

    /**
     * 变更楼盘详情
     * @param YWLSH 业务流水号
     * @return EstateIdGet
     */
    public EstateIdGet showEstateProjectAlterInfo(String YWLSH);



    /**
     * 受理变更楼盘列表
     * @param LPMC 变更楼盘名称
     * @param ZHUANGTAI 业务状态
     * @param pageNo  页码
     * @param pageSize 一页最大条数
     * @return PageRes
     */
    public PageRes getEstateProjectAlterInfoAccept(TokenContext tokenContext,String LPMC, String ZHUANGTAI,String KSSJ,String JSSJ , String pageNo, String pageSize);

    /**
     * 受理变更楼盘列表
     * @param LPMC 变更楼盘名称
     * @param ZHUANGTAI 业务状态
     * @param marker  页码
     * @param pageSize 一页最大条数
     * @return PageRes
     */
    public PageResNew getEstateProjectAlterInfoAcceptNew(TokenContext tokenContext, String LPMC, String ZHUANGTAI, String KSSJ, String JSSJ , String marker, String pageSize,String action);



    /**
     * 审核通过后操作
     * @param YWLSH 业务流水号
     * @return ResponseEntity
     */
    public void doEstateProjectAlter(String YWLSH);

    /**
     * 批量提交操作
     * @param YWLSHS 业务流水号集合
     * @return
     */
    public CommonResponses submitEstateProjectAlter(TokenContext tokenContext,List<String> YWLSHS);



}
