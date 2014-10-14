# Copyright (c) 2014 杭州端点网络科技有限公司
require "handlebars_helpers"
require "ajax"
require "semantic"

console.info "log from app!"

$("[data-comp-path]").each ->
  $el = $(@)
  path = "#{$el.data("compPath")}/view"
  if window.require.modules[path] isnt undefined
    comp = require path
    comp.init.apply($el) if comp.init
