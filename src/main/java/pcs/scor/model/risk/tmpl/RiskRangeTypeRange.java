package pcs.scor.model.risk.tmpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RiskRangeTypeRange {
	private int riskRangeTypeRangeId;
	private String level; 
	private int min;
	private int max;
}
