package com.handge.housingfund.server.controllers.finance.api;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.finance.model.VoucherBatchPdfPost;
import com.handge.housingfund.common.service.finance.model.VoucherModel;

import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/25.
 */
public interface IVoucherAPI {
    /**
     * 获取业务凭证列表
     *
     * @param mbbh
     * @param ywmc
     * @param km
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Response getVoucherList(String mbbh, String ywmc, String km, int pageNo, int pageSize);

    /**
     * 获取业务凭证详情
     *
     * @param id
     * @return
     */
    public Response getVoucher(String id);

    /**
     * 根据业务id获取业务凭证详情
     *
     * @param ywid
     * @return
     */
    public Response getVoucherByYWID(String ywid);

    /**
     * 新增业务凭证
     *
     * @param voucherModel
     * @return
     */
    public Response addVoucher(VoucherModel voucherModel);

    /**
     * 修改业务凭证
     *
     * @param id
     * @param voucherModel
     * @return
     */
    public Response updateVoucher(String id, VoucherModel voucherModel);

    /**
     * 删除业务凭证
     *
     * @param id
     * @return
     */
    public Response deleteVoucher(String id);

    /**
     * 批量删除业务凭证
     *
     * @param delList
     * @return
     */
    public Response delVouchers(ArrayList<String> delList);

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
    Response getTemporaryRecordList(String HRHM, int state, String HRSJKS, String HRSJJS, String FSE, String JZPZH, String YHMC, String ZHHM, String pageSize, String pageNo);

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
    Response getTemporaryRecordList(String HRHM, int state, String HRSJKS, String HRSJJS, String FSE, String JZPZH,String YHMC, String ZHHM, String marker, String action, String pageSize);


    /**
     * 获取业务凭证管理列表
     *
     * @param PZRQKS
     * @param PZRQJS
     * @param PZH
     * @param YWLX
     * @param pageSize
     * @param pageNo
     * @return
     */
    Response getVoucherManagerList(String PZRQKS, String PZRQJS, String PZH, String YWLX, String YWMC,String ZhaiYao, String FSE, String pageSize, String pageNo);

    /**
     * 获取业务凭证管理列表
     *
     * @param PZRQKS
     * @param PZRQJS
     * @param PZH
     * @param YWLX
     * @param pageSize
     * @return
     */
    Response getVoucherManagerList(String PZRQKS, String PZRQJS, String PZH, String YWLX, String YWMC,String ZhaiYao, String FSE, String marker, String action, String pageSize);

    /**
     * 获取业务凭证管理详情
     *
     * @param JZPZH
     * @return
     */
    Response getVoucherManagerDetail(String JZPZH);

    /**
     * 获取业务凭证管理pdf
     *
     * @param JZPZH
     * @return
     */
    Response getVoucherManagerDetailpdf(String JZPZH);

    /**
     * * 获取业务详情
     *
     * @param YWLSH
     * @param CZNR
     * @return
     */
    Response getBusinessDetail(String YWLSH, String CZNR);

    /**
     * 结账
     *
     * @param KJQJ
     * @return
     */
    Response checkVoucher(TokenContext tokenContext, String KJQJ);

    /**
     * 获取科目汇总
     *
     * @param JZRQ 截止日期
     * @return
     */
    Response getSubjectsCollect(String JZRQ);

    /**
     * 获取明细账
     *
     * @param KMBH    科目名称
     * @param KJQJKS  会计期间开始
     * @param KJQJJS  会计期间结束
     * @param BHWJZPZ 是否包含未结账凭证
     * @return
     */
    Response getBooksDetails(String KMBH, String KJQJKS, String KJQJJS, boolean BHWJZPZ);

    /**
     * 获取总账
     *
     * @param KMBH    科目名称
     * @param KJQJKS  会计期间开始
     * @param KJQJJS  会计期间结束
     * @param BHWJZPZ 是否包含未结账凭证
     * @return
     */
    Response getBooksGeneral(String KMBH, String KJQJKS, String KJQJJS, boolean BHWJZPZ);

    /**
     * 获取批量凭证pdf
     *
     * @param voucherBatchPdfPost 凭证号集合
     * @return
     */
    Response batchVoucherPdf(VoucherBatchPdfPost voucherBatchPdfPost);
}
