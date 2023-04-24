package pcs.scor;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import pcs.scor.data.risk.repository.RiskAssessmentRepository;
import pcs.scor.data.risk.repository.RiskAssessmentTemplateRepository;
import pcs.scor.data.risk.repository.RiskFactorLevelRepository;
import pcs.scor.data.risk.repository.RiskFactorRepository;
import pcs.scor.data.risk.repository.RiskLevelRepository;
import pcs.scor.data.risk.repository.RiskRangeTypeRangeRepository;
import pcs.scor.data.risk.repository.RiskRangeTypeRepository;
import pcs.scor.domain.risk.tmpl.RiskAssessmentTemplateEntity;
import pcs.scor.domain.risk.tmpl.RiskFactorEntity;
import pcs.scor.domain.risk.tmpl.RiskFactorLevelEntity;
import pcs.scor.domain.risk.tmpl.RiskLevelEntity;
import pcs.scor.domain.risk.tmpl.RiskRangeTypeEntity;
import pcs.scor.domain.risk.tmpl.RiskRangeTypeRangeEntity;

@Component
public class DemoData {
	@Autowired
	RiskAssessmentTemplateRepository riskAssessmentTemplateRepository;
	@Autowired
	RiskRangeTypeRepository riskRangeTypeRepository;
	@Autowired
	RiskRangeTypeRangeRepository riskRangeTypeRangeRepository;
	@Autowired
	RiskFactorRepository riskFactorRepository;
	@Autowired
	RiskFactorLevelRepository riskFactorLevelRepository;
	@Autowired
	RiskAssessmentRepository riskAssessmentRepository;
	@Autowired
	RiskLevelRepository riskLevelRepository;
	
