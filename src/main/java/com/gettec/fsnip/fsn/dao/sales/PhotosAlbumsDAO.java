package com.gettec.fsnip.fsn.dao.sales;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.sales.PhotosAlbums;
import com.gettec.fsnip.fsn.vo.sales.DetailAlbumVO;
import com.gettec.fsnip.fsn.vo.sales.PhotosAlbumVO;
import com.gettec.fsnip.fsn.vo.sales.ViewReportVO;

/**
 * 相册DAO
 * @author tangxin 2015/04/24
 *
 */
public interface PhotosAlbumsDAO extends BaseDAO<PhotosAlbums> {

	long countByName(Long organization, String name) throws DaoException;

	List<DetailAlbumVO> getListAlbumsByPage(Long organization, String name,
			int page, int pageSize, String cut) throws DaoException;

	ViewReportVO getViewReport(Long productId) throws DaoException;
	
	ViewReportVO getViewReport(Long productId,int type) throws DaoException;
	

	PhotosAlbumVO findByOrganizationAndName(Long organization, String name)
			throws DaoException;

	List<DetailAlbumVO> getListAlbumsByBusId(Long businessId, String cut)throws DaoException;

	long countCertification(Long businessId) throws DaoException;

	List<Object[]> getListCertification(Long businessId, int page, int pageSize) throws DaoException;
}
