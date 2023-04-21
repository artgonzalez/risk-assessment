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
	private long riskAssessmentTemplateId;
	private long contractId;
	private Date riskAssessmentDate;
	private String fiscalYear;
	private String primaryRiskAssessor;
	private String riskAssessmentTemplateVersion;
	private List<AssessedRiskRange> riskRangeTypes; 
}
