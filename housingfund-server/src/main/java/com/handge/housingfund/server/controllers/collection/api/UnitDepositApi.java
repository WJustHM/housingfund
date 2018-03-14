package com.handge.housingfund.server.controllers.collection.api;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.deposit.*;

public interface UnitDepositApi<T> {


    /**
     * 根据账号查询清册信息（生成清册数据，从公共基础表读取）
     *
     * @param DWZH 单位账号
     **/
    public T getRemittanceInventoryPersonalList(TokenContext tokenContext, final String DWZH);


    /**
     * 获取催缴历史记录
     *
     * @param YWLSH 业务流水号
     **/
    public T getUnitPayCallHistory(TokenContext tokenContext, final String YWLSH);


    /**
     * 提交催缴
     *
     * @param unitPayCallPosts
     **/
    public T postUnitPayCall(TokenContext tokenContext, UnitPayCallPost unitPayCallPosts);


    /**
     * 单位缴存比例调整修改
     *
     * @param YWLSH 业务流水号
     **/
    public T putUnitDepositRatio(TokenContext tokenContext, final String YWLSH, final UnitDepositRatioPut unitdepositratioput);


    /**
     * 单位缴存比例调整详情
     *
     * @param YWLSH 业务流水号
     **/
    public T getUnitDepositRatio(TokenContext tokenContext, final String YWLSH);


    /**
     * 汇缴申请修改
     *
     * @param YWLSH 业务流水号
     **/
    public T putUnitRemittance(TokenContext tokenContext, final String YWLSH, final UnitRemittancePut unitremittanceput);


    /**
     * 汇缴申请详情
     *
     * @param YWLSH 业务流水号
     **/
    public T getUnitRemittance(TokenContext tokenContext, final String YWLSH);


    /**
     * 单位汇缴-缴存回执单
     *
     * @param YWLSH 业务流水号
     **/
    public T headUnitRemittanceReceipt(TokenContext tokenContext, final String YWLSH);


    /**
     * 修改错缴申请
     *
     * @param YWLSH 业务流水号
     **/
    public T putUnitPayWrong(TokenContext tokenContext, final String YWLSH, final UnitPayWrongPut unitpaywrongput);


    /**
     * 获取错缴详情
     *
     * @param YWLSH 业务流水号
     **/
    public T getUnitPayWrong(TokenContext tokenContext, final String YWLSH);


    /**
     * 申请单位缴存比例调整时，先获取单位相关信息
     *
     * @param DWZH 单位账号
     **/
    public T getUnitDepositRatioAuto(TokenContext tokenContext, final String DWZH);


    /**
     * 新建汇缴申请
     **/
    public T postUnitRemittance(TokenContext tokenContext, final UnitRemittancePost unitremittancepost);


    /**
     * 获取补缴记录
     *
     * @param DWMC 单位名称
     * @param DWZH 单位账号
     * @param YWZT 业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)
     * @param CZY  操作员
     * @param KSSJ 受理时间开始
     * @param JSSJ 受理时间结束
     **/
    public T getUnitPayBackList(TokenContext tokenContext, final String DWMC, final String DWZH, final String YWZT, final String CZY, String YWWD ,final String KSSJ, final String JSSJ, String pageNo, String pageSize);


    /**
     * 获取单位补缴申请时的单位信息
     *
     * @param DWZH 单位账号
     **/
    public T getUnitPayBackAuto(TokenContext tokenContext, final String DWZH, final String HBJNY);


    /**
     * 新建补缴申请
     **/
    public T postUnitPayBack(TokenContext tokenContext, final UnitPayBackPost unitpaybackpost);


    /**
     * 查看单位缴存信息（从列表进入）
     *
     * @param DWZH 单位账号
     **/
    public T getUnitDepositDetail(TokenContext tokenContext, final String DWZH);


    /**
     * 个人缴存基数调整列表、搜索
     *
     * @param DWMC      单位名称
     * @param DWZH      单位账号
     * @param ZhuangTai 业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)
     * @param KSSJ      受理时间开始
     * @param JSSJ      受理时间结束
     **/
    public T getPersonRadixList(TokenContext tokenContext, final String DWMC, final String DWZH, final String ZhuangTai, final String KSSJ, final String JSSJ, final String pageNumber, final String pageSize);


    T getPersonRadixListnew(TokenContext tokenContext, String DWMC, String DWZH, String ZhuangTai, String KSSJ, String JSSJ, String marker, String pageSize, String action);


