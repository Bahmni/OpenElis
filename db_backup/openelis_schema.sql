--
-- Note: This file has been generated from bahmni-base.dump using pg_restore command. 
-- This contains the schema creation commands for OpenElis Database.
--

--
-- PostgreSQL database dump
--

-- Dumped from database version 9.2.4
-- Dumped by pg_dump version 9.2.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: clinlims; Type: SCHEMA; Schema: -; Owner: clinlims
--

CREATE SCHEMA clinlims;


ALTER SCHEMA clinlims OWNER TO clinlims;

--
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO postgres;

--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

--
-- Name: breakpoint; Type: TYPE; Schema: public; Owner: clinlims
--

CREATE TYPE breakpoint AS (
	func oid,
	linenumber integer,
	targetname text
);


ALTER TYPE public.breakpoint OWNER TO clinlims;

--
-- Name: dblink_pkey_results; Type: TYPE; Schema: public; Owner: clinlims
--

CREATE TYPE dblink_pkey_results AS (
	"position" integer,
	colname text
);


ALTER TYPE public.dblink_pkey_results OWNER TO clinlims;

--
-- Name: frame; Type: TYPE; Schema: public; Owner: clinlims
--

CREATE TYPE frame AS (
	level integer,
	targetname text,
	func oid,
	linenumber integer,
	args text
);


ALTER TYPE public.frame OWNER TO clinlims;

--
-- Name: proxyinfo; Type: TYPE; Schema: public; Owner: clinlims
--

CREATE TYPE proxyinfo AS (
	serverversionstr text,
	serverversionnum integer,
	proxyapiver integer,
	serverprocessid integer
);


ALTER TYPE public.proxyinfo OWNER TO clinlims;

--
-- Name: tablefunc_crosstab_2; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE tablefunc_crosstab_2 AS (
	row_name text,
	category_1 text,
	category_2 text
);


ALTER TYPE public.tablefunc_crosstab_2 OWNER TO postgres;

--
-- Name: tablefunc_crosstab_3; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE tablefunc_crosstab_3 AS (
	row_name text,
	category_1 text,
	category_2 text,
	category_3 text
);


ALTER TYPE public.tablefunc_crosstab_3 OWNER TO postgres;

--
-- Name: tablefunc_crosstab_4; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE tablefunc_crosstab_4 AS (
	row_name text,
	category_1 text,
	category_2 text,
	category_3 text,
	category_4 text
);


ALTER TYPE public.tablefunc_crosstab_4 OWNER TO postgres;

--
-- Name: targetinfo; Type: TYPE; Schema: public; Owner: clinlims
--

CREATE TYPE targetinfo AS (
	target oid,
	schema oid,
	nargs integer,
	argtypes oidvector,
	targetname name,
	argmodes "char"[],
	argnames text[],
	targetlang oid,
	fqname text,
	returnsset boolean,
	returntype oid
);


ALTER TYPE public.targetinfo OWNER TO clinlims;

--
-- Name: var; Type: TYPE; Schema: public; Owner: clinlims
--

CREATE TYPE var AS (
	name text,
	varclass character(1),
	linenumber integer,
	isunique boolean,
	isconst boolean,
	isnotnull boolean,
	dtype oid,
	value text
);


ALTER TYPE public.var OWNER TO clinlims;

--
-- Name: xpath_list(text, text); Type: FUNCTION; Schema: public; Owner: clinlims
--

CREATE FUNCTION xpath_list(text, text) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT xpath_list($1,$2,',')$_$;


ALTER FUNCTION public.xpath_list(text, text) OWNER TO clinlims;

--
-- Name: xpath_nodeset(text, text); Type: FUNCTION; Schema: public; Owner: clinlims
--

CREATE FUNCTION xpath_nodeset(text, text) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT xpath_nodeset($1,$2,'','')$_$;


ALTER FUNCTION public.xpath_nodeset(text, text) OWNER TO clinlims;

--
-- Name: xpath_nodeset(text, text, text); Type: FUNCTION; Schema: public; Owner: clinlims
--

CREATE FUNCTION xpath_nodeset(text, text, text) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT xpath_nodeset($1,$2,'',$3)$_$;


ALTER FUNCTION public.xpath_nodeset(text, text, text) OWNER TO clinlims;

SET search_path = clinlims, pg_catalog;

SET default_tablespace = '';

--
-- Name: action; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE action (
    id numeric(10,0) NOT NULL,
    code character varying(10) NOT NULL,
    description character varying(256) NOT NULL,
    type character varying(10) NOT NULL,
    lastupdated timestamp(6) without time zone
);


ALTER TABLE clinlims.action OWNER TO clinlims;

--
-- Name: action_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE action_seq
    START WITH 45
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;


ALTER TABLE clinlims.action_seq OWNER TO clinlims;

--
-- Name: address_part; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE address_part (
    id numeric(10,0) NOT NULL,
    part_name character varying(20) NOT NULL,
    display_order numeric(4,0),
    display_key character varying(20)
);


ALTER TABLE clinlims.address_part OWNER TO clinlims;

--
-- Name: TABLE address_part; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE address_part IS 'Holds the different parts of an address';


--
-- Name: COLUMN address_part.id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN address_part.id IS 'Unique id genereated from address_part seq';


--
-- Name: COLUMN address_part.part_name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN address_part.part_name IS 'What part of the address is this, street, commune state etc.';


--
-- Name: COLUMN address_part.display_order; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN address_part.display_order IS 'The order in which they are listed in the standardard address format';


--
-- Name: COLUMN address_part.display_key; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN address_part.display_key IS 'The display key for localization';


--
-- Name: address_part_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE address_part_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.address_part_seq OWNER TO clinlims;

--
-- Name: analysis; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE analysis (
    id numeric(10,0) NOT NULL,
    sampitem_id numeric(10,0),
    test_sect_id numeric(10,0),
    test_id numeric(10,0),
    revision numeric,
    status character varying(1),
    started_date timestamp without time zone,
    completed_date timestamp without time zone,
    released_date timestamp without time zone,
    printed_date timestamp without time zone,
    is_reportable character varying(1),
    so_send_ready_date timestamp without time zone,
    so_client_reference character varying(240),
    so_notify_received_date timestamp without time zone,
    so_notify_send_date timestamp without time zone,
    so_send_date timestamp without time zone,
    so_send_entry_by character varying(240),
    so_send_entry_date timestamp without time zone,
    analysis_type character varying(10) NOT NULL,
    lastupdated timestamp(6) without time zone,
    parent_analysis_id numeric(10,0),
    parent_result_id numeric(10,0),
    reflex_trigger boolean DEFAULT false,
    status_id numeric(10,0),
    entry_date timestamp with time zone,
    panel_id numeric(10,0)
);


ALTER TABLE clinlims.analysis OWNER TO clinlims;

--
-- Name: COLUMN analysis.id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analysis.id IS 'Sequential number';


--
-- Name: COLUMN analysis.sampitem_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analysis.sampitem_id IS 'Sample source write in if not already defined';


--
-- Name: COLUMN analysis.test_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analysis.test_id IS 'Sequential value assigned on insert';


--
-- Name: COLUMN analysis.revision; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analysis.revision IS 'revision number';


--
-- Name: COLUMN analysis.status; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analysis.status IS 'Analysis Status; logged in, initiated, completed, released';


--
-- Name: COLUMN analysis.started_date; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analysis.started_date IS 'Date and time analysis started';


--
-- Name: COLUMN analysis.completed_date; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analysis.completed_date IS 'Date and time analysis completed';


--
-- Name: COLUMN analysis.released_date; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analysis.released_date IS 'Date and time analysis was released; basically verified and ready to report';


--
-- Name: COLUMN analysis.printed_date; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analysis.printed_date IS 'Date and time analysis was last printed for sending out';


--
-- Name: COLUMN analysis.is_reportable; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analysis.is_reportable IS 'Indicates if this analysis should be reported';


--
-- Name: COLUMN analysis.so_send_ready_date; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analysis.so_send_ready_date IS 'Send out ready date';


--
-- Name: COLUMN analysis.so_notify_received_date; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analysis.so_notify_received_date IS 'Date that send out facility notificed MDH that they had received the specimen';


--
-- Name: COLUMN analysis.so_notify_send_date; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analysis.so_notify_send_date IS 'Date that MDH sent out the specimen to a sendout facility';


--
-- Name: COLUMN analysis.so_send_entry_by; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analysis.so_send_entry_by IS 'User name who entered sendout';


--
-- Name: COLUMN analysis.reflex_trigger; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analysis.reflex_trigger IS 'True if this analysis has triggered a reflex test';


--
-- Name: COLUMN analysis.status_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analysis.status_id IS 'foriegn key to status of analysis ';


--
-- Name: COLUMN analysis.entry_date; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analysis.entry_date IS 'Date on which the results for this analysis was first entered';


--
-- Name: COLUMN analysis.panel_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analysis.panel_id IS 'If this analysis is part of a panel then this is the id';


--
-- Name: analysis_qaevent; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE analysis_qaevent (
    id numeric(10,0) NOT NULL,
    qa_event_id numeric(10,0),
    analysis_id numeric(10,0),
    lastupdated timestamp(6) without time zone,
    completed_date timestamp without time zone
);


ALTER TABLE clinlims.analysis_qaevent OWNER TO clinlims;

--
-- Name: analysis_qaevent_action; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE analysis_qaevent_action (
    id numeric(10,0) NOT NULL,
    analysis_qaevent_id numeric(10,0) NOT NULL,
    action_id numeric(10,0) NOT NULL,
    created_date timestamp without time zone NOT NULL,
    lastupdated timestamp(6) without time zone,
    sys_user_id numeric(10,0)
);


ALTER TABLE clinlims.analysis_qaevent_action OWNER TO clinlims;

--
-- Name: analysis_qaevent_action_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE analysis_qaevent_action_seq
    START WITH 221
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;


ALTER TABLE clinlims.analysis_qaevent_action_seq OWNER TO clinlims;

--
-- Name: analysis_qaevent_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE analysis_qaevent_seq
    START WITH 326
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;


ALTER TABLE clinlims.analysis_qaevent_seq OWNER TO clinlims;

--
-- Name: analysis_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE analysis_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.analysis_seq OWNER TO clinlims;

--
-- Name: analysis_storages; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE analysis_storages (
    id numeric(10,0) NOT NULL,
    storage_id numeric(10,0),
    checkin timestamp without time zone,
    checkout timestamp without time zone,
    analysis_id numeric(10,0)
);


ALTER TABLE clinlims.analysis_storages OWNER TO clinlims;

--
-- Name: COLUMN analysis_storages.checkin; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analysis_storages.checkin IS 'Date and time sample was moved to this storage_location';


--
-- Name: COLUMN analysis_storages.checkout; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analysis_storages.checkout IS 'Date and time sample was removed from this storage location';


--
-- Name: analysis_users; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE analysis_users (
    id numeric(10,0) NOT NULL,
    action character varying(1),
    analysis_id numeric(10,0),
    system_user_id numeric(10,0)
);


ALTER TABLE clinlims.analysis_users OWNER TO clinlims;

--
-- Name: COLUMN analysis_users.action; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analysis_users.action IS 'Type of action performed such as test request, complete, release';


--
-- Name: analyte; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE analyte (
    id numeric(10,0) NOT NULL,
    analyte_id numeric(10,0),
    name character varying(60),
    is_active character varying(1),
    external_id character varying(20),
    lastupdated timestamp(6) without time zone,
    local_abbrev character varying(10)
);


ALTER TABLE clinlims.analyte OWNER TO clinlims;

--
-- Name: COLUMN analyte.name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analyte.name IS 'Name of analyte';


--
-- Name: COLUMN analyte.is_active; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analyte.is_active IS 'Flag indicating if the test is active';


--
-- Name: COLUMN analyte.external_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analyte.external_id IS 'External ID such as CAS #';


--
-- Name: analyte_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE analyte_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.analyte_seq OWNER TO clinlims;

--
-- Name: analyzer; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE analyzer (
    id numeric(10,0) NOT NULL,
    scrip_id numeric(10,0),
    name character varying(20),
    machine_id character varying(20),
    description character varying(60),
    analyzer_type character varying(30),
    is_active boolean,
    location character varying(60),
    lastupdated timestamp(6) without time zone
);


ALTER TABLE clinlims.analyzer OWNER TO clinlims;

--
-- Name: COLUMN analyzer.name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analyzer.name IS 'Short name for analyzer';


--
-- Name: COLUMN analyzer.machine_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analyzer.machine_id IS 'id which uniquely matches a machine, descriminates between duplicate analyzers';


--
-- Name: COLUMN analyzer.description; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analyzer.description IS 'analyzer description';


--
-- Name: COLUMN analyzer.analyzer_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analyzer.analyzer_type IS 'Type of analyzer: Mass Spec, HPLC, etc.';


--
-- Name: COLUMN analyzer.is_active; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analyzer.is_active IS 'Flag indicating if the analyzer is active';


--
-- Name: COLUMN analyzer.location; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analyzer.location IS 'Location of analyzer';


--
-- Name: analyzer_result_status; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE analyzer_result_status (
    id numeric(10,0) NOT NULL,
    name character varying(30) NOT NULL,
    description character varying(60)
);


ALTER TABLE clinlims.analyzer_result_status OWNER TO clinlims;

--
-- Name: analyzer_results; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE analyzer_results (
    id numeric(10,0) NOT NULL,
    analyzer_id numeric(10,0) NOT NULL,
    accession_number character varying(20) NOT NULL,
    test_name character varying(20) NOT NULL,
    result character varying(20) NOT NULL,
    units character varying(10),
    status_id numeric(10,0) DEFAULT 1 NOT NULL,
    iscontrol boolean DEFAULT false NOT NULL,
    lastupdated timestamp(6) without time zone,
    read_only boolean DEFAULT false,
    test_id numeric(10,0),
    duplicate_id numeric(10,0),
    positive boolean DEFAULT false,
    complete_date timestamp with time zone,
    test_result_type character varying(1)
);


ALTER TABLE clinlims.analyzer_results OWNER TO clinlims;

--
-- Name: TABLE analyzer_results; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE analyzer_results IS 'A holding table for analyzer results ';


--
-- Name: COLUMN analyzer_results.analyzer_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analyzer_results.analyzer_id IS 'Reference to analyzer table';


--
-- Name: COLUMN analyzer_results.accession_number; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analyzer_results.accession_number IS 'The display version of the accession number.  May be either the extended or normal accession number';


--
-- Name: COLUMN analyzer_results.test_name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analyzer_results.test_name IS 'The test name, if a mapping is found then the mapping will be used, if not then the analyzer test name will be useds';


--
-- Name: COLUMN analyzer_results.result; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analyzer_results.result IS 'The result of the test, the meaning depends on the test itself';


--
-- Name: COLUMN analyzer_results.units; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analyzer_results.units IS 'The units as sent from the analyzer';


--
-- Name: COLUMN analyzer_results.status_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analyzer_results.status_id IS 'The status for the this analyzer result';


--
-- Name: COLUMN analyzer_results.iscontrol; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analyzer_results.iscontrol IS 'Is this result a control';


--
-- Name: COLUMN analyzer_results.read_only; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analyzer_results.read_only IS 'Is this result read only';


--
-- Name: COLUMN analyzer_results.test_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analyzer_results.test_id IS 'Test this is result is mapped to';


--
-- Name: COLUMN analyzer_results.duplicate_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analyzer_results.duplicate_id IS 'Reference to another analyzer result with the same analyzer and analyzer test';


--
-- Name: COLUMN analyzer_results.positive; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analyzer_results.positive IS 'Is the test positive';


--
-- Name: COLUMN analyzer_results.complete_date; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analyzer_results.complete_date IS 'The time stamp for when the analyzsis was done';


--
-- Name: analyzer_results_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE analyzer_results_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.analyzer_results_seq OWNER TO clinlims;

--
-- Name: analyzer_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE analyzer_seq
    START WITH 0
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.analyzer_seq OWNER TO clinlims;

--
-- Name: analyzer_test_map; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE analyzer_test_map (
    analyzer_id numeric(10,0) NOT NULL,
    analyzer_test_name character varying(30) NOT NULL,
    test_id numeric(10,0) NOT NULL,
    lastupdated timestamp with time zone DEFAULT '2012-04-24 00:30:14.130688+00'::timestamp with time zone
);


ALTER TABLE clinlims.analyzer_test_map OWNER TO clinlims;

--
-- Name: TABLE analyzer_test_map; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE analyzer_test_map IS 'Maps the analyzers names to the tests in database';


--
-- Name: COLUMN analyzer_test_map.analyzer_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analyzer_test_map.analyzer_id IS 'foriegn key to analyzer table';


--
-- Name: COLUMN analyzer_test_map.analyzer_test_name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analyzer_test_map.analyzer_test_name IS 'The name the analyzer uses for the test';


--
-- Name: COLUMN analyzer_test_map.test_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN analyzer_test_map.test_id IS 'foriegn key to test table';


--
-- Name: animal_common_name; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE animal_common_name (
    id numeric(10,0) NOT NULL,
    name character varying(30)
);


ALTER TABLE clinlims.animal_common_name OWNER TO clinlims;

--
-- Name: COLUMN animal_common_name.name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN animal_common_name.name IS 'Lists the animal common name';


--
-- Name: animal_scientific_name; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE animal_scientific_name (
    id numeric(10,0) NOT NULL,
    comm_anim_id numeric(10,0) NOT NULL,
    name character varying(30)
);


ALTER TABLE clinlims.animal_scientific_name OWNER TO clinlims;

--
-- Name: COLUMN animal_scientific_name.id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN animal_scientific_name.id IS 'Sequential Number';


--
-- Name: COLUMN animal_scientific_name.name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN animal_scientific_name.name IS 'May include Genus and Species';


--
-- Name: attachment; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE attachment (
    id numeric(10,0) NOT NULL,
    attach_type character varying(20),
    filename character varying(60),
    description character varying(80),
    storage_reference character varying(255)
);


ALTER TABLE clinlims.attachment OWNER TO clinlims;

--
-- Name: attachment_item; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE attachment_item (
    id numeric(10,0) NOT NULL,
    reference_id numeric,
    reference_table_id numeric,
    attachment_id numeric
);


ALTER TABLE clinlims.attachment_item OWNER TO clinlims;

--
-- Name: aux_data; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE aux_data (
    id numeric(10,0) NOT NULL,
    sort_order numeric,
    is_reportable character varying(1),
    auxdata_type character varying(1),
    value character varying(80),
    reference_id numeric,
    reference_table numeric,
    aux_field_id numeric(10,0)
);


ALTER TABLE clinlims.aux_data OWNER TO clinlims;

--
-- Name: COLUMN aux_data.sort_order; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN aux_data.sort_order IS 'The order the analytes (questions) are displayed';


--
-- Name: COLUMN aux_data.is_reportable; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN aux_data.is_reportable IS 'Flag indicating if this entry is reportable';


--
-- Name: COLUMN aux_data.auxdata_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN aux_data.auxdata_type IS 'Type of value: Dictionary, Titer range, Number, Date, String';


--
-- Name: COLUMN aux_data.value; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN aux_data.value IS 'Actual value';


--
-- Name: COLUMN aux_data.reference_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN aux_data.reference_id IS 'Link to record in table to which this entry pertains';


--
-- Name: COLUMN aux_data.reference_table; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN aux_data.reference_table IS 'Link to table that this entry belongs to';


--
-- Name: aux_field; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE aux_field (
    id numeric(10,0) NOT NULL,
    sort_order numeric,
    auxfld_type character varying(1),
    is_active character varying(1),
    is_reportable character varying(1),
    reference_table numeric,
    analyte_id numeric(10,0),
    scriptlet_id numeric(10,0)
);


ALTER TABLE clinlims.aux_field OWNER TO clinlims;

--
-- Name: COLUMN aux_field.id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN aux_field.id IS 'Sequential Identifier';


--
-- Name: COLUMN aux_field.sort_order; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN aux_field.sort_order IS 'The order the analystes (questions) are displayed';


--
-- Name: COLUMN aux_field.auxfld_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN aux_field.auxfld_type IS 'Type of field: Required...';


--
-- Name: COLUMN aux_field.is_active; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN aux_field.is_active IS 'Flag indicating is this entry is active';


--
-- Name: COLUMN aux_field.is_reportable; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN aux_field.is_reportable IS 'Default value to reportable flag';


--
-- Name: COLUMN aux_field.reference_table; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN aux_field.reference_table IS 'Link to table in which this entity can be used.';


--
-- Name: aux_field_values; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE aux_field_values (
    id numeric(10,0) NOT NULL,
    auxfldval_type character varying(1),
    value character varying(80),
    aux_field_id numeric(10,0)
);


ALTER TABLE clinlims.aux_field_values OWNER TO clinlims;

--
-- Name: COLUMN aux_field_values.id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN aux_field_values.id IS 'Sequential Identifier';


--
-- Name: COLUMN aux_field_values.auxfldval_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN aux_field_values.auxfldval_type IS 'Type of value: Dictionary, titer range, number, date, string';


--
-- Name: COLUMN aux_field_values.value; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN aux_field_values.value IS 'A permissible field value';


--
-- Name: city_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE city_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.city_seq OWNER TO clinlims;

--
-- Name: city_state_zip; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE city_state_zip (
    id numeric(10,0),
    city character varying(30),
    state character varying(2),
    zip_code character varying(10),
    county_fips numeric(3,0),
    county character varying(25),
    region_id numeric(3,0),
    region character varying(30),
    state_fips numeric(3,0),
    state_name character varying(30),
    lastupdated timestamp(6) without time zone
);


ALTER TABLE clinlims.city_state_zip OWNER TO clinlims;

--
-- Name: code_element_type; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE code_element_type (
    id numeric(10,0) NOT NULL,
    text character varying(60),
    lastupdated timestamp(6) without time zone,
    local_reference_table numeric(10,0)
);


ALTER TABLE clinlims.code_element_type OWNER TO clinlims;

--
-- Name: code_element_type_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE code_element_type_seq
    START WITH 21
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;


ALTER TABLE clinlims.code_element_type_seq OWNER TO clinlims;

--
-- Name: code_element_xref; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE code_element_xref (
    id numeric(10,0) NOT NULL,
    message_org_id numeric(10,0),
    code_element_type_id numeric(10,0),
    receiver_code_element_id numeric(10,0),
    local_code_element_id numeric(10,0),
    lastupdated timestamp(6) without time zone
);


ALTER TABLE clinlims.code_element_xref OWNER TO clinlims;

--
-- Name: code_element_xref_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE code_element_xref_seq
    START WITH 41
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;


ALTER TABLE clinlims.code_element_xref_seq OWNER TO clinlims;

--
-- Name: contact_type; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE contact_type (
    id numeric(10,0) NOT NULL,
    description character varying(20),
    ct_type character varying(4),
    is_unique character varying(1)
);


ALTER TABLE clinlims.contact_type OWNER TO clinlims;

--
-- Name: COLUMN contact_type.id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN contact_type.id IS 'sequential field';


--
-- Name: COLUMN contact_type.description; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN contact_type.description IS 'Can include such things as "Operator", "Accounting"';


--
-- Name: COLUMN contact_type.ct_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN contact_type.ct_type IS 'Type code';


--
-- Name: COLUMN contact_type.is_unique; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN contact_type.is_unique IS 'Indicates if only 1 of this contact type is allowed per organization';


--
-- Name: county_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE county_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.county_seq OWNER TO clinlims;

--
-- Name: databasechangelog; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE databasechangelog (
    id character varying(63) NOT NULL,
    author character varying(63) NOT NULL,
    filename character varying(200) NOT NULL,
    dateexecuted timestamp with time zone NOT NULL,
    md5sum character varying(32),
    description character varying(255),
    comments character varying(255),
    tag character varying(255),
    liquibase character varying(10)
);


ALTER TABLE clinlims.databasechangelog OWNER TO clinlims;

--
-- Name: databasechangeloglock; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE databasechangeloglock (
    id integer NOT NULL,
    locked boolean NOT NULL,
    lockgranted timestamp with time zone,
    lockedby character varying(255)
);


ALTER TABLE clinlims.databasechangeloglock OWNER TO clinlims;

--
-- Name: dictionary; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE dictionary (
    id numeric(10,0) NOT NULL,
    is_active character varying(1),
    dict_entry character varying(4000),
    lastupdated timestamp(6) without time zone,
    local_abbrev character varying(10),
    dictionary_category_id numeric(10,0),
    display_key character varying(60)
);


ALTER TABLE clinlims.dictionary OWNER TO clinlims;

--
-- Name: COLUMN dictionary.is_active; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN dictionary.is_active IS 'Flag indicating if the analyte is active';


--
-- Name: COLUMN dictionary.dict_entry; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN dictionary.dict_entry IS 'Finding, result, interpretation';


--
-- Name: COLUMN dictionary.display_key; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN dictionary.display_key IS 'Resource file lookup key for localization of displaying the name';


--
-- Name: dictionary_category; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE dictionary_category (
    id numeric(10,0) NOT NULL,
    description character varying(60),
    lastupdated timestamp(6) without time zone,
    local_abbrev character varying(10),
    name character varying(50)
);


ALTER TABLE clinlims.dictionary_category OWNER TO clinlims;

--
-- Name: COLUMN dictionary_category.id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN dictionary_category.id IS 'A unique auto generated integer number assigned by database';


--
-- Name: COLUMN dictionary_category.description; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN dictionary_category.description IS 'Human readable description';


--
-- Name: dictionary_category_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE dictionary_category_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;


ALTER TABLE clinlims.dictionary_category_seq OWNER TO clinlims;

--
-- Name: dictionary_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE dictionary_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;


ALTER TABLE clinlims.dictionary_seq OWNER TO clinlims;

--
-- Name: district; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE district (
    id numeric(10,0) NOT NULL,
    city_id numeric(10,0) NOT NULL,
    dist_entry character varying(300),
    lastupdated timestamp without time zone,
    description character varying(50)
);


ALTER TABLE clinlims.district OWNER TO clinlims;

--
-- Name: COLUMN district.dist_entry; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN district.dist_entry IS 'Finding, result, interpretation';


--
-- Name: district_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE district_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;


ALTER TABLE clinlims.district_seq OWNER TO clinlims;

--
-- Name: document_track; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE document_track (
    id numeric(10,0) NOT NULL,
    table_id numeric(10,0) NOT NULL,
    row_id numeric(10,0) NOT NULL,
    document_type_id numeric(10,0) NOT NULL,
    parent_id numeric(10,0),
    report_generation_time timestamp with time zone,
    lastupdated timestamp with time zone,
    name character varying(80)
);


ALTER TABLE clinlims.document_track OWNER TO clinlims;

--
-- Name: TABLE document_track; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE document_track IS 'Table to track operations on documents.  Expected use is for has a document of some been printed for a sample, qa_event etc';


--
-- Name: COLUMN document_track.table_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN document_track.table_id IS 'The table to which the row_id references';


--
-- Name: COLUMN document_track.row_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN document_track.row_id IS 'The particular record for which a document has been generated';


--
-- Name: COLUMN document_track.document_type_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN document_track.document_type_id IS 'References the type of document which the record has been generated for';


--
-- Name: COLUMN document_track.parent_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN document_track.parent_id IS 'If the document has been generated more than once for this record then this will point to the previous record';


--
-- Name: COLUMN document_track.report_generation_time; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN document_track.report_generation_time IS 'When this report was generated';


--
-- Name: COLUMN document_track.lastupdated; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN document_track.lastupdated IS 'Last time this record was updated';


--
-- Name: COLUMN document_track.name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN document_track.name IS 'The name of the report if there is more than one of the type';


--
-- Name: document_track_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE document_track_seq
    START WITH 0
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.document_track_seq OWNER TO clinlims;

--
-- Name: document_type; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE document_type (
    id numeric(10,0) NOT NULL,
    name character varying(40) NOT NULL,
    description character varying(80),
    lastupdated timestamp with time zone
);


ALTER TABLE clinlims.document_type OWNER TO clinlims;

--
-- Name: TABLE document_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE document_type IS 'Table which describes document types to be tracked by document_track table';


--
-- Name: COLUMN document_type.name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN document_type.name IS 'The name of the document';


--
-- Name: COLUMN document_type.description; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN document_type.description IS 'The description of the document';


--
-- Name: COLUMN document_type.lastupdated; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN document_type.lastupdated IS 'Last time this record was updated';


--
-- Name: document_type_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE document_type_seq
    START WITH 0
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.document_type_seq OWNER TO clinlims;

