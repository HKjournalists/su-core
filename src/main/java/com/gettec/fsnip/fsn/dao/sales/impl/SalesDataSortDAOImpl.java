package com.gettec.fsnip.fsn.dao.sales.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.sales.SalesDataSortDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.sales.SalesDataSort;
import com.gettec.fsnip.fsn.model.sales.SortField;
import com.gettec.fsnip.fsn.vo.sales.SortFieldValueVO;

/**
 * 销售资料排序 DAOImpl
 * @author tangxin 2015-05-07
 */

@Repository(value="salesDataSortDAO")
public class SalesDataSortDAOImpl extends BaseDAOImpl<SalesDataSort> 
			implements SalesDataSortDAO {

	/**
	 * 分页获取排序后或未排序的产品相册(备注:产品相册的sort_field_id=6)
	 * @param sort true 获取已经排序的产品
	 * @author tangxin 2015-05-07
	 */
	@Override
	public List<SortFieldValueVO> getSortProductAlbum(Long organization, boolean sort, int page,int pageSize) 
			throws DaoException{
		try{
			String sql = "";
			Object[] params = new Object[]{organization, organization};
			if(sort){
				/* 查询排序后的产品信息 */
				sql = "select t.pid,t.object_id,t.`name`,t.sort,t.URL,t.sid,0 'no',t.format,t.des FROM " +
						"(select pro.id 'pid',pro.`name`,sort.sort,pro.URL,sort.id 'sid',sort.object_id,pro.format,pro.des FROM t_sales_data_sort sort " +
						"LEFT JOIN (SELECT tt.id,tt.`name`,tt.format,tt.des,tt.URL,tt.UPLOAD_DATE FROM " +
						"(SELECT pr.id,pr.`name`,pr.format,pr.des,res.URL,res.UPLOAD_DATE FROM product pr " +
						"LEFT JOIN t_test_product_to_resource t2p ON pr.id = t2p.PRODUCT_ID " +
						"LEFT JOIN t_test_resource res ON t2p.RESOURCE_ID = res.RESOURCE_ID " +
						"WHERE pr.organization = ?1 ORDER BY res.UPLOAD_DATE DESC) tt GROUP BY tt.id ORDER BY tt.id DESC) pro ON sort.object_id = pro.id " +
						"WHERE sort.organization = ?2 and sort.sort_field_id = 6 and sort.sort != -1 ORDER BY sort.sort ASC) t";
			}else{
				/* 查询未排序的产品,不包含已经排序的产品 */
				sql = "SELECT pro.id,pro.id,pro.name,-1 'sort',pro.URL,-1 'sid',0 'no',pro.format,pro.des FROM " +
						"(SELECT tt.id,tt.`name`,tt.format,tt.des,tt.URL,tt.UPLOAD_DATE FROM " +
						"(SELECT pr.id,pr.`name`,pr.format,pr.des,res.URL,res.UPLOAD_DATE FROM product pr " +
						"LEFT JOIN t_test_product_to_resource t2p ON pr.id = t2p.PRODUCT_ID " +
						"LEFT JOIN t_test_resource res ON t2p.RESOURCE_ID = res.RESOURCE_ID " +
						"WHERE pr.organization = ?1 ORDER BY res.UPLOAD_DATE DESC) tt GROUP BY tt.id ORDER BY tt.id DESC) pro " +
						"WHERE pro.id not IN (SELECT object_id FROM t_sales_data_sort sort WHERE sort.organization = ?2 AND sort.sort_field_id = 6)";
			}
			List<Object[]> listObjc = this.getListBySQLWithPage(sql, params, page, pageSize);
			return createSortFieldValueVO(listObjc,6L,6L, false, true, false, false);
		}catch(Exception e){
			throw new DaoException(e.getMessage(), e);
		}
	}
	
	/**
	 * 封装SortFieldValueVO集合通过List<Object[]>
	 * @author tangxin 2015-05-08
	 */
	private List<SortFieldValueVO> createSortFieldValueVO(List<Object[]> listObjs, Long sortFileId,
			Long sortParentId, boolean cert, boolean product, boolean salseCase, boolean busAlbums){
		if(listObjs == null) {
			return null;
		}
		List<SortFieldValueVO> listVO = new ArrayList<SortFieldValueVO>();
		List<String> exisName = new ArrayList<String>();
		for(Object[] objs : listObjs){
			String name = objs[2] != null ? objs[2].toString() : null;
			/* 对于有多张图片的证件，不重复封装 , 企业掠影除外 */
			if(!busAlbums){
				if(name != null && !"".equals(name)){
					if(exisName.contains(name)){
						continue;
					} else {
						exisName.add(name);
					}
				}
			}
			SortFieldValueVO vo = new SortFieldValueVO();
			vo.setId(objs[0] != null ? Long.parseLong(objs[0].toString()) : 0L);
			vo.setObjectId(objs[1] != null ? Long.parseLong(objs[1].toString()) : 0L);
			vo.setName(name);
			vo.setSort(objs[3] != null ? Integer.parseInt(objs[3].toString()) : -1);
			Long sortId = (objs[5] != null ? Long.parseLong(objs[5].toString()) : 0L);
			vo.setSortId(sortId > 0 ? sortId : null);
			vo.setNo(objs[6] != null ? objs[6].toString() : null);
			String imgUrl = (objs[4] != null ? objs[4].toString() : "");
			/* 默认取每一个产品的第一张图片 */
			int index = imgUrl.indexOf("|");
			if(index > -1) {
				vo.setUrl(imgUrl.substring(0, index));
			}else{
				vo.setUrl(imgUrl);
			}
			if(cert){
				vo.setSortFieldId(objs[7] != null ? Long.parseLong(objs[7].toString()) : 0L);
				vo.setSortFieldParentId(sortParentId);
			}else{
				vo.setSortFieldId(sortFileId);
				vo.setSortFieldParentId(sortParentId);
			}
			if(product){
				vo.setFormat(objs[7] != null ? objs[7].toString() : "");
				vo.setDesc(objs[8] != null ? objs[8].toString() : "");
			}
			if(salseCase){
				vo.setDesc(objs[7] != null ? objs[7].toString() : "");
			}
			listVO.add(vo);
		}
		return listVO;
	}
	
	/**
	 * 根据企业组织机构获取企业注册时的id已经企业id
	 * @author tangxin 2015-05-08
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Long> findEnterpriseIdAndBusIdByOrganization(Long organization) 
			throws DaoException{
		try{
			String sql="SELECT t.eid,t.bid FROM "+
					"(SELECT er.id 'eid',bus.id 'bid' FROM enterprise_registe er " +
					"LEFT JOIN business_unit bus ON er.enterpriteName = bus.`name` " +
					"WHERE bus.organization = ?1 ) t";
			List<Object[]> objs = entityManager.createNativeQuery(sql).setParameter(1, organization).getResultList();
			if(objs == null || objs.size() < 1){
				return null;
			}
			Object[] ids = objs.get(0);
			Map<String, Long> resultMap = new HashMap<String, Long>();
			resultMap.put("enterpriseId", (ids[0] != null ? Long.parseLong(ids[0].toString()) : null));
			resultMap.put("businessId", (ids[1] != null ? Long.parseLong(ids[1].toString()) : null));
			return resultMap;
		}catch(Exception e){
			throw new DaoException(e.getMessage(), e);
		}
	}
	
	/**
	 * 根据SortField id获取 SortField对象
	 * @author tangxin 2015-05-08
	 */
	@Override
	public SortField findSortFieldById(Long id) throws DaoException{
		try{
			if(id == null){
				return null;
			}
			String jpql = " select e from com.gettec.fsnip.fsn.model.sales.SortField e where e.id = ?1";
			List<SortField> listField = getListByJPQL(SortField.class, jpql, new Object[]{id});
			return (listField != null ? listField.get(0) : null);
		}catch(Exception e){
			throw new DaoException(e.getMessage(), e);
		}
	}
	
	/**
	 * 分页查询企业掠影排序或者未排序的图片
	 * @param sort true 获取已经排序的产品
	 * @author tangxin 2015-05-08
	 */
	@Override
	public List<SortFieldValueVO> getBusAlbumsWithPage(Long organization, int page, int pageSize) throws DaoException{
		try{
			
			String sql = "SELECT sres.id,0 'oid',sres.file_name,sres.sort,sres.url,-1 'sid',0 'no' FROM t_sales_resource sres " +
					"LEFT JOIN t_bus_photos_albums ph ON sres.guid = ph.guid " +
					"WHERE ph.organization = ?1 AND sres.del_status = 0 " +
					"ORDER BY sres.sort ASC,sres.cover DESC,sres.id ASC";
			List<Object[]> listObjs = getListBySQLWithPage(sql, new Object[]{organization}, page, pageSize);
			return createSortFieldValueVO(listObjs, 8L, 8L, false, false, false, true);
		}catch(Exception e){
			throw new DaoException(e.getMessage(), e);
		}
	}
	
	/**
	 * 分页查询企业销售案例排序或者未排序的封面图片
	 * @param sort true 获取已经排序的销售案例
	 * @author tangxin 2015-05-08
	 */
	@Override
	public List<SortFieldValueVO> getSalesCaseWithPage(Long organization, int page, int pageSize) throws DaoException{
		try{
			String sql = "SELECT tm.bid,0 'oid',tm.`name`,tm.sort,tm.url,-1 'stid',0 'no',tm.description FROM " +
					"(SELECT bs.id 'bid',bs.`name`,bs.sort,sres.url,bs.description FROM t_sales_resource sres " +
					"LEFT JOIN t_bus_sales_case bs ON sres.guid = bs.guid " +
					"WHERE bs.organization = ?1 AND sres.del_status = 0 " +
					"ORDER BY bs.sort ASC,sres.cover DESC) tm GROUP BY tm.bid ";
			List<Object[]> listObjs = getListBySQLWithPage(sql, new Object[]{organization}, page, pageSize);
			return createSortFieldValueVO(listObjs, 9L, 9L, false, false, true, false);
		}catch(Exception e){
			throw new DaoException(e.getMessage(), e);
		}
	}
	
	/**
	 * 分页查询已排序企业三证、生产许可证、荣誉证 图片
	 * @author tangxin 2015-05-08
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SortFieldValueVO> getSanZhengWithPage(Long organization, int page, int pageSize) throws DaoException{
		try{
			String sql = "SELECT t.RESOURCE_ID,t.object_id,t.FILE_NAME,t.sort,t.URL,t.id,t.no,t.sort_field_id from " +
					"(SELECT res.RESOURCE_ID,'组织机构证' AS 'FILE_NAME',sot.sort,sot.sort_field_id,res.URL," +
					"sot.organization,sot.id,sot.no,sot.object_id FROM t_test_resource res " +
					"LEFT JOIN t_org_license_to_resource ors ON res.RESOURCE_ID = ors.RESOURCE_ID " +
					"LEFT JOIN t_sales_data_sort sot ON ors.ENTERPRISE_REGISTE_ID = sot.object_id " +
					"WHERE sot.organization = :organization AND sort_field_id = 1 " +
					"UNION " +
					"(SELECT res.RESOURCE_ID,'营业执照证' AS 'FILE_NAME',sot.sort,sot.sort_field_id,res.URL," +
					"sot.organization,sot.id,sot.no,sot.object_id FROM t_test_resource res " +
					"LEFT JOIN t_business_license_to_resource lis ON res.RESOURCE_ID = lis.RESOURCE_ID " +
					"LEFT JOIN t_sales_data_sort sot ON lis.ENTERPRISE_REGISTE_ID = sot.object_id " +
					"WHERE sot.organization = :organization AND sort_field_id = 2) " +
					"UNION " +
					"(SELECT res.RESOURCE_ID,'税务登记证' AS 'FILE_NAME',sot.sort,sot.sort_field_id,res.URL," +
					"sot.organization,sot.id,sot.no,sot.object_id FROM t_test_resource res " +
					"LEFT JOIN t_tax_register_cert_to_resource tas ON res.RESOURCE_ID = tas.RESOURCE_ID " +
					"LEFT JOIN t_sales_data_sort sot ON tas.ENTERPRISE_REGISTE_ID = sot.object_id " +
					"WHERE sot.organization = :organization AND sort_field_id = 3) " +
					"UNION " +
					"(SELECT res.RESOURCE_ID,pli.qs_no 'FILE_NAME',sot.sort,sot.sort_field_id,res.URL," +
					"sot.organization,sot.id,sot.no,sot.object_id FROM t_test_resource res " +
					"LEFT JOIN productionlicenseinfo_to_resource q2s ON q2s.resource_id = res.RESOURCE_ID " +
					"LEFT JOIN production_license_info pli ON q2s.qs_id = pli.id " +
					"LEFT JOIN t_sales_data_sort sot ON pli.qs_no = sot.no " +
					"WHERE sot.organization = :organization AND sort_field_id = 4) " +
					"UNION " +
					"(SELECT bc.id 'RESOURCE_ID',crt.`name` AS 'FILE_NAME',sot.sort,sot.sort_field_id,res.URL," +
					"sot.organization,sot.id,sot.no,sot.object_id FROM t_test_resource res " +
					"LEFT JOIN business_certification bc ON res.RESOURCE_ID = bc.resource_id " +
					"LEFT JOIN t_sales_data_sort sot ON bc.id = sot.object_id " +
					"LEFT JOIN certification crt ON bc.cert_id = crt.id " +
					"WHERE sot.organization = :organization AND sort_field_id = 5)) t ORDER BY t.sort ASC,t.RESOURCE_ID DESC";
				    Query query = entityManager.createNativeQuery(sql).setParameter("organization", organization);
				    if(page > 0){
				    	query.setFirstResult((page-1)*pageSize);
				    	query.setMaxResults(pageSize);
				    }
			List<Object[]> listObjs = query.getResultList();
			return createSortFieldValueVO(listObjs, 0L, 7L, true, false, false, false);
		}catch(Exception e){
			throw new DaoException(e.getMessage(), e);
		}
	}
	
	/**
	 * 根据fieldTypeId 获取指定的排序资料集合
	 * @author tangxin 2015-05-10
	 */
	@Override
	public List<SalesDataSort> getListByFieldType(Long organizaton, Long fieldTypeId) throws DaoException{
		try{
			if(organizaton == null || fieldTypeId == null) {
				return null;
			}
			String condition = " where e.organization = ?1 and e.sortField.id = ?2 ";
			return getListByCondition(condition, new Object[]{organizaton, fieldTypeId});
		}catch(Exception e){
			throw new DaoException(e.getMessage(), e);
		}
	}
}
