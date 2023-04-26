package pcs.scor.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pcs.scor.data.risk.repository.RiskAssessmentTemplateRepository;
import pcs.scor.data.risk.repository.RiskFactorRepository;
import pcs.scor.domain.risk.tmpl.RiskAssessmentTemplateEntity;
import pcs.scor.model.risk.tmpl.RiskAssessmentTemplate;
import pcs.scor.model.risk.tmpl.RiskFactor;
import pcs.scor.model.risk.tmpl.RiskFactorLevel;
import pcs.scor.model.risk.tmpl.RiskRangeType;
import pcs.scor.model.risk.tmpl.RiskRangeTypeRange;

@Service
public class RiskAssessmentTemplateService {
	@Autowired
	RiskAssessmentTemplateRepository riskAsmtTemplateRepository;
	@Autowired
	RiskFactorRepository riskFactorRepository;
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
		
		RiskAssessmentTemplate template = modelMapper.map(templateEntity, RiskAssessmentTemplate.class);
		
		template.getRiskRangeTypes().sort(Comparator.comparing(RiskRangeType::getId));
		template.getRiskRangeTypes().forEach(range -> {
						range.getRiskFactors().sort(Comparator.comparing(RiskFactor::getId));
						range.getRiskRangeTypeRanges().sort(Comparator.comparing(RiskRangeTypeRange::getMin));
						range.getRiskFactors().forEach(factor -> 
						factor.getRiskFactorLevels().sort(Comparator.comparing(RiskFactorLevel::getScore)));
					});
		
		
		
		return template;
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
