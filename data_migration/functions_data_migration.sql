CREATE OR REPLACE FUNCTION insert_test_section(test_section_name TEXT) RETURNS VOID AS
$$
DECLARE
    test_section_id int;
    org_id int;
    result_role_id int;
    validation_role_id int;
BEGIN

    SELECT id INTO test_section_id FROM clinlims.test_section WHERE name = test_section_name;
        IF NOT FOUND THEN
            BEGIN
                SELECT id INTO STRICT org_id from clinlims.organization WHERE name = 'JSS';
                EXCEPTION   
                    WHEN NO_DATA_FOUND THEN
                    RAISE EXCEPTION 'organization % not found', 'JSS';
                    WHEN TOO_MANY_ROWS THEN
                    RAISE EXCEPTION 'organization % not unique', 'JSS';   
            END;

            BEGIN
                SELECT id INTO STRICT result_role_id from clinlims.system_role WHERE name like 'Results%entry%';
                EXCEPTION   
                    WHEN NO_DATA_FOUND THEN
                    RAISE EXCEPTION 'role % not found', 'Result Entry';
                    WHEN TOO_MANY_ROWS THEN
                    RAISE EXCEPTION 'role % not unique', 'Result Entry';   
            END;

            BEGIN
                SELECT id INTO STRICT validation_role_id from clinlims.system_role WHERE name like 'Validation%';
                EXCEPTION   
                    WHEN NO_DATA_FOUND THEN
                    RAISE EXCEPTION 'role % not found', 'Validation';
                    WHEN TOO_MANY_ROWS THEN
                    RAISE EXCEPTION 'role % not unique', 'Validation';   
            END;

            INSERT INTO clinlims.test_section (id, name, description, org_id, is_external, lastupdated, sort_order, is_active)
            VALUES (nextval('clinlims.test_section_seq'), test_section_name, test_section_name, org_id, 'N', localtimestamp, 0, 'Y');


            INSERT INTO clinlims.system_module (id, name, description, has_select_flag, has_add_flag, has_update_flag, has_delete_flag)
            VALUES (nextval('clinlims.system_module_seq'), 'LogbookResults:' || test_section_name, 'Results=>Enter=>' || test_section_name, 'Y', 'Y', 'Y', 'Y');                     


            INSERT INTO clinlims.system_role_module (id, has_select, has_add, has_update, has_delete, system_role_id, system_module_id)
            VALUES (nextval('clinlims.system_user_module_seq'), 'Y', 'Y', 'Y', 'Y', result_role_id, currval('clinlims.system_module_seq'));


            INSERT INTO clinlims.system_role_module (id, has_select, has_add, has_update, has_delete, system_role_id, system_module_id)
            VALUES (nextval('clinlims.system_user_module_seq'), 'Y', 'Y', 'Y', 'Y', validation_role_id, currval('clinlims.system_module_seq'));

            
        END IF;
END
$$
LANGUAGE plpgsql;



CREATE OR REPLACE FUNCTION insert_sample_type(sample_type_name TEXT) RETURNS VOID AS
$$
DECLARE
    sample_type_id int;
BEGIN

    SELECT id INTO sample_type_id FROM clinlims.type_of_sample WHERE description = sample_type_name;
    IF NOT FOUND THEN

        INSERT INTO clinlims.type_of_sample (id, description, domain, lastupdated, local_abbrev, is_active, sort_order)
        VALUES (nextval('clinlims.type_of_sample_seq'), sample_type_name, 'H', localtimestamp, SUBSTRING(sample_type_name, 1, 1), true, 10);

    END IF;    

   
END
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION insert_panel(panel_name TEXT) RETURNS VOID AS
$$
DECLARE
    panel_id int;
BEGIN

    SELECT id INTO panel_id FROM clinlims.panel WHERE name = panel_name;
    IF NOT FOUND THEN

        INSERT INTO clinlims.panel (id, name, description, lastupdated, sort_order, is_active)
        VALUES (nextval('clinlims.panel_seq'), panel_name, panel_name, localtimestamp, 10, 'Y');

    END IF;    

   
END
$$
LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION create_relationship_panel_sampletype(panel_name TEXT, sample_type_name TEXT) RETURNS VOID AS
$$
DECLARE
    relationship_id int;
    panel_id_value int;
    sample_type_id_value int;
