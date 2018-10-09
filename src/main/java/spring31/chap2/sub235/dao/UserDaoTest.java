package spring31.chap2.sub235.dao;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import spring31.chap2.sub235.domain.User;


public class UserDaoTest {
	private UserDao dao;
	private User user1;
	private User user2;
	private User user3;
	
	@Before
	public void setUp() {
		ApplicationContext context = new GenericXmlApplicationContext("spring31/chap2/sub235/applicationContext.xml");
		this.dao = context.getBean("userDao", UserDao.class);
		
		user1 = new User("use001","Jacob","pwd001");
		user2 = new User("use002","Hani","pwd002");
		user3 = new User("use003","Isic","pwd003");
	}
	
	@Test
	public void addAndGet() throws ClassNotFoundException, SQLException {
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
		dao.deleteAll();
		
		assertThat(dao.getCount(), equalTo(0));
		
		dao.get("unknown_id"); //예외가 발생하면 성공, 발생하지 않으면 실패
	}
}
