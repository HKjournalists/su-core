package com.gettec.fsnip.fsn.dao.data_access.impl;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.data_access.Western_electronicDAO;
import com.gettec.fsnip.fsn.model.business.BusinessBrand;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.OrganizingInstitution;
import com.gettec.fsnip.fsn.model.business.TaxRegisterInfo;
import com.gettec.fsnip.fsn.model.data_access.Western_electronic;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductCategoryInfo;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.model.trace.TraceData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;

/**
 * @author litg
 */
@Repository(value = "western_electronicDAO")
public class Western_electronicDAOImpl extends BaseDAOImpl<Western_electronic> implements Western_electronicDAO {
	
	@SuppressWarnings("unchecked")
	public int getCount() throws Exception{
		StringBuilder sql = new StringBuilder();
		sql.append("select count(0) from western_electronic");
		Query query = entityManager.createNativeQuery(sql.toString());
		List<Object> results = query.getResultList();
		Integer count = 0;
		if (results != null && !results.isEmpty()) {
			String s_count = results.get(0).toString();
			count = Integer.parseInt(s_count);
		}
		return count;
	}
	
	@SuppressWarnings("unchecked")
	public Western_electronic getLast() throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from western_electronic order by id desc limit 1");
		Query query = entityManager.createNativeQuery(sql.toString());
		List<Object> data_list = query.getResultList();
		Western_electronic western_electronic = null;
		if (data_list != null && !data_list.isEmpty()) {
			western_electronic = new Western_electronic();
			Object[] data = (Object[])data_list.get(0);
			western_electronic.setStatus(data[1].toString());
			western_electronic.setPercent(data[2].toString());
			western_electronic.setComplete_num(data[3] == null ? 0 : Integer.parseInt(data[3].toString()));
			western_electronic.setCompany_num(data[4] == null ? 0 : Integer.parseInt(data[4].toString()));
			western_electronic.setProduct_num(data[5] == null ? 0 : Integer.parseInt(data[5].toString()));
			western_electronic.setReport_num(data[6] == null ? 0 : Integer.parseInt(data[6].toString()));
			western_electronic.setTrace_num(data[7] == null ? 0 : Integer.parseInt(data[7].toString()));
			western_electronic.setSuccess_num(data[8] == null ? 0 : Integer.parseInt(data[8].toString()));
			western_electronic.setFail_num(data[9] == null ? 0 : Integer.parseInt(data[9].toString()));
		}
		return western_electronic;
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String,Integer> save_western_data(BusinessUnit businessUnit,ArrayList<ProductInstance> product_list,
			ArrayList<TestResult> report_list,ArrayList<TraceData> trace_list) throws Exception {
		StringBuilder sql = new StringBuilder();
		int product_num = 0;
		int report_num = 0;
		int trace_num = 0;
		HashMap<String,Integer> result_map = new HashMap<String,Integer>();
		//增加企业信息
		sql.append("insert into business_unit(name,address,contact,telephone,region,license_no) values ('");
		sql.append(businessUnit.getName());
		sql.append("','");
		sql.append(businessUnit.getAddress());
		sql.append("','");
		sql.append(businessUnit.getContact());
		sql.append("','");
		sql.append(businessUnit.getTelephone());
		sql.append("','");
		sql.append(businessUnit.getRegion());
		sql.append("','");
		sql.append(businessUnit.getLincesNo());
		sql.append("')");
		Query query = entityManager.createNativeQuery(sql.toString());
		query.executeUpdate();
		
		//获取企业id
		sql.delete(0, sql.length());
		sql.append("select last_insert_id()");
		query = entityManager.createNativeQuery(sql.toString());
		Integer business_id = null;
		List<Object> results = query.getResultList();
		if (results != null && !results.isEmpty()) {
			String s_id = results.get(0).toString();
			business_id = Integer.parseInt(s_id);
		}
		
		sql.delete(0, sql.length());
		//增加营业执照信息
		sql.append("insert into license_info(license_no,license_name) values ('");
		sql.append(businessUnit.getLincesNo());
		sql.append("','");
		sql.append(businessUnit.getName());
		sql.append("')");
		query = entityManager.createNativeQuery(sql.toString());
		query.executeUpdate();
		
		sql.delete(0, sql.length());
		//增加组织机构代码信息
		OrganizingInstitution organizingInstitution = businessUnit.getOrgInstitution();
		sql.append("insert into organizing_institution(org_code,org_name,org_address) values ('");
		sql.append(organizingInstitution.getOrgCode());
		sql.append("','");
		sql.append(organizingInstitution.getOrgName());
		sql.append("','");
		sql.append(organizingInstitution.getOrgAddress());
		sql.append("')");
		query = entityManager.createNativeQuery(sql.toString());
		query.executeUpdate();
		
		sql.delete(0, sql.length());
		//增加税务登记信息
		TaxRegisterInfo taxRegisterInfo = businessUnit.getTaxRegister();
		sql.append("insert into tax_register_cert(taxer_name,address) values ('");
		sql.append(taxRegisterInfo.getTaxerName());
		sql.append("','");
		sql.append(taxRegisterInfo.getAddress());
		sql.append("')");
		query = entityManager.createNativeQuery(sql.toString());
		query.executeUpdate();
		
		//增加税务登记图片信息
		Set<Resource> taxAttachments = taxRegisterInfo.getTaxAttachments();
		if (taxAttachments != null && taxAttachments.size() > 0) {
			for (Iterator<Resource> iter = taxAttachments.iterator(); iter.hasNext();) {
				Resource resource = iter.next();
				sql.delete(0, sql.length());
				sql.append("insert into T_TEST_RESOURCE(URL,origin) values ('");
				sql.append(resource.getUrl());
				sql.append("','");
				sql.append(resource.getOrigin());
				sql.append("')");
				query = entityManager.createNativeQuery(sql.toString());
				query.executeUpdate();
			}
		}
		
		//增加产品信息
		if (product_list != null && !product_list.isEmpty()) {
			Integer product_instance_id = null;
			Integer brand_id = null;
			Integer product_id = null;
			for (ProductInstance productInstance : product_list) {
				Product product = productInstance.getProduct();
				String kind = product.getSecondCategoryCode();//类别
				Integer product_category_id = null;
				if (kind == null || "".equals(kind)) {
					sql.delete(0, sql.length());
					sql.append("select id from product_category where name = '其他' limit 1");
					query = entityManager.createNativeQuery(sql.toString());
					List<Object> results2 = query.getResultList();
					if (results2 != null && !results2.isEmpty()) {
						String s_id = results2.get(0).toString();
						product_category_id = Integer.parseInt(s_id);
					}
				} else {
					sql.delete(0, sql.length());
					sql.append("select id from product_category where name = '"+kind+"' limit 1");
					query = entityManager.createNativeQuery(sql.toString());
					List<Object> results2 = query.getResultList();
					if (results2 != null && !results2.isEmpty()) {
						String s_id = results2.get(0).toString();
						product_category_id = Integer.parseInt(s_id);
					}
					
					if (product_category_id == null) {
						//未获取到产品类别，新增一个类别
						sql.delete(0, sql.length());
						sql.append("insert into product_category(name,display) values ('");
						sql.append(kind);
						sql.append("','");
						sql.append(kind);
						sql.append("')");
						query = entityManager.createNativeQuery(sql.toString());
						query.executeUpdate();
						
						sql.delete(0, sql.length());
						sql.append("select last_insert_id()");
						query = entityManager.createNativeQuery(sql.toString());
						List<Object> results1 = query.getResultList();
						if (results1 != null && !results1.isEmpty()) {
							String s_id = results1.get(0).toString();
							product_category_id = Integer.parseInt(s_id);
						}
					}
				}
				if (product_category_id == null) {
					//没有品牌
					continue;
				}
				
				Set<ProductCategoryInfo> regularity = product.getRegularity();//执行标准
				Integer category_id = null;
				if (regularity != null && regularity.size() > 0) {
					for (Iterator<ProductCategoryInfo> iter1 = regularity.iterator(); iter1.hasNext();) {
						ProductCategoryInfo productCategoryInfo = iter1.next();
						sql.delete(0, sql.length());
						sql.append("insert into product_category_info(name,category_flag,category_id,addition) values ('");
						sql.append(productCategoryInfo.getName());
						sql.append("',");
						sql.append(productCategoryInfo.getCategoryFlag() == true ? 1 : 0);
						sql.append(",");
						sql.append(product_category_id);
						sql.append(",");
						sql.append(0);
						sql.append(")");
						query = entityManager.createNativeQuery(sql.toString());
						query.executeUpdate();
						
						//获取产品类别id
						sql.delete(0, sql.length());
						sql.append("select last_insert_id()");
						query = entityManager.createNativeQuery(sql.toString());
						List<Object> results1 = query.getResultList();
						if (results1 != null && !results1.isEmpty()) {
							String s_id = results1.get(0).toString();
							category_id = Integer.parseInt(s_id);
						}
					}
				}
				
				BusinessBrand businessBrand = product.getBusinessBrand();
				if (businessBrand != null) {
					Set<Resource> logAttachments = businessBrand.getLogAttachments();//logo
					Integer brand_resource_id = null;
					for (Iterator<Resource> iter2 = logAttachments.iterator(); iter2.hasNext();) {
						Resource resource = iter2.next();
						sql.delete(0, sql.length());
						sql.append("insert into T_TEST_RESOURCE(URL,origin) values ('");
						sql.append(resource.getUrl());
						sql.append("','");
						sql.append(resource.getOrigin());
						sql.append("')");
						query = entityManager.createNativeQuery(sql.toString());
						query.executeUpdate();
						
						//获取品牌资源id
						sql.delete(0, sql.length());
						sql.append("select last_insert_id()");
						query = entityManager.createNativeQuery(sql.toString());
						List<Object> results1 = query.getResultList();
						if (results1 != null && !results1.isEmpty()) {
							String s_id = results1.get(0).toString();
							brand_resource_id = Integer.parseInt(s_id);
						}
					}
					
					//插入产品品牌
					sql.delete(0, sql.length());
					sql.append("insert into business_brand(name,business_unit_id)");
					sql.append(" values ('");
					sql.append(businessBrand.getName());
					sql.append("',");
					sql.append(business_id);
					sql.append(")");
					query = entityManager.createNativeQuery(sql.toString());
					query.executeUpdate();
					
					//获取品牌id
					sql.delete(0, sql.length());
					sql.append("select last_insert_id()");
					query = entityManager.createNativeQuery(sql.toString());
					List<Object> results1 = query.getResultList();
					if (results1 != null && !results1.isEmpty()) {
						String s_id = results1.get(0).toString();
						brand_id = Integer.parseInt(s_id);
					}
					
					//插入品牌、资源对应表
					sql.delete(0, sql.length());
					sql.append("insert into BRAND_TO_RESOURCE(BRAND_ID,RESOURCE_ID)");
					sql.append(" values (");
					sql.append(brand_id);
					sql.append(",");
					sql.append(brand_resource_id);
					sql.append(")");
					query = entityManager.createNativeQuery(sql.toString());
					query.executeUpdate();
				}
				
				sql.delete(0, sql.length());
				sql.append("insert into product(name,other_Name,status,format,net_content,barcode,note,des,cstm,expiration,");
				sql.append("business_brand_id,producer_id,category_id,provinceID,cityID,areaID) values ('");
				sql.append(product.getName());
				sql.append("',");
				sql.append(product.getOtherName() == null || "".equals(product.getOtherName()) ? null : "'" + product.getOtherName() + "'");
				sql.append(",");
				sql.append(product.getStatus() == null || "".equals(product.getStatus()) ? null : "'" + product.getStatus() + "'");
				sql.append(",");
				sql.append(product.getFormat() == null || "".equals(product.getFormat()) ? null : "'" + product.getFormat() + "'");
				sql.append(",");
				sql.append(product.getNetContent() == null || "".equals(product.getNetContent()) ? null : "'" + product.getNetContent() + "'");
				sql.append(",");
				sql.append(product.getBarcode() == null || "".equals(product.getBarcode()) ? null : "'" + product.getBarcode() + "'");
				sql.append(",");
				sql.append(product.getNote() == null || "".equals(product.getNote()) ? null : "'" + product.getNote() + "'");
				sql.append(",");
				sql.append(product.getDes() == null || "".equals(product.getDes()) ? null : "'" + product.getDes() + "'");
				sql.append(",");
				sql.append(product.getCstm() == null || "".equals(product.getCstm()) ? null : "'" + product.getCstm() + "'");
				sql.append(",");
				sql.append(product.getExpiration() == null || "".equals(product.getExpiration()) ? null : "'" + product.getExpiration() + "'");
				sql.append(",");
				sql.append(brand_id);
				sql.append(",");
				sql.append(business_id);
				sql.append(",");
				sql.append(category_id);
				sql.append(",'','','')");
				query = entityManager.createNativeQuery(sql.toString());
				query.executeUpdate();
				
				//获取产品id
				sql.delete(0, sql.length());
				sql.append("select last_insert_id()");
				query = entityManager.createNativeQuery(sql.toString());
				List<Object> results2 = query.getResultList();
				if (results2 != null && !results2.isEmpty()) {
					String s_id = results2.get(0).toString();
					product_id = Integer.parseInt(s_id);
				}
				
				sql.delete(0, sql.length());
				sql.append("insert into product_instance(batch_serial_no,production_date,product_id,producer_id) values (");
				sql.append(productInstance.getBatchSerialNo() == null || "".equals(productInstance.getBatchSerialNo()) ? null : "'" + productInstance.getBatchSerialNo() + "'");
				sql.append(",");
				sql.append(productInstance.getProductionDate() == null ? null : "'" + productInstance.getProductionDate() + "'");
				sql.append(",");
				sql.append(product_id);
				sql.append(",");
				sql.append(business_id);
				sql.append(")");
				query = entityManager.createNativeQuery(sql.toString());
				query.executeUpdate();
				
				//获取产品实例id
				sql.delete(0, sql.length());
				sql.append("select last_insert_id()");
				query = entityManager.createNativeQuery(sql.toString());
				List<Object> results3 = query.getResultList();
				if (results3 != null && !results3.isEmpty()) {
					String s_id = results3.get(0).toString();
					product_instance_id = Integer.parseInt(s_id);
				}
				product_num++;
			}
			
			if (report_list != null && !report_list.isEmpty()) {
				//产品检测报告
				for (TestResult testResult : report_list) {
					sql.delete(0, sql.length());
					sql.append("insert into test_result(testee_id,sample_id,brand_id,sample_quantity,sampling_location,");
					sql.append("test_place,test_date,standard,result,pass,suppliers_back_count) values (");
					sql.append(business_id);
					sql.append(",");
					sql.append(product_instance_id);
					sql.append(",");
					sql.append(brand_id);
					sql.append(",");
					sql.append(testResult.getSampleQuantity() == null || "".equals(testResult.getSampleQuantity()) ? null : "'" + testResult.getSampleQuantity() + "'");
					sql.append(",");
					sql.append(testResult.getSamplingLocation() == null || "".equals(testResult.getSamplingLocation()) ? null : "'" + testResult.getSamplingLocation() + "'");
					sql.append(",");
					sql.append(testResult.getTestPlace() == null || "".equals(testResult.getTestPlace()) ? null : "'" + testResult.getTestPlace() + "'");
					sql.append(",");
					sql.append(testResult.getSamplingDate() == null || "".equals(testResult.getSamplingDate()) ? null : "'" + testResult.getSamplingDate() + "'");
					sql.append(",");
					sql.append(testResult.getStandard() == null || "".equals(testResult.getStandard()) ? null : "'" + testResult.getStandard() + "'");
					sql.append(",");
					sql.append(testResult.getResult() == null || "".equals(testResult.getResult()) ? null : "'" + testResult.getResult() + "'");
					sql.append(",");
					sql.append(testResult.getPass() == true ? 1 : 0);
					sql.append(",0)");
					query = entityManager.createNativeQuery(sql.toString());
					query.executeUpdate();
					report_num++;
				}
			}
			
			if (trace_list != null && !trace_list.isEmpty()) {
				for (TraceData trace : trace_list) {
					sql.delete(0, sql.length());
					sql.append("insert into trace_data(productID,address,packageSpec,productName,timeTrack) values (");
					sql.append(product_instance_id);
					sql.append(",");
					sql.append(trace.getAddress() == null || "".equals(trace.getAddress()) ? null : "'" + trace.getAddress() + "'");
					sql.append(",");
					sql.append(trace.getPackageSpec() == null || "".equals(trace.getPackageSpec()) ? "''" : "'" + trace.getPackageSpec() + "'");
					sql.append(",");
					sql.append(trace.getProductName() == null || "".equals(trace.getProductName()) ? "''" : "'" + trace.getProductName() + "'");
					sql.append(",");
					sql.append(trace.getTimeTrack() == null || "".equals(trace.getTimeTrack()) ? "''" : "'" + trace.getTimeTrack() + "'");
					sql.append(")");
					query = entityManager.createNativeQuery(sql.toString());
					query.executeUpdate();
					trace_num++;
				}
			}
		}
		result_map.put("product_num", product_num);
		result_map.put("report_num", report_num);
		result_map.put("trace_num", trace_num);
		return result_map;
	}
	