BEGIN

    BEGIN
        SELECT id INTO STRICT panel_id_value FROM clinlims.panel WHERE name = panel_name;
        EXCEPTION   
                    WHEN NO_DATA_FOUND THEN
                    RAISE EXCEPTION 'panel % not found', panel_name;
                    WHEN TOO_MANY_ROWS THEN
                    RAISE EXCEPTION 'panel % not unique', panel_name;  
    END;

    BEGIN
        SELECT id INTO STRICT sample_type_id_value FROM clinlims.type_of_sample WHERE description = sample_type_name;
        EXCEPTION   
                    WHEN NO_DATA_FOUND THEN
                    RAISE EXCEPTION 'sample type % not found', sample_type_name;
                    WHEN TOO_MANY_ROWS THEN
                    RAISE EXCEPTION 'sample type % not unique', sample_type_name;  
    END;

    SELECT id INTO relationship_id FROM clinlims.sampletype_panel WHERE panel_id = panel_id_value and sample_type_id = sample_type_id_value;
    IF NOT FOUND THEN

        INSERT INTO clinlims.sampletype_panel (id, sample_type_id, panel_id)
        VALUES (nextval('clinlims.sample_type_panel_seq'), sample_type_id_value, panel_id_value);

    END IF;    

   
END
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION create_relationship_panel_test(panel_name TEXT, test_name TEXT) RETURNS VOID AS
$$
DECLARE
    relationship_id int;
    panel_id_value int;
    test_id_value int;
BEGIN

    BEGIN
        SELECT id INTO STRICT panel_id_value FROM clinlims.panel WHERE name = panel_name;
        EXCEPTION   
                    WHEN NO_DATA_FOUND THEN
                    RAISE EXCEPTION 'panel % not found', panel_name;
                    WHEN TOO_MANY_ROWS THEN
                    RAISE EXCEPTION 'panel % not unique', panel_name;  
    END;

    BEGIN
        SELECT id INTO STRICT test_id_value FROM clinlims.test WHERE name = test_name or description = test_name;
        EXCEPTION   
                    WHEN NO_DATA_FOUND THEN
                    RAISE EXCEPTION 'test % not found', test_name;
                    WHEN TOO_MANY_ROWS THEN
                    RAISE EXCEPTION 'test % not unique', test_name;  
    END;

    SELECT id INTO relationship_id FROM clinlims.panel_item WHERE panel_id = panel_id_value and test_id = test_id_value;
    IF NOT FOUND THEN

        INSERT INTO clinlims.panel_item (id, panel_id, sort_order, test_local_abbrev, lastupdated, test_id)
        VALUES (nextval('clinlims.panel_item_seq'), panel_id_value, 1, test_name, localtimestamp, test_id_value);

    END IF;    

   
END
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION create_relationship_sample_test(sample_type_name TEXT, test_name TEXT) RETURNS VOID AS
$$
DECLARE
    relationship_id int;
    sample_type_id_value int;
    test_id_value int;
BEGIN

    BEGIN
        SELECT id INTO STRICT sample_type_id_value FROM clinlims.type_of_sample WHERE description = sample_type_name;
        EXCEPTION   
                    WHEN NO_DATA_FOUND THEN
                    RAISE EXCEPTION 'sample type % not found', sample_type_name;
                    WHEN TOO_MANY_ROWS THEN
                    RAISE EXCEPTION 'sample type % not unique', sample_type_name;  
    END;

    BEGIN
        SELECT id INTO STRICT test_id_value FROM clinlims.test WHERE name = test_name or description = test_name;
        EXCEPTION   
                    WHEN NO_DATA_FOUND THEN
                    RAISE EXCEPTION 'test % not found', test_name;
                    WHEN TOO_MANY_ROWS THEN
                    RAISE EXCEPTION 'test % not unique', test_name;  
    END;

    SELECT id INTO relationship_id FROM clinlims.sampletype_test WHERE sample_type_id = sample_type_id_value and test_id = test_id_value;
    IF NOT FOUND THEN

        INSERT INTO clinlims.sampletype_test (id, sample_type_id, test_id, is_panel)
        VALUES (nextval('clinlims.sample_type_test_seq'), sample_type_id_value, test_id_value, false);

    END IF;    

   
END
$$
LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION insert_unit_of_measure(unit_of_measure_name TEXT, test_name TEXT) RETURNS VOID AS
$$
DECLARE
    unit_id int;
    test_id_value int;
