package com.handge.housingfund.collection.service.indiacctmanage;


import com.handge.housingfund.collection.utils.BusUtils;
import com.handge.housingfund.collection.utils.ComUtils;
import com.handge.housingfund.collection.utils.StateMachineUtils;
import com.handge.housingfund.common.service.ISaveAuditHistory;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.AccountRpcService;
import com.handge.housingfund.common.service.account.model.Msg;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.PageResNew;
import com.handge.housingfund.common.service.account.model.RpcAuth;
import com.handge.housingfund.common.service.collection.enumeration.*;
import com.handge.housingfund.common.service.collection.model.individual.*;
import com.handge.housingfund.common.service.collection.service.indiacctmanage.IndiAcctAlter;
import com.handge.housingfund.common.service.finance.IVoucherManagerService;
import com.handge.housingfund.common.service.finance.model.enums.VoucherBusinessType;
import com.handge.housingfund.common.service.loan.model.CommonResponses;
import com.handge.housingfund.common.service.others.IDictionaryService;
import com.handge.housingfund.common.service.others.IPdfService;
import com.handge.housingfund.common.service.others.ISMSCommon;
import com.handge.housingfund.common.service.others.IUploadImagesService;
import com.handge.housingfund.common.service.others.enums.UploadFileBusinessType;
import com.handge.housingfund.common.service.others.enums.UploadFileModleType;
import com.handge.housingfund.common.service.others.model.CommonDictionary;
import com.handge.housingfund.common.service.others.model.SingleDictionaryDetail;
import com.handge.housingfund.common.service.sms.enums.SMSTemp;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.*;
import com.handge.housingfund.database.utils.CriteriaUtils;
import com.handge.housingfund.database.utils.DAOBuilder;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Consumer;

/*
 * Created by Liujuhao on 2017/7/3.
 */
@SuppressWarnings({"Duplicates", "Convert2Lambda", "Anonymous2MethodRef", "SpringAutowiredFieldsWarningInspection", "SpringJavaAutowiringInspection", "serial", "RedundantCast", "BooleanMethodIsAlwaysInverted", "SpringJavaInjectionPointsAutowiringInspection"})
@Service
public class IndiAcctAlterImpl implements IndiAcctAlter {

	@Autowired
	private ICCollectionIndividualAccountBasicViceDAO collectionIndividualAccountBasicViceDAO;
	@Autowired
	private ICLoanHousingPersonInformationBasicDAO loanHousingPersonInformationBasicDAO;
	@Autowired
	private IStCollectionPersonalBusinessDetailsDAO collectionPersonalBusinessDetailsDAO;
	@Autowired
	private IStCommonPersonDAO commonPersonDAO;
	@Autowired
	private IStCommonUnitDAO commonUnitDAO;
	@Autowired
	private com.handge.housingfund.statemachineV2.IStateMachineService stateMachineService;
	@Autowired
	private AccountRpcService accountRpcService;
	@Autowired
	private IPdfService pdfService;
	@Autowired
	private ICAuditHistoryDAO icAuditHistoryDAO;
	@Autowired
	private ICAccountNetworkDAO cAccountNetworkDAO;
	@Autowired
	private ISaveAuditHistory iSaveAuditHistory;
	@Autowired
	private IDictionaryService iDictionaryService;
	@Autowired
	private IUploadImagesService iUploadImagesService;
	@Autowired
	private IVoucherManagerService voucherManagerService;
	@Autowired
	private IStCollectionUnitBusinessDetailsDAO collectionUnitBusinessDetailsDAO;
	@Autowired
	private IStHousingPersonalLoanDAO housingPersonalLoanDAO;
	@Autowired
	private ICLoanHousingBusinessProcessDAO loanHousingBusinessProcessDAO;
	@Autowired
	private ISMSCommon ismsCommon;
	private static String format = "yyyy-MM-dd";
	private static String formatNYRSF = "yyyy-MM-dd HH:mm";
	private static String formatNY = "yyyy-MM";

