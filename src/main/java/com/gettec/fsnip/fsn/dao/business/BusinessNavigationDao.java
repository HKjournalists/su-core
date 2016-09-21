package com.gettec.fsnip.fsn.dao.business;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.model.business.BusinessNavigation;

public interface BusinessNavigationDao extends BaseDAO<BusinessNavigation>{

	List<BusinessNavigation> getNavigationList(Long businessID);

}
