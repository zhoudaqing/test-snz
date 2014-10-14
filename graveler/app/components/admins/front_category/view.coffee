TipAndAlert = require "common/tip_and_alert/view"
Modal = require "pokeball/components/modal"
BackCategory = require "admins/back_category/view"
class FrontCategory extends BackCategory
  _.extend @::, TipAndAlert
  sort =
    "backCategory":
      "type": 2
      "maxLength": 4
      "url": "/api/admin/backCategories"
    "frontCategory":
      "type": 1
      "maxLength": 4
      "url": "/api/admin/frontCategories"

  constructor: ->
    @categoryType = sort[$(".category").data("type")]
    super

  that = this
  categoryItemTemplate = Handlebars.partials["admins/back_category/templates/_category_item"]
  categoryTemplate = Handlebars.templates["admins/back_category/templates/category"]
  categoryOperateTemplate = Handlebars.templates["admins/front_category_bind/templates/front_category_bind"]

  bindEvent: ->
    that = this
    super()

  popFrontBindModule: (categoryData, categoryContainer)->
    status = true
    $.ajax
      url: "/api/admin/frontCategories/#{categoryData.id}/mapping"
      type: "GET"
      async: false
      success: (bindData)->
        if _.isEmpty bindData
          $(".front-area").remove()
          json = {"data": "", "frontCategoryData": categoryData}
          if categoryData.level is (that.categoryType.maxLength - 1)
            $(".category-#{that.categoryType.maxLength}").addClass("category-last")
        else
          level = parseInt(categoryData.level) + 1
          $(".category-#{categoryData.level} .selected .js-delete-category").hide()
          $(".category-#{level}").remove()
          json = {"data": bindData, "frontCategoryData": categoryData}
        categoryBind = categoryOperateTemplate({"data": json})
        $(".front-area").remove()
        $(".edit-area").append("""<div class="front-area"></div>""")
        $(".front-area").append(categoryBind)
        bindComponent = require("admins/front_category_bind/view")
        new bindComponent()
      complete: ->
        $(categoryContainer).spin(false)

  schedule: (categoryData, categoryContainer)->
    if categoryData.level < that.categoryType.maxLength
      $("#edit-category").addClass("active")
      $(".front-area").remove()
      if categoryData["hasChildren"] is true
        that.renderNextCategory(categoryData, true, categoryContainer)
      else
        that.renderNextCategory(categoryData, false, categoryContainer)
        that.popFrontBindModule(categoryData, categoryContainer)
    else
      $(".category-#{categoryData.level}").addClass("category-last")
      that.popFrontBindModule(categoryData, categoryContainer)

  nextCategory: ->
    categoryId = $(@).attr "id"
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

module.exports = FrontCategory
