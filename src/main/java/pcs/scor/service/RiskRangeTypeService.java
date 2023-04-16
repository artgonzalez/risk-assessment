package pcs.scor.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pcs.scor.data.risk.repository.RiskRangeTypeRangeRepository;
import pcs.scor.data.risk.repository.RiskRangeTypeRepository;
import pcs.scor.domain.risk.tmpl.RiskRangeTypeEntity;
import pcs.scor.domain.risk.tmpl.RiskRangeTypeRangeEntity;
import pcs.scor.model.risk.tmpl.RiskRangeType;
import pcs.scor.model.risk.tmpl.RiskRangeTypeRange;

@Service
public class RiskRangeTypeService {

	@Autowired
	RiskRangeTypeRepository riskRangeTypeRepository;
	
	@Autowired
	RiskRangeTypeRangeRepository riskRangeTypeRangeRepository;
	
	@Autowired
    private ModelMapper modelMapper;
	
	public List<RiskRangeType> getRiskRangeTypesByRiskAssessmentTemplateId(long riskAssessmentTemplateId){
		List<RiskRangeTypeEntity> riskRangeTypeEntities = riskRangeTypeRepository.findByRiskAssessmentTemplateId(riskAssessmentTemplateId);
		List<RiskRangeType> riskRangeTypes = new ArrayList<RiskRangeType>();
		
		riskRangeTypeEntities.forEach(entity -> riskRangeTypes.add(modelMapper.map(entity, RiskRangeType.class)));
		
		return riskRangeTypes;
	}
	
	public List<RiskRangeTypeRange> getRiskRangeTypeRangeByRiskRangeTypeId(long riskRangeTypeId){
		List<RiskRangeTypeRangeEntity> riskRangeTypeRangeEntities = riskRangeTypeRangeRepository.findByRiskRangeTypeId(riskRangeTypeId);
		List<RiskRangeTypeRange> riskRangeTypeRanges = new ArrayList<RiskRangeTypeRange>();
		
		riskRangeTypeRangeEntities.forEach(entity -> riskRangeTypeRanges.add(modelMapper.map(entity, RiskRangeTypeRange.class)));
		
		return riskRangeTypeRanges;
	}
}
