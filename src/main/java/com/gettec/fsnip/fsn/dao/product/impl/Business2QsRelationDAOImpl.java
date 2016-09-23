package com.gettec.fsnip.fsn.dao.product.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.product.Business2QsRelationDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.vo.product.ProductQSVO;

/**
 * 企业-qs号关系  DAO层实现类
 * @author ZhangHui 2015/5/15
 */
@Repository(value="business2QsRelationDAO")
public class Business2QsRelationDAOImpl implements Business2QsRelationDAO {
	@PersistenceContext private EntityManager entityManager;
	
	/**
	 * 功能描述：分页查询当前登录企业下的qs号，用于在qs权限设置页面展示
	 * @author ZhangHui 2015/5/15
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProductQSVO> getListOfQsByPage(long organization, int local, int page, int pageSize) throws DaoException {
		try {
			int begin = (page-1)*pageSize;
			
			String sql ="SELECT b2p.id,b2p.qs_id,pli.qs_no,pli.product_name,b2p.can_use,b2p.can_edit,bus.name FROM businessunit_to_prolicinfo b2p " +
						"INNER JOIN business_unit bus ON bus.id = b2p.business_id AND bus.organization = ?1 " +
						"INNER JOIN production_license_info pli ON pli.id = b2p.qs_id " +
						"WHERE b2p.`local` = ?2 AND b2p.del = 0 " +
						"ORDER BY b2p.id ASC LIMIT " + begin + "," + pageSize;
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, organization);
			query.setParameter(2, local);
			/* 数据封装 */
			List<Object[]> result = query.getResultList();
			List<ProductQSVO> vos = new ArrayList<ProductQSVO>();
			if(result != null){
				for(Object[] obj : result){
					ProductQSVO vo = new ProductQSVO(((BigInteger)obj[0]).longValue(),
							((BigInteger)obj[1]).longValue(),
							obj[2]==null?"":obj[2].toString(), 
							obj[3]==null?"":obj[3].toString(),
							Integer.parseInt(String.valueOf(obj[4].toString())),
							Integer.parseInt(String.valueOf(obj[5].toString())),
							obj[6]==null?"":obj[6].toString());
					vos.add(vo);
				}
			}
			return vos;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("Business2QsRelationDAOImpl.getListOfQsByPage() 出现异常！", e);
		}
	}

	/**
	 * 功能描述：查找当前企业自己的qs号总数量
	 * @param local
	 * 			0 代表qs号为不是当前企业自己的qs号
	 *          1 代表qs号是当前企业自己的qs号
	 * @author ZhangHui 2015/5/15
	 */
	@Override
	public long countOfMyOwn(long organization) {
		try {
			String sql ="SELECT COUNT(*) FROM businessunit_to_prolicinfo b2p " +
						"INNER JOIN business_unit bus ON bus.id = b2p.business_id AND bus.organization = ?1 " +
						"WHERE b2p.`local` = 1 AND b2p.del = 0";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, organization);
			
			return Long.parseLong(query.getSingleResult().toString());
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 功能描述：查找可以对当前qs号有编辑权限的被授权企业数量
	 * @author Zhanghui 2015/5/28
	 */
	@Override
	public long countOfBeAuthorizedCanEdit(Long qsId) throws DaoException {
		try {
			if(qsId == null){
				throw new Exception("参数为空");
			}
			
			String sql ="SELECT COUNT(*) FROM businessunit_to_prolicinfo b2p " +
						"WHERE b2p.qs_id = ?1 AND b2p.`local` = 0 AND b2p.can_edit = 1 AND b2p.del = 0";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, qsId);
			
			return Long.parseLong(query.getSingleResult().toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("Business2QsRelationDAOImpl.countOfBeAuthorizedCanEdit() 出现异常！", e);
		}
	}

	/**
	 * 功能描述：查找当前qs号被授权的企业
	 * @param local
	 * 			0 代表qs号为不是当前企业自己的qs号
	 *          1 代表qs号是当前企业自己的qs号
	 * @author ZhangHui 2015/5/15
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProductQSVO> getListOfQs(Long qsId, int local) throws DaoException {
		try {
			if(qsId == null){
				return null;
			}
			
			String sql ="SELECT b2p.id,b2p.can_use,b2p.can_edit,bus.`name`,b2p.business_id FROM businessunit_to_prolicinfo b2p " +
						"INNER JOIN business_unit bus ON bus.id = b2p.business_id " +
						"WHERE b2p.qs_id = ?1 AND b2p.`local` = ?2 AND b2p.del = 0 " +
						"ORDER BY b2p.id DESC";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, qsId);
			query.setParameter(2, local);
			/* 数据封装 */
			List<Object[]> result = query.getResultList();
			List<ProductQSVO> vos = new ArrayList<ProductQSVO>();
			if(result != null){
				for(Object[] obj : result){
					ProductQSVO vo = new ProductQSVO(((BigInteger)obj[0]).longValue(), 
							Integer.parseInt(String.valueOf(obj[1].toString())),
							Integer.parseInt(String.valueOf(obj[2].toString())), 
							obj[3]==null?"":obj[3].toString(),((BigInteger)obj[4]).longValue());
					vos.add(vo);
				}
			}
			return vos;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("Business2QsRelationDAOImpl.getListOfQs() 出现异常！", e);
		}
	}

	/**
	 * 功能描述：新增一条qs授权许可
	 * @author Zhanghui 2015/5/18
	 */
	@Override
	public boolean create(Long business_id, Long qs_id, int local, int can_use, int can_edit) {
		try {
			if(business_id==null || qs_id==null){
				return false;
			}
			
			String sql ="INSERT INTO businessunit_to_prolicinfo(business_id,qs_id,local,can_use,can_edit) VALUES(?1,?2,?3,?4,?5)";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, business_id);
			query.setParameter(2, qs_id);
			query.setParameter(3, local);
			query.setParameter(4, can_use);
			query.setParameter(5, can_edit);
			
			query.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 功能描述：编辑一条qs授权许可
	 * @author Zhanghui 2015/5/19
	 */
	@Override
	public boolean updateOfRight(Long id, int can_use, int can_edit) {
		try {
			if(id == null){
				return false;
			}
			
			String sql ="UPDATE businessunit_to_prolicinfo SET can_use = ?1, can_edit = ?2, del = 0 WHERE id = ?3";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, can_use);
			query.setParameter(2, can_edit);
			query.setParameter(3, id);
			
			query.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 功能描述：编辑一条 企业-qs 关系
	 * @author Zhanghui 2015/5/22
	 */
	@Override
	public boolean updateOfRight(Long id, int local, int can_use, int can_edit) {
		try {
			if(id == null){
				return false;
			}
			
			String sql ="UPDATE businessunit_to_prolicinfo SET local = ?1, can_use = ?2, can_edit = ?3, del = 0 WHERE id = ?4";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, local);
			query.setParameter(2, can_use);
			query.setParameter(3, can_edit);
			query.setParameter(4, id);
			
			query.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 功能描述：收回不是该qs号的主企业的编辑权
	 * @author Zhanghui 2015/5/28
	 */
	@Override
	public boolean updateOfReclaimRights(Long qsId) {
		try {
			if(qsId == null){
				return false;
			}
			
			String sql ="UPDATE businessunit_to_prolicinfo SET can_edit = 2 " +
						"WHERE qs_id = ?1 AND local = 0 AND del = 0 AND can_use = 1 AND can_edit = 1";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, qsId);
			
			query.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 功能描述：查找一条qs授权许可
	 * @author Zhanghui 2015/5/18
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Long findIdByVO(ProductQSVO vo) {
		try {
			if(vo.getBusinessId()==null || vo.getQsId()==null){
				return null;
			}
			
			String sql = "SELECT id FROM businessunit_to_prolicinfo WHERE business_id = ?1 " +
						 "AND qs_id = ?2 AND `local` = ?3 AND can_use = ?4 AND can_edit = ?5 AND del = 0";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, vo.getBusinessId());
			query.setParameter(2, vo.getQsId());
			query.setParameter(3, 0);
			query.setParameter(4, vo.isCan_use()?1:2);
			query.setParameter(5, vo.isCan_eidt()?1:2);
			
			List<Object> result = query.getResultList();
			if(result!=null && result.size()>0){
				Object obj = result.get(0);
				return obj!=null?((BigInteger)obj).longValue():null;
			}
			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 功能描述：删除/恢复 一条 企业-qs 记录
	 * @param del
	 *          0 代表未删除
	 *          1 代表已删除
	 * @author Zhanghui 2015/5/22
	 * @throws DaoException 
	 */
	@Override
	public void updateByDel(long id, int del) throws DaoException {
		try {
			String sql ="UPDATE businessunit_to_prolicinfo SET del = ?1 WHERE id = ?2";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, del);
			query.setParameter(2, id);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("Business2QsRelationDAOImpl.updateByDel()-->" + e.getMessage(), e);
		}
	}
	
	/**
	 * 功能描述：批量 删除/恢复  企业-qs 关系
	 * @param local
	 *         0 代表 批量 删除/恢复  被授权企业-qs 关系
	 *         1 代表 删除/恢复 主企业-qs 关系
	 * @param del
	 * 		   0 代表 还原为未删除状态
	 * 		   1 代表 删除 
	 * @author Zhanghui 2015/5/20
	 * @throws DaoException 
	 */
	@Override
	public void updateByDel(Long qsId, int local, int del) throws DaoException {
		try {
			if(qsId == null){
				throw new Exception("参数为空");
			}
			
			String sql ="UPDATE businessunit_to_prolicinfo SET del = ?1 WHERE qs_id = ?2 AND local = ?3";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, del);
			query.setParameter(2, qsId);
			query.setParameter(3, local);
			
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("Business2QsRelationDAOImpl.updateByDel()-->" + e.getMessage(), e);
		}
	}

	/**
	 * 功能描述：查找授权qs数量
	 * @author Zhanghui 2015/5/19
	 */
	@Override
	public long count(Long qsId, Long businessId) {
		try {
			if(businessId==null || qsId==null){
				return -1;
			}
			
			String sql = "SELECT COUNT(*) FROM businessunit_to_prolicinfo WHERE business_id = ?1 AND qs_id = ?2 AND del = 0";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, businessId);
			query.setParameter(2, qsId);
			
			return Long.parseLong(query.getSingleResult().toString());
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * 功能描述：查看xx企业有无xxqs号的使用权
	 * @author Zhanghui 2015/6/19
	 * @throws DaoException 
	 */
	@Override
	public long count(String qs_no, Long organization) throws DaoException {
		try {
			if(organization==null || qs_no==null || "".equals(qs_no)){
				throw new Exception("参数为空");
			}
			
			String sql = "SELECT COUNT(*) FROM businessunit_to_prolicinfo b2p " +
						 "INNER JOIN production_license_info pri ON pri.id = b2p.qs_id AND pri.qs_no = ?1 " +
						 "INNER JOIN business_unit bus ON bus.id = b2p.business_id AND bus.organization = ?2 " +
						 "WHERE b2p.del = 0";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, qs_no);
			query.setParameter(2, organization);
			
			return Long.parseLong(query.getSingleResult().toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("Business2QsRelationDAOImpl.count()-->" + e.getMessage(), e);
		}
	}
	
	/**
	 * 功能描述：查找授权qs数量
	 * @author Zhanghui 2015/5/22
	 */
	@Override
	public long count(Long qsId, int local) throws DaoException {
		try {
			if(qsId == null){
				throw new Exception("参数为空");
			}
			
			String sql ="SELECT COUNT(*) FROM businessunit_to_prolicinfo b2p " +
						"WHERE b2p.qs_id = ?1 AND b2p.`local` = ?2 AND b2p.del = 0";
			Query query = entityManager.createNativeQuery(sql);
			
			query.setParameter(1, qsId);
			query.setParameter(2, local);
			
			return Long.parseLong(query.getSingleResult().toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("Business2QsRelationDAOImpl.count() " + e.getMessage(), e);
		}
	}
	
	/**
	 * 功能描述：获取当前qs的主企业id
	 * @author Zhanghui 2015/5/22
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String getOwnerBusNameOfQs(Long qsId) throws DaoException {
		try {
			if(qsId == null){
				throw new Exception("参数为空");
			}
			
			String sql ="SELECT bus.name FROM businessunit_to_prolicinfo b2p " +
						"INNER JOIN business_unit bus ON bus.id = b2p.business_id " +
						"WHERE b2p.qs_id = ?1 AND b2p.`local` = 1 AND b2p.del = 0";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, qsId);

			List<Object> result = query.getResultList();
			if(result.size() > 0){
				Object obj = result.get(0);
				return obj==null?"":obj.toString();
			}
			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("Business2QsRelationDAOImpl.getOwnerBusIdOfQs() " + e.getMessage(), e);
		}
	}
	
	/**
	 * 功能描述：获取当前qs的主企业id
	 * @author Zhanghui 2015/5/22
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ProductQSVO getVOOfOwnerBus(String qsno) throws DaoException {
		try {
			if(qsno==null || "".equals(qsno)){
				throw new Exception("参数为空");
			}
			
			String sql ="SELECT bus.organization,bus.name FROM businessunit_to_prolicinfo b2p " +
						"INNER JOIN production_license_info pli ON pli.id = b2p.qs_id AND pli.qs_no = ?1 " +
						"INNER JOIN business_unit bus ON bus.id = b2p.business_id " +
						"WHERE b2p.`local` = 1 AND b2p.del = 0";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, qsno);

			List<Object[]> result = query.getResultList();
			if(result.size() > 0){
				Object[] obj = result.get(0);
				ProductQSVO vo = new ProductQSVO();
				vo.setOrganization(((BigInteger)obj[0]).longValue());
				vo.setBusinessName(obj[1]==null?"":obj[1].toString());
				return vo;
			}
			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("Business2QsRelationDAOImpl.getOwnerBusIdOfQs() " + e.getMessage(), e);
		}
	}
	
	/**
	 * 功能描述：查找授权qs（不考虑del）
	 * @author Zhanghui 2015/5/22
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ProductQSVO getVOWithoutDel(Long qsId, Long businessId) throws DaoException {
		try {
			if(businessId==null || qsId==null){
				throw new Exception("参数为空");
			}
			
			String sql = "SELECT id,local FROM businessunit_to_prolicinfo WHERE business_id = ?1 AND qs_id = ?2";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, businessId);
			query.setParameter(2, qsId);
			
			List<Object[]> result = query.getResultList();
			if(result.size() > 0){
				Object obj[] = result.get(0);
				ProductQSVO vo = new ProductQSVO();
				vo.setId(((BigInteger)obj[0]).longValue());
				vo.setLocal(Integer.parseInt(String.valueOf(obj[1].toString()))==0?false:true);
				return vo;
			}
			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("Business2QsRelationDAOImpl.getIdWithoutDel()" + e.getMessage(), e);
		}
	}
	
	/**
	 * 功能描述：根据企业id获取qs号
	 * @author ZhangHui 2015/5/20
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProductQSVO> getListOfQsByPage(Long organization, int page, int pageSize) throws DaoException {
		try {
			int begin = (page-1)*pageSize;
			
			String sql = "SELECT pli.id,pli.qs_no,pli.busunit_name,pli.product_name,btp.can_use,btp.can_edit,btp.`local`,pli.qsformat_id " +
						 "FROM production_license_info pli " +
						 "INNER JOIN businessunit_to_prolicinfo btp ON pli.id = btp.qs_id AND btp.del = 0 " +
						 "INNER JOIN business_unit bus ON btp.business_id = bus.id AND bus.organization = ?1 " +
						 "ORDER BY pli.id DESC LIMIT " + begin + "," + pageSize;
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, organization);
			/* 数据封装 */
			List<Object[]> result = query.getResultList();
			List<ProductQSVO> vos = new ArrayList<ProductQSVO>();
			if(result != null){
				for(Object[] obj : result){
					ProductQSVO vo = new ProductQSVO(((BigInteger)obj[0]).longValue(),
								obj[1]==null?"":obj[1].toString(), 
								obj[2]==null?"":obj[2].toString(), 
								obj[3]==null?"":obj[3].toString(), 
								Integer.parseInt(String.valueOf(obj[4].toString())), 
								Integer.parseInt(String.valueOf(obj[5].toString())),
								Integer.parseInt(String.valueOf(obj[6].toString())),
								obj[7]==null?1:(Integer.parseInt(String.valueOf(obj[7].toString()))));
					vos.add(vo);
				}
			}
			return vos;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("Business2QsRelationDAOImpl.getListOfQs() 出现异常！", e);
		}
	}

	/**
	 * 功能描述：通过对比更新前后的qs号，如果：
	 *         a. 前后相同，则不需要更新
	 *         b. 前后不同，则执行更新操作
	 * @author Zhanghui 2015/5/21
	 */
	@Override
	public boolean updateByContrast(Long bussinessId, Long qsId, Long old_edit_qsId, int local) throws DaoException {
		try {
			if(bussinessId==null || qsId==null || old_edit_qsId==null){
				throw new Exception("参数异常");
			}
			
			String sql ="UPDATE businessunit_to_prolicinfo SET qs_id = ?1, local = ?2, del = 0 WHERE business_id = ?3 AND qs_id = ?4";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, qsId);
			query.setParameter(2, local);
			query.setParameter(3, bussinessId);
			query.setParameter(4, old_edit_qsId);
			
			query.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("Business2QsRelationDAOImpl.update() " + e.getMessage(), e);
		}
	}
	
	/**
	 * 功能描述：删除/恢复 企业-qs 关系
	 * @param local
	 *         0 代表当前企业是该qs号的主人
	 *         1 代表当前企业不是该qs号的主人
	 * @param del 
	 *         0 代表 还原为未删除状态
	 * 		   1 代表 删除
	 * @author Zhanghui 2015/5/22
	 */
	@Override
	public void updateByDel(Long bussinessId, Long qsId, boolean local, int del) throws DaoException {
		try {
			if(bussinessId==null || qsId==null){
				throw new Exception("参数异常");
			}
			
			String sql ="UPDATE businessunit_to_prolicinfo SET del = ?1 ";
			
			if(local){
				sql += ", can_use = 1, can_edit = 1 WHERE qs_id = " + qsId;
			}else{
				sql += "WHERE qs_id = " + qsId + " AND local = " + local + " AND business_id = " + bussinessId;
			}
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, del);
			
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("Business2QsRelationDAOImpl.updateByDel() " + e.getMessage(), e);
		}
	}

	/**
	 * 功能描述：根据企业组织机构查找企业所有可以使用的qs号总数量
	 * @author Zhanghui 2015/5/21
	 */
	@Override
	public long countOfMyAll(Long organization) throws DaoException {
		try {
			if(organization == null){
				throw new Exception("参数为空");
			}
			
			String sql ="SELECT COUNT(*) FROM businessunit_to_prolicinfo b2p " +
						"INNER JOIN business_unit bus ON bus.id = b2p.business_id AND bus.organization = ?1 " +
						"WHERE b2p.del = 0";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, organization);
			return Long.parseLong(query.getSingleResult().toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("Business2QsRelationDAOImpl.countOfMyAll() " + e.getMessage(), e);
		}
	}

	/**
	 * 功能描述：验证当前企业是否为该qs号的主人，如果不是主人，是否有编辑权
	 * @author Zhanghui 2015/5/28
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ProductQSVO find(Long bussinessId, Long qsId) throws DaoException {
		try {
			if(bussinessId==null || qsId==null){
				throw new Exception("参数为空");
			}
			
			String sql = "SELECT local,can_edit FROM businessunit_to_prolicinfo WHERE business_id = ?1 AND qs_id = ?2 AND del = 0";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, bussinessId);
			query.setParameter(2, qsId);

			List<Object[]> result = query.getResultList();
			if(result.size() > 0){
				Object[] obj = result.get(0);
				ProductQSVO vo = new ProductQSVO();
				int local = Integer.parseInt(String.valueOf(obj[0].toString()));
				vo.setLocal(local==1?true:false);
				int can_edit = Integer.parseInt(String.valueOf(obj[1].toString()));;
				if(!vo.isLocal() && can_edit==1){
					vo.setCan_eidt(true);
				}
				return vo;
			}
			
			throw new Exception("没有找到任何一条记录");
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("Business2QsRelationDAOImpl.findLocal() " + e.getMessage(), e);
		}
	}

	/**
	 * 功能描述：根据组织机构id和qs号，获取一条 企业-qs 关系
	 * @author Zhanghui 2015/5/25
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ProductQSVO find(String qsno, Long organization) throws DaoException {
		try {
			if("".equals(qsno) || organization==null){
				throw new Exception("参数为空");
			}
			
			String sql = "SELECT b2p.`local`,b2p.can_use,b2p.can_edit FROM businessunit_to_prolicinfo b2p " +
						 "INNER JOIN business_unit bus ON bus.id = b2p.business_id AND bus.organization = ?1 " +
						 "INNER JOIN production_license_info pin ON pin.id = b2p.qs_id AND pin.qs_no = ?2 " +
						 "WHERE b2p.del = 0";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, organization);
			query.setParameter(2, qsno);
			/* 数据封装 */
			List<Object[]> result = query.getResultList();
			if(result.size() > 0){
				Object[] obj = result.get(0);
				ProductQSVO vo = new ProductQSVO(	
						    Integer.parseInt(String.valueOf(obj[0].toString())), 
							Integer.parseInt(String.valueOf(obj[1].toString())),
							Integer.parseInt(String.valueOf(obj[2].toString())));
				return vo;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("Business2QsRelationDAOImpl.find() " + e.getMessage(), e);
		}
	}

	/**
	 * 功能描述：当qs号未被认领前，删除第一家企业-qs号关系后，查找需要将编辑权转移到下一家的 企业-qs id
	 * @author Zhanghui 2015/5/28
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Long findTargetIdOfMoveRight(Long qsId) throws DaoException {
		try {
			if(qsId==null){
				throw new Exception("参数异常");
			}
			
			String sql ="SELECT id FROM businessunit_to_prolicinfo WHERE qs_id = ?1 AND local = 0 AND del = 0 ORDER BY id LIMIT 0,1";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, qsId);
			
			List<Object> result = query.getResultList();
			if(result.size() > 0){
				Object obj = result.get(0);
				return ((BigInteger)obj).longValue();
			}
			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("Business2QsRelationDAOImpl.findTargetIdOfMoveRight() " + e.getMessage(), e);
		}
	}
	
	/**
	 * 功能描述：当qs号未被认领前，删除第一家企业-qs号关系后，需要将编辑权转移到另外一家
	 * @author Zhanghui 2015/5/28
	 */
	@Override
	public void moveRightOfCanEdit(Long targetId) throws DaoException {
		try {
			if(targetId==null){
				throw new Exception("参数异常");
			}
			
			String sql ="UPDATE businessunit_to_prolicinfo SET can_edit = 1 WHERE id = ?1";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, targetId);
			
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("Business2QsRelationDAOImpl.moveRightOfCanEdit() " + e.getMessage(), e);
		}
	}
	
	/**
	 * 获取指定企业下可以使用的QS号
	 * @author tanxin 2015-06-01
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ProductQSVO> getListCanUseByOrganization(Long organization) throws DaoException {
		try{
			if(organization == null) {
				return null;
			}
			String sql = "SELECT pli.id,pli.qs_no FROM production_license_info pli " +
					"RIGHT JOIN businessunit_to_prolicinfo b2p ON b2p.qs_id = pli.id "+
					"RIGHT JOIN business_unit bus ON b2p.business_id = bus.id " +
					"WHERE bus.organization = ?1 AND b2p.can_use != 2 and b2p.del = 0 ";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, organization);
			/* 数据封装 */
			List<Object[]> result = query.getResultList();
			List<ProductQSVO> vos = new ArrayList<ProductQSVO>();
			if(result != null){
				for(Object[] obj : result){
					ProductQSVO vo = new ProductQSVO();
					vo.setQsId(((BigInteger)obj[0]).longValue());
					vo.setQsno(obj[1]==null?"":obj[1].toString());
					vos.add(vo);
				}
			}
			return vos;
		}catch(Exception e){
			throw new DaoException(e.getMessage(), e);
		}
	}

	/**
	 * 授予/取消企业该qs号的所有权
	 * @param business_id 企业id
	 * @param qs_id
	 * @param local
	 * 			0  代表授予
	 * 			1  代表取消
	 * @author ZhangHui 2015/6/2
	 * @throws DaoException 
	 */
	@Override
	public void updateByLocal(Long business_id, Long qs_id, int local) throws DaoException {
		try {
			if(business_id==null || qs_id==null){
				throw new Exception("参数为空");
			}
			
			String sql = "UPDATE businessunit_to_prolicinfo SET local = ?1, del = 0 WHERE business_id = ?2 AND qs_id = ?3";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, local);
			query.setParameter(2, business_id);
			query.setParameter(3, qs_id);
			
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("Business2QsRelationDAOImpl.updateByLocal() " + e.getMessage(), e);
		}
	}

	/**
	 * 功能描述：根据企业名称查找一条 该企业-qs 关系
	 * @author Zhanghui 2015/6/24
	 * @throws DaoException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ProductQSVO findByBusname(String bus_name, Long qs_id) throws DaoException {
		try {
			if(bus_name==null || "".equals(bus_name) || qs_id==null){
				throw new Exception("参数为空");
			}
			
			String sql = "SELECT b2p.`local`,b2p.can_use,b2p.can_edit FROM businessunit_to_prolicinfo b2p " +
						 "INNER JOIN business_unit bus ON bus.id = b2p.business_id AND bus.name = ?1 " +
						 "WHERE b2p.del = 0 AND b2p.qs_id = ?2";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, bus_name);
			query.setParameter(2, qs_id);
			/* 数据封装 */
			List<Object[]> result = query.getResultList();
			if(result.size() > 0){
				Object[] obj = result.get(0);
				ProductQSVO vo = new ProductQSVO(	
						    Integer.parseInt(String.valueOf(obj[0].toString())), 
							Integer.parseInt(String.valueOf(obj[1].toString())),
							Integer.parseInt(String.valueOf(obj[2].toString())));
				return vo;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("Business2QsRelationDAOImpl.findByBusname() " + e.getMessage(), e);
		}
	}
}
