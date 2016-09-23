package com.gettec.fsnip.fsn.dao.sentiment;

import java.util.List;
import java.util.Map;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.model.sentiment.SentimentWebsite;
import com.gettec.fsnip.fsn.vo.page.PageVO;

public interface SentimentWebsiteDAO extends BaseDAO<SentimentWebsite>{

	List<SentimentWebsite> findByOrg(Integer page, Integer pageSize, Long organizationId);

	boolean checkUnique(Long websiteId, String host);

	Long countByOrg(Long organizationId);
	
	public Map<String,Object> getPageWebsite(PageVO pageVo,SentimentWebsite sentimentWebsite);
	
	public boolean unique(SentimentWebsite sentimentWebsite);
}
