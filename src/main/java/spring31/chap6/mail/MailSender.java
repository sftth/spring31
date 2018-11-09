package spring31.chap6.mail;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public interface MailSender{
	void send(SimpleMailMessage simpleMessage) throws MailException;
	void send(SimpleMailMessage[] sipleMessages) throws MailException;
}
