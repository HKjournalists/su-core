package com.gettec.fsnip.fsn.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.lhfs.fsn.util.HTTPUtil;

/**
 * 系统常用工具类
 * @author wuchangqiang
 *
 */
public class FsnUtil {
	//static Properties props = new Properties();
	public FsnUtil(){
		
	}
	public static String REQUEST_ENCODING= "UTF-8";
	public static String LIMS_SMS_SIGN="【食安科技】";//短信签名
	
	/**
	 * 判断对象是否为空  wuchangqiang add 20140926
	 * @param o 实例对象则是判断是否为null 2，字符除了判断null，还判断字符长度，如果长度是空，则算空
	 * @return
	 */
	public static boolean isNull(Object o) {
		if (o == null) {
			return true;
		} else {
			if (o instanceof String) {
				return o.toString().trim().length() == 0 ? true : false;
			} else {
				return false;
			}
		}
	}
	/**
	 * 判断日期是否符合标准
	 * @param josDate
	 * @return
	 * @throws Exception
	 */
	public static boolean isDate(String josDate) throws Exception{
		try{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			dateFormat.parse(josDate);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	/**
	 * MD5加密  wuchangqiang
	 * @param _str
	 * @return
	 */
	public static String md5_32(String _str) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = _str.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * 日期转换  String转 date wuchangqiang
	 * @param sDate
	 * @return
	 * @throws Exception
	 */
	 public static Date formatDate(String sDate) throws Exception {
		 try{
			 int jj;
		      char ss, cc;
		      String[] sss = {
		            "-", "/", "."};
		      String[] result;
		      int kk, mm;
		      final String emsg = "非法日期格式！";

		      GregorianCalendar cl = null;
		      //检查分隔符
		      for (jj = 0; jj < sss.length; jj++) {
		         if (sDate.indexOf(sss[jj]) >= 0)
		            break;
		      }
		      if (jj >= sss.length)
		         throw new Exception(emsg);

		      ss = sss[jj].charAt(0);
		      //检查数字有效性即除了数字和分隔符，不应该再包括其它字符
		      for (int i = 0; i < sDate.length(); i++) {
		         cc = sDate.charAt(i);
		         if (cc != ss && (cc < '0' || cc > '9'))
		            throw new Exception(emsg);
		      }

		      //劈开，获取3个数字
		      result = sDate.split(sss[jj], -1); //检查全部，包括空的元素，用0会忽略空
		      if (result.length != 3)
		         throw new Exception(emsg);
		      jj = Integer.parseInt(result[0]);
		      kk = Integer.parseInt(result[1]);
		      mm = Integer.parseInt(result[2]);

		      //判断是否符合一种日期格式
		      //1、y/M/d格式
		      if (isValidDates(jj, kk, mm))
		         cl = new GregorianCalendar(jj < 30 ? jj + 2000 :
		                                    (jj <= 99 ? jj + 1900 : jj), kk - 1, mm);
		      else {
		         if (mm < 30)
		            mm += 2000;
		         else if (mm <= 99)
		            mm += 1900;
		            //2、M/d/y格式
		         if (isValidDates(mm, jj, kk))
		            cl = new GregorianCalendar(mm, jj - 1, kk);
		            //3、d/M/y格式
		         else if (isValidDates(mm, kk, jj))
		            cl = new GregorianCalendar(mm, kk - 1, jj);
		         else
		            throw new Exception(emsg);
		      }
		      return cl.getTime(); 
		 }catch(Exception e){
			 throw new Exception(e.getMessage());
		 }
	      
	   }
	 /**
	  * 日期格式验证
	  * @param year
	  * @param month
	  * @param day
	  * @return
	  */
	 public static boolean isValidDates(int year, int month, int day) {
	      GregorianCalendar cl;

	      if (year < 0 || (year > 99 && (year < 1000 || year > 9999)))
	         return false;
	      if (year < 30)
	         year += 2000;
	      else if (year <= 99)
	         year += 1900;

	      if (month < 1 || month > 12)
	         return false;

	      cl = new GregorianCalendar(year, month - 1, 1); //参数月份从0开始所以减一
	      if (day < cl.getActualMinimum(Calendar.DAY_OF_MONTH) ||
	          day > cl.getActualMaximum(Calendar.DAY_OF_MONTH))
	         return false;

	      return true;
	   }
	 /**
	  * 日期转换
	  * @param dDate
	  * @param format
	  * @return String：返回格式化好的日期字符串
	  */
	 public static String formatDate(Date dDate, String format) {
		return (new SimpleDateFormat(format)).format(dDate);
	 }

	 /**
	  * 日期格式化，采用日期字符串做源日期参数	
	  * @param sDate
	  * @param format
	  * @return 日期格式化，采用日期字符串做源日期参数
	  * @throws ParseException
	  */
	 public static String formatDate(String sDate, String format) throws ParseException {
		return formatDate(DateFormat.getDateInstance().parse(sDate), format);
	 }
	 /**
	  * 日期相加
	  * @param dDate
	  * @param days
	  * @return
	  */
	 public static final Date addDay(Date dDate, int days) {
		return addDate(dDate, days, Calendar.DAY_OF_MONTH);
	 }
	 /**
	  * 日期处理
	  * @param dDate
	  * @param amount
	  * @param field
	  * @return
	  */
	 public static Date addDate(Date dDate, int amount, int field) {
		GregorianCalendar cl = new GregorianCalendar();
		cl.setTime(dDate);
		cl.add(field, amount);
		return cl.getTime();
	 }
	 /**
	  * 如a,b,c自动转换成'a','b','c'
	  * @param 
	  * @return
	  */
	 public static String splitToSQLStr(Object o) {
			StringBuffer strb = new StringBuffer();
			if (o instanceof List) {
				List<?> list = (List<?>) o;
				if (list.size() > 0) {
					for (int i = 0, l = list.size(); i < l; i++) {
						strb.append("'").append(list.get(i).toString()).append("'")
								.append(",");
					}

					if (strb.length() > 0) {
						strb.deleteCharAt(strb.length() - 1);
					}
					return strb.toString();
				}
			} else if (o instanceof String[]) {
				String[] arrStr = (String[]) o;
				if (arrStr.length > 0) {
					for (int i = 0, l = arrStr.length; i < l; i++) {
						strb.append("'").append(arrStr[i]).append("'").append(",");
					}

					if (strb.length() > 0) {
						strb.deleteCharAt(strb.length() - 1);
					}
					return strb.toString();
				}
			} else if (o instanceof String) {
				String[] arrStr = o.toString().split(",");
				if (arrStr.length > 0) {
					for (int i = 0, l = arrStr.length; i < l; i++) {
						strb.append("'").append(arrStr[i]).append("'").append(",");
					}

					if (strb.length() > 0) {
						strb.deleteCharAt(strb.length() - 1);
					}
					return strb.toString();
				}
			}
			return "''";
		}

	public static String LIMS_SMS_SN = "SDK-WSS-010-07501";//序列号
	public static String LIMS_SMS_PWD = "^3^d1-1^";//密码

	/**
	 * 获取配置文件信息 wuchangqing 20141013
	 * @param properties 获取参数
	 * @return
	 */
	public static String getProperties(String properties)throws Exception{
		try{
			String resultPerties=null;
			Properties p = new Properties();
			p.load(FsnUtil.class.getClassLoader().getResourceAsStream("JOS_lims.properties"));
			resultPerties=String.valueOf(p.getProperty(properties));
			return resultPerties;
		}catch(Exception e){
			throw new Exception("获取配置文件失败:"+e.getMessage());
		}
	}
	/**
	 * 两种日期类型转换 java.sql.date本身就是java.util.date不用转换
	 * java.util.date到java.sql.date的转换用这个函数实现
	 */
	public static java.sql.Date toSqlDate(Date dDate) {
		return new java.sql.Date(dDate.getTime());
	}
	/**
	 * 在sql查询的时候，使用in条件查询的时候是用限制的当in的条件字段大于1000时就出问题
	 * 所以需要对in的条件做一下处理
	 * @param str sql条件中in的条件值
	 * @param file 需要进行in的sql表的字段
	 * @return 
	 */
	public static String getInSql (String str,String file){
		String whereSql = "  1=1 ";
		String[] insqlValue = str.split(",");
		StringBuffer buff = new StringBuffer();
		int key =1;
		int yus = insqlValue.length%1000;
		for(int i=1; i<=insqlValue.length; i++){
			if(insqlValue[i-1]!=null&&insqlValue[i-1].toString().trim().length()>0){
				buff.append(insqlValue[i-1].toString() ).append(",");
			}
			if(key==1000){
				buff.setLength(buff.length()-1);
				buff.append("\t");
				key =1;
			}else if(i+yus == insqlValue.length){
				buff.setLength(buff.length()-1);
				buff.append("\t");
				key =1;
			}
			key++;
		}
		if(buff.length()>1){
			buff.setLength(buff.length()-1);
		}else{
			buff.append("''");
		}
		String[] rebuff = buff.toString().split("\t");
		whereSql += " and (";
		for(int j = 0; j<rebuff.length; j ++ ){
			if(j==rebuff.length-1){
				whereSql += " "+file+"  in ("+rebuff[j].toString()+")  ";
			}else{
				whereSql +=" "+file+" in ("+rebuff[j].toString()+") or ";
			}
			
		}
		whereSql += " ) ";
		return whereSql ;
	}
	/**
	 * 判断是否在正常年、月、日属于正常范围 wcq 20141021
	 * @param year  年	
	 * @param month 月
	 * @param day 日
	 * @return  
	 */
	public static boolean isValidDate(int year, int month, int day) {
		GregorianCalendar cl;

		if (year < 0 || (year > 99 && (year < 1000 || year > 9999)))
			return false;
		if (year < 30)
			year += 2000;
		else if (year <= 99)
			year += 1900;

		if (month < 1 || month > 12)
			return false;

		cl = new GregorianCalendar(year, month - 1, 1); // 参数月份从0开始所以减一
		if (day < cl.getActualMinimum(Calendar.DAY_OF_MONTH)
				|| day > cl.getActualMaximum(Calendar.DAY_OF_MONTH))
			return false;

		return true;
	}
	/**
	 * 判断是否月末 wcq
	 * @param dt
	 * @return
	 */
	public static boolean isEndMonth(Date dt) {
		return !(getMonth(addDay(dt, 1)) == getMonth(dt));
	}
	/**
	 * 获取年份wcq
	 * @param dDate
	 * @return
	 */
	public static int getYear(Date dDate) {
		return getDateItems(dDate, Calendar.YEAR);
	}
	/**
	 * 获取月份 wcq
	 * @param dDate
	 * @return
	 */
	public static int getMonth(Date dDate) {
		return getDateItems(dDate, Calendar.MONTH) + 1;
	}
	/**
	 * 获取日 wcq
	 * @param dDate
	 * @return
	 */
	public static int getDay(Date dDate) {
		return getDateItems(dDate, Calendar.DAY_OF_MONTH);
	}
	/**
	 * 获取小时 wcq
	 * @param dDate
	 * @return
	 */
	public static int getHour(Date dDate) {
		return getDateItems(dDate, Calendar.HOUR_OF_DAY);
	}
	/**
	 * 获取分wcq
	 * @param dDate
	 * @return
	 */
	public static int getMinute(Date dDate) {
		return getDateItems(dDate, Calendar.MINUTE);
	}
	/**
	 * 获取秒wcq
	 * @param dDate
	 * @return
	 */
	public static int getSecond(Date dDate) {
		return getDateItems(dDate, Calendar.SECOND);
	}
	/**
	 * 返回日期元素 wcq
	 * @param dDate
	 * @param field
	 * @return
	 */
	public static final int getDateItems(Date dDate, int field) {
		GregorianCalendar cl = new GregorianCalendar();
		cl.setTime(dDate);
		return cl.get(field);
	}
	/**
	 * 获取星期wcq
	 * @param dDate
	 * @return
	 */
	public static int getWeekDay(Date dDate) {
		return getDateItems(dDate, Calendar.DAY_OF_WEEK);
	}
	/**
	 * 得到某个月有多少周wcq
	 * @param dDate
	 * @return
	 */
	public static int getWeek(Date dDate) {
		int days = getDay(dDate);
		if (days % 7 == 0) {
			return days / 7;
		} else {
			return days / 7 + 1;
		}
	}
	/**
	 * 相加月份wcq
	 * @param dDate
	 * @param months
	 * @return
	 */
	public static final Date addMonth(Date dDate, int months) {
		return addDate(dDate, months, Calendar.MONTH);
	}
	/**
	 * 相加年份 wcq
	 * @param dDate
	 * @param years
	 * @return
	 */
	public static final Date addYear(Date dDate, int years) {
		return addDate(dDate, years, Calendar.YEAR);
	}
	/**
	 * 
	 * @param dblMoney wcq
	 * @param blnFull boolean： =False,2001.30->贰仟零壹元叁角整 =True,2001.30->贰仟零佰零拾壹元叁角零分
	 * @param bZheng boolean：是否强制末尾加整字（如果不是到分，那么一定有整）
	 * @return
	 */
	public static final String chineseAmount(double dblMoney, boolean blnFull,
			boolean bZheng) {

		String conChineseNum = "零壹贰叁肆伍陆柒捌玖";
		String conChineseMUnit = "分角元拾佰仟万拾佰仟亿";
		String strMoney = null;
		String T1 = "";
		char T2, t0 = ' ';
		int ii, jj, kk;
		boolean noZero = false;
		strMoney = new DecimalFormat("0.00").format(FsnUtil.round(dblMoney, 2,
				true));// 去掉前导0
		kk = 0;
		if (blnFull) {
			for (ii = strMoney.length() - 1; ii >= 0; ii--) {
				T2 = strMoney.charAt(ii);
				if (T2 >= '0' && T2 <= '9') {
					T1 = String.valueOf(conChineseNum.charAt(T2 - '0'))
							+ String.valueOf(conChineseMUnit.charAt(kk++)) + T1;
					if (kk > 10)
						kk -= 8;
				}
			}
		} else {
			for (ii = strMoney.length() - 1; ii >= 0; ii--) {
				T2 = strMoney.charAt(ii);
				if (T2 == '0' && !noZero) {
					if (kk == 2 || kk == 6)
						T1 = String.valueOf(conChineseMUnit.charAt(kk)) + T1;
					else if (kk == 10)
						T1 = String.valueOf(conChineseMUnit.charAt(kk))
								+ (T1.charAt(0) == '万' ? T1.substring(1) : T1);

					if (++kk > 10)
						kk -= 8;
				} else if (T2 == '.' && !noZero) {

				} else {
					noZero = true;
					if (T2 >= '0' && T2 <= '9') {
						if (T2 == '0') {
							if (T2 != t0) {
								t0 = T2;
								jj = T2 - '0';
								if (kk == 2 || kk == 6 || kk == 10)
									T1 = String.valueOf(conChineseMUnit
											.charAt(kk))
											+ T1;
								else
									T1 = String.valueOf(conChineseNum
											.charAt(jj))
											+ T1;
							} else {
								if (kk == 2 || kk == 6)
									T1 = String.valueOf(conChineseMUnit
											.charAt(kk))
											+ T1;
								else if (kk == 10)
									T1 = String.valueOf(conChineseMUnit
											.charAt(kk))
											+ (T1.charAt(0) == '万' ? T1
													.substring(1) : T1);

							}
						} else {
							t0 = T2;
							jj = T2 - '0';
							if (kk == 10 && T1.charAt(0) == '万')
								T1 = T1.substring(1);
							T1 = String.valueOf(conChineseNum.charAt(jj))
									+ String
											.valueOf(conChineseMUnit.charAt(kk))
									+ T1;
						}
						if (++kk > 10)
							kk -= 8;
					}
				}
			}
			T2 = T1.charAt(0);
			while (conChineseMUnit.indexOf(T2) >= 0 || T2 == '零') {
				T1 = T1.substring(1);
				if (T1.length() > 1)
					T2 = T1.charAt(0);
				else
					break;
			}
			if (T1.length() == 0)
				T1 = "零元";
		}

		return T1
				.concat(bZheng || T1.charAt(T1.length() - 1) != '分' ? "整" : "");
	}
	/**
	 * 小数四舍五入处理
	 * @param v
	 * @param lDecs
	 * @param bTrunc
	 * @return
	 */
	public static final double round(double v,int lDecs,boolean bTrunc){
        if(lDecs < 0)
            return v;
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one,lDecs ,bTrunc ? BigDecimal.ROUND_DOWN : BigDecimal.ROUND_HALF_UP).doubleValue();
    }
	/**
	 * 字符串截取容错处理
	 * @param sSrc
	 * @param iLen
	 * @return
	 */
	public static final String left(String sSrc, int iLen) {
		if (iLen >= sSrc.length())
			return sSrc;
		return sSrc.substring(0, iLen);
	}
	/**
	 * 去掉字符串首尾空格
	 * @param sSrc
	 * @return
	 */
	public static final String trim(String sSrc) {
		int i, j;
		// 去除尾部空格
		for (i = sSrc.length() - 1; i >= 0; i--)
			if (sSrc.charAt(i) != ' ')
				break;
		if (i < 0)
			return "";
		// 去除开头空格
		for (j = 0; j < i; j++) {
			if (sSrc.charAt(j) != ' ')
				break;
		}
		return sSrc.substring(j, i + 1);// 返回从j到i的字符
	}
	 /**
	   * 生成txt文件 wcq 
	   * @param fileContext 文件内容
	   * @param filePath  文件路径
	   * @param fileName  路径+文件名
	   * @throws Exception
	   */
	public static void getFileText(String fileContext, String filePath, String fileName) throws Exception {
		BufferedWriter bw = null;
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				file.mkdirs();
			}
			bw = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(fileName)));
			bw.write(fileContext);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			if (bw != null) {
				try {
					bw.flush();
					bw.close();
				} catch (IOException e) {
					throw new Exception(e.getMessage());
				}
			}
		}
	}
	/**
	 * 生成编号 wcq
	 * @return
	 * @throws Exception
	 */
	public static String getSerialNumber()throws Exception{
		try{
			return  new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())
				+String.valueOf((int)(Math.random()*900)+100);			
		}catch(Exception e){
			throw new Exception("在生成编号出错."+e.getMessage());
		}
	}
	/**
	 * 循环生成编号 wcq
	 * @param i
	 * @return
	 * @throws Exception
	 */
	public static String getSerialNumber(int i)throws Exception{
		try{
			return  new SimpleDateFormat("MMddHHmmssSSS").format(new Date())+String.valueOf(Math.abs((int)new Date().getTime()/(i+1)%10000000));
		}catch(Exception e){
			throw new Exception("在产生编号的时候出错了."+e.getMessage());
		}
	}
	public static String getStatementProperties(String properties)throws Exception{
		try{
			String resultPerties=null;
			Properties p = new Properties();
			p.load(FsnUtil.class.getClassLoader().getResourceAsStream("JOS_pdf_contents.properties"));
			resultPerties=String.valueOf(p.getProperty(properties));
			return resultPerties;
		}catch(Exception e){
			throw new Exception("获取配置文件失败:"+e.getMessage());
		}
	}
	/**
	 * 日期路径转换 wcq
	 * @param path  路径
	 * @param date  日期
	 * @return
	 * @throws Exception
	 */
	public static String getPathUtil(String path,String date)throws Exception{
		try{
			String filePath=null;
			if(FsnUtil.isNull(path))throw new Exception("系统提示:未获取到路径信息,请确认是否配置!");
			Date datePath=null;
			if(path.indexOf("日期_")!=-1){
				if(FsnUtil.isNull(date))throw new Exception("系统提示:未获取到日期信息,请确认后再操作!");
				datePath=FsnUtil.formatDate(date);
				String[] fileRules ;
				if(path.indexOf("/")!=-1){
					fileRules = path.split("/");
				}else{
					fileRules = path.split("\\[");
				}
				String newRoules=path;
				for(int i=0;i<fileRules.length;i++){
					String oldStr= fileRules[i];
					if(oldStr.indexOf("日期_")!=-1){
						if(oldStr.indexOf("#")>0){
							oldStr = oldStr.split("#")[0];
						}
						if(oldStr.indexOf("\\")>0){
							oldStr = oldStr.substring(0, oldStr.indexOf("\\"));
						}
						oldStr= (oldStr.replaceAll("\\[", "")).replaceAll("\\]", "").trim();
						String format=oldStr.split("_")[1];
						String newStr=FsnUtil.formatDate(datePath, format);
						newRoules=newRoules.replaceAll("\\["+oldStr+"\\]", newStr);
						filePath=newRoules;
					}
				}
				
			}else{
				filePath=path;
			}
			return filePath;
		}catch(Exception e){
			throw new Exception("路径转换出错:"+e.getMessage());
		}
		
	}
	/**
	 * 输入流转换成String wcq
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static String httpRequestString(HttpServletRequest request) throws Exception {
		try{
			InputStream in = request.getInputStream();
			StringBuffer out = new StringBuffer();
			InputStreamReader inread = new InputStreamReader(in,REQUEST_ENCODING);
			char[] b = new char[4096];
			for (int n; (n = inread.read(b)) != -1;) {
				out.append(new String(b, 0, n));
			}
			return out.toString();
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}

	}
	/**
	 * 拼接返回错误json字符串 wcq
	 * @param method 方法名称
	 * @param errorMsg 错误内容
	 * @param result 返回结果
	 * @return
	 * @throws Exception
	 */
	public static String responseJson(String method,String errorMsg,String result)throws Exception{
		StringBuffer strJson=new StringBuffer();
		strJson.append("{");
		strJson.append("\""+method+"\":{");
		strJson.append("\"code\":\"0\",\""+result+"\":{\"r_msg\":\""+errorMsg+"\",\"success\":false}}}");
		return strJson.toString();
	}
	/**
	 * 日期转换wcq
	 * @param sDate
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static Date parseDate(String sDate, String format)throws Exception {
		GregorianCalendar cl = new GregorianCalendar();
		final String DERR = "解析日期出错！";
		int year;
		try {
			cl.setTime((new SimpleDateFormat(format)).parse(sDate));
			year = cl.get(Calendar.YEAR);
			if (year < 1000 || year > 9999)
				throw new Exception(DERR + "\n\t年份必须在1000－9999之间");

			return cl.getTime();
		} catch (ParseException pe) {
			throw new Exception(DERR, pe);
		}
	}
	
	/**
	 * 两个数据相减
	 * @param dDate1
	 * @param dDate2
	 * @return
	 */
	public static int dateDiff(java.util.Date dDate1, java.util.Date dDate2) {
		int year = 0;
		int month = 0;
		int day = 0;
		GregorianCalendar cl1 = new GregorianCalendar();
		GregorianCalendar cl2 = null;
		cl1.setTime(dDate2);
		year = cl1.get(Calendar.YEAR);
		month = cl1.get(Calendar.MONTH);
		day = cl1.get(Calendar.DAY_OF_MONTH);
		cl2 = new GregorianCalendar(year, month, day);
		cl1.setTime(dDate1);
		year = cl1.get(Calendar.YEAR);
		month = cl1.get(Calendar.MONTH);
		day = cl1.get(Calendar.DAY_OF_MONTH);
		cl1.clear();
		cl1.set(year, month, day);
		return (int) FsnD.round(FsnD.sub(cl2.getTimeInMillis(), cl1.getTimeInMillis())/ (1000 * 3600 * 24), 0);
	}
	/**
	 * 加小时
	 * @param dDate
	 * @param hours
	 * @return
	 */
	public static Date addHour(Date dDate, int hours) {
		return addDate(dDate, hours, Calendar.HOUR_OF_DAY);
	}
	/**
	 * 加分钟
	 * @param dDate
	 * @param minutes
	 * @return
	 */
	public static Date addMinute(Date dDate, int minutes) {
		return addDate(dDate, minutes, Calendar.MINUTE);
	}
	/**
	 * 加秒
	 * @param dDate
	 * @param second
	 * @return
	 */
	public static Date addSecond(Date dDate, int second) {
		return addDate(dDate, second, Calendar.SECOND);
	}
	
	/**
	 * 短信模板
	 * @param userName 
	 * @param serviceName
	 * @param subId
	 * @param flag
	 * @return
	 * @throws Exception
	 */
	public static String getSendMessage(String userName, String password, String enterpriseName)throws Exception{
		try{
			StringBuffer str=new StringBuffer();
			str.append(userName + "您好：");
			str.append("欢迎贵公司[" + enterpriseName + "]注册成为食品安全与营养云平台的企业级用户。\r\n");
			str.append("您的账户是：" + userName + "\r\n,");
			str.append("初始密码为：" + password + "\r\n,");
			str.append("登录地址为：" + HTTPUtil.getReqServerName(null) + "\r\n。");
			str.append("感谢您的关注和支持！\r\n");
			return str.toString();
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
	}

	/**
	 *  判断字符串是否是整数
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){ 
	    Pattern pattern = Pattern.compile("[0-9]*"); 
	    return pattern.matcher(str).matches();    
	 }
	/**
	 * 获取自检报告返回值wcq
	 * @param key
	 * @return
	 */
	public static String getJosResult(String key){
		Map<String, String> map=new HashMap<String, String>();
		map.put("jingdong.market.service.qt.report.update", "publicResult");
		map.put("jingdong.market.service.qt.report.add", "publicResult");
		map.put("jingdong.market.service.qt.report.get", "publicResult");
		map.put("jingdong.market.service.property.get", "serviceItemResult");
		map.put("jingdong.market.service.qt.subscribe.get", "qtArticleResult");
		map.put("jingdong.market.service.qt.report.list.get", "publicResult");
		map.put("jingdong.market.service.qt.report.delete", "publicResult");
		if(map.get(key)==null){
			return key;
		}
		return (String)map.get(key);
	}
	/**
	 * 短信失败码 wcq
	 * @param key
	 * @return
	 */
	public static String getSmsError(String key){
		Map<String, String> map=new HashMap<String, String>();
		map.put("-2", "帐号/密码不正确");
		map.put("-4", "余额不足支持本次发送");
		map.put("-5", "数据格式错误");
		map.put("-6", "参数有误");
		map.put("-7", "权限受限");
		map.put("-8", "流量控制错误");
		map.put("-9", "扩展码权限错误");
		map.put("-10", "内容长度长");
		map.put("-11", "内部数据库错误");
		map.put("-12", "序列号状态错误");
		map.put("-14", "服务器写文件失败");
		map.put("-17", "没有权限");
		map.put("-19", "禁止同时使用多个接口地址");
		map.put("-20", "相同手机号，相同内容重复提交");
		map.put("-22", "Ip鉴权失败");
		map.put("-23", "缓存无此序列号信息");
		map.put("-601", "序列号为空，参数错误");
		map.put("-602", "序列号格式错误，参数错误");
		map.put("-603", "密码为空，参数错误");
		map.put("-604", "手机号码为空，参数错误");
		map.put("-605", "内容为空，参数错误");
		map.put("-606", "ext长度大于9，参数错误");
		map.put("-607", "参数错误 扩展码非数字");
		map.put("-608", "参数错误 定时时间非日期格式");
		map.put("-609", "rrid长度大于18,参数错误");
		map.put("-610", "参数错误 rrid非数字");
		map.put("-611", "参数错误 内容编码不符合规范");
		map.put("-623", "手机个数与内容个数不匹配");
		map.put("-624", "扩展个数与手机个数数");
		if(map.get(key)==null){
			return key;
		}
		return (String)map.get(key);
	}
	
}
