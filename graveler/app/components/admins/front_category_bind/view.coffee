Modal = require "pokeball/components/modal"
TipAndAlert = require "common/tip_and_alert/view"

class categoryBind
  _.extend @::, TipAndAlert
  constructor: ->
    @$bindCategorySelect = $(".js-front-category-select")
    @$bindButton = $(".js-front-category-bind")
    @bindEvent()
    
  that = this
  categoryOperateTemplate = Handlebars.templates["admins/front_category_bind/templates/front_category_bind"]
  categoryBindTemplate = Handlebars.templates["admins/front_category_bind/templates/popup_bind"]

  bindEvent: ->
    that = this
    @$bindCategorySelect.on "click", @renderFirstCategory
    $(document).on "confirm:delete-bind", that.frontCategoryDelete
    @$bindButton.on "click", @saveBindCategory
    $(".front-category-bind").on "click", ".js-delete-binded-category", @deleteBindedCategory

  saveBindCategory: ->
    frontCategoryData = $(".front-category-bind").data("frontid")
    frontCategoriesLevel = frontCategoryData.level
    fcid = frontCategoryData.id
    bcs = JSON.stringify(_.map $(".bind-category-show .binded-label"), (i)-> {id: $(i).data("id"), path: $(i).data("path")})
    $.ajax
      url: "/api/admin/frontCategories/mapping"
      type: "POST"
      data: {fcid, bcs}
      success: (data)->
        $(".front-category-bind").remove()
        $.get "/api/admin/frontCategories/#{fcid}/mapping", (data)->
          if _.isEmpty data
            data = ""
          json = {"data": data, "frontCategoryData": frontCategoryData}
          categoryBind = categoryOperateTemplate({"data": json})
          if frontCategoriesLevel < 3
            $(".category-#{frontCategoriesLevel}").nextAll(".category").remove()
          $(".front-area").remove()
          $(".edit-area").append("""<div class="front-area"></div>""")
          $(".front-area").append(categoryBind)
          bindComponent = require("admins/front_category_bind/view")
          new bindComponent()
          $(".category-#{frontCategoriesLevel} .selected .js-delete-category").hide()

  renderFirstCategory: ->
    $.get "/api/admin/backCategories/0/children", (data) ->
      popCategory = categoryBindTemplate({extras: {"level": 1, "parentId": 0}, data: data})
      new Modal(popCategory).show()
      that.selectBackCategory()
      Component = require($(".popup-bind.js-comp").data("comp-path"))
      new Component("backCategories")

  selectBackCategory: ->
    $(".bind-category-pop").on "click", ".js-bind-front-category", ->
      selectedItem = $(".pop-main").find(".selected")
      length = $(".pop-main .selected").length
      backCategoriesItem = _.map $(".pop-main .selected"), (i)-> $(i).data("category")
      if _.isEmpty backCategoriesItem
        that.alert ".bind-category-pop", "info", "小贴士" , "请选择叶子类目"
        $(".alert").css("top", 160).css("left", 300)
      else
        if backCategoriesItem[length - 1].hasChildren
          that.alert ".bind-category-pop", "info", "小贴士" , "请选择叶子类目"
          $(".alert").css("top", 160).css("left", 300)
        else
          backCategoriesId = backCategoriesItem[length - 1].id
          stringCache = _.map selectedItem, (i)-> $(i).find(".item-pop span").html()
          selectString = stringCache.join(" > ")
          $(".bind-category-show").append("""<label class="binded-label" data-id="#{backCategoriesId}" data-path="#{selectString}"><a href="javascript:void(0);" class="js-delete-binded-category"><i class="icon-trash-small"></i></a>&nbsp;&nbsp;&nbsp;已选择: <span>#{selectString}</span></label>""")
          $(".js-delete-binded-category").on "click", that.deleteBindedCategory
          that.canSaveBindCategory()
          $(".modal-footer .close").trigger("click")

  deleteBindedCategory: ->
    $(@).closest("label").remove()

  canSaveBindCategory: ->
    $(".front-category-bind .js-front-category-bind.hide").removeClass("hide")

  frontCategoryDelete: ->
    frontCategoryData = $(".front-category-bind").data("frontid")
    frontCategoriesId = frontCategoryData.id
    frontCategoriesLevel = frontCategoryData.level
    $.ajax
      url: "/api/admin/frontCategories/#{frontCategoriesId}/mapping"
      type: "DELETE"
      async: false
      success: ->
        json = {"data": "", "frontCategoryData": frontCategoryData}
        categoryBind = categoryOperateTemplate({"data": json})
        $(".front-area").remove()
        if frontCategoriesLevel = 3
          $(".edit-area").append("""<div class="front-area"></div>""")
          $(".front-area").append(categoryBind)
        bindComponent = require("admins/front_category_bind/view")
        new bindComponent()

module.exports = categoryBind
