package com.gettec.fsnip.fsn.dao.base;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.base.SysArea;
import com.gettec.fsnip.fsn.vo.BaseDataVO;

import java.util.List;

public interface SysAreaDAO extends BaseDAO<SysArea>{

	List<SysArea> getListByParentId(Long parentId) throws DaoException;

	List<BaseDataVO> getDataSet();
}
