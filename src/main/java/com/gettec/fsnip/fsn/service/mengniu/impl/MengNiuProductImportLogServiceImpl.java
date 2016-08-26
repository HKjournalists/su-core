package com.gettec.fsnip.fsn.service.mengniu.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gettec.fsnip.fsn.dao.mengniu.MengNiuProductImportLogDAO;
import com.gettec.fsnip.fsn.model.mengniu.MengNiuProductImportLog;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.mengniu.MengNiuProductImportLogService;
@Service
public class MengNiuProductImportLogServiceImpl extends
		BaseServiceImpl<MengNiuProductImportLog, MengNiuProductImportLogDAO> implements
		MengNiuProductImportLogService {
	@Autowired
	private MengNiuProductImportLogDAO mengniuProductImportLogDAO;
	@Override
	public MengNiuProductImportLogDAO getDAO() {
		return mengniuProductImportLogDAO;
	}

}
