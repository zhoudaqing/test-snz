Modal = require "pokeball/components/modal"
Tip = require "common/tip_and_alert/view"
class FeedbackManage
  constructor: ($)->
    @findit = $("#feedback_find")
    @deleteit = $("#feedback_delete")
    @updateit = $("#feedback_update")
    @bindEvent()

  bindEvent: ->
    @findit.on "click", @findById
    @deleteit.on "click", @deleteById
    @updateit.on "click", @updateById

  findById: ->
    id = $("input[name=user_id]").val()
    $.ajax
      url: "/api/admin/feedback_manage/findByStatus"
      type: "POST"
      data: {status: id}
      success: (data)->
        $(".table-striped tbody").empty()
        console.log data
        $.each data , (i, data)  ->
          $(".table-striped tbody").append("<tr><td>#{data.id}</td><td>#{data.userId}</td><td>#{data.supplierId}</td><td>#{data.message}</td></tr>")
      error: (data)->
        new Modal
          "icon":"error"
          "title": "error"
          "content": data.responseText
        .show()


  deleteById: ->
    id = $("input[name=user_id]").val()
    $.ajax
      url: "/api/admin/feedback_manage/deleteFeedbackByUserId"
      type: "POST"
      data: {id: id}
      success: (data)->
        alert "delete it success"
      error: (data)->
        new Modal
          "icon":"error"
          "title": "error"
          "content": data.responseText
        .show()

  updateById: ->
    id = $("input[name=user_id]").val()
    userid = $("input[name=user_notKnown_1]").val()
    supplierid = $("input[name=user_notKnown_2]").val()
    status = $("input[name=user_notKnown_3]").val()
    data={id: id, userId: userid, supplierId: supplierid, status: status} 
    console.log JSON.stringify data
    $.ajax
      url: "/api/admin/feedback_manage/updateFeedbackByUserId"
      type: "POST"
      data: {yzlCreditQualify: JSON.stringify (data)}
      success: (data)->
        alert "update it success"
      error: (data)->
        new Modal
          "icon":"error"
          "title": "error"
          "content": data.responseText
        .show()
module.exports = FeedbackManage
