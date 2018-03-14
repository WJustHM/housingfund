package com.handge.housingfund.collection.service.indiacctmanage;

import com.handge.housingfund.collection.utils.ComUtils;
import com.handge.housingfund.collection.utils.ResponseUtils;
import com.handge.housingfund.common.service.collection.model.individual.*;
import com.handge.housingfund.common.service.collection.service.indiacctmanage.IndiInnerTransfer;
import com.handge.housingfund.common.service.enumeration.ErrorEnumeration;
import com.handge.housingfund.common.service.util.ResponseEntity;
import com.handge.housingfund.database.dao.ICAccountNetworkDAO;
import com.handge.housingfund.database.dao.ICCollectionIndividualAccountTransferViceDAO;
import com.handge.housingfund.database.dao.IStCommonPersonDAO;
import com.handge.housingfund.database.dao.IStCommonUnitDAO;
import com.handge.housingfund.database.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Liujuhao on 2017/7/4. 内部转移
 */
@Service
@Deprecated
public class IndiInnerTransferImpl implements IndiInnerTransfer {

	@Autowired
	private ICCollectionIndividualAccountTransferViceDAO innerTransferDAO;

	@Autowired
	private IStCommonPersonDAO personDAO;

	@Autowired
	private IStCommonUnitDAO unitDAO;

	@Autowired
	private ICAccountNetworkDAO cAccountNetworkDAO;

	/**
	 * 获取个人内部转移信息列表（批阅） 组合查询参数（转出单位、转入单位、状态）
	 *
	 * @return 封装了多条账户转移信息的列表 1.根据转出单位、转入单位、状态参数，查询“个人账户业务记录”相关的表； 2.异常处理，统一待定
	 *         3.封装数据库返回数据 4.返回结果
	 */
	public ResponseEntity getTransferInfo(final String ZCDWZH, final String ZhuangTai) {

		List<CCollectionIndividualAccountTransferVice> list = innerTransferDAO.getList(ZCDWZH,ZhuangTai);
		List<ListIndiAcctsTransferResRes> resultList = new ArrayList<ListIndiAcctsTransferResRes>();
		if(list == null){
			return ResponseUtils.buildCommonEntityResponse(resultList);
		}

		for (CCollectionIndividualAccountTransferVice dataModel : list) {
			ListIndiAcctsTransferResRes model = new ListIndiAcctsTransferResRes();
			model.setYWLSH(dataModel.getDwywmx().getYwlsh());
			//model.setZRSXRQ(ComUtils.parseToString(dataModel.getZysxny(), "yyyyMM"));
			model.setSLSJ(ComUtils.parseToString(dataModel.getDwywmx().getExtension().getSlsj(), "yyyyMM"));
			model.setZhuangTai(dataModel.getDwywmx().getExtension().getStep());
			model.setZCDW(dataModel.getZcdwmc());
			resultList.add(model);
		}
		return ResponseUtils.buildCommonEntityResponse(resultList);
	}

