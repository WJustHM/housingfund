package com.handge.housingfund.server.controllers.account.api;

import javax.ws.rs.core.Response;

/**
 * Created by gxy on 17-12-8.
 */
public interface ICenterInfoAPI {

    /**
     * 获取中心信息列表
     * @param zxmc   中心名称
     * @param zxdm   中心代码
     * @param pageNo
     * @param pageSize
     * @return
     */
    Response getCenterInfoList(String zxmc, String zxdm, int pageNo, int pageSize);
}
