package com.gettec.fsnip.fsn.service.base;

import java.util.List;

import com.gettec.fsnip.fsn.dao.base.DistrictDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.base.District;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface DistrictService extends BaseService<District, DistrictDAO>{
	
	List<District> getListByDescription(String description) throws ServiceException;
}
