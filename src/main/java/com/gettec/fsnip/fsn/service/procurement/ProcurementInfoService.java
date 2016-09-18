package com.gettec.fsnip.fsn.service.procurement;

import java.util.List;

import com.gettec.fsnip.fsn.dao.procurement.ProcurementInfoDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.procurement.ProcurementInfo;
import com.gettec.fsnip.fsn.service.common.BaseService;

/**
 * ProcurementInfo Service
 * @author lxz
 *
 */
public interface ProcurementInfoService extends BaseService<ProcurementInfo, ProcurementInfoDAO>{

	/**
	 * 根据采购类型获取采购信息数量
	 * @param name 采购材料名称
	 * @param type 采购类型   1：原辅料  2：添加剂  3：包装材料
	 * @param currentUserOrganization 当前企业机构id
	 * @return long
	 * @throws ServiceException
	 * @author lxz
	 */
	long getProcurementTotalByType(String name, int type,
			Long currentUserOrganization)throws ServiceException;

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
	List<ProcurementInfo> getProcurementListByType(int page, int pageSize,
			String name, int type, Long currentUserOrganization)throws ServiceException;

}