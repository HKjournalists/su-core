package com.gettec.fsnip.fsn.service.base;

import com.gettec.fsnip.fsn.dao.base.SysAreaDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.base.SysArea;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.BaseDataVO;

import java.util.List;

public interface SysAreaService extends BaseService<SysArea,SysAreaDAO>{

	List<SysArea> getListByParentId(Long parentId) throws ServiceException;

	List<BaseDataVO> getDataSet();
}
