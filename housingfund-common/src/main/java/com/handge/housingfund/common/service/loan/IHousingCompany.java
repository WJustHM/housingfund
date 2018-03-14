package com.handge.housingfund.common.service.loan;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.loan.model.CommonResponses;
import com.handge.housingfund.common.service.loan.model.HousingCompanyPost;
import com.handge.housingfund.common.service.loan.model.HousingIdGet;

import java.util.List;

/**
 * Created by 向超 on 2017/8/9.
 */
public interface IHousingCompany {

    /**
     * 房开列表
     * @param FKGS 房开公司名称
     * @param FKZH 房开公司账号
     * @param SFQY 是否启用 true启用 false 禁用
     * @param pageNo 页码
     * @param pageSize 一页最大条数
     * @return  PageRes
     */
    public PageRes getHousingCompanyInfo(TokenContext tokenContext,String FKGS, String FKZH, Boolean SFQY,String kssj,String jssj, String pageNo, String pageSize);

    /**
     * 房开列表
     * @param FKGS 房开公司名称
     * @param FKZH 房开公司账号
     * @param SFQY 是否启用 true启用 false 禁用
     * @param marker 页码
     * @param pageSize 一页最大条数
     * @return  PageRes
     */
    public PageResNew getHousingCompanyInfoNew(TokenContext tokenContext,String FKGS, String FKZH, Boolean SFQY,String kssj,String jssj, String marker, String pageSize,String action);

    /**
     * 添加房开信息
     * @param CZLX 操作类型 0 保存 1 提交
     * @param housingCompanyInfo 添加房开公司信息
     * @return CommonResponses
     */
    public CommonResponses addHousingCompany(TokenContext tokenContext,String CZLX, HousingCompanyPost housingCompanyInfo);

    /**
     * 修改房开信息
     * @param YWLSH 业务流水号
     * @param CZLX 操作类型 0 保存 1 提交
     * @param housingCompanyInfo 修改房开公司信息
     * @return CommonResponses
     */
    public CommonResponses reHousingCompanyInfo(TokenContext tokenContext,String YWLSH, String CZLX, HousingCompanyPost housingCompanyInfo);

    /**
     * 房开详情
     * @param YWLSH 业务流水号
     * @param type 类型 1 审核通过前  2 审核通过后
     * @return HousingIdGet
     */
    public HousingIdGet showHousingCompanyInfo(String YWLSH, String type);

    /**
     * 受理房开列表
     * @param FKGS 房开公司名称
     * @param ZHUANGTAI 业务状态
     * @param pageNo 页码
     * @param pageSize 一页最大条数
     * @return PageRes
     */
    public PageRes getHousingCompanyInfoAccept(TokenContext tokenContext,String FKGS, String ZHUANGTAI,String KSSJ,String JSSJ , String pageNo, String pageSize);

    /**
     * 受理房开列表
     * @param FKGS 房开公司名称
     * @param ZHUANGTAI 业务状态
     * @param marker 游标
     * @param pageSize 一页最大条数
     * @return PageRes
     */
    public PageResNew getHousingCompanyInfoAcceptNew(TokenContext tokenContext, String FKGS, String ZHUANGTAI, String KSSJ, String JSSJ , String marker, String pageSize, String action);

    /**
     * 房开回执单
     * @param YWLSH 业务流水号
     * @return ApplyHousingCompanyReceipt
     */
    public CommonResponses receiptHousingCompany(String YWLSH);

    /**
     * 审核通过后操作
     * @param YWLSH 业务流水号
     * @return ResponseEntity
     */
    public void doHousingCompany(String YWLSH);

    /**
     * 批量提交操作
     * @param YWLSHS 业务流水号集合
     * @return CommonResponses
     */
    public CommonResponses submitHousingCompany(TokenContext tokenContext,List<String> YWLSHS);

    /**
     * 根据房开账号和房开公司查询房开公司
     * @param FKGS 房开公司名称
     * @param FKZH 房开账号
     * @param pageNo 页码
     * @param pageSize 一页最大条数
     * @return PageRes
     */
    public PageRes getHousingCompany(String FKGS, String FKZH, String YWWD,String pageNo, String pageSize);

    /**
     * 房开公司是否启用
     * @param FKGSZH 房开公司账号
     * @param SFQY 是否启用 0 禁用 1 启用
     * @return CommonResponses
     */
    public CommonResponses reHousingCompanySFQY(TokenContext tokenContext,String FKGSZH, String SFQY);

    /**
     * 查询房开公司历史
     * @param ZZJGDM 组织机构代码
     * @return  ArrayList<GetCommonHistory>
     */
    public PageRes getCompanyHistory(String ZZJGDM,String pageNo,String pageSize);

    /**
     *  根据房开公司账号查找房开信息
     * @param fkzh 房开公司账号
     * @return HousingIdGet
     */
    public HousingIdGet getCompanyInfoByFKZH(String fkzh);

}
