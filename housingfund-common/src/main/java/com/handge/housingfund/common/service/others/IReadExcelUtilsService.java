package com.handge.housingfund.common.service.others;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.individual.ImportExcelRes;

/**
 * Created by sjw on 2017/10/23.
 */
public interface IReadExcelUtilsService {
    ImportExcelRes getImportUnitAcctInfo(TokenContext tokenContext, String id);
    ImportExcelRes getImportIndiAcctInfo(TokenContext tokenContext, String id);
    ImportExcelRes getImportPersonRadix(TokenContext tokenContext, String id);
}
