Modal = require "pokeball/components/modal"
Tip = require "common/tip_and_alert/view"

class CategoryManage
  _.extend @::, Tip
  constructor: ->
    @switchPanel = (".tab a.switch-panel")
    @attributeUpdate = (".js-update-attribute")
    @editCancel = (".js-edit-cancel")
    @attrValueEdit = (".js-attr-edit")
    @menuCancel = (".js-menu-cancel")
    @attrAdd = (".js-add-attribute")
    @attrDelete = (".js-delete-attribute")
    @attrValueWatch = (".js-attr-value-get")
    @isSpu = "input[name=isSpu]"
    @isSku = "input[name=isSku]"
    @jsMenuEdit = ".js-menu-edit"
    @brandSearch = "input.js-search-brand"

    @bindEvent()
  attrValueUpdateTemplate = Handlebars.templates["admins/category_manage/templates/attribute_value_update"]
  attrValueViewTemplate = Handlebars.templates["admins/category_manage/templates/attribute_value_view"]
  spuPropertiesViewTemplate = Handlebars.templates["admins/category_manage/templates/spu_properties_view"]
  spuNewTemplate = Handlebars.templates["admins/category_manage/templates/createSPU"]
  attrValueItemTemplate = Handlebars.partials["admins/category_manage/templates/_attribute_value_item"]
  attrCreateTemplate = Handlebars.templates["admins/category_manage/templates/attribute_create"]
  spuItemTemplate = Handlebars.partials["admins/category_manage/templates/_spu_item"]

  that = this

  bindEvent: ->
    that = this
    $(".edit-panel").on "click", @switchPanel, @switchTab
    $(".edit-panel").on "click", @attrValueWatch, @renderAttributeValue
    $(".edit-panel").on "click", @attributeUpdate, @updateAttribute
    $(".edit-panel").on "click", @editCancel, @updateCancel
    $(".edit-panel").on "click", @attrValueEdit, @renderAttrValueEdit
    $(document).on "click", @attrAdd, @addAttribute
    $(".edit-panel").on "click", ".js-attribute-cancel", @removeAddAttribute
    $(".edit-panel").on "change", @isSku, @getSkuProperty
    $(".edit-panel").on "click", @jsMenuEdit, @updateAttributeValue
    $(document).on "confirm:deleteAttribute", @confirmDeleteAttribute
    $(document).on "confirm:deleteSpu", @confirmDeleteSpu
    $(document).on "mouseout", ".dropdown-group", @hideDropdownMenu
    $(document).on "mouseover", ".dropdown-group", @showDropDownMenu
    $(document).on "keyup", @brandSearch, @searchBrand

  searchBrand: ->
    if !_.isEmpty $(@).val()
      $(@).closest(".select-spu-brand").find("#spu-brand-id option").prop("selected", false).filter(":contains('"+( $(@).val())+"')").prop("selected", true)

  showDropDownMenu: ->
    $(@).find(".dropdown-title").css("z-index", 99)
    $(@).find(".dropdown-menu").removeClass("hide")
    height = $(@).find(".dropdown-btn").height() + $(@).find(".dropdown-menu").height()
    $(@).find(".dropdown-title").css("height", height)
    $(@).closest("li").siblings("li").css("pointer-events", "none")

  hideDropdownMenu: ->
    $(@).find(".dropdown-title").css("z-index", 9)
    $(@).find(".dropdown-menu").addClass("hide")
    height = $(@).find(".dropdown-btn").height()
    $(@).find(".dropdown-title").css("height", height)
    $(@).closest("li").siblings("li").css("pointer-events", "all")

  switchTab: (event)->
    event.preventDefault()
    $(@).parent().addClass "active"
    $(@).parent().siblings().removeClass "active"
    $("div#{$(@).attr "href"}").siblings().find("button").removeAttr "disabled"
    $("div#{$(@).attr "href"}").siblings().find(".menu-panel").remove()
    $("div#{$(@).attr "href"}").siblings().find(".spu-list").removeAttr "pointer-events"
    $("div#{$(@).attr "href"}").addClass "active"
    $("div#{$(@).attr "href"}").siblings().removeClass "active"

  confirmDeleteAttribute: (event, data)->
    closestLi = $("#edit-category ##{data}").closest("li")
    id = closestLi.find(".attribute").data("id")
    categoryId = $("#edit-category").attr "category-id"
    $.ajax
      url: "/api/admin/backProperties/#{id}"
      type: "DELETE"
      success: =>
        closestLi.remove()
        _.each $(".attribute-li"), (li, i)->
          $(li).find(".attribute-number").text(i + 1)


  addAttribute: ->
    categoryId = $(@).parent().attr "category-id"
    $(@).attr "disabled", true
    $(@).siblings("ul").prepend(attrCreateTemplate())
    $(".add-attr-li").closest("#edit-category").find(".js-add-attribute").css("pointer-events", "none")
    $(".add-attr-li").closest("ul").find(".attribute-li").css("pointer-events", "none")
    $(".js-create-attribute").on "click", ->
      attributes = []
      _.each $(@).closest(".add-attr-li").siblings(".attribute-li"), (li)->
        attributes.push $(li).find(".attr").data("name")
      form = $(@).parents(".add-attribute-form")
      if $(".attribute-li").length is 1
        parent = form.find(".input-group")
        new Tip({parent:$(parent), type:"error", direct:"up", message:"最多一项属性"}).tip()
        $(".tip").css("top", 35)
      else if form.find(".update-category-input").val() is ""
        parent = form.find(".input-group")
        new Tip({parent:$(parent), type:"error", direct:"up", message:"属性名不能为空"}).tip()
        $(".tip").css("top", 35)
      else if _.contains attributes, $(@).closest(".input-group").find("input.update-category-input").val()
        parent = form.find(".input-group")
        new Tip({parent:$(parent), type:"error", direct:"up", message:"属性重名"}).tip()
        $(".tip").css("top", 35)
      else if $(@).closest(".input-group").find("input.update-category-input").val() is "品牌"
        parent = form.find(".input-group")
        new Tip({parent:$(parent), type:"error", direct:"up", message:"属性名不能为品牌"}).tip()
        $(".tip").css("top", 35)
      else if $(@).closest(".input-group").find("input.update-category-input").val().length > 5
        parent = form.find(".input-group")
        new Tip({parent:$(parent), type:"error", direct:"up", message:"属性名长度大于5"}).tip()
        $(".tip").css("top", 35)
      else
        property = form.serializeObject()
        property.bcId = $(".edit-panel").data("categoryid")
        $.ajax
          url: "/api/admin/backProperties"
          type: "POST"
          dataType: "json"
          data: {"property": JSON.stringify(property)}
          success: (propertyId)=>
            attributeLength = $(".attribute-li").length
            property.index = attributeLength + 1
            property.id = propertyId
            categoryKeyTemplate = Handlebars.partials["admins/category_manage/templates/_back_category_key"](property)
            $(".add-attr-li").closest("ul").find(".attribute-li").css("pointer-events", "all")
            $(".add-attr-li").closest("#edit-category").find(".js-add-attribute").css("pointer-events", "all")
            $("#edit-category ul .add-attr-li").remove()
            $("#edit-category ul").append(categoryKeyTemplate)
            $(".js-add-attribute").removeAttr "disabled"

  removeAddAttribute: ->
    $(".add-attr-li").closest("ul").find(".attribute-li").css("pointer-events", "all")
    $(".add-attr-li").closest("#edit-category").find(".js-add-attribute").css("pointer-events", "all")
    $(".js-add-attribute").removeAttr "disabled"
    $(@).parents(".add-attr-li").remove()
    if $(".add-attribute-form").length > 0
      $(".add-attribute-form").closest("ul").find(".attribute-li").css("pointer-events", "none")

  renderSPUAttribute: ->
    spu = $(@).closest("li").data("spu")
    brandStatus = false
    attributeStatus = false
    brandName = ""
    attributes = {}
    $.get "/api/admin/spus/#{spu.id}/brand", (data)=>
      brandName = data.data.name
      brandStatus = true
      if brandStatus and attributeStatus
        $(@).parent().append(spuPropertiesViewTemplate(data:{brandName:brandName, attributes: attributes}))
        $(".dropdown-menu").addClass("hide")
        $(".menu-panel").attr "disabled", false
        $(".menu-panel").css("left", "0em").css "width", $(@).parent().parent().width()
        $(".menu-panel").on "mouseleave", ->
          $(@).remove()
    $.get "/api/admin/spus/#{spu.id}", (data)=>
      attributeStatus = true
      attributes = data.data.attributes
      if brandStatus and attributeStatus
        $(@).parent().append(spuPropertiesViewTemplate(data:{brandName:brandName, attributes: attributes}))
        $(".dropdown-menu").addClass("hide")
        $(".menu-panel").attr "disabled", false
        $(".menu-panel").css("left", "0em").css "width", $(@).parent().parent().width()
        $(".menu-panel").on "mouseleave", ->
          $(@).remove()

  renderAttributeValue: ->
    attributeKeyId = $(@).closest("li").data("attribute").id
    $.get "/api/admin/backProperties/#{attributeKeyId}", (data)=>
      if _.isEmpty(data)
        data = [{"id":-1,"value": "属性值为空 请添加属性值"}]
      $(@).parent().append(attrValueViewTemplate(data:data))
      $(".menu-panel").attr "disabled", false
      $(".menu-panel").css("left", "0em").css "width", $(@).parent().width()
      $(".menu-panel").on "mouseleave", ->
        $(@).remove()


  getSkuProperty: ->
    if $(@).prop('checked')
      $(@).parents('.spu-property-list:first').find('*[name="value"]').attr("disabled", true).css("display", "none")
    else
      $(@).parents('.spu-property-list:first').find('*[name="value"]').removeAttr("disabled").css("display", "inline")

  updateAttribute: ->
    li = $(@).closest("li")
    li.siblings().css("pointer-events", "none")
    text = li.find("span.attr").text()
    $(@).closest("li").find("span.attr").replaceWith("""<span class="attr"><input class="input-small" type="text" name="" placeholder="请输入属性" value="#{text}"></span>""")
    $(@).closest("li").find("span.is-enum").prepend("""<input type="checkbox" value="">""")
    li.find("span.operate.edit-status").addClass("active-status").removeClass("disable-status")
    li.find(".operate.on-hover").addClass("disable-status").removeClass("active-status")
    li.find(".end").addClass("disable-status")

  updateCancel: ->
    li = $(@).closest("li")
    li.siblings().css("pointer-events", "all")
    text = li.find("span.attr>input").val()
    $(@).closest("li").find("span.attr").replaceWith("""<span class="attr">#{text}</span>""")
    $(@).closest("li").find("span.is-enum > input[type=checkbox]").remove()
    li.find("span.operate.edit-status").addClass("disable-status").removeClass("active-status")
    li.find(".operate.on-hover").removeClass("disable-status")
    li.find(".end").removeClass("disable-status")

  renderAttrValueEdit: ->
    categoryId = $("#edit-category").attr "category-id"
    attributeKeyId = $(@).closest("li").data("attribute").id
    $(@).closest("li").siblings("li").css("pointer-events", "none")
    $(@).closest(".operate.on-hover").addClass("active-status")
    $.get "/api/admin/backProperties/#{attributeKeyId}", (data)=>
      $(@).parent().append(attrValueUpdateTemplate(data:data))
      $(".menu-panel").css("left", "0em").css "width", $(@).parent().width()
      $(document).on "click", ".js-menu-cancel", ->
        $(".operate.on-hover").removeClass("active-status")
        $(".menu-panel").remove()
        $("li.attribute-li").css("pointer-events", "all")
      $(".menu-panel").on "click", ".js-add-attr-value", ->
        status = true
        for i in $("input.attribute-show")
          if $(".properties-edit-form").find("input[name=data]").val() is $(i).val()
            that.tip $(@).closest("form"), "error", "up", "类目属性值重名！"
            $(".tip").css("top", 35)
            status = false
            break
        if status
          $(@).closest(".properties-edit").spin("medium")
          $.ajax
            url: "/api/admin/categories/#{categoryId}/keys/#{attributeKeyId}/values"
            type: "POST"
            data: $(@).closest(".properties-edit-form").serialize()
            success: (data) =>
              $(@).closest(".properties-edit").find(".add-properties-li").before(attrValueItemTemplate(data.data))
              $(@).parent().siblings("input[name=data]").val("")
            complete: =>
              $(@).closest(".properties-edit").spin(false)

  updateAttributeValue: (event)=>
    id = $(".menu-panel").data("id")
    property = {value1: $("input[name=value1]").val(), value2: $("input[name=value2]").val(), value3: $("input[name=value3]").val()}
    $(".menu-panel").spin("small")
    $.ajax
      url: "/api/admin/backProperties/#{id}"
      type: "PUT"
      data: {property: JSON.stringify(property)}
      success: (data)->
        new Tip({parent:$(".js-menu-edit"), type:"success", direct:"up", message:"修改属性成功"}).tip()
        $(".tip").css("top", 35).css("width", "100px")
      complete: ->
        $(".menu-panel").spin(false)


    $(document).on "click", ".js-attribute-value-delete", ->
      attrValueData = $(@).closest("li").data("attrValue")
      $.ajax
        url: "/api/admin/categories/#{categoryId}/keys/#{attributeKeyId}/values/#{attrValueData.id}"
        type: "DELETE"
        success: =>
          $(@).closest("li").remove()

module.exports = CategoryManage
