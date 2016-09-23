package com.gettec.fsnip.fsn.vo.sales;

import java.util.List;

/**
 * 资料排序vo
 * @author tangxin 2015-05-07
 */
public class DataSortVO {

	private List<SortFieldValueVO> listSortCertVO; //三证、荣誉证书、生产许可证
	private List<SortFieldValueVO> listSortAlbumVO; //企业掠影
	private List<SortFieldValueVO> listSortProductVO; //产品排序信息
	private List<SortFieldValueVO> listSortSalesCaseVO; //销售案例
	
	public List<SortFieldValueVO> getListSortCertVO() {
		return listSortCertVO;
	}
	public void setListSortCertVO(List<SortFieldValueVO> listSortCertVO) {
		this.listSortCertVO = listSortCertVO;
	}
	public List<SortFieldValueVO> getListSortAlbumVO() {
		return listSortAlbumVO;
	}
	public void setListSortAlbumVO(List<SortFieldValueVO> listSortAlbumVO) {
		this.listSortAlbumVO = listSortAlbumVO;
	}
	public List<SortFieldValueVO> getListSortProductVO() {
		return listSortProductVO;
	}
	public void setListSortProductVO(List<SortFieldValueVO> listSortProductVO) {
		this.listSortProductVO = listSortProductVO;
	}
	public List<SortFieldValueVO> getListSortSalesCaseVO() {
		return listSortSalesCaseVO;
	}
	public void setListSortSalesCaseVO(List<SortFieldValueVO> listSortSalesCaseVO) {
		this.listSortSalesCaseVO = listSortSalesCaseVO;
	}

}
