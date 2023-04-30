package pcs.scor.domain.risk.tmpl;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "risk_factor_level")
public class RiskFactorLevelEntity {
	public RiskFactorLevelEntity(long riskFactorLevelId) {
		this.riskFactorLevelId = riskFactorLevelId;
	}
	
	public RiskFactorLevelEntity(RiskFactorEntity riskFactor, RiskLevelEntity riskLevelEntity, String levelName, int score) 
	{
		this.riskFactor = riskFactor;
		this.riskLevel = riskLevelEntity;
		this.levelName = levelName;
		this.score = score;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen_risk_factor_level")
    @SequenceGenerator(name = "seq_gen_risk_factor_level", sequenceName = "seq_risk_factor_level", initialValue = 100, allocationSize = 1)
	@Column(name = "risk_factor_level_id")
	private long riskFactorLevelId;

	@OneToOne
	@JoinColumn(name = "risk_level_id", foreignKey=@ForeignKey(name = "FK_rsk_fctr_lvl_rsk_lvl"))
	private RiskLevelEntity riskLevel;
	
	private String levelName;
	private int score;	
	
	@ManyToOne
	@JoinColumn(name="risk_factor_id", foreignKey=@ForeignKey(name = "FK_rsk_fctr_lvl_rsk_fctr"))
	RiskFactorEntity riskFactor;
}
