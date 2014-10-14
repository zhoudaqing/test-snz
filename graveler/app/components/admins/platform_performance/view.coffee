class PlatformPerformance
  constructor: ($)->
    @$plot = $("#js-performance-plot")
    @renderCharts()

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


module.exports = PlatformPerformance