--
-- Name: ethnicity; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE ethnicity (
    id numeric(10,0) NOT NULL,
    ethnic_type character varying(1) NOT NULL,
    description character varying(20),
    is_active character varying(1)
);


ALTER TABLE clinlims.ethnicity OWNER TO clinlims;

--
-- Name: COLUMN ethnicity.id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN ethnicity.id IS 'Any code values in tables.';


--
-- Name: COLUMN ethnicity.ethnic_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN ethnicity.ethnic_type IS 'Ethnicity of Patient';


--
-- Name: gender; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE gender (
    id numeric(10,0) NOT NULL,
    gender_type character varying(1),
    description character varying(20),
    lastupdated timestamp(6) without time zone,
    name_key character varying(60)
);


ALTER TABLE clinlims.gender OWNER TO clinlims;

--
-- Name: COLUMN gender.id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN gender.id IS 'A unique auto generated integer number assigned by database';


--
-- Name: COLUMN gender.gender_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN gender.gender_type IS 'Gender code (M, F, U)';


--
-- Name: COLUMN gender.description; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN gender.description IS 'Human readable description';


--
-- Name: gender_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE gender_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.gender_seq OWNER TO clinlims;

--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;


ALTER TABLE clinlims.hibernate_sequence OWNER TO clinlims;

--
-- Name: history; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE history (
    id numeric(10,0) NOT NULL,
    sys_user_id numeric(10,0) NOT NULL,
    reference_id numeric NOT NULL,
    reference_table numeric NOT NULL,
    "timestamp" timestamp without time zone NOT NULL,
    activity character varying(1) NOT NULL,
    changes bytea
);


ALTER TABLE clinlims.history OWNER TO clinlims;

--
-- Name: COLUMN history.id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN history.id IS 'Sequential number for audit records';


--
-- Name: COLUMN history.sys_user_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN history.sys_user_id IS 'Sequential Identifier';


--
-- Name: COLUMN history.reference_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN history.reference_id IS 'Links to record in table to which this entry pertains';


--
-- Name: COLUMN history.reference_table; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN history.reference_table IS 'Link to table that this entity belongs to';


--
-- Name: COLUMN history."timestamp"; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN history."timestamp" IS 'Date of history record';


--
-- Name: COLUMN history.activity; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN history.activity IS 'U for update, D for delete';


--
-- Name: COLUMN history.changes; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN history.changes IS 'XML image of record prior to change';


--
-- Name: history_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE history_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;


ALTER TABLE clinlims.history_seq OWNER TO clinlims;

--
-- Name: patient; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE patient (
    id numeric(10,0) NOT NULL,
    person_id numeric(10,0) NOT NULL,
    race character varying(5),
    gender character varying(1),
    birth_date timestamp without time zone,
    epi_first_name character varying(25),
    epi_middle_name character varying(25),
    epi_last_name character varying(240),
    birth_time timestamp without time zone,
    death_date timestamp without time zone,
    national_id character varying(240),
    ethnicity character varying(1),
    school_attend character varying(240),
    medicare_id character varying(240),
    medicaid_id character varying(240),
    birth_place character varying(35),
    lastupdated timestamp(6) without time zone,
    external_id character varying(20),
    chart_number character varying(20),
    entered_birth_date character varying(10)
);


ALTER TABLE clinlims.patient OWNER TO clinlims;

--
-- Name: COLUMN patient.race; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN patient.race IS 'A string of 1 character race code(s)';


--
-- Name: COLUMN patient.gender; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN patient.gender IS 'A one character gender code';


--
-- Name: COLUMN patient.birth_date; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN patient.birth_date IS 'Date of birth';


--
-- Name: COLUMN patient.birth_time; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN patient.birth_time IS 'Time of birth for newborn patients';


--
-- Name: COLUMN patient.death_date; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN patient.death_date IS 'Date of death if unexplained illness or death';


--
-- Name: COLUMN patient.national_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN patient.national_id IS 'National Patient ID or SSN';


--
-- Name: COLUMN patient.medicare_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN patient.medicare_id IS 'Medicare Number';


--
-- Name: COLUMN patient.medicaid_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN patient.medicaid_id IS 'Medicaid Number';


--
-- Name: COLUMN patient.entered_birth_date; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN patient.entered_birth_date IS 'Persons birthdate may not be known and it will be entered with XX for date and/or month';


--
-- Name: sample_human; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE sample_human (
    id numeric(10,0) NOT NULL,
    provider_id numeric(10,0),
    samp_id numeric(10,0) NOT NULL,
    patient_id numeric(10,0),
    lastupdated timestamp(6) without time zone
);


ALTER TABLE clinlims.sample_human OWNER TO clinlims;

--
-- Name: COLUMN sample_human.samp_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_human.samp_id IS 'MDH Specimen Number';


--
-- Name: sample_item; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE sample_item (
    id numeric(10,0) NOT NULL,
    sort_order numeric NOT NULL,
    sampitem_id numeric(10,0),
    samp_id numeric(10,0),
    source_id numeric(10,0),
    typeosamp_id numeric(10,0),
    uom_id numeric(10,0),
    source_other character varying(40),
    quantity numeric,
    lastupdated timestamp(6) without time zone,
    external_id character varying(20),
    collection_date timestamp with time zone,
    status_id numeric(10,0) NOT NULL,
    collector character varying(60)
);


ALTER TABLE clinlims.sample_item OWNER TO clinlims;

--
-- Name: COLUMN sample_item.id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_item.id IS 'Sample source write in if not already defined';


--
-- Name: COLUMN sample_item.sort_order; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_item.sort_order IS 'Sample items unique sequence number for this sample';


--
-- Name: COLUMN sample_item.sampitem_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_item.sampitem_id IS 'Sample source write in if not already defined';


--
-- Name: COLUMN sample_item.samp_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_item.samp_id IS 'MDH Specimen Number';


--
-- Name: COLUMN sample_item.source_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_item.source_id IS 'A unique auto generated integer number assigned by the database.';


--
-- Name: COLUMN sample_item.typeosamp_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_item.typeosamp_id IS 'A unique auto generated integer number assigned by the database';


--
-- Name: COLUMN sample_item.source_other; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_item.source_other IS 'Sample source write in if not already defined';


--
-- Name: COLUMN sample_item.quantity; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_item.quantity IS 'Amount of sample';


--
-- Name: COLUMN sample_item.external_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_item.external_id IS 'An external id that may have been attached to the sample item before it came to the lab';


--
-- Name: COLUMN sample_item.collection_date; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_item.collection_date IS 'The date this sample_item was collected or seperated from other part of sample';


--
-- Name: COLUMN sample_item.status_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_item.status_id IS 'The status of this sample item';


--
-- Name: COLUMN sample_item.collector; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_item.collector IS 'The name of the person who collected the sample';


--
-- Name: status_of_sample; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE status_of_sample (
    id numeric(10,0) NOT NULL,
    description character varying(240),
    code numeric(3,0) NOT NULL,
    status_type character varying(10) NOT NULL,
    lastupdated timestamp(6) without time zone,
    name character varying(30),
    display_key character varying(60),
    is_active character varying(1) DEFAULT 'Y'::character varying
);


ALTER TABLE clinlims.status_of_sample OWNER TO clinlims;

--
-- Name: COLUMN status_of_sample.is_active; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN status_of_sample.is_active IS 'Either Y or N';


--
-- Name: test; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE test (
    id numeric(10,0) NOT NULL,
    method_id numeric(10,0),
    uom_id numeric(10,0),
    description character varying(120) NOT NULL,
    loinc character varying(240),
    reporting_description character varying(60),
    sticker_req_flag character varying(1),
    is_active character varying(1),
    active_begin timestamp without time zone,
    active_end timestamp without time zone,
    is_reportable character varying(1),
    time_holding numeric,
    time_wait numeric,
    time_ta_average numeric,
    time_ta_warning numeric,
    time_ta_max numeric,
    label_qty numeric,
    lastupdated timestamp(6) without time zone,
    label_id numeric(10,0),
    test_trailer_id numeric(10,0),
    test_section_id numeric(10,0),
    scriptlet_id numeric(10,0),
    test_format_id numeric(10,0),
    local_abbrev character varying(50),
    sort_order numeric DEFAULT 2147483647,
    name character varying(60) NOT NULL,
    display_key character varying(60),
    orderable boolean DEFAULT true
);


ALTER TABLE clinlims.test OWNER TO clinlims;

--
-- Name: COLUMN test.id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test.id IS 'Sequential value assigned on insert';


--
-- Name: COLUMN test.method_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test.method_id IS 'Sequential number';


--
-- Name: COLUMN test.description; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test.description IS 'Description for test';


--
-- Name: COLUMN test.reporting_description; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test.reporting_description IS 'Description for test that appears on reports';


--
-- Name: COLUMN test.is_active; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test.is_active IS 'Active status flag';


--
-- Name: COLUMN test.active_begin; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test.active_begin IS 'Active end date';


--
-- Name: COLUMN test.active_end; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test.active_end IS 'Active begin date';


--
-- Name: COLUMN test.is_reportable; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test.is_reportable IS 'The default flag indicating if ths test is reportable';


--
-- Name: COLUMN test.time_holding; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test.time_holding IS 'Max hours between collection and received time';


--
-- Name: COLUMN test.time_wait; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test.time_wait IS 'Hours to wait before analysis can begin';


--
-- Name: COLUMN test.time_ta_average; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test.time_ta_average IS 'Average hours for test to be reported';


--
-- Name: COLUMN test.time_ta_warning; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test.time_ta_warning IS 'Hours before issuing touraround warning for test not reported';


--
-- Name: COLUMN test.time_ta_max; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test.time_ta_max IS 'Max hours test should be in laboratory';


--
-- Name: COLUMN test.label_qty; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test.label_qty IS 'Number of labels to print';


--
-- Name: COLUMN test.display_key; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test.display_key IS 'Resource file lookup key for localization of displaying the name';


--
-- Name: COLUMN test.orderable; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test.orderable IS 'Should this test show in list of tests which can be ordered.  If not it is a reflex only test';


--
-- Name: test_section; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE test_section (
    id numeric(10,0) NOT NULL,
    name character varying(20),
    description character varying(60) NOT NULL,
    org_id numeric(10,0),
    is_external character varying(1),
    lastupdated timestamp(6) without time zone,
    parent_test_section numeric(10,0),
    display_key character varying(60),
    sort_order numeric DEFAULT 2147483647,
    is_active character varying(1) DEFAULT 'Y'::character varying
);


ALTER TABLE clinlims.test_section OWNER TO clinlims;

--
-- Name: COLUMN test_section.name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test_section.name IS 'Short section name';


--
-- Name: COLUMN test_section.description; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test_section.description IS 'MDH Locations including various labs';


--
-- Name: COLUMN test_section.org_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test_section.org_id IS 'Sequential Numbering Field';


--
-- Name: COLUMN test_section.is_external; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test_section.is_external IS 'Flag indicating if section is external to organization (Y/N)';


--
-- Name: COLUMN test_section.display_key; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test_section.display_key IS 'Resource file lookup key for localization of displaying the name';


--
-- Name: hiv_patients; Type: VIEW; Schema: clinlims; Owner: clinlims
--

CREATE VIEW hiv_patients AS
    SELECT DISTINCT ON (pt.id) pt.id AS patient_id, pt.person_id, pt.gender, (age(a.started_date, pt.birth_date) < ((28)::double precision * '1 day'::interval)) AS infant, a.completed_date FROM ((((((analysis a JOIN sample_item si ON ((a.sampitem_id = si.id))) JOIN sample_human sh ON ((si.samp_id = sh.samp_id))) JOIN patient pt ON ((sh.patient_id = pt.id))) JOIN test t ON ((a.test_id = t.id))) JOIN test_section ts ON (((t.test_section_id = ts.id) AND ((ts.name)::text = 'VCT'::text)))) JOIN status_of_sample sos ON ((((sos.name)::text ~~ 'Finished'::text) AND (sos.id = to_number((a.status_id)::text, '999999999'::text))))) ORDER BY pt.id;


ALTER TABLE clinlims.hiv_patients OWNER TO clinlims;

--
-- Name: hl7_encoding_type_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE hl7_encoding_type_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.hl7_encoding_type_seq OWNER TO clinlims;

--
-- Name: htmldb_plan_table; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE htmldb_plan_table (
    statement_id character varying(30),
    plan_id numeric,
    "timestamp" timestamp without time zone,
    remarks character varying(4000),
    operation character varying(30),
    options character varying(255),
    object_node character varying(128),
    object_owner character varying(30),
    object_name character varying(30),
    object_alias character varying(65),
    object_instance numeric,
    object_type character varying(30),
    optimizer character varying(255),
    search_columns numeric,
    id numeric,
    parent_id numeric,
    depth numeric,
    "position" numeric,
    cost numeric,
    cardinality numeric,
    bytes numeric,
    other_tag character varying(255),
    partition_start character varying(255),
    partition_stop character varying(255),
    partition_id numeric,
    other text,
    distribution character varying(30),
    cpu_cost numeric,
    io_cost numeric,
    temp_space numeric,
    access_predicates character varying(4000),
    filter_predicates character varying(4000),
    projection character varying(4000),
    "time" numeric,
    qblock_name character varying(30)
);


ALTER TABLE clinlims.htmldb_plan_table OWNER TO clinlims;

--
-- Name: instrument; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE instrument (
    id numeric(10,0) NOT NULL,
    scrip_id numeric(10,0),
    name character varying(20),
    description character varying(60),
    instru_type character varying(30),
    is_active character varying(1),
    location character varying(60)
);


ALTER TABLE clinlims.instrument OWNER TO clinlims;

--
-- Name: COLUMN instrument.name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN instrument.name IS 'Short name for instrument';


--
-- Name: COLUMN instrument.description; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN instrument.description IS 'Instrument description';


--
-- Name: COLUMN instrument.instru_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN instrument.instru_type IS 'Type of instrument: Mass Spec, HPLC, etc.';


--
-- Name: COLUMN instrument.is_active; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN instrument.is_active IS 'Flag indicating if the instrument is active';


--
-- Name: COLUMN instrument.location; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN instrument.location IS 'Location of instrument';


--
-- Name: instrument_analyte; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE instrument_analyte (
    id numeric(10,0) NOT NULL,
    analyte_id numeric(10,0),
    instru_id numeric(10,0),
    method_id numeric(10,0),
    result_group numeric
);


ALTER TABLE clinlims.instrument_analyte OWNER TO clinlims;

--
-- Name: COLUMN instrument_analyte.id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN instrument_analyte.id IS 'Sequential Identifier';


--
-- Name: COLUMN instrument_analyte.method_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN instrument_analyte.method_id IS 'Sequential number';


--
-- Name: COLUMN instrument_analyte.result_group; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN instrument_analyte.result_group IS 'A program generated group number';


--
-- Name: instrument_log; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE instrument_log (
    id numeric(10,0) NOT NULL,
    instru_id numeric(10,0),
    instlog_type character varying(1),
    event_begin timestamp without time zone,
    event_end timestamp without time zone
);


ALTER TABLE clinlims.instrument_log OWNER TO clinlims;

--
-- Name: COLUMN instrument_log.id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN instrument_log.id IS 'Sequential Identifier';


--
-- Name: COLUMN instrument_log.instlog_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN instrument_log.instlog_type IS 'type of log entry: downtime, maintenance';


--
-- Name: COLUMN instrument_log.event_begin; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN instrument_log.event_begin IS 'date-time logged event started';


--
-- Name: COLUMN instrument_log.event_end; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN instrument_log.event_end IS 'Date-time logged event ended';


--
-- Name: inventory_component; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE inventory_component (
    id numeric(10,0) NOT NULL,
    invitem_id numeric(10,0),
    quantity numeric,
    material_component_id numeric(10,0)
);


ALTER TABLE clinlims.inventory_component OWNER TO clinlims;

--
-- Name: COLUMN inventory_component.quantity; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN inventory_component.quantity IS 'Quantity of material required';


--
-- Name: inventory_item; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE inventory_item (
    id numeric(10,0) NOT NULL,
    uom_id numeric(10,0),
    name character varying(20),
    description character varying(60),
    quantity_min_level numeric,
    quantity_max_level numeric,
    quantity_to_reorder numeric,
    is_reorder_auto character varying(1),
    is_lot_maintained character varying(1),
    is_active character varying(1),
    average_lead_time numeric,
    average_cost numeric,
    average_daily_use numeric
);


ALTER TABLE clinlims.inventory_item OWNER TO clinlims;

--
-- Name: COLUMN inventory_item.name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN inventory_item.name IS 'Unique Short Name for this item ie "Red Top Tube"';


--
-- Name: COLUMN inventory_item.description; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN inventory_item.description IS 'Description of Item';


--
-- Name: COLUMN inventory_item.quantity_min_level; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN inventory_item.quantity_min_level IS 'Minimum inventory level';


--
-- Name: COLUMN inventory_item.quantity_max_level; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN inventory_item.quantity_max_level IS 'Maximum Inventory Level';


--
-- Name: COLUMN inventory_item.quantity_to_reorder; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN inventory_item.quantity_to_reorder IS 'Amount to reorder';


--
-- Name: COLUMN inventory_item.is_reorder_auto; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN inventory_item.is_reorder_auto IS 'Flag indicating system should automatically reorder';


--
-- Name: COLUMN inventory_item.is_lot_maintained; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN inventory_item.is_lot_maintained IS 'Indicates if individual lot information is maintained for this item';


--
-- Name: COLUMN inventory_item.is_active; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN inventory_item.is_active IS 'Indicates if this item is available for use.';


--
-- Name: COLUMN inventory_item.average_lead_time; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN inventory_item.average_lead_time IS 'Average lead time in days';


--
-- Name: COLUMN inventory_item.average_cost; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN inventory_item.average_cost IS 'Average cost per unit';


--
-- Name: COLUMN inventory_item.average_daily_use; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN inventory_item.average_daily_use IS 'Seasonally adjusted average usage per day';


--
-- Name: inventory_item_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE inventory_item_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.inventory_item_seq OWNER TO clinlims;

--
-- Name: inventory_location; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE inventory_location (
    id numeric(10,0) NOT NULL,
    storage_id numeric(10,0),
    lot_number character varying(20),
    quantity_onhand numeric,
    expiration_date timestamp without time zone,
    inv_item_id numeric(10,0)
);


ALTER TABLE clinlims.inventory_location OWNER TO clinlims;

--
-- Name: COLUMN inventory_location.lot_number; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN inventory_location.lot_number IS 'Lot number';


--
-- Name: COLUMN inventory_location.quantity_onhand; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN inventory_location.quantity_onhand IS 'Number of units onhand';


--
-- Name: inventory_location_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE inventory_location_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.inventory_location_seq OWNER TO clinlims;

--
-- Name: inventory_receipt; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE inventory_receipt (
    id numeric(10,0) NOT NULL,
    invitem_id numeric(10,0) NOT NULL,
    received_date timestamp without time zone,
    quantity_received numeric,
    unit_cost numeric,
    qc_reference character varying(20),
    external_reference character varying(20),
    org_id numeric(10,0)
);


ALTER TABLE clinlims.inventory_receipt OWNER TO clinlims;

--
-- Name: COLUMN inventory_receipt.received_date; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN inventory_receipt.received_date IS 'Date and time item was received';


--
-- Name: COLUMN inventory_receipt.quantity_received; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN inventory_receipt.quantity_received IS 'Number of units received';


--
-- Name: COLUMN inventory_receipt.unit_cost; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN inventory_receipt.unit_cost IS 'Cost per unit item';


--
-- Name: COLUMN inventory_receipt.qc_reference; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN inventory_receipt.qc_reference IS 'External QC reference number';


--
-- Name: COLUMN inventory_receipt.external_reference; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN inventory_receipt.external_reference IS 'External reference to purchase order, invoice number.';


--
-- Name: inventory_receipt_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE inventory_receipt_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.inventory_receipt_seq OWNER TO clinlims;

--
-- Name: lab_order_item; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE lab_order_item (
    id numeric(10,0) NOT NULL,
    lab_order_type_id numeric(10,0) NOT NULL,
    table_ref numeric(10,0),
    record_id numeric(10,0),
    identifier character varying(20),
    action character varying(20),
    lastupdated timestamp with time zone DEFAULT now()
);


ALTER TABLE clinlims.lab_order_item OWNER TO clinlims;

--
-- Name: TABLE lab_order_item; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE lab_order_item IS 'Association table between lab order type and the thing they affect';


--
-- Name: COLUMN lab_order_item.lab_order_type_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN lab_order_item.lab_order_type_id IS 'The lab order type this refers to';


--
-- Name: COLUMN lab_order_item.table_ref; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN lab_order_item.table_ref IS 'If the thing it refers to is a db object what table is it in';


--
-- Name: COLUMN lab_order_item.record_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN lab_order_item.record_id IS 'If the thing it refers to is a db object what record in the table is it';


--
-- Name: COLUMN lab_order_item.identifier; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN lab_order_item.identifier IS 'If this is not a db object then another way to identify it.  could be a class name on a form';


--
-- Name: COLUMN lab_order_item.action; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN lab_order_item.action IS 'What should happen if this is in a lab order type';


--
-- Name: lab_order_item_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE lab_order_item_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.lab_order_item_seq OWNER TO clinlims;

--
-- Name: lab_order_type; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE lab_order_type (
    id numeric(10,0) NOT NULL,
    domain character varying(20) NOT NULL,
    type character varying(40) NOT NULL,
    context character varying(60),
    description character varying(60),
    sort_order numeric,
    lastupdated timestamp with time zone,
    display_key character varying(60)
);


ALTER TABLE clinlims.lab_order_type OWNER TO clinlims;

--
-- Name: TABLE lab_order_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE lab_order_type IS 'If lab differentiates based on the type of order i.e. first visit, follow-up.  The types are defined here';


--
-- Name: COLUMN lab_order_type.domain; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN lab_order_type.domain IS 'Refers to Human, Environmental, New born etc';


--
-- Name: COLUMN lab_order_type.type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN lab_order_type.type IS 'The lab order type i.e. first visit, follow-up etc';


--
-- Name: COLUMN lab_order_type.context; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN lab_order_type.context IS 'What is the context that this type is significant. i.e. Sample Entry, confirmation entry';


--
-- Name: COLUMN lab_order_type.sort_order; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN lab_order_type.sort_order IS 'What is the order when displayed to the user';


--
-- Name: COLUMN lab_order_type.display_key; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN lab_order_type.display_key IS 'Localization information.  Match found in MessageResource.properties';


--
-- Name: lab_order_type_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE lab_order_type_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.lab_order_type_seq OWNER TO clinlims;

--
-- Name: label; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE label (
    id numeric(10,0) NOT NULL,
    name character varying(30),
    description character varying(60),
    printer_type character(1),
    scriptlet_id numeric(10,0),
    lastupdated timestamp(6) without time zone
);


ALTER TABLE clinlims.label OWNER TO clinlims;

--
-- Name: label_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE label_seq
    START WITH 3
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.label_seq OWNER TO clinlims;

--
-- Name: login_user; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE login_user (
    id numeric(10,0) NOT NULL,
    login_name character varying(20) NOT NULL,
    password character varying(256) NOT NULL,
    password_expired_dt date NOT NULL,
    account_locked character varying(1) NOT NULL,
    account_disabled character varying(1) NOT NULL,
    is_admin character varying(1) NOT NULL,
    user_time_out character varying(3) NOT NULL
);


ALTER TABLE clinlims.login_user OWNER TO clinlims;

--
-- Name: COLUMN login_user.login_name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN login_user.login_name IS 'User LOGIN_NAME from SYSTEM_USER table';


--
-- Name: COLUMN login_user.password; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN login_user.password IS 'User password';


--
-- Name: COLUMN login_user.password_expired_dt; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN login_user.password_expired_dt IS 'Password expiration date';


--
-- Name: COLUMN login_user.account_locked; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN login_user.account_locked IS 'Account locked (Y/N)';


--
-- Name: COLUMN login_user.account_disabled; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN login_user.account_disabled IS 'Account disabled (Y/N)';


--
-- Name: COLUMN login_user.is_admin; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN login_user.is_admin IS 'Indicates if this user is administrator (Y/N)';


--
-- Name: COLUMN login_user.user_time_out; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN login_user.user_time_out IS 'User session time out in minute';


--
-- Name: login_user_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE login_user_seq
    START WITH 0
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.login_user_seq OWNER TO clinlims;

--
-- Name: menu; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE menu (
    id numeric(10,0) NOT NULL,
    parent_id numeric(10,0),
    presentation_order numeric,
    element_id character varying(40) NOT NULL,
    action_url character varying(120),
    click_action character varying(120),
    display_key character varying(60),
    tool_tip_key character varying(60),
    new_window boolean DEFAULT false
);


ALTER TABLE clinlims.menu OWNER TO clinlims;

--
-- Name: TABLE menu; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE menu IS 'Table for menuing system';


--
-- Name: COLUMN menu.id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN menu.id IS 'primary key';


--
-- Name: COLUMN menu.parent_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN menu.parent_id IS 'If this is a submenu then the parent menu id';


--
-- Name: COLUMN menu.presentation_order; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN menu.presentation_order IS 'For top level menus the order across the page for sub menu the order in the popup menu.  If there is a collision then the order is alphebetical';


--
-- Name: COLUMN menu.element_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN menu.element_id IS 'The element id in the context of HTML.';


--
-- Name: COLUMN menu.action_url; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN menu.action_url IS 'If clicking on the element moves to a new page, the url of that page';


--
-- Name: COLUMN menu.click_action; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN menu.click_action IS 'If clicking on the element calls javascript then the javascript call';


--
-- Name: COLUMN menu.display_key; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN menu.display_key IS 'The message key for what will be displayed in the menu';


--
-- Name: COLUMN menu.tool_tip_key; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN menu.tool_tip_key IS 'The message key for the tool-tip';


--
-- Name: COLUMN menu.new_window; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN menu.new_window IS 'If true the menu action will be done in a new window';


--
-- Name: menu_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE menu_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.menu_seq OWNER TO clinlims;

--
-- Name: message_org; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE message_org (
    id numeric(10,0) NOT NULL,
    org_id character varying(60),
    is_active character varying(1),
    active_begin timestamp without time zone,
    active_end timestamp without time zone,
    description character varying(60),
    lastupdated timestamp(6) without time zone
);


ALTER TABLE clinlims.message_org OWNER TO clinlims;

--
-- Name: message_org_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE message_org_seq
    START WITH 41
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;


ALTER TABLE clinlims.message_org_seq OWNER TO clinlims;

--
-- Name: messagecontrolidseq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE messagecontrolidseq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.messagecontrolidseq OWNER TO clinlims;

--
-- Name: method; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE method (
    id numeric(10,0) NOT NULL,
    name character varying(20) NOT NULL,
    description character varying(60) NOT NULL,
    reporting_description character varying(60),
    is_active character varying(1),
    active_begin timestamp without time zone,
    active_end timestamp without time zone,
    lastupdated timestamp(6) without time zone
);


ALTER TABLE clinlims.method OWNER TO clinlims;

--
-- Name: COLUMN method.id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN method.id IS 'Sequential number';


--
-- Name: COLUMN method.name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN method.name IS 'Sort name for method';


--
-- Name: COLUMN method.description; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN method.description IS 'Description for Method';


--
-- Name: COLUMN method.reporting_description; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN method.reporting_description IS 'Description that appears on reports';


--
-- Name: COLUMN method.is_active; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN method.is_active IS 'Flag indicating if the test is active';


--
-- Name: COLUMN method.active_begin; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN method.active_begin IS 'Date test was moved into production';


--
-- Name: COLUMN method.active_end; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN method.active_end IS 'Date test was removed from production';


--
-- Name: method_analyte; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE method_analyte (
    id numeric(10,0) NOT NULL,
    method_id numeric(10,0),
    analyte_id numeric(10,0),
    result_group numeric,
    sort_order numeric,
    ma_type character varying(1)
);


ALTER TABLE clinlims.method_analyte OWNER TO clinlims;

--
-- Name: COLUMN method_analyte.method_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN method_analyte.method_id IS 'Sequential number';


--
-- Name: COLUMN method_analyte.result_group; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN method_analyte.result_group IS 'a program generated group (sequence) number';


--
-- Name: COLUMN method_analyte.sort_order; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN method_analyte.sort_order IS 'The order the analytes are displayed (sort order)';


--
-- Name: COLUMN method_analyte.ma_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN method_analyte.ma_type IS 'type of analyte';


--
-- Name: method_result; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE method_result (
    id numeric(10,0) NOT NULL,
    scrip_id numeric(10,0),
    result_group numeric,
    flags character varying(10),
    methres_type character varying(1),
    value character varying(80),
    quant_limit character varying(30),
    cont_level character varying(30),
    method_id numeric(10,0)
);


