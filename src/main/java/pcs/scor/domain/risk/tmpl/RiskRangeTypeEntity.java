package pcs.scor.domain.risk.tmpl;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "risk_range_type")
public class RiskRangeTypeEntity {
	public RiskRangeTypeEntity(long riskRangeTypeId) {
		this.id = riskRangeTypeId;
	}
	
	public RiskRangeTypeEntity(RiskAssessmentTemplateEntity riskAssessmentTemplate, String name) {
		this.riskAssessmentTemplate = riskAssessmentTemplate;
		this.name = name;
	}
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "risk_range_type_id")
	private long id;
	private String name;
	
	@ManyToOne
	@JoinColumn(name="risk_assessment_template_id")
	RiskAssessmentTemplateEntity riskAssessmentTemplate;
	
	@OneToMany(mappedBy="riskRangeType")
	private Set<RiskRangeTypeRangeEntity> riskRangeTypeRanges;

	@OneToMany(mappedBy = "riskRangeType")
	private Set<RiskFactorEntity> riskFactors;
}
