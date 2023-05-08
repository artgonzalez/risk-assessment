"use strict";
scorApp.factory('riskAssessmentFactory', function($http, $q, baseUrl, localSessionFactory, scorHttpService) {
    return {
        getRiskAssessmentTemplate: function(riskAsmtTemplateId) {            
            return scorHttpService.getServiceRequest('/api/risk-asmt-tplts/' + riskAsmtTemplateId);
        },
        getCurrentRiskAssessmentTemplate: function() {            
            return scorHttpService.getServiceRequest('/sysadmin/raTemplates/currentTemplate');
        },
        getContractRiskAssessments: function(pageNumber, noOfRecordsPerPage) {
            //var contractId = localSessionFactory.getContractId();
			var contractId = 1;
            //return scorHttpService.getServiceRequest('/contracts/' + contractId + '/riskAssessments?page=' + pageNumber + '&size=' + noOfRecordsPerPage + '&sort=riskAssessmentDate,riskAssessmentId');
			//return scorHttpService.getServiceRequest('/contracts/' + contractId + '/riskAssessments/risk-assessment-summary-list.json/?page=' + pageNumber + '&size=' + noOfRecordsPerPage + '&sort=riskAssessmentDate,riskAssessmentId');
			return scorHttpService.getServiceRequest('/api/contracts/'  + contractId +'/risk-assessments?page=' + pageNumber + '&size=' + noOfRecordsPerPage + '&sort=riskAssessmentDate,riskAssessmentId');
        },
        getContractRiskAssessment: function(riskAssessmentId) {
            //var contractId = localSessionFactory.getContractId();
            //return scorHttpService.getServiceRequest('/contracts/' + contractId + '/riskAssessments/' + riskAssessmentId);
			return scorHttpService.getServiceRequest('/api/contracts/risk-assessments/' + riskAssessmentId);
        },
		getRiskRangeTypesByRiskAssessmentTemplate: function(riskAssessmentTemplateId) {  
            //return scorHttpService.getServiceRequest('/contracts/riskAssessments/' + riskAssessmentTemplateId + '-risk-range-types.json');
			return scorHttpService.getServiceRequest('/api/risk-asmt-tplts/' + riskAssessmentTemplateId + '/risk-range-types');
        },
		getRiskFactorByRiskRangeTypes: function(riskRangeTypeId) {            
            //return scorHttpService.getServiceRequest('/contracts/riskAssessments/' + riskRangeTypeIdId + '-risk-range-type-risk-factors.json');
			return scorHttpService.getServiceRequest('/api/risk-asmt-tplts/risk-range-types/'+ riskRangeTypeId +'/risk-factors');
        },
		getRiskFactorLevelsByRiskFactorId: function(riskFactorId) {            
            //return scorHttpService.getServiceRequest('/contracts/riskAssessments/' + riskRangeTypeIdId + '-risk-range-type-risk-factors.json');
			return scorHttpService.getServiceRequest('/api/risk-asmt-tplts/risk-range-types/risk-factors/'+ riskFactorId +'/risk-factor-levels');
        },
        deleteContractRiskAssessment: function(riskAssessmentId) {
            var contractId = localSessionFactory.getContractId();            
            return scorHttpService.postServiceRequest('/contracts/' + contractId + '/riskAssessments/' + riskAssessmentId + '/delete', null);
        },
        getLookUpValues: function() {
           return scorHttpService.getServiceRequest('/lookups/riskAssessment');
        },
        createRiskAssessmentB: function(riskAssessment) {
            //var contractId = localSessionFactory.getContractId();            
			var contractId = 1;
            return scorHttpService.postServiceRequest('/api/contracts/' + contractId + '/risk-assessments', riskAssessment);
        },
        updateRiskAssessmentB: function(riskAssessmentId, riskAssessment) {
            var contractId = localSessionFactory.getContractId();            
            return scorHttpService.postServiceRequest('/api/contracts/risk-assessments/' + riskAssessmentId, riskAssessment);
        },
		createRiskAssessedFactor: function(riskAssessedFactor) {
            
            return scorHttpService.postServiceRequest('/api/contracts/risk-assessments/1/assessed-risk-factors');
        },
    };
});