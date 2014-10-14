Pokeball = require "pokeball"
require "extras/ajax"
require "extras/handlebars"

module.exports = ->
  require("pokeball/helpers/component").initialize()

  new Pokeball.Pagination("#js-pagination").total($("#js-total").val()).show(20)
