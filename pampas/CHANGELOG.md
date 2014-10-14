# 1.0-SNAPSHOT

## 2014-08-09

-   **[*] 配置方式修改为读取用户目录下的 `pampas-console.properties` 文件**

    不再使用环境变量 `PAMPAS_CLS_ZK` 来配置 zookeeper 地址，改为在当前用户 home 目录下放置文件 `pampas-console.properties` 来进行配置。

    文件内容视需要配置：

    ```
    zk=localhost:2181
    ```

-   **[+] 权限相关功能添加，包括用户和权限的 CRUD ，登陆登出，界面和接口的权限控制**

    权限相关信息存储于 MySQL 中，因此需要配置 MySQL 。

    在 `pampas-console.properties` 文件中添加如下三项配置（具体值请视需要配置）：

    ```
    jdbcUrl=jdbc:mysql://localhost:3306/pampas
    jdbcUsername=root
    jdbcPassword=root
    ```

    并新建两个表：

    ```sql
    CREATE TABLE `users` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,​
    `name` varchar(64) NOT NULL COMMENT '用户登陆名称',​
    `password` varchar(127) DEFAULT NULL COMMENT '密码',​
    `status` smallint(6) DEFAULT NULL COMMENT '用户状态 ，1：正常，，-1：禁用',​
    `email` varchar(32) DEFAULT NULL COMMENT '邮箱',​
    `real_name` varchar(32) DEFAULT NULL COMMENT '真实姓名',​
    `login_at` datetime DEFAULT NULL COMMENT '上次登陆日期',​
    `created_at` datetime DEFAULT NULL COMMENT '创建日期',​
    `updated_at` datetime DEFAULT NULL COMMENT '更新日期',​
    PRIMARY KEY (`id`),​
    UNIQUE KEY `idx_users_name_UNIQUE` (`name`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    CREATE TABLE `user_permissions` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,​
    `user_id` bigint(20) NOT NULL COMMENT '用户id',​
    `permissions` varchar(4096) DEFAULT NULL COMMENT '以json形式存储的授权列表',​
    `created_at` datetime DEFAULT NULL COMMENT '创建日期',​
    `updated_at` datetime DEFAULT NULL COMMENT '更新日期',​
    PRIMARY KEY (`id`),​
    UNIQUE KEY `idx_ups_user_id_UNIQUE` (`user_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
    ```

    初始化 admin 账号，密码 anywhere ：

    ```sql
    INSERT INTO `users` (`id`,​ `name`,​ `password`,​ `status`,​ `email`,​ `real_name`,​ `login_at`,​ `created_at`,​ `updated_at`)
    VALUES
    (1,​'admin',​'8b4d@baf3a867dfdd2167780a',​1,​'admin@pampas.console',​'管理员',null,now(),now());

    INSERT INTO `user_permissions` (`id`,​ `user_id`,​ `permissions`,​ `created_at`,​ `updated_at`)
    VALUES
    (1,​1,​'[\"admin\"]',now(),now());
    ```

## 2014-08-03

-   [+] 完成中心化时多域名的支持，移除 app 中的 `extraDomains` 属性

    嵌入式时不需要额外进行多域名的配置，中心化则需要在 console 中管理 app 的地方进行额外域名的配置。

## 2014-07-27

-   [+] 增加嵌入式时对 app 多域名的支持，中心化时的支持待添加

    ~~为 app 增加一个类型为 `List<String>` 的属性 `extraDomains` ，不过其实嵌入式时 `domain` 和 `extraDomains` 的配置都不是必须的。~~

-   [*] 修复中心化时 app 中某些值的默认值不生效的问题

## 2014-07-23

-   [*] 修复调用 dubbo 服务，方法有原始类型参数时会出错的问题

## 2014-07-06

-   [*] 修复装修时当被装修应用的静态资源经过 gzip 后乱码的问题

## 2014-06-27

-   [+] 支持在调用服务时将 json 反序列化为带范型的类型实例

    例如，`List<Model>` 这样的类型，以及 `List<Map<String, List<SomeClass>>>` 这样嵌套数层的也可以。

-   [*] 修复调用服务时，将整个上下文注入 `Map` 时报错的问题

## 2014-06-22

-   [+] 为 console 增加 app 的管理功能

    对于嵌入式的 group ，只能显示惟一的 app 的信息；对于中心化的 group ，可以进行全面的增删改查。

-   [*] 升级 semantic 为 `0.16.1`

## 2014-06-14

-   [+] 增加打包 console 用的 bash 脚本

-   [*] 修复 `HandlebarViewResolver` 中路径前会额外添加一个 `/` 的问题

-   [+] console 的站点列表页面现在会额外显示一些 app 信息，并且增加了直接访问子站的链接

## 2014-06-13

-   [*] 修复当 `devMode` 为 `false` 时，组件模板获取不到的问题

## 2014-06-12

-   **[+] `io.terminus.pampas.engine.Setting` 中增加属性 `devMode` 用于指定是否为开发模式，默认为 `false`**

    当设置 `devMode` 为 `true` 时，对配置文件的 reload 动作会加快到每 5 秒一次，同时会关闭绝大多数 cache 。

    可通过配置 `<property name="devMode" value="#{app.mode == 'dev'}"/>` 兼容现有配置。

## 2014-06-11

-   [*] `front_config.yaml` 中 `render.prefixs` 的配置现在对大小写不敏感

-   [*] `/api/design/components/render` API 修改为 POST 方式，避免出现需要渲染的组件模板长度太长时报错

-   [*] pampas-console 中装修器部分的前端代码更新为最新版本，修复了一些小问题

## 2014-06-10

-   [+] 容器支持复数个 `<dubbo:registry ../>` 的配置

    支持复数个 `<dubbo:registry ../>` 的配置，在使用复数个配置时每一个应当被指定 id 。

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
        <property name="registryId" value="pampas"/>
    </bean>
    ```

    只有被 setting 引用的 registry 需要为 zookeeper 协议，其余 registry 配置随意。

    当只有单个 registry 时仍然像以前一样无需任何配置。

    **需要注意的是容器在进行无 jar 调用时暂时还只认识 setting 中指定的 registry 范围内的 dubbo，非此范围内的调不到，待优化。**
