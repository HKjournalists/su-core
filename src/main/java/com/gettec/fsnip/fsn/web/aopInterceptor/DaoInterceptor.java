package com.gettec.fsnip.fsn.web.aopInterceptor;



import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;


import com.gettec.fsnip.fsn.web.ThreadLocalUtil;
import com.gettec.logsystem.client.model.DaoLog;
/**
 * 
 * @author Rongshen Xie
 *
 */
//@Component
@Aspect
public class DaoInterceptor {
	/**
	 * 声明一个切入点 <br/>
	 */
	@SuppressWarnings("unused")
	// @Pointcut("execution(* *.dao.impl.*.*(..)) ")
	// @Pointcut("execution(* com.gettec.lims.common.dao.impl.AbstractModelDAOImpl.*(..)) ")
	// @Pointcut("execution(* com.gettec.lims.core.*.dao.impl.*.*(..))")
	@Pointcut("execution(* com.gettec.fsnip.fsn.dao.*.impl.*.*(..))|| execution(* com.lhfs.fsn.dao.*.impl..*.*(..))")
	private void anyMethod() {
	}

	// @Before("anyMethod()")
	public void doAccessCheck() {
	}

	// @Before("anyMethod() && args(name)")
	public void doAccessCheckTwo(String name) {
	}

	// @AfterReturning("anyMethod()")
	public void doAccessReturning() {
	}

	// @AfterReturning(pointcut="anyMethod()", returning="result")
	public void doAccessReturningTwo(String result) {
	}

	// @After("anyMethod()")
	public void doAfter() {
	}

	// @AfterThrowing("anyMethod()")
	public void doAfterThrowing() {
	}

	// @AfterThrowing(pointcut="anyMethod()", throwing="e")
	public void doAfterThrowingTwo(Exception e) {
	}

	@Around("anyMethod()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		// 获取被拦截的方法
		Method method = ((MethodSignature) pjp.getSignature()).getMethod();
		DaoLog dlog = new DaoLog();
		dlog.setBelongClass(pjp.getSourceLocation().getWithinType().getName());
		dlog.setStartTime(ThreadLocalUtil.getClient().getCurrentTime());
		dlog.setError(false);
		dlog.setErrorMsg("no dao error");
		dlog.setMethodName(method.getName());
		dlog.setReturnValue(method.getReturnType().toString());
		/** 不取具体数据暂时屏蔽
		 * 
		List<String> ptype = new ArrayList<String>();
		// 获取拦截方法的参数
		Object[] obj = pjp.getArgs();
		List<Map<String, String>> pvalue = new ArrayList<Map<String, String>>();
		for (Object object : obj) {
			Map<String, String> hs = new HashMap<String, String>();
			if (object == null) {
				for (Class<?> clzss : method.getParameterTypes()) {
					ptype.add(clzss.getClass().toString());
					hs.put(clzss.getClass().toString(), "null");

				}
			} else {
				ptype.add(object.getClass().getName().toString());
				if (!object.getClass().getName().startsWith("java.lang")) {
					Field[] fields = object.getClass().getDeclaredFields();
					for (int i = 0; i < fields.length; i++) {
						fields[i].setAccessible(true);
						String rs = vaildObject(fields[i], fields[i].get(object));
						if (rs.equals("0")) {
							hs.put(fields[i].getName().toString(), (fields[i]
									.get(object) == null) ? "null" : fields[i]
									.get(object).toString());
						} else if (rs.equals("-1")) {
							hs.put(fields[i].getName().toString(), "null");
						} else {
							hs.put(fields[i].getName().toString(), rs);
						}
					}
				} else {
					hs.put(object.getClass().toString(), object.toString());
				}
			}
			pvalue.add(hs);
		}
		dlog.setParametersType(ptype);
		dlog.setParametersValue(pvalue);
		 */
		// 必须执行这方法，如果后面有切面则先执行切面；
		// 如果不执行proceed(),则后面的方法和和切面都不执行
		Object result = pjp.proceed();
		dlog.setEndTime(ThreadLocalUtil.getClient().getCurrentTime());
		ThreadLocalUtil.get().getDaoLogs().add(dlog);
		return result;
	}

	public static String vaildObject(Field filed, Object obj) throws Exception {
		String r = "0";
		if (obj != null && !obj.getClass().getName().startsWith("java.lang")
				&& !obj.getClass().getName().startsWith("java.util.Date")) {
			// 先取当前 obj 是参数的某个属性 的annotation 判断是否为jpa的相关映射
			if (filed.getAnnotation(javax.persistence.ManyToMany.class) != null
					|| filed.getAnnotation(javax.persistence.ManyToOne.class) != null
					|| filed.getAnnotation(javax.persistence.OneToOne.class) != null) {
				Field[] fields = obj.getClass().getDeclaredFields();
				for (int i = 0; i < fields.length; i++) {
					fields[i].setAccessible(true);
					if (fields[i].getAnnotation(javax.persistence.Id.class) != null) {
						try {
							r = fields[i].get(obj).toString();
						} catch (Exception e) {
							r = "-1";
						}
						break;
					}
				}
			}else if(filed.getAnnotation(javax.persistence.OneToMany.class) != null){
				r = "2";
			}
		}
		return r;
	}

}
