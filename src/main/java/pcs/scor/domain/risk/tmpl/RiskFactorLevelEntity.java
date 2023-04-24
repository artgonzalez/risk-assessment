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
	
	public RiskFactorLevelEntity(RiskFactorEntity riskFactor, RiskLevelEntity riskLevelEntity, String level, int score) 
	{
		this.riskFactor = riskFactor;
		this.riskLevelEntity = riskLevelEntity;
		this.level = level;
		this.score = score;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen_risk_factor_level")
    @SequenceGenerator(name = "seq_gen_risk_factor_level", sequenceName = "seq_risk_factor_level_template", initialValue = 100, allocationSize = 1)
	@Column(name = "risk_factor_level_id")
	private long riskFactorLevelId;

	@OneToOne
	@JoinColumn(name = "risk_level_id", foreignKey=@ForeignKey(name = "FK_risk_factor_level_risk_level"))
	private RiskLevelEntity riskLevelEntity;
	
	private String level;
	private int score;	
	
	@ManyToOne
	@JoinColumn(name="risk_factor_id", foreignKey=@ForeignKey(name = "FK_risk_factor_level_risk_factor"))
	RiskFactorEntity riskFactor;
}
