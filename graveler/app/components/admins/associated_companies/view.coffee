Modal = require "pokeball/components/modal"
class AssociatedCompany
  constructor: ($)->  
    @chooseCompanyA = $(".choose-company-a")
    @chooseCancleA = $(".choose-no-a")
    @getCompanyMessageA = $(".get-company-a")
    @chooseCompanyB = $(".choose-company-b")
    @chooseCancleB = $(".choose-no-b")
    @getCompanyMessageB = $(".get-company-b")
    @getContact = $(".get-company")
    @companyOwn = $(".company-own")
    @cancelContect = ".cancel-contect"
    @companyClose = $(".get-company-over")
    @bindEvent()
  that = this
  bindEvent: ->
    that = this
    @chooseCompanyA.on "click" ,@getCompanyInfo
    @chooseCancleA.on "click" ,@clearEmpty
    @getCompanyMessageA.on "click" ,@getMessage
    @chooseCompanyB.on "click" ,@getCompanyInfoB
    @chooseCancleB.on "click" ,@clearEmptyB
    @getCompanyMessageB.on "click" ,@getMessageB
    @getContact.on "click" ,@contact
    @companyOwn.on "click" ,@getCompanyOwn
    $(document).on "click" ,@cancelContect ,@delContect
    @companyClose.on "click" ,@colseTable
  getCompanyInfo: ->
    supplierName = $.trim($(@).closest(".supplier-a").find("input[type=text]").val())
    if supplierName
      $.ajax
          url: "/api/admins/associate"
          type: "POST"
          data: {"supplierName": supplierName}
          success: (data)->
            if data.length is 0
              new Modal
                "icon": "info"
                "title": ""
                "content": "没有相应的供应商"
              .show()
            else 
              $(".company-list-a").show()
              $(".company-list-a tbody").empty()
              $.each data, (i , d) ->
                $(".company-list-a tbody").append("""
            <tr>
              <td><input type="radio" name="company"><input type="hidden" value="#{d.id}"></td>
              <td>#{d.supplierCode}</td>
              <td>#{d.corporation}</td>
              <td>#{d.initAgent}</td>
            </tr>""")
          complete: ->
            that.removeUn()
    else
      new Modal
        "icon": "info"
        "title": ""
        "content": "请输入相应供应商"
      .show()
  clearEmpty: ->
    $(@).closest(".supplier-a").find("input[type=text]").val("")
  getMessage: ->
    $(".company-list-a tbody tr").each ->
     if $(@).find("input[type=radio]").prop("checked")
      id = $(@).find("input[type=hidden]").val()
      name = $(@).find("td:eq(2)").text()
      $(".supplier-a-name").empty().text(name)
      $(".supplier-a-id").empty().text(id)
      return false
    $(".company-list-a").hide()
  getCompanyInfoB: ->
    supplierName = $.trim($(@).closest(".supplier-b").find("input[type=text]").val())
    if supplierName
      $.ajax
          url: "/api/admins/associate"
          type: "POST"
          data: {"supplierName": supplierName}
          success: (data)->
            if data.length is 0
              new Modal
                "icon": "info"
                "title": ""
                "content": "没有相应的供应商"
              .show()
            else
              $(".company-list-b").show()
              $(".company-list-b tbody").empty()
              $.each data, (i , d) ->
                $(".company-list-b tbody").append("""
            <tr>
              <td><input type="radio" name="company"><input type="hidden" value="#{d.id}"></td>
              <td>#{d.supplierCode}</td>
              <td>#{d.corporation}</td>
              <td>#{d.initAgent}</td>
            </tr>""")
          complete: ->
            that.removeUn()
    else
      new Modal
        "icon": "info"
        "title": ""
        "content": "请输入相应供应商"
      .show()
  clearEmptyB: ->
    $(@).closest(".supplier-b").find("input[type=text]").val("")
  getMessageB: ->
    $(".company-list-b tbody tr").each ->
     if $(@).find("input[type=radio]").prop("checked")
      id = $(@).find("input[type=hidden]").val()
      name = $(@).find("td:eq(2)").text()
      $(".supplier-b-name").empty().text(name)
      $(".supplier-b-id").empty().text(id)
      return false
    $(".company-list-b").hide()
  contact: ->
    supplierAId = $(".supplier-a-id").text()
    supplierBId = $(".supplier-b-id").text()
    if supplierAId is ""
      new Modal
        "icon": "error"
        "title": ""
        "content": "您没有选择供应商"
      .show()
    else if supplierBId is ""
      new Modal
        "icon": "error"
        "title": ""
        "content": "您没有选择关联供应商"
      .show()
    else if supplierAId is supplierBId
      new Modal
        "icon": "error"
        "title": ""
        "content": "您不能关联自己"
      .show()
    else   
      $.ajax
        url: "/api/admins/createRelation"
        type: "POST"
        data: {"alphaId": supplierAId, "betaId": supplierBId}
        success: (data)->
          new Modal
            "icon": "success"
            "title": ""
            "content": "关联成功"
          .show()
  getCompanyOwn: ->
    supplierAId = $(".supplier-a-id").text()
    if supplierAId is ""
      new Modal
        "icon": "info"
        "title": ""
        "content": "您没有选择供应商"
      .show()
    else
      $.ajax
          url: "/api/admins/getRelatedSuppliers"
          type: "POST"
          data: {"alphaId": supplierAId}
          success: (data)-> 
            $(".company-list-con tbody").empty()
            if data.length is 0
              new Modal
                "icon": "info"
                "title": ""
                "content": "没有已关联供应商"
              .show()
              $(".company-list-con").hide()
            else
              $(".company-list-con").show()
              $.each data, (i , d) ->
                $(".company-list-con tbody").append("""
              <tr>
                <td>#{d.supplierCode}</td>
                <td>#{d.corporation}</td>
                <td>#{d.initAgent}</td>
                <td>
                <a class="btn btn-primary cancel-contect" href="javascript:void(0)">解除关联</a>
                <input type="hidden" value="#{d.id}">
                </td>
              </tr>""")
          complete: ->
            that.removeUn()
  delContect: ->
    supplierAId = $(".supplier-a-id").text()
    supplierBId = $(@).closest("td").find("input[type=hidden]").val()
    $.ajax
      url: "/api/admins/deleteRelation"
      type: "POST"
      data: {"alphaId": supplierAId, "betaId": supplierBId}
      success: (data)-> 
        that.getCompanyOwn()
  colseTable: ->
    $(".company-list-con").hide()
  removeUn: ->
    $(".company-list-a tbody tr td").each ->
      if $(@).text() is "undefined"
        $(@).text("")
    $(".company-list-b tbody tr td").each ->
      if $(@).text() is "undefined"
        $(@).text("")
    $(".company-list-con tbody tr td").each ->
      if $(@).text() is "undefined"
        $(@).text("")
module.exports = AssociatedCompany