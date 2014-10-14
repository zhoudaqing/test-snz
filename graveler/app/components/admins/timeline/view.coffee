itemTemplate = Handlebars.templates["admins/timeline/templates/item"]

class SupplierResumeTimeline
  constructor: ->
    that = @

    pageNo = 2
    size = 10

    $(window).on "timeline:visible", =>
      setTimeout =>
        @$el.find(".timeline").timeline()
      , 100

    @$el.find(".link-more").click (evt) ->
      evt.preventDefault()
      return if $(@).hasClass("disabled")

      $(@).spin("medium")
      $.ajax
        url: "/api/supplier/timeline"
        type: "GET"
        data:
          userId: $.query.get("userId")
          pageNo: pageNo
          size: size
        success: (data) =>
          timelineData = data.data or []
          if timelineData.length < size
            $(@).addClass("disabled")
          if timelineData.length > 0
            items = _.map timelineData, (item) ->
              $(itemTemplate(item))
            that.$el.find(".timeline").timeline("append", items)
        complete: =>
          $(@).spin(false)
      pageNo++

    if @$el.find(".timeline").data("length") < size or @$el.find(".timeline").data("total") == size
      @$el.find(".link-more").addClass("disabled")

module.exports = SupplierResumeTimeline
