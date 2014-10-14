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
