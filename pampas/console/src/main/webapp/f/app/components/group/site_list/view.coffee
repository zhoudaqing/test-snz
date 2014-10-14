# Copyright (c) 2014 杭州端点网络科技有限公司
siteFormModalContentTemplate = Handlebars.templates["group/site_list/templates/site_form_modal_content"]

siteFormPrepare = ($form, onSuccess) ->
  validationRules =
    name:
      identifier: "name"
      rules: [
        type: "empty"
        prompt: "请输入站点名称，中英文不限"
      ]
    subdomain:
      identifier: "subdomain"
      rules: [
        type: "empty"
        prompt: "请输入二级域名"
      ]
  settings =
    inline: true
    on: "blur"
    onSuccess: onSuccess
  $form.form validationRules, settings

module.exports =
  init: ->
    $modal = $(@).find(".group-site-list-modal.site-info")
    $delConfirmModal = $(@).find(".group-site-list-modal.del-site-confirm")
    $pubConfirmModal = $(@).find(".group-site-list-modal.pub-site-confirm")
    app = $.query.get("app")
    group = $.query.get("group")

    $(@).on "click", ".js-btn-edit-site", ->
      site = $(@).closest("tr").data("site")
      $modal.find(".content").replaceWith siteFormModalContentTemplate(site)
      $siteForm = $modal.find("form")
      siteFormPrepare $siteForm, ->
        $.ajax
          url: "/api/design/group/#{group}/site/#{site.id}"
          type: "PUT"
          data:
            site: JSON.stringify($siteForm.serializeObject())
          success: ->
            window.location.reload()
        false
      $modal.modal("setting",
        onApprove: ->
          $siteForm.form("submit")
          false
        ).modal("show")

    $(@).on "click", ".js-btn-del-site", ->
      siteId = $(@).closest("tr").data("site").id
      $delConfirmModal.modal("setting",
        onApprove: ->
          $.ajax
            url: "/api/design/group/#{group}/site/#{siteId}"
            type: "DELETE"
            success: ->
              window.location.reload()
        ).modal("show")

    $(@).on "click", ".js-btn-pub-site", ->
      siteId = $(@).closest("tr").data("site").id
      $pubConfirmModal.modal("setting",
        onApprove: ->
          $.ajax
            url: "/api/design/group/#{group}/site/#{siteId}/publish"
            type: "POST"
            success: ->
              Essage.show
                message: "发布成功"
                status: "success"
              , 2000
        ).modal("show")

    $(@).find(".js-btn-new-site").click ->
      $modal.find(".content").replaceWith siteFormModalContentTemplate({app: app})
      $siteForm = $modal.find("form")
      siteFormPrepare $siteForm, ->
        $.ajax
          url: "/api/design/group/#{group}/app/#{app}/site"
          type: "POST"
          data:
            site: JSON.stringify($siteForm.serializeObject())
          success: ->
            window.location.reload()
        false
      $modal.modal("setting",
        onApprove: ->
          $siteForm.form("submit")
          false
        ).modal("show")