	/**
	 * 新建个人账户转移
	 *
	 * @param addIndiAcctTransfer
	 *            封装了个人账户转移信息的对象
	 * @return 成功/失败 1.接收addIndiAcctTransfer对象，获取转移账户列表
	 *         2.处理每条转移账户（判断账户状态？判断单位状态？） 3.更新个人账号和单位账号，更新“个人账户信息”、“单位账户信息”相关的表
	 *         4.异常处理，统一待定 5.返回结果
	 */
	public ResponseEntity addAcctTransfer(IndiAcctTransferPost addIndiAcctTransfer) {
		FlagMessage flagMsg = paramCheck(addIndiAcctTransfer);
		if (flagMsg.flag != true) {
			// 参数验证检查出错误，直接返回
			return ResponseUtils.buildCommonErrorResponse(ErrorEnumeration.Parameter_NOT_MATCH);
		}
		// 保存办理信息，单位主体部分
		CCollectionIndividualAccountTransferVice saveMainMsg = new CCollectionIndividualAccountTransferVice();
		//saveMainMsg.setZysxny(ComUtils.parseToDate(addIndiAcctTransfer.getZYSXNY(),"yyyyMM"));	//转移生效年月
		saveMainMsg.setZcdwmc(addIndiAcctTransfer.getZCDWMC());		//转出单位名称
		saveMainMsg.setZcdwzh(addIndiAcctTransfer.getZCDWZH());		//转出单位账号
		//业务明细表部分
		StCollectionUnitBusinessDetails dwywmx = new StCollectionUnitBusinessDetails();
		dwywmx.setDwzh(addIndiAcctTransfer.getZCDWZH());
		//dwywmx.setFsrs((long)addIndiAcctTransfer.getlist().size());
		saveMainMsg.setDwywmx(dwywmx);
		//业务明细附表部分
		CCollectionUnitBusinessDetailsExtension dwywmxfb= new CCollectionUnitBusinessDetailsExtension();
		dwywmxfb.setBlzl(addIndiAcctTransfer.getBLZL());    //vabli资料
		dwywmxfb.setCzy(addIndiAcctTransfer.getCZY());      //操作员
		dwywmxfb.setSlsj(new Date());           //受理日期
//		dwywmxfb.setYwwd(addIndiAcctTransfer.getYWWD());    //业务网点
        dwywmxfb.setJbrxm(addIndiAcctTransfer.getJBRXM());     //经办人姓名
        dwywmxfb.setJbrzjhm(addIndiAcctTransfer.getJBRZJHM());  //经办人证件号码
        dwywmxfb.setJbrzjlx(addIndiAcctTransfer.getJBRZJLX());  //经办人证件类型
		dwywmx.setExtension(dwywmxfb);
		// 单位下办理的个人详细部分
		List<IndiAcctTransferPostList> list = addIndiAcctTransfer.getlist();
		CCollectionIndividualAccountTransferDetailVice perMsg;
		Set<CCollectionIndividualAccountTransferDetailVice> nbzyxqSet = new HashSet<CCollectionIndividualAccountTransferDetailVice>();
		for (IndiAcctTransferPostList perdetail : list) {
			perMsg = new CCollectionIndividualAccountTransferDetailVice();
			perMsg.setBeiZhu(perdetail.getBeiZhu());
			perMsg.setGrzh(perdetail.getGRZH());
			perMsg.setXingMing(perdetail.getXingMing());
			perMsg.setZrdwmc(perdetail.getZRDWMC());
			perMsg.setZrdwzh(perdetail.getZRDWZH());
			nbzyxqSet.add(perMsg);
		}
		saveMainMsg.setNbzyxq(nbzyxqSet);
		// 设置级联关系进行保存
		//根据操作类型调用流程信息，/1保存2提交
		//TODO TEST
/*		if("1".equals(addIndiAcctTransfer.getCZLX())){
			saveMainMsg.getDwywmx().getExtension().setYwzt("01");
		}else{
			saveMainMsg.getDwywmx().getExtension().setYwzt("02");
		}*/
		innerTransferDAO.save(saveMainMsg);

		AddIndiAcctTransferRes result = new AddIndiAcctTransferRes();
		result.setStatus("200");
		result.setYWLSH(saveMainMsg.getDwywmx().getYwlsh());
		return ResponseUtils.buildCommonEntityResponse(result);
	}

