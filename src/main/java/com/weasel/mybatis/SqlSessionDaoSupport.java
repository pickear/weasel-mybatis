package com.weasel.mybatis;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * @author Dylan
 * @time 2013-9-5
 */
public class SqlSessionDaoSupport {

	private final static String DEFAULT_RESOURCE = "mybatis_config.xml";
	
	private SqlSession sqlSession;
	
	private String resource = DEFAULT_RESOURCE;
	
	public SqlSessionDaoSupport(){
		try {
			InputStream inputStream = Resources.getResourceAsStream(resource);
			sqlSession = new SqlSessionFactoryBuilder().build(inputStream).openSession(ExecutorType.SIMPLE, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public SqlSession getSqlSession() {
		return sqlSession;
	}

	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}
	
	
}
