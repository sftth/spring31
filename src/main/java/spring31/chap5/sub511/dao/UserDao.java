package spring31.chap5.sub511.dao;

import java.util.List;

import spring31.chap5.sub511.domain.User;

public interface UserDao {
	public void add(User user);

	public User get(String id);

	public List<User> getAll();

	public void deleteAll();

	public int getCount();
	
	public void update(User user);
}
