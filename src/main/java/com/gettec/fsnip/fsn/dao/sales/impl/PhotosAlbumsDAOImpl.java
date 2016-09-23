package com.gettec.fsnip.fsn.dao.sales.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.sales.PhotosAlbumsDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.sales.PhotosAlbums;
import com.gettec.fsnip.fsn.util.ImgUtils;
import com.gettec.fsnip.fsn.util.sales.SalesUtil;
import com.gettec.fsnip.fsn.vo.sales.DetailAlbumVO;
import com.gettec.fsnip.fsn.vo.sales.PhotoFieldVO;
import com.gettec.fsnip.fsn.vo.sales.PhotosAlbumVO;
import com.gettec.fsnip.fsn.vo.sales.ViewReportVO;

/**
 * 相册dao层实现
 * @author tangxin 2015/04/24
 *
 */
@Repository(value = "photosAlbumsDAO")
public class PhotosAlbumsDAOImpl extends BaseDAOImpl<PhotosAlbums>
		implements PhotosAlbumsDAO {

	/**
	 * 根据相册名称统计相册中图片的数量
	 * @author tangxin 2015-05-05
	 */
	@Override
	public long countByName(Long organization, String name) throws DaoException{
		try{
			if(organization == null || name == null) {
				return 0l;
			}
			String sql = "SELECT count(sres.id) FROM t_bus_photos_albums ph " +
					"LEFT JOIN t_sales_resource sres ON ph.guid = sres.guid " +
					"WHERE ph.organization = ?1 and ph.`name` = ?2 " +
					"and ph.del_status = 0 and sres.del_status = 0";
			return this.countBySql(sql, new Object[]{organization, name});
		}catch(Exception e){
			throw new DaoException(e.getMessage(), e);
		}
	}
	
	/**
	 * 获取相册列表
	 * @author tangxin 2015-05-05
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DetailAlbumVO> getListAlbumsByPage(Long organization, String name, int page, int pageSize, String cut) throws DaoException{
		try{
			List<DetailAlbumVO> listAlbumVO = new  ArrayList<DetailAlbumVO>();
			if(organization == null || name == null) {
				return listAlbumVO;
			}
			
			String sql = "SELECT ph.id,sres.file_name,sres.url,ph.description FROM t_bus_photos_albums ph " +
					"LEFT JOIN t_sales_resource sres ON ph.guid = sres.guid "+
					"WHERE ph.organization = ?1 and ph.`name` = ?2 and sres.del_status = 0 " +
					"and ph.del_status = 0 order by sres.cover desc,sres.id asc";
			Query query = this.entityManager.createNativeQuery(sql);
			if(page > 0 && pageSize > 0) {
				page = (page - 1) * pageSize;
				query.setFirstResult(page);
				query.setMaxResults(pageSize);
			} 
			query.setParameter(1, organization);
			query.setParameter(2, name);
			List<Object[]> listObjs = query.getResultList();
			return createDetailAlbumVO(listObjs, cut);
		}catch(Exception e){
			throw new DaoException(e.getMessage(), e);
		}
	}
	
	/**
	 * 创建 相册结果集 List<DetailAlbumVO>
	 * @author tangxin 2015-05-05
	 */
	private List<DetailAlbumVO> createDetailAlbumVO(List<Object[]> listObjs, String cut) {
		List<DetailAlbumVO> listVO = new ArrayList<DetailAlbumVO>();
		if(listObjs == null || listObjs.size() < 1) {
			return listVO;
		}
		Map<String, Integer> cutMap = SalesUtil.getCutWidthAndHeight(cut);
		int fieldType = SalesUtil.PHOTO_FIELD_TYPE_BUSALBUM;
		for(Object[] objs : listObjs){
			DetailAlbumVO albumVO = new DetailAlbumVO();
			albumVO.setProductId(0L);
			albumVO.setId((objs[0] != null ? Long.parseLong(objs[0].toString()) : null));
			albumVO.setImgName(objs[1] != null ? objs[1].toString() : null);
			String imgUrl = (objs[2] != null ? objs[2].toString() : "");
			String desc = (objs[3] != null ? objs[3].toString() : "");
			albumVO.setImgUrl(imgUrl);
			if(cutMap != null) {
				albumVO.setThumbnailUrl(ImgUtils.getImgPath(imgUrl, cutMap.get("width"), cutMap.get("height")));
			}
			PhotoFieldVO fieldVO = new PhotoFieldVO(null,null,null,null,null,null,null,null,null,null,0l,0l,fieldType,desc);
			albumVO.setField(fieldVO);
			listVO.add(albumVO);
		}
		return listVO;
	}
	
	/**
	 * 封装 ViewReportVO
	 * @param listObjs List<Object[]>
	 * @return ViewReportVO
	 * @author tangxin 2015-05-06
	 */
	private ViewReportVO createViewReportVO(List<Object[]> listObjs) {
		if(listObjs == null || listObjs.size() < 1){
			return null;
		}
		Object[] objs = listObjs.get(0);
		ViewReportVO reportVO = new ViewReportVO();
		reportVO.setReportId(objs[0] != null ? Long.parseLong(objs[0].toString()) : null);
		reportVO.setProductName(objs[1] != null ? objs[1].toString() : null);
		reportVO.setProductFormat(objs[2] != null ? objs[2].toString() : null);
		reportVO.setProductDesc(objs[3] != null ? objs[3].toString() : null);
		reportVO.setSelfNumber(objs[4] != null ? Long.parseLong(objs[4].toString()) : 0l);
		reportVO.setCensorNumber(objs[5] != null ? Long.parseLong(objs[5].toString()) : 0l);
		reportVO.setSampleNumber(objs[6] != null ? Long.parseLong(objs[6].toString()) : 0l);
		reportVO.setPdfPath(objs[7] != null ? objs[7].toString() : null);
		reportVO.setReportType(objs[8] != null ? objs[8].toString() : null);
		reportVO.setReportNo(objs[9] != null ? objs[9].toString() : null);
		return reportVO;
	}
	
	/**
	 * 根据产品id获取产品最新的自检报告以及 各类型的报告数量
	 * @author tangxin 2015-05-06
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ViewReportVO getViewReport(Long productId) throws DaoException{
		try{
			if(productId == null) {
				return null;
			}
			String sql = "SELECT tm4.id,pd.`name`,pd.format,pd.des,tm1.slfcon,tm2.censcon,tm3.smpcon,tm4.fullPdfPath,tm4.test_type,tm4.service_order FROM product pd " +
					"LEFT JOIN (SELECT pi.product_id,count(tr.id) 'slfcon' FROM product_instance pi " +
					"LEFT JOIN test_result tr ON pi.id = tr.sample_id " +
					"where pi.product_id = :productId AND tr.publish_flag = 1 and tr.test_type = '企业自检') tm1 ON pd.id = tm1.product_id " +
					"LEFT JOIN " +
					"(SELECT pi.product_id,count(tr.id) 'censcon' FROM product_instance pi " +
					"LEFT JOIN test_result tr ON pi.id = tr.sample_id " +
					"where pi.product_id = :productId AND tr.publish_flag = 1 and tr.test_type = '企业送检') tm2 ON pd.id = tm2.product_id " +
					"LEFT JOIN " +
					"(SELECT pi.product_id,count(tr.id) 'smpcon' FROM product_instance pi " +
					"LEFT JOIN test_result tr ON pi.id = tr.sample_id " +
					"where pi.product_id = :productId AND tr.publish_flag = 1 and tr.test_type = '政府抽检') tm3 ON pd.id = tm3.product_id " +
					"LEFT JOIN " +
					"(SELECT pi.product_id,pi.production_date,tr.fullPdfPath,tr.test_type,tr.id,tr.service_order FROM product_instance pi " +
					"LEFT JOIN test_result tr ON pi.id = tr.sample_id " +
					"WHERE pi.product_id = :productId AND tr.publish_flag = 1 and tr.test_type = '企业送检' " +
					"ORDER BY pi.production_date DESC LIMIT 0,1) tm4 ON pd.id = tm4.product_id " +
					"where pd.id = :productId";
			List<Object[]>  listObjs = this.entityManager.createNativeQuery(sql)
				.setParameter("productId", productId).getResultList();
			return createViewReportVO(listObjs);
		}catch(Exception e){
			throw new DaoException(e.getMessage(), e);
		}
	}
	
	
	/**
	 * 封装 ViewReportVO
	 * @param listObjs List<Object[]>
	 * @return ViewReportVO
	 * @author tangxin 2015-05-06
	 */
	private ViewReportVO createViewReportVO1(List<Object[]> listObjs) {
		if(listObjs == null || listObjs.size() < 1)return null;
		Object[] objs = listObjs.get(0);
		ViewReportVO reportVO = new ViewReportVO();
		reportVO.setReportId(objs[0] != null ? Long.parseLong(objs[0].toString()) : null);
		reportVO.setProductName(objs[1] != null ? objs[1].toString() : null);
		reportVO.setProductFormat(objs[2] != null ? objs[2].toString() : null);
		reportVO.setProductDesc(objs[3] != null ? objs[3].toString() : null);
		reportVO.setPdfPath(objs[4] != null ? objs[4].toString() : null);
		reportVO.setReportType(objs[5] != null ? objs[5].toString() : null);
		reportVO.setReportNo(objs[6] != null ? objs[6].toString() : null);
		reportVO.setSelfNumber(objs[7] != null ? Long.parseLong(objs[7].toString()) : 0l);
		return reportVO;
	}
	
	
	@Override
	public ViewReportVO getViewReport(Long productId, int type) throws DaoException {
		try{
			if(productId == null)return null;
			String sType = "";
			if(type == 1)//送检报告
			{
				sType="企业送检";
			}else if(type == 2){//抽检报告
				sType="政府抽检";
			}else if(type == 3){//自检报告
				sType="企业自检";
			}else{
				return null;
			}
			String sql = "SELECT tm4.id,pd.`name`,pd.format,pd.des,tm4.fullPdfPath,tm4.test_type,tm4.service_order,tm3.censcon FROM product pd " +
					
					"LEFT JOIN (SELECT pi.product_id,pi.production_date,tr.fullPdfPath,tr.test_type,tr.id,tr.service_order FROM product_instance pi " +
								"LEFT JOIN test_result tr ON pi.id = tr.sample_id " +
								"WHERE pi.product_id = :productId AND tr.publish_flag = 1 and tr.test_type = :sType " +
								"ORDER BY pi.production_date DESC LIMIT 0,1) tm4 ON pd.id = tm4.product_id " +
					
					"LEFT JOIN (SELECT pro.id,count(tr.id) 'censcon' FROM product pro " +
									"LEFT JOIN product_instance pi ON pro.id = pi.product_id " +
									"LEFT JOIN test_result tr ON pi.id = tr.sample_id " +
									"WHERE pi.product_id = :productId  AND tr.publish_flag = 1 and tr.test_type = :sType GROUP BY pro.id) tm3 ON pd.id = tm3.id "+
					"where pd.id = :productId";
			@SuppressWarnings("unchecked")
			List<Object[]>  listObjs = this.entityManager.createNativeQuery(sql)
				.setParameter("productId", productId).setParameter("sType", sType).getResultList();
			return createViewReportVO1(listObjs);
		}catch(Exception e){
			throw new DaoException(e.getMessage(), e);
		}
	}
	
	/**
	 * 根据企业组织机构和相册名称查询
	 * @author tangxin 2015-05-06
	 */
	@Override
	public PhotosAlbumVO findByOrganizationAndName(Long organization, String name) throws DaoException{
		try{
			if(organization == null || name == null) {
				return null;
			}
			PhotosAlbumVO albumVO = null;
			String condition = " WHERE e.organization = ?1 AND e.name = ?2 ";
			List<PhotosAlbums> albums = getListByCondition(condition, new Object[]{organization,name});
			if(albums != null && albums.size() > 0){
				albumVO = new PhotosAlbumVO();
				albumVO.setName(albums.get(0).getName());
				albumVO.setGuid(albums.get(0).getGuid());
				albumVO.setId(albums.get(0).getId());
				albumVO.setDescribe(albums.get(0).getDescription());
			}
			return albumVO;
		}catch(JPAException jpae){
			throw new DaoException(jpae.getMessage(),jpae);
		}
	}

	/**
	 * 根据销售案例id 获取企业的销售案例和销售案例图片集
	 * @author HY
	 * Create Date 2015-05-07
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DetailAlbumVO> getListAlbumsByBusId(Long id, String cut) throws DaoException {
			List<DetailAlbumVO> listAlbumVO = new  ArrayList<DetailAlbumVO>();
			if(id == null) {
				return listAlbumVO;
			}
			String sql = "SELECT sc.id,sres.file_name,sres.url,sc.description FROM t_bus_sales_case sc "+
			" LEFT JOIN t_sales_resource sres ON sc.guid = sres.guid "+
			" WHERE sc.id = ?1 AND sres.del_status = 0 "+
			" AND sc.del_status = 0 ORDER BY sres.cover DESC,sres.id ASC";
			List<Object[]> listObjs = this.entityManager.createNativeQuery(sql)
					.setParameter(1,id)
					.getResultList();
		return createDetailAlbumVO(listObjs, cut);
	}
	
	/**
	 * 统计企业其他认证相册数量，包括qs证
	 * @author tangxin 2015-05-21
	 */
	@Override
	@SuppressWarnings("unchecked")
	public long countCertification(Long businessId) throws DaoException{
		try{
			long count = 0;
			String sql = "SELECT t1.cont1,t2.cont2 from (SELECT count(b2p.qs_id) 'cont1' FROM businessunit_to_prolicinfo b2p " +
					"WHERE b2p.business_id = :busId AND b2p.del = 0) t1, " +
					"(SELECT count(bc.id) 'cont2' FROM business_certification bc " +
					"LEFT JOIN certification crt ON bc.cert_id = crt.id " +
					"WHERE bc.business_id = :busId AND crt.std_status = 0) t2";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter("busId", businessId);
			List<Object[]> listObj = query.getResultList();
			if(listObj != null && listObj.size() > 0){
				Object[] objs = listObj.get(0);
				count = ( objs[0] != null ? Long.parseLong(objs[0].toString()) : 0 ) + 
						( objs[1] != null ? Long.parseLong(objs[1].toString()) : 0 );
			}
			return count;
		}catch(Exception e){
			throw new DaoException(e.getMessage(), e);
		}
	}
	
	/**
	 * 分页查询企业其他认证相册数量，包括qs证
	 * @author tangxin 2015-05-21
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getListCertification(Long businessId, int page, int pageSize) throws DaoException{
		
		String description = "国家对于具备某种产品的生产条件并能保证产品质量的企业,依法授予的许可生产该项产品的凭证。";
		try{
			String sql="SELECT t.`no`,t.`name`,t.enddate,t.FILE_NAME,t.URL,t.description FROM "+
					"(SELECT '' AS 'no',crt.`name`,bc.enddate,res.FILE_NAME,res.URL,res.RESOURCE_ID,crt.description FROM business_certification bc "+
					"LEFT JOIN certification crt ON bc.cert_id = crt.id "+
					"LEFT JOIN t_test_resource res ON bc.resource_id = res.RESOURCE_ID "+
					"WHERE bc.business_id = :busId AND crt.std_status = 0 "+
					"UNION "+
					"(SELECT pli.qs_no 'no','生产许可证' AS 'name',pli.end_time 'endDate',res.FILE_NAME,res.URL,res.RESOURCE_ID,'"+description+"' AS description FROM businessunit_to_prolicinfo b2p "+
					"LEFT JOIN production_license_info pli ON b2p.qs_id = pli.id "+
					"LEFT JOIN productionlicenseinfo_to_resource p2r ON pli.id = p2r.qs_id "+ 
					"LEFT JOIN t_test_resource res ON p2r.resource_id = res.RESOURCE_ID "+
					"WHERE b2p.business_id = :busId AND b2p.del = 0)) t ORDER BY t.RESOURCE_ID DESC";
			Query query = entityManager.createNativeQuery(sql);
			if(page > 0){
				page = (page - 1) * pageSize;
				query.setFirstResult(page);
				query.setMaxResults(pageSize);
			}
			query.setParameter("busId", businessId);
			return query.getResultList();
		}catch(Exception e){
			throw new DaoException(e.getMessage(), e);
		}
	}

	
}