customerTemplate = Handlebars.templates["admins/supplier_detail/templates/customer"]
Modal = require "pokeball/components/modal"
Tip = require "common/tip_and_alert/view"
class Detail
  constructor: ->
    @supplierAuditSuccess = $(".js-audit-success")
    @supplierAuditCancel = $(".js-audit-cancel")
    @$customerBody = $("#customer-group-table tbody")
    @$customerBody.append(customerTemplate(@$customerBody.data("content")))
    @imageSwitch = $(".img img")
    @bindEvent()

  bindEvent: ->
    $(document).on "confirm:audit-supplier-apply-success", (evt, data) =>
      @auditSuccess data, true
    $(document).on "confirm:audit-supplier-edit-success", (evt, data) =>
      @auditSuccess data, false
    @imageSwitch.on "click", @switchImage
    @supplierAuditCancel.on "click", @auditCancel

  auditSuccess: (userId, apply)->
    rejectTemplate = Handlebars.templates["admins/supplier_audit/templates/reject"]
    rejectModal = new Modal rejectTemplate({type: 1, apply: apply})
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
        operation: 1
        description: formData.description
        resourceType: formData.resourceType
      if formData.resourceType is "1"
        competitors = _.compact(formData.competitor)
        return if competitors.length is 0
        requestData.competitors = competitors.join(",")
      $.ajax
        url: "/api/admin/supplier/audit"
        type: "POST"
        data: requestData
        success: (data)->
          window.location.href = "/suppliers/audit"

  auditCancel: ->
    data = $(@).data("id")
    rejectTemplate = Handlebars.templates["admins/supplier_audit/templates/reject"]
    rejectModal = new Modal rejectTemplate({type: 2})
    rejectModal.show()
    $("#reject-form").on "submit", (evt)->
      evt.preventDefault()
      if $("input[name=description]").val() is ""
        new Tip({parent: $("input[name=description]").parent(), direct: "up", type: "error", message: "必须输入意见", left: 130, top: 25}).tip()
      else
        $.ajax
          url: "/api/admin/supplier/audit"
          type: "POST"
          data: {"userId": data, "operation": 2, description: $("input[name=description]").val()}
          success: (data)->
            window.location.href = "/suppliers/audit"

  switchImage: ->
    window.open($(@).attr("src"))

module.exports = Detail
