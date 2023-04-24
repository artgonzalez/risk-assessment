package pcs.scor.model.risk.tmpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RiskFactorLevel {
	private long riskFactorLevelId;
	private RiskLevel riskLevel;
	private String level;
	private int score;	
}
