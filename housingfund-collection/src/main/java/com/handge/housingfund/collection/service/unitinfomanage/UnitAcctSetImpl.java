package com.handge.housingfund.collection.service.unitinfomanage;


import com.handge.housingfund.collection.utils.AssertUtils;
import com.handge.housingfund.collection.utils.ComUtils;
import com.handge.housingfund.collection.utils.StateMachineUtils;
import com.handge.housingfund.common.service.ISaveAuditHistory;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.AccountRpcService;
import com.handge.housingfund.common.service.account.ISettlementSpecialBankAccountManageService;
import com.handge.housingfund.common.service.account.model.*;
import com.handge.housingfund.common.service.collection.enumeration.*;
import com.handge.housingfund.common.service.collection.model.CommonMessage;
import com.handge.housingfund.common.service.collection.model.unit.*;
import com.handge.housingfund.common.service.collection.service.unitinfomanage.UnitAcctSet;
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
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.utils.CriteriaUtils;
import com.handge.housingfund.database.utils.DAOBuilder;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;

;

/*
 * Created by lian on 2017/7/18.
 */
@SuppressWarnings({"Duplicates", "Convert2Lambda", "Anonymous2MethodRef", "SpringAutowiredFieldsWarningInspection", "SpringJavaAutowiringInspection", "serial", "ConstantConditions"})
@Component
public class UnitAcctSetImpl implements UnitAcctSet {

	@Autowired
	private ICCollectionUnitInformationBasicViceDAO collectionUnitInformationBasicViceDAO;
	@Autowired
	private IStCollectionUnitBusinessDetailsDAO collectionUnitBusinessDetailsDAO;

	@Autowired
	private com.handge.housingfund.statemachineV2.IStateMachineService stateMachineService;

	@Autowired
	private IStCommonUnitDAO commonUnitDAO;

	@Autowired
	private AccountRpcService accountRpcService;
	@Autowired
	private IPdfService pdfService;
	@Autowired
	private ICAuditHistoryDAO icAuditHistoryDAO;

	@Autowired
	private ISaveAuditHistory iSaveAuditHistory;

	@Autowired
	private IStCommonPolicyDAO commonPolicyDAO;
	@Autowired
	private IDictionaryService iDictionaryService;

	@Autowired
	private IUploadImagesService iUploadImagesService;

	@Autowired
	private ICAccountNetworkDAO cAccountNetworkDAO;

	@Autowired

	private ISettlementSpecialBankAccountManageService settlementSpecialBankAccountManageService;

	private static String format = "yyyy-MM-dd";

	private static String formatNY = "yyyy-MM";

	private static String formatNYRSF = "yyyy-MM-dd HH:mm";

	private static String formatR = "dd";


	@Override
	public AddUnitAcctSetRes addUnitAccountSet(TokenContext tokenContext, UnitAcctSetPost addUnitAcctPost) {

		//region //参数检查
		boolean allowNull = addUnitAcctPost.getCZLX().equals("0");

		addCheck(addUnitAcctPost);	//名称重复验证

		if(!DateUtil.isFollowFormat(addUnitAcctPost.getDWDJXX().getDWSCHJNY()/*单位首次汇缴年月*/,formatNY,allowNull)){throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"单位首次汇缴年月");}

