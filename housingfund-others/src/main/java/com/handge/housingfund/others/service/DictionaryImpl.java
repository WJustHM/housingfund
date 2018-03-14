package com.handge.housingfund.others.service;

import com.handge.housingfund.common.service.others.IDictionaryService;
import com.handge.housingfund.common.service.others.model.CommonDictionary;
import com.handge.housingfund.common.service.others.model.SingleDictionaryDetail;
import com.handge.housingfund.common.service.util.CollectionUtils;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.database.dao.IDictionaryDAO;
import com.handge.housingfund.database.entities.CDictionary;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by gxy on 17-7-6.
 */

public class DictionaryImpl implements IDictionaryService {
	@Autowired
	IDictionaryDAO iDictionaryDAO;
	static volatile DictionaryImpl instance = null;

	private DictionaryImpl() {

	}

	public static DictionaryImpl createInstance() {
		if (instance == null) {
			synchronized (DictionaryImpl.class) {
				if (instance == null) {
					instance = new DictionaryImpl();
				}
			}
		}
		return instance;
	}

	public HashMap<String, List<CommonDictionary>> getDictionary() {
		HashMap<String, List<CommonDictionary>> map = new HashMap<String, List<CommonDictionary>>();
		List<CDictionary> dictList = iDictionaryDAO.list(null, null, null, null, null, null, null);
		System.out.println(dictList.size());
		for (CDictionary dictionary : dictList) {
			List<CommonDictionary> commonList = map.get(dictionary.getType());
			if (commonList == null) {
				commonList = new ArrayList<CommonDictionary>();
				CommonDictionary commonDictionary = convertDictionaryToCommonDictionary(dictionary);
				commonList.add(commonDictionary);
			} else {
				CommonDictionary commonDictionary = convertDictionaryToCommonDictionary(dictionary);
				commonList.add(commonDictionary);
			}
			//====================
			if (dictionary.getType() == null) continue;
			//====================
			map.put(dictionary.getType(), commonList);
		}
		return map;
	}

	private CommonDictionary convertDictionaryToCommonDictionary(CDictionary dict) {
		if (dict == null) {
			return null;
		} else {
			return new CommonDictionary(dict.getNo(), dict.getCode(), dict.getName());
		}
	}

	public SingleDictionaryDetail getSingleDetail(String code , String type) {
		if(code!=null && type!=null){
			HashMap<String, Object> filter = new HashMap<>();
			filter.put("code",code);
			filter.put("type",type);
			try{
				List<CDictionary> dictList = iDictionaryDAO.list(filter, null, null, null, null, null, null);
				if(dictList!=null&&dictList.size()>0){
					CDictionary cDictionary = dictList.get(0);
					SingleDictionaryDetail singleDictionaryDetail= new SingleDictionaryDetail();
					singleDictionaryDetail.setNo(cDictionary.getNo());
					singleDictionaryDetail.setName(cDictionary.getName());
					singleDictionaryDetail.setCode(cDictionary.getCode());
					singleDictionaryDetail.setType(cDictionary.getType());
					return singleDictionaryDetail;
				}else{
					return null;
				}
			}catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}else{
            return null;

		}
	}

	public SingleDictionaryDetail getCodeSingle(String name , String type) {
		if(name!=null && type!=null){
			HashMap<String, Object> filter = new HashMap<>();
			filter.put("name",name);
			filter.put("type",type);
			try{
				List<CDictionary> dictList = iDictionaryDAO.list(filter, null, null, null, null, null, null);
				if(dictList!=null&&dictList.size()>0){
					CDictionary cDictionary = dictList.get(0);
					SingleDictionaryDetail singleDictionaryDetail= new SingleDictionaryDetail();
					singleDictionaryDetail.setNo(cDictionary.getNo());
					singleDictionaryDetail.setName(cDictionary.getName());
					singleDictionaryDetail.setCode(cDictionary.getCode());
					singleDictionaryDetail.setType(cDictionary.getType());
					return singleDictionaryDetail;
				}else{
					return null;
				}
			}catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}else{
			return null;

		}
	}

	@Override
	public List<CommonDictionary> getDictionaryByType(String type) {

		return new ArrayList<>(CollectionUtils.flatmap(DAOBuilder.instance(this.iDictionaryDAO).searchFilter(new HashMap<String, Object>() {{

			this.put("type",type);

		}}).getList(new DAOBuilder.ErrorHandler() {
			@Override
			public void error(Exception e) { throw new ErrorException(e);}

		}), new CollectionUtils.Transformer<CDictionary, CommonDictionary>() {
			@Override
			public CommonDictionary tansform(CDictionary var1) {
				CommonDictionary dictionary = new CommonDictionary();

				dictionary.setCode(var1.getCode());
				dictionary.setName(var1.getName());
				dictionary.setNo(var1.getNo());
				return dictionary;
			}
		}));
	}
}