BEGIN
    
    BEGIN
        SELECT id INTO STRICT test_id_value FROM clinlims.test WHERE name = test_name or description = test_name;
        EXCEPTION   
                    WHEN NO_DATA_FOUND THEN
                    RAISE EXCEPTION 'test % not found', test_name;
                    WHEN TOO_MANY_ROWS THEN
                    RAISE EXCEPTION 'test % not unique', test_name;  
    END;  

    SELECT id INTO unit_id FROM clinlims.unit_of_measure WHERE name = unit_of_measure_name;
    IF NOT FOUND THEN

        INSERT INTO clinlims.unit_of_measure (id, name, description, lastupdated)
        VALUES (nextval('clinlims.unit_of_measure_seq'), unit_of_measure_name, unit_of_measure_name, localtimestamp);

        UPDATE clinlims.test SET uom_id = currval('clinlims.unit_of_measure_seq'), lastupdated = localtimestamp 
        WHERE id = test_id_value;

    ELSE

        UPDATE clinlims.test SET uom_id = unit_id, lastupdated = localtimestamp 
        WHERE id = test_id_value;    

    END IF;    

   
END
$$
LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION insert_result_limit_normal_range(test_name TEXT, lower_limit double precision, upper_limit double precision) RETURNS VOID AS
$$
DECLARE
    result_limits_id int;
    test_id_value int;
BEGIN

    IF lower_limit = -1 AND upper_limit = -1 THEN
        RETURN ;
    END IF;

    BEGIN
        SELECT id INTO STRICT test_id_value FROM clinlims.test WHERE name = test_name or description = test_name;
        EXCEPTION   
                    WHEN NO_DATA_FOUND THEN
                    RAISE EXCEPTION 'test % not found', test_name;
                    WHEN TOO_MANY_ROWS THEN
                    RAISE EXCEPTION 'test % not unique', test_name;  
    END;  

    SELECT id INTO result_limits_id FROM clinlims.result_limits WHERE test_id = test_id_value;
    IF NOT FOUND THEN

        INSERT INTO clinlims.result_limits (id, test_id, test_result_type_id, low_normal, high_normal, lastupdated)
        VALUES (nextval('clinlims.result_limits_seq'), test_id_value, 4, lower_limit, upper_limit, localtimestamp);

    ELSE

        UPDATE clinlims.result_limits SET low_normal = lower_limit, high_normal = upper_limit, lastupdated = localtimestamp 
        WHERE id = result_limits_id;

    END IF;    

   
END
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION insert_result_limit_valid_range(test_name TEXT, lower_limit double precision, upper_limit double precision) RETURNS VOID AS
$$
DECLARE
    result_limits_id int;
    test_id_value int;
BEGIN

    IF lower_limit = -1 AND upper_limit = -1 THEN
        RETURN ;
    END IF;

    BEGIN
        SELECT id INTO STRICT test_id_value FROM clinlims.test WHERE name = test_name or description = test_name;
        EXCEPTION   
                    WHEN NO_DATA_FOUND THEN
                    RAISE EXCEPTION 'test % not found', test_name;
                    WHEN TOO_MANY_ROWS THEN
                    RAISE EXCEPTION 'test % not unique', test_name;  
    END;  

    SELECT id INTO result_limits_id FROM clinlims.result_limits WHERE test_id = test_id_value;
    IF NOT FOUND THEN

        INSERT INTO clinlims.result_limits (id, test_id, test_result_type_id, low_valid, high_valid, lastupdated)
        VALUES (nextval('clinlims.result_limits_seq'), test_id_value, 4, lower_limit, upper_limit, localtimestamp);

    ELSE

        UPDATE clinlims.result_limits SET low_valid = lower_limit, high_valid = upper_limit, lastupdated = localtimestamp 
        WHERE id = result_limits_id;

    END IF;    

   
END
$$
LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION create_relationship_test_section_test(test_section_name TEXT, test_name TEXT) RETURNS VOID AS
$$
DECLARE
    test_section_id_value int;
    test_id_value INT;
BEGIN


    BEGIN
        SELECT id INTO STRICT test_section_id_value FROM clinlims.test_section WHERE name = test_section_name or description = test_section_name;
        EXCEPTION   
                    WHEN NO_DATA_FOUND THEN
                    RAISE EXCEPTION 'test section % not found', test_section_name;
                    WHEN TOO_MANY_ROWS THEN
                    RAISE EXCEPTION 'test section % not unique', test_section_name;  
    END; 

    BEGIN
        SELECT id INTO STRICT test_id_value FROM clinlims.test WHERE name = test_name or description = test_name;
        EXCEPTION   
                    WHEN NO_DATA_FOUND THEN
                    RAISE EXCEPTION 'test % not found', test_name;
                    WHEN TOO_MANY_ROWS THEN
                    RAISE EXCEPTION 'test % not unique', test_name;  
    END;   

    UPDATE clinlims.test SET test_section_id = test_section_id_value, lastupdated = localtimestamp WHERE id = test_id_value;

   
END
$$
LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION add_test_result_type(test_name TEXT, test_result_type TEXT) RETURNS VOID AS
$$
DECLARE
    test_id_value INT;
    test_result_id_value INT;
