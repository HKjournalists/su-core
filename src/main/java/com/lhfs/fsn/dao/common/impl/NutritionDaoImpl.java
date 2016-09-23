package com.lhfs.fsn.dao.common.impl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.model.base.Nutrition;
import com.lhfs.fsn.dao.common.NutritionDao;

@Repository
public class NutritionDaoImpl extends BaseDAOImpl<Nutrition> implements NutritionDao {

	@SuppressWarnings("unchecked")
	@Override
	public Nutrition findByName(String name) {
		List<Nutrition> result = entityManager
				.createNativeQuery("select * from nutri_std where name=?1", Nutrition.class)
				.setParameter(1,name).getResultList();
		if(result != null && result.size() > 0){
			return result.get(0);
		}
		return null;
	}

	@Override
	public List<Nutrition> searchNutriInfoByPQ(String queryName, String queryParam, boolean bCn) {
		String subSQL = bCn? queryName+" ":" hzpy("+queryName+") ";
		queryParam = (bCn ? "%":"")+queryParam+"%";
		List<Nutrition> result = entityManager.createQuery(
						"from nutri_std where " +subSQL+" like ?1 ", Nutrition.class)
						.setParameter(1, queryParam).setMaxResults(50)
						.getResultList();
		if(result != null && result.size() > 0){
			return result;
		}
		return null;
	}

	@Override
	public int getRecordCount(String keyword) {
		BigInteger count = (BigInteger) entityManager.createNativeQuery("select count(*) from nutri_std where name like ?1")
				.setParameter(1, "%"+keyword+"%").getSingleResult();
		return count.intValue();
	}

	@Override
	public List<Nutrition> getNutritionByKeyword(String keyword, int startindex, int pagesize) {
		List<Nutrition> result = entityManager
				.createQuery(
						"from nutri_std where name like ?1",
						Nutrition.class).setParameter(1,"%"+keyword+"%")
						.setFirstResult(startindex)
						.setMaxResults(pagesize)
						.getResultList();
		if(result != null && result.size() > 0){
			return result;
		}
		return null;
	}

}
