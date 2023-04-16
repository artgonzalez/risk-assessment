package pcs.scor.domain.risk.asmt;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pcs.scor.domain.risk.tmpl.RiskRangeTypeEntity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "risk_assessment_risk_range_type")
@IdClass(RiskAssessmentRiskRangeTypeEntityId.class)
public class RiskAssessmentRiskRangeTypeEntity {
	
	@Id
	@ManyToOne
	@JoinColumn(name = "risk_assessment_id")
	private RiskAssessmentEntity riskAssessment;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "risk_range_type_id")
	private RiskRangeTypeEntity riskRangeType;

	@OneToMany(mappedBy = "riskAssessmentRiskRangeType")
	private Set<AssessedRiskFactorEntity> riskAssessmentRiskFactors;
}
