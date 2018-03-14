package com.handge.housingfund.common.service.collection.service.indiacctmanage;

import com.handge.housingfund.common.service.collection.model.individual.IndiAcctMergePost;
import com.handge.housingfund.common.service.collection.model.individual.IndiAcctMergePut;
import com.handge.housingfund.common.service.util.ResponseEntity;

/**
 * Created by Liujuhao on 2017/7/1.
 */
public interface IndiAcctMerge {

    public ResponseEntity getMergeInfo(final String DWMC, final String ZhuangTai, final String XingMing, final String ZJHM, final String GRZH, final String CZMC);

    public ResponseEntity addAcctMerge(IndiAcctMergePost addIndiAcctMerge);

    public ResponseEntity reAcctMerge(String YWLSH, IndiAcctMergePut reIndiAcctMerge);

    public ResponseEntity showAcctMerge(String YWLSH);

    public ResponseEntity AutoIndiAcctMerge(final String XingMing, final String ZJLX, final String ZJHM);

    public ResponseEntity headAcctMerge(final String YWLSH);

    public ResponseEntity doAcctMerge(String YWLSH);

}
