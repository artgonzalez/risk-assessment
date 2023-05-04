package pcs.scor.data.risk.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import pcs.scor.domain.risk.asmt.RiskAssessmentEntity;

public interface RiskAssessmentRepository extends JpaRepository<RiskAssessmentEntity, Long>{
	Page<RiskAssessmentEntity> findByContractId(long contractId, Pageable page);
	RiskAssessmentEntity findByRiskAssessmentId(long riskAssessmentId);
}
