package com.handge.housingfund.server.controllers.finance.api.impl;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.Msg;
import com.handge.housingfund.common.service.account.model.Success;
import com.handge.housingfund.common.service.finance.IReconciliationService;
import com.handge.housingfund.common.service.finance.model.ReconciliationBase;
import com.handge.housingfund.common.service.loan.model.Error;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnCode;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.server.controllers.finance.api.IReconciliationAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;

/**
 * Created by gxy on 17-10-25.
 */
@Component
public class ReconciliationAPI implements IReconciliationAPI {

    @Autowired
    private IReconciliationService iReconciliationService;

    @Override
    public Response getReconciliationList(String yhmc, String zhhm, String tjny, int pageNo, int pageSize) {
        if (pageNo == 0)
            pageNo = 1;
        if (pageSize == 0)
            pageSize = 10;

        try {
            return Response.status(200).entity(iReconciliationService.getReconciliationList(yhmc, zhhm, tjny, pageNo, pageSize)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getReconciliation(String id) {
        if (!StringUtil.notEmpty(id)) return Response.status(200).entity(new Msg() {
            {
                this.setMsg("银行余额调节id不能为空");
                this.setCode(ReturnCode.Error);
            }
        }).build();

        try {
            return Response.status(200).entity(iReconciliationService.getReconciliation(id)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response addReconciliation(TokenContext tokenContext,ReconciliationBase reconciliation) {
        if (reconciliation == null)
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("银行余额调节内容不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(reconciliation.getTJRQ()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("调节日期不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(reconciliation.getNode()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("银行节点号不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(reconciliation.getZHHM()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("专户号码不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(reconciliation.getZXCKYE()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("中心银行存款日记账余额不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(reconciliation.getYHDZYE()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("银行对账单余额不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(reconciliation.getZXYE()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("调节后的中心存款余额不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(reconciliation.getYHYE()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("调节后的银行存款余额不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!reconciliation.getZXYE().equals(reconciliation.getYHYE()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("调节后的余额不一致");
                    this.setCode(ReturnCode.Error);
                }
            }).build();

        try {
            return Response.status(200).entity(iReconciliationService.addReconciliation(tokenContext,reconciliation)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response updateReconciliation(TokenContext tokenContext,String id, ReconciliationBase reconciliation) {
        if (!StringUtil.notEmpty(id))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("银行余额调节id不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (reconciliation == null)
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("银行余额调节内容不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(reconciliation.getTJRQ()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("调节日期不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(reconciliation.getNode()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("银行节点号不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(reconciliation.getZHHM()))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("专户号码不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();

        try {
            return Response.status(200).entity(iReconciliationService.updateReconciliation(tokenContext,id, reconciliation)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response delReconciliation(String id) {
        try {
            return Response.status(200).entity(iReconciliationService.delReconciliation(id) ? new Msg() {{
                this.setCode(ReturnCode.Success);
                this.setMsg("删除失败");
            }} : new Success() {{
                this.setState("删除成功");
            }}).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }

    @Override
    public Response getBalance(String node, String khbh, String yhzh, String tjny) {
        if (!StringUtil.notEmpty(node))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("银行节点号不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(khbh))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("客户编号不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(yhzh))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("银行账户不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        if (!StringUtil.notEmpty(tjny))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("调节年月不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();

        try {
            return Response.status(200).entity(iReconciliationService.getReconciliationInitBalance(node, khbh, yhzh, tjny)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {{
                this.setMsg(e.getMsg());
                this.setCode(e.getCode());
            }}).build();
        }
    }
}