		if(!DateUtil.isFollowFormat(addUnitAcctPost.getDWDJXX().getDWSLRQ()/*单位设立日期*/,format,allowNull)){throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"单位设立日期");}

		if(!StringUtil.isDigits(addUnitAcctPost.getDWDJXX().getGRJCBL()+""/*个人缴存比例*/,allowNull)){throw  new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"个人缴存比例");}

		if(!StringUtil.isDigits(addUnitAcctPost.getDWDJXX().getDWJCBL()+""/*单位缴存比例*/,allowNull)){throw  new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"单位缴存比例");}

		if(!StringUtil.isDigits(addUnitAcctPost.getDWDJXX().getDWFXR()/*单位发薪日*/,allowNull)){throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"单位发薪日");}

		//endregion

		//region //必要字段申明&关系配置
		CCollectionUnitInformationBasicVice collectionUnitInformationBasicVice = new CCollectionUnitInformationBasicVice();

		CCollectionUnitBusinessDetailsExtension collectionUnitBusinessDetailsExtension = new CCollectionUnitBusinessDetailsExtension();

		StCollectionUnitBusinessDetails collectionUnitBusinessDetails = new StCollectionUnitBusinessDetails();

		collectionUnitBusinessDetails.setExtension(collectionUnitBusinessDetailsExtension);

		collectionUnitInformationBasicVice.setDwywmx(collectionUnitBusinessDetails);

		//endregion
		
		//region //字段填充
		
		//COMMON

		collectionUnitBusinessDetailsExtension.setCzmc(CollectionBusinessType.开户.getCode());
		collectionUnitBusinessDetailsExtension.setFchxhyy(collectionUnitBusinessDetailsExtension.getFchxhyy()/*操作原因 不填*/);
		collectionUnitBusinessDetailsExtension.setJzpzh("0"/*记账凭证号 不填*/);
		collectionUnitBusinessDetailsExtension.setSlsj(new Date()/*todo 受理时间*/);
		collectionUnitBusinessDetailsExtension.setShbtgyy(collectionUnitBusinessDetailsExtension.getShbtgyy()/*审核不通过原因 不填*/);

		collectionUnitBusinessDetails.setCzbz(CommonFieldType.非冲账.getCode());
		collectionUnitBusinessDetails.setFse(collectionUnitBusinessDetails.getFse()/*发生额 开户不填*/);
		collectionUnitBusinessDetails.setFslxe(collectionUnitBusinessDetails.getFslxe()/*发生利息额 开户不填*/);
		collectionUnitBusinessDetails.setFsrs(collectionUnitBusinessDetails.getFsrs()/*发生人数 开户不填*/);
		collectionUnitBusinessDetails.setHbjny(collectionUnitBusinessDetails.getHbjny()/*汇补缴年月 开户不填*/);
		collectionUnitBusinessDetails.setJzrq(collectionUnitBusinessDetails.getJzrq()/*记账日期 开户不填*/);
		collectionUnitBusinessDetails.setYwmxlx(CollectionBusinessType.开户.getCode());
		collectionUnitBusinessDetails.setUnit(collectionUnitBusinessDetails.getUnit()/*单位 开户不填*/);

		collectionUnitInformationBasicVice.setDdsj(collectionUnitInformationBasicVice.getDdsj()/*开户不填*/);
		collectionUnitInformationBasicVice.setCzmc(CollectionBusinessType.开户.getCode());
		collectionUnitInformationBasicVice.setDwschjny(collectionUnitInformationBasicVice.getDwschjny()/*开户不填*/);
		collectionUnitInformationBasicVice.setYwmxlx(CollectionBusinessType.开户.getCode());
		collectionUnitInformationBasicVice.setSlsj(new Date()/*todo 受理时间*/);

		//CZY
		collectionUnitInformationBasicVice.setCzy(tokenContext.getUserInfo().getCZY()/*操作员*/);
		collectionUnitBusinessDetailsExtension.setCzy(tokenContext.getUserInfo().getCZY()/*操作员*/);



		// DWDJXX
		collectionUnitInformationBasicVice.setGrjcbl(StringUtil.safeRatioBigDecimal(addUnitAcctPost.getDWDJXX().getGRJCBL()/*个人缴存比例*/));
		collectionUnitInformationBasicVice.setFxzhkhyh(addUnitAcctPost.getDWDJXX().getFXZHKHYH()/*发薪账号开户银行*/);
		collectionUnitInformationBasicVice.setDwfxr(addUnitAcctPost.getDWDJXX().getDWFXR()/*单位发薪日*/);
		collectionUnitInformationBasicVice.setBeiZhu(addUnitAcctPost.getDWDJXX().getBeiZhu()/*备注*/);
		collectionUnitBusinessDetailsExtension.setBeizhu(addUnitAcctPost.getDWDJXX().getBeiZhu()/*备注*/);
		collectionUnitInformationBasicVice.setDwjcbl(StringUtil.safeRatioBigDecimal(addUnitAcctPost.getDWDJXX().getDWJCBL()/*单位缴存比例*/));
		collectionUnitInformationBasicVice.setFxzhhm(addUnitAcctPost.getDWDJXX().getFXZHHM()/*发薪账号户名*/);
		collectionUnitInformationBasicVice.setDwschjny(DateUtil.safeStr2DBDate(formatNY,addUnitAcctPost.getDWDJXX().getDWSCHJNY()/*单位首次汇缴年月*/,DateUtil.dbformatYear_Month));
		collectionUnitInformationBasicVice.setFxzh(addUnitAcctPost.getDWDJXX().getFXZH()/*发薪账号*/);
		collectionUnitInformationBasicVice.setDwslrq(DateUtil.safeStr2Date(format,addUnitAcctPost.getDWDJXX().getDWSLRQ()/*单位设立日期*/));

		// WTYHXX
		collectionUnitInformationBasicVice.setStyhdm(addUnitAcctPost.getWTYHXX().getSTYHDM()/*受托银行代码*/);
		collectionUnitInformationBasicVice.setStyhmc(addUnitAcctPost.getWTYHXX().getSTYHMC()/*受托银行名称*/);

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
		collectionUnitInformationBasicVice.setDwczhm(addUnitAcctPost.getDWLXFS().getCZHM()/*传真号码*/);
		collectionUnitInformationBasicVice.setDwdzxx(addUnitAcctPost.getDWLXFS().getDWDZXX()/*单位电子信箱 */);
		collectionUnitInformationBasicVice.setDwyb(addUnitAcctPost.getDWLXFS().getDWYB()/*单位邮编*/);
		collectionUnitInformationBasicVice.setDwlxdh(addUnitAcctPost.getDWLXFS().getDWLXDH()/*单位联系电话*/);

		// DWGJXX
		collectionUnitInformationBasicVice.setPzjgmc(addUnitAcctPost.getDWGJXX().getPZJGMC()/*批准机关名称*/);
		collectionUnitInformationBasicVice.setDwfrdbzjhm(addUnitAcctPost.getDWGJXX().getDWFRDBZJHM()/*单位法人代表证件号码*/);
		collectionUnitInformationBasicVice.setDwjjlx(addUnitAcctPost.getDWGJXX().getDWJJLX()/*单位经济类型*/);
		collectionUnitInformationBasicVice.setDwfrdbxm(addUnitAcctPost.getDWGJXX().getDWFRDBXM()/*单位法人代表姓名*/);
		collectionUnitInformationBasicVice.setXzqy(addUnitAcctPost.getDWGJXX().getDWXZQY()/*单位行政区域*/);
		collectionUnitInformationBasicVice.setDjsyyz(addUnitAcctPost.getDWGJXX().getDJSYYZ()/*登记使用印章*/);
		collectionUnitInformationBasicVice.setDwlb(addUnitAcctPost.getDWGJXX().getDWLB()/*单位类别*/);
		collectionUnitInformationBasicVice.setDwfrdbzjlx(addUnitAcctPost.getDWGJXX().getDWFRDBZJLX()/*单位法人代表证件类型*/);
		collectionUnitInformationBasicVice.setPzjgjb(addUnitAcctPost.getDWGJXX().getPZJGJB()/*批准机关级别*/);
		collectionUnitInformationBasicVice.setKgqk(addUnitAcctPost.getDWGJXX().getKGQK()/*控股情况*/);
		collectionUnitInformationBasicVice.setDjzch(addUnitAcctPost.getDWGJXX().getDJZCH()/*登记注册号*/);
		collectionUnitInformationBasicVice.setZzjgdm(addUnitAcctPost.getDWGJXX().getZZJGDM()/*组织机构代码*/);
		collectionUnitInformationBasicVice.setDwlsgx(addUnitAcctPost.getDWGJXX().getDWLSGX()/*单位隶属关系*/);
		collectionUnitInformationBasicVice.setDwdz(addUnitAcctPost.getDWGJXX().getDWDZ()/*单位地址*/);
		collectionUnitInformationBasicVice.setDwmc(addUnitAcctPost.getDWGJXX().getDWMC()/*单位名称*/);
		collectionUnitInformationBasicVice.setDwsshy(addUnitAcctPost.getDWGJXX().getDWSSHY()/*单位所属行业*/);

		// DWKHBLZL
		if(!"1".equals(addUnitAcctPost.getFJCBLZL())) {
			collectionUnitInformationBasicVice.setBlzl(addUnitAcctPost.getDWKHBLZL().getBLZL()/*办理资料*/);
			collectionUnitBusinessDetailsExtension.setBlzl(addUnitAcctPost.getDWKHBLZL().getBLZL()/*办理资料*/);
		}
		// JBRXX
		collectionUnitInformationBasicVice.setJbrzjlx(addUnitAcctPost.getJBRXX().getJBRZJLX()/*经办人证件类型 */);
		collectionUnitInformationBasicVice.setJbrgddhhm(addUnitAcctPost.getJBRXX().getJBRGDDHHM()/*经办人固定电话号码*/);
		collectionUnitInformationBasicVice.setJbrxm(addUnitAcctPost.getJBRXX().getJBRXM()/*经办人姓名*/);
		collectionUnitInformationBasicVice.setJbrzjhm(addUnitAcctPost.getJBRXX().getJBRZJHM()/*经办人证件号码*/);
		collectionUnitInformationBasicVice.setJbrsjhm(addUnitAcctPost.getJBRXX().getJBRSJHM()/*经办人手机号码*/);

		collectionUnitBusinessDetailsExtension.setJbrzjlx(addUnitAcctPost.getJBRXX().getJBRZJLX()/*经办人证件类型 */);
		collectionUnitBusinessDetailsExtension.setJbrxm(addUnitAcctPost.getJBRXX().getJBRXM()/*经办人姓名*/);
		collectionUnitBusinessDetailsExtension.setJbrzjhm(addUnitAcctPost.getJBRXX().getJBRZJHM()/*经办人证件号码*/);

        //endregion

		//region//唯一性验证
		StCommonUnit commonUnit = DAOBuilder.instance(this.commonUnitDAO).extension(new IBaseDAO.CriteriaExtension() {
			@Override
			public void extend(Criteria criteria) {

				criteria.add(Restrictions.or(
						Restrictions.eq("dwdz",collectionUnitInformationBasicVice.getDwdz()),
						Restrictions.eq("dwmc",collectionUnitInformationBasicVice.getDwmc())
				));
			}
		}).getObject(new DAOBuilder.ErrorHandler() {

			@Override

			public void error(Exception e) { throw new ErrorException(e);}
		});

		if(addUnitAcctPost.getCZLX().equals("1")&&commonUnit!=null){

			throw new ErrorException(ReturnEnumeration.Data_Already_Eeist,"单位名称或单位地址");
		}
		//endregion

		//region //修改状态
		CCollectionUnitInformationBasicVice savedVice = DAOBuilder.instance(this.collectionUnitInformationBasicViceDAO).entity(collectionUnitInformationBasicVice).saveThenFetchObject(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e); }

		});

		if (savedVice == null||savedVice.getDwywmx()==null) {

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"业务记录");
		}

		StateMachineUtils.updateState(this.stateMachineService, new HashMap<String, String>(){{

			this.put("0", Events.通过.getEvent());

			this.put("1",Events.提交.getEvent());

		}}.get(addUnitAcctPost.getCZLX()),new TaskEntity() {{

			this.setStatus(savedVice.getDwywmx().getExtension().getStep()==null ? "初始状态":savedVice.getDwywmx().getExtension().getStep());
			this.setTaskId(savedVice.getDwywmx().getYwlsh());
			this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
			this.setNote("");
			this.setSubtype(BusinessSubType.归集_单位开户.getSubType());
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

					doUnitAcctSet(tokenContext,savedVice.getDwywmx().getYwlsh());
				}
			}
		});

		//endregion

		//region //在途验证

		if((addUnitAcctPost.getCZLX().equals("1")&&!DAOBuilder.instance(this.collectionUnitInformationBasicViceDAO).extension(new IBaseDAO.CriteriaExtension() {
			@Override
			public void extend(Criteria criteria) {

				criteria.add(Restrictions.not(Restrictions.in(CriteriaUtils.addAlias(criteria,"dwywmx.cCollectionUnitBusinessDetailsExtension.step"), (Collection)CollectionUtils.flatmap(CollectionUtils.merge(CollectionBusinessStatus.办结.getSubTypes(), CollectionBusinessStatus.新建.getSubTypes()), new CollectionUtils.Transformer<CollectionBusinessStatus, String>() {

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

		if(addUnitAcctPost.getCZLX().equals("1")){

			this.iscollectionUnitInformationBasicViceAvailable(collectionUnitInformationBasicVice);


		}

		this.iSaveAuditHistory.saveNormalBusiness(savedVice.getDwywmx().getYwlsh(),tokenContext, CollectionBusinessType.开户.getName(),"新建");
		//endregion

		return new AddUnitAcctSetRes() {{

			this.setYWLSH(savedVice.getDwywmx().getYwlsh());

			this.setStatus("success");

		}};

	}

	private void addCheck(UnitAcctSetPost addUnitAcctPost) {
		CommonMessage unitNameCheckMessage = getUnitNameCheckMessage(addUnitAcctPost.getDWGJXX().getDWMC());
		String code = unitNameCheckMessage.getCode();
		if("02".equals(code)){
			throw new ErrorException(unitNameCheckMessage.getMessage());
		}
	}

	@Override
	public GetUnitAcctSetRes getUnitAccountSet(TokenContext tokenContext,String YWLSH) {

		//region // 检查参数

		if (YWLSH == null) { throw new ErrorException(ReturnEnumeration.Parameter_MISS,"业务流水号"); }

		//endregion

		//region //必要字段查询&完整性验证
		CCollectionUnitInformationBasicVice collectionUnitInformationBasicVice = DAOBuilder.instance(this.collectionUnitInformationBasicViceDAO).searchFilter(new HashMap<String, Object>() {{

			this.put("dwywmx.ywlsh", YWLSH);

			this.put("czmc",CollectionBusinessType.开户.getCode());

		}}).getObject(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e); }

		});

		if (collectionUnitInformationBasicVice == null||collectionUnitInformationBasicVice.getDwywmx()==null) {

			throw new ErrorException(ReturnEnumeration.Data_MISS,"业务记录");
		}

		List<CAuditHistory> list_auditHistory = DAOBuilder.instance(icAuditHistoryDAO).searchFilter(new HashMap<String, Object>() {{
			this.put("ywlsh", YWLSH);
		}}).getList(new DAOBuilder.ErrorHandler() {
			@Override
			public void error(Exception e) { throw new ErrorException(e); }
		});
		//endregion

		return  new GetUnitAcctSetRes() {{

			this.setDWKHBLZL(new GetUnitAcctSetResDWKHBLZL() {{

				this.setBLZL(collectionUnitInformationBasicVice.getBlzl());

			}});

			this.setDWDJXX(new GetUnitAcctSetResDWDJXX() {{

				this.setGRJCBL((collectionUnitInformationBasicVice.getGrjcbl() == null ? "0": collectionUnitInformationBasicVice.getGrjcbl().multiply(new BigDecimal("100")).doubleValue()) + "");
				this.setFXZHKHYH(collectionUnitInformationBasicVice.getFxzhkhyh());
				this.setBeiZhu(collectionUnitInformationBasicVice.getBeiZhu());
				this.setDWFXR(collectionUnitInformationBasicVice.getDwfxr());
				this.setDWJCBL((collectionUnitInformationBasicVice.getDwjcbl() == null ? "0": collectionUnitInformationBasicVice.getDwjcbl().multiply(new BigDecimal("100")).doubleValue())+ "");
				this.setFXZHHM(collectionUnitInformationBasicVice.getFxzhhm());
				this.setDWSCHJNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month,collectionUnitInformationBasicVice.getDwschjny(), formatNY));
				this.setFXZH(collectionUnitInformationBasicVice.getFxzh());
				this.setDWSLRQ(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month,collectionUnitInformationBasicVice.getDwslrq(), format));

				if(collectionUnitInformationBasicVice.getDwywmx().getUnit()!=null) {
					Date bjsj = collectionUnitInformationBasicVice.getDwywmx().getUnit().getCreated_at();
					if (!ComUtils.isEmpty(bjsj)) {
						String dwkhrq = ComUtils.parseToString(bjsj, "yyyy-MM-dd");
						this.setDWKHRQ(dwkhrq);
					}
				}
			}});

			this.setDWLXFS(new GetUnitAcctSetResDWLXFS() {{

				this.setCZHM(collectionUnitInformationBasicVice.getDwczhm());
				this.setDWLXDH(collectionUnitInformationBasicVice.getDwlxdh());
				this.setDWDZXX(collectionUnitInformationBasicVice.getDwdzxx());
				this.setDWYB(collectionUnitInformationBasicVice.getDwyb());
			}});

			this.setDWGJXX(new GetUnitAcctSetResDWGJXX() {{

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

			}});

			this.setJBRXX(new GetUnitAcctSetResJBRXX() {{

				this.setJBRZJLX(collectionUnitInformationBasicVice.getJbrzjlx());
				this.setJBRGDDHHM(collectionUnitInformationBasicVice.getJbrgddhhm());
				this.setJBRXM(collectionUnitInformationBasicVice.getJbrxm());
				this.setJBRZJHM(collectionUnitInformationBasicVice.getJbrzjhm());
				this.setJBRSJHM(collectionUnitInformationBasicVice.getJbrsjhm());
				this.setCZY(collectionUnitInformationBasicVice.getDwywmx().getExtension().getCzy());
				this.setYWWD(collectionUnitInformationBasicVice.getDwywmx().getExtension().getYwwd().getMingCheng());
			}});

			this.setWTYHXX(new GetUnitAcctSetResWTYHXX() {{

				this.setSTYHDM(collectionUnitInformationBasicVice.getStyhdm());
				this.setSTYHMC(collectionUnitInformationBasicVice.getStyhmc());

			}});

//			this.setReviewInfos(new ArrayList<>(CollectionUtils.flatmap(list_auditHistory, new CollectionUtils.Transformer<CAuditHistory, ReviewInfo>() {
//				@Override
//				public ReviewInfo tansform(CAuditHistory cAuditHistory) {
//
//					return new ReviewInfo() {{
//
//						this.setYWLSH(YWLSH);
//						this.setSHJG(cAuditHistory.getShjg());
//						this.setYYYJ(cAuditHistory.getYyyj());
//						this.setCZY(cAuditHistory.getCzy());
//						this.setYWWD(cAuditHistory.getYwwd());
//						this.setZhiWu(cAuditHistory.getZhiwu());
//						this.setCZQD(cAuditHistory.getCzqd());
//						this.setBeiZhu(cAuditHistory.getBeiZhu());
//					}};
//				}
//			})));
		}};

	}

	@Override
	public AddUnitAcctSetRes reUnitAccountSet(TokenContext tokenContext,String YWLSH, UnitAcctSetPut reUnitAcctSetPut) {

		//region //参数检查

		boolean allowNull = reUnitAcctSetPut.getCZLX().equals("0");



		if(!DateUtil.isFollowFormat(reUnitAcctSetPut.getDWDJXX().getDWSCHJNY()/*单位首次汇缴年月*/,formatNY,allowNull)){throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"单位首次汇缴年月");}

		if(!DateUtil.isFollowFormat(reUnitAcctSetPut.getDWDJXX().getDWSLRQ()/*单位设立日期*/,format,allowNull)){throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"单位设立日期");}

		if(!StringUtil.isDigits(reUnitAcctSetPut.getDWDJXX().getGRJCBL()+""/*个人缴存比例*/,allowNull)){throw  new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"个人缴存比例");}

		if(!StringUtil.isDigits(reUnitAcctSetPut.getDWDJXX().getDWJCBL()+""/*单位缴存比例*/,allowNull)){throw  new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"单位缴存比例");}

		if(!StringUtil.isDigits(reUnitAcctSetPut.getDWDJXX().getDWFXR()/*单位发薪日*/,allowNull)){throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"单位发薪日");}
		//endregion

		//region //必要字段查询&完整性验证
		CCollectionUnitInformationBasicVice collectionUnitInformationBasicVice = DAOBuilder.instance(this.collectionUnitInformationBasicViceDAO).searchFilter(new HashMap<String, Object>() {{

			this.put("dwywmx.ywlsh",YWLSH);

			this.put("czmc",CollectionBusinessType.开户.getCode());

		}}).getObject(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e); }

		});

		if (collectionUnitInformationBasicVice == null||collectionUnitInformationBasicVice.getDwywmx() == null || collectionUnitInformationBasicVice.getDwywmx().getExtension() == null) {

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
		collectionUnitInformationBasicVice.setGrjcbl(StringUtil.safeRatioBigDecimal(reUnitAcctSetPut.getDWDJXX().getGRJCBL()/*个人缴存比例*/));
		collectionUnitInformationBasicVice.setFxzhkhyh(reUnitAcctSetPut.getDWDJXX().getFXZHKHYH()/*发薪账号开户银行*/);
		collectionUnitInformationBasicVice.setDwfxr(reUnitAcctSetPut.getDWDJXX().getDWFXR()/*单位发薪日*/);
		collectionUnitInformationBasicVice.setBeiZhu(reUnitAcctSetPut.getDWDJXX().getBeiZhu()/*备注*/);
		collectionUnitBusinessDetailsExtension.setBeizhu(reUnitAcctSetPut.getDWDJXX().getBeiZhu()/*备注*/);
		collectionUnitInformationBasicVice.setDwjcbl(StringUtil.safeRatioBigDecimal(reUnitAcctSetPut.getDWDJXX().getDWJCBL()/*单位缴存比例*/));
		collectionUnitInformationBasicVice.setFxzhhm(reUnitAcctSetPut.getDWDJXX().getFXZHHM()/*发薪账号户名*/);
		collectionUnitInformationBasicVice.setDwschjny(DateUtil.safeStr2DBDate(formatNY,reUnitAcctSetPut.getDWDJXX().getDWSCHJNY()/*单位首次汇缴年月*/,DateUtil.dbformatYear_Month));
		collectionUnitInformationBasicVice.setFxzh(reUnitAcctSetPut.getDWDJXX().getFXZH()/*发薪账号*/);
		collectionUnitInformationBasicVice.setDwslrq(DateUtil.safeStr2Date(format,reUnitAcctSetPut.getDWDJXX().getDWSLRQ()/*单位设立日期*/));

		// WTYHXX
		collectionUnitInformationBasicVice.setStyhdm(reUnitAcctSetPut.getWTYHXX().getSTYHDM()/*受托银行代码*/);
		collectionUnitInformationBasicVice.setStyhmc(reUnitAcctSetPut.getWTYHXX().getSTYHMC()/*受托银行名称*/);

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
		collectionUnitInformationBasicVice.setDwczhm(reUnitAcctSetPut.getDWLXFS().getCZHM()/*传真号码*/);
		collectionUnitInformationBasicVice.setDwdzxx(reUnitAcctSetPut.getDWLXFS().getDWDZXX()/*单位电子信箱 */);
		collectionUnitInformationBasicVice.setDwyb(reUnitAcctSetPut.getDWLXFS().getDWYB()/*单位邮编*/);
		collectionUnitInformationBasicVice.setDwlxdh(reUnitAcctSetPut.getDWLXFS().getDWLXDH()/*单位联系电话*/);

		// DWGJXX
		collectionUnitInformationBasicVice.setPzjgmc(reUnitAcctSetPut.getDWGJXX().getPZJGMC()/*批准机关名称*/);
		collectionUnitInformationBasicVice.setDwfrdbzjhm(reUnitAcctSetPut.getDWGJXX().getDWFRDBZJHM()/*单位法人代表证件号码*/);
		collectionUnitInformationBasicVice.setDwjjlx(reUnitAcctSetPut.getDWGJXX().getDWJJLX()/*单位经济类型*/);
		collectionUnitInformationBasicVice.setDwfrdbxm(reUnitAcctSetPut.getDWGJXX().getDWFRDBXM()/*单位法人代表姓名*/);
		collectionUnitInformationBasicVice.setXzqy(reUnitAcctSetPut.getDWGJXX().getDWXZQY()/*单位行政区域*/);
		collectionUnitInformationBasicVice.setDjsyyz(reUnitAcctSetPut.getDWGJXX().getDJSYYZ()/*登记使用印章*/);
		collectionUnitInformationBasicVice.setDwlb(reUnitAcctSetPut.getDWGJXX().getDWLB()/*单位类别*/);
		collectionUnitInformationBasicVice.setDwfrdbzjlx(reUnitAcctSetPut.getDWGJXX().getDWFRDBZJLX()/*单位法人代表证件类型*/);
		collectionUnitInformationBasicVice.setPzjgjb(reUnitAcctSetPut.getDWGJXX().getPZJGJB()/*批准机关级别*/);
		collectionUnitInformationBasicVice.setKgqk(reUnitAcctSetPut.getDWGJXX().getKGQK()/*控股情况*/);
		collectionUnitInformationBasicVice.setDjzch(reUnitAcctSetPut.getDWGJXX().getDJZCH()/*登记注册号*/);
		collectionUnitInformationBasicVice.setZzjgdm(reUnitAcctSetPut.getDWGJXX().getZZJGDM()/*组织机构代码*/);
		collectionUnitInformationBasicVice.setDwlsgx(reUnitAcctSetPut.getDWGJXX().getDWLSGX()/*单位隶属关系*/);
		collectionUnitInformationBasicVice.setDwdz(reUnitAcctSetPut.getDWGJXX().getDWDZ()/*单位地址*/);
		collectionUnitInformationBasicVice.setDwmc(reUnitAcctSetPut.getDWGJXX().getDWMC()/*单位名称*/);
		collectionUnitInformationBasicVice.setDwsshy(reUnitAcctSetPut.getDWGJXX().getDWSSHY()/*单位所属行业*/);

		// DWKHBLZL
		collectionUnitInformationBasicVice.setBlzl(reUnitAcctSetPut.getDWKHBLZL().getBLZL()/*办理资料*/);
		collectionUnitBusinessDetailsExtension.setBlzl(reUnitAcctSetPut.getDWKHBLZL().getBLZL()/*办理资料*/);

		// JBRXX
		collectionUnitInformationBasicVice.setJbrzjlx(reUnitAcctSetPut.getJBRXX().getJBRZJLX()/*经办人证件类型 */);
		collectionUnitInformationBasicVice.setJbrgddhhm(reUnitAcctSetPut.getJBRXX().getJBRGDDHHM()/*经办人固定电话号码*/);
		collectionUnitInformationBasicVice.setJbrxm(reUnitAcctSetPut.getJBRXX().getJBRXM()/*经办人姓名*/);
		collectionUnitInformationBasicVice.setJbrzjhm(reUnitAcctSetPut.getJBRXX().getJBRZJHM()/*经办人证件号码*/);
		collectionUnitInformationBasicVice.setJbrsjhm(reUnitAcctSetPut.getJBRXX().getJBRSJHM()/*经办人手机号码*/);

		collectionUnitBusinessDetailsExtension.setJbrzjlx(reUnitAcctSetPut.getJBRXX().getJBRZJLX()/*经办人证件类型 */);
		collectionUnitBusinessDetailsExtension.setJbrxm(reUnitAcctSetPut.getJBRXX().getJBRXM()/*经办人姓名*/);
		collectionUnitBusinessDetailsExtension.setJbrzjhm(reUnitAcctSetPut.getJBRXX().getJBRZJHM()/*经办人证件号码*/);

		//endregion

		//region//组织机构代码唯一性验证
		StCommonUnit commonUnit = DAOBuilder.instance(this.commonUnitDAO).extension(new IBaseDAO.CriteriaExtension() {
			@Override
			public void extend(Criteria criteria) {

				criteria.add(Restrictions.or(
						Restrictions.eq("dwdz",collectionUnitInformationBasicVice.getDwdz()),
						Restrictions.eq("dwmc",collectionUnitInformationBasicVice.getDwmc())

				));
			}
		}).getObject(new DAOBuilder.ErrorHandler() {

			@Override

			public void error(Exception e) { throw new ErrorException(e);}
		});

		if(reUnitAcctSetPut.getCZLX().equals("1")&&commonUnit!=null){

			throw new ErrorException(ReturnEnumeration.Data_Already_Eeist,"单位名称或单位地址");
		}
		//endregion

		//region //修改状态
		StateMachineUtils.updateState(this.stateMachineService, new HashMap<String, String>(){{

			this.put("0", Events.保存.getEvent());

			this.put("1",Events.通过.getEvent());

		}}.get(reUnitAcctSetPut.getCZLX()),new TaskEntity() {{

			this.setStatus(collectionUnitBusinessDetailsExtension.getStep()==null ? "初始状态":collectionUnitBusinessDetailsExtension.getStep());
			this.setTaskId(YWLSH);
			this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
			this.setNote("");
			this.setSubtype(BusinessSubType.归集_单位开户.getSubType());
			this.setType(BusinessType.Collection);
			this.setOperator(collectionUnitInformationBasicVice.getCzy());
			this.setWorkstation(collectionUnitInformationBasicVice.getYwwd());

		}}, new StateMachineUtils.StateChangeHandler() {

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

					doUnitAcctSet(tokenContext,YWLSH);
				}
			}
		});

		//endregion

		//region //在途验证

		if((reUnitAcctSetPut.getCZLX().equals("1")&&!DAOBuilder.instance(this.collectionUnitInformationBasicViceDAO).extension(new IBaseDAO.CriteriaExtension() {
			@Override
			public void extend(Criteria criteria) {

				criteria.add(Restrictions.not(Restrictions.in(CriteriaUtils.addAlias(criteria,"dwywmx.cCollectionUnitBusinessDetailsExtension.step"),(Collection)CollectionUtils.flatmap(CollectionUtils.merge(CollectionBusinessStatus.办结.getSubTypes(), CollectionBusinessStatus.新建.getSubTypes()), new CollectionUtils.Transformer<CollectionBusinessStatus, String>() {

					@Override
					public String tansform(CollectionBusinessStatus var1) { return var1.getName(); }

				}))));
				criteria.add(Restrictions.or(
						Restrictions.eq("dwdz",collectionUnitInformationBasicVice.getDwdz()),
						Restrictions.eq("dwmc",collectionUnitInformationBasicVice.getDwmc())
				));
				criteria.add(Restrictions.ne("dwywmx.ywlsh",YWLSH));
				criteria.add(Restrictions.in("cCollectionUnitBusinessDetailsExtension.czmc",CollectionBusinessType.开户.getCode(),CollectionBusinessType.变更.getCode()));
			}
		}).isUnique(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e);}

		}))){

			throw new ErrorException(ReturnEnumeration.Business_In_Process,"已有单位正在使用 相同的地址 或 名称 办理开户或变更业务");
		}

		if(reUnitAcctSetPut.getCZLX().equals("1")){

			this.iscollectionUnitInformationBasicViceAvailable(collectionUnitInformationBasicVice);

			this.iSaveAuditHistory.saveNormalBusiness(YWLSH,tokenContext, CollectionBusinessType.开户.getName(),"修改");
		}
		//endregion

		return  new AddUnitAcctSetRes() {{

			this.setYWLSH(YWLSH);

			this.setStatus("success");

		}};

	}

	@Override
	public PostUnitAcctSetSubmitRes submitUnitAccountSet(TokenContext tokenContext,List<String> YWLSHs) {

		//region // 检查参数

		if (YWLSHs == null) { throw new ErrorException(ReturnEnumeration.Parameter_MISS,"业务流水号"); }

		//endregion

		ArrayList<Exception> exceptions = new ArrayList<>();

		for(String YWLSH : YWLSHs) {

			//region //必要字段查询&完整性验证
			CCollectionUnitInformationBasicVice collectionUnitInformationBasicVice = DAOBuilder.instance(this.collectionUnitInformationBasicViceDAO).searchFilter(new HashMap<String, Object>() {{

				this.put("dwywmx.ywlsh", YWLSH);

				this.put("czmc", CollectionBusinessType.开户.getCode());

			}}).getObject(new DAOBuilder.ErrorHandler() {

				@Override
				public void error(Exception e) { exceptions.add(e); }

			});

			if (collectionUnitInformationBasicVice == null || collectionUnitInformationBasicVice.getDwywmx() == null || collectionUnitInformationBasicVice.getDwywmx().getExtension() == null) {

				return new PostUnitAcctSetSubmitRes() {{

					this.setSBYWLSH(YWLSH);

					this.setStatus("fail");

				}};
			}

			try {

				this.iscollectionUnitInformationBasicViceAvailable(collectionUnitInformationBasicVice);

				this.iSaveAuditHistory.saveNormalBusiness(YWLSH,tokenContext, CollectionBusinessType.开户.getName(),"修改");

			}catch (Exception e){

				return new PostUnitAcctSetSubmitRes() {{

					this.setSBYWLSH(YWLSH);

					this.setStatus("fail");

				}};
			}
			StCollectionUnitBusinessDetails collectionUnitBusinessDetails = collectionUnitInformationBasicVice.getDwywmx();

			CCollectionUnitBusinessDetailsExtension collectionUnitBusinessDetailsExtension = collectionUnitBusinessDetails.getExtension();

			//endregion

			//region//唯一性验证
			StCommonUnit commonUnit = DAOBuilder.instance(this.commonUnitDAO).extension(new IBaseDAO.CriteriaExtension() {
				@Override
				public void extend(Criteria criteria) {

					criteria.add(Restrictions.or(
							Restrictions.eq("dwdz",collectionUnitInformationBasicVice.getDwdz()),
							Restrictions.eq("dwmc",collectionUnitInformationBasicVice.getDwmc())

					));
				}
			}).getObject(new DAOBuilder.ErrorHandler() {

				@Override

				public void error(Exception e) { throw new ErrorException(e);}
			});

			if(commonUnit!=null){

				throw new ErrorException(ReturnEnumeration.Data_Already_Eeist,"单位名称或单位地址");
			}

			if(!tokenContext.getUserInfo().getCZY().equals(collectionUnitInformationBasicVice.getDwywmx().getExtension().getCzy())){

				throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务(" + YWLSH + ")不是由您受理的，不能提交");
			}
			//endregion

			//region //修改状态
			StateMachineUtils.updateState(this.stateMachineService, Events.通过.getEvent(), new TaskEntity() {{

				this.setStatus(collectionUnitBusinessDetailsExtension.getStep()==null ? "初始状态":collectionUnitBusinessDetailsExtension.getStep());
				this.setTaskId(YWLSH);
				this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
				this.setNote("");
				this.setSubtype(BusinessSubType.归集_单位开户.getSubType());
				this.setType(BusinessType.Collection);
				this.setOperator(collectionUnitInformationBasicVice.getCzy());
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
						public void error(Exception e) { throw new ErrorException(e); }

					});

					if(next.equals(CollectionBusinessStatus.办结.getName())){

						try {

							doUnitAcctSet(tokenContext,collectionUnitBusinessDetails.getYwlsh());

						}catch (Exception ex){

							exceptions.add(ex);
						}

					}
				}
			});

			if(exceptions.size()!=0){

				return new PostUnitAcctSetSubmitRes() {{

					this.setSBYWLSH(YWLSH);

					this.setStatus("fail");

				}};
			}
			
			//endregion

			//region //在途验证

			if(!DAOBuilder.instance(this.collectionUnitInformationBasicViceDAO).extension(new IBaseDAO.CriteriaExtension() {
				@Override
				public void extend(Criteria criteria) {

					criteria.add(Restrictions.not(Restrictions.in(CriteriaUtils.addAlias(criteria,"dwywmx.cCollectionUnitBusinessDetailsExtension.step"),(Collection)CollectionUtils.flatmap(CollectionUtils.merge(CollectionBusinessStatus.办结.getSubTypes(), CollectionBusinessStatus.新建.getSubTypes()), new CollectionUtils.Transformer<CollectionBusinessStatus, String>() {

						@Override
						public String tansform(CollectionBusinessStatus var1) { return var1.getName(); }

					}))));
					criteria.add(Restrictions.or(
							Restrictions.eq("dwdz",collectionUnitInformationBasicVice.getDwdz()),
							Restrictions.eq("dwmc",collectionUnitInformationBasicVice.getDwmc())
					));
					criteria.add(Restrictions.ne("dwywmx.ywlsh",YWLSH));
					criteria.add(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.czmc",CollectionBusinessType.开户.getCode()));
				}
			}).isUnique(new DAOBuilder.ErrorHandler() {

				@Override
				public void error(Exception e) { exceptions.add(e);}

			})){

				return new PostUnitAcctSetSubmitRes() {{

					this.setSBYWLSH(YWLSH);

					this.setStatus("fail");

				}};
			}
			//endregion
		}

		return  new PostUnitAcctSetSubmitRes() {{

			this.setSBYWLSH(this.getSBYWLSH());

			this.setStatus("success");

		}};

	}

	@Override
	public CommonMessage getUnitNameCheckMessage(String dwmc) {
		AssertUtils.notEmpty(dwmc,"单位名称不能为空");
		CommonMessage msg = null;
		//1、查询是否有已有相同的单位（正常使用）
		List<StCommonUnit> unitList = commonUnitDAO.getUnitByName(dwmc);
		if(unitList == null || unitList.size() == 0){
			msg = new CommonMessage("01","正常开户！");
		}else if(unitList.size() == 1){
			return new CommonMessage("02","系统已存在与该名称["+dwmc+"]相同的单位！");
		}else{
			return new CommonMessage("02","系统已存在多条与该名称["+dwmc+"]相同的单位！");
		}
		//2、查看是否有正在办理的单位，单位名称有冲突
		List<CCollectionUnitInformationBasicVice> unitSet = collectionUnitInformationBasicViceDAO.getUnitSetDoingByName(dwmc);
		if(unitSet != null && unitSet.size() > 0){
			msg = new CommonMessage("02","系统已存在与该名称["+dwmc+"]相同的单位，正在办理开户信息！");
		}
		return msg;
	}


	@Override
	public PageRes<ListUnitAcctSetResRes> showUnitAccountsSet(TokenContext tokenContext,String DWMC, String DWLB, String ZhuangTai,String page,String pagesize,String KSSJ,String JSSJ) {
		
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

		List<StCollectionUnitBusinessDetails> list_business = DAOBuilder.instance(this.collectionUnitBusinessDetailsDAO).extension(new IBaseDAO.CriteriaExtension() {

			@Override
			public void extend(Criteria criteria) {

				if (DateUtil.isFollowFormat(KSSJ,formatNYRSF,false)){ criteria.add(Restrictions.ge("created_at",DateUtil.safeStr2Date(formatNYRSF,KSSJ)));}
				if (DateUtil.isFollowFormat(JSSJ,formatNYRSF,false)){ criteria.add(Restrictions.lt("created_at",DateUtil.safeStr2Date(formatNYRSF,JSSJ)));}

				criteria.createAlias("unitInformationBasicVice","unitInformationBasicVice");

				if (StringUtil.notEmpty(DWMC)) { criteria.add(Restrictions.like("unitInformationBasicVice.dwmc", "%"+DWMC+"%")); }

				if (StringUtil.notEmpty(DWLB)) { criteria.add(Restrictions.eq("unitInformationBasicVice.dwlb", DWLB)); }

				criteria.createAlias("cCollectionUnitBusinessDetailsExtension","cCollectionUnitBusinessDetailsExtension");

				if (StringUtil.notEmpty(ZhuangTai) && !ZhuangTai.equals(CollectionBusinessStatus.所有.getName())&&!ZhuangTai.equals(CollectionBusinessStatus.待审核.getName())) { criteria.add(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.step", ZhuangTai)); }

				if(CollectionBusinessStatus.待审核.getName().equals(ZhuangTai)){
					criteria.add(Restrictions.like("cCollectionUnitBusinessDetailsExtension.step", LoanBussinessStatus.待某人审核.getName()));
				}
				criteria.add(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.czmc", CollectionBusinessType.开户.getCode()));
				criteria.createAlias("cCollectionUnitBusinessDetailsExtension.ywwd","ywwd");

				if(!"1".equals(tokenContext.getUserInfo().getYWWD())){
					criteria.add(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.ywwd.id",tokenContext.getUserInfo().getYWWD()));
				}
			}

		}).pageOption(pageRes,pagesize_number,page_number).orderOption("unitInformationBasicVice.slsj",Order.DESC).getList(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e); }

		});

		//endregion

		return new PageRes<ListUnitAcctSetResRes>() {{

			this.setResults(new ArrayList<ListUnitAcctSetResRes>() {{

				for (StCollectionUnitBusinessDetails collectionUnitBusinessDetails : list_business) {

					CCollectionUnitInformationBasicVice collectionIndividualAccountBasicVice = collectionUnitBusinessDetails.getUnitInformationBasicVice();

					if (collectionIndividualAccountBasicVice == null) { continue; }

					this.add(new ListUnitAcctSetResRes() {{

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
	public PageResNew<ListUnitAcctSetResRes> showUnitAccountsSet(TokenContext tokenContext, String DWMC, String DWLB, String ZhuangTai, String marker, String action, String pagesize, String KSSJ, String JSSJ) {
		//region //参数检查

		int pagesize_number = 0;

		try {


			if(pagesize!=null) { pagesize_number = Integer.parseInt(pagesize); }

		}catch (Exception e){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"页码");
		}

		//endregion

		//region //必要字段查询

		List<StCollectionUnitBusinessDetails> list_business = DAOBuilder.instance(this.collectionUnitBusinessDetailsDAO).extension(new IBaseDAO.CriteriaExtension() {

			@Override
			public void extend(Criteria criteria) {

				if (DateUtil.isFollowFormat(KSSJ,formatNYRSF,false)){ criteria.add(Restrictions.ge("created_at",DateUtil.safeStr2Date(formatNYRSF,KSSJ)));}
				if (DateUtil.isFollowFormat(JSSJ,formatNYRSF,false)){ criteria.add(Restrictions.lt("created_at",DateUtil.safeStr2Date(formatNYRSF,JSSJ)));}

				criteria.createAlias("unitInformationBasicVice","unitInformationBasicVice");

				if (StringUtil.notEmpty(DWMC)) { criteria.add(Restrictions.like("unitInformationBasicVice.dwmc", "%"+DWMC+"%")); }

				if (StringUtil.notEmpty(DWLB)) { criteria.add(Restrictions.eq("unitInformationBasicVice.dwlb", DWLB)); }

				criteria.createAlias("cCollectionUnitBusinessDetailsExtension","cCollectionUnitBusinessDetailsExtension");

				if (StringUtil.notEmpty(ZhuangTai) && !ZhuangTai.equals(CollectionBusinessStatus.所有.getName())&&!ZhuangTai.equals(CollectionBusinessStatus.待审核.getName())) { criteria.add(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.step", ZhuangTai)); }

				if(CollectionBusinessStatus.待审核.getName().equals(ZhuangTai)){
					criteria.add(Restrictions.like("cCollectionUnitBusinessDetailsExtension.step", LoanBussinessStatus.待某人审核.getName()));
				}
				criteria.add(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.czmc", CollectionBusinessType.开户.getCode()));
				criteria.createAlias("cCollectionUnitBusinessDetailsExtension.ywwd","ywwd");

				if(!"1".equals(tokenContext.getUserInfo().getYWWD())){
					criteria.add(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.ywwd.id",tokenContext.getUserInfo().getYWWD()));
				}
			}

		}).pageOption(marker,action,pagesize_number).orderOption("unitInformationBasicVice.slsj",Order.DESC).getList(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e); }

		});

		//endregion

		return new PageResNew<ListUnitAcctSetResRes>() {{

			this.setResults(action,new ArrayList<ListUnitAcctSetResRes>() {{

				for (StCollectionUnitBusinessDetails collectionUnitBusinessDetails : list_business) {

					CCollectionUnitInformationBasicVice collectionIndividualAccountBasicVice = collectionUnitBusinessDetails.getUnitInformationBasicVice();

					if (collectionIndividualAccountBasicVice == null) { continue; }

					this.add(new ListUnitAcctSetResRes() {{

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
	public CommonResponses headUnitAcctsSetReceipt(TokenContext tokenContext, String YWLSH) {

		//region // 检查参数

		if (YWLSH == null) { throw new ErrorException(ReturnEnumeration.Parameter_MISS,"业务流水号"); }

		//endregion

		//region  // 必要字段查询
		CCollectionUnitInformationBasicVice collectionUnitInformationBasicVice = DAOBuilder.instance(this.collectionUnitInformationBasicViceDAO).searchFilter(new HashMap<String, Object>() {{

			this.put("dwywmx.ywlsh", YWLSH);

		}}).getObject(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e); }

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
		DWKHBLZL.setBLZL(collectionUnitInformationBasicVice.getBlzl());
		result.setDWKHBLZL(DWKHBLZL);
		DWDJXX.setGRJCBL((collectionUnitInformationBasicVice.getGrjcbl() == null ? "0" : collectionUnitInformationBasicVice.getGrjcbl().multiply(new BigDecimal("100")).setScale(2)) + "");
		DWDJXX.setFXZHKHYH(collectionUnitInformationBasicVice.getFxzhkhyh());
		DWDJXX.setBeiZhu(collectionUnitInformationBasicVice.getBeiZhu());
		DWDJXX.setDWFXR(collectionUnitInformationBasicVice.getDwfxr());
		DWDJXX.setDWJCBL((collectionUnitInformationBasicVice.getDwjcbl() == null ? "0" : collectionUnitInformationBasicVice.getDwjcbl().multiply(new BigDecimal("100")).setScale(2)) + "");
		DWDJXX.setDWSCHJNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month,collectionUnitInformationBasicVice.getDwschjny(), formatNY));
		DWDJXX.setFXZH(collectionUnitInformationBasicVice.getFxzh());
		DWDJXX.setDWSLRQ(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month_Day,collectionUnitInformationBasicVice.getDwslrq(), format));
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
		DWGJXX.setCZY(collectionUnitInformationBasicVice.getDwywmx().getExtension().getCzy());
		DWGJXX.setYWWD(collectionUnitBusinessDetailsDAO.getByYwlsh(YWLSH).getExtension().getYwwd().getMingCheng());
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
		String id = pdfService.getUnitAcctsSetReceiptPdf(result,CollectionBusinessType.开户.getCode());
		System.out.println("生成id的值："+id);
		CommonResponses commonResponses = new CommonResponses();
		commonResponses.setId(id);
		commonResponses.setState("success");
		return commonResponses;
	}


	public void doUnitAcctSet(TokenContext tokenContext,String YWLSH) {

		AssertUtils.notEmpty(YWLSH,"业务流水号不能为空！");
		CCollectionUnitInformationBasicVice unitAccSet = collectionUnitInformationBasicViceDAO.getUnitBaseBus(YWLSH);
		AssertUtils.notEmpty(unitAccSet,"业务流水号错误");

		StCollectionUnitBusinessDetails collectionUnitBusinessDetails = unitAccSet.getDwywmx();

		CCollectionUnitBusinessDetailsExtension collectionUnitBusinessDetailsExtension = collectionUnitBusinessDetails.getExtension();

		StCommonUnit commonUnit = new StCommonUnit();
		StCollectionUnitAccount collectionUnitAccount = new StCollectionUnitAccount();
		CCollectionUnitAccountExtension collectionUnitAccountExtension = new CCollectionUnitAccountExtension();
		CCommonUnitExtension commonUnitExtension = new CCommonUnitExtension();
		collectionUnitAccount.setExtension(collectionUnitAccountExtension);
		commonUnit.setExtension(commonUnitExtension);
		commonUnit.setCollectionUnitAccount(collectionUnitAccount);

		//common
		commonUnit.setDwmc(unitAccSet.getDwmc());
		commonUnit.setDwzh(commonUnit.getDwzh()/*不填 自动生成*/);
		commonUnit.setDwdz(unitAccSet.getDwdz());
		commonUnit.setDwfrdbxm(unitAccSet.getDwfrdbxm());
		commonUnit.setDwfrdbzjlx(unitAccSet.getDwfrdbzjlx());
		commonUnit.setDwfrdbzjhm(unitAccSet.getDwfrdbzjhm());
		commonUnit.setDwlsgx(unitAccSet.getDwlsgx());
		commonUnit.setDwjjlx(unitAccSet.getDwjjlx());
		commonUnit.setDwsshy(unitAccSet.getDwsshy());
		commonUnit.setDwyb(unitAccSet.getDwyb());
		commonUnit.setDwdzxx(unitAccSet.getDwdzxx());
		commonUnit.setDwfxr(unitAccSet.getDwfxr());
		commonUnit.setJbrxm(unitAccSet.getJbrxm());
		commonUnit.setJbrgddhhm(unitAccSet.getJbrgddhhm());
		commonUnit.setJbrsjhm(unitAccSet.getJbrsjhm());
		commonUnit.setJbrzjlx(unitAccSet.getJbrzjlx());
		commonUnit.setJbrzjhm(unitAccSet.getJbrzjhm());
		commonUnit.setZzjgdm(unitAccSet.getZzjgdm());
		commonUnit.setDwslrq(unitAccSet.getDwslrq());
		commonUnit.setDwkhrq(new Date());
		commonUnit.setStyhmc(unitAccSet.getStyhmc());
		commonUnit.setStyhdm(unitAccSet.getStyhdm());


		//collectionUnitAccount.setDwzh(dwzh);
		collectionUnitAccount.setDwjcbl(unitAccSet.getDwjcbl());
		collectionUnitAccount.setGrjcbl(unitAccSet.getGrjcbl());
		collectionUnitAccount.setDwjcrs(0L);
		collectionUnitAccount.setDwfcrs(0L);
		collectionUnitAccount.setDwzhye(BigDecimal.ZERO);
		collectionUnitAccount.setDwxhrq(collectionUnitAccount.getDwxhrq()/*销户日期不填*/);
		collectionUnitAccount.setDwxhyy(collectionUnitAccount.getDwxhyy()/*销户原因不填*/);
		collectionUnitAccount.setDwzhzt(UnitAccountStatus.开户.getCode());
		collectionUnitAccount.setJzny(collectionUnitAccount.getJzny()/*缴至年月不填*/);

		collectionUnitAccountExtension.setFxzhkhyh(unitAccSet.getFxzhkhyh());
		collectionUnitAccountExtension.setFxzhhm(unitAccSet.getFxzhhm());
		collectionUnitAccountExtension.setFxzh(unitAccSet.getFxzh());
		collectionUnitAccountExtension.setZsye(BigDecimal.ZERO);

        commonUnitExtension.setKhwd(collectionUnitBusinessDetailsExtension.getYwwd().getId());
		commonUnitExtension.setDwlb(unitAccSet.getDwlb());
		commonUnitExtension.setKgqk(unitAccSet.getKgqk());
		commonUnitExtension.setDwxzqy(unitAccSet.getXzqy());
		commonUnitExtension.setPzjgmc(unitAccSet.getPzjgmc());
		commonUnitExtension.setPzjgjb(unitAccSet.getPzjgjb());
		commonUnitExtension.setDjzch(unitAccSet.getDjzch());
		commonUnitExtension.setDjsyyz(unitAccSet.getDjsyyz());
		commonUnitExtension.setDwlxdh(unitAccSet.getDwlxdh());
		commonUnitExtension.setDwschjny(unitAccSet.getDwschjny());
		commonUnitExtension.setCzhm(unitAccSet.getDwczhm());
		commonUnitExtension.setBeiZhu(unitAccSet.getBeiZhu());
		commonUnitExtension.setDwzl(unitAccSet.getBlzl());	//增加单位资料

		commonUnitDAO.save(commonUnit);
		String dwzh = collectionUnitAccount.getDwzh();		//正式产生 由单位账户表上的触发器产生
		commonUnit.setDwzh(dwzh);
		commonUnitDAO.update(commonUnit);	//

		collectionUnitBusinessDetails.setUnit(commonUnit);
		collectionUnitBusinessDetails.setDwzh(dwzh);
		unitAccSet.setDwzh(dwzh);

		collectionUnitBusinessDetailsExtension.setBjsj(new Date());

		collectionUnitBusinessDetailsDAO.update(collectionUnitBusinessDetails);
		collectionUnitInformationBasicViceDAO.update(unitAccSet);

		this.iSaveAuditHistory.saveNormalBusiness(YWLSH,tokenContext, CollectionBusinessType.开户.getName(),"办结");

		Msg rpcMsg = accountRpcService.registerAuth(ResUtils.noneAdductionValue(RpcAuth.class,new RpcAuth(){{
			this.setEmail(unitAccSet.getDwdzxx() );
			this.setType(3);
			this.setPassword(unitAccSet.getDwywmx().getUnit().getCollectionUnitAccount().getDwzh());
			this.setUser_id(unitAccSet.getDwywmx().getUnit().getCollectionUnitAccount().getId());
			this.setUsername(unitAccSet.getDwywmx().getUnit().getCollectionUnitAccount().getDwzh());
			this.setState(1);
		}}));

		if(rpcMsg.getCode() != ReturnCode.Success){
			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,rpcMsg.getMsg());
		}

	}


	private void iscollectionUnitInformationBasicViceAvailable(CCollectionUnitInformationBasicVice collectionUnitInformationBasicVice){


		List<StCommonPolicy> list_commonPolicy = DAOBuilder.instance(commonPolicyDAO).searchFilter(new HashMap<String, Object>(){{

			this.put("id",Arrays.asList("GRJCBLSX","GRJCBLXX","DWJCBLSX","DWJCBLXX"));

		}}).getList(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e);}

		});

		StCommonPolicy commonPolicy = new StCommonPolicy();

		list_commonPolicy.forEach(new Consumer<StCommonPolicy>() {
			@Override
			public void accept(StCommonPolicy stCommonPolicy) {

				if("GRJCBLSX".equals(stCommonPolicy.getId())){

					commonPolicy.setGrjcblsx(stCommonPolicy.getGrjcblsx()==null?BigDecimal.ZERO:stCommonPolicy.getGrjcblsx());
				}
				if("GRJCBLXX".equals(stCommonPolicy.getId())){

					commonPolicy.setGrjcblxx(stCommonPolicy.getGrjcblxx()==null?BigDecimal.ZERO:stCommonPolicy.getGrjcblxx());
				}
				if("DWJCBLSX".equals(stCommonPolicy.getId())){

					commonPolicy.setDwjcblsx(stCommonPolicy.getDwjcblsx()==null?BigDecimal.ZERO:stCommonPolicy.getDwjcblsx());
				}
				if("DWJCBLXX".equals(stCommonPolicy.getId())){

					commonPolicy.setDwjcblxx(stCommonPolicy.getDwjcblxx()==null?BigDecimal.ZERO:stCommonPolicy.getDwjcblxx());
				}
			}
		});

		CCollectionUnitBusinessDetailsExtension collectionUnitBusinessDetailsExtension = collectionUnitInformationBasicVice.getDwywmx().getExtension();

		if(collectionUnitInformationBasicVice.getGrjcbl()==null){

			throw new ErrorException(ReturnEnumeration.Data_MISS,"个人缴存比例");

		}else {

			if(collectionUnitInformationBasicVice.getGrjcbl().compareTo(commonPolicy.getGrjcblsx())>0||collectionUnitInformationBasicVice.getGrjcbl().compareTo(commonPolicy.getGrjcblxx())<0){

				throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"个人缴存比例"+collectionUnitInformationBasicVice.getGrjcbl().multiply(new BigDecimal("100"))+"不在"+commonPolicy.getGrjcblxx().multiply(new BigDecimal("100"))+"~"+commonPolicy.getGrjcblsx().multiply(new BigDecimal("100"))+"区间内");
			}
		}

		if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getFxzhkhyh())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"发薪账号开户银行");}

		if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getDwfxr())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"单位发薪日");}

		if(collectionUnitInformationBasicVice.getDwfxr().length()!=2||collectionUnitInformationBasicVice.getDwfxr().compareTo("01") < 0 || collectionUnitInformationBasicVice.getDwfxr().compareTo("31") > 0){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"单位发薪日");
		}
		/*if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getBeiZhu())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"备注");}
*/
		//if(!StringUtil.notEmpty(collectionUnitBusinessDetailsExtension.getBeizhu())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"备注");}

		if(collectionUnitInformationBasicVice.getDwjcbl()==null){

			throw new ErrorException(ReturnEnumeration.Data_MISS,"单位缴存比例");

		}else {

			if(collectionUnitInformationBasicVice.getDwjcbl().compareTo(commonPolicy.getDwjcblsx())>0||collectionUnitInformationBasicVice.getDwjcbl().compareTo(commonPolicy.getDwjcblxx())<0){

				throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"单位缴存比例"+collectionUnitInformationBasicVice.getDwjcbl().multiply(new BigDecimal("100"))+"不在"+commonPolicy.getDwjcblxx().multiply(new BigDecimal("100"))+"~"+commonPolicy.getDwjcblsx().multiply(new BigDecimal("100"))+"区间内");
			}
		}

		if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getFxzhhm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"发薪账号户名");}

		if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getDwschjny())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"单位首次汇缴年月");}

		if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getFxzh())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"发薪账号");}

		if(collectionUnitInformationBasicVice.getDwslrq()==null){ throw new ErrorException(ReturnEnumeration.Data_MISS,"单位设立日期");}

		if(collectionUnitInformationBasicVice.getDwslrq().getTime() > DateUtils.addDays(DateUtils.addMonths(DateUtil.safeStr2Date("yyyyMM",collectionUnitInformationBasicVice.getDwschjny()),1),-1).getTime()){

			throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"单位首次汇缴年月不能小于单位设立日期");
		}

		if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getStyhdm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"受托银行代码");}

		if(collectionUnitInformationBasicVice.getStyhdm().length()!=3){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"受托银行代码");
		}

		if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getStyhmc())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"受托银行名称");}

		if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getYwwd())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"业务网点");}

