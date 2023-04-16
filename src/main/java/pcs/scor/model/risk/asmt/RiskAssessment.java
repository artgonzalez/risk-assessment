package pcs.scor.model.risk.asmt;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RiskAssessment {
	private long riskAssessmentId;
	private long contractId;
	private Date riskAssessmentDate;
	private String primaryRiskAssessor;
	private String version;
	//private Set<RiskRangeType> riskRangeTypes = new HashSet<>();
	//private RiskFactorLevel riskFactorLevel;
	//private List<RiskAssessmentRiskFactorAsmtLevel> riskAssessmentRiskFactors = new ArrayList<RiskAssessmentRiskFactorAsmtLevel>();
	private List<RiskAssessmentRiskRangeType> riskRangeTypes;
}
