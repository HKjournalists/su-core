package com.gettec.fsnip.fsn.dao.account.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.persistence.Query;

import com.gettec.fsnip.fsn.dao.account.TZStockInfoDAO;
import com.gettec.fsnip.fsn.vo.account.ProductionDateVO;
import com.gettec.fsnip.fsn.vo.business.BusinessMarketVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.account.TZStockDAO;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.account.TZStock;
import com.gettec.fsnip.fsn.vo.account.ReturnProductVO;
import com.gettec.fsnip.fsn.vo.account.WholeSaleVO;

/**
 * Author:chenxiaolin
 * 
 */
@Repository(value = "tZStockDAO")
public class TZStockDAOImpl extends BaseDAOImpl<TZStock> implements TZStockDAO {

	@Autowired
	TZStockInfoDAO tzStockInfoDAO;

	@Override
	public long getMaxId() {
		String sql = " select MAX(id) from tz_stock ";
		Query query = entityManager.createNativeQuery(sql.toString());
		Object result = query.getSingleResult();
		return Long.parseLong(result.toString());

	}

	@SuppressWarnings("rawtypes")
	@Override
	public WholeSaleVO findByProductId(Long productId)throws DaoException {
		 String sql = " select pro.id,pro.name,pro.barcode,proToBus.QS_NO,pro.format,tzsto.product_num FROM product pro " +
		 		" LEFT JOIN tz_stock tzsto on pro.id = tzsto.product_id LEFT JOIN product_to_businessunit proToBus on " +
		 		" pro.id = proToBus.PRODUCT_ID  where pro.id = ?1 ";
	        try {
	        	 Query  query = entityManager.createNativeQuery(sql.toString()).setParameter(1, productId);
	             List resultList = query.getResultList();
	             Object[] result = null;
	             if(resultList.size()>0){
	            	 result =  (Object[]) resultList.get(0);
	             }
	             WholeSaleVO wholeSaleVO = null;
	             if(result.length>0&&result!=null){
	            	 wholeSaleVO = new WholeSaleVO();
	            	 wholeSaleVO.setId(Long.valueOf(result[0].toString()));
	            	 wholeSaleVO.setName(result[1]!=null?result[1].toString():"");
	            	 wholeSaleVO.setBarcode(result[2]!=null?result[2].toString():"");
	            	 wholeSaleVO.setQsNumber(result[3]!=null?result[3].toString():"");
	            	 wholeSaleVO.setFormat(result[4]!=null?result[4].toString():"");
	            	 wholeSaleVO.setCount(result[5]!=null?Long.valueOf(result[5].toString()):0);
	             }
	            return wholeSaleVO;
				} catch (Exception e) {
					throw new DaoException("TZStockDAOImpl.findByProductId() 根据产品ID封装库存商品,出现异常！", e);
	        }
	}

	/**
	 * 根据产品条形码加载产品基本信息
	 * @author HY
	 */
	@Override
	public ReturnProductVO findVOByBarcode(String barcode) throws DaoException {

		String sql = "SELECT p.id,p.name,p.barcode,p.format,ins.production_date,ins.batch_serial_no,qs.qs_no " +
				" FROM product p LEFT JOIN product_instance ins ON ins.product_id = p.id " +
				" LEFT JOIN product_to_businessunit ptb ON ptb.PRODUCT_ID = p.id " +
				" LEFT JOIN production_license_info qs ON qs.id = ptb.qs_id " +
				" WHERE p.barcode =?1 GROUP BY p.id ";
		try {
			Query query = entityManager.createNativeQuery(sql.toString()).setParameter(1, barcode);
			List<Object[]> result = query.getResultList();
			List<ReturnProductVO> lists = encapsulationResultToReturnProductVO(result);
			return lists!=null&&lists.size()>0?lists.get(0):null;
		}catch (Exception e){
			throw new DaoException("TZStockDAOImpl.findVOByBarcode()根据订单编号加载可以退货的产品,出现异常！",e);
		}
	}

