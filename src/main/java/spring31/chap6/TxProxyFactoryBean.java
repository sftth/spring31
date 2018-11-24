package spring31.chap6;

import java.lang.reflect.Proxy;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

public class TxProxyFactoryBean implements FactoryBean<Object>{
	Object target;
	PlatformTransactionManager transactionManager;
	String pattern;
	Class<?> serviceInterface;
	
	public void setTarget(Object target) {
		this.target = target;
	}
	
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	public void setPattern (String pattern) {
		this.pattern = pattern;
	}
	
	public void setServiceInterface(Class<?> serviceInterface) {
		this.serviceInterface = serviceInterface;
	}

	//FactoryBean 인터페이스 구현 메소드
	@Override
	public Object getObject() throws Exception {
		TransactionHandler txHandler = new TransactionHandler();
		txHandler.setTarget(target);
		txHandler.setTransactionManager(transactionManager);
		txHandler.setPattern(pattern);
		return Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { serviceInterface }, txHandler);
	}

	@Override
	public Class<?> getObjectType() {
		// TODO Auto-generated method stub
		return serviceInterface;
	}
	
	public boolean inSingleton() {
		return false;
	}

}
