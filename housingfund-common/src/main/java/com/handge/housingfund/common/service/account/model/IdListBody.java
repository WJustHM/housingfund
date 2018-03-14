package com.handge.housingfund.common.service.account.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Created by tanyi on 2017/7/18.
 */
@XmlRootElement(name = "IdListBody")
@XmlAccessorType(XmlAccessType.FIELD)
public class IdListBody extends Req<List<String>> implements Serializable {
}
