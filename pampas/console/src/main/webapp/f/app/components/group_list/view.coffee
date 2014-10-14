# Copyright (c) 2014 杭州端点网络科技有限公司
module.exports =
  init: ->
    that = @
    $(@).find(".js-input-group-filter").keyup ->
      value = $(@).val()
      if value
        $(that).find(".group-row").hide()
        $(that).find(".group-row[data-group*=\"#{value}\"]").show()
      else
        $(that).find(".group-row").show()
