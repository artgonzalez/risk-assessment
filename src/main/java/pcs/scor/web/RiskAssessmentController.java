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

import pcs.scor.model.risk.asmt.RiskAssessment;
import pcs.scor.service.RiskAssessmentService;

@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:8082"})
@RestController
@RequestMapping("/api")
public class RiskAssessmentController {
	
	@Autowired
	RiskAssessmentService riskAssessmentService;
	
	@RequestMapping(method = RequestMethod.GET, value="/contracts/{contractId}/risk-assessments")
	ResponseEntity<List<RiskAssessment>> getRiskAssessmentsByContractId(@PathVariable("contractId") long contractId){
		List<RiskAssessment> riskAssessments = riskAssessmentService.getRiskAssessmentsByContractId(contractId);
		
		return new ResponseEntity<>(riskAssessments, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/contracts/risk-assessments/{riskAssessmentId}")
	ResponseEntity<RiskAssessment> getContractRiskAssessment(@PathVariable("riskAssessmentId") long riskAssessmentId){
		RiskAssessment riskAssessment = riskAssessmentService.getContractRiskAssessment(riskAssessmentId);
		
		return new ResponseEntity<>(riskAssessment, HttpStatus.OK);
	}
}
