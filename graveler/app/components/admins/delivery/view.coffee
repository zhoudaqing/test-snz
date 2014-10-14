deviceTemplate = Handlebars.templates["admins/delivery/templates/device"]

class Delivery

  constructor: ->
    $("#device-group-table tbody").append(
      deviceTemplate(
        data: $("#device-group-table tbody").data("content")
        url: $("#device-group-table tbody").data("url")
      )
    )

    $("#autodevice-group-table tbody").append(
      deviceTemplate(
        data: $("#autodevice-group-table tbody").data("content")
        url: $("#device-group-table tbody").data("url")
      )
    )

module.exports = Delivery
