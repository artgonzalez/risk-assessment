package pcs.scor.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pcs.scor.data.risk.repository.RiskAssessmentRepository;
import pcs.scor.domain.risk.asmt.RiskAssessmentEntity;
import pcs.scor.model.risk.asmt.RiskAssessment;

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
		RiskAssessmentEntity riskAssessment = riskAssessmentRepository.findOne(riskAssessmentId);
		
		RiskAssessment assessment = modelMapper.map(riskAssessment, RiskAssessment.class);
		
		return assessment;
	}
}
