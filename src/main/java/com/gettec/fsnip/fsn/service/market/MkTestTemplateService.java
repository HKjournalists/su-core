package com.gettec.fsnip.fsn.service.market;

import java.util.List;
import java.util.Map;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.market.MkTestTemplate;
import com.gettec.fsnip.fsn.vo.business.report.BusinessUnitOfReportVO;
import com.gettec.fsnip.fsn.vo.report.ReportOfMarketVO;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;


public interface MkTestTemplateService {
	/**
	 * 功能描述：按barcode查找一条产品-生产企业-报告信息
	 * @author ZhangHui 2015/6/5
	 */
	public ReportOfMarketVO findReportByBarCode(String barcode, AuthenticateInfo info,Long currentUserOrganization) throws ServiceException;

	void save(MkTestTemplate template, boolean isNew) throws ServiceException;

	List<String> getListOfBarCode(Long orignizatonId) throws ServiceException;

	MkTestTemplate findByReportId(Long reportId) throws ServiceException;

	MkTestTemplate findTemplateByBarCode(String barcode, Long organization, String userName) throws ServiceException;

	List<MkTestTemplate> findTemplateByBarCode(String barcode, Long orignizatonId) throws ServiceException;

	/**
	 * 返回前台所有该产品绑定的qs号，用于用户下拉选择
	 */
	public List<Map<Object, String>> getBusNamesFromPro2Bus( List<BusinessUnitOfReportVO> pro2Bus);

	/**
	 * 根据企业名称，从已有的产品-生产企业-qs关系list中，获取一条信息
	 * @author ZhangHui 2015/5/1
	 */
	public BusinessUnitOfReportVO getPro2BusFromList(String name,List<BusinessUnitOfReportVO> list);

	/**
	 * 根据 barcode 和 organization 查找一条template模板信息id
	 * @author tangxin 2015/6/8
	 * @throws ServiceException 
	 */
	Long findIdByOrganizationAndUserName(String barcode, Long organization, String userName) throws ServiceException;

	/**
	 * 根据 id 更新 template 中的 barcode 和 reportid 字段 
	 * @author tangxin 2015/6/8
	 * @throws ServiceException
	 */
	boolean updateById(Long id, String barcode, Long reportId) throws ServiceException;

	/**
	 * 创建 template 信息 
	 * @author tangxin 2015/6/8
	 * @throws ServiceException
	 */
	boolean createBySql(String barcode, Long reportId, Long organization, String userName) throws ServiceException;

	/**
	 * 保存 template 信息
	 * @author tangxin 2015/6/8
	 * @throws ServiceException
	 */
	boolean saveTemplate(ReportOfMarketVO report_vo, AuthenticateInfo info, Long myOrganization) throws ServiceException;

	public ReportOfMarketVO findReportOfMarketByBarCode(String barcode,
			AuthenticateInfo info, Long currentUserOrganization,int page, int pageSize);

	public boolean deleteMkTempReportItem(long id);

}
