class Sidebar
  constructor: ($)->
    @sidebarSelect = $(".sidebar-li-a")
    @bindEvent()

  bindEvent: ->
    @getLocal()
    @sidebarSelect.on "click", @selectMenu

  selectMenu: ->
    $(@).siblings(".sidebar-inner-ul").toggle(100)
    if !$(@).siblings(".sidebar-inner-ul").hasClass("expand")
      $(@).siblings(".sidebar-inner-ul").addClass("expand")
      $(@).find("i").removeClass("icon-jian8").addClass("icon-jia8")
    else
      $(@).siblings(".sidebar-inner-ul").removeClass("expand")
      $(@).find("i").removeClass("icon-jia8").addClass("icon-jian8")

  getLocal: ->
    local = window.location.pathname
    $(""".inner-item-a[href="#{local}"]""").closest(".sidebar-inner-ul")
    $(""".inner-item-a[href="#{local}"]""").closest(".sidebar-inner-ul").siblings(".sidebar-li-a").find("i").removeClass("icon-plus").addClass("icon-minus")
    $(".inner-item").removeClass("active")
    $(""".inner-item-a[href="#{local}"]""").closest(".inner-item").addClass("active")

module.exports = Sidebar
