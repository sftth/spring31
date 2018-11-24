package spring31.chap6.service;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import spring31.chap6.domain.User;

public class UserServiceTx implements UserService {
	UserService userService;
	
	PlatformTransactionManager transactionManager;
	
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	//UserService DI
	@Override
	public void add(User user) {
		userService.add(user);
	}

	//DI받은 UserService 모든 기능 위임
	@Override
	public void upgradeLevels() {
		TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			userService.upgradeLevels();
			this.transactionManager.commit(status);
		} catch (RuntimeException e) {
			this.transactionManager.rollback(status);
			throw e;
		}
	}

}
