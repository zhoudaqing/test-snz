# Copyright (c) 2014 杭州端点网络科技有限公司
appFormModalContentTemplate = Handlebars.templates["group/app_list/templates/app_form_modal_content"]

appFormPrepare = ($form, onSuccess) ->
  validationRules =
    name:
      identifier: "key"
      rules: [
        type: "empty"
        prompt: "请输入 app key"
      ]
    subdomain:
      identifier: "domain"
      rules: [
        type: "empty"
        prompt: "请输入 app 域名"
      ]
  settings =
    inline: true
    on: "blur"
    onSuccess: onSuccess
  $form.form validationRules, settings

module.exports =
  init: ->
    that = @

    $(@).find(".ui.dropdown").dropdown()

    rootPath = $(@).find(".js-val-root-path").val()
    return unless rootPath # 如果没有 rootPath 就表示是 implant mode ，啥初始化都不用做了
    $dimmer = $(@).find(".dimmer")
    $modal = $(@).find(".group-app-list-modal.app-info")
    $extraDomainsModal = $(@).find(".group-app-list-modal.extra-domains")
    $delConfirmModal = $(@).find(".group-app-list-modal.del-app-confirm")
    group = $.query.get("group")

    $(@).find(".js-btn-new-app").click ->
      $modal.find(".content").replaceWith appFormModalContentTemplate({rootPath: rootPath})
      $appForm = $modal.find("form")
      appFormPrepare $appForm, ->
        $.ajax
          url: "/api/settings/group/#{group}/app"
          type: "POST"
          data:
            group: group
            app: JSON.stringify($appForm.serializeObject())
          success: ->
            window.location.reload()
        false
      $modal.modal("setting",
        onApprove: ->
          $appForm.form("submit")
          false
        ).modal("show")

    $(@).find(".js-btn-edit-app").click ->
      $dimmer.dimmer("show")
      $.ajax
        url: "/api/settings/group/#{group}/app/#{$(@).data("appKey")}"
        type: "GET"
        success: (data) ->
          $dimmer.dimmer("hide")
          $modal.find(".content").replaceWith appFormModalContentTemplate({rootPath: rootPath, app: data})
          $appForm = $modal.find("form")
          appFormPrepare $appForm, ->
            $.ajax
              url: "/api/settings/group/#{group}/app"
              type: "PUT"
              data:
                group: group
                app: JSON.stringify($appForm.serializeObject())
              success: ->
                window.location.reload()
            false
          $modal.modal("setting",
            onApprove: ->
              $appForm.form("submit")
              false
            ).modal("show")

    $(@).find(".js-btn-del-app").click ->
      appKey = $(@).data("appKey")
      $delConfirmModal.modal("setting",
        onApprove: ->
          $.ajax
            url: "/api/settings/group/#{group}/app/#{appKey}"
            type: "DELETE"
            success: ->
              window.location.reload()
        ).modal("show")

    $(@).find(".js-btn-extra-domains").click ->
      $dimmer.dimmer("show")
      appKey = $(@).data("appKey")
      $.ajax
        url: "/api/settings/group/#{group}/app/#{appKey}/extra-domains"
        type: "GET"
        success: (data) ->
          $dimmer.dimmer("hide")
          domainsText = if data.length is 0 then "" else data.join("\n")
          $extraDomainsModal.find("textarea").val(domainsText)
          $extraDomainsModal.modal("setting",
            onApprove: ->
              domainsText = $extraDomainsModal.find("textarea").val()
              domainsArray = if domainsText is "" then [] else _.compact(domainsText.split("\n").map((domain) ->
                domain.trim()
                ))
              $.ajax
                url: "/api/settings/group/#{group}/app/#{appKey}/extra-domains"
                type: "PUT"
                data:
                  extraDomains: JSON.stringify domainsArray
                success: ->
                  window.location.reload()
              false
            ).modal("show")
