package pcs.scor.model.risk.asmt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RiskAssessedContract {
	private long riskAssessmentId;
	private long riskAssessmentTemplateId;
	private long contractId;
	private Date riskAssessmentDate;
	private String fiscalYear;
	private String primaryRiskAssessor;
	private String riskAssessmentTemplateVersion;
	private String createdBy;
	private Date createdDate;
	private String updatedBy;
	private Date updatedDate;

	private List<AssessedRiskRange> riskRangeTypes = new ArrayList<AssessedRiskRange>(); 
}
