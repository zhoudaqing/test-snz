Pagination = require "pokeball/components/pagination"
Modal = require "pokeball/components/modal"
Tip = require "common/tip_and_alert/view"
DemandModule = require "admins/demand_module/view"

topicListTemplate = Handlebars.templates["admins/mutual_tab/templates/list_topic"]
newTopicTemplate = Handlebars.templates["admins/mutual_tab/templates/new_topic"]
operatinSelectSupplierTemplate = Handlebars.templates["admins/mutual_tab/templates/operation_select_supplier_table"]
peopleListTemplate = Handlebars.templates["admins/mutual_tab/templates/people_list"]
techSolutionTableTemplate = Handlebars.templates["admins/mutual_tab/templates/tech_solution_table"]
newModuleTemplate = Handlebars.templates["admins/demand_module/templates/module_edit"]
moduleItemTemplate = Handlebars.templates["admins/mutual_tab/templates/module_item"]
unitTemplate = Handlebars.templates["admins/demand_module/templates/select_unit"]
purchaserModuleQuotaTemplate = Handlebars.templates["admins/mutual_tab/templates/purchaser_quota_list"]
supplierModuleQuotaTemplate = Handlebars.templates["admins/mutual_tab/templates/supplier_quota_list"]
userModuleQuotaTemplate = Handlebars.templates["admins/mutual_tab/templates/user_quota_list"]
techSelectSupplierTemplate = Handlebars.templates["admins/mutual_tab/templates/tech_select_supplier_table"]
mutualSuppliersTemplate = Handlebars.templates["admins/mutual_tab/templates/mutual_supplier_table"]
moduleFactoryTemplate = Handlebars.templates["admins/mutual_tab/templates/factory_select_num"]

