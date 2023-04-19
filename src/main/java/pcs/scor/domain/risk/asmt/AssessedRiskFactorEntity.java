package pcs.scor.domain.risk.asmt;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pcs.scor.domain.risk.tmpl.RiskFactorEntity;
import pcs.scor.domain.risk.tmpl.RiskFactorLevelEntity;
import pcs.scor.domain.risk.tmpl.RiskRangeTypeEntity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "risk_factor_assessed")
//@IdClass(AssessedRiskFactorEntityId.class)
public class AssessedRiskFactorEntity{
	
	public AssessedRiskFactorEntity(RiskAssessmentEntity riskAssessment, RiskRangeTypeEntity riskRangeType,RiskFactorEntity riskFactor,
			RiskFactorLevelEntity riskFactorLevel) {
		this.riskAssessment = riskAssessment;
		this.riskRangeType = riskRangeType;
		this.riskFactor = riskFactor;
		this.riskFactorLevel = riskFactorLevel;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="assessed_risk_factor_id")
	private long assessedRiskFactorId;
	
	@ManyToOne
	@JoinColumn(name = "risk_assessment_id")
	private RiskAssessmentEntity riskAssessment;
	
	@OneToOne
	@JoinColumn(name = "risk_range_type_id")
	private RiskRangeTypeEntity riskRangeType;
	
	@OneToOne
	@JoinColumn(name = "risk_factor_id")
	private RiskFactorEntity riskFactor;	

	@OneToOne
	@JoinColumn(name="risk_factor_level_id")
	private RiskFactorLevelEntity riskFactorLevel;
	
	private int score; 
}
