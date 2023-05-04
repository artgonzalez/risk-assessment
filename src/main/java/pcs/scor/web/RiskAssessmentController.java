package pcs.scor.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
	ResponseEntity<PagedResources<RiskAssessment>> getRiskAssessmentsByContractId(@PathVariable("contractId") long contractId, Pageable page){
		PagedResources<RiskAssessment> riskAssessments = riskAssessmentService.getRiskAssessmentsByContractId(contractId, page);
		
		return new ResponseEntity<>(riskAssessments, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/contracts/risk-assessments/{riskAssessmentId}")
	ResponseEntity<RiskAssessment> getContractRiskAssessment(@PathVariable("riskAssessmentId") long riskAssessmentId){
		RiskAssessment riskAssessment = riskAssessmentService.getContractRiskAssessment(riskAssessmentId);
		
		return new ResponseEntity<>(riskAssessment, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/contracts/{contractId}/risk-assessments")
	public ResponseEntity<RiskAssessment> createRiskAssessment(@PathVariable("contractId") long contractId,
			@RequestBody RiskAssessment riskAssessment){
		
		riskAssessment.setContractId(contractId);
		RiskAssessment new_riskAssessment = riskAssessmentService.save(riskAssessment);
		
		return new ResponseEntity<>(new_riskAssessment, HttpStatus.CREATED);
	}	 
	 
	@RequestMapping(method=RequestMethod.POST, value="/contracts/risk-assessments/{riskAssessmentId}")
	public ResponseEntity<RiskAssessment> updateRiskAssessment(@PathVariable("riskAssessmentId") long riskAssessmentId,
			@RequestBody RiskAssessment riskAssessment){
		
		RiskAssessment new_riskAssessment = riskAssessmentService.update(riskAssessment);
		
		return new ResponseEntity<>(new_riskAssessment, HttpStatus.OK);
	}	 

	 
}
