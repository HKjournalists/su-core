package com.gettec.fsnip.fsn.dao.erp.buss.impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.erp.buss.BussToMerchandisesDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.erp.buss.BussToMerchandises;

@Repository("bussToMerchandisesDAO")
public class BussToMerchandisesDAOImpl extends BaseDAOImpl<BussToMerchandises> 
		implements BussToMerchandisesDAO{
	/**
	 * 根据单号和类型获取入库/出库/调拨的商品列表
	 * @param page
	 * @param pageSize
	 * @param no
	 * @param type
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public List<BussToMerchandises> getMerInfoByNoPage(int page, int pageSize,
			String no, int type) throws DaoException {
		try {
			String condition = " WHERE e.no_1=?1";
			Object[] params = new Object[]{no};
			if(type == 5){ // 入库
				condition += " AND e.storage_1 is not null";
			}else if(type == 6){  // 出库
				condition += " AND e.storage_2 is not null";
			}else if(type == 7){ // 调拨
				condition += " AND e.storage_1 is not null and e.storage_2 is not null";
			}
			return this.getListByPage(page, pageSize, condition, params);
		} catch (JPAException jpae) {
			throw new DaoException("BussToMerchandisesDAOImpl.getMerInfoByNoPage() 根据单号和类型获取入库/出库/调拨的商品列表,出现异常！", jpae.getException());
		}
	}

    @Override
    public long countOutStorageRecord(int page, int pageSize, String num,int type) throws DaoException {
        try {
            String condition = " WHERE e.no_1=?1";
            Object[] params = new Object[]{num};
            if(type == 5){ // 入库
                condition += " AND e.storage_1 is not null";
            }else if(type == 6){  // 出库
                condition += " AND e.storage_2 is not null";
            }else if(type == 7){ // 调拨
                condition += " AND e.storage_1 is not null and e.storage_2 is not null";
            }
            return this.count(condition,params);
        } catch (JPAException jpae) {
            throw new DaoException("BussToMerchandisesDAOImpl.getMerInfoByNoPage() 根据单号和类型获取入库/出库/调拨的商品列表,出现异常！", jpae.getException());
        }
    }
	
	
}
