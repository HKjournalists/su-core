package com.tzapp.fsn.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import com.gettec.fsnip.fsn.dao.account.TZAccountDAO;
import com.gettec.fsnip.fsn.dao.account.TZBusAccountOutDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.account.TZAccount;
import com.gettec.fsnip.fsn.model.account.TZAccountInfo;
import com.gettec.fsnip.fsn.model.account.TZBusAccountOut;
import com.gettec.fsnip.fsn.model.account.TZBusaccountInfoOut;
import com.gettec.fsnip.fsn.model.account.TZProductTrail;
import com.gettec.fsnip.fsn.model.account.TZStock;
import com.gettec.fsnip.fsn.model.account.TZStockInfo;
import com.gettec.fsnip.fsn.model.test.ImportedProductTestResult;
import com.gettec.fsnip.fsn.model.test.ReportFlowLog;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.service.account.TZAccountInfoService;
import com.gettec.fsnip.fsn.service.account.TZAccountService;
import com.gettec.fsnip.fsn.service.account.TZBusAccountOutService;
import com.gettec.fsnip.fsn.service.account.TZBusaccountInfoOutService;
import com.gettec.fsnip.fsn.service.account.TZProductTrailService;
import com.gettec.fsnip.fsn.service.account.TZStockInfoService;
import com.gettec.fsnip.fsn.service.account.TZStockService;
import com.gettec.fsnip.fsn.service.test.ImportedProductTestResultService;
import com.gettec.fsnip.fsn.service.test.TestResultHandlerService;
import com.gettec.fsnip.fsn.util.StatisticsClient;
import com.gettec.fsnip.fsn.vo.account.AccountOutVO;
import com.lhfs.fsn.dao.testReport.TestReportDao;
import com.lhfs.fsn.service.testReport.TestReportService;
import com.tzapp.fsn.dao.TzAppDao;
import com.tzapp.fsn.service.TzAppService;
import com.tzapp.fsn.util.StringUtil;
import com.tzapp.fsn.vo.ConfirmReceiptVO;
import com.tzapp.fsn.vo.TzAppBusInfoVO;
import com.tzapp.fsn.vo.TzAppProductDetailVO;
import com.tzapp.fsn.vo.TzAppReceiptVO;
import com.tzapp.fsn.vo.TzAppReportAndProductDetailVO;
import com.tzapp.fsn.vo.TzAppRequestParamVO;
import com.tzapp.fsn.vo.TzAppSearchAndScanVO;
import com.tzapp.fsn.vo.TzAppTestProperty;

@Service("tzAppService")
public class TzAppServiceImpl implements TzAppService{

	@Autowired TzAppDao tzAppDao;
	@Autowired TZBusaccountInfoOutService tZBusaccountInfoOutService;
	@Autowired TZAccountDAO tZAccountDAO;
	@Autowired TZBusAccountOutDAO accountOutDAO;
	@Autowired TZStockService tzStockService;
	@Autowired TZProductTrailService tzProductTrailService;
	@Autowired TZAccountService tZAccountService;
	@Autowired TZAccountInfoService tZAccountInfoService;
	@Autowired TZBusAccountOutService accountOutService;
	@Autowired TZBusaccountInfoOutService busaccountInfoOutService;
	@Autowired TZStockService tZStockService;
	@Autowired TZStockInfoService tZStockInfoService;
	@Autowired private TestReportService testReportService;
	@Autowired private StatisticsClient staClient;
	@Autowired private TestReportDao testReportDao;
	@Autowired private ImportedProductTestResultService importedProductTestResultService;
	@Autowired private TestResultHandlerService testResultHandlerService;
	
	SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 根据当前登录组织机构ID获取企业名称
	 * desc:用于台账app登陆成功后显示企业名称
	 */
	@Override
	public TzAppSearchAndScanVO getBusNameByOrg(TzAppSearchAndScanVO vo) throws ServiceException {
		try {
			return tzAppDao.getBusNameByOrg(vo);
		} catch (Exception e) {
			throw new ServiceException("TzAppServiceImpl-->getBusNameByOrg()根据当前登录组织机构ID获取企业名称，出现异常！",e);
		}
	}
	
