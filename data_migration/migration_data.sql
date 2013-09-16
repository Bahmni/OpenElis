-- This file is generated automatically. If you need to change it, change the spreedsheet and run the script create_migration_data.pl 


-- Begin Line: 2 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Routine Blood','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Routine Blood','Absolute Eosinphil Count'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Absolute Eosinphil Count'); 
SELECT insert_unit_of_measure('cumm','Absolute Eosinphil Count'); 
SELECT create_relationship_test_section_test('Haematology','Absolute Eosinphil Count'); 
SELECT insert_result_limit_normal_range('Absolute Eosinphil Count',100,600); 
SELECT insert_result_limit_valid_range('Absolute Eosinphil Count',100,30000); 
-- Begin Line: 3 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Anaemia Panel','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Anaemia Panel','Absolute Eosinphil Count'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Absolute Eosinphil Count'); 
SELECT insert_unit_of_measure('cumm','Absolute Eosinphil Count'); 
SELECT create_relationship_test_section_test('Haematology','Absolute Eosinphil Count'); 
SELECT insert_result_limit_normal_range('Absolute Eosinphil Count',100,600); 
SELECT insert_result_limit_valid_range('Absolute Eosinphil Count',25,30000); 
-- Begin Line: 4 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Liver Function Test','serum'); 
SELECT create_relationship_panel_test('Liver Function Test','ALK. Phosphate'); 
SELECT create_relationship_sample_test('serum','ALK. Phosphate'); 
SELECT insert_unit_of_measure('u/L','ALK. Phosphate'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','ALK. Phosphate'); 
SELECT insert_result_limit_normal_range('ALK. Phosphate',0,135); 
SELECT insert_result_limit_valid_range('ALK. Phosphate',0,2000); 
-- Begin Line: 5 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Liver Function Test','serum'); 
SELECT create_relationship_panel_test('Liver Function Test','Amylase'); 
SELECT create_relationship_sample_test('serum','Amylase'); 
SELECT insert_unit_of_measure('u/L','Amylase'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','Amylase'); 
SELECT insert_result_limit_normal_range('Amylase',25,125); 
SELECT insert_result_limit_valid_range('Amylase',10,2000); 
-- Begin Line: 6 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Bio Chemistry','serum'); 
SELECT create_relationship_panel_test('Bio Chemistry','ANA'); 
SELECT create_relationship_sample_test('serum','ANA'); 
SELECT insert_unit_of_measure('od ratio','ANA'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','ANA'); 
SELECT insert_result_limit_normal_range('ANA',0,1); 
SELECT insert_result_limit_valid_range('ANA',0,1); 
-- Begin Line: 7 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Serology','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Serology','Anti Streptolysin Turbidometry'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Anti Streptolysin Turbidometry'); 
SELECT insert_unit_of_measure('iU/ml','Anti Streptolysin Turbidometry'); 
SELECT create_relationship_test_section_test('Serology','Anti Streptolysin Turbidometry'); 
SELECT insert_result_limit_normal_range('Anti Streptolysin Turbidometry',5,200); 
-- Begin Line: 8 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Coagulation Factor','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Coagulation Factor','APTT (Control)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','APTT (Control)'); 
SELECT insert_unit_of_measure('sec','APTT (Control)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','APTT (Control)'); 
SELECT insert_result_limit_normal_range('APTT (Control)',38,40); 
SELECT insert_result_limit_valid_range('APTT (Control)',20,720); 
-- Begin Line: 9 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Coagulation Factor','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Coagulation Factor','APTT (Test)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','APTT (Test)'); 
SELECT insert_unit_of_measure('sec','APTT (Test)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','APTT (Test)'); 
SELECT insert_result_limit_normal_range('APTT (Test)',38,40); 
SELECT insert_result_limit_valid_range('APTT (Test)',20,720); 
-- Begin Line: 10 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Bio Chemistry','serum'); 
SELECT create_relationship_panel_test('Bio Chemistry','B HCG'); 
SELECT create_relationship_sample_test('serum','B HCG'); 
SELECT insert_unit_of_measure('U/MM','B HCG'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','B HCG'); 
-- Begin Line: 11 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Differential Count','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Differential Count','Basophil'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Basophil'); 
SELECT insert_unit_of_measure('%','Basophil'); 
SELECT create_relationship_test_section_test('Haematology','Basophil'); 
SELECT insert_result_limit_normal_range('Basophil',0,1); 
SELECT insert_result_limit_valid_range('Basophil',0,2); 
-- Begin Line: 12 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Bio Chemistry','serum'); 
SELECT create_relationship_panel_test('Bio Chemistry','Bicarbonate'); 
SELECT create_relationship_sample_test('serum','Bicarbonate'); 
SELECT insert_unit_of_measure('me/ql','Bicarbonate'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','Bicarbonate'); 
SELECT insert_result_limit_normal_range('Bicarbonate',23,29); 
SELECT insert_result_limit_valid_range('Bicarbonate',10,60); 
-- Begin Line: 13 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Coagulation Factor','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Coagulation Factor','Bleeding Time'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Bleeding Time'); 
SELECT insert_unit_of_measure('sec','Bleeding Time'); 
SELECT create_relationship_test_section_test('Haematology','Bleeding Time'); 
-- Begin Line: 14 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Bio Chemistry','serum'); 
SELECT create_relationship_panel_test('Bio Chemistry','Blood Urea'); 
SELECT create_relationship_sample_test('serum','Blood Urea'); 
SELECT insert_unit_of_measure('ng/dl','Blood Urea'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','Blood Urea'); 
SELECT insert_result_limit_normal_range('Blood Urea',20,40); 
SELECT insert_result_limit_valid_range('Blood Urea',10,1000); 
-- Begin Line: 15 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Bio Chemistry','serum'); 
SELECT create_relationship_panel_test('Bio Chemistry','CA-125'); 
SELECT create_relationship_sample_test('serum','CA-125'); 
SELECT insert_unit_of_measure('u/mm','CA-125'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','CA-125'); 
SELECT insert_result_limit_normal_range('CA-125',5,35); 
SELECT insert_result_limit_valid_range('CA-125',5,200); 
-- Begin Line: 16 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Bio Chemistry','serum'); 
SELECT create_relationship_panel_test('Bio Chemistry','CA19-9'); 
SELECT create_relationship_sample_test('serum','CA19-9'); 
SELECT insert_unit_of_measure('u/mm','CA19-9'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','CA19-9'); 
SELECT add_test_result_type('CA19-9','R'); 
-- Begin Line: 17 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Electrolyte','serum'); 
SELECT create_relationship_panel_test('Electrolyte','Calcium'); 
SELECT create_relationship_sample_test('serum','Calcium'); 
SELECT insert_unit_of_measure('mg/dl','Calcium'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','Calcium'); 
SELECT insert_result_limit_normal_range('Calcium',8.40,10.60); 
SELECT insert_result_limit_valid_range('Calcium',2,22); 
-- Begin Line: 18 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Urine Microscopy','Urine'); 
SELECT create_relationship_panel_test('Urine Microscopy','Cast'); 
SELECT create_relationship_sample_test('Urine','Cast'); 
SELECT insert_unit_of_measure('/hPF','Cast'); 
SELECT create_relationship_test_section_test('Urine','Cast'); 
SELECT add_test_result_type('Cast','R'); 
-- Begin Line: 19 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Lipid Profile','serum'); 
SELECT create_relationship_panel_test('Lipid Profile','Cholesterol'); 
SELECT create_relationship_sample_test('serum','Cholesterol'); 
SELECT insert_unit_of_measure('mg/dl','Cholesterol'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','Cholesterol'); 
SELECT insert_result_limit_normal_range('Cholesterol',150,200); 
SELECT insert_result_limit_valid_range('Cholesterol',50,600); 
-- Begin Line: 20 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Coagulation Factor','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Coagulation Factor','Clotting Time'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Clotting Time'); 
SELECT insert_unit_of_measure('sec','Clotting Time'); 
SELECT create_relationship_test_section_test('Haematology','Clotting Time'); 
-- Begin Line: 21 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Kinyoun''s Stain','Stool'); 
SELECT create_relationship_panel_test('Kinyoun''s Stain','Coccidians'); 
SELECT create_relationship_sample_test('Stool','Coccidians'); 
SELECT insert_unit_of_measure('NA','Coccidians'); 
SELECT create_relationship_test_section_test('Stool','Coccidians'); 
SELECT add_test_result_type('Coccidians','R'); 
-- Begin Line: 22 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Stool Microscopy w/o Concetration','Stool'); 
SELECT create_relationship_panel_test('Stool Microscopy w/o Concetration','Color'); 
SELECT create_relationship_sample_test('Stool','Color'); 
SELECT insert_unit_of_measure('NA','Color'); 
SELECT create_relationship_test_section_test('Stool','Color'); 
SELECT add_test_result_type('Color','R'); 
-- Begin Line: 23 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Stool Microscopy with Concetration','Stool'); 
SELECT create_relationship_panel_test('Stool Microscopy with Concetration','Color'); 
SELECT create_relationship_sample_test('Stool','Color'); 
SELECT insert_unit_of_measure('NA','Color'); 
SELECT create_relationship_test_section_test('Stool','Color'); 
SELECT add_test_result_type('Color','R'); 
-- Begin Line: 24 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Semen'); 
SELECT create_relationship_panel_sampletype('Semen Analysis','Semen'); 
SELECT create_relationship_panel_test('Semen Analysis','Colour'); 
SELECT create_relationship_sample_test('Semen','Colour'); 
SELECT insert_unit_of_measure('','Colour'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Colour'); 
SELECT add_test_result_type('Colour','R'); 
-- Begin Line: 25 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Bio Chemistry','serum'); 
SELECT create_relationship_panel_test('Bio Chemistry','Creatinine'); 
SELECT create_relationship_sample_test('serum','Creatinine'); 
SELECT insert_unit_of_measure('mg/dl','Creatinine'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','Creatinine'); 
SELECT insert_result_limit_normal_range('Creatinine',0.60,1.20); 
SELECT insert_result_limit_valid_range('Creatinine',0.20,30); 
-- Begin Line: 26 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Bio Chemistry','serum'); 
SELECT create_relationship_panel_test('Bio Chemistry','Creatinine Kinase'); 
SELECT create_relationship_sample_test('serum','Creatinine Kinase'); 
SELECT insert_unit_of_measure('u/L','Creatinine Kinase'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','Creatinine Kinase'); 
SELECT insert_result_limit_normal_range('Creatinine Kinase',20,115); 
SELECT insert_result_limit_valid_range('Creatinine Kinase',5,500); 
-- Begin Line: 27 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Urine Microscopy','Urine'); 
SELECT create_relationship_panel_test('Urine Microscopy','Crystal'); 
SELECT create_relationship_sample_test('Urine','Crystal'); 
SELECT insert_unit_of_measure('/hPF','Crystal'); 
SELECT create_relationship_test_section_test('Urine','Crystal'); 
SELECT add_test_result_type('Crystal','R'); 
-- Begin Line: 28 
SELECT insert_test_section('Microbiology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('CSF','Body Fluid'); 
SELECT create_relationship_panel_test('CSF','Culture'); 
SELECT create_relationship_sample_test('Body Fluid','Culture'); 
SELECT insert_unit_of_measure('','Culture'); 
SELECT create_relationship_test_section_test('Microbiology','Culture'); 
SELECT add_test_result_type('Culture','R'); 
-- Begin Line: 29 
SELECT insert_test_section('Microbiology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Asitic Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Asitic Fluid','Culture'); 
SELECT create_relationship_sample_test('Body Fluid','Culture'); 
SELECT insert_unit_of_measure('','Culture'); 
SELECT create_relationship_test_section_test('Microbiology','Culture'); 
SELECT add_test_result_type('Culture','R'); 
-- Begin Line: 30 
SELECT insert_test_section('Microbiology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Pyrotinial Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Pyrotinial Fluid','Culture'); 
SELECT create_relationship_sample_test('Body Fluid','Culture'); 
SELECT insert_unit_of_measure('','Culture'); 
SELECT create_relationship_test_section_test('Microbiology','Culture'); 
SELECT add_test_result_type('Culture','R'); 
-- Begin Line: 31 
SELECT insert_test_section('Microbiology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Pus','Body Fluid'); 
SELECT create_relationship_panel_test('Pus','Culture'); 
SELECT create_relationship_sample_test('Body Fluid','Culture'); 
SELECT insert_unit_of_measure('','Culture'); 
SELECT create_relationship_test_section_test('Microbiology','Culture'); 
SELECT add_test_result_type('Culture','R'); 
-- Begin Line: 32 
SELECT insert_test_section('Microbiology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Knee Joint Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Knee Joint Fluid','Culture'); 
SELECT create_relationship_sample_test('Body Fluid','Culture'); 
SELECT insert_unit_of_measure('','Culture'); 
SELECT create_relationship_test_section_test('Microbiology','Culture'); 
SELECT add_test_result_type('Culture','R'); 
-- Begin Line: 33 
SELECT insert_test_section('Microbiology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Plural Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Plural Fluid','Culture'); 
SELECT create_relationship_sample_test('Body Fluid','Culture'); 
SELECT insert_unit_of_measure('','Culture'); 
SELECT create_relationship_test_section_test('Microbiology','Culture'); 
SELECT add_test_result_type('Culture','R'); 
-- Begin Line: 34 
SELECT insert_test_section('Histo Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('CSF','Body Fluid'); 
SELECT create_relationship_panel_test('CSF','Cytology'); 
SELECT create_relationship_sample_test('Body Fluid','Cytology'); 
SELECT insert_unit_of_measure('','Cytology'); 
SELECT create_relationship_test_section_test('Histo Pathology','Cytology'); 
SELECT add_test_result_type('Cytology','R'); 
-- Begin Line: 35 
SELECT insert_test_section('Histo Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Asitic Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Asitic Fluid','Cytology'); 
SELECT create_relationship_sample_test('Body Fluid','Cytology'); 
SELECT insert_unit_of_measure('','Cytology'); 
SELECT create_relationship_test_section_test('Histo Pathology','Cytology'); 
SELECT add_test_result_type('Cytology','R'); 
-- Begin Line: 36 
SELECT insert_test_section('Histo Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Pyrotinial Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Pyrotinial Fluid','Cytology'); 
SELECT create_relationship_sample_test('Body Fluid','Cytology'); 
SELECT insert_unit_of_measure('','Cytology'); 
SELECT create_relationship_test_section_test('Histo Pathology','Cytology'); 
SELECT add_test_result_type('Cytology','R'); 
-- Begin Line: 37 
SELECT insert_test_section('Histo Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Pus','Body Fluid'); 
SELECT create_relationship_panel_test('Pus','Cytology'); 
SELECT create_relationship_sample_test('Body Fluid','Cytology'); 
SELECT insert_unit_of_measure('','Cytology'); 
SELECT create_relationship_test_section_test('Histo Pathology','Cytology'); 
SELECT add_test_result_type('Cytology','R'); 
-- Begin Line: 38 
SELECT insert_test_section('Histo Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Knee Joint Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Knee Joint Fluid','Cytology'); 
SELECT create_relationship_sample_test('Body Fluid','Cytology'); 
SELECT insert_unit_of_measure('','Cytology'); 
SELECT create_relationship_test_section_test('Histo Pathology','Cytology'); 
SELECT add_test_result_type('Cytology','R'); 
-- Begin Line: 39 
SELECT insert_test_section('Histo Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Plural Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Plural Fluid','Cytology'); 
SELECT create_relationship_sample_test('Body Fluid','Cytology'); 
SELECT insert_unit_of_measure('','Cytology'); 
SELECT create_relationship_test_section_test('Histo Pathology','Cytology'); 
SELECT add_test_result_type('Cytology','R'); 
-- Begin Line: 40 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Asitic Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Asitic Fluid','Differential Count'); 
SELECT create_relationship_sample_test('Body Fluid','Differential Count'); 
SELECT insert_unit_of_measure('%','Differential Count'); 
SELECT create_relationship_test_section_test('Haematology','Differential Count'); 
-- Begin Line: 41 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Pyrotinial Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Pyrotinial Fluid','Differential Count'); 
SELECT create_relationship_sample_test('Body Fluid','Differential Count'); 
SELECT insert_unit_of_measure('%','Differential Count'); 
SELECT create_relationship_test_section_test('Haematology','Differential Count'); 
-- Begin Line: 42 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Pus','Body Fluid'); 
SELECT create_relationship_panel_test('Pus','Differential Count'); 
SELECT create_relationship_sample_test('Body Fluid','Differential Count'); 
SELECT insert_unit_of_measure('%','Differential Count'); 
SELECT create_relationship_test_section_test('Haematology','Differential Count'); 
-- Begin Line: 43 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Knee Joint Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Knee Joint Fluid','Differential Count'); 
SELECT create_relationship_sample_test('Body Fluid','Differential Count'); 
SELECT insert_unit_of_measure('%','Differential Count'); 
SELECT create_relationship_test_section_test('Haematology','Differential Count'); 
-- Begin Line: 44 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Plural Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Plural Fluid','Differential Count'); 
SELECT create_relationship_sample_test('Body Fluid','Differential Count'); 
SELECT insert_unit_of_measure('%','Differential Count'); 
SELECT create_relationship_test_section_test('Haematology','Differential Count'); 
-- Begin Line: 45 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('CSF','Body Fluid'); 
SELECT create_relationship_panel_test('CSF','Differential Count (CSF)'); 
SELECT create_relationship_sample_test('Body Fluid','Differential Count (CSF)'); 
SELECT insert_unit_of_measure('%','Differential Count (CSF)'); 
SELECT create_relationship_test_section_test('Haematology','Differential Count (CSF)'); 
-- Begin Line: 46 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Liver Function Test','serum'); 
SELECT create_relationship_panel_test('Liver Function Test','Direct Bilirubin'); 
SELECT create_relationship_sample_test('serum','Direct Bilirubin'); 
SELECT insert_unit_of_measure('mg/dl','Direct Bilirubin'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','Direct Bilirubin'); 
SELECT insert_result_limit_normal_range('Direct Bilirubin',0.30,0.60); 
SELECT insert_result_limit_valid_range('Direct Bilirubin',0.20,30); 
-- Begin Line: 47 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Differential Count','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Differential Count','Eosinophil'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Eosinophil'); 
SELECT insert_unit_of_measure('%','Eosinophil'); 
SELECT create_relationship_test_section_test('Haematology','Eosinophil'); 
SELECT insert_result_limit_normal_range('Eosinophil',1,6); 
SELECT insert_result_limit_valid_range('Eosinophil',1,70); 
-- Begin Line: 48 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Urine Microscopy','Urine'); 
SELECT create_relationship_panel_test('Urine Microscopy','Epithelial Cells'); 
SELECT create_relationship_sample_test('Urine','Epithelial Cells'); 
SELECT insert_unit_of_measure('/hPF','Epithelial Cells'); 
SELECT create_relationship_test_section_test('Urine','Epithelial Cells'); 
SELECT add_test_result_type('Epithelial Cells','R'); 
-- Begin Line: 49 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Sedimentation','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Sedimentation','ESR'); 
SELECT create_relationship_sample_test('Blood (EDTA)','ESR'); 
SELECT insert_unit_of_measure('mm/hour','ESR'); 
SELECT create_relationship_test_section_test('Haematology','ESR'); 
SELECT insert_result_limit_normal_range('ESR',5,20); 
SELECT insert_result_limit_valid_range('ESR',5,180); 
-- Begin Line: 50 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('CBC','Blood (EDTA)'); 
SELECT create_relationship_panel_test('CBC','ESR'); 
SELECT create_relationship_sample_test('Blood (EDTA)','ESR'); 
SELECT insert_unit_of_measure('mm/hour','ESR'); 
SELECT create_relationship_test_section_test('Haematology','ESR'); 
SELECT insert_result_limit_normal_range('ESR',5,20); 
SELECT insert_result_limit_valid_range('ESR',5,180); 
-- Begin Line: 51 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Stool Microscopy w/o Concetration','Stool'); 
SELECT create_relationship_panel_test('Stool Microscopy w/o Concetration','Fat Droplets'); 
SELECT create_relationship_sample_test('Stool','Fat Droplets'); 
SELECT insert_unit_of_measure('NA','Fat Droplets'); 
SELECT create_relationship_test_section_test('Stool','Fat Droplets'); 
SELECT add_test_result_type('Fat Droplets','R'); 
-- Begin Line: 52 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Stool Microscopy with Concetration','Stool'); 
SELECT create_relationship_panel_test('Stool Microscopy with Concetration','Fat Droplets'); 
SELECT create_relationship_sample_test('Stool','Fat Droplets'); 
SELECT insert_unit_of_measure('NA','Fat Droplets'); 
SELECT create_relationship_test_section_test('Stool','Fat Droplets'); 
SELECT add_test_result_type('Fat Droplets','R'); 
-- Begin Line: 53 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Bio Chemistry','serum'); 
SELECT create_relationship_panel_test('Bio Chemistry','FBS'); 
SELECT create_relationship_sample_test('serum','FBS'); 
SELECT insert_unit_of_measure('mg/dl','FBS'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','FBS'); 
SELECT insert_result_limit_normal_range('FBS',60,110); 
SELECT insert_result_limit_valid_range('FBS',10,1000); 
-- Begin Line: 54 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Bio Chemistry','serum'); 
SELECT create_relationship_panel_test('Bio Chemistry','Ferittin'); 
SELECT create_relationship_sample_test('serum','Ferittin'); 
SELECT insert_unit_of_measure('ng/ml','Ferittin'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','Ferittin'); 
SELECT insert_result_limit_normal_range('Ferittin',20,200); 
SELECT insert_result_limit_valid_range('Ferittin',5,500); 
-- Begin Line: 55 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Stool Microscopy w/o Concetration','Stool'); 
SELECT create_relationship_panel_test('Stool Microscopy w/o Concetration','Food Particles'); 
SELECT create_relationship_sample_test('Stool','Food Particles'); 
SELECT insert_unit_of_measure('NA','Food Particles'); 
SELECT create_relationship_test_section_test('Stool','Food Particles'); 
SELECT add_test_result_type('Food Particles','R'); 
-- Begin Line: 56 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Stool Microscopy with Concetration','Stool'); 
SELECT create_relationship_panel_test('Stool Microscopy with Concetration','Food Particles'); 
SELECT create_relationship_sample_test('Stool','Food Particles'); 
SELECT insert_unit_of_measure('NA','Food Particles'); 
SELECT create_relationship_test_section_test('Stool','Food Particles'); 
SELECT add_test_result_type('Food Particles','R'); 
-- Begin Line: 57 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Bio Chemistry','serum'); 
SELECT create_relationship_panel_test('Bio Chemistry','FT3'); 
SELECT create_relationship_sample_test('serum','FT3'); 
SELECT insert_unit_of_measure('pg/ml','FT3'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','FT3'); 
SELECT insert_result_limit_normal_range('FT3',1.70,4.20); 
SELECT insert_result_limit_valid_range('FT3',0.50,20); 
-- Begin Line: 58 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Bio Chemistry','serum'); 
SELECT create_relationship_panel_test('Bio Chemistry','FT4'); 
SELECT create_relationship_sample_test('serum','FT4'); 
SELECT insert_unit_of_measure('ng/dl','FT4'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','FT4'); 
SELECT insert_result_limit_normal_range('FT4',0.70,1.80); 
SELECT insert_result_limit_valid_range('FT4',0,20); 
-- Begin Line: 59 
SELECT insert_test_section('Vaginal'); 
SELECT insert_sample_type('vaginal'); 
SELECT create_relationship_panel_sampletype('Vaginal Panel','vaginal'); 
SELECT create_relationship_panel_test('Vaginal Panel','Gram Stain'); 
SELECT create_relationship_sample_test('vaginal','Gram Stain'); 
SELECT insert_unit_of_measure('NA','Gram Stain'); 
SELECT create_relationship_test_section_test('Vaginal','Gram Stain'); 
SELECT add_test_result_type('Gram Stain','R'); 
-- Begin Line: 60 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Gram Stain','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Gram Stain','Gram Stain'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Gram Stain'); 
SELECT insert_unit_of_measure('NA','Gram Stain'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Gram Stain'); 
SELECT add_test_result_type('Gram Stain','R'); 
-- Begin Line: 61 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Gram Stain','Urine'); 
SELECT create_relationship_panel_test('Gram Stain','Gram Stain'); 
SELECT create_relationship_sample_test('Urine','Gram Stain'); 
SELECT insert_unit_of_measure('NA','Gram Stain'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Gram Stain'); 
SELECT add_test_result_type('Gram Stain','R'); 
-- Begin Line: 62 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Gram Stain','Stool'); 
SELECT create_relationship_panel_test('Gram Stain','Gram Stain'); 
SELECT create_relationship_sample_test('Stool','Gram Stain'); 
SELECT insert_unit_of_measure('NA','Gram Stain'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Gram Stain'); 
SELECT add_test_result_type('Gram Stain','R'); 
-- Begin Line: 63 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Sputum'); 
SELECT create_relationship_panel_sampletype('Gram Stain','Sputum'); 
SELECT create_relationship_panel_test('Gram Stain','Gram Stain'); 
SELECT create_relationship_sample_test('Sputum','Gram Stain'); 
SELECT insert_unit_of_measure('NA','Gram Stain'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Gram Stain'); 
SELECT add_test_result_type('Gram Stain','R'); 
-- Begin Line: 64 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Gram Stain','Body Fluid'); 
SELECT create_relationship_panel_test('Gram Stain','Gram Stain'); 
SELECT create_relationship_sample_test('Body Fluid','Gram Stain'); 
SELECT insert_unit_of_measure('NA','Gram Stain'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Gram Stain'); 
SELECT add_test_result_type('Gram Stain','R'); 
-- Begin Line: 65 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Pus'); 
SELECT create_relationship_panel_sampletype('Gram Stain','Pus'); 
SELECT create_relationship_panel_test('Gram Stain','Gram Stain'); 
SELECT create_relationship_sample_test('Pus','Gram Stain'); 
SELECT insert_unit_of_measure('NA','Gram Stain'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Gram Stain'); 
SELECT add_test_result_type('Gram Stain','R'); 
-- Begin Line: 66 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Slit Skin'); 
SELECT create_relationship_panel_sampletype('Gram Stain','Slit Skin'); 
SELECT create_relationship_panel_test('Gram Stain','Gram Stain'); 
SELECT create_relationship_sample_test('Slit Skin','Gram Stain'); 
SELECT insert_unit_of_measure('NA','Gram Stain'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Gram Stain'); 
SELECT add_test_result_type('Gram Stain','R'); 
-- Begin Line: 67 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Semen'); 
SELECT create_relationship_panel_sampletype('Gram Stain','Semen'); 
SELECT create_relationship_panel_test('Gram Stain','Gram Stain'); 
SELECT create_relationship_sample_test('Semen','Gram Stain'); 
SELECT insert_unit_of_measure('NA','Gram Stain'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Gram Stain'); 
SELECT add_test_result_type('Gram Stain','R'); 
-- Begin Line: 68 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Semen'); 
SELECT create_relationship_panel_sampletype('Semen Analysis','Semen'); 
SELECT create_relationship_panel_test('Semen Analysis','Gram Stain'); 
SELECT create_relationship_sample_test('Semen','Gram Stain'); 
SELECT insert_unit_of_measure('','Gram Stain'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Gram Stain'); 
SELECT add_test_result_type('Gram Stain','R'); 
-- Begin Line: 69 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('CSF','Body Fluid'); 
SELECT create_relationship_panel_test('CSF','Gram Stain'); 
SELECT create_relationship_sample_test('Body Fluid','Gram Stain'); 
SELECT insert_unit_of_measure('','Gram Stain'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Gram Stain'); 
SELECT add_test_result_type('Gram Stain','R'); 
-- Begin Line: 70 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Asitic Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Asitic Fluid','Gram Stain'); 
SELECT create_relationship_sample_test('Body Fluid','Gram Stain'); 
SELECT insert_unit_of_measure('','Gram Stain'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Gram Stain'); 
SELECT add_test_result_type('Gram Stain','R'); 
-- Begin Line: 71 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Pyrotinial Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Pyrotinial Fluid','Gram Stain'); 
SELECT create_relationship_sample_test('Body Fluid','Gram Stain'); 
SELECT insert_unit_of_measure('','Gram Stain'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Gram Stain'); 
SELECT add_test_result_type('Gram Stain','R'); 
-- Begin Line: 72 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Pus','Body Fluid'); 
SELECT create_relationship_panel_test('Pus','Gram Stain'); 
SELECT create_relationship_sample_test('Body Fluid','Gram Stain'); 
SELECT insert_unit_of_measure('','Gram Stain'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Gram Stain'); 
SELECT add_test_result_type('Gram Stain','R'); 
-- Begin Line: 73 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Knee Joint Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Knee Joint Fluid','Gram Stain'); 
SELECT create_relationship_sample_test('Body Fluid','Gram Stain'); 
SELECT insert_unit_of_measure('','Gram Stain'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Gram Stain'); 
SELECT add_test_result_type('Gram Stain','R'); 
-- Begin Line: 74 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Plural Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Plural Fluid','Gram Stain'); 
SELECT create_relationship_sample_test('Body Fluid','Gram Stain'); 
SELECT insert_unit_of_measure('','Gram Stain'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Gram Stain'); 
SELECT add_test_result_type('Gram Stain','R'); 
-- Begin Line: 75 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Hb Only','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Hb Only','Haemoglobin'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Haemoglobin'); 
SELECT insert_unit_of_measure('gm/dl','Haemoglobin'); 
SELECT create_relationship_test_section_test('Haematology','Haemoglobin'); 
SELECT insert_result_limit_normal_range('Haemoglobin',12,16); 
SELECT insert_result_limit_valid_range('Haemoglobin',1,22); 
-- Begin Line: 76 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Anaemia Panel','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Anaemia Panel','Haemoglobin'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Haemoglobin'); 
SELECT insert_unit_of_measure('gm/dl','Haemoglobin'); 
SELECT create_relationship_test_section_test('Haematology','Haemoglobin'); 
SELECT insert_result_limit_normal_range('Haemoglobin',11.50,15.50); 
SELECT insert_result_limit_valid_range('Haemoglobin',1.50,24); 
-- Begin Line: 77 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('CBC','Blood (EDTA)'); 
SELECT create_relationship_panel_test('CBC','Haemoglobin'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Haemoglobin'); 
SELECT insert_unit_of_measure('gm/dl','Haemoglobin'); 
SELECT create_relationship_test_section_test('Haematology','Haemoglobin'); 
SELECT insert_result_limit_normal_range('Haemoglobin',12,16); 
SELECT insert_result_limit_valid_range('Haemoglobin',1,22); 
-- Begin Line: 78 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Cross Match','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Cross Match','Haemoglobin'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Haemoglobin'); 
SELECT insert_unit_of_measure('gm/dl','Haemoglobin'); 
SELECT create_relationship_test_section_test('Haematology','Haemoglobin'); 
SELECT insert_result_limit_normal_range('Haemoglobin',11.50,15.50); 
SELECT insert_result_limit_valid_range('Haemoglobin',1.50,24); 
-- Begin Line: 79 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Cross Match','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Cross Match','Haemoglobin (Relative)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Haemoglobin (Relative)'); 
SELECT insert_unit_of_measure('gm/dl','Haemoglobin (Relative)'); 
SELECT create_relationship_test_section_test('Haematology','Haemoglobin (Relative)'); 
SELECT insert_result_limit_normal_range('Haemoglobin (Relative)',11.50,15.50); 
SELECT insert_result_limit_valid_range('Haemoglobin (Relative)',1.50,24); 
-- Begin Line: 80 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Lipid Profile','serum'); 
SELECT create_relationship_panel_test('Lipid Profile','HDL'); 
SELECT create_relationship_sample_test('serum','HDL'); 
SELECT insert_unit_of_measure('mg/dl','HDL'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','HDL'); 
SELECT insert_result_limit_normal_range('HDL',35,60); 
SELECT insert_result_limit_valid_range('HDL',5,300); 
-- Begin Line: 81 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('haematology','Blood (EDTA)'); 
SELECT create_relationship_panel_test('haematology','HPLC'); 
SELECT create_relationship_sample_test('Blood (EDTA)','HPLC'); 
SELECT insert_unit_of_measure('NA','HPLC'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','HPLC'); 
SELECT add_test_result_type('HPLC','R'); 
-- Begin Line: 82 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Coagulation Factor','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Coagulation Factor','INR Ratio'); 
SELECT create_relationship_sample_test('Blood (EDTA)','INR Ratio'); 
SELECT insert_unit_of_measure('ratio','INR Ratio'); 
SELECT create_relationship_test_section_test('Clinical Pathology','INR Ratio'); 
SELECT insert_result_limit_normal_range('INR Ratio',0.90,1.10); 
SELECT insert_result_limit_valid_range('INR Ratio',0.50,12); 
-- Begin Line: 83 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Bio Chemistry','serum'); 
SELECT create_relationship_panel_test('Bio Chemistry','LDH'); 
SELECT create_relationship_sample_test('serum','LDH'); 
SELECT insert_unit_of_measure('u/L','LDH'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','LDH'); 
SELECT insert_result_limit_normal_range('LDH',45,90); 
SELECT insert_result_limit_valid_range('LDH',20,700); 
-- Begin Line: 84 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Lipid Profile','serum'); 
SELECT create_relationship_panel_test('Lipid Profile','LDL'); 
SELECT create_relationship_sample_test('serum','LDL'); 
SELECT insert_unit_of_measure('mg/dl','LDL'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','LDL'); 
SELECT insert_result_limit_normal_range('LDL',85,130); 
SELECT insert_result_limit_valid_range('LDL',20,700); 
-- Begin Line: 85 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Urine LE/Nitrite','Urine'); 
SELECT create_relationship_panel_test('Urine LE/Nitrite','LE'); 
SELECT create_relationship_sample_test('Urine','LE'); 
SELECT insert_unit_of_measure('NA','LE'); 
SELECT create_relationship_test_section_test('Urine','LE'); 
SELECT add_test_result_type('LE','R'); 
-- Begin Line: 86 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Bio Chemistry','serum'); 
SELECT create_relationship_panel_test('Bio Chemistry','LH'); 
SELECT create_relationship_sample_test('serum','LH'); 
SELECT insert_unit_of_measure('Iu/L','LH'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','LH'); 
SELECT insert_result_limit_normal_range('LH',1,9); 
SELECT insert_result_limit_valid_range('LH',0,50); 
-- Begin Line: 87 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Semen'); 
SELECT create_relationship_panel_sampletype('Semen Analysis','Semen'); 
SELECT create_relationship_panel_test('Semen Analysis','Liquification time'); 
SELECT create_relationship_sample_test('Semen','Liquification time'); 
SELECT insert_unit_of_measure('min','Liquification time'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Liquification time'); 
SELECT insert_result_limit_normal_range('Liquification time',20,30); 
-- Begin Line: 88 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Differential Count','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Differential Count','Lymph'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Lymph'); 
SELECT insert_unit_of_measure('%','Lymph'); 
SELECT create_relationship_test_section_test('Haematology','Lymph'); 
SELECT insert_result_limit_normal_range('Lymph',20,40); 
SELECT insert_result_limit_valid_range('Lymph',10,50); 
-- Begin Line: 89 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Electrolyte','serum'); 
SELECT create_relationship_panel_test('Electrolyte','Magnesium'); 
SELECT create_relationship_sample_test('serum','Magnesium'); 
SELECT insert_unit_of_measure('mg/dl','Magnesium'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','Magnesium'); 
SELECT insert_result_limit_normal_range('Magnesium',1.30,2.10); 
SELECT insert_result_limit_valid_range('Magnesium',0.20,10); 
-- Begin Line: 90 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Anaemia Panel','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Anaemia Panel','MCH'); 
SELECT create_relationship_sample_test('Blood (EDTA)','MCH'); 
SELECT insert_unit_of_measure('per fL','MCH'); 
SELECT create_relationship_test_section_test('Haematology','MCH'); 
SELECT insert_result_limit_normal_range('MCH',28,31); 
SELECT insert_result_limit_valid_range('MCH',20,50); 
-- Begin Line: 91 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Anaemia Panel','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Anaemia Panel','MCV'); 
SELECT create_relationship_sample_test('Blood (EDTA)','MCV'); 
SELECT insert_unit_of_measure('per fL','MCV'); 
SELECT create_relationship_test_section_test('Haematology','MCV'); 
SELECT insert_result_limit_normal_range('MCV',75,81); 
SELECT insert_result_limit_valid_range('MCV',40,130); 
-- Begin Line: 92 
SELECT insert_test_section('Vaginal'); 
SELECT insert_sample_type('vaginal'); 
SELECT create_relationship_panel_sampletype('Vaginal Panel','vaginal'); 
SELECT create_relationship_panel_test('Vaginal Panel','Microscopy of Saline mount'); 
SELECT create_relationship_sample_test('vaginal','Microscopy of Saline mount'); 
SELECT insert_unit_of_measure('NA','Microscopy of Saline mount'); 
SELECT create_relationship_test_section_test('Vaginal','Microscopy of Saline mount'); 
SELECT add_test_result_type('Microscopy of Saline mount','R'); 
-- Begin Line: 93 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Differential Count','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Differential Count','Mono'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Mono'); 
SELECT insert_unit_of_measure('%','Mono'); 
SELECT create_relationship_test_section_test('Haematology','Mono'); 
SELECT insert_result_limit_normal_range('Mono',1,6); 
SELECT insert_result_limit_valid_range('Mono',1,70); 
-- Begin Line: 94 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Semen'); 
SELECT create_relationship_panel_sampletype('Semen Analysis','Semen'); 
SELECT create_relationship_panel_test('Semen Analysis','Motility'); 
SELECT create_relationship_sample_test('Semen','Motility'); 
SELECT insert_unit_of_measure('','Motility'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Motility'); 
SELECT add_test_result_type('Motility','R'); 
-- Begin Line: 95 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Urine LE/Nitrite','Urine'); 
SELECT create_relationship_panel_test('Urine LE/Nitrite','Nitrite'); 
SELECT create_relationship_sample_test('Urine','Nitrite'); 
SELECT insert_unit_of_measure('NA','Nitrite'); 
SELECT create_relationship_test_section_test('Urine','Nitrite'); 
SELECT add_test_result_type('Nitrite','R'); 
-- Begin Line: 96 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Urine Microscopy','Urine'); 
SELECT create_relationship_panel_test('Urine Microscopy','Others'); 
SELECT create_relationship_sample_test('Urine','Others'); 
SELECT insert_unit_of_measure('/hPF','Others'); 
SELECT create_relationship_test_section_test('Urine','Others'); 
SELECT add_test_result_type('Others','R'); 
-- Begin Line: 97 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Stool Microscopy w/o Concetration','Stool'); 
SELECT create_relationship_panel_test('Stool Microscopy w/o Concetration','Others'); 
SELECT create_relationship_sample_test('Stool','Others'); 
SELECT insert_unit_of_measure('NA','Others'); 
SELECT create_relationship_test_section_test('Stool','Others'); 
SELECT add_test_result_type('Others','R'); 
-- Begin Line: 98 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Stool Microscopy with Concetration','Stool'); 
SELECT create_relationship_panel_test('Stool Microscopy with Concetration','Others'); 
SELECT create_relationship_sample_test('Stool','Others'); 
SELECT insert_unit_of_measure('NA','Others'); 
SELECT create_relationship_test_section_test('Stool','Others'); 
SELECT add_test_result_type('Others','R'); 
-- Begin Line: 99 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Stool Microscopy w/o Concetration','Stool'); 
SELECT create_relationship_panel_test('Stool Microscopy w/o Concetration','Ova/Parasite/Cyst'); 
SELECT create_relationship_sample_test('Stool','Ova/Parasite/Cyst'); 
SELECT insert_unit_of_measure('NA','Ova/Parasite/Cyst'); 
SELECT create_relationship_test_section_test('Stool','Ova/Parasite/Cyst'); 
SELECT add_test_result_type('Ova/Parasite/Cyst','R'); 
-- Begin Line: 100 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Stool Microscopy with Concetration','Stool'); 
SELECT create_relationship_panel_test('Stool Microscopy with Concetration','Ova/Parasite/Cyst'); 
SELECT create_relationship_sample_test('Stool','Ova/Parasite/Cyst'); 
SELECT insert_unit_of_measure('NA','Ova/Parasite/Cyst'); 
SELECT create_relationship_test_section_test('Stool','Ova/Parasite/Cyst'); 
SELECT add_test_result_type('Ova/Parasite/Cyst','R'); 
-- Begin Line: 101 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Routine Blood','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Routine Blood','Packed Cell Volume (PCV)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Packed Cell Volume (PCV)'); 
SELECT insert_unit_of_measure('%','Packed Cell Volume (PCV)'); 
SELECT create_relationship_test_section_test('Haematology','Packed Cell Volume (PCV)'); 
SELECT insert_result_limit_normal_range('Packed Cell Volume (PCV)',45,55); 
SELECT insert_result_limit_valid_range('Packed Cell Volume (PCV)',20,80); 
-- Begin Line: 102 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Anaemia Panel','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Anaemia Panel','Packed Cell Volume (PCV)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Packed Cell Volume (PCV)'); 
SELECT insert_unit_of_measure('%','Packed Cell Volume (PCV)'); 
SELECT create_relationship_test_section_test('Haematology','Packed Cell Volume (PCV)'); 
SELECT insert_result_limit_normal_range('Packed Cell Volume (PCV)',45,55); 
SELECT insert_result_limit_valid_range('Packed Cell Volume (PCV)',20,80); 
-- Begin Line: 103 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Routine Urine','Urine'); 
SELECT create_relationship_panel_test('Routine Urine','pH'); 
SELECT create_relationship_sample_test('Urine','pH'); 
SELECT insert_unit_of_measure('NA','pH'); 
SELECT create_relationship_test_section_test('Urine','pH'); 
SELECT insert_result_limit_normal_range('pH',6.80,7); 
SELECT insert_result_limit_valid_range('pH',0,14); 
-- Begin Line: 104 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Environment','Stool'); 
SELECT create_relationship_panel_test('Environment','pH'); 
SELECT create_relationship_sample_test('Stool','pH'); 
SELECT insert_unit_of_measure('NA','pH'); 
SELECT create_relationship_test_section_test('Stool','pH'); 
SELECT insert_result_limit_normal_range('pH',6.80,7.20); 
SELECT insert_result_limit_valid_range('pH',1,14); 
-- Begin Line: 105 
SELECT insert_test_section('Vaginal'); 
SELECT insert_sample_type('vaginal'); 
SELECT create_relationship_panel_sampletype('Vaginal Panel','vaginal'); 
SELECT create_relationship_panel_test('Vaginal Panel','pH'); 
SELECT create_relationship_sample_test('vaginal','pH'); 
SELECT insert_unit_of_measure('NA','pH'); 
SELECT create_relationship_test_section_test('Vaginal','pH'); 
SELECT insert_result_limit_normal_range('pH',1,14); 
SELECT insert_result_limit_valid_range('pH',1,14); 
-- Begin Line: 106 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Electrolyte','serum'); 
SELECT create_relationship_panel_test('Electrolyte','Phosphorous'); 
SELECT create_relationship_sample_test('serum','Phosphorous'); 
SELECT insert_unit_of_measure('mg/dl','Phosphorous'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','Phosphorous'); 
SELECT insert_result_limit_normal_range('Phosphorous',3,4.50); 
SELECT insert_result_limit_valid_range('Phosphorous',1,14); 
-- Begin Line: 107 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Anaemia Panel','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Anaemia Panel','Platelet Count'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Platelet Count'); 
SELECT insert_unit_of_measure('cumm','Platelet Count'); 
SELECT create_relationship_test_section_test('Haematology','Platelet Count'); 
SELECT insert_result_limit_normal_range('Platelet Count',100000,300000); 
SELECT insert_result_limit_valid_range('Platelet Count',5000,1000000); 
-- Begin Line: 108 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('CBC','Blood (EDTA)'); 
SELECT create_relationship_panel_test('CBC','Platelet Count'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Platelet Count'); 
SELECT insert_unit_of_measure('cumm','Platelet Count'); 
SELECT create_relationship_test_section_test('Haematology','Platelet Count'); 
SELECT insert_result_limit_normal_range('Platelet Count',100000,300000); 
SELECT insert_result_limit_valid_range('Platelet Count',5000,1000000); 
-- Begin Line: 109 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Differential Count','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Differential Count','Poly'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Poly'); 
SELECT insert_unit_of_measure('%','Poly'); 
SELECT create_relationship_test_section_test('Haematology','Poly'); 
SELECT insert_result_limit_normal_range('Poly',40,75); 
SELECT insert_result_limit_valid_range('Poly',20,80); 
-- Begin Line: 110 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Electrolyte','serum'); 
SELECT create_relationship_panel_test('Electrolyte','Potassium'); 
SELECT create_relationship_sample_test('serum','Potassium'); 
SELECT insert_unit_of_measure('me/ql','Potassium'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','Potassium'); 
SELECT insert_result_limit_normal_range('Potassium',3.50,5.10); 
SELECT insert_result_limit_valid_range('Potassium',0.50,12); 
-- Begin Line: 111 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Bio Chemistry','serum'); 
SELECT create_relationship_panel_test('Bio Chemistry','PP2BS'); 
SELECT create_relationship_sample_test('serum','PP2BS'); 
SELECT insert_unit_of_measure('mg/dl','PP2BS'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','PP2BS'); 
SELECT insert_result_limit_normal_range('PP2BS',120,150); 
SELECT insert_result_limit_valid_range('PP2BS',120,1000); 
-- Begin Line: 112 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Bio Chemistry','serum'); 
SELECT create_relationship_panel_test('Bio Chemistry','Prolactin'); 
SELECT create_relationship_sample_test('serum','Prolactin'); 
SELECT insert_unit_of_measure('nd/ml','Prolactin'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','Prolactin'); 
-- Begin Line: 113 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Asitic Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Asitic Fluid','Protein'); 
SELECT create_relationship_sample_test('Body Fluid','Protein'); 
SELECT insert_unit_of_measure('mg/dl','Protein'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','Protein'); 
-- Begin Line: 114 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Pyrotinial Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Pyrotinial Fluid','Protein'); 
SELECT create_relationship_sample_test('Body Fluid','Protein'); 
SELECT insert_unit_of_measure('mg/dl','Protein'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','Protein'); 
-- Begin Line: 115 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Pus','Body Fluid'); 
SELECT create_relationship_panel_test('Pus','Protein'); 
SELECT create_relationship_sample_test('Body Fluid','Protein'); 
SELECT insert_unit_of_measure('mg/dl','Protein'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','Protein'); 
-- Begin Line: 116 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Knee Joint Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Knee Joint Fluid','Protein'); 
SELECT create_relationship_sample_test('Body Fluid','Protein'); 
SELECT insert_unit_of_measure('mg/dl','Protein'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','Protein'); 
-- Begin Line: 117 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Plural Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Plural Fluid','Protein'); 
SELECT create_relationship_sample_test('Body Fluid','Protein'); 
SELECT insert_unit_of_measure('mg/dl','Protein'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','Protein'); 
-- Begin Line: 118 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('CSF','Body Fluid'); 
SELECT create_relationship_panel_test('CSF','Protein (CSF)'); 
SELECT create_relationship_sample_test('Body Fluid','Protein (CSF)'); 
SELECT insert_unit_of_measure('mg/dl','Protein (CSF)'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','Protein (CSF)'); 
-- Begin Line: 119 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Coagulation Factor','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Coagulation Factor','Prothrombin Time (Control)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Prothrombin Time (Control)'); 
SELECT insert_unit_of_measure('sec','Prothrombin Time (Control)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Prothrombin Time (Control)'); 
SELECT insert_result_limit_normal_range('Prothrombin Time (Control)',11,16); 
SELECT insert_result_limit_valid_range('Prothrombin Time (Control)',5,720); 
-- Begin Line: 120 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Liver Function Test','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Liver Function Test','Prothrombin Time (Control)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Prothrombin Time (Control)'); 
SELECT insert_unit_of_measure('sec','Prothrombin Time (Control)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Prothrombin Time (Control)'); 
SELECT insert_result_limit_normal_range('Prothrombin Time (Control)',11,16); 
SELECT insert_result_limit_valid_range('Prothrombin Time (Control)',5,720); 
-- Begin Line: 121 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Coagulation Factor','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Coagulation Factor','Prothrombin Time (Test)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Prothrombin Time (Test)'); 
SELECT insert_unit_of_measure('sec','Prothrombin Time (Test)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Prothrombin Time (Test)'); 
SELECT insert_result_limit_normal_range('Prothrombin Time (Test)',11,16); 
SELECT insert_result_limit_valid_range('Prothrombin Time (Test)',5,720); 
-- Begin Line: 122 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Liver Function Test','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Liver Function Test','Prothrombin Time (Test)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Prothrombin Time (Test)'); 
SELECT insert_unit_of_measure('sec','Prothrombin Time (Test)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Prothrombin Time (Test)'); 
SELECT insert_result_limit_normal_range('Prothrombin Time (Test)',11,16); 
SELECT insert_result_limit_valid_range('Prothrombin Time (Test)',5,720); 
-- Begin Line: 123 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('RBC Morphology','Blood (EDTA)'); 
SELECT create_relationship_panel_test('RBC Morphology','PS for RBC Morphology'); 
SELECT create_relationship_sample_test('Blood (EDTA)','PS for RBC Morphology'); 
SELECT insert_unit_of_measure('NA','PS for RBC Morphology'); 
SELECT create_relationship_test_section_test('Haematology','PS for RBC Morphology'); 
SELECT add_test_result_type('PS for RBC Morphology','R'); 
-- Begin Line: 124 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Anaemia Panel','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Anaemia Panel','PS for RBC Morphology'); 
SELECT create_relationship_sample_test('Blood (EDTA)','PS for RBC Morphology'); 
SELECT insert_unit_of_measure('NA','PS for RBC Morphology'); 
SELECT create_relationship_test_section_test('Haematology','PS for RBC Morphology'); 
SELECT add_test_result_type('PS for RBC Morphology','R'); 
-- Begin Line: 125 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Bio Chemistry','serum'); 
SELECT create_relationship_panel_test('Bio Chemistry','PSA'); 
SELECT create_relationship_sample_test('serum','PSA'); 
SELECT insert_unit_of_measure('ng/ml','PSA'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','PSA'); 
SELECT insert_result_limit_normal_range('PSA',0.10,4); 
SELECT insert_result_limit_valid_range('PSA',0.10,50); 
-- Begin Line: 126 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Urine Microscopy','Urine'); 
SELECT create_relationship_panel_test('Urine Microscopy','Pus Cells'); 
SELECT create_relationship_sample_test('Urine','Pus Cells'); 
SELECT insert_unit_of_measure('/hPF','Pus Cells'); 
SELECT create_relationship_test_section_test('Urine','Pus Cells'); 
SELECT add_test_result_type('Pus Cells','R'); 
-- Begin Line: 127 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Stool Microscopy w/o Concetration','Stool'); 
SELECT create_relationship_panel_test('Stool Microscopy w/o Concetration','Pus Cells'); 
SELECT create_relationship_sample_test('Stool','Pus Cells'); 
SELECT insert_unit_of_measure('NA','Pus Cells'); 
SELECT create_relationship_test_section_test('Stool','Pus Cells'); 
SELECT add_test_result_type('Pus Cells','R'); 
-- Begin Line: 128 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Stool Microscopy with Concetration','Stool'); 
SELECT create_relationship_panel_test('Stool Microscopy with Concetration','Pus Cells'); 
SELECT create_relationship_sample_test('Stool','Pus Cells'); 
SELECT insert_unit_of_measure('NA','Pus Cells'); 
SELECT create_relationship_test_section_test('Stool','Pus Cells'); 
SELECT add_test_result_type('Pus Cells','R'); 
-- Begin Line: 129 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Semen'); 
SELECT create_relationship_panel_sampletype('Semen Analysis','Semen'); 
SELECT create_relationship_panel_test('Semen Analysis','Pus Cells (Semen)'); 
SELECT create_relationship_sample_test('Semen','Pus Cells (Semen)'); 
SELECT insert_unit_of_measure('per hpf','Pus Cells (Semen)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Pus Cells (Semen)'); 
SELECT add_test_result_type('Pus Cells (Semen)','R'); 
-- Begin Line: 130 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Stool Microscopy w/o Concetration','Stool'); 
SELECT create_relationship_panel_test('Stool Microscopy w/o Concetration','Quality'); 
SELECT create_relationship_sample_test('Stool','Quality'); 
SELECT insert_unit_of_measure('NA','Quality'); 
SELECT create_relationship_test_section_test('Stool','Quality'); 
SELECT add_test_result_type('Quality','R'); 
-- Begin Line: 131 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Stool Microscopy with Concetration','Stool'); 
SELECT create_relationship_panel_test('Stool Microscopy with Concetration','Quality'); 
SELECT create_relationship_sample_test('Stool','Quality'); 
SELECT insert_unit_of_measure('NA','Quality'); 
SELECT create_relationship_test_section_test('Stool','Quality'); 
SELECT add_test_result_type('Quality','R'); 
-- Begin Line: 132 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Semen'); 
SELECT create_relationship_panel_sampletype('Semen Analysis','Semen'); 
SELECT create_relationship_panel_test('Semen Analysis','RBC (Semen)'); 
SELECT create_relationship_sample_test('Semen','RBC (Semen)'); 
SELECT insert_unit_of_measure('per hpf','RBC (Semen)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','RBC (Semen)'); 
SELECT add_test_result_type('RBC (Semen)','R'); 
-- Begin Line: 133 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Stool Microscopy w/o Concetration','Stool'); 
SELECT create_relationship_panel_test('Stool Microscopy w/o Concetration','RBC (Stool)'); 
SELECT create_relationship_sample_test('Stool','RBC (Stool)'); 
SELECT insert_unit_of_measure('NA','RBC (Stool)'); 
SELECT create_relationship_test_section_test('Stool','RBC (Stool)'); 
SELECT add_test_result_type('RBC (Stool)','R'); 
-- Begin Line: 134 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Stool Microscopy with Concetration','Stool'); 
SELECT create_relationship_panel_test('Stool Microscopy with Concetration','RBC (Stool)'); 
SELECT create_relationship_sample_test('Stool','RBC (Stool)'); 
SELECT insert_unit_of_measure('NA','RBC (Stool)'); 
SELECT create_relationship_test_section_test('Stool','RBC (Stool)'); 
SELECT add_test_result_type('RBC (Stool)','R'); 
-- Begin Line: 135 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Urine Microscopy','Urine'); 
SELECT create_relationship_panel_test('Urine Microscopy','RBC (Urine)'); 
SELECT create_relationship_sample_test('Urine','RBC (Urine)'); 
SELECT insert_unit_of_measure('/hPF','RBC (Urine)'); 
SELECT create_relationship_test_section_test('Urine','RBC (Urine)'); 
SELECT add_test_result_type('RBC (Urine)','R'); 
-- Begin Line: 136 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('CBC','Blood (EDTA)'); 
SELECT create_relationship_panel_test('CBC','RBC Count'); 
SELECT create_relationship_sample_test('Blood (EDTA)','RBC Count'); 
SELECT insert_unit_of_measure('per cumm','RBC Count'); 
SELECT create_relationship_test_section_test('Haematology','RBC Count'); 
SELECT insert_result_limit_normal_range('RBC Count',450000,550000); 
SELECT insert_result_limit_valid_range('RBC Count',200000,800000); 
-- Begin Line: 137 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Anaemia Panel','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Anaemia Panel','RBC Count'); 
SELECT create_relationship_sample_test('Blood (EDTA)','RBC Count'); 
SELECT insert_unit_of_measure('per cumm','RBC Count'); 
SELECT create_relationship_test_section_test('Haematology','RBC Count'); 
SELECT insert_result_limit_normal_range('RBC Count',450000,550000); 
SELECT insert_result_limit_valid_range('RBC Count',200000,800000); 
-- Begin Line: 138 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Bio Chemistry','serum'); 
SELECT create_relationship_panel_test('Bio Chemistry','RBS'); 
SELECT create_relationship_sample_test('serum','RBS'); 
SELECT insert_unit_of_measure('mg/dl','RBS'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','RBS'); 
SELECT insert_result_limit_normal_range('RBS',60,160); 
SELECT insert_result_limit_valid_range('RBS',10,1000); 
-- Begin Line: 139 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Renal','Urine'); 
SELECT create_relationship_panel_test('Renal','Renal Concentrating Ability'); 
SELECT create_relationship_sample_test('Urine','Renal Concentrating Ability'); 
SELECT insert_unit_of_measure('NA','Renal Concentrating Ability'); 
SELECT create_relationship_test_section_test('Urine','Renal Concentrating Ability'); 
SELECT add_test_result_type('Renal Concentrating Ability','R'); 
-- Begin Line: 140 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Routine Blood','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Routine Blood','Reticulocyte Count'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Reticulocyte Count'); 
SELECT insert_unit_of_measure('%','Reticulocyte Count'); 
SELECT create_relationship_test_section_test('Haematology','Reticulocyte Count'); 
SELECT insert_result_limit_normal_range('Reticulocyte Count',1.50,3); 
SELECT insert_result_limit_valid_range('Reticulocyte Count',0.50,30); 
-- Begin Line: 141 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Anaemia Panel','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Anaemia Panel','Reticulocyte Count'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Reticulocyte Count'); 
SELECT insert_unit_of_measure('%','Reticulocyte Count'); 
SELECT create_relationship_test_section_test('Haematology','Reticulocyte Count'); 
SELECT insert_result_limit_normal_range('Reticulocyte Count',1,3); 
SELECT insert_result_limit_valid_range('Reticulocyte Count',0.50,30); 
-- Begin Line: 142 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Serology','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Serology','Rheumatoid Arthritis - Turbidometry'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Rheumatoid Arthritis - Turbidometry'); 
SELECT insert_unit_of_measure('iU/ml','Rheumatoid Arthritis - Turbidometry'); 
SELECT create_relationship_test_section_test('Serology','Rheumatoid Arthritis - Turbidometry'); 
SELECT insert_result_limit_normal_range('Rheumatoid Arthritis - Turbidometry',0,20); 
-- Begin Line: 143 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Liver Function Test','serum'); 
SELECT create_relationship_panel_test('Liver Function Test','S. Albumin'); 
SELECT create_relationship_sample_test('serum','S. Albumin'); 
SELECT insert_unit_of_measure('gm/dl','S. Albumin'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','S. Albumin'); 
SELECT insert_result_limit_normal_range('S. Albumin',2.40,5); 
SELECT insert_result_limit_valid_range('S. Albumin',1.50,10); 
-- Begin Line: 144 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Liver Function Test','serum'); 
SELECT create_relationship_panel_test('Liver Function Test','S. ALT '); 
SELECT create_relationship_sample_test('serum','S. ALT '); 
SELECT insert_unit_of_measure('mg/dl','S. ALT '); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','S. ALT '); 
SELECT insert_result_limit_normal_range('S. ALT ',5,35); 
SELECT insert_result_limit_valid_range('S. ALT ',3,2000); 
-- Begin Line: 145 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Liver Function Test','serum'); 
SELECT create_relationship_panel_test('Liver Function Test','S. AST'); 
SELECT create_relationship_sample_test('serum','S. AST'); 
SELECT insert_unit_of_measure('u/L','S. AST'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','S. AST'); 
SELECT insert_result_limit_normal_range('S. AST',5,35); 
SELECT insert_result_limit_valid_range('S. AST',3,3000); 
-- Begin Line: 146 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Liver Function Test','serum'); 
SELECT create_relationship_panel_test('Liver Function Test','S. Protein'); 
SELECT create_relationship_sample_test('serum','S. Protein'); 
SELECT insert_unit_of_measure('gm/dl','S. Protein'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','S. Protein'); 
SELECT insert_result_limit_normal_range('S. Protein',6.50,8.50); 
SELECT insert_result_limit_valid_range('S. Protein',1.50,15); 
-- Begin Line: 147 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Lipid Profile','serum'); 
SELECT create_relationship_panel_test('Lipid Profile','S. Triglyceride'); 
SELECT create_relationship_sample_test('serum','S. Triglyceride'); 
SELECT insert_unit_of_measure('mg/dl','S. Triglyceride'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','S. Triglyceride'); 
SELECT insert_result_limit_normal_range('S. Triglyceride',40,150); 
SELECT insert_result_limit_valid_range('S. Triglyceride',20,700); 
-- Begin Line: 148 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Electrolyte','serum'); 
SELECT create_relationship_panel_test('Electrolyte','Sodium'); 
SELECT create_relationship_sample_test('serum','Sodium'); 
SELECT insert_unit_of_measure('me/ql','Sodium'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','Sodium'); 
SELECT insert_result_limit_normal_range('Sodium',135,145); 
SELECT insert_result_limit_valid_range('Sodium',60,250); 
-- Begin Line: 149 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Routine Urine','Urine'); 
SELECT create_relationship_panel_test('Routine Urine','Specific Gravity'); 
SELECT create_relationship_sample_test('Urine','Specific Gravity'); 
SELECT insert_unit_of_measure('NA','Specific Gravity'); 
SELECT create_relationship_test_section_test('Urine','Specific Gravity'); 
SELECT insert_result_limit_normal_range('Specific Gravity',1.01,1.03); 
SELECT insert_result_limit_valid_range('Specific Gravity',1,1.05); 
-- Begin Line: 150 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Asitic Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Asitic Fluid','Sugar'); 
SELECT create_relationship_sample_test('Body Fluid','Sugar'); 
SELECT insert_unit_of_measure('mg/dl','Sugar'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','Sugar'); 
-- Begin Line: 151 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Pyrotinial Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Pyrotinial Fluid','Sugar'); 
SELECT create_relationship_sample_test('Body Fluid','Sugar'); 
SELECT insert_unit_of_measure('mg/dl','Sugar'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','Sugar'); 
-- Begin Line: 152 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Pus','Body Fluid'); 
SELECT create_relationship_panel_test('Pus','Sugar'); 
SELECT create_relationship_sample_test('Body Fluid','Sugar'); 
SELECT insert_unit_of_measure('mg/dl','Sugar'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','Sugar'); 
-- Begin Line: 153 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Knee Joint Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Knee Joint Fluid','Sugar'); 
SELECT create_relationship_sample_test('Body Fluid','Sugar'); 
SELECT insert_unit_of_measure('mg/dl','Sugar'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','Sugar'); 
-- Begin Line: 154 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Plural Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Plural Fluid','Sugar'); 
SELECT create_relationship_sample_test('Body Fluid','Sugar'); 
SELECT insert_unit_of_measure('mg/dl','Sugar'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','Sugar'); 
-- Begin Line: 155 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('CSF','Body Fluid'); 
SELECT create_relationship_panel_test('CSF','Sugar (CSF)'); 
SELECT create_relationship_sample_test('Body Fluid','Sugar (CSF)'); 
SELECT insert_unit_of_measure('mg/dl','Sugar (CSF)'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','Sugar (CSF)'); 
-- Begin Line: 156 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Bio Chemistry','serum'); 
SELECT create_relationship_panel_test('Bio Chemistry','T3'); 
SELECT create_relationship_sample_test('serum','T3'); 
SELECT insert_unit_of_measure('ng/dl','T3'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','T3'); 
SELECT insert_result_limit_normal_range('T3',60,200); 
SELECT insert_result_limit_valid_range('T3',10,600); 
-- Begin Line: 157 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Bio Chemistry','serum'); 
SELECT create_relationship_panel_test('Bio Chemistry','T4'); 
SELECT create_relationship_sample_test('serum','T4'); 
SELECT insert_unit_of_measure('ug/dl','T4'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','T4'); 
SELECT insert_result_limit_normal_range('T4',4.50,12); 
SELECT insert_result_limit_valid_range('T4',1,50); 
-- Begin Line: 158 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('CBC','Blood (EDTA)'); 
SELECT create_relationship_panel_test('CBC','TC'); 
SELECT create_relationship_sample_test('Blood (EDTA)','TC'); 
SELECT insert_unit_of_measure('','TC'); 
SELECT create_relationship_test_section_test('Haematology','TC'); 
-- Begin Line: 159 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Lipid Profile','serum'); 
SELECT create_relationship_panel_test('Lipid Profile','TC/HDL Ratio'); 
SELECT create_relationship_sample_test('serum','TC/HDL Ratio'); 
SELECT insert_unit_of_measure('','TC/HDL Ratio'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','TC/HDL Ratio'); 
SELECT insert_result_limit_normal_range('TC/HDL Ratio',3.50,3.50); 
SELECT insert_result_limit_valid_range('TC/HDL Ratio',3.50,3.50); 
-- Begin Line: 160 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Liver Function Test','serum'); 
SELECT create_relationship_panel_test('Liver Function Test','Total Bilirubin'); 
SELECT create_relationship_sample_test('serum','Total Bilirubin'); 
SELECT insert_unit_of_measure('mg/dl','Total Bilirubin'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','Total Bilirubin'); 
SELECT insert_result_limit_normal_range('Total Bilirubin',0.30,1.20); 
SELECT insert_result_limit_valid_range('Total Bilirubin',0.20,50); 
-- Begin Line: 161 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Semen'); 
SELECT create_relationship_panel_sampletype('Semen Analysis','Semen'); 
SELECT create_relationship_panel_test('Semen Analysis','Total Count'); 
SELECT create_relationship_sample_test('Semen','Total Count'); 
SELECT insert_unit_of_measure('per ml','Total Count'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Total Count'); 
SELECT insert_result_limit_normal_range('Total Count',60000000,150000000); 
-- Begin Line: 162 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Asitic Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Asitic Fluid','Total Leucocyte Count'); 
SELECT create_relationship_sample_test('Body Fluid','Total Leucocyte Count'); 
SELECT insert_unit_of_measure('cumm','Total Leucocyte Count'); 
SELECT create_relationship_test_section_test('Haematology','Total Leucocyte Count'); 
-- Begin Line: 163 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Pyrotinial Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Pyrotinial Fluid','Total Leucocyte Count'); 
SELECT create_relationship_sample_test('Body Fluid','Total Leucocyte Count'); 
SELECT insert_unit_of_measure('cumm','Total Leucocyte Count'); 
SELECT create_relationship_test_section_test('Haematology','Total Leucocyte Count'); 
-- Begin Line: 164 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Pus','Body Fluid'); 
SELECT create_relationship_panel_test('Pus','Total Leucocyte Count'); 
SELECT create_relationship_sample_test('Body Fluid','Total Leucocyte Count'); 
SELECT insert_unit_of_measure('cumm','Total Leucocyte Count'); 
SELECT create_relationship_test_section_test('Haematology','Total Leucocyte Count'); 
-- Begin Line: 165 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Knee Joint Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Knee Joint Fluid','Total Leucocyte Count'); 
SELECT create_relationship_sample_test('Body Fluid','Total Leucocyte Count'); 
SELECT insert_unit_of_measure('cumm','Total Leucocyte Count'); 
SELECT create_relationship_test_section_test('Haematology','Total Leucocyte Count'); 
-- Begin Line: 166 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Plural Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Plural Fluid','Total Leucocyte Count'); 
SELECT create_relationship_sample_test('Body Fluid','Total Leucocyte Count'); 
SELECT insert_unit_of_measure('cumm','Total Leucocyte Count'); 
SELECT create_relationship_test_section_test('Haematology','Total Leucocyte Count'); 
-- Begin Line: 167 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('CSF','Body Fluid'); 
SELECT create_relationship_panel_test('CSF','Total Leucocyte Count (CSF)'); 
SELECT create_relationship_sample_test('Body Fluid','Total Leucocyte Count (CSF)'); 
SELECT insert_unit_of_measure('cumm','Total Leucocyte Count (CSF)'); 
SELECT create_relationship_test_section_test('Haematology','Total Leucocyte Count (CSF)'); 
-- Begin Line: 168 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Routine Blood','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Routine Blood','Total Leucocyte Count'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Total Leucocyte Count'); 
SELECT insert_unit_of_measure('cumm','Total Leucocyte Count'); 
SELECT create_relationship_test_section_test('Haematology','Total Leucocyte Count'); 
SELECT insert_result_limit_normal_range('Total Leucocyte Count',4500,11000); 
SELECT insert_result_limit_valid_range('Total Leucocyte Count',500,400000); 
-- Begin Line: 169 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Bio Chemistry','serum'); 
SELECT create_relationship_panel_test('Bio Chemistry','TSH'); 
SELECT create_relationship_sample_test('serum','TSH'); 
SELECT insert_unit_of_measure('Iu/ml','TSH'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','TSH'); 
SELECT insert_result_limit_normal_range('TSH',0.30,5.50); 
SELECT insert_result_limit_valid_range('TSH',0.10,30); 
-- Begin Line: 170 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Bio Chemistry','serum'); 
SELECT create_relationship_panel_test('Bio Chemistry','Urea Nitrogen'); 
SELECT create_relationship_sample_test('serum','Urea Nitrogen'); 
SELECT insert_unit_of_measure('mg/dl','Urea Nitrogen'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','Urea Nitrogen'); 
SELECT insert_result_limit_normal_range('Urea Nitrogen',40,150); 
SELECT insert_result_limit_valid_range('Urea Nitrogen',10,1000); 
-- Begin Line: 171 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Bio Chemistry','serum'); 
SELECT create_relationship_panel_test('Bio Chemistry','Uric Acid'); 
SELECT create_relationship_sample_test('serum','Uric Acid'); 
SELECT insert_unit_of_measure('mg/dl','Uric Acid'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','Uric Acid'); 
SELECT insert_result_limit_normal_range('Uric Acid',3.50,7.20); 
SELECT insert_result_limit_valid_range('Uric Acid',2,30); 
-- Begin Line: 172 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('','Urine'); 
SELECT create_relationship_panel_test('','Urine Protein Creatinine Ratio'); 
SELECT create_relationship_sample_test('Urine','Urine Protein Creatinine Ratio'); 
SELECT insert_unit_of_measure('','Urine Protein Creatinine Ratio'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','Urine Protein Creatinine Ratio'); 
SELECT insert_result_limit_normal_range('Urine Protein Creatinine Ratio',0.20,0.20); 
-- Begin Line: 173 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Urine Microscopy','Urine'); 
SELECT create_relationship_panel_test('Urine Microscopy','Vaginal Trichomonas'); 
SELECT create_relationship_sample_test('Urine','Vaginal Trichomonas'); 
SELECT insert_unit_of_measure('/hPF','Vaginal Trichomonas'); 
SELECT create_relationship_test_section_test('Urine','Vaginal Trichomonas'); 
SELECT add_test_result_type('Vaginal Trichomonas','R'); 
-- Begin Line: 174 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Semen'); 
SELECT create_relationship_panel_sampletype('Semen Analysis','Semen'); 
SELECT create_relationship_panel_test('Semen Analysis','Viscoscity '); 
SELECT create_relationship_sample_test('Semen','Viscoscity '); 
SELECT insert_unit_of_measure('','Viscoscity '); 
SELECT create_relationship_test_section_test('Clinical Pathology','Viscoscity '); 
SELECT add_test_result_type('Viscoscity ','R'); 
-- Begin Line: 175 
SELECT insert_test_section('BIOCHEMISTRY'); 
SELECT insert_sample_type('serum'); 
SELECT create_relationship_panel_sampletype('Lipid Profile','serum'); 
SELECT create_relationship_panel_test('Lipid Profile','VLDL Cholesterol'); 
SELECT create_relationship_sample_test('serum','VLDL Cholesterol'); 
SELECT insert_unit_of_measure('mg/dl','VLDL Cholesterol'); 
SELECT create_relationship_test_section_test('BIOCHEMISTRY','VLDL Cholesterol'); 
SELECT insert_result_limit_normal_range('VLDL Cholesterol',5,40); 
SELECT insert_result_limit_valid_range('VLDL Cholesterol',1,60); 
-- Begin Line: 176 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Semen'); 
SELECT create_relationship_panel_sampletype('Semen Analysis','Semen'); 
SELECT create_relationship_panel_test('Semen Analysis','Volume (Semen)'); 
SELECT create_relationship_sample_test('Semen','Volume (Semen)'); 
SELECT insert_unit_of_measure('per ml','Volume (Semen)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Volume (Semen)'); 
SELECT insert_result_limit_normal_range('Volume (Semen)',2,3.50); 
-- Begin Line: 177 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Urine Microscopy','Urine'); 
SELECT create_relationship_panel_test('Urine Microscopy','Yeast Cell'); 
SELECT create_relationship_sample_test('Urine','Yeast Cell'); 
SELECT insert_unit_of_measure('/hPF','Yeast Cell'); 
SELECT create_relationship_test_section_test('Urine','Yeast Cell'); 
SELECT add_test_result_type('Yeast Cell','R'); 
-- Begin Line: 178 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Routine Urine','Urine'); 
SELECT create_relationship_panel_test('Routine Urine','Albumin'); 
SELECT create_relationship_sample_test('Urine','Albumin'); 
SELECT insert_unit_of_measure('NA','Albumin'); 
SELECT create_relationship_test_section_test('Urine','Albumin'); 
SELECT add_test_result_type('Albumin','D', 'Nil'); 
SELECT add_test_result_type('Albumin','D', 'Trace'); 
SELECT add_test_result_type('Albumin','D', '1+'); 
SELECT add_test_result_type('Albumin','D', '2+'); 
SELECT add_test_result_type('Albumin','D', '3+'); 
SELECT add_test_result_type('Albumin','D', '4+'); 
-- Begin Line: 179 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Serology','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Serology','Anti Streptolysin Qualitative'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Anti Streptolysin Qualitative'); 
SELECT insert_unit_of_measure('NA','Anti Streptolysin Qualitative'); 
SELECT create_relationship_test_section_test('Serology','Anti Streptolysin Qualitative'); 
SELECT add_test_result_type('Anti Streptolysin Qualitative','D', 'Positive 1:2'); 
SELECT add_test_result_type('Anti Streptolysin Qualitative','D', 'Positive 1:4'); 
SELECT add_test_result_type('Anti Streptolysin Qualitative','D', 'Positive 1:8'); 
SELECT add_test_result_type('Anti Streptolysin Qualitative','D', 'Positive 1:16'); 
SELECT add_test_result_type('Anti Streptolysin Qualitative','D', 'Positive 1:32'); 
SELECT add_test_result_type('Anti Streptolysin Qualitative','D', 'Negative'); 
-- Begin Line: 180 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Cross Match','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Cross Match','Blood Group (Relative)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Blood Group (Relative)'); 
SELECT insert_unit_of_measure('','Blood Group (Relative)'); 
SELECT create_relationship_test_section_test('Haematology','Blood Group (Relative)'); 
SELECT add_test_result_type('Blood Group (Relative)','D', 'O+'); 
SELECT add_test_result_type('Blood Group (Relative)','D', 'A+'); 
SELECT add_test_result_type('Blood Group (Relative)','D', 'B+'); 
SELECT add_test_result_type('Blood Group (Relative)','D', 'AB+'); 
SELECT add_test_result_type('Blood Group (Relative)','D', 'O-'); 
SELECT add_test_result_type('Blood Group (Relative)','D', 'A-'); 
SELECT add_test_result_type('Blood Group (Relative)','D', 'B-'); 
SELECT add_test_result_type('Blood Group (Relative)','D', 'AB-'); 
-- Begin Line: 181 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Cross Match','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Cross Match','Cross Match'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Cross Match'); 
SELECT insert_unit_of_measure('NA','Cross Match'); 
SELECT create_relationship_test_section_test('Serology','Cross Match'); 
SELECT add_test_result_type('Cross Match','D', 'OK'); 
SELECT add_test_result_type('Cross Match','D', 'NOT OK'); 
-- Begin Line: 182 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Fat','Stool'); 
SELECT create_relationship_panel_test('Fat','Fat (Semi Quantitative)'); 
SELECT create_relationship_sample_test('Stool','Fat (Semi Quantitative)'); 
SELECT insert_unit_of_measure('NA','Fat (Semi Quantitative)'); 
SELECT create_relationship_test_section_test('Stool','Fat (Semi Quantitative)'); 
SELECT add_test_result_type('Fat (Semi Quantitative)','D', 'Present'); 
SELECT add_test_result_type('Fat (Semi Quantitative)','D', 'Absent'); 
-- Begin Line: 183 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Hb w/Electrophoresis','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Hb w/Electrophoresis','Hb Electrophoresis'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Hb Electrophoresis'); 
SELECT insert_unit_of_measure('NA','Hb Electrophoresis'); 
SELECT create_relationship_test_section_test('Haematology','Hb Electrophoresis'); 
SELECT add_test_result_type('Hb Electrophoresis','D', 'AA'); 
SELECT add_test_result_type('Hb Electrophoresis','D', 'AS'); 
SELECT add_test_result_type('Hb Electrophoresis','D', 'SS'); 
-- Begin Line: 184 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Anaemia Panel','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Anaemia Panel','Hb Electrophoresis'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Hb Electrophoresis'); 
SELECT insert_unit_of_measure('NA','Hb Electrophoresis'); 
SELECT create_relationship_test_section_test('Haematology','Hb Electrophoresis'); 
SELECT add_test_result_type('Hb Electrophoresis','D', 'AA'); 
SELECT add_test_result_type('Hb Electrophoresis','D', 'AS'); 
SELECT add_test_result_type('Hb Electrophoresis','D', 'SS'); 
-- Begin Line: 185 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Serology','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Serology','HbsAg ELISA'); 
SELECT create_relationship_sample_test('Blood (EDTA)','HbsAg ELISA'); 
SELECT insert_unit_of_measure('NA','HbsAg ELISA'); 
SELECT create_relationship_test_section_test('Serology','HbsAg ELISA'); 
SELECT add_test_result_type('HbsAg ELISA','D', 'Positive'); 
SELECT add_test_result_type('HbsAg ELISA','D', 'Negative'); 
-- Begin Line: 186 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Cross Match','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Cross Match','HbsAg ELISA'); 
SELECT create_relationship_sample_test('Blood (EDTA)','HbsAg ELISA'); 
SELECT insert_unit_of_measure('NA','HbsAg ELISA'); 
SELECT create_relationship_test_section_test('Serology','HbsAg ELISA'); 
SELECT add_test_result_type('HbsAg ELISA','D', 'Positive'); 
SELECT add_test_result_type('HbsAg ELISA','D', 'Negative'); 
-- Begin Line: 187 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Serology','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Serology','HbsAg Rapid'); 
SELECT create_relationship_sample_test('Blood (EDTA)','HbsAg Rapid'); 
SELECT insert_unit_of_measure('NA','HbsAg Rapid'); 
SELECT create_relationship_test_section_test('Serology','HbsAg Rapid'); 
SELECT add_test_result_type('HbsAg Rapid','D', 'Positive'); 
SELECT add_test_result_type('HbsAg Rapid','D', 'Negative'); 
-- Begin Line: 188 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Cross Match','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Cross Match','HbsAg Rapid'); 
SELECT create_relationship_sample_test('Blood (EDTA)','HbsAg Rapid'); 
SELECT insert_unit_of_measure('NA','HbsAg Rapid'); 
SELECT create_relationship_test_section_test('Serology','HbsAg Rapid'); 
SELECT add_test_result_type('HbsAg Rapid','D', 'Positive'); 
SELECT add_test_result_type('HbsAg Rapid','D', 'Negative'); 
-- Begin Line: 189 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Serology','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Serology','HCV ELISA'); 
SELECT create_relationship_sample_test('Blood (EDTA)','HCV ELISA'); 
SELECT insert_unit_of_measure('NA','HCV ELISA'); 
SELECT create_relationship_test_section_test('Serology','HCV ELISA'); 
SELECT add_test_result_type('HCV ELISA','D', 'Positive'); 
SELECT add_test_result_type('HCV ELISA','D', 'Negative'); 
-- Begin Line: 190 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Cross Match','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Cross Match','HCV ELISA'); 
SELECT create_relationship_sample_test('Blood (EDTA)','HCV ELISA'); 
SELECT insert_unit_of_measure('NA','HCV ELISA'); 
SELECT create_relationship_test_section_test('Serology','HCV ELISA'); 
SELECT add_test_result_type('HCV ELISA','D', 'Positive'); 
SELECT add_test_result_type('HCV ELISA','D', 'Negative'); 
-- Begin Line: 191 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Serology','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Serology','HCV Tridot'); 
SELECT create_relationship_sample_test('Blood (EDTA)','HCV Tridot'); 
SELECT insert_unit_of_measure('NA','HCV Tridot'); 
SELECT create_relationship_test_section_test('Serology','HCV Tridot'); 
SELECT add_test_result_type('HCV Tridot','D', 'Positive'); 
SELECT add_test_result_type('HCV Tridot','D', 'Negative'); 
-- Begin Line: 192 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Cross Match','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Cross Match','HCV Tridot'); 
SELECT create_relationship_sample_test('Blood (EDTA)','HCV Tridot'); 
SELECT insert_unit_of_measure('NA','HCV Tridot'); 
SELECT create_relationship_test_section_test('Serology','HCV Tridot'); 
SELECT add_test_result_type('HCV Tridot','D', 'Positive'); 
SELECT add_test_result_type('HCV Tridot','D', 'Negative'); 
-- Begin Line: 193 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Serology','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Serology','HIV ELISA'); 
SELECT create_relationship_sample_test('Blood (EDTA)','HIV ELISA'); 
SELECT insert_unit_of_measure('NA','HIV ELISA'); 
SELECT create_relationship_test_section_test('Serology','HIV ELISA'); 
SELECT add_test_result_type('HIV ELISA','D', 'Positive'); 
SELECT add_test_result_type('HIV ELISA','D', 'Negative'); 
-- Begin Line: 194 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Cross Match','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Cross Match','HIV ELISA'); 
SELECT create_relationship_sample_test('Blood (EDTA)','HIV ELISA'); 
SELECT insert_unit_of_measure('NA','HIV ELISA'); 
SELECT create_relationship_test_section_test('Serology','HIV ELISA'); 
SELECT add_test_result_type('HIV ELISA','D', 'Positive'); 
SELECT add_test_result_type('HIV ELISA','D', 'Negative'); 
-- Begin Line: 195 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Serology','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Serology','HIV Tridot'); 
SELECT create_relationship_sample_test('Blood (EDTA)','HIV Tridot'); 
SELECT insert_unit_of_measure('NA','HIV Tridot'); 
SELECT create_relationship_test_section_test('Serology','HIV Tridot'); 
SELECT add_test_result_type('HIV Tridot','D', 'Positive'); 
SELECT add_test_result_type('HIV Tridot','D', 'Negative'); 
-- Begin Line: 196 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Cross Match','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Cross Match','HIV Tridot'); 
SELECT create_relationship_sample_test('Blood (EDTA)','HIV Tridot'); 
SELECT insert_unit_of_measure('NA','HIV Tridot'); 
SELECT create_relationship_test_section_test('Serology','HIV Tridot'); 
SELECT add_test_result_type('HIV Tridot','D', 'Positive'); 
SELECT add_test_result_type('HIV Tridot','D', 'Negative'); 
-- Begin Line: 197 
SELECT insert_test_section('Vaginal'); 
SELECT insert_sample_type('vaginal'); 
SELECT create_relationship_panel_sampletype('Vaginal Panel','vaginal'); 
SELECT create_relationship_panel_test('Vaginal Panel','LE'); 
SELECT create_relationship_sample_test('vaginal','LE'); 
SELECT insert_unit_of_measure('NA','LE'); 
SELECT create_relationship_test_section_test('Vaginal','LE'); 
SELECT add_test_result_type('LE','D', 'Positive'); 
SELECT add_test_result_type('LE','D', 'Negative'); 
-- Begin Line: 198 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Cross Match','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Cross Match','Malaria Parasite'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Malaria Parasite'); 
SELECT insert_unit_of_measure('NA','Malaria Parasite'); 
SELECT create_relationship_test_section_test('Serology','Malaria Parasite'); 
SELECT add_test_result_type('Malaria Parasite','D', 'Positive'); 
SELECT add_test_result_type('Malaria Parasite','D', 'Negative'); 
-- Begin Line: 199 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Anaemia Panel','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Anaemia Panel','MCHC'); 
SELECT create_relationship_sample_test('Blood (EDTA)','MCHC'); 
SELECT insert_unit_of_measure('','MCHC'); 
SELECT create_relationship_test_section_test('Haematology','MCHC'); 
-- Begin Line: 200 
SELECT insert_test_section('Vaginal'); 
SELECT insert_sample_type('vaginal'); 
SELECT create_relationship_panel_sampletype('Vaginal Panel','vaginal'); 
SELECT create_relationship_panel_test('Vaginal Panel','Microscopy of KOH mount'); 
SELECT create_relationship_sample_test('vaginal','Microscopy of KOH mount'); 
SELECT insert_unit_of_measure('NA','Microscopy of KOH mount'); 
SELECT create_relationship_test_section_test('Vaginal','Microscopy of KOH mount'); 
SELECT add_test_result_type('Microscopy of KOH mount','D', 'Present'); 
SELECT add_test_result_type('Microscopy of KOH mount','D', 'Absent'); 
-- Begin Line: 201 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Semen'); 
SELECT create_relationship_panel_sampletype('Semen Analysis','Semen'); 
SELECT create_relationship_panel_test('Semen Analysis','Morphology'); 
SELECT create_relationship_sample_test('Semen','Morphology'); 
SELECT insert_unit_of_measure('','Morphology'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Morphology'); 
SELECT add_test_result_type('Morphology','D', 'Normal'); 
SELECT add_test_result_type('Morphology','D', 'Abnornal'); 
-- Begin Line: 202 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Semen'); 
SELECT create_relationship_panel_sampletype('Semen Analysis','Semen'); 
SELECT create_relationship_panel_test('Semen Analysis','Parasite'); 
SELECT create_relationship_sample_test('Semen','Parasite'); 
SELECT insert_unit_of_measure('per hpf','Parasite'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Parasite'); 
SELECT add_test_result_type('Parasite','D', 'Present'); 
SELECT add_test_result_type('Parasite','D', 'Absent'); 
-- Begin Line: 203 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Cross Match','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Cross Match','Patient Blood Group'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Patient Blood Group'); 
SELECT insert_unit_of_measure('','Patient Blood Group'); 
SELECT create_relationship_test_section_test('Haematology','Patient Blood Group'); 
SELECT add_test_result_type('Patient Blood Group','D', 'O+'); 
SELECT add_test_result_type('Patient Blood Group','D', 'A+'); 
SELECT add_test_result_type('Patient Blood Group','D', 'B+'); 
SELECT add_test_result_type('Patient Blood Group','D', 'AB+'); 
SELECT add_test_result_type('Patient Blood Group','D', 'O-'); 
SELECT add_test_result_type('Patient Blood Group','D', 'A-'); 
SELECT add_test_result_type('Patient Blood Group','D', 'B-'); 
SELECT add_test_result_type('Patient Blood Group','D', 'AB-'); 
-- Begin Line: 204 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('CSF','Body Fluid'); 
SELECT create_relationship_panel_test('CSF','Pendy Reagent Test'); 
SELECT create_relationship_sample_test('Body Fluid','Pendy Reagent Test'); 
SELECT insert_unit_of_measure('','Pendy Reagent Test'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Pendy Reagent Test'); 
SELECT add_test_result_type('Pendy Reagent Test','D', 'Positive'); 
SELECT add_test_result_type('Pendy Reagent Test','D', 'Negative'); 
-- Begin Line: 205 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Malaria','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Malaria','PS for Malaria Parasites'); 
SELECT create_relationship_sample_test('Blood (EDTA)','PS for Malaria Parasites'); 
SELECT insert_unit_of_measure('NA','PS for Malaria Parasites'); 
SELECT create_relationship_test_section_test('Haematology','PS for Malaria Parasites'); 
SELECT add_test_result_type('PS for Malaria Parasites','D', 'Positive'); 
SELECT add_test_result_type('PS for Malaria Parasites','D', 'Negative'); 
-- Begin Line: 206 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Microfilaria','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Microfilaria','PS for mf by concentration'); 
SELECT create_relationship_sample_test('Blood (EDTA)','PS for mf by concentration'); 
SELECT insert_unit_of_measure('NA','PS for mf by concentration'); 
SELECT create_relationship_test_section_test('Haematology','PS for mf by concentration'); 
SELECT add_test_result_type('PS for mf by concentration','D', 'Positive'); 
SELECT add_test_result_type('PS for mf by concentration','D', 'Negative'); 
-- Begin Line: 207 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Environment','Stool'); 
SELECT create_relationship_panel_test('Environment','Reducing substance'); 
SELECT create_relationship_sample_test('Stool','Reducing substance'); 
SELECT insert_unit_of_measure('NA','Reducing substance'); 
SELECT create_relationship_test_section_test('Stool','Reducing substance'); 
SELECT add_test_result_type('Reducing substance','D', 'Negative'); 
SELECT add_test_result_type('Reducing substance','D', 'Positive'); 
SELECT add_test_result_type('Reducing substance','D', '1+'); 
SELECT add_test_result_type('Reducing substance','D', '2+'); 
SELECT add_test_result_type('Reducing substance','D', '3+'); 
SELECT add_test_result_type('Reducing substance','D', '4+'); 
-- Begin Line: 208 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Semen'); 
SELECT create_relationship_panel_sampletype('Semen Analysis','Semen'); 
SELECT create_relationship_panel_test('Semen Analysis','Result'); 
SELECT create_relationship_sample_test('Semen','Result'); 
SELECT insert_unit_of_measure('','Result'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Result'); 
SELECT add_test_result_type('Result','D', 'Normo Spermia'); 
SELECT add_test_result_type('Result','D', 'Oligo Spermia'); 
SELECT add_test_result_type('Result','D', 'Azo Spermia'); 
-- Begin Line: 209 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Serology','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Serology','Rheumatoid Arthritis - Qualitative'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Rheumatoid Arthritis - Qualitative'); 
SELECT insert_unit_of_measure('NA','Rheumatoid Arthritis - Qualitative'); 
SELECT create_relationship_test_section_test('Serology','Rheumatoid Arthritis - Qualitative'); 
SELECT add_test_result_type('Rheumatoid Arthritis - Qualitative','D', 'Positive 1:2'); 
SELECT add_test_result_type('Rheumatoid Arthritis - Qualitative','D', 'Positive 1:4'); 
SELECT add_test_result_type('Rheumatoid Arthritis - Qualitative','D', 'Positive 1:8'); 
SELECT add_test_result_type('Rheumatoid Arthritis - Qualitative','D', 'Positive 1:16'); 
SELECT add_test_result_type('Rheumatoid Arthritis - Qualitative','D', 'Positive 1:32'); 
SELECT add_test_result_type('Rheumatoid Arthritis - Qualitative','D', 'Negative'); 
-- Begin Line: 210 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Sickling Only','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Sickling Only','Sickling Test'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Sickling Test'); 
SELECT insert_unit_of_measure('NA','Sickling Test'); 
SELECT create_relationship_test_section_test('Haematology','Sickling Test'); 
SELECT add_test_result_type('Sickling Test','D', 'Positive'); 
SELECT add_test_result_type('Sickling Test','D', 'Negative'); 
-- Begin Line: 211 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Anaemia Panel','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Anaemia Panel','Sickling Test'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Sickling Test'); 
SELECT insert_unit_of_measure('NA','Sickling Test'); 
SELECT create_relationship_test_section_test('Haematology','Sickling Test'); 
SELECT add_test_result_type('Sickling Test','D', 'Positive'); 
SELECT add_test_result_type('Sickling Test','D', 'Negative'); 
-- Begin Line: 212 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Stool occult blood','Stool'); 
SELECT create_relationship_panel_test('Stool occult blood','Stool occult blood'); 
SELECT create_relationship_sample_test('Stool','Stool occult blood'); 
SELECT insert_unit_of_measure('NA','Stool occult blood'); 
SELECT create_relationship_test_section_test('Stool','Stool occult blood'); 
SELECT add_test_result_type('Stool occult blood','D', 'Negative'); 
SELECT add_test_result_type('Stool occult blood','D', 'Positive'); 
SELECT add_test_result_type('Stool occult blood','D', '1+'); 
SELECT add_test_result_type('Stool occult blood','D', '2+'); 
SELECT add_test_result_type('Stool occult blood','D', '3+'); 
SELECT add_test_result_type('Stool occult blood','D', '4+'); 
-- Begin Line: 213 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Routine Urine','Urine'); 
SELECT create_relationship_panel_test('Routine Urine','Sugar'); 
SELECT create_relationship_sample_test('Urine','Sugar'); 
SELECT insert_unit_of_measure('NA','Sugar'); 
SELECT create_relationship_test_section_test('Urine','Sugar'); 
SELECT add_test_result_type('Sugar','D', 'Nil'); 
SELECT add_test_result_type('Sugar','D', 'Trace'); 
SELECT add_test_result_type('Sugar','D', '1+'); 
SELECT add_test_result_type('Sugar','D', '2+'); 
SELECT add_test_result_type('Sugar','D', '3+'); 
SELECT add_test_result_type('Sugar','D', '4+'); 
-- Begin Line: 214 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Serology','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Serology','Troponin - T'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Troponin - T'); 
SELECT insert_unit_of_measure('NA','Troponin - T'); 
SELECT create_relationship_test_section_test('Serology','Troponin - T'); 
SELECT add_test_result_type('Troponin - T','D', 'Positive'); 
SELECT add_test_result_type('Troponin - T','D', 'Negative'); 
-- Begin Line: 215 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Bile Pigment','Urine'); 
SELECT create_relationship_panel_test('Bile Pigment','Urine Bile Pigment'); 
SELECT create_relationship_sample_test('Urine','Urine Bile Pigment'); 
SELECT insert_unit_of_measure('NA','Urine Bile Pigment'); 
SELECT create_relationship_test_section_test('Urine','Urine Bile Pigment'); 
SELECT add_test_result_type('Urine Bile Pigment','D', 'Negative'); 
SELECT add_test_result_type('Urine Bile Pigment','D', 'Positive'); 
SELECT add_test_result_type('Urine Bile Pigment','D', '1+'); 
SELECT add_test_result_type('Urine Bile Pigment','D', '2+'); 
SELECT add_test_result_type('Urine Bile Pigment','D', '3+'); 
SELECT add_test_result_type('Urine Bile Pigment','D', '4+'); 
-- Begin Line: 216 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Bile Salt','Urine'); 
SELECT create_relationship_panel_test('Bile Salt','Urine Bile Salt'); 
SELECT create_relationship_sample_test('Urine','Urine Bile Salt'); 
SELECT insert_unit_of_measure('NA','Urine Bile Salt'); 
SELECT create_relationship_test_section_test('Urine','Urine Bile Salt'); 
SELECT add_test_result_type('Urine Bile Salt','D', 'Negative'); 
SELECT add_test_result_type('Urine Bile Salt','D', 'Positive'); 
SELECT add_test_result_type('Urine Bile Salt','D', '1+'); 
SELECT add_test_result_type('Urine Bile Salt','D', '2+'); 
SELECT add_test_result_type('Urine Bile Salt','D', '3+'); 
SELECT add_test_result_type('Urine Bile Salt','D', '4+'); 
-- Begin Line: 217 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Ketone','Urine'); 
SELECT create_relationship_panel_test('Ketone','Urine Ketone'); 
SELECT create_relationship_sample_test('Urine','Urine Ketone'); 
SELECT insert_unit_of_measure('NA','Urine Ketone'); 
SELECT create_relationship_test_section_test('Urine','Urine Ketone'); 
SELECT add_test_result_type('Urine Ketone','D', 'Nil'); 
SELECT add_test_result_type('Urine Ketone','D', 'Trace'); 
SELECT add_test_result_type('Urine Ketone','D', '1+'); 
SELECT add_test_result_type('Urine Ketone','D', '2+'); 
SELECT add_test_result_type('Urine Ketone','D', '3+'); 
SELECT add_test_result_type('Urine Ketone','D', '4+'); 
-- Begin Line: 218 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Urine Pregnancy Test','Urine'); 
SELECT create_relationship_panel_test('Urine Pregnancy Test','Urine Pregnancy Test'); 
SELECT create_relationship_sample_test('Urine','Urine Pregnancy Test'); 
SELECT insert_unit_of_measure('','Urine Pregnancy Test'); 
SELECT create_relationship_test_section_test('Urine','Urine Pregnancy Test'); 
SELECT add_test_result_type('Urine Pregnancy Test','D', 'Positive'); 
SELECT add_test_result_type('Urine Pregnancy Test','D', 'Negative'); 
-- Begin Line: 219 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Serology','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Serology','VDRL ELISA'); 
SELECT create_relationship_sample_test('Blood (EDTA)','VDRL ELISA'); 
SELECT insert_unit_of_measure('NA','VDRL ELISA'); 
SELECT create_relationship_test_section_test('Serology','VDRL ELISA'); 
SELECT add_test_result_type('VDRL ELISA','D', 'Positive'); 
SELECT add_test_result_type('VDRL ELISA','D', 'Negative'); 
-- Begin Line: 220 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Cross Match','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Cross Match','VDRL ELISA'); 
SELECT create_relationship_sample_test('Blood (EDTA)','VDRL ELISA'); 
SELECT insert_unit_of_measure('NA','VDRL ELISA'); 
SELECT create_relationship_test_section_test('Serology','VDRL ELISA'); 
SELECT add_test_result_type('VDRL ELISA','D', 'Positive'); 
SELECT add_test_result_type('VDRL ELISA','D', 'Negative'); 
-- Begin Line: 221 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Serology','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Serology','VDRL Rapid'); 
SELECT create_relationship_sample_test('Blood (EDTA)','VDRL Rapid'); 
SELECT insert_unit_of_measure('NA','VDRL Rapid'); 
SELECT create_relationship_test_section_test('Serology','VDRL Rapid'); 
SELECT add_test_result_type('VDRL Rapid','D', 'Positive'); 
SELECT add_test_result_type('VDRL Rapid','D', 'Negative'); 
-- Begin Line: 222 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Cross Match','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Cross Match','VDRL Rapid'); 
SELECT create_relationship_sample_test('Blood (EDTA)','VDRL Rapid'); 
SELECT insert_unit_of_measure('NA','VDRL Rapid'); 
SELECT create_relationship_test_section_test('Serology','VDRL Rapid'); 
SELECT add_test_result_type('VDRL Rapid','D', 'Positive'); 
SELECT add_test_result_type('VDRL Rapid','D', 'Negative'); 
-- Begin Line: 223 
SELECT insert_test_section('Vaginal'); 
SELECT insert_sample_type('vaginal'); 
SELECT create_relationship_panel_sampletype('Vaginal Panel','vaginal'); 
SELECT create_relationship_panel_test('Vaginal Panel','Whiff test'); 
SELECT create_relationship_sample_test('vaginal','Whiff test'); 
SELECT insert_unit_of_measure('NA','Whiff test'); 
SELECT create_relationship_test_section_test('Vaginal','Whiff test'); 
SELECT add_test_result_type('Whiff test','D', 'Positive'); 
SELECT add_test_result_type('Whiff test','D', 'Negative'); 
-- Begin Line: 224 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Serology','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Serology','WIDAL Qualitative'); 
SELECT create_relationship_sample_test('Blood (EDTA)','WIDAL Qualitative'); 
SELECT insert_unit_of_measure('NA','WIDAL Qualitative'); 
SELECT create_relationship_test_section_test('Serology','WIDAL Qualitative'); 
SELECT add_test_result_type('WIDAL Qualitative','D', 'Positive 1:40'); 
SELECT add_test_result_type('WIDAL Qualitative','D', 'Positive 1:80'); 
SELECT add_test_result_type('WIDAL Qualitative','D', 'Positive 1:160'); 
SELECT add_test_result_type('WIDAL Qualitative','D', 'Positive 1:320'); 
SELECT add_test_result_type('WIDAL Qualitative','D', 'Negative (&lt'); 
SELECT add_test_result_type('WIDAL Qualitative','D', ' 40)'); 
-- Begin Line: 225 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('ZN Stain','Urine'); 
SELECT create_relationship_panel_test('ZN Stain','ZN Stain'); 
SELECT create_relationship_sample_test('Urine','ZN Stain'); 
SELECT insert_unit_of_measure('NA','ZN Stain'); 
SELECT create_relationship_test_section_test('Clinical Pathology','ZN Stain'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 1+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 2+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 3+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 4+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 5+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 6+'); 
SELECT add_test_result_type('ZN Stain','D', 'Negative'); 
-- Begin Line: 226 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('ZN Stain','Stool'); 
SELECT create_relationship_panel_test('ZN Stain','ZN Stain'); 
SELECT create_relationship_sample_test('Stool','ZN Stain'); 
SELECT insert_unit_of_measure('NA','ZN Stain'); 
SELECT create_relationship_test_section_test('Clinical Pathology','ZN Stain'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 1+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 2+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 3+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 4+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 5+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 6+'); 
SELECT add_test_result_type('ZN Stain','D', 'Negative'); 
-- Begin Line: 227 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Sputum'); 
SELECT create_relationship_panel_sampletype('ZN Stain','Sputum'); 
SELECT create_relationship_panel_test('ZN Stain','ZN Stain'); 
SELECT create_relationship_sample_test('Sputum','ZN Stain'); 
SELECT insert_unit_of_measure('NA','ZN Stain'); 
SELECT create_relationship_test_section_test('Clinical Pathology','ZN Stain'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 1+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 2+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 3+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 4+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 5+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 6+'); 
SELECT add_test_result_type('ZN Stain','D', 'Negative'); 
-- Begin Line: 228 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('ZN Stain','Body Fluid'); 
SELECT create_relationship_panel_test('ZN Stain','ZN Stain'); 
SELECT create_relationship_sample_test('Body Fluid','ZN Stain'); 
SELECT insert_unit_of_measure('NA','ZN Stain'); 
SELECT create_relationship_test_section_test('Clinical Pathology','ZN Stain'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 1+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 2+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 3+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 4+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 5+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 6+'); 
SELECT add_test_result_type('ZN Stain','D', 'Negative'); 
-- Begin Line: 229 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Pus'); 
SELECT create_relationship_panel_sampletype('ZN Stain','Pus'); 
SELECT create_relationship_panel_test('ZN Stain','ZN Stain'); 
SELECT create_relationship_sample_test('Pus','ZN Stain'); 
SELECT insert_unit_of_measure('NA','ZN Stain'); 
SELECT create_relationship_test_section_test('Clinical Pathology','ZN Stain'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 1+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 2+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 3+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 4+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 5+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 6+'); 
SELECT add_test_result_type('ZN Stain','D', 'Negative'); 
-- Begin Line: 230 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Slit Skin'); 
SELECT create_relationship_panel_sampletype('ZN Stain','Slit Skin'); 
SELECT create_relationship_panel_test('ZN Stain','ZN Stain'); 
SELECT create_relationship_sample_test('Slit Skin','ZN Stain'); 
SELECT insert_unit_of_measure('NA','ZN Stain'); 
SELECT create_relationship_test_section_test('Clinical Pathology','ZN Stain'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 1+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 2+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 3+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 4+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 5+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 6+'); 
SELECT add_test_result_type('ZN Stain','D', 'Negative'); 
-- Begin Line: 231 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('CSF','Body Fluid'); 
SELECT create_relationship_panel_test('CSF','ZN Stain'); 
SELECT create_relationship_sample_test('Body Fluid','ZN Stain'); 
SELECT insert_unit_of_measure('','ZN Stain'); 
SELECT create_relationship_test_section_test('Clinical Pathology','ZN Stain'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 1+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 2+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 3+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 4+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 5+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 6+'); 
SELECT add_test_result_type('ZN Stain','D', 'Negative'); 
-- Begin Line: 232 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Asitic Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Asitic Fluid','ZN Stain'); 
SELECT create_relationship_sample_test('Body Fluid','ZN Stain'); 
SELECT insert_unit_of_measure('','ZN Stain'); 
SELECT create_relationship_test_section_test('Clinical Pathology','ZN Stain'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 1+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 2+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 3+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 4+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 5+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 6+'); 
SELECT add_test_result_type('ZN Stain','D', 'Negative'); 
-- Begin Line: 233 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Pyrotinial Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Pyrotinial Fluid','ZN Stain'); 
SELECT create_relationship_sample_test('Body Fluid','ZN Stain'); 
SELECT insert_unit_of_measure('','ZN Stain'); 
SELECT create_relationship_test_section_test('Clinical Pathology','ZN Stain'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 1+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 2+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 3+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 4+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 5+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 6+'); 
SELECT add_test_result_type('ZN Stain','D', 'Negative'); 
-- Begin Line: 234 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Pus','Body Fluid'); 
SELECT create_relationship_panel_test('Pus','ZN Stain'); 
SELECT create_relationship_sample_test('Body Fluid','ZN Stain'); 
SELECT insert_unit_of_measure('','ZN Stain'); 
SELECT create_relationship_test_section_test('Clinical Pathology','ZN Stain'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 1+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 2+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 3+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 4+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 5+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 6+'); 
SELECT add_test_result_type('ZN Stain','D', 'Negative'); 
-- Begin Line: 235 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Knee Joint Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Knee Joint Fluid','ZN Stain'); 
SELECT create_relationship_sample_test('Body Fluid','ZN Stain'); 
SELECT insert_unit_of_measure('','ZN Stain'); 
SELECT create_relationship_test_section_test('Clinical Pathology','ZN Stain'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 1+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 2+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 3+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 4+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 5+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 6+'); 
SELECT add_test_result_type('ZN Stain','D', 'Negative'); 
-- Begin Line: 236 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Plural Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Plural Fluid','ZN Stain'); 
SELECT create_relationship_sample_test('Body Fluid','ZN Stain'); 
SELECT insert_unit_of_measure('','ZN Stain'); 
SELECT create_relationship_test_section_test('Clinical Pathology','ZN Stain'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 1+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 2+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 3+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 4+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 5+'); 
SELECT add_test_result_type('ZN Stain','D', 'Positive 6+'); 
SELECT add_test_result_type('ZN Stain','D', 'Negative'); 
