package com.gettec.fsnip.fsn.dao.base.impl;

import com.gettec.fsnip.fsn.dao.base.SysAreaDAO;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.base.SysArea;
import com.gettec.fsnip.fsn.vo.BaseDataVO;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository(value="sysAreaDAO")
public class SysAreaDAOImpl extends BaseDAOImpl<SysArea>
		implements SysAreaDAO{

	/**
	 * 根据父区域id查询子区域集合
	 * @param parentId
	 * @return List<SysArea>
	 * @throws DaoException
	 */
	@Override
	public List<SysArea> getListByParentId(Long parentId) throws DaoException {
		try{
			if(parentId == null) {return null;}
			String condition = " WHERE e.parentId = ?1";
			return this.getListByCondition(condition, new Object[]{parentId});
		}catch(JPAException jape){
			throw new DaoException("SysAreaDAOImpl.getListByParentId()-->" + jape.getMessage(),jape.getException());
		}
	}

	@Override
	public List<BaseDataVO> getDataSet() {
		List<BaseDataVO> vos = new ArrayList<BaseDataVO>();
		try {
			String sql = "SELECT ID,ZYMC,ZYLXMC,ZYLX FROM baseData";

			Query query = entityManager.createNativeQuery(sql);
			List<Object[]> result = query.getResultList();

			if(result != null){
				for(Object[] obj : result){
					BaseDataVO vo = new BaseDataVO(
							obj[0]==null?"":obj[0].toString(),
							obj[1]==null?"":obj[1].toString(),
							obj[2]==null?"":obj[2].toString(),
							obj[3]==null?"":obj[3].toString()
					);
					vos.add(vo);
				}
			}
			return vos;
		} catch (Exception e) {
			e.printStackTrace();
			return vos;
		}
	}
}