	/**
	 * 根据扫描条码加载收货单列表
	 */
	@Override
	public Model loadReceipt(Model model,TzAppSearchAndScanVO paramVo) throws ServiceException{
		try {
			List<TzAppReceiptVO> list = tzAppDao.loadReceipt(paramVo);
			model.addAttribute("total", tzAppDao.receiptOrRefuseTotal(paramVo));
			model.addAttribute("data", list);
			return model;
		} catch (Exception e) {
			throw new ServiceException("TzAppServiceImpl-->loadReceipt()根据扫描条码加载收货单列表，出现异常！",e);
		}
	}

	/**
	 * 点击收货单的某行查看收货单详情
	 */
	@Override
	public Model loadReceiptDetail(Model model, TzAppRequestParamVO requestParamVO)
			throws ServiceException {
		try {
			List<TzAppProductDetailVO> list = tzAppDao.loadReceiptDetail(requestParamVO);
			//根据收货单ID获取供应商名和营业执照号
			TzAppRequestParamVO busInfo = tzAppDao.getBusInfo(requestParamVO);
			if(busInfo!=null){
				model.addAttribute("busId", busInfo.getBusId());
				model.addAttribute("busName", busInfo.getOutBusName());
				model.addAttribute("licNo", busInfo.getOutLicNo());
				model.addAttribute("dealDate", busInfo.getDealDate());
				model.addAttribute("refuseReason", busInfo.getRefuseReason());
			}else{
				model.addAttribute("busId", "");
				model.addAttribute("busName", "");
				model.addAttribute("licNo", "");
				model.addAttribute("dealDate", "");
				model.addAttribute("refuseReason", "");
			}
			return model.addAttribute("data", list);
		} catch (Exception e) {
			throw new ServiceException("TzAppServiceImpl-->loadReceiptDetail()点击收货单的某行查看收货单详情，出现异常！",e);
		}
	}

	/**
	 * 根据台账信息ID加载产品详情
	 */
	@Override
	public Model lookProductDetail(Model model, TzAppRequestParamVO busInfo)
			throws ServiceException {
		try {
			List<TzAppTestProperty> testPropertyList =null;
			TzAppReportAndProductDetailVO repPro = tzAppDao.lookProductDetail(busInfo);
			if(repPro!=null&&repPro.getId()>0){
				busInfo.setReportId(repPro.getId());//设置报告ID
				testPropertyList = tzAppDao.getTestPropertyList(busInfo);
			}
			model.addAttribute("product", repPro);
			model.addAttribute("data", testPropertyList);
			return model;
		} catch (Exception e) {
			throw new ServiceException("TzAppServiceImpl-->lookProductDetail()根据台账信息ID加载产品详情，出现异常！",e);
		}
	}

	/**
	 * 供应商详情
	 */
	@Override
	public Model lookBusInfo(Model model, TzAppRequestParamVO requestParam)
			throws ServiceException {
		try {
			TzAppBusInfoVO busInfo = tzAppDao.lookBusInfo(requestParam);
			model.addAttribute("busInfo", busInfo);
			return model;
		} catch (Exception e) {
			throw new ServiceException("TzAppServiceImpl-->lookBusInfo()根据收货单ID加载供应商详情，出现异常！",e);
		}
	}
	
