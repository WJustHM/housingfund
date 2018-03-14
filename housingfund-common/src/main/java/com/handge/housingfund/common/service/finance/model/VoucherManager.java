package com.handge.housingfund.common.service.finance.model;

import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNoticeFile;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Created by tanyi on 2017/8/26.
 */
@XmlRootElement(name = "业务凭证管理详情")
@XmlAccessorType(XmlAccessType.FIELD)
public class VoucherManager extends VoucherManagerBase implements Serializable {

    private static final long serialVersionUID = 1159192128720897639L;

    private String HSDW;//核算单位
    private String FJSL;//附件数量
    private String JFHJ;//借方合计
    private String DFHJ;//贷方合计
    private String CWZG;//财务主管
    private String FuHe;//复核
    private String ChuNa;//出纳
    private String ZhiDan;//制单
    private String CZNR;//操作内容
    private String KMYEFX;//科目余额方向
    private String HJJFJE;//借方金额合计
    private String HJDFJE;//贷方金额合计
    private String PDFID;//PDF文件ID

    private List<AccChangeNoticeFile> accChangeNoticeFiles;//    账户变动通知回执单

    private List<VoucherMangerList> voucherMangerLists;//会计科目列表

    public VoucherManager() {
    }

    public VoucherManager(String id, String JZPZH, String JZRQ, String jiZhang, String YWMC,
                          String YWLX, String YWLSH, String FSE, String ZhaiYao, String HSDW,
                          String FJSL, String JFHJ, String DFHJ, String CWZG, String fuHe, String chuNa,
                          String zhiDan, String CZNR, String KMYEFX, String HJJFJE, String HJDFJE,
                          String PDFID, List<AccChangeNoticeFile> accChangeNoticeFile, List<VoucherMangerList> voucherMangerLists) {
        super(id, JZPZH, JZRQ, jiZhang, YWMC, YWLX, YWLSH, FSE, ZhaiYao);
        this.HSDW = HSDW;
        this.FJSL = FJSL;
        this.JFHJ = JFHJ;
        this.DFHJ = DFHJ;
        this.CWZG = CWZG;
        FuHe = fuHe;
        ChuNa = chuNa;
        ZhiDan = zhiDan;
        this.CZNR = CZNR;
        this.KMYEFX = KMYEFX;
        this.HJJFJE = HJJFJE;
        this.HJDFJE = HJDFJE;
        this.PDFID = PDFID;
        this.accChangeNoticeFiles = accChangeNoticeFiles;
        this.voucherMangerLists = voucherMangerLists;
    }

    public String getHSDW() {
        return HSDW;
    }

    public void setHSDW(String HSDW) {
        this.HSDW = HSDW;
    }

    public String getFJSL() {
        return FJSL;
    }

    public void setFJSL(String FJSL) {
        this.FJSL = FJSL;
    }

    public String getJFHJ() {
        return JFHJ;
    }

    public void setJFHJ(String JFHJ) {
        this.JFHJ = JFHJ;
    }

    public String getDFHJ() {
        return DFHJ;
    }

    public void setDFHJ(String DFHJ) {
        this.DFHJ = DFHJ;
    }

    public String getCWZG() {
        return CWZG;
    }

    public void setCWZG(String CWZG) {
        this.CWZG = CWZG;
    }

    public String getFuHe() {
        return FuHe;
    }

    public void setFuHe(String fuHe) {
        FuHe = fuHe;
    }

    public String getChuNa() {
        return ChuNa;
    }

    public void setChuNa(String chuNa) {
        ChuNa = chuNa;
    }

    public String getZhiDan() {
        return ZhiDan;
    }

    public void setZhiDan(String zhiDan) {
        ZhiDan = zhiDan;
    }

    public String getCZNR() {
        return CZNR;
    }

    public void setCZNR(String CZNR) {
        this.CZNR = CZNR;
    }

    public String getKMYEFX() {
        return KMYEFX;
    }

    public void setKMYEFX(String KMYEFX) {
        this.KMYEFX = KMYEFX;
    }

    public String getHJJFJE() {
        return HJJFJE;
    }

    public void setHJJFJE(String HJJFJE) {
        this.HJJFJE = HJJFJE;
    }

    public String getHJDFJE() {
        return HJDFJE;
    }

    public void setHJDFJE(String HJDFJE) {
        this.HJDFJE = HJDFJE;
    }

    public String getPDFID() {
        return PDFID;
    }

    public void setPDFID(String PDFID) {
        this.PDFID = PDFID;
    }

    public List<AccChangeNoticeFile> getAccChangeNoticeFiles() {
        return accChangeNoticeFiles;
    }

    public void setAccChangeNoticeFiles(List<AccChangeNoticeFile> accChangeNoticeFiles) {
        this.accChangeNoticeFiles = accChangeNoticeFiles;
    }

    public List<VoucherMangerList> getVoucherMangerLists() {
        return voucherMangerLists;
    }

    public void setVoucherMangerLists(List<VoucherMangerList> voucherMangerLists) {
        this.voucherMangerLists = voucherMangerLists;
    }
}
