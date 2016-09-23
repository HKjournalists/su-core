package com.gettec.fsnip.fsn.service.sales.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.sales.SalesBranchDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.sales.SalesBranch;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.sales.SalesBranchService;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.fsn.util.sales.SalesUtil;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.sales.SalesBranchVO;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

@Service(value="salesBranchService")
public class SalesBranchServiceImpl extends BaseServiceImpl<SalesBranch,SalesBranchDAO> implements SalesBranchService {

    @Autowired private SalesBranchDAO salesBranchDAO;
    @Autowired private BusinessUnitService businessUnitService;
    
    /**
     * 新增 or 更新销售网点 信息
     * @author tangxin 2015-04-27
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public SalesBranchVO save(SalesBranchVO salesBranchVO,
			AuthenticateInfo info, boolean isNew) throws ServiceException{
		try{
			// 如果保存的对象为空时，直接返回
			if(salesBranchVO == null) {
				return salesBranchVO;
			}
			// 定义一个销售网点
			SalesBranch branch = null;
			if(!isNew) {
				// 如果是更新销售网点，则从数据库中查询一个已存在的网点实体
				branch = getDAO().findById(salesBranchVO.getId());
			}
			// 将前台传递来的网点VO 转换为 Entity
			branch = salesBranchVO.toEntity(branch);
			branch.setUpdateTime(new Date());
			branch.setUpdateUser(info.getUserName());
			// 如果是新增状态，
			if(isNew) {
				// 获取当前登陆用户的 企业 id
				Long busId = businessUnitService.findIdByOrg(info.getOrganization());
				branch.setCreateTime(new Date());
				branch.setCreateUser(info.getUserName());
				branch.setGuid(SalesUtil.createGUID());
				branch.setBusinessId(busId);
				branch.setOrganization(info.getOrganization());
				// 设置删除状态为零
				branch.setDelStatus(0);
				create(branch);
				// 新增网点创建成功后，给网点 VO 赋id值，新增时需要将id返回到前台
				salesBranchVO.setId(branch.getId());
			}else{
				 // 更新网点信息
				update(branch);
			}
		}catch(Exception e){
			throw new ServiceException(e.getMessage(), e);
		}
		return salesBranchVO;
	}
    
    /**
	 * 根据筛选条件统计销售网点信息数量
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
	 * 根据销售网点名称统计数量，验证名称时候重复用
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
	 * 根据筛选条件分页查询销售网点信息
	 * @author tangxin 2015-04-27
	 */
	@Override
	public List<SalesBranchVO> getListByOrganizationWithPage(Long organization,
			String configure, Integer page, Integer pageSize)
			throws ServiceException {
		try{
			return this.getDAO().getListByOrganizationWithPage(organization, getConfigure(configure), page, pageSize);
		}catch(DaoException daoe){
			throw new ServiceException(daoe.getMessage(), daoe.getException());
		}
	}

	/**
	 * 根据id删除销售网点信息(假删除)
	 * @author tangxin 2015-04-27
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ResultVO removeBranchById(Long branchId, AuthenticateInfo info) throws ServiceException {
		ResultVO resultVO = new ResultVO();
		if(branchId == null) {
			resultVO.setStatus("false");
			resultVO.setErrorMessage("删除失败,被删除网点的id为空！");
			return resultVO;
		}
		// 查询需要删除的网点实体
		SalesBranch orig_branch = findById(branchId);
		if(orig_branch != null) {
			// 更新被删除的网点实体的 状态
			orig_branch.setUpdateTime(new Date());
			orig_branch.setUpdateUser(info.getUserName());
			// delStatus 为 1 时 表示网点已经被删除，假删除
			orig_branch.setDelStatus(1);
			update(orig_branch);
		} else {
			resultVO.setStatus("false");
			resultVO.setErrorMessage("删除失败,被删除的网点不存在！");
		}
		return resultVO;
	}
	
	/**
	 * 根据地址类型（01省、02市） 和关键字 获取销售网点二级地址的列表
	 * @author tangxin 2015-04-28
	 */
	@Override
	public List<String> getListAddrByType(Long organization, String keyword, String type) throws ServiceException {
		try{
			return getDAO().getListAddrByType(organization, keyword, type);
		}catch(DaoException daoe){
			throw new ServiceException(daoe.getMessage(), daoe);
		}
	}
	
	/**
	 * 获取当前登陆企业 指定省份下的销售网点信息(销售网点模块，预览地图时加载)
	 * @author tangxin 2015/6/15
	 */
	@Override
	public List<SalesBranchVO> getListAddrByProvince(Long organization, String province) throws ServiceException {
		try{
			return getDAO().getListAddrByProvince(organization, province);
		}catch(DaoException daoe){
			throw new ServiceException(daoe.getMessage(), daoe);
		}
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
			return FilterUtils.getConditionStr("branch.id",mark,value);
		}
		if(field.equals("name")){
			return FilterUtils.getConditionStr("branch.name",mark,value);
		}
		if(field.equals("address")){
			return FilterUtils.getConditionStr("branch.address",mark,value);
		}
		if(field.equals("telPhone")){
			return FilterUtils.getConditionStr("branch.telPhone",mark,value);
		}
		if(field.equals("contact")){
			return FilterUtils.getConditionStr("branch.contact",mark,value);
		}
		return null;
	}

	@Override
    public SalesBranchDAO getDAO() {
        return salesBranchDAO;
    }
    
}
