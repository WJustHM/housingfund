package com.handge.housingfund.server.controllers.finance.api.impl;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.Msg;
import com.handge.housingfund.common.service.account.model.Success;
import com.handge.housingfund.common.service.collection.service.unitdeposit.UnitPayWrong;
import com.handge.housingfund.common.service.collection.service.unitdeposit.UnitPayback;
import com.handge.housingfund.common.service.collection.service.unitdeposit.UnitRemittance;
import com.handge.housingfund.common.service.collection.service.unitinfomanage.UnitAcctDrop;
import com.handge.housingfund.common.service.collection.service.withdrawl.WithdrawlTasks;
import com.handge.housingfund.common.service.finance.IVoucherManagerService;
import com.handge.housingfund.common.service.finance.IVoucherService;
import com.handge.housingfund.common.service.finance.model.VoucherBatchPdfPost;
import com.handge.housingfund.common.service.finance.model.VoucherModel;
import com.handge.housingfund.common.service.finance.model.enums.VoucherBusinessType;
import com.handge.housingfund.common.service.loan.ILoanService;
import com.handge.housingfund.common.service.loan.IRepaymentService;
import com.handge.housingfund.common.service.loan.model.CommonResponses;
import com.handge.housingfund.common.service.loan.model.Error;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnCode;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.server.controllers.finance.api.IVoucherAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/25.
 */
@Component
public class VoucherAPI implements IVoucherAPI {

    @Autowired
    IVoucherService iVoucherService;

    @Autowired
    private IVoucherManagerService iVoucherManagerService;

    @Autowired
    private ILoanService loanService;

    @Autowired
    private IRepaymentService repaymentService;

    @Autowired
    private WithdrawlTasks withdrawlTasks;

    @Autowired
    private UnitAcctDrop unitAcctDrop;

    @Autowired
    private UnitRemittance unitRemittance;

    @Autowired
    private UnitPayWrong unitPayWrong;

    @Autowired
    private UnitPayback unitPayback;

