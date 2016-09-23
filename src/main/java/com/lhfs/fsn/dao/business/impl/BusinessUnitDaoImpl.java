package com.lhfs.fsn.dao.business.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.erp.base.ReceivingNote;
import com.lhfs.fsn.dao.business.BusinessUnitDao;
import com.lhfs.fsn.vo.PlusInfoVO;
import com.lhfs.fsn.vo.ResourceVO;
import com.lhfs.fsn.vo.business.BusinessAndPros2PortalVO;
import com.lhfs.fsn.vo.business.BusinessResultVO;
import com.lhfs.fsn.vo.business.BusinessVOWda;
import com.lhfs.fsn.vo.business.BussinessBaseInfoVO;
import com.lhfs.fsn.vo.business.BussinessCredentialsVO;
import com.lhfs.fsn.vo.product.ProductInfoVO;
import com.lhfs.fsn.vo.product.ProductInfosVO;
/**
 * 
 * @author Kxo
 */
@Repository(value="businessUnitLFDao")
public class BusinessUnitDaoImpl extends BaseDAOImpl<BusinessUnit> implements BusinessUnitDao{

	@PersistenceContext private EntityManager entityManager;

	@Override
	public BusinessUnit findByName(String name) {
		List<BusinessUnit> result = entityManager.createQuery("from business_unit where name=?1", BusinessUnit.class)
				.setParameter(1, name).getResultList();
		
		if(result != null && result.size() > 0){
			return result.get(0);
		}
		return null;
	}

