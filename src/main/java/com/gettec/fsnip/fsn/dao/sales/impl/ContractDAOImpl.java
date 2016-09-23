package com.gettec.fsnip.fsn.dao.sales.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.sales.ContractDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.sales.Contract;
import com.gettec.fsnip.fsn.vo.sales.ContractVO;
import com.gettec.fsnip.fsn.vo.sales.ContractVOAPP;

/**
 * 电子合同dao层实现
 * @author tangxin 2015/04/24
 *
 */
@Repository(value = "contractDAO")
public class ContractDAOImpl extends BaseDAOImpl<Contract> implements ContractDAO{

	/**
     * 根据筛选条件统计电子合同数量
     * @author tangxin 2015-04-29
     */
	@Override
	public long countByConfigure(Long organization, String configure) throws DaoException {
		try{
			configure = (configure == null ? " WHERE 1=1 " : configure);
			configure = configure + " and contract.organization = ?1 and contract.del_status = 0 ";
			String sql = "select count(contract.id) from t_bus_contract contract " + configure;
			return this.countBySql(sql, new Object[]{organization});
		}catch(JPAException jpae) {
			throw new DaoException(jpae.getMessage(), jpae.getException());
		}
	}

	/**
	 * 根据电子合同名称统计数量，验证名称是否重复用
	 * @author tangxin 2015-04-29
	 */
	@Override
	public long countByName(String name, Long organization, Long id) throws DaoException {
		try{
			String condition = "";
			Object[] params = null;
			if(id != null && id > 0) {
				condition = " and contract.id != ?3 ";
				params = new Object[]{name, organization, id};
			} else {
				params = new Object[]{name, organization};
			}
			String sql = "select count(contract.id) from t_bus_contract contract where contract.name = ?1 " +
					"and contract.organization = ?2 and contract.del_status = 0 " + condition;
			return this.countBySql(sql, params);
		}catch(JPAException jpae) {
			throw new DaoException(jpae.getMessage(), jpae.getException());
		}
	}
	
	/**
	 * 根据筛选条件分页查询电子合同信息
	 * @author tangxin 2015-04-29
	 */
	@Override
	public List<ContractVO> getListByOrganizationWithPage(Long organization, String condition, 
			Integer page, Integer pageSize) throws DaoException {
		try{
			condition = (condition == null ? " WHERE 1=1 " : condition);
			condition = condition.replace("update_time", "updateTime");
			condition += " and contract.organization = ?1 and contract.delStatus = 0 ";
			String jpql = " select new com.gettec.fsnip.fsn.vo.sales.ContractVO(contract) from " +
					"com.gettec.fsnip.fsn.model.sales.Contract contract ";
			jpql = jpql + condition;
			return this.getListByJPQL(ContractVO.class, jpql, page, pageSize, new Object[]{organization});
		}catch(JPAException jpae){
			throw new DaoException(jpae.getMessage(), jpae);
		}
	}
	
	/**
   	 * 企业APP 获取企业电子合同信息
   	 * @author tangxin 2015-05-10
   	 */
   	@Override
   	public List<ContractVOAPP> getListForAPPWithPage(Long organization, String configure, Integer page, Integer pageSize)
   			throws DaoException {
   		try{
   			String sql = "SELECT t.cid,t.`name`,t.rid,t.url,t.update_time from " +
   					"(SELECT cn.id 'cid',cn.`name`,sr.id 'rid',sr.url,cn.update_time FROM t_bus_contract cn " +
   					"LEFT JOIN t_sales_resource sr ON cn.guid = sr.guid WHERE cn.organization = ?1 and cn.del_status = 0 order by cn.update_time desc) t";
   			List<Object[]> listObjs = getListBySQLWithPage(sql, new Object[]{organization}, -1, 0);
   			return createContractVOAPP(listObjs);
   		}catch(JPAException jpae){
   			throw new DaoException(jpae.getMessage(), jpae);
   		}
   	}
   	
   	/**
   	 * 从 List<Object[]> 转 ContractVOAPP
   	 * @author tangxin 2015-05-10
   	 */
   	private List<ContractVOAPP> createContractVOAPP(List<Object[]> listObjs){
   		if(listObjs == null || listObjs.size() < 1){
			return null;
		}
		List<ContractVOAPP> listVO = new ArrayList<ContractVOAPP>();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		for(Object[] objs : listObjs){
			ContractVOAPP vo = new ContractVOAPP();
			vo.setId((objs[0] != null ? Long.parseLong(objs[0].toString()) : 0L));
			vo.setName(((objs[1] != null ? objs[1].toString() : null)));
			vo.setAttachmentsId((objs[2] != null ? Long.parseLong(objs[2].toString()) : 0L));
			vo.setAttachmentsUrl((objs[3] != null ? objs[3].toString() : ""));
			try {
				vo.setUpdateTime((objs[4] != null ? sf.parse(objs[4].toString()) : null));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			listVO.add(vo);
		}
		return listVO;
	}
   	
}
