# Pampas / 潘帕斯草原

Pampas 是一个贯穿于开发期和运行期的框架/容器。它基于 SpringMVC 、Handlebars 、Dubbo 进行了轻量的封装，为应用开发提供统一的渲染引擎，支持前端的模块化开发，所见即所得的页面创建方式，无 jar 的 Dubbo 调用等特性。

# 名词解释

-   SpringMVC：Spring 的 MVC 框架
-   Handlebars：一种轻逻辑的模板技术，同时有 Java 和 JavaScript 的实现，前后端渲染可使用同样的模板
-   Dubbo：Alibaba B2B 开发的 Java 服务治理框架，可支持像本地调用一样调用远程方法
-   Linner：Terminus.io 开发的前端打包工具
-   Pampas：Terminus.io 开发的后端开发框架和运行时容器
-   装修：结合前端组件化，通过所见即所得的方式创建和编辑页面

# 总览

## Pampas 做了什么？

Pampas 集成并扩展了 Handlebars 模板技术，支持的主要特性有：

-   传统 SpringMVC 方式，通过在 controller 层返回 view 路径渲染
-   统一入口，通过访问时的 path 直接获取 view 路径进行渲染
-   前端资源可独立部署，可支持本地文件、http 、servlet 路径、classpath 路径多种协议
-   对 Handlebars 进行增强，提供多个实用的 helper 并提供扩展点
-   为前端模块化开发提供支持
-   基于前端模块化的装修系统支持

Pampas 封装了一套服务调用体系，可以非常简单的将 web 层的请求映射到服务执行：

-   支持 http url 映射到服务
-   支持组件映射到服务
-   支持智能的上下文参数获取和转换
-   支持映射到 spring bean 或者 dubbo
-   支持无 jar 的 dubbo 调用

Pampas 提供了一个中心化的 console ，为运行时提供监控和功能入口：

-   查看应用健康状况（待补充）
-   查看应用配置文件加载状况，配置文件详情以及警告信息
-   提供装修入口

Pampas 制定了一套组件化的开发方式，并进而支持了所见即所得的页面编辑和配置方式，通过一些工具辅助这个过程

-   Linner：支持组件化开发的前端打包器
-   Jigglypuff：fake pampas http server ，支持前期的前端快速开发
-   Eevee：装修系统前端工程，嵌入在 console 中

## Pampas 技术概述

### 结构

Pampas 主体分为下属几个模块

-   pampas-client 供发布服务的业务系统依赖的客户端，用于代理服务访问
-   pampas-common 一些通用 modal 和工具类等，pampas 内部使用
-   pampas-console 控制台，发布为 war 包，可中心化的监视和控制多组 pampas 实例，包含装修系统前端部分
-   pampas-design 与装修相关的所有后端逻辑，使用时需要依赖 redis
-   pampas-design-api 与装修相关服务的 api ，由 pampas-design 依赖
-   pampas-engine 核心容器逻辑
-   pampas-engine-api 核心容器 api 由 pampas-engine 依赖

其中 pampas-client 对于提供服务的业务系统是必选的；pampas-engine 是整个运行时必须的基础；当需要装修功能时可以可选的方式依赖 pampas-design ；pampas-console 则独立部署。

### 使用的中间件

-   zookeeper 作为 dubbo 和 pampas 本身的注册中心使用，pampas-console 通过 zookeeper 来发现运行中的 pampas 实例。
-   redis 作为装修系统的存储使用，只有支持装修功能的 pampas 实例会用到 redis 。
-   mysql 作为 pampas-console 的存储使用，存储 console 的用户和权限关系。（暂不需要，实现中）

上述三者的存储压力都很小。

以下是一个典型的 pampas 系统结构图：

![pampas 系统结构图](./img/pampas_struct.png)

其中 pampas 容器也可以不使用装修进而不依赖 redis ，进而还可以不向 zookeeper 注册而成为一个独立的节点。

这种情况下的 pampas 容器即为一个最小的运行时单元。

### 部署环境

