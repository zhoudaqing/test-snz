# Copyright (c) 2014 杭州端点网络科技有限公司
userFormModalContentTemplate = Handlebars.templates["settings/users/user_list/templates/user_form_modal_content"]

userFormPrepare = ($form, isEdit, onSuccess) ->
  validationRules =
    name:
      identifier: "name"
      rules: [
        type: "empty"
        prompt: "请输入用户名"
      ]
    realName:
      identifier: "realName"
      rules: [
        type: "empty"
        prompt: "请输入真名"
      ]
    email:
      identifier: "email"
      rules: [
        type: "empty"
        prompt: "请输入 email"
      ]
  unless isEdit
    validationRules.password =
      identifier: "password"
      rules: [
        type: "empty"
        prompt: "请输入密码"
      ]
  settings =
    inline: true
    on: "blur"
    onSuccess: onSuccess
  $form.form validationRules, settings

module.exports =
  init: ->
    that = @

    $modal = $(@).find(".settings-users-user-list-modal.user-info")
    $delConfirmModal = $(@).find(".settings-users-user-list-modal.del-user-confirm")

    $(@).find(".js-btn-new-user").click ->
      $modal.find(".content").replaceWith userFormModalContentTemplate()
      $userForm = $modal.find("form")
      userFormPrepare $userForm, false, ->
        $.ajax
          url: "/api/settings/user"
          type: "POST"
          data:
            user: JSON.stringify($userForm.serializeObject())
          success: ->
            window.location.reload()
        false
      $modal.modal("setting",
        onApprove: ->
          $userForm.form("submit")
          false
        ).modal("show")

    $(@).find(".js-btn-edit-user").click ->
      user = $(@).data("user")
      $modal.find(".content").replaceWith userFormModalContentTemplate({user: user})
      $userForm = $modal.find("form")
      userFormPrepare $userForm, true, ->
        $.ajax
          url: "/api/settings/user"
          type: "PUT"
          data:
            user: JSON.stringify($userForm.serializeObject())
          success: ->
            window.location.reload()
        false
      $modal.modal("setting",
        onApprove: ->
          $userForm.form("submit")
          false
        ).modal("show")

    $(@).find(".js-btn-del-user").click ->
      userId = $(@).data("userId")
      $delConfirmModal.modal("setting",
        onApprove: ->
          $.ajax
            url: "/api/settings/user/#{userId}"
            type: "DELETE"
            success: ->
              window.location.reload()
        ).modal("show")

    require("pagination").init $(@).find(".pagination-list")
