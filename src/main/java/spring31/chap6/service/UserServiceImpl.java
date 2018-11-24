package spring31.chap6.service;

import java.util.List;
import java.util.Properties;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;

import org.hsqldb.lib.StringUtil;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import spring31.chap6.dao.UserDao;
import spring31.chap6.domain.Level;
import spring31.chap6.domain.User;
import spring31.chap6.mail.MailSender;

public class UserServiceImpl implements UserService {
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECCOMEND_FOR_GOLD = 30;
	
	private UserDao userDao;
	
	private PlatformTransactionManager transactionManager;
	
	private MailSender mailSender;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	public void upgradeLevels(){
		List<User> users = userDao.getAll();
		for(User user : users) {
			if(canUpgradeLevel(user)) {
				upgradeLevel(user);
			}
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
	
	public void upgradeLevel(User user){
		user.upgradeLevel();
		userDao.update(user);
		sendUpgradeEMail(user);
	}
	
	public void add(User user) {
		if(user.getLevel() == null) user.setLevel(Level.BASIC);
		userDao.add(user);
	}
	
	public void sendUpgradeEMail(User user) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setFrom("admin@me.com");
		mailMessage.setSubject("Upgrade 안내");
		mailMessage.setText(String.format("사용자의 등급이 %1s",user.getLevel().name()) );
		
		this.mailSender.send(mailMessage);
	}
}