package pcs.scor.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pcs.scor.model.risk.tmpl.RiskAssessmentTemplate;
import pcs.scor.service.RiskAssessmentTemplateService;

@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:8082"})
@RestController
@RequestMapping("/api")
public class RiskAssessmentTemplateController {
	final public String riskAsmtTemplateMapping = "risk-asmt-tplts";
	
	@Autowired
	RiskAssessmentTemplateService riskAssessmentTemplateService;
	
	@RequestMapping(method = RequestMethod.GET, value=riskAsmtTemplateMapping)
	public ResponseEntity<List<RiskAssessmentTemplate>> getAllRiskAssessmentTemplates() {
		List<RiskAssessmentTemplate> riskAsmtTemplates = riskAssessmentTemplateService.getAllRiskAssessmentTemplates();
		
		if (riskAsmtTemplates.isEmpty()){ 
		   return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<>(riskAsmtTemplates, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value=riskAsmtTemplateMapping + "/{riskAssessmentTemplateId}")
	 public ResponseEntity<RiskAssessmentTemplate> getRiskAssessmentTemplateById(@PathVariable("riskAssessmentTemplateId") long id) {
		RiskAssessmentTemplate template = riskAssessmentTemplateService.getRiskAssessmentTemplateById(id);
	        
	    return new ResponseEntity<>(template, HttpStatus.OK);
	 }

	@RequestMapping(method = RequestMethod.POST, value=riskAsmtTemplateMapping)
	public ResponseEntity<RiskAssessmentTemplate> createRiskAssessmentTemplate(@RequestBody RiskAssessmentTemplate template) {
		
		RiskAssessmentTemplate new_template = riskAssessmentTemplateService.createRiskAssessmentTemplate(template);
		
	    return new ResponseEntity<>(new_template, HttpStatus.CREATED);
	}
	
	@RequestMapping(method = RequestMethod.POST, value=riskAsmtTemplateMapping + "/{id}")
	public ResponseEntity<RiskAssessmentTemplate> updateRiskAssessmentTemplate(@PathVariable("id") long id, @RequestBody RiskAssessmentTemplate template) {
		RiskAssessmentTemplate updatedTemplate = riskAssessmentTemplateService.updateRiskAssessmentTemplate(template);
			
	    return new ResponseEntity<>(updatedTemplate, HttpStatus.OK);
	 }
}