	/**
	 * 修改个人账户转移
	 * @return 成功/失败 1.接受业务流水号YWLSH和reIndiAcctTransfer对象
	 *         2.获取对象中的转移账户列表，处理每条转移账户（判断账户状态？判断单位状态？）
	 *         3.根据业务流水号，更新“个人账户业务记录”相关的表 4.异常处理，统一待定 5.返回结果
	 */
	public ResponseEntity reAcctTransfer(String ywlsh, IndiAcctTransferPut reAcctTransfer) {
		FlagMessage flagMsg = paramCheck(reAcctTransfer);
		if (flagMsg.flag != true) {
			// 参数验证检查出错误，直接返回
			return ResponseUtils.buildCommonErrorResponse(ErrorEnumeration.Parameter_NOT_MATCH);
		}
		CCollectionIndividualAccountTransferVice innerUpdatMsg = innerTransferDAO.getByYWLSH(reAcctTransfer.getYWLSH());
		//innerUpdatMsg.setZysxny(ComUtils.parseToDate(reAcctTransfer.getZYSXNY(),"yyyyMM")); //单位部分只能修改生效年月

		// 更新单位下办理的个人详细部分
		List<IndiAcctTransferPutList> list = reAcctTransfer.getlist();
        Set<CCollectionIndividualAccountTransferDetailVice> perDetailDataSet = innerUpdatMsg.getNbzyxq();
        //根据修改的列，可能对原来的个人详情的产生增加，修改，删除（失效）的操作
		CCollectionIndividualAccountTransferDetailVice perMsg;
        outer : for (CCollectionIndividualAccountTransferDetailVice perDetailData: perDetailDataSet) {
			//perMsg = new CCollectionIndividualAccountTransferDetailVice();
            //perdetailModidy此时找到与数据库中个人账户相同数据，进行更新
            for(IndiAcctTransferPutList perdetailModidy : list){
                if(perdetailModidy.getGRZH().equals(perDetailData.getGrzh())){
                    perDetailData.setZrdwmc(perdetailModidy.getZRDWMC());
                    perDetailData.setZrdwzh(perdetailModidy.getZRDWZH());
                    perDetailData.setBeiZhu(perdetailModidy.getBeiZhu());
                    list.remove(perdetailModidy);
                    continue outer;
                }
            }
            //数据库中没有找到与perdetailModidy个人账户相同数据，删除当前perDetailData
            perDetailDataSet.remove(perDetailData);
		}
        //对数据库中没有的数据进行添加操作
        for(IndiAcctTransferPutList perdetailAdd : list){
            CCollectionIndividualAccountTransferDetailVice addMsg= new CCollectionIndividualAccountTransferDetailVice();
            addMsg.setBeiZhu(perdetailAdd.getBeiZhu());
            addMsg.setZrdwmc(perdetailAdd.getZRDWMC());
            addMsg.setZrdwzh(perdetailAdd.getZRDWZH());
            addMsg.setGrzh(perdetailAdd.getGRZH());
            addMsg.setXingMing(perdetailAdd.getXingMing());
            perDetailDataSet.add(addMsg);
        }
		CCollectionUnitBusinessDetailsExtension extension = innerUpdatMsg.getDwywmx().getExtension();
        extension.setCzy(reAcctTransfer.getCZY());
//        extension.setYwwd(reAcctTransfer.getYWWD());
		extension.setJbrxm(reAcctTransfer.getJBRXM());
		extension.setJbrzjhm(reAcctTransfer.getJBRZJHM());
		extension.setJbrzjlx(reAcctTransfer.getJBRZJLX());
		//TODO TEST
		if("0".equals(reAcctTransfer.getCZLX())){

			extension.setStep("保存");
		}else{

			extension.setStep("待审核");
		}
		// 设置级联关系进行修改
		innerTransferDAO.update(innerUpdatMsg);
        //根据操作类型调用流程信息，/1保存2提交

		ReIndiAcctTransferRes result = new ReIndiAcctTransferRes();
		result.setStatus("200");
		result.setYWLSH(reAcctTransfer.getYWLSH());
		return ResponseUtils.buildCommonEntityResponse(result);
	}

	/**
	 * 查看个人账户转移详情
	 *
	 * @param YWLSH
	 *            业务流水号
	 * @return 成功/失败 1.根据业务流水号，查询“个人账户业务记录”相关的表 2.异常处理，统一待定 3.封装数据库返回数据 4.返回结果
	 */
	public ResponseEntity showAcctTransfer(String YWLSH) {
		HashMap map = new HashMap();
		map.put("YWLSH", YWLSH);
		CCollectionIndividualAccountTransferVice corpInnerMsg = innerTransferDAO.getByYWLSH(YWLSH);
		// 内部转移查询返回的model
		GetIndiAcctTransferRes result = new GetIndiAcctTransferRes();
		if (null == corpInnerMsg) {
			// 查询为空，直接返回
			return ResponseUtils.buildCommonEntityResponse(result);
		}
		// 根据dwzh查询单位的信息,得到经办人信息

        CCollectionUnitBusinessDetailsExtension extension = corpInnerMsg.getDwywmx().getExtension();
        result.setYWLSH(corpInnerMsg.getDwywmx().getYwlsh());
        result.setBLZL(extension.getBlzl());
        result.setCZY(extension.getCzy());
        result.setJBRZJLX(extension.getJbrzjlx());
        result.setJBRZJHM(extension.getJbrzjhm());
        result.setJBRXM(extension.getJbrxm());
        result.setYWWD(extension.getYwwd().getMingCheng());
		result.setZCDWMC(corpInnerMsg.getZcdwmc());
		result.setZCDWZH(corpInnerMsg.getZcdwzh());
		//result.setZYSXNY(ComUtils.parseToString(corpInnerMsg.getZysxny(), "yyyyMM"));
		// 个人详细信息
		Set<CCollectionIndividualAccountTransferDetailVice> perInnerMsgs = corpInnerMsg.getNbzyxq();
		ArrayList<GetIndiAcctTransferResList> list = new ArrayList<GetIndiAcctTransferResList>();
		for (CCollectionIndividualAccountTransferDetailVice perInner : perInnerMsgs) {
			GetIndiAcctTransferResList indiAcctTransfer = new GetIndiAcctTransferResList();
			indiAcctTransfer.setBeiZhu(perInner.getBeiZhu());
			indiAcctTransfer.setGRZH(perInner.getGrzh());
			indiAcctTransfer.setXingMing(perInner.getXingMing());
			indiAcctTransfer.setZRDWMC(perInner.getZrdwmc());
			indiAcctTransfer.setZRDWZH(perInner.getZrdwzh());
			list.add(indiAcctTransfer);
		}
		result.setlist(list);
		return ResponseUtils.buildCommonEntityResponse(result);
	}

