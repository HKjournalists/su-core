package com.gettec.fsnip.fsn.service.base;

import java.util.List;

import com.gettec.fsnip.fsn.dao.base.OfficeDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.base.Office;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface OfficeService extends BaseService<Office, OfficeDAO>{

	List<Office> getListByParnetId(Long parentId) throws ServiceException;
}
