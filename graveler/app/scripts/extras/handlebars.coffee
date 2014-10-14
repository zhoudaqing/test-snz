Handlebars.registerHelper 'pp', (json, options) ->
  JSON.stringify(json)

Handlebars.registerHelper 'add', (a,b, options) ->
  a + b

Handlebars.registerHelper "formatPrice", (price, type, options) ->
  return if not price?
  if type is 1
    formatedPrice = (price / 100)
    roundedPrice = parseInt(price / 100)
  else
    formatedPrice = (price / 100).toFixed(2)
    roundedPrice = parseInt(price / 100).toFixed(2)
  if `formatedPrice == roundedPrice` then roundedPrice else formatedPrice

Handlebars.registerHelper "formatDate", (date, type, options) ->
  return unless date
  switch type
    when "gmt" then moment(parseInt date).format("EEE MMM dd HH:mm:ss Z yyyy")
    when "day" then moment(parseInt date).format("YYYY-MM-DD")
    else moment(parseInt date).format("YYYY-MM-DD HH:mm:ss")

Handlebars.registerHelper 'length', (a, options)->
  length = a.length

Handlebars.registerHelper 'of', (a, b, options)->
  values = b.split(",")
  if _.contains values, a.toString()
    options.fn(this)
  else
    options.inverse(this)

Handlebars.registerHelper "lt", (a, b, options) ->
  if a < b
    options.fn(this)
  else
    options.inverse(this)

Handlebars.registerHelper "gt", (a, b, options) ->
  if a > b
    options.fn(this)
  else
    options.inverse(this)

Handlebars.registerHelper 'gtTime', (a, b, options) ->
  nowTime = moment()
  switch b
    when "dayStart" then benchmarkTime = new Date(nowTime.format("YYYY-MM-DD")).valueOf()
    when "now" then benchmarkTime = nowTime.valueOf()
    when "dayEnd" then benchmarkTime = new Date(moment().date(nowTime.date()+1).format("YYYY-MM-DD")).valueOf()
    else benchmarkTime = moment(b).valueOf()
  if moment(a).valueOf() > benchmarkTime
    options.fn(this)
  else
    options.inverse(this)

Handlebars.registerHelper "isArray", (a, options) ->
  if _.isArray a
    options.fn(this)
  else
    options.inverse(this)


Handlebars.registerHelper "between", (a, b, c, options) ->
  if  a >= b and a <= c
    options.fn(this)
  else
    options.inverse(this)