	public void add_western_electronic(Western_electronic western_electronic) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("insert into western_electronic(status,percent,complete_num,company_num,");
		sql.append("product_num,report_num,trace_num,success_num,fail_num,operation_user,operation_time,complete_time) values ('");
		sql.append(western_electronic.getStatus());
		sql.append("','");
		sql.append(western_electronic.getPercent());
		sql.append("',");
		sql.append(western_electronic.getComplete_num());
		sql.append(",");
		sql.append(western_electronic.getCompany_num());
		sql.append(",");
		sql.append(western_electronic.getProduct_num());
		sql.append(",");
		sql.append(western_electronic.getReport_num());
		sql.append(",");
		sql.append(western_electronic.getTrace_num());
		sql.append(",");
		sql.append(western_electronic.getSuccess_num());
		sql.append(",");
		sql.append(western_electronic.getFail_num());
		sql.append(",'");
		sql.append(western_electronic.getOperation_user());
		sql.append("','");
		sql.append(western_electronic.getOperation_time());
		sql.append("',");
		String complete_time = western_electronic.getComplete_time();
		if (complete_time == null || "".equals(complete_time)) {
			sql.append("null");
		} else {
			sql.append("'" + complete_time + "'");
		}
		sql.append(")");
		Query query = entityManager.createNativeQuery(sql.toString());
		query.executeUpdate();
	}
	
	public void update_western_electronic(Western_electronic western_electronic) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("update western_electronic set status = '");
		sql.append(western_electronic.getStatus());
		sql.append("',percent = '");
		sql.append(western_electronic.getPercent());
		sql.append("',complete_num = ");
		sql.append(western_electronic.getComplete_num());
		sql.append(",company_num = ");
		sql.append(western_electronic.getCompany_num());
		sql.append(",product_num = ");
		sql.append(western_electronic.getProduct_num());
		sql.append(",report_num = ");
		sql.append(western_electronic.getReport_num());
		sql.append(",trace_num = ");
		sql.append(western_electronic.getTrace_num());
		sql.append(",success_num = ");
		sql.append(western_electronic.getSuccess_num());
		sql.append(",fail_num = ");
		sql.append(western_electronic.getFail_num());
		String complete_time = western_electronic.getComplete_time();
		if (complete_time != null && !"".equals(complete_time)) {
			sql.append(",complete_time = '");
			sql.append(complete_time);
			sql.append("'");
		}
		sql.append(" where id = ");
		sql.append(western_electronic.getId());
		
		Query query = entityManager.createNativeQuery(sql.toString());
		query.executeUpdate();
	}
}