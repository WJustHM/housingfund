package com.handge.housingfund.account.service.network;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.INetworkManagementService;
import com.handge.housingfund.common.service.account.IPermissionService;
import com.handge.housingfund.common.service.account.model.*;
import com.handge.housingfund.common.service.util.CollectionUtils;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.ICAccountDepartmentDAO;
import com.handge.housingfund.database.dao.ICAccountEmployeeDAO;
import com.handge.housingfund.database.dao.ICAccountNetworkDAO;
import com.handge.housingfund.database.dao.ICStateMachineConfigurationDAO;
import com.handge.housingfund.database.entities.CAccountDepartment;
import com.handge.housingfund.database.entities.CAccountEmployee;
import com.handge.housingfund.database.entities.CAccountNetwork;
import com.handge.housingfund.database.entities.CStateMachineConfiguration;
import com.handge.housingfund.database.enums.BusinessType;
import com.handge.housingfund.database.enums.ListDeleted;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.database.enums.TransitionKind;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by tanyi on 2017/7/19.
 */
@Component
public class NetWorkManagementServiceImpl implements INetworkManagementService {

    @Autowired
    ICAccountNetworkDAO iAccountNetworkDAO;
    @Autowired
    ICAccountDepartmentDAO iAccountDepartmentDAO;
    @Autowired
    ICAccountEmployeeDAO iAccountEmployeeDAO;
    @Autowired
    @Qualifier(value = "permissionService")
    IPermissionService permissionService;

    @Autowired
    protected ICStateMachineConfigurationDAO icStateMachineConfigurationDAO;

    @Override
    public PageRes<Network> getNetworkList(int pageNo, int pageSize, String MingCheng) {

        PageRes<Network> pageRes = new PageRes<>();

        List<CAccountNetwork> cAccountNetworks = DAOBuilder.instance(iAccountNetworkDAO).searchFilter(new HashMap<String, Object>() {
            {
                if (StringUtil.notEmpty(MingCheng)) {
                    this.put("MingCheng", MingCheng);
                }
            }
        }).pageOption(pageRes, pageSize, pageNo).searchOption(SearchOption.FUZZY).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        ArrayList<Network> networks = new ArrayList<>();
        for (int i = 0; i < cAccountNetworks.size(); i++) {
            Network network = new Network();
            CAccountNetwork cAccountNetwork = cAccountNetworks.get(i);
            if (cAccountNetwork.getId().equals("0")) {
                continue;
            }
            network.setBLSJ(cAccountNetwork.getBLSJ());
            network.setDiQu(cAccountNetwork.getDiQu());
            network.setFZR(cAccountNetwork.getFZR());
            network.setId(cAccountNetwork.getId());
            network.setJingDu(cAccountNetwork.getJingDu());
            network.setLXDH(cAccountNetwork.getLXDH());
            network.setMingCheng(cAccountNetwork.getMingCheng());
            network.setSJWD(cAccountNetwork.getSJWD());
            network.setWeiDu(cAccountNetwork.getWeiDu());
            network.setXXDZ(cAccountNetwork.getXXDZ());
            networks.add(network);
        }
        pageRes.setResults(networks);
        pageRes.setTotalCount(pageRes.getTotalCount() - 1);
        return pageRes;
    }

