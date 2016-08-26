package com.gettec.fsnip.fsn.dao.sales;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.model.Model;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.sales.SalesDataSort;
import com.gettec.fsnip.fsn.model.sales.SortField;
import com.gettec.fsnip.fsn.vo.sales.SortFieldValueVO;

/**
 * 销售资料排序 DAO
 * @author tangxin 2015-05-07
 */
public interface SalesDataSortDAO extends BaseDAO<SalesDataSort>, Model {

	List<SortFieldValueVO> getSortProductAlbum(Long organization, boolean sort, int page, int pageSize) throws DaoException;

	Map<String,Long> findEnterpriseIdAndBusIdByOrganization(Long organization) throws DaoException;

	List<SortFieldValueVO> getBusAlbumsWithPage(Long organization, int page, int pageSize) throws DaoException;

	List<SortFieldValueVO> getSalesCaseWithPage(Long organization, int page, int pageSize) throws DaoException;

	List<SortFieldValueVO> getSanZhengWithPage(Long organization, int page, int pageSize) throws DaoException;

	List<SalesDataSort> getListByFieldType(Long organizaton, Long fieldTypeId) throws DaoException;

	SortField findSortFieldById(Long id) throws DaoException;

}
