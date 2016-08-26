package com.gettec.fsnip.fsn.service.mengniu.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gettec.fsnip.fsn.dao.mengniu.MengNiuProductNameToBarcodeDAO;
import com.gettec.fsnip.fsn.model.mengniu.MengNiuProductNameToBarcode;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.mengniu.MengNiuProductNameToBarcodeService;
@Service
public class MengNiuProductNameToBarcodeServiceImpl extends BaseServiceImpl<MengNiuProductNameToBarcode, MengNiuProductNameToBarcodeDAO>
		implements MengNiuProductNameToBarcodeService {
	@Autowired
	private MengNiuProductNameToBarcodeDAO mengniuProductNameToBarcodeDAO;
	@Override
	public MengNiuProductNameToBarcodeDAO getDAO() {
		return mengniuProductNameToBarcodeDAO;
	}
}
