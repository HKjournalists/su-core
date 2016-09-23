package com.gettec.fsnip.fsn.dao.sales;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.sales.ElectronicData;
import com.gettec.fsnip.fsn.vo.sales.MaterialsVO;

/**
 * 电子资料DAO
 * @author tangxin 2015/04/24
 *
 */
public interface ElectronicDataDAO extends BaseDAO<ElectronicData> {

	List<MaterialsVO> getListMaterials(Long organization) throws DaoException;

	ElectronicData findByOrganization(Long organization,int type) throws DaoException;

}
