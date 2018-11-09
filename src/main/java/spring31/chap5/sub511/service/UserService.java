package spring31.chap5.sub511.service;

import java.util.List;
import java.util.Properties;

import javax.mail.Session;
import javax.sql.DataSource;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import spring31.chap5.sub511.dao.UserDao;
import spring31.chap5.sub511.domain.Level;
import spring31.chap5.sub511.domain.User;

public class UserService {
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECCOMEND_FOR_GOLD = 30;
	
	private UserDao userDao;
	
	private PlatformTransactionManager transactionManager;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	public void upgradeLevels() throws Exception{
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			List<User> users = userDao.getAll();
			for(User user : users) {
				if(canUpgradeLevel(user)) {
					upgradeLevel(user);
				}
			}
			transactionManager.commit(status);
		} catch (Exception e) {
			transactionManager.rollback(status);
			throw e;
		} 		
	}
	
	private boolean canUpgradeLevel(User user) {
		Level currentLevel = user.getLevel();
		switch(currentLevel) {
		case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
		case SILVER: return (user.getRecommend() >= MIN_RECCOMEND_FOR_GOLD);
		case GOLD: return false;
		default: throw new IllegalArgumentException("Unknown Level: " + currentLevel);
		}
	}
	
	public void upgradeLevel(User user) throws Exception {
		user.upgradeLevel();
		userDao.update(user);
		sendUpgradeEMail(user);
	}
	
	public void add(User user) {
		if(user.getLevel() == null) user.setLevel(Level.BASIC);
		userDao.add(user);
	}
	
	public void sendUpgradeEMail(User user) {
		String host = "smtp.naver.com";
		
		final String username = "sftth";
		final String password = "gw170104@u";
		int port = 465;
		
		//메일 내용
		String recipient = "sftth@naver.com";
		String subject ="메일테스트";
		
		Properties props = System.getProperties();
		
		Session session = null;
		
		if(session == null) {
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.port", port);
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.ssl.enable", "true");
			props.put("mail.smpt.ssl.trust", host);
		}
	}
}
