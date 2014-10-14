class PlatformDemand
  constructor: ($)->
    @$plot = $("#js-performance-plot")
    @$indicatorPlot = $(".indicator-plots .plot")
    @renderCharts()
    @renderIndicatorCharts()

  renderCharts: ->
    @$plot.highcharts
      chart:
        type: "area"

      title:
        text: null

      xAxis:
        type: "datetime"
        labels:
          formatter: ->
            Highcharts.dateFormat('%Y-%m-%d', @value);

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
          pointStart: Date.now() - 24 * 3600 * 1000 * 20,
          pointInterval: 24 * 3600 * 1000

      series: [
        {
          name: "现金收入"
          data: @$plot.data("points")[0]
        }, {
          name: "利润"
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


module.exports = PlatformDemand
