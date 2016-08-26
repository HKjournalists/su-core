package com.gettec.fsnip.fsn.dao.sales.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.sales.EmailLogDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.sales.EmailLog;

/**
 * 邮件发送日志dao层实现
 * @author tangxin 2015/04/24
 *
 */
@Repository(value = "emailLogDAOImpl")
public class EmailLogDAOImpl extends BaseDAOImpl<EmailLog> implements
		EmailLogDAO {

	/**
	 * 根据资源id获取电子资料id
	 * @param listId 资源id集合
	 * @author tangxin 2015-05-11
	 */
	@Override
	public Map<String, Long> getAttachmentsId(Long organization, List<Long> listId) throws DaoException {
		try{
			if(organization == null || listId == null || listId.size() < 1) {
				return null;
			}
			String idStr = "";
			for(Long resId : listId){
				idStr += resId.toString() + ",";
			}
			if("".equals(idStr)) return null;
			idStr = idStr.substring(0, idStr.length()-1);
			String sql = "SELECT t.eid,t.sid FROM " +
					"(SELECT ed.id 'eid',sr.id 'sid' FROM t_bus_electronic_data ed " +
					"LEFT JOIN t_sales_resource sr ON ed.guid = sr.guid " +
					"WHERE ed.organization = ?1 AND sr.id IN ("+idStr+")) t";
			List<Object[]> listObjs = getListBySQLWithPage(sql, new Object[]{organization}, -1, 0);
			Map<String, Long> map = null;
			if(listObjs != null && listObjs.size() > 0){
				map = new HashMap<String, Long>();
				Object[] ids = listObjs.get(0);
				map.put("dataId", (ids[0] != null ? Long.parseLong(ids[0].toString()) : 0L));
				map.put("resourceId", (ids[1] != null ? Long.parseLong(ids[1].toString()) : 0L));
			}
			return map;
		}catch(Exception e){
			throw new DaoException(e.getMessage(), e);
		}
	}
}
