package spring31.chap2.sub222.dao;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import spring31.chap2.sub222.domain.User;


public class UserDaoTest {
	@Test
	public void addAndGet() throws ClassNotFoundException, SQLException {
		ApplicationContext context = new GenericXmlApplicationContext("spring31/chap2/sub222/applicationContext.xml");
		UserDao dao = context.getBean("userDao", UserDao.class);
		
		User user1 = new User("use001","Jacob","pwd001");
		User user2 = new User("use002","Hani","pwd002");
		
		//delete 추가
		dao.deleteAll();
		assertThat(dao.getCount(), equalTo(0));
		
		dao.add(user1);
		dao.add(user2);
		assertThat(dao.getCount(), equalTo(2));
		
		User userget1 = dao.get(user1.getId());
		assertThat(userget1.getName(), equalTo(user1.getName()));
		assertThat(userget1.getPassword(), equalTo(user1.getPassword()));
		
		User userget2 = dao.get(user2.getId());
		assertThat(userget2.getName(), equalTo(user2.getName()));
		assertThat(userget2.getPassword(), equalTo(user2.getPassword()));
	}
	
	@Test
	public void count() throws ClassNotFoundException, SQLException {
		ApplicationContext context = new GenericXmlApplicationContext("spring31/chap2/sub222/applicationContext.xml");
		UserDao dao = context.getBean("userDao", UserDao.class);
		User user1 = new User("use001","Jacob","pwd001");
		User user2 = new User("use002","Hani","pwd002");
		User user3 = new User("use003","Isic","pwd003");
		
		dao.deleteAll();
		assertThat(dao.getCount(), equalTo(0));
		
		dao.add(user1);
		assertThat(dao.getCount(), equalTo(1));
		
		dao.add(user2);
		assertThat(dao.getCount(), equalTo(2));
		
		dao.add(user3);
		assertThat(dao.getCount(), equalTo(3));
	}
	
	//테스트 중에 발생할 것으로 예상되는 예외 클래스를 지정
	@Test(expected = EmptyResultDataAccessException.class)
	public void getUserFailure() throws ClassNotFoundException, SQLException {
		ApplicationContext context = new GenericXmlApplicationContext("spring31/chap2/sub222/applicationContext.xml");
		UserDao dao = context.getBean("userDao", UserDao.class);
		dao.deleteAll();
		
		assertThat(dao.getCount(), equalTo(0));
		
		dao.get("unknown_id"); //예외가 발생하면 성공, 발생하지 않으면 실패
	}
}
