package com.gettec.fsnip.fsn.dao.receive.impl;

import java.util.List;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.receive.ReceiveBusKeyConfigDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.receive.ReceiveBusKeyConfig;
import com.gettec.fsnip.fsn.sign.SignConfig;

/**
 * 
 * @author ZhangHui 2015/4/24
 */
@Repository(value="receiveBusKeyConfigDAO")
public class ReceiveBusKeyConfigDAOImpl extends BaseDAOImpl<ReceiveBusKeyConfig> 
		implements ReceiveBusKeyConfigDAO{

	/**
	 * 根据企业唯一编号，超找签名的配置信息
	 * @author ZhangHui 2014/4/24
	 */
	@SuppressWarnings("unchecked")
	@Override
	public SignConfig findByNo(String no) throws DaoException {
		try {
			String sql = "SELECT config.`NO`,config.`KEY`,config.`STATUS` FROM receive_bus_key_config config WHERE `NO` = ?1";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, no);
			List<Object[]> results = query.getResultList();
			if(results!=null && results.size() > 0){
				Object[] obj = results.get(0);
				/* 数据封装 */
				SignConfig config = new SignConfig(obj[0]==null?"":obj[0].toString(), 
						obj[1]==null?"":obj[1].toString(), Integer.parseInt(String.valueOf(obj[2])));
				return config;
			}
			return null;
		} catch (Exception e) {
			throw new DaoException("ReceiveBusKeyConfigDAOImpl.findByNo() 出现异常！", e);
		}
	}
	
}
