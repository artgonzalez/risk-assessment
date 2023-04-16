package pcs.scor.data.risk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pcs.scor.domain.risk.asmt.AssessedRiskFactorEntity;
public interface AssessedRiskFactorRepository extends JpaRepository<AssessedRiskFactorEntity, Long> {
	//AssessedRiskFactorEntity findByRiskAssessmentAndRiskFactorAndRiskFactorLevel
		//(RiskAssessmentEntity riskAssessment, RiskFactorEntity riskFactor, RiskFactorLevelEntity riskFactorLevel);
	//List<RiskAssessmentRiskFactorEntity> findByRiskAssessment(RiskAssessmentEntity riskAssessment);
}
