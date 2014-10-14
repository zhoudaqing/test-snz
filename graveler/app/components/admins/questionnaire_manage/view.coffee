Modal = require "pokeball/components/modal"
Pagination = require "pokeball/components/pagination"

questionnaireDetailTemplate = Handlebars.templates["admins/questionnaire_manage/templates/questionnaire_info"]
class QuestionnaireManage
  constructor: ($)->
    @detailShow = $(".js-questionnaire-detail")
    @bindEvents()

  bindEvents: ->
    new Pagination(".question-pagination").total($(".question-pagination").data("total")).show()
    @detailShow.on "click", @showDetail

  showDetail: ->
    data = $(@).closest("tr").data("info")
    new Modal(questionnaireDetailTemplate(data)).show()


module.exports = QuestionnaireManage
