package com.weasel.mybatis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weasel.core.Page;
import com.weasel.core.helper.GodHands;
import com.weasel.mybatis.dialect.Dialect;
import com.weasel.mybatis.exception.DatabaseException;

/**
 * 
 * @author Dylan
 * @time 上午10:13:21
 */
public class MybatisHelper {

	private final static Logger LOG = LoggerFactory.getLogger(MybatisHelper.class);
	
	private MybatisHelper(){}
	
	/**
	 * get the connection of database
	 * @param statement
	 * @return
	 */
	public static Connection getConnection(final MappedStatement statement){
		
		try {
			return statement.getConfiguration().getEnvironment().getDataSource().getConnection();
		} catch (SQLException e) {
			throw new DatabaseException("connection database exception " + e.getMessage());
		}
	}
	
	/**
	 * free the database resource
	 * @param rs
	 * @param ps
	 */
	public static void freeResource(Connection connection,ResultSet rs,PreparedStatement ps){
		
			try {
				if(null != rs){
					rs.close();
					rs = null;
				}
				if(null != ps){
					ps.close();
					ps = null;
				}
				if(null != connection){
					connection.close();
					connection = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

	/**
	 * 
	 * @param sql
	 * @param page
	 * @param dialect
	 * @return
	 */
	public static String buildPageSql(String sql, Page<?> page, Dialect dialect) {
		if(dialect.supportPage()){
			int start = (page.getCurrentPage()-1)*page.getPageSize();
			return dialect.buildPageSql(sql,start<0?0:start,page.getPageSize());
		}
		
		return sql;
	}

	/**
	 * 
	 * @param statement
	 * @param pageSql
	 * @param boundSql
	 * @return
	 */
	public static BoundSql newBoundSql(MappedStatement statement, String pageSql, BoundSql boundSql) {
		
		return new BoundSql(statement.getConfiguration(), pageSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
	}

	/**
	 * 
	 * @param statement
	 * @param sqlSource
	 * @return
	 */
	public static MappedStatement copyMappedStatement(MappedStatement statement, SqlSource sqlSource) {
		 MappedStatement.Builder builder = new MappedStatement.Builder(statement.getConfiguration(),
				 statement.getId(), sqlSource, statement.getSqlCommandType());
	        builder.resource(statement.getResource());
	        builder.fetchSize(statement.getFetchSize());
	        builder.statementType(statement.getStatementType());
	        builder.keyGenerator(statement.getKeyGenerator());
	        if (statement.getKeyProperties() != null) {
	            for (String keyProperty : statement.getKeyProperties()) {
	                builder.keyProperty(keyProperty);
	            }
	        }
	        builder.timeout(statement.getTimeout());
	        builder.parameterMap(statement.getParameterMap());
	        builder.resultMaps(statement.getResultMaps());
	        builder.cache(statement.getCache());
	        return builder.build();
	}

	public static Dialect getDialect(Properties properties) {
		String dialectName = properties.getProperty("DIALECT");
		if(StringUtils.isBlank(dialectName)){
			LOG.error("the 'DIALECT' property of plugin must be declare");
			throw new IllegalStateException("the 'DIALECT' property of plugin must be declare");
		}
		
		Dialect dialect = (Dialect) GodHands.newInstance(dialectName);
		if(null == dialect){
			LOG.error("the Dialect init fail,please check the dialect class exist or not");
			throw new IllegalStateException("the Dialect init fail,please check the dialect class exist or not");
		}
		return dialect;
	}

	public static String getSqlRegular(Properties properties) {
		String SQL_REGULAR = properties.getProperty("SQL_REGULAR");
		if(StringUtils.isBlank(SQL_REGULAR)){
			LOG.error("the 'SQL_REGULAR' property of plugin must be declare");
			throw new IllegalStateException("the 'SQL_REGULAR' property of plugin must be declare");
		}
		return SQL_REGULAR;
	}
	
}
