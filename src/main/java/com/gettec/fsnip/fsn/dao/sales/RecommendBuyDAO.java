package com.gettec.fsnip.fsn.dao.sales;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.sales.RecommendBuy;
import com.gettec.fsnip.fsn.vo.sales.RecommendBuyVO;

/**
 * 推荐购买方式DAO
 * @author tangxin 2015/04/24
 *
 */
public interface RecommendBuyDAO extends BaseDAO<RecommendBuy>{

	long countByConfigure(Long organization, String configure) throws DaoException;

	List<RecommendBuyVO> getListByOrganizationWithPage(Long organization,
			String condition, Integer page, Integer pageSize) throws DaoException;

	long countByName(String name, Long organization, Long id) throws DaoException;

	List<RecommendBuyVO> getEntityByBusId(Long busId)throws DaoException;
}
