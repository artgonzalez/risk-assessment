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

import pcs.scor.data.risk.repository.RiskAssessmentTemplateRepository;
import pcs.scor.data.risk.repository.RiskRangeTypeRangeRepository;
import pcs.scor.model.risk.tmpl.RiskRangeType;
import pcs.scor.model.risk.tmpl.RiskRangeTypeRange;
import pcs.scor.service.RiskRangeTypeService;

@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:8082"})
@RestController
@RequestMapping("/api")
public class RiskRangeTypeController {
	final public String riskRangeTypesMapping = "risk-range-types";
	
	@Autowired
	RiskRangeTypeService riskRangeTypeService;
		
	@Autowired
	RiskRangeTypeRangeRepository riskRangeTypeRangeRepository;
	@Autowired
	RiskAssessmentTemplateRepository riskAsmtTemplateRepository;
	
	@RequestMapping(method = RequestMethod.GET, value= "risk-asmt-tplts/{riskAssessmentTemplateId}/" + riskRangeTypesMapping)
	public ResponseEntity<List<RiskRangeType>> getRiskRangeTypeByRiskAssessmentTemplateId(
			@PathVariable("riskAssessmentTemplateId") long riskAssessmentTemplateId){
		
		List<RiskRangeType> riskRangeTypes = riskRangeTypeService.getRiskRangeTypesByRiskAssessmentTemplateId(riskAssessmentTemplateId);
		
		if (riskRangeTypes.isEmpty()){ 
			   return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<>(riskRangeTypes, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value= "risk-asmt-tplts/" + riskRangeTypesMapping + "/{riskRangeTypeId}/risk-range-type-ranges")
	public ResponseEntity<List<RiskRangeTypeRange>> getRiskRangeTypeRangesByRiskRangeTypeId(
			@PathVariable("riskRangeTypeId") long riskRangeTypeId){
		
		List<RiskRangeTypeRange> riskRangeTypeRanges = riskRangeTypeService.getRiskRangeTypeRangeByRiskRangeTypeId(riskRangeTypeId);
		
		if (riskRangeTypeRanges.isEmpty()){ 
			   return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<>(riskRangeTypeRanges, HttpStatus.OK);
	}
	/*
	@PostMapping("risk-asmt-tplts/{riskAssessmentTemplateId}/" + riskRangeTypesMapping)
	public ResponseEntity<RiskRangeTypeEntity> createRiskRangeType(@PathVariable("riskAssessmentTemplateId") long id, @RequestBody RiskRangeTypeEntity riskRangeType){
		
		RiskAssessmentTemplateEntity template = riskAsmtTemplateRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Risk Assessmemt Template not found with id = " + id));
		
		RiskRangeTypeEntity new_riskRangeType = riskRangeTypeRepository.save(
				new RiskRangeTypeEntity(template,
						          riskRangeType.getName()));
		
		return new ResponseEntity<>(new_riskRangeType, HttpStatus.CREATED);
	}
	
	@PutMapping("risk-asmt-tplts" + riskRangeTypesMapping + "/{riskRangeTypeId}")
	public ResponseEntity<RiskRangeTypeEntity> updateRiskRangeType(@PathVariable("riskRangeTypeId") long id, @RequestBody RiskRangeTypeEntity riskRangeType){
		RiskRangeTypeEntity update_riskRangeType = riskRangeTypeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Not found RiskRange Type with id = " + id));
		
		return new ResponseEntity<>(riskRangeTypeRepository.save(update_riskRangeType), HttpStatus.OK);
	}
	
	@GetMapping("risk-asmt-tplts" + riskRangeTypesMapping + "/{riskRangeTypeId}/ranges")
	public ResponseEntity<RiskRangeTypeRangeEntity> getRiskRangeTypeRangeByRiskRangeTypeId(@PathVariable("riskRangeTypeId") long id){
		RiskRangeTypeRangeEntity riskRangeTypeRange = riskRangeTypeRangeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Risk Range Type Range not found with id = " + id));
		
		return new ResponseEntity<>(riskRangeTypeRange, HttpStatus.OK);
	}
	
	@PostMapping("risk-asmt-tplts" + riskRangeTypesMapping + "/risk-range-types-ranges/{riskRangeTypeRangeId}")
	public ResponseEntity<RiskRangeTypeRangeEntity> createRiskRangeTypeRange(@PathVariable("riskRangeTypeRangeId") long id, @RequestBody RiskRangeTypeRangeEntity riskRangeTypeRange){
		
		RiskRangeTypeEntity riskRangeType = riskRangeTypeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Risk Range Type Range not found with id = " + id));
		
		RiskRangeTypeRangeEntity new_riskRangeTypeRange = riskRangeTypeRangeRepository.save(
				new RiskRangeTypeRangeEntity(riskRangeType,
						               riskRangeTypeRange.getLevel(),
						               riskRangeTypeRange.getMin(),
						               riskRangeTypeRange.getMax()));
		
		return new ResponseEntity<>(new_riskRangeTypeRange, HttpStatus.CREATED);
	}
	
	@PutMapping("risk-asmt-tplts" + riskRangeTypesMapping + "/risk-range-types-ranges/{riskRangeTypeRangeId}")
	public ResponseEntity<RiskRangeTypeRangeEntity> updateRiskRangeTypeRange(@PathVariable("riskRangeTypeRangeId") long id, @RequestBody RiskRangeTypeRangeEntity riskRangeTypeRange){
		RiskRangeTypeRangeEntity update_riskRangeTypeRange = riskRangeTypeRangeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Not found Risk Range Type Range with id = " + id));
		
		update_riskRangeTypeRange.setLevel(riskRangeTypeRange.getLevel());
		update_riskRangeTypeRange.setMin(riskRangeTypeRange.getMin());
		update_riskRangeTypeRange.setMax(riskRangeTypeRange.getMax());
		
		return new ResponseEntity<>(riskRangeTypeRangeRepository.save(update_riskRangeTypeRange), HttpStatus.OK);
	}*/
}