ALTER TABLE clinlims.method_result OWNER TO clinlims;

--
-- Name: COLUMN method_result.result_group; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN method_result.result_group IS 'The method analyte result group number';


--
-- Name: COLUMN method_result.flags; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN method_result.flags IS 'A string of 1 character codes: Positive, Reportable';


--
-- Name: COLUMN method_result.methres_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN method_result.methres_type IS 'Type of parameter: Dictionary, Titer-range, Number-range, date';


--
-- Name: COLUMN method_result.value; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN method_result.value IS 'A possible result value based on type';


--
-- Name: COLUMN method_result.quant_limit; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN method_result.quant_limit IS 'Quantitation Limit (if any)';


--
-- Name: COLUMN method_result.cont_level; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN method_result.cont_level IS 'Contaminant level (if any)';


--
-- Name: method_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE method_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.method_seq OWNER TO clinlims;

--
-- Name: mls_lab_type; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE mls_lab_type (
    id numeric(10,0) NOT NULL,
    description character varying(50) NOT NULL,
    org_mlt_org_mlt_id numeric(10,0)
);


ALTER TABLE clinlims.mls_lab_type OWNER TO clinlims;

--
-- Name: COLUMN mls_lab_type.id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN mls_lab_type.id IS 'Sequential ID';


--
-- Name: COLUMN mls_lab_type.description; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN mls_lab_type.description IS 'Used to define MLS lab types including Level A, Urine, Virology';


--
-- Name: note; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE note (
    id numeric(10,0) NOT NULL,
    sys_user_id numeric(10,0),
    reference_id numeric,
    reference_table numeric,
    note_type character varying(1),
    subject character varying(60),
    text character varying(1000),
    lastupdated timestamp(6) without time zone
);


ALTER TABLE clinlims.note OWNER TO clinlims;

--
-- Name: COLUMN note.sys_user_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN note.sys_user_id IS 'Sequential Identifier';


--
-- Name: COLUMN note.reference_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN note.reference_id IS 'Link to record in table to which this entry pertains.';


--
-- Name: COLUMN note.reference_table; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN note.reference_table IS 'Link to table that this entity belongs to';


--
-- Name: COLUMN note.note_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN note.note_type IS 'Type of comment such as external, internal';


--
-- Name: COLUMN note.subject; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN note.subject IS 'Comment subject';


--
-- Name: note_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE note_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;


ALTER TABLE clinlims.note_seq OWNER TO clinlims;

--
-- Name: observation_history; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE observation_history (
    id numeric(10,0) NOT NULL,
    patient_id numeric(10,0) NOT NULL,
    sample_id numeric(10,0) NOT NULL,
    observation_history_type_id numeric(10,0) NOT NULL,
    value_type character varying(5) NOT NULL,
    value character varying(80),
    lastupdated timestamp with time zone,
    sample_item_id numeric(10,0)
);


ALTER TABLE clinlims.observation_history OWNER TO clinlims;

--
-- Name: TABLE observation_history; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE observation_history IS 'defines the answer at the time of a interview (with sample) to a demographic question.';


--
-- Name: COLUMN observation_history.id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN observation_history.id IS 'primary key';


--
-- Name: COLUMN observation_history.patient_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN observation_history.patient_id IS 'FK linking this information to a patient';


--
-- Name: COLUMN observation_history.sample_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN observation_history.sample_id IS 'FK linking this information to a sample (and a collection date).';


--
-- Name: COLUMN observation_history.observation_history_type_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN observation_history.observation_history_type_id IS 'FK to type table to define what is contained in the value column';


--
-- Name: COLUMN observation_history.value_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN observation_history.value_type IS 'L=local or literal, a value right here in the history table. D=Defined/Dictionary, the value is a foreign key to the Dictionary table. For multiple choice questions with fixed answers.';


--
-- Name: COLUMN observation_history.value; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN observation_history.value IS 'either a literal value for this demo. type, or a Foreign key to dictionary';


--
-- Name: COLUMN observation_history.sample_item_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN observation_history.sample_item_id IS 'Optional refereence to sample item';


--
-- Name: observation_history_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE observation_history_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.observation_history_seq OWNER TO clinlims;

--
-- Name: observation_history_type; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE observation_history_type (
    id numeric(10,0) NOT NULL,
    type_name character varying(30) NOT NULL,
    description character varying(400),
    lastupdated timestamp with time zone
);


ALTER TABLE clinlims.observation_history_type OWNER TO clinlims;

--
-- Name: TABLE observation_history_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE observation_history_type IS 'defines the possible classes of values allowed in the demo. history table.';


--
-- Name: COLUMN observation_history_type.id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN observation_history_type.id IS 'primary key';


--
-- Name: COLUMN observation_history_type.type_name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN observation_history_type.type_name IS 'a short tag (1 word) for this type. What was asked "Gender", "HIVPositive" etc.';


--
-- Name: COLUMN observation_history_type.description; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN observation_history_type.description IS 'a long description of this type.';


--
-- Name: COLUMN observation_history_type.lastupdated; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN observation_history_type.lastupdated IS 'the last time this item was written to the database.';


--
-- Name: observation_history_type_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE observation_history_type_seq
    START WITH 0
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.observation_history_type_seq OWNER TO clinlims;

--
-- Name: occupation; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE occupation (
    id numeric(10,0) NOT NULL,
    occupation character varying(300),
    lastupdated timestamp without time zone
);


ALTER TABLE clinlims.occupation OWNER TO clinlims;

--
-- Name: occupation_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE occupation_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;


ALTER TABLE clinlims.occupation_seq OWNER TO clinlims;

--
-- Name: or_properties; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE or_properties (
    property_id integer NOT NULL,
    property_key character varying(255) NOT NULL,
    property_value character varying(255)
);


ALTER TABLE clinlims.or_properties OWNER TO clinlims;

--
-- Name: or_tags; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE or_tags (
    tag_id integer NOT NULL,
    tagged_object_id integer NOT NULL,
    tagged_object_class character varying(255) NOT NULL,
    tag_value character varying(255) NOT NULL,
    tag_type character varying(255) NOT NULL
);


ALTER TABLE clinlims.or_tags OWNER TO clinlims;

--
-- Name: order_item; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE order_item (
    id numeric(10,0) NOT NULL,
    ord_id numeric(10,0) NOT NULL,
    quantity_requested numeric,
    quantity_received numeric,
    inv_loc_id numeric(10,0)
);


ALTER TABLE clinlims.order_item OWNER TO clinlims;

--
-- Name: COLUMN order_item.quantity_requested; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN order_item.quantity_requested IS 'Quantity requested by organization';


--
-- Name: COLUMN order_item.quantity_received; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN order_item.quantity_received IS 'Quantity received';


--
-- Name: orders; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE orders (
    id numeric(10,0) NOT NULL,
    org_id numeric(10,0) NOT NULL,
    sys_user_id numeric(10,0),
    ordered_date timestamp without time zone,
    neededby_date timestamp without time zone,
    requested_by character varying(30),
    cost_center character varying(15),
    shipping_type character varying(2),
    shipping_carrier character varying(2),
    shipping_cost numeric,
    delivered_date timestamp without time zone,
    is_external character varying(1),
    external_order_number character varying(20),
    is_filled character varying(1)
);


ALTER TABLE clinlims.orders OWNER TO clinlims;

--
-- Name: COLUMN orders.org_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN orders.org_id IS 'Sequential Numbering Field';


--
-- Name: COLUMN orders.sys_user_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN orders.sys_user_id IS 'Sequential Identifier';


--
-- Name: COLUMN orders.neededby_date; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN orders.neededby_date IS 'Date-time the order must be received by client';


--
-- Name: COLUMN orders.requested_by; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN orders.requested_by IS 'Name of person/entity who placed the order';


--
-- Name: COLUMN orders.cost_center; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN orders.cost_center IS 'Entity or project the order will be charged against';


--
-- Name: COLUMN orders.shipping_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN orders.shipping_type IS 'Type of shipping such as: Air, ground, First Class, Bulk....';


--
-- Name: COLUMN orders.shipping_carrier; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN orders.shipping_carrier IS 'Company used for shipping: UPS, FedEx, USPS...';


--
-- Name: COLUMN orders.shipping_cost; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN orders.shipping_cost IS 'Shipping Cost';


--
-- Name: COLUMN orders.delivered_date; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN orders.delivered_date IS 'Date-time shipment received by client (including us)';


--
-- Name: COLUMN orders.is_external; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN orders.is_external IS 'Indicates if current order will be filled by an external vendor';


--
-- Name: COLUMN orders.external_order_number; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN orders.external_order_number IS 'External order number';


--
-- Name: COLUMN orders.is_filled; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN orders.is_filled IS 'Flag indicating if the order has been filled';


--
-- Name: org_hl7_encoding_type; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE org_hl7_encoding_type (
    organization_id numeric(10,0) NOT NULL,
    encoding_type_id numeric(10,0) NOT NULL,
    lastupdated timestamp with time zone
);


ALTER TABLE clinlims.org_hl7_encoding_type OWNER TO clinlims;

--
-- Name: TABLE org_hl7_encoding_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE org_hl7_encoding_type IS 'mapping table to identify which organization uses which hly encoding schema';


--
-- Name: org_mls_lab_type; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE org_mls_lab_type (
    org_id numeric NOT NULL,
    mls_lab_id numeric NOT NULL,
    org_mlt_id numeric(10,0) NOT NULL
);


ALTER TABLE clinlims.org_mls_lab_type OWNER TO clinlims;

--
-- Name: COLUMN org_mls_lab_type.org_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN org_mls_lab_type.org_id IS 'foreign key from organization table';


--
-- Name: COLUMN org_mls_lab_type.mls_lab_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN org_mls_lab_type.mls_lab_id IS 'foreign key from MLS lab type table';


--
-- Name: organization; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE organization (
    id numeric(10,0) NOT NULL,
    name character varying(80) NOT NULL,
    city character varying(30),
    zip_code character varying(10),
    mls_sentinel_lab_flag character varying(1) DEFAULT 'N'::character varying NOT NULL,
    org_mlt_org_mlt_id numeric(10,0),
    org_id numeric(10,0),
    short_name character varying(15),
    multiple_unit character varying(30),
    street_address character varying(30),
    state character varying(2),
    internet_address character varying(40),
    clia_num character varying(12),
    pws_id character varying(15),
    lastupdated timestamp(6) without time zone,
    mls_lab_flag character(1),
    is_active character(1),
    local_abbrev character varying(10)
);


ALTER TABLE clinlims.organization OWNER TO clinlims;

--
-- Name: COLUMN organization.id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN organization.id IS 'Sequential Numbering Field';


--
-- Name: COLUMN organization.name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN organization.name IS 'The full name of an organization';


--
-- Name: COLUMN organization.city; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN organization.city IS 'City';


--
-- Name: COLUMN organization.zip_code; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN organization.zip_code IS 'Zip code';


--
-- Name: COLUMN organization.mls_sentinel_lab_flag; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN organization.mls_sentinel_lab_flag IS 'Yes or No field indicating that the submitter is an Minnesota Laboratory System Lab';


--
-- Name: COLUMN organization.org_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN organization.org_id IS 'Sequential Numbering Field';


--
-- Name: COLUMN organization.short_name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN organization.short_name IS 'A shortened or abbreviated name of an organization. For example "BCBSM" is the short name for Blue Cross Blue Shield Minnesota';


--
-- Name: COLUMN organization.multiple_unit; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN organization.multiple_unit IS 'Apartment number or unit information';


--
-- Name: COLUMN organization.street_address; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN organization.street_address IS 'Street address for this organization';


--
-- Name: COLUMN organization.state; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN organization.state IS 'State or Province';


--
-- Name: COLUMN organization.internet_address; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN organization.internet_address IS 'A uniform resource locator (URL), ie a website address assigned to an entity or pager.';


--
-- Name: COLUMN organization.clia_num; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN organization.clia_num IS 'Clinical Laboratory Improvement Amendments number';


--
-- Name: COLUMN organization.pws_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN organization.pws_id IS 'Public water supply id (SDWIS)';


--
-- Name: organization_address; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE organization_address (
    organization_id numeric(10,0) NOT NULL,
    address_part_id numeric(10,0) NOT NULL,
    type character(1) DEFAULT 'T'::bpchar,
    value character varying(120)
);


ALTER TABLE clinlims.organization_address OWNER TO clinlims;

--
-- Name: TABLE organization_address; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE organization_address IS 'Join table between address parts and an organization';


--
-- Name: COLUMN organization_address.organization_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN organization_address.organization_id IS 'The id of the organization who this address is attached to';


--
-- Name: COLUMN organization_address.address_part_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN organization_address.address_part_id IS 'The id of the address part for which we have a value';


--
-- Name: COLUMN organization_address.type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN organization_address.type IS 'The type of the value, either D for dictionary or T for text';


--
-- Name: COLUMN organization_address.value; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN organization_address.value IS 'The actual value for this part of the address';


--
-- Name: organization_contact; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE organization_contact (
    id numeric(10,0) NOT NULL,
    organization_id numeric(10,0) NOT NULL,
    person_id numeric(10,0) NOT NULL,
    "position" character varying(32)
);


ALTER TABLE clinlims.organization_contact OWNER TO clinlims;

--
-- Name: TABLE organization_contact; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE organization_contact IS 'A join table between organizations and persons';


--
-- Name: COLUMN organization_contact.id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN organization_contact.id IS 'Unique key for each row';


--
-- Name: COLUMN organization_contact.organization_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN organization_contact.organization_id IS 'The id of the organization being referred to';


--
-- Name: COLUMN organization_contact.person_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN organization_contact.person_id IS 'The id of the person being referred to';


--
-- Name: COLUMN organization_contact."position"; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN organization_contact."position" IS 'The position of the person within the organization';


--
-- Name: organization_contact_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE organization_contact_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.organization_contact_seq OWNER TO clinlims;

--
-- Name: organization_organization_type; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE organization_organization_type (
    org_id numeric(10,0) NOT NULL,
    org_type_id numeric(10,0) NOT NULL
);


ALTER TABLE clinlims.organization_organization_type OWNER TO clinlims;

--
-- Name: TABLE organization_organization_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE organization_organization_type IS 'many to many mapping table between organization and orginaztion type';


--
-- Name: organization_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE organization_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.organization_seq OWNER TO clinlims;

--
-- Name: organization_type; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE organization_type (
    id numeric(10,0) NOT NULL,
    short_name character varying(20) NOT NULL,
    description character varying(60),
    name_display_key character varying(60),
    lastupdated timestamp(6) without time zone
);


ALTER TABLE clinlims.organization_type OWNER TO clinlims;

--
-- Name: TABLE organization_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE organization_type IS 'The type of an organization.  Releationship will be many to many';


--
-- Name: COLUMN organization_type.short_name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN organization_type.short_name IS 'The name to be displayed in when organization type is to be associated with an organization';


--
-- Name: COLUMN organization_type.description; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN organization_type.description IS 'Optional longer description of the type';


--
-- Name: COLUMN organization_type.name_display_key; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN organization_type.name_display_key IS 'Resource file lookup key for localization of displaying the name';


--
-- Name: organization_type_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE organization_type_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.organization_type_seq OWNER TO clinlims;

--
-- Name: package_1; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE package_1 (
    id numeric(10,0) NOT NULL
);


ALTER TABLE clinlims.package_1 OWNER TO clinlims;

--
-- Name: panel; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE panel (
    id numeric(10,0) NOT NULL,
    name character varying(64),
    description character varying(64) NOT NULL,
    lastupdated timestamp(6) without time zone,
    display_key character varying(60),
    sort_order numeric DEFAULT 2147483647,
    is_active character varying(1) DEFAULT 'Y'::character varying
);


ALTER TABLE clinlims.panel OWNER TO clinlims;

--
-- Name: COLUMN panel.display_key; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN panel.display_key IS 'Resource file lookup key for localization of displaying the name';


--
-- Name: COLUMN panel.is_active; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN panel.is_active IS 'Is this panel currently active';


--
-- Name: panel_item; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE panel_item (
    id numeric(10,0) NOT NULL,
    panel_id numeric(10,0) NOT NULL,
    sort_order numeric,
    test_local_abbrev character varying(20),
    method_name character varying(20),
    lastupdated timestamp(6) without time zone,
    test_name character varying(20),
    test_id numeric(10,0) NOT NULL
);


ALTER TABLE clinlims.panel_item OWNER TO clinlims;

--
-- Name: COLUMN panel_item.sort_order; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN panel_item.sort_order IS 'The order the tests are displayed (sort order)';


--
-- Name: COLUMN panel_item.test_local_abbrev; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN panel_item.test_local_abbrev IS 'Short test name';


--
-- Name: COLUMN panel_item.method_name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN panel_item.method_name IS 'Short method name';


--
-- Name: panel_item_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE panel_item_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.panel_item_seq OWNER TO clinlims;

--
-- Name: panel_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE panel_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.panel_seq OWNER TO clinlims;

--
-- Name: patient_identity; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE patient_identity (
    id numeric(10,0) NOT NULL,
    identity_type_id numeric(10,0),
    patient_id numeric(10,0),
    identity_data character varying(40),
    lastupdated timestamp(6) without time zone
);


ALTER TABLE clinlims.patient_identity OWNER TO clinlims;

--
-- Name: patient_identity_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE patient_identity_seq
    START WITH 0
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.patient_identity_seq OWNER TO clinlims;

--
-- Name: patient_identity_type; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE patient_identity_type (
    id numeric(10,0) NOT NULL,
    identity_type character varying(30),
    description character varying(400),
    lastupdated timestamp without time zone
);


ALTER TABLE clinlims.patient_identity_type OWNER TO clinlims;

--
-- Name: patient_identity_type_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE patient_identity_type_seq
    START WITH 30
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.patient_identity_type_seq OWNER TO clinlims;

--
-- Name: patient_occupation; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE patient_occupation (
    id numeric(10,0) NOT NULL,
    patient_id numeric(10,0),
    occupation character varying(400),
    lastupdated timestamp without time zone
);


ALTER TABLE clinlims.patient_occupation OWNER TO clinlims;

--
-- Name: patient_occupation_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE patient_occupation_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.patient_occupation_seq OWNER TO clinlims;

--
-- Name: patient_patient_type; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE patient_patient_type (
    id numeric(10,0) NOT NULL,
    patient_type_id numeric(10,0),
    patient_id numeric(10,0),
    lastupdated timestamp without time zone
);


ALTER TABLE clinlims.patient_patient_type OWNER TO clinlims;

--
-- Name: patient_patient_type_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE patient_patient_type_seq
    START WITH 0
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.patient_patient_type_seq OWNER TO clinlims;

--
-- Name: patient_relation_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE patient_relation_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.patient_relation_seq OWNER TO clinlims;

--
-- Name: patient_relations; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE patient_relations (
    id numeric(10,0) NOT NULL,
    pat_id_source numeric(10,0) NOT NULL,
    pat_id numeric(10,0),
    relation character varying(1)
);


ALTER TABLE clinlims.patient_relations OWNER TO clinlims;

--
-- Name: COLUMN patient_relations.relation; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN patient_relations.relation IS 'Type of relation (mother to child, parent to child, sibling)';


--
-- Name: patient_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE patient_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.patient_seq OWNER TO clinlims;

--
-- Name: patient_type; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE patient_type (
    id numeric(10,0) NOT NULL,
    type character varying(300),
    description character varying(4000),
    lastupdated timestamp without time zone
);


ALTER TABLE clinlims.patient_type OWNER TO clinlims;

--
-- Name: patient_type_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE patient_type_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;


ALTER TABLE clinlims.patient_type_seq OWNER TO clinlims;

--
-- Name: payment_type; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE payment_type (
    id numeric(10,0) NOT NULL,
    type character varying(300),
    description character varying(4000)
);


ALTER TABLE clinlims.payment_type OWNER TO clinlims;

--
-- Name: payment_type_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE payment_type_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;


ALTER TABLE clinlims.payment_type_seq OWNER TO clinlims;

--
-- Name: person; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE person (
    id numeric(10,0) NOT NULL,
    last_name character varying(40),
    first_name character varying(40),
    middle_name character varying(30),
    multiple_unit character varying(30),
    street_address character(30),
    city character(30),
    state character(2),
    zip_code character(10),
    country character varying(20),
    work_phone character varying(17),
    home_phone character(12),
    cell_phone character varying(17),
    fax character varying(17),
    email character varying(60),
    lastupdated timestamp(6) without time zone
);


ALTER TABLE clinlims.person OWNER TO clinlims;

--
-- Name: COLUMN person.last_name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN person.last_name IS 'Last name';


--
-- Name: COLUMN person.first_name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN person.first_name IS 'Person Name';


--
-- Name: COLUMN person.middle_name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN person.middle_name IS 'Middle Name';


--
-- Name: COLUMN person.multiple_unit; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN person.multiple_unit IS 'Designates a specific part of a building, such as "APT 212"';


--
-- Name: person_address; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE person_address (
    person_id numeric(10,0) NOT NULL,
    address_part_id numeric(10,0) NOT NULL,
    type character(1) DEFAULT 'T'::bpchar,
    value character varying(120)
);


ALTER TABLE clinlims.person_address OWNER TO clinlims;

--
-- Name: TABLE person_address; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE person_address IS 'Join table between address parts and a person';


--
-- Name: COLUMN person_address.person_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN person_address.person_id IS 'The id of the person who this address is attached to';


--
-- Name: COLUMN person_address.address_part_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN person_address.address_part_id IS 'The id of the address part for which we have a value';


--
-- Name: COLUMN person_address.type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN person_address.type IS 'The type of the value, either D for dictionary or T for text';


--
-- Name: COLUMN person_address.value; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN person_address.value IS 'The actual value for this part of the address';


--
-- Name: person_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE person_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.person_seq OWNER TO clinlims;

--
-- Name: program; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE program (
    id numeric(10,0) NOT NULL,
    code character varying(10) NOT NULL,
    name character varying(50) NOT NULL,
    lastupdated timestamp(6) without time zone
);


ALTER TABLE clinlims.program OWNER TO clinlims;

--
-- Name: COLUMN program.name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN program.name IS 'EPI PROGRAM NAME';


--
-- Name: program_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE program_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.program_seq OWNER TO clinlims;

--
-- Name: project; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE project (
    id numeric(10,0) NOT NULL,
    name character varying(50) NOT NULL,
    sys_user_id numeric(10,0),
    description character varying(60),
    started_date timestamp without time zone,
    completed_date timestamp without time zone,
    is_active character varying(1),
    reference_to character varying(20),
    program_code character varying(10),
    lastupdated timestamp(6) without time zone,
    scriptlet_id numeric(10,0),
    local_abbrev numeric(10,0),
    display_key character varying(60)
);


ALTER TABLE clinlims.project OWNER TO clinlims;

--
-- Name: COLUMN project.id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN project.id IS 'Sequential number assigned by sequence';


--
-- Name: COLUMN project.name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN project.name IS 'Short name of Project';


--
-- Name: COLUMN project.sys_user_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN project.sys_user_id IS 'Sequential Identifier';


--
-- Name: COLUMN project.description; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN project.description IS 'Description of Project';


--
-- Name: COLUMN project.started_date; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN project.started_date IS 'Start date of project';


--
-- Name: COLUMN project.completed_date; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN project.completed_date IS 'End date of project';


--
-- Name: COLUMN project.is_active; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN project.is_active IS 'Flag indicating if project is active';


--
-- Name: COLUMN project.reference_to; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN project.reference_to IS 'External reference information such as Grant number, contract number or purchase order number associated with this project.';


--
-- Name: COLUMN project.display_key; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN project.display_key IS 'Resource file lookup key for localization of displaying the name';


--
-- Name: project_organization; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE project_organization (
    project_id numeric(10,0) NOT NULL,
    org_id numeric(10,0) NOT NULL
);


ALTER TABLE clinlims.project_organization OWNER TO clinlims;

--
-- Name: TABLE project_organization; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE project_organization IS 'many to many mapping table between project and organization';


--
-- Name: project_parameter; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE project_parameter (
    id numeric(10,0) NOT NULL,
    projparam_type character varying(1),
    operation character varying(2),
    value character varying(256),
    project_id numeric(10,0),
    param_name character varying(80)
);


ALTER TABLE clinlims.project_parameter OWNER TO clinlims;

--
-- Name: COLUMN project_parameter.projparam_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN project_parameter.projparam_type IS 'Type of parameter: apply-parameter or search-parameter';


--
-- Name: COLUMN project_parameter.operation; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN project_parameter.operation IS 'Operation to be performed: =, <, <=, >, >=,in,!=';


--
-- Name: COLUMN project_parameter.value; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN project_parameter.value IS 'Query or Set value';


--
-- Name: project_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE project_seq
    START WITH 13
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.project_seq OWNER TO clinlims;

--
-- Name: provider; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE provider (
    id numeric(10,0) NOT NULL,
    npi character varying(10),
    person_id numeric(10,0) NOT NULL,
    external_id character varying(50),
    provider_type character varying(1),
    lastupdated timestamp(6) without time zone
);


ALTER TABLE clinlims.provider OWNER TO clinlims;

--
-- Name: COLUMN provider.npi; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN provider.npi IS 'Unique National Provider ID';


--
-- Name: COLUMN provider.external_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN provider.external_id IS 'National/Local provider reference number such as UPIN or MINC#NIMC';


--
-- Name: COLUMN provider.provider_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN provider.provider_type IS 'Type of Provider (physician, nurse)';


--
-- Name: provider_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE provider_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.provider_seq OWNER TO clinlims;

--
-- Name: qa_event; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE qa_event (
    id numeric(10,0) NOT NULL,
    name character varying(20),
    description character varying(120),
    is_billable character varying(1),
    reporting_sequence numeric,
    reporting_text character varying(1000),
    test_id numeric(10,0),
    is_holdable character varying(1) NOT NULL,
    lastupdated timestamp(6) without time zone,
    type numeric(10,0),
    category numeric(10,0),
    display_key character varying(60)
);


ALTER TABLE clinlims.qa_event OWNER TO clinlims;

--
-- Name: COLUMN qa_event.name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN qa_event.name IS 'Short Name';


--
-- Name: COLUMN qa_event.description; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN qa_event.description IS 'Description of the QA event';


--
-- Name: COLUMN qa_event.is_billable; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN qa_event.is_billable IS 'Indicates if analysis with this QA Event is billable';


--
-- Name: COLUMN qa_event.reporting_sequence; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN qa_event.reporting_sequence IS 'An overall number that orders the print sequence';


--
-- Name: COLUMN qa_event.test_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN qa_event.test_id IS 'Reported/printed text';


--
-- Name: COLUMN qa_event.display_key; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN qa_event.display_key IS 'Resource file lookup key for localization of displaying the name';


--
-- Name: qa_event_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE qa_event_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;


ALTER TABLE clinlims.qa_event_seq OWNER TO clinlims;

--
-- Name: qa_observation; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE qa_observation (
    id numeric(10,0) NOT NULL,
    observed_id numeric(10,0) NOT NULL,
    observed_type character varying(30) NOT NULL,
    qa_observation_type_id numeric(10,0) NOT NULL,
    value_type character varying(1) NOT NULL,
    value character varying(30) NOT NULL,
    lastupdated timestamp with time zone DEFAULT now()
);


ALTER TABLE clinlims.qa_observation OWNER TO clinlims;

--
-- Name: TABLE qa_observation; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE qa_observation IS 'The observation that are about sample/analysis qa_events.  Does not include data about the sample';


--
-- Name: COLUMN qa_observation.observed_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN qa_observation.observed_id IS 'The row id for either sample_qaEvent or analysis_qaEvent';


--
-- Name: COLUMN qa_observation.observed_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN qa_observation.observed_type IS 'One of ANALYSIS or SAMPLE';


--
-- Name: COLUMN qa_observation.qa_observation_type_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN qa_observation.qa_observation_type_id IS 'The type of observation this is';


--
-- Name: COLUMN qa_observation.value_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN qa_observation.value_type IS 'Dictionary or literal, D or L';


--
-- Name: COLUMN qa_observation.value; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN qa_observation.value IS 'The actual value';


--
-- Name: qa_observation_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE qa_observation_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.qa_observation_seq OWNER TO clinlims;

--
-- Name: qa_observation_type; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE qa_observation_type (
    id numeric(10,0) NOT NULL,
    name character varying(30) NOT NULL,
    description character varying(60),
    lastupdated timestamp with time zone DEFAULT now()
);


