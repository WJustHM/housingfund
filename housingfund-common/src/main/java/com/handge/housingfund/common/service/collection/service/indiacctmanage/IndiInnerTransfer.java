package com.handge.housingfund.common.service.collection.service.indiacctmanage;

import com.handge.housingfund.common.service.collection.model.individual.IndiAcctTransferPost;
import com.handge.housingfund.common.service.collection.model.individual.IndiAcctTransferPut;
import com.handge.housingfund.common.service.util.ResponseEntity;

import java.util.List;

/**
 * Created by Liujuhao on 2017/7/1.
 * 内部转移
 */
public interface IndiInnerTransfer<T> {

    public ResponseEntity getTransferInfo(final String ZCDWZH, final String ZhuangTai);

    public ResponseEntity addAcctTransfer(IndiAcctTransferPost addIndiAcctTransfer);

    public ResponseEntity reAcctTransfer(String YWLSH, IndiAcctTransferPut reIndiAcctTransfer);

    public ResponseEntity showAcctTransfer(String YWLSH);

    public ResponseEntity doAcctTransfer(String YWLSH);

    public ResponseEntity headIndiAcctTransfer(String ywlsh);

    public ResponseEntity submitIndiAcctTransfer(List<String> YWLSHJH);
}
