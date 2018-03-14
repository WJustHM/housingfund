package com.handge.housingfund.bank.server;

import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNoticeOut;

/**
 * Created by gxy on 17-8-16.
 */
public interface IHandleAccChangeNotice {
    void handler(AccChangeNoticeOut accChangeNoticeOut);
}
