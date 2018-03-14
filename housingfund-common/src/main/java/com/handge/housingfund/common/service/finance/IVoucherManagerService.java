package com.handge.housingfund.common.service.finance;

import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNotice;
import com.handge.housingfund.common.service.finance.model.*;
import com.handge.housingfund.common.service.loan.model.CommonResponses;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by tanyi on 2017/8/24.
 */
public interface IVoucherManagerService {
    /**
     * 生成凭证 有到账通知，凭证模板中借方或贷方科目唯一，有模板编号
     *
     * @param HSDW            核算单位
     * @param JiZhang         记账
     * @param FuHe            复核
     * @param ChuNa           出纳
     * @param ZhiDan          制单
     * @param MBBH            模板编号
     * @param CZNR            操作内容
     * @param accChangeNotice 结算平台回执单
     * @param YHDM            银行代码（还款和贷款发放必传）
     * @return 业务凭证号
     */
    VoucherRes addVoucher(String HSDW, String JiZhang, String FuHe, String ChuNa, String ZhiDan,
                          String MBBH, String CZNR, AccChangeNotice accChangeNotice, String YHDM);

    /**
     * 生成凭证 无到账通知，凭证模板中借方或贷方科目唯一，有模板编号
     *
     * @param HSDW    核算单位
     * @param JiZhang 记账
     * @param FuHe    复核
     * @param ChuNa   出纳
     * @param ZhiDan  制单
     * @param MBBH    模板编号
     * @param CZNR    操作内容
     * @param YWLSH   业务流水号
     * @param ZhaiYao 摘要
     * @param FSE     发生额
     * @param YHZHH   没有则传null
     * @param YHDM    银行代码（还款和贷款发放必传）
     * @return
     */
    VoucherRes addVoucher(String HSDW, String JiZhang, String FuHe, String ChuNa, String ZhiDan,
                          String MBBH, String CZNR, String YWLSH, String ZhaiYao, BigDecimal FSE, String YHZHH, String YHDM);


    /**
     * 生成凭证 有到账通知，凭证模板中借方或贷方科目不唯一，有模板编号
     *
     * @param HSDW            核算单位
     * @param JiZhang         记账
     * @param FuHe            复核
     * @param ChuNa           出纳
     * @param ZhiDan          制单
     * @param MBBH            模板编号
     * @param CZNR            操作内容
     * @param YWLSH           业务流水号
     * @param JFSJ            借方数据
     * @param DFSJ            贷方数据
     * @param DJSL            单据数量
     * @param accChangeNotice 结算平台回执单
     * @param YHDM            银行代码（还款和贷款发放必传）
     * @return
     */
    VoucherRes addVoucher(String HSDW, String JiZhang, String FuHe, String ChuNa, String ZhiDan,
                          String MBBH, String CZNR, String YWLSH, List<VoucherAmount> JFSJ,
                          List<VoucherAmount> DFSJ, String DJSL, AccChangeNotice accChangeNotice, String YHDM);

    /**
     * 生成凭证 有到账通知，凭证模板中借方或贷方科目不唯一，无模板编号
     *
     * @param HSDW            核算单位
     * @param JiZhang         记账
     * @param FuHe            复核
     * @param ChuNa           出纳
     * @param ZhiDan          制单
     * @param YWID            业务ID
     * @param CZNR            操作内容
     * @param YWLSH           业务流水号
     * @param JFSJ            借方数据
     * @param DFSJ            贷方数据
     * @param DJSL            单据数量
     * @param YWMC            业务名称
     * @param accChangeNotice 结算平台回执单
     * @param YHDM            银行代码（还款和贷款发放必传）
     * @return
     */
    VoucherRes addVoucher(String HSDW, String JiZhang, String FuHe, String ChuNa, String ZhiDan, String YWID,
                          String CZNR, String YWLSH, List<VoucherAmount> JFSJ,
                          List<VoucherAmount> DFSJ, String DJSL, String YWMC, AccChangeNotice accChangeNotice, String YHDM);

    /**
     * 生成凭证 无到账通知，凭证模板中借方或贷方科目不唯一，有模板编号
     *
     * @param HSDW    核算单位
     * @param JiZhang 记账
     * @param FuHe    复核
     * @param ChuNa   出纳
     * @param ZhiDan  制单
     * @param MBBH    模板编号
     * @param CZNR    操作内容
     * @param YWLSH   业务流水号
     * @param JFSJ    借方数据
     * @param DFSJ    贷方数据
     * @param DJSL    单据数量
     * @param YHZHH   银行专户号,没有则传null
     * @param YHDM    银行代码（还款和贷款发放必传）
     * @return
     */
    VoucherRes addVoucher(String HSDW, String JiZhang, String FuHe, String ChuNa, String ZhiDan,
                          String MBBH, String CZNR, String YWLSH, List<VoucherAmount> JFSJ,
                          List<VoucherAmount> DFSJ, String DJSL, String YHZHH, String YHDM);

