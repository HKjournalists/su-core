package com.gettec.fsnip.fsn.service.erp;

import java.util.List;

import com.gettec.fsnip.fsn.dao.erp.OrderTypeDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.OrderType;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

public interface OrderTypeService extends BaseService<OrderType, OrderTypeDAO> {
	/*
	 * author:cgw
	 * @parameter1 module;
	 * @parameter2 order;
	 * 只能传入以下值 moudule ：销售模块 {order：销售订单/合同管理;销售单/出货通知单管理;送检单管理;出货单/发货单管理;销退换货管理}
	 * 					      库存模块{order:商品出/入库管理;调拨管理}
	 * 					      采购模块{order:请购单管理;采购单管理;收货单管理}
	 * describe:通过对应模块以及单据获取单别
	 * */
	public List<OrderType> getOrdrTypeByModuleAndOrder(String module,String order,Long organization);
	public String getAutoCodeNumStart(String Num);
	public String getAutoCodeNum(String no) throws ServiceException;
	
	/**
	 * 过滤
	 * @param page
	 * @param pageSize
	 * @param configure
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	public PagingSimpleModelVO<OrderType> getOrderTypefilter(int page, int pageSize,String configure, Long organization) throws ServiceException;
	
	/**
	 * 添加单别
	 * @author Liang Zhou
	 * 2014-10-27
	 * @param ordertype
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	public boolean add(OrderType ordertype, Long organization) throws ServiceException;
	
	/**
	 * 删除单别
	 * @author Liang Zhou
	 * 2014-10-27
	 * @param ordertype
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	public boolean remove(OrderType ordertype, Long organization) throws ServiceException;
}
