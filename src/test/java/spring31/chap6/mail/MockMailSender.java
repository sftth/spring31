package spring31.chap6.mail;

import java.util.*;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;

public class MockMailSender implements MailSender{
	private List<String> requests = new ArrayList<String>();
	
	public List<String> getRequests() {
		return requests;
	}
	
	@Override
	public void send(SimpleMailMessage simpleMessage) throws MailException {
		requests.add(simpleMessage.getTo()[0]);
		
	}

	@Override
	public void send(SimpleMailMessage[] sipleMessages) throws MailException {
		// TODO Auto-generated method stub
		
	}

}
