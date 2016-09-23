package com.gettec.fsnip.fsn.dao.test;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.test.TestProperty;
/**
 * TestProperty customized operation implementation
 * 
 * @author Ryan Wang
 */
 public interface TestPropertyDAO extends BaseDAO<TestProperty>{

	Long countByReportId(Long repId) throws DaoException;

	List<TestProperty> getListByReportIdWithPage(Long repId, 
			int from, int size) throws DaoException;

	/**
	 * 按报告Id查找检测项目列表<br>
	 * 最后更新：zhangHui 2015/5/8<br>
	 * 更新内容：未结构化检测报告无需返回检测项目数据<br>
	 */
	List<TestProperty> findByReportId(Long reportId) throws DaoException;

	List<String> getListOfColumeValue(int columnName, String keyword, int page,
			int pageSize) throws DaoException;

	/**
	 * 根据报告ID删除检测项目
	 * @param reportid
	 * @throws DaoException
	 * @author LongXianZhen
	 */
	public void deleByTestResultId(Long reportid)throws DaoException;

	/**
	 * 根据报告id查找检测项目（无需返回id）
	 * @author ZhangHui 2015/5/8
	 */
	public List<TestProperty> findByReportIdWithoutId(Long reportId)
			throws DaoException;
}