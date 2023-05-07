package pcs.scor.data.risk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import pcs.scor.domain.risk.asmt.RiskAssessedContractEntity;

public interface RiskAssessmentRepository extends JpaRepository<RiskAssessedContractEntity, Long>{
	Page<RiskAssessedContractEntity> findByContractId(long contractId, Pageable page);
	RiskAssessedContractEntity findByRiskAssessmentId(long riskAssessmentId);
}
