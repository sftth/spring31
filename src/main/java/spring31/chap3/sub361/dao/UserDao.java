package spring31.chap3.sub361.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import spring31.chap3.sub361.dao.UserDao;
import spring31.chap3.sub361.domain.User;

public class UserDao {
	private JdbcTemplate jdbcTemplate;
	
	private RowMapper<User> userMapper = new RowMapper<User>() { 
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			return user;
		}
	};
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public void add(User user) throws SQLException {
		this.jdbcTemplate.update("insert into users(id,name,password) values(?,?,?)",
				user.getId(),user.getName(),user.getPassword());
	}

	public User get(String id) throws SQLException {
		return this.jdbcTemplate.queryForObject("select * from users where id = ?", 
				new Object[] {id}, this.userMapper);
		
	}

	public void deleteAll() {
		this.jdbcTemplate.update("delete from users");
	}
	
	public int getCount() throws ClassNotFoundException, SQLException {
		return this.jdbcTemplate.queryForInt("select count(*) from users");
	}
	
	public List<User> getAll() {
		return this.jdbcTemplate.query("select * from users order by id", this.userMapper);
	}
	
}
