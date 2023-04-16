package pcs.scor.data.risk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pcs.scor.domain.risk.tmpl.RiskRangeTypeRangeEntity;

@Repository
public interface RiskRangeTypeRangeRepository extends JpaRepository<RiskRangeTypeRangeEntity, Long> {
	List<RiskRangeTypeRangeEntity> findByRiskRangeTypeId(long riskRangeTypeId);
}
