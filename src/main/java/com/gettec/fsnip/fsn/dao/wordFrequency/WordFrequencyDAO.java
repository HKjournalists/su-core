package com.gettec.fsnip.fsn.dao.wordFrequency;

import java.util.List;
import java.util.Map;

import com.gettec.fsnip.fsn.vo.WordFrequencyVO;


public interface WordFrequencyDAO {

	Map<Long, List<WordFrequencyVO>> getFrequency();
}
