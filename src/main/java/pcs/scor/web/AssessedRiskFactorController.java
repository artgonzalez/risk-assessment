package pcs.scor.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pcs.scor.model.risk.asmt.AssessedRiskFactor;
import pcs.scor.model.risk.asmt.RiskAssessmentRiskRangeType;
import pcs.scor.service.AssessedRiskFactorService;

@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:8082"})
@RestController
@RequestMapping("/api")
public class AssessedRiskFactorController {
	
	@Autowired
	AssessedRiskFactorService riskAssessmentRiskFactorService;

	@RequestMapping(method = RequestMethod.GET, value="risk-assessments/{riskAssessmentId}/risk-range-types/risk-factors/risk-factor-level")
	public ResponseEntity<List<RiskAssessmentRiskRangeType>> getRiskAssessmentId(@PathVariable("riskAssessmentId") long riskAssessmentId){
		
		List<RiskAssessmentRiskRangeType> riskAssessmentRiskFactor = 
				riskAssessmentRiskFactorService.findByRiskAssessmentId(riskAssessmentId);
		
		return new ResponseEntity<>(riskAssessmentRiskFactor, HttpStatus.OK);
	}	
	
	@RequestMapping(method = RequestMethod.GET, value="risk-assessments/{riskAssessmentId}/risk-range-types/risk-factors/{riskFactorId}/risk-factor-level/{riskFactorLevelId}")
	public ResponseEntity<AssessedRiskFactor> getRiskAssessmentRiskFactorById(@PathVariable("riskAssessmentId") long riskAssessmentId,
			@PathVariable("riskFactorId") long riskFactorId, @PathVariable("riskFactorLevelId") long riskFactorLevelId){
		
		AssessedRiskFactor riskAssessmentRiskFactor = 
				riskAssessmentRiskFactorService.findByRiskAssessmentAndRiskFactorAndRiskFactorLevel(riskAssessmentId, riskFactorId, riskFactorLevelId);
		
		return new ResponseEntity<>(riskAssessmentRiskFactor, HttpStatus.OK);
	}
	
}