console 作为一个内部非核心应用，可单实例部署（每个 zookeeper 集群一个实例）。

pampas 容器试需要可部署多集群，每集群不限实例数量，推荐至少双实例互为备份。但对于中心化的部署方式，不建议一个集群中实例更多，并托管过多 app ，应当减小粒度，降低风险。

zookeeper 与 dubbo 注册中心共用（三实例以上做集群即可）。

redis 为每一个需要装修的 pampas 容器集群部署一组主备即可，不建议在两组 pampas 容器集群间共享 redis（可能出现同名 app 导致冲突，如一定要共享则应该使用不同的 database No.）。

### 交互逻辑

pampas 容器在启动时，会注册信息到 zookeeper 上，并开放供 console 调用的 dubbo 接口。

console 在启动后，通过 zookeeper 发现所有注册上来的 pampas 容器，并基于 zookeeper 上的元信息获取这些 pampas 容器的 dubbo 调用地址。

console 全部通过 dubbo 接口来完成对 pampas 容器的控制。

如需使用装修，需保证 console 所在机器能通过 http 访问到对应需装修 app 所配置的资源路径。

详细内容请查看附录中的 console 如何工作。

### 数据隔离策略

使用同一组 zookeeper 集群的 pampas 容器集群，应该使用不同的 dubbo application name ，console 使用此名称作为 pampas 容器集群的 group name 。

在同一组 pampas 集群中，每个 app 拥有集群内惟一的 appKey ，redis 中的数据会根据这个 appKey 做隔离，也因此如果多个集群共用一个 redis 则可能导致 appKey 冲突。

# INSTALL

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

## console 部署

pampas console 通过一个 zookeeper 集群作为注册中心来管理 pampas 集群，所有注册到该 zookeeper 集群的 pampas 实例都可以被此 console 进行管理。

部署 console 需要指定一个 zookeeper 地址，该地址可通过系统环境变量 `PAMPAS_CLS_ZK` 来指定。

一种可能的部署方式为：

1.  获取 pampas-console.war 文件，部署到 jetty 中

2.  设置环境变量并启动 jetty 即可

    可以编写如下脚本辅助启动：

    ```sh
    export PAMPAS_CLS_ZK='somehost1:port,somehost2:port'
    jetty start
    ```

    也可直接在 `.bashrc` 等文件中预先设置好 `PAMPAS_CLS_ZK` 的值。

# 配置文件示例

# Config File

## Front Config File

```yaml
components:
  "common/ceiling": # component path
    category: "OFFICIAL" # category: OFFICIAL, TEMPLATE, SHOP, ITEM, ITEM_TEMPLATE
    name: "吊顶"
    service: "showcase:cookieService" # app:serviceKey
    desc: "页面吊顶" # 可选

mappings:
  - pattern: "api/test_service/{param1}/{param2}" # like SpringMVC
    methods: # methods 多选: ALL, GET, POST, HEAD, OPTIONS, PUT, PATCH, DELETE, TRACE
      - ALL
    service: "showcase:testService2" # app:serviceKey
    desc: "一个 ajax API" # 可选

auths:
  protectedList:
    - pattern: "api/buyer/.*" # 正则表达式
      roles:
        - ADMIN_ROLE
        - BUYER_ROLE
  whiteList:
    - pattern: "login" # 正则表达式
      methods: # methods 多选: ALL, GET, POST, HEAD, OPTIONS, PUT, PATCH, DELETE, TRACE
        - ALL

render: # 装修相关设置，可选，不设置则认为全部直接渲染
  prefixs: # startsWith ，跳过装修直接渲染的 url 路径
    - "index"
  eeveeLayout: "eevee/wysiwyg" # 装修的 layout

hrefs: # 可选，所有出现在这个 hash 里的值都会以 _HREF_ 为 key 注入渲染上下文
  href1: "http://www.baidu.com"
```

## Back Config File

