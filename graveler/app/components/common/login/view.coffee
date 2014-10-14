Tip = require "common/tip_and_alert/view"
class Login
  constructor: ($)->
    @$userLoginForm = $("#login")
    @loginName = $("#login-name")
    @loginPassword = $("#login-password")
    @bindEvent()

  that = this
  bindEvent: ->
    that = this
    @$userLoginForm.on "submit", @adminLoginFormSubmit

  adminLoginFormSubmit: (evt)->
    evt.preventDefault()
    $.ajax
      url: "/api/user/login"
      type: "POST"
      data: $(@).serialize()
      success: (data)->
        if $.query.get("target") is ""
          href = "/"
        else
          href = $.query.get("target")
        window.location.href = href
      error: (data)->
        if /^用户/.test(data.responseText) ||  /^登录名/.test(data.responseText)
          new Tip({parent: that.loginName, type:"error", direct:"up", message: data.responseText, top: 30, left: 25, width: 120}).tip()
        else
          new Tip({parent: that.loginPassword, type:"error", direct:"up", message: data.responseText, top: 30, left: 30, width: 120}).tip()

module.exports = Login