    /**
     * 个人缴存基数调整申请
     **/
    public T postUnitCardinalNumber(TokenContext tokenContext, final PersonRadixPost personradixpost);


    /**
     * 根据单位账号获取个人缴存基数调整前详情
     *
     * @param DWZH 单位账号
     **/
    public T getPersonRadixBeforeDetail(TokenContext tokenContext, final String DWZH,final String SXNY);


    /**
     * 根据单位账号获取汇缴申请的单位信息
     *
     * @param DWZH 单位账号
     **/
    public T getUnitRemittanceAuto(TokenContext tokenContext, final String DWZH);


    /**
     * 新建错缴更正申请
     **/
    public T postUnitPayWrong(TokenContext tokenContext, final UnitPayWrongPost unitpaywrongpost);


    /**
     * 公积金补缴通知单
     *
     * @param YWLSH 业务流水号
     **/
    public T headUnitPayBackNotice(TokenContext tokenContext, final String YWLSH);


    /**
     * 获取汇缴清册信息（获取清册数据，从清册业务表读取）
     *
     * @param DWZH 单位账号
     **/
    public T getUnitInventoryList(TokenContext tokenContext, final String DWZH);

    /**
     * 打印清册确认单
     **/
    public T headRemittanceInventory(TokenContext tokenContext, final String dwzh, final String qcny);

    /**
     * 打印清册回执单
     *
     * @param QCQRDH 清册确认单号
     **/
    public T headRemittanceInventory(TokenContext tokenContext, final String QCQRDH);


    /**
     * 提交缓缴申请
     **/
    public T postUnitPayHold(TokenContext tokenContext, final UnitPayHoldPost unitpayholdpost);


    /**
     * 缓缴回执单
     *
     * @param YWLSH 业务流水号
     **/
    public T headUnitPayHoldReceipt(TokenContext tokenContext, final String YWLSH);


    /**
     * 获取错缴更正业务记录列表
     *
     * @param DWMC      单位名称
     * @param DWZH      单位账号
     * @param ZhuangTai 业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)
     * @param KSSJ      受理时间开始
     * @param JSSJ      受理时间结束
     **/
    public T getUnitPayWrongList(TokenContext tokenContext, final String DWMC, final String DWZH, final String ZhuangTai, final String CZY, final String KSSJ, final String JSSJ, final String page, final String pageSize);


    /**
     * 批量提交缓缴
     *
     * @param ywlshs
     * @return
     */
    public T submitUnitPayHold(TokenContext tokenContext, BatchSubmission ywlshs);


    /**
     * 错缴业务回执单
     *
     * @param YWLSH 业务流水号
     **/
    public T headUnitPayWrongReceipt(TokenContext tokenContext, final String YWLSH);

    /**
     * 根据单位账号和更正缴至年月,获取单位错缴信息
     *
     * @param DWZH 单位账号
     * @param JZNY 更正缴至年月
     **/
    public T autoGetPayWrong(TokenContext tokenContext, final String DWZH, final String JZNY);


    /**
     * 汇缴清册详情
     *
     * @param QCQRDH 清册确认单号
     **/
    public T getUnitRemittanceInventoryDetail(TokenContext tokenContext, final String QCQRDH);


    /**
     * 单位汇缴业务记录
     *
     * @param DWMC 单位名称
     * @param DWZH 单位账号
     * @param YWZT 业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)
     * @param CZY  操作员
     * @param KSSJ 受理时间开始
     * @param JSSJ 受理时间结束
     **/
    public T getUnitRemittanceList(TokenContext tokenContext, final String DWMC, final String DWZH, final String YWZT, final String CZY,final String YHMC, final String YWWD, final String KSSJ, final String JSSJ,final String YWPCH, String pageNo, String pageSzie);


    /**
     * 催缴记录
     *
     * @param DWMC 单位名称
     * @param DWZH 单位账号
     **/
    public T getUnitPayCallList(TokenContext tokenContext, final String DWMC, final String DWZH, final String page, final String pageSize,final String kssj,final String jssj);


    /**
     *获取催缴详情
     * @param YWLSH 业务流水号
     **/
//    public T getUnitPayCallDetail(TokenContext tokenContext,final  String YWLSH);


    /**
     * 催缴回执单（打印催缴记录）
     *
     * @param YWLSH 业务流水号
     **/
    public T headUnitPayCallReceipt(TokenContext tokenContext, final String YWLSH);


