package com.lhfs.fsn.dao.common;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.model.base.Nutrition;

public interface NutritionDao extends BaseDAO<Nutrition> {

	Nutrition findByName(String name);

	List<Nutrition> searchNutriInfoByPQ(String string, String queryParam, boolean bCn);

	int getRecordCount(String keyword);

	List<Nutrition> getNutritionByKeyword(String keyword, int startindex,
			int pagesize);

}
