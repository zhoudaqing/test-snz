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
