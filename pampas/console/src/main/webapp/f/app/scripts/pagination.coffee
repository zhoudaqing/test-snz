# Copyright (c) 2014 杭州端点网络科技有限公司
module.exports =
  init: ($el, options) ->
    total = $el.data("total")
    page = if $.query.get("pageNo") then $.query.get("pageNo") - 1 else 0

    defaults =
      prev_text: "PREV"
      next_text: "NEXT"
      current_page: page
      items_per_page: 20
      link_to: "javascript:void(0)"
      num_edge_entries: 1
      load_first_page: false
      num_display_entries: 6
      max_page: 100
      callback: (curr) =>
        window.location.search = $.query.set("pageNo", parseInt(curr) + 1).toString()

    options = $.extend defaults, options

    showTotal = Math.min(parseInt(total), options.items_per_page * options.max_page)

    $el.pagination showTotal, options
