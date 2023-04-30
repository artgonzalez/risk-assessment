package pcs.scor.domain.risk.asmt;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pcs.scor.domain.risk.tmpl.RiskRangeTypeEntity;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "risk_range_type_assessed")
public class AssessedRiskRangeTypeEntity {
	public AssessedRiskRangeTypeEntity(RiskAssessmentEntity riskAssessment, RiskRangeTypeEntity riskRangeTypeEntity) {
		this.riskAssessment = riskAssessment;
		this.riskRangeTypeEntity = riskRangeTypeEntity;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen_assessed_risk_range_type")
    @SequenceGenerator(name = "seq_gen_assessed_risk_range_type", sequenceName = "seq_asssessed_risk_range_type", initialValue = 100, allocationSize = 1)
	@Column(name="assessed_risk_factor_id")
	private long assessedRiskRangeTypeId;
	
	@ManyToOne
	@JoinColumn(name = "risk_assessment_id", foreignKey=@ForeignKey(name = "FK_rsk_rg_typ_ad_rsk_asmt"))
	private RiskAssessmentEntity riskAssessment;
	
	@OneToOne
	@JoinColumn(name="risk_range_type_id", foreignKey=@ForeignKey(name = "FK_rsk_rg_typ_ad_rsk_rg_typ"))
	private RiskRangeTypeEntity riskRangeTypeEntity;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy="assessedRiskRangeType")
	private Set<AssessedRiskFactorEntity> assessedRiskFactors = new HashSet<>();
}
