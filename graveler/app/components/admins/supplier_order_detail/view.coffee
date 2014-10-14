Pagination = require "pokeball/components/pagination"
class SupplierOrderDetail
  constructor: ($)->
    @$orderPlot = $("#js-order-plot")
    @$startAtInput = $("#startAt")
    @$endAtInput = $("#endAt")
    @$searchButton = $(".js-btn-search")
    @$navli = $("ul.tab-navs li[data-role=nav]")
    @$checkboxs = $("input[name=range]")
    @$showColumnSelect = $("#dimension-select")
    @$exportButton = $(".js-btn-export")
    @bindEvents()
    @renderCharts()

  that = this

  bindEvents: ->
    that = this
    $(".tab").tab()
    @$startAtInput.datepicker
      onSelect: (selectedDate)->
        $("#endAt").data("pikaday").setMinDate(selectedDate)
    @$endAtInput.datepicker
      onSelect: (selectedDate)->
        $("#startAt").data("pikaday").setMaxDate(selectedDate)
    @$searchButton.on "click",@queryOrder
    @$navli.on "click",{},@queryOrder
    @$checkboxs.on "change",@queryOrder
    @$showColumnSelect.on "change",@queryOrder
    @$exportButton.on "click",@exportDetail
    new Pagination(".orderlist-pagination").total($(".orderlist-pagination").data("total")).show(2)
    @titleYesterday()
    @pageDefaults()

  exportDetail: ->
    search = []
    userId = $.query.get("userId")
    search.push "userId=#{userId}"
    search.push "startAt=#{$(".order-form").find("input[name=startAt]").val()}" if !_.isEmpty $(".order-form").find("input[name=startAt]").val()
    search.push "endAt=#{$(".order-form").find("input[name=endAt]").val()}" if !_.isEmpty $(".order-form").find("input[name=endAt]").val()
    search.push "range=#{$(".order-form").find("input[name=range]:checked").val()}" if !_.isEmpty $(".order-form").find("input[name=range]:checked").val()
    search.push "type=#{$("ul.tab-navs").find("li.active").data("type")}" if _.isNumber $("ul.tab-navs").find("li.active").data("type")
    window.location.href = "/api/export/reparation?"+search.join("&")

  queryOrder: (evt)->
    evt.preventDefault()
    search = []
    userId = $.query.get("userId")
    search.push "userId=#{userId}"
    search.push "startAt=#{$(".order-form").find("input[name=startAt]").val()}" if !_.isEmpty $(".order-form").find("input[name=startAt]").val()
    search.push "endAt=#{$(".order-form").find("input[name=endAt]").val()}" if !_.isEmpty $(".order-form").find("input[name=endAt]").val()
    search.push "range=#{$(".order-form").find("input[name=range]:checked").val()}" if !_.isEmpty $(".order-form").find("input[name=range]:checked").val()
    if evt.data
      search.push "type=#{$(this).data("type")}" if _.isNumber $(this).data("type")
    else
      search.push "type=#{$("ul.tab-navs").find("li.active").data("type")}" if _.isNumber $("ul.tab-navs").find("li.active").data("type")

    search.push "showColumn=#{$(".order-form").find("select[name=showColumn]").val()}" if !_.isEmpty $(".order-form").find("select[name=showColumn]").val()
    window.location.search = search.join("&")

  renderCharts: ->
    @$orderPlot.highcharts
      chart:
        type: 'line'
      title:
        text: null
      xAxis: @getXAxis()
      yAxis: @getYAxis()
      tooltip:
        headerFormat: '<span style="font-size:10px">{point.key} </span><table>'
        pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                '<td style="padding:0"><b>{point.y}</b></td></tr>'
        followTouchMove: true
        followPointer: false
        footerFormat: '</table>'
      series: @getSeries()
      legend:
        enabled: true
      credits:
        enabled: false

  #获取图标x轴数据
  getXAxis: ->
    categories = @getRecentDays()
    rotation = if categories.length >= 30 then -45 else 0
    {
      categories: categories
      enabled: true
      title:
        text: null
      labels:
        rotation: rotation
    }

  #获取图表Y轴数据
  getYAxis: ->
    showColumn = $("select[name=showColumn]").val()
    switch showColumn
      when "0"
        [
          title:
            text: "损失数量"
        ]
      when "1"
        [
          title:
            text: "损失数量(元)"
        ]
      else
        [
            title:
              text: "损失数量"
          ,
            title:
              text: "损失数量(元)"
            opposite: true
        ]

  #获取图表数据
  getSeries: ->
    dataArray = @$orderPlot.data("points")
    showColumn = $("select[name=showColumn]").val()
    showLable = if dataArray[1].length >=30 then false else true
    if showColumn is "1"
      [
        name: "损失数量"
        data:  dataArray[1]
        color: "#6c93d3"
        tooltip:
          valueSuffix: ' 元'
        dataLabels:
            align: 'left'
            enabled: showLable
            useHTML: true
            color: 'white'
            style:
              textShadow: "0 0 3px black, 0 0 3px black"
      ]
    else if showColumn is "0"
      [
        name: "损失数量"
        data: dataArray[0]
        color:  "#90ed7d"
        tooltip:
          valueSuffix: ' 个'
        dataLabels:
          enabled: showLable
          useHTML: true
          color: 'white'
          style:
            fontWeight: 'bold'
            textShadow: "0 0 3px black, 0 0 3px black"
      ]
    else
      [
        name: "损失数量"
        data: dataArray[0]
        color:  "#90ed7d"
        tooltip:
          valueSuffix: ' 个'
        dataLabels:
          enabled: showLable
          useHTML: true
          color: 'white'
          style:
            fontWeight: 'bold'
            textShadow: "0 0 3px black, 0 0 3px black"
      ,
        name: "损失数量"
        data: dataArray[1]
        color: "#6c93d3"
        yAxis: 1
        tooltip:
          valueSuffix: ' 元'
        dataLabels:
            align: 'left'
            enabled: showLable
            useHTML: true
            color: 'white'
            style:
              textShadow: "0 0 3px black, 0 0 3px black"
      ]

  #获取7天日期
  getRecent7Day: ->
    array = new Array(30)
    now = moment()
    for i in [0..29]
      array[i] = moment().subtract("days",i).format "MM-DD"
    array.reverse()

  getRecentDays: ->
    startDate = @$startAtInput.val()
    endDate = @$endAtInput.val()
    range = $("input[name=range]:checked").val()
    if _.isEmpty(startDate) and _.isEmpty(endDate)
      switch range
        when "1"
          array = new Array(7)
          dataArray = [1..7]
        when "2"
          array = new Array(30)
          dataArray = [1..30]
        when "3"
          array = new Array(365)
          dataArray = [1..365]
        else
          array = new Array(7)
          dataArray = [1..7]
      endDate = moment()
    else
      rangeNum = if range is "1" or _.isEmpty(range) then 7 else if range is "2" then 30 else if range is "3" then 365 else 7
      startDate = moment(endDate).subtract("days",rangeNum) if _.isEmpty startDate
      endDate =  moment() if _.isEmpty(endDate)
      startDate = moment(startDate)
      endDate = moment(endDate)
      num = endDate.diff(startDate, 'days') + 1;
      array = new Array(num)
      dataArray = [0..num-1]
    for i in dataArray
        array[i] = moment(endDate).subtract("days",i).format "MM-DD"
    array.reverse()

  showColumn: ->
    orderPlot = $("#js-order-plot")

    showColumn = parseInt($("select[name=showColumn").val())
    if (showColumn==1 or showColumn==0)
      orderPlot.highcharts().series[0].setVisible(true)
      orderPlot.highcharts().series[1].setVisible(true)
      showColumn = Math.abs(showColumn-1)
      orderPlot.highcharts().series[showColumn].setVisible(false)
    else
      orderPlot.highcharts().series[0].setVisible(true)
      orderPlot.highcharts().series[1].setVisible(true)
      orderPlot.highcharts().render()

  titleYesterday: ->
    yesterday = new Date(Date.now() - 3600*24*1000)
    $("h4#yesterday").html(\
        yesterday.getFullYear()+"-"+(yesterday.getMonth()+1)+"-"+yesterday.getDate()+" 昨日")

  pageDefaults: ->
    _.each $(".default-horiz td"), (i) ->
      if _.isEmpty $(i).html()
        $(i).html('<span class="note">---</span>')

module.exports = SupplierOrderDetail