ALTER TABLE clinlims.qa_observation_type OWNER TO clinlims;

--
-- Name: TABLE qa_observation_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE qa_observation_type IS 'The types of observation that are about sample/analysis qa_events ';


--
-- Name: qa_observation_type_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE qa_observation_type_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.qa_observation_type_seq OWNER TO clinlims;

--
-- Name: qc; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE qc (
    id numeric NOT NULL,
    uom_id numeric(10,0),
    sys_user_id numeric(10,0),
    name character varying(30),
    source character varying(30),
    lot_number character varying(30),
    prepared_date timestamp without time zone,
    prepared_volume numeric,
    usable_date timestamp without time zone,
    expire_date timestamp without time zone
);


ALTER TABLE clinlims.qc OWNER TO clinlims;

--
-- Name: COLUMN qc.sys_user_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN qc.sys_user_id IS 'Sequential Identifier';


--
-- Name: COLUMN qc.name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN qc.name IS 'Descriptive QC name: Positive control for CHL';


--
-- Name: COLUMN qc.source; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN qc.source IS 'Name of supplier such as company or in-house';


--
-- Name: COLUMN qc.lot_number; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN qc.lot_number IS 'Lot number';


--
-- Name: COLUMN qc.prepared_date; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN qc.prepared_date IS 'Date QC was prepared';


--
-- Name: COLUMN qc.prepared_volume; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN qc.prepared_volume IS 'Amount prepared';


--
-- Name: COLUMN qc.usable_date; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN qc.usable_date IS 'Cannot be used before this date';


--
-- Name: COLUMN qc.expire_date; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN qc.expire_date IS 'Cannot be used after this date';


--
-- Name: qc_analytes; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE qc_analytes (
    id numeric(10,0) NOT NULL,
    qcanaly_type character varying(1),
    value character varying(80),
    analyte_id numeric(10,0)
);


ALTER TABLE clinlims.qc_analytes OWNER TO clinlims;

--
-- Name: COLUMN qc_analytes.qcanaly_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN qc_analytes.qcanaly_type IS 'Type of value: dictionary, titer range, number range, true value';


--
-- Name: COLUMN qc_analytes.value; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN qc_analytes.value IS 'Min max,, true value & % if type is any range';


--
-- Name: qrtz_blob_triggers; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE qrtz_blob_triggers (
    trigger_name character varying(80) NOT NULL,
    trigger_group character varying(80) NOT NULL,
    blob_data bytea
);


ALTER TABLE clinlims.qrtz_blob_triggers OWNER TO clinlims;

--
-- Name: qrtz_calendars; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE qrtz_calendars (
    calendar_name character varying(80) NOT NULL,
    calendar bytea NOT NULL
);


ALTER TABLE clinlims.qrtz_calendars OWNER TO clinlims;

--
-- Name: qrtz_cron_triggers; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE qrtz_cron_triggers (
    trigger_name character varying(80) NOT NULL,
    trigger_group character varying(80) NOT NULL,
    cron_expression character varying(80) NOT NULL,
    time_zone_id character varying(80)
);


ALTER TABLE clinlims.qrtz_cron_triggers OWNER TO clinlims;

--
-- Name: qrtz_fired_triggers; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE qrtz_fired_triggers (
    entry_id character varying(95) NOT NULL,
    trigger_name character varying(80) NOT NULL,
    trigger_group character varying(80) NOT NULL,
    is_volatile boolean NOT NULL,
    instance_name character varying(80) NOT NULL,
    fired_time bigint NOT NULL,
    priority integer NOT NULL,
    state character varying(16) NOT NULL,
    job_name character varying(80),
    job_group character varying(80),
    is_stateful boolean,
    requests_recovery boolean
);


ALTER TABLE clinlims.qrtz_fired_triggers OWNER TO clinlims;

--
-- Name: qrtz_job_details; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE qrtz_job_details (
    job_name character varying(80) NOT NULL,
    job_group character varying(80) NOT NULL,
    description character varying(120),
    job_class_name character varying(128) NOT NULL,
    is_durable boolean NOT NULL,
    is_volatile boolean NOT NULL,
    is_stateful boolean NOT NULL,
    requests_recovery boolean NOT NULL,
    job_data bytea
);


ALTER TABLE clinlims.qrtz_job_details OWNER TO clinlims;

--
-- Name: qrtz_job_listeners; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE qrtz_job_listeners (
    job_name character varying(80) NOT NULL,
    job_group character varying(80) NOT NULL,
    job_listener character varying(80) NOT NULL
);


ALTER TABLE clinlims.qrtz_job_listeners OWNER TO clinlims;

--
-- Name: qrtz_locks; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE qrtz_locks (
    lock_name character varying(40) NOT NULL
);


ALTER TABLE clinlims.qrtz_locks OWNER TO clinlims;

--
-- Name: qrtz_paused_trigger_grps; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE qrtz_paused_trigger_grps (
    trigger_group character varying(80) NOT NULL
);


ALTER TABLE clinlims.qrtz_paused_trigger_grps OWNER TO clinlims;

--
-- Name: qrtz_scheduler_state; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE qrtz_scheduler_state (
    instance_name character varying(80) NOT NULL,
    last_checkin_time bigint NOT NULL,
    checkin_interval bigint NOT NULL
);


ALTER TABLE clinlims.qrtz_scheduler_state OWNER TO clinlims;

--
-- Name: qrtz_simple_triggers; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE qrtz_simple_triggers (
    trigger_name character varying(80) NOT NULL,
    trigger_group character varying(80) NOT NULL,
    repeat_count bigint NOT NULL,
    repeat_interval bigint NOT NULL,
    times_triggered bigint NOT NULL
);


ALTER TABLE clinlims.qrtz_simple_triggers OWNER TO clinlims;

--
-- Name: qrtz_trigger_listeners; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE qrtz_trigger_listeners (
    trigger_name character varying(80) NOT NULL,
    trigger_group character varying(80) NOT NULL,
    trigger_listener character varying(80) NOT NULL
);


ALTER TABLE clinlims.qrtz_trigger_listeners OWNER TO clinlims;

--
-- Name: qrtz_triggers; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE qrtz_triggers (
    trigger_name character varying(80) NOT NULL,
    trigger_group character varying(80) NOT NULL,
    job_name character varying(80) NOT NULL,
    job_group character varying(80) NOT NULL,
    is_volatile boolean NOT NULL,
    description character varying(120),
    next_fire_time bigint,
    prev_fire_time bigint,
    priority integer,
    trigger_state character varying(16) NOT NULL,
    trigger_type character varying(8) NOT NULL,
    start_time bigint NOT NULL,
    end_time bigint,
    calendar_name character varying(80),
    misfire_instr smallint,
    job_data bytea
);


ALTER TABLE clinlims.qrtz_triggers OWNER TO clinlims;

--
-- Name: quartz_cron_scheduler; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE quartz_cron_scheduler (
    id numeric(10,0) NOT NULL,
    cron_statement character varying(32) DEFAULT 'never'::character varying NOT NULL,
    last_run timestamp with time zone,
    active boolean DEFAULT true,
    run_if_past boolean DEFAULT true,
    name character varying(40),
    job_name character varying(60) NOT NULL,
    display_key character varying(60),
    description_key character varying(60)
);


ALTER TABLE clinlims.quartz_cron_scheduler OWNER TO clinlims;

--
-- Name: TABLE quartz_cron_scheduler; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE quartz_cron_scheduler IS 'Sets up the quartz scheduler for cron jobs';


--
-- Name: COLUMN quartz_cron_scheduler.id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN quartz_cron_scheduler.id IS 'Id for this schedule';


--
-- Name: COLUMN quartz_cron_scheduler.cron_statement; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN quartz_cron_scheduler.cron_statement IS 'The cron statement for when the job should run. N.B. the default is not a valid cron expression';


--
-- Name: COLUMN quartz_cron_scheduler.last_run; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN quartz_cron_scheduler.last_run IS 'The last time this job was run';


--
-- Name: COLUMN quartz_cron_scheduler.active; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN quartz_cron_scheduler.active IS 'True if the schedule is active, false if it is suspended';


--
-- Name: COLUMN quartz_cron_scheduler.run_if_past; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN quartz_cron_scheduler.run_if_past IS 'True if the job should be run if the application is started after the run time and it has not run yet that day';


--
-- Name: COLUMN quartz_cron_scheduler.name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN quartz_cron_scheduler.name IS 'The name for this job';


--
-- Name: COLUMN quartz_cron_scheduler.job_name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN quartz_cron_scheduler.job_name IS 'The internal job name, should not be editible by end user';


--
-- Name: COLUMN quartz_cron_scheduler.display_key; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN quartz_cron_scheduler.display_key IS 'The localized name for this job';


--
-- Name: quartz_cron_scheduler_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE quartz_cron_scheduler_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.quartz_cron_scheduler_seq OWNER TO clinlims;

--
-- Name: race; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE race (
    id numeric(10,0) NOT NULL,
    description character varying(20) NOT NULL,
    race_type character varying(1),
    is_active character(1)
);


ALTER TABLE clinlims.race OWNER TO clinlims;

--
-- Name: receiver_code_element; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE receiver_code_element (
    id numeric(10,0) NOT NULL,
    identifier character varying(20),
    text character varying(60),
    code_system character varying(20),
    lastupdated timestamp(6) without time zone,
    message_org_id numeric(10,0),
    code_element_type_id numeric(10,0)
);


ALTER TABLE clinlims.receiver_code_element OWNER TO clinlims;

--
-- Name: receiver_code_element_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE receiver_code_element_seq
    START WITH 21
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;


ALTER TABLE clinlims.receiver_code_element_seq OWNER TO clinlims;

--
-- Name: reference_tables; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE reference_tables (
    id numeric(10,0) NOT NULL,
    name character varying(40),
    keep_history character varying(1),
    is_hl7_encoded character varying(1),
    lastupdated timestamp(6) without time zone
);


ALTER TABLE clinlims.reference_tables OWNER TO clinlims;

--
-- Name: COLUMN reference_tables.name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN reference_tables.name IS 'Name of table or module';


--
-- Name: reference_tables_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE reference_tables_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.reference_tables_seq OWNER TO clinlims;

--
-- Name: referral; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE referral (
    id numeric(10,0) NOT NULL,
    analysis_id numeric(10,0),
    organization_id numeric(10,0),
    organization_name character varying(30),
    send_ready_date timestamp with time zone,
    sent_date timestamp with time zone,
    result_recieved_date timestamp with time zone,
    referral_reason_id numeric(10,0),
    referral_type_id numeric(10,0) NOT NULL,
    requester_name character varying(60),
    lastupdated timestamp with time zone,
    canceled boolean DEFAULT false,
    referral_request_date timestamp with time zone
);


ALTER TABLE clinlims.referral OWNER TO clinlims;

--
-- Name: TABLE referral; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE referral IS 'Tracks referrals made to and from the lab.  If referrals should be attached to samples add another column for sample and edit this comment';


--
-- Name: COLUMN referral.analysis_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN referral.analysis_id IS 'The analysis which will be duplicated at other lab when refering out or which will be be done at this lab when referred in.  ';


--
-- Name: COLUMN referral.organization_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN referral.organization_id IS 'The organization the sample was sent to or from';


--
-- Name: COLUMN referral.organization_name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN referral.organization_name IS 'The organiztion the sample was sent to or from, if busness rules allow them not to be in the organization table';


--
-- Name: COLUMN referral.send_ready_date; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN referral.send_ready_date IS 'The date the referral out results are ready to be sent';


--
-- Name: COLUMN referral.sent_date; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN referral.sent_date IS 'The date the referral out results are actually sent';


--
-- Name: COLUMN referral.result_recieved_date; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN referral.result_recieved_date IS 'If this was a referral out then the date the results were recieved from the external lab';


--
-- Name: COLUMN referral.referral_reason_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN referral.referral_reason_id IS 'Why was this referral done';


--
-- Name: COLUMN referral.requester_name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN referral.requester_name IS 'The name of the person who requested that the referral be done';


--
-- Name: COLUMN referral.referral_request_date; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN referral.referral_request_date IS 'The date the referral request was made';


--
-- Name: referral_reason; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE referral_reason (
    id numeric(10,0) NOT NULL,
    name character varying(30) NOT NULL,
    description character varying(60),
    display_key character varying(60),
    lastupdated timestamp with time zone
);


ALTER TABLE clinlims.referral_reason OWNER TO clinlims;

--
-- Name: TABLE referral_reason; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE referral_reason IS 'The reason a referral was made to or from the lab';


--
-- Name: COLUMN referral_reason.name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN referral_reason.name IS 'The name of the reason, default value displayed to user if no display_key value';


--
-- Name: COLUMN referral_reason.description; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN referral_reason.description IS 'Clarification of the reason';


--
-- Name: COLUMN referral_reason.display_key; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN referral_reason.display_key IS 'Key for localization files to display locale appropriate reasons';


--
-- Name: referral_reason_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE referral_reason_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.referral_reason_seq OWNER TO clinlims;

--
-- Name: referral_result; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE referral_result (
    id numeric(10,0) NOT NULL,
    referral_id numeric(10,0) NOT NULL,
    test_id numeric(10,0),
    result_id numeric(10,0),
    referral_report_date timestamp with time zone,
    lastupdated timestamp with time zone
);


ALTER TABLE clinlims.referral_result OWNER TO clinlims;

--
-- Name: TABLE referral_result; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE referral_result IS 'A referral may have one or more results';


--
-- Name: COLUMN referral_result.referral_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN referral_result.referral_id IS 'The referral for which this is a result. May be one to many';


--
-- Name: COLUMN referral_result.test_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN referral_result.test_id IS 'When the referral lab reported the results back';


--
-- Name: COLUMN referral_result.result_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN referral_result.result_id IS 'The result returned by the referral lab';


--
-- Name: referral_result_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE referral_result_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.referral_result_seq OWNER TO clinlims;

--
-- Name: referral_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE referral_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.referral_seq OWNER TO clinlims;

--
-- Name: referral_type; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE referral_type (
    id numeric(10,0) NOT NULL,
    name character varying(30) NOT NULL,
    description character varying(60),
    display_key character varying(60),
    lastupdated timestamp with time zone
);


ALTER TABLE clinlims.referral_type OWNER TO clinlims;

--
-- Name: TABLE referral_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE referral_type IS 'The type of referral. i.e. a referral into the lab or a referral out of the lab';


--
-- Name: COLUMN referral_type.name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN referral_type.name IS 'The name of the type, default value displayed to user if no display_key value';


--
-- Name: COLUMN referral_type.description; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN referral_type.description IS 'Clarification of the type';


--
-- Name: COLUMN referral_type.display_key; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN referral_type.display_key IS 'Key for localization files to display locale appropriate types';


--
-- Name: referral_type_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE referral_type_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.referral_type_seq OWNER TO clinlims;

--
-- Name: region; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE region (
    id numeric(10,0) NOT NULL,
    region character varying(240) NOT NULL,
    lastupdated timestamp(6) without time zone
);


ALTER TABLE clinlims.region OWNER TO clinlims;

--
-- Name: COLUMN region.id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN region.id IS 'Sequential Number';


--
-- Name: COLUMN region.region; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN region.region IS 'Epidemiology Region Description used for MLS';


--
-- Name: region_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE region_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.region_seq OWNER TO clinlims;

--
-- Name: report_external_export; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE report_external_export (
    id numeric(10,0) NOT NULL,
    event_date timestamp with time zone NOT NULL,
    collection_date timestamp with time zone NOT NULL,
    sent_date timestamp with time zone,
    type numeric(10,0) NOT NULL,
    data text,
    lastupdated timestamp with time zone,
    send_flag boolean DEFAULT true,
    bookkeeping text
);


ALTER TABLE clinlims.report_external_export OWNER TO clinlims;

--
-- Name: TABLE report_external_export; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE report_external_export IS 'Table for holding aggregated results to be sent to an external application';


--
-- Name: COLUMN report_external_export.id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN report_external_export.id IS 'primary key';


--
-- Name: COLUMN report_external_export.event_date; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN report_external_export.event_date IS 'The date for which the information was collected.  Granularity assumed to be one day';


--
-- Name: COLUMN report_external_export.collection_date; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN report_external_export.collection_date IS 'The date on which the information was collected.';


--
-- Name: COLUMN report_external_export.sent_date; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN report_external_export.sent_date IS 'The date which the data was successfully sent';


--
-- Name: COLUMN report_external_export.type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN report_external_export.type IS 'The type of report this is';


--
-- Name: COLUMN report_external_export.data; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN report_external_export.data IS 'Formated data.  May be either JASON or xml.  Many datapoints per row';


--
-- Name: COLUMN report_external_export.lastupdated; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN report_external_export.lastupdated IS 'The last time the row was updated for any reason';


--
-- Name: COLUMN report_external_export.send_flag; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN report_external_export.send_flag IS 'The data is ready to be sent.  It may have already been sent once';


--
-- Name: COLUMN report_external_export.bookkeeping; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN report_external_export.bookkeeping IS 'Data which the application will need to record that this document has been sent';


--
-- Name: report_external_import; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE report_external_import (
    id numeric(10,0) NOT NULL,
    sending_site character varying(20) NOT NULL,
    event_date timestamp with time zone NOT NULL,
    recieved_date timestamp with time zone,
    type character varying(32) NOT NULL,
    updated_flag boolean DEFAULT false,
    data text,
    lastupdated timestamp with time zone
);


ALTER TABLE clinlims.report_external_import OWNER TO clinlims;

--
-- Name: TABLE report_external_import; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE report_external_import IS 'Table for holding aggregated results sent by an external application';


--
-- Name: COLUMN report_external_import.id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN report_external_import.id IS 'primary key';


--
-- Name: COLUMN report_external_import.sending_site; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN report_external_import.sending_site IS 'The site which is sending the info';


--
-- Name: COLUMN report_external_import.event_date; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN report_external_import.event_date IS 'The date for which the information was collected.  Granularity assumed to be one day';


--
-- Name: COLUMN report_external_import.recieved_date; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN report_external_import.recieved_date IS 'The date which the data was received';


--
-- Name: COLUMN report_external_import.type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN report_external_import.type IS 'The type of report this is';


--
-- Name: COLUMN report_external_import.data; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN report_external_import.data IS 'Formated data.  May be either JASON or xml.  Many datapoints per row';


--
-- Name: COLUMN report_external_import.lastupdated; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN report_external_import.lastupdated IS 'The last time the row was updated for any reason';


--
-- Name: report_external_import_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE report_external_import_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.report_external_import_seq OWNER TO clinlims;

--
-- Name: report_queue_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE report_queue_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.report_queue_seq OWNER TO clinlims;

--
-- Name: report_queue_type; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE report_queue_type (
    id numeric(10,0) NOT NULL,
    name character varying(32) NOT NULL,
    description character varying(60)
);


ALTER TABLE clinlims.report_queue_type OWNER TO clinlims;

--
-- Name: report_queue_type_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE report_queue_type_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.report_queue_type_seq OWNER TO clinlims;

--
-- Name: requester_type; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE requester_type (
    id numeric(10,0) NOT NULL,
    requester_type character varying(20) NOT NULL
);


ALTER TABLE clinlims.requester_type OWNER TO clinlims;

--
-- Name: TABLE requester_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE requester_type IS 'The types of entities which can request test.  This table will be used by sample_requester so the type should map to table';


--
-- Name: COLUMN requester_type.requester_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN requester_type.requester_type IS 'The type. i.e. organization or provider';


--
-- Name: requester_type_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE requester_type_seq
    START WITH 2
    INCREMENT BY 1
    MINVALUE 2
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.requester_type_seq OWNER TO clinlims;

--
-- Name: result; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE result (
    id numeric(10,0) NOT NULL,
    analysis_id numeric(10,0),
    sort_order numeric,
    is_reportable character varying(1),
    result_type character varying(1),
    value character varying(200),
    analyte_id numeric(10,0),
    test_result_id numeric(10,0),
    lastupdated timestamp(6) without time zone,
    min_normal double precision,
    max_normal double precision,
    parent_id numeric(10,0)
);


ALTER TABLE clinlims.result OWNER TO clinlims;

--
-- Name: COLUMN result.analysis_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN result.analysis_id IS 'Sequential number';


--
-- Name: COLUMN result.sort_order; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN result.sort_order IS 'The order the results are displayed (sort order)';


--
-- Name: COLUMN result.is_reportable; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN result.is_reportable IS 'Indicates if the result is reportable.';


--
-- Name: COLUMN result.result_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN result.result_type IS 'Type of result: Dictionary, titer, number, date';


--
-- Name: COLUMN result.value; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN result.value IS 'Actual result value.';


--
-- Name: COLUMN result.min_normal; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN result.min_normal IS 'The min normal value for this result. (May vary by patient sex and age)';


--
-- Name: COLUMN result.max_normal; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN result.max_normal IS 'The max normal value for this result. (May vary by patient sex and age)';


--
-- Name: COLUMN result.parent_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN result.parent_id IS 'The id of the result that this result is dependent on';


--
-- Name: result_inventory; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE result_inventory (
    id numeric(10,0) NOT NULL,
    inventory_location_id numeric(10,0) NOT NULL,
    result_id numeric(10,0) NOT NULL,
    description character varying(20),
    lastupdated timestamp(6) without time zone
);


ALTER TABLE clinlims.result_inventory OWNER TO clinlims;

--
-- Name: TABLE result_inventory; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE result_inventory IS 'Table to link analyte, inventory_items and results.  This is to tie a specific test kit to HIV or syphilis test result.';


--
-- Name: COLUMN result_inventory.inventory_location_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN result_inventory.inventory_location_id IS 'The specific identifiable inventory.  For Haiti this should be a test kit in inventory';


--
-- Name: COLUMN result_inventory.result_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN result_inventory.result_id IS 'The result which is tied to the inventory';


--
-- Name: COLUMN result_inventory.description; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN result_inventory.description IS 'The description of inventory the result refers to.';


--
-- Name: result_inventory_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE result_inventory_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.result_inventory_seq OWNER TO clinlims;

--
-- Name: result_limits; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE result_limits (
    id numeric(10,0) NOT NULL,
    test_id numeric(10,0) NOT NULL,
    test_result_type_id numeric NOT NULL,
    min_age double precision DEFAULT 0,
    max_age double precision DEFAULT 'Infinity'::double precision,
    gender character(1),
    low_normal double precision DEFAULT '-Infinity'::double precision,
    high_normal double precision DEFAULT 'Infinity'::double precision,
    low_valid double precision DEFAULT '-Infinity'::double precision,
    high_valid double precision DEFAULT 'Infinity'::double precision,
    lastupdated timestamp(6) without time zone,
    normal_dictionary_id numeric(10,0),
    always_validate boolean DEFAULT false
);


ALTER TABLE clinlims.result_limits OWNER TO clinlims;

--
-- Name: TABLE result_limits; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE result_limits IS 'This is a mainly read only table for normal and valid limits for given tests.  Currently it assumes that only age and gender matter.  If more criteria matter then refactor';


--
-- Name: COLUMN result_limits.test_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN result_limits.test_id IS 'Refers to the test table, this is additional information';


--
-- Name: COLUMN result_limits.test_result_type_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN result_limits.test_result_type_id IS 'The data type of the results';


--
-- Name: COLUMN result_limits.min_age; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN result_limits.min_age IS 'Should be null or less than max age';


--
-- Name: COLUMN result_limits.max_age; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN result_limits.max_age IS 'Should be null or greater than min age';


--
-- Name: COLUMN result_limits.gender; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN result_limits.gender IS 'Should be F or M or null if gender is not a criteria';


--
-- Name: COLUMN result_limits.low_normal; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN result_limits.low_normal IS 'Low end of normal range';


--
-- Name: COLUMN result_limits.high_normal; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN result_limits.high_normal IS 'High end of the normal range';


--
-- Name: COLUMN result_limits.low_valid; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN result_limits.low_valid IS 'Low end of the valid range, any result value lower should be considered suspect';


--
-- Name: COLUMN result_limits.high_valid; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN result_limits.high_valid IS 'high end of the valid range, any result value higher should be considered suspect';


--
-- Name: COLUMN result_limits.normal_dictionary_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN result_limits.normal_dictionary_id IS 'Reference to the dictionary value which is normal for test';


--
-- Name: COLUMN result_limits.always_validate; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN result_limits.always_validate IS 'Is further validation always required no matter what the results';


--
-- Name: result_limits_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE result_limits_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.result_limits_seq OWNER TO clinlims;

--
-- Name: result_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE result_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.result_seq OWNER TO clinlims;

--
-- Name: result_signature; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE result_signature (
    id numeric(10,0) NOT NULL,
    result_id numeric(10,0) NOT NULL,
    system_user_id numeric(10,0),
    is_supervisor boolean DEFAULT false NOT NULL,
    lastupdated timestamp(6) without time zone,
    non_user_name character varying(20)
);


ALTER TABLE clinlims.result_signature OWNER TO clinlims;

--
-- Name: TABLE result_signature; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE result_signature IS 'This matches the person who signed the result form with the result.';


--
-- Name: COLUMN result_signature.result_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN result_signature.result_id IS 'The result which is being signed';


--
-- Name: COLUMN result_signature.system_user_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN result_signature.system_user_id IS 'The signer of the result';


--
-- Name: COLUMN result_signature.is_supervisor; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN result_signature.is_supervisor IS 'Is the signer a supervisor';


--
-- Name: COLUMN result_signature.non_user_name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN result_signature.non_user_name IS 'For signers that are not systemUsers';


--
-- Name: result_signature_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE result_signature_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.result_signature_seq OWNER TO clinlims;

--
-- Name: sample; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE sample (
    id numeric(10,0) NOT NULL,
    accession_number character varying(20) NOT NULL,
    package_id numeric(10,0),
    domain character varying(1),
    next_item_sequence numeric(10,0),
    revision numeric,
    entered_date timestamp without time zone NOT NULL,
    received_date timestamp without time zone NOT NULL,
    collection_date timestamp without time zone,
    client_reference character varying(20),
    status character varying(1),
    released_date timestamp without time zone,
    sticker_rcvd_flag character varying(1),
    sys_user_id numeric(10,0),
    barcode character varying(20),
    transmission_date timestamp without time zone,
    lastupdated timestamp(6) without time zone,
    spec_or_isolate character varying(1),
    priority numeric(1,0),
    status_id numeric(10,0)
);


ALTER TABLE clinlims.sample OWNER TO clinlims;

--
-- Name: COLUMN sample.status_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample.status_id IS 'foriegn key to status of analysis ';


--
-- Name: sample_animal; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE sample_animal (
    id numeric(10,0) NOT NULL,
    sci_name_id numeric(10,0) NOT NULL,
    comm_anim_id numeric(10,0) NOT NULL,
    sampling_location character varying(40),
    collector character varying(40),
    samp_id numeric(10,0) NOT NULL,
    multiple_unit character varying(30),
    street_address character varying(30),
    city character varying(30),
    state character varying(2),
    country character varying(20),
    zip_code character varying(10)
);


ALTER TABLE clinlims.sample_animal OWNER TO clinlims;

--
-- Name: COLUMN sample_animal.sci_name_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_animal.sci_name_id IS 'Sequential Number';


--
-- Name: COLUMN sample_animal.sampling_location; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_animal.sampling_location IS 'Sampling location - name of farm';


--
-- Name: COLUMN sample_animal.collector; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_animal.collector IS 'Person collecting sample';


--
-- Name: COLUMN sample_animal.samp_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_animal.samp_id IS 'MDH Specimen Number';


--
-- Name: COLUMN sample_animal.street_address; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_animal.street_address IS 'Address of animal';


--
-- Name: sample_domain; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE sample_domain (
    id numeric(10,0) NOT NULL,
    domain_description character varying(20),
    domain character varying(1),
    lastupdated timestamp(6) without time zone
);


ALTER TABLE clinlims.sample_domain OWNER TO clinlims;

--
-- Name: COLUMN sample_domain.id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_domain.id IS 'A unique auto generated integer number assigned by the database.';


--
-- Name: COLUMN sample_domain.domain_description; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_domain.domain_description IS 'Type of sample this can be applied to:environ, human, animal, rabies, bt, newborn.';


--
-- Name: COLUMN sample_domain.domain; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_domain.domain IS 'Code for description: E-Environmental, A-Animal, C-Clinical';


--
-- Name: sample_domain_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE sample_domain_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.sample_domain_seq OWNER TO clinlims;

