package com.handge.housingfund.others.service;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessStatus;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.collection.model.withdrawlModel.CommonReturn;
import com.handge.housingfund.common.service.others.IStateMachineService;
import com.handge.housingfund.common.service.others.model.StateMachineConfig;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.dao.IBaseDAO;
import com.handge.housingfund.database.dao.ICAccountNetworkDAO;
import com.handge.housingfund.database.dao.ICRoleDAO;
import com.handge.housingfund.database.dao.ICStateMachineConfigurationDAO;
import com.handge.housingfund.database.entities.CRole;
import com.handge.housingfund.database.entities.CStateMachineConfiguration;
import com.handge.housingfund.database.enums.*;
import com.handge.housingfund.database.utils.DAOBuilder;
import com.handge.housingfund.statemachine.repository.StateConstants;
import com.handge.housingfund.statemachine.repository.StateMachineEvent;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Created by xuefei_wang on 17-8-8.
 */

public class StateMachineServiceImpl implements IStateMachineService {

    @Autowired
    protected ICStateMachineConfigurationDAO icStateMachineConfigurationDAO;
    @Autowired
    protected ICAccountNetworkDAO networkDAO;
    @Autowired
    protected ICRoleDAO roleDAO;

    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public ArrayList<StateMachineConfig> list(TokenContext tokenContext, String type, String ywwd, String subtype) {
        if (StringUtil.isEmpty(type) || StringUtil.isEmpty(ywwd)) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "业务类型或业务网点");
        }
        List<CRole> roleList = roleDAO.list(new HashMap<String, Object>(), null, null, null, null, null, null);
        HashMap<String, String> roleMap = new HashMap<>();
        for (CRole role : roleList) {
            roleMap.put(role.getId(), role.getRole_name());
        }

        ArrayList<StateMachineConfig> results = new ArrayList<>();
        try {
            HashMap<String, Object> filter = new HashMap<String, Object>();
            if (!type.equals("所有")) {
                filter.put("type", BusinessType.valueOf(StateMachineBusinessType.getTypeByDesc(type)));
            }
            filter.put("workstation", ywwd);
            filter.put("flag", true);
            List<CStateMachineConfiguration> confs = icStateMachineConfigurationDAO.list(filter, null, null, "subType", Order.DESC, null, null,
                    new IBaseDAO.CriteriaExtension() {
                        @Override
                        public void extend(Criteria criteria) {
                            if (StringUtil.notEmpty(subtype))
                                criteria.add(Restrictions.like("subType", "%" + subtype + "%"));
                        }
                    });
            List<String> subTypes = new ArrayList<>();//子类型列表

            String left = "";
            String right = "";

            for (CStateMachineConfiguration conf : confs) {

                if (Arrays.asList("审核不通过", "入账失败", "初始状态", "放款失败").contains(conf.getSource()))
                    continue;

                if (!conf.getEvent().equals(Events.通过.getEvent())) {
                    if (!conf.getEvent().equals(Events.特审.getEvent()) && !conf.getEvent().equals(Events.入账成功.getEvent())) {
                        continue;
                    } else {
                        left = left + conf.getSource();
                        right = right + conf.getTarget();
                    }
                }

                if (!subTypes.contains(conf.getSubType())) {
                    subTypes.add(conf.getSubType());
                    StateMachineConfig config = new StateMachineConfig();
                    config.setSub_type(conf.getSubType());
                    config.setEffectiveTime(DateUtil.date2Str(conf.getEffectiveDate(), "yyyy-MM-dd"));
                    config.setId(ywwd);
                    config.setYwwd(networkDAO.get(conf.getWorkstation()).getMingCheng());
                    config.setType(StateMachineBusinessType.getDescByType(conf.getType().toString()));
                    results.add(config);
                    left = "";
                    right = "";
                }

                StateMachineConfig result = results.get(subTypes.indexOf(conf.getSubType()));

                if (result.getFlowInfos() == null) {
                    List<StateMachineConfig.FlowInfo> flowInfos = new ArrayList<>();
                    result.setFlowInfos(flowInfos);
                }
                List<StateMachineConfig.FlowInfo> infos = result.getFlowInfos();
                List<String> roles = new ArrayList<>();
                if (!conf.getRole().isEmpty()) {
                    for (String roleId : conf.getRole().split(",")) {
                        roles.add(roleMap.get(roleId));
                    }
                }


                boolean flag = true;
                for (int i = 0; i < infos.size(); i++) {
                    if (infos.get(i).getState().equals(conf.getSource())) {
                        flag = false;
                        infos.get(i).setRoles(roles);
                        infos.get(i).setAuditLevel(Integer.parseInt(conf.getIsAudit()));
                        if (right != null && right.contains(conf.getSource()))
                            infos.get(i).setIsSpeicial(true);
                        if (left != null && left.contains(conf.getSource()) && conf.getEvent().equals(Events.通过.getEvent()))
                            break;
                        StateMachineConfig.FlowInfo flow = new StateMachineConfig.FlowInfo();
                        flow.setState(conf.getTarget());
                        if (!(i < infos.size() - 1 && conf.getTarget().equals(infos.get(i + 1).getState())))
                            infos.add(i + 1, flow);
                        break;
                    } else if (infos.get(i).getState().equals(conf.getTarget())) {
                        flag = false;
                        StateMachineConfig.FlowInfo flow = new StateMachineConfig.FlowInfo();
                        flow.setState(conf.getSource());
                        flow.setRoles(roles);
                        flow.setAuditLevel(Integer.parseInt(conf.getIsAudit()));
                        if (right != null && right.contains(conf.getSource()))
                            flow.setIsSpeicial(true);
                        infos.add(i, flow);
                        break;
                    }
                }
                if (flag) {
                    StateMachineConfig.FlowInfo flow1 = new StateMachineConfig.FlowInfo();
                    StateMachineConfig.FlowInfo flow2 = new StateMachineConfig.FlowInfo();
                    flow1.setState(conf.getSource());
                    flow1.setRoles(roles);
                    flow1.setAuditLevel(Integer.parseInt(conf.getIsAudit()));
                    if (right != null && right.contains(conf.getSource()))
                        flow1.setIsSpeicial(true);
                    flow2.setState(conf.getTarget());
                    infos.add(infos.size(), flow1);
                    infos.add(infos.size(), flow2);
                }
            }

        } catch (ErrorException e) {
            throw e;
        }

        return results;
    }

    @Override
    public List<StateMachineConfig> listDetails(TokenContext tokenContext, String type, String subtype) {
        List<StateMachineConfig> result = new ArrayList<>();
        String netWorkStation = tokenContext.getUserInfo().getYWWD();
        if (type == null || subtype == null) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "业务类型或子类型");
        }
        try {
            HashMap<String, Object> filter = new HashMap<String, Object>();
            filter.put("type", BusinessType.valueOf(StateMachineBusinessType.getTypeByDesc(type)));
            filter.put("workstation", netWorkStation);
            filter.put("flag", true);
            filter.put("subType", subtype);
            List<CStateMachineConfiguration> confs = icStateMachineConfigurationDAO.list(filter, null, null, null, null, null, null);
            List<StateMachineConfig.FlowInfo> fls = new ArrayList<>();
            StateMachineConfig stateMachineConfig = new StateMachineConfig();
            int No = 0;//编号
            String nextStatus = null;
            List<String> sourceList = new ArrayList<>();
            //第一条
            for (CStateMachineConfiguration config : confs) {
                stateMachineConfig.setType(StateMachineBusinessType.getDescByType(config.getType().toString()));
                stateMachineConfig.setSub_type(config.getSubType());
                stateMachineConfig.setEffectiveTime(DateUtil.date2Str(config.getEffectiveDate(), "yyyy-MM-dd"));
                if (CollectionBusinessStatus.新建.getName().equals(config.getSource()) && Arrays.asList(Events.通过.getEvent()).contains(config.getEvent())) {
                    No++;
                    StateMachineConfig.FlowInfo flowInfo = new StateMachineConfig.FlowInfo();
                    flowInfo.setNo(No);
                    flowInfo.setState(config.getSource());
                    flowInfo.setRoles(Arrays.asList(config.getRole().split(",")));
                    flowInfo.setAuditLevel(Integer.parseInt(config.getIsAudit()));
                    fls.add(flowInfo);
                    nextStatus = config.getTarget();
                    stateMachineConfig.setFlowInfos(fls);
                }
                sourceList.add(config.getSource());
            }

            for (CStateMachineConfiguration config : confs) {
                if (config.getSource().equals(nextStatus) && Arrays.asList(Events.通过.getEvent(), Events.特审.getEvent()).contains(config.getEvent())) {
                    No++;
                    StateMachineConfig.FlowInfo flowInfo = new StateMachineConfig.FlowInfo();
                    flowInfo.setNo(No);
                    flowInfo.setState(config.getSource());
                    flowInfo.setRoles(Arrays.asList(config.getRole().split(",")));
                    flowInfo.setAuditLevel(Integer.parseInt(config.getIsAudit()));
                    fls.add(flowInfo);
                    nextStatus = config.getTarget();
                    stateMachineConfig.setFlowInfos(fls);
                }
            }
            //最后一条
            if (nextStatus != null && !sourceList.contains(nextStatus)) {
                No++;
                StateMachineConfig.FlowInfo flowInfo = new StateMachineConfig.FlowInfo();
                flowInfo.setNo(No);
                flowInfo.setState(nextStatus);
                flowInfo.setRoles(Arrays.asList("自动"));
                flowInfo.setAuditLevel(0);
                fls.add(flowInfo);
                stateMachineConfig.setFlowInfos(fls);
            }
            result.add(stateMachineConfig);
        } catch (ErrorException e) {
            throw e;
        }
        return result;
    }

    /**
     * 列出业务类型集合
     *
     * @param tokenContext
     * @return
     */
    @Override
    public List<String> listTypes(TokenContext tokenContext) {
        String netWorkStation = tokenContext.getUserInfo().getYWWD();
        HashMap<String, Object> filter = new HashMap<String, Object>();
        filter.put("workstation", netWorkStation);
        filter.put("flag", true);
        List<String> results = new ArrayList<>();
        try {
            List<CStateMachineConfiguration> confs = icStateMachineConfigurationDAO.list(filter, null, null, null, null, ListDeleted.NOTDELETED, SearchOption.FUZZY);
            results = confs.stream().map(new Function<CStateMachineConfiguration, String>() {
                public String apply(CStateMachineConfiguration cStateMachineConfiguration) {
                    return StateMachineBusinessType.getDescByType(cStateMachineConfiguration.getType().toString());
                }
            }).distinct()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return results;
    }

    /**
     * 列出子业务类型集合
     *
     * @param tokenContext
     * @param type
     * @return
     */
    @Override
    public List<String> listSubTypes(TokenContext tokenContext, String type) {
        if (type == null) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "业务类型");
        }
        String netWorkStation = tokenContext.getUserInfo().getYWWD();
        List<String> results = new ArrayList<>();
        try {
            HashMap<String, Object> filter = new HashMap<String, Object>();
            filter.put("type", BusinessType.valueOf(StateMachineBusinessType.getTypeByDesc(type)));
            filter.put("workstation", netWorkStation);
            filter.put("flag", true);
            List<CStateMachineConfiguration> confs = icStateMachineConfigurationDAO.list(filter, null, null, null, null, ListDeleted.NOTDELETED, SearchOption.REFINED);
            results = confs.stream().map(new Function<CStateMachineConfiguration, String>() {
                public String apply(CStateMachineConfiguration cStateMachineConfiguration) {
                    return cStateMachineConfiguration.getSubType();
                }
            }).distinct()
                    .collect(Collectors.<String>toList());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return results;
    }


    /**
     * 更新流程(暂定立即生效)
     *
     * @param tokenContext
     * @param stateMachineConfig
     * @return
     */
    @Override
    public CommonReturn update(TokenContext tokenContext, StateMachineConfig stateMachineConfig, String ywwd) {
        if (StringUtil.isEmpty(ywwd)) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "业务网点");
        }
        CommonReturn commonReturn = new CommonReturn();
        Date effectiveTime = null;
        try {
            effectiveTime = DateUtil.str2Date("yyyy-MM-dd", stateMachineConfig.getEffectiveTime());
        } catch (ParseException e) {
            e.printStackTrace();
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "生效时间格式有误");
        }

