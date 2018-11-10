package spring31.chap5.sub541.service;

import static spring31.chap5.sub541.service.UserService.MIN_LOGCOUNT_FOR_SILVER;

import java.util.List;
import java.util.Properties;

import javax.activation.*;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.sql.DataSource;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import spring31.chap5.sub541.dao.UserDao;
import spring31.chap5.sub541.domain.Level;
import spring31.chap5.sub541.domain.User;

public class UserService {
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECCOMEND_FOR_GOLD = 30;
	
	private UserDao userDao;
	
	private DataSource dataSource;
	
	private PlatformTransactionManager transactionManager;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
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
	
	private void sendUpgradeEMail(User user) throws Exception{
		String host = "smtp.naver.com";
		
		final String username = "sftth";
		final String password = "gw170104@u";
		final int port = 465;
		
		String mailFrom = "sftth@naver.com";
		String mailTo = "sftth@naver.com";
		String mailTitle = "메일테스트";
		
		Properties props = System.getProperties();
		
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.starttls.enable","true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.ssl.enable", "false");
//		props.put("mail.smtp.ssl.trust", host);
		
		Session session = Session.getDefaultInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		session.setDebug(true);
		
		final MimeMessage mesg = new MimeMessage(session);
		
		mesg.setHeader("Importance", "High");
		mesg.setFrom(new InternetAddress(mailFrom));
		mesg.setRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
		mesg.setSubject(mailTitle);
		
		Multipart mp = new MimeMultipart();
		
		BodyPart mbp_body = new MimeBodyPart();
		mbp_body.setContent(getBodyContent().toString(), "text/html;charset=utf-8");
		
		BodyPart mbp_body2 = new MimeBodyPart();
	    javax.activation.DataSource source = new FileDataSource("/Users/Summit/IDE/hello.txt");
	    mbp_body2.setDataHandler(new DataHandler(source));
	    mbp_body2.setFileName("‎⁨hello.txt⁩");
		
		mp.addBodyPart(mbp_body);
		mp.addBodyPart(mbp_body2);
		mesg.setContent(mp);
		
		Transport.send(mesg);
	}
	
	public StringBuffer getBodyContent() {
		StringBuffer contents = new StringBuffer();
		
		contents.append("<html><head><title>Global Finalcial Validation System</title></head>");
		contents.append("<body><h1>한글은 괜찮나?</h1></body>");
		
		return contents;
	}
	
	public static void main(String[] args) {
		UserService service = new UserService();
		
		try {
			service.sendUpgradeEMail(new User("bumjin",  "name1", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

