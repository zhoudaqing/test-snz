# Copyright (c) 2014 杭州端点网络科技有限公司
$.ajaxSetup
  cache: false
  error: (jqXHR) ->
    Essage.show
      message: jqXHR.responseText
      status: "error"
    , 2000
