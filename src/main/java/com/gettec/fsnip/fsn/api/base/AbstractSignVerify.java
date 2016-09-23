package com.gettec.fsnip.fsn.api.base;

import static com.lhfs.fsn.util.APIMaxVisitConfig.API_MAX_VIS_MAP;

import com.gettec.fsnip.fsn.service.receive.ReceiveBusKeyConfigService;
import com.gettec.fsnip.fsn.sign.AES;
import com.gettec.fsnip.fsn.sign.MD5;
import com.gettec.fsnip.fsn.sign.SignConfig;

/**
 * 泛型 T 为方法 doPassHandle 和 verifyAndDoPassHandle 的返回类型，若不需要返回类型，请指定T为String并返回null
 * @author TangXin 2015/6/24
 * 
 */
public abstract class AbstractSignVerify<T> {
   
	private String data; //待处理的数据项
	private String sign; //签名结果
	private String appkey; //企业唯一编号，查找签名的配置信息
	/**
	 * 枷锁对象
	 */
	private Object lockObj = new Object();
	/**
	 * 接口请求的最大并发量，默认为空，子类可以覆盖并赋值
	 * 如果该变量的值为null，则不做接口过载保护处理，
	 * 如果该变量的值不null，并且值大于0，则需要验证接口并发访问量不能超过该值
	 */
	private Integer maxVisitNumber = null;
	
	public Integer getMaxVisitNumber() {
		return maxVisitNumber;
	}

	public void setMaxVisitNumber(Integer maxVisitNumber) {
		this.maxVisitNumber = maxVisitNumber;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	/**
	 * 判断当前接口的并发请求是否过载
	 * 该方法需要抛出一个 Exception 异常，用来传递并发过载时的提示信息，最终将会被输出到客户端
	 * @throws Exception
	 */
	private void judgeOverLoad(String visitKey, Integer maxVisitNumber) throws Exception{
		if(maxVisitNumber == null || maxVisitNumber < 1) {
			   return;
		}
		synchronized (lockObj) {
			Integer vcont = API_MAX_VIS_MAP.get(visitKey);
			if(vcont == null) {
				vcont = 0;
				API_MAX_VIS_MAP.put(visitKey, 0);
			}
			if(vcont >= maxVisitNumber) {
				throw new Exception("接口访问太过频繁，当前的并发数量已经超过50，拒绝访问。");
			}
		}	
	}
	
	/**
	 * 一个新的请求,同步执行并发数量 atgooCurrentVisitCont 加 1
	 * @throws Exception
	 */
	private void newRequest(String visitKey, Integer maxVisitNumber) throws Exception{
		if(maxVisitNumber == null || maxVisitNumber < 1) {
			   return;
		}
		try{
			/**
			 * 对当前并发量 aipVisitCountMap.get(visitKey) 添加同步锁
			 * aipVisitCountMap.get(visitKey) Integer类型的静态变量，记录接口的并发访问量
			 */
			synchronized (lockObj) {
				Integer vcont = API_MAX_VIS_MAP.get(visitKey);
				if(vcont == null) {
					vcont = 0;
				}
				API_MAX_VIS_MAP.put(visitKey, vcont + 1);
			}
		}catch(Exception e){
			throw e;
		}
	}
	
	/**
	 * 销毁一个请求，同步执行并发数量  aipVisitCountMap.get(visitKey) 减 1 操作
	 */
	private void destroyRequest(String visitKey, Integer maxVisitNumber){
		try{
			
		}finally{
			if(maxVisitNumber == null || maxVisitNumber < 1){
				return;
			}
			/**
			 * 对当前并发量添加同步锁
			 * aipVisitCountMap.get(visitKey) 变量，记录接口的并发访问量
			 */
			synchronized (lockObj) {
				Integer vcont = API_MAX_VIS_MAP.get(visitKey);
				if(vcont == null) {
					vcont = 1;
				}
				if(vcont > 0){
					API_MAX_VIS_MAP.put(visitKey, vcont - 1);
				}
			}
		}
	};
	
	/**
	 * 得到 ReceiveBusKeyConfigService，用来查询签名的校验信息,由实现类提供具体的方法
	 * @return
	 */
   protected abstract ReceiveBusKeyConfigService getReceiveBusKeyConfigService();
   
   /**
    * 签名验证通过后需要执行的具体操作,由实现类提供具体的业务逻辑
    * @param T 方法的返回类型，若无返回类型，请指定T为String并返回null
    * @param data 解密后的数据
    */
   protected abstract T doPassHandle(String data) throws Exception;
   
   /**
    * 获取签名的字段（不包含SECERT_KEY） ,默认为 data + appkey,如果签名字段不同，请覆盖此方法
    * 使用多个参数拼接时，请确保拼接的顺序跟签名使用的顺序一致
    * @return
    */
   protected String getSignField() {
	   return (this.data + this.appkey);
   }
   
   /**
    * receiveBusKeyConfigService 空指针异常
    * @param e
    * @throws NullPointerException
    */
   protected void throwReceiveBusKeyConfigServiceNullException(Exception e) throws NullPointerException{
	   throw new NullPointerException(e.getMessage());
   }
   
   /**
    * 没有找到签名配置信息异常
    * @param e
    * @throws ClassNotFoundException
    */
   protected void throwSignConfigNotFindException(Exception e) throws ClassNotFoundException{
	   throw new ClassNotFoundException(e.getMessage(), e);
   }
   
   /**
    * 签名已过期异常
    * @param e
    * @throws Exception
    */
   protected void throwSignInfoOverdueException(Exception e) throws Exception{
	   throw e;
   }

   /**
    * 数据被篡改异常
    * @param e
    * @throws Exception
    */
   protected void throwDataTamperedException(Exception e) throws Exception{
	   throw e;
   }
   
   /**
    * AES加密
    * @param content 待加密内容
    * @param key 加密的密钥
    * @return
    */
   protected String encrypt(String content, String key) throws Exception{
	   return AES.encrypt_16key(content, key);
   }
   
   /**
    * AES解密
    * @param content 待解密内容
    * @param key 解密的密钥
    * @return
    */
   protected String decrypt(String content, String key) throws Exception {
	   return AES.desEncrypt_16key(content, key);
   }
   
   /**
    * 校验签名并执行通过后的集体操作
    * @param appkey 企业唯一编号，查找签名的配置信息
    * @param data 需要签名的字符串
    * @param sign 签名结果
    * @param signData 签名的数据
    * @return T
    * @throws Exception
    */
   public final T verifyAndDoPassHandle(String appkey, String data, String sign) throws Exception{
	   //当前接口访问量的key，等于子类的包名+类名+抽象类的方法名（verifyAndDoPassHandle）
	   String vcontkey = (this.getClass().getName()+"."+Thread.currentThread().getStackTrace()[1].getMethodName()).replace(".", "_");
	   /**
	    * 获取最大访问量，如果子类覆盖了最大访问量，则获取的是子类变量，否则获取的是父类的默认值
	    */
	   Integer maxCont = getMaxVisitNumber();

	   /**
		* 判断当前接口的并发请求是否过载
		* 过载时方法会抛出一个 Exception 异常，用来传递并发过载时的提示信息，最终将会被输出到客户端
		* @throws Exception
		*/
		judgeOverLoad(vcontkey, maxCont);
	   
	   try{
		   /*一个新的请求，并发量加 1 */
		   this.newRequest(vcontkey, maxCont);
		   
		   T result = null; //定义方法的返回值，泛型
		   
		   this.appkey = appkey;
		   this.data = data;
		   this.sign = sign;
		   
		   //得到 ReceiveBusKeyConfigService，用来查询签名的配置信息
		   ReceiveBusKeyConfigService receiveService = getReceiveBusKeyConfigService();
		   if(receiveService == null) {
			   throwReceiveBusKeyConfigServiceNullException(new Exception("空指针异常：receiveBusKeyConfigService 为空！"));
		   }
		   /* 获取签名的配置信息 */
		   SignConfig config = receiveService.findByNo(appkey);
		   if(config == null) {
			   throwSignConfigNotFindException(new Exception("当前appkey '" + (appkey != null ? appkey : "null") + "' 不存在。"));
		   }
		   /* 验证是否过期 */
		   if(config.getStatus() == 0){
			   throwSignInfoOverdueException(new Exception("签名已过期。"));
		   }
		   /* 获取签名的数据 */
		   String signField = getSignField();
		   /* 签名字符串校验 */
		   boolean pass = MD5.verify(signField, sign, config.getKey(), config.getInput_charset());
		   if(pass){
			   /* 参数解密，采用AES解密 */
			   String deSecertData = decrypt(data, config.getKey());
			   /* 签名验证通过后执行相应操作 */
			   result = doPassHandle(deSecertData);
		   }else{
			   throwDataTamperedException(new Exception("数据被篡改。"));
		   }
		   return result;
	   }catch(Exception e){
		   throw e;
	   }finally{
		   /* 销毁一个请求，并发数量减 1 */
		   this.destroyRequest(vcontkey, maxCont);
	   }
   }
   
}