    @Override
    public NetworkWithDepartment getNetworkDetail(String id) {
        CAccountNetwork cAccountNetwork = iAccountNetworkDAO.get(id);
        NetworkWithDepartment network = new NetworkWithDepartment();
        network.setBLSJ(cAccountNetwork.getBLSJ());
        network.setXXDZ(cAccountNetwork.getXXDZ());
        network.setWeiDu(cAccountNetwork.getWeiDu());
        network.setSJWD(cAccountNetwork.getSJWD());
        network.setMingCheng(cAccountNetwork.getMingCheng());
        network.setLXDH(cAccountNetwork.getLXDH());
        network.setJingDu(cAccountNetwork.getJingDu());
        network.setId(cAccountNetwork.getId());
        network.setFZR(cAccountNetwork.getFZR());
        network.setDiQu(cAccountNetwork.getDiQu());
        List<DepartmentWithNetwork> departments = new ArrayList<>();
        for (int i = 0; i < cAccountNetwork.getcAccountDepartments().size(); i++) {
            CAccountDepartment cAccountDepartment = cAccountNetwork.getcAccountDepartments().get(i);
            if (cAccountDepartment.isDeleted()) {
                continue;
            }
            DepartmentWithNetwork department = new DepartmentWithNetwork();
            department.setCreated_at(cAccountDepartment.getCreated_at());
            department.setId(cAccountDepartment.getId());
            department.setUpdated_at(cAccountDepartment.getUpdated_at());
            department.setMingCheng(cAccountDepartment.getMingCheng());
            department.setLXDH(cAccountDepartment.getLXDH());
            department.setFZR(cAccountDepartment.getFZR());
            department.setDeleted(cAccountDepartment.isDeleted());
            department.setDeleted_at(cAccountDepartment.getDeleted_at());
            List<Employee> employees = new ArrayList<>();
            for (int j = 0; j < cAccountDepartment.getcAccountEmployees().size(); j++) {
                CAccountEmployee cAccountEmployee = cAccountDepartment.getcAccountEmployees().get(j);
                if (cAccountEmployee.isDeleted()) {
                    continue;
                }
                Employee employee = new Employee();
                employee.setLXDH(cAccountEmployee.getLxdh());
                employee.setYouXiang(cAccountEmployee.getYouXiang());
                employee.setYHTX(cAccountEmployee.getYhtx());
                employee.setXueLi(cAccountEmployee.getXueLi());
                employee.setXingMing(cAccountEmployee.getXingMing());
                employee.setXingBie(cAccountEmployee.getXingBie());
                employee.setWXH(cAccountEmployee.getWxh());
                employee.setRZSJ(cAccountEmployee.getRzsj() != null ? cAccountEmployee.getRzsj().getTime() : 0L);
                employee.setQQH(cAccountEmployee.getQqh());
                employees.add(employee);
            }
            department.setcAccountEmployees(employees);
            departments.add(department);
        }
        network.setcAccountDepartments(departments);
        return network;
    }

    /**
     * @param network 网点信息
     * @return
     */
    @Override
    public String addNetwork(TokenContext tokenContext, Network network) {
        CAccountNetwork accountNetwork = new CAccountNetwork();
        try {
            accountNetwork.setBLSJ(network.getBLSJ());
            accountNetwork.setcAccountDepartments(null);
            accountNetwork.setcAccountEmployees(null);
            accountNetwork.setDiQu(network.getDiQu());
            accountNetwork.setFZR(network.getFZR());
            accountNetwork.setJingDu(network.getJingDu());
            accountNetwork.setLXDH(network.getLXDH());
            accountNetwork.setMingCheng(network.getMingCheng());
            accountNetwork.setWeiDu(network.getWeiDu());
            accountNetwork.setXXDZ(network.getXXDZ());
            accountNetwork.setSJWD(network.getSJWD());
            String id = iAccountNetworkDAO.save(accountNetwork);
            if (StringUtil.notEmpty(id)) {
                initStateMachineConfg(tokenContext, id);
                return id;
            } else {
                throw new ErrorException("未知数据库错误");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorException(e);
        }

    }

    /**
     * 修改网点信息
     *
     * @param id      网点ID
     * @param network 网点信息
     * @return
     */
    @Override
    public String putNetwork(TokenContext tokenContext, String id, Network network) {
        CAccountNetwork accountNetwork = iAccountNetworkDAO.get(id);
        try {
            accountNetwork.setBLSJ(network.getBLSJ());
            accountNetwork.setDiQu(network.getDiQu());
            accountNetwork.setFZR(network.getFZR());
            accountNetwork.setJingDu(network.getJingDu());
            accountNetwork.setLXDH(network.getLXDH());
            accountNetwork.setMingCheng(network.getMingCheng());
            accountNetwork.setWeiDu(network.getWeiDu());
            accountNetwork.setXXDZ(network.getXXDZ());
            accountNetwork.setSJWD(network.getSJWD());

            iAccountNetworkDAO.update(accountNetwork);
            return id;
        } catch (Exception e) {
            throw new ErrorException(e);
        }

    }

    @Override
    public List<Delres> delNetWork(List<String> ids) {
        List<Delres> delres = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            Delres delres1 = new Delres();
            delres1.setId(ids.get(i));
            delres1.setRes(true);
            CAccountNetwork accountNetwork = iAccountNetworkDAO.get(ids.get(i));
            if (accountNetwork != null) {
                List<CAccountDepartment> accountDepartments = accountNetwork.getcAccountDepartments();
                for (int j = 0; j < accountDepartments.size(); j++) {
                    CAccountDepartment accountDepartment = accountDepartments.get(j);
                    if (accountDepartment != null) {
                        List<CAccountEmployee> accountEmployees = accountDepartment.getcAccountEmployees();
                        List<String> employeeids = new ArrayList<>();
                        for (int k = 0; k < accountEmployees.size(); k++) {
                            employeeids.add(accountEmployees.get(k).getId());
                        }
                        permissionService.delEmployee(employeeids);
                        iAccountDepartmentDAO.delete(accountDepartment);
                    }
                }
                iAccountNetworkDAO.delete(accountNetwork);
                deleteStateMachineConfigUnderNetWorkStation(accountNetwork.getId());
            } else {
                delres1.setRes(false);
            }
            delres.add(delres1);
        }
        return delres;
    }

