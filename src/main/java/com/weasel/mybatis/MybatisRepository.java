/**
 * MybatisRepositoryInterface.java
 */
package com.weasel.mybatis;

import java.io.Serializable;
import java.util.List;

import com.weasel.core.Page;

/**
 * @author Dylan
 * @time 2013-4-17
 */
public interface MybatisRepository<ID extends Serializable, T> {

	/**
	 * get the result by id
	 * 
	 * @param id
	 * @return
	 */
	T get(ID id);

	/**
	 * if exit,we update,or not,we insert
	 * 
	 * @param entity
	 */
	T save(T entity);

	/**
	 * save a list to database
	 * 
	 * @param entities
	 * @return
	 */
	int saveBatch(List<T> entities);
	
	/**
	 * insert the entity to database,if exit,will be exception
	 * 
	 * @param entity
	 * @return
	 */
	T insert(T entity);

	/**
	 * update the entity to database
	 * 
	 * @param entity
	 * @return
	 */
	int update(T entity);
	
	/**
	 * @param entity
	 * @return
	 */
	int delete(T entity);
	
	/**
	 * @param entities
	 * @return
	 */
	int deleteBatch(List<T> entities);

	/**
	 * query all
	 * 
	 * @param entity
	 * @return
	 */
	List<T> query(T entity);

	/**
	 * query for page
	 * 
	 * @param page
	 * @return
	 */
	Page<T> queryPage(Page<T> page);
}
