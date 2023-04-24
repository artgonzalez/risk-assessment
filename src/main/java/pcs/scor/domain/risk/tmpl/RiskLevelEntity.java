package pcs.scor.domain.risk.tmpl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "risk_level")
public class RiskLevelEntity {
	public RiskLevelEntity(int risklevelId) {
		this.riskLevelId = risklevelId;
	}
	
	public RiskLevelEntity(String level) {
		this.level = level;
	}
	
	public RiskLevelEntity(int risklevelId, String level) {
		this.riskLevelId = risklevelId;
		this.level = level;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen_risk_level")
    @SequenceGenerator(name = "seq_gen_risk_level", sequenceName = "seq_risk_level", initialValue = 100, allocationSize = 1)
	private int riskLevelId;
	private String level;
}
