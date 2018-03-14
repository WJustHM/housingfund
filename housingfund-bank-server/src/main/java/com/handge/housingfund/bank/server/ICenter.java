package com.handge.housingfund.bank.server;

import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNoticeIn;
import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNoticeOut;
import com.handge.housingfund.common.service.bank.bean.sys.InterfaceCheckIn;
import com.handge.housingfund.common.service.bank.bean.sys.InterfaceCheckOut;

/**
 * Created by gxy on 17-8-14.
 */
public interface ICenter {
    /**
     * 接口探测(SYS600).
     * 用于测试系统服务是否正常.
     * @param interfaceCheckOut InterfaceCheckOut对象
     * @return InterfaceCheckIn, 收到返回值, 则表示系统服务正常, 反之亦然.
     * @throws Exception 异常
     */
    InterfaceCheckIn recMsg(InterfaceCheckOut interfaceCheckOut) throws Exception;

    /**
     * 账户变动通知(SBDC100)
     * @param accChangeNoticeOut AccChangeNoticeOut对象
     * @return AccChangeNoticeIn, 返回给住建部, 告诉住建部接收成功
     * @throws Exception 异常
     */
    AccChangeNoticeIn recMsg(AccChangeNoticeOut accChangeNoticeOut) throws Exception;
}
