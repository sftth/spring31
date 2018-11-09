package spring31.chap6.mail;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;

public class MailSenderImpl implements MailSender {

	@Override
	public void send(SimpleMailMessage simpleMessage) throws MailException {
		String host = "smtp.naver.com";
		
		final String username = "sftth";
		final String password = "gw170104@u";
		int port = 465;
		
		//메일 내용
		String mailFrom = "sftth@naver.com";
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
			
			session = Session.getDefaultInstance(props, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});
			
			session.setDebug(true);
			
			Message mimeMessage = new MimeMessage(session);

			try {
				mimeMessage.setFrom(new InternetAddress(mailFrom));
				mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
				mimeMessage.setSubject(subject);
				
				Multipart mp = new MimeMultipart();
				
				BodyPart mbp_body = new MimeBodyPart();
				mbp_body.setContent("<h1>Test</h1>", "text/html;charset=UTF-8");
				mbp_body.setDisposition(BodyPart.INLINE);
				
				BodyPart mbp_body2 = new MimeBodyPart();
				DataSource source = new FileDataSource("C:\\snf_log_20181001.txt");
				mbp_body2.setDataHandler(new DataHandler(source));
				mbp_body2.setFileName("snflog.txt");
				
				mp.addBodyPart(mbp_body);
				mp.addBodyPart(mbp_body2);
				
				mimeMessage.setContent(mp);
				
				Transport.send(mimeMessage);
			} catch (AddressException e) {
				e.printStackTrace();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void send(SimpleMailMessage[] sipleMessages) throws MailException {

	}

}
