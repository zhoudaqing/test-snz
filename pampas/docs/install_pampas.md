# Install Pampas step by step

## maven:

最小依赖：

```xml
<dependency>
    <groupId>io.terminus.pampas</groupId>
    <artifactId>pampas-webc</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

---

当需要注册到 console 上时需要额外依赖 dubbo 以及使用 zookeeper 协议作为 dubbo 注册中心 ：

```xml
<!-- dubbo -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>dubbo</artifactId>
    <version>2.5.3</version>
</dependency>
<!-- dubbo 服务注册中心，需要使用 zookeeper -->
<dependency>
    <groupId>com.github.sgroschupf</groupId>
    <artifactId>zkclient</artifactId>
    <version>0.1</version>
</dependency>
```

---

当需要使用装修功能时，除了必须注册到 console 上（因此要依赖 dubbo 和 zkclient）之外，还需要额外依赖 pampas-design ：

```xml
<!-- 需要装修时引入 -->
<dependency>
    <groupId>io.terminus.pampas</groupId>
    <artifactId>pampas-design</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

**如果不需要装修功能，要记住把 pampas-design 的依赖移除，否则 spring 启动时可能出错**

## SpringMVC 配置

```xml
<!-- beans -->
<context:component-scan base-package="io.terminus.pampas.webc, yours.package"/>
<!-- 打开 annotation driven ，并使 StringHttpMessageConverter 默认使用 UTF-8 编码 -->
<mvc:annotation-driven>
    <mvc:message-converters>
        <beans:bean class="org.springframework.http.converter.StringHttpMessageConverter">
            <beans:constructor-arg value="UTF-8"/>
            <beans:property name="supportedMediaTypes">
                <util:list>
                    <beans:value>text/plain;charset=UTF-8</beans:value>
                    <beans:value>text/html;charset=UTF-8</beans:value>
                </util:list>
            </beans:property>
        </beans:bean>
        <beans:bean class="io.terminus.pampas.webc.converter.JsonMessageConverter"/>
    </mvc:message-converters>
</mvc:annotation-driven>
<!-- interceptors 相关 -->
<mvc:interceptors>
    <beans:bean class="org.springframework.web.servlet.i18n.LocaleChangeInterceptor">
        <beans:property name="paramName" value="lang"/>
    </beans:bean>
    <beans:bean class="io.terminus.pampas.webc.interceptor.AppInterceptor"/>
    <beans:bean class="io.terminus.pampas.webc.interceptor.CookieInterceptor"/>
    <beans:bean class="io.terminus.pampas.webc.interceptor.LoginInterceptor"/>
    <beans:bean class="io.terminus.pampas.webc.interceptor.AuthInterceptor"/>
</mvc:interceptors>
<!-- viewReslover -->
<beans:bean id="viewResolver" class="io.terminus.pampas.webc.resolver.HandlebarsViewResolver">
    <beans:constructor-arg ref="handlebarEngine"/>
    <!-- <beans:property name="cache" value="true"/> 可选是否启用 cache -->
</beans:bean>
<beans:bean class="io.terminus.pampas.webc.resolver.ExceptionResolver">
    <beans:property name="order" value="0"/>
    <!-- 定义默认的异常处理页面，当该异常类型的注册时使用 -->
    <beans:property name="defaultErrorView" value="error"/>
</beans:bean>
<!-- defaultHandler -->
<beans:bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping">
    <beans:property name="defaultHandler" ref="entrance"/>
</beans:bean>
```

## Spring 配置

最简配置：

```xml
<!-- pampas settings -->
<!-- 如果是嵌入式 -->
<bean id="setting" class="io.terminus.pampas.core.Setting">
    <property name="mode" value="IMPLANT"/>
    <property name="implantApp">
        <bean class="io.terminus.pampas.core.model.App">
            <property name="key" value="showcase"/>
            <property name="domain" value="www.showcase.com"/>
            <property name="assetsHome" value="http://localhost:8088/"/>
            <property name="configPath" value="http://localhost:8088/back_config.yaml"/>
        </bean>
    </property>
</bean>
<!-- 如果是中心化 -->
<bean id="setting" class="io.terminus.pampas.core.Setting">
    <property name="mode" value="CENTER"/>
    <property name="rootPath" value="http://localhost:8088/configs"/>
</bean>
<!-- 中心化时需要一个额外的 redis 配置 -->
<alias name="jedisPool" alias="pampasJedisPool"/>

<import resource="classpath*:/pampas/engine-context.xml"/>
```

中心化使用时需要一个 class 为 `redis.clients.jedis.JedisPool` ，name 为 `pampasJedisPool` 的 bean 作为 redis 数据源。

