package com.gettec.fsnip.fsn.dao.ledger;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.ledger.LedgerPrepackNo;
import com.gettec.fsnip.fsn.vo.ledger.LedgerPrepackNoVO;

public interface LedgerPrepackNoDao extends BaseDAO<LedgerPrepackNo> {
	List<LedgerPrepackNoVO> loadLedgerPrepackNo(int page, int pageSize,String productName,String companyName,String companyPhone,long qiyeId)throws DaoException;
	long getLedgerPrepackNoTotal(String productName,String companyName,String companyPhone,long qiyeId) throws DaoException;
	/**
	 * 根据企业id及处理时间查询非预包装采购信息
	 * @param orgId
	 * @param date
	 * @return
	 * @throws DaoException
	 */
	List<LedgerPrepackNoVO> getListLedgerPrepackNo(String orgId,String date)throws DaoException;
	/**
	 * 删除相应的图片
	 * @param sqlColumn
	 * @param emty
	 */
	void deleteResource(String sqlColumn, LedgerPrepackNo emty);
	/**
	 * 根据图片ID删除图片信息
	 * @param id
	 */
	void deleteResource(Long id);
}
