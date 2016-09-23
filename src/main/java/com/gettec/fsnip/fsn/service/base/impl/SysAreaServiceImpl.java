package com.gettec.fsnip.fsn.service.base.impl;

import com.gettec.fsnip.fsn.dao.base.SysAreaDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.base.SysArea;
import com.gettec.fsnip.fsn.service.base.SysAreaService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.vo.BaseDataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "sysAreaService")
public class SysAreaServiceImpl extends BaseServiceImpl<SysArea, SysAreaDAO> implements SysAreaService {

	@Autowired private SysAreaDAO sysAreaDAO;
	
	/** 
	 * 根据父区域id查询子区域集合
	 */
	@Override
	public List<SysArea> getListByParentId(Long parentId)
			throws ServiceException {
		try{
			List<SysArea> listArea = sysAreaDAO.getListByParentId(parentId);
			if(listArea == null) return null;
			for(SysArea area : listArea){
				if(area.getParentIds().length()<4) area.setHasChildren(true);
				else area.setHasChildren(false);
			}
			return listArea;
		}catch(DaoException daoe){
			throw new ServiceException("SysAreaServiceImpl.getListByParentId() " + daoe.getMessage(),daoe);
		}
	}

	@Override
	public List<BaseDataVO> getDataSet() {
		return getDAO().getDataSet();
	}

	@Override
	public SysAreaDAO getDAO() {
		return sysAreaDAO;
	}

}
