package com.gettec.fsnip.fsn.service.resort;

import java.util.List;

import com.gettec.fsnip.fsn.dao.resort.ResortsDao;
import com.gettec.fsnip.fsn.model.resort.Resorts;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface ResortsService extends BaseService<Resorts, ResortsDao>{


	List<Resorts> getResortsList(int page, int pageSize, String name,Long currentUserOrganization);

	long getResortsTotal(String name, Long currentUserOrganization);

}
