package com.lhfs.fsn.dao.search.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.business.BusinessBrand;
import com.gettec.fsnip.fsn.model.product.ProductCategory;
import com.gettec.fsnip.fsn.model.product.SearchResult;
import com.gettec.fsnip.fsn.util.NutritionUtil;
import com.lhfs.fsn.dao.search.SearchDao;
import com.lhfs.fsn.vo.product.BrandVO;
import com.lhfs.fsn.vo.product.CategoryVO;
import com.lhfs.fsn.vo.product.ProductCategoryVO;

@Repository
public class SearchDaoImpl extends BaseDAOImpl<SearchResult> implements SearchDao{

	/**
	 * portal搜索接口
     * @updated TangXin 2015/03/31
	 */
	@SuppressWarnings("unchecked")
	public  List<Object[]> getResultList(String keyword, String category,Integer catLevel,String brand, String[] featureList,
			String ordername, String ordertype, int start, int pageSize, String nutriLabel) {
		String featureSql = getSqlByFeature(featureList);
		brand = ((!brand.equals(""))&&(brand!=null))?brand:("%"+brand+"%");
		String condition = getConditionByTestTypeOrder(ordername,ordertype);
        List<Object[]> result;
        String proCatCondition = ""; // 根据产品分类级拼查询条件
        String nutriLabelCondition = "";
        if(nutriLabel != null && !"".equals(nutriLabel)){
        	nutriLabelCondition = " and product.nutri_label like '%"+nutriLabel+"%' ";
        }
        /*  当产品分类是第三级时 查询条件是三级分类的id */
        if(catLevel != null && catLevel == 3) {
            proCatCondition = " AND product.category_id = ?6 ";
        }else {
            /* 分类属于一二级时 查询条件是 code */
            
            /* 1.如果是手机传来的三级分类 */
            if (!"".equals(category)&& category != null && category.length()==6) {
                String sql = "select product.id,product.name,product.imgurl,product.cstm,product.qscore_self,product.qscore_censor,product.qscore_sample," +
                		"product.risk_succeed,product.riskIndex,product.test_property_name,product.nutri_label from product "+ condition +
                        " where ((product.name like ?1 or product.barcode = ?2 or " +
                        "product.business_brand_id in ( select distinct business_brand.id from business_brand where " +
                        "business_brand.name like ?3 ) or product.organization IN " +
                        "(SELECT DISTINCT organization FROM business_unit where name like ?4))  or " +
                        "product.id IN(SELECT DISTINCT samp.product_id FROM business_unit bus LEFT JOIN product_instance samp " +
                        "ON bus.id=samp.producer_id WHERE bus.`name` LIKE ?5 AND samp.product_id is NOT NULL)) " +
                        "and product.category_id in (SELECT DISTINCT pci.id FROM product_category_info pci RIGHT JOIN " +
                        "(SELECT id,`name` FROM product_category WHERE `code` LIKE ?6 AND LENGTH(`code`)=6) ctgy " +
                        "ON  pci.`display`=ctgy.`name`) AND product.business_brand_id in( select distinct business_brand.id from business_brand where "+
                        "business_brand.name like ?7) " + nutriLabelCondition;
                result = entityManager.createNativeQuery(sql)
                        .setParameter(1, "%"+keyword+"%")
                        .setParameter(2, keyword)
                        .setParameter(3, "%"+keyword+"%")
                        .setParameter(4, "%"+keyword+"%")
                        .setParameter(5, "%"+keyword+"%")
                        .setParameter(6, category+"%")
                        .setParameter(7, brand)
                        .getResultList();
                return result;
            } else {
                /* 2 一二级分类的情况 */
                proCatCondition = " AND product.category like ?6 ";
                }
            }
        if(!ordertype.equals("")){
            /* 根据排序规则获取condition */
            String ordby = "";
            if(!condition.equals("")){
            	String tmpCond = " tmp.cont ";
            	if("风险指数".equals(ordername)){
            		tmpCond = " product.riskIndex ";
            	}
            	ordby = " ORDER BY " + tmpCond + ordertype;
            } 
            String sql = "select product.id,product.name,product.imgurl,product.cstm,product.qscore_self,product.qscore_censor,product.qscore_sample," +
            		"product.risk_succeed,product.riskIndex,product.test_property_name,product.nutri_label from product "+
            		condition+" where ((product.name like ?1 or product.barcode = ?2 or " +
                    "product.business_brand_id in ( select distinct business_brand.id from business_brand where business_brand.name like ?3 )) or " +
                    "product.id IN(SELECT DISTINCT samp.product_id FROM business_unit bus LEFT JOIN product_instance samp " +
                    "ON bus.id=samp.producer_id WHERE bus.`name` LIKE ?4 AND samp.product_id is NOT NULL)" +
                    "OR product.organization IN(SELECT DISTINCT organization FROM business_unit where name like ?5)) " + proCatCondition + 
                    " AND product.business_brand_id in( select distinct business_brand.id from business_brand where " +
                    "business_brand.name like ?7) " + nutriLabelCondition +
                    featureSql+(ordby.equals("")?" order by " + ordername + " DESC ":ordby);
            result = entityManager.createNativeQuery(sql)
                            .setParameter(1, "%"+keyword+"%")
                            .setParameter(2, keyword)
                            .setParameter(3, "%"+keyword+"%")
                            .setParameter(4, "%"+keyword+"%")
                            .setParameter(5, "%"+keyword+"%")
                            .setParameter(6, (catLevel != null && catLevel == 3)? Long.valueOf(category) : category+"%") 
                            .setParameter(7, brand)
                            .setFirstResult(start)
                            .setMaxResults(pageSize)
                            .getResultList();
            return result;
        }
		return null;
	}

