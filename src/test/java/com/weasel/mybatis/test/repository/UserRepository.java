package com.weasel.mybatis.test.repository;

import com.weasel.mybatis.MybatisRepository;
import com.weasel.mybatis.test.domain.User;
/**
 * @author Dylan
 * @time 2015年9月16日
 */
public interface UserRepository extends MybatisRepository<Long, User> {

}
