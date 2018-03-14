package com.handge.housingfund.account.service.permission;

import com.handge.housingfund.common.service.account.AccountRpcService;
import com.handge.housingfund.common.service.account.IPermissionService;
import com.handge.housingfund.common.service.account.model.*;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnCode;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.ListDeleted;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gxy on 17-7-19.
 */
@Component
public class PermissionServiceImpl implements IPermissionService {
    @Autowired
    ICAuthDAO iAuthDAO;
    @Autowired
    ICRoleDAO iRoleDAO;
    @Autowired
    ICPermissionDAO iPermissionDAO;
    @Autowired
    ICAccountNetworkDAO iAccountNetworkDAO;
    @Autowired
    ICAccountDepartmentDAO iAccountDepartmentDAO;
    @Autowired
    ICAccountEmployeeDAO iAccountEmployeeDAO;
    @Autowired
    ICAccountClassifyDAO iAccountClassifyDAO;
    @Autowired
    @Qualifier(value = "accountRpcServiceImpl")
    AccountRpcService rpcService;

    /**
     * 获取角色列表
     *
     * @param roleName 角色名
     * @param pageNo   页码
     * @param pageSize 条数
     * @return 角色列表
     */
    public RoleWithAuthList getRoleList(String roleName, int pageNo, int pageSize) {

        HashMap<String, Object> filter = null;
        SearchOption refined = null;
        if (StringUtil.notEmpty(roleName)) {
            filter = new HashMap<>();
            filter.put("role_name", roleName);
            refined = SearchOption.FUZZY;
        }

        PageResults<CRole> results = iRoleDAO.listWithPage(filter, null, null, null, null, null, refined, pageNo,
                pageSize);
        List<CRole> roles = results.getResults();
        // System.out.println(roles.get(0));
        RoleWithAuthList roleWithAuthList = new RoleWithAuthList();
        roleWithAuthList.setCurrentPage(results.getCurrentPage());
        roleWithAuthList.setNextPageNo(results.getPageNo());
        roleWithAuthList.setPageCount(results.getPageCount());
        roleWithAuthList.setPageSize(results.getPageSize());
        roleWithAuthList.setTotalCount(results.getTotalCount());
        List<RoleWithAuth> roleWithAuths = new ArrayList<>();
        for (CRole role : roles) {
            RoleRes roleRes = new RoleRes();
            RoleWithAuth roleWithAuth = new RoleWithAuth();
            roleRes.setId(role.getId());
            roleRes.setRoleName(role.getRole_name());
            roleRes.setRoleNote(role.getRole_note());
            roleWithAuth.setRole(roleRes);
            for (CAuth auth : role.getAuths()) {
                Auth auth1 = new Auth();
                if (auth != null) {
                    CAccountEmployee employee = iAccountEmployeeDAO.get(auth.getUser_id());
                    if (employee != null) {
                        auth1.setUserId(employee.getId());
                        auth1.setUserName(employee.getXingMing());
                        roleWithAuth.getAuths().add(auth1);
                    }
                }
            }
            roleWithAuths.add(roleWithAuth);
        }
        roleWithAuthList.setRoles(roleWithAuths);
        return roleWithAuthList;
    }

    /**
     * 获取角色详情
     *
     * @param id 角色id
     * @return 角色详情
     */
    public RoleWithAuth getRoleDetail(String id) {
        CRole role = iRoleDAO.get(id);
        if (role == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "角色不存在");
        RoleRes roleRes = new RoleRes();
        roleRes.setRoleName(role.getRole_name());
        roleRes.setRoleNote(role.getRole_note());
        roleRes.setId(role.getId());
        RoleWithAuth roleWithAuth = new RoleWithAuth();
        roleWithAuth.setRole(roleRes);
        for (CPermission permission : role.getPermissions()) {
            RolePermission rolePermission = new RolePermission();
            if (permission != null) {
                rolePermission.setId(permission.getId());
                rolePermission.setPermissionName(permission.getPermission_name());
                rolePermission.setPermissionCode(permission.getPermission_code());
                roleWithAuth.getRolePermissions().add(rolePermission);
            }
        }
        return roleWithAuth;
    }

    /**
     * 添加角色
     *
     * @param roleReq 新角色
     */
    public boolean addRole(Role roleReq) {
        ArrayList<Exception> exceptions = new ArrayList<>();

        CRole role = new CRole();
        role.setRole_name(roleReq.getRole_name());
        role.setRole_note(roleReq.getRole_note());

        DAOBuilder.instance(iRoleDAO).entity(role).save(e -> exceptions.add(e));

        return exceptions.size() <= 0;
    }

