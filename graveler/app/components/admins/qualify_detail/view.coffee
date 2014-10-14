Modal = require "pokeball/components/modal"
Tip = require "common/tip_and_alert/view"
class QualifyDetail
  constructor: ->
    @$qualifyDetailForm = $("#qualify-detail-form")
    @bindEvent()

  successTemplate = Handlebars.templates["admins/qualify_detail/templates/submit_success"]

  that = this
  bindEvent:->
    that = this
    @$qualifyDetailForm.validator
      identifier: ".check-input,select"
      isErrorOnParent: true
      errorCallback: @errorCallback
    $("input[name=file]").on "click", @fileUpload
    @$qualifyDetailForm.on "submit", @qualifyDetailFormSubmit


  fileUpload: (evt)->
    _this = @
    fileName = $(_this).closest(".control-group").find(".file-name")
    $(@).fileupload
      url: "/api/files/upload"
      type: "POST"
      dataType: "json"
      change: ->
        $(_this).closest(".btn").spin("small")
      success: (data)=>
        fileName.empty().append("""<a href="#{data.url}" target="_blank">#{data.name}</a>""")
        fileName.closest(".control-group").find(".file-input").val(data.url)
      complete: ->
        fileName.closest(".control-group").find("input[name=file]")
        $(_this).closest(".btn").spin(false)

  errorCallback: (unvalidFields)->
    $this = unvalidFields[0]
    offsetTop = $($this.$el).offset().top
    $("body").animate({scrollTop: (offsetTop-10)})
    if $this.error is "empty"
      message = $this.$el.data("empty")
    else if $this.error is "unvalid"
      message = $this.$el.data("error")
    new Tip({parent: $this.$el.parent(), type:"error", direct:"up", message: message, top:35, left: 30, width: 130}).tip()

  organizeData: (supplierId)->
    data = []
    status = true
    $.each $(".submit-group"), (i, div)->
      object = {}
      object.subjectId = $(div).find("input[name=subject]").val()
      object.attachUrl = $(div).find("input[name=attachUrl]").val()
      object.view = $(div).find("input[name=view]").val()
      object.supplierId = supplierId
      object.status = $(div).find("select[name=isPassed]").val()
      check1 = (object.attachUrl is "" and object.view is "" and object.status is "")
      check2 = (object.attachUrl isnt "" and object.view isnt "" and object.status isnt "")
      if !(check1 or check2)
        status = false
        new Tip({parent: $(div), type:"error", direct:"left", message: "请填写所有信息", top:30, left: 780, width: 130}).tip()
        offsetTop = $(div).offset().top
        $("body").animate({scrollTop: (offsetTop-10)})
      if check2
        data.push object
    {data, status}


  qualifyDetailFormSubmit: (evt)->
    evt.preventDefault()
    supplierId =  $.query.get("supplierId")
    requirementId = $.query.get("requirementId")
    data = {}
    data = that.organizeData(supplierId)
    false unless data.status
    if data.status
      $.ajax
        url: "/api/supplier/qualification"
        type: "POST"
        data: {supplierId: supplierId, details: JSON.stringify(data.data)}
        success: (data)->
          new Modal successTemplate()
          .show()

module.exports = QualifyDetail
