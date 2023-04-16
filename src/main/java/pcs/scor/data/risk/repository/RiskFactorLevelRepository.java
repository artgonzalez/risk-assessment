package pcs.scor.data.risk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pcs.scor.domain.risk.tmpl.RiskFactorLevelEntity;

public interface RiskFactorLevelRepository extends JpaRepository<RiskFactorLevelEntity, Long> {
	
	List<RiskFactorLevelEntity> findByRiskFactorId(long riskFactorId);
}
