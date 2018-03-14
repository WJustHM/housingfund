package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.database.dao.ICCollectionIndividualAccountActionViceDAO;
import com.handge.housingfund.database.entities.CCollectionIndividualAccountActionVice;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Funnyboy on 2017/7/10.
 */
@Repository
public class CCollectionIndividualAccountActionViceDAO extends BaseDAO<CCollectionIndividualAccountActionVice>
		implements ICCollectionIndividualAccountActionViceDAO {
	@Override
	public CCollectionIndividualAccountActionVice getByYWLSH(String ywlsh) {
		String sql = "select result from CCollectionIndividualAccountActionVice result where result.grywmx.ywlsh = :ywlsh ";
		CCollectionIndividualAccountActionVice result = getSession().createQuery(sql, CCollectionIndividualAccountActionVice.class)
				.setParameter("ywlsh", ywlsh)
				.uniqueResult();
		return result;
	}

	@Override
	public List<CCollectionIndividualAccountActionVice> getSealBusBefore(String dwzh, String qcyf, Date lastqcbjsj) {
		String sql = "select result from CCollectionIndividualAccountActionVice result " +
				"where result.dwzh = :dwzh and result.sxny = :qcyf " +
				"and result.grywmx.cCollectionPersonalBusinessDetailsExtension.bjsj < :lastqcbjsj " +
				"and result.grywmx.cCollectionPersonalBusinessDetailsExtension.step = '办结' ";
		List<CCollectionIndividualAccountActionVice> list = getSession().createQuery(sql, CCollectionIndividualAccountActionVice.class)
				.setParameter("dwzh", dwzh)
				.setParameter("qcyf", qcyf)
				.setParameter("lastqcbjsj", lastqcbjsj)
				.list();
		return list;
	}

	@Override
	public List<CCollectionIndividualAccountActionVice> getSealBusAfter(String dwzh, String qcyf, Date lastqcbjsj) {
		String sql = "select result from CCollectionIndividualAccountActionVice result " +
				"where result.dwzh = :dwzh and result.sxny+0 <= :qcyf " +
				"and result.grywmx.cCollectionPersonalBusinessDetailsExtension.bjsj > :lastqcbjsj " +
				"and result.grywmx.cCollectionPersonalBusinessDetailsExtension.step = '办结' " +
				"order by result.grywmx.cCollectionPersonalBusinessDetailsExtension.bjsj asc ";
		List<CCollectionIndividualAccountActionVice> list = getSession().createQuery(sql, CCollectionIndividualAccountActionVice.class)
				.setParameter("dwzh", dwzh)
				.setParameter("qcyf", Integer.parseInt(qcyf))
				.setParameter("lastqcbjsj", lastqcbjsj)
				.list();
		return list;
	}

	@Override
	public List<CCollectionIndividualAccountActionVice> getSealBus(String dwzh, String qcyf) {
		String sql = "select result from CCollectionIndividualAccountActionVice result " +
				"where result.dwzh = :dwzh and result.sxny = :qcyf " +
				"and result.grywmx.cCollectionPersonalBusinessDetailsExtension.step = '办结' " +
				"order by result.grywmx.cCollectionPersonalBusinessDetailsExtension.bjsj asc ";
		List<CCollectionIndividualAccountActionVice> list = getSession().createQuery(sql, CCollectionIndividualAccountActionVice.class)
				.setParameter("dwzh", dwzh)
				.setParameter("qcyf", qcyf)
				.list();
		return list;
	}

	@Override
	public CCollectionIndividualAccountActionVice getNearlySeal(String grzh, String dwzh) {
		String sql = "select result from CCollectionIndividualAccountActionVice result " +
				"where result.grzh = :grzh " +
				"and result.dwzh = :dwzh " +
				"and result.grywmx.cCollectionPersonalBusinessDetailsExtension.step = '办结' " +
				"and result.grywmx.cCollectionPersonalBusinessDetailsExtension.czmc = '05'" +
				"order by sxny desc ";
		List<CCollectionIndividualAccountActionVice> list = getSession().createQuery(sql, CCollectionIndividualAccountActionVice.class)
				.setParameter("grzh", grzh)
				.setParameter("dwzh", dwzh)
				.list();
		if(list != null && list.size() >0){
			return list.get(0);
		}
		return null;
	}

}
