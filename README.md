weasel-mybatis
==============
前言

这是一个用于操作mybatis的包，基于普通项目.该包主要封装了对数据库的一些通用CURD操作和数据库的连接。通常在应用中我们都需要分页，这里也提供了一个分页插件和分页方法，方便应用的分页操作。
目前支持分页的数据库主要有两个，分别是MySQL和PostgreSQL。如果需要支持其他数据库的分页也很简单，用户主要编写自己的分页方言。
编写自己的方言需要实现Dialect类，并把该方言配置到mybatis的配置文件中，怎么配置下面会有介绍。项目基于mybatis-3.2.1

依赖包

要连接数据库，首先要将数据库厂商的连接实现依赖到项目中，在这个包中没有依赖该实现包，需要使用者在自己的应用中依赖。
mysql的依赖配置

<dependency>
	<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId>
	<version>5.1.36</version>
</dependency>

postgresql的依赖配置
<dependency>
	<groupId>org.postgresql</groupId>
	<artifactId>postgresql</artifactId>
	<version>9.4-1202-jdbc42</version>
</dependency>


配置

因为该项目是在mybatis之上进行适度的封装，所以mybatis的配置依然适合该项目，更多的mybatis配置可以参考官方文档:http://mybatis.github.io/mybatis-3/zh/index.html
这里给出一个简单的配置供参考，该配置文件在项目中也存在。

首先定义一个jdbc.properties，用于定义数据库的连接信息:
#jdbc settings

#jdbc.dirverClass=com.mysql.jdbc.Driver
#jdbc.url=jdbc:mysql://127.0.0.1:3306?useUnicode=true&characterEncoding=UTF-8
#jdbc.username=你的用户 名
#jdbc.password=你的密码

jdbc.dirverClass=org.postgresql.Driver
jdbc.url=jdbc:postgresql://127.0.0.1:5432/test
jdbc.username=你的用户 名
jdbc.password=你的密码

定义mybatis的配置文件(这里需要命名为mybatis_config.xml)
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
	<properties resource="jdbc.properties"/>  <!--在上面配置的jdbc.properties文件-->
	
	<settings>
		<setting name="cacheEnabled" value="true"/>
		<!-- <setting name="lazyLoadingEnabled" value="true"/> --> <!-- 如果这里开方lazyLoad，需要在自己的应用依赖cglib库。似乎在新版本中nybatis默认包含了Javassist，如果想使用cglib要另行配置-->
		<setting name="aggressiveLazyLoading" value="false"/>
	</settings>
	
	<typeAliases>   
       <typeAlias alias="User" type="com.weasel.mybatis.test.domain.User"/>   <!--起别名，方便在mapper文件中使用-->
    </typeAliases>  
    
    <plugins>
		<plugin interceptor="com.weasel.mybatis.PagePlugin">  <!--定义分页插件--?
			<property name="SQL_REGULAR" value=".*?queryPage.*?"/>  <!--定义插件会拦截哪些语句-->
			<!-- <property name="DIALECT" value="com.weasel.mybatis.dialect.impl.MySQLDialect"/> -->  <!--mysql分页方言-->
			<property name="DIALECT" value="com.weasel.mybatis.dialect.impl.PostgreSQLDialect"/>  <!--postgresql分页方言-->
		</plugin>
	</plugins>
	
	<environments default="development">
		<environment id="development">
			<transactionManager type="JDBC"/>
			<dataSource type="com.weasel.mybatis.C3P0DataSourceFactory">   <!--自定义的c3po factory-->
				<property name="driverClass" value="${jdbc.dirverClass}" />
                <property name="jdbcUrl" value="${jdbc.url}" />
                <property name="user" value="${jdbc.username}" />
                <property name="password" value="${jdbc.password}" />
                <property name="idleConnectionTestPeriod" value="60" />
                <property name="maxPoolSize" value="20" />
                <property name="maxIdleTime" value="1600" />
                <property name="testConnectionOnCheckout" value="true"/>
			</dataSource>
		</environment>
	</environments>
	
	<mappers>   
        <mapper resource="mappers/user.xml" />    <!--用户自定义的mapper文件-->
    </mappers>

</configuration>

如何使用

首先可以自定自己的一个接口，并且该接口继承MybatisRepository接口
public interface UserRepository extends MybatisRepository<Long, User> {

}

定义自己的一个实现类，继承MybatisOperations类，并实现自己定义的接口
public class UserRepositoryImpl extends MybatisOperations<Long, User> implements UserRepository {

}
这样自定义的类就拥有了连接数据库和一些通用的CRUD操作，这些方法来自MybatisOperations，分别有get(ID id);save(T entity);saveBatch(List<T> entities);insert(T entity);update(T entity);delete(T entity); deleteBatch(List<T> entities);query(T entity); queryPage(Page<T> page);方法。
这里值得注意的是save方法，该方法的默认实现是当entity的id值不为空时，认为用户想做的是更新操作，会调用update方法。当entity的id值为空时，认为用户想做的是插入操作，会调用insert方法。

mapper文件的配置

当然，具体的sql语句该包是不知道的，这个还需要用户自己去定义，sql语句的定义在mapper文件中，具体怎么写mapper文件用户可以参考mybatis的官方文档，下面给一个简单的:
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">


<mapper namespace="com.weasel.mybatis.test.domain.User">  

	<resultMap id="userResultMap" type="User">
		<id property="id" column="id" />
		<result property="username" column="username" />
		<result property="password" column="password" />
	</resultMap>

	<select id="getById" resultType="User">
		select * from users where id =
		#{id}
	</select>

	<insert id="save" parameterType="User">
		insert into users
		(id,username,password)
		values (#{id},#{username},#{password})
	</insert>

	<update id="update">
		update users set
		username = #{username},
		password = #{password}
		where id = #{id}
	</update>

	<select id="queryPage" resultMap="userResultMap">
		select id as id,
			   username as username,
			   password as password
	    from users
	</select>

</mapper>

mapper文件配置的一些注意事项
这里我们看到namespace使用的是com.weasel.mybatis.test.domain.User，即用户类的包路径，这里是一个约定。如果用户不想这样的约定，在继承MybatisOperations类时可以重写getNamespace，自定义namespace。还有个queryPage，因为分页插件中配置了<property name="SQL_REGULAR" value=".*?queryPage.*?"/>，也即当发起带有queryPage字母的，插件都会拦截，为该请求加上分页，这也是一个约定。
因为MybatisOperations中提供了很多通用的方法，但这些方法的sql语句是都没有写的，框架也不知道用户具体的sql语句怎么写，需要用户自己提供。如果用户需要使用所有MybatisOperations提供的方法，应该在mapper文件中定义以下的sql语句，方法和对应的sql语句的id如下:

get(ID id) <---------> getById
get(T entity) <---------> get
insert(T entity) <---------> save
update(T entity) <---------> update
delete(T entity) <---------> delete
query(T entity) <---------> query
queryPage(Page<T> page) <---------> queryPage