    @Override
    public Response getVoucherList(String mbbh, String ywmc, String km, int pageNo, int pageSize) {
        if (pageNo == 0)
            pageNo = 1;
        if (pageSize == 0)
            pageSize = 10;

        try {
            return Response.status(200).entity(iVoucherService.getVoucherList(mbbh, ywmc, km, pageNo, pageSize)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getVoucher(String id) {
        if (!StringUtil.notEmpty(id)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("业务凭证id不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();

        try {
            return Response.status(200).entity(iVoucherService.getVoucher(id)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getVoucherByYWID(String ywid) {
        if (!StringUtil.notEmpty(ywid)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("业务id不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();

        try {
            return Response.status(200).entity(iVoucherService.getVoucherByYWID(ywid)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response addVoucher(VoucherModel voucherModel) {
        if (voucherModel == null)
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("业务凭证内容不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(voucherModel.getYWMC()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("业务名称不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(voucherModel.getDFKM()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("贷方科目不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(voucherModel.getJFKM()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("借方科目不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();

        try {
            return Response.status(200).entity(iVoucherService.addVoucher(voucherModel)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response updateVoucher(String id, VoucherModel voucherModel) {
        if (!StringUtil.notEmpty(id))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("业务凭证id不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (voucherModel == null)
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("业务凭证内容不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(voucherModel.getYWMC()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("业务名称不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(voucherModel.getDFKM()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("贷方科目不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(voucherModel.getJFKM()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("借方科目不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();

        try {
            return Response.status(200).entity(iVoucherService.updateVoucher(id, voucherModel) ? new Msg() {{
                this.setCode(ReturnCode.Success);
                this.setMsg("该业务凭证已在使用，只能修改备注");
            }} : new Success() {{
                this.setState("修改业务凭证成功");
            }}).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response deleteVoucher(String id) {
        if (!StringUtil.notEmpty(id)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("凭证id不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        try {
            return Response.status(200).entity(iVoucherService.deleteVoucher(id) ? new Msg() {{
                this.setCode(ReturnCode.Success);
                this.setMsg("已使用业务凭证，不能删除");
            }} : new Success() {{
                this.setState("删除业务凭证成功");
            }}).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response delVouchers(ArrayList<String> delList) {
        if (delList == null || delList.size() <= 0)
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("业务凭证id不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();

        for (String id : delList) {
            if (!StringUtil.notEmpty(id)) {
                return Response.status(200).entity(new Msg() {
                    {
                        this.setMsg("业务凭证id不能为空");
                        this.setCode(ReturnCode.Error);
                    }
                }).build();
            }
        }

        try {
            return Response.status(200).entity(iVoucherService.delVouchers(delList) ? new Msg() {{
                this.setCode(ReturnCode.Success);
                this.setMsg("已使用业务凭证，不能删除");
            }} : new Success() {{
                this.setState("删除业务凭证成功");
            }}).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getTemporaryRecordList(String HRHM, int state, String HRSJKS, String HRSJJS, String FSE, String JZPZH, String YHMC, String ZHHM, String pageSize, String pageNo) {

        try {
            int pageno = StringUtil.notEmpty(pageNo) ? Integer.parseInt(pageNo) : 1;
            int pagesize = StringUtil.notEmpty(pageSize) ? Integer.parseInt(pageSize) : 30;
            if (StringUtil.notEmpty(FSE)) {
                try {
                    new BigDecimal(FSE);
                } catch (Exception e) {
                    return Response.status(200).entity(new Msg() {
                        {
                            this.setMsg("金额非法");
                            this.setCode(ReturnCode.Error);
                        }
                    }).build();
                }
            }
            return Response.status(200).entity(iVoucherManagerService.getTemporaryRecordList(HRHM, state, HRSJKS, HRSJJS, FSE, JZPZH, YHMC, ZHHM, pagesize, pageno)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getTemporaryRecordList(String HRHM, int state, String HRSJKS, String HRSJJS, String FSE, String JZPZH, String YHMC, String ZHHM, String marker, String action, String pageSize) {

        try {
            int pagesize = StringUtil.notEmpty(pageSize) ? Integer.parseInt(pageSize) : 30;
            if (StringUtil.notEmpty(FSE)) {
                try {
                    new BigDecimal(FSE);
                } catch (Exception e) {
                    return Response.status(200).entity(new Msg() {
                        {
                            this.setMsg("金额非法");
                            this.setCode(ReturnCode.Error);
                        }
                    }).build();
                }
            }
            return Response.status(200).entity(iVoucherManagerService.getTemporaryRecordList(HRHM, state, HRSJKS, HRSJJS, FSE, JZPZH, YHMC, ZHHM, marker, action, pagesize)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }


    @Override
    public Response getVoucherManagerList(String PZRQKS, String PZRQJS, String PZH, String YWLX, String YWMC, String ZhaiYao, String FSE, String pageSize, String pageNo) {

        try {
            int pageno = pageNo == null ? 1 : Integer.parseInt(pageNo);
            int pagesize = pageSize == null ? 10 : Integer.parseInt(pageSize);
            if (StringUtil.notEmpty(FSE)) {
                try {
                    new BigDecimal(FSE);
                } catch (Exception e) {
                    return Response.status(200).entity(new Msg() {
                        {
                            this.setMsg("金额非法");
                            this.setCode(ReturnCode.Error);
                        }
                    }).build();
                }
            }
            return Response.status(200).entity(iVoucherManagerService.getVoucherList(PZRQKS, PZRQJS, PZH, YWLX, YWMC, ZhaiYao, FSE, pagesize, pageno)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getVoucherManagerList(String PZRQKS, String PZRQJS, String PZH, String YWLX, String YWMC, String ZhaiYao, String FSE, String marker, String action, String pageSize) {

        try {
            int pagesize = pageSize == null ? 30 : Integer.parseInt(pageSize);
            if (StringUtil.notEmpty(FSE)) {
                try {
                    new BigDecimal(FSE);
                } catch (Exception e) {
                    return Response.status(200).entity(new Msg() {
                        {
                            this.setMsg("金额非法");
                            this.setCode(ReturnCode.Error);
                        }
                    }).build();
                }
            }
            return Response.status(200).entity(iVoucherManagerService.getVoucherList(PZRQKS, PZRQJS, PZH, YWLX, YWMC, ZhaiYao, FSE, marker, action, pagesize)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getVoucherManagerDetail(String JZPZH) {
        if (StringUtil.notEmpty(JZPZH)) {
            try {
                return Response.status(200).entity(iVoucherManagerService.getDetail(JZPZH)).build();
            } catch (ErrorException e) {
                return Response.status(200).entity(new Error() {{
                    this.setMsg(e.getMsg());
                    this.setCode(e.getCode());
                }}).build();
            }
        } else {
            return Response.status(200).entity(new Msg() {{
                this.setMsg("业务凭证号不能为空");
                this.setCode(ReturnCode.Error);
            }}).build();
        }
    }

    @Override
    public Response getVoucherManagerDetailpdf(String JZPZH) {
        if (StringUtil.notEmpty(JZPZH)) {
            try {
                return Response.status(200).entity(iVoucherManagerService.getDetailpdf(JZPZH)).build();
            } catch (ErrorException e) {
                return Response.status(200).entity(new Error() {{
                    this.setMsg(e.getMsg());
                    this.setCode(e.getCode());
                }}).build();
            }
        } else {
            return Response.status(200).entity(new Msg() {{
                this.setMsg("业务凭证号不能为空");
                this.setCode(ReturnCode.Error);
            }}).build();
        }
    }

    @Override
    public Response getBusinessDetail(String YWLSH, String CZNR) {
        try {
            if (YWLSH.indexOf("01") == 0) {
                //个人业务流水号
                if (CZNR.equals(VoucherBusinessType.错缴.getCode())) {
                    return Response.status(200).entity(unitPayWrong.headUnitPayWrong(YWLSH)).build();
                } else if (CZNR.equals(VoucherBusinessType.汇补缴.getCode())) {
                    return Response.status(200).entity(unitRemittance.headUnitRemittance(YWLSH)).build();
                }
//                else if (CZNR.equals(VoucherBusinessType.补缴.getCode())) {
//                    return Response.status(200).entity(unitPayback.headUnitPayBackNotice(YWLSH)).build();
//                }
                else if (CZNR.equals(VoucherBusinessType.外部转入.getCode())) {

                } else if (CZNR.equals(VoucherBusinessType.部分提取.getCode())) {
                    return Response.status(200).entity(withdrawlTasks.printWithdrawlReceipt(YWLSH)).build();
                } else if (CZNR.equals(VoucherBusinessType.销户提取.getCode())) {
                    return Response.status(200).entity(withdrawlTasks.printWithdrawlReceipt(YWLSH)).build();
                } else if (CZNR.equals(VoucherBusinessType.死亡无继承人销户.getCode())) {
                    return Response.status(200).entity(unitAcctDrop.headUnitAcctsDropReceipt(YWLSH)).build();
                } else if (CZNR.equals(VoucherBusinessType.外部转出.getCode())) {

                }
            } else if (YWLSH.indexOf("02") == 0) {
                //单位业务流水号
                if (CZNR.equals(VoucherBusinessType.错缴.getCode())) {
                    return Response.status(200).entity(unitPayWrong.headUnitPayWrong(YWLSH)).build();
                } else if (CZNR.equals(VoucherBusinessType.汇补缴.getCode())) {
                    return Response.status(200).entity(unitRemittance.headUnitRemittance(YWLSH)).build();
                }
//                else if (CZNR.equals(VoucherBusinessType.补缴.getCode())) {
//                    return Response.status(200).entity(unitPayback.headUnitPayBackNotice(YWLSH)).build();
//                }
                else if (CZNR.equals(VoucherBusinessType.外部转入.getCode())) {

                } else if (CZNR.equals(VoucherBusinessType.部分提取.getCode())) {
                    return Response.status(200).entity(withdrawlTasks.printWithdrawlReceipt(YWLSH)).build();
                } else if (CZNR.equals(VoucherBusinessType.销户提取.getCode())) {
                    return Response.status(200).entity(withdrawlTasks.printWithdrawlReceipt(YWLSH)).build();
                } else if (CZNR.equals(VoucherBusinessType.外部转出.getCode())) {

                }
            } else if (YWLSH.indexOf("05") == 0) {
                //贷款业务流水号
                if (CZNR.equals(VoucherBusinessType.贷款发放.getCode())) {
                    return Response.status(200).entity(loanService.getConfirmLoansSubmit(null, YWLSH)).build();
                } else if (CZNR.equals(VoucherBusinessType.正常还款.getCode())) {
                    return Response.status(200).entity(repaymentService.printRepaymentReceipt(null, YWLSH)).build();
                } else {
                    return Response.status(200).entity(new Msg() {{
                        this.setMsg("操作内容错误");
                        this.setCode(ReturnCode.Error);
                    }}).build();
                }
            } else if (YWLSH.indexOf("11") == 0) {
                //财务业务日常流水号
            } else if (YWLSH.indexOf("09") == 0) {
                //财务业务活期转定期流水号
            } else if (YWLSH.indexOf("10") == 0) {
                //财务业务定期支取流水号
            } else {
                return Response.status(200).entity(new Msg() {{
                    this.setMsg("业务流水号错误");
                    this.setCode(ReturnCode.Error);
                }}).build();
            }
            return Response.status(200).entity(new Msg() {{
                this.setMsg("操作内容填写错误");
                this.setCode(ReturnCode.Error);
            }}).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Msg() {{
                this.setMsg(e.getMsg());
                this.setCode(ReturnCode.Error);
            }}).build();
        }
    }

    @Override
    public Response checkVoucher(TokenContext tokenContext, String KJQJ) {
        if (StringUtil.notEmpty(KJQJ)) {
            try {
                return Response.status(200).entity(iVoucherManagerService.checkoutVoucher(tokenContext.getUserInfo().getCZY(), KJQJ)).build();
            } catch (ErrorException e) {
                return Response.status(200).entity(new Error() {{
                    this.setMsg(e.getMsg());
                    this.setCode(e.getCode());
                }}).build();
            }
        } else {
            return Response.status(200).entity(new Msg() {{
                this.setMsg("会计期间不能为空");
                this.setCode(ReturnCode.Error);
            }}).build();
        }
    }

    @Override
    public Response getSubjectsCollect(String JZRQ) {
        if (StringUtil.notEmpty(JZRQ)) {
            try {
                return Response.status(200).entity(iVoucherManagerService.getSubjectsCollect(JZRQ)).build();
            } catch (ErrorException e) {
                return Response.status(200).entity(new Error() {{
                    this.setMsg(e.getMsg());
                    this.setCode(e.getCode());
                }}).build();
            }
        } else {
            return Response.status(200).entity(new Msg() {{
                this.setMsg("结账日期不能为空");
                this.setCode(ReturnCode.Error);
            }}).build();
        }
    }

    @Override
    public Response getBooksDetails(String KMBH, String KJQJKS, String KJQJJS, boolean BHWJZPZ) {
        if (StringUtil.notEmpty(KMBH)) {
            try {
                return Response.status(200).entity(iVoucherManagerService.getBooksDetails(KMBH, KJQJKS, KJQJJS, BHWJZPZ)).build();
            } catch (ErrorException e) {
                return Response.status(200).entity(new Error() {{
                    this.setMsg(e.getMsg());
                    this.setCode(e.getCode());
                }}).build();
            }
        } else {
            return Response.status(200).entity(new Msg() {{
                this.setMsg("科目编号不能为空");
                this.setCode(ReturnCode.Error);
            }}).build();
        }
    }

    @Override
    public Response getBooksGeneral(String KMBH, String KJQJKS, String KJQJJS, boolean BHWJZPZ) {
        if (StringUtil.notEmpty(KMBH)) {
            try {
                return Response.status(200).entity(iVoucherManagerService.getBooksGeneral(KMBH, KJQJKS, KJQJJS, BHWJZPZ)).build();
            } catch (ErrorException e) {
                return Response.status(200).entity(new Error() {{
                    this.setMsg(e.getMsg());
                    this.setCode(e.getCode());
                }}).build();
            }
        } else {
            return Response.status(200).entity(new Msg() {{
                this.setMsg("科目编号不能为空");
                this.setCode(ReturnCode.Error);
            }}).build();
        }
    }

    @Override
    public Response batchVoucherPdf(VoucherBatchPdfPost voucherBatchPdfPost) {
        try {
            return Response.status(200).entity(iVoucherManagerService.batchVoucherPdf(voucherBatchPdfPost)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }
}
