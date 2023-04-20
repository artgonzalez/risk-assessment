package pcs.scor.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

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
import pcs.scor.domain.risk.tmpl.RiskRangeTypeRangeEntity;
import pcs.scor.model.risk.asmt.AssessedRiskFactor;
import pcs.scor.model.risk.asmt.AssessedRiskRange;
import pcs.scor.model.risk.asmt.RiskAssessment;
import pcs.scor.model.risk.tmpl.RiskRangeTypeRange;

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
			riskAssessment.setRiskRangeTypes(getRiskRanges(entity));
			riskAssessment.setRiskAssessmentTemplateVersion(entity.getRiskAssessmentTemplate().getVersion());
			riskAssessments.add(riskAssessment);
		});
		
		return riskAssessments;
	}
	
	public RiskAssessment getContractRiskAssessment(long riskAssessmentId){
		RiskAssessmentEntity riskAssessmentEntity = riskAssessmentRepository.findOne(riskAssessmentId);
		
		RiskAssessment riskAssessment = modelMapper.map(riskAssessmentEntity, RiskAssessment.class);
		riskAssessment.setRiskRangeTypes(getRiskRanges(riskAssessmentEntity));
		
		return riskAssessment;
	}
	
	@Transactional
	public RiskAssessment save(RiskAssessment riskAssessment) {
		
		RiskAssessmentEntity riskAssessmentEntity = riskAssessmentRepository.save(modelMapper.map(riskAssessment, RiskAssessmentEntity.class));
		RiskAssessment new_riskAssessment = modelMapper.map(riskAssessmentEntity, RiskAssessment.class);
		
		riskAssessment.setRiskAssessmentId(riskAssessmentEntity.getRiskAssessmentId());
		saveAssessedRiskFactors(riskAssessment);
		
		return new_riskAssessment;
	}
	
	@Transactional
	public RiskAssessment update(RiskAssessment riskAssessment) {
		RiskAssessmentEntity riskAssessmentEntity = riskAssessmentRepository.save(modelMapper.map(riskAssessment, RiskAssessmentEntity.class));
		RiskAssessment new_riskAssessment = modelMapper.map(riskAssessmentEntity, RiskAssessment.class);
		
		saveAssessedRiskFactors(riskAssessment);
		
		return new_riskAssessment;
	}
	
	private void saveAssessedRiskFactors(RiskAssessment riskAssessment) {
		for(AssessedRiskRange riskRange: riskAssessment.getRiskRangeTypes()) {
			for(AssessedRiskFactor factor: riskRange.getRiskFactors()) {
				AssessedRiskFactorEntity entity = new AssessedRiskFactorEntity
													(new RiskAssessmentEntity(riskAssessment.getRiskAssessmentId()),
													 new RiskRangeTypeEntity(factor.getRiskRangeTypeId()),
													 new RiskFactorEntity(factor.getRiskFactorId()),
													 new RiskFactorLevelEntity(factor.getRiskFactorLevelId()));
													 
				if(factor.getAssessedRiskFactorId() != 0) {
					entity.setAssessedRiskFactorId(factor.getAssessedRiskFactorId());
				}
				
				assessedRiskFactorRepository.save(entity);
			}
		}

	}
	
	private List<AssessedRiskRange> getRiskRanges(RiskAssessmentEntity riskAssessmentEntity) {
		
		long riskRangeTypeId = 0;

		List<AssessedRiskRange> assessedRiskRanges = new ArrayList<AssessedRiskRange>();

		if(riskAssessmentEntity.getAssessedRiskFactors().size() > 0) {
			riskRangeTypeId = riskAssessmentEntity.getAssessedRiskFactors().get(0).getRiskRangeType().getId();
		}
		else {
			return assessedRiskRanges;
		}
		
		AssessedRiskRange assessedRiskRange = new AssessedRiskRange(riskRangeTypeId, 
				riskAssessmentEntity.getAssessedRiskFactors().get(0).getRiskRangeType().getName());
		
		List<RiskRangeTypeRange> riskRangeTypeRanges = 
				getRiskRangeTypeRanges(riskAssessmentEntity.getAssessedRiskFactors().get(0).getRiskRangeType().getRiskRangeTypeRanges());
			
		assessedRiskRange.setRiskRangeTypeRanges(riskRangeTypeRanges);
		assessedRiskRanges.add(assessedRiskRange);
		
		for(AssessedRiskFactorEntity factor: riskAssessmentEntity.getAssessedRiskFactors()) {
			
			if(riskRangeTypeId != factor.getRiskRangeType().getId()) {
				riskRangeTypeId = factor.getRiskRangeType().getId();
				assessedRiskRange = new AssessedRiskRange(riskRangeTypeId, factor.getRiskRangeType().getName());
				riskRangeTypeRanges = getRiskRangeTypeRanges(factor.getRiskRangeType().getRiskRangeTypeRanges());
				assessedRiskRange.setRiskRangeTypeRanges(riskRangeTypeRanges);
				assessedRiskRanges.add(assessedRiskRange);
			}

			AssessedRiskFactor assessedRiskFactor = modelMapper.map(factor, AssessedRiskFactor.class);
			assessedRiskFactor.setName(factor.getRiskFactor().getName());
			assessedRiskFactor.setWeightMultiplier(factor.getRiskFactor().getWeightMultiplier());
			assessedRiskFactor.setRiskFactorLevelId(factor.getRiskFactorLevel().getRiskFactorLevelId());
			
			assessedRiskRange.getRiskFactors().add(assessedRiskFactor);
		}
		
		return assessedRiskRanges;
	}
	
	private List<RiskRangeTypeRange> getRiskRangeTypeRanges(Set<RiskRangeTypeRangeEntity> ranges){
		List<RiskRangeTypeRange> riskRangeTypeRanges = new ArrayList<RiskRangeTypeRange>();
		
		ranges.forEach(entity ->
			{
				riskRangeTypeRanges.add(modelMapper.map(entity, RiskRangeTypeRange.class));
			}
			);
		
		riskRangeTypeRanges.sort(Comparator.comparing(RiskRangeTypeRange::getMin));
		
		return riskRangeTypeRanges;
	}

}
