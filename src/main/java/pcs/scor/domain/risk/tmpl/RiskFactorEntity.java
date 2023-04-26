package pcs.scor.domain.risk.tmpl;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen_risk_factor")
    @SequenceGenerator(name = "seq_gen_risk_factor", sequenceName = "seq_risk_factor", initialValue = 100, allocationSize = 1)
	@Column(name="risk_factor_id")
	private long id;
	private String name;
	private int weightMultiplier;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="riskFactor")
	private Set<RiskFactorLevelEntity> riskFactorLevels = new HashSet<>();
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="risk_range_type_id", foreignKey=@ForeignKey(name = "FK_risk_factor_risk_range_type"))
	RiskRangeTypeEntity riskRangeType;
}
