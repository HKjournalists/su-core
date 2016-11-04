package com.gettec.fsnip.fsn.service.video;

import com.gettec.fsnip.fsn.dao.video.EnterpriseVideoDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.video.Enterprise_video;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.business.BusinessVideoVo;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

import java.util.List;

public interface EnterpriseVideoService extends BaseService<Enterprise_video, EnterpriseVideoDAO>{
	public PagingSimpleModelVO<Enterprise_video> getPage(int page, int page_size, String orgId) throws ServiceException;
	public List<BusinessVideoVo> getbusinessByvideo(int page, int page_size, String name, String province,String address,String type) throws ServiceException;
	public String countbusinessByvideo(int page, int page_size, String name,String province,String address,String type) throws ServiceException;
	public List<Enterprise_video> getVideoByOrgid(int page, int page_size, Long orgid) throws ServiceException;
	public String countvideoBybuess(Long orgid) throws ServiceException;
}