    @Override
    public List<DepartmentWithNetwork> getDepartmentList(String id) {
        HashMap<String, Object> filter = new HashMap<>();
        if (StringUtil.notEmpty(id)) {
            filter.put("cAccountNetwork.id", id);
        }
        filter.put("cAccountNetwork.deleted", false);
        filter.put("deleted", false);
        List<CAccountDepartment> cAccountDepartmentPageResults =
                iAccountDepartmentDAO.list(filter, null, null, null, null, ListDeleted.NOTDELETED, SearchOption.REFINED);
        List<DepartmentWithNetwork> res = new ArrayList<>();
        for (CAccountDepartment cAccountDepartment : cAccountDepartmentPageResults) {
            if (cAccountDepartment.isDeleted()) {
                continue;
            }
            DepartmentWithNetwork departmentWithNetwork = new DepartmentWithNetwork();
            departmentWithNetwork.setId(cAccountDepartment.getId());
            departmentWithNetwork.setUpdated_at(cAccountDepartment.getUpdated_at());
            departmentWithNetwork.setMingCheng(cAccountDepartment.getMingCheng());
            departmentWithNetwork.setLXDH(cAccountDepartment.getLXDH());
            departmentWithNetwork.setFZR(cAccountDepartment.getFZR());
            departmentWithNetwork.setDeleted_at(cAccountDepartment.getDeleted_at());
            departmentWithNetwork.setCreated_at(cAccountDepartment.getCreated_at());
            if (StringUtil.notEmpty(id)) {
                List<Employee> employees = new ArrayList<>();
                for (CAccountEmployee cAccountEmployee : cAccountDepartment.getcAccountEmployees()) {
                    if (cAccountEmployee.isDeleted()) {
                        continue;
                    }
                    AccountEmployeeDetail employee = new AccountEmployeeDetail();
                    employee.setLXDH(cAccountEmployee.getLxdh());
                    employee.setYouXiang(cAccountEmployee.getYouXiang());
                    employee.setYHTX(cAccountEmployee.getYhtx());
                    employee.setXueLi(cAccountEmployee.getXueLi());
                    employee.setXingMing(cAccountEmployee.getXingMing());
                    employee.setXingBie(cAccountEmployee.getXingBie());
                    employee.setWXH(cAccountEmployee.getWxh());
                    employee.setRZSJ(cAccountEmployee.getRzsj() != null ? cAccountEmployee.getRzsj().getTime() : 0L);
                    employee.setQQH(cAccountEmployee.getQqh());
                    employee.setId(cAccountEmployee.getId());
                    employees.add(employee);
                }
                departmentWithNetwork.setcAccountEmployees(employees);
            } else {
                CAccountNetwork network = cAccountDepartment.getcAccountNetwork();
                Network cAccountNetwork = new Network();
                cAccountNetwork.setId(network.getId());
                cAccountNetwork.setMingCheng(network.getMingCheng());
                departmentWithNetwork.setcAccountNetwork(cAccountNetwork);
            }
            res.add(departmentWithNetwork);
        }
        return res;
    }