	// completed 已测试 - 正常
	@Override
	public PageRes<ListOperationAcctsResRes> getAlterInfo(TokenContext tokenContext,final String YWWD,final String DWMC, final String ZHZT, final String XingMing, final String ZJHM, final String GRZH, final String CZMC, String page, String pagesize,String KSSJ,String JSSJ) {

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
		PageRes pageRes =  new PageRes();

		List<StCollectionPersonalBusinessDetails> list_business = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{

			if (StringUtil.notEmpty(GRZH)) { this.put("grzh", GRZH); }

			if (StringUtil.notEmpty(DWMC)) { this.put("unit.dwmc", DWMC); }

			if (StringUtil.notEmpty(ZHZT)&&!ZHZT.equals(CollectionBusinessStatus.所有.getName())&&!ZHZT.equals(CollectionBusinessStatus.待审核.getName())) { this.put("cCollectionPersonalBusinessDetailsExtension.step", ZHZT); }

			if (StringUtil.notEmpty(XingMing)) { this.put("person.xingMing", XingMing); }

			if (StringUtil.notEmpty(ZJHM )) { this.put("person.zjhm", ZJHM); }

		}}).searchOption(SearchOption.FUZZY).extension(new IBaseDAO.CriteriaExtension() {
			@Override
			public void extend(Criteria criteria) {


				if (!(StringUtil.notEmpty(ZHZT)&&!ZHZT.equals(CollectionBusinessStatus.所有.getName())&&!ZHZT.equals(CollectionBusinessStatus.待审核.getName()))){

					criteria.createAlias("cCollectionPersonalBusinessDetailsExtension","cCollectionPersonalBusinessDetailsExtension");
				}
				if(CollectionBusinessStatus.待审核.getName().equals(ZHZT)){

					criteria.add(Restrictions.like("cCollectionPersonalBusinessDetailsExtension.step",CollectionBusinessStatus.待某人审核.getName()));
				}
				criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.czmc", CollectionBusinessType.变更.getCode()));

				if (DateUtil.isFollowFormat(KSSJ,formatNYRSF,false)){
					criteria.add(Restrictions.ge("created_at",DateUtil.safeStr2Date(formatNYRSF,KSSJ)));
					criteria.add(Restrictions.ge("cCollectionPersonalBusinessDetailsExtension.created_at",DateUtil.safeStr2Date(formatNYRSF,KSSJ)));
				}
				if (DateUtil.isFollowFormat(JSSJ,formatNYRSF,false)){
					criteria.add(Restrictions.lt("created_at",DateUtil.safeStr2Date(formatNYRSF,JSSJ)));
					criteria.add(Restrictions.lt("cCollectionPersonalBusinessDetailsExtension.created_at",DateUtil.safeStr2Date(formatNYRSF,JSSJ)));
				}

				criteria.createAlias("cCollectionPersonalBusinessDetailsExtension.ywwd","ywwd");

				if(StringUtil.isEmpty(DWMC)) {
					criteria.createAlias("unit", "unit");
				}
				criteria.createAlias("unit.cCommonUnitExtension", "cCommonUnitExtension");

				if(!"1".equals(tokenContext.getUserInfo().getYWWD())) {
					if(StringUtil.notEmpty(YWWD)){

						if(YWWD.equals(tokenContext.getUserInfo().getYWWD())){

							criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.ywwd.id", tokenContext.getUserInfo().getYWWD()));

						}else {
							criteria.add(Restrictions.and(
									Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.ywwd.id", YWWD),
									Restrictions.eq("cCommonUnitExtension.khwd", tokenContext.getUserInfo().getYWWD())
							));
						}

					}else {
						criteria.add(Restrictions.or(
								Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.ywwd.id", tokenContext.getUserInfo().getYWWD()),
								Restrictions.eq("cCommonUnitExtension.khwd", tokenContext.getUserInfo().getYWWD())
						));
					}
				}else {

					if(StringUtil.notEmpty(YWWD)){

						criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.ywwd.id", YWWD));
					}
				}
			}

		}).pageOption(pageRes,pagesize_number,page_number).searchOption(SearchOption.FUZZY).getList(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e); }

		});
		//endregion

		return new PageRes<ListOperationAcctsResRes>() {{

			this.setResults(new ArrayList<ListOperationAcctsResRes>() {{

				for (StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails : list_business) {

					CCollectionIndividualAccountBasicVice collectionIndividualAccountBasicVice = collectionPersonalBusinessDetails.getIndividualAccountBasicVice() == null ? new CCollectionIndividualAccountBasicVice():collectionPersonalBusinessDetails.getIndividualAccountBasicVice();

					StCommonPerson commonPerson = collectionPersonalBusinessDetails.getPerson() == null ? new StCommonPerson():collectionPersonalBusinessDetails.getPerson();

					StCommonUnit commonUnit = collectionPersonalBusinessDetails.getPerson().getUnit() == null ? new StCommonUnit():collectionPersonalBusinessDetails.getPerson().getUnit();

					this.add(new ListOperationAcctsResRes() {{

						this.setId(collectionPersonalBusinessDetails.getId());
						this.setXingming(collectionIndividualAccountBasicVice.getXingMing());

						this.setGRJCJS(""+collectionIndividualAccountBasicVice.getGrjcjs());

						this.setYWLSH(collectionPersonalBusinessDetails.getYwlsh());

						this.setSLSJ(DateUtil.date2Str(collectionPersonalBusinessDetails.getCreated_at(), formatNYRSF));

						this.setZhuangTai(collectionIndividualAccountBasicVice.getGrywmx().getExtension().getStep());

						this.setZJHM(collectionIndividualAccountBasicVice.getZjhm());

						if(commonPerson.getCollectionPersonalAccount()!=null) {

							this.setGRZHZT(commonPerson.getCollectionPersonalAccount().getGrzhzt());
						}
						if (commonUnit.getCollectionUnitAccount() != null&&collectionIndividualAccountBasicVice.getGrjcjs()!=null&&commonUnit.getCollectionUnitAccount().getGrjcbl()!=null&&commonUnit.getCollectionUnitAccount().getDwjcbl()!=null) {

							this.setYJCE(""+(collectionIndividualAccountBasicVice.getGrjcjs().floatValue() * (commonUnit.getCollectionUnitAccount().getGrjcbl().floatValue() + commonUnit.getCollectionUnitAccount().getDwjcbl().floatValue())));
						}

						this.setDWMC(commonUnit.getDwmc());

						this.setGRZH(collectionPersonalBusinessDetails.getGrzh());
						this.setYWWD(collectionPersonalBusinessDetails.getExtension().getYwwd().getMingCheng());
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
	public PageResNew<ListOperationAcctsResRes> getAlterInfo(TokenContext tokenContext,final String YWWD, String DWMC, String ZHZT, String XingMing, String ZJHM, String GRZH, String CZMC, String marker, String action, String pagesize, String KSSJ, String JSSJ) {
		//region //参数检查

		int pagesize_number = 0;

		try {

			if(pagesize!=null) { pagesize_number = Integer.parseInt(pagesize); }

		}catch (Exception e){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"页码");
		}

		//endregion

		//region //必要字段查询

		List<StCollectionPersonalBusinessDetails> list_business = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {{

			if (StringUtil.notEmpty(GRZH)) { this.put("grzh", GRZH); }

			if (StringUtil.notEmpty(DWMC)) { this.put("unit.dwmc", DWMC); }

			if (StringUtil.notEmpty(ZHZT)&&!ZHZT.equals(CollectionBusinessStatus.所有.getName())&&!ZHZT.equals(CollectionBusinessStatus.待审核.getName())) { this.put("cCollectionPersonalBusinessDetailsExtension.step", ZHZT); }

			if (StringUtil.notEmpty(XingMing)) { this.put("person.xingMing", XingMing); }

			if (StringUtil.notEmpty(ZJHM )) { this.put("person.zjhm", ZJHM); }

		}}).searchOption(SearchOption.FUZZY).extension(new IBaseDAO.CriteriaExtension() {
			@Override
			public void extend(Criteria criteria) {


				if (!(StringUtil.notEmpty(ZHZT)&&!ZHZT.equals(CollectionBusinessStatus.所有.getName())&&!ZHZT.equals(CollectionBusinessStatus.待审核.getName()))){

					criteria.createAlias("cCollectionPersonalBusinessDetailsExtension","cCollectionPersonalBusinessDetailsExtension");
				}
				if(CollectionBusinessStatus.待审核.getName().equals(ZHZT)){

					criteria.add(Restrictions.like("cCollectionPersonalBusinessDetailsExtension.step",CollectionBusinessStatus.待某人审核.getName()));
				}
				criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.czmc", CollectionBusinessType.变更.getCode()));

				if (DateUtil.isFollowFormat(KSSJ,formatNYRSF,false)){
					criteria.add(Restrictions.ge("created_at",DateUtil.safeStr2Date(formatNYRSF,KSSJ)));
					criteria.add(Restrictions.ge("cCollectionPersonalBusinessDetailsExtension.created_at",DateUtil.safeStr2Date(formatNYRSF,KSSJ)));
				}
				if (DateUtil.isFollowFormat(JSSJ,formatNYRSF,false)){
					criteria.add(Restrictions.lt("created_at",DateUtil.safeStr2Date(formatNYRSF,JSSJ)));
					criteria.add(Restrictions.lt("cCollectionPersonalBusinessDetailsExtension.created_at",DateUtil.safeStr2Date(formatNYRSF,JSSJ)));
				}

				criteria.createAlias("cCollectionPersonalBusinessDetailsExtension.ywwd","ywwd");

				if(StringUtil.isEmpty(DWMC)) {
					criteria.createAlias("unit", "unit");
				}
				criteria.createAlias("unit.cCommonUnitExtension", "cCommonUnitExtension");

				if(!"1".equals(tokenContext.getUserInfo().getYWWD())) {
					criteria.add(Restrictions.or(
							Restrictions.and(
									Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.ywwd.id", tokenContext.getUserInfo().getYWWD()),
									Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.czy", tokenContext.getUserInfo().getCZY())),
							Restrictions.eq("cCommonUnitExtension.khwd", tokenContext.getUserInfo().getYWWD())
					));
				}
			}

		}).pageOption(marker,action,pagesize_number).searchOption(SearchOption.FUZZY).getList(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e); }

		});
		//endregion

		return new PageResNew<ListOperationAcctsResRes>() {{


			this.setResults(action,new ArrayList<ListOperationAcctsResRes>() {{

				for (StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails : list_business) {

					CCollectionIndividualAccountBasicVice collectionIndividualAccountBasicVice = collectionPersonalBusinessDetails.getIndividualAccountBasicVice() == null ? new CCollectionIndividualAccountBasicVice():collectionPersonalBusinessDetails.getIndividualAccountBasicVice();

					StCommonPerson commonPerson = collectionPersonalBusinessDetails.getPerson() == null ? new StCommonPerson():collectionPersonalBusinessDetails.getPerson();

					StCommonUnit commonUnit = collectionPersonalBusinessDetails.getPerson().getUnit() == null ? new StCommonUnit():collectionPersonalBusinessDetails.getPerson().getUnit();

					this.add(new ListOperationAcctsResRes() {{

						this.setId(collectionPersonalBusinessDetails.getId());
						this.setXingming(collectionIndividualAccountBasicVice.getXingMing());

						this.setGRJCJS(""+collectionIndividualAccountBasicVice.getGrjcjs());

						this.setYWLSH(collectionPersonalBusinessDetails.getYwlsh());

						this.setSLSJ(DateUtil.date2Str(collectionPersonalBusinessDetails.getCreated_at(), formatNYRSF));

						this.setZhuangTai(collectionIndividualAccountBasicVice.getGrywmx().getExtension().getStep());

						this.setZJHM(collectionIndividualAccountBasicVice.getZjhm());

						if(commonPerson.getCollectionPersonalAccount()!=null) {

							this.setGRZHZT(commonPerson.getCollectionPersonalAccount().getGrzhzt());
						}
						if (commonUnit.getCollectionUnitAccount() != null&&collectionIndividualAccountBasicVice.getGrjcjs()!=null&&commonUnit.getCollectionUnitAccount().getGrjcbl()!=null&&commonUnit.getCollectionUnitAccount().getDwjcbl()!=null) {

							this.setYJCE(""+(collectionIndividualAccountBasicVice.getGrjcjs().floatValue() * (commonUnit.getCollectionUnitAccount().getGrjcbl().floatValue() + commonUnit.getCollectionUnitAccount().getDwjcbl().floatValue())));
						}

						this.setDWMC(commonUnit.getDwmc());

						this.setGRZH(collectionPersonalBusinessDetails.getGrzh());
					}});
				}
			}});
		}};
	}

	// completed 已测试 - 正常 xc
	@Override
	public AddIndiAcctAlterRes addAcctAlter(TokenContext tokenContext, IndiAcctAlterPost addIndiAcctAlter) {

		//region //检查参数

		boolean allowNull = addIndiAcctAlter.getCZLX().equals("0");

		if(!DateUtil.isFollowFormat(addIndiAcctAlter.getGRJCXX().getGJJSCHJNY()/*公积金首次汇缴年月*/,format,allowNull)){throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"公积金首次汇缴年月");}

		if(!DateUtil.isFollowFormat(addIndiAcctAlter.getGRXX().getCSNY()/*出生年月*/,format,allowNull)){throw  new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"出生年月");}

		if(!StringUtil.isDigits(addIndiAcctAlter.getGRJCXX().getGRJCJS()+""/*个人缴存基数*/,allowNull)){throw  new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"个人缴存基数");}

		if(!StringUtil.isDigits(addIndiAcctAlter.getGRXX().getJTYSR()/*家庭月收入*/+"")){throw  new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"家庭月收入");}

		if(!DateUtil.isFollowFormat(addIndiAcctAlter.getGRJCXX().getGJJSCHJNY()/*公积金首次汇缴年月*/,format,allowNull)){throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"公积金首次汇缴年月");}

		if(!DateUtil.isFollowFormat(addIndiAcctAlter.getGRXX().getCSNY()/*出生年月*/,format,allowNull)){throw  new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"出生年月");}

		if(!StringUtil.isDigits(addIndiAcctAlter.getGRJCXX().getGRJCJS()+""/*个人缴存基数*/,allowNull)){throw  new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"个人缴存基数");}

		if (!StringUtil.notEmpty(addIndiAcctAlter.getGRJCXX().getGRCKZHHM()/*个人存款账户号码*/, allowNull)) { throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "个人存款账户号码"); }

		if (!StringUtil.notEmpty(addIndiAcctAlter.getJBRZJLX()/*经办人证件类型*/, allowNull)) { throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "经办人证件类型"); }

		if (!StringUtil.notEmpty(addIndiAcctAlter.getJBRXM()/*经办人姓名*/, allowNull)) { throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "经办人姓名"); }

		if (!StringUtil.notEmpty(addIndiAcctAlter.getGRXX().getHYZK()/*婚姻状况*/, allowNull)) { throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "婚姻状况"); }

		if (!StringUtil.notEmpty(addIndiAcctAlter.getGRXX().getZJLX()/*证件类型*/, allowNull)) { throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "证件类型"); }

		if (!StringUtil.notEmpty(addIndiAcctAlter.getGRXX().getSJHM()/*手机号码*/, allowNull)) { throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "手机号码"); }

		if (!StringUtil.notEmpty(addIndiAcctAlter.getGRXX().getZJHM()/*证件号码*/, allowNull)) { throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "证件号码"); }

		if (!StringUtil.notEmpty(addIndiAcctAlter.getGRXX().getXMQP()/*姓名全拼*/, allowNull)) { throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "姓名全拼"); }

		if (!StringUtil.notEmpty(addIndiAcctAlter.getGRXX().getXingBie()/*性别*/, allowNull)) { throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "性别"); }

		if (!StringUtil.notEmpty(addIndiAcctAlter.getGRXX().getXingMing()/*姓名*/, allowNull)) { throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "姓名"); }

		if (!StringUtil.notEmpty(addIndiAcctAlter.getJBRZJHM()/*经办人证件号码*/, allowNull)) { throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "经办人证件号码"); }

		if (!StringUtil.notEmpty(addIndiAcctAlter.getDWXX().getDWZH()/*单位账号*/, allowNull)) { throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "单位账号"); }

		//endregion

		//region //必要参数声明&关系配置
		CAccountNetwork network = DAOBuilder.instance(cAccountNetworkDAO).searchFilter(new HashMap<String, Object>() {{

			this.put("id",tokenContext.getUserInfo().getYWWD());

		}}).getObject(new DAOBuilder.ErrorHandler() {
			@Override
			public void error(Exception e) { throw new ErrorException(e); }
		});

		CCollectionIndividualAccountBasicVice collectionIndividualAccountBasicVice = new CCollectionIndividualAccountBasicVice();

		StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = new StCollectionPersonalBusinessDetails();

		CCollectionPersonalBusinessDetailsExtension collectionPersonalBusinessDetailsExtension = new CCollectionPersonalBusinessDetailsExtension();

		collectionPersonalBusinessDetails.setExtension(collectionPersonalBusinessDetailsExtension);

		collectionIndividualAccountBasicVice.setGrywmx(collectionPersonalBusinessDetails);

		StCommonPerson commonPerson = DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {{

			this.put("grzh",addIndiAcctAlter.getGRZH());

		}}).getObject(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e); }
		});

		if (commonPerson == null || commonPerson.getUnit() == null||commonPerson.getExtension() == null) {

			throw new ErrorException(ReturnEnumeration.Data_MISS,"个人信息");
		}


		if(commonPerson.getUnit().getDwzh()!=null&&!commonPerson.getUnit().getDwzh().equals(addIndiAcctAlter.getDWXX().getDWZH())){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"单位账号");
		}

		if(UnitAccountStatus.封存.getCode().equals(commonPerson.getUnit().getCollectionUnitAccount().getDwzhzt())){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"单位已封存");
		}

		if("1".equals(addIndiAcctAlter.getCZLX())&&"02".equals(commonPerson.getExtension().getSfdj())){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"账号未解冻");
		}

		//endregion

		//region  //字段填充
		// COMMON
		collectionPersonalBusinessDetails.setYwlsh(collectionPersonalBusinessDetails.getYwlsh()/* 不填 数据库自动生成 */);
		collectionPersonalBusinessDetails.setCzbz(CommonFieldType.非冲账.getCode()/* todo 冲账标识 */);
		collectionPersonalBusinessDetails.setDngjfse(collectionPersonalBusinessDetails.getDngjfse()/* 变更此字段不填 */);
		collectionPersonalBusinessDetails.setFse(collectionPersonalBusinessDetails.getFse()/* 变更此字段不填 */);
		collectionPersonalBusinessDetails.setFslxe(collectionPersonalBusinessDetails.getFslxe()/* 变更此字段不填 */);
		collectionPersonalBusinessDetails.setGjhtqywlx(CollectionBusinessType.其他.getCode());
		collectionPersonalBusinessDetails.setGrzh(addIndiAcctAlter.getGRZH());
		collectionPersonalBusinessDetails.setJzrq(collectionPersonalBusinessDetails.getJzrq()/* todo 记账日期 */);
		collectionPersonalBusinessDetails.setSnjzfse(collectionPersonalBusinessDetails.getSnjzfse()/* 变更此字段不填 */);
		collectionPersonalBusinessDetails.setTqyy(collectionPersonalBusinessDetails.getTqyy()/* 变更此字段不填 */);
		collectionPersonalBusinessDetails.setTqfs(collectionPersonalBusinessDetails.getTqfs()/* 变更此字段不填 */);
		collectionPersonalBusinessDetails.setUnit(commonPerson.getUnit());
		collectionPersonalBusinessDetails.setPerson(commonPerson);
		collectionPersonalBusinessDetails.setCreated_at(new Date());

		collectionPersonalBusinessDetailsExtension.setSlsj(new Date()/* todo 受理时间 */);
		collectionPersonalBusinessDetailsExtension.setCzmc(CollectionBusinessType.变更.getCode());
		collectionPersonalBusinessDetailsExtension.setDjje(collectionPersonalBusinessDetailsExtension.getDjje()/* 变更此字段不填 */);
		collectionPersonalBusinessDetailsExtension.setBeizhu(collectionPersonalBusinessDetailsExtension.getBeizhu()/* 变更此字段不填 */);
		collectionPersonalBusinessDetailsExtension.setZcdw(collectionPersonalBusinessDetailsExtension.getZcdw()/* 变更此字段不填 */);
		collectionPersonalBusinessDetailsExtension.setShbtgyy(collectionPersonalBusinessDetailsExtension.getShbtgyy()/*不填*/);


		collectionIndividualAccountBasicVice.setYwlsh(collectionIndividualAccountBasicVice.getYwlsh()/* 不填 数据库自动生成 */);
		collectionIndividualAccountBasicVice.setGjhtqywlx(CollectionBusinessType.变更.getCode());
		collectionIndividualAccountBasicVice.setSlsj(new Date()/* todo 受理时间 */);
		collectionIndividualAccountBasicVice.setGrzh(addIndiAcctAlter.getGRZH());
		collectionIndividualAccountBasicVice.setGrzhye(commonPerson.getCollectionPersonalAccount()==null?BigDecimal.ZERO:commonPerson.getCollectionPersonalAccount().getGrzhye());



		// GRJCXX
		collectionIndividualAccountBasicVice.setGjjschjny(DateUtil.safeStr2DBDate(format,addIndiAcctAlter.getGRJCXX().getGJJSCHJNY()/*公积金首次汇缴年月*/,DateUtil.dbformatYear_Month));
		collectionIndividualAccountBasicVice.setGrckzhhm(addIndiAcctAlter.getGRJCXX().getGRCKZHHM()/*个人存款账户号码*/);
		collectionIndividualAccountBasicVice.setGrjcjs(StringUtil.safeBigDecimal(addIndiAcctAlter.getGRJCXX().getGRJCJS()/*个人缴存基数*/+""));
		collectionIndividualAccountBasicVice.setGrckzhkhyhdm(addIndiAcctAlter.getGRJCXX().getGRCKZHKHYHDM()/*个人存款账户开户银行代码*/);
		collectionIndividualAccountBasicVice.setGrckzhkhyhmc(addIndiAcctAlter.getGRJCXX().getGRCKZHKHYHMC()/*个人存款账户开银行名称*/);


		// JBRZJLX
		collectionPersonalBusinessDetailsExtension.setJbrzjlx(addIndiAcctAlter.getJBRZJLX()/*经办人证件类型*/);

		// BLZL
		collectionIndividualAccountBasicVice.setBlzl(addIndiAcctAlter.getBLZL()/*办理资料*/);

		// JBRXM
		collectionPersonalBusinessDetailsExtension.setJbrxm(addIndiAcctAlter.getJBRXM()/*经办人姓名*/);

		// GRXX
		collectionIndividualAccountBasicVice.setHyzk(addIndiAcctAlter.getGRXX().getHYZK()/*婚姻状况*/);
		collectionIndividualAccountBasicVice.setJtzz(addIndiAcctAlter.getGRXX().getJTZZ()/*家庭住址*/);
		collectionIndividualAccountBasicVice.setCsny(DateUtil.safeStr2DBDate(format,addIndiAcctAlter.getGRXX().getCSNY()/*出生年月*/,DateUtil.dbformatYear_Month));
		collectionIndividualAccountBasicVice.setZjlx(addIndiAcctAlter.getGRXX().getZJLX()/*证件类型*/);
		collectionIndividualAccountBasicVice.setZhiYe(addIndiAcctAlter.getGRXX().getZhiYe()/*职业*/);
		collectionIndividualAccountBasicVice.setYzbm(addIndiAcctAlter.getGRXX().getYZBM()/*邮政编码*/);
		collectionIndividualAccountBasicVice.setZhiCheng(addIndiAcctAlter.getGRXX().getZhiCheng()/*职称*/);
		collectionIndividualAccountBasicVice.setSjhm(addIndiAcctAlter.getGRXX().getSJHM()/*手机号码*/);
		collectionIndividualAccountBasicVice.setZjhm(addIndiAcctAlter.getGRXX().getZJHM()/*证件号码*/);
		collectionIndividualAccountBasicVice.setGddhhm(addIndiAcctAlter.getGRXX().getGDDHHM()/*固定电话号码*/);
		collectionIndividualAccountBasicVice.setXmqp(addIndiAcctAlter.getGRXX().getXMQP()/*姓名全拼*/);
		collectionIndividualAccountBasicVice.setXingBie(addIndiAcctAlter.getGRXX().getXingBie()/*性别*/);
		collectionIndividualAccountBasicVice.setJtysr(StringUtil.safeBigDecimal(addIndiAcctAlter.getGRXX().getJTYSR()/*家庭月收入*/+""));
		collectionIndividualAccountBasicVice.setXueLi(addIndiAcctAlter.getGRXX().getXueLi()/*学历*/);
		collectionIndividualAccountBasicVice.setXingMing(addIndiAcctAlter.getGRXX().getXingMing()/*姓名*/);
		collectionIndividualAccountBasicVice.setZhiWu(addIndiAcctAlter.getGRXX().getZhiWu()/*职务*/);
		collectionIndividualAccountBasicVice.setDzyx(addIndiAcctAlter.getGRXX().getYouXiang()/*邮箱*/);

		// YWWD
		collectionIndividualAccountBasicVice.setYwwd(tokenContext.getUserInfo().getYWWD()/*业务网点*/);



		collectionPersonalBusinessDetailsExtension.setYwwd(network/*业务网点*/);

		// JBRZJHM
		collectionPersonalBusinessDetailsExtension.setJbrzjhm(addIndiAcctAlter.getJBRZJHM()/*经办人证件号码*/);

		// DWXX
		collectionIndividualAccountBasicVice.setDwzh(addIndiAcctAlter.getDWXX().getDWZH()/*单位账号*/);

		// CZY
		collectionIndividualAccountBasicVice.setCzy(tokenContext.getUserInfo().getCZY()/*操作员*/);
		collectionPersonalBusinessDetailsExtension.setCzy(tokenContext.getUserInfo().getCZY()/*操作员*/);

		//endregion

		//region //修改状态
		CCollectionIndividualAccountBasicVice savedVice = DAOBuilder.instance(this.collectionIndividualAccountBasicViceDAO).entity(collectionIndividualAccountBasicVice).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
			@Override
			public void error(Exception e) { throw new ErrorException(e); }
		});

		StateMachineUtils.updateState(this.stateMachineService, new HashMap<String, String>(){{

			this.put("0", Events.通过.getEvent());

			this.put("1",Events.提交.getEvent());

		}}.get(addIndiAcctAlter.getCZLX()),new TaskEntity() {{

			this.setStatus(savedVice.getGrywmx().getExtension().getStep()==null ? "初始状态":savedVice.getGrywmx().getExtension().getStep());
			this.setTaskId(savedVice.getGrywmx().getYwlsh());
			this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));
			this.setNote("");
			this.setSubtype(BusinessSubType.归集_个人账户信息变更.getSubType());
			this.setType(BusinessType.Collection);
			this.setOperator(savedVice.getCzy());
			this.setWorkstation(tokenContext.getUserInfo().getYWWD());

		}}, new StateMachineUtils.StateChangeHandler() {

			@Override
			public void onStateChange(boolean succeed, String next, Exception e) {

				if(e!=null){throw new ErrorException(e);}

				if(!succeed||next==null){ return;}

				savedVice.getGrywmx().getExtension().setStep(next);


				if(StringUtil.isIntoReview(next,null)){

				    savedVice.getGrywmx().getExtension().setDdsj(new Date());

				}

				DAOBuilder.instance(collectionIndividualAccountBasicViceDAO).entity(savedVice).save(new DAOBuilder.ErrorHandler() {

					@Override
					public void error(Exception e) { throw new ErrorException(e); }
				});

				if(next.equals(CollectionBusinessStatus.办结.getName())){

					doAcctAlter(tokenContext,savedVice.getGrywmx().getYwlsh());
				}
			}
		});
		//endregion

		//region //唯一性验证

		if("1".equals(addIndiAcctAlter.getCZLX())&&!DAOBuilder.instance(this.commonPersonDAO).extension(new IBaseDAO.CriteriaExtension() {
			@Override
			public void extend(Criteria criteria) {
				criteria.add(Restrictions.or(Restrictions.eq("zjhm",collectionIndividualAccountBasicVice.getZjhm()),Restrictions.eq("sjhm",collectionIndividualAccountBasicVice.getSjhm())));
				criteria.add(Restrictions.ne("grzh",commonPerson.getGrzh()));
				criteria.createAlias("collectionPersonalAccount","collectionPersonalAccount");
				criteria.add(Restrictions.not(Restrictions.in("collectionPersonalAccount.grzhzt",PersonAccountStatus.合并销户.getCode(),PersonAccountStatus.外部转出销户.getCode(),PersonAccountStatus.提取销户.getCode())));
			}
		}).isUnique(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e); }

		})){

			throw new ErrorException(ReturnEnumeration.Data_Already_Eeist,"手机号或证件号码");
		}

		if(addIndiAcctAlter.getCZLX().equals("1")&&DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>(){{

			this.put("collectionPersonalAccount.grckzhhm",collectionIndividualAccountBasicVice.getGrckzhhm());

		}}).extension(new IBaseDAO.CriteriaExtension() {
			@Override
			public void extend(Criteria criteria) {
				criteria.add(Restrictions.ne("grzh",commonPerson.getGrzh()));
				criteria.add(Restrictions.not(Restrictions.in("collectionPersonalAccount.grzhzt",PersonAccountStatus.合并销户.getCode(),PersonAccountStatus.外部转出销户.getCode(),PersonAccountStatus.提取销户.getCode())));
			}

		}).getObject(new DAOBuilder.ErrorHandler() {

			@Override

			public void error(Exception e) { throw new ErrorException(e);}
		})!=null) {

			throw new ErrorException(ReturnEnumeration.Data_Already_Eeist,"个人存款账户号码");
		}
		//endregion

		//region //在途验证
		if(addIndiAcctAlter.getCZLX().equals("1")){

			this.isCollectionIndividualAccountBasicViceAvailable(collectionIndividualAccountBasicVice);


		}

		if((addIndiAcctAlter.getCZLX().equals("1")&&!DAOBuilder.instance(collectionIndividualAccountBasicViceDAO).extension(new IBaseDAO.CriteriaExtension() {

			@Override
			public void extend(Criteria criteria) {

				criteria.add(Restrictions.not(Restrictions.in(CriteriaUtils.addAlias(criteria,"grywmx.cCollectionPersonalBusinessDetailsExtension.step"), (Collection)CollectionUtils.flatmap(CollectionUtils.merge(CollectionBusinessStatus.办结.getSubTypes(), CollectionBusinessStatus.新建.getSubTypes()), new CollectionUtils.Transformer<CollectionBusinessStatus, String>() {

					@Override
					public String tansform(CollectionBusinessStatus var1) { return var1.getName(); }

				}))));
				criteria.add(Restrictions.or(
						Restrictions.eq("zjhm", collectionIndividualAccountBasicVice.getZjhm()),
						Restrictions.eq("sjhm", collectionIndividualAccountBasicVice.getSjhm()),
						Restrictions.eq("grckzhhm", collectionIndividualAccountBasicVice.getGrckzhhm())
				));
				criteria.add(Restrictions.ne("grywmx.ywlsh",savedVice.getGrywmx().getYwlsh()));
				criteria.add(Restrictions.in("cCollectionPersonalBusinessDetailsExtension.czmc",CollectionBusinessType.变更.getCode(),CollectionBusinessType.开户.getCode()));

			}
		}).isUnique(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e);}

		}))){

			throw new ErrorException(ReturnEnumeration.Business_In_Process, "已有人正在使用相同的 证件号码 或 手机号码 或 个人存款账户号码 办理开户或变更业务");
		}
		this.iSaveAuditHistory.saveNormalBusiness(savedVice.getGrywmx().getYwlsh(),tokenContext, CollectionBusinessType.变更.getName(),"新建");
		//endregion

		return new AddIndiAcctAlterRes() {{

			this.setYWLSH(savedVice.getGrywmx().getYwlsh());

			this.setStatus("success");

		}};
	}

	// completed 已测试 - 正常xc
	@Override
	public ReIndiAcctAlterRes reAcctAlter(TokenContext tokenContext,String YWLSH, IndiAcctAlterPut reIndiAcctAlter) {

		//region //检查参数

		boolean allowNull = reIndiAcctAlter.getCZLX().equals("0");

		if(!DateUtil.isFollowFormat(reIndiAcctAlter.getGRZHXX().getGJJSCHJNY()/*公积金首次汇缴年月*/,format,allowNull)){throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"公积金首次汇缴年月");}

		if(!DateUtil.isFollowFormat(reIndiAcctAlter.getGRXX().getCSNY()/*出生年月*/,format,allowNull)){throw  new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"出生年月");}

		if(!StringUtil.isDigits(reIndiAcctAlter.getGRZHXX().getGRJCJS()+""/*个人缴存基数*/,allowNull)){throw  new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"个人缴存基数");}

		if(!StringUtil.isDigits(reIndiAcctAlter.getGRXX().getJTYSR()/*家庭月收入*/+"")){throw  new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"家庭月收入");}

		if(!DateUtil.isFollowFormat(reIndiAcctAlter.getGRZHXX().getGJJSCHJNY()/*公积金首次汇缴年月*/,format,allowNull)){throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"公积金首次汇缴年月");}

		if(!DateUtil.isFollowFormat(reIndiAcctAlter.getGRXX().getCSNY()/*出生年月*/,format,allowNull)){throw  new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"出生年月");}

		if(!StringUtil.isDigits(reIndiAcctAlter.getGRZHXX().getGRJCJS()+""/*个人缴存基数*/,allowNull)){throw  new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH,"个人缴存基数");}

		if (!StringUtil.notEmpty(reIndiAcctAlter.getGRZHXX().getGRCKZHHM()/*个人存款账户号码*/, allowNull)) { throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "个人存款账户号码"); }

		if (!StringUtil.notEmpty(reIndiAcctAlter.getJBRZJLX()/*经办人证件类型*/, allowNull)) { throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "经办人证件类型"); }

		if (!StringUtil.notEmpty(reIndiAcctAlter.getJBRXM()/*经办人姓名*/, allowNull)) { throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "经办人姓名"); }

		if (!StringUtil.notEmpty(reIndiAcctAlter.getGRXX().getHYZK()/*婚姻状况*/, allowNull)) { throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "婚姻状况"); }

		if (!StringUtil.notEmpty(reIndiAcctAlter.getGRXX().getZJLX()/*证件类型*/, allowNull)) { throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "证件类型"); }

		if (!StringUtil.notEmpty(reIndiAcctAlter.getGRXX().getSJHM()/*手机号码*/, allowNull)) { throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "手机号码"); }

		if (!StringUtil.notEmpty(reIndiAcctAlter.getGRXX().getZJHM()/*证件号码*/, allowNull)) { throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "证件号码"); }

		if (!StringUtil.notEmpty(reIndiAcctAlter.getGRXX().getXMQP()/*姓名全拼*/, allowNull)) { throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "姓名全拼"); }

		if (!StringUtil.notEmpty(reIndiAcctAlter.getGRXX().getXingBie()/*性别*/, allowNull)) { throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "性别"); }

		if (!StringUtil.notEmpty(reIndiAcctAlter.getGRXX().getXingMing()/*姓名*/, allowNull)) { throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "姓名"); }

		if (!StringUtil.notEmpty(reIndiAcctAlter.getJBRZJHM()/*经办人证件号码*/, allowNull)) { throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "经办人证件号码"); }

		if (!StringUtil.notEmpty(reIndiAcctAlter.getDWXX().getDWZH()/*单位账号*/, allowNull)) { throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "单位账号"); }

		//endregion

		//region //必要字段查询&完整性验证
		CCollectionIndividualAccountBasicVice collectionIndividualAccountBasicVice = DAOBuilder.instance(this.collectionIndividualAccountBasicViceDAO).searchFilter(new HashMap<String, Object>() {{

			this.put("grywmx.ywlsh", YWLSH);

		}}).getObject(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e); }

		});

		if (collectionIndividualAccountBasicVice == null || collectionIndividualAccountBasicVice.getGrywmx() == null||collectionIndividualAccountBasicVice.getGrywmx().getExtension()==null) {

			throw new ErrorException(ReturnEnumeration.Data_MISS,"业务记录");
		}

		if(!tokenContext.getUserInfo().getCZY().equals(collectionIndividualAccountBasicVice.getGrywmx().getExtension().getCzy())){

			throw new ErrorException(ReturnEnumeration.Permission_Denied);

		}
		StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = collectionIndividualAccountBasicVice.getGrywmx();

		CCollectionPersonalBusinessDetailsExtension collectionPersonalBusinessDetailsExtension = collectionPersonalBusinessDetails.getExtension();

		StCommonPerson commonPerson = collectionPersonalBusinessDetails.getPerson();

		if (commonPerson == null || commonPerson.getUnit() == null||commonPerson.getExtension() == null) {

			throw new ErrorException(ReturnEnumeration.Data_MISS,"个人信息");
		}
		if(UnitAccountStatus.封存.getCode().equals(commonPerson.getUnit().getCollectionUnitAccount().getDwzhzt())){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"单位已封存");
		}
		if("1".equals(reIndiAcctAlter.getCZLX())&&"02".equals(commonPerson.getExtension().getSfdj())){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"账号未解冻");
		}
		//endregion

		//region  //字段填充

		// GRJCXX

		collectionIndividualAccountBasicVice.setGjjschjny(DateUtil.safeStr2DBDate(format,reIndiAcctAlter.getGRZHXX().getGJJSCHJNY()/*公积金首次汇缴年月*/,DateUtil.dbformatYear_Month));
		collectionIndividualAccountBasicVice.setGrckzhhm(reIndiAcctAlter.getGRZHXX().getGRCKZHHM()/*个人存款账户号码*/);
		collectionIndividualAccountBasicVice.setGrjcjs(StringUtil.safeBigDecimal(reIndiAcctAlter.getGRZHXX().getGRJCJS()/*个人缴存基数*/+""));
		collectionIndividualAccountBasicVice.setGrckzhkhyhdm(reIndiAcctAlter.getGRZHXX().getGRCKZHKHYHDM()/*个人存款账户开户银行代码*/);
		collectionIndividualAccountBasicVice.setGrckzhkhyhmc(reIndiAcctAlter.getGRZHXX().getGRCKZHKHYHMC()/*个人存款账户开银行名称*/);


		// JBRZJLX
		collectionPersonalBusinessDetailsExtension.setJbrzjlx(reIndiAcctAlter.getJBRZJLX()/*经办人证件类型*/);

		// BLZL
		collectionIndividualAccountBasicVice.setBlzl(reIndiAcctAlter.getBLZL()/*办理资料*/);

		// JBRXM
		collectionPersonalBusinessDetailsExtension.setJbrxm(reIndiAcctAlter.getJBRXM()/*经办人姓名*/);

		// GRXX
		collectionIndividualAccountBasicVice.setHyzk(reIndiAcctAlter.getGRXX().getHYZK()/*婚姻状况*/);
		collectionIndividualAccountBasicVice.setJtzz(reIndiAcctAlter.getGRXX().getJTZZ()/*家庭住址*/);
		collectionIndividualAccountBasicVice.setCsny(DateUtil.safeStr2DBDate(format,reIndiAcctAlter.getGRXX().getCSNY()/*出生年月*/,DateUtil.dbformatYear_Month));
		collectionIndividualAccountBasicVice.setZjlx(reIndiAcctAlter.getGRXX().getZJLX()/*证件类型*/);
		collectionIndividualAccountBasicVice.setZhiYe(reIndiAcctAlter.getGRXX().getZhiYe()/*职业*/);
		collectionIndividualAccountBasicVice.setYzbm(reIndiAcctAlter.getGRXX().getYZBM()/*邮政编码*/);
		collectionIndividualAccountBasicVice.setZhiCheng(reIndiAcctAlter.getGRXX().getZhiCheng()/*职称*/);
		collectionIndividualAccountBasicVice.setSjhm(reIndiAcctAlter.getGRXX().getSJHM()/*手机号码*/);
		collectionIndividualAccountBasicVice.setZjhm(reIndiAcctAlter.getGRXX().getZJHM()/*证件号码*/);
		collectionIndividualAccountBasicVice.setGddhhm(reIndiAcctAlter.getGRXX().getGDDHHM()/*固定电话号码*/);
		collectionIndividualAccountBasicVice.setXmqp(reIndiAcctAlter.getGRXX().getXMQP()/*姓名全拼*/);
		collectionIndividualAccountBasicVice.setXingBie(reIndiAcctAlter.getGRXX().getXingBie()/*性别*/);
		collectionIndividualAccountBasicVice.setJtysr(StringUtil.safeBigDecimal(reIndiAcctAlter.getGRXX().getJTYSR()/*家庭月收入*/+""));
		collectionIndividualAccountBasicVice.setXueLi(reIndiAcctAlter.getGRXX().getXueLi()/*学历*/);
		collectionIndividualAccountBasicVice.setXingMing(reIndiAcctAlter.getGRXX().getXingMing()/*姓名*/);
		collectionIndividualAccountBasicVice.setZhiWu(reIndiAcctAlter.getGRXX().getZhiWu()/*职务*/);
		collectionIndividualAccountBasicVice.setDzyx(reIndiAcctAlter.getGRXX().getYouXiang()/*邮箱*/);

		// YWWD
		collectionIndividualAccountBasicVice.setYwwd(tokenContext.getUserInfo().getYWWD()/*业务网点*/);

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
		collectionPersonalBusinessDetailsExtension.setYwwd(network/*业务网点*/);

		// JBRZJHM
		collectionPersonalBusinessDetailsExtension.setJbrzjhm(reIndiAcctAlter.getJBRZJHM()/*经办人证件号码*/);

		// DWXX
		collectionIndividualAccountBasicVice.setDwzh(reIndiAcctAlter.getDWXX().getDWZH()/*单位账号*/);

		// CZY
		collectionPersonalBusinessDetailsExtension.setCzy(tokenContext.getUserInfo().getCZY()/*操作员*/);


		//endregion

		//region //修改状态
		StateMachineUtils.updateState(this.stateMachineService, new HashMap<String, String>(){{

			this.put("0", Events.保存.getEvent());

			this.put("1",Events.通过.getEvent());

		}}.get(reIndiAcctAlter.getCZLX()),new TaskEntity() {{

			this.setStatus(collectionIndividualAccountBasicVice.getGrywmx().getExtension().getStep()==null ? "初始状态":collectionIndividualAccountBasicVice.getGrywmx().getExtension().getStep());
			this.setTaskId(YWLSH);
			this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));;
			this.setNote("");
			this.setSubtype(BusinessSubType.归集_个人账户信息变更.getSubType());
			this.setType(BusinessType.Collection);
			this.setOperator(collectionIndividualAccountBasicVice.getCzy());
			this.setWorkstation(tokenContext.getUserInfo().getYWWD());

		}}, new StateMachineUtils.StateChangeHandler() {

			@Override
			public void onStateChange(boolean succeed, String next, Exception e) {

				if(e!=null){throw new ErrorException(e);}

				if(!succeed||next==null){ return;}

				collectionPersonalBusinessDetailsExtension.setStep(next);


				if(StringUtil.isIntoReview(next,null)){

				    collectionPersonalBusinessDetailsExtension.setDdsj(new Date());

				}

				DAOBuilder.instance(collectionIndividualAccountBasicViceDAO).entity(collectionIndividualAccountBasicVice).save(new DAOBuilder.ErrorHandler() {

					@Override
					public void error(Exception e) { throw new ErrorException(e); }
				});

				if(next.equals(CollectionBusinessStatus.办结.getName())){

					doAcctAlter(tokenContext,collectionIndividualAccountBasicVice.getGrywmx().getYwlsh());
				}
			}
		});

		//endregion

		//region //唯一性验证

		if("1".equals(reIndiAcctAlter.getCZLX())&&!DAOBuilder.instance(this.commonPersonDAO).extension(new IBaseDAO.CriteriaExtension() {
			@Override
			public void extend(Criteria criteria) {
				criteria.add(Restrictions.or(Restrictions.eq("zjhm",collectionIndividualAccountBasicVice.getZjhm()),Restrictions.eq("sjhm",collectionIndividualAccountBasicVice.getSjhm())));
				criteria.add(Restrictions.ne("grzh",commonPerson.getGrzh()));
				criteria.createAlias("collectionPersonalAccount","collectionPersonalAccount");
				criteria.add(Restrictions.not(Restrictions.in("collectionPersonalAccount.grzhzt",PersonAccountStatus.合并销户.getCode(),PersonAccountStatus.外部转出销户.getCode(),PersonAccountStatus.提取销户.getCode())));
			}
		}).isUnique(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e); }

		})){

			throw new ErrorException(ReturnEnumeration.Data_Already_Eeist,"手机号或证件号码");
		}

		if(reIndiAcctAlter.getCZLX().equals("1")&&DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>(){{

			this.put("collectionPersonalAccount.grckzhhm",collectionIndividualAccountBasicVice.getGrckzhhm());

		}}).extension(new IBaseDAO.CriteriaExtension() {
			@Override
			public void extend(Criteria criteria) {

				criteria.add(Restrictions.ne("grzh",commonPerson.getGrzh()));
				criteria.add(Restrictions.not(Restrictions.in("collectionPersonalAccount.grzhzt",PersonAccountStatus.合并销户.getCode(),PersonAccountStatus.外部转出销户.getCode(),PersonAccountStatus.提取销户.getCode())));
			}

		}).getObject(new DAOBuilder.ErrorHandler() {

			@Override

			public void error(Exception e) { throw new ErrorException(e);}
		})!=null) {

			throw new ErrorException(ReturnEnumeration.Data_Already_Eeist,"个人存款账户号码");
		}
		//endregion

		//region //在途验证

		if((reIndiAcctAlter.getCZLX().equals("1")&&!DAOBuilder.instance(collectionIndividualAccountBasicViceDAO).searchFilter(new HashMap<String, Object>(){{

			this.put("zjhm",reIndiAcctAlter.getGRXX().getZJHM());

		}}).extension(new IBaseDAO.CriteriaExtension() {
			@Override
			public void extend(Criteria criteria) {

				criteria.add(Restrictions.not(Restrictions.in(CriteriaUtils.addAlias(criteria,"grywmx.cCollectionPersonalBusinessDetailsExtension.step"),(Collection)CollectionUtils.flatmap(CollectionUtils.merge(CollectionBusinessStatus.办结.getSubTypes(), CollectionBusinessStatus.新建.getSubTypes()), new CollectionUtils.Transformer<CollectionBusinessStatus, String>() {

					@Override
					public String tansform(CollectionBusinessStatus var1) { return var1.getName(); }

				}))));
				criteria.add(Restrictions.or(
						Restrictions.eq("zjhm", collectionIndividualAccountBasicVice.getZjhm()),
						Restrictions.eq("sjhm", collectionIndividualAccountBasicVice.getSjhm()),
						Restrictions.eq("grckzhhm", collectionIndividualAccountBasicVice.getGrckzhhm())
				));
				criteria.add(Restrictions.in("cCollectionPersonalBusinessDetailsExtension.czmc",CollectionBusinessType.变更.getCode(),CollectionBusinessType.开户.getCode()));
				criteria.add(Restrictions.ne("grywmx.ywlsh", collectionIndividualAccountBasicVice.getGrywmx().getYwlsh()));
			}
		}).isUnique(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e);}

		}))){

			throw new ErrorException(ReturnEnumeration.Business_In_Process, "已有人正在使用相同的 证件号码 或 手机号码 或 个人存款账户号码 办理开户或变更业务");
		}

		if(reIndiAcctAlter.getCZLX().equals("1")){

			this.isCollectionIndividualAccountBasicViceAvailable(collectionIndividualAccountBasicVice);

			this.iSaveAuditHistory.saveNormalBusiness(YWLSH,tokenContext, CollectionBusinessType.变更.getName(),"修改");
		}
		//endregion

		return new ReIndiAcctAlterRes() {{

			this.setYWLSH(YWLSH);

			this.setStatus("success");

		}};
	}

	@Override
	public CommonResponses doMerge(TokenContext tokenContext,String YZJHM,String XZJHM,String XINGMING,String GRCKZHHM){

		//region //必要字段查询&正确性验证
		List<StCommonPerson> list_person_original = DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>(){{

			this.put("zjhm",YZJHM);

		}}).extension(new IBaseDAO.CriteriaExtension() {
			@Override
			public void extend(Criteria criteria) {

				criteria.createAlias("collectionPersonalAccount","collectionPersonalAccount");

				criteria.add(Restrictions.isNull("collectionPersonalAccount.xhrq"));
			}
		}).getList(new DAOBuilder.ErrorHandler() {
			@Override
			public void error(Exception e) { throw new ErrorException(e);}
		});

		if(list_person_original.size() >1){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"存在多个账号 无法合并");
		}

		StCommonPerson commonPerson_original = CollectionUtils.getFirst(list_person_original);

		if(commonPerson_original==null||commonPerson_original.getCollectionPersonalAccount()==null){

			throw new ErrorException(ReturnEnumeration.Data_MISS,"原账号信息");
		}

		if(!PersonAccountStatus.封存.getCode().equals(commonPerson_original.getCollectionPersonalAccount().getGrzhzt())){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"原账号未封存 不能合并");
		}

		List<StCommonPerson> list_person_current = DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>(){{

			this.put("zjhm",XZJHM);

		}}).extension(new IBaseDAO.CriteriaExtension() {
			@Override
			public void extend(Criteria criteria) {

				criteria.createAlias("collectionPersonalAccount","collectionPersonalAccount");

				criteria.add(Restrictions.isNull("collectionPersonalAccount.xhrq"));
			}
		}).getList(new DAOBuilder.ErrorHandler() {
			@Override
			public void error(Exception e) { throw new ErrorException(e);}
		});

		if(list_person_current.size() >1){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"存在多个账号 无法合并");
		}

		StCommonPerson commonPerson_current = CollectionUtils.getFirst(list_person_current);

		if(commonPerson_current==null||commonPerson_current.getCollectionPersonalAccount()==null){

			throw new ErrorException(ReturnEnumeration.Data_MISS,"新账号信息");
		}
		if(!commonPerson_current.getXingMing().equals(XINGMING)||!commonPerson_current.getCollectionPersonalAccount().getGrckzhhm().equals(GRCKZHHM)){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"姓名或个人存款账户号码不一致");
		}

		CAccountNetwork YWWD = DAOBuilder.instance(this.cAccountNetworkDAO).searchFilter(new HashMap<String, Object>() {{

			this.put("id",tokenContext.getUserInfo().getYWWD());

		}}).getObject(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e); }
		});

		//endregion

		//region //在途验证
		if(!DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).extension(new IBaseDAO.CriteriaExtension() {
			@Override
			public void extend(Criteria criteria) {
				criteria.add(Restrictions.eq("grzh",commonPerson_current.getGrzh()));
				criteria.createAlias("cCollectionPersonalBusinessDetailsExtension","cCollectionPersonalBusinessDetailsExtension");
				criteria.add(Restrictions.in("cCollectionPersonalBusinessDetailsExtension.czmc", (Collection) Arrays.asList(CollectionBusinessType.部分提取.getCode(),CollectionBusinessType.销户提取.getCode())));
				criteria.add(Restrictions.not(Restrictions.in("cCollectionPersonalBusinessDetailsExtension.step",CollectionBusinessStatus.已入账.getName(),CollectionBusinessStatus.已作废.getName())));
			}
		}).isUnique(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e); }

		})){

			throw new ErrorException(ReturnEnumeration.Business_In_Process,"证件号码"+commonPerson_current.getZjhm()+"正在办理提取业务");
		}

		if(!DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).extension(new IBaseDAO.CriteriaExtension() {
			@Override
			public void extend(Criteria criteria) {
				criteria.add(Restrictions.eq("grzh",commonPerson_original.getGrzh()));
				criteria.createAlias("cCollectionPersonalBusinessDetailsExtension","cCollectionPersonalBusinessDetailsExtension");
				criteria.add(Restrictions.in("cCollectionPersonalBusinessDetailsExtension.czmc", (Collection) Arrays.asList(CollectionBusinessType.部分提取.getCode(),CollectionBusinessType.销户提取.getCode())));
				criteria.add(Restrictions.not(Restrictions.in("cCollectionPersonalBusinessDetailsExtension.step",CollectionBusinessStatus.已入账.getName(),CollectionBusinessStatus.已作废.getName())));
			}
		}).isUnique(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e); }

		})){

			throw new ErrorException(ReturnEnumeration.Business_In_Process,"证件号码"+commonPerson_original.getZjhm()+"正在办理提取业务");
		}
		//endregion

		//region //必要字段声明&关系配置
		StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = new StCollectionPersonalBusinessDetails();

		CCollectionPersonalBusinessDetailsExtension collectionPersonalBusinessDetailsExtension = new CCollectionPersonalBusinessDetailsExtension();
		collectionPersonalBusinessDetails.setExtension(collectionPersonalBusinessDetailsExtension);
		collectionPersonalBusinessDetails.setPerson(commonPerson_current);

		StCommonUnit unit_original = commonPerson_original.getUnit();

		StCommonUnit unit_current  = commonPerson_current.getUnit();

		StCollectionPersonalAccount personAccount_original = commonPerson_original.getCollectionPersonalAccount();

		CCollectionIndividualAccountTransferNewVice collectionIndividualAccountTransferNewVice = new CCollectionIndividualAccountTransferNewVice();
		collectionPersonalBusinessDetails.setIndividualAccountTransferNewVice(collectionIndividualAccountTransferNewVice);
		collectionIndividualAccountTransferNewVice.setGrywmx(collectionPersonalBusinessDetails);



		//endregion

		//region //转移业务
		collectionPersonalBusinessDetails.setFse(BigDecimal.ZERO);
		collectionPersonalBusinessDetails.setGjhtqywlx(CollectionBusinessType.内部转移.getCode());
		collectionPersonalBusinessDetails.setGrzh(commonPerson_current.getGrzh());
		collectionPersonalBusinessDetails.setCzbz(CommonFieldType.非冲账.getCode());
		collectionPersonalBusinessDetailsExtension.setStep(CollectionBusinessStatus.办结.getName());
		collectionPersonalBusinessDetailsExtension.setBjsj(new Date());
		collectionPersonalBusinessDetailsExtension.setSlsj(new Date());
		collectionPersonalBusinessDetailsExtension.setCzy(tokenContext.getUserInfo().getCZY());
		collectionPersonalBusinessDetailsExtension.setYwwd(YWWD);
		collectionPersonalBusinessDetailsExtension.setCzmc(CollectionBusinessType.合并.getCode());
		collectionPersonalBusinessDetailsExtension.setDqye(commonPerson_current.getCollectionPersonalAccount().getGrzhye().add(personAccount_original.getGrzhye()));

		StCollectionPersonalBusinessDetails savedBusiness = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).entity(collectionPersonalBusinessDetails).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
			@Override
			public void error(Exception e) { throw new ErrorException(e);}
		});





		collectionIndividualAccountTransferNewVice.setGrzh(commonPerson_current.getGrzh());  //职工个人账号
		collectionIndividualAccountTransferNewVice.setZcdw(unit_original/*转出单位账号*/);
		collectionIndividualAccountTransferNewVice.setZrdw(unit_current);  //转入单位账号
		collectionIndividualAccountTransferNewVice.setYgrjcjs(personAccount_original.getGrjcjs()+""/*原单位下个人月缴存基数*/);
		collectionIndividualAccountTransferNewVice.setYdwyjce(personAccount_original.getDwyjce()+""/*原单位下单位月缴存额*/);
		collectionIndividualAccountTransferNewVice.setYgryjce(personAccount_original.getGryjce()+""/*原单位下个人月缴存额*/);
		collectionIndividualAccountTransferNewVice.setZysgrzhye(personAccount_original.getGrzhye()+""/*转移时个人账户余额*/);
		collectionIndividualAccountTransferNewVice.setZysdngjye(personAccount_original.getGrzhdngjye()+""/*转移时个人账户当年归集余额*/);
		collectionIndividualAccountTransferNewVice.setZyssnjzye(personAccount_original.getGrzhsnjzye()/*转移时个人账户上年结转余额）*/);

		//endregion

        //region //原单位业务
		StCollectionUnitBusinessDetails collectionUnitBusinessDetails_original = new StCollectionUnitBusinessDetails();

		CCollectionUnitBusinessDetailsExtension collectionUnitBusinessDetailsExtension_original = new CCollectionUnitBusinessDetailsExtension();
		collectionUnitBusinessDetails_original.setExtension(collectionUnitBusinessDetailsExtension_original);

		collectionUnitBusinessDetails_original.setDwzh(unit_original.getDwzh());
		collectionUnitBusinessDetails_original.setUnit(unit_original);
		collectionUnitBusinessDetails_original.setYwmxlx(CollectionBusinessType.内部转移.getCode());
		collectionUnitBusinessDetails_original.setFse(personAccount_original.getGrzhye().multiply(new BigDecimal("-1")));
		collectionUnitBusinessDetails_original.setFslxe(BigDecimal.ZERO);
		collectionUnitBusinessDetails_original.setCzbz(CommonFieldType.非冲账.getCode());
		collectionUnitBusinessDetails_original.setJzrq(new Date());


		collectionUnitBusinessDetailsExtension_original.setBjsj(new Date());
		collectionUnitBusinessDetailsExtension_original.setStep(CollectionBusinessStatus.办结.getName());
		collectionUnitBusinessDetailsExtension_original.setCzmc(CollectionBusinessType.合并.getCode());
		collectionUnitBusinessDetailsExtension_original.setBeizhu("转移合户-"+commonPerson_original.getGrzh());
		collectionUnitBusinessDetailsExtension_original.setSlsj(new Date());
		collectionUnitBusinessDetailsExtension_original.setCzy(tokenContext.getUserInfo().getCZY());
		collectionUnitBusinessDetailsExtension_original.setYwwd(YWWD);

		unit_original.getCollectionUnitAccount().setDwzhye(unit_original.getCollectionUnitAccount().getDwzhye().add(collectionUnitBusinessDetails_original.getFse()));

		DAOBuilder.instance(this.collectionUnitBusinessDetailsDAO).entity(collectionUnitBusinessDetails_original).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
			@Override
			public void error(Exception e) { throw new ErrorException(e); }
		});

		//endregion

		//region //现单位业务
		StCollectionUnitBusinessDetails collectionUnitBusinessDetails_current = new StCollectionUnitBusinessDetails();

		CCollectionUnitBusinessDetailsExtension collectionUnitBusinessDetailsExtension_current = new CCollectionUnitBusinessDetailsExtension();
		collectionUnitBusinessDetails_current.setExtension(collectionUnitBusinessDetailsExtension_current);

		collectionUnitBusinessDetails_current.setDwzh(unit_current.getDwzh());
		collectionUnitBusinessDetails_current.setUnit(unit_current);
		collectionUnitBusinessDetails_current.setYwmxlx(CollectionBusinessType.内部转移.getCode());
		collectionUnitBusinessDetails_current.setFse(personAccount_original.getGrzhye());
		collectionUnitBusinessDetails_current.setFslxe(BigDecimal.ZERO);
		collectionUnitBusinessDetails_current.setCzbz(CommonFieldType.非冲账.getCode());
		collectionUnitBusinessDetails_current.setJzrq(new Date());


		collectionUnitBusinessDetailsExtension_current.setBjsj(new Date());
		collectionUnitBusinessDetailsExtension_current.setCzmc(CollectionBusinessType.合并.getCode());
		collectionUnitBusinessDetailsExtension_current.setStep(CollectionBusinessStatus.办结.getName());
		collectionUnitBusinessDetailsExtension_current.setBeizhu("转移合户-"+commonPerson_current.getGrzh());
		collectionUnitBusinessDetailsExtension_current.setSlsj(new Date());
		collectionUnitBusinessDetailsExtension_current.setCzy(tokenContext.getUserInfo().getCZY());
		collectionUnitBusinessDetailsExtension_current.setYwwd(YWWD);

		unit_current.getCollectionUnitAccount().setDwzhye(unit_current.getCollectionUnitAccount().getDwzhye().add(collectionUnitBusinessDetails_current.getFse()));

		DAOBuilder.instance(this.collectionUnitBusinessDetailsDAO).entity(collectionUnitBusinessDetails_current).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
			@Override
			public void error(Exception e) { throw new ErrorException(e); }
		});

		//endregion

		//region //余额转移
		StCollectionPersonalAccount personAccount_current = commonPerson_current.getCollectionPersonalAccount();

		personAccount_current.setGrzhye(personAccount_current.getGrzhye().add(personAccount_original.getGrzhye()));

		commonPersonDAO.update(commonPerson_current);
		personAccount_original.setGrzhye(BigDecimal.ZERO);
		personAccount_original.setGrzhzt(PersonAccountStatus.合并销户.getCode());
		personAccount_original.setXhrq(new Date());
		personAccount_original.setXhyy(CollectionBusinessType.合并.getCode());
		commonPerson_original.setZjhm(commonPerson_current.getZjhm());
		commonPerson_original.setXingMing(commonPerson_current.getXingMing());
		personAccount_original.setGrckzhhm(commonPerson_current.getCollectionPersonalAccount().getGrckzhhm());
		commonPersonDAO.update(commonPerson_original);

		//endregion

		//region //更新贷款

		//region //个人信息附表-共同借款人附表-合同表
		List<CLoanHousingBusinessProcess> list_process = DAOBuilder.instance(this.loanHousingBusinessProcessDAO).searchOption(SearchOption.FUZZY).extension(new IBaseDAO.CriteriaExtension() {

			@Override
			public void extend(Criteria criteria) {

				criteria.createAlias("loanHousingPersonInformationVice","loanHousingPersonInformationVice");
				criteria.createAlias("loanContract","loanContract",JoinType.LEFT_OUTER_JOIN);
				criteria.createAlias("loanHousingCoborrowerVice","loanHousingCoborrowerVice",JoinType.LEFT_OUTER_JOIN);

				criteria.add(Restrictions.or(
						Restrictions.eq("loanHousingPersonInformationVice.jkrgjjzh",commonPerson_original.getGrzh()),
						Restrictions.and(Restrictions.isNotNull("loanContract"),Restrictions.eq("loanContract.jkrgjjzh",commonPerson_original.getGrzh())),
						Restrictions.and(Restrictions.isNotNull("loanHousingCoborrowerVice"),Restrictions.eq("loanHousingCoborrowerVice.gtjkrgjjzh",commonPerson_original.getGrzh()))
				));
			}
		}).getList(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e); }

		});

		list_process.forEach(new Consumer<CLoanHousingBusinessProcess>() {
			@Override
			public void accept(CLoanHousingBusinessProcess housingBusinessProcess) {

				if(housingBusinessProcess.getLoanHousingPersonInformationVice()!=null){

					if(commonPerson_original.getGrzh().equals(housingBusinessProcess.getLoanHousingPersonInformationVice().getJkrgjjzh())){

						housingBusinessProcess.getLoanHousingPersonInformationVice().setJkrxm(commonPerson_current.getXingMing());
						housingBusinessProcess.getLoanHousingPersonInformationVice().setJkrzjhm(commonPerson_current.getZjhm());
						housingBusinessProcess.getLoanHousingPersonInformationVice().setJkrgjjzh(commonPerson_current.getGrzh());
					}
				}

				if(housingBusinessProcess.getLoanContract()!=null){

					if(commonPerson_current.getGrzh().equals(housingBusinessProcess.getLoanContract().getJkrgjjzh())){

						housingBusinessProcess.getLoanContract().setJkrxm(commonPerson_current.getXingMing());
						housingBusinessProcess.getLoanContract().setJkrzjh(commonPerson_current.getZjhm());
						housingBusinessProcess.getLoanContract().setJkrgjjzh(commonPerson_current.getGrzh());
					}
				}

				if(housingBusinessProcess.getLoanHousingCoborrowerVice()!=null){

					if(commonPerson_current.getGrzh().equals(housingBusinessProcess.getLoanHousingCoborrowerVice().getGtjkrgjjzh())){

						housingBusinessProcess.getLoanHousingCoborrowerVice().setGtjkrxm(commonPerson_current.getXingMing());
						housingBusinessProcess.getLoanHousingCoborrowerVice().setGtjkrzjhm(commonPerson_current.getZjhm());
						housingBusinessProcess.getLoanHousingCoborrowerVice().setGtjkrgjjzh(commonPerson_current.getGrzh());
					}
				}
			}
		});

		DAOBuilder.instance(this.loanHousingBusinessProcessDAO).entities(list_process).save(new DAOBuilder.ErrorHandler() {
			@Override
			public void error(Exception e) { throw new ErrorException(e);}
		});

		//endregion

		//region //个人信息附表-共同借款人附表-合同表
		List<CLoanHousingPersonInformationBasic> list_basic = DAOBuilder.instance(this.loanHousingPersonInformationBasicDAO).searchOption(SearchOption.FUZZY).extension(new IBaseDAO.CriteriaExtension() {

			@Override
			public void extend(Criteria criteria) {

				criteria.createAlias("coborrower","coborrower",JoinType.LEFT_OUTER_JOIN);

				criteria.add(Restrictions.or(
						Restrictions.eq("jkrgjjzh",commonPerson_original.getGrzh()),
						Restrictions.and(Restrictions.isNotNull("coborrower"),Restrictions.eq("coborrower.gtjkrgjjzh",commonPerson_original.getGrzh()))
				));
			}
		}).getList(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e); }

		});

		list_basic.forEach(new Consumer<CLoanHousingPersonInformationBasic>() {

			@Override
			public void accept(CLoanHousingPersonInformationBasic loanHousingPersonInformationBasic) {


				if(commonPerson_original.getGrzh().equals(loanHousingPersonInformationBasic.getJkrgjjzh())){

					loanHousingPersonInformationBasic.setJkrxm(commonPerson_current.getXingMing());
					loanHousingPersonInformationBasic.setJkrzjhm(commonPerson_current.getZjhm());
					loanHousingPersonInformationBasic.setJkrgjjzh(commonPerson_current.getGrzh());
				}

				if(loanHousingPersonInformationBasic.getCoborrower()!=null){

					if(commonPerson_original.getGrzh().equals(loanHousingPersonInformationBasic.getCoborrower().getGtjkrgjjzh())){

						loanHousingPersonInformationBasic.getCoborrower().setGtjkrxm(commonPerson_current.getXingMing());
						loanHousingPersonInformationBasic.getCoborrower().setGtjkrzjhm(commonPerson_current.getZjhm());
						loanHousingPersonInformationBasic.getCoborrower().setGtjkrgjjzh(commonPerson_current.getGrzh());
					}
				}
			}
		});

		DAOBuilder.instance(this.loanHousingPersonInformationBasicDAO).entities(list_basic).save(new DAOBuilder.ErrorHandler() {
			@Override
			public void error(Exception e) { throw new ErrorException(e);}
		});
		//endregion

		//endregion

		//region //业务迁移
		this.collectionPersonalBusinessDetailsDAO.updateGRZH(commonPerson_current.getGrzh(),commonPerson_original.getGrzh(),DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>(){{

			this.put("grzh",commonPerson_current.getGrzh());

		}}).getObject(new DAOBuilder.ErrorHandler() {
			@Override
			public void error(Exception e) { throw new ErrorException(e);}
		}));

		//endregion

		//region//平账
		region(commonPerson_current.getGrzh());

		//endregion

		//region //财务入账

		this.voucherManagerService.addVoucher("毕节市住房公积金管理中心",tokenContext.getUserInfo().getCZY(),tokenContext.getUserInfo().getCZY(),"","",
				VoucherBusinessType.转移合户.getCode(),
				VoucherBusinessType.转移合户.getCode(), savedBusiness.getYwlsh(),unit_original.getDwmc()+commonPerson_original.getXingMing() + "合并到"+ unit_current.getDwmc() + commonPerson_current.getXingMing(),
				new BigDecimal(collectionIndividualAccountTransferNewVice.getZysgrzhye()),null,null);

		//endregion


		this.iSaveAuditHistory.saveNormalBusiness(savedBusiness.getYwlsh(),tokenContext, CollectionBusinessType.内部转移.getName(),"办结");
		return new CommonResponses(){{

			this.setId(savedBusiness.getYwlsh());
			this.setState("success");
		}};
	}

	/**
	 * 获取该人的业务明细信息
	 * 重新计算明细的当前余额
	 * 更新
	 * @param grzh
	 */
	private void region(String grzh) {
		List<StCollectionPersonalBusinessDetails> personBus = DAOBuilder.instance(this.collectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>(){{
			this.put("grzh",grzh);
			this.put("cCollectionPersonalBusinessDetailsExtension.czmc",(Collection)Arrays.asList(
					CollectionBusinessType.汇缴.getCode(),CollectionBusinessType.补缴.getCode(),CollectionBusinessType.年终结息.getCode(),
					CollectionBusinessType.部分提取.getCode(),CollectionBusinessType.销户提取.getCode(),CollectionBusinessType.错缴更正.getCode(),
					CollectionBusinessType.结息.getCode()));

		}}).orderOption("created_at", Order.DESC).getList(new DAOBuilder.ErrorHandler() {
			@Override
			public void error(Exception e) { throw new ErrorException(e);}
		});

		StCommonPerson person = commonPersonDAO.getByGrzh(grzh);
		BigDecimal grzhye = person.getCollectionPersonalAccount().getGrzhye();

		if(personBus.size() != 0 && grzhye.compareTo(personBus.get(0).getExtension().getDqye()) != 0){
			BigDecimal dqye = grzhye;
			BigDecimal fse = BigDecimal.ZERO;

			for(int i = 0;i < personBus.size();i++){
				String id = personBus.get(i).getExtension().getId();	//拓展表的id
				dqye = dqye.subtract(fse);
				collectionPersonalBusinessDetailsDAO.updateDQYE(id,dqye);
				fse = personBus.get(i).getFse();
			}
		}
	}

	// completed 已测试 - 正常
	@Override
	public GetIndiAcctAlterRes showAcctAlter(TokenContext tokenContext,String YWLSH) {


		//region // 检查参数

		if (YWLSH == null) { throw new ErrorException(ReturnEnumeration.Parameter_MISS,"业务流水号"); }

		//endregion

		//region // 必要参数查询
		CCollectionIndividualAccountBasicVice collectionIndividualAccountBasicVice = DAOBuilder.instance(this.collectionIndividualAccountBasicViceDAO).searchFilter(new HashMap<String, Object>() {{

			this.put("grywmx.ywlsh", YWLSH);

			this.put("grywmx.cCollectionPersonalBusinessDetailsExtension.czmc", CollectionBusinessType.变更.getCode());

		}}).getObject(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e); }
		});

		if (collectionIndividualAccountBasicVice == null || collectionIndividualAccountBasicVice.getGrywmx() == null) {

			throw new ErrorException(ReturnEnumeration.Data_MISS,"业务记录");
		}

		// 对应个人账户
		StCommonPerson commonPerson = DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>() {{

			this.put("grzh", collectionIndividualAccountBasicVice.getGrzh());

		}}).getObject(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e); }
		});

		if (commonPerson == null || commonPerson.getExtension() == null) {

			throw new ErrorException(ReturnEnumeration.Data_MISS,"个人信息");
		}

		StCommonUnit commonUnit = DAOBuilder.instance(this.commonUnitDAO).searchFilter(new HashMap<String, Object>() {{

			this.put("dwzh", collectionIndividualAccountBasicVice.getDwzh());

		}}).getObject(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e); }
		});

		//endregion

		return new GetIndiAcctAlterRes() {{

			// 构建返回值
			this.setBLZL(collectionIndividualAccountBasicVice.getBlzl());

			if (commonUnit != null) {

				this.setJBRZJLX(commonUnit.getJbrzjlx());

				this.setJBRXM(commonUnit.getJbrxm());

			}
			this.setGRXX(new GetIndiAcctAlterResGRXX() {{

				this.setHYZK(collectionIndividualAccountBasicVice.getHyzk());

				this.setJTZZ(collectionIndividualAccountBasicVice.getJtzz());

				this.setCSNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month,collectionIndividualAccountBasicVice.getCsny(), format));

				this.setZJLX(collectionIndividualAccountBasicVice.getZjlx());

				this.setZhiYe(collectionIndividualAccountBasicVice.getZhiYe());

				this.setYZBM(collectionIndividualAccountBasicVice.getYzbm());

				this.setZhiCheng(collectionIndividualAccountBasicVice.getZhiCheng());

				this.setSJHM(collectionIndividualAccountBasicVice.getSjhm());

				this.setZJHM(collectionIndividualAccountBasicVice.getZjhm());

				this.setGDDHHM(collectionIndividualAccountBasicVice.getGddhhm());

				this.setXMQP(collectionIndividualAccountBasicVice.getXmqp());

				this.setXingBie(collectionIndividualAccountBasicVice.getXingBie());

				this.setJTYSR(collectionIndividualAccountBasicVice.getJtysr() + "");

				this.setXueLi(collectionIndividualAccountBasicVice.getXueLi());

				this.setXingMing(collectionIndividualAccountBasicVice.getXingMing());

				this.setZhiWu(collectionIndividualAccountBasicVice.getZhiWu());

				this.setYouXiang(collectionIndividualAccountBasicVice.getDzyx());

				this.setGRCKZHHM(collectionIndividualAccountBasicVice.getGrckzhhm());

				this.setGRCKZHKHHDM(collectionIndividualAccountBasicVice.getGrckzhkhyhdm());

				this.setGRCKZHKHHMC(collectionIndividualAccountBasicVice.getGrckzhkhyhmc());
			}});

			this.setYWWD(collectionIndividualAccountBasicVice.getGrywmx().getExtension().getYwwd().getMingCheng());

			if (commonUnit != null) {

				this.setJBRZJHM(commonUnit.getJbrzjhm());
			}

			this.setDWXX(new GetIndiAcctAlterResDWXX() {{


				this.setYWLSH(collectionIndividualAccountBasicVice.getGrywmx().getYwlsh());

				this.setDWZH(collectionIndividualAccountBasicVice.getDwzh());

				if (commonUnit != null && commonUnit.getCollectionUnitAccount()!=null) {

					this.setDWMC(commonUnit.getDwmc());

					this.setJZNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month,commonUnit.getCollectionUnitAccount().getJzny(), formatNY));
				}
			}});

			this.setGRZHXX(new GetIndiAcctAlterResGRZHXX() {{

				this.setGRJCJS(commonPerson.getCollectionPersonalAccount().getGrjcjs().setScale(2,BigDecimal.ROUND_HALF_UP).toString());

				this.setGJJSCHJNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month,commonPerson.getExtension().getGjjschjny(), format));

				this.setGRCKZHKHYHMC(collectionIndividualAccountBasicVice.getGrckzhkhyhmc());

				this.setGRCKZHKHYHDM(collectionIndividualAccountBasicVice.getGrckzhkhyhdm());

				this.setGRZH(collectionIndividualAccountBasicVice.getGrzh());

				if (commonUnit != null && commonUnit.getCollectionUnitAccount() != null&&collectionIndividualAccountBasicVice.getGrjcjs()!=null&&commonUnit.getCollectionUnitAccount().getDwjcbl()!=null&&commonUnit.getCollectionUnitAccount().getGrjcbl()!=null) {

					this.setGRJCBL((commonUnit.getCollectionUnitAccount().getGrjcbl()==null? 0 :commonUnit.getCollectionUnitAccount().getGrjcbl().multiply(new BigDecimal("100")))+"");

					this.setGRYJCE(""+(collectionIndividualAccountBasicVice.getGrjcjs().floatValue() * (commonUnit.getCollectionUnitAccount().getGrjcbl().floatValue())));

					this.setDWJCBL(""+(commonUnit.getCollectionUnitAccount().getDwjcbl()==null? 0:commonUnit.getCollectionUnitAccount().getDwjcbl().multiply(new BigDecimal("100"))));

					this.setDWYJCE(""+(collectionIndividualAccountBasicVice.getGrjcjs().doubleValue() * (commonUnit.getCollectionUnitAccount().getDwjcbl().doubleValue())));

					this.setYJCE(""+(collectionIndividualAccountBasicVice.getGrjcjs().doubleValue() * (commonUnit.getCollectionUnitAccount().getDwjcbl().doubleValue()) + collectionIndividualAccountBasicVice.getGrjcjs().doubleValue() * (commonUnit.getCollectionUnitAccount().getGrjcbl().doubleValue())));
				}

				if (commonPerson.getCollectionPersonalAccount() != null) {

					this.setKHRQ(DateUtil.date2Str(commonPerson.getCollectionPersonalAccount().getKhrq(), format));

					this.setGRZHZT(commonPerson.getCollectionPersonalAccount().getGrzhzt());
				}

				this.setGRCKZHKHYHDM(collectionIndividualAccountBasicVice.getGrckzhkhyhdm());

				this.setGRCKZHHM(collectionIndividualAccountBasicVice.getGrckzhhm());

			}});


			this.setCZY(collectionIndividualAccountBasicVice.getGrywmx().getExtension().getCzy());

			this.setDelta(new ArrayList<String>(){{
                                
				if(!IndiAcctAlterImpl.isEqualTo(commonPerson.getHyzk(),collectionIndividualAccountBasicVice.getHyzk())){this.add("HYZK");}

				if(!IndiAcctAlterImpl.isEqualTo(commonPerson.getJtzz(),collectionIndividualAccountBasicVice.getJtzz())){this.add("JTZZ");}

				if(!IndiAcctAlterImpl.isEqualTo(commonPerson.getCsny(),collectionIndividualAccountBasicVice.getCsny())){this.add("CSNY");}

				if(!IndiAcctAlterImpl.isEqualTo(commonPerson.getZjlx(),collectionIndividualAccountBasicVice.getZjlx())){this.add("ZJLX");}

				if(!IndiAcctAlterImpl.isEqualTo(commonPerson.getZhiYe(),collectionIndividualAccountBasicVice.getZhiYe())){this.add("ZhiYe");}

				if(!IndiAcctAlterImpl.isEqualTo(commonPerson.getYzbm(),collectionIndividualAccountBasicVice.getYzbm())){this.add("YZBM");}

				if(!IndiAcctAlterImpl.isEqualTo(commonPerson.getZhiChen(),collectionIndividualAccountBasicVice.getZhiCheng())){this.add("ZhiCheng");}

				if(!IndiAcctAlterImpl.isEqualTo(commonPerson.getSjhm(),collectionIndividualAccountBasicVice.getSjhm())){this.add("SJHM");}

				if(!IndiAcctAlterImpl.isEqualTo(commonPerson.getZjhm(),collectionIndividualAccountBasicVice.getZjhm())){this.add("ZJHM");}

				if(!IndiAcctAlterImpl.isEqualTo(commonPerson.getGddhhm(),collectionIndividualAccountBasicVice.getGddhhm())){this.add("GDDHHM");}

				if(!IndiAcctAlterImpl.isEqualTo(commonPerson.getXmqp(),collectionIndividualAccountBasicVice.getXmqp())){this.add("XMQP");}

				if(!IndiAcctAlterImpl.isEqualTo(commonPerson.getXingBie()+"",collectionIndividualAccountBasicVice.getXingBie())){this.add("XingBie");}

				if(!IndiAcctAlterImpl.isEqualTo(commonPerson.getJtysr(),collectionIndividualAccountBasicVice.getJtysr())){this.add("JTYSR");}

				if(!IndiAcctAlterImpl.isEqualTo(commonPerson.getXueLi(),collectionIndividualAccountBasicVice.getXueLi())){this.add("XueLi");}

				if(!IndiAcctAlterImpl.isEqualTo(commonPerson.getXingMing(),collectionIndividualAccountBasicVice.getXingMing())){this.add("XingMing");}

				if(!IndiAcctAlterImpl.isEqualTo(commonPerson.getZhiWu(),collectionIndividualAccountBasicVice.getZhiWu())){this.add("ZhiWu");}

				if(!IndiAcctAlterImpl.isEqualTo(commonPerson.getExtension().getDzyx(),collectionIndividualAccountBasicVice.getDzyx())){this.add("YouXiang");}

				if(!IndiAcctAlterImpl.isEqualTo(commonPerson.getCollectionPersonalAccount().getGrckzhhm(),collectionIndividualAccountBasicVice.getGrckzhhm())){this.add("GRCKZHHM");}

				if(!IndiAcctAlterImpl.isEqualTo(commonPerson.getCollectionPersonalAccount().getGrckzhkhyhdm(),collectionIndividualAccountBasicVice.getGrckzhkhyhdm())){this.add("GRCKZHKHYHDM");}

				if(!IndiAcctAlterImpl.isEqualTo(commonPerson.getCollectionPersonalAccount().getGrckzhkhyhmc(),collectionIndividualAccountBasicVice.getGrckzhkhyhmc())){this.add("GRCKZHKHYHMC");}
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
	
	// completed 测试 - xc
	@Override
	public CommonResponses headAcctAlter(TokenContext tokenContext, String YWLSH) {

		//region // 检查参数

		if (YWLSH == null) { throw new ErrorException(ReturnEnumeration.Parameter_MISS,"业务流水号"); }

		//endregion

		//region // 必要字段查询&完整性验证
		CCollectionIndividualAccountBasicVice collectionIndividualAccountBasicVice = DAOBuilder.instance(this.collectionIndividualAccountBasicViceDAO).searchFilter(new HashMap<String, Object>() {{

			this.put("grywmx.ywlsh", YWLSH);

		}}).getObject(new DAOBuilder.ErrorHandler() {
			@Override
			public void error(Exception e) { throw new ErrorException(e); }
		});

		if (collectionIndividualAccountBasicVice == null||collectionIndividualAccountBasicVice.getGrywmx()==null||collectionIndividualAccountBasicVice.getGrywmx().getExtension()==null) {

			throw new ErrorException(ReturnEnumeration.Data_MISS,"业务记录");
		}

		if(!CollectionBusinessStatus.办结.getName().equals(collectionIndividualAccountBasicVice.getGrywmx().getExtension().getStep())){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"业务状态");
		}

		final StCommonPerson commonPerson = collectionIndividualAccountBasicVice.getGrywmx().getPerson();


		StCommonUnit commonUnit = DAOBuilder.instance(this.commonUnitDAO).searchFilter(new HashMap<String, Object>() {{

			this.put("dwzh", collectionIndividualAccountBasicVice.getDwzh());

		}}).getObject(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e); }
		});

		//endregion
		HeadAcctAlterRes result = new HeadAcctAlterRes();
		// 构建返回值
		result.setGRJCJS(""+collectionIndividualAccountBasicVice.getGrjcjs());

		result.setZhiWu(collectionIndividualAccountBasicVice.getZhiWu());

		result.setCSNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month,collectionIndividualAccountBasicVice.getCsny(), format));

		result.setGGJSCHJNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month,collectionIndividualAccountBasicVice.getGjjschjny(), format));

		result.setZJLX(collectionIndividualAccountBasicVice.getZjlx());

		Float gryjce = (commonUnit.getCollectionUnitAccount().getGrjcbl().floatValue()) * collectionIndividualAccountBasicVice.getGrjcjs().floatValue();

		result.setGRYJCE(""+ComUtils.moneyFormat(new BigDecimal(gryjce.toString())));

		result.setGRZH(collectionIndividualAccountBasicVice.getGrzh());

		result.setRiQi(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month,new Date(), format));

		result.setZhiYe(collectionIndividualAccountBasicVice.getZhiYe());

		result.setJZNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month,collectionIndividualAccountBasicVice.getGjjschjny(), format));

		if (commonUnit != null && commonUnit.getCollectionUnitAccount() != null && commonUnit.getCollectionUnitAccount().getGrjcbl() != null) {
			Float dwyjce = collectionIndividualAccountBasicVice.getGrjcjs().floatValue() * (commonUnit.getCollectionUnitAccount().getDwjcbl().floatValue());
			result.setDWYJCE(""+ComUtils.moneyFormat(new BigDecimal(dwyjce.toString())));
		}
		result.setZJHM(collectionIndividualAccountBasicVice.getZjhm());

		if (commonPerson != null && commonPerson.getCollectionPersonalAccount() != null) {

			SingleDictionaryDetail  XingBieDetail = iDictionaryService.getSingleDetail(commonPerson.getCollectionPersonalAccount().getGrzhzt(),"PersonalAccountState");

			result.setGRZHZT(XingBieDetail!=null?XingBieDetail.getName():"");

//			result.setGRZHZT(commonPerson.getCollectionPersonalAccount().getGrzhzt());
		}
		SingleDictionaryDetail  XingBieDetail = iDictionaryService.getSingleDetail(collectionIndividualAccountBasicVice.getXingBie(),"Gender");

		result.setXingBie(XingBieDetail!=null?XingBieDetail.getName():"");

		if (commonUnit != null) {

			result.setDWMC(commonUnit.getDwmc());
		}
		result.setDWZH(collectionIndividualAccountBasicVice.getDwzh());
		if(collectionIndividualAccountBasicVice.getHyzk() !=null) {

			SingleDictionaryDetail  HYZKDetail = iDictionaryService.getSingleDetail(collectionIndividualAccountBasicVice.getHyzk(),"MaritalStatus");

			result.setHYZK(HYZKDetail!=null?HYZKDetail.getName():"");
		}


		result.setYWLSH(YWLSH);

		result.setGRCKZHKHYHMC(collectionIndividualAccountBasicVice.getGrckzhkhyhmc());

		result.setJTZZ(collectionIndividualAccountBasicVice.getJtzz());

		if (commonUnit != null && commonUnit.getCollectionUnitAccount() != null && commonUnit.getCollectionUnitAccount().getGrjcbl() != null && commonUnit.getCollectionUnitAccount().getDwjcbl() != null) {

			Float yjce = collectionIndividualAccountBasicVice.getGrjcjs().floatValue() * (commonUnit.getCollectionUnitAccount().getGrjcbl().floatValue() + commonUnit.getCollectionUnitAccount().getDwjcbl().floatValue());

			result.setYJCE(""+ComUtils.moneyFormat(new BigDecimal(yjce.toString())));
		}
		result.setYZBM(collectionIndividualAccountBasicVice.getYzbm());

		if (commonUnit != null) {

			result.setGRJCBL(""+(commonUnit.getCollectionUnitAccount().getGrjcbl()==null?0:commonUnit.getCollectionUnitAccount().getGrjcbl().multiply(new BigDecimal("100"))));

		}
		result.setZhiCheng(collectionIndividualAccountBasicVice.getZhiCheng());

		result.setSJHM(collectionIndividualAccountBasicVice.getSjhm());

		if (commonPerson != null && commonPerson.getCollectionPersonalAccount() != null) {

			result.setKHRQ(DateUtil.date2Str(commonPerson.getCollectionPersonalAccount().getKhrq(), format));
		}

		result.setGDDHHM(collectionIndividualAccountBasicVice.getGddhhm());

		if (commonUnit != null && commonUnit.getCollectionUnitAccount() != null) {

			result.setDWJCBL(""+(commonUnit.getCollectionUnitAccount().getDwjcbl()==null?0:commonUnit.getCollectionUnitAccount().getDwjcbl().multiply(new BigDecimal("100"))));
		}

		result.setXMQP(collectionIndividualAccountBasicVice.getXmqp());

		result.setYouXiang(collectionIndividualAccountBasicVice.getDzyx());
		if(collectionIndividualAccountBasicVice.getXueLi()!=null) {

			SingleDictionaryDetail XueLiDetail = iDictionaryService.getSingleDetail(collectionIndividualAccountBasicVice.getXueLi(), "Educational");

			result.setXueLi(XueLiDetail!=null?XueLiDetail.getName():"");

		}
		result.setXingMing(collectionIndividualAccountBasicVice.getXingMing());

		result.setGRCKZHHM(collectionIndividualAccountBasicVice.getGrckzhhm());

		result.setGRCKZHKHYHDM(collectionIndividualAccountBasicVice.getGrckzhkhyhdm());

		result.setYWWD(collectionIndividualAccountBasicVice.getGrywmx().getExtension().getYwwd().getMingCheng());

		result.setCZY(collectionIndividualAccountBasicVice.getGrywmx().getExtension().getCzy());

		result.setDWJBR(collectionPersonalBusinessDetailsDAO.getByYwlsh(YWLSH).getExtension().getJbrxm());
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
		String id = pdfService.getIndiAcctAlterReceiptPdf(result);
		System.out.println("生成id的值："+id);
		CommonResponses commonResponses = new CommonResponses();
		commonResponses.setId(id);
		commonResponses.setState("success");
		return commonResponses;
	}

	@Override
	public void doAcctAlter(TokenContext tokenContext, String ywlsh) {

		CCollectionIndividualAccountBasicVice accAlter = DAOBuilder.instance(this.collectionIndividualAccountBasicViceDAO).searchFilter(new HashMap<String, Object>(){{

			this.put("grywmx.ywlsh",ywlsh);

		}}).getObject(new DAOBuilder.ErrorHandler() {
			@Override
			public void error(Exception e) { throw new ErrorException(e);}
		});

		if(accAlter == null||accAlter.getGrywmx()==null){

			throw new ErrorException(ReturnEnumeration.Data_MISS,"业务记录");

		}
		StCollectionPersonalBusinessDetails grywmx = accAlter.getGrywmx();
		CCollectionPersonalBusinessDetailsExtension busExtension = grywmx.getExtension();

		StCommonPerson commonPerson =  accAlter.getGrywmx().getPerson();
		StCommonUnit commonUnit = accAlter.getGrywmx().getUnit();

		StCollectionPersonalAccount personalAccount = commonPerson.getCollectionPersonalAccount();
		CCommonPersonExtension commonPersonExtension =  commonPerson.getExtension();

		commonPerson.setXingMing(accAlter.getXingMing());
		commonPerson.setXmqp(accAlter.getXmqp());
		commonPerson.setXingBie(accAlter.getXingBie().toCharArray()[0]);
		commonPerson.setGddhhm(accAlter.getGddhhm());
		commonPerson.setSjhm(accAlter.getSjhm());
		commonPerson.setZjlx(accAlter.getZjlx());
		commonPerson.setZjhm(accAlter.getZjhm());
		commonPerson.setCsny(accAlter.getCsny());
		commonPerson.setHyzk(accAlter.getHyzk());
		commonPerson.setZhiYe(accAlter.getZhiYe());
		commonPerson.setZhiChen(accAlter.getZhiCheng());
		commonPerson.setZhiWu(accAlter.getZhiWu());
		commonPerson.setXueLi(accAlter.getXueLi());
		commonPerson.setYzbm(accAlter.getYzbm());
		commonPerson.setJtzz(accAlter.getJtzz());
		commonPerson.setJtysr(accAlter.getJtysr());
		commonPerson.setUnit(commonUnit);

		personalAccount.setGrckzhhm(accAlter.getGrckzhhm());
		personalAccount.setGrckzhkhyhmc(accAlter.getGrckzhkhyhmc());
		personalAccount.setGrckzhkhyhdm(accAlter.getGrckzhkhyhdm());

		commonPersonExtension.setGjjschjny(accAlter.getGjjschjny());
		commonPersonExtension.setGrzl(accAlter.getBlzl());
		commonPersonExtension.setDzyx(accAlter.getDzyx());

		busExtension.setBjsj(new Date());
		collectionIndividualAccountBasicViceDAO.update(accAlter);
		RpcAuth rpcAuth = new RpcAuth();
		rpcAuth.setEmail(commonPerson.getExtension().getDzyx());
		rpcAuth.setType(1);
		rpcAuth.setUser_id(commonPerson.getCollectionPersonalAccount().getId());
		rpcAuth.setUsername(commonPerson.getCollectionPersonalAccount().getGrzh());
		rpcAuth.setState(1);
		Msg rpcMsg = accountRpcService.updateAuth(commonPerson.getCollectionPersonalAccount().getId(),ResUtils.noneAdductionValue(RpcAuth.class,rpcAuth));

		this.iSaveAuditHistory.saveNormalBusiness(ywlsh,tokenContext, CollectionBusinessType.变更.getName(),"办结");

		if(rpcMsg.getCode() != ReturnCode.Success){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,rpcMsg.getMsg());
		}

		//region //更新贷款

		//region //个人信息附表-共同借款人附表-合同表
		List<CLoanHousingBusinessProcess> list_process = DAOBuilder.instance(this.loanHousingBusinessProcessDAO).searchOption(SearchOption.FUZZY).extension(new IBaseDAO.CriteriaExtension() {

			@Override
			public void extend(Criteria criteria) {

				criteria.createAlias("loanHousingPersonInformationVice","loanHousingPersonInformationVice");
				criteria.createAlias("loanContract","loanContract",JoinType.LEFT_OUTER_JOIN);
				criteria.createAlias("loanHousingCoborrowerVice","loanHousingCoborrowerVice",JoinType.LEFT_OUTER_JOIN);

				criteria.add(Restrictions.or(
						Restrictions.eq("loanHousingPersonInformationVice.jkrgjjzh",grywmx.getGrzh()),
						Restrictions.and(Restrictions.isNotNull("loanContract"),Restrictions.eq("loanContract.jkrgjjzh",grywmx.getGrzh())),
						Restrictions.and(Restrictions.isNotNull("loanHousingCoborrowerVice"),Restrictions.eq("loanHousingCoborrowerVice.gtjkrgjjzh",grywmx.getGrzh()))
				));
			}
		}).getList(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e); }

		});

		list_process.forEach(new Consumer<CLoanHousingBusinessProcess>() {
			@Override
			public void accept(CLoanHousingBusinessProcess housingBusinessProcess) {

				if(housingBusinessProcess.getLoanHousingPersonInformationVice()!=null){

					if(commonPerson.getGrzh().equals(housingBusinessProcess.getLoanHousingPersonInformationVice().getJkrgjjzh())){

						housingBusinessProcess.getLoanHousingPersonInformationVice().setJkrxm(commonPerson.getXingMing());
						housingBusinessProcess.getLoanHousingPersonInformationVice().setJkrzjhm(commonPerson.getZjhm());
					}
				}

				if(housingBusinessProcess.getLoanContract()!=null){

					if(commonPerson.getGrzh().equals(housingBusinessProcess.getLoanContract().getJkrgjjzh())){

						housingBusinessProcess.getLoanContract().setJkrxm(commonPerson.getXingMing());
						housingBusinessProcess.getLoanContract().setJkrzjh(commonPerson.getZjhm());
					}
				}

				if(housingBusinessProcess.getLoanHousingCoborrowerVice()!=null){

					if(commonPerson.getGrzh().equals(housingBusinessProcess.getLoanHousingCoborrowerVice().getGtjkrgjjzh())){

						housingBusinessProcess.getLoanHousingCoborrowerVice().setGtjkrxm(commonPerson.getXingMing());
						housingBusinessProcess.getLoanHousingCoborrowerVice().setGtjkrzjhm(commonPerson.getZjhm());
					}
				}
			}
		});

		DAOBuilder.instance(this.loanHousingBusinessProcessDAO).entities(list_process).save(new DAOBuilder.ErrorHandler() {
			@Override
			public void error(Exception e) { throw new ErrorException(e);}
		});

		//endregion

		//region //个人信息附表-共同借款人附表-合同表
		List<CLoanHousingPersonInformationBasic> list_basic = DAOBuilder.instance(this.loanHousingPersonInformationBasicDAO).searchOption(SearchOption.FUZZY).extension(new IBaseDAO.CriteriaExtension() {

			@Override
			public void extend(Criteria criteria) {

				criteria.createAlias("coborrower","coborrower",JoinType.LEFT_OUTER_JOIN);

				criteria.add(Restrictions.or(
						Restrictions.eq("jkrgjjzh",grywmx.getGrzh()),
						Restrictions.and(Restrictions.isNotNull("coborrower"),Restrictions.eq("coborrower.gtjkrgjjzh",grywmx.getGrzh()))
				));
			}
		}).getList(new DAOBuilder.ErrorHandler() {

			@Override
			public void error(Exception e) { throw new ErrorException(e); }

		});

		list_basic.forEach(new Consumer<CLoanHousingPersonInformationBasic>() {

			@Override
			public void accept(CLoanHousingPersonInformationBasic loanHousingPersonInformationBasic) {


				if(commonPerson.getGrzh().equals(loanHousingPersonInformationBasic.getJkrgjjzh())){

					loanHousingPersonInformationBasic.setJkrxm(commonPerson.getXingMing());
					loanHousingPersonInformationBasic.setJkrzjhm(commonPerson.getZjhm());
				}

				if(loanHousingPersonInformationBasic.getCoborrower()!=null){

					if(commonPerson.getGrzh().equals(loanHousingPersonInformationBasic.getCoborrower().getGtjkrgjjzh())){

						loanHousingPersonInformationBasic.getCoborrower().setGtjkrxm(commonPerson.getXingMing());
						loanHousingPersonInformationBasic.getCoborrower().setGtjkrzjhm(commonPerson.getZjhm());
					}
				}
			}
		});

		DAOBuilder.instance(this.loanHousingPersonInformationBasicDAO).entities(list_basic).save(new DAOBuilder.ErrorHandler() {
			@Override
			public void error(Exception e) { throw new ErrorException(e);}
		});
		//endregion

		//endregion

		//region//更新历史
		List<StCommonPerson>list_commonPerson = DAOBuilder.instance(this.commonPersonDAO).searchFilter(new HashMap<String, Object>(){{

			this.put("zjhm",commonPerson.getZjhm());

		}}).getList(new DAOBuilder.ErrorHandler() {
			@Override
			public void error(Exception e) { throw new ErrorException(e);}
		});

		list_commonPerson.forEach(new Consumer<StCommonPerson>() {
			@Override
			public void accept(StCommonPerson stCommonPerson) {

				stCommonPerson.setXingMing(commonPerson.getXingMing());
			}
		});

		DAOBuilder.instance(this.commonPersonDAO).entities(list_commonPerson).save(new DAOBuilder.ErrorHandler() {
			@Override
			public void error(Exception e) { throw new ErrorException(e); }
		});
		//endregion

		//region //短信
		try {
			this.ismsCommon.sendSingleSMSWithTemp(commonPerson.getSjhm(), SMSTemp.个人账户信息变更.getCode(),
					new ArrayList<String>() {{
						this.add(commonPerson.getXingMing());
						this.add(String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1));
						this.add(String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
					}}
			);
		}catch (Exception e){
			LogManager.getLogger(this.getClass()).info("个人账户信息变更短信发送失败:"+e.getMessage());
		}
		//endregion
	}

	private void isCollectionIndividualAccountBasicViceAvailable(CCollectionIndividualAccountBasicVice collectionIndividualAccountBasicVice){


		CCollectionPersonalBusinessDetailsExtension collectionPersonalBusinessDetailsExtension = collectionIndividualAccountBasicVice.getGrywmx().getExtension();

		if(!this.iUploadImagesService.validateUploadFile(UploadFileModleType.归集.getCode(), UploadFileBusinessType.个人信息变更.getCode(),collectionIndividualAccountBasicVice.getBlzl())){
			throw new ErrorException(ReturnEnumeration.Data_MISS,"办理资料");
		}

		if(!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getGjjschjny())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"公积金首次汇缴年月");}

		if(!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getGrckzhhm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"个人存款账户号码");}

		if(collectionIndividualAccountBasicVice.getGrjcjs() == null){ throw new ErrorException(ReturnEnumeration.Data_MISS,"个人缴存基数");}

		if(!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getGrckzhkhyhdm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"个人存款账户开户银行代码");}

		if(!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getGrckzhkhyhmc())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"个人存款账户开银行名称");}

		if(!StringUtil.notEmpty(collectionPersonalBusinessDetailsExtension.getJbrzjlx())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"经办人证件类型");}

		//if(!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getBlzl())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"办理资料");}

		if(!StringUtil.notEmpty(collectionPersonalBusinessDetailsExtension.getJbrxm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"经办人姓名");}

		if(!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getHyzk())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"婚姻状况");}

		if(!CollectionUtils.flatmap(iDictionaryService.getDictionaryByType("MaritalStatus"), new CollectionUtils.Transformer<CommonDictionary, String>() {
			@Override
			public String tansform(CommonDictionary var1) { return var1.getCode(); }

		}).contains(collectionIndividualAccountBasicVice.getHyzk())){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"婚姻状况");
		}
		//if(!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getJtzz())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"家庭住址");}

		if(!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getCsny())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"出生年月");}

		if(!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getZjlx())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"证件类型");}

		if(!CollectionUtils.flatmap(iDictionaryService.getDictionaryByType("PersonalCertificate"), new CollectionUtils.Transformer<CommonDictionary, String>() {
			@Override
			public String tansform(CommonDictionary var1) { return var1.getCode(); }

		}).contains(collectionIndividualAccountBasicVice.getZjlx())){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"证件类型");
		}

		if(!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getZhiYe())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"职业");}

		if(!CollectionUtils.flatmap(iDictionaryService.getDictionaryByType("Vocation"), new CollectionUtils.Transformer<CommonDictionary, String>() {
			@Override
			public String tansform(CommonDictionary var1) { return var1.getCode(); }

		}).contains(collectionIndividualAccountBasicVice.getZhiYe())){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"职业");
		}
		//if(!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getYzbm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"邮政编码");}

		if(!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getZhiCheng())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"职称");}

		if(collectionIndividualAccountBasicVice.getZhiCheng().length()!=3){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"职称");
		}

		if(!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getSjhm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"手机号码");}

		if(collectionIndividualAccountBasicVice.getSjhm().length()!=11){
			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"手机号码");
		}

		if(!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getZjhm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"证件号码");}

		if(PersonCertificateType.身份证.getCode().equals(collectionIndividualAccountBasicVice.getZjlx())&&!IdcardValidator.isValidatedAllIdcard(collectionIndividualAccountBasicVice.getZjhm())){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"身份证号码");
		}
		//if(!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getGddhhm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"固定电话号码");}

		if(!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getXmqp())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"姓名全拼");}

		if(!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getXingBie())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"性别");}

		if(!CollectionUtils.flatmap(iDictionaryService.getDictionaryByType("Gender"), new CollectionUtils.Transformer<CommonDictionary, String>() {
			@Override
			public String tansform(CommonDictionary var1) { return var1.getCode(); }

		}).contains(collectionIndividualAccountBasicVice.getXingBie())){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"性别");
		}
		//if(!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getJtysr())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"家庭月收入");}

		if(!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getXueLi())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"学历");}

		if(collectionIndividualAccountBasicVice.getXueLi().length()!=3&&!CollectionUtils.flatmap(iDictionaryService.getDictionaryByType("Educational"), new CollectionUtils.Transformer<CommonDictionary, String>() {
			@Override
			public String tansform(CommonDictionary var1) { return var1.getCode(); }

		}).contains(collectionIndividualAccountBasicVice.getXueLi())){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"学历");
		}
		if(!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getXingMing())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"姓名");}

		if(collectionIndividualAccountBasicVice.getXingMing().contains(" ")){
			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"姓名不能包含空格");
		}
		if(!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getZhiWu())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"职务");}

		if(collectionIndividualAccountBasicVice.getZhiWu().length()!=4){
			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"职务");
		}
		//if(!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getDzyx())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"邮箱");}

		if(!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getYwwd())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"业务网点");}

