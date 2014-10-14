Pagination = require "pokeball/components/pagination"
Modal = require "pokeball/components/modal"
class DemandList
  constructor: ($)->
    @$tab = $(".tab")
    @filterForm = $("#requirement-filter-form")
    @bindEvents()

  bindEvents: ->
    # @$tab.tab()
    $(".datepicker").datepicker()
    $(document).on "confirm:apply-audit", @applyAudit
    $(document).on "confirm:audit-success", @auditRequirementSuccess
    $(document).on "confirm:audit-cancel", @auditRequirementCancel
    $(document).on "confirm:publish-requirement", @publishRequirement
    $(document).on "confirm:delete-requirement", @deleteRequirement
    @filterForm.on "submit", @queryRequirement
    new Pagination(".demand-pagination").total($(".demand-pagination").data("total")).show(10)

  queryRequirement: (evt)->
    evt.preventDefault()
    search = []
    search.push "reqName=#{$(@).find("input[name=reqName]").val()}" if !_.isEmpty $(@).find("input[name=reqName]").val()
    search.push "status=#{$(@).find("select[name=status]").val()}" if !_.isEmpty $(@).find("select[name=status]").val()
    search.push "startAt=#{$(@).find("input[name=startAt]").val()}" if !_.isEmpty $(@).find("input[name=startAt]").val()
    search.push "endAt=#{$(@).find("input[name=endAt]").val()}" if !_.isEmpty $(@).find("input[name=endAt]").val()
    if !_.isEmpty search
      window.location.search = search.join("&")

  applyAudit: (evt, data)->
    requirementId = data
    $.ajax
      url: "/api/purchaser/requirement/#{requirementId}/apply"
      type: "POST"
      success: (data)->
        window.location.reload()

  auditRequirementSuccess: (evt, data)->
    $.ajax
      url: "/api/purchaser/requirement/#{data}/auditSuccess"
      type: "POST"
      data: {auditRes: 2}
      success: (data)->
        window.location.reload()

  auditRequirementCancel: (evt, data)->
    rejectTemplate = Handlebars.templates["purchaser/demand_list/templates/reject"]
    rejectModal = new Modal rejectTemplate({type: 2})
    rejectModal.show()
    $("#reject-form").on "submit", (evt)=>
      evt.preventDefault()
      $.ajax
        url: "/api/purchaser/requirement/#{data}/auditCancel"
        type: "POST"
        data: {auditRes: -1, description: $("input[name=description]").val()}
        success: (data)->
          window.location.reload()

  publishRequirement: (evt, data)->
    $.ajax
      url: "/api/purchaser/requirement/#{data}/transitionStatus"
      type: "POST"
      success: ->
        $.ajax
          url: "/api/purchaser/requirement/#{data}/topics/default"
          type: "POST"
          data: {t: JSON.stringify({reqId: data})}
          sync: true
          success: ->
            window.location.href = "/purchaser/demand-detail?requirementId=#{data}"

  deleteRequirement: (evt, data)->
    requirementId = data
    $.ajax
      url: "/api/purchaser/requirement/#{requirementId}"
      type: "DELETE"
      success: (data)->
        window.location.reload()

module.exports = DemandList
