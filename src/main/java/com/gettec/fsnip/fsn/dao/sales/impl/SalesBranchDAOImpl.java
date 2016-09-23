package com.gettec.fsnip.fsn.dao.sales.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.sales.SalesBranchDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.sales.SalesBranch;
import com.gettec.fsnip.fsn.vo.sales.SalesBranchVO;

@Repository(value="salesBranchDAO")
public class SalesBranchDAOImpl  extends BaseDAOImpl<SalesBranch> implements  SalesBranchDAO {
	
	/**
     * 根据筛选条件统计销售网点信息数量
     * @author tangxin 2015-04-27
     */
	@Override
	public long countByConfigure(Long organization, String configure) throws DaoException {
		try{
			configure = (configure == null ? " WHERE 1=1 " : configure);
			configure = configure + " and branch.organization = ?1 and branch.del_status = 0 ";
			String sql = "select count(branch.id) from t_bus_sales_branch branch " + configure;
			return this.countBySql(sql, new Object[]{organization});
		}catch(JPAException jpae) {
			throw new DaoException(jpae.getMessage(), jpae.getException());
		}
	}

	/**
	 * 根据销售网点名称统计数量，验证名称时候重复用
	 * @author tangxin 2015-04-29
	 */
	@Override
	public long countByName(String name, Long organization, Long id) throws DaoException {
		try{
			String condition = "";
			Object[] params = null;
			if(id != null && id > 0) {
				condition = " and branch.id != ?3 ";
				params = new Object[]{name, organization, id};
			} else {
				params = new Object[]{name, organization};
			}
			String sql = "select count(branch.id) from t_bus_sales_branch branch where branch.name = ?1 " +
					"and branch.organization = ?2 and branch.del_status = 0 " + condition;
			return this.countBySql(sql, params);
		}catch(JPAException jpae) {
			throw new DaoException(jpae.getMessage(), jpae.getException());
		}
	}
	
	/**
	 * 根据筛选条件分页查询销售网点信息
	 * @author tangxin 2015-04-27
	 */
	@Override
	public List<SalesBranchVO> getListByOrganizationWithPage(Long organization, String condition, 
			Integer page, Integer pageSize) throws DaoException {
		try{
			condition = (condition == null ? " WHERE 1=1 " : condition);
			condition += " and branch.organization = ?1 and branch.delStatus = 0 ";
			String jpql = " select new com.gettec.fsnip.fsn.vo.sales.SalesBranchVO(branch) from " +
					"com.gettec.fsnip.fsn.model.sales.SalesBranch branch ";
			jpql = jpql + condition;
			return this.getListByJPQL(SalesBranchVO.class, jpql, page, pageSize, new Object[]{organization});
		}catch(JPAException jpae){
			throw new DaoException(jpae.getMessage(), jpae);
		}
	}
	
	/**
	 * 根据地址类型  和 关键字 获取销售网点二级地址的列表
	 * type 
	 *   00  获取当前企业下所有销售网点所在的省份
	 *   01  获取指定省份下销售网点所在的市级城市
	 *   02  获取指定市级城市下销售网点所在的县级城市
	 *   03  获取指定省份下销售网点所在的县级城市
	 * @author tangxin 2015-04-28
	 */
	@Override
	public List<String> getListAddrByType(Long organization,String keyword, String type) throws DaoException {
		try{
			List<String> result = null;
			if(keyword == null || type == null) {
				return result;
			}
			String selectCol = "branch.addr_province";
			String conStr = " branch.organization = ?1";
			String filter = "北京市;天津市;上海市;重庆市";
			if(filter.contains(keyword)){
				type = "03";
			}
			Object[] params = null;
			/* 00 获取企业下销售网点的省级城市 */
			if("00".equals(type)) {
				params = new Object[]{organization};
			}else{
				params = new Object[]{organization, "%"+keyword+"%"};
			}
			/* 01 获取企业下销售网点的市级城市 */
			if("01".equals(type)) {
				selectCol = "branch.addr_city";
				conStr += " and branch.addr_province like ?2";
			}else if("02".equals(type)) {  /* 02获取企业下销售网点的县级城市 */
				selectCol = "branch.addr_counties";
				conStr += " and branch.addr_city like ?2";
			}else if("03".equals(type)){
				selectCol = "branch.addr_counties";
				conStr += " and branch.addr_province like ?2";
			}
			conStr += " and branch.del_status = 0 ";
			String sql = "SELECT DISTINCT " + selectCol + " FROM t_bus_sales_branch branch WHERE " + conStr;
			List<String> listAddr = this.getListBySQLWithoutType(String.class, sql, params);
			return listAddr;
		}catch(Exception e){
			throw new DaoException(e.getMessage(), e);
		}
	}
	
	/**
	 * 获取当前登陆企业 指定省份下的销售网点信息(销售网点模块，预览地图时加载)
	 * @author tangxin 2015/6/15
	 */
	@Override
	public List<SalesBranchVO> getListAddrByProvince(Long organization,String province) throws DaoException {
		try{
			String jpql = " select new com.gettec.fsnip.fsn.vo.sales.SalesBranchVO(branch) from " +
					"com.gettec.fsnip.fsn.model.sales.SalesBranch branch where branch.organization = ?1 and branch.delStatus = 0 and branch.province like ?2";
			return this.getListByJPQL(SalesBranchVO.class, jpql, -1, -1, new Object[]{organization, "%" + province + "%"});
		}catch(Exception e){
			throw new DaoException(e.getMessage(), e);
		}
	}
}
