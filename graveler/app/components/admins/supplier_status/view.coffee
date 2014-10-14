class Status
  constructor: ->
    @bindEvents()
  bindEvents: ->
    @renderStatus()
  renderStatus: ->
    step = $(".js-progress-step").data("step")
    userId = $.query.get("userId")
    if step is 6
      $(".js-progress-step").css("color","#ccc")
      #资质交互
      $.ajax
        url: "/api/supplier/checkQualified?userId=#{userId}"
        type: "GET"
        success: (data)->
          if data.status is 2 or data.status is 3
            $(".js-progress-step").find("span:eq(1)").css("color","#cc003a")
          else
            $(".js-progress-step").find("span:eq(1)").css("color","#ccc")
      #等级验证
      $.ajax
        url: "/api/supplier/credit/info?userId=#{userId}"
        type: "GET"
        success: (data)->
          if data.status >=1 or data.status <= 5
            $(".js-progress-step").find("span:eq(2)").css("color","#cc003a")
          else
            $(".js-progress-step").find("span:eq(2)").css("color","#ccc")
    else if step > 6
      $(".js-progress-step").addClass("active")
module.exports =  Status


