class PlatformTab
  constructor: ($)->
    path = window.location.pathname
    _.each $(".tab-navs a"), (link) ->
      if $(link).attr("href") is path
        $(link).parent().addClass("active")

module.exports = PlatformTab
