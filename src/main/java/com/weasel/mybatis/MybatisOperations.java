/**
 * MybatisRepositorySupport.java
 */
package com.weasel.mybatis;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import com.weasel.core.Page;
import com.weasel.core.helper.GodHands;
import com.weasel.core.helper.annotation.Id;
import com.weasel.mybatis.exception.DatabaseException;

/**
 * this class offer some common crud function to operate mybatis for us. but
 * first ,we must offer a sqlSessionFactory bean for it;we can configurate 
 * <bean
 * id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
 * <property name="dataSource" ref="dataSource"/>
 * </bean>
 * @author Dylan
 * @time 2013-3-30
 */
public abstract class MybatisOperations<ID  extends Serializable, T> extends SqlSessionDaoSupport {

	private Class<T> entityClass;
	private String idName;

	@SuppressWarnings("unchecked")
	public MybatisOperations() {
		entityClass = (Class<T>) GodHands.genericsTypes(getClass())[1];
		Field [] fields = GodHands.getAccessibleFields(entityClass);
		for(Field field : fields){
			if(field.isAnnotationPresent(Id.class) || StringUtils.equalsIgnoreCase("id", field.getName())) {  
                this.idName = field.getName();  
            }  
		}
	}

	/**
	 * get the result by id
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T get(ID id) {
		Validate.notNull(id, "id must not be null");

		try {
			return (T)getSqlSession().selectOne(getNamespace().concat(".getById"), id);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public T get(T entity){
		
		Validate.notNull(entity, "id must not be null");

		try {
			return (T)getSqlSession().selectOne(getNamespace().concat(".get"), entity);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/**
	 * if exit,we update,or not,we insert
	 * 
	 * @param entity
	 */
	public T save(T entity) {

		Validate.notNull(entity, "entity must not be null");
		
		if (null == GodHands.getFieldValue(entity, idName)) {
			return insert(entity);
		}
		update(entity);
		return entity;
	}

	/**
	 * save a list to database
	 * 
	 * @param entities
	 * @return
	 */
	public int saveBatch(List<T> entities) {
		int result = 0;

		for (T entity : entities) {
			save(entity);
			++result;
		}

		return result;
	}

	/**
	 * insert the entity to database,if exit,will be exception
	 * 
	 * @param entity
	 * @return
	 */
	public T insert(T entity) {
		Validate.notNull(entity, "entity must not be null");
		try {
			getSqlSession().insert(getNamespace().concat(".save"), entity);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
		return entity;
	}

	/**
	 * update the entity to database
	 * 
	 * @param entity
	 * @return
	 */
	public int update(T entity) {

		Validate.notNull(entity, "entity must not be null");
		try {
			return getSqlSession().update(getNamespace().concat(".update"), entity);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	/**
	 * update for batch
	 * @param entities
	 * @return
	 */
	public int updateBatch(List<T> entities){
		
		int i = 0;
		
		for(T entity : entities){
			i += update(entity);
		}
		return i;
	}
	
	/**
	 * 
	 * @param entity
	 * @return
	 */
	public int delete(T entity){
		
		Validate.notNull(entity,"entity must not be null");
		
		return getSqlSession().delete(getNamespace().concat(".delete"), entity);
	}
	
	/**
	 * 
	 * @param entities
	 * @return
	 */
	public int deleteBatch(List<T> entities){
		
		int i = 0;
		for(T entity : entities){
			i += delete(entity);
		}
		return i;
	}

	/**
	 * query all
	 * 
	 * @param entity
	 * @return
	 */
	public List<T> query(T entity) {

		Validate.notNull(entity, "entity must not be null");

		try {
			return getSqlSession().selectList(getNamespace().concat(".query"), entity);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/**
	 * query for page
	 * 
	 * @param page
	 * @return
	 */
	public Page<T> queryPage(Page<T> page) {

		Validate.notNull(page, "page entity must not be null");

		try {
			List<T> results = getSqlSession().selectList(getNamespace().concat(".queryPage"), page);
			page.setResult(results);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
		return page;
	}

	protected String getNamespace() {
		return entityClass.getName();
	}

}
