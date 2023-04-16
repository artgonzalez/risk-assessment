package pcs.scor.data.risk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pcs.scor.domain.risk.tmpl.RiskRangeTypeEntity;

public interface RiskRangeTypeRepository extends JpaRepository<RiskRangeTypeEntity, Long> {
	List<RiskRangeTypeEntity> findByRiskAssessmentTemplateId(long riskAssessmentTemplateId);
}
