package com.gettec.fsnip.fsn.dao.product.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.product.ProductToBusinessUnitDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.ProductToBusinessUnit;
import com.gettec.fsnip.fsn.vo.business.report.BusinessUnitOfReportVO;
import com.gettec.fsnip.fsn.vo.product.ProductOfMarketVO;

/**
 * ProductToBusinessUnitDao customized operation implementation
 * 
 * @author Hui Zhang
 */
@Repository(value="productToBusinessUnitDAO")
public class ProductToBusinessUnitDAOImpl extends BaseDAOImpl<ProductToBusinessUnit>
		implements ProductToBusinessUnitDAO {

	/**
	 * 背景：生产企业 报告录入界面，产品条形码下拉选择
	 * 功能描述：查找当前登录的生产企业的所有产品条形码
	 * @author ZhangHui 2015/6/5
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProductOfMarketVO> getListBarcodeByOrganization(Long organization) throws DaoException {
		try {
			if(organization==null){
				throw new Exception("参数为空");
			}
			
			String sql = "SELECT pro.barcode,pro.id,pro.name FROM product pro " +
					 	 "WHERE pro.organization = ?1";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, organization);
			
			List<Object[]> result = query.getResultList();
			List<ProductOfMarketVO> vos = new ArrayList<ProductOfMarketVO>();
			if(result!=null && result.size() > 0){
				for(Object[] obj : result){
					ProductOfMarketVO vo = new ProductOfMarketVO();
					vo.setBarcode(obj[0]==null?"":obj[0].toString());
					vo.setId(Long.valueOf(obj[1].toString()));
					vo.setName(obj[2]==null?"":obj[2].toString());
					vos.add(vo);
				}
			}
			
			return vos;
		} catch (Exception e) {
			throw new DaoException("BusinessUnitDAOImpl.findBusUnitVO()-->" + e.getMessage(), e);
		}
	}

	/**
	 * 功能描述：根据产品id和企业id，查找一条有效的 企业-产品-qs 关系
	 * @author ZhangHui 2015/6/4
	 * @throws DaoException 
	 */
	@Override
	public ProductToBusinessUnit find(Long product_id, Long business_id) throws DaoException {
		try {
			if(product_id==null || business_id==null){
				throw new Exception("参数为空");
			}
			
			String condition = " WHERE e.product_id = ?1 AND e.business_id = ?2";
			
			List<ProductToBusinessUnit> result = this.getListByCondition(condition, new Object[]{product_id, business_id});
			
			if(result.size() > 0){
				return result.get(0);
			}
			
			return null;
		} catch (Exception e) {
			throw new DaoException("ProductToBusinessUnitDAOImpl.find()-->" + e.getMessage(), e);
		}
	}
	
	/**
	 * 功能描述：根据产品 barcode 查找 企业-产品-qs 集合
	 * @author ZhangHui 2015/6/5
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BusinessUnitOfReportVO> getListByBarcode(String barcode) throws DaoException {
		try {
			if(barcode==null || "".equals(barcode)){
				throw new Exception("参数为空");
			}
			
			String sql = "SELECT bus.id,bus.`name`,bus.license_no,bus.address,p2b.bind,p2b.effect,bus.organization,pli.qs_no,pli.qsformat_id " +
						 "FROM product_to_businessunit p2b " +
						 "INNER JOIN production_license_info pli ON pli.id = p2b.qs_id " +
						 "INNER JOIN business_unit bus ON bus.id = p2b.business_id " +
						 "INNER JOIN product pro ON pro.id = p2b.PRODUCT_ID AND pro.barcode = ?1";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, barcode);
			
			List<Object[]> result = query.getResultList();
			List<BusinessUnitOfReportVO> vos = new ArrayList<BusinessUnitOfReportVO>();
			if(result.size() > 0){
				for(Object[] obj : result){
					BusinessUnitOfReportVO vo = new BusinessUnitOfReportVO(
							((BigInteger)obj[0]).longValue(),
							obj[1]==null?"":obj[1].toString(),
							obj[2]==null?"":obj[2].toString(),
							obj[3]==null?"":obj[3].toString(),
							Integer.parseInt(String.valueOf(obj[4].toString())),
							Integer.parseInt(String.valueOf(obj[5].toString())),
							obj[6]==null?null:((BigInteger)obj[6]).longValue(),
							obj[7]==null?"":obj[7].toString(),
							obj[8]==null?1:Integer.parseInt(String.valueOf(obj[8].toString()))
						);
					vos.add(vo);
				}
			}
			
			return vos;
		} catch (Exception e) {
			throw new DaoException("BusinessUnitDAOImpl.findBusUnitVO()-->" + e.getMessage(), e);
		}
	}
	
	/**
	 * 功能描述：根据产品条形码 和 生产许可证编码，查找一条生产企业信息
	 * @author ZhangHui 2015/6/4
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BusinessUnitOfReportVO getBusUnitOfReportVOByQsno(String barcode, String qsno) throws DaoException{
		try {
			if(barcode==null || qsno==null || "".equals(barcode) || "".equals(qsno)){
				throw new Exception("参数为空");
			}
			
			String sql = "SELECT bus.id,bus.`name`,bus.license_no,bus.address,p2b.bind,p2b.effect,bus.organization " +
						 "FROM product_to_businessunit p2b " +
						 "INNER JOIN business_unit bus ON bus.id = p2b.business_id " +
						 "INNER JOIN production_license_info pli ON pli.id = p2b.qs_id AND pli.qs_no = ?1 " +
						 "INNER JOIN product pro ON pro.id = p2b.PRODUCT_ID AND pro.barcode = ?2";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, qsno);
			query.setParameter(2, barcode);
			
			List<Object[]> result = query.getResultList();
			
			if(result.size() > 0){
				Object[] obj = result.get(0);
				BusinessUnitOfReportVO vo = new BusinessUnitOfReportVO(
						((BigInteger)obj[0]).longValue(),
						obj[1]==null?"":obj[1].toString(),
						obj[2]==null?"":obj[2].toString(),
						obj[3]==null?"":obj[3].toString(),
						Integer.parseInt(String.valueOf(obj[4].toString())),
						Integer.parseInt(String.valueOf(obj[5].toString())),
						((BigInteger)obj[6]).longValue()
					);
				return vo;
			}
			
			return null;
		} catch (Exception e) {
			throw new DaoException("BusinessUnitDAOImpl.findBusUnitVO()-->" + e.getMessage(), e);
		}
	}
	
	/**
	 * 背景：在报告录入界面
	 * 功能描述：根据营业执照号，查找一条生产企业信息
	 * @author ZhangHui 2015/6/4
	 * @throws ServiceException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BusinessUnitOfReportVO getBusUnitOfReportVOLicenseNo(String licenseNo) throws DaoException {
		try {
			if(licenseNo==null || "".equals(licenseNo)){
				return null;
			}
			
			String sql = "SELECT bus.id,bus.`name`,bus.license_no,bus.address,bus.organization " +
						 "FROM business_unit bus " +
						 "WHERE bus.license_no = ?1";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, licenseNo);
			
			List<Object[]> result = query.getResultList();
			
			if(result.size() > 0){
				Object[] obj = result.get(0);
				BusinessUnitOfReportVO vo = new BusinessUnitOfReportVO(
						((BigInteger)obj[0]).longValue(),
						obj[1]==null?"":obj[1].toString(),
						obj[2]==null?"":obj[2].toString(),
						obj[3]==null?"":obj[3].toString(),
						obj[4]==null?null:((BigInteger)obj[4]).longValue()
					);
				return vo;
			}
			
			return null;
		} catch (Exception e) {
			throw new DaoException("BusinessUnitDAOImpl.findBusUnitVO()-->" + e.getMessage(), e);
		}
	}
	
	/**
	 * 背景：在报告录入界面
	 * 功能描述：根据营业执照号，查找一条生产企业信息
	 * @author ZhangHui 2015/6/4
	 * @throws ServiceException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BusinessUnitOfReportVO getBusUnitOfReportVOLicenseNo(String licenseNo, String barcode) throws DaoException {
		try {
			if(licenseNo==null || "".equals(licenseNo) || barcode==null || "".equals(barcode)){
				return null;
			}
			
			String sql = "SELECT bus.id,bus.`name`,bus.license_no,bus.address,p2b.bind,p2b.effect,bus.organization,pli.qs_no,pli.qsformat_id " +
					 	 "FROM product_to_businessunit p2b " +
					 	 "INNER JOIN production_license_info pli ON pli.id = p2b.qs_id " +
					 	 "INNER JOIN business_unit bus ON bus.id = p2b.business_id AND bus.license_no = ?1 " +
					 	 "INNER JOIN product pro ON pro.id = p2b.PRODUCT_ID AND pro.barcode = ?2";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, licenseNo);
			query.setParameter(2, barcode);
			
			List<Object[]> result = query.getResultList();
			
			if(result.size() > 0){
				Object[] obj = result.get(0);
				BusinessUnitOfReportVO vo = new BusinessUnitOfReportVO(
						((BigInteger)obj[0]).longValue(),
						obj[1]==null?"":obj[1].toString(),
						obj[2]==null?"":obj[2].toString(),
						obj[3]==null?"":obj[3].toString(),
						Integer.parseInt(String.valueOf(obj[4].toString())),
						Integer.parseInt(String.valueOf(obj[5].toString())),
						obj[6]==null?null:((BigInteger)obj[6]).longValue(),
						obj[7]==null?"":obj[7].toString(),
						obj[8]==null?1:Integer.parseInt(String.valueOf(obj[8].toString()))
					);
				return vo;
			}
			
			return null;
		} catch (Exception e) {
			throw new DaoException("BusinessUnitDAOImpl.findBusUnitVO()-->" + e.getMessage(), e);
		}
	}
	
	/**
	 * 背景：在报告录入界面，根据生产企业名称和产品条形码，未找到任何一条生产企业信息
	 * 功能描述：根据生产企业名称，查找一条生产企业信息
	 * @author ZhangHui 2015/6/4
	 * @throws ServiceException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BusinessUnitOfReportVO getBusUnitOfReportVOByBusname(String bus_name) throws DaoException{
		try {
			if(bus_name==null || "".equals(bus_name)){
				return null;
			}
			
			String sql = "SELECT bus.id,bus.`name`,bus.license_no,bus.address,bus.organization " +
						 "FROM business_unit bus " +
						 "WHERE bus.name = ?1";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, bus_name);
			
			List<Object[]> result = query.getResultList();
			
			if(result.size() > 0){
				Object[] obj = result.get(0);
				BusinessUnitOfReportVO vo = new BusinessUnitOfReportVO(
						((BigInteger)obj[0]).longValue(),
						obj[1]==null?"":obj[1].toString(),
						obj[2]==null?"":obj[2].toString(),
						obj[3]==null?"":obj[3].toString(),
						obj[4]==null?null:((BigInteger)obj[4]).longValue()
					);
				return vo;
			}
			
			return null;
		} catch (Exception e) {
			throw new DaoException("BusinessUnitDAOImpl.findBusUnitVO()-->" + e.getMessage(), e);
		}
	}

	/**
	 * 背景：在报告录入界面
	 * 功能描述：根据生产企业名称和产品条形码，查找一条生产企业信息（包括生产许可证信息）
	 * @author ZhangHui 2015/6/4
	 * @throws DaoException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BusinessUnitOfReportVO getBusUnitOfReportVOByBusname(String bus_name, String barcode) throws DaoException {
		try {
			if(bus_name==null || "".equals(bus_name) || barcode==null || "".equals(barcode)){
				return null;
			}
			
			String sql = "SELECT bus.id,bus.`name`,bus.license_no,bus.address,p2b.bind,p2b.effect,bus.organization,pli.qs_no,pli.qsformat_id " +
						 "FROM product_to_businessunit p2b " +
						 "INNER JOIN production_license_info pli ON pli.id = p2b.qs_id " +
						 "INNER JOIN business_unit bus ON bus.id = p2b.business_id AND bus.name = ?1 " +
						 "INNER JOIN product pro ON pro.id = p2b.PRODUCT_ID AND pro.barcode = ?2";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, bus_name);
			query.setParameter(2, barcode);
			
			List<Object[]> result = query.getResultList();
			
			if(result.size() > 0){
				Object[] obj = result.get(0);
				BusinessUnitOfReportVO vo = new BusinessUnitOfReportVO(
						((BigInteger)obj[0]).longValue(),
						obj[1]==null?"":obj[1].toString(),
						obj[2]==null?"":obj[2].toString(),
						obj[3]==null?"":obj[3].toString(),
						Integer.parseInt(String.valueOf(obj[4].toString())),
						Integer.parseInt(String.valueOf(obj[5].toString())),
						obj[6]==null?null:((BigInteger)obj[6]).longValue(),
						obj[7]==null?"":obj[7].toString(),
						obj[8]==null?1:Integer.parseInt(String.valueOf(obj[8].toString()))
					);
				return vo;
			}
			
			return null;
		} catch (Exception e) {
			throw new DaoException("BusinessUnitDAOImpl.getBusUnitOfReportVOByBusname()-->" + e.getMessage(), e);
		}
	}

	/**
	 * 功能描述：根据产品id和企业组织机构，查找qs号
	 * @author ZhangHui 2015/6/5
	 * @throws DaoException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Long getQsId(Long product_id, Long organization) throws DaoException {
		try {
			if(product_id==null || organization==null){
				throw new Exception("参数为空");
			}
			
			String sql = "SELECT p2b.qs_id FROM product_to_businessunit p2b " +
						 "INNER JOIN business_unit bus ON bus.id = p2b.business_id AND bus.organization = ?1 " +
						 "WHERE p2b.PRODUCT_ID = ?2 AND p2b.effect = 1 AND p2b.bind = 1";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, organization);
			query.setParameter(2, product_id);
			
			List<Object[]> result = query.getResultList();
			
			if(result.size() > 0){
				Object obj = result.get(0);
				return obj==null?null:((BigInteger)obj).longValue();
			}
			
			return null;
		} catch (Exception e) {
			throw new DaoException("BusinessUnitDAOImpl.getQsId()-->" + e.getMessage(), e);
		}
	}
	
	/**
	 * 功能描述：将企业-产品-qs号 设置为有效/无效
	 * @param effect
	 *          0 代表设置为无效
	 *          1 代表设置为有效
	 * @author Zhanghui 2015/6/15
	 * @throws DaoException 
	 */
	@Override
	public void updateByEffect(long qs_id, long business_id, int effect) throws DaoException {
		try {
			String sql = "UPDATE product_to_businessUnit SET effect = ?1 WHERE qs_id = ?2 AND business_id = ?3";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, effect);
			query.setParameter(2, qs_id);
			query.setParameter(3, business_id);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("Business2QsRelationDAOImpl.updateByEffect()-->" + e.getMessage(), e);
		}
	}

	/**
	 * 功能描述：除了当前qs号的主企业，将其他被授权企业-产品-qs号 设置为有效/无效
	 * @param effect
	 *          0 代表设置为无效
	 *          1 代表设置为有效
	 * @author Zhanghui 2015/6/15
	 * @throws DaoException 
	 */
	@Override
	public void updateExOwnerbusByEffect(Long qs_id, Long owner_bus_id, int effect) throws DaoException {
		try {
			String sql = "UPDATE product_to_businessUnit SET effect = ?1 WHERE qs_id = ?2 AND business_id <> ?3";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, effect);
			query.setParameter(2, qs_id);
			query.setParameter(3, owner_bus_id);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("Business2QsRelationDAOImpl.updateExOwnerbusByEffect()-->" + e.getMessage(), e);
		}
	}
}