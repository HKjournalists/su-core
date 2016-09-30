package com.gettec.fsnip.fsn.service.account.impl;

import com.gettec.fsnip.fsn.dao.account.TZAccountDAO;
import com.gettec.fsnip.fsn.dao.product.ProductDAO;
import com.gettec.fsnip.fsn.dao.test.TestResultDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.account.*;
import com.gettec.fsnip.fsn.service.account.*;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.vo.account.*;
import com.gettec.fsnip.sso.client.util.AccessUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Account service implementation
 *
 * @author chenxiaolin
 */
@Service(value = "tZAccountService")
public class TZAccountServiceImpl extends
		BaseServiceImpl<TZAccount, TZAccountDAO> implements
		TZAccountService {

	@Autowired
	private TZAccountDAO tZAccountDAO;
	@Autowired
	private TZAccountInfoService tZAccountInfoService;
	@Autowired
	private TZProductTrailService tZProductTrailService;
	@Autowired
	private TZStockService tZStockService;
	@Autowired
	private TZStockInfoService tZStockInfoService;
	@Autowired
	protected TestResultDAO testResultDAO;
	@Autowired
	protected ProductDAO productDAO;

	@Autowired
	protected TZBusAccountOutService accountOutService;
	@Autowired
	protected TZBusaccountInfoOutService busaccountInfoOutService;

	SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Override
	public TZAccountDAO getDAO() {
		return tZAccountDAO;
	}

	/**
	 * 查询进货首页信息
	 */
	@Override
	public Model loadTZReturnProduct(Long org, int page, int pageSize,
									 String number, String licOrName, Model model,int status)
			throws ServiceException {
		try {
			List<PurchaseAccountVO> purchaseAccountList = tZAccountDAO.searchBuyGoods(org, page, pageSize, number, licOrName,status);
			Long total = tZAccountDAO.getTsearchBuyGoodsTotal(org, number,licOrName,status);
			model.addAttribute("data",purchaseAccountList != null ? purchaseAccountList : "");
			model.addAttribute("total", total);
		} catch (DaoException daoe) {
			model.addAttribute("data", "");
			model.addAttribute("total", 0);
			throw new ServiceException(daoe.getMessage(), daoe.getException());
		}
		return model;
	}

	/**
	 * 新增进货时选择商品
	 */
	@Override
	public Model selectBuyGoods(Long curOrg, int page, int pageSize,
								String name,String barcode, Model model) throws ServiceException {
		try {
			String[] str = tZAccountDAO.getBusType(curOrg);
			List<ReturnProductVO> productList = tZAccountDAO.getselectBuyGoodsList(Long.valueOf(str[0]), page, pageSize,name,barcode);
			Long total = tZAccountDAO.getselectBuyGoodsTotal(Long.valueOf(str[0]),name,barcode);
			model.addAttribute("data", productList != null ? productList : "");
			model.addAttribute("total", total);
		} catch (DaoException daoe) {
			model.addAttribute("data", "");
			model.addAttribute("total", 0);
			throw new ServiceException(daoe.getMessage(), daoe.getException());
		}
		return model;
	}
	

	/**
	 * 添加批发信息主表
	 *
	 * @param vo
	 * @param tz
	 * @param type 进货1 ， 批发 0
	 * @return
	 */
	private TZAccount setTZAccountSale(AccountOutVO vo, TZAccount tz,Integer type) {
		if (tz == null) {
			tz = new TZAccount();
		}
		if(type==1){
			tz.setType(1);
			tz.setInDate(vo.getInDate());// 批发日期
			tz.setInStatus(vo.getInStatus());// 批发状态 0：保存 1：确认
		}else{
			tz.setOutDate(vo.getOutDate());// 批发日期
			tz.setOutStatus(vo.getOutStatus());// 批发状态 0：保存 1：确认
			if(vo.getOutStatus()==1){//确认批发后才设置退货状态
				tz.setReturnStatus(0);
			}
			tz.setType(0);
		}
		tz.setOutBusinessId(vo.getOutBusId());// 供应商ID
		tz.setInBusinessId(vo.getInBusId());// 购货商ID
		tz.setCreateTime(SDF.format(new Date()));// 创建日期
		return tz;
	}

	/**
	 * 添加进货信息详请表
	 *
	 * @param vo
	 * @return
	 * @throws ParseException
	 */
	public TZAccountInfo setTZAccountInfo(ReturnProductVO vo,TZAccountInfo tzInfo) throws ParseException {
		if (tzInfo == null) {//新增
			tzInfo = new TZAccountInfo();
		}
		tzInfo.setProductId(vo.getProductId());
		tzInfo.setProductNum(vo.getReturnCount() != null ? vo.getReturnCount() : 0);
		tzInfo.setProductPrice(vo.getPrice() != null ? vo.getPrice(): BigDecimal.valueOf(0.00));
		tzInfo.setProductBatch(vo.getBatch() != null ? vo.getBatch() : "");
		tzInfo.setProductionDate(vo.getProductionDate());
		tzInfo.setOverDate(vo.getOverDate());
		tzInfo.setQsNo(vo.getQsNumber()!=null?vo.getQsNumber():"");
		if (vo.getReportId()!=null){
			tzInfo.setReportId(vo.getReportId()+"");
		}
		return tzInfo;
	}

	/**
	 * 加载查看进货台账或批发台账的产品的供应商信息
	 */
	@Override
	public Model lookTZPurchaseProduct(long id, String type, Model model)
			throws ServiceException {
		try {
			TZAccount tZAccount = null;
			String barcode = "";
			StringBuilder strBarcode = new StringBuilder();
			tZAccount = tZAccountDAO.findById(id);
			List<TZAccountInfo> tZAccountInfoList = tZAccountInfoService.getListByAccountId(tZAccount.getId());// 根据台账ID获取台账详细列表信息
			if (tZAccountInfoList != null && tZAccountInfoList.size() > 0) {
				for (TZAccountInfo tzAccountInfo : tZAccountInfoList) {
					barcode = tZAccountDAO.getBarcodeByProductId(tzAccountInfo.getProductId());
					strBarcode.append("".equals(barcode) ? "" : barcode).append(",");
				}
			}
			PurchaseAccountVO purchase = new PurchaseAccountVO();
			purchase.setBarcode("".equals(strBarcode.toString())? "" : strBarcode.toString().substring(0, strBarcode.toString().length() - 1));// 商品条形码
			if ("buyGoods".equals(type)) {// 进货
				String[] str = tZAccountDAO.getBusById(tZAccount.getOutBusinessId());
				purchase.setLicNo(str[1]);// 供应商执照号
				purchase.setOutBusName(str[0]);// 供应商名称
				purchase.setOutBusId(tZAccount.getOutBusinessId());// 供应商Id
				purchase.setCreateDate(tZAccount.getInDate());// 进货时间
				purchase.setInStatus(tZAccount.getInStatus());
			} else {// 批发
				String[] str = tZAccountDAO.getBusById(tZAccount.getInBusinessId());
				purchase.setLicNo(str[1]);// 购货商执照号
				purchase.setOutBusName(str[0]);// 购货商名称
				purchase.setInBusId(tZAccount.getInBusinessId());// 购货商Id
				purchase.setCreateDate(tZAccount.getOutDate());// 批发时间
				purchase.setOutStatus(tZAccount.getOutStatus());
			}
			model.addAttribute("outBusInfo", purchase);
		} catch (JPAException jpae) {
			throw new ServiceException(jpae.getMessage(), jpae.getException());
		} catch (DaoException daoe) {
			throw new ServiceException(daoe.getMessage(), daoe.getException());
		}
		return model;
	}

	/**
	 * 显示当前企业台账ID进货产品详情
	 */
	@Override
	public Model viewPurchaseNotSubmit(long id,Long curOrg, int page, int pageSize,
									   Model model) throws ServiceException {
		try {
			long total = 0;
			List<ReturnProductVO> tZAccountInfoList = null;
			String[] busT = tZAccountDAO.getBusType(curOrg);//根据当前组织机构ID获取企业的ID和类型--busT[0]表示企业ID  busT[1]表示企业类型
			TZAccount tz = null;
			if (id > 0) {
				tz = this.findById(id);
				if (tz != null) {
					tZAccountInfoList = tZAccountInfoService.getPurchaseList(tz.getId(), tz.getType(),Long.valueOf(busT[0]), page, pageSize);// 根据台账ID获取商品列表
					total = tZAccountInfoService.getPurchaseTotal(id);
				}
			}
			model.addAttribute("data", tZAccountInfoList == null ? "": tZAccountInfoList);
			model.addAttribute("total", total);
			return model;
		} catch (Exception daoe) {
			throw new ServiceException(daoe.getMessage(), daoe);
		}
	}

	/**
	 * 选择商品
	 */
	@Override
	public Model selectProduct(int page, int pageSize, Model model)
			throws ServiceException {
		try {
			long total = 0;
			List<WholeSaleVO> wholeSaleList = new ArrayList<WholeSaleVO>();
			WholeSaleVO wholeSale = null;
			List<TZStock> listStock = tZStockService.getTZStockListByPage(page,
					pageSize);
			if (listStock.size() > 0) {
				for (TZStock tzStock : listStock) {
					wholeSale = tZStockService.findByProductId(tzStock
							.getProductId());
					wholeSaleList.add(wholeSale);
				}
				total = listStock.size();
			}
			model.addAttribute("data", wholeSaleList.size() > 0 ? wholeSaleList
					: "");
			model.addAttribute("total", total);
			return model;
		} catch (ServiceException daoe) {
			throw new ServiceException(daoe.getMessage(), daoe.getException());
		}
	}

	/***************************************** 批发台账 ******************************************************/
	/**
	 * 添加批发台账商品及信息
	 * 添加进货台账商品及信息
	 * type 进货1 ， 批发 0
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Model submitTZWholeSaleProduct(Long organization,AccountOutVO accountOut, Model model, String status,Integer type)throws ServiceException {
		model.addAttribute("save", false);
		try {
			String[] busT = tZAccountDAO.getBusType(organization);//根据当前组织机构ID获取企业的ID和类型--busT[0]表示企业ID  busT[1]表示企业类型
			List<ReturnProductVO> pVOs = accountOut.getProList();// 获取商品列表
			/* 添加主表 */
			TZAccount tzOral = null;
			TZAccount tZAccount = null;
			if (accountOut.getId() != null) {// 更新
				tzOral = tZAccountDAO.findById(accountOut.getId());
				tZAccount = setTZAccountSale(accountOut, tzOral,type);
				this.update(tZAccount);
				tZAccountInfoService.deleteInfoByaccountId(tZAccount.getId());
			} else {// 新增
				tZAccount = setTZAccountSale(accountOut, tzOral,type);
				this.create(tZAccount);
			}
			/* 添加进货产品详细信息 */
			for (ReturnProductVO voNew : pVOs) {
				TZAccountInfo tzInfo = setTZAccountInfo(voNew, null);
				tzInfo.setBusAccountId(tZAccount.getId());
				tZAccountInfoService.create(tzInfo);
				if("submit".equals(status)){
					/* 查找库存*/
					TZStock orig_stock = tZStockService.findByProIdAndQsNo(Long.valueOf(busT[0]),tzInfo.getQsNo(), tzInfo.getProductId());
					if (orig_stock instanceof TZStock) {
						/* 更新库存主表 */
						setTzStock(tzInfo,orig_stock,tZAccount);
						orig_stock.setBusinessId(orig_stock.getBusinessId());// 设置当前企业ID
						tZStockService.update(orig_stock);
					} else {
						/* 新增库存主表 */
						orig_stock = orig_stock !=null ? orig_stock:new TZStock();
						setTzStock(tzInfo,orig_stock,tZAccount);
						tZStockService.create(orig_stock);
					}
					/* 新增库存详情表 */
					TZStockInfo tzStockInfoNew = setStockInfo(voNew, orig_stock, tZAccount);
					tZStockInfoService.create(tzStockInfoNew);
					/* 新增商品轨迹数据 */
					addTZProductTrail(tzInfo,tZAccount);
				}
			}
			model.addAttribute("save", true);
			model.addAttribute("tzId", tZAccount.getId());
			return model;
		} catch (Exception daoe) {
			throw new ServiceException(daoe.getMessage(), daoe);
		}
	}

	/**
	 * 新增商品轨迹数据 
	 */
	private void addTZProductTrail(TZAccountInfo tzInfo, TZAccount tZAccount) throws ServiceException, DaoException {
		TZProductTrail tZProductTrail = setTZProductTrail(tzInfo);
		tZProductTrail.setAccountId(tZAccount.getId());//台账ID
		tZProductTrail.setType(0);
		tZProductTrail.setOutBusId(tZAccount.getOutBusinessId());// 供应商ID
		tZProductTrail.setInBusId(tZAccount.getInBusinessId());// 当前企业ID
		tZProductTrailService.create(tZProductTrail);
	}
	/* 封装产品轨迹数据 */
	private TZProductTrail setTZProductTrail(TZAccountInfo tzInfo) throws DaoException {
		TZProductTrail tzProductTrail = new TZProductTrail();
		tzProductTrail.setProductId(tzInfo.getProductId());
		tzProductTrail.setBarcode(tZAccountDAO.getBarcodeByProductId(tzInfo.getProductId()));
		tzProductTrail.setProductBatch(tzInfo.getProductBatch());
		tzProductTrail.setProductNum(tzInfo.getProductNum()!=null?tzInfo.getProductNum():0);
		if(tzInfo.getProductionDate()!=null){
			tzProductTrail.setProductionDate(tzInfo.getProductionDate());
		}
		if(tzInfo.getOverDate()!=null){
			tzProductTrail.setOverDate(tzInfo.getOverDate());
		}
		tzProductTrail.setAccountDate(SDF.format(new Date()));
		tzProductTrail.setQsNo(tzInfo.getQsNo());
		return tzProductTrail;
	}

	private void setTzStock(TZAccountInfo tzInfo, TZStock orig_stock,TZAccount tzaccount) {
		orig_stock.setProductId(tzInfo.getProductId());
		orig_stock.setQsNo(tzInfo.getQsNo());
		orig_stock.setReportStatus(1);//1表示有报告
		if(tzaccount.getType() == 1){ //进货
//			if (tzaccount.getOutStatus() == 0) {
//				orig_stock.setProductNum(0l);
//			}else {
//				orig_stock.setProductNum((orig_stock.getProductNum()!=null?orig_stock.getProductNum():0)+(tzInfo.getProductNum()!= null ? tzInfo.getProductNum() : 0));
//			}
			orig_stock.setBusinessId(tzaccount.getInBusinessId());// 当前企业ID
			orig_stock.setProductNum((orig_stock.getProductNum()!=null?orig_stock.getProductNum():0)+(tzInfo.getProductNum()!= null ? tzInfo.getProductNum() : 0));
		}else{ //批发
			orig_stock.setBusinessId(tzaccount.getOutBusinessId());// 当前企业ID
			orig_stock.setProductNum((orig_stock.getProductNum()!=null?orig_stock.getProductNum():0)-(tzInfo.getProductNum()!= null ? tzInfo.getProductNum() : 0));
		}
	}

	private TZStockInfo setStockInfo(ReturnProductVO voNew, TZStock orig_stock,TZAccount account) {
		TZStockInfo info= null;
		if(orig_stock!=null){
			info= new TZStockInfo();
			info.setBusinessId(orig_stock.getBusinessId());
			info.setCreateType(1);
			info.setInDate(SDF.format(new Date()));
			info.setProductBatch(voNew.getBatch());
			info.setProductId(orig_stock.getProductId());
			info.setProductNum(voNew.getReturnCount());
			info.setQsNo(voNew.getQsNumber());
			info.setStockId(orig_stock.getId());
			info.setAccountId(account.getId());
			if(account.getType()==1){
				info.setType(0);
			}else{
				info.setType(1);
			}
		}
		return info;
	}

	/**
	 * 添加批发台账轨迹表
	 * @throws ParseException
	 */
	public TZProductTrail setTZProductTrail(ReturnProductVO vo) throws ParseException {
		TZProductTrail tzProductTrail = new TZProductTrail();
		tzProductTrail.setProductId(vo.getProductId());
		tzProductTrail.setBarcode(vo.getBarcode() != null ? vo.getBarcode() : "");
		tzProductTrail.setProductBatch(vo.getBatch() != null ? vo.getBatch() : "");
		tzProductTrail.setProductNum(vo.getCount() != null ? vo.getCount() : 0);
		tzProductTrail.setProductionDate(vo.getProductionDate());
		tzProductTrail.setOverDate(vo.getOverDate());
		tzProductTrail.setAccountDate(SDF.format(new Date()));
		tzProductTrail.setQsNo(vo.getQsNumber() != null ? vo.getQsNumber(): "");
		return tzProductTrail;
	}

	@Override
	public Model viewWholeSale(Long org, int page, int pageSize, String number,
							   String licOrName, Model model,int status) throws ServiceException {
		try {
			List<PurchaseAccountVO> purchaseAccountList = tZAccountDAO.loadTZWholeSaleProduct(org, page, pageSize, number,licOrName,status);
			Long total = tZAccountDAO.getTZWholeSaleProductTotal(org, number,licOrName,status);
			model.addAttribute("data",purchaseAccountList != null ? purchaseAccountList : "");
			model.addAttribute("total", total);
		} catch (DaoException daoe) {
			model.addAttribute("data", "");
			model.addAttribute("total", 0);
			throw new ServiceException(daoe.getMessage(), daoe.getException());
		}
		return model;
	}

	/**
	 * 新增批发台账时选择商品
	 */
	@Override
	public Model selectSaleGoods(Long curOrg,String name,String barcode, int page, int pageSize,
								 Model model) throws ServiceException {
		try {
			List<ReturnProductVO> productList = null;
			Long total = 0l;
			String[] busT = tZAccountDAO.getBusType(curOrg);//根据当前组织机构ID获取企业的ID和类型--busT[0]表示企业ID  busT[1]表示企业类型
			if(busT[1].indexOf("生产企业")>-1){
				productList = tZAccountDAO.getSaleGoodsListToSC(curOrg,Long.valueOf(busT[0]), page, pageSize,name,barcode);
				total = tZAccountDAO.getSaleGoodsListToSCTotal(curOrg,Long.valueOf(busT[0]),name,barcode);
			}else{
				productList = tZAccountDAO.getSaleGoodsListByReportBatch(Long.valueOf(busT[0]),name, barcode,page,pageSize);
				total = tZAccountDAO.getSaleGoodsListByReportBatchTotal(Long.valueOf(busT[0]),name,barcode);
			}
			model.addAttribute("data", productList);
			model.addAttribute("total", total);
		} catch (DaoException daoe) {
			model.addAttribute("data", "");
			model.addAttribute("total", 0);
			throw new ServiceException(daoe.getMessage(), daoe.getException());
		}
		return model;
	}

	/*********************************************** 收货台账 *******************************************************/
	/**
	 * 显示收货台账首页数据
	 */
	@Override
	public Model viewReceiptGoods(Long org, int page, int pageSize,
								  String number, String licOrName, Model model)
			throws ServiceException {
		try {
			List<PurchaseAccountVO> purchaseAccountList = tZAccountDAO.loadTZReceiptGoods(org, page, pageSize, number, licOrName);
			Long total = tZAccountDAO.getTZReceiptGoodsTotal(org, number,licOrName);
			model.addAttribute("data",purchaseAccountList != null ? purchaseAccountList : "");
			model.addAttribute("total", total);
		} catch (DaoException daoe) {
			model.addAttribute("data", "");
			model.addAttribute("total", 0);
			throw new ServiceException(daoe.getMessage(), daoe.getException());
		}
		return model;
	}

	/**
	 * 确认收货
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void submitReceiptGoods(long tzId, Long curOrg,List<Map<String,Object>> rvo) throws ServiceException {
		try {
			String[] busT = tZAccountDAO.getBusType(curOrg);//根据当前组织机构获取企业ID和类型    busT[0]=ID、busT[1]=Type   
			TZAccount account = this.findById(tzId);
			if (account != null && account.getOutStatus()!=null && account.getOutStatus()==1) {
				/* 修改库存数量 */
				TZBusAccountOut abOut = updateStock2(rvo,Long.valueOf(busT[0]),account.getOutBusinessId(),tzId);
				/*创建退货信息*/
				if(abOut!=null){
					createOutproduct(abOut);
//					account.setReturnStatus(1);
				}
				account.setInStatus(1);
				account.setInDate(SDF.format(new Date()));
				this.update(account);

				/* 添加收货台账到轨迹表 */
				//addTrail(inInfos,account);
			}
		} catch (Exception daoe) {
			throw new ServiceException(daoe.getMessage(), daoe);
		}
	}

	/**
	 * 确认退货
	 * chenxiaolin 2015-09-18
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void returnOfGoods(long tzId, Long curOrg,String refuseReason) throws ServiceException {
		try {
			String[] busT = tZAccountDAO.getBusType(curOrg);//根据当前组织机构获取企业ID和类型    busT[0]=ID、busT[1]=Type  
			TZAccount account = this.findById(tzId);
			if (account != null && account.getOutStatus()!=null && account.getOutStatus()==1) {//已确认批发
				account.setOutStatus(0);//设置退货的商品为未批发
				account.setReturnStatus(1);//设置被退回
				account.setOutDate(SDF.format(new Date()));//操作日期
				//设置拒收原因（针对台账App）
				if(refuseReason!=null&&!"".equals(refuseReason)){
					account.setRefuseReason(refuseReason);
				}
				this.update(account);//更新台账主表
				//查找台账详情列表
				List<TZAccountInfo> inInfos = tZAccountInfoService.getListByAccountId(account.getId());
				/* 修改批发商库存数量 */
				updateStock3(inInfos,Long.valueOf(account.getOutBusinessId()),tzId);
				/* 删除轨迹表对应的退货商品记录 */
				tZProductTrailService.deleteTrainByTzId(tzId,Long.valueOf(busT[0]));
			}
		} catch (Exception daoe) {
			throw new ServiceException(daoe.getMessage(), daoe);
		}
	}

	/**
	 * 根据生产日期检验该生产日期所对应的报告是否已经存在且不过期
	 * @param model
	 * @param prodate
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Model checkReport(Model model, String prodate,Long proId) throws ServiceException {
		try {
			String reportId = tZAccountDAO.checkReport(prodate,proId);
			if(!"".equals(reportId)){
				model.addAttribute("haveReport",true);
			}else{
				model.addAttribute("haveReport",false);
			}
			model.addAttribute("reportId",reportId);
			return model;
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	/**
	 * 添加收货台账到轨迹表
	 * @param inInfos tZAccount
	 * @throws ServiceException
	 * @throws DaoException
	 */
	private void addTrail(List<TZAccountInfo> inInfos, TZAccount tZAccount) throws ServiceException, DaoException {
		if(inInfos!=null&&inInfos.size()>0){
			for (TZAccountInfo tzAccountInfo : inInfos) {
				TZProductTrail tZProductTrail = setTZProductTrail(tzAccountInfo);
				tZProductTrail.setAccountId(tZAccount.getId());//台账ID
				tZProductTrail.setType(0);
				tZProductTrail.setOutBusId(tZAccount.getOutBusinessId());// 供应商ID
				tZProductTrail.setInBusId(tZAccount.getInBusinessId());// 当前企业ID
				tZProductTrailService.create(tZProductTrail);
			}
		}
	}

	/**
	 * 修改库存
	 * @throws ServiceException
	 */
	private TZBusAccountOut updateStock2(List<Map<String,Object>> inInfos,Long busId,Long outbusId,Long tzId) throws ServiceException {
		TZBusAccountOut abOut = null;
		if(inInfos!=null&&inInfos.size()>0){
			List<TZBusaccountInfoOut> outinfoList = null;
			for(int i = 0 ; i < inInfos.size(); i++){
				Map<String,Object> rpvo = inInfos.get(i);
				String qs = rpvo.get("qsNumber")!=null?rpvo.get("qsNumber").toString():"";
				Integer proId = (Integer) rpvo.get("productId");
				Integer returnCount = (Integer)rpvo.get("returnCount") ;
				TZStock orig_stock = tZStockService.findByProIdAndQsNo(busId,qs,Long.valueOf(proId));
				if(orig_stock instanceof TZStock){
					orig_stock.setProductNum(orig_stock.getProductNum()+returnCount);
					orig_stock.setReportStatus(1);
					tZStockService.update(orig_stock);
					/* 添加库存详情 */
				}else{
					if(returnCount > 0){
						orig_stock = (orig_stock !=null ) ? orig_stock:new TZStock();
						orig_stock.setBusinessId(busId);
						orig_stock.setProductId(Long.valueOf(proId));
						orig_stock.setProductNum(Long.valueOf(returnCount));
						orig_stock.setQsNo(qs);
						orig_stock.setReportStatus(1);
						tZStockService.create(orig_stock);
					}

				}

				/*修改收货商实际收货数量*/
				TZAccountInfo tZAccountInfo = tZAccountInfoService.findById(Long.valueOf((Integer) rpvo.get("id")));
				tZAccountInfo.setRealNum(Long.valueOf(returnCount));
				tZAccountInfoService.update(tZAccountInfo);

				Integer countTotal = (Integer) rpvo.get("countTotal");
				if(countTotal>returnCount){
					/*创建退货详情信息 并添加到list中*/
					TZBusaccountInfoOut InfoOut = new TZBusaccountInfoOut();
					InfoOut.setProductId(Long.valueOf(proId));
					InfoOut.setProductNum(Long.valueOf(countTotal-returnCount));
					if(rpvo.get("price") instanceof Integer){
						InfoOut.setProductPrice(BigDecimal.valueOf((Integer)rpvo.get("price")));
					}else if(rpvo.get("price") instanceof Double){
						InfoOut.setProductPrice(BigDecimal.valueOf((Double)rpvo.get("price")));
					}

					InfoOut.setProductBatch(rpvo.get("batch").toString());
					InfoOut.setQsNo(qs);
					InfoOut.setProductionDate(rpvo.get("productionDate").toString());
					InfoOut.setOverDate(rpvo.get("overDate").toString());
					if(outinfoList == null){
						outinfoList = new ArrayList<TZBusaccountInfoOut>();
					}
					outinfoList.add(InfoOut);
					/*创建退货主信息*/
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					if(abOut==null){
						abOut = new TZBusAccountOut();
						abOut.setOutBusId(busId);//退货企业id
						abOut.setInBusId(outbusId);//收货货企业id
						abOut.setCreateDate(sdf.format(new Date()));//创建时间
						abOut.setOutStatus(1);//确定退货
						abOut.setOutDate(sdf.format(new Date()));//确定退货
						abOut.setAccountId(tzId.toString());
					}
					abOut.setOutInfoList(outinfoList);

				}
				addTZStockInfo(orig_stock,rpvo);
			}
		}
		return abOut;
	}

	private void createOutproduct(TZBusAccountOut abOut) throws ServiceException {
		accountOutService.create(abOut);
		List<TZBusaccountInfoOut> infoOuts = abOut.getOutInfoList();
		if(infoOuts!=null && infoOuts.size()>0){
			for(int i = 0; i< infoOuts.size(); i++){
				infoOuts.get(i).setBusAccountId(abOut.getId());
				busaccountInfoOutService.create(infoOuts.get(i));
			}
		}
	}

	/**
	 * 修改库存详情表
	 * @throws ServiceException
	 */
	private void addTZStockInfo(TZStock orig_stock, Map<String,Object> rpvo) throws ServiceException {
		Integer returnCount =(Integer) rpvo.get("returnCount");
		if(returnCount>0) {
			String qs = rpvo.get("qsNumber")!=null?rpvo.get("qsNumber").toString():"";
			Integer proId = (Integer) rpvo.get("productId");
			String batch = rpvo.get("batch")!=null?rpvo.get("batch").toString():"";
			TZStockInfo sinfo = new TZStockInfo();
			sinfo.setStockId(orig_stock.getId());
			sinfo.setQsNo(qs);
			sinfo.setBusinessId(orig_stock.getBusinessId());
			sinfo.setCreateType(1);
			sinfo.setType(0);// 0 表示进货
			sinfo.setInDate(SDF.format(new Date()));
			sinfo.setProductBatch(batch);
			sinfo.setProductId(Long.valueOf(proId));
			sinfo.setProductNum(Long.valueOf(returnCount));
			tZStockInfoService.create(sinfo);
		}

	}

	/**
	 * 查看收货信息
	 */
	@Override
	public Model lookTZReceiptGoods(long accountId, Model model)
			throws ServiceException {
		SimpleDateFormat SDFTime = new SimpleDateFormat("yyyy-MM-dd");
		try {
			TZAccount tZAccount = null;
			String barcode = "";
			PurchaseAccountVO receiptGoods = new PurchaseAccountVO();
			StringBuilder strBarcode = new StringBuilder();
			tZAccount = tZAccountDAO.findById(accountId);
			List<TZAccountInfo> tZAccountInfoList = tZAccountInfoService.getListByAccountId(tZAccount.getId());// 根据台账ID获取台账详细列表信息
			if (tZAccountInfoList != null && tZAccountInfoList.size() > 0) {
				for (TZAccountInfo tzAccountInfo : tZAccountInfoList) {
					try {
						barcode = tZAccountDAO.getBarcodeByProductId(tzAccountInfo.getProductId());
						strBarcode.append("".equals(barcode) ? "" : barcode).append(",");
					} catch (DaoException e) {
						e.printStackTrace();
					}
				}
			}
			String[] str = tZAccountDAO.getBusById(tZAccount.getOutBusinessId());
			receiptGoods.setInStatus(tZAccount.getInStatus());
			receiptGoods.setCreateDate(SDFTime.format(tZAccount.getInDate() == null ? new Date(): SDFTime.parse(tZAccount.getInDate())));// 收货时间
			receiptGoods.setLicNo(str[1]);// 供货商执照号
			receiptGoods.setOutBusName(str[0]);// 供货商名称
			receiptGoods.setBarcode(strBarcode == null ? "" : strBarcode.toString().substring(0, strBarcode.toString().length() - 1));// 商品条形码
			model.addAttribute("outBusInfo", receiptGoods);
		} catch (Exception daoe) {
			throw new ServiceException(daoe.getMessage(), daoe);
		}
		return model;
	}

	/**
	 * 收货的产品详情
	 */
	@Override
	public Model viewNoReceiptGoods(long tzId, int page, int pageSize,
									Model model) throws ServiceException {
		try {
			TZAccount tZAccount = null;
			List<ReturnProductVO> proList = null;
			Long total = 0l;
			tZAccount = tZAccountDAO.findById(tzId);
			if (tZAccount != null && tZAccount.getOutStatus() == 1) {
				proList = tZAccountInfoService.getProList(tzId, page, pageSize);
				total = tZAccountInfoService.getTZReceiptTotal(tzId);
			}
			model.addAttribute("data", proList);
			model.addAttribute("total", total);
			return model;
		} catch (JPAException daoe) {
			throw new ServiceException(daoe.getMessage(), daoe.getException());
		}
	}

	/**
	 * 首页点击查看批发台账后进入新增页面然后加载产品详情
	 */
	@Override
	public Model loadingDetailGoods(Long tzId,Long curOrg, int page, int pageSize,
									Model model) throws ServiceException {
		try {
			long total = 0;
			int busType = 0;
			List<ReturnProductVO> tZAccountInfoList = null;
			String[] busT = tZAccountDAO.getBusType(curOrg);
			if("生产企业".equals(busT[1])){
				busType = 1;
			}
			TZAccount tz = null;
			if (tzId > 0) {
				tz = this.findById(tzId);
				if (tz != null) {
					tZAccountInfoList = tZAccountInfoService.loadingDetailGoods(tz.getId(),busType, tz.getOutStatus(),Long.valueOf(busT[0]),page, pageSize);// 根据台账ID获取商品列表
					total = tZAccountInfoService.getloadingDetailGoodsTotal(tzId);
				}
			}
			model.addAttribute("data", tZAccountInfoList == null ? "": tZAccountInfoList);
			model.addAttribute("total", total);
			return model;
		} catch (Exception daoe) {
			throw new ServiceException(daoe.getMessage(), daoe);
		}
	}

	/**
	 * 删除操作
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteGoods(long tZId,String type) throws ServiceException {
		try {
			TZAccount account = this.findById(tZId);
			if(account!=null){
				/* 删除进货产品信息 */
				tZAccountInfoService.deleteInfoByaccountId(tZId);
				if("buyGoods".equals(type)&&account.getInStatus()!=1){
					this.delete(account.getId());
				}
				/* 删除批发产品信息 */
				if("saleGoods".equals(type)&&account.getOutStatus()!=1){
					this.delete(account.getId());
				}
			}
		} catch (Exception daoe) {
			throw new ServiceException(daoe.getMessage(), daoe);
		}
	}

	/**
	 * 加载监管系统首页数据
	 */
	@Override
	public Model loadingFDAMSDate(Model model) throws ServiceException {
		Map<String, Map<String, Long>> totalMap = null;
		try {
			totalMap = tZAccountDAO.loadingFDAMSDate();
			model.addAttribute("data",totalMap);
			return model;
		} catch (Exception daoe) {
			throw new ServiceException(daoe.getMessage(), daoe);
		}

	}

	/**
	 * type: 1:进货 2：批发
	 */
	@Override
	public Model loadBustBasicInfo(Long tzId, int type, Model model)throws ServiceException {
		try {
			long busId = 0l;
			// 根据台账ID获取企业Id
			TZAccount account = tZAccountDAO.findById(tzId);
			if (account != null) {
				if (type == 1) {
					busId = account.getInBusinessId();
				}
				if (type == 2) {
					busId = account.getOutBusinessId();
				}
				BusRelationVO bu = tZAccountDAO.findBusRelationById(busId);
				model.addAttribute("basicInfo", bu);
			}
			return model;
		} catch (Exception e) {
			throw new ServiceException("TZAccountServiceImpl.loadBustBasicInfo()加载企业的基本信息，异常！",e);
		}
	}

	@Override
	public Model loadZFReportList(int page, int pageSize, Long busId,
								  String name, String batch, Model model) throws ServiceException {
		try {
			List<ZhengFuVO> testResults = null;
			Long total = 0l;
			Long org = tZAccountDAO.getOrgByBusId(busId);
			if(org>0){
				testResults = tZAccountDAO.loadZFReportList(page,pageSize,org,name,batch);
				total = tZAccountDAO.loadZFReportTotal(org,name,batch);
			}
			model.addAttribute("data", testResults!=null?testResults:"");
			model.addAttribute("total", total);
			return model;
		} catch (Exception daoe) {
			throw new ServiceException(daoe.getMessage(), daoe);
		}
	}

	/**
	 * 政府查看企业注册列表
	 */
	@Override
	public Model loadZFEnterList(int page, int pageSize, String name,int type, Model model) throws ServiceException {
		try {
			Long total = 0l;
			List<ZhengFuEnterVO> businessStaVo = new ArrayList<ZhengFuEnterVO>();
			List<ZhengFuEnterVO> businessSta = tZAccountDAO.loadZFEnterList(page,pageSize,name,type);
			if(businessSta!=null){
				for (ZhengFuEnterVO zhengFuEnterVO : businessSta) {
					//查询已发布报告的产品数
					Long productQuantity=testResultDAO.getPublishProCountByOrganizationId(zhengFuEnterVO.getOrg());
					//查询已发布的报告数
					Long reportQuantity=testResultDAO.getRepoCountByOrganizationId(zhengFuEnterVO.getOrg());
					//没有发布报告的产品数=该企业所有产品数-已发布报告产品数
					Long notPublishProQuantity=productDAO.getAllProCountByOrganization(zhengFuEnterVO.getOrg())-productQuantity;
					zhengFuEnterVO.setProductQuantity(productQuantity > 0 ? productQuantity : 0);
					zhengFuEnterVO.setNotPublishProQuantity(notPublishProQuantity > 0 ? notPublishProQuantity : 0);
					zhengFuEnterVO.setReportQuantity(reportQuantity > 0 ? reportQuantity : 0);
					businessStaVo.add(zhengFuEnterVO);
				}
			}
			total = tZAccountDAO.loadZFEnterTotal(name,type);
			model.addAttribute("data", businessStaVo!=null?businessStaVo:"");
			model.addAttribute("total", total);
			return model;
		} catch (Exception daoe) {
			throw new ServiceException(daoe.getMessage(), daoe);
		}
	}

	/**
	 * 确认退货时修改批发商的库存
	 * chenxiaolin 2015-09-18
	 * @param tzId
	 */
	private void updateStock3(List<TZAccountInfo> inInfos,Long busId, long tzId) throws ServiceException {
		if(inInfos!=null&&inInfos.size()>0){
			for(int i = 0 ; i < inInfos.size(); i++){
				TZStock orig_stock = tZStockService.findByProIdAndQsNo(busId,inInfos.get(i).getQsNo(),inInfos.get(i).getProductId());
				if(orig_stock instanceof TZStock){//修改库存主表
					orig_stock.setProductNum(orig_stock.getProductNum()+inInfos.get(i).getProductNum());
					tZStockService.update(orig_stock);
					//根据台账ID删除库存详情表中对应的商品
					tZStockInfoService.deleteListByStockId(tzId);
				}
			}
		}
	}
