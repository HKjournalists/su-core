package com.gettec.fsnip.fsn.service.business;

import java.util.List;

import com.gettec.fsnip.fsn.dao.business.BrandCategoryDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BrandCategory;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.business.BrandCategoryTreeNode;

public interface BrandCategoryService extends BaseService<BrandCategory, BrandCategoryDAO>{

	List<BrandCategoryTreeNode> getTreeNodes(int level, String keyword)
			throws ServiceException;
	
}
