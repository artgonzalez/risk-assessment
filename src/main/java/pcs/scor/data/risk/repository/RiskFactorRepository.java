package pcs.scor.data.risk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pcs.scor.domain.risk.tmpl.RiskFactorEntity;

public interface RiskFactorRepository extends JpaRepository<RiskFactorEntity, Long> {
	List<RiskFactorEntity> findByRiskRangeTypeId(long riskRangeTypeId);
}
