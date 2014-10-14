class SupplierPlatformPerformance
  constructor: ($)->
    @$piePlot = $(".overall-performance .plot")
    @$trackPlot = $(".month-track .plot")

    @bindEvent()
    @renderCharts()
    @renderDetailCharts()


  bindEvent: ->


  renderCharts: ->
    @$piePlot.highcharts
      chart:
        type: "pie"
        options3d:
          enabled: true
          alpha:  65
          beta: 0
      pane:
        size: "80%"
      title:
        text: null
      plotOptions:
        pie:
          allowPointSelect: true
          cursor: "pointer"
          depth: 35
          dataLabels:
            enable: true
            format: "{point.name},{point.y}"
      tooltip:
        pointFormat: "{series.name}: <b>{point.percentage:.1f}%</b>"
      series: [
        type: "pie"
        name: "占据比例",
        data: @mapPiePlotData @$piePlot.data("points")
      ]
      legend:
        enabled: false
      credits:
        enabled: false

    @$trackPlot.highcharts
      chart:
        type: "column"
        options3d:
          enabled: true
          alpha: 0
          beta: 0
          depth: 30
          viewDistance: 0
      title:
        text: null
      xAxis:
        categories: ['1月', '2月', '3月', '4月', '5月', "6月", "7月", "8月", "9月", "10月", "11月", "12月"]
        enabled: false
        title:
          text: null
      yAxis:
        min: 0
        title:
          text: null
        labels:
          enabled: false
        reversed: true
      plotOptions:
        column:
          depth: 25
          stacking: "percent"
          dataLabels:
            enabled: true,
            useHTML: true
            color: 'white',
            style:
              textShadow: "0 0 3px black, 0 0 3px black"
      tooltip:
        pointFormat: " {series.name}: {point.percentage:.1f}%"
        followTouchMove: true
        followPointer: true
      series: [
        name: "绿区"
        data: @$trackPlot.data("points")[3]
        stack: "track"
        color:  "#90ed7d"
      ,
        name: "蓝区"
        data: @$trackPlot.data("points")[2]
        stack: "track"
        color: "#6c93d3"
      ,
        name: "黄区"
        data: @$trackPlot.data("points")[1]
        stack: "track"
        color: "#f6a969"
      ,
        name: "红区"
        data: @$trackPlot.data("points")[0]
        stack: "track"
        color: "#f56507"
      ]
      legend:
        enabled: false
      credits:
        enabled: false

  mapPiePlotData: (data)->
    colors = [ "#f56507", "#f6a969", "#6c93d3", "#90ed7d" ].reverse()
    dataNames = [ "红区", "黄区", "蓝区", "绿区" ].reverse()
    result = _.map data, (num)->
        unless _.isNumber num
          num = 0
        {
          name: dataNames.pop()
          y:  num
          color:  colors.pop()
        }
    _.reject result, (obj)->
      obj.y is 0

  renderDetailCharts: ->



module.exports = SupplierPlatformPerformance
