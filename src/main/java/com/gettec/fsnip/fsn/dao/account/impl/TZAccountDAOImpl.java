package com.gettec.fsnip.fsn.dao.account.impl;

import com.gettec.fsnip.fsn.dao.account.TZAccountDAO;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.account.TZAccount;
import com.gettec.fsnip.fsn.util.sales.SalesUtil;
import com.gettec.fsnip.fsn.vo.account.*;

import org.springframework.stereotype.Repository;

import javax.persistence.Query;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Account customized operation implementation
 *
 * @author chenxiaolin
 */
@Repository(value = "tZAccountDAO")
public class TZAccountDAOImpl extends BaseDAOImpl<TZAccount> implements TZAccountDAO {

	static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat SDFTIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 根据企业关系加载对应的企业信息List
	 *
	 * @param myBusId 本企业id
	 * @param type    企业关系 0：供应，1：销售 Create Author HY Date 2015-05-17
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BusRelationVO> loadTZBusRelation(Long myBusId, int type, int page, int pageSize) throws DaoException {
		String sql = "SELECT bunit.id,bunit.name,bunit.license_no,brel.type,brel.create_time FROM business_unit bunit "
				+ " LEFT JOIN tz_business_relation brel ON bunit.id = brel.business_id "
				+ " WHERE  brel.type = ?1 AND brel.self_business_id = ?2 GROUP BY bunit.id";
		try {
			Query query = entityManager.createNativeQuery(sql.toString()).setParameter(1, type).setParameter(2, myBusId);
			if (page > 0) {
				query.setFirstResult((page - 1) * pageSize);
				query.setMaxResults(pageSize);
			}
			List<Object[]> result = query.getResultList();
			return encapsulationResultToBusRelationVO(result);
		} catch (Exception e) {
			throw new DaoException("TZBusinessRelationDAOImpl.loadTZBusRelation() 根据企业关系加载对应的企业信息,出现异常！", e);
		}
	}

