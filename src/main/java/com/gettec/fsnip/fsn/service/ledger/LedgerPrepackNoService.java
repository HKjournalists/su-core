package com.gettec.fsnip.fsn.service.ledger;

import java.util.List;
import java.util.TreeMap;

import com.gettec.fsnip.fsn.dao.ledger.LedgerPrepackNoDao;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.ledger.LedgerPrepackNo;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.ledger.LedgerPrepackNoVO;
import com.gettec.fsnip.fsn.vo.waste.WasteDisposaVO;


public interface LedgerPrepackNoService extends BaseService<LedgerPrepackNo, LedgerPrepackNoDao> {
	List<LedgerPrepackNoVO> loadLedgerPrepackNo(int page, int pageSize,String productName,String companyName,String companyPhone,long qiyeId)throws DaoException;
	long getLedgerPrepackNoTotal(String productName,String companyName,String companyPhone,long qiyeId) throws DaoException;
	/**
	 * 根据企业id及处理时间查询非预包装采购信息
	 * @param orgId
	 * @param date
	 * @return
	 * @throws DaoException
	 */
	List<LedgerPrepackNoVO> getListLedgerPrepackNo(String orgId,String date) throws DaoException;
	void saveledgerPrepackNo(
			TreeMap<String, TreeMap<String, TreeMap<String, Object>>> sheetMap,
			Long fromBusId);
	//做图片上传
	void setResourceData(LedgerPrepackNo ledgerPrepackNo);
}
