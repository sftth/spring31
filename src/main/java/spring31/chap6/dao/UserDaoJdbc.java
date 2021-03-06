package spring31.chap6.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import spring31.chap6.domain.Level;
import spring31.chap6.domain.User;

public class UserDaoJdbc implements UserDao {
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	private JdbcTemplate jdbcTemplate;
	
	private RowMapper<User> userMapper = 
		new RowMapper<User>() {
				public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User user = new User();
				user.setId(rs.getString("id"));
				user.setName(rs.getString("name"));
				user.setPassword(rs.getString("password"));
				user.setLevel(Level.valueOf(rs.getInt("level")));
				user.setLogin(rs.getInt("login"));
				user.setRecommend(rs.getInt("recommend"));
				user.setEmail(rs.getString("email"));
				return user;
			}
		};

	public void add(User user) {
		this.jdbcTemplate.update(
				"insert into users(id, name, password, level, login, recommend, email) " +
				"values(?,?,?,?,?,?,?)", 
					user.getId(), user.getName(), user.getPassword(), 
					user.getLevel().intValue(), user.getLogin(), user.getRecommend(),user.getEmail());
	}

	public User get(String id) {
		return this.jdbcTemplate.queryForObject("select * from users where id = ?",
				new Object[] {id}, this.userMapper);
	} 

	public void deleteAll() {
		this.jdbcTemplate.update("delete from users");
	}

	public int getCount() {
		return this.jdbcTemplate.queryForInt("select count(*) from users");
	}

	public List<User> getAll() {
		return this.jdbcTemplate.query("select * from users order by name",this.userMapper);
	}

	@Override
	public void update(User user) {
		String query = "update users set name = ?, password = ?, level = ?, login = ?, recommend = ?, email = ? where id = ? ";
		this.jdbcTemplate.update(query, user.getName(), user.getPassword(), 
				                        user.getLevel().intValue(), user.getLogin(), 
				                        user.getRecommend(), user.getEmail(), user.getId());
	}
}
