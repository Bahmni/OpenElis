-- This file is generated automatically. If you need to change it, change the spreedsheet and run the script create_migration_data.pl 


-- Begin Line: 2 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Anaemia Panel','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Anaemia Panel','Absolute Eosinphil Count'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Absolute Eosinphil Count'); 
SELECT insert_unit_of_measure('cumm','Absolute Eosinphil Count'); 
SELECT create_relationship_test_section_test('Haematology','Absolute Eosinphil Count'); 
SELECT insert_result_limit_normal_range('Absolute Eosinphil Count',100,600); 
SELECT insert_result_limit_valid_range('Absolute Eosinphil Count',25,30000); 
-- Begin Line: 3 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Routine Blood','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Routine Blood','Absolute Eosinphil Count'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Absolute Eosinphil Count'); 
SELECT insert_unit_of_measure('cumm','Absolute Eosinphil Count'); 
SELECT create_relationship_test_section_test('Haematology','Absolute Eosinphil Count'); 
SELECT insert_result_limit_normal_range('Absolute Eosinphil Count',100,600); 
SELECT insert_result_limit_valid_range('Absolute Eosinphil Count',25,30000); 
-- Begin Line: 4 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Biochemistry','Serum'); 
SELECT create_relationship_panel_test('Biochemistry','AFP'); 
SELECT create_relationship_sample_test('Serum','AFP'); 
SELECT insert_unit_of_measure('U/MM','AFP'); 
SELECT create_relationship_test_section_test('Serology','AFP'); 
SELECT insert_result_limit_normal_range('AFP',0,8); 
SELECT insert_result_limit_valid_range('AFP',0,12); 
-- Begin Line: 5 
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
-- Begin Line: 6 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Liver Function Test','Serum'); 
SELECT create_relationship_panel_test('Liver Function Test','ALK. Phosphate'); 
SELECT create_relationship_sample_test('Serum','ALK. Phosphate'); 
SELECT insert_unit_of_measure('u/L','ALK. Phosphate'); 
SELECT create_relationship_test_section_test('Biochemistry','ALK. Phosphate'); 
SELECT insert_result_limit_normal_range('ALK. Phosphate',0,135); 
SELECT insert_result_limit_valid_range('ALK. Phosphate',0,2000); 
-- Begin Line: 7 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Liver Function Test','Serum'); 
SELECT create_relationship_panel_test('Liver Function Test','Amylase'); 
SELECT create_relationship_sample_test('Serum','Amylase'); 
SELECT insert_unit_of_measure('u/L','Amylase'); 
SELECT create_relationship_test_section_test('Biochemistry','Amylase'); 
SELECT insert_result_limit_normal_range('Amylase',25,125); 
SELECT insert_result_limit_valid_range('Amylase',10,2000); 
-- Begin Line: 8 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Biochemistry','Serum'); 
SELECT create_relationship_panel_test('Biochemistry','ANA'); 
SELECT create_relationship_sample_test('Serum','ANA'); 
SELECT insert_unit_of_measure('od ratio','ANA'); 
SELECT create_relationship_test_section_test('Biochemistry','ANA'); 
SELECT insert_result_limit_normal_range('ANA',0,1); 
SELECT insert_result_limit_valid_range('ANA',0,1); 
-- Begin Line: 9 
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
-- Begin Line: 10 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Serology','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Serology','Anti Streptolysin Turbidometry'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Anti Streptolysin Turbidometry'); 
SELECT insert_unit_of_measure('iU/ml','Anti Streptolysin Turbidometry'); 
SELECT create_relationship_test_section_test('Serology','Anti Streptolysin Turbidometry'); 
SELECT insert_result_limit_normal_range('Anti Streptolysin Turbidometry',5,200); 
-- Begin Line: 11 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('APTT','Serum'); 
SELECT create_relationship_panel_test('APTT','APTT (Control) (Serum)'); 
SELECT create_relationship_sample_test('Serum','APTT (Control) (Serum)'); 
SELECT insert_unit_of_measure('sec','APTT (Control) (Serum)'); 
SELECT create_relationship_test_section_test('Biochemistry','APTT (Control) (Serum)'); 
SELECT insert_result_limit_normal_range('APTT (Control) (Serum)',38,40); 
SELECT insert_result_limit_valid_range('APTT (Control) (Serum)',20,720); 
-- Begin Line: 12 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Coagulation Factor','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Coagulation Factor','APTT (Control) (Blood)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','APTT (Control) (Blood)'); 
SELECT insert_unit_of_measure('sec','APTT (Control) (Blood)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','APTT (Control) (Blood)'); 
SELECT insert_result_limit_normal_range('APTT (Control) (Blood)',38,40); 
SELECT insert_result_limit_valid_range('APTT (Control) (Blood)',20,720); 
-- Begin Line: 13 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('APTT','Serum'); 
SELECT create_relationship_panel_test('APTT','APTT (Test) (Serum)'); 
SELECT create_relationship_sample_test('Serum','APTT (Test) (Serum)'); 
SELECT insert_unit_of_measure('sec','APTT (Test) (Serum)'); 
SELECT create_relationship_test_section_test('Biochemistry','APTT (Test) (Serum)'); 
SELECT insert_result_limit_normal_range('APTT (Test) (Serum)',38,40); 
SELECT insert_result_limit_valid_range('APTT (Test) (Serum)',20,720); 
-- Begin Line: 14 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Coagulation Factor','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Coagulation Factor','APTT (Test) (Blood)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','APTT (Test) (Blood)'); 
SELECT insert_unit_of_measure('sec','APTT (Test) (Blood)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','APTT (Test) (Blood)'); 
SELECT insert_result_limit_normal_range('APTT (Test) (Blood)',38,40); 
SELECT insert_result_limit_valid_range('APTT (Test) (Blood)',20,720); 
-- Begin Line: 15 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Biochemistry','Serum'); 
SELECT create_relationship_panel_test('Biochemistry','B HCG'); 
SELECT create_relationship_sample_test('Serum','B HCG'); 
SELECT insert_unit_of_measure('U/MM','B HCG'); 
SELECT create_relationship_test_section_test('Biochemistry','B HCG'); 
-- Begin Line: 16 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Differential Count','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Differential Count','Basophil'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Basophil'); 
SELECT insert_unit_of_measure('%','Basophil'); 
SELECT create_relationship_test_section_test('Haematology','Basophil'); 
SELECT insert_result_limit_normal_range('Basophil',0,1); 
SELECT insert_result_limit_valid_range('Basophil',0,2); 
-- Begin Line: 17 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_sample_test('Urine','Bence Jones Protein'); 
SELECT insert_unit_of_measure('','Bence Jones Protein'); 
SELECT create_relationship_test_section_test('Urine','Bence Jones Protein'); 
SELECT add_test_result_type('Bence Jones Protein','D', 'Positive'); 
SELECT add_test_result_type('Bence Jones Protein','D', 'Negative'); 
-- Begin Line: 18 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Biochemistry','Serum'); 
SELECT create_relationship_panel_test('Biochemistry','Bicarbonate'); 
SELECT create_relationship_sample_test('Serum','Bicarbonate'); 
SELECT insert_unit_of_measure('me/ql','Bicarbonate'); 
SELECT create_relationship_test_section_test('Biochemistry','Bicarbonate'); 
SELECT insert_result_limit_normal_range('Bicarbonate',23,29); 
SELECT insert_result_limit_valid_range('Bicarbonate',10,60); 
-- Begin Line: 19 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Coagulation Factor','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Coagulation Factor','Bleeding Time'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Bleeding Time'); 
SELECT insert_unit_of_measure('sec','Bleeding Time'); 
SELECT create_relationship_test_section_test('Haematology','Bleeding Time'); 
-- Begin Line: 20 
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
-- Begin Line: 21 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Biochemistry','Serum'); 
SELECT create_relationship_panel_test('Biochemistry','Blood Urea'); 
SELECT create_relationship_sample_test('Serum','Blood Urea'); 
SELECT insert_unit_of_measure('ng/dl','Blood Urea'); 
SELECT create_relationship_test_section_test('Biochemistry','Blood Urea'); 
SELECT insert_result_limit_normal_range('Blood Urea',20,40); 
SELECT insert_result_limit_valid_range('Blood Urea',10,1000); 
-- Begin Line: 22 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Biochemistry','Serum'); 
SELECT create_relationship_panel_test('Biochemistry','CA-125'); 
SELECT create_relationship_sample_test('Serum','CA-125'); 
SELECT insert_unit_of_measure('u/mm','CA-125'); 
SELECT create_relationship_test_section_test('Biochemistry','CA-125'); 
SELECT insert_result_limit_normal_range('CA-125',5,35); 
SELECT insert_result_limit_valid_range('CA-125',5,200); 
-- Begin Line: 23 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Biochemistry','Serum'); 
SELECT create_relationship_panel_test('Biochemistry','CA19-9'); 
SELECT create_relationship_sample_test('Serum','CA19-9'); 
SELECT insert_unit_of_measure('u/ml','CA19-9'); 
SELECT create_relationship_test_section_test('Biochemistry','CA19-9'); 
SELECT insert_result_limit_normal_range('CA19-9',0,27); 
-- Begin Line: 24 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Electrolyte','Serum'); 
SELECT create_relationship_panel_test('Electrolyte','Calcium (mmole/l)'); 
SELECT create_relationship_sample_test('Serum','Calcium (mmole/l)'); 
SELECT insert_unit_of_measure('mmole/l','Calcium (mmole/l)'); 
SELECT create_relationship_test_section_test('Biochemistry','Calcium (mmole/l)'); 
SELECT insert_result_limit_normal_range('Calcium (mmole/l)',1,1.20); 
SELECT insert_result_limit_valid_range('Calcium (mmole/l)',0.40,3); 
-- Begin Line: 25 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_sample_test('Serum','Calcium(mg/dl)'); 
SELECT insert_unit_of_measure('mg/dl','Calcium(mg/dl)'); 
SELECT create_relationship_test_section_test('Biochemistry','Calcium(mg/dl)'); 
SELECT insert_result_limit_normal_range('Calcium(mg/dl)',8.40,10.60); 
SELECT insert_result_limit_valid_range('Calcium(mg/dl)',2,22); 
-- Begin Line: 26 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Urine Microscopy','Urine'); 
SELECT create_relationship_panel_test('Urine Microscopy','Cast'); 
SELECT create_relationship_sample_test('Urine','Cast'); 
SELECT insert_unit_of_measure('/hPF','Cast'); 
SELECT create_relationship_test_section_test('Urine','Cast'); 
SELECT add_test_result_type('Cast','R'); 
-- Begin Line: 27 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','CD4 Test'); 
SELECT insert_unit_of_measure('','CD4 Test'); 
SELECT create_relationship_test_section_test('Haematology','CD4 Test'); 
SELECT add_test_result_type('CD4 Test','R'); 
-- Begin Line: 28 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Lipid Profile','Serum'); 
SELECT create_relationship_panel_test('Lipid Profile','Cholesterol'); 
SELECT create_relationship_sample_test('Serum','Cholesterol'); 
SELECT insert_unit_of_measure('mg/dl','Cholesterol'); 
SELECT create_relationship_test_section_test('Biochemistry','Cholesterol'); 
SELECT insert_result_limit_normal_range('Cholesterol',150,200); 
SELECT insert_result_limit_valid_range('Cholesterol',50,600); 
-- Begin Line: 29 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Coagulation Factor','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Coagulation Factor','Clotting Time'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Clotting Time'); 
SELECT insert_unit_of_measure('sec','Clotting Time'); 
SELECT create_relationship_test_section_test('Haematology','Clotting Time'); 
-- Begin Line: 30 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_sample_test('Stool','Coccidians'); 
SELECT insert_unit_of_measure('NA','Coccidians'); 
SELECT create_relationship_test_section_test('Stool','Coccidians'); 
SELECT add_test_result_type('Coccidians','R'); 
-- Begin Line: 31 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Stool Microscopy w/o Concetration','Stool'); 
SELECT create_relationship_panel_test('Stool Microscopy w/o Concetration','Color (w/o concentration)'); 
SELECT create_relationship_sample_test('Stool','Color (w/o concentration)'); 
SELECT insert_unit_of_measure('NA','Color (w/o concentration)'); 
SELECT create_relationship_test_section_test('Stool','Color (w/o concentration)'); 
SELECT add_test_result_type('Color (w/o concentration)','R'); 
-- Begin Line: 32 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Stool Microscopy with Concetration','Stool'); 
SELECT create_relationship_panel_test('Stool Microscopy with Concetration','Color (with concentration)'); 
SELECT create_relationship_sample_test('Stool','Color (with concentration)'); 
SELECT insert_unit_of_measure('NA','Color (with concentration)'); 
SELECT create_relationship_test_section_test('Stool','Color (with concentration)'); 
SELECT add_test_result_type('Color (with concentration)','R'); 
-- Begin Line: 33 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Semen'); 
SELECT create_relationship_panel_sampletype('Semen Analysis','Semen'); 
SELECT create_relationship_panel_test('Semen Analysis','Colour'); 
SELECT create_relationship_sample_test('Semen','Colour'); 
SELECT insert_unit_of_measure('','Colour'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Colour'); 
SELECT add_test_result_type('Colour','R'); 
-- Begin Line: 34 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Haematology','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Haematology','Coombs Test (Direct)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Coombs Test (Direct)'); 
SELECT insert_unit_of_measure('','Coombs Test (Direct)'); 
SELECT create_relationship_test_section_test('Biochemistry','Coombs Test (Direct)'); 
SELECT add_test_result_type('Coombs Test (Direct)','D', 'Positive'); 
SELECT add_test_result_type('Coombs Test (Direct)','D', 'Negative'); 
-- Begin Line: 35 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Haematology','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Haematology','Coombs Test (Indirect)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Coombs Test (Indirect)'); 
SELECT insert_unit_of_measure('','Coombs Test (Indirect)'); 
SELECT create_relationship_test_section_test('Biochemistry','Coombs Test (Indirect)'); 
SELECT add_test_result_type('Coombs Test (Indirect)','D', 'Positive'); 
SELECT add_test_result_type('Coombs Test (Indirect)','D', 'Negative'); 
-- Begin Line: 36 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Biochemistry','Serum'); 
SELECT create_relationship_panel_test('Biochemistry','Creatinine'); 
SELECT create_relationship_sample_test('Serum','Creatinine'); 
SELECT insert_unit_of_measure('mg/dl','Creatinine'); 
SELECT create_relationship_test_section_test('Biochemistry','Creatinine'); 
SELECT insert_result_limit_normal_range('Creatinine',0.60,1.20); 
SELECT insert_result_limit_valid_range('Creatinine',0.20,30); 
-- Begin Line: 37 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Biochemistry','Serum'); 
SELECT create_relationship_panel_test('Biochemistry','Creatinine Kinase'); 
SELECT create_relationship_sample_test('Serum','Creatinine Kinase'); 
SELECT insert_unit_of_measure('u/L','Creatinine Kinase'); 
SELECT create_relationship_test_section_test('Biochemistry','Creatinine Kinase'); 
SELECT insert_result_limit_normal_range('Creatinine Kinase',20,115); 
SELECT insert_result_limit_valid_range('Creatinine Kinase',5,500); 
-- Begin Line: 38 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Cross Match','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Cross Match','Cross Match'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Cross Match'); 
SELECT insert_unit_of_measure('NA','Cross Match'); 
SELECT create_relationship_test_section_test('Serology','Cross Match'); 
SELECT add_test_result_type('Cross Match','D', 'OK'); 
SELECT add_test_result_type('Cross Match','D', 'Not OK'); 
-- Begin Line: 39 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Urine Microscopy','Urine'); 
SELECT create_relationship_panel_test('Urine Microscopy','Crystal'); 
SELECT create_relationship_sample_test('Urine','Crystal'); 
SELECT insert_unit_of_measure('/hPF','Crystal'); 
SELECT create_relationship_test_section_test('Urine','Crystal'); 
SELECT add_test_result_type('Crystal','R'); 
-- Begin Line: 40 
SELECT insert_test_section('Microbiology'); 
SELECT insert_sample_type('Sputum'); 
SELECT create_relationship_sample_test('Sputum','Culture (Sputum)'); 
SELECT insert_unit_of_measure('','Culture (Sputum)'); 
SELECT create_relationship_test_section_test('Microbiology','Culture (Sputum)'); 
SELECT add_test_result_type('Culture (Sputum)','R'); 
-- Begin Line: 41 
SELECT insert_test_section('Microbiology'); 
SELECT insert_sample_type('Tissue'); 
SELECT create_relationship_sample_test('Tissue','Culture (Tissue)'); 
SELECT insert_unit_of_measure('','Culture (Tissue)'); 
SELECT create_relationship_test_section_test('Microbiology','Culture (Tissue)'); 
SELECT add_test_result_type('Culture (Tissue)','R'); 
-- Begin Line: 42 
SELECT insert_test_section('Microbiology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Culture (Blood)'); 
SELECT insert_unit_of_measure('','Culture (Blood)'); 
SELECT create_relationship_test_section_test('Microbiology','Culture (Blood)'); 
SELECT add_test_result_type('Culture (Blood)','R'); 
-- Begin Line: 43 
SELECT insert_test_section('Microbiology'); 
SELECT insert_sample_type('Semen'); 
SELECT create_relationship_sample_test('Semen','Culture (Semen)'); 
SELECT insert_unit_of_measure('','Culture (Semen)'); 
SELECT create_relationship_test_section_test('Microbiology','Culture (Semen)'); 
SELECT add_test_result_type('Culture (Semen)','R'); 
-- Begin Line: 44 
SELECT insert_test_section('Microbiology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Asitic Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Asitic Fluid','Culture (Asitic Fluid)'); 
SELECT create_relationship_sample_test('Body Fluid','Culture (Asitic Fluid)'); 
SELECT insert_unit_of_measure('','Culture (Asitic Fluid)'); 
SELECT create_relationship_test_section_test('Microbiology','Culture (Asitic Fluid)'); 
SELECT add_test_result_type('Culture (Asitic Fluid)','R'); 
-- Begin Line: 45 
SELECT insert_test_section('Microbiology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('CSF','Body Fluid'); 
SELECT create_relationship_panel_test('CSF','Culture (CSF)'); 
SELECT create_relationship_sample_test('Body Fluid','Culture (CSF)'); 
SELECT insert_unit_of_measure('','Culture (CSF)'); 
SELECT create_relationship_test_section_test('Microbiology','Culture (CSF)'); 
SELECT add_test_result_type('Culture (CSF)','R'); 
-- Begin Line: 46 
SELECT insert_test_section('Microbiology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Knee Joint Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Knee Joint Fluid','Culture (Knee Joint Fluid)'); 
SELECT create_relationship_sample_test('Body Fluid','Culture (Knee Joint Fluid)'); 
SELECT insert_unit_of_measure('','Culture (Knee Joint Fluid)'); 
SELECT create_relationship_test_section_test('Microbiology','Culture (Knee Joint Fluid)'); 
SELECT add_test_result_type('Culture (Knee Joint Fluid)','R'); 
-- Begin Line: 47 
SELECT insert_test_section('Microbiology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Plural Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Plural Fluid','Culture (Plural Fluid)'); 
SELECT create_relationship_sample_test('Body Fluid','Culture (Plural Fluid)'); 
SELECT insert_unit_of_measure('','Culture (Plural Fluid)'); 
SELECT create_relationship_test_section_test('Microbiology','Culture (Plural Fluid)'); 
SELECT add_test_result_type('Culture (Plural Fluid)','R'); 
-- Begin Line: 48 
SELECT insert_test_section('Microbiology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Pus','Body Fluid'); 
SELECT create_relationship_panel_test('Pus','Culture (Pus)'); 
SELECT create_relationship_sample_test('Body Fluid','Culture (Pus)'); 
SELECT insert_unit_of_measure('','Culture (Pus)'); 
SELECT create_relationship_test_section_test('Microbiology','Culture (Pus)'); 
SELECT add_test_result_type('Culture (Pus)','R'); 
-- Begin Line: 49 
SELECT insert_test_section('Microbiology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Pyrotinial Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Pyrotinial Fluid','Culture (Pyrotinial Fluid)'); 
SELECT create_relationship_sample_test('Body Fluid','Culture (Pyrotinial Fluid)'); 
SELECT insert_unit_of_measure('','Culture (Pyrotinial Fluid)'); 
SELECT create_relationship_test_section_test('Microbiology','Culture (Pyrotinial Fluid)'); 
SELECT add_test_result_type('Culture (Pyrotinial Fluid)','R'); 
-- Begin Line: 50 
SELECT insert_test_section('Histo Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Asitic Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Asitic Fluid','Cytology (Asitic Fluid)'); 
SELECT create_relationship_sample_test('Body Fluid','Cytology (Asitic Fluid)'); 
SELECT insert_unit_of_measure('','Cytology (Asitic Fluid)'); 
SELECT create_relationship_test_section_test('Histo Pathology','Cytology (Asitic Fluid)'); 
SELECT add_test_result_type('Cytology (Asitic Fluid)','R'); 
-- Begin Line: 51 
SELECT insert_test_section('Histo Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('CSF','Body Fluid'); 
SELECT create_relationship_panel_test('CSF','Cytology (CSF)'); 
SELECT create_relationship_sample_test('Body Fluid','Cytology (CSF)'); 
SELECT insert_unit_of_measure('','Cytology (CSF)'); 
SELECT create_relationship_test_section_test('Histo Pathology','Cytology (CSF)'); 
SELECT add_test_result_type('Cytology (CSF)','R'); 
-- Begin Line: 52 
SELECT insert_test_section('Histo Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Knee Joint Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Knee Joint Fluid','Cytology (Knee Joint Fluid)'); 
SELECT create_relationship_sample_test('Body Fluid','Cytology (Knee Joint Fluid)'); 
SELECT insert_unit_of_measure('','Cytology (Knee Joint Fluid)'); 
SELECT create_relationship_test_section_test('Histo Pathology','Cytology (Knee Joint Fluid)'); 
SELECT add_test_result_type('Cytology (Knee Joint Fluid)','R'); 
-- Begin Line: 53 
SELECT insert_test_section('Histo Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Plural Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Plural Fluid','Cytology (Plural Fluid)'); 
SELECT create_relationship_sample_test('Body Fluid','Cytology (Plural Fluid)'); 
SELECT insert_unit_of_measure('','Cytology (Plural Fluid)'); 
SELECT create_relationship_test_section_test('Histo Pathology','Cytology (Plural Fluid)'); 
SELECT add_test_result_type('Cytology (Plural Fluid)','R'); 
-- Begin Line: 54 
SELECT insert_test_section('Histo Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Pus','Body Fluid'); 
SELECT create_relationship_panel_test('Pus','Cytology (Pus)'); 
SELECT create_relationship_sample_test('Body Fluid','Cytology (Pus)'); 
SELECT insert_unit_of_measure('','Cytology (Pus)'); 
SELECT create_relationship_test_section_test('Histo Pathology','Cytology (Pus)'); 
SELECT add_test_result_type('Cytology (Pus)','R'); 
-- Begin Line: 55 
SELECT insert_test_section('Histo Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Pyrotinial Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Pyrotinial Fluid','Cytology (Pyrotinial Fluid)'); 
SELECT create_relationship_sample_test('Body Fluid','Cytology (Pyrotinial Fluid)'); 
SELECT insert_unit_of_measure('','Cytology (Pyrotinial Fluid)'); 
SELECT create_relationship_test_section_test('Histo Pathology','Cytology (Pyrotinial Fluid)'); 
SELECT add_test_result_type('Cytology (Pyrotinial Fluid)','R'); 
-- Begin Line: 56 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Serology','Serum'); 
SELECT create_relationship_panel_test('Serology','Dengue (Elisa)'); 
SELECT create_relationship_sample_test('Serum','Dengue (Elisa)'); 
SELECT insert_unit_of_measure('','Dengue (Elisa)'); 
SELECT create_relationship_test_section_test('Serology','Dengue (Elisa)'); 
SELECT add_test_result_type('Dengue (Elisa)','D', 'Positive'); 
SELECT add_test_result_type('Dengue (Elisa)','D', 'Negative'); 
-- Begin Line: 57 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Serology','Serum'); 
SELECT create_relationship_panel_test('Serology','Dengue (Rapid)'); 
SELECT create_relationship_sample_test('Serum','Dengue (Rapid)'); 
SELECT insert_unit_of_measure('','Dengue (Rapid)'); 
SELECT create_relationship_test_section_test('Serology','Dengue (Rapid)'); 
SELECT add_test_result_type('Dengue (Rapid)','D', 'Positive'); 
SELECT add_test_result_type('Dengue (Rapid)','D', 'Negative'); 
-- Begin Line: 58 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Asitic Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Asitic Fluid','Differential Count (Asitic Fluid)'); 
SELECT create_relationship_sample_test('Body Fluid','Differential Count (Asitic Fluid)'); 
SELECT insert_unit_of_measure('','Differential Count (Asitic Fluid)'); 
SELECT create_relationship_test_section_test('Haematology','Differential Count (Asitic Fluid)'); 
SELECT add_test_result_type('Differential Count (Asitic Fluid)','R'); 
-- Begin Line: 59 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('CSF','Body Fluid'); 
SELECT create_relationship_panel_test('CSF','Differential Count (CSF)'); 
SELECT create_relationship_sample_test('Body Fluid','Differential Count (CSF)'); 
SELECT insert_unit_of_measure('','Differential Count (CSF)'); 
SELECT create_relationship_test_section_test('Haematology','Differential Count (CSF)'); 
SELECT add_test_result_type('Differential Count (CSF)','R'); 
-- Begin Line: 60 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Knee Joint Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Knee Joint Fluid','Differential Count (Knee Joint Fluid)'); 
SELECT create_relationship_sample_test('Body Fluid','Differential Count (Knee Joint Fluid)'); 
SELECT insert_unit_of_measure('','Differential Count (Knee Joint Fluid)'); 
SELECT create_relationship_test_section_test('Haematology','Differential Count (Knee Joint Fluid)'); 
SELECT add_test_result_type('Differential Count (Knee Joint Fluid)','R'); 
-- Begin Line: 61 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Plural Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Plural Fluid','Differential Count (Plural Fluid)'); 
SELECT create_relationship_sample_test('Body Fluid','Differential Count (Plural Fluid)'); 
SELECT insert_unit_of_measure('','Differential Count (Plural Fluid)'); 
SELECT create_relationship_test_section_test('Haematology','Differential Count (Plural Fluid)'); 
SELECT add_test_result_type('Differential Count (Plural Fluid)','R'); 
-- Begin Line: 62 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Pyrotinial Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Pyrotinial Fluid','Differential Count (Purotinial Fluid)'); 
SELECT create_relationship_sample_test('Body Fluid','Differential Count (Purotinial Fluid)'); 
SELECT insert_unit_of_measure('','Differential Count (Purotinial Fluid)'); 
SELECT create_relationship_test_section_test('Haematology','Differential Count (Purotinial Fluid)'); 
SELECT add_test_result_type('Differential Count (Purotinial Fluid)','R'); 
-- Begin Line: 63 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Pus','Body Fluid'); 
SELECT create_relationship_panel_test('Pus','Differential Count (Pus)'); 
SELECT create_relationship_sample_test('Body Fluid','Differential Count (Pus)'); 
SELECT insert_unit_of_measure('','Differential Count (Pus)'); 
SELECT create_relationship_test_section_test('Haematology','Differential Count (Pus)'); 
SELECT add_test_result_type('Differential Count (Pus)','R'); 
-- Begin Line: 64 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Liver Function Test','Serum'); 
SELECT create_relationship_panel_test('Liver Function Test','Direct Bilirubin'); 
SELECT create_relationship_sample_test('Serum','Direct Bilirubin'); 
SELECT insert_unit_of_measure('mg/dl','Direct Bilirubin'); 
SELECT create_relationship_test_section_test('Biochemistry','Direct Bilirubin'); 
SELECT insert_result_limit_normal_range('Direct Bilirubin',0.30,0.60); 
SELECT insert_result_limit_valid_range('Direct Bilirubin',0.20,30); 
-- Begin Line: 65 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Differential Count','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Differential Count','Eosinophil'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Eosinophil'); 
SELECT insert_unit_of_measure('%','Eosinophil'); 
SELECT create_relationship_test_section_test('Haematology','Eosinophil'); 
SELECT insert_result_limit_normal_range('Eosinophil',1,6); 
SELECT insert_result_limit_valid_range('Eosinophil',1,70); 
-- Begin Line: 66 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Urine Microscopy','Urine'); 
SELECT create_relationship_panel_test('Urine Microscopy','Epithelial Cells'); 
SELECT create_relationship_sample_test('Urine','Epithelial Cells'); 
SELECT insert_unit_of_measure('/hPF','Epithelial Cells'); 
SELECT create_relationship_test_section_test('Urine','Epithelial Cells'); 
SELECT add_test_result_type('Epithelial Cells','R'); 
-- Begin Line: 67 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('CBC','Blood (EDTA)'); 
SELECT create_relationship_panel_test('CBC','ESR'); 
SELECT create_relationship_sample_test('Blood (EDTA)','ESR'); 
SELECT insert_unit_of_measure('mm/hour','ESR'); 
SELECT create_relationship_test_section_test('Haematology','ESR'); 
SELECT insert_result_limit_normal_range('ESR',5,20); 
SELECT insert_result_limit_valid_range('ESR',5,180); 
-- Begin Line: 68 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','ESR'); 
SELECT insert_unit_of_measure('mm/hour','ESR'); 
SELECT create_relationship_test_section_test('Haematology','ESR'); 
SELECT insert_result_limit_normal_range('ESR',5,20); 
SELECT insert_result_limit_valid_range('ESR',5,180); 
-- Begin Line: 69 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Glucose Tolerance Test (GTT)','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Glucose Tolerance Test (GTT)','Fasting Blood Sugar'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Fasting Blood Sugar'); 
SELECT insert_unit_of_measure('mg/dl','Fasting Blood Sugar'); 
SELECT create_relationship_test_section_test('Biochemistry','Fasting Blood Sugar'); 
SELECT insert_result_limit_normal_range('Fasting Blood Sugar',80,120); 
SELECT insert_result_limit_valid_range('Fasting Blood Sugar',10,1000); 
-- Begin Line: 70 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_sample_test('Stool','Fat (Semi Quantitative)'); 
SELECT insert_unit_of_measure('NA','Fat (Semi Quantitative)'); 
SELECT create_relationship_test_section_test('Stool','Fat (Semi Quantitative)'); 
SELECT add_test_result_type('Fat (Semi Quantitative)','D', 'Present'); 
SELECT add_test_result_type('Fat (Semi Quantitative)','D', 'Absent'); 
-- Begin Line: 71 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Stool Microscopy w/o Concetration','Stool'); 
SELECT create_relationship_panel_test('Stool Microscopy w/o Concetration','Fat Droplets (w/o concentration)'); 
SELECT create_relationship_sample_test('Stool','Fat Droplets (w/o concentration)'); 
SELECT insert_unit_of_measure('NA','Fat Droplets (w/o concentration)'); 
SELECT create_relationship_test_section_test('Stool','Fat Droplets (w/o concentration)'); 
SELECT add_test_result_type('Fat Droplets (w/o concentration)','R'); 
-- Begin Line: 72 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Stool Microscopy with Concetration','Stool'); 
SELECT create_relationship_panel_test('Stool Microscopy with Concetration','Fat Droplets (with concentration)'); 
SELECT create_relationship_sample_test('Stool','Fat Droplets (with concentration)'); 
SELECT insert_unit_of_measure('NA','Fat Droplets (with concentration)'); 
SELECT create_relationship_test_section_test('Stool','Fat Droplets (with concentration)'); 
SELECT add_test_result_type('Fat Droplets (with concentration)','R'); 
-- Begin Line: 73 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Biochemistry','Serum'); 
SELECT create_relationship_panel_test('Biochemistry','FBS'); 
SELECT create_relationship_sample_test('Serum','FBS'); 
SELECT insert_unit_of_measure('mg/dl','FBS'); 
SELECT create_relationship_test_section_test('Biochemistry','FBS'); 
SELECT insert_result_limit_normal_range('FBS',80,120); 
SELECT insert_result_limit_valid_range('FBS',10,1000); 
-- Begin Line: 74 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Biochemistry','Serum'); 
SELECT create_relationship_panel_test('Biochemistry','Ferittin'); 
SELECT create_relationship_sample_test('Serum','Ferittin'); 
SELECT insert_unit_of_measure('ng/ml','Ferittin'); 
SELECT create_relationship_test_section_test('Biochemistry','Ferittin'); 
SELECT insert_result_limit_normal_range('Ferittin',20,200); 
SELECT insert_result_limit_valid_range('Ferittin',5,500); 
-- Begin Line: 75 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Stool Microscopy w/o Concetration','Stool'); 
SELECT create_relationship_panel_test('Stool Microscopy w/o Concetration','Food Particles (w/o concentration)'); 
SELECT create_relationship_sample_test('Stool','Food Particles (w/o concentration)'); 
SELECT insert_unit_of_measure('NA','Food Particles (w/o concentration)'); 
SELECT create_relationship_test_section_test('Stool','Food Particles (w/o concentration)'); 
SELECT add_test_result_type('Food Particles (w/o concentration)','R'); 
-- Begin Line: 76 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Stool Microscopy with Concetration','Stool'); 
SELECT create_relationship_panel_test('Stool Microscopy with Concetration','Food Particles (with concentration)'); 
SELECT create_relationship_sample_test('Stool','Food Particles (with concentration)'); 
SELECT insert_unit_of_measure('NA','Food Particles (with concentration)'); 
SELECT create_relationship_test_section_test('Stool','Food Particles (with concentration)'); 
SELECT add_test_result_type('Food Particles (with concentration)','R'); 
-- Begin Line: 77 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Biochemistry','Serum'); 
SELECT create_relationship_panel_test('Biochemistry','FT3'); 
SELECT create_relationship_sample_test('Serum','FT3'); 
SELECT insert_unit_of_measure('pg/ml','FT3'); 
SELECT create_relationship_test_section_test('Biochemistry','FT3'); 
SELECT insert_result_limit_normal_range('FT3',1.70,4.20); 
SELECT insert_result_limit_valid_range('FT3',0.50,20); 
-- Begin Line: 78 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Biochemistry','Serum'); 
SELECT create_relationship_panel_test('Biochemistry','FT4'); 
SELECT create_relationship_sample_test('Serum','FT4'); 
SELECT insert_unit_of_measure('ng/dl','FT4'); 
SELECT create_relationship_test_section_test('Biochemistry','FT4'); 
SELECT insert_result_limit_normal_range('FT4',0.70,1.80); 
SELECT insert_result_limit_valid_range('FT4',0,20); 
-- Begin Line: 79 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Haematology','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Haematology','G6PD'); 
SELECT create_relationship_sample_test('Blood (EDTA)','G6PD'); 
SELECT insert_unit_of_measure('','G6PD'); 
SELECT create_relationship_test_section_test('Biochemistry','G6PD'); 
SELECT add_test_result_type('G6PD','D', 'Positive'); 
SELECT add_test_result_type('G6PD','D', 'Negative'); 
-- Begin Line: 80 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Semen'); 
SELECT create_relationship_panel_sampletype('Semen Analysis','Semen'); 
SELECT create_relationship_panel_test('Semen Analysis','Gram Stain (Semen)'); 
SELECT create_relationship_sample_test('Semen','Gram Stain (Semen)'); 
SELECT insert_unit_of_measure('','Gram Stain (Semen)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Gram Stain (Semen)'); 
SELECT add_test_result_type('Gram Stain (Semen)','R'); 
-- Begin Line: 81 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Gram Stain (Blood)'); 
SELECT insert_unit_of_measure('NA','Gram Stain (Blood)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Gram Stain (Blood)'); 
SELECT add_test_result_type('Gram Stain (Blood)','R'); 
-- Begin Line: 82 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_sample_test('Urine','Gram Stain (Urine)'); 
SELECT insert_unit_of_measure('NA','Gram Stain (Urine)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Gram Stain (Urine)'); 
SELECT add_test_result_type('Gram Stain (Urine)','R'); 
-- Begin Line: 83 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_sample_test('Stool','Gram Stain (Stool)'); 
SELECT insert_unit_of_measure('NA','Gram Stain (Stool)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Gram Stain (Stool)'); 
SELECT add_test_result_type('Gram Stain (Stool)','R'); 
-- Begin Line: 84 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Sputum'); 
SELECT create_relationship_sample_test('Sputum','Gram Stain (Sputum)'); 
SELECT insert_unit_of_measure('NA','Gram Stain (Sputum)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Gram Stain (Sputum)'); 
SELECT add_test_result_type('Gram Stain (Sputum)','R'); 
-- Begin Line: 85 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_sample_test('Body Fluid','Gram Stain (Body Fluid)'); 
SELECT insert_unit_of_measure('NA','Gram Stain (Body Fluid)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Gram Stain (Body Fluid)'); 
SELECT add_test_result_type('Gram Stain (Body Fluid)','R'); 
-- Begin Line: 86 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Pus'); 
SELECT create_relationship_sample_test('Pus','Gram Stain (Pus)'); 
SELECT insert_unit_of_measure('NA','Gram Stain (Pus)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Gram Stain (Pus)'); 
SELECT add_test_result_type('Gram Stain (Pus)','R'); 
-- Begin Line: 87 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Slit Skin'); 
SELECT create_relationship_sample_test('Slit Skin','Gram Stain (Slit Skin)'); 
SELECT insert_unit_of_measure('NA','Gram Stain (Slit Skin)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Gram Stain (Slit Skin)'); 
SELECT add_test_result_type('Gram Stain (Slit Skin)','R'); 
-- Begin Line: 88 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Semen'); 
SELECT create_relationship_sample_test('Semen','Gram Stain (Semen)'); 
SELECT insert_unit_of_measure('NA','Gram Stain (Semen)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Gram Stain (Semen)'); 
SELECT add_test_result_type('Gram Stain (Semen)','R'); 
-- Begin Line: 89 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Asitic Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Asitic Fluid','Gram Stain (Asitic Fluid)'); 
SELECT create_relationship_sample_test('Body Fluid','Gram Stain (Asitic Fluid)'); 
SELECT insert_unit_of_measure('','Gram Stain (Asitic Fluid)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Gram Stain (Asitic Fluid)'); 
SELECT add_test_result_type('Gram Stain (Asitic Fluid)','R'); 
-- Begin Line: 90 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('CSF','Body Fluid'); 
SELECT create_relationship_panel_test('CSF','Gram Stain (CSF)'); 
SELECT create_relationship_sample_test('Body Fluid','Gram Stain (CSF)'); 
SELECT insert_unit_of_measure('','Gram Stain (CSF)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Gram Stain (CSF)'); 
SELECT add_test_result_type('Gram Stain (CSF)','R'); 
-- Begin Line: 91 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Knee Joint Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Knee Joint Fluid','Gram Stain (Knee Joint Fluid)'); 
SELECT create_relationship_sample_test('Body Fluid','Gram Stain (Knee Joint Fluid)'); 
SELECT insert_unit_of_measure('','Gram Stain (Knee Joint Fluid)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Gram Stain (Knee Joint Fluid)'); 
SELECT add_test_result_type('Gram Stain (Knee Joint Fluid)','R'); 
-- Begin Line: 92 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Plural Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Plural Fluid','Gram Stain (Plural Fluid)'); 
SELECT create_relationship_sample_test('Body Fluid','Gram Stain (Plural Fluid)'); 
SELECT insert_unit_of_measure('','Gram Stain (Plural Fluid)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Gram Stain (Plural Fluid)'); 
SELECT add_test_result_type('Gram Stain (Plural Fluid)','R'); 
-- Begin Line: 93 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Pus','Body Fluid'); 
SELECT create_relationship_panel_test('Pus','Gram Stain (Body Fluid) (Pus)'); 
SELECT create_relationship_sample_test('Body Fluid','Gram Stain (Body Fluid) (Pus)'); 
SELECT insert_unit_of_measure('','Gram Stain (Body Fluid) (Pus)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Gram Stain (Body Fluid) (Pus)'); 
SELECT add_test_result_type('Gram Stain (Body Fluid) (Pus)','R'); 
-- Begin Line: 94 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Pyrotinial Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Pyrotinial Fluid','Gram Stain (Pyrotinial Fluid)'); 
SELECT create_relationship_sample_test('Body Fluid','Gram Stain (Pyrotinial Fluid)'); 
SELECT insert_unit_of_measure('','Gram Stain (Pyrotinial Fluid)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Gram Stain (Pyrotinial Fluid)'); 
SELECT add_test_result_type('Gram Stain (Pyrotinial Fluid)','R'); 
-- Begin Line: 95 
SELECT insert_test_section('Vaginal'); 
SELECT insert_sample_type('Vaginal'); 
SELECT create_relationship_panel_sampletype('Vaginal Panel','Vaginal'); 
SELECT create_relationship_panel_test('Vaginal Panel','Gram Stain (Vaginal)'); 
SELECT create_relationship_sample_test('Vaginal','Gram Stain (Vaginal)'); 
SELECT insert_unit_of_measure('NA','Gram Stain (Vaginal)'); 
SELECT create_relationship_test_section_test('Vaginal','Gram Stain (Vaginal)'); 
SELECT add_test_result_type('Gram Stain (Vaginal)','R'); 
-- Begin Line: 96 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Anaemia Panel','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Anaemia Panel','Haemoglobin'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Haemoglobin'); 
SELECT insert_unit_of_measure('gm/dl','Haemoglobin'); 
SELECT create_relationship_test_section_test('Haematology','Haemoglobin'); 
SELECT insert_result_limit_normal_range('Haemoglobin',11.50,15.50); 
SELECT insert_result_limit_valid_range('Haemoglobin',1.50,24); 
-- Begin Line: 97 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('CBC','Blood (EDTA)'); 
SELECT create_relationship_panel_test('CBC','Haemoglobin'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Haemoglobin'); 
SELECT insert_unit_of_measure('gm/dl','Haemoglobin'); 
SELECT create_relationship_test_section_test('Haematology','Haemoglobin'); 
SELECT insert_result_limit_normal_range('Haemoglobin',12,16); 
SELECT insert_result_limit_valid_range('Haemoglobin',1,22); 
-- Begin Line: 98 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Cross Match','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Cross Match','Haemoglobin'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Haemoglobin'); 
SELECT insert_unit_of_measure('gm/dl','Haemoglobin'); 
SELECT create_relationship_test_section_test('Haematology','Haemoglobin'); 
SELECT insert_result_limit_normal_range('Haemoglobin',11.50,15.50); 
SELECT insert_result_limit_valid_range('Haemoglobin',1.50,24); 
-- Begin Line: 99 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Haemoglobin'); 
SELECT insert_unit_of_measure('gm/dl','Haemoglobin'); 
SELECT create_relationship_test_section_test('Haematology','Haemoglobin'); 
SELECT insert_result_limit_normal_range('Haemoglobin',12,16); 
SELECT insert_result_limit_valid_range('Haemoglobin',1,22); 
-- Begin Line: 100 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Cross Match','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Cross Match','Haemoglobin (Relative)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Haemoglobin (Relative)'); 
SELECT insert_unit_of_measure('gm/dl','Haemoglobin (Relative)'); 
SELECT create_relationship_test_section_test('Haematology','Haemoglobin (Relative)'); 
SELECT insert_result_limit_normal_range('Haemoglobin (Relative)',11.50,15.50); 
SELECT insert_result_limit_valid_range('Haemoglobin (Relative)',1.50,24); 
-- Begin Line: 101 
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
-- Begin Line: 102 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Hb Electrophoresis'); 
SELECT insert_unit_of_measure('NA','Hb Electrophoresis'); 
SELECT create_relationship_test_section_test('Haematology','Hb Electrophoresis'); 
SELECT add_test_result_type('Hb Electrophoresis','D', 'AA'); 
SELECT add_test_result_type('Hb Electrophoresis','D', 'AS'); 
SELECT add_test_result_type('Hb Electrophoresis','D', 'SS'); 
-- Begin Line: 103 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Hb1AC'); 
SELECT insert_unit_of_measure('%','Hb1AC'); 
SELECT create_relationship_test_section_test('Biochemistry','Hb1AC'); 
SELECT insert_result_limit_normal_range('Hb1AC',7,8); 
SELECT insert_result_limit_valid_range('Hb1AC',2,15); 
-- Begin Line: 104 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Cross Match','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Cross Match','HbsAg ELISA'); 
SELECT create_relationship_sample_test('Blood (EDTA)','HbsAg ELISA'); 
SELECT insert_unit_of_measure('NA','HbsAg ELISA'); 
SELECT create_relationship_test_section_test('Serology','HbsAg ELISA'); 
SELECT add_test_result_type('HbsAg ELISA','D', 'Positive'); 
SELECT add_test_result_type('HbsAg ELISA','D', 'Negative'); 
-- Begin Line: 105 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Serology','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Serology','HbsAg ELISA'); 
SELECT create_relationship_sample_test('Blood (EDTA)','HbsAg ELISA'); 
SELECT insert_unit_of_measure('NA','HbsAg ELISA'); 
SELECT create_relationship_test_section_test('Serology','HbsAg ELISA'); 
SELECT add_test_result_type('HbsAg ELISA','D', 'Positive'); 
SELECT add_test_result_type('HbsAg ELISA','D', 'Negative'); 
-- Begin Line: 106 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Cross Match','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Cross Match','HbsAg Rapid'); 
SELECT create_relationship_sample_test('Blood (EDTA)','HbsAg Rapid'); 
SELECT insert_unit_of_measure('NA','HbsAg Rapid'); 
SELECT create_relationship_test_section_test('Serology','HbsAg Rapid'); 
SELECT add_test_result_type('HbsAg Rapid','D', 'Positive'); 
SELECT add_test_result_type('HbsAg Rapid','D', 'Negative'); 
-- Begin Line: 107 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Serology','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Serology','HbsAg Rapid'); 
SELECT create_relationship_sample_test('Blood (EDTA)','HbsAg Rapid'); 
SELECT insert_unit_of_measure('NA','HbsAg Rapid'); 
SELECT create_relationship_test_section_test('Serology','HbsAg Rapid'); 
SELECT add_test_result_type('HbsAg Rapid','D', 'Positive'); 
SELECT add_test_result_type('HbsAg Rapid','D', 'Negative'); 
-- Begin Line: 108 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Cross Match','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Cross Match','HCV ELISA'); 
SELECT create_relationship_sample_test('Blood (EDTA)','HCV ELISA'); 
SELECT insert_unit_of_measure('NA','HCV ELISA'); 
SELECT create_relationship_test_section_test('Serology','HCV ELISA'); 
SELECT add_test_result_type('HCV ELISA','D', 'Positive'); 
SELECT add_test_result_type('HCV ELISA','D', 'Negative'); 
-- Begin Line: 109 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Serology','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Serology','HCV ELISA'); 
SELECT create_relationship_sample_test('Blood (EDTA)','HCV ELISA'); 
SELECT insert_unit_of_measure('NA','HCV ELISA'); 
SELECT create_relationship_test_section_test('Serology','HCV ELISA'); 
SELECT add_test_result_type('HCV ELISA','D', 'Positive'); 
SELECT add_test_result_type('HCV ELISA','D', 'Negative'); 
-- Begin Line: 110 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Cross Match','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Cross Match','HCV Tridot'); 
SELECT create_relationship_sample_test('Blood (EDTA)','HCV Tridot'); 
SELECT insert_unit_of_measure('NA','HCV Tridot'); 
SELECT create_relationship_test_section_test('Serology','HCV Tridot'); 
SELECT add_test_result_type('HCV Tridot','D', 'Positive'); 
SELECT add_test_result_type('HCV Tridot','D', 'Negative'); 
-- Begin Line: 111 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Serology','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Serology','HCV Tridot'); 
SELECT create_relationship_sample_test('Blood (EDTA)','HCV Tridot'); 
SELECT insert_unit_of_measure('NA','HCV Tridot'); 
SELECT create_relationship_test_section_test('Serology','HCV Tridot'); 
SELECT add_test_result_type('HCV Tridot','D', 'Positive'); 
SELECT add_test_result_type('HCV Tridot','D', 'Negative'); 
-- Begin Line: 112 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Lipid Profile','Serum'); 
SELECT create_relationship_panel_test('Lipid Profile','HDL'); 
SELECT create_relationship_sample_test('Serum','HDL'); 
SELECT insert_unit_of_measure('mg/dl','HDL'); 
SELECT create_relationship_test_section_test('Biochemistry','HDL'); 
SELECT insert_result_limit_normal_range('HDL',35,60); 
SELECT insert_result_limit_valid_range('HDL',5,300); 
-- Begin Line: 113 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Serology','Serum'); 
SELECT create_relationship_panel_test('Serology','Hepatitis A (Elisa)'); 
SELECT create_relationship_sample_test('Serum','Hepatitis A (Elisa)'); 
SELECT insert_unit_of_measure('','Hepatitis A (Elisa)'); 
SELECT create_relationship_test_section_test('Serology','Hepatitis A (Elisa)'); 
SELECT add_test_result_type('Hepatitis A (Elisa)','D', 'Positive'); 
SELECT add_test_result_type('Hepatitis A (Elisa)','D', 'Negative'); 
-- Begin Line: 114 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Serology','Serum'); 
SELECT create_relationship_panel_test('Serology','Hepatitis A (Rapid)'); 
SELECT create_relationship_sample_test('Serum','Hepatitis A (Rapid)'); 
SELECT insert_unit_of_measure('','Hepatitis A (Rapid)'); 
SELECT create_relationship_test_section_test('Serology','Hepatitis A (Rapid)'); 
SELECT add_test_result_type('Hepatitis A (Rapid)','D', 'Positive'); 
SELECT add_test_result_type('Hepatitis A (Rapid)','D', 'Negative'); 
-- Begin Line: 115 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Serology','Serum'); 
SELECT create_relationship_panel_test('Serology','Hepatitis E (Elisa)'); 
SELECT create_relationship_sample_test('Serum','Hepatitis E (Elisa)'); 
SELECT insert_unit_of_measure('','Hepatitis E (Elisa)'); 
SELECT create_relationship_test_section_test('Serology','Hepatitis E (Elisa)'); 
SELECT add_test_result_type('Hepatitis E (Elisa)','D', 'Positive'); 
SELECT add_test_result_type('Hepatitis E (Elisa)','D', 'Negative'); 
-- Begin Line: 116 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Serology','Serum'); 
SELECT create_relationship_panel_test('Serology','Hepatitis E (Rapid)'); 
SELECT create_relationship_sample_test('Serum','Hepatitis E (Rapid)'); 
SELECT insert_unit_of_measure('','Hepatitis E (Rapid)'); 
SELECT create_relationship_test_section_test('Serology','Hepatitis E (Rapid)'); 
SELECT add_test_result_type('Hepatitis E (Rapid)','D', 'Positive'); 
SELECT add_test_result_type('Hepatitis E (Rapid)','D', 'Negative'); 
-- Begin Line: 117 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Cross Match','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Cross Match','HIV ELISA (Blood)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','HIV ELISA (Blood)'); 
SELECT insert_unit_of_measure('NA','HIV ELISA (Blood)'); 
SELECT create_relationship_test_section_test('Serology','HIV ELISA (Blood)'); 
SELECT add_test_result_type('HIV ELISA (Blood)','D', 'Positive'); 
SELECT add_test_result_type('HIV ELISA (Blood)','D', 'Negative'); 
-- Begin Line: 118 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Serology','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Serology','HIV ELISA (Blood)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','HIV ELISA (Blood)'); 
SELECT insert_unit_of_measure('NA','HIV ELISA (Blood)'); 
SELECT create_relationship_test_section_test('Serology','HIV ELISA (Blood)'); 
SELECT add_test_result_type('HIV ELISA (Blood)','D', 'Positive'); 
SELECT add_test_result_type('HIV ELISA (Blood)','D', 'Negative'); 
-- Begin Line: 119 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Serology','Serum'); 
SELECT create_relationship_panel_test('Serology','HIV ELISA (Serum)'); 
SELECT create_relationship_sample_test('Serum','HIV ELISA (Serum)'); 
SELECT insert_unit_of_measure('NA','HIV ELISA (Serum)'); 
SELECT create_relationship_test_section_test('Serology','HIV ELISA (Serum)'); 
SELECT add_test_result_type('HIV ELISA (Serum)','D', 'Positive'); 
SELECT add_test_result_type('HIV ELISA (Serum)','D', 'Negative'); 
-- Begin Line: 120 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Cross Match','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Cross Match','HIV Tridot'); 
SELECT create_relationship_sample_test('Blood (EDTA)','HIV Tridot'); 
SELECT insert_unit_of_measure('NA','HIV Tridot'); 
SELECT create_relationship_test_section_test('Serology','HIV Tridot'); 
SELECT add_test_result_type('HIV Tridot','D', 'Positive'); 
SELECT add_test_result_type('HIV Tridot','D', 'Negative'); 
-- Begin Line: 121 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Serology','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Serology','HIV Tridot'); 
SELECT create_relationship_sample_test('Blood (EDTA)','HIV Tridot'); 
SELECT insert_unit_of_measure('NA','HIV Tridot'); 
SELECT create_relationship_test_section_test('Serology','HIV Tridot'); 
SELECT add_test_result_type('HIV Tridot','D', 'Positive'); 
SELECT add_test_result_type('HIV Tridot','D', 'Negative'); 
-- Begin Line: 122 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Haematology','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Haematology','HPLC'); 
SELECT create_relationship_sample_test('Blood (EDTA)','HPLC'); 
SELECT insert_unit_of_measure('NA','HPLC'); 
SELECT create_relationship_test_section_test('Biochemistry','HPLC'); 
SELECT add_test_result_type('HPLC','R'); 
-- Begin Line: 123 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Coagulation Factor','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Coagulation Factor','INR Ratio (Blood)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','INR Ratio (Blood)'); 
SELECT insert_unit_of_measure('ratio','INR Ratio (Blood)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','INR Ratio (Blood)'); 
SELECT insert_result_limit_normal_range('INR Ratio (Blood)',0.90,1.10); 
SELECT insert_result_limit_valid_range('INR Ratio (Blood)',0.50,12); 
-- Begin Line: 124 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Prothrombin Time','Serum'); 
SELECT create_relationship_panel_test('Prothrombin Time','INR Ratio (Serum)'); 
SELECT create_relationship_sample_test('Serum','INR Ratio (Serum)'); 
SELECT insert_unit_of_measure('ratio','INR Ratio (Serum)'); 
SELECT create_relationship_test_section_test('Biochemistry','INR Ratio (Serum)'); 
SELECT insert_result_limit_normal_range('INR Ratio (Serum)',0.90,1.10); 
SELECT insert_result_limit_valid_range('INR Ratio (Serum)',0.50,12); 
-- Begin Line: 125 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Biochemistry','Serum'); 
SELECT create_relationship_panel_test('Biochemistry','LDH'); 
SELECT create_relationship_sample_test('Serum','LDH'); 
SELECT insert_unit_of_measure('u/L','LDH'); 
SELECT create_relationship_test_section_test('Biochemistry','LDH'); 
SELECT insert_result_limit_normal_range('LDH',45,90); 
SELECT insert_result_limit_valid_range('LDH',20,700); 
-- Begin Line: 126 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Lipid Profile','Serum'); 
SELECT create_relationship_panel_test('Lipid Profile','LDL'); 
SELECT create_relationship_sample_test('Serum','LDL'); 
SELECT insert_unit_of_measure('mg/dl','LDL'); 
SELECT create_relationship_test_section_test('Biochemistry','LDL'); 
SELECT insert_result_limit_normal_range('LDL',85,130); 
SELECT insert_result_limit_valid_range('LDL',20,700); 
-- Begin Line: 127 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Urine LE/Nitrite','Urine'); 
SELECT create_relationship_panel_test('Urine LE/Nitrite','LE (Urine)'); 
SELECT create_relationship_sample_test('Urine','LE (Urine)'); 
SELECT insert_unit_of_measure('NA','LE (Urine)'); 
SELECT create_relationship_test_section_test('Urine','LE (Urine)'); 
SELECT add_test_result_type('LE (Urine)','R'); 
-- Begin Line: 128 
SELECT insert_test_section('Vaginal'); 
SELECT insert_sample_type('Vaginal'); 
SELECT create_relationship_panel_sampletype('Vaginal Panel','Vaginal'); 
SELECT create_relationship_panel_test('Vaginal Panel','LE (Vaginal)'); 
SELECT create_relationship_sample_test('Vaginal','LE (Vaginal)'); 
SELECT insert_unit_of_measure('NA','LE (Vaginal)'); 
SELECT create_relationship_test_section_test('Vaginal','LE (Vaginal)'); 
SELECT add_test_result_type('LE (Vaginal)','D', 'Positive'); 
SELECT add_test_result_type('LE (Vaginal)','D', 'Negative'); 
-- Begin Line: 129 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Biochemistry','Serum'); 
SELECT create_relationship_panel_test('Biochemistry','LH'); 
SELECT create_relationship_sample_test('Serum','LH'); 
SELECT insert_unit_of_measure('Iu/L','LH'); 
SELECT create_relationship_test_section_test('Biochemistry','LH'); 
SELECT insert_result_limit_normal_range('LH',1,9); 
SELECT insert_result_limit_valid_range('LH',0,50); 
-- Begin Line: 130 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Semen'); 
SELECT create_relationship_panel_sampletype('Semen Analysis','Semen'); 
SELECT create_relationship_panel_test('Semen Analysis','Liquification time'); 
SELECT create_relationship_sample_test('Semen','Liquification time'); 
SELECT insert_unit_of_measure('min','Liquification time'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Liquification time'); 
SELECT insert_result_limit_normal_range('Liquification time',20,30); 
-- Begin Line: 131 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Differential Count','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Differential Count','Lymphocyte'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Lymphocyte'); 
SELECT insert_unit_of_measure('%','Lymphocyte'); 
SELECT create_relationship_test_section_test('Haematology','Lymphocyte'); 
SELECT insert_result_limit_normal_range('Lymphocyte',20,40); 
SELECT insert_result_limit_valid_range('Lymphocyte',10,50); 
-- Begin Line: 132 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Electrolyte','Serum'); 
SELECT create_relationship_panel_test('Electrolyte','Magnesium'); 
SELECT create_relationship_sample_test('Serum','Magnesium'); 
SELECT insert_unit_of_measure('mg/dl','Magnesium'); 
SELECT create_relationship_test_section_test('Biochemistry','Magnesium'); 
SELECT insert_result_limit_normal_range('Magnesium',1.30,2.10); 
SELECT insert_result_limit_valid_range('Magnesium',0.20,10); 
-- Begin Line: 133 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Cross Match','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Cross Match','Malaria Parasite'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Malaria Parasite'); 
SELECT insert_unit_of_measure('NA','Malaria Parasite'); 
SELECT create_relationship_test_section_test('Serology','Malaria Parasite'); 
SELECT add_test_result_type('Malaria Parasite','D', 'Positive'); 
SELECT add_test_result_type('Malaria Parasite','D', 'Negative'); 
-- Begin Line: 134 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Anaemia Panel','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Anaemia Panel','MCH'); 
SELECT create_relationship_sample_test('Blood (EDTA)','MCH'); 
SELECT insert_unit_of_measure('per fL','MCH'); 
SELECT create_relationship_test_section_test('Haematology','MCH'); 
SELECT insert_result_limit_normal_range('MCH',28,31); 
SELECT insert_result_limit_valid_range('MCH',20,50); 
-- Begin Line: 135 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Anaemia Panel','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Anaemia Panel','MCHC'); 
SELECT create_relationship_sample_test('Blood (EDTA)','MCHC'); 
SELECT insert_unit_of_measure('g/dl','MCHC'); 
SELECT create_relationship_test_section_test('Haematology','MCHC'); 
SELECT insert_result_limit_normal_range('MCHC',34,36); 
SELECT insert_result_limit_valid_range('MCHC',11,400); 
-- Begin Line: 136 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Anaemia Panel','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Anaemia Panel','MCV'); 
SELECT create_relationship_sample_test('Blood (EDTA)','MCV'); 
SELECT insert_unit_of_measure('per fL','MCV'); 
SELECT create_relationship_test_section_test('Haematology','MCV'); 
SELECT insert_result_limit_normal_range('MCV',75,81); 
SELECT insert_result_limit_valid_range('MCV',40,130); 
-- Begin Line: 137 
SELECT insert_test_section('Vaginal'); 
SELECT insert_sample_type('Vaginal'); 
SELECT create_relationship_panel_sampletype('Vaginal Panel','Vaginal'); 
SELECT create_relationship_panel_test('Vaginal Panel','Microscopy of KOH mount'); 
SELECT create_relationship_sample_test('Vaginal','Microscopy of KOH mount'); 
SELECT insert_unit_of_measure('NA','Microscopy of KOH mount'); 
SELECT create_relationship_test_section_test('Vaginal','Microscopy of KOH mount'); 
SELECT add_test_result_type('Microscopy of KOH mount','D', 'Present'); 
SELECT add_test_result_type('Microscopy of KOH mount','D', 'Absent'); 
-- Begin Line: 138 
SELECT insert_test_section('Vaginal'); 
SELECT insert_sample_type('Vaginal'); 
SELECT create_relationship_panel_sampletype('Vaginal Panel','Vaginal'); 
SELECT create_relationship_panel_test('Vaginal Panel','Microscopy of Saline mount'); 
SELECT create_relationship_sample_test('Vaginal','Microscopy of Saline mount'); 
SELECT insert_unit_of_measure('NA','Microscopy of Saline mount'); 
SELECT create_relationship_test_section_test('Vaginal','Microscopy of Saline mount'); 
SELECT add_test_result_type('Microscopy of Saline mount','R'); 
-- Begin Line: 139 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Differential Count','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Differential Count','Monocyte'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Monocyte'); 
SELECT insert_unit_of_measure('%','Monocyte'); 
SELECT create_relationship_test_section_test('Haematology','Monocyte'); 
SELECT insert_result_limit_normal_range('Monocyte',1,6); 
SELECT insert_result_limit_valid_range('Monocyte',1,70); 
-- Begin Line: 140 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Montoux Test'); 
SELECT create_relationship_sample_test('Montoux Test','Montoux Test'); 
SELECT insert_unit_of_measure('mm','Montoux Test'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Montoux Test'); 
SELECT add_test_result_type('Montoux Test','D', 'Positive'); 
SELECT add_test_result_type('Montoux Test','D', 'Negative'); 
-- Begin Line: 141 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Semen'); 
SELECT create_relationship_panel_sampletype('Semen Analysis','Semen'); 
SELECT create_relationship_panel_test('Semen Analysis','Morphology'); 
SELECT create_relationship_sample_test('Semen','Morphology'); 
SELECT insert_unit_of_measure('','Morphology'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Morphology'); 
SELECT add_test_result_type('Morphology','D', 'Normal'); 
SELECT add_test_result_type('Morphology','D', 'Abnornal'); 
-- Begin Line: 142 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Semen'); 
SELECT create_relationship_panel_sampletype('Semen Analysis','Semen'); 
SELECT create_relationship_panel_test('Semen Analysis','Motility'); 
SELECT create_relationship_sample_test('Semen','Motility'); 
SELECT insert_unit_of_measure('','Motility'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Motility'); 
SELECT add_test_result_type('Motility','R'); 
-- Begin Line: 143 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Urine LE/Nitrite','Urine'); 
SELECT create_relationship_panel_test('Urine LE/Nitrite','Nitrite'); 
SELECT create_relationship_sample_test('Urine','Nitrite'); 
SELECT insert_unit_of_measure('NA','Nitrite'); 
SELECT create_relationship_test_section_test('Urine','Nitrite'); 
SELECT add_test_result_type('Nitrite','R'); 
-- Begin Line: 144 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Stool Microscopy w/o Concetration','Stool'); 
SELECT create_relationship_panel_test('Stool Microscopy w/o Concetration','Others (Stool w/o concentration)'); 
SELECT create_relationship_sample_test('Stool','Others (Stool w/o concentration)'); 
SELECT insert_unit_of_measure('NA','Others (Stool w/o concentration)'); 
SELECT create_relationship_test_section_test('Stool','Others (Stool w/o concentration)'); 
SELECT add_test_result_type('Others (Stool w/o concentration)','R'); 
-- Begin Line: 145 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Stool Microscopy with Concetration','Stool'); 
SELECT create_relationship_panel_test('Stool Microscopy with Concetration','Others (Stool with concentration)'); 
SELECT create_relationship_sample_test('Stool','Others (Stool with concentration)'); 
SELECT insert_unit_of_measure('NA','Others (Stool with concentration)'); 
SELECT create_relationship_test_section_test('Stool','Others (Stool with concentration)'); 
SELECT add_test_result_type('Others (Stool with concentration)','R'); 
-- Begin Line: 146 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Urine Microscopy','Urine'); 
SELECT create_relationship_panel_test('Urine Microscopy','Others (Urine)'); 
SELECT create_relationship_sample_test('Urine','Others (Urine)'); 
SELECT insert_unit_of_measure('/hPF','Others (Urine)'); 
SELECT create_relationship_test_section_test('Urine','Others (Urine)'); 
SELECT add_test_result_type('Others (Urine)','R'); 
-- Begin Line: 147 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Stool Microscopy w/o Concetration','Stool'); 
SELECT create_relationship_panel_test('Stool Microscopy w/o Concetration','Ova/Parasite/Cyst'); 
SELECT create_relationship_sample_test('Stool','Ova/Parasite/Cyst'); 
SELECT insert_unit_of_measure('NA','Ova/Parasite/Cyst'); 
SELECT create_relationship_test_section_test('Stool','Ova/Parasite/Cyst'); 
SELECT add_test_result_type('Ova/Parasite/Cyst','R'); 
-- Begin Line: 148 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Stool Microscopy with Concetration','Stool'); 
SELECT create_relationship_panel_test('Stool Microscopy with Concetration','Ova/Parasite/Cyst'); 
SELECT create_relationship_sample_test('Stool','Ova/Parasite/Cyst'); 
SELECT insert_unit_of_measure('NA','Ova/Parasite/Cyst'); 
SELECT create_relationship_test_section_test('Stool','Ova/Parasite/Cyst'); 
SELECT add_test_result_type('Ova/Parasite/Cyst','R'); 
-- Begin Line: 149 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Anaemia Panel','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Anaemia Panel','Packed Cell Volume (PCV)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Packed Cell Volume (PCV)'); 
SELECT insert_unit_of_measure('%','Packed Cell Volume (PCV)'); 
SELECT create_relationship_test_section_test('Haematology','Packed Cell Volume (PCV)'); 
SELECT insert_result_limit_normal_range('Packed Cell Volume (PCV)',45,55); 
SELECT insert_result_limit_valid_range('Packed Cell Volume (PCV)',20,80); 
-- Begin Line: 150 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Routine Blood','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Routine Blood','Packed Cell Volume (PCV)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Packed Cell Volume (PCV)'); 
SELECT insert_unit_of_measure('%','Packed Cell Volume (PCV)'); 
SELECT create_relationship_test_section_test('Haematology','Packed Cell Volume (PCV)'); 
SELECT insert_result_limit_normal_range('Packed Cell Volume (PCV)',45,55); 
SELECT insert_result_limit_valid_range('Packed Cell Volume (PCV)',20,80); 
-- Begin Line: 151 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Glucose Tolerance Test (GTT)','Urine'); 
SELECT create_relationship_panel_test('Glucose Tolerance Test (GTT)','Parallel Urine Sugar (120 mins)'); 
SELECT create_relationship_sample_test('Urine','Parallel Urine Sugar (120 mins)'); 
SELECT insert_unit_of_measure('','Parallel Urine Sugar (120 mins)'); 
SELECT create_relationship_test_section_test('Biochemistry','Parallel Urine Sugar (120 mins)'); 
SELECT add_test_result_type('Parallel Urine Sugar (120 mins)','D', 'Nil'); 
SELECT add_test_result_type('Parallel Urine Sugar (120 mins)','D', 'Trace'); 
SELECT add_test_result_type('Parallel Urine Sugar (120 mins)','D', '1+'); 
SELECT add_test_result_type('Parallel Urine Sugar (120 mins)','D', '2+'); 
SELECT add_test_result_type('Parallel Urine Sugar (120 mins)','D', '3+'); 
SELECT add_test_result_type('Parallel Urine Sugar (120 mins)','D', '4+'); 
-- Begin Line: 152 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Glucose Tolerance Test (GTT)','Urine'); 
SELECT create_relationship_panel_test('Glucose Tolerance Test (GTT)','Parallel Urine Sugar (30 mins)'); 
SELECT create_relationship_sample_test('Urine','Parallel Urine Sugar (30 mins)'); 
SELECT insert_unit_of_measure('','Parallel Urine Sugar (30 mins)'); 
SELECT create_relationship_test_section_test('Biochemistry','Parallel Urine Sugar (30 mins)'); 
SELECT add_test_result_type('Parallel Urine Sugar (30 mins)','D', 'Nil'); 
SELECT add_test_result_type('Parallel Urine Sugar (30 mins)','D', 'Trace'); 
SELECT add_test_result_type('Parallel Urine Sugar (30 mins)','D', '1+'); 
SELECT add_test_result_type('Parallel Urine Sugar (30 mins)','D', '2+'); 
SELECT add_test_result_type('Parallel Urine Sugar (30 mins)','D', '3+'); 
SELECT add_test_result_type('Parallel Urine Sugar (30 mins)','D', '4+'); 
-- Begin Line: 153 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Glucose Tolerance Test (GTT)','Urine'); 
SELECT create_relationship_panel_test('Glucose Tolerance Test (GTT)','Parallel Urine Sugar (60 mins)'); 
SELECT create_relationship_sample_test('Urine','Parallel Urine Sugar (60 mins)'); 
SELECT insert_unit_of_measure('','Parallel Urine Sugar (60 mins)'); 
SELECT create_relationship_test_section_test('Biochemistry','Parallel Urine Sugar (60 mins)'); 
SELECT add_test_result_type('Parallel Urine Sugar (60 mins)','D', 'Nil'); 
SELECT add_test_result_type('Parallel Urine Sugar (60 mins)','D', 'Trace'); 
SELECT add_test_result_type('Parallel Urine Sugar (60 mins)','D', '1+'); 
SELECT add_test_result_type('Parallel Urine Sugar (60 mins)','D', '2+'); 
SELECT add_test_result_type('Parallel Urine Sugar (60 mins)','D', '3+'); 
SELECT add_test_result_type('Parallel Urine Sugar (60 mins)','D', '4+'); 
-- Begin Line: 154 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Glucose Tolerance Test (GTT)','Urine'); 
SELECT create_relationship_panel_test('Glucose Tolerance Test (GTT)','Parallel Urine Sugar (90 mins)'); 
SELECT create_relationship_sample_test('Urine','Parallel Urine Sugar (90 mins)'); 
SELECT insert_unit_of_measure('','Parallel Urine Sugar (90 mins)'); 
SELECT create_relationship_test_section_test('Biochemistry','Parallel Urine Sugar (90 mins)'); 
SELECT add_test_result_type('Parallel Urine Sugar (90 mins)','D', 'Nil'); 
SELECT add_test_result_type('Parallel Urine Sugar (90 mins)','D', 'Trace'); 
SELECT add_test_result_type('Parallel Urine Sugar (90 mins)','D', '1+'); 
SELECT add_test_result_type('Parallel Urine Sugar (90 mins)','D', '2+'); 
SELECT add_test_result_type('Parallel Urine Sugar (90 mins)','D', '3+'); 
SELECT add_test_result_type('Parallel Urine Sugar (90 mins)','D', '4+'); 
-- Begin Line: 155 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Glucose Tolerance Test (GTT)','Urine'); 
SELECT create_relationship_panel_test('Glucose Tolerance Test (GTT)','Parallel Urine Sugar (Fasting Blood)'); 
SELECT create_relationship_sample_test('Urine','Parallel Urine Sugar (Fasting Blood)'); 
SELECT insert_unit_of_measure('','Parallel Urine Sugar (Fasting Blood)'); 
SELECT create_relationship_test_section_test('Biochemistry','Parallel Urine Sugar (Fasting Blood)'); 
SELECT add_test_result_type('Parallel Urine Sugar (Fasting Blood)','D', 'Nil'); 
SELECT add_test_result_type('Parallel Urine Sugar (Fasting Blood)','D', 'Trace'); 
SELECT add_test_result_type('Parallel Urine Sugar (Fasting Blood)','D', '1+'); 
SELECT add_test_result_type('Parallel Urine Sugar (Fasting Blood)','D', '2+'); 
SELECT add_test_result_type('Parallel Urine Sugar (Fasting Blood)','D', '3+'); 
SELECT add_test_result_type('Parallel Urine Sugar (Fasting Blood)','D', '4+'); 
-- Begin Line: 156 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Semen'); 
SELECT create_relationship_panel_sampletype('Semen Analysis','Semen'); 
SELECT create_relationship_panel_test('Semen Analysis','Parasite'); 
SELECT create_relationship_sample_test('Semen','Parasite'); 
SELECT insert_unit_of_measure('per hpf','Parasite'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Parasite'); 
SELECT add_test_result_type('Parasite','D', 'Present'); 
SELECT add_test_result_type('Parasite','D', 'Absent'); 
-- Begin Line: 157 
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
-- Begin Line: 158 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('CSF','Body Fluid'); 
SELECT create_relationship_panel_test('CSF','Pendy Reagent Test'); 
SELECT create_relationship_sample_test('Body Fluid','Pendy Reagent Test'); 
SELECT insert_unit_of_measure('','Pendy Reagent Test'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Pendy Reagent Test'); 
SELECT add_test_result_type('Pendy Reagent Test','D', 'Positive'); 
SELECT add_test_result_type('Pendy Reagent Test','D', 'Negative'); 
-- Begin Line: 159 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Environment','Stool'); 
SELECT create_relationship_panel_test('Environment','pH (Stool)'); 
SELECT create_relationship_sample_test('Stool','pH (Stool)'); 
SELECT insert_unit_of_measure('NA','pH (Stool)'); 
SELECT create_relationship_test_section_test('Stool','pH (Stool)'); 
SELECT insert_result_limit_normal_range('pH (Stool)',6.80,7.20); 
SELECT insert_result_limit_valid_range('pH (Stool)',1,14); 
-- Begin Line: 160 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Routine Urine','Urine'); 
SELECT create_relationship_panel_test('Routine Urine','pH (Urine)'); 
SELECT create_relationship_sample_test('Urine','pH (Urine)'); 
SELECT insert_unit_of_measure('NA','pH (Urine)'); 
SELECT create_relationship_test_section_test('Urine','pH (Urine)'); 
SELECT insert_result_limit_normal_range('pH (Urine)',6.80,7); 
SELECT insert_result_limit_valid_range('pH (Urine)',0,14); 
-- Begin Line: 161 
SELECT insert_test_section('Vaginal'); 
SELECT insert_sample_type('Vaginal'); 
SELECT create_relationship_panel_sampletype('Vaginal Panel','Vaginal'); 
SELECT create_relationship_panel_test('Vaginal Panel','pH (Vaginal)'); 
SELECT create_relationship_sample_test('Vaginal','pH (Vaginal)'); 
SELECT insert_unit_of_measure('NA','pH (Vaginal)'); 
SELECT create_relationship_test_section_test('Vaginal','pH (Vaginal)'); 
SELECT insert_result_limit_normal_range('pH (Vaginal)',1,14); 
SELECT insert_result_limit_valid_range('pH (Vaginal)',1,14); 
-- Begin Line: 162 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Electrolyte','Serum'); 
SELECT create_relationship_panel_test('Electrolyte','Phosphorous'); 
SELECT create_relationship_sample_test('Serum','Phosphorous'); 
SELECT insert_unit_of_measure('mg/dl','Phosphorous'); 
SELECT create_relationship_test_section_test('Biochemistry','Phosphorous'); 
SELECT insert_result_limit_normal_range('Phosphorous',3,4.50); 
SELECT insert_result_limit_valid_range('Phosphorous',1,14); 
-- Begin Line: 163 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Anaemia Panel','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Anaemia Panel','Platelet Count'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Platelet Count'); 
SELECT insert_unit_of_measure('cumm','Platelet Count'); 
SELECT create_relationship_test_section_test('Haematology','Platelet Count'); 
SELECT insert_result_limit_normal_range('Platelet Count',100000,300000); 
SELECT insert_result_limit_valid_range('Platelet Count',5000,1000000); 
-- Begin Line: 164 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('CBC','Blood (EDTA)'); 
SELECT create_relationship_panel_test('CBC','Platelet Count'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Platelet Count'); 
SELECT insert_unit_of_measure('cumm','Platelet Count'); 
SELECT create_relationship_test_section_test('Haematology','Platelet Count'); 
SELECT insert_result_limit_normal_range('Platelet Count',100000,300000); 
SELECT insert_result_limit_valid_range('Platelet Count',5000,1000000); 
-- Begin Line: 165 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Differential Count','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Differential Count','Polymorph'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Polymorph'); 
SELECT insert_unit_of_measure('%','Polymorph'); 
SELECT create_relationship_test_section_test('Haematology','Polymorph'); 
SELECT insert_result_limit_normal_range('Polymorph',40,75); 
SELECT insert_result_limit_valid_range('Polymorph',20,80); 
-- Begin Line: 166 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Glucose Tolerance Test (GTT)','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Glucose Tolerance Test (GTT)','Post Blood Sugar (120 mins)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Post Blood Sugar (120 mins)'); 
SELECT insert_unit_of_measure('mg/dl','Post Blood Sugar (120 mins)'); 
SELECT create_relationship_test_section_test('Biochemistry','Post Blood Sugar (120 mins)'); 
SELECT insert_result_limit_normal_range('Post Blood Sugar (120 mins)',120,160); 
SELECT insert_result_limit_valid_range('Post Blood Sugar (120 mins)',10,1000); 
-- Begin Line: 167 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Glucose Tolerance Test (GTT)','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Glucose Tolerance Test (GTT)','Post Blood Sugar (30 mins)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Post Blood Sugar (30 mins)'); 
SELECT insert_unit_of_measure('mg/dl','Post Blood Sugar (30 mins)'); 
SELECT create_relationship_test_section_test('Biochemistry','Post Blood Sugar (30 mins)'); 
SELECT insert_result_limit_valid_range('Post Blood Sugar (30 mins)',10,1000); 
-- Begin Line: 168 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Glucose Tolerance Test (GTT)','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Glucose Tolerance Test (GTT)','Post Blood Sugar (60 mins)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Post Blood Sugar (60 mins)'); 
SELECT insert_unit_of_measure('mg/dl','Post Blood Sugar (60 mins)'); 
SELECT create_relationship_test_section_test('Biochemistry','Post Blood Sugar (60 mins)'); 
SELECT insert_result_limit_valid_range('Post Blood Sugar (60 mins)',10,1000); 
-- Begin Line: 169 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Glucose Tolerance Test (GTT)','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Glucose Tolerance Test (GTT)','Post Blood Sugar (90 mins)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Post Blood Sugar (90 mins)'); 
SELECT insert_unit_of_measure('mg/dl','Post Blood Sugar (90 mins)'); 
SELECT create_relationship_test_section_test('Biochemistry','Post Blood Sugar (90 mins)'); 
SELECT insert_result_limit_valid_range('Post Blood Sugar (90 mins)',10,1000); 
-- Begin Line: 170 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Electrolyte','Serum'); 
SELECT create_relationship_panel_test('Electrolyte','Potassium'); 
SELECT create_relationship_sample_test('Serum','Potassium'); 
SELECT insert_unit_of_measure('mmole/l','Potassium'); 
SELECT create_relationship_test_section_test('Biochemistry','Potassium'); 
SELECT insert_result_limit_normal_range('Potassium',3.50,5.10); 
SELECT insert_result_limit_valid_range('Potassium',0.50,12); 
-- Begin Line: 171 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Biochemistry','Serum'); 
SELECT create_relationship_panel_test('Biochemistry','PP2BS'); 
SELECT create_relationship_sample_test('Serum','PP2BS'); 
SELECT insert_unit_of_measure('mg/dl','PP2BS'); 
SELECT create_relationship_test_section_test('Biochemistry','PP2BS'); 
SELECT insert_result_limit_normal_range('PP2BS',120,150); 
SELECT insert_result_limit_valid_range('PP2BS',120,1000); 
-- Begin Line: 172 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Biochemistry','Serum'); 
SELECT create_relationship_panel_test('Biochemistry','Prolactin'); 
SELECT create_relationship_sample_test('Serum','Prolactin'); 
SELECT insert_unit_of_measure('nd/ml','Prolactin'); 
SELECT create_relationship_test_section_test('Biochemistry','Prolactin'); 
-- Begin Line: 173 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Asitic Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Asitic Fluid','Protein (Asitic Fluid)'); 
SELECT create_relationship_sample_test('Body Fluid','Protein (Asitic Fluid)'); 
SELECT insert_unit_of_measure('mg/dl','Protein (Asitic Fluid)'); 
SELECT create_relationship_test_section_test('Biochemistry','Protein (Asitic Fluid)'); 
-- Begin Line: 174 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('CSF','Body Fluid'); 
SELECT create_relationship_panel_test('CSF','Protein (CSF)'); 
SELECT create_relationship_sample_test('Body Fluid','Protein (CSF)'); 
SELECT insert_unit_of_measure('mg/dl','Protein (CSF)'); 
SELECT create_relationship_test_section_test('Biochemistry','Protein (CSF)'); 
-- Begin Line: 175 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Knee Joint Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Knee Joint Fluid','Protein (Knee Joint Fluid)'); 
SELECT create_relationship_sample_test('Body Fluid','Protein (Knee Joint Fluid)'); 
SELECT insert_unit_of_measure('mg/dl','Protein (Knee Joint Fluid)'); 
SELECT create_relationship_test_section_test('Biochemistry','Protein (Knee Joint Fluid)'); 
-- Begin Line: 176 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Plural Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Plural Fluid','Protein (Plural Fluid)'); 
SELECT create_relationship_sample_test('Body Fluid','Protein (Plural Fluid)'); 
SELECT insert_unit_of_measure('mg/dl','Protein (Plural Fluid)'); 
SELECT create_relationship_test_section_test('Biochemistry','Protein (Plural Fluid)'); 
-- Begin Line: 177 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Pus','Body Fluid'); 
SELECT create_relationship_panel_test('Pus','Protein (Pus)'); 
SELECT create_relationship_sample_test('Body Fluid','Protein (Pus)'); 
SELECT insert_unit_of_measure('mg/dl','Protein (Pus)'); 
SELECT create_relationship_test_section_test('Biochemistry','Protein (Pus)'); 
-- Begin Line: 178 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Pyrotinial Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Pyrotinial Fluid','Protein (Pyrotinial Fluid)'); 
SELECT create_relationship_sample_test('Body Fluid','Protein (Pyrotinial Fluid)'); 
SELECT insert_unit_of_measure('mg/dl','Protein (Pyrotinial Fluid)'); 
SELECT create_relationship_test_section_test('Biochemistry','Protein (Pyrotinial Fluid)'); 
-- Begin Line: 179 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Coagulation Factor','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Coagulation Factor','Prothrombin Time (Control) (Blood)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Prothrombin Time (Control) (Blood)'); 
SELECT insert_unit_of_measure('sec','Prothrombin Time (Control) (Blood)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Prothrombin Time (Control) (Blood)'); 
SELECT insert_result_limit_normal_range('Prothrombin Time (Control) (Blood)',11,16); 
SELECT insert_result_limit_valid_range('Prothrombin Time (Control) (Blood)',5,720); 
-- Begin Line: 180 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Liver Function Test','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Liver Function Test','Prothrombin Time (Control) (Blood)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Prothrombin Time (Control) (Blood)'); 
SELECT insert_unit_of_measure('sec','Prothrombin Time (Control) (Blood)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Prothrombin Time (Control) (Blood)'); 
SELECT insert_result_limit_normal_range('Prothrombin Time (Control) (Blood)',11,16); 
SELECT insert_result_limit_valid_range('Prothrombin Time (Control) (Blood)',5,720); 
-- Begin Line: 181 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Prothrombin Time','Serum'); 
SELECT create_relationship_panel_test('Prothrombin Time','Prothrombin Time (Control) (Serum)'); 
SELECT create_relationship_sample_test('Serum','Prothrombin Time (Control) (Serum)'); 
SELECT insert_unit_of_measure('sec','Prothrombin Time (Control) (Serum)'); 
SELECT create_relationship_test_section_test('Biochemistry','Prothrombin Time (Control) (Serum)'); 
SELECT insert_result_limit_normal_range('Prothrombin Time (Control) (Serum)',11,16); 
SELECT insert_result_limit_valid_range('Prothrombin Time (Control) (Serum)',5,720); 
-- Begin Line: 182 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Coagulation Factor','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Coagulation Factor','Prothrombin Time (Test) (Blood)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Prothrombin Time (Test) (Blood)'); 
SELECT insert_unit_of_measure('sec','Prothrombin Time (Test) (Blood)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Prothrombin Time (Test) (Blood)'); 
SELECT insert_result_limit_normal_range('Prothrombin Time (Test) (Blood)',11,16); 
SELECT insert_result_limit_valid_range('Prothrombin Time (Test) (Blood)',5,720); 
-- Begin Line: 183 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Liver Function Test','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Liver Function Test','Prothrombin Time (Test) (Blood)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Prothrombin Time (Test) (Blood)'); 
SELECT insert_unit_of_measure('sec','Prothrombin Time (Test) (Blood)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Prothrombin Time (Test) (Blood)'); 
SELECT insert_result_limit_normal_range('Prothrombin Time (Test) (Blood)',11,16); 
SELECT insert_result_limit_valid_range('Prothrombin Time (Test) (Blood)',5,720); 
-- Begin Line: 184 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Prothrombin Time','Serum'); 
SELECT create_relationship_panel_test('Prothrombin Time','Prothrombin Time (Test) (Serum)'); 
SELECT create_relationship_sample_test('Serum','Prothrombin Time (Test) (Serum)'); 
SELECT insert_unit_of_measure('sec','Prothrombin Time (Test) (Serum)'); 
SELECT create_relationship_test_section_test('Biochemistry','Prothrombin Time (Test) (Serum)'); 
SELECT insert_result_limit_normal_range('Prothrombin Time (Test) (Serum)',11,16); 
SELECT insert_result_limit_valid_range('Prothrombin Time (Test) (Serum)',5,720); 
-- Begin Line: 185 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','PS for Malaria Parasites'); 
SELECT insert_unit_of_measure('NA','PS for Malaria Parasites'); 
SELECT create_relationship_test_section_test('Haematology','PS for Malaria Parasites'); 
SELECT add_test_result_type('PS for Malaria Parasites','D', 'Positive'); 
SELECT add_test_result_type('PS for Malaria Parasites','D', 'Negative'); 
-- Begin Line: 186 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','PS for mf by concentration'); 
SELECT insert_unit_of_measure('NA','PS for mf by concentration'); 
SELECT create_relationship_test_section_test('Haematology','PS for mf by concentration'); 
SELECT add_test_result_type('PS for mf by concentration','D', 'Positive'); 
SELECT add_test_result_type('PS for mf by concentration','D', 'Negative'); 
-- Begin Line: 187 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Anaemia Panel','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Anaemia Panel','PS for RBC Morphology'); 
SELECT create_relationship_sample_test('Blood (EDTA)','PS for RBC Morphology'); 
SELECT insert_unit_of_measure('NA','PS for RBC Morphology'); 
SELECT create_relationship_test_section_test('Haematology','PS for RBC Morphology'); 
SELECT add_test_result_type('PS for RBC Morphology','D', 'Iron Deficiency Anaemia'); 
SELECT add_test_result_type('PS for RBC Morphology','D', 'Haemolytic Amaemia'); 
SELECT add_test_result_type('PS for RBC Morphology','D', 'Megaloblastic Anaemia'); 
SELECT add_test_result_type('PS for RBC Morphology','D', 'Haemorrhagic Anaemia'); 
SELECT add_test_result_type('PS for RBC Morphology','D', 'Pancytopenia'); 
SELECT add_test_result_type('PS for RBC Morphology','D', 'Due to Bone Marrow Failure Anaemia'); 
SELECT add_test_result_type('PS for RBC Morphology','D', 'Parasitic Anaemia'); 
SELECT add_test_result_type('PS for RBC Morphology','D', 'Sickle Cell Anaemia'); 
-- Begin Line: 188 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Biochemistry','Serum'); 
SELECT create_relationship_panel_test('Biochemistry','PSA'); 
SELECT create_relationship_sample_test('Serum','PSA'); 
SELECT insert_unit_of_measure('ng/ml','PSA'); 
SELECT create_relationship_test_section_test('Biochemistry','PSA'); 
SELECT insert_result_limit_normal_range('PSA',0.10,4); 
SELECT insert_result_limit_valid_range('PSA',0.10,50); 
-- Begin Line: 189 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Semen'); 
SELECT create_relationship_panel_sampletype('Semen Analysis','Semen'); 
SELECT create_relationship_panel_test('Semen Analysis','Pus Cells (Semen)'); 
SELECT create_relationship_sample_test('Semen','Pus Cells (Semen)'); 
SELECT insert_unit_of_measure('/hpf','Pus Cells (Semen)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Pus Cells (Semen)'); 
-- Begin Line: 190 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Stool Microscopy w/o Concetration','Stool'); 
SELECT create_relationship_panel_test('Stool Microscopy w/o Concetration','Pus Cells (Stool)'); 
SELECT create_relationship_sample_test('Stool','Pus Cells (Stool)'); 
SELECT insert_unit_of_measure('NA','Pus Cells (Stool)'); 
SELECT create_relationship_test_section_test('Stool','Pus Cells (Stool)'); 
SELECT add_test_result_type('Pus Cells (Stool)','R'); 
-- Begin Line: 191 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Stool Microscopy with Concetration','Stool'); 
SELECT create_relationship_panel_test('Stool Microscopy with Concetration','Pus Cells (Stool)'); 
SELECT create_relationship_sample_test('Stool','Pus Cells (Stool)'); 
SELECT insert_unit_of_measure('NA','Pus Cells (Stool)'); 
SELECT create_relationship_test_section_test('Stool','Pus Cells (Stool)'); 
SELECT add_test_result_type('Pus Cells (Stool)','R'); 
-- Begin Line: 192 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Urine Microscopy','Urine'); 
SELECT create_relationship_panel_test('Urine Microscopy','Pus Cells (Urine)'); 
SELECT create_relationship_sample_test('Urine','Pus Cells (Urine)'); 
SELECT insert_unit_of_measure('/hPF','Pus Cells (Urine)'); 
SELECT create_relationship_test_section_test('Urine','Pus Cells (Urine)'); 
-- Begin Line: 193 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Stool Microscopy w/o Concetration','Stool'); 
SELECT create_relationship_panel_test('Stool Microscopy w/o Concetration','Quality'); 
SELECT create_relationship_sample_test('Stool','Quality'); 
SELECT insert_unit_of_measure('NA','Quality'); 
SELECT create_relationship_test_section_test('Stool','Quality'); 
SELECT add_test_result_type('Quality','R'); 
-- Begin Line: 194 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Stool Microscopy with Concetration','Stool'); 
SELECT create_relationship_panel_test('Stool Microscopy with Concetration','Quality'); 
SELECT create_relationship_sample_test('Stool','Quality'); 
SELECT insert_unit_of_measure('NA','Quality'); 
SELECT create_relationship_test_section_test('Stool','Quality'); 
SELECT add_test_result_type('Quality','R'); 
-- Begin Line: 195 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Semen'); 
SELECT create_relationship_panel_sampletype('Semen Analysis','Semen'); 
SELECT create_relationship_panel_test('Semen Analysis','RBC (Semen)'); 
SELECT create_relationship_sample_test('Semen','RBC (Semen)'); 
SELECT insert_unit_of_measure('per hpf','RBC (Semen)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','RBC (Semen)'); 
-- Begin Line: 196 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Stool Microscopy w/o Concetration','Stool'); 
SELECT create_relationship_panel_test('Stool Microscopy w/o Concetration','RBC (Stool)'); 
SELECT create_relationship_sample_test('Stool','RBC (Stool)'); 
SELECT insert_unit_of_measure('NA','RBC (Stool)'); 
SELECT create_relationship_test_section_test('Stool','RBC (Stool)'); 
SELECT add_test_result_type('RBC (Stool)','R'); 
-- Begin Line: 197 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Stool Microscopy with Concetration','Stool'); 
SELECT create_relationship_panel_test('Stool Microscopy with Concetration','RBC (Stool)'); 
SELECT create_relationship_sample_test('Stool','RBC (Stool)'); 
SELECT insert_unit_of_measure('NA','RBC (Stool)'); 
SELECT create_relationship_test_section_test('Stool','RBC (Stool)'); 
SELECT add_test_result_type('RBC (Stool)','R'); 
-- Begin Line: 198 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Urine Microscopy','Urine'); 
SELECT create_relationship_panel_test('Urine Microscopy','RBC (Urine)'); 
SELECT create_relationship_sample_test('Urine','RBC (Urine)'); 
SELECT insert_unit_of_measure('/hPF','RBC (Urine)'); 
SELECT create_relationship_test_section_test('Urine','RBC (Urine)'); 
-- Begin Line: 199 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Anaemia Panel','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Anaemia Panel','RBC Count'); 
SELECT create_relationship_sample_test('Blood (EDTA)','RBC Count'); 
SELECT insert_unit_of_measure('per cumm','RBC Count'); 
SELECT create_relationship_test_section_test('Haematology','RBC Count'); 
SELECT insert_result_limit_normal_range('RBC Count',4500000,5500000); 
SELECT insert_result_limit_valid_range('RBC Count',2000000,8000000); 
-- Begin Line: 200 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('CBC','Blood (EDTA)'); 
SELECT create_relationship_panel_test('CBC','RBC Count'); 
SELECT create_relationship_sample_test('Blood (EDTA)','RBC Count'); 
SELECT insert_unit_of_measure('per cumm','RBC Count'); 
SELECT create_relationship_test_section_test('Haematology','RBC Count'); 
SELECT insert_result_limit_normal_range('RBC Count',4500000,5500000); 
SELECT insert_result_limit_valid_range('RBC Count',2000000,8000000); 
-- Begin Line: 201 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Biochemistry','Serum'); 
SELECT create_relationship_panel_test('Biochemistry','RBS'); 
SELECT create_relationship_sample_test('Serum','RBS'); 
SELECT insert_unit_of_measure('mg/dl','RBS'); 
SELECT create_relationship_test_section_test('Biochemistry','RBS'); 
SELECT insert_result_limit_normal_range('RBS',60,160); 
SELECT insert_result_limit_valid_range('RBS',10,1000); 
-- Begin Line: 202 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_panel_sampletype('Environment','Stool'); 
SELECT create_relationship_panel_test('Environment','Reducing substance'); 
SELECT create_relationship_sample_test('Stool','Reducing substance'); 
SELECT insert_unit_of_measure('NA','Reducing substance'); 
SELECT create_relationship_test_section_test('Stool','Reducing substance'); 
SELECT add_test_result_type('Reducing substance','D', 'Negative'); 
SELECT add_test_result_type('Reducing substance','D', 'Positive'); 
SELECT add_test_result_type('Reducing substance','D', 'Positive 1+'); 
SELECT add_test_result_type('Reducing substance','D', 'Positive 2+'); 
SELECT add_test_result_type('Reducing substance','D', 'Positive 3+'); 
SELECT add_test_result_type('Reducing substance','D', 'Positive 4+'); 
-- Begin Line: 203 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_sample_test('Urine','Renal Concentrating Ability'); 
SELECT insert_unit_of_measure('NA','Renal Concentrating Ability'); 
SELECT create_relationship_test_section_test('Urine','Renal Concentrating Ability'); 
SELECT add_test_result_type('Renal Concentrating Ability','R'); 
-- Begin Line: 204 
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
-- Begin Line: 205 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Anaemia Panel','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Anaemia Panel','Reticulocyte Count'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Reticulocyte Count'); 
SELECT insert_unit_of_measure('%','Reticulocyte Count'); 
SELECT create_relationship_test_section_test('Haematology','Reticulocyte Count'); 
SELECT insert_result_limit_normal_range('Reticulocyte Count',1,3); 
SELECT insert_result_limit_valid_range('Reticulocyte Count',0.50,30); 
-- Begin Line: 206 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Routine Blood','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Routine Blood','Reticulocyte Count'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Reticulocyte Count'); 
SELECT insert_unit_of_measure('%','Reticulocyte Count'); 
SELECT create_relationship_test_section_test('Haematology','Reticulocyte Count'); 
SELECT insert_result_limit_normal_range('Reticulocyte Count',1.50,3); 
SELECT insert_result_limit_valid_range('Reticulocyte Count',0.50,30); 
-- Begin Line: 207 
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
-- Begin Line: 208 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Serology','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Serology','Rheumatoid Arthritis - Turbidometry'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Rheumatoid Arthritis - Turbidometry'); 
SELECT insert_unit_of_measure('iU/ml','Rheumatoid Arthritis - Turbidometry'); 
SELECT create_relationship_test_section_test('Serology','Rheumatoid Arthritis - Turbidometry'); 
SELECT insert_result_limit_normal_range('Rheumatoid Arthritis - Turbidometry',0,20); 
-- Begin Line: 209 
SELECT insert_test_section('Microbiology'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_sample_test('Stool','Routine Microbiology Culture (Stool)'); 
SELECT insert_unit_of_measure('','Routine Microbiology Culture (Stool)'); 
SELECT create_relationship_test_section_test('Microbiology','Routine Microbiology Culture (Stool)'); 
SELECT add_test_result_type('Routine Microbiology Culture (Stool)','R'); 
-- Begin Line: 210 
SELECT insert_test_section('Microbiology'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_sample_test('Urine','Routine Microbiology Culture (Urine)'); 
SELECT insert_unit_of_measure('','Routine Microbiology Culture (Urine)'); 
SELECT create_relationship_test_section_test('Microbiology','Routine Microbiology Culture (Urine)'); 
SELECT add_test_result_type('Routine Microbiology Culture (Urine)','R'); 
-- Begin Line: 211 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Liver Function Test','Serum'); 
SELECT create_relationship_panel_test('Liver Function Test','S. Albumin'); 
SELECT create_relationship_sample_test('Serum','S. Albumin'); 
SELECT insert_unit_of_measure('gm/dl','S. Albumin'); 
SELECT create_relationship_test_section_test('Biochemistry','S. Albumin'); 
SELECT insert_result_limit_normal_range('S. Albumin',2.40,5); 
SELECT insert_result_limit_valid_range('S. Albumin',1.50,10); 
-- Begin Line: 212 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Liver Function Test','Serum'); 
SELECT create_relationship_panel_test('Liver Function Test','S. ALT '); 
SELECT create_relationship_sample_test('Serum','S. ALT '); 
SELECT insert_unit_of_measure('mg/dl','S. ALT '); 
SELECT create_relationship_test_section_test('Biochemistry','S. ALT '); 
SELECT insert_result_limit_normal_range('S. ALT ',5,35); 
SELECT insert_result_limit_valid_range('S. ALT ',3,2000); 
-- Begin Line: 213 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Liver Function Test','Serum'); 
SELECT create_relationship_panel_test('Liver Function Test','S. AST'); 
SELECT create_relationship_sample_test('Serum','S. AST'); 
SELECT insert_unit_of_measure('u/L','S. AST'); 
SELECT create_relationship_test_section_test('Biochemistry','S. AST'); 
SELECT insert_result_limit_normal_range('S. AST',5,35); 
SELECT insert_result_limit_valid_range('S. AST',3,3000); 
-- Begin Line: 214 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Liver Function Test','Serum'); 
SELECT create_relationship_panel_test('Liver Function Test','S. Protein'); 
SELECT create_relationship_sample_test('Serum','S. Protein'); 
SELECT insert_unit_of_measure('gm/dl','S. Protein'); 
SELECT create_relationship_test_section_test('Biochemistry','S. Protein'); 
SELECT insert_result_limit_normal_range('S. Protein',6.50,8.50); 
SELECT insert_result_limit_valid_range('S. Protein',1.50,15); 
-- Begin Line: 215 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Lipid Profile','Serum'); 
SELECT create_relationship_panel_test('Lipid Profile','S. Triglyceride'); 
SELECT create_relationship_sample_test('Serum','S. Triglyceride'); 
SELECT insert_unit_of_measure('mg/dl','S. Triglyceride'); 
SELECT create_relationship_test_section_test('Biochemistry','S. Triglyceride'); 
SELECT insert_result_limit_normal_range('S. Triglyceride',40,150); 
SELECT insert_result_limit_valid_range('S. Triglyceride',20,700); 
-- Begin Line: 216 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Scrapping for Fungus'); 
SELECT create_relationship_sample_test('Scrapping for Fungus','Scrapping for Fungus'); 
SELECT insert_unit_of_measure('','Scrapping for Fungus'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Scrapping for Fungus'); 
SELECT add_test_result_type('Scrapping for Fungus','D', 'Negative'); 
SELECT add_test_result_type('Scrapping for Fungus','D', 'Positive'); 
SELECT add_test_result_type('Scrapping for Fungus','D', 'Positive 1+'); 
SELECT add_test_result_type('Scrapping for Fungus','D', 'Positive 2+'); 
SELECT add_test_result_type('Scrapping for Fungus','D', 'Positive 3+'); 
SELECT add_test_result_type('Scrapping for Fungus','D', 'Positive 4+'); 
-- Begin Line: 217 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Anaemia Panel','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Anaemia Panel','Sickling Test'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Sickling Test'); 
SELECT insert_unit_of_measure('NA','Sickling Test'); 
SELECT create_relationship_test_section_test('Haematology','Sickling Test'); 
SELECT add_test_result_type('Sickling Test','D', 'Positive'); 
SELECT add_test_result_type('Sickling Test','D', 'Negative'); 
-- Begin Line: 218 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Sickling Test'); 
SELECT insert_unit_of_measure('NA','Sickling Test'); 
SELECT create_relationship_test_section_test('Haematology','Sickling Test'); 
SELECT add_test_result_type('Sickling Test','D', 'Positive'); 
SELECT add_test_result_type('Sickling Test','D', 'Negative'); 
-- Begin Line: 219 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Electrolyte','Serum'); 
SELECT create_relationship_panel_test('Electrolyte','Sodium'); 
SELECT create_relationship_sample_test('Serum','Sodium'); 
SELECT insert_unit_of_measure('mmole/l','Sodium'); 
SELECT create_relationship_test_section_test('Biochemistry','Sodium'); 
SELECT insert_result_limit_normal_range('Sodium',135,145); 
SELECT insert_result_limit_valid_range('Sodium',60,250); 
-- Begin Line: 220 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Routine Urine','Urine'); 
SELECT create_relationship_panel_test('Routine Urine','Specific Gravity'); 
SELECT create_relationship_sample_test('Urine','Specific Gravity'); 
SELECT insert_unit_of_measure('NA','Specific Gravity'); 
SELECT create_relationship_test_section_test('Urine','Specific Gravity'); 
SELECT insert_result_limit_normal_range('Specific Gravity',1.01,1.03); 
SELECT insert_result_limit_valid_range('Specific Gravity',1,1.05); 
-- Begin Line: 221 
SELECT insert_test_section('Stool'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_sample_test('Stool','Stool occult blood'); 
SELECT insert_unit_of_measure('NA','Stool occult blood'); 
SELECT create_relationship_test_section_test('Stool','Stool occult blood'); 
SELECT add_test_result_type('Stool occult blood','D', 'Negative'); 
SELECT add_test_result_type('Stool occult blood','D', 'Positive'); 
SELECT add_test_result_type('Stool occult blood','D', 'Positive 1+'); 
SELECT add_test_result_type('Stool occult blood','D', 'Positive 2+'); 
SELECT add_test_result_type('Stool occult blood','D', 'Positive 3+'); 
SELECT add_test_result_type('Stool occult blood','D', 'Positive 4+'); 
-- Begin Line: 222 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Asitic Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Asitic Fluid','Sugar (Asitic Fluid)'); 
SELECT create_relationship_sample_test('Body Fluid','Sugar (Asitic Fluid)'); 
SELECT insert_unit_of_measure('mg/dl','Sugar (Asitic Fluid)'); 
SELECT create_relationship_test_section_test('Biochemistry','Sugar (Asitic Fluid)'); 
-- Begin Line: 223 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('CSF','Body Fluid'); 
SELECT create_relationship_panel_test('CSF','Sugar (CSF)'); 
SELECT create_relationship_sample_test('Body Fluid','Sugar (CSF)'); 
SELECT insert_unit_of_measure('mg/dl','Sugar (CSF)'); 
SELECT create_relationship_test_section_test('Biochemistry','Sugar (CSF)'); 
-- Begin Line: 224 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Knee Joint Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Knee Joint Fluid','Sugar (Knee Joint Fluid)'); 
SELECT create_relationship_sample_test('Body Fluid','Sugar (Knee Joint Fluid)'); 
SELECT insert_unit_of_measure('mg/dl','Sugar (Knee Joint Fluid)'); 
SELECT create_relationship_test_section_test('Biochemistry','Sugar (Knee Joint Fluid)'); 
-- Begin Line: 225 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Plural Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Plural Fluid','Sugar (Plural Fluid)'); 
SELECT create_relationship_sample_test('Body Fluid','Sugar (Plural Fluid)'); 
SELECT insert_unit_of_measure('mg/dl','Sugar (Plural Fluid)'); 
SELECT create_relationship_test_section_test('Biochemistry','Sugar (Plural Fluid)'); 
-- Begin Line: 226 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Pus','Body Fluid'); 
SELECT create_relationship_panel_test('Pus','Sugar (Pus)'); 
SELECT create_relationship_sample_test('Body Fluid','Sugar (Pus)'); 
SELECT insert_unit_of_measure('mg/dl','Sugar (Pus)'); 
SELECT create_relationship_test_section_test('Biochemistry','Sugar (Pus)'); 
-- Begin Line: 227 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Pyrotinial Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Pyrotinial Fluid','Sugar (Pyrotinial Fluid)'); 
SELECT create_relationship_sample_test('Body Fluid','Sugar (Pyrotinial Fluid)'); 
SELECT insert_unit_of_measure('mg/dl','Sugar (Pyrotinial Fluid)'); 
SELECT create_relationship_test_section_test('Biochemistry','Sugar (Pyrotinial Fluid)'); 
-- Begin Line: 228 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Routine Urine','Urine'); 
SELECT create_relationship_panel_test('Routine Urine','Sugar (Routine Urine)'); 
SELECT create_relationship_sample_test('Urine','Sugar (Routine Urine)'); 
SELECT insert_unit_of_measure('NA','Sugar (Routine Urine)'); 
SELECT create_relationship_test_section_test('Urine','Sugar (Routine Urine)'); 
SELECT add_test_result_type('Sugar (Routine Urine)','D', 'Nil'); 
SELECT add_test_result_type('Sugar (Routine Urine)','D', 'Trace'); 
SELECT add_test_result_type('Sugar (Routine Urine)','D', '1+'); 
SELECT add_test_result_type('Sugar (Routine Urine)','D', '2+'); 
SELECT add_test_result_type('Sugar (Routine Urine)','D', '3+'); 
SELECT add_test_result_type('Sugar (Routine Urine)','D', '4+'); 
-- Begin Line: 229 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Thyroid Function','Serum'); 
SELECT create_relationship_panel_test('Thyroid Function','T3'); 
SELECT create_relationship_sample_test('Serum','T3'); 
SELECT insert_unit_of_measure('ng/dl','T3'); 
SELECT create_relationship_test_section_test('Biochemistry','T3'); 
SELECT insert_result_limit_normal_range('T3',60,200); 
SELECT insert_result_limit_valid_range('T3',10,600); 
-- Begin Line: 230 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Thyroid Function','Serum'); 
SELECT create_relationship_panel_test('Thyroid Function','T4'); 
SELECT create_relationship_sample_test('Serum','T4'); 
SELECT insert_unit_of_measure('ug/dl','T4'); 
SELECT create_relationship_test_section_test('Biochemistry','T4'); 
SELECT insert_result_limit_normal_range('T4',4.50,12); 
SELECT insert_result_limit_valid_range('T4',1,50); 
-- Begin Line: 231 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Lipid Profile','Serum'); 
SELECT create_relationship_panel_test('Lipid Profile','TC/HDL Ratio'); 
SELECT create_relationship_sample_test('Serum','TC/HDL Ratio'); 
SELECT insert_unit_of_measure('','TC/HDL Ratio'); 
SELECT create_relationship_test_section_test('Biochemistry','TC/HDL Ratio'); 
SELECT insert_result_limit_normal_range('TC/HDL Ratio',3.50,3.50); 
SELECT insert_result_limit_valid_range('TC/HDL Ratio',3.50,3.50); 
-- Begin Line: 232 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_sample_test('Serum','TORCH (IGG)'); 
SELECT insert_unit_of_measure('','TORCH (IGG)'); 
SELECT create_relationship_test_section_test('Biochemistry','TORCH (IGG)'); 
SELECT add_test_result_type('TORCH (IGG)','R'); 
-- Begin Line: 233 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_sample_test('Serum','TORCH (IGM)'); 
SELECT insert_unit_of_measure('','TORCH (IGM)'); 
SELECT create_relationship_test_section_test('Biochemistry','TORCH (IGM)'); 
SELECT add_test_result_type('TORCH (IGM)','R'); 
-- Begin Line: 234 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Liver Function Test','Serum'); 
SELECT create_relationship_panel_test('Liver Function Test','Total Bilirubin'); 
SELECT create_relationship_sample_test('Serum','Total Bilirubin'); 
SELECT insert_unit_of_measure('mg/dl','Total Bilirubin'); 
SELECT create_relationship_test_section_test('Biochemistry','Total Bilirubin'); 
SELECT insert_result_limit_normal_range('Total Bilirubin',0.30,1.20); 
SELECT insert_result_limit_valid_range('Total Bilirubin',0.20,50); 
-- Begin Line: 235 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Semen'); 
SELECT create_relationship_panel_sampletype('Semen Analysis','Semen'); 
SELECT create_relationship_panel_test('Semen Analysis','Total Count'); 
SELECT create_relationship_sample_test('Semen','Total Count'); 
SELECT insert_unit_of_measure('million/ ml','Total Count'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Total Count'); 
SELECT insert_result_limit_normal_range('Total Count',60,150); 
-- Begin Line: 236 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Asitic Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Asitic Fluid','Total Leucocyte Count (Asitic Fluid)'); 
SELECT create_relationship_sample_test('Body Fluid','Total Leucocyte Count (Asitic Fluid)'); 
SELECT insert_unit_of_measure('cumm','Total Leucocyte Count (Asitic Fluid)'); 
SELECT create_relationship_test_section_test('Haematology','Total Leucocyte Count (Asitic Fluid)'); 
-- Begin Line: 237 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Routine Blood','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Routine Blood','Total Leucocyte Count (Blood)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Total Leucocyte Count (Blood)'); 
SELECT insert_unit_of_measure('cumm','Total Leucocyte Count (Blood)'); 
SELECT create_relationship_test_section_test('Haematology','Total Leucocyte Count (Blood)'); 
SELECT insert_result_limit_normal_range('Total Leucocyte Count (Blood)',4500,11000); 
SELECT insert_result_limit_valid_range('Total Leucocyte Count (Blood)',500,400000); 
-- Begin Line: 238 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('CBC','Blood (EDTA)'); 
SELECT create_relationship_panel_test('CBC','Total Leucocyte Count (Blood)'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Total Leucocyte Count (Blood)'); 
SELECT insert_unit_of_measure('cumm','Total Leucocyte Count (Blood)'); 
SELECT create_relationship_test_section_test('Haematology','Total Leucocyte Count (Blood)'); 
SELECT insert_result_limit_normal_range('Total Leucocyte Count (Blood)',4500,11000); 
SELECT insert_result_limit_valid_range('Total Leucocyte Count (Blood)',500,400000); 
-- Begin Line: 239 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('CSF','Body Fluid'); 
SELECT create_relationship_panel_test('CSF','Total Leucocyte Count (CSF)'); 
SELECT create_relationship_sample_test('Body Fluid','Total Leucocyte Count (CSF)'); 
SELECT insert_unit_of_measure('cumm','Total Leucocyte Count (CSF)'); 
SELECT create_relationship_test_section_test('Haematology','Total Leucocyte Count (CSF)'); 
-- Begin Line: 240 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Knee Joint Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Knee Joint Fluid','Total Leucocyte Count (Knee Joint Fluid)'); 
SELECT create_relationship_sample_test('Body Fluid','Total Leucocyte Count (Knee Joint Fluid)'); 
SELECT insert_unit_of_measure('cumm','Total Leucocyte Count (Knee Joint Fluid)'); 
SELECT create_relationship_test_section_test('Haematology','Total Leucocyte Count (Knee Joint Fluid)'); 
-- Begin Line: 241 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Plural Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Plural Fluid','Total Leucocyte Count (Plural Fluid)'); 
SELECT create_relationship_sample_test('Body Fluid','Total Leucocyte Count (Plural Fluid)'); 
SELECT insert_unit_of_measure('cumm','Total Leucocyte Count (Plural Fluid)'); 
SELECT create_relationship_test_section_test('Haematology','Total Leucocyte Count (Plural Fluid)'); 
-- Begin Line: 242 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Pus','Body Fluid'); 
SELECT create_relationship_panel_test('Pus','Total Leucocyte Count (Pus)'); 
SELECT create_relationship_sample_test('Body Fluid','Total Leucocyte Count (Pus)'); 
SELECT insert_unit_of_measure('cumm','Total Leucocyte Count (Pus)'); 
SELECT create_relationship_test_section_test('Haematology','Total Leucocyte Count (Pus)'); 
-- Begin Line: 243 
SELECT insert_test_section('Haematology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Pyrotinial Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Pyrotinial Fluid','Total Leucocyte Count (Pyrotinial Fluid)'); 
SELECT create_relationship_sample_test('Body Fluid','Total Leucocyte Count (Pyrotinial Fluid)'); 
SELECT insert_unit_of_measure('cumm','Total Leucocyte Count (Pyrotinial Fluid)'); 
SELECT create_relationship_test_section_test('Haematology','Total Leucocyte Count (Pyrotinial Fluid)'); 
-- Begin Line: 244 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Serology','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Serology','Troponin - T'); 
SELECT create_relationship_sample_test('Blood (EDTA)','Troponin - T'); 
SELECT insert_unit_of_measure('NA','Troponin - T'); 
SELECT create_relationship_test_section_test('Serology','Troponin - T'); 
SELECT add_test_result_type('Troponin - T','D', 'Positive'); 
SELECT add_test_result_type('Troponin - T','D', 'Negative'); 
-- Begin Line: 245 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Thyroid Function','Serum'); 
SELECT create_relationship_panel_test('Thyroid Function','TSH'); 
SELECT create_relationship_sample_test('Serum','TSH'); 
SELECT insert_unit_of_measure('Iu/ml','TSH'); 
SELECT create_relationship_test_section_test('Biochemistry','TSH'); 
SELECT insert_result_limit_normal_range('TSH',0.30,5.50); 
SELECT insert_result_limit_valid_range('TSH',0.10,30); 
-- Begin Line: 246 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Biochemistry','Serum'); 
SELECT create_relationship_panel_test('Biochemistry','Urea Nitrogen'); 
SELECT create_relationship_sample_test('Serum','Urea Nitrogen'); 
SELECT insert_unit_of_measure('mg/dl','Urea Nitrogen'); 
SELECT create_relationship_test_section_test('Biochemistry','Urea Nitrogen'); 
SELECT insert_result_limit_normal_range('Urea Nitrogen',40,150); 
SELECT insert_result_limit_valid_range('Urea Nitrogen',10,1000); 
-- Begin Line: 247 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Biochemistry','Serum'); 
SELECT create_relationship_panel_test('Biochemistry','Uric Acid'); 
SELECT create_relationship_sample_test('Serum','Uric Acid'); 
SELECT insert_unit_of_measure('mg/dl','Uric Acid'); 
SELECT create_relationship_test_section_test('Biochemistry','Uric Acid'); 
SELECT insert_result_limit_normal_range('Uric Acid',3.50,7.20); 
SELECT insert_result_limit_valid_range('Uric Acid',2,30); 
-- Begin Line: 248 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_sample_test('Urine','Urine Bile Pigment'); 
SELECT insert_unit_of_measure('NA','Urine Bile Pigment'); 
SELECT create_relationship_test_section_test('Urine','Urine Bile Pigment'); 
SELECT add_test_result_type('Urine Bile Pigment','D', 'Negative'); 
SELECT add_test_result_type('Urine Bile Pigment','D', 'Positive'); 
SELECT add_test_result_type('Urine Bile Pigment','D', 'Positive 1+'); 
SELECT add_test_result_type('Urine Bile Pigment','D', 'Positive 2+'); 
SELECT add_test_result_type('Urine Bile Pigment','D', 'Positive 3+'); 
SELECT add_test_result_type('Urine Bile Pigment','D', 'Positive 4+'); 
-- Begin Line: 249 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_sample_test('Urine','Urine Bile Salt'); 
SELECT insert_unit_of_measure('NA','Urine Bile Salt'); 
SELECT create_relationship_test_section_test('Urine','Urine Bile Salt'); 
SELECT add_test_result_type('Urine Bile Salt','D', 'Negative'); 
SELECT add_test_result_type('Urine Bile Salt','D', 'Positive'); 
SELECT add_test_result_type('Urine Bile Salt','D', 'Positive 1+'); 
SELECT add_test_result_type('Urine Bile Salt','D', 'Positive 2+'); 
SELECT add_test_result_type('Urine Bile Salt','D', 'Positive 3+'); 
SELECT add_test_result_type('Urine Bile Salt','D', 'Positive 4+'); 
-- Begin Line: 250 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Urine Protein Creatinine Ratio','Urine'); 
SELECT create_relationship_panel_test('Urine Protein Creatinine Ratio','Urine Creatinine'); 
SELECT create_relationship_sample_test('Urine','Urine Creatinine'); 
SELECT insert_unit_of_measure('mg/dl','Urine Creatinine'); 
SELECT create_relationship_test_section_test('Biochemistry','Urine Creatinine'); 
-- Begin Line: 251 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_sample_test('Urine','Urine Ketone'); 
SELECT insert_unit_of_measure('NA','Urine Ketone'); 
SELECT create_relationship_test_section_test('Urine','Urine Ketone'); 
SELECT add_test_result_type('Urine Ketone','D', 'Nil'); 
SELECT add_test_result_type('Urine Ketone','D', 'Trace'); 
SELECT add_test_result_type('Urine Ketone','D', '1+'); 
SELECT add_test_result_type('Urine Ketone','D', '2+'); 
SELECT add_test_result_type('Urine Ketone','D', '3+'); 
SELECT add_test_result_type('Urine Ketone','D', '4+'); 
-- Begin Line: 252 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_sample_test('Urine','Urine Pregnancy Test'); 
SELECT insert_unit_of_measure('','Urine Pregnancy Test'); 
SELECT create_relationship_test_section_test('Urine','Urine Pregnancy Test'); 
SELECT add_test_result_type('Urine Pregnancy Test','D', 'Positive'); 
SELECT add_test_result_type('Urine Pregnancy Test','D', 'Negative'); 
-- Begin Line: 253 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Urine Protein Creatinine Ratio','Urine'); 
SELECT create_relationship_panel_test('Urine Protein Creatinine Ratio','Urine Protein'); 
SELECT create_relationship_sample_test('Urine','Urine Protein'); 
SELECT insert_unit_of_measure('mg/dl','Urine Protein'); 
SELECT create_relationship_test_section_test('Biochemistry','Urine Protein'); 
SELECT insert_result_limit_normal_range('Urine Protein',20,120); 
-- Begin Line: 254 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Urine Protein Creatinine Ratio','Urine'); 
SELECT create_relationship_panel_test('Urine Protein Creatinine Ratio','Urine Protein Creatinine Ratio'); 
SELECT create_relationship_sample_test('Urine','Urine Protein Creatinine Ratio'); 
SELECT insert_unit_of_measure('','Urine Protein Creatinine Ratio'); 
SELECT create_relationship_test_section_test('Biochemistry','Urine Protein Creatinine Ratio'); 
SELECT insert_result_limit_normal_range('Urine Protein Creatinine Ratio',0.20,0.20); 
-- Begin Line: 255 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Urine Microscopy','Urine'); 
SELECT create_relationship_panel_test('Urine Microscopy','Vaginal Trichomonas'); 
SELECT create_relationship_sample_test('Urine','Vaginal Trichomonas'); 
SELECT insert_unit_of_measure('/hPF','Vaginal Trichomonas'); 
SELECT create_relationship_test_section_test('Urine','Vaginal Trichomonas'); 
SELECT add_test_result_type('Vaginal Trichomonas','R'); 
-- Begin Line: 256 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Cross Match','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Cross Match','VDRL ELISA'); 
SELECT create_relationship_sample_test('Blood (EDTA)','VDRL ELISA'); 
SELECT insert_unit_of_measure('NA','VDRL ELISA'); 
SELECT create_relationship_test_section_test('Serology','VDRL ELISA'); 
SELECT add_test_result_type('VDRL ELISA','D', 'Positive'); 
SELECT add_test_result_type('VDRL ELISA','D', 'Negative'); 
-- Begin Line: 257 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Serology','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Serology','VDRL ELISA'); 
SELECT create_relationship_sample_test('Blood (EDTA)','VDRL ELISA'); 
SELECT insert_unit_of_measure('NA','VDRL ELISA'); 
SELECT create_relationship_test_section_test('Serology','VDRL ELISA'); 
SELECT add_test_result_type('VDRL ELISA','D', 'Positive'); 
SELECT add_test_result_type('VDRL ELISA','D', 'Negative'); 
-- Begin Line: 258 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Cross Match','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Cross Match','VDRL Rapid'); 
SELECT create_relationship_sample_test('Blood (EDTA)','VDRL Rapid'); 
SELECT insert_unit_of_measure('NA','VDRL Rapid'); 
SELECT create_relationship_test_section_test('Serology','VDRL Rapid'); 
SELECT add_test_result_type('VDRL Rapid','D', 'Positive'); 
SELECT add_test_result_type('VDRL Rapid','D', 'Negative'); 
-- Begin Line: 259 
SELECT insert_test_section('Serology'); 
SELECT insert_sample_type('Blood (EDTA)'); 
SELECT create_relationship_panel_sampletype('Serology','Blood (EDTA)'); 
SELECT create_relationship_panel_test('Serology','VDRL Rapid'); 
SELECT create_relationship_sample_test('Blood (EDTA)','VDRL Rapid'); 
SELECT insert_unit_of_measure('NA','VDRL Rapid'); 
SELECT create_relationship_test_section_test('Serology','VDRL Rapid'); 
SELECT add_test_result_type('VDRL Rapid','D', 'Positive'); 
SELECT add_test_result_type('VDRL Rapid','D', 'Negative'); 
-- Begin Line: 260 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Semen'); 
SELECT create_relationship_panel_sampletype('Semen Analysis','Semen'); 
SELECT create_relationship_panel_test('Semen Analysis','Viscoscity '); 
SELECT create_relationship_sample_test('Semen','Viscoscity '); 
SELECT insert_unit_of_measure('','Viscoscity '); 
SELECT create_relationship_test_section_test('Clinical Pathology','Viscoscity '); 
SELECT add_test_result_type('Viscoscity ','R'); 
-- Begin Line: 261 
SELECT insert_test_section('Biochemistry'); 
SELECT insert_sample_type('Serum'); 
SELECT create_relationship_panel_sampletype('Lipid Profile','Serum'); 
SELECT create_relationship_panel_test('Lipid Profile','VLDL Cholesterol'); 
SELECT create_relationship_sample_test('Serum','VLDL Cholesterol'); 
SELECT insert_unit_of_measure('mg/dl','VLDL Cholesterol'); 
SELECT create_relationship_test_section_test('Biochemistry','VLDL Cholesterol'); 
SELECT insert_result_limit_normal_range('VLDL Cholesterol',5,40); 
SELECT insert_result_limit_valid_range('VLDL Cholesterol',1,60); 
-- Begin Line: 262 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Semen'); 
SELECT create_relationship_panel_sampletype('Semen Analysis','Semen'); 
SELECT create_relationship_panel_test('Semen Analysis','Volume (Semen)'); 
SELECT create_relationship_sample_test('Semen','Volume (Semen)'); 
SELECT insert_unit_of_measure('per ml','Volume (Semen)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','Volume (Semen)'); 
SELECT insert_result_limit_normal_range('Volume (Semen)',2,3.50); 
-- Begin Line: 263 
SELECT insert_test_section('Vaginal'); 
SELECT insert_sample_type('Vaginal'); 
SELECT create_relationship_panel_sampletype('Vaginal Panel','Vaginal'); 
SELECT create_relationship_panel_test('Vaginal Panel','Whiff test'); 
SELECT create_relationship_sample_test('Vaginal','Whiff test'); 
SELECT insert_unit_of_measure('NA','Whiff test'); 
SELECT create_relationship_test_section_test('Vaginal','Whiff test'); 
SELECT add_test_result_type('Whiff test','D', 'Positive'); 
SELECT add_test_result_type('Whiff test','D', 'Negative'); 
-- Begin Line: 264 
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
-- Begin Line: 265 
SELECT insert_test_section('Urine'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_panel_sampletype('Urine Microscopy','Urine'); 
SELECT create_relationship_panel_test('Urine Microscopy','Yeast Cell'); 
SELECT create_relationship_sample_test('Urine','Yeast Cell'); 
SELECT insert_unit_of_measure('/hPF','Yeast Cell'); 
SELECT create_relationship_test_section_test('Urine','Yeast Cell'); 
SELECT add_test_result_type('Yeast Cell','R'); 
-- Begin Line: 266 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Urine'); 
SELECT create_relationship_sample_test('Urine','ZN Stain (Urine)'); 
SELECT insert_unit_of_measure('NA','ZN Stain (Urine)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','ZN Stain (Urine)'); 
SELECT add_test_result_type('ZN Stain (Urine)','D', 'Positive 1+'); 
SELECT add_test_result_type('ZN Stain (Urine)','D', 'Positive 2+'); 
SELECT add_test_result_type('ZN Stain (Urine)','D', 'Positive 3+'); 
SELECT add_test_result_type('ZN Stain (Urine)','D', 'Positive 4+'); 
SELECT add_test_result_type('ZN Stain (Urine)','D', 'Positive 5+'); 
SELECT add_test_result_type('ZN Stain (Urine)','D', 'Positive 6+'); 
SELECT add_test_result_type('ZN Stain (Urine)','D', 'Negative'); 
-- Begin Line: 267 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Stool'); 
SELECT create_relationship_sample_test('Stool','ZN Stain (Stool)'); 
SELECT insert_unit_of_measure('NA','ZN Stain (Stool)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','ZN Stain (Stool)'); 
SELECT add_test_result_type('ZN Stain (Stool)','D', 'Positive 1+'); 
SELECT add_test_result_type('ZN Stain (Stool)','D', 'Positive 2+'); 
SELECT add_test_result_type('ZN Stain (Stool)','D', 'Positive 3+'); 
SELECT add_test_result_type('ZN Stain (Stool)','D', 'Positive 4+'); 
SELECT add_test_result_type('ZN Stain (Stool)','D', 'Positive 5+'); 
SELECT add_test_result_type('ZN Stain (Stool)','D', 'Positive 6+'); 
SELECT add_test_result_type('ZN Stain (Stool)','D', 'Negative'); 
-- Begin Line: 268 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Sputum'); 
SELECT create_relationship_sample_test('Sputum','ZN Stain (Sputum)'); 
SELECT insert_unit_of_measure('NA','ZN Stain (Sputum)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','ZN Stain (Sputum)'); 
SELECT add_test_result_type('ZN Stain (Sputum)','D', 'Positive 1+'); 
SELECT add_test_result_type('ZN Stain (Sputum)','D', 'Positive 2+'); 
SELECT add_test_result_type('ZN Stain (Sputum)','D', 'Positive 3+'); 
SELECT add_test_result_type('ZN Stain (Sputum)','D', 'Positive 4+'); 
SELECT add_test_result_type('ZN Stain (Sputum)','D', 'Positive 5+'); 
SELECT add_test_result_type('ZN Stain (Sputum)','D', 'Positive 6+'); 
SELECT add_test_result_type('ZN Stain (Sputum)','D', 'Negative'); 
-- Begin Line: 269 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_sample_test('Body Fluid','ZN Stain (Body Fluid)'); 
SELECT insert_unit_of_measure('NA','ZN Stain (Body Fluid)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','ZN Stain (Body Fluid)'); 
SELECT add_test_result_type('ZN Stain (Body Fluid)','D', 'Positive 1+'); 
SELECT add_test_result_type('ZN Stain (Body Fluid)','D', 'Positive 2+'); 
SELECT add_test_result_type('ZN Stain (Body Fluid)','D', 'Positive 3+'); 
SELECT add_test_result_type('ZN Stain (Body Fluid)','D', 'Positive 4+'); 
SELECT add_test_result_type('ZN Stain (Body Fluid)','D', 'Positive 5+'); 
SELECT add_test_result_type('ZN Stain (Body Fluid)','D', 'Positive 6+'); 
SELECT add_test_result_type('ZN Stain (Body Fluid)','D', 'Negative'); 
-- Begin Line: 270 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Pus'); 
SELECT create_relationship_sample_test('Pus','ZN Stain (Pus)'); 
SELECT insert_unit_of_measure('NA','ZN Stain (Pus)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','ZN Stain (Pus)'); 
SELECT add_test_result_type('ZN Stain (Pus)','D', 'Positive 1+'); 
SELECT add_test_result_type('ZN Stain (Pus)','D', 'Positive 2+'); 
SELECT add_test_result_type('ZN Stain (Pus)','D', 'Positive 3+'); 
SELECT add_test_result_type('ZN Stain (Pus)','D', 'Positive 4+'); 
SELECT add_test_result_type('ZN Stain (Pus)','D', 'Positive 5+'); 
SELECT add_test_result_type('ZN Stain (Pus)','D', 'Positive 6+'); 
SELECT add_test_result_type('ZN Stain (Pus)','D', 'Negative'); 
-- Begin Line: 271 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Slit Skin'); 
SELECT create_relationship_sample_test('Slit Skin','ZN Stain (Slit Skin)'); 
SELECT insert_unit_of_measure('NA','ZN Stain (Slit Skin)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','ZN Stain (Slit Skin)'); 
SELECT add_test_result_type('ZN Stain (Slit Skin)','D', 'Positive 1+'); 
SELECT add_test_result_type('ZN Stain (Slit Skin)','D', 'Positive 2+'); 
SELECT add_test_result_type('ZN Stain (Slit Skin)','D', 'Positive 3+'); 
SELECT add_test_result_type('ZN Stain (Slit Skin)','D', 'Positive 4+'); 
SELECT add_test_result_type('ZN Stain (Slit Skin)','D', 'Positive 5+'); 
SELECT add_test_result_type('ZN Stain (Slit Skin)','D', 'Positive 6+'); 
SELECT add_test_result_type('ZN Stain (Slit Skin)','D', 'Negative'); 
-- Begin Line: 272 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Asitic Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Asitic Fluid','ZN Stain (Asitic Fluid)'); 
SELECT create_relationship_sample_test('Body Fluid','ZN Stain (Asitic Fluid)'); 
SELECT insert_unit_of_measure('','ZN Stain (Asitic Fluid)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','ZN Stain (Asitic Fluid)'); 
SELECT add_test_result_type('ZN Stain (Asitic Fluid)','D', 'Positive 1+'); 
SELECT add_test_result_type('ZN Stain (Asitic Fluid)','D', 'Positive 2+'); 
SELECT add_test_result_type('ZN Stain (Asitic Fluid)','D', 'Positive 3+'); 
SELECT add_test_result_type('ZN Stain (Asitic Fluid)','D', 'Positive 4+'); 
SELECT add_test_result_type('ZN Stain (Asitic Fluid)','D', 'Positive 5+'); 
SELECT add_test_result_type('ZN Stain (Asitic Fluid)','D', 'Positive 6+'); 
SELECT add_test_result_type('ZN Stain (Asitic Fluid)','D', 'Negative'); 
-- Begin Line: 273 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('CSF','Body Fluid'); 
SELECT create_relationship_panel_test('CSF','ZN Stain (CSF)'); 
SELECT create_relationship_sample_test('Body Fluid','ZN Stain (CSF)'); 
SELECT insert_unit_of_measure('','ZN Stain (CSF)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','ZN Stain (CSF)'); 
SELECT add_test_result_type('ZN Stain (CSF)','D', 'Positive 1+'); 
SELECT add_test_result_type('ZN Stain (CSF)','D', 'Positive 2+'); 
SELECT add_test_result_type('ZN Stain (CSF)','D', 'Positive 3+'); 
SELECT add_test_result_type('ZN Stain (CSF)','D', 'Positive 4+'); 
SELECT add_test_result_type('ZN Stain (CSF)','D', 'Positive 5+'); 
SELECT add_test_result_type('ZN Stain (CSF)','D', 'Positive 6+'); 
SELECT add_test_result_type('ZN Stain (CSF)','D', 'Negative'); 
-- Begin Line: 274 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Knee Joint Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Knee Joint Fluid','ZN Stain (Knee Joint Fluid)'); 
SELECT create_relationship_sample_test('Body Fluid','ZN Stain (Knee Joint Fluid)'); 
SELECT insert_unit_of_measure('','ZN Stain (Knee Joint Fluid)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','ZN Stain (Knee Joint Fluid)'); 
SELECT add_test_result_type('ZN Stain (Knee Joint Fluid)','D', 'Positive 1+'); 
SELECT add_test_result_type('ZN Stain (Knee Joint Fluid)','D', 'Positive 2+'); 
SELECT add_test_result_type('ZN Stain (Knee Joint Fluid)','D', 'Positive 3+'); 
SELECT add_test_result_type('ZN Stain (Knee Joint Fluid)','D', 'Positive 4+'); 
SELECT add_test_result_type('ZN Stain (Knee Joint Fluid)','D', 'Positive 5+'); 
SELECT add_test_result_type('ZN Stain (Knee Joint Fluid)','D', 'Positive 6+'); 
SELECT add_test_result_type('ZN Stain (Knee Joint Fluid)','D', 'Negative'); 
-- Begin Line: 275 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Plural Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Plural Fluid','ZN Stain (Plural Fluid)'); 
SELECT create_relationship_sample_test('Body Fluid','ZN Stain (Plural Fluid)'); 
SELECT insert_unit_of_measure('','ZN Stain (Plural Fluid)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','ZN Stain (Plural Fluid)'); 
SELECT add_test_result_type('ZN Stain (Plural Fluid)','D', 'Positive 1+'); 
SELECT add_test_result_type('ZN Stain (Plural Fluid)','D', 'Positive 2+'); 
SELECT add_test_result_type('ZN Stain (Plural Fluid)','D', 'Positive 3+'); 
SELECT add_test_result_type('ZN Stain (Plural Fluid)','D', 'Positive 4+'); 
SELECT add_test_result_type('ZN Stain (Plural Fluid)','D', 'Positive 5+'); 
SELECT add_test_result_type('ZN Stain (Plural Fluid)','D', 'Positive 6+'); 
SELECT add_test_result_type('ZN Stain (Plural Fluid)','D', 'Negative'); 
-- Begin Line: 276 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Pus','Body Fluid'); 
SELECT create_relationship_panel_test('Pus','ZN Stain (Body Fluid) (Pus)'); 
SELECT create_relationship_sample_test('Body Fluid','ZN Stain (Body Fluid) (Pus)'); 
SELECT insert_unit_of_measure('','ZN Stain (Body Fluid) (Pus)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','ZN Stain (Body Fluid) (Pus)'); 
SELECT add_test_result_type('ZN Stain (Body Fluid) (Pus)','D', 'Positive 1+'); 
SELECT add_test_result_type('ZN Stain (Body Fluid) (Pus)','D', 'Positive 2+'); 
SELECT add_test_result_type('ZN Stain (Body Fluid) (Pus)','D', 'Positive 3+'); 
SELECT add_test_result_type('ZN Stain (Body Fluid) (Pus)','D', 'Positive 4+'); 
SELECT add_test_result_type('ZN Stain (Body Fluid) (Pus)','D', 'Positive 5+'); 
SELECT add_test_result_type('ZN Stain (Body Fluid) (Pus)','D', 'Positive 6+'); 
SELECT add_test_result_type('ZN Stain (Body Fluid) (Pus)','D', 'Negative'); 
-- Begin Line: 277 
SELECT insert_test_section('Clinical Pathology'); 
SELECT insert_sample_type('Body Fluid'); 
SELECT create_relationship_panel_sampletype('Pyrotinial Fluid','Body Fluid'); 
SELECT create_relationship_panel_test('Pyrotinial Fluid','ZN Stain (Pyrotinial Fluid)'); 
SELECT create_relationship_sample_test('Body Fluid','ZN Stain (Pyrotinial Fluid)'); 
SELECT insert_unit_of_measure('','ZN Stain (Pyrotinial Fluid)'); 
SELECT create_relationship_test_section_test('Clinical Pathology','ZN Stain (Pyrotinial Fluid)'); 
SELECT add_test_result_type('ZN Stain (Pyrotinial Fluid)','D', 'Positive 1+'); 
SELECT add_test_result_type('ZN Stain (Pyrotinial Fluid)','D', 'Positive 2+'); 
SELECT add_test_result_type('ZN Stain (Pyrotinial Fluid)','D', 'Positive 3+'); 
SELECT add_test_result_type('ZN Stain (Pyrotinial Fluid)','D', 'Positive 4+'); 
SELECT add_test_result_type('ZN Stain (Pyrotinial Fluid)','D', 'Positive 5+'); 
SELECT add_test_result_type('ZN Stain (Pyrotinial Fluid)','D', 'Positive 6+'); 
SELECT add_test_result_type('ZN Stain (Pyrotinial Fluid)','D', 'Negative'); 