//        if(new Date().getTime() > effectiveTime.getTime()){
//            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "生效时间不能小于当前时间");
//        }
        String sub_type = null;
        BusinessType type = null;
        try {
            sub_type = stateMachineConfig.getSub_type();
            type = BusinessType.valueOf(StateMachineBusinessType.getTypeByDesc(stateMachineConfig.getType()));
        } catch (ErrorException e) {
            throw e;
        }
        List<StateMachineConfig.FlowInfo> flows = stateMachineConfig.getFlowInfos();
        if (flows.size() < 1) {
            commonReturn.setStatus("success");
            return commonReturn;
        }
        List<String> stateName = new ArrayList<>();
        boolean flag = false;
        List<String> lxhqrjsRole = new ArrayList<>();
        try {
            List<CStateMachineConfiguration> configs = new ArrayList<>();
            int auditNo = 0;
            StateMachineConfig.FlowInfo temp = null;
            for (StateMachineConfig.FlowInfo flow : flows) {

                if (flow.getAuditLevel() != 0) {

                    if (flow.getIsSpeicial()) {
                        flag = true;
                    }
                    if (!flow.getIsSpeicial() && flag) {
                        throw new ErrorException(ReturnEnumeration.StateMachineConfig_Unknow_Error, "特审配置不规范");
                    }
                    if (!stateName.contains(flow.getState())) {
                        stateName.add(flow.getState());
                    } else {
                        throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "描述(状态)不能重复");
                    }

                    auditNo++;
                }
                if (flow.getAuditLevel() == 1 && flow.getIsSpeicial()) {
                    throw new ErrorException(ReturnEnumeration.StateMachineConfig_Unknow_Error, "第一级审核不能为特审");
                }

                if (BusinessSubType.归集_转出个人接续.getSubType().equals(sub_type) && CollectionBusinessStatus.联系函确认接收.getName().equals(flow.getState())) {
                    for (String role : flow.getRoles()) {
                        List<CRole> cRoles = roleDAO.list(new HashMap<String, Object>() {{
                            this.put("role_name", role);
                        }}, null, null, null, null, null, null);
                        if (cRoles.size() == 0) {
                            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "角色不能为空");
                        }
                        CRole cRole = cRoles.get(0);
                        lxhqrjsRole.add(cRole.getId());
                    }
                }
            }


            boolean isFirstTransion = false;
            String initRole = null;
            String auditFinalState = null;
            StateMachineConfig.FlowInfo beforeSpeicialState = null;

            String lxhqrjsNext = null;
            //region缴存特殊业务
            if (BusinessType.Collection.equals(type) && Arrays.asList(BusinessSubType.归集_错缴更正.getSubType(), BusinessSubType.归集_缴存清册记录.getSubType(), BusinessSubType.归集_补缴记录.getSubType(), BusinessSubType.归集_汇缴记录.getSubType()).contains(sub_type)) {
                //这四个业务子类型单独处理
                HashMap filter1 = new HashMap();
                filter1.put("workstation", ywwd);
                filter1.put("type", type);
                filter1.put("subType", sub_type);
                filter1.put("flag", true);
                List<CStateMachineConfiguration> configurations = icStateMachineConfigurationDAO.list(FixMap.ignoreNullValue(filter1), null, null, null, null, null, null);
                //region生效日期
//                for (CStateMachineConfiguration configuration : configurations) {
//                    List<String> roleIds = new ArrayList<>();
//                    for (String role : flows.get(0).getRoles()) {
//                        CRole cRole = roleDAO.list(new HashMap<String, Object>() {{
//                            this.put("role_name", role);
//                        }}, null, null, null, null, null, null).get(0);
//                        roleIds.add(cRole.getId());
//                    }
//
//                    configs.add(new CStateMachineConfiguration(
//                            type,
//                            sub_type,
//                            configuration.getSource(),
//                            configuration.getTarget(),
//                            configuration.getEvent(),
//                            false,
////                            true,
//                            String.join(",", roleIds),
//                            configuration.getTransitionKind(),
//                            configuration.getEffectiveDate(),
//                            configuration.getIsAudit(),
//                            ywwd
//                    ));
//                }
                //endregion
                //region立即生效
                if (BusinessSubType.归集_汇缴记录.getSubType().equals(sub_type)) {
                    for (CStateMachineConfiguration configuration : configurations) {
                        if (configuration.getSource().equals(CollectionBusinessStatus.新建.getName())) {
                            List<String> roleIds = new ArrayList<>();
                            if (flows.get(0).getRoles().size() == 0) {
//                         throw new ErrorException(ReturnEnumeration.Parameter_MISS,"角色不能为空");
                            }
                            for (String role : flows.get(0).getRoles()) {
                                List<CRole> cRoles = roleDAO.list(new HashMap<String, Object>() {{
                                    this.put("role_name", role);
                                }}, null, null, null, null, null, null);
                                if (cRoles.size() == 0) {
                                    throw new ErrorException(ReturnEnumeration.Parameter_MISS, "角色不能为空");
                                }
                                CRole cRole = cRoles.get(0);
                                roleIds.add(cRole.getId());
                            }
                            configuration.setRole(String.join(",", roleIds));
                            configuration.setFlag(true);
                            icStateMachineConfigurationDAO.update(configuration);
                        }
                        if (CollectionBusinessStatus.待确认.getName().equals(configuration.getSource())) {
                            List<String> roleIds = new ArrayList<>();
                            if (flows.get(1).getRoles().size() == 0) {
//                         throw new ErrorException(ReturnEnumeration.Parameter_MISS,"角色不能为空");
                            }
                            for (String role : flows.get(1).getRoles()) {
                                List<CRole> cRoles = roleDAO.list(new HashMap<String, Object>() {{
                                    this.put("role_name", role);
                                }}, null, null, null, null, null, null);
                                if (cRoles.size() == 0) {
                                    throw new ErrorException(ReturnEnumeration.Parameter_MISS, "角色不能为空");
                                }
                                CRole cRole = cRoles.get(0);
                                roleIds.add(cRole.getId());
                            }
                            configuration.setRole(String.join(",", roleIds));
                            configuration.setFlag(true);
                            icStateMachineConfigurationDAO.update(configuration);
                        }
                    }
                } else {
                    for (CStateMachineConfiguration configuration : configurations) {

                        List<String> roleIds = new ArrayList<>();
                        if (flows.get(0).getRoles().size() == 0) {
//                         throw new ErrorException(ReturnEnumeration.Parameter_MISS,"角色不能为空");
                        }
                        for (String role : flows.get(0).getRoles()) {
                            List<CRole> cRoles = roleDAO.list(new HashMap<String, Object>() {{
                                this.put("role_name", role);
                            }}, null, null, null, null, null, null);
                            if (cRoles.size() == 0) {
                                throw new ErrorException(ReturnEnumeration.Parameter_MISS, "角色不能为空");
                            }
                            CRole cRole = cRoles.get(0);
                            roleIds.add(cRole.getId());
                        }
                        configuration.setRole(String.join(",", roleIds));
                        configuration.setFlag(true);
                        icStateMachineConfigurationDAO.update(configuration);

                    }
                }

                //endregion
            }
            //endregion

            else {

                for (StateMachineConfig.FlowInfo flow : flows) {
                    if (temp == null) {
                        temp = flow;
                        isFirstTransion = true;
                        continue;
                    }
                    //region根据输入的角色名获取角色的ID
                    if (temp.getRoles().size() == 0) {
//                        throw new ErrorException(ReturnEnumeration.Parameter_MISS,"角色不能为空");
                    }

                    List<String> tempRoleIds = new ArrayList<>();
                    for (String role : temp.getRoles()) {
                        List<CRole> cRoles = roleDAO.list(new HashMap<String, Object>() {{
                            this.put("role_name", role);
                        }}, null, null, null, null, null, null);
                        if (cRoles.size() == 0) {
                            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "角色不能为空");
                        }
                        CRole cRole = cRoles.get(0);
                        tempRoleIds.add(cRole.getId());
                    }

                    List<String> flowRoleIds = new ArrayList<>();
                    for (String role : flow.getRoles()) {
                        List<CRole> cRoles = roleDAO.list(new HashMap<String, Object>() {{
                            this.put("role_name", role);
                        }}, null, null, null, null, null, null);
                        if (cRoles.size() == 0) {
                            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "角色不能为空");
                        }
                        CRole cRole = cRoles.get(0);
                        flowRoleIds.add(cRole.getId());
                    }
                    //endregion

                    //region 常规业务审核配置
                    if (temp.getAuditLevel() != 0 && flow.getAuditLevel() == 0) {
                        auditFinalState = flow.getState();
                    }

                    if (!temp.getIsSpeicial() && flow.getIsSpeicial()) {
                        beforeSpeicialState = temp;
                    }

                    // Ax --("通过",A.role)-->Bx
                    CStateMachineConfiguration stepTranstion = new CStateMachineConfiguration(type,
                            sub_type,
                            temp.getState(),
                            flow.getState(),
                            flow.getIsSpeicial() ? StateMachineEvent.SPECIAL : StateMachineEvent.SUCCESS,
//                                false,
                            true,
                            String.join(",", tempRoleIds),
                            TransitionKind.EXTERNAL,
                            effectiveTime,
                            String.valueOf(temp.getAuditLevel()),
                            ywwd);
                    configs.add(stepTranstion);


                    if (Arrays.asList(BusinessType.Loan).contains(type) && Arrays.asList(BusinessSubType.贷款_个人贷款申请.getSubType()).contains(sub_type) && temp.getState().equals(CollectionBusinessStatus.待确认.getName())) {
                        // 待确认 --("保存"　Ax.role)--> 待确认
                        configs.add(new CStateMachineConfiguration(type,
                                sub_type,
                                temp.getState(),
                                temp.getState(),
                                StateMachineEvent.SAVE,
//                                    false,
                                true,
                                String.join(",", tempRoleIds),
                                TransitionKind.EXTERNAL,
                                effectiveTime,
                                String.valueOf(0),
                                ywwd));

                        // 待确认 --(作废)--> 待确认
                        configs.add(new CStateMachineConfiguration(type,
                                sub_type,
                                temp.getState(),
                                CollectionBusinessStatus.已作废.getName(),
                                StateMachineEvent.CANCEL,
//                                    false,
                                true,
                                String.join(",", tempRoleIds),
                                TransitionKind.EXTERNAL,
                                effectiveTime,
                                String.valueOf(0),
                                ywwd));




                        //待放款--（作废）--已作废
                        CStateMachineConfiguration cancelStep = new CStateMachineConfiguration(type,
                                sub_type,
                                flow.getState(),
                                CollectionBusinessStatus.已作废.getName(),
                                StateMachineEvent.CANCEL,
//                                    false,
                                true,
                                String.join(",", tempRoleIds),
                                TransitionKind.EXTERNAL,
                                effectiveTime,
                                String.valueOf(temp.getAuditLevel()),
                                ywwd);
                        configs.add(cancelStep);

                        //放款失败--（作废）-- 已作废
                        CStateMachineConfiguration cancelStep2 = new CStateMachineConfiguration(type,
                                sub_type,
                                String.format("%s%s", flow.getState().replace("待", ""), "失败"),
                                CollectionBusinessStatus.已作废.getName(),
                                StateMachineEvent.CANCEL,
//                                    false,
                                true,
                                String.join(",", tempRoleIds),
                                TransitionKind.EXTERNAL,
                                effectiveTime,
                                String.valueOf(temp.getAuditLevel()),
                                ywwd);
                        configs.add(cancelStep2);


                    }
