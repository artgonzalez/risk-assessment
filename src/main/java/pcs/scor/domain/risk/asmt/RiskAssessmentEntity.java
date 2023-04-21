package pcs.scor.domain.risk.asmt;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pcs.scor.domain.risk.tmpl.RiskAssessmentTemplateEntity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "risk_assessment")
public class RiskAssessmentEntity {
	
	public RiskAssessmentEntity(long riskAssessmentId) {
		this.riskAssessmentId = riskAssessmentId;
	}
	
	public RiskAssessmentEntity(int riskAssessmentId, int contractId, Date date, String string) {
		this.contractId = contractId;
		this.primaryRiskAssessor = string;
		this.riskAssessmentDate = date;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "risk_assessment_id")
	private long riskAssessmentId;
	
	@OneToOne
	@JoinColumn(name="risk_assessment_template_id")
	private RiskAssessmentTemplateEntity riskAssessmentTemplate;
	
	private long contractId;
	private Date riskAssessmentDate;
	private String fiscalYear;
	private String primaryRiskAssessor;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name= "risk_assessment_id")
	private List<AssessedRiskFactorEntity> assessedRiskFactors;

}
