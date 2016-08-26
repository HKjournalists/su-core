package com.gettec.fsnip.fsn.transfer;

import java.util.List;

import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.test.TestResult;

public class TestResultTransfer {
	public static TestResult transfer(TestResult testResult){
		if(testResult.getBrand()!=null){
			BusinessUnit businessUnit=BusinessUnitTransfer.transfer(testResult.getBrand().getBusinessUnit());
			testResult.getBrand().setBusinessUnit(businessUnit);
		}
		if(testResult.getSample()!=null&&testResult.getSample().getProducer()!=null){
			BusinessUnit bu=BusinessUnitTransfer.transfer(testResult.getSample().getProducer());
			testResult.getSample().setProducer(bu);
		}
		if(testResult.getSample()!=null&&testResult.getSample().getProduct()!=null){
			ProductTransfer.transfer(testResult.getSample().getProduct());
		}
		if(testResult.getTestee()!=null){
			BusinessUnit testee=BusinessUnitTransfer.transfer(testResult.getTestee());
			testResult.setTestee(testee);
		}
		return testResult;
	}
	
	public static List<TestResult> transfer(List<TestResult> testResultList){
		for(TestResult tr:testResultList){
			transfer(tr);
		}
		return testResultList;
	}
}
