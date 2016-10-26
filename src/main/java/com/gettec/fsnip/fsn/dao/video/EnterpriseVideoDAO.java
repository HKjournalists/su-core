package com.gettec.fsnip.fsn.dao.video;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.video.Enterprise_video;
import com.gettec.fsnip.fsn.vo.business.BusinessVideoVo;

import java.util.List;

public interface EnterpriseVideoDAO extends BaseDAO<Enterprise_video>{
    public List<BusinessVideoVo> getbusinessByvideo(int page, int page_size, String name, String keyword) throws ServiceException;
    public String countbusinessByvideo(int page, int page_size, String name,String keyword) throws ServiceException;
    public List<Enterprise_video> getVideoByOrgid(int page, int page_size, Long orgid) throws ServiceException;

}