package pcs.scor.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pcs.scor.data.risk.repository.AssessedRiskFactorRepository;
//import pcs.scor.data.risk.repository.RiskAssessmentRiskRangeTypeRepository;
import pcs.scor.domain.risk.asmt.AssessedRiskFactorEntity;
import pcs.scor.domain.risk.asmt.RiskAssessmentEntity;
//import pcs.scor.domain.risk.asmt.RiskAssessmentRiskRangeTypeEntity;
import pcs.scor.domain.risk.tmpl.RiskFactorEntity;
import pcs.scor.domain.risk.tmpl.RiskFactorLevelEntity;
import pcs.scor.domain.risk.tmpl.RiskRangeTypeEntity;
import pcs.scor.model.risk.asmt.AssessedRiskFactor;
import pcs.scor.model.risk.asmt.AssessedRiskRange;
//import pcs.scor.model.risk.asmt.RiskAssessmentRiskRangeType;

@Service
public class AssessedRiskFactorService {
	@Autowired
	AssessedRiskFactorRepository assessedRiskFactorRepository;
	//@Autowired
	//RiskAssessmentRiskRangeTypeRepository riskAssessmentRiskRangeTypeRepository;
	@Autowired
    private ModelMapper modelMapper;

	public List<AssessedRiskRange> findByRiskAssessmentId( long riskAssessmentId){
		RiskAssessmentEntity a = new RiskAssessmentEntity();
		a.setRiskAssessmentId(riskAssessmentId);
		
		List<AssessedRiskFactorEntity> entities = assessedRiskFactorRepository.findAll();
		
		List<AssessedRiskRange> assessedRiskRanges = getRiskRanges(entities);
		
		return assessedRiskRanges;
	}
	
	private List<AssessedRiskRange> getRiskRanges(List<AssessedRiskFactorEntity> assessedRiskFactors) {
		
		long riskRangeTypeId = 0;

		List<AssessedRiskRange> assessedRiskRanges = new ArrayList<AssessedRiskRange>();

		if(assessedRiskFactors.size() > 0) {
			riskRangeTypeId = assessedRiskFactors.get(0).getRiskRangeType().getId();
		}
		
		AssessedRiskRange assessedRiskRange = new AssessedRiskRange(riskRangeTypeId, assessedRiskFactors.get(0).getRiskRangeType().getName());
		assessedRiskRanges.add(assessedRiskRange);
		
		for(AssessedRiskFactorEntity factor: assessedRiskFactors) {
			
			if(riskRangeTypeId != factor.getRiskRangeType().getId()) {
				riskRangeTypeId = factor.getRiskRangeType().getId();
				assessedRiskRange = new AssessedRiskRange(riskRangeTypeId, factor.getRiskRangeType().getName());
				assessedRiskRanges.add(assessedRiskRange);
			}
			else {
				assessedRiskRange.getRiskFactors().add(modelMapper.map(factor, AssessedRiskFactor.class));
			}
		}
		
		return assessedRiskRanges;
	}
	
	public AssessedRiskFactor findByRiskAssessmentAndRiskFactorAndRiskFactorLevel( long riskAssessmentId, 
			long riskFactorId, long riskFactorLevelId){
		RiskAssessmentEntity a = new RiskAssessmentEntity();
		a.setRiskAssessmentId(riskAssessmentId);
		RiskFactorEntity f = new RiskFactorEntity();
		f.setId(riskFactorId);
		RiskFactorLevelEntity level = new RiskFactorLevelEntity();
		level.setRiskFactorLevelId(riskFactorLevelId);
		
		//List<AssessedRiskFactorEntity> riskAssessmentRiskFactorEntity = 
			//	riskAssessmentRiskFactorRepository.findAll();				
		
		AssessedRiskFactor riskAssessmentRiskFactor = null; //modelMapper.map(riskAssessmentRiskFactorEntity.get(0), AssessedRiskFactor.class); 
		
		return riskAssessmentRiskFactor;
	}
	
	public AssessedRiskFactor save(AssessedRiskFactor assessedRiskFactor) {
		
		AssessedRiskFactorEntity entity = assessedRiskFactorRepository.save(
				new AssessedRiskFactorEntity(new RiskAssessmentEntity(assessedRiskFactor.getRiskAssessmentId()),
											 new RiskRangeTypeEntity(assessedRiskFactor.getRiskRangeTypeId()),
											 new RiskFactorEntity(assessedRiskFactor.getRiskFactorId()),
											 new RiskFactorLevelEntity(assessedRiskFactor.getRiskFactorLevelId())));
		
		return modelMapper.map(entity, AssessedRiskFactor.class);
	}
}