	/**
	 * 按条形码查找库存中是否已存在相关信息
	 * @author HY
	 */
	@Override
	public List<TZStock> getStoreProductByBarcode(String barcode, Long org, String qs) throws DaoException {
		try{
			String sql = " select tzs.* from tz_stock tzs " +
					" LEFT JOIN product p on p.id = tzs.product_id " +
					" LEFT JOIN business_unit b on b.id = tzs.business_id " +
					" where p.barcode = ?1 AND tzs.qs_no = ?2 AND b.organization = ?3 GROUP BY tzs.id ";
			Query query = entityManager.createNativeQuery(sql,TZStock.class).setParameter(1, barcode).setParameter(2, qs).setParameter(3, org);
			List<TZStock> result = query.getResultList();

			return (result!=null&&result.size()>0) ? result:null;
		}catch (Exception e){
			throw new DaoException("TZStockDAOImpl.getStoreProductByBarcode()按条形码查找库存中是否已存在相关信息,异常",e);
		}
	}

	/**
	 * 根据自己企业的id和产品id查找相关的库存信息
	 * @author HY
	 */
	@Override
	public TZStock getByProIdAndSelfBusId(Long productId, Long outBusId,String qsNO) throws DaoException {
		String condition = " WHERE e.businessId =?1 AND e.productId = ?2 AND e.qsNo = ?3";
		List<TZStock> lists= null;
		try {
			lists = this.getListByCondition(condition, new Object[]{outBusId, productId,qsNO});
			return lists!=null&&lists.size()>0?lists.get(0):null;
		} catch (JPAException jpae) {
			throw new DaoException(jpae.getMessage(),jpae.getException());
		}
	}

	/**
	 * 封装可以退货的产品
	 * @author HY
	 */
	private List<ReturnProductVO> encapsulationResultToReturnProductVO(
			List<Object[]> result) throws ParseException {
		List<ReturnProductVO> lists = null;
		if(result!=null&&result.size()>0) {
			lists = new ArrayList<ReturnProductVO>();
			for(int i = 0; i < result.size(); i++) {
				Object[] objs = result.get(i);
				ReturnProductVO vo = new ReturnProductVO();
				vo.setProductId(objs[0] != null ? Long.valueOf(objs[0].toString()) : -1);
				vo.setName(objs[1] != null ? objs[1].toString() : "");
				vo.setBarcode(objs[2] != null ? objs[2].toString() : "");
				vo.setFormat(objs[3] != null ? objs[3].toString() : "");
				vo.setProductionDate(objs[4] != null ? objs[4].toString().substring(0,10) : null);
				vo.setBatch(objs[5] != null ? objs[5].toString() : "");
				vo.setQsNumber(objs[6] != null && !"".equals(objs[6].toString()) ? objs[6].toString() : "");
				lists.add(vo);
			}
		}
		return lists;
	}

	static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/*格式化时间*/
	private Date formatDate(String date) throws ParseException {
		Date time = SDF.parse(date);
		return time;
	}
	
	@Override
	public List<TZStock> getTZStockListByPage(int page, int pageSize)
			throws DaoException {
		try {
			return this.getPaging(page, pageSize, "");
		} catch (JPAException e) {
			throw new DaoException("TZStockDAOImpl.getTZStockListByPage() 获取库存商品列表,出现异常！", e);
		}
	}

	@Override
	public List<TZStock> getTZStockListByCurBusAndProId(Long inBusId,Long productId) throws DaoException {
		String condition = " WHERE e.businessId =?1 AND e.productId = ?2 ";
		try {
			return this.getListByCondition(condition, new Object[]{inBusId, productId});
		} catch (JPAException jpae) {
			throw new DaoException("TZStockDAOImpl.getTZStockListByCurBusAndProId() 获取库存商品列表,出现异常！", jpae);
		}
	}

	/**
	 * 根据产品条形码加载该产品对应的qs号
	 */
	@Override
	public List<String> getQsNoByBarcode(Long proId) throws DaoException {
		String sql = " SELECT qs.qs_no FROM product_to_businessunit ptb " +
					" LEFT JOIN production_license_info qs ON ptb.qs_id = qs.id " +
					" WHERE ptb.PRODUCT_ID = ?1 AND qs.qs_no IS NOT NULL GROUP BY qs.qs_no ";
		try {
			return this.getListBySQLWithoutType(String.class, sql, new Object[]{proId});
		} catch (JPAException jpae) {
			throw new DaoException(jpae.getMessage(),jpae.getException());
		}
	}