```yaml
services:
  "testService":
    uri: "com.haier.showcase.TestService:method" # className:methodName
    desc: "我是一个不知道干啥用的 service" # 可选
  "testService2":
    type: SPRING # type，默认 DUBBO ，可选：DUBBO, SPRING
    uri: "com.haier.showcase.TestService:method2"
```

# 附录

## 方法映射

pampas 提供一套机制来将 web 开发中的各种请求映射到后端的 spring bean 或者 dubbo 服务上。这个过程遵循一定的约定和约束。

### 对需要被 method mapping 调用的方法进行注解

使用 annotation `io.terminus.pampas.client.Export` 对需要被 method mapping 调用的方法进行注解。并使用 paramNames 参数依须列出方法中所有参数需要被注入的 key 。

例如：

```java
@Export(paramNames = {"group", "app"})
public List<Site> listSites(String group, String app) {
  // some code here
}
```

上述注解表示 `listSites` 方法的第一个参数的 key 为 `group` ，第二个参数的 key 为 `app`。

所有的基本类型参数都需要此 paramName 作为注入的依据，而复杂类型则试情况而定。

-   所有基本类型，即 `java.lang.Integer` 、`java.lang.Long` 等等

    从上下文中按 key 获取并尽可能的转型后注入。

-   参数类型继承自 `io.terminus.pampas.common.BaseUser`

    会尝试注入当前登录用户。

-   参数类型为 `io.terminus.pampas.common.InnerCookie`

    会注入 InnerCookie 对象，对 cookie 的读取和修改进行了包装。

-   参数类型继承自 `java.util.Map`

    会将整个上下文中的所有基本类型组装成 map 作为整体注入。

-   参数为非基本类型又非上述类型的所有其它类型

    首先尝试从上下文中按 paramNames 所注解的 key 寻找字符串类型的变量，如果找到则视为 json 进行 json 2 object 后注入。
    如未找到则使用整个上下文进行 map 2 object 后注入。

### 调用上下文

调用上下文来自 web 请求中所携带的参数以及 inject helper 中内嵌的 json 配置。

例如一个 url 请求 `http://localhost:8080/index?a=1&b=2` 会在上下文中出现 `{"a": 1, "b": 2}` 这两个参数。

---

又如果在这个 url 对应渲染的 hbs 中有一个 inject helper 如下：

```
{{#inject 'common/nav_bar'}}
{
  "b": 1,
  "c": 2
}
{{/inject}}
```

则在执行此 inject 对应的组件以及组件内部时，上下文被 merge 后成为 `{"a": 1, "b": 1, "c": 2}` 的形式。

而在此 inject 外部的其余地方，上下文仍然是 `{"a": 1, "b": 2}` 。

---

如果请求来自于 front_config.yaml 中的 mappings 配置，则上下文还可能来自于 path 中的匹配。

例如一个 mapping 的 pattern 配置为：

`api/design/group/{group}/app/{app}/site`

对应请求的 url 为：

`api/design/group/console/app/demo/site`

则会在上下文中置入 `{"group": "console", "app": "demo"}` 。

### 特殊操作

-   在参数中注入 `InnerCookie` 以便进行 cookie 的获取和操作

    使用 innerCookie 的 set 方法组和 remove 方法组操作 cookie 。所有的操作都会被记住并在请求完成后自动执行，只需要简单调用 set 和 remove 就可以了。

-   返回类型为 `io.terminus.pampas.client.action.LoginAction` 的对象以实现 pampas 默认的登录逻辑

    如果使用 pampas 自带的 `io.terminus.pampas.webc.interceptor.LoginInterceptor` 进行登录拦截的话，则通过使 mapping method 返回 `LoginAction` 实例来完成登录动作。

    如以下伪代码所示：

    ```java
    public LoginAction login(String userName, String password) {
      if (checkPassword(userName, password)) {
        BaseUser user = getUserByName(userName);
        return new LoginAction(user.getId(), null);
      } else {
        return new LoginAction(null, "password not match");
      }
    }
    ```

## 开发 step by step

在 Pampas 的开发过程中，前后端是分离的。下面我们以开发一个显示当前时间的组件为例，进行描述。

