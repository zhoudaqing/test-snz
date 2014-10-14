customerTemplate = Handlebars.templates["admins/company_info/templates/customer"]
factoryTemplate = Handlebars.templates["admins/company_info/templates/factory"]

module.exports = class SupplierResumeCompanyInfo
  constructor: ->
    $el = @$el

    # render customer
    $el.find("[data-customer]").each ->
      $(@).append(customerTemplate({content: $(@).data("customer"), url: $(@).data("url")}))

    # render factory
    $el.find("[data-factory]").each ->
      $(@).append(factoryTemplate({data: $(@).data("factory")}))

    # render country
    $.ajax
      url: "/api/countries" 
      type: "GET"
      success: (data) ->
        $el.find("[data-country]").each ->
          $(@).text(data[$(@).data("country") - 1].name)

    # render province
    $.ajax
      url: "/api/provinces"
      type: "GET"
      success: (data) ->
        $el.find("[data-province]").each ->
          _.each data, (province) =>
            if province.id == $(@).data("province")
              $(@).text(province.name)
              return false

    # render city
    $el.find("[data-city]").each ->
      $target = $(@)
      cityId = $(@).data("city")
      provinceId = $(@).data("parent")

      if provinceId
        $.ajax
          url: "/api/provinces/#{provinceId}/cities"
          type: "GET"
          success: (data) ->
            _.each data, (city) ->
              if city.id == cityId
                $target.text(city.name)

    # render business
    $el.find("[data-business]").each ->
      $target = $(@)

      $.ajax
        url: "/api/category/ancestors/#{$target.data("business")}"
        type: "GET"
        success: (data) ->
          $target.text("#{data[2].name} > #{data[1].name} > #{data[0].name}")
