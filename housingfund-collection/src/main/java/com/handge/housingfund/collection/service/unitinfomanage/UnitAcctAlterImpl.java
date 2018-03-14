package com.handge.housingfund.collection.service.unitinfomanage;



import com.handge.housingfund.collection.utils.StateMachineUtils;
import com.handge.housingfund.common.service.ISaveAuditHistory;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.AccountRpcService;
import com.handge.housingfund.common.service.account.model.Msg;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.account.model.RpcAuth;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessStatus;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.collection.enumeration.CommonFieldType;
import com.handge.housingfund.common.service.collection.enumeration.PersonCertificateType;
import com.handge.housingfund.common.service.collection.model.unit.*;
import com.handge.housingfund.common.service.collection.service.unitinfomanage.UnitAcctAlter;
import com.handge.housingfund.common.service.loan.enums.LoanBussinessStatus;
import com.handge.housingfund.common.service.loan.model.CommonResponses;
import com.handge.housingfund.common.service.others.IDictionaryService;
import com.handge.housingfund.common.service.others.IPdfService;
import com.handge.housingfund.common.service.others.IUploadImagesService;
import com.handge.housingfund.common.service.others.enums.UploadFileBusinessType;
import com.handge.housingfund.common.service.others.enums.UploadFileModleType;
import com.handge.housingfund.common.service.others.model.CommonDictionary;
import com.handge.housingfund.common.service.others.model.SingleDictionaryDetail;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.BusinessSubType;
import com.handge.housingfund.database.enums.BusinessType;
import com.handge.housingfund.database.enums.Events;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.database.utils.CriteriaUtils;
import com.handge.housingfund.database.utils.DAOBuilder;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import com.handge.housingfund.statemachineV2.IStateMachineService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

;

/*
 * Created by lian on 2017/7/18.
 */
@SuppressWarnings({"Duplicates", "Convert2Lambda", "Anonymous2MethodRef", "SpringAutowiredFieldsWarningInspection", "SpringJavaAutowiringInspection", "serial", "BigDecimalMethodWithoutRoundingCalled", "BooleanMethodIsAlwaysInverted"})
@Component
public class UnitAcctAlterImpl implements UnitAcctAlter {

	@Autowired
	private ICCollectionUnitInformationBasicViceDAO collectionUnitInformationBasicViceDAO;

	@Autowired
	private IStCollectionUnitBusinessDetailsDAO collectionUnitBusinessDetailsDAO;

	@Autowired
	private IStCommonUnitDAO commonUnitDAO;

	@Autowired
	private IStateMachineService stateMachineService;

	@Autowired
	private AccountRpcService accountRpcService;
	@Autowired
	private IPdfService pdfService;
	@Autowired
	private ICAuditHistoryDAO icAuditHistoryDAO;

	@Autowired
	private ISaveAuditHistory iSaveAuditHistory;
	@Autowired
	private IDictionaryService iDictionaryService;
	@Autowired
	private IStCommonPolicyDAO commonPolicyDAO;
	@Autowired
	private IUploadImagesService iUploadImagesService;

	@Autowired
	private ICAccountNetworkDAO cAccountNetworkDAO;

	private static String format = "yyyy-MM-dd";

	private static String formatNY = "yyyy-MM";

	private static String formatNYRSF = "yyyy-MM-dd HH:mm";

	private static String formatR = "dd";


	@Override
	public AddUnitAcctSetRes addUnitAccountAlter(TokenContext tokenContext, UnitAcctAlterPost unitAcctAlterPost) {

		//region //参数检查
		boolean allowNull = unitAcctAlterPost.getCZLX().equals("0");


		//if(!DateUtil.isFollowFormat(unitAcctAlterPost.getDWDJXX().getDWSCHJNY()/*单位首次汇缴年月*/,format,allowNull)){throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"单位首次汇缴年月");}

		if(!DateUtil.isFollowFormat(unitAcctAlterPost.getDWDJXX().getDWSLRQ()/*单位设立日期*/,format,allowNull)){throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"单位设立日期");}

