package com.gettec.fsnip.fsn.service.sentiment;

import java.util.List;
import java.util.Map;

import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.sentiment.SentimentWebsite;
import com.gettec.fsnip.fsn.vo.page.PageVO;

public interface SentimentWebsiteService {

	SentimentWebsite create(SentimentWebsite sentimentWebsite) throws JPAException;
	
	List<SentimentWebsite> getWebsitesByOrg(Integer page, Integer pageSize, Long organizationId);
	
	boolean checkUnique(Long websiteId, String host);

	Long countByOrg(Long organizationId);
	
	SentimentWebsite update(SentimentWebsite sentimentWebsite) throws JPAException;
	
	public Map<String,Object> getPageWebsite(PageVO pageVo,SentimentWebsite sentimentWebsite);
	
	public boolean unique(SentimentWebsite sentimentWebsite);
	
	public boolean updateWebsite(SentimentWebsite sentimentWebsite);
	
	public boolean deleteWebsite(Long id);

}