//                    }

                    if (temp.getAuditLevel() != 0) {
                        // Ax --("不通过"　A.role)-->审核不通过
                        CStateMachineConfiguration failedtepTranstion = new CStateMachineConfiguration(type,
                                sub_type,
                                temp.getState(),
                                StateConstants.failedState.getId(),
                                StateMachineEvent.FAILED,
//                                false,
                                true,
                                String.join(",", tempRoleIds),
                                TransitionKind.EXTERNAL,
                                effectiveTime,
                                String.valueOf(temp.getAuditLevel()),
                                ywwd);
                        configs.add(failedtepTranstion);
                        //非特审才能撤回到审核不通过
                        if (!flow.getIsSpeicial() && flow.getAuditLevel() != 0) {
                            // Ax --("撤回"　A.role)-->审核不通过
                            CStateMachineConfiguration reFailedTranstion = new CStateMachineConfiguration(type,
                                    sub_type,
                                    flow.getState(),
                                    StateConstants.failedState.getId(),
                                    StateMachineEvent.REVOKE,
//                                    false,
                                    true,
                                    String.join(",", flowRoleIds),
                                    TransitionKind.EXTERNAL,
                                    effectiveTime,
                                    String.valueOf(flow.getAuditLevel()),
                                    ywwd);
                            configs.add(reFailedTranstion);
                        }
                    } else {
                        if (Arrays.asList(BusinessSubType.贷款_个人贷款申请.getSubType()).contains(sub_type) && temp.getState().equals(CollectionBusinessStatus.待签合同.getName())) {
                            //待X --（不通过）--X失败
                            CStateMachineConfiguration failedtepTranstion = new CStateMachineConfiguration(type,
                                    sub_type,
                                    temp.getState(),
                                    CollectionBusinessStatus.已作废.getName(),
                                    StateMachineEvent.CANCEL,
//                                false,
                                    true,
                                    String.join(",", tempRoleIds),
                                    TransitionKind.EXTERNAL,
                                    effectiveTime,
                                    String.valueOf(temp.getAuditLevel()),
                                    ywwd);
                            configs.add(failedtepTranstion);
                        } else {
                            if (Arrays.asList(BusinessSubType.归集_转出个人接续.getSubType()).contains(sub_type)) {
                                HashMap<String, String> map = new HashMap();
                                map.put(CollectionBusinessStatus.联系函审核通过.getName(), CollectionBusinessStatus.转入撤销业务办结.getName());
                                map.put(CollectionBusinessStatus.联系函确认接收.getName(), CollectionBusinessStatus.转出审核不通过.getName());
                                map.put(CollectionBusinessStatus.转出地已转账.getName(), CollectionBusinessStatus.协商中.getName());
                                map.put(CollectionBusinessStatus.新建.getName(), "新建失败");
                                map.put(CollectionBusinessStatus.账户信息审核通过.getName(), "账户信息审核不通过");
                                map.put(CollectionBusinessStatus.转账中.getName(), "转账失败");

                                CStateMachineConfiguration failedtepTranstion = new CStateMachineConfiguration(type,
                                        sub_type,
                                        temp.getState(),
                                        map.get(temp.getState()),
                                        StateMachineEvent.FAILED,
//                                         false,
                                        true,
                                        String.join(",", tempRoleIds),
                                        TransitionKind.EXTERNAL,
                                        effectiveTime,
                                        String.valueOf(temp.getAuditLevel()),
                                        ywwd);
                                configs.add(failedtepTranstion);

                                if (CollectionBusinessStatus.新建.getName().equals(temp.getState())) {
                                    lxhqrjsNext = flow.getState();
                                    // Ax --("通过",A.role)-->Bx
                                    CStateMachineConfiguration stepTranstion2 = new CStateMachineConfiguration(type,
                                            sub_type,
                                            CollectionBusinessStatus.联系函确认接收.getName(),
                                            lxhqrjsNext,
                                            StateMachineEvent.SUBMIT,
//                                false,
                                            true,
                                            String.join(",", lxhqrjsRole),
                                            TransitionKind.EXTERNAL,
                                            effectiveTime,
                                            String.valueOf(temp.getAuditLevel()),
                                            ywwd);
                                    configs.add(stepTranstion2);

                                    // 审核不通过 --("通过"　Ax.role)-->
                                    configs.add(new CStateMachineConfiguration(type,
                                            sub_type,
                                            StateConstants.failedState.getId(),
                                            flow.getState(),
                                            StateMachineEvent.SUCCESS,
                                            //                                false,
                                            true,
                                            String.join(",", tempRoleIds),
                                            TransitionKind.EXTERNAL,
                                            effectiveTime,
                                            String.valueOf(temp.getAuditLevel()),
                                            ywwd));


                                    if (flow.getAuditLevel() == 1) {
                                        // B1 --("撤回"　A1.role)-->A1
                                        configs.add(new CStateMachineConfiguration(type,
                                                sub_type,
                                                flow.getState(),
                                                temp.getState(),
                                                StateMachineEvent.REVOKE,
//                                    false,
                                                true,
                                                String.join(",", tempRoleIds),
                                                TransitionKind.EXTERNAL,
                                                effectiveTime,
                                                String.valueOf(temp.getAuditLevel()),
                                                ywwd));
                                    }
                                }

                            } else if (sub_type.equals(BusinessSubType.归集_转入个人接续.getSubType())) {
                                HashMap<String, String> map = new HashMap();
                                map.put(CollectionBusinessStatus.联系函审核通过.getName(), CollectionBusinessStatus.转入撤销业务办结.getName());
                                map.put(CollectionBusinessStatus.联系函确认接收.getName(), CollectionBusinessStatus.转出审核不通过.getName());
                                map.put(CollectionBusinessStatus.账户信息审核通过.getName(), CollectionBusinessStatus.协商中.getName());
                                map.put(CollectionBusinessStatus.新建.getName(), "新建失败");
                                CStateMachineConfiguration failedtepTranstion = new CStateMachineConfiguration(type,
                                        sub_type,
                                        temp.getState(),
                                        map.get(temp.getState()),
                                        StateMachineEvent.FAILED,
//                                         false,
                                        true,
                                        String.join(",", tempRoleIds),
                                        TransitionKind.EXTERNAL,
                                        effectiveTime,
                                        String.valueOf(temp.getAuditLevel()),
                                        ywwd);
                                configs.add(failedtepTranstion);
                            } else {
                                //待X --（不通过）--X失败
                                CStateMachineConfiguration failedtepTranstion = new CStateMachineConfiguration(type,
                                        sub_type,
                                        temp.getState(),
                                        String.format("%s%s", temp.getState().replace("待", ""), "失败"),
                                        StateMachineEvent.FAILED,
//                                false,
                                        true,
                                        String.join(",", tempRoleIds),
                                        TransitionKind.EXTERNAL,
                                        effectiveTime,
                                        String.valueOf(temp.getAuditLevel()),
                                        ywwd);
                                configs.add(failedtepTranstion);
                                if (Arrays.asList(BusinessSubType.贷款_个人还款申请.getSubType()).contains(sub_type) && temp.getState().equals(CollectionBusinessStatus.待入账.getName())) {
                                    //X失败--（通过）--Bx
                                    CStateMachineConfiguration failedtep2SuccessTranstion = new CStateMachineConfiguration(type,
                                            sub_type,
                                            String.format("%s%s", temp.getState().replace("待", ""), "失败"),
                                            flow.getState(),
                                            StateMachineEvent.SUCCESS,
//                                    false,
                                            true,
                                            String.join(",", tempRoleIds),
                                            TransitionKind.EXTERNAL,
                                            effectiveTime,
                                            String.valueOf(temp.getAuditLevel()),
                                            ywwd);
                                    configs.add(failedtep2SuccessTranstion);
                                }
                                if (Arrays.asList(BusinessSubType.贷款_个人贷款申请.getSubType()).contains(sub_type) && temp.getState().equals(CollectionBusinessStatus.待放款.getName())) {
                                    //X失败--（通过）--Bx
                                    CStateMachineConfiguration successtep2SuccessTranstion = new CStateMachineConfiguration(type,
                                            sub_type,
                                            String.format("%s%s", temp.getState().replace("待", ""), "失败"),
                                            flow.getState(),
                                            StateMachineEvent.SUCCESS,
//                                    false,
                                            true,
                                            String.join(",", tempRoleIds),
                                            TransitionKind.EXTERNAL,
                                            effectiveTime,
                                            String.valueOf(temp.getAuditLevel()),
                                            ywwd);
                                    configs.add(successtep2SuccessTranstion);

                                    //X失败--（不通过）--X失败
                                    CStateMachineConfiguration failedtep2SuccessTranstion = new CStateMachineConfiguration(type,
                                            sub_type,
                                            String.format("%s%s", temp.getState().replace("待", ""), "失败"),
                                            String.format("%s%s", temp.getState().replace("待", ""), "失败"),
                                            StateMachineEvent.FAILED,
//                                    false,
                                            true,
                                            String.join(",", tempRoleIds),
                                            TransitionKind.EXTERNAL,
                                            effectiveTime,
                                            String.valueOf(temp.getAuditLevel()),
                                            ywwd);
                                    configs.add(failedtep2SuccessTranstion);


                                }
                            }
                        }

                    }
                    //endregion

                    //region初始设置
                    if (isFirstTransion) {
                        initRole = String.join(",", tempRoleIds);
                        //初始状态 --（"提交"）--> A1
                        configs.add(new CStateMachineConfiguration(type,
                                sub_type,
                                CollectionBusinessStatus.初始状态.getName(),
                                flow.getState(),
                                StateMachineEvent.SUBMIT,
//                                false,
                                true,
                                initRole,
                                TransitionKind.EXTERNAL,
                                effectiveTime,
                                String.valueOf(0),
                                ywwd));


                        // 审核不通过 --("不通过"　Ax.role)-->丢弃
                        configs.add(new CStateMachineConfiguration(type,
                                sub_type,
                                StateConstants.failedState.getId(),
                                StateConstants.abandonState.getId(),
                                StateMachineEvent.FAILED,
//                                false,
                                true,
                                String.join(",", tempRoleIds),
                                TransitionKind.EXTERNAL,
                                effectiveTime,
                                String.valueOf(temp.getAuditLevel()),
                                ywwd));
                        // 审核不通过 --("保存"　Ax.role)-->
                        configs.add(new CStateMachineConfiguration(type,
                                sub_type,
                                StateConstants.failedState.getId(),
                                StateConstants.failedState.getId(),
                                StateMachineEvent.SAVE,
//                                false,
                                true,
                                String.join(",", tempRoleIds),
                                TransitionKind.EXTERNAL,
                                effectiveTime,
                                String.valueOf(0),
                                ywwd));
                        // 新建 --("保存"　Ax.role)-->
                        configs.add(new CStateMachineConfiguration(type,
                                sub_type,
                                CollectionBusinessStatus.新建.getName(),
                                CollectionBusinessStatus.新建.getName(),
                                StateMachineEvent.SAVE,
//                                false,
                                true,
                                String.join(",", tempRoleIds),
                                TransitionKind.EXTERNAL,
                                effectiveTime,
                                String.valueOf(0),
                                ywwd));
                        if (!BusinessSubType.归集_转出个人接续.getSubType().equals(sub_type)) {
                            // 审核不通过 --("通过"　Ax.role)-->
                            configs.add(new CStateMachineConfiguration(type,
                                    sub_type,
                                    StateConstants.failedState.getId(),
                                    flow.getState(),
                                    StateMachineEvent.SUCCESS,
                                    //                                false,
                                    true,
                                    String.join(",", tempRoleIds),
                                    TransitionKind.EXTERNAL,
                                    effectiveTime,
                                    String.valueOf(temp.getAuditLevel()),
                                    ywwd));

                            if (flow.getAuditLevel() == 1) {
                                // B1 --("撤回"　A1.role)-->A1
                                configs.add(new CStateMachineConfiguration(type,
                                        sub_type,
                                        flow.getState(),
                                        temp.getState(),
                                        StateMachineEvent.REVOKE,
//                                    false,
                                        true,
                                        String.join(",", tempRoleIds),
                                        TransitionKind.EXTERNAL,
                                        effectiveTime,
                                        String.valueOf(temp.getAuditLevel()),
                                        ywwd));
                            }
                        }

                        // 初始状态 --("通过"　Ax.role)-->新建
                        configs.add(new CStateMachineConfiguration(type,
                                sub_type,
                                StateConstants.initialState.getId(),
                                CollectionBusinessStatus.新建.getName(),
                                StateMachineEvent.SUCCESS,
//                                false,
                                true,
                                String.join(",", tempRoleIds),
                                TransitionKind.EXTERNAL,
                                effectiveTime,
                                String.valueOf(temp.getAuditLevel()),
                                ywwd));
                        if (Arrays.asList(BusinessSubType.归集_转出个人接续.getSubType(), BusinessSubType.归集_转入个人接续.getSubType()).contains(sub_type)) {

                            CStateMachineConfiguration failedtepTranstion1 = new CStateMachineConfiguration(type,
                                    sub_type,
                                    CollectionBusinessStatus.协商中.getName(),
                                    CollectionBusinessStatus.协商后业务办结.getName(),
                                    StateMachineEvent.FAILED,
//                                         false,
                                    true,
                                    String.join(",", tempRoleIds),
                                    TransitionKind.EXTERNAL,
                                    effectiveTime,
                                    String.valueOf(temp.getAuditLevel()),
                                    ywwd);
                            configs.add(failedtepTranstion1);

                            CStateMachineConfiguration failedtepTranstion2 = new CStateMachineConfiguration(type,
                                    sub_type,
                                    CollectionBusinessStatus.转出审核不通过.getName(),
                                    CollectionBusinessStatus.转出失败业务办结.getName(),
                                    StateMachineEvent.FAILED,
//                                         false,
                                    true,
                                    String.join(",", tempRoleIds),
                                    TransitionKind.EXTERNAL,
                                    effectiveTime,
                                    String.valueOf(temp.getAuditLevel()),
                                    ywwd);
                            configs.add(failedtepTranstion2);
                        }


                        isFirstTransion = false;
                    }
                    //endregion
                    temp = flow;


                }
                //region直接审核完毕

                if (auditFinalState != null) {
                    for (StateMachineConfig.FlowInfo flowInfo : flows) {
                        if (flowInfo.getIsSpeicial() && flowInfo.getAuditLevel() != auditNo) {
                            List<String> roleIdss = new ArrayList<>();
                            for (String role : flowInfo.getRoles()) {
                                CRole cRole = roleDAO.list(new HashMap<String, Object>() {{
                                    this.put("role_name", role);
                                }}, null, null, null, null, null, null).get(0);
                                roleIdss.add(cRole.getId());
                            }
                            //特审中的状态可直接审核完毕
                            // X-1 --("通过"　A.role)-->审核完毕
                            CStateMachineConfiguration failedtepTranstion = new CStateMachineConfiguration(type,
                                    sub_type,
                                    flowInfo.getState(),
                                    auditFinalState,
                                    StateMachineEvent.SUCCESS,
//                                    false,
                                    true,
                                    String.join(",", roleIdss),
                                    TransitionKind.EXTERNAL,
                                    effectiveTime,
                                    String.valueOf(flowInfo.getAuditLevel()),
                                    ywwd);
                            configs.add(failedtepTranstion);
                        }
                    }
                }

                if (auditFinalState != null && beforeSpeicialState != null) {
                    List<String> roleIdss = new ArrayList<>();
                    for (String role : beforeSpeicialState.getRoles()) {
                        CRole cRole = roleDAO.list(new HashMap<String, Object>() {{
                            this.put("role_name", role);
                        }}, null, null, null, null, null, null).get(0);
                        roleIdss.add(cRole.getId());
                    }
                    // X-1 --("通过"　A.role)-->审核完毕
                    CStateMachineConfiguration failedtepTranstion = new CStateMachineConfiguration(type,
                            sub_type,
                            beforeSpeicialState.getState(),
                            auditFinalState,
                            StateMachineEvent.SUCCESS,
//                                    false,
                            true,
                            String.join(",", roleIdss),
                            TransitionKind.EXTERNAL,
                            effectiveTime,
                            String.valueOf(beforeSpeicialState.getAuditLevel()),
                            ywwd);
                    configs.add(failedtepTranstion);
                }
                //endregion
            }
            if (!(BusinessType.Collection.equals(type) && Arrays.asList(BusinessSubType.归集_错缴更正.getSubType(), BusinessSubType.归集_缴存清册记录.getSubType(), BusinessSubType.归集_补缴记录.getSubType(), BusinessSubType.归集_汇缴记录.getSubType()).contains(sub_type))) {
                HashMap filter = new HashMap();
                filter.put("workstation", ywwd);
                filter.put("type", type);
                filter.put("subType", sub_type);
                filter.put("flag", true);
                List<CStateMachineConfiguration> smcs = icStateMachineConfigurationDAO.list(FixMap.ignoreNullValue(filter), null, null, null, null, null, null);
                for (CStateMachineConfiguration smc : smcs) {
                    icStateMachineConfigurationDAO.delete(smc); //*****
                }
            }
            for (CStateMachineConfiguration c : configs) {
                icStateMachineConfigurationDAO.save(c);
                System.out.println(c);
            }
        } catch (
                ErrorException e)

        {
            e.printStackTrace();
            throw e;
        }
        commonReturn.setStatus("success");
        return commonReturn;
    }


    @Override
    public CommonReturn updateStatemachineConfig() {
        List<CStateMachineConfiguration> configs = DAOBuilder.instance(icStateMachineConfigurationDAO).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {

            }
        });
        List<String> subtypes = new ArrayList<>();
        List<String> networks = new ArrayList<>();
        for (CStateMachineConfiguration config : configs) {
            try {
                if (df.parse(df.format(config.getEffectiveDate())).getTime() == df.parse(df.format(new Date())).getTime()) {
                    config.setFlag(true);
                    if (!subtypes.contains(config.getSubType())) {
                        subtypes.add(config.getSubType());
                    }
                    if (!networks.contains(config.getWorkstation())) {
                        networks.add(config.getWorkstation());
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        for (CStateMachineConfiguration config : configs) {
            for (String subtype : subtypes) {
                for (String network : networks) {
                    if (config.getSubType().equals(subtype) && config.getWorkstation().equals(network)) {
                        try {
                            if (df.parse(df.format(config.getEffectiveDate())).getTime() < df.parse(df.format(new Date())).getTime()) {
                                icStateMachineConfigurationDAO.delete(config);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return new CommonReturn() {{
            this.setStatus("success");
        }};
    }

    @Override
    public boolean addNewConfig(String typename) {
        try {
            String subtype = "日常" + typename;
            List<CStateMachineConfiguration> configs = new ArrayList<>();
            for (int ywwd = 1; ywwd <= 11; ywwd++) {
                //初始状态 --（"通过"）--> 新建
                configs.add(new CStateMachineConfiguration(BusinessType.Finance,
                        subtype,
                        CollectionBusinessStatus.初始状态.getName(),
                        CollectionBusinessStatus.新建.getName(),
                        StateMachineEvent.SUCCESS,
//                  false,
                        true,
                        "",
                        TransitionKind.EXTERNAL,
                        new Date(),
                        String.valueOf(0),
                        String.valueOf(ywwd)));
                //初始状态 --（"提交"）--> 办结
                configs.add(new CStateMachineConfiguration(BusinessType.Finance,
                        subtype,
                        CollectionBusinessStatus.初始状态.getName(),
                        CollectionBusinessStatus.办结.getName(),
                        StateMachineEvent.SUBMIT,
//                  false,
                        true,
                        "",
                        TransitionKind.EXTERNAL,
                        new Date(),
                        String.valueOf(0),
                        String.valueOf(ywwd)));
                //新建 --（"通过"）--> 办结
                configs.add(new CStateMachineConfiguration(BusinessType.Finance,
                        subtype,
                        CollectionBusinessStatus.新建.getName(),
                        CollectionBusinessStatus.办结.getName(),
                        StateMachineEvent.SUCCESS,
//                  false,
                        true,
                        "",
                        TransitionKind.EXTERNAL,
                        new Date(),
                        String.valueOf(0),
                        String.valueOf(ywwd)));
                //新建 --（"保存"）--> 新建
                configs.add(new CStateMachineConfiguration(BusinessType.Finance,
                        subtype,
                        CollectionBusinessStatus.新建.getName(),
                        CollectionBusinessStatus.新建.getName(),
                        StateMachineEvent.SAVE,
//                  false,
                        true,
                        "",
                        TransitionKind.EXTERNAL,
                        new Date(),
                        String.valueOf(0),
                        String.valueOf(ywwd)));
                //审核不通过 --（"保存"）--> 审核不通过
                configs.add(new CStateMachineConfiguration(BusinessType.Finance,
                        subtype,
                        CollectionBusinessStatus.审核不通过.getName(),
                        CollectionBusinessStatus.审核不通过.getName(),
                        StateMachineEvent.SAVE,
//                  false,
                        true,
                        "",
                        TransitionKind.EXTERNAL,
                        new Date(),
                        String.valueOf(0),
                        String.valueOf(ywwd)));
                //审核不通过 --（"通过"）--> 办结
                configs.add(new CStateMachineConfiguration(BusinessType.Finance,
                        subtype,
                        CollectionBusinessStatus.审核不通过.getName(),
                        CollectionBusinessStatus.办结.getName(),
                        StateMachineEvent.SUCCESS,
//                  false,
                        true,
                        "",
                        TransitionKind.EXTERNAL,
                        new Date(),
                        String.valueOf(0),
                        String.valueOf(ywwd)));
                //审核不通过 --（"不通过"）--> 丢弃
                configs.add(new CStateMachineConfiguration(BusinessType.Finance,
                        subtype,
                        CollectionBusinessStatus.审核不通过.getName(),
                        StateConstants.abandonState.getId(),
                        StateMachineEvent.FAILED,
//                  false,
                        true,
                        "",
                        TransitionKind.EXTERNAL,
                        new Date(),
                        String.valueOf(0),
                        String.valueOf(ywwd)));
            }

            for (CStateMachineConfiguration c : configs) {
                icStateMachineConfigurationDAO.save(c);
                System.out.println(c);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void deleteConfig(BusinessType type, String subtype) {
        HashMap filter = new HashMap();
        filter.put("type", type);
        filter.put("subType", subtype);
        List<CStateMachineConfiguration> smcs = icStateMachineConfigurationDAO.list(FixMap.ignoreNullValue(filter), null, null, null, null, null, null);
        for (CStateMachineConfiguration smc : smcs) {
            icStateMachineConfigurationDAO.delete(smc); //*****
        }
    }

//    /**
//     * 获取所有业务的类型列表
//     * @return
//     */
//    public List<String> listTypes() {
//        List<String> result = new ArrayList<>();
//        try {
//            List<CStateMachineConfiguration> confs = icStateMachineConfigurationDAO.list(null, null, null, null, null, ListDeleted.NOTDELETED, SearchOption.FUZZY);
//            result = confs.stream().map(new Function<CStateMachineConfiguration, String>() {
//                public String apply(CStateMachineConfiguration cStateMachineConfiguration) {
//                    return cStateMachineConfiguration.getType().toString();
//                }
//            }).distinct()
//                    .collect(Collectors.toList());
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//        return result;
//    }
//
//    /**
//     * 获取业务子类型列表
//     * @param type 业务类型
//     * @return
//     */
//    public List<String> listSubTypes(String type) {
//
//        List<String> result = new ArrayList<>();
//        try{
//            HashMap<String, Object> filter = new HashMap<String, Object>();
//            filter.put("type", BusinessType.valueOf(type));
//            List<CStateMachineConfiguration> confs = icStateMachineConfigurationDAO.list(filter, null, null, null, null, ListDeleted.NOTDELETED, SearchOption.REFINED);
//            result = confs.stream().map(new Function<CStateMachineConfiguration, String>() {
//                public String apply(CStateMachineConfiguration cStateMachineConfiguration) {
//                    return cStateMachineConfiguration.getSubType();
//                }
//            }).distinct()
//                    .collect(Collectors.<String>toList());
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//        return result;
//    }
//
//    /**
//     * 获取对应业务的流程配置
//     * @param type 业务类型
//     * @param subType 业务子类型
//     * @return
//     */
//    public List<StateMachineConfModel> listStateMachineConfs(String type, String subType) {
//        List<StateMachineConfModel> listConfigs = new ArrayList<>();
//        try{
//            HashMap<String, Object> filter = new HashMap<String, Object>();
//            filter.put("type", BusinessType.valueOf(type));
//            filter.put("subType",subType);
//            List<CStateMachineConfiguration> confs = icStateMachineConfigurationDAO.list(filter, null, null, null, null, ListDeleted.NOTDELETED, SearchOption.REFINED);
//            listConfigs = confs.stream().map(new Function<CStateMachineConfiguration, StateMachineConfModel>() {
//                public StateMachineConfModel apply(CStateMachineConfiguration cStateMachineConfiguration) {
//                    return readFrom(cStateMachineConfiguration);
//                }
//            })
//                    .distinct()
//                    .collect(Collectors.toList());
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//        return listConfigs;
//    }
//
//
//    /**
//     * 添加业务的状态机流程配置
//     * @param stateMachineConfModel
//     * @return
//     */
//    public Boolean addStateMachineConf(StateMachineConfModel stateMachineConfModel) {
//        try{
//            CStateMachineConfiguration stateMachineConfiguration = readFrom(stateMachineConfModel);
//            icStateMachineConfigurationDAO.save(stateMachineConfiguration);
//        }catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public Boolean addStateMachineConfs(ArrayList arrayList) {
//        try {
//            ArrayList<StateMachineConfModel> confModels = arrayList;
//            confModels.stream().forEach(stateMachineConfModel -> icStateMachineConfigurationDAO.save(readFrom(stateMachineConfModel)));
//        }catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * 更新业务的状态机的流程配置
//     * @param id
//     * @param stateMachineConfModel
//     * @return
//     */
//    public Boolean updateStateMachineConf(String id, StateMachineConfModel stateMachineConfModel) {
//        try{
//            stateMachineConfModel.setId(id);
//            CStateMachineConfiguration stateMachineConfiguration = readFrom(stateMachineConfModel);
//            icStateMachineConfigurationDAO.update(stateMachineConfiguration);
//        }catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * 更新业务网点的流程
//     * @param type 业务类型
//     * @param subtype 业务子类型
//     * @param list 流程列表
//     * @return
//     */
//    @Override
//    public Boolean updateStateMachineConfs(String type, String subtype, List list) {
//        try{
//            List<StateMachineConfModel> confModels = list;
//            confModels.forEach(confModel ->
//                    icStateMachineConfigurationDAO.update(readFrom(confModel)));
//        }catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * 删除业务的状态机的流程配置
//     * @param id 业务流程 id 号
//     * @return
//     */
//    public Boolean deleteStateMachineConf(String id) {
//        try {
//            CStateMachineConfiguration stateMachineConfiguration = new CStateMachineConfiguration();
//            stateMachineConfiguration.setId(id);
//            icStateMachineConfigurationDAO.delete(stateMachineConfiguration);
//        }catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public  List<String> listWorkstation(String type, String subtype) {
//        List<String> result = new ArrayList<>();
//        try{
//            result = icStateMachineConfigurationDAO.list(new HashMap<String, Object>(){{
//                this.put("type",BusinessType.valueOf(type));
//                this.put("subType",subtype);
//            }}, null, null,null, Order.ASC, ListDeleted.NOTDELETED, SearchOption.REFINED)
//                    .stream()
//                    .map(cStateMachineConfiguration -> cStateMachineConfiguration.getWorkstation())
//                    .collect(Collectors.<String>toList());
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//        return result;
//    }
//
//    @Override
//    public List<String> listWorkstationBusinessType(String workstation) {
//        List<String> result = new ArrayList<>();
//        try{
//            result = icStateMachineConfigurationDAO.list(new HashMap<String, Object>(){{
//                this.put("workstation", workstation);
//            }}, null, null,null, Order.ASC, ListDeleted.NOTDELETED, SearchOption.REFINED)
//                    .stream()
//                    .map(cStateMachineConfiguration -> cStateMachineConfiguration.getType().toString())
//                    .collect(Collectors.<String>toList());
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//        return result;
//    }
//
//    @Override
//    public List<String> listWorkstationBusinessSubType(String workstation, String type) {
//        List<String> result = new ArrayList<>();
//        try {
//            result = icStateMachineConfigurationDAO.list(new HashMap<String, Object>() {{
//                this.put("type", BusinessType.valueOf(type));
//                this.put("workstation", workstation);
//            }}, null, null, null, Order.ASC, ListDeleted.NOTDELETED, SearchOption.REFINED)
//                    .stream()
//                    .map(cStateMachineConfiguration -> cStateMachineConfiguration.getSubType())
//                    .collect(Collectors.<String>toList());
//
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//        return result;
//    }
//
//    @Override
//    public List<StateMachineConfModel>  listWorkstationBusinessStateMachineConfs(String workstation, String type, String subType) {
//        List<StateMachineConfModel> result = new ArrayList<>();
//        try {
//            result = icStateMachineConfigurationDAO.list(new HashMap<String, Object>() {{
//                this.put("type", BusinessType.valueOf(type));
//                this.put("subType", subType);
//                this.put("workstation", workstation);
//            }}, null, null, null, Order.ASC, ListDeleted.NOTDELETED, SearchOption.REFINED)
//                    .stream()
//                    .map(cStateMachineConfiguration -> readFrom(cStateMachineConfiguration))
//                    .collect(Collectors.<StateMachineConfModel>toList());
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//        return result;
//    }
//
//    @Override
//    public Boolean addWorkstationBusinessStateMachineConf(String workstation, StateMachineConfModel stateMachineConfModel) {
//        try {
//            stateMachineConfModel.setWorkstation(workstation);
//            icStateMachineConfigurationDAO.save(readFrom(stateMachineConfModel));
//        }catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public Boolean addWorkstationBusinessStateMachineConfs(String workstation, ArrayList arrayList) {
//        try {
//            ArrayList<StateMachineConfModel> conflist = arrayList;
//            conflist.stream()
//                    .forEach(stateMachineConfModel -> icStateMachineConfigurationDAO.save(readFrom(stateMachineConfModel)));
//        }catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public Boolean updateWorkstationBusinessStateMachineConf(String workstation, StateMachineConfModel stateMachineConfModel) {
//        try {
//            stateMachineConfModel.setWorkstation(workstation);
//            CStateMachineConfiguration stateMachineConfiguration = readFrom(stateMachineConfModel);
//            icStateMachineConfigurationDAO.update(stateMachineConfiguration);
//        }catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public Boolean  updateWorkstationBusinessStateMachineConfs(String workstation, ArrayList arrayList) {
//        try {
//            ArrayList<StateMachineConfModel> conflist = arrayList;
//            icStateMachineConfigurationDAO.list(new HashMap<String, Object>() {{
//                this.put("workstation", workstation);
//            }}, null, null, null, Order.ASC, ListDeleted.NOTDELETED, SearchOption.REFINED)
//                    .stream()
//                    .filter(config -> conflist.stream().anyMatch(conf -> conf.getId().equals(config.getId())))
//                    .forEach(cStateMachineConfiguration -> {
//                        icStateMachineConfigurationDAO.update(cStateMachineConfiguration);
//                    });
//        }catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public List<String> listAuditConfsBySubType(String subType, String role, String workstation) {
//        List<StateMachineConfModel> auditConfs = icStateMachineConfigurationDAO.list(new HashMap<String, Object>(){{
//            this.put("subType", subType);
//            this.put("workstation", workstation);
//        }}, null, null, null, Order.ASC, ListDeleted.NOTDELETED, SearchOption.REFINED)
//                .stream()
//                .filter(cStateMachineConfiguration -> !cStateMachineConfiguration.getIsAudit().equals("0"))
//                .map(cStateMachineConfiguration -> readFrom(cStateMachineConfiguration))
//                .collect(Collectors.toList());
//        List<String> sources = new ArrayList<>();
//        for(StateMachineConfModel model : auditConfs) {
//            sources.add(model.getSource());
//        }
//        return sources;
//    }
//
//    @Override
//    public  List<StateMachineConfModel> listAuditConfs(String type, String subType, String role, String workstation) {
//        List<StateMachineConfModel> auditConfs = icStateMachineConfigurationDAO.list(new HashMap<String, Object>(){{
//            this.put("type", BusinessType.valueOf(type));
//            this.put("subType", subType);
//            this.put("workstation", workstation);
//        }}, null, null, null, Order.ASC, ListDeleted.NOTDELETED, SearchOption.REFINED)
//                .stream()
//                .filter(cStateMachineConfiguration ->
//                     !cStateMachineConfiguration.getIsAudit().equals("0"))
//                .filter(cStateMachineConfiguration -> !cStateMachineConfiguration.getIsAudit().equals("0"))
//                .map(cStateMachineConfiguration -> readFrom(cStateMachineConfiguration))
//                .collect(Collectors.toList());
//        return auditConfs;
//    }
//
//    @Deprecated
//    @Override
//    public Object addWorkStation(String workstation) {
//        return null;
//    }
//
//    private CStateMachineConfiguration readFrom(StateMachineConfModel stateMachineConfModel){
//        CStateMachineConfiguration stateMachineConfiguration = new CStateMachineConfiguration();
//        stateMachineConfiguration.setSource(stateMachineConfModel.getSource());
//        stateMachineConfiguration.setEvent(stateMachineConfModel.getEvent());
//        stateMachineConfiguration.setTarget(stateMachineConfModel.getTarget());
//        stateMachineConfiguration.setSubType(stateMachineConfModel.getSubType());
//        stateMachineConfiguration.setType(BusinessType.valueOf(stateMachineConfModel.getType()));
//        stateMachineConfiguration.setTransitionKind(TransitionKind.valueOf(stateMachineConfModel.getTransitionKind()));
//        stateMachineConfiguration.setFlag(false);
//        stateMachineConfiguration.setId(stateMachineConfModel.getId());
//        stateMachineConfiguration.setRole(String.join(",",stateMachineConfModel.getRoles()));
//        stateMachineConfiguration.setWorkstation(stateMachineConfModel.getWorkstation() == null ? null : stateMachineConfModel.getWorkstation());
//        return stateMachineConfiguration;
//    }
//
//    private StateMachineConfModel readFrom(CStateMachineConfiguration cStateMachineConfiguration){
//        return new StateMachineConfModel(cStateMachineConfiguration.getId(),
//                cStateMachineConfiguration.getSource(),
//                cStateMachineConfiguration.getEvent(),
//                cStateMachineConfiguration.getTarget(),
//                cStateMachineConfiguration.getTransitionKind().toString(),
//                Arrays.asList(cStateMachineConfiguration.getRole()),
//                cStateMachineConfiguration.getType().toString(),
//                cStateMachineConfiguration.getSubType());
//    }

}
