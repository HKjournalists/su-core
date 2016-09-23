package com.gettec.fsnip.fsn.service.waste;

import java.util.List;

import com.gettec.fsnip.fsn.dao.waste.WasteDisposaDao;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.waste.WasteDisposa;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.waste.WasteDisposaVO;
import com.lhfs.fsn.vo.business.BussinessUnitVO;

public interface WasteDisposaService extends BaseService<WasteDisposa, WasteDisposaDao>{
	List<WasteDisposaVO> loadWasteDisposa(int page, int pageSize, String handler,long qiyeId)throws DaoException;
	long getWasteTotal(String handler,long qiyeId) throws DaoException;
	
	/**
	 * 根据企业id及处理时间查询废物处理记录
	 * @param orgId
	 * @param date
	 * @return
	 * @throws DaoException
	 */
	List<WasteDisposaVO> getListWaste(String orgId,String date) throws DaoException;
	/**
	 * 根据如下条件获取企业信息
	 * @author wb
	 * @param licenseNo  营业执照号
	 * @param qsNo   生产许可证号
	 * @param businessName  企业名称
	 * @return  
	 */
	BussinessUnitVO getBussinessUnitVO(String licenseNo, String qsNo,
			String businessName);
}
