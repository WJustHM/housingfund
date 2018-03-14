package com.handge.housingfund.server.controllers.finance.api.impl;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.Msg;
import com.handge.housingfund.common.service.account.model.Success;
import com.handge.housingfund.common.service.finance.IActived2FixedService;
import com.handge.housingfund.common.service.finance.IFinanceTrader;
import com.handge.housingfund.common.service.finance.IFixedDrawService;
import com.handge.housingfund.common.service.finance.IFixedRecordService;
import com.handge.housingfund.common.service.finance.model.Actived2Fixed;
import com.handge.housingfund.common.service.finance.model.ActivedBalance;
import com.handge.housingfund.common.service.finance.model.FixedDraw;
import com.handge.housingfund.common.service.loan.model.Error;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnCode;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.server.controllers.finance.api.IFixedManageAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.Arrays;

/**
 * 创建人Dalu Guo
 * 创建时间 2017/9/13.
 * 描述
 */
@SuppressWarnings("Duplicates")
@Component
public class FixedManageAPI implements IFixedManageAPI{
    @Autowired
    IFixedRecordService iFixedRecordService;
    @Autowired
    IActived2FixedService iActived2FixedService;
    @Autowired
    IFixedDrawService iFixedDrawService;
    @Autowired
    IFinanceTrader iFinanceTrader;

