package com.gettec.fsnip.fsn.service.erp.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import com.gettec.fsnip.fsn.dao.erp.OutGoodsInfoDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.InitializeProduct;
import com.gettec.fsnip.fsn.model.erp.base.MerchandiseInstance;
import com.gettec.fsnip.fsn.model.erp.base.OutBillToOutGoods;
import com.gettec.fsnip.fsn.model.erp.base.OutBillToOutGoodsPK;
import com.gettec.fsnip.fsn.model.erp.base.OutGoodsInfo;
import com.gettec.fsnip.fsn.model.erp.base.OutOfBill;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.InitializeProductService;
import com.gettec.fsnip.fsn.service.erp.OutBillToOutGoodsService;
import com.gettec.fsnip.fsn.service.erp.OutGoodsInfoService;
import com.gettec.fsnip.fsn.service.erp.OutOfBillService;
import com.gettec.fsnip.fsn.service.erp.buss.MerchandiseInstanceService;
import com.gettec.fsnip.fsn.service.erp.buss.MerchandiseStorageInfoService;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

@Service("outOfGoodsService")
public class OutGoodsInfoServiceImpl extends BaseServiceImpl<OutGoodsInfo, OutGoodsInfoDAO>
		implements OutGoodsInfoService {
	@Autowired private OutGoodsInfoDAO outOfGoodsDAO;
	@Autowired private OutOfBillService outOfBillService;
	@Autowired private OutBillToOutGoodsService outBillToOutGoodsService;
	@Autowired private InitializeProductService initializeProductService;
	@Autowired private MerchandiseInstanceService merchandiseInstanceService;
	@Autowired private MerchandiseStorageInfoService merchandiseStorageInfoService;

	/**
	 * 根据出货单号查找商品列表信息
	 * @param no 出货单号
	 * @return
	 * @throws DaoException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<OutGoodsInfo> getListByNo(String no) throws ServiceException {
		try {
			return outOfGoodsDAO.getListByNo(no);
		} catch (DaoException dex) {
			throw new ServiceException("OutGoodsInfoServiceImpl.getListByNo()-->", dex);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public List<OutGoodsInfo> getAllProductByNum(String num) throws ServiceException {
		try {
			//获取商品id，根据出货单号
			List<BigInteger> o_id = outOfBillService.getCidByOutOrder(num);
			List<OutGoodsInfo> result = new ArrayList<OutGoodsInfo>();
			for(int i=0;i<o_id.size();i++){
				Long oid = o_id.get(i).longValue();
				OutGoodsInfo vo = findById(oid);
				result.add(vo);
			}
			return result;
		} catch (ServiceException sex) {
			throw new ServiceException("OutGoodsInfoServiceImpl.getAllProductByNum()-->", sex.getException());
		}
	}

	/**
	 * 根据出货单号查找商品列表信息(分页)
	 * @param page
	 * @param pageSize
	 * @param no 出货单号
	 * @return
	 * @throws ServiceException 
	 * @throws DaoException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public PagingSimpleModelVO<OutGoodsInfo> getListByNoPage(int page, int pageSize, String no) throws ServiceException {
		try {
			PagingSimpleModelVO<OutGoodsInfo> result = new PagingSimpleModelVO<OutGoodsInfo>();
			result.setListOfModel(outOfGoodsDAO.getListByNoPage(page, pageSize, no));
			result.setCount(outOfGoodsDAO.countByNo(no));
			return result;
		} catch (DaoException dex) {
			throw new ServiceException("OutGoodsInfoServiceImpl.getListByNoPage()-->", dex);
		}
	}

	/**
	 * 保存出货单的商品信息列表
	 * @param outGoods
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	public void save(OutOfBill outOfBill) throws ServiceException {
		try {
			/* 1. 根据出货单号查找已有的商品信息列表 */
			List<OutGoodsInfo> orig_outGoods = getListByNo(outOfBill.getOutOfBillNo());
			/* 2. 获取页面删除的商品列表 */
			List<OutGoodsInfo> removes = getListOfRemoves(orig_outGoods, outOfBill.getContacts());
			List<OutBillToOutGoods> addOutGoods = new ArrayList<OutBillToOutGoods>();
			for(OutGoodsInfo good : outOfBill.getContacts()){
				InitializeProduct initializeProduct = initializeProductService.findByBarcodeAndOrgId(good.getNo(), outOfBill.getOrganization());
				MerchandiseInstance merchandiseInstance = merchandiseInstanceService.getInstanceByNoAndBatchNumber(initializeProduct.getId(), good.getBatchNo());
				int count = Integer.parseInt(good.getOutNumber().toString());
				if(good.getId() == null){
					/* 3. 新增商品信息 */
					create(good);
					OutBillToOutGoodsPK pk = new OutBillToOutGoodsPK(outOfBill.getOutOfBillNo(), good.getId());
					OutBillToOutGoods outBillToOutGood = new OutBillToOutGoods(pk);
					addOutGoods.add(outBillToOutGood);
					/*3.1增加储备数量*/
					merchandiseStorageInfoService.changeReserves(merchandiseInstance,count,good.getFirstStorage(),"add");
				}else{
					/* 4. 更新商品信息 */
					OutGoodsInfo orig_good = findById(good.getId());
					/*4.1改变储备数量>>若数量变大则执行add，否则执行reduce*/
					if(orig_good.getOutNumber()>good.getOutNumber()){
						count = Integer.parseInt(good.getOutNumber().toString())-Integer.parseInt(orig_good.getOutNumber().toString());
						merchandiseStorageInfoService.changeReserves(merchandiseInstance,count,good.getFirstStorage(),"add");
					}else{
						count = Integer.parseInt(orig_good.getOutNumber().toString())-Integer.parseInt(good.getOutNumber().toString());
						merchandiseStorageInfoService.changeReserves(merchandiseInstance,count,good.getFirstStorage(),"reduce");
					}
					/* 4.2 更新商品信息 */
					setOutGoodsInfoValue(orig_good, good);
					update(orig_good);
				}
			}
			/* 5. 新增关系 */
			if(!CollectionUtils.isEmpty(addOutGoods)){
				outBillToOutGoodsService.addRelationShips(addOutGoods);
			}
			/* 6. 删除商品信息及其关系 */
			if(!CollectionUtils.isEmpty(removes)){
				for(OutGoodsInfo good : removes){
					/*6.1减少储备数量*/
					 InitializeProduct initializeProduct = initializeProductService.findByBarcodeAndOrgId(good.getNo(), outOfBill.getOrganization());
					 MerchandiseInstance merchandiseInstance = merchandiseInstanceService.getInstanceByNoAndBatchNumber(initializeProduct.getId(), good.getBatchNo());
					 int count = Integer.parseInt(good.getOutNumber().toString());
					 merchandiseStorageInfoService.changeReserves(merchandiseInstance,count,good.getFirstStorage(),"reduce");	
					 /*6.2删除关系*/
					 delete(good);
					 OutBillToOutGoodsPK pk = new OutBillToOutGoodsPK(outOfBill.getOutOfBillNo(), good.getId());
					 OutBillToOutGoods orig_outBillToGood = outBillToOutGoodsService.findByPk(pk);
					 outBillToOutGoodsService.delete(orig_outBillToGood); 
				}
			}
		} catch (ServiceException sex) {
			throw new ServiceException("OutGoodsInfoServiceImpl.save()-->", sex.getException());
		}
	}

	/**
	 * 商品信息赋值操作
	 * @param orig_good
	 * @param good
	 */
	private void setOutGoodsInfoValue(OutGoodsInfo orig_good, OutGoodsInfo good) {
		orig_good.setCategory(good.getCategory());
		orig_good.setFirstStorage(good.getFirstStorage());
		orig_good.setMode(good.isMode());
		orig_good.setMoneyType(good.getMoneyType());
		orig_good.setName(good.getName());
		orig_good.setNote(good.getNote());
		orig_good.setOutNumber(good.getOutNumber());
		orig_good.setSpecification(good.getSpecification());
		orig_good.setTotalAmount(good.getTotalAmount());
		orig_good.setType(good.getType());
		orig_good.setUnit(good.getUnit());
		orig_good.setUnitPrice(good.getUnitPrice());
		orig_good.setInspectionReport(good.isInspectionReport());
		orig_good.setHasInspectionReport(good.isHasInspectionReport());
	}

	/**
	 * 对比前后的商品列表，获取删除的集合
	 */
	private List<OutGoodsInfo> getListOfRemoves(List<OutGoodsInfo> orig_goods, List<OutGoodsInfo> goods) {
		List<OutGoodsInfo> removes = new ArrayList<OutGoodsInfo>();
		List<Long> currentId = new ArrayList<Long>();
		for (OutGoodsInfo good : goods) {
			Long id = good.getId();
			if (id != null) {
				currentId.add(id);
			}
		}
		for (OutGoodsInfo good : orig_goods) {
			if (good.getId()!=null && !currentId.contains(good.getId())) {
				removes.add(good);
			}
		}
		return removes;
	}
	
	@Override
	public OutGoodsInfoDAO getDAO() {
		return outOfGoodsDAO;
	}
}
