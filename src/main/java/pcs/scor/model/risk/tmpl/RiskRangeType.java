package pcs.scor.model.risk.tmpl;


import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RiskRangeType {
	private long id;
	private String name;
	private Set<RiskFactor> riskFactors;
	private Set<RiskRangeTypeRange> riskRangeTypeRanges;
}