    @Override
    public Response getFixedRecords(TokenContext tokenContext, String khyhmc, String acctNo, String bookNo, String bookListNo, String status, int pageNo, int pageSize) {
        if (pageNo == 0)
            pageNo = 1;
        if (pageSize == 0)
            pageSize = 10;

        int bookLIstNo;
        try {
            if (!StringUtil.notEmpty(bookListNo)) bookLIstNo = -1;
            else bookLIstNo = Integer.parseInt(bookListNo);
        } catch (Exception e) {
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("笔号格式不正确,只能是纯数字");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        }

        try {
            return Response.status(200).entity(iFixedRecordService.getFixedRecords(khyhmc, acctNo, bookNo, bookLIstNo, status, pageNo, pageSize)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getFixedRecord(TokenContext tokenContext,String id) {
        if (!StringUtil.notEmpty(id)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("id不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();

        try {
            return Response.status(200).entity(iFixedRecordService.getFixedRecord(id)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getActivedToFixeds(TokenContext tokenContext,String khyhmc, String acctNo, String depositPeriod, String status, int pageNo, int pageSize) {
        if (pageNo == 0)
            pageNo = 1;
        if (pageSize == 0)
            pageSize = 10;

        try {
            return Response.status(200).entity(iActived2FixedService.getActivedToFixeds(tokenContext,khyhmc, acctNo, depositPeriod, status, pageNo, pageSize)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getActivedToFixed(TokenContext tokenContext,String id) {
        if (!StringUtil.notEmpty(id)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("id不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();

        try {
            return Response.status(200).entity(iActived2FixedService.getActivedToFixed(tokenContext,id)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response addActivedToFixed(TokenContext tokenContext,Actived2Fixed actived2Fixed, String type) {
        if (!StringUtil.notEmpty(type) || !Arrays.asList("0", "1").contains(type))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("操作类型不正确");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (actived2Fixed == null)
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("活期转定期内容不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(actived2Fixed.getAcct_name()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("账户户名不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(actived2Fixed.getAcct_no()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("账号不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(actived2Fixed.getDeposit_period()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("存期不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(actived2Fixed.getAmt()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("本金金额不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.isMoney(actived2Fixed.getAmt()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("本金金额格式不正确");
                    this.setCode(ReturnCode.Error);
                }
            }).build();

        try {
            return Response.status(200).entity(iActived2FixedService.addActivedToFixed(tokenContext,actived2Fixed, type)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response modifyActivedToFixed(TokenContext tokenContext,String id, Actived2Fixed actived2Fixed, String type) {
        if (!StringUtil.notEmpty(type) || !Arrays.asList("0", "1").contains(type))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("操作类型不正确");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(id))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("id不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (actived2Fixed == null)
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("活期转定期内容不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(actived2Fixed.getAcct_name()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("账户户名不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(actived2Fixed.getAcct_no()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("账号不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(actived2Fixed.getDeposit_period()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("存期不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(actived2Fixed.getAmt()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("本金金额不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.isMoney(actived2Fixed.getAmt()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("本金金额格式不正确");
                    this.setCode(ReturnCode.Error);
                }
            }).build();

        try {
            return Response.status(200).entity(iActived2FixedService.modifyActivedToFixed(tokenContext,id, actived2Fixed, type) ? new Msg() {{
                this.setCode(ReturnCode.Success);
                this.setMsg("此活期转定期不能修改");
            }} : new Success(){{
                this.setState("修改活期转定期成功");
            }}).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response delActivedToFixed(TokenContext tokenContext,String id) {
        if (!StringUtil.notEmpty(id)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("id不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        try {
            return Response.status(200).entity(iActived2FixedService.delActivedToFixed(tokenContext,id) ? new Msg() {{
                this.setCode(ReturnCode.Success);
                this.setMsg("此活期转定期不能删除");
            }} : new Success(){{
                this.setState("删除活期转定期成功");
            }}).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response submitActivedToFixed(TokenContext tokenContext, String id) {
        if (!StringUtil.notEmpty(id)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("id不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();

        try {
            return Response.status(200).entity(iActived2FixedService.submitActivedToFixed(tokenContext,id) ? new Msg() {{
                this.setCode(ReturnCode.Success);
                this.setMsg("此活期转定期不能提交");
            }} : new Success(){{
                this.setState("提交活期转定期成功");
            }}).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response revokeActivedToFixed(TokenContext tokenContext, String id) {
        if (!StringUtil.notEmpty(id)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("id不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();

        try {
            return Response.status(200).entity(iActived2FixedService.revokeActivedToFixed(tokenContext,id) ? new Msg() {{
                this.setCode(ReturnCode.Success);
                this.setMsg("此活期转定期不能撤回");
            }} : new Success(){{
                this.setState("撤回活期转定期成功");
            }}).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response updateA2FStep(TokenContext tokenContext, String id, String step) {
        if (!StringUtil.notEmpty(id)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("id不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        if (!StringUtil.notEmpty(step)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("状态不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();

        try {
            return Response.status(200).entity(iActived2FixedService.updateA2FStep(tokenContext,id, step) ? new Msg() {{
                this.setCode(ReturnCode.Success);
                this.setMsg("此活期转定期不能人工处理");
            }} : new Success(){{
                this.setState("操作成功");
            }}).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getFixedDraws(TokenContext tokenContext,String khyhmc, String acctNo, String bookNo, String bookListNo, String status, int pageNo, int pageSize) {
        if (pageNo == 0)
            pageNo = 1;
        if (pageSize == 0)
            pageSize = 10;

        int bookLIstNo;
        try {
            if (!StringUtil.notEmpty(bookListNo)) bookLIstNo = -1;
            else bookLIstNo = Integer.parseInt(bookListNo);
        } catch (Exception e) {
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("笔号格式不正确,只能是纯数字");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        }

        try {
            return Response.status(200).entity(iFixedDrawService.getFixedDraws(tokenContext,khyhmc, acctNo, bookNo, bookLIstNo, status, pageNo, pageSize)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getFixedDraw(TokenContext tokenContext,String id) {
        if (!StringUtil.notEmpty(id)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("id不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();

        try {
            return Response.status(200).entity(iFixedDrawService.getFixedDraw(tokenContext,id)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response addFixedDraw(TokenContext tokenContext,FixedDraw fixedDraw, String type) {
        if (!StringUtil.notEmpty(type) || !Arrays.asList("0", "1").contains(type))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("操作类型不正确");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (fixedDraw == null)
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("定期支取内容不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(fixedDraw.getAcct_no()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("账号不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(fixedDraw.getDeposit_period()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("存期不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(fixedDraw.getDeposit_begin_date()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("存入日期不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(fixedDraw.getDeposit_end_date()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("到期日期不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(fixedDraw.getDraw_amt()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("支取金额不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.isMoney(fixedDraw.getDraw_amt()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("支取金额格式不正确");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(fixedDraw.getBook_no()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("册号不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(fixedDraw.getZqqk()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("支取情况不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();

        try {
            return Response.status(200).entity(iFixedDrawService.addFixedDraw(tokenContext,fixedDraw, type)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response modifyFixedDraw(TokenContext tokenContext,String id, FixedDraw fixedDraw, String type) {
        if (!StringUtil.notEmpty(type) || !Arrays.asList("0", "1").contains(type))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("操作类型不正确");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(id))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("id不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (fixedDraw == null)
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("定期支取内容不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(fixedDraw.getAcct_no()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("账号不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(fixedDraw.getDeposit_period()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("存期不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(fixedDraw.getDeposit_begin_date()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("存入日期不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(fixedDraw.getDeposit_end_date()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("到期日期不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(fixedDraw.getDraw_amt()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("支取金额不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.isMoney(fixedDraw.getDraw_amt()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("支取金额格式不正确");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(fixedDraw.getBook_no()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("册号不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(fixedDraw.getZqqk()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("支取情况不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();

        try {
            return Response.status(200).entity(iFixedDrawService.modifyFixedDraw(tokenContext,id, fixedDraw, type) ? new Msg() {{
                this.setCode(ReturnCode.Success);
                this.setMsg("此定期支取不能修改");
            }} : new Success(){{
                this.setState("修改定期支取成功");
            }}).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response delFixedDraw(TokenContext tokenContext,String id) {
        if (!StringUtil.notEmpty(id)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("id不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();

        try {
            return Response.status(200).entity(iFixedDrawService.delFixedDraw(tokenContext,id) ? new Msg() {{
                this.setCode(ReturnCode.Success);
                this.setMsg("此定期支取不能删除");
            }} : new Success(){{
                this.setState("删除定期支取成功");
            }}).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response submitFixedDraw(TokenContext tokenContext, String id) {
        if (!StringUtil.notEmpty(id)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("id不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();

        try {
            return Response.status(200).entity(iFixedDrawService.submitFixedDraw(tokenContext,id) ? new Msg() {{
                this.setCode(ReturnCode.Success);
                this.setMsg("此定期支取不能提交");
            }} : new Success(){{
                this.setState("提交定期支取成功");
            }}).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response revokeFixedDraw(TokenContext tokenContext, String id) {
        if (!StringUtil.notEmpty(id)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("id不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();

        try {
            return Response.status(200).entity(iFixedDrawService.revokeFixedDraw(tokenContext,id) ? new Msg() {{
                this.setCode(ReturnCode.Success);
                this.setMsg("此定期支取不能撤回");
            }} : new Success(){{
                this.setState("撤回定期支取成功");
            }}).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response updateF2AStep(TokenContext tokenContext, String id, String step) {
        if (!StringUtil.notEmpty(id)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("id不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();
        if (!StringUtil.notEmpty(step)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("状态不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();

        try {
            return Response.status(200).entity(iFixedDrawService.updateF2AStep(tokenContext,id, step) ? new Msg() {{
                this.setCode(ReturnCode.Success);
                this.setMsg("此定期支取不能人工处理");
            }} : new Success(){{
                this.setState("操作成功");
            }}).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getActivedBalance(TokenContext tokenContext, String zhhm) {
        if (!StringUtil.notEmpty(zhhm)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("专户号码不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();

        ActivedBalance activedBalance = iFinanceTrader.getActivedBalance(zhhm);
        if (activedBalance == null) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("查询活期余额失败");
                this.setCode(ReturnCode.Error);
            }
        }).build();

        try {
            return Response.status(200).entity(activedBalance).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }
}