    /**
     * 获取缓缴业务记录列表
     *
     * @param DWMC      单位名称
     * @param DWZH      单位账号
     * @param ZhuangTai 业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)
     * @param CZY       操作员
     * @param KSSJ      受理时间开始
     * @param JSSJ      受理时间结束
     **/
    public T getUnitPayHoldList(TokenContext tokenContext, final String DWMC, final String DWZH, final String ZhuangTai, final String CZY, final String YWWD, final String KSSJ, final String JSSJ, final String page, final String pageSize);


    /**
     * 获取缓缴业务记录列表
     *
     * @param DWMC      单位名称
     * @param DWZH      单位账号
     * @param ZhuangTai 业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)
     * @param CZY       操作员
     * @param KSSJ      受理时间开始
     * @param JSSJ      受理时间结束
     **/
    public T getUnitPayHoldListNew(TokenContext tokenContext, final String DWMC, final String DWZH, final String ZhuangTai, final String CZY,String YWWD,final String KSSJ, final String JSSJ, final String marker, final String pageSize,String action);


    /**
     * 缴存列表信息
     *  @param DWMC 单位名称
     * @param DWZH 单位账号
     * @param GLWD 是否根据网点筛选
     * @param GLXH 是否筛选已销户单位
     * @param KHWD
     **/
    public T getUnitDepositListRes(TokenContext tokenContext, final String DWMC, final String DWZH, final String pageSize, final String page, final boolean GLWD, final boolean GLXH, String KHWD,String SFYWFTYE,String JZNY);

    /**
     * 单位缴存信息列表
     *
     * @param date 年限
     * @param DWZH 单位账号
     **/
    public T getUnitDepositInfoList(TokenContext tokenContext, String DWZH, String date, final String pageSize, final String page);




    /**
     * 单位缓缴修改
     *
     * @param YWLSH 业务流水号
     **/
    public T putUnitPayHold(TokenContext tokenContext, final String YWLSH, final UnitPayHoldPut unitpayholdput);


    /**
     * 获取缓缴申请详情
     *
     * @param YWLSH 业务流水号
     **/
    public T getUnitPayHold(TokenContext tokenContext, final String YWLSH);


    /**
     * 单位缴存比例调整列表、搜索
     *
     * @param DWMC      单位名称
     * @param DWZH      单位账号
     * @param ZhuangTai 业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)
     * @param KSSJ      受理时间起点
     * @param JSSJ      受理时间终点
     **/
    public T getUnitDepositRatioList(TokenContext tokenContext, final String DWMC, final String DWZH, final String ZhuangTai, final String KSSJ, final String JSSJ, final String pageNumber, final String pageSize);

    T getUnitDepositRatioListnew(TokenContext tokenContext, String dwmc, String dwzh, String ywzt, String kssj, String jssj, String marker, String pageSize, String action);


    /**
     * 缴存清册列表
     *
     * @param DWMC 单位名称
     * @param DWZH 单位账号
     * @param CZY  操作员
     * @param KSSJ 受理时间开始(开始时间）
     * @param JSSJ 受理时间结束(结束时间）
     **/
    public T getInventoryList(TokenContext tokenContext, final String DWMC, final String DWZH, final String CZY, final String YWWD, final String KSSJ, final String JSSJ, String pageNo, String pageSize);


    /**
     * 清册确认
     **/
    public T postInventoryConfirm(TokenContext tokenContext, final InventoryConfirmPost inventoryconfirmpost);


    /**
     * 个人缴存基数调整修改
     *
     * @param YWLSH 业务流水号
     **/
    public T putPersonRadix(TokenContext tokenContext, final String YWLSH, final PersonRadixPut personradixput);


    /**
     * 个人缴存基数调整详情
     *
     * @param YWLSH 业务流水号
     **/
    public T getPersonRadix(TokenContext tokenContext, final String YWLSH);


    /**
     * 公积金补缴回执单
     *
     * @param YWLSH 业务流水号
     **/
    public T headUnitPayBackReceipt(TokenContext tokenContext, final String YWLSH);


    /**
     * 单位缴存比例调整申请
     **/
    public T postUnitDepositRatio(TokenContext tokenContext, final PostListUnitDepositRatioPost postlistunitdepositratiopost);


    /**
     * 单位缴存比例调整回执单
     *
     * @param YWLSH 业务流水号
     **/
    public T headUnitDepositRatioReceipt(TokenContext tokenContext, final String YWLSH);


    /**
     * 批量提交
     */
    public T unitSubmitBatch(TokenContext tokenContext, BatchSubmission body);

