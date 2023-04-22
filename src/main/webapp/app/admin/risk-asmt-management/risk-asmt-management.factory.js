"use strict";
scorApp.factory('riskAssessmentManagementFactory', function($http, $q, baseUrl, localSessionFactory, scorHttpService) {
    return {
        getRiskAssessmentTemplate: function(riskAsmtTemplateId) {            
            return scorHttpService.getServiceRequest('/sysadmin/raTemplates/' + riskAsmtTemplateId);            
        },
        getCurrentRiskAssessmentTemplate: function() {            
            return scorHttpService.getServiceRequest('/sysadmin/raTemplates/currentTemplate');
        },
        getContractRiskAssessments: function(contractId, pageNumber, noOfRecordsPerPage) {            
            return scorHttpService.getServiceRequest('/contracts/' + contractId + '/riskAssessments?page=' + pageNumber + '&size=' + noOfRecordsPerPage + '&sort=riskAssessmentDate,riskAssessmentId');
        },
        getContractRiskAssessment: function(contractId, riskAssessmentId) {            
            return scorHttpService.getServiceRequest('/contracts/' + contractId + '/riskAssessments/' + riskAssessmentId);
        },
        deleteContractRiskAssessment: function(contractId, riskAssessmentId) {
            return scorHttpService.postServiceRequest('/sysadmin/' + contractId + '/riskAssessmentsEntry/' + riskAssessmentId + '/delete', null);
        },
        getLookUpValues: function() {
            return scorHttpService.getServiceRequest('/lookups/riskAssessment');
        },
        createRiskAssessment: function(contractId, riskAssessment) {
            return scorHttpService.postServiceRequest('/sysadmin/' + contractId + '/riskAssessmentsEntry', riskAssessment);
        },
        updateRiskAssessment: function(contractId, riskAssessmentId, riskAssessment) {
            return scorHttpService.postServiceRequest('/sysadmin/' + contractId + '/riskAssessmentsEntry/' + riskAssessmentId + '/edit', riskAssessment);
        },
        getContractHeader: function(contractNumber) {
            return scorHttpService.getServiceRequest('/contracts/header/' + contractNumber);
        }
    };
});