	@Override
	public TZStock findByProIdAndQsNo(Long busId, String qsNo,Long productId)throws DaoException {
			String condition = " WHERE e.businessId = ?1 AND e.productId = ?2 AND e.qsNo = ?3 ";
			try {
				List<TZStock> lists = this.getListByCondition(condition, new Object[]{busId,productId,qsNo});
				return (lists!=null && lists.size()>0) ? lists.get(0):null;
			} catch (JPAException e) {
				throw new DaoException(e.getMessage(),e.getException());
			}
	}
	/**
	 * 加载企业的所有类型
	 * @author HY
	 */
	@Override
	public List<BusinessMarketVO> loadBusinessType() throws DaoException {
		String sql = "  SELECT b.`type` FROM business_unit b WHERE b.`type` <> '' " +
				" AND b.`type` IS NOT NULL  AND LENGTH(b.`type`) >= 4 GROUP BY b.`type` ";

		try{
			Query query = entityManager.createNativeQuery(sql.toString());
			List<String> result = query.getResultList();
			List<BusinessMarketVO> list = null;
			if(result!=null && result.size() > 0){
				list = new ArrayList<BusinessMarketVO>();
				Map<String,Boolean> map = new HashMap<String, Boolean>();
				for(int i = 0; i < result.size(); i++){
					String objs = result.get(i);
					BusinessMarketVO vo = new BusinessMarketVO();
					String type = objs != null ? objs.toString():"";
					if(type.contains("生产企业")){
						type = "生产企业";
					}
					if(type.contains("流通企业")){
						type = "流通企业";
					}
					if(map == null || map.size()<1){
						vo.setId(Long.valueOf(i + 1));
						vo.setType(type);
						map.put(type,true);
						list.add(vo);
					}else if(map != null && map.get(type)==null){
						vo.setId(Long.valueOf(i + 1));
						vo.setType(type);
						map.put(type,true);
						list.add(vo);
					}
				}
			}
			return list;
		}catch (Exception e){
			throw new DaoException("TZStockDAOImpl.loadBusinessType()加载企业的所有类型出现异常",e);
		}
	}

	/**
	 * 查询报告转态更加企业类型，商品id，批次
	 * @author HY
	 */
	@Override
	public int getReportStatus(String batch, Long productId, String busType) throws DaoException {

		String condition = "";
		if(busType.contains("供应商")){
			condition += " AND t.publish_flag not in (4,5,7)";
		}else if(busType.contains("生产企业")){
			condition += " AND t.publish_flag =  1";
		}
		String sql = "SELECT count(t.id) FROM test_result t " +
				" LEFT JOIN product_instance pin ON pin.id = t.sample_id " +
				" WHERE pin.product_id = ?1 AND pin.batch_serial_no = ?2 "+ condition;
		try {
			Long count = this.countBySql(sql.toString(), new Object[]{productId, batch});
			int result = count > 0 ? 1:0;
			return result;
		} catch (JPAException jpae) {
			throw new DaoException(jpae.getMessage(),jpae.getException());
		}
	}