    /**
     * 修改角色信息
     *
     * @param id      角色id
     * @param roleReq 修改的角色信息
     */
    public boolean putRole(String id, Role roleReq) {
        ArrayList<Exception> exceptions = new ArrayList<>();

        DAOBuilder<CRole, ICRoleDAO> builder = DAOBuilder.instance(iRoleDAO);
        CRole role = builder.UID(id).getObject(e -> exceptions.add(e));
        role.setRole_name(roleReq.getRole_name());
        role.setRole_note(roleReq.getRole_note());
        builder.entity(role).save(e -> exceptions.add(e));

        return exceptions.size() <= 0;
    }

    /**
     * 删除角色
     *
     * @param id 角色id
     */
    public boolean delRole(String id) {
        ArrayList<Exception> exceptions = new ArrayList<>();

        DAOBuilder<CRole, ICRoleDAO> builder = DAOBuilder.instance(iRoleDAO);
        CRole role = builder.UID(id).getObject(e -> {
            exceptions.add(e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        });
        role.setAuths(null);
        builder.entity(role).delete(e -> exceptions.add(e));


        // // 删除redis中的相关权限信息
        // try {
        // RedisUtil.del("Permisssion_" + id);
        // for (CAuth cAuth : role.getAuths()) {
        // RedisUtil.del("Permisssion_" + cAuth.getUser_id());
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // exceptions.add(e);
        // TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        // }

        return exceptions.size() <= 0;
    }

    /**
     * 获取未分配角色的员工列表
     *
     * @param name
     * @param id   角色ID
     * @return 未分配角色的员工列表
     */

    public List<EmployeeWithDepartment> getRoleEmployeeList(String name, String id) {
        CRole role = iRoleDAO.get(id);
        if (role == null)
            throw new ErrorException(ReturnEnumeration.Data_MISS, "角色不存在");
        List<EmployeeWithDepartment> res = new ArrayList<>();
        List<CAccountEmployee> cAccountEmployees = new ArrayList<>();// 该角色下所有员工
        List<CAccountEmployee> cAccountEmployeeList = iAccountEmployeeDAO.list(null, null, null, null, null, ListDeleted.NOTDELETED, SearchOption.REFINED);
        for (CAuth auth : role.getAuths()) {
            CAccountEmployee employee = searchCAccountEmployee(cAccountEmployeeList, auth.getUser_id());
            if (employee != null) {
                cAccountEmployees.add(employee);
            }
        }
        HashMap<String, Object> filter = new HashMap<>();
        if (StringUtil.notEmpty(name)) {
            filter.put("xingMing", name);
        }
        // filter.put("cAccountDepartment.deleted", false);
        // filter.put("cAccountNetwork.deleted", false);
        List<CAccountEmployee> employees = iAccountEmployeeDAO.list(filter, null, null, null, null,
                ListDeleted.NOTDELETED, SearchOption.FUZZY);
        HashMap<String, Object> filter1 = new HashMap<>();
        filter1.put("type", 2);
        List<CAuth> auths = iAuthDAO.list(filter1, null, null, null, null, ListDeleted.NOTDELETED,
                SearchOption.REFINED);

        for (CAccountEmployee cAccountEmployee : employees) {
            CAuth a = searchCAuth(cAccountEmployee.getId(), auths);
            if (a == null || !a.getState()) {
                continue;
            }
            boolean isfind = false;
            for (CAccountEmployee employee : cAccountEmployees) {
                if (employee.getId().equals(cAccountEmployee.getId())) {
                    isfind = true;
                    break;
                }
            }
            EmployeeWithDepartment employee1 = new EmployeeWithDepartment();// 用于输出的员工对象
            employee1.setId(cAccountEmployee.getId());
            employee1.setLxdh(cAccountEmployee.getLxdh());
            employee1.setQqh(cAccountEmployee.getQqh());
            employee1.setRzsj(cAccountEmployee.getRzsj());
            employee1.setWxh(cAccountEmployee.getWxh());
            employee1.setXingBie(cAccountEmployee.getXingBie());
            employee1.setXingMing(cAccountEmployee.getXingMing());
            employee1.setXueLi(cAccountEmployee.getXueLi());
            employee1.setYhtx(cAccountEmployee.getYhtx());
            employee1.setCreated_at(cAccountEmployee.getCreated_at());
            employee1.setZhangHao(cAccountEmployee.getZhangHao());
            CAccountNetwork network = cAccountEmployee.getcAccountNetwork();
            Network cAccountNetwork = new Network();
            if (network != null) {
                if (network.isDeleted()) {
                    continue;
                }
                cAccountNetwork.setId(network.getId());
                cAccountNetwork.setMingCheng(network.getMingCheng());
            } else {
                cAccountNetwork = null;
            }
            employee1.setcAccountNetwork(cAccountNetwork);
            CAccountDepartment department = cAccountEmployee.getcAccountDepartment();
            Department cAccountDepartment = new Department();
            if (department != null) {
                if (department.isDeleted()) {
                    continue;
                }
                cAccountDepartment.setId(department.getId());
                cAccountDepartment.setMingCheng(department.getMingCheng());
            } else {
                cAccountDepartment = null;
            }
            employee1.setcAccountDepartment(cAccountDepartment);

            if (isfind) {
                employee1.setYouXiang("1");// 用于标记该员工是否是当前角色1为是，0为否
                res.add(employee1);
                continue;
            }

            CAuth auth = searchCAuth(employee1.getId(), auths);
            if (auth != null && auth.getRoles().size() != 0) {

            } else {
                employee1.setYouXiang("0");// 用于标记该员工是否是当前角色1为是，0为否
                res.add(employee1);
            }
        }

        // List<CAccountNetwork> cAccountNetworks =
        // iAccountNetworkDAO.list(null, null, null, null,
        // null, ListDeleted.NOTDELETED, SearchOption.REFINED);
        //
        // List<CAccountNetwork> networks = new ArrayList<>();
        // for (int j = 0; j < cAccountNetworks.size(); j++) {
        // CAccountNetwork cAccountNetwork = cAccountNetworks.get(j);
        //
        // List<CAccountDepartment> departments = new ArrayList<>();
        // for (CAccountDepartment cAccountDepartment :
        // cAccountNetwork.getcAccountDepartments()) {
        //
        // List<CAccountEmployee> employees = new ArrayList<>();
        // for (int i = 0; i < cAccountDepartment.getcAccountEmployees().size();
        // i++) {
        // CAccountEmployee cAccountEmployee =
        // cAccountDepartment.getcAccountEmployees().get(i);
        //// 根据name是否符合，是否是该角色下面员工，是否已经有角色三个条件判定员工是否保留
        // if (StringUtil.notEmpty(name) &&
        // !cAccountEmployee.getXingMing().contains(name)) {
        // cAccountDepartment.getcAccountEmployees().remove(i);
        // --i;
        // break;
        // }
        // boolean isfind = false;
        // for (CAccountEmployee employee : cAccountEmployees) {
        // if (employee.getId().equals(cAccountEmployee.getId())) {
        // isfind = true;
        // break;
        // }
        // }
        // CAccountEmployee employee1 = new CAccountEmployee();//用于输出的员工对象
        // employee1.setId(cAccountEmployee.getId());
        // employee1.setLxdh(cAccountEmployee.getLxdh());
        // employee1.setQqh(cAccountEmployee.getQqh());
        // employee1.setRzsj(cAccountEmployee.getRzsj());
        // employee1.setWxh(cAccountEmployee.getWxh());
        // employee1.setXingBie(cAccountEmployee.getXingBie());
        // employee1.setXingMing(cAccountEmployee.getXingMing());
        // employee1.setXueLi(cAccountEmployee.getXueLi());
        // employee1.setYhtx(cAccountEmployee.getYhtx());
        // employee1.setYouXiang(cAccountEmployee.getYouXiang());
        // employee1.setCreated_at(cAccountEmployee.getCreated_at());
        // employee1.setZhangHao(cAccountEmployee.getZhangHao());
        //
        // if (isfind) {
        // employee1.setYouXiang("1");//用于标记该员工是否是当前角色1为是，0为否
        // employees.add(employee1);
        // continue;
        // }
        // HashMap<String, Object> filter = new HashMap<>();
        // filter.put("user_id", cAccountEmployee.getId());
        // filter.put("type", 2);
        // List<CAuth> auths = iAuthDAO.list(filter, null, null, null, null,
        // ListDeleted.NOTDELETED, SearchOption.REFINED);
        // CAuth auth = auths.size() > 0 ? auths.get(0) : null;
        // if (auth != null && auth.getRoles().size() != 0) {
        // cAccountDepartment.getcAccountEmployees().remove(i);
        // i--;
        // } else {
        // employee1.setYouXiang("0");//用于标记该员工是否是当前角色1为是，0为否
        // employees.add(employee1);
        // }
        // }
        // CAccountDepartment department = new CAccountDepartment();
        // department.setFZR(cAccountDepartment.getFZR());
        // department.setLXDH(cAccountDepartment.getLXDH());
        // department.setMingCheng(cAccountDepartment.getMingCheng());
        // department.setId(cAccountDepartment.getId());
        // department.setCreated_at(cAccountDepartment.getCreated_at());
        // department.setcAccountEmployees(employees, false);
        // departments.add(department);
        // }
        //
        // CAccountNetwork network = new CAccountNetwork();
        // network.setMingCheng(cAccountNetwork.getMingCheng());
        // network.setId(cAccountNetwork.getId());
        // network.setXXDZ(cAccountNetwork.getXXDZ());
        // network.setWeiDu(cAccountNetwork.getWeiDu());
        // network.setLXDH(cAccountNetwork.getLXDH());
        // network.setJingDu(cAccountNetwork.getJingDu());
        // network.setFZR(cAccountNetwork.getFZR());
        // network.setDiQu(cAccountNetwork.getDiQu());
        // network.setBLSJ(cAccountNetwork.getBLSJ());
        // network.setSJWD(cAccountNetwork.getSJWD());
        // network.setCreated_at(cAccountNetwork.getCreated_at());
        // network.setcAccountDepartments(departments, false);
        // networks.add(network);
        // }
        return res;

    }

    /**
     * 修改角色成员
     *
     * @param id
     * @param list
     * @return
     */
    public boolean putRoleEmployee(String id, List<String> list) {
        ArrayList<Exception> exceptions = new ArrayList<>();

        DAOBuilder<CRole, ICRoleDAO> builder = DAOBuilder.instance(iRoleDAO);
        DAOBuilder<CAuth, ICAuthDAO> authBuilder = DAOBuilder.instance(iAuthDAO);

        CRole role = builder.UID(id).getObject(e -> exceptions.add(e));
        if (role == null)
            return false;

        role.getAuths().clear();
        for (String userId : list) {
            HashMap<String, Object> filter = new HashMap<>();
            if (StringUtil.notEmpty(userId)) {
                filter.put("user_id", userId);
            }
            CAuth auth = authBuilder.searchFilter(filter).getObject(e -> exceptions.add(e));
            if (auth != null) {
                role.getAuths().add(auth);
            }
        }

        builder.entity(role).save(e -> exceptions.add(e));

        // // 删除redis中之前的成员和角色对于关系
        // try {
        // for (CAuth cAuth : role.getAuths()) {
        // if (!list.contains(cAuth.getUser_id())) {
        // RedisUtil.del("Permisssion_" + cAuth.getUser_id());
        // }
        // }
        // for (String authId : list) {
        // boolean flag = false;
        // for (CAuth cAuth : role.getAuths()) {
        // if (authId.equals(cAuth.getUser_id())) {
        // flag = true;
        // }
        // }
        // if (!flag)
        // RedisUtil.del("Permisssion_" + authId);
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // exceptions.add(e);
        // TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        // }

        return exceptions.size() <= 0;
    }

    private CAccountEmployee searchCAccountEmployee(List<CAccountEmployee> employees, String id) {
        if (!StringUtil.notEmpty(id)) {
            return null;
        }
        for (CAccountEmployee employee : employees) {
            if (id.equals(employee.getId())) {
                return employee;
            }
        }
        return null;
    }

    /**
     * 获取编辑权限列表
     *
     * @return 编辑权限列表
     */
    public List<Classify> getClassifyList(String id) {
        ArrayList<Exception> exceptions = new ArrayList<>();

        HashMap<String, Object> filter = new HashMap<>();
        if (id != null) {
            filter.put("id", id);
        }

        List<Classify> classifies = new ArrayList<>();
        List<CAccountClassify> accountClassifys = DAOBuilder.instance(iAccountClassifyDAO).searchFilter(filter)
                .getList(e -> exceptions.add(e));

        for (CAccountClassify accountClassify : accountClassifys) {
            Classify classify = new Classify();
            classify.setId(accountClassify.getId());
            classify.setName(accountClassify.getClassify_name());
            for (CAccountModule accountModule : accountClassify.getcAccountModules()) {
                Module module = new Module();
                module.setId(accountModule.getId());
                module.setModuleName(accountModule.getModule_name());
                for (CPermission permission : accountModule.getcPermissions()) {
                    ModulePermission modulePermission = new ModulePermission();
                    modulePermission.setId(permission.getId());
                    modulePermission.setPermissionName(permission.getPermission_name());
                    module.getModulePermissions().add(modulePermission);
                }
                classify.getModules().add(module);
            }
            classifies.add(classify);
        }
        return exceptions.size() > 0 ? null : classifies;
    }

    /**
     * 修改角色权限
     *
     * @param id   角色id
     * @param list 权限id列表
     * @return 是否修改成功
     */
    public boolean addPermission(String id, List<String> list) {
        ArrayList<Exception> exceptions = new ArrayList<>();

        DAOBuilder<CRole, ICRoleDAO> builder = DAOBuilder.instance(iRoleDAO);
        DAOBuilder<CPermission, ICPermissionDAO> permissionbuilder = DAOBuilder.instance(iPermissionDAO);

        CRole role = builder.UID(id).getObject(e -> exceptions.add(e));
        if (role == null)
            return false;
        // builder.entity(role).delete(e -> exceptions.add(e));
        //
        // role.setDeleted(false);
        role.getPermissions().clear();

        for (String permissionId : list) {
            CPermission permission = permissionbuilder.UID(permissionId).getObject(e -> exceptions.add(e));
            role.getPermissions().add(permission);

        }

        builder.entity(role).save(e -> exceptions.add(e));

        // // 删除redis之前的角色权限关系
        // try {
        // RedisUtil.del("Permisssion_" + id);
        // } catch (Exception e) {
        // e.printStackTrace();
        // exceptions.add(e);
        // TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        // }

        return exceptions.size() <= 0;
    }

    @Override
    public List<String> getRoleIDByUserId(String userId) {
        List<String> roleIDs = new ArrayList<String>();
        HashMap<String, Object> authFilter = new HashMap<String, Object>();
        authFilter.put("user_id", userId);
        List<CAuth> cAuths = iAuthDAO.list(authFilter, null, null, null, null, null, null);
        if (!cAuths.isEmpty()) {
            CAuth cauth = cAuths.get(0);
            List<CRole> roles = cauth.getRoles();
            for (CRole cRole : roles) {
                roleIDs.add(cRole.getId());
            }
        }
        return roleIDs;
    }

    @Override
    public boolean getPermissionByUserId(String userId, int type, String MU) throws Exception {
        boolean flag = false;
        HashMap<String, Object> filter = new HashMap<>();
        filter.put("user_id", userId);
        filter.put("type", type);
        List<CAuth> auths = iAuthDAO.list(filter, null, null, null, null, ListDeleted.NOTDELETED, SearchOption.REFINED);
        CAuth auth = auths.size() > 0 ? auths.get(0) : null;
        if (auth == null) {
            return false;
        }
        List<CRole> roles = auth.getRoles();
        // for (CRole role : roles) {
        // String roleName = role.getRole_name();
        // if (RedisUtil.sismember("Permisssion_" + roleName, MU)) {
        // flag = true;
        // break;
        // } else {
        // RedisUtil.sadd("Permisssion_" + userId + "_type_" + type, roleName);
        // RedisUtil.expire(userId, 24 * 60 * 60);
        // List<CPermission> permissions = role.getPermissions();
        // for (CPermission permission : permissions) {
        // List<CAction> actions = permission.getActions();
        // for (CAction action : actions) {
        // String actionMethod = action.getAction_method();
        // String actionUrl = action.getAction_url();
        // String actMU = actionMethod + ":" + actionUrl;
        // if (MU.equals(actMU)) {
        // RedisUtil.sadd("Permisssion_" + roleName, actMU);
        // RedisUtil.expire(roleName, 24 * 60 * 60);
        // flag = true;
        // }
        // }
        // }
        // }
        // }
        return flag;
    }

    @Override
    public List<String> getUserAccessAPS(String userId, int type, String action, String path) {
        List<String> aps = new ArrayList<String>();
        HashMap<String, Object> filter = new HashMap<>();
        filter.put("user_id", userId);
        filter.put("type", type);
        List<CAuth> auths = iAuthDAO.list(filter, null, null, null, null, ListDeleted.NOTDELETED, SearchOption.REFINED);
        CAuth auth = auths.size() > 0 ? auths.get(0) : null;
        if (auth == null) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "认证信息未找到");
        }
        List<CRole> roles = auth.getRoles();
        for (CRole role : roles) {
            List<CPermission> permissions = role.getPermissions();
            for (CPermission permission : permissions) {
                List<CAction> actions = permission.getActions();
                for (CAction act : actions) {
                    String actionMethod = act.getAction_method();
                    String actionUrl = act.getAction_url();
                    aps.add(String.format("%s:%s", actionMethod, actionUrl));
                }
            }
        }
        return aps;
    }

    @Override
    public PageRes<AccountEmployeeDetail> getEmployeeList(int YeMa, int TiaoShu, String WangDian, String MingCheng,
                                                          String ZhangHao, String BuMen) {
        String withWangian = "";
        String withBumen = "";
        String withXingMing = "";
        String withZhangHao = "";
        HashMap<String, Object> param = new HashMap<>();
        if (StringUtil.notEmpty(WangDian)) {
            withWangian = " and can.MingCheng LIKE :wangdian";
            param.put("wangdian", "%"+WangDian+"%");
        }
        System.out.println(withWangian);
        if (StringUtil.notEmpty(BuMen)) {
            withBumen = " and cad.MingCheng LIKE :bumen";
            param.put("bumen", "%"+BuMen+"%");
        }
        if (StringUtil.notEmpty(MingCheng)) {
            withXingMing = " and cae.XingMing LIKE :xingming";
            param.put("xingming", "%"+MingCheng+"%");
        }
        if (StringUtil.notEmpty(ZhangHao)) {
            withZhangHao = " and cae.ZhangHao LIKE :zhanghao";
            param.put("zhanghao", "%"+ZhangHao+"%");
        }
        param.put("type", 2);
        String sql = "SELECT" +
                " cae.id as id," +
                "cae.LXDH as LXDH," +
                "cae.QQH as QQH," +
                "cae.RZSJ as RZSJ," +
                "cae.WXH as WXH," +
                "cae.XingBie as XingBie," +
                "cae.XingMing as XingMing," +
                "cae.XueLi as XueLi," +
                "cae.YHTX as YHTK," +
                "cae.YouXiang as YouXiang," +
                "cae.ZhangHao as zhanghao," +
                "can.MingCheng as WangDian," +
                "cad.MingCheng as BuMen," +
                "cr.role_name as JueSe," +
                "ca.state as state" +
                " FROM" +
                " c_account_employee cae" +
                " INNER JOIN c_account_network can ON cae.cAccountNetwork_id = can.id" +
                withWangian +
                " left JOIN c_account_department cad ON cae.cAccountDepartment_id = cad.id" +
                withBumen +
                " INNER JOIN c_auth ca ON cae.id = ca.user_id" +
                " AND ca.type = :type" +
                " INNER JOIN c_auth_role car ON ca.id = car.auth_id" +
                " INNER JOIN c_role cr ON cr.id = car.role_id" +
                " WHERE cae.deleted=0" +
                withZhangHao +
                withXingMing +
                " ORDER BY cae.ZhangHao";
        String countSQL = "SELECT" +
                " count(*)" +
                " FROM" +
                " c_account_employee cae" +
                " INNER JOIN c_account_network can ON cae.cAccountNetwork_id = can.id" +
                withWangian +
                " INNER JOIN c_account_department cad ON cae.cAccountDepartment_id = cad.id" +
                withBumen +
                " INNER JOIN c_auth ca ON cae.id = ca.user_id" +
                " AND ca.type = :type" +
                " INNER JOIN c_auth_role car ON ca.id = car.auth_id" +
                " INNER JOIN c_role cr ON cr.id = car.role_id" +
                " WHERE cae.deleted=0" +
                withZhangHao +
                withXingMing;

        PageResults<Object[]> employees = iAccountEmployeeDAO.listWithPage(sql, param, countSQL, param, YeMa, TiaoShu);
        PageRes<AccountEmployeeDetail> pageRes = new PageRes<>();
        pageRes.setTotalCount(employees.getTotalCount());
        pageRes.setPageSize(employees.getPageSize());
        pageRes.setPageCount(employees.getPageCount());
        pageRes.setNextPageNo(employees.getPageNo());
        pageRes.setCurrentPage(employees.getCurrentPage());
        pageRes.setResults(fromObjectList(employees.getResults()));
        return pageRes;
    }

    private CAuth searchCAuth(String id, List<CAuth> auths) {
        for (CAuth auth : auths) {
            if (id.equals(auth.getUser_id())) {
                return auth;
            }
        }
        return null;
    }

    @Override
    public AccountEmployeeDetail getEmployeeDetail(String id) {
        AccountEmployeeDetail accountEmployeeDetail = new AccountEmployeeDetail();
        CAccountEmployee employee = iAccountEmployeeDAO.get(id);
        if (employee != null) {
            accountEmployeeDetail.setId(employee.getId());
            accountEmployeeDetail.setLXDH(employee.getLxdh());
            accountEmployeeDetail.setQQH(employee.getQqh());
            accountEmployeeDetail.setRZSJ((employee.getRzsj() != null) ? employee.getRzsj().getTime() : 0);
            accountEmployeeDetail.setWXH(employee.getWxh());
            accountEmployeeDetail.setXingBie(employee.getXingBie());
            accountEmployeeDetail.setXingMing(employee.getXingMing());
            accountEmployeeDetail.setXueLi(employee.getXueLi());
            accountEmployeeDetail.setYHTX(employee.getYhtx());
            accountEmployeeDetail.setYouXiang(employee.getYouXiang());
            accountEmployeeDetail.setZhangaho(employee.getZhangHao());
            if (employee.getcAccountNetwork() == null) {
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "该员工没有网点信息");
            }
            accountEmployeeDetail.setWangDian(employee.getcAccountNetwork().getId());
            accountEmployeeDetail.setBuMen(employee.getcAccountDepartment() != null ? employee.getcAccountDepartment().getId() : "");
            HashMap<String, Object> filter = new HashMap<>();
            filter.put("type", 2);
            filter.put("user_id", employee.getId());
            List<CAuth> auths = iAuthDAO.list(filter, null, null, null, null, ListDeleted.NOTDELETED,
                    SearchOption.REFINED);
            CAuth auth = auths.size() > 0 ? auths.get(0) : null;
            if (auth != null) {
                accountEmployeeDetail.setState(auth.getState() ? "1" : "0");
                accountEmployeeDetail.setJueSe(auth.getRoles().size() > 0 ? auth.getRoles().get(0).getId() : null);
            } else {
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "该员工没有认证信息");
            }
        } else {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "该员工信息未找到");
        }
        return accountEmployeeDetail;
    }

    @Override
    public AccountEmployeeDetail addEmployee(Employee employee) {
        CAccountEmployee accountEmployee = new CAccountEmployee();
        CAccountNetwork accountNetwork = iAccountNetworkDAO.get(employee.getWangDian());
        if (accountNetwork == null) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "网点信息未找到");
        }
        accountEmployee.setcAccountNetwork(accountNetwork);
        CAccountDepartment department = iAccountDepartmentDAO.get(employee.getBuMen());
        if (department != null && !department.getcAccountNetwork().getId().equals(accountNetwork.getId())) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "该员工部门和网点信息不匹配");
        }
        accountEmployee.setcAccountDepartment(department);
        accountEmployee.setLxdh(employee.getLXDH());
        accountEmployee.setQqh(employee.getQQH());
        if (employee.getRZSJ() != 0) {
            try {
                accountEmployee.setRzsj(new Date(employee.getRZSJ()));
            } catch (Exception e) {
                throw new ErrorException(e);
            }
        } else {
            accountEmployee.setRzsj(null);
        }
        accountEmployee.setWxh(employee.getWXH());
        accountEmployee.setXingBie(employee.getXingBie());
        accountEmployee.setXingMing(employee.getXingMing());
        accountEmployee.setXueLi(employee.getXueLi());
        accountEmployee.setYhtx(employee.getYHTX());
        accountEmployee.setYouXiang(employee.getYouXiang());
        String id = iAccountEmployeeDAO.save(accountEmployee);
        CAccountEmployee cAccountEmployee = iAccountEmployeeDAO.get(id);

        RpcAuth rpcAuth = new RpcAuth();
        rpcAuth.setEmail(cAccountEmployee.getYouXiang());
        rpcAuth.setPassword(null);
        rpcAuth.setRole_id(employee.getJueSe());
        rpcAuth.setType(2);
        rpcAuth.setUser_id(cAccountEmployee.getId());
        rpcAuth.setUsername(cAccountEmployee.getZhangHao());
        rpcAuth.setState(Integer.parseInt(employee.getState()));
        Msg rpcMsg = rpcService.registerAuth(rpcAuth);

        AccountEmployeeDetail accountEmployeeDetail = new AccountEmployeeDetail();

        accountEmployeeDetail.setId(cAccountEmployee.getId());
        accountEmployeeDetail.setLXDH(cAccountEmployee.getLxdh());
        accountEmployeeDetail.setQQH(cAccountEmployee.getQqh());
        accountEmployeeDetail.setRZSJ((cAccountEmployee.getRzsj() != null) ? cAccountEmployee.getRzsj().getTime() : 0);
        accountEmployeeDetail.setWXH(cAccountEmployee.getWxh());
        accountEmployeeDetail.setXingBie(cAccountEmployee.getXingBie());
        accountEmployeeDetail.setXingMing(cAccountEmployee.getXingMing());
        accountEmployeeDetail.setXueLi(cAccountEmployee.getXueLi());
        accountEmployeeDetail.setYHTX(cAccountEmployee.getYhtx());
        accountEmployeeDetail.setYouXiang(cAccountEmployee.getYouXiang());
        accountEmployeeDetail.setZhangaho(cAccountEmployee.getZhangHao());
        if (rpcMsg.getCode() == ReturnCode.Success) {
            return accountEmployeeDetail;
        } else {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "该员工认证信息注册失败");
        }

    }

    @Override
    public String putEmployee(String id, Employee employee) {
        CAccountEmployee accountEmployee = iAccountEmployeeDAO.get(id);
        CAccountNetwork accountNetwork = iAccountNetworkDAO.get(employee.getWangDian());
        if (accountNetwork == null) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "网点信息未找到");
        }
        accountEmployee.setcAccountNetwork(accountNetwork);
        CAccountDepartment department = iAccountDepartmentDAO.get(employee.getBuMen());
        if (!department.getcAccountNetwork().getId().equals(accountNetwork.getId())) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "该员工部门和网点信息不匹配");
        }
        accountEmployee.setcAccountDepartment(department);
        accountEmployee.setLxdh(employee.getLXDH());
        accountEmployee.setQqh(employee.getQQH());
        accountEmployee.setRzsj(new Date(employee.getRZSJ()));
        accountEmployee.setWxh(employee.getWXH());
        accountEmployee.setXingBie(employee.getXingBie());
        accountEmployee.setXingMing(employee.getXingMing());
        accountEmployee.setXueLi(employee.getXueLi());
        accountEmployee.setYhtx(employee.getYHTX());
        accountEmployee.setYouXiang(employee.getYouXiang());
        iAccountEmployeeDAO.update(accountEmployee);
        RpcAuth rpcAuth = new RpcAuth();
        rpcAuth.setEmail(employee.getYouXiang());
        rpcAuth.setPassword(null);
        rpcAuth.setRole_id(employee.getJueSe());
        rpcAuth.setType(2);
        try {
            rpcAuth.setState(Integer.parseInt(employee.getState()));
        } catch (Exception e) {
            throw new ErrorException(e);
        }
        rpcAuth.setUser_id(accountEmployee.getId());
        rpcAuth.setUsername(accountEmployee.getZhangHao());
        Msg rpcMsg = rpcService.updateAuth(accountEmployee.getId(), rpcAuth);
        if (rpcMsg.getCode() == ReturnCode.Success) {
            return accountEmployee.getId();
        } else {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, rpcMsg.getMsg());
        }
    }

    @Override
    public Msg delEmployee(List<String> list) {
        Msg msg = new Msg();
        for (int i = 0; i < list.size(); i++) {
            CAccountEmployee employee = iAccountEmployeeDAO.get(list.get(i));
            HashMap<String, Object> filter = new HashMap<>();
            filter.put("user_id", employee.getId());
            filter.put("type", 2);
            List<CAuth> auths = iAuthDAO.list(filter, null, null, null, null, ListDeleted.NOTDELETED,
                    SearchOption.REFINED);
            CAuth auth = auths.size() > 0 ? auths.get(0) : null;
            if (auth != null) {
                auth.setRoles(new ArrayList<>());
                iAuthDAO.update(auth);
                iAuthDAO.delete(auth);
                iAccountEmployeeDAO.delete(employee);
            }
        }
        msg.setMsg("认证表删除成功");
        msg.setCode(ReturnCode.Success);
        return msg;
    }

    public AccountEmployeeDetail fromObject(Object[] rs) {
        AccountEmployeeDetail accountEmployeeDetail = new AccountEmployeeDetail();
        accountEmployeeDetail.setId((String) rs[0]);
        accountEmployeeDetail.setLXDH((String) rs[1]);
        accountEmployeeDetail.setQQH((String) rs[2]);
        if (rs[3] != null) {
            accountEmployeeDetail.setRZSJ(((java.sql.Timestamp) rs[3]).getTime());
        }
        accountEmployeeDetail.setWXH((String) rs[4]);
        accountEmployeeDetail.setXingBie((String) rs[5]);
        accountEmployeeDetail.setXingMing((String) rs[6]);
        accountEmployeeDetail.setXueLi((String) rs[7]);
        accountEmployeeDetail.setYHTX((String) rs[8]);
        accountEmployeeDetail.setYouXiang((String) rs[9]);
        accountEmployeeDetail.setZhangaho((String) rs[10]);
        accountEmployeeDetail.setWangDian((String) rs[11]);
        accountEmployeeDetail.setBuMen((String) rs[12]);
        accountEmployeeDetail.setJueSe((String) rs[13]);
        accountEmployeeDetail.setState(String.valueOf(rs[14]));
        return accountEmployeeDetail;
    }

    public ArrayList<AccountEmployeeDetail> fromObjectList(List<Object[]> rsList) {
        ArrayList<AccountEmployeeDetail> accountEmployeeDetailList = new ArrayList<AccountEmployeeDetail>();
        for (Object[] rs : rsList) {
            accountEmployeeDetailList.add(fromObject(rs));
        }
        return accountEmployeeDetailList;
    }

}