--
-- Name: sample_environmental; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE sample_environmental (
    id numeric(10,0) NOT NULL,
    samp_id numeric(10,0) NOT NULL,
    is_hazardous character varying(1),
    lot_nbr character varying(30),
    description character varying(40),
    chem_samp_num character varying(240),
    street_address character varying(30),
    multiple_unit character varying(30),
    city character varying(30),
    state character varying(2),
    zip_code character varying(10),
    country character varying(20),
    collector character varying(40),
    sampling_location character varying(40)
);


ALTER TABLE clinlims.sample_environmental OWNER TO clinlims;

--
-- Name: COLUMN sample_environmental.samp_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_environmental.samp_id IS 'MDH Specimen Number';


--
-- Name: COLUMN sample_environmental.lot_nbr; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_environmental.lot_nbr IS 'If sample is unopened package of food then include the lot number from the package';


--
-- Name: COLUMN sample_environmental.description; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_environmental.description IS 'Additional description field for sample attributes.';


--
-- Name: COLUMN sample_environmental.zip_code; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_environmental.zip_code IS 'Zip +4 code';


--
-- Name: COLUMN sample_environmental.collector; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_environmental.collector IS 'Person collecting the sample';


--
-- Name: COLUMN sample_environmental.sampling_location; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_environmental.sampling_location IS 'Sampling location - name of restaurant, store, farm';


--
-- Name: sample_human_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE sample_human_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.sample_human_seq OWNER TO clinlims;

--
-- Name: sample_item_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE sample_item_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.sample_item_seq OWNER TO clinlims;

--
-- Name: sample_newborn; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE sample_newborn (
    id numeric(10,0) NOT NULL,
    weight numeric(5,0),
    multi_birth character(1),
    birth_order numeric(2,0),
    gestational_week numeric(5,2),
    date_first_feeding date,
    breast character(1),
    tpn character(1),
    formula character(1),
    milk character(1),
    soy character(1),
    jaundice character(1),
    antibiotics character(1),
    transfused character(1),
    date_transfusion date,
    medical_record_numeric character varying(18),
    nicu character(1),
    birth_defect character(1),
    pregnancy_complication character(1),
    deceased_sibling character(1),
    cause_of_death character varying(50),
    family_history character(1),
    other character varying(100),
    y_numeric character varying(18),
    yellow_card character(1),
    lastupdated timestamp(6) without time zone
);


ALTER TABLE clinlims.sample_newborn OWNER TO clinlims;

--
-- Name: sample_org_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE sample_org_seq
    START WITH 112
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.sample_org_seq OWNER TO clinlims;

--
-- Name: sample_organization; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE sample_organization (
    id numeric(10,0) NOT NULL,
    org_id numeric(10,0),
    samp_id numeric(10,0),
    samp_org_type character varying(1),
    lastupdated timestamp(6) without time zone
);


ALTER TABLE clinlims.sample_organization OWNER TO clinlims;

--
-- Name: COLUMN sample_organization.org_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_organization.org_id IS 'Sequential Numbering Field';


--
-- Name: COLUMN sample_organization.samp_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_organization.samp_id IS 'MDH Specimen Number';


--
-- Name: COLUMN sample_organization.samp_org_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_organization.samp_org_type IS 'Type of organization: Primary, Secondary, Billing';


--
-- Name: sample_pdf; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE sample_pdf (
    id numeric(10,0) NOT NULL,
    accession_number numeric(10,0) NOT NULL,
    allow_view character varying(1),
    barcode character varying(20)
);


ALTER TABLE clinlims.sample_pdf OWNER TO clinlims;

--
-- Name: sample_pdf_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE sample_pdf_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;


ALTER TABLE clinlims.sample_pdf_seq OWNER TO clinlims;

--
-- Name: sample_proj_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE sample_proj_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.sample_proj_seq OWNER TO clinlims;

--
-- Name: sample_projects; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE sample_projects (
    samp_id numeric(10,0) NOT NULL,
    proj_id numeric(10,0),
    is_permanent character varying(1),
    id numeric(10,0) NOT NULL,
    lastupdated timestamp(6) without time zone
);


ALTER TABLE clinlims.sample_projects OWNER TO clinlims;

--
-- Name: COLUMN sample_projects.samp_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_projects.samp_id IS 'MDH Specimen Number';


--
-- Name: COLUMN sample_projects.proj_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_projects.proj_id IS 'Sequential number assigned by sequence';


--
-- Name: COLUMN sample_projects.is_permanent; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_projects.is_permanent IS 'Indicates if project is assigned to this sample permanently (Y/N)';


--
-- Name: sample_qaevent; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE sample_qaevent (
    id numeric(10,0) NOT NULL,
    qa_event_id numeric(10,0),
    sample_id numeric(10,0),
    completed_date date,
    lastupdated timestamp without time zone,
    sampleitem_id numeric(10,0),
    entered_date timestamp with time zone
);


ALTER TABLE clinlims.sample_qaevent OWNER TO clinlims;

--
-- Name: COLUMN sample_qaevent.sampleitem_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_qaevent.sampleitem_id IS 'If the qaevent refers to a sampleitem of the sample use this column';


--
-- Name: sample_qaevent_action; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE sample_qaevent_action (
    id numeric(10,0) NOT NULL,
    sample_qaevent_id numeric(10,0) NOT NULL,
    action_id numeric(10,0) NOT NULL,
    created_date date NOT NULL,
    lastupdated timestamp(6) without time zone,
    sys_user_id numeric(10,0)
);


ALTER TABLE clinlims.sample_qaevent_action OWNER TO clinlims;

--
-- Name: sample_qaevent_action_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE sample_qaevent_action_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.sample_qaevent_action_seq OWNER TO clinlims;

--
-- Name: sample_qaevent_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE sample_qaevent_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.sample_qaevent_seq OWNER TO clinlims;

--
-- Name: sample_requester; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE sample_requester (
    sample_id numeric(10,0) NOT NULL,
    requester_id numeric(10,0) NOT NULL,
    requester_type_id numeric(10,0) NOT NULL,
    lastupdated timestamp(6) without time zone
);


ALTER TABLE clinlims.sample_requester OWNER TO clinlims;

--
-- Name: TABLE sample_requester; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE sample_requester IS 'Links a sample to the entity which requested it';


--
-- Name: COLUMN sample_requester.sample_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_requester.sample_id IS 'The sample';


--
-- Name: COLUMN sample_requester.requester_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_requester.requester_id IS 'The requester_id.  The exact table row depends on the requester type';


--
-- Name: COLUMN sample_requester.requester_type_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN sample_requester.requester_type_id IS 'The type from the requester_type table';


--
-- Name: sample_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE sample_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.sample_seq OWNER TO clinlims;

--
-- Name: sample_type_panel_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE sample_type_panel_seq
    START WITH 0
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.sample_type_panel_seq OWNER TO clinlims;

--
-- Name: sample_type_test_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE sample_type_test_seq
    START WITH 0
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.sample_type_test_seq OWNER TO clinlims;

--
-- Name: source_of_sample; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE source_of_sample (
    id numeric(10,0) NOT NULL,
    description character varying(40),
    domain character varying(1),
    lastupdated timestamp(6) without time zone
);


ALTER TABLE clinlims.source_of_sample OWNER TO clinlims;

--
-- Name: COLUMN source_of_sample.id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN source_of_sample.id IS 'A unique auto generated integer number assigned by the database.';


--
-- Name: COLUMN source_of_sample.description; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN source_of_sample.description IS 'Description such as left ear, right hand, kitchen sink.';


--
-- Name: COLUMN source_of_sample.domain; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN source_of_sample.domain IS 'Type of sample this can be applied to: Environ, Animal, Clinical';


--
-- Name: type_of_sample; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE type_of_sample (
    id numeric(10,0) NOT NULL,
    description character varying(40) NOT NULL,
    domain character varying(1),
    lastupdated timestamp(6) without time zone,
    local_abbrev character varying(10),
    display_key character varying(60),
    is_active boolean DEFAULT true,
    sort_order numeric DEFAULT 2147483647
);


ALTER TABLE clinlims.type_of_sample OWNER TO clinlims;

--
-- Name: COLUMN type_of_sample.id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN type_of_sample.id IS 'A unique auto generated integer number assigned by the database';


--
-- Name: COLUMN type_of_sample.description; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN type_of_sample.description IS 'Description such as water, tissue, sludge, etc.';


--
-- Name: COLUMN type_of_sample.domain; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN type_of_sample.domain IS 'Type of sample this can be applied to : Environ, Animal, Clinical';


--
-- Name: COLUMN type_of_sample.display_key; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN type_of_sample.display_key IS 'Resource file lookup key for localization of displaying the name';


--
-- Name: sampletracking; Type: VIEW; Schema: clinlims; Owner: clinlims
--

CREATE VIEW sampletracking AS
    SELECT organizationinfo.accnum, patientinfo.patientid, organizationinfo.cliref, patientinfo.patientlastname, patientinfo.patientfirstname, patientinfo.dateofbirth, organizationinfo.orglocalabbrev AS org_local_abbrev, organizationinfo.orgname, organizationinfo.recddate, typeinfo.tosid, typeinfo.tosdesc, sourceinfo.sosid, sourceinfo.sosdesc, organizationinfo.colldate, sampleinfo.si AS sori FROM (SELECT s.accession_number AS accnum, p.last_name AS patientlastname, p.first_name AS patientfirstname, pt.external_id AS patientid, pt.birth_date AS dateofbirth FROM (((sample s LEFT JOIN sample_human samphum ON ((s.id = samphum.samp_id))) LEFT JOIN patient pt ON ((samphum.patient_id = pt.id))) LEFT JOIN person p ON ((pt.person_id = p.id)))) patientinfo, (SELECT s.accession_number AS accnum, sos.id AS sosid, sos.description AS sosdesc FROM ((sample s LEFT JOIN sample_item sampitem ON ((s.id = sampitem.samp_id))) LEFT JOIN source_of_sample sos ON ((sampitem.source_id = sos.id)))) sourceinfo, (SELECT s.accession_number AS accnum, tos.id AS tosid, tos.description AS tosdesc FROM ((sample s LEFT JOIN sample_item sampitem ON ((s.id = sampitem.samp_id))) LEFT JOIN type_of_sample tos ON ((sampitem.typeosamp_id = tos.id)))) typeinfo, (SELECT s.accession_number AS accnum, org.local_abbrev AS orglocalabbrev, org.name AS orgname, s.received_date AS recddate, s.collection_date AS colldate, s.client_reference AS cliref FROM ((sample s LEFT JOIN sample_organization samporg ON ((s.id = samporg.samp_id))) LEFT JOIN organization org ON ((samporg.org_id = org.id)))) organizationinfo, (SELECT s.accession_number AS accnum, s.spec_or_isolate AS si FROM sample s) sampleinfo WHERE (((((typeinfo.accnum)::text = (organizationinfo.accnum)::text) AND ((sourceinfo.accnum)::text = (organizationinfo.accnum)::text)) AND ((patientinfo.accnum)::text = (organizationinfo.accnum)::text)) AND ((sampleinfo.accnum)::text = (organizationinfo.accnum)::text)) ORDER BY organizationinfo.orglocalabbrev, organizationinfo.recddate, organizationinfo.colldate, typeinfo.tosdesc, sourceinfo.sosdesc;


ALTER TABLE clinlims.sampletracking OWNER TO clinlims;

--
-- Name: sampletype_panel; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE sampletype_panel (
    id numeric(10,0) NOT NULL,
    sample_type_id numeric(10,0) NOT NULL,
    panel_id numeric(10,0) NOT NULL
);


ALTER TABLE clinlims.sampletype_panel OWNER TO clinlims;

--
-- Name: sampletype_test; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE sampletype_test (
    id numeric(10,0) NOT NULL,
    sample_type_id numeric(10,0) NOT NULL,
    test_id numeric(10,0) NOT NULL,
    is_panel boolean DEFAULT false
);


ALTER TABLE clinlims.sampletype_test OWNER TO clinlims;

--
-- Name: scriptlet; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE scriptlet (
    id numeric(10,0) NOT NULL,
    name character varying(40),
    code_type character varying(1),
    code_source character varying(4000),
    lastupdated timestamp(6) without time zone
);


ALTER TABLE clinlims.scriptlet OWNER TO clinlims;

--
-- Name: COLUMN scriptlet.name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN scriptlet.name IS 'Script name';


--
-- Name: COLUMN scriptlet.code_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN scriptlet.code_type IS 'Flag indicating type of script code : Java, Basic, PLSQL';


--
-- Name: COLUMN scriptlet.lastupdated; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN scriptlet.lastupdated IS 'Body of Source Code';


--
-- Name: scriptlet_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE scriptlet_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.scriptlet_seq OWNER TO clinlims;

--
-- Name: sequence; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE sequence (
    seq_name character varying(30),
    seq_count numeric
);


ALTER TABLE clinlims.sequence OWNER TO clinlims;

--
-- Name: site_information; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE site_information (
    id integer NOT NULL,
    name character varying(32) NOT NULL,
    lastupdated timestamp with time zone,
    description character varying(120),
    value character varying(200),
    encrypted boolean DEFAULT false,
    domain_id numeric(10,0),
    value_type character varying(10) DEFAULT 'text'::character varying NOT NULL,
    instruction_key character varying(40),
    "group" numeric DEFAULT (0)::numeric,
    schedule_id numeric(10,0),
    tag character varying(20),
    dictionary_category_id numeric(10,0)
);


ALTER TABLE clinlims.site_information OWNER TO clinlims;

--
-- Name: TABLE site_information; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE site_information IS 'Information about a specific installation at a site, seperate from an implimentation';


--
-- Name: COLUMN site_information.name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN site_information.name IS 'Name by which this information will be found';


--
-- Name: COLUMN site_information.description; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN site_information.description IS 'Clarification of the name';


--
-- Name: COLUMN site_information.value; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN site_information.value IS 'Value for the named information';


--
-- Name: COLUMN site_information.encrypted; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN site_information.encrypted IS 'Is the value an encrypted value.  Used for passwords';


--
-- Name: COLUMN site_information.value_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN site_information.value_type IS 'The type of value which can be specified for the value. Currently either ''boolean'',''text'' or ''dictionary''';


--
-- Name: COLUMN site_information.instruction_key; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN site_information.instruction_key IS 'The key in Message_Resource which give the user the text for the meaning and consequences of the information';


--
-- Name: COLUMN site_information."group"; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN site_information."group" IS 'If items should be grouped together when displaying they should have the same group number';


--
-- Name: COLUMN site_information.schedule_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN site_information.schedule_id IS 'quartz_cron_scheduler id if the item is associated with a scheduler ';


--
-- Name: COLUMN site_information.tag; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN site_information.tag IS 'A tag to help determine how the information should be used';


--
-- Name: COLUMN site_information.dictionary_category_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN site_information.dictionary_category_id IS 'Value of the dictionary category if the type of record is dictionary';


--
-- Name: site_information_domain; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE site_information_domain (
    id numeric(10,0) NOT NULL,
    name character varying(20) NOT NULL,
    description character varying(120)
);


ALTER TABLE clinlims.site_information_domain OWNER TO clinlims;

--
-- Name: TABLE site_information_domain; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE site_information_domain IS 'Marks the domains to which site information belongs.  Intended use is administration pages';


--
-- Name: site_information_domain_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE site_information_domain_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.site_information_domain_seq OWNER TO clinlims;

--
-- Name: site_information_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE site_information_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.site_information_seq OWNER TO clinlims;

--
-- Name: source_of_sample_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE source_of_sample_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.source_of_sample_seq OWNER TO clinlims;

--
-- Name: state_code; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE state_code (
    id numeric(10,0) NOT NULL,
    code character varying(240),
    description character varying(240),
    lastupdated timestamp(6) without time zone
);


ALTER TABLE clinlims.state_code OWNER TO clinlims;

--
-- Name: COLUMN state_code.code; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN state_code.code IS 'State abbreviation';


--
-- Name: COLUMN state_code.description; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN state_code.description IS 'State Name';


--
-- Name: state_code_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE state_code_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.state_code_seq OWNER TO clinlims;

--
-- Name: status_of_sample_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE status_of_sample_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.status_of_sample_seq OWNER TO clinlims;

--
-- Name: storage_location; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE storage_location (
    id numeric(10,0) NOT NULL,
    sort_order numeric,
    name character varying(20),
    location character varying(80),
    is_available character varying(1),
    parent_storageloc_id numeric(10,0),
    storage_unit_id numeric(10,0)
);


ALTER TABLE clinlims.storage_location OWNER TO clinlims;

--
-- Name: COLUMN storage_location.sort_order; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN storage_location.sort_order IS 'The sequence order of this item; sort order used for display';


--
-- Name: COLUMN storage_location.name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN storage_location.name IS 'Name of unit: Virology Fridge #1';


--
-- Name: COLUMN storage_location.location; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN storage_location.location IS 'Location of storage';


--
-- Name: COLUMN storage_location.is_available; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN storage_location.is_available IS 'Indicates if storage is available for use.';


--
-- Name: storage_unit; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE storage_unit (
    id numeric(10,0) NOT NULL,
    category character varying(15),
    description character varying(60),
    is_singular character varying(1)
);


ALTER TABLE clinlims.storage_unit OWNER TO clinlims;

--
-- Name: COLUMN storage_unit.category; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN storage_unit.category IS 'type of storage unit: box, fridge, shelf, tube';


--
-- Name: COLUMN storage_unit.description; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN storage_unit.description IS 'Description of unit: 10 mL tube, 5 shelf fridge.';


--
-- Name: COLUMN storage_unit.is_singular; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN storage_unit.is_singular IS 'Y, N flag indicating if this unit can contain more than one item.';


--
-- Name: system_module; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE system_module (
    id numeric(10,0) NOT NULL,
    name character varying(40),
    description character varying(80),
    has_select_flag character varying(1),
    has_add_flag character varying(1),
    has_update_flag character varying(1),
    has_delete_flag character varying(1)
);


ALTER TABLE clinlims.system_module OWNER TO clinlims;

--
-- Name: COLUMN system_module.name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN system_module.name IS 'Name of security module';


--
-- Name: COLUMN system_module.description; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN system_module.description IS 'Description for this security module';


--
-- Name: COLUMN system_module.has_select_flag; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN system_module.has_select_flag IS 'Flag indicating if this module can be assigned to a user';


--
-- Name: COLUMN system_module.has_add_flag; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN system_module.has_add_flag IS 'Flag indicating if this module has add capability';


--
-- Name: COLUMN system_module.has_update_flag; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN system_module.has_update_flag IS 'Flag indicating if this module has update capability';


--
-- Name: COLUMN system_module.has_delete_flag; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN system_module.has_delete_flag IS 'Flag indicating if this module has delete capability';


--
-- Name: system_module_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE system_module_seq
    START WITH 0
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.system_module_seq OWNER TO clinlims;

--
-- Name: system_role; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE system_role (
    id numeric(10,0) NOT NULL,
    name character(20) NOT NULL,
    description character varying(80),
    is_grouping_role boolean DEFAULT false,
    grouping_parent numeric(10,0),
    display_key character varying(60),
    active boolean DEFAULT true,
    editable boolean DEFAULT false
);


ALTER TABLE clinlims.system_role OWNER TO clinlims;

--
-- Name: TABLE system_role; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE system_role IS 'Describes the roles a user may have.  ';


--
-- Name: COLUMN system_role.name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN system_role.name IS 'The name of the role, this is how it will appear to the user';


--
-- Name: COLUMN system_role.description; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN system_role.description IS 'Notes about the role';


--
-- Name: COLUMN system_role.is_grouping_role; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN system_role.is_grouping_role IS 'Indicates that this role is only for grouping other roles.  It should not have modules assigned to it';


--
-- Name: COLUMN system_role.grouping_parent; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN system_role.grouping_parent IS 'Should only refer to a grouping role';


--
-- Name: COLUMN system_role.display_key; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN system_role.display_key IS 'key for localizing dropdown lists';


--
-- Name: COLUMN system_role.active; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN system_role.active IS 'Is this role active for this installation';


--
-- Name: COLUMN system_role.editable; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN system_role.editable IS 'Is this a role that can be de/activated by the user';


--
-- Name: system_role_module; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE system_role_module (
    id numeric(10,0) NOT NULL,
    has_select character varying(1),
    has_add character varying(1),
    has_update character varying(1),
    has_delete character varying(1),
    system_role_id numeric(10,0) NOT NULL,
    system_module_id numeric(10,0) NOT NULL
);


ALTER TABLE clinlims.system_role_module OWNER TO clinlims;

--
-- Name: system_role_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE system_role_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.system_role_seq OWNER TO clinlims;

--
-- Name: system_user; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE system_user (
    id numeric(10,0) NOT NULL,
    external_id character varying(80),
    login_name character varying(20) NOT NULL,
    last_name character varying(30) NOT NULL,
    first_name character varying(20) NOT NULL,
    initials character varying(3),
    is_active character varying(1) NOT NULL,
    is_employee character varying(1) NOT NULL,
    lastupdated timestamp(6) without time zone
);


ALTER TABLE clinlims.system_user OWNER TO clinlims;

--
-- Name: COLUMN system_user.id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN system_user.id IS 'Sequential Identifier';


--
-- Name: COLUMN system_user.external_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN system_user.external_id IS 'External ID such as employee number or external system ID.';


--
-- Name: COLUMN system_user.login_name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN system_user.login_name IS 'User''s system log in name.';


--
-- Name: COLUMN system_user.last_name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN system_user.last_name IS 'Last name of person';


--
-- Name: COLUMN system_user.first_name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN system_user.first_name IS 'Person Name';


--
-- Name: COLUMN system_user.initials; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN system_user.initials IS 'Middle Initial';


--
-- Name: COLUMN system_user.is_active; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN system_user.is_active IS 'Indicates the status of active or inactive for user';


--
-- Name: COLUMN system_user.is_employee; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN system_user.is_employee IS 'Indicates if user is an MDH employee';


--
-- Name: system_user_module; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE system_user_module (
    id numeric(10,0) NOT NULL,
    has_select character varying(1),
    has_add character varying(1),
    has_update character varying(1),
    has_delete character varying(1),
    system_user_id numeric(10,0),
    system_module_id numeric(10,0)
);


ALTER TABLE clinlims.system_user_module OWNER TO clinlims;

--
-- Name: COLUMN system_user_module.has_select; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN system_user_module.has_select IS 'Flag indicating if this user has permission to enter this module';


--
-- Name: COLUMN system_user_module.has_add; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN system_user_module.has_add IS 'Flag indicating if this user has permission to add a record';


--
-- Name: COLUMN system_user_module.has_update; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN system_user_module.has_update IS 'Flag indicating if this person has permission to update a record';


--
-- Name: COLUMN system_user_module.has_delete; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN system_user_module.has_delete IS 'Flag indicating if this person has permission to remove a record';


--
-- Name: system_user_module_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE system_user_module_seq
    START WITH 0
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.system_user_module_seq OWNER TO clinlims;

--
-- Name: system_user_role; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE system_user_role (
    system_user_id numeric(10,0) NOT NULL,
    role_id numeric(10,0) NOT NULL
);


ALTER TABLE clinlims.system_user_role OWNER TO clinlims;

--
-- Name: system_user_section; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE system_user_section (
    id numeric NOT NULL,
    has_view character varying(1),
    has_assign character varying(1),
    has_complete character varying(1),
    has_release character varying(1),
    has_cancel character varying(1),
    system_user_id numeric(10,0),
    test_section_id numeric(10,0)
);


ALTER TABLE clinlims.system_user_section OWNER TO clinlims;

--
-- Name: COLUMN system_user_section.has_view; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN system_user_section.has_view IS 'Flag indicating if user has permission to iew this sections''s records';


--
-- Name: COLUMN system_user_section.has_assign; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN system_user_section.has_assign IS 'Flag indicating if user has permission to assign this section''s tests';


--
-- Name: COLUMN system_user_section.has_complete; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN system_user_section.has_complete IS 'Flag indicating if user has permission to complete this section''s tests';


--
-- Name: COLUMN system_user_section.has_release; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN system_user_section.has_release IS 'Flag indicating if user has permission to release this section''s tests';


--
-- Name: COLUMN system_user_section.has_cancel; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN system_user_section.has_cancel IS 'Flag indicating if user has permission to cancel this section''s tests';


--
-- Name: system_user_section_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE system_user_section_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.system_user_section_seq OWNER TO clinlims;

--
-- Name: system_user_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE system_user_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.system_user_seq OWNER TO clinlims;

--
-- Name: test_analyte; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE test_analyte (
    id numeric(10,0) NOT NULL,
    test_id numeric(10,0),
    analyte_id numeric(10,0),
    result_group numeric,
    sort_order numeric,
    testalyt_type character varying(1),
    lastupdated timestamp(6) without time zone,
    is_reportable character varying(1)
);


ALTER TABLE clinlims.test_analyte OWNER TO clinlims;

--
-- Name: COLUMN test_analyte.test_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test_analyte.test_id IS 'Sequential value assigned on insert';


--
-- Name: COLUMN test_analyte.result_group; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test_analyte.result_group IS 'A program generated group number';


--
-- Name: COLUMN test_analyte.sort_order; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test_analyte.sort_order IS 'The order in which the analytes are displayed (sort order)';


--
-- Name: COLUMN test_analyte.testalyt_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test_analyte.testalyt_type IS 'Type of analyte: required...';


--
-- Name: test_analyte_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE test_analyte_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.test_analyte_seq OWNER TO clinlims;

--
-- Name: test_code; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE test_code (
    test_id numeric(10,0) NOT NULL,
    code_type_id numeric(10,0) NOT NULL,
    value character varying(20) NOT NULL,
    lastupdated timestamp with time zone
);


ALTER TABLE clinlims.test_code OWNER TO clinlims;

--
-- Name: TABLE test_code; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE test_code IS 'For a given test and schema it gives the encoding';


--
-- Name: COLUMN test_code.test_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test_code.test_id IS 'The test for which the coding supports. FK to test table.';


--
-- Name: COLUMN test_code.code_type_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test_code.code_type_id IS 'The coding type id of the code';


--
-- Name: COLUMN test_code.value; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test_code.value IS 'The actual code';


--
-- Name: test_code_type; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE test_code_type (
    id numeric(10,0) NOT NULL,
    schema_name character varying(32) NOT NULL,
    lastupdated timestamp with time zone
);


ALTER TABLE clinlims.test_code_type OWNER TO clinlims;

--
-- Name: TABLE test_code_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON TABLE test_code_type IS 'The names of the encoding schems supported (SNOMWD, LOINC etc)';


--
-- Name: test_formats; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE test_formats (
    id numeric(10,0) NOT NULL,
    lastupdated timestamp(6) without time zone
);


ALTER TABLE clinlims.test_formats OWNER TO clinlims;

--
-- Name: test_reflex; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE test_reflex (
    id numeric(10,0) NOT NULL,
    tst_rslt_id numeric,
    flags character varying(10),
    lastupdated timestamp(6) without time zone,
    test_analyte_id numeric(10,0),
    test_id numeric(10,0),
    add_test_id numeric(10,0),
    sibling_reflex numeric(10,0),
    scriptlet_id numeric(10,0)
);


ALTER TABLE clinlims.test_reflex OWNER TO clinlims;

--
-- Name: COLUMN test_reflex.flags; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test_reflex.flags IS 'A string of 1 character codes: duplicate, auto-add';


--
-- Name: COLUMN test_reflex.sibling_reflex; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test_reflex.sibling_reflex IS 'Reference to tests and results for reflexes with more than one condition.  All add_test_ids should be the same';


--
-- Name: COLUMN test_reflex.scriptlet_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test_reflex.scriptlet_id IS 'If a non-test action should be taken then reference the scriptlet which says what to do';


--
-- Name: test_reflex_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE test_reflex_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.test_reflex_seq OWNER TO clinlims;

--
-- Name: test_result; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE test_result (
    id numeric NOT NULL,
    test_id numeric(10,0) NOT NULL,
    result_group numeric,
    flags character varying(10),
    tst_rslt_type character varying(1),
    value character varying(80),
    significant_digits numeric DEFAULT 0,
    quant_limit character varying(30),
    cont_level character varying(30),
    lastupdated timestamp(6) without time zone,
    scriptlet_id numeric(10,0),
    sort_order numeric(22,0)
);


ALTER TABLE clinlims.test_result OWNER TO clinlims;

--
-- Name: COLUMN test_result.test_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test_result.test_id IS 'Sequential value assigned on insert';


--
-- Name: COLUMN test_result.result_group; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test_result.result_group IS 'the test_analyte result_group number';


--
-- Name: COLUMN test_result.flags; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test_result.flags IS 'a string of 1 character codes: Positive, Reportable...';


