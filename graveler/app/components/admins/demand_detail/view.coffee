timePlanTemplate = Handlebars.templates["admins/demand_detail/templates/time_plan"]
Modal = require "pokeball/components/modal"

class DemandDetail
  constructor: ($)->
    @target = @$el
    @$deliveryShow = $(".demand-delivery")
    @$attachmentDiv = $(".demand-attachment")
    @$requirementStatusTransition = $(".js-transition-status")
    @$moreInfo = $("#js-more-info")
    @$solutionSubmit = $(".js-submit-solution")
    @$requirementTimeDelay = $(".js-requirement-delay")
    @$inputFile = $("input[type=file]")
    @bindEvents()

  that = this
  bindEvents: ->
    that = this
    $(document).on "confirm:lock-requirement", @transitionRequirementStatus
    @$requirementStatusTransition.on "click", @transitionRequirementStatus
    @$moreInfo.on "click", @hideOrShowDetail
    @resolveScope()
    @resolveFiles()
    # @showDelivery()
    @$requirementTimeDelay.on "click", @renderTimePlan
    @invalidOperateAfterEnd()
    @target.delegate "input[type=file]","click",@fileUpload
    #@$inputFile.on("click", @fileUpload) if @$inputFile.length isnt 0

  invalidOperateAfterEnd: ->
    if $(".demand-detail .demand-content").data("endTime")
      endTime = new Date($(".demand-detail .demand-content").data("endTime")).valueOf()
      TodayTime = new Date(moment().format("YYYY-MM-DD")).valueOf()
      if endTime <= TodayTime
        $(".demand-detail .btn:not(.js-requirement-delay, .js-transition-status, .js-confirm-requirement, .js-supplier-link)").attr("disabled", "disabled")
        $(".mutual-tab .btn:not(.js-requirement-delay, .js-transition-status, .js-new-topic, .js-rank-btn, .js-calculate-supplier, .js-add-new-module)").attr("disabled", "disabled")
        $(".demand-detail .js-link").removeAttr "disabled"
      else
        $(".demand-detail .js-link").attr("href", "javascript:void(0);")

  # 提升交互，点击日历图标触发日期选择
  triggerDateInput: ->
    $(@).closest(".input-group").find("input").trigger("click")

  # 显示或隐藏详情
  hideOrShowDetail: ->
    $(".detail-info").toggle()

  # 显示园区信息
  showDelivery: ->
    delivery = @$deliveryShow.data("delivery")
    _.each delivery, (i)=>
      @$deliveryShow.append("#{i.paName} > #{i.faNum}&nbsp;&nbsp;")

  # 手动使需求进入下一阶段
  transitionRequirementStatus: (evt, data)->
    if !_.isUndefined(data) then requirementId = data else requirementId = $(@).data("id")
    status = $(".demand-content").data("status")
    $.ajax
      url: "/api/purchaser/requirement/#{requirementId}/transitionStatus"
      type: "POST"
      success: ->
        window.location.reload()

  # 解析产业范围
  resolveScope: ->
    scope = $(".product-scope").data("scope")
    scopeText = (_.map scope, (i)-> i.name).join(",")
    $(".product-scope").text(scopeText).attr "title", scopeText

  # 解析附件
  resolveFiles: ->
    files = @$attachmentDiv.data("files")
    status = @$attachmentDiv.data "status"
    source = @$attachmentDiv.data "source"
    replaceHref = if status is 1  or status is 2 then """ &nbsp;<a class="replace-file">替换</a> """ else ""
    _.each files, (i)=>
      if !_.isEmpty i.file
        @$attachmentDiv.append("""<a href="#{i.file}" class="js-load-file" data-fileName="#{i.fileName}" data-cname="#{i.cName}" data-file="#{i.file}" data-name="#{i.name}">#{i.cName}</a>#{replaceHref}&nbsp;&nbsp;&nbsp;""")
      else
        if i.cName is "模块接口" or i.cName is "封包" or  i.cName is "技术规格书"
          @$attachmentDiv.append("""<a href="javascript:void(0);" class="js-load-file js-no-file" data-fileName="" data-cname="#{i.cName}" data-file="#{i.file}" data-name="#{i.name}">#{i.cName}</a>#{replaceHref}&nbsp;&nbsp;&nbsp;""")
        else
          @$attachmentDiv.append("""<a class="js-notshow-file" data-fileName="#{i.fileName}" data-cName="#{i.cName}" data-file="#{i.file}" data-name="#{i.name}"></a>""")

    @$attachmentDiv.find("a.replace-file").on("click",(evt)->
      realtrigger = this
      that.$inputFile.trigger "click",{link:realtrigger,filelink:$(realtrigger).prev()}
     ) if @$inputFile.length isnt 0

  #上传文件
  fileUpload:(evt, data)->
    $filelink = data.filelink
    $self = $(@)
    $(@).fileupload
      "change": =>$(".demand-attachment").spin("small")
      url: "/api/files/upload"
      type: "POST"
      dataType: "json"
      success: (data)->
        that.$inputFile.val("")
        that.$inputFile.on "click", that.fileUpload
        name = $filelink.data("name")
        cName = $filelink.data("cname")
        $filelink.removeClass "js-no-file" if $filelink.hasClass "js-no-file"
        file = data.url
        fileName = data.name
        files = that.organizeRequirementFile {name, cName, file, fileName}
        that.updateRequirementFile files,$filelink,{name, cName, file, fileName}
      complete: =>
        $(".demand-attachment").spin(false)

  updateRequirementFile: (data, link, obj)->
    accessories = data
    requirementId = that.$inputFile.data("id")
    $.ajax
      url: "/api/purchaser/requirement/#{requirementId}/accessories"
      type: "PUT"
      data: {accessories: JSON.stringify accessories}
      success: (data)->
        new Modal
          "icon": "success"
          "title": "附件替换成功"
          "content": "替换成功"
        .show()
        that.replaceLinkValue link, obj

  # 组织需求文件数据
  organizeRequirementFile: (obj)->
    data = _.map $(".js-load-file,.js-notshow-file"), (i)->
      name = $(i).data("name")
      file = $(i).data("file")
      cName = $(i).data("cname")
      fileName =  $(i).data("filename")
      if cName is obj.cName and name is obj.name
        obj
      else
        {name, cName, file, fileName}
    data

  #替换filelink 上得值
  replaceLinkValue: (link, obj)->
    $(link).data("name",obj.name).data("file",obj.file).data("cname",obj.cName).data("filename",obj.fileName).attr("href",obj.file)

  # 渲染时间计划
  renderTimePlan: ->
    requirementId = $(@).data("id")
    $.ajax
      url: "/api/purchaser/requirement/#{requirementId}/times"
      type: "GET"
      success: (data)->
        status = $(".demand-content").data("status")
        new Modal(timePlanTemplate({predictTimeList: data.reqPredictTimes, requirementTimes: data.requirementTimes, status: status})).show()
        $("#time-form").validator()
        that.initTimeMinDate()
        $(".calendar").on "click", @triggerDateInput
        $(".predict-start").on "change", that.changeStartTime
        $(".predict-end").on "change", that.changeEndTime
        $("#time-form").on "submit", that.updateRequirementTimes

  # 初始化日期选取范围
  initTimeMinDate: ->
    _.each $(".datepicker[data-write=true]"), (i)->
      if !_.isEmpty $(i).val()
        $(i).datepicker({minDate: moment($(i).val()).toDate()})
      else
        $(i).datepicker({minDate: moment().toDate()})

  # 选择开始时间
  changeStartTime: ->
    $predictStart = $(@).closest(".date-div").find(".predict-start")
    $predictEnd = $(@).closest(".date-div").find(".predict-end")
    currentIndex = $(".date-div.control-group").index($(@).closest(".date-div"))
    limitLaterTime(currentIndex, $predictStart, $predictEnd)
    $predictEnd.data("pikaday")._o.minDate = moment($predictStart.val()).toDate()
    if !_.isEmpty($predictEnd.val()) and moment($predictStart.val()).valueOf() > moment($predictEnd.val()).valueOf()
      $predictEnd.val($predictStart.val())
      $predictEnd.trigger("change")

  # 选择结束时间
  changeEndTime: ->
    $predictStart = $(@).closest(".date-div").find(".predict-start")
    $predictEnd = $(@).closest(".date-div").find(".predict-end")
    currentIndex = $(".date-div.control-group").index($(@).closest(".date-div"))
    nextDateGroup = $(".date-div.control-group:eq(#{currentIndex+1})")
    if nextDateGroup.length isnt 0
      nextDateGroup.find(".predict-start").val($predictEnd.val()).data("pikaday")._o.minDate = moment($predictEnd.val()).toDate()
      nextDateGroup.find(".predict-start").trigger("change")

  # 统一限制最小时间
  limitLaterTime = (currentIndex, $predictStart, $predictEnd)->
    if $(".date-div.control-group:gt(#{currentIndex})").length isnt 0
      _.each $(".date-div.control-group:gt(#{currentIndex})"), (i)->
        if moment($predictStart.data("pikaday")._o.minDate).valueOf() > moment($(i).find(".predict-start").data("pikaday")._o.minDate).valueOf()
          $(i).find(".predict-start").data("pikaday")._o.minDate = moment($predictStart.val()).toDate()
        if moment($predictStart.data("pikaday")._o.minDate).valueOf() > moment($(i).find(".predict-end").data("pikaday")._o.minDate).valueOf()
          $(i).find(".predict-end").data("pikaday")._o.minDate = moment($predictStart.val()).toDate()
        if !_.isEmpty($(i).find(".predict-start").val()) and moment($predictStart.val()).valueOf() > moment($(i).find(".predict-start").val()).valueOf()
          $(i).find(".predict-start").val($predictStart.val())
        if moment($predictStart.val()).valueOf() > moment($(i).find(".predict-end").val()).valueOf()
          $(i).find(".predict-end").val($predictStart.val())

  # 组织时间计划信息
  organizeTimes: (form)->
    requirementTimes = _.map $(form).find(".date-div.control-group"), (i)->
      id = $(i).data("id")
      requirementId = $(".demand-content").data("id")
      requirementName = $(".demand-content").data("name")
      type = $(i).data("type")
      predictStart = $(i).find(".predict-start").val()
      predictEnd = $(i).find(".predict-end").val()
      {id, type, predictStart, predictEnd, requirementId, requirementName}

  # 计划延期
  updateRequirementTimes: (evt)->
    evt.preventDefault()
    requirementTimes = that.organizeTimes($(@))
    $.ajax
      url: "/api/purchaser/requirement/times"
      type: "PUT"
      data: {requirementTimes: JSON.stringify requirementTimes}
      success: ->
        window.location.reload()


module.exports = DemandDetail
