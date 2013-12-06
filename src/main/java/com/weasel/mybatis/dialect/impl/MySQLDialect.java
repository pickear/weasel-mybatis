package com.weasel.mybatis.dialect.impl;

import com.weasel.mybatis.dialect.Dialect;

/**
 * 
 * @author Dylan
 * @time 上午11:57:37
 */
public class MySQLDialect extends Dialect {

	
	@Override
	public String buildPageSql(String sql, int offset, int pageSize) {
		
		StringBuilder builder = new StringBuilder(sql);
		return builder.append(" limit ").append(offset).append(",").append(pageSize).toString();
	}

}
