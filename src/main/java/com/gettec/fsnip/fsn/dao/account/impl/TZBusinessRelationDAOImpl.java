package com.gettec.fsnip.fsn.dao.account.impl;

import com.gettec.fsnip.fsn.dao.account.TZBusinessRelationDAO;
import com.gettec.fsnip.fsn.dao.account.TZStockDAO;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.account.TZBusAccountOut;
import com.gettec.fsnip.fsn.model.account.TZBusinessRelation;
import com.gettec.fsnip.fsn.model.account.TZStock;
import com.gettec.fsnip.fsn.service.account.TZBusAccountOutService;
import com.gettec.fsnip.fsn.util.sales.SalesUtil;
import com.gettec.fsnip.fsn.vo.account.BusRelationVO;
import com.gettec.fsnip.fsn.vo.account.ProductionDateVO;
import com.gettec.fsnip.fsn.vo.account.ReturnProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 台账系统
 * 
 * @author HuangYog
 * @email HuangYong@fsnip.com
 */
@Repository(value = "tzBusinessRelationDAO")
public class TZBusinessRelationDAOImpl extends
        BaseDAOImpl<TZBusinessRelation> implements
        TZBusinessRelationDAO {

    @Autowired TZStockDAO tzStockDAO;
    @Autowired
    TZBusAccountOutService tzBusAccountOutService;
    
    static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    /**
     * 根据企业关系加载对应的企业信息List
     * @param myOrg 本企业id
     * @param type 企业关系 0：供应，1：销售 Create Author HY Date 2015-05-17
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<BusRelationVO> loadTZBusRelation(Long myOrg, int type, String busName, String busLic,int page, int pageSize) throws DaoException {
        String condition = "";
        Map<String, String> param = new HashMap<String, String>();
        param.put("btype",type+"");
        param.put("org",myOrg+"");
        if(busName != null && !"".equals(busName)){
            condition += " AND b.name LIKE :bname ";
            param.put("bname","%" + busName + "%");
        }
        if(busLic != null && !"".equals(busLic)){
            condition += " AND b.license_no LIKE :blic ";
            param.put("blic","%" + busLic + "%");
        }

        String sql = " SELECT b.id,b.name,b.license_no,t.type,b.contact,b.telephone,b.address FROM business_unit b " +
                "LEFT JOIN t_meta_business_diy_type t ON t.business_id = b.id " +
                "WHERE t.type = :btype AND t.organization = :org "+ condition +" GROUP BY b.id ";
        try {
            Query  query = entityManager.createNativeQuery(sql.toString());//.setParameter(1, type).setParameter(2, myOrg);
            setParamtter(param, query);
            if(page > 0) {
                query.setFirstResult((page - 1) * pageSize);
                query.setMaxResults(pageSize);
            }
            List<Object[]> result = query.getResultList();
            return encapsulationResultToBusRelationVO(result);
        } catch (Exception e) {
            throw new DaoException("TZBusinessRelationDAOImpl.loadTZBusRelation() 根据企业关系加载对应的企业信息,出现异常！", e);
        }
    }

    /**
     * 封装供销企业信息
     * @author HY
     * @throws ParseException 
     */
    private List<BusRelationVO> encapsulationResultToBusRelationVO(
            List<Object[]> result) throws ParseException {
        List<BusRelationVO> lists = new ArrayList<BusRelationVO>();
        if (result != null && result.size() > 0) {
            for(int i = 0; i < result.size(); i++) {
                Object[] obj = result.get(i);
                BusRelationVO vo = new BusRelationVO();
                vo.setId(obj[0] != null ? Long.valueOf(obj[0].toString()) : -1);
                vo.setBusName(obj[1] != null ? obj[1].toString() : "");
                vo.setLicNo(obj[2] != null ? obj[2].toString() : "");
                vo.setType(obj[3] != null ? Integer.parseInt(obj[3].toString()) : -1);
                //vo.setCreateDate(obj[4]!=null?formatDate(obj[4].toString()):null);
                vo.setContact(obj[5]!=null?obj[5].toString():"");
                vo.setAddress(obj[6]!=null?obj[6].toString():"");
                lists.add(vo);
            }
        }
        return lists;
    }
    
    /*格式化时间*/
    private Date formatDate(String date) throws ParseException {
        Date time = SDF.parse(date);
        return time;
    }
    
    /**
     * 根据企业关系加载对应的企业信息的总条数
     * @author HY
     */
    @Override
    public Long loadTZBusRelationToatl(Long myOrg, int type, String busName, String busLic)
            throws DaoException {
        String condition = "";
        Map<String, String> param = new HashMap<String, String>();
        param.put("btype", type + "");
        param.put("org", myOrg + "");
        if(busName != null && !"".equals(busName)){
            condition += " AND b.name LIKE :bname ";
            param.put("bname","%" + busName + "%");
        }
        if(busLic != null && !"".equals(busLic)){
            condition += " AND b.license_no LIKE :blic ";
            param.put("blic", "%" + busLic + "%");
        }
        String sql = " SELECT count( DISTINCT b.id) FROM business_unit b " +
                        " LEFT JOIN t_meta_business_diy_type t ON t.business_id = b.id " +
                        " WHERE t.type = :btype AND t.organization = :org " + condition;
        try {

            Query  query = entityManager.createNativeQuery(sql.toString());
            setParamtter(param, query);
            Object rtn = query.getSingleResult();
            return rtn == null ? 0 : Long.parseLong(rtn.toString());
        } catch (Exception jpae) {
            throw new DaoException("TZBusinessRelationDAOImpl.loadTZBusRelation() 根据企业关系加载对应的企业信息的总条数,出现异常！", jpae);
        }
    }

    /**
     * 根据企业关系加载可以退货的产品
     */
    @Override
    public List<ReturnProductVO> loadTZReturnProduct(Long myBusId, String proName, String proBar, int page,
            int pageSize) throws DaoException {
        String condition = "";
        Map<String, String> param = new HashMap<String, String>();
        param.put("busId",myBusId+"");
        if(proName != null && !"".equals(proName)){
            condition += " AND p.name LIKE :pname ";
            param.put("pname","%" + proName + "%");
        }
        if(proBar != null && !"".equals(proBar)){
            condition += " AND p.barcode LIKE :pbar ";
            param.put("pbar","%" + proBar + "%");
        }

        String sql = "SELECT p.id,p.name,p.barcode,p.format,'',tzs.qs_no,tzs.product_num,'',p.expiration_date FROM tz_stock tzs " +
                    " LEFT JOIN product p On p.id = tzs.product_id AND tzs.report_status = 1 WHERE tzs.business_id = :busId " +
                    " AND  p.id IS NOT NULL and tzs.product_num > 0 "+ condition +" GROUP BY p.id,tzs.qs_no ";
        try {
            Query query = entityManager.createNativeQuery(sql.toString());//.setParameter(1, myBusId);
            setParamtter(param, query);
            if(page>0) {
                query.setFirstResult((page-1)*pageSize);
                query.setMaxResults(pageSize);
            }
            List<Object[]> result = query.getResultList();
            return encapsulationResultToReturnProductVO(result, null);
        }catch (Exception e){
            throw new DaoException("TZBusinessRelationDAOImpl.loadTZReturnProduct()根据企业关系加载可以退货的产品,出现异常！",e);
        }
    }

    private void setParamtter(Map<String, String> param, Query query) {
        Iterator keyIt = param.keySet().iterator();
        do{
            String key = (String) keyIt.next();
            String value = param.get(key);
            query.setParameter(key,value);
        } while (keyIt.hasNext());
    }

    /**
     * 统计该企业可以退货的产品品种数量
     */
    @Override
    public Long getTZReturnProductTotal(Long myBusId, String proName, String proBar) throws DaoException {
        String condition = "";
        Map<String, String> param = new HashMap<String, String>();
        param.put("busId",myBusId+"");
        if(proName != null && !"".equals(proName)){
            condition += " AND p.name LIKE :pname ";
            param.put("pname","%" + proName + "%");
        }
        if(proBar != null && !"".equals(proBar)){
            condition += " AND p.barcode LIKE :pbar ";
            param.put("pbar","%" + proBar + "%");
        }

        String sql = "  SELECT COUNT(count) FROM( " +
                        " SELECT COUNT(*) count FROM tz_stock tzs " +
                        " LEFT JOIN product p On p.id = tzs.product_id AND tzs.report_status = 1 " +
                        " WHERE tzs.business_id = :busId AND p.id IS NOT NULL and tzs.product_num > 0 " + condition + " GROUP BY p.id,tzs.qs_no ) temp ";
        try {
            Query query = entityManager.createNativeQuery(sql.toString());
            setParamtter(param, query);
            Object rtn = query.getSingleResult();
            return rtn == null ? 0 : Long.parseLong(rtn.toString());
        } catch (Exception jpae) {
            throw new DaoException("TZBusinessRelationDAOImpl.getTZReturnProductTotal()统计该企业可以退货的产品品种数量，出现异常！",jpae);
        }
    }

    /**
     * 根据企业关系的id加载销往企业
     * @author HY
     */
    @Override
    public BusRelationVO getBusinessRelationById(Long myBusId, Long busId) throws DaoException {
        String sql = "SELECT b.id,b.name,b.license_no,t.type,b.contact,b.telephone,b.address FROM business_unit b " +
                    " LEFT JOIN t_meta_customer_and_provider_type t ON t.organization = b.organization " +
                    " WHERE b.id = ?1 GROUP BY b.id  ";
        try {
            Query  query = entityManager.createNativeQuery(sql.toString()).setParameter(1, myBusId);
            List<Object[]> result = query.getResultList();
            List<BusRelationVO> lists = encapsulationResultToBusRelationVO(result);
            return lists!=null && lists.size()>0 ? lists.get(0):null;
        } catch (Exception e) {
            throw new DaoException("TZBusinessRelationDAOImpl.loadTZBusRelation() 根据企业关系加载对应的企业信息,出现异常！", e);
        }
    }

    /**
     * 根据订单编号加载相应的退货产品
     * @author HY
     */
    @Override
    public List<ReturnProductVO> loadTZReturnProductByOutId(Long outId,Integer page,Integer pageSize) throws DaoException {
        String sql = "SELECT temp.* FROM  (SELECT p.id pid,p.name pname,p.barcode pbarcode,p.format pformat,"+
                " iout.id outId,iout.qs_no,iout.product_num,iout.product_price,p.expiration_date,"+
        		" iout.production_date prodate,iout.product_batch batch,iout.over_date overdate,iout.problem_describe"+
                " FROM tz_business_account_info_out iout LEFT JOIN product p ON p.id = iout.product_id"+
                " WHERE iout.business_account_id = ?1) temp";
        System.out.println("sql : " + sql);
        try {
            Query query = entityManager.createNativeQuery(sql).setParameter(1, outId);
            if(page != null && page > 0){
                query.setFirstResult((page-1)*pageSize);
                query.setMaxResults(pageSize);
            }
            List<Object[]> result = query.getResultList();
            return encapsulationResultToReturnProductVO(result, outId);
        }catch (Exception e){
            throw new DaoException("TZBusinessRelationDAOImpl.loadTZReturnProductByAccountNo()根据订单编号加载可以退货的产品,出现异常！",e);
        }
    }

    @Override
    public Long loadTZReturnProductTotalsById(Long outId) throws DaoException {
        String sql = "select count(*) from tz_business_account_info_out iout LEFT JOIN product p ON p.id = iout.product_id " +
                " where iout.business_account_id = ?1 ";
        try {
            return this.countBySql(sql, new Object[]{outId});
        } catch (JPAException jpae) {
            throw new DaoException(jpae.getMessage(),jpae.getException());
        }
    }

    /**
     * 封装可以退货的产品
     * @author HY
     */
    private List<ReturnProductVO> encapsulationResultToReturnProductVO(
            List<Object[]> result,Long outId) throws Exception {
        List<ReturnProductVO> lists = null;
        if(result!=null&&result.size()>0) {
            lists = new ArrayList<ReturnProductVO>();
            for(int i = 0; i < result.size(); i++) {
                Object[] objs = result.get(i);
                ReturnProductVO vo = new ReturnProductVO();
                vo.setUuid(SalesUtil.createGUID());
                vo.setProductId(objs[0] != null ? Long.valueOf(objs[0].toString()) : -1);
                vo.setName(objs[1] != null ? objs[1].toString() : "");
                vo.setBarcode(objs[2] != null ? objs[2].toString() : "");
                vo.setFormat(objs[3] != null ? objs[3].toString() : "");
                vo.setId(objs[4] != null && !"".equals(objs[4].toString()) ? Long.valueOf(objs[4].toString()) : -1);
                vo.setQsNumber(objs[5] != null ? objs[5].toString() : "");
                vo.setPrice(new BigDecimal(objs[7] != null && !"".equals(objs[7].toString()) ? objs[7].toString() : 0 + ""));
                String expDay = objs[8] != null ? objs[8].toString(): "";
                List<ProductionDateVO> birthDateList = getBirthDateByProductId(vo.getProductId(),expDay);
                vo.setBirthDateList(birthDateList);
                if(outId!=null){
                	vo.setProductionDate(objs[9] != null ? objs[9].toString() : "");
                	vo.setBatch(objs[10] != null ? objs[10].toString() : "");
                	vo.setOverDate(objs[11] != null ? objs[11].toString() : "");
                   // getProductDate(outId,vo);
                    vo.setReturnCount(objs[6] != null ? Long.valueOf(objs[6].toString()) : 0);
                    vo.setProblem_describe(objs[12] != null ? objs[12].toString() : "");
                    TZBusAccountOut accountOut = tzBusAccountOutService.findById(outId);
                    if(accountOut!=null){
                        TZStock tzStock = tzStockDAO.getByProIdAndSelfBusId(vo.getProductId(),accountOut.getOutBusId(),vo.getQsNumber());
                        if(tzStock!=null){
                            vo.setCount(tzStock.getProductNum());
                        }
                    }
                }else{
                    if(birthDateList!=null&&birthDateList.size()>0) {
                        vo.setProductionDate(birthDateList.get(0).getBirthDate());
                        vo.setBatch(birthDateList.get(0).getBatch());
                        vo.setOverDate(birthDateList.get(0).getOverDate());
                    }
                    vo.setCount(objs[6] != null ? Long.valueOf(objs[6].toString()) : 0);
                    vo.setReturnCount(Long.valueOf(0));
                }
                lists.add(vo);
            }
        }
        return lists;
    }

    /* 加载退货信息中的生产日期过期日期批次 */
    private void getProductDate(Long outId,ReturnProductVO vo) {
        String sql = "SELECT iout.production_date,iout.product_batch,iout.over_date FROM tz_business_account_info_out iout " +
                " WHERE iout.business_account_id = ?1 AND iout.product_id = ?2 ";
        Query query = entityManager.createNativeQuery(sql).setParameter(1,outId).setParameter(2,vo.getProductId());
        List<Object[]> result = query.getResultList();
        if (result!=null&&result.size()>0){
            Object[] objs = result.get(0);
            vo.setProductionDate(objs[0] != null ? objs[0].toString().substring(0,10) : "");
            vo.setBatch(objs[1] != null ? objs[1].toString() : "");
            vo.setOverDate(objs[2]!=null?objs[2].toString():"");
        }
    }

    //根据生产日期和保质期天数计算过期日期
    private void calculationOverDate(ProductionDateVO vo, int expDay) throws ParseException {
        /* 判断过期天数 超过五年的以五年计算 */
        expDay = expDay>365*5?365*5:expDay;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(vo.getBirthDate()!=null&&!"".equals(vo.getBirthDate())){
        	Date date = sdf.parse(vo.getBirthDate());
            Calendar cl = Calendar.getInstance();
            cl.setTime(date);
            cl.add(Calendar.DATE, expDay);
            vo.setOverDate(sdf.format(cl.getTime()));
        }else{
        	vo.setOverDate(null);
        }
    }

    /**
     * 根据产品id加载实例的生产日期
     * @author HY
     */
    private List<ProductionDateVO> getBirthDateByProductId(Long productId,String day) throws ParseException {
        String sql = "SELECT pin.id, pin.production_date,pin.batch_serial_no,pin.expiration_date FROM product_instance pin " +
                "WHERE pin.product_id = ?1 ";
        Query query = entityManager.createNativeQuery(sql).setParameter(1,productId);
        List<Object[]> result = query.getResultList();
        List<ProductionDateVO> lists = null;
        if(result!=null&&result.size()>0){
            lists = new ArrayList<ProductionDateVO>();
            for(int i=0;i<result.size();i++){
                Object[] objs=result.get(i);
                ProductionDateVO vo = new ProductionDateVO();
                vo.setInstanceId(objs[0] != null ? objs[0].toString() : "");
                vo.setBirthDate(objs[1] != null ? objs[1].toString().substring(0, 10) : "");
                vo.setBatch(objs[2] != null ? objs[2].toString() : "");
                vo.setOverDate(objs[3] != null ? objs[3].toString().substring(0, 10) : "");
                if(day!=null&&!"".equals(day)){
                    calculationOverDate(vo, Integer.parseInt(day));
                }
                lists.add(vo);
            }
        }
        return lists;
    }

    @Override
    public List<BusRelationVO> getProBusList(Long myOrg, String busName, String busLic, int page, int pageSize) throws DaoException {
        String sql="SELECT b.id,b.name,b.license_no,bt.type,b.contact,b.telephone,b.address FROM t_meta_enterprise_to_customer ec " +
                "INNER JOIN business_unit u ON u.id=ec.business_id " +
                "INNER JOIN t_meta_business_diy_type bt ON bt.organization=u.organization AND bt.business_id=ec.customer_id " +
                "INNER JOIN business_unit b ON b.id=ec.customer_id " +
                "WHERE u.organization=?1 AND bt.type=2 AND b.type LIKE '%生产企业%' ";
       /* String sql="SELECT b.id,b.name,b.license_no,t.type,b.contact,b.telephone,b.address FROM t_meta_business_diy_type t " +
                " LEFT JOIN business_unit b ON b.id=t.business_id " +
                " WHERE t.organization=?1 AND t.type=2 AND b.type LIKE '%生产企业%' " ;*/
        if(busName != null && !"".equals(busName)){
            sql+=" AND b.name LIKE '%"+busName+"%' " ;
        }
        if(busLic != null && !"".equals(busLic)){
            sql+=" AND b.license_no LIKE '%"+busLic+"%' " ;
        }
        try {
            Query  query = entityManager.createNativeQuery(sql.toString());
            query.setParameter(1,myOrg);
            if(page > 0) {
                query.setFirstResult((page - 1) * pageSize);
                query.setMaxResults(pageSize);
            }
            List<Object[]> result = query.getResultList();
            return encapsulationResultToBusRelationVO(result);
        } catch (Exception e) {
            throw new DaoException("TZBusinessRelationDAOImpl.loadTZBusRelation() 根据企业关系加载对应的企业信息,出现异常！", e);
        }
    }

    @Override
    public Long getProBusToatl(Long myOrg, String busName, String busLic) throws DaoException {
        String sql="SELECT count(*) FROM t_meta_enterprise_to_customer ec " +
                "INNER JOIN business_unit u ON u.id=ec.business_id " +
                "INNER JOIN t_meta_business_diy_type bt ON bt.organization=u.organization AND bt.business_id=ec.customer_id " +
                "INNER JOIN business_unit b ON b.id=ec.customer_id " +
                "WHERE u.organization=?1 AND bt.type=2 AND b.type LIKE '%生产企业%' ";
        if(busName != null && !"".equals(busName)){
            sql+=" AND b.name LIKE '%"+busName+"%' " ;
        }
        if(busLic != null && !"".equals(busLic)){
            sql+=" AND b.license_no LIKE '%"+busLic+"%' " ;
        }
        try {
            Query  query = entityManager.createNativeQuery(sql.toString());
            query.setParameter(1,myOrg);
            Object rtn = query.getSingleResult();
            return rtn == null ? 0 : Long.parseLong(rtn.toString());
        } catch (Exception jpae) {
            throw new DaoException("TZBusinessRelationDAOImpl.loadTZBusRelation() 根据企业关系加载对应的企业信息的总条数,出现异常！", jpae);
        }
    }


}