//====================================================销往客户台账修改=========================================================

	/**
	 * 供应商进货商品列表
	 */
	@Override
	public Model selectBuyGoodsById(Long orgId, String name, String barcode,
			int page, int pageSize, Model model)throws ServiceException {
		try {
			List<ReturnProductVO> productList = new ArrayList<ReturnProductVO>();
			Long total = 0l;
			productList = tZAccountDAO.getSaleGoodsListToSC(orgId,0L, page, pageSize,name,barcode);
			total = tZAccountDAO.getSaleGoodsListToSCTotal(orgId,0L,name,barcode);
			model.addAttribute("data", productList);
			model.addAttribute("total", total);
		} catch (DaoException daoe) {
			model.addAttribute("data", "");
			model.addAttribute("total", 0);
			throw new ServiceException(daoe.getMessage(), daoe.getException());
		}
		return model;
	}
	
	/**
	 * 供应商主动进货
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Model submitTZWholeSaleProductGYS(Long organization,AccountOutVO accountOut, Model model, String status,Integer type)throws ServiceException {
		model.addAttribute("save", false);
		try {
			List<ReturnProductVO> pVOs = accountOut.getProList();// 获取商品列表
			/* 添加主表 */
			TZAccount tzOral = null;
			TZAccount tZAccount = null;
			if (accountOut.getId() != null) {// 更新
				tzOral = tZAccountDAO.findById(accountOut.getId());
				tZAccount = setTZAccountSaleGYS(accountOut, tzOral,type);
				this.update(tZAccount);
				tZAccountInfoService.deleteInfoByaccountId(tZAccount.getId());
			} else {// 新增
				tZAccount = setTZAccountSaleGYS(accountOut, tzOral,type);
				this.create(tZAccount);
			}
			/* 添加进货产品详细信息 */
			for (ReturnProductVO voNew : pVOs) {
				TZAccountInfo tzInfo = setTZAccountInfo(voNew, null);
				tzInfo.setBusAccountId(tZAccount.getId());
				tZAccountInfoService.create(tzInfo);
			}
			model.addAttribute("save", true);
			model.addAttribute("tzId", tZAccount.getId());
			return model;
		} catch (Exception daoe) {
			throw new ServiceException(daoe.getMessage(), daoe);
		}
	}
	
	/**
	 * 供应商主动进货台账信息
	 * @param vo
	 * @param tz
	 * @param type
	 * @return
	 */
	private TZAccount setTZAccountSaleGYS(AccountOutVO vo, TZAccount tz,Integer type) {
		if (tz == null) {
			tz = new TZAccount();
		}
		tz.setType(1);
		tz.setInDate(vo.getInDate());// 批发日期
		tz.setInStatus(0);// 批发状态 0：保存 1：确认
		tz.setOutStatus(0);
		
		tz.setOutBusinessId(vo.getOutBusId());// 供应商ID
		tz.setInBusinessId(vo.getInBusId());// 购货商ID
		tz.setCreateTime(SDF.format(new Date()));// 创建日期
		return tz;
	}

	/**
	 * 生产商销售确认列表
	 */
	@Override
	public Model viewWholeSaleGYS(Long myOrg, int page, int pageSize,
			String number, String licOrName, Model model, int status) throws ServiceException {
		try {
			List<PurchaseAccountVO> purchaseAccountList = tZAccountDAO.loadTZWholeSaleProductGYS(myOrg, page, pageSize, number,licOrName,status);
			Long total = tZAccountDAO.getTZWholeSaleProductTotalGYS(myOrg, number,licOrName,status);
			model.addAttribute("data",purchaseAccountList != null ? purchaseAccountList : "");
			model.addAttribute("total", total);
		} catch (DaoException daoe) {
			model.addAttribute("data", "");
			model.addAttribute("total", 0);
			throw new ServiceException(daoe.getMessage(), daoe.getException());
		}
		return model;
	}

	/**
	 * 生产商确认供应商主动进货
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public TZAccount saleSure(long id) {
		try {
			TZAccount account = tZAccountDAO.findById(id);
			account.setInStatus(1);
			account.setOutStatus(1);
			List<TZAccountInfo> listByAccountId = tZAccountInfoService.getListByAccountId(id);
			for (TZAccountInfo tzInfo : listByAccountId) {
				TZStock orig_stock = tZStockService.findByProIdAndQsNo(account.getInBusinessId(),tzInfo.getQsNo(), tzInfo.getProductId());
				if (orig_stock instanceof TZStock) {
					/* 更新库存主表 */
					setTzStock(tzInfo,orig_stock,account);
					orig_stock.setBusinessId(orig_stock.getBusinessId());// 设置当前企业ID
					tZStockService.update(orig_stock);
				} else {
					/* 新增库存主表 */
					orig_stock = orig_stock !=null ? orig_stock:new TZStock();
					setTzStock(tzInfo,orig_stock,account);
					tZStockService.create(orig_stock);
				}
				/* 新增库存详情表 */
				TZStockInfo tzStockInfoNew = setStockInfo(tzInfo, orig_stock, account);
				tZStockInfoService.create(tzStockInfoNew);
				/* 新增商品轨迹数据 */
				addTZProductTrail(tzInfo,account);
				
			}
			return account;
		} catch (JPAException e) {
			e.printStackTrace();
		} catch (DaoException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 生产商确认供应商主动进货后，才给供应商添加库存
	 * @param voNew
	 * @param orig_stock
	 * @param account
	 * @return
	 */
	private TZStockInfo setStockInfo(TZAccountInfo voNew, TZStock orig_stock,TZAccount account) {
		TZStockInfo info= null;
		if(orig_stock!=null){
			info= new TZStockInfo();
			info.setBusinessId(orig_stock.getBusinessId());
			info.setCreateType(1);
			info.setInDate(SDF.format(new Date()));
			info.setProductBatch(voNew.getProductBatch());
			info.setProductId(orig_stock.getProductId());
			info.setProductNum(voNew.getProductNum());
			info.setQsNo(voNew.getQsNo());
			info.setStockId(orig_stock.getId());
			info.setAccountId(account.getId());
			if(account.getType()==1){
				info.setType(0);
			}else{
				info.setType(1);
			}
		}
		return info;
	}

}