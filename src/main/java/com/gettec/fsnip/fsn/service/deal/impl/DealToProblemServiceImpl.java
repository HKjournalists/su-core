package com.gettec.fsnip.fsn.service.deal.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.deal.DealToProblemDAO;
import com.gettec.fsnip.fsn.enums.DealProblemTypeEnums;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.deal.DealProblem;
import com.gettec.fsnip.fsn.model.deal.DealToProblem;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.deal.DealToProblemService;
import com.gettec.fsnip.fsn.vo.deal.DealProblemBean;
import com.gettec.fsnip.fsn.vo.deal.DealProblemLogBean;
import com.gettec.fsnip.fsn.vo.deal.DealProblemLogVO;
import com.gettec.fsnip.fsn.vo.deal.DealProblemVO;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

@Service(value="dealToProblemService")
public class DealToProblemServiceImpl extends BaseServiceImpl<DealToProblem, DealToProblemDAO> implements DealToProblemService{
	@Autowired
	DealToProblemDAO dealToProblemDAO;
	
	@Autowired 
	private BusinessUnitService businessUnitService;
	
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	public DealToProblemDAO getDAO() {
		return dealToProblemDAO;
	}
	@Override
	public long getdealToProblemTotal(String barcode,long businessId) {
		return dealToProblemDAO.getdealToProblemTotal(barcode,businessId);
	}
	@Override
	public List<DealToProblem> getdealToProblemList(int page, int pageSize,
			String barcode,long businessId) {
		List<DealToProblem> dealToProblem = dealToProblemDAO.getdealToProblemList(page,pageSize,barcode,businessId);
		for (DealToProblem problem : dealToProblem) {
			problem.setDealType(problem.getDealStatus().getDealStatus());
			problem.setStatus(problem.getCommitStatus().getCommitStatus());
			problem.setProblem(problem.getProblemType().getProblemType());
		}
		return dealToProblem;
	}
	/**
	 * 对象转换之前做数据业务处理
	 */
	private JSONObject getJsonDealToProblem(DealToProblem vo) {
		DealProblemVO e = new DealProblemVO();
			e.setScode(vo.getScode());
			e.setQyname(vo.getBusinessName());
			e.setQylicense(vo.getLicenseNo());
			e.setBarcode(vo.getBarcode());
			e.setProductTime(vo.getProductionDate());
			e.setProductName(vo.getProductName());
			e.setDqcode(vo.getProblemCode());
			e.setWtcode(vo.getProblemType().getId());
		    e.setTjtime(vo.getCreateTime());
		    e.setYjtime(vo.getDealTime());
		    e.setWctime(vo.getFinishTime());
		    e.setDqcode(vo.getProblemCode());
		    e.setLongitude(vo.getLongitude());
		    e.setLatitude(vo.getLatitude());
		    e.setAddress(vo.getAddress());
		    e.setXxly(vo.getInfoSource());
		    e.setMstatus(2);
		    e.setRemark(vo.getRemark());
		    e.setBackup(vo.getBackup());
		    e.setAddress(vo.getAddress());
		    
		    DealProblemBean bean = new DealProblemBean();
		    bean.setData(e);
		    bean.setType("1");
		return JSONObject.fromObject(bean);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public JSONObject getDealProblemLogVO(long id,AuthenticateInfo info) {
		try {
			DealToProblem dtpvo = dealToProblemDAO.findById(id);
			Date FinishTime = new Date();
			dtpvo.setDealStatus(DealProblemTypeEnums.ONE);
			  dtpvo.setFinishTime(FinishTime);
			  DealProblemLogVO logVO = new DealProblemLogVO();
				    logVO.setBarcode(dtpvo.getBarcode());
					logVO.setPcode(dtpvo.getScode());
					logVO.setFstatus(2);
					logVO.setBstatus(3);
					logVO.setOperator(info.getUserName());
					logVO.setProductname(dtpvo.getProductName());
//					logVO.setOptime(FinishTime);
//					logVO.setProducttime(dtpvo.getProductionDate());
					logVO.setOpunit(dtpvo.getBusinessName());
					
					DealProblemLogBean logbean = new DealProblemLogBean();
					logbean.setData(logVO);
					logbean.setType("2");
				JSONObject json = 	JSONObject.fromObject(logbean);
				json.getJSONObject("data").put("optime", formatter.format(FinishTime));
				json.getJSONObject("data").put("producttime", dtpvo.getProductionDate()==null?null:formatter.format(dtpvo.getProductionDate()));
			return json;
		} catch (JPAException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 数据转换
	 * @param dealvo
	 * @return
	 */
	private DealToProblem getDealToProblem(DealProblem dealvo) {
		
		DealToProblem newvo = new DealToProblem();
		//通过企业名称查询企业的ID
		BusinessUnit businessUnit = businessUnitService.getBusinessUnitByCondition(dealvo.getBusinessName(),null,null);
		
		if (businessUnit != null) {
			newvo.setBusinessId(businessUnit.getId());
		}else{
			newvo.setBusinessId(dealvo.getBusinessId());	
		}
		newvo.setLicenseNo(dealvo.getLicenseNo());
		newvo.setProductionDate(dealvo.getProductTime());
		newvo.setProductName(dealvo.getProductName());
		newvo.setBusinessName(dealvo.getBusinessName());
		newvo.setBarcode(dealvo.getBarcode());
		newvo.setProblemType(dealvo.getProblemType());
		newvo.setProblemCode(dealvo.getCounties());
		newvo.setCreateTime(dealvo.getCreateTime());
		newvo.setDealTime(dealvo.getRequestDealTime());
		newvo.setCommitStatus(dealvo.getCommitStatus());
		newvo.setInfoSource(dealvo.getOrigin().getId() + "");
		newvo.setLatitude(dealvo.getLatitude());
		newvo.setLongitude(dealvo.getLongitude());
		newvo.setScode(dealvo.getScode());
		newvo.setRemark(dealvo.getRemark());
		newvo.setDealStatus(DealProblemTypeEnums.ZERO);
		return newvo;
	}
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public JSONObject getJsonDealToProblem(DealProblem sendproblem,long pageStatus) {
		try {
			DealToProblem dtpvo = this.getDealToProblem(sendproblem);
			
			if(pageStatus == 0){
				dealToProblemDAO.persistent(dtpvo);
			}
			return this.getJsonDealToProblem(dtpvo);
		} catch (JPAException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public void getNoticeDealToProblem(DealProblem sendproblem) {
		try {
			DealToProblem dtpvo = this.getDealToProblem(sendproblem);
			dealToProblemDAO.persistent(dtpvo);
		} catch (JPAException e) {
			e.printStackTrace();
		}	
	}
	
	private DealToProblem getScodeDealToProblem(DealProblemVO obj) throws ParseException {
		DealToProblem e = null;
		//通过企业名称查询企业信息
		BusinessUnit businessUnit = businessUnitService.getBusinessUnitByCondition(obj.getQyname(),null,null);
		//如果企业信息不存在,则不把给问题通知企业,即:不处理
		if(businessUnit != null){
			e = new DealToProblem();
			e.setScode(obj.getScode());
			e.setBusinessId(businessUnit.getId());
			e.setJgname(obj.getJgname());
			e.setBusinessName(obj.getQyname());
			e.setLicenseNo(obj.getQylicense());
			e.setBarcode(obj.getBarcode());
			e.setProductionDate(obj.getProductTime());
			e.setProductName(obj.getProductName());
			e.setProblemType(DealProblemTypeEnums.typeEnumeId(obj.getWtcode()));
		    e.setCreateTime(obj.getTjtime());
		    e.setDealTime(obj.getYjtime());
		    e.setFinishTime(null);
		    e.setProblemCode(obj.getDqcode());
		    e.setLongitude(obj.getLongitude());
		    e.setLatitude(obj.getLatitude());
		    e.setAddress(obj.getAddress());
		    e.setInfoSource(obj.getXxly());
		    e.setRemark(obj.getRemark());
		    e.setBackup(obj.getBackup());
		    e.setAddress(obj.getAddress());
		    e.setDealStatus(DealProblemTypeEnums.ZERO);
		    e.setCommitStatus(DealProblemTypeEnums.ZERO);
		}
		return e;
	}
	@SuppressWarnings("static-access")
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void setDealToProblem(JSONObject obj) {
		try {
			if(obj.get("type").equals("2")){
				//根据流水号去查询信息
				DealToProblem dtpvo = dealToProblemDAO.getFindPcodeOrScode(obj.getJSONObject("data").get("pcode").toString());
				//获取到信息了，则修改状态，如果没有查找到该信息，则不去处理
				if(dtpvo != null){
					dtpvo.setDealStatus(DealProblemTypeEnums.ONE);
					dtpvo.setFinishTime(formatter.parse(obj.getJSONObject("data").getString("optime")));
					try {
						this.update(dtpvo);
					} catch (ServiceException e) {
						e.printStackTrace();
					}
				}
			}else if(obj.get("type").equals("1")){
				DealToProblem dtpvo = dealToProblemDAO.getFindPcodeOrScode(obj.getJSONObject("data").get("scode").toString());
				if(dtpvo == null){
					DealProblemBean dpbvo = (DealProblemBean)obj.toBean(obj,DealProblemBean.class);
					dpbvo.getData().setProductTime(obj.getJSONObject("data").getString("productTime")==null?null:formatter.parse(obj.getJSONObject("data").getString("productTime")));
					dpbvo.getData().setTjtime(obj.getJSONObject("data").getString("tjtime")==null?null:formatter.parse(obj.getJSONObject("data").getString("tjtime")));
					dpbvo.getData().setYjtime(obj.getJSONObject("data").getString("yjtime")==null?null:formatter.parse(obj.getJSONObject("data").getString("yjtime")));
					DealToProblem dtpBean = this.getScodeDealToProblem(dpbvo.getData());
					
					if(dtpBean !=null){
						dealToProblemDAO.persistent(dtpBean);
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (JPAException e) {
			e.printStackTrace();
		}
	}
}
