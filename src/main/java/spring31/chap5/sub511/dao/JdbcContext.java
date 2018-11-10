package spring31.chap5.sub511.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import spring31.chap5.sub511.strategy.StatementStrategy;

public class JdbcContext {
	private DataSource dataSource;
	
	//dao를 DI 받을 수 있게 준
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void workWithStatementStrategy(StatementStrategy stmt) throws SQLException {
		Connection c = null;
		PreparedStatement ps = null;
		
		try {
			c = dataSource.getConnection();
			ps = stmt.makePreparedStatement(c);
			ps.executeUpdate();
		} catch (Exception e) {
			throw e;
		}  finally {
			 if(ps != null) {
				 try {
					 ps.close();
				 } catch(SQLException e) {
				 }
			 }
			if(c != null) {
				try {
					c.close();
				} catch (SQLException e) {
				}
			}
		 }
	}
	
	public void executeSql(final String query) throws SQLException {
		workWithStatementStrategy(new StatementStrategy() {
			
			@Override
			public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
				return c.prepareStatement(query);
			}
		});
	}

}