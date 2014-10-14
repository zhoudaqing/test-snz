recentSoldTemplate = Handlebars.templates["admins/qualification_info/templates/recent_sold"]

module.exports = class SupplierResumeQualificationInfo
  constructor: ->
    $el = @$el
    @$el.find("[data-recent-sold]").each ->
      $(@).append(recentSoldTemplate({content: $(@).data("recentSold")}))
    # render country
    $.ajax
      url: "/api/countries"
      type: "GET"
      success: (data) ->
        $el.find("[data-country]").each ->
          if data[$(@).data("country") - 1]
            $(@).text(data[$(@).data("country") - 1].name)
