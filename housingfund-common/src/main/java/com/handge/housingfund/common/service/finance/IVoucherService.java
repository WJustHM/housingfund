package com.handge.housingfund.common.service.finance;

import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.finance.model.VoucherModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/25.
 */
public interface IVoucherService {
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
    PageRes<VoucherModel> getVoucherList(String mbbh, String ywmc, String km, int pageNo, int pageSize);

    /**
     * 获取业务凭证详情
     *
     * @param id
     * @return
     */
    VoucherModel getVoucher(String id);

    /**
     * 根据业务id获取业务凭证详情
     *
     * @param ywid
     * @return
     */
    VoucherModel getVoucherByYWID(String ywid);

    /**
     * 新增业务凭证
     *
     * @param voucherModel
     */
    VoucherModel addVoucher(VoucherModel voucherModel);

    /**
     * 修改业务凭证
     *
     * @param id
     * @param voucherModel
     * @return
     */
    boolean updateVoucher(String id, VoucherModel voucherModel);

    /**
     * 删除业务凭证
     *
     * @param id
     * @return
     */
    boolean deleteVoucher(String id);

    /**
     * 批量删除业务凭证
     *
     * @param delList
     * @return
     */
    boolean delVouchers(ArrayList<String> delList);

}
