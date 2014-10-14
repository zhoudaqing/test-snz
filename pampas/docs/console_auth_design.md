# console 权限系统设计

用户角色分为：console admin 、app owner 。

console admin 具有的权限：

-   创建和管理 app owner
-   浏览所有 group 的配置信息
-   为 app owner 分配 group 和 app 的权限
-   为 app owner 分配 group 和 app 中的 site 的权限

app owner 具有的权限：

-   当 app owner 拥有某个 group 的权限时，他可以：

    - 在此 group 中创建和删除 app ，修改已有 app 的信息
    - 同时拥有此 group 中所有 app 的权限

-   当 app owner 拥有某个 app 的权限时，他可以：

    - 浏览该 app 的配置信息
    - 为该 app 创建、修改、装修、发布、删除 site

-   当 app owner 只拥有特定 app 中的 特定 site 的权限

    - 浏览该 site 所属 app 的配置信息
    - 装修、发布特定的 site

不提供注册功能，console admin 直接初始化到 DB 中，app owner 由 console admin 创建。

## 表结构

console_users

|field|type|extra|desc
|-|-|-
|id|bigint|primary key, not null|id
|username|varchar|not null|user name
|password|varchar|not null|encrypted password
|nickname|varchar|not null|nick name or real name
|login_at|datetime|null|last login time
|created_at|datetime|not null|create time
|updated_at|datetime|not null|last update time

console_auths

|field|type|extra|desc
|-|-|-
|id|bigint|primary key, not null|id
|user_id|bigint|not null|user id
|role|varchar|not null|role type, can be GROUP,APP,SITE
|value|varchar|not null|role value, depend on role
|created_at|datetime|not null|create time
|updated_at|datetime|not null|last update time

value 字段根据 role 的不同，具有不同的含义：

- role GROUP，value groupName
- role APP，value groupName|appKey1,appKey2
- role SITE，value groupName|appKey|siteName1,siteName2