		if(!StringUtil.isDigits(unitAcctAlterPost.getDWDJXX().getDWFXR()/*单位发薪日*/,allowNull)){throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"单位发薪日");}
		//endregion

		//region //必要字段申明&关系配置
		CCollectionUnitInformationBasicVice collectionUnitInformationBasicVice = new CCollectionUnitInformationBasicVice();

		CCollectionUnitBusinessDetailsExtension collectionUnitBusinessDetailsExtension = new CCollectionUnitBusinessDetailsExtension();

		StCollectionUnitBusinessDetails collectionUnitBusinessDetails = new StCollectionUnitBusinessDetails();

		collectionUnitBusinessDetails.setExtension(collectionUnitBusinessDetailsExtension);

		collectionUnitInformationBasicVice.setDwywmx(collectionUnitBusinessDetails);

		
		
		StCommonUnit commonUnit = DAOBuilder.instance(this.commonUnitDAO).searchFilter(new HashMap<String,Object>(){{

			this.put("dwzh",unitAcctAlterPost.getDWGJXX().getDWZH());

		}}).getObject(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e);}
		});

		if (commonUnit == null || commonUnit.getCollectionUnitAccount() == null) {

			throw new ErrorException(ReturnEnumeration.Data_MISS,"当前账号对应的单位信息不存在");
		}

		if(!commonUnit.getExtension().getKhwd().equals(tokenContext.getUserInfo().getYWWD())) {
			throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务不在当前网点受理范围内");
		}

		//endregion

		//region //字段填充
		//COMMON
		collectionUnitBusinessDetails.setYwmxlx(CollectionBusinessType.其他.getCode());

		collectionUnitBusinessDetailsExtension.setCzmc(CollectionBusinessType.变更.getCode());
		collectionUnitBusinessDetailsExtension.setFchxhyy(collectionUnitBusinessDetailsExtension.getFchxhyy()/*操作原因 不填*/);
		collectionUnitBusinessDetailsExtension.setJzpzh("0"/*记账凭证号 不填*/);
		collectionUnitBusinessDetailsExtension.setSlsj(new Date()/*todo 受理时间*/);
		collectionUnitBusinessDetailsExtension.setShbtgyy(collectionUnitBusinessDetailsExtension.getShbtgyy()/*审核不通过原因 不填*/);

		collectionUnitBusinessDetails.setCzbz(CommonFieldType.非冲账.getCode()/*todo 冲账标志*/);
		collectionUnitBusinessDetails.setFse(collectionUnitBusinessDetails.getFse()/*发生额 变更不填*/);
		collectionUnitBusinessDetails.setFslxe(collectionUnitBusinessDetails.getFslxe()/*发生利息额 变更不填*/);
		collectionUnitBusinessDetails.setFsrs(collectionUnitBusinessDetails.getFsrs()/*发生人数 变更不填*/);
		collectionUnitBusinessDetails.setHbjny(collectionUnitBusinessDetails.getHbjny()/*汇补缴年月 变更不填*/);
		collectionUnitBusinessDetails.setJzrq(collectionUnitBusinessDetails.getJzrq()/*记账日期 变更不填*/);
		collectionUnitBusinessDetails.setYwmxlx(CollectionBusinessType.其他.getCode());
		collectionUnitBusinessDetails.setUnit(commonUnit);
		collectionUnitBusinessDetails.setDwzh(commonUnit.getDwzh());


		collectionUnitInformationBasicVice.setDdsj(collectionUnitInformationBasicVice.getDdsj()/*变更不填*/);
		collectionUnitInformationBasicVice.setCzmc(CollectionBusinessType.其他.getCode());
		collectionUnitInformationBasicVice.setDwschjny(collectionUnitInformationBasicVice.getDwschjny()/*变更不填*/);
		collectionUnitInformationBasicVice.setYwmxlx(CollectionBusinessType.其他.getCode());
		collectionUnitInformationBasicVice.setSlsj(new Date()/*todo 受理时间*/);


		//CZY
		collectionUnitInformationBasicVice.setCzy(tokenContext.getUserInfo().getCZY()/*操作员*/);
		collectionUnitBusinessDetailsExtension.setCzy(tokenContext.getUserInfo().getCZY()/*操作员*/);

		
		// DWDJXX
		collectionUnitInformationBasicVice.setFxzhkhyh(unitAcctAlterPost.getDWDJXX().getFXZHKHYH()/*发薪账号开户银行*/);
		collectionUnitInformationBasicVice.setDwfxr(unitAcctAlterPost.getDWDJXX().getDWFXR()/*单位发薪日*/);
		collectionUnitInformationBasicVice.setBeiZhu(unitAcctAlterPost.getDWDJXX().getBeiZhu()/*备注*/);
		collectionUnitBusinessDetailsExtension.setBeizhu(unitAcctAlterPost.getDWDJXX().getBeiZhu()/*备注*/);
		collectionUnitInformationBasicVice.setFxzhhm(unitAcctAlterPost.getDWDJXX().getFXZHHM()/*发薪账号户名*/);
		//collectionUnitInformationBasicVice.setDwschjny(DateUtil.safeStr2DBDate(format,unitAcctAlterPost.getDWDJXX().getDWSCHJNY()/*单位首次汇缴年月*/,DateUtil.dbformatYear_Month));
		collectionUnitInformationBasicVice.setFxzh(unitAcctAlterPost.getDWDJXX().getFXZH()/*发薪账号*/);
		collectionUnitInformationBasicVice.setDwslrq(DateUtil.safeStr2Date(format,unitAcctAlterPost.getDWDJXX().getDWSLRQ()/*单位设立日期*/));


		// WTYHXX
		collectionUnitInformationBasicVice.setStyhdm(unitAcctAlterPost.getWTYHXX().getSTYHDM()/*受托银行代码*/);
		collectionUnitInformationBasicVice.setStyhmc(unitAcctAlterPost.getWTYHXX().getSTYHMC()/*受托银行名称*/);

		//YWWD
		collectionUnitInformationBasicVice.setYwwd(tokenContext.getUserInfo().getYWWD()/*业务网点*/);
		CAccountNetwork network = DAOBuilder.instance(cAccountNetworkDAO).searchFilter(new HashMap<String, Object>() {{
			this.put("id",tokenContext.getUserInfo().getYWWD());
		}}).getObject(new DAOBuilder.ErrorHandler() {
			@Override
			public void error(Exception e) { throw new ErrorException(e); }
		});
		collectionUnitBusinessDetailsExtension.setYwwd(network/*业务网点*/);

		// DWLXFS
		collectionUnitInformationBasicVice.setDwczhm(unitAcctAlterPost.getDWLXFS().getCZHM()/*传真号码*/);
		collectionUnitInformationBasicVice.setDwdzxx(unitAcctAlterPost.getDWLXFS().getDWDZXX()/*单位电子信箱 */);
		collectionUnitInformationBasicVice.setDwyb(unitAcctAlterPost.getDWLXFS().getDWYB()/*单位邮编*/);
		collectionUnitInformationBasicVice.setDwlxdh(unitAcctAlterPost.getDWLXFS().getDWLXDH()/*单位联系电话*/);

		// DWGJXX
		collectionUnitInformationBasicVice.setPzjgmc(unitAcctAlterPost.getDWGJXX().getPZJGMC()/*批准机关名称*/);
		collectionUnitInformationBasicVice.setDwfrdbzjhm(unitAcctAlterPost.getDWGJXX().getDWFRDBZJHM()/*单位法人代表证件号码*/);
		collectionUnitInformationBasicVice.setDwjjlx(unitAcctAlterPost.getDWGJXX().getDWJJLX()/*单位经济类型*/);
		collectionUnitInformationBasicVice.setDwfrdbxm(unitAcctAlterPost.getDWGJXX().getDWFRDBXM()/*单位法人代表姓名*/);
		collectionUnitInformationBasicVice.setXzqy(unitAcctAlterPost.getDWGJXX().getDWXZQY()/*单位行政区域*/);
		collectionUnitInformationBasicVice.setDjsyyz(unitAcctAlterPost.getDWGJXX().getDJSYYZ()/*登记使用印章*/);
		collectionUnitInformationBasicVice.setDwlb(unitAcctAlterPost.getDWGJXX().getDWLB()/*单位类别*/);
		collectionUnitInformationBasicVice.setDwfrdbzjlx(unitAcctAlterPost.getDWGJXX().getDWFRDBZJLX()/*单位法人代表证件类型*/);
		collectionUnitInformationBasicVice.setPzjgjb(unitAcctAlterPost.getDWGJXX().getPZJGJB()/*批准机关级别*/);
		collectionUnitInformationBasicVice.setKgqk(unitAcctAlterPost.getDWGJXX().getKGQK()/*控股情况*/);
		collectionUnitInformationBasicVice.setDjzch(unitAcctAlterPost.getDWGJXX().getDJZCH()/*登记注册号*/);
		collectionUnitInformationBasicVice.setZzjgdm(unitAcctAlterPost.getDWGJXX().getZZJGDM()/*组织机构代码*/);
		collectionUnitInformationBasicVice.setDwlsgx(unitAcctAlterPost.getDWGJXX().getDWLSGX()/*单位隶属关系*/);
		collectionUnitInformationBasicVice.setDwdz(unitAcctAlterPost.getDWGJXX().getDWDZ()/*单位地址*/);
		collectionUnitInformationBasicVice.setDwmc(unitAcctAlterPost.getDWGJXX().getDWMC()/*单位名称*/);
		collectionUnitInformationBasicVice.setDwsshy(unitAcctAlterPost.getDWGJXX().getDWSSHY()/*单位所属行业*/);
		collectionUnitInformationBasicVice.setDwzh(unitAcctAlterPost.getDWGJXX().getDWZH()/*单位账号*/);

		// DWKHBLZL
		collectionUnitInformationBasicVice.setBlzl(unitAcctAlterPost.getDWKHBLZL().getBLZL()/*办理资料*/);
		collectionUnitBusinessDetailsExtension.setBlzl(unitAcctAlterPost.getDWKHBLZL().getBLZL()/*办理资料*/);

		// JBRXX
		collectionUnitInformationBasicVice.setJbrzjlx(unitAcctAlterPost.getJBRXX().getJBRZJLX()/*经办人证件类型 */);
		collectionUnitInformationBasicVice.setJbrgddhhm(unitAcctAlterPost.getJBRXX().getJBRGDDHHM()/*经办人固定电话号码*/);
		collectionUnitInformationBasicVice.setJbrxm(unitAcctAlterPost.getJBRXX().getJBRXM()/*经办人姓名*/);
		collectionUnitInformationBasicVice.setJbrzjhm(unitAcctAlterPost.getJBRXX().getJBRZJHM()/*经办人证件号码*/);
		collectionUnitInformationBasicVice.setJbrsjhm(unitAcctAlterPost.getJBRXX().getJBRSJHM()/*经办人手机号码*/);

		collectionUnitBusinessDetailsExtension.setJbrzjlx(unitAcctAlterPost.getJBRXX().getJBRZJLX()/*经办人证件类型 */);
		collectionUnitBusinessDetailsExtension.setJbrxm(unitAcctAlterPost.getJBRXX().getJBRXM()/*经办人姓名*/);
		collectionUnitBusinessDetailsExtension.setJbrzjhm(unitAcctAlterPost.getJBRXX().getJBRZJHM()/*经办人证件号码*/);

		//endregion

		//region //修改状态
		CCollectionUnitInformationBasicVice savedVice = DAOBuilder.instance(this.collectionUnitInformationBasicViceDAO).entity(collectionUnitInformationBasicVice).saveThenFetchObject(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e); }

		});

		if (savedVice == null||savedVice.getDwywmx()==null||savedVice.getDwywmx().getExtension()==null) {

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"业务记录");
		}

		StateMachineUtils.updateState(this.stateMachineService, new HashMap<String, String>(){{

			this.put("0", Events.通过.getEvent());

			this.put("1",Events.提交.getEvent());

		}}.get(unitAcctAlterPost.getCZLX()),new TaskEntity() {{

			this.setStatus(savedVice.getDwywmx().getExtension().getStep()==null ? "初始状态":savedVice.getDwywmx().getExtension().getStep());
			this.setTaskId(savedVice.getDwywmx().getYwlsh());
			this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));;
			this.setNote("");
			this.setSubtype(BusinessSubType.归集_单位账户变更.getSubType());
			this.setType(BusinessType.Collection);
			this.setOperator(savedVice.getCzy());
			this.setWorkstation(savedVice.getYwwd());

		}}, new StateMachineUtils.StateChangeHandler() {

			@Override
			public void onStateChange(boolean succeed, String next, Exception e) {

				if(e!=null){throw new ErrorException(e);}

				if(!succeed||next==null){ return;}

				savedVice.getDwywmx().getExtension().setStep(next);


				if(StringUtil.isIntoReview(next,null)){

				    savedVice.getDwywmx().getExtension().setDdsj(new Date());

				}

				DAOBuilder.instance(collectionUnitInformationBasicViceDAO).entity(savedVice).save(new DAOBuilder.ErrorHandler() {
					@Override
					public void error(Exception e) { throw new ErrorException(e); }

				});

				if(next.equals(CollectionBusinessStatus.办结.getName())){

					doUnitAcctAlter(tokenContext,savedVice.getDwywmx().getYwlsh());
				}
			}
		});

		//endregion

		//region //唯一性验证

		if("1".equals(unitAcctAlterPost.getCZLX())&&!DAOBuilder.instance(this.commonUnitDAO).extension(new IBaseDAO.CriteriaExtension() {
			@Override
			public void extend(Criteria criteria) {

				criteria.add(Restrictions.or(
						Restrictions.eq("dwdz",collectionUnitInformationBasicVice.getDwdz()),
						Restrictions.eq("dwmc",collectionUnitInformationBasicVice.getDwmc())

				));

				criteria.add(Restrictions.ne("dwzh",commonUnit.getDwzh()));
			}
		}).isUnique(new DAOBuilder.ErrorHandler() {
			@Override
			public void error(Exception e) { throw new ErrorException(e); }

		})){

			throw new ErrorException(ReturnEnumeration.Data_Already_Eeist,"单位名称或地址");
		}
		//endregion

		//region //在途验证
		if((unitAcctAlterPost.getCZLX().equals("1")&&!DAOBuilder.instance(this.collectionUnitInformationBasicViceDAO).extension(new IBaseDAO.CriteriaExtension() {
			@Override
			public void extend(Criteria criteria) {

				criteria.add(Restrictions.not(Restrictions.in(CriteriaUtils.addAlias(criteria,"dwywmx.cCollectionUnitBusinessDetailsExtension.step"),(Collection)CollectionUtils.flatmap(CollectionUtils.merge(CollectionBusinessStatus.办结.getSubTypes(), CollectionBusinessStatus.新建.getSubTypes()), new CollectionUtils.Transformer<CollectionBusinessStatus, String>() {

					@Override
					public String tansform(CollectionBusinessStatus var1) { return var1.getName(); }

				}))));

				criteria.add(Restrictions.ne("dwywmx.ywlsh",savedVice.getDwywmx().getYwlsh()));
				criteria.add(Restrictions.or(
						Restrictions.eq("dwdz",collectionUnitInformationBasicVice.getDwdz()),
						Restrictions.eq("dwmc",collectionUnitInformationBasicVice.getDwmc())
				));
				criteria.add(Restrictions.in("cCollectionUnitBusinessDetailsExtension.czmc",CollectionBusinessType.开户.getCode(),CollectionBusinessType.变更.getCode()));
			}
		}).isUnique(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e);}

		}))){

			throw new ErrorException(ReturnEnumeration.Business_In_Process,"已有单位正在使用 相同的地址 或 名称 办理开户或变更业务");
		}

		if(unitAcctAlterPost.getCZLX().equals("1")){

			this.iscollectionUnitInformationBasicViceAvailable(collectionUnitInformationBasicVice);

		}
		this.iSaveAuditHistory.saveNormalBusiness(savedVice.getDwywmx().getYwlsh(),tokenContext, CollectionBusinessType.变更.getName(),"新建");
		//endregion

		return new AddUnitAcctSetRes() {{

			this.setYWLSH(savedVice.getDwywmx().getYwlsh());

			this.setStatus("success");

		}};


	}


	@Override
	public GetUnitAcctAlterRes getUnitAccountAlter(TokenContext tokenContext,String YWLSH) {

		//region // 检查参数

		if (YWLSH == null) { throw new ErrorException(ReturnEnumeration.Parameter_MISS,"业务流水号"); }

		//endregion

		//region //必要字段查询&完整性验证
		CCollectionUnitInformationBasicVice collectionUnitInformationBasicVice = DAOBuilder.instance(this.collectionUnitInformationBasicViceDAO).searchFilter(new HashMap<String, Object>() {{

			this.put("dwywmx.ywlsh", YWLSH);

		}}).getObject(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e); }

		});

		if (collectionUnitInformationBasicVice == null||collectionUnitInformationBasicVice.getDwywmx()==null) {

			throw new ErrorException(ReturnEnumeration.Data_MISS,"业务记录");
		}

		if(collectionUnitInformationBasicVice.getDwywmx().getUnit() == null || collectionUnitInformationBasicVice.getDwywmx().getUnit().getCollectionUnitAccount() == null||collectionUnitInformationBasicVice.getDwywmx().getUnit().getExtension() == null){

			throw new ErrorException(ReturnEnumeration.Data_MISS,"当前账号对应的单位信息不存在");
		}
		StCommonUnit commonUnit = collectionUnitInformationBasicVice.getDwywmx().getUnit();
		//endregion

		return new GetUnitAcctAlterRes() {{

			this.setDWKHBLZL(new GetUnitAcctAlterResDWKHBLZL() {{

				this.setBLZL(collectionUnitInformationBasicVice.getBlzl());

			}});

			this.setDWDJXX(new GetUnitAcctAlterResDWDJXX() {{

				this.setGRJCBL((collectionUnitInformationBasicVice.getDwywmx().getUnit().getCollectionUnitAccount().getGrjcbl()==null?"0":collectionUnitInformationBasicVice.getDwywmx().getUnit().getCollectionUnitAccount().getGrjcbl().multiply(new BigDecimal("100")).doubleValue()) + "");
				this.setFXZHKHYH(collectionUnitInformationBasicVice.getFxzhkhyh());
				this.setBeiZhu(collectionUnitInformationBasicVice.getBeiZhu());
				this.setDWFXR(collectionUnitInformationBasicVice.getDwfxr());
				this.setDWJCBL((collectionUnitInformationBasicVice.getDwywmx().getUnit().getCollectionUnitAccount().getDwjcbl() == null ? "0" : collectionUnitInformationBasicVice.getDwywmx().getUnit().getCollectionUnitAccount().getDwjcbl().multiply(new BigDecimal("100")).doubleValue()) + "");
				this.setFXZHHM(collectionUnitInformationBasicVice.getFxzhhm());
				this.setDWSCHJNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month,collectionUnitInformationBasicVice.getDwywmx().getUnit().getExtension().getDwschjny(), formatNY));
				this.setFXZH(collectionUnitInformationBasicVice.getFxzh());
				this.setDWSLRQ(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month,collectionUnitInformationBasicVice.getDwslrq(), format));


			}});

			this.setDWLXFS(new GetUnitAcctAlterResDWLXFS() {{

				this.setCZHM(collectionUnitInformationBasicVice.getDwczhm());
				this.setDWLXDH(collectionUnitInformationBasicVice.getDwlxdh());
				this.setDWDZXX(collectionUnitInformationBasicVice.getDwdzxx());
				this.setDWYB(collectionUnitInformationBasicVice.getDwyb());
			}});

			this.setDWGJXX(new GetUnitAcctAlterResDWGJXX() {{

				this.setPZJGMC(collectionUnitInformationBasicVice.getPzjgmc());
				this.setDWFRDBZJHM(collectionUnitInformationBasicVice.getDwfrdbzjhm());
				this.setYWLSH(collectionUnitInformationBasicVice.getDwywmx().getYwlsh());
				this.setDWFRDBXM(collectionUnitInformationBasicVice.getDwfrdbxm());
				this.setDWXZQY(collectionUnitInformationBasicVice.getXzqy());
				this.setDJSYYZ(collectionUnitInformationBasicVice.getDjsyyz());
				this.setDWLB(collectionUnitInformationBasicVice.getDwlb());
				this.setDWFRDBZJLX(collectionUnitInformationBasicVice.getDwfrdbzjlx());
				this.setPZJGJB(collectionUnitInformationBasicVice.getPzjgjb());
				this.setKGQK(collectionUnitInformationBasicVice.getKgqk());
				this.setDWJJLX(collectionUnitInformationBasicVice.getDwjjlx());
				this.setDJZCH(collectionUnitInformationBasicVice.getDjzch());
				this.setDWLSGX(collectionUnitInformationBasicVice.getDwlsgx());
				this.setDWDZ(collectionUnitInformationBasicVice.getDwdz());
				this.setZZJGDM(collectionUnitInformationBasicVice.getZzjgdm());
				this.setDWMC(collectionUnitInformationBasicVice.getDwmc());
				this.setDWZH(collectionUnitInformationBasicVice.getDwzh());
				this.setDWSSHY(collectionUnitInformationBasicVice.getDwsshy());
				this.setDWZHZT(collectionUnitInformationBasicVice.getDwywmx().getUnit().getCollectionUnitAccount().getDwzhzt());
			}});

			this.setJBRXX(new GetUnitAcctAlterResJBRXX() {{

				this.setJBRZJLX(collectionUnitInformationBasicVice.getJbrzjlx());
				this.setJBRGDDHHM(collectionUnitInformationBasicVice.getJbrgddhhm());
				this.setJBRXM(collectionUnitInformationBasicVice.getJbrxm());
				this.setJBRZJHM(collectionUnitInformationBasicVice.getJbrzjhm());
				this.setJBRSJHM(collectionUnitInformationBasicVice.getJbrsjhm());
				this.setCZY(collectionUnitInformationBasicVice.getDwywmx().getExtension().getCzy());
				this.setYWWD(collectionUnitInformationBasicVice.getDwywmx().getExtension().getYwwd().getMingCheng());
			}});

			this.setWTYHXX(new GetUnitAcctAlterResWTYHXX() {{

				this.setSTYHDM(collectionUnitInformationBasicVice.getStyhdm());
				this.setSTYHMC(collectionUnitInformationBasicVice.getStyhmc());

			}});

			this.setDELTA(new ArrayList<String>(){{


				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getCollectionUnitAccount().getExtension().getFxzhkhyh(),collectionUnitInformationBasicVice.getFxzhkhyh())){this.add("FXZHKHYH");}
				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getExtension().getBeiZhu(),collectionUnitInformationBasicVice.getBeiZhu())){this.add("BeiZhu");}
				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getDwfxr(),collectionUnitInformationBasicVice.getDwfxr())){this.add("DWFXR");}
				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getCollectionUnitAccount().getExtension().getFxzhhm(),collectionUnitInformationBasicVice.getFxzhhm())){this.add("FXZHHM");}
				//if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getExtension().getDwschjny(),collectionUnitInformationBasicVice.getDwschjny())){this.add("DWSCHJNY");}
				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getCollectionUnitAccount().getExtension().getFxzh(),collectionUnitInformationBasicVice.getFxzh())){this.add("FXZH");}
				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getDwslrq(),collectionUnitInformationBasicVice.getDwslrq())){this.add("DWSLRQ");}
				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getExtension().getCzhm(),collectionUnitInformationBasicVice.getDwczhm())){this.add("CZHM");}
				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getExtension().getDwlxdh(),collectionUnitInformationBasicVice.getDwlxdh())){this.add("DWLXDH");}
				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getDwdzxx(),collectionUnitInformationBasicVice.getDwdzxx())){this.add("DWDZXX");}
				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getDwyb(),collectionUnitInformationBasicVice.getDwyb())){this.add("DWYB");}
				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getExtension().getPzjgmc(),collectionUnitInformationBasicVice.getPzjgmc())){this.add("PZJGMC");}
				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getDwfrdbzjhm(),collectionUnitInformationBasicVice.getDwfrdbzjhm())){this.add("DWFRDBZJHM");}
				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getDwfrdbxm(),collectionUnitInformationBasicVice.getDwfrdbxm())){this.add("DWFRDBXM");}
				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getExtension().getDwxzqy(),collectionUnitInformationBasicVice.getXzqy())){this.add("DWXZQY");}
				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getExtension().getDjsyyz(),collectionUnitInformationBasicVice.getDjsyyz())){this.add("DJSYYZ");}
				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getExtension().getDwlb(),collectionUnitInformationBasicVice.getDwlb())){this.add("DWLB");}
				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getDwfrdbzjlx(),collectionUnitInformationBasicVice.getDwfrdbzjlx())){this.add("DWFRDBZJLX");}
				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getExtension().getPzjgjb(),collectionUnitInformationBasicVice.getPzjgjb())){this.add("PZJGJB");}
				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getExtension().getKgqk(),collectionUnitInformationBasicVice.getKgqk())){this.add("KGQK");}
				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getDwjjlx(),collectionUnitInformationBasicVice.getDwjjlx())){this.add("DWJJLX");}
				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getExtension().getDjzch(),collectionUnitInformationBasicVice.getDjzch())){this.add("DJZCH");}
				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getDwlsgx(),collectionUnitInformationBasicVice.getDwlsgx())){this.add("DWLSGX");}
				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getDwdz(),collectionUnitInformationBasicVice.getDwdz())){this.add("DWDZ");}
				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getZzjgdm(),collectionUnitInformationBasicVice.getZzjgdm())){this.add("ZZJGDM");}
				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getDwmc(),collectionUnitInformationBasicVice.getDwmc())){this.add("DWMC");}
				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getDwzh(),collectionUnitInformationBasicVice.getDwzh())){this.add("DWZH");}
				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getDwsshy(),collectionUnitInformationBasicVice.getDwsshy())){this.add("DWSSHY");}
				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getJbrzjlx(),collectionUnitInformationBasicVice.getJbrzjlx())){this.add("JBRZJLX");}
				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getJbrgddhhm(),collectionUnitInformationBasicVice.getJbrgddhhm())){this.add("JBRGDDHHM");}
				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getJbrxm(),collectionUnitInformationBasicVice.getJbrxm())){this.add("JBRXM");}
				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getJbrzjhm(),collectionUnitInformationBasicVice.getJbrzjhm())){this.add("JBRZJHM");}
				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getJbrsjhm(),collectionUnitInformationBasicVice.getJbrsjhm())){this.add("JBRSJHM");}
				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getStyhdm(),collectionUnitInformationBasicVice.getStyhdm())){this.add("STYHDM");}
				if(!UnitAcctAlterImpl.isEqualTo(commonUnit.getStyhmc(),collectionUnitInformationBasicVice.getStyhmc())){this.add("STYHMC");}

			}});
		}};

	}

	private static boolean isEqualTo(Object obj1, Object obj2){

		if(obj1==null||"".equals(obj1)){ return obj2 == null || "".equals(obj2);}

		if(obj1 instanceof Date && (obj2 != null &&obj2 instanceof Date)){

			return ((Date) obj1).getTime() == ((Date) obj2).getTime();
		}

		if(obj1 instanceof BigDecimal && (obj2 != null &&obj2 instanceof BigDecimal)){

			return (((BigDecimal) obj1).compareTo((BigDecimal) obj2)) == 0;
		}


		return obj1.equals(obj2);
	}

	@Override
	public AddUnitAcctSetRes reUnitAccountAlter(TokenContext tokenContext,String YWLSH, UnitAcctAlterPut unitAcctAlterPut) {

		//region //参数检查
		boolean allowNull = unitAcctAlterPut.getCZLX().equals("0");



		//if(!DateUtil.isFollowFormat(unitAcctAlterPut.getDWDJXX().getDWSCHJNY()/*单位首次汇缴年月*/,format,allowNull)){throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"单位首次汇缴年月");}

		if(!DateUtil.isFollowFormat(unitAcctAlterPut.getDWDJXX().getDWSLRQ()/*单位设立日期*/,format,allowNull)){throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"单位设立日期");}

		if(!StringUtil.isDigits(unitAcctAlterPut.getDWDJXX().getDWFXR()/*单位发薪日*/,allowNull)){throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"单位发薪日");}
		//endregion

		//region //必要字段查询&完整性验证
		CCollectionUnitInformationBasicVice collectionUnitInformationBasicVice = DAOBuilder.instance(this.collectionUnitInformationBasicViceDAO).searchFilter(new HashMap<String, Object>() {{

			this.put("dwywmx.ywlsh", YWLSH);

		}}).getObject(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e); }

		});


		if (collectionUnitInformationBasicVice == null||collectionUnitInformationBasicVice.getDwzh()==null||collectionUnitInformationBasicVice.getDwywmx()==null||collectionUnitInformationBasicVice.getDwywmx().getUnit()==null||collectionUnitInformationBasicVice.getDwywmx().getUnit().getCollectionUnitAccount()==null) {

			throw new ErrorException(ReturnEnumeration.Data_MISS,"业务记录");
		}

		if(!tokenContext.getUserInfo().getCZY().equals(collectionUnitInformationBasicVice.getDwywmx().getExtension().getCzy())){

			throw new ErrorException(ReturnEnumeration.Permission_Denied);

		}
		StCollectionUnitBusinessDetails collectionUnitBusinessDetails = collectionUnitInformationBasicVice.getDwywmx();

		CCollectionUnitBusinessDetailsExtension collectionUnitBusinessDetailsExtension = collectionUnitBusinessDetails.getExtension();

		//endregion

		//region //字段填充

		//CZY
		collectionUnitInformationBasicVice.setCzy(tokenContext.getUserInfo().getCZY()/*操作员*/);
		collectionUnitBusinessDetailsExtension.setCzy(tokenContext.getUserInfo().getCZY()/*操作员*/);


		// DWDJXX
		collectionUnitInformationBasicVice.setFxzhkhyh(unitAcctAlterPut.getDWDJXX().getFXZHKHYH()/*发薪账号开户银行*/);
		collectionUnitInformationBasicVice.setDwfxr(unitAcctAlterPut.getDWDJXX().getDWFXR()/*单位发薪日*/);
		collectionUnitInformationBasicVice.setBeiZhu(unitAcctAlterPut.getDWDJXX().getBeiZhu()/*备注*/);
		collectionUnitBusinessDetailsExtension.setBeizhu(unitAcctAlterPut.getDWDJXX().getBeiZhu()/*备注*/);
		collectionUnitInformationBasicVice.setFxzhhm(unitAcctAlterPut.getDWDJXX().getFXZHHM()/*发薪账号户名*/);
		//collectionUnitInformationBasicVice.setDwschjny(DateUtil.safeStr2DBDate(format,unitAcctAlterPut.getDWDJXX().getDWSCHJNY()/*单位首次汇缴年月*/,DateUtil.dbformatYear_Month));
		collectionUnitInformationBasicVice.setFxzh(unitAcctAlterPut.getDWDJXX().getFXZH()/*发薪账号*/);
		collectionUnitInformationBasicVice.setDwslrq(DateUtil.safeStr2Date(format,unitAcctAlterPut.getDWDJXX().getDWSLRQ()/*单位设立日期*/));


		// WTYHXX
		collectionUnitInformationBasicVice.setStyhdm(unitAcctAlterPut.getWTYHXX().getSTYHDM()/*受托银行代码*/);
		collectionUnitInformationBasicVice.setStyhmc(unitAcctAlterPut.getWTYHXX().getSTYHMC()/*受托银行名称*/);

		//YWWD
		collectionUnitInformationBasicVice.setYwwd(tokenContext.getUserInfo().getYWWD()/*业务网点*/);
		CAccountNetwork network = DAOBuilder.instance(cAccountNetworkDAO).searchFilter(new HashMap<String, Object>() {
			{
				this.put("id",tokenContext.getUserInfo().getYWWD());
			}
		}).getObject(new DAOBuilder.ErrorHandler() {
			@Override
			public void error(Exception e) {
				throw new ErrorException(e);
			}
		});
		collectionUnitBusinessDetailsExtension.setYwwd(network/*业务网点*/);

		// DWLXFS
		collectionUnitInformationBasicVice.setDwczhm(unitAcctAlterPut.getDWLXFS().getCZHM()/*传真号码*/);
		collectionUnitInformationBasicVice.setDwdzxx(unitAcctAlterPut.getDWLXFS().getDWDZXX()/*单位电子信箱 */);
		collectionUnitInformationBasicVice.setDwyb(unitAcctAlterPut.getDWLXFS().getDWYB()/*单位邮编*/);
		collectionUnitInformationBasicVice.setDwlxdh(unitAcctAlterPut.getDWLXFS().getDWLXDH()/*单位联系电话*/);

		// DWGJXX
		collectionUnitInformationBasicVice.setPzjgmc(unitAcctAlterPut.getDWGJXX().getPZJGMC()/*批准机关名称*/);
		collectionUnitInformationBasicVice.setDwfrdbzjhm(unitAcctAlterPut.getDWGJXX().getDWFRDBZJHM()/*单位法人代表证件号码*/);
		collectionUnitInformationBasicVice.setDwjjlx(unitAcctAlterPut.getDWGJXX().getDWJJLX()/*单位经济类型*/);
		collectionUnitInformationBasicVice.setDwfrdbxm(unitAcctAlterPut.getDWGJXX().getDWFRDBXM()/*单位法人代表姓名*/);
		collectionUnitInformationBasicVice.setXzqy(unitAcctAlterPut.getDWGJXX().getDWXZQY()/*单位行政区域*/);
		collectionUnitInformationBasicVice.setDjsyyz(unitAcctAlterPut.getDWGJXX().getDJSYYZ()/*登记使用印章*/);
		collectionUnitInformationBasicVice.setDwlb(unitAcctAlterPut.getDWGJXX().getDWLB()/*单位类别*/);
		collectionUnitInformationBasicVice.setDwfrdbzjlx(unitAcctAlterPut.getDWGJXX().getDWFRDBZJLX()/*单位法人代表证件类型*/);
		collectionUnitInformationBasicVice.setPzjgjb(unitAcctAlterPut.getDWGJXX().getPZJGJB()/*批准机关级别*/);
		collectionUnitInformationBasicVice.setKgqk(unitAcctAlterPut.getDWGJXX().getKGQK()/*控股情况*/);
		collectionUnitInformationBasicVice.setDjzch(unitAcctAlterPut.getDWGJXX().getDJZCH()/*登记注册号*/);
		collectionUnitInformationBasicVice.setZzjgdm(unitAcctAlterPut.getDWGJXX().getZZJGDM()/*组织机构代码*/);
		collectionUnitInformationBasicVice.setDwlsgx(unitAcctAlterPut.getDWGJXX().getDWLSGX()/*单位隶属关系*/);
		collectionUnitInformationBasicVice.setDwdz(unitAcctAlterPut.getDWGJXX().getDWDZ()/*单位地址*/);
		collectionUnitInformationBasicVice.setDwmc(unitAcctAlterPut.getDWGJXX().getDWMC()/*单位名称*/);
		collectionUnitInformationBasicVice.setDwsshy(unitAcctAlterPut.getDWGJXX().getDWSSHY()/*单位所属行业*/);

		// DWKHBLZL
		collectionUnitInformationBasicVice.setBlzl(unitAcctAlterPut.getDWKHBLZL().getBLZL()/*办理资料*/);
		collectionUnitBusinessDetailsExtension.setBlzl(unitAcctAlterPut.getDWKHBLZL().getBLZL()/*办理资料*/);

		// JBRXX
		collectionUnitInformationBasicVice.setJbrzjlx(unitAcctAlterPut.getJBRXX().getJBRZJLX()/*经办人证件类型 */);
		collectionUnitInformationBasicVice.setJbrgddhhm(unitAcctAlterPut.getJBRXX().getJBRGDDHHM()/*经办人固定电话号码*/);
		collectionUnitInformationBasicVice.setJbrxm(unitAcctAlterPut.getJBRXX().getJBRXM()/*经办人姓名*/);
		collectionUnitInformationBasicVice.setJbrzjhm(unitAcctAlterPut.getJBRXX().getJBRZJHM()/*经办人证件号码*/);
		collectionUnitInformationBasicVice.setJbrsjhm(unitAcctAlterPut.getJBRXX().getJBRSJHM()/*经办人手机号码*/);

		collectionUnitBusinessDetailsExtension.setJbrzjlx(unitAcctAlterPut.getJBRXX().getJBRZJLX()/*经办人证件类型 */);
		collectionUnitBusinessDetailsExtension.setJbrxm(unitAcctAlterPut.getJBRXX().getJBRXM()/*经办人姓名*/);
		collectionUnitBusinessDetailsExtension.setJbrzjhm(unitAcctAlterPut.getJBRXX().getJBRZJHM()/*经办人证件号码*/);

		//endregion

		//region //修改状态
		StateMachineUtils.updateState(this.stateMachineService, new HashMap<String, String>(){{

			this.put("0", Events.保存.getEvent());

			this.put("1",Events.通过.getEvent());

		}}.get(unitAcctAlterPut.getCZLX()),new TaskEntity() {{

			this.setStatus(collectionUnitBusinessDetailsExtension.getStep()==null ? "初始状态":collectionUnitBusinessDetailsExtension.getStep());
			this.setTaskId(YWLSH);
			this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));;
			this.setNote("");
			this.setSubtype(BusinessSubType.归集_单位账户变更.getSubType());
			this.setType(BusinessType.Collection);
			this.setOperator(collectionUnitInformationBasicVice.getCzy());
			this.setWorkstation(collectionUnitInformationBasicVice.getYwwd());

		}} ,new StateMachineUtils.StateChangeHandler() {

			@Override
			public void onStateChange(boolean succeed, String next, Exception e) {

				if(e!=null){throw new ErrorException(e);}

				if(!succeed||next==null){ return;}

				collectionUnitBusinessDetailsExtension.setStep(next);


				if(StringUtil.isIntoReview(next,null)){

				    collectionUnitBusinessDetailsExtension.setDdsj(new Date());

				}

				DAOBuilder.instance(collectionUnitInformationBasicViceDAO).entity(collectionUnitInformationBasicVice).save(new DAOBuilder.ErrorHandler() {
					@Override
					public void error(Exception e) { throw new ErrorException(e); }

				});

				if(next.equals(CollectionBusinessStatus.办结.getName())){

					doUnitAcctAlter(tokenContext,collectionUnitBusinessDetails.getYwlsh());
				}
			}
		});

		//endregion

		//region //唯一性验证

		if("1".equals(unitAcctAlterPut.getCZLX())&&!DAOBuilder.instance(this.commonUnitDAO).extension(new IBaseDAO.CriteriaExtension() {
			@Override
			public void extend(Criteria criteria) {

				criteria.add(Restrictions.or(
						Restrictions.eq("dwdz",collectionUnitInformationBasicVice.getDwdz()),
						Restrictions.eq("dwmc",collectionUnitInformationBasicVice.getDwmc())

				));
				criteria.add(Restrictions.ne("dwzh",collectionUnitInformationBasicVice.getDwzh()));
			}
		}).isUnique(new DAOBuilder.ErrorHandler() {
			@Override
			public void error(Exception e) { throw new ErrorException(e); }

		})){

			throw new ErrorException(ReturnEnumeration.Data_Already_Eeist,"单位名称或地址");
		}
		//endregion

		//region //在途验证

		if(unitAcctAlterPut.getCZLX().equals("1")&&!DAOBuilder.instance(this.collectionUnitInformationBasicViceDAO).extension(new IBaseDAO.CriteriaExtension() {
			@Override
			public void extend(Criteria criteria) {

				criteria.add(Restrictions.not(Restrictions.in(CriteriaUtils.addAlias(criteria,"dwywmx.cCollectionUnitBusinessDetailsExtension.step"),(Collection)CollectionUtils.flatmap(CollectionUtils.merge(CollectionBusinessStatus.办结.getSubTypes(), CollectionBusinessStatus.新建.getSubTypes()), new CollectionUtils.Transformer<CollectionBusinessStatus, String>() {

					@Override
					public String tansform(CollectionBusinessStatus var1) { return var1.getName(); }

				}))));

				criteria.add(Restrictions.ne("dwywmx.ywlsh",collectionUnitInformationBasicVice.getDwywmx().getYwlsh()));
				criteria.add(Restrictions.or(
						Restrictions.eq("dwdz",collectionUnitInformationBasicVice.getDwdz()),
						Restrictions.eq("dwmc",collectionUnitInformationBasicVice.getDwmc())
				));
				criteria.add(Restrictions.in("cCollectionUnitBusinessDetailsExtension.czmc",CollectionBusinessType.开户.getCode(),CollectionBusinessType.变更.getCode()));

			}
		}).isUnique(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e);}

		})){

			throw new ErrorException(ReturnEnumeration.Business_In_Process,"已有单位正在使用 相同的地址 或 名称 办理开户或变更业务");
		}

		if(unitAcctAlterPut.getCZLX().equals("1")){

			this.iscollectionUnitInformationBasicViceAvailable(collectionUnitInformationBasicVice);

			this.iSaveAuditHistory.saveNormalBusiness(YWLSH,tokenContext, CollectionBusinessType.变更.getName(),"修改");
		}
		//endregion

		return new AddUnitAcctSetRes() {{

			this.setYWLSH(YWLSH);

			this.setStatus("success");

		}};
	}

	@Override
	public PostUnitAcctAlterSubmitRes submitUnitAccountAlter(TokenContext tokenContext,List<String> YWLSHs) {

		//region // 检查参数

		if (YWLSHs == null) { throw new ErrorException(ReturnEnumeration.Parameter_MISS,"业务流水号"); }

		//endregion

		ArrayList<Exception> exceptions = new ArrayList<>();

		for(String YWLSH : YWLSHs) {

			//region //必要字段查询&完整性验证
			CCollectionUnitInformationBasicVice collectionUnitInformationBasicVice = DAOBuilder.instance(this.collectionUnitInformationBasicViceDAO).searchFilter(new HashMap<String, Object>() {{

				this.put("dwywmx.ywlsh", YWLSH);

			}}).getObject(new DAOBuilder.ErrorHandler() {

				@Override
				public void error(Exception e) { exceptions.add(e); }

			});

			if (collectionUnitInformationBasicVice == null || collectionUnitInformationBasicVice.getDwzh() == null || collectionUnitInformationBasicVice.getDwywmx() == null || collectionUnitInformationBasicVice.getDwywmx().getUnit() == null || collectionUnitInformationBasicVice.getDwywmx().getUnit().getCollectionUnitAccount() == null) {

				return new PostUnitAcctAlterSubmitRes() {{

					this.setSBYWLSH(YWLSH);

					this.setStatus("fail");

				}};
			}

			try {

				this.iscollectionUnitInformationBasicViceAvailable(collectionUnitInformationBasicVice);

				this.iSaveAuditHistory.saveNormalBusiness(YWLSH,tokenContext, CollectionBusinessType.变更.getName(),"修改");

				if(!DAOBuilder.instance(this.commonUnitDAO).extension(new IBaseDAO.CriteriaExtension() {
					@Override
					public void extend(Criteria criteria) {

						criteria.add(Restrictions.or(
								Restrictions.eq("dwdz",collectionUnitInformationBasicVice.getDwdz()),
								Restrictions.eq("dwmc",collectionUnitInformationBasicVice.getDwmc())

						));
						criteria.add(Restrictions.ne("dwzh",collectionUnitInformationBasicVice.getDwzh()));
					}
				}).isUnique(new DAOBuilder.ErrorHandler() {
					@Override
					public void error(Exception e) { throw new ErrorException(e); }

				})){

					throw new ErrorException(ReturnEnumeration.Data_Already_Eeist,"单位已存在");
				}
			}catch (Exception e){

				return new PostUnitAcctAlterSubmitRes() {{

					this.setSBYWLSH(YWLSH);

					this.setStatus("fail");

				}};
			}
			StCollectionUnitBusinessDetails collectionUnitBusinessDetails = collectionUnitInformationBasicVice.getDwywmx();

			CCollectionUnitBusinessDetailsExtension collectionUnitBusinessDetailsExtension = collectionUnitBusinessDetails.getExtension();

			if(!tokenContext.getUserInfo().getCZY().equals(collectionUnitInformationBasicVice.getDwywmx().getExtension().getCzy())){

				throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务(" + YWLSH + ")不是由您受理的，不能提交");

			}
			//endregion

			//region //在途验证

			if(!DAOBuilder.instance(this.collectionUnitInformationBasicViceDAO).searchFilter(new HashMap<String, Object>(){{

				this.put("dwmc",collectionUnitInformationBasicVice.getDwmc());

			}}).extension(new IBaseDAO.CriteriaExtension() {
				@Override
				public void extend(Criteria criteria) {

					criteria.add(Restrictions.not(Restrictions.in(CriteriaUtils.addAlias(criteria,"dwywmx.cCollectionUnitBusinessDetailsExtension.step"), (Collection)CollectionUtils.flatmap(CollectionUtils.merge(CollectionBusinessStatus.办结.getSubTypes(), CollectionBusinessStatus.新建.getSubTypes()), new CollectionUtils.Transformer<CollectionBusinessStatus, String>() {

						@Override
						public String tansform(CollectionBusinessStatus var1) { return var1.getName(); }

					}))));
					criteria.add(Restrictions.ne("dwywmx.ywlsh",YWLSH));
					criteria.add(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.czmc",CollectionBusinessType.变更.getCode()));
				}
			}).isUnique(new DAOBuilder.ErrorHandler() {

				@Override
				public void error(Exception e) { exceptions.add(e);}

			})){

				return new PostUnitAcctAlterSubmitRes() {{

					this.setSBYWLSH(YWLSH);

					this.setStatus("fail");

				}};
			}
			//endregion

			//region //修改状态
			StateMachineUtils.updateState(this.stateMachineService, Events.通过.getEvent(), new TaskEntity() {{

				this.setStatus(collectionUnitBusinessDetailsExtension.getStep()==null ? "初始状态":collectionUnitBusinessDetailsExtension.getStep());
				this.setTaskId(YWLSH);
				this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));;
				this.setNote("");
				this.setSubtype(BusinessSubType.归集_单位账户变更.getSubType());
				this.setType(BusinessType.Collection);
				this.setOperator(collectionUnitInformationBasicVice.getCzy());
				this.setOperator(tokenContext.getUserInfo().getCZY());
				this.setWorkstation(collectionUnitInformationBasicVice.getYwwd());

			}},new StateMachineUtils.StateChangeHandler() {

				@Override
				public void onStateChange(boolean succeed, String next, Exception e) {

					if (e != null) { exceptions.add(e); }

					if (!succeed || next == null || e != null) { return; }

					collectionUnitBusinessDetailsExtension.setStep(next);


					if(StringUtil.isIntoReview(next,null)){

					    collectionUnitBusinessDetailsExtension.setDdsj(new Date());

					}

					DAOBuilder.instance(collectionUnitInformationBasicViceDAO).entity(collectionUnitInformationBasicVice).save(new DAOBuilder.ErrorHandler() {
						@Override
						public void error(Exception e) { exceptions.add(e); }

					});

					if(next.equals(CollectionBusinessStatus.办结.getName())){

						try {

							doUnitAcctAlter(tokenContext,collectionUnitBusinessDetails.getYwlsh());

						} catch (Exception ex){

							exceptions.add(ex);
						}

					}
				}
			});

			if(exceptions.size()!=0){

				return new PostUnitAcctAlterSubmitRes() {{

					this.setSBYWLSH(YWLSH);

					this.setStatus("fail");

				}};
			}
			//endregion
		}

		return new PostUnitAcctAlterSubmitRes() {{

			this.setSBYWLSH(this.getSBYWLSH());

			this.setStatus("success");

		}};

	}


	@Override
	public PageRes<ListUnitAcctAlterResRes> showUnitAccountsAlter(TokenContext tokenContext,String DWZH, String DWMC, String DWLB, String ZhuangTai,String page,String pagesize,String KSSJ,String JSSJ) {

		//region //参数检查

		int page_number = 0;

		int pagesize_number = 0;

		try {

			if(page!=null) {
				page_number = Integer.parseInt(page);
			}

			if(pagesize!=null) {
				pagesize_number = Integer.parseInt(pagesize);
			}
		}catch (Exception e){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"页码");
		}

		//endregion

		//region //必要字段查询
		PageRes pageRes = new PageRes();

		List<StCollectionUnitBusinessDetails> list_business = DAOBuilder.instance(this.collectionUnitBusinessDetailsDAO).searchFilter(new HashMap<String, Object>(){{

			if (StringUtil.notEmpty(DWZH)){ this.put("unitInformationBasicVice.dwzh",DWZH);}

			if (StringUtil.notEmpty(DWMC)) { this.put("unitInformationBasicVice.dwmc", DWMC); }

		}}).searchOption(SearchOption.FUZZY).extension(new IBaseDAO.CriteriaExtension() {

			@Override
			public void extend(Criteria criteria) {
				if (DateUtil.isFollowFormat(KSSJ,formatNYRSF,false)){ criteria.add(Restrictions.ge("created_at",DateUtil.safeStr2Date(formatNYRSF,KSSJ)));}
				if (DateUtil.isFollowFormat(JSSJ,formatNYRSF,false)){ criteria.add(Restrictions.lt("created_at",DateUtil.safeStr2Date(formatNYRSF,JSSJ)));}

				if(!StringUtil.notEmpty(DWZH)&&!StringUtil.notEmpty(DWMC)) {

					criteria.createAlias("unitInformationBasicVice", "unitInformationBasicVice");
				}

				if (StringUtil.notEmpty(DWLB)) { criteria.add(Restrictions.eq("unitInformationBasicVice.dwlb", DWLB)); }

				criteria.createAlias("cCollectionUnitBusinessDetailsExtension","cCollectionUnitBusinessDetailsExtension");

				if (StringUtil.notEmpty(ZhuangTai) && !ZhuangTai.equals(CollectionBusinessStatus.所有.getName())&&!ZhuangTai.equals(CollectionBusinessStatus.待审核.getName())) {

					criteria.add(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.step", ZhuangTai));
				}

				if(CollectionBusinessStatus.待审核.getName().equals(ZhuangTai)){

					criteria.add(Restrictions.like("cCollectionUnitBusinessDetailsExtension.step", LoanBussinessStatus.待某人审核.getName()));
				}

				criteria.add(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.czmc", CollectionBusinessType.变更.getCode()));

				criteria.createAlias("cCollectionUnitBusinessDetailsExtension.ywwd","ywwd");

				if(!"1".equals(tokenContext.getUserInfo().getYWWD())){
					criteria.add(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.ywwd.id",tokenContext.getUserInfo().getYWWD()));
				}
			}

		}).pageOption(pageRes,pagesize_number,page_number).getList(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e); }

		});

		//endregion

		return new PageRes<ListUnitAcctAlterResRes>() {{

			this.setResults(new ArrayList<ListUnitAcctAlterResRes>() {{

				for (StCollectionUnitBusinessDetails collectionUnitBusinessDetails : list_business) {

					CCollectionUnitInformationBasicVice collectionIndividualAccountBasicVice = collectionUnitBusinessDetails.getUnitInformationBasicVice()==null?new CCollectionUnitInformationBasicVice():collectionUnitBusinessDetails.getUnitInformationBasicVice();

					this.add(new ListUnitAcctAlterResRes() {{

						this.setId(collectionUnitBusinessDetails.getId());
						this.setZZJGDM(collectionIndividualAccountBasicVice.getZzjgdm());
						this.setYWLSH(collectionIndividualAccountBasicVice.getDwywmx().getYwlsh());
						this.setSLSJ(DateUtil.date2Str(collectionIndividualAccountBasicVice.getSlsj(), formatNYRSF));
						this.setZhuangTai(collectionIndividualAccountBasicVice.getDwywmx().getExtension().getStep());
						String ywwdmc = collectionUnitBusinessDetails.getExtension().getYwwd().getMingCheng();
						this.setXZQY(ywwdmc);
						this.setDWMC(collectionIndividualAccountBasicVice.getDwmc());
						this.setDWZH(collectionIndividualAccountBasicVice.getDwzh());
						this.setCZY(collectionIndividualAccountBasicVice.getDwywmx().getExtension().getCzy());
						this.setDWLB(collectionIndividualAccountBasicVice.getDwlb());
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
	public PageResNew<ListUnitAcctAlterResRes> showUnitAccountsAlter(TokenContext tokenContext, String DWZH, String DWMC, String DWLB, String ZhuangTai, String marker, String action, String pagesize, String KSSJ, String JSSJ) {
		//region //参数检查

		int pagesize_number = 0;

		try {

			if(pagesize!=null) { pagesize_number = Integer.parseInt(pagesize); }

		}catch (Exception e){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"页码");
		}

		//endregion

		//region //必要字段查询
		PageRes pageRes = new PageRes();

		List<StCollectionUnitBusinessDetails> list_business = DAOBuilder.instance(this.collectionUnitBusinessDetailsDAO).searchFilter(new HashMap<String, Object>(){{

			if (StringUtil.notEmpty(DWZH)){ this.put("unitInformationBasicVice.dwzh",DWZH);}

			if (StringUtil.notEmpty(DWMC)) { this.put("unitInformationBasicVice.dwmc", DWMC); }

		}}).searchOption(SearchOption.FUZZY).extension(new IBaseDAO.CriteriaExtension() {

			@Override
			public void extend(Criteria criteria) {
				if (DateUtil.isFollowFormat(KSSJ,formatNYRSF,false)){ criteria.add(Restrictions.ge("created_at",DateUtil.safeStr2Date(formatNYRSF,KSSJ)));}
				if (DateUtil.isFollowFormat(JSSJ,formatNYRSF,false)){ criteria.add(Restrictions.lt("created_at",DateUtil.safeStr2Date(formatNYRSF,JSSJ)));}

				if(!StringUtil.notEmpty(DWZH)&&!StringUtil.notEmpty(DWMC)) {

					criteria.createAlias("unitInformationBasicVice", "unitInformationBasicVice");
				}

				if (StringUtil.notEmpty(DWLB)) { criteria.add(Restrictions.eq("unitInformationBasicVice.dwlb", DWLB)); }

				criteria.createAlias("cCollectionUnitBusinessDetailsExtension","cCollectionUnitBusinessDetailsExtension");

				if (StringUtil.notEmpty(ZhuangTai) && !ZhuangTai.equals(CollectionBusinessStatus.所有.getName())&&!ZhuangTai.equals(CollectionBusinessStatus.待审核.getName())) {

					criteria.add(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.step", ZhuangTai));
				}

				if(CollectionBusinessStatus.待审核.getName().equals(ZhuangTai)){

					criteria.add(Restrictions.like("cCollectionUnitBusinessDetailsExtension.step", LoanBussinessStatus.待某人审核.getName()));
				}

				criteria.add(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.czmc", CollectionBusinessType.变更.getCode()));

				criteria.createAlias("cCollectionUnitBusinessDetailsExtension.ywwd","ywwd");

				if(!"1".equals(tokenContext.getUserInfo().getYWWD())){
					criteria.add(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.ywwd.id",tokenContext.getUserInfo().getYWWD()));
				}
			}

		}).pageOption(marker,action,pagesize_number).getList(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e); }

		});

		//endregion

		return new PageResNew<ListUnitAcctAlterResRes>() {{

			this.setResults(action,new ArrayList<ListUnitAcctAlterResRes>() {{

				for (StCollectionUnitBusinessDetails collectionUnitBusinessDetails : list_business) {

					CCollectionUnitInformationBasicVice collectionIndividualAccountBasicVice = collectionUnitBusinessDetails.getUnitInformationBasicVice()==null?new CCollectionUnitInformationBasicVice():collectionUnitBusinessDetails.getUnitInformationBasicVice();

					this.add(new ListUnitAcctAlterResRes() {{
						this.setId(collectionUnitBusinessDetails.getId());
						this.setZZJGDM(collectionIndividualAccountBasicVice.getZzjgdm());
						this.setYWLSH(collectionIndividualAccountBasicVice.getDwywmx().getYwlsh());
						this.setSLSJ(DateUtil.date2Str(collectionIndividualAccountBasicVice.getSlsj(), formatNYRSF));
						this.setZhuangTai(collectionIndividualAccountBasicVice.getDwywmx().getExtension().getStep());
						String ywwdmc = collectionUnitBusinessDetails.getExtension().getYwwd().getMingCheng();
						this.setXZQY(ywwdmc);
						this.setDWMC(collectionIndividualAccountBasicVice.getDwmc());
						this.setDWZH(collectionIndividualAccountBasicVice.getDwzh());
						this.setCZY(collectionIndividualAccountBasicVice.getDwywmx().getExtension().getCzy());
						this.setDWLB(collectionIndividualAccountBasicVice.getDwlb());
					}});
				}
			}});

		}};
	}


	@Override
	public CommonResponses headUnitAcctsAlterReceipt(TokenContext tokenContext, String YWLSH) {

		//region // 检查参数

		if (YWLSH == null) { throw new ErrorException(ReturnEnumeration.Parameter_MISS,"业务流水号"); }

		//endregion

		//region  // 必要字段查询
		CCollectionUnitInformationBasicVice collectionUnitInformationBasicVice = DAOBuilder.instance(this.collectionUnitInformationBasicViceDAO).searchFilter(new HashMap<String, Object>() {{

			this.put("dwywmx.ywlsh", YWLSH);

		}}).getObject(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw  new ErrorException(e); }

		});

		if (collectionUnitInformationBasicVice == null || collectionUnitInformationBasicVice.getDwywmx()==null||collectionUnitInformationBasicVice.getDwywmx().getExtension()==null) {

			throw new  ErrorException(ReturnEnumeration.Data_MISS,"业务记录");
		}

		if(!CollectionBusinessStatus.办结.getName().equals(collectionUnitInformationBasicVice.getDwywmx().getExtension().getStep())){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"业务状态");

		}

		//endregion
		HeadUnitAcctBasicRes result  = new HeadUnitAcctBasicRes();
		HeadUnitAcctBasicResDWKHBLZL DWKHBLZL = new HeadUnitAcctBasicResDWKHBLZL();
		HeadUnitAcctBasicResDWDJXX DWDJXX = new HeadUnitAcctBasicResDWDJXX();
		HeadUnitAcctBasicResDWLXFS DWLXFS = new HeadUnitAcctBasicResDWLXFS();
		HeadUnitAcctBasicResDWGJXX DWGJXX = new HeadUnitAcctBasicResDWGJXX();
		HeadUnitAcctBasicResJBRXX JBRXX = new HeadUnitAcctBasicResJBRXX();
		HeadUnitAcctBasicResWTYHXX WTYHXX = new HeadUnitAcctBasicResWTYHXX();
		StCommonUnit commonUnit = DAOBuilder.instance(this.commonUnitDAO).searchFilter(new HashMap<String, Object>(){{

			this.put("dwzh",collectionUnitInformationBasicVice.getDwzh());

		}}).getObject(new DAOBuilder.ErrorHandler() {

			@Override

			public void error(Exception e) { throw new ErrorException(e);}
		});
		DWKHBLZL.setBLZL(collectionUnitInformationBasicVice.getBlzl());
		result.setDWKHBLZL(DWKHBLZL);
		DWDJXX.setGRJCBL((commonUnit.getCollectionUnitAccount().getGrjcbl()==null?"0" : commonUnit.getCollectionUnitAccount().getGrjcbl().multiply(new BigDecimal("100")).setScale(2)) + "");
		DWDJXX.setFXZHKHYH(collectionUnitInformationBasicVice.getFxzhkhyh());
		DWDJXX.setBeiZhu(collectionUnitInformationBasicVice.getBeiZhu());
		DWDJXX.setDWFXR(collectionUnitInformationBasicVice.getDwfxr());
		DWDJXX.setDWJCBL((commonUnit.getCollectionUnitAccount().getDwjcbl()==null?"0":commonUnit.getCollectionUnitAccount().getDwjcbl().multiply(new BigDecimal("100")).setScale(2)) + "");
		DWDJXX.setDWSCHJNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month,commonUnit.getExtension().getDwschjny(), formatNY));
		DWDJXX.setFXZH(collectionUnitInformationBasicVice.getFxzh());
		DWDJXX.setDWSLRQ(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month,collectionUnitInformationBasicVice.getDwslrq(), format));
		result.setDWDJXX(DWDJXX);
		DWLXFS.setCZHM(collectionUnitInformationBasicVice.getDwczhm());
		DWLXFS.setDWLXDH(collectionUnitInformationBasicVice.getDwlxdh());
		DWLXFS.setDWDZXX(collectionUnitInformationBasicVice.getDwdzxx());
		DWLXFS.setDWYB(collectionUnitInformationBasicVice.getDwyb());
		result.setDWLXFS(DWLXFS);
		DWGJXX.setPZJGMC(collectionUnitInformationBasicVice.getPzjgmc());
		DWGJXX.setDWFRDBZJHM(collectionUnitInformationBasicVice.getDwfrdbzjhm());
		DWGJXX.setYWLSH(collectionUnitInformationBasicVice.getDwywmx().getYwlsh());
		DWGJXX.setDWFRDBXM(collectionUnitInformationBasicVice.getDwfrdbxm());
		DWGJXX.setDWXZQY(collectionUnitInformationBasicVice.getXzqy());
		DWGJXX.setDJSYYZ(collectionUnitInformationBasicVice.getDjsyyz());
		SingleDictionaryDetail dictionaryDetail = iDictionaryService.getSingleDetail(collectionUnitInformationBasicVice.getDwlb(),"UnitClass");
		DWGJXX.setDWLB(dictionaryDetail!=null?dictionaryDetail.getName():"");
		DWGJXX.setDWFRDBZJLX(collectionUnitInformationBasicVice.getDwfrdbzjlx());
		DWGJXX.setPZJGJB(collectionUnitInformationBasicVice.getPzjgjb());
		DWGJXX.setKGQK(collectionUnitInformationBasicVice.getKgqk());
		DWGJXX.setDWJJLX(collectionUnitInformationBasicVice.getDwjjlx());
		DWGJXX.setDJZCH(collectionUnitInformationBasicVice.getDjzch());
		DWGJXX.setDWLSGX(collectionUnitInformationBasicVice.getDwlsgx());
		DWGJXX.setDWDZ(collectionUnitInformationBasicVice.getDwdz());
		DWGJXX.setZZJGDM(collectionUnitInformationBasicVice.getZzjgdm());
		DWGJXX.setDWMC(collectionUnitInformationBasicVice.getDwmc());
		DWGJXX.setDWZH(collectionUnitInformationBasicVice.getDwzh());
		DWGJXX.setDWSSHY(collectionUnitInformationBasicVice.getDwsshy());
		DWGJXX.setYWWD(collectionUnitBusinessDetailsDAO.getByYwlsh(YWLSH).getExtension().getYwwd().getMingCheng());
		DWGJXX.setCZY(collectionUnitInformationBasicVice.getDwywmx().getExtension().getCzy());
		result.setDWGJXX(DWGJXX);
		JBRXX.setJBRZJLX(collectionUnitInformationBasicVice.getJbrzjlx());
		JBRXX.setJBRGDDHHM(collectionUnitInformationBasicVice.getJbrgddhhm());
		JBRXX.setJBRXM(collectionUnitInformationBasicVice.getJbrxm());
		JBRXX.setJBRZJHM(collectionUnitInformationBasicVice.getJbrzjhm());
		JBRXX.setJBRSJHM(collectionUnitInformationBasicVice.getJbrsjhm());
		result.setJBRXX(JBRXX);
		WTYHXX.setSTYHDM(collectionUnitInformationBasicVice.getStyhdm());
		WTYHXX.setSTYHMC(collectionUnitInformationBasicVice.getStyhmc());
		result.setWTYHXX(WTYHXX);
		//审核人，该条记录审核通过的操作员
		CAuditHistory cAuditHistory = DAOBuilder.instance(icAuditHistoryDAO).searchFilter(new HashMap<String, Object>() {
			{
				this.put("ywlsh", YWLSH);
				this.put("shjg","01");
			}
		}).getObject(new DAOBuilder.ErrorHandler() {
			@Override
			public void error(Exception e) {
				throw new ErrorException(e);
			}
		});
		if(cAuditHistory!=null){
			result.setSHR(cAuditHistory.getCzy());
		}
		String id = pdfService.getUnitAcctsSetReceiptPdf(result,CollectionBusinessType.变更.getCode());
		System.out.println("生成id的值："+id);
		CommonResponses commonResponses = new CommonResponses();
		commonResponses.setId(id);
		commonResponses.setState("success");
		return commonResponses;

	}

	public void doUnitAcctAlter(TokenContext tokenContext,String YWLSH) {


		//region //检查参数

		if (YWLSH == null) { throw new ErrorException(ReturnEnumeration.Parameter_MISS,"业务流水号"); }

		//endregion

		//region //必要字段查询&完整性验证
		CCollectionUnitInformationBasicVice collectionUnitInformationBasicVice = DAOBuilder.instance(this.collectionUnitInformationBasicViceDAO).searchFilter(new HashMap<String,Object>(){{

			this.put("dwywmx.ywlsh", YWLSH);

			this.put("dwywmx.cCollectionUnitBusinessDetailsExtension.czmc", CollectionBusinessType.变更.getCode());

		}}).getObject(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e); }

		});

		if(collectionUnitInformationBasicVice==null||collectionUnitInformationBasicVice.getDwywmx()==null||collectionUnitInformationBasicVice.getDwywmx().getExtension()==null){

			throw new ErrorException(ReturnEnumeration.Data_MISS,"业务记录");
		}

		if(collectionUnitInformationBasicVice.getDwywmx().getUnit()==null||collectionUnitInformationBasicVice.getDwywmx().getUnit().getCollectionUnitAccount()==null||collectionUnitInformationBasicVice.getDwywmx().getUnit().getCollectionUnitAccount().getExtension()==null||collectionUnitInformationBasicVice.getDwywmx().getUnit().getExtension()==null){

			throw new ErrorException(ReturnEnumeration.Data_MISS,"当前账号对应的单位信息不存在");
		}
		//endregion

		//region //必要字段声明&关系配置
		StCollectionUnitBusinessDetails collectionUnitBusinessDetails = collectionUnitInformationBasicVice.getDwywmx();

		CCollectionUnitBusinessDetailsExtension collectionUnitBusinessDetailsExtension = collectionUnitBusinessDetails.getExtension();

		StCommonUnit commonUnit = collectionUnitInformationBasicVice.getDwywmx().getUnit();

		StCollectionUnitAccount collectionUnitAccount = commonUnit.getCollectionUnitAccount();

		CCollectionUnitAccountExtension collectionUnitAccountExtension = collectionUnitAccount.getExtension();

		CCommonUnitExtension commonUnitExtension = commonUnit.getExtension();

		//endregion

		//region //字段填充
		//common
		commonUnit.setDwmc(collectionUnitInformationBasicVice.getDwmc());
		commonUnit.setDwzh(collectionUnitInformationBasicVice.getDwzh());
		commonUnit.setDwdz(collectionUnitInformationBasicVice.getDwdz());
		commonUnit.setDwfrdbxm(collectionUnitInformationBasicVice.getDwfrdbxm());
		commonUnit.setDwfrdbzjlx(collectionUnitInformationBasicVice.getDwfrdbzjlx());
		commonUnit.setDwfrdbzjhm(collectionUnitInformationBasicVice.getDwfrdbzjhm());
		commonUnit.setDwlsgx(collectionUnitInformationBasicVice.getDwlsgx());
		commonUnit.setDwjjlx(collectionUnitInformationBasicVice.getDwjjlx());
		commonUnit.setDwsshy(collectionUnitInformationBasicVice.getDwsshy());
		commonUnit.setDwyb(collectionUnitInformationBasicVice.getDwyb());
		commonUnit.setDwdzxx(collectionUnitInformationBasicVice.getDwdzxx());
		commonUnit.setDwfxr(collectionUnitInformationBasicVice.getDwfxr());
		commonUnit.setJbrxm(collectionUnitInformationBasicVice.getJbrxm());
		commonUnit.setJbrgddhhm(collectionUnitInformationBasicVice.getJbrgddhhm());
		commonUnit.setJbrsjhm(collectionUnitInformationBasicVice.getJbrsjhm());
		commonUnit.setJbrzjlx(collectionUnitInformationBasicVice.getJbrzjlx());
		commonUnit.setJbrzjhm(collectionUnitInformationBasicVice.getJbrzjhm());
		commonUnit.setZzjgdm(collectionUnitInformationBasicVice.getZzjgdm());
		commonUnit.setDwslrq(collectionUnitInformationBasicVice.getDwslrq());
		commonUnit.setDwkhrq(commonUnit.getDwkhrq());
		commonUnit.setStyhmc(collectionUnitInformationBasicVice.getStyhmc());
		commonUnit.setStyhdm(collectionUnitInformationBasicVice.getStyhdm());

		collectionUnitAccount.setDwzh(commonUnit.getDwzh());
		collectionUnitAccount.setDwjcbl(collectionUnitAccount.getDwjcbl());
		collectionUnitAccount.setGrjcbl(collectionUnitAccount.getGrjcbl());
		collectionUnitAccount.setDwjcrs(collectionUnitAccount.getDwjcrs());
		collectionUnitAccount.setDwfcrs(collectionUnitAccount.getDwfcrs());
		collectionUnitAccount.setDwzhye(collectionUnitAccount.getDwzhye());
		collectionUnitAccount.setDwxhrq(collectionUnitAccount.getDwxhrq()/*销户日期不填*/);
		collectionUnitAccount.setDwxhyy(collectionUnitAccount.getDwxhyy()/*销户原因不填*/);
		collectionUnitAccount.setDwzhzt(collectionUnitAccount.getDwzhzt()/*todo 单位账号状态*/);
		collectionUnitAccount.setJzny(collectionUnitAccount.getJzny());

		collectionUnitAccountExtension.setFxzhkhyh(collectionUnitInformationBasicVice.getFxzhkhyh());
		collectionUnitAccountExtension.setFxzhhm(collectionUnitInformationBasicVice.getFxzhhm());
		collectionUnitAccountExtension.setFxzh(collectionUnitInformationBasicVice.getFxzh());

		commonUnitExtension.setDwlb(collectionUnitInformationBasicVice.getDwlb());
		commonUnitExtension.setKgqk(collectionUnitInformationBasicVice.getKgqk());
		commonUnitExtension.setDwxzqy(collectionUnitInformationBasicVice.getXzqy());
		commonUnitExtension.setPzjgmc(collectionUnitInformationBasicVice.getPzjgmc());
		commonUnitExtension.setPzjgjb(collectionUnitInformationBasicVice.getPzjgjb());
		commonUnitExtension.setDjzch(collectionUnitInformationBasicVice.getDjzch());
		commonUnitExtension.setDjsyyz(collectionUnitInformationBasicVice.getDjsyyz());
		commonUnitExtension.setDwlxdh(collectionUnitInformationBasicVice.getDwlxdh());
		commonUnitExtension.setDwschjny(commonUnitExtension.getDwschjny());
		commonUnitExtension.setCzhm(collectionUnitInformationBasicVice.getDwczhm());
		commonUnitExtension.setDwzl(collectionUnitInformationBasicVice.getBlzl());
		commonUnitExtension.setBeiZhu(collectionUnitInformationBasicVice.getBeiZhu());

		collectionUnitBusinessDetailsExtension.setBjsj(new Date());

		//endregion

		DAOBuilder.instance(this.collectionUnitInformationBasicViceDAO).entity(collectionUnitInformationBasicVice).save(new DAOBuilder.ErrorHandler() {
			@Override
			public void error(Exception e) { throw new ErrorException(e);}
		});


		this.iSaveAuditHistory.saveNormalBusiness(YWLSH,tokenContext, CollectionBusinessType.变更.getName(),"办结");
		Msg rpcMsg = accountRpcService.updateAuth(collectionUnitInformationBasicVice.getDwywmx().getUnit().getCollectionUnitAccount().getId(),ResUtils.noneAdductionValue(RpcAuth.class,new RpcAuth(){{
			this.setEmail(collectionUnitInformationBasicVice.getDwdzxx() );
			this.setType(3);
			this.setUser_id(collectionUnitInformationBasicVice.getDwywmx().getUnit().getCollectionUnitAccount().getId());
			this.setUsername(collectionUnitInformationBasicVice.getDwywmx().getUnit().getCollectionUnitAccount().getDwzh());
			this.setState(1);
		}}));

		if(rpcMsg.getCode() != ReturnCode.Success){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,rpcMsg.getMsg());
		}

	}

	private void iscollectionUnitInformationBasicViceAvailable(CCollectionUnitInformationBasicVice collectionUnitInformationBasicVice){

//		List<StCommonPolicy> list_commonPolicy = DAOBuilder.instance(commonPolicyDAO).searchFilter(new HashMap<String, Object>(){{
//
//			this.put("id",Arrays.asList("GRJCBLSX","GRJCBLXX","DWJCBLSX","DWJCBLXX"));
//
//		}}).getList(new DAOBuilder.ErrorHandler() {
//
//			@Override
//			public void error(Exception e) { throw new ErrorException(e);}
//
//		});
//
//		StCommonPolicy commonPolicy = new StCommonPolicy();
//
//		list_commonPolicy.forEach(new Consumer<StCommonPolicy>() {
//			@Override
//			public void accept(StCommonPolicy stCommonPolicy) {
//
//				if("GRJCBLSX".equals(stCommonPolicy.getId())){
//
//					commonPolicy.setGrjcblsx(stCommonPolicy.getGrjcblsx()==null?BigDecimal.ZERO:stCommonPolicy.getGrjcblsx());
//				}
//				if("GRJCBLXX".equals(stCommonPolicy.getId())){
//
//					commonPolicy.setGrjcblxx(stCommonPolicy.getGrjcblxx()==null?BigDecimal.ZERO:stCommonPolicy.getGrjcblxx());
//				}
//				if("DWJCBLSX".equals(stCommonPolicy.getId())){
//
//					commonPolicy.setDwjcblsx(stCommonPolicy.getDwjcblsx()==null?BigDecimal.ZERO:stCommonPolicy.getDwjcblsx());
//				}
//				if("DWJCBLXX".equals(stCommonPolicy.getId())){
//
//					commonPolicy.setDwjcblxx(stCommonPolicy.getDwjcblxx()==null?BigDecimal.ZERO:stCommonPolicy.getDwjcblxx());
//				}
//			}
//		});

		CCollectionUnitBusinessDetailsExtension collectionUnitBusinessDetailsExtension = collectionUnitInformationBasicVice.getDwywmx().getExtension();

//		if(collectionUnitInformationBasicVice.getGrjcbl()==null){
//
//			throw new ErrorException(ReturnEnumeration.Data_MISS,"个人缴存比例");
//
//		}else {
//
//			if(collectionUnitInformationBasicVice.getGrjcbl().compareTo(commonPolicy.getGrjcblsx())>0||collectionUnitInformationBasicVice.getGrjcbl().compareTo(commonPolicy.getGrjcblxx())<0){
//
//				throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"个人缴存比例"+collectionUnitInformationBasicVice.getGrjcbl()+"不在"+commonPolicy.getGrjcblxx()+"~"+commonPolicy.getGrjcblsx()+"区间内");
//			}
//		}


		if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getFxzhkhyh())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"发薪账号开户银行");}

		if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getDwfxr())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"单位发薪日");}

		if(collectionUnitInformationBasicVice.getDwfxr().length()!=2||collectionUnitInformationBasicVice.getDwfxr().compareTo("01") < 0 || collectionUnitInformationBasicVice.getDwfxr().compareTo("31") > 0){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"单位发薪日");
		}
		//if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getBeiZhu())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"备注");}

		//if(!StringUtil.notEmpty(collectionUnitBusinessDetailsExtension.getBeizhu())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"备注");}

		if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getFxzhhm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"发薪账号户名");}

		//if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getDwschjny())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"单位首次汇缴年月");}

		if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getFxzh())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"发薪账号");}

		if(collectionUnitInformationBasicVice.getDwslrq()==null){ throw new ErrorException(ReturnEnumeration.Data_MISS,"单位设立日期");}

		if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getStyhdm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"受托银行代码");}

		if(collectionUnitInformationBasicVice.getStyhdm().length()!=3){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"受托银行代码");
		}
		if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getStyhmc())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"受托银行名称");}

		if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getYwwd())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"业务网点");}