--
-- Name: COLUMN test_result.tst_rslt_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test_result.tst_rslt_type IS 'Type of parameter: Dictionary, Titer Range, Number Range, Date';


--
-- Name: COLUMN test_result.value; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test_result.value IS 'Possible result value based on type';


--
-- Name: COLUMN test_result.significant_digits; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test_result.significant_digits IS 'Number of decimal digits';


--
-- Name: COLUMN test_result.quant_limit; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test_result.quant_limit IS 'Quantitation Limit (if any)';


--
-- Name: COLUMN test_result.cont_level; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test_result.cont_level IS 'Contamination Level (if any)';


--
-- Name: test_result_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE test_result_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.test_result_seq OWNER TO clinlims;

--
-- Name: test_section_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE test_section_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;


ALTER TABLE clinlims.test_section_seq OWNER TO clinlims;

--
-- Name: test_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE test_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.test_seq OWNER TO clinlims;

--
-- Name: test_trailer; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE test_trailer (
    id numeric(10,0) NOT NULL,
    name character varying(20),
    description character varying(60),
    text character varying(4000),
    lastupdated timestamp(6) without time zone
);


ALTER TABLE clinlims.test_trailer OWNER TO clinlims;

--
-- Name: test_trailer_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE test_trailer_seq
    START WITH 2
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.test_trailer_seq OWNER TO clinlims;

--
-- Name: test_worksheet_item; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE test_worksheet_item (
    id numeric(10,0) NOT NULL,
    tw_id numeric(10,0) NOT NULL,
    qc_id numeric,
    "position" numeric,
    cell_type character varying(2)
);


ALTER TABLE clinlims.test_worksheet_item OWNER TO clinlims;

--
-- Name: COLUMN test_worksheet_item."position"; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test_worksheet_item."position" IS 'Well location or position within the batch';


--
-- Name: COLUMN test_worksheet_item.cell_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test_worksheet_item.cell_type IS 'Cell/position type: First, random, duplicate last, last run';


--
-- Name: test_worksheets; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE test_worksheets (
    id numeric(10,0) NOT NULL,
    test_id numeric(10,0),
    batch_capacity numeric,
    total_capacity numeric,
    number_format character varying(1)
);


ALTER TABLE clinlims.test_worksheets OWNER TO clinlims;

--
-- Name: COLUMN test_worksheets.test_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test_worksheets.test_id IS 'Sequential value assigned on insert';


--
-- Name: COLUMN test_worksheets.batch_capacity; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test_worksheets.batch_capacity IS 'number of samples (including QC) per batch/plate';


--
-- Name: COLUMN test_worksheets.total_capacity; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test_worksheets.total_capacity IS 'Number of samples (including QC) per worksheet';


--
-- Name: COLUMN test_worksheets.number_format; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN test_worksheets.number_format IS 'Specifies the numbering scheme for worksheet cell';


--
-- Name: tobereomved_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE tobereomved_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.tobereomved_seq OWNER TO clinlims;

--
-- Name: type_of_provider; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE type_of_provider (
    id numeric(10,0) NOT NULL,
    description character varying(240),
    tp_code character varying(1)
);


ALTER TABLE clinlims.type_of_provider OWNER TO clinlims;

--
-- Name: COLUMN type_of_provider.id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN type_of_provider.id IS 'A unique auto generated integer number assigned by the database.';


--
-- Name: COLUMN type_of_provider.description; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN type_of_provider.description IS 'Description such as doctor, nurse, clinician, veteranarian.';


--
-- Name: type_of_sample_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE type_of_sample_seq
    START WITH 80
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.type_of_sample_seq OWNER TO clinlims;

--
-- Name: type_of_test_result; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE type_of_test_result (
    id numeric(10,0) NOT NULL,
    test_result_type character varying(1),
    description character varying(20),
    lastupdated timestamp(6) without time zone,
    hl7_value character varying(20)
);


ALTER TABLE clinlims.type_of_test_result OWNER TO clinlims;

--
-- Name: COLUMN type_of_test_result.id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN type_of_test_result.id IS 'A unique auto generated integer number assigned by database';


--
-- Name: COLUMN type_of_test_result.test_result_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN type_of_test_result.test_result_type IS 'Test Result Type (T, N, D)';


--
-- Name: COLUMN type_of_test_result.description; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN type_of_test_result.description IS 'Human readable description';


--
-- Name: type_of_test_result_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE type_of_test_result_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.type_of_test_result_seq OWNER TO clinlims;

--
-- Name: unit_of_measure; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE unit_of_measure (
    id numeric(10,0) NOT NULL,
    name character varying(20),
    description character varying(60),
    lastupdated timestamp(6) without time zone
);


ALTER TABLE clinlims.unit_of_measure OWNER TO clinlims;

--
-- Name: COLUMN unit_of_measure.name; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN unit_of_measure.name IS 'Name of Unit';


--
-- Name: COLUMN unit_of_measure.description; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN unit_of_measure.description IS 'Description of Unit';


--
-- Name: unit_of_measure_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE unit_of_measure_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.unit_of_measure_seq OWNER TO clinlims;

--
-- Name: user_alert_map; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE user_alert_map (
    user_id integer NOT NULL,
    alert_id integer,
    report_id integer,
    alert_limit integer,
    alert_operator character varying(255),
    map_id integer NOT NULL
);


ALTER TABLE clinlims.user_alert_map OWNER TO clinlims;

--
-- Name: user_group_map; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE user_group_map (
    user_id integer NOT NULL,
    group_id integer NOT NULL,
    map_id integer NOT NULL
);


ALTER TABLE clinlims.user_group_map OWNER TO clinlims;

--
-- Name: user_security; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE user_security (
    user_id integer NOT NULL,
    role_name character varying(255)
);


ALTER TABLE clinlims.user_security OWNER TO clinlims;

--
-- Name: worksheet_analysis; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE worksheet_analysis (
    id numeric(10,0) NOT NULL,
    reference_type character(1),
    reference_id numeric,
    worksheet_item_id numeric(10,0)
);


ALTER TABLE clinlims.worksheet_analysis OWNER TO clinlims;

--
-- Name: COLUMN worksheet_analysis.reference_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN worksheet_analysis.reference_type IS 'Flag indicating if reference points to analysis or QC';


--
-- Name: COLUMN worksheet_analysis.reference_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN worksheet_analysis.reference_id IS 'ID of analysis or QC';


--
-- Name: worksheet_analyte; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE worksheet_analyte (
    id numeric NOT NULL,
    wrkst_anls_id numeric(10,0),
    sort_order numeric,
    result_id numeric(10,0)
);


ALTER TABLE clinlims.worksheet_analyte OWNER TO clinlims;

--
-- Name: COLUMN worksheet_analyte.sort_order; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN worksheet_analyte.sort_order IS 'The order worksheet analytes are displayed (sort order)';


--
-- Name: worksheet_heading; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE worksheet_heading (
    id numeric(10,0),
    worksheet_name character varying(20),
    rownumber numeric(10,0),
    column1 character varying(50),
    column2 character varying(50),
    column3 character varying(50),
    column4 character varying(50),
    column5 character varying(50),
    column6 character varying(50),
    column7 character varying(50),
    column8 character varying(50),
    column9 character varying(50),
    column10 character varying(50),
    type character varying(20)
);


ALTER TABLE clinlims.worksheet_heading OWNER TO clinlims;

--
-- Name: worksheet_item; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE worksheet_item (
    id numeric(10,0) NOT NULL,
    "position" numeric,
    worksheet_id numeric(10,0)
);


ALTER TABLE clinlims.worksheet_item OWNER TO clinlims;

--
-- Name: worksheet_qc; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE worksheet_qc (
    id numeric NOT NULL,
    sort_order numeric,
    wq_type character varying(1),
    value character varying(80),
    worksheet_analysis_id numeric(10,0),
    qc_analyte_id numeric(10,0)
);


ALTER TABLE clinlims.worksheet_qc OWNER TO clinlims;

--
-- Name: COLUMN worksheet_qc.sort_order; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN worksheet_qc.sort_order IS 'The order worksheet analystes are displayed (sort order)';


--
-- Name: COLUMN worksheet_qc.wq_type; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN worksheet_qc.wq_type IS 'Type of result: dictionary, titer, number...';


--
-- Name: COLUMN worksheet_qc.value; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN worksheet_qc.value IS 'Result value';


--
-- Name: worksheets; Type: TABLE; Schema: clinlims; Owner: clinlims
--

CREATE TABLE worksheets (
    id numeric(10,0) NOT NULL,
    sys_user_id numeric(10,0) NOT NULL,
    test_id numeric(10,0),
    created timestamp without time zone,
    status character varying(1),
    number_format character varying(1)
);


ALTER TABLE clinlims.worksheets OWNER TO clinlims;

--
-- Name: COLUMN worksheets.sys_user_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN worksheets.sys_user_id IS 'Sequential Identifier';


--
-- Name: COLUMN worksheets.test_id; Type: COMMENT; Schema: clinlims; Owner: clinlims
--

COMMENT ON COLUMN worksheets.test_id IS 'Sequential value assigned on insert';


--
-- Name: zip_code_seq; Type: SEQUENCE; Schema: clinlims; Owner: clinlims
--

CREATE SEQUENCE zip_code_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clinlims.zip_code_seq OWNER TO clinlims;

ALTER TABLE ONLY address_part
    ADD CONSTRAINT address_part_part_name_key UNIQUE (part_name);


--
-- Name: address_part_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY address_part
    ADD CONSTRAINT address_part_pk PRIMARY KEY (id);


--
-- Name: analysis_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY analysis
    ADD CONSTRAINT analysis_pk PRIMARY KEY (id);


--
-- Name: analyte_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY analyte
    ADD CONSTRAINT analyte_pk PRIMARY KEY (id);


--
-- Name: analyzer_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY analyzer
    ADD CONSTRAINT analyzer_pk PRIMARY KEY (id);


--
-- Name: analyzer_result_status_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY analyzer_result_status
    ADD CONSTRAINT analyzer_result_status_pk PRIMARY KEY (id);


--
-- Name: analyzer_test_map_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY analyzer_test_map
    ADD CONSTRAINT analyzer_test_map_pk PRIMARY KEY (analyzer_id, analyzer_test_name);


--
-- Name: anauser_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY analysis_users
    ADD CONSTRAINT anauser_pk PRIMARY KEY (id);


--
-- Name: ani_samp_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample_animal
    ADD CONSTRAINT ani_samp_pk PRIMARY KEY (id);


--
-- Name: anqaev_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY analysis_qaevent
    ADD CONSTRAINT anqaev_pk PRIMARY KEY (id);


--
-- Name: anstore_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY analysis_storages
    ADD CONSTRAINT anstore_pk PRIMARY KEY (id);


--
-- Name: attachment_item_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY attachment_item
    ADD CONSTRAINT attachment_item_pk PRIMARY KEY (id);


--
-- Name: attachment_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY attachment
    ADD CONSTRAINT attachment_pk PRIMARY KEY (id);


--
-- Name: auxdata_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY aux_data
    ADD CONSTRAINT auxdata_pk PRIMARY KEY (id);


--
-- Name: auxfld_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY aux_field
    ADD CONSTRAINT auxfld_pk PRIMARY KEY (id);


--
-- Name: auxfldval_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY aux_field_values
    ADD CONSTRAINT auxfldval_pk PRIMARY KEY (id);


--
-- Name: code_element_type_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY code_element_type
    ADD CONSTRAINT code_element_type_pk PRIMARY KEY (id);


--
-- Name: code_element_xref_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY code_element_xref
    ADD CONSTRAINT code_element_xref_pk PRIMARY KEY (id);


--
-- Name: comm_anim_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY animal_common_name
    ADD CONSTRAINT comm_anim_pk PRIMARY KEY (id);


--
-- Name: cron_scheduler_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY quartz_cron_scheduler
    ADD CONSTRAINT cron_scheduler_pk PRIMARY KEY (id);


--
-- Name: ct_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY contact_type
    ADD CONSTRAINT ct_pk PRIMARY KEY (id);


--
-- Name: demog_hist_type_type_name_u; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY observation_history_type
    ADD CONSTRAINT demog_hist_type_type_name_u UNIQUE (type_name);


--
-- Name: demographic_history_type_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY observation_history_type
    ADD CONSTRAINT demographic_history_type_pk PRIMARY KEY (id);


--
-- Name: demographics_history_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY observation_history
    ADD CONSTRAINT demographics_history_pk PRIMARY KEY (id);


--
-- Name: dict_cat_desc_u; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY dictionary_category
    ADD CONSTRAINT dict_cat_desc_u UNIQUE (description);


--
-- Name: dict_cat_local_abbrev_u; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY dictionary_category
    ADD CONSTRAINT dict_cat_local_abbrev_u UNIQUE (local_abbrev);


--
-- Name: dict_dict_cat_id_dict_entry_u; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY dictionary
    ADD CONSTRAINT dict_dict_cat_id_dict_entry_u UNIQUE (dictionary_category_id, dict_entry);


--
-- Name: dict_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY dictionary
    ADD CONSTRAINT dict_pk PRIMARY KEY (id);


--
-- Name: dictionary_category_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY dictionary_category
    ADD CONSTRAINT dictionary_category_pk PRIMARY KEY (id);


--
-- Name: dist_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY district
    ADD CONSTRAINT dist_pk PRIMARY KEY (id);


--
-- Name: env_samp_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample_environmental
    ADD CONSTRAINT env_samp_pk PRIMARY KEY (id);


--
-- Name: ethnic_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY ethnicity
    ADD CONSTRAINT ethnic_pk PRIMARY KEY (id);


--
-- Name: gender_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY gender
    ADD CONSTRAINT gender_pk PRIMARY KEY (id);


--
-- Name: hist_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY history
    ADD CONSTRAINT hist_pk PRIMARY KEY (id);


--
-- Name: hl7_encoding_type_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test_code_type
    ADD CONSTRAINT hl7_encoding_type_pk PRIMARY KEY (id);


--
-- Name: hum_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample_human
    ADD CONSTRAINT hum_pk PRIMARY KEY (id);


--
-- Name: ia_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY instrument_analyte
    ADD CONSTRAINT ia_pk PRIMARY KEY (id);


--
-- Name: id; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY analyzer_results
    ADD CONSTRAINT id PRIMARY KEY (id);


--
-- Name: identity_type_uk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY patient_identity_type
    ADD CONSTRAINT identity_type_uk UNIQUE (identity_type);


--
-- Name: il_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY instrument_log
    ADD CONSTRAINT il_pk PRIMARY KEY (id);


--
-- Name: instru_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY instrument
    ADD CONSTRAINT instru_pk PRIMARY KEY (id);


--
-- Name: inv_recpt_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY inventory_receipt
    ADD CONSTRAINT inv_recpt_pk PRIMARY KEY (id);


--
-- Name: invcomp_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY inventory_component
    ADD CONSTRAINT invcomp_pk PRIMARY KEY (id);


--
-- Name: invitem_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY inventory_item
    ADD CONSTRAINT invitem_pk PRIMARY KEY (id);


--
-- Name: invloc_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY inventory_location
    ADD CONSTRAINT invloc_pk PRIMARY KEY (id);


--
-- Name: lab_order_item_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY lab_order_item
    ADD CONSTRAINT lab_order_item_pk PRIMARY KEY (id);


--
-- Name: lab_order_type_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY lab_order_type
    ADD CONSTRAINT lab_order_type_pk PRIMARY KEY (id);


--
-- Name: label_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY label
    ADD CONSTRAINT label_pk PRIMARY KEY (id);


--
-- Name: login_user_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY login_user
    ADD CONSTRAINT login_user_pk PRIMARY KEY (login_name);


--
-- Name: ma_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY method_analyte
    ADD CONSTRAINT ma_pk PRIMARY KEY (id);


--
-- Name: menu_element_id_key; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY menu
    ADD CONSTRAINT menu_element_id_key UNIQUE (element_id);


--
-- Name: message_org_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY message_org
    ADD CONSTRAINT message_org_pk PRIMARY KEY (id);


--
-- Name: method_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY method
    ADD CONSTRAINT method_pk PRIMARY KEY (id);


--
-- Name: methres_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY method_result
    ADD CONSTRAINT methres_pk PRIMARY KEY (id);


--
-- Name: mls_lab_tp_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY mls_lab_type
    ADD CONSTRAINT mls_lab_tp_pk PRIMARY KEY (id);


--
-- Name: newborn_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample_newborn
    ADD CONSTRAINT newborn_pk PRIMARY KEY (id);


--
-- Name: note_id; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY note
    ADD CONSTRAINT note_id PRIMARY KEY (id);


--
-- Name: oct_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY occupation
    ADD CONSTRAINT oct_pk PRIMARY KEY (id);


--
-- Name: or_properties_pkey; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY or_properties
    ADD CONSTRAINT or_properties_pkey PRIMARY KEY (property_id);


--
-- Name: or_properties_property_key_key; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY or_properties
    ADD CONSTRAINT or_properties_property_key_key UNIQUE (property_key);


--
-- Name: or_tags_pkey; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY or_tags
    ADD CONSTRAINT or_tags_pkey PRIMARY KEY (tag_id);


--
-- Name: ord_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY orders
    ADD CONSTRAINT ord_pk PRIMARY KEY (id);


--
-- Name: orditem_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY order_item
    ADD CONSTRAINT orditem_pk PRIMARY KEY (id);


--
-- Name: org_contact_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY organization_contact
    ADD CONSTRAINT org_contact_pk PRIMARY KEY (id);


--
-- Name: org_hl7_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY org_hl7_encoding_type
    ADD CONSTRAINT org_hl7_pk PRIMARY KEY (organization_id, encoding_type_id);


--
-- Name: org_mlt_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY org_mls_lab_type
    ADD CONSTRAINT org_mlt_pk PRIMARY KEY (org_mlt_id);


--
-- Name: org_org_type_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY organization_organization_type
    ADD CONSTRAINT org_org_type_pk PRIMARY KEY (org_id, org_type_id);


--
-- Name: org_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY organization
    ADD CONSTRAINT org_pk PRIMARY KEY (id);


--
-- Name: org_type_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY organization_type
    ADD CONSTRAINT org_type_pk PRIMARY KEY (id);


--
-- Name: organization_address_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY organization_address
    ADD CONSTRAINT organization_address_pk PRIMARY KEY (organization_id, address_part_id);


--
-- Name: pac_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY package_1
    ADD CONSTRAINT pac_pk PRIMARY KEY (id);


--
-- Name: pan_it_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY panel_item
    ADD CONSTRAINT pan_it_pk PRIMARY KEY (id);


--
-- Name: panel_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY panel
    ADD CONSTRAINT panel_pk PRIMARY KEY (id);


--
-- Name: pat_ident_type_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY patient_identity_type
    ADD CONSTRAINT pat_ident_type_pk PRIMARY KEY (id);


--
-- Name: pat_identity_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY patient_identity
    ADD CONSTRAINT pat_identity_pk PRIMARY KEY (id);


--
-- Name: pat_occupation_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY patient_occupation
    ADD CONSTRAINT pat_occupation_pk PRIMARY KEY (id);


--
-- Name: pat_pat_type_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY patient_patient_type
    ADD CONSTRAINT pat_pat_type_pk PRIMARY KEY (id);


--
-- Name: pat_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY patient
    ADD CONSTRAINT pat_pk PRIMARY KEY (id);


--
-- Name: pat_type_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY patient_type
    ADD CONSTRAINT pat_type_pk PRIMARY KEY (id);


--
-- Name: pay_type_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY payment_type
    ADD CONSTRAINT pay_type_pk PRIMARY KEY (id);


--
-- Name: person_address_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY person_address
    ADD CONSTRAINT person_address_pk PRIMARY KEY (person_id, address_part_id);


--
-- Name: person_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY person
    ADD CONSTRAINT person_pk PRIMARY KEY (id);


--
-- Name: pk_databasechangelog; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY databasechangelog
    ADD CONSTRAINT pk_databasechangelog PRIMARY KEY (id, author, filename);


--
-- Name: pk_databasechangeloglock; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY databasechangeloglock
    ADD CONSTRAINT pk_databasechangeloglock PRIMARY KEY (id);


--
-- Name: pk_document_track; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY document_track
    ADD CONSTRAINT pk_document_track PRIMARY KEY (id);


--
-- Name: pk_document_type; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY document_type
    ADD CONSTRAINT pk_document_type PRIMARY KEY (id);


--
-- Name: pk_menu; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY menu
    ADD CONSTRAINT pk_menu PRIMARY KEY (id);


--
-- Name: pk_qa_observation; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY qa_observation
    ADD CONSTRAINT pk_qa_observation PRIMARY KEY (id);


--
-- Name: pk_qa_observation_type; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY qa_observation_type
    ADD CONSTRAINT pk_qa_observation_type PRIMARY KEY (id);


--
-- Name: pr_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY patient_relations
    ADD CONSTRAINT pr_pk PRIMARY KEY (id);


--
-- Name: progs_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY program
    ADD CONSTRAINT progs_pk PRIMARY KEY (id);


--
-- Name: proj_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY project
    ADD CONSTRAINT proj_pk PRIMARY KEY (id);


--
-- Name: project_org_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY project_organization
    ADD CONSTRAINT project_org_pk PRIMARY KEY (project_id, org_id);


--
-- Name: projparam_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY project_parameter
    ADD CONSTRAINT projparam_pk PRIMARY KEY (id);


--
-- Name: provider_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY provider
    ADD CONSTRAINT provider_pk PRIMARY KEY (id);


--
-- Name: qa_event_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY qa_event
    ADD CONSTRAINT qa_event_pk PRIMARY KEY (id);


--
-- Name: qc_analyt_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY qc_analytes
    ADD CONSTRAINT qc_analyt_pk PRIMARY KEY (id);


--
-- Name: qc_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY qc
    ADD CONSTRAINT qc_pk PRIMARY KEY (id);


--
-- Name: qrtz_blob_triggers_pkey; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY qrtz_blob_triggers
    ADD CONSTRAINT qrtz_blob_triggers_pkey PRIMARY KEY (trigger_name, trigger_group);


--
-- Name: qrtz_calendars_pkey; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY qrtz_calendars
    ADD CONSTRAINT qrtz_calendars_pkey PRIMARY KEY (calendar_name);


--
-- Name: qrtz_cron_triggers_pkey; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY qrtz_cron_triggers
    ADD CONSTRAINT qrtz_cron_triggers_pkey PRIMARY KEY (trigger_name, trigger_group);


--
-- Name: qrtz_fired_triggers_pkey; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY qrtz_fired_triggers
    ADD CONSTRAINT qrtz_fired_triggers_pkey PRIMARY KEY (entry_id);


--
-- Name: qrtz_job_details_pkey; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY qrtz_job_details
    ADD CONSTRAINT qrtz_job_details_pkey PRIMARY KEY (job_name, job_group);


--
-- Name: qrtz_job_listeners_pkey; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY qrtz_job_listeners
    ADD CONSTRAINT qrtz_job_listeners_pkey PRIMARY KEY (job_name, job_group, job_listener);


--
-- Name: qrtz_locks_pkey; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY qrtz_locks
    ADD CONSTRAINT qrtz_locks_pkey PRIMARY KEY (lock_name);


--
-- Name: qrtz_paused_trigger_grps_pkey; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY qrtz_paused_trigger_grps
    ADD CONSTRAINT qrtz_paused_trigger_grps_pkey PRIMARY KEY (trigger_group);


--
-- Name: qrtz_scheduler_state_pkey; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY qrtz_scheduler_state
    ADD CONSTRAINT qrtz_scheduler_state_pkey PRIMARY KEY (instance_name);


--
-- Name: qrtz_simple_triggers_pkey; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY qrtz_simple_triggers
    ADD CONSTRAINT qrtz_simple_triggers_pkey PRIMARY KEY (trigger_name, trigger_group);


--
-- Name: qrtz_trigger_listeners_pkey; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY qrtz_trigger_listeners
    ADD CONSTRAINT qrtz_trigger_listeners_pkey PRIMARY KEY (trigger_name, trigger_group, trigger_listener);


--
-- Name: qrtz_triggers_pkey; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY qrtz_triggers
    ADD CONSTRAINT qrtz_triggers_pkey PRIMARY KEY (trigger_name, trigger_group);


--
-- Name: race_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY race
    ADD CONSTRAINT race_pk PRIMARY KEY (id);


--
-- Name: receiver_code_element_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY receiver_code_element
    ADD CONSTRAINT receiver_code_element_pk PRIMARY KEY (id);


--
-- Name: referral_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY referral
    ADD CONSTRAINT referral_pk PRIMARY KEY (id);


--
-- Name: referral_reason_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY referral_reason
    ADD CONSTRAINT referral_reason_pk PRIMARY KEY (id);


--
-- Name: referral_result_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY referral_result
    ADD CONSTRAINT referral_result_pk PRIMARY KEY (id);


--
-- Name: referral_type_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY referral_type
    ADD CONSTRAINT referral_type_pk PRIMARY KEY (id);


--
-- Name: region_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY region
    ADD CONSTRAINT region_pk PRIMARY KEY (id);


--
-- Name: report_external_import_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY report_external_import
    ADD CONSTRAINT report_external_import_pk PRIMARY KEY (id);


--
-- Name: report_queue_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY report_external_export
    ADD CONSTRAINT report_queue_pk PRIMARY KEY (id);


--
-- Name: report_queue_type_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY report_queue_type
    ADD CONSTRAINT report_queue_type_pk PRIMARY KEY (id);


--
-- Name: requester_type_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY requester_type
    ADD CONSTRAINT requester_type_pk PRIMARY KEY (id);


--
-- Name: result_inventory_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY result_inventory
    ADD CONSTRAINT result_inventory_pk PRIMARY KEY (id);


--
-- Name: result_limits_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY result_limits
    ADD CONSTRAINT result_limits_pk PRIMARY KEY (id);


--
-- Name: result_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY result
    ADD CONSTRAINT result_pk PRIMARY KEY (id);


--
-- Name: result_signature_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY result_signature
    ADD CONSTRAINT result_signature_pk PRIMARY KEY (id);


--
-- Name: rt_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY reference_tables
    ADD CONSTRAINT rt_pk PRIMARY KEY (id);


--
-- Name: samp_org_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample_organization
    ADD CONSTRAINT samp_org_pk PRIMARY KEY (id);


--
-- Name: samp_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample
    ADD CONSTRAINT samp_pk PRIMARY KEY (id);


--
-- Name: sampitem_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample_item
    ADD CONSTRAINT sampitem_pk PRIMARY KEY (id);


--
-- Name: sample_human_samp_id_u; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample_human
    ADD CONSTRAINT sample_human_samp_id_u UNIQUE (samp_id);


--
-- Name: sample_pdf_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample_pdf
    ADD CONSTRAINT sample_pdf_pk PRIMARY KEY (id);


--
-- Name: sample_projects_pk_i; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample_projects
    ADD CONSTRAINT sample_projects_pk_i PRIMARY KEY (id);


--
-- Name: sample_qaevent_action_pkey; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample_qaevent_action
    ADD CONSTRAINT sample_qaevent_action_pkey PRIMARY KEY (id);


--
-- Name: sample_qaevent_pkey; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample_qaevent
    ADD CONSTRAINT sample_qaevent_pkey PRIMARY KEY (id);


--
-- Name: sample_requester_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample_requester
    ADD CONSTRAINT sample_requester_pk PRIMARY KEY (sample_id, requester_id, requester_type_id);


--
-- Name: sampledom_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample_domain
    ADD CONSTRAINT sampledom_pk PRIMARY KEY (id);


--
-- Name: sampletype_panel_pkey; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sampletype_panel
    ADD CONSTRAINT sampletype_panel_pkey PRIMARY KEY (id);


--
-- Name: sampletype_test_pkey; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sampletype_test
    ADD CONSTRAINT sampletype_test_pkey PRIMARY KEY (id);


--
-- Name: sci_name_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY animal_scientific_name
    ADD CONSTRAINT sci_name_pk PRIMARY KEY (id);


--
-- Name: scr_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY state_code
    ADD CONSTRAINT scr_pk PRIMARY KEY (id);


--
-- Name: scrip_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY scriptlet
    ADD CONSTRAINT scrip_pk PRIMARY KEY (id);


