package com.handge.housingfund.common.service.loan;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.loan.model.*;

import java.util.List;

/**
 * Created by 向超 on 2017/8/9.
 */
public interface IEstateProject {

    /**
     * 楼盘列表
     * @param LPMC 楼盘名称
     * @param FKGS  房开公司名称
     * @param SFQY  是否启用
     * @param pageNo 页码
     * @param pageSize 一页最大条数
     * @return PageRes
     */
    public PageRes getEstateProjectInfo(TokenContext tokenContext,String LPMC, String FKGS,Boolean SFQY,String YWWD, String pageNo, String pageSize);

    /**
     * 添加楼盘信息
     * @param CZLX 操作类型 0 保存 1 提交
     * @param estateProjectInfo 添加楼盘信息
     * @return CommonResponses
     */
    public CommonResponses addEstateProject(TokenContext tokenContext, String CZLX, EstateProjectInfo estateProjectInfo);

    /**
     * 修改楼盘信息
     * @param CZLX 操作类型 0 保存 1 提交
     * @param YWLSH  业务流水号
     * @param estatePut 修改楼盘信息
     * @return CommonResponses
     */
    public CommonResponses reEstateProjectInfo(TokenContext tokenContext,String CZLX, String YWLSH, EstatePut estatePut);

    /**
     * 楼盘详情
     * @param YWLSH 业务流水号
     * @return EstateIdGet
     */
    public EstateIdGet showEstateProjectInfo(String YWLSH);


    /**
     * 受理楼盘列表
     * @param LPMC 楼盘名称
     * @param ZHUANGTAI 业务状态
     * @param pageNo  页码
     * @param pageSize 一页最大条数
     * @return PageRes
     */
    public PageRes getEstateProjectInfoAccept(TokenContext tokenContext,String LPMC, String ZHUANGTAI,String KSSJ,String JSSJ , String pageNo, String pageSize);

    /**
     * 受理楼盘列表
     * @param LPMC 楼盘名称
     * @param ZHUANGTAI 业务状态
     * @param marker  页码
     * @param pageSize 一页最大条数
     * @return PageRes
     */
    public PageResNew getEstateProjectInfoAcceptNew(TokenContext tokenContext, String LPMC, String ZHUANGTAI, String KSSJ, String JSSJ , String marker, String pageSize, String action);


    /**
     * 审核通过后操作
     * @param YWLSH 业务流水号
     * @return ResponseEntity
     */
    public void doEstateProject(String YWLSH);

    /**
     * 批量提交操作
     * @param YWLSHS 业务流水号集合
     * @return
     */
    public CommonResponses submitEstateProject(TokenContext tokenContext,List<String> YWLSHS);


    /**
     * 是否启用楼盘
     * @param LPBH 楼盘编号
     * @param SFQY 是否启用 1 启用 0 禁用
     * @return CommonResponses
     */
    public CommonResponses reEstateProjectSFQY(TokenContext tokenContext,String LPBH,String SFQY);

    /**
     * 楼盘历史记录
     * @param LPMC 楼盘名称
     * @return ArrayList<GetCommonHistory>
     */
    public PageRes<GetCommonHistory> getEstateProjectHistory(String LPMC,String pageNo,String pageSize);

    /**
     *  根据楼盘编号查找楼盘信息
     * @param lpbh 楼盘编号
     * @return EstateIdGet
     */
    public EstateIdGet getEstateProjectInfoByLPBH(String lpbh);

    /**
     *  根据楼盘名称查找售房人开户信息
     * @param lpmc 楼盘名称
     * @return EstateIdGet
     */
    public HousingIdGet getSFRZHHM(String lpmc);
}
