ruleTemplate = Handlebars.templates["admins/qualify_rule/templates/rule"]
Modal = require "pokeball/components/modal"

class qualifyRule
  constructor: ->
    @$jsAddRule = $(".js-add-rule")
    @jsDeleteRule = ".js-delete-rule"
    @$qualifyRuleForm = $("#qualify-rule-form")
    @bindEvent()

  that = this
  bindEvent: ->
    that = this
    @$jsAddRule.on "click", @addRule
    $(document).on "click", @jsDeleteRule, @deleteRule
    @$qualifyRuleForm.on "submit", @ruleFormSubmit

  #增加一行规则
  addRule: ->
    length = $(".rule-group").length
    $(@).closest(".control-group").before(ruleTemplate({data: length}))

  #删除一行规则
  deleteRule: ->
    $(@).closest(".control-group").remove()

  #整理校验的条目内容
  organizeRule: ->
    data = []
    $.each $(".rule-group"), (i, div)->
      data.push {"name": $(div).find("input[name=name]").val(), "role": $(div).find("select[name=role]").val(), "type": $(div).find(".rule-type:checked").val()}
    data

  #提交表单
  ruleFormSubmit: (evt)->
    evt.preventDefault()
    data = that.organizeRule()
    $.ajax
      url: "/api/qualify/rule"
      type: "POST"
      data: {subjects: JSON.stringify(data)}
      success: (data)->
        new Modal
          "icon": "success"
          "title": "保存成功"
          "content": "信息已经保存成功，您可以继续其他操作"
        .show()

module.exports = qualifyRule
