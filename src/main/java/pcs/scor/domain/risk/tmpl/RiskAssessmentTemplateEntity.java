package pcs.scor.domain.risk.tmpl;

import java.util.Date;
import java.util.HashSet;
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
@Table(name = "risk_asmt_template_2")
public class RiskAssessmentTemplateEntity{
	public RiskAssessmentTemplateEntity(long riskAssessmentTemplateId) {
		this.riskAssessmentTemplateId = riskAssessmentTemplateId;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen_risk_assessment_template")
    @SequenceGenerator(name = "seq_gen_risk_assessment_template", sequenceName = "seq_risk_asmt_template", initialValue = 100, allocationSize = 1)
	@Column(name = "risk_assessment_template_id")
	private long riskAssessmentTemplateId;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy="riskAssessmentTemplate")
	private Set<RiskRangeTypeEntity> riskRangeTypes = new HashSet<>();
	
	private String version;
	private Date effectiveStartDate;
	private Date effectiveEndDate;
	private String comments;
	private String createdBy;
	private String updatedBy;
	private Date createdDate;
	private Date updatedDate;
}
