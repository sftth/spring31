package spring31.chap5.sub541.dao;

import java.util.List;

import spring31.chap5.sub541.domain.User;

public interface UserDao {

	void add(User user);

	User get(String id);

	List<User> getAll();

	void deleteAll();

	int getCount();

	void update(User user);

}