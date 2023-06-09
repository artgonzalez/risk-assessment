package pcs.scor.model.risk.tmpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RiskRangeTypeRange {
	private int riskRangeTypeRangeId;
	private RiskLevel riskLevel; 
	private int min;
	private int max;
}