	/**
	 * portal 综合排序搜索接口，查询没有报告的商品信息
     * @updated TangXin 2015/03/31
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getResultListByZH(String ordername, String ordertype, int start, int pageSize) {
		if(!ordertype.equals("")){
		    List<Object[]> result;
			String orderByCond = " ORDER BY tmp.cont " + ordertype;
			String orderByType = "";
			String condition = "";
			if("风险指数".equals(ordername)) {
				orderByCond = " ORDER BY pro.riskIndex " +ordertype;
				condition = " AND pro.risk_succeed = 1 ";
			} else if(!"综合".equals(ordername)) {
				orderByType = " AND rep.test_type = '" + ordername + "' ";
			}
			
		   String sql="SELECT pro.id,pro.name,pro.imgurl,pro.cstm,pro.qscore_self,pro.qscore_censor,pro.qscore_sample,pro.risk_succeed," +
		    		"pro.riskIndex,pro.test_property_name,pro.nutri_label,tmp.cont FROM product pro " +
		    		"LEFT JOIN (SELECT smp.product_id,count(rep.id) cont FROM product_instance smp " +
		    		"LEFT JOIN test_result rep ON smp.id=rep.sample_id AND rep.del = 0 " +
		    		"WHERE rep.publish_flag='1' AND rep.test_type is not null  and rep.test_type!='第三方检测'   "  + orderByType +
                    "GROUP BY smp.product_id) tmp ON pro.id=tmp.product_id   WHERE pro.organization  IS NOT NULL  " + condition + orderByCond;
            result = entityManager.createNativeQuery(sql)
                            .setFirstResult(start)
                            .setMaxResults(pageSize)
                            .getResultList();
            return result;
        }
		return null;
	}
	
	/**
	 * 根据报告检测类别，返回排序语句
	 * @param testType
	 * @param ordertype 排序
	 * @updated TangXin 2015/03/31
	 */
	private String getConditionByTestTypeOrder(String testType,String ordertype){
		String condition = "";
		String testTyepCondition = "";
		if(testType==null || "风险指数".equals(testType)) {
			 return condition;
		}
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
     * 筛选产品分类
     * @updated TangXin 2015/03/31
     */
	@SuppressWarnings("unchecked")
	public List<CategoryVO> getCategoryList(String keyword, String category,Integer catLevel, String brand) {
		brand = (!brand.equals(""))&&(brand!=null)?("%"+brand+"%"):("%"+keyword+"%");
		List<Object[]> result = null;
		/* 处理三级分类 */
        if(catLevel != null && catLevel == 3) {
            String sql =  "SELECT info.id,info.name,info.display FROM product_category_info info " + 
                    "LEFT JOIN  product_category pc ON info.category_id = pc.id WHERE pc.code = ?1 " + 
                    " GROUP BY info.id ";  
            result = entityManager.createNativeQuery(sql).setParameter(1, category).getResultList();
        }else {
            /* 用第二级分类加载出下面的所有第三级时 */
            if (category != null && category.length() == 4) {
                String sql = " select pcinfo.id,pcinfo.name,pcinfo.display from product_category_info pcinfo " +
                        "RIGHT JOIN product_category pc ON pcinfo.category_id = pc.id" + 
                        " where pc.code = ?1 AND pcinfo.category_flag =1 "; 
                catLevel = 3;
                result = entityManager.createNativeQuery(sql).setParameter(1, category).getResultList();
            } else {
                /* 加载一、二 级分类的情况 */
                String sql = "SELECT  pc.id,pc.name,pc.display,pc.code FROM product_category pc " + 
                                "LEFT JOIN (SELECT category AS code FROM product pt where (pt.name LIKE ?1 OR " + 
                                "pt.barcode = ?2 OR pt.business_brand_id IN " +
                                "(SELECT business_brand.id FROM business_brand WHERE " + 
                                "business_brand.name LIKE ?3 OR business_brand.name LIKE ?4)) " +
                                "GROUP BY pt.id) pcate ON pc.code = pcate.code " + 
                            "WHERE LENGTH(pc.code)= LENGTH(?5)+2 AND pc.code LIKE ?6 GROUP BY pc.code ";
                result = entityManager.createNativeQuery(sql)
                            .setParameter(1,"%"+keyword+"%")
                            .setParameter(2,keyword)
                            .setParameter(3,"%"+keyword+"%")
                            .setParameter(4,"%"+keyword+"%")
                            .setParameter(5, category)
                            .setParameter(6, category+"%")
                            .getResultList();
            }  
        }
        return (result != null && result.size() > 0) ? potteCategory(result,catLevel):null;
	}
	
	/**
     * 用于封装category中的id，name，code，display
     * @param objs 
     * @param catLeve 类别的等级
     * @return List<CategoryVO>
     */
    private List<CategoryVO> potteCategory(List<Object[]> objs, Integer catLevel){
        List<CategoryVO> categoryVOs = null;
        if (objs != null && objs.size() > 0) {
            /* 第三级分类的情况 */
            if (catLevel != null && catLevel == 3) {
                String code = "";
                categoryVOs = new ArrayList<CategoryVO>();
                for (int i = 0; i < objs.size(); i++) {
                    Object[] obj = (Object[]) objs.get(i);
                    Long id = obj[0] != null ? Long.valueOf(obj[0].toString()) : -1;
                    String name = obj[1] != null ? obj[1].toString() : "";
                    String display = obj[2] != null ? obj[2].toString() : "";
                    categoryVOs.add(new CategoryVO(id, code, name, display));
                }
            }else { /* 一、二 级分类的情况 */
                categoryVOs = new ArrayList<CategoryVO>();
                for (int i = 0; i < objs.size(); i++) {
                    Object[] obj = (Object[]) objs.get(i);
                    Long id = obj[0] != null ? Long.valueOf(obj[0].toString()) : -1;
                    String name = obj[1] != null ? obj[1].toString() : "";
                    String display = obj[2] != null ? obj[2].toString() : "";
                    String code = obj[3] != null ? obj[3].toString() : "";
                    categoryVOs.add(new CategoryVO(id, code, name, display));
                }
            }
        } 
        return categoryVOs;
    }
    
	/**
	 * 根据企业名称和商标加载产品类别
	 */
	@SuppressWarnings("unchecked")
	public List<CategoryVO> getCategoryListByBusName(String brand,String enterpriseName,String category,Integer catLevel) {
	    List<Object[]> result =null;
	    String sql  = ""; 
	    String condition = ""; 
	    if(!"".equals(brand)) { //有商标的情况
            condition = "LEFT JOIN business_brand brand ON pro.business_brand_id=brand.id " + 
                        " where brand.`name`= ?3 AND ";
        }else{ //没有商标的情况
            condition = " WHERE '' = ?3 AND ";
        }
	    
	    /* 判断是否是三级菜单的情况 */
	    if(catLevel != null && catLevel==3) { //是 3级菜单
	        sql = "SELECT id,name,display,'' AS `code` from product_category_info where category_id IN "+
                    "(SELECT DISTINCT pro.category_id FROM product pro "+
                    "LEFT JOIN product_instance sam ON pro.id=sam.product_id "+
                    "RIGHT JOIN business_unit bus ON sam.producer_id=bus.id "+
                     condition +" bus.`name`= ?1 and pro.category_id = ?2 ) ";
	    }else { //不是三级菜单
	        sql="SELECT id,name,display,`code` from product_category where `code` IN" +
	                "(SELECT DISTINCT pro.category FROM product pro " +
	                "LEFT JOIN product_instance sam ON pro.id=sam.product_id " +
	                "LEFT JOIN business_unit bus ON sam.producer_id=bus.id " +
	                condition +" bus.`name`= ?1 and pro.category LIKE ?2 )";
        }
	    result=entityManager.createNativeQuery(sql)
                .setParameter(1,enterpriseName)
                .setParameter(2,(catLevel != null && catLevel==3)?Integer.parseInt(category):category+"%")
                .setParameter(3,brand)
                .getResultList();
        return (result != null && result.size() > 0) ? potteCategory(result,1) : null;
	}
    /**
     *搜索接口获取产品商标
     *@updated TangXin 2015/03/31
     */
	@SuppressWarnings("unchecked")
	public List<BrandVO> getBrandList(String keyword, String category,Integer catLevel,
			String brand, String[] featureList, String nutriLabel) {
	    brand = (brand==null)? "%%":("%"+brand+"%");
		String featureSql = getSqlByFeature(featureList);
		if(catLevel!= null && catLevel == 3) {
		    featureSql = " AND product.category_id = ?4 "+ featureSql;
		}else {
		    featureSql = " and product.category like ?4 " + featureSql;
		}
		String nutriLabelCondi = "";
		if(nutriLabel != null && !"".equals(nutriLabel)){
			nutriLabelCondi = " and product.nutri_label like '%" + nutriLabel + "%' ";
		}
		String sql = " select business_brand.id,business_brand.name from business_brand where "+
                " business_brand.id in (select distinct business_brand_id from product where (( "+
                " product.name like ?1 or  product.barcode = ?2 or product.business_brand_id in " +
                "(select distinct business_brand.id from business_brand where "+
                " business_brand.name like ?3)) " + featureSql + " ) " + nutriLabelCondi + " ) " +
                "and business_brand.name like ?5 order by business_brand.id ";
		List<Object[]> result = entityManager.createNativeQuery(sql)
                					.setParameter(1,"%"+keyword+"%")
                					.setParameter(2,keyword)
                					.setParameter(3,"%"+keyword+"%")
                					.setParameter(4,(catLevel!= null && catLevel == 3) ? Long.parseLong(category) : category+"%")
                					.setParameter(5,brand)
                					.getResultList();
		
	
		return (result !=null && result.size() > 0 ) ? structureBrandVO(result):null;
	}
	
	/**
	 * 大众门户搜索接口（v2.1.5），根据筛选条件获取品牌和分类信息
	 * @author tangxin 2015/04/01
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Map<String,Object> getListBrandAndCategoryByCondition(String keyword, String category,Integer catLevel,
			String brand,String enterpriseName, String nutriLabel){
		List<Object[]> listSearchResult = null;
		List<BrandVO> brandVOList = null;
		List<CategoryVO> secondCategory = null;
		List<CategoryVO> thirdCategory = null;
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			String condition = "";
			Map<String, Object> conMap = getSearchCondition(category, catLevel, enterpriseName, brand, nutriLabel);
			if(conMap.get("condition") != null) {
				condition = conMap.get("condition").toString();
			}
			String sql = " SELECT te.bid,te.bname,te.pcid,te.pcname,te.pccode,te.pcdispaly,te.pciid,te.pciname,'',te.pcidispaly,te.organization FROM " +
					"(SELECT DISTINCT brand.id as bid,brand.name as bname,pci.category_id as pcid,pc.name as pcname,pc.code as pccode,pc.display as pcdispaly," +
					"pci.id as pciid,pci.name as pciname,pci.display as pcidispaly,brand.organization as organization FROM product " + 
					"LEFT JOIN business_unit bus ON product.organization = bus.organization  " +
					"LEFT JOIN business_brand brand ON product.business_brand_id = brand.id " +
					"LEFT JOIN product_category_info pci ON product.category_id = pci.id " +
					"LEFT JOIN product_category pc ON pc.id = pci.category_id " +
					"WHERE (product.`name` LIKE ?1 OR product.barcode = ?2 "+//OR bus.`name` LIKE ?3 " +
					"OR brand.`name` LIKE ?4 OR pci.`name` LIKE ?5 ) " + condition + ") te";
			
			Query query = entityManager.createNativeQuery(sql)
                    .setParameter(1, "%"+keyword+"%")
                    .setParameter(2, keyword)
                    //.setParameter(3, "%"+keyword+"%")
                    .setParameter(4, "%"+keyword+"%")
                    .setParameter(5, "%"+keyword+"%");
			setQueryParamsValue(query, (Map<String, String>) conMap.get("params"));
			listSearchResult = query.getResultList();
			brandVOList = getStructureBrandVO(listSearchResult);
			map.put("brandVOList", brandVOList);
			secondCategory = createCategory(listSearchResult,2);
			map.put("secondCategory", secondCategory);
			thirdCategory = createCategory(listSearchResult,6);
			map.put("thirdCategory", thirdCategory);
		}catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 大众门户搜索接口（v2.1.5），根据筛选条件获取一级分类信息
	 * @author tangxin 2015/04/01
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<CategoryVO> getListFirstCategoryByCondition(String keyword, String category,Integer catLevel,
			String brand,String enterpriseName, String nutriLabel){
		List<Object[]> listSearchResult = null;
		List<CategoryVO> firstCategory = null;
		try{
			String condition = "";
			Map<String, Object> conMap = getSearchCondition(category, catLevel, enterpriseName, brand, nutriLabel);
			if(conMap.get("condition") != null) {
				condition = conMap.get("condition").toString();
			}
			condition += " AND pc.id is not null ";
			String sql = "SELECT ppc.id,ppc.name,ppc.code,ppc.display FROM product_category ppc RIGHT JOIN" +
					"(SELECT DISTINCT pc.code FROM product " + 
					"LEFT JOIN business_unit bus ON product.organization = bus.organization " +
					"LEFT JOIN business_brand brand ON product.business_brand_id = brand.id " +
					"LEFT JOIN product_category_info pci ON product.category_id = pci.id " +
					"LEFT JOIN product_category pc ON pc.id = pci.category_id " +
					"WHERE (product.`name` LIKE ?1 OR product.barcode = ?2 "+//OR bus.`name` LIKE ?3 " +
					"OR brand.`name` LIKE ?4 OR pci.`name` LIKE ?5) " + condition +") tmp ON ppc.`code` = SUBSTR(tmp.`code`,1,2)";
			Query query = entityManager.createNativeQuery(sql)
                    .setParameter(1, "%"+keyword+"%")
                    .setParameter(2, keyword)
                    //.setParameter(3, "%"+keyword+"%")
                    .setParameter(4, "%"+keyword+"%")
                    .setParameter(5, "%"+keyword+"%");
			setQueryParamsValue(query, (Map<String, String>) conMap.get("params"));
			listSearchResult = query.getResultList();
			firstCategory = createCategory(listSearchResult,0);
		}catch(Exception e){
			e.printStackTrace();
		}
		return firstCategory;
	}
	
	/* 将result封装到BrandVO中 */
	private List<BrandVO> structureBrandVO(List<Object[]> result){
        List<BrandVO> listBrandVO = new ArrayList<BrandVO>();
        List<Long> listIds = new ArrayList<Long>();
       if(result!=null&&result.size()>0){
           for(int i=0;i<result.size();i++){
               Object[] objs = (Object[]) result.get(i);
               String idStr = (objs[0] == null || objs[0].toString() == null || "".equals(objs[0].toString())) ? "-1" : objs[0].toString();
               Long id = Long.parseLong(idStr);
               if(id == -1L || listIds.contains(id)){
            	   continue;
               }
               String name = objs[1].toString() !=null ? objs[1].toString() : "";
               BrandVO brandVO = new BrandVO(id,name);
               listBrandVO.add(brandVO);
               listIds.add(id);
           }
       }
       return listBrandVO;
	}
	/* 将result封装到BrandVO中 */
	private List<BrandVO> getStructureBrandVO(List<Object[]> result){
        List<BrandVO> listBrandVO = new ArrayList<BrandVO>();
        List<Long> listIds = new ArrayList<Long>();
       if(result!=null&&result.size()>0){
           for(int i=0;i<result.size();i++){
               Object[] objs = (Object[]) result.get(i);
               int organization = Integer.valueOf(objs[10]!=null ? objs[10].toString() : "0");
               if(organization!=1){
            	   String idStr = (objs[0] == null || objs[0].toString() == null || "".equals(objs[0].toString())) ? "-1" : objs[0].toString();
            	   Long id = Long.parseLong(idStr);
            	   if(id == -1L || listIds.contains(id)){
            		   continue;
            	   }
            	   
            	   String name = objs[1]!=null ? objs[1].toString() : "";
            	   BrandVO brandVO = new BrandVO(id,name);
            	   listBrandVO.add(brandVO);
            	   listIds.add(id);
               }
           }
       }
       return listBrandVO;
	}
	
	/* 将result封装到ProductCategory中 */
	private List<CategoryVO> createCategory(List<Object[]> result, int index){
        List<CategoryVO> listCategort = new ArrayList<CategoryVO>();
        List<Long> listIds = new ArrayList<Long>();
       if(result!=null&&result.size()>0){
           for(int i=0;i<result.size();i++){
               Object[] objs = (Object[]) result.get(i);
               String idStr = (objs[index] == null || objs[index].toString() == null || "".equals(objs[index].toString())) ? "-1" : objs[index].toString();
               Long id = Long.parseLong(idStr);
               if(id ==-1L || listIds.contains(id)){
            	   continue;
               }
               String name = objs[index+1] !=null ? objs[index+1].toString() : "";
               String code  = objs[index+2] !=null ? objs[index+2].toString() : "";
               String display = objs[index+3] !=null ? objs[index+3].toString() : "";
               CategoryVO pc = new CategoryVO(id,code,name,display);
               listCategort.add(pc);
               listIds.add(id);
           }
       }
       return listCategort;
	}
	
	/**
	 * 根据企业名称加载商标
	 * @param enterpriseName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BusinessBrand> getBrandListByBusName(String enterpriseName) {
		try{
			String sql="SELECT * FROM business_brand where business_unit_id= " +
					"(SELECT id FROM business_unit where `name`= ?1)";
			List<BusinessBrand> result =entityManager.createNativeQuery(sql, BusinessBrand.class)
					.setParameter(1, enterpriseName)
					.getResultList();
			return result;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 大众门户产品搜索接口,根据分类的code获取导航分类信息
	 * @updater tangxin 2015/04/01
	 */
	@SuppressWarnings("unchecked")
	public List<CategoryVO> getNavigation(String category, String root) {
		List<ProductCategory> productCategoryList = entityManager
				.createNativeQuery("select e.* from product_category e where ("+
								"IF(LENGTH(?1)=6," +
								"(e.code like SUBSTRING(?2,1,2) or e.code like SUBSTRING(?3,1,4) or e.code like SUBSTRING(?4,1,6)),"+
								"IF(LENGTH(?5)=4,(e.code like SUBSTRING(?6,1,2) or e.code like SUBSTRING(?7,1,4)),"+
								"IF(LENGTH(?8)=2, (e.code like SUBSTRING(?9,1,2)),false)))) order by e.code "
						,ProductCategory.class)
						.setParameter(1,category)
						.setParameter(2,category)
						.setParameter(3,category)
						.setParameter(4,category)
						.setParameter(5,category)
						.setParameter(6,category)
						.setParameter(7,category)
						.setParameter(8,category)
						.setParameter(9,category)
						.getResultList();
		List<CategoryVO> navigationVO = new ArrayList<CategoryVO>();
		if(productCategoryList != null && productCategoryList.size() > 1){
			for(ProductCategory pc : productCategoryList)
			navigationVO.add(new CategoryVO(pc));
		}
		return navigationVO;
	}
	
	public String getSqlByFeature (String[] featureList){
		String sql = "";
		if(featureList == null)
			return sql;
		for(String a : featureList){
			String tmpSql = " and INSTR(product.feature , \""+a+"\") <> 0";
			sql = sql.concat(tmpSql);
		}
		return sql;
	}

	/**
	 * 统计所有的产品数量
	 * @return int
	 */
	@Override
	public int countAllProduct(){
		int result = 0;
		try{
			result = Integer.parseInt(entityManager.createNativeQuery(" SELECT count(*) FROM product WHERE organization  IS NOT NULL  ").getSingleResult().toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 统计计算风险指数成功的所有的产品数量
	 * @return int
	 */
	@Override
	public int countAllProductbyRisk(){
		int result = 0;
		try{
			result = Integer.parseInt(entityManager.createNativeQuery(" SELECT count(*) FROM product pro WHERE pro.risk_succeed = 1  and   pro.organization is not null    ").getSingleResult().toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 大众门户搜索接口-按产条件统计数量（new）
	 * @author tangxin 2015/04/01
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int countProductByCondition(String keyword, String category,Integer catLevel, String enterpriseName, String brand, String nutriLabel,String ordername) {
		int count = 0;
		try{
			String condition = "";
			Map<String, Object> conMap = getSearchCondition(category, catLevel, enterpriseName, brand, nutriLabel);
			if(conMap.get("condition") != null) {
				condition = conMap.get("condition").toString();
			}
			if("风险指数".equals(ordername)){
				condition += " and product.risk_succeed = 1 ";
			}
			String sql = "SELECT COUNT(DISTINCT product.id) FROM product " +
					"LEFT JOIN business_unit bus ON product.organization = bus.organization " +
					"LEFT JOIN business_brand brand ON product.business_brand_id = brand.id " +
					"LEFT JOIN product_category_info pci ON product.category_id = pci.id " +
					"LEFT JOIN product_category pc ON product.category = pc.code " +
					"WHERE pci.category_flag = 1 AND (product.`name` LIKE ?1 OR product.barcode = ?2 "+//OR bus.`name` LIKE ?3 " +
					"OR brand.`name` LIKE ?4 OR pci.`name` LIKE ?5) " + condition;
			Query query = entityManager.createNativeQuery(sql)
                    .setParameter(1, "%"+keyword+"%")
                    .setParameter(2, keyword)
                    //.setParameter(3, "%"+keyword+"%")
                    .setParameter(4, "%"+keyword+"%")
                    .setParameter(5, "%"+keyword+"%");
			setQueryParamsValue(query, (Map<String, String>) conMap.get("params"));
			Object objCont = query.getSingleResult();
			count = (objCont != null ? Integer.valueOf(objCont.toString()):0);
		}catch(Exception e){
			e.printStackTrace();
		}
		return count;
	}
	
	/**
	 * 搜索接口按条件查询产品数量
	 * @author TangXin  2015/04/01
	 */
    @SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getListProductsByCondition(String keyword,String enterpriseName,
			String category, Integer catLevel, String brand, String ordername,
			String ordertype, int start, int pageSize, String nutriLabel) {
		List<Object[]> listSearchProduct = null;
		try{
			String condition = "";
			String limtStr = " LIMIT 0,40 ";
			String orderCond = "";
			String orderByCond = " ORDER BY  p2.cont " + ordertype +", product.id " + ordertype + " ";
			if("风险指数".equals(ordername)) {
				orderByCond = " ORDER BY product.riskIndex " +ordertype +", product.id " + ordertype + " ";
			} else if(!"综合".equals(ordername)) {
				orderCond = " AND rep.test_type = '" + ordername + "'";
			}
			Map<String, Object> conMap = getSearchCondition(category, catLevel, enterpriseName, brand, nutriLabel);
			if(conMap.get("condition") != null) {
				condition = conMap.get("condition").toString();
			}
			if(start>-1){
				limtStr = (" LIMIT "+ start +"," + pageSize + " ");
			}
			if("风险指数".equals(ordername)){
				condition += " and product.risk_succeed = 1 ";
			}
			String sql ="SELECT DISTINCT product.id,product.name,product.imgurl,product.cstm,product.qscore_self," +
					"product.qscore_censor,product.qscore_sample,product.risk_succeed,product.riskIndex," +
					"product.test_property_name,product.nutri_label,p2.cont,producer.name as busName from product " +
					"LEFT JOIN (SELECT samp.product_id,COUNT(rep.id) cont FROM product " +
					"LEFT JOIN  product_instance samp ON product.`id`=samp.`product_id` " +
					"LEFT JOIN test_result rep ON samp.id=rep.sample_id AND rep.del = 0 " +
					"LEFT JOIN business_unit bus ON product.organization = bus.organization " +
					"LEFT JOIN business_brand brand ON product.business_brand_id = brand.id " +
					"LEFT JOIN product_category_info pci ON product.category_id = pci.id " +
					"WHERE rep.publish_flag='1' AND rep.test_type IS NOT NULL AND rep.test_type!='' " +
					"AND (product.`name` LIKE ?1 OR product.barcode = ?2 "+//OR bus.`name` LIKE ?3 " +
					"OR brand.`name` LIKE ?4 OR pci.`name` LIKE ?5) " + condition + orderCond +
					"GROUP BY samp.product_id ) p2 ON product.id=p2.product_id " +
					"LEFT JOIN business_unit bus ON product.organization = bus.organization " +
					"LEFT JOIN business_unit producer ON producer.id = product.producer_id " +
					"LEFT JOIN business_brand brand ON product.business_brand_id = brand.id " +
					"LEFT JOIN product_category_info pci ON product.category_id = pci.id " +
					"WHERE pci.category_flag in (0,1)  AND (product.`name` LIKE ?1 OR product.barcode = ?2 "+//OR bus.`name` LIKE ?3 " +
					"OR brand.`name` LIKE ?4 OR pci.`name` LIKE ?5)   and product.organization  IS NOT NULL " + condition + orderByCond + limtStr;
			Query query = entityManager.createNativeQuery(sql)
                    .setParameter(1, "%"+keyword+"%")
                    .setParameter(2, keyword)
                    //.setParameter(3, "%"+keyword+"%")
                    .setParameter(4, "%"+keyword+"%")
                    .setParameter(5, "%"+keyword+"%");
			setQueryParamsValue(query, (Map<String, String>) conMap.get("params"));
			listSearchProduct = query.getResultList();
		}catch(Exception e){
			e.printStackTrace();
		}
		return listSearchProduct;
	}

	/**
     * 搜索接口按条件统计产品数量
     * @updated TangXin  2015/03/31
     */
	public int getProductCount(String keyword, String category,Integer catLevel, String brand,
			String[] featureList, String ordername, String ordertype, String nutriLabel) {
		brand = ((!brand.equals(""))&&(brand!=null))?brand:("%"+brand+"%");
		if(!ordertype.equals("")){
		    String condition = getConditionByTestTypeOrder(ordername,ordertype);
		    String proCatCondition = ""; // 根据产品分类级拼查询条件
		    String nutriLabelCondition = "";
		    if(nutriLabel != null && !"".equals(nutriLabel)){
		    	nutriLabelCondition = " and product.nutri_label like '%"+nutriLabel+"%' ";
		    }
            /* 当产品分类是第三级时 查询条件是三级分类的id */
            if(catLevel != null && catLevel == 3) {
                proCatCondition = " AND product.category_id = ?6 ";
            }else {
                /* 1.如果是手机传来的三级分类 */
                if (!"".equals(category)&& category != null && category.length()==6) {
                        String sql = "select count(*) from product "+condition+" where ((product.name like ?1 or product.barcode = ?2 or " +
                            "product.business_brand_id in ( select distinct business_brand.id from business_brand where " +
                            "business_brand.name like ?3 ) or product.organization IN " +
                            "(SELECT DISTINCT organization FROM business_unit where name like ?4))  or " +
                            "product.id IN(SELECT DISTINCT samp.product_id FROM business_unit bus LEFT JOIN product_instance samp " +
                            "ON bus.id=samp.producer_id WHERE bus.`name` LIKE ?5 AND samp.product_id is NOT NULL)) " +
                            "and product.category_id in (SELECT DISTINCT pci.id FROM product_category_info pci RIGHT JOIN " +
                            "(SELECT id,`name` FROM product_category WHERE `code` LIKE ?6 AND LENGTH(`code`)=6) ctgy " +
                            "ON pci.`display`=ctgy.`name`) AND product.business_brand_id in( select distinct business_brand.id from business_brand where "+
                            "business_brand.name like ?7) " + nutriLabelCondition;
                        Object count = entityManager.createNativeQuery(sql)
                                    .setParameter(1, "%"+keyword+"%")
                                    .setParameter(2, keyword)
                                    .setParameter(3, "%"+keyword+"%")
                                    .setParameter(4, "%"+keyword+"%")
                                    .setParameter(5, "%"+keyword+"%")
                                    .setParameter(6, category+"%")
                                    .setParameter(7, brand)
                                    .getSingleResult();
                        return count != null ? Integer.valueOf(count.toString()):0;
                    }
                /* 分类属于一二级时 查询条件是 code */
                proCatCondition = " AND product.category like ?6 ";
            }
            String sql = "select count(*) from product where ((product.name like ?1 or product.barcode = ?2 or " +
                    "product.business_brand_id in ( select distinct business_brand.id from business_brand where " +
                    "business_brand.name like ?3 )or product.organization IN " +
                    "(SELECT DISTINCT organization FROM business_unit where name like ?4))  or " +
                    "product.id IN(SELECT DISTINCT samp.product_id FROM business_unit bus LEFT JOIN product_instance samp " +
                    "ON bus.id=samp.producer_id WHERE bus.`name` LIKE ?5 AND samp.product_id is NOT NULL)) " + proCatCondition +
                    " AND product.business_brand_id in( select distinct business_brand.id from business_brand where "+
                    "business_brand.name like ?7) " + nutriLabelCondition;
            Object count = entityManager.createNativeQuery(sql)
                            .setParameter(1, "%"+keyword+"%")
                            .setParameter(2, keyword)
                            .setParameter(3, "%"+keyword+"%")
                            .setParameter(4, "%"+keyword+"%")
                            .setParameter(5, "%"+keyword+"%")
                            .setParameter(6, (catLevel != null && catLevel == 3)? Long.valueOf(category) : category+"%")
                            .setParameter(7, brand)
                            .getSingleResult();
            return (count!=null?Integer.parseInt(count.toString()):0);
        }
		return 0;
	}

	@SuppressWarnings("unchecked")
	public List<ProductCategory> getCategory() {
		List<ProductCategory> result = entityManager
				.createNativeQuery(
						" select * from product_category  order by product_category.code"	
						,ProductCategory.class)
						.getResultList();
		return result;
	}
	@SuppressWarnings("unchecked")
    public CategoryVO getCategoryByCode(String category,Integer catLevel) {
        String sql = "";
        if (catLevel != null && catLevel == 3) {
            /* 当前属于第三级分类时 */
            sql = "SELECT id,name,display FROM product_category_info WHERE id = ?1";
        } else {
            /* 当前属于第一二级分类时 */
            sql = " SELECT id,name,display,code FROM product_category WHERE product_category.code = ?1" ;
        }
        List<Object[]> result =  entityManager.createNativeQuery(sql)
                        .setParameter(1, catLevel == 3? Long.valueOf(category) : category)
                        .getResultList();
        
        CategoryVO categoryVO = (result != null && result.size() > 0) ? potteCategory(result, catLevel).get(0):null;
        return categoryVO; 
    }

	@SuppressWarnings("unchecked")
	@Override
	public List<BrandVO> getAllBrandList() {
	    List<Object[]> result = null;
		try{
			String sql="SELECT br.id,br.name FROM business_brand br LEFT JOIN product pro ON br.id=pro.business_brand_id " +
					"WHERE pro.category is not NULL and br.organization!=1 AND pro.business_brand_id is NOT NULL " +
					"AND br.id is not null group by br.name ORDER BY br.`name` DESC";
			result=entityManager.createNativeQuery(sql).getResultList();
		}catch(Exception e){
			e.printStackTrace();
		}
		return (result !=null && result.size() > 0 ) ? structureBrandVO(result):null;
	}

	/**
	 * 手机app接口 获取第一级商品分类
	 * @return List<ProductCategoryVO>
	 * @author TangXin
	 */
	@Override
	public List<ProductCategoryVO> getFirstCategory() {
		try{
			String sql="select new com.lhfs.fsn.vo.product.ProductCategoryVO(e) from ProductCategory e WHERE LENGTH(e.code)=2";
			return this.getListByJPQL(ProductCategoryVO.class, sql, new Object[]{});
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 大众门户接口 获取产品第一级商品分类
	 * @author TangXin 2015/03/30
	 */
	@Override
	public List<CategoryVO> getALLFirstCategoryVO() {
		try{
			String sql="select new com.lhfs.fsn.vo.product.CategoryVO(e) from ProductCategory e WHERE LENGTH(e.code)=2 ORDER BY e.id asc";
			return this.getListByJPQL(CategoryVO.class, sql, new Object[]{});
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 按父类code查找下一级商品分类
	 * @param code:父类code
	 * @return List<ProductCategoryVO>
	 * @author TangXin
	 */
	@Override
	public List<ProductCategoryVO> getChildrenCategory(String code) {
		try{
			int len = code.length() + 2;
			String sql="select new com.lhfs.fsn.vo.product.ProductCategoryVO(e) from ProductCategory e WHERE LENGTH(e.code)= ?1 and e.code like ?2";
			return this.getListByJPQL(ProductCategoryVO.class, sql, new Object[]{len,code+"%"});
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 按二级分类ID查找商品三级分类
	 * @param secondCategoryId 商品二级分类ID
	 * @return List<ProductCategoryVO>
	 * @author TangXin
	 */
	@Override
	public List<ProductCategoryVO> getThridCategory(Long secondCategoryId) {
		try{
			if(secondCategoryId == null) return null;
			String jpql="SELECT new com.lhfs.fsn.vo.product.ProductCategoryVO(e) FROM ProductCategoryInfo e WHERE e.category.id= ?1 AND e.categoryFlag='1'";
			return getListByJPQL(ProductCategoryVO.class, jpql, new Object[]{secondCategoryId});
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 筛选条件
	 *@author tangxin 2015/04/01
	 */
	private Map<String,Object> getSearchCondition(String category,Integer catLevel, String enterpriseName, String brand, String nutriLabel) {
		Map<String,Object> conditionMap = new HashMap<String,Object>();
		Map<String,String> paramMap = new HashMap<String,String>();
		String conditionStr = "";
		if(!"".equals(category)){
			if(catLevel != 3) {
				conditionStr += (" AND product.category LIKE :category ");
				paramMap.put("category", category+"%");
			}else {
				conditionStr += (" AND product.category_id = :category ");
				paramMap.put("category", category);
			}
		}
		if(!"".equals(enterpriseName)) {
			conditionStr += (" AND bus.`name` like :enterpriseName ");
			enterpriseName="%"+enterpriseName+"%";
			paramMap.put("enterpriseName", enterpriseName);
		}
		if(!"".equals(brand)) {
			conditionStr += (" AND brand.`name` = :brand ");
			paramMap.put("brand", brand);
		}
		if(!"".equals(nutriLabel)) {
			/* 产品营养成分模糊查询处理 */
			nutriLabel = NutritionUtil.getNutritionLabel(nutriLabel);
			conditionStr +=  (" AND product.`nutri_label` like :nutriLabel ");
			paramMap.put("nutriLabel", "%" + nutriLabel + "%");
		}
		if(!"".equals(conditionStr)){
			conditionMap.put("condition", conditionStr);
			conditionMap.put("params", paramMap);
		}
		return conditionMap;
	}
	
	/**
	 * 按需给Query 赋参数值
	 * @author tangxin 2015/04/01
	 */
	private void setQueryParamsValue(Query query, Map<String, String> paramsMap) {
		if(query == null || paramsMap == null) {
			return;
		}
		if(paramsMap.get("category") != null){
			query.setParameter("category", paramsMap.get("category"));
		}
		if(paramsMap.get("enterpriseName") != null){
			query.setParameter("enterpriseName", paramsMap.get("enterpriseName"));
		}
		if(paramsMap.get("brand") != null){
			query.setParameter("brand", paramsMap.get("brand"));
		}
		if(paramsMap.get("nutriLabel") != null){
			query.setParameter("nutriLabel", paramsMap.get("nutriLabel"));
		}
	}

	/**
	 * 根据3级分类id，查询出一级、二级、三级分类vo
	 * @author tangxin 2015/04/22
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map getFirstAndSecondAndThridCategoryByThridId(String categoryId){
		Map<String ,Object> resultMap = null;
		if(categoryId == null || "".equals(categoryId)) {
			return resultMap;
		} 
		Long pc3Id = Long.parseLong(categoryId);
		String sql = "select t.c1id,t.c1name,t.c1code,t.c1display,t.pc2id,t.pc2name,t.pc2code,t.pc2display," +
				"t.pc3id,t.pc3name,'' AS pc3code ,t.pc3disply " +
				"from (SELECT pc1.id as c1id, pc1.`name` as c1name, pc1.display as c1display, pc1.`code` as c1code, "+ 
				" pc23.pc2id, pc23.pc2name, pc23.pc2display, pc23.pc2code, pc23.pc3id,pc23.pc3name,pc23.pc3name as pc3disply"+
				" FROM product_category pc1 RIGHT JOIN  "+
				" (SELECT pc2.id as pc2id, pc2.`name` as pc2name,pc2.display as pc2display, pc2.`code` as pc2code, "+
				" pc3.id as pc3id,pc3.`name` as pc3name FROM product_category pc2  "+
				" RIGHT JOIN product_category_info pc3 ON pc2.id=pc3.category_id  "+
				" where pc3.id = ?1 AND pc3.category_flag = 1) pc23 ON pc1.`code` = SUBSTR(pc23.pc2code,1,2)) t" ;
		Query query = entityManager.createNativeQuery(sql)
                	  .setParameter(1, pc3Id);
		List<Object[]> listSearchResult = query.getResultList();
		if(listSearchResult == null || listSearchResult.size() < 1) {
			return resultMap;
		}
		resultMap = new HashMap<String, Object>();
		List<CategoryVO> firstCategory = createCategory(listSearchResult,0);
		resultMap.put("firstCategory", firstCategory != null ? firstCategory.get(0) : null);
		List<CategoryVO> secondCategory = createCategory(listSearchResult,4);
		resultMap.put("secondCategory", secondCategory != null ? secondCategory.get(0) : null);
		List<CategoryVO> thirdCategory = createCategory(listSearchResult,8);
		resultMap.put("thirdCategory", thirdCategory != null ? thirdCategory.get(0) : null);
		return resultMap;
	}

	/**
	 * 获取下一及产品分类
	 * @author tangxin 2015/04/22
	 */
	@Override
	public List<CategoryVO> getNextCategory(String category, Integer catLevel) {
		String sql = "";
		try {
			int length = category.length() + 2;
			if(catLevel == 1) {
				/* 通过一级分类的code查其二级分类 */
				sql="select new com.lhfs.fsn.vo.product.CategoryVO(e) from ProductCategory e WHERE LENGTH(e.code) = ?1 AND e.code like ?2";
				return this.getListByJPQL(CategoryVO.class, sql, new Object[]{length, category+"%"});
			}else if(catLevel == 2) {
				/* 通过二级分类的code查其三级分类 */
				sql="select new com.lhfs.fsn.vo.product.CategoryVO(e) from ProductCategoryInfo e WHERE e.category.code = ?1 and e.categoryFlag = 1";
				return this.getListByJPQL(CategoryVO.class, sql, new Object[]{category});
			}
		} catch (JPAException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}