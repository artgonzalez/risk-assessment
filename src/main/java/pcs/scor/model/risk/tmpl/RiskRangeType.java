package pcs.scor.model.risk.tmpl;


import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RiskRangeType {
	private long id;
	private String name;
	private List<RiskFactor> riskFactors;
	private List<RiskRangeTypeRange> riskRangeTypeRanges;
}
