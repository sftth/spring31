package spring31.chap5.sub511.dao;

import java.util.List;

import spring31.chap5.sub511.domain.User;

public interface UserDao {

	void add(User user);

	User get(String id);

	List<User> getAll();

	void deleteAll();

	int getCount();

	void update(User user);
}