package pcs.scor.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pcs.scor.data.risk.repository.AssessedRiskFactorRepository;
import pcs.scor.data.risk.repository.RiskAssessmentRiskRangeTypeRepository;
import pcs.scor.domain.risk.asmt.AssessedRiskFactorEntity;
import pcs.scor.domain.risk.asmt.RiskAssessmentEntity;
import pcs.scor.domain.risk.asmt.RiskAssessmentRiskRangeTypeEntity;
import pcs.scor.domain.risk.tmpl.RiskFactorEntity;
import pcs.scor.domain.risk.tmpl.RiskFactorLevelEntity;
import pcs.scor.model.risk.asmt.AssessedRiskFactor;
import pcs.scor.model.risk.asmt.RiskAssessmentRiskRangeType;

@Service
public class AssessedRiskFactorService {
	@Autowired
	AssessedRiskFactorRepository riskAssessmentRiskFactorRepository;
	@Autowired
	RiskAssessmentRiskRangeTypeRepository riskAssessmentRiskRangeTypeRepository;
	@Autowired
    private ModelMapper modelMapper;

	public List<RiskAssessmentRiskRangeType> findByRiskAssessmentId( long riskAssessmentId){
		RiskAssessmentEntity a = new RiskAssessmentEntity();
		a.setRiskAssessmentId(riskAssessmentId);
		
		//List<RiskAssessmentRiskFactorEntity> entities = riskAssessmentRiskFactorRepository.findByRiskAssessment(a); 				
		List<RiskAssessmentRiskRangeTypeEntity> entities = riskAssessmentRiskRangeTypeRepository.findAll();
		List<RiskAssessmentRiskRangeType> levels = new ArrayList<RiskAssessmentRiskRangeType>();
		
		entities.forEach(entity -> {
			RiskAssessmentRiskRangeType riskRangeType = modelMapper.map(entity, RiskAssessmentRiskRangeType.class);
			riskRangeType.setId(entity.getRiskRangeType().getId());
			riskRangeType.setName(entity.getRiskRangeType().getName());
			
			levels.add(riskRangeType);
			
			}); 
		
		return levels;
	}	
	
	public AssessedRiskFactor findByRiskAssessmentAndRiskFactorAndRiskFactorLevel( long riskAssessmentId, 
			long riskFactorId, long riskFactorLevelId){
		RiskAssessmentEntity a = new RiskAssessmentEntity();
		a.setRiskAssessmentId(riskAssessmentId);
		RiskFactorEntity f = new RiskFactorEntity();
		f.setId(riskFactorId);
		RiskFactorLevelEntity level = new RiskFactorLevelEntity();
		level.setRiskFactorLevelId(riskFactorLevelId);
		
		List<AssessedRiskFactorEntity> riskAssessmentRiskFactorEntity = 
				riskAssessmentRiskFactorRepository.findAll();				
		
		AssessedRiskFactor riskAssessmentRiskFactor = modelMapper.map(riskAssessmentRiskFactorEntity.get(0), AssessedRiskFactor.class); 
		
		return riskAssessmentRiskFactor;
	}
}