	/**
	 * 确认收货
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
	public void confirmReceipt(ConfirmReceiptVO vo) throws ServiceException {
		try {
			List<Map<String,Object>> rvo = new ArrayList<Map<String,Object>>();
			Map<String,Object> map = null;
			if(vo!=null){
				String[] busT = tZAccountDAO.getBusType(vo.getOrg());//根据当前组织机构获取企业ID和类型    busT[0]=ID、busT[1]=Type  
				TZAccount  account = tZAccountService.findById(vo.getTzId());
				if (account != null && account.getOutStatus()!=null && account.getOutStatus()==1&&(account.getInStatus()==null||account.getInStatus()==0)) {//确认已批发且未收货
					account.setInStatus(1);
					account.setInDate(SDF.format(new Date()));
					tZAccountService.update(account);//更新主表
					//封装成rvo
					List<ConfirmReceiptVO> listVO = vo.getListVO();
					if(listVO!=null&&listVO.size()>0){
						for(int i=0;i<listVO.size();i++){
							ConfirmReceiptVO receipt = listVO.get(i);
							TZAccountInfo accountInfo = tZAccountInfoService.findById(receipt.getId());
							if(accountInfo!=null){
								map = new HashMap<String, Object>();
								map.put("id", accountInfo.getId());//台账信息表ID
								map.put("productId", accountInfo.getProductId());//产品ID
								map.put("qsNumber", accountInfo.getQsNo());//qs
								map.put("returnCount",receipt.getReturnCount());//实收数量
								map.put("countTotal", accountInfo.getProductNum());//批发总数量
								map.put("price", accountInfo.getProductPrice());//价格
								map.put("batch", accountInfo.getProductBatch());//批次
								map.put("productionDate", StringUtil.isNotEmpty(accountInfo.getProductionDate())?
										SDF.format(SDF.parse(accountInfo.getProductionDate())):"");//生产日期
								map.put("overDate", StringUtil.isNotEmpty(accountInfo.getOverDate())?
										SDF.format(SDF.parse(accountInfo.getOverDate())):"");//过期日期
								rvo.add(map);
							}
						}
					}
					/* 修改库存数量 */
					TZBusAccountOut abOut = updateStock2(rvo,Long.valueOf(busT[0]),account.getOutBusinessId(),vo.getTzId());
					/*创建退货信息*/
					if(abOut!=null){
						createOutproduct(abOut);
					}
				}
			}
		} catch (Exception e) {
			throw new ServiceException("TzAppServiceImpl-->confirmReceipt()确认收货，出现异常！",e);
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
				Long proId = (Long) rpvo.get("productId");
				Long returnCount = (Long)rpvo.get("returnCount") ;
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
				TZAccountInfo tZAccountInfo = tZAccountInfoService.findById(Long.valueOf((Long) rpvo.get("id")));
				tZAccountInfo.setRealNum(Long.valueOf(returnCount));
				tZAccountInfoService.update(tZAccountInfo);

				Long countTotal = (Long) rpvo.get("countTotal");
				if(countTotal>returnCount){
					/*创建退货详情信息 并添加到list中*/
					TZBusaccountInfoOut InfoOut = new TZBusaccountInfoOut();
					InfoOut.setProductId(Long.valueOf(proId));
					InfoOut.setProductNum(Long.valueOf(countTotal-returnCount));
					if(rpvo.get("price") instanceof Long){
						InfoOut.setProductPrice(BigDecimal.valueOf((Long)rpvo.get("price")));
					}else if(rpvo.get("price") instanceof Double){
						InfoOut.setProductPrice(BigDecimal.valueOf((Double)rpvo.get("price")));
					}else if(rpvo.get("price") instanceof BigDecimal){//台账app调用
						InfoOut.setProductPrice((BigDecimal)rpvo.get("price"));
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
	
	/**
	 * 修改库存详情表
	 * @throws ServiceException 
	 */
	private void addTZStockInfo(TZStock orig_stock, Map<String,Object> rpvo) throws ServiceException {
		Long returnCount =(Long) rpvo.get("returnCount");
		if(returnCount>0) {
			String qs = rpvo.get("qsNumber")!=null?rpvo.get("qsNumber").toString():"";
			Long proId = (Long) rpvo.get("productId");
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
	 * 确认退货
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void confirmReturnOfGoods(Long id) throws ServiceException {
		try {
			TZBusAccountOut out = accountOutDAO.findById(id);
			if(out!=null&&out.getOutStatus()!=1){
				AccountOutVO accountOut = new AccountOutVO();
				accountOut.setOutBusId(out.getOutBusId());
				//改变退货状态为已确认
				tzAppDao.confirmReturnOfGoods(id);
				//根据退货ID获取退货产品列表
				List<TZBusaccountInfoOut>  list = tZBusaccountInfoOutService.getListByOutId(id);
				TZBusaccountInfoOut orig_InfoOut = null;
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						orig_InfoOut = list.get(i);
						//把退货的产品添加到库存及溯源表
						tzStockService.updateTZStock(accountOut, orig_InfoOut);//跟新库存
						/* 创建台账轨迹 */
						tzProductTrailService.create(setTZProductTrail(orig_InfoOut,out));
					}
				}
			}
		} catch (Exception e) {
			throw new ServiceException("TzAppServiceImpl-->confirmReturnOfGoods()确认退货，出现异常！",e);
		}
		
	}
	
	/**
	 * 轨迹表信息设置
	 * @param orig_infoOut
	 * @param out
	 * @return
	 * @throws ServiceException
	 */
	 private TZProductTrail setTZProductTrail(TZBusaccountInfoOut orig_infoOut, TZBusAccountOut out) throws ServiceException {
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        TZProductTrail trail = new TZProductTrail();
	        trail.setProductId(orig_infoOut.getProductId());
	        trail.setProductNum(orig_infoOut.getProductNum());
	        trail.setInBusId(out.getInBusId());
	        trail.setProductBatch(orig_infoOut.getProductBatch());
	        trail.setAccountDate(sdf.format(new Date()));
	        trail.setOutBusId(out.getOutBusId());
	        trail.setType(1); // 退货台账轨迹
	        trail.setAccountId(out.getId());
	        try {
	            String barcode = tZAccountDAO.getBarcodeByProductId(orig_infoOut.getProductId());
	            trail.setBarcode(barcode);
	        } catch (DaoException daoe) {
	            throw new ServiceException(daoe.getMessage(),daoe.getException());
	        }
	        trail.setProductionDate(orig_infoOut.getProductionDate());
	        return trail;
	    }

	 /**
	  * 加载未审核的报告列表
	  */
	@Override
	public Model noCheck(Model model, TzAppSearchAndScanVO paramVo)
			throws ServiceException {
		try {
			List<TzAppReportAndProductDetailVO> list = tzAppDao.noCheck(paramVo);
			paramVo.setIsTotal(0);//报告总数标识
			model.addAttribute("total", tzAppDao.getTotal(paramVo));// 报告总数量
			paramVo.setIsTotal(1);//未审核报告总数标识
			model.addAttribute("noCheckCount", tzAppDao.getTotal(paramVo));//未审核的数量
			model.addAttribute("data", list);
			return model;
		} catch (Exception e) {
			throw new ServiceException("TzAppServiceImpl-->noCheck()加载未审核的报告列表，出现异常！",e);
		}
	}

	/**
	 * 根据报告ID获取审核详情
	 */
	@Override
	public Model reportlDetail(Model model, TzAppRequestParamVO busInfo)throws ServiceException{
		try {
			TzAppReportAndProductDetailVO vo = tzAppDao.reportlDetail(busInfo);
			model.addAttribute("data", vo);
			return model;
		} catch (Exception e) {
			throw new ServiceException("TzAppServiceImpl-->reportlDetail()根据报告ID获取审核详情，出现异常！",e);
		}
	}

	/**
	 * 确认报告审核通过或退回:直接调用FSN接口
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public void reportPass(TzAppSearchAndScanVO paramVo) throws ServiceException {
		try {
			updatePublishFlag(paramVo);
			/* 报告日志处理 */ 
			TestResult tr=testReportService.findById(paramVo.getReportId());
			if(paramVo.isPass()){
				ReportFlowLog rfl=new ReportFlowLog(tr.getId(),paramVo.getCurBusName(),tr.getSample().getProduct().getBarcode()
							,tr.getSample().getBatchSerialNo(),tr.getServiceOrder(),"完成商超报告审核通过操作");
				staClient.offer(rfl);//记录报告日志为异步处理
			}else{
				ReportFlowLog rfl=new ReportFlowLog(tr.getId(),paramVo.getCurBusName(),tr.getSample().getProduct().getBarcode()
						,tr.getSample().getBatchSerialNo(),tr.getServiceOrder(),"完成商超报告退回操作 退回原因："+paramVo.getReturnReason());
				staClient.offer(rfl);
			}
		} catch (Exception e) {
			throw new ServiceException("TzAppServiceImpl-->reportPass()确认报告审核通过或退回，出现异常！",e);
		}
	}
	
	/**
	 * 更改报告发布状态
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	private void updatePublishFlag(TzAppSearchAndScanVO paramVo) throws ServiceException {
		try {
			String publishFlag = String.valueOf(paramVo.getPublishFlag());
			//商超审核通过时 如果是进口食品报告 则不到testleb审核 直接发到大众门户
			if("6".equals(publishFlag)){
				ImportedProductTestResult impProTest=importedProductTestResultService.findByReportId(paramVo.getReportId());
				if(impProTest!=null){ //判断是否是进口食品报告
					publishFlag = "1";
				}
			}
			//testReportDao.updatePublishFlag(publishFlag.charAt(0), paramVo.getReportId(), paramVo.getReturnReason(),paramVo.getCurBusName());
			tzAppDao.updatePublishFlag(publishFlag.charAt(0), paramVo.getReportId(), paramVo.getReturnReason(),paramVo.getCurBusName());
			// 根据报告ID新建一条需要格式化的报告，并根据算法自动分配
			if("6".equals(publishFlag)){
				testResultHandlerService.createByReportId(paramVo.getReportId());
			}
		} catch (DaoException e) {
			throw new ServiceException("TestReportServiceImpl.updatePublishFlag()-->" + e.getMessage(), e.getException());
		}
	}

	/**
	 * 审核搜索
	 */
	@Override
	public Model searchReport(Model model, TzAppSearchAndScanVO paramVo)
			throws ServiceException {
		try {
			List<TzAppReportAndProductDetailVO> list = tzAppDao.searchReport(paramVo);
			paramVo.setIsTotal(0);//报告总数标识
			model.addAttribute("total", tzAppDao.getTotal(paramVo));// 报告总数量
			model.addAttribute("noCheckCount", tzAppDao.getScanTotal(paramVo));//未审核的数量
			model.addAttribute("data", list);
			return model;
		} catch (Exception e) {
			throw new ServiceException("TzAppServiceImpl-->searchReport()审核搜索，出现异常！",e);
		}
	}

	/**
	 * 获取快捷原因列表
	 */
	@Override
	public Model backResults(Model model, TzAppRequestParamVO busInfo)
			throws ServiceException {
		try {
			List<Map<String,String>> list = tzAppDao.backResults(busInfo);
			model.addAttribute("data", list);
			return model;
		} catch (Exception e) {
			throw new ServiceException("TzAppServiceImpl-->backResults()获取快捷原因列表，出现异常！",e);
		}
	}

	/**
	 * 初始化台账相关总数
	 * @author ChenXiaolin 2015-11-24
	 */
	@Override
	public Model loadTotal(Model model, TzAppSearchAndScanVO vo)
			throws ServiceException {
		try {
			vo.setConfirmTotal(0);//已收货标识
			Long receiptTotal = tzAppDao.loadTotal(vo);
			vo.setConfirmTotal(1);//待收货标识
			Long unReceiptTotal = tzAppDao.loadTotal(vo);
			vo.setIsTotal(1);//未审核报告总数标识
			model.addAttribute("noCheckCount", tzAppDao.getTotal(vo));//未审核的总数
			model.addAttribute("receiptTotal", receiptTotal);
			model.addAttribute("unReceiptTotal", unReceiptTotal);
			return model;
		} catch (Exception e) {
			throw new ServiceException("TzAppServiceImpl-->loadTotal()初始化台账相关总数，出现异常！",e);
		}
	}


}
