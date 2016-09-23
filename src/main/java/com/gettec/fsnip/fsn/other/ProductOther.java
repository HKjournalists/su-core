package com.gettec.fsnip.fsn.other;

import java.io.IOException;

import net.sf.json.JSONObject;

import com.gettec.fsnip.fsn.service.product.ProductService;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

public class ProductOther implements Consumer{
	private ProductService productService;
	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	@Override
	public void handleConsumeOk(String consumerTag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleCancelOk(String consumerTag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleCancel(String consumerTag) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleDelivery(String arg0, Envelope arg1,
			BasicProperties arg2, byte[] msg) throws IOException {
		try{
			String content=new String(msg, "utf-8");
			JSONObject rs=JSONObject.fromObject(content);
			String barcode=rs.getString("product_barcode");
			int cert=Integer.valueOf(rs.getString("status"));
			productService.updateProductCertByBarcode(barcode, cert);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void handleShutdownSignal(String consumerTag,
			ShutdownSignalException sig) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleRecoverOk(String consumerTag) {
		// TODO Auto-generated method stub

	}
}
