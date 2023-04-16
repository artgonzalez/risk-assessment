package pcs.scor.model.risk.tmpl;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RiskFactor {
	private long id;
	private String name;
	private int weightMultiplier;
	private Set<RiskFactorLevel> riskFactorLevels;
}
