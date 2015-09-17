weasel-mybatis<br />
==============<br />
<strong><span style="font-size:32px;">前言</span></strong><br />
<br />
<br />
这是一个用于操作mybatis的包，基于普通项目.该包主要封装了对数据库的一些通用CURD操作和数据库的连接。项目基于mybatis-3.2.1<br />
<br />
<br />
<strong><span style="font-size:32px;">功能</span></strong><br />

<p>
	一 通用CRUD数据库操作。通过MybatisRepository接口和MybatisOperations提供了一些通用的CURD数据库操作。
</p>
<p>
	<br />
	
</p>
<p>
	二 分页插件。能过PagePlugin提供自动分页，目前支持MySQL和PostgreSQL数据库的分页。如果需要更多的数据库支持，用户可以实现Dialect接口来实现。
</p>
<p>
	<br />
	
</p>
三 C3P0数据源。通过C3P0DataSourceFactory类提供C3P0数据源。<br />
<br />
<br />
<strong><span style="font-size:32px;">依赖包</span></strong><br />
<br />
<br />
要连接数据库，首先要将数据库厂商的连接实现依赖到项目中，在这个包中没有依赖该实现包，需要使用者在自己的应用中依赖。<br />
mysql的依赖配置<br />
&lt;dependency&gt;<br />
<span style="white-space:pre">	</span>&lt;groupId&gt;mysql&lt;/groupId&gt;<br />
<span style="white-space:pre">	</span>&lt;artifactId&gt;mysql-connector-java&lt;/artifactId&gt;<br />
<span style="white-space:pre">	</span>&lt;version&gt;5.1.36&lt;/version&gt;<br />
&lt;/dependency&gt;<br />
<br />
postgresql的依赖配置<br />
&lt;dependency&gt;<br />
<span style="white-space:pre">	</span>&lt;groupId&gt;org.postgresql&lt;/groupId&gt;<br />
<span style="white-space:pre">	</span>&lt;artifactId&gt;postgresql&lt;/artifactId&gt;<br />
<span style="white-space:pre">	</span>&lt;version&gt;9.4-1202-jdbc42&lt;/version&gt;<br />
&lt;/dependency&gt;<br />
<br />
<strong><span style="font-size:32px;">配置</span></strong><br />
<br />
<br />
因为该项目是在mybatis之上进行适度的封装，所以mybatis的配置依然适合该项目，更多的mybatis配置可以参考官方文档:http://mybatis.github.io/mybatis-3/zh/index.html<br />
这里给出一个简单的配置供参考，该配置文件在项目中也存在。<br />
<br />
首先定义一个jdbc.properties，用于定义数据库的连接信息:<br />
##jdbc settings<br />
#jdbc.dirverClass=com.mysql.jdbc.Driver<br />
#jdbc.url=jdbc:mysql://127.0.0.1:3306?useUnicode=true&amp;characterEncoding=UTF-8<br />
#jdbc.username=你的用户 名<br />
#jdbc.password=你的密码<br />
<br />
jdbc.dirverClass=org.postgresql.Driver<br />
jdbc.url=jdbc:postgresql://127.0.0.1:5432/test<br />
jdbc.username=你的用户 名<br />
jdbc.password=你的密码<br />
<br />
定义mybatis的配置文件(这里需要命名为mybatis_config.xml)<br />
&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot; ?&gt;<br />
&lt;!DOCTYPE configuration PUBLIC &quot;-//mybatis.org//DTD Config 3.0//EN&quot; &quot;http://mybatis.org/dtd/mybatis-3-config.dtd&quot;&gt;<br />
&lt;configuration&gt;<br />
<span style="white-space:pre">	</span>&lt;properties resource=&quot;jdbc.properties&quot;/&gt; &nbsp;&lt;!--在上面配置的jdbc.properties文件--&gt;<br />
<span style="white-space:pre">	</span>&lt;settings&gt;<br />
<span style="white-space:pre">		</span>&lt;setting name=&quot;cacheEnabled&quot; value=&quot;true&quot;/&gt;<br />
<span style="white-space:pre">		</span>&lt;!-- &lt;setting name=&quot;lazyLoadingEnabled&quot; value=&quot;true&quot;/&gt; --&gt; &lt;!-- 如果这里开方lazyLoad，需要在自己的应用依赖cglib库。似乎在新版本中nybatis默认包含了Javassist，如果想使用cglib要另行配置--&gt;<br />
<span style="white-space:pre">		</span>&lt;setting name=&quot;aggressiveLazyLoading&quot; value=&quot;false&quot;/&gt;<br />
<span style="white-space:pre">	</span>&lt;/settings&gt;<br />
<br />
&nbsp; &nbsp; &nbsp; &nbsp;&lt;typeAliases&gt; &nbsp;&nbsp;<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&lt;typeAlias alias=&quot;User&quot; type=&quot;com.weasel.mybatis.test.domain.User&quot;/&gt; &nbsp; &lt;!--起别名，方便在mapper文件中使用--&gt;<br />
&nbsp; &nbsp; &nbsp; &lt;/typeAliases&gt; &nbsp;<br />
&nbsp; &nbsp;&nbsp;<br />
&nbsp; &nbsp; &nbsp; &lt;plugins&gt;<br />
<span style="white-space:pre">		</span>&lt;plugin interceptor=&quot;com.weasel.mybatis.PagePlugin&quot;&gt; &nbsp;&lt;!--定义分页插件--?<br />
<span style="white-space:pre">			</span>&lt;property name=&quot;SQL_REGULAR&quot; value=&quot;.*?queryPage.*?&quot;/&gt; &nbsp;&lt;!--定义插件会拦截哪些语句--&gt;<br />
<span style="white-space:pre">			</span>&lt;!-- &lt;property name=&quot;DIALECT&quot; value=&quot;com.weasel.mybatis.dialect.impl.MySQLDialect&quot;/&gt; --&gt; &nbsp;&lt;!--mysql分页方言--&gt;<br />
<span style="white-space:pre">			</span>&lt;property name=&quot;DIALECT&quot; value=&quot;com.weasel.mybatis.dialect.impl.PostgreSQLDialect&quot;/&gt; &nbsp;&lt;!--postgresql分页方言--&gt;<br />
<span style="white-space:pre">		</span>&lt;/plugin&gt;<br />
&nbsp; &nbsp; &nbsp;&lt;/plugins&gt;<br />
<span style="white-space:pre">	</span><br />
<span style="white-space:pre">	</span>&lt;environments default=&quot;development&quot;&gt;<br />
<span style="white-space:pre">		</span>&lt;environment id=&quot;development&quot;&gt;<br />
<span style="white-space:pre">			</span>&lt;transactionManager type=&quot;JDBC&quot;/&gt;<br />
<span style="white-space:pre">			</span>&lt;dataSource type=&quot;com.weasel.mybatis.C3P0DataSourceFactory&quot;&gt; &nbsp; &lt;!--自定义的c3po factory--&gt;<br />
<span style="white-space:pre">				</span>&lt;property name=&quot;driverClass&quot; value=&quot;${jdbc.dirverClass}&quot; /&gt;<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; <span style="white-space:pre">	</span>&nbsp; &nbsp; &nbsp; &nbsp;&nbsp;&lt;property name=&quot;jdbcUrl&quot; value=&quot;${jdbc.url}&quot; /&gt;<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; <span style="white-space:pre">		</span>&lt;property name=&quot;user&quot; value=&quot;${jdbc.username}&quot; /&gt;<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; <span style="white-space:pre">		</span>&lt;property name=&quot;password&quot; value=&quot;${jdbc.password}&quot; /&gt;<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<span style="white-space:pre">			</span> &lt;property name=&quot;idleConnectionTestPeriod&quot; value=&quot;60&quot; /&gt;<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<span style="white-space:pre">			</span> &lt;property name=&quot;maxPoolSize&quot; value=&quot;20&quot; /&gt;<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; <span style="white-space:pre">		</span>&lt;property name=&quot;maxIdleTime&quot; value=&quot;1600&quot; /&gt;<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<span style="white-space:pre">			</span> &lt;property name=&quot;testConnectionOnCheckout&quot; value=&quot;true&quot;/&gt;<br />
<span style="white-space:pre">			</span>&lt;/dataSource&gt;<br />
<span style="white-space:pre">		</span>&lt;/environment&gt;<br />
<span style="white-space:pre">	</span>&lt;/environments&gt;<br />
<span style="white-space:pre">	</span><br />
<span style="white-space:pre">	</span>&lt;mappers&gt; &nbsp;&nbsp;<br />
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &lt;mapper resource=&quot;mappers/user.xml&quot; /&gt; &nbsp; &nbsp;&lt;!--用户自定义的mapper文件--&gt;<br />
&nbsp; &nbsp; &nbsp; &nbsp; &lt;/mappers&gt;<br />
<br />
&lt;/configuration&gt;<br />
<br />
<strong><span style="font-size:32px;">如何使用</span></strong><br />
<br />
<br />
首先可以自定自己的一个接口，并且该接口继承MybatisRepository接口<br />
public interface UserRepository extends MybatisRepository&lt;Long, User&gt; {<br />
<br />
<br />
}<br />
<br />
<br />
定义自己的一个实现类，继承MybatisOperations类，并实现自己定义的接口<br />
public class UserRepositoryImpl extends MybatisOperations&lt;Long, User&gt; implements UserRepository {<br />
<br />
<br />
}<br />
这样自定义的类就拥有了连接数据库和一些通用的CRUD操作，这些方法来自MybatisOperations，分别有get(ID id);save(T entity);saveBatch(List&lt;T&gt; entities);insert(T entity);update(T entity);delete(T entity); deleteBatch(List&lt;T&gt; entities);query(T entity); queryPage(Page&lt;T&gt; page);方法。<br />
这里值得注意的是save方法，该方法的默认实现是当entity的id值不为空时，认为用户想做的是更新操作，会调用update方法。当entity的id值为空时，认为用户想做的是插入操作，会调用insert方法。<br />
<br />
<br />
<strong><span style="font-size:32px;">mapper文件的配置</span></strong><br />
<br />
<br />
当然，具体的sql语句该包是不知道的，这个还需要用户自己去定义，sql语句的定义在mapper文件中，具体怎么写mapper文件用户可以参考mybatis的官方文档，下面给一个简单的:<br />
&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot; ?&gt;<br />
&lt;!DOCTYPE mapper PUBLIC &quot;-//mybatis.org//DTD Mapper 3.0//EN&quot; &quot;http://mybatis.org/dtd/mybatis-3-mapper.dtd&quot;&gt;<br />
&lt;mapper namespace=&quot;com.weasel.mybatis.test.domain.User&quot;&gt; &nbsp;<br />
<br />
<span style="white-space:pre">	</span>&lt;resultMap id=&quot;userResultMap&quot; type=&quot;User&quot;&gt;<br />
<span style="white-space:pre">		</span>&lt;id property=&quot;id&quot; column=&quot;id&quot; /&gt;<br />
<span style="white-space:pre">		</span>&lt;result property=&quot;username&quot; column=&quot;username&quot; /&gt;<br />
<span style="white-space:pre">		</span>&lt;result property=&quot;password&quot; column=&quot;password&quot; /&gt;<br />
<span style="white-space:pre">	</span>&lt;/resultMap&gt;<br />
<br />
<span style="white-space:pre">	</span>&lt;select id=&quot;getById&quot; resultType=&quot;User&quot;&gt;<br />
<span style="white-space:pre">		</span>select * from users where id =<br />
<span style="white-space:pre">		</span>#{id}<br />
<span style="white-space:pre">	</span>&lt;/select&gt;<br />
<br />
<span style="white-space:pre">	</span>&lt;insert id=&quot;save&quot; parameterType=&quot;User&quot;&gt;<br />
<span style="white-space:pre">		</span>insert into users<br />
<span style="white-space:pre">		</span>(id,username,password)<br />
<span style="white-space:pre">		</span>values (#{id},#{username},#{password})<br />
<span style="white-space:pre">	</span>&lt;/insert&gt;<br />
<br />
<span style="white-space:pre">	</span>&lt;update id=&quot;update&quot;&gt;<br />
<span style="white-space:pre">		</span>update users set<br />
<span style="white-space:pre">		</span>username = #{username},<br />
<span style="white-space:pre">		</span>password = #{password}<br />
<span style="white-space:pre">		</span>where id = #{id}<br />
<span style="white-space:pre">	</span>&lt;/update&gt;<br />
<br />
<span style="white-space:pre">	</span>&lt;select id=&quot;queryPage&quot; resultMap=&quot;userResultMap&quot;&gt;<br />
<span style="white-space:pre">		</span>select id as id,<br />
<span style="white-space:pre">			</span> &nbsp; username as username,<br />
<span style="white-space:pre">			</span> &nbsp; password as password<br />
<span style="white-space:pre">	</span> &nbsp; &nbsp;from users<br />
<span style="white-space:pre">	</span>&lt;/select&gt;<br />
<br />
&lt;/mapper&gt;<br />
<br />
<br />
<strong><span style="font-size:32px;">mapper文件配置的一些注意事项</span></strong><br />
这里我们看到namespace使用的是com.weasel.mybatis.test.domain.User，即用户类的包路径，这里是一个约定。如果用户不想这样的约定，在继承MybatisOperations类时可以重写getNamespace，自定义namespace。还有个queryPage，因为分页插件中配置了&lt;property name=&quot;SQL_REGULAR&quot; value=&quot;.*?queryPage.*?&quot;/&gt;，也即当发起带有queryPage字母的，插件都会拦截，为该请求加上分页，这也是一个约定。<br />
因为MybatisOperations中提供了很多通用的方法，但这些方法的sql语句是都没有写的，框架也不知道用户具体的sql语句怎么写，需要用户自己提供。如果用户需要使用所有MybatisOperations提供的方法，应该在mapper文件中定义以下的sql语句，方法和对应的sql语句的id如下:<br />
<br />
<br />
get(ID id) &lt;---------&gt; getById<br />
get(T entity) &lt;---------&gt; get<br />
insert(T entity) &lt;---------&gt; save<br />
update(T entity) &lt;---------&gt; update<br />
delete(T entity) &lt;---------&gt; delete<br />
query(T entity) &lt;---------&gt; query<br />
queryPage(Page&lt;T&gt; page) &lt;---------&gt; queryPage<br />
<br />