	/**
	 * 查找热点企业
	 * @throws DaoException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getListOfHotBusinessUnitWithPage(int page,
			int pageSize, Long busId, String busIds) throws DaoException {
		try {
			String condition = "";
			if(busId!=null){
				condition = " AND bus.id=" + busId;
			}else if(busIds!=null){
				condition = " AND bus.id not in (" + (busIds.equals("") ? "'"+"'" :busIds )+") ";
			}

			String sql="SELECT bus.id FROM business_unit bus " + 
			 		"RIGHT JOIN product pro ON pro.organization = bus.organization " + 
			 		"LEFT JOIN product_instance pin ON  pin.product_id = pro.id " + 
			 		"LEFT JOIN test_result res ON res.sample_id = pin.id AND res.del = 0 AND res.publish_flag = '1' and res.test_type != '' and res.test_type is not null " + 
			 		"WHERE  (bus.type='生产企业' or bus.type='仁怀市白酒生产企业' ) "+ condition +" GROUP BY bus.id ORDER BY count(res.id) DESC,count(pro.id) DESC,bus.organization DESC  ";

			if(page >= 0){
				sql += " limit "+ page + "," + (pageSize-page);
			}
			Query query = entityManager.createNativeQuery(sql);
			List<Long> buIds=query.getResultList();
			return buIds;
		} catch (Exception e) {
			throw new DaoException("【DAO-error】查找热点企业，出现异常", e);
		}
	}

	/**
	 * 查找热点企业总数
	 * @throws DaoException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public long countOfHotBusinessUnit() throws DaoException {
		try {		
			String sql="SELECT COUNT(*) FROM business_unit bus " + 
			 		"RIGHT JOIN product pro ON pro.organization = bus.organization " + 
			 		"LEFT JOIN product_instance pin ON  pin.product_id = pro.id " + 
			 		"LEFT JOIN test_result res ON res.sample_id = pin.id AND res.del = 0 AND res.publish_flag = '1' " + 
			 		"WHERE  (bus.type='生产企业' or bus.type='仁怀市白酒生产企业' ) GROUP BY bus.id   ";
			Query query = entityManager.createNativeQuery(sql);
			List<Object> result = query.getResultList();
			if(result.size() <= 0){
				return 0;
			}
			return result.size();
			
			/*Query query = entityManager.createNativeQuery(sql);
			Object rtn = query.getSingleResult();
			return rtn == null ? 0 : Long.parseLong(rtn.toString());*/
		} catch (Exception e) {
			throw new DaoException("【DAO-error】查找热点企业总数，出现异常", e);
		}
	}
	
	/**
     * 根据批次和条形码获取收货单集合
     * @param batch
     * @param barcode
     * @return List<ReceivingNote>
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ReceivingNote> getProductTraceabilityList(String barcode, String batch) throws DaoException{
        try{
	    	String sql = "SELECT DISTINCT t.* FROM t_meta_receivingnote t "+
	    					 "INNER JOIN t_meta_receivingnote_to_contact t1 ON t1.receivenote_no=t.re_num "+
	    					 "INNER JOIN t_meta_purchaseorder_info t2 ON t2.po_id=t1.contact_id "+
	    					 "WHERE t2.po_batch=?1 AND t2.po_product_info_id=?2 ";
	        Query query = entityManager.createNativeQuery(sql,ReceivingNote.class);
	        query.setParameter(1, batch);
	        query.setParameter(2, barcode);
	        List<ReceivingNote> list = query.getResultList();
	        return list.size() < 0 ? null : list ;
        }catch(Exception e){
			throw new DaoException("BusinessUnitServiceImpl.getReceivingNoteByBatchAndBarcode() 根据批次和条形码获取收货单集合, 出现异常！",e);
		}
    }
    
    /**
     * 根据organization查询企业
     * @param organization
     * @return BusinessUnit
     */
    @Override
    public BusinessUnit findByOrgnizationId(Long organizationId) throws DaoException {
        try {
            String condition = " WHERE e.organization = ?1";
            Object[] params = new Object[]{organizationId};
            List<BusinessUnit> result = this.getListByCondition(condition, params);
            if(result.size()>0){
                return result.get(0);
            }
            return null;
        } catch (JPAException jpae) {
            throw new DaoException("【dao-error】按组织机构Id查找一条生产商信息，出现异常！", jpae.getException());
        }
    }

    /**
     * 根据模糊的企业名称查询出匹配的企业名称
     * @param name 模糊的企业名称
     * @return List<String>
     */
    @Override
    public List<String> loadBusinessUnitListForName(String name) throws DaoException {
        try {
            String sql = "SELECT b.name FROM business_unit b WHERE b.name LIKE ?1  ";
            List<String> list = this.getListBySQLWithoutType(String.class, sql, new Object[] {"%"+name+"%"}) ;
            return list.size() > 0 ? list : null  ;
        } catch (JPAException jpae) {
            throw new DaoException("【dao-error】loadBusinessUnitListForName()根据模糊的企业名称查询出匹配的企业名称，出现异常！", jpae.getException());
        }
    }
    
    /**
     * 根据模糊的企业名称查询出匹配的企业名称 分页显示
     * @param name 模糊的企业名称
     * @return List<String>
     */
    @Override
    public List<String> loadBusinessUnitListForName(String name,int page,int pageSize) throws DaoException {
        try {
        	if(page < 1){
    			return null;
    		}
            String sql = "SELECT b.name FROM business_unit b WHERE b.name LIKE ?1 LIMIT ?2,?3 ";
            List<String> list = this.getListBySQLWithoutType(String.class, sql, new Object[] {"%"+name+"%",(page-1)*pageSize,pageSize}) ;
            return list.size() > 0 ? list : null  ;
        } catch (JPAException jpae) {
            throw new DaoException("【dao-error】loadBusinessUnitListForName()根据模糊的企业名称查询出匹配的企业名称，出现异常！", jpae.getException());
        }
    }
    

    @Override
    public Object loadBusinessUnitListForNameCount(String name) throws DaoException {
        try {
            Long total = this.count("WHERE e.name LIKE ?1", new Object[] {"%"+name+"%"});
            return total ;
        } catch (JPAException jpae) {
            throw new DaoException("【dao-error】loadBusinessUnitListForNameCount()根据模糊的企业名称查询出匹配的企业名称，出现异常！", jpae.getException());
        }
    }

    /**
     * 根据模糊的企业营业执照号查询企业的企业营业执照号
     * @param licenseNo 模糊的企业营业执照号
     * @return List<String>
     */
    @Override
    public List<String> loadBusinessUnitListForlicenseNo(String licenseNo) throws DaoException {
        try {
            String sql = "SELECT b.license_no FROM business_unit b WHERE b.license_no LIKE ?1 ";
            List<String> list = this.getListBySQLWithoutType(String.class,sql,new Object[] {"%"+licenseNo+"%"});
            return list.size() > 0 ? list : null;
        } catch (JPAException jpae) {
           throw new DaoException("【dao-error】loadBusinessUnitListForlicenseNo()根据模糊的企业营业执照号查询企业的企业营业执照号，出现异常！", jpae.getException());
        }
    }
    
    /**
	 * 监管系统获取来源于超市没有认证的企业信息
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BusinessVOWda> getListOfMarketByMarketIdWithPage(Long busId,
			int page, int pageSize) throws DaoException {
		List<BusinessVOWda> busVO = null;
		try{
			String sql="SELECT bus.id,bus.`name`,bus.address,bus.contact,bus.telephone FROM " +
					"(SELECT yhbs.business_id,lic.business_id AS licbid FROM t_market_to_business yhbs " +
					"LEFT JOIN businessunit_to_prolicinfo lic " +
					"ON yhbs.business_id=lic.business_id AND lic.del = 0" +
					"WHERE yhbs.market_bus_id= ?1) mp LEFT JOIN business_unit bus " +
					"ON mp.business_id=bus.id " +
					"WHERE mp.licbid is NULL AND lic.del = 0";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, busId);
			if (page > 0) {
				query.setFirstResult((page - 1) * pageSize);
				query.setMaxResults(pageSize);
			}
			List<Object[]> listObjs = query.getResultList();
			if(listObjs!=null&&listObjs.size()>0){
				busVO = new ArrayList<BusinessVOWda>();
				for(int i=0;i<listObjs.size();i++){
					Object[] obj = (Object[]) listObjs.get(i);
					BusinessVOWda vo = new BusinessVOWda();
					vo.setName(obj[1]!=null?obj[1].toString():"");
					vo.setAddress(obj[2]!=null?obj[2].toString():"");
					vo.setContact(obj[3]!=null?obj[3].toString():"");
					vo.setTelephone(obj[4]!=null?obj[4].toString():"");
					busVO.add(vo);
				}
			}
			return busVO;
		}catch(Exception e){
			throw new DaoException("BusinessUnitDaoImpl.getListOfMarketByMarketIdWithPage() "+e.getMessage(),e);
		}
	}

	//cxl
	@SuppressWarnings("unchecked")
	public List<ProductInfoVO> getBusinessUnitListByName(int page,int pageSize,String name,
			String licenseNo) throws DaoException {
		List<ProductInfoVO> proVO = null;
		String sqlName= " and name like '%"+name+"%' ";
		String sqlLisNo = " and license_no like '%"+licenseNo+"%' ";
		try{
			String sql = " select name,address,license_no,contact,telephone,organization from business_unit bus " +
							"where organization is not null and organization!='' and type='流通企业' ";
			if(!name.equals("")&&licenseNo.equals("")){
				sql = sql+sqlName;
			}
			if(name.equals("")&&!licenseNo.equals("")){
				sql = sql+sqlLisNo;
			}
			if(!name.equals("")&&!licenseNo.equals("")){
				sql = sql+sqlName+sqlLisNo;
			}
			Query query = entityManager.createNativeQuery(sql);
			if(page > 0){
				query.setFirstResult((page-1) * pageSize);
				query.setMaxResults(pageSize);
			}
			List<Object[]> objs = query.getResultList();
			if(objs!=null && objs.size()>0){
				proVO = new ArrayList<ProductInfoVO>();
				for(int i=0;i<objs.size();i++){
					Object[] obj = (Object[]) objs.get(i);
					ProductInfoVO vo = new ProductInfoVO();
					vo.setBusinessName(obj[0]!=null?obj[0].toString():"");
					vo.setAddress(obj[1]!=null?obj[1].toString():"");
					vo.setLicenseNo(obj[2]!=null?obj[2].toString():"");
					vo.setContact(obj[3]!=null?obj[3].toString():"");
					vo.setTelephone(obj[4]!=null?obj[4].toString():"");
					vo.setOrgId((Long.parseLong(obj[5].toString())));
					proVO.add(vo);
				}
			}
			return proVO;
		}catch(Exception jpae){
			throw new DaoException("BusinessUnitDaoImpl.getBusinessUnitListByName() "+jpae.getMessage(),jpae);
		}
       
	}

	/**
	 * 统计指定类型的企业数量
	 * @param type
	 * @return
	 * @throws DaoException
	 */
	@Override
	public long countByType(String type,String name,String organization) throws DaoException{
		try{
			String condition = "";
			if(!organization.equals("")){
				condition = " AND e.org_code LIKE ?3 ";
			}
			String sql = " SELECT COUNT(*) FROM business_unit e WHERE e.type LIKE ?1 AND e.name LIKE ?2 AND e.organization !=1 "+condition;
			Query query = entityManager.createNativeQuery(sql);
	        query.setParameter(1, "%"+type+"%");
	        query.setParameter(2,"%"+name+"%");
	        if(!organization.equals("")){
	        	query.setParameter(3,"%"+organization+"%");
			}
	        Object rtn = query.getSingleResult();
			return rtn == null ? 0 : Long.parseLong(rtn.toString());
		}catch(Exception jpae){
			throw new DaoException("BusinessUnitDaoImpl.countByType() "+jpae.getMessage(),jpae);
		}
	}
	
	/**
	 * 查询指定类型的企业类别
	 * @param type
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws DaoException
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<BusinessVOWda> getListEnterpriseByTypeWithPage(String type,int page, int pageSize,String name,String organization) throws DaoException {
		List<BusinessVOWda> busVO = null;
		try{
			String condition ="";
			if(type == null){return null;}
			if(!organization.equals("")){
				condition = " AND bus.org_code LIKE ?3 ";
			}
			String sql="SELECT bus.id,bus.name,bus.address,bus.contact,bus.telephone,bus.organization,tmp.pcount,bus.org_code " +
					"from business_unit bus LEFT JOIN (SELECT mp.organization,count(mp.product_id) as pcount from " +
					"t_meta_initialize_product mp GROUP BY mp.organization ORDER BY count(mp.product_id) DESC) tmp " +
					"ON bus.organization=tmp.organization " +
					"where bus.type like ?1 AND bus.name LIKE ?2 "+condition+" AND bus.organization !=1 ORDER BY tmp.pcount DESC";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, "%"+type+"%");
			query.setParameter(2, "%"+name+"%");
			if(!organization.equals("")){
				query.setParameter(3, "%"+organization+"%");
			}
			if(page > 0){
				query.setFirstResult((page-1) * pageSize);
				query.setMaxResults(pageSize);
			}
			List<Object[]> objs = query.getResultList();
			if(objs!=null && objs.size()>0){
				busVO = new ArrayList<BusinessVOWda>();
				for(int i=0;i<objs.size();i++){
					Object[] obj = (Object[]) objs.get(i);
					BusinessVOWda vo = new BusinessVOWda();
					vo.setId(Long.parseLong(obj[0]!=null?obj[0].toString():"-1L"));
					vo.setName(obj[1]!=null?obj[1].toString():"");
					vo.setAddress(obj[2]!=null?obj[2].toString():"");
					vo.setContact(obj[3]!=null?obj[3].toString():"");
					vo.setTelephone(obj[4]!=null?obj[4].toString():"");
					vo.setOrganization(Long.parseLong(obj[5]!=null?obj[5].toString():"-1L"));
					vo.setProductNumber(Long.parseLong(obj[6]!=null?obj[6].toString():"0"));
					vo.setOrgCode(obj[7]!=null?obj[7].toString():"");
					busVO.add(vo);
				}
			}
			return busVO;
		}catch(Exception jpae){
			throw new DaoException("BusinessUnitDaoImpl.getListEnterpriseByTypeWithPage() "+jpae.getMessage(), jpae);
		}
	}

	/**
	 * 根据交易单号、组织结构ID、批次、条形码获取交易总数量
	 * @param re_num
	 * @param organization
	 * @param batch
	 * @param barcode
	 * @return 交易总数量
	 */
	@Override
	public Long getTransactionNum(String re_num, Long organization,
			String batch, String barcode) throws DaoException {
		try{
			String sql="SELECT SUM(t.po_receivenum) FROM t_meta_purchaseorder_info t  "+
							"INNER JOIN t_meta_receivingnote_to_contact t1 ON t1.contact_id=t.po_id "+
							"INNER JOIN t_meta_receivingnote t2 ON t2.re_num=t1.receivenote_no "+
							"WHERE t2.organization=?1 AND t2.re_num=?2 AND t.po_batch=?3 "+
							"AND t.po_product_info_id=?4 ";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, organization);
			query.setParameter(2, re_num);
			query.setParameter(3, batch);
			query.setParameter(4, barcode);
			Object count = query.getSingleResult();
			return count==null?0:Long.parseLong(count.toString());	
		}catch(Exception jpae){
			throw new DaoException("BusinessUnitDaoImpl.getTransactionNum() 根据交易单号、组织结构ID、批次、条形码获取交易总数量，出现异常！", jpae);
		}
	}
	
	//加载相关企业类型下的企业和产品，按报告数量多到少排序
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> loadBusinessUnit(String type, int page,
			int pagesize,String keyword) throws DaoException {
		try{
			if(page> 0){
				page = (page-1)*pagesize;
			}else{
				return null;
			}
			String condition =""; 
			if(keyword!=null&&!"".equals(keyword)){
				condition=condition+" AND bus.name like '%"+keyword+"%' ";
			}
			String sql="SELECT bus.id,bus.about,bus.`name`, bus.website,bus.type,bus.organization FROM business_unit bus " + 
			 		"RIGHT JOIN product pro ON pro.organization = bus.organization " + 
			 		"LEFT JOIN product_instance pin ON  pin.product_id = pro.id " + 
			 		"LEFT JOIN test_result res ON res.sample_id = pin.id AND res.del = 0 AND res.publish_flag = '1' " + 
			 		"WHERE  bus.type= ?1 "+condition+" GROUP BY bus.id ORDER BY count(res.id) DESC,count(pro.id) DESC,organization DESC LIMIT ?2 , ?3 ";
		            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, type);
            query.setParameter(2, page);
            query.setParameter(3, pagesize);
            return query.getResultList();
        }catch(Exception e){
            throw new DaoException("BusinessUnitDAOImpl.loadBusinessUnit()-->"+e.getMessage(),e);
        }
	}
	//加载相关企业类型下的企业和产品，按报告数量多到少排序 的企业总数
	@Override
	public Long loadBusinessUnitCount(String type,String keyword) throws DaoException {
		try{
			String condition =""; 
			if(keyword!=null&&!"".equals(keyword)){
				condition=condition+" AND bus.name like '%"+keyword+"%' ";
			}
            String sql="SELECT count(DISTINCT bus.organization) FROM business_unit bus " + 
            		"RIGHT JOIN enterprise_registe ent ON ent.enterpriteName = bus.`name` " + 
            		"RIGHT JOIN product pro ON pro.organization = bus.organization "+
            		"WHERE  bus.type= ?1 "; 
            sql=sql+condition;
            long count = this.countBySql(sql, new Object[]{type});
			return count;
        }catch(Exception e){
            throw new DaoException("BusinessUnitDAOImpl.loadBusinessUnitCount()-->"+e.getMessage(),e);
        }
	}
	
	/**
	 * 通过关键字统计企业数量
	 * @param keyword
	 * @return
	 */
	@Override
	public int getCount(String keyword) {
		Object result = entityManager
				.createNativeQuery(
						"select count(*) from business_unit where name like ?1")
						.setParameter(1,"%"+keyword+"%")
				.getSingleResult();
		return Integer.parseInt(result.toString());
	}
	
	/**
	 * 通过关键字搜索企业
	 * @param keyword
	 * @param startindex
	 * @param pagesize
	 * @return
	 */
	@Override
	public List<BusinessUnit> getUnitByKeyword(String keyword, int startindex,
			int pagesize) {
		@SuppressWarnings("unchecked")
		List<BusinessUnit> result = entityManager
				.createNativeQuery(
						"select * from business_unit bus RIGHT JOIN product pro ON pro.organization = bus.organization " + 
			 		"LEFT JOIN product_instance pin ON  pin.product_id = pro.id " + 
			 		"LEFT JOIN test_result res ON res.sample_id = pin.id AND res.del = 0 AND res.publish_flag = '1' and res.test_type != '' and res.test_type is not null  where bus.name like ?1 GROUP BY bus.id",
						BusinessUnit.class).setParameter(1,"%"+keyword+"%")
						.setFirstResult(startindex)
						.setMaxResults(pagesize)
						.getResultList();
		if(result != null && result.size() > 0){
			return result;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BusinessAndPros2PortalVO> getBusinessByBuIds(String buIds)
			throws DaoException {
		try{
            String sql="SELECT bu.id,bu.name,bu.about,r.URL FROM business_unit bu " +
            				"LEFT JOIN enterprise_registe er ON er.enterpriteName=bu.`name` " +
            				"LEFT JOIN t_business_logo_to_resource blr ON er.id=blr.ENTERPRISE_REGISTE_ID " +
            				"LEFT JOIN t_test_resource r ON r.RESOURCE_ID=blr.RESOURCE_ID " +
            				"WHERE bu.id IN("+buIds+") GROUP BY bu.id";
            Query query=entityManager.createNativeQuery(sql);
            List<Object[]> result=query.getResultList();
            List<BusinessAndPros2PortalVO> buPros=new ArrayList<BusinessAndPros2PortalVO>();
            if(result!=null&&result.size()>0){
            	for(Object[] obj:result){
            		BusinessAndPros2PortalVO buPro=new BusinessAndPros2PortalVO();
            		buPro.setId(Long.parseLong(obj[0]!=null?obj[0].toString():"-1L"));
            		buPro.setName(obj[1]!=null?obj[1].toString():"");
            		buPro.setAbout(obj[2]!=null?obj[2].toString():"");
            		buPro.setLogo(obj[3]!=null?obj[3].toString():"");
            		buPros.add(buPro);
            	}
            }
            return buPros;
        }catch(Exception e){
            throw new DaoException("BusinessUnitDAOImpl.getBusinessByBuIds()-->"+e.getMessage(),e);
        }
	}

	/**
	 * 根据企业组织机构获取企业基本信息,和资质信息
	 * @author wb 
	 * @ date : 2016.5.9
	 * @param organization
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BusinessResultVO getOrganizationBussinessInfo(Long organization) {
		String sql = "SELECT  bus.ID,bus.license_no,bus.NAME,bus.TYPE,bus.address,bus.telephone,bus.org_code,ttrorg.URL AS organization_url,";
		sql +="bus.license_no,ttrlic.URL AS business_license_url,ttrtax.URL AS tax_registration_url,MAX(toltr.RESOURCE_ID) ";
		sql +="FROM  business_unit bus ";  
		sql +="LEFT JOIN enterprise_registe er ON er.enterpriteName=bus.name "; 
		sql +="LEFT JOIN t_org_license_to_resource toltr ON er.id=toltr.ENTERPRISE_REGISTE_ID ";
		sql +="LEFT JOIN t_test_resource ttrorg ON ttrorg.RESOURCE_ID=toltr.RESOURCE_ID ";
		sql +="LEFT JOIN t_business_license_to_resource tbltr ON  er.id=tbltr.ENTERPRISE_REGISTE_ID ";
		sql +="LEFT JOIN t_test_resource ttrlic ON ttrlic.RESOURCE_ID=tbltr.RESOURCE_ID ";
		sql +="LEFT JOIN t_tax_register_cert_to_resource ttrctr ON er.id=ttrctr.ENTERPRISE_REGISTE_ID ";
		sql +="LEFT JOIN t_test_resource ttrtax ON ttrtax.RESOURCE_ID=ttrctr.RESOURCE_ID ";
		sql +="WHERE bus.organization=?1 ";
		 
		Query query=entityManager.createNativeQuery(sql);
		query.setParameter(1, organization);
		List<Object[]> result = query.getResultList();
        
         BusinessResultVO business = new BusinessResultVO();
        List<BussinessCredentialsVO> CredentialsList = new ArrayList<BussinessCredentialsVO>();
         if(result.size()>0){
        	 Object[] obj =  result.get(0);
        	 BussinessBaseInfoVO base_info = new BussinessBaseInfoVO();
        	 base_info.setId(obj[0]==null?null:Long.parseLong(obj[0].toString()));
        	 base_info.setLicenseNo(obj[1]==null?null:obj[1].toString());
        	 base_info.setEnterprise_name(obj[2]==null?null:obj[2].toString());
        	 base_info.setEnterprise_type(obj[3]==null?null:obj[3].toString());
        	 base_info.setAddress(obj[4]==null?null:obj[4].toString());
        	 base_info.setContact(obj[5]==null?null:obj[5].toString());
        	
//        	 BussinessCredentialsVO credentials =new BussinessCredentialsVO();
//        	 credentials.setOrganization_code(obj[6]==null?null:obj[6].toString());
//        	 credentials.setOrganization_url(obj[7]==null?null:obj[7].toString());
//        	 credentials.setBusiness_license_code(obj[8]==null?null:obj[8].toString());
//        	 credentials.setBusiness_license_url(obj[9]==null?null:obj[9].toString());
//        	 credentials.setTax_registration_url(obj[10]==null?null:obj[10].toString());
        	
        	 BussinessCredentialsVO credentials1 =new BussinessCredentialsVO();
        	 credentials1.setTypeName("组织机构代码");
        	 credentials1.setCode(obj[6]==null?null:obj[6].toString());
        	 credentials1.setDocumentFullPath(obj[7]==null?null:obj[7].toString());
        	 credentials1.setType("organization_code");
        	 CredentialsList.add(credentials1);
        	 
        	 BussinessCredentialsVO credentials2 =new BussinessCredentialsVO();
        	 credentials2.setTypeName("营业执照证");
        	 credentials2.setCode(obj[8]==null?null:obj[8].toString());
        	 credentials2.setDocumentFullPath(obj[9]==null?null:obj[9].toString());
        	 credentials2.setType("business_license");
        	 CredentialsList.add(credentials2);
        	 
        	 BussinessCredentialsVO credentials3 =new BussinessCredentialsVO();
        	 credentials3.setTypeName("税务登记证");
        	 credentials3.setDocumentFullPath(obj[10]==null?null:obj[10].toString());
        	 credentials3.setType("tax_registration");
        	 CredentialsList.add(credentials3);
        	 
        	business.setBase_info(base_info);
        	business.setCredentials(CredentialsList);
         }
         return business;
	}

	/**
	 * 根据企业组织机构获取企业生产许可证基本信息
	 * @author wb 
	 * @ date : 2016.5.9
	 * @param organization
	 * @return
	 */
	@Override
	public List<ResourceVO> getOrganiaztionQSImg(Long organization) {
		String sql = "SELECT DISTINCT  btp.qs_id,pli.qs_no,ttr.file_name,ttr.URL FROM business_unit bus ";
			   sql +="LEFT JOIN businessunit_to_prolicinfo btp ON bus.id=btp.business_id ";
			   sql +="LEFT JOIN productionlicenseinfo_to_resource ptr ON ptr.qs_id=btp.qs_id ";
			   sql +="LEFT JOIN t_test_resource ttr ON ttr.RESOURCE_ID=ptr.RESOURCE_ID ";
			   sql +="LEFT JOIN production_license_info pli ON pli.id=btp.qs_id ";
			   sql +="WHERE bus.organization=?1 ";
		   
			   Query query=entityManager.createNativeQuery(sql);
				query.setParameter(1, organization);
				@SuppressWarnings("unchecked")
				List<Object[]> objs = query.getResultList();
				List<ResourceVO> licenseImg = new ArrayList<ResourceVO>();
				ResourceVO resource = null;
				for (Object[] obj : objs) {
					resource = new ResourceVO();
					resource.setQs_Id(obj[0]==null?null:obj[0].toString());
					resource.setQs_no(obj[1]==null?null:obj[1].toString());
					resource.setUrlName(obj[2]==null?null:obj[2].toString());
					resource.setQs_URL(obj[3]==null?null:obj[3].toString());
					licenseImg.add(resource);
				}
				return licenseImg;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductInfosVO> getLikeProductInfosVO(String organization,
			String proName, String proBarcode) {
		
		//String config = getSQLConfig(organization,proName, proBarcode);
		
		String sql ="SELECT DISTINCT pro.id,pro.name,pro.barcode,(SELECT tmu.NAME FROM t_meta_unit tmu WHERE tmu.ID=pro.UNIT_ID) unit, " ;
			   sql +="(SELECT COUNT(tr.test_type)  FROM product p " ;
			   sql +="LEFT JOIN product_instance pri ON pri.product_id = p.id " ;
			   sql +="LEFT JOIN test_result tr ON tr.sample_id = pri.id AND tr.test_type='企业送检' AND (tr.limsSampleId IS NULL OR tr.limsSampleId = '') " ;
			   sql +="WHERE  p.id=pro.id GROUP BY p.id) inspection_num , " ;
			
			   sql +="(SELECT COUNT(tr.test_type)  FROM product p " ;
			   sql +="LEFT JOIN product_instance pri ON pri.product_id = p.id " ;
			   sql +="LEFT JOIN test_result tr ON tr.sample_id = pri.id AND tr.test_type='企业自检' AND (tr.limsSampleId IS NULL OR tr.limsSampleId = '') " ;
			   sql +="WHERE   p.id=pro.id GROUP BY p.id) self_check_num, " ;
			
			   sql +="(SELECT COUNT(tr.test_type)  FROM product p " ;
			   sql +="LEFT JOIN product_instance pri ON pri.product_id = p.id " ;
			   sql +="LEFT JOIN test_result tr ON tr.sample_id = pri.id AND tr.test_type='政府抽检' AND (tr.limsSampleId IS NULL OR tr.limsSampleId = '') " ;
			   sql +="WHERE  p.id=pro.id GROUP BY p.id) sampling_num, " ;

			   sql +="(SELECT COUNT(tr.test_type)  FROM product p ";
			   sql +="LEFT JOIN product_instance pri ON pri.product_id = p.id ";
			   sql +="LEFT JOIN test_result tr ON tr.sample_id = pri.id AND (tr.limsSampleId IS NOT NULL OR tr.limsSampleId != '')";
			  sql +="WHERE  p.id=pro.id GROUP BY p.id) third_party_num, ";
			  sql +="pro.status,pro.format ";

			   
			   sql +=" FROM product pro " ;
			   sql +="LEFT JOIN product_instance pri ON pri.product_id = pro.id " ;
			   sql +="LEFT JOIN test_result tr ON tr.sample_id = pri.id " ;
			   sql +="WHERE  1=1  "+ getSQLConfig(organization,proName, proBarcode)+" " ;
			   Query query = entityManager.createNativeQuery(sql);
			   List<Object[]> objs = query.getResultList();
			   List<ProductInfosVO> proList = new ArrayList<ProductInfosVO>();
			   ProductInfosVO vo = null;
			   for (Object[] obj : objs) {
				   vo = new ProductInfosVO();
				  vo.setProId(obj[0]==null?null:obj[0].toString()) ;
				  vo.setProduct_name(obj[1]==null?null:obj[1].toString()) ;
				  vo.setProduct_barcode(obj[2]==null?null:obj[2].toString()) ;
				  vo.setMin_pack_unit(obj[3]==null?null:obj[3].toString()) ;
				  vo.setInspection_num(obj[4]==null?0:Integer.parseInt(obj[4].toString())) ;
				  vo.setSelf_check_num(obj[5]==null?0:Integer.parseInt(obj[5].toString())) ;
				  vo.setSampling_num(obj[6]==null?0:Integer.parseInt(obj[6].toString())) ;
				  vo.setThird_party_num(obj[7]==null?0:Integer.parseInt(obj[7].toString())) ;
				  vo.setStatus(obj[8]==null?null:obj[8].toString()) ;
				  vo.setFormat(obj[9]==null?null:obj[9].toString()) ;
				   proList.add(vo);	
				}
		return proList;
	}

	private String getSQLConfig(String organization, String proName,
			String proBarcode) {
		String config = "";
		 if(organization!=null&&!"".equals(organization)){
				 config+=" AND pro.organization = " + organization ;
		 }
		 if(proName!=null&&!"".equals(proName)&&proBarcode!=null&&!"".equals(proBarcode)){
				 config+=" AND (pro.name LIKE '%" + proName + "%' or pro.barcode LIKE '%" + proBarcode + "%') " ;
		 }
		return config;
	}
	/**
	 * 根据条形码,时间范围内获取企业自检数量
	 */
	@Override
	public String getSelfNum(String productBarcode, String startDate,String endDate) {
		
		String selfNum = "0";
		try {
			String sql = "SELECT p.id,p.barcode,COUNT( p.barcode) sun FROM product p ";
			sql+=" LEFT JOIN product_instance pri ON pri.product_id = p.id ";
			sql+=" LEFT JOIN test_result tr ON tr.sample_id = pri.id ";
			sql+=" WHERE   tr.test_type='企业自检' AND  tr.test_type IS NOT NULL AND tr.receiveDate IS NOT NULL  AND p.barcode=?1  "; 
			if (startDate != null && !"".equals(startDate) && endDate != null && !"".equals(endDate)) {
				sql += " AND tr.receiveDate BETWEEN '" + startDate + "' AND '" + endDate + "' ";
			}
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, productBarcode);
			@SuppressWarnings("unchecked")
			List<Object[]> objs = query.getResultList();
			for (Object[] obj : objs) {
				selfNum=obj[2]==null?"0":obj[2].toString();
			}
			return selfNum;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return selfNum;
	}
	/**
	 * 获取报告活跃度总数
	 */
	@Override
	public String getActiveDegree(String productBarcode, String startDate,
			String endDate) {
		String activeDegree = "0";
		try {
			String sql = "SELECT p.id,p.barcode,COUNT( p.barcode) sun FROM product p ";
			sql+="LEFT JOIN product_instance pri ON pri.product_id = p.id ";
			sql+="LEFT JOIN test_result tr ON tr.sample_id = pri.id ";
			sql+="WHERE   tr.test_type IS NOT NULL AND tr.receiveDate IS NOT NULL  AND p.barcode=?1  "; 
			if (startDate != null && !"".equals(startDate) && endDate != null && !"".equals(endDate)) {
				sql += " AND tr.receiveDate BETWEEN '" + startDate + "' AND '" + endDate + "' ";
			}
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, productBarcode);
			@SuppressWarnings("unchecked")
			List<Object[]> objs = query.getResultList();
			for (Object[] obj : objs) {
				activeDegree=obj[2]==null?"0":obj[2].toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return activeDegree;
	}
	
	/**
	 * 根据条形码,时间范围内获取企业送检数量
	 * 
	 */
	@Override
	public int getInspectionFrequency(String productBarcode,String startDate, String endDate) {
		int sunInt = 0;
		try {
			String sql ="SELECT COUNT(*)  FROM  (SELECT p.barcode,COUNT(tr.receiveDate) FROM product p ";
				   sql+=" LEFT JOIN product_instance pri ON pri.product_id = p.id ";
				   sql+=" LEFT JOIN test_result tr ON tr.sample_id = pri.id ";
				   sql+=" WHERE  tr.test_type='企业送检' AND tr.test_type IS NOT NULL AND tr.receiveDate IS NOT NULL  AND p.barcode=?1 "; 
				   if (startDate != null && !"".equals(startDate) && endDate != null && !"".equals(endDate)) {
						sql += " AND tr.receiveDate BETWEEN '" + startDate + "' AND '" + endDate + "' ";
					}
				   sql+=" GROUP BY MONTH(tr.publishDate)) t ";
				   Query query = entityManager.createNativeQuery(sql);
				   query.setParameter(1, productBarcode);
				   sunInt = Integer.parseInt(query.getSingleResult().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sunInt;
	}

	/**
	 *plus_info数据
	 */
	public PlusInfoVO getPlusInfoData(String productBarcode, String startDate,
			String endDate) {
		/**
		 * 目前没有该项信息，所以先返回new的对象
		 */
		return new PlusInfoVO();
	}

	
}