	/**
	 * 封装供销企业信息
	 *
	 * @throws ParseException
	 * @author HY
	 */
	private List<BusRelationVO> encapsulationResultToBusRelationVO(
			List<Object[]> result) throws ParseException {
		List<BusRelationVO> lists = null;
		if (result != null && result.size() > 0) {
			lists = new ArrayList<BusRelationVO>();
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = result.get(i);
				BusRelationVO vo = new BusRelationVO();
				vo.setId(obj[0] != null ? Long.valueOf(obj[0].toString()) : -1);
				vo.setBusName(obj[1] != null ? obj[1].toString() : "");
				vo.setLicNo(obj[2] != null ? obj[2].toString() : "");
				vo.setType(obj[3] != null ? Integer.parseInt(obj[3].toString()) : -1);
				vo.setCreateDate(obj[4] != null ? obj[4].toString() : null);
				lists.add(vo);
			}
		}
		return lists;
	}

	/**
	 * 根据企业关系加载对应的企业信息的总条数
	 *
	 * @author HY
	 */
	@Override
	public Long loadTZBusRelationToatl(Long myBusId, int type)
			throws DaoException {
		String sql = "select count(DISTINCT bunit.id) from business_unit bunit " +
				"LEFT JOIN tz_business_relation brel ON bunit.id = brel.business_id " +
				" WHERE  brel.type = ?1 AND brel.self_business_id = ?2 ";
		try {
			return this.countBySql(sql, new Object[]{type, myBusId});
		} catch (JPAException jpae) {
			throw new DaoException("TZBusinessRelationDAOImpl.loadTZBusRelation() 根据企业关系加载对应的企业信息的总条数,出现异常！", jpae.getException());
		}
	}

	/**
	 * 统计该企业可以退货的产品品种数量
	 */
	@Override
	public Long getTZReturnProductTotal(Long org) throws DaoException {
		String sql = "SELECT count(DISTINCT p.id) from product p " +
				" LEFT JOIN product_instance ins on p.id = ins.product_id " +
				" where p.organization = ?1 ";
		try {
			return this.countBySql(sql, new Object[]{org});
		} catch (JPAException jpae) {
			throw new DaoException("TZBusinessRelationDAOImpl.getTZReturnProductTotal()统计该企业可以退货的产品品种数量，出现异常！", jpae.getException());
		}
	}

	/**
	 * 封装新增进货时选择产品
	 */
	private List<ReturnProductVO> getselectBuyGoodsListVO(List<Object[]> result) throws ParseException {
		List<ReturnProductVO> lists = null;
		if (result != null && result.size() > 0) {
			lists = new ArrayList<ReturnProductVO>();
			for (int i = 0; i < result.size(); i++) {
				Object[] objs = result.get(i);
				ReturnProductVO vo = new ReturnProductVO();
				vo.setUuid(SalesUtil.createGUID());
				vo.setProductId(Long.valueOf(objs[0].toString()));//产品ID
				vo.setName(objs[1] != null ? objs[1].toString() : "");//产品名称
				vo.setBarcode(objs[2] != null ? objs[2].toString() : "");//条形码
				vo.setFormat(objs[3] != null ? objs[3].toString() : "");//规格
				vo.setQsNumber(objs[4] != null ? objs[4].toString() : "");//QS
				vo.setPrice(new BigDecimal(0 + ""));//价格
				String expDay = objs[6] != null ? objs[6].toString() : "";//有效期天数
				vo.setReturnCount(0l);
				vo.setCount(0l);//进货时不考虑库存
				List<ProductionDateVO> birthDateList = getBirthDateByProductId(vo.getProductId(), expDay, 0);
				if (birthDateList != null && birthDateList.size() > 0) {
					vo.setProductionDate(birthDateList.get(0).getBirthDate());
					vo.setBatch(birthDateList.get(0).getBatch());
					vo.setOverDate(birthDateList.get(0).getOverDate());
				}
				vo.setBirthDateList(birthDateList);
				lists.add(vo);
			}
		}
		return lists;
	}

	/**
	 * 根据产品id加载实例的生产日期---进货--批发
	 *
	 * @param type 0表示非生产企业或其他  1表示生产企业
	 * @throws ParseException
	 * @throws NumberFormatException
	 */
	@SuppressWarnings("unchecked")
	private List<ProductionDateVO> getBirthDateByProductId(Long productId, String day, int type) throws NumberFormatException, ParseException {
		StringBuilder sb = new StringBuilder();
		String sqlStr1 = "SELECT proinst.id,proinst.production_date,proinst.batch_serial_no,pro.expiration_date from product pro " +
				" RIGHT JOIN product_instance proinst on proinst.product_id = pro.id " +
				" RIGHT JOIN test_result test on test.sample_id = proinst.id and test.del = 0 where pro.id = ?1 ";
		sb.append(sqlStr1);
		if (type == 1) {
			sb.append(" AND test.publish_flag = 1 ");
		} else if (type == 9) {
			sb.append(" AND test.publish_flag not in(4,5) ");
		} else {
			sb.append(" AND test.publish_flag not in(4,5,7) ");
		}
		sb.append(" GROUP BY proinst.batch_serial_no ORDER BY proinst.production_date DESC");
		Query query = entityManager.createNativeQuery(sb.toString()).setParameter(1, productId);
		List<Object[]> result = query.getResultList();
		List<ProductionDateVO> lists = null;
		if (result != null && result.size() > 0) {
			lists = new ArrayList<ProductionDateVO>();
			for (int i = 0; i < result.size(); i++) {
				Object[] objs = result.get(i);
				ProductionDateVO vo = new ProductionDateVO();
				vo.setInstanceId(objs[0] != null ? objs[0].toString() : "");
				vo.setBatch(objs[2] != null ? objs[2].toString() : "");
				vo.setBirthDate(objs[1] != null ? objs[1].toString().substring(0, 10) : "");
				vo.setOverDate(objs[3] != null ? objs[3].toString() : "");
				if (day != null && !"".equals(day)) {
					calculationOverDate(vo, Integer.parseInt(day));
				}
				lists.add(vo);
			}
		}
		return lists;
	}


	//根据生产日期和保质期天数计算过期日期
	private void calculationOverDate(ProductionDateVO vo, int expDay) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (vo.getBirthDate() != null && !"".equals(vo.getBirthDate())) {
			Date date = sdf.parse(vo.getBirthDate());
			Calendar cl = Calendar.getInstance();
			cl.setTime(date);
			cl.add(Calendar.DATE, expDay);
			vo.setOverDate(sdf.format(cl.getTime()));
		}
	}

	@Override
	public long getTZAccountId() {
		String sql = " select MAX(id) from tz_business_account ";
		Query query = entityManager.createNativeQuery(sql.toString());
		Object result = query.getSingleResult();
		return Long.parseLong(result.toString());
	}

	@Override
	public List<TZAccount> loadTZPurchaseProduct(Long curBusId, int page, int pageSize, String number) throws DaoException {
		try {
			StringBuilder condition = new StringBuilder();
			condition.append(" WHERE in_business_id = ?1 and (type = 0 or type = 1) ");
			if (!number.equals("")) {
				condition.append(" and account_no like '%" + number + "%'");
			}
			return this.getListByPage(page, pageSize, condition.toString(), new Object[]{curBusId});
		} catch (JPAException jpae) {
			throw new DaoException("TZAccountDAOImpl.loadTZPurchaseProduct  按照当前企业Id获取进货产品时, 出现异常！", jpae.getException());
		}
	}

	@Override
	public Long getTZPurchaseProductTotal(Long curBusId, String number) throws DaoException {
		try {
			StringBuilder condition = new StringBuilder();
			condition.append(" WHERE in_business_id = ?1 and (type = 0 or type = 1) ");
			if (!number.equals("")) {
				condition.append(" and account_no like '%" + number + "%'");
			}
			return this.count(condition.toString(), new Object[]{curBusId});
		} catch (JPAException jpae) {
			throw new DaoException("TZAccountDAOImpl.getTZPurchaseProductTotal  按照当前企业Id获取进货产品总数时, 出现异常！", jpae.getException());
		}
	}

	@Override
	public TZAccount findByIdAndOutStatus(long id, int outStatus) throws DaoException {
		try {
			String condition = " WHERE id = ?1 and out_status = ?2 ";
			List<TZAccount> list = this.getListByCondition(condition, new Object[]{id, outStatus});
			if (list.size() > 0) {
				return list.get(0);
			}
			return null;
		} catch (JPAException jpae) {
			throw new DaoException("TZAccountDAOImpl.findByIdAndOutStatus  按照当前企业Id获取进货产品总数时, 出现异常！", jpae.getException());
		}
	}

	/**
	 * 显示批发台账首页数据
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PurchaseAccountVO> loadTZWholeSaleProduct(Long org, int page, int pageSize, String number, String licOrName, int status) throws DaoException {
		try {
			String staString = "";
			if (status == 1) {
				staString = " tz.out_status = 1) ";
			} else {
				staString = " outBus.organization = " + org + " and (tz.out_status = 1 or tz.out_status = 0 )) ";
			}
			StringBuilder sql = new StringBuilder();
			String sqlStr = " select temp.* from ( select tz.id id,tz.account_no no,inBus.name inName,inBus.license_no licNo," +
					" tz.create_time time,outBus.name outName,tz.out_status outstatus,outBus.license_no,tz.in_status instatus,tz.return_status from business_unit outBus " +
					" LEFT JOIN tz_business_account tz on tz.out_business_id = outBus.id " +
					" LEFT JOIN business_unit inBus on inBus.id = tz.in_business_id where " +
					staString + " temp where 1=1 ";
			sql.append(sqlStr);
			if (!number.equals("")) {
				sql.append(" and temp.no like '%" + number + "%'");
			}
			if (!licOrName.equals("")) {
				sql.append(" and (temp.inName like '%" + licOrName + "%' or temp.licNo like '%" + licOrName + "%')");
			}
			if (status == 1) {
				sql.append(" order by temp.instatus asc,temp.id desc ");
			} else {
				sql.append(" order by temp.outstatus asc,temp.time desc ");
			}
			Query query = entityManager.createNativeQuery(sql.toString());
			if (page > 0 && pageSize > 0) {
				query.setFirstResult((page - 1) * pageSize);
				query.setMaxResults(pageSize);
			}
			List<Object[]> result = query.getResultList();
			return receiptVO2(result);
		} catch (Exception jpae) {
			throw new DaoException("TZAccountDAOImpl.loadTZWholeSaleProduct  按照当前企业Id获取批发台账时, 出现异常！", jpae);
		}
	}

	/**
	 * 封装批发台账首页信息
	 */
	private List<PurchaseAccountVO> receiptVO2(List<Object[]> result) throws ParseException {
		List<PurchaseAccountVO> list = null;
		if (result != null && result.size() > 0) {
			list = new ArrayList<PurchaseAccountVO>();
			for (int i = 0; i < result.size(); i++) {
				Object[] objs = result.get(i);
				PurchaseAccountVO vo = new PurchaseAccountVO();
				vo.setId(Long.valueOf(objs[0].toString()));
				vo.setNum(new Date().getTime() + "");
				vo.setInBusName(objs[2] != null ? objs[2].toString() : "");
				vo.setLicNo(objs[3] != null ? objs[3].toString() : "");
				vo.setCreateDate(objs[4] != null ? SDFTIME.format(SDFTIME.parse(objs[4].toString())) : "");
				vo.setOutBusName(objs[5] != null ? objs[5].toString() : "");
				vo.setOutStatus(objs[6] != null ? Integer.parseInt((objs[6].toString())) : 0);
				vo.setBuylicNo(objs[7] != null ? objs[7].toString() : "");
				vo.setInStatus(objs[8] != null ? Integer.parseInt((objs[8].toString())) : 0);
				vo.setReturnStatus(objs[9] != null ? Integer.parseInt((objs[9].toString())) : 0);
				list.add(vo);
			}
		}
		return list;
	}

	private List<PurchaseAccountVO> receiptVO3(List<Object[]> result) throws ParseException {
		List<PurchaseAccountVO> list = null;
		if (result != null && result.size() > 0) {
			list = new ArrayList<PurchaseAccountVO>();
			for (int i = 0; i < result.size(); i++) {
				Object[] objs = result.get(i);
				PurchaseAccountVO vo = new PurchaseAccountVO();
				vo.setId(Long.valueOf(objs[0].toString()));
				vo.setNum(new Date().getTime() + "");
				vo.setInBusName(objs[2] != null ? objs[2].toString() : "");
				vo.setLicNo(objs[3] != null ? objs[3].toString() : "");
				vo.setCreateDate(objs[4] != null ? SDFTIME.format(SDFTIME.parse(objs[4].toString())) : "");
				vo.setOutBusName(objs[5] != null ? objs[5].toString() : "");
				vo.setOutStatus(objs[6] != null ? Integer.parseInt((objs[6].toString())) : 0);
				vo.setBuylicNo(objs[7] != null ? objs[7].toString() : "");
				vo.setInStatus(objs[8] != null ? Integer.parseInt((objs[8].toString())) : 0);
				vo.setReturnStatus(objs[9] != null ? Integer.parseInt((objs[9].toString())) : 0);
				list.add(vo);
			}
		}
		return list;
	}
	/**
	 * 获取批发台账首页总记录数
	 */
	public Long getTZWholeSaleProductTotal(Long org, String number, String licOrName, int status) throws DaoException {
		try {
			String staString = "";
			if (status == 1) {
				staString = " tz.out_status = 1) ";
			} else {
				staString = " outBus.organization = " + org + " and (tz.out_status = 1 or tz.out_status = 0 )) ";
			}
			StringBuilder sql = new StringBuilder();
			String sqlStr = " select count(*) from ( select tz.id id,tz.account_no no,inBus.name inName,inBus.license_no licNo,  " +
					" tz.create_time time,outBus.name outName,tz.out_status outstatus from business_unit outBus  " +
					" LEFT JOIN tz_business_account tz on tz.out_business_id = outBus.id " +
					" LEFT JOIN business_unit inBus on inBus.id = tz.in_business_id  " +
					" where " + staString + " temp where 1=1 ";
			sql.append(sqlStr);
			if (!number.equals("")) {
				sql.append(" and temp.no like '%" + number + "%'");
			}
			if (!licOrName.equals("")) {
				sql.append(" and (temp.inName like '%" + licOrName + "%' or temp.licNo like '%" + licOrName + "%')");
			}
			Query query = entityManager.createNativeQuery(sql.toString());
			return Long.valueOf(query.getSingleResult().toString());
		} catch (Exception jpae) {
			throw new DaoException("TZAccountDAOImpl.getTZReceiptGoodsTotal  按照当前企业Id获取批发台账总数时, 出现异常！", jpae);
		}
	}

	/**
	 * 按照当组织机构ID获取收货台账首页数据
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PurchaseAccountVO> loadTZReceiptGoods(Long org, int page, int pageSize, String number, String licOrName) throws DaoException {
		try {
			StringBuilder sql = new StringBuilder();
			String sqlStr = " select temp.* from (select tz.id tzId,tz.account_no tzNo,outBus.name outName,outBus.license_no outLic," +
					" tz.create_time time,inbus.name inBus,tz.in_status inStatus,tz.type type,inBus.license_no from business_unit inBus LEFT JOIN tz_business_account tz on " +
					" tz.in_business_id = inbus.id LEFT JOIN business_unit outBus on outBus.id = tz.out_business_id where " +
					" inBus.organization = ?1 and tz.out_status = 1 and tz.type = 0) temp where 1 = 1 ";
			sql.append(sqlStr);
			if (!number.equals("")) {
				sql.append(" and temp.tzNo like '%" + number + "%'");
			}
			if (!licOrName.equals("")) {
				sql.append(" and (temp.outName like '%" + licOrName + "%' or temp.outLic like '%" + licOrName + "%')");
			}
			sql.append(" order by temp.inStatus asc,temp.tzId desc ");
			Query query = entityManager.createNativeQuery(sql.toString()).setParameter(1, org);
			if (page > 0 && pageSize > 0) {
				query.setFirstResult((page - 1) * pageSize);
				query.setMaxResults(pageSize);
			}
			List<Object[]> result = query.getResultList();
			return receiptVO(result, "receipteGoods");
		} catch (Exception jpae) {
			throw new DaoException("TZAccountDAOImpl.loadTZReceiptGoods  按照当组织机构ID获取收货台账时, 出现异常！", jpae);
		}
	}

	/**
	 * 封装进货以及收货时的台账首页信息
	 */
	private List<PurchaseAccountVO> receiptVO(List<Object[]> result, String type) throws ParseException {
		List<PurchaseAccountVO> list = null;
		if (result != null && result.size() > 0) {
			list = new ArrayList<PurchaseAccountVO>();
			for (int i = 0; i < result.size(); i++) {
				Object[] objs = result.get(i);
				PurchaseAccountVO vo = new PurchaseAccountVO();
				vo.setId(Long.valueOf(objs[0].toString()));
				vo.setNum(new Date().getTime() + "");
				vo.setOutBusName(objs[2] != null ? objs[2].toString() : "");
				vo.setLicNo(objs[3] != null ? objs[3].toString() : "");
				vo.setCreateDate(objs[4] != null ? SDFTIME.format(SDFTIME.parse(objs[4].toString())) : "");
				vo.setInBusName(objs[5] != null ? objs[5].toString() : "");
				vo.setInStatus(objs[6] != null ? Integer.parseInt((objs[6].toString())) : 0);
				vo.setBuylicNo(objs[8] != null ? objs[8].toString() : "");
				list.add(vo);
			}
		}
		return list;
	}

	/**
	 * 按照当组织机构ID获取收货台账总数
	 */
	public Long getTZReceiptGoodsTotal(Long org, String number, String licOrName) throws DaoException {
		try {
			StringBuilder sql = new StringBuilder();
			String sqlStr = " select count(*) from (select tz.id tzId,tz.account_no tzNo,outBus.name outName,outBus.license_no outLic," +
					" tz.create_time time,inbus.name inBus,tz.in_status inStatus,tz.type type from business_unit inBus LEFT JOIN tz_business_account tz on " +
					" tz.in_business_id = inbus.id LEFT JOIN business_unit outBus on outBus.id = tz.out_business_id where " +
					" inBus.organization = ?1 and tz.out_status = 1 and tz.type = 0) temp where 1 = 1 ";
			sql.append(sqlStr);
			if (!number.equals("")) {
				sql.append(" and temp.tzNo like '%" + number + "%'");
			}
			if (!licOrName.equals("")) {
				sql.append(" and (temp.outName like '%" + licOrName + "%' or temp.outLic like '%" + licOrName + "%')");
			}
			return this.countBySql(sql.toString(), new Object[]{org});
		} catch (JPAException jpae) {
			throw new DaoException("TZAccountDAOImpl.getTZReceiptGoodsTotal  按照当组织机构ID获取收货台账总数时, 出现异常！", jpae.getException());
		}
	}

	/**
	 * 新增进货时选择商品
	 * not in(4,5,7) 表示商超审核通过后就可以做台账
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ReturnProductVO> getselectBuyGoodsList(Long busId, int page, int pageSize, String name, String barcode) throws DaoException {
		StringBuilder sb = new StringBuilder();
		String sqlStr = " select pro.id,pro.name,pro.barcode,pro.format,prosto.qs_no," +
				" prosto.product_num,pro.expiration_date from tz_stock prosto " +
				" RIGHT JOIN product pro on pro.id = prosto.product_id" +
				" RIGHT JOIN product_instance proinst on proinst.product_id = pro.id" +
				" RIGHT JOIN test_result test on test.sample_id = proinst.id and test.del = 0" +
				" WHERE prosto.business_id = ?1";
		sb.append(sqlStr);
		if (!"".equals(name)) {
			sb.append(" AND pro.name like '%" + name + "%'");
		}
		if (!"".equals(barcode)) {
			sb.append(" AND pro.barcode like '%" + barcode + "%'");
		}
		sb.append(" AND test.publish_flag not in(4,5,7) GROUP BY pro.id,prosto.qs_no");
		try {
			Query query = entityManager.createNativeQuery(sb.toString()).setParameter(1, busId);
			if (page > 0 && pageSize > 0) {
				query.setFirstResult((page - 1) * pageSize);
				query.setMaxResults(pageSize);
			}
			List<Object[]> result = query.getResultList();
			return getselectBuyGoodsListVO(result);
		} catch (Exception e) {
			throw new DaoException("TZAccountDAOImpl.getselectBuyGoodsList()进货时选择商品时,出现异常！", e);
		}
	}

	/**
	 * 新增进货时选择商品总数
	 * not in(4,5,7) 表示商超审核通过后就可以做台账
	 */
	@Override
	public Long getselectBuyGoodsTotal(Long busId, String name, String barcode) throws DaoException {
		try {
			StringBuilder sb = new StringBuilder();
			String sqlStr = " select count(*) from ( select pro.id,pro.name,pro.barcode,pro.format,prosto.qs_no,prosto.product_num,pro.expiration_date from tz_stock prosto " +
					" RIGHT JOIN product pro on pro.id = prosto.product_id " +
					" RIGHT JOIN product_instance proinst on proinst.product_id = pro.id  " +
					" RIGHT JOIN test_result test on test.sample_id = proinst.id and test.del = 0 " +
					" WHERE prosto.business_id = ?1";
			sb.append(sqlStr);
			if (!"".equals(name)) {
				sb.append(" AND pro.name like '%" + name + "%'");
			}
			if (!"".equals(barcode)) {
				sb.append(" AND pro.barcode like '%" + barcode + "%'");
			}
			sb.append(" AND test.publish_flag not in(4,5,7) GROUP BY pro.id,prosto.qs_no ) temp");
			Query query = entityManager.createNativeQuery(sb.toString()).setParameter(1, busId);
			return Long.valueOf(query.getSingleResult().toString());
		} catch (Exception e) {
			throw new DaoException("TZAccountDAOImpl.getselectBuyGoodsTotal()进货时选择商品总数,出现异常！", e);
		}
	}

	/**
	 * 查询进货台账首页信息
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PurchaseAccountVO> searchBuyGoods(Long org, int page, int pageSize, String number, String licOrName, int status) throws DaoException {
		try {
			String staString = "";
			if (status == 1) {
				staString = " tz.type = 1 and tz.in_status=1) ";
			} else {
				staString = " inBus.organization = " + org + " and tz.type = 1 and (tz.in_status = 0 or tz.in_status=1 )) ";
			}
			StringBuilder sql = new StringBuilder();
			String sqlStr = " select temp.* from ( select tz.id id,tz.account_no no,outBus.name outName,outBus.license_no licNo," +
					" tz.create_time time,inbus.name inName,tz.in_status instatus,tz.type type,inBus.license_no  from business_unit inBus LEFT JOIN tz_business_account tz on " +
					" tz.in_business_id = inbus.id LEFT JOIN business_unit outBus on outBus.id = tz.out_business_id where " +
					staString + " temp where 1=1 ";
			sql.append(sqlStr);
			if (!"".equals(number)) {
				sql.append(" and temp.no like '%" + number + "%'");
			}
			if (!"".equals(licOrName)) {
				sql.append(" and (temp.outName like '%" + licOrName + "%' or temp.licNo like '%" + licOrName + "%')");
			}
			sql.append(" order by temp.instatus asc,temp.time desc  ");
			Query query = entityManager.createNativeQuery(sql.toString());
			if (page > 0 && pageSize > 0) {
				query.setFirstResult((page - 1) * pageSize);
				query.setMaxResults(pageSize);
			}
			List<Object[]> result = query.getResultList();
			return receiptVO(result, "buyGoods");
		} catch (Exception jpae) {
			throw new DaoException("TZAccountDAOImpl.loadTZReceiptGoods  按照当组织机构ID获取进货货台账时, 出现异常！", jpae);
		}
	}

	/**
	 * 查询进货台账首页信息总数
	 */
	@Override
	public Long getTsearchBuyGoodsTotal(Long org, String number, String licOrName, int status) throws DaoException {
		try {
			String staString = "";
			if (status == 1) {
				staString = " tz.type = 1 and tz.in_status=1 ";
			} else {
				staString = " inBus.organization = " + org + " and tz.type = 1 and (tz.in_status = 0 or tz.in_status=1 ) ";
			}
			StringBuilder sql = new StringBuilder();
			String sqlStr = " select count(*) from business_unit inBus LEFT JOIN tz_business_account tz on " +
					" tz.in_business_id = inbus.id LEFT JOIN business_unit outBus on outBus.id = tz.out_business_id where " +
					staString;
			sql.append(sqlStr);
			if (!"".equals(number)) {
				sql.append(" and tz.account_no like '%" + number + "%'");
			}
			if (!"".equals(licOrName)) {
				sql.append(" and (outBus.name like '%" + licOrName + "%' or outBus.license_no like '%" + licOrName + "%')");
			}
			return this.countBySql(sql.toString(), null);
		} catch (JPAException jpae) {
			throw new DaoException("TZAccountDAOImpl.getTZReceiptGoodsTotal  按照当组织机构ID获取进货台账总数时, 出现异常！", jpae.getException());
		}
	}

	/**
	 * 新增批发台账时选择商品--非生产企业
	 * not in(4,5,7) 表示商超审核通过后就可以做台账
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ReturnProductVO> getSaleGoodsList(Long busId, int page,
												  int pageSize, String name, String barcode) throws DaoException {
		StringBuilder sb = new StringBuilder();
		String sqlStr = "SELECT pro.id,pro.name,pro.barcode,pro.format,tzsto.qs_no, " +
				" pro.expiration_date,tzsto.product_num FROM tz_stock tzsto " +
				" RIGHT JOIN product pro on pro.id = tzsto.product_id  " +
				" RIGHT JOIN product_instance inst on inst.product_id = pro.id  " +
				" RIGHT JOIN test_result test on test.sample_id = inst.id and test.del = 0  " +
				" WHERE tzsto.business_id = ?1";
		sb.append(sqlStr);
		if (!"".equals(name)) {
			sb.append(" and pro.name like '%" + name + "%'");
		}
		if (!"".equals(barcode)) {
			sb.append(" and pro.barcode like '%" + barcode + "%'");
		}
		sb.append(" AND test.publish_flag not in(4,5,7)  GROUP BY pro.id,tzsto.qs_no");
		try {
			Query query = entityManager.createNativeQuery(sb.toString()).setParameter(1, busId);
			if (page > 0 && pageSize > 0) {
				query.setFirstResult((page - 1) * pageSize);
				query.setMaxResults(pageSize);
			}
			List<Object[]> result = query.getResultList();
			return encapsulationSaleGoodsVO(result, 0);//0：非生产企业
		} catch (Exception e) {
			throw new DaoException("TZAccountDAOImpl.getselectBuyGoodsList()进货时选择商品时,出现异常！", e);
		}
	}

	/**
	 * 封装批发的产品
	 *
	 * @type 1:生产企业 0：非生产企业
	 */
	private List<ReturnProductVO> encapsulationSaleGoodsVO(List<Object[]> result, int type) throws ParseException {
		List<ReturnProductVO> lists = null;
		if (result != null && result.size() > 0) {
			lists = new ArrayList<ReturnProductVO>();
			for (int i = 0; i < result.size(); i++) {
				Object[] objs = result.get(i);
				ReturnProductVO vo = new ReturnProductVO();
				vo.setUuid(UUID.randomUUID().toString());//一条记录的唯一标识
				vo.setProductId(Long.valueOf(objs[0].toString()));//产品ID
				vo.setName(objs[1] != null ? objs[1].toString() : "");//产品名称
				vo.setBarcode(objs[2] != null ? objs[2].toString() : "");//条形码
				vo.setFormat(objs[3] != null ? objs[3].toString() : "");//规格
				vo.setQsNumber(objs[4] != null ? objs[4].toString() : "");//QS
				String expDay = objs[5] != null ? objs[5].toString() : "";//有效期天数
				vo.setPrice(new BigDecimal(0 + ""));//价格
				vo.setReturnCount(0l);
				if (type == 1) {
					vo.setBusType(1);
				} else {
					vo.setBusType(0);
					vo.setCount(objs[6] != null ? Long.valueOf(objs[6].toString()) : new Long(0));//数量
				}
				List<ProductionDateVO> birthDateList = getBirthDateByProductId(vo.getProductId(), expDay, type);
				if (birthDateList != null && birthDateList.size() > 0) {
					vo.setProductionDate(birthDateList.get(0).getBirthDate());
					vo.setBatch(birthDateList.get(0).getBatch());
					vo.setOverDate(birthDateList.get(0).getOverDate());
				}
				vo.setBirthDateList(birthDateList);
				lists.add(vo);
			}
		}
		return lists;
	}

	/**
	 * 非生产企业的总数
	 */
	@Override
	public Long getSaleGoodsListTotal(Long busId, String name, String barcode) throws DaoException {
		StringBuilder sb = new StringBuilder();
		String sqlStr = " SELECT count(*) from ( SELECT pro.id,pro.name,pro.barcode,pro.format,tzsto.qs_no," +
				" pro.expiration_date,tzsto.product_num FROM tz_stock tzsto " +
				" RIGHT JOIN product pro on pro.id = tzsto.product_id " +
				" RIGHT JOIN product_instance inst on inst.product_id = pro.id " +
				" RIGHT JOIN test_result test on test.sample_id = inst.id and test.del = 0 " +
				" WHERE tzsto.business_id = ?1";
		sb.append(sqlStr);
		if (!"".equals(name)) {
			sb.append(" and pro.name like '%" + name + "%'");
		}
		if (!"".equals(barcode)) {
			sb.append(" and pro.barcode like '%" + barcode + "%'");
		}
		sb.append(" AND test.publish_flag not in(4,5,7) GROUP BY pro.id,tzsto.qs_no) temp");
		try {
			Query query = entityManager.createNativeQuery(sb.toString()).setParameter(1, busId);
			return Long.valueOf(query.getSingleResult().toString());
		} catch (Exception e) {
			throw new DaoException("TZAccountDAOImpl.getSaleGoodsListTotal()批发选择商品总数,出现异常！", e);
		}
	}

	/**
	 * 新增批发台账时选择商品--生产企业
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ReturnProductVO> getSaleGoodsListToSC(Long curOrg, Long busId, int page,
													  int pageSize, String name, String barcode) throws DaoException {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT p.id,p.name,p.barcode,p.format,p.qs_no,p.expiration_date,1 FROM ");
		sb.append(" (SELECT pro.id,pro.name,pro.barcode,pro.format,qs.qs_no,pro.expiration_date FROM product pro ");
		sb.append(" INNER JOIN product_instance ip ON ip.product_id = pro.id " );
		sb.append(" INNER JOIN test_result tr ON tr.sample_id = ip.id  ");
		sb.append(" LEFT JOIN product_to_businessunit pb ON pb.PRODUCT_ID=pro.id ");
		sb.append(" LEFT JOIN production_license_info qs ON qs.id=pb.qs_id ");
		sb.append(" WHERE tr.publish_flag = 1 and pro.organization = ?1");
		if (!"".equals(name)&&!"".equals(barcode)) {
			sb.append(" AND( pro.name like '%" + name + "%' OR pro.barcode like '%" + barcode + "%') ");
		}
		if (!"".equals(barcode)&&"".equals(name)) {
			sb.append(" AND pro.barcode like '%" + barcode + "%' ");
		}
		if ("".equals(barcode)&&!"".equals(name)) {
			sb.append(" AND pro.name like '%" + name + "%' ");
		}
		sb.append(" GROUP BY pro.id ) AS p ");
		try {
			Query query = entityManager.createNativeQuery(sb.toString()).setParameter(1, curOrg);
			System.out.println(sb.toString());
			if (page > 0 && pageSize > 0) {
				query.setFirstResult((page - 1) * pageSize);
				query.setMaxResults(pageSize);
			}
			List<Object[]> result = query.getResultList();
			return setSaleGoodsVO(result, 1);//生产企业
		} catch (Exception e) {
			throw new DaoException("TZAccountDAOImpl.getselectBuyGoodsList()进货时选择商品时,出现异常！", e);
		}
	}

	/**
	 * 新增批发台账时选择商品总数--生产企业
	 */
	@Override
	public Long getSaleGoodsListToSCTotal(Long curOrg, Long busId, String name, String barcode) throws DaoException {
		
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(" SELECT count(*) FROM ");
			sb.append(" (SELECT pro.id,pro.name,pro.barcode,pro.format,qs.qs_no,pro.expiration_date FROM product pro ");
			sb.append(" INNER JOIN product_instance ip ON ip.product_id = pro.id " );
			sb.append(" INNER JOIN test_result tr ON tr.sample_id = ip.id  ");
			sb.append(" LEFT JOIN product_to_businessunit pb ON pb.PRODUCT_ID=pro.id ");
			sb.append(" LEFT JOIN production_license_info qs ON qs.id=pb.qs_id ");
			sb.append(" WHERE  tr.publish_flag = 1 and pro.organization = ?1");
			if (!"".equals(name)&&!"".equals(barcode)) {
				sb.append(" AND( pro.name like '%" + name + "%' OR pro.barcode like '%" + barcode + "%') ");
			}
			if (!"".equals(barcode)&&"".equals(name)) {
				sb.append(" AND pro.barcode like '%" + barcode + "%' ");
			}
			if ("".equals(barcode)&&!"".equals(name)) {
				sb.append(" AND pro.name like '%" + name + "%' ");
			}
			sb.append(" GROUP BY pro.id ) AS p ");
			Query query = entityManager.createNativeQuery(sb.toString()).setParameter(1, curOrg);
			return Long.valueOf(query.getSingleResult().toString());
		} catch (Exception e) {
			throw new DaoException("TZAccountDAOImpl.getSaleGoodsListTotal()批发选择商品总数,出现异常！", e);
		}
	}

	@Override
	public String getBarcodeByProductId(long id) throws DaoException {
		String sql = "SELECT p.barcode FROM product p WHERE p.id = ?1";
		Query query = entityManager.createNativeQuery(sql).setParameter(1, id);
		Object bar = query.getSingleResult();
		return bar != null ? bar.toString() : "";
	}

	/**
	 * 监管首页加载数据
	 */
	@Override
	public Map<String, Map<String, Long>> loadingFDAMSDate() throws DaoException {
		Map<String, Map<String, Long>> totalMap = new HashMap<String, Map<String, Long>>();
		Map<String, Long> map1 = getTotal1();/* 供应商主体备案信息  */
		Map<String, Long> map2 = getTotal2();/* 食品条码备案信息  */
		Map<String, Long> map3 = getTotal3();/* 食品检测报告备案信息  */
		Map<String, Long> map4 = getTotal4();/* 收到台账信息  */
		totalMap.put("total1", map1);
		totalMap.put("total2", map2);
		totalMap.put("total3", map3);
		totalMap.put("total4", map4);
		return totalMap;
	}

	/**
	 * 获取本周的开始时间和结束时间
	 */
	private String[] getSetTime() throws DaoException {
		try {
			String[] week = new String[2];
			Calendar c = Calendar.getInstance();
			int weekday = c.get(7) - 1;
			c.add(5, -weekday);
			String beginTime = SDF.format(c.getTime()) + " 00:00:00";//本周开始时间(每周天 00:00:00)
			c.add(5, 6);
			String endTime = SDF.format(c.getTime()) + " 23:59:59";//本周结束时间(每周六 23:59:59)
			week[0] = beginTime;
			week[1] = endTime;
			return week;
		} catch (Exception e) {
			throw new DaoException("TZAccountDAOImpl.getSetTime()获取本周的开始时间和结束时间时,出现异常！", e);
		}
	}

	/**
	 * 供应商主体备案信息
	 */
	public Map<String, Long> getTotal1() throws DaoException {
		Map<String, Long> map = new HashMap<String, Long>();
		try {
			String[] week = getSetTime();//获取本周时间
			String beginTime = week[0];//本周开始时间
			String endTime = week[1];//本周结束时间
			String sql = " SELECT COUNT(*) FROM enterprise_registe WHERE password IS NOT NULL ";
			String sql1 = sql + " AND (enterpriteDate >= '" + beginTime + "' AND enterpriteDate <= '" + endTime + "') ";
			String sql2 = sql + " AND status like '%审核通过%' ";
			String sql3 = sql + " AND status like '%待审核%' ";
			Long t1 = getTotal(sql1);
			Long t2 = getTotal(sql2);
			Long t3 = getTotal(sql3);
			map.put("t1", t1);
			map.put("t2", t2);
			map.put("t3", t3);
			return map;
		} catch (Exception e) {
			throw new DaoException("TZAccountDAOImpl.getTotal1()查询监管系统首页食品条码备案信息时,出现异常！", e);
		}
	}

	/**
	 * 食品条码备案信息
	 */
	public Map<String, Long> getTotal2() throws DaoException {
		Map<String, Long> map = new HashMap<String, Long>();
		try {
			String[] week = getSetTime();//获取本周时间
			String beginTime = week[0];//本周开始时间
			String endTime = week[1];//本周结束时间
			String sql = " SELECT COUNT(*) FROM product WHERE 1=1 ";
			String sql1 = sql + " AND (last_modify_time >= '" + beginTime + "' AND last_modify_time <= '" + endTime + "') ";
			String sql2 = sql;
			Long t1 = getTotal(sql1);
			Long t2 = getTotal(sql2);
			map.put("t1", t1);
			map.put("t2", t2);
			return map;
		} catch (Exception e) {
			throw new DaoException("TZAccountDAOImpl.getTotal2()查询监管系统首页食品条码备案信息时,出现异常！", e);
		}
	}

	/**
	 * 食品检测报告备案信息
	 */
	public Map<String, Long> getTotal3() throws DaoException {
		Map<String, Long> map = new HashMap<String, Long>();
		try {
			String[] week = getSetTime();//获取本周时间
			String beginTime = week[0];//本周开始时间
			String endTime = week[1];//本周结束时间
			String sql = " SELECT COUNT(*) FROM test_result WHERE 1=1 ";
			String sql1 = sql + " AND (last_modify_time >= '" + beginTime + "' AND last_modify_time <= '" + endTime + "') ";
			String sql2 = sql + " AND publish_flag = 1 ";
			String sql3 = sql + " AND publish_flag = 0 ";
			String sql4 = sql + " AND publish_flag = 2 ";
			Long t1 = getTotal(sql1);
			Long t2 = getTotal(sql2);
			Long t3 = getTotal(sql3);
			Long t4 = getTotal(sql4);
			map.put("t1", t1);
			map.put("t2", t2);
			map.put("t3", t3);
			map.put("t4", t4);
			return map;
		} catch (Exception e) {
			throw new DaoException("TZAccountDAOImpl.getTotal3()查询监管系统首页食品检测报告备案信息时,出现异常！", e);
		}
	}

	/**
	 * 收到台账信息
	 */
	public Map<String, Long> getTotal4() throws DaoException {
		try {
			Map<String, Long> map = new HashMap<String, Long>();
			String sql = " SELECT COUNT(*) FROM tz_business_account WHERE out_status=1 AND (in_status !=1 OR in_status is NULL) ";
			Long t1 = getTotal(sql);
			map.put("t1", t1);
			return map;
		} catch (Exception e) {
			throw new DaoException("TZAccountDAOImpl.getTotal4()查询监管系统首页收到台账信息时,出现异常！", e);
		}
	}

	/**
	 * 公共的取得总条数的sql方法
	 */
	public Long getTotal(String sql) throws DaoException {
		try {
			Query query = entityManager.createNativeQuery(sql);
			Object total = query.getSingleResult();
			return Long.valueOf(total.toString());
		} catch (Exception e) {
			throw new DaoException("TZAccountDAOImpl.getTotal()公共的取得总条数的sql方法时,出现异常！", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public BusRelationVO findBusRelationById(long busId) throws DaoException {
		String sql = " SELECT id,name,license_no,telephone,address FROM business_unit WHERE id =?1 ";
		try {
			BusRelationVO brVO = null;
			Query query = entityManager.createNativeQuery(sql).setParameter(1, busId);
			List<Object[]> result = query.getResultList();
			if (result != null && result.size() > 0) {
				brVO = new BusRelationVO();
				Object[] obj = result.get(0);
				brVO.setId(Long.valueOf(obj[0].toString()));
				brVO.setBusName(obj[1] != null ? obj[1].toString() : "");
				brVO.setLicNo(obj[2] != null ? obj[2].toString() : "");
				brVO.setContact(obj[3] != null ? obj[3].toString() : "");
				brVO.setAddress(obj[4] != null ? obj[4].toString() : "");
			}
			return brVO;
		} catch (Exception e) {
			throw new DaoException("TZAccountDAOImpl.findBusRelationById()根据企业ID获取基本信息时,出现异常！", e);
		}
	}

	/**
	 * 根据当前组织机构获取企业ID和类型
	 */
	@SuppressWarnings("unchecked")
	public String[] getBusType(Long curOrg) throws DaoException {
		try {
			String sql = " SELECT id,type FROM business_unit WHERE organization = ?1 ";
			Query query = entityManager.createNativeQuery(sql).setParameter(1, curOrg);
			List<Object[]> result = query.getResultList();
			String[] str = null;
			if (result != null && result.size() > 0) {
				str = new String[2];
				Object[] obj = result.get(0);
				str[0] = obj[0] != null ? obj[0].toString() : "-1";
				str[1] = obj[1] != null ? obj[1].toString() : "";
			}
			return str;
		} catch (Exception e) {
			throw new DaoException("TZAccountDAOImpl.getBusType()根据组织机构Id获取企业类型,出现异常！", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String[] getBusById(Long busId) throws DaoException {
		try {
			String sql = " SELECT name,license_no FROM business_unit WHERE id = ?1 ";
			Query query = entityManager.createNativeQuery(sql).setParameter(1, busId);
			List<Object[]> result = query.getResultList();
			String[] str = new String[2];
			if (result != null && result.size() > 0) {
				Object[] obj = result.get(0);
				str[0] = obj[0].toString();
				str[1] = obj[1].toString();
			}
			return str;
		} catch (Exception e) {
			throw new DaoException("TZAccountDAOImpl.getBusById()根据Id企业信息,出现异常！", e);
		}
	}

	@Override
	public Long getOrgByBusId(Long busId) throws DaoException {
		try {
			String sql = " SELECT organization FROM business_unit WHERE id = ?1 ";
			Query query = entityManager.createNativeQuery(sql).setParameter(1, busId);
			return Long.valueOf(query.getSingleResult().toString());
		} catch (Exception e) {
			throw new DaoException("TZAccountDAOImpl.getOrgByBusId()根据企业Id获取组织机构ID,出现异常！", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ZhengFuVO> loadZFReportList(int page, int pageSize, Long org,
											String name, String batch) throws DaoException {
		try {
			StringBuilder sql = new StringBuilder();
			String sqlStr = " SELECT test.id,test.service_order,pro.name,proinst.batch_serial_no,test.test_type,test.test_date FROM test_result test  " +
					" LEFT JOIN product_instance  proinst ON test.sample_id = proinst.id " +
					" LEFT JOIN product pro ON pro.id = proinst.product_id  " +
					" WHERE test.publish_flag = 1 AND test.organization =" + org;
			sql.append(sqlStr);
			if (!"".equals(name)) {
				sql.append(" and pro.name like '%" + name + "%'");
			}
			if (!"".equals(batch)) {
				sql.append(" and proinst.batch_serial_no like '%" + batch + "%'");
			}
			Query query = entityManager.createNativeQuery(sql.toString());
			if (page > 0 && pageSize > 0) {
				query.setFirstResult((page - 1) * pageSize);
				query.setMaxResults(pageSize);
			}
			List<Object[]> result = query.getResultList();
			return getList(result);
		} catch (Exception jpae) {
			throw new DaoException("TZAccountDAOImpl.loadZFReportList()", jpae);
		}
	}

	/**
	 * 封装政府所属查看报告列表页面的VO
	 *
	 * @param result
	 * @return
	 * @throws ParseException
	 */
	private List<ZhengFuVO> getList(List<Object[]> result) throws ParseException {
		List<ZhengFuVO> list = null;
		if (result != null && result.size() > 0) {
			list = new ArrayList<ZhengFuVO>();
			for (int i = 0; i < result.size(); i++) {
				Object[] objs = result.get(i);
				ZhengFuVO vo = new ZhengFuVO();
				vo.setId(Long.valueOf(objs[0].toString()));
				vo.setServiceOrder(objs[1] != null ? objs[1].toString() : "");
				vo.setName(objs[2] != null ? objs[2].toString() : "");
				vo.setBatchSerialNo(objs[3] != null ? objs[3].toString() : "");
				vo.setTestType(objs[4] != null ? objs[4].toString() : "");
				vo.setPublishDate(objs[5] != null ? SDF.format(SDF.parse(objs[5].toString())) : "");
				list.add(vo);
			}
		}
		return list;
	}

	@Override
	public Long loadZFReportTotal(Long org, String name, String batch) throws DaoException {
		try {
			StringBuilder sql = new StringBuilder();
			String sqlStr = " SELECT count(*) FROM test_result test  " +
					" LEFT JOIN product_instance  proinst ON test.sample_id = proinst.id " +
					" LEFT JOIN product pro ON pro.id = proinst.product_id  " +
					" WHERE test.publish_flag = 1 AND test.organization =" + org;
			sql.append(sqlStr);
			if (!"".equals(name)) {
				sql.append(" and pro.name like '%" + name + "%'");
			}
			if (!"".equals(batch)) {
				sql.append(" and proinst.batch_serial_no like '%" + batch + "%'");
			}
			return this.countBySql(sql.toString(), null);
		} catch (Exception jpae) {
			throw new DaoException("TZAccountDAOImpl.loadZFReportTotal()", jpae);
		}
	}

	/**
	 * 政府查看企业注册列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ZhengFuEnterVO> loadZFEnterList(int page, int pageSize, String name, int type) throws DaoException {
		try {
			StringBuilder sql = new StringBuilder();
			String sqlStr = " SELECT id,name,type,organization FROM business_unit WHERE organization is NOT NULL AND type is not NULL" +
					" AND type !='交易市场' AND type != '仁怀市白酒生产企业' ";
			sql.append(sqlStr);
			if (!"".equals(name)) {
				sql.append(" and name like '%" + name + "%'");
			}
			if (type == 1) {
				sql.append(" and type like '%生产企业%'");
			}
			if (type == 2) {
				sql.append(" and type like '%流通企业%'");
			}
			if (type == 3) {
				sql.append(" and type like '%餐饮企业%'");
			}
			Query query = entityManager.createNativeQuery(sql.toString());
			if (page > 0 && pageSize > 0) {
				query.setFirstResult((page - 1) * pageSize);
				query.setMaxResults(pageSize);
			}
			List<Object[]> result = query.getResultList();
			return getListBusinessStaVO(result);
		} catch (Exception jpae) {
			throw new DaoException("TZAccountDAOImpl.loadZFReportList()", jpae);
		}
	}

	/**
	 * 封装政府所属查看企业注册页面的VO
	 *
	 * @param result
	 * @return
	 */
	private List<ZhengFuEnterVO> getListBusinessStaVO(List<Object[]> result) {
		List<ZhengFuEnterVO> list = null;
		if (result != null && result.size() > 0) {
			list = new ArrayList<ZhengFuEnterVO>();
			for (int i = 0; i < result.size(); i++) {
				Object[] objs = result.get(i);
				ZhengFuEnterVO vo = new ZhengFuEnterVO();
				vo.setBusinessId(objs[0] != null ? Long.valueOf(objs[0].toString()) : -1l);
				vo.setBusinessName(objs[1] != null ? objs[1].toString() : "");
				vo.setBusinessType(objs[2] != null ? objs[2].toString() : "");
				vo.setOrg(objs[3] != null ? Long.valueOf(objs[3].toString()) : -1l);
				list.add(vo);
			}
		}
		return list;
	}

	/**
	 * 政府查看企业注册总数
	 */
	@Override
	public Long loadZFEnterTotal(String name, int type) throws DaoException {
		try {
			StringBuilder sql = new StringBuilder();
			String sqlStr = " SELECT count(*) FROM business_unit WHERE organization is NOT NULL AND type is not NULL" +
					" AND type !='交易市场' AND type != '仁怀市白酒生产企业' ";
			sql.append(sqlStr);
			if (!"".equals(name)) {
				sql.append(" and name like '%" + name + "%'");
			}
			if (type == 1) {
				sql.append(" and type like '%生产企业%'");
			}
			if (type == 2) {
				sql.append(" and type like '%流通企业%'");
			}
			if (type == 3) {
				sql.append(" and type like '%餐饮企业%'");
			}
			return this.countBySql(sql.toString(), null);
		} catch (Exception jpae) {
			throw new DaoException("TZAccountDAOImpl.loadZFEnterTotal()", jpae);
		}
	}

	/**
	 * 台帐 加载生产企业自己的产品（生产企业）
	 *
	 * @author HY
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ReturnProductVO> loadSelfProductList(Long myOrg, String pname, String pbar, String type, int page, int pageSize) throws DaoException {
        /* 生产企业的情况 */
		String sql = "";
		Query query = null;
		String condition = "";
		Map<String, String> param = new HashMap<String, String>();
		if (pname != null && !"".equals(pname)) {
			condition += " AND p.name LIKE :pname ";
			param.put("pname", "%" + pname + "%");
		}
		if (pbar != null && !"".equals(pbar)) {
			condition += " AND p.barcode LIKE :pbar ";
			param.put("pbar", "%" + pbar + "%");
		}
		int busType = 0;
		if (!"".equals(type) && type.contains("生产企业")) {
			busType = 1;
			sql = "SELECT p.id,p.name,p.barcode,p.format,qs.qs_no,p.expiration_date,1 FROM product p " +
					" LEFT JOIN product_to_businessunit ptb ON ptb.PRODUCT_ID = p.id " +
					" LEFT JOIN production_license_info qs ON qs.id = ptb.qs_id " +
					" WHERE p.organization = :org AND p.barcode <> '' " +
					" AND p.barcode IS NOT NULL " + condition + " GROUP BY p.id ";
			query = entityManager.createNativeQuery(sql.toString()).setParameter("org", myOrg);
		}
        /* 供应商的情况 */
		else if (!"".equals(type) && type.contains("供应商")) {
			busType = 9;
			sql = "select p.id,p.name,p.barcode,p.format,qs.qs_no, " +
					" p.expiration_date,1 from t_meta_initialize_product init " +
					" LEFT JOIN product p ON init.product_id = p.id " +
					" LEFT JOIN product_to_businessunit ptb ON ptb.PRODUCT_ID = p.id " +
					" LEFT JOIN production_license_info qs ON qs.id = ptb.qs_id " +
					" WHERE init.organization = :org AND p.organization = :oorg AND init.`local` = 0 AND  init.del = 0 AND p.barcode <> '' " +
					condition + " AND p.barcode IS NOT NULL GROUP BY p.id ";
			query = entityManager.createNativeQuery(sql.toString()).setParameter("org", myOrg).setParameter("oorg", myOrg);
		}
		setParamtter(param, query);
		try {
			if (!"".equals(sql)) {
				if (page > 0 && pageSize > 0) {
					query.setFirstResult((page - 1) * pageSize);
					query.setMaxResults(pageSize);
				}
				List<Object[]> result = query.getResultList();
				return encapsulationSaleGoodsVO(result, busType);
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new DaoException("TZAccountDAOImpl.loadSelfProductList()出现异常！", e);
		}
	}

	@SuppressWarnings("rawtypes")
	private void setParamtter(Map<String, String> param, Query query) {
		if (param != null && param.size() > 0) {
			Iterator keyIt = param.keySet().iterator();
			do {
				String key = (String) keyIt.next();
				String value = param.get(key);
				query.setParameter(key, value);
			} while (keyIt.hasNext());
		}
	}

	/**
	 * 台帐 加载生产企业自己的产品的数量（生产企业）
	 *
	 * @author HY
	 */
	@Override
	public long loadSelfProductListTotals(Long myOrg, String pname, String pbar, String type) throws DaoException {
/* 生产企业的情况 */
		String sql = "";
		String condition = "";
		Map<String, String> param = new HashMap<String, String>();
		if (pname != null && !"".equals(pname)) {
			condition += " AND p.name LIKE :pname ";
			param.put("pname", "%" + pname + "%");
		}
		if (pbar != null && !"".equals(pbar)) {
			condition += " AND p.barcode LIKE :pbar ";
			param.put("pbar", "%" + pbar + "%");
		}
		try {
			Query query = null;
			if (!"".equals(type) && type.contains("生产企业")) {
				sql = "SELECT count(DISTINCT p.id) FROM product p " +
						" LEFT JOIN product_to_businessunit ptb ON ptb.PRODUCT_ID = p.id " +
						" LEFT JOIN production_license_info qs ON qs.id = ptb.qs_id " +
						" WHERE p.organization = :org AND p.barcode <> '' " + condition +
						" AND p.barcode IS NOT NULL ";
				query = entityManager.createNativeQuery(sql.toString()).setParameter("org", myOrg);
			}
		/* 供应商的情况 */
			else if (!"".equals(type) && type.contains("供应商")) {
				sql = "SELECT count(DISTINCT p.id) from t_meta_initialize_product init " +
						" LEFT JOIN product p ON init.product_id = p.id " +
						" LEFT JOIN product_to_businessunit ptb ON ptb.PRODUCT_ID = p.id " +
						" LEFT JOIN production_license_info qs ON qs.id = ptb.qs_id " +
						" WHERE init.organization = :org AND p.organization = :oorg " +
						" AND init.`local` = 0 AND  init.del = 0 AND p.barcode <> '' " +
						" AND p.barcode IS NOT NULL " + condition;
				query = entityManager.createNativeQuery(sql.toString()).setParameter("org", myOrg).setParameter("oorg", myOrg);
			}
			setParamtter(param, query);
			Object rtn = query.getSingleResult();
			return rtn == null ? 0 : Long.parseLong(rtn.toString());
		} catch (Exception jpae) {
			throw new DaoException("TZAccountDAOImpl.loadSelfProductListTotals()台帐 加载生产企业自己的产品的数量", jpae);
		}
	}

	/**
	 * 产品批次 = 报告批次(包括送检，抽检，自检)
	 *
	 * @param busId
	 * @param name
	 * @param barcode
	 * @user HuangYog
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ReturnProductVO> getSaleGoodsListByReportBatch(Long busId, String name, String barcode,int page, int pageSize) throws DaoException {

		try {
			StringBuilder sb = new StringBuilder();
			sb.append(" SELECT pro.id,pro.name,pro.barcode,pro.format,tzsto.qs_no,pro.expiration_date,tzsto.product_num");
			sb.append(" FROM tz_stock tzsto ");
			sb.append(" INNER JOIN product pro on pro.id = tzsto.product_id");
			sb.append(" WHERE tzsto.business_id = ?1");
			sb.append(" AND tzsto.product_num >0");
			/* 条件查询 */
			if(!"".equals(name)&&name!=null){

				sb.append(" and pro.name like '%").append(name).append("%'");
			}
			if(!"".equals(barcode)&&barcode!=null){

				sb.append(" and pro.barcode like '%").append(barcode).append("%'");
			}
			sb.append(" GROUP BY pro.id,tzsto.qs_no");


			Query query = entityManager.createNativeQuery(sb.toString());
			query.setParameter(1,busId);
			if(page > 0){
				query.setFirstResult((page - 1) * pageSize);
				query.setMaxResults(pageSize);
			}
			List<Object[]> result = query.getResultList();
			return setSaleGoodsVO(result, 0);//0：非生产企业
		} catch (Exception e) {
			throw new DaoException("TZAccountDAOImpl.getselectBuyGoodsList()进货时选择商品时,出现异常！", e);
		}
	}

	/**
	 * 判断是否是蒸馏酒;因为包就包含其中，没有保质期，都默认为白酒
	 * @param proId
	 * @return
	 */
	private boolean isJiuLei(Long proId){

		String sql = " SELECT count(*) FROM product WHERE id = ?1 AND category='1501'";//二级分类为蒸馏酒
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter(1,proId);
		return Long.parseLong(query.getSingleResult().toString())>0?true:false;
	}

	/**
	 * /**
	 * 产品批次 = 报告批次(包括送检，抽检，自检)
	 * 产品批次 = 报告批次+ 6 个月(报告过期时间) (包括送检和抽检)
	 * 的总数
	 * @param busId
	 * @param name
	 * @param barcode
	 * @return
	 * @throws DaoException
	 */
	@Override
	public Long getSaleGoodsListByReportBatchTotal(Long busId, String name, String barcode) throws DaoException {

		try {
			StringBuilder sb = new StringBuilder();
			sb.append(" SELECT count(*) FROM (");
			sb.append(" SELECT DISTINCT pro.id,tzsto.qs_no FROM tz_stock tzsto ");
			sb.append(" INNER JOIN product pro on pro.id = tzsto.product_id ");
			sb.append(" WHERE tzsto.business_id = ?1 AND tzsto.product_num >0");
			/* 条件查询 */
			if(!"".equals(name)&&name!=null){

				sb.append(" and pro.name like '%").append(name).append("%'");
			}
			if(!"".equals(barcode)&&barcode!=null){

				sb.append(" and pro.barcode like '%").append(barcode).append("%'");
			}
			sb.append(") test");
			Query query = entityManager.createNativeQuery(sb.toString());
			query.setParameter(1,busId);
			return Long.parseLong(query.getSingleResult().toString());
		} catch (Exception e) {
			throw new DaoException("TZAccountDAOImpl.getSaleGoodsListByReportBatchTotal()", e);
		}
	}

	/**
	 * 根据生产日期检验该生产日期所对应的报告是否已经存在且不过期--方法入口
	 * @param prodate
	 * @return
	 * @throws DaoException
	 */
	@Override
	public String checkReport(String prodate,Long proId) throws DaoException {

		try {

			Long exday = getExpriceDay(proId);//获取保质期
			boolean is = isJiuLei(proId);
			if(is){//如果是酒类，保质期默认为半年
				exday = 180l;
			}

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(!"".equals(prodate)&&prodate!=null){
				prodate = sdf.format(sdf.parse(prodate));
			}
			String reportId1 = checkReport1(prodate,proId,is);//第一种情况要考虑到是否是酒类
			if(!"".equals(reportId1)){
				return reportId1;
			}
			String reportId2 = checkReport2(prodate,proId,exday);
			if(!"".equals(reportId2)){
				return reportId2;
			}
			return "";
		} catch (Exception e) {
			throw new DaoException("",e);
		}
	}

	/**
	 * 根据生产日期检验该生产日期所对应的报告是否已经存在且不过期--第一步
	 * @param prodate
	 * @param proId
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	private String checkReport1(String prodate,Long proId,boolean is) throws DaoException {
		try {

			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT MAX(result.id) FROM product_instance pinst");
//			sb.append(" s");
			sb.append(" INNER JOIN test_result result ON result.sample_id = pinst.id");
			sb.append(" WHERE pinst.product_id = ?1");
			sb.append(" AND DATE_FORMAT(pinst.production_date,'%Y-%m-%d') = ?2");

			/*if(is){//二级分类为蒸馏酒的默认保质期为180天
				sb.append(" AND DATE_ADD(pinst.production_date,INTERVAL 180 DAY) > NOW()");
			}else{
				sb.append(" AND DATE_ADD(pinst.production_date,INTERVAL pro.expiration_date DAY) > NOW()");
			}*/

//			sb.append(" AND result.publish_flag NOT IN(4,5,7) AND result.del = 0");
			sb.append(" AND result.publish_flag=6 AND result.del = 0");
			//sb.append(" AND result.test_type = '企业自检'");
			Query query = entityManager.createNativeQuery(sb.toString());
			query.setParameter(1,proId);
			query.setParameter(2,prodate);
			List<Object> objects = query.getResultList();
			if(objects!=null&&objects.size()>0){
				Object object = objects.get(0);
				if(object!=null){
					return object.toString();
				}
			}
			return "";
		} catch (Exception e) {
			throw new DaoException("",e);
		}
	}

	/**
	 * 根据生产日期检验该生产日期所对应的报告是否已经存在且不过期--第二步
	 * @param prodate
	 * @param proId
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	private String checkReport2(String prodate,Long proId,Long exday) throws DaoException {
		try {

			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT MAX(result.id) FROM product_instance pinst");
			sb.append(" INNER JOIN product pro ON pro.id = pinst.product_id");
			sb.append(" INNER JOIN test_result result ON result.sample_id = pinst.id");
			sb.append(" WHERE pinst.product_id = ?1");

			sb.append(" AND pinst.production_date >= DATE_ADD(?2,INTERVAL -6 MONTH)");
			sb.append(" AND pinst.production_date <= ?3");

			sb.append(" AND (result.test_type = '企业送检' OR result.test_type = '政府抽检')");
//			sb.append(" AND result.publish_flag NOT IN(4,5,7) AND result.del = 0");
			sb.append(" AND result.publish_flag=6 AND result.del = 0");
			Query query = entityManager.createNativeQuery(sb.toString());
			query.setParameter(1,proId);
			query.setParameter(2,prodate);
			query.setParameter(3,prodate);
			List<Object> objects = query.getResultList();
			if(objects!=null&&objects.size()>0){
				Object object = objects.get(0);
				if(object!=null){
					return object.toString();
				}
			}
			return "";
		} catch (Exception e) {
			throw new DaoException("",e);
		}
	}

	/**
	 * 获取产品的保质期
	 * @param proId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Long getExpriceDay(Long proId){

		String sql = " SELECT expiration_date FROM product WHERE id = ?1 ";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter(1,proId);
		List<Object> objects = query.getResultList();
		if(objects!=null&&objects.size()>0){
			Object obj = objects.get(0);
			if(obj!=null){
				return Long.parseLong(obj.toString())<180?180:Long.parseLong(obj.toString());
			}
		}
		return 0l;
	};

	/**
	 * 封装供应商选择产品的产品数据
	 * @User HuangYog
	 * @throws ParseException
	 */
	private List<ReturnProductVO> setSaleGoodsVO(List<Object[]> result, int type) throws ParseException {
		List<ReturnProductVO> lists = new ArrayList<ReturnProductVO>();
		if (result != null && result.size() > 0) {
			//lists = new ArrayList<ReturnProductVO>();
			for (int i = 0; i < result.size(); i++) {
				Object[] objs = result.get(i);
				ReturnProductVO vo = new ReturnProductVO();
				vo.setUuid(UUID.randomUUID().toString());//一条记录的唯一标识
				vo.setProductId(Long.valueOf(objs[0].toString()));//产品ID
				vo.setName(objs[1] != null ? objs[1].toString() : "");//产品名称
				vo.setBarcode(objs[2] != null ? objs[2].toString() : "");//条形码
				vo.setFormat(objs[3] != null ? objs[3].toString() : "");//规格
				vo.setQsNumber(objs[4] != null ? objs[4].toString() : "");//QS
				if(isJiuLei(vo.getProductId())){
					vo.setExpday(0);
				}else{
					String expDay = objs[5] != null ? objs[5].toString() : "0";//有效期天数
					vo.setExpday(Integer.parseInt("".equals(expDay)?"0":expDay));
				}
				vo.setPrice(new BigDecimal(0 + ""));//价格
				vo.setReturnCount(0l);
				if (type == 1) {
					vo.setBusType(1);
				} else {
					vo.setBusType(0);
					vo.setCount(objs[6] != null ? Long.valueOf(objs[6].toString()) : new Long(0));//数量
				}
				List<ProductionDateVO> birthDateList = getBirthDateAndReportByProductId(vo.getProductId(), vo.getExpday()+"", type);
				if (birthDateList != null && birthDateList.size() > 0) {
					vo.setProductionDate(birthDateList.get(0).getBirthDate());
					vo.setBatch(birthDateList.get(0).getBatch());
					vo.setOverDate(birthDateList.get(0).getOverDate());
					vo.setReportId(birthDateList.get(0).getReportId()!=null ? Long.parseLong(birthDateList.get(0).getReportId()):0L);
				}
				vo.setBirthDateList(birthDateList);
				lists.add(vo);
			}
		}
		return lists;
	}

	/**
	 * 根据产品id加载实例的生产日期,报告信息--批发
	 *
	 * @throws NumberFormatException
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	private List<ProductionDateVO> getBirthDateAndReportByProductId(Long productId, String day, int type) throws NumberFormatException, ParseException {
		StringBuilder sb = new StringBuilder();
		String sqlStr1 = "SELECT temp.* FROM (SELECT proinst.id pinId,proinst.production_date productDate,proinst.batch_serial_no batch," +
				" pro.expiration_date expdate,test.id reportId,test.test_date testDate from product pro " +
				" RIGHT JOIN product_instance proinst on proinst.product_id = pro.id " +
				" RIGHT JOIN test_result test on test.sample_id = proinst.id and test.del = 0 where pro.id = ?1 ";
		sb.append(sqlStr1);
		if (type == 1) {
			sb.append(" AND test.publish_flag = 1 ");
		} else if (type == 9) {
			sb.append(" AND test.publish_flag not in(4,5) ");
		} else {
			sb.append(" AND test.publish_flag not in(4,5,7) ");
		}
		sb.append(" GROUP BY proinst.batch_serial_no ORDER BY proinst.production_date DESC ) temp");
		Query query = entityManager.createNativeQuery(sb.toString()).setParameter(1, productId);
		List<Object[]> result = query.getResultList();
		List<ProductionDateVO> lists = null;
		if (result != null && result.size() > 0) {
			lists = new ArrayList<ProductionDateVO>();
			for (int i = 0; i < result.size(); i++) {
				Object[] objs = result.get(i);
				ProductionDateVO vo = new ProductionDateVO();
				vo.setInstanceId(objs[0] != null ? objs[0].toString() : "");
				vo.setBatch(objs[2] != null ? objs[2].toString() : "");
				vo.setBirthDate(objs[1] != null ? objs[1].toString().substring(0, 10) : "");
				vo.setOverDate("");
				vo.setReportId(objs[4] != null ? objs[4].toString() : "-1");
				/*判断该批次对应的报告是否已过期*/
				calculationReportExpire(vo, objs[5]);
				if (day != null && !"".equals(day)) {
					calculationOverDate(vo, Integer.parseInt(day));
				}
				lists.add(vo);
			}
		}
		return lists;
	}

	/**
	 * 判断该批次对应的报告是否已过期
	 *
	 * @param vo
	 * @param obj 对应报告的检查时间
	 */
	private void calculationReportExpire(ProductionDateVO vo, Object obj) {
        /*默认是没有过期的*/
		vo.setExpire(false);
		if (obj == null) {
			vo.setExpire(true);
			return;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date testDate = sdf.parse(obj.toString());
			Calendar cl = Calendar.getInstance();
			cl.setTime(testDate);
			cl.add(Calendar.DATE, 30*6);
			Date expireDate = cl.getTime();
			if(expireDate.getTime() > new Date().getTime()){
                /*说明报告已过期*/
				vo.setExpire(true);
			}
		} catch (ParseException e) {
			System.out.println("日期格式转换出现异常");
			e.printStackTrace();
			return;
		}

	}
	
	//====================================================销往客户台账修改=========================================================


		@Override
		public List<PurchaseAccountVO> loadTZWholeSaleProductGYS(Long myOrg,
				int page, int pageSize, String number, String licOrName,
				int status) throws DaoException {
			try {
				StringBuilder sql = new StringBuilder();
				
				String sqlStr =" select temp.* from ( SELECT tz.id id,tz.account_no no,inBus.name inName,inBus.license_no licNo,tz.create_time time,"
						+ " outBus.name outName,tz.out_status outstatus,outBus.license_no,tz.in_status instatus,tz.return_status FROM tz_business_account tz" 
						+ " LEFT JOIN business_unit inBus ON inBus.id=tz.in_business_id "
						+ " LEFT JOIN business_unit outBus ON outBus.id=tz.out_business_id "
						+ " WHERE tz.type=1 AND outBus.organization=?1 AND tz.in_status=1 ) temp where 1=1 ";
				
				sql.append(sqlStr);
				if (!licOrName.equals("")) {
					sql.append(" and (temp.inName like '%" + licOrName + "%' or temp.licNo like '%" + licOrName + "%')");
				}
					sql.append(" order by temp.instatus asc,temp.id desc ");
				Query query = entityManager.createNativeQuery(sql.toString());
				query.setParameter(1, myOrg);
				if (page > 0 && pageSize > 0) {
					query.setFirstResult((page - 1) * pageSize);
					query.setMaxResults(pageSize);
				}
				List<Object[]> result = query.getResultList();
				return receiptVO3(result);
			} catch (Exception jpae) {
				throw new DaoException("TZAccountDAOImpl.loadTZWholeSaleProduct  按照当前企业Id获取批发台账时, 出现异常！", jpae);
			}
		}

		@Override
		public Long getTZWholeSaleProductTotalGYS(Long myOrg, String number,
				String licOrName, int status) throws DaoException {
			try {
				StringBuilder sql = new StringBuilder();
				String sqlStr =" select count(*) from ( SELECT tz.id id,tz.account_no no,inBus.name inName,inBus.license_no licNo,tz.create_time time,"
						+ " outBus.name outName,tz.out_status outstatus,outBus.license_no,tz.in_status instatus,tz.return_status FROM tz_business_account tz" 
						+ " LEFT JOIN business_unit inBus ON inBus.id=tz.in_business_id "
						+ " LEFT JOIN business_unit outBus ON outBus.id=tz.out_business_id "
						+ " WHERE tz.type=1 AND outBus.organization=?1 AND tz.in_status=1  ) temp where 1=1 ";
				
				sql.append(sqlStr);
				if (!licOrName.equals("")) {
					sql.append(" and (temp.inName like '%" + licOrName + "%' or temp.licNo like '%" + licOrName + "%')");
				}
				Query query = entityManager.createNativeQuery(sql.toString());
				query.setParameter(1, myOrg);
				return Long.valueOf(query.getSingleResult().toString());
			} catch (Exception jpae) {
				throw new DaoException("TZAccountDAOImpl.getTZReceiptGoodsTotal  按照当前企业Id获取批发台账总数时, 出现异常！", jpae);
			}
		}

	@Override
	public List<ReturnProductVO> getSaleGoodsListToCS(Long orgId, int page, int pageSize, String name, String barcode) throws DaoException {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT p.id,p.name,p.barcode,p.format,p.qs_no,p.expiration_date,1 FROM ");
		sb.append(" (SELECT pro.id,pro.name,pro.barcode,pro.format,qs.qs_no,pro.expiration_date FROM t_meta_initialize_product ip ");
		sb.append(" INNER JOIN product pro ON ip.product_id=pro.id  ");
		sb.append(" LEFT JOIN product_to_businessunit pb ON pb.PRODUCT_ID=pro.id ");
		sb.append(" LEFT JOIN production_license_info qs ON qs.id=pb.qs_id ");
		sb.append(" WHERE ip.organization = ?1 AND ip.del=0 ");
		if (!"".equals(name)&&!"".equals(barcode)) {
			sb.append(" AND( pro.name like '%" + name + "%' OR pro.barcode like '%" + barcode + "%') ");
		}
		if (!"".equals(barcode)&&"".equals(name)) {
			sb.append(" AND pro.barcode like '%" + barcode + "%' ");
		}
		if ("".equals(barcode)&&!"".equals(name)) {
			sb.append(" AND pro.name like '%" + name + "%' ");
		}
		sb.append(" GROUP BY pro.id ) AS p ");
		try {
			Query query = entityManager.createNativeQuery(sb.toString()).setParameter(1, orgId);
			if (page > 0 && pageSize > 0) {
				query.setFirstResult((page - 1) * pageSize);
				query.setMaxResults(pageSize);
			}
			List<Object[]> result = query.getResultList();
			return setSaleGoodsVO(result, 1);//生产企业
		} catch (Exception e) {
			throw new DaoException("TZAccountDAOImpl.getselectBuyGoodsList()进货时选择商品时,出现异常！", e);
		}
	}

	@Override
	public Long getSaleGoodsListToCSTotal(Long orgId, String name, String barcode) throws DaoException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(" SELECT count(*) FROM ");
			sb.append(" (SELECT pro.id,pro.name,pro.barcode,pro.format,qs.qs_no,pro.expiration_date FROM t_meta_initialize_product ip ");
			sb.append(" INNER JOIN product pro ON ip.product_id=pro.id  ");
			sb.append(" LEFT JOIN product_to_businessunit pb ON pb.PRODUCT_ID=pro.id ");
			sb.append(" LEFT JOIN production_license_info qs ON qs.id=pb.qs_id ");
			sb.append(" WHERE ip.organization = ?1 AND ip.del=0 ");
			if (!"".equals(name)&&!"".equals(barcode)) {
				sb.append(" AND( pro.name like '%" + name + "%' OR pro.barcode like '%" + barcode + "%') ");
			}
			if (!"".equals(barcode)&&"".equals(name)) {
				sb.append(" AND pro.barcode like '%" + barcode + "%' ");
			}
			if ("".equals(barcode)&&!"".equals(name)) {
				sb.append(" AND pro.name like '%" + name + "%' ");
			}
			sb.append(" GROUP BY pro.id ) AS p ");
			Query query = entityManager.createNativeQuery(sb.toString()).setParameter(1, orgId);
			return Long.valueOf(query.getSingleResult().toString());
		} catch (Exception e) {
			throw new DaoException("TZAccountDAOImpl.getSaleGoodsListTotal()批发选择商品总数,出现异常！", e);
		}
	}


}