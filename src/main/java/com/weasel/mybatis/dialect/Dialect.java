package com.weasel.mybatis.dialect;

/**
 * 
 * @author Dylan
 * @time 下午3:54:34
 */
public abstract class Dialect {

	/**
	 * mark is support to page or not
	 * @return
	 */
	public boolean supportPage() {
		return true;
	}

	/**
	 * build the sql of page
	 * @param sql
	 * @param i
	 * @param pageSize
	 * @return
	 */
	public abstract String buildPageSql(String sql, int start, int pageSize);

}
