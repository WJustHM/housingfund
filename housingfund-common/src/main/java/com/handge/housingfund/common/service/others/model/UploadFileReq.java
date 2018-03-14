package com.handge.housingfund.common.service.others.model;

import com.handge.housingfund.common.service.account.model.Req;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
/**
 * Created by tanyi on 2017/8/19.
 */
@XmlRootElement(name = "UploadFileReq")
@XmlAccessorType(XmlAccessType.FIELD)
public class UploadFileReq extends Req<UploadFile> implements Serializable {
}
