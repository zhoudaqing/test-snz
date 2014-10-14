# 前端工程结构

## 目录结构

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

## 配置文件

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
