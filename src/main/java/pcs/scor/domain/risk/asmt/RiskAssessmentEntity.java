package pcs.scor.domain.risk.asmt;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pcs.scor.domain.risk.tmpl.RiskAssessmentTemplateEntity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "risk_assessment_2")
public class RiskAssessmentEntity {
	
	public RiskAssessmentEntity(long riskAssessmentId) {
		this.riskAssessmentId = riskAssessmentId;
	}
	
	public RiskAssessmentEntity(long riskAssessmentId, long contractId, Date riskAssessmentDate, String primaryRiskAssessor, String fiscalYear) {
		this.riskAssessmentId = riskAssessmentId;
		this.contractId = contractId;
		this.riskAssessmentDate = riskAssessmentDate;
		this.primaryRiskAssessor = primaryRiskAssessor;
		this.fiscalYear = fiscalYear;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen_risk_assessment_2")
    @SequenceGenerator(name = "seq_gen_risk_assessment_2", sequenceName = "seq_risk_assessment_2", initialValue = 100, allocationSize = 1)
	@Column(name = "risk_assessment_id")
	private long riskAssessmentId;
	
	@OneToOne
	@JoinColumn(name="risk_assessment_template_id", foreignKey=@ForeignKey(name = "FK_risk_asmt_risk_asmt_tpl"))
	private RiskAssessmentTemplateEntity riskAssessmentTemplate;
	
	private long contractId;
	private Date riskAssessmentDate;
	private String fiscalYear;
	private String primaryRiskAssessor;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name= "risk_assessment_id")
	private Set<AssessedRiskRangeTypeEntity> riskRangeTypes = new HashSet<>();

}