--
-- Name: site_info_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY site_information
    ADD CONSTRAINT site_info_pk PRIMARY KEY (id);


--
-- Name: site_information_domain_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY site_information_domain
    ADD CONSTRAINT site_information_domain_pk PRIMARY KEY (id);


--
-- Name: source_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY source_of_sample
    ADD CONSTRAINT source_pk PRIMARY KEY (id);


--
-- Name: storage_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY storage_location
    ADD CONSTRAINT storage_pk PRIMARY KEY (id);


--
-- Name: su_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY storage_unit
    ADD CONSTRAINT su_pk PRIMARY KEY (id);


--
-- Name: sys_c003998; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY action
    ADD CONSTRAINT sys_c003998 PRIMARY KEY (id);


--
-- Name: sys_c003999; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY action
    ADD CONSTRAINT sys_c003999 UNIQUE (code);


--
-- Name: sys_c004009; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY analysis_qaevent_action
    ADD CONSTRAINT sys_c004009 PRIMARY KEY (id);


--
-- Name: sys_c004307; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY status_of_sample
    ADD CONSTRAINT sys_c004307 PRIMARY KEY (id);


--
-- Name: sys_mod_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY system_module
    ADD CONSTRAINT sys_mod_pk PRIMARY KEY (id);


--
-- Name: sys_role_mo_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY system_role_module
    ADD CONSTRAINT sys_role_mo_pk PRIMARY KEY (id);


--
-- Name: sys_use_mo_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY system_user_module
    ADD CONSTRAINT sys_use_mo_pk PRIMARY KEY (id);


--
-- Name: sys_use_se_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY system_user_section
    ADD CONSTRAINT sys_use_se_pk PRIMARY KEY (id);


--
-- Name: sys_user_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY system_user
    ADD CONSTRAINT sys_user_pk PRIMARY KEY (id);


--
-- Name: sys_usr_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY system_user_role
    ADD CONSTRAINT sys_usr_pk PRIMARY KEY (system_user_id, role_id);


--
-- Name: system_role_pkey; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY system_role
    ADD CONSTRAINT system_role_pkey PRIMARY KEY (id);


--
-- Name: test_analyte_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test_analyte
    ADD CONSTRAINT test_analyte_pk PRIMARY KEY (id);


--
-- Name: test_hl7_code_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test_code
    ADD CONSTRAINT test_hl7_code_pk PRIMARY KEY (test_id, code_type_id);


--
-- Name: test_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test
    ADD CONSTRAINT test_pk PRIMARY KEY (id);


--
-- Name: test_reflx_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test_reflex
    ADD CONSTRAINT test_reflx_pk PRIMARY KEY (id);


--
-- Name: test_sect_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test_section
    ADD CONSTRAINT test_sect_pk PRIMARY KEY (id);


--
-- Name: testfrmt_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test_formats
    ADD CONSTRAINT testfrmt_pk PRIMARY KEY (id);


--
-- Name: tst_rslt_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test_result
    ADD CONSTRAINT tst_rslt_pk PRIMARY KEY (id);


--
-- Name: tsttrlr_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test_trailer
    ADD CONSTRAINT tsttrlr_pk PRIMARY KEY (id);


--
-- Name: tw_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test_worksheets
    ADD CONSTRAINT tw_pk PRIMARY KEY (id);


--
-- Name: twi_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test_worksheet_item
    ADD CONSTRAINT twi_pk PRIMARY KEY (id);


--
-- Name: type_of_test_result_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY type_of_test_result
    ADD CONSTRAINT type_of_test_result_pk PRIMARY KEY (id);


--
-- Name: typeosamp_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY type_of_sample
    ADD CONSTRAINT typeosamp_pk PRIMARY KEY (id);


--
-- Name: typofprovider_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY type_of_provider
    ADD CONSTRAINT typofprovider_pk PRIMARY KEY (id);


--
-- Name: uom_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY unit_of_measure
    ADD CONSTRAINT uom_pk PRIMARY KEY (id);


--
-- Name: user_alert_map_pkey; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY user_alert_map
    ADD CONSTRAINT user_alert_map_pkey PRIMARY KEY (user_id, map_id);


--
-- Name: user_group_map_pkey; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY user_group_map
    ADD CONSTRAINT user_group_map_pkey PRIMARY KEY (user_id, map_id);


--
-- Name: workst_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY worksheets
    ADD CONSTRAINT workst_pk PRIMARY KEY (id);


--
-- Name: wq_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY worksheet_qc
    ADD CONSTRAINT wq_pk PRIMARY KEY (id);


--
-- Name: wrkst_anls_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY worksheet_analysis
    ADD CONSTRAINT wrkst_anls_pk PRIMARY KEY (id);


--
-- Name: wrkst_anlt_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY worksheet_analyte
    ADD CONSTRAINT wrkst_anlt_pk PRIMARY KEY (id);


--
-- Name: wrkst_item_pk; Type: CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY worksheet_item
    ADD CONSTRAINT wrkst_item_pk PRIMARY KEY (id);


--
-- Name: accnum_uk; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE UNIQUE INDEX accnum_uk ON sample USING btree (accession_number);


--
-- Name: ad_afield_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX ad_afield_fk_i ON aux_data USING btree (aux_field_id);


--
-- Name: af_analyte_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX af_analyte_fk_i ON aux_field USING btree (analyte_id);


--
-- Name: af_script_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX af_script_fk_i ON aux_field USING btree (scriptlet_id);


--
-- Name: afv_afield_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX afv_afield_fk_i ON aux_field_values USING btree (aux_field_id);


--
-- Name: analysis_sampitem_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX analysis_sampitem_fk_i ON analysis USING btree (sampitem_id);


--
-- Name: analysis_test_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX analysis_test_fk_i ON analysis USING btree (test_id);


--
-- Name: analysis_test_sect_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX analysis_test_sect_fk_i ON analysis USING btree (test_sect_id);


--
-- Name: analyte_analyte_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX analyte_analyte_fk_i ON analyte USING btree (analyte_id);


--
-- Name: ani_samp_comm_anim_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX ani_samp_comm_anim_fk_i ON sample_animal USING btree (comm_anim_id);


--
-- Name: ani_samp_samp_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX ani_samp_samp_fk_i ON sample_animal USING btree (samp_id);


--
-- Name: ani_samp_sci_name_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX ani_samp_sci_name_fk_i ON sample_animal USING btree (sci_name_id);


--
-- Name: anqaev_anal_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX anqaev_anal_fk_i ON analysis_qaevent USING btree (analysis_id);


--
-- Name: anqaev_qa_event_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX anqaev_qa_event_fk_i ON analysis_qaevent USING btree (qa_event_id);


--
-- Name: anst_anal_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX anst_anal_fk_i ON analysis_storages USING btree (analysis_id);


--
-- Name: anstore_storage_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX anstore_storage_fk_i ON analysis_storages USING btree (storage_id);


--
-- Name: anus_anal_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX anus_anal_fk_i ON analysis_users USING btree (analysis_id);


--
-- Name: anus_sysuser_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX anus_sysuser_fk_i ON analysis_users USING btree (system_user_id);


--
-- Name: attachmtitem_attachmt_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX attachmtitem_attachmt_fk_i ON attachment_item USING btree (attachment_id);


--
-- Name: env_samp_samp_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX env_samp_samp_fk_i ON sample_environmental USING btree (samp_id);


--
-- Name: hist_sys_user_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX hist_sys_user_fk_i ON history USING btree (sys_user_id);


--
-- Name: hist_table_row_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX hist_table_row_i ON history USING btree (reference_id, reference_table);


--
-- Name: hum_pat_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX hum_pat_fk_i ON sample_human USING btree (patient_id);


--
-- Name: hum_provider_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX hum_provider_fk_i ON sample_human USING btree (provider_id);


--
-- Name: hum_samp_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX hum_samp_fk_i ON sample_human USING btree (samp_id);


--
-- Name: ia_analyte_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX ia_analyte_fk_i ON instrument_analyte USING btree (analyte_id);


--
-- Name: ia_instru_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX ia_instru_fk_i ON instrument_analyte USING btree (instru_id);


--
-- Name: ia_method_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX ia_method_fk_i ON instrument_analyte USING btree (method_id);


--
-- Name: il_instru_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX il_instru_fk_i ON instrument_log USING btree (instru_id);


--
-- Name: il_inv_item_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX il_inv_item_fk_i ON inventory_location USING btree (inv_item_id);


--
-- Name: instru_scrip_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX instru_scrip_fk_i ON instrument USING btree (scrip_id);


--
-- Name: inv_recpt_invitem_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX inv_recpt_invitem_fk_i ON inventory_receipt USING btree (invitem_id);


--
-- Name: invcomp_invitem_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX invcomp_invitem_fk_i ON inventory_component USING btree (invitem_id);


--
-- Name: invcomp_matcomp_fk_ii; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX invcomp_matcomp_fk_ii ON inventory_component USING btree (material_component_id);


--
-- Name: invitem_invname_uk; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE UNIQUE INDEX invitem_invname_uk ON inventory_item USING btree (name);


--
-- Name: invitem_uom_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX invitem_uom_fk_i ON inventory_item USING btree (uom_id);


--
-- Name: invloc_storage_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX invloc_storage_fk_i ON inventory_location USING btree (storage_id);


--
-- Name: invrec_org_fk_ii; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX invrec_org_fk_ii ON inventory_receipt USING btree (org_id);


--
-- Name: label_script_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX label_script_fk_i ON label USING btree (scriptlet_id);


--
-- Name: ma_analyte_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX ma_analyte_fk_i ON method_analyte USING btree (analyte_id);


--
-- Name: ma_method_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX ma_method_fk_i ON method_analyte USING btree (method_id);


--
-- Name: methres_method_fk_iii; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX methres_method_fk_iii ON method_result USING btree (method_id);


--
-- Name: methres_scrip_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX methres_scrip_fk_i ON method_result USING btree (scrip_id);


--
-- Name: mls_lab_tp_org_mlt_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX mls_lab_tp_org_mlt_fk_i ON mls_lab_type USING btree (org_mlt_org_mlt_id);


--
-- Name: note_sys_user_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX note_sys_user_fk_i ON note USING btree (sys_user_id);


--
-- Name: obs_history_sample_idx; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX obs_history_sample_idx ON observation_history USING btree (sample_id);


--
-- Name: ord_org_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX ord_org_fk_i ON orders USING btree (org_id);


--
-- Name: ord_sys_user_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX ord_sys_user_fk_i ON orders USING btree (sys_user_id);


--
-- Name: orditem_inv_loc_fk_iii; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX orditem_inv_loc_fk_iii ON order_item USING btree (inv_loc_id);


--
-- Name: orditem_ord_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX orditem_ord_fk_i ON order_item USING btree (ord_id);


--
-- Name: org_org_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX org_org_fk_i ON organization USING btree (org_id);


--
-- Name: org_org_mlt_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX org_org_mlt_fk_i ON organization USING btree (org_mlt_org_mlt_id);


--
-- Name: pan_it_panel_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX pan_it_panel_fk_i ON panel_item USING btree (panel_id);


--
-- Name: pat_person_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX pat_person_fk_i ON patient USING btree (person_id);


--
-- Name: pr_pat_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX pr_pat_fk_i ON patient_relations USING btree (pat_id);


--
-- Name: pr_pat_source_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX pr_pat_source_fk_i ON patient_relations USING btree (pat_id_source);


--
-- Name: proj_sys_user_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX proj_sys_user_fk_i ON project USING btree (sys_user_id);


--
-- Name: project_script_fk_iii; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX project_script_fk_iii ON project USING btree (scriptlet_id);


--
-- Name: projectparam_proj_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX projectparam_proj_fk_i ON project_parameter USING btree (project_id);


--
-- Name: provider_person_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX provider_person_fk_i ON provider USING btree (person_id);


--
-- Name: qaev_test_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX qaev_test_fk_i ON qa_event USING btree (test_id);


--
-- Name: qc_sys_user_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX qc_sys_user_fk_i ON qc USING btree (sys_user_id);


--
-- Name: qc_uom_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX qc_uom_fk_i ON qc USING btree (uom_id);


--
-- Name: qcanlyt_analyte_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX qcanlyt_analyte_fk_i ON qc_analytes USING btree (analyte_id);


--
-- Name: report_import_date; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX report_import_date ON report_external_import USING btree (event_date);


--
-- Name: report_queue_date; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX report_queue_date ON report_external_export USING btree (event_date);


--
-- Name: result_analysis_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX result_analysis_fk_i ON result USING btree (analysis_id);


--
-- Name: result_analyte_fk_iii; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX result_analyte_fk_iii ON result USING btree (analyte_id);


--
-- Name: result_testresult_fk_1; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX result_testresult_fk_1 ON result USING btree (test_result_id);


--
-- Name: samp_org_org_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX samp_org_org_fk_i ON sample_organization USING btree (org_id);


--
-- Name: samp_org_samp_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX samp_org_samp_fk_i ON sample_organization USING btree (samp_id);


--
-- Name: samp_package_1_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX samp_package_1_fk_i ON sample USING btree (package_id);


--
-- Name: samp_sys_user_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX samp_sys_user_fk_i ON sample USING btree (sys_user_id);


--
-- Name: sampitem_samp_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX sampitem_samp_fk_i ON sample_item USING btree (samp_id);


--
-- Name: sampitem_samp_item_uk_uk; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE UNIQUE INDEX sampitem_samp_item_uk_uk ON sample_item USING btree (id, sort_order);


--
-- Name: sampitem_sampitem_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX sampitem_sampitem_fk_i ON sample_item USING btree (sampitem_id);


--
-- Name: sampitem_source_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX sampitem_source_fk_i ON sample_item USING btree (source_id);


--
-- Name: sampitem_typeosamp_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX sampitem_typeosamp_fk_i ON sample_item USING btree (typeosamp_id);


--
-- Name: sampitem_uom_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX sampitem_uom_fk_i ON sample_item USING btree (uom_id);


--
-- Name: sample_projects_pk; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE UNIQUE INDEX sample_projects_pk ON sample_projects USING btree (id);


--
-- Name: sampledom_dom_uk_uk; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE UNIQUE INDEX sampledom_dom_uk_uk ON sample_domain USING btree (domain);


--
-- Name: sci_name_comm_anim_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX sci_name_comm_anim_fk_i ON animal_scientific_name USING btree (comm_anim_id);


--
-- Name: source_dom_desc_uk_uk; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE UNIQUE INDEX source_dom_desc_uk_uk ON source_of_sample USING btree (description, domain);


--
-- Name: sp_proj_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX sp_proj_fk_i ON sample_projects USING btree (proj_id);


--
-- Name: sp_samp_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX sp_samp_fk_i ON sample_projects USING btree (samp_id);


--
-- Name: storloc_parent_storloc_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX storloc_parent_storloc_fk_i ON storage_location USING btree (parent_storageloc_id);


--
-- Name: storloc_storunit_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX storloc_storunit_fk_i ON storage_location USING btree (storage_unit_id);


--
-- Name: sysrolemodule_sysmodule_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX sysrolemodule_sysmodule_fk_i ON system_role_module USING btree (system_module_id);


--
-- Name: sysrolemodule_sysuser_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX sysrolemodule_sysuser_fk_i ON system_role_module USING btree (system_role_id);


--
-- Name: sysusermodule_sysmodule_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX sysusermodule_sysmodule_fk_i ON system_user_module USING btree (system_module_id);


--
-- Name: sysusermodule_sysuser_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX sysusermodule_sysuser_fk_i ON system_user_module USING btree (system_user_id);


--
-- Name: sysusersect_sysuser_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX sysusersect_sysuser_fk_i ON system_user_section USING btree (system_user_id);


--
-- Name: sysusersect_testsect_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX sysusersect_testsect_fk_i ON system_user_section USING btree (test_section_id);


--
-- Name: test_desc_uk; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE UNIQUE INDEX test_desc_uk ON test USING btree (description);


--
-- Name: test_label_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX test_label_fk_i ON test USING btree (label_id);


--
-- Name: test_method_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX test_method_fk_i ON test USING btree (method_id);


--
-- Name: test_reflx_tst_rslt_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX test_reflx_tst_rslt_fk_i ON test_reflex USING btree (tst_rslt_id);


--
-- Name: test_scriptlet_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX test_scriptlet_fk_i ON test USING btree (scriptlet_id);


--
-- Name: test_sect_org_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX test_sect_org_fk_i ON test_section USING btree (org_id);


--
-- Name: test_testformat_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX test_testformat_fk_i ON test USING btree (test_format_id);


--
-- Name: test_testsect_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX test_testsect_fk_i ON test USING btree (test_section_id);


--
-- Name: test_testtrailer_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX test_testtrailer_fk_i ON test USING btree (test_trailer_id);


--
-- Name: test_uom_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX test_uom_fk_i ON test USING btree (uom_id);


--
-- Name: testalyt_analyte_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX testalyt_analyte_fk_i ON test_analyte USING btree (analyte_id);


--
-- Name: testalyt_test_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX testalyt_test_fk_i ON test_analyte USING btree (test_id);


--
-- Name: testreflex_addtest_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX testreflex_addtest_fk_i ON test_reflex USING btree (add_test_id);


--
-- Name: testreflex_test_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX testreflex_test_fk_i ON test_reflex USING btree (test_id);


--
-- Name: testreflex_testanalyt_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX testreflex_testanalyt_fk_i ON test_reflex USING btree (test_analyte_id);


--
-- Name: testresult_scriptlet_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX testresult_scriptlet_fk_i ON test_result USING btree (scriptlet_id);


--
-- Name: tst_rslt_test_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX tst_rslt_test_fk_i ON test_result USING btree (test_id);


--
-- Name: tw_test_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX tw_test_fk_i ON test_worksheets USING btree (test_id);


--
-- Name: twi_qc_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX twi_qc_fk_i ON test_worksheet_item USING btree (qc_id);


--
-- Name: twi_tw_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX twi_tw_fk_i ON test_worksheet_item USING btree (tw_id);


--
-- Name: typeosamp_dom_desc_uk_uk; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE UNIQUE INDEX typeosamp_dom_desc_uk_uk ON type_of_sample USING btree (description, domain);


--
-- Name: typofprov_desc_uk_uk; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE UNIQUE INDEX typofprov_desc_uk_uk ON type_of_provider USING btree (description);


--
-- Name: wkshtanalysis_wkshtitem_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX wkshtanalysis_wkshtitem_fk_i ON worksheet_analysis USING btree (worksheet_item_id);


--
-- Name: wkshtanalyte_result_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX wkshtanalyte_result_fk_i ON worksheet_analyte USING btree (result_id);


--
-- Name: wkshtitem_wksht_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX wkshtitem_wksht_fk_i ON worksheet_item USING btree (worksheet_id);


--
-- Name: wkshtqc_qcanalyte_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX wkshtqc_qcanalyte_fk_i ON worksheet_qc USING btree (qc_analyte_id);


--
-- Name: wkshtqc_wkshtanalysis_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX wkshtqc_wkshtanalysis_fk_i ON worksheet_qc USING btree (worksheet_analysis_id);


--
-- Name: workst_sys_user_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX workst_sys_user_fk_i ON worksheets USING btree (sys_user_id);


--
-- Name: workst_test_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX workst_test_fk_i ON worksheets USING btree (test_id);


--
-- Name: wrkst_anlt_wrkst_anls_fk_i; Type: INDEX; Schema: clinlims; Owner: clinlims
--

CREATE INDEX wrkst_anlt_wrkst_anls_fk_i ON worksheet_analyte USING btree (wrkst_anls_id);


--
-- Name: ad_afield_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY aux_data
    ADD CONSTRAINT ad_afield_fk FOREIGN KEY (aux_field_id) REFERENCES aux_field(id) MATCH FULL;


--
-- Name: af_analyte_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY aux_field
    ADD CONSTRAINT af_analyte_fk FOREIGN KEY (analyte_id) REFERENCES analyte(id) MATCH FULL;


--
-- Name: af_script_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY aux_field
    ADD CONSTRAINT af_script_fk FOREIGN KEY (scriptlet_id) REFERENCES scriptlet(id) MATCH FULL;


--
-- Name: afv_afield_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY aux_field_values
    ADD CONSTRAINT afv_afield_fk FOREIGN KEY (aux_field_id) REFERENCES aux_field(id) MATCH FULL;


--
-- Name: analysis_panel_FK; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY analysis
    ADD CONSTRAINT "analysis_panel_FK" FOREIGN KEY (panel_id) REFERENCES panel(id) ON DELETE SET NULL;


--
-- Name: analysis_parent_analysis_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY analysis
    ADD CONSTRAINT analysis_parent_analysis_fk FOREIGN KEY (parent_analysis_id) REFERENCES analysis(id) MATCH FULL;


--
-- Name: analysis_parent_result_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY analysis
    ADD CONSTRAINT analysis_parent_result_fk FOREIGN KEY (parent_result_id) REFERENCES result(id) MATCH FULL;


--
-- Name: analysis_sampitem_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY analysis
    ADD CONSTRAINT analysis_sampitem_fk FOREIGN KEY (sampitem_id) REFERENCES sample_item(id) MATCH FULL;


--
-- Name: analysis_status_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY analysis
    ADD CONSTRAINT analysis_status_fk FOREIGN KEY (status_id) REFERENCES status_of_sample(id);


--
-- Name: analysis_test_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY analysis
    ADD CONSTRAINT analysis_test_fk FOREIGN KEY (test_id) REFERENCES test(id) MATCH FULL;


--
-- Name: analysis_test_sect_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY analysis
    ADD CONSTRAINT analysis_test_sect_fk FOREIGN KEY (test_sect_id) REFERENCES test_section(id) MATCH FULL;


--
-- Name: analyte_analyte_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY analyte
    ADD CONSTRAINT analyte_analyte_fk FOREIGN KEY (analyte_id) REFERENCES analyte(id) MATCH FULL;


--
-- Name: analyzer_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY analyzer_results
    ADD CONSTRAINT analyzer_fk FOREIGN KEY (analyzer_id) REFERENCES analyzer(id);


--
-- Name: analyzer_results_test_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY analyzer_results
    ADD CONSTRAINT analyzer_results_test_fk FOREIGN KEY (test_id) REFERENCES test(id);


--
-- Name: analyzer_test_map_analyzer_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY analyzer_test_map
    ADD CONSTRAINT analyzer_test_map_analyzer_fk FOREIGN KEY (analyzer_id) REFERENCES analyzer(id);


--
-- Name: analyzer_test_map_test_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY analyzer_test_map
    ADD CONSTRAINT analyzer_test_map_test_fk FOREIGN KEY (test_id) REFERENCES test(id);


--
-- Name: ani_samp_comm_anim_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample_animal
    ADD CONSTRAINT ani_samp_comm_anim_fk FOREIGN KEY (comm_anim_id) REFERENCES animal_common_name(id) MATCH FULL;


--
-- Name: ani_samp_samp_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample_animal
    ADD CONSTRAINT ani_samp_samp_fk FOREIGN KEY (samp_id) REFERENCES sample(id) MATCH FULL;


--
-- Name: ani_samp_sci_name_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample_animal
    ADD CONSTRAINT ani_samp_sci_name_fk FOREIGN KEY (sci_name_id) REFERENCES animal_scientific_name(id) MATCH FULL;


--
-- Name: anqaev_anal_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY analysis_qaevent
    ADD CONSTRAINT anqaev_anal_fk FOREIGN KEY (analysis_id) REFERENCES analysis(id) MATCH FULL;


--
-- Name: anqaev_qa_event_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY analysis_qaevent
    ADD CONSTRAINT anqaev_qa_event_fk FOREIGN KEY (qa_event_id) REFERENCES qa_event(id) MATCH FULL;


--
-- Name: anst_anal_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY analysis_storages
    ADD CONSTRAINT anst_anal_fk FOREIGN KEY (analysis_id) REFERENCES analysis(id) MATCH FULL;


--
-- Name: anstore_storage_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY analysis_storages
    ADD CONSTRAINT anstore_storage_fk FOREIGN KEY (storage_id) REFERENCES storage_location(id) MATCH FULL;


--
-- Name: anus_anal_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY analysis_users
    ADD CONSTRAINT anus_anal_fk FOREIGN KEY (analysis_id) REFERENCES analysis(id) MATCH FULL;


--
-- Name: anus_sysuser_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY analysis_users
    ADD CONSTRAINT anus_sysuser_fk FOREIGN KEY (system_user_id) REFERENCES system_user(id) MATCH FULL;


--
-- Name: attachmtitem_attachmt_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY attachment_item
    ADD CONSTRAINT attachmtitem_attachmt_fk FOREIGN KEY (attachment_id) REFERENCES attachment(id) MATCH FULL;


--
-- Name: cd_elmt_xref_cd_elmt_type_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY code_element_xref
    ADD CONSTRAINT cd_elmt_xref_cd_elmt_type_fk FOREIGN KEY (code_element_type_id) REFERENCES code_element_type(id) MATCH FULL;


--
-- Name: cd_elmt_xref_message_org_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY code_element_xref
    ADD CONSTRAINT cd_elmt_xref_message_org_fk FOREIGN KEY (message_org_id) REFERENCES message_org(id) MATCH FULL;


--
-- Name: cd_elmt_xref_rcvr_cd_elmt_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY code_element_xref
    ADD CONSTRAINT cd_elmt_xref_rcvr_cd_elmt_fk FOREIGN KEY (receiver_code_element_id) REFERENCES receiver_code_element(id) MATCH FULL;


--
-- Name: demographics_history_type_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY observation_history
    ADD CONSTRAINT demographics_history_type_fk FOREIGN KEY (observation_history_type_id) REFERENCES observation_history_type(id);


--
-- Name: dictionary_dict_cat_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY dictionary
    ADD CONSTRAINT dictionary_dict_cat_fk FOREIGN KEY (dictionary_category_id) REFERENCES dictionary_category(id) MATCH FULL;


--
-- Name: env_samp_samp_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample_environmental
    ADD CONSTRAINT env_samp_samp_fk FOREIGN KEY (samp_id) REFERENCES sample(id) MATCH FULL;


--
-- Name: fk_doc_parent_id; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY document_track
    ADD CONSTRAINT fk_doc_parent_id FOREIGN KEY (parent_id) REFERENCES document_track(id);


--
-- Name: fk_doc_type; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY document_track
    ADD CONSTRAINT fk_doc_type FOREIGN KEY (document_type_id) REFERENCES document_type(id);


--
-- Name: fk_sibling_reflex; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test_reflex
    ADD CONSTRAINT fk_sibling_reflex FOREIGN KEY (sibling_reflex) REFERENCES test_reflex(id) ON DELETE CASCADE;


--
-- Name: fk_table_id; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY document_track
    ADD CONSTRAINT fk_table_id FOREIGN KEY (table_id) REFERENCES reference_tables(id);


--
-- Name: history_sysuer_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY history
    ADD CONSTRAINT history_sysuer_fk FOREIGN KEY (sys_user_id) REFERENCES system_user(id) MATCH FULL;


--
-- Name: ia_analyte_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY instrument_analyte
    ADD CONSTRAINT ia_analyte_fk FOREIGN KEY (analyte_id) REFERENCES analyte(id) MATCH FULL;


--
-- Name: ia_instru_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY instrument_analyte
    ADD CONSTRAINT ia_instru_fk FOREIGN KEY (instru_id) REFERENCES instrument(id) MATCH FULL;


--
-- Name: ia_method_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY instrument_analyte
    ADD CONSTRAINT ia_method_fk FOREIGN KEY (method_id) REFERENCES method(id) MATCH FULL;


--
-- Name: identity_type_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY patient_identity
    ADD CONSTRAINT identity_type_fk FOREIGN KEY (identity_type_id) REFERENCES patient_identity_type(id);


--
-- Name: il_instru_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY instrument_log
    ADD CONSTRAINT il_instru_fk FOREIGN KEY (instru_id) REFERENCES instrument(id) MATCH FULL;


--
-- Name: il_inv_item_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY inventory_location
    ADD CONSTRAINT il_inv_item_fk FOREIGN KEY (inv_item_id) REFERENCES inventory_item(id) MATCH FULL;


--
-- Name: inv_recpt_invitem_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY inventory_receipt
    ADD CONSTRAINT inv_recpt_invitem_fk FOREIGN KEY (invitem_id) REFERENCES inventory_item(id) MATCH FULL;


--
-- Name: invcomp_invitem_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY inventory_component
    ADD CONSTRAINT invcomp_invitem_fk FOREIGN KEY (invitem_id) REFERENCES inventory_item(id) MATCH FULL;


