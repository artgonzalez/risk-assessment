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

import pcs.scor.data.risk.repository.RiskFactorLevelRepository;
import pcs.scor.data.risk.repository.RiskRangeTypeRepository;
import pcs.scor.model.risk.tmpl.RiskFactor;
import pcs.scor.model.risk.tmpl.RiskFactorLevel;
import pcs.scor.service.RiskFactorService;

@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:8082"})
@RestController
@RequestMapping("/api")
public class RiskFactorController {
	final public String riskFactorMapping = "risk-factors";
	
	@Autowired
	RiskRangeTypeRepository riskRangeTypeRepository;
	@Autowired
	RiskFactorService riskFactorService;
	@Autowired
	RiskFactorLevelRepository riskFactorLevelRepository;
	
	@RequestMapping(method = RequestMethod.GET, value= "risk-asmt-tplts/risk-range-types/{riskRangeTypeId}/" + riskFactorMapping)
	public ResponseEntity<List<RiskFactor>> getFactorsByRiskRangeTypeId(@PathVariable("riskRangeTypeId") long id){
		List<RiskFactor> riskFactors = riskFactorService.findByRiskRangeTypeId(id);
		
		return new ResponseEntity<>(riskFactors, HttpStatus.OK);
	}	
	
	/*@GetMapping("risk-asmt-tplts/risk-range-types/" + riskFactorMapping + "/{riskFactorId}")
	public ResponseEntity<RiskFactorEntity> getFactorById(@PathVariable("riskFactorId") long id){
		RiskFactorEntity riskFactor = riskFactorRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Risk Factor not found with id = " + id));
		
		return new ResponseEntity<>(riskFactor, HttpStatus.OK);
	}
	
	@PostMapping("risk-asmt-tplts/risk-range-types/" + riskFactorMapping +"/{id}")
	public ResponseEntity<RiskFactorEntity> createRiskFactor(@PathVariable("id") long id, @RequestBody RiskFactorEntity riskFactor){
		RiskRangeTypeEntity riskRangeType = riskRangeTypeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Risk Range Type not found with id = " + id));
		
		RiskFactorEntity new_riskFactor = riskFactorRepository.save(new RiskFactorEntity(riskRangeType, 
				                                              riskFactor.getName(),
				                                              riskFactor.getWeightMultiplier()));
		
		return new ResponseEntity<>(new_riskFactor, HttpStatus.CREATED);
	}
	
	@PutMapping("risk-asmt-tplts/risk-range-types/" + riskFactorMapping +"/{id}")
	public ResponseEntity<RiskFactorEntity> updateRiskRangeType(@PathVariable("id") long id, @RequestBody RiskFactorEntity riskFactor){
		RiskFactorEntity update_riskFactor = riskFactorRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Not found Risk Factor with id = " + id));
		
		return new ResponseEntity<>(riskFactorRepository.save(update_riskFactor), HttpStatus.OK);
	}
	
	@PostMapping("risk-asmt-tplts/risk-range-types/" + riskFactorMapping +"/{riskFactorId}/factor-level")
	public ResponseEntity<RiskFactorLevelEntity> createRiskFactorLevel(@PathVariable("riskFactorId") long id, @RequestBody RiskFactorLevelEntity riskFactorLevel){
		
		RiskFactorEntity riskFactor = riskFactorRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Risk Factor not found with id = " + id));
		
		RiskFactorLevelEntity new_riskFactorLevel = riskFactorLevelRepository.save(
				new RiskFactorLevelEntity(riskFactor,
						       riskFactorLevel.getLevel(),
						       riskFactorLevel.getScore()));
		
		return new ResponseEntity<>(new_riskFactorLevel, HttpStatus.CREATED);
	}
	*/
	@RequestMapping(method = RequestMethod.GET, value= "risk-asmt-tplts/risk-range-types/" + riskFactorMapping + "/{riskFactorId}/risk-factor-levels")
	public ResponseEntity<List<RiskFactorLevel>> getRiskFactorLevelByFactorId(@PathVariable("riskFactorId") long riskFactorId){
		List<RiskFactorLevel> riskFactorLevels = riskFactorService.findByRiskFactorId(riskFactorId);
		
		return new ResponseEntity<>(riskFactorLevels, HttpStatus.OK);
	}
}