BEGIN

    BEGIN
        SELECT id INTO STRICT test_id_value FROM clinlims.test WHERE name = test_name or description = test_name;
        EXCEPTION
                    WHEN NO_DATA_FOUND THEN
                    RAISE EXCEPTION 'test % not found', test_name;
                    WHEN TOO_MANY_ROWS THEN
                    RAISE EXCEPTION 'test % not unique', test_name;
    END;

    BEGIN
        SELECT id INTO test_result_id_value FROM clinlims.test_result WHERE test_id = test_id_value;
        IF NOT FOUND THEN
            INSERT INTO clinlims.test_result(id, test_id, tst_rslt_type)
            VALUES (nextval('clinlims.test_result_seq'), test_id_value, test_result_type);
        END IF;
    END;
END
$$
LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION add_test_result_type(test_name TEXT, test_result_type TEXT, test_result_value TEXT) RETURNS VOID AS
$$
DECLARE
    test_id_value INT;
    test_result_id_value INT;
    dictionary_id_value INT;
    dictionary_categ_id_value INT;
BEGIN

    BEGIN
        SELECT id INTO STRICT test_id_value FROM clinlims.test WHERE name = test_name or description = test_name;
        EXCEPTION
                    WHEN NO_DATA_FOUND THEN
                    RAISE EXCEPTION 'test % not found', test_name;
                    WHEN TOO_MANY_ROWS THEN
                    RAISE EXCEPTION 'test % not unique', test_name;
    END;

    BEGIN
        SELECT id INTO STRICT dictionary_categ_id_value FROM clinlims.dictionary_category WHERE name = 'Bahmni Lab';
        EXCEPTION
                    WHEN NO_DATA_FOUND THEN
                    RAISE EXCEPTION 'JSS Lab dictionary category';
                    WHEN TOO_MANY_ROWS THEN
                    RAISE EXCEPTION 'JSS Lab dictionary category is not unique';
    END;

    BEGIN
        SELECT id INTO dictionary_id_value FROM clinlims.dictionary WHERE dict_entry = test_result_value and dictionary_category_id = dictionary_categ_id_value;
        IF NOT FOUND THEN
            INSERT INTO clinlims.dictionary(id, dict_entry, dictionary_category_id, lastupdated)
            VALUES (nextval('dictionary_seq'), test_result_value, dictionary_categ_id_value, localtimestamp);
        END IF;
    END;

    BEGIN
        SELECT id INTO STRICT dictionary_id_value FROM clinlims.dictionary WHERE dict_entry = test_result_value and dictionary_category_id = dictionary_categ_id_value;
        SELECT id INTO test_result_id_value FROM clinlims.test_result WHERE test_id = test_id_value and tst_rslt_type = test_result_type and value = cast(dictionary_id_value as text);
        IF NOT FOUND THEN
            INSERT INTO clinlims.test_result(id, test_id, tst_rslt_type, value)
            VALUES (nextval('clinlims.test_result_seq'), test_id_value, test_result_type, dictionary_id_value);
        END IF;
    END;
END
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION clinlims.insert_sample_source(sample_source_name text, sample_source_description text)
  RETURNS void
AS
  $BODY$
  DECLARE
    sample_source_id int;
    max_display_order int;
BEGIN

    select max(display_order) into max_display_order from clinlims.sample_source;
    SELECT id INTO sample_source_id FROM clinlims.sample_source WHERE name = sample_source_name;
    IF NOT FOUND THEN
        
      INSERT INTO clinlims.sample_source (id, name, description, active, display_order)
        VALUES (nextval('clinlims.sample_source_id_seq'), sample_source_name, sample_source_description, true, max_display_order);

    END IF;    

   
END
$BODY$
LANGUAGE plpgsql;



CREATE OR REPLACE FUNCTION clinlims.insert_provider(provider_first_name text, provider_last_name text)
  RETURNS void
AS
  $BODY$
  DECLARE
    existing_person_id int;
    existing_provider_id int;
BEGIN

    SELECT id
    INTO existing_person_id
    FROM clinlims.person
    WHERE first_name = provider_first_name and last_name = provider_last_name;
    IF FOUND
    THEN
      SELECT id
      into existing_provider_id
      from clinlims.provider
      where person_id = existing_person_id;
      IF NOT FOUND
      THEN
        insert into clinlims.provider (id, person_id, lastupdated)
        VALUES (nextval('clinlims.provider_seq'), existing_person_id, now());
      END IF;
    END IF;    

   
END
$BODY$
LANGUAGE plpgsql;
