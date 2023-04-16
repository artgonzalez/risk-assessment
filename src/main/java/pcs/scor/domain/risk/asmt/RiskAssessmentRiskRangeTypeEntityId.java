package pcs.scor.domain.risk.asmt;

import java.io.Serializable;

import pcs.scor.domain.risk.tmpl.RiskRangeTypeEntity;

public class RiskAssessmentRiskRangeTypeEntityId implements Serializable {

	private static final long serialVersionUID = -4141837321000018622L;
	
	private RiskAssessmentEntity riskAssessment;
	private RiskRangeTypeEntity riskRangeType;
	
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((riskAssessment == null) ? 0 : riskAssessment.hashCode());
        result = prime * result + ((riskRangeType == null) ? 0 : riskRangeType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RiskAssessmentRiskRangeTypeEntityId other = (RiskAssessmentRiskRangeTypeEntityId) obj;
        if (riskAssessment == null) {
            if (other.riskAssessment != null)
                return false;
        } else if (!riskAssessment.equals(other.riskAssessment))
            return false;
        if (riskRangeType == null) {
            if (other.riskRangeType != null)
                return false;
        } else if (!riskRangeType.equals(other.riskRangeType))
            return false;
        return true;
    }
}
