package com.handge.housingfund.loan.service;

import com.handge.housingfund.common.service.ISaveAuditHistory;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessStatus;
import com.handge.housingfund.common.service.loan.IEstateProject;
import com.handge.housingfund.common.service.loan.enums.LoanBusinessType;
import com.handge.housingfund.common.service.loan.enums.LoanBussinessStatus;
import com.handge.housingfund.common.service.loan.model.*;
import com.handge.housingfund.common.service.others.IPdfService;
import com.handge.housingfund.common.service.others.IUploadImagesService;
import com.handge.housingfund.common.service.others.enums.UploadFileBusinessType;
import com.handge.housingfund.common.service.others.enums.UploadFileModleType;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.BusinessSubType;
import com.handge.housingfund.database.enums.Events;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.database.utils.DAOBuilder;
import com.handge.housingfund.loan.utils.StateMachineUtils;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import com.handge.housingfund.statemachineV2.IStateMachineService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.handge.housingfund.common.service.util.ReturnEnumeration.Data_MISS;
import static com.handge.housingfund.common.service.util.ReturnEnumeration.Parameter_MISS;
import static com.handge.housingfund.common.service.util.StringUtil.isIntoReview;
import static com.handge.housingfund.common.service.util.StringUtil.timeTransform;
import static com.handge.housingfund.database.enums.BusinessSubType.贷款_楼盘申请受理;
import static com.handge.housingfund.database.enums.BusinessType.Loan;
import static com.handge.housingfund.database.enums.Events.通过;
import static com.handge.housingfund.database.utils.DAOBuilder.ErrorHandler;
import static com.handge.housingfund.database.utils.DAOBuilder.instance;
import static com.handge.housingfund.loan.utils.StateMachineUtils.StateChangeHandler;
import static com.handge.housingfund.loan.utils.StateMachineUtils.updateState;

/**
 * Created by 向超 on 2017/8/9.
 */
@SuppressWarnings("Duplicates")
@Component
public class EstateProjectImpl implements IEstateProject {

    @Autowired
    private ICLoanHousingCompanyBasicDAO icLoanHousingCompanyBasicDAO;
    @Autowired
    private ICLoanHousingBusinessProcessDAO loanHousingBusinessProcessDAO;
    @Autowired
    @Resource(name = "stateMachineServiceV2")
    private IStateMachineService iStateMachineService;
    @Autowired
    private ICLoanEatateProjectBasicDAO loanEatateProjectBasicDAO;
    @Autowired
    private ICLoanBuildingInformationViceDAO icLoanBuildingInformationViceDAO;
    @Autowired
    private ISaveAuditHistory saveAuditHistory;
    @Autowired
    private IPdfService pdfService;
    @Autowired
    private ICAuditHistoryDAO icAuditHistoryDAO;
    @Autowired
    private IUploadImagesService iUploadImagesService;

    @Autowired
    private ICAccountNetworkDAO cAccountNetworkDAO;


    private static final String format = "yyyy-MM-dd HH:mm";