--
-- Name: invcomp_matcomp_fk_iii; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY inventory_component
    ADD CONSTRAINT invcomp_matcomp_fk_iii FOREIGN KEY (material_component_id) REFERENCES inventory_item(id) MATCH FULL;


--
-- Name: inventory__location_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY result_inventory
    ADD CONSTRAINT inventory__location_fk FOREIGN KEY (inventory_location_id) REFERENCES inventory_location(id);


--
-- Name: invitem_uom_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY inventory_item
    ADD CONSTRAINT invitem_uom_fk FOREIGN KEY (uom_id) REFERENCES unit_of_measure(id) MATCH FULL;


--
-- Name: invloc_storage_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY inventory_location
    ADD CONSTRAINT invloc_storage_fk FOREIGN KEY (storage_id) REFERENCES storage_location(id) MATCH FULL;


--
-- Name: invrec_org_fk_iii; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY inventory_receipt
    ADD CONSTRAINT invrec_org_fk_iii FOREIGN KEY (org_id) REFERENCES organization(id) MATCH FULL;


--
-- Name: lab_order_item_table_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY lab_order_item
    ADD CONSTRAINT lab_order_item_table_fk FOREIGN KEY (table_ref) REFERENCES reference_tables(id);


--
-- Name: lab_order_item_type_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY lab_order_item
    ADD CONSTRAINT lab_order_item_type_fk FOREIGN KEY (lab_order_type_id) REFERENCES lab_order_type(id);


--
-- Name: label_script_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY label
    ADD CONSTRAINT label_script_fk FOREIGN KEY (scriptlet_id) REFERENCES scriptlet(id) MATCH FULL;


--
-- Name: ma_analyte_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY method_analyte
    ADD CONSTRAINT ma_analyte_fk FOREIGN KEY (analyte_id) REFERENCES analyte(id) MATCH FULL;


--
-- Name: ma_method_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY method_analyte
    ADD CONSTRAINT ma_method_fk FOREIGN KEY (method_id) REFERENCES method(id) MATCH FULL;


--
-- Name: menu_parent_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY menu
    ADD CONSTRAINT menu_parent_fk FOREIGN KEY (parent_id) REFERENCES menu(id);


--
-- Name: methres_method_fk_ii; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY method_result
    ADD CONSTRAINT methres_method_fk_ii FOREIGN KEY (method_id) REFERENCES method(id) MATCH FULL;


--
-- Name: methres_scrip_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY method_result
    ADD CONSTRAINT methres_scrip_fk FOREIGN KEY (scrip_id) REFERENCES scriptlet(id) MATCH FULL;


--
-- Name: mls_lab_tp_org_mlt_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY mls_lab_type
    ADD CONSTRAINT mls_lab_tp_org_mlt_fk FOREIGN KEY (org_mlt_org_mlt_id) REFERENCES org_mls_lab_type(org_mlt_id) MATCH FULL;


--
-- Name: note_sys_user_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY note
    ADD CONSTRAINT note_sys_user_fk FOREIGN KEY (sys_user_id) REFERENCES system_user(id) MATCH FULL;


--
-- Name: ord_org_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY orders
    ADD CONSTRAINT ord_org_fk FOREIGN KEY (org_id) REFERENCES organization(id) MATCH FULL;


--
-- Name: ord_sys_user_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY orders
    ADD CONSTRAINT ord_sys_user_fk FOREIGN KEY (sys_user_id) REFERENCES system_user(id) MATCH FULL;


--
-- Name: orditem_inv_loc_fk_ii; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY order_item
    ADD CONSTRAINT orditem_inv_loc_fk_ii FOREIGN KEY (inv_loc_id) REFERENCES inventory_location(id) MATCH FULL;


--
-- Name: orditem_ord_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY order_item
    ADD CONSTRAINT orditem_ord_fk FOREIGN KEY (ord_id) REFERENCES orders(id) MATCH FULL;


--
-- Name: org_contact_org_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY organization_contact
    ADD CONSTRAINT org_contact_org_fk FOREIGN KEY (organization_id) REFERENCES organization(id);


--
-- Name: org_contact_person_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY organization_contact
    ADD CONSTRAINT org_contact_person_fk FOREIGN KEY (person_id) REFERENCES person(id);


--
-- Name: org_hl7_encoding_id_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY org_hl7_encoding_type
    ADD CONSTRAINT org_hl7_encoding_id_fk FOREIGN KEY (encoding_type_id) REFERENCES test_code_type(id);


--
-- Name: org_hl7_org_id_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY org_hl7_encoding_type
    ADD CONSTRAINT org_hl7_org_id_fk FOREIGN KEY (organization_id) REFERENCES organization(id);


--
-- Name: org_org_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY organization
    ADD CONSTRAINT org_org_fk FOREIGN KEY (org_id) REFERENCES organization(id) MATCH FULL;


--
-- Name: org_org_mlt_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY organization
    ADD CONSTRAINT org_org_mlt_fk FOREIGN KEY (org_mlt_org_mlt_id) REFERENCES org_mls_lab_type(org_mlt_id) MATCH FULL;


--
-- Name: organization_address_address_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY organization_address
    ADD CONSTRAINT organization_address_address_fk FOREIGN KEY (address_part_id) REFERENCES address_part(id);


--
-- Name: organization_address_organization_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY organization_address
    ADD CONSTRAINT organization_address_organization_fk FOREIGN KEY (organization_id) REFERENCES organization(id);


--
-- Name: organization_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY project_organization
    ADD CONSTRAINT organization_fk FOREIGN KEY (org_id) REFERENCES organization(id);


--
-- Name: organization_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY organization_organization_type
    ADD CONSTRAINT organization_fk FOREIGN KEY (org_id) REFERENCES organization(id) ON DELETE CASCADE;


--
-- Name: organization_type_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY organization_organization_type
    ADD CONSTRAINT organization_type_fk FOREIGN KEY (org_type_id) REFERENCES organization_type(id);


--
-- Name: pan_it_panel_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY panel_item
    ADD CONSTRAINT pan_it_panel_fk FOREIGN KEY (panel_id) REFERENCES panel(id) MATCH FULL;


--
-- Name: parent_test_sec_test_sect_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test_section
    ADD CONSTRAINT parent_test_sec_test_sect_fk FOREIGN KEY (parent_test_section) REFERENCES test_section(id) MATCH FULL;


--
-- Name: pat_id_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY patient_occupation
    ADD CONSTRAINT pat_id_fk FOREIGN KEY (patient_id) REFERENCES patient(id);


--
-- Name: pat_person_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY patient
    ADD CONSTRAINT pat_person_fk FOREIGN KEY (person_id) REFERENCES person(id) MATCH FULL;


--
-- Name: patient_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY patient_identity
    ADD CONSTRAINT patient_fk FOREIGN KEY (patient_id) REFERENCES patient(id);


--
-- Name: patient_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY patient_patient_type
    ADD CONSTRAINT patient_fk FOREIGN KEY (patient_id) REFERENCES patient(id);


--
-- Name: patient_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY observation_history
    ADD CONSTRAINT patient_fk FOREIGN KEY (patient_id) REFERENCES patient(id);


--
-- Name: patient_type_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY patient_patient_type
    ADD CONSTRAINT patient_type_fk FOREIGN KEY (patient_type_id) REFERENCES patient_type(id);


--
-- Name: person_address_address_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY person_address
    ADD CONSTRAINT person_address_address_fk FOREIGN KEY (address_part_id) REFERENCES address_part(id);


--
-- Name: person_address_person_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY person_address
    ADD CONSTRAINT person_address_person_fk FOREIGN KEY (person_id) REFERENCES person(id);


--
-- Name: pr_pat_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY patient_relations
    ADD CONSTRAINT pr_pat_fk FOREIGN KEY (pat_id) REFERENCES patient(id) MATCH FULL;


--
-- Name: pr_pat_source_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY patient_relations
    ADD CONSTRAINT pr_pat_source_fk FOREIGN KEY (pat_id_source) REFERENCES patient(id) MATCH FULL;


--
-- Name: project_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY project_organization
    ADD CONSTRAINT project_fk FOREIGN KEY (project_id) REFERENCES project(id);


--
-- Name: project_script_fk_ii; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY project
    ADD CONSTRAINT project_script_fk_ii FOREIGN KEY (scriptlet_id) REFERENCES scriptlet(id) MATCH FULL;


--
-- Name: project_sysuer_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY project
    ADD CONSTRAINT project_sysuer_fk FOREIGN KEY (sys_user_id) REFERENCES system_user(id) MATCH FULL;


--
-- Name: projectparam_proj_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY project_parameter
    ADD CONSTRAINT projectparam_proj_fk FOREIGN KEY (project_id) REFERENCES project(id) MATCH FULL;


--
-- Name: prov_person_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY provider
    ADD CONSTRAINT prov_person_fk FOREIGN KEY (person_id) REFERENCES person(id) MATCH FULL;


--
-- Name: qa_observation_type_k; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY qa_observation
    ADD CONSTRAINT qa_observation_type_k FOREIGN KEY (qa_observation_type_id) REFERENCES qa_observation_type(id);


--
-- Name: qaev_test_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY qa_event
    ADD CONSTRAINT qaev_test_fk FOREIGN KEY (test_id) REFERENCES test(id) MATCH FULL;


--
-- Name: qc_sys_user_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY qc
    ADD CONSTRAINT qc_sys_user_fk FOREIGN KEY (sys_user_id) REFERENCES system_user(id) MATCH FULL;


--
-- Name: qc_uom_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY qc
    ADD CONSTRAINT qc_uom_fk FOREIGN KEY (uom_id) REFERENCES unit_of_measure(id) MATCH FULL;


--
-- Name: qcanlyt_analyte_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY qc_analytes
    ADD CONSTRAINT qcanlyt_analyte_fk FOREIGN KEY (analyte_id) REFERENCES analyte(id) MATCH FULL;


--
-- Name: qrtz_blob_triggers_trigger_name_fkey; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY qrtz_blob_triggers
    ADD CONSTRAINT qrtz_blob_triggers_trigger_name_fkey FOREIGN KEY (trigger_name, trigger_group) REFERENCES qrtz_triggers(trigger_name, trigger_group);


--
-- Name: qrtz_cron_triggers_trigger_name_fkey; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY qrtz_cron_triggers
    ADD CONSTRAINT qrtz_cron_triggers_trigger_name_fkey FOREIGN KEY (trigger_name, trigger_group) REFERENCES qrtz_triggers(trigger_name, trigger_group);


--
-- Name: qrtz_job_listeners_job_name_fkey; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY qrtz_job_listeners
    ADD CONSTRAINT qrtz_job_listeners_job_name_fkey FOREIGN KEY (job_name, job_group) REFERENCES qrtz_job_details(job_name, job_group);


--
-- Name: qrtz_simple_triggers_trigger_name_fkey; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY qrtz_simple_triggers
    ADD CONSTRAINT qrtz_simple_triggers_trigger_name_fkey FOREIGN KEY (trigger_name, trigger_group) REFERENCES qrtz_triggers(trigger_name, trigger_group);


--
-- Name: qrtz_trigger_listeners_trigger_name_fkey; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY qrtz_trigger_listeners
    ADD CONSTRAINT qrtz_trigger_listeners_trigger_name_fkey FOREIGN KEY (trigger_name, trigger_group) REFERENCES qrtz_triggers(trigger_name, trigger_group);


--
-- Name: qrtz_triggers_job_name_fkey; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY qrtz_triggers
    ADD CONSTRAINT qrtz_triggers_job_name_fkey FOREIGN KEY (job_name, job_group) REFERENCES qrtz_job_details(job_name, job_group);


--
-- Name: receiver_code_code_element_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY receiver_code_element
    ADD CONSTRAINT receiver_code_code_element_fk FOREIGN KEY (code_element_type_id) REFERENCES code_element_type(id) MATCH FULL;


--
-- Name: receiver_code_message_org_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY receiver_code_element
    ADD CONSTRAINT receiver_code_message_org_fk FOREIGN KEY (message_org_id) REFERENCES message_org(id) MATCH FULL;


--
-- Name: referral_analysis_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY referral
    ADD CONSTRAINT referral_analysis_fk FOREIGN KEY (analysis_id) REFERENCES analysis(id);


--
-- Name: referral_organization_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY referral
    ADD CONSTRAINT referral_organization_fk FOREIGN KEY (organization_id) REFERENCES organization(id) ON DELETE CASCADE;


--
-- Name: referral_reason_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY referral
    ADD CONSTRAINT referral_reason_fk FOREIGN KEY (referral_reason_id) REFERENCES referral_reason(id);


--
-- Name: referral_result_referral_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY referral_result
    ADD CONSTRAINT referral_result_referral_fk FOREIGN KEY (referral_id) REFERENCES referral(id) ON DELETE CASCADE;


--
-- Name: referral_result_result; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY referral_result
    ADD CONSTRAINT referral_result_result FOREIGN KEY (result_id) REFERENCES result(id);


--
-- Name: referral_result_test_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY referral_result
    ADD CONSTRAINT referral_result_test_fk FOREIGN KEY (test_id) REFERENCES test(id);


--
-- Name: referral_type_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY referral
    ADD CONSTRAINT referral_type_fk FOREIGN KEY (referral_type_id) REFERENCES referral_type(id);


--
-- Name: report_queue_type_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY report_external_export
    ADD CONSTRAINT report_queue_type_fk FOREIGN KEY (type) REFERENCES report_queue_type(id);


--
-- Name: requester_type_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample_requester
    ADD CONSTRAINT requester_type_fk FOREIGN KEY (requester_type_id) REFERENCES requester_type(id);


--
-- Name: result_analysis_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY result
    ADD CONSTRAINT result_analysis_fk FOREIGN KEY (analysis_id) REFERENCES analysis(id) MATCH FULL;


--
-- Name: result_analyte_fk_ii; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY result
    ADD CONSTRAINT result_analyte_fk_ii FOREIGN KEY (analyte_id) REFERENCES analyte(id) MATCH FULL;


--
-- Name: result_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY result_inventory
    ADD CONSTRAINT result_fk FOREIGN KEY (result_id) REFERENCES result(id);


--
-- Name: result_id_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY result_signature
    ADD CONSTRAINT result_id_fk FOREIGN KEY (result_id) REFERENCES result(id);


--
-- Name: result_parent_id_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY result
    ADD CONSTRAINT result_parent_id_fk FOREIGN KEY (parent_id) REFERENCES result(id) ON DELETE CASCADE;


--
-- Name: result_testresult_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY result
    ADD CONSTRAINT result_testresult_fk FOREIGN KEY (test_result_id) REFERENCES test_result(id) MATCH FULL;


--
-- Name: role_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY system_user_role
    ADD CONSTRAINT role_fk FOREIGN KEY (role_id) REFERENCES system_role(id) ON DELETE CASCADE;


--
-- Name: role_parent_role_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY system_role
    ADD CONSTRAINT role_parent_role_fk FOREIGN KEY (grouping_parent) REFERENCES system_role(id);


--
-- Name: samp_org_org_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample_organization
    ADD CONSTRAINT samp_org_org_fk FOREIGN KEY (org_id) REFERENCES organization(id) MATCH FULL;


--
-- Name: samp_org_samp_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample_organization
    ADD CONSTRAINT samp_org_samp_fk FOREIGN KEY (samp_id) REFERENCES sample(id) MATCH FULL;


--
-- Name: samphuman_patient_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample_human
    ADD CONSTRAINT samphuman_patient_fk FOREIGN KEY (patient_id) REFERENCES patient(id) MATCH FULL;


--
-- Name: samphuman_provider_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample_human
    ADD CONSTRAINT samphuman_provider_fk FOREIGN KEY (provider_id) REFERENCES provider(id) MATCH FULL;


--
-- Name: samphuman_sample_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample_human
    ADD CONSTRAINT samphuman_sample_fk FOREIGN KEY (samp_id) REFERENCES sample(id) MATCH FULL;


--
-- Name: sampitem_sampitem_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample_item
    ADD CONSTRAINT sampitem_sampitem_fk FOREIGN KEY (sampitem_id) REFERENCES sample_item(id) MATCH FULL;


--
-- Name: sampitem_sample_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample_item
    ADD CONSTRAINT sampitem_sample_fk FOREIGN KEY (samp_id) REFERENCES sample(id) MATCH FULL;


--
-- Name: sampitem_sourceosamp_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample_item
    ADD CONSTRAINT sampitem_sourceosamp_fk FOREIGN KEY (source_id) REFERENCES source_of_sample(id) MATCH FULL;


--
-- Name: sampitem_typeosamp_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample_item
    ADD CONSTRAINT sampitem_typeosamp_fk FOREIGN KEY (typeosamp_id) REFERENCES type_of_sample(id) MATCH FULL;


--
-- Name: sampitem_uom_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample_item
    ADD CONSTRAINT sampitem_uom_fk FOREIGN KEY (uom_id) REFERENCES unit_of_measure(id) MATCH FULL;


--
-- Name: sample_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY observation_history
    ADD CONSTRAINT sample_fk FOREIGN KEY (sample_id) REFERENCES sample(id);


--
-- Name: sample_package_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample
    ADD CONSTRAINT sample_package_fk FOREIGN KEY (package_id) REFERENCES package_1(id) MATCH FULL;


--
-- Name: sample_qaevent_sampleitem_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample_qaevent
    ADD CONSTRAINT sample_qaevent_sampleitem_fk FOREIGN KEY (sampleitem_id) REFERENCES sample_item(id);


--
-- Name: sample_status_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample
    ADD CONSTRAINT sample_status_fk FOREIGN KEY (status_id) REFERENCES status_of_sample(id);


--
-- Name: sample_sysuser_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample
    ADD CONSTRAINT sample_sysuser_fk FOREIGN KEY (sys_user_id) REFERENCES system_user(id) MATCH FULL;


--
-- Name: sampletype_panel_panel_id_fkey; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sampletype_panel
    ADD CONSTRAINT sampletype_panel_panel_id_fkey FOREIGN KEY (panel_id) REFERENCES panel(id);


--
-- Name: sampletype_panel_sample_type_id_fkey; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sampletype_panel
    ADD CONSTRAINT sampletype_panel_sample_type_id_fkey FOREIGN KEY (sample_type_id) REFERENCES type_of_sample(id);


--
-- Name: sampletype_test_sample_type_id_fkey; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sampletype_test
    ADD CONSTRAINT sampletype_test_sample_type_id_fkey FOREIGN KEY (sample_type_id) REFERENCES type_of_sample(id);


--
-- Name: sampletype_test_test_id_fkey; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sampletype_test
    ADD CONSTRAINT sampletype_test_test_id_fkey FOREIGN KEY (test_id) REFERENCES test(id);


--
-- Name: sampnewborn_sample_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample_newborn
    ADD CONSTRAINT sampnewborn_sample_fk FOREIGN KEY (id) REFERENCES sample_human(id);


--
-- Name: sampproj_project_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample_projects
    ADD CONSTRAINT sampproj_project_fk FOREIGN KEY (proj_id) REFERENCES project(id) MATCH FULL;


--
-- Name: sampproj_sample_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY sample_projects
    ADD CONSTRAINT sampproj_sample_fk FOREIGN KEY (samp_id) REFERENCES sample(id) MATCH FULL;


--
-- Name: sci_name_comm_anim_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY animal_scientific_name
    ADD CONSTRAINT sci_name_comm_anim_fk FOREIGN KEY (comm_anim_id) REFERENCES animal_common_name(id) MATCH FULL;


--
-- Name: status_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY analyzer_results
    ADD CONSTRAINT status_fk FOREIGN KEY (status_id) REFERENCES analyzer_result_status(id);


--
-- Name: storloc_parent_storloc_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY storage_location
    ADD CONSTRAINT storloc_parent_storloc_fk FOREIGN KEY (parent_storageloc_id) REFERENCES storage_location(id) MATCH FULL;


--
-- Name: storloc_storunit_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY storage_location
    ADD CONSTRAINT storloc_storunit_fk FOREIGN KEY (storage_unit_id) REFERENCES storage_unit(id) MATCH FULL;


--
-- Name: sysrolemodule_sysmodule_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY system_role_module
    ADD CONSTRAINT sysrolemodule_sysmodule_fk FOREIGN KEY (system_module_id) REFERENCES system_module(id) MATCH FULL ON DELETE CASCADE;


--
-- Name: sysrolemodule_sysrole_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY system_role_module
    ADD CONSTRAINT sysrolemodule_sysrole_fk FOREIGN KEY (system_role_id) REFERENCES system_role(id) MATCH FULL ON DELETE CASCADE;


--
-- Name: system_user_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY system_user_role
    ADD CONSTRAINT system_user_fk FOREIGN KEY (system_user_id) REFERENCES system_user(id) ON DELETE CASCADE;


--
-- Name: system_user_id_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY result_signature
    ADD CONSTRAINT system_user_id_fk FOREIGN KEY (system_user_id) REFERENCES system_user(id);


--
-- Name: sysusermodule_sysmodule_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY system_user_module
    ADD CONSTRAINT sysusermodule_sysmodule_fk FOREIGN KEY (system_module_id) REFERENCES system_module(id) MATCH FULL;


--
-- Name: sysusermodule_sysuser_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY system_user_module
    ADD CONSTRAINT sysusermodule_sysuser_fk FOREIGN KEY (system_user_id) REFERENCES system_user(id) MATCH FULL;


--
-- Name: sysusersect_sysuser_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY system_user_section
    ADD CONSTRAINT sysusersect_sysuser_fk FOREIGN KEY (system_user_id) REFERENCES system_user(id) MATCH FULL;


--
-- Name: sysusersect_testsect_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY system_user_section
    ADD CONSTRAINT sysusersect_testsect_fk FOREIGN KEY (test_section_id) REFERENCES test_section(id) MATCH FULL;


--
-- Name: test_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY result_limits
    ADD CONSTRAINT test_fk FOREIGN KEY (test_id) REFERENCES test(id);


--
-- Name: test_hl7_encoding_id_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test_code
    ADD CONSTRAINT test_hl7_encoding_id_fk FOREIGN KEY (code_type_id) REFERENCES test_code_type(id);


--
-- Name: test_hl7_test_id_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test_code
    ADD CONSTRAINT test_hl7_test_id_fk FOREIGN KEY (test_id) REFERENCES test(id);


--
-- Name: test_label_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test
    ADD CONSTRAINT test_label_fk FOREIGN KEY (label_id) REFERENCES label(id) MATCH FULL;


--
-- Name: test_method_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test
    ADD CONSTRAINT test_method_fk FOREIGN KEY (method_id) REFERENCES method(id) MATCH FULL;


--
-- Name: test_reflex_scriptlet_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test_reflex
    ADD CONSTRAINT test_reflex_scriptlet_fk FOREIGN KEY (scriptlet_id) REFERENCES scriptlet(id);


--
-- Name: test_result_type_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY result_limits
    ADD CONSTRAINT test_result_type_fk FOREIGN KEY (test_result_type_id) REFERENCES type_of_test_result(id);


--
-- Name: test_scriptlet_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test
    ADD CONSTRAINT test_scriptlet_fk FOREIGN KEY (scriptlet_id) REFERENCES scriptlet(id) MATCH FULL;


--
-- Name: test_sect_org_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test_section
    ADD CONSTRAINT test_sect_org_fk FOREIGN KEY (org_id) REFERENCES organization(id) MATCH FULL;


--
-- Name: test_testformat_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test
    ADD CONSTRAINT test_testformat_fk FOREIGN KEY (test_format_id) REFERENCES test_formats(id) MATCH FULL;


--
-- Name: test_testsect_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test
    ADD CONSTRAINT test_testsect_fk FOREIGN KEY (test_section_id) REFERENCES test_section(id) MATCH FULL;


--
-- Name: test_testtrailer_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test
    ADD CONSTRAINT test_testtrailer_fk FOREIGN KEY (test_trailer_id) REFERENCES test_trailer(id) MATCH FULL;


--
-- Name: test_uom_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test
    ADD CONSTRAINT test_uom_fk FOREIGN KEY (uom_id) REFERENCES unit_of_measure(id) MATCH FULL;


--
-- Name: testalyt_analyte_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test_analyte
    ADD CONSTRAINT testalyt_analyte_fk FOREIGN KEY (analyte_id) REFERENCES analyte(id) MATCH FULL;


--
-- Name: testalyt_test_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test_analyte
    ADD CONSTRAINT testalyt_test_fk FOREIGN KEY (test_id) REFERENCES test(id) MATCH FULL;


--
-- Name: testreflex_addtest_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test_reflex
    ADD CONSTRAINT testreflex_addtest_fk FOREIGN KEY (add_test_id) REFERENCES test(id) MATCH FULL;


--
-- Name: testreflex_test_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test_reflex
    ADD CONSTRAINT testreflex_test_fk FOREIGN KEY (test_id) REFERENCES test(id) MATCH FULL;


--
-- Name: testreflex_testanalyt_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test_reflex
    ADD CONSTRAINT testreflex_testanalyt_fk FOREIGN KEY (test_analyte_id) REFERENCES test_analyte(id) MATCH FULL;


--
-- Name: testreflex_tstrslt_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test_reflex
    ADD CONSTRAINT testreflex_tstrslt_fk FOREIGN KEY (tst_rslt_id) REFERENCES test_result(id) MATCH FULL;


--
-- Name: testresult_scriptlet_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test_result
    ADD CONSTRAINT testresult_scriptlet_fk FOREIGN KEY (scriptlet_id) REFERENCES scriptlet(id) MATCH FULL;


--
-- Name: testresult_test_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test_result
    ADD CONSTRAINT testresult_test_fk FOREIGN KEY (test_id) REFERENCES test(id) MATCH FULL;


--
-- Name: tw_test_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test_worksheets
    ADD CONSTRAINT tw_test_fk FOREIGN KEY (test_id) REFERENCES test(id) MATCH FULL;


--
-- Name: twi_qc_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test_worksheet_item
    ADD CONSTRAINT twi_qc_fk FOREIGN KEY (qc_id) REFERENCES qc(id) MATCH FULL;


--
-- Name: twi_tw_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY test_worksheet_item
    ADD CONSTRAINT twi_tw_fk FOREIGN KEY (tw_id) REFERENCES test_worksheets(id) MATCH FULL;


--
-- Name: wkshtanalysis_wkshtitem_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY worksheet_analysis
    ADD CONSTRAINT wkshtanalysis_wkshtitem_fk FOREIGN KEY (worksheet_item_id) REFERENCES worksheet_item(id) MATCH FULL;


--
-- Name: wkshtanalyte_result_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY worksheet_analyte
    ADD CONSTRAINT wkshtanalyte_result_fk FOREIGN KEY (result_id) REFERENCES result(id) MATCH FULL;


--
-- Name: wkshtitem_wksht_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY worksheet_item
    ADD CONSTRAINT wkshtitem_wksht_fk FOREIGN KEY (worksheet_id) REFERENCES worksheets(id) MATCH FULL;


--
-- Name: wkshtqc_qcanalyte_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY worksheet_qc
    ADD CONSTRAINT wkshtqc_qcanalyte_fk FOREIGN KEY (qc_analyte_id) REFERENCES qc_analytes(id) MATCH FULL;


--
-- Name: wkshtqc_wkshtanalysis_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY worksheet_qc
    ADD CONSTRAINT wkshtqc_wkshtanalysis_fk FOREIGN KEY (worksheet_analysis_id) REFERENCES worksheet_analysis(id) MATCH FULL;


--
-- Name: workst_sys_user_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY worksheets
    ADD CONSTRAINT workst_sys_user_fk FOREIGN KEY (sys_user_id) REFERENCES system_user(id) MATCH FULL;


--
-- Name: workst_test_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY worksheets
    ADD CONSTRAINT workst_test_fk FOREIGN KEY (test_id) REFERENCES test(id) MATCH FULL;


--
-- Name: wrkst_anlt_wrkst_anls_fk; Type: FK CONSTRAINT; Schema: clinlims; Owner: clinlims
--

ALTER TABLE ONLY worksheet_analyte
    ADD CONSTRAINT wrkst_anlt_wrkst_anls_fk FOREIGN KEY (wrkst_anls_id) REFERENCES worksheet_analysis(id) MATCH FULL;


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;
