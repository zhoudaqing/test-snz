Modal = require "pokeball/components/modal"
Tip = require "common/tip_and_alert/view"
Pagination = require "pokeball/components/pagination"
newModuleTemplate = Handlebars.templates["purchaser/demand_module/templates/module_edit"]
moduleItemTemplate = Handlebars.templates["purchaser/demand_module/templates/module_item"]
unitTemplate = Handlebars.templates["purchaser/demand_module/templates/select_unit"]
identifiesTemplate = Handlebars.templates["purchaser/demand_module/templates/identifies_list"]

class DemandModule
  constructor: ($)->
    @$target = @$el
    @$newModuleAdd = $(".js-add-new-module")
    @$multiRow = $(".multi-row")
    @$inputFile = $("input[type=file]")
    @bindEvents()

  that = this

  bindEvents: ->
    that = this
    new Pagination(".demand-pagination").total($(".demand-pagination").data('total')).show(10)
    @renderFactorytable()
    @$newModuleAdd.on "click", @addNewModule
    $(document).on "confirm:delete-module", @deleteModule
    $(document).on "confirm:lock-requirement", @lockRequirement
    @$target.on "click", ".js-edit-module", @editModule
    @$inputFile.on "click", @fileUpload

  #上传文件
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

  # 添加新模块
  addNewModule: ->
    seriesIds = $(@).data("series")
    factories = that.$multiRow.data("factory")
    id = $(@).data("id")
    new Modal(newModuleTemplate({module: {requirementId:id, factory:factories, seriesIds: seriesIds}})).show()
    $("#cost-group").append(unitTemplate({hasQuantity: true}))
    $("#delivery-group").append(unitTemplate())
    $(".resource-input").after(unitTemplate())
    that.getModuleIdentifies({})
    initEvent("create")
    $(".js-search-module").on "click", that.getModuleNumberInfo

  # 绑定部分弹窗事件
  initEvent = (status)->
    $(".datepicker").datepicker()
    $(".module-form").validator
      identifier: "input"
      isErrorOnParent: true
    $(".module-form").on "submit", that.submitSingleModule
    $("#module-category", ".module-form").on "change", that.selectModuleCategory
    $("#module-category").trigger "change"
    #添加认证多选
    $(".js-select-multiple").on "click",that.getModuleIdentifiesMultiple
    that.checkProductScope()

  # 根据专用号查专用号信息
  getModuleNumberInfo: ->
    moduleNum = $(@).closest(".control-group").find("input[name=moduleNum]").val()
    if !_.isEmpty moduleNum
      $.get "/api/purchaser/requirement/moduleInfo/#{moduleNum}", (data)=>
        if _.isEmpty data
          new Tip({parent: $(@).closest(".control-group"), direct:"up", type: "info", message: "未找到专用号", left: 130, top: 35}).tip()
        else
          if _.contains(_.map($(".module-form").data("series"), (i)-> i.name), data.seriesName)
            $("input[name=moduleNum]").data("num", moduleNum)
            $("#module-category").attr("disabled", true).find("option[value=#{data.seriesId}]").prop "selected", true
            $("#module-category").trigger "change"
            $("input[name=moduleName]").val(data.moduleName).attr "readonly", true
            $(".unit-sales").attr("disabled", true).find("option[data-name=#{data.unit}]").prop "selected", true
          else
            new Tip({parent: $(@).closest(".control-group"), direct:"up", type: "info", message: "专用号类目不匹配需求类目", left: 130, top: 35}).tip()

  #展示多选
  getModuleIdentifiesMultiple: (@identify)->
    $("#identifies-select-menu").remove()
    _self = @
    if $("#attestation-select-multiple").data("identifies")
      that.bindMutipleSelect $("#attestation-select-multiple").data("identifies"),_self
    else
      $.ajax
        url: "/api/purchaser/module/identifies"
        type: "GET"
        success: (data)->
          that.bindMutipleSelect(data, _self)

  #展示认证多选当中的数据，并绑定事件
  bindMutipleSelect: (data, _self)->
    $(_self).parent().append(identifiesTemplate({data:data}))
    $("#attestation-select-multiple").data("identifies",data)
    $(".js-save-identifies-scope", "#identifies-select-menu").on "click", that.saveIdentifiesScope
    $(".close", "#identifies-select-menu").on "click", that.closeScopeSelect
    that.checkProductScope()

  #校验多选当中的选中项
  checkProductScope: ->
    # seriesIds = $("#attestation-select-multiple").data("id")
    # if !_.isEmpty(seriesIds)
    #   _.each seriesIds.split(","), (i)->
    #     $("#identifies-select-menu input[value=#{i}]").prop "checked", true
    #   if _.isEmpty $(".attestation-show").text()
    #     seriesName = _.map $("#identifies-select-menu").find("input:checked"), (i)->
    #       name = $(i).data("name")
    #     $(".attestation-show").text(seriesName.join(",")).attr "title", seriesName.join(",")

    seriesIds = $("#attestation-select-multiple").data("checkdata")
    if !_.isUndefined seriesIds
      _.each seriesIds, (i)->
        $("#identifies-select-menu input[value=#{i.id}]").prop "checked", true
      seriesName = _.map seriesIds, (i)-> i.name
      $(".attestation-show").text(seriesName.join(",")).attr "title", seriesName.join(",")

  #多选当中的确定按钮事件
  saveIdentifiesScope: ->
    seriesIds = _.map $(@).closest("#identifies-select-menu").find("input:checked"), (i)->
      id = $(i).val()
      name = $(i).data("name")
      {id, name}
    if !_.isEmpty seriesIds
      seriesName = _.map seriesIds, (i)-> i.name
      seriesId = _.map seriesIds, (i)-> i.id
      $(".attestation-show").text(seriesName.join(",")).attr "title", seriesName.join(",")
      $(@).closest("#attestation-select-multiple").data("id", seriesId)
      $(@).closest("#attestation-select-multiple").data("checkdata", seriesIds)
      $(@).closest(".menu").remove()

  #多选当中取消按钮事件
  closeScopeSelect: ->
    $(@).closest(".menu").remove()

  #验证是否选中多选
  validateIdentifiesScope: (enable)->
    if enable
      status = true
      seriesIds = $("#attestation-select-multiple").data("checkdata")
      if _.isUndefined seriesIds
        new Tip({parent:$("#attestation-select-multiple").closest(".control-group"), direct: "up", type: "info", message: "请选择认证", left:130, top:35}).tip()
        status = false
      status
    else
      true

  # 选择模块类目
  selectModuleCategory: ->
    categoryId = $(@).val()
    $.ajax
      url: "/api/category/backendCategory/#{categoryId}/properties"
      type: "GET"
      success: (data)->
        $("#category-property").empty()
        if !_.isEmpty data
          that.properties = data
          _.each data, (i)->
            $("""<option value="#{i.id}">#{i.name}</option>""").data("property", i).appendTo("#category-property")
            $(".resource-input[data-num=#{i.factoryNum}]").data("property", i.id).data("value1", i.value1).data("value2", i.value2)
          $("#category-property option[value=#{$("#category-property").data("property")}]").prop("selected", true)
          _.each $(".resource-input"), (i)-> unless $(i).data("property") then $(i).data("property", $("#category-property").val())
          that.validateValues($("#category-property option:selected"))
            # that.validateModuleAttributeWithFactory(data)
        else
          new Modal({icon: "error", content: "模块对应的类目暂无资源量标准，请联系平台运营"}).show()

  # 验证模块类目属性是否有对应的工厂信息
  validateModuleAttributeWithFactory: (data)->
    factoryList = _.map $(".resource-input"), (i)-> $(i).data("num").toString()
    moduleFactories = _.without (_.map data, (i)-> i.factoryNum), undefined
    commonFactories = _.intersection(moduleFactories, factoryList)
    if !_.isEmpty(_.difference(commonFactories, factoryList)) or !_.isEmpty(_.difference(factoryList, commonFactories))
      new Tip({parent:$("#module-category").closest(".control-group"), direct: "up", type: "info", message: "请联系平台运营完善类目对应资源量标准", left:130, top:35}).tip()
      return false
    return false

  # 选择模块类目属性
  selectCategoryProperty: ->
    property = $(@).find("option:selected")
    _.each property.data("property"), (i)->
      $(@).closest(".resource-group").find(".resource-input").data("property", property.data("property").id)

  # 验证模块类目属性是否有最小量信息
  validateValues: (property)->
    if _.isEmpty(property.data("property").value1)
      new Tip({parent:property.closest(".control-group"), direct: "up", type: "info", message: "请联系平台运营完善类目对应资源量标准", left:130, top:35}).tip()
      return false
    return true

  # 校验模块资源量
  validateModuleResource: ->
    status = that.validateValues($("#category-property option:selected"))
    minTotal = $("#category-property option:selected").data("property").value1
    realTotal = 0
    _.each $(".resource-input"), (i)-> realTotal += parseInt $(i).val()
    if realTotal < minTotal
      new Tip({parent: $(".resource-input:last").parent(), direct: "up", type: "info", message: "模块的资源量需要大于对应模块类的最小量", left: 30, top: 30, width:250}).tip()
      status = false
    status

  # 渲染工厂表格
  renderFactorytable: ->
    size = _.size @$multiRow.data("factory")
    $("#module-table").css("width", 1020 + size * 100)
    that.renderFactoryResource()

  # 渲染模块列表
  renderFactoryResource: ->
    _.each $(".module-tr"), (i)->
      moduleInfo = $(i).data("module")
      # module.resource = JSON.parse module.resourceNum
      moduleInfo.module.status = $("#module-table tbody").data("status")
      moduleInfo.module.checkResult = $("#module-table tbody").data("check")
      moduleInfo.module.units = JSON.parse moduleInfo.module.units if moduleInfo.module.units
      moduleInfo.module.attestationsName = _.pluck(JSON.parse(moduleInfo.module.attestations),'name').join(",") if moduleInfo.module.attestations
      $(i).append(moduleItemTemplate({moduleInfo: moduleInfo}))

  # 获取模块认证信息
  getModuleIdentifies: (@identify)->
    if !_.isEmpty @identify
      id = @identify.id
    $.ajax
      url: "/api/purchaser/module/identifies"
      type: "GET"
      success: (data)->
        _.each data, (i)->
          $("#attestation-select").append("""<option value="#{i.id}" data-name="#{i.name}">#{i.name}</option>""")
        $("#attestation-select option[value=#{id}]").prop "selected", true if !_.isUndefined id

  # 编辑模块
  editModule: ->
    moduleInfo = $(@).data("module")
    moduleInfo.module.seriesIds = $(@).closest("tr").data("series")
    new Modal(newModuleTemplate(moduleInfo)).show()
    $(".module-form").data("units", JSON.stringify moduleInfo.module.units)
    that.getModuleIdentifies({id: module.attestationId})
    initEvent("update")

  # 删除模块
  deleteModule: (evt,data)->
    moduleId = data
    $.ajax
      url: "/api/purchaser/requirement/module/#{moduleId}"
      type: "DELETE"
      success: ->
        window.location.reload()

  # 锁定需求
  lockRequirement: (evt, data)->
    requirementId = data
    $.ajax
      url: "/api/purchaser/requirement/#{requirementId}/transitionStatus"
      type: "POST"
      success: (data)->
        window.location.href = "/purchaser/solution-mutual?requirementId=#{requirementId}"

  # units 结构为 {cost:{salesId: 1, salesName: EA, quantityId:2, quantityName: 10}, delivery:{salesId: 1, salesName: EA}}
  organizeModuleUnitInfo: (form)->
    if $(form).data("units")
      $(form).data("units")
    else
      costSalesId = $(form).find("#cost-group .unit-sales").val()
      costSalesName = $(form).find("#cost-group .unit-sales option:selected").data("name")
      costQuantityId = $(form).find("#cost-group .unit-quantity").val()
      costQuantityName = $(form).find("#cost-group .unit-quantity option:selected").data("name")
      cost = {salesId: costSalesId, salesName:costSalesName, quantityId: costQuantityId, quantityName: costQuantityName}
      deliverySalesId = $(form).find("#delivery-group .unit-sales").val()
      deliverySalesName = $(form).find("#delivery-group .unit-sales option:selected").data("name")
      delivery = {salesId: deliverySalesId, salesName:deliverySalesName}
      JSON.stringify {cost, delivery}

  # 组织工厂数据
  organizeModuleFactory: (form)->
    module = that.organizeModuleInfo(form)
    resourceTotal = 0
    factory = _.map $(form).find(".resource-group"), (i)->
      moduleId = module.id if module.id
      id = $(i).data("id") if $(i).data("id")
      $input = $(i).find(".resource-input")
      $unitSales = $(i).find(".unit-sales")
      resourceTotal += parseInt $input.val()
      {id, moduleId, factoryNum: $input.data("num"), factoryName:$input.data("name"), propertyId: $input.data("property"), resourceNum:$input.val(), salesId:$unitSales.val(),salesName: $unitSales.find("option:selected").data("name")}
    factoryList = {factory, total: resourceTotal}

  # 组织模块基础信息
  organizeModuleInfo: (form)->
    module = $(form).serializeObject()
    module.cost *= 100
    module.moduleNum = $(form).find("input[name=moduleNum]").data("num") || ""
    module.seriesId = $(form).find("#module-category").val()
    module.seriesName = $(form).find("#module-category option:selected").data("name")
    module.units = that.organizeModuleUnitInfo(form)
    # module.attestation = $(form).find("#attestation-select option:selected").data("name")
    #如果多选后台开始支持放开下面一行代码，注掉上面一行代码
    module.attestations = JSON.stringify $(form).find("#attestation-select-multiple").data("checkdata")
    module

  # 提交单个模块
  submitSingleModule: (evt)->
    evt.preventDefault()
    module = that.organizeModuleInfo($(@))
    factory = that.organizeModuleFactory($(@))
    module.total = factory.total
    factoryList = factory.factory
    moduleInfoDto = {module, factoryList}

    if that.validateIdentifiesScope(true) and that.validateModuleResource()
      if module.id
        url = "/api/purchaser/requirement/module/update"
        type = "PUT"
      else
        url = "/api/purchaser/requirement/module/create"
        type = "POST"
      $.ajax
        url: url
        type: type
        data: {moduleInfoDto: JSON.stringify(moduleInfoDto)}
        success: ->
          window.location.reload()

module.exports = DemandModule
