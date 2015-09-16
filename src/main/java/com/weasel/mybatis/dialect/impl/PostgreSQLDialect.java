package com.weasel.mybatis.dialect.impl;

import com.weasel.mybatis.dialect.Dialect;
/**
 * postgresql数据库的分布方言
 * @author Dylan
 * @time 2015年9月16日
 */
public class PostgreSQLDialect extends Dialect {

	@Override
	public String buildPageSql(String sql, int offset, int pageSize) {
		// TODO Auto-generated method stub
		
		StringBuilder builder = new StringBuilder(sql);
		return builder.append(" limit ").append(pageSize).append(" offset ").append(offset).toString();
	}

}
