package com.gettec.fsnip.fsn.service.sales;

import java.util.List;

import com.gettec.fsnip.fsn.dao.sales.RecommendBuyDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.sales.RecommendBuy;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.sales.RecommendBuyVO;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

/**
 * 推荐购买方式service
 * @author tangxin 2015/04/24
 *
 */
public interface RecommendBuyService extends BaseService<RecommendBuy, RecommendBuyDAO>{

	RecommendBuyVO save(RecommendBuyVO recommendBuyVO, AuthenticateInfo info, boolean isNew) throws ServiceException;

	ResultVO removeRecommendBuyById(Long buywayId, AuthenticateInfo info) throws ServiceException;

	long countByConfigure(Long organization, String configure) throws ServiceException;

	List<RecommendBuyVO> getListByOrganizationWithPage(Long organization, String configure, Integer page, Integer pageSize)
			throws ServiceException;

	long countByName(String name, Long organization, Long id) throws ServiceException;

	List<RecommendBuyVO> getEntityByBusId(Long busId)throws ServiceException;
}
