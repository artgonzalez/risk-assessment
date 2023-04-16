package pcs.scor.model.risk.tmpl;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RiskAssessmentTemplate{
	
	private long id;
	private Set<RiskRangeType> riskRangeTypes = new HashSet<>();
	private String version;
	private Date effective_start_date;
	private Date effective_end_date;
	private String comment;
}
