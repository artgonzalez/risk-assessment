package pcs.scor.model.risk.asmt;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pcs.scor.model.risk.tmpl.RiskRangeType;

@Getter
@Setter
@NoArgsConstructor
public class RiskAssessmentRiskRangeType {
    //private RiskAssessment riskAssessment;
	private long id;
	private String name;
	private RiskRangeType riskRangeType;
	private Set<AssessedRiskFactor> riskFactors;
}
