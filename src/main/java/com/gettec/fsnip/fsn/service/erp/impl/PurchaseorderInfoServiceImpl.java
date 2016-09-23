package com.gettec.fsnip.fsn.service.erp.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.erp.PurchaseorderInfoDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.InitializeProduct;
import com.gettec.fsnip.fsn.model.erp.base.PurchaseorderInfo;
import com.gettec.fsnip.fsn.model.erp.base.ReceivingNote;
import com.gettec.fsnip.fsn.model.erp.base.ReceivingNoteToContactinfo;
import com.gettec.fsnip.fsn.model.erp.base.ReceivingNoteToContactinfoPK;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.InitializeProductService;
import com.gettec.fsnip.fsn.service.erp.PurchaseorderInfoService;
import com.gettec.fsnip.fsn.service.erp.ReceivingNoteToPurchaseorderInfoService;
import com.gettec.fsnip.fsn.service.product.ProductService;
import com.lhfs.fsn.vo.product.ProductInfoVO;

@Service("purchaseorderInfoService")
public class PurchaseorderInfoServiceImpl extends BaseServiceImpl<PurchaseorderInfo, PurchaseorderInfoDAO>
		implements PurchaseorderInfoService{
	@Autowired private PurchaseorderInfoDAO purchaseorderInfoDAO;
	@Autowired private ProductService productService;
	@Autowired private InitializeProductService initializeProductService;
	@Autowired private ReceivingNoteToPurchaseorderInfoService receivingNoteToPurchaseorderInfoService;
	
	/**
	 * 根据收获单号查找商品列表
	 * @param no
	 * @return
	 */
	@Override
	public List<PurchaseorderInfo> getListByNo(String no) throws ServiceException {
		try {
			return purchaseorderInfoDAO.getListByNo(no);
		} catch (DaoException dex) {
			throw new ServiceException("PurchaseorderInfoServiceImpl.getListByNo()-->", dex.getException());
		}
	}

	/**
	 * 保存收货单商品及其关系
	 * @param purchaseGoods 收货单商品
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(ReceivingNote receivNote, Long organization) throws ServiceException {
		try {
			for(PurchaseorderInfo purchaseGood : receivNote.getContacts()){
				if(purchaseGood.getPo_id() == null){
					/* 1.1 新增初始化产品实例（引进商品）  */
					Product orig_product = productService.getDAO().findByBarcode(purchaseGood.getProduct().getBarcode());
					InitializeProduct orig_initProduct = initializeProductService.findByProIdAndOrgId(orig_product.getId(),organization);
					if(orig_initProduct == null){
						InitializeProduct initProduct = new InitializeProduct();
						initProduct.setIsLocal(false);
						initProduct.setOrganization(organization);
						initProduct.setProduct(productService.findById(orig_product.getId()));
						initializeProductService.create(initProduct);
						orig_initProduct = initProduct;
					}
					/* 1.2  新增收货单商品信息 */
					purchaseGood.setProduct(orig_initProduct.getProduct());
					create(purchaseGood);
					/* 1.3  新增收货单与商品关系 */
					ReceivingNoteToContactinfoPK pk = new ReceivingNoteToContactinfoPK(receivNote.getRe_num(), purchaseGood.getPo_id());
					ReceivingNoteToContactinfo receivToGood = new ReceivingNoteToContactinfo(pk);
					receivingNoteToPurchaseorderInfoService.create(receivToGood);
				}else{
					/* 2.1 更新收货单商品信息 */
					PurchaseorderInfo orig_purchaseGood = findById(purchaseGood.getPo_id());
					setPurchaseInfoValue(orig_purchaseGood, purchaseGood);
					update(orig_purchaseGood);
				}
			}
		} catch (Exception e) {
			throw new ServiceException("PurchaseorderInfoServiceImpl.save()-->", e);
		}
	}

	/**
	 * 赋值操作
	 * @param orig_purchaseGood
	 * @param purchaseGood
	 */
	private void setPurchaseInfoValue(PurchaseorderInfo orig_purchaseGood, PurchaseorderInfo purchaseGood){
		orig_purchaseGood.setPo_ismode(purchaseGood.isPo_ismode());
		orig_purchaseGood.setPo_isgift(purchaseGood.isPo_isgift());
		orig_purchaseGood.setPo_receivenum(purchaseGood.getPo_receivenum());
		orig_purchaseGood.setPo_unit(purchaseGood.getPo_unit());
		orig_purchaseGood.setPo_fact_receivenum(purchaseGood.getPo_fact_receivenum());
		orig_purchaseGood.setPo_chbackmoney(purchaseGood.getPo_chbackmoney());
		orig_purchaseGood.setPo_price(purchaseGood.getPo_price());
		orig_purchaseGood.setPo_isneedqr(purchaseGood.isPo_isneedqr());
		orig_purchaseGood.setHasreport(purchaseGood.getHasreport());
		orig_purchaseGood.setPo_remark(purchaseGood.getPo_remark());
		orig_purchaseGood.setPo_batch(purchaseGood.getPo_batch());
		orig_purchaseGood.setPo_storage_address(purchaseGood.getPo_storage_address());
		orig_purchaseGood.setPo_mtype(purchaseGood.getPo_mtype());
	}
	
	/**
	 * 根据收货单号查找商品列表(分页)
	 * @param no
	 * @param page
	 * @param pageSize
	 * @throws ServiceException 
	 */
	@Override
	public List<PurchaseorderInfo> getListByNoPage(String no, int page, int pageSize) throws ServiceException {
		try {
			return getDAO().getListByNoPage(no, page, pageSize);
		} catch (DaoException dex) {
			throw new ServiceException("PurchaseorderInfoServiceImpl.getListByNoPage()-->" + dex.getMessage(), dex.getException());
		}
	}
	
	/**
	 * 根据收货单号查找商品总数
	 * @param no
	 * @throws ServiceException 
	 */
	@Override
	public long countByNo(String no) throws ServiceException {
		try {
			return getDAO().countByNo(no);
		} catch (DaoException dex) {
			throw new ServiceException("PurchaseorderInfoServiceImpl.countByNo()-->" + dex.getMessage(), dex.getException());
		}
	}
	
	@Override
	public PurchaseorderInfoDAO getDAO() {
		return purchaseorderInfoDAO;
	}

	//查询 erp中的对应产品实例 总数
	@Override
	public long countGetErpProductInstance(Long proId, String batch,String startTime,String endTime)throws ServiceException {
		try {
			return purchaseorderInfoDAO.countGetErpProductInstance(proId,batch,startTime,endTime);
		} catch (DaoException dex) {
			throw new ServiceException("PurchaseorderInfoServiceImpl.countGetErpProductInstance()-->" + dex.getMessage(), dex.getException());
		}
	}
    //查询 erp中的对应产品实例
	@Override
	public List<ProductInfoVO> getErpProductInstanceByProductId(int page,int pageSize, Long proId,
			String batch,String startTime,String endTime) throws ServiceException {
		try {
			List<PurchaseorderInfo> instances = purchaseorderInfoDAO.getErpProductInstanceByProductId(page,pageSize,proId,batch,startTime,endTime);
			List<ProductInfoVO> productVOWdas = null;
			if(instances != null){
				productVOWdas = new ArrayList<ProductInfoVO>();
				for(PurchaseorderInfo instance : instances){
					ProductInfoVO productVOWda = new ProductInfoVO();
					productVOWda.setId(instance.getProduct().getId());
					productVOWda.setBarcode(instance.getProduct().getBarcode());//产品barcode
					productVOWda.setExpiration(instance.getProduct().getExpirationDate());//保质天数
					productVOWda.setName(instance.getProduct().getName());//产品名称
					productVOWda.setBatch(instance.getPo_batch());// 批次
					productVOWdas.add(productVOWda);
				}
			}
			return productVOWdas;
		} catch (DaoException dex) {
			throw new ServiceException("PurchaseorderInfoServiceImpl.getErpProductInstanceByProductId()-->" + dex.getMessage(), dex.getException());
		}
		
	}

}
