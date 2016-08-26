package com.gettec.fsnip.fsn.service.erp.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.gettec.fsnip.fsn.dao.erp.OrderTypeDAO;
import com.gettec.fsnip.fsn.dao.erp.OutOfBillDAO;
import com.gettec.fsnip.fsn.dao.erp.ReceivingNoteDAO;
import com.gettec.fsnip.fsn.dao.erp.buss.FlittingOrderDAO;
import com.gettec.fsnip.fsn.dao.erp.buss.InStorageRecordDAO;
import com.gettec.fsnip.fsn.dao.erp.buss.OutStorageRecordDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.OrderType;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.OrderTypeService;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

@Service("orderTypeService")
public class OrderTypeServiceImpl extends BaseServiceImpl<OrderType, OrderTypeDAO> 
		implements OrderTypeService {
	@Autowired private OrderTypeDAO orderTypeDAO;
	@Autowired private InStorageRecordDAO inStorageRecordDAO;
	@Autowired private OutStorageRecordDAO outStorageRecordDAO;
	@Autowired private FlittingOrderDAO flittingOrderDAO;
	@Autowired private OutOfBillDAO outOfBillDAO;
	@Autowired private ReceivingNoteDAO receivingNoteDAO;

	/**
	 * 新增单别
	 * @param vo
	 * @param organization
	 * @param userName
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean add(OrderType ordertype, Long organization) throws ServiceException {
		try {
			long count = orderTypeDAO.count(ordertype.getOt_belong_module(), ordertype.getOt_belong_order(), 
					organization, ordertype.getOt_type());
			if(count < 1) {
				create(ordertype);
				return true;
			}
			return false;
		} catch (DaoException dex) {
			throw new ServiceException("[DaoException]OrderTypeServiceImpl.create()-->", dex.getException());
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException]OrderTypeServiceImpl.create()-->", sex.getException());
		}
	}

	/**
	 * 删除单别
	 * @param ordertype  
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean remove(OrderType ordertype, Long organization) throws ServiceException {
		try {
			OrderType orig_orderType = findById(ordertype.getOt_id());
			Object[] params = new Object[]{organization, orig_orderType.getOt_type() + "%"};
			String belongModule = orig_orderType.getOt_belong_module(); // 所属模块
			String belongOrder = orig_orderType.getOt_belong_order(); // 所属单据
			String condition;
			long count;
			if("库存模块".equals(belongModule) && "商品入库管理".equals(belongOrder)) {
				condition = " WHERE organization = ?1 AND no like ?2"; //入库
				count = inStorageRecordDAO.count(condition, params);
			} else if("库存模块".equals(belongModule) && "商品出库管理".equals(belongOrder)) {
				condition = " WHERE organization = ?1 AND no like ?2"; //出库
				count = outStorageRecordDAO.count(condition, params);
			} else if("库存模块".equals(belongModule) && "调拨管理".equals(belongOrder)) {
				condition = " WHERE organization = ?1 AND no like ?2"; //调拨
				count = flittingOrderDAO.count(condition, params);
			} else if("销售模块".equals(belongModule) && "出货单/发货单管理".equals(belongOrder)) {
				condition = " WHERE organization = ?1 AND outOfBillNo like ?2"; //出货单
				count = outOfBillDAO.count(condition, params);
			} else {
				condition = " WHERE organization = ?1 AND re_num like ?2"; //收货单
				count = receivingNoteDAO.count(condition, params);
			}
			if(count > 0) {
				return false; // 有关联，不可删除
			}
			delete(orig_orderType);
			return true;
		} catch (JPAException jpae) {
			throw new ServiceException("[JPAException]OrderTypeServiceImpl.remove()-->", jpae.getException());
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException]OrderTypeServiceImpl.remove()-->", sex.getException());
		}		
	}
	
	/**
	 * 分页查询单别
	 * @param receivNote
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public PagingSimpleModelVO<OrderType> getPaging(int page, int size, 
			String keywords, Long organization) throws ServiceException {
		try {
			PagingSimpleModelVO<OrderType> result = new PagingSimpleModelVO<OrderType>();
			String configure = getConfigure(null, organization);
			result.setListOfModel(orderTypeDAO.getListByPage(page, size, configure));
			result.setCount(orderTypeDAO.count(configure));
			return result;
		} catch (JPAException jpae) {
			throw new ServiceException("OrderTypeServiceImpl.getOrderTypefilter()-->", jpae.getException());
		}
	}

	@Override
	public List<OrderType> getOrdrTypeByModuleAndOrder(String module,
			String order, Long organization) {
		String configure = " AND organization = " + organization;
		return orderTypeDAO.getOrdrTypeByModuleAndOrder(module, order, configure);
	}

	/**
	 * 商品入库
	 * @param no
	 * @return
	 */
	@Override
	public String getAutoCodeNumStart(String no) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String date = formatter.format(new java.util.Date());
		String noStart = no + "-" +date;
		return noStart;
	}
	
	/**
	 * 根据已有的最大编号，自动加1后，获取最新最大编号
	 * @param no
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	public String getAutoCodeNum(String no) throws ServiceException {
		String noMax = null;
		try {
			String StartNo = no.substring(0,(no.length()-4));
			int initNum = Integer.parseInt(no.substring(no.length()-4)); // 1
			initNum = initNum+1; // 2
			String Snum = "0000" +initNum; // 00002
			String EndNum = Snum.substring(Snum.length()-4); // 0002
			noMax = StartNo + EndNum;
			return noMax;
		} catch (Exception e) {
			throw new ServiceException("OrderTypeServiceImpl.getAutoCodeNum() 根据已有的最大编号，自动加1后，获取最新最大编号,出现异常！", e);
		}
	}

	/**
	 * 根据过滤条件查询当前登录企业的客户列表总数
	 * @param page
	 * @param pageSize
	 * @param condition
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 * @throws DaoException 
	 */
	public PagingSimpleModelVO<OrderType> getOrderTypefilter(int page, int pageSize,String condition,
				Long organization) throws ServiceException {
		try {
			PagingSimpleModelVO<OrderType> result = new PagingSimpleModelVO<OrderType>();
			String configure = getConfigure(condition, organization);
			result.setListOfModel(orderTypeDAO.getListByPage(page, pageSize, configure));
			result.setCount(orderTypeDAO.count(configure));
			return result;
		} catch (JPAException jpae) {
			throw new ServiceException("OrderTypeServiceImpl.getOrderTypefilter()-->", jpae.getException());
		}
	}
	
	/**
	 * 按过滤条件拼接where字符串
	 * @param configure 页面过滤条件
	 * @param organization 
	 * @return
	 */
	private String getConfigure(String configure, Long organization) {
		if(organization == null){ return null; }
	    String new_configure = " WHERE organization = " + organization;
	    if(configure != null){
	    	String filter[] = configure.split("@@");
		    for(int i=0;i<filter.length;i++){
		    	String filters[] = filter[i].split("@");
		    	try {
		    		new_configure += " AND " + splitJointConfigure(filters[0],filters[1],filters[2]);
				} catch (Exception e) {
					e.printStackTrace();
				}
		    }
	    }
	    return new_configure;
	}
	
	private String splitJointConfigure(String field,String mark,String value){
		try {
			value = URLDecoder.decode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(field.equals("ot_type")){
			return FilterUtils.getConditionStr("ot_type",mark,value,true);
		}
		if(field.equals("ot_describe")){
			return FilterUtils.getConditionStr("ot_describe",mark,value,true);
		}
		if(field.equals("ot_belong_module")){
			return FilterUtils.getConditionStr("ot_belong_module",mark,value,true);
		}
		if(field.equals("ot_belong_order")){
			return FilterUtils.getConditionStr("ot_belong_order",mark,value,true);
		}
		if(field.equals("ot_id")){
			return FilterUtils.getConditionStr("e.ot_id",mark,value);
		}
		return null;
	}

	@Override
	public OrderTypeDAO getDAO() {
		return orderTypeDAO;
	}
}
