Modal = require "pokeball/components/modal"
Tip = require "common/tip_and_alert/view"
class SupplierOrderList
  constructor: ($)->
    @supplierAuditSuccess = $(".js-audit-success")
    @supplierAuditCancel = $(".js-audit-cancel")
    @bindEvent()


  bindEvent: ->
    $(document).on "confirm:audit-supplier-apply-success", @auditSuccess
    @supplierAuditCancel.on "click", @auditCancel

  auditSuccess: (evt, data)->
    rejectTemplate = Handlebars.templates["admins/supplier_audit/templates/reject"]
    rejectModal = new Modal rejectTemplate({type: 1})
    rejectModal.show()
    $("#reject-form").on "submit", (evt)->
      evt.preventDefault()
      $.ajax
        url: "/api/admin/supplier/audit"
        type: "POST"
        data: {"userId": data, "operation": 1, description: $("input[name=description]").val()}
        success: (data)->
          window.location.reload()

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
            window.location.reload()

module.exports = SupplierOrderList
