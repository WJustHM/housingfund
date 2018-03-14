package com.handge.housingfund.database.utils;

import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.util.CollectionUtils;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.database.dao.IBaseDAO;
import com.handge.housingfund.database.entities.Common;
import com.handge.housingfund.database.entities.PageResults;
import com.handge.housingfund.database.enums.ListAction;
import com.handge.housingfund.database.enums.ListDeleted;
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.enums.SearchOption;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Consumer;


/*
 * Created by lian on 2017/7/17.
 */
@SuppressWarnings({"unused", "WeakerAccess", "Convert2Lambda", "Anonymous2MethodRef"})
public class DAOBuilder<K extends Common, T extends IBaseDAO<K>> {

	private T dao;

	private String UID = null;

	private HashMap<String, Object> searchFilter = new HashMap<>();

	private int page = 0;

	private int pageSize = 0;

	private Date start;

	private Date end;

	private String orderBy;

	private Order order;

	private ListDeleted listDeleted = ListDeleted.NOTDELETED;

	private SearchOption searchOption = null;

	private PageRes pageRes = null;

	private String marker = null;

	private ListAction action;

	private IBaseDAO.CriteriaExtension extension;

	private enum SaveOption {

		Default, Fetch_WhenSave
	}

	private UpateEntity upateEntity;

	private List<K> entities = new ArrayList<>();

	public static class UpateEntity {

		public void copyFromEntity(Object object){

			DAOBuilder.copyProperties(object,this);
		}
	}

	/*
	 * 使用说明
	 * 
	 * DAOBuilder<Common,Common> builder = DAOBuilder(this.commonDAO);
	 * 
	 * // 设置 dao 中 list方法 对应的filter参数 // 根据id 查询 key为UID 也可以用 builder.UID()
	 * builder.searchFilter(new HashMap())
	 * 
	 * // 设置 dao 中 get方法 对应的 id 参数 builder.UID("ID")
	 * 
	 * // 不设置时不会分页查询 设置以后将分页查询 bulider.pageOption(10,1)
	 * 
	 * // 设置 dao 中 list 方法 对应的 start 和 end 参数 builder.betweenDate(new Date(),
	 * new Date())
	 * 
	 * // 设置 dao 中 list 方法对应的 isDeleted 参数
	 * builder.isDeleted(ListDeleted.NOTDELETED)
	 * 
	 * // 设置 dao 中 list 方法对应的 searchOption 参数
	 * builder.searchOption(SearchOption.REFINED)
	 * 
	 * // 设置 dao 中 save 或 update 方法对应的 entity 参数 builder.entity(new Common());
	 * 
	 * // 获取列表 在提供UID 的情况下 返回一个 只有一个对象或没有对象的 数组 builder.getList(new
	 * DAOBuilder.ErrorHandler() {
	 * 
	 * @Override public void error(Exception e) { }
	 * 
	 * }
	 * 
	 * // 获取列表 在有多个对象满足条件时 取第一个 builder.getObject(new DAOBuilder.ErrorHandler(){
	 * 
	 * @Override public void error(Exception e) { exceptions.add(e); }
	 * 
	 * }
	 * 
	 * // entity 有id 时 调用update 否则调用save builder.save(new DAOBuilder.ErrorHandler() {
	 * 
	 * @Override public void error(Exception e) { exceptions.add(e); }
	 * 
	 * }
	 * 
	 * // entity 有id 时 调用delete 否则不进行任何操作 builder.delete(new DAOBuilder.ErrorHandler() {
	 * 
	 * @Override public void error(Exception e) { exceptions.add(e); }
	 * 
	 * }
	 * 
	 */

	/**
	 * @see com.handge.housingfund.common.service.collection.service.indiacctmanage
	 */
	private DAOBuilder(T dao) {

		this.dao = dao;
	}

	public static <K extends Common, T extends IBaseDAO<K>> DAOBuilder<K, T> instance(T dao) {

		return new DAOBuilder<>(dao);
	}