    /**
	 * 内部转移办结时处理
	 * 1、个人信息更新
	 * 2、再对单位信息(根据当前状态)进行资金、人员变动的修正
	 * 3、由于内部转移，单位是否发生资金变动，这个需要看财务的定义(一般的话应该是发生了变动)
	 *
	 * 生效年月的问题，这里不做判断，需外部判断。
	 * 这里的改变的前提是立刻生效，或等到某月生效时调用该方法
	 * @param ywlsh
	 * @return
	 */
    @Override
	public ResponseEntity doAcctTransfer(String ywlsh){

		CCollectionIndividualAccountTransferVice transferMsg = innerTransferDAO.getByYWLSH(ywlsh);

		//记录哪些些单位需要修正
		Map<String,StCommonUnit> modifyUnitMap = new HashMap<String,StCommonUnit>();

		//1、个人信息更新
		Set<CCollectionIndividualAccountTransferDetailVice> innerDetailSet = transferMsg.getNbzyxq();
		for(CCollectionIndividualAccountTransferDetailVice perInnerDetail: innerDetailSet){
			//对每个人进行遍历，通过个人账号得到个人信息
			StCommonPerson person= personDAO.getByGrzh(perInnerDetail.getGrzh());
			StCommonUnit unit = unitDAO.getUnit(perInnerDetail.getZrdwzh());
			//添加转出单位信息
			modifyUnitMap.put(unit.getDwzh(),unit);
			person.setUnit(unit);
			personDAO.save(person);
			// TODO 账户信息的改变历史如何记录
		}

		modifyUnitMap.put(transferMsg.getZcdwzh(),unitDAO.getUnit(transferMsg.getZcdwzh()));
		//2、对转入/转出的单位进行资金、人员的修正
		Set<Map.Entry<String, StCommonUnit>> entrySet = modifyUnitMap.entrySet();
		for(Map.Entry<String, StCommonUnit> unitEntry :entrySet){
			refleshUnitMsg(unitEntry.getValue());
		}
		// TODO 待处理
		//3、财务相关

		return ResponseUtils.buildCommonEntityResponse(null);
	}

	@Override
	public ResponseEntity headIndiAcctTransfer(String ywlsh) {

		CCollectionIndividualAccountTransferVice zyxx = innerTransferDAO.getByYWLSH(ywlsh);
		HeadIndiAcctTransfer result = new HeadIndiAcctTransfer();
		if(zyxx == null){
			return ResponseUtils.buildCommonEntityResponse(result);
		}
		result.setYWLSH(ywlsh);
		result.setRiQi(ComUtils.parseToString(new Date(),"yyyyMM"));
		result.setZCDWMC(zyxx.getZcdwmc());
		result.setZCDWZH(zyxx.getZcdwzh());
		result.setJBRXM(zyxx.getDwywmx().getExtension().getJbrxm());
		result.setCZY("yangfan");
		//result.setZYSXNY(ComUtils.parseToString(zyxx.getZysxny(),"yyyyMM"));

		ArrayList<HeadIndiAcctTransferList> list = new ArrayList<HeadIndiAcctTransferList>();
		Set<CCollectionIndividualAccountTransferDetailVice> nbzyxq = zyxx.getNbzyxq();
		for(CCollectionIndividualAccountTransferDetailVice zyxq: nbzyxq){
			HeadIndiAcctTransferList zyxqView = new HeadIndiAcctTransferList();
			zyxqView.setZRDWZH(zyxq.getZrdwzh());
			zyxqView.setZRDWMC(zyxq.getZrdwmc());
			zyxqView.setGRZH(zyxq.getGrzh());
			zyxqView.setXingMing(zyxq.getXingMing());
			zyxqView.setBeiZhu(zyxq.getBeiZhu());
			list.add(zyxqView);
		}
		result.setlist(list);
		return ResponseUtils.buildCommonEntityResponse(result);
	}

