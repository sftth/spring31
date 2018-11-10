package spring31.chap5.sub511.dao;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import spring31.chap5.sub511.dao.UserDaoJdbc;
import spring31.chap5.sub511.domain.Level;
import spring31.chap5.sub511.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
//어플리케이션 컨텍스트 설정파일 위치 지정
@ContextConfiguration(locations="/spring31/chap3/sub361/applicationContext.xml")
public class UserDaoTest {
	@Autowired
	private ApplicationContext context;
	
	private UserDaoJdbc dao;
	private User user1;
	private User user2;
	private User user3;
	
	@Before
	public void setUp() {
		this.dao = this.context.getBean("userDao", UserDaoJdbc.class);

		user1 = new User("use001","Jacob","pwd001",Level.BASIC,1,0);
		user2 = new User("use002","Hani","pwd002",Level.SILVER,55,10);
		user3 = new User("use003","Isic","pwd003",Level.GOLD,100,40);
	}
	
	@Test
	public void addAndGet() {
		//delete 추가
		dao.deleteAll();
		assertThat(dao.getCount(), equalTo(0));
		
		dao.add(user1);
		dao.add(user2);
		assertThat(dao.getCount(), equalTo(2));
		
		User userget1 = dao.get(user1.getId());
		checkSameUser(userget1, user1);
		
		User userget2 = dao.get(user2.getId());
		checkSameUser(userget2, user2);
	}
	
	@Test
	public void count() {
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
	public void getUserFailure() {
		dao.deleteAll();
		
		assertThat(dao.getCount(), equalTo(0));
		
		dao.get("unknown_id"); //예외가 발생하면 성공, 발생하지 않으면 실패
	}
	
	@Test
	public void update() {
		dao.deleteAll();
		
		dao.add(user1);
		dao.add(user2);
		
		user1.setName("Jacob");
		user1.setPassword("pwd01");
		user1.setLevel(Level.GOLD);
		user1.setLogin(1000);
		user1.setRecommend(999);
		
		dao.update(user1);
		
		User user1update = dao.get(user1.getId());
		checkSameUser(user1, user1update);
		User user2same = dao.get(user2.getId());
		checkSameUser(user2, user2same);
	}
	
	private void checkSameUser(User user1, User user2) {
		assertThat(user1.getId(), equalTo(user2.getId()));
		assertThat(user1.getName(), equalTo(user2.getName()));
		assertThat(user1.getPassword(), equalTo(user2.getPassword()));
		assertThat(user1.getLogin(), equalTo(user2.getLogin()));
		assertThat(user1.getRecommend(), equalTo(user2.getRecommend()));
	}
	
}
