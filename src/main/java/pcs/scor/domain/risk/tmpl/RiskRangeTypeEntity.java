package pcs.scor.domain.risk.tmpl;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "risk_range_type")
public class RiskRangeTypeEntity {
	public RiskRangeTypeEntity(long riskRangeTypeId) {
		this.riskRangeTypeId = riskRangeTypeId;
	}
	
	public RiskRangeTypeEntity(RiskAssessmentTemplateEntity riskAssessmentTemplate, String name) {
		this.riskAssessmentTemplate = riskAssessmentTemplate;
		this.name = name;
	}
		
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen_risk_range_type")
    @SequenceGenerator(name = "seq_gen_risk_range_type", sequenceName = "seq_risk_range_type", initialValue = 100, allocationSize = 1)
	private long riskRangeTypeId;
	private String name;
	
	@ManyToOne
	@JoinColumn(name="risk_assessment_template_id", foreignKey=@ForeignKey(name = "FK_risk_range_type_risk_assessment_template"))
	RiskAssessmentTemplateEntity riskAssessmentTemplate;
	
	@OneToMany(mappedBy="riskRangeType")
	private Set<RiskRangeTypeRangeEntity> riskRangeTypeRanges = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "riskRangeType")
	private Set<RiskFactorEntity> riskFactors = new HashSet<>();
}
