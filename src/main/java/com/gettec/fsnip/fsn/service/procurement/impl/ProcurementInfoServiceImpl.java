package com.gettec.fsnip.fsn.service.procurement.impl;

import com.gettec.fsnip.fsn.dao.procurement.ProcurementInfoDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.procurement.ProcurementInfo;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.procurement.ProcurementInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * ProcurementInfo service implementation
 * 
 * @author lxz
 */
@Service(value="procurementInfoService")
public class ProcurementInfoServiceImpl extends BaseServiceImpl<ProcurementInfo, ProcurementInfoDAO> 
		implements ProcurementInfoService{
	@Autowired private ProcurementInfoDAO procurementInfoDAO;
	
	
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public ProcurementInfoDAO getDAO() {
		return procurementInfoDAO;
	}



	/**
	 * 根据采购类型获取采购信息数量
	 * @param name 采购材料名称
	 * @param type 采购类型   1：原辅料  2：添加剂  3：包装材料
	 * @param currentUserOrganization 当前企业机构id
	 * @return long
	 * @throws ServiceException
	 * @author lxz
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public long getProcurementTotalByType(String name, int type,
			Long currentUserOrganization) throws ServiceException {
		try {
			String condition = " WHERE e.organizationId = ?1 AND e.type=?2  ";
			Object[] param = new Object[]{currentUserOrganization,type};
			if(StringUtils.isNotBlank(name)){
				param = new Object[]{currentUserOrganization,type,name};
				condition += " and e.name like concat('%',concat(?3,'%'))  ";
			}
			return getDAO().count(condition,param);
				
		} catch (JPAException e) {
			e.printStackTrace();
		}
		return 0;
	}



	/**
	 * 根据采购类型获取采购信息集合 有分页
	 * @param page
	 * @param pageSize
	 * @param name 采购材料名称
	 * @param type 采购类型   1：原辅料  2：添加剂  3：包装材料
	 * @param currentUserOrganization 当前企业机构id
	 * @return List<ProcurementInfo>
	 * @throws ServiceException
	 * @author lxz
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public List<ProcurementInfo> getProcurementListByType(int page,
			int pageSize, String name, int type, Long currentUserOrganization)
			throws ServiceException {
		try {
			String condition = " WHERE e.organizationId = ?1 AND e.type=?2  ";
			Object[] param = new Object[]{currentUserOrganization,type};
			if(StringUtils.isNotBlank(name)){
				param = new Object[]{currentUserOrganization,type,name};
				condition += " and e.name like concat('%',concat(?3,'%'))  ";
				condition+=" order by e.procurementDate desc";
			} else{
				condition+=" order by e.createDate desc";
			}
			List<ProcurementInfo> list = this.getDAO().getListByPage(page,pageSize,condition, param);
			return list;
				
		} catch (JPAException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

}