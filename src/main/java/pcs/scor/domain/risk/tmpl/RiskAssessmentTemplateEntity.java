package pcs.scor.domain.risk.tmpl;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "risk_assessment_template")
public class RiskAssessmentTemplateEntity {
	public RiskAssessmentTemplateEntity(String version, Date start_date, Date end_date, String comment) {
		this.version = version;
		this.effective_start_date = start_date;
		this.effective_end_date = end_date;
		this.comment = comment;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen_risk_assessment_template")
    @SequenceGenerator(name = "seq_gen_risk_assessment_template", sequenceName = "seq_risk_assessment_template", initialValue = 100, allocationSize = 1)
	@Column(name = "risk_assessment_template_id")
	private long id;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy="riskAssessmentTemplate")
	private Set<RiskRangeTypeEntity> riskRangeTypes;
	
	private String version;
	private Date effective_start_date;
	private Date effective_end_date;
	private String comment;
}
