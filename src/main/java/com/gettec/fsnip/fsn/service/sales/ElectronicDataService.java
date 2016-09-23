package com.gettec.fsnip.fsn.service.sales;

import java.util.List;

import com.gettec.fsnip.fsn.dao.sales.ElectronicDataDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.sales.ElectronicData;
import com.gettec.fsnip.fsn.model.sales.SalesResource;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.util.sales.BaseMailDefined;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.sales.DataSortVO;
import com.gettec.fsnip.fsn.vo.sales.MaterialsVO;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

/**
 * 电子资料service层
 * @author tangxin 2015/04/24
 *
 */
public interface ElectronicDataService extends
		BaseService<ElectronicData, ElectronicDataDAO> {

	ResultVO sendMail(BaseMailDefined baseMail, AuthenticateInfo info) throws ServiceException;

	List<MaterialsVO> getListMaterials(Long organization) throws ServiceException;

	ResultVO save(DataSortVO dataSortVO, AuthenticateInfo info) throws ServiceException;

	ResultVO createElectDate(DataSortVO dataSortVO, AuthenticateInfo info) throws ServiceException;
	
	public void savePdfData(SalesResource res,long orgId,long enterpriseId,AuthenticateInfo info,int type)throws ServiceException;

}