	public DAOBuilder<K, T> searchFilter(HashMap<String, Object> searchFilter) {

		this.UID = searchFilter.get("UID") == null ? null : (String) searchFilter.get("UID");

		searchFilter.remove("UID");

		this.searchFilter = searchFilter;

		return this;
	}

	public DAOBuilder<K, T> UID(String UID) {

		this.UID = UID;

		return this;
	}

	public DAOBuilder<K, T> pageOption(int pageSize, int page) {

		this.pageSize = pageSize;

		this.page = page;

		return this;
	}

	public DAOBuilder<K, T> orderOption(String orderBy, Order order) {

		this.order = order;

		this.orderBy = orderBy;

		return this;
	}

	public DAOBuilder<K, T> betweenDate(Date start, Date end) {

		this.start = start;

		this.end = end;

		return this;
	}

	public DAOBuilder<K, T> isDeleted(ListDeleted listDeleted) {

		this.listDeleted = listDeleted;

		return this;
	}

	public DAOBuilder<K, T> searchOption(SearchOption searchOption) {

		this.searchOption = searchOption;

		return this;
	}

	public DAOBuilder<K, T> updateEntity(UpateEntity upateEntity) {

		this.upateEntity = upateEntity;

		return this;
	}

	public DAOBuilder<K, T> pageOption(PageRes pageRes,int pageSize, int page) {

		this.pageRes = pageRes;

		this.page = page;

		this.pageSize = pageSize;

		return this;
	}

	public DAOBuilder<K, T> pageOption(String marker,String action,int pageSize) {

		this.marker = marker;

		for(ListAction listAction : ListAction.values()) {

			if(listAction.getCode().equals(action)){

				this.action = listAction;

				break;
			}
		}

		if(this.action == null){

			throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH,"不支持的操作类型");
		}

		this.pageSize = pageSize;

