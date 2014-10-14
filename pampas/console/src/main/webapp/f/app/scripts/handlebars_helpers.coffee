# Copyright (c) 2014 杭州端点网络科技有限公司
Handlebars.registerHelper "rget", (vals, options) ->
  if Object.prototype.toString.call(vals) == '[object Array]'
    list = vals
  else
    list = vals.toString().split(",")
  list[parseInt(Math.random() * list.length)]
