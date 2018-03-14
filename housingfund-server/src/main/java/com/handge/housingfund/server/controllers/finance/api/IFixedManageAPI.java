package com.handge.housingfund.server.controllers.finance.api;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.finance.model.Actived2Fixed;
import com.handge.housingfund.common.service.finance.model.FixedDraw;

import javax.ws.rs.core.Response;

/**
 * 创建人Dalu Guo
 * 创建时间 2017/9/13.
 * 描述
 */
public interface IFixedManageAPI {
    Response getFixedRecords(TokenContext tokenContext,String khyhmc, String acctNo, String bookNo, String bookListNo, String status, int pageNo, int pageSize);

    Response getFixedRecord(TokenContext tokenContext, String id);

    Response getActivedToFixeds(TokenContext tokenContext,String khyhmc, String acctNo, String depositPeriod, String status, int pageNo, int pageSize);

    Response getActivedToFixed(TokenContext tokenContext,String id);

    Response addActivedToFixed(TokenContext tokenContext,Actived2Fixed actived2Fixed, String type);

    Response modifyActivedToFixed(TokenContext tokenContext,String id, Actived2Fixed actived2Fixed, String type);

    Response delActivedToFixed(TokenContext tokenContext,String id);

    Response submitActivedToFixed(TokenContext tokenContext,String id);

    Response revokeActivedToFixed(TokenContext tokenContext,String id);

    Response updateA2FStep(TokenContext tokenContext, String id, String step);

    Response getFixedDraws(TokenContext tokenContext,String khyhmc, String acctNo, String bookNo, String bookListNo, String status, int pageNo, int pageSize);

    Response getFixedDraw(TokenContext tokenContext,String id);

    Response addFixedDraw(TokenContext tokenContext,FixedDraw fixedDraw, String type);

    Response modifyFixedDraw(TokenContext tokenContext,String id, FixedDraw fixedDraw, String type);

    Response delFixedDraw(TokenContext tokenContext,String id);

    Response submitFixedDraw(TokenContext tokenContext,String id);

    Response revokeFixedDraw(TokenContext tokenContext,String id);

    Response updateF2AStep(TokenContext tokenContext, String id, String step);

    Response getActivedBalance(TokenContext tokenContext,String id);
}
