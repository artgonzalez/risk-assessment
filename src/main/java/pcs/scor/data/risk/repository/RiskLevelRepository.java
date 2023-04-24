package pcs.scor.data.risk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pcs.scor.domain.risk.tmpl.RiskLevelEntity;

 public interface RiskLevelRepository extends JpaRepository<RiskLevelEntity, Integer> {
	
}
