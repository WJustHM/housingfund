package com.handge.housingfund.common.service.bank.bean.center;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 结果列表
 */
public class BDC121Summary implements Serializable {
    private static final long serialVersionUID = 5096626685584975697L;
    /**
     * 通知设定序号(required)
     */
    private String NoticeSetSerialNo;
    /**
     * 通知标志(required)
     */
    private String NoticeFlag;
    /**
     * 通知设定日期
     */
    private String NoticeDrawSetDate;
    /**
     * 通知支取日期(required)
     */
    private String NoticeDrawDate;
    /**
     * 通知支取金额(required)
     */
    private BigDecimal NoticeDrawAmt;

    public BDC121Summary() {
    }

    public BDC121Summary(String noticeSetSerialNo, String noticeFlag, String noticeDrawDate, BigDecimal noticeDrawAmt) {
        NoticeSetSerialNo = noticeSetSerialNo;
        NoticeFlag = noticeFlag;
        NoticeDrawDate = noticeDrawDate;
        NoticeDrawAmt = noticeDrawAmt;
    }

    public String getNoticeSetSerialNo() {
        return NoticeSetSerialNo;
    }

    public void setNoticeSetSerialNo(String noticeSetSerialNo) {
        NoticeSetSerialNo = noticeSetSerialNo;
    }

    public String getNoticeFlag() {
        return NoticeFlag;
    }

    public void setNoticeFlag(String noticeFlag) {
        NoticeFlag = noticeFlag;
    }

    public String getNoticeDrawSetDate() {
        return NoticeDrawSetDate;
    }

    public void setNoticeDrawSetDate(String noticeDrawSetDate) {
        NoticeDrawSetDate = noticeDrawSetDate;
    }

    public String getNoticeDrawDate() {
        return NoticeDrawDate;
    }

    public void setNoticeDrawDate(String noticeDrawDate) {
        NoticeDrawDate = noticeDrawDate;
    }

    public BigDecimal getNoticeDrawAmt() {
        return NoticeDrawAmt;
    }

    public void setNoticeDrawAmt(BigDecimal noticeDrawAmt) {
        NoticeDrawAmt = noticeDrawAmt;
    }

    @Override
    public String toString() {
        return "Summary{" +
                "NoticeSetSerialNo='" + NoticeSetSerialNo + '\'' +
                ", NoticeFlag='" + NoticeFlag + '\'' +
                ", NoticeDrawSetDate='" + NoticeDrawSetDate + '\'' +
                ", NoticeDrawDate='" + NoticeDrawDate + '\'' +
                ", NoticeDrawAmt=" + NoticeDrawAmt +
                '}';
    }
}
