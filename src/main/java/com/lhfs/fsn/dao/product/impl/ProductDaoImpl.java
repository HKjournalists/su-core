package com.lhfs.fsn.dao.product.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.business.BusinessBrand;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductCategoryInfo;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.lhfs.fsn.dao.product.ProductDao;
import com.lhfs.fsn.vo.TestBusinessUnitVo;
import com.lhfs.fsn.vo.atgoo.ProductVO;
import com.lhfs.fsn.vo.product.ProductIdAndNameVO;
import com.lhfs.fsn.vo.product.ProductInfoVOToPortal;
import com.lhfs.fsn.vo.product.ProductSortVo;
import com.lhfs.fsn.vo.product.ProductVOWda;
import com.lhfs.fsn.vo.product.SearchProductVO;

@Repository(value="productLFDAO")
public class ProductDaoImpl extends BaseDAOImpl<Product> 
		implements ProductDao {
	@PersistenceContext private EntityManager entityManager;
	
	/**
	 * 功能描述：根据产品条形码查找产品信息
	 * @throws DaoException
	 * @author ZhangHui 2015/6/5
	 */
	@Override
	public Product findByBarcode(String barcode) throws DaoException {
		try {
			String condition = " WHERE e.barcode = ?1";
			Object[] params = new Object[]{barcode};
			List<Product> result = this.getListByCondition(condition, params);
			
			if(result != null && result.size() > 0){
				return result.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new DaoException("ProductDaoImpl.findByBarcode()-->" + e.getMessage(), e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> findNutritionById(long id) {
		String sql = "select p.id, p.cstm, p.ingredient, ns.name ,nr.unit,nr.per,nr.value,ns.daily_intake,nr.nrv"+
				  " from product as p, nutri_rpt as nr, nutri_std as ns where p.id = :pid and nr.product_id = p.id and nr.nutri_id = ns.id";
		List<Object[]>  list = entityManager.createNativeQuery(sql).setParameter("pid", id).getResultList();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ProductInstance findInstance(String barcode, String batchSeriaNo) {
		List<ProductInstance> result = entityManager
				.createNativeQuery(
						" select * from product_instance where "+
						" product_instance.product_id in (select product.id from product where product.barcode = ?1) and "+
						" product_instance.batch_serial_no = ?2 ",
						ProductInstance.class)
				.setParameter(1, barcode)
				.setParameter(2, batchSeriaNo)
				.getResultList();
		if(result != null && result.size() > 0){
			return result.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Product findByName(String sampleName) {
		List<Product> result = entityManager.createNativeQuery("select * from product where name=?1",Product.class).setParameter(1,sampleName).getResultList();
		if(result != null && result.size() > 0){
			return result.get(0);
		}
		return null;
	}

	/**
	 * 获取热点产品的总数,
	 * @throws DaoException 
	 */
	@Override
	public long countOfHotProduct() throws DaoException {
		try {
        	String sql = "SELECT COUNT(*) from (SELECT pro.id FROM product pro"+ 
					" LEFT JOIN product_instance sample ON pro.id = sample.product_id "+ 
					" LEFT JOIN test_result report ON sample.id = report.sample_id AND report.del = 0 "+ 
					" WHERE report.publish_flag =1 and report.test_type!=?1 "+
					" GROUP BY pro.id ) temp";
        	Query query = entityManager.createNativeQuery(sql);
        	query.setParameter(1,"第三方检测");
        	Object rtn = query.getSingleResult();
			return rtn == null ? 0 : Long.parseLong(rtn.toString());
        } catch (Exception e) {
            throw new DaoException("【DAO-error】获取热点产品的总数，出现异常", e);
        }
	}
	
	/**
     * 查找热点产品列表
     * @param page
     * @param pageSize
     * @return List<HotProductVO>
     * @throws DaoException
     */
	@SuppressWarnings("unchecked")
	@Override
    public List<SearchProductVO> getListOfHotProductWithPage(String condition, int page, int pageSize) throws DaoException {
        try {
            String sql ="SELECT pro.id,pro.name,pro.cstm,pro.imgurl,count(pro.id),pro.risk_succeed," +
		    		"pro.riskIndex,pro.test_property_name,pro.nutri_label,pro.product_certification FROM product pro"+ 
                    " LEFT JOIN product_instance sample ON pro.id = sample.product_id "+ 
                    " LEFT JOIN test_result report ON sample.id = report.sample_id AND report.del = 0 "+ 
                    " WHERE report.publish_flag =1 " + condition + " and report.test_type !='第三方检测' and report.test_type is not null "+
                    " GROUP BY pro.id ORDER BY COUNT(pro.id) DESC,report.service_order DESC";
        	if(page >= 0){
				sql += " limit "+ page + "," + pageSize;
			}
        	Query query = entityManager.createNativeQuery(sql);
        	List<Object[]> listOfProduct = query.getResultList();
        	if(listOfProduct!=null&&listOfProduct.size()>0){
        		List<SearchProductVO> listOfProductVO = new ArrayList<SearchProductVO>();
        		SearchProductVO spvo;
        		for(Object[] objs:listOfProduct){
        			spvo=new SearchProductVO();
        			spvo.setId(Long.parseLong(objs[0]!=null?objs[0].toString():null));
        			spvo.setName(objs[1]!=null?objs[1].toString():null);
        			spvo.setCstm(objs[2]!=null?objs[2].toString():null);
        			spvo.setImgUrl(objs[3]!=null?objs[3].toString():null);
        			spvo.setReportCounts(Integer.parseInt(objs[4]!=null?objs[4].toString():"0"));
        			spvo.setRiskSucceed(objs[5] != null ? Boolean.valueOf(objs[5].toString()): false);
                    spvo.setRiskIndex(objs[6] != null ? Double.valueOf(objs[6].toString()) : null);
                    spvo.setTestPropertyName(objs[7] != null ? objs[7].toString() : "");
                    String nutriLabel = objs[8] != null ? objs[8].toString() : "";
                    spvo.setNutriLabel(formatNutriLabel(nutriLabel));
                    spvo.setCertificationStatus(Integer.valueOf(objs[9].toString()));
        			listOfProductVO.add(spvo);
        		}
        		return listOfProductVO;
        	}
        	return null;
        } catch (Exception e) {
            throw new DaoException("ProductDaoImpl.getListOfHotProductWithPage() 查找热点产品列表，出现异常！", e);
        }
    }
	
	/**
     * 根据产品id查找一条热点产品信息
     * @param productId
     * @return List<HotProductVO>
     * @throws DaoException
     */
	@SuppressWarnings("unchecked")
	@Override
    public List<SearchProductVO> getHotProductByProductId(Long productId) throws DaoException {
        try {
        	if(productId==null){return null;}
        	String sql ="SELECT pro.id,pro.name,pro.cstm,pro.imgurl,count(pro.id),pro.risk_succeed," +
		    		"pro.riskIndex,pro.test_property_name,pro.nutri_label,pro.product_certification FROM product pro"+ 
					" LEFT JOIN product_instance sample ON pro.id = sample.product_id "+ 
					" LEFT JOIN test_result report ON sample.id = report.sample_id AND report.del = 0 "+ 
					" WHERE report.publish_flag =1 and pro.id=?1 and report.test_type!=?2"+
					" GROUP BY pro.id ORDER BY COUNT(pro.id) DESC,report.service_order DESC" +
					" limit 0,1";
        	Query query = entityManager.createNativeQuery(sql);
        	query.setParameter(1, productId);
        	query.setParameter(2, "第三方检测");
        	List<Object[]> listOfProduct = query.getResultList();
        	if(listOfProduct!=null&&listOfProduct.size()>0){
        		List<SearchProductVO> listOfProductVO = new ArrayList<SearchProductVO>();
        		SearchProductVO spvo;
        		for(Object[] objs:listOfProduct){
        			spvo=new SearchProductVO();
        			spvo.setId(Long.parseLong(objs[0]!=null?objs[0].toString():null));
        			spvo.setName(objs[1]!=null?objs[1].toString():null);
        			spvo.setCstm(objs[2]!=null?objs[2].toString():null);
        			spvo.setImgUrl(objs[3]!=null?objs[3].toString():null);
        			spvo.setReportCounts(Integer.parseInt(objs[4]!=null?objs[4].toString():"0"));
        			spvo.setRiskSucceed(objs[5] != null ? Boolean.valueOf(objs[5].toString()): false);
                    spvo.setRiskIndex(objs[6] != null ? Double.valueOf(objs[6].toString()) : null);
                    spvo.setTestPropertyName(objs[7] != null ? objs[7].toString() : "");
                    String nutriLabel = objs[8] != null ? objs[8].toString() : "";
                    spvo.setNutriLabel(formatNutriLabel(nutriLabel));
                    spvo.setCertificationStatus(Integer.valueOf(objs[9].toString()));
        			listOfProductVO.add(spvo);
        		}
        		return listOfProductVO;
        	}
        	return null;
        } catch (Exception e) {
            throw new DaoException("ProductDaoImpl.getHotProductByProductId() 根据产品id查找一条热点产品信息，出现异常", e);
        }
    }

	/**
	 * 各式化营养指数，使得多个标签之间使用 “|” 分隔
	 * @author tangxin
	 */
	private String formatNutriLabel(String nutriLabel) {
		/* 处理营养指数 */
		if(nutriLabel == null) {
			return nutriLabel;
		}
        nutriLabel = nutriLabel.replaceAll(";", "|");
        if(nutriLabel.endsWith("|")) {
			nutriLabel = nutriLabel.substring(0, nutriLabel.length()-1);
		}
		return nutriLabel;
	}
	
	/**
	 * 已知产品类别，查找报告最多的若干个产品
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SearchProductVO> getListOfProductByCategory(String category,int page, int pageSize) throws DaoException {
		List<SearchProductVO> listOfProductVO=null;
		try {
			String sql = " SELECT pro.id,pro.name,pro.cstm,pro.imgurl FROM product pro " +
					" LEFT JOIN product_instance sample ON pro.id = sample.product_id "+ 
					" LEFT JOIN test_result report ON sample.id = report.sample_id AND report.del = 0 "+ 
					" WHERE pro.category LIKE ?1 AND report.publish_flag = 1 "+ 
					" GROUP BY pro.id ORDER BY COUNT(report.id) DESC";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, category + "%");
			if (page > 0) {
				query.setFirstResult((page - 1) * pageSize);
				query.setMaxResults(pageSize);
			}
			List<Object[]> listOfProduct = query.getResultList();
        	if(listOfProduct!=null&&listOfProduct.size()>0){
        		listOfProductVO=new ArrayList<SearchProductVO>();
        		SearchProductVO spvo;
        		for(Object[] objs:listOfProduct){
        			spvo=new SearchProductVO();
        			spvo.setId(Long.parseLong(objs[0]!=null?objs[0].toString():null));
        			spvo.setName(objs[1]!=null?objs[1].toString():null);
        			spvo.setCstm(objs[2]!=null?objs[2].toString():null);
        			spvo.setImgUrl(objs[3]!=null?objs[3].toString():null);
        			listOfProductVO.add(spvo);
        		}
        	}
			return listOfProductVO;
		} catch (Exception e) {
			throw new DaoException("【dao-error】已知产品类别，查找报告最多的若干个产品,出现异常。", e);
		}
	}
	
	/**
	 * portal接口，根据企业查询企业下的产品按报告数量排序
     * @updated TangXin 2015/03/31
	 */
	public int countProductByBusNameAndCategoryAndBrand(String enterpriseName,String category,Integer catLevel,String Brand,
			 String ordername, String ordertype, String nutriLabel){
		int count=0;
		String condition = "";
		condition = getConditionByTestTypeOrder(ordername,ordertype);
		try{
		    String proCatCondition = ""; // 根据产品分类级拼查询条件
		    String nutriLabelCondi = "";
		    if(nutriLabel != null && !"".equals(nutriLabel)){
		    	nutriLabelCondi = " AND product.nutri_label like '%"+nutriLabel+"%' ";
		    }
            /* 当产品分类是第三级时 查询条件是三级分类的id */
            if(catLevel != null && catLevel == 3) {
                proCatCondition = " AND product.category_id = ?3 ";
            }else {
                /* 分类属于一二级时 查询条件是 code */
                proCatCondition = " AND cat.`code` LIKE ?3 ";
                }
            String sql="SELECT count(tmpe.id) FROM (SELECT product.id FROM product " + condition +
                    " RIGHT JOIN business_unit bus ON product.organization=bus.organization " +
                    " RIGHT JOIN business_brand brand ON product.business_brand_id=brand.id " +
                    " LEFT JOIN product_category cat ON product.category=cat.`code` " +
                    " LEFT JOIN product_instance smp ON product.id=smp.product_id " +
                    " WHERE bus.`name`=?1 AND brand.`name` LIKE ?2 " + proCatCondition + nutriLabelCondi +
                    " GROUP BY product.id) tmpe";
			Query query=entityManager.createNativeQuery(sql);
			query.setParameter(1,enterpriseName);
			query.setParameter(2,"%"+Brand+"%");
			query.setParameter(3,(catLevel != null && catLevel == 3)? Long.valueOf(category) : category+"%");
			count=Integer.parseInt(query.getSingleResult().toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		return count;
	}
	
	/**
     * 已知企业名称，按产品名称模糊查询产品
     * @updated TangXin 2015/03/31
     */
	@SuppressWarnings("unchecked")
	@Override
	public  List<Object[]> getListOfProductByName2(String enterpriseName,String category,Integer catLevel,String brand,
			int page, int pageSize, String ordername, String ordertype,String nutriLabel)throws DaoException{
	    List<Object[]> lsitResult=null;
		String sql="", condition = "";
		condition = getConditionByTestTypeOrder(ordername,ordertype);
		try{
		    String proCatCondition = ""; // 根据产品分类级拼查询条件
		    String nutriLabelCondi = "";
		    if(nutriLabel != null && !"".equals(nutriLabel)){
		    	nutriLabelCondi = " product.nutri_label like'%"+nutriLabel+"%' ";
		    }
            /* 当产品分类是第三级时 查询条件是三级分类的id */
            if(catLevel != null && catLevel == 3) {
                proCatCondition = " AND product.category_id = ?3 ";
            }else {
                /* 分类属于一二级时 查询条件是 code */
                proCatCondition = " AND cat.`code` LIKE ?3 ";
                }
            sql="SELECT product.id,product.name,product.imgurl,product.cstm,product.qscore_self,product.qscore_censor,product.qscore_sample," +
            		"product.risk_succeed,product.riskIndex,product.test_property_name,product.nutri_label FROM product " + condition +
                    " RIGHT JOIN business_unit bus ON product.organization=bus.organization " +
                    "RIGHT JOIN business_brand brand ON product.business_brand_id=brand.id " +
                    "LEFT JOIN product_category cat ON product.category=cat.`code` WHERE bus.`name`=?1 AND brand.`name` LIKE ?2 " + proCatCondition + nutriLabelCondi +
                    " GROUP BY product.id ORDER BY tmp.cont "+ordertype+",product.id desc";
            Query query=entityManager.createNativeQuery(sql);
			if (page >= 0) {
				query.setFirstResult(page);
				query.setMaxResults(pageSize);
			}
			query.setParameter(1, enterpriseName);
			query.setParameter(2, "%"+brand+"%");
			query.setParameter(3,  (catLevel != null && catLevel == 3)? Long.valueOf(category) : category+"%");
			lsitResult=query.getResultList();
		}catch(Exception e){
			throw new DaoException("【dao-error】已知企业名称，按产品名称模糊查询产品,出现异常。", e);
		}
		return lsitResult;
	}
	
	/**
	 * 根据报告检测类别，返回排序语句
	 * @param testType
	 * @param ordertype 排序
	 * @return
	 */
	private String getConditionByTestTypeOrder(String testType,String ordertype){
		String condition = "";
		String testTyepCondition = "";
		if(testType==null) return "";
		if(!testType.equals("综合")){
			testTyepCondition = " AND rep.test_type='"+testType+"' ";
		}
		condition = " LEFT JOIN (SELECT smp.product_id,count(rep.id) cont FROM " +
				"product_instance smp LEFT JOIN test_result rep " +
				"ON smp.id=rep.sample_id AND rep.del = 0 WHERE rep.publish_flag='1' " +
				"AND rep.test_type is not null AND rep.test_type!='' " + testTyepCondition +
				" GROUP BY smp.product_id) tmp ON product.id=tmp.product_id ";
		return condition;
	}
	
	/**
	 * 根据org查找product
	 */
	@Override
	public List<Product> getProductByOrg(long org) throws DaoException {
		try {
			String condition = " WHERE e.organization= ?1 ";
			return this.getListByCondition(condition, new Object[]{org});
		} catch (Exception e) {
			throw new DaoException("【DAO-error】根据org查找product，出现异常", e);
		}
	}

	/**
	 * 根据商品名称或是条码，或是名称和条目得到相关商品
     * @param name
     * @param barcode
     * @return List<Product>
	 */
    @SuppressWarnings("unchecked")
	@Override
    public List<Product> findByNameAndBarcode(int page,int pageSize,String name, String barcode,Long orgId) throws DaoException {
        List<Product> products = null;
        try {
        	String sql = "";
        	String nameCondition =" AND p.name LIKE '%"+name+"%' ";
        	String barcodeCondition =" AND p.barcode LIKE '%"+barcode+"%' ";
            if (orgId>0) {
            	sql = " SELECT p.* FROM product p WHERE id IN (select product_id from t_meta_initialize_product where organization is not null and organization !='' and organization= "+orgId+")";
            	if (!name.equals("")&&barcode.equals("")) {
            		sql = sql + nameCondition ;
            	}
            	if (name.equals("")&&!barcode.equals("")) {
            		sql = sql + barcodeCondition;
            	}
            	if (!name.equals("")&&!barcode.equals("")) {
            		sql = sql + barcodeCondition + nameCondition;
            	}
            }else{
            	sql = " SELECT p.* FROM product p WHERE 1=1 ";
            	if (!name.equals("")&&barcode.equals("")) {
            		sql = sql + nameCondition ;
            	}
            	if (name.equals("")&&!barcode.equals("")) {
            		sql = sql + barcodeCondition;
            	}
            	if (!name.equals("")&&!barcode.equals("")) {
            		sql = sql + barcodeCondition + nameCondition;
            	}
            }
            sql = sql + " LIMIT ?1,?2 ";
            Query query = entityManager.createNativeQuery(sql,Product.class);
            query.setParameter(1, (page-1)*pageSize);
            query.setParameter(2, pageSize);
            products=query.getResultList();
            return products.size() > 0 ? products : null ;
        } catch (Exception e) {
            throw new DaoException("【DAO-error】ProductDaoImpl.findByNameAndBarcode() 根据商品名称或是条码，或是名称和条目得到相关商品，出现异常", e);
        }
    }

    /**
     * 根据商品名称或是条码，或是名称和条目得到相关商品 的总条数
     * @param name 商品名称
     * @param barcode 商品barcode
     * @return long
     */
    @Override
    public long getListOfProductCount(String name, String barcode,long orgId) throws DaoException {
        
        try {
        	String sql = "";
        	String nameCondition =" AND p.name LIKE '%"+name+"%' ";
        	String barcodeCondition =" AND p.barcode LIKE '%"+barcode+"%' ";
            if (orgId>0) {
            	sql = " SELECT count(*) FROM product p WHERE id IN (select product_id from t_meta_initialize_product where organization is not null and organization !='' and organization= "+orgId+")";
            	if (!name.equals("")&&barcode.equals("")) {
            		sql = sql + nameCondition ;
            	}
            	if (name.equals("")&&!barcode.equals("")) {
            		sql = sql  + barcodeCondition;
            	}
            	if (!name.equals("")&&!barcode.equals("")) {
            		sql = sql + barcodeCondition + nameCondition;
            	}
            }else{
            	sql = " SELECT count(*) FROM product p WHERE 1=1 " ;
            	if (!name.equals("")&&barcode.equals("")) {
            		sql = sql + nameCondition ;
            	}
            	if (name.equals("")&&!barcode.equals("")) {
            		sql = sql + barcodeCondition;
            	}
            	if (!name.equals("")&&!barcode.equals("")) {
            		sql = sql + barcodeCondition + nameCondition;
            	}
            }
            return this.countBySql(sql, null);
        } catch (Exception e) {
            throw new DaoException("【DAO-error】ProductDaoImpl.getListOfProductCount() 根据商品名称或是条码，或是名称和条目得到相关商品 的总条数，出现异常"+e.getMessage(), e);
        }
    }

    /**
     * 根据批次或是生产日期范围，得到相关商品的总条数
     * @param batch 批次
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return long
     */
    @Override
    public Long getListOfProductCountByBatchOrProductDate(String batch,
            String startTime, String endTime,long productId) throws DaoException {
        try {
            String sql ="SELECT COUNT(e.id) FROM product e LEFT JOIN product_instance pro_i ON  e.id = pro_i.product_id " ;
            Query query = null;
            if (!batch.equals("")) {
                sql = sql + "WHERE pro_i.batch_serial_no = ?1 AND e.id = ?2 ";
                query = entityManager.createNativeQuery(sql);
                query.setParameter(1, batch);
                query.setParameter(2, productId);
            }

            if (!startTime.equals("") && !endTime.equals("")) {
                sql = sql + "WHERE pro_i.production_date >= ?1 AND pro_i.production_date <= ?2 AND e.id = ?3 ";
                query = entityManager.createNativeQuery(sql);
                query.setParameter(1, startTime);
                query.setParameter(2, endTime);
                query.setParameter(3, productId);
            }
            return Long.parseLong(query.getSingleResult().toString());
        }catch (Exception e) {
              e.printStackTrace();
        }
         return null;   
    }

    /**
     * 根据企业名称或是营业执照号 ，获取该企业下经营的所有产品
     * @param name 企业名称
     * @param licenseNo 营业执照号
     * @return List<Product>
     */
    @Override
    public List<Product> loadProductListForBusinessUnit(String name,
            String licenseNo) throws DaoException {
        try {
            String sql = "SELECT * FROM product p RIGHT JOIN  business_unit b ON p.organization = b.organization ";
            Object[] objects = null;
            if (!name.equals("") && licenseNo.equals("")) {
                sql = sql + "WHERE b.name = ?1";
                objects = new Object[] { name };
            }
            if (name.equals("") && !licenseNo.equals("")) {
                sql = sql + "WHERE b.license_no = ?1";
                objects = new Object[] { licenseNo };
            }
            return this.getListBySQL(Product.class, sql, objects);
        } catch (JPAException e) {
            throw new DaoException("【DAO-error】ProductDaoImpl.loadProductListForBusinessUnit() 根据企业名称或是营业执照号 ，获取该企业下经营的所有产品，出现异常", e);
        }
    }

	public List<ProductInstance> getProductInstanceListById(int page,int pageSize,Long id)
			throws DaoException {
		try {
			String sql = "SELECT p.* FROM product_instance p where p.product_id = ?1 GROUP BY p.batch_serial_no LIMIT ?2,?3 ";
			return this.getListBySQL(ProductInstance.class, sql, new Object[] {id,(page-1)*pageSize,pageSize});
		} catch (JPAException e) {
			throw new DaoException("【DAO-error】ProductDaoImpl.getProductInstanceListById() 根据id获取商品实例，出现异常",e);
		}
	}

	@Override
	public Long getCountByproId(Long id) throws DaoException {
		String sql = " SELECT count(DISTINCT p.batch_serial_no ) FROM product_instance p where p.product_id = ?1";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter(1, id);
    	Object rtn = query.getSingleResult();
		return rtn == null ? 0 : Long.parseLong(rtn.toString());
	}

    /**
     * 统计超市没有报告的产品
     * @param organizationId
     * @return
     * @throws DaoException
     */
    public long countMarketProduct(Long organizationId,boolean haveReport,String name,String barcode) throws DaoException{
    	try{
    		String sql="";
    		if(haveReport){
    				sql = "SELECT count(*) FROM product p ,(SELECT tmp.product_id from t_meta_initialize_product mp LEFT JOIN "+
    						  "(SELECT samp.id,samp.product_id FROM product_instance samp LEFT JOIN test_result rpt "+
    						  "ON samp.id = rpt.sample_id AND rpt.del = 0 GROUP BY samp.product_id) tmp "+
    						  "ON mp.product_id = tmp.product_id where mp.organization = ?1) tmp1 "+
    						  "WHERE tmp1.product_id is not NULL AND p.id = tmp1.product_id AND p.name LIKE ?2 AND p.barcode LIKE ?3 ";
    				
    		}else{
    				sql="SELECT count(*) from product p , (SELECT mp.product_id,mp.organization,sp.id AS 'sampId' " +
    						"from t_meta_initialize_product mp LEFT JOIN product_instance sp " +
    						"ON mp.product_id=sp.product_id) tmp where tmp.sampId is null AND tmp.organization =?1 AND p.id = tmp.product_id AND p.name LIKE ?2 AND p.barcode LIKE ?3 ";
    			
    		}
    		return this.countBySql(sql, new Object[]{organizationId,"%"+name+"%","%"+barcode+"%"});
    	}catch(Exception e){
    		throw new DaoException("ProductDaoImpl.countMarketProduct() "+e.getMessage(),e);
    	}
    }
    
    /**
	 * 监管系统获取超市没有报告产品
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProductVOWda> getListOfMarketByMarketIdWithPage(Boolean flag,Long organizationId, boolean haveReport,
			int page, int pageSize,String name,String barcode) throws DaoException {
		try{
			
			String limit = "";
			if (page > 0 && !flag) {
				limit = "LIMIT "+ (page-1) * pageSize +"," + pageSize;
			}
			String sql = "";
			if(haveReport){
				sql =  "SELECT pro.id,pro.`name`,pro.barcode,pro.format,ttmp.busName,pro.expiration,ttmp.email,ttmp.telephone from  "+
						"(SELECT mp.product_id,tmp.busName,tmp.email,tmp.telephone FROM t_meta_initialize_product mp, "+
						"(SELECT samp.id,samp.product_id,bus.`name` AS busName ,bus.email as email ,bus.telephone as telephone FROM product_instance samp LEFT JOIN test_result rpt "+
						"ON samp.id = rpt.sample_id AND rpt.del = 0 LEFT JOIN business_unit bus ON samp.producer_id = bus.id GROUP BY samp.product_id) tmp  "+
						"WHERE mp.product_id=tmp.product_id and mp.organization = ?1 ) ttmp , product pro "+
						"WHERE ttmp.product_id=pro.id AND pro.name LIKE ?2 AND pro.barcode LIKE ?3 " +limit;
			}else{
				sql ="SELECT market.* from (SELECT ttmp.id,ttmp.`name`,ttmp.barcode,ttmp.format,bus.`name` as 'busName',ttmp.expiration ,bus.email,bus.telephone FROM  "+
						"(SELECT tmp.sampId,pro.id,pro.`name`,pro.barcode,pro.format,pro.expiration,pro.organization ,pro.producer_id as producer_id from "+
						"(SELECT mp.product_id,sp.id AS sampId FROM t_meta_initialize_product mp LEFT JOIN product_instance sp "+
						"ON mp.product_id=sp.product_id WHERE mp.organization=?1) tmp, product pro "+
						"WHERE tmp.product_id=pro.id  AND pro.name LIKE ?2 AND pro.barcode LIKE ?3) ttmp "+
						"LEFT JOIN business_unit bus ON ttmp.producer_id = bus.id "+
						"where ttmp.sampId is NULL "+limit+") market ";
			}
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, organizationId);
			query.setParameter(2, "%"+name+"%");
			query.setParameter(3, "%"+barcode+"%");
			List<Object[]> listProduct = query.getResultList();
			return getListProductVOWdaByListObject(listProduct);
		}catch(Exception e){
			throw new DaoException("ProductDaoImpl.getListOfMarketByMarketIdWithPage() "+e.getMessage(),e);
		}
	}

	/**
	 * 通过一个Object数据，封装并返回一个ProductVOWda对象集合
	 * @param listObjects
	 * @return
	 * @throws Exception
	 */
	private List<ProductVOWda> getListProductVOWdaByListObject(List<Object[]> listObjects) throws Exception{
		List<ProductVOWda> productVO = null;
		if(listObjects.size()>0){
			productVO = new ArrayList<ProductVOWda>();
			for(int i=0;i<listObjects.size();i++){
				ProductVOWda vo = new ProductVOWda();
				Object[] pro = (Object[]) listObjects.get(i);
				vo.setId(Long.parseLong(pro[0]!=null?pro[0].toString():"0"));
				vo.setName(pro[1]!=null?pro[1].toString():"");
				vo.setBarcode(pro[2]!=null?pro[2].toString():"");
				vo.setFormat(pro[3]!=null?pro[3].toString():"");
				vo.setExpiration(pro[5]!=null?pro[5].toString():"");
				vo.seteMail(pro[6]!=null?pro[6].toString():"");
				vo.setTelephone(pro[7]!=null?pro[7].toString():"");
				vo.setProducer("");
				vo.setAllBatchExpire(false);
				if(pro[4]!=null){
					if(pro[4].toString().trim().equals("贵州省分析测试研究院")||
							pro[4].toString().trim().equals("永辉超市")){
						vo.setProducer("");
					}else{
						vo.setProducer(pro[4].toString().trim());
					}
				}
				productVO.add(vo);
			}
		}
		return productVO;
	}
	
	/**
	 * 监管接口，根据organization统计没有生产许可证的产品数量
	 * @param organization
	 * @return
	 * @throws DaoException
	 */
	@Override
	public long countNoneProLicinfoByOrganization(Long organization,String name,String barcode) throws DaoException{
		try{
			String sql = "SELECT count(tmp.product_id) FROM product p,  "+
							"(SELECT pm.product_id,pb.QS_NO FROM t_meta_initialize_product pm LEFT JOIN product_to_businessunit pb  "+
							"ON pm.product_id=pb.PRODUCT_ID where pm.organization= ?1) tmp "+
							"WHERE p.id=tmp.product_id AND tmp.QS_NO is NULL AND p.name LIKE ?2 AND p.barcode LIKE ?3 ";
			return this.countBySql(sql, new Object[]{organization,"%"+name+"%","%"+barcode+"%"});
		}catch(JPAException jpae){
			throw new DaoException("ProductDaoImpl.countNoneProLicinfoByOrganization() "+jpae.getMessage(),jpae);
		}
	}
	
	/**
	 * 监管接口，查询超市没有生产许可证的产品列表
	 * @param organization
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProductVOWda> getListOfMarketNoneProlicinfoByOrgIdWithPage(Boolean flag,
			Long organization, int page, int pageSize,String name,String barcode) throws DaoException{
		try{
			String limit = "";
			if (page > 0 && !flag) {
				limit = " LIMIT "+ (page-1) * pageSize +"," + pageSize;
			}
			String sql="SELECT ttmp.id,ttmp.pName,ttmp.barcode,ttmp.format,bus.`name`,ttmp.expiration,bus.email,bus.telephone FROM "+
						"(SELECT pro.id,pro.barcode,pro.`name` as pName,pro.format,pro.expiration, pro.organization, pro.producer_id producer FROM "+
						"(SELECT pm.product_id,pb.QS_NO FROM t_meta_initialize_product pm LEFT JOIN product_to_businessunit pb "+
						"ON pm.product_id=pb.PRODUCT_ID where pm.organization= ?1) tmp, product pro "+
						"where tmp.product_id = pro.id AND pro.name like ?2 and pro.barcode LIKE ?3 "+
						"AND tmp.QS_NO is NULL"+limit+") ttmp LEFT JOIN business_unit bus ON ttmp.producer=bus.id ";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, organization);
			query.setParameter(2, "%"+name+"%");
			query.setParameter(3, "%"+barcode+"%");
			List<Object[]> listProduct = query.getResultList();
			return getListProductVOWdaByListObject(listProduct);
		}catch(Exception e){
			throw new DaoException("ProductDaoImpl.getListOfMarketByMarketIdWithPage() "+e.getMessage(),e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductInstance> getListOfProductInstByBatchOrProductDate(
			int page, int pageSize, String batch, String startTime,
			String endTime, long productId) throws DaoException {
		try {
        	String sql = " select * from product_instance where product_id = ?1 ";
        	 if (!batch.equals("") && !startTime.equals("")) {
	                sql = sql + " AND batch_serial_no = '"+batch+"' AND (production_date BETWEEN '"+
	                	 startTime+"' AND '"+endTime+"' ) ";
	            }
            if (batch.equals("") && !startTime.equals("")) {
            	sql = sql + " AND (production_date BETWEEN '"+
	                	 startTime+"' AND '"+endTime+"') ";
            }
            if (!batch.equals("") && startTime.equals("")) {
            	sql = sql + " AND batch_serial_no = '"+batch+"'" ;
            }
            sql = sql + " GROUP BY batch_serial_no LIMIT ?2,?3 ";
        	 Query query = entityManager.createNativeQuery(sql,ProductInstance.class);
        	 query.setParameter(1, productId);
        	 query.setParameter(2, (page-1)*pageSize);
        	 query.setParameter(3, pageSize);
             List<ProductInstance> list = query.getResultList();
             return list.size() > 0 ? list : null ;
        } catch (Exception e) {
        	throw new DaoException("ProductDaoImpl.getListOfProductInstByBatchOrProductDate() "+e.getMessage(),e);
        }
	}

	/**
	 * 提供给食安监的接口：根据产品barcode 模糊查找相关batch（批次）
	 * @param barcode 产品条形码
	 * @return List<Object>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getBatchForBarcode(String barcode) throws DaoException {
		try {
        	String sql = " SELECT DISTINCT t.po_batch FROM t_meta_purchaseorder_info t WHERE t.po_product_info_id=?1 ";
        	 Query query = entityManager.createNativeQuery(sql);
        	 query.setParameter(1, barcode);
             List<Object> list = query.getResultList();
             return list.size() > 0 ? list : null ;
        } catch (Exception e) {
        	throw new DaoException("ProductDaoImpl.getBatchForBarcode() "+e.getMessage(),e);
        }
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getListOfMarketAllProductsWithPage(
			Long organization, int page, int pageSize) throws DaoException {
		try {
        	String sql = " select * from product where id in(select product_id from t_meta_initialize_product where organization = ?1 ) LIMIT ?2,?3 ";
        	 Query query = entityManager.createNativeQuery(sql,Product.class);
        	 query.setParameter(1, organization);
        	 query.setParameter(2, (page-1)*pageSize);
        	 query.setParameter(3, pageSize);
             List<Product> list = query.getResultList();
             return list.size() > 0 ? list : null ;
        } catch (Exception e) {
        	throw new DaoException("ProductDaoImpl.getListOfMarketAllProductsWithPage() "+e.getMessage(),e);
        }
	}

	//cxl
	@Override
	public long countMarketAllProduct(Long organization) throws DaoException {
		try {
        	String sql = " select count(*) from product where id in(select product_id from t_meta_initialize_product where organization = ?1 ) ";
            return this.countBySql(sql, new Object[]{organization});
        } catch (Exception e) {
        	throw new DaoException("ProductDaoImpl.countMarketAllProduct() "+e.getMessage(),e);
        }
	}

	//根据产品组织机构  查找产品 并按报告数量降序
	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getReportMaxProductByOrganization(Long productCount,Long organization)
			throws DaoException {
		try {
			/*String sql = " SELECT pro.id,pro.name,pro.imgurl FROM product pro " +
					"LEFT JOIN (SELECT smp.product_id,count(rep.id) cont FROM " +
					"product_instance smp LEFT JOIN test_result rep ON smp.id=rep.sample_id AND rep.del = 0 WHERE rep.publish_flag='1' " +
					"AND rep.test_type is not null AND rep.test_type!='' GROUP BY smp.product_id) tmp " +
					"ON pro.id=tmp.product_id LEFT JOIN product_instance sample ON pro.id = sample.product_id " +
					"LEFT JOIN test_result report ON sample.id = report.sample_id AND report.del = 0 WHERE pro.organization = ?1 " +
					"GROUP BY pro.id ORDER BY tmp.cont DESC,pro.id DESC LIMIT 0,?2 ";*/
			String sql= "SELECT pro.id,pro.name,pro.imgurl FROM product pro "+
					"LEFT JOIN product_instance ip ON ip.product_id=pro.id "+
					"LEFT JOIN test_result report ON ip.id = report.sample_id "+
					"WHERE pro.organization = ?1 AND report.publish_flag='1' AND "+ 
					"report.test_type is not null AND report.test_type!='' "+
					"GROUP BY pro.id ORDER BY count(report.id) DESC,pro.id DESC LIMIT 0,?2 " ;
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, organization);
			query.setParameter(2, productCount);
			List<Object[]> objs = query.getResultList();
			List<Product> products =new ArrayList<Product>();
			if(objs != null && objs.size() > 0) {
				for(Object[] obj:objs){
					Product product=new Product();
					product.setId(obj[0] != null ? Long.parseLong(obj[0].toString()) : -1L);
					product.setName(obj[1] != null ? obj[1].toString() : "");
					product.setImgUrl(obj[2] != null ? obj[2].toString() : "");
					products.add(product);
				}
			}
			return products;
        } catch (Exception e) {
        	throw new DaoException("ProductDaoImpl.countMarketAllProduct() "+e.getMessage(),e);
        }
	}

	@Override
	public long getCountByBusNameOrLisNo(String name, String licenseNo)
			throws DaoException {
		String sqlName= " and bus.name like '%"+name+"%' ";
		String sqlLisNo = " and bus.license_no like '%"+licenseNo+"%' ";
		try {
        	String sql = " select COUNT(*) from business_unit bus " +
					" where organization is not null and organization!='' and type='流通企业' ";
        	if(!name.equals("")&&licenseNo.equals("")){
				sql = sql+sqlName;
			}
			if(name.equals("")&&!licenseNo.equals("")){
				sql = sql+sqlLisNo;
			}
			if(!name.equals("")&&!licenseNo.equals("")){
				sql = sql+sqlName+sqlLisNo;
			}
        	return this.countBySql(sql, null);
        } catch (Exception e) {
        	throw new DaoException("ProductDaoImpl.getCountByBusNameOrLisNo() "+e.getMessage(),e);
        }
	}
	
	/**
	 * 根据指定商品列和值查询满足条件的商品列表
	 * @param queryName
	 * @param queryParam
	 * @param bCn
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProductIdAndNameVO> searchBrandInfoByPQ(String queryName, String queryParam,
			boolean bCn) {
		try {
			String sql="select p.id,p.name from product p where p."+queryName+" like '%"+queryParam+"%' "; 
			Query query=entityManager.createNativeQuery(sql);
			//query.setParameter(1, "'%"+queryParam+"%'");
			List<Object[]> result = query.getResultList();
			List<ProductIdAndNameVO> productVos = new ArrayList<ProductIdAndNameVO>();
			if(result.size() <= 0){
				return null;
			}
			for (Object[] obj : result) {
				Long proId = Long.valueOf(obj[0].toString());
				String name = obj[1] !=null ? obj[1].toString() : "";
				ProductIdAndNameVO productVO = new ProductIdAndNameVO(proId, name, null);
				productVos.add(productVO);
			}
			return productVos;
		} catch (Exception e) {
		}
		return null;
	}
	/**
	 * 根据指定产品id获取相同分类产品列表
	 * @author ZhaWanNeng	2015/05/06
	 */
	@SuppressWarnings("unchecked")
	public List<ProductSortVo> getClassifyProductByid(long id,int pageSize,int page) throws DaoException {
		try {
			String sql = "select DISTINCT count(res.id),p.category,p.name,p.id,p.imgurl from " +
					"(select pro.category AS category,pro.name as `name`,pro.id as id,pro.imgurl as imgurl from product pro " +
					"where pro.category_id = " +
					"(select p.category_id from product p where  p.id = " +id+ ")) p " +
					"LEFT JOIN product_instance pin ON  pin.product_id = p.id  " +
					"LEFT JOIN test_result res ON res.sample_id = pin.id AND res.del = 0 AND res.publish_flag = '1'  and res.test_type != '' and res.test_type is not null " +
					"where  p.id != " +id+
					" GROUP BY p.id ORDER BY count(res.id) DESC ";
			Query query = entityManager.createNativeQuery(sql);
			query.setFirstResult((page - 1)*pageSize);
			query.setMaxResults(pageSize);
			
			List<Object[]> result = query.getResultList();
			List<ProductSortVo> productSortVo = new ArrayList<ProductSortVo>();
			if(result.size() <= 0){
				return null;
			}
			for (Object[] obj : result) {
				String name = obj[2] !=null ? obj[2].toString() : "";
				Long productid = Long.valueOf(obj[3].toString());
				String imgUrl = obj[4] !=null ? obj[4].toString() : "";
				ProductSortVo productSort = new ProductSortVo(productid, name, imgUrl);
				productSortVo.add(productSort);
			}
			return productSortVo;
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.getClassifyProductByid() ", e);
		}
	}
	
	/**
	 * Portal 接口:根据产品id集合返回产品集合
	 * @author LongXianZhen 2015/06/25
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProductIdAndNameVO> getProductByProIds(String proIds)
			throws DaoException {
		try {
			String sql = "select p.id , p.name , p.imgurl from product p where p.id in("+proIds+") ";
			Query query = entityManager.createNativeQuery(sql);
			//query.setParameter(1, proIds);
			List<Object[]> result = query.getResultList();
			List<ProductIdAndNameVO> productSortVo = new ArrayList<ProductIdAndNameVO>();
			if(result.size() <= 0){
				return null;
			}
			for (Object[] obj : result) {
				Long productid = Long.valueOf(obj[0].toString());
				String name = obj[1] !=null ? obj[1].toString() : "";
				String imgUrl = obj[2] !=null ? obj[2].toString() : "";
				ProductIdAndNameVO productSort = new ProductIdAndNameVO(productid,name,imgUrl);
				productSortVo.add(productSort);
			}
			return productSortVo;
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.getProductByProIds() ", e);
		}
	}

	/**
	 * 获取进口食品列表
	 * @author LongXianZhen 2015/07/01 
	 *  
	 * alter lyj 2015-09-18   sql 搜索字段新增：ipro.cont  统计进口食品检测报告数量 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProductIdAndNameVO> getImportProductList(int page, int pageSize)
			throws DaoException {
		try {
			String sql = "SELECT ipro.pId,ipro.pName,ipro.pImgUrl,ipro.cont,ipro.country_name FROM ("+
					"SELECT p.id AS pId,p.`name` AS pName,p.imgurl AS pImgUrl,a.cont,c.name as country_name FROM imported_product ip "+
					"left join product p ON p.id=ip.product_id "+
					"LEFT JOIN (SELECT smp.product_id,count(rep.id) cont FROM product_instance smp "+
					"inner JOIN test_result rep ON smp.id=rep.sample_id AND rep.del = 0 and rep.publish_flag='1' AND rep.test_type  ='政府抽检'   group by product_id) a on p.id=a.product_id "+
					"inner join country c on c.id=ip.country_id "+
					 " WHERE ip.del=0 and country_id!=65) ipro  where ipro.pId<>'' ORDER BY ipro.cont DESC";	
			Query query = entityManager.createNativeQuery(sql);
			query.setFirstResult((page - 1)*pageSize);
			query.setMaxResults(pageSize);
			List<Object[]> result = query.getResultList();
			List<ProductIdAndNameVO> productSortVo = new ArrayList<ProductIdAndNameVO>();
			if(result==null||result.size() <= 0){
				return null;
			}
			for (Object[] obj : result) {
				Long productid = Long.valueOf(obj[0].toString());
				String name = obj[1] !=null ? obj[1].toString() : "";
				String imgUrl = obj[2] !=null ? obj[2].toString() : "";
				Long reportNum=obj[3] !=null ?Long.valueOf(obj[3].toString()): 0;
				String countryName=obj[4]!=null?obj[4].toString():"";
				ProductIdAndNameVO productSort = new ProductIdAndNameVO(productid,name,imgUrl,reportNum,countryName);
				productSortVo.add(productSort);
			}
			return productSortVo;
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.getImportProductList() ", e);
		}
	}

	/**
	 * 获取进口食品数量
	 * @author LongXianZhen 2015/07/01
	 */
	@Override
	public int getImportProductCount() throws DaoException {
		try {
			String sql = "SELECT count(*) FROM imported_product ip " +
					"LEFT JOIN country co ON co.id=ip.country_id LEFT JOIN " +
					"product p ON p.id=ip.product_id WHERE ip.del=0 AND co.name<>'中国' ";
			Query query = entityManager.createNativeQuery(sql);
			Object rtn = query.getSingleResult();
			return rtn == null ? 0 : Integer.parseInt(rtn.toString());
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.getImportProductCount() ", e);
		}
	}
	
	public int getImportProductReportCount() throws DaoException {
		try {
			String sql = "SELECT count(*) FROM imported_product ip "+
"inner JOIN product p ON p.id=ip.product_id and ip.del=0 "+
"INNER JOIN product_instance pi on p.id=pi.product_id "+
"inner join test_result t on pi.id=t.sample_id and test_type='政府抽检' WHERE ip.country_id<>65 AND t.publish_flag='1'";
			Query query = entityManager.createNativeQuery(sql);
			Object rtn = query.getSingleResult();
			return rtn == null ? 0 : Integer.parseInt(rtn.toString());
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.getImportProductCount() ", e);
		}
	}
	/**
	 * 通过Object 数组 封装一个 ProductVO
	 * @param obj
	 * @return
	 */
	private ProductVO createProductVOByObjectAry(Object[] obj){
		if(obj == null || obj.length < 1) {
			return null;
		}
		ProductVO productVO = new ProductVO();
		productVO.setId(obj[0] != null ? Long.parseLong(obj[0].toString()) : -1L);
		productVO.setIngredient(obj[1] != null ? obj[1].toString() : "");
		productVO.setCstm(obj[2] != null ? obj[2].toString() : "");
		productVO.setRiskIndex(obj[3] != null ? obj[3].toString() : "");
		String testDate = (obj[4] != null ? obj[4].toString() : "");
		testDate = (testDate.length() > 10 ? testDate.substring(0, 10) : testDate);
		productVO.setTestDate(testDate);
		String pass = (obj[5] != null ? obj[5].toString() : "");
		if("true".equals(pass)) {
			pass = "合格";
		} else if("false".equals(pass)) {
			pass = "不合格";
		}
		productVO.setResult(pass);
		/**
		 * 安全指数计算项目(可能存在重复项)，一下代码将做去重操作
		 */
		String allTpName = (obj[6] != null ? obj[6].toString() : "");
		String targetNmae = "";
		if(!"".equals(allTpName)) {
			//使用符号 | 分割，得到所有的计算项目
			String[] tpName = allTpName.split("\\|");
			for(String name : tpName){
				if(!targetNmae.contains(name)){
					targetNmae = targetNmae + name + "|";
				}
			}
			//去除末尾的|符号
			int len = targetNmae.length();
			if(len > 0 && "|".equals(targetNmae.substring(len - 1))){
				targetNmae = targetNmae.substring(0, len - 1);
			}
		}
		productVO.setTestPropertyName(targetNmae);
		productVO.setStatus(obj[7] != null ? obj[7].toString() : "");
		productVO.setTestResultId(obj[8] != null ? Long.parseLong(obj[8].toString()) : -1L);
		productVO.setBatchSerialNo(obj[9] != null ? obj[9].toString() : "");
		String proDate = (obj[10] != null ? obj[10].toString() : "");
		proDate = (proDate.length() > 10 ? proDate.substring(0, 10) : proDate);
		productVO.setProductionDate(proDate);
		productVO.setJudgeGist(obj[11] != null ? obj[11].toString() : "");
		return productVO;
	}
	
	/**
	 * 根据产品id获取 ProductVO 目前提供给爱特购系统使用
	 * @param id 产品id
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ProductVO findById_(Long id) throws DaoException {
		/**
		 * 根据产品id查询营养成分、适用人群、安全指数、最新报告检测日期、检验结论、 安全指数计算项目字段
		 */
		String sql = "SELECT pro.id,pro.ingredient,pro.cstm,pro.riskIndex,tp.test_date,tp.pass," +
				"pro.test_property_name ,pro.status,tp.rId,tp.batch_serial_no,tp.production_date,tp.standard FROM product pro LEFT JOIN " +
				"(SELECT pi.product_id,rep.test_date,rep.pass,rep.id AS rId,pi.batch_serial_no,pi.production_date,rep.standard FROM product_instance pi " +
				"LEFT JOIN test_result rep ON rep.sample_id = pi.id " +
				"WHERE rep.publish_flag = 1 AND pi.product_id = :productId ORDER BY pi.production_date DESC LIMIT 0,1) tp " +
				"ON tp.product_id = pro.id " +
				"WHERE pro.id = :productId";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("productId", id);
		List<Object[]> objs = query.getResultList();
		ProductVO productVO = null;
		if(objs != null && objs.size() > 0) {
			productVO = createProductVOByObjectAry(objs.get(0));
		}
		return productVO;
	}

	/**
	 * 根据产品barcode获取 ProductVO 目前提供给爱特购系统使用
	 * @param barcode 条形码
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ProductVO findByBarcode_(String barcode) throws DaoException {
		/**
		 * 根据产品id查询营养成分、适用人群、安全指数、最新报告检测日期、检验结论、 安全指数计算项目字段
		 */
		String sql = "SELECT pro.id,pro.ingredient,pro.cstm,pro.riskIndex,tp.test_date,tp.pass," +
				"pro.test_property_name,pro.status,tp.rId,tp.batch_serial_no,tp.production_date,tp.standard FROM product pro LEFT JOIN " +
				"(SELECT pi.product_id,rep.test_date,rep.pass,rep.id AS rId,pi.batch_serial_no,pi.production_date,rep.standard FROM product_instance pi " +
				"RIGHT JOIN product pr ON pi.product_id = pr.id AND pr.barcode = :barcode "+
				"LEFT JOIN test_result rep ON rep.sample_id = pi.id " +
				"WHERE rep.publish_flag = 1 ORDER BY pi.production_date DESC LIMIT 0,1) tp " +
				"ON tp.product_id = pro.id " +
				"WHERE pro.barcode = :barcode";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("barcode", barcode);
		List<Object[]> objs = query.getResultList();
		ProductVO productVO = null;
		if(objs != null && objs.size() > 0) {
			productVO = createProductVOByObjectAry(objs.get(0));
		}
		return productVO;
	}
	
	
	
	private String getSqlByStringList(List<String> list ,int type)
	{
		StringBuffer buff = new StringBuffer();
		buff.append("(");
		for( String barCode: list)
		{
			if(type == 2)buff.append("'");
			buff.append(barCode);
			if(type == 2) buff.append("'");
			buff.append(",");
		}
		int len = buff.length();
		buff.replace(len-1, len, ")");
		return buff.toString();
	}
	
	
	
	@Override
	public List<Object> findTestResultByBarcode(List<String> barcodes) throws DaoException {
		/**
		 * 根据产品id查询营养成分、适用人群、安全指数、最新报告检测日期、检验结论、 安全指数计算项目字段
		 */
		String sql = "SELECT DISTINCT p.barcode FROM product p"
                              +" RIGHT JOIN product_instance pi ON p.id = pi.product_id"
                              +" RIGHT JOIN test_result tr ON pi.id = tr.sample_id"
                              +" WHERE p.barcode IN "+getSqlByStringList(barcodes,2)+" AND tr.publish_flag = 1";
		Query query = entityManager.createNativeQuery(sql);
		@SuppressWarnings("unchecked")
		List <Object>ls  = query.getResultList();
		return ls;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> findTestResultByProduct(List<String> productIds) throws DaoException {
		/**
		 * 根据产品id查询营养成分、适用人群、安全指数、最新报告检测日期、检验结论、 安全指数计算项目字段
		 */
		String sql = "SELECT DISTINCT p.id FROM product p"
				                +" RIGHT JOIN product_instance pi ON p.id = pi.product_id"
				                +" RIGHT JOIN test_result tr ON pi.id = tr.sample_id"
				                +" WHERE p.id IN "+getSqlByStringList(productIds,1)+" AND tr.publish_flag = 1";
		Query query = entityManager.createNativeQuery(sql);
		List<Object> ls = query.getResultList();
		return ls;
	}

	/**
	 * 根据条形码查询产品部分信息
	 * @author LongXianZhen 2015/08/06
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ProductInfoVOToPortal getProductInfoByBarcode(String barcode)
			throws DaoException {
		try {
			String sql = "SELECT pro.id,pro.pName,pro.bName,pro.barcode,pro.format,pro.expiration_date,pro.imgurl FROM ( " +
					"SELECT p.id,p.`name` AS pName,bb.`name` AS bName,p.barcode,p.format,p.expiration_date,p.imgurl " +
					"FROM product p LEFT JOIN business_brand bb ON bb.id=p.business_brand_id " +
					"WHERE p.barcode=?1) AS pro";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, barcode);
			List<Object[]> objs = query.getResultList();
			ProductInfoVOToPortal productVO =null; new ProductInfoVOToPortal();
			if(objs != null && objs.size() > 0) {
				Object[] obj=objs.get(0);
				productVO=new ProductInfoVOToPortal();
				productVO.setId(obj[0] != null ? Long.parseLong(obj[0].toString()) : -1L);
				productVO.setName(obj[1] != null ? obj[1].toString() : "");
				productVO.setBusinessBrand(obj[2] != null ? obj[2].toString() : "");
				productVO.setBarcode(obj[3] != null ? obj[3].toString() : "");
				productVO.setFormat(obj[4] != null ? obj[4].toString() : "");
				productVO.setExpirationDate(obj[5] != null ? obj[5].toString() : "");
				productVO.setImgUrl(obj[6] != null ? obj[6].toString() : "");
			}
			return productVO;
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.getProductInfoByBarcode() ", e);
		}
	}

	/**
	 * 根据产品id查询产品基本信息
	 * @author longxianzhen 20150807
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Product findProductBasicById(long id) throws DaoException {
		try {
			String sql = "SELECT pro.id,pro.pName,pro.`status`,pro.format,pro.barcode," +
					"pro.bName,pro.buName,pro.imgurl,pro.des,pro.cstm,pro.ingredient," +
					"pro.characteristic,pro.expiration_date,pro.expiration,pro.organization," +
					"pro.nutri_label,pro.riskIndex,pro.risk_succeed,pro.test_property_name,pro.buId,pro.category_id," +
					"pro.provinceID,pro.cityID,pro.areaID,pro.is_agriculture_product,pro.product_certification FROM ( " +
					" SELECT p.id,p.`name` AS pName,p.`status`,p.format," +
					"p.barcode,bb.`name` AS bName,bu.`name` AS buName," +
					"p.imgurl,p.des,p.cstm,p.ingredient,p.characteristic," +
					"p.expiration_date,p.expiration,p.organization,p.nutri_label," +
					"p.riskIndex,p.risk_succeed,p.test_property_name,bu.id AS buId,p.category_id,hp.province provinceID,hc.city cityID,ha.area areaID,p.is_agriculture_product,p.product_certification FROM product p " +
					"LEFT JOIN business_brand bb ON bb.id=p.business_brand_id " +
					"LEFT JOIN business_unit bu ON bu.id=p.producer_id " +
					"LEFT JOIN hat_province hp on hp.provinceID=p.provinceID "+
					"LEFT JOIN hat_city hc on hc.cityID=p.cityID "+
					"LEFT JOIN hat_area ha on ha.areaID=p.areaID "+
					"WHERE p.id=?1 " +
					
					") AS pro";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, id);
			List<Object[]> objs = query.getResultList();
			Product productVO =null; 
			if(objs != null && objs.size() > 0) {
				Object[] obj=objs.get(0);
				productVO=new Product();
				productVO.setId(obj[0] != null ? Long.parseLong(obj[0].toString()) : -1L);
				productVO.setName(obj[1] != null ? obj[1].toString() : "");
				productVO.setStatus(obj[2] != null ? obj[2].toString() : "");
				productVO.setFormat(obj[3] != null ? obj[3].toString() : "");
				productVO.setBarcode(obj[4] != null ? obj[4].toString() : "");
				productVO.setBusinessBrand(new BusinessBrand(obj[5] != null ? obj[5].toString() : ""));
				productVO.setProducer(new BusinessUnit(obj[19] != null ? Long.parseLong(obj[19].toString()) : 0L,obj[6] != null ? obj[6].toString() : ""));
				productVO.setImgUrl(obj[7] != null ? obj[7].toString() : "");
				productVO.setDes(obj[8] != null ? obj[8].toString() : "");
				productVO.setCstm(obj[9] != null ? obj[9].toString() : "");
				productVO.setIngredient(obj[10] != null ? obj[10].toString() : "");
				productVO.setCharacteristic(obj[11] != null ? obj[11].toString() : "");
				productVO.setExpirationDate(obj[12] != null ? obj[12].toString() : "");
				productVO.setExpiration(obj[13] != null ? obj[13].toString() : "");
				productVO.setOrganization(obj[14] != null ? Long.parseLong(obj[14].toString()) : -1L);
				productVO.setNutriLabel(obj[15] != null ? obj[15].toString() : "");
				productVO.setRiskIndex(obj[16] != null ? (Double)obj[16] : null);
				productVO.setRisk_succeed(obj[17] != null ? (Boolean)obj[17] : false);
				productVO.setTestPropertyName(obj[18] != null ? obj[18].toString() : "");
				ProductCategoryInfo pc=new ProductCategoryInfo();
				pc.setId(obj[20] != null ? Long.parseLong(obj[20].toString()) : -1L);
				productVO.setCategory(pc);
				productVO.setPackageFlag('0');
				productVO.setNutriStatus('0');
				productVO.setProvinceID(obj[21]==null?"":obj[21].toString());
				productVO.setCityID(obj[22]==null?"":obj[22].toString());
				productVO.setAreaID(obj[23]==null?"":obj[23].toString());
				productVO.setAgricultureProduct((Boolean)obj[24]);
				productVO.setCertificationStatus(Integer.valueOf(obj[25].toString()));
			}
			return productVO;
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.getProductInfoByBarcode() ", e);
		}
	}

	/**
	 * 根据产品id集合查找产品ProductSortVo信息
	 * @param proIds
	 * @author lxz 2015/09/26
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProductSortVo> getProductSortVoByProIds(String proIds)throws DaoException {
		try {
			String sql = "select p.id , p.name , p.imgurl,p.barcode from product p where p.id in("+proIds+") ";
			Query query = entityManager.createNativeQuery(sql);
			List<Object[]> result = query.getResultList();
			List<ProductSortVo> productSortVo = new ArrayList<ProductSortVo>();
			if(result!=null&&result.size()>0){
				for (Object[] obj : result) {
					ProductSortVo psVo=new ProductSortVo();
					psVo.setId(Long.parseLong(obj[0]!=null?obj[0].toString():"-1L"));
					psVo.setName(obj[1] !=null ? obj[1].toString() : "");
					psVo.setImgUrl(obj[2] !=null ? obj[2].toString() : "");
					psVo.setBarcode(obj[3] !=null ? obj[3].toString() : "");
					productSortVo.add(psVo);
				}
			}
			return productSortVo;
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.getProductByProIds() ", e);
		}
	}
    
	/**
	 * 根据条形吗查询生产企业
	 * @param barcode
	 * @author wb 2015/11/13
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TestBusinessUnitVo> getBusinessUnitVOList(String barcode, Integer page,
			Integer pageSize) {
 String sql = "SELECT  bu.id,bu.NAME,bu.address,bu.contact,bu.telephone,bu.email,bu.postal_code,bu.license_no,bu.distribution_no,bu.administrativeLevel,";
        sql +="bu.region,bu.sampleLocal,bu.jg_lic_url,bu.jg_dis_url,bu.jg_qs_url,";
		sql +="p.id AS productId,p.name AS proName,p.other_name,p.status,p.format,p.format_pdf,p.regularity,p.barcode,p.qscore_censor,p.qscore_sample,";
		sql +="p.imgurl,p.ingredient,p.expiration_date,p.expiration,tr.id as testId,tr.fullPdfPath,ttr.resource_id,ttr.resource_name,ttr.url logoUrl,tttr.URL orgUrl,ttttr.URL taxUrl  ";
		
		sql +="FROM business_unit bu ";
		sql +="LEFT JOIN enterprise_registe er ON er.enterpritename = bu.name AND er.status = '审核通过' ";
		sql +="LEFT JOIN t_business_logo_to_resource tltr ON tltr.ENTERPRISE_REGISTE_ID = er.id ";
		sql +="LEFT JOIN t_test_resource ttr ON ttr.RESOURCE_ID = tltr.RESOURCE_ID  ";
		sql +="LEFT JOIN product_instance ip ON ip.producer_id = bu.id ";
		sql +="LEFT JOIN product p ON ip.product_id = p.id ";
		sql +="LEFT JOIN test_result tr ON ip.id = tr.sample_id ";
		sql +="LEFT JOIN t_org_license_to_resource toltr ON toltr.ENTERPRISE_REGISTE_ID=er.id  ";
		sql +="LEFT JOIN t_test_resource tttr  ON tttr.RESOURCE_ID = toltr.RESOURCE_ID  ";
		sql +="LEFT JOIN t_tax_register_cert_to_resource tax ON tax.ENTERPRISE_REGISTE_ID=er.id  ";
		sql +="LEFT JOIN t_test_resource ttttr  ON ttttr.RESOURCE_ID = toltr.RESOURCE_ID  ";
		sql +="WHERE bu.TYPE = ?1 AND p.barcode = ?2 GROUP BY bu.id ORDER BY tr.last_modify_time  DESC ";
			   
			   Query query = entityManager.createNativeQuery(sql);
			   query.setParameter(1, "生产企业");
			   query.setParameter(2, barcode);
			   if(page!=null&&page>0&&pageSize!=null&&pageSize>0){
				   query.setFirstResult((page-1)*pageSize);
				   query.setMaxResults(pageSize);
			   }
	   List<TestBusinessUnitVo> busvoList = new ArrayList<TestBusinessUnitVo>();
	   List<Object[]> objs = query.getResultList();
	   TestBusinessUnitVo  vo = null;
	   for (Object[] obj : objs) {
		   vo = new TestBusinessUnitVo();
		   vo.setId(obj[0]==null?null:Long.parseLong(obj[0].toString()));
		   vo.setName(obj[1]==null?"":obj[1].toString());
		   vo.setAddress(obj[2]==null?"":obj[2].toString());
		   vo.setContact(obj[3]==null?"":obj[3].toString());
		   vo.setContact_phone(obj[4]==null?"":obj[4].toString());
		   vo.setEmail(obj[5]==null?"":obj[5].toString());
		   vo.setPostalCode(obj[6]==null?"":obj[6].toString());
		   vo.setLicense_no(obj[7]==null?"":obj[7].toString());
		   vo.setDistribution_no(obj[8]==null?"":obj[8].toString());
		   vo.setAdministrativeLevel(obj[9]==null?"":obj[9].toString());
		   vo.setRegion(obj[10]==null?"":obj[10].toString());
		   vo.setSampleLocal(obj[11]==null?"":obj[11].toString());
		   vo.setJgLicUrl(obj[12]==null?"":obj[12].toString());
		   vo.setJgDisUrl(obj[13]==null?"":obj[13].toString());
		   vo.setJgQsUrl(obj[14]==null?"":obj[14].toString());
		   vo.setProductId(obj[15]==null?null:Long.parseLong(obj[15].toString()));
		   vo.setProName(obj[16]==null?"":obj[16].toString());
		   vo.setOther_name(obj[17]==null?"":obj[17].toString());
		   vo.setStatus(obj[18]==null?"":obj[18].toString());
		   vo.setPformat(obj[19]==null?"":obj[19].toString());
		   vo.setPformat_pdf(obj[20]==null?"":obj[20].toString());
		   vo.setPregularity(obj[21]==null?"":obj[21].toString());
		   vo.setPbarcode(obj[22]==null?"":obj[22].toString());
		   vo.setPqscore_censor(obj[23]==null?"":obj[23].toString());
		   vo.setPqscore_sample(obj[24]==null?"":obj[24].toString());
		   vo.setPimgurl(obj[25]==null?"":obj[25].toString());
		   vo.setPingredient(obj[26]==null?"":obj[26].toString());
		   vo.setPexpiration_date(obj[27]==null?"":obj[27].toString());
		   vo.setPexpiration(obj[28]==null?"":obj[28].toString());
		   vo.setTestId(obj[29]==null?null:Long.parseLong(obj[29].toString()));
		   vo.setFullPdfPath(obj[30]==null?"":obj[30].toString());
		   vo.setResourceId(obj[31]==null?null:Long.parseLong(obj[31].toString()));
		   vo.setImgName(obj[32]==null?"":obj[32].toString());
		   vo.setImgLogoUrl(obj[33]==null?"":obj[33].toString());
		   vo.setImgOrgUrl(obj[34]==null?"":obj[34].toString());
		   vo.setImgTaxUrl(obj[35]==null?"":obj[35].toString());
		   busvoList.add(vo);
	   }
		return busvoList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public TestBusinessUnitVo getBusinessUnitVO(String barcode, long id) {
		String sql = "SELECT  bu.id,bu.NAME,bu.address,bu.contact,bu.telephone,bu.email,bu.postal_code,bu.license_no,bu.distribution_no,bu.administrativeLevel,";
        sql +="bu.region,bu.sampleLocal,bu.jg_lic_url,bu.jg_dis_url,bu.jg_qs_url,";
		sql +="p.id AS productId,p.name AS proName,p.other_name,p.status,p.format,p.format_pdf,p.regularity,p.barcode,p.qscore_censor,p.qscore_sample,";
		sql +="p.imgurl,p.ingredient,p.expiration_date,p.expiration,tr.id as testId,tr.fullPdfPath,ttr.resource_id,ttr.resource_name,ttr.url logoUrl,tttr.URL orgUrl,ttttr.URL taxUrl  ";
		sql +="FROM business_unit bu ";
		sql +="LEFT JOIN enterprise_registe er ON er.enterpritename = bu.name AND er.status = '审核通过' ";
		sql +="LEFT JOIN t_business_logo_to_resource tltr ON tltr.ENTERPRISE_REGISTE_ID = er.id ";
		sql +="LEFT JOIN t_test_resource ttr ON ttr.RESOURCE_ID = tltr.RESOURCE_ID  ";
		sql +="LEFT JOIN t_org_license_to_resource toltr ON toltr.ENTERPRISE_REGISTE_ID=er.id  ";
		sql +="LEFT JOIN t_test_resource tttr  ON tttr.RESOURCE_ID = toltr.RESOURCE_ID  ";
		sql +="LEFT JOIN t_tax_register_cert_to_resource tax ON tax.ENTERPRISE_REGISTE_ID=er.id  ";
		sql +="LEFT JOIN t_test_resource ttttr  ON ttttr.RESOURCE_ID = toltr.RESOURCE_ID  ";
		sql +="LEFT JOIN product_instance ip ON ip.producer_id = bu.id ";
		sql +="LEFT JOIN product p ON ip.product_id = p.id ";
		sql +="LEFT JOIN test_result tr ON ip.id = tr.sample_id ";
		
		sql +="WHERE bu.id=?1  AND bu.TYPE = ?2 AND p.barcode = ?3 GROUP BY bu.id ORDER BY tr.last_modify_time  DESC";
		   Query query = entityManager.createNativeQuery(sql);
		   query.setParameter(1, id);
		   query.setParameter(2, "生产企业");
		   query.setParameter(3, barcode);
		List<Object[]> objs = query.getResultList();
		TestBusinessUnitVo vo = null;
		for (Object[] obj : objs) {
			 vo = new TestBusinessUnitVo();
			   vo.setId(obj[0]==null?null:Long.parseLong(obj[0].toString()));
			   vo.setName(obj[1]==null?"":obj[1].toString());
			   vo.setAddress(obj[2]==null?"":obj[2].toString());
			   vo.setContact(obj[3]==null?"":obj[3].toString());
			   vo.setContact_phone(obj[4]==null?"":obj[4].toString());
			   vo.setEmail(obj[5]==null?"":obj[5].toString());
			   vo.setPostalCode(obj[6]==null?"":obj[6].toString());
			   vo.setLicense_no(obj[7]==null?"":obj[7].toString());
			   vo.setDistribution_no(obj[8]==null?"":obj[8].toString());
			   vo.setAdministrativeLevel(obj[9]==null?"":obj[9].toString());
			   vo.setRegion(obj[10]==null?"":obj[10].toString());
			   vo.setSampleLocal(obj[11]==null?"":obj[11].toString());
			   vo.setJgLicUrl(obj[12]==null?"":obj[12].toString());
			   vo.setJgDisUrl(obj[13]==null?"":obj[13].toString());
			   vo.setJgQsUrl(obj[14]==null?"":obj[14].toString());
			   vo.setProductId(obj[15]==null?null:Long.parseLong(obj[15].toString()));
			   vo.setProName(obj[16]==null?"":obj[16].toString());
			   vo.setOther_name(obj[17]==null?"":obj[17].toString());
			   vo.setStatus(obj[18]==null?"":obj[18].toString());
			   vo.setPformat(obj[19]==null?"":obj[19].toString());
			   vo.setPformat_pdf(obj[20]==null?"":obj[20].toString());
			   vo.setPregularity(obj[21]==null?"":obj[21].toString());
			   vo.setPbarcode(obj[22]==null?"":obj[22].toString());
			   vo.setPqscore_censor(obj[23]==null?"":obj[23].toString());
			   vo.setPqscore_sample(obj[24]==null?"":obj[24].toString());
			   vo.setPimgurl(obj[25]==null?"":obj[25].toString());
			   vo.setPingredient(obj[26]==null?"":obj[26].toString());
			   vo.setPexpiration_date(obj[27]==null?"":obj[27].toString());
			   vo.setPexpiration(obj[28]==null?"":obj[28].toString());
			   vo.setTestId(obj[29]==null?null:Long.parseLong(obj[29].toString()));
			   vo.setFullPdfPath(obj[30]==null?"":obj[30].toString());
			   vo.setResourceId(obj[31]==null?null:Long.parseLong(obj[31].toString()));
			   vo.setImgName(obj[32]==null?"":obj[32].toString());
			   vo.setImgLogoUrl(obj[33]==null?"":obj[33].toString());
			   vo.setImgOrgUrl(obj[34]==null?"":obj[34].toString());
			   vo.setImgTaxUrl(obj[35]==null?"":obj[35].toString());
			   break;
		}
	return vo;
	}
	
	
	
	
	/**
	 * 功能描述：根据二维码id获取产品条形码
	 * @throws DaoException
	 * @author liuyuanjing 2015/12/28
	 */
	@Override
	public String findProductBarcodeByQRcode(String QRcode) throws DaoException {
		try {
			String sql = "SELECT pb.product_barcode from product_barcode_to_qrcode pb WHERE pb.start_num<=:QRcode and pb.end_num>=:QRcode";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter("QRcode", QRcode);
	    	return query.getSingleResult().toString();
		} catch (Exception e) {
			return "";
		}
	}
	

	/**
	 * 功能描述：根据二维码id获取产品条形码
	 * @throws DaoException
	 * @author liuyuanjing 2015/12/28
	 */
	@Override
	public String findProductIdByQRcode(String QRcode) throws DaoException {
		try {
			String sql = "SELECT pb.product_id from product_barcode_to_qrcode pb WHERE pb.start_num<=:QRcode and pb.end_num>=:QRcode";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter("QRcode", QRcode);
	    	return query.getSingleResult().toString();
		} catch (Exception e) {
			return "";
		}
	}
	
	
}