package spring31.chap6;

import org.springframework.beans.factory.FactoryBean;

public class MessageFactoryBean implements FactoryBean<Message>{
	private String text;
	
	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public Message getObject() throws Exception {
		return Message.newMessage(text);
	}

	@Override
	public Class<?> getObjectType() {
		// TODO Auto-generated method stub
		return Message.class;
	}

	public boolean isSingleton() {
		return false;
	}
}