//		if(!StringUtil.notEmpty(collectionUnitBusinessDetailsExtension.getYwwd())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"业务网点");}

		//if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getDwczhm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"传真号码");}

		//if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getDwdzxx())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"单位电子信箱 ");}

		if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getDwyb())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"单位邮编");}

		if(collectionUnitInformationBasicVice.getDwyb().length()!=6){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"单位邮编");
		}
		//if(!StringUtil.notEmpty(collectionUnitInformationBasicVice.getDwlxdh())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"单位联系电话");}

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
		if(!this.iUploadImagesService.validateUploadFile(UploadFileModleType.归集.getCode(), UploadFileBusinessType.单位开户.getCode(),collectionUnitInformationBasicVice.getBlzl())){

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

		String Jbrsjhm = collectionUnitInformationBasicVice.getJbrsjhm();

		if(!StringUtil.notEmpty(Jbrsjhm)){ throw new ErrorException(ReturnEnumeration.Data_MISS,"经办人手机号码");}

		if(collectionUnitInformationBasicVice.getJbrsjhm().length()!=11){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"经办人手机号码");
		}

		if(!StringUtil.notEmpty(collectionUnitBusinessDetailsExtension.getJbrzjlx())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"经办人证件类型 ");}

		if(!StringUtil.notEmpty(collectionUnitBusinessDetailsExtension.getJbrxm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"经办人姓名");}

		if(!StringUtil.notEmpty(collectionUnitBusinessDetailsExtension.getJbrzjhm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"经办人证件号码");}

	}
	public CommonMessage addImportAcctInfo(TokenContext tokenContext , Map<Integer, Map<Integer, Object>> map){
		HashMap<String, String> KeyValue = new HashMap<>();
		for (int i = 1; i <= map.size(); i++) {
			switch (i) {
				case 1:
					KeyValue.put("DWMC", String.valueOf(map.get(i).get(1)));
					KeyValue.put("DWDZ", String.valueOf(map.get(i).get(5)));
					break;
				case 2:
					KeyValue.put("DWFR", String.valueOf(map.get(i).get(1)));
					KeyValue.put("SFZH", String.valueOf(map.get(i).get(3)));
					if(map.get(i).get(5)!=null){
						SingleDictionaryDetail LSGX = iDictionaryService.getCodeSingle( String.valueOf(map.get(i).get(5)),"UnitRelationship");
						KeyValue.put("LSGX",LSGX!=null?LSGX.getCode():"");
					}
					break;
				case 3:
					if(map.get(i).get(1)!=null) {
						SingleDictionaryDetail DWLB = iDictionaryService.getCodeSingle(String.valueOf(map.get(i).get(1)), "UnitClass");
						KeyValue.put("DWLB", DWLB != null ? DWLB.getCode() : "");
					}
					if(map.get(i).get(3)!=null) {
						SingleDictionaryDetail DWJJLX = iDictionaryService.getCodeSingle(String.valueOf(map.get(i).get(3)), "UnitEconomyClass");
						KeyValue.put("DWJJLX", DWJJLX != null ? DWJJLX.getCode() : "");
					}
					if(map.get(i).get(5)!=null) {
						SingleDictionaryDetail DWSSHY = iDictionaryService.getCodeSingle(String.valueOf(map.get(i).get(5)), "UnitIndustry");
						KeyValue.put("DWSSHY", DWSSHY != null ? DWSSHY.getCode() : "");
					}
					break;
				case 4:
					if(map.get(i).get(1)!=null) {
						SingleDictionaryDetail DWQY = iDictionaryService.getCodeSingle(String.valueOf(map.get(i).get(1)), "UnitArea");
						KeyValue.put("DWQY", DWQY != null ? DWQY.getCode() : "");
					}
					KeyValue.put("ZZJGDM", String.valueOf(map.get(i).get(3)));
					KeyValue.put("YZBH", String.valueOf(map.get(i).get(5)));
					break;
				case 5:
					SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
					KeyValue.put("DWSLRQ", ymd.format(new Date (String.valueOf(map.get(i).get(1)))));
					if(String.valueOf(map.get(i).get(3)).length()<2){
						KeyValue.put("DWFXR", String.valueOf("0"+map.get(i).get(3)));
					}else{
						KeyValue.put("DWFXR", String.valueOf(map.get(i).get(3)));
					}
					KeyValue.put("FXZHHM", String.valueOf(map.get(i).get(5)));
					break;
				case 6:
					KeyValue.put("FXZHKHYH", String.valueOf(map.get(i).get(1)));
					KeyValue.put("FXZHZH", String.valueOf(map.get(i).get(3)));
					KeyValue.put("WTYHMC", String.valueOf(map.get(i).get(5)));
					break;
				case 7:
					KeyValue.put("DWJCBL", String.valueOf(map.get(i).get(1)));
					KeyValue.put("GRJCBL", String.valueOf(map.get(i).get(3)));
					SimpleDateFormat ym = new SimpleDateFormat("yyyy-MM");
					KeyValue.put("SCHJNY",ym.format(new Date (String.valueOf(map.get(i).get(5)))));
					break;
				case 8:
					KeyValue.put("JBRXM", String.valueOf(map.get(i).get(1)));
					KeyValue.put("JBRSFZH", String.valueOf(map.get(i).get(3)));
					KeyValue.put("JBRSJHM", String.valueOf(map.get(i).get(5)));
					break;
			}
		}
		UnitAcctSetPost addUnitAcctPost = new UnitAcctSetPost();
		UnitAcctSetPostDWGJXX DWGJXX = new UnitAcctSetPostDWGJXX();
		//单位关键信息
		DWGJXX.setDWMC(KeyValue.get("DWMC"));
		DWGJXX.setDWDZ(KeyValue.get("DWDZ"));
		DWGJXX.setDWFRDBXM(KeyValue.get("DWFR"));
		DWGJXX.setDWFRDBZJHM(KeyValue.get("SFZH"));
		DWGJXX.setDWLSGX(KeyValue.get("LSGX"));
		DWGJXX.setDWLB(KeyValue.get("DWLB"));
		DWGJXX.setDWJJLX(KeyValue.get("DWJJLX"));
		DWGJXX.setDWSSHY(KeyValue.get("DWSSHY"));
		DWGJXX.setDWXZQY(KeyValue.get("DWQY"));
		DWGJXX.setZZJGDM(KeyValue.get("ZZJGDM"));
		DWGJXX.setDWFRDBZJLX("01");
		addUnitAcctPost.setDWGJXX(DWGJXX);
		//单位联系方式
		UnitAcctSetPostDWLXFS DWLXFS = new UnitAcctSetPostDWLXFS();
		DWLXFS.setDWYB(KeyValue.get("YZBH"));
		addUnitAcctPost.setDWLXFS(DWLXFS);
		//单位登记信息
		UnitAcctSetPostDWDJXX DWDJXX = new UnitAcctSetPostDWDJXX();
		DWDJXX.setDWSLRQ(KeyValue.get("DWSLRQ"));

//		String DWFXR = KeyValue.get("DWFXR").substring(0,2);
//		String a[] = DWFXR.split("\\.");
//		if(Integer.parseInt(a[0])<10){
//			DWDJXX.setDWFXR("0"+a[0]);
//		}else {
//			DWDJXX.setDWFXR(a[0]);
//		}
		DWDJXX.setDWFXR(KeyValue.get("DWFXR"));
		DWDJXX.setFXZHHM(KeyValue.get("FXZHHM"));
		DWDJXX.setFXZHKHYH(KeyValue.get("FXZHKHYH"));
		DWDJXX.setFXZH(KeyValue.get("FXZHZH"));
		DWDJXX.setDWJCBL(KeyValue.get("DWJCBL"));
		DWDJXX.setGRJCBL(KeyValue.get("GRJCBL"));
		DWDJXX.setDWSCHJNY(KeyValue.get("SCHJNY"));
		addUnitAcctPost.setDWDJXX(DWDJXX);
		//住房公积金中心委托银行信息
		UnitAcctSetPostWTYHXX WTYHXX = new UnitAcctSetPostWTYHXX();
		WTYHXX.setSTYHMC(KeyValue.get("WTYHMC"));
		if(KeyValue.get("WTYHMC")!=null){
			PageRes<SettlementSpecialBankAccount> accountList= settlementSpecialBankAccountManageService.getSpecialAccountList(KeyValue.get("WTYHMC"),null,null,1,10);
			if(accountList.getResults().size()>0 && accountList.getResults().get(0).getYHDM()!=null){
				WTYHXX.setSTYHDM(accountList.getResults().get(0).getYHDM());
			}else{
				WTYHXX.setSTYHDM("0");
			}
		}
		addUnitAcctPost.setWTYHXX(WTYHXX);
		//经办人信息
		UnitAcctSetPostJBRXX JBRXX = new UnitAcctSetPostJBRXX();
		JBRXX.setJBRXM(KeyValue.get("JBRXM"));
		JBRXX.setJBRZJLX("01");
		JBRXX.setJBRZJHM(KeyValue.get("JBRSFZH"));
		JBRXX.setJBRSJHM(KeyValue.get("JBRSJHM"));
		addUnitAcctPost.setJBRXX(JBRXX);
		addUnitAcctPost.setCZLX("0");
		addUnitAcctPost.setFJCBLZL("1");
		CommonMessage result = addImportUnitAccountSet(tokenContext, addUnitAcctPost);
		return result;
	}


	//新增import导入调用
	public CommonMessage addImportUnitAccountSet(TokenContext tokenContext, UnitAcctSetPost addUnitAcctPost) {

		CommonMessage commonMessage =  new CommonMessage();

		//region //参数检查
		boolean allowNull = addUnitAcctPost.getCZLX().equals("0");

		//addCheck(addUnitAcctPost);	//名称重复验证
		CommonMessage unitNameCheckMessage = getUnitNameCheckMessage(addUnitAcctPost.getDWGJXX().getDWMC());
		if(!unitNameCheckMessage.getCode().equals("01")){
			return unitNameCheckMessage;
		}
		if(addUnitAcctPost.getDWGJXX().getDWMC().equals("")){
			commonMessage.setCode(ReturnEnumeration.Parameter_MISS.getCode());
			commonMessage.setMessage("单位名称数据缺失");
			return commonMessage;
		}
		if(addUnitAcctPost.getDWGJXX().getDWDZ().equals("")){
			commonMessage.setCode(ReturnEnumeration.Parameter_MISS.getCode());
			commonMessage.setMessage("单位地址数据缺失");
			return commonMessage;
		}
		if(addUnitAcctPost.getDWGJXX().getDWDZ().length()<10){
			commonMessage.setCode(ReturnEnumeration.Parameter_MISS.getCode());
			commonMessage.setMessage("单位地址长度不能少于10个字");
			return commonMessage;
		}
		if(addUnitAcctPost.getDWGJXX().getDWFRDBXM().equals("")){
			commonMessage.setCode(ReturnEnumeration.Parameter_MISS.getCode());
			commonMessage.setMessage("法人/负责人姓名数据缺失");
			return commonMessage;
		}

		if(addUnitAcctPost.getDWGJXX().getDWFRDBZJHM().equals("")){
			commonMessage.setCode(ReturnEnumeration.Parameter_MISS.getCode());
			commonMessage.setMessage("法人/负责人身份证号数据缺失");
			return commonMessage;
		}
		if(PersonCertificateType.身份证.getCode().equals("01")&&!IdcardValidator.isValidatedAllIdcard(addUnitAcctPost.getDWGJXX().getDWFRDBZJHM())){
			commonMessage.setCode(ReturnEnumeration.Data_NOT_MATCH.getCode());
			commonMessage.setMessage("法人/负责人身份证号数据异常，不合规");
			return commonMessage;
		}
		if(addUnitAcctPost.getDWGJXX().getDWLSGX().equals("")){
			commonMessage.setCode(ReturnEnumeration.Parameter_MISS.getCode());
			commonMessage.setMessage("隶属关系数据缺失");
			return commonMessage;
		}
		if(addUnitAcctPost.getDWGJXX().getDWJJLX().equals("")){
			commonMessage.setCode(ReturnEnumeration.Parameter_MISS.getCode());
			commonMessage.setMessage("单位经济类型数据缺失");
			return commonMessage;
		}
		if(addUnitAcctPost.getDWGJXX().getDWSSHY().equals("")){
			commonMessage.setCode(ReturnEnumeration.Parameter_MISS.getCode());
			commonMessage.setMessage("单位所属行业数据缺失");
			return commonMessage;
		}
		if(addUnitAcctPost.getDWGJXX().getDWXZQY().equals("")){
			commonMessage.setCode(ReturnEnumeration.Parameter_MISS.getCode());
			commonMessage.setMessage("行政区域数据缺失");
			return commonMessage;
		}
		if(addUnitAcctPost.getDWGJXX().getZZJGDM().equals("")){
			commonMessage.setCode(ReturnEnumeration.Parameter_MISS.getCode());
			commonMessage.setMessage("组织机构代码数据缺失");
			return commonMessage;
		}
		if(addUnitAcctPost.getDWLXFS().getDWYB().equals("")){
			commonMessage.setCode(ReturnEnumeration.Parameter_MISS.getCode());
			commonMessage.setMessage("邮政编号数据缺失");
			return commonMessage;
		}
		if(addUnitAcctPost.getDWLXFS().getDWYB().length()!=6){
			commonMessage.setCode(ReturnEnumeration.Parameter_NOT_MATCH.getCode());
			commonMessage.setMessage("邮政编号长度为6位");
			return commonMessage;
		}
		if(!DateUtil.isFollowFormat(addUnitAcctPost.getDWDJXX().getDWSCHJNY()/*单位首次汇缴年月*/,formatNY,allowNull)){
			commonMessage.setCode(ReturnEnumeration.Parameter_NOT_MATCH.getCode());
			commonMessage.setMessage("单位首次汇缴年月数据异常");
			return commonMessage;
		}

		if(!DateUtil.isFollowFormat(addUnitAcctPost.getDWDJXX().getDWSLRQ()/*单位设立日期*/,format,allowNull)){
			commonMessage.setCode(ReturnEnumeration.Parameter_NOT_MATCH.getCode());
			commonMessage.setMessage("单位设立日期数据异常");
			return commonMessage;
		}
		if(addUnitAcctPost.getDWDJXX().getFXZHHM().equals("")){
			commonMessage.setCode(ReturnEnumeration.Parameter_NOT_MATCH.getCode());
			commonMessage.setMessage("发薪账户开户银行数据缺失");
			return commonMessage;
		}
		if(addUnitAcctPost.getDWDJXX().getFXZH().equals("")){
			commonMessage.setCode(ReturnEnumeration.Parameter_NOT_MATCH.getCode());
			commonMessage.setMessage("发薪账户账号数据缺失");
			return commonMessage;
		}
		if(addUnitAcctPost.getWTYHXX().getSTYHMC().equals("")){
			commonMessage.setCode(ReturnEnumeration.Parameter_NOT_MATCH.getCode());
			commonMessage.setMessage("委托银行名称数据缺失");
			return commonMessage;
		}
		if("0".equals(addUnitAcctPost.getWTYHXX().getSTYHDM())){
			commonMessage.setCode(ReturnEnumeration.Parameter_NOT_MATCH.getCode());
			commonMessage.setMessage("委托银行名称数据异常");
			return commonMessage;
		}
		if(addUnitAcctPost.getDWDJXX().getGRJCBL().equals("")){
			commonMessage.setCode(ReturnEnumeration.Parameter_MISS.getCode());
			commonMessage.setMessage("个人缴存比例数据缺失");
			return commonMessage;
		}

		if(addUnitAcctPost.getDWDJXX().getDWJCBL().equals("")){
			commonMessage.setCode(ReturnEnumeration.Parameter_MISS.getCode());
			commonMessage.setMessage("单位缴存比例数据缺失");
			return commonMessage;
		}

		if(addUnitAcctPost.getDWDJXX().getDWFXR().equals("")){
			commonMessage.setCode(ReturnEnumeration.Parameter_NOT_MATCH.getCode());
			commonMessage.setMessage("单位发薪日参数异常");
			return commonMessage;
		}
		if(addUnitAcctPost.getJBRXX().getJBRXM().equals("")){
			commonMessage.setCode(ReturnEnumeration.Parameter_NOT_MATCH.getCode());
			commonMessage.setMessage("经办人姓名数据缺失");
			return commonMessage;
		}
		if(addUnitAcctPost.getJBRXX().getJBRZJHM().equals("")){
			commonMessage.setCode(ReturnEnumeration.Parameter_NOT_MATCH.getCode());
			commonMessage.setMessage("经办人身份证号数据缺失");
			return commonMessage;
		}
		if(PersonCertificateType.身份证.getCode().equals("01")&&!IdcardValidator.isValidatedAllIdcard(addUnitAcctPost.getJBRXX().getJBRZJHM())){
			commonMessage.setCode(ReturnEnumeration.Data_NOT_MATCH.getCode());
			commonMessage.setMessage("经办人身份证号数据异常，不合规");
			return commonMessage;
		}
		if(addUnitAcctPost.getJBRXX().getJBRSJHM().equals("")){
			commonMessage.setCode(ReturnEnumeration.Parameter_NOT_MATCH.getCode());
			commonMessage.setMessage("经办人手机号码数据缺失");
			return commonMessage;
		}
		if(addUnitAcctPost.getJBRXX().getJBRSJHM().length()!=11){
			commonMessage.setCode(ReturnEnumeration.Parameter_NOT_MATCH.getCode());
			commonMessage.setMessage("经办人手机号码长度为11位");
			return commonMessage;
		}
		//region //必要字段申明&关系配置
		CCollectionUnitInformationBasicVice collectionUnitInformationBasicVice = new CCollectionUnitInformationBasicVice();

		CCollectionUnitBusinessDetailsExtension collectionUnitBusinessDetailsExtension = new CCollectionUnitBusinessDetailsExtension();

		StCollectionUnitBusinessDetails collectionUnitBusinessDetails = new StCollectionUnitBusinessDetails();

		collectionUnitBusinessDetails.setExtension(collectionUnitBusinessDetailsExtension);

		collectionUnitInformationBasicVice.setDwywmx(collectionUnitBusinessDetails);

		//endregion

		//region //字段填充

		//COMMON

		collectionUnitBusinessDetailsExtension.setCzmc(CollectionBusinessType.开户.getCode());
		collectionUnitBusinessDetailsExtension.setFchxhyy(collectionUnitBusinessDetailsExtension.getFchxhyy()/*操作原因 不填*/);
		collectionUnitBusinessDetailsExtension.setJzpzh("0"/*记账凭证号 不填*/);
		collectionUnitBusinessDetailsExtension.setSlsj(new Date()/*todo 受理时间*/);
		collectionUnitBusinessDetailsExtension.setShbtgyy(collectionUnitBusinessDetailsExtension.getShbtgyy()/*审核不通过原因 不填*/);

		collectionUnitBusinessDetails.setCzbz(collectionUnitBusinessDetails.getCzbz()/*todo 冲账标志*/);
		collectionUnitBusinessDetails.setFse(collectionUnitBusinessDetails.getFse()/*发生额 开户不填*/);
		collectionUnitBusinessDetails.setFslxe(collectionUnitBusinessDetails.getFslxe()/*发生利息额 开户不填*/);
		collectionUnitBusinessDetails.setFsrs(collectionUnitBusinessDetails.getFsrs()/*发生人数 开户不填*/);
		collectionUnitBusinessDetails.setHbjny(collectionUnitBusinessDetails.getHbjny()/*汇补缴年月 开户不填*/);
		collectionUnitBusinessDetails.setJzrq(collectionUnitBusinessDetails.getJzrq()/*记账日期 开户不填*/);
		collectionUnitBusinessDetails.setYwmxlx(CollectionBusinessType.开户.getCode());
		collectionUnitBusinessDetails.setUnit(collectionUnitBusinessDetails.getUnit()/*单位 开户不填*/);

		collectionUnitInformationBasicVice.setDdsj(collectionUnitInformationBasicVice.getDdsj()/*开户不填*/);
		collectionUnitInformationBasicVice.setCzmc(CollectionBusinessType.开户.getCode());
		collectionUnitInformationBasicVice.setDwschjny(collectionUnitInformationBasicVice.getDwschjny()/*开户不填*/);
		collectionUnitInformationBasicVice.setYwmxlx(CollectionBusinessType.开户.getCode());
		collectionUnitInformationBasicVice.setSlsj(new Date()/*todo 受理时间*/);

		//CZY
		collectionUnitInformationBasicVice.setCzy(tokenContext.getUserInfo().getCZY()/*操作员*/);
		collectionUnitBusinessDetailsExtension.setCzy(tokenContext.getUserInfo().getCZY()/*操作员*/);



		// DWDJXX
		collectionUnitInformationBasicVice.setGrjcbl(StringUtil.safeRatioBigDecimal(addUnitAcctPost.getDWDJXX().getGRJCBL()/*个人缴存比例*/));
		collectionUnitInformationBasicVice.setFxzhkhyh(addUnitAcctPost.getDWDJXX().getFXZHKHYH()/*发薪账号开户银行*/);
		collectionUnitInformationBasicVice.setDwfxr(addUnitAcctPost.getDWDJXX().getDWFXR()/*单位发薪日*/);

		collectionUnitInformationBasicVice.setDwjcbl(StringUtil.safeRatioBigDecimal(addUnitAcctPost.getDWDJXX().getDWJCBL()/*单位缴存比例*/));
		collectionUnitInformationBasicVice.setFxzhhm(addUnitAcctPost.getDWDJXX().getFXZHHM()/*发薪账号户名*/);
		collectionUnitInformationBasicVice.setDwschjny(DateUtil.safeStr2DBDate(formatNY,addUnitAcctPost.getDWDJXX().getDWSCHJNY()/*单位首次汇缴年月*/,DateUtil.dbformatYear_Month));
		collectionUnitInformationBasicVice.setFxzh(addUnitAcctPost.getDWDJXX().getFXZH()/*发薪账号*/);
		collectionUnitInformationBasicVice.setDwslrq(DateUtil.safeStr2Date(format,addUnitAcctPost.getDWDJXX().getDWSLRQ()/*单位设立日期*/));

		// WTYHXX

		collectionUnitInformationBasicVice.setStyhmc(addUnitAcctPost.getWTYHXX().getSTYHMC()/*受托银行名称*/);
		if(addUnitAcctPost.getWTYHXX().getSTYHDM()!=null){
			collectionUnitInformationBasicVice.setStyhdm(addUnitAcctPost.getWTYHXX().getSTYHDM()/*受托银行代码*/);
		}
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
		collectionUnitBusinessDetailsExtension.setStep("新建");
		// DWLXFS


		collectionUnitInformationBasicVice.setDwyb(addUnitAcctPost.getDWLXFS().getDWYB()/*单位邮编*/);
		collectionUnitInformationBasicVice.setDwlxdh(addUnitAcctPost.getDWLXFS().getDWLXDH()/*单位联系电话*/);

		// DWGJXX

		collectionUnitInformationBasicVice.setDwfrdbzjhm(addUnitAcctPost.getDWGJXX().getDWFRDBZJHM()/*单位法人代表证件号码*/);
		collectionUnitInformationBasicVice.setDwjjlx(addUnitAcctPost.getDWGJXX().getDWJJLX()/*单位经济类型*/);
		collectionUnitInformationBasicVice.setDwfrdbxm(addUnitAcctPost.getDWGJXX().getDWFRDBXM()/*单位法人代表姓名*/);
		collectionUnitInformationBasicVice.setXzqy(addUnitAcctPost.getDWGJXX().getDWXZQY()/*单位行政区域*/);

		collectionUnitInformationBasicVice.setDwlb(addUnitAcctPost.getDWGJXX().getDWLB()/*单位类别*/);
		collectionUnitInformationBasicVice.setDwfrdbzjlx(addUnitAcctPost.getDWGJXX().getDWFRDBZJLX()/*单位法人代表证件类型*/);
		collectionUnitInformationBasicVice.setZzjgdm(addUnitAcctPost.getDWGJXX().getZZJGDM()/*组织机构代码*/);
		collectionUnitInformationBasicVice.setDwlsgx(addUnitAcctPost.getDWGJXX().getDWLSGX()/*单位隶属关系*/);
		collectionUnitInformationBasicVice.setDwdz(addUnitAcctPost.getDWGJXX().getDWDZ()/*单位地址*/);
		collectionUnitInformationBasicVice.setDwmc(addUnitAcctPost.getDWGJXX().getDWMC()/*单位名称*/);
		collectionUnitInformationBasicVice.setDwsshy(addUnitAcctPost.getDWGJXX().getDWSSHY()/*单位所属行业*/);

		// JBRXX
		collectionUnitInformationBasicVice.setJbrzjlx(addUnitAcctPost.getJBRXX().getJBRZJLX()/*经办人证件类型 */);
		collectionUnitInformationBasicVice.setJbrgddhhm(addUnitAcctPost.getJBRXX().getJBRGDDHHM()/*经办人固定电话号码*/);
		collectionUnitInformationBasicVice.setJbrxm(addUnitAcctPost.getJBRXX().getJBRXM()/*经办人姓名*/);
		collectionUnitInformationBasicVice.setJbrzjhm(addUnitAcctPost.getJBRXX().getJBRZJHM()/*经办人证件号码*/);
		collectionUnitInformationBasicVice.setJbrsjhm(addUnitAcctPost.getJBRXX().getJBRSJHM()/*经办人手机号码*/);

		collectionUnitBusinessDetailsExtension.setJbrzjlx(addUnitAcctPost.getJBRXX().getJBRZJLX()/*经办人证件类型 */);
		collectionUnitBusinessDetailsExtension.setJbrxm(addUnitAcctPost.getJBRXX().getJBRXM()/*经办人姓名*/);
		collectionUnitBusinessDetailsExtension.setJbrzjhm(addUnitAcctPost.getJBRXX().getJBRZJHM()/*经办人证件号码*/);

		//endregion

		//region//唯一性验证
		StCommonUnit commonUnit = DAOBuilder.instance(this.commonUnitDAO).searchFilter(new HashMap<String, Object>(){{

			this.put("dwmc",collectionUnitInformationBasicVice.getDwmc());

		}}).getObject(new DAOBuilder.ErrorHandler() {

			@Override

			public void error(Exception e) { throw new ErrorException(e);}
		});

		if(addUnitAcctPost.getCZLX().equals("1")&&commonUnit!=null) {
			commonMessage.setCode(ReturnEnumeration.Data_Already_Eeist.getCode());
			commonMessage.setMessage("单位名称已存在");
			return commonMessage;
		}
		//endregion

		//region //修改状态


		CCollectionUnitInformationBasicVice savedVice = DAOBuilder.instance(this.collectionUnitInformationBasicViceDAO).entity(collectionUnitInformationBasicVice).saveThenFetchObject(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e); }

		});

		if (savedVice == null||savedVice.getDwywmx()==null) {
			commonMessage.setCode(ReturnEnumeration.Data_NOT_MATCH.getCode());
			commonMessage.setMessage("业务记录异常");
			return commonMessage;
		}

		savedVice.getDwywmx().getExtension().setDdsj(new Date());

		DAOBuilder.instance(collectionUnitInformationBasicViceDAO).entity(savedVice).save(new DAOBuilder.ErrorHandler() {
			@Override
			public void error(Exception e) { throw new ErrorException(e); }

		});


		//endregion

		//region //在途验证

		if((addUnitAcctPost.getCZLX().equals("1")&&!DAOBuilder.instance(this.collectionUnitInformationBasicViceDAO).searchFilter(new HashMap<String, Object>(){{

			this.put("dwmc",addUnitAcctPost.getDWGJXX().getDWMC());

		}}).extension(new IBaseDAO.CriteriaExtension() {
			@Override
			public void extend(Criteria criteria) {

				criteria.add(Restrictions.not(Restrictions.in(CriteriaUtils.addAlias(criteria,"dwywmx.cCollectionUnitBusinessDetailsExtension.step"), (Collection)CollectionUtils.flatmap(CollectionUtils.merge(CollectionBusinessStatus.办结.getSubTypes(), CollectionBusinessStatus.新建.getSubTypes()), new CollectionUtils.Transformer<CollectionBusinessStatus, String>() {

					@Override
					public String tansform(CollectionBusinessStatus var1) { return var1.getName(); }

				}))));
				criteria.add(Restrictions.ne("dwywmx.ywlsh",savedVice.getDwywmx().getYwlsh()));
				criteria.add(Restrictions.eq("cCollectionUnitBusinessDetailsExtension.czmc",CollectionBusinessType.开户.getCode()));
			}
		}).isUnique(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e);}

		}))){
			commonMessage.setCode(ReturnEnumeration.Business_In_Process.getCode());
			commonMessage.setMessage("单位名称"+addUnitAcctPost.getDWGJXX().getDWMC()+"业务已在办理");
			return commonMessage;
		}

		if(addUnitAcctPost.getCZLX().equals("1")){

			this.iscollectionUnitInformationBasicViceAvailable(collectionUnitInformationBasicVice);


		}

		this.iSaveAuditHistory.saveNormalBusiness(savedVice.getDwywmx().getYwlsh(),tokenContext, CollectionBusinessType.开户.getName(),"新建");
		//endregion
		commonMessage.setCode("0");
		commonMessage.setMessage("Success");
		return commonMessage;

	}
}
