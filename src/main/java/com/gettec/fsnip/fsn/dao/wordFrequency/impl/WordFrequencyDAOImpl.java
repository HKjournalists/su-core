package com.gettec.fsnip.fsn.dao.wordFrequency.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.gettec.fsnip.fsn.dao.wordFrequency.WordFrequencyDAO;
import com.gettec.fsnip.fsn.vo.WordFrequencyVO;


@Repository(value="wordFrequencyDAO")
public class WordFrequencyDAOImpl implements WordFrequencyDAO {

	@PersistenceContext(type=PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public Map<Long, List<WordFrequencyVO>> getFrequency() {
		Map<Long, List<WordFrequencyVO>> map = new HashMap<Long, List<WordFrequencyVO>>();
		try {
			String sql = "select t3.word, temp.wordID, sum(temp.news_total) as total from ("
					+ "SELECT t1.wordID, SUM(t1.wordCOUNT) as news_total " 
					+ "FROM newswordcount t1 group by t1.wordID "
					+ "union all "
					+ "SELECT t2.wordID, SUM(t2.wordCOUNT) as weibo_total " 
					+ "FROM weibowordcount t2 group by t2.WordID) as temp "
					+ "left join dictionary t3 on temp.WordID = t3.wordID " 
					+ "group by temp.wordID order by total desc limit 15";
	
			Query query = entityManager.createNativeQuery(sql);
			
			List<Object[]> result = query.getResultList();
			List<WordFrequencyVO> vos = new ArrayList<WordFrequencyVO>();
			if(!CollectionUtils.isEmpty(result)){
				for(Object[] row : result){
					WordFrequencyVO vo = new WordFrequencyVO();
					vo.setWordName(row[0].toString());
					vo.setWordID(((Integer)row[1]).intValue());
					vo.setTotal(((BigDecimal)row[2]).intValue());
					vos.add(vo);
				}
			}
			Date now = new Date();
			map.put(now.getTime(), vos);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
}
