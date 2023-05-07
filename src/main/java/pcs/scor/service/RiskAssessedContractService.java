package pcs.scor.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.stereotype.Service;

//import pcs.scor.data.risk.repository.AssessedRiskFactorRepository;
import pcs.scor.data.risk.repository.RiskAssessmentRepository;
import pcs.scor.domain.risk.asmt.AssessedRiskFactorEntity;
import pcs.scor.domain.risk.asmt.AssessedRiskRangeTypeEntity;
import pcs.scor.domain.risk.asmt.RiskAssessedContractEntity;
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
public class RiskAssessedContractService {
	@Autowired
	RiskAssessmentRepository riskAssessmentRepository;
	@Autowired
    private ModelMapper modelMapper;
	
	public PagedResources<RiskAssessment> getRiskAssessmentsByContractId(long contractId, Pageable page){
		Page<RiskAssessedContractEntity> riskAssessedContractEntities = riskAssessmentRepository.findByContractId(contractId, page);
		List<RiskAssessment> riskAssessments = new ArrayList<RiskAssessment>();
		
		riskAssessedContractEntities.forEach(entity -> riskAssessments.add(getRiskAssessment(entity)));
		
		PagedResources<RiskAssessment> pagedRiskAssessments = new PagedResources<RiskAssessment>(riskAssessments,
				new PageMetadata(riskAssessedContractEntities.getSize(), riskAssessedContractEntities.getNumber(),
						riskAssessedContractEntities.getTotalElements()));
		
		return pagedRiskAssessments;
	}
	
	public RiskAssessment getContractRiskAssessment(long riskAssessmentId){
		RiskAssessedContractEntity riskAssessedContractEntity = riskAssessmentRepository.findOne(riskAssessmentId);
		RiskAssessment riskAssessment = getRiskAssessment(riskAssessedContractEntity);
		
		return riskAssessment;
	}
	
	@Transactional
	public RiskAssessment save(RiskAssessment riskAssessment) {
		
		RiskAssessedContractEntity riskAssessedContractEntity = new RiskAssessedContractEntity(0,
				riskAssessment.getContractId(),
				riskAssessment.getRiskAssessmentDate(),
				riskAssessment.getPrimaryRiskAssessor(),
				riskAssessment.getFiscalYear());
		
		riskAssessedContractEntity.setRiskAssessmentTemplate(new RiskAssessmentTemplateEntity(riskAssessment.getRiskAssessmentTemplateId()));
		prepareForSave(riskAssessment, riskAssessedContractEntity);
		
		riskAssessedContractEntity.setCreatedDate(new Date());
		RiskAssessedContractEntity newRiskAssessmentEntity = riskAssessmentRepository.save(riskAssessedContractEntity);
		
		RiskAssessment newRiskAssessment = modelMapper.map(newRiskAssessmentEntity, RiskAssessment.class);
		
		return newRiskAssessment;
	}
	
	@Transactional
	public RiskAssessment update(RiskAssessment riskAssessment) {
		RiskAssessedContractEntity riskAssessedContractEntity = new RiskAssessedContractEntity(riskAssessment.getRiskAssessmentId(),
														riskAssessment.getContractId(),
														riskAssessment.getRiskAssessmentDate(),
														riskAssessment.getPrimaryRiskAssessor(),
														riskAssessment.getFiscalYear());
		riskAssessedContractEntity.setUpdatedDate(new Date());

		riskAssessedContractEntity.setRiskAssessmentTemplate(new RiskAssessmentTemplateEntity(riskAssessment.getRiskAssessmentTemplateId())); 
		prepareForSave(riskAssessment, riskAssessedContractEntity);
		
		RiskAssessedContractEntity updatedRiskAssessmentEntity = riskAssessmentRepository.save(riskAssessedContractEntity);
		RiskAssessment newRiskAssessment = modelMapper.map(updatedRiskAssessmentEntity, RiskAssessment.class);
		
		return newRiskAssessment;
	}
	
	private RiskAssessment getRiskAssessment(RiskAssessedContractEntity riskAssessedContractEntity) {
		
		RiskAssessment riskAssessment = new RiskAssessment();
		riskAssessment.setContractId(riskAssessedContractEntity.getContractId());
		riskAssessment.setFiscalYear(riskAssessedContractEntity.getFiscalYear());
		riskAssessment.setPrimaryRiskAssessor(riskAssessedContractEntity.getPrimaryRiskAssessor());
		riskAssessment.setRiskAssessmentDate(riskAssessedContractEntity.getRiskAssessmentDate());
		riskAssessment.setRiskAssessmentId(riskAssessedContractEntity.getRiskAssessmentId());
		riskAssessment.setRiskAssessmentTemplateId(riskAssessedContractEntity.getRiskAssessmentTemplate().getRiskAssessmentTemplateId());
		riskAssessment.setRiskAssessmentTemplateVersion(riskAssessedContractEntity.getRiskAssessmentTemplate().getVersion());
		riskAssessment.setCreatedBy(riskAssessedContractEntity.getCreatedBy());
		riskAssessment.setCreatedDate(riskAssessedContractEntity.getCreatedDate());
		riskAssessment.setUpdatedBy(riskAssessedContractEntity.getUpdatedBy());
		riskAssessment.setUpdatedDate(riskAssessedContractEntity.getUpdatedDate());
		
		for(AssessedRiskRangeTypeEntity riskRange: riskAssessedContractEntity.getRiskRangeTypes()) {
			AssessedRiskRange assessedRiskRange = new AssessedRiskRange(riskRange.getAssessedRiskRangeTypeId(),
																		riskRange.getRiskRangeTypeEntity().getName());
			assessedRiskRange.setRiskRangeTypeId(riskRange.getRiskRangeTypeEntity().getRiskRangeTypeId());
			
			for(AssessedRiskFactorEntity factor: riskRange.getAssessedRiskFactors()) {
				AssessedRiskFactor assessedFactor = modelMapper.map(factor, AssessedRiskFactor.class);
				assessedFactor.setRiskFactorLevelId(factor.getRiskFactorLevel().getRiskFactorLevelId());
				assessedFactor.setFactorDesc(factor.getRiskFactor().getFactorDesc());
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
	
	private void prepareForSave(RiskAssessment riskAssessment, RiskAssessedContractEntity riskAssessedContractEntity) {
		
		Set<AssessedRiskRangeTypeEntity> assessedRiskRangeTypeEntities = new HashSet<>();
		
		for(AssessedRiskRange riskRange: riskAssessment.getRiskRangeTypes()) {
			AssessedRiskRangeTypeEntity riskRangeTypeEntity = new AssessedRiskRangeTypeEntity(riskAssessedContractEntity,
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
		
		riskAssessedContractEntity.setRiskRangeTypes(assessedRiskRangeTypeEntities);
	}
}
