package com.gettec.fsnip.fsn.service.wordFrequency;

import java.util.List;
import java.util.Map;

import com.gettec.fsnip.fsn.vo.WordFrequencyVO;


public interface WordFrequencyService {

	Map<Long, List<WordFrequencyVO>> getFrequency();
}
