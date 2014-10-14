licenceTemplate = Handlebars.templates["admins/paperwork_info/templates/license"]

module.exports = class SupplierResumePaperworkInfo
  constructor: ->
    that = this
    newLicense = @$el.find("[data-license]").data("license")
    newLicense.blDate = moment(newLicense.blData).format("YYYY-MM-DD")
    newLicense.ocDate = moment(newLicense.ocDate).format("YYYY-MM-DD")
    newLicense.tnDate = moment(newLicense.tnDate).format("YYYY-MM-DD")
    newData = that.licenseReturn(newLicense)
    oldLicense = @$el.find("[data-license-old]").data("licenseOld")
    oldData = if oldLicense
      oldLicense.regCountry = oldLicense["company-reg-country"]
      oldLicense.businessLicense = oldLicense["company-business-license"]
      oldLicense.blDate = moment(oldLicense["company-business-license-date"]).format("YYYY-MM-DD") if oldLicense["company-business-license-date"]
      oldLicense.businessLicenseId = oldLicense["company-business-license-id"]
      oldLicense.orgCert = oldLicense["company-org-cert"]
      oldLicense.ocDate = moment(oldLicense["company-org-cert-date"]).format("YYYY-MM-DD") if oldLicense["company-org-cert-date"]
      oldLicense.orgCertId = oldLicense["company-org-cert-id"]
      oldLicense.taxNo = oldLicense["company-tax-no"]
      oldLicense.tnDate = moment(oldLicense["company-tax-no-date"]).format("YYYY-MM-DD") if oldLicense["company-tax-no-date"]
      oldLicense.taxNoId = oldLicense["company-tax-no-id"]
      that.licenseReturn(oldLicense)
    else
      []
    data = _.map newData, (license, i) ->
      {
        new: license,
        old: oldData[i]
      }
    @$el.find("[data-license]").append(licenceTemplate({data: data}))
  licenseReturn: (licenseData) ->
    data = switch licenseData.regCountry
      when 10
        [
          {"name": "CCIAA"}
        ]
      when 11
        [
          {"name": "Certificate of Incorporation"}
        ]
      when 2
        [
          {"name": "Business license"},
          {"name": "Tax ID certificate"}
        ]
      when 12
        [
          {"name": "Business license"},
          {"name": "Tax registration certificate"}
        ]
      when 13
        [
          {"name": "Businnes Registration Certificate"},
          {"name": "Investment License"},
          {"name": "Taxcode Certificate"}
        ]
      when 14
        [
          {"name": "Businnes Registration Certificate"}
        ]
      when 6
        [
          {"name": "會社登記簿誊本"}
        ]
      when 15
        []
      when 16
        []
      else
        [
          {"name": "企业营业执照"},
          {"name": "组织机构代码证"},
          {"name": "税务登记证"}
        ]
    _.map data, (object, i) ->
      if i is 0
        object.license = licenseData.businessLicense
        object.date = licenseData.blDate
        object.licenseId = licenseData.businessLicenseId
      else if i is 1
        object.license = licenseData.orgCert
        object.date = licenseData.ocDate
        object.licenseId = licenseData.orgCertId
      else if i is 2
        object.license = licenseData.taxNo
        object.date = licenseData.tnDate
        object.licenseId = licenseData.taxNoId
      object
