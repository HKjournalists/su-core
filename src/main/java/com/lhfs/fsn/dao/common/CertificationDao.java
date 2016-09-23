package com.lhfs.fsn.dao.common;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.model.base.Certification;
import com.lhfs.fsn.vo.product.CertificationVO;

public interface CertificationDao extends BaseDAO<Certification>{

	int getRecordCount(String keyword);

	List<Certification> getCertificationByKeyword(String keyword,
			int startindex, int pagesize);

	Certification getCertByID(Long id);
	
	List<CertificationVO> findByPuroductId(Long id);

}
