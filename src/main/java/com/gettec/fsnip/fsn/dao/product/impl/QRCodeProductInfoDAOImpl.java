package com.gettec.fsnip.fsn.dao.product.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.product.QRCodeProductInfoDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.product.QRCodeProductInfo;

/**
 * 二维码产品信息DAO层接口实现
 * @author TangXin
 *
 */
@Repository(value="qRCodeProductInfoDAO")
public class QRCodeProductInfoDAOImpl extends BaseDAOImpl<QRCodeProductInfo>
	implements QRCodeProductInfoDAO{

	/**
	 * 根据产品ID获取当前产品的二维码信息
	 * @param productId 产品ID
	 * @return QRCodeProductInfo 二维码产品信息
	 * @throws DaoException dao异常
	 */
	@Override
	public QRCodeProductInfo findByProductId(Long productId) throws DaoException{
		try{
			List<QRCodeProductInfo> listResult = this.getListByCondition(" where e.product.id = ?1 ", new Object[]{productId});
			if(listResult!=null&&listResult.size()>0){
				return listResult.get(0);
			}
			return null;
		}catch(JPAException jpae){
			throw new DaoException("QRCodeProductInfoDAOImpl.findByProductId()->"+jpae.getMessage(),jpae);
		}
	}
	
	/**
	 * 根据组织机构id获取当前企业下产品最大流水号
	 * @param organization
	 * @return
	 * @throws DaoException
	 * @author TangXin
	 */
	@Override
	public long getMaxSerialNumberByOrganization(Long organization) throws DaoException{
		try{
			String sql="SELECT MAX(qpi.serial_number) FROM qrcode_product_info qpi WHERE qpi.product_id IN " +
					"(SELECT id FROM product pro WHERE pro.organization= ?1)";
			List<Object> result = this.getListBySQLWithoutType(null, sql, new Object[]{organization});
			if(result!=null&&result.size()>0){
				Object obj = result.get(0);
				return obj==null?0L:Long.parseLong(obj.toString());
			}
			return 0L;
		}catch(JPAException jpae){
			throw new DaoException("QRCodeProductInfoDAOImpl.getMaxSerialNumberByOrganization()->"+jpae.getMessage(),jpae);
		}
	}
	
	/**
	 * 验证产品内部码是否重复，针对同一个企业内部验证。
	 * @param innderCode
	 * @param organization
	 * @param productId
	 * @return true:验证通过  false：验证失败
	 * @throws DaoException
	 * @author TangXin
	 */
	@Override
	public boolean validateInnerCode(String innderCode, Long organization, Long productId) throws DaoException{
		try{
			String condition = "";
			Object[] params = null;
			if(productId == null){
				condition = " where e.product.organization = ?1 and e.innerCode= ?2 ";
				params = new Object[]{organization,innderCode};
			}else{
				condition = " where e.product.organization = ?1 and e.innerCode= ?2 and e.product.id != ?3 ";
				params = new Object[]{organization,innderCode,productId};
			}
			List<QRCodeProductInfo> result = getListByCondition(condition, params);
			if(result==null||result.size()<1) return true; //验证通过，产品内部码不重复
			return false; //验证不通过，产品内部码重复
		}catch(JPAException jpae){
			throw new DaoException("QRCodeProductInfoDAOImpl.validateInnerCode()->"+jpae.getMessage(),jpae);
		}
	}
}
