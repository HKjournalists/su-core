package com.gettec.fsnip.fsn.service.sales.impl;

import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_SALES_PROMOTION_PATH;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.sales.PromotionCaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.sales.PromotionCase;
import com.gettec.fsnip.fsn.model.sales.SalesResource;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.sales.PromotionCaseService;
import com.gettec.fsnip.fsn.service.sales.SalesResourceService;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.sales.SalesUtil;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.sales.PromotionCaseVO;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

@Service(value="promotionCaseService")
public class PromotionCaseServiceImpl extends BaseServiceImpl<PromotionCase,PromotionCaseDAO> implements PromotionCaseService {

    @Autowired private PromotionCaseDAO promotionCaseDAO;
    @Autowired private SalesResourceService salesResourceService;
    @Autowired private BusinessUnitService businessUnitService;
    
    /**
     * 新增 or 更新 促销活动 信息
     * @author tangxin 2015-05-03
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ResultVO save(PromotionCaseVO promotionVO, AuthenticateInfo info, boolean isNew) throws ServiceException{
    	ResultVO resultVO = new ResultVO();
    	try{
			if(promotionVO == null) {
				return resultVO;
			}
			PromotionCase promotion = null;
			if(!isNew) {
				promotion = getDAO().findById(promotionVO.getId());
			}
			promotion = promotionVO.toEntity(promotion);
			promotion.setUpdateTime(new Date());
			promotion.setUpdateUser(info.getUserName());
			if(isNew) {
				Long busId = businessUnitService.findIdByOrg(info.getOrganization());
				promotion.setCreateTime(new Date());
				promotion.setCreateUser(info.getUserName());
				promotion.setGuid(SalesUtil.createGUID());
				promotion.setBusinessId(busId);
				promotion.setOrganization(info.getOrganization());
				promotion.setDelStatus(0);
				create(promotion);
				promotionVO.setId(promotion.getId());
			}else{
				update(promotion);
			}
			List<SalesResource> salesResList = promotionVO.getResource();
			if(salesResList != null) {
				String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_SALES_PROMOTION_PATH);
				resultVO = salesResourceService.saveListResourceByFtppath(salesResList,promotion.getGuid(),info,ftpPath);
			} else {
				salesResourceService.removeResourceByGUID(promotion.getGuid(), info);
			}
			promotionVO.setResource(salesResList);
		}catch(Exception e){
			throw new ServiceException(e.getMessage(), e);
		}
		return resultVO;
	}
    
    /**
   	 * 根据筛选条件统计促销活动数量
   	 * @author tangxin 2015-05-03
   	 */
   	@Override
   	public long countByConfigure(Long organization, String configure) throws ServiceException {
   		try{
   			return this.getDAO().countByConfigure(organization, getConfigure(configure));
   		}catch(DaoException daoe){
   			throw new ServiceException(daoe.getMessage(), daoe.getException());
   		}
   	}
    
	/**
   	 * 根据促销活动名称统计数量，验证名称时候重复用
   	 * @author tangxin 2015-05-03
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
   	 * 根据筛选条件分页查询促销活动信息
   	 * @author tangxin 2015-05-03
   	 */
   	@Override
   	public List<PromotionCaseVO> getListByOrganizationWithPage(Long organization, String configure, Integer page, Integer pageSize)
   			throws ServiceException {
   		try{
   			return this.getDAO().getListByOrganizationWithPage(organization, getConfigure(configure), page, pageSize);
   		}catch(DaoException daoe){
   			throw new ServiceException(daoe.getMessage(), daoe.getException());
   		}
   	}
   	
   	/**
   	 * 根据id删除销活动信息信息(假删除)
   	 * @author tangxin 2015-05-03
   	 */
   	@Override
   	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
   	public ResultVO removeById(Long id, AuthenticateInfo info) throws ServiceException {
   		ResultVO resultVO = new ResultVO();
   		if(id == null) {
   			resultVO.setStatus("false");
   			resultVO.setErrorMessage("删除失败,被删除销活动的id为空！");
   			return resultVO;
   		}
   		PromotionCase orig_promotion = findById(id);
   		if(orig_promotion != null) {
   			orig_promotion.setDelStatus(1);
   			orig_promotion.setUpdateTime(new Date());
   			orig_promotion.setUpdateUser(info.getUserName());
   			resultVO = salesResourceService.removeResourceByGUID(orig_promotion.getGuid(), info);
   			update(orig_promotion);
   		} else {
   			resultVO.setStatus("false");
   			resultVO.setErrorMessage("删除失败,被删除的销活动信息不存在！");
   		}
   		return resultVO;
   	}
   	
   	/**
	 * 拼接configure条件（纯sql语句）
	 * @author tangxin 2015-05-03
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
	 * @author tangxin 2015-05-03
	 */
	private String splitJointConfigure(String field, String mark, String value, boolean isIgnoreBrand) throws ServiceException{
		try {
			value = URLDecoder.decode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new ServiceException("【Service-Error】数据筛选时，利用utf-8解码筛选条件时出现异常！",e);
		}
		if(field.equals("id")){
			return FilterUtils.getConditionStr("promotion.id",mark,value);
		}
		if(field.equals("name")){
			return FilterUtils.getConditionStr("promotion.name",mark,value);
		}
		if(field.equals("startDate")){
			return FilterUtils.getConditionStr("promotion.startDate",mark,value);
		}
		if(field.equals("area")){
			return FilterUtils.getConditionStr("promotion.area",mark,value);
		}
		return null;
	}
   	
    @Override
    public PromotionCaseDAO getDAO() {
        return promotionCaseDAO;
    }
    
}
