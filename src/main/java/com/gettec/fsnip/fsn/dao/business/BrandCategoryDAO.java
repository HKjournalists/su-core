package com.gettec.fsnip.fsn.dao.business;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.business.BrandCategory;
import com.gettec.fsnip.fsn.model.business.BrandCategoryDetail;

public interface BrandCategoryDAO extends BaseDAO<BrandCategory>{

	List<BrandCategoryDetail> getTreeNodes(int level, String keyword)
			throws DaoException;

}
