# Copyright (c) 2014 杭州端点网络科技有限公司
module.exports =
  init: ->
    $(@).find(".js-form-login").submit (evt) ->
      evt.preventDefault()

      loginTarget = $.query.get("target")

      $.ajax
        type: "POST"
        url: "/api/user/login"
        data:
          $(@).serializeObject()
        success: ->
          window.location.href = loginTarget or "/"