### 准备

首先你需要一个可运行的 Pampas ，无论是与你的应用嵌入在一起，或者是一个独立的实例。

请查看[安装和配置 Pampas ](install_pampas.md)。

假设我们开发的 app 名字是 demo ，domain 配置为 `www.demo.com` 。

### 定义服务

我们想要使一个组件能显示当前时间，同时我们还想和查看的人打个招呼，因此我们需要一个下面所写的服务：

首先定义一个返回值类型：

```java
public class Hello implements Serializable {
  private static final long serialVersionUID = 6605245210822077562L;

  private String name;
  private Date date;

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  public Date getDate() {
    return date;
  }
  public void setDate(Date date) {
    this.date = date;
  }
}
```

需要注意如果此返回类型用于 dubbo 接口，应当实现 `Serializable` 接口。

接着定义如下服务：

```java
package some.example.package

public interface ExampleService {
  @Export(paramNames = {"name"})
  Hello sayHello(String name);
}

public class ExampleServiceImpl {
  public Hello sayHello(String name) {
    Hello hello = new Hello();
    hello.setName(name.toUpperCase());
    hello.setDate(new Date());
  }
}
```

为了方法可直接被组件映射到，方法上使用 `io.terminus.pampas.client.Export` 进行了注解并填写了参数名信息。需要注意的是注解应该尽量注解到 interface 上。

关于方法映射的详细信息请参考[这里](method_mapping.md)。

### 注册服务

接下来，我们需要将这个服务进行注册。服务有两种形式，一种是远端 dubbo 的，另一种是本地 spring 的。

spring 形式的服务没有什么特殊，只需要通过 annotation scan 或者显示配置的方式进入 spring 上下文即可。而远端 dubbo 服务则需要在服务提供方的工程里嵌入一个代理。

#### dubbo 服务代理配置

代理实现由 Pampas 提供，引入如下 jar 包（maven 配置）:

```xml
<dependency>
    <groupId>io.terminus.pampas</groupId>
    <artifactId>pampas-client</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

配置 agent 为一个 dubbo service ：

```xml
<bean id="agentImpl" class="io.terminus.pampas.client.AgentImpl"/>
<dubbo:service interface="io.terminus.pampas.client.Agent" ref="agentImpl" version="application-name"/>
```

需要注意 agent 的 version ，应当为每一组应用配置惟一的名字，推荐使用和 dubbo applicaton 一样的配置：

```xml
<dubbo:application name="application-name"/>
```

#### 注册到 Pampas 配置文件中

在 `back_config.yaml` 文件中写入如下内容：

```yaml
services:
  "sayHello": # 服务的 key
    app: "application-name" # 这个 app 是 dubbo 服务发布者的 agent 的 version ，并不是 app 本身的 name（demo）。如果是自己发布的可不填，spring 服务也不填。
    type: DUBBO # 如果是 spring 就写 SPRING（全大写），默认值为 DUBBO ，可不填
    uri: "some.example.package.ExampleService:sayHello" # 类全路径:方法名
```

现在服务已经准备好了，是时候转战前端工程了。

### 编写组件

前端工程使用 linner 打包并且遵循一定的结构，[详情见此](front_project_struct.md)。

组件通常被放置在 `app/components` 文件夹下，我们为这个组件新建文件夹和模板文件：

```bash
mkdir app/components/say_hello
touch app/components/say_hello/view.hbs
```

暂时我们还不需要 js 和 css ，因为没有建立 `view.css | view.scss` 和 `view.js | view.coffee` 文件。

这个组件的内容可以是这样：

```html
{{#component "say-hello"}}
<h2>Hi, {{_DATA_.name}}</h2>
<p>Now is {{formatDate _DATA_.date}}</p>
{{/component}}
```

其中 `{{component ...}}` 和 `{{formatDate ...}}` 都是 Pampas 内置的 helper 。前者用来生成一个组件的外围包装 div ，后者则将 `java.util.Date` 类型格式化成字符串。

更多和更详细的 helper 说明请[查看这里](handlebars_helpers.md)。

### 注册组件

在 `front_config.yaml` 中添加如下内容：

```yaml
components:
  "say_hello": # 组件的基于 app/components 的 path
    category: OFFICIAL # 组件分类，和装修时是否可见有关，默认为 ADMIN
    name: "打招呼" # 组件名
    desc: "我会打招呼并且显示当前时间" # 描述信息，非必填
    service: "demo:sayHello" # 格式为：服务注册的 app 名字:服务的 key 。在此例中服务注册在 app 自己的 back_config.yaml 中，因此 app 就是 demo 。
```

可以看到其中的 service 配置将组件和服务进行了关联。

### 创建 layout

接下来我们创建一个 layout 以便把组件显示出来，到 `app/views` 目录下新建文件 `test.hbs` ，内容如下：

```html
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Pampas demo</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
  </head>
  <body>
    {{inject "say_hello"}}
  </body>
</html>
```

我们通过 `{{inject ...}}` helper 将组件放置到需要的位置，后面的参数代表了组件的 path 。

### 访问组件和传参

如果不出意外，当我们访问 `http://www.demo.com/test?name=Anson` 时，应该就可以看到渲染结果，渲染出来的 html 应该是这样的：

```html
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Pampas demo</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
  </head>
  <body>
    <div class="say-hello" data-comp-path="say_hello">
      <h2>Hi, Anson</h2>
      <p>Now is 2014-06-18 19:45:48</p>
    </div>
  </body>
</html>
```

单独看组件的渲染结果

```html
<div class="say-hello" data-comp-path="say_hello">
  <h2>Hi, ANSON</h2>
  <p>Now is 2014-06-18 19:45:48</p>
</div>
```

我们还可以通过在 `{{inject ...}}` 标签内构建 json 来向组件传递参数，例如：

```html
{{#inject "say_hello"}}
{
  "name": "Anson"
}
{{/inject}}
```

### 通过 url 调用服务

服务除了可以映射到组件外，还可以映射到 url 上，尝试在 `front_config.yaml` 中添加如下内容：

```yaml
mappings:
  - pattern: "/api/demo/say_hello/{name}"
    methods:
      - GET
    service: demo:sayHello
```

等待配置生效（devMode 为 true 时 5 秒，否则为 5 分钟）后在浏览器中访问 `http://www.demo.com/api/demo/say_hello/Anson` 可获得如下返回：

```json
{
  "name": "ANSON",
  "date": 1403101932423
}
```

### 为组件加入 js 和 css

现在让我们给组件加入一些样式和行为，首先我们创建一个样式文件 `app/components/say_hello/view.scss` ，然后添加一些样式：

```scss
.say-hello {
  h2 {
    border-bottom: 2px solid #666;
  }
  p {
    text-shadow: 1px 1px rgba(black, 0.2);
  }
}
```

对应的 css 为：

```css
.say-hello h2 {
  border-bottom: 2px solid #666;
}
.say-hello p {
  text-shadow: 1px 1px rgba(0, 0, 0, 0.2);
}
```

因为 css 不能像 js 那样使用 CMD 的方式包装隔离，所以我们选择在组件的 css 最外层包上组件的惟一 class 做隔离。这样 `say_hello` 组件中 `h2` 和 `p` 的样式就不会影响到别的组件了。

接着让我们添加一些 js ，创建文件 `app/components/say_hello/view.coffee` ，然后添加如下代码：

```coffee
class SayHello
  constructor: ($) ->
    @$p = $("p")
    @bindEvents()

  bindEvents: ->
    @$p.hover ->
      $(@).css("background-color", "#eee")
    , ->
      $(@).css("background-color", "transparent")

module.exports = SayHello
```

对应的 js 为：

```js
var SayHello;

SayHello = (function() {
  function SayHello($) {
    this.$p = $("p");
    this.bindEvents();
  }

  SayHello.prototype.bindEvents = function() {
    return this.$p.hover(function() {
      return $(this).css("background-color", "#eee");
    }, function() {
      return $(this).css("background-color", "transparent");
    });
  };

  return SayHello;

})();

module.exports = SayHello;
```

我们给组件的 `p` 标签增加了一个 hover 效果。需要注意的是这里的写法：

-   我们创建了一个类，通过 coffee 的 class 语法，当然你也可以通过 js 原生的使用 prototype 的方式

-   在类的构造方法中我们传入了一个 `$` ，这个 `$` 的作用是在该组件自身范围内选择元素，`$("p")` 可认为等同于 `jQuery.find("p", compDom)`

-   因为在构造方法中 `$` 被覆盖，为了避免歧义和不自觉的错误，建议在构造方法中只进行元素选择，别的动作都放到别的方法中进行，所以这里将事件绑定单独拿到了 `bindEvents` 方法中

-   组件类在执行中会被注入一个 `this.$el` 指向自身 dom 的 jQuery 对象，因此不论是在构造方法还是其余别的方法中，都可以使用 `this.$el.find("something")` 去选取元素或者用 `this.$el` 选取自身

-   最后一定要记得使用 `module.exports = SayHello` 将组件类导出

现在我们组件的 p 元素就带上了 hover 效果了。

## 前端工程结构

### 目录结构

一个典型的 linner 管理的可被 Pampas 使用的前端工程结构如下：

```bash
.
├── app
│   ├── components
│   │   └── dropdown
│   │       ├── templates
│   │       ├── view.coffee
│   │       ├── view.hbs
│   │       └── view.scss
│   ├── images
│   │   └── logo.png
│   ├── scripts
│   │   └── app.coffee
│   ├── styles
│   │   └── app.scss
│   ├── templates
│   │   └── welcome.hbs
│   ├── views
│   │   └── index.html
│   └── files
├── bin
│   └── server
├── config.yml
├── public
├── test
└── vendor
```

`app` 文件夹是用户自己编写代码的地方
  * `images` 用以存放项目相关的图片文件
  * `scripts` 用以存放项目相关的 JavaScript 文件
  * `styles` 用以存放项目相关的 Stylesheet 文件
  * `templates` 用以存放项目相关的前端模板文件
  * `views` 用以存放项目相关的后端模板文件
  * `components` 用以存放项目的组件文件
  * `files` 用来存放例如 `front_config.yaml` 这样的文件

`config.yml` 是整个项目的配置文件

`bin` 文件夹可以让用户很方便的启动一个本地的服务器，以当前文件夹作为根，当然更好的选择是使用 jigglypuff 来启动一个带渲染逻辑的服务器

`test` 文件夹可以使用户编写一些单元测试来测试自己的前端项目

`vendor` 文件夹可以使用户引入第三方的代码组件，如 jQuery、Underscore 等

`public` 文件夹是项目打包后文件位置，发布项目所需要的所有文件

其中组件 dropdown 下的三个文件是约定好的：

- `view.hbs` 组件模板
- `view.scss | view.css` 组件 css
- `view.coffee | view.js` 组件 js
- `templates` 文件夹可用来放置组件自身的前端模板
- `config.scss | config.css` 是可选的，用于组件装修时的特殊样式
- `config.coffee | config.js` 也是可选的，用于组件装修时的配置

### 配置文件

一个典型的 `config.yml` 可能是这样的：

```yaml
paths:
  public: "public"
groups:
  scripts:
    concat:
      "/assets/scripts/app.js": "app/{scripts,components}/**/*.{js,coffee}"
      "/assets/scripts/vendor.js": "vendor/**/*.{js,coffee}"
    order:
      - "vendor/pokeball.js"
      - "..."
      - "app/scripts/app.coffee"
  styles:
    concat:
      "/assets/styles/vendor.css": "vendor/**/*.{css,scss,sass}"
      "/assets/styles/app.css": "{app/styles/app.{css,scss,sass},app/styles/icons.scss,app/components/**/view.{css,scss,sass}}"
    order:
      - "vendor/pokeball.css"
      - "vendor/base.css"
      - "..."
  images:
    sprite:
      "../app/styles/icons.scss": "app/images/**/*.png"
  views:
    paths:
      - app/views
    copy:
      "/views": "app/views/**/*.hbs"
      "/components/": "app/components/**/view.hbs"
  templates:
    paths:
      - "app/components"
    precompile:
      "../vendor/templates.js": "app/components/**/templates/*.hbs"
  files:
    paths:
      - "app/files"
    copy:
      "/": "app/files/*"
modules:
  wrapper: "cmd"
  ignored: "vendor/**/*"
  definition: "/scripts/app.js"
sprites:
  selector: ".icon-"
  path: "/assets/images/"
  url: "/assets/images/"
revision:
  - /views/layout.hbs
notification: true
bundles:
  "pokeball.js":
    version: "master"
    url: "http://git.terminus.io:9099/terminus/pokeball/raw/master/public/pokeball.js"
  "greatball.css":
    version: "master"
    url: "http://git.terminus.io:9099/terminus/pokeball/raw/master/public/greatball.css"
  "../app/styles/pokeball/_variables.scss":
    version: master
    url: http://git.terminus.io:9099/terminus/pokeball/raw/master/src/styles/greatball/_variables.scss
```

## console 如何工作

pampas console 负责管理所有的 pampas 容器实例，从最前面 console 的页面到 pampas 容器后面的 redis ，整个交互路径较长，因此在这里进行一个详细的描述。

以下用 console 指代 pampas console ，用 pampas 指代 pampas 容器。

### console 和 pampas 的交互

两种方式，一种是 dubbo 调用，还有一种是 http 调用。

主要的操作都走 dubbo ，http 调用是为了解决装修时前端资源的跨域问题。

### 一切都是怎么发生的

1.  当 pampas 配置了需要注册后，在 pampas 启动时：

    - 会获取 spring 配置中的 `<dubbo:registry ../>` 中的 zookeeper 地址作为注册中心地址

      当有多个 `<dubbo:registry ../>` 的配置时，通过 `setting` 配置中的 `registryId` 匹配一个

    - 会获取 spring 配置中的 `<dubbo:application ../>` 中的 name 作为 group name

    - 会向之前获取到的 dubbo registry 上注册服务 `io.terminus.pampas.engine.service.ConfigService` ，服务的 version 为 group name

    - 如果配置了支持装修，则还会向 dubbo registry 上注册多个服务，`io.terminus.pampas.design.service.DesignMetaService` ，`io.terminus.pampas.design.service.SiteService` ，`io.terminus.pampas.design.service.SiteInstanceService` ，`io.terminus.pampas.design.service.PageService` 。这些服务的 version 同样也为 group name 。

2.  当 console 启动时：

    - 会获取环境变量 `PAMPAS_CLS_ZK` 的值作为 zookeeper 注册中心的地址

    - 从 zookeeper 中获取到所有 pampas 的 group 和 cell 信息，作为调用 pampas dubbo 服务的调用信息

    - 下属没有 cell 信息的 group 会被隐藏

    - 根据选中的 group 和 cell 的不同，构造 dubbo reference 向 pampas 发起调用

      其中 group 为 dubbo 服务的 version ，cell 则为点对点直连时的 IP

3.  装修时发生了什么：

    因为装修器是在 console 之中的，而被装修的页面存在于 pampas 中，因此会有跨域的问题，console 对跨域资源在后端做了代理。

    装修器使用一个 iframe 嵌入被装修的页面，在获取被装修页面的 js 和 css 等资源时，由 console 后端代理进行 http 调用，因此需要保证 console 所在机器能通过 http 正确访问到被装修 app 。

    - console 会从被装修的 app 中获取 `domain`（通过调用对应 group 的 `ConfigService`），连接 http 协议后作为代理访问地址

      如果被装修的 app 的 domain 配置为 `690.haier.cn` 则对于资源文件 `/assets/scripts/app.js` console 会通过此路径尝试获取：`http://690.haier.cn/assets/scripts/app.js`

    - 如果使用 domain 访问有困难，也可通过为 app 配置 `proxyRoot` 属性指定代理访问路径，但这仅限于嵌入式的 pampas（因为中心化 pampas 自身就是靠 domain 来区分 app 的）

      `proxyRoot` 属性需要被填写完整，例如一个 pampas 被部署于本机 jetty 的 690 path 下，则应该填写为 `http://localhost:8080/690/` 。最后的一个 `/` 是必须的。

      如果不是情况真的特殊，还是建议全部使用 domain 的配置来进行代理访问。

## 渲染引擎如何工作

在 Pampas 中存在两种创建和渲染页面的方式。一种是传统的使用模板语言书写 layout 的方式；另一种是通过装修系统组合前端组件的方式。

不论何种方式，都需要通过 app 中的 `assetsHome` 配置前端资源路径，Pampas 会从此路径下获取模板文件以及 js 、css 等静态资源。

`assetsHome` 支持 4 种协议，分别是：

-   `http` 协议，填写以 `http://` 开头的 url 即可
-   `servlet` 协议，使用 `resource:` 做为前缀，需要填写最开始的 `/` ，例如 `resouce:/f/pulic`
-   `classpath` 协议，使用 `classpath:` 做为前缀，例如 `classpath:/assets`
-   `file` 协议，无需任何前缀，直接填写本地路径即可，例如 `/Users/anson/assets/celebi/public`

### Pampas 如何处理访问请求

Pampas 默认的页面渲染逻辑处于最低优先级，在此之前会有三种情况将请求拦截处理，优先级从高到低排列：

-   有自定义的 SpringMVC 的 RequestMapping 成功匹配

    由 SpringMVC 正常执行匹配方法

-   url 的 path 部分中带有点号（`.`）

    将会作为静态资源处理，直接从 `assetsHome` 中寻找对应文件并返回

-   url 的 path 部分匹配到 `front_config.yaml` 中的某个 mapping 配置

    匹配到对应的 service 处理，并将结果转为 json 后返回

当这三种情况都未匹配成功时，进入 pampas 的页面渲染逻辑。

大致处理逻辑是这样的：

1.  获取此次请求对应的 app

    如果是嵌入式的 pampas ，则只有一个 app ；如果是中心化的 pampas ，则通过匹配 app 的 domain 和请求中的 domain 找出对应的 app

2.  从对应 app 的 `front_config.yaml` 中读取 render 配置

    当无 render 配置时，或者当请求 path 能够被 render 中的 prefixs 匹配时，则进入直接渲染路径；否则进入装修渲染路径

### 直接渲染

获取请求中的 path 部分并与 `assetsHome` 组装后成为访问 layout 模板文件的路径：`${assetsHome}/views/${path}.hbs` 。

例如请求为 `http://www.haier.com/some/path/to/render` ，`assetsHome` 为 `/Users/anson/assets/celebi/public` 。组装后的模板文件路径为：

`/Users/anson/assets/celebi/public/views/some/path/to/render.hbs`

Pampas 依上述方式获取路径并尝试获取模板，与请求上下文一起进行渲染。

---

在渲染模板中遇到的 `{{inject component/path}}` 也会与 `assetsHome` 组装成组件的模板文件的路径：`${assetsHome}/components/${path}/view.hbs` 。

例如有个 inject helper 为 `{{inject component/path}}` ，`assetsHome` 同上例。则组装后的模板文件路径为：

`/Users/anson/assets/celebi/public/components/component/path/view.hbs`

### 装修渲染

根据 domain 部分获取对应的 site ，例如 app 的 domain 为 `haier.com` ，则访问 domain 为 `www.haier.com` 时，以 `www` 作为 sub-domain 寻找 site ；当访问 domain 为 `haier.com` 时，以 `/` 作为 sub-domain 寻找 site 。

根据 path 从之前获取的 site 中找到对应的装修好的 page ，获取其中存储的模板内容并与请求上下文一起进行渲染。