    /**
     * 批量提交
     */
    public T personSubmitBatch(TokenContext tokenContext, BatchSubmission body);

    /**
     * 个人缴存基数调整回执单
     *
     * @param YWLSH 业务流水号
     **/
    public T headPersonRadix(TokenContext tokenContext, final String YWLSH);


    /**
     * 修改补缴
     *
     * @param YWLSH 业务流水号
     **/
    public T putUnitPayBack(TokenContext tokenContext, final String YWLSH, final UnitPayBackPut unitpaybackput);


    /**
     * 获取单位补缴详情
     *
     * @param YWLSH 业务流水号
     **/
    public T getUnitPayBack(TokenContext tokenContext, final String YWLSH);


    /**
     * 单位补缴重新使用暂收来进行缴款
     * @param YWLSH 业务流水号
     **/
    public T postPayBackTemporary(TokenContext tokenContext, final String YWLSH);


    /**
     * 单位业务明细列表
     *
     * @param DWMC   单位名称
     * @param DWZH   单位账号
     * @param YWMXLX 业务明细类型
     **/
    public T getUnitBusnissDetailList(TokenContext tokenContext, final String DWMC, final String DWZH, final String YWMXLX, final String pageNumber, final String pageSize,String start,String end);


    public T getUnitBusnissDetailListnew(TokenContext tokenContext, final String DWMC, final String DWZH, final String YWMXLX, final String marker, final String pageSize,String start,String end,String  action);


    /**
     * 个人业务明细列表
     *
     * @param XingMing 姓名
     * @param GRZH     个人账号
     * @param YWMXLX   业务明细类型
     **/
    public T getIndiBusnissDetailList(TokenContext tokenContext, final String XingMing, final String GRZH, final String YWMXLX, final String pageNumber, final String pageSize,String start,String end,String ZJHM);

    /**
     * 个人业务明细列表
     *
     * @param XingMing 姓名
     * @param GRZH     个人账号
     * @param YWMXLX   业务明细类型
     **/
    public T getIndiBusnissDetailListnew(TokenContext tokenContext, final String XingMing, final String GRZH, final String YWMXLX, final String marker, final String pageSize,String start,String end,String action);

    /**
     * 汇缴申请时，根据账号+汇补缴年月查询清册信息
     *
     * @param dwzh
     * @param hbjny
     * @return
     */
    public T UnitRemittanceInventory(TokenContext tokenContext, String dwzh, String hbjny);

    /**
     * 公积金汇缴通知单
     *
     * @param ywlsh
     * @return
     */
    public T headUnitRemittanceNotice(TokenContext tokenContext, String ywlsh);

    /**
     * 获取多月的清册信息
     */
    public T getInventoryBatchList(TokenContext tokenContext, String dwzh, String qcsjq, String qcsjz, String YWLSH);

    /**
     * 清册受理提交
     */
    public T postInventoryBatch(TokenContext tokenContext, String dwzh, String qcsjq, String qcsjz, String jkfs, String CXQC);

    /**
     * 获取单位该月的最新清册详情
     */
    public T getLatestInventoryOfMonth(TokenContext tokenContext, String ywlsh);

    /**
     * 获取单位该月的最初的汇缴清册详情
     */
    public T getFirstInventoryOfMonth(TokenContext tokenContext, String ywlsh);

    /**
     * 生成单位该月的清册详情的详情..
     */
    T getInventoryDetail(String dwzh, String qcyf, String xingMing, String pageNumber, String pageSize);

    /**
     * 获取单位清册初始化数据
     */
    public T getUnitInventoryInit(TokenContext tokenContext, String dwzh);


    T getUnitPayBackList(TokenContext tokenContext, String dwmc, String dwzh, String ywzt, String czy, String ywwd,String kssj, String jssj, String marker, String pageSize, String action);

    T getUnitPayWrongList(TokenContext tokenContext, String dwmc, String dwzh, String ywzt, String czy, String kssj, String jssj, String marker, String pageSize, String action);

    T getUnitRemittanceList(TokenContext tokenContext, String dwmc, String dwzh, String ywzt, String czy,String yhmc,String ywwd, String kssj, String jssj, String ywpch, String marker, String pageSize, String action);

    T getUnitPayCallList(TokenContext tokenContext, String dwmc, String dwzh, String marker, String pageSize, String kssj, String jssj, String action);

    T getInventoryList(TokenContext tokenContext, String dwmc, String dwzh, String czy,String ywwd,String kssj, String jssj, String marker, String pageSize, String action);
}