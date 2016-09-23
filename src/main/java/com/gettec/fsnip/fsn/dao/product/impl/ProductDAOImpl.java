package com.gettec.fsnip.fsn.dao.product.impl;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.product.ProductDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessBrand;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.transfer.ProductTransfer;
import com.gettec.fsnip.fsn.util.ImgUtils;
import com.gettec.fsnip.fsn.util.sales.SalesUtil;
import com.gettec.fsnip.fsn.vo.ProductStaVO;
import com.gettec.fsnip.fsn.vo.business.BusinessLicenseLismVo;
import com.gettec.fsnip.fsn.vo.product.ProductLismVo;
import com.gettec.fsnip.fsn.vo.product.ProductManageViewVO;
import com.gettec.fsnip.fsn.vo.sales.DetailAlbumVO;
import com.gettec.fsnip.fsn.vo.sales.PhotoFieldVO;
import com.lhfs.fsn.util.StringUtil;
import com.lhfs.fsn.vo.product.ProductBarcodeToQRcodeVO;
import com.lhfs.fsn.vo.product.ProductListVo;
import com.lhfs.fsn.vo.product.ProductNutritionVO;
import com.lhfs.fsn.vo.product.ProductRiskVo;


/**
 * Product customized operation implementation
 * 
 * @author Ryan Wang
 */
@Repository(value="productDAO")
public class ProductDAOImpl extends BaseDAOImpl<Product>
		implements ProductDAO {
	@PersistenceContext
	private EntityManager entityManager;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	@SuppressWarnings("unchecked")
	public List<Product> findProducts(String name, Long businessUnitId, Long businessBrandId,  List<Long> producerIds, Long productGroupId,int pageSize,int page) {
		List<Product> result = null;
		
		int begin=(page-1)*pageSize;
		StringBuilder sql = new StringBuilder();
		sql.append("select * from product p ");
		if(businessBrandId != null || businessUnitId != null){
			sql.append("inner join business_brand bb on p.business_brand_id = bb.id ");
		}
		if(businessUnitId != null){
			sql.append("inner join business_unit bu on bb.business_unit_id = bu.id ");
		}
		sql.append("where 1 = 1 ");
		if(StringUtils.isNotBlank(name)){
			sql.append("and p.name like :name ");
		}
		if(businessBrandId != null){
			sql.append("and bb.id = :business_brand_id ");
		}
		if(businessUnitId != null){
			sql.append("and bu.id = :business_unit_id ");
		}
		if(producerIds != null && producerIds.size() > 0){
			sql.append(" and (1 != 1 ");
			for(int idx = 0; idx < producerIds.size(); idx++){
				sql.append(" or p.producer_id = :producer_id" + idx);
			}
			sql.append(") ");
		}
		if(productGroupId != null){
			sql.append("and p.fda_product_group = :fda_product_group ");
		}
		sql.append("order by p.id desc");
		sql.append(" limit "+begin+","+pageSize);
		Query query = entityManager.createNativeQuery(sql.toString(), Product.class);
		if(StringUtils.isNotBlank(name)){
			query.setParameter("name", "%" + name + "%");
		}
		if(businessBrandId != null){
			query.setParameter("business_brand_id", businessBrandId);
		}
		if(businessUnitId != null){
			query.setParameter("business_unit_id", businessUnitId);
		}
		if(producerIds != null && producerIds.size() > 0){
			sql.append(" and (1 != 1 ");
			for(int idx = 0; idx < producerIds.size(); idx++){
				query.setParameter("producer_id" + idx, producerIds.get(idx));
			}
			sql.append(") ");
		}
		if(productGroupId != null){
			query.setParameter("fda_product_group", productGroupId);
		}
		
		result = query.getResultList();

		return result;
	}

	/**
	 * 按barcode查找产品信息
	 * @param barcode
	 * @return
	 */
	public Product findByBarcode(String barcode) throws DaoException {
		try {
			String condition = " WHERE e.barcode = ?1";
			List<Product> result = this.getListByCondition(condition, new Object[]{barcode});
			if(result.size()>0){
				return result.get(0);
			}
			return null;
		} catch (JPAException jpae) {
			throw new DaoException("【DAO-error】按产品条形码查找一条产品信息，出现异常", jpae.getException());
		}
	}

	@Override
	public List<Product> getListByBarcode(String barcode) throws DaoException {
		try {
			String condition = " WHERE e.barcode Like ?1";
			return ProductTransfer.transfer(this.getListByCondition(condition, new Object[]{"%" + barcode + "%"}));
		} catch (JPAException jpae) {
			throw new DaoException("【DAO-error】按产品条形码查找产品信息，出现异常", jpae.getException());
		}
	}
	
	/**
	 * 根据条件获取条形码集合有分页处理
	 * @param condition 
	 * @param page
	 * @param pageSize
	 * @return List<String>
	 * @throws DaoException
	 * @author LongXianZhen
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getBarcodeListByCondition(String condition,int page ,int pageSize) throws DaoException{
		try {
			List<String> barcodes = null;
			if (StringUtils.isNotBlank(condition)) {
				String sql = "select barcode from product where barcode like :barcode ";
				Query query=entityManager.createNativeQuery(sql)
						.setParameter("barcode", "%"+condition+"%");
				if (page > 0) {
					query.setFirstResult((page - 1) * pageSize);
					query.setMaxResults(pageSize);
				}
				barcodes =query.getResultList();
			}  
			return barcodes;
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.getBarcodeListByCondition()根据条件获取条形码集合有分页处理, 出现异常！", e);
		}
	}

	/**
	 * 按组织机构Id获取产品总数
	 */
	@Override
	public long countByCondition(Map<String, Object> map) throws DaoException {
		try {
			String condition = (String) map.get("condition");
			Object[] params = (Object[]) map.get("params");
			if(condition != null) {
				condition += " and packageFlag = '0' ";
			}
			return this.count(condition, params);
		} catch (JPAException jpae) {
			throw new DaoException("【dao-error】按组织机构Id获取产品总数，出现异常！", jpae.getException());
		}
	}

	/**
	 * 按组织机构Id获取某一分页的产品集合
	 * @throws DaoException 
	 */
	@Override
	public List<Product> getListOfProductByConditionWithPage(
			Map<String, Object> map, int page, int pageSize) throws DaoException {
		try {
			String condition = (String) map.get("condition");
			Object[] params = (Object[]) map.get("params");
			condition += " and e.packageFlag = '0' order by e.lastModifyTime desc ";
			return this.getListByPage(page, pageSize, condition, params);
		} catch (JPAException jpae) {
			throw new DaoException("【dao-error】organization，出现异常！", jpae.getException());
		}
	}
	
	/**
	 * 按组织机构Id获取某一分页的产品集合(不包括已经绑定qs的产品)
	 * @throws DaoException 
	 */
	@Override
	public List<Product> getListOfProductByConditionOfSonWithPage(
			Map<String, Object> map, int page, int pageSize) throws DaoException {
		try {
			String condition = (String) map.get("condition");
			Object[] params = (Object[]) map.get("params");
			return this.getListByPage(page, pageSize, condition, params);
		} catch (Exception e) {
			throw new DaoException("【dao-error】按组织机构Id获取某一分页的产品集合(不包括已经绑定qs的产品)，出现异常！", e);
		}
	}

	/**
	 * dao层根据名称模糊查询产品列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Product> searchProductListByName(String name)
			throws DaoException {
		List<Product> product=null;
		try {
			String jpql="SELECT * FROM product where name like ?1";
			product = entityManager.createNativeQuery(jpql,Product.class)
					.setParameter(1, "%"+name+"%").getResultList();
		} catch (Exception e) {
			throw new DaoException("dao层根据名称模糊查询产品列表出错", e);
		}
		return product;
	}

	/**
	 * dao层根据名称模糊查询产品数量
	 */
	@Override
	public long getCountByName(String name) throws DaoException {
		int count = 0;
		try {
			String jpql = "SELECT count(*) FROM product where name like ?1";
			Query query = entityManager.createNativeQuery(jpql).setParameter(1, "%"+name+"%");
			Number result=(Number)query.getSingleResult();
			count=result.intValue();
		} catch (Exception e) {
			throw new DaoException("dao层根据名称模糊查询产品数量出错", e);
		}
		return count;
	}
	
	public List<Product> getAllProductsByOrg(long organization){
		String jpql="SELECT id,name,barcode,format from product where organization=?1";
		Query query = entityManager.createNativeQuery(jpql).setParameter(1,organization);
		List<Object[]> objList=query.getResultList();
		List<Product> productList=new ArrayList<Product>();
		for(Object obj[]:objList){
			Product product=new Product();
			product.setId(Long.valueOf(obj[0].toString()));
			product.setName(obj[1].toString()+obj[3].toString());
			product.setBarcode(obj[2].toString());
			productList.add(product);
		}
		return productList;
	}

	/**
	 * 根据条件（产品名称或条形码）分页查询某个企业的产品列表
	 * @param bu 生产企业
	 * @param page 第几页
	 * @param pageSize 每页显示条数
	 * @param productName 产品名称
	 * @param barcode 条形码
	 * @return List<Product>
	 * @throws DaoException
	 * @author LongXianZhen
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getProListByBusiness(BusinessUnit bu, int page,
			int pageSize, String productName, String barcode) throws DaoException {
		try {
			boolean nemaFlag=!productName.equals("")&&productName!=null;
			boolean barFlag=!barcode.equals("")&&barcode!=null;
			String proCofig="";
			if(nemaFlag||barFlag){
				if(nemaFlag){
					proCofig=proCofig+" AND e.name like '%"+productName+"%' ";
				}
				if(barFlag){
					proCofig=proCofig+" AND e.barcode like '%"+barcode+"%' ";
				}
			}
			String pageSql=" ";
			if(page!=0&&pageSize!=0){
				pageSql=" LIMIT " +(page - 1) * pageSize+","+pageSize;
			}
			String	sql="SELECT * FROM product e WHERE e.organization="+ bu.getOrganization()+" "+proCofig+pageSql;
			List<Product> products = entityManager.createNativeQuery(sql,Product.class).getResultList();
			return products;
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.getProListByBusiness()-->根据条件（产品名称或条形码）分页查询某个企业的产品列表出错", e);
		}
	}

	/**
	 * 根据条件查询某个企业下产品总数
	 * @param organizationId 企业组织机构ID
	 * @param productName 产品名称
	 * @param barcode 条形码
	 * @return Long
	 * @throws DaoException
	 * @author LongXianZhen
	 */
	@Override
	public Long getProductStaCountByConfigure(Long organizationId,
			String productName, String barcode)
			throws DaoException {
		try {
			boolean nemaFlag=!productName.equals("")&&productName!=null;
			boolean barFlag=!barcode.equals("")&&barcode!=null;
			String proCofig="";
			if(nemaFlag||barFlag){
				if(nemaFlag){
					proCofig=proCofig+" AND e.name like '%"+productName+"%' ";
				}
				if(barFlag){
					proCofig=proCofig+" AND e.barcode like '%"+barcode+"%' ";
				}
			}
			String sql="SELECT count(*) FROM product e WHERE e.organization="+ organizationId +" "+proCofig;

			Object rtn =  entityManager.createNativeQuery(sql).getSingleResult();
			return rtn == null ? 0 : Long.parseLong(rtn.toString());
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.getProductStaCountByConfigure()-->根据条件查询某个企业下产品总数出错", e);
		}
	}

	/**
	 * 按产品名称和规格查找产品集合。
	 * @param name 
	 * @param format
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public List<Product> getListByNameAndFormat(String name, String format) throws DaoException {
		try {
			String condition = " WHERE e.name = ?1 AND e.format = ?2";
			Object[] params = new Object[]{name, format};
			return this.getListByCondition(condition, params);
		} catch (Exception e) {
			throw new DaoException("【DAO-error】按产品名称和规格查找产品集合，出现异常", e);
		}
	}

	/**
	 * 按产品别名和规格查找产品集合。
	 * @param otherName 
	 * @param format
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public List<Product> getListByOtherNameAndFormat(String otherName, String format) throws DaoException {
		try {
			String condition = " WHERE e.otherName = ?1 AND e.format = ?2";
			Object[] params = new Object[]{otherName, format};
			return this.getListByCondition(condition, params);
		} catch (Exception e) {
			throw new DaoException("【DAO-error】按产品名称和规格查找产品集合，出现异常", e);
		}
	}
	
	/**
	 * 按产品别名和规格查找产品集合。
	 * @param otherName 
	 * @param format
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public List<Product> getListByOtherNameAndPDFformat(String otherName, String pdfFormat, boolean isObscure) throws DaoException {
		try {
			String condition = "";
			Object[] params = new Object[]{pdfFormat};
			if(!isObscure){
				condition = " WHERE e.otherName = ?1 AND e.pdfFormat = ?2";
				params = new Object[]{otherName, pdfFormat};
			}else{
				condition = " WHERE e.otherName LIKE '%" + otherName + "%' AND e.pdfFormat = ?1";
			}
			return this.getListByCondition(condition, params);
		} catch (Exception e) {
			throw new DaoException("【DAO-error】按产品名称和规格查找产品集合，出现异常", e);
		}
	}

	/**
	 * 按热词查找产品条数。
	 * @param organization  组织机构id
	 * @param hotWords  热词集合
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public long countByHotWord(Long organization, String hotWord) throws DaoException {
		try {
			String sql = "SELECT count(*)" +
					" FROM product, nutri_rpt" +
					" WHERE product.organization = " + organization +
					" AND product.id = nutri_rpt.product_id" +
					" AND nutri_rpt.`name` = '" + hotWord + "'";
			Query query = entityManager.createNativeQuery(sql.toString());
			Number result=(Number) query.getSingleResult();
			int count = result.intValue();
			return count;
		} catch (Exception e) {
			throw new DaoException("【DAO-error】按热词查找产品条数，出现异常", e);
		}
	}

	/**
	 * 按热词查找产品集合。
	 * @param organization  组织机构id
	 * @param hotWords  热词集合
	 * @return
	 * @throws DaoException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getListByHotWordWithPage(Long organization,
			String hotWord, int page, int pageSize) throws DaoException {
		try {
			String sql = "SELECT product.id, product.`name`" +
					" FROM product, nutri_rpt" +
					" WHERE product.organization = " + organization +
					" AND product.id = nutri_rpt.product_id" +
					" AND nutri_rpt.`name` = '" + hotWord + "'";
			int begin=(page-1)*pageSize;
			sql += " limit "+ begin + "," + pageSize;
			
			Query query = entityManager.createNativeQuery(sql.toString());
			List<Object[]> result = query.getResultList();
			List<Product> products = new ArrayList<Product>();
			for(Object[] obj : result){
				Product product = new Product(((BigInteger)obj[0]).longValue(), obj[1].toString());
				products.add(product);
			}
			return products;
		} catch (Exception e) {
			throw new DaoException("【DAO-error】按热词查找产品集合，出现异常", e);
		}
	}
	/***
	 * 根据商品库存查找不重复的商品信息且库存大于0
	 * @param organization  组织机构id
	 * @return 商品信息集合
	 * @throws DaoException
	 * @author 郝圆彬
	 * 2014-10-28
	 * 修改
	 */
	@SuppressWarnings("unchecked")
	public List<Product> getListByStorageInfo(Long organization) throws DaoException {
		List<Product> result = null;
		try{
			String sql ="SELECT   DISTINCT p.* FROM product p "
								+"inner JOIN t_meta_merchandise_info_instance i on  p.id=i.product_id "
								+"inner JOIN t_buss_merchandise_storage_info s on i.INSTANCE_ID=s.NO_2 where s.SYS_COUNT>0 and s.organization=:organization";
			Query query = entityManager.createNativeQuery(sql.toString(),Product.class);
			query.setParameter("organization", organization);
			result = query.getResultList();
			return result;
	} catch (Exception e) {
		throw new DaoException("【DAO-error】根据库存查询产品集合，出现异常", e);
	}
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getListProductByIds(List<Long> ids) {
		String sql = "SELECT e FROM product e where e.id in(:ids)";
		Query query = entityManager.createQuery(sql);
		List<Product> list = new ArrayList<Product>();
		try {
			query.setParameter("ids", ids);
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 根据组织机构查找企业本地产品
	 * @param page 
	 * @param size 
	 * @param organization 
	 * @return
	 * @throws DaoException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getAllLocalProduct(int page, int size,Long organization) throws DaoException {
		try {
			String sql = "SELECT DISTINCT p.* FROM product p "
					+"Inner JOIN business_unit b ON p.producer_id=b.id "
					+"LEFT JOIN t_meta_initialize_product t ON p.id=t.product_id AND t.organization=?1 "
					+"WHERE b.organization=?2 AND t.first_storage_id is NULL ";
	
			Query query = entityManager.createNativeQuery(sql.toString(),Product.class);
			query.setParameter(1, organization);
			query.setParameter(2, organization);
			if (page > 0) {
				query.setFirstResult((page - 1) * size);
				query.setMaxResults(size);
			}
			List<Product> result = query.getResultList();
			return result;
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.getAllLocalProduct() 根据组织机构查找企业本地产品,出现异常！", e);
		}
	}
	
	/**
	 * 根据组织机构查找企业引进产品列表（分页）
	 * @param page 
	 * @param size 
	 * @param organization 
	 * @return
	 * @throws DaoException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getAllNotLocalProduct(int page, int size,Long organization) throws DaoException {
		try {
			String sql="SELECT  p.* FROM product p "
					+"RIGHT JOIN t_meta_initialize_product t ON p.id=t.product_id "
					+"WHERE t.`local`=0 AND t.first_storage_id IS NULL AND t.organization=?1";
			Query query = entityManager.createNativeQuery(sql.toString(),Product.class);
			query.setParameter(1, organization);
			if (page > 0) {
				query.setFirstResult((page - 1) * size);
				query.setMaxResults(size);
			}
			List<Product> result = query.getResultList();
			return result;	
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.getAllNotLocalProduct() 根据组织机构查找企业引进产品，出现异常！", e);
		}
	}

	/**
	 * 根据组织机构查找企业本地产品总数
	 * @param page 
	 * @param size 
	 * @param organization 
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public Long getCountOfAllLocalProduct(Long organization) throws DaoException {
		try {
			String sql = "SELECT DISTINCT count(DISTINCT p.id) FROM product p "
					+"RIGHT JOIN business_unit b ON p.producer_id=b.id "
					+"LEFT JOIN t_meta_initialize_product t ON p.id=t.product_id AND t.organization=?1 "
					+"WHERE b.organization=?2 AND t.first_storage_id is NULL ";
			Query query = entityManager.createNativeQuery(sql.toString());
			query.setParameter(1, organization);
			query.setParameter(2, organization);
			return ((Number)query.getSingleResult()).longValue();
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.getCountOfAllLocalProduct() 根据组织机构查找企业本地产品总数,出现异常！", e);
		}
	}

	/**
	 * 根据组织机构查找企业引进产品总数
	 * @param page 
	 * @param size 
	 * @param organization 
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public Long getCountOfAllNotLocalProduct(Long organization) throws DaoException {
		try {
			String sql = "SELECT count(*) FROM product p "
					+"RIGHT JOIN t_meta_initialize_product t ON p.id=t.product_id "
					+"WHERE t.`local`=0 AND t.first_storage_id IS NULL AND t.organization=?1";
			Query query = entityManager.createNativeQuery(sql.toString());
			query.setParameter(1, organization);
			return ((Number)query.getSingleResult()).longValue();
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.getCountOfAllNotLocalProduct() 根据组织机构查找企业引进产品总数,出现异常！", e);
		}
	}

	/**
	 * 新增产品时条形码的唯一性
	 */
	@Override
	public boolean checkExistBarcode(String barcode) throws DaoException {
		try {
			String condition = " where e.barcode = ?1 ";
			long total =  this.count(condition, new Object[]{barcode});
			if(total>0){
				return true;
			}
			return false;
		} catch (Exception e) {
			throw new DaoException("【DAO-error】新增产品时条形码的唯一性，出现异常", e);
		}
	}
	
	/**
	 * 根据barcode、品牌名称、企业id查找品牌id
	 * @param barcode 
	 * @param brandName 
	 * @param bussunitId 
	 * @return Product
	 * @throws DaoException 
	 * @throws ServiceException 
	 */
	@Override
	public Long findByBarcodeAndBrandNameAndBusunitId(String barcode,
			String brandName, Long bussunitId) throws DaoException {
		try {
			String sql = "SELECT business_brand_id FROM product WHERE barcode = ?1" +
						 " AND producer_id = ?2 AND business_brand_id in" +
						 " (SELECT id FROM business_brand WHERE `name` = ?3)";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, barcode);
			query.setParameter(2, bussunitId);
			query.setParameter(3, brandName);
			Object result = query.getSingleResult();
			if(result != null){
				return Long.parseLong(result.toString());
			}
			return null;
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.findByBarcodeAndBrandNameAndBusunitId() 根据barcode、品牌名称、企业id查找品牌id, 出现异常！", e);
		}
	}

	/**
	 * 查询所有条形码
	 * @return List<String>
	 * @throws DaoException
	 * @author LongXianZhen
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAllBarcode() throws DaoException {
		try {
			String sql = "select barcode from product where barcode is not null and barcode <>''";
			List<String>  barcodes = entityManager.createNativeQuery(sql).getResultList();
			return barcodes;
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.getAllBarcode()查询所有条形码, 出现异常！", e);
		}
	}
	/**
	 * 根据名字查找产品
	 * @param sampleName 名字
	 * @author ZhaWanNeng
	 * 更新时间2015/3/17
	 */
	@SuppressWarnings("unchecked")
	public Product findByName(String sampleName) {
		List<Product> result = entityManager.createNativeQuery("select * from product where name=?1",Product.class).setParameter(1,sampleName).getResultList();
		if(result != null && result.size() > 0){
			return result.get(0);
		}
		return null;
	}
	
	/**
	 * 根据组织机构ID查询一个企业下的所有产品数量
	 * @param organization 企业组织机构ID
	 * @return Long
	 * @author LongXianZhen
	 */
	@Override
	public Long getAllProCountByOrganization(Long organization) throws DaoException {
		try {
			String condition = " where e.organization = ?1 ";
			Long total =  this.count(condition, new Object[]{organization});
			return total;
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.getCountByOrganization() 根据组织机构ID查询一个企业下的所有产品", e);
		}
	}
	/**
	 * 产品总数
	 * @return Long
	 * @author ZhaWanNeng<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/4/10
	 */
	@SuppressWarnings("unchecked")
	public Long productCount() throws DaoException {
		try {
			    String sql = "SELECT count(p.id) FROM product p ;";
	            Query query = entityManager.createNativeQuery(sql);
	            List<Object> list = query.getResultList();
			    return Long.valueOf(list.get(0).toString());
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.productCount() ", e);
		}
	}
	/**
	 * 产品的营养指数排行
	 * @return Long
	 * @author ZhaWanNeng<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/4/10
	 */
	@SuppressWarnings("unchecked")
	public List<ProductListVo> getProductList(Long type,int pageSize,int page) throws DaoException {
		try {
			String sql="";
			if(type==3||type==5){
				String nrv = "0.3";
				if(type==3){
					nrv = "0.1";
				}
				sql = " SELECT t.id,t.pname,t.pimg,t.nname,t.nvalue,t.nunit,t.nnrv,t.nper from (" +
				 	  " SELECT DISTINCT pro.id as id,pro.name as pname,pro.imgUrl as pimg,nr.`name` as nname,nr.value as nvalue,nr.unit as nunit,nr.nrv as nnrv,nr.per as nper " +
					  " FROM product pro LEFT JOIN nutri_rpt nr ON nr.product_id = pro.id " +
					  " WHERE nr.nutri_id =?1 and nr.per='每100克' and nr.nrv!='' and nr.nrv!='0' and nr.nrv!='0%' and nr.nrv > "+nrv+" ORDER BY cast(nr.nrv as decimal(10,2)) ASC ) t  ";
			}
			else{
				sql = " SELECT t.id,t.pname,t.pimg,t.nname,t.nvalue,t.nunit,t.nnrv,t.nper from (" +
				 		" SELECT DISTINCT pro.id as id,pro.name as pname,pro.imgUrl as pimg,nr.`name` as nname,nr.value as nvalue,nr.unit as nunit,nr.nrv as nnrv,nr.per as nper " +
						" FROM product pro LEFT JOIN nutri_rpt nr ON nr.product_id = pro.id " +
					    " WHERE nr.nutri_id =?1 and nr.per='每100克' and nr.nrv!='' ORDER BY cast(nr.nrv as decimal(10,2)) DESC ) t  ";
			}
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, type);
			query.setFirstResult((page - 1)*pageSize);
			query.setMaxResults(pageSize);
			
			List<Object[]> result = query.getResultList();
			List<ProductListVo> productListVo = new ArrayList<ProductListVo>();
			if(result.size() <= 0){
				return null;
			}
			for (Object[] obj : result) {
				    ProductNutritionVO vutrition = new ProductNutritionVO();
					Long productid = Long.valueOf(obj[0].toString());
					String productName = obj[1] !=null ? obj[1].toString() : "";
					String imgUrl = obj[2] !=null ? obj[2].toString() : "";
					String name = obj[3] !=null ? obj[3].toString() : "";
					String value = obj[4] !=null ? obj[4].toString() : "";
					String unit = obj[5] !=null ? obj[5].toString() : "";
					String nrv = obj[6] !=null ? obj[6].toString() : "";
					String per = obj[7] !=null ? obj[7].toString() : "";
					vutrition.setName(name) ;
					vutrition.setNrv(nrv);
					vutrition.setPer(per);
					vutrition.setUnit(unit);
					vutrition.setValue(value);
					ProductListVo listVo = new ProductListVo(productid, productName, imgUrl, vutrition);
					productListVo.add(listVo);
			}
			return productListVo;
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.productCount() ", e);
		}
	}
	/**
	 * 产品风险排行
	 * @return Long
	 * @author ZhaWanNeng<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/4/10
	 */
	@SuppressWarnings("unchecked")
	public List<ProductRiskVo> riskBillboard(String type,int pageSize,int page) throws DaoException {
		try {
			String sql = "";
			if("14".equals(type) || "10".equals(type) || "01".equals(type)){
			 sql = " SELECT DISTINCT pro.id,pro.riskIndex,pro.name,pro.imgUrl,pro.test_property_name,pro.risk_succeed FROM product pro " +
				   " WHERE pro.category LIKE '"+type+"_%' and pro.risk_succeed=1 ORDER BY pro.riskIndex ASC ";
			}else if("00".equals(type)){
			 sql = " SELECT DISTINCT pro.id,pro.riskIndex,pro.name,pro.imgUrl,pro.test_property_name,pro.risk_succeed FROM product pro " +
				   " WHERE pro.category not LIKE '14_%' and pro.category not LIKE '10_%' and  pro.category not LIKE '01_%' and pro.risk_succeed=1 ORDER BY pro.riskIndex ASC ";
			}else {
				return null;
			}
			Query query = entityManager.createNativeQuery(sql);
//			query.setParameter(1, type);
			query.setFirstResult((page - 1)*pageSize);
			query.setMaxResults(pageSize);
			List<Object[]> result = query.getResultList();
			List<ProductRiskVo> productList = new ArrayList<ProductRiskVo>();
			if(result.size() <= 0){
				return null;
			}
			for (Object[] obj : result) {
				Long id = Long.valueOf(obj[0].toString());
				String riskIndex = obj[1] !=null ? obj[1].toString() : "";
				String name = obj[2] !=null ? obj[2].toString() : "";
				String imgUrl = obj[3] !=null ? obj[3].toString() : "";
				String testPropertyName = obj[4] !=null ? obj[4].toString() : "";
				String tes = obj[5] !=null ?  obj[5].toString(): "";
				Boolean riskSucceed = false;
				if(tes .equals("true")){
					 riskSucceed = true;
				}
				ProductRiskVo productRisk = new ProductRiskVo(id, name, imgUrl, riskIndex, testPropertyName, riskSucceed);
				productList.add(productRisk);
			}
			return productList;
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.riskBillboard() ", e);
		}
	}
	/**
	 * 获取产品的的一级分类的code
	 * @param name 一级分类的名称
	 * @author ZhaWanNeng<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/4/10
	 */
	@SuppressWarnings("unchecked")
	public String productCode(String name) throws DaoException {
		try {
			String sql = "SELECT  pc.code FROM product_category pc WHERE pc.name = "+name+" ";
			Query query = entityManager.createNativeQuery(sql);
			List<Object> result = query.getResultList();
			if(result.size() <= 0){
				return null;
			}
			return result.get(0).toString();
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.riskBillboard()-->" + e.getMessage(), e);
		}
	}
	/**
	 * 获取产品的风险排行的数量
	 * @param code 一级分类的code
	 * @author ZhaWanNeng<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/4/10
	 */
	@SuppressWarnings("unchecked")
	public int countriskBill(String code) throws DaoException {
		try {
			String sql = "SELECT DISTINCT COUNT(pro.id)  FROM product pro " +
				         " WHERE pro.category LIKE '%"+code+"_%' and pro.risk_succeed=1 ORDER BY pro.riskIndex DESC ";
			Query query = entityManager.createNativeQuery(sql);
			List<Object> result = query.getResultList();
			if(result.size() <= 0){
				return 0;
			}
			return Integer.valueOf(result.get(0).toString());
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.riskBillboard() ", e);
		}
	}

	/**
	 * 根据barcode获取产品id
	 * @author ZhangHui 2015/4/10
	 */
	@Override
	public Long getIdByBarcode(String barcode) throws DaoException {
		
		try{
			String sql= "SELECT id FROM product WHERE barcode = ?1";
			/*String sql= "SELECT DISTINCT p.id,ttb.from_bus_id,ttb.to_bus_id  FROM product p LEFT JOIN t_meta_from_to_business ttb ON p.id=ttb.pro_id"; 
			sql+=" WHERE p.barcode=:code";
			if(organizationID!=null&&!"null".equals(organizationID)&&!"".equals(organizationID)){
				sql+="  AND ttb.from_bus_id = (SELECT MAX(id) FROM business_unit WHERE organization=:org) ";
			}
			*/
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, barcode);
			Object obj = query.getSingleResult();
			return obj!=null ? Long.parseLong(obj.toString()) : null;
			/*List<Object[]> objs = query.getResultList();
			Long[] id = null;
			if(objs!=null&&objs.size()>0){
				Object[] obj = objs.get(0);
				id = new Long[3];
				id[0] = obj[0]!=null?Long.parseLong(obj[0].toString()):0;
				id[1] = obj[1]!=null?Long.parseLong(obj[1].toString()):0;
				id[2] = obj[2]!=null?Long.parseLong(obj[2].toString()):0;
			}*/
//			return obj!=null ? Long.parseLong(obj.toString()) : null;
//			return id;
		}catch(Exception e){
			throw new DaoException("ProductDAOImpl.getIdByBarcode()",e);
		}
	}

	/**
	 * 获取轻量级产品信息
	 * @author ZhangHui 2015/4/11
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProductManageViewVO> getLightProductVOsByPage(int page, int pageSize, String condition, 
			String condition_barnd, Long organization, Long fromBusId, boolean isDel) throws DaoException {
		try {
			if(organization == null){
				return null;
			}
			
			int begin = (page-1)*pageSize;
			//如果等于configure = 0的情况下，是不需要查询条件的，因此在如下判断中，给你了一个1=1的条件来满足查询sql的常规书写
			boolean cofFlag = true;
			if(condition == null || "".equals(condition)){
				condition += " WHERE organization = :org  ORDER BY last_modify_time DESC ";
			}else{
				condition += " AND organization = :org  ORDER BY last_modify_time DESC";
			}
			/* 使用纯sql查询,营养指数对应的字段为  nutri_status */
			condition = condition.replace("nutriStatus", "nutri_status");
			String sql = "SELECT pro.id,pro.proname,businessBrand.name,pro.format,pro.barcode,pro.cstm,pro.ingredient,pro.package_flag," +
								"(SELECT GROUP_CONCAT(NAME) FROM business_unit " +
								"WHERE id IN(SELECT to_bus_id FROM t_meta_from_to_business WHERE from_bus_id = :fromBusId AND del = :del AND pro_id = pro.id )" +
								")AS customernames,IF(ISNULL(pro.nutri_status)||LENGTH(TRIM(pro.nutri_status))<1,0,pro.nutri_status) as nutri_status   " +
						 "FROM " +
								"(SELECT id,NAME AS proname,FORMAT,barcode,cstm,ingredient,business_brand_id,package_flag," +
								"IF(ISNULL(nutri_status)||LENGTH(TRIM(nutri_status))<1,0,nutri_status) as nutri_status  " +
								"FROM product " + condition + " LIMIT " + begin + "," + pageSize +
								")AS pro " +
						 "LEFT JOIN business_brand businessBrand " +
						 		"ON pro.business_brand_id = businessBrand.id " +
						 condition_barnd;
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter("org", organization);
			query.setParameter("fromBusId", fromBusId);
			query.setParameter("del", isDel);
			/* 数据封装 */
			List<Object[]> result = query.getResultList();
			List<ProductManageViewVO> vos = new ArrayList<ProductManageViewVO>();
			if(result != null){
				for(Object[] obj : result){
					ProductManageViewVO vo = new ProductManageViewVO(((BigInteger)obj[0]).longValue(), 
							obj[1]==null?"":obj[1].toString(),obj[2]==null?"":obj[2].toString(), 
							obj[3]==null?"":obj[3].toString(), obj[4]==null?"":obj[4].toString(), 
							obj[5]==null?"":obj[5].toString(),obj[6]==null?"":obj[6].toString(),
							obj[7]==null?"":obj[7].toString(),obj[8]==null?"":obj[8].toString(),
							obj[9]==null||"".equals(obj[9].toString())?'0':obj[9].toString().charAt(0));
					vos.add(vo);
				}
			}
			return vos;
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.getLightProductVOsByPage() 出现异常！", e);
		}
	}
	
	
	/**
	 * 获取轻量级产品信息
	 * @author ZhangHui 2015/4/11
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProductManageViewVO> getLightProductVOsByPage(int page, int pageSize, String condition, 
			String condition_barnd) throws DaoException {
		try {
			int begin = (page-1)*pageSize;
			//如果等于configure = 0的情况下，是不需要查询条件的，因此在如下判断中，给你了一个1=1的条件来满足查询sql的常规书写
				condition += " ORDER BY last_modify_time DESC ";
			/* 使用纯sql查询,营养指数对应的字段为  nutri_status */
			condition = condition.replace("nutriStatus", "nutri_status");
			String sql = "SELECT pro.id,pro.proname,businessBrand.name,pro.format,pro.barcode,pro.cstm,pro.ingredient,pro.package_flag," +
								"IF(ISNULL(pro.nutri_status)||LENGTH(TRIM(pro.nutri_status))<1,0,pro.nutri_status) as nutri_status " +
						 "FROM " +
								"(SELECT id,NAME AS proname,FORMAT,barcode,cstm,ingredient,business_brand_id,package_flag,IF(ISNULL(nutri_status)||LENGTH(TRIM(nutri_status))<1,0,nutri_status) as nutri_status " +
								"FROM product " + condition + " LIMIT " + begin + "," + pageSize +
								")AS pro " +
						 "LEFT JOIN business_brand businessBrand " +
						 		"ON pro.business_brand_id = businessBrand.id " +
						 condition_barnd;
			
			Query query = entityManager.createNativeQuery(sql);
			/* 数据封装 */
			List<Object[]> result = query.getResultList();
			List<ProductManageViewVO> vos = new ArrayList<ProductManageViewVO>();
			if(result != null){
				for(Object[] obj : result){
					ProductManageViewVO vo = new ProductManageViewVO(((BigInteger)obj[0]).longValue(), 
							obj[1]==null?"":obj[1].toString(),obj[2]==null?"":obj[2].toString(), 
							obj[3]==null?"":obj[3].toString(), obj[4]==null?"":obj[4].toString(), 
							obj[5]==null?"":obj[5].toString(),obj[6]==null?"":obj[6].toString(),
							obj[7]==null?"":obj[7].toString(),null,
							obj[8]==null||"".equals(obj[8].toString())?'0':obj[8].toString().charAt(0));
					vos.add(vo);
				}
			}
			return vos;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("ProductDAOImpl.getLightProductVOsByPage() 出现异常！", e);
		}
	}
	
	/**
	 * 获取轻量级产品信息(包括引进产品)
	 * @author ZhangHui 2015/4/14
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProductManageViewVO> getAllLightProductVOsByPage(int page, int pageSize, String condition, 
			String condition_barnd, Long organization, Long fromBusId, boolean isDel) throws DaoException {
		try {
			if(organization == null){
				return null;
			}
			
			int begin = (page-1)*pageSize;
			
			if(condition == null || "".equals(condition)){
				condition += " WHERE ";
			}else{
				condition += " AND ";
			}
			
			String sql = "SELECT pro.id,pro.proname,businessBrand.name,pro.format,pro.barcode,pro.cstm,pro.ingredient,pro.package_flag," +
							"(SELECT GROUP_CONCAT(NAME) " +
							"FROM business_unit " +
							"WHERE id IN" +
						 		"(SELECT ftb.to_bus_id FROM t_meta_from_to_business ftb " +
						 		 "WHERE ftb.from_bus_id = ?2 AND ftb.del = 0 AND ftb.pro_id = pro.id)" +
						 	")AS customernames, " +
							"pro.local,pro.organization,pro.is_special_product " +
						 "FROM " +
								"(SELECT DISTINCT product.id,product.NAME AS proname,product.FORMAT,product.barcode,product.cstm,product.ingredient,product.organization,product.business_brand_id,product.package_flag,ip.local,is_special_product " +
								 "FROM product " +
								 "LEFT JOIN t_meta_initialize_product ip " +
								 		"ON ip.product_id = product.id AND ip.organization = ?1 AND ip.del = 0 " +
								 condition + " ip.local IS NOT NULL ORDER BY last_modify_time DESC LIMIT " + begin + "," + pageSize +
								")AS pro " +
						 "LEFT JOIN business_brand businessBrand " +
						 		"ON pro.business_brand_id = businessBrand.id " +
						 condition_barnd;
			sql=sql+" ORDER BY pro.id desc ";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, organization);
			query.setParameter(2, fromBusId);
//			query.setParameter(3, fromBusId);
			/* 数据封装 */
			List<Object[]> result = query.getResultList();
			List<ProductManageViewVO> vos = new ArrayList<ProductManageViewVO>();
			if(result != null){
				for(Object[] obj : result){
					ProductManageViewVO vo = new ProductManageViewVO(((BigInteger)obj[0]).longValue(), 
							obj[1]==null?"":obj[1].toString(),obj[2]==null?"":obj[2].toString(), 
							obj[3]==null?"":obj[3].toString(), obj[4]==null?"":obj[4].toString(), 
							obj[5]==null?"":obj[5].toString(),obj[6]==null?"":obj[6].toString(),
							obj[7]==null?"":obj[7].toString(),obj[8]==null?"":obj[8].toString(),
							obj[9]==null?"":obj[9].toString(),obj[11].toString().equals("true")?true:false);
					Long org = obj[10]==null?null:((BigInteger)obj[10]).longValue();
					if("false".equals(vo.getLocal()) && org!=null && org.equals(organization)){
						vo.setLocal("true");
					}
					vo.setNutriStatus('0');
					vos.add(vo);
				}
			}
			return vos;
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.getAllLightProductVOsByPage() 出现异常！", e);
		}
	}
	
	/**
	 * 获取产品数量(包括引进产品)
	 * @author ZhangHui 2015/4/14
	 */
	@Override
	public long countAllProduct(String condition, String condition_barnd, Long organization) throws DaoException {
		try {
			if(condition == null || "".equals(condition)){
				condition += " WHERE ";
			}else{
				condition += " AND ";
			}
			
			String sql = "SELECT COUNT(*) " +
						 "FROM " +
								"(SELECT id,NAME AS proname,FORMAT,barcode,cstm,ingredient,business_brand_id,package_flag " +
								 "FROM product " +
								 condition +
								 "id IN " +
								 		"(SELECT product_id FROM t_meta_initialize_product WHERE organization = ?1 AND del = '0' ) " +
								")AS pro " +
						 "LEFT JOIN business_brand businessBrand " +
						 		"ON pro.business_brand_id = businessBrand.id " +
						 condition_barnd;
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, organization);
			/* 数据封装 */
			Object rtn = query.getSingleResult();
			return rtn == null ? 0 : Long.parseLong(rtn.toString());
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.countAllProduct() 出现异常！", e);
		}
	}

	/**
	 * 经销商只能加载出自己的产品条形码
	 * @author HuangYog
	 * Create date 2015/04/13
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAllBarcode(Long myOrg) throws DaoException {
		try {
			String sql = "SELECT DISTINCT barcode FROM product p RIGHT JOIN t_meta_initialize_product initp ON p.id = initp.product_id "+
							"WHERE initp.organization = ?1 ";
			List<String>  barcodes = entityManager.createNativeQuery(sql).setParameter(1, myOrg).getResultList();
			return barcodes;
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.getAllBarcode()查询所有条形码, 出现异常！", e);
		}
	}
	
	/**
	 * 获取没计算风险指数所有的产品
	 * @return
	 * @author ZhaWanNeng	2015/04/17
	 */
	@SuppressWarnings("unchecked")
	public List<Product> getproductList(int pageSize,int page) throws DaoException {
		try {
			String sql = " SELECT  e FROM  " +entityClass.getName()+" e ";
			Query query = entityManager.createQuery(sql);
			query.setFirstResult((page - 1)*pageSize);
			query.setMaxResults(pageSize);
			List<Product> result = query.getResultList();
			return result;
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.getproductList()-->> 出现异常！", e);
		}
	}
	
	/**
	 * 创建 产品相册 List<DetailAlbumVO>
	 * @author tangxin 2015-05-05
	 */
	private List<DetailAlbumVO> createDetailAlbumVO(List<Object[]> listObjs, String cut) {
		List<DetailAlbumVO> listVO = new ArrayList<DetailAlbumVO>();
		if(listObjs == null || listObjs.size() < 1) {
			return listVO;
		}
		Map<String,Integer> cutMap = SalesUtil.getCutWidthAndHeight(cut);
		int fieldType = SalesUtil.PHOTO_FIELD_TYPE_PRODUCT;
		for(Object[] objs : listObjs){
			DetailAlbumVO albumVO = new DetailAlbumVO();
			long proId = (objs[0] != null ? Long.parseLong(objs[0].toString()) : 0L);
			albumVO.setId(proId);
			albumVO.setProductId(proId);
			String name = (objs[1] != null ? objs[1].toString() : null);
			albumVO.setImgName(name);
			String imgurl = (objs[2] != null ? objs[2].toString() : "");
			imgurl = ("".equals(imgurl) ? "http://qa.fsnrec.com/portal/img/product/temp/temp.jpg" : imgurl);
			albumVO.setImgUrl(imgurl);
			if(cutMap != null) {
				albumVO.setThumbnailUrl(ImgUtils.getImgPath(imgurl, cutMap.get("width"), cutMap.get("height")));
			}else{
				albumVO.setThumbnailUrl(imgurl);
			}
			String format = (objs[3] != null ? objs[3].toString() : null);
			String desc = (objs[4] != null ? objs[4].toString() : null);
			long sjCont = (objs[5] != null ? Long.parseLong(objs[5].toString()) : 0l);
			long cjCont = (objs[6] != null ? Long.parseLong(objs[6].toString()) : 0l);
			long zjcount = (objs[7] != null ? Long.parseLong(objs[7].toString()) : 0l);
			double riskIndex = (objs[8] != null ? Double.parseDouble(objs[8].toString()) : 0f);
			long allCont = sjCont+cjCont+zjcount;
			PhotoFieldVO fieldVO = new PhotoFieldVO(proId,null,name,null,null,null,null,null,format,desc,sjCont,allCont,fieldType,null);
			
			fieldVO.setCjReportNumber(cjCont);
			fieldVO.setZjReportNumber(zjcount);
			fieldVO.setRiskIndex(riskIndex);
			
			albumVO.setField(fieldVO);
			listVO.add(albumVO);
		}
		return listVO;
	}
	
	/**
	 * 根据企业组织机构分页查询产品相册
	 * @author tangxin 2015-05-05
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DetailAlbumVO> getProductAlbums(Long organization, int page, int pageSize, String cut) throws DaoException{
		try{
			String sql = "SELECT pd.id,pd.`name`,tm0.URL,pd.format,pd.des,tm1.censcon1,tm2.censcon2,tm3.censcon3,pd.riskIndex FROM product pd " +
					"LEFT JOIN (SELECT tt.id,tt.URL,tt.UPLOAD_DATE FROM (SELECT pr.id,res.URL,res.UPLOAD_DATE FROM product pr " +
					"LEFT JOIN t_test_product_to_resource t2p ON pr.id = t2p.PRODUCT_ID " +
					"LEFT JOIN t_test_resource res ON t2p.RESOURCE_ID = res.RESOURCE_ID " +
					"WHERE pr.organization = :organization ORDER BY res.UPLOAD_DATE DESC) tt GROUP BY tt.id ORDER BY tt.id DESC) tm0 ON pd.id = tm0.id " +
				
					"LEFT JOIN (SELECT pro.id,count(tr.id) 'censcon1' FROM product pro " +
					"LEFT JOIN product_instance pi ON pro.id = pi.product_id " +
					"LEFT JOIN test_result tr ON pi.id = tr.sample_id " +
					"where pro.organization = :organization AND tr.publish_flag = 1 and tr.test_type = '企业送检' GROUP BY pro.id) tm1 ON pd.id = tm1.id " +
					
					"LEFT JOIN (SELECT pro.id,count(tr.id) 'censcon2' FROM product pro " +
					"LEFT JOIN product_instance pi ON pro.id = pi.product_id " +
					"LEFT JOIN test_result tr ON pi.id = tr.sample_id " +
					"where pro.organization = :organization AND tr.publish_flag = 1 and tr.test_type = '政府抽检' GROUP BY pro.id) tm2 ON pd.id = tm2.id " +
					
					"LEFT JOIN (SELECT pro.id,count(tr.id) 'censcon3' FROM product pro " +
					"LEFT JOIN product_instance pi ON pro.id = pi.product_id " +
					"LEFT JOIN test_result tr ON pi.id = tr.sample_id " +
					"where pro.organization = :organization AND tr.publish_flag = 1 and tr.test_type = '企业自检' GROUP BY pro.id) tm3 ON pd.id = tm3.id " +
					
				    " where pd.organization = :organization ORDER BY pd.id DESC";
			Query query = entityManager.createNativeQuery(sql);
			if(page > 0 && pageSize > 0) {
				page = (page - 1) * pageSize;
				query.setFirstResult(page);
				query.setMaxResults(pageSize);
			}
			query.setParameter("organization", organization);
			List<Object[]> listProduct = query.getResultList();
			return createDetailAlbumVO(listProduct,cut);
		}catch(Exception e){
			throw new DaoException(e.getMessage(), e);
		}
	}

	/**
	 * 判断产品组织机构是否能修改
     * @author LongXianZhen	2015/05/06
	 */
	@Override
	public boolean judgeProductOrgModify(Long organization) throws DaoException {
		try {
			String sql = " SELECT COUNT(*) FROM lead_product_org WHERE organization=?1 ";
			Query query = entityManager.createNativeQuery(sql).setParameter(1, organization);
			Number result=(Number)query.getSingleResult();
			int count=result.intValue();
			return count>0?true:false;
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.judgeProductOrgModify()-->> 出现异常！", e);
		}
	}

	/**
	 * 功能描述：根据产品条形码查找一条已经被生产企业认领的产品信息（如果没有被认领，则返回null）
     * @author ZhangHui 2015/06/04
	 * @throws DaoException 
	 */
	@Override
	public Product findByBarcodeOfHasClaim(String barcode) throws DaoException {
		try {
			if(barcode==null || "".equals(barcode)){
				return null;
			}
			
			String condition = " WHERE e.barcode = ?1 AND e.producer.organization = e.organization";
			List<Product> result = this.getListByCondition(condition, new Object[]{barcode});
			if(result.size()>0){
				return result.get(0);
			}
			
			return null;
		} catch (Exception e) {
			throw new DaoException("BusinessUnitDAOImpl.findByBarcodeOfHasClaim()-->" + e.getMessage(), e);
		}
	}

	/**
	 * 功能描述：检查产品有无被生产企业认领
	 * @return  true  代表已经被生产企业认领
	 * 			false 代表没有没生产企业认领
	 * @throws DaoException 
     * @author ZhangHui 2015/06/05
	 */
	@Override
	public boolean checkClaimOfProduct(Long id) throws DaoException {
		try {
			String condition = " WHERE e.id = ?1 AND e.producer.organization = e.organization";
			long count = this.count(condition, new Object[]{id});
			if(count > 0){
				return true;
			}
			return false;
		} catch (JPAException e) {
			throw new DaoException("BusinessUnitDAOImpl.checkClaimOfProduct()-->" + e.getMessage(), e.getException());
		}
	}

	/**
	 * 根据产品id查询产品轻量级分装
	 * @param productId
	 * @author longxianzhen 2015/08/03
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ProductManageViewVO findByProductManageViewVOByProId(Long productId)
			throws DaoException {
		try {
			if(productId==null){
				return null;
			}
			String sql ="SELECT pro.id,pro.pName,pro.format,pro.barcode,pro.bName,pro.`status`,pro.expiration,pro.imgurl FROM (" +
							"SELECT p.id,p.name AS pName,p.`status`,p.format," +
							"p.barcode,p.expiration,bu.`name` AS bName ,p.imgurl FROM product p " +
							"LEFT JOIN business_unit bu ON p.producer_id=bu.id WHERE p.id=?1) AS pro";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, productId);
			/* 数据封装 */
			List<Object[]> result = query.getResultList();
			ProductManageViewVO vo = null;
			if(result != null){
				Object[] obj=result.get(0);
				vo = new ProductManageViewVO(((BigInteger)obj[0]).longValue(),
						obj[1]==null?"":obj[1].toString(),obj[2]==null?"":obj[2].toString(),
						obj[3]==null?"":obj[3].toString(),obj[4]==null?"":obj[4].toString(),
						obj[5]==null?"":obj[5].toString(),obj[6]==null?"":obj[6].toString(),
						obj[7]==null?"":obj[7].toString());
			}
			return vo;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("ProductDAOImpl.findByProductManageViewVOByProId() 出现异常！", e);
		}
	}

	/**
	 * 根据产品id集合查找产品集合 
	 * @author longxianzhen 2015/08/07
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getProListByCondition(String proIdStrs)
			throws DaoException {
		try {
//
//			String sql ="SELECT pro.id,pro.pName,pro.barcode,pro.format,pro.cstm,pro.bName FROM ( "+
//						"SELECT p.id,p.`name` AS pName,p.barcode,p.format,p.cstm,bb.`name` AS bName " +
//						"FROM product p LEFT JOIN business_brand bb ON bb.id=p.business_brand_id " +
//						"WHERE p.id IN("+proIdStrs+") ) AS pro" ;
//			Query query = entityManager.createNativeQuery(sql);
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT pro.id,pro.pName,pro.barcode,pro.format,pro.cstm,pro.bName,trc.last_time,pro.package_flag FROM ");
			sb.append(" (SELECT p.id,p.`name`     AS pName, p.barcode,p.format,p.cstm, bb.`name`    AS bName ,p.package_flag");
			sb.append(" FROM product p LEFT JOIN business_brand bb  ON bb.id = p.business_brand_id WHERE p.id IN("); 
			sb.append(proIdStrs);
			sb.append(")) pro ").append(" LEFT JOIN  ");      
			sb.append(" (SELECT a.product_id,MAX(a.last_modify_time) last_time FROM ");
			sb.append(" (SELECT  pin.product_id,tr.last_modify_time  FROM  product_instance pin,test_result tr "); 
			sb.append(" WHERE DATEDIFF(pin.expiration_date , SYSDATE()) > 0 and tr.sample_id=pin.id AND pin.product_id IN(");
			sb.append(proIdStrs);
		    sb.append(")) a GROUP BY a.product_id ) trc ON trc.product_id = pro.id order by trc.last_time");
			Query query = entityManager.createNativeQuery(sb.toString());
			
			
			/* 数据封装 */
			List<Object[]> result = query.getResultList();
			List<Product> pros=new ArrayList<Product>();
			if(result != null&&result.size()>0){
				for(Object[] obj:result){
					Product p=new Product();
					p.setId(((BigInteger)obj[0]).longValue());
					p.setName(obj[1]==null?"":obj[1].toString());
					p.setBarcode(obj[2]==null?"":obj[2].toString());
					p.setFormat(obj[3]==null?"":obj[3].toString());
					p.setCstm(obj[4]==null?"":obj[4].toString());
					p.setBusinessBrand(new BusinessBrand(obj[5]==null?"":obj[5].toString()));
					p.setNutriStatus('0');
					p.setLastModifyTime(obj[6]==null?null:formatter.parse(obj[6].toString()));
					p.setPackageFlag(obj[7]==null||"".equals(obj[7].toString())?null:obj[7].toString().charAt(0));
					pros.add(p);
				}
			}
			return pros;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("ProductDAOImpl.findByProductManageViewVOByProId() 出现异常！", e);
		}
	}

	@Override
	public Product checkProduct(Product product) throws ServiceException {
		Criteria criteria=getSession().createCriteria(Product.class);
		BusinessUnit businessUnit =product.getProducer();
		if(null!=businessUnit&&!StringUtil.isBlank(businessUnit.getName())){
			criteria.createAlias("producer", "producer");
			criteria.add(Restrictions.eq("producer.name",businessUnit.getName()));
		}
		if(!StringUtil.isBlank(product.getName())){
			criteria.add(Restrictions.eq("name",product.getName()));
		}
		if(!StringUtil.isBlank(product.getFormat())){
			criteria.add(Restrictions.eq("format",product.getFormat()));
		}
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);// ROOT_ENTITY
		
		if(criteria.list().size()>0){
			return (Product) criteria.list().get(0);
		}
		return null;
	}

	@Override
	public List<ProductStaVO> getProductStaListByConfigureData(Long businessId,
			String productName, String barcode, String startDate,
			String endDate, int page, int pageSize) {
			String sqlString = "";
			if(startDate!=null&&!"".equals(startDate)){
				sqlString+=" AND tr.publishDate >= '"+startDate+"' ";
			}
			if(endDate!=null&&!"".equals(endDate)){
				sqlString+=" AND tr.publishDate < DATE_ADD('"+endDate+"', INTERVAL 1 DAY) ";
			}
		    String sql=" SELECT  ";
		        //查询该产品已发布报告数量
		        sql+=" (SELECT COUNT(*) FROM product_instance pri,test_result tr WHERE p.id=pri.product_id AND  tr.sample_id=pri.id AND tr.publish_flag=1 ";
				sql+=" AND tr.organization = bu.organization ";
				sql+= sqlString;
				sql+=" ) publishReportQuantity ,";
				//查询该产品未发布报告数量
				sql+="(SELECT COUNT(*) FROM product_instance pri,test_result tr WHERE p.id=pri.product_id AND  tr.sample_id=pri.id AND tr.publish_flag=0 ";
				sql+=" AND tr.organization = bu.organization) notPublishReportQuantity ,";
				//查询该产品最后发布报告时间
				sql+=" (SELECT MAX(tr.publishDate) FROM product_instance pri,test_result tr WHERE p.id=pri.product_id AND  tr.sample_id=pri.id AND tr.publish_flag=1";
				sql+=" AND tr.organization = bu.organization AND tr.publishDate is not null ";
				sql+= sqlString;
				sql+=" ) publishDate,";
				sql+="bu.name buName,p.name productName,p.barcode,bu.organization ";
				sql+=" FROM  product p " ;
				sql+=" LEFT JOIN business_unit bu ON  p.organization = bu.organization ";
				sql+=" WHERE bu.id=:buId ";
				if(productName!=null&&!"".equals(productName)){
				sql+=" AND p.name LIKE '%"+productName+"%' ";
				}
				if(barcode!=null&&!"".equals(barcode)){
				sql+=" AND p.barcode LIKE '%"+barcode+"%'";
				}
				Query query = entityManager.createNativeQuery(sql);
				query.setParameter("buId", businessId);
				if(page > 0 && pageSize > 0) {
					query.setFirstResult((page - 1) * pageSize);
					query.setMaxResults(pageSize);
				}
				@SuppressWarnings("unchecked")
				List<Object[]> objs = query.getResultList();
				List<ProductStaVO> proList = new  ArrayList<ProductStaVO>();
				try {
					for (Object[] obj:objs) {
						ProductStaVO proSta= new ProductStaVO();
						//查询该产品已发布报告数量
						proSta.setReportQuantity(obj[0]==null?null:Long.parseLong(obj[0].toString()));
						//查询该产品未发布报告数量
						proSta.setNotPublishReportQuantity(obj[1]==null?null:Long.parseLong(obj[1].toString()));
						//查询该产品最后发布报告时间
						proSta.setLastPubDate(obj[2]==null?null:formatter.parse(obj[2].toString()));
						proSta.setBusinessName(obj[3]==null?null:obj[3].toString());
						proSta.setProductName(obj[4]==null?null:obj[4].toString());
						proSta.setBarcode(obj[5]==null?null:obj[5].toString());
						proList.add(proSta);
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
		return proList;
	}

	@Override
	public Long getProductStaCountByConfigureData(Long businessId,
			String productName, String barcode) {
		
		String sql=" SELECT  count(*) ";
		sql+=" FROM  product p " ;
		sql+=" LEFT JOIN business_unit bu ON  p.organization = bu.organization ";
		sql+=" WHERE bu.id=:buId ";
		if(productName!=null&&!"".equals(productName)){
		sql+=" AND p.name LIKE '%"+productName+"%' ";
		}
		if(barcode!=null&&!"".equals(barcode)){
		sql+=" AND p.barcode LIKE '%"+barcode+"%'";
		}
		Long counts = 0l;
		try {
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter("buId", businessId);
			Object rtn = query.getSingleResult();
			counts = rtn == null ? 0 : Long.parseLong(rtn.toString());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return counts;
	}
	
	@Override
	public boolean setBarcodeToQRcode(String barcode,Long productID, String QRStart,String QREnd) {
		//判断输入范围是否可插入，如果返回结果集大于0，则不
		String isExist="SELECT pb.start_num,pb.end_num  FROM product_barcode_to_qrcode pb WHERE ?1  BETWEEN start_num and end_num OR ?2 BETWEEN start_num AND end_num or ?1<=start_num AND ?2>=end_num";
		Query isExistQuery= entityManager.createNativeQuery(isExist);
		isExistQuery.setParameter(1, QRStart);
		isExistQuery.setParameter(2, QREnd);
		int existSize=isExistQuery.getResultList().size();
		//长度为0可插入输入范围
		if(existSize==0){
			String insertSql = "INSERT INTO product_barcode_to_qrcode (product_barcode,product_id,start_num,end_num  )VALUES (?1,?2,?3,?4) ";
			Query insertQuery= entityManager.createNativeQuery(insertSql);
			insertQuery.setParameter(1, barcode);
			insertQuery.setParameter(2, productID);
			insertQuery.setParameter(3, QRStart);
			insertQuery.setParameter(4, QREnd);
			int success=insertQuery.executeUpdate();
			System.out.print("----sql----"+success);
			if(success==1){
				return true;
			}
			else{
				return false;
			}

		}
		return false;

	}
	
	@Override
	public List<ProductBarcodeToQRcodeVO> getBarcodeToQRcode() throws DaoException{
		try {

			String sql = " SELECT pb.id,pb.product_barcode,pb.product_id,pb.start_num,pb.end_num from product_barcode_to_qrcode pb  ";
			Query query = entityManager.createNativeQuery(sql);
			List<Object[]> result = query.getResultList();
			List<ProductBarcodeToQRcodeVO> vos = new ArrayList<ProductBarcodeToQRcodeVO>();

			if (result != null) {
				for (Object[] obj : result) {
					ProductBarcodeToQRcodeVO vo = new ProductBarcodeToQRcodeVO(
							((Integer) obj[0]).longValue(),
							obj[1] == null ? "" : obj[1].toString(),
							obj[2] == null ? "" : obj[2].toString(),
							obj[3] == null ? 0 : Integer.valueOf(obj[3].toString()),
							obj[4] == null ? 0 : Integer.valueOf(obj[4].toString()));
					vos.add(vo);
				}
			}
			return vos;
		} catch (Exception e) {
			throw new DaoException(
					"ProductDAOImpl.getBarcodeToQRcode() 出现异常！", e);
		}
	}
	
	
	@Override
	public boolean deleteBarcodeToQRcode(Long id) throws DaoException{
		try {

			String sql = " DELETE  FROM product_barcode_to_qrcode WHERE id=?1 ";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, id);
			int success=query.executeUpdate();
			if(success>0){
				return true;
			}
				
			else{
				return false;
			}
			 
		} catch (Exception e) {
			throw new DaoException(
					"ProductDAOImpl.getBarcodeToQRcode() 出现异常！", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ProductLismVo getByBarcodeProList(String barcode) {
//		String sql = "SELECT DISTINCT p.id,p.name,p.barcode,p.status,p.format FROM product p WHERE p.barcode=?1";
		
		String sql = "SELECT b.proId,b.proName,b.barcode,b.status,b.format,";
		sql +="b.busId,b.busName,b.license_no,b.address,b.qs_no,";
		sql +="b.contact,b.telephone,b.email,b.fax,b.about,b.type" ;
		sql += " FROM (SELECT pro.id as proId,pro.name as proName,pro.barcode,pro.status,pro.format,";
		sql +="bus.id as busId,bus.`name` as busName,bus.license_no,bus.address,pli.qs_no,";
		sql +="bus.contact,bus.telephone,bus.email,bus.fax,bus.about,bus.type" ;
		sql +=" FROM product_to_businessunit p2b" ;
		sql +=" INNER JOIN production_license_info pli ON pli.id = p2b.qs_id" ;
		sql +=" INNER JOIN business_unit bus ON bus.id = p2b.business_id"; 
		sql +=" INNER JOIN product pro ON pro.id = p2b.PRODUCT_ID AND pro.barcode = ?1) b";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter(1, barcode);
		List<Object[]> objs = query.getResultList();
		
		List<BusinessLicenseLismVo> bussPro = new ArrayList<BusinessLicenseLismVo>();
		ProductLismVo proLims = null;
		BusinessLicenseLismVo busList = null;
		int k = 0;
		for (Object[] obj : objs) {
			if(k == 0){
				proLims = new ProductLismVo();
				proLims.setId(Long.parseLong(obj[0].toString()));
				proLims.setProName(obj[1].toString());
				proLims.setBarcode(obj[2].toString());
				proLims.setStatus(obj[3].toString());
				proLims.setFormat(obj[4].toString());
			}
			busList = new BusinessLicenseLismVo();
			busList.setId(Long.parseLong(obj[5].toString()));
			busList.setName(obj[6]==null?"":obj[6].toString());
			busList.setLicenseNo(obj[7]==null?"":obj[7].toString());
			busList.setAddress(obj[8]==null?"":obj[8].toString());
			busList.setQsNo(obj[9]==null?"":obj[9].toString());
			busList.setContact(obj[10]==null?"":obj[10].toString());
			busList.setTel(obj[11]==null?"":obj[11].toString());
			busList.setEmail(obj[12]==null?"":obj[12].toString());
			busList.setFax(obj[13]==null?"":obj[13].toString());
			busList.setAbout(obj[14]==null?"":obj[14].toString());
			busList.setType(obj[15]==null?"":obj[15].toString());
			bussPro.add(busList);
			k++;
		}
		proLims.setBussPro(bussPro);
		
		return proLims;
	}
	
	public int updateProductCertByBarcode(String barcode,int cert){
		String sql="update product set product_certification="+cert+" where barcode='"+barcode+"'";
		Query query = entityManager.createNativeQuery(sql);
		return query.executeUpdate();
	}

	@Override
	public Product getAllProductsByOrgandid(long organization, long id) {

		String jpql="SELECT * from product where organization=?1";
		jpql+=" and id=?2";
		Query query = entityManager.createNativeQuery(jpql,Product.class);
		query.setParameter(1,organization);
		query.setParameter(2,id);
		List<Product> productList = query.getResultList();
		if(productList.size()==0){
			return null;
		}else{
			return productList.get(0);
		}
	
	}

	@Override
	public Long getByBarcodeProduct(String barcode) {
		String sql = "SELECT id,barcode from product where barcode = ?1 ";
				Query query = entityManager.createNativeQuery(sql);
				query.setParameter(1,barcode);
				List<Object[]> objs = query.getResultList();
				Long id = null;
				if(objs.size()>0){
					for (Object[] objects : objs) {
						id = Long.parseLong(objects[0].toString());
						break;
					}
				}
		return id;
	}
	
}