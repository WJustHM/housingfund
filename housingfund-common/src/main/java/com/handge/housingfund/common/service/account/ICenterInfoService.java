package com.handge.housingfund.common.service.account;

import com.handge.housingfund.common.service.account.model.CenterInfoModel;
import com.handge.housingfund.common.service.account.model.PageRes;

/**
 * Created by gxy on 17-12-8.
 */
public interface ICenterInfoService {
    /**
     * 获取中心信息列表
     * @param zxmc   中心名称
     * @param zxdm   中心代码
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageRes<CenterInfoModel> getCenterInfoList(String zxmc, String zxdm, int pageNo, int pageSize);
}
