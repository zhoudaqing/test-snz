Modal = require "pokeball/components/modal"
Tip = require "common/tip_and_alert/view"
memberSelectTemplate = Handlebars.templates["admins/supplier_qualify/templates/invite_member"]
memberItemTemplate = Handlebars.templates["admins/supplier_qualify/templates/member_item"]
teamMemberListTemplate = Handlebars.templates["admins/supplier_qualify/templates/team_member_list"]
class Qualify
  constructor: ->
    @$jsInviteButton = $(".js-invite")
    @bindEvent()

  that = this
  bindEvent: ->
    that = this
    @renderCategory()
    @$jsInviteButton.on "click", @inviteMember
    $(document).on "confirm:applyForReject", @applyForReject
    $(document).on "confirm:reject", @rejectSupplier

  #渲染类目
  renderCategory: ->
    _.each $(".supplier-tr"), (tr)->
      $.each $(tr).find(".qualify-td").data("category"), (i, category)->
        $(tr).find(".qualify-td").append("#{category.name} ")

  #申请二级驳回
  applyForReject: (evt, data)->
    $.ajax
      url: "/api/qualify/reject/apply?supplierId=#{data}"
      type: "GET"
      success: (data)->
        window.location.reload()

  #二级驳回供应商
  rejectSupplier: (evt, data)->
    $.ajax
      url: "/api/qualify/reject?supplierId=#{data}"
      type: "GET"
      success: (data)->
        window.location.reload()

  validateTeamInput: (form)->
    status = true
    if _.isEmpty($(form).find("input[name=workNo]").val()) and _.isEmpty($(form).find("input[name=name]").val())
      new Tip({parent: $(form).find("input[name=workNo]").parent(), direct: "up", type: "info", message: "请完善成员信息", left: 140, top: 30}).tip()
      status = false
    else if ( (not _.isEmpty($(form).find("input[name=workNo]").val()))) and _.isEmpty($(form).find("input[name=name]").val()) and (not /^\d{6,}$/.test($(form).find("input[name=workNo]").val()))
      new Tip({parent: $(form).find("input[name=workNo]").parent(), direct: "up", type: "error", message: "工号至少6位数字", left: 140, top: 30}).tip()
      status = false
    else if (_.isEmpty($(form).find("input[name=workNo]").val()) and (not _.isEmpty($(form).find("input[name=name]").val()))) and (not /^[\u4E00-\u9FA5]{2,}$/.test($(form).find("input[name=name]").val()))
      new Tip({parent: $(form).find("input[name=name]").parent(), direct: "up", type: "error", message: "姓名至少两个中文字符", left: 140, top: 30}).tip()
      status = false
    status

  #邀请人员
  inviteMember: ->
    btn = $(@)
    supplierId = $(@).data("supplierId")
    categories=[]
    $.ajax
      url: "/api/qualify/getBcsCanInvite"
      type: "GET"
      data: {supplierId}
      async:false
      success:(data)=>
        categories = data || []
    modal = new Modal(memberSelectTemplate({categories, supplierId}))
    modal.show()
    $(".js-select-categories").on "click",-> $(".select-menu").slideToggle()
    $('#categories-select-menu-select-ul li').on "click", that.refreshCategories
    $(".category-close-btn").click(-> $(".select-menu").slideUp())
    $("#query-member-form").on "submit", that.renderNewMembers
    $(".js-render-new-members").on "click", {btn, modal}, that.renderNewMembers

  #根据勾选类目选项动态增删类目
  refreshCategories: ->
    that.checkedCategories = that.checkedCategories || {}
    parent = $(@)
    if parent.find(":checkbox").prop("checked") is true
      that.checkedCategories[parent.find(":input").val()] = parent.find(":input").data("name")
    else
      delete that.checkedCategories[parent.find(":input").val()]

  #展示新增的团队成员
  renderNewMembers: (evt)->
    evt.preventDefault()
    if that.validateTeamInput($("#query-member-form"))
      $form = $("#query-member-form")
      name = $form.find("input[name=name]").val()
      workNo = $form.find("input[name=workNo]").val()
      isSearch = true
      if (_.isEmpty $form.data("workName")) and (_.isEmpty $form.data("workNo"))
        #第一次进入直接查询
        $form.data "workName",name
        $form.data "workNo",workNo
      else if $form.data("workName") is name and $form.data("workNo") is workNo
        isSearch = false
      if isSearch
        $form.data "workName",name
        $form.data "workNo",workNo
        if _.isEmpty workNo
          url = "/api/haier/staff/name"
          data = {name}
        else
          url = "/api/haier/staff/workNo"
          data = {workNo}
        $.ajax
          url: url
          type: "GET"
          data: data
          success: (data)->
            $form.find(".team-list-group").remove()
            $form.append(teamMemberListTemplate({data}))
            if $(".new-member-info").length
              $(".new-member-info:first").attr "checked","checked"
              $(".js-render-new-members").text "确定"
            else
              new Tip({parent: $(".team-list-group"), direct: "up", type: "info", message: "无数据,请检查数据或者点击取消按钮", left: 60, top: 25}).tip()
      else
        if !$(".new-member-info").length or $(".new-member-info").length is 0
          new Tip({parent: $(".team-list-group"), direct: "up", type: "info", message: "无数据,请检查数据或者点击取消按钮", left: 60, top: 25}).tip()
        else if $(".new-member-info:checked").length
          that.selectmember($(".new-member-info:checked").data("number"), undefined)
        else
          new Tip({parent: $(".team-list-group"), direct: "up", type: "info", message: "请至少勾选一位成员", left: 60, top: 25}).tip()

  #提交用户
  selectmember: (nick)->
    bcIds=[]
    for id,v of that.checkedCategories
      bcIds.push(id)
    $.ajax
      url: "/api/qualify/invite"
      type: "POST"
      data: {nick: nick, supplierId: $("#query-member-form").data("supplierId"), role: $("select[name=role]").val(), bcIds:JSON.stringify(bcIds)}
      success: (data)->
        new Modal({
          "icon": "success",
          "title": "保存成功",
          "content": "信息已经保存成功，您可以继续其他操作"}).show()
        $(".close").on "click", ->
          window.location.reload()
      error: (data)->
        new Modal({
          "icon": "error",
          "content": data.responseText}).show()

module.exports = Qualify
