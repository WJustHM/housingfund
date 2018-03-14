package com.handge.housingfund.common.service.others;

import com.handge.housingfund.common.service.others.model.CommonDistrict;

import java.util.HashMap;
import java.util.List;

/**
 * Created by sjw on 2017/10/11.
 */
public interface IDistrictService {
    /**
     * 获取缴存地列表
     * @return
     */
    public HashMap<String, List<CommonDistrict>> getDistrictInfo(String name);

    /**
     * 获取省列表
     * @return
     */
    public HashMap <String, List<CommonDistrict>> getProvinceList();

    /**
     * 获取市列表
     * @param parent
     * @return
     */
    public HashMap<String, List<CommonDistrict>> getCityList(String parent);

    /**
     * 获取区/县列表
     * @param parent
     * @return
     */
    public HashMap<String, List<CommonDistrict>> getAreaList(String parent);
    /**
     *  获取全名（省+市+区）
     */
    public CommonDistrict getDistrictFullName(String id);
    /**
     *  获取名字
     */
    public CommonDistrict getDistrictName(String id,String name);
}
