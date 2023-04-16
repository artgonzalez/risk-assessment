package pcs.scor.domain.risk.tmpl;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "risk_range_type_range")
public class RiskRangeTypeRangeEntity {
	public RiskRangeTypeRangeEntity(RiskRangeTypeEntity riskRangeType, String level, int min, int max) {
		this.riskRangeType = riskRangeType;
		this.level = level;
		this.min = min;
		this.max = max;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int riskRangeTypeRangeId;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="risk_range_type_id")
	@JsonIgnore
	private RiskRangeTypeEntity riskRangeType;
	private String level; 
	private int min;
	private int max;

}
