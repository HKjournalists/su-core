package com.gettec.fsnip.fsn.vo.sales;

import java.util.List;

/**
 * APP 资料中心VO
 * @author tangxin tangxin 2015-05-10
 *
 */
public class DataCenterVO {

	private List<ContractVOAPP> Contracts;
	private List<MaterialsVO> Materials;
	
	public List<ContractVOAPP> getContracts() {
		return Contracts;
	}
	public void setContracts(List<ContractVOAPP> contracts) {
		Contracts = contracts;
	}
	public List<MaterialsVO> getMaterials() {
		return Materials;
	}
	public void setMaterials(List<MaterialsVO> materials) {
		Materials = materials;
	}
	
	public DataCenterVO(){
		super();
	}
	
	public DataCenterVO(List<ContractVOAPP> contracts,
			List<MaterialsVO> materials) {
		super();
		Contracts = contracts;
		Materials = materials;
	}
	
}
