package com.handge.housingfund.common.service.collection.service.unitdeposit;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.collection.model.*;
import com.handge.housingfund.common.service.collection.model.deposit.*;
import com.handge.housingfund.common.service.collection.service.common.WorkFlow;
import com.handge.housingfund.common.service.loan.model.CommonResponses;

import java.util.Date;

/**
 * 单位缴存清册 yangfan
 * 单位清册提交后不能修改
 */
public interface UnitDepositInventory extends WorkFlow{

    /**
     * 提交单位清册信息
     */
    public AddInventoryConfirmRes postUnitDepositInventory(TokenContext tokenContext, InventoryConfirmPost inventoryConfirmPost);

    /**
     * 获取单位缴存清册列表
     */
    public PageRes<ListInventoryResRes> getUnitInventoryInfo(TokenContext tokenContext,String dwmc, String dwzh, String czy,String ywwd, String kssj, String jssj, String pageNumber, String pageSize);

    /**
     * 通过清册确认单号得到汇缴清册详情
     */
    public GetRemittanceInventoryDetailRes getUnitRemittanceInventoryDetail(TokenContext tokenContext,String qcqrdh);

    /**
     *打印清册回执单
     */
    public CommonResponses getReceipt(String qcqrdh);

    /**
     * 根据单位缴存情况/人员信息来生成清册数据
     */
    public AutoRemittanceInventoryRes getUnitRemittanceInventoryAuto(TokenContext tokenContext,String dwzh);


    public void doFinal(String ywlsh);

    //下面是新API

    /**
     * 清册受理时，根据条件，获取清册信息(含详情)
     */
    InventoryBatchMessage getInventoryBatchList2(TokenContext tokenContext, String dwzh, String qcsjq, String qcsjz, String YWLSH);


    /**
     * 清册受理页面，清除不需要的个人详情信息，以免单位人数多的情况下，页面加载效率过低
     */
    InventoryBatchMessage getInventoryBatchList(TokenContext tokenContext, String dwzh, String qcsjq, String qcsjz, String ywlsh);

    /**
     * 清册受理提交
     */
    public BusCommonRetrun addInventoryBatch(TokenContext tokenContext, String dwzh, String qcsjq, String qcsjz, String jkfs, String cxqc);


    /**
     * 获取单位该月的最新清册详情,通过ywlsh（与下面的不一致）
     */
    public LatestInventory getLatestInventoryOfMonth(TokenContext tokenContext,String ywlsh);


    /**
     * 获取单位该月的最初的汇缴清册详情
     */
    public FirstInventory getFirstInventoryOfMonth(TokenContext tokenContext,String ywlsh);

    /**
     * 获取单位清册初始化页面
     */
    public InventoryInit getUnitInventoryInit(TokenContext tokenContext,String dwzh);

    LatestInventory getLatestInventoryOfMonth(String dwzh, Date sxyf);

    PageRes<InventoryDetail> getInventoryDetail(String dwzh,String qcyf,String xingMing, String pageNumber, String pageSize);

    PageResNew<ListInventoryResRes> getUnitInventoryInfo(TokenContext tokenContext, String dwmc, String dwzh, String czy, String ywwd, String kssj, String jssj, String marker, String pageSize, String action);

    /**
     *打印清册确认单
     */
    CommonResponses getReceipt(TokenContext tokenContext,String dwzh, String qcnyStr);

    InventoryMessage getInventory2(String dwzh, Date qcny);
    /**
     *获取清册数据
     */
    HeadRemittanceInventoryRes getInventoryConfiromationData(TokenContext tokenContext,String dwzh, String qcnyStr);
    /**
     *导出清册确认单数据
     */
    ExportInventoryConfirmationInfoRes getExportInventoryConfirmationInfo(String dwzh, String qcnyStr);


}
