package pcs.scor.data.risk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pcs.scor.domain.risk.tmpl.RiskAssessmentTemplateEntity;

@Repository
public interface RiskAssessmentTemplateRepository extends JpaRepository<RiskAssessmentTemplateEntity, Long>{

}
