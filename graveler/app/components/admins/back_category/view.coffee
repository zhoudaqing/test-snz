Tip = require "common/tip_and_alert/view"
Modal = require "pokeball/components/modal"
class BackCategory
  sort =
    "backCategory":
      "type": 2
      "maxLength": 3
      "url": "/api/admin/backCategories"
    "frontCategory":
      "type": 1
      "maxLength": 3
      "url": "/api/admin/frontCategories"

  constructor: ->
    @nextLevel = ("li.divide-li")
    @categoryAdd = (".js-add-category")
    @categoryUpdate = (".js-update-category")
    @categoryDelete = (".js-delete-category")
    @categoryEdit = (".js-edit-category")
    @categorySearch = (".js-search-item")
    @categoryEditForm = (".category-edit-form")
    @categoryType = sort[$(".category").data("type")]
    @setHeight(@)
    $(window).resize =>
      @setHeight(@)
    @bindEvent()

  that = this
  categoryItemTemplate = Handlebars.partials["admins/back_category/templates/_category_item"]
  categoryTemplate = Handlebars.templates["admins/back_category/templates/category"]
  attributeManageTemplate = Handlebars.templates["admins/category_manage/templates/attributes_manage"]

  bindEvent: ->
    that = this
    $(".category-list").on "click", @nextLevel, @nextCategory
    $(".category-list").on "click", @categoryAdd, @addCategory
    $(".category-list").on "click", @categoryUpdate, @updateCategory
    $(".category-list").on "click", @categoryEdit, @editCategory
    $(".category-list").on "click", @categoryDelete, @deleteCategory
    $(".category-list").on "keyup", @categorySearch, @categorySearchItem
    $(document).on("click", @stopEdit)
    $(".category-list").on("click", @categoryEditForm, @categoryEditFormClick)
    $(".category-list").on("keyup", @categorySearch, @categorySearchItem)


  setHeight: (that)->
    headerHeight = $(".category-list .category .category-header").height()
    listHeight = $(".category-list").height()
    $(".edit-area").css("height", listHeight - 45)
    $(".category-list .category").css("height", listHeight - 47)
    $(".category-list .category .category-body").css("height", listHeight - headerHeight - 47)
    $(".category-list .divide-ul").css("height", listHeight - headerHeight - 119)

  categorySearchItem: ->
    $(@).closest(".category-body").find(".divide-ul li").hide().filter(":contains('"+( $(this).val() )+"')").show()

  stopEdit: ->
    $(".category-edit-form").hide()
    $(".normal-status").show()

  categoryEditFormClick: (event)->
    event.stopPropagation()

  renderNextCategory: (categoryData, hasChildren, categoryContainer)->
    if hasChildren
      $.ajax
        url: "#{that.categoryType.url}/#{categoryData.id}/children"
        type: "GET"
        success: (data)=>
          $(".category-list").append(categoryTemplate({extras: {"level": parseInt(categoryData.level) + 1, "parentId": categoryData.id, "type": that.categoryType.type}, data: data}))
          that.setHeight(that)
        complete: ->
          $(categoryContainer).spin(false)
    else
      $(".category-list").append(categoryTemplate({extras: {"level": parseInt(categoryData.level) + 1, "parentId": categoryData.id, "type": that.categoryType.type}}))
      that.setHeight(that)
      $(categoryContainer).spin(false)

  backCategoryNextSelect: (categoryData, categoryContainer)->
    status = false
    $.ajax
      url: "/api/admin/backCategories/#{categoryData.id}/properties"
      type: "GET"
      async: false
      success: (data)->
        that.renderEditPanel(categoryData, categoryContainer)
        if !_.isEmpty data
          if categoryData.level is (that.categoryType.maxLength - 1)
            $(".category-#{that.categoryType.maxLength}").addClass("category-last")
          status = true
    status

  schedule: (categoryData, categoryContainer)->
    if categoryData.level < that.categoryType.maxLength
      $(".edit-panel").addClass("disable")
      $(".menu-panel").remove()
      $(".js-spu-new").removeAttr "disabled"
      $(".js-add-attribute").removeAttr "disabled"
      $(".js-add-attribute").css("pointer-events", "all")
      $(".manage-li").removeClass("active")
      $(".attribute-manage").addClass("active")
      $(".manage-div").removeClass("active")
      $("#edit-category").addClass("active")
      $(".front-area").remove()
      if categoryData["hasChildren"] is true
        that.renderNextCategory(categoryData, true, categoryContainer)
      else if that.categoryType.type is 2 and categoryData["hasChildren"] is false
        if !that.backCategoryNextSelect(categoryData, categoryContainer)
          that.renderNextCategory(categoryData, false, categoryContainer)
    else
      if that.categoryType.type is 2
        #$(".category-#{categoryData.level}").addClass("category-last")
        that.renderEditPanel(categoryData, categoryContainer)

  nextCategory: ->
    categoryId = $(@).attr "id"
    $(".spu-manage").removeClass("active")
    $(".attribute-manage").addClass("active")
    $("#edit-category").addClass("active")
    $("#spu-manage").removeClass("active")
    $(".category-id-show").text(categoryId)
    $(this).parents(".category").nextAll(".category").remove()
    categoryData = $(@).data("category")
    $(@).addClass("selected")
    $(@).find(".icon-littlearrow").addClass("hide")
    $(@).find(".icon-whitearrow").removeClass("hide")
    $(@).siblings().removeClass("selected")
    $(@).siblings().find(".icon-whitearrow").addClass("hide")
    $(@).siblings().find(".icon-littlearrow").removeClass("hide")
    $(@).removeClass("mouseover")
    $(@).closest(".category").spin("medium")
    that.schedule(categoryData, $(@).closest(".category"))

  renderEditPanel: (categoryData, categoryContainer)->
    $(".attribute-list").empty()
    keyStatus = false
    attrData = ""
    $.ajax
      url: "/api/admin/backCategories/#{categoryData.id}/properties"
      type: "GET"
      success: (data)->
        keyStatus = true
        $(".attribute-list").append(attributeManageTemplate(data: data))
        #$(".category-#{that.categoryType.maxLength}").addClass("category-last")
        $(".edit-panel").data("categoryid", categoryData.id)
        if keyStatus
          $(categoryContainer).spin(false)
      error: (data)->
        new Modal
          "icon": "error"
          "title": "出错啦！"
          "content": data.responseText || "未知故障"
        .show()
        $(categoryContainer).spin(false)

    $("div.edit-panel #edit-category").attr "category-id", categoryData.id
    $(".edit-panel").removeClass("disable")


  addCategory: ->
    newCategory = $(@).closest(".category-form")
    if newCategory.find(".js-new-category").val() is ""
      new Tip({parent: $(newCategory), type:"error", direct:"down", message: "类目名称不能为空", top: -35}).tip()
      newCategory.find(".js-new-category").focus()
    else
      $.ajax
        url: "#{that.categoryType.url}"
        type: "POST"
        data: newCategory.serialize()
        dataType: "json"
        success: (data)=>
          category = {id: data}
          category["name"] = newCategory.find("input[name=name]").val()
          category["hasChildren"] = false
          category["type"] = that.categoryType.type
          category["level"] = $(@).closest("div.category").data("level")
          category["parentId"] = newCategory.find("input[name=parentId]").val()
          if category["level"] isnt 1
            if $(".category-list").find("li##{category.parentId}").data("category").hasChildren is false
              $(".category-list").find("li##{category.parentId}").data("category").hasChildren = true
              $(".category-list").find("li##{category.parentId}").find(".operate-status.js-delete-category").remove()
          categoryItem = categoryItemTemplate(category)
          $(@).parents("div.bottom-add-category").siblings("ul.divide-ul").append(categoryItem)
          $(".js-new-category").val("")
          $(".front-category-bind").remove()

  updateCategory: ->
    categoryData = $(@).closest("li.divide-li").data("category")
    name = $(@).closest("form.update-category-group").find(".update-category-input").val()
    parent = $(@).closest("li.divide-li")
    if name is ""
      new Tip({parent: $(parent), type:"error", direct:"up", message: "类目名不得为空", top: 40}).tip()
    else
      $.ajax
        url: "#{that.categoryType.url}"
        type: "PUT"
        data: $(@).closest("form.update-category-group").serialize()
        success: =>
          $(@).closest("li.divide-li").find(".update-category-group").css("display", "none")
          $(@).closest("li.divide-li").find(".normal-status").css("display", "block")
          $(@).closest("li.divide-li").find(".item-pop span").text(name)
          $(@).closest("li.divide-li").data("category")["name"] = name

  editCategory: (event)->
    event.stopPropagation()
    $(".update-category-group").css "display", "none"
    $(".normal-status").css "display", "block"
    $(@).closest("li.divide-li").find(".normal-status").css("display", "none")
    unchangedName = $(@).closest("li.divide-li").find(".normal-status .item-pop span").html()
    $(@).closest("li.divide-li").find(".update-category-group").css("display", "block")
    if $(@).closest("li.divide-li").find(".update-category-group").width() < 140
      $(@).closest("li.divide-li").find(".update-category-group .update-category-input").addClass("short-input")
    else
      $(@).closest("li.divide-li").find(".update-category-group .update-category-input").removeClass("short-input")
    $(@).closest("li.divide-li").find(".update-category-group .update-category-input").val(unchangedName)

  deleteCategory: (event)->
    event.stopPropagation()
    url = ""
    categoryItem = $(@).closest("li").data("category")
    $.ajax
      url: "#{that.categoryType.url}/#{categoryItem.id}"
      type: "DELETE"
      success: (data)=>
        if categoryItem.parentId isnt 0 and $(@).closest("li").siblings(".divide-li").length is 0
          $(".category-list").find("li##{categoryItem.parentId}").data("category").hasChildren = false
          $(".category-list").find("li##{categoryItem.parentId}").find("span.operate-span").append("""<span class="operate-status js-delete-category"><i class="icon-del20"></i></span>""")
          $(@).closest(".divide-li").remove()
        else
          $(@).closest(".divide-li").remove()

module.exports = BackCategory
