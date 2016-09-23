package com.lhfs.fsn.dao.product.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.lhfs.fsn.dao.product.ProductInstanceDao;

@Repository(value = "productInstanceLFDAO")
public class ProductInstanceDaoImpl extends BaseDAOImpl<ProductInstance> 
		implements ProductInstanceDao{
	/**
	 * 根据时间范围和barcode 查处所有的batch
     * @param barcode 商品条形码
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return List<String>
	 */
    @Override
    public List<String> getProductBacth4ProductDateAndBarcode(String barcode,
            String startTime, String endTime) throws DaoException {
        List<String> list = new ArrayList<String>();
        try {
            String sql = "SELECT sample.po_batch FROM t_meta_purchaseorder_info sample,product pro ,t_meta_receivingnote t "+
						"INNER JOIN t_meta_receivingnote_to_contact t1 ON t1.receivenote_no=t.re_num "+
		    			"INNER JOIN t_meta_purchaseorder_info t2 ON t2.po_id=t1.contact_id "+
	                    "WHERE  sample.po_product_info_id =?1  AND pro.barcode = sample.po_product_info_id "+
						"AND t.re_date>= ?2 AND t.re_date<= ?3 GROUP BY sample.po_product_info_id, sample.po_batch ";
            list = this.getListBySQLWithoutType(String.class, sql, new Object[] {barcode , startTime , endTime});
        } catch (JPAException e) {
            e.printStackTrace();
        }
        return list.size() > 0 ? list : null;
    }
}
