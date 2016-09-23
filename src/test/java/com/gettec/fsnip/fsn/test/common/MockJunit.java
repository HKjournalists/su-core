package com.gettec.fsnip.fsn.test.common;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
/** 
* @ClassName: MockTest  单元测试基类
* @author chen_wsong
* @date 2014-10-29 下午5:52:25 
*
*/
@RunWith(SpringJUnit4ClassRunner.class) 
@TestExecutionListeners
@ContextConfiguration(locations={"classpath:META-INF/spring-context.xml","classpath:META-INF/aop-config.xml"})
public class MockJunit extends AbstractTransactionalJUnit4SpringContextTests { 
	protected Mockery context = new JUnit4Mockery(); 

	protected int page=1;
	protected int pageSize=10;
	
	protected int id;  
	protected String keyword; 
}