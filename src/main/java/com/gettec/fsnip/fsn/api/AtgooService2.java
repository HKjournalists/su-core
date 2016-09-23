package com.gettec.fsnip.fsn.api;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gettec.fsnip.fsn.api.base.AbstractSignVerify;
import com.gettec.fsnip.fsn.service.receive.ReceiveBusKeyConfigService;
import com.lhfs.fsn.service.product.ProductService;
import com.lhfs.fsn.vo.atgoo.ProductVO;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 爱特够系统获取产品信息的服务
 * @author tangxin
 *
 */
@Service(value="atgooService2")
public class AtgooService2 extends AbstractSignVerify<ProductVO>{
	
	@Autowired 
	private ProductService productService;
	
	@Autowired 
	private ReceiveBusKeyConfigService receiveBusKeyConfigService;

	@Override
	protected ReceiveBusKeyConfigService getReceiveBusKeyConfigService() {
		return receiveBusKeyConfigService;
	}

	@Override
	protected ProductVO doPassHandle(String data) throws Exception {
		ProductVO pv = new ProductVO();
		try {
			JSONObject json = JSONObject.fromObject(data);
			String type = json.getString("type");
			if(type == null || type.equals(""))
			{
				throw new Exception("type参数错误");
			}
			JSONArray results = json.getJSONArray("values");
			if(results.size()<=0)
			{
				throw new Exception("values参数错误");
			}
			if(type.equalsIgnoreCase("PRODUCT_ID"))
			{
				@SuppressWarnings("unchecked")
				Iterator<String> it = results.iterator();
				List<String> list = new ArrayList<String>();
				while(it.hasNext())
				{
					list.add((String)it.next());
				}
				@SuppressWarnings("unchecked")
				List<Object>productIds = productService.findTestResultByProductIds(list);
				if(productIds == null)productIds = new ArrayList<Object>();
				pv.setTestList(productIds);
			}else if(type.equalsIgnoreCase("BARCODE"))
			{
				@SuppressWarnings("unchecked")
				Iterator <String>it = results.iterator();
				List<String> list = new ArrayList<String>();
				while(it.hasNext())
				{
					list.add(it.next());
				}
				@SuppressWarnings("unchecked")
				List <String>barcodes = productService.findTestResultByBarcode(list);
				if(barcodes == null)barcodes = new ArrayList<String>();
				pv.setTestList(barcodes);
			}else{
				throw new Exception("没有type参数");
			}
		} catch (Exception e) {
			throw new Exception("json不正确参数错误");
		}
		return pv;
	}

}
