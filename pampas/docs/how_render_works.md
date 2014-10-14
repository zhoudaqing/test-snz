# How Pampas render engine works

在 Pampas 中存在两种创建和渲染页面的方式。一种是传统的使用模板语言书写 layout 的方式；另一种是通过装修系统组合前端组件的方式。

不论何种方式，都需要通过 app 中的 `assetsHome` 配置前端资源路径，Pampas 会从此路径下获取模板文件以及 js 、css 等静态资源。

`assetsHome` 支持 4 种协议，分别是：

-   `http` 协议，填写以 `http://` 开头的 url 即可
-   `servlet` 协议，使用 `resource:` 做为前缀，需要填写最开始的 `/` ，例如 `resouce:/f/pulic`
-   `classpath` 协议，使用 `classpath:` 做为前缀，例如 `classpath:/assets`
-   `file` 协议，无需任何前缀，直接填写本地路径即可，例如 `/Users/anson/assets/celebi/public`

## Pampas 如何处理访问请求

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

## 直接渲染

获取请求中的 path 部分并与 `assetsHome` 组装后成为访问 layout 模板文件的路径：`${assetsHome}/views/${path}.hbs` 。

例如请求为 `http://www.haier.com/some/path/to/render` ，`assetsHome` 为 `/Users/anson/assets/celebi/public` 。组装后的模板文件路径为：

`/Users/anson/assets/celebi/public/views/some/path/to/render.hbs`

Pampas 依上述方式获取路径并尝试获取模板，与请求上下文一起进行渲染。

---

在渲染模板中遇到的 `{{inject component/path}}` 也会与 `assetsHome` 组装成组件的模板文件的路径：`${assetsHome}/components/${path}/view.hbs` 。

例如有个 inject helper 为 `{{inject component/path}}` ，`assetsHome` 同上例。则组装后的模板文件路径为：

`/Users/anson/assets/celebi/public/components/component/path/view.hbs`

## 装修渲染

根据 domain 部分获取对应的 site ，例如 app 的 domain 为 `haier.com` ，则访问 domain 为 `www.haier.com` 时，以 `www` 作为 sub-domain 寻找 site ；当访问 domain 为 `haier.com` 时，以 `/` 作为 sub-domain 寻找 site 。

根据 path 从之前获取的 site 中找到对应的装修好的 page ，获取其中存储的模板内容并与请求上下文一起进行渲染。