		return this;
	}

	public DAOBuilder<K, T> pageOption(PageRes pageRes,String marker,String pagesize) {

		this.pageRes = pageRes;

		return this;
	}

	public DAOBuilder<K, T> extension(IBaseDAO.CriteriaExtension extension){

		this.extension = extension;

		return this;
	}

	public DAOBuilder<K, T> entity(K entity) {

		if(entity!=null) {

			this.entities = Collections.singletonList(entity);
		}
		return this;
	}

	public DAOBuilder<K, T> entities(List<K> entities){

		if(entities!=null&&entities.size()!=0){
			this.entities = entities;
		}

		return this;
	}

	public interface  ErrorHandler {

		 void  error(Exception e);

	}

	public List<K> getList(ErrorHandler handler) {

		if (this.UID != null) {

			K object = null;

			try {

				object =  this.dao.get(this.UID);

			} catch (Exception e) {

				e.printStackTrace();

				handler.error(e);
			}

			return object == null ? new ArrayList<K>() : Collections.singletonList(object);
		}

		if (page * pageSize != 0) {

			List<K> list = new ArrayList<>();

			try {

				PageResults<K> pageResults = this.extension!=null?this.dao.listWithPage(this.searchFilter, this.start, this.end, this.orderBy==null?"created_at":this.orderBy, this.order==null?Order.DESC:this.order, this.listDeleted, this.searchOption, this.page, this.pageSize,extension):this.dao.listWithPage(this.searchFilter, this.start, this.end, this.orderBy==null?"created_at":this.orderBy, this.order==null?Order.DESC:this.order, this.listDeleted, this.searchOption, this.page, this.pageSize);

				list =  pageResults.getResults();

				if(this.pageRes !=null){

					this.pageRes.setCurrentPage(pageResults.getCurrentPage());

					this.pageRes.setNextPageNo(pageResults.getPageNo());

					this.pageRes.setPageCount(pageResults.getPageCount());

					this.pageRes.setTotalCount(pageResults.getTotalCount());

					this.pageRes.setPageSize(pageResults.getPageSize());
				}

			} catch (Exception e) {

				e.printStackTrace();

				handler.error(e);
			}


			return list;

		}else if(this.marker!=null||this.action!=null){

			List<K> list = new ArrayList<>();

			try {

				PageResults<K> pageResults = this.extension!=null?this.dao.listWithMarker(this.searchFilter, this.start, this.end, this.orderBy==null?"created_at":this.orderBy, this.order==null?Order.DESC:this.order, this.listDeleted, this.searchOption, this.marker, this.pageSize,this.action,extension):this.dao.listWithMarker(this.searchFilter, this.start, this.end, this.orderBy==null?"created_at":this.orderBy, this.order==null?Order.DESC:this.order, this.listDeleted, this.searchOption, this.marker, this.pageSize,this.action);

				list =  pageResults.getResults();


			} catch (Exception e) {

				e.printStackTrace();

				handler.error(e);
			}


			return list;
		} else {

			List<K> list = new ArrayList<>();
			try {

				list = this.extension!=null ? this.dao.list(this.searchFilter, start, end, orderBy, order, listDeleted, searchOption, extension): this.dao.list(this.searchFilter, start, end, orderBy, order, listDeleted, searchOption);

			} catch (Exception e) {

				e.printStackTrace();

				handler.error(e);
			}
			return list;
		}

	}

	public K getObject(ErrorHandler handler) {

		return CollectionUtils.getFirst(this.getList(handler));
	}

	public long getCount(ErrorHandler handler){

		long count = 0;

		try {

			count = this.extension!=null?this.dao.count(this.searchFilter, start, end, orderBy, order, listDeleted, searchOption,extension):this.dao.count(this.searchFilter, start, end, orderBy, order, listDeleted, searchOption);

		}catch (Exception e){

			e.printStackTrace();

			handler.error(e);
		}

		return count;
	}

	/* handler 执行和操作执行在同一线程 不用做线程同步 */
	public void save(ErrorHandler handler) {

		try {

			for(K entity :this.entities) {

				if (entity != null && entity.getId() == null) {

					this.dao.save(entity);

				} else if(entity != null){

					if(this.upateEntity!=null){

						copyProperties(upateEntity,entity);
					}

					this.dao.update(entity);
				}
			}
		} catch (Exception e) {

			e.printStackTrace();

			handler.error(e);
		}
	}

	public String saveThenFetchId(ErrorHandler handler) {

		return CollectionUtils.getFirst(saveThenFetchIds(handler));
	}

	public K saveThenFetchObject(ErrorHandler handler) {

		return CollectionUtils.getFirst(saveThenFetchObjects(handler));

	}

	public ArrayList<String> saveThenFetchIds(ErrorHandler handler) {

		ArrayList<String> id = new ArrayList<>();

		try {

			for(K entity:this.entities) {

				if (entity != null && entity.getId() == null) {

					id.add(this.dao.save(entity));

				} else if (entity != null) {

					id.add(entity.getId());

					this.dao.update(entity);

//				((BaseDAO)this.dao).getSession().flush();
//
//				((BaseDAO)this.dao).getSession().refresh(this.entity);

				}
			}
		} catch (Exception e) {

			e.printStackTrace();

			handler.error(e);
		}

		return id;
	}

	public ArrayList<K> saveThenFetchObjects(ErrorHandler handler) {

		ArrayList<String> ids = saveThenFetchIds(handler);


		ArrayList<K> objects = new ArrayList<K>();

		try {

			for(String id :ids) {

				objects.add(this.dao.get(id));
			}
		}catch (Exception e){

			e.printStackTrace();

			handler.error(e);
		}

		return objects;

	}

	public void delete(ErrorHandler handler) {

		try {


			for(K entity:this.entities) {

				if (entity != null && entity.getId() != null) {

					this.dao.delete(entity);

				}
			}

			if (this.searchFilter != null&&this.searchFilter.size()!=0) {

				List<K> list = this.getList(new ErrorHandler() {

					@Override
					public void error(Exception e) {
						handler.error(e);
					}
				});

				for (K object : list) {

					this.dao.delete(object);
				}
			}

		} catch (Exception e) {

			e.printStackTrace();

			handler.error(e);
		}

	}

	public void deleteForever(ErrorHandler handler) {

		try {


			for(K entity:this.entities) {

				if (entity != null && entity.getId() != null) {

					this.dao.deleteForever(entity);

				}
			}

			if (this.searchFilter != null&&this.searchFilter.size()!=0) {

				List<K> list = this.getList(new ErrorHandler() {

					@Override
					public void error(Exception e) {
						handler.error(e);
					}
				});

				for (K object : list) {

					this.dao.deleteForever(object);
				}
			}

		} catch (Exception e) {

			e.printStackTrace();

			handler.error(e);
		}

	}

	public boolean isUnique(ErrorHandler handler){

		K object = this.getObject(new ErrorHandler() {

			@Override
			public void error(Exception e) {
				e.printStackTrace();
				handler.error(e);
			}
		});

		return object == null;
	}

	private static void copyProperties(Object source, Object target) throws BeansException {
		Class<?> editable = null;

		String[] ignoreProperties = null;

		Assert.notNull(source, "Source must not be null");
		Assert.notNull(target, "Target must not be null");

		Class<?> actualEditable = target.getClass();
		if (editable != null) {
			if (!editable.isInstance(target)) {
				throw new IllegalArgumentException("Target class [" + target.getClass().getName() +
						"] not assignable to Editable class [" + editable.getName() + "]");
			}
			actualEditable = editable;
		}
		PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
		List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

		List<String> needsConvert = new ArrayList<>();

		for (PropertyDescriptor targetPd : targetPds) {
			Method writeMethod = targetPd.getWriteMethod();
			if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
				PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
				if (sourcePd != null) {
					Method readMethod = sourcePd.getReadMethod();

					if (readMethod != null && ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {



						try {
							if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
								readMethod.setAccessible(true);
							}
							Object value = readMethod.invoke(source);
							if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
								writeMethod.setAccessible(true);
							}
							writeMethod.invoke(target, value);



							if(value!=null&&(targetPd.getPropertyType().getName().contains(target.getClass().getName())||!BeanUtils.isSimpleProperty(targetPd.getPropertyType()))){


								Object o = targetPd.getPropertyType().newInstance();

								if(targetPd.getPropertyType().getName().equals(Object.class.getName())&&value!=null){

									Class realClass = value.getClass();

									while (realClass.getName().contains("$")){
										realClass = realClass.getSuperclass();
									}

									o = realClass.newInstance();
								}

								DAOBuilder.copyProperties(value,o);

								writeMethod.invoke(target, o);

								needsConvert.add(targetPd.getPropertyType().getName());

								if(value instanceof Collection &&value!=null){//&&((Collection)value).size()!=0){

									Object finalO = o;
									((Collection)value).forEach(new Consumer() {
										@Override
										public void accept(Object o1) {

											try {

												Class realClass = o1.getClass();

												while (realClass.getName().contains("$")){
													realClass = realClass.getSuperclass();
												}
												Object element = realClass.newInstance();
												if(o1==null||BeanUtils.isSimpleProperty(o1.getClass())){
													element = o1;
												}else {
													DAOBuilder.copyProperties(o1,element);
												}
												((Collection) finalO).add(element);
											} catch (InstantiationException e) {
												e.printStackTrace();
											} catch (IllegalAccessException e) {
												e.printStackTrace();
											};

										}
									});
								}


							}
						}
						catch (Throwable ex) {
							throw new FatalBeanException(

									"Could not copy property '" + targetPd.getName() + "' from source to target", ex);
						}
					}
				}
			}
		}
	}

}