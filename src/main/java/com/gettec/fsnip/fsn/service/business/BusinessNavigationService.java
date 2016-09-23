package com.gettec.fsnip.fsn.service.business;

import java.util.List;

import com.gettec.fsnip.fsn.dao.business.BusinessNavigationDao;
import com.gettec.fsnip.fsn.model.business.BusinessNavigation;
import com.gettec.fsnip.fsn.model.business.NavigationAddress;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface BusinessNavigationService extends BaseService<BusinessNavigation, BusinessNavigationDao>{

	List<BusinessNavigation> getNavigationList(Long businessID);

	void updateNavigationList(NavigationAddress navigationData);

	int getCount(Long currentUserOrganization);

}