	/**
	 * 台帐 在商品入库页面加载已经添加商品入库了的产品
	 * @author HY
	 */
	@Override
	public List<ReturnProductVO> loadIntakeProductList(Long busId,String pname, String pbarcode, int page, int pageSize) throws DaoException {
		String condition = "";
		Map<String, String> param = new HashMap<String, String>();
		param.put("bId",busId+"");
		if(pname != null && !"".equals(pname)){
			condition += " AND p.name LIKE :pname ";
			param.put("pname","%" + pname + "%");
		}
		if(pbarcode != null && !"".equals(pbarcode)){
			condition += " AND p.barcode LIKE :pbar ";
			param.put("pbar","%" + pbarcode + "%");
		}
		try{
			String sql = " SELECT ts.id,p.name,p.barcode,sinf.product_format,sinf.qs_no,sinf.product_batch,sinf.date, " +
					" sinf.product_num FROM tz_stock ts LEFT JOIN tz_stock_info sinf ON ts.id = sinf.stock_id " +
					" LEFT JOIN product p ON p.id = sinf.product_id " +
					" WHERE ts.business_id = :bId AND sinf.intake = 1 AND sinf.type = 0 "+ condition +
					"GROUP BY sinf.product_id,sinf.qs_no,sinf.product_batch";
			Query query = entityManager.createNativeQuery(sql.toString());
			setParamtter(param,query);
			if(page > 0){
				query.setFirstResult((page-1)*pageSize).setMaxResults(pageSize);
			}
			List<Object[]> res = query.getResultList();

			return res!=null && res.size() > 0 ? encapsulationSaleGoodsVO(res) : null;
		}catch (Exception e){
			throw new DaoException("TZStockDAOImpl.loadIntakeProductList()在商品入库页面加载已经添加商品入库了的产品 ，出现异常",e);
		}
	}

	@Override
	public Long loadIntakeProductTotals(Long busId,String pname, String pbarcode) throws DaoException {

		String condition = "";
		Map<String, String> param = new HashMap<String, String>();
		param.put("bid",busId+"");
		if(pname != null && !"".equals(pname)){
			condition += " AND p.name LIKE :pname ";
			param.put("pname","%" + pname + "%");
		}
		if(pbarcode != null && !"".equals(pbarcode)){
			condition += " AND p.barcode LIKE :pbar ";
			param.put("pbar","%" + pbarcode + "%");
		}
		String sql = "SELECT count(DISTINCT sinf.product_id,sinf.qs_no,sinf.product_batch) FROM tz_stock ts " +
				" LEFT JOIN tz_stock_info sinf ON ts.id = sinf.stock_id " +
				" LEFT JOIN product p ON p.id = sinf.product_id " +
				" WHERE ts.business_id = :bid  AND sinf.intake = 1 AND sinf.type = 0 " + condition;
		try {
			Query query = entityManager.createNativeQuery(sql.toString());
			setParamtter(param,query);
			Object rtn = query.getSingleResult();
			return rtn == null ? 0 : Long.parseLong(rtn.toString());
		} catch (Exception jpae) {
			throw new DaoException("TZStockDAOImpl.loadIntakeProductTotals()在商品入库页面加载已经添加商品入库了的产品总数 ，出现异常",jpae);
		}
	}

	private void setParamtter(Map<String, String> param, Query query) {
		if(param!=null && param.size() > 0){
			Iterator keyIt = param.keySet().iterator();
			do{
				String key = (String) keyIt.next();
				String value = param.get(key);
				query.setParameter(key,value);
			} while (keyIt.hasNext());
		}
	}

	/**
	 * 封装批发的产品
	 * @type 1:生产企业 0：非生产企业
	 */
	private List<ReturnProductVO> encapsulationSaleGoodsVO(List<Object[]> result) throws ParseException, DaoException {
		List<ReturnProductVO> lists = null;
		if(result!=null&&result.size()>0) {
			lists = new ArrayList<ReturnProductVO>();
			for(int i = 0; i < result.size(); i++) {
				Object[] objs = result.get(i);
				ReturnProductVO vo = new ReturnProductVO();
				vo.setProductId(objs[0] != null ? Long.valueOf(objs[0].toString()) : -1);//产品ID
				vo.setName(objs[1] != null ? objs[1].toString() : "");//产品名称
				vo.setBarcode(objs[2] != null ? objs[2].toString() : "");//条形码
				vo.setFormat(objs[3] != null ? objs[3].toString() : "");//规格
				vo.setQsNumber(objs[4] != null ? objs[4].toString() : "");//QS
				vo.setBatch(objs[5] != null ? objs[5].toString() : "");
				String createDate = objs[6] != null ? objs[6].toString(): "";
				vo.setOverDate(createDate);// 创建日期
				Long totalCount = tzStockInfoDAO.getStockCountByStockId(vo.getProductId(),vo.getBatch());
				vo.setCount(totalCount);
				lists.add(vo);
			}
		}
		return lists;
	}
}
