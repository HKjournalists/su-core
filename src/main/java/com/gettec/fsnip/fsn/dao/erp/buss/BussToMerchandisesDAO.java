package com.gettec.fsnip.fsn.dao.erp.buss;

import java.util.List;
import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.erp.buss.BussToMerchandises;

public interface BussToMerchandisesDAO extends BaseDAO<BussToMerchandises> {
	List<BussToMerchandises> getMerInfoByNoPage(int page, int pageSize, String no, int type) throws DaoException;

    long countOutStorageRecord(int page, int pageSize, String num, int type) throws DaoException;
}
