package com.gettec.fsnip.fsn.dao.receive;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.receive.ReceiveTestResult;

/**
 * 
 * @author ZhangHui 2015/4/24
 */
public interface ReceiveTestResultDAO extends BaseDAO<ReceiveTestResult>{

	/**
	 * 功能描述：
	 *       根据报告的唯一标识，超找报告数量。
	 * @param receive_id 报告唯一id
	 * @param edition 企业数据来源唯一标识
	 * @return 数量
	 * @throws DaoException
	 * @author ZhangHui 2015/4/24
	 */
	public long countByReceiveIdAndEdition(String receive_id, String edition) throws DaoException;
	
}
