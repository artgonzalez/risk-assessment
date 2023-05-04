"use strict";
scorApp.controller('riskAssessmentController', function($scope, $location, $anchorScroll, $confirm, $mdDialog, $mdMedia, $rootScope, riskAssessmentFactory, dialogMessageFactory, formUtilFactory, $state, UserAuth, localSessionFactory, /*riskAsmtEntryFactory*/) {

    //$rootScope.setChildTab('contract_risk');
    $scope.riskAssessmentObj = {riskRangeTypes:[{riskFactors:[]}]};
    $scope.oldRiskAssessmentObj = {};
    $scope.isAddRiskAssessment = false;
	$scope.isEditRiskAssessment = false;
    $scope.riskAssessments = [];
    $scope.rowCount = 0;
    $scope.otherRiskFactorInfo = {};
    $scope.displayDeleteButton = false;
    $scope.riskAsmtIndex = 0;
    $scope.selectedAll = false;
    $scope.selectOrDeselectLabel = 'Select All';
    $scope.displayOnlyRiskAssessment = false;
    $scope.displayRecordSavedMsg = false;
    $scope.displayDeleteOtherRiskFactorsButton = false;
    $scope.vm = { currentPage: 1 };
    $scope.noOfRecordsPerPage = "5";
    $scope.noOfRec = $scope.noOfRecordsPerPage;
    $scope.reverseSort = false;
    $scope.errorFromRestService = false;
    $scope.displayRecordSavedMsg = false;
    $scope.isNoRecordsMessageDisabled = true;
    $scope.unableToRetrieveTemplate = false;
    $scope.otherRiskLevelLookupList = [];
    $scope.primaryRiskAssessorLookupList = [];
    $scope.displayLastUpdatedBySection = true;
    $scope.invalidRiskAssessmentDate = false;
    $scope.invalidRiskAssessmentFiscalYear = false;
    $scope.riskAssessmentDateGreaterThanToday = false;
    $scope.displayRecordDeletedMsg = false;
    $scope.displayPagination = false;
    $scope.isOtherRiskLevelRequired = false;
    $scope.required = false;
	$scope.riskRangeTypeTotalScore = [];
	$scope.riskRangeTypeRiskLevel = [];
	$scope.baseLineRiskLevelScore = 0;
	$scope.contractBaseLineRiskLevel = "";
	$scope.baseLineRiskRangeTypeRanges = [];
	$scope.isAddEditRiskAsmtVisible = false;
	$scope.addEditRiskAmtLabel = "";
	
	var msg;
    $scope.disableAdd = false;
    //$scope.contractInfo = JSON.parse(localSessionFactory.getContractHeaderInfo());
	
    $scope.checkContractStatus = function() {
        if ($scope.contractInfo.contractStatus.contractStatusId === "3" || $scope.contractInfo.contractStatus.contractStatusId === "4" || $scope.contractInfo.contractStatus.contractStatusId === "5" || $scope.contractInfo.contractStatus.contractStatusId === "13") {
            $scope.disableAdd = true;
        } else {
            $scope.disableAdd = false;
        }
    };

    //$scope.checkContractStatus();
	$scope.initializeRiskAssessmentModel = function(riskAssessmentTemplate){
		
		$scope.riskAssessmentObj.riskAssessmentTemplateId = riskAssessmentTemplate.riskAssessmentTemplateId;
		for(var i=0; i < riskAssessmentTemplate.riskRangeTypes.length; i++){
			for(var j=0; j < riskAssessmentTemplate.riskRangeTypes[i].riskFactors.length; j++){
				//$scope.riskAssessmentObj.riskRangeTypes[i].riskFactors[j].riskAssessmentId = 0;
				$scope.riskAssessmentObj.riskRangeTypes[i].riskFactors[j].riskRangeTypeId = riskAssessmentTemplate.riskRangeTypes[i].riskRangeTypeId;
				$scope.riskAssessmentObj.riskRangeTypes[i].riskFactors[j].riskFactorId = riskAssessmentTemplate.riskRangeTypes[i].riskFactors[j].riskFactorId;
			}
		}
		
		//console.log($scope.riskAssessmentObj);
		
	}
	
	$scope.createRiskAssessmentModelCalculatedFields = function(riskAssessment){
		console.log(riskAssessment);
		var riskRangeTypes = riskAssessment.riskRangeTypes;
		
		riskAssessment.createBaseLineRiskRanges = function (){
			var baseLineRiskRangesText = [];

			for(var j=0; j < riskRangeTypes[0].riskRangeTypeRanges.length; j++){
				baseLineRiskRangesText[j] = {
													level : "",
													min : 0,
													max : 0
												};
				
			}
			
			for(var i=0; i < riskRangeTypes.length; i++){
				
				for(var j=0; j < riskRangeTypes[i].riskRangeTypeRanges.length; j++){
					baseLineRiskRangesText[j].level = riskRangeTypes[i].riskRangeTypeRanges[j].riskLevel.levelDesc;
					baseLineRiskRangesText[j].min += riskRangeTypes[i].riskRangeTypeRanges[j].min;
					baseLineRiskRangesText[j].max += riskRangeTypes[i].riskRangeTypeRanges[j].max;
					
				}
				
				/*risk ranges produce a gap when summing risk range risk scores example:
				   Low = 3-6    Medium = 7-10    High = 11-15
				   Low = 3-7    Medium = 8-12    High = 13-17
				total:  6-13			15-22          24-32 <-- notice 13 jumps to 15 and 22 to 24
				this loop adjusts to produce correct result of
				Low = 6-13    Medium = 14-22    High = 23-32 according to legacy risk Assessment risk calculations
				*/
				
				for(var k=1; k < riskRangeTypes[i].riskRangeTypeRanges.length; k++){
					baseLineRiskRangesText[k].min = baseLineRiskRangesText[k-1].max + 1;
				}
			}
			
			riskAssessment.baseLineRiskRanges = baseLineRiskRangesText;
			
		};
		
		riskAssessment.baseLineRiskRangesText = function (){
			//if(riskAssessment.baseLineRiskRanges == undefined){
				//riskAssessment.createBaseLineRiskRanges();
			//}
			
			var riskRangesText = "";
			var riskRanges = riskAssessment.baseLineRiskRanges;
			
			for(var i=0; i < riskRanges.length; i++){
				riskRangesText += riskRanges[i].level + ' = ' + riskRanges[i].min + '-' + riskRanges[i].max + ' ';
			}
			
			return riskRangesText;
		};
		
		riskAssessment.baseLineRiskRangeText = function(){
			//if(riskAssessment.baseLineRiskRanges == undefined){
				//riskAssessment.createBaseLineRiskRanges();
			//}
			
			var riskRanges = riskAssessment.baseLineRiskRanges;
				
			return riskRanges[0].min + ' - ' + riskRanges[riskRanges.length-1].max;
		};
			
		riskAssessment.baseLineRiskLevelScore = 
			function(){
				var sum = 0;
				
				for(var i=0; i < riskRangeTypes.length; i++){
					for(var j=0; j < riskRangeTypes[i].riskFactors.length; j++){
						if(riskRangeTypes[i].riskFactors[j].riskFactorLevel == undefined){
							if(riskRangeTypes[i].riskFactors[j].score != undefined){
								sum = sum + riskRangeTypes[i].riskFactors[j].score;
							}
						}
						else{
							sum = sum + riskRangeTypes[i].riskFactors[j].riskFactorLevel.score;
						}
					}
				}
				
			return sum;
		};
				
		riskAssessment.baseLineRiskLevel = 
			function(){
				//if(riskAssessment.baseLineRiskRanges == undefined){
					//riskAssessment.createBaseLineRiskRanges();
				//}
				
				var riskLevel = "";
				var riskScore = riskAssessment.baseLineRiskLevelScore();
				var ranges = riskAssessment.baseLineRiskRanges;
				console.log(ranges);
				for(var i=0; i < ranges.length; i++){
					if(riskScore >= ranges[i].min && riskScore <= ranges[i].max){
						riskLevel = ranges[i].level;
						break;
					}
				}
				
				return riskLevel;
		};
		
		for(var i=0; i < riskAssessment.riskRangeTypes.length; i++){
			
			for(var j=0; j < riskAssessment.riskRangeTypes[i].riskFactors.length; j++){
				
				var riskFactor = riskAssessment.riskRangeTypes[i].riskFactors[j];
				//create riskFactor riskPoint
				riskAssessment.riskRangeTypes[i].riskFactors[j].riskPoints = 
					function(score, weightMultiplier){
							return (score == undefined || weightMultiplier == undefined) ? 0 : score * weightMultiplier;
					};
			}
			
			for(var j=0; j < riskAssessment.riskRangeTypes[i].riskRangeTypeRanges.length; j++){
				//create risk range riskRangeText
				riskAssessment.riskRangeTypes[i].riskRangeText = 
					function(ranges){
						return ranges[0].min + ' - ' + ranges[ranges.length-1].max;
					};
				//create risk range riskRangesText
				riskAssessment.riskRangeTypes[i].riskRangesText = 
					function(ranges){
						var rangesText = "";
						console.log(ranges);
						for(var idx=0; idx < ranges.length; idx++){
							rangesText += ranges[idx].riskLevel.levelDesc + ' = ' + ranges[idx].min + '-' + ranges[idx].max + '  ';
						}
						
						return rangesText;
					};
			}
		}
	};

    $scope.getContractRiskAssessments = function(pageNumber, noOfRecordsPerPage) {
        riskAssessmentFactory.getContractRiskAssessments(pageNumber - 1, noOfRecordsPerPage).then(function(response) {
            if (response.success) {
                $scope.data = response.data;
				$scope.riskAssessments = $scope.data.content;
				console.log($scope.riskAssessments.length);
				for(var i=0; i < $scope.riskAssessments.length; i++){
					$scope.createRiskAssessmentModelCalculatedFields($scope.riskAssessments[i]);
					$scope.riskAssessments[i].createBaseLineRiskRanges();
				}
				
                $scope.totalItems = $scope.riskAssessments.length;
                $scope.isNoRecordsMessageDisabled = true;
                $scope.displayPagination = true;
            } else {
                $scope.riskAssessments = [];
                $scope.totalItems = 0;
                $scope.isNoRecordsMessageDisabled = false;
                $scope.displayPagination = false;
                if ($scope.displayRecordDeletedMsg && $scope.vm.currentPage - 1 > 0) {
                    $scope.vm.currentPage = $scope.vm.currentPage - 1;
                    $scope.getContractRiskAssessments($scope.vm.currentPage, $scope.noOfRecordsPerPage);
                }
            }
        });
    };

    $scope.getContractRiskAssessments($scope.vm.currentPage, $scope.noOfRecordsPerPage);

    $scope.getLookupValues = function() {
        riskAssessmentFactory.getLookUpValues().then(function(response) {
            if (response.success) {
                $scope.data = response.data;
                $scope.otherRiskLevelLookupList = $scope.data.otherRiskLevelLookupList;
                $scope.primaryRiskAssessorLookupList = $scope.data.primaryRiskAssessorLookupList;
            }
        });
    };

    //$scope.getLookupValues();
	$scope.getRiskAssessmentTemplate = function(riskAssessmentTemplateId) {
        riskAssessmentFactory.getRiskAssessmentTemplate(riskAssessmentTemplateId).then(function(response) {
            if (response.success) {
                //$scope.data = response.data;
                $scope.riskRangeTypeList = response.data;
				
				for(var i=0; i < response.data.length; i++){
					$scope.riskRangeTypeTotalScore.push(0);
				}
				
            }
        });
    };
	
    $scope.InitializeNewRiskAssessment = function(scrollLocation) {
        $scope.contractRiskAsmtForm.$setPristine();
        $scope.riskAssessmentDateGreaterThanToday = false;
		
		riskAssessmentFactory.getRiskAssessmentTemplate(1).then(function(response) {
            if (response.success) {
				$scope.addEditRiskAmtLabel = "Add Risk Assessment";
                $scope.riskAssessmentObj = response.data;
				console.log($scope.riskAssessmentObj);
				
				$scope.initializeRiskAssessmentModel($scope.riskAssessmentObj);
				$scope.createRiskAssessmentModelCalculatedFields($scope.riskAssessmentObj);
				$scope.riskAssessmentObj.createBaseLineRiskRanges();
									
				for(var i=0; i < response.data.length; i++){
					$scope.riskRangeTypeTotalScore.push(0);
				}
				
				$scope.isAddEditRiskAsmtVisible = true;
				$scope.isEditRiskAssessment = false;
				$scope.isAddRiskAssessment = true;
            }
        });
		$scope.isAddEditRiskAsmtVisible = true;
		
        $scope.displayRecordSavedMsg = false;
        $scope.isNoRecordsMessageDisabled = true;
        $scope.displayRecordDeletedMsg = false;
        $scope.errorFromRestService = false;
        $scope.displayDeleteButton = false;
        $scope.displayDeleteOtherRiskFactorsButton = false;
        $scope.required = true;
        $scope.resetSelectAll();
    };
	
	$scope.setAssessedFactorProperties = function(riskRangeTypeId, riskFactorId, riskRange, riskFactor, riskFactorLevel){
		
		$scope.riskAssessmentObj.riskRangeTypes[riskRangeTypeId].riskFactors[riskFactorId].riskFactorLevelId = riskFactorLevel.riskFactorLevelId;
		$scope.riskAssessmentObj.riskRangeTypes[riskRangeTypeId].riskFactors[riskFactorId].riskFactorLevel = riskFactorLevel;

	}
	
	$scope.getBaseLineRiskRanges = function(riskRangeType){
		for(var i=0; i < riskRangeType.riskRangeTypeRanges.length; i++){
			$scope.baseLineRiskRangeTypeRanges[i].level = riskRangeType.riskRangeTypeRanges[i].level;
			$scope.baseLineRiskRangeTypeRanges[i].min += riskRangeType.riskRangeTypeRanges[i].min;
			$scope.baseLineRiskRangeTypeRanges[i].max += riskRangeType.riskRangeTypeRanges[i].max;
		}
		
		/*risk ranges produce a gap when summing risk range risk scores example:
		   Low = 3-6    Medium = 7-10    High = 11-15
		   Low = 3-7    Medium = 8-12    High = 13-17
		total:  6-13			15-22          24-32 <-- notice 13 jumps to 15 and 22 to 24
        this loop adjusts to produce correct result of
		Low = 6-13    Medium = 14-22    High = 23-32 according to legacy risk Assessment risk calculations
		*/
		for(var i=1; i < riskRangeType.riskRangeTypeRanges.length; i++){
			$scope.baseLineRiskRangeTypeRanges[i].min = $scope.baseLineRiskRangeTypeRanges[i-1].max + 1;
		}
	};
	
	$scope.getRiskRangeTotalScore = function(riskRangeIndex, riskFactors) {
		var sum = 0;
		
		var localRiskFactors = riskFactors;
		
		/*if(riskFactors == undefined){
			localRiskFactors = riskFactors.riskFactors;
		}*/
		
		for(var i=0; i < localRiskFactors.length; i++){
			if(localRiskFactors[i].riskFactorLevel == undefined){
				if(riskFactors[i].score != undefined){
					sum = sum + riskFactors[i].score;
				}
			}
			else{
				sum = sum + riskFactors[i].riskFactorLevel.score;
			}
		}
		
		$scope.riskRangeTypeTotalScore[riskRangeIndex] = sum;
	};
	
	$scope.getRiskRangeRiskLevel = function(riskRangeIndex, riskRange){
		
		if(riskRange.riskRangeTypeRanges != undefined){
			
			var riskLevel = "";
			var riskRangeScore = $scope.riskRangeTypeTotalScore[riskRangeIndex];
			
			for(var i=0; i < riskRange.riskRangeTypeRanges.length; i++){
				var riskRangeTypeRange = riskRange.riskRangeTypeRanges[i];
				
				if(riskRangeScore >= riskRangeTypeRange.min && riskRangeScore <= riskRangeTypeRange.max){
						
					riskLevel = riskRangeTypeRange.riskLevel.levelDesc;
					break;
				}
			}
			
			
			
			$scope.riskRangeTypeRiskLevel[riskRangeIndex] = riskLevel;
		}
	};
	
	$scope.editRiskRangeTotalScore = function(riskRangeIndex, riskFactor, riskFactorLevel){

		var sum = $scope.riskRangeTypeTotalScore[riskRangeIndex];
		
		sum -= riskFactor.weightMultiplier * riskFactor.riskFactorLevel.score;
		sum += riskFactor.weightMultiplier * riskFactorLevel.score;
		
		riskFactor.riskFactorLevel = riskFactorLevel;
	
		$scope.riskRangeTypeTotalScore[riskRangeIndex] = sum;
	};

    $scope.showAddRiskAsmtPanel = function(scrollLocation) {
        if ($scope.contractRiskAsmtForm.$dirty && !$scope.formNotChanged()) {
            msg = 'There are unsaved changes in the form. Do you want to discard?';
            dialogMessageFactory.getConfirmation(msg).then(function() {
                $scope.InitializeNewRiskAssessment(scrollLocation);
            }, function() {});
        } else {
            $scope.InitializeNewRiskAssessment(scrollLocation);
        }
    };

    $scope.InitializeExistingRiskAssessment = function(riskAssessment, scrollLocation) {
        $scope.required = false;
        $scope.invalidRiskAssessmentFiscalYear = false;
        $scope.riskAssessmentDateGreaterThanToday = false;
        if (!angular.isUndefined($scope.contractRiskAsmtForm.riskAssessmentFiscalYear)) {
            $scope.contractRiskAsmtForm.riskAssessmentFiscalYear.$setValidity($scope.contractRiskAsmtForm.riskAssessmentFiscalYear.$errors, true);
        }
        $scope.invalidRiskAssessmentDate = false;
        if (!angular.isUndefined($scope.contractRiskAsmtForm.riskAssessmentCompletedDate)) {
            $scope.contractRiskAsmtForm.riskAssessmentCompletedDate.$setValidity($scope.contractRiskAsmtForm.riskAssessmentCompletedDate.$errors, true);
        }
        $scope.contractRiskAsmtForm.$setPristine();
		
		riskAssessmentFactory.getContractRiskAssessment(riskAssessment.riskAssessmentId).then(function(response) {
			
			if (response.success) {
				$scope.addEditRiskAmtLabel = "Edit Risk Assessment";
				$scope.riskAssessmentObj = response.data;
				console.log($scope.riskAssessmentObj);
				$scope.isAddEditRiskAsmtVisible = true;
				$scope.isAddRiskAssessment = false;
				$scope.isEditRiskAssessment = true;
				$scope.baseLineRiskLevelScore = 0;
				$scope.createRiskAssessmentModelCalculatedFields($scope.riskAssessmentObj);
				$scope.riskAssessmentObj.createBaseLineRiskRanges();
			}
		});

        $scope.displayRecordSavedMsg = false;
        $scope.displayRecordDeletedMsg = false;
        $scope.isOtherRiskLevelRequired = false;
        $scope.displayDeleteButton = true;
        $scope.resetSelectAll();
    };

    $scope.editRiskAssessment = function(riskAssessment, scrollLocation) {
        if ($scope.contractRiskAsmtForm.$dirty && !$scope.formNotChanged()) {
            msg = 'There are unsaved changes in the form. Do you want to discard?';
            dialogMessageFactory.getConfirmation(msg).then(function() {
                $scope.InitializeExistingRiskAssessment(riskAssessment, scrollLocation);
            }, function() {});
        } else {
            $scope.InitializeExistingRiskAssessment(riskAssessment, scrollLocation);
        }
    };

    $scope.addOtherRiskFactor = function() {
        $scope.rowCount = $scope.rowCount + 1;
        $scope.otherRiskFactorInfo = {
            number: $scope.rowCount,
            riskFactorName: '',
            otherRiskLevel: '',
            otherRiskWeight: '',
            otherRiskPoint: ''
        };
        $scope.riskAssessmentObj.otherRiskFactorList.push($scope.otherRiskFactorInfo);
        $scope.displayDeleteOtherRiskFactorsButton = true;
        $scope.isOtherRiskLevelRequired = true;
    };

    $scope.chckedIndexes = [];

    $scope.deleteOtherRiskFactors = function() {
        if ($scope.chckedIndexes.length === 0) {
            msg = 'Please select other risk factor(s) to delete.';
            dialogMessageFactory.getAlertMsg(msg);
        } else {
            msg = 'Do you want to delete the selected other risk factor(s)?. If you select "YES", please make sure to save the Risk Assessment record to permanently delete the other risk factor(s).';
            dialogMessageFactory.getConfirmation(msg).then(function() {
                $scope.selectedAll = false;
                $scope.selectOrDeselectLabel = 'Select All';
                angular.forEach($scope.chckedIndexes, function(value) {
                    //index = $scope.riskAssessmentObj.otherRiskFactorList.indexOf(value);
                    $scope.riskAssessmentObj.otherRiskFactorList.splice($scope.riskAssessmentObj.otherRiskFactorList.indexOf(value), 1);
                });
                $scope.calculateActiveLegalAndContractRiskFactors($scope.riskAssessmentTemplate);
                for (var index = 0; index < $scope.riskAssessmentObj.otherRiskFactorList.length; index++) {
                    $scope.rowCount = $scope.rowCount + 1;
                    $scope.riskAssessmentObj.otherRiskFactorList[index].number = $scope.rowCount;
                }
                if ($scope.riskAssessmentObj.otherRiskFactorList.length === 0) {
                    $scope.displayDeleteOtherRiskFactorsButton = false;
                    $scope.isOtherRiskLevelRequired = false;
                    $scope.riskAssessmentObj.otherRiskLevelLookup = null;
                }
                $scope.chckedIndexes = [];
                $scope.calculateOtherRiskScore();
            }, function() {});
        }
    };

    $scope.deleteRiskAssessment = function(riskAssessment) {
        msg = 'Do you want to delete this risk assessment?';
        dialogMessageFactory.getConfirmation(msg).then(function() {
            riskAssessmentFactory.deleteContractRiskAssessment(riskAssessment.riskAssessmentId).then(function(response) {
                if (response.success) {
                    $scope.displayRecordDeletedMsg = true;
                    $scope.getContractRiskAssessments($scope.vm.currentPage, $scope.noOfRecordsPerPage);
                    $scope.isAddEditRiskAsmtVisible = false;                    
                    $scope.contractRiskAsmtForm.$setPristine();
                } else {
                    $scope.errorMsg = response.error.error.description;
                    $scope.errorFromRestService = true;
                    $scope.displayRecordDeletedMsg = false;
                }
            });
        }, function() {});
    };

    $scope.saveRiskAssessment = function(riskAssessment) {
        $location.hash("");
        $anchorScroll();
        //if (riskAssessment.otherRiskLevelLookup == null) {
            //riskAssessment.otherRiskLevelLookup = {};
        //}
        //var user_info = JSON.parse(localSessionFactory.getUser());
        //riskAssessment.updatedBy = '00000107183';//user_info.userName;
		
        dialogMessageFactory.showProgressBar();
        if ($scope.isAddRiskAssessment) {
			console.log(riskAssessment);
            riskAssessmentFactory.createRiskAssessment(riskAssessment).then(function(response) {
                if (response.success) {
					$scope.addEditRiskAmtLabel = "";
                    $scope.data = response.data;
                    $scope.displayOnlyRiskAssessment = true;
                    $scope.isAddEditRiskAsmtVisible = false;
                    $scope.displayRecordSavedMsg = true;
                    $scope.errorFromRestService = false;
                    $scope.displayLastUpdatedBySection = true;
                    riskAssessmentFactory.getContractRiskAssessment(response.data.riskAssessmentId).then(function(response) {
                        $scope.riskAssessmentObj = response.data;
						$scope.isAddEditRiskAsmtVisible = true;
						$scope.isAddRiskAssessment = false;
						$scope.isEditRiskAssessment = true;
						$scope.baseLineRiskLevelScore = 0;
						$scope.createRiskAssessmentModelCalculatedFields($scope.riskAssessmentObj);
						$scope.riskAssessmentObj.createBaseLineRiskRanges();
                    });
                    $scope.contractRiskAsmtForm.$setPristine();
                    $scope.getContractRiskAssessments($scope.vm.currentPage, $scope.noOfRecordsPerPage);
                    dialogMessageFactory.hideProgressBar();
                } else {
                    $scope.errorMsg = response.error.error.description;
                    $scope.errorFromRestService = true;
                    dialogMessageFactory.hideProgressBar();
                }
            });
        } else {
			console.log(riskAssessment);
            riskAssessmentFactory.updateRiskAssessment(riskAssessment.riskAssessmentId, riskAssessment).then(function(response) {
                if (response.success) {
					$scope.addEditRiskAmtLabel = "";
                    $scope.data = response.data;
                    $scope.displayOnlyRiskAssessment = true;
                    $scope.isAddEditRiskAsmtVisible = false;
                    $scope.displayRecordSavedMsg = true;
                    $scope.errorFromRestService = false;
                    riskAssessmentFactory.getContractRiskAssessment(response.data.riskAssessmentId).then(function(response) {
						console.log(response);
                        $scope.riskAssessmentObj = response.data;
						$scope.isAddEditRiskAsmtVisible = true;
						$scope.isAddRiskAssessment = false;
						$scope.isEditRiskAssessment = true;
						$scope.baseLineRiskLevelScore = 0;
						$scope.createRiskAssessmentModelCalculatedFields($scope.riskAssessmentObj);
                    });
                    $scope.contractRiskAsmtForm.$setPristine();
                    $scope.getContractRiskAssessments($scope.vm.currentPage, $scope.noOfRecordsPerPage);
                    dialogMessageFactory.hideProgressBar();
                } else {
                    $scope.errorMsg = response.error.error.description;
                    $scope.errorFromRestService = true;
                    dialogMessageFactory.hideProgressBar();
                }
            });
        }
    };
    $scope.selectAllRiskFactors = function() {
        $scope.selectedAll = !$scope.selectedAll;
        if ($scope.selectedAll) {
            $scope.selectOrDeselectLabel = 'Deselect All';
        } else {
            $scope.selectOrDeselectLabel = 'Select All';
        }
        angular.forEach($scope.riskAssessmentObj.otherRiskFactorList, function(item) {
            item.checked = $scope.selectedAll;
            if (item.checked) {
                if ($scope.chckedIndexes.indexOf(item) === -1) {
                    $scope.chckedIndexes.push(item);
                }
            } else {
                $scope.chckedIndexes.splice($scope.chckedIndexes.indexOf(item), 1);
            }
        });
    };

    $scope.checkedIndex = function(otherRiskFactor) {
        if ($scope.chckedIndexes.indexOf(otherRiskFactor) === -1 &&
            otherRiskFactor.checked) {
            $scope.chckedIndexes.push(otherRiskFactor);
            if ($scope.riskAssessmentObj.otherRiskFactorList.length === $scope.chckedIndexes.length) {
                $scope.selectedAll = true;
                $scope.selectOrDeselectLabel = 'Deselect All';
            }
        } else {
            $scope.chckedIndexes.splice($scope.chckedIndexes.indexOf(otherRiskFactor), 1);
            if ($scope.chckedIndexes.length === 0) {
                $scope.resetSelectAll();
            } else if ($scope.chckedIndexes.length > 0) {
                $scope.selectedAll = false;
                $scope.selectOrDeselectLabel = 'Select All';
            }
        }
    };




    $scope.calculateOtherRiskScore = function() {
        $scope.itemOtherRiskScore = 0;
        angular.forEach($scope.riskAssessmentObj.otherRiskFactorList, function(value, key) {
            if (!angular.isUndefined(value.otherRiskLevel) && !angular.isUndefined(value.otherRiskWeight)) {
                $scope.itemOtherRiskScore = $scope.itemOtherRiskScore + value.otherRiskLevel * value.otherRiskWeight;
                $scope.riskAssessmentObj.otherRiskFactorList[key].otherRiskPoint = $scope.riskAssessmentObj.otherRiskFactorList[key].otherRiskLevel * $scope.riskAssessmentObj.otherRiskFactorList[key].otherRiskWeight;
            }
        });
        $scope.riskAssessmentObj.otherRiskScore = $scope.itemOtherRiskScore;
    };

    $scope.resetSelectAll = function() {
        $scope.selectedAll = false;
        $scope.selectOrDeselectLabel = 'Select All';
        $scope.chckedIndexes = [];
    };

    $scope.cancelRiskAssessment = function() {
        msg = 'There are unsaved changes in the form. Do you want to discard?';
        $scope.displayRecordSavedMsg = false;
        if ($scope.isAddRiskAssessment) {
            if ($scope.contractRiskAsmtForm.$dirty && !$scope.formNotChanged()) {
                dialogMessageFactory.getConfirmation(msg).then(function() {
                    $scope.riskAssessmentObj = {};
					$scope.isAddRiskAssessment = false;
                    $scope.isOtherRiskLevelRequired = false;
                    $scope.contractRiskAsmtForm.$setPristine();
                    $scope.isAddEditRiskAsmtVisible = false;
                    $scope.displayOnlyRiskAssessment = false;
                    $scope.resetSelectAll();
                    $location.hash('');
                    $anchorScroll();
                }, function() {});
            } else {
				$scope.isAddRiskAssessment = false;
                $scope.riskAssessmentObj = {};
                $scope.resetSelectAll();
                $location.hash('');
                $anchorScroll();
                $scope.contractRiskAsmtForm.$setPristine();
                $scope.isAddEditRiskAsmtVisible = false;
                $scope.displayOnlyRiskAssessment = false;
            }
        } else {
            if ($scope.contractRiskAsmtForm.$dirty && !$scope.formNotChanged()) {
                dialogMessageFactory.getConfirmation(msg).then(function() {
					$scope.isEditRiskAssessment = false;
                    $scope.resetSelectAll();
                    $scope.isOtherRiskLevelRequired = false;
                    $scope.contractRiskAsmtForm.$setPristine();
                    $scope.isAddEditRiskAsmtVisible = false;
                    $scope.displayOnlyRiskAssessment = false;
                    $location.hash('');
                    $anchorScroll();
                }, function() {});
            } else {
				$scope.isEditRiskAssessment = false;
                $scope.resetSelectAll();
                $location.hash('');
                $anchorScroll();
                $scope.contractRiskAsmtForm.$setPristine();
                $scope.isAddEditRiskAsmtVisible = false;
                $scope.displayOnlyRiskAssessment = false;
            }
        }
    };

    $scope.setNoOfRecordsPage = function(noOfRecordsPerPage) {
        $scope.vm.currentPage = 1;
        $scope.noOfRec = noOfRecordsPerPage;
        $scope.getContractRiskAssessments($scope.vm.currentPage, $scope.noOfRecordsPerPage);
    };

    $scope.retrievePaginatedRiskAssessments = function() {
        $scope.getContractRiskAssessments($scope.vm.currentPage, $scope.noOfRec);
    };

    $scope.calculateActiveLegalAndContractRiskFactors = function(riskAssessmentTemplate) {
        $scope.activeLegalTemplates = 0;
        $scope.activeContractTemplates = 0;
        for (var index = 0; index < riskAssessmentTemplate.riskAssessmentTemplateLegals.length; index++) {
            if (riskAssessmentTemplate.riskAssessmentTemplateLegals[index].riskFactorStatus === true) {
                $scope.activeLegalTemplates = $scope.activeLegalTemplates + 1;
            }
        }
        for (index = 0; index < riskAssessmentTemplate.riskAssessmentTemplateContracts.length; index++) {
            if (riskAssessmentTemplate.riskAssessmentTemplateContracts[index].riskFactorStatus === true) {
                $scope.activeContractTemplates = $scope.activeContractTemplates + 1;
            }
        }
        $scope.rowCount = $scope.activeLegalTemplates + $scope.activeContractTemplates;
    };
    
    $scope.validateRiskAssessmentDate = function(){
    	//riskAsmtEntryFactory.validateRiskAssessmentDate($scope);
    }
    
    $scope.getCreatedDate = function(){
    	//return riskAsmtEntryFactory.getCreatedDate($scope);
    }

    $scope.validateFiscalYear = function() {
        if (!angular.isUndefined($scope.riskAssessmentObj.riskAssessmentDate) && $scope.riskAssessmentObj.riskAssessmentDate !== '') {
            var riskAssessmentDate = new Date($scope.riskAssessmentObj.riskAssessmentDate);
            if (riskAssessmentDate < new Date('01/01/1920') || riskAssessmentDate > new Date('12/31/2199')) {
                $scope.invalidRiskAssessmentDate = true;
                $scope.contractRiskAsmtForm.riskAssessmentCompletedDate.$setValidity($scope.contractRiskAsmtForm.riskAssessmentCompletedDate.$errors, false);
                $scope.contractRiskAsmtForm.riskAssessmentFiscalYear.$setValidity($scope.contractRiskAsmtForm.riskAssessmentFiscalYear.$errors, true);
            } else {
                $scope.invalidRiskAssessmentDate = false;
                $scope.contractRiskAsmtForm.riskAssessmentCompletedDate.$setValidity($scope.contractRiskAsmtForm.riskAssessmentCompletedDate.$errors, true);
                if (!angular.isUndefined($scope.riskAssessmentObj.riskAssessmentFiscalYear) && $scope.riskAssessmentObj.riskAssessmentFiscalYear !== "") {
                    var riskAssessmentYear = riskAssessmentDate.getFullYear();
                    var riskAssessmentFiscalYear = Number($scope.riskAssessmentObj.riskAssessmentFiscalYear);
                    if (riskAssessmentFiscalYear > (riskAssessmentYear + 1) || riskAssessmentFiscalYear < (riskAssessmentYear - 1)) {
                        $scope.invalidRiskAssessmentFiscalYear = true;
                        $scope.contractRiskAsmtForm.riskAssessmentFiscalYear.$setValidity($scope.contractRiskAsmtForm.riskAssessmentFiscalYear.$errors, false);
                    } else {
                        $scope.invalidRiskAssessmentFiscalYear = false;
                        $scope.contractRiskAsmtForm.riskAssessmentFiscalYear.$setValidity($scope.contractRiskAsmtForm.riskAssessmentFiscalYear.$errors, true);
                    }
                } else {
                    $scope.invalidRiskAssessmentFiscalYear = false;
                    //$scope.contractRiskAsmtForm.riskAssessmentFiscalYear.$setValidity($scope.contractRiskAsmtForm.riskAssessmentFiscalYear.$errors, false);
                }
            }
        } else {
            $scope.resetFormErrorMessages();
        }
    };

    $scope.validateOtherRiskLevel = function() {
        if ($scope.riskAssessmentObj.otherRiskLevelLookup == null && !$scope.isAddRiskAssessment) {
            $scope.isOtherRiskLevelRequired = true;
        } else {
            $scope.isOtherRiskLevelRequired = false;
        }
    };

    $scope.resetFormErrorMessages = function() {
        $scope.invalidRiskAssessmentDate = false;
        $scope.invalidRiskAssessmentFiscalYear = false;
    };

    var routeChangeOff = $scope.$on('$stateChangeStart',
        function(event, toState) {
            var destination = toState.name;
            event.preventDefault();
            msg = 'There are unsaved changes in the form. Do you want to discard?';
            if ($scope.contractRiskAsmtForm.$dirty && !$scope.formNotChanged()) {
                dialogMessageFactory.getConfirmation(msg).then(function() {
                    routeChangeOff();
                    $state.go(destination);
                }, function() {
                    $rootScope.setChildTab('contract_risk');
                });
            } else {
                routeChangeOff();
                $state.go(destination);
            }
        });

    $scope.formNotChanged = function() {
        return formUtilFactory.trueEqual($scope.oldRiskAssessmentObj, $scope.riskAssessmentObj) &&
            formUtilFactory.checkEqualityForList($scope.oldRiskAssessmentObj.otherRiskFactorList, $scope.riskAssessmentObj.otherRiskFactorList);
    };
});