package com.gettec.fsnip.fsn.service.sales;

import com.gettec.fsnip.fsn.dao.sales.PhotosAlbumsDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.sales.PhotosAlbums;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.sales.*;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

import java.util.List;

/**
 * 相册service层
 * @author tangxin 2015/04/24
 *
 */
public interface PhotosAlbumsService extends
		BaseService<PhotosAlbums, PhotosAlbumsDAO> {

	List<ViewAlbumVO> getAlbumsByOrgId(Long organization, String cut, String sysType) throws ServiceException;

	AlbumVO getDetailAblumByPage(Long organization, String albumID, String cut,
			int page, int pageSize) throws ServiceException;

	ViewReportVO getViewReport(Long productId) throws ServiceException;

	PhotosAlbumVO save(PhotosAlbumVO albumVO, AuthenticateInfo info,
			boolean isNew) throws ServiceException;

	PhotosAlbumVO findByOrganizationAndName(Long organization, String name)
			throws ServiceException;

	List<DetailAlbumVO> getListAlbumsByBusId(Long businessId,String cut)throws ServiceException;

	ReportAppVO findReportByBusAPP(Long productId) throws ServiceException;
	
	ReportAppVO findReportByBusAPP(Long productId,int type) throws ServiceException;

}
