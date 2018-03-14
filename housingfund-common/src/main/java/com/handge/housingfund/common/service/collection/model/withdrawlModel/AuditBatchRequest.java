package com.handge.housingfund.common.service.collection.model.withdrawlModel;



import com.handge.housingfund.common.service.review.model.ReviewInfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xuefei_wang on 17-6-27.
 */

@XmlRootElement(name = "BatchAuditInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class AuditBatchRequest implements Serializable {
    /**
     * ids 业务流水号集合
     * reviewInfo 审批信息
     */
   private ArrayList<String> ids ;
   private ReviewInfo reviewInfo;

    public ArrayList<String> getIds() {
        return ids;
    }

    public void setIds(ArrayList<String> ids) {
        this.ids = ids;
    }

    public ReviewInfo getReviewInfo() {
        return reviewInfo;
    }

    public void setReviewInfo(ReviewInfo reviewInfo) {
        this.reviewInfo = reviewInfo;
    }
}
