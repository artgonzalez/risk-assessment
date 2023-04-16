package pcs.scor.model.risk.tmpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RiskFactorLevel {
	private long riskFactorLevelId;
	private String level;
	private int score;	
}
