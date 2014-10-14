class SupplierReport
  constructor: ->
    @$reportPlot = $("#reportplot")
    @$condtionLink = $(".js-extra-condition")
    @$extraCondition = $(".condition-extra")
    @$selectColumn = $(".js-select-column")
    @$checkbox = $(".scope-category-label input:checkbox")
    @$startAtInput = $("#registeredAtStart")
    @$endAtInput = $("#registeredAtEnd")
    @$exportButton = $(".js-export-report")
    @bindEvent()
    @renderCharts()
    @renderParks()
    @renderIndustry()
    @renderSmallResources()
    @renderTableData()

  that = this

  bindEvent: ->
    that = this
    @$startAtInput.datepicker
      onSelect: (selectedDate)->
        $("#registeredAtEnd").data("pikaday").setMinDate(selectedDate)
    @$endAtInput.datepicker
      onSelect: (selectedDate)->
        $("#registeredAtStart").data("pikaday").setMaxDate(selectedDate)
    @$condtionLink.on "click",@toggleCondition
    @$selectColumn.on "click",@toggleCloumnDiv
    $(".close").on "click", @closeScopeSelect
    _.each @$checkbox.not(":checked"), (i)->
      $("table tr th.#{$(i).val()},table tr td.#{$(i).val()}").hide()
    # $("table").width  _.reduce($("table tr th").not(":hidden"),(memo, num)->
    #   memo + num
    # , 0)
    @$checkbox.on "click", @toggleColumn
    @$exportButton.on "click", @exportReport

  exportReport: ->
    $.ajax
      url: "/api/admin/supplierreport/export"
      type: "GET"
      success: (data)->
        window.location.href = data


  renderTableData: ->
    #customers
    _.each $("table tbody tr td.customers"),(td)->
      customerJson = $(td).data "value"
      if !_.isEmpty(customerJson)
        data = _.flatten(_.values(customerJson))
        result = _.reject( _.pluck(data,"remark"),(string)->
          _.isEmpty(string) or string is "null"
        ).join(",")
        $(td).html result

    #工厂面积 factories_area
    _.each $("table tbody tr td.factories_area"),(td)->
      areaJson = $(td).data "value"
      if !_.isEmpty(areaJson)
        length = areaJson.length
        _.each areaJson, (obj,index)->
          innerclass = if length is 1 then "" else " "
          $(td).append """<div class="#{innerclass}">#{obj.size}&nbsp;</div>"""
    #人数
    _.each $("table tbody tr td.factories_num"),(td)->
      areaJson = $(td).data "value"
      if !_.isEmpty(areaJson)
        length = areaJson.length
        _.each areaJson, (obj,index)->
          innerclass = if length is 1 then "" else " "
          $(td).append """<div class="#{innerclass}">#{obj.employee}&nbsp;</div>"""
    #固定资产
    _.each $("table tbody tr td.factories_assets"),(td)->
      areaJson = $(td).data "value"
      if !_.isEmpty(areaJson)
        length = areaJson.length
        _.each areaJson, (obj,index)->
          innerclass = if length is 1 then "" else " "
          $(td).append """<div class="#{innerclass}">#{obj.factoryFixedAssets}&nbsp;</div>"""

    #销售额(万元)
    _.each $("table tbody tr td.recentFinance_sold"),(td)->
      soldJson = $(td).data "value"
      if !_.isEmpty(soldJson)
        $(td).html """<div>#{soldJson.year1}年:#{soldJson.sold1}</div><div>#{soldJson.year2}年:#{soldJson.sold2}</div><div>#{soldJson.year3}年:#{soldJson.sold3}</div>"""

    #净利润(万元)
    _.each $("table tbody tr td.recentFinance_net"),(td)->
      netJson = $(td).data "value"
      if !_.isEmpty(netJson)
        $(td).html """<div>#{netJson.year1}年:#{netJson.net1}</div><div>#{netJson.year2}年:#{netJson.net2}</div><div>#{netJson.year3}年:#{netJson.net3}</div>"""

  renderSmallResources: ->
    $.ajax
      url: "/api/supplier/frontendCategory/1"
      type: "GET"
      success: (data)->
        smallResourcesSelect = $("select[name=moduleId]").empty();
        smallResourcesSelect.append("""<option value="">请选择</option>""")
        _.each data, (item)->
          smallResourcesSelect.append("""<option value="#{item.id}">#{item.name}</option>""")
        smallResources = smallResourcesSelect.data("moduleid")
        if smallResources isnt ""
          smallResourcesSelect.find("option[value=#{smallResources}]").prop "selected", true

  renderIndustry: ->
    $.ajax
      url: "/api/supplier/businesses"
      type: "GET"
      success: (data)->
        industrySelect = $("select[name=mainBusinessId]").empty();
        industrySelect.append("""<option value="">请选择</option>""")
        _.each data, (item)->
          industrySelect.append("""<option value="#{item.id}">#{item.name}</option>""")
        industry = industrySelect.data("industry")
        if industry isnt ""
          industrySelect.find("option[value=#{industry}]").prop "selected", true

  renderParks: ->
    $.ajax
      url: "/api/delivery/parks"
      type: "GET"
      success: (data)->
        parkSelect = $("select[name=supplyParkId]").empty();
        parkSelect.append("""<option value="">请选择</option>""")
        _.each data, (item)->
          parkSelect.append("""<option value="#{item.id}">#{item.parkName}</option>""")
        park = parkSelect.data("park")
        if park isnt ""
          parkSelect.find("option[value=#{park}]").prop "selected", true

  toggleCondition: ->
    if $(@).data("status") is "show"
      that.$extraCondition.show()
      $(@).data("status","hidden")
      $(@).html("隐藏条件")
    else if $(@).data("status") is "hidden"
      that.$extraCondition.hide()
      $(@).data("status","show")
      $(@).html("显示条件")

  toggleCloumnDiv: ->
    $(".select-menu").toggleClass "hiden"

  closeScopeSelect: ->
    $(".select-menu").toggleClass "hiden"

  toggleColumn: ->
    val = $(@).val()
    if $(@).is(":checked")
      # _.each $("table tr"), (item)->
        $("table").find(".#{val}").show()
    else
      # _.each $("table tr"), (item)->
        $("table").find(".#{val}").hide()

  renderCharts: ->
    $.ajax
      url: "/api/supplier/count"
      type: "GET"
      success: (data)=>
        data = [data.registeredCount,data.completedCount,data.standardCount,data.alternativeCount,data.inCount,data.dieOutCount]
        @$reportPlot.data "points",data
        @$reportPlot.highcharts
          chart:
            type: 'column'
          title:
            text: null
          xAxis:
            categories: ["广选", "完善信息", "入围", "备选", "合作", "淘汰"]
          yAxis:
            floor: 0
            tickInterval: 200
            title:
              text: null
            labels:
              enabled: true
          tooltip:
            headerFormat: '<span style="font-size:10px">{point.key} </span><table>'
            pointFormat: '<tr><td style="color:{series.color};padding:0">:</td>' +
                    '<td style="padding:0"><b>{point.y}</b></td></tr>'
            followTouchMove: true
            followPointer: false
            footerFormat: '</table>'
          series: [{
            data: [
              {
                y: @$reportPlot.data("points")[0]
                color: "#da4a41"
              },
              {
                y: @$reportPlot.data("points")[1]
                color: "#558ed5"
              },
              {
                y: @$reportPlot.data("points")[2]
                color: "#ffd94b"
              },
              {
                y: @$reportPlot.data("points")[3]
                color: "#59b053"
              },
              {
                y: @$reportPlot.data("points")[4]
                color: "#999eff"
              },
              {
                y: @$reportPlot.data("points")[5]
                color: "#5c5c61"
              }]
            dataLabels:
                align: 'center'
                enabled: true
                useHTML: true
                color: 'white'
                style:
                  textShadow: "0 0 3px black, 0 0 3px black"
          }]
          legend:
            enabled: false
          credits:
            enabled: false

module.exports = SupplierReport