	/**
     * 因人员变动，对单位的账户信息进行更新
     */
	private void refleshUnitMsg(StCommonUnit value) {
		//TODO
		Object[] obj= innerTransferDAO.getUnitRefleshMsg(value);


	}

	/**
	 * 对修改model的参数进行验证
	 */
	private FlagMessage paramCheck(IndiAcctTransferPut reAcctTransfer) {
		if (null == reAcctTransfer) {
			return new FlagMessage(false, "参数不能为空！");
		} else if (ComUtils.isEmpty(reAcctTransfer.getYWLSH())) {
			return new FlagMessage(false, "业务流水号不能为空！");
		} else if (ComUtils.isEmpty(reAcctTransfer.getZYSXNY())) {
			return new FlagMessage(false, "转移生效年月不能为空！");
		} else if(!ComUtils.dateYYYYMMmatch(reAcctTransfer.getZYSXNY())){
            return new FlagMessage(false, "转移生效年月格式错误！");
        } else if(!("0".equals(reAcctTransfer.getCZLX()) ||  "1".equals(reAcctTransfer.getCZLX()))){
			return new FlagMessage(false, "操作类型错误，只能为0保存或1提交！");
		}

		ArrayList<IndiAcctTransferPutList> list = reAcctTransfer.getlist();
		for (IndiAcctTransferPutList perdetail : list) {
			if (null == perdetail) {
				return new FlagMessage(false, "要修改的个人信息,参数不能为空！");
			} else if (ComUtils.isEmpty(perdetail.getGRZH())) {
				return new FlagMessage(false, "要修改的个人信息,个人账号不能为空！");
			} else if (ComUtils.isEmpty(perdetail.getZRDWZH())) {
				return new FlagMessage(false, "要修改的个人信息,转入单位账号不能为空！");
			}
		}
		return new FlagMessage(true);
	}

	private FlagMessage paramCheck(IndiAcctTransferPost reAcctTransfer) {
		if (null == reAcctTransfer) {
			return new FlagMessage(false, "参数不能为空！");
		}else if (ComUtils.isEmpty(reAcctTransfer.getZCDWZH())) {
			return new FlagMessage(false, "转出单位账号不能为空！");
		}else if (ComUtils.isEmpty(reAcctTransfer.getZYSXNY())) {
			return new FlagMessage(false, "转移生效年月不能为空！");
		}else if (ComUtils.isEmpty(reAcctTransfer.getCZLX())) {
            return new FlagMessage(false, "操作类型不能为空！");
        }else if(!ComUtils.dateYYYYMMmatch(reAcctTransfer.getZYSXNY())){
            return new FlagMessage(false, "转移生效年月格式错误！");
        } else if(!("0".equals(reAcctTransfer.getCZLX()) ||  "1".equals(reAcctTransfer.getCZLX()))){
			return new FlagMessage(false, "操作类型错误，只能为0保存或1提交！");
		}

		final ArrayList<IndiAcctTransferPostList> list = reAcctTransfer.getlist();
		for (IndiAcctTransferPostList perdetail : list) {
			if (null == perdetail) {
				return new FlagMessage(false, "个人信息,参数不能为空！");
			} else if (ComUtils.isEmpty(perdetail.getGRZH())) {
				return new FlagMessage(false, "个人信息,个人账号不能为空！");
			} else if (ComUtils.isEmpty(perdetail.getZRDWZH())) {
				return new FlagMessage(false, "个人信息,转入单位账号不能为空！");
			}
		}
		return new FlagMessage(true);
	}

	@Override
	public ResponseEntity submitIndiAcctTransfer(List YWLSHJH) {
		return null;
	}

	/**
	 * 定义：验证检查的返回 flag为true，表示检查通过 message表示检查信息，可为空
	 */
	private class FlagMessage {
		boolean flag;
		String message;

		FlagMessage(boolean flag, String message) {
			this.flag = flag;
			this.message = message;
		}

		FlagMessage(boolean flag) {
			this.flag = flag;
		}
	}

}