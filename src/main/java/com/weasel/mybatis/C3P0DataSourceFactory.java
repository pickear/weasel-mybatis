package com.weasel.mybatis;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * @author Dylan
 * @mail pickear@gmail.com
 * @time 2014年6月24日
 */
public class C3P0DataSourceFactory extends UnpooledDataSourceFactory {

	public C3P0DataSourceFactory(){
		this.dataSource = new ComboPooledDataSource();
	}
}
