Modal = require "pokeball/components/modal"
Pagination = require "pokeball/components/pagination"
Tip = require "common/tip_and_alert/view"
class SupplierAppointed
  constructor: ($)->
    @supplierAuditCancelFirst = $(".js-audit-cancel-first")
    @supplierAuditCancelLast = $(".js-audit-cancel-last")
    @supplierAuditSuccess = $(".js-audit-success")
    @bindEvent()

  that = this

  bindEvent: ->
    $(document).on "confirm:audit-supplier-apply-success-first", {flag:0}, @auditSuccess
    $(document).on "confirm:audit-supplier-apply-success-last", {flag:1}, @auditSuccess
    @supplierAuditCancelFirst.on "click", @auditCancel
    @supplierAuditCancelLast.on "click", @auditCancel
    new Pagination(".supplier-pagination").total($(".supplier-pagination").data("total")).show(10)

  #审核，flag=0初级审核通过，flag=1终极审核通过
  auditSuccess: (evt, data)->
    evt.preventDefault()
    dataArray = data.split(",")
    if dataArray.length > 1 and dataArray[1] is "1"
      $.ajax
        url: "/api/admin/supplierAppointed/audit"
        type: "POST"
        data: {"jiaZhiId": dataArray[0], "flag": evt.data.flag}
        success: (data)->
          window.location.reload()
    else
      new Modal
        "icon": "info"
        "title": "审核失败"
        "content": "供应商未注册，不能审核"
      .show()

  #退回，flag=0初级审核通过，flag=1终极审核通过
  auditCancel: ->
    data = $(@).data("id")
    flag = $(@).data("flag")
    rejectTemplate = Handlebars.templates["admins/supplier_audit/templates/reject"]
    rejectModal = new Modal rejectTemplate({type: 2})
    rejectModal.show()
    $("#reject-form").on "submit", (evt)->
      evt.preventDefault()
      if $("input[name=description]").val() is ""
        new Tip({parent: $("input[name=description]").parent(), direct: "up", type: "error", message: "必须输入意见", left: 130, top: 25}).tip()
      else
        $.ajax
          url: "/api/admin/supplierAppointed/reject"
          type: "POST"
          data: {"jiaZhiId": data, "flag": flag, advice: $("input[name=description]").val()}
          success: (data)->
            window.location.reload()

module.exports = SupplierAppointed