    @Override
    public String putDepartment(Department department) {
        CAccountNetwork accountNetwork = iAccountNetworkDAO.get(department.getWangDian_id());
        if (accountNetwork == null) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "网点ID不能为空");
        }
        CAccountDepartment accountDepartment = new CAccountDepartment();
        List<CAccountEmployee> accountEmployees = new ArrayList<>();
        List<CAccountEmployee> employees = iAccountEmployeeDAO.list(null, null, null, null, null, ListDeleted.NOTDELETED, SearchOption.REFINED);
        List<String> departmentEmployees = department.getBMCY();
        if (StringUtil.notEmpty(department.getId())) {
            accountDepartment = iAccountDepartmentDAO.get(department.getId());
            if (accountDepartment == null) {
                accountDepartment = new CAccountDepartment();
            }
            accountEmployees = accountDepartment.getcAccountEmployees();
            if (accountEmployees == null) {
                accountEmployees = new ArrayList<>();
            }
            //合并新、旧数组到旧数组
            for (int j = 0; j < departmentEmployees.size(); j++) {
                String employee = departmentEmployees.get(j);
                int res = -1;
                for (int i = 0; i < accountEmployees.size(); i++) {
                    if (accountEmployees.get(i).getId().equals(employee)) {
                        res = i;
                        break;
                    }
                }
                if (res == -1) {
                    CAccountEmployee accountEmployee = searchCAccountEmployee(employees, employee);
                    if (accountEmployee != null) {
                        accountEmployee.setcAccountDepartment(accountDepartment);
                        accountEmployees.add(accountEmployee);
                    }
                }
            }
            //找出新旧数组中旧数组没有的对象
            for (int j = 0; j < accountEmployees.size(); j++) {
                CAccountEmployee employee = accountEmployees.get(j);
                int res = -1;
                for (int i = 0; i < departmentEmployees.size(); i++) {
                    if (departmentEmployees.get(i).equals(employee.getId())) {
                        res = i;
                        break;
                    }
                }
                if (res == -1) {
                    employee.setcAccountDepartment(null);
                }
            }
        } else {
            for (int i = 0; i < departmentEmployees.size(); i++) {
                CAccountEmployee accountEmployee = iAccountEmployeeDAO.get(departmentEmployees.get(i));
                if (accountEmployee != null) {
                    accountEmployees.add(accountEmployee);
                }
            }
        }
        accountDepartment.setcAccountEmployees(accountEmployees);
        accountDepartment.setFZR(department.getFZR());
        accountDepartment.setLXDH(department.getLXDH());
        accountDepartment.setMingCheng(department.getMingCheng());
        accountDepartment.setcAccountNetwork(accountNetwork);
        if (StringUtil.notEmpty(department.getId())) {
            iAccountDepartmentDAO.update(accountDepartment);
        } else {
            iAccountDepartmentDAO.save(accountDepartment);
        }
        return accountDepartment.getId();
    }

    //查询员工by员工ID
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

    @Override
    public List<Delres> delDepartment(List<String> ids) {
        List<Delres> delres = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            Delres delres1 = new Delres();
            delres1.setId(ids.get(i));
            delres1.setRes(true);
            CAccountDepartment accountDepartment = iAccountDepartmentDAO.get(ids.get(i));
            if (accountDepartment != null) {
                List<CAccountEmployee> accountEmployees = accountDepartment.getcAccountEmployees();
                List<String> employeeids = new ArrayList<>();
                for (int j = 0; j < accountEmployees.size(); j++) {
                    CAccountEmployee employee = accountEmployees.get(j);
                    employeeids.add(employee.getId());
                }
                permissionService.delEmployee(employeeids);
                iAccountDepartmentDAO.delete(accountDepartment);
            } else {
                delres1.setRes(false);
            }
            delres.add(delres1);
        }
        return delres;
    }

    @Override
    public List<EmployeeWithDepartment> getDepartmentMember(String id) {
        HashMap<String, Object> filter = new HashMap<>();
        filter.put("cAccountNetwork.id", id);
        List<CAccountEmployee> accountEmployees = iAccountEmployeeDAO.list(filter, null, null, null, null, null, null);
        List<EmployeeWithDepartment> employees = new ArrayList<>();
        for (int i = 0; i < accountEmployees.size(); i++) {
            CAccountEmployee cAccountEmployee = accountEmployees.get(i);
            if (cAccountEmployee.getcAccountDepartment() != null) {
                continue;
            }
            EmployeeWithDepartment employee = new EmployeeWithDepartment();
            employee.setId(cAccountEmployee.getId());
            employee.setCreated_at(cAccountEmployee.getCreated_at());
            employee.setLxdh(cAccountEmployee.getLxdh());
            employee.setQqh(cAccountEmployee.getQqh());
            employee.setRzsj(cAccountEmployee.getRzsj());
            employee.setWxh(cAccountEmployee.getWxh());
            employee.setXingMing(cAccountEmployee.getXingMing());
            employee.setXingBie(cAccountEmployee.getXingBie());
            employee.setZhangHao(cAccountEmployee.getZhangHao());
            employee.setYouXiang(cAccountEmployee.getYouXiang());
            employee.setYhtx(cAccountEmployee.getYhtx());
            employee.setXueLi(cAccountEmployee.getXueLi());
            employees.add(employee);

        }
        return employees;
    }

    /**
     * @param netWorkStation
     * @return
     * @author xuefei_wang
     */
    private boolean initStateMachineConfg(TokenContext tokenContext, String netWorkStation) {

        File f = new File(Thread.currentThread().getContextClassLoader().getResource("").getPath() + "/statemachine.ini");
        try {
            List<String> lines = FileUtils.readLines(f, "UTF-8");
            for (String l : lines) {
                String[] ds = l.split(",");
                if (ds.length != 7) {
                    System.out.println("无法正确解析：" + Arrays.toString(ds));
                    continue;
                }
                CStateMachineConfiguration configuration = new CStateMachineConfiguration();
                configuration.setSource(ds[0]);
                configuration.setEvent(ds[1]);
                configuration.setTarget(ds[2]);
                configuration.setType(BusinessType.valueOf(ds[3]));
                configuration.setSubType(ds[4]);
                configuration.setTransitionKind(TransitionKind.valueOf(ds[5]));
                configuration.setIsAudit(ds[6]);
                configuration.setFlag(true);
                configuration.setRole(CollectionUtils.getFirst(tokenContext.getRoleList()));
                configuration.setEffectiveDate(new Date());
                configuration.setWorkstation(netWorkStation);
                icStateMachineConfigurationDAO.save(configuration);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * @param netWorkStation
     * @return
     * @author xuefei_wang
     */
    private boolean deleteStateMachineConfigUnderNetWorkStation(String netWorkStation) {
        HashMap f = new HashMap();
        f.put("workstation", netWorkStation);
        try {
            List<CStateMachineConfiguration> confs = icStateMachineConfigurationDAO.list(f, null, null, null, null, null, null);
            for (CStateMachineConfiguration cs : confs) {
                icStateMachineConfigurationDAO.delete(cs);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }


}
