# Copyright (c) 2014 杭州端点网络科技有限公司
module.exports =
  init: ->
    $(@).find(".ui.dropdown").dropdown({on: "hover"})
    $(@).find(".js-link-logout").click (evt) ->
      evt.preventDefault()
      $.ajax
        type: "POST"
        url: "/api/user/logout"
        success: ->
          window.location.href = "/login"
