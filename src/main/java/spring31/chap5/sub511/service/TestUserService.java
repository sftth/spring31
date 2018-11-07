package spring31.chap5.sub511.service;

import spring31.chap5.sub511.domain.User;
import spring31.chap5.sub511.service.UserService;

/**
 * @author summit.park
 * id 에 해당하는 사용자 upgradeLevel시 
 * TestUserServiceException 발생
 */
public class TestUserService extends UserService {
	private String id;
	
	public TestUserService(String id) {
		this.id = id;
	}
	
	//UserService 메소드를 override 함
	public void upgradeLevel(User user)  throws Exception {
		//지정된 id의 User 오브젝트가 발견되면 예외를 throw 함.
		if(user.getId().equals(id)) throw new TestUserServletException();
		super.upgradeLevel(user);
	}
	
	static class TestUserServletException extends RuntimeException {
		
	}
}
