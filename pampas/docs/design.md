# Pampas 概要设计文档

## Pampas 目标

- 前后端分离的开发部署方式
- web 和服务分离的开发部署方式
- 简少开发代码量
- web app 虚拟化
- 组件化开发以及基于组件化的所见即所得页面编辑
- 支持上述特性的高性能高可用运行时环境
- 低侵入、低门槛、易用、易扩展
- 中心控制台，提供统一的运维、管理、监控

## 技术选型

- Spring & SpringMVC

  基本上算是不二之选，稳定可靠，用户数量大。

- Handlebars

  模版技术，优势是轻逻辑、易用、易扩展；高效；同时拥有 Java 和 JavaScript 的实现，可统一前后端模版。

- Dubbo

  Alibaba B2B 自用并且开源的 Java 远程调用和服务治理框架，在国内被大量公司使用，稳定高效，自带 console 。

- ZooKeeper

  分布式一致性中间件，用途很多，可以作为配置维护，集群状态检查等等。对于 Dubbo 来说可以作为服务注册中心，对于 Pampas 则可以用于集群管理。

- Redis

  非常快的内存 kv 存储，可持久化，与 memcache 相比优势是支持多种数据结构，非常适用用来存放需要频繁读写的结构数据。

- MySQL(MariaDB)

  关系型数据存储，MariaDB 是由 MySQL 发起人在 Oracle 收购 MySQL 后维护的版本，完全兼容 MySQL 。

## 关键技术点

- 前后端分离，模版的加载方式

  自定义 TemplateLoader ，支持多种协议远程或本地加载模版。

- 组件化开发，前端工程的组织和打包，以及对应的渲染

  开发前端打包工具，以及对应的渲染引擎。打包工具应支持对以细粒度文件夹形式组织的前端资源（模版，脚本，样式）进行连接和压缩；图片拼接；css 、js 预处理器；外部资源引入；实时 watch 等特性。

- 前后端分离的开发过程支持

  开发辅助工具使前端可脱离后端独自运行和开发，完全模拟后端的渲染过程，支持组件接口 mock 和 api mock 。

- 所见即所得的页面编辑器

  为现代浏览器实现，编辑器本身不考虑浏览器兼容性，大量使用 html5 特性，通过 iframe 来隔离编辑器和被装修页面。后端使用 Redis 存储，需要考虑所编辑内容的安全性，进行过滤。

- 组件化开发，web 和服务分离，如何映射组件和远程服务

  为了便于开发，需要解决 dubbo 调用时必须要 client jar 包的问题，打通组件到服务、http api 到服务的调用路径。

### 前端打包工具

通过组件化的方式组织的前端代码过于分散，实际使用时不可能加载那么多分散的文件，同时也无法维护文件之间的依赖关系，因此打包工具需要能够按一定规则将分散的 css 和 js 文件进行拼接。

为了解决大量 js 文件之间的作用域和依赖关系问题，打包工具要能够使用一种模块定义标准自动包装 js 文件。

为了提高开发效率，打包工具应该支持 coffee 、sass 等预处理器，将对应的文件编译转换成目标文件。

为了开发流畅，打包工具应能实时 watch 相应文件的改动被进行对应处理；并能对第三方资源进行获取和管理。

为了网站执行和发布效率，打包工具应该能支持小图片的拼接处理；js 、css 的压缩；为资源文件添加后缀避免不必要的 cache 等特性。

为了方便开发和网站执行效率，打包工具应该支持讲前端 Handlebars 脚本进行预编译，生成 js 代码。可以减少运行时的编译时间以及所需的编译环境。

### 所见即所得的页面编辑器

编辑器应当易于操作，可通过拖放、鼠标点击、键盘快捷键等多种方式操作。

编辑器应在不影响操作的情况下尽量的接近所见即所得的效果，并且提供完全的预览功能。在对页面或者组件进行修改后，应该尽可能的不刷新页面而呈现效果。

### 服务调用体系

要支持 http api 到服务和组件到服务的映射，能够正确的从请求中获取所需的参数并正确的传递给服务。

抽象服务，支持本地调用和远程 dubbo 调用等多种方式。

解决调用 dubbo 必须有 client jar 的问题，否则无法热部署 dubbo 服务。关键需要解决参数和返回值序列化/反序列化的问题。

### console

console 应当能自动发现并管理复数的容器，容器可通过 zk 注册，并暴露 dubbo 接口供 console 调用。

console 作为承载页面编辑器的入口，为支持所见即所得的容器提供页面编辑的功能。

console 应该具有用户和权限体系以保证不同容器和 web app 的管理需求。
