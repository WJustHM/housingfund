package com.handge.housingfund.server.controllers.collection.api;


import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.individual.*;

import javax.ws.rs.core.Response;

public interface IndiAcctManageApi<T> {


    /**
     * 修改合并个人账户
     *
     * @param YWLSH
     **/
    public T putIndiAcctMerge(TokenContext tokenContext,final String YWLSH, final IndiAcctMergePut indiacctmergeput);


    /**
     * 合并个人账户回执单
     *
     * @param YWLSH
     **/
    public T headIndiAcctMerge(TokenContext tokenContext,final String YWLSH);


    /**
     * 查看合并个人账户详情
     *
     * @param YWLSH
     **/
    public T getIndiAcctMerge(TokenContext tokenContext,final String YWLSH);


    /**
     * 新建合并个人账户
     **/
    public T postIndiAcctMerge(TokenContext tokenContext,final IndiAcctMergePost indiacctmergepost);





    /**
     * Action（封存、启封、冻结、解冻、托管）个人账户业务新建
     *
     * @param GRZH 个人账号
     **/
    public T postIndiAcctAction(TokenContext tokenContext,final String GRZH, final String CZMC, final IndiAcctActionPost addindiacctaction);


    /**
     * 条件查询个人内部转移信息列表
     *
     * @param ZCDW      转出单位
     * @param ZhuangTai 状态
     **/
    public T getIndiAcctsTransferList(TokenContext tokenContext,final String ZCDW, final String ZhuangTai);



    /**
     * Action（封存、启封、冻结、解冻、托管）个人账户修改
     *
     * @param YWLSH 业务流水号
     **/
    public T putIndiAcctAction(final TokenContext tokenContext,final String YWLSH, final String CZMC, final IndiAcctActionPut indiacctactionput);


    /**
     * 个人账户Action（封存、启封、冻结、解冻、托管）回执单
     *
     * @param YWLSH 业务流水号
     **/
    public T headIndiAcctAction(TokenContext tokenContext,final String YWLSH, final String CZMC);


    /**
     * 查看Action（封存、启封、冻结、解冻、托管）个人账户详情
     *
     * @param YWLSH 业务流水号
     **/
    public T getIndiAcctAction(TokenContext tokenContext,final String YWLSH, final String CZMC);


    /**
     * 修改个人账户内部转移
     *
     * @param YWLSH 业务流水号
     **/
    public T putIndiAcctTransfer(TokenContext tokenContext,final String YWLSH, final IndiAcctTransferPut indiaccttransferput);


    /**
     * 个人账户内部转移回执单
     *
     * @param YWLSH
     **/
    public T headIndiAcctTransfer(TokenContext tokenContext,final String YWLSH);


    /**
     * 查看个人账户内部转移详情
     *
     * @param YWLSH 业务流水号
     **/
    public T getIndiAcctTransfer(TokenContext tokenContext,final String YWLSH);


    /**
     * 新建个人账户变更
     **/
    public T postIndiAcctAlter(TokenContext tokenContext,final IndiAcctAlterPost indiacctalterpost);


    /**
     * 根据业务类型，获取该业务下的账户列表（以默认参数查询）
     *
     * @param XingMing  姓名
     * @param ZJHM      证件号码
     * @param GRZH      个人账号（开户业务时无此项）
     * @param DWMC      单位名称
     * @param CZYY      操作原因
     * @param ZhuangTai 业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)
     * @param CZMC      根据业务名称查询（01:设立；02:变更；03:冻结;04:解冻;05:封存;06:启封;07:个人内部转移）
     **/
    public T getOperationAcctsList(TokenContext tokenContext,final String YWWD,final String XingMing, final String ZJHM, final String GRZH,
                                   final String DWMC, final String ZhuangTai, final String CZMC,
                                   final String CZYY, String page, String pageSize,String KSSJ,String JSSJ);

    /**
     * 根据业务类型，获取该业务下的账户列表（以默认参数查询）
     *
     * @param XingMing  姓名
     * @param ZJHM      证件号码
     * @param GRZH      个人账号（开户业务时无此项）
     * @param DWMC      单位名称
     * @param CZYY      操作原因
     * @param ZhuangTai 业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)
     * @param CZMC      根据业务名称查询（01:设立；02:变更；03:冻结;04:解冻;05:封存;06:启封;07:个人内部转移）
     **/
    public T getOperationAcctsList(TokenContext tokenContext,final String YWWD,final String XingMing, final String ZJHM, final String GRZH,
                                   final String DWMC, final String ZhuangTai, final String CZMC,
                                   final String CZYY,String marker,String action, String pageSize,String KSSJ,String JSSJ);

    /**
     * 个人账户Action（封存、启封、冻结、解冻）创建情时，根据个人账号自动获取相关信息
     *
     * @param GRZH
     **/
    public T getIndiAcctActionAuto(TokenContext tokenContext,final String GRZH, final String CZMC);


    /**
     * 个人账户合并业务添加/修改时，根据参数查询账号信息
     *
     * @param XingMing 姓名
     * @param ZJLX     证件类型
     * @param ZJHM     证件号码
     * @return
     */
    public T getIndiAcctMergeAuto(TokenContext tokenContext,final String XingMing, final String ZJLX, final String ZJHM);


    /**
     * 新建个人账户内部转移
     **/
    public T postAcctTransfer(TokenContext tokenContext,final IndiAcctTransferPost indiaccttransferpost);


    /**
     * 个人账户设立修改
     *
     * @param YWLSH 业务流水号
     **/
    public T putIndiAcctSet(TokenContext tokenContext,final String YWLSH, final IndiAcctSetPut indiacctsetput);


