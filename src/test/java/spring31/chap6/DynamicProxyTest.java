package spring31.chap6;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Proxy;

import org.junit.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

public class DynamicProxyTest {
	
	@Test
	public void simpleProxy() {
		Hello hello = new HelloTarget();
		assertThat(hello.sayHello("Jacob"), is("Hello Jacob"));
		assertThat(hello.sayHi("Jacob"), is("Hi Jacob"));
		assertThat(hello.sayThankYou("Jacob"), is("Thank You Jacob"));
	}
	
	@Test
	public void dynamicProxy() {
		Hello proxiedHello = (Hello) Proxy.newProxyInstance(
				getClass().getClassLoader(),
				new Class[] {Hello.class},
				new UppercaseHandler(new HelloTarget()));
		
		assertThat(proxiedHello.sayHello("Jacob"), is("HELLO JACOB"));
		assertThat(proxiedHello.sayHi("Jacob"), is("HI JACOB"));
		assertThat(proxiedHello.sayThankYou("Jacob"), is("THANK YOU JACOB"));
	}
	
	@Test
	public void proxyFactoryBean() {
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(new HelloTarget());
		pfBean.addAdvice(new UppercaseAdvice());
		
		Hello proxiedHello = (Hello) pfBean.getObject();
		
		assertThat(proxiedHello.sayHello("Jacob"), is("HELLO JACOB"));
		assertThat(proxiedHello.sayHi("Jacob"), is("HI JACOB"));
		assertThat(proxiedHello.sayThankYou("Jacob"), is("THANK YOU JACOB"));
	}
	
	@Test
	public void pointcutAdvisor() {
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(new HelloTarget());
		
		NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
		
		pointcut.setMappedName("sayH*");
		
		pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
		
		Hello proxiedHello = (Hello) pfBean.getObject();
		
		assertThat(proxiedHello.sayHello("Jacob"), is("HELLO JACOB"));
		assertThat(proxiedHello.sayHi("Jacob"), is("HI JACOB"));
		assertThat(proxiedHello.sayThankYou("Jacob"), is("Thank You Jacob"));
	}
}
