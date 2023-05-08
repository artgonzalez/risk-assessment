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

import pcs.scor.data.risk.repository.AssessedRiskContractRepository;
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
import pcs.scor.model.risk.asmt.RiskAssessedContract;
import pcs.scor.model.risk.tmpl.RiskFactorLevel;
import pcs.scor.model.risk.tmpl.RiskRangeTypeRange;

@Service
public class RiskAssessedContractService {
	@Autowired
	AssessedRiskContractRepository assessedRiskContractRepository;
	@Autowired
    private ModelMapper modelMapper;
	
	public PagedResources<RiskAssessedContract> getRiskAssessmentsByContractId(long contractId, Pageable page){
		Page<RiskAssessedContractEntity> riskAssessedContractEntities = assessedRiskContractRepository.findByContractId(contractId, page);
		List<RiskAssessedContract> riskAssessedContracts = new ArrayList<RiskAssessedContract>();
		
		riskAssessedContractEntities.forEach(entity -> riskAssessedContracts.add(getRiskAssessment(entity)));
		
		PagedResources<RiskAssessedContract> pagedRiskAssessments = new PagedResources<RiskAssessedContract>(riskAssessedContracts,
				new PageMetadata(riskAssessedContractEntities.getSize(), riskAssessedContractEntities.getNumber(),
						riskAssessedContractEntities.getTotalElements()));
		
		return pagedRiskAssessments;
	}
	
	public RiskAssessedContract getContractRiskAssessment(long riskAssessmentId){
		RiskAssessedContractEntity riskAssessedContractEntity = assessedRiskContractRepository.findOne(riskAssessmentId);
		RiskAssessedContract riskAssessedContract = getRiskAssessment(riskAssessedContractEntity);
		
		return riskAssessedContract;
	}
	
	@Transactional
	public RiskAssessedContract save(RiskAssessedContract riskAssessedContract) {
		
		RiskAssessedContractEntity riskAssessedContractEntity = new RiskAssessedContractEntity(0,
				riskAssessedContract.getContractId(),
				riskAssessedContract.getRiskAssessmentDate(),
				riskAssessedContract.getPrimaryRiskAssessor(),
				riskAssessedContract.getFiscalYear());
		
		riskAssessedContractEntity.setRiskAssessmentTemplate(new RiskAssessmentTemplateEntity(riskAssessedContract.getRiskAssessmentTemplateId()));
		prepareForSave(riskAssessedContract, riskAssessedContractEntity);
		
		riskAssessedContractEntity.setCreatedDate(new Date());
		RiskAssessedContractEntity newRiskAssessmentEntity = assessedRiskContractRepository.save(riskAssessedContractEntity);
		
		RiskAssessedContract newRiskAssessment = modelMapper.map(newRiskAssessmentEntity, RiskAssessedContract.class);
		
		return newRiskAssessment;
	}
	
	@Transactional
	public RiskAssessedContract update(RiskAssessedContract riskAssessedContract) {
		RiskAssessedContractEntity riskAssessedContractEntity = new RiskAssessedContractEntity(riskAssessedContract.getRiskAssessmentId(),
														riskAssessedContract.getContractId(),
														riskAssessedContract.getRiskAssessmentDate(),
														riskAssessedContract.getPrimaryRiskAssessor(),
														riskAssessedContract.getFiscalYear());
		riskAssessedContractEntity.setUpdatedDate(new Date());

		riskAssessedContractEntity.setRiskAssessmentTemplate(new RiskAssessmentTemplateEntity(riskAssessedContract.getRiskAssessmentTemplateId())); 
		prepareForSave(riskAssessedContract, riskAssessedContractEntity);
		
		RiskAssessedContractEntity updatedRiskAssessmentEntity = assessedRiskContractRepository.save(riskAssessedContractEntity);
		RiskAssessedContract newRiskAssessment = modelMapper.map(updatedRiskAssessmentEntity, RiskAssessedContract.class);
		
		return newRiskAssessment;
	}
	
	private RiskAssessedContract getRiskAssessment(RiskAssessedContractEntity riskAssessedContractEntity) {
		
		RiskAssessedContract riskAssessedContract = new RiskAssessedContract();
		riskAssessedContract.setContractId(riskAssessedContractEntity.getContractId());
		riskAssessedContract.setFiscalYear(riskAssessedContractEntity.getFiscalYear());
		riskAssessedContract.setPrimaryRiskAssessor(riskAssessedContractEntity.getPrimaryRiskAssessor());
		riskAssessedContract.setRiskAssessmentDate(riskAssessedContractEntity.getRiskAssessmentDate());
		riskAssessedContract.setRiskAssessmentId(riskAssessedContractEntity.getRiskAssessmentId());
		riskAssessedContract.setRiskAssessmentTemplateId(riskAssessedContractEntity.getRiskAssessmentTemplate().getRiskAssessmentTemplateId());
		riskAssessedContract.setRiskAssessmentTemplateVersion(riskAssessedContractEntity.getRiskAssessmentTemplate().getVersion());
		riskAssessedContract.setCreatedBy(riskAssessedContractEntity.getCreatedBy());
		riskAssessedContract.setCreatedDate(riskAssessedContractEntity.getCreatedDate());
		riskAssessedContract.setUpdatedBy(riskAssessedContractEntity.getUpdatedBy());
		riskAssessedContract.setUpdatedDate(riskAssessedContractEntity.getUpdatedDate());
		
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
			
			riskAssessedContract.getRiskRangeTypes().add(assessedRiskRange);
		}
		
		riskAssessedContract.getRiskRangeTypes().sort(Comparator.comparing(AssessedRiskRange::getRiskRangeTypeId));
		
		return riskAssessedContract;
	}
	
	private void prepareForSave(RiskAssessedContract riskAssessedContract, RiskAssessedContractEntity riskAssessedContractEntity) {
		
		Set<AssessedRiskRangeTypeEntity> assessedRiskRangeTypeEntities = new HashSet<>();
		
		for(AssessedRiskRange riskRange: riskAssessedContract.getRiskRangeTypes()) {
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
