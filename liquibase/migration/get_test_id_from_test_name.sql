CREATE FUNCTION get_test_id_from_test_name (IN test_name VARCHAR(255))
  RETURNS NUMERIC AS $test_id$
  DECLARE test_id NUMERIC;
  BEGIN
    SELECT id INTO test_id FROM test WHERE name = test_name;
    RETURN test_id;
  END;
$test_id$ LANGUAGE plpgsql;