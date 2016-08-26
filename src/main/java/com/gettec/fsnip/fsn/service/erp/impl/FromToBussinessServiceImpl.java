package com.gettec.fsnip.fsn.service.erp.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.erp.FromToBussinessDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.buss.FromToBussiness;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.FromToBussinessService;
import com.gettec.fsnip.fsn.service.product.ProductService;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.SystemDefaultInterface;
import com.gettec.fsnip.fsn.vo.ResultVO;

@Service("fromToBusService")
public class FromToBussinessServiceImpl extends BaseServiceImpl<FromToBussiness, FromToBussinessDAO> 
		implements FromToBussinessService {
	@Autowired private FromToBussinessDAO fromToBusDAO;
	@Autowired private BusinessUnitService businessUnitService;
	@Autowired private ProductService productService;

	@Override
	public FromToBussinessDAO getDAO() {
		return fromToBusDAO;
	}
	
	/**
	 * 保存销往企业
	 * @param proId 产品id
	 * @param fromBusId 出货商id
	 * @param toBusIds 收货商ids
	 * @return void
	 * @author Zhanghui 2015/4/8
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(ResultVO resultVO, Long proId, Long fromBusId, String[] toBusIdStrs) throws ServiceException{
		if(proId==null || fromBusId==null){ return; }
		try {
			/* 1. 将toBusIdStrs的类型转为List<Long> */
			List<Long> nowToBusIds = new ArrayList<Long>();
			for(int i=0; i<toBusIdStrs.length; i++){
				if(toBusIdStrs[i].equals("")){continue;}
				nowToBusIds.add(Long.parseLong(toBusIdStrs[i]));
			}
			
			String has_dealer_bus_names = ""; // 已经有供应商的商超
			String no_dealer_bus_names = "";  // 成功添加的商超
			
			/* 2. 保存页面上添加的销往企业 */
			for(Long toBusId : nowToBusIds){
				String to_bus_name = businessUnitService.findNameById(toBusId);
			
			    /*Long orig_fromBusId = getDAO().findFromBusId(proId, toBusId);
				
				if(orig_fromBusId!=null){
					if(orig_fromBusId.equals(fromBusId)){
						no_dealer_bus_names += "商超【" + to_bus_name + "】;";
						continue;
					}else{
						String bus_name = businessUnitService.findNameById(orig_fromBusId);
						has_dealer_bus_names += "商超【" + to_bus_name + "】 已经选择了供应商 " + "【" + bus_name + "】;";
						continue;
					}
				}*/
				
				FromToBussiness orig_fromToBus = getDAO().findById(proId, fromBusId, toBusId);
				if(orig_fromToBus == null){
					/* 2.1 新增 */
					FromToBussiness fromToBus = new FromToBussiness(proId, fromBusId, toBusId, false);
					create(fromToBus);
				}else if(orig_fromToBus.isDel()){
					/* 2.2 更新 */
					orig_fromToBus.setDel(false);
					update(orig_fromToBus);
				}
				
				no_dealer_bus_names += "商超【" + to_bus_name + "】;";
			}
			/* 3. 从数据库中删除，已经被用户在页面删除的销往企业 */
			List<Long> origToBusIds = getDAO().finAllToBusIds(proId, fromBusId, false);
			Set<Long> removes = getListOfRemoves(origToBusIds, nowToBusIds);
			for(Long removeId : removes){
				FromToBussiness orig_fromToBus = getDAO().findById(proId, fromBusId, removeId);
				orig_fromToBus.setDel(true);
				update(orig_fromToBus);
			}
			
			if(!"".equals(has_dealer_bus_names)){
				resultVO.setErrorMessage(has_dealer_bus_names + "  您添加失败！");
			}
			
			if(!"".equals(no_dealer_bus_names)){
				resultVO.setMessage("您成功将 " + no_dealer_bus_names + " 添加为销往客户！");
			}
			//如果是销往为年货会的,那么就是年货产品
			if(nowToBusIds.contains(Long.valueOf(PropertiesUtil.getProperty(SystemDefaultInterface.NIANHUOHUI_CUSTOMER_ID)))){
				productService.updateSpeicalProduct(proId, true);
			}
			
		} catch (DaoException dex) {
			throw new ServiceException("FromToBussinessServiceImpl.save()-->" + dex.getMessage(), dex.getException());
		} catch (Exception e) {
			throw new ServiceException("FromToBussinessServiceImpl.save()", e);
		}
	}
	
	/**
	 * 对比前后的资源列表，获取删除的集合
	 */
	private Set<Long> getListOfRemoves(Collection<Long> origToBusIds,
			Collection<Long> nowToBusIds) {
		if (origToBusIds == null) {
			return null;
		}
		
		Set<Long> removes = new HashSet<Long>();
		for (Long toBusId : origToBusIds) {
			if (toBusId != null
					&& !nowToBusIds.contains(toBusId)) {
				removes.add(toBusId);
			}
		}
		return removes;
	}

	/**
	 * 获取销往企业名称
	 * @author Zhanghui 2015/4/8
	 */
	@Override
	public String findNamestrs(Long proId, Long fromBusId, boolean isDel) throws ServiceException {
		try {
			return getDAO().findNamestrs(proId, fromBusId, isDel);
		} catch (DaoException dex) {
			throw new ServiceException("FromToBussinessServiceImpl.findIdstrs()-->"  + dex.getMessage(), dex.getException());
		}
	}
	
	/**
	 * 获取销往企业ids
	 * @author Zhanghui 2015/4/8
	 */
	@Override
	public String findIdstrs(Long proId, Long fromBusId, boolean isDel) throws ServiceException {
		try {
			List<Long> ids = getDAO().finAllToBusIds(proId, fromBusId, isDel);
			String strIds = "";
			for(Long id : ids){
				strIds += id + ",";
			}
			return strIds;
		} catch (DaoException dex) {
			throw new ServiceException("FromToBussinessServiceImpl.findIdstrs()-->"  + dex.getMessage(), dex.getException());
		}
	}
	
	/**
	 * 根据供应商和收货商id，获取产品id集合
	 * @author Zhanghui 2015/4/9
	 */
	@Override
	public String findAllProIdStrsByPage(Long fromBusId, Long toBusId, boolean isDel, int page, int pageSize,String configure) throws ServiceException {
		try {
			List<Long> ids = getDAO().finAllProIdsByPage(fromBusId, toBusId, isDel, page, pageSize,configure);
			String strIds = "";
			int size = ids.size()-1;
			for(int i=0; i<ids.size(); i++){
				if(i != size){
					strIds += ids.get(i) + ",";
					continue;
				}
				strIds += ids.get(i);
			}
			return strIds;
		} catch (DaoException dex) {
			throw new ServiceException("FromToBussinessServiceImpl.finAllProIds()-->"  + dex.getMessage(), dex.getException());
		}
	}
	
	/**
	 * 获取某供应商提供给当前登录的商超的待处理报告的产品idStrs
	 * @author Zhanghui 2015/5/4
	 */
	@Override
	public String findAllProIdStrsOfOnHandle(Long fromBusId, Long toBusId, boolean isDel, int page, int pageSize,String configure) throws ServiceException {
		try {
			List<Long> ids = getDAO().findAllProIdStrsOfOnHandle(fromBusId, toBusId, isDel, page, pageSize,configure);
			String strIds = "";
			int size = ids.size()-1;
			for(int i=0; i<ids.size(); i++){
				if(i != size){
					strIds += ids.get(i) + ",";
					continue;
				}
				strIds += ids.get(i);
			}
			return strIds;
		} catch (DaoException dex) {
			throw new ServiceException("FromToBussinessServiceImpl.finAllProIds()-->"  + dex.getMessage(), dex.getException());
		}
	}
	
	/**
	 * 根据供应商和收货商id，获取产品id集合
	 * @author Zhanghui 2015/4/9
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateDelFlag(Long proId, Long fromBusId, Long toBusId, boolean isDel) throws ServiceException {
		try {
			getDAO().updateDelFlag(proId, fromBusId, toBusId, isDel);
		} catch (DaoException dex) {
			throw new ServiceException("FromToBussinessServiceImpl.updateDelFlag()-->"  + dex.getMessage(), dex.getException());
		}
	}

	/**
	 * 根据供应商和收货商id，获取产品数量
	 * @author Zhanghui 2015/4/10
	 */
	@Override
	public Long counts(Long fromBusId, Long toBusId, boolean isDel) throws ServiceException {
		try {
			return getDAO().counts(fromBusId, toBusId, isDel);
		} catch (DaoException dex) {
			throw new ServiceException("FromToBussinessServiceImpl.counts()-->"  + dex.getMessage(), dex.getException());
		}
	}
	
	/**
	 * 功能描述：获取供货给当前商超的供应商数量
	 * @author Zhanghui 2015/7/7
	 * @throws ServiceException 
	 */
	@Override
	public long count(Long productId, Long toBusId) throws ServiceException {
		try {
			return getDAO().count(productId, toBusId);
		} catch (DaoException dex) {
			throw new ServiceException("FromToBussinessServiceImpl.count()-->"  + dex.getMessage(), dex.getException());
		}
	}
	
	/**
	 * 获取某供应商提供给当前登录的商超的所有产品总数<br>
	 * isDel:true  已删除<br>
	 * isDel:false 未删除
	 * @author Zhanghui 2015/6/17
	 */
	@Override
	public Long countsOfProduct2Super(Long fromBusId, Long toBusId, boolean isDel,String configure) throws ServiceException {
		try {
			return getDAO().countsOfProduct2Super(fromBusId, toBusId, isDel,configure);
		} catch (DaoException dex) {
			throw new ServiceException("FromToBussinessServiceImpl.countsOfProduct2Super()-->"  + dex.getMessage(), dex.getException());
		}
	}
	
	/**
	 * 根据供应商和收货商id，获取产品数量
	 * @author Zhanghui 2015/4/10
	 */
	@Override
	public Long countsOfOnHandle(Long fromBusId, Long toBusId, boolean isDel,String configure) throws ServiceException {
		try {
			return getDAO().countsOfOnHandle(fromBusId, toBusId, isDel,configure);
		} catch (DaoException dex) {
			throw new ServiceException("FromToBussinessServiceImpl.counts()-->"  + dex.getMessage(), dex.getException());
		}
	}

	/**
	 * 根据产品id和商超ID获取供应商
	 * @author LongXianZhen 2015/5/6
	 */
	@Override
	public FromToBussiness getFromBuByproIdAndToBuId(Long productId, Long toBusId)
			throws ServiceException {
		try {
			return getDAO().getFromBuByproIdAndToBuId(productId,toBusId);
		} catch (DaoException dex) {
			throw new ServiceException("FromToBussinessServiceImpl.getFromBuByproIdAndToBuId()-->"  + dex.getMessage(), dex.getException());
		}
	}
	
	/**
	 * 功能描述：获取某供应商提供给某商超的产品的记录
	 * @author Zhanghui 2015/6/23
	 * @throws ServiceException 
	 */
	@Override
	public long count(Long productId, Long fromBusId, Long toBusId) throws ServiceException {
		try {
			return getDAO().count(productId, fromBusId, toBusId);
		} catch (DaoException e) {
			throw new ServiceException("FromToBussinessServiceImpl.find()-->"  + e.getMessage(), e.getException());
		}
	}

	/**
	 * 功能描述：根据产品id，商超企业id查找供应商企业id
	 * @author Zhanghui 2015/7/1
	 * @throws ServiceException 
	 */
	@Override
	public Long findFromBusId(Long productId, Long toBusId) throws ServiceException {
		try {
			return getDAO().findFromBusId(productId, toBusId);
		} catch (DaoException e) {
			throw new ServiceException("FromToBussinessServiceImpl.findFromBusId()-->"  + e.getMessage(), e.getException());
		}
	}

	/**
	 * 功能描述：获取供货给当前商超的供应商名称
	 * @author Zhanghui 2015/7/7
	 * @throws ServiceException 
	 */
	@Override
	public List<String> getListOfFromBusName(Long productId, Long toBusId) throws ServiceException {
		try {
			return getDAO().getListOfFromBusName(productId, toBusId);
		} catch (DaoException e) {
			throw new ServiceException("FromToBussinessServiceImpl.getListOfFromBusName()-->"  + e.getMessage(), e.getException());
		}
	}

	@Override
	public Product getProbyId(Long fromBusId, Long toBusId,Long proId, boolean isDel)
			throws ServiceException {
		try {
			return getDAO().getProbyId(fromBusId, toBusId, proId, isDel);
		} catch (DaoException dex) {
			throw new ServiceException("FromToBussinessServiceImpl.getProbyId()-->"  + dex.getMessage(), dex.getException());
		}
	}
}
