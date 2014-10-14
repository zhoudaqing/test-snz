Modal = require "pokeball/components/modal"
class Credit
  constructor: ->
    @jsCreditButton = $(".js-credit")
    @creditSelect = "#credit-select"
    @rejectSelect = ".js-reject"
    @bindEvent()
  creditTemplate = Handlebars.templates["admins/credit_list/templates/credit"]
  that = this
  bindEvent: ->
    that = this
    @jsCreditButton.on "click", @jsCreditClick
    $(document).on "change", @creditSelect, @creditSelectChange
    $(document).on "confirm:btn" ,@rejectMessage
    $(document).on "click", @rejectSelect, @rejectSubmit

  jsCreditClick: ->
    id = $(@).data("id")
    alert id
    creditModal = new Modal creditTemplate({id: id})
    creditModal.show()
    $(".credit-form").on "submit", (evt)->
      evt.preventDefault()
      creditObject = $(@).serializeObject()
      $.ajax
        url: "/api/supplier/credit/update"
        type: "POST"
        data: creditObject
        success: (data)->
          alert data
          window.location.reload()

  creditSelectChange: ->
    if $(@).val() is "-3" or $(@).val() is "-1" or $(@).val() is "-2" or $(@).val() is "-4"
      $(@).closest(".control-group").siblings(".control-group").show()
  rejectMessage: ->
    id = $("#js-company-id").text()
    $.ajax
      url: "/api/supplier/credit-list/reject"
      data: {id: id}
      type: "GET"
      success: (data)->
        window.location.reload()
      error:(data) ->
        new Modal
          "icon" : "error"
          "title": "出错了"
          "content" : data.responseText
        .show()
  rejectSubmit: ->
    companyId = $(@).data("id")
    $("#js-company-id").text(companyId)
    $(@).closest("tr").find(".js-reject-submit").trigger("click")
module.exports = Credit
