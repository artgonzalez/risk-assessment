package pcs.scor.web;

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

import pcs.scor.model.risk.asmt.RiskAssessedContract;
import pcs.scor.service.RiskAssessedContractService;

@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:8082"})
@RestController
@RequestMapping("/api")
public class RiskAssessedContractController {
	
	@Autowired
	RiskAssessedContractService riskAssessedContractService;
	
	@RequestMapping(method = RequestMethod.GET, value="/contracts/{contractId}/risk-assessments")
	ResponseEntity<PagedResources<RiskAssessedContract>> getRiskAssessmentsByContractId(@PathVariable("contractId") long contractId, Pageable page){
		PagedResources<RiskAssessedContract> riskAssessedContracts = riskAssessedContractService.getRiskAssessmentsByContractId(contractId, page);
		
		return new ResponseEntity<>(riskAssessedContracts, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/contracts/risk-assessments/{riskAssessmentId}")
	ResponseEntity<RiskAssessedContract> getContractRiskAssessment(@PathVariable("riskAssessmentId") long riskAssessmentId){
		RiskAssessedContract riskAssessedContract = riskAssessedContractService.getContractRiskAssessment(riskAssessmentId);
		
		return new ResponseEntity<>(riskAssessedContract, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/contracts/{contractId}/risk-assessments")
	public ResponseEntity<RiskAssessedContract> createRiskAssessment(@PathVariable("contractId") long contractId,
			@RequestBody RiskAssessedContract riskAssessedContract){
		
		riskAssessedContract.setContractId(contractId);
		RiskAssessedContract new_riskAssessment = riskAssessedContractService.save(riskAssessedContract);
		
		return new ResponseEntity<>(new_riskAssessment, HttpStatus.CREATED);
	}	 
	 
	@RequestMapping(method=RequestMethod.POST, value="/contracts/risk-assessments/{riskAssessmentId}")
	public ResponseEntity<RiskAssessedContract> updateRiskAssessment(@PathVariable("riskAssessmentId") long riskAssessmentId,
			@RequestBody RiskAssessedContract riskAssessedContract){
		
		RiskAssessedContract new_riskAssessment = riskAssessedContractService.update(riskAssessedContract);
		
		return new ResponseEntity<>(new_riskAssessment, HttpStatus.OK);
	}	 

	 
}
