package com.gettec.fsnip.fsn.service.sales;

import java.util.List;
import java.util.Map;

import com.gettec.fsnip.fsn.dao.sales.SalesDataSortDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.sales.SalesDataSort;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.sales.DataSortVO;
import com.gettec.fsnip.fsn.vo.sales.SortFieldValueVO;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

/**
 * 销售资料排序 service
 * @author tangxin 2015-05-07
 */
public interface SalesDataSortService extends BaseService<SalesDataSort, SalesDataSortDAO> {

	List<SortFieldValueVO> getSortProductAlbum(Long organization,boolean sort, int page, int pageSize) throws ServiceException;

	List<SortFieldValueVO> getSortBusinesAlbum(Long organization, int page, int pageSize) throws ServiceException;

	List<SortFieldValueVO> getSortSalesCase(Long organization, int page, int pageSize) throws ServiceException;

	List<SortFieldValueVO> getSanZhengWithPage(Long organization, int page, int pageSize) throws ServiceException;

	DataSortVO saveSortData(DataSortVO dataSortVO, AuthenticateInfo info) throws ServiceException;

	List<SortFieldValueVO> filterSanZhengVO(List<SortFieldValueVO> sanZhengVO, Long organization) throws Exception;

}
