"use strict";
scorApp.factory('riskAsmtTemplateFactory', function($http, $q, baseUrl, localSessionFactory, scorHttpService) {
    return {
        getRiskAsmtTemplates: function() {
            return scorHttpService.getServiceRequest('/sysadmin/raTemplates/templates.json');
        },
        getRiskAsmtTemplate: function(riskAsmtTemplateId) {
			
			return scorHttpService.getServiceRequest('/api/risk-asmt-tplts/' + riskAsmtTemplateId);
            //return scorHttpService.getServiceRequest('/sysadmin/raTemplates/' + riskAsmtTemplateId + "-templates.json");
        },
        addRiskAsmtTemplate: function(riskAsmtTemplate) {
            return scorHttpService.postServiceRequest('/sysadmin/raTemplates', riskAsmtTemplate);
        },
        updateRiskAsmtTemplate: function(riskAsmtTemplate) {
            return scorHttpService.postServiceRequest('/sysadmin/raTemplates/' + riskAsmtTemplate.riskAssessmentTemplateId + '/edit', riskAsmtTemplate);
        },//
		getRiskAsmtRiskRangeLevels: function() {
            return scorHttpService.getServiceRequest('/sysadmin/raTemplates/riskLevels.json');
        },
		getRiskAsmtRiskRangeTypes: function(riskAsmtTemplateId) {
            return scorHttpService.getServiceRequest('/sysadmin/raTemplates/' + riskAsmtTemplateId + '-riskRangeTypes.json');
        },
		getRiskAsmtRiskFactors: function(riskRangeTypeId) {
            return scorHttpService.getServiceRequest('/sysadmin/raTemplates/' + riskRangeTypeId +  '-riskFactors.json');
        },
		getRiskAsmtRiskFactor: function(riskFactorId) {
            return scorHttpService.getServiceRequest('/sysadmin/raTemplates/' + riskFactorId + '-riskFactors.json');
        },
		getRiskAsmtRiskFactorLevels: function(riskFactorId) {
            return scorHttpService.getServiceRequest('/sysadmin/raTemplates/' + riskFactorId+ '-riskFactorLevels.json');
        }
    };
});