    /**
     * 个人登记（开户）回执单
     *
     * @param YWLSH 业务流水号
     **/
    public Response headIndiAcctSet(TokenContext tokenContext, final String YWLSH);


    /**
     * 设立个人账户详情
     *
     * @param YWLSH 业务流水号
     **/
    public Response getIndiAcctSet(TokenContext tokenContext,final String YWLSH);



    /**
     * 新建设立个人账户
     **/
    public T postIndiAcctSet(TokenContext tokenContext,final IndiAcctSetPost indiacctsetpost);

    /**
     * 内部转移
     **/
    public Response doInnerTransfer(TokenContext tokenContext, InnerTransferPost innerTransferPost);

    /**
     * 查询个人账户信息列表
     *
     * @param DWMC     单位名称
     * @param GRZH     个人账号
     * @param XingMing 姓名
     * @param ZJHM     证件号码
     * @param GRZHZT   个人账户状态(00:所有；01：正常；:02:封存；03:合并销户；04:外部转出销户；05：提取销户；06：冻结；99：其他)
     **/
    public T getIndiAcctsList(TokenContext tokenContext,final String DWMC, final String GRZH, final String XingMing, final String ZJHM,
                              final String GRZHZT,final String YWWD,final String SFDJ, String startTime, String endTime,String page, String pageSize);


    /**
     * 修改个人账户变更
     *
     * @param YWLSH 业务流水号
     **/
    public T putIndiAcctAlter(TokenContext tokenContext,final String YWLSH, final IndiAcctAlterPut indiacctalterput);


    /**
     * 个人信息变更回执单
     *
     * @param YWLSH 业务流水号
     **/
    public T headIndiAcctAlter(TokenContext tokenContext,final String YWLSH);


    /**
     * 变更个人账户详情
     *
     * @param YWLSH 业务流水号
     **/
    public T getIndiAcctAlter(TokenContext tokenContext,final String YWLSH);




    /**
     *个人账户冻结提交
     **/
    public T putIndiAcctFreezeSubmit(TokenContext tokenContext,final  IndiAcctFreezeSubmitPost indiacctfreezesubmitpost);


    /**
     *个人账户封存提交
     **/
    public T postIndiAcctSealedSubmit(TokenContext tokenContext,final  IndiAcctSealedSubmitPost indiacctsealedsubmitpost);


    /**
     *个人账户设立提交
     **/
    public T postIndiAcctSetSubmit(TokenContext tokenContext,final  IndiAcctSetSubmitPost indiacctsetsubmitpost);


    /**
     *个人账户内部转移提交
     **/
    public T postIndiAcctTransferSubmit(TokenContext tokenContext,final  IndiAcctTransferSubmitPost indiaccttransfersubmitpost);


    /**
     *个人账户合并提交
     **/
    public T postIndiAccttMergeSubmit(TokenContext tokenContext,final  IndiAccttMergeSubmitPost indiaccttmergesubmitpost);


    /**
     *个人账户变更提交
     **/
    public T postAlterIndiAcctSubmit(TokenContext tokenContext,final  AlterIndiAcctSubmitPost alterindiacctsubmitpost);


    /**
     *个人账户启封提交
     **/
    public T postIndiAcctUnsealedSubmit(TokenContext tokenContext,final  IndiAcctUnsealedSubmitPost indiacctunsealedsubmitpost);


    /**
     * 合户
     **/
    public T doMerge(TokenContext tokenContext,String YZJHM,String XZJHM,String XINGMING,String GRCKZHHM);

    /**
     *个人账户解冻提交
     **/
    public T postIndiAcctUnfreezeSubmit(TokenContext tokenContext,final  IndiAcctUnfreezeSubmitPost indiacctunfreezesubmitpost);


    /**
     *通过账号查询个人账户信息
     **/
    public T getAcctsInfoDetails(TokenContext tokenContext,final  String GRZH);

    /**
     * 个人开户时检查个人信息
     */
    public T getIndiAcctSetCheck(TokenContext tokenContext,String xingMing, String zjlx, String zjhm);

    /**
     * 银行列表
     */
    public T getBanks(TokenContext tokenContext,final String Code , final String  Name, final  String pageNo, final String pageSize);

    /**
     * 银行列表
     */
    public T getBanks(TokenContext tokenContext,final String Code , final String  Name, String marker,String action, final String pageSize);

    /*
    *  个人缴存明细
    */
    public T getPersonDepositDetails(TokenContext tokenContext,String grzh, String pageNo, String pageSize);

    /*
     *  获取证件号码相同的账号
     */
    public T getAccounts(TokenContext tokenContext, String zjhm,String GLXH);

    /*
    *  转移列表
    * */
    public T getTransferList(TokenContext tokenContext, String Xingming, String GRZH, String ZCDWM, String ZRDWM, String ZJHM, String page, String pagesize, String KSSJ, String JSSJ);

    /*
    *  转移列表
    * */
    public T getTransferList(TokenContext tokenContext, String Xingming, String GRZH, String ZCDWM, String ZRDWM, String ZJHM, String marker, String action, String pagesize, String KSSJ, String JSSJ);

    /*
     * 打印回执单
     */
    public T getIndiAcctMergeReceipt(TokenContext tokenContext, String ywlsh);
    /*
    * 打印个人缴存回执单
    */
    public T getPersonDepositPdfDetails(TokenContext tokenContext, String grzh,String hjnys, String hjnye);
    /*
     * 打印异地贷款缴存回执单
     */
    public T getDiffTerritoryLoadProvePdf(TokenContext tokenContext, String grzh);
}


