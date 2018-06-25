CREATE OR REPLACE FUNCTION get_test_id_from_test_name(test_name VARCHAR(255))
  RETURNS NUMERIC
AS'
  DECLARE
    test_id NUMERIC;
BEGIN
  SELECT id
  INTO test_id
  FROM test
  WHERE lower(name) like lower(test_name);
  RETURN test_id;
END'
LANGUAGE plpgsql;