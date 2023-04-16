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

	//private RiskAssessment riskAssessment;
	private RiskFactor riskFactor;
	private RiskFactorLevel riskFactorLevel;
	private int score;
}
