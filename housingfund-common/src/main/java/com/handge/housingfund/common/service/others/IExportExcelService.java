package com.handge.housingfund.common.service.others;


import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.loan.model.CommonResponses;

/**
 * Created by sjw on 2017/11/6.
 */
public interface IExportExcelService {
    CommonResponses getExceldata(TokenContext tokenContext, String DWZH,String sxny);
    CommonResponses getInventoryData(TokenContext tokenContext, String dwzh,String qcny);
    CommonResponses getemployeeListData(TokenContext tokenContext, String dwzh);
    CommonResponses getAnnualReportData(TokenContext tokenContext);
    CommonResponses getPaymentHistoryData(TokenContext tokenContext,String dkzh,String hkrqs,String hkrqe);
}
