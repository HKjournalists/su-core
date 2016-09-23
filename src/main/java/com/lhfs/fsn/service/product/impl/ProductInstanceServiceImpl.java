package com.lhfs.fsn.service.product.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.lhfs.fsn.dao.product.ProductInstanceDao;
import com.lhfs.fsn.service.product.ProductInstanceService;

/**
 * 
 * @author Kxo
 */
@Service(value="productInstanceLFService")
public class ProductInstanceServiceImpl extends BaseServiceImpl<ProductInstance, ProductInstanceDao> 
		implements ProductInstanceService{
	@Autowired private ProductInstanceDao productInstanceLFDAO;
	
	@Override
	public ProductInstanceDao getDAO() {
		return productInstanceLFDAO;
	}

	/**
	 * 根据时间范围和barcode 查处所有的batch
     * @param barcode 商品条形码
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return List<String>
	 */
    @Override
    public List<String> getProductBacth4ProductDateAndBarcode(String barcode,String startTime, String endTime) throws ServiceException {
        
        try {
            return getDAO().getProductBacth4ProductDateAndBarcode(barcode,startTime, endTime);
        } catch(DaoException daoe){
            throw new ServiceException("ProductInstanceServiceImpl.getProductBacth4ProductDateAndBarcode()-->"+daoe.getMessage(),daoe.getException());
        }
    }
    
    
}
