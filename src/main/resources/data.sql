insert into risk_level(risk_level_id, level) values(seq_risk_level.nextval,'Low');
insert into risk_level(risk_level_id, level) values(seq_risk_level.nextval,'Medium');
insert into risk_level(risk_level_id, level) values(seq_risk_level.nextval,'High');

insert into risk_assessment_template(risk_assessment_template_id, comment, effective_start_date, effective_end_date,version) 
                              values(seq_risk_assessment_template.nextval,'Testing', '2022-12-31' ,'2023-12-31' , '1.0');

insert into risk_range_type(risk_range_type_id, name, risk_assessment_template_id) values(seq_risk_range_type.nextval, 'Tier 1', 1);

insert into risk_range_type_range(risk_range_type_range_id, min, max, risk_level_id, risk_range_type_id)values(seq_risk_range_type_range.nextval, 3, 7, 1, 1);
insert into risk_range_type_range(risk_range_type_range_id, min, max, risk_level_id, risk_range_type_id)values(seq_risk_range_type_range.nextval, 8, 12, 2, 1);
insert into risk_range_type_range(risk_range_type_range_id, min, max, risk_level_id, risk_range_type_id)values(seq_risk_range_type_range.nextval, 13, 17, 3, 1);

insert into risk_factor(risk_factor_id, name, weight_multiplier, risk_range_type_id) values(seq_risk_factor.nextval, 'Number of Active Contracts with HHS', 1, 1);

insert into risk_factor_level(risk_factor_level_id, level, score, risk_factor_id, risk_level_id) values(seq_risk_factor_level.nextval, 'Less than 5 contracts', 1, 1, 1);
insert into risk_factor_level(risk_factor_level_id, level, score, risk_factor_id, risk_level_id) values(seq_risk_factor_level.nextval, '6 to 14 contracts', 3, 1, 2);
insert into risk_factor_level(risk_factor_level_id, level, score, risk_factor_id, risk_level_id) values(seq_risk_factor_level.nextval, 'More than 15 contracts', 5, 1, 3);

insert into risk_factor(risk_factor_id, name, weight_multiplier, risk_range_type_id) values(seq_risk_factor.nextval, 'Total Expenditures from Active HHS Contracts', 1, 1);

insert into risk_factor_level(risk_factor_level_id, level, score, risk_factor_id, risk_level_id) values(seq_risk_factor_level.nextval, 'Less than $1 million', 1, 2, 1);
insert into risk_factor_level(risk_factor_level_id, level, score, risk_factor_id, risk_level_id) values(seq_risk_factor_level.nextval, '$1 million to $9,999,999.99', 3, 2, 2);
insert into risk_factor_level(risk_factor_level_id, level, score, risk_factor_id, risk_level_id) values(seq_risk_factor_level.nextval, '$10 million to $24,999,999.99', 5, 2, 3);

insert into risk_factor(risk_factor_id, name, weight_multiplier, risk_range_type_id) values(seq_risk_factor.nextval, 'Number of Adverse Actions taken by HHS', 1, 1);

insert into risk_factor_level(risk_factor_level_id, level, score, risk_factor_id, risk_level_id) values(seq_risk_factor_level.nextval, 'No adverse actions taken', 1, 3, 1);
insert into risk_factor_level(risk_factor_level_id, level, score, risk_factor_id, risk_level_id) values(seq_risk_factor_level.nextval, '1-3 adverse actions taken', 3, 3, 2);
insert into risk_factor_level(risk_factor_level_id, level, score, risk_factor_id, risk_level_id) values(seq_risk_factor_level.nextval, 'More than 3 adverse actions taken', 5, 3, 3);

insert into risk_range_type(risk_range_type_id, name, risk_assessment_template_id) values(seq_risk_range_type.nextval, 'Tier 2', 1);

insert into risk_range_type_range(risk_range_type_range_id, min, max, risk_level_id, risk_range_type_id)values(seq_risk_range_type_range.nextval, 3, 6, 1, 2);
insert into risk_range_type_range(risk_range_type_range_id, min, max, risk_level_id, risk_range_type_id)values(seq_risk_range_type_range.nextval, 7, 10, 2, 2);
insert into risk_range_type_range(risk_range_type_range_id, min, max, risk_level_id, risk_range_type_id)values(seq_risk_range_type_range.nextval, 11, 15, 3, 2);

insert into risk_factor(risk_factor_id, name, weight_multiplier, risk_range_type_id) values(seq_risk_factor.nextval, 'Contractual Relationship', 1, 2);

insert into risk_factor_level(risk_factor_level_id, level, score, risk_factor_id, risk_level_id) values(seq_risk_factor_level.nextval, 'Contractor Other', 1, 4, 1);
insert into risk_factor_level(risk_factor_level_id, level, score, risk_factor_id, risk_level_id) values(seq_risk_factor_level.nextval, 'Recipient', 3, 4, 2);
insert into risk_factor_level(risk_factor_level_id, level, score, risk_factor_id, risk_level_id) values(seq_risk_factor_level.nextval, 'Subrecipient', 5, 4, 3);

insert into risk_factor(risk_factor_id, name, weight_multiplier, risk_range_type_id) values(seq_risk_factor.nextval, 'Dollar Amount of Contract', 1, 2);

insert into risk_factor_level(risk_factor_level_id, level, score, risk_factor_id, risk_level_id) values(seq_risk_factor_level.nextval, 'Less than $500,000', 1, 5, 1);
insert into risk_factor_level(risk_factor_level_id, level, score, risk_factor_id, risk_level_id) values(seq_risk_factor_level.nextval, '$500,000 to $1 Million', 3, 5, 2);
insert into risk_factor_level(risk_factor_level_id, level, score, risk_factor_id, risk_level_id) values(seq_risk_factor_level.nextval, 'More than $1 Million', 5, 5, 3);

insert into risk_factor(risk_factor_id, name, weight_multiplier, risk_range_type_id) values(seq_risk_factor.nextval, 'Risk to Health and Safety', 1, 2);

insert into risk_factor_level(risk_factor_level_id, level, score, risk_factor_id, risk_level_id) values(seq_risk_factor_level.nextval, 'No Direct Contact', 1, 6, 1);
insert into risk_factor_level(risk_factor_level_id, level, score, risk_factor_id, risk_level_id) values(seq_risk_factor_level.nextval, 'Incidental Contact', 3, 6, 2);
insert into risk_factor_level(risk_factor_level_id, level, score, risk_factor_id, risk_level_id) values(seq_risk_factor_level.nextval, 'Direct Contact', 5, 6, 3);



