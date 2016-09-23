package com.gettec.fsnip.fsn.service.erp;

import java.util.List;
import com.gettec.fsnip.fsn.dao.erp.OutBillToOutGoodsDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.OutBillToOutGoods;
import com.gettec.fsnip.fsn.model.erp.base.OutBillToOutGoodsPK;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface OutBillToOutGoodsService extends BaseService<OutBillToOutGoods, OutBillToOutGoodsDAO>{

	/**
	 * 批量增加联系人关系
	 * 
	 * @param infos
	 * @return
	 */
	List<OutBillToOutGoods> addRelationShips(List<OutBillToOutGoods> infos);

	/**
	 * 通过编号查询关联联系人
	 * 
	 * @param no
	 * @return
	 * @throws ServiceException 
	 */
	List<OutBillToOutGoods> getRelationShipByNo(String no) throws ServiceException;

	OutBillToOutGoods findByPk(OutBillToOutGoodsPK pk)throws ServiceException;
}
