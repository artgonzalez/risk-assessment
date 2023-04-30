package pcs.scor.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import pcs.scor.data.risk.repository.AssessedRiskFactorRepository;
import pcs.scor.data.risk.repository.RiskAssessmentRepository;
import pcs.scor.domain.risk.asmt.AssessedRiskFactorEntity;
import pcs.scor.domain.risk.asmt.AssessedRiskRangeTypeEntity;
import pcs.scor.domain.risk.asmt.RiskAssessmentEntity;
import pcs.scor.domain.risk.tmpl.RiskAssessmentTemplateEntity;
import pcs.scor.domain.risk.tmpl.RiskFactorEntity;
import pcs.scor.domain.risk.tmpl.RiskFactorLevelEntity;
import pcs.scor.domain.risk.tmpl.RiskRangeTypeEntity;
import pcs.scor.domain.risk.tmpl.RiskRangeTypeRangeEntity;
import pcs.scor.model.risk.asmt.AssessedRiskFactor;
import pcs.scor.model.risk.asmt.AssessedRiskRange;
import pcs.scor.model.risk.asmt.RiskAssessment;
import pcs.scor.model.risk.tmpl.RiskFactorLevel;
import pcs.scor.model.risk.tmpl.RiskRangeTypeRange;

@Service
public class RiskAssessmentService {
	@Autowired
	RiskAssessmentRepository riskAssessmentRepository;
	@Autowired
    private ModelMapper modelMapper;
	
	public List<RiskAssessment> getRiskAssessmentsByContractId(long contractId){
		List<RiskAssessmentEntity> riskAssessmentEntities = riskAssessmentRepository.findByContractId(contractId);
		List<RiskAssessment> riskAssessments = new ArrayList<RiskAssessment>();
		
		riskAssessmentEntities.forEach(entity -> riskAssessments.add(modelMapper.map(entity, RiskAssessment.class)));
		
		return riskAssessments;
	}
	
	public RiskAssessment getContractRiskAssessment(long riskAssessmentId){
		RiskAssessmentEntity riskAssessmentEntity = riskAssessmentRepository.findOne(riskAssessmentId);
		RiskAssessment riskAssessment = getRiskAssessment(riskAssessmentEntity);
		
		return riskAssessment;
	}
	
	@Transactional
	public RiskAssessment save(RiskAssessment riskAssessment) {
		
		RiskAssessmentEntity riskAssessmentEntity = new RiskAssessmentEntity(0,
				riskAssessment.getContractId(),
				riskAssessment.getRiskAssessmentDate(),
				riskAssessment.getPrimaryRiskAssessor(),
				riskAssessment.getFiscalYear());
		
		riskAssessmentEntity.setRiskAssessmentTemplate(new RiskAssessmentTemplateEntity(riskAssessment.getRiskAssessmentTemplateId()));
		prepareForSave(riskAssessment, riskAssessmentEntity);
		
		RiskAssessmentEntity newRiskAssessmentEntity = riskAssessmentRepository.save(riskAssessmentEntity);
		
		RiskAssessment newRiskAssessment = modelMapper.map(newRiskAssessmentEntity, RiskAssessment.class);
		
		return newRiskAssessment;
	}
	
	@Transactional
	public RiskAssessment update(RiskAssessment riskAssessment) {
		RiskAssessmentEntity riskAssessmentEntity = new RiskAssessmentEntity(riskAssessment.getRiskAssessmentId(),
														riskAssessment.getContractId(),
														riskAssessment.getRiskAssessmentDate(),
														riskAssessment.getPrimaryRiskAssessor(),
														riskAssessment.getFiscalYear());
		
		riskAssessmentEntity.setRiskAssessmentTemplate(new RiskAssessmentTemplateEntity(riskAssessment.getRiskAssessmentTemplateId())); 
		prepareForSave(riskAssessment, riskAssessmentEntity);
		
		RiskAssessmentEntity updatedRiskAssessmentEntity = riskAssessmentRepository.save(riskAssessmentEntity);
		RiskAssessment newRiskAssessment = modelMapper.map(updatedRiskAssessmentEntity, RiskAssessment.class);
		
		return newRiskAssessment;
	}
	
	private RiskAssessment getRiskAssessment(RiskAssessmentEntity riskAssessmentEntity) {
		
		RiskAssessment riskAssessment = new RiskAssessment();
		riskAssessment.setContractId(riskAssessmentEntity.getContractId());
		riskAssessment.setFiscalYear(riskAssessmentEntity.getFiscalYear());
		riskAssessment.setPrimaryRiskAssessor(riskAssessmentEntity.getPrimaryRiskAssessor());
		riskAssessment.setRiskAssessmentDate(riskAssessmentEntity.getRiskAssessmentDate());
		riskAssessment.setRiskAssessmentId(riskAssessmentEntity.getRiskAssessmentId());
		riskAssessment.setRiskAssessmentTemplateId(riskAssessmentEntity.getRiskAssessmentTemplate().getRiskAssessmentTemplateId());
		riskAssessment.setRiskAssessmentTemplateVersion(riskAssessmentEntity.getRiskAssessmentTemplate().getVersion());
		
		for(AssessedRiskRangeTypeEntity riskRange: riskAssessmentEntity.getRiskRangeTypes()) {
			AssessedRiskRange assessedRiskRange = new AssessedRiskRange(riskRange.getAssessedRiskRangeTypeId(),
																		riskRange.getRiskRangeTypeEntity().getName());
			assessedRiskRange.setRiskRangeTypeId(riskRange.getRiskRangeTypeEntity().getRiskRangeTypeId());
			
			for(AssessedRiskFactorEntity factor: riskRange.getAssessedRiskFactors()) {
				AssessedRiskFactor assessedFactor = modelMapper.map(factor, AssessedRiskFactor.class);
				assessedFactor.setRiskFactorLevelId(factor.getRiskFactorLevel().getRiskFactorLevelId());
				assessedFactor.setName(factor.getRiskFactor().getFactorDesc());
				assessedFactor.setScore(factor.getRiskFactorLevel().getScore());
				assessedFactor.setWeightMultiplier(factor.getWeightMultiplier());
				assessedFactor.getRiskFactorLevels().sort(Comparator.comparing(RiskFactorLevel::getRiskFactorLevelId));
				assessedRiskRange.getRiskFactors().add(assessedFactor);
			}
			
			assessedRiskRange.getRiskFactors().sort(Comparator.comparing(AssessedRiskFactor::getRiskFactorId));
			
			for(RiskRangeTypeRangeEntity rangeEntity: riskRange.getRiskRangeTypeEntity().getRiskRangeTypeRanges()){
				assessedRiskRange.getRiskRangeTypeRanges().add(modelMapper.map(rangeEntity, RiskRangeTypeRange.class));
			}
			
			assessedRiskRange.getRiskRangeTypeRanges().sort(Comparator.comparing(RiskRangeTypeRange::getRiskRangeTypeRangeId));
			
			riskAssessment.getRiskRangeTypes().add(assessedRiskRange);
		}
		
		riskAssessment.getRiskRangeTypes().sort(Comparator.comparing(AssessedRiskRange::getRiskRangeTypeId));
		
		return riskAssessment;
	}
	
	private void prepareForSave(RiskAssessment riskAssessment, RiskAssessmentEntity riskAssessmentEntity) {
		
		Set<AssessedRiskRangeTypeEntity> assessedRiskRangeTypeEntities = new HashSet<>();
		
		for(AssessedRiskRange riskRange: riskAssessment.getRiskRangeTypes()) {
			AssessedRiskRangeTypeEntity riskRangeTypeEntity = new AssessedRiskRangeTypeEntity(riskAssessmentEntity,
												new RiskRangeTypeEntity(riskRange.getRiskRangeTypeId()));
			if(riskRange.getAssessedRiskRangeTypeId() != 0) {
				riskRangeTypeEntity.setAssessedRiskRangeTypeId(riskRange.getAssessedRiskRangeTypeId());
			}
			
			for(AssessedRiskFactor factor: riskRange.getRiskFactors()) {
				AssessedRiskFactorEntity riskFactorEntity = new AssessedRiskFactorEntity
													(riskRangeTypeEntity,
													 new RiskFactorEntity(factor.getRiskFactorId()),
													 new RiskFactorLevelEntity(factor.getRiskFactorLevelId()),
													 factor.getScore(),
													 factor.getWeightMultiplier());
													 
				if(factor.getAssessedRiskFactorId() != 0) {
					riskFactorEntity.setAssessedRiskFactorId(factor.getAssessedRiskFactorId());
				}
				
				riskRangeTypeEntity.getAssessedRiskFactors().add(riskFactorEntity);
			}
			assessedRiskRangeTypeEntities.add(riskRangeTypeEntity);
		}
		
		riskAssessmentEntity.setRiskRangeTypes(assessedRiskRangeTypeEntities);
	}
}
