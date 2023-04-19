package pcs.scor.model.risk.asmt;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AssessedRiskRange {
	public AssessedRiskRange(long riskRangeTypeId,	String name) {
		this.riskRangeTypeId = riskRangeTypeId;
		this.name = name;
	}
	
	long riskRangeTypeId;
	String name;
	private List<AssessedRiskFactor> riskFactors = new ArrayList<AssessedRiskFactor>();
}
