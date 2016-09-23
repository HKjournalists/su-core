package com.lhfs.fsn.test.portal;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;

import com.gettec.fsnip.fsn.model.base.Certification;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.util.ImgUtils;

/**
 * 
 * @author HuiZhang
 */
public class SearchServiceImplTest extends TestCase{
	
	@Test
	public void getSearchResultTest(){
		/* 对原图片尺寸按要求进行处理
		 * http://fsnrec.com:8080/portal/img/product/6927322001901/6927322001901-20140813133943121.jpg
		 * 处理成
		 * http://fsnrec.com:8080/viewimage/portal/img/product/6927322001901/6927322001901-20140813133943121/300x400.jpg
		 * */
		List<Product> products = new ArrayList<Product>();
		Product pro1 = new Product();
		pro1.setImgUrl("http://fsnrec.com:8080/portal/market/99e3d7ee3f65/99e3d7ee3f65201402121155323.jpg|http://fsnrec.com:8080/portal/market/99e3d7ee3f65/99e3d7ee3f6520140212115532947.jpg|http://fsnrec.com:8080/portal/market/99e3d7ee3f65/99e3d7ee3f6520140212115532346.jpg|http://fsnrec.com:8080/portal/market/99e3d7ee3f65/99e3d7ee3f6520140212115532143.jpg|http://fsnrec.com:8080/portal/market/99e3d7ee3f65/99e3d7ee3f6520140212115532820.jpg|http://fsnrec.com:8080/portal/market/99e3d7ee3f65/99e3d7ee3f6520140212115532826.jpg|");
		products.add(pro1);
		Product pro2 = new Product();
		pro2.setImgUrl("http://fsnrec.com:8080/portal/market/99e3d7ee3f65/99e3d7ee3f65201402121155323.jpg|");
		products.add(pro2);
		for(Product product : products){
			String new_imgUrl = "";
			String old_imgUrls = product.getImgUrl();
			String[] old_imgUrls_array = old_imgUrls.split("\\|");
			for(int i=0; i<old_imgUrls_array.length; i++){
				new_imgUrl += ImgUtils.getImgPath(old_imgUrls_array[i], 100, 100) + "|";
			}
			product.setImgUrl(new_imgUrl);
			System.out.println(new_imgUrl);
		}
	}
	
	@Test
	public void findProductByIdTest(){
		try {
			/* 1.测试数据准备 */
			Product product = new Product();
			product.setImgUrl("http://fsnrec.com:8080/portal/market/99e3d7ee3f65/99e3d7ee3f65201402121155323.jpg|http://fsnrec.com:8080/portal/market/99e3d7ee3f65/99e3d7ee3f6520140212115532947.jpg|http://fsnrec.com:8080/portal/market/99e3d7ee3f65/99e3d7ee3f6520140212115532346.jpg|http://fsnrec.com:8080/portal/market/99e3d7ee3f65/99e3d7ee3f6520140212115532143.jpg|http://fsnrec.com:8080/portal/market/99e3d7ee3f65/99e3d7ee3f6520140212115532820.jpg|http://fsnrec.com:8080/portal/market/99e3d7ee3f65/99e3d7ee3f6520140212115532826.jpg|");
			List<Certification> productCertification = new ArrayList<Certification>();
			Certification certification = new Certification();
			certification.setImgUrl("http://fsnrec.com:8080/portal/img/certification/128/3C.jpg");
			certification.setDocumentUrl("http://fsnrec.com:8080/portal/market/SP130400/SP13040020140119142125115.jpg");
			productCertification.add(certification);
			product.setProductCertification(productCertification);
			/* 2.按规则重新生成产品图片链接地址 */
			String new_proImgUrl = "";
			String old_imgUrls = product.getImgUrl();
			String[] old_imgUrls_array = old_imgUrls.split("\\|");
			for(int i=0; i<old_imgUrls_array.length; i++){
				new_proImgUrl += ImgUtils.getImgPath(old_imgUrls_array[i], 100, 100) + "|";
			}
			product.setImgUrl(new_proImgUrl);
			System.out.println("pro: " + new_proImgUrl);
			/* 3.按规则重新生成认证图片链接地址 */
			List<Certification> certs = product.getProductCertification();
			for(Certification cert : certs){
				String new_certImgUrl = "";
				String new_certDocUrl = "";
				new_certImgUrl += ImgUtils.getImgPath(cert.getImgUrl(), 100, 100);
				cert.setImgUrl(new_certImgUrl);
				System.out.println("cert img: " + new_certImgUrl);
				new_certDocUrl += ImgUtils.getImgPath(cert.getDocumentUrl(), 100, 100);
				cert.setDocumentUrl(new_certDocUrl);
				System.out.println("cert doc: " + new_certDocUrl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
