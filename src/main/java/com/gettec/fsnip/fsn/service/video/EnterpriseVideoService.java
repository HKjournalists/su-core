package com.gettec.fsnip.fsn.service.video;

import com.gettec.fsnip.fsn.dao.video.EnterpriseVideoDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.video.Enterprise_video;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

public interface EnterpriseVideoService extends BaseService<Enterprise_video, EnterpriseVideoDAO>{
	public PagingSimpleModelVO<Enterprise_video> getPage(int page, int page_size, String orgId) throws ServiceException;
}