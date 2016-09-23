package com.gettec.fsnip.fsn.service.wordFrequency.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.wordFrequency.WordFrequencyDAO;
import com.gettec.fsnip.fsn.service.wordFrequency.WordFrequencyService;
import com.gettec.fsnip.fsn.vo.WordFrequencyVO;

@Service(value="wordFrequencyService")
public class WordFrequencyServiceImpl implements WordFrequencyService {

	@Autowired
	private WordFrequencyDAO wordFrequencyDAO;
	private final static Logger LOG = Logger.getLogger(WordFrequencyServiceImpl.class);

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public Map<Long, List<WordFrequencyVO>> getFrequency() {
		try {
			return wordFrequencyDAO.getFrequency();
		} catch (Exception e) {
			LOG.error("ERROR - wordFrequencyDAO.getFrequency()");
		}
		return null;
	}
	
	
}
