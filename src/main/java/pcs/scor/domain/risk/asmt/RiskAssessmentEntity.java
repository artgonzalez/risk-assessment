package pcs.scor.domain.risk.asmt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pcs.scor.domain.risk.tmpl.RiskRangeTypeEntity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "risk_assessment")
public class RiskAssessmentEntity {
	public RiskAssessmentEntity(int riskAssessmentId, int contractId, Date date, String string) {
		this.contractId = contractId;
		this.primary_risk_assessor = string;
		this.risk_assessment_date = date;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "risk_assessment_id")
	private long riskAssessmentId;
	private long contractId;
	private Date risk_assessment_date;
	private String primary_risk_assessor;
	
	/*@ManyToMany(fetch = FetchType.LAZY,
	      cascade = {
	          CascadeType.PERSIST,
	          CascadeType.MERGE
	      })
	@JoinTable(name = "risk_assessment_risk_range_type",
	       joinColumns = { @JoinColumn(name = "risk_assessment_id") },
	       inverseJoinColumns = { @JoinColumn(name = "risk_range_type_id") })*/
	@OneToMany(mappedBy = "riskAssessment")
	private List<RiskRangeTypeEntity> templateRiskRangeTypes = new ArrayList<RiskRangeTypeEntity>();
	
/*	@OneToMany(mappedBy = "riskAssessment")
	private List<iskAssessmentRiskRangeType> riskAssessmentRiskFactors ;*/

	@OneToMany(mappedBy = "riskAssessment")
	private List<RiskAssessmentRiskRangeTypeEntity> riskRangeTypes;

}
