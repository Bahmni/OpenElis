-- This will delete all haiti openelis data that is not needed for Bahmni.

begin;

set search_path to clinlims;
set constraints all deferred;

delete from result_signature;
delete from result;
delete from analysis;

delete from analysis_qaevent;
delete from analysis_qaevent_action;
delete from analysis_storages;
delete from analysis_users;
delete from analyzer_results;
delete from city_state_zip;
delete from dictionary;
delete from dictionary_category;
delete from document_track;
delete from history;
delete from login_user where id > 130;

delete from code_element_xref;
delete from receiver_code_element;
delete from message_org;

delete from note;
delete from org_hl7_encoding_type;

delete from result_limits;
delete from test_reflex;
delete from test_result;
delete from test_analyte;
delete from sampletype_test;
delete from test;
delete from test_section;
delete from inventory_receipt;
delete from organization;

delete from organization_organization_type;

delete from sampletype_panel;
delete from panel_item;
delete from panel;

delete from patient_identity;
delete from sample_human;
delete from patient_patient_type;
delete from patient;

delete from provider;
delete from person;
delete from person_address;
delete from qa_observation;
delete from region;
delete from report_external_export;

delete from sample_qaevent;
delete from sample_item;
delete from sample;

delete from sample_animal;
delete from sample_requester;
delete from state_code;
delete from system_user where id > 106;
delete from test_trailer;
delete from type_of_sample;
delete from unit_of_measure;

delete from databasechangelog where filename not ilike '%bahmni%';

commit;
