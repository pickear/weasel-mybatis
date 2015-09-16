package com.weasel.mybatis.test.repository;

import com.weasel.mybatis.MybatisOperations;
import com.weasel.mybatis.test.domain.User;
/**
 * @author Dylan
 * @time 2015年9月16日
 */
public class UserRepositoryImpl extends MybatisOperations<Long, User> implements UserRepository {

}
