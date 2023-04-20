package pcs.scor.model.risk.asmt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pcs.scor.model.risk.tmpl.RiskFactor;
import pcs.scor.model.risk.tmpl.RiskFactorLevel;

@Getter
@Setter
@NoArgsConstructor
public class AssessedRiskFactor {
	public AssessedRiskFactor(long riskAssessmentId, long riskRangeTypeId, long riskFactorId, long riskFactorLevelId,
			RiskFactor riskFactor, RiskFactorLevel riskFactorLevel) {
		this.riskAssessmentId = riskAssessmentId;
		this.riskRangeTypeId = riskRangeTypeId;
		this.riskFactorId = riskFactorId;
		this.riskFactorLevelId = riskFactorLevelId;
	}
	
	private long assessedRiskFactorId;
	private long riskAssessmentId;
	private long riskRangeTypeId;
	private long riskFactorId;
	private long riskFactorLevelId;
	private RiskFactor riskFactor;
	private RiskFactorLevel riskFactorLevel;
	private int score;
}
