package pcs.scor.model.risk.asmt;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pcs.scor.model.risk.tmpl.RiskRangeType;
import pcs.scor.model.risk.tmpl.RiskRangeTypeRange;

@Getter
@Setter
@NoArgsConstructor
public class AssessedRiskRange {

	private long assessedRiskRangeTypeId;
	private long riskRangeTypeId;
	String name;
	private RiskRangeType riskRangeType;
	private List<AssessedRiskFactor> riskFactors = new ArrayList<AssessedRiskFactor>();
	private List<RiskRangeTypeRange> riskRangeTypeRanges = new ArrayList<RiskRangeTypeRange>();

	public AssessedRiskRange(long id,	String name) {
		this.assessedRiskRangeTypeId = id;
		this.name = name;
	}
}
