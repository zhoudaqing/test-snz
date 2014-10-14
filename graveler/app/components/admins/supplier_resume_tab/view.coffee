Modal = require "pokeball/components/modal"

class SupplierResumeDetail
  constructor: ($)->
    @bindEvents()

  that = this

  bindEvents: ->
    that = this
    $(".tab").tab()
    @$el.find(".tab, .compare-tab").tab()
    @$el.find(".js-btn-resource-type").click @auditSuccess

    @$el.find(".js-nav-timeline").click ->
      $(window).trigger "timeline:visible"

  auditSuccess: ->
    userId = $.query.get("userId")
    rejectTemplate = Handlebars.templates["admins/supplier_audit/templates/reject"]
    rejectModal = new Modal rejectTemplate({type: 1, apply: true, onlyResource: true})
    rejectModal.modal.find(".js-select-quality").change (evt) ->
      evt.preventDefault()
      if $(@).val() is "1"
        rejectModal.modal.find(".rival-company").show()
        rejectModal.resetPosition()
      else
        rejectModal.modal.find(".rival-company").hide()
        rejectModal.resetPosition()
    rejectModal.show()
    $("#reject-form").on "submit", (evt)->
      evt.preventDefault()
      formData = $(@).serializeObject()
      requestData =
        userId: userId
        resourceType: formData.resourceType
      if formData.resourceType is "1"
        competitors = _.compact(formData.competitor)
        return if competitors.length is 0
        requestData.competitors = competitors.join(",")
      $.ajax
        url: "/api/admin/supplier/resource/confirm"
        type: "PUT"
        data: requestData
        success: ->
          window.location.reload()


module.exports = SupplierResumeDetail
