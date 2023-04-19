package pcs.scor.domain.risk.tmpl;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "risk_factor")
public class RiskFactorEntity {
	public RiskFactorEntity(long riskFactorId) {
		id = riskFactorId;
	}
	
	public RiskFactorEntity(RiskRangeTypeEntity riskRangeType, String name, int weightMultiplier)	{
		this.riskRangeType = riskRangeType;
		this.name = name;
		this.weightMultiplier = weightMultiplier;
	}	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="risk_factor_id")
	private long id;
	private String name;
	private int weightMultiplier;
	
	@OneToMany(mappedBy="riskFactor")
	private Set<RiskFactorLevelEntity> riskFactorLevels;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="risk_range_type_id")
	RiskRangeTypeEntity riskRangeType;
}
