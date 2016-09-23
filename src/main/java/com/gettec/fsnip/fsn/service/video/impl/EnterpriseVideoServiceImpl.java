package com.gettec.fsnip.fsn.service.video.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gettec.fsnip.fsn.dao.video.EnterpriseVideoDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.video.Enterprise_video;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.video.EnterpriseVideoService;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

/**
 * @author litg
 */
@Service(value="enterpriseVideoService")
public class EnterpriseVideoServiceImpl extends BaseServiceImpl<Enterprise_video, EnterpriseVideoDAO> 
		implements EnterpriseVideoService{
	@Autowired
	private EnterpriseVideoDAO enterpriseVideoDAO;
	
	public EnterpriseVideoDAO getDAO() {
		return enterpriseVideoDAO;
	}
	
	@SuppressWarnings("unused")
	public PagingSimpleModelVO<Enterprise_video> getPage(int page, int page_size, String orgId) throws ServiceException {
		PagingSimpleModelVO<Enterprise_video> result = new PagingSimpleModelVO<Enterprise_video>();
		int limit_start = (page - 1) * page_size;
		String condition = " where e.is_show = '1'";
		condition += " and e.org_id = '";
		condition += orgId;
		condition += "' order by e.sort";
		Long count = 0l;
		try {
			count = getDAO().count(condition);
			result.setCount(count);
			result.setListOfModel(getDAO().getPaging(page, page_size, condition));
		} catch (JPAException jpae) {
			throw new ServiceException("", jpae.getException());
		}
		return result;
	}
}