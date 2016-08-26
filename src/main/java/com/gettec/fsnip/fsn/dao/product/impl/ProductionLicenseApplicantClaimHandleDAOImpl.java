package com.gettec.fsnip.fsn.dao.product.impl;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.product.ProductionLicenseApplicantClaimHandleDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.ProductionLicenseApplicantClaimHandle;
import com.gettec.fsnip.fsn.vo.product.ProductionLicenseApplicantClaimHandleVO;

/**
 * 
 * @author ZhangHui 2015/5/29
 */
@Repository(value="productLicenseOfApplicantClaimHandleDAO")
public class ProductionLicenseApplicantClaimHandleDAOImpl extends BaseDAOImpl<ProductionLicenseApplicantClaimHandle>
		implements ProductionLicenseApplicantClaimHandleDAO {
	
	@PersistenceContext private EntityManager entityManager;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	/**
	 * 查找一条未过期的企业申请认领qs的记录
	 * @param qs_id         // 需要申请认领qsId
	 * @param currentBusId  // 提出申请认领企业id
	 * @author ZhangHui 2015/6/1 
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ProductionLicenseApplicantClaimHandleVO findOfNotOverdue(Long qs_id, Long applicantBusId) throws DaoException {
		try {
			if(qs_id==null || applicantBusId==null){
				throw new Exception("参数为空");
			}
			
			String sql = "SELECT qac.id,qac.applicant,qac.applicant_time,qac.handle_time,qac.handle_result,qac.note FROM prolicinfo_qs_applicant_claim qac " +
						 "INNER JOIN production_license_info_applicant_claim_backup pli ON pli.id = qac.qs_id_applicant_claim_backup AND pli.qs_id = ?1 " +
						 "WHERE qac.business_id = ?2 AND qac.handle_result <> 8";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, qs_id);
			query.setParameter(2, applicantBusId);

			List<Object[]> result = query.getResultList();
			if(result.size() > 0){
				Object[] obj = result.get(0);
				
				ProductionLicenseApplicantClaimHandleVO vo = new ProductionLicenseApplicantClaimHandleVO(
						((BigInteger)obj[0]).longValue(),
						obj[1]==null?"":obj[1].toString(), 
						obj[2]==null?"":(sdf.format((Date)obj[2])),
						obj[3]==null?"":(sdf.format((Date)obj[3])), 
						Integer.parseInt(String.valueOf(obj[4].toString())),
						obj[5]==null?"":obj[5].toString());
				
				return vo;
			}
			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("ProductionLicenseApplicantClaimHandleDAOImpl.findOfNotOverdue() " + e.getMessage(), e);
		}
	}
	
	/**
	 * 功能描述：查找一条未过期的企业申请认领qs的记录
	 * @param qs_no         // 需要申请认领qs号
	 * @param organization  // 提出申请认领企业组织机构
	 * @author ZhangHui 2015/6/19
	 * @throws ServiceException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ProductionLicenseApplicantClaimHandleVO findOfNotOverdue(String qs_no, Long organization) throws DaoException {
		try {
			if(organization==null || qs_no==null || "".equals(qs_no)){
				throw new Exception("参数为空");
			}
			
			String sql = "SELECT qac.id,qac.applicant,qac.applicant_time,qac.handle_time,qac.handle_result,qac.note " +
						 "FROM prolicinfo_qs_applicant_claim qac " +
						 "INNER JOIN production_license_info_applicant_claim_backup pli ON pli.id = qac.qs_id_applicant_claim_backup " +
						 "INNER JOIN production_license_info pri ON pri.id = pli.qs_id AND pri.qs_no = ?1 " +
						 "INNER JOIN business_unit bus ON bus.id = qac.business_id AND bus.organization = ?2 " +
						 "WHERE qac.handle_result <> 8";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, qs_no);
			query.setParameter(2, organization);

			List<Object[]> result = query.getResultList();
			if(result.size() > 0){
				Object[] obj = result.get(0);
				
				ProductionLicenseApplicantClaimHandleVO vo = new ProductionLicenseApplicantClaimHandleVO(
						((BigInteger)obj[0]).longValue(),
						obj[1]==null?"":obj[1].toString(), 
						obj[2]==null?"":(sdf.format((Date)obj[2])),
						obj[3]==null?"":(sdf.format((Date)obj[3])), 
						Integer.parseInt(String.valueOf(obj[4].toString())),
						obj[5]==null?"":obj[5].toString());
				
				return vo;
			}
			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("ProductionLicenseApplicantClaimHandleDAOImpl.findOfNotOverdue() " + e.getMessage(), e);
		}
	}
	
	/**
	 * 设置申请过期
	 * @author ZhangHui 2015/6/1
	 * @throws DaoException 
	 */
	@Override
	public void setApplicantOverdue(Long id) throws DaoException {
		try {
			if(id==null){
				throw new Exception("参数异常");
			}
			
			String sql = "UPDATE prolicinfo_qs_applicant_claim SET handle_result = 8 WHERE id = ?1";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, id);
			
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("ProductionLicenseApplicantClaimHandleDAOImpl.setApplicantOverdue() " + e.getMessage(), e);
		}
	}

	/**
	 * 功能描述：执行处理操作
	 * @param applicantId 当前正在审核的申请记录id
	 * @param pass
	 * 			true:  通过审核
	 * 			false: 未通过审核
	 * @param note 处理意见
	 * @author Zhanghui 2015/6/1
	 * @throws DaoException 
	 */
	@Override
	public void executeProcess(Long applicantId, boolean pass, String note, String handler) throws DaoException {
		try {
			if(applicantId==null){
				throw new Exception("参数异常");
			}
			
			String sql = "UPDATE prolicinfo_qs_applicant_claim SET handle_result = ?1,handle_time = now(),note = ?2,`handler` = ?3  WHERE id = ?4";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, pass?2:4);
			query.setParameter(2, note);
			query.setParameter(3, handler);
			query.setParameter(4, applicantId);
			
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("ProductionLicenseApplicantClaimHandleDAOImpl.executeProcess() " + e.getMessage(), e);
		}
	}

	/**
	 * 功能描述：根据该处理结果查询企业认领申请列表
	 * @param handle_result
	 * 				1 代表待处理
	 * 				2 代表审核通过
	 * 				4 代表未通过审核
	 * 				8 代表申请过期
	 * @author ZhangHui 2015/6/2
	 * @throws DaoException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProductionLicenseApplicantClaimHandleVO> getListByPage(int handle_result, int page, int pageSize) throws DaoException {
		try {
			int begin = (page-1)*pageSize;
			
			String sql = "SELECT pqa.id,pqa.qs_id_applicant_claim_backup,pla.qs_id,pli.qs_no,pla.busunit_name,pqa.applicant,pqa.applicant_time,pqa.handle_result,pqa.handle_time,pqa.note " +
						 "FROM prolicinfo_qs_applicant_claim pqa " +
						 "INNER JOIN production_license_info_applicant_claim_backup pla ON pla.id = pqa.qs_id_applicant_claim_backup " +
						 "INNER JOIN production_license_info pli ON pli.id = pla.qs_id " +
						 "WHERE pqa.handle_result = ?1 LIMIT " + begin + "," + pageSize;
		
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, handle_result);
	
			List<Object[]> result = query.getResultList();
			List<ProductionLicenseApplicantClaimHandleVO> vos = new ArrayList<ProductionLicenseApplicantClaimHandleVO>();
			if(result!=null && result.size()>0){
				for(Object[] obj : result){
					ProductionLicenseApplicantClaimHandleVO vo = new ProductionLicenseApplicantClaimHandleVO(
							((BigInteger)obj[0]).longValue(),
							((BigInteger)obj[1]).longValue(),
							((BigInteger)obj[2]).longValue(),
							obj[3]==null?"":obj[3].toString(),
							obj[4]==null?"":obj[4].toString(),
							obj[5]==null?"":obj[5].toString(),
							obj[6]==null?"":(sdf.format((Date)obj[6])),
							Integer.parseInt(String.valueOf(obj[7].toString())),
							obj[8]==null?"":(sdf.format((Date)obj[8])),
							obj[9]==null?"":obj[9].toString()
							);
					
					vos.add(vo);
				}
			}
			
			return vos;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("ProductionLicenseApplicantClaimHandleDAOImpl.getListByPage() " + e.getMessage(), e);
		}
	}

	/**
	 * 功能描述：根据处理结果查询企业认领申请数量
	 * @param handle_result
	 * 				1 代表待处理
	 * 				2 代表审核通过
	 * 				4 代表未通过审核
	 * 				8 代表申请过期
	 * @author ZhangHui 2015/6/2
	 * @throws DaoException 
	 */
	@Override
	public long count(int handle_result) throws DaoException {
		try {
			String sql = "SELECT COUNT(*) FROM prolicinfo_qs_applicant_claim WHERE handle_result = ?1";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, handle_result);
			
			return Long.parseLong(query.getSingleResult().toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("ProductionLicenseApplicantClaimHandleDAOImpl.count() " + e.getMessage(), e);
		}
	}

	/**
	 * 功能描述：获取当前登录企业 qs认领申请处理消息数量
	 * @author ZhangHui 2015/11
	 * @throws DaoException 
	 */
	@Override
	public long countWithoutOverdue(Long organization) throws DaoException {
		try {
			if(organization == null){
				throw new Exception();
			}
			
			String sql = "SELECT COUNT(*) FROM prolicinfo_qs_applicant_claim pqa " +
						 "INNER JOIN business_unit bus ON bus.id = pqa.business_id AND bus.organization = ?1 " +
						 "WHERE handle_result IN(1,2,4)";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, organization);
			
			return Long.parseLong(query.getSingleResult().toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("ProductionLicenseApplicantClaimHandleDAOImpl.countWithoutOverdue() " + e.getMessage(), e);
		}
	}

	/**
	 * 功能描述：获取当前登录企业 qs认领申请处理消息
	 * @author ZhangHui 2015/6/11
	 * @throws DaoException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProductionLicenseApplicantClaimHandleVO> getListWithoutOverdueByPage(
			Long organization, int page, int pageSize) throws DaoException {
		try {
			int begin = (page-1)*pageSize;
			
			String sql = "SELECT pqa.id,pli.qs_no,pqa.applicant,pqa.applicant_time,pqa.handle_time,pqa.handle_result,pqa.note " +
					 "FROM prolicinfo_qs_applicant_claim pqa " +
					 "INNER JOIN business_unit bus ON bus.id = pqa.business_id AND bus.organization = ?1 " +
					 "INNER JOIN production_license_info_applicant_claim_backup pla ON pla.id = pqa.qs_id_applicant_claim_backup " +
					 "INNER JOIN production_license_info pli ON pli.id = pla.qs_id " +
					 "WHERE handle_result IN(1,2,4) " +
					 "ORDER BY pqa.applicant_time DESC LIMIT " + begin + "," + pageSize;
		
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, organization);
	
			List<Object[]> result = query.getResultList();
			List<ProductionLicenseApplicantClaimHandleVO> vos = new ArrayList<ProductionLicenseApplicantClaimHandleVO>();
			if(result!=null && result.size()>0){
				for(Object[] obj : result){
					ProductionLicenseApplicantClaimHandleVO vo = new ProductionLicenseApplicantClaimHandleVO(
							((BigInteger)obj[0]).longValue(),
							obj[1]==null?"":obj[1].toString(),
							obj[2]==null?"":obj[2].toString(),
							obj[3]==null?"":(sdf.format((Date)obj[3])),
							obj[4]==null?"":(sdf.format((Date)obj[4])),
							Integer.parseInt(String.valueOf(obj[5].toString())),
							obj[6]==null?"":obj[6].toString()
							);
					
					vos.add(vo);
				}
			}
			
			return vos;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("ProductionLicenseApplicantClaimHandleDAOImpl.getListWithoutOverdueByPage() " + e.getMessage(), e);
		}
	}

	/**
	 * 功能描述：根据qs_id，批量将所有待处理的申请认领设置为退回状态
	 * @author ZhangHui 2015/6/18
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getIdsByHandleResult(Long qs_id, int handle_result) throws DaoException {
		try {
			if(qs_id == null){
				throw new Exception("参数为空");
			}
			
			String sql = "SELECT pqa.id FROM prolicinfo_qs_applicant_claim pqa " +
						 "INNER JOIN production_license_info_applicant_claim_backup pab ON pqa.qs_id_applicant_claim_backup = pab.id AND pab.qs_id = ?1 " +
						 "INNER JOIN production_license_info pri ON pri.id = ?2 " +
						 "WHERE pqa.handle_result = ?3";
		
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, qs_id);
			query.setParameter(2, qs_id);
			query.setParameter(3, handle_result);
	
			List<Object> result = query.getResultList();
			List<Long> ids = new ArrayList<Long>();
			if(result!=null && result.size()>0){
				for(Object obj : result){
					if(obj != null){
						Long id = ((BigInteger)obj).longValue();
						ids.add(id);
					}
				}
			}
			
			return ids;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("ProductionLicenseApplicantClaimHandleDAOImpl.getIdsByHandleResult() " + e.getMessage(), e);
		}
	}

	/**
	 * 功能描述：批量将idStrs申请认领设置为退回状态
	 * @author ZhangHui 2015/6/18
	 * @throws DaoException
	 */
	@Override
	public void batchSetBackByQsidstrs(String idStrs, String handler, String back_msg) throws DaoException {
		try {
			if(idStrs==null || "".equals(idStrs)){
				return;
			}
			
			String sql = "UPDATE prolicinfo_qs_applicant_claim " +
						 "SET handle_result = 4,handle_time = now(),note = ?1,`handler` = ?2 " +
						 "WHERE id IN " + idStrs;
		
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, back_msg);
			query.setParameter(2, handler);
	
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("ProductionLicenseApplicantClaimHandleDAOImpl.batchSetBackByQsidstrs() " + e.getMessage(), e);
		}
	}
}