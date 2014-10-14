pampas console 通过一个 zookeeper 集群作为注册中心来管理 pampas 集群，所有注册到该 zookeeper 集群的 pampas 实例都可以被此 console 进行管理。

pampas console 需要一个 mysql 作为权限和用户的存储。

一种可能的部署方式为：

1.  初始化 mysql 的表和数据

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

    INSERT INTO `users` (`id`,​ `name`,​ `password`,​ `status`,​ `email`,​ `real_name`,​ `login_at`,​ `created_at`,​ `updated_at`)
    VALUES
    (1,​'admin',​'8b4d@baf3a867dfdd2167780a',​1,​'admin@pampas.console',​'管理员',null,now(),now());

    INSERT INTO `user_permissions` (`id`,​ `user_id`,​ `permissions`,​ `created_at`,​ `updated_at`)
    VALUES
    (1,​1,​'[\"admin\"]',now(),now());
    ```

    初始化的数据带有一个 admin 权限的账号，用户名密码为：admin/anywhere 。

2.  获取 pampas-console.war 文件，部署到 jetty 中

3.  创建配置文件并启动 jetty

    在当前用户的 home 目录下创建配置文件 `pampas-console.properties` ，并写入需要的配置：

    ```
    zk=localhost:2181
    jdbcUrl=jdbc:mysql://localhost:3306/pampas
    jdbcUsername=root
    jdbcPassword=root
    ```
