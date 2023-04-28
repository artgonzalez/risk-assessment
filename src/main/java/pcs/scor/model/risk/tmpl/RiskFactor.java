package pcs.scor.model.risk.tmpl;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RiskFactor {
	private long riskFactorId;
	private long riskRangeTypeId;
	private String name;
	private int weightMultiplier;
	private List<RiskFactorLevel> riskFactorLevels;
}
