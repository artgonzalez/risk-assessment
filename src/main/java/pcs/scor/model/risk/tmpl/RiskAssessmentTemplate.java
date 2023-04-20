package pcs.scor.model.risk.tmpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RiskAssessmentTemplate{
	
	private long id;
	private List<RiskRangeType> riskRangeTypes = new ArrayList<RiskRangeType>();
	private String version;
	private Date effective_start_date;
	private Date effective_end_date;
	private String comment;
}
