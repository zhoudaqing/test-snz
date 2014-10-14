# Copyright (c) 2014 杭州端点网络科技有限公司
module.exports =
  init: ->
    $el = $(@)
    $el.find(".js-desc-popup").popup()
    $el.find(".js-sidebar").sidebar()

    $el.find(".sidebar-toggle-button").click ->
      $el.find(".js-sidebar").sidebar("toggle")
