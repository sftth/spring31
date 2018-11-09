package spring31.chap6.dao;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import spring31.chap6.domain.Level;
import spring31.chap6.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="../applicationContext.xml")
public class UserDaoTest {
	@Autowired UserDao dao; 
	@Autowired DataSource dataSource;
	
	private User user1;
	private User user2;
	private User user3; 
	
	@Before
	public void setUp() {
		this.user1 = new User("gyumee", "사용자1", "springno1", Level.BASIC, 1, 0,"sftt1@naver.com");
		this.user2 = new User("leegw700", "사용자2", "springno2", Level.SILVER, 55, 10,"sftt2@naver.com");
		this.user3 = new User("bumjin", "사용자3", "springno3", Level.GOLD, 100, 40,"sftt3@naver.com");
	}
	
	@Test 
	public void andAndGet() {		
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

	@Test(expected=EmptyResultDataAccessException.class)
	public void getUserFailure() throws SQLException {
		dao.deleteAll();
		assertThat(dao.getCount(), equalTo(0));
		
		dao.get("unknown_id");
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
	
	@Test
	public void getAll()  {
		dao.deleteAll();
		
		List<User> users0 = dao.getAll();
		assertThat(users0.size(), equalTo(0));
		
		dao.add(user1); // Id: gyumee
		List<User> users1 = dao.getAll();
		assertThat(users1.size(), equalTo(1));
		checkSameUser(user1, users1.get(0));
		
		dao.add(user2); // Id: leegw700
		List<User> users2 = dao.getAll();
		assertThat(users2.size(), equalTo(2));
		checkSameUser(user1, users2.get(0));  
		checkSameUser(user2, users2.get(1));
		
		dao.add(user3); // Id: bumjin
		List<User> users3 = dao.getAll();
		assertThat(users3.size(), equalTo(3));
		checkSameUser(user1, users3.get(0));  
		checkSameUser(user2, users3.get(1));  
		checkSameUser(user3, users3.get(2));  
	}

	private void checkSameUser(User user1, User user2) {
		assertThat(user1.getId(), equalTo(user2.getId()));
		assertThat(user1.getName(), equalTo(user2.getName()));
		assertThat(user1.getPassword(), equalTo(user2.getPassword()));
		assertThat(user1.getLevel(), equalTo(user2.getLevel()));
		assertThat(user1.getLogin(), equalTo(user2.getLogin()));
		assertThat(user1.getRecommend(), equalTo(user2.getRecommend()));
	}

	@Test(expected=DuplicateKeyException.class)
	public void duplciateKey() {
		dao.deleteAll();
		
		dao.add(user1);
		dao.add(user1);
	}
	
//	@Test
	public void sqlExceptionTranslate() {
		dao.deleteAll();
		
		try {
			dao.add(user1);
			dao.add(user1);
		}
		catch(DuplicateKeyException ex) {
			SQLException sqlEx = (SQLException)ex.getCause();
			SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);			
			DataAccessException transEx = set.translate(null, null, sqlEx);
			assertThat(transEx, equalTo(DuplicateKeyException.class));
		}
	}
	
	@Test
	public void update() {
		dao.deleteAll();
		
		dao.add(user1);		// ������ �����
		dao.add(user2);		// �������� ���� �����
		
		user1.setName("user001");
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

}
