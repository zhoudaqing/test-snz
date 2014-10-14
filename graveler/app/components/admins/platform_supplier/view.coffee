class PlatformSupplier
  constructor: ($)->
    @$plot = $("#js-performance-plot")
    @$indicatorPlot = $(".indicator-plots .plot")
    @renderCharts()
    @renderIndicatorCharts()
    @$dimensionSelect = $("#dimension-select")
    @$jsFilter = $(".js-filter")
    @bindEvent()
  
  that = this
  bindEvent: ->
    that = this
    @scheduleChart()
    @$dimensionSelect.on "change", @scheduleChart
    @$jsFilter.on "click", @scheduleChart

  scheduleChart: ->
    scheduleIndex = $("#dimension-select").val()
    isFilted = 0
    if $(@).data("check") is 1
      isFilted = 1
    switch scheduleIndex
      when "1"
        that.getPark(scheduleIndex, that.supplierDimension, isFilted)
      when "2"
        that.statusDimension(scheduleIndex, isFilted)
      when "4"
        that.getCategory(scheduleIndex, that.categoryDimension, isFilted)
      else
        that.levelDimension(scheduleIndex, isFilted)

  getPark: (scheduleIndex, callback, isFilted)->
    $.ajax
      url: "/api/delivery/parks"
      type: "GET"
      success: (data)->
        if isFilted is 1
          park1 = $(".target1").val()
          park2 = $(".target2").val()
        $(".target1").empty()
        $(".target2").empty().append("""<option value="" selected="">选择指标</option>""")
        $.each data, (i, d)->
          option = """<option value="#{d.id}">#{d.parkName}</option>"""
          $(".target1").append(option)
          $(".target2").append(option)
        if isFilted is 1
          $(".target1 option[value=#{park1}]").prop "selected", true
          $(".target2 option[value=#{park2}]").prop "selected", true
          callback(scheduleIndex, isFilted, park1, park2) if callback
        else
          callback(scheduleIndex, isFilted) if callback

  supplierDimension: (scheduleIndex, isFilted, park1, park2)->
    if isFilted is 0
      park1 = $(".target1").val()
      park2 = $(".target2").val()
    $.ajax
      url: "/api/supplier/parks/count?supplyParkId1=#{park1}&supplyParkId2=#{park2}"
      type: "GET"
      success: (data)->
        array = []
        array[0] = data.result1
        array[1] = data.result2
        $("#js-performance-plot").data("points", array)
        nameSeries = ["园区1", "园区2"]
        that.renderCharts(nameSeries)

  statusDimension: (scheduleIndex, isFilted)->
    options = """<option value="1">注册</option><option value="2">参与交互</option><option value="3">入围</option><option value="4">合作</option>"""
    if isFilted is 1
      oldStatus1 = $(".target1").val()
      oldStatus2 = $(".target2").val()
    $(".target1").empty().append(options).find("option[value=#{oldStatus1}]").prop("selected", true)
    $(".target2").empty().append("""<option value="" selected="">选择指标</option>""").append(options).find("option[value=#{oldStatus2}]").prop("selected", true)
    status1 = $(".target1").val()
    status2 = $(".target2").val()
    $.ajax
      url: "/api/supplier/status/count?status1=#{status1}&status2=#{status2}"
      type: "GET"
      success: (data)->
        array = []
        array[0] = data.result1
        array[1] = data.result2
        $("#js-performance-plot").data("points", array)
        nameSeries = ["状态1", "状态2"]
        that.renderCharts(nameSeries)

  levelDimension: (scheduleIndex, isFilted)->
    options = """<option value="1">优选</option><option value="2">合格</option><option value="3">限制</option><option value="4">淘汰</option>"""
    if isFilted is 1
      oldLevel1 = $(".target1").val()
      oldLevel2 = $(".target2").val()
    $(".target1").empty().append(options).find("option[value=#{oldLevel1}]").prop("selected", true)
    $(".target2").empty().append("""<option value="" selected="">选择指标</option>""").append(options).find("option[value=#{oldLevel2}]").prop("selected", true)
    level1 = $(".target1").val()
    level2 = $(".target2").val()    
    $.ajax
      url: "/api/supplier/level/count?level1=#{level1}&level2=#{level2}"
      type: "GET"
      success: (data)->
        array = []
        array[0] = data.result1
        array[1] = data.result2
        $("#js-performance-plot").data("points", array)
        nameSeries = ["层次1", "层次2"]
        that.renderCharts(nameSeries)

  getCategory: (scheduleIndex, callback, isFilted)->
    $.ajax
      url: "/api/admin/frontCategories/0/children"
      type: "GET"
      success: (data)->
        if isFilted is 1
          category1 = $(".target1").val()
          category2 = $(".target2").val()
        $(".target1").empty()
        $(".target2").empty().append("""<option value="" selected="">选择指标</option>""")
        $.each data, (i, d)->
          option = """<option value="#{d.id}">#{d.name}</option>"""
          $(".target1").append(option)
          $(".target2").append(option)
        if isFilted is 1
          $(".target1 option[value=#{category1}]").prop "selected", true
          $(".target2 option[value=#{category2}]").prop "selected", true
          callback(scheduleIndex, isFilted, category1, category2) if callback
        else
          callback(scheduleIndex, isFilted) if callback

  categoryDimension: (scheduleIndex, isFilted, category1, category2)->
    if isFilted is 0
      category1 = $(".target1").val()
      category2 = $(".target2").val()
    $.ajax
      url: "/api/supplier/category/count?industryId1=#{category1}&industryId2=#{category2}"
      type: "GET"
      success: (data)->
        array = []
        array[0] = data.result1
        array[1] = data.result2
        $("#js-performance-plot").data("points", array)
        nameSeries = ["类目1", "类目2"]
        that.renderCharts(nameSeries)


  renderCharts: (nameSeries)->
    if nameSeries is undefined
      nameSeries = []
      nameSeries[0] = "数据1"
      nameSeries[1] = "数据2"
    @$plot.highcharts
      chart:
        type: "area"

      title:
        text: null

      xAxis:
        type: "datetime"
        labels:
          formatter: ->
            Highcharts.dateFormat('%m-%d', @value);

      yAxis:
        enabled: false
        title:
          text: null

      tooltip:
        pointFormat: "{point.y:,.0f}"
        dateTimeLabelFormats:
          day: '%Y-%m-%d'

      plotOptions:
        area:
          marker:
            enabled: false
            symbol: "circle"
            radius: 2
            states:
              hover:
                enabled: true
        series:
          pointStart: Date.now() - 3600 * 1000 * (30 * 24 + 8),  #要多减去东八区的时差
          pointInterval: 24 * 3600 * 1000

      series: [
        {
          name: nameSeries[0]
          data: @$plot.data("points")[0]
          }, {
          name: nameSeries[1]
          data: @$plot.data("points")[1]
        }
      ]

      credits:
        enabled: false

  renderIndicatorCharts: ->
    colors = ["#a5b7d1", "#a7dece", "#9cc17a", "#f5e4a4", "#d1b9a5", "#bd9cf3"]
    _.each @$indicatorPlot, (i) ->
      $(i).highcharts
        chart:
          type: "area"
        title: null
        xAxis:
          enables: false
          labels:
            enabled: false
            step: 2
          tickLength: 0
        yAxis:
          enabled: false
          title:
            text: null
          labels:
            enabled: false
          tickPixelInterval: "20"
        series: [
          {
            name: "point"
            data: $(i).data("points")
            color: colors.pop()
            marker:
              enabled: false
          }
        ]
        legend:
          enabled: false
        credits:
          enabled: false




module.exports = PlatformSupplier
