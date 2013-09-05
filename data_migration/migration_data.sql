-- This file is generated automatically. If you need to change it, change the spreedsheet and run the script create_migration_data.pl 


-- Begin Line: 2 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Hb Only','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Hb Only','Hb'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Hb'); 
SELECT insert_unit_of_measure('gm/dl','Hb'); 
SELECT create_relationship_test_section_test('Haematology','Hb'); 
SELECT insert_result_limit_normal_range('Hb',12,16); 
SELECT insert_result_limit_valid_range('Hb',1,22); 
-- Begin Line: 3 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Routine Blood','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Routine Blood','Packed Cell Volume (PCV)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Packed Cell Volume (PCV)'); 
SELECT insert_unit_of_measure('%','Packed Cell Volume (PCV)'); 
SELECT create_relationship_test_section_test('Haematology','Packed Cell Volume (PCV)'); 
SELECT insert_result_limit_normal_range('Packed Cell Volume (PCV)',45,55); 
-- Begin Line: 4 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Routine Blood','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Routine Blood','Total Leukocyte Count'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Total Leukocyte Count'); 
SELECT insert_unit_of_measure('cumm','Total Leukocyte Count'); 
SELECT create_relationship_test_section_test('Haematology','Total Leukocyte Count'); 
SELECT insert_result_limit_normal_range('Total Leukocyte Count',4500,11000); 
-- Begin Line: 5 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Routine Blood','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Routine Blood','Differential Count'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Differential Count'); 
SELECT insert_unit_of_measure('NA','Differential Count'); 
SELECT create_relationship_test_section_test('Haematology','Differential Count'); 
-- Begin Line: 6 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Routine Blood','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Routine Blood','Absolute Eosinphil Count'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Absolute Eosinphil Count'); 
SELECT insert_unit_of_measure('cumm','Absolute Eosinphil Count'); 
SELECT create_relationship_test_section_test('Haematology','Absolute Eosinphil Count'); 
SELECT insert_result_limit_normal_range('Absolute Eosinphil Count',100,600); 
SELECT insert_result_limit_valid_range('Absolute Eosinphil Count',100,30000); 
-- Begin Line: 7 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Routine Blood','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Routine Blood','Reticulocyte Count'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Reticulocyte Count'); 
SELECT insert_unit_of_measure('%','Reticulocyte Count'); 
SELECT create_relationship_test_section_test('Haematology','Reticulocyte Count'); 
SELECT insert_result_limit_normal_range('Reticulocyte Count',1.50,3); 
-- Begin Line: 8 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Sickling Only','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Sickling Only','Sickling Test'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Sickling Test'); 
SELECT insert_unit_of_measure('NA','Sickling Test'); 
SELECT create_relationship_test_section_test('Haematology','Sickling Test'); 
-- Begin Line: 9 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Hb w/Electrophoresis','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Hb w/Electrophoresis','Hb'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Hb'); 
SELECT insert_unit_of_measure('NA','Hb'); 
SELECT create_relationship_test_section_test('Haematology','Hb'); 
-- Begin Line: 10 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Hb w/Electrophoresis','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Hb w/Electrophoresis','Hb Electrophoresis'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Hb Electrophoresis'); 
SELECT insert_unit_of_measure('NA','Hb Electrophoresis'); 
SELECT create_relationship_test_section_test('Haematology','Hb Electrophoresis'); 
-- Begin Line: 11 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Sedimentation','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Sedimentation','ESR'); 
SELECT create_relationship_sample_test('Blood (EDTA)','ESR'); 
SELECT insert_unit_of_measure('mm/hour','ESR'); 
SELECT create_relationship_test_section_test('Haematology','ESR'); 
SELECT insert_result_limit_normal_range('ESR',5,20); 
SELECT insert_result_limit_valid_range('ESR',5,180); 
-- Begin Line: 12 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('RBC Morphology','Blood (EDTA)'); 
SELECT create_relationship_panel_test('RBC Morphology','PS for RBC Morphology'); 
SELECT create_relationship_sample_test('Blood (EDTA)','PS for RBC Morphology'); 
SELECT insert_unit_of_measure('NA','PS for RBC Morphology'); 
SELECT create_relationship_test_section_test('Haematology','PS for RBC Morphology'); 
-- Begin Line: 13 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Malaria','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Malaria','PS for Malaria Parasites'); 
SELECT create_relationship_sample_test('Blood (EDTA)','PS for Malaria Parasites'); 
SELECT insert_unit_of_measure('NA','PS for Malaria Parasites'); 
SELECT create_relationship_test_section_test('Haematology','PS for Malaria Parasites'); 
-- Begin Line: 14 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Microfilaria','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Microfilaria','PS for mf by concentration'); 
SELECT create_relationship_sample_test('Blood (EDTA)','PS for mf by concentration'); 
SELECT insert_unit_of_measure('NA','PS for mf by concentration'); 
SELECT create_relationship_test_section_test('Haematology','PS for mf by concentration'); 
-- Begin Line: 15 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Anaemia Panel','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Anaemia Panel','haemoglobin'); 
SELECT create_relationship_sample_test('Blood (EDTA)','haemoglobin'); 
SELECT insert_unit_of_measure('gm/dl','haemoglobin'); 
SELECT create_relationship_test_section_test('Haematology','haemoglobin'); 
SELECT insert_result_limit_normal_range('haemoglobin',11.50,15.50); 
-- Begin Line: 16 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Anaemia Panel','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Anaemia Panel','Packed Cell Volume (PCV)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Packed Cell Volume (PCV)'); 
SELECT insert_unit_of_measure('%','Packed Cell Volume (PCV)'); 
SELECT create_relationship_test_section_test('Haematology','Packed Cell Volume (PCV)'); 
SELECT insert_result_limit_normal_range('Packed Cell Volume (PCV)',45,55); 
-- Begin Line: 17 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Anaemia Panel','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Anaemia Panel','PS for RBC Morphology'); 
SELECT create_relationship_sample_test('Blood (EDTA)','PS for RBC Morphology'); 
SELECT insert_unit_of_measure('NA','PS for RBC Morphology'); 
SELECT create_relationship_test_section_test('Haematology','PS for RBC Morphology'); 
-- Begin Line: 18 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Anaemia Panel','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Anaemia Panel','Sickling Test'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Sickling Test'); 
SELECT insert_unit_of_measure('NA','Sickling Test'); 
SELECT create_relationship_test_section_test('Haematology','Sickling Test'); 
-- Begin Line: 19 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Anaemia Panel','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Anaemia Panel','Reticulocyte Count'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Reticulocyte Count'); 
SELECT insert_unit_of_measure('%','Reticulocyte Count'); 
SELECT create_relationship_test_section_test('Haematology','Reticulocyte Count'); 
SELECT insert_result_limit_normal_range('Reticulocyte Count',1,3); 
-- Begin Line: 20 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Anaemia Panel','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Anaemia Panel','Hb Electrophoresis'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Hb Electrophoresis'); 
SELECT insert_unit_of_measure('NA','Hb Electrophoresis'); 
SELECT create_relationship_test_section_test('Haematology','Hb Electrophoresis'); 
-- Begin Line: 21 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Anaemia Panel','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Anaemia Panel','Platelet Count'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Platelet Count'); 
SELECT insert_unit_of_measure('cumm','Platelet Count'); 
SELECT create_relationship_test_section_test('Haematology','Platelet Count'); 
SELECT insert_result_limit_normal_range('Platelet Count',10000,300000); 
-- Begin Line: 22 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Anaemia Panel','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Anaemia Panel','CBC'); 
SELECT create_relationship_sample_test('Blood (EDTA)','CBC'); 
SELECT insert_unit_of_measure('NA','CBC'); 
SELECT create_relationship_test_section_test('Haematology','CBC'); 
-- Begin Line: 23 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Anaemia Panel','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Anaemia Panel','AEC'); 
SELECT create_relationship_sample_test('Blood (EDTA)','AEC'); 
SELECT insert_unit_of_measure('cumm','AEC'); 
SELECT create_relationship_test_section_test('Haematology','AEC'); 
SELECT insert_result_limit_normal_range('AEC',100,600); 
SELECT insert_result_limit_valid_range('AEC',25,30000); 
-- Begin Line: 24 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Routine Urine','Urine'); 
SELECT create_relationship_panel_test('Routine Urine','Albumin'); 
SELECT create_relationship_sample_test('Urine','Albumin'); 
SELECT insert_unit_of_measure('NA','Albumin'); 
SELECT create_relationship_test_section_test('Haematology','Albumin'); 
-- Begin Line: 25 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Routine Urine','Urine'); 
SELECT create_relationship_panel_test('Routine Urine','Sugar'); 
SELECT create_relationship_sample_test('Urine','Sugar'); 
SELECT insert_unit_of_measure('NA','Sugar'); 
SELECT create_relationship_test_section_test('Urine','Sugar'); 
-- Begin Line: 26 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Routine Urine','Urine'); 
SELECT create_relationship_panel_test('Routine Urine','pH'); 
SELECT create_relationship_sample_test('Urine','pH'); 
SELECT insert_unit_of_measure('NA','pH'); 
SELECT create_relationship_test_section_test('Urine','pH'); 
SELECT insert_result_limit_normal_range('pH',6.80,7); 
SELECT insert_result_limit_valid_range('pH',0,14); 
-- Begin Line: 27 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Routine Urine','Urine'); 
SELECT create_relationship_panel_test('Routine Urine','Specific Gravity'); 
SELECT create_relationship_sample_test('Urine','Specific Gravity'); 
SELECT insert_unit_of_measure('NA','Specific Gravity'); 
SELECT create_relationship_test_section_test('Urine','Specific Gravity'); 
SELECT insert_result_limit_normal_range('Specific Gravity',1.01,1.03); 
SELECT insert_result_limit_valid_range('Specific Gravity',1000,1050); 
-- Begin Line: 28 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Urine LE/Nitrite','Urine'); 
SELECT create_relationship_panel_test('Urine LE/Nitrite','LE'); 
SELECT create_relationship_sample_test('Urine','LE'); 
SELECT insert_unit_of_measure('NA','LE'); 
SELECT create_relationship_test_section_test('Urine','LE'); 
-- Begin Line: 29 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Urine LE/Nitrite','Urine'); 
SELECT create_relationship_panel_test('Urine LE/Nitrite','Nitrite'); 
SELECT create_relationship_sample_test('Urine','Nitrite'); 
SELECT insert_unit_of_measure('NA','Nitrite'); 
SELECT create_relationship_test_section_test('Urine','Nitrite'); 
-- Begin Line: 30 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Renal','Urine'); 
SELECT create_relationship_panel_test('Renal','Renal Concentrating Ability'); 
SELECT create_relationship_sample_test('Urine','Renal Concentrating Ability'); 
SELECT insert_unit_of_measure('NA','Renal Concentrating Ability'); 
SELECT create_relationship_test_section_test('Urine','Renal Concentrating Ability'); 
-- Begin Line: 31 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Ketone','Urine'); 
SELECT create_relationship_panel_test('Ketone','Urine Ketone'); 
SELECT create_relationship_sample_test('Urine','Urine Ketone'); 
SELECT insert_unit_of_measure('NA','Urine Ketone'); 
SELECT create_relationship_test_section_test('Urine','Urine Ketone'); 
-- Begin Line: 32 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Bile Pigment','Urine'); 
SELECT create_relationship_panel_test('Bile Pigment','Urine Bile Pigment'); 
SELECT create_relationship_sample_test('Urine','Urine Bile Pigment'); 
SELECT insert_unit_of_measure('NA','Urine Bile Pigment'); 
SELECT create_relationship_test_section_test('Urine','Urine Bile Pigment'); 
-- Begin Line: 33 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Bile Salt','Urine'); 
SELECT create_relationship_panel_test('Bile Salt','Urine Bile Salt'); 
SELECT create_relationship_sample_test('Urine','Urine Bile Salt'); 
SELECT insert_unit_of_measure('NA','Urine Bile Salt'); 
SELECT create_relationship_test_section_test('Urine','Urine Bile Salt'); 
-- Begin Line: 34 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Microscopy','Urine'); 
SELECT create_relationship_panel_test('Microscopy','Urine Microscopy'); 
SELECT create_relationship_sample_test('Urine','Urine Microscopy'); 
SELECT insert_unit_of_measure('NA','Urine Microscopy'); 
SELECT create_relationship_test_section_test('Urine','Urine Microscopy'); 
-- Begin Line: 35 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('stool'); 
SELECT create_relationship_panel_sampletype('Microscopy w/o concentration','stool'); 
SELECT create_relationship_panel_test('Microscopy w/o concentration','Stool Microscopy'); 
SELECT create_relationship_sample_test('stool','Stool Microscopy'); 
SELECT insert_unit_of_measure('NA','Stool Microscopy'); 
SELECT create_relationship_test_section_test('Stool','Stool Microscopy'); 
-- Begin Line: 36 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('stool'); 
SELECT create_relationship_panel_sampletype('Microscopy w/ concentration','stool'); 
SELECT create_relationship_panel_test('Microscopy w/ concentration','Stool Microscopy'); 
SELECT create_relationship_sample_test('stool','Stool Microscopy'); 
SELECT insert_unit_of_measure('NA','Stool Microscopy'); 
SELECT create_relationship_test_section_test('Stool','Stool Microscopy'); 
-- Begin Line: 37 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('stool'); 
SELECT create_relationship_panel_sampletype('Kinyoun&apos;s Stain','stool'); 
SELECT create_relationship_panel_test('Kinyoun&apos;s Stain','Coccidians'); 
SELECT create_relationship_sample_test('stool','Coccidians'); 
SELECT insert_unit_of_measure('NA','Coccidians'); 
SELECT create_relationship_test_section_test('Stool','Coccidians'); 
-- Begin Line: 38 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('stool'); 
SELECT create_relationship_panel_sampletype('Occult blood','stool'); 
SELECT create_relationship_panel_test('Occult blood','Stool occult blood'); 
SELECT create_relationship_sample_test('stool','Stool occult blood'); 
SELECT insert_unit_of_measure('NA','Stool occult blood'); 
SELECT create_relationship_test_section_test('Stool','Stool occult blood'); 
-- Begin Line: 39 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('stool'); 
SELECT create_relationship_panel_sampletype('Environment','stool'); 
SELECT create_relationship_panel_test('Environment','Reducing substance'); 
SELECT create_relationship_sample_test('stool','Reducing substance'); 
SELECT insert_unit_of_measure('NA','Reducing substance'); 
SELECT create_relationship_test_section_test('Stool','Reducing substance'); 
-- Begin Line: 40 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('stool'); 
SELECT create_relationship_panel_sampletype('Environment','stool'); 
SELECT create_relationship_panel_test('Environment','pH'); 
SELECT create_relationship_sample_test('stool','pH'); 
SELECT insert_unit_of_measure('NA','pH'); 
SELECT create_relationship_test_section_test('Stool','pH'); 
SELECT insert_result_limit_normal_range('pH',6.80,7.20); 
SELECT insert_result_limit_valid_range('pH',1,14); 
-- Begin Line: 41 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('stool'); 
SELECT create_relationship_panel_sampletype('Fat','stool'); 
SELECT create_relationship_panel_test('Fat','FAT (SEMI-QUANTITATIVE)'); 
SELECT create_relationship_sample_test('stool','FAT (SEMI-QUANTITATIVE)'); 
SELECT insert_unit_of_measure('NA','FAT (SEMI-QUANTITATIVE)'); 
SELECT create_relationship_test_section_test('Stool','FAT (SEMI-QUANTITATIVE)'); 
-- Begin Line: 42 
SELECT insert_test_section('Vaginal'); 
SELECT insert_sample_type('vaginal'); 
SELECT create_relationship_panel_sampletype('Vaginal Panel','vaginal'); 
SELECT create_relationship_panel_test('Vaginal Panel','Microscopy of Saline mount'); 
SELECT create_relationship_sample_test('vaginal','Microscopy of Saline mount'); 
SELECT insert_unit_of_measure('NA','Microscopy of Saline mount'); 
SELECT create_relationship_test_section_test('Vaginal','Microscopy of Saline mount'); 
-- Begin Line: 43 
SELECT insert_test_section('Vaginal'); 
SELECT insert_sample_type('vaginal'); 
SELECT create_relationship_panel_sampletype('Vaginal Panel','vaginal'); 
SELECT create_relationship_panel_test('Vaginal Panel','Microscopy of KOH mount'); 
SELECT create_relationship_sample_test('vaginal','Microscopy of KOH mount'); 
SELECT insert_unit_of_measure('NA','Microscopy of KOH mount'); 
SELECT create_relationship_test_section_test('Vaginal','Microscopy of KOH mount'); 
-- Begin Line: 44 
SELECT insert_test_section('Vaginal'); 
SELECT insert_sample_type('vaginal'); 
SELECT create_relationship_panel_sampletype('Vaginal Panel','vaginal'); 
SELECT create_relationship_panel_test('Vaginal Panel','Gram Stain'); 
SELECT create_relationship_sample_test('vaginal','Gram Stain'); 
SELECT insert_unit_of_measure('NA','Gram Stain'); 
SELECT create_relationship_test_section_test('Vaginal','Gram Stain'); 
-- Begin Line: 45 
SELECT insert_test_section('Vaginal'); 
SELECT insert_sample_type('vaginal'); 
SELECT create_relationship_panel_sampletype('Vaginal Panel','vaginal'); 
SELECT create_relationship_panel_test('Vaginal Panel','pH'); 
SELECT create_relationship_sample_test('vaginal','pH'); 
SELECT insert_unit_of_measure('NA','pH'); 
SELECT create_relationship_test_section_test('Vaginal','pH'); 
SELECT insert_result_limit_valid_range('pH',1,14); 
-- Begin Line: 46 
SELECT insert_test_section('Vaginal'); 
SELECT insert_sample_type('vaginal'); 
SELECT create_relationship_panel_sampletype('Vaginal Panel','vaginal'); 
SELECT create_relationship_panel_test('Vaginal Panel','Whiff test'); 
SELECT create_relationship_sample_test('vaginal','Whiff test'); 
SELECT insert_unit_of_measure('NA','Whiff test'); 
SELECT create_relationship_test_section_test('Vaginal','Whiff test'); 
-- Begin Line: 47 
SELECT insert_test_section('Vaginal'); 
SELECT insert_sample_type('vaginal'); 
SELECT create_relationship_panel_sampletype('Vaginal Panel','vaginal'); 
SELECT create_relationship_panel_test('Vaginal Panel','LE'); 
SELECT create_relationship_sample_test('vaginal','LE'); 
SELECT insert_unit_of_measure('NA','LE'); 
SELECT create_relationship_test_section_test('Vaginal','LE'); 
-- Begin Line: 48 
SELECT insert_test_section('cerebrospinal fluid'); 
SELECT insert_sample_type('csf'); 
SELECT create_relationship_panel_sampletype('CSF','csf'); 
SELECT create_relationship_panel_test('CSF','TLC-'); 
SELECT create_relationship_sample_test('csf','TLC-'); 
SELECT insert_unit_of_measure('CUMM','TLC-'); 
SELECT create_relationship_test_section_test('cerebrospinal fluid','TLC-'); 
SELECT insert_result_limit_normal_range('TLC-',0,5); 
-- Begin Line: 49 
SELECT insert_test_section('cerebrospinal fluid'); 
SELECT insert_sample_type('csf'); 
SELECT create_relationship_panel_sampletype('CSF','csf'); 
SELECT create_relationship_panel_test('CSF','DLC'); 
SELECT create_relationship_sample_test('csf','DLC'); 
SELECT insert_unit_of_measure('','DLC'); 
SELECT create_relationship_test_section_test('cerebrospinal fluid','DLC'); 
-- Begin Line: 50 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('biochemestry','serum'); 
SELECT create_relationship_panel_test('biochemestry','RBS'); 
SELECT create_relationship_sample_test('serum','RBS'); 
SELECT insert_unit_of_measure('mg/dl','RBS'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','RBS'); 
SELECT insert_result_limit_normal_range('RBS',60,160); 
SELECT insert_result_limit_valid_range('RBS',10,1000); 
-- Begin Line: 51 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('biochemestry','serum'); 
SELECT create_relationship_panel_test('biochemestry','FBS'); 
SELECT create_relationship_sample_test('serum','FBS'); 
SELECT insert_unit_of_measure('mg/dl','FBS'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','FBS'); 
SELECT insert_result_limit_normal_range('FBS',60,110); 
SELECT insert_result_limit_valid_range('FBS',10,1000); 
-- Begin Line: 52 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('biochemestry','serum'); 
SELECT create_relationship_panel_test('biochemestry','PP2BS'); 
SELECT create_relationship_sample_test('serum','PP2BS'); 
SELECT insert_unit_of_measure('mg/dl','PP2BS'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','PP2BS'); 
SELECT insert_result_limit_normal_range('PP2BS',120,150); 
-- Begin Line: 53 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('biochemestry','serum'); 
SELECT create_relationship_panel_test('biochemestry','BLOOD UREA'); 
SELECT create_relationship_sample_test('serum','BLOOD UREA'); 
SELECT insert_unit_of_measure('ng/dl','BLOOD UREA'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','BLOOD UREA'); 
SELECT insert_result_limit_normal_range('BLOOD UREA',20,40); 
SELECT insert_result_limit_valid_range('BLOOD UREA',10,1000); 
-- Begin Line: 54 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('biochemestry','serum'); 
SELECT create_relationship_panel_test('biochemestry','CREATNINE'); 
SELECT create_relationship_sample_test('serum','CREATNINE'); 
SELECT insert_unit_of_measure('mg/dl','CREATNINE'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','CREATNINE'); 
SELECT insert_result_limit_normal_range('CREATNINE',0.60,1.20); 
SELECT insert_result_limit_valid_range('CREATNINE',0.20,30); 
-- Begin Line: 55 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('biochemestry','serum'); 
SELECT create_relationship_panel_test('biochemestry','total bilirubin'); 
SELECT create_relationship_sample_test('serum','total bilirubin'); 
SELECT insert_unit_of_measure('mg/dl','total bilirubin'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','total bilirubin'); 
SELECT insert_result_limit_normal_range('total bilirubin',0.30,1.20); 
SELECT insert_result_limit_valid_range('total bilirubin',0.20,50); 
-- Begin Line: 56 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('biochemestry','serum'); 
SELECT create_relationship_panel_test('biochemestry','direct bilirubin'); 
SELECT create_relationship_sample_test('serum','direct bilirubin'); 
SELECT insert_unit_of_measure('mg/dl','direct bilirubin'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','direct bilirubin'); 
SELECT insert_result_limit_normal_range('direct bilirubin',0.30,0.60); 
SELECT insert_result_limit_valid_range('direct bilirubin',0.20,30); 
-- Begin Line: 57 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('biochemestry','serum'); 
SELECT create_relationship_panel_test('biochemestry','S.ALT'); 
SELECT create_relationship_sample_test('serum','S.ALT'); 
SELECT insert_unit_of_measure('mg/dl','S.ALT'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','S.ALT'); 
SELECT insert_result_limit_normal_range('S.ALT',5,35); 
SELECT insert_result_limit_valid_range('S.ALT',3,2000); 
-- Begin Line: 58 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('biochemestry','serum'); 
SELECT create_relationship_panel_test('biochemestry','S.AST'); 
SELECT create_relationship_sample_test('serum','S.AST'); 
SELECT insert_unit_of_measure('u/L','S.AST'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','S.AST'); 
SELECT insert_result_limit_normal_range('S.AST',5,35); 
SELECT insert_result_limit_valid_range('S.AST',3,3000); 
-- Begin Line: 59 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('biochemestry','serum'); 
SELECT create_relationship_panel_test('biochemestry','ALK.PHOSPHATE'); 
SELECT create_relationship_sample_test('serum','ALK.PHOSPHATE'); 
SELECT insert_unit_of_measure('u/L','ALK.PHOSPHATE'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','ALK.PHOSPHATE'); 
-- Begin Line: 60 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('biochemestry','serum'); 
SELECT create_relationship_panel_test('biochemestry','URIC ACID'); 
SELECT create_relationship_sample_test('serum','URIC ACID'); 
SELECT insert_unit_of_measure('mg/dl','URIC ACID'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','URIC ACID'); 
SELECT insert_result_limit_normal_range('URIC ACID',3.50,7.20); 
SELECT insert_result_limit_valid_range('URIC ACID',2,30); 
-- Begin Line: 61 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('biochemestry','serum'); 
SELECT create_relationship_panel_test('biochemestry','S.PROTEIN'); 
SELECT create_relationship_sample_test('serum','S.PROTEIN'); 
SELECT insert_unit_of_measure('gm/dl','S.PROTEIN'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','S.PROTEIN'); 
SELECT insert_result_limit_normal_range('S.PROTEIN',6.50,8.50); 
SELECT insert_result_limit_valid_range('S.PROTEIN',1.50,15); 
-- Begin Line: 62 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('biochemestry','serum'); 
SELECT create_relationship_panel_test('biochemestry','CHOLESTROL'); 
SELECT create_relationship_sample_test('serum','CHOLESTROL'); 
SELECT insert_unit_of_measure('mg/dl','CHOLESTROL'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','CHOLESTROL'); 
-- Begin Line: 63 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('biochemestry','serum'); 
SELECT create_relationship_panel_test('biochemestry','S.TRIGLYCERIDE'); 
SELECT create_relationship_sample_test('serum','S.TRIGLYCERIDE'); 
SELECT insert_unit_of_measure('mg/dl','S.TRIGLYCERIDE'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','S.TRIGLYCERIDE'); 
SELECT insert_result_limit_normal_range('S.TRIGLYCERIDE',40,150); 
SELECT insert_result_limit_valid_range('S.TRIGLYCERIDE',20,700); 
-- Begin Line: 64 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('biochemestry','serum'); 
SELECT create_relationship_panel_test('biochemestry','H.D.L.'); 
SELECT create_relationship_sample_test('serum','H.D.L.'); 
SELECT insert_unit_of_measure('mg/dl','H.D.L.'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','H.D.L.'); 
SELECT insert_result_limit_valid_range('H.D.L.',5,300); 
-- Begin Line: 65 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('biochemestry','serum'); 
SELECT create_relationship_panel_test('biochemestry','L.D.H.'); 
SELECT create_relationship_sample_test('serum','L.D.H.'); 
SELECT insert_unit_of_measure('u/L','L.D.H.'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','L.D.H.'); 
SELECT insert_result_limit_normal_range('L.D.H.',45,90); 
SELECT insert_result_limit_valid_range('L.D.H.',20,700); 
-- Begin Line: 66 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('biochemestry','serum'); 
SELECT create_relationship_panel_test('biochemestry','L.D.L.'); 
SELECT create_relationship_sample_test('serum','L.D.L.'); 
SELECT insert_unit_of_measure('mg/dl','L.D.L.'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','L.D.L.'); 
-- Begin Line: 67 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('biochemestry','serum'); 
SELECT create_relationship_panel_test('biochemestry','L.H.'); 
SELECT create_relationship_sample_test('serum','L.H.'); 
SELECT insert_unit_of_measure('Iu/L','L.H.'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','L.H.'); 
SELECT insert_result_limit_normal_range('L.H.',1,9); 
SELECT insert_result_limit_valid_range('L.H.',0,50); 
-- Begin Line: 68 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('biochemestry','serum'); 
SELECT create_relationship_panel_test('biochemestry','magnesium'); 
SELECT create_relationship_sample_test('serum','magnesium'); 
SELECT insert_unit_of_measure('mg/dl','magnesium'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','magnesium'); 
SELECT insert_result_limit_normal_range('magnesium',1.30,2.10); 
SELECT insert_result_limit_valid_range('magnesium',0.20,10); 
-- Begin Line: 69 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('biochemestry','serum'); 
SELECT create_relationship_panel_test('biochemestry','PHOSPHATE'); 
SELECT create_relationship_sample_test('serum','PHOSPHATE'); 
SELECT insert_unit_of_measure('mg/dl','PHOSPHATE'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','PHOSPHATE'); 
SELECT insert_result_limit_normal_range('PHOSPHATE',3,4.50); 
SELECT insert_result_limit_valid_range('PHOSPHATE',1,14); 
-- Begin Line: 70 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('biochemestry','serum'); 
SELECT create_relationship_panel_test('biochemestry','POTASSIUM'); 
SELECT create_relationship_sample_test('serum','POTASSIUM'); 
SELECT insert_unit_of_measure('me/ql','POTASSIUM'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','POTASSIUM'); 
SELECT insert_result_limit_normal_range('POTASSIUM',3.50,5.10); 
SELECT insert_result_limit_valid_range('POTASSIUM',0.50,12); 
-- Begin Line: 71 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('biochemestry','serum'); 
SELECT create_relationship_panel_test('biochemestry','PROLECTIN'); 
SELECT create_relationship_sample_test('serum','PROLECTIN'); 
SELECT insert_unit_of_measure('nd/ml','PROLECTIN'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','PROLECTIN'); 
-- Begin Line: 72 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('biochemestry','serum'); 
SELECT create_relationship_panel_test('biochemestry','Na'); 
SELECT create_relationship_sample_test('serum','Na'); 
SELECT insert_unit_of_measure('me/ql','Na'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','Na'); 
SELECT insert_result_limit_normal_range('Na',135,145); 
SELECT insert_result_limit_valid_range('Na',60,250); 
-- Begin Line: 73 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('biochemestry','serum'); 
SELECT create_relationship_panel_test('biochemestry','AMYLASE'); 
SELECT create_relationship_sample_test('serum','AMYLASE'); 
SELECT insert_unit_of_measure('u/L','AMYLASE'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','AMYLASE'); 
SELECT insert_result_limit_normal_range('AMYLASE',25,125); 
SELECT insert_result_limit_valid_range('AMYLASE',10,2000); 
-- Begin Line: 74 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('biochemestry','serum'); 
SELECT create_relationship_panel_test('biochemestry','BICARBONATE'); 
SELECT create_relationship_sample_test('serum','BICARBONATE'); 
SELECT insert_unit_of_measure('me/ql','BICARBONATE'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','BICARBONATE'); 
SELECT insert_result_limit_normal_range('BICARBONATE',23,29); 
SELECT insert_result_limit_valid_range('BICARBONATE',10,60); 
-- Begin Line: 75 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('biochemestry','serum'); 
SELECT create_relationship_panel_test('biochemestry','Ca'); 
SELECT create_relationship_sample_test('serum','Ca'); 
SELECT insert_unit_of_measure('mg/dl','Ca'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','Ca'); 
SELECT insert_result_limit_normal_range('Ca',8.40,10.60); 
SELECT insert_result_limit_valid_range('Ca',2,22); 
-- Begin Line: 76 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('biochemestry','serum'); 
SELECT create_relationship_panel_test('biochemestry','CREATNINE KINASE'); 
SELECT create_relationship_sample_test('serum','CREATNINE KINASE'); 
SELECT insert_unit_of_measure('u/L','CREATNINE KINASE'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','CREATNINE KINASE'); 
SELECT insert_result_limit_normal_range('CREATNINE KINASE',20,115); 
SELECT insert_result_limit_valid_range('CREATNINE KINASE',5,500); 
-- Begin Line: 77 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('biochemestry','serum'); 
SELECT create_relationship_panel_test('biochemestry','FERITTIN'); 
SELECT create_relationship_sample_test('serum','FERITTIN'); 
SELECT insert_unit_of_measure('ng/ml','FERITTIN'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','FERITTIN'); 
SELECT insert_result_limit_normal_range('FERITTIN',20,200); 
SELECT insert_result_limit_valid_range('FERITTIN',5,500); 
-- Begin Line: 78 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('serology','serum'); 
SELECT create_relationship_panel_test('serology','T3'); 
SELECT create_relationship_sample_test('serum','T3'); 
SELECT insert_unit_of_measure('ng/dl','T3'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','T3'); 
SELECT insert_result_limit_normal_range('T3',60,200); 
SELECT insert_result_limit_valid_range('T3',10,600); 
-- Begin Line: 79 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('serology','serum'); 
SELECT create_relationship_panel_test('serology','T4'); 
SELECT create_relationship_sample_test('serum','T4'); 
SELECT insert_unit_of_measure('ug/dl','T4'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','T4'); 
SELECT insert_result_limit_normal_range('T4',4.50,12); 
SELECT insert_result_limit_valid_range('T4',1,50); 
-- Begin Line: 80 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('serology','serum'); 
SELECT create_relationship_panel_test('serology','T.S.H.'); 
SELECT create_relationship_sample_test('serum','T.S.H.'); 
SELECT insert_unit_of_measure('Iu/ml','T.S.H.'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','T.S.H.'); 
SELECT insert_result_limit_normal_range('T.S.H.',0.30,5.50); 
SELECT insert_result_limit_valid_range('T.S.H.',0.10,30); 
-- Begin Line: 81 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('biochemestry','serum'); 
SELECT create_relationship_panel_test('biochemestry','UREA NITROGEN'); 
SELECT create_relationship_sample_test('serum','UREA NITROGEN'); 
SELECT insert_unit_of_measure('mg/dl','UREA NITROGEN'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','UREA NITROGEN'); 
SELECT insert_result_limit_normal_range('UREA NITROGEN',40,150); 
SELECT insert_result_limit_valid_range('UREA NITROGEN',10,1000); 
-- Begin Line: 82 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('serology','serum'); 
SELECT create_relationship_panel_test('serology','FT3'); 
SELECT create_relationship_sample_test('serum','FT3'); 
SELECT insert_unit_of_measure('pg/ml','FT3'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','FT3'); 
SELECT insert_result_limit_normal_range('FT3',1.70,4.20); 
SELECT insert_result_limit_valid_range('FT3',0.50,20); 
-- Begin Line: 83 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('serology','serum'); 
SELECT create_relationship_panel_test('serology','FT4'); 
SELECT create_relationship_sample_test('serum','FT4'); 
SELECT insert_unit_of_measure('ng/dl','FT4'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','FT4'); 
SELECT insert_result_limit_normal_range('FT4',0.70,1.80); 
SELECT insert_result_limit_valid_range('FT4',0,20); 
-- Begin Line: 84 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('serology','serum'); 
SELECT create_relationship_panel_test('serology','ANA'); 
SELECT create_relationship_sample_test('serum','ANA'); 
SELECT insert_unit_of_measure('od ratio','ANA'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','ANA'); 
-- Begin Line: 85 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('serology','serum'); 
SELECT create_relationship_panel_test('serology','PSA'); 
SELECT create_relationship_sample_test('serum','PSA'); 
SELECT insert_unit_of_measure('ng/ml','PSA'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','PSA'); 
SELECT insert_result_limit_valid_range('PSA',0.10,50); 
-- Begin Line: 86 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('serology','serum'); 
SELECT create_relationship_panel_test('serology','CA-125'); 
SELECT create_relationship_sample_test('serum','CA-125'); 
SELECT insert_unit_of_measure('u/mm','CA-125'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','CA-125'); 
SELECT insert_result_limit_valid_range('CA-125',5,200); 
-- Begin Line: 87 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('serology','serum'); 
SELECT create_relationship_panel_test('serology','CA19-9'); 
SELECT create_relationship_sample_test('serum','CA19-9'); 
SELECT insert_unit_of_measure('u/mm','CA19-9'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','CA19-9'); 
-- Begin Line: 88 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('serology','serum'); 
SELECT create_relationship_panel_test('serology','B HCG'); 
SELECT create_relationship_sample_test('serum','B HCG'); 
SELECT insert_unit_of_measure('U/MM','B HCG'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','B HCG'); 
-- Begin Line: 89 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('haematology','Blood (EDTA)'); 
SELECT create_relationship_panel_test('haematology','HPLC'); 
SELECT create_relationship_sample_test('Blood (EDTA)','HPLC'); 
SELECT insert_unit_of_measure('NA','HPLC'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','HPLC'); 
