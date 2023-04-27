"use strict";
scorApp.controller('riskAssessmentTemplateController', function($scope, $rootScope, $location, $anchorScroll, riskAsmtTemplateFactory, $parse, dialogMessageFactory, formUtilFactory, $state) {
    $rootScope.setChildTab('administration_contract_management_risk_assessment_template');
    $rootScope.displayContractManagementSubmenu = true;
    $scope.displayAddNewRiskAsmtTplPanel = false;
    $scope.displayOnlyRiskAsmtTemplate = false;
    $scope.displayEditCurrentRiskAsmtTplPanel = false;
    $scope.vm = { currentPage: 1 };
    $scope.noOfRecordsPerPage = "5";
    $scope.noOfRec = $scope.noOfRecordsPerPage;
    $scope.reverseSort = false;
    $scope.isPastTemplate = false;
    $scope.isCurrentTemplate = false;
    $scope.isFutureTemplate = false;
    $scope.isNewTemplate = false;
    $scope.isNoRecordsMessageDisabled = true;
    $scope.disableAddNewButton = false;
    $scope.errorFromRestService = false;
    $scope.displayRecordSavedMsg = false;
    $scope.isAllPanelsExpanded = false;
    $scope.message = '';
    $scope.isLoading = false;
    $scope.invalidLegalMediumMin = false;
    $scope.invalidLegalHighMin = false;
    $scope.legalMediumMinLessThanLegalLowMin = false;
    $scope.legalHighMaxLessThanLegalHighMin = false;
    $scope.invalidContractMediumMin = false;
    $scope.invalidContractHighMin = false;
    $scope.contractMediumMinLessThanContractLowMin = false;
    $scope.contractHighMaxLessThanContractHighMin = false;
	$scope.baseLineRiskRangeTotals = [];
	$scope.riskAssessmentObj = [];
	$scope.newRiskFactor = {};
	$scope.required = true;
	$scope.isAddEditDisplayRiskFactor = false;
	$scope.isAddEditRiskAsmtTemplate = false;
    var msg;

    $scope.riskAssessmentSummaryPanelStatus = [{
        open: true
    }];

    $scope.expandCollapseStatus = [{
        open: true
    }, {
        open: false
    }, {
        open: false
    }];


    $scope.effectiveDateOptions = {
        initDate: new Date(),
        minDate: new Date(),
        maxDate: new Date("12/31/2199")
    };

    $scope.expirationDateOptions = {
        initDate: new Date(),
        minDate: new Date(),
        maxDate: new Date("12/31/2199")
    };

    $scope.getRiskAsmtTemplates = function() {
        riskAsmtTemplateFactory.getRiskAsmtTemplates().then(function(response) {
            if (response.success) {
                $scope.data = response.data;
                $scope.riskAsmtTplVersions = $scope.data;
                $scope.totalItems = $scope.data.length;
                if ($scope.riskAsmtTplVersions.length > 0) {
                    var latestTemplateEffectiveDate = $scope.riskAsmtTplVersions[0].effectiveDate;
                    if (latestTemplateEffectiveDate > new Date()) {
                        $scope.disableAddNewButton = true;
                    }
                }
            } else {
                $scope.isNoRecordsMessageDisabled = false;
            }
        });
    };

    $scope.getRiskAsmtTemplates();
	
	$scope.createRiskAsmtTemplateCalculatedFields = function(riskAssessment){
		var riskRanges = riskAssessment.riskRangeTypes;
		
		riskAssessment.createBaseLineRiskRanges = function (){
			var baseLineRiskRangesText = [];
			
			for(var j=0; j < riskRanges[0].riskRangeTypeRanges.length; j++){
				baseLineRiskRangesText[j] = {
													level : "",
													min : 0,
													max : 0
												};
				
			}
			
			var valuesCount = 0;
			for(var i=0; i < riskRanges.length; i++){
				
				for(var j=0; j < riskRanges[i].riskRangeTypeRanges.length; j++){
					baseLineRiskRangesText[j].level = riskRanges[i].riskRangeTypeRanges[j].level;
					baseLineRiskRangesText[j].min += riskRanges[i].riskRangeTypeRanges[j].min;
					baseLineRiskRangesText[j].max += riskRanges[i].riskRangeTypeRanges[j].max;
				}
				
				/*risk ranges produce a gap when summing risk range risk scores example:
				   Low = 3-6    Medium = 7-10    High = 11-15
				   Low = 3-7    Medium = 8-12    High = 13-17
				total:  6-13			15-22          24-32 <-- notice 13 jumps to 15 and 22 to 24
				this loop adjusts to produce correct result of
				Low = 6-13    Medium = 14-22    High = 23-32 according to legacy risk Assessment risk calculations
				*/
				
				for(var k=1; k < riskRanges[i].riskRangeTypeRanges.length; k++){
					baseLineRiskRangesText[k].min = baseLineRiskRangesText[k-1].max + 1;
				}
			}
		
			riskAssessment.baseLineRiskRangesValues = [];
			var valuesCount = 0;
			
			for(var j = 0; j < baseLineRiskRangesText.length; j++){
				riskAssessment.baseLineRiskRangesValues[valuesCount++] = baseLineRiskRangesText[j].min;
				riskAssessment.baseLineRiskRangesValues[valuesCount++] = baseLineRiskRangesText[j].max;
			}
			
			riskAssessment.baseLineRiskRanges = baseLineRiskRangesText;
		}			
		
		var valuesCount = 0;		
		for(var i=0; i < riskRanges.length; i++){
			
			riskAssessment.riskRangeTypes[i].riskRangeTypeRangesLabels = [];
			riskAssessment.riskRangeTypes[i].riskRangeTypeRangesValues = [];
			
			for(var j=0; j < riskRanges[i].riskRangeTypeRanges.length; j++){
				riskRanges[i].riskRangeTypeRangesLabels[valuesCount] = "Min";
				riskRanges[i].riskRangeTypeRangesValues[valuesCount++] = riskRanges[i].riskRangeTypeRanges[j].min;

				riskRanges[i].riskRangeTypeRangesLabels[valuesCount] = "Max";
				riskRanges[i].riskRangeTypeRangesValues[valuesCount++] = riskRanges[i].riskRangeTypeRanges[j].max;
			}
			
			valuesCount = 0;
		}
	};
	
	$scope.calculateRiskRangeTypeRanges = function(riskRangeType){
		var lowMin = 0, lowMax = 0;
		var medMin = 0, medMax = 0;
		var highMin = 0, highMax = 0;
		
		for(var i=0; i < riskRangeType.riskFactors.length; i++){
			var riskFactor = riskRangeType.riskFactors[i];
			lowMin += riskFactor.weightMultiplier * $scope.getRiskFactorLevelsLowScore(riskFactor.riskFactorLevels);
			highMax += riskFactor.weightMultiplier * $scope.getRiskFactorLevelsHighScore(riskFactor.riskFactorLevels);
		}
		
		var rangePerMaxValues = Math.floor(highMax / 4);
		console.log(lowMin);
		console.log(lowMin + rangePerMaxValues);
		console.log(lowMin + rangePerMaxValues + 1);
		console.log(lowMin + rangePerMaxValues + rangePerMaxValues);
		console.log(lowMin + rangePerMaxValues + rangePerMaxValues + 1);
		console.log(highMax);
		
		
	};
	
	$scope.getRiskFactorLevelsLowScore = function(riskFactorLevels){
		var minScore = 1000;
		
		for(var i=0; i < riskFactorLevels.length; i++){
			if(riskFactorLevels[i].score < minScore){
				minScore = riskFactorLevels[i].score;
			}
		}
		
		return minScore;
	};
	
	$scope.getRiskFactorLevelsHighScore = function(riskFactorLevels){
		var maxScore = -1000;
		
		for(var i=0; i < riskFactorLevels.length; i++){
			if(riskFactorLevels[i].score > maxScore){
				maxScore = riskFactorLevels[i].score;
			}
		}
		
		return maxScore;
	};
	
    $scope.getRiskAsmtTemplate = function(riskAsmtId) {
        $scope.displayRecordSavedMsg = false;
        $scope.message = '';
        riskAsmtTemplateFactory.getRiskAsmtTemplate(riskAsmtId).then(function(response) {
            if (response.success) {
                $scope.data = response.data;
				$scope.riskRangeTypeIndex = 0;
				$scope.riskFactorIndex = 0;
				$scope.riskAssessmentObj = $scope.data;
				$scope.createRiskAsmtTemplateCalculatedFields($scope.riskAssessmentObj);
				$scope.riskAssessmentObj.createBaseLineRiskRanges();
                console.log($scope.riskAssessmentObj);
				$scope.addEditRiskAsmtTplVersion = $scope.data;
                $scope.addEditRiskAsmtTplVersion.version = parseFloat($scope.addEditRiskAsmtTplVersion.version).toFixed(1);
                $scope.isNewTemplate = false;
                if ($scope.addEditRiskAsmtTplVersion.effectiveDate > new Date() && ($scope.addEditRiskAsmtTplVersion.expirationDate > new Date() || $scope.addEditRiskAsmtTplVersion.expirationDate === null)) {
                    $scope.isNewTemplate = false;
                    $scope.isPastTemplate = false;
                    $scope.isCurrentTemplate = false;
                    $scope.isFutureTemplate = true;
                    $scope.displayOnlyRiskAsmtTemplate = false;
                    $scope.displayAddNewRiskAsmtTplPanel = true;
                    $scope.displayEditCurrentRiskAsmtTplPanel = false;
                    $location.hash('addNewRiskAsmtTemplate');
                    $anchorScroll();
                    $scope.addEditLabel = 'Edit Risk Assessment Template - ' + parseFloat($scope.addEditRiskAsmtTplVersion.version).toFixed(1);
                } else if ($scope.addEditRiskAsmtTplVersion.effectiveDate <= new Date() && ($scope.addEditRiskAsmtTplVersion.expirationDate > new Date() || $scope.addEditRiskAsmtTplVersion.expirationDate === null)) {
                    $scope.isNewTemplate = false;
                    $scope.isPastTemplate = false;
                    $scope.isCurrentTemplate = true;
                    $scope.isFutureTemplate = false;
                    $scope.displayOnlyRiskAsmtTemplate = false;
                    $scope.displayAddNewRiskAsmtTplPanel = false;
                    $scope.displayEditCurrentRiskAsmtTplPanel = true;
                    $location.hash('editCurrentRiskAsmtTemplate');
                    $anchorScroll();
                } else {
                    $scope.isNewTemplate = false;
                    $scope.isPastTemplate = true;
                    $scope.isCurrentTemplate = false;
                    $scope.isFutureTemplate = false;
                    $scope.displayOnlyRiskAsmtTemplate = true;
                    $scope.displayAddNewRiskAsmtTplPanel = false;
                    $scope.displayEditCurrentRiskAsmtTplPanel = false;
                    $location.hash('displayOnlyRiskAsmtTemplate');
                    $anchorScroll();
                }
                $scope.oldAddEditRiskAsmtTylVersion = angular.copy($scope.addEditRiskAsmtTplVersion);
            }
        });
        $scope.errorFromRestService = false;
    };

	$scope.getRiskAsmtRiskFactorLevels = function(riskFactorIndex) {
        $scope.riskFactorIndex = riskFactorIndex;
    };
	
    $scope.getRiskAsmtRiskFactors = function(riskRangeIndex) {
        $scope.riskRangeTypeIndex = riskRangeIndex;
    };

	$scope.prepareAddRiskFactor = function(riskFactors){
		$scope.isAddEditDisplayRiskFactor = true;
		$scope.newRiskFactor = {
								id: 0,
								name: "",
								riskFactorLevels: [],
								riskRangeTypeId: 0,
								weightMultiplier: 0,
			
								};
								
		console.log($scope.newRiskFactor);
		$scope.newRiskFactorIndex = riskFactors.length;
	};
	
    $scope.initializeGrids = function(riskTemplateId) {
		//$scope.getRiskAsmtRiskFactors($scope.riskAssessmentObj[0].riskRangeTypeId);
		//$scope.getRiskAsmtRiskFactorLevels($scope.riskAsmtRiskFactorList[0].riskFactorId);
    };
	
	$scope.initializeGrids(0);
	
    $scope.addNewRiskAsmtTemplate = function(scrollLocation) {
        $location.hash(scrollLocation);
        $anchorScroll();
        $scope.isNewTemplate = true;
        if ($scope.isNewTemplate || $scope.isFutureTemplate) {
            if ($scope.riskAsmtTemplateForm.$dirty && !$scope.formNotChanged()) {
                msg = 'There are unsaved changes in the form. Do you want to discard?';
                dialogMessageFactory.getConfirmation(msg).then(function() {
                    $scope.addEditRiskAsmtTplVersion = {};
                    $scope.resetForm();
                    $scope.isFutureTemplate = false;
                    $scope.initializeAddNewTemplate();
                }, function() {});
            } else {
                $scope.addEditRiskAsmtTplVersion = {};
                $scope.resetForm();
                $scope.initializeAddNewTemplate();
                $scope.isFutureTemplate = false;
            }
        } else {
            if ($scope.isCurrentTemplate && $scope.editCurrentRiskAsmtTemplateForm.$dirty) {
                msg = 'There are unsaved changes in the form. Do you want to discard?';
                dialogMessageFactory.getConfirmation(msg).then(function() {
                    $scope.editCurrentRiskAsmtTemplateForm.$setPristine();
                    $scope.displayEditCurrentRiskAsmtTplPanel = false;
                    $scope.isCurrentTemplate = false;
                    $scope.initializeAddNewTemplate();
                }, function() {
                    $scope.displayEditCurrentRiskAsmtTplPanel = true;
                });
            } else {
                $scope.editCurrentRiskAsmtTemplateForm.$setPristine();
                $scope.displayEditCurrentRiskAsmtTplPanel = false;
                $scope.isCurrentTemplate = false;
                $scope.initializeAddNewTemplate();
            }
        }
    };

    $scope.setNoOfRecordsPage = function(noOfRecordsPerPage) {
        $scope.vm.currentPage = 1;
        $scope.noOfRec = noOfRecordsPerPage;
    };

    $scope.createNewTemplateFromExisting = function() {
        $scope.displayAddNewRiskAsmtTplPanel = true;
        $scope.displayOnlyRiskAsmtTemplate = false;
        $scope.displayEditCurrentRiskAsmtTplPanel = false;
        $scope.isNewTemplate = true;
        $scope.addEditLabel = 'Add Risk Assessment Template';
        var latestVersionNumber = parseFloat($scope.riskAsmtTplVersions[0].version) + parseFloat(0.1);
        $scope.addEditRiskAsmtTplVersion.version = parseFloat(latestVersionNumber).toFixed(1);
        $scope.addEditRiskAsmtTplVersion.riskAssessmentTemplateId = 0;
        $scope.addEditRiskAsmtTplVersion.effectiveDate = null;
        $scope.addEditRiskAsmtTplVersion.expirationDate = null;
        $scope.addEditRiskAsmtTplVersion.riskAssessmentTplComment = '';
        $scope.errorFromRestService = false;
        $scope.isAllPanelsExpanded = false;
        for (var i = 0; i < 3; i++) {
            $scope.expandCollapseStatus[i].open = $scope.isAllPanelsExpanded;
        }
    };

    $scope.saveRiskAssessmentTemplate = function(riskAsmtTemplate) {
        $scope.isAtleastOneLegalRiskFactorEntered = false;
        $scope.isAtleastOneContractRiskFactorEntered = false;
		
		if($scope.isAddEditDisplayRiskFactor){
			var newRiskFactorIndex = $scope.riskAssessmentObj.riskRangeTypes[$scope.riskRangeTypeIndex].riskFactors.length;
			$scope.newRiskFactor.id = 0;
			$scope.newRiskFactor.riskRangeTypeId = $scope.riskAssessmentObj.riskRangeTypes[$scope.riskRangeTypeIndex].id;
			
			$scope.riskAssessmentObj.riskRangeTypes[$scope.riskRangeTypeIndex].riskFactors[$scope.newRiskFactorIndex] = $scope.newRiskFactor;
			console.log($scope.riskAssessmentObj.riskRangeTypes[$scope.riskRangeTypeIndex].riskFactors);
		}
		/*
        for (var index = 0; index < $scope.addEditRiskAsmtTplVersion.riskAssessmentTemplateLegals.length; index++) {
            if ($scope.addEditRiskAsmtTplVersion.riskAssessmentTemplateLegals[index].riskFactorStatus === true) {
                $scope.isAtleastOneLegalRiskFactorEntered = true;
                break;
            }
        }

        for (index = 0; index < $scope.addEditRiskAsmtTplVersion.riskAssessmentTemplateContracts.length; index++) {
            if ($scope.addEditRiskAsmtTplVersion.riskAssessmentTemplateContracts[index].riskFactorStatus === true) {
                $scope.isAtleastOneContractRiskFactorEntered = true;
                break;
            }
        }

        if ($scope.isAtleastOneLegalRiskFactorEntered === false && $scope.isAtleastOneContractRiskFactorEntered === false) {
            msg = 'Please enter at least one legal entity and one contract risk factor';
            dialogMessageFactory.getAlertMsg(msg);

        } else if ($scope.isAtleastOneLegalRiskFactorEntered === false) {
            msg = 'Please enter at least one legal entity risk factor';
            dialogMessageFactory.getAlertMsg(msg);
        } else if ($scope.isAtleastOneContractRiskFactorEntered === false) {
            msg = 'Please enter at least one contract risk factor';
            dialogMessageFactory.getAlertMsg(msg);
        } else {
            $scope.displayOnlyRiskAsmtTemplate = true;
            $scope.displayAddNewRiskAsmtTplPanel = false;
            $scope.displayEditCurrentRiskAsmtTplPanel = false;
            dialogMessageFactory.showProgressBar();
            if ($scope.isNewTemplate) {
                riskAsmtTemplateFactory.addRiskAsmtTemplate(riskAsmtTemplate).then(function(response) {
                    if (response.success) {
                        $scope.data = response.data;
                        $scope.message = $scope.data.message;
                        $scope.getRiskAsmtTemplates();
                        $scope.isNoRecordsMessageDisabled = true;
                        $scope.displayRecordSavedMsg = true;
                        $scope.riskAsmtTemplateForm.$setPristine();
                        $location.hash('displayRecordSavedMsg');
                        $anchorScroll();
                        dialogMessageFactory.hideProgressBar();
                    } else {
                        $scope.errorMsg = response.error.error.description;
                        $scope.errorFromRestService = true;
                        $scope.displayAddNewRiskAsmtTplPanel = false;
                        $scope.displayOnlyRiskAsmtTemplate = false;
                        $scope.displayEditCurrentRiskAsmtTplPanel = false;
                        $scope.displayRecordSavedMsg = false;
                        $scope.riskAsmtTemplateForm.$setPristine();
                        $location.hash('errorMessageDisplay');
                        $anchorScroll();
                        dialogMessageFactory.hideProgressBar();
                    }
                });
            } else {*/
			//riskAsmtTemplateFactory.addRiskFactor($scope.riskAssessmentObj.id, $scope.newRiskFactor).then(function(response) {
				//console.log(response);
                riskAsmtTemplateFactory.updateRiskAsmtTemplate(riskAsmtTemplate).then(function(response) {
				//riskAsmtTemplateFactory.addRiskFactor($scope.riskAssessmentObj.id, $scope.newRiskFactor).then(function(response) {
                    if (response.success) {
                        $scope.data = response.data;
                        $scope.message = $scope.data.message;
                        $scope.displayRecordSavedMsg = true;
                        $scope.getRiskAsmtTemplates();
                        $scope.riskAsmtTemplateForm.$setPristine();
                        //$scope.editCurrentRiskAsmtTemplateForm.$setPristine();

                        $location.hash('displayRecordSavedMsg');
                        $anchorScroll();
                        dialogMessageFactory.hideProgressBar();
                    } else {
                        $scope.errorMsg = response.error.error.description;
                        $scope.errorFromRestService = true;
                        $scope.displayAddNewRiskAsmtTplPanel = false;
                        $scope.displayOnlyRiskAsmtTemplate = false;
                        $scope.displayEditCurrentRiskAsmtTplPanel = false;
                        $scope.displayRecordSavedMsg = false;
                        $scope.riskAsmtTemplateForm.$setPristine();
                        //$scope.editCurrentRiskAsmtTemplateForm.$setPristine();
                        $location.hash('errorMessageDisplay');
                        $anchorScroll();
                        dialogMessageFactory.hideProgressBar();
                    }
                });
			
            //}
            $scope.isNewTemplate = false;
            $scope.isFutureTemplate = false;
            $scope.isCurrentTemplate = false;
        //}
    };

    $scope.cancelRiskAssessmentTemplate = function() {
        $scope.displayRecordSavedMsg = false;
        $scope.message = '';
        $scope.displayOnlyRiskAsmtTemplate = false;
        $scope.displayEditCurrentRiskAsmtTplPanel = false;
        $location.hash('');
        $anchorScroll();
        if ($scope.isNewTemplate || $scope.isFutureTemplate) {
            if ($scope.riskAsmtTemplateForm.$dirty /*&& !$scope.formNotChanged()*/) {
                msg = 'There are unsaved changes in the form. Do you want to discard?';
                dialogMessageFactory.getConfirmation(msg).then(function() {
                    for (var index = 0; index < 5; index++) {
                        $scope["legalRiskFactorNameRequired_" + index] = false;
                        $scope["legalRiskLevelScore4TextRequired_" + index] = false;
                        $scope["contractRiskFactorNameRequired_" + index] = false;
                        $scope["contractRiskLevelScore4TextRequired_" + index] = false;
                    }
                    $scope.addEditRiskAsmtTplVersion = {};
                    $scope.riskAsmtTemplateForm.$setPristine();
                    $scope.displayAddNewRiskAsmtTplPanel = false;
                    $scope.isNewTemplate = false;
                    $scope.isFutureTemplate = false;
                }, function() {});
            } else {
                $scope.addEditRiskAsmtTplVersion = {};
                $scope.riskAsmtTemplateForm.$setPristine();
                $scope.displayAddNewRiskAsmtTplPanel = false;
                $scope.isNewTemplate = false;
                $scope.isFutureTemplate = false;
            }
        } else {
            if ($scope.isCurrentTemplate && $scope.editCurrentRiskAsmtTemplateForm.$dirty) {
                msg = 'There are unsaved changes in the form. Do you want to discard?';
                dialogMessageFactory.getConfirmation(msg).then(function() {
                    $scope.editCurrentRiskAsmtTemplateForm.$setPristine();
                    $scope.displayEditCurrentRiskAsmtTplPanel = false;
                    $scope.isCurrentTemplate = false;
                }, function() {
                    $scope.displayEditCurrentRiskAsmtTplPanel = true;
                });
            } else {
				console.log('else');
                $scope.riskAsmtTemplateForm.$setPristine();
				$scope.newRiskFactor = {}
				if(!$scope.isAddEditDisplayRiskFactor){
					$scope.isAddEditRiskAsmtTemplate = false;
				}
				
				$scope.isAddEditDisplayRiskFactor = false;
                $scope.displayEditCurrentRiskAsmtTplPanel = false;
                $scope.isCurrentTemplate = false;
            }
        }
    };

    $scope.validateLegalMediumMinValue = function() {
        $scope.legalMediumMinLessThanLegalLowMin = false;
        $scope.invalidLegalMediumMin = false;
        $scope.invalidLegalHighMin = false;

        var legalMediumMin = $scope.addEditRiskAsmtTplVersion.legalMediumMin;
        var legalHighMin = $scope.addEditRiskAsmtTplVersion.legalHighMin;
        var legalLowMin = $scope.addEditRiskAsmtTplVersion.legalLowMin;

        if (!angular.isUndefined(legalHighMin) && $scope.legalHighMaxLessThanLegalHighMin === false) {
            $scope.riskAsmtTemplateForm.legalHighMin.$setValidity($scope.riskAsmtTemplateForm.legalHighMin.$errors, true);
        }

        if (!angular.isUndefined(legalMediumMin)) {
            $scope.riskAsmtTemplateForm.legalMediumMin.$setValidity($scope.riskAsmtTemplateForm.legalMediumMin.$errors, true);

            if (Number(legalMediumMin) < (Number(legalLowMin) + 2)) {
                $scope.legalMediumMinLessThanLegalLowMin = true;
                $scope.riskAsmtTemplateForm.legalMediumMin.$setValidity($scope.riskAsmtTemplateForm.legalMediumMin.$errors, false);
            }
            if (!angular.isUndefined(legalHighMin) && Number(legalMediumMin) > (Number(legalHighMin) - 2)) {
                $scope.invalidLegalMediumMin = true;
                $scope.riskAsmtTemplateForm.legalMediumMin.$setValidity($scope.riskAsmtTemplateForm.legalMediumMin.$errors, false);
            }
        }
    };

    $scope.validateLegalHighMinValue = function() {
        $scope.invalidLegalHighMin = false;
        $scope.invalidLegalMediumMin = false;
        $scope.legalHighMaxLessThanLegalHighMin = false;

        var legalMediumMin = $scope.addEditRiskAsmtTplVersion.legalMediumMin;
        var legalHighMin = $scope.addEditRiskAsmtTplVersion.legalHighMin;
        var legalHighMax = $scope.addEditRiskAsmtTplVersion.legalHighMax;

        if (!angular.isUndefined(legalMediumMin) && $scope.legalMediumMinLessThanLegalLowMin === false) {
            $scope.riskAsmtTemplateForm.legalMediumMin.$setValidity($scope.riskAsmtTemplateForm.legalMediumMin.$errors, true);
        }

        if (!angular.isUndefined(legalHighMin)) {
            $scope.riskAsmtTemplateForm.legalHighMin.$setValidity($scope.riskAsmtTemplateForm.legalHighMin.$errors, true);

            if (Number(legalHighMax) <= Number(legalHighMin)) {
                $scope.legalHighMaxLessThanLegalHighMin = true;
                $scope.riskAsmtTemplateForm.legalHighMin.$setValidity($scope.riskAsmtTemplateForm.legalHighMin.$errors, false);
            }
            if (!angular.isUndefined(legalMediumMin) && Number(legalMediumMin) > (Number(legalHighMin) - 2)) {
                $scope.invalidLegalHighMin = true;
                $scope.riskAsmtTemplateForm.legalHighMin.$setValidity($scope.riskAsmtTemplateForm.legalHighMin.$errors, false);
            }
        }
    };

    $scope.validateContractMediumMinValue = function() {
        $scope.contractMediumMinLessThanContractLowMin = false;
        $scope.invalidContractMediumMin = false;
        $scope.invalidContractHighMin = false;

        var contractMediumMin = $scope.addEditRiskAsmtTplVersion.contractMediumMin;
        var contractHighMin = $scope.addEditRiskAsmtTplVersion.contractHighMin;
        var contractLowMin = $scope.addEditRiskAsmtTplVersion.contractLowMin;

        if (!angular.isUndefined(contractHighMin) && $scope.contractHighMaxLessThanContractHighMin === false) {
            $scope.riskAsmtTemplateForm.contractHighMin.$setValidity($scope.riskAsmtTemplateForm.contractHighMin.$errors, true);
        }

        if (!angular.isUndefined(contractMediumMin)) {
            $scope.riskAsmtTemplateForm.contractMediumMin.$setValidity($scope.riskAsmtTemplateForm.contractMediumMin.$errors, true);

            if (Number(contractMediumMin) < (Number(contractLowMin) + 2)) {
                $scope.contractMediumMinLessThanContractLowMin = true;
                $scope.riskAsmtTemplateForm.contractMediumMin.$setValidity($scope.riskAsmtTemplateForm.contractMediumMin.$errors, false);
            }
            if (!angular.isUndefined(contractHighMin) && Number(contractMediumMin) > (Number(contractHighMin) - 2)) {
                $scope.invalidContractMediumMin = true;
                $scope.riskAsmtTemplateForm.contractMediumMin.$setValidity($scope.riskAsmtTemplateForm.contractMediumMin.$errors, false);
            }
        }
    };

    $scope.validateContractHighMinValue = function() {
        $scope.invalidContractHighMin = false;
        $scope.invalidContractMediumMin = false;
        $scope.contractHighMaxLessThanContractHighMin = false;

        var contractMediumMin = $scope.addEditRiskAsmtTplVersion.contractMediumMin;
        var contractHighMin = $scope.addEditRiskAsmtTplVersion.contractHighMin;
        var contractHighMax = $scope.addEditRiskAsmtTplVersion.contractHighMax;

        if (!angular.isUndefined(contractMediumMin) && $scope.contractMediumMinLessThanContractLowMin === false) {
            $scope.riskAsmtTemplateForm.contractMediumMin.$setValidity($scope.riskAsmtTemplateForm.contractMediumMin.$errors, true);
        }

        if (!angular.isUndefined(contractHighMin)) {
            $scope.riskAsmtTemplateForm.contractHighMin.$setValidity($scope.riskAsmtTemplateForm.contractHighMin.$errors, true);

            if (Number(contractHighMax) <= Number(contractHighMin)) {
                $scope.contractHighMaxLessThanContractHighMin = true;
                $scope.riskAsmtTemplateForm.contractHighMin.$setValidity($scope.riskAsmtTemplateForm.contractHighMin.$errors, false);
            }
            if (!angular.isUndefined(contractMediumMin) && Number(contractMediumMin) > (Number(contractHighMin) - 2)) {
                $scope.invalidContractHighMin = true;
                $scope.riskAsmtTemplateForm.contractHighMin.$setValidity($scope.riskAsmtTemplateForm.contractHighMin.$errors, false);
            }
        }
    };

    $scope.validateLegalRiskFactors = function(index) {
        var riskFactorStatus = $scope.addEditRiskAsmtTplVersion.riskAssessmentTemplateLegals[index].riskFactorStatus;
        var fieldName = "legalRiskFactorNameRequired_" + index;
        if (riskFactorStatus) {
            $scope[fieldName] = true;
        } else {
            $scope[fieldName] = false;
        }
    };

    $scope.validateLegalHighScore4Text = function(index) {
        var riskLevelScoreStatus = $scope.addEditRiskAsmtTplVersion.riskAssessmentTemplateLegals[index].riskLevelScore4Status;
        var fieldName = "legalRiskLevelScore4TextRequired_" + index;
        if (riskLevelScoreStatus) {
            $scope[fieldName] = true;
        } else {
            $scope[fieldName] = false;
        }
    };

    $scope.validateContractRiskFactors = function(index) {
        var riskFactorStatus = $scope.addEditRiskAsmtTplVersion.riskAssessmentTemplateContracts[index].riskFactorStatus;
        var fieldName = "contractRiskFactorNameRequired_" + index;
        if (riskFactorStatus) {
            $scope[fieldName] = true;
        } else {
            $scope[fieldName] = false;
        }
    };

    $scope.validateContractHighScore4Text = function(index) {
        var riskLevelScore4Status = $scope.addEditRiskAsmtTplVersion.riskAssessmentTemplateContracts[index].riskLevelScore4Status;
        var fieldName = "contractRiskLevelScore4TextRequired_" + index;
        if (riskLevelScore4Status) {
            $scope[fieldName] = true;
        } else {
            $scope[fieldName] = false;
        }
    };


    $scope.validateEffectiveDate = function() {
        var effectiveDate = $scope.addEditRiskAsmtTplVersion.effectiveDate;
        var currentDate = new Date();
        currentDate.setHours(0, 0, 0, 0);
        if (!(angular.isUndefined(effectiveDate)) && (effectiveDate < currentDate)) {
            $scope.isEffectiveDateInvalid = true;
            $scope.riskAsmtTemplateForm.effectiveDate.$setValidity($scope.riskAsmtTemplateForm.effectiveDate.$errors, false);
        } else {
            $scope.isEffectiveDateInvalid = false;
            $scope.riskAsmtTemplateForm.effectiveDate.$setValidity($scope.riskAsmtTemplateForm.effectiveDate.$errors, true);
        }
    };

    $scope.validateExpirationDate = function() {
       /* var expirationDate = $scope.addEditRiskAsmtTplVersion.expirationDate;
        var effectiveDate = $scope.addEditRiskAsmtTplVersion.effectiveDate;
        var currentDate = new Date();
        currentDate.setHours(0, 0, 0, 0);
        if (!angular.isUndefined(expirationDate) && (expirationDate !== null)) {
            if ($scope.isNewTemplate || $scope.isFutureTemplate) {
                if (expirationDate <= currentDate) {
                    $scope.isExpirationDateLessThanCurrentDate = true;
                    $scope.riskAsmtTemplateForm.expirationDate.$setValidity($scope.riskAsmtTemplateForm.expirationDate.$errors, false);
                } else if (!angular.isUndefined(effectiveDate) && expirationDate <= effectiveDate) {
                    $scope.isExpirationDateInvalid = true;
                    $scope.riskAsmtTemplateForm.expirationDate.$setValidity($scope.riskAsmtTemplateForm.expirationDate.$errors, false);
                } else {
                    $scope.isExpirationDateInvalid = false;
                    $scope.isExpirationDateLessThanCurrentDate = false;
                    $scope.riskAsmtTemplateForm.expirationDate.$setValidity($scope.riskAsmtTemplateForm.expirationDate.$errors, true);
                }
            } else {
                if (expirationDate <= currentDate) {
                    $scope.isCurrentExpirationDateLessThanCurrentDate = true;
                    $scope.editCurrentRiskAsmtTemplateForm.expirationDate.$setValidity($scope.editCurrentRiskAsmtTemplateForm.expirationDate.$errors, false);
                } else if (expirationDate <= effectiveDate) {
                    $scope.isCurrentExpirationDateInvalid = true;
                    $scope.editCurrentRiskAsmtTemplateForm.expirationDate.$setValidity($scope.editCurrentRiskAsmtTemplateForm.expirationDate.$errors, false);
                } else {
                    $scope.isCurrentExpirationDateInvalid = false;
                    $scope.isCurrentExpirationDateLessThanCurrentDate = false;
                    $scope.editCurrentRiskAsmtTemplateForm.expirationDate.$setValidity($scope.editCurrentRiskAsmtTemplateForm.expirationDate.$errors, true);
                }
            }
        } else {
            $scope.isCurrentExpirationDateInvalid = false;
            $scope.isCurrentExpirationDateLessThanCurrentDate = false;
            $scope.isExpirationDateLessThanCurrentDate = false;
            if (!angular.isUndefined($scope.editCurrentRiskAsmtTemplateForm.expirationDate)) {
                $scope.editCurrentRiskAsmtTemplateForm.expirationDate.$setValidity($scope.editCurrentRiskAsmtTemplateForm.expirationDate.$errors, true);
            }
            $scope.isExpirationDateInvalid = false;
            if (!angular.isUndefined($scope.riskAsmtTemplateForm.expirationDate)) {
                $scope.riskAsmtTemplateForm.expirationDate.$setValidity($scope.riskAsmtTemplateForm.expirationDate.$errors, true);
            }
        }*/
    };

    $scope.initializeAddNewTemplate = function() {
        $scope.addEditLabel = 'Add Risk Assessment Template';
        $scope.isNewTemplate = true;
        $scope.isAllPanelsExpanded = false;
        for (var i = 0; i < 3; i++) {
            $scope.expandCollapseStatus[i].open = $scope.isAllPanelsExpanded;
        }
        var latestVersionNumber;
        if ($scope.riskAsmtTplVersions !== undefined && $scope.riskAsmtTplVersions.length > 0) {
            latestVersionNumber = parseFloat($scope.riskAsmtTplVersions[0].version) + parseFloat(0.1);
        } else {
            latestVersionNumber = 1.0;
        }
        $scope.addEditRiskAsmtTplVersion = {
            "version": parseFloat(latestVersionNumber).toFixed(1),
            "legalRiskRangeType": "Legal Entity Risk Range",
            "contractRiskRangeType": "Contract Risk Range",
            "baselineRiskRangeType": "Baseline Risk Range"
        };
        $scope.addEditRiskAsmtTplVersion.riskAssessmentTemplateLegals = [];
        $scope.addEditRiskAsmtTplVersion.riskAssessmentTemplateContracts = [];
        for (var index = 0; index < 5; index++) {
            $scope.riskAssessmentTemplateLegal = {
                "lowScoreLabel2": "Score 1",
                "mediumScoreLabel2": "Score 3",
                "highScoreLabel2": "Score 5",
                "highScoreLabel4": "Score 7",
                "riskLevelScore1": 1,
                "riskLevelScore1Status": true,
                "riskLevelScore2": 3,
                "riskLevelScore2Status": true,
                "riskLevelScore3": 5,
                "riskLevelScore3Status": true,
                "riskLevelScore4": 7,
                "riskLevelScore4Status": false
            };
            $scope.addEditRiskAsmtTplVersion.riskAssessmentTemplateLegals.push($scope.riskAssessmentTemplateLegal);
            $scope.riskAssessmentTemplateContract = {
                "lowScoreLabel2": "Score 1",
                "mediumScoreLabel2": "Score 3",
                "highScoreLabel2": "Score 5",
                "highScoreLabel4": "Score 7",
                "riskLevelScore1": 1,
                "riskLevelScore1Status": true,
                "riskLevelScore2": 3,
                "riskLevelScore2Status": true,
                "riskLevelScore3": 5,
                "riskLevelScore3Status": true,
                "riskLevelScore4": 7,
                "riskLevelScore4Status": false
            };
            $scope.addEditRiskAsmtTplVersion.riskAssessmentTemplateContracts.push($scope.riskAssessmentTemplateContract);
        }
        $scope.isNewTemplate = true;
        $scope.displayAddNewRiskAsmtTplPanel = true;
        $scope.displayOnlyRiskAsmtTemplate = false;
        $scope.displayEditCurrentRiskAsmtTplPanel = false;
        $scope.errorFromRestService = false;
    };

    $scope.resetForm = function() {
        $scope.isExpirationDateInvalid = false;
        if (!angular.isUndefined($scope.riskAsmtTemplateForm.expirationDate)) {
            $scope.riskAsmtTemplateForm.expirationDate.$setValidity($scope.riskAsmtTemplateForm.expirationDate.$errors, true);
        }
        /*$scope.isCurrentExpirationDateInvalid = false;
        if (!angular.isUndefined($scope.editCurrentRiskAsmtTemplateForm.expirationDate)) {
            $scope.editCurrentRiskAsmtTemplateForm.expirationDate.$setValidity($scope.editCurrentRiskAsmtTemplateForm.expirationDate.$errors, true);
        }*/

        $scope.isCurrentExpirationDateLessThanCurrentDate = false;
        $scope.isExpirationDateLessThanCurrentDate = false;
        if ($scope.isEffectiveDateInvalid) {
            $scope.isEffectiveDateInvalid = false;
            $scope.riskAsmtTemplateForm.effectiveDate.$setValidity($scope.riskAsmtTemplateForm.effectiveDate.$errors, true);
        }

        $scope.riskAsmtTemplateForm.$setPristine();
        //$scope.editCurrentRiskAsmtTemplateForm.$setPristine();
    };

    $scope.initializeExistingTemplate = function(riskAsmtId) {
		$scope.isAddEditRiskAsmtTemplate = true;
        if (($scope.riskAsmtTemplateForm.$dirty /*|| $scope.editCurrentRiskAsmtTemplateForm.$dirty*/) && !$scope.formNotChanged()) {
            msg = 'There are unsaved changes in the form. Do you want to discard?';
            dialogMessageFactory.getConfirmation(msg).then(function() {
                $scope.resetForm();
                //$scope.isAllPanelsExpanded = false;
                //for (var i = 0; i < 3; i++) {
                  //  $scope.expandCollapseStatus[i].open = $scope.isAllPanelsExpanded;
                //}
                $scope.getRiskAsmtTemplate(riskAsmtId);
            }, function() {});
        } else {
            /*$scope.isAllPanelsExpanded = false;
            for (var i = 0; i < 3; i++) {
                $scope.expandCollapseStatus[i].open = $scope.isAllPanelsExpanded;
            }*/
            $scope.getRiskAsmtTemplate(riskAsmtId);
        }
    };

    $scope.expandCollapseAll = function() {
        $scope.isAllPanelsExpanded = !$scope.isAllPanelsExpanded;
        if ($scope.expandCollapseStatus[0].open === true && $scope.expandCollapseStatus[1].open === true && $scope.expandCollapseStatus[2].open === true) {
            $scope.isAllPanelsExpanded = false;
        } else if ($scope.expandCollapseStatus[0].open === false && $scope.expandCollapseStatus[1].open === false && $scope.expandCollapseStatus[2].open === false) {
            $scope.isAllPanelsExpanded = true;
        }
        for (var i = 0; i < 3; i++) {
            $scope.expandCollapseStatus[i].open = $scope.isAllPanelsExpanded;
        }
    };

    var routeChangeOff = $scope.$on('$stateChangeStart',
        function(event, toState) {
            var destination = toState.name;
            event.preventDefault();
            var msg = 'There are unsaved changes in the form. Do you want to discard?';
            if ($scope.riskAsmtTemplateForm.$dirty && !$scope.formNotChanged()) {
                dialogMessageFactory.getConfirmation(msg).then(function() {
                    routeChangeOff();
                    $state.go(destination);
                }, function() {
                    $rootScope.setChildTab('administration_contract_management_risk_assessment_template');
                });
            } else {
                routeChangeOff();
                $state.go(destination);
            }

        });

    $scope.formNotChanged = function() {
        /*if (!angular.isUndefined($scope.oldAddEditRiskAsmtTylVersion)) {
            return formUtilFactory.trueEqual($scope.oldAddEditRiskAsmtTylVersion, $scope.addEditRiskAsmtTplVersion) &&
                formUtilFactory.checkEqualityForList($scope.oldAddEditRiskAsmtTylVersion.riskAssessmentTemplateContracts, JSON.parse(angular.toJson($scope.addEditRiskAsmtTplVersion.riskAssessmentTemplateContracts))) &&
                formUtilFactory.checkEqualityForList($scope.oldAddEditRiskAsmtTylVersion.riskAssessmentTemplateLegals, JSON.parse(angular.toJson($scope.addEditRiskAsmtTplVersion.riskAssessmentTemplateLegals)));
        }*/
    };
});