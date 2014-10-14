Modal = require "pokeball/components/modal"
class SupplierDetail
  constructor: ->
    @$spiderPlot = $(".overall-performance .plot")
    @$barPlot = $(".overall-tendency .plot")
    @$stackPlot = $(".overall-order .plot")
    @$tendencyPlots = $(".tendency-detail-plots .plot")
    @$orderPlots = $(".order-detail-plots .plot")
    @$showStrategyButton = $(".js-show-strategy")
    @bindEvent()
    @renderCharts()
    @renderDetailCharts()

  showStrategyTemplate = Handlebars.templates["admins/supplier_performance/templates/show_strategy"]
  that = this

  bindEvent: ->
    that = this
    @showScope()
    @$showStrategyButton.on "click",@showStrategy

  renderCharts: ->
    @$spiderPlot.highcharts
      chart:
        polar: true
        type: "line"
      title:
        text: null
      pane:
        size: "80%"
      xAxis:
        categories: ['T', 'Q', 'R', 'D', 'C'],
        tickmarkPlacement: 'on',
        lineWidth: 0
      yAxis:
        gridLineInterpolation: 'polygon',
        ceiling: 100
        tickInterval: 20
        floor: 0
        lineWidth: 0,
        min: 0
      series: [{
        name: '得分',
        data: @$spiderPlot.data("points"),
        pointPlacement: 'on'
        color: "#666"
      }]
      legend:
        enabled: false
      credits:
        enabled: false

    @$barPlot.highcharts
      chart:
        type: 'column'
      title:
        text: null
      xAxis:
        categories: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"]
        enabled: false
        title:
          text: null
        tickInterval: 2
        tickLength: 5
        labels:
          style:
            fontFamily: "arial"
      yAxis:
        ceiling: 100
        floor: 0
        tickInterval: 20
        title:
          text: null
        labels:
          enabled: true
        plotLines: [
          value: 80
          color: "#59b053"
          width: 2
          zIndex: 4
        ]
      series: [{
        name: "得分"
        data: @cssRedUnder80 @$barPlot.data("points")
        zIndex: 1
      },{
        type: "line"
        name: "得分"
        data: @$barPlot.data("points")
        color: "#c1504b"
        zIndex: 10
      }]
      legend:
        enabled: false
      credits:
        enabled: false

    @$stackPlot.highcharts
      chart:
        type: 'column'
      title:
        text: null
      xAxis:
        categories: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"]
        enabled: false
        title:
          text: null
        tickInterval: 2
        tickLength: 5
      yAxis:
        title:
          text: null
        labels:
          useHTML: true
          enabled: true
          formatter: ->
            if @value < 25
              "<div style='margin-top: 40px'>优选</div>"
            else if @value <50
              "<div style='margin-top: 40px'>合格</div>"
            else if @value <75
              "<div style='margin-top: 40px'>限制</div>"
            else if @value <100
              "<div style='margin-top: 40px'>淘汰</div>"
            else
              ""
        reversed: true
      tooltip:
        shared: false,
        formatter: ->
          @y if this.series.name == "分数"
        enabled: false
      plotOptions:
        column:
          stacking: 'percent'
      series: [{
        name: "得分"
        data: [2,2,2,2,2,2,2,2,2,2,2,2]
        color: "#c13128"
        zIndex: 1
      }, {
        name: "得分"
        data: [2,2,2,2,2,2,2,2,2,2,2,2]
        color: "#f6c032"
        zIndex: 1
      }, {
        name: "得分"
        data: [2,2,2,2,2,2,2,2,2,2,2,2]
        color: "#558ed5"
        zIndex: 1
      }, {
        name: "得分"
        data: [2,2,2,2,2,2,2,2,2,2,2,2]
        color: "#59b053"
        zIndex: 1
      },{
        type: "line"
        name: "分数"
        data: @$stackPlot.data("points")
        color: "#c1504b"
        zIndex: 10
      }]
      legend:
        enabled: false
      credits:
        enabled: false

  renderDetailCharts: ->
    _.each @$tendencyPlots, (i) ->
      $(i).highcharts
        chart:
          type: 'column'
        title:
          text: null
        xAxis:
          categories: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"]
          enabled: false
          title:
            text: null
          labels:
            enabled: false
          tickLength: 0
        yAxis:
          enabled: false
          ceiling: 100
          title:
            text: null
          labels:
            enabled: false
          tickInterval: 20
          plotLines: [
            value: 80
            color: "#59b053"
            width: 2
            zIndex: 4
          ]
        series: [{
          name: "得分"
          data: that.cssRedUnder80 $(i).data("points")
          zIndex: 1
        },{
          type: "line"
          name: "得分"
          data: $(i).data("points")
          color: "#c1504b"
          zIndex: 10
        }]
        legend:
          enabled: false
        credits:
          enabled: false

    _.each @$orderPlots, (i) ->
      $(i).highcharts
        chart:
          type: 'column'
        title:
          text: null
        xAxis:
          categories: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"]
          enabled: false
          title:
            text: null
          labels:
            enabled: false
          tickLength: 0
        yAxis:
          enabled: false
          title:
            text: null
          labels:
            enabled: false
          ceiling: 100
          tickInterval: 25
          reversed: true
        tooltip:
          shared: false,
          formatter: ->
            @y if this.series.name == "分数"
          enabled: false
        plotOptions:
          column:
            stacking: 'percent'
        series: [{
          name: "得分"
          data: [2,2,2,2,2,2,2,2,2,2,2,2]
          color: "#c13128"
          zIndex: 1
        }, {
          name: "得分"
          data: [2,2,2,2,2,2,2,2,2,2,2,2]
          color: "#f6c032"
          zIndex: 1
        }, {
          name: "得分"
          data: [2,2,2,2,2,2,2,2,2,2,2,2]
          color: "#558ed5"
          zIndex: 1
        }, {
          name: "得分"
          data: [2,2,2,2,2,2,2,2,2,2,2,2]
          color: "#59b053"
          zIndex: 1
        },{
          type: "line"
          name: "分数"
          data: $(i).data("points")
          color: "#c1504b"
          zIndex: 10
        }]
        legend:
          enabled: false
        credits:
          enabled: false

  cssRedUnder80: (data)->
    _.map data,(num)->
      if num < 80
        {y:num,color:"#BF0B23"}
      else
        num

  showStrategy: ->
    compositeScore = parseInt( $("span#compositeScore").html())
    new Modal(showStrategyTemplate({compositeScore:compositeScore})).show()
  showScope:  ->
    scope = $('#compositeScore').text()
    if scope < 60
      $('.showScope').append("""分区 <span style="color:#c13128">淘汰</span>""")
    else if scope < 80
      $('.showScope').append("""分区 <span style="color:#f6c032">限制</span>""")
    else if scope < 90
      $('.showScope').append("""分区 <span style="color:#558ed5">合格</span>""")
    else if scope <= 100
      $('.showScope').append("""分区 <span style="color:#59b053">优选</span>""")
module.exports = SupplierDetail
