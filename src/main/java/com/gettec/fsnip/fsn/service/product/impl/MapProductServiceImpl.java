package com.gettec.fsnip.fsn.service.product.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.product.MapProductDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.MapProduct;
import com.gettec.fsnip.fsn.model.product.MapProductAddr;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.product.MapProductAddrService;
import com.gettec.fsnip.fsn.service.product.MapProductService;
@Service
public class MapProductServiceImpl extends BaseServiceImpl<MapProduct, MapProductDAO> implements MapProductService {
	@Autowired
	private MapProductDAO mapProductDAO;
	@Autowired private MapProductAddrService mapProductAddrService;
	@Override
	public MapProductDAO getDAO() {
		return mapProductDAO;
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public MapProduct update(MapProduct mapProduct) throws ServiceException {
		try {
			MapProduct _mapProduct=mapProductDAO.findById(mapProduct.getId());
			for(MapProductAddr mapProductAddr:_mapProduct.getMapProductAddrList()){
				mapProductAddrService.delete(mapProductAddr);
			}
			for(MapProductAddr addr:mapProduct.getMapProductAddrList()){
				addr.setMapProduct(_mapProduct);
			}
			_mapProduct.setLat(mapProduct.getLat());
			_mapProduct.setLng(mapProduct.getLng());
			_mapProduct.setProductId(mapProduct.getProductId());
			_mapProduct.setProductName(mapProduct.getProductName());
			_mapProduct.setMapProductAddrList(mapProduct.getMapProductAddrList());
			mapProductDAO.merge(_mapProduct);
		} catch (JPAException e) {
			e.printStackTrace();
		}
		return mapProduct;
	}
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public MapProduct findByProductId(long productId){
		return mapProductDAO.findByProductId(productId);
	}
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public boolean isProductExist(Long id,long productId){
		try {
			List<MapProduct> mapProductList=this.mapProductDAO.getListByCondition(" where e.productId=?1",new Object[]{productId});
			if(mapProductList.size()>0){
				if(id==null){
					return true;
				}else if(id.equals(mapProductList.get(0).getId())){
					return false;
				}else{
					return true;
				}
			}
			return false;
		} catch (JPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
