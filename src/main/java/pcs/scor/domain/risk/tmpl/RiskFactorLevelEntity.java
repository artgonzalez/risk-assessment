package pcs.scor.domain.risk.tmpl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	
	public RiskFactorLevelEntity(RiskFactorEntity riskFactor, String level, int score) 
	{
		this.riskFactor = riskFactor;
		this.level = level;
		this.score = score;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "risk_factor_level_id")
	private long riskFactorLevelId;
	private String level;
	private int score;	
	
	@ManyToOne
	@JoinColumn(name="risk_factor_id")
	@JsonIgnore
	RiskFactorEntity riskFactor;
}
