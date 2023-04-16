package pcs.scor.data.risk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pcs.scor.domain.risk.asmt.RiskAssessmentEntity;

public interface RiskAssessmentRepository extends JpaRepository<RiskAssessmentEntity, Long>{
	List<RiskAssessmentEntity> findByContractId(long contractId);
	RiskAssessmentEntity findByRiskAssessmentId(long riskAssessmentId);
}
