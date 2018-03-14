package com.handge.housingfund.others.service;
import com.handge.housingfund.common.service.others.IDistrictService;
import com.handge.housingfund.common.service.others.model.CommonDistrict;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.database.dao.IDistrictDAO;
import com.handge.housingfund.database.entities.CDistrict;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sjw on 2017/10/11.
 */
public class DistrictImpl implements IDistrictService {
    @Autowired
    private IDistrictDAO iDistrictDAO;

    @Override
    public HashMap<String, List<CommonDistrict>> getDistrictInfo(String name) {
        return null;
    }
    @Override
    public HashMap <String, List<CommonDistrict>> getProvinceList() {
        HashMap<String, List<CommonDistrict>> map = new HashMap<>();
        System.out.println("获取省列表详情");
        map = getCommonList(null);
        return map;
    }

    @Override
    public HashMap<String, List<CommonDistrict>> getCityList(String parent) {
        HashMap<String, List<CommonDistrict>> map = new HashMap<>();
        System.out.println("获取市列表详情");
        map = getCommonList(parent);
        return map;
    }

    @Override
    public HashMap<String, List<CommonDistrict>> getAreaList(String parent) {
        HashMap<String, List<CommonDistrict>> map = new HashMap<>();
        System.out.println("获取区/县列表详情");
        map = getCommonList(parent);
        return map;
    }
    public HashMap<String, List<CommonDistrict>> getCommonList(String parent) {
        HashMap<String, List<CommonDistrict>> map = new HashMap<>();
        HashMap<String, Object> filters = new HashMap<>();
        filters.put("parent",parent);
        List<CDistrict> DistrictList = iDistrictDAO.list(filters, null, null, null, null, null, null);
        for (CDistrict list:DistrictList) {
            List<CommonDistrict> commonList = map.get(list.getId());
            if (commonList == null) {
                commonList = new ArrayList<CommonDistrict>();
                CommonDistrict CommonDistrict = getDistrictList(list);
                commonList.add(CommonDistrict);
            } else {
                CommonDistrict CommonDistrict = getDistrictList(list);
                commonList.add(CommonDistrict);
            }
            map.put(list.getId(), commonList);
        }
        return map;
    }
    private CommonDistrict getDistrictList(CDistrict cDistrict) {
        if (cDistrict == null) {
            return null;
        } else {
            CommonDistrict commonDistrict = new CommonDistrict(cDistrict.getId(), cDistrict.getParent(), cDistrict.getName());
            return commonDistrict;
        }
    }
    /*
     *  获取全名（省+市+区）
     */

    public CommonDistrict getDistrictFullName(String id) {
        String fullName = "";
        CDistrict  AreaInfo= DAOBuilder.instance(this.iDistrictDAO).searchFilter(new HashMap<String,Object>(){{
            this.put("id",id);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) { throw new ErrorException(e);}
        });
        if (AreaInfo!=null&&AreaInfo.getParent()!=null){
            CDistrict  CityInfo= DAOBuilder.instance(this.iDistrictDAO).searchFilter(new HashMap<String,Object>(){{
                this.put("id",AreaInfo.getParent());
            }}).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) { throw new ErrorException(e);}
            });
            if(CityInfo!=null&&CityInfo.getParent()!=null&&!CityInfo.getName().equals("北京市市辖区")&&!CityInfo.getName().equals("天津市市辖区")&&!CityInfo.getName().equals("上海市市辖区")&&!CityInfo.getName().equals("重庆市市辖区")){
                CDistrict  ProvinceInfo= DAOBuilder.instance(this.iDistrictDAO).searchFilter(new HashMap<String,Object>(){{
                    this.put("id",CityInfo.getParent());
                }}).getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) { throw new ErrorException(e);}
                });
                if(ProvinceInfo!=null){
                    fullName = ProvinceInfo.getName()+""+CityInfo.getName()+""+AreaInfo.getName();
                    CommonDistrict commonDistrict = new CommonDistrict(AreaInfo.getId(), AreaInfo.getParent(), fullName);
                    return commonDistrict;
                }
            }
            if(CityInfo!=null){
                fullName = CityInfo.getName()+""+AreaInfo.getName();
                CommonDistrict commonDistrict = new CommonDistrict(AreaInfo.getId(), AreaInfo.getParent(), fullName);
                return commonDistrict;
            }
        }else{
            if(AreaInfo!=null){
                CommonDistrict commonDistrict = new CommonDistrict(AreaInfo.getId(), AreaInfo.getParent(), AreaInfo.getName());
                return commonDistrict;
            }
        }
        return null;
    }
    /*
     *  获取名字
     */
    public CommonDistrict getDistrictName(String id,String name) {
        if(id!=null){
            CDistrict  district= DAOBuilder.instance(this.iDistrictDAO).searchFilter(new HashMap<String,Object>(){{
                this.put("id",id);
            }}).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) { throw new ErrorException(e);}
            });
            if(district!=null){
                CommonDistrict commonDistrict = new CommonDistrict(district.getId(), district.getParent(), district.getName());
                return commonDistrict;
            }
        }
        if(name!=null) {
            CDistrict  district= DAOBuilder.instance(this.iDistrictDAO).searchFilter(new HashMap<String,Object>(){{
                this.put("name",name);
            }}).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) { throw new ErrorException(e);}
            });
            if(district!=null){
                CommonDistrict commonDistrict = new CommonDistrict(district.getId(), district.getParent(), district.getName());
                return commonDistrict;
            }

        }
        if(id==null&&name==null){
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "查询缴存地");
        }
        return null;
    }
}
