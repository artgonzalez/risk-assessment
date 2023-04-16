package pcs.scor.domain.risk.asmt;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pcs.scor.domain.risk.tmpl.RiskFactorEntity;
import pcs.scor.domain.risk.tmpl.RiskFactorLevelEntity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "risk_assessment_risk_factor")
@IdClass(AssessedRiskFactorEntityId.class)
public class AssessedRiskFactorEntity {
	/*@Id
	@ManyToOne
	@JoinColumn(name = "risk_assessment_id")
	private RiskAssessmentEntity riskAssessment;*/
	
	@Id
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name = "risk_assessment_id"),
		@JoinColumn(name = "risk_range_type_id")
		})
	private RiskAssessmentRiskRangeTypeEntity riskAssessmentRiskRangeType;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "risk_factor_id")
	private RiskFactorEntity riskFactor;	

	@OneToOne
	@JoinColumn(name="risk_factor_level_id")
	private RiskFactorLevelEntity riskFactorLevel;
	
	private int score; 
}
