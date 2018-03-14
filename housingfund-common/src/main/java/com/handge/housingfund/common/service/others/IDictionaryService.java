package com.handge.housingfund.common.service.others;

import com.handge.housingfund.common.service.others.model.CommonDictionary;
import com.handge.housingfund.common.service.others.model.SingleDictionaryDetail;

import java.util.HashMap;
import java.util.List;

/**
 * 获取字典表接口 Created by gxy on 17-7-6.
 */
public interface IDictionaryService {
	public HashMap<String, List<CommonDictionary>> getDictionary();
	public SingleDictionaryDetail getSingleDetail(String code, String type);
	public SingleDictionaryDetail getCodeSingle(String name, String type);
	public List<CommonDictionary> getDictionaryByType(String type);
}

