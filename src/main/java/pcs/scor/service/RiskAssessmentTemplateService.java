package pcs.scor.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pcs.scor.data.risk.repository.RiskAssessmentTemplateRepository;
//import pcs.scor.data.risk.repository.RiskFactorRepository;
import pcs.scor.domain.risk.tmpl.RiskAssessmentTemplateEntity;
import pcs.scor.domain.risk.tmpl.RiskFactorEntity;
import pcs.scor.domain.risk.tmpl.RiskRangeTypeEntity;
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
		
		template.getRiskRangeTypes().sort(Comparator.comparing(RiskRangeType::getRiskRangeTypeId));
		template.getRiskRangeTypes().forEach(range -> {
						range.getRiskFactors().sort(Comparator.comparing(RiskFactor::getRiskFactorId));
						range.getRiskRangeTypeRanges().sort(Comparator.comparing(RiskRangeTypeRange::getMin));
						range.getRiskFactors().forEach(factor -> 
						factor.getRiskFactorLevels().sort(Comparator.comparing(RiskFactorLevel::getScore)));
					});
		
		
		
		return template;
	}
	
	@Transactional
	public RiskAssessmentTemplate createRiskAssessmentTemplate(RiskAssessmentTemplate riskAssessmentTemplate) {
		RiskAssessmentTemplateEntity template = modelMapper.map(riskAssessmentTemplate, RiskAssessmentTemplateEntity.class);
		prepareTemplateEntity(template);
		template.setCreatedDate(new Date());
		
		RiskAssessmentTemplateEntity new_template = riskAsmtTemplateRepository.save(template);
		
		return modelMapper.map(new_template, RiskAssessmentTemplate.class);
	}
	
	@Transactional
	public RiskAssessmentTemplate updateRiskAssessmentTemplate(RiskAssessmentTemplate riskAssessmentTemplate) {
		RiskAssessmentTemplateEntity updateTemplateEntity = modelMapper.map(riskAssessmentTemplate, RiskAssessmentTemplateEntity.class);
		prepareTemplateEntity(updateTemplateEntity);
	    updateTemplateEntity.setUpdatedDate(new Date());
		
	    RiskAssessmentTemplateEntity savedTemplateEntity = riskAsmtTemplateRepository.save(updateTemplateEntity);
	    		
		return modelMapper.map(savedTemplateEntity, RiskAssessmentTemplate.class);
	}
	
	private void prepareTemplateEntity(RiskAssessmentTemplateEntity templateEntity) {
		for(RiskRangeTypeEntity riskRangeType: templateEntity.getRiskRangeTypes()) {
			riskRangeType.setRiskAssessmentTemplate(templateEntity);
			
			for(RiskFactorEntity riskFactorEntity: riskRangeType.getRiskFactors()) {
				riskFactorEntity.setRiskRangeType(riskRangeType);
				riskFactorEntity.getRiskFactorLevels().forEach(level -> level.setRiskFactor(riskFactorEntity));
			}
			
			riskRangeType.getRiskRangeTypeRanges().forEach(rangeType -> rangeType.setRiskRangeType(riskRangeType));
		}
	}
}
