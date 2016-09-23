package com.lhfs.fsn.util;

import java.util.List;

public class PageUtil {
	private List<?> list; // 结果集容器
	private int pagenum; // 查看页码
	private int startindex; // 想看的页面数据该数据库哪个地方开始取
	private int totalrecord; // 总纪录数
	private int pagesize; // 页面大小
	private int totalpage; // 总页数
	private int perPageNumber=10 ; // 显示的页宽度
	private int startpage; 
	private int endpage;
	private String code;
	
	public PageUtil(){
		this.pagesize = 50;
	}

	public PageUtil(int totalrecord, int pagenum, int pagesize) {

		this.totalrecord = totalrecord;

		this.pagenum = pagenum;
		
		this.pagesize= pagesize;

		// 如果总记录数为0，则将总记录数强制设置为1
		if(totalrecord==0)
			this.totalrecord = 1;

		if(this.totalrecord % pagesize==0){ // 如果总记录数能整除每一页要显示的条数，则我们设置他们的商为总的页数
			this.totalpage = this.totalrecord / pagesize;
		}else{ // 否则就在他们的商上加1作为总页数
			this.totalpage = this.totalrecord / pagesize+1;
		}
	  
		// 数据库查询的起点=(当前页-1) * 每一页要显示的条数
		this.startindex = (this.pagenum - 1) * this.pagesize;
	   
		if(this.totalpage<=perPageNumber){// 如果总的页数小于限定的页数（10条），那么其实页数就是第一页，结束页数就是最后一页
			
			this.startpage = 1;
			this.endpage = this.totalpage;
			
		}else{ // 否则,该部分功能：当正在查看的页面显示在中间时

			this.startpage = this.pagenum-4;
			this.endpage = this.pagenum+5;

			if(this.startpage<1){
				this.startpage = 1;
				this.endpage = perPageNumber;
			}

			if(this.endpage>this.totalpage){
				this.endpage = this.totalpage;
				this.startpage = this.totalpage-(perPageNumber-1);
			}
		}
	}
	 
	public void setUpInfo(int totalrecord,int pagenum){

		this.totalrecord = totalrecord;
		this.pagenum = pagenum;
		
		if(totalrecord==0)
			this.totalrecord=1;
		
		if(this.totalrecord%this.pagesize==0){
			this.totalpage = this.totalrecord/this.pagesize;
		}else{
			this.totalpage = this.totalrecord/this.pagesize+1;
		}
		
		this.startindex = (this.pagenum-1)*this.pagesize;
   
		if(this.totalpage<=perPageNumber){
			this.startpage = 1;
			this.endpage = this.totalpage;
		}else{
			this.startpage = this.pagenum-4;
			this.endpage = this.pagenum+5;
			if(this.startpage<1){
				this.startpage = 1;
				this.endpage = perPageNumber;
			}

			if(this.endpage>this.totalpage){
				this.endpage = this.totalpage;
				this.startpage = this.totalpage-(perPageNumber-1);
			}
		}
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	public int getPagenum() {
		return pagenum;
	}


	public void setPagenum(int pagenum) {
		this.pagenum = pagenum;
	}


	public int getStartindex() {
		return startindex;
	}


	public void setStartindex(int startindex) {
		this.startindex = startindex;
	}


	public int getTotalrecord() {
		return totalrecord;
	}


	public void setTotalrecord(int totalrecord) {
		this.totalrecord = totalrecord;
	}


	public int getPagesize() {
		return pagesize;
	}


	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}


	public int getTotalpage() {
		return totalpage;
	}


	public void setTotalpage(int totalpage) {
		this.totalpage = totalpage;
	}


	public int getPerPageNumber() {
		return perPageNumber;
	}


	public void setPerPageNumber(int perPageNumber) {
		this.perPageNumber = perPageNumber;
	}


	public int getStartpage() {
		return startpage;
	}


	public void setStartpage(int startpage) {
		this.startpage = startpage;
	}


	public int getEndpage() {
		return endpage;
	}


	public void setEndpage(int endpage) {
		this.endpage = endpage;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
