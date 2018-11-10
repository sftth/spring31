package spring31.chap5.sub513.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import spring31.chap5.sub511.dao.UserDao;
import spring31.chap5.sub511.domain.Level;
import spring31.chap5.sub511.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/spring31/chap5/applicationContext.xml")
public class UserServiceTest {
	@Autowired
	UserService userService;
	
	@Autowired
	UserDao userDao;
	
	List<User> users;
	
	@Before
	public void setUp() {
		users = Arrays.asList(
				new User("use001", "Jacob", "pwd001", Level.BASIC, 49, 0),
				new User("use002", "Hani", "pwd002", Level.BASIC, 55, 10),
				new User("use003", "Isic", "pwd003", Level.SILVER, 60, 29),
				new User("use003", "Isic", "pwd003", Level.SILVER, 60, 30),
				new User("use003", "Isic", "pwd003", Level.GOLD, 100, 100)
				);
	}
	
	@Test
	public void upgradeLevels() {
		userDao.deleteAll();
		for(User user : users) {
			userDao.add(user);
		}
		
		userService.upgradeLevels();
		//각 사용자별 예상 레벨 검
		checkLevel(users.get(0), Level.BASIC);
		checkLevel(users.get(1), Level.SILVER);
		checkLevel(users.get(2), Level.SILVER);
		checkLevel(users.get(3), Level.GOLD);
		checkLevel(users.get(4), Level.GOLD);
	}
	
	@Test
	public void add() {
		userDao.deleteAll();
		//GOLD 레벨이 지정된 user는 레벨을 초기화 하지 않음.
		User userWithLevel = users.get(4);
		//레벨이 비어 있는 사용자는 BASIC으로 설
		User userWithoutLevel = users.get(0);
		userWithoutLevel.setLevel(null);
		
		userService.add(userWithLevel);
		userService.add(userWithoutLevel);
		//DB에 저장된 결과를 확인
		User userWithLevelRead = userDao.get(userWithLevel.getId());
		User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());
		
		assertThat(userWithLevelRead.getLevel(), equalTo(userWithLevel.getLevel()));
		assertThat(userWithoutLevelRead.getLevel(), equalTo(Level.BASIC));
	}
	//DB에서 조회한 레벨을 확인하는 코드는 중복되므로 메소드로 분리함.
	private void checkLevel(User user, Level expectedLevel) {
		User userUpdate = userDao.get(user.getId());
		assertThat(userUpdate.getLevel(), equalTo(expectedLevel));
	}
}
