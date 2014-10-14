Modal = require "pokeball/components/modal"
$.ajaxSetup
  cache: false
  error: (jqXHR, textStatus, errorThrown) ->
    if jqXHR.status is 401
      window.location.href = "/login"
    else
      new Modal
        "icon": "error"
        "title": "出错啦！"
        "content": jqXHR.responseText || "未知故障"
      .show()
    $("body").spin(false)