可以用 alias 复用应用自身的 redis 数据源，也可以单独配置。

**[UPDATE: 2014-09-03]**

不论是中心化还是嵌入式，都可以通过 `devMode` 选项控制是否为开发模式：

```xml
<bean id="setting" class="io.terminus.pampas.core.Setting">
    ...
    <property name="devMode" value="${devMode}"/>
    ...
</bean>
```

`devMode` 默认是 `false` 。当其为 `false` 时，对配置文件的重加载频率为 5 分钟，并且模板文件会有 5 分钟的 cache（一些额外的地方也会有 cache ）；而当其为 `true` 时，对配置文件的重加载频率加快到 5 秒，并且绝大多数 cache 会被关闭（包括模板的）。

---

如果需要注册到 console ，需要保证有 dubbo 的环境配置 ：

```xml
<dubbo:application name="console"/>
<dubbo:registry address="zookeeper://localhost:2181"/>
<dubbo:protocol name="dubbo" port="20889" threads="10" heartbeat="100000"/>

<import resource="classpath*:/pampas/register-context.xml"/>
```

需要注意的是，dubbo 只支持注册到 zookeeper ，以及必须使用明确的端口号来发布（无法识别 -1 这样最后会动态生成端口号的值）

**[UPDATE: 2014-06-10]**

支持复数个 `<dubbo:registry ../>` 的配置，使用复数个时每一个应当被指定 id 。
在 `io.terminus.pampas.engine.Setting` 中增加属性 `registryId` 用于指定 pampas 所用的注册中心。

例如：
```xml
<dubbo:application name="showcase"/>
<dubbo:registry id="wtf" protocol="zookeeper" address="localhost:2180"/>
<dubbo:registry id="pampas" protocol="zookeeper" address="localhost:2181"/>
<dubbo:protocol name="dubbo" port="20880" threads="10" heartbeat="100000"/>

<bean id="setting" class="io.terminus.pampas.engine.Setting">
    <property name="mode" value="IMPLANT"/>
    <property name="implantApp">
        <bean class="io.terminus.pampas.engine.model.App">
            <property name="key" value="showcase"/>
            <property name="domain" value="www.showcase.com"/>
            <property name="assetsHome" value="/Users/anson/nuwa/public"/>
            <property name="configPath" value="/Users/anson/nuwa/public/back_config.yaml"/>
        </bean>
    </property>
    <property name="registryId" value="pampas"/> <!-- 在这里指定 -->
</bean>
```

只有被 setting 引用的 registry 需要为 zookeeper 协议，其余 registry 配置随意。

**需要注意的是容器在进行无 jar 调用时暂时还只认识 setting 中指定的 registry 范围内的 dubbo，非此范围内的调不到，待优化。**

---

如果需要装修功能，除了上面的注册到 console 的配置外，还额外需要一个 redis 配置：

```xml
<alias name="jedisPool" alias="pampasJedisPool"/>

<import resource="classpath*:/pampas/design-context.xml"/>
```

需要一个 class 为 `redis.clients.jedis.JedisPool` ，name 为 `pampasJedisPool` 的 bean 作为 redis 数据源。

可以用 alias 复用应用自身的 redis 数据源，也可以单独配置。如果是中心化部署的，则应该已配置了 redis 数据源，而且中心化和装修也必须共用一个数据源。

---

`io.terminus.pampas.core.model.App` 中的配置：

- key : app 名字，随意即可，在一个 redis 范围内不要重复即可（如果是 implant 模式则完全随意）

- domain : 使用何域名来访问此 app

- proxyRoot : [可选] 用于在装修时，console 代理获取此 pampas 资源时的路径，不填则默认为 `http://#{domain}/`

- assetsHome : [可选] 该 app 的前端资源路径，包括 hbs 、js 、css 、front_config.yaml 等，可使用的协议有：

    - `resource:` 前缀，从当前 servlet 容器获取文件，例如 `resource:/f/public` 第一个 `/` 不能省略
    - `http://` 前缀，以 http 协议获取文件
    - `classpath://` 前缀，从当前 classpath 路径下获取文件
    - 无前缀，从本地文件系统路径获取文件

- configPath : [可选] 该 app 的后端服务配置文件路径，可使用的协议同 `assetsHome`

- configJsPath : [可选] 装修时的配置 js 文件路径，不填时默认为 `assets/scripts/config.js`

- desc : [可选] 该 app 的描述信息

因为一个 app 可以单独包括前后端内容之一或者全部包括，所以 assetsHome 和 configPath 都是可选的。
