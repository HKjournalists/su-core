package com.gettec.fsnip.fsn.service.deal.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.deal.DealProblemDAO;
import com.gettec.fsnip.fsn.enums.DealProblemTypeEnums;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.deal.DealProblem;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.deal.DealProblemService;

@Service
public class DealProblemServiceImpl extends BaseServiceImpl<DealProblem, DealProblemDAO>
									implements DealProblemService{

	@Autowired
	private DealProblemDAO dealProblemDAO;
	
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public DealProblemDAO getDAO() {
		return dealProblemDAO;	
	}


	@Override
	public List<DealProblem> getProblemList(int page,int pageSize,String businessName,String licenseNo,String barcode,long businessId,int status) throws DaoException {
		List<DealProblem> dealProblemsList = null;
		DealProblemTypeEnums comStatus = DealProblemTypeEnums.ZERO;
		if(status == 1){
			comStatus = DealProblemTypeEnums.ONE;
		} else if(status == 2){
			comStatus = DealProblemTypeEnums.TWO;
		}
		
		 dealProblemsList  = this.getDAO().getProblemList(page,pageSize,businessName,licenseNo,barcode,businessId,comStatus);
		
		for (DealProblem dealProblem : dealProblemsList) {
			//获取枚举
			String problemType = dealProblem.getProblemType()==null?"":dealProblem.getProblemType().getProblemType();
			String origin = dealProblem.getOrigin()==null?"":dealProblem.getOrigin().getOrigin();
			String commitStatus = dealProblem.getCommitStatus()==null?"":dealProblem.getCommitStatus().getCommitStatus();
			String complainStatus = dealProblem.getComplainStatus()==null?"":dealProblem.getComplainStatus().getComplainStatus();
			
			//页面直接显示中文状态
			dealProblem.setShowProblemType(problemType);
			dealProblem.setShowOrigin(origin);
			dealProblem.setShowCommitStatus(commitStatus);
			dealProblem.setShowComplainStatus(complainStatus);
		}
		return dealProblemsList;
	}




	//保存大众门户传过来的参数
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(DealProblem dealProblem) {
			try {
				/**
				 * 流水号自动生成
				 */
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); 
				String nowdate = sdf.format(new Date()); 
				String st = Math.random()+"";
				String s = st.substring(2, 6);
				dealProblem.setScode(nowdate+s);
				dealProblemDAO.persistent(dealProblem);
			} catch (JPAException e) {
				e.printStackTrace();
			}
	}


	//获取总记录数
	@Override
	public long getTotalRecord(String businewssName, String licenseNo,
			String barcode,long businessId ,int status) throws DaoException {
		
		/*DealProblemTypeEnums comStatus = DealProblemTypeEnums.ZERO;
		if(status == 1){
			comStatus = DealProblemTypeEnums.ONE;
		} else if(status == 2){
			comStatus = DealProblemTypeEnums.TWO;
		}*/
		
		return this.getDAO().getTotalRecord(businewssName,licenseNo,barcode,businessId,status);
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public DealProblem  noticeComplainById(long id,long status) {
		try {
			DealProblem eProblem = dealProblemDAO.findById(id);
			if (status == 0) {
				eProblem.setCommitStatus(DealProblemTypeEnums.ZERO);
				eProblem.setComplainStatus(DealProblemTypeEnums.ONE);
			}else if(status == 1){
				eProblem.setCommitStatus(DealProblemTypeEnums.ONE);
				eProblem.setComplainStatus(DealProblemTypeEnums.ONE);
			}else{
				eProblem.setCommitStatus(DealProblemTypeEnums.TWO);
				eProblem.setComplainStatus(DealProblemTypeEnums.TWO);
			}
//		this.getDAO().ignoreComplainById(id);
			return eProblem;
		} catch (JPAException e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteDealProblemById(long id) {
		try {
			DealProblem dealProblem = this.getDAO().findById(id);
			this.getDAO().remove(dealProblem);
		} catch (JPAException e) {
			e.printStackTrace();
		}
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void backComplain(long id) {
		try {
			DealProblem problem = dealProblemDAO.findById(id);
			problem.setCommitStatus(DealProblemTypeEnums.ZERO);
			problem.setComplainStatus(DealProblemTypeEnums.ZERO);
		} catch (JPAException e) {
			e.printStackTrace();
		}
		
		
		
		
	}







	
	
	
	
	
	
	
	
	
	
	
	
	
}