	@EventListener
	public void appReady(ApplicationReadyEvent event) throws ParseException 
	{
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
		RiskLevelEntity riskLevelEntity1 = riskLevelRepository.save(new RiskLevelEntity("Low"));
		RiskLevelEntity riskLevelEntity2 = riskLevelRepository.save(new RiskLevelEntity("Medium"));
		RiskLevelEntity riskLevelEntity3 = riskLevelRepository.save(new RiskLevelEntity("High"));
		
		RiskAssessmentTemplateEntity template = riskAssessmentTemplateRepository.save(new RiskAssessmentTemplateEntity("1.0", formatter.parse("12-01-2021"), 
				formatter.parse("12-01-2022"), "Test"));
		
		RiskRangeTypeEntity riskRangeType = riskRangeTypeRepository.save(new RiskRangeTypeEntity(template, "Tier 1"));
		
		riskRangeTypeRangeRepository.save(new RiskRangeTypeRangeEntity(riskRangeType, riskLevelEntity1, 3, 7));
		riskRangeTypeRangeRepository.save(new RiskRangeTypeRangeEntity(riskRangeType, riskLevelEntity2, 8, 12));
		riskRangeTypeRangeRepository.save(new RiskRangeTypeRangeEntity(riskRangeType, riskLevelEntity3, 13, 17));
		
		RiskFactorEntity riskFactor1 = riskFactorRepository.save(new RiskFactorEntity(riskRangeType, "Number of Active Contracts with HHS", 1));
		RiskFactorEntity riskFactor2 = riskFactorRepository.save(new RiskFactorEntity(riskRangeType, "Total Expenditures from Active HHS Contracts", 1));
		RiskFactorEntity riskFactor3 = riskFactorRepository.save(new RiskFactorEntity(riskRangeType, "Number of Adverse Actions taken by HHS", 1));
		
		riskFactorLevelRepository.save(new RiskFactorLevelEntity(riskFactor1, riskLevelEntity1, "Less than 5 contracts", 1));
		riskFactorLevelRepository.save(new RiskFactorLevelEntity(riskFactor1, riskLevelEntity2, "6 to 14 contracts", 3));
		riskFactorLevelRepository.save(new RiskFactorLevelEntity(riskFactor1, riskLevelEntity3, "More than 15 contracts", 5));
		
		riskFactorLevelRepository.save(new RiskFactorLevelEntity(riskFactor2, riskLevelEntity1, "Less than $1 million", 1));
		riskFactorLevelRepository.save(new RiskFactorLevelEntity(riskFactor2, riskLevelEntity2, "$1 million to $9,999,999.99", 3));
		riskFactorLevelRepository.save(new RiskFactorLevelEntity(riskFactor2, riskLevelEntity3, "$10 million to $24,999,999.99", 5));
		
		riskFactorLevelRepository.save(new RiskFactorLevelEntity(riskFactor3, riskLevelEntity1, "No adverse actions taken", 1));
		riskFactorLevelRepository.save(new RiskFactorLevelEntity(riskFactor3, riskLevelEntity2, "1-3 adverse actions taken", 3));
		riskFactorLevelRepository.save(new RiskFactorLevelEntity(riskFactor3, riskLevelEntity3, "More than 3 adverse actions taken", 5));
		
		riskRangeType = riskRangeTypeRepository.save(new RiskRangeTypeEntity(template, "Tier 2"));
		
		riskRangeTypeRangeRepository.save(new RiskRangeTypeRangeEntity(riskRangeType, riskLevelEntity1, 3, 6));
		riskRangeTypeRangeRepository.save(new RiskRangeTypeRangeEntity(riskRangeType, riskLevelEntity2, 7, 10));
		riskRangeTypeRangeRepository.save(new RiskRangeTypeRangeEntity(riskRangeType, riskLevelEntity3, 11, 15));
		
		riskFactor1 = riskFactorRepository.save(new RiskFactorEntity(riskRangeType, "Contractual Relationship", 1));
		riskFactor2 = riskFactorRepository.save(new RiskFactorEntity(riskRangeType, "Dollar Amount of Contract", 1));
		riskFactor3 = riskFactorRepository.save(new RiskFactorEntity(riskRangeType, "Risk to Health and Safety", 1));
		
		riskFactorLevelRepository.save(new RiskFactorLevelEntity(riskFactor1, riskLevelEntity1, "Contractor Other", 1));
		riskFactorLevelRepository.save(new RiskFactorLevelEntity(riskFactor1, riskLevelEntity2, "Recipient", 3));
		riskFactorLevelRepository.save(new RiskFactorLevelEntity(riskFactor1, riskLevelEntity3, "Subrecipient", 5));
		
		riskFactorLevelRepository.save(new RiskFactorLevelEntity(riskFactor2, riskLevelEntity1, "Less than $500,000", 1));
		riskFactorLevelRepository.save(new RiskFactorLevelEntity(riskFactor2, riskLevelEntity2, "$500,000 to $1 Million", 3));
		riskFactorLevelRepository.save(new RiskFactorLevelEntity(riskFactor2, riskLevelEntity3, "More than $1 Million", 5));
		
		riskFactorLevelRepository.save(new RiskFactorLevelEntity(riskFactor3, riskLevelEntity1, "No Direct Contact", 1));
		riskFactorLevelRepository.save(new RiskFactorLevelEntity(riskFactor3, riskLevelEntity2, "Incidental Contact", 3));
		riskFactorLevelRepository.save(new RiskFactorLevelEntity(riskFactor3, riskLevelEntity3, "Direct Contact", 5));
				
		riskAssessmentTemplateRepository.save(new RiskAssessmentTemplateEntity("1.1", formatter.parse("12-01-2021"), 
				formatter.parse("12-01-2022"), "Testing"));
		riskAssessmentTemplateRepository.save(new RiskAssessmentTemplateEntity("1.2", formatter.parse("12-01-2021"), 
				formatter.parse("12-01-2022"), "Testing"));
		
		/*RiskAssessmentEntity entity = new RiskAssessmentEntity(0, 1, new Date(), "00000107183");
		entity.setRiskAssessmentTemplate(template);
		riskAssessmentRepository.save(entity);*/
	}
}