//		if(!StringUtil.notEmpty(collectionPersonalBusinessDetailsExtension.getYwwd())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"业务网点");}

		if(!StringUtil.notEmpty(collectionPersonalBusinessDetailsExtension.getJbrzjhm())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"经办人证件号码");}

		if(!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getDwzh())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"单位账号");}

		if(!StringUtil.notEmpty(collectionIndividualAccountBasicVice.getCzy())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"操作员");}

		if(!StringUtil.notEmpty(collectionPersonalBusinessDetailsExtension.getCzy())){ throw new ErrorException(ReturnEnumeration.Data_MISS,"操作员");}

	}

	//region //过时
	@Deprecated
	private void doAcctMerge(StCommonPerson person, StCommonPerson detetedPerson,CCollectionIndividualAccountBasicVice accAlter) {

		//1、插入合并账户的业务表，以供查询


		//2、更改个人信息，原账户销户，其他信息不做更改，钱作销户处理

		StCollectionPersonalAccount detetedAccount = detetedPerson.getCollectionPersonalAccount();
		CCommonPersonExtension detetedExtension = detetedPerson.getExtension();
		//TODO  现在还不知道利息到底如何计算
		BigDecimal bjje = detetedExtension.getBjje();
		BigDecimal lxje = detetedExtension.getLxje();		//利息金额，这里是否每天计算

		detetedExtension.setBjje(BigDecimal.ZERO);
		detetedExtension.setLxje(BigDecimal.ZERO);
		detetedPerson.setXingMing(accAlter.getXingMing());
		detetedPerson.setZjhm(accAlter.getZjhm());
		detetedAccount.setGrckzhhm(accAlter.getGrckzhhm());
		detetedAccount.setGrzhzt(PersonAccountStatus.合并销户.getCode());
		commonPersonDAO.update(detetedPerson);

		StCollectionPersonalAccount pAccount = person.getCollectionPersonalAccount();
		BigDecimal grzhye = pAccount.getGrzhye();
		grzhye = grzhye.add(bjje).add(lxje);
		pAccount.setGrzhdngjye(grzhye);
		commonPersonDAO.update(person);
		//3、单位信息更新		//TODO 暂时这样处理,后面入账功能可以了再改
		String dwzh = person.getUnit().getDwzh();
		String oldDwzh = detetedPerson.getUnit().getDwzh();
		BusUtils.refreshUnitAcount(oldDwzh);
		BusUtils.refreshUnitAcount(dwzh);
		//4、入账,类似提取销户


	}

	@Deprecated @Override
	public SubmitAlterIndiAcctRes submitAcctAlter(TokenContext tokenContext,List<String> YWLSHJH){

		if(true){

			throw new ErrorException(ReturnEnumeration.Data_MISS,"变更批量提交功能已取消");

		}
		//region // 检查参数

		if (YWLSHJH == null) { throw new ErrorException(ReturnEnumeration.Parameter_MISS,"业务流水号"); }

		//endregion

		ArrayList<Exception> exceptions = new ArrayList<>();

		for(String YWLSH: YWLSHJH) {

			//region //必要字段查询&完整性验证
			CCollectionIndividualAccountBasicVice collectionIndividualAccountBasicVice = DAOBuilder.instance(this.collectionIndividualAccountBasicViceDAO).searchFilter(new HashMap<String, Object>() {{

				this.put("grywmx.ywlsh", YWLSH);

			}}).getObject(new DAOBuilder.ErrorHandler() {

				@Override
				public void error(Exception e) { throw new ErrorException(e); }

			});

			if (collectionIndividualAccountBasicVice == null || collectionIndividualAccountBasicVice.getGrywmx() == null || collectionIndividualAccountBasicVice.getGrywmx().getExtension() == null) {

				return new SubmitAlterIndiAcctRes(){{

					this.setSBYWLSH(YWLSH);

					this.setStatus("fail");
				}};
			}

			try {

				this.isCollectionIndividualAccountBasicViceAvailable(collectionIndividualAccountBasicVice);

				this.iSaveAuditHistory.saveNormalBusiness(YWLSH,tokenContext, CollectionBusinessType.变更.getName(),"修改");

			}catch (Exception e){

				return new SubmitAlterIndiAcctRes(){{

					this.setSBYWLSH(YWLSH);

					this.setStatus("fail");
				}};
			}

			StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = collectionIndividualAccountBasicVice.getGrywmx();

			CCollectionPersonalBusinessDetailsExtension collectionPersonalBusinessDetailsExtension = collectionPersonalBusinessDetails.getExtension();

			StCommonPerson commonPerson = collectionPersonalBusinessDetails.getPerson();

			if(commonPerson == null || commonPerson.getUnit() == null||commonPerson.getExtension() == null||"02".equals(commonPerson.getExtension().getSfdj())){

				return new SubmitAlterIndiAcctRes(){{

					this.setSBYWLSH(YWLSH);

					this.setStatus("fail");
				}};
			}
			//endregion

			//region //在途验证

			if(!DAOBuilder.instance(collectionIndividualAccountBasicViceDAO).searchFilter(new HashMap<String, Object>(){{

				this.put("zjhm",collectionIndividualAccountBasicVice.getZjhm());

			}}).extension(new IBaseDAO.CriteriaExtension() {
				@Override
				public void extend(Criteria criteria) {

					criteria.add(Restrictions.not(Restrictions.in(CriteriaUtils.addAlias(criteria,"grywmx.cCollectionPersonalBusinessDetailsExtension.step"),(Collection)CollectionUtils.flatmap(CollectionUtils.merge(CollectionBusinessStatus.办结.getSubTypes(), CollectionBusinessStatus.新建.getSubTypes()), new CollectionUtils.Transformer<CollectionBusinessStatus, String>() {

						@Override
						public String tansform(CollectionBusinessStatus var1) { return var1.getName(); }

					}))));
					criteria.add(Restrictions.ne("grywmx.ywlsh",YWLSH));
					criteria.add(Restrictions.eq("cCollectionPersonalBusinessDetailsExtension.czmc",CollectionBusinessType.变更.getCode()));
				}
			}).isUnique(new DAOBuilder.ErrorHandler() {

				@Override
				public void error(Exception e) { exceptions.add(e);}

			})){

				return new SubmitAlterIndiAcctRes(){{

					this.setSBYWLSH(YWLSH);

					this.setStatus("fail");
				}};
			}
			//endregion

			//region //修改状态
			StateMachineUtils.updateState(this.stateMachineService, Events.通过.getEvent(),new TaskEntity() {{

				this.setStatus(collectionPersonalBusinessDetailsExtension.getStep()==null ? "初始状态":collectionPersonalBusinessDetailsExtension.getStep());
				this.setTaskId(YWLSH);
				this.setRoleSet(new HashSet<>(tokenContext.getRoleList()));;
				this.setNote("");
				this.setSubtype(BusinessSubType.归集_个人账户信息变更.getSubType());
				this.setType(BusinessType.Collection);
				this.setOperator(collectionIndividualAccountBasicVice.getCzy());
				this.setWorkstation(collectionPersonalBusinessDetails.getUnit().getExtension().getKhwd());

			}}, new StateMachineUtils.StateChangeHandler() {

				@Override
				public void onStateChange(boolean succeed, String next, Exception e) {

					if (e != null) { exceptions.add(e); }

					if (!succeed || next == null || e != null) { return; }

					collectionPersonalBusinessDetailsExtension.setStep(next);


					if(StringUtil.isIntoReview(next,null)){

						collectionPersonalBusinessDetailsExtension.setDdsj(new Date());

					}

					DAOBuilder.instance(collectionIndividualAccountBasicViceDAO).entity(collectionIndividualAccountBasicVice).save(new DAOBuilder.ErrorHandler() {

						@Override
						public void error(Exception e) { exceptions.add(e); }
					});

					if(next.equals(CollectionBusinessStatus.办结.getName())){

						try {

							doAcctAlter(tokenContext,collectionPersonalBusinessDetails.getYwlsh());

						}catch (Exception ex){

							exceptions.add(ex);
						}

					}
				}
			});


			if(exceptions.size()!=0){

				return new SubmitAlterIndiAcctRes(){{

					this.setSBYWLSH(YWLSH);

					this.setStatus("fail");
				}};
			}

			//endregion
		}

		return  new SubmitAlterIndiAcctRes() {{

			this.setSBYWLSH(this.getSBYWLSH());

			this.setStatus("success");

		}};
	}

	//endregion
}