class MutualTab extends DemandModule
  constructor: ($)->
    @$target = @$el
    @$tab = $(".tab")
    @$tab.tab({activeIndex: @$tab.find(".active[data-role=nav]").index()})
    @$topicStatusSelect = $(".topic-status-select")
    @totalTopic = $(".topic-total").data("total")
    @topicNew = $(".js-new-topic")
    @supplierRange = $(".js-range-supplier")
    @$solutionForms = $(".solution-form")
    @$selectSupplierForm = $("#select-supplier-form")
    @$solutionList = $("#select-supplier-group")
    @$newModuleAdd = $(".js-add-new-module")
    @$calculateSupplier = $(".js-calculate-supplier")
    @$multiRow = $(".multi-row")
    @$importExcel = $(".js-import-module")
    @$inputFile = $("input[type=file]")
    @$downloadTemplate = $(".js-download-template")
    @bindEvents()

  that = this

  bindEvents: ->
    that = this
    @getTopicList()
    @getModuleList()
    @setTeamMember()
    @getPurchaserModuleQuotaList() if @$tab.data("solution")
    @renderSolutionTable() if _.contains([3,4,6], @$tab.data("status"))
    @renderPromisedSuppliers() if _.contains([1,2,3,4,5,6], @$tab.data("status"))
    @fetchSolutions() if _.contains([3,5,6], @$tab.data("status"))
    super()
    @topicNew.on "click", @newTopic
    @supplierRange.on "click", @selectRankWay
    @$topicStatusSelect.on "change", @selectTopicStatus
    @$inputFile.on "click", @fileUpload
    # $(document).on ""
    @$importExcel.on "click", ->
      that.$inputFile.trigger "click"
    @$downloadTemplate.on "click", ->
      window.location.href = "/api/template/modulestemplate/#{$(@).data("id")}"

  #批量导入
  fileUpload: ->
    $self = $(@)
    $(@).fileupload
      "change": => $self.closest(".btn-upload").spin("small")
      url: "/api/template/importmodules/#{$(@).data("id")}"
      type: "POST"
      dataType: "json"
      success: (data)->
        new Modal
          "icon": "success"
          "title": "成功！"
          "content": "Excel上传成功,批量导入成功！"
        .show ->
          window.reload()
      complete: =>
        $self.closest(".btn-upload").spin(false)

  # 获取话题分页列表
  getTopicList: (pageNo)->
    self = @
    pageNo = pageNo || 1
    reqId = $.query.get("requirementId")
    query = ["pageNo=#{pageNo}", "reqId=#{reqId}", "pageSize=5"]
    if $("#req-status").val() isnt "0"
      reqStatus = $("#req-status").val()
      query.push "reqStatus=#{reqStatus}"

    $.get """/api/topics?#{query.join("&")}""", (data)=>
      $(".topics-body").remove()
      if data.total isnt 0
        $(".topics-table").append(topicListTemplate({topics: data.data}))
        reqStatusText = $("#req-status").find("option:selected").text()
        $(".topic-req-status").text(reqStatusText)
        $("#topic-total-num").text(data.total)
        new Pagination($(".topic-pagination")).total(data.total).show 5,
          current_page: pageNo - 1
          callback: (pageNo) ->
            self.getTopicList pageNo + 1
      else
        $(".topic-pagination").empty()
      $("#topic-total-num").text(data.total)

  # 获取不同状态的话题列表
  selectTopicStatus: ->
    that.getTopicList()

  # 创建话题
  newTopic: ->
    companyName = $("#demand-content-show").data("company")
    requirementId = $(@).data("id")
    requirementStatus = $("#demand-content-show").data("status")
    new Modal(newTopicTemplate({companyName:companyName, reqId: requirementId, reqStatus: requirementStatus})).show()
    $("#new-topic-form").validator()
    $("#new-topic-form").on "submit", that.submitTopic
    $("input[type=file]", "#new-topic-form").on "click", that.uploadMutipleFile
    $("input[name=scope]", "#new-topic-form").on "change", that.changeScopeOfTopic

  # 选取不同的话题圈子范围
  changeScopeOfTopic: ->
    if $(@).val() is "2" or $(@).val() is "3"
      requirementId = $(@).closest("form").data("id")
      $(@).closest(".select-topic-scope").find(".menu").remove()
      if $(@).val() is "3" then size = "small" else size = "big"
      $.ajax
        url: "/api/topic/#{requirementId}/users"
        type: "GET"
        success: (data)=>
          $(@).closest(".scope-select-label").append(peopleListTemplate({data:data, size:size}))
          $(".ul-show-a", "#new-topic-form").on "click", that.hideAndShowSelectUsers
    else
      $(@).closest(".select-topic-scope").find(".menu").remove()

  # 隐藏或显示用户列表
  hideAndShowSelectUsers: ->
    if $("""ul##{$(@).data("target")}""").hasClass("hide")
      $("""ul##{$(@).data("target")}""").removeClass("hide")
      $(@).find("i.icon-arrowdown-small").removeClass("hide")
      $(@).find("i.icon-arrowright-small").addClass("hide")
    else
      $("""ul##{$(@).data("target")}""").addClass("hide")
      $(@).find("i.icon-arrowright-small").removeClass("hide")
      $(@).find("i.icon-arrowdown-small").addClass("hide")

  # 上传文件
  uploadMutipleFile: ->
    input = $(@).closest(".attachment-file").find("input[name=files]")
    $self = $(@)
    if !_.isEmpty(input.val()) then files = JSON.parse(input.val()) else files = []
    $(@).fileupload
      "change": => $self.closest(".btn-upload").spin("small")
      url: "/api/files/upload"
      type: "POST"
      dataType: "json"
      success: (data)=>
        files.pop() if files.length is 5
        files.push {name: data.name, url:data.url}
        input.val(JSON.stringify(files))
      complete: ->
        $self.closest(".btn-upload").spin(false)

  # 组织话题参与者数据
  organizeTopicJoiners: ->
    scopeValue = $("#new-topic-form input[name=scope]:checked").val()
    joinerIds = (_.map $("input.joiner:checked"), (i)-> $(i).val()).join(",") if scopeValue is "2" or scopeValue is "3"
    joinerIds

  # 提交话题信息并创建
  submitTopic: (evt)->
    evt.preventDefault()
    topic = $(@).serializeObject()
    joinerIds = that.organizeTopicJoiners()
    $.post "/api/purchaser/topics", {t: JSON.stringify(topic), joinerIds: joinerIds}, (data) ->
      new Modal({icon: "success", content: "创建话题成功，您可以在话题列表中查看"}).show ->
        that.getTopicList()
        $(".modal").remove()

  # 渲染签过保密协议的供应商列表
  renderPromisedSuppliers: ->
    self = @
    pageNo = pageNo or 1
    requirementId = $.query.get("requirementId")
    requirmentStatus = $(".tab").data "status"
    $.get "/api/admin/requirement/#{requirementId}/suppliers?pageNo=#{pageNo}", (data) ->
      $(".mutual-supplier-group").empty()
      $(".mutual-supplier-group").append(mutualSuppliersTemplate({data: data, type: "protocol", href: that.$tab.data("href"), status:requirmentStatus}))
      new Pagination($(".supplier-pagination")).total(data.total).show 20,
        current_page: pageNo - 1
        callback: (pageNo) ->
          self.renderPromisedSuppliers pageNo + 1

  # 根据tactics渲染不同的查看方案tab，tactics是1或2，查看方案为供应商的解决方案列表，tactics为3，查看方案为承诺过的供应商列表
  renderSolutionTable: ->
    requirementId = $.query.get("requirementId")
    tactics = @$tab.data("tactics")
    status = that.$tab.data("status")
    if tactics is 3 then statusArray = "1,2,3,4,5" else statusArray = "4,5"
    $.get "/api/admin/requirement/#{requirementId}/solutions", {statusArray: statusArray}, (data) ->
      if tactics is 3 and _.contains [1,2,3,4,5,6], status
        $("#solution-score-form").append(mutualSuppliersTemplate({data: data, type: "promise"}))
      else if  _.contains([1,2], tactics) and _.contains [3,5,6], status
         that.renderTechSolutionTable(data)

  # 渲染需求的团队成员列表
  setTeamMember: ->
    memberList = $("#team-member-group").data("memberList")
    $("#team-member-group").data("member", _.map memberList, (i)-> i.userId)

  # 渲染技术领先和差异化的解决方案列表
  renderTechSolutionTable: (data)->
    userId = that.$tab.data("userId")
    creatorId = that.$tab.data("creator")
    userType = that.$tab.data("user")
    $("#solution-score-form").append(techSolutionTableTemplate({solutions: data.data, status: that.$tab.data("status"), userId, creatorId, userType}))
    that.resolveSingleSolutionFile("#solution-score-form")
    $("#solution-score-form").validator()
    $("#solution-score-form").on "submit", that.submitSolutionScore

  # 提交供应商评分
  submitSolutionScore: (evt)->
    evt.preventDefault()
    solutions = _.map $(".solution-score-input"), (i)->
      id = $(i).data("id")
      technology = $(i).val() * 100
      {id, technology}
    $.post "/api/purchaser/requirement/solution/updateBatchTechnology", {solutions: JSON.stringify solutions}, ->
      window.location.reload()

  # 解析供应商解决方案文件
  resolveSingleSolutionFile: (container)->
    _.each $(container).find(".solution-download-td"), (i)->
      file = $(i).data("file")
      if file then $(i).prepend("""<a href="#{file.url}">#{file.name}</a>""") else $(i).prepend("暂未提交方案")

  # 选择排名方式
  selectRankWay: ->
    if not $(@).find("i").hasClass("icon-arrowdown-current")
      $(@).closest("tr").find("i").attr("class","icon-arrowdown")
      $(@).find("i").attr("class","icon-arrowdown-current")
    that.fetchSolutions()

  # 获取方案排名列表
  fetchSolutions: ->
    @$solutionList.empty()
    requirementId = $.query.get("requirementId")
    if @$tab.data("tactics") is 3
      $.get "/api/purchaser/solutions", {requirementId}, (data) =>
        @$solutionList.append(operatinSelectSupplierTemplate({data: data, requirementId}))
        that.resolveSolutionFile()
    else
      $.get "/api/purchaser/tSolutions", {requirementId}, (data) =>
        _.each data, (i)-> i.requirementRank.selectFile = JSON.parse i.requirementRank.selectFile if i.requirementRank and i.requirementRank.selectFile
        @$solutionList.append(techSelectSupplierTemplate({data: data, requirementId}))
        that.resolveSingleSolutionFile("#select-supplier-form")
        $("input[type=file]", "#select-supplier-form").on "click", that.proofUpload
        that.resolveSolutionFile()
        $("#select-supplier-form").validator
          identifier: "input[type=text]"
        that.$selectSupplierForm.on "submit", that.submitQuota

  # 选定供应商上传附件
  proofUpload: ->
    fileSpan = $(@).closest("td").find(".file-name")
    input = $(@).closest("td").find(".file-input")
    attachment = $(@).closest("td")
    $self = $(@)
    $(@).fileupload
      "change": => $self.closest(".btn-upload").spin("small")
      url: "/api/files/upload"
      type: "POST"
      dataType: "json"
      success: (data)->
        fileSpan.text(data.name)
        input.val(data.url)
        input.data "file", {name:data.name, url: data.url}
      complete: =>
        $self.closest(".btn-upload").spin(false)

  # 提交配额，根据类型分发不同的场景
  submitQuota: (evt)->
    evt.preventDefault()
    if $(@).hasClass("js-rank-cost")
      that.SupplierQuotaByCost()
    else
      that.SupplierQuotaByTech()

  # 根据成本进行供应商配额排名
  SupplierQuotaByCost: (e) =>
    sortType = that.$el.find("i.icon-arrowdown-current")?.data("sortType")
    sortType = sortType || 1
    requirementId = $.query.get("requirementId")
    $.post "/api/purchaser/ranks", {requirementId, sortType}, (data) ->
      new Modal({icon: "success", title: "选定供应商成功", content: "选定供应商成功"}).show ->
        window.location.reload()

  # 根据技术进行供应商配额排名
  SupplierQuotaByTech: (e) =>
    requirementRanks = MutualTab.organizeRequirementRank()
    if !_.isEmpty requirementRanks
      $.post "/api/purchaser/ranksT", {requirementRanks: JSON.stringify requirementRanks}, (data) ->
        new Modal({icon: "success", title: "选定供应商成功", content: "选定供应商成功"}).show ->
          window.location.reload()

  # 组织供应商根据技术排名信息
  @organizeRequirementRank: ->
    requirementId = $.query.get("requirementId")
    requirementRanks = _.without (_.map $("input[name=rank]"), (i)->
      if !_.isEmpty $(i).val()
        tr = $(i).closest("tr")
        array = [["requirementId", $(i).closest("tbody").data("id")],["supplierId", $(tr).data("supplierId")], ["supplierName", $(tr).data("supplierName")], ["rank", $(tr).find("input[name=rank]").val()]]
        selectReason = $(tr).find("input[name=selectReason]").val() if !_.isEmpty $(tr).find("input[name=selectReason]").val()
        array.push ["selectReason", selectReason] if !_.isUndefined selectReason
        selectFile = JSON.stringify $(tr).find("input[name=selectFile]").data("file") if !_.isEmpty $(tr).find("input[name=selectFile]").data("file")
        array.push ["selectFile", selectFile] if !_.isUndefined selectFile
        _.object array
      ), undefined


  # 解析方案中的文件
  resolveSolutionFile: ->
    _.each $(".supplier-solution-tr"), (tr)->
      td = $(tr).find(".file-show-td")
      file = $(td).data("file").url
      name = $(td).data("file").name
      $(td).append("""<a href="#{file}">#{name}</a>""")

  # 渲染工厂表头
  renderFactorytable: ->
    size = _.size @$multiRow.data("factory")
    if that.$tab.data("index")
      $("#module-table").css("width", 900 + size * 200)
    else
      $("#module-table").css("width", 1020 + size * 200)

  # 获取模块列表
  getModuleList: (pageNo)->
    self = @
    pageNo = pageNo or 1
    requirementId = $.query.get("requirementId")
    $.get "/api/admin/requirement/#{requirementId}/moduleList?pageNo=#{pageNo}&size=10", (data) ->
      if data.modulePaging.total isnt 0
        $("#module-table tbody").empty()
        _.each data.modulePaging.data, (i)->
          i.module.series = JSON.parse $("#module-table tbody").data("series") if $("#module-table tbody").data("series")
          i.module.user = $("#module-table").data("user")
          i.module.userId = that.$tab.data("userId").toString()
          i.module.status = that.$tab.data("status")
          i.module.units = JSON.parse i.module.units if i.module.units
          i.module.attestationsName = _.pluck(JSON.parse(i.module.attestations),'name').join(",") if i.module.attestations
          $("#module-table tbody").append(moduleItemTemplate({moduleInfo: i}))
        $(".js-edit-select-num", "#module-table").on "click", that.editFactorySelectNumber
        new Pagination($(".module-pagination")).total(data.modulePaging.total).show 10,
          current_page: pageNo - 1
          callback: (pageNo) ->
            self.getModuleList pageNo + 1

  # 手动修改模块工厂的供应商数量
  editFactorySelectNumber: ->
    module = $(@).siblings(".js-edit-module").data("module")
    new Modal(moduleFactoryTemplate(module)).show()
    $(".select-num-form").validator()
    $(".select-num-form").on "submit", that.updateModuleFactorySelectNum

  # 组织更新的数据
  organizeModuleSelectNumInfo: (form)->
    moduleId = $(form).data("id")
    factoryList = _.map $("input[name=selectNum]", ".select-num-form"), (i) ->
      id = $(i).data("id")
      moduleId = moduleId
      selectNum = $(i).val()
      {id, selectNum, moduleId}
    requirementId = $(form).data("req")
    moduleInfoDto = {module: {id: moduleId, requirementId}, factoryList}

  # 获采购商取配额信息
  getPurchaserModuleQuotaList: (pageNo)->
    self = @
    pageNo = pageNo or 1
    requirementId = $.query.get("requirementId")
    $.get "/api/admin/requirement/#{requirementId}/moduleQuota?pageNo=#{pageNo}", (data) ->
      if data.total
        $(".quota-result").removeClass("hide")
        $("#tab-quota-list").empty().append(purchaserModuleQuotaTemplate({data, requirementId}))
        new Pagination($(".quota-pagination")).total(data.total).show 20,
          current_page: pageNo - 1
          callback: (pageNo) ->
            self.getPurchaserModuleQuotaList pageNo + 1

module.exports = MutualTab
