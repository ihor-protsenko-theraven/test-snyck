package com.essence.hc.util.dao.impl;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.sql.DataSource;


import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.essence.hc.model.User;
import com.essence.hc.util.dao.PruebaDAO;


public class PruebaDAOImpl implements PruebaDAO {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	  private DataSource dataSource;

//	  public PruebaDAOImpl(DataSource ds) {
//	    dataSource = ds;
//	  }
	  
	  public PruebaDAOImpl() {
		  dataSource = null;
	  }
	  
	  
//	/**
//	 * Returns the database datetime
//	 * @return
//	 */
//	public String getDate(){
//		
//		try{
//			JdbcTemplate db = new JdbcTemplate(dataSource);
//			String date = (String) db.queryForObject("select cast(getdate() as nvarchar) as now", String.class);
//		if (date == null)
//			logger.error("Error llamando al SQLSERVER");		
//			return "Hola desde el DAO: " + date ;
//		} catch(DataAccessException e){
//			logger.error("DataAccessExcepcion",e);
//			return "0";
//		}
//	}
//	
//	
//	/**
//	 * Returns the number of users on the table
//	 * @return
//	 */
//	public int getInt(){
//		try{
//			JdbcTemplate db = new JdbcTemplate(dataSource);
//			int i = db.queryForInt("Select count(*) from users");
//			return i;
//		} catch (Exception e){
//			logger.error("DataAccessExcepcion",e);
//			//logger.error("Valor del tamplate: {}", jdbcTemplate.toString() );
//		}
//		return -1;
//	}
//	
//	/**
//	 * Inserts a new user
//	 */
//	
//	public int newUser(String login, String pwd, int status, String name){
//		try{
//			JdbcTemplate db = new JdbcTemplate(dataSource);
//			int i = db.update(String.format("insert into users (login, password, status, name) values ('%s','%s',%d,'%s');", login, pwd, status, name));
//			logger.info("Usuario {} insertado",name);
//			return i;
//		} catch (Exception e){
//			logger.error("DataAccessExcepcion",e);
//			//logger.error("Valor del tamplate: {}", jdbcTemplate.toString() );
//		}
//		return -1;
//	}
//	
//	
//	RowMapper<String> StringMapper = new RowMapper<String>() {
//		@Override
//		public String mapRow(ResultSet rs, int rowNum)
//				throws SQLException {
//			String str = rs.getString(1);
//			return str;
//		}
//	};
//	
//	/**
//	 * Modify user with a procedure
//	 * @return
//	 */
//	public int activateUser(int userId){
//		try{
//			ExecSP sp = new ExecSP(dataSource,"dbo.UPDATESTATUS");
//			sp.execute(userId);
//			return 0;
//		} catch (Exception e){
//			logger.error("DataAccessExcepcion",e);
//			//logger.error("Valor del tamplate: {}", jdbcTemplate.toString() );
//		}
//		return -1;
//		
//	}
//	
//	/**
//	 * Clase para la ejecución de un procedimiento almacenado de la bbdd
//	 * @author manuel.garcia
//	 *
//	 */
//	   class ExecSP extends StoredProcedure {
//	        private static final String ID_PARAM = "@userId";
//	 
//	        public ExecSP(DataSource dataSource, String sprocName) {
//	            super(dataSource, sprocName);
//	            declareParameter(new SqlParameter(ID_PARAM,
//	                    Types.INTEGER));
//	            compile();
//	        }
//	 
//	        public Map execute(int id) {
//	            Map inputs = new HashMap();
//	            inputs.put(ID_PARAM, id);
//	            return super.execute(inputs);
//	        }
//	 
//	    }
//
//	@Override
//	/**
//	 * Returns a Vector of users
//	 */
//	public Vector<User> getUsers(int idFrom) {		
//		try{
//			JdbcTemplate db = new JdbcTemplate(dataSource);
//			Vector<User> v = new Vector<User>(db.query("Select top 25 login,name from users where id > ?", new Object[]{idFrom},userMapper ));
//			if (v.size() == 0)
//				logger.error(String.format("Errors getting users"));
//			return v;
//		} catch (Exception e){
//			logger.error("DataAccessExcepcion",e);
//		}
//		return null;
//	}
//	
//
//	RowMapper<User> userMapper = new RowMapper<User>() {
//		@Override
//		public User mapRow(ResultSet rs, int rowNum)
//				throws SQLException {
//			User user = new User();
//			user.setName(rs.getString(1));
//			user.setSurName(rs.getString(2));
//			return user;
//		}
//	};


	/**
	 * Deberiamos atacar a la bbdd pero por ahora creamos un usuario con lo que entre
	 * siempre que se llame 'test'
	 */
//	@Override
//	public User getUserByCredentials(String login, String password,
//			String country, String service, boolean s) {
//		// DUMMY: Test valido. el resto no
//		User user = null;
//		// Creamos el token de seguridad
//		if (StringUtils.containsIgnoreCase(login, "test")){
//			List<GrantedAuthority> AUTHORITIES = new ArrayList<GrantedAuthority>();
//			AUTHORITIES.add(new SimpleGrantedAuthority("ROLE_USER"));
//			AUTHORITIES.add(new SimpleGrantedAuthority("ROLE_OWNER"));
//			user = new User(login,password,AUTHORITIES);
//			user.setMobile("696734567");
//			user.setId("10");
//		}
//		return user;
//	}

	//	@Autowired
	//	DataSource dataSource;
	//@Autowired
	//JdbcTemplate jdbcTemplate;
	
}
