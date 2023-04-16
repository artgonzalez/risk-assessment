package pcs.scor.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pcs.scor.data.risk.repository.RiskAssessmentTemplateRepository;
import pcs.scor.data.risk.repository.RiskFactorLevelRepository;
import pcs.scor.data.risk.repository.RiskFactorRepository;
import pcs.scor.data.risk.repository.RiskRangeTypeRangeRepository;
import pcs.scor.data.risk.repository.RiskRangeTypeRepository;
import pcs.scor.domain.risk.tmpl.RiskAssessmentTemplateEntity;
import pcs.scor.model.risk.tmpl.RiskAssessmentTemplate;

@Service
public class RiskAsmtTemplateService {
	@Autowired
	RiskAssessmentTemplateRepository riskAsmtTemplateRepository;
	@Autowired
	RiskRangeTypeRepository riskRangeTypeRepository;
	@Autowired
	RiskRangeTypeRangeRepository riskRangeTypeRangeRepository;
	@Autowired
	RiskFactorRepository riskFactorRepository;
	@Autowired
	RiskFactorLevelRepository riskFactorLevelRepository;
	@Autowired
    private ModelMapper modelMapper;
	
	public List<RiskAssessmentTemplate> getAllRiskAssessmentTemplates(){
		List<RiskAssessmentTemplateEntity> riskAsmtTemplateEntities = riskAsmtTemplateRepository.findAll();
		
		List<RiskAssessmentTemplate> riskAsmtTemplates = new ArrayList<RiskAssessmentTemplate>();
		
		riskAsmtTemplateEntities.forEach(entity -> riskAsmtTemplates.add(modelMapper.map(entity, RiskAssessmentTemplate.class)));
			
		return riskAsmtTemplates;
	}
	
	public RiskAssessmentTemplate getRiskAssessmentTemplateById(long riskAssessmentTemplateId) {
		RiskAssessmentTemplateEntity templateEntity = riskAsmtTemplateRepository.findOne(riskAssessmentTemplateId);
		
		return modelMapper.map(templateEntity, RiskAssessmentTemplate.class);
	}
	
	public RiskAssessmentTemplate createRiskAssessmentTemplate(RiskAssessmentTemplate riskAssessmentTemplate) {
		RiskAssessmentTemplateEntity template = modelMapper.map(riskAssessmentTemplate, RiskAssessmentTemplateEntity.class);
		RiskAssessmentTemplateEntity new_template = riskAsmtTemplateRepository.save(template);
		
		return modelMapper.map(new_template, RiskAssessmentTemplate.class);
	}
	
	public RiskAssessmentTemplate updateRiskAssessmentTemplate(RiskAssessmentTemplate riskAssessmentTemplate) {
		RiskAssessmentTemplateEntity updateTemplateEntity = modelMapper.map(riskAssessmentTemplate, RiskAssessmentTemplateEntity.class);
	    
	    RiskAssessmentTemplateEntity updatedTemplateEntity = riskAsmtTemplateRepository.save(updateTemplateEntity);
	    		
		return modelMapper.map(updatedTemplateEntity, RiskAssessmentTemplate.class);
	}
}
