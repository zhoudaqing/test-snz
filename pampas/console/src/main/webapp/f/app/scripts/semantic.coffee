# Copyright (c) 2014 杭州端点网络科技有限公司
$.fn.form.settings.rules.regex = (value, regexText) ->
  new RegExp(regexText).test(value)
