# Copyright (c) 2014 杭州端点网络科技有限公司
formPrepare = ($form, onSuccess) ->
  validationRules =
    permission:
      identifier: "permission"
      rules: [
        {
          type: "empty"
          prompt: "不能为空"
        }
        {
          type: "regex[^group:[^:]+(:app:[^:]+(:site:[^:]+)?)?$]"
          prompt: "格式不符"
        }
      ]
  settings =
    inline: true
    on: "blur"
    onSuccess: onSuccess
  $form.form validationRules, settings

permissionItemTemplate = Handlebars.templates["settings/users/user_permission/templates/permission_item"]

module.exports =
  init: ->
    that = @

    userId = $.query.get("userId")

    $(@).find(".ui.checkbox").checkbox()

    getPermissionList = ->
      permissions = []
      permissions.push "admin" if $(that).find(".js-checkbox-role-admin").prop("checked")
      $(that).find("[data-permission]").each ->
        permissions.push $(@).data("permission")
      permissions

    updatePermission = ->
      $(that).find(".js-dimmer-permission").dimmer("show")
      permissions = getPermissionList()
      $.ajax
        type: "POST"
        url: "/api/settings/user/#{userId}/permissions"
        data:
          permissions: JSON.stringify(permissions)
        complete: ->
          $(that).find(".js-dimmer-permission").dimmer("hide")

    $modal = $(@).find(".settings-users-user-permission-modal.new-permission")
    $permissionForm = $modal.find("form")
    formPrepare $permissionForm, ->
      permission = $(@).find("input[name=permission]").val()
      permissions = getPermissionList()
      if _.contains(permissions, permission)
        Essage.show
          message: "权限已存在"
          status: "warning"
        , 2000
        return false
      $(that).find(".js-list-permission").prepend(permissionItemTemplate(permission))
      $modal.modal("hide")
      updatePermission()
      false

    $(@).find(".js-btn-new-permission").click ->
      $modal.modal("setting",
        onApprove: ->
          $permissionForm.form("submit")
          false
        ).modal("show")

    $(@).find(".js-checkbox-role-admin").change updatePermission

    $(@).on "click", ".js-link-del-permission", (evt) ->
      evt.preventDefault()
      $(@).closest(".item").remove()
      updatePermission()
