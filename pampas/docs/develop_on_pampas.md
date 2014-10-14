# Develop on Pampas step by step

在 Pampas 的开发过程中，前后端是分离的。下面我们以开发一个显示当前时间的组件为例，进行描述。

## 准备

首先你需要一个可运行的 Pampas ，无论是与你的应用嵌入在一起，或者是一个独立的实例。

请查看[安装和配置 Pampas ](install_pampas.md)。

假设我们开发的 app 名字是 demo ，domain 配置为 `www.demo.com` 。

## 定义服务

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

## 注册服务

接下来，我们需要将这个服务进行注册。服务有两种形式，一种是远端 dubbo 的，另一种是本地 spring 的。

spring 形式的服务没有什么特殊，只需要通过 annotation scan 或者显示配置的方式进入 spring 上下文即可。而远端 dubbo 服务则需要在服务提供方的工程里嵌入一个代理。

### dubbo 服务代理配置

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

### 注册到 Pampas 配置文件中

在 `back_config.yaml` 文件中写入如下内容：

```yaml
services:
  "sayHello": # 服务的 key
    app: "application-name" # 这个 app 是 dubbo 服务发布者的 agent 的 version ，并不是 app 本身的 name（demo）。如果是自己发布的可不填，spring 服务也不填。
    type: DUBBO # 如果是 spring 就写 SPRING（全大写），默认值为 DUBBO ，可不填
    uri: "some.example.package.ExampleService:sayHello" # 类全路径:方法名
```

现在服务已经准备好了，是时候转战前端工程了。

## 编写组件

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

## 注册组件

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

## 创建 layout

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

## 访问组件和传参

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

## 通过 url 调用服务

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

## 为组件加入 js 和 css

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