    @Override
    public PageRes getEstateProjectInfo(TokenContext tokenContext, String LPMC, String FKGS, Boolean SFQY, String YWWD, String pageNo, String pageSize) {
        System.out.println("楼盘列表");
        PageRes pageRes = new PageRes();
        int page_number = 1;
        int pagesize_number = 10;
        try {
            if (!StringUtil.isEmpty(pageNo)) {
                page_number = Integer.parseInt(pageNo);
            }
            if (!StringUtil.isEmpty(pageSize)) {
                pagesize_number = Integer.parseInt(pageSize);
            }
        } catch (Exception e) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "页码");
        }
        List<CLoanEatateProjectBasic> cLoanEatateProjectBasics = DAOBuilder.instance(loanEatateProjectBasicDAO).searchFilter(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(LPMC)) this.put("lpmc", LPMC);
            if (StringUtil.notEmpty(FKGS)) this.put("cLoanHousingCompanyBasic.fkgs", FKGS);
            if (SFQY != null) this.put("sfqy", SFQY);
        }}).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                if(StringUtil.notEmpty(YWWD)){
                    criteria.add(Restrictions.eq("ywwd",YWWD));
                }
            }
        }).pageOption(pageRes, pagesize_number, page_number).searchOption(SearchOption.FUZZY).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        return new PageRes<EstateProjectListRes>() {{
            this.setResults(new ArrayList<EstateProjectListRes>() {{
                for (CLoanEatateProjectBasic cLoanEatateProjectBasic : cLoanEatateProjectBasics) {
                    this.add(new EstateProjectListRes() {{
                        if (cLoanEatateProjectBasic.getBzjbl() != null)
                            this.setBZJBL(cLoanEatateProjectBasic.getBzjbl().toPlainString());//保证金比例

                        this.setLPMC(cLoanEatateProjectBasic.getLpmc());//楼盘名称

                        this.setLPBH(cLoanEatateProjectBasic.getLpbh());//楼盘编号

                        this.setFKGS(cLoanEatateProjectBasic.getcLoanHousingCompanyBasic().getFkgs());//房开公司

                        this.setFKGSZH(cLoanEatateProjectBasic.getcLoanHousingCompanyBasic().getFkgszh());//房开公司账号
                        CAccountNetwork network = DAOBuilder.instance(cAccountNetworkDAO).searchFilter(new HashMap<String, Object>() {
                            {
                                this.put("id", cLoanEatateProjectBasic.getYwwd());
                            }
                        }).getObject(new DAOBuilder.ErrorHandler() {
                            @Override
                            public void error(Exception e) {
                                throw new ErrorException(e);
                            }
                        });

                        this.setYWWD(network.getMingCheng());//业务网点

                        this.setLPDZ(cLoanEatateProjectBasic.getLpdz());//楼盘地址

                        this.setCZY(cLoanEatateProjectBasic.getCzy());//操作员
                        if (cLoanEatateProjectBasic.getSfqy() != null) {
                            if (cLoanEatateProjectBasic.getSfqy())
                                this.setSFQY("是");//是否启用
                            else
                                this.setSFQY("否");//是否启用
                        }
                        this.setSLSJ(DateUtil.date2Str(cLoanEatateProjectBasic.getCreated_at(), format));//受理时间
                    }});
                }
            }});
            this.setCurrentPage(pageRes.getCurrentPage());

            this.setNextPageNo(pageRes.getNextPageNo());

            this.setPageCount(pageRes.getPageCount());

            this.setTotalCount(pageRes.getTotalCount());

            this.setPageSize(pageRes.getPageSize());
        }};
    }


    //测试通过xc
    @Override
    public CommonResponses addEstateProject(TokenContext tokenContext, String CZLX, EstateProjectInfo estateProjectInfo) {
        System.out.println("添加楼盘信息");
        if (CZLX.equals("1") && !iUploadImagesService.validateUploadFile(UploadFileModleType.贷款.getCode(),
                UploadFileBusinessType.楼盘项目申请.getCode(), estateProjectInfo.getBLZL())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "上传资料");
        }
        CLoanHousingCompanyBasic cLoanHousingCompanyBasic = DAOBuilder.instance(icLoanHousingCompanyBasicDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("fkgs", estateProjectInfo.getLPXX().getFKGS());
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (cLoanHousingCompanyBasic == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "房开公司不存在");
        }
        if (!cLoanHousingCompanyBasic.getYwwd().equals(tokenContext.getUserInfo().getYWWD())) {
            throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务不在当前网点受理范围内");
        }
        List<CLoanEatateProjectBasic> loanEatateProjectBasicList = DAOBuilder.instance(loanEatateProjectBasicDAO).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        for (CLoanEatateProjectBasic cLoanEatateProjectBasic : loanEatateProjectBasicList) {
            if (cLoanEatateProjectBasic.getLpmc().equals(estateProjectInfo.getLPXX().getLPMC())) {
                throw new ErrorException(ReturnEnumeration.User_Defined, "楼盘名称已存在");
            }
        }
        CLoanEatateProjectVice cLoanEatateProjectVice = new CLoanEatateProjectVice();
        cLoanEatateProjectVice.setFkgs(estateProjectInfo.getLPXX().getFKGS());//房开公司
        cLoanEatateProjectVice.setLpmc(estateProjectInfo.getLPXX().getLPMC());//楼盘名称
        cLoanEatateProjectVice.setLpdz(estateProjectInfo.getLPXX().getLPDZ());//楼盘地址
        cLoanEatateProjectVice.setYsxkzh(estateProjectInfo.getLPXX().getYSXKZH());//预售许可证号
        cLoanEatateProjectVice.setHqtddj(new BigDecimal(StringUtil.isEmpty(estateProjectInfo.getLPXX().getHQTDDJ()) ? "0" : estateProjectInfo.getLPXX().getHQTDDJ()));//获取土地单价
        cLoanEatateProjectVice.setHqtdzj(new BigDecimal(StringUtil.isEmpty(estateProjectInfo.getLPXX().getHQTDZJ()) ? "0" : estateProjectInfo.getLPXX().getHQTDZJ()));//获取土地总价
        cLoanEatateProjectVice.setBzjbl(new BigDecimal(StringUtil.isEmpty(estateProjectInfo.getLPXX().getBZJBL()) ? "0" : estateProjectInfo.getLPXX().getBZJBL()));//保证金比例
        cLoanEatateProjectVice.setJzzmj(new BigDecimal(StringUtil.isEmpty(estateProjectInfo.getLPXX().getJZZMJ()) ? "0" : estateProjectInfo.getLPXX().getJZZMJ()));//建筑总面积
        cLoanEatateProjectVice.setJzzje(new BigDecimal(StringUtil.isEmpty(estateProjectInfo.getLPXX().getJZZJE()) ? "0" : estateProjectInfo.getLPXX().getJZZJE()));//建筑总金额
        if (StringUtil.notEmpty(estateProjectInfo.getLPXX().getAJXYRQ())) {
            try {
                cLoanEatateProjectVice.setAjxyrq(new SimpleDateFormat("yyyy-MM-dd").parse(estateProjectInfo.getLPXX().getAJXYRQ()));//按揭协议日期
            } catch (ParseException e) {
                throw new ErrorException(e);
            }
        }
        cLoanEatateProjectVice.setLxr(estateProjectInfo.getLPXX().getLXR());//联系人
        cLoanEatateProjectVice.setLxdh(estateProjectInfo.getLPXX().getLXDH());//联系人电话
        cLoanEatateProjectVice.setBeiZhu(estateProjectInfo.getLPXX().getBeiZhu());//备注


        List<CLoanBuildingInformationVice> loanBuildingInformationVices = new ArrayList<>();
        checkYSXKZUniq(estateProjectInfo.getLDXX().getestateDetail());
        for (EstateProjectInfoLDXXEstateDetail estateProjectInfoLDXXEstateDetail : estateProjectInfo.getLDXX().getestateDetail()) {
            CLoanBuildingInformationVice cLoanBuildingInformationVice = new CLoanBuildingInformationVice();
            cLoanBuildingInformationVice.setLdmh(estateProjectInfoLDXXEstateDetail.getLDMH());
            cLoanBuildingInformationVice.setDkbl(new BigDecimal(StringUtil.isEmpty(estateProjectInfoLDXXEstateDetail.getDKBL()) ? "0" : estateProjectInfoLDXXEstateDetail.getDKBL()));
            try {
                if (StringUtil.notEmpty(estateProjectInfoLDXXEstateDetail.getJGRQ()))
                    cLoanBuildingInformationVice.setJgrq(DateUtil.str2Date("yyyy-MM-dd", estateProjectInfoLDXXEstateDetail.getJGRQ()));
            } catch (ParseException e) {
                throw new ErrorException(e);
            }
            try {
                if (StringUtil.notEmpty(estateProjectInfoLDXXEstateDetail.getXYRQ()))
                    cLoanBuildingInformationVice.setXyrq(DateUtil.str2Date("yyyy-MM-dd", estateProjectInfoLDXXEstateDetail.getXYRQ()));
            } catch (ParseException e) {
                throw new ErrorException(e);
            }
            cLoanBuildingInformationVice.setKsdyh(new BigDecimal(StringUtil.isEmpty(estateProjectInfoLDXXEstateDetail.getKSDYH()) ? "0" : estateProjectInfoLDXXEstateDetail.getKSDYH()));
            cLoanBuildingInformationVice.setDys(new BigDecimal(StringUtil.isEmpty(estateProjectInfoLDXXEstateDetail.getDYS()) ? "0" : estateProjectInfoLDXXEstateDetail.getDYS()));
            cLoanBuildingInformationVice.setBeiZhu(estateProjectInfoLDXXEstateDetail.getBeiZhu());
            cLoanBuildingInformationVice.setYsxkz(estateProjectInfoLDXXEstateDetail.getYSXKZ());
            cLoanBuildingInformationVice.setLoanEatateProjectVice(cLoanEatateProjectVice);
            loanBuildingInformationVices.add(cLoanBuildingInformationVice);
        }
        //构造房开公司副表信息
        CLoanHousingCompanyBasic cLoanHousingCompanyBasic1 = cLoanHousingCompanyBasic;
        CLoanHousingCompanyVice cLoanHousingCompanyVice = new CLoanHousingCompanyVice();
        cLoanHousingCompanyVice.setFkgs(cLoanHousingCompanyBasic1.getFkgs());//房开公司
        cLoanHousingCompanyVice.setSjflb(cLoanHousingCompanyBasic1.getSjflb());//售建房类别
        cLoanHousingCompanyVice.setZzdj(cLoanHousingCompanyBasic1.getZzdj());//资质等级
        cLoanHousingCompanyVice.setDwdz(cLoanHousingCompanyBasic1.getDwdz());//单位地址
        cLoanHousingCompanyVice.setZcdz(cLoanHousingCompanyBasic1.getZcdz());//注册地址
        cLoanHousingCompanyVice.setZczj(cLoanHousingCompanyBasic1.getZczj());//注册资金
        cLoanHousingCompanyVice.setFrdb(cLoanHousingCompanyBasic1.getFrdb());//法人代表
        cLoanHousingCompanyVice.setFrdbzjlx(cLoanHousingCompanyBasic1.getFrdbzjlx());//法人代表证件类型（0：身份证 1：军官证 2：护照 3：外国人永久居留证 4：其他）
        cLoanHousingCompanyVice.setFrdbzjhm(cLoanHousingCompanyBasic1.getFrdbzjhm());//法人代表证件号码


        List<CLoanHousingCompanyViceExtension> loanHousingCompanyViceExtensions = new ArrayList<>();
        for (CLoanHousingCompanyBasicExtension cLoanHousingCompanyBasicExtension : cLoanHousingCompanyBasic1.getcLoanHousingCompanyBasicExtensions()) {
            CLoanHousingCompanyViceExtension cLoanHousingCompanyViceExtension = new CLoanHousingCompanyViceExtension();
            cLoanHousingCompanyViceExtension.setFkgsfb(cLoanHousingCompanyVice);
            cLoanHousingCompanyViceExtension.setSFRKHYHMC(cLoanHousingCompanyBasicExtension.getSFRKHYHMC());//售房人开户银行名称
            cLoanHousingCompanyViceExtension.setSFRZHHM(cLoanHousingCompanyBasicExtension.getSFRZHHM());//售房人账户号码
            cLoanHousingCompanyViceExtension.setSFRKHYHKHM(cLoanHousingCompanyBasicExtension.getSFRKHYHKHM());//售房人开户银行开户名
            loanHousingCompanyViceExtensions.add(cLoanHousingCompanyViceExtension);
        }

        cLoanHousingCompanyVice.setLoanHousingCompanyViceExtensions(loanHousingCompanyViceExtensions);

        cLoanHousingCompanyVice.setLxr(cLoanHousingCompanyBasic1.getLxr());//联系人
        cLoanHousingCompanyVice.setLxdh(cLoanHousingCompanyBasic1.getLxdh());//联系电话
        cLoanHousingCompanyVice.setZzjgdm(cLoanHousingCompanyBasic1.getZzjgdm());//组织机构代码
        cLoanHousingCompanyVice.setBeiZhu(cLoanHousingCompanyBasic1.getBeiZhu());//备注
        cLoanHousingCompanyVice.setBlzl(cLoanHousingCompanyBasic1.getBlzl());//办理资料
        cLoanHousingCompanyVice.setBzjzh(cLoanHousingCompanyBasic1.getBzjzh());//保证金账户
        cLoanHousingCompanyVice.setBzjzhkhh(cLoanHousingCompanyBasic1.getBzjzhkhh());//保证金账户开户行
        cLoanHousingCompanyVice.setBzjkhm(cLoanHousingCompanyBasic1.getBzjkhm());//保证金开户名
        cLoanHousingCompanyVice.setSfbg(cLoanHousingCompanyBasic1.getSfbg());//是否变更
        cLoanHousingCompanyVice.setFkgszh(cLoanHousingCompanyBasic1.getFkgszh());//房开公司账号


        cLoanEatateProjectVice.setLoanBuildingInformationVices(loanBuildingInformationVices);//设置楼栋信息
        cLoanEatateProjectVice.setBlzl(estateProjectInfo.getBLZL());//办理资料

        cLoanEatateProjectVice.setLoanHousingCompanyVice(cLoanHousingCompanyVice);//设置房开公司信息

        CLoanHousingBusinessProcess cLoanHousingBusinessProcess = new CLoanHousingBusinessProcess();//构造业务表
        cLoanHousingBusinessProcess.setLoanEatateProjectVice(cLoanEatateProjectVice);
        cLoanHousingBusinessProcess.setBlzl(estateProjectInfo.getBLZL());//办理资料
        cLoanHousingBusinessProcess.setCznr(LoanBusinessType.新建楼盘.getCode());//操作类容
        cLoanHousingBusinessProcess.setCzy(tokenContext.getUserInfo().getCZY());//操作员
        CAccountNetwork network = DAOBuilder.instance(cAccountNetworkDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("id", tokenContext.getUserInfo().getYWWD());
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        cLoanHousingBusinessProcess.setYwwd(network);//业务网点
        cLoanHousingBusinessProcess.setStep(LoanBussinessStatus.初始状态.getName());
        if (!Arrays.asList("0", "1").contains(CZLX))
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "操作类型异常");

        cLoanHousingCompanyVice.setGrywmx(cLoanHousingBusinessProcess);
        cLoanEatateProjectVice.setGrywmx(cLoanHousingBusinessProcess);

        CLoanHousingBusinessProcess cLoanHousingBusinessProcess1 = instance(loanHousingBusinessProcessDAO).entity(cLoanHousingBusinessProcess).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (CZLX.equals("1")) {
            //数据库唯一验证
            CLoanEatateProjectBasic cLoanEatateProjectBasic = DAOBuilder.instance(loanEatateProjectBasicDAO).extension(new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    criteria.add(Restrictions.or(Restrictions.eq("lpmc", estateProjectInfo.getLPXX().getLPMC()), Restrictions.eq("lpdz", estateProjectInfo.getLPXX().getLPDZ())));
                }
            }).getObject(new ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            if (cLoanEatateProjectBasic != null) {
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "楼盘已存在，请勿重复添加");
            }
        }
        StateMachineUtils.updateState(this.iStateMachineService, new HashMap<String, String>() {{
            this.put("0", 通过.getEvent());
            this.put("1", Events.提交.getEvent());
        }}.get(CZLX), new TaskEntity(cLoanHousingBusinessProcess1.getYwlsh(), cLoanHousingBusinessProcess1.getStep(), new HashSet<String>(tokenContext.getRoleList()),
                cLoanHousingBusinessProcess1.getCzy(), cLoanHousingCompanyVice.getBeiZhu(), 贷款_楼盘申请受理.getSubType(), com.handge.housingfund.database.enums.BusinessType.Loan, cLoanHousingBusinessProcess1.getYwwd().getId()), new StateMachineUtils.StateChangeHandler() {
            @Override
            public void onStateChange(boolean succeed, String next, Exception e) {
                if (e != null) {
                    throw new ErrorException(e);
                }
                if (!StringUtil.notEmpty(next) || e != null) {
                    return;
                }

                if (succeed) {
                    //缺少step
                    cLoanHousingBusinessProcess1.setStep(next);
                    if (StringUtil.isIntoReview(next, null)) cLoanHousingBusinessProcess1.setDdsj(new Date());
                    instance(loanHousingBusinessProcessDAO).entity(cLoanHousingBusinessProcess1).save(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
                            throw new ErrorException(e);
                        }
                    });
                    if (next.equals(LoanBussinessStatus.办结.getName()))
                        doEstateProject(cLoanHousingBusinessProcess1.getYwlsh());
                }
            }
        });
        if (CZLX.equals("1")) {
            checkParam(cLoanHousingBusinessProcess1);
            //在途验证
            CLoanHousingBusinessProcess cLoanHousingBusinessProcessOR = instance(loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{
                // this.put("loanEatateProjectVice.lpmc", estateProjectInfo.getLPXX().getLPMC());
                // this.put("loanEatateProjectVice.lpdz", estateProjectInfo.getLPXX().getLPDZ());
                this.put("cznr", LoanBusinessType.新建楼盘.getCode());
            }}).extension(new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    criteria.add(Restrictions.ne("ywlsh", cLoanHousingBusinessProcess1.getYwlsh()));
                    criteria.createAlias("loanEatateProjectVice", "loanEatateProjectVice");
                    criteria.add(Restrictions.or(Restrictions.eq("loanEatateProjectVice.lpmc", estateProjectInfo.getLPXX().getLPMC()), Restrictions.eq("loanEatateProjectVice.lpdz", estateProjectInfo.getLPXX().getLPDZ())));
                    criteria.add(Restrictions.like("step", LoanBussinessStatus.待某人审核.getName()));
                }
            }).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            if (cLoanHousingBusinessProcessOR != null) {
                throw new ErrorException(ReturnEnumeration.Business_In_Process);
            }
        }
        saveAuditHistory.saveNormalBusiness(cLoanHousingBusinessProcess1.getYwlsh(), tokenContext, LoanBusinessType.新建楼盘.getName(), "新建");

        return new CommonResponses() {{
            this.setId(cLoanHousingBusinessProcess1.getYwlsh());
            this.setState("success");
        }};
    }

    @Override
    public CommonResponses reEstateProjectInfo(TokenContext tokenContext, String CZLX, String YWLSH, EstatePut estateProjectInfo) {
        System.out.println("修改楼盘信息");

        CLoanHousingBusinessProcess cLoanHousingBusinessProcess = instance(loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("ywlsh", YWLSH);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (cLoanHousingBusinessProcess == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应的业务");
        }
        if (!tokenContext.getUserInfo().getCZY().equals(cLoanHousingBusinessProcess.getCzy())) {
            throw new ErrorException(ReturnEnumeration.Permission_Denied);
        }
        if (CZLX.equals("1") && !iUploadImagesService.validateUploadFile(UploadFileModleType.贷款.getCode(),
                UploadFileBusinessType.楼盘项目申请.getCode(), estateProjectInfo.getBLZL())) {
            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "上传资料");
        }
        CLoanEatateProjectVice cLoanEatateProjectVice = cLoanHousingBusinessProcess.getLoanEatateProjectVice();
        cLoanEatateProjectVice.setFkgs(estateProjectInfo.getLPXX().getFKGS());//房开公司
        CLoanHousingCompanyBasic cLoanHousingCompanyBasic = DAOBuilder.instance(icLoanHousingCompanyBasicDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("fkgs", estateProjectInfo.getLPXX().getFKGS());
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (cLoanHousingCompanyBasic == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "房开公司不存在");
        }
        if (!cLoanHousingCompanyBasic.getYwwd().equals(tokenContext.getUserInfo().getYWWD())) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "房开公司与楼盘不在同一网点，请更换房开公司");
        }
        cLoanEatateProjectVice.setLpmc(estateProjectInfo.getLPXX().getLPMC());//楼盘名称
        cLoanEatateProjectVice.setLpdz(estateProjectInfo.getLPXX().getLPDZ());//楼盘地址
        cLoanEatateProjectVice.setYsxkzh(estateProjectInfo.getLPXX().getYSXKZH());//预售许可证号

        cLoanEatateProjectVice.setHqtddj(new BigDecimal(StringUtil.isEmpty(estateProjectInfo.getLPXX().getHQTDDJ()) ? "0" : estateProjectInfo.getLPXX().getHQTDDJ()));//获取土地单价
        cLoanEatateProjectVice.setHqtdzj(new BigDecimal(StringUtil.isEmpty(estateProjectInfo.getLPXX().getHQTDZJ()) ? "0" : estateProjectInfo.getLPXX().getHQTDZJ()));//获取土地总价
        cLoanEatateProjectVice.setBzjbl(new BigDecimal(StringUtil.isEmpty(estateProjectInfo.getLPXX().getBZJBL()) ? "0" : estateProjectInfo.getLPXX().getBZJBL()));//保证金比例
        cLoanEatateProjectVice.setJzzmj(new BigDecimal(StringUtil.isEmpty(estateProjectInfo.getLPXX().getJZZMJ()) ? "0" : estateProjectInfo.getLPXX().getJZZMJ()));//建筑总面积
        cLoanEatateProjectVice.setJzzje(new BigDecimal(StringUtil.isEmpty(estateProjectInfo.getLPXX().getJZZJE()) ? "0" : estateProjectInfo.getLPXX().getJZZJE()));//建筑总金额
        try {
            if (!StringUtil.isEmpty(estateProjectInfo.getLPXX().getAJXYRQ()))
                cLoanEatateProjectVice.setAjxyrq(new SimpleDateFormat("yyyy-MM-dd").parse(estateProjectInfo.getLPXX().getAJXYRQ()));//按揭协议日期
        } catch (ParseException e) {
            throw new ErrorException(e);
        }
        cLoanEatateProjectVice.setLxr(estateProjectInfo.getLPXX().getLXR());//联系人
        cLoanEatateProjectVice.setLxdh(estateProjectInfo.getLPXX().getLXDH());//联系人电话
        cLoanEatateProjectVice.setBeiZhu(estateProjectInfo.getLPXX().getBeiZhu());//备注

        List<CLoanBuildingInformationVice> loanBuildingInformationVices = new ArrayList<>();
        checkYSXKZUniqPut(estateProjectInfo.getLDXX().getestateDetail());
        for (EstatePutLDXXEstateDetail estateProjectInfoLDXXEstateDetail : estateProjectInfo.getLDXX().getestateDetail()) {
            CLoanBuildingInformationVice cLoanBuildingInformationVice = new CLoanBuildingInformationVice();
            cLoanBuildingInformationVice.setLdmh(estateProjectInfoLDXXEstateDetail.getLDMH());
            cLoanBuildingInformationVice.setDkbl(new BigDecimal(StringUtil.isEmpty(estateProjectInfoLDXXEstateDetail.getDKBL()) ? "0" : estateProjectInfoLDXXEstateDetail.getDKBL()));
            try {
                if (!StringUtil.isEmpty(estateProjectInfoLDXXEstateDetail.getJGRQ()))
                    cLoanBuildingInformationVice.setJgrq(new SimpleDateFormat("yyyy-MM-dd").parse(estateProjectInfoLDXXEstateDetail.getJGRQ()));
            } catch (ParseException e) {
                throw new ErrorException(e);
            }
            try {
                if (!StringUtil.isEmpty(estateProjectInfoLDXXEstateDetail.getXYRQ()))
                    cLoanBuildingInformationVice.setXyrq(new SimpleDateFormat("yyyy-MM-dd").parse(estateProjectInfoLDXXEstateDetail.getXYRQ()));
            } catch (ParseException e) {
                throw new ErrorException(e);
            }
            cLoanBuildingInformationVice.setKsdyh(new BigDecimal(StringUtil.isEmpty(estateProjectInfoLDXXEstateDetail.getKSDYH()) ? "0" : estateProjectInfoLDXXEstateDetail.getKSDYH()));
            cLoanBuildingInformationVice.setDys(new BigDecimal(StringUtil.isEmpty(estateProjectInfoLDXXEstateDetail.getDYS()) ? "0" : estateProjectInfoLDXXEstateDetail.getDYS()));
            cLoanBuildingInformationVice.setBeiZhu(estateProjectInfoLDXXEstateDetail.getBeiZhu());
            cLoanBuildingInformationVice.setYsxkz(estateProjectInfoLDXXEstateDetail.getYSXKZ());
            cLoanBuildingInformationVice.setLoanEatateProjectVice(cLoanEatateProjectVice);
            loanBuildingInformationVices.add(cLoanBuildingInformationVice);
        }

        //删除以前存在的楼栋信息
        List<CLoanBuildingInformationVice> cLoanBuildingInformationVices = cLoanEatateProjectVice.getLoanBuildingInformationVices();
        cLoanBuildingInformationVices.size();

        cLoanEatateProjectVice.setLoanBuildingInformationVices(loanBuildingInformationVices);//设置楼栋信息
        cLoanEatateProjectVice.setBlzl(estateProjectInfo.getBLZL());//办理资料

        //构造房开公司副表信息

        CLoanHousingCompanyBasic cLoanHousingCompanyBasic1 = DAOBuilder.instance(icLoanHousingCompanyBasicDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("fkgs", estateProjectInfo.getLPXX().getFKGS());
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        cLoanEatateProjectVice.getLoanHousingCompanyVice().setFkgs(cLoanHousingCompanyBasic1.getFkgs());//房开公司
        cLoanEatateProjectVice.getLoanHousingCompanyVice().setSjflb(cLoanHousingCompanyBasic1.getSjflb());//售建房类别
        cLoanEatateProjectVice.getLoanHousingCompanyVice().setZzdj(cLoanHousingCompanyBasic1.getZzdj());//资质等级
        cLoanEatateProjectVice.getLoanHousingCompanyVice().setDwdz(cLoanHousingCompanyBasic1.getDwdz());//单位地址
        cLoanEatateProjectVice.getLoanHousingCompanyVice().setZcdz(cLoanHousingCompanyBasic1.getZcdz());//注册地址
        cLoanEatateProjectVice.getLoanHousingCompanyVice().setZczj(cLoanHousingCompanyBasic1.getZczj());//注册资金
        cLoanEatateProjectVice.getLoanHousingCompanyVice().setFrdb(cLoanHousingCompanyBasic1.getFrdb());//法人代表
        cLoanEatateProjectVice.getLoanHousingCompanyVice().setFrdbzjlx(cLoanHousingCompanyBasic1.getFrdbzjlx());//法人代表证件类型（0：身份证 1：军官证 2：护照 3：外国人永久居留证 4：其他）
        cLoanEatateProjectVice.getLoanHousingCompanyVice().setFrdbzjhm(cLoanHousingCompanyBasic1.getFrdbzjhm());//法人代表证件号码

        cLoanEatateProjectVice.getLoanHousingCompanyVice().setLxr(cLoanHousingCompanyBasic1.getLxr());//联系人
        cLoanEatateProjectVice.getLoanHousingCompanyVice().setLxdh(cLoanHousingCompanyBasic1.getLxdh());//联系电话
        cLoanEatateProjectVice.getLoanHousingCompanyVice().setZzjgdm(cLoanHousingCompanyBasic1.getZzjgdm());//组织机构代码
        cLoanEatateProjectVice.getLoanHousingCompanyVice().setBeiZhu(cLoanHousingCompanyBasic1.getBeiZhu());//备注
        cLoanEatateProjectVice.getLoanHousingCompanyVice().setBlzl(cLoanHousingCompanyBasic1.getBlzl());//办理资料
        cLoanEatateProjectVice.getLoanHousingCompanyVice().setBzjzh(cLoanHousingCompanyBasic1.getBzjzh());//保证金账户
        cLoanEatateProjectVice.getLoanHousingCompanyVice().setBzjzhkhh(cLoanHousingCompanyBasic1.getBzjzhkhh());//保证金账户开户行
        cLoanEatateProjectVice.getLoanHousingCompanyVice().setBzjkhm(cLoanHousingCompanyBasic1.getBzjkhm());//保证金开户名
        cLoanEatateProjectVice.getLoanHousingCompanyVice().setSfbg(cLoanHousingCompanyBasic1.getSfbg());//是否变更
        cLoanEatateProjectVice.getLoanHousingCompanyVice().setFkgszh(cLoanHousingCompanyBasic1.getFkgszh());//房开公司账号

        cLoanHousingBusinessProcess.setLoanEatateProjectVice(cLoanEatateProjectVice);

        cLoanHousingBusinessProcess.setCzy(tokenContext.getUserInfo().getCZY());//操作员
        cLoanHousingBusinessProcess.setBlzl(estateProjectInfo.getBLZL());//办理资料
        CAccountNetwork network = DAOBuilder.instance(cAccountNetworkDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("id", tokenContext.getUserInfo().getYWWD());
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        cLoanHousingBusinessProcess.setYwwd(network);//业务网点
        CLoanHousingBusinessProcess cLoanHousingBusinessProcess1 = instance(loanHousingBusinessProcessDAO).entity(cLoanHousingBusinessProcess).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (CZLX.equals("1")) {
            //数据库唯一验证
            CLoanEatateProjectBasic cLoanEatateProjectBasic = DAOBuilder.instance(loanEatateProjectBasicDAO).extension(new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    criteria.add(Restrictions.or(Restrictions.eq("lpmc", estateProjectInfo.getLPXX().getLPMC()), Restrictions.eq("lpdz", estateProjectInfo.getLPXX().getLPDZ())));
                }
            }).getObject(new ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            if (cLoanEatateProjectBasic != null) {
                throw new ErrorException(ReturnEnumeration.User_Defined, "楼盘已存在，请勿重复添加");
            }
        }

        StateMachineUtils.updateState(this.iStateMachineService, new HashMap<String, String>() {{
            this.put("0", Events.保存.getEvent());
            this.put("1", Events.通过.getEvent());
        }}.get(CZLX), new TaskEntity(cLoanHousingBusinessProcess.getYwlsh(), cLoanHousingBusinessProcess.getStep(), new HashSet<String>(tokenContext.getRoleList()),
                tokenContext.getUserInfo().getCZY(), cLoanEatateProjectVice.getBeiZhu(), BusinessSubType.贷款_楼盘申请受理.getSubType(), com.handge.housingfund.database.enums.BusinessType.Loan, tokenContext.getUserInfo().getYWWD()), new StateMachineUtils.StateChangeHandler() {
            @Override
            public void onStateChange(boolean succeed, String next, Exception e) {
                if (e != null) {
                    throw new ErrorException(e);
                }
                if (!StringUtil.notEmpty(next) || e != null) {
                    return;
                }

                if (succeed) {
                    //缺少step
                    cLoanHousingBusinessProcess.setStep(next);
                    if (StringUtil.isIntoReview(next, null)) cLoanHousingBusinessProcess.setDdsj(new Date());
                    instance(loanHousingBusinessProcessDAO).entity(cLoanHousingBusinessProcess).save(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
                            throw new ErrorException(e);
                        }
                    });
                    if (next.equals(LoanBussinessStatus.办结.getName())) doEstateProject(YWLSH);
                }
            }
        });
        DAOBuilder.instance(icLoanBuildingInformationViceDAO).entities(cLoanBuildingInformationVices).delete(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (CZLX.equals("1")) {
            checkParam(cLoanHousingBusinessProcess1);
            saveAuditHistory.saveNormalBusiness(cLoanHousingBusinessProcess.getYwlsh(), tokenContext, LoanBusinessType.新建楼盘.getName(), "修改");
            //在途验证
            CLoanHousingBusinessProcess cLoanHousingBusinessProcessOR = instance(loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{
                // this.put("loanEatateProjectVice.lpmc", estateProjectInfo.getLPXX().getLPMC());
                // this.put("loanEatateProjectVice.lpdz", estateProjectInfo.getLPXX().getLPDZ());
                this.put("cznr", LoanBusinessType.新建楼盘.getCode());
            }}).extension(new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    criteria.add(Restrictions.ne("ywlsh", YWLSH));
                    criteria.createAlias("loanEatateProjectVice", "loanEatateProjectVice");
                    criteria.add(Restrictions.or(Restrictions.eq("loanEatateProjectVice.lpmc", estateProjectInfo.getLPXX().getLPMC()), Restrictions.eq("loanEatateProjectVice.lpdz", estateProjectInfo.getLPXX().getLPDZ())));
                    criteria.add(Restrictions.like("step", LoanBussinessStatus.待某人审核.getName()));
                }
            }).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            if (cLoanHousingBusinessProcessOR != null) {
                throw new ErrorException(ReturnEnumeration.Business_In_Process);
            }
        }

        return new CommonResponses() {{
            this.setId(YWLSH);
            this.setState("success");
        }};
    }

    //测试通过
    @Override
    public EstateIdGet showEstateProjectInfo(String YWLSH) {
        System.out.println("楼盘详情回执单");

        CLoanHousingBusinessProcess cLoanHousingBusinessProcess = instance(loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("ywlsh", YWLSH);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (cLoanHousingBusinessProcess == null || cLoanHousingBusinessProcess.getLoanEatateProjectVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }
        //构造pdf 模型
        EstateIdGet estateIdGet = new EstateIdGet();

        //获取审核人信息
        CAuditHistory cAuditHistory = DAOBuilder.instance(icAuditHistoryDAO).searchFilter(new HashMap<String, Object>() {
            {
                this.put("ywlsh", YWLSH);
                this.put("shjg", "01");
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (cAuditHistory != null) {
            estateIdGet.setSHR(cAuditHistory.getCzy());
        }

        CLoanEatateProjectVice cLoanEatateProjectVice = cLoanHousingBusinessProcess.getLoanEatateProjectVice();

        estateIdGet.setJZZJE(cLoanEatateProjectVice.getJzzje().toPlainString());//建筑总金额

        estateIdGet.setHQTDDJ(cLoanEatateProjectVice.getHqtddj().toPlainString());//获取土地单价

        estateIdGet.setYWLSH(cLoanHousingBusinessProcess.getYwlsh());//业务流水号

        estateIdGet.setmanagerInformation(new EstateIdGetManagerInformation() {{
            this.setCZY(cLoanHousingBusinessProcess.getCzy());
            this.setYWWD(cLoanHousingBusinessProcess.getYwwd().getMingCheng());
        }});
        estateIdGet.setYSXKZH(cLoanEatateProjectVice.getYsxkzh());//预售许可证号

        estateIdGet.setLXR(cLoanEatateProjectVice.getLxr());//联系人

        estateIdGet.setLXDH(cLoanEatateProjectVice.getLxdh());//联系电话
        EstateIdGetLDXX estateIdGetLDXX = new EstateIdGetLDXX();
        ArrayList<EstateIdGetLDXXEstateDetail> estateIdGetLDXXEstateDetails = new ArrayList<EstateIdGetLDXXEstateDetail>();
        for (CLoanBuildingInformationVice cLoanBuildingInformationVice : cLoanEatateProjectVice.getLoanBuildingInformationVices()) {
            if (!cLoanBuildingInformationVice.isDeleted()) {
                EstateIdGetLDXXEstateDetail estateIdGetLDXXEstateDetail = new EstateIdGetLDXXEstateDetail();
                estateIdGetLDXXEstateDetail.setYSXKZ(cLoanBuildingInformationVice.getYsxkz());
                estateIdGetLDXXEstateDetail.setLDMH(cLoanBuildingInformationVice.getLdmh());//楼栋名/号
                if (cLoanBuildingInformationVice.getJgrq() != null)
                    estateIdGetLDXXEstateDetail.setJGRQ(DateUtil.date2Str(cLoanBuildingInformationVice.getJgrq(), "yyyy-MM-dd"));//竣工日期

                estateIdGetLDXXEstateDetail.setKSDYH(cLoanBuildingInformationVice.getKsdyh().toPlainString());//开始单元号

                estateIdGetLDXXEstateDetail.setBeiZhu(cLoanBuildingInformationVice.getBeiZhu());//备注

                estateIdGetLDXXEstateDetail.setXYRQ(DateUtil.date2Str(cLoanBuildingInformationVice.getXyrq(), "yyyy-MM-dd"));//协议日期

                estateIdGetLDXXEstateDetail.setDKBL(cLoanBuildingInformationVice.getDkbl().toPlainString());//贷款比例

                estateIdGetLDXXEstateDetail.setDYS(cLoanBuildingInformationVice.getDys().toPlainString());//单元数
                estateIdGetLDXXEstateDetails.add(estateIdGetLDXXEstateDetail);
            }
        }
        estateIdGetLDXX.setestateDetail(estateIdGetLDXXEstateDetails);

        estateIdGet.setLDXX(estateIdGetLDXX);

        estateIdGet.setHQTDZJ(cLoanEatateProjectVice.getHqtdzj().toPlainString());//获取土地总价

        if (cLoanEatateProjectVice.getAjxyrq() != null)
            estateIdGet.setAJXYRQ(DateUtil.date2Str(cLoanEatateProjectVice.getAjxyrq(), "yyyy-MM-dd"));//按揭协议日期

        estateIdGet.setBZJBL(cLoanEatateProjectVice.getBzjbl().toPlainString());//保证金比例

        estateIdGet.setLPMC(cLoanEatateProjectVice.getLpmc());//楼盘名称

        estateIdGet.setLPBH(cLoanEatateProjectVice.getLpbh());    //楼盘编号

        estateIdGet.setBeiZhu(cLoanEatateProjectVice.getBeiZhu());//备注

        estateIdGet.setYWWD(cLoanHousingBusinessProcess.getYwwd().getMingCheng());//业务网点

        estateIdGet.setCZY(cLoanHousingBusinessProcess.getCzy());//操作员

        estateIdGet.setFKGS(cLoanEatateProjectVice.getFkgs());//房开公司

        estateIdGet.setLPDZ(cLoanEatateProjectVice.getLpdz());//楼盘地址

        estateIdGet.setJZZMJ(cLoanEatateProjectVice.getJzzmj().toPlainString());//建筑总面积

        estateIdGet.setBLZL(StringUtil.notEmpty(cLoanEatateProjectVice.getBlzl()) ? cLoanEatateProjectVice.getBlzl() : "");//办理资料

        estateIdGet.setTZRQ(DateUtil.date2Str(new Date(), format)); //填制日期

        String id = pdfService.getEstateProjectAlterReceipt(ResUtils.noneAdductionValue(EstateIdGet.class, estateIdGet), "01");

        estateIdGet.setID(id);

        return estateIdGet;
    }

    @Override
    public PageRes getEstateProjectInfoAccept(TokenContext tokenContext, String LPMC, String ZHUANGTAI, String KSSJ, String JSSJ, String pageNo, String pageSize) {
        System.out.println("受理楼盘列表");
        PageRes pageRes = new PageRes();
        int page_number = 1;
        int pagesize_number = 10;
        try {
            if (!StringUtil.isEmpty(pageNo)) {
                page_number = Integer.parseInt(pageNo);
            }
            if (!StringUtil.isEmpty(pageSize)) {
                pagesize_number = Integer.parseInt(pageSize);
            }
        } catch (Exception e) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "页码");
        }
        List<CLoanHousingBusinessProcess> cLoanHousingBusinessProcess = instance(loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(ZHUANGTAI) && !ZHUANGTAI.equals(CollectionBusinessStatus.所有.getName()) && !ZHUANGTAI.equals(CollectionBusinessStatus.待审核.getName()))
                this.put("step", ZHUANGTAI);
            if (StringUtil.notEmpty(LPMC)) this.put("loanEatateProjectVice.lpmc", LPMC);
            this.put("cznr", LoanBusinessType.新建楼盘.getCode());
        }}).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                if (CollectionBusinessStatus.待审核.getName().equals(ZHUANGTAI)) {
                    criteria.add(Restrictions.like("step", LoanBussinessStatus.待某人审核.getName()));
                }
                if (StringUtil.notEmpty(tokenContext.getUserInfo().getYWWD()) && !"1".equals(tokenContext.getUserInfo().getYWWD())) {
                    criteria.createAlias("ywwd", "ywwd");

                    criteria.add(Restrictions.eq("ywwd.id", tokenContext.getUserInfo().getYWWD()));
                }
            }
        }).betweenDate(timeTransform(KSSJ, JSSJ)[0], timeTransform(KSSJ, JSSJ)[1]).pageOption(pageRes, pagesize_number, page_number).searchOption(SearchOption.FUZZY).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        return new PageRes<EstateProjectListRes>() {{
            this.setResults(new ArrayList<EstateProjectListRes>() {{
                for (CLoanHousingBusinessProcess cLoanHousingBusinessProcess1 : cLoanHousingBusinessProcess) {
                    this.add(new EstateProjectListRes() {{
                        this.setBZJBL(cLoanHousingBusinessProcess1.getLoanEatateProjectVice().getBzjbl().toPlainString());//保证金比例

                        this.setLPMC(cLoanHousingBusinessProcess1.getLoanEatateProjectVice().getLpmc());//楼盘名称

                        this.setYWLSH(cLoanHousingBusinessProcess1.getYwlsh()); //业务流水号

                        this.setFKGS(cLoanHousingBusinessProcess1.getLoanEatateProjectVice().getLoanHousingCompanyVice().getFkgs());//房开公司

                        this.setLXR(cLoanHousingBusinessProcess1.getLoanEatateProjectVice().getLxr());//联系人

                        this.setLXDH(cLoanHousingBusinessProcess1.getLoanEatateProjectVice().getLxdh());//联系电话

                        this.setZHUANGTAI(cLoanHousingBusinessProcess1.getStep());//状态

                        this.setYWWD(cLoanHousingBusinessProcess1.getYwwd().getMingCheng());//业务网点

                        this.setLPDZ(cLoanHousingBusinessProcess1.getLoanEatateProjectVice().getLpdz());//楼盘地址

                        this.setCZY(cLoanHousingBusinessProcess1.getCzy());//操作员

                        this.setSLSJ(DateUtil.date2Str(cLoanHousingBusinessProcess1.getCreated_at(), format));//受理时间

                    }});
                }
            }});
            this.setCurrentPage(pageRes.getCurrentPage());

            this.setNextPageNo(pageRes.getNextPageNo());

            this.setPageCount(pageRes.getPageCount());

            this.setTotalCount(pageRes.getTotalCount());

            this.setPageSize(pageRes.getPageSize());
        }};
    }

    @Override
    public PageResNew getEstateProjectInfoAcceptNew(TokenContext tokenContext, String LPMC, String ZHUANGTAI, String KSSJ, String JSSJ, String marker, String pageSize,String action) {
        System.out.println("受理楼盘列表");
        int pagesize_number = 10;
        try {
            if (!StringUtil.isEmpty(pageSize)) {
                pagesize_number = Integer.parseInt(pageSize);
            }
        } catch (Exception e) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "页码大小");
        }
        List<CLoanHousingBusinessProcess> cLoanHousingBusinessProcess = instance(loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(LPMC)) this.put("loanEatateProjectVice.lpmc", LPMC);
        }}).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                if (StringUtil.notEmpty(ZHUANGTAI) && !ZHUANGTAI.equals(CollectionBusinessStatus.所有.getName()) && !ZHUANGTAI.equals(CollectionBusinessStatus.待审核.getName()))
                    criteria.add(Restrictions.eq("step", ZHUANGTAI));
                criteria.add(Restrictions.eq("cznr", LoanBusinessType.新建楼盘.getCode()));
                if (CollectionBusinessStatus.待审核.getName().equals(ZHUANGTAI)) {
                    criteria.add(Restrictions.like("step", LoanBussinessStatus.待某人审核.getName()));
                }
                if (StringUtil.notEmpty(tokenContext.getUserInfo().getYWWD()) && !"1".equals(tokenContext.getUserInfo().getYWWD())) {
                    criteria.createAlias("ywwd", "ywwd");

                    criteria.add(Restrictions.eq("ywwd.id", tokenContext.getUserInfo().getYWWD()));
                }
            }
        }).betweenDate(timeTransform(KSSJ, JSSJ)[0], timeTransform(KSSJ, JSSJ)[1]).pageOption(marker, action, pagesize_number).searchOption(SearchOption.FUZZY).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        return new PageResNew<EstateProjectListRes>() {{
            this.setResults(action,new ArrayList<EstateProjectListRes>() {{
                for (CLoanHousingBusinessProcess cLoanHousingBusinessProcess1 : cLoanHousingBusinessProcess) {
                    this.add(new EstateProjectListRes() {{
                        this.setId(cLoanHousingBusinessProcess1.getId());
                        this.setBZJBL(cLoanHousingBusinessProcess1.getLoanEatateProjectVice().getBzjbl().toPlainString());//保证金比例

                        this.setLPMC(cLoanHousingBusinessProcess1.getLoanEatateProjectVice().getLpmc());//楼盘名称

                        this.setYWLSH(cLoanHousingBusinessProcess1.getYwlsh()); //业务流水号

                        this.setFKGS(cLoanHousingBusinessProcess1.getLoanEatateProjectVice().getLoanHousingCompanyVice().getFkgs());//房开公司

                        this.setLXR(cLoanHousingBusinessProcess1.getLoanEatateProjectVice().getLxr());//联系人

                        this.setLXDH(cLoanHousingBusinessProcess1.getLoanEatateProjectVice().getLxdh());//联系电话

                        this.setZHUANGTAI(cLoanHousingBusinessProcess1.getStep());//状态

                        this.setYWWD(cLoanHousingBusinessProcess1.getYwwd().getMingCheng());//业务网点

                        this.setLPDZ(cLoanHousingBusinessProcess1.getLoanEatateProjectVice().getLpdz());//楼盘地址

                        this.setCZY(cLoanHousingBusinessProcess1.getCzy());//操作员

                        this.setSLSJ(DateUtil.date2Str(cLoanHousingBusinessProcess1.getCreated_at(), format));//受理时间

                    }});
                }
            }});

        }};
    }


    //测试通过
    @Override
    public void doEstateProject(String YWLSH) {
        System.out.println("审核通过后添加楼盘信息");

        CLoanHousingBusinessProcess cLoanHousingBusinessProcess = instance(loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("ywlsh", YWLSH);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (cLoanHousingBusinessProcess == null || cLoanHousingBusinessProcess.getLoanEatateProjectVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务不存在");
        }
        CLoanEatateProjectVice cLoanEatateProjectVice = cLoanHousingBusinessProcess.getLoanEatateProjectVice();
        CLoanEatateProjectBasic cLoanEatateProjectBasic = new CLoanEatateProjectBasic();
        cLoanEatateProjectBasic.setLpmc(cLoanEatateProjectVice.getLpmc());//楼盘名称
        cLoanEatateProjectBasic.setLpdz(cLoanEatateProjectVice.getLpdz());//楼盘地址
        cLoanEatateProjectBasic.setYsxkzh(cLoanEatateProjectVice.getYsxkzh());//预售许可证号
        cLoanEatateProjectBasic.setHqtddj(cLoanEatateProjectVice.getHqtddj());//获取土地单价
        cLoanEatateProjectBasic.setHqtdzj(cLoanEatateProjectVice.getHqtdzj());//获取土地总价
        cLoanEatateProjectBasic.setBzjbl(cLoanEatateProjectVice.getBzjbl());//保证金比例
        cLoanEatateProjectBasic.setJzzmj(cLoanEatateProjectVice.getJzzmj());//建筑总面积
        cLoanEatateProjectBasic.setJzzje(cLoanEatateProjectVice.getJzzje());//建筑总金额
        cLoanEatateProjectBasic.setAjxyrq(cLoanEatateProjectVice.getAjxyrq());//按揭协议日期
        cLoanEatateProjectBasic.setLxr(cLoanEatateProjectVice.getLxr());//联系人
        cLoanEatateProjectBasic.setLxdh(cLoanEatateProjectVice.getLxdh());//联系电话
        cLoanEatateProjectBasic.setBeiZhu(cLoanEatateProjectVice.getBeiZhu());//备注
        cLoanEatateProjectBasic.setBlzl(cLoanEatateProjectVice.getBlzl());//办理资料
        cLoanEatateProjectBasic.setSfbg(cLoanEatateProjectVice.getSfbg());//是否变更
        cLoanEatateProjectBasic.setYwwd(cLoanHousingBusinessProcess.getYwwd().getId());//业务网点
        cLoanEatateProjectBasic.setCzy(cLoanHousingBusinessProcess.getCzy());//操作员
        cLoanEatateProjectBasic.setSfqy(true);//是否启用
        List<CLoanBuildingInformationBasic> cLoanBuildingInformationBasics = new ArrayList<>();
        for (CLoanBuildingInformationVice cLoanBuildingInformationVice : cLoanEatateProjectVice.getLoanBuildingInformationVices()) {
            if (!cLoanBuildingInformationVice.isDeleted()) {
                CLoanBuildingInformationBasic cLoanBuildingInformationBasic = new CLoanBuildingInformationBasic();//构造楼栋信息
                cLoanBuildingInformationBasic.setYsxkz(cLoanBuildingInformationVice.getYsxkz());//预售许可证
                cLoanBuildingInformationBasic.setLdmh(cLoanBuildingInformationVice.getLdmh());//楼栋名/号
                cLoanBuildingInformationBasic.setDkbl(cLoanBuildingInformationVice.getDkbl());//贷款比例
                cLoanBuildingInformationBasic.setJgrq(cLoanBuildingInformationVice.getJgrq());//竣工日期
                cLoanBuildingInformationBasic.setXyrq(cLoanBuildingInformationVice.getXyrq());//协议日期
                cLoanBuildingInformationBasic.setKsdyh(cLoanBuildingInformationVice.getKsdyh());//开始单元号
                cLoanBuildingInformationBasic.setDys(cLoanBuildingInformationVice.getDys());//单元数
                cLoanBuildingInformationBasic.setBeiZhu(cLoanBuildingInformationVice.getBeiZhu());//备注
                cLoanBuildingInformationBasic.setcLoanEatateProjectBasic(cLoanEatateProjectBasic);
                cLoanBuildingInformationBasics.add(cLoanBuildingInformationBasic);
            }
        }
        cLoanEatateProjectBasic.setHBuildingInformationBasics(cLoanBuildingInformationBasics);

        CLoanHousingCompanyBasic cLoanHousingCompanyBasic = DAOBuilder.instance(icLoanHousingCompanyBasicDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("fkgszh", cLoanEatateProjectVice.getLoanHousingCompanyVice().getFkgszh());
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        cLoanEatateProjectBasic.setcLoanHousingCompanyBasic(cLoanHousingCompanyBasic);

        cLoanHousingBusinessProcess.setBjsj(new Date());//办结时间

        instance(loanHousingBusinessProcessDAO).entity(cLoanHousingBusinessProcess).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });


        CLoanEatateProjectBasic cLoanEatateProjectBasic1 = instance(loanEatateProjectBasicDAO).entity(cLoanEatateProjectBasic).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        cLoanHousingBusinessProcess.getLoanEatateProjectVice().setLpbh(cLoanEatateProjectBasic1.getLpbh());
        instance(loanHousingBusinessProcessDAO).entity(cLoanHousingBusinessProcess).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        saveAuditHistory.saveNormalBusiness(YWLSH, LoanBusinessType.新建楼盘.getName(), "办结");


    }

    @Override
    public CommonResponses submitEstateProject(TokenContext tokenContext, List<String> YWLSHS) {
        System.out.println("批量提交操作");
        ArrayList<Exception> exceptions = new ArrayList<>();
        for (String YWLSH : YWLSHS) {
            if (YWLSH == null) {
                throw new ErrorException(Parameter_MISS);
            }
            CLoanHousingBusinessProcess cLoanHousingBusinessProcess = instance(loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{
                this.put("ywlsh", YWLSH);
            }}).getObject(new ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            if (cLoanHousingBusinessProcess == null) {
                throw new ErrorException(Data_MISS, "业务流水号不存在：" + YWLSH);
            }
            if (!cLoanHousingBusinessProcess.getCzy().equals(tokenContext.getUserInfo().getCZY()) ||
                    !cLoanHousingBusinessProcess.getYwwd().getId().equals(tokenContext.getUserInfo().getYWWD()))
                throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务(" + YWLSH + ")不是由您受理的，不能提交");
            checkParam(cLoanHousingBusinessProcess);//数据完整性验证
            if (!iUploadImagesService.validateUploadFile(UploadFileModleType.贷款.getCode(),
                    UploadFileBusinessType.楼盘项目申请.getCode(), cLoanHousingBusinessProcess.getLoanEatateProjectVice().getBlzl())) {
                throw new ErrorException(ReturnEnumeration.Parameter_MISS, "上传资料");
            }
            //数据库唯一验证
            CLoanEatateProjectBasic cLoanEatateProjectBasic = DAOBuilder.instance(loanEatateProjectBasicDAO).extension(new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    criteria.add(Restrictions.or(Restrictions.eq("lpmc", cLoanHousingBusinessProcess.getLoanEatateProjectVice().getLpmc()), Restrictions.eq("lpdz", cLoanHousingBusinessProcess.getLoanEatateProjectVice().getLpdz())));
                }
            }).getObject(new ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            if (cLoanEatateProjectBasic != null) {
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "楼盘已存在，请勿重复添加");
            }
            updateState(this.iStateMachineService, 通过.getEvent(), new TaskEntity(cLoanHousingBusinessProcess.getYwlsh(), cLoanHousingBusinessProcess.getStep(), new HashSet<String>(tokenContext.getRoleList()),
                    tokenContext.getUserInfo().getCZY(), cLoanHousingBusinessProcess.getLoanEatateProjectVice().getBeiZhu(), 贷款_楼盘申请受理.getSubType(), Loan, tokenContext.getUserInfo().getYWWD()), new StateChangeHandler() {
                @Override
                public void onStateChange(boolean succeed, String next, Exception e) {
                    if (e != null) {
                        throw new ErrorException(e);
                    }
                    if (!StringUtil.notEmpty(next) || e != null) {
                        return;
                    }

                    if (succeed) {
                        //缺少step
                        cLoanHousingBusinessProcess.setStep(next);
                        if (isIntoReview(next, null)) cLoanHousingBusinessProcess.setDdsj(new Date());
                        instance(loanHousingBusinessProcessDAO).entity(cLoanHousingBusinessProcess).save(new ErrorHandler() {
                            @Override
                            public void error(Exception e) {
                                throw new ErrorException(e);
                            }
                        });
                        if (next.equals(LoanBussinessStatus.办结.getName())) doEstateProject(YWLSH);
                    }
                }
            });
            saveAuditHistory.saveNormalBusiness(YWLSH, tokenContext, LoanBusinessType.新建楼盘.getName(), "修改");
            //在途验证
            CLoanHousingBusinessProcess cLoanHousingBusinessProcessOR = instance(loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{
                // this.put("loanEatateProjectVice.lpmc", estateProjectInfo.getLPXX().getLPMC());
                // this.put("loanEatateProjectVice.lpdz", estateProjectInfo.getLPXX().getLPDZ());
                this.put("cznr", LoanBusinessType.新建楼盘.getCode());
            }}).extension(new IBaseDAO.CriteriaExtension() {
                @Override
                public void extend(Criteria criteria) {
                    criteria.add(Restrictions.ne("ywlsh", YWLSH));
                    criteria.createAlias("loanEatateProjectVice", "loanEatateProjectVice");
                    criteria.add(Restrictions.or(Restrictions.eq("loanEatateProjectVice.lpmc", cLoanHousingBusinessProcess.getLoanEatateProjectVice().getLpmc()), Restrictions.eq("loanEatateProjectVice.lpdz", cLoanHousingBusinessProcess.getLoanEatateProjectVice().getLpdz())));
                    criteria.add(Restrictions.like("step", LoanBussinessStatus.待某人审核.getName()));
                }
            }).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            if (cLoanHousingBusinessProcessOR != null) {
                throw new ErrorException(ReturnEnumeration.Business_In_Process);
            }

        }
        return new CommonResponses() {{
            this.setState("success");
        }};
    }

    public CommonResponses reEstateProjectSFQY(TokenContext tokenContext, String LPBH, String SFQY) {
        CLoanEatateProjectBasic cLoanEatateProjectBasic = instance(loanEatateProjectBasicDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("lpbh", LPBH);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (cLoanEatateProjectBasic == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "楼盘编号不存在");
        }
        if (!cLoanEatateProjectBasic.getYwwd().equals(tokenContext.getUserInfo().getYWWD())) {
            throw new ErrorException(ReturnEnumeration.User_Defined, "没有操作权限");
        }

        if (SFQY.equals("0")) {
            cLoanEatateProjectBasic.setSfqy(false);
        } else {
            if (cLoanEatateProjectBasic.getcLoanHousingCompanyBasic().getSfqy())
                cLoanEatateProjectBasic.setSfqy(true);
            else
                throw new ErrorException(ReturnEnumeration.Account_NOT_MATCH, "请先启用该楼盘的房开公司");

        }
        instance(loanEatateProjectBasicDAO).entity(cLoanEatateProjectBasic).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        return new CommonResponses() {{
            this.setId(cLoanEatateProjectBasic.getLpbh());
            this.setState("success");
        }};
    }

    @Override
    public PageRes<GetCommonHistory> getEstateProjectHistory(String LPBH, String pageNo, String pageSize) {
        PageRes pageRes = new PageRes();
        int page_number = 1;
        int pagesize_number = 10;
        try {
            if (!StringUtil.isEmpty(pageNo)) {
                page_number = Integer.parseInt(pageNo);
            }
            if (!StringUtil.isEmpty(pageSize)) {
                pagesize_number = Integer.parseInt(pageSize);
            }
        } catch (Exception e) {
            throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "页码");
        }
        List<CLoanHousingBusinessProcess> cLoanHousingBusinessProcess = instance(loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("loanEatateProjectVice.lpbh", LPBH);
        }}).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        List<CAuditHistory> auditHistoryList = DAOBuilder.instance(this.icAuditHistoryDAO)
                .searchFilter(new HashMap<String, Object>() {
                    {
                        this.put("ywlsh", (Collection) CollectionUtils.flatmap(cLoanHousingBusinessProcess, new CollectionUtils.Transformer<CLoanHousingBusinessProcess, String>() {
                            @Override
                            public String tansform(CLoanHousingBusinessProcess var1) {
                                return var1.getYwlsh();
                            }
                        }));
                    }
                }).pageOption(pageRes, pagesize_number, page_number).getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        e.printStackTrace();
                    }
                });
        ArrayList<GetCommonHistory> getCommonHistories = new ArrayList<>();
        for (CAuditHistory cAuditHistory : auditHistoryList) {
            GetCommonHistory getCommonHistory = new GetCommonHistory();
            try {
                String ywwdmc = cAccountNetworkDAO.get(cAuditHistory.getYwwd()).getMingCheng();
                getCommonHistory.setYWWD(ywwdmc);
            } catch (Exception e) {
                getCommonHistory.setYWWD("缺失");
            }
            getCommonHistory.setYWLX(cAuditHistory.getYwlx());
            getCommonHistory.setCZNR(cAuditHistory.getCaoZuo());
            getCommonHistory.setCZY(cAuditHistory.getCzy());
            getCommonHistory.setSLSJ(DateUtil.date2Str(cAuditHistory.getShsj() == null ? cAuditHistory.getDdsj() : cAuditHistory.getShsj(), format));
            getCommonHistory.setCZQD(cAuditHistory.getCzqd());
            getCommonHistory.setYWLSH(cAuditHistory.getYwlsh());
            getCommonHistories.add(getCommonHistory);
        }

        return new PageRes<GetCommonHistory>() {{
            this.setResults(getCommonHistories);
            this.setCurrentPage(pageRes.getCurrentPage());
            this.setNextPageNo(pageRes.getNextPageNo());
            this.setPageCount(pageRes.getPageCount());
            this.setPageSize(pageRes.getPageSize());
            this.setTotalCount(pageRes.getTotalCount());
        }};
    }

    //获取售房人账户号码
    @Override
    public HousingIdGet getSFRZHHM(String lpmc) {
        CLoanEatateProjectBasic cLoanEatateProjectBasic = instance(loanEatateProjectBasicDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("lpmc", lpmc);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (cLoanEatateProjectBasic == null || cLoanEatateProjectBasic.getcLoanHousingCompanyBasic() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "楼盘不存在");
        }
        List<CLoanHousingCompanyBasicExtension> cLoanHousingCompanyBasicExtensions = cLoanEatateProjectBasic.getcLoanHousingCompanyBasic().getcLoanHousingCompanyBasicExtensions();
        ArrayList<HousingCompanyInfoSale> housingCompanyInfoSale = new ArrayList<>();
        for (CLoanHousingCompanyBasicExtension cLoanHousingCompanyBasicExtension : cLoanHousingCompanyBasicExtensions) {
            if (!cLoanHousingCompanyBasicExtension.isDeleted()) {
                HousingCompanyInfoSale housingCompanyInfoSale1 = new HousingCompanyInfoSale();
                housingCompanyInfoSale1.setSFRKHYHMC(cLoanHousingCompanyBasicExtension.getSFRKHYHMC());//售房人开户银行名称
                housingCompanyInfoSale1.setSFRZHHM(cLoanHousingCompanyBasicExtension.getSFRZHHM()); //售房人账户号码
                housingCompanyInfoSale1.setSFRKHYHKHM(cLoanHousingCompanyBasicExtension.getSFRKHYHKHM()); //售房人开户银行开户名
                housingCompanyInfoSale.add(housingCompanyInfoSale1);
            }
        }
        return new HousingIdGet() {{
            this.setHousingCompanyInfoSales(housingCompanyInfoSale);
        }};
    }

    @Override
    public EstateIdGet getEstateProjectInfoByLPBH(String lpbh) {
        System.out.println("根据楼盘编号查找楼盘信息");

        CLoanEatateProjectBasic cLoanEatateProjectBasic = instance(loanEatateProjectBasicDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("lpbh", lpbh);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (cLoanEatateProjectBasic == null || cLoanEatateProjectBasic.getcLoanHousingCompanyBasic() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "楼盘编号不存在");
        }
        return new EstateIdGet() {{
            if (cLoanEatateProjectBasic.getJzzje() != null)
                this.setJZZJE(cLoanEatateProjectBasic.getJzzje().toPlainString());//建筑总金额
            if (cLoanEatateProjectBasic.getHqtddj() != null)
                this.setHQTDDJ(cLoanEatateProjectBasic.getHqtddj().toPlainString());//获取土地单价
            this.setCZY(cLoanEatateProjectBasic.getCzy());
            try {
                String ywwdmc = cAccountNetworkDAO.get(cLoanEatateProjectBasic.getYwwd()).getMingCheng();
                this.setYWWD(ywwdmc);
            } catch (Exception e) {
                this.setYWWD("缺失");
            }
            this.setYSXKZH(cLoanEatateProjectBasic.getYsxkzh());//预售许可证号

            this.setLXR(cLoanEatateProjectBasic.getLxr());//联系人

            this.setLXDH(cLoanEatateProjectBasic.getLxdh());//联系电话

            this.setLDXX(new EstateIdGetLDXX() {{
                this.setestateDetail(new ArrayList<EstateIdGetLDXXEstateDetail>() {{
                    for (CLoanBuildingInformationBasic cLoanBuildingInformationBasic : cLoanEatateProjectBasic.getHBuildingInformationBasics()) {
                        if (!cLoanBuildingInformationBasic.isDeleted()) {
                            this.add(new EstateIdGetLDXXEstateDetail() {{
                                this.setYSXKZ(cLoanBuildingInformationBasic.getYsxkz());
                                this.setLDMH(cLoanBuildingInformationBasic.getLdmh());//楼栋名/号
                                if (cLoanBuildingInformationBasic.getJgrq() != null)
                                    this.setJGRQ(DateUtil.date2Str(cLoanBuildingInformationBasic.getJgrq(), "yyyy-MM-dd"));//竣工日期

                                this.setKSDYH(cLoanBuildingInformationBasic.getKsdyh().toPlainString());//开始单元号

                                this.setBeiZhu(cLoanBuildingInformationBasic.getBeiZhu());//备注

                                this.setXYRQ(DateUtil.date2Str(cLoanBuildingInformationBasic.getXyrq(), "yyyy-MM-dd"));//协议日期

                                this.setDKBL(cLoanBuildingInformationBasic.getDkbl().toPlainString());//贷款比例

                                this.setDYS(cLoanBuildingInformationBasic.getDys().toPlainString());//单元数
                            }});
                        }
                    }
                }});
            }});//楼栋信息

            this.setHQTDZJ(cLoanEatateProjectBasic.getHqtdzj().toPlainString());//获取土地总价

            if (cLoanEatateProjectBasic.getAjxyrq() != null)
                this.setAJXYRQ(DateUtil.date2Str(cLoanEatateProjectBasic.getAjxyrq(), "yyyy-MM-dd"));//按揭协议日期
            if (cLoanEatateProjectBasic.getBzjbl() != null)
                this.setBZJBL(cLoanEatateProjectBasic.getBzjbl().toPlainString());//保证金比例

            this.setLPMC(cLoanEatateProjectBasic.getLpmc());//楼盘名称

            this.setLPBH(cLoanEatateProjectBasic.getLpbh());    //楼盘编号

            this.setBeiZhu(cLoanEatateProjectBasic.getBeiZhu());//备注

            this.setFKGS(cLoanEatateProjectBasic.getcLoanHousingCompanyBasic().getFkgs());//房开公司

            this.setLPDZ(cLoanEatateProjectBasic.getLpdz());//楼盘地址

            this.setJZZMJ(cLoanEatateProjectBasic.getJzzmj().toPlainString());//建筑总面积

            this.setBLZL(cLoanEatateProjectBasic.getBlzl());//办理资料


        }};
    }

    private void checkParam(CLoanHousingBusinessProcess cLoanHousingBusinessProcess) {
        if (cLoanHousingBusinessProcess.getLoanEatateProjectVice() == null || cLoanHousingBusinessProcess.getLoanEatateProjectVice().getLoanHousingCompanyVice() == null) {
            throw new ErrorException(Data_MISS);
        }
        CLoanEatateProjectVice cLoanEatateProjectVice = cLoanHousingBusinessProcess.getLoanEatateProjectVice();
        if (!StringUtil.notEmpty(cLoanEatateProjectVice.getLpmc())) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "楼盘名称");
        }
        if (!StringUtil.notEmpty(cLoanEatateProjectVice.getLpdz())) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "楼盘地址");
        }
        if (cLoanEatateProjectVice.getLpdz().length() < 10) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "楼盘地址不能小于10个字");
        }

        if (cLoanEatateProjectVice.getHqtddj() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "获取土地单价");
        }
        if (cLoanEatateProjectVice.getHqtddj().compareTo(new BigDecimal("0")) == 0) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "获取土地单价");
        }
        if (cLoanEatateProjectVice.getHqtdzj() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "获取土地总价");
        }
        if (cLoanEatateProjectVice.getHqtdzj().compareTo(new BigDecimal("0")) == 0) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "获取土地总价");
        }
        if (!StringUtil.notEmpty(cLoanEatateProjectVice.getJzzmj().toPlainString())) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "建筑总面积");
        }
        if (cLoanEatateProjectVice.getJzzmj().compareTo(new BigDecimal("0")) == 0) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "建筑总面积");
        }

        if (cLoanEatateProjectVice.getAjxyrq() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "按揭协议日期");
        }
        if (!StringUtil.notEmpty(cLoanEatateProjectVice.getLxr())) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "联系人");
        }
        if (!StringUtil.notEmpty(cLoanEatateProjectVice.getLxdh())) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "联系电话");
        }
        if (!StringUtil.isMobile(cLoanEatateProjectVice.getLxdh()) && !StringUtil.isPhone(cLoanEatateProjectVice.getLxdh())) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "电话号码格式不正确");
        }


    }
     //预售许可证唯一性判断
    private void checkYSXKZUniq(ArrayList<EstateProjectInfoLDXXEstateDetail> arrayList){
        if(arrayList==null) return;
        HashSet<String> hashSet = new HashSet();
        for (EstateProjectInfoLDXXEstateDetail estateProjectInfoLDXXEstateDetail : arrayList){
            if(!StringUtil.notEmpty(estateProjectInfoLDXXEstateDetail.getYSXKZ())){
                throw new ErrorException(ReturnEnumeration.Data_MISS,"预售许可证");
            }
            hashSet.add(estateProjectInfoLDXXEstateDetail.getYSXKZ());
        }
        if (hashSet.size()!=arrayList.size()) throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"预售许可证重复");
    }
    private void checkYSXKZUniqPut(ArrayList<EstatePutLDXXEstateDetail> arrayList){      
        if(arrayList==null) return;
        HashSet<String> hashSet = new HashSet();
        for (EstatePutLDXXEstateDetail estatePutLDXXEstateDetail : arrayList){
            if(!StringUtil.notEmpty(estatePutLDXXEstateDetail.getYSXKZ())){
                throw new ErrorException(ReturnEnumeration.Data_MISS,"预售许可证");
            }
            hashSet.add(estatePutLDXXEstateDetail.getYSXKZ());
        }
        if (hashSet.size()!=arrayList.size()) throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"预售许可证重复");
    }


}
