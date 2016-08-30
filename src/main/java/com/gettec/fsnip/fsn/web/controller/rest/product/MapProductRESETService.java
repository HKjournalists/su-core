package com.gettec.fsnip.fsn.web.controller.rest.product;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.MapProduct;
import com.gettec.fsnip.fsn.model.product.MapProductAddr;
import com.gettec.fsnip.fsn.service.product.MapProductAddrService;
import com.gettec.fsnip.fsn.service.product.MapProductService;
import com.gettec.fsnip.fsn.service.product.ProductTobusinessUnitService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;
import com.gettec.fsnip.fsn.vo.product.ProductOfMarketVO;
import com.gettec.fsnip.sso.client.util.AccessUtils;

@Controller
@RequestMapping("/mapProduct/")
public class MapProductRESETService {
	@Autowired private MapProductService mapProductService;
	@Autowired private ProductTobusinessUnitService productToBusinessUnitService;
	@Autowired private MapProductAddrService mapProductAddrService;
	@RequestMapping(method = RequestMethod.GET, value = "list")
	public View list(Model model,@RequestParam(value="page") int page,@RequestParam(value="pageSize") int pageSize) {
		long org=Long.valueOf(AccessUtils.getUserRealOrg().toString());
		try {
			PagingSimpleModelVO<MapProduct> data=mapProductService.getPaging(page, pageSize, null, org);
			model.addAttribute("data",data);
			
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return JSON;
	}

	@RequestMapping(method = {RequestMethod.POST,RequestMethod.PUT}, value = "save")
	public View save(Model model,@RequestBody MapProduct mapProduct) {
		boolean isExist=mapProductService.isProductExist(mapProduct.getId(),mapProduct.getProductId());
		if(isExist){
			model.addAttribute("status",2);
			return JSON;
		}
		long org=Long.valueOf(AccessUtils.getUserRealOrg().toString());
		boolean status=true;
		if(mapProduct.getId()==null){
			for(MapProductAddr addr:mapProduct.getMapProductAddrList()){
				addr.setMapProduct(mapProduct);
			}
			mapProduct.setOrganization(org);
			try {
				mapProductService.create(mapProduct);
			} catch (ServiceException e) {
				status=false;
				e.printStackTrace();
			}
		}else{
			try {
				mapProductService.update(mapProduct);
			} catch (ServiceException e) {
				e.printStackTrace();
				status=false;
			}
		}
		model.addAttribute("status",status);
		return JSON;
	}

	@RequestMapping(method=RequestMethod.GET, value="/searchProductAll")
	public View searchProductAll(@RequestParam(value="id",required=false) Long id, Model model){
		ResultVO resultVO = new ResultVO();
		try{
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			List<ProductOfMarketVO> rs=new ArrayList<ProductOfMarketVO>();
			List<ProductOfMarketVO> vos = productToBusinessUnitService.getListBarcodeByOrganization(currentUserOrganization);
			List<MapProduct> mapProductList=mapProductService.findAll();
			for(ProductOfMarketVO _productOfMarketVO:vos){
				for(MapProduct _mapProduct:mapProductList){
					if(_mapProduct.getProductId().equals(_productOfMarketVO.getId())){
						if(id!=null&&_mapProduct.getId()==id){
							rs.add(_productOfMarketVO);
						}
						//vos.remove(_productOfMarketVO);
					}else{
						rs.add(_productOfMarketVO);
					}
				}
			}
			model.addAttribute("data", rs);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}


	@RequestMapping(method=RequestMethod.GET, value="/getInfoByid")
	public View getInfoByid(@RequestParam(value="id",required=false) Long id, Model model){
		try {
			MapProduct mapProduct=mapProductService.findById(id);
			model.addAttribute("status",true);
			model.addAttribute("mapProduct",mapProduct);
		} catch (ServiceException e) {
			model.addAttribute("status",false);
			e.printStackTrace();
		}
		return JSON;
	}

	@RequestMapping(method = RequestMethod.GET, value = "delete")
	public View delete(Model model,@RequestParam(value="id") long id) {
		long org=Long.valueOf(AccessUtils.getUserOrg().toString());
		try {
			MapProduct mapProduct=mapProductService.findById(id);
			if(mapProduct.getOrganization()==org){
				mapProductService.delete(mapProduct);
				model.addAttribute("status",true);
			}
			model.addAttribute("status",false);
		} catch (ServiceException e) {
			e.printStackTrace();
			model.addAttribute("status",false);
		}
		return JSON;
	}
	
	@RequestMapping(method = RequestMethod.GET,value = "/getAllMapProducts")
	public View getAllMapProducts(Model model){
		List<MapProduct> data = mapProductAddrService.getAllMapProducts();
		model.addAttribute("data",data);
		return JSON;
	}
	
	@RequestMapping(method = {RequestMethod.POST,RequestMethod.PUT}, value = "copyAddress/{mapProductId}")
	public View copyAddress(@PathVariable(value = "mapProductId") long mapProductId,@RequestBody MapProduct mapProduct,Model model){
		long org=Long.valueOf(AccessUtils.getUserRealOrg().toString());
		try {
			if(mapProduct.getId()==null){
				MapProduct oldMapProduct = mapProductService.findById(mapProductId);
				mapProduct.setLat(oldMapProduct.getLat());
				mapProduct.setLng(oldMapProduct.getLng());
				mapProduct.setOrganization(org);
				List<MapProductAddr> mapProductAddrList = mapProduct.getMapProductAddrList();
				for(MapProductAddr addr : mapProductAddrList){
					addr.setLat(oldMapProduct.getLat());
					addr.setLng(oldMapProduct.getLng());
					addr.setMapProduct(mapProduct);
				}
				mapProductService.create(mapProduct);
				model.addAttribute("status",1);
			}
		} catch (ServiceException e) {
			model.addAttribute("status",2);
			e.printStackTrace();
		}
		return JSON;
	}
}