    /**
     * 生成凭证 无到账通知，凭证模板中借方或贷方科目不唯一，没有模板编号
     *
     * @param HSDW    核算单位
     * @param JiZhang 记账
     * @param FuHe    复核
     * @param ChuNa   出纳
     * @param ZhiDan  制单
     * @param CZNR    操作内容
     * @param YWLSH   业务流水号
     * @param JFKM    借方科目
     * @param DFKM    贷方科目
     * @param JFSJ    借方数据
     * @param DFSJ    贷方数据
     * @param DJSL    单据数量
     * @param YWMC    业务名称
     * @param YHZHH   银行专户号,没有则传null
     * @param YHDM    银行代码（还款和贷款发放必传）
     * @return
     */
    VoucherRes addVoucher(String HSDW, String JiZhang, String FuHe, String ChuNa, String ZhiDan,
                          String CZNR, String YWID, String YWLSH, String JFKM, String DFKM, List<VoucherAmount> JFSJ,
                          List<VoucherAmount> DFSJ, String DJSL, String YWMC, String YHZHH, String YHDM);


    /**
     * 修改凭证
     *
     * @param YWLSH
     * @param JFSJ
     * @param DFSJ
     * @param DJSL
     * @param YHZHH
     * @param YHDM
     * @return
     */
    VoucherRes reVoucher(String YWLSH, List<VoucherAmount> JFSJ, List<VoucherAmount> DFSJ, String DJSL, String YHZHH, String YHDM);

    /**
     * 删除 记账凭证
     *
     * @param YWLSH 业务流水号
     * @return
     */
    void delVoucher(String YWLSH);

    /**
     * 对冲 记账凭证
     *
     * @param YWLSH
     * @return
     */
    VoucherRes rehedgeVoucher(String YWLSH);

    /**
     * 新增暂收记录
     *
     * @param SKZH    收款账号
     * @param SKHM    收款户名
     * @param FKZH    付款账号
     * @param FKHM    付款户名
     * @param HRJE    汇入金额
     * @param HRSJ    汇入日期
     * @param JZPZH
     * @param zhaiyao
     * @return
     */
    String addTemporaryRecord(String SKZH, String SKHM, String FKZH, String FKHM, BigDecimal HRJE, Date HRSJ, String JZPZH, String zhaiyao, String remark);

    /**
     * 更新暂收状态
     *
     * @param ID
     * @param state
     * @return
     */
    String updateTemporaryRecord(String ID, Boolean state, String JZPZH);

    /**
     * 获取暂收列表
     *
     * @param HRHM     汇入户名
     * @param state    状态
     * @param HRSJKS   汇入时间开始
     * @param HRSJJS   汇入时间结束
     * @param pageSize
     * @param pageNo
     * @return
     */
    PageRes<TemporaryRecord> getTemporaryRecordList(String HRHM, int state, String HRSJKS, String HRSJJS, String FSE, String JZPZH, String YHMC, String ZHHM, int pageSize, int pageNo);

    /**
     * 获取暂收列表
     *
     * @param HRHM     汇入户名
     * @param state    状态
     * @param HRSJKS   汇入时间开始
     * @param HRSJJS   汇入时间结束
     * @param marker
     * @param action
     * @param pageSize
     * @return
     */
    PageResNew<TemporaryRecord> getTemporaryRecordList(String HRHM, int state, String HRSJKS, String HRSJJS, String FSE, String JZPZH, String YHMC, String ZHHM, String marker, String action, int pageSize);


    /**
     * 获取凭证管理列表
     *
     * @param PZRQKS 凭证开始日期
     * @param PZRQJS 凭证结束日期
     * @param PZH    凭证号码
     * @param YWLX   业务类型
     * @return
     */
    PageRes<VoucherManagerBase> getVoucherList(String PZRQKS, String PZRQJS, String PZH, String YWLX, String YWMC, String ZhaiYao, String FSE, int pageSize, int pageNo);

    /**
     * 获取凭证管理列表
     *
     * @param PZRQKS 凭证开始日期
     * @param PZRQJS 凭证结束日期
     * @param PZH    凭证号码
     * @param YWLX   业务类型
     * @return
     */
    PageResNew<VoucherManagerBase> getVoucherList(String PZRQKS, String PZRQJS, String PZH, String YWLX, String YWMC, String ZhaiYao, String FSE, String marker, String action, int pageSize);

    /**
     * 获取记账凭证详情
     *
     * @param JZPZH 业务凭证号
     * @return
     */
    VoucherManager getDetail(String JZPZH);

    /**
     * 获取记账凭证pdf
     *
     * @param JZPZH 业务凭证号
     * @return
     */
    CommonResponses getDetailpdf(String JZPZH);

    /**
     * 结账
     *
     * @param KJQJ 会计期间
     * @param JZR  结账人
     * @return
     */
    CommonResponses checkoutVoucher(String JZR, String KJQJ);

    /**
     * 获取科目汇总列表
     *
     * @param JZRQ 截止日期
     * @return
     */
    List<SubjectsBalance> getSubjectsCollect(String JZRQ);

    /**
     * 获取明细账
     *
     * @param KMBH
     * @param KJQJKS
     * @param KJQJJS
     * @param BHWJZPZ
     * @return
     */
    List<BookDetails> getBooksDetails(String KMBH, String KJQJKS, String KJQJJS, boolean BHWJZPZ);

    /**
     * 获取总账
     *
     * @param KMBH
     * @param KJQJKS
     * @param KJQJJS
     * @param BHWJZPZ
     * @return
     */
    List<BookDetails> getBooksGeneral(String KMBH, String KJQJKS, String KJQJJS, boolean BHWJZPZ);

    /**
     * 获取批量凭证pdf
     *
     * @param voucherBatchPdfPost
     * @return
     */
    CommonResponses batchVoucherPdf(VoucherBatchPdfPost voucherBatchPdfPost);

}
