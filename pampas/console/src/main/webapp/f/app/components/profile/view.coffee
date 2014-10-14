# Copyright (c) 2014 杭州端点网络科技有限公司
module.exports =
  init: ->
    that = @

    $profileForm = $(@).find(".js-form-profile")
    $passwordForm = $(@).find(".js-form-password")

    $profileForm.form(
      {
        realName:
          identifier: "realName"
          rules: [
            type: "empty"
            prompt: "请输入真实姓名"
          ]
        email:
          identifier: "email"
          rules: [
            {
              type: "empty"
              prompt: "请输入邮箱"
            }, {
              type: "email"
              prompt: "不是正确的邮箱格式"
            }
          ]
      }, {
        inline: true
        on: "blur"
        onSuccess: ->
          $.ajax
            type: "PUT"
            url: "/api/user/profile"
            data:
              user: JSON.stringify($profileForm.serializeObject())
            success: ->
              window.location.reload()
          false
      }
    )

    $passwordForm.form(
      {
        originPassword:
          identifier: "originPassword"
          rules: [
            type: "empty"
            prompt: "请输入原密码"
          ]
        newPassword:
          identifier: "newPassword"
          rules: [
            type: "empty"
            prompt: "请输入新密码"
          ]
        newPasswordRepeat:
          identifier: "newPasswordRepeat"
          rules: [
            type: "empty"
            prompt: "请重复新密码"
            ,
            type: "match[newPassword]"
            prompt: "两次输入的密码必须一致"
          ]
      }, {
        inline: true
        on: "blur"
        onSuccess: ->
          $.ajax
            type: "PUT"
            url: "/api/user/password"
            data: $passwordForm.serializeObject()
            success: ->
              window.location.reload()
          false
      }
    )
