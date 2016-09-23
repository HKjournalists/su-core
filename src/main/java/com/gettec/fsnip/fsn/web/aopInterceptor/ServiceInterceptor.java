package com.gettec.fsnip.fsn.web.aopInterceptor;



import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;


import com.gettec.fsnip.fsn.web.ThreadLocalUtil;

import com.gettec.logsystem.client.model.ServiceLog;

/**
 * 
 * @author Rongshen Xie
 *
 */
@Aspect
public class ServiceInterceptor{
	
/*执行顺序：
	前置通知，是在方法前执行吗？
	环绕通知 执行，进入方法...
	执行save()方法...
	@后置通知，是在方法后执行吗？
	最终通知 执行...
	@异常通知，程序出现异常了吗？
	退出方法...
*/
/*	
 * 解析：
 * @Pointcut("execution(* com.newer.service..*.*(..) ")
 * @Pointcut("execution(* com.newer.action..*.user*LoginOut())")
 * execution(* com.newer.service..*.*(..) <br/>
 * execution(1 com.newer.service..2.3(..) <br/>
 * 1. 表示返回值类型，例如：java.lang.String,*号代表任何返回类型 ，!void表示任意返回值类型(必须有返回值)<br/>
 * 2. 表示类，* 号代表对所有类拦截 <br/>
 * 3. 表示方法，* 号代表所有方法 <br/>
 * 4. service.. 这两个点代表可以对service下的子包进行拦截 <br/>
 * 5. (..) 表示方法的参数可以可无，一个也行，多个亦可，例如：(java.lang.String,..)
 * 
 * 注解引用切面的写法
 * @Before("userLogin()")
 * @Before("userLogin() && userLoginOut()")
 * @Before("execution(* com.vveoss.examination.dailyExam.submitPaper(..))")
*/

	/** 
	 * 声明一个切入点  <br/>
	 */
	@SuppressWarnings("unused")
	@Pointcut("execution(* com.gettec.fsnip.fsn.service.*.impl.*.*(..))|| execution(* com.lhfs.fsn.service.*.impl..*.*(..))")
	private void anyMethod(){}
	
	
	/**
	 * 注解定义前置通知(方法执行前执行) <br/>
	 * 需指明切入点 <br/>
	 */
//	@Before("anyMethod()")
	public void doAccessCheck() {
		System.out.println("前置通知，是在方法前执行吗？");
	}
	
	/**
	 * 前置通知，获取被拦截方法的参数 <br/>
	 * 根据参数拦截相关方法，被拦截的方法参数类型需相同 <br/>
	 * args(name) 里参数name必须和*Two(String name)参数名相同[name-name]<br/>
	 * 需指明切入点 ，可以指定多个。 用 [||、&&]<br/>
	 */
//	@Before("anyMethod() && args(name)")
	public void doAccessCheckTwo(String name) {
		System.out.println("获取传入参数的前置通知，传入参数name：" + name);
	}
	
	/**
	 * 后置通知(方法执行成功后执行) <br/>
	 * 需指明切入点 <br/>
	 */
//	@AfterReturning("anyMethod()")
	public void doAccessReturning() {
		System.out.println("后置通知，是在方法后执行吗？");
	}
	
	/**
	 * 后置通知，获取被拦截方法的返回数据 <br/>
	 * 注意参数的设定 <br/>
	 * 需指明切入点 <br/>
	 */
//	@AfterReturning(pointcut="anyMethod()", returning="result")
	public void doAccessReturningTwo(String result) {
		System.out.println("带返回值的后置通知，返回值result："+result);
	}
	
	/**
	 * 最终通知 (finally块)<br/>
	 * 需指明切入点 <br/>
	 */
//	@After("anyMethod()")
	public void doAfter() {
		System.out.println("最终通知 执行...");
	}
	
	/**
	 * 例外通知(异常通知catch块) <br/>
	 * 后置通知不执行(最终通知在异常通知前执行) <br/>
	 * 需指明切入点 <br/>
	 */
	@AfterThrowing("anyMethod()")
	public void doAfterThrowing() {
		System.out.println("异常通知，程序出现异常了吗？");
	}
	
	/**
	 * 例外通知，获取异常 <br/>
	 * 需指明切入点 <br/>
	 */
//	@AfterThrowing(pointcut="anyMethod()", throwing="e")
	public void doAfterThrowingTwo(Exception e) {
//		ThreadLocalUtil.get().getServiceLogs().
//		slog.setError(false);
//		slog.setErrorMsg("no error");
//		System.out.println("异常通知，异常："+e);
	}
	
	/**
	 * 环绕通知 (用于权限判断)<br/>
	 * 除方法名和参数名可修改外，其余不可变 <br/>
	 * 需指明切入点 <br/>
	 */
	@Around("anyMethod()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		ServiceLog slog = new ServiceLog();
		slog.setStartTime(ThreadLocalUtil.getClient().getCurrentTime());
		//获取被拦截的方法
		Method method = ((MethodSignature)pjp.getSignature()).getMethod();
		slog.setBelongClass(pjp.getSourceLocation().getWithinType().getName());
		slog.setError(false);
		slog.setErrorMsg("no error");
		slog.setMethodName(method.getName());
		slog.setReturnValue(method.getReturnType().toString());
		List<String> ptype = new ArrayList<String>();
		//获取拦截方法的参数
		for (Class<?> object : method.getParameterTypes()) {
			ptype.add(object.getName().toString());
		}
		slog.setParametersType(ptype);
		//必须执行这方法，如果后面有切面则先执行切面；
		//如果不执行proceed(),则后面的方法和和切面都不执行
		Object result = pjp.proceed();
		if(result!= null&&result.getClass().getName().endsWith("ResponseVO")){
			// ResponseVO vo = (ResponseVO)result;
		/*	 if(vo.getErrorMsg() != null){
				 slog.setError(true);`
				 slog.setErrorMsg(vo.getErrorMsg());
			 }*/
		}
		slog.setEndTime(ThreadLocalUtil.getClient().getCurrentTime());
		ThreadLocalUtil.get().getServiceLogs().add(slog);
		return result;
	}
	
}
