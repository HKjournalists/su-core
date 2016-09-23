package com.gettec.fsnip.fsn.dao.sales.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.sales.ElectronicDataDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.sales.ElectronicData;
import com.gettec.fsnip.fsn.vo.sales.MaterialsVO;

/**
 * 电子资料dao层实现
 * @author tangxin 2015/04/24
 *
 */
@Repository(value = "electronicDataDAO")
public class ElectronicDataDAOImpl extends BaseDAOImpl<ElectronicData> implements
		ElectronicDataDAO {

	/**
	 * 获取指定组织机构下的电子资料信息VO
	 * @author tangxin 2015-05-10
	 */
	@Override
	public List<MaterialsVO> getListMaterials(Long organization) throws DaoException{
		try{
			if(organization == null) {
				return null;
			}
			String sql = "SELECT t.eid,t.`name`,t.rid,t.url,t.update_time from " +
					"(SELECT ed.id 'eid',ed.`name`,sr.id 'rid',sr.url,sr.update_time FROM t_bus_electronic_data ed " +
					"LEFT JOIN t_sales_resource sr ON ed.guid = sr.guid " +
					"WHERE ed.organization = ?1 and sr.del_status = 0 order by sr.update_time desc) t ";
			List<Object[]> listObjs = getListBySQLWithPage(sql, new Object[]{organization}, -1, 0);
			return createMaterialsVO(listObjs);
		}catch(JPAException jpae){
			throw new DaoException(jpae.getMessage(), jpae);
		}
	}
	
	/**
	 * 获取指定组织机构下的电子资料信息实体
	 * @author tangxin 2015-05-10
	 */
	@Override
	public ElectronicData findByOrganization(Long organization,int type) throws DaoException{
		try{
			if(organization == null) {
				return null;
			}
			String condition = " where e.organization = ?1 and e.type=?2";
			List<ElectronicData> list = getListByCondition(condition, new Object[]{organization,type});
			return (list != null && list.size() > 0) ? list.get(0) : null;
		}catch(JPAException jpae){
			throw new DaoException(jpae.getMessage(), jpae);
		}
	}
	
	private List<MaterialsVO> createMaterialsVO(List<Object[]> listObjs){
		if(listObjs == null || listObjs.size() < 1){
			return null;
		}
		List<MaterialsVO> listVO = new ArrayList<MaterialsVO>();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		for(Object[] objs : listObjs){
			MaterialsVO vo = new MaterialsVO();
			vo.setId((objs[0] != null ? Long.parseLong(objs[0].toString()) : 0L));
			vo.setName(((objs[1] != null ? objs[1].toString() : null)));
			vo.setAttachmentsId((objs[2] != null ? Long.parseLong(objs[2].toString()) : 0L));
			vo.setAttachmentsUrl(((objs[3] != null ? objs[3].toString() : "")));
			try {
				vo.setUpdateTime(((objs[4] != null ? sf.parse(objs[4].toString()) : null)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			listVO.add(vo);
		}
		return listVO;
	}
}
