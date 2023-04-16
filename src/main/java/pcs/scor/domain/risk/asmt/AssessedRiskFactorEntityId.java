package pcs.scor.domain.risk.asmt;

import java.io.Serializable;

import pcs.scor.domain.risk.tmpl.RiskFactorEntity;

public class AssessedRiskFactorEntityId implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6708760181974236421L;
	/**
	 * 
	 */
	
	private RiskAssessmentRiskRangeTypeEntity riskAssessmentRiskRangeType;
	private RiskFactorEntity riskFactor;
	
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((riskAssessmentRiskRangeType == null) ? 0 : riskAssessmentRiskRangeType.hashCode());
        result = prime * result + ((riskFactor == null) ? 0 : riskFactor.hashCode());
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
        AssessedRiskFactorEntityId other = (AssessedRiskFactorEntityId) obj;
        if (riskAssessmentRiskRangeType == null) {
            if (other.riskAssessmentRiskRangeType != null)
                return false;
        } else if (!riskAssessmentRiskRangeType.equals(other.riskAssessmentRiskRangeType))
            return false;
        if (riskFactor == null) {
            if (other.riskFactor != null)
                return false;
        } else if (!riskFactor.equals(other.riskFactor))
            return false;
        return true;
    }
}
