package pcs.scor.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pcs.scor.data.risk.repository.AssessedRiskFactorRepository;
import pcs.scor.data.risk.repository.RiskAssessmentRepository;
import pcs.scor.domain.risk.asmt.AssessedRiskFactorEntity;
import pcs.scor.domain.risk.asmt.RiskAssessmentEntity;
import pcs.scor.domain.risk.tmpl.RiskFactorEntity;
import pcs.scor.domain.risk.tmpl.RiskFactorLevelEntity;
import pcs.scor.domain.risk.tmpl.RiskRangeTypeEntity;
import pcs.scor.model.risk.asmt.AssessedRiskFactor;
import pcs.scor.model.risk.asmt.AssessedRiskRange;
import pcs.scor.model.risk.asmt.RiskAssessment;

@Service
public class RiskAssessmentService {
	@Autowired
	RiskAssessmentRepository riskAssessmentRepository;
	@Autowired
	AssessedRiskFactorRepository assessedRiskFactorRepository;
	@Autowired
    private ModelMapper modelMapper;
	
	public List<RiskAssessment> getRiskAssessmentsByContractId(long contractId){
		List<RiskAssessmentEntity> riskAssessmentEntities = riskAssessmentRepository.findByContractId(contractId);
		List<RiskAssessment> riskAssessments = new ArrayList<RiskAssessment>();
		
		riskAssessmentEntities.forEach(entity -> {
			RiskAssessment riskAssessment = modelMapper.map(entity, RiskAssessment.class);
			riskAssessment.setRiskRangeTypes(getRiskRanges(entity.getAssessedRiskFactors()));
			riskAssessments.add(riskAssessment);
		});
		
		return riskAssessments;
	}
	
	public RiskAssessment getContractRiskAssessment(long riskAssessmentId){
		RiskAssessmentEntity riskAssessmentEntity = riskAssessmentRepository.findOne(riskAssessmentId);
		
		RiskAssessment riskAssessment = modelMapper.map(riskAssessmentEntity, RiskAssessment.class);
		riskAssessment.setRiskRangeTypes(getRiskRanges(riskAssessmentEntity.getAssessedRiskFactors()));
		
		return riskAssessment;
	}
	
	@Transactional
	public RiskAssessment save(RiskAssessment riskAssessment) {
		
		RiskAssessmentEntity riskAssessmentEntity = riskAssessmentRepository.save(modelMapper.map(riskAssessment, RiskAssessmentEntity.class));
		RiskAssessment new_riskAssessment = modelMapper.map(riskAssessmentEntity, RiskAssessment.class);
		
		for(AssessedRiskRange riskRange: riskAssessment.getRiskRangeTypes()) {
			for(AssessedRiskFactor factor: riskRange.getRiskFactors()) {
				AssessedRiskFactorEntity entity = new AssessedRiskFactorEntity
													(new RiskAssessmentEntity(riskAssessment.getRiskAssessmentId()),
													 new RiskRangeTypeEntity(factor.getRiskRangeTypeId()),
													 new RiskFactorEntity(factor.getRiskFactorId()),
													 new RiskFactorLevelEntity(factor.getRiskFactorLevelId()));
													 
													 
				entity.setRiskAssessment(new RiskAssessmentEntity(riskAssessmentEntity.getRiskAssessmentId()));
				assessedRiskFactorRepository.save(entity);
			}
		}
		
		return new_riskAssessment;
	}
	
	private List<AssessedRiskRange> getRiskRanges(List<AssessedRiskFactorEntity> assessedRiskFactors) {
		
		long riskRangeTypeId = 0;

		List<AssessedRiskRange> assessedRiskRanges = new ArrayList<AssessedRiskRange>();

		if(assessedRiskFactors.size() > 0) {
			riskRangeTypeId = assessedRiskFactors.get(0).getRiskRangeType().getId();
		}
		else {
			return assessedRiskRanges;
		}
		
		AssessedRiskRange assessedRiskRange = new AssessedRiskRange(riskRangeTypeId, assessedRiskFactors.get(0).getRiskRangeType().getName());
		assessedRiskRanges.add(assessedRiskRange);
		
		for(AssessedRiskFactorEntity factor: assessedRiskFactors) {
			
			if(riskRangeTypeId != factor.getRiskRangeType().getId()) {
				riskRangeTypeId = factor.getRiskRangeType().getId();
				assessedRiskRange = new AssessedRiskRange(riskRangeTypeId, factor.getRiskRangeType().getName());
				//assessedRiskRange.getRiskFactors().add(modelMapper.map(factor, AssessedRiskFactor.class));
				assessedRiskRanges.add(assessedRiskRange);
			}
			//else {
			AssessedRiskFactor assessedRiskFactor = modelMapper.map(factor, AssessedRiskFactor.class);
			assessedRiskFactor.setRiskFactorLevelId(factor.getRiskFactorLevel().getRiskFactorLevelId());
			
			assessedRiskRange.getRiskFactors().add(assessedRiskFactor);
			//}
		}
		
		return assessedRiskRanges;
	}

}
