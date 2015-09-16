package com.weasel.mybatis.test;

import java.util.List;

import com.weasel.core.Page;
import com.weasel.mybatis.test.domain.User;
import com.weasel.mybatis.test.repository.UserRepository;
import com.weasel.mybatis.test.repository.UserRepositoryImpl;

/**
 * @author Dylan
 * @time 2015年9月16日
 */
public class UserRepositoryTest {

	public static UserRepository repository = new UserRepositoryImpl();
	
	public static void main(String[] args) {
	
	//	testInsert();
	//	testGet();
		testQueryPage();
	}
	
	public static void testInsert(){
		User user = new User();
		user.setId(new Long(6));
		user.setUsername("李四");
		user.setPassword("123");
		repository.insert(user);
	}
	
	public static void testGet(){
		
		User u = repository.get(new Long(1));
		System.out.println(u.getUsername());
	}
	
	public static void testQueryPage(){
		
		Page<User> page = new Page<User>();
		page.setPageSize(2);
		page.setCurrentPage(2);
		page = repository.queryPage(page);
		
		List<User> results = page.getResult();
		
		for(User u : results){
			System.out.println(u.getId());
		}
	}
}
