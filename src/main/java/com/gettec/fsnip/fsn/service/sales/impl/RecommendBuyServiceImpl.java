package com.gettec.fsnip.fsn.service.sales.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.sales.RecommendBuyDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.sales.RecommendBuy;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.sales.RecommendBuyService;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.fsn.util.sales.SalesUtil;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.sales.RecommendBuyVO;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

/**
 * 推荐购买方式ServiceImpl
 * @author tangxin 2015/04/24
 *
 */
@Service(value="recommendBuyService")
public class RecommendBuyServiceImpl extends
		BaseServiceImpl<RecommendBuy, RecommendBuyDAO> implements
		RecommendBuyService {

	@Autowired RecommendBuyDAO recommendBuyDAO;
	@Autowired private BusinessUnitService businessUnitService;
	
	/**
	 * 创建 or 更新 推荐购买方式
	 * @author tangxin 2015-04-27
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public RecommendBuyVO save(RecommendBuyVO recommendBuyVO,
			AuthenticateInfo info, boolean isNew) throws ServiceException{
		try{
			if(recommendBuyVO == null) {
				return recommendBuyVO;
			}
			RecommendBuy recommendBuy = null;
			if(!isNew) {
				recommendBuy = getDAO().findById(recommendBuyVO.getId());
			}
			recommendBuy = recommendBuyVO.toEntity(recommendBuy);
			recommendBuy.setUpdateTime(new Date());
			recommendBuy.setUpdateUser(info.getUserName());
			if(isNew) {
				Long busId = businessUnitService.findIdByOrg(info.getOrganization());
				recommendBuy.setCreateTime(new Date());
				recommendBuy.setCreateUser(info.getUserName());
				recommendBuy.setGuid(SalesUtil.createGUID());
				recommendBuy.setBusinessId(busId);
				recommendBuy.setOrganization(info.getOrganization());
				recommendBuy.setDelStatus(0);
				create(recommendBuy);
				recommendBuyVO.setId(recommendBuy.getId());
			}else{
				update(recommendBuy);
			}
		}catch(Exception e){
			throw new ServiceException(e.getMessage(), e);
		}
		return recommendBuyVO;
	}
	
	/**
	 * 根据筛选条件统计 推荐购买方式 数量
	 * @author tangxin 2015-04-27
	 */
	@Override
	public long countByConfigure(Long organization, String configure)
			throws ServiceException {
		try{
			return this.getDAO().countByConfigure(organization, getConfigure(configure));
		}catch(DaoException daoe){
			throw new ServiceException(daoe.getMessage(), daoe.getException());
		}
	}

	/**
	 * 根据推荐购买方式名称统计 数量 验证名称是否重复使用
	 * @author tangxin 2015-04-29
	 */
	@Override
	public long countByName(String name, Long organization, Long id) throws ServiceException {
		try{
			return this.getDAO().countByName(name, organization, id);
		}catch(DaoException daoe){
			throw new ServiceException(daoe.getMessage(), daoe.getException());
		}
	}

	/**
	 * 根据企业id获取对应的推荐购买方式
	 * @author HY
	 * Create Date 2015-05-05
	 */
	@Override
	public List<RecommendBuyVO> getEntityByBusId(Long busId) throws ServiceException {
		try {
			return this.getDAO().getEntityByBusId(busId);
		} catch (DaoException daoe) {
			throw new ServiceException(daoe.getMessage(), daoe.getException());
		}
	}

	/**
	 * 根据筛选条件分页查询 推荐购买方式
	 * @author tangxin 2015-04-27
	 */
	@Override
	public List<RecommendBuyVO> getListByOrganizationWithPage(Long organization,
			String configure, Integer page, Integer pageSize)
			throws ServiceException {
		try{
			return this.getDAO().getListByOrganizationWithPage(organization, getConfigure(configure), page, pageSize);
		}catch(DaoException daoe){
			throw new ServiceException(daoe.getMessage(), daoe.getException());
		}
	}

	/**
	 * 删除 推荐购买方式 (假删除)
	 * @author tangxin 2015-04-27
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ResultVO removeRecommendBuyById(Long buywayId, AuthenticateInfo info) throws ServiceException {
		ResultVO resultVO = new ResultVO();
		if(buywayId == null) {
			resultVO.setStatus("false");
			resultVO.setErrorMessage("删除失败,被删除网点的id为空！");
			return resultVO;
		}
		RecommendBuy orig_buyWay = findById(buywayId);
		if(orig_buyWay != null) {
			orig_buyWay.setUpdateTime(new Date());
			orig_buyWay.setUpdateUser(info.getUserName());
			orig_buyWay.setDelStatus(1);
			update(orig_buyWay);
		} else {
			resultVO.setStatus("false");
			resultVO.setErrorMessage("删除失败,被删除的网点不存在！");
		}
		return resultVO;
	}
	
	/**
	 * 拼接configure条件（纯sql语句）
	 * @author tangxin 2015-04-27
	 */
	private String getConfigure(String configure) throws ServiceException{
		String new_configure = "";
		if(configure == null) {
			return " WHERE 1=1 ";
		}
		String filter[] = configure.split("@@");
		for(int i=0;i<filter.length;i++){
			String filters[] = filter[i].split("@");
			if(filters.length > 3){
				try {
					String config = splitJointConfigure(filters[0],filters[1],filters[2], true);
					if(config==null){
						continue;
					}
					if(i==0){
						new_configure = new_configure + " WHERE " + config;
					}else{
						new_configure = new_configure +" AND " + config;
					}
				} catch (Exception e) {
				e.printStackTrace();
				}
			}
		}
		new_configure = (new_configure.equals("") ? " WHERE 1=1 " : new_configure);
	    return new_configure;
	}
	
	/**
	 * 分割页面的过滤信息
     * @author tangxin 2015-04-27
	 */
	private String splitJointConfigure(String field, String mark, String value, boolean isIgnoreBrand) throws ServiceException{
		try {
			value = URLDecoder.decode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new ServiceException("【Service-Error】数据筛选时，利用utf-8解码筛选条件时出现异常！",e);
		}
		if(field.equals("id")){
			return FilterUtils.getConditionStr("buyway.id",mark,value);
		}
		if(field.equals("name")){
			return FilterUtils.getConditionStr("buyway.name",mark,value);
		}
		if(field.equals("way")){
			return FilterUtils.getConditionStr("buyway.way",mark,value);
		}
		return null;
	}
	
	@Override
	public RecommendBuyDAO getDAO() {
		return recommendBuyDAO;
	}

}
