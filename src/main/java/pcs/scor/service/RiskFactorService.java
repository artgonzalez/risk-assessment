package pcs.scor.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import pcs.scor.data.risk.repository.RiskFactorLevelRepository;
import pcs.scor.data.risk.repository.RiskFactorRepository;
import pcs.scor.data.risk.repository.RiskRangeTypeRepository;
import pcs.scor.domain.risk.tmpl.RiskFactorEntity;
import pcs.scor.domain.risk.tmpl.RiskFactorLevelEntity;
import pcs.scor.model.risk.tmpl.RiskFactor;
import pcs.scor.model.risk.tmpl.RiskFactorLevel;

@Service
public class RiskFactorService {
	
	@Autowired
	RiskRangeTypeRepository riskRangeTypeRepository;
	@Autowired
	RiskFactorRepository riskFactorRepository;
	@Autowired
	RiskFactorLevelRepository riskFactorLevelRepository;
	@Autowired
    private ModelMapper modelMapper;
	
	public List<RiskFactor> findByRiskRangeTypeId(@PathVariable("riskRangeTypeId") long id){
		List<RiskFactorEntity> riskFactorEntities = riskFactorRepository.findByRiskRangeTypeId(id);
		List<RiskFactor> riskFactors = new ArrayList<RiskFactor>();
		
		riskFactorEntities.forEach(entity -> riskFactors.add(modelMapper.map(entity, RiskFactor.class)));
		
		return riskFactors;
	}
	
	public List<RiskFactorLevel> findByRiskFactorId(@PathVariable("riskFactorId") long riskFactorId){
		List<RiskFactorLevelEntity> riskFactorLevelEntities = riskFactorLevelRepository.findByRiskFactorId(riskFactorId);
		List<RiskFactorLevel> riskFactorLevels = new ArrayList<RiskFactorLevel>();
		
		riskFactorLevelEntities.forEach(entity -> riskFactorLevels.add(modelMapper.map(entity, RiskFactorLevel.class)));
		
		return riskFactorLevels;
	}
}
