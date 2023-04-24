package pcs.scor.domain.risk.tmpl;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "risk_range_type_range")
public class RiskRangeTypeRangeEntity {
	public RiskRangeTypeRangeEntity(RiskRangeTypeEntity riskRangeType, RiskLevelEntity riskLevelEntity, int min, int max) {
		this.riskRangeType = riskRangeType;
		this.riskLevelEntity = riskLevelEntity;
		this.min = min;
		this.max = max;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen_risk_range_type")
    @SequenceGenerator(name = "seq_gen_risk_range_type", sequenceName = "seq_risk_range_type", initialValue = 100, allocationSize = 1)
	private int riskRangeTypeRangeId;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="risk_range_type_id", foreignKey=@ForeignKey(name = "FK_risk_range_type_range_risk_range_type"))
	private RiskRangeTypeEntity riskRangeType;

	@OneToOne
	@JoinColumn(name = "risk_level_id", foreignKey=@ForeignKey(name = "FK_risk_range_type_risk_level"))
	private RiskLevelEntity riskLevelEntity;
	 
	private int min;
	private int max;

}