//		if(!StringUtil.notEmpty(collectionUnitBusinessDetailsExtension.getYwwd())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"业务网点");}

		//if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getDwczhm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"传真号码");}

	//	if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getDwdzxx())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"单位电子信箱 ");}

		if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getDwyb())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"单位邮编");}

		if(collectionUnitInformationBasicVice.getDwyb().length()!=6){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"单位邮编");
		}
	//	if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getDwlxdh())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"单位联系电话");}

		//if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getPzjgmc())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"批准机关名称");}
		if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getDwfrdbzjlx())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"单位法人代表证件类型");}

		if(!CollectionUtils.flatmap(iDictionaryService.getDictionaryByType("PersonalCertificate"), new CollectionUtils.Transformer<CommonDictionary, String>() {
			@Override
			public String tansform(CommonDictionary var1) { return var1.getCode(); }

		}).contains(collectionUnitInformationBasicVice.getDwfrdbzjlx())){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"单位法人代表证件类型");
		}

		if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getDwfrdbzjhm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"单位法人代表证件号码");}

		if(PersonCertificateType.身份证.getCode().equals(collectionUnitInformationBasicVice.getDwfrdbzjlx())&&!IdcardValidator.isValidatedAllIdcard(collectionUnitInformationBasicVice.getDwfrdbzjhm())){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"单位法人代表证件号码");
		}
		if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getDwjjlx())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"单位经济类型");}

		if(collectionUnitInformationBasicVice.getDwjjlx().length()!=3){ throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"单位经济类型"); }

		if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getDwfrdbxm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"单位法人代表姓名");}
		if(collectionUnitInformationBasicVice.getDwfrdbxm().length()<2){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"单位法人代表姓名");
		}
		//if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getXzqy())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"单位行政区域");}

		//if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getDjsyyz())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"登记使用印章");}

		if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getDwlb())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"单位类别");}



		//if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getPzjgjb())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"批准机关级别");}

		//if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getKgqk())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"控股情况");}

		//if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getDjzch())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"登记注册号");}

		String zjgdm = collectionUnitInformationBasicVice.getZzjgdm();

		if(!StringUtil.notEmpty(zjgdm)){ throw new ErrorException(ReturnEnumeration.Data_MISS,"组织机构代码");}

		if((StringUtil.notEmpty(collectionUnitInformationBasicVice.getZzjgdm()))&&(zjgdm.length()>20||zjgdm.length()<6)){
			throw new ErrorException(ReturnEnumeration.ZZJGDMData_LENGTH);
		}

		if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getDwlsgx())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"单位隶属关系");}

		if(!CollectionUtils.flatmap(iDictionaryService.getDictionaryByType("UnitRelationship"), new CollectionUtils.Transformer<CommonDictionary, String>() {
			@Override
			public String tansform(CommonDictionary var1) { return var1.getCode(); }

		}).contains(collectionUnitInformationBasicVice.getDwlsgx())){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"单位隶属关系");
		}
		if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getDwdz())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"单位地址");}

		if(collectionUnitInformationBasicVice.getDwdz().length()<10){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"单位地址");
		}
		if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getDwmc())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"单位名称");}

		if(collectionUnitInformationBasicVice.getDwmc().contains(" ")){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"单位名称不能包含空格");
		}
		if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getDwsshy())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"单位所属行业");}

		if(collectionUnitInformationBasicVice.getDwsshy().length()<1||collectionUnitInformationBasicVice.getDwsshy().length()>4){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"单位所属行业");
		}
		if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getDwzh())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"单位账号");}

		if(!this.iUploadImagesService.validateUploadFile(UploadFileModleType.归集.getCode(), UploadFileBusinessType.单位信息变更.getCode(),collectionUnitInformationBasicVice.getBlzl())){
			throw new ErrorException(ReturnEnumeration.Data_MISS,"办理资料");
		}
		//if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getBlzl())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"办理资料");}

		//if(!StringUtil.notEmpty(collectionUnitBusinessDetailsExtension.getBlzl())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"办理资料");}

		if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getJbrzjlx())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"经办人证件类型");}

		if(!CollectionUtils.flatmap(iDictionaryService.getDictionaryByType("PersonalCertificate"), new CollectionUtils.Transformer<CommonDictionary, String>() {
			@Override
			public String tansform(CommonDictionary var1) { return var1.getCode(); }

		}).contains(collectionUnitInformationBasicVice.getJbrzjlx())){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"经办人证件类型");
		}
		//if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getJbrgddhhm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"经办人固定电话号码");}

		if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getJbrxm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"经办人姓名");}

		if(collectionUnitInformationBasicVice.getJbrxm().length()<2){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"经办人姓名");
		}
		if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getJbrzjhm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"经办人证件号码");}

		if(PersonCertificateType.身份证.getCode().equals(collectionUnitInformationBasicVice.getJbrzjlx())&&!IdcardValidator.isValidatedAllIdcard(collectionUnitInformationBasicVice.getJbrzjhm())){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"经办人证件号码");
		}
		if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getJbrsjhm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"经办人手机号码");}

		if(collectionUnitInformationBasicVice.getJbrsjhm().length()!=11){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"经办人手机号码");
		}
		if(!StringUtil.notEmpty(collectionUnitBusinessDetailsExtension.getJbrzjlx())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"经办人证件类型 ");}

		if(!StringUtil.notEmpty(collectionUnitBusinessDetailsExtension.getJbrxm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"经办人姓名");}

		if(!StringUtil.notEmpty(collectionUnitBusinessDetailsExtension.getJbrzjhm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"经办人证件号码");}

	}
}
