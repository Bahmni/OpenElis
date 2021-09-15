--
-- Note: This file has been generated from bahmni-base.dump using pg_restore command. This contains the default data for OpenElis Application.
--

--
-- TO-DO : Validate whether we need all of these data in fresh implementation of OpenElis
--

--
-- Data for Name: action; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY action (id, code, description, type, lastupdated) FROM stdin;
1	FSR	Fee sticker received	resolving	2007-08-21 16:48:38.434
2	CDC	Collection date corrected	resolving	2007-08-21 15:58:29.117
3	RQSOC	Request source corrected	resolving	2007-08-21 14:11:01.737
4	SNAC	Submitter name corrected	resolving	2007-08-21 14:21:11.696
26	DLRQR	Delayed request form received	internal	2008-01-11 04:17:09.054
25	CMRE	Communication reviewed	internal	2008-05-01 21:38:41.775
27	DURPS	Duplicate report to submitter	message	2008-01-11 04:22:59.507
28	SPDC	Specimen discarded	internal	2008-01-11 04:20:13.235
29	SCL	Submitter was called	message	2008-01-11 04:22:33.637
30	SPSOC	Specimen source corrected	internal	2008-01-11 04:24:33.863
31	DLRQRQ	Delayed request form requested	message	2008-01-11 04:46:58.057
32	RPDF	Report placed in dead file	internal	2008-01-11 04:47:25.498
33	RQIDC	Request form ID corrected	internal	2008-01-11 04:47:57.136
34	SPCA	Specimen canceled	internal	2008-01-11 04:48:25.614
35	SPIDC	Specimen ID corrected	internal	2008-01-11 04:48:47.451
36	SPUNS	Specimen declared unsatisfactory	internal	2008-01-11 04:49:13.262
\.


--
-- Name: action_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('action_seq', 45, false);


--
-- Data for Name: address_part; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY address_part (id, part_name, display_order, display_key) FROM stdin;
1	department	\N	address.department
2	commune	\N	address.commune
3	village	\N	address.village
4	fax	\N	address.fax
5	phone	\N	address.phone
6	street	\N	address.street
\.


--
-- Name: address_part_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('address_part_seq', 6, true);


--
-- Data for Name: analysis; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY analysis (id, sampitem_id, test_sect_id, test_id, revision, status, started_date, completed_date, released_date, printed_date, is_reportable, so_send_ready_date, so_client_reference, so_notify_received_date, so_notify_send_date, so_send_date, so_send_entry_by, so_send_entry_date, analysis_type, lastupdated, parent_analysis_id, parent_result_id, reflex_trigger, status_id, entry_date, panel_id) FROM stdin;
\.


--
-- Data for Name: analysis_qaevent; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY analysis_qaevent (id, qa_event_id, analysis_id, lastupdated, completed_date) FROM stdin;
\.


--
-- Data for Name: analysis_qaevent_action; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY analysis_qaevent_action (id, analysis_qaevent_id, action_id, created_date, lastupdated, sys_user_id) FROM stdin;
\.


--
-- Name: analysis_qaevent_action_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('analysis_qaevent_action_seq', 221, false);


--
-- Name: analysis_qaevent_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('analysis_qaevent_seq', 326, false);


--
-- Name: analysis_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('analysis_seq', 468, true);


--
-- Data for Name: analysis_storages; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY analysis_storages (id, storage_id, checkin, checkout, analysis_id) FROM stdin;
\.


--
-- Data for Name: analysis_users; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY analysis_users (id, action, analysis_id, system_user_id) FROM stdin;
\.


--
-- Data for Name: analyte; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY analyte (id, analyte_id, name, is_active, external_id, lastupdated, local_abbrev) FROM stdin;
44	\N	HIV-2 Result	Y	\N	2007-05-15 13:29:44.989	\N
62	\N	MEP Agar	Y	\N	2007-10-09 08:38:25.01	\N
63	\N	MYP Agar	Y	\N	2007-10-09 08:38:31.459	\N
64	\N	PLET Agar	Y	\N	2007-10-09 08:38:41.299	\N
65	\N	SBA Agar	Y	\N	2007-10-09 08:38:49.58	\N
67	\N	Extraction Kit	Y	\N	2007-12-10 09:04:09.124	\N
68	\N	Amplification Kit	Y	\N	2007-12-10 09:04:16.37	\N
69	\N	Light Cycler	Y	\N	2007-12-10 09:04:24.964	\N
70	\N	ABI 7000	Y	\N	2007-12-10 09:04:36.709	\N
71	\N	i Cycler	Y	\N	2007-12-10 09:04:56.821	\N
72	\N	ABI 7500	Y	\N	2007-12-10 09:05:04.01	\N
73	\N	DFA Capsule Antigen Detection	Y	\N	2007-12-10 09:05:41.24	\N
74	\N	DFA Cell Wall Detection	Y	\N	2007-12-10 09:06:56.313	\N
78	\N	Dilution 1:10	N	\N	2007-12-27 03:05:58.517	\N
89	\N	TRF Spore Detection Interpretation	Y	\N	2007-12-27 05:34:16.557	\N
90	\N	TRF Cell Detection Data Value	Y	\N	2007-12-27 05:34:57.18	\N
91	\N	TRF Cell Detection Result	Y	\N	2007-12-27 05:35:11.751	\N
92	\N	TRF Cell Detection Interpretation	Y	\N	2007-12-27 05:35:25.667	\N
1	\N	Influenza Virus B RNA	Y	\N	2007-02-02 14:09:33.524	\N
45	\N	Final HIV Interpretation 	Y	\N	2007-05-15 13:30:15.23	\N
47	\N	Penicillin	Y	\N	2007-05-18 09:41:04.107	\N
48	\N	Ceftriaxone	Y	\N	2007-05-18 09:41:14.295	\N
49	\N	Ciprofloxacin 	Y	\N	2007-05-18 09:43:17.764	\N
50	\N	Spectinomycin	Y	\N	2007-05-18 09:43:30.214	\N
43	\N	HIV-1 Result	Y	\N	2007-05-15 13:29:33.206	\N
51	\N	Tetracycline	Y	\N	2007-05-18 09:43:37.86	\N
52	\N	Cefixime	Y	\N	2007-05-18 09:43:52.144	\N
53	\N	Azithromycin	Y	\N	2007-05-18 09:44:01.789	\N
15	\N	Isoniazid 0.4 mcg.ml	Y	\N	2007-02-13 14:30:20.194	\N
16	\N	Streptomycin 2.0 mcg/ml	Y	\N	2007-02-13 14:30:43.17	\N
17	\N	Ethambutol 2.5 mcg/ml	Y	\N	2007-02-13 14:30:58.832	\N
18	\N	Pyrazinamide 100 mcg/ml	Y	\N	2007-02-13 14:32:46.073	\N
19	\N	DFA Capsule Antigen Detection from Isolate	Y	\N	2007-12-11 03:56:08.454	\N
8	\N	Respiratory Syncytial Virus A RNA	Y	\N	2007-02-02 14:13:44.532	\N
9	\N	Respiratory Syncytial Virus B RNA	Y	\N	2007-02-02 14:13:56.705	\N
20	\N	DFA Cell Wall Detection from Isolate	Y	\N	2007-12-11 03:56:47.987	\N
38	\N	Comment 8	Y	\N	2007-04-02 09:05:17.216	\N
41	\N	Titer	Y	\N	2007-05-03 15:59:07.522	\N
22	\N	Preliminary Result	Y	\N	2007-03-28 14:00:45.933	\N
23	\N	Result 4	Y	\N	2007-03-28 14:00:51.064	\N
24	\N	Result 5	Y	\N	2007-03-28 14:01:00.965	\N
25	\N	Result 6	Y	\N	2007-03-28 14:01:05.352	\N
26	\N	Result 7	Y	\N	2007-03-28 14:01:16.014	\N
27	\N	Result 8	Y	\N	2007-03-28 14:01:20.594	\N
28	\N	Result 9	Y	\N	2007-03-28 14:01:31.066	\N
29	\N	Preliminary Result Modifier	Y	\N	2007-03-28 16:22:57.579	\N
30	\N	Final Result Modifier	Y	\N	2007-03-28 16:22:21.34	\N
208	\N	Influenza Virus A/H3 RNA	Y	\N	2006-09-07 08:32:05	\N
209	\N	Influenza Virus A/H5 RNA	Y	\N	2006-09-07 08:32:21	\N
210	\N	Influenza Virus A/H7 RNA	Y	\N	2006-10-13 10:34:13.207	\N
211	\N	Influenza Virus A/H9 RNA	Y	\N	2008-01-23 08:02:52.021	\N
212	\N	Final Result	Y	\N	2006-09-07 08:34:31	\N
213	\N	Presumptive Result	Y	\N	2006-09-07 08:34:40	\N
214	\N	Result 1	Y	\N	2006-11-07 08:11:16	\N
215	\N	Result 2	Y	\N	2006-10-20 13:28:24	\N
216	\N	Result 3	Y	\N	2006-10-20 13:28:30	\N
217	\N	Interpretation	Y	\N	2006-09-07 08:35:29	\N
21	\N	Comment	Y	\N	2007-02-27 11:09:27.335	\N
54	\N	Gentamycin Interpretation	Y	\N	2007-06-20 10:01:45.134	\N
220	\N	BAND GP160	Y	\N	2006-10-18 09:19:37.724	\N
221	\N	BAND GP120	Y	\N	2006-10-18 09:19:38.505	\N
222	\N	BAND P65	Y	\N	2006-09-07 08:39:02	\N
223	\N	BAND P55	Y	\N	2006-09-07 08:39:12	\N
224	\N	BAND P51	Y	\N	2006-09-07 08:39:22	\N
225	\N	BAND GP41	Y	\N	2006-10-18 09:19:37.302	\N
226	\N	BAND P40	Y	\N	2006-11-07 13:55:36.669	\N
227	\N	BAND P31	Y	\N	2006-11-06 13:15:38.449	\N
228	\N	BAND P24	Y	\N	2006-11-06 13:15:29.222	\N
229	\N	BAND P18	Y	\N	2006-11-08 08:09:44.805	\N
246	\N	Rnase P Interpretation	Y	\N	2006-09-18 10:27:21	\N
251	\N	Result Status	Y	\N	2006-10-10 09:27:45	\N
252	\N	Probability	Y	\N	2006-10-03 10:24:02	\N
271	\N	Modifier 1	Y	\N	2006-10-20 09:28:32	\N
272	\N	Modifier 2	Y	\N	2006-10-20 09:28:37	\N
273	\N	Modifier 3	Y	\N	2006-10-20 09:28:42	\N
274	\N	Result Status 1	Y	\N	2006-10-20 09:28:51	\N
275	\N	Result Status 2	Y	\N	2006-10-20 09:28:59	\N
276	\N	Result Status 3	Y	\N	2006-10-20 09:29:06	\N
206	\N	Influenza Virus A RNA	Y	\N	2007-02-02 14:19:32.675	\N
207	\N	Influenza Virus A/H1 RNA	Y	\N	2006-09-07 08:31:50	\N
240	\N	BA2 CT value	Y	\N	2006-10-18 09:19:43.005	\N
244	\N	Extraction Method	Y	\N	2006-09-20 16:26:23	\N
245	\N	16S Interpretation	Y	\N	2006-10-25 11:21:09.457	\N
40	\N	Agent	Y	\N	2007-04-12 09:42:50.799	\N
249	\N	Modifier	Y	\N	2006-10-02 10:23:08	\N
250	\N	Quantity	Y	\N	2006-10-03 09:12:43	\N
253	\N	Method	Y	\N	2006-11-08 10:28:43	\N
266	\N	Capsule M-Fadyean	Y	\N	2006-10-11 13:51:30	\N
234	\N	BA3 interpretation	Y	\N	2006-10-18 09:19:39.302	\N
235	\N	16S CT value	Y	\N	2007-06-18 08:49:51.801	\N
232	\N	BA1 interpretation	Y	\N	2006-10-18 09:19:44.442	\N
233	\N	BA2 interpretation	Y	\N	2006-10-18 09:19:42.067	\N
236	\N	Rnase P CT value	Y	\N	2006-10-13 09:57:57.558	\N
237	\N	Result	Y	\N	2006-09-18 10:03:34	\N
46	\N	Western Blot Interpretation	Y	\N	2007-05-15 13:48:03.305	\N
239	\N	BA1 CT value	Y	\N	2006-10-23 14:12:56.284	\N
241	\N	BA3 CT value	Y	\N	2006-10-18 09:19:40.583	\N
242	\N	TRF Spore Detection Dilution	Y	\N	2007-12-27 05:33:28.883	\N
243	\N	TRF Cell Detection Dilution	Y	\N	2007-12-27 05:34:38.238	\N
247	\N	Disclaimer	Y	\N	2006-09-20 16:26:06	\N
256	\N	Rifampin 2.0 mcg/ml	Y	\N	2006-10-10 09:29:37	\N
55	\N	Kanamycin Interpretation	Y	\N	2007-06-20 10:02:26.74	\N
255	\N	Rifampin 1.0 mcg/ml	Y	\N	2006-10-10 09:29:21	\N
257	\N	Isoniazid 0.1 mcg/ml	Y	\N	2006-10-10 09:29:54	\N
258	\N	Colony Morphology	Y	\N	2006-10-11 13:49:58	\N
259	\N	Hemolysis	Y	\N	2006-10-11 13:50:05	\N
260	\N	Gram stain	Y	\N	2006-10-11 13:50:12	\N
261	\N	Motility wet mount	Y	\N	2006-10-11 13:50:35	\N
262	\N	Gamma phage	Y	\N	2006-10-11 13:50:43	\N
263	\N	DFA capsule from specimen	N	\N	2007-12-10 09:06:14.825	\N
264	\N	DFA cell wall from specimen	N	\N	2007-12-10 09:06:14.846	\N
265	\N	Capsule India Ink	Y	\N	2006-10-11 13:51:06	\N
267	\N	Capsule bicarbonate	Y	\N	2006-10-11 13:51:40	\N
268	\N	Catalase	Y	\N	2006-10-11 13:51:46	\N
269	\N	Malachite green for spores	Y	\N	2006-10-11 13:51:57	\N
270	\N	Wet mount for spores	Y	\N	2006-10-11 13:52:38	\N
31	\N	Comment 1	Y	\N	2007-04-02 09:04:22.529	\N
32	\N	Comment 2	Y	\N	2007-04-02 09:04:28.801	\N
33	\N	Comment 3	Y	\N	2007-04-02 09:04:41.088	\N
34	\N	Comment 4	Y	\N	2007-04-02 09:04:46.92	\N
35	\N	Comment 5	Y	\N	2007-04-02 09:04:56.112	\N
36	\N	Comment 6	Y	\N	2007-04-02 09:05:01.84	\N
37	\N	Comment 7	Y	\N	2007-04-02 09:05:08.789	\N
39	\N	Comment 9	Y	\N	2007-04-02 09:05:21.5	\N
56	\N	Gentamycin	Y	\N	2007-06-20 10:02:52.199	\N
57	\N	Kanamycin	Y	\N	2007-06-20 10:03:02.449	\N
58	\N	Motility Standard Media	Y	\N	2007-10-02 08:38:33.138	\N
59	\N	Standard Motility Media	Y	\N	2007-10-09 08:37:56.727	\N
60	\N	Chocolate Agar	Y	\N	2007-10-09 08:38:11.08	\N
61	\N	DEA Agar	Y	\N	2007-10-09 08:38:17.659	\N
66	\N	Test Moiety	Y	\N	2007-12-10 09:03:38.415	\N
75	\N	Choose Equipment	Y	\N	2007-12-11 05:35:57.035	\N
76	\N	Previous FTA Reactivity	Y	\N	2007-12-27 02:40:54.886	\N
77	\N	Fluorescence Grading	Y	\N	2007-12-27 02:41:18.215	\N
79	\N	E. coli 25922	Y	\N	2007-12-27 03:34:33.647	\N
80	\N	P. aeruginosa	Y	\N	2007-12-27 03:39:41.842	\N
81	\N	S. aureus	Y	\N	2007-12-27 03:34:57.55	\N
82	\N	Susceptible	N	\N	2007-12-27 03:41:45.362	\N
83	\N	Nonsusceptible - Contact CDC for confirmation of resistance	N	\N	2007-12-27 03:41:30.012	\N
84	\N	Test Not Performed	N	\N	2007-12-27 03:41:50.671	\N
85	\N	No Pass	Y	\N	2007-12-27 03:36:31.375	\N
86	\N	Pass	Y	\N	2007-12-27 03:36:35.561	\N
87	\N	TRF Spore Detection Date Value	Y	\N	2007-12-27 05:33:47.18	\N
88	\N	TRF Spore Detection Result	Y	\N	2007-12-27 05:34:05.441	\N
93	\N	test kit	Y		2009-04-03 10:15:45.64	TESTKIT
94	\N	Conclusion	Y	\N	2010-10-28 06:12:42.031482	\N
95	\N	generated CD4 Count	Y	\N	2010-10-28 06:13:55.508252	\N
96	\N	VIH Test - Collodial Gold/Shangai Kehua Result	Y	\N	2011-02-02 11:55:53.383208	\N
97	\N	Determine Result	Y	\N	2011-02-02 11:55:53.383208	\N
\.


--
-- Name: analyte_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('analyte_seq', 97, true);


--
-- Data for Name: analyzer; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY analyzer (id, scrip_id, name, machine_id, description, analyzer_type, is_active, location, lastupdated) FROM stdin;
1	\N	sysmex	1	bootstrap machine	\N	t	\N	2009-11-25 15:35:31.343118
3	\N	cobas_integra	\N	cobas_integra	\N	t	\N	2009-12-14 15:35:31.34118
\.


--
-- Data for Name: analyzer_result_status; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY analyzer_result_status (id, name, description) FROM stdin;
1	NOT_REVIEWED	The result has not yet been reviewed by the user
2	ACCEPTED	The result has been reviewed and accepted by the user
3	DECLINED	The result has been reviewed and not accepted by the user
4	MATCHING_ACCESSION_NOT_FOUND	The Lab No does not exist in the system
5	MATCHING_TEST_NOT_FOUND	The Lab No exists but the test has not been entered
6	TEST_MAPPING_NOT_FOUND	The test name from the analyzer is not recognized
7	ERROR	The result sent from the analyzer can not be understood
\.


--
-- Data for Name: analyzer_results; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY analyzer_results (id, analyzer_id, accession_number, test_name, result, units, status_id, iscontrol, lastupdated, read_only, test_id, duplicate_id, positive, complete_date, test_result_type) FROM stdin;
\.


--
-- Name: analyzer_results_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('analyzer_results_seq', 1, false);


--
-- Name: analyzer_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('analyzer_seq', 1, true);


--
-- Data for Name: analyzer_test_map; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY analyzer_test_map (analyzer_id, analyzer_test_name, test_id, lastupdated) FROM stdin;
\.


--
-- Data for Name: animal_common_name; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY animal_common_name (id, name) FROM stdin;
\.


--
-- Data for Name: animal_scientific_name; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY animal_scientific_name (id, comm_anim_id, name) FROM stdin;
\.


--
-- Data for Name: attachment; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY attachment (id, attach_type, filename, description, storage_reference) FROM stdin;
\.


--
-- Data for Name: attachment_item; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY attachment_item (id, reference_id, reference_table_id, attachment_id) FROM stdin;
\.


--
-- Data for Name: aux_data; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY aux_data (id, sort_order, is_reportable, auxdata_type, value, reference_id, reference_table, aux_field_id) FROM stdin;
\.


--
-- Data for Name: aux_field; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY aux_field (id, sort_order, auxfld_type, is_active, is_reportable, reference_table, analyte_id, scriptlet_id) FROM stdin;
\.


--
-- Data for Name: aux_field_values; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY aux_field_values (id, auxfldval_type, value, aux_field_id) FROM stdin;
\.


--
-- Name: city_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('city_seq', 1, false);


--
-- Data for Name: city_state_zip; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY city_state_zip (id, city, state, zip_code, county_fips, county, region_id, region, state_fips, state_name, lastupdated) FROM stdin;
\.


--
-- Data for Name: code_element_type; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY code_element_type (id, text, lastupdated, local_reference_table) FROM stdin;
1	TEST	2007-03-07 15:27:58.72	5
2	STATUS OF SAMPLE	2007-03-07 15:28:22.718	40
\.


--
-- Name: code_element_type_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('code_element_type_seq', 21, false);


--
-- Data for Name: code_element_xref; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY code_element_xref (id, message_org_id, code_element_type_id, receiver_code_element_id, local_code_element_id, lastupdated) FROM stdin;
\.


--
-- Name: code_element_xref_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('code_element_xref_seq', 41, false);


--
-- Data for Name: contact_type; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY contact_type (id, description, ct_type, is_unique) FROM stdin;
\.


--
-- Name: county_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('county_seq', 1, false);


--
-- Data for Name: databasechangelog; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY databasechangelog (id, author, filename, dateexecuted, md5sum, description, comments, tag, liquibase) FROM stdin;
\.


--
-- Data for Name: databasechangeloglock; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY databasechangeloglock (id, locked, lockgranted, lockedby) FROM stdin;
1	f	\N	\N
\.


--
-- Data for Name: dictionary; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY dictionary (id, is_active, dict_entry, lastupdated, local_abbrev, dictionary_category_id, display_key) FROM stdin;
\.


--
-- Data for Name: dictionary_category; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY dictionary_category (id, description, lastupdated, local_abbrev, name) FROM stdin;
\.


--
-- Name: dictionary_category_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('dictionary_category_seq', 196, true);


--
-- Name: dictionary_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('dictionary_seq', 1199, true);


--
-- Data for Name: district; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY district (id, city_id, dist_entry, lastupdated, description) FROM stdin;
\.


--
-- Name: district_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('district_seq', 1, false);


--
-- Data for Name: document_track; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY document_track (id, table_id, row_id, document_type_id, parent_id, report_generation_time, lastupdated, name) FROM stdin;
\.


--
-- Name: document_track_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('document_track_seq', 57, true);


--
-- Data for Name: document_type; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY document_type (id, name, description, lastupdated) FROM stdin;
1	nonConformityNotification	Non_Conformity reports to be sent to clinic	2012-04-24 00:30:14.523972+00
2	resultExport	Results sent electronically to other systems	2012-04-24 00:30:14.678323+00
3	malariaCase	malaria case report sent	2012-05-01 16:46:25.085126+00
4	patientReport	Any patient report, name in report_tracker	2013-08-08 08:02:35.04225+00
\.


--
-- Name: document_type_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('document_type_seq', 4, true);


--
-- Data for Name: ethnicity; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY ethnicity (id, ethnic_type, description, is_active) FROM stdin;
\.


--
-- Data for Name: gender; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY gender (id, gender_type, description, lastupdated, name_key) FROM stdin;
145	M	MALE	2006-10-10 13:18:40.094	gender.male
146	F	FEMALE	2006-11-21 12:04:02.372	gender.female
\.


--
-- Name: gender_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('gender_seq', 1, false);


--
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('hibernate_sequence', 4819, true);


--
-- Data for Name: history; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY history (id, sys_user_id, reference_id, reference_table, "timestamp", activity, changes) FROM stdin;
\.


--
-- Name: history_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('history_seq', 1400, true);


--
-- Name: hl7_encoding_type_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('hl7_encoding_type_seq', 4, true);


--
-- Data for Name: htmldb_plan_table; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY htmldb_plan_table (statement_id, plan_id, "timestamp", remarks, operation, options, object_node, object_owner, object_name, object_alias, object_instance, object_type, optimizer, search_columns, id, parent_id, depth, "position", cost, cardinality, bytes, other_tag, partition_start, partition_stop, partition_id, other, distribution, cpu_cost, io_cost, temp_space, access_predicates, filter_predicates, projection, "time", qblock_name) FROM stdin;
7146420525171748	1	2008-05-08 14:33:14	\N	SELECT STATEMENT	\N	\N	\N	\N	\N	\N	\N	ALL_ROWS	\N	0	\N	0	238	238	79529	5726088	\N	\N	\N	\N	\N	\N	33895495	232	\N	\N	\N	\N	3	\N
7146420525171748	1	2008-05-08 14:33:14	<remarks><info type="db_version">10.2.0.1</info><info type="parse_schema"><![CDATA["CLINLIMS"]]></info><info type="plan_hash">288471584</info><outline_data><hint><![CDATA[FULL(@"SEL$1" "CITY_STATE_ZIP"@"SEL$1")]]></hint><hint><![CDATA[OUTLINE_LEAF(@"SEL$1")]]></hint><hint><![CDATA[ALL_ROWS]]></hint><hint><![CDATA[OPTIMIZER_FEATURES_ENABLE('10.2.0.1')]]></hint><hint><![CDATA[IGNORE_OPTIM_EMBEDDED_HINTS]]></hint></outline_data></remarks>	TABLE ACCESS	FULL	\N	CLINLIMS	CITY_STATE_ZIP	CITY_STATE_ZIP@SEL$1	1	TABLE	ANALYZED	\N	1	0	1	1	238	79529	5726088	\N	\N	\N	\N	\N	\N	33895495	232	\N	\N	\N	"CITY_STATE_ZIP"."ID"[NUMBER,22], "CITY_STATE_ZIP"."CITY"[VARCHAR2,30], "CITY_STATE_ZIP"."STATE"[VARCHAR2,2], "CITY_STATE_ZIP"."ZIP_CODE"[VARCHAR2,10], "CITY_STATE_ZIP"."COUNTY_FIPS"[NUMBER,22], "CITY_STATE_ZIP"."COUNTY"[VARCHAR2,25], "CITY_STATE_ZIP"."REGION_ID"[NUMBER,22], "CITY_STATE_ZIP"."REGION"[VARCHAR2,30], "CITY_STATE_ZIP"."STATE_FIPS"[NUMBER,22], "CITY_STATE_ZIP"."STATE_NAME"[VARCHAR2,30], "CITY_STATE_ZIP"."LASTUPDATED"[TIMESTAMP,11]	3	SEL$1
7146722603172428	2	2008-05-08 14:33:20	\N	SELECT STATEMENT	\N	\N	\N	\N	\N	\N	\N	ALL_ROWS	\N	0	\N	0	238	238	79529	5726088	\N	\N	\N	\N	\N	\N	33895495	232	\N	\N	\N	\N	3	\N
7146722603172428	2	2008-05-08 14:33:20	<remarks><info type="db_version">10.2.0.1</info><info type="parse_schema"><![CDATA["CLINLIMS"]]></info><info type="plan_hash">288471584</info><outline_data><hint><![CDATA[FULL(@"SEL$1" "CITY_STATE_ZIP"@"SEL$1")]]></hint><hint><![CDATA[OUTLINE_LEAF(@"SEL$1")]]></hint><hint><![CDATA[ALL_ROWS]]></hint><hint><![CDATA[OPTIMIZER_FEATURES_ENABLE('10.2.0.1')]]></hint><hint><![CDATA[IGNORE_OPTIM_EMBEDDED_HINTS]]></hint></outline_data></remarks>	TABLE ACCESS	FULL	\N	CLINLIMS	CITY_STATE_ZIP	CITY_STATE_ZIP@SEL$1	1	TABLE	ANALYZED	\N	1	0	1	1	238	79529	5726088	\N	\N	\N	\N	\N	\N	33895495	232	\N	\N	\N	"CITY_STATE_ZIP"."ID"[NUMBER,22], "CITY_STATE_ZIP"."CITY"[VARCHAR2,30], "CITY_STATE_ZIP"."STATE"[VARCHAR2,2], "CITY_STATE_ZIP"."ZIP_CODE"[VARCHAR2,10], "CITY_STATE_ZIP"."COUNTY_FIPS"[NUMBER,22], "CITY_STATE_ZIP"."COUNTY"[VARCHAR2,25], "CITY_STATE_ZIP"."REGION_ID"[NUMBER,22], "CITY_STATE_ZIP"."REGION"[VARCHAR2,30], "CITY_STATE_ZIP"."STATE_FIPS"[NUMBER,22], "CITY_STATE_ZIP"."STATE_NAME"[VARCHAR2,30], "CITY_STATE_ZIP"."LASTUPDATED"[TIMESTAMP,11]	3	SEL$1
7146420525171748	1	2008-05-08 14:33:14	\N	SELECT STATEMENT	\N	\N	\N	\N	\N	\N	\N	ALL_ROWS	\N	0	\N	0	238	238	79529	5726088	\N	\N	\N	\N	\N	\N	33895495	232	\N	\N	\N	\N	3	\N
7146420525171748	1	2008-05-08 14:33:14	<remarks><info type="db_version">10.2.0.1</info><info type="parse_schema"><![CDATA["CLINLIMS"]]></info><info type="plan_hash">288471584</info><outline_data><hint><![CDATA[FULL(@"SEL$1" "CITY_STATE_ZIP"@"SEL$1")]]></hint><hint><![CDATA[OUTLINE_LEAF(@"SEL$1")]]></hint><hint><![CDATA[ALL_ROWS]]></hint><hint><![CDATA[OPTIMIZER_FEATURES_ENABLE('10.2.0.1')]]></hint><hint><![CDATA[IGNORE_OPTIM_EMBEDDED_HINTS]]></hint></outline_data></remarks>	TABLE ACCESS	FULL	\N	CLINLIMS	CITY_STATE_ZIP	CITY_STATE_ZIP@SEL$1	1	TABLE	ANALYZED	\N	1	0	1	1	238	79529	5726088	\N	\N	\N	\N	\N	\N	33895495	232	\N	\N	\N	"CITY_STATE_ZIP"."ID"[NUMBER,22], "CITY_STATE_ZIP"."CITY"[VARCHAR2,30], "CITY_STATE_ZIP"."STATE"[VARCHAR2,2], "CITY_STATE_ZIP"."ZIP_CODE"[VARCHAR2,10], "CITY_STATE_ZIP"."COUNTY_FIPS"[NUMBER,22], "CITY_STATE_ZIP"."COUNTY"[VARCHAR2,25], "CITY_STATE_ZIP"."REGION_ID"[NUMBER,22], "CITY_STATE_ZIP"."REGION"[VARCHAR2,30], "CITY_STATE_ZIP"."STATE_FIPS"[NUMBER,22], "CITY_STATE_ZIP"."STATE_NAME"[VARCHAR2,30], "CITY_STATE_ZIP"."LASTUPDATED"[TIMESTAMP,11]	3	SEL$1
7146722603172428	2	2008-05-08 14:33:20	\N	SELECT STATEMENT	\N	\N	\N	\N	\N	\N	\N	ALL_ROWS	\N	0	\N	0	238	238	79529	5726088	\N	\N	\N	\N	\N	\N	33895495	232	\N	\N	\N	\N	3	\N
7146722603172428	2	2008-05-08 14:33:20	<remarks><info type="db_version">10.2.0.1</info><info type="parse_schema"><![CDATA["CLINLIMS"]]></info><info type="plan_hash">288471584</info><outline_data><hint><![CDATA[FULL(@"SEL$1" "CITY_STATE_ZIP"@"SEL$1")]]></hint><hint><![CDATA[OUTLINE_LEAF(@"SEL$1")]]></hint><hint><![CDATA[ALL_ROWS]]></hint><hint><![CDATA[OPTIMIZER_FEATURES_ENABLE('10.2.0.1')]]></hint><hint><![CDATA[IGNORE_OPTIM_EMBEDDED_HINTS]]></hint></outline_data></remarks>	TABLE ACCESS	FULL	\N	CLINLIMS	CITY_STATE_ZIP	CITY_STATE_ZIP@SEL$1	1	TABLE	ANALYZED	\N	1	0	1	1	238	79529	5726088	\N	\N	\N	\N	\N	\N	33895495	232	\N	\N	\N	"CITY_STATE_ZIP"."ID"[NUMBER,22], "CITY_STATE_ZIP"."CITY"[VARCHAR2,30], "CITY_STATE_ZIP"."STATE"[VARCHAR2,2], "CITY_STATE_ZIP"."ZIP_CODE"[VARCHAR2,10], "CITY_STATE_ZIP"."COUNTY_FIPS"[NUMBER,22], "CITY_STATE_ZIP"."COUNTY"[VARCHAR2,25], "CITY_STATE_ZIP"."REGION_ID"[NUMBER,22], "CITY_STATE_ZIP"."REGION"[VARCHAR2,30], "CITY_STATE_ZIP"."STATE_FIPS"[NUMBER,22], "CITY_STATE_ZIP"."STATE_NAME"[VARCHAR2,30], "CITY_STATE_ZIP"."LASTUPDATED"[TIMESTAMP,11]	3	SEL$1
7146420525171748	1	2008-05-08 14:33:14	\N	SELECT STATEMENT	\N	\N	\N	\N	\N	\N	\N	ALL_ROWS	\N	0	\N	0	238	238	79529	5726088	\N	\N	\N	\N	\N	\N	33895495	232	\N	\N	\N	\N	3	\N
7146420525171748	1	2008-05-08 14:33:14	<remarks><info type="db_version">10.2.0.1</info><info type="parse_schema"><![CDATA["CLINLIMS"]]></info><info type="plan_hash">288471584</info><outline_data><hint><![CDATA[FULL(@"SEL$1" "CITY_STATE_ZIP"@"SEL$1")]]></hint><hint><![CDATA[OUTLINE_LEAF(@"SEL$1")]]></hint><hint><![CDATA[ALL_ROWS]]></hint><hint><![CDATA[OPTIMIZER_FEATURES_ENABLE('10.2.0.1')]]></hint><hint><![CDATA[IGNORE_OPTIM_EMBEDDED_HINTS]]></hint></outline_data></remarks>	TABLE ACCESS	FULL	\N	CLINLIMS	CITY_STATE_ZIP	CITY_STATE_ZIP@SEL$1	1	TABLE	ANALYZED	\N	1	0	1	1	238	79529	5726088	\N	\N	\N	\N	\N	\N	33895495	232	\N	\N	\N	"CITY_STATE_ZIP"."ID"[NUMBER,22], "CITY_STATE_ZIP"."CITY"[VARCHAR2,30], "CITY_STATE_ZIP"."STATE"[VARCHAR2,2], "CITY_STATE_ZIP"."ZIP_CODE"[VARCHAR2,10], "CITY_STATE_ZIP"."COUNTY_FIPS"[NUMBER,22], "CITY_STATE_ZIP"."COUNTY"[VARCHAR2,25], "CITY_STATE_ZIP"."REGION_ID"[NUMBER,22], "CITY_STATE_ZIP"."REGION"[VARCHAR2,30], "CITY_STATE_ZIP"."STATE_FIPS"[NUMBER,22], "CITY_STATE_ZIP"."STATE_NAME"[VARCHAR2,30], "CITY_STATE_ZIP"."LASTUPDATED"[TIMESTAMP,11]	3	SEL$1
7146722603172428	2	2008-05-08 14:33:20	\N	SELECT STATEMENT	\N	\N	\N	\N	\N	\N	\N	ALL_ROWS	\N	0	\N	0	238	238	79529	5726088	\N	\N	\N	\N	\N	\N	33895495	232	\N	\N	\N	\N	3	\N
7146722603172428	2	2008-05-08 14:33:20	<remarks><info type="db_version">10.2.0.1</info><info type="parse_schema"><![CDATA["CLINLIMS"]]></info><info type="plan_hash">288471584</info><outline_data><hint><![CDATA[FULL(@"SEL$1" "CITY_STATE_ZIP"@"SEL$1")]]></hint><hint><![CDATA[OUTLINE_LEAF(@"SEL$1")]]></hint><hint><![CDATA[ALL_ROWS]]></hint><hint><![CDATA[OPTIMIZER_FEATURES_ENABLE('10.2.0.1')]]></hint><hint><![CDATA[IGNORE_OPTIM_EMBEDDED_HINTS]]></hint></outline_data></remarks>	TABLE ACCESS	FULL	\N	CLINLIMS	CITY_STATE_ZIP	CITY_STATE_ZIP@SEL$1	1	TABLE	ANALYZED	\N	1	0	1	1	238	79529	5726088	\N	\N	\N	\N	\N	\N	33895495	232	\N	\N	\N	"CITY_STATE_ZIP"."ID"[NUMBER,22], "CITY_STATE_ZIP"."CITY"[VARCHAR2,30], "CITY_STATE_ZIP"."STATE"[VARCHAR2,2], "CITY_STATE_ZIP"."ZIP_CODE"[VARCHAR2,10], "CITY_STATE_ZIP"."COUNTY_FIPS"[NUMBER,22], "CITY_STATE_ZIP"."COUNTY"[VARCHAR2,25], "CITY_STATE_ZIP"."REGION_ID"[NUMBER,22], "CITY_STATE_ZIP"."REGION"[VARCHAR2,30], "CITY_STATE_ZIP"."STATE_FIPS"[NUMBER,22], "CITY_STATE_ZIP"."STATE_NAME"[VARCHAR2,30], "CITY_STATE_ZIP"."LASTUPDATED"[TIMESTAMP,11]	3	SEL$1
7146420525171748	1	2008-05-08 14:33:14	\N	SELECT STATEMENT	\N	\N	\N	\N	\N	\N	\N	ALL_ROWS	\N	0	\N	0	238	238	79529	5726088	\N	\N	\N	\N	\N	\N	33895495	232	\N	\N	\N	\N	3	\N
7146420525171748	1	2008-05-08 14:33:14	<remarks><info type="db_version">10.2.0.1</info><info type="parse_schema"><![CDATA["CLINLIMS"]]></info><info type="plan_hash">288471584</info><outline_data><hint><![CDATA[FULL(@"SEL$1" "CITY_STATE_ZIP"@"SEL$1")]]></hint><hint><![CDATA[OUTLINE_LEAF(@"SEL$1")]]></hint><hint><![CDATA[ALL_ROWS]]></hint><hint><![CDATA[OPTIMIZER_FEATURES_ENABLE('10.2.0.1')]]></hint><hint><![CDATA[IGNORE_OPTIM_EMBEDDED_HINTS]]></hint></outline_data></remarks>	TABLE ACCESS	FULL	\N	CLINLIMS	CITY_STATE_ZIP	CITY_STATE_ZIP@SEL$1	1	TABLE	ANALYZED	\N	1	0	1	1	238	79529	5726088	\N	\N	\N	\N	\N	\N	33895495	232	\N	\N	\N	"CITY_STATE_ZIP"."ID"[NUMBER,22], "CITY_STATE_ZIP"."CITY"[VARCHAR2,30], "CITY_STATE_ZIP"."STATE"[VARCHAR2,2], "CITY_STATE_ZIP"."ZIP_CODE"[VARCHAR2,10], "CITY_STATE_ZIP"."COUNTY_FIPS"[NUMBER,22], "CITY_STATE_ZIP"."COUNTY"[VARCHAR2,25], "CITY_STATE_ZIP"."REGION_ID"[NUMBER,22], "CITY_STATE_ZIP"."REGION"[VARCHAR2,30], "CITY_STATE_ZIP"."STATE_FIPS"[NUMBER,22], "CITY_STATE_ZIP"."STATE_NAME"[VARCHAR2,30], "CITY_STATE_ZIP"."LASTUPDATED"[TIMESTAMP,11]	3	SEL$1
7146722603172428	2	2008-05-08 14:33:20	\N	SELECT STATEMENT	\N	\N	\N	\N	\N	\N	\N	ALL_ROWS	\N	0	\N	0	238	238	79529	5726088	\N	\N	\N	\N	\N	\N	33895495	232	\N	\N	\N	\N	3	\N
7146722603172428	2	2008-05-08 14:33:20	<remarks><info type="db_version">10.2.0.1</info><info type="parse_schema"><![CDATA["CLINLIMS"]]></info><info type="plan_hash">288471584</info><outline_data><hint><![CDATA[FULL(@"SEL$1" "CITY_STATE_ZIP"@"SEL$1")]]></hint><hint><![CDATA[OUTLINE_LEAF(@"SEL$1")]]></hint><hint><![CDATA[ALL_ROWS]]></hint><hint><![CDATA[OPTIMIZER_FEATURES_ENABLE('10.2.0.1')]]></hint><hint><![CDATA[IGNORE_OPTIM_EMBEDDED_HINTS]]></hint></outline_data></remarks>	TABLE ACCESS	FULL	\N	CLINLIMS	CITY_STATE_ZIP	CITY_STATE_ZIP@SEL$1	1	TABLE	ANALYZED	\N	1	0	1	1	238	79529	5726088	\N	\N	\N	\N	\N	\N	33895495	232	\N	\N	\N	"CITY_STATE_ZIP"."ID"[NUMBER,22], "CITY_STATE_ZIP"."CITY"[VARCHAR2,30], "CITY_STATE_ZIP"."STATE"[VARCHAR2,2], "CITY_STATE_ZIP"."ZIP_CODE"[VARCHAR2,10], "CITY_STATE_ZIP"."COUNTY_FIPS"[NUMBER,22], "CITY_STATE_ZIP"."COUNTY"[VARCHAR2,25], "CITY_STATE_ZIP"."REGION_ID"[NUMBER,22], "CITY_STATE_ZIP"."REGION"[VARCHAR2,30], "CITY_STATE_ZIP"."STATE_FIPS"[NUMBER,22], "CITY_STATE_ZIP"."STATE_NAME"[VARCHAR2,30], "CITY_STATE_ZIP"."LASTUPDATED"[TIMESTAMP,11]	3	SEL$1
7146420525171748	1	2008-05-08 14:33:14	\N	SELECT STATEMENT	\N	\N	\N	\N	\N	\N	\N	ALL_ROWS	\N	0	\N	0	238	238	79529	5726088	\N	\N	\N	\N	\N	\N	33895495	232	\N	\N	\N	\N	3	\N
7146420525171748	1	2008-05-08 14:33:14	<remarks><info type="db_version">10.2.0.1</info><info type="parse_schema"><![CDATA["CLINLIMS"]]></info><info type="plan_hash">288471584</info><outline_data><hint><![CDATA[FULL(@"SEL$1" "CITY_STATE_ZIP"@"SEL$1")]]></hint><hint><![CDATA[OUTLINE_LEAF(@"SEL$1")]]></hint><hint><![CDATA[ALL_ROWS]]></hint><hint><![CDATA[OPTIMIZER_FEATURES_ENABLE('10.2.0.1')]]></hint><hint><![CDATA[IGNORE_OPTIM_EMBEDDED_HINTS]]></hint></outline_data></remarks>	TABLE ACCESS	FULL	\N	CLINLIMS	CITY_STATE_ZIP	CITY_STATE_ZIP@SEL$1	1	TABLE	ANALYZED	\N	1	0	1	1	238	79529	5726088	\N	\N	\N	\N	\N	\N	33895495	232	\N	\N	\N	"CITY_STATE_ZIP"."ID"[NUMBER,22], "CITY_STATE_ZIP"."CITY"[VARCHAR2,30], "CITY_STATE_ZIP"."STATE"[VARCHAR2,2], "CITY_STATE_ZIP"."ZIP_CODE"[VARCHAR2,10], "CITY_STATE_ZIP"."COUNTY_FIPS"[NUMBER,22], "CITY_STATE_ZIP"."COUNTY"[VARCHAR2,25], "CITY_STATE_ZIP"."REGION_ID"[NUMBER,22], "CITY_STATE_ZIP"."REGION"[VARCHAR2,30], "CITY_STATE_ZIP"."STATE_FIPS"[NUMBER,22], "CITY_STATE_ZIP"."STATE_NAME"[VARCHAR2,30], "CITY_STATE_ZIP"."LASTUPDATED"[TIMESTAMP,11]	3	SEL$1
7146722603172428	2	2008-05-08 14:33:20	\N	SELECT STATEMENT	\N	\N	\N	\N	\N	\N	\N	ALL_ROWS	\N	0	\N	0	238	238	79529	5726088	\N	\N	\N	\N	\N	\N	33895495	232	\N	\N	\N	\N	3	\N
7146722603172428	2	2008-05-08 14:33:20	<remarks><info type="db_version">10.2.0.1</info><info type="parse_schema"><![CDATA["CLINLIMS"]]></info><info type="plan_hash">288471584</info><outline_data><hint><![CDATA[FULL(@"SEL$1" "CITY_STATE_ZIP"@"SEL$1")]]></hint><hint><![CDATA[OUTLINE_LEAF(@"SEL$1")]]></hint><hint><![CDATA[ALL_ROWS]]></hint><hint><![CDATA[OPTIMIZER_FEATURES_ENABLE('10.2.0.1')]]></hint><hint><![CDATA[IGNORE_OPTIM_EMBEDDED_HINTS]]></hint></outline_data></remarks>	TABLE ACCESS	FULL	\N	CLINLIMS	CITY_STATE_ZIP	CITY_STATE_ZIP@SEL$1	1	TABLE	ANALYZED	\N	1	0	1	1	238	79529	5726088	\N	\N	\N	\N	\N	\N	33895495	232	\N	\N	\N	"CITY_STATE_ZIP"."ID"[NUMBER,22], "CITY_STATE_ZIP"."CITY"[VARCHAR2,30], "CITY_STATE_ZIP"."STATE"[VARCHAR2,2], "CITY_STATE_ZIP"."ZIP_CODE"[VARCHAR2,10], "CITY_STATE_ZIP"."COUNTY_FIPS"[NUMBER,22], "CITY_STATE_ZIP"."COUNTY"[VARCHAR2,25], "CITY_STATE_ZIP"."REGION_ID"[NUMBER,22], "CITY_STATE_ZIP"."REGION"[VARCHAR2,30], "CITY_STATE_ZIP"."STATE_FIPS"[NUMBER,22], "CITY_STATE_ZIP"."STATE_NAME"[VARCHAR2,30], "CITY_STATE_ZIP"."LASTUPDATED"[TIMESTAMP,11]	3	SEL$1
7146420525171748	1	2008-05-08 14:33:14	\N	SELECT STATEMENT	\N	\N	\N	\N	\N	\N	\N	ALL_ROWS	\N	0	\N	0	238	238	79529	5726088	\N	\N	\N	\N	\N	\N	33895495	232	\N	\N	\N	\N	3	\N
7146420525171748	1	2008-05-08 14:33:14	<remarks><info type="db_version">10.2.0.1</info><info type="parse_schema"><![CDATA["CLINLIMS"]]></info><info type="plan_hash">288471584</info><outline_data><hint><![CDATA[FULL(@"SEL$1" "CITY_STATE_ZIP"@"SEL$1")]]></hint><hint><![CDATA[OUTLINE_LEAF(@"SEL$1")]]></hint><hint><![CDATA[ALL_ROWS]]></hint><hint><![CDATA[OPTIMIZER_FEATURES_ENABLE('10.2.0.1')]]></hint><hint><![CDATA[IGNORE_OPTIM_EMBEDDED_HINTS]]></hint></outline_data></remarks>	TABLE ACCESS	FULL	\N	CLINLIMS	CITY_STATE_ZIP	CITY_STATE_ZIP@SEL$1	1	TABLE	ANALYZED	\N	1	0	1	1	238	79529	5726088	\N	\N	\N	\N	\N	\N	33895495	232	\N	\N	\N	"CITY_STATE_ZIP"."ID"[NUMBER,22], "CITY_STATE_ZIP"."CITY"[VARCHAR2,30], "CITY_STATE_ZIP"."STATE"[VARCHAR2,2], "CITY_STATE_ZIP"."ZIP_CODE"[VARCHAR2,10], "CITY_STATE_ZIP"."COUNTY_FIPS"[NUMBER,22], "CITY_STATE_ZIP"."COUNTY"[VARCHAR2,25], "CITY_STATE_ZIP"."REGION_ID"[NUMBER,22], "CITY_STATE_ZIP"."REGION"[VARCHAR2,30], "CITY_STATE_ZIP"."STATE_FIPS"[NUMBER,22], "CITY_STATE_ZIP"."STATE_NAME"[VARCHAR2,30], "CITY_STATE_ZIP"."LASTUPDATED"[TIMESTAMP,11]	3	SEL$1
7146722603172428	2	2008-05-08 14:33:20	\N	SELECT STATEMENT	\N	\N	\N	\N	\N	\N	\N	ALL_ROWS	\N	0	\N	0	238	238	79529	5726088	\N	\N	\N	\N	\N	\N	33895495	232	\N	\N	\N	\N	3	\N
7146722603172428	2	2008-05-08 14:33:20	<remarks><info type="db_version">10.2.0.1</info><info type="parse_schema"><![CDATA["CLINLIMS"]]></info><info type="plan_hash">288471584</info><outline_data><hint><![CDATA[FULL(@"SEL$1" "CITY_STATE_ZIP"@"SEL$1")]]></hint><hint><![CDATA[OUTLINE_LEAF(@"SEL$1")]]></hint><hint><![CDATA[ALL_ROWS]]></hint><hint><![CDATA[OPTIMIZER_FEATURES_ENABLE('10.2.0.1')]]></hint><hint><![CDATA[IGNORE_OPTIM_EMBEDDED_HINTS]]></hint></outline_data></remarks>	TABLE ACCESS	FULL	\N	CLINLIMS	CITY_STATE_ZIP	CITY_STATE_ZIP@SEL$1	1	TABLE	ANALYZED	\N	1	0	1	1	238	79529	5726088	\N	\N	\N	\N	\N	\N	33895495	232	\N	\N	\N	"CITY_STATE_ZIP"."ID"[NUMBER,22], "CITY_STATE_ZIP"."CITY"[VARCHAR2,30], "CITY_STATE_ZIP"."STATE"[VARCHAR2,2], "CITY_STATE_ZIP"."ZIP_CODE"[VARCHAR2,10], "CITY_STATE_ZIP"."COUNTY_FIPS"[NUMBER,22], "CITY_STATE_ZIP"."COUNTY"[VARCHAR2,25], "CITY_STATE_ZIP"."REGION_ID"[NUMBER,22], "CITY_STATE_ZIP"."REGION"[VARCHAR2,30], "CITY_STATE_ZIP"."STATE_FIPS"[NUMBER,22], "CITY_STATE_ZIP"."STATE_NAME"[VARCHAR2,30], "CITY_STATE_ZIP"."LASTUPDATED"[TIMESTAMP,11]	3	SEL$1
\.


--
-- Data for Name: instrument; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY instrument (id, scrip_id, name, description, instru_type, is_active, location) FROM stdin;
\.


--
-- Data for Name: instrument_analyte; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY instrument_analyte (id, analyte_id, instru_id, method_id, result_group) FROM stdin;
\.


--
-- Data for Name: instrument_log; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY instrument_log (id, instru_id, instlog_type, event_begin, event_end) FROM stdin;
\.


--
-- Data for Name: inventory_component; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY inventory_component (id, invitem_id, quantity, material_component_id) FROM stdin;
\.


--
-- Data for Name: inventory_item; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY inventory_item (id, uom_id, name, description, quantity_min_level, quantity_max_level, quantity_to_reorder, is_reorder_auto, is_lot_maintained, is_active, average_lead_time, average_cost, average_daily_use) FROM stdin;
20	\N	HIV testKit	HIV	\N	\N	\N	\N	\N	Y	\N	\N	\N
21	\N	SyphlisTK	SYPHILIS	\N	\N	\N	\N	\N	Y	\N	\N	\N
\.


--
-- Name: inventory_item_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('inventory_item_seq', 21, true);


--
-- Data for Name: inventory_location; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY inventory_location (id, storage_id, lot_number, quantity_onhand, expiration_date, inv_item_id) FROM stdin;
19	\N	1	\N	2011-08-12 00:00:00	20
20	\N	1	\N	\N	21
\.


--
-- Name: inventory_location_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('inventory_location_seq', 20, true);


--
-- Data for Name: inventory_receipt; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY inventory_receipt (id, invitem_id, received_date, quantity_received, unit_cost, qc_reference, external_reference, org_id) FROM stdin;
\.


--
-- Name: inventory_receipt_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('inventory_receipt_seq', 20, true);


--
-- Data for Name: lab_order_item; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY lab_order_item (id, lab_order_type_id, table_ref, record_id, identifier, action, lastupdated) FROM stdin;
\.


--
-- Name: lab_order_item_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('lab_order_item_seq', 1, false);


--
-- Data for Name: lab_order_type; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY lab_order_type (id, domain, type, context, description, sort_order, lastupdated, display_key) FROM stdin;
\.


--
-- Name: lab_order_type_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('lab_order_type_seq', 1, false);


--
-- Data for Name: label; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY label (id, name, description, printer_type, scriptlet_id, lastupdated) FROM stdin;
62	MOLECULAR EPI - TEST	TEST LABEL FOR MOLECULAR EPIDEMIOLOGY	P	13	2008-05-01 21:25:56.575
65	labelname1	labeldescr1	P	12	2006-11-20 15:31:47
68	testa	test	t	11	2006-12-13 10:06:03.873
69	testb	test	t	12	2006-12-13 10:05:58.435
70	testc	test	t	12	2006-12-13 10:05:43.107
60	Diane Test	Diane Test	P	11	2008-05-01 21:26:17.387
61	VIROLOGY-TEST	TEST LABELS FOR VIROLOGY	P	12	2006-09-07 08:06:40
71	12	12	1	11	2006-12-13 10:56:53.37
67	test	test	t	12	2006-12-13 10:05:04.56
64	NO LABEL	NO LABEL	P	\N	2006-10-25 08:09:35
66	Label Name 2	Label Desc 2	P	13	2008-05-05 23:13:30.414
1	aaa	aaa	\N	\N	2007-10-11 09:33:32.059
2	a	a	\N	\N	2007-10-10 16:45:24.842
\.


--
-- Name: label_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('label_seq', 3, false);


--
-- Data for Name: login_user; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY login_user (id, login_name, password, password_expired_dt, account_locked, account_disabled, is_admin, user_time_out) FROM stdin;
130	user	FVSAQzka8nEbrZUyGU3iTQ==	2030-12-31	N	N	N	480
1	admin	n2OrWHXVm/BQsgd1YZJoCA==	2030-12-31	N	N	Y	220
\.


--
-- Name: login_user_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('login_user_seq', 147, true);


--
-- Data for Name: menu; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY menu (id, parent_id, presentation_order, element_id, action_url, click_action, display_key, tool_tip_key, new_window) FROM stdin;
107	\N	1	menu_home	/Dashboard.do	\N	banner.menu.home	tooltip.bannner.menu.home	f
108	\N	2	menu_sample	\N	\N	banner.menu.sample	tooltip.bannner.menu.sample	f
109	108	1	menu_sample_add	/SamplePatientEntry.do	\N	banner.menu.sampleAdd	tooltip.bannner.menu.sampleAdd	f
110	108	2	menu_sample_edit	/SampleEdit.do?type=readwrite	\N	banner.menu.sampleEdit	tooltip.banner.menu.sampleEdit	f
111	\N	3	menu_patient	\N	\N	banner.menu.patient	tooltip.bannner.menu.patient	f
112	111	1	menu_patient_add_or_edit	/PatientManagement.do	\N	banner.menu.patient.addOrEdit	tooltip.banner.menu.patient.addOrEdit	f
113	\N	4	menu_nonconformity	/NonConformity.do	\N	banner.menu.nonconformity	tooltip.banner.menu.nonconformity	f
114	\N	5	menu_workplan	\N	\N	banner.menu.workplan	tooltip.bannner.menu.workplan	f
115	114	1	menu_workplan_test	/WorkPlanByTest.do?type=test	\N	banner.menu.workplan.test	tooltip.bannner.menu.workplan.test	f
116	114	2	menu_workplan_panel	/WorkPlanByPanel.do?type=panel	\N	banner.menu.workplan.panel	tooltip.bannner.menu.workplan.panel	f
117	114	3	menu_workplan_bench	\N	\N	banner.menu.workplan.bench	tooltip.bannner.menu.workplan.bench	f
118	117	10	menu_workplan_hematology	/WorkPlanByTestSection.do?type=hematology	\N	banner.menu.results.logbook.hemato	tooltip.banner.menu.results.logbook.hemato	f
119	117	20	menu_workplan_chem	/WorkPlanByTestSection.do?type=chem	\N	banner.menu.results.logbook.chem	tooltip.banner.menu.results.logbook.chem	f
120	117	40	menu_workplan_cytobacteriology	/WorkPlanByTestSection.do?type=cytobacteriology	\N	banner.menu.results.logbook.bacteria	tooltip.banner.menu.results.logbook.bacteria	f
121	117	70	menu_workplan_parasitology	/WorkPlanByTestSection.do?type=parasitology	\N	banner.menu.results.logbook.parasitology	tooltip.banner.menu.results.logbook.parasitology	f
122	117	80	menu_workplan_immuno	/WorkPlanByTestSection.do?type=immuno	\N	banner.menu.results.logbook.immunoSerology	tooltip.banner.menu.results.logbook.immunoSerology	f
123	117	60	menu_workplan_ecbu	/WorkPlanByTestSection.do?type=ECBU	\N	banner.menu.results.logbook.ecbu	tooltip.banner.menu.results.logbook.ecbu	f
124	117	50	menu_workplan_bacteria	/WorkPlanByTestSection.do?type=bacteriology	\N	banner.menu.results.logbook.bacteria.root	banner.menu.results.logbook.bacteria.root	f
125	117	110	menu_workplan_liquidBio	/WorkPlanByTestSection.do?type=liquidBio	\N	banner.menu.results.logbook.liquidBio	tooltip.banner.menu.results.logbook.liquidBio	f
126	117	90	menu_workplan_mycrobacteriology	/WorkPlanByTestSection.do?type=mycrobacteriology	\N	banner.menu.results.logbook.mycobacteriology	tooltip.banner.menu.results.logbook.mycobacteriology	f
127	117	100	menu_workplan_endocrin	/WorkPlanByTestSection.do?type=endocrin	\N	banner.menu.results.logbook.endocrinology	tooltip.banner.menu.results.logbook.endocrinology	f
128	117	120	menu_workplan_serology	/WorkPlanByTestSection.do?type=serologie	\N	banner.menu.results.logbook.serology	tooltip.banner.menu.results.logbook.serology	f
129	117	130	menu_workplan_HIV	/WorkPlanByTestSection.do?type=HIV	\N	banner.menu.results.logbook.vct	tooltip.banner.menu.results.logbook.vct	f
130	\N	6	menu_results	\N	\N	banner.menu.results	tooltip.bannner.menu.results	f
131	130	1	menu_results_logbook	\N	\N	banner.menu.results.logbook	tooltip.banner.menu.results.logbook	f
132	131	10	menu_results_hematology	/LogbookResults.do?type=hematology	\N	banner.menu.results.logbook.hemato	tooltip.banner.menu.results.logbook.hemato	f
133	131	20	menu_results_chem	/LogbookResults.do?type=chem	\N	banner.menu.results.logbook.chem	tooltip.banner.menu.results.logbook.chem	f
134	131	40	menu_results_bacteria	/LogbookResults.do?type=cytobacteriology	\N	banner.menu.results.logbook.bacteria	tooltip.banner.menu.results.logbook.bacteria	f
135	131	70	menu_results_parasitology	/LogbookResults.do?type=parasitology	\N	banner.menu.results.logbook.parasitology	tooltip.banner.menu.results.logbook.parasitology	f
136	131	80	menu_results_immunology	/LogbookResults.do?type=immuno	\N	banner.menu.results.logbook.immunoSerology	tooltip.banner.menu.results.logbook.immunoSerology	f
137	131	60	menu_results_ecbu	/LogbookResults.do?type=ECBU	\N	banner.menu.results.logbook.ecbu	tooltip.banner.menu.results.logbook.ecbu	f
138	131	50	menu_results_bacerteria	/LogbookResults.do?type=bacteriology	\N	banner.menu.results.logbook.bacteria.root	banner.menu.results.logbook.bacteria.root	f
139	131	110	menu_results_liquid_bio	/LogbookResults.do?type=liquidBio	\N	banner.menu.results.logbook.liquidBio	tooltip.banner.menu.results.logbook.liquidBio	f
140	131	90	menu_results_mycoBacteria	/LogbookResults.do?type=mycobacteriology	\N	banner.menu.results.logbook.mycobacteriology	tooltip.banner.menu.results.logbook.mycobacteriology	f
141	131	100	menu_results_endocrin	/LogbookResults.do?type=endocrin	\N	banner.menu.results.logbook.endocrinology	tooltip.banner.menu.results.logbook.endocrinology	f
142	131	120	menu_results_serology	/LogbookResults.do?type=serologie	\N	banner.menu.results.logbook.serology	tooltip.banner.menu.results.logbook.serology	f
143	131	130	menu_results_vct	/LogbookResults.do?type=HIV	\N	banner.menu.results.logbook.vct	tooltip.banner.menu.results.logbook.vct	f
144	130	2	menu_results_search	\N	\N	banner.menu.results.search	tooltip.banner.menu.results.search	f
145	144	1	menu_results_patient	/PatientResults.do	\N	banner.menu.results.patient	tooltip.banner.menu.results.patient	f
146	144	2	menu_results_accession	/AccessionResults.do	\N	banner.menu.results.accession	tooltip.banner.menu.results.accession	f
147	144	3	menu_results_status	/StatusResults.do?blank=true	\N	banner.menu.results.status	tooltip.banner.menu.results.status	f
148	\N	7	menu_resultvalidation	\N	\N	banner.menu.resultvalidation	tooltip.banner.menu.resultvalidation	f
149	148	10	menu_resultvalidation_hematology	/ResultValidation.do?type=hematology&test=	\N	banner.menu.resultvalidation.hematology	tooltip.banner.menu.resultvalidation.hematology	f
150	148	20	menu_resultvalidation_biochemistry	/ResultValidation.do?type=biochemistry&test=	\N	banner.menu.resultvalidation.biochemistry	banner.menu.resultvalidation.biochemistry	f
151	148	40	menu_resultvalidation_cytobacteriology	/ResultValidation.do?type=Cytobacteriologie&test=	\N	banner.menu.resultvalidation.cytobacteriology	banner.menu.resultvalidation.cytobacteriology	f
152	148	60	menu_resultvalidation_ecbu	/ResultValidation.do?type=ECBU&test=	\N	banner.menu.resultvalidation.ecbu	banner.menu.resultvalidation.ecbu	f
153	148	70	menu_resultvalidation_parasitology	/ResultValidation.do?type=Parasitology&test=	\N	banner.menu.resultvalidation.parasitology	banner.menu.resultvalidation.parasitology	f
154	148	80	menu_resultvalidation_immuno_verology	/ResultValidation.do?type=immunology&test=	\N	banner.menu.resultvalidation.immunology	banner.menu.resultvalidation.immunology	f
155	148	50	menu_resultvalidation_bacteriology	/ResultValidation.do?type=Bacteria&test=	\N	banner.menu.resultvalidation.bacteria	banner.menu.resultvalidation.bacteria	f
156	148	110	menu_resultvalidation_liquid_biology	/ResultValidation.do?type=Liquides biologique&test=	\N	banner.menu.resultvalidation.liquidBio	banner.menu.resultvalidation.liquidBio	f
157	148	90	menu_resultvalidation_mycobacteriology	/ResultValidation.do?type=Mycobacteriology&test=	\N	banner.menu.resultvalidation.mycobacteriology	banner.menu.resultvalidation.mycobacteriology	f
158	148	100	menu_resultvalidation_endocrinologie	/ResultValidation.do?type=Endocrinologie&test=	\N	banner.menu.resultvalidation.endocrinologie	banner.menu.resultvalidation.endocrinologie	f
159	148	120	menu_resultvalidation_serology	/ResultValidation.do?type=Serologie&test=	\N	banner.menu.resultvalidation.serology	banner.menu.resultvalidation.serology	f
160	148	130	menu_resultvalidation_CDV	/ResultValidation.do?type=VCT&test=	\N	banner.menu.resultvalidation.VCT	banner.menu.resultvalidation.VCT	f
161	\N	8	menu_inventory	/ManageInventory.do	\N	banner.menu.inventory	tooltip.banner.menu.inventory	f
162	\N	9	menu_reports	\N	\N	banner.menu.reports	tooltip.banner.menu.reports	f
163	162	1	menu_reports_status	\N	\N	openreports.statusreports.title	tooltip.openreports.statusreports.title	f
164	163	1	menu_reports_status_patient	/Report.do?type=patient&report=patientHaitiClinical	\N	openreports.patientTestStatus	tooltip.openreports.patientTestStatus	f
165	162	2	menu_reports_aggregate	\N	\N	openreports.aggregate.title	tooltip.openreports.aggregate.title	f
166	165	1	menu_reports_aggregate_hiv	/Report.do?type=indicator&report=indicatorHaitiClinicalHIV	\N	openreports.hiv.aggregate	tooltip.openreports.hiv.aggregate	f
167	165	2	menu_reports_aggregate_all	/Report.do?type=indicator&report=indicatorHaitiClinicalAllTests	\N	openreports.all.tests.aggregate	tooltip.openreports.all.tests.aggregate	f
168	162	3	menu_reports_nonconformity	\N	\N	reports.nonConformity.menu	tooltip.reports.nonConformity.menu	f
169	168	1	menu_reports_nonconformity_date	/Report.do?type=patient&report=haitiClinicalNonConformityByDate	\N	reports.nonConformity.byDate.report	tooltip.reports.nonConformity.byDate.report	f
170	168	2	menu_reports_nonconformity_section	/Report.do?type=patient&report=haitiClinicalNonConformityBySectionReason	\N	reports.nonConformity.bySectionReason.report	tooltip.reports.nonConformity.bySectionReason.report	f
171	162	5	menu_reports_auditTrail	/AuditTrailReport.do	\N	reports.auditTrail	reports.auditTrail	f
172	\N	10	menu_administration	/MasterListsPage.do	\N	banner.menu.administration	tooltip.banner.menu.administration	f
173	\N	11	menu_help	/documentation/HaitiClinical_fr.pdf	\N	banner.menu.help	tooltip.banner.menu.help	t
\.


--
-- Name: menu_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('menu_seq', 173, true);


--
-- Data for Name: message_org; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY message_org (id, org_id, is_active, active_begin, active_end, description, lastupdated) FROM stdin;
\.


--
-- Name: message_org_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('message_org_seq', 41, false);


--
-- Name: messagecontrolidseq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('messagecontrolidseq', 1, false);


--
-- Data for Name: method; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY method (id, name, description, reporting_description, is_active, active_begin, active_end, lastupdated) FROM stdin;
1	EIA 	Enzyme-linked immunoassay	EIA 	Y	2007-04-24 00:00:00	\N	2007-04-24 13:46:47.063
31	PCR	Polymerase Chain Reaction	\N	Y	2006-09-18 00:00:00	\N	2006-09-29 14:32:51
32	STAIN	Stain	\N	Y	2006-09-29 00:00:00	\N	2006-09-29 14:32:57
33	CULTURE	Culture	\N	Y	2006-09-29 00:00:00	\N	2006-11-01 08:10:49.606
34	PROBE	Probe	\N	Y	2006-09-29 00:00:00	\N	2006-09-29 14:33:05
35	BIOCHEMICAL	Biochemical	\N	Y	2006-09-29 00:00:00	\N	2006-11-08 09:16:24.377
27	Diane Test	Diane Test	\N	Y	2006-09-06 00:00:00	\N	2006-10-23 15:35:39.534
36	HPLC	High Pressure Liquid Chromatography	\N	Y	2006-09-29 00:00:00	\N	2006-09-29 14:31:50
37	DNA SEQUENCING	DNA Sequencing	\N	Y	2006-09-29 00:00:00	\N	2006-10-23 15:35:40.691
3	AUTO	Automated (Haiti)		Y	2009-02-24 00:00:00	\N	2009-02-24 16:26:17.507
4	MANUAL	test done manually (Haiti)		Y	2009-02-24 00:00:00	\N	2009-02-24 16:26:47.604
5	HIV_TEST_KIT	Uses Hiv test kit		Y	2009-03-05 00:00:00	\N	2009-03-05 14:26:19.46
6	SYPHILIS_TEST_KIT	Test kit for syphilis		Y	2009-03-05 00:00:00	\N	2009-03-05 14:28:11.61
\.


--
-- Data for Name: method_analyte; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY method_analyte (id, method_id, analyte_id, result_group, sort_order, ma_type) FROM stdin;
\.


--
-- Data for Name: method_result; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY method_result (id, scrip_id, result_group, flags, methres_type, value, quant_limit, cont_level, method_id) FROM stdin;
\.


--
-- Name: method_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('method_seq', 6, true);


--
-- Data for Name: mls_lab_type; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY mls_lab_type (id, description, org_mlt_org_mlt_id) FROM stdin;
\.


--
-- Data for Name: note; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY note (id, sys_user_id, reference_id, reference_table, note_type, subject, text, lastupdated) FROM stdin;
\.


--
-- Name: note_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('note_seq', 140, true);


--
-- Data for Name: observation_history; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY observation_history (id, patient_id, sample_id, observation_history_type_id, value_type, value, lastupdated, sample_item_id) FROM stdin;
\.


--
-- Name: observation_history_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('observation_history_seq', 1, false);


--
-- Data for Name: observation_history_type; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY observation_history_type (id, type_name, description, lastupdated) FROM stdin;
1	initialSampleCondition	The condition of the sample when it was delievered to the lab	2011-02-16 22:48:42.513601+00
2	paymentStatus	The payment status of the patient	2012-04-24 00:30:14.756638+00
\.


--
-- Name: observation_history_type_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('observation_history_type_seq', 2, true);


--
-- Data for Name: occupation; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY occupation (id, occupation, lastupdated) FROM stdin;
\.


--
-- Name: occupation_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('occupation_seq', 1, false);


--
-- Data for Name: or_properties; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY or_properties (property_id, property_key, property_value) FROM stdin;
1870	base.directory	/usr/share/tomcat5.5/webapps/openreports/reports/
1871	temp.directory	/usr/share/tomcat5.5/webapps/openreports/temp/
1872	report.generation.directory	/usr/share/tomcat5.5/webapps/openreports/generatedreports/
1873	date.format	dd/MM/yyyy
1874	mail.auth.password	barLAC28
1875	mail.auth.user	admin
1876	mail.smtp.auth	false
1877	mail.smtp.host	
1878	xmla.catalog	
1879	xmla.datasource	
1880	xmla.uri	
\.


--
-- Data for Name: or_tags; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY or_tags (tag_id, tagged_object_id, tagged_object_class, tag_value, tag_type) FROM stdin;
\.


--
-- Data for Name: order_item; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY order_item (id, ord_id, quantity_requested, quantity_received, inv_loc_id) FROM stdin;
\.


--
-- Data for Name: orders; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY orders (id, org_id, sys_user_id, ordered_date, neededby_date, requested_by, cost_center, shipping_type, shipping_carrier, shipping_cost, delivered_date, is_external, external_order_number, is_filled) FROM stdin;
\.


--
-- Data for Name: org_hl7_encoding_type; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY org_hl7_encoding_type (organization_id, encoding_type_id, lastupdated) FROM stdin;
\.


--
-- Data for Name: org_mls_lab_type; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY org_mls_lab_type (org_id, mls_lab_id, org_mlt_id) FROM stdin;
\.


--
-- Data for Name: organization; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY organization (id, name, city, zip_code, mls_sentinel_lab_flag, org_mlt_org_mlt_id, org_id, short_name, multiple_unit, street_address, state, internet_address, clia_num, pws_id, lastupdated, mls_lab_flag, is_active, local_abbrev) FROM stdin;
3	Haiti	Seattle	98103	N	\N	\N			DNA	WA			\N	2008-11-20 13:48:42.141	N	Y	22
\.


--
-- Data for Name: organization_address; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY organization_address (organization_id, address_part_id, type, value) FROM stdin;
\.


--
-- Data for Name: organization_contact; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY organization_contact (id, organization_id, person_id, "position") FROM stdin;
\.


--
-- Name: organization_contact_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('organization_contact_seq', 1, false);


--
-- Data for Name: organization_organization_type; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY organization_organization_type (org_id, org_type_id) FROM stdin;
\.


--
-- Name: organization_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('organization_seq', 1282, true);


--
-- Data for Name: organization_type; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY organization_type (id, short_name, description, name_display_key, lastupdated) FROM stdin;
1	TestKitVender	Organization selling HIV test kits	db.organization.type.name.testkit	2009-12-17 12:07:12.477554
3	referralLab	An organization to which samples may be sent	orgainzation.type.referral.lab	2010-11-23 10:30:22.117828
4	referring clinic	Name of org who can order lab tests	organization.type.referral.in.lab	2011-02-16 14:46:31.32568
5	resultRequester	An organization which can request lab results	org_type.resultRequester	2012-04-23 17:30:16.500759
\.


--
-- Name: organization_type_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('organization_type_seq', 5, true);


--
-- Data for Name: package_1; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY package_1 (id) FROM stdin;
\.


--
-- Data for Name: panel; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY panel (id, name, description, lastupdated, display_key, sort_order, is_active) FROM stdin;
\.


--
-- Data for Name: panel_item; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY panel_item (id, panel_id, sort_order, test_local_abbrev, method_name, lastupdated, test_name, test_id) FROM stdin;
\.


--
-- Name: panel_item_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('panel_item_seq', 517, true);


--
-- Name: panel_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('panel_seq', 84, true);


--
-- Data for Name: patient; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY patient (id, person_id, race, gender, birth_date, epi_first_name, epi_middle_name, epi_last_name, birth_time, death_date, national_id, ethnicity, school_attend, medicare_id, medicaid_id, birth_place, lastupdated, external_id, chart_number, entered_birth_date) FROM stdin;
\.


--
-- Data for Name: patient_identity; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY patient_identity (id, identity_type_id, patient_id, identity_data, lastupdated) FROM stdin;
\.


--
-- Name: patient_identity_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('patient_identity_seq', 11, true);


--
-- Data for Name: patient_identity_type; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY patient_identity_type (id, identity_type, description, lastupdated) FROM stdin;
2	ST	ST Number	2008-11-05 10:36:39.615
3	AKA	Also known as name	2008-11-05 10:36:39.615
4	MOTHER	Mothers name	2008-11-05 10:36:39.615
1	NATIONAL	National ID	2008-11-05 10:36:39.615
5	INSURANCE	Primary insurance number	\N
6	OCCUPATION	patients occupation	\N
9	SUBJECT	Subject Number	2010-01-06 12:56:16.166813
8	ORG_SITE	Organization Site	2010-01-06 12:56:39.622399
11	MOTHERS_INITIAL	Initial of mothers first name	2010-03-15 13:15:08.22301
14	GUID	\N	2011-03-10 13:25:23.644
30	EDUCATION	Patients education level	2013-08-08 08:02:34.866733
31	MARITIAL	Patients maritial status	2013-08-08 08:02:34.866733
32	NATIONALITY	Patients nationality	2013-08-08 08:02:34.866733
33	OTHER NATIONALITY	Named nationality if OTHER is selected	2013-08-08 08:02:34.866733
34	HEALTH DISTRICT	Patients health district	2013-08-08 08:02:34.866733
35	HEALTH REGION	Patients health region	2013-08-08 08:02:34.866733
\.


--
-- Name: patient_identity_type_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('patient_identity_type_seq', 35, true);


--
-- Data for Name: patient_occupation; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY patient_occupation (id, patient_id, occupation, lastupdated) FROM stdin;
\.


--
-- Name: patient_occupation_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('patient_occupation_seq', 1, false);


--
-- Data for Name: patient_patient_type; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY patient_patient_type (id, patient_type_id, patient_id, lastupdated) FROM stdin;
\.


--
-- Name: patient_patient_type_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('patient_patient_type_seq', 2, true);


--
-- Name: patient_relation_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('patient_relation_seq', 1, false);


--
-- Data for Name: patient_relations; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY patient_relations (id, pat_id_source, pat_id, relation) FROM stdin;
\.


--
-- Name: patient_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('patient_seq', 7, true);


--
-- Data for Name: patient_type; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY patient_type (id, type, description, lastupdated) FROM stdin;
1	R	Referré	2009-07-09 13:06:10.215545
2	E	Patient Externe	2009-07-09 13:06:10.215545
3	H	Hospitalisé	2009-07-09 13:06:10.215545
4	U	Urgences	2009-07-09 13:06:10.215545
5	P	Patient Privé	2009-07-09 13:06:10.215545
\.


--
-- Name: patient_type_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('patient_type_seq', 20, true);


--
-- Data for Name: payment_type; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY payment_type (id, type, description) FROM stdin;
\.


--
-- Name: payment_type_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('payment_type_seq', 1, false);


--
-- Data for Name: person; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY person (id, last_name, first_name, middle_name, multiple_unit, street_address, city, state, zip_code, country, work_phone, home_phone, cell_phone, fax, email, lastupdated) FROM stdin;
\.


--
-- Data for Name: person_address; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY person_address (person_id, address_part_id, type, value) FROM stdin;
\.


--
-- Name: person_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('person_seq', 9, true);


--
-- Data for Name: program; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY program (id, code, name, lastupdated) FROM stdin;
\.


--
-- Name: program_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('program_seq', 1, false);


--
-- Data for Name: project; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY project (id, name, sys_user_id, description, started_date, completed_date, is_active, reference_to, program_code, lastupdated, scriptlet_id, local_abbrev, display_key) FROM stdin;
\.


--
-- Data for Name: project_organization; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY project_organization (project_id, org_id) FROM stdin;
\.


--
-- Data for Name: project_parameter; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY project_parameter (id, projparam_type, operation, value, project_id, param_name) FROM stdin;
\.


--
-- Name: project_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('project_seq', 13, false);


--
-- Data for Name: provider; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY provider (id, npi, person_id, external_id, provider_type, lastupdated) FROM stdin;
\.


--
-- Name: provider_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('provider_seq', 5, true);


--
-- Data for Name: qa_event; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY qa_event (id, name, description, is_billable, reporting_sequence, reporting_text, test_id, is_holdable, lastupdated, type, category, display_key) FROM stdin;
67	Insufficient	Insufficient sample	\N	\N	\N	\N	Y	2012-04-23 17:30:24.343465	\N	\N	qa_event.insufficient
68	Hemolytic	Hemolytic sample	\N	\N	\N	\N	Y	2012-04-23 17:30:24.343465	\N	\N	qa_event.hemolytic
69	Mislabeled	Bad or mislabled sample	\N	\N	\N	\N	Y	2012-04-23 17:30:24.343465	\N	\N	qa_event.mislabled
70	No form	 No form with sample	\N	\N	\N	\N	Y	2012-04-23 17:30:24.343465	\N	\N	qa_event.noForm
71	Incorrect Form	Form not filled out correctly	\N	\N	\N	\N	Y	2012-04-23 17:30:24.343465	\N	\N	qa_event.formNotCorrect
72	No Sample	No sample with form	\N	\N	\N	\N	Y	2012-04-23 17:30:24.343465	\N	\N	qa_event.noSample
73	Bloodstained Tube	Bloodstained tube	\N	\N	\N	\N	Y	2012-04-23 17:30:24.343465	\N	\N	qa_event.bloodstained.tube
74	Bloodstained Form	Bloodstained form	\N	\N	\N	\N	Y	2012-04-23 17:30:24.343465	\N	\N	qa_event.bloodstained.form
75	Other	Other	\N	\N	\N	\N	Y	2012-04-23 17:30:24.343465	\N	\N	qa_event.other
76	Broken	Broken tube/container	\N	\N	\N	\N	Y	2012-04-23 17:30:24.343465	\N	\N	qa_event.broken
77	Contaminated	Contaminated sample	\N	\N	\N	\N	Y	2012-04-23 17:30:24.343465	\N	\N	qa_event.contaminated
78	Frozen	Frozen sample	\N	\N	\N	\N	Y	2012-04-23 17:30:24.343465	\N	\N	qa_event.frozen
79	Inadequate	Inadequate sample	\N	\N	\N	\N	Y	2012-04-23 17:30:24.343465	\N	\N	qa_event.inadequate
80	unrefrigerated	unrefrigerated sample	\N	\N	\N	\N	Y	2012-04-23 17:30:24.343465	\N	\N	qa_event.unrefrigerated
81	Overturned	Overturned specimen	\N	\N	\N	\N	Y	2012-04-23 17:30:24.343465	\N	\N	qa_event.overturned
66	Coagulated	Coagulated sample	\N	\N	\N	\N	Y	2012-04-23 17:30:24.343465	\N	\N	qa_event.coagulated
\.


--
-- Name: qa_event_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('qa_event_seq', 85, true);


--
-- Data for Name: qa_observation; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY qa_observation (id, observed_id, observed_type, qa_observation_type_id, value_type, value, lastupdated) FROM stdin;
\.


--
-- Name: qa_observation_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('qa_observation_seq', 1, true);


--
-- Data for Name: qa_observation_type; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY qa_observation_type (id, name, description, lastupdated) FROM stdin;
1	authorizer	The name of the person who authorized the event	2013-08-08 08:02:34.982965+00
2	section	The section in which this happened	2013-08-08 08:02:34.982965+00
3	documentNumber	The qa document tracking number	2013-08-08 08:02:34.982965+00
\.


--
-- Name: qa_observation_type_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('qa_observation_type_seq', 3, true);


--
-- Data for Name: qc; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY qc (id, uom_id, sys_user_id, name, source, lot_number, prepared_date, prepared_volume, usable_date, expire_date) FROM stdin;
\.


--
-- Data for Name: qc_analytes; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY qc_analytes (id, qcanaly_type, value, analyte_id) FROM stdin;
\.


--
-- Data for Name: qrtz_blob_triggers; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY qrtz_blob_triggers (trigger_name, trigger_group, blob_data) FROM stdin;
\.


--
-- Data for Name: qrtz_calendars; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY qrtz_calendars (calendar_name, calendar) FROM stdin;
\.


--
-- Data for Name: qrtz_cron_triggers; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY qrtz_cron_triggers (trigger_name, trigger_group, cron_expression, time_zone_id) FROM stdin;
\.


--
-- Data for Name: qrtz_fired_triggers; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY qrtz_fired_triggers (entry_id, trigger_name, trigger_group, is_volatile, instance_name, fired_time, priority, state, job_name, job_group, is_stateful, requests_recovery) FROM stdin;
\.


--
-- Data for Name: qrtz_job_details; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY qrtz_job_details (job_name, job_group, description, job_class_name, is_durable, is_volatile, is_stateful, requests_recovery, job_data) FROM stdin;
\.


--
-- Data for Name: qrtz_job_listeners; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY qrtz_job_listeners (job_name, job_group, job_listener) FROM stdin;
\.


--
-- Data for Name: qrtz_locks; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY qrtz_locks (lock_name) FROM stdin;
TRIGGER_ACCESS
JOB_ACCESS
CALENDAR_ACCESS
STATE_ACCESS
MISFIRE_ACCESS
\.


--
-- Data for Name: qrtz_paused_trigger_grps; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY qrtz_paused_trigger_grps (trigger_group) FROM stdin;
\.


--
-- Data for Name: qrtz_scheduler_state; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY qrtz_scheduler_state (instance_name, last_checkin_time, checkin_interval) FROM stdin;
\.


--
-- Data for Name: qrtz_simple_triggers; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY qrtz_simple_triggers (trigger_name, trigger_group, repeat_count, repeat_interval, times_triggered) FROM stdin;
\.


--
-- Data for Name: qrtz_trigger_listeners; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY qrtz_trigger_listeners (trigger_name, trigger_group, trigger_listener) FROM stdin;
\.


--
-- Data for Name: qrtz_triggers; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY qrtz_triggers (trigger_name, trigger_group, job_name, job_group, is_volatile, description, next_fire_time, prev_fire_time, priority, trigger_state, trigger_type, start_time, end_time, calendar_name, misfire_instr, job_data) FROM stdin;
\.


--
-- Data for Name: quartz_cron_scheduler; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY quartz_cron_scheduler (id, cron_statement, last_run, active, run_if_past, name, job_name, display_key, description_key) FROM stdin;
2	never	\N	f	t	gather site indicators	gatherSiteIndicators	schedule.name.gatherSiteIndicators	schedule.description.gatherSiteIndicators
3	0 40 2 ? * *	2012-05-06 09:40:00.013+00	t	t	send malaria surviellance report	sendMalariaSurviellanceReport	schedule.name.sendMalariaServiellanceReport	schedule.description.sendMalariaServiellanceReport
1	0 11 11 ? * *	2012-05-06 18:11:00.004+00	t	t	send site indicators	sendSiteIndicators	schedule.name.sendSiteIndicators	schedule.description.sendSiteIndicators
\.


--
-- Name: quartz_cron_scheduler_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('quartz_cron_scheduler_seq', 3, true);


--
-- Data for Name: race; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY race (id, description, race_type, is_active) FROM stdin;
\.


--
-- Data for Name: receiver_code_element; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY receiver_code_element (id, identifier, text, code_system, lastupdated, message_org_id, code_element_type_id) FROM stdin;
\.


--
-- Name: receiver_code_element_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('receiver_code_element_seq', 21, false);


--
-- Data for Name: reference_tables; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY reference_tables (id, name, keep_history, is_hl7_encoded, lastupdated) FROM stdin;
40	STATUS_OF_SAMPLE	Y	Y	\N
39	NOTE	Y	N	\N
1	SAMPLE	Y	N	\N
2	GENDER	Y	N	\N
3	SAMPLE_ORGANIZATION	Y	N	\N
4	ANALYSIS	Y	N	\N
5	TEST	Y	Y	\N
6	CITY	Y	N	\N
7	ANALYTE	Y	Y	\N
8	COUNTY	Y	N	\N
9	DICTIONARY	Y	N	\N
10	DICTIONARY_CATEGORY	Y	N	\N
11	LABEL	Y	N	\N
12	METHOD	Y	N	\N
13	PANEL	Y	N	\N
14	PANEL_ITEM	Y	N	\N
15	PATIENT	Y	N	\N
16	PERSON	Y	N	\N
17	PROGRAM	Y	N	\N
18	PROJECT	Y	N	\N
19	PROVIDER	Y	N	\N
20	REGION	Y	N	\N
21	RESULT	Y	N	\N
22	SAMPLE_DOMAIN	Y	N	\N
23	SAMPLE_ITEM	Y	N	\N
24	SAMPLE_PROJECTS	Y	N	\N
25	SCRIPTLET	Y	N	\N
26	SOURCE_OF_SAMPLE	Y	N	\N
27	STATE_CODE	Y	N	\N
28	SYSTEM_USER	Y	N	\N
29	TEST_SECTION	Y	N	\N
30	TEST_ANALYTE	Y	N	\N
31	TEST_REFLEX	Y	N	\N
32	TEST_RESULT	Y	N	\N
33	TEST_TRAILER	Y	N	\N
34	TYPE_OF_SAMPLE	Y	N	\N
35	TYPE_OF_TEST_RESULT	Y	N	\N
36	UNIT_OF_MEASURE	Y	Y	\N
37	ZIP_CODE	Y	N	\N
38	ORGANIZATION	Y	N	\N
45	SAMPLE_HUMAN	Y	N	\N
46	QA_EVENT	Y	N	\N
48	ANALYSIS_QAEVENT	Y	N	\N
47	ACTION	Y	N	\N
49	ANALYSIS_QAEVENT_ACTION	Y	N	\N
70	REFERENCE_TABLES	N	N	\N
41	CODE_ELEMENT_TYPE	Y	N	\N
42	CODE_ELEMENT_XREF	Y	N	\N
43	MESSAGE_ORG	Y	N	\N
44	RECEIVER_CODE_ELEMENT	Y	N	\N
50	LOGIN_USER	Y	N	\N
51	SYSTEM_MODULE	Y	N	\N
52	SYSTEM_USER_MODULE	Y	N	\N
53	SYSTEM_USER_SECTION	Y	N	\N
110	SAMPLE_NEWBORN	Y	N	\N
111	PATIENT_RELATIONS	Y	N	\N
112	PATIENT_IDENTITY	Y	N	\N
113	PATIENT_PATIENT_TYPE	Y	N	\N
130	PATIENT_TYPE	N	N	\N
154	RESULT_LIMITS	Y	N	2009-02-10 17:11:57.227
155	RESULT_SIGNATURE	Y	N	2009-02-20 13:05:56.666
167	INVENTORY_LOCATION	N	N	2009-03-19 12:20:50.594
168	INVENTORY_ITEM	N	N	2009-03-19 12:20:50.594
169	INVENTORY_RECEIPT	N	N	2009-03-19 12:20:50.594
171	RESULT_INVENTORY	Y	N	2009-03-25 16:20:17.301
172	SYSTEM_ROLE	Y	N	2009-05-20 09:56:52.877513
173	SYSTEM_ROLE_MODULE	Y	N	2009-06-05 11:49:44.562736
174	SYSTEM_ROLE	Y	N	2009-06-05 11:50:56.86615
175	SYSTEM_USER_ROLE	Y	N	2009-06-05 11:59:25.708258
176	SYSTEM_USER_ROLE	Y	N	2009-06-05 12:03:40.526192
177	SYSTEM_ROLE	Y	N	2009-06-05 12:04:41.627999
178	SYSTEM_USER_ROLE	Y	N	2009-06-05 12:04:48.416696
179	SYSTEM_ROLE_MODULE	Y	N	2009-06-05 12:05:01.033811
182	analyzer	Y	N	2009-11-25 15:35:31.308859
183	analyzer_results	Y	N	2009-11-25 15:35:31.569744
184	site_information	Y	N	2010-03-23 17:04:19.671634
187	observation_history_type	Y	N	2010-04-28 14:13:23.717515
186	observation_history	Y	N	2010-04-21 10:38:59.516839
185	observation_history_type	Y	N	2010-04-21 10:38:50.05707
188	SAMPLE_QAEVENT	Y	N	2010-10-28 06:12:39.992393
189	referral_reason	Y	N	2010-10-28 06:13:55.299708
190	referral_type	Y	N	2010-10-28 06:13:55.299708
191	referral	Y	N	2010-10-28 06:13:55.299708
192	referral_result	Y	N	2010-11-23 10:30:22.045552
193	org_hl7_encoding_type	Y	N	2011-03-04 16:38:48.986228
197	address_part	Y	N	2011-03-29 16:23:10.813326
198	person_address	Y	N	2011-03-29 16:23:10.813326
199	organization_address	Y	N	2011-03-29 16:23:10.813326
200	organization_contact	Y	N	2011-03-29 16:23:10.825084
201	SITE_INFORMATION_DOMAIN	Y	N	2012-04-23 17:30:07.193494
202	MENU	Y	N	2012-04-23 17:30:07.25924
203	QUARTZ_CRON_SCHEDULER	Y	N	2012-04-23 17:30:07.331733
204	REPORT_QUEUE_TYPE	Y	N	2012-04-23 17:30:07.402102
205	REPORT_QUEUE	Y	N	2012-04-23 17:30:07.424341
206	REPORT_EXTERNAL_IMPORT	Y	N	2012-04-23 17:30:07.500089
207	document_type	Y	N	2012-04-23 17:30:13.955455
208	document_track	Y	N	2012-04-23 17:30:13.955455
209	ANALYZER_TEST_MAP	Y	N	2012-04-23 17:30:14.633487
210	PATIENT_IDENTITY_TYPE	Y	N	2012-04-23 17:30:14.633487
211	SAMPLETYPE_TEST	Y	N	2012-04-23 17:30:14.633487
212	SAMPLETYPE_PANEL	Y	N	2012-04-23 17:30:14.633487
213	SAMPLE_REQUESTER	Y	N	2012-04-23 17:30:14.633487
195	TEST_CODE	Y	N	2011-03-04 16:38:48.986228
194	TEST_CODE_TYPE	Y	N	2011-03-04 16:38:48.986228
214	QA_OBSERVATION	Y	N	2013-08-08 08:02:34.962871
\.


--
-- Name: reference_tables_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('reference_tables_seq', 214, true);


--
-- Data for Name: referral; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY referral (id, analysis_id, organization_id, organization_name, send_ready_date, sent_date, result_recieved_date, referral_reason_id, referral_type_id, requester_name, lastupdated, canceled, referral_request_date) FROM stdin;
\.


--
-- Data for Name: referral_reason; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY referral_reason (id, name, description, display_key, lastupdated) FROM stdin;
\.


--
-- Name: referral_reason_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('referral_reason_seq', 1, false);


--
-- Data for Name: referral_result; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY referral_result (id, referral_id, test_id, result_id, referral_report_date, lastupdated) FROM stdin;
\.


--
-- Name: referral_result_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('referral_result_seq', 1, false);


--
-- Name: referral_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('referral_seq', 1, false);


--
-- Data for Name: referral_type; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY referral_type (id, name, description, display_key, lastupdated) FROM stdin;
\.


--
-- Name: referral_type_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('referral_type_seq', 1, false);


--
-- Data for Name: region; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY region (id, region, lastupdated) FROM stdin;
\.


--
-- Name: region_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('region_seq', 1, false);


--
-- Data for Name: report_external_export; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY report_external_export (id, event_date, collection_date, sent_date, type, data, lastupdated, send_flag, bookkeeping) FROM stdin;
\.


--
-- Data for Name: report_external_import; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY report_external_import (id, sending_site, event_date, recieved_date, type, updated_flag, data, lastupdated) FROM stdin;
\.


--
-- Name: report_external_import_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('report_external_import_seq', 1, false);


--
-- Name: report_queue_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('report_queue_seq', 124, true);


--
-- Data for Name: report_queue_type; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY report_queue_type (id, name, description) FROM stdin;
1	labIndicator	Lab indicator reports.  Number of tests run etc
2	Results	Result sharing with iSante
3	malariaCase	malaria case report
\.


--
-- Name: report_queue_type_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('report_queue_type_seq', 3, true);


--
-- Data for Name: requester_type; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY requester_type (id, requester_type) FROM stdin;
1	organization
2	provider
\.


--
-- Name: requester_type_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('requester_type_seq', 2, false);


--
-- Data for Name: result; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY result (id, analysis_id, sort_order, is_reportable, result_type, value, analyte_id, test_result_id, lastupdated, min_normal, max_normal, parent_id) FROM stdin;
\.


--
-- Data for Name: result_inventory; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY result_inventory (id, inventory_location_id, result_id, description, lastupdated) FROM stdin;
\.


--
-- Name: result_inventory_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('result_inventory_seq', 1, false);


--
-- Data for Name: result_limits; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY result_limits (id, test_id, test_result_type_id, min_age, max_age, gender, low_normal, high_normal, low_valid, high_valid, lastupdated, normal_dictionary_id, always_validate) FROM stdin;
\.


--
-- Name: result_limits_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('result_limits_seq', 624, true);


--
-- Name: result_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('result_seq', 69, true);


--
-- Data for Name: result_signature; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY result_signature (id, result_id, system_user_id, is_supervisor, lastupdated, non_user_name) FROM stdin;
\.


--
-- Name: result_signature_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('result_signature_seq', 63, true);


--
-- Data for Name: sample; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY sample (id, accession_number, package_id, domain, next_item_sequence, revision, entered_date, received_date, collection_date, client_reference, status, released_date, sticker_rcvd_flag, sys_user_id, barcode, transmission_date, lastupdated, spec_or_isolate, priority, status_id) FROM stdin;
\.


--
-- Data for Name: sample_animal; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY sample_animal (id, sci_name_id, comm_anim_id, sampling_location, collector, samp_id, multiple_unit, street_address, city, state, country, zip_code) FROM stdin;
\.


--
-- Data for Name: sample_domain; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY sample_domain (id, domain_description, domain, lastupdated) FROM stdin;
28	ANIMAL SAMPLES	A	2006-11-08 10:58:03.229
29	ENVIRONMENTAL	E	2006-09-21 10:06:53
27	HUMAN SAMPLES	H	2006-09-21 10:06:01
2	NEWBORN SAMPLES	N	2008-10-31 15:19:03.544
\.


--
-- Name: sample_domain_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('sample_domain_seq', 2, true);


--
-- Data for Name: sample_environmental; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY sample_environmental (id, samp_id, is_hazardous, lot_nbr, description, chem_samp_num, street_address, multiple_unit, city, state, zip_code, country, collector, sampling_location) FROM stdin;
\.


--
-- Data for Name: sample_human; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY sample_human (id, provider_id, samp_id, patient_id, lastupdated) FROM stdin;
\.


--
-- Name: sample_human_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('sample_human_seq', 26, true);


--
-- Data for Name: sample_item; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY sample_item (id, sort_order, sampitem_id, samp_id, source_id, typeosamp_id, uom_id, source_other, quantity, lastupdated, external_id, collection_date, status_id, collector) FROM stdin;
\.


--
-- Name: sample_item_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('sample_item_seq', 43, true);


--
-- Data for Name: sample_newborn; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY sample_newborn (id, weight, multi_birth, birth_order, gestational_week, date_first_feeding, breast, tpn, formula, milk, soy, jaundice, antibiotics, transfused, date_transfusion, medical_record_numeric, nicu, birth_defect, pregnancy_complication, deceased_sibling, cause_of_death, family_history, other, y_numeric, yellow_card, lastupdated) FROM stdin;
\.


--
-- Name: sample_org_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('sample_org_seq', 112, false);


--
-- Data for Name: sample_organization; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY sample_organization (id, org_id, samp_id, samp_org_type, lastupdated) FROM stdin;
\.


--
-- Data for Name: sample_pdf; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY sample_pdf (id, accession_number, allow_view, barcode) FROM stdin;
\.


--
-- Name: sample_pdf_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('sample_pdf_seq', 1, false);


--
-- Name: sample_proj_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('sample_proj_seq', 1, false);


--
-- Data for Name: sample_projects; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY sample_projects (samp_id, proj_id, is_permanent, id, lastupdated) FROM stdin;
\.


--
-- Data for Name: sample_qaevent; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY sample_qaevent (id, qa_event_id, sample_id, completed_date, lastupdated, sampleitem_id, entered_date) FROM stdin;
\.


--
-- Data for Name: sample_qaevent_action; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY sample_qaevent_action (id, sample_qaevent_id, action_id, created_date, lastupdated, sys_user_id) FROM stdin;
\.


--
-- Name: sample_qaevent_action_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('sample_qaevent_action_seq', 1, false);


--
-- Name: sample_qaevent_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('sample_qaevent_seq', 2, true);


--
-- Data for Name: sample_requester; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY sample_requester (sample_id, requester_id, requester_type_id, lastupdated) FROM stdin;
\.


--
-- Name: sample_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('sample_seq', 26, true);


--
-- Name: sample_type_panel_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('sample_type_panel_seq', 106, true);


--
-- Name: sample_type_test_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('sample_type_test_seq', 1219, true);


--
-- Data for Name: sampletype_panel; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY sampletype_panel (id, sample_type_id, panel_id) FROM stdin;
\.


--
-- Data for Name: sampletype_test; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY sampletype_test (id, sample_type_id, test_id, is_panel) FROM stdin;
\.


--
-- Data for Name: scriptlet; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY scriptlet (id, name, code_type, code_source, lastupdated) FROM stdin;
13	Ais Test	B	C	2006-12-13 11:00:01.748
11	Diane Test	T	Diane test	2006-11-01 13:34:49.667
12	SCRIPTLET	S	test	2006-11-08 10:58:32.964
1	HIV Status Indeterminate	I	HIV Indeterminate	2011-02-02 11:55:53.344606
2	HIV Status Negative	I	HIV N	2011-02-02 11:55:53.344606
3	HIV Status Positive	I	HIV Positive	2011-02-02 11:55:53.344606
\.


--
-- Name: scriptlet_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('scriptlet_seq', 3, true);


--
-- Data for Name: sequence; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY sequence (seq_name, seq_count) FROM stdin;
\.


--
-- Data for Name: site_information; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY site_information (id, name, lastupdated, description, value, encrypted, domain_id, value_type, instruction_key, "group", schedule_id, tag, dictionary_category_id) FROM stdin;
17	modify results role	2012-04-24 00:30:14.552196+00	Should a separate role be required to be able to modify results	true	f	1	boolean	\N	0	\N	\N	\N
22	showValidationFailureIcon	2012-04-24 00:30:14.608163+00	If the analysis has failed validation show icon on results page	false	f	1	boolean	instructions.results.validationFailIcon	0	\N	\N	\N
19	ResultTechnicianName	2012-04-24 00:30:14.59688+00	If true then the technician name is required for entering results	true	f	1	boolean	instructions.results.technician	0	\N	\N	\N
20	autoFillTechNameBox	2012-04-24 00:30:14.59688+00	If the techs name is required on results then add a box for autofill	true	f	1	boolean	instructions.results.autofilltechbox	0	\N	\N	\N
18	modify results note required	2012-04-24 00:30:14.552196+00	Is a note required when results are modified	true	f	1	boolean	\N	0	\N	\N	\N
25	alertWhenInvalidResult	2012-04-24 00:30:14.768284+00	Should there be an alert when the user enters a result outside of the valid range	true	f	1	boolean	instructions.results.invalidAlert	0	\N	\N	\N
32	reflex_rules	2012-04-24 00:30:25.248429+00	What set of reflex rules are used. From a predefined list	\N	f	6	text	\N	0	\N	\N	\N
40	stringContext	2012-04-24 00:30:25.342503+00	The context for the property, ex: Cote d' Iviore	Haiti	f	11	text	\N	0	\N	\N	\N
11	siteNumber	2011-09-27 16:49:46.515742+00	The site number of the this lab	11404	f	2	text	\N	0	\N	\N	\N
14	patientSearchURL	2011-09-27 16:49:46.51774+00	The service URL from which to import patient demographics	https://192.168.1.50/PatientSearchService/iSante/services/patients	f	2	text	\N	0	\N	\N	\N
15	patientSearchLogOnUser	2011-09-27 16:49:46.518276+00	The user name for using the service	iSanteSvcUser	f	2	text	\N	0	\N	\N	\N
41	statusRules	2012-04-24 00:30:25.342503+00	statusRules determine specific status values for the application, ex: LNSP_haiti	Haiti	f	11	text	\N	0	\N	\N	\N
13	useExternalPatientSource	2011-09-27 16:49:46.517196+00	Use an external source patient demographics true/false	true	f	2	boolean	\N	0	\N	\N	\N
16	allowLanguageChange	2012-04-24 00:30:07.039386+00	Allows the user to change the language at login	false	f	2	boolean	\N	0	\N	\N	\N
5	appName	2010-10-28 11:13:55.857654+00	The name of the application.	haitiOpenElis	f	8	text	\N	0	\N	\N	\N
39	trackPayment	2012-04-24 00:30:25.331052+00	If true track patient payment for services	false	f	10	boolean	instructions.patient.payment	0	\N	\N	\N
42	reflexAction	2012-04-24 00:30:25.342503+00	reflexActions determine the meaning of the flags in reflexes, ex: RetroCI	Haiti	f	11	text	\N	0	\N	\N	\N
43	acessionFormat	2012-04-24 00:30:25.342503+00	specifies the format of the acession number,ex: SiteYearNum	SiteYearNum	f	11	text	\N	0	\N	\N	\N
44	passwordRequirements	2012-04-24 00:30:25.342503+00	changes the password requirements depending on site, ex: HAITI	HAITI	f	11	text	\N	0	\N	\N	\N
45	setFieldForm	2012-04-24 00:30:25.342503+00	set form fields for each different implementation, ex: Haiti	Haiti	f	11	text	\N	0	\N	\N	\N
8	patientSearchPassword	2010-10-28 11:13:56.491221+00	The password for using the service	cq:qzhuxpVz80oD	t	2	text	\N	0	\N	\N	\N
47	malariaSurURL	2012-04-25 00:25:44.847629+00	The URL for malaria Surveillance reports	https://openelis-dev.cirg.washington.edu/upload/receive-file.pl	f	9	text	instructions.result.malaria.sur.url	2	\N	url	\N
48	malariaSurReport	2012-04-25 00:25:44.847629+00	True to send reports, false otherwise	true	f	9	boolean	instructions.result.malaria.surveillance	2	3	enable	\N
38	resultReportingURL	2012-04-24 00:30:25.292745+00	Where reporting results electronically should be sent	https://openelis-dev.cirg.washington.edu/upload/receive-file.pl	f	9	text	\N	1	\N	url	\N
10	TrainingInstallation	\N	Allows for deletion of all patient and sample data	false	f	2	boolean	\N	0	\N	\N	\N
50	malariaCaseReport	2012-04-25 00:25:44.847629+00	True to send reports, false otherwise	true	f	9	boolean	instructions.result.malaria.case	3	\N	enable	\N
37	resultReporting	2012-04-24 00:30:25.292745+00	Should reporting results electronically be enabled	enable	f	9	text	\N	1	\N	enable	\N
49	malariaCaseURL	2012-04-25 00:25:44.847629+00	The URL for malaria case reports	https://openelis-dev.cirg.washington.edu/upload/receive-file.pl	f	9	text	instructions.result.malaria.case.url	3	\N	url	\N
21	autoFillTechNameUser	2012-04-24 00:30:14.59688+00	If the techs name is required on results then autofill with logged in user	true	f	1	boolean	instructions.results.autofilltech.user	0	\N	\N	\N
34	testUsageAggregationUserName	2012-04-24 00:30:25.248429+00	The user name for accesses to the service for aggregating test usage	user	f	7	text	\N	0	\N	\N	\N
35	testUsageAggregationPassword	2012-04-24 00:30:25.248429+00	The password for accesser to the service for aggregating test usage	userUSER!	t	7	text	\N	0	\N	\N	\N
51	testUsageReporting	2012-05-02 20:18:13.979353+00	Should reporting testUsage electronically be enabled	true	f	7	boolean	instructions.test.usage	0	\N	enable	\N
33	testUsageAggregationUrl	2012-04-24 00:30:25.248429+00	The url of the site to which test usage will be sent	https://openelis-dev.cirg.washington.edu/LNSP_HaitiOpenElis	f	7	text	\N	0	\N	\N	\N
36	testUsageSendStatus	2012-04-24 00:30:25.266236+00	The status of what happened the last time an attempt was made to send the report	Succès!	f	7	text	\N	0	\N	\N	\N
52	default language locale	2013-08-08 08:02:34.795224+00	The default language local	fr-FR	f	2	dictionary	\N	0	\N	\N	177
53	default date locale	2013-08-08 08:02:34.795224+00	The default date local	fr-FR	f	2	dictionary	\N	0	\N	\N	177
55	condenseNSF	2013-08-08 08:02:34.823415+00	Should NFS be represented as NFS or as individual tests	false	f	11	boolean	\N	0	\N	\N	\N
56	roleForPatientOnResults	2013-08-08 08:02:34.878224+00	Is patient information restricted to those in correct role	false	f	1	boolean	\N	0	\N	\N	\N
54	configuration name	2013-08-08 08:02:34.806178+00	The name which will appear after the version number in header	Haiti Clinical	f	2	text	\N	0	\N	\N	\N
12	lab director	2011-09-27 16:49:46.516648+00	Name which may appear on reports as lab head	Yolette Francois	f	12	text	\N	0	\N	\N	\N
23	SiteName	2012-04-24 00:30:14.645114+00	The name of the site for reports and header		f	12	text	instructions.site.name	0	\N	\N	\N
24	useLogoInReport	2012-04-24 00:30:14.687813+00	Should the site logo be used in the report	true	f	12	boolean	instructions.site.logo	0	\N	\N	\N
58	sortQaEvents	2013-08-08 08:02:35.09779+00	sort qa events in lists	true	f	2	boolean	siteInformation.instruction.sortQaEvents	0	\N	\N	\N
59	validate all results	2013-08-08 08:02:35.118408+00	all results should be validated even if normal	false	f	1	boolean	siteInformation.instruction.validate.all	0	\N	\N	\N
57	reportPageNumbers	2013-08-08 08:02:35.049849+00	use page numbers on reports	true	f	12	boolean	siteInformation.instruction.pageNumbers	0	\N	\N	\N
60	additional site info	2013-08-08 08:02:35.279543+00	additional information for report header		f	12	freeText	siteInformation.instruction.headerInfo	0	\N	\N	\N
61	lab logo	2013-08-08 08:02:35.287294+00	Provides for uploading lab logo		f	12	logoUpload	siteInformation.instruction.labLogo	0	\N	\N	\N
\.


--
-- Data for Name: site_information_domain; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY site_information_domain (id, name, description) FROM stdin;
1	resultConfiguration	site information which effects the handling of results
2	siteIdentity	Identityfing items which don't change the behavior
3	patientSharing	Items needed to share patient information
4	siteExtras	Items which turn extra capacity on and off
5	formating	Items which specify the format of artifacts
6	rules	Items which change the busness rules and effect the workflow
7	testUsage	Items which change the busness rules and effect the workflow
8	configIdentity	Identityfing items which identify the configuration
10	sampleEntryConfig	Configuration for those items which can appear on the sample entry form
11	hiddenProperties	Configuration properties invisible to the user
9	resultReporting	Items which effect reports being sent electronically
12	printedReportsConfig	items which effect printed reports
\.


--
-- Name: site_information_domain_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('site_information_domain_seq', 12, true);


--
-- Name: site_information_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('site_information_seq', 61, true);


--
-- Data for Name: source_of_sample; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY source_of_sample (id, description, domain, lastupdated) FROM stdin;
43	Ankle	H	2006-09-12 10:09:25
56	Bone marrow	H	2006-09-12 10:14:26
62	Brain meninges	H	2006-09-12 10:24:32
88	Eye lid	H	2006-09-12 10:34:28
91	Finger 1st	H	2006-09-12 10:35:08
102	Head	H	2006-09-12 10:38:14
109	Lingula	H	2006-09-12 10:39:28
205	test11	A	2006-12-11 14:48:24
26	Left Lower Lobe	H	2006-09-12 10:03:04
29	Right Lower Lobe	H	2006-09-12 10:03:24
30	Middle Lobe	H	2006-09-12 10:03:37
31	Acute	H	2006-09-12 10:05:06
32	Convalescent	H	2006-09-12 10:05:19
33	Abdominal	H	2006-11-08 11:10:05.484
34	Amniotic	H	2006-09-12 10:05:53
35	Joint	H	2006-09-12 10:06:04
36	Paracentesis	H	2006-09-12 10:07:23
37	Pericardial	H	2006-09-12 10:07:33
38	Synovial	H	2006-09-12 10:07:43
39	Thoracentesis	H	2006-09-12 10:08:09
40	Vitreous	H	2006-09-12 10:08:20
41	Abdomen	H	2006-11-08 11:09:50.202
42	Adenoid	H	2006-09-12 10:09:17
44	Aorta	H	2006-09-12 10:09:34
45	Arm	H	2006-09-12 10:09:42
46	Axilla	H	2006-09-12 10:09:53
47	Back	H	2006-09-12 10:10:00
48	Bladder	H	2006-09-12 10:10:36
49	Bone	A	2006-11-27 09:21:04
50	Bone tibia	H	2006-09-12 10:13:00
51	Bone femur	H	2006-09-12 10:12:33
52	Bone coccyx	H	2006-09-12 10:13:18
53	Bone clavicle	H	2006-09-12 10:13:32
54	Bone cranium	H	2006-09-12 10:13:46
55	Bone mastoid	H	2006-09-12 10:14:01
57	Bowel	H	2006-09-12 10:14:37
58	Brain	H	2006-09-12 10:23:28
59	Brain frontal lobe	H	2006-09-12 10:23:48
60	Brain subgaleal	H	2006-09-12 10:24:07
61	Brain subdural	H	2006-09-12 10:24:21
63	Brain parietal lobe	H	2006-09-12 10:24:46
64	Brain dura	H	2006-09-12 10:24:56
65	Brain cerebellum	H	2006-09-12 10:25:10
66	Breast	H	2006-09-12 10:25:19
67	Buccal	H	2006-09-12 10:25:37
68	Buttock	H	2006-09-12 10:26:10
69	Calf	H	2006-09-12 10:26:19
70	Cervix	H	2006-09-12 10:26:47
71	Chest	H	2006-09-12 10:26:56
72	Coccyx	H	2006-09-12 10:27:19
73	Colon	H	2006-09-12 10:27:27
74	Composite	H	2006-09-12 10:27:35
75	Disc	H	2006-09-12 10:27:47
76	Duodenum	H	2006-09-12 10:27:58
77	Ear	H	2006-09-12 10:28:05
78	Ear canal	H	2006-09-12 10:28:20
79	Ear mastoid	H	2006-09-12 10:28:32
80	Elbow	H	2006-09-12 10:28:43
81	Endometrium	H	2006-09-12 10:28:53
82	Epidural	H	2006-09-12 10:29:03
83	Epiglottis	H	2006-09-12 10:29:17
84	Esophagus	H	2006-09-12 10:29:37
85	Eye	H	2006-09-12 10:33:46
86	Eye cornea	H	2006-09-12 10:34:00
87	Eye conjunctiva	H	2006-09-12 10:34:19
89	Face	H	2006-09-12 10:34:40
90	Finger	H	2006-09-12 10:34:48
92	Finger 2nd	H	2006-09-12 10:35:19
93	Finger 3rd	H	2006-09-12 10:35:28
94	Finger 4th	H	2006-09-12 10:35:36
95	Finger thumb	H	2006-09-12 10:35:47
96	Foot	H	2006-09-12 10:36:00
97	Gall Bladder	H	2006-09-12 10:37:27
98	Gastric	H	2006-09-12 10:37:35
99	Hair	H	2006-09-12 10:37:42
100	Hand	H	2006-09-12 10:37:52
101	Hard Palate	H	2006-09-12 10:38:03
103	Hip	H	2006-09-12 10:38:25
104	Intestine	H	2006-09-12 10:38:38
105	Jaw	H	2006-09-12 10:38:48
106	Kidney	H	2006-09-12 10:38:58
107	Knee	H	2006-09-12 10:39:07
108	Leg	H	2006-09-12 10:39:20
110	Lip	H	2006-09-12 10:39:43
111	Lumbar	H	2006-09-12 10:39:51
112	Lumbar Disc Space	H	2006-09-12 10:40:03
113	Lymph Node	H	2006-09-12 10:40:14
114	Lymph Node abdominal	H	2006-09-12 10:40:36
115	Lymph Node axillary	H	2006-09-12 10:40:50
116	Lymph Node cervical	H	2006-09-12 10:41:04
117	Lymph Node hilar	H	2006-09-12 10:41:22
118	Lymph Node inguinal	H	2006-09-12 10:41:47
124	Neck	H	2006-09-12 11:34:26
125	Nose	H	2006-09-12 11:34:34
140	Rectum	H	2006-09-12 11:38:12
143	Scrotum	H	2006-09-12 11:39:22
144	Shin	H	2006-09-12 11:39:30
174	Wrist	H	2006-09-12 11:46:47
185	Heart	H	2006-09-12 11:58:15
189	Lymph Node mediastinal	H	2006-09-12 12:45:59
190	Lymph Node mesenteric	H	2006-09-12 12:46:17
191	Lymph Node paratracheal	H	2006-09-12 12:46:30
192	Lymph Node portal	H	2006-09-12 12:47:25
193	Lymph Node post auricular	H	2006-09-12 12:47:42
194	Lymph Node submandibular	H	2006-09-12 12:47:57
195	Lymph Node supraclavicular	H	2006-09-12 12:48:23
196	Lymph Node tracheal	H	2006-09-12 12:48:33
197	Port-a-Cath	H	2006-09-13 08:49:45
198	IV Catheter Tip	H	2006-09-13 08:50:14
120	Urethral	H	2006-09-12 11:33:15
28	Right Upper Lobe	H	2006-09-12 10:03:14
119	Cervical	H	2006-09-12 11:33:05
121	Oral	H	2006-09-12 11:33:38
122	Mandible	H	2006-09-12 11:34:14
123	Nail	H	2006-09-12 11:34:20
126	Nose anterior nares	H	2006-09-12 11:34:59
127	Nose nares	H	2006-09-12 11:35:12
128	Nose turbinate	H	2006-09-12 11:35:22
129	Omentum	H	2006-09-12 11:35:33
130	Paraspinal	H	2006-09-12 11:35:44
131	Paratracheal	H	2006-09-12 11:36:11
132	Parotid	H	2006-09-12 11:36:21
133	Penis	H	2006-09-12 11:36:59
134	Pericardium	H	2006-09-12 11:37:09
135	Perineum	H	2006-09-12 11:37:22
136	Peritoneum	H	2006-09-12 11:37:37
137	Placenta	H	2006-09-12 11:37:45
138	Pleura	H	2006-09-12 11:37:56
139	Prostate	H	2006-09-12 11:38:04
141	Sacrum	H	2006-09-12 11:38:26
142	Scalp	H	2006-09-12 11:38:36
145	Shoulder	H	2006-09-12 11:39:38
146	Sinus	H	2006-09-12 11:39:49
147	Sinus ethmoid	H	2006-09-12 11:40:08
148	Sinus frontal	H	2006-09-12 11:40:17
149	Sinus maxillary	H	2006-09-12 11:41:30
150	Sinus sphenoid	H	2006-09-12 11:41:42
151	Skin	H	2006-09-12 11:41:55
152	Small Bowel	H	2006-09-12 11:42:04
153	Spine	H	2006-09-12 11:42:11
154	Spleen	H	2006-09-12 11:42:20
155	Sternum	H	2006-09-12 11:42:30
156	Stomach	H	2006-09-12 11:42:38
157	Synovium	H	2006-09-12 11:43:00
158	Testicle	H	2006-09-12 11:43:17
159	Thigh	H	2006-09-12 11:43:25
160	Toe	H	2006-09-12 11:43:33
161	Toe great	H	2006-09-12 11:43:58
162	Toe 1st	H	2006-09-12 11:44:10
163	Toe 2nd	H	2006-09-12 11:44:19
164	Toe 3rd	H	2006-09-12 11:44:29
165	Toe 4th	H	2006-09-12 11:44:38
166	Toe 5th	H	2006-09-12 11:45:37
167	Toenail	H	2006-09-12 11:45:48
168	Tongue	H	2006-09-12 11:45:57
169	Tonsil	H	2006-09-12 11:46:04
170	Vagina	H	2006-09-12 11:46:18
171	Vein	H	2006-09-12 11:46:25
172	Vertebra	H	2006-09-12 11:46:32
173	Vulva	H	2006-09-12 11:46:40
175	Other	H	2006-09-12 11:47:35
176	Midstream	H	2006-09-12 11:47:54
177	Clean Catch	H	2006-09-12 11:48:05
178	Catheter	H	2006-09-12 11:48:12
179	Foley Catheter	H	2006-09-12 11:48:33
180	Peripheral	H	2006-09-12 11:48:45
181	EDTA	H	2006-09-12 11:48:51
182	Whole	H	2006-09-12 11:48:57
183	Venous	H	2006-09-12 11:49:06
184	Cord	H	2006-09-12 11:49:11
186	Heart valve	H	2006-09-12 11:58:28
187	Heart aortic	H	2006-09-12 11:58:38
188	Heart pericardium	H	2006-09-12 11:58:49
25	Left Upper Lobe 	H	2006-09-12 10:02:44
\.


--
-- Name: source_of_sample_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('source_of_sample_seq', 1, false);


--
-- Data for Name: state_code; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY state_code (id, code, description, lastupdated) FROM stdin;
\.


--
-- Name: state_code_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('state_code_seq', 1, false);


--
-- Data for Name: status_of_sample; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY status_of_sample (id, description, code, status_type, lastupdated, name, display_key, is_active) FROM stdin;
4	This test has not yet been done	1	ANALYSIS	2010-04-28 15:39:55.011	Not Tested	status.test.notStarted	Y
9	test has been referred to an outside lab and the results have not been returned	1	ANALYSIS	2010-10-28 06:13:55.174221	referred out	status.test.referred.out	Y
10	test has been done at an outside lab and then referred to this lab	1	ANALYSIS	2011-03-29 16:23:08.29256	referred in	status.test.referred.in	Y
6	The results of the analysis are final	1	ANALYSIS	2010-04-28 15:39:55.011	Finalized	status.test.valid	Y
7	The Biologist did not accept this result as valid	1	ANALYSIS	2012-04-23 17:30:24.415051	Biologist Rejection	status.test.biologist.reject	Y
15	Test was requested but then canceled	1	ANALYSIS	2012-04-23 17:30:24.415051	Test Canceled	status.test.canceled	Y
16	The results of the test were accepted by technician as being valid	1	ANALYSIS	2012-04-23 17:30:24.415051	Technical Acceptance	status.test.tech.accepted	Y
17	The results of the test were not accepted by the technicain	1	ANALYSIS	2012-04-23 17:30:24.415051	Technical Rejected	status.test.tech.rejected	Y
1	No tests have been run for this order	1	ORDER	2010-04-28 15:39:55.011	Test Entered	status.sample.notStarted	Y
2	Some tests have been run on this order	1	ORDER	2010-04-28 15:39:55.011	Testing Started	status.sample.started	Y
3	All tests have been run on this order	1	ORDER	2010-04-28 15:39:55.011	Testing finished	status.sample.finished	Y
18	The sample has been canceled by the user	1	SAMPLE	2013-08-08 08:02:34.640284	SampleCanceled	status.sample.entered	Y
19	The sample has been entered into the system	1	SAMPLE	2013-08-08 08:02:34.640284	SampleEntered	status.sample.entered	Y
11	The order is non-conforming	1	ORDER	2012-04-23 17:30:13.485907	NonConforming	status.sample.nonConforming	N
14	The order is non-conforming	1	ORDER	2012-04-23 17:30:24.383612	NonConforming	status.sample.nonConforming	N
12	The order is non-conforming	1	ANALYSIS	2012-04-23 17:30:13.485907	NonConforming	status.analysis.nonConforming	N
13	The order is non-conforming	1	ANALYSIS	2012-04-23 17:30:24.330299	NonConforming	status.analysis.nonconforming	N
\.


--
-- Name: status_of_sample_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('status_of_sample_seq', 19, true);


--
-- Data for Name: storage_location; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY storage_location (id, sort_order, name, location, is_available, parent_storageloc_id, storage_unit_id) FROM stdin;
\.


--
-- Data for Name: storage_unit; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY storage_unit (id, category, description, is_singular) FROM stdin;
\.


--
-- Data for Name: system_module; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY system_module (id, name, description, has_select_flag, has_add_flag, has_update_flag, has_delete_flag) FROM stdin;
1	PanelItem	Master Lists => Panel Item => edit	Y	Y	Y	N
2	DictionaryCategory	Master Lists => Dictionary Category => edit	Y	Y	Y	N
3	Dictionary	Master Lists => Dictonary => edit	Y	Y	Y	N
4	Gender	Master Lists => Gender => edit	Y	Y	Y	N
5	LoginUser	Master Lists => Login User => Edit	Y	Y	Y	N
6	Organization	Master Lists => Organization => edit	Y	Y	Y	N
7	Panel	Master Lists => Panel	Y	Y	Y	Y
8	PatientResults	Results->By Patient	N	N	N	N
9	ResultLimits	Master Lists => ResultLimits => edit	Y	Y	Y	N
10	Result	Master Lists => Result => edit	Y	Y	Y	N
11	Role	Master Lists => Role => edit	Y	Y	Y	N
12	StatusOfSample	Master Lists => StatusOfSample => edit	Y	Y	Y	N
13	SystemModule	Master Lists => System Module	Y	Y	Y	N
14	SystemUser	Master Lists => System User	Y	Y	Y	N
15	Test	Master Lists => Test	Y	Y	Y	N
16	SampleEntry	Sample->Sample Entry	N	N	N	N
17	MasterList	Administration	N	N	N	N
18	Inventory	Inventory	N	N	N	N
26	StatusResults	Results->By Status	N	N	N	N
27	ReportAdmin	Master Lists => OR Admin	Y	Y	Y	N
28	ReportUserDetail	Reports	Y	Y	Y	N
29	ReportUserOption	Reports	Y	Y	Y	N
30	ReportUserRun	Reports	Y	Y	Y	N
31	TypeOfTestResult	Master Lists => Type Of Test Result	Y	Y	Y	N
32	SystemUserModule	Master Lists => System User Module	Y	Y	Y	N
33	ResultsEntry	Result Management => Results Entry	Y	Y	Y	N
34	TestSection	Master Lists => Test Section	Y	Y	Y	N
35	TypeOfSample	Master Lists => Type Of Sample	Y	Y	Y	N
36	UnitOfMeasure	Master Lists => Unit Of Measure	N	N	N	N
40	UserRole	MasterList => UserRole	Y	Y	Y	Y
41	PatientType	MasterList => PatientType	Y	Y	Y	Y
42	TypeOfSamplePanel	MasterList => Associtate type of sample with panel	Y	Y	Y	Y
43	TypeOfSampleTest	MasterList => Associtate type of sample with tests	Y	Y	Y	Y
44	UnifiedSystemUser	MasterList->ManageUsers	Y	Y	Y	Y
45	LogbookResults	Results=>logbook=>save	Y	Y	Y	Y
46	SamplePatientEntry	Sample->SampleEntry	Y	Y	Y	Y
47	SiteInformation	MasterList=>Site Information	Y	Y	Y	Y
48	AnalyzerTestName	MasterList->Analyzer Test Name	Y	Y	Y	Y
49	AnalyzerResults	Results->Analyzers	Y	Y	Y	Y
51	SampleEntryByProject:initial	Sample=>CreateSample=>initial	Y	Y	Y	Y
52	SampleEntryByProject:verify	Sample=>CreateSample=>verify	Y	Y	Y	Y
55	PatientEntryByProject:initial	Patient=>Enter=>initial	Y	Y	Y	Y
56	PatientEntryByProject:verify	Patient=>Enter=>verify	Y	Y	Y	Y
60	LogbookResults:serology	Results=>Enter=>serology	Y	Y	Y	Y
61	LogbookResults:virology	Results=>Enter=>virology	Y	Y	Y	Y
63	AccessionResults	Results=>Search=>Lab No.	Y	Y	Y	Y
65	AnalyzerResults:cobas_integra	Results=>Analyzer=>cobas_integra	Y	Y	Y	Y
66	AnalyzerResults:sysmex	Results=>Analyzer=>sysmex	Y	Y	Y	Y
67	AnalyzerResults:facscalibur	Results=>Analyzer=>facscalibur	Y	Y	Y	Y
68	AnalyzerResults:evolis	Results=>Analyzer=>evolis	Y	Y	Y	Y
69	Workplan:test	Workplan=>test	Y	Y	Y	Y
70	Workplan:serology	Workplan=>serology	Y	Y	Y	Y
71	Workplan:immunology	Workplan=>immunology	Y	Y	Y	Y
72	Workplan:hematology	Workplan=>hematology	Y	Y	Y	Y
73	Workplan:biochemistry	Workplan=>biochemistry	Y	Y	Y	Y
74	Workplan:virology	Workplan=>virology	Y	Y	Y	Y
84	TestResult	Admin=>TestResult	Y	Y	Y	Y
86	TestReflex	Admin=>TestReflex	Y	Y	Y	Y
87	TestAnalyte	Admin=>TestAnalyte	Y	Y	Y	Y
97	Method	Admin=>Method	Y	Y	Y	Y
53	SampleEditByProject:readwrite	Sample=>SampleEditByProject:readwrite	Y	Y	Y	Y
54	SampleEditByProject:readonly	Sample=>SampleEditByProject:readonly	Y	Y	Y	Y
20	LogbookResults:chem	Results->By Logbook->Chem	N	N	N	N
25	LogbookResults:HIV	Results->By Logbook->VCT	N	N	N	N
19	LogbookResults:bacteriology	Results->By Logbook->Bacteria	N	N	N	N
21	LogbookResults:ECBU	Results->By Logbook->ECBU	N	N	N	N
22	LogbookResults:hematology	Results->By Logbook->Hemaology	N	N	N	N
23	LogbookResults:immuno	Results->By Logbook->Immuno	N	N	N	N
24	LogbookResults:parasitology	Results->By Logbook->Parasitology	N	N	N	N
101	Analyte	Admin=>Analyte	Y	Y	Y	Y
106	SampleEdit	Sample=>edit	Y	Y	Y	Y
107	NonConformity	NonConformity	Y	Y	Y	Y
108	Report:patient	Patient reports	Y	Y	Y	Y
109	Report:summary	Lab summary reports	Y	Y	Y	Y
110	Report:indicator	Lab quality indicator reports	Y	Y	Y	Y
111	ResultValidation:serology	Validation=>serology	Y	Y	Y	Y
112	ResultValidation:immunology	Validation=>immunology	Y	Y	Y	Y
113	ResultValidation:hematology	Validation=>hematology	Y	Y	Y	Y
114	ResultValidation:biochemistry	Validation=>biochemistry	Y	Y	Y	Y
115	ResultValidation:virology	Validation=>virology	Y	Y	Y	Y
116	PatientEditByProject:readwrite	Patient=>PatientEdit	Y	Y	Y	Y
117	PatientEditByProject:readonly	Patient=>PatientConsult	Y	Y	Y	Y
118	SampleEdit:readwrite	Sample -> edit	Y	Y	Y	Y
123	SampleEdit:readonly	Sample=>SampleConsult	Y	Y	Y	Y
173	ReportUserDetail:patientARV2	Report=>patient=>ARV follow-up Save	Y	Y	Y	Y
174	ReportUserDetail:patientEID	Report=>patient=>EID	Y	Y	Y	Y
185	ReferredOutTests	Results=>Referrals	Y	Y	Y	Y
253	SampleConfirmationEntry	Sample=>sample confirmation	Y	Y	Y	Y
322	LogbookResults:mycobacteriology	Results=>logbooks=>mycobacteriology	Y	Y	Y	Y
341	AnalyzerResults:facscanto	Results=>Analyzer=>facscanto	Y	Y	Y	Y
349	Workplan:chem	Workplan=>chem	Y	Y	Y	Y
350	Workplan:cytobacteriology	Workplan=>cytobacteriology	Y	Y	Y	Y
351	Workplan:bacteriology	Workplan=>bacteriology	Y	Y	Y	Y
352	Workplan:ECBU	Workplan=>ECBU	Y	Y	Y	Y
353	Workplan:parasitology	Workplan=>parasitology	Y	Y	Y	Y
354	Workplan:immuno	Workplan=>immuno	Y	Y	Y	Y
355	Workplan:HIV	Workplan=>HIV	Y	Y	Y	Y
356	Workplan:molecularBio	Workplan=>molecularBio	Y	Y	Y	Y
357	Workplan:liquidBio	Workplan=>liquidBio	Y	Y	Y	Y
358	Workplan:mycrobacteriology	Workplan=>mycrobacteriology	Y	Y	Y	Y
359	Workplan:endocrin	Workplan=>endocrin	Y	Y	Y	Y
360	Workplan:serologie	Workplan=>serologie	Y	Y	Y	Y
405	LogbookResults:mycrobacteriology	Results=>logbooks=>mycrobacteriology	Y	Y	Y	Y
406	LogbookResults:cytobacteriology	Results=>logbooks=>cytobacteriology	Y	Y	Y	Y
407	LogbookResults:molecularBio	Results=>logbooks=>molecularBio	Y	Y	Y	Y
408	LogbookResults:liquidBio	Results=>logbooks=>liquidBio	Y	Y	Y	Y
409	LogbookResults:endocrin	Results=>logbooks=>endocrin	Y	Y	Y	Y
410	Workplan:panel	Workplan=>panel	Y	Y	Y	Y
411	Workplan:virologie	workplan-unit-virology	Y	Y	Y	Y
412	LogbookResults:virologie	results-section-virology	Y	Y	Y	Y
495	Workplan:mycology	Workplan=>mycology	Y	Y	Y	Y
496	LogbookResults:mycology	LogbookResults=>mycology	Y	Y	Y	Y
508	LogbookResults:serologie	Results=>Enter=>serologie	Y	Y	Y	Y
667	LogbookResults:serolo-immunology	LogbookResults=>serology-immunology	Y	Y	Y	Y
668	LogbookResults:immunology	LogbookResults=>immunology	Y	Y	Y	Y
669	LogbookResults:hemato-immunology	LogbookResults=>hemato-immunology	Y	Y	Y	Y
670	LogbookResults:biochemistry	LogbookResults=>biochemistry	Y	Y	Y	Y
671	Workplan:hemato-immunology	workplan=>units=>hemato-immunology	Y	Y	Y	Y
672	Workplan:serology-immunology	workplan=>units=>serology-immunology	Y	Y	Y	Y
673	ResultValidation:Hemto-Immunology	validation=>units=>Hemato-Immunity	Y	Y	Y	Y
675	ResultValidation:Serology-Immunology	validation=>units=>Serology-Immunity	Y	Y	Y	Y
676	ResultValidation	validation return	Y	Y	Y	Y
677	PatientDataOnResults	Able to view patient data when looking at results	Y	Y	Y	Y
678	AuditTrailView	Report=>view audit log	Y	Y	Y	Y
679	AnalyzerResults:cobasDBS	Result=>analyzers=>CobasTaqmanDBS	Y	Y	Y	Y
680	ResultValidation:molecularBio	ResultValidation=>molecularBio	Y	Y	Y	Y
681	ResultValidation:Cytobacteriologie	ResultValidation=>Cytobacteriologie	Y	Y	Y	Y
682	ResultValidation:ECBU	ResultValidation=>ECBU	Y	Y	Y	Y
683	ResultValidation:Parasitology	ResultValidation=>Parasitology	Y	Y	Y	Y
684	ResultValidation:Liquides biologique	ResultValidation=>Liquides biologique	Y	Y	Y	Y
685	ResultValidation:Mycobacteriology	ResultValidation=>Mycobacteriology	Y	Y	Y	Y
686	ResultValidation:Endocrinologie	ResultValidation=>Endocrinologie	Y	Y	Y	Y
687	ResultValidation:Serologie	ResultValidation=>Serologie	Y	Y	Y	Y
688	ResultValidation:VCT	ResultValidation=>VCT	Y	Y	Y	Y
689	ResultValidation:virologie	ResultValidation=>virologie	Y	Y	Y	Y
690	ResultValidation:Bacteria	ResultValidation=>Bacteria	Y	Y	Y	Y
691	ResultValidation:mycology	ResultValidation=>mycology	Y	Y	Y	Y
692	AnalyzerResults:cobasc311	AnalyzerResults=>cobasc311	Y	Y	Y	Y
\.


--
-- Name: system_module_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('system_module_seq', 692, true);


--
-- Data for Name: system_role; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY system_role (id, name, description, is_grouping_role, grouping_parent, display_key, active, editable) FROM stdin;
1	Maintenance Admin   	Change tests, panels etc.	f	\N	\N	t	f
2	User Admin          	Add/remove users and assign roles.	f	\N	\N	t	f
4	Intake              	Sample entry and patient management.	f	\N	\N	t	f
5	Results entry       	Enter and review results.	f	\N	\N	t	f
6	Inventory mgr       	Add and de/reactivate kits.	f	\N	\N	t	f
7	Reports             	Generate reports.	f	\N	\N	t	f
10	Quality control     	Able to do QC (e.g. nonconformity)	f	\N	role.quality.control	t	f
9	Results modifier    	Has permission to modify already entered results	f	\N	role.result.modifier	t	t
11	Audit Trail         	Able to view the audit trail	f	\N	role.audittrail	t	f
3	Validation          	Able to validate results	f	\N	\N	t	f
\.


--
-- Data for Name: system_role_module; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY system_role_module (id, has_select, has_add, has_update, has_delete, system_role_id, system_module_id) FROM stdin;
259	Y	Y	Y	Y	1	17
260	Y	Y	Y	Y	1	87
261	Y	Y	Y	Y	1	48
262	Y	Y	Y	Y	1	3
263	Y	Y	Y	Y	1	4
264	Y	Y	Y	Y	1	97
265	Y	Y	Y	Y	1	6
266	Y	Y	Y	Y	1	7
267	Y	Y	Y	Y	1	1
268	Y	Y	Y	Y	1	41
269	Y	Y	Y	Y	1	9
270	Y	Y	Y	Y	1	11
271	Y	Y	Y	Y	1	47
272	Y	Y	Y	Y	1	12
273	Y	Y	Y	Y	1	15
274	Y	Y	Y	Y	1	86
275	Y	Y	Y	Y	1	84
276	Y	Y	Y	Y	1	34
277	Y	Y	Y	Y	1	35
278	Y	Y	Y	Y	1	42
279	Y	Y	Y	Y	1	43
280	Y	Y	Y	Y	1	31
281	Y	Y	Y	Y	1	36
282	Y	Y	Y	Y	1	27
283	Y	Y	Y	Y	1	13
284	Y	Y	Y	Y	1	32
285	Y	Y	Y	Y	1	44
286	Y	Y	Y	Y	1	101
287	Y	Y	Y	Y	2	17
288	Y	Y	Y	Y	2	44
289	Y	Y	Y	Y	4	46
290	Y	Y	Y	Y	5	8
291	Y	Y	Y	Y	5	26
292	Y	Y	Y	Y	5	63
293	Y	Y	Y	Y	5	20
294	Y	Y	Y	Y	5	25
295	Y	Y	Y	Y	5	19
296	Y	Y	Y	Y	5	21
297	Y	Y	Y	Y	5	22
298	Y	Y	Y	Y	5	23
299	Y	Y	Y	Y	5	24
300	Y	Y	Y	Y	5	45
301	Y	Y	Y	Y	6	18
302	Y	Y	Y	Y	7	28
303	Y	Y	Y	Y	7	29
304	Y	Y	Y	Y	7	30
305	Y	Y	Y	Y	4	118
306	Y	Y	Y	Y	4	123
307	Y	Y	Y	Y	4	106
308	Y	Y	Y	Y	4	118
309	Y	Y	Y	Y	4	123
310	Y	Y	Y	Y	4	106
311	Y	Y	Y	Y	5	185
312	Y	Y	Y	Y	7	108
258	Y	Y	Y	Y	5	185
313	Y	Y	Y	Y	7	110
314	Y	Y	Y	Y	5	322
315	Y	Y	Y	Y	5	69
316	Y	Y	Y	Y	5	72
317	Y	Y	Y	Y	5	349
318	Y	Y	Y	Y	5	350
319	Y	Y	Y	Y	5	352
320	Y	Y	Y	Y	5	353
321	Y	Y	Y	Y	5	354
322	Y	Y	Y	Y	5	356
323	Y	Y	Y	Y	5	357
324	Y	Y	Y	Y	5	358
325	Y	Y	Y	Y	5	359
326	Y	Y	Y	Y	5	360
327	Y	Y	Y	Y	5	355
328	Y	Y	Y	Y	10	107
329	Y	Y	Y	Y	5	405
330	Y	Y	Y	Y	5	406
331	Y	Y	Y	Y	5	407
332	Y	Y	Y	Y	5	408
333	Y	Y	Y	Y	5	409
334	Y	Y	Y	Y	5	60
335	Y	Y	Y	Y	5	410
336	Y	Y	Y	Y	5	356
337	Y	Y	Y	Y	5	411
338	Y	Y	Y	Y	5	412
339	Y	Y	Y	Y	5	508
340	Y	Y	Y	Y	11	678
341	Y	Y	Y	Y	3	113
342	Y	Y	Y	Y	3	114
343	Y	Y	Y	Y	3	681
344	Y	Y	Y	Y	3	682
345	Y	Y	Y	Y	3	683
346	Y	Y	Y	Y	3	112
347	Y	Y	Y	Y	3	680
348	Y	Y	Y	Y	3	684
349	Y	Y	Y	Y	3	685
350	Y	Y	Y	Y	3	686
351	Y	Y	Y	Y	3	687
352	Y	Y	Y	Y	3	688
353	Y	Y	Y	Y	3	676
\.


--
-- Name: system_role_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('system_role_seq', 11, true);


--
-- Data for Name: system_user; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY system_user (id, external_id, login_name, last_name, first_name, initials, is_active, is_employee, lastupdated) FROM stdin;
1	1	admin	ELIS	Open	OE	Y	Y	2006-11-08 11:11:14.312
106	1	user	User	User	UU	Y	Y	2011-02-14 16:40:02.925
\.


--
-- Data for Name: system_user_module; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY system_user_module (id, has_select, has_add, has_update, has_delete, system_user_id, system_module_id) FROM stdin;
\.


--
-- Name: system_user_module_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('system_user_module_seq', 353, true);


--
-- Data for Name: system_user_role; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY system_user_role (system_user_id, role_id) FROM stdin;
106	4
106	6
106	7
106	5
106	3
106	2
106	10
106	9
\.


--
-- Data for Name: system_user_section; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY system_user_section (id, has_view, has_assign, has_complete, has_release, has_cancel, system_user_id, test_section_id) FROM stdin;
\.


--
-- Name: system_user_section_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('system_user_section_seq', 1, false);


--
-- Name: system_user_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('system_user_seq', 121, true);


--
-- Data for Name: test; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY test (id, method_id, uom_id, description, loinc, reporting_description, sticker_req_flag, is_active, active_begin, active_end, is_reportable, time_holding, time_wait, time_ta_average, time_ta_warning, time_ta_max, label_qty, lastupdated, label_id, test_trailer_id, test_section_id, scriptlet_id, test_format_id, local_abbrev, sort_order, name, display_key, orderable) FROM stdin;
\.


--
-- Data for Name: test_analyte; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY test_analyte (id, test_id, analyte_id, result_group, sort_order, testalyt_type, lastupdated, is_reportable) FROM stdin;
\.


--
-- Name: test_analyte_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('test_analyte_seq', 280, true);


--
-- Data for Name: test_code; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY test_code (test_id, code_type_id, value, lastupdated) FROM stdin;
\.


--
-- Data for Name: test_code_type; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY test_code_type (id, schema_name, lastupdated) FROM stdin;
1	LOINC	2012-04-24 00:30:07.075933+00
2	SNOMED	2012-04-24 00:30:07.075933+00
3	billingCode	2013-08-08 08:02:34.814891+00
4	analyzeCode	2013-08-08 08:02:34.814891+00
\.


--
-- Data for Name: test_formats; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY test_formats (id, lastupdated) FROM stdin;
\.


--
-- Data for Name: test_reflex; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY test_reflex (id, tst_rslt_id, flags, lastupdated, test_analyte_id, test_id, add_test_id, sibling_reflex, scriptlet_id) FROM stdin;
\.


--
-- Name: test_reflex_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('test_reflex_seq', 6, true);


--
-- Data for Name: test_result; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY test_result (id, test_id, result_group, flags, tst_rslt_type, value, significant_digits, quant_limit, cont_level, lastupdated, scriptlet_id, sort_order) FROM stdin;
\.


--
-- Name: test_result_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('test_result_seq', 2586, true);


--
-- Data for Name: test_section; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY test_section (id, name, description, org_id, is_external, lastupdated, parent_test_section, display_key, sort_order, is_active) FROM stdin;
136	user	Indicates user will chose test section	3	N	2013-08-08 08:02:35.294475	\N	\N	2147483647	N
\.


--
-- Name: test_section_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('test_section_seq', 155, true);


--
-- Name: test_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('test_seq', 783, true);


--
-- Data for Name: test_trailer; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY test_trailer (id, name, description, text, lastupdated) FROM stdin;
\.


--
-- Name: test_trailer_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('test_trailer_seq', 2, false);


--
-- Data for Name: test_worksheet_item; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY test_worksheet_item (id, tw_id, qc_id, "position", cell_type) FROM stdin;
\.


--
-- Data for Name: test_worksheets; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY test_worksheets (id, test_id, batch_capacity, total_capacity, number_format) FROM stdin;
\.


--
-- Name: tobereomved_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('tobereomved_seq', 1, false);


--
-- Data for Name: type_of_provider; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY type_of_provider (id, description, tp_code) FROM stdin;
\.


--
-- Data for Name: type_of_sample; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY type_of_sample (id, description, domain, lastupdated, local_abbrev, display_key, is_active, sort_order) FROM stdin;
\.


--
-- Name: type_of_sample_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('type_of_sample_seq', 81, true);


--
-- Data for Name: type_of_test_result; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY type_of_test_result (id, test_result_type, description, lastupdated, hl7_value) FROM stdin;
2	D	Dictionary	2006-11-08 11:40:58.824	TX
3	T	Titer	2006-03-29 11:53:15	TX
4	N	Numeric	2006-03-29 11:53:21	NM
1	R	Remark	2010-10-28 06:12:41.971687	TX
5	A	Alpha,no range check	2010-10-28 06:13:53.177655	TX
6	M	Multiselect	2011-01-06 10:57:15.79331	TX
\.


--
-- Name: type_of_test_result_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('type_of_test_result_seq', 6, true);


--
-- Data for Name: unit_of_measure; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY unit_of_measure (id, name, description, lastupdated) FROM stdin;
\.


--
-- Name: unit_of_measure_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('unit_of_measure_seq', 81, true);


--
-- Data for Name: user_alert_map; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY user_alert_map (user_id, alert_id, report_id, alert_limit, alert_operator, map_id) FROM stdin;
\.


--
-- Data for Name: user_group_map; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY user_group_map (user_id, group_id, map_id) FROM stdin;
1	1120	0
241	1121	0
\.


--
-- Data for Name: user_security; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY user_security (user_id, role_name) FROM stdin;
1	ROOT_ADMIN_ROLE
1	LOG_VIEWER_ROLE
1	UPLOAD_ROLE
1	GROUP_ADMIN_ROLE
1	DATASOURCE_ADMIN_ROLE
1	CHART_ADMIN_ROLE
1	REPORT_ADMIN_ROLE
1	ADVANCED_SCHEDULER_ROLE
1	PARAMETER_ADMIN_ROLE
1	SCHEDULER_ROLE
1	ROOT_ADMIN_ROLE
1	LOG_VIEWER_ROLE
1	UPLOAD_ROLE
1	GROUP_ADMIN_ROLE
1	DATASOURCE_ADMIN_ROLE
1	CHART_ADMIN_ROLE
1	REPORT_ADMIN_ROLE
1	ADVANCED_SCHEDULER_ROLE
1	PARAMETER_ADMIN_ROLE
1	SCHEDULER_ROLE
1	ROOT_ADMIN_ROLE
1	LOG_VIEWER_ROLE
1	UPLOAD_ROLE
1	GROUP_ADMIN_ROLE
1	DATASOURCE_ADMIN_ROLE
1	CHART_ADMIN_ROLE
1	REPORT_ADMIN_ROLE
1	ADVANCED_SCHEDULER_ROLE
1	PARAMETER_ADMIN_ROLE
1	SCHEDULER_ROLE
1	ROOT_ADMIN_ROLE
1	LOG_VIEWER_ROLE
1	UPLOAD_ROLE
1	GROUP_ADMIN_ROLE
1	DATASOURCE_ADMIN_ROLE
1	CHART_ADMIN_ROLE
1	REPORT_ADMIN_ROLE
1	ADVANCED_SCHEDULER_ROLE
1	PARAMETER_ADMIN_ROLE
1	SCHEDULER_ROLE
1	ROOT_ADMIN_ROLE
1	LOG_VIEWER_ROLE
1	UPLOAD_ROLE
1	GROUP_ADMIN_ROLE
1	DATASOURCE_ADMIN_ROLE
1	CHART_ADMIN_ROLE
1	REPORT_ADMIN_ROLE
1	ADVANCED_SCHEDULER_ROLE
1	PARAMETER_ADMIN_ROLE
1	SCHEDULER_ROLE
1	ROOT_ADMIN_ROLE
1	LOG_VIEWER_ROLE
1	UPLOAD_ROLE
1	GROUP_ADMIN_ROLE
1	DATASOURCE_ADMIN_ROLE
1	CHART_ADMIN_ROLE
1	REPORT_ADMIN_ROLE
1	ADVANCED_SCHEDULER_ROLE
1	PARAMETER_ADMIN_ROLE
1	SCHEDULER_ROLE
\.


--
-- Data for Name: worksheet_analysis; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY worksheet_analysis (id, reference_type, reference_id, worksheet_item_id) FROM stdin;
\.


--
-- Data for Name: worksheet_analyte; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY worksheet_analyte (id, wrkst_anls_id, sort_order, result_id) FROM stdin;
\.


--
-- Data for Name: worksheet_heading; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY worksheet_heading (id, worksheet_name, rownumber, column1, column2, column3, column4, column5, column6, column7, column8, column9, column10, type) FROM stdin;
1	TB	1	Micro Result	Culture Result	Report Date	\N	\N	\N	\N	\N	\N	\N	RESULT
2	GC	1	12 HOUR	24 HOUR	48 HOUR	\N	\N	\N	\N	\N	\N	\N	RESULT
9	US FTA	7	\N	\N	\N	\N	PBS	.	.	.	-	.	QC
10	US FTA	8	\N	\N	\N	\N	Sorbent	.	.	.	-	.	QC
8	US FTA	6	PBS	.	.	.	Nonspecific Control / Sorbent	.	.	.	-	.	QC
11	US FTA	1	    USR     Repeat Quant	   FTA      Current Run	    FTA     Previous Runs	   FTA         Final	  TPPA       Result	Comments	\N	\N	\N	\N	RESULT
26	US FTA	1	TEST	TESTing	\N	\N	\N	\N	\N	\N	\N	\N	ANALYTE
7	US FTA	5	.	.	.	.	Nonspecific Control/ PBS	.	.	.	>2+	.	QC
6	US FTA	4	FITCconjugate Working Dilution:	.	.	.	Minimally Reactive Control	.	.	.	1+	.	QC
5	US FTA	3	Sorbent	.	.	.	Reactive Control / Sorbent	.	.	.	3-4+	.	QC
4	US FTA	2	T Palladium antigen	.	.	.	Reactive Control/PBS	.	.	.	4+	.	QC
3	US FTA	1	Reagent	Source	Lot	Expire Date	Reagent	Source	Lot	Expires	Expect	Actual	QC
25	F	23	KNO3	RT incubation	.	.	\N	\N	\N	\N	\N	\N	ANALYTE
24	F	22	dulcitol	chlamydospores	.	.	\N	\N	\N	\N	\N	\N	ANALYTE
23	F	21	melibiose	ascospores	NO3	.	\N	\N	\N	\N	\N	\N	ANALYTE
22	F	20	trehalose	pigment	gelatin	.	\N	\N	\N	\N	\N	\N	ANALYTE
21	F	19	xylose	germ tubes	urea	probe (histo)	\N	\N	\N	\N	\N	\N	ANALYTE
20	F	18	inositol	urea	T4	probe (blasto)	\N	\N	\N	\N	\N	\N	ANALYTE
19	F	17	raffinose	OTHER TESTS	T3	probe (cocci)	\N	\N	\N	\N	\N	\N	ANALYTE
18	F	16	cellobiose	galactose	T2	PROBE TESTS	\N	\N	\N	\N	\N	\N	ANALYTE
17	F	15	galactose	trehalose	T1	.	\N	\N	\N	\N	\N	\N	ANALYTE
16	F	14	lactose	lactose	37 degrees	lysozyme	\N	\N	\N	\N	\N	\N	ANALYTE
15	F	13	sucrose	sucrose	RT incubation	xanthine	\N	\N	\N	\N	\N	\N	ANALYTE
14	F	12	maltose	maltose	sabs without CH	tyrosine	\N	\N	\N	\N	\N	\N	ANALYTE
13	F	11	dextrose	dextrose	sabs with CH	casein	\N	\N	\N	\N	\N	\N	ANALYTE
12	F	10	ASSIMILATIONS	FERMENTATIONS	FILAMENTOUS	ACTINOMYCETES	\N	\N	\N	\N	\N	\N	ANALYTE
62	US VDRL	1	QL	QN:2	4	8	16	32	64	128	256	Final	RESULT
93	US USR	15	Titer	.	.	.	.	.	.	.	\N	\N	QC
96	US FTA	10	\N	\N	\N	\N	TPPA - control	.	.	.	-	.	QC
95	US FTA	9	\N	\N	\N	\N	TPPA + control	.	.	.	+	.	QC
49	US USR	11	CONTROLS	SOURCE	LOT	EXPIRE DATE	RESULTS Qualitative Expect	RESULTS Qualitative  Actual	RESULTS Quantitative Expect	RESULTS Quantitative Actual	\N	\N	QC
48	US USR	10	Rotation of Slides	178-182	__________	rotations/min	\N	\N	\N	\N	\N	\N	QC
47	US USR	9	Temp of Lab	23-29	__________	degrees C	\N	\N	\N	\N	\N	\N	QC
46	US USR	6	Sterile DI H20	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
45	US USR	8	USR working antigen	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
44	US USR	5	Phosphate Buffer	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
43	US USR	4	EDTA	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
42	US USR	3	Choline Chloride	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
41	US USR	2	VDRL Antigen Kit	Difco	.	.	\N	\N	\N	\N	\N	\N	QC
40	US USR	1	REAGENT	SOURCE	LOT	EXPIRE DATE	\N	\N	\N	\N	\N	\N	QC
52	US USR	14	NC	Difco	.	.	NR	.	.	.	\N	\N	QC
51	US USR	13	LPC	Difco	.	.	WR	.	.	.	\N	\N	QC
50	US USR	12	HPC	Difco	.	.	R	.	.	.	\N	\N	QC
55	HSV TYPING	3	Expiration Date:	________________	Negative Cells:	Lot _____________	\N	\N	\N	\N	\N	\N	QC
54	HSV TYPING	2	Lot:	________________	HSV2 Positive Cells:	Lot _____________	\N	\N	\N	\N	\N	\N	QC
53	HSV TYPING	1	HSV1 / HSV2 Typing Kit:	________________	HSV1 Positive Cells:	Lot _____________	\N	\N	\N	\N	\N	\N	QC
57	HSV TYPING	2	HSV-1 PC	.	.	.	.	.	.	.	\N	\N	RESULT
58	HSV TYPING	3	HSV-1 nc	.	.	.	.	.	.	.	\N	\N	RESULT
60	HSV TYPING	5	HSV-2 nc	.	.	.	.	.	.	.	\N	\N	RESULT
59	HSV TYPING	4	HSV-2 PC	.	.	.	.	.	.	.	\N	\N	RESULT
74	MICRO	6	Pigment	Mannitol	Casein	Gelatin	\N	\N	\N	\N	\N	\N	ANALYTE
73	MICRO	5	Hemolysis	Maltose	Trehalose	Litmus Milk	\N	\N	\N	\N	\N	\N	ANALYTE
72	MICRO	4	O2 Relation	Sucrose	Sorbose	Ornithine	\N	\N	\N	\N	\N	\N	ANALYTE
92	STREP	11	\N	\N	\N	\N	PFGE	\N	\N	\N	\N	\N	ANALYTE
35	US VDRL	8	Rotation of Slides	.	178 - 182	__________	rotation/min	\N	\N	\N	\N	\N	QC
34	US VDRL	7	Temp of Lab	.	23 - 29	__________	degrees C	\N	\N	\N	\N	\N	QC
36	US VDRL	9	CONTROLS	SOURCE	LOT	EXPIRE DATE	RESULT Qualitative Expect	RESULT Qualitative  Actual	RESULT Quantitative Expect	RESULT Quantitative Actual	\N	\N	QC
56	HSV TYPING	1	Specimen ID	Received Date	Source	Pass	HSV1	HSV2	Comments	Mailer	\N	\N	RESULT
27	VI	1	Comments	Results	Interpretation	\N	\N	\N	\N	\N	\N	\N	RESULT
32	US VDRL	5	VDRL working antigen	.	.	.	\N	\N	\N	\N	\N	\N	QC
31	US VDRL	4	10% Saline	.	.	.	\N	\N	\N	\N	\N	\N	QC
30	US VDRL	3	Kahn Saline 0.9%	.	.	.	\N	\N	\N	\N	\N	\N	QC
29	US VDRL	2	VDRL Antigen Kit	Difco	.	.	\N	\N	\N	\N	\N	\N	QC
28	US VDRL	1	REAGENT	SOURCE	LOT	EXPIRE DATE	\N	\N	\N	\N	\N	\N	QC
38	US VDRL	11	LPC	Difco	.	.	WR	.	.	.	\N	\N	QC
37	US VDRL	10	HPC	Difco	.	.	R	.	.	.	\N	\N	QC
39	US VDRL	12	NC	Difco	.	.	NR	.	.	.	\N	\N	QC
61	US USR	1	QL	QN: 2	4	8	16	32	64	128	256	Final	RESULT
68	STREP	6	Factors	PFGE	\N	Catalase	Lact	\N	\N	\N	\N	\N	ANALYTE
67	STREP	5	Group	Latex	Latex	Kilian	Suc	\N	\N	\N	\N	\N	ANALYTE
66	STREP	4	Typing pool	PYR	Hipp	Satellites	Malt	\N	\N	\N	\N	\N	ANALYTE
65	STREP	3	Bile sol	A disc	Camp	X+V	Dex	\N	\N	\N	\N	\N	ANALYTE
64	STREP	2	P disc	GmSt	GmSt	GmSt	GmSt	\N	\N	\N	\N	\N	ANALYTE
63	STREP	1	Strep pneumo	Group A Strep	Group B Strep	H Influenzae	N. meningitidis	\N	\N	\N	\N	\N	ANALYTE
70	MICRO	3	Spores	Lactose	Sorbitol	Arginine	\N	\N	\N	\N	\N	\N	ANALYTE
69	MICRO	2	42	Glucose	Salicin	Lysine	\N	\N	\N	\N	\N	\N	ANALYTE
71	MICRO	1	Colony morphology	.	Gram morphology	.	\N	\N	\N	\N	\N	\N	ANALYTE
79	MICRO	11	MacConkey	Fructose	Motility	Pyruvate	\N	\N	\N	\N	\N	\N	ANALYTE
78	MICRO	10	Serology	Inulin	.	6.5% salt	\N	\N	\N	\N	\N	\N	ANALYTE
77	MICRO	9	Satellites	Cellibiose	Alk Phos	Bile Esculin	\N	\N	\N	\N	\N	\N	ANALYTE
76	MICRO	8	Kilian test	Arabinose	PZA	Esculin	\N	\N	\N	\N	\N	\N	ANALYTE
75	MICRO	7	X+V Req	Xylose	Starch	Cetrimide	\N	\N	\N	\N	\N	\N	ANALYTE
87	MICRO	19	Lecthinase	Ribose	Phenylalanine	.	\N	\N	\N	\N	\N	\N	ANALYTE
86	MICRO	18	Lipase	Rhamnose	Acetate	FREEZE	\N	\N	\N	\N	\N	\N	ANALYTE
85	MICRO	17	.	Raffinose	Citrate	PFGE	\N	\N	\N	\N	\N	\N	ANALYTE
84	MICRO	16	Optochin	MMP	MR         VP	LAP         PYR	\N	\N	\N	\N	\N	\N	ANALYTE
83	MICRO	15	Camp	Melibiose	Indol	Erythromycin	\N	\N	\N	\N	\N	\N	ANALYTE
82	MICRO	14	Bacitracin	Mannose	Nitite	Vancomycin	\N	\N	\N	\N	\N	\N	ANALYTE
81	MICRO	13	Catalase	Glycerol	Nitrate	Coagulase	\N	\N	\N	\N	\N	\N	ANALYTE
80	MICRO	12	Oxidase	Galactose	Urease	MGP	\N	\N	\N	\N	\N	\N	ANALYTE
91	STREP	10	\N	\N	\N	\N	Serology	\N	\N	\N	\N	\N	ANALYTE
90	STREP	9	\N	\N	\N	\N	TM	\N	\N	\N	\N	\N	ANALYTE
89	STREP	8	\N	\N	\N	Serology	Oxidase	\N	\N	\N	\N	\N	ANALYTE
88	STREP	7	\N	\N	\N	Oxidase	Catalase	\N	\N	\N	\N	\N	ANALYTE
94	US USR	7	Kahn's solution	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
1	TB	1	Micro Result	Culture Result	Report Date	\N	\N	\N	\N	\N	\N	\N	RESULT
2	GC	1	12 HOUR	24 HOUR	48 HOUR	\N	\N	\N	\N	\N	\N	\N	RESULT
9	US FTA	7	\N	\N	\N	\N	PBS	.	.	.	-	.	QC
10	US FTA	8	\N	\N	\N	\N	Sorbent	.	.	.	-	.	QC
8	US FTA	6	PBS	.	.	.	Nonspecific Control / Sorbent	.	.	.	-	.	QC
11	US FTA	1	    USR     Repeat Quant	   FTA      Current Run	    FTA     Previous Runs	   FTA         Final	  TPPA       Result	Comments	\N	\N	\N	\N	RESULT
26	US FTA	1	TEST	TESTing	\N	\N	\N	\N	\N	\N	\N	\N	ANALYTE
7	US FTA	5	.	.	.	.	Nonspecific Control/ PBS	.	.	.	>2+	.	QC
6	US FTA	4	FITCconjugate Working Dilution:	.	.	.	Minimally Reactive Control	.	.	.	1+	.	QC
5	US FTA	3	Sorbent	.	.	.	Reactive Control / Sorbent	.	.	.	3-4+	.	QC
4	US FTA	2	T Palladium antigen	.	.	.	Reactive Control/PBS	.	.	.	4+	.	QC
3	US FTA	1	Reagent	Source	Lot	Expire Date	Reagent	Source	Lot	Expires	Expect	Actual	QC
25	F	23	KNO3	RT incubation	.	.	\N	\N	\N	\N	\N	\N	ANALYTE
24	F	22	dulcitol	chlamydospores	.	.	\N	\N	\N	\N	\N	\N	ANALYTE
23	F	21	melibiose	ascospores	NO3	.	\N	\N	\N	\N	\N	\N	ANALYTE
22	F	20	trehalose	pigment	gelatin	.	\N	\N	\N	\N	\N	\N	ANALYTE
21	F	19	xylose	germ tubes	urea	probe (histo)	\N	\N	\N	\N	\N	\N	ANALYTE
20	F	18	inositol	urea	T4	probe (blasto)	\N	\N	\N	\N	\N	\N	ANALYTE
19	F	17	raffinose	OTHER TESTS	T3	probe (cocci)	\N	\N	\N	\N	\N	\N	ANALYTE
18	F	16	cellobiose	galactose	T2	PROBE TESTS	\N	\N	\N	\N	\N	\N	ANALYTE
17	F	15	galactose	trehalose	T1	.	\N	\N	\N	\N	\N	\N	ANALYTE
16	F	14	lactose	lactose	37 degrees	lysozyme	\N	\N	\N	\N	\N	\N	ANALYTE
15	F	13	sucrose	sucrose	RT incubation	xanthine	\N	\N	\N	\N	\N	\N	ANALYTE
14	F	12	maltose	maltose	sabs without CH	tyrosine	\N	\N	\N	\N	\N	\N	ANALYTE
13	F	11	dextrose	dextrose	sabs with CH	casein	\N	\N	\N	\N	\N	\N	ANALYTE
12	F	10	ASSIMILATIONS	FERMENTATIONS	FILAMENTOUS	ACTINOMYCETES	\N	\N	\N	\N	\N	\N	ANALYTE
62	US VDRL	1	QL	QN:2	4	8	16	32	64	128	256	Final	RESULT
93	US USR	15	Titer	.	.	.	.	.	.	.	\N	\N	QC
96	US FTA	10	\N	\N	\N	\N	TPPA - control	.	.	.	-	.	QC
95	US FTA	9	\N	\N	\N	\N	TPPA + control	.	.	.	+	.	QC
49	US USR	11	CONTROLS	SOURCE	LOT	EXPIRE DATE	RESULTS Qualitative Expect	RESULTS Qualitative  Actual	RESULTS Quantitative Expect	RESULTS Quantitative Actual	\N	\N	QC
48	US USR	10	Rotation of Slides	178-182	__________	rotations/min	\N	\N	\N	\N	\N	\N	QC
47	US USR	9	Temp of Lab	23-29	__________	degrees C	\N	\N	\N	\N	\N	\N	QC
46	US USR	6	Sterile DI H20	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
45	US USR	8	USR working antigen	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
44	US USR	5	Phosphate Buffer	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
43	US USR	4	EDTA	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
42	US USR	3	Choline Chloride	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
41	US USR	2	VDRL Antigen Kit	Difco	.	.	\N	\N	\N	\N	\N	\N	QC
40	US USR	1	REAGENT	SOURCE	LOT	EXPIRE DATE	\N	\N	\N	\N	\N	\N	QC
52	US USR	14	NC	Difco	.	.	NR	.	.	.	\N	\N	QC
51	US USR	13	LPC	Difco	.	.	WR	.	.	.	\N	\N	QC
50	US USR	12	HPC	Difco	.	.	R	.	.	.	\N	\N	QC
55	HSV TYPING	3	Expiration Date:	________________	Negative Cells:	Lot _____________	\N	\N	\N	\N	\N	\N	QC
54	HSV TYPING	2	Lot:	________________	HSV2 Positive Cells:	Lot _____________	\N	\N	\N	\N	\N	\N	QC
53	HSV TYPING	1	HSV1 / HSV2 Typing Kit:	________________	HSV1 Positive Cells:	Lot _____________	\N	\N	\N	\N	\N	\N	QC
57	HSV TYPING	2	HSV-1 PC	.	.	.	.	.	.	.	\N	\N	RESULT
58	HSV TYPING	3	HSV-1 nc	.	.	.	.	.	.	.	\N	\N	RESULT
60	HSV TYPING	5	HSV-2 nc	.	.	.	.	.	.	.	\N	\N	RESULT
59	HSV TYPING	4	HSV-2 PC	.	.	.	.	.	.	.	\N	\N	RESULT
74	MICRO	6	Pigment	Mannitol	Casein	Gelatin	\N	\N	\N	\N	\N	\N	ANALYTE
73	MICRO	5	Hemolysis	Maltose	Trehalose	Litmus Milk	\N	\N	\N	\N	\N	\N	ANALYTE
72	MICRO	4	O2 Relation	Sucrose	Sorbose	Ornithine	\N	\N	\N	\N	\N	\N	ANALYTE
92	STREP	11	\N	\N	\N	\N	PFGE	\N	\N	\N	\N	\N	ANALYTE
35	US VDRL	8	Rotation of Slides	.	178 - 182	__________	rotation/min	\N	\N	\N	\N	\N	QC
34	US VDRL	7	Temp of Lab	.	23 - 29	__________	degrees C	\N	\N	\N	\N	\N	QC
36	US VDRL	9	CONTROLS	SOURCE	LOT	EXPIRE DATE	RESULT Qualitative Expect	RESULT Qualitative  Actual	RESULT Quantitative Expect	RESULT Quantitative Actual	\N	\N	QC
56	HSV TYPING	1	Specimen ID	Received Date	Source	Pass	HSV1	HSV2	Comments	Mailer	\N	\N	RESULT
27	VI	1	Comments	Results	Interpretation	\N	\N	\N	\N	\N	\N	\N	RESULT
32	US VDRL	5	VDRL working antigen	.	.	.	\N	\N	\N	\N	\N	\N	QC
31	US VDRL	4	10% Saline	.	.	.	\N	\N	\N	\N	\N	\N	QC
30	US VDRL	3	Kahn Saline 0.9%	.	.	.	\N	\N	\N	\N	\N	\N	QC
29	US VDRL	2	VDRL Antigen Kit	Difco	.	.	\N	\N	\N	\N	\N	\N	QC
28	US VDRL	1	REAGENT	SOURCE	LOT	EXPIRE DATE	\N	\N	\N	\N	\N	\N	QC
38	US VDRL	11	LPC	Difco	.	.	WR	.	.	.	\N	\N	QC
37	US VDRL	10	HPC	Difco	.	.	R	.	.	.	\N	\N	QC
39	US VDRL	12	NC	Difco	.	.	NR	.	.	.	\N	\N	QC
61	US USR	1	QL	QN: 2	4	8	16	32	64	128	256	Final	RESULT
68	STREP	6	Factors	PFGE	\N	Catalase	Lact	\N	\N	\N	\N	\N	ANALYTE
67	STREP	5	Group	Latex	Latex	Kilian	Suc	\N	\N	\N	\N	\N	ANALYTE
66	STREP	4	Typing pool	PYR	Hipp	Satellites	Malt	\N	\N	\N	\N	\N	ANALYTE
65	STREP	3	Bile sol	A disc	Camp	X+V	Dex	\N	\N	\N	\N	\N	ANALYTE
64	STREP	2	P disc	GmSt	GmSt	GmSt	GmSt	\N	\N	\N	\N	\N	ANALYTE
63	STREP	1	Strep pneumo	Group A Strep	Group B Strep	H Influenzae	N. meningitidis	\N	\N	\N	\N	\N	ANALYTE
70	MICRO	3	Spores	Lactose	Sorbitol	Arginine	\N	\N	\N	\N	\N	\N	ANALYTE
69	MICRO	2	42	Glucose	Salicin	Lysine	\N	\N	\N	\N	\N	\N	ANALYTE
71	MICRO	1	Colony morphology	.	Gram morphology	.	\N	\N	\N	\N	\N	\N	ANALYTE
79	MICRO	11	MacConkey	Fructose	Motility	Pyruvate	\N	\N	\N	\N	\N	\N	ANALYTE
78	MICRO	10	Serology	Inulin	.	6.5% salt	\N	\N	\N	\N	\N	\N	ANALYTE
77	MICRO	9	Satellites	Cellibiose	Alk Phos	Bile Esculin	\N	\N	\N	\N	\N	\N	ANALYTE
76	MICRO	8	Kilian test	Arabinose	PZA	Esculin	\N	\N	\N	\N	\N	\N	ANALYTE
75	MICRO	7	X+V Req	Xylose	Starch	Cetrimide	\N	\N	\N	\N	\N	\N	ANALYTE
87	MICRO	19	Lecthinase	Ribose	Phenylalanine	.	\N	\N	\N	\N	\N	\N	ANALYTE
86	MICRO	18	Lipase	Rhamnose	Acetate	FREEZE	\N	\N	\N	\N	\N	\N	ANALYTE
85	MICRO	17	.	Raffinose	Citrate	PFGE	\N	\N	\N	\N	\N	\N	ANALYTE
84	MICRO	16	Optochin	MMP	MR         VP	LAP         PYR	\N	\N	\N	\N	\N	\N	ANALYTE
83	MICRO	15	Camp	Melibiose	Indol	Erythromycin	\N	\N	\N	\N	\N	\N	ANALYTE
82	MICRO	14	Bacitracin	Mannose	Nitite	Vancomycin	\N	\N	\N	\N	\N	\N	ANALYTE
81	MICRO	13	Catalase	Glycerol	Nitrate	Coagulase	\N	\N	\N	\N	\N	\N	ANALYTE
80	MICRO	12	Oxidase	Galactose	Urease	MGP	\N	\N	\N	\N	\N	\N	ANALYTE
91	STREP	10	\N	\N	\N	\N	Serology	\N	\N	\N	\N	\N	ANALYTE
90	STREP	9	\N	\N	\N	\N	TM	\N	\N	\N	\N	\N	ANALYTE
89	STREP	8	\N	\N	\N	Serology	Oxidase	\N	\N	\N	\N	\N	ANALYTE
88	STREP	7	\N	\N	\N	Oxidase	Catalase	\N	\N	\N	\N	\N	ANALYTE
94	US USR	7	Kahn's solution	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
1	TB	1	Micro Result	Culture Result	Report Date	\N	\N	\N	\N	\N	\N	\N	RESULT
2	GC	1	12 HOUR	24 HOUR	48 HOUR	\N	\N	\N	\N	\N	\N	\N	RESULT
9	US FTA	7	\N	\N	\N	\N	PBS	.	.	.	-	.	QC
10	US FTA	8	\N	\N	\N	\N	Sorbent	.	.	.	-	.	QC
8	US FTA	6	PBS	.	.	.	Nonspecific Control / Sorbent	.	.	.	-	.	QC
11	US FTA	1	    USR     Repeat Quant	   FTA      Current Run	    FTA     Previous Runs	   FTA         Final	  TPPA       Result	Comments	\N	\N	\N	\N	RESULT
26	US FTA	1	TEST	TESTing	\N	\N	\N	\N	\N	\N	\N	\N	ANALYTE
7	US FTA	5	.	.	.	.	Nonspecific Control/ PBS	.	.	.	>2+	.	QC
6	US FTA	4	FITCconjugate Working Dilution:	.	.	.	Minimally Reactive Control	.	.	.	1+	.	QC
5	US FTA	3	Sorbent	.	.	.	Reactive Control / Sorbent	.	.	.	3-4+	.	QC
4	US FTA	2	T Palladium antigen	.	.	.	Reactive Control/PBS	.	.	.	4+	.	QC
3	US FTA	1	Reagent	Source	Lot	Expire Date	Reagent	Source	Lot	Expires	Expect	Actual	QC
25	F	23	KNO3	RT incubation	.	.	\N	\N	\N	\N	\N	\N	ANALYTE
24	F	22	dulcitol	chlamydospores	.	.	\N	\N	\N	\N	\N	\N	ANALYTE
23	F	21	melibiose	ascospores	NO3	.	\N	\N	\N	\N	\N	\N	ANALYTE
22	F	20	trehalose	pigment	gelatin	.	\N	\N	\N	\N	\N	\N	ANALYTE
21	F	19	xylose	germ tubes	urea	probe (histo)	\N	\N	\N	\N	\N	\N	ANALYTE
20	F	18	inositol	urea	T4	probe (blasto)	\N	\N	\N	\N	\N	\N	ANALYTE
19	F	17	raffinose	OTHER TESTS	T3	probe (cocci)	\N	\N	\N	\N	\N	\N	ANALYTE
18	F	16	cellobiose	galactose	T2	PROBE TESTS	\N	\N	\N	\N	\N	\N	ANALYTE
17	F	15	galactose	trehalose	T1	.	\N	\N	\N	\N	\N	\N	ANALYTE
16	F	14	lactose	lactose	37 degrees	lysozyme	\N	\N	\N	\N	\N	\N	ANALYTE
15	F	13	sucrose	sucrose	RT incubation	xanthine	\N	\N	\N	\N	\N	\N	ANALYTE
14	F	12	maltose	maltose	sabs without CH	tyrosine	\N	\N	\N	\N	\N	\N	ANALYTE
13	F	11	dextrose	dextrose	sabs with CH	casein	\N	\N	\N	\N	\N	\N	ANALYTE
12	F	10	ASSIMILATIONS	FERMENTATIONS	FILAMENTOUS	ACTINOMYCETES	\N	\N	\N	\N	\N	\N	ANALYTE
62	US VDRL	1	QL	QN:2	4	8	16	32	64	128	256	Final	RESULT
93	US USR	15	Titer	.	.	.	.	.	.	.	\N	\N	QC
96	US FTA	10	\N	\N	\N	\N	TPPA - control	.	.	.	-	.	QC
95	US FTA	9	\N	\N	\N	\N	TPPA + control	.	.	.	+	.	QC
49	US USR	11	CONTROLS	SOURCE	LOT	EXPIRE DATE	RESULTS Qualitative Expect	RESULTS Qualitative  Actual	RESULTS Quantitative Expect	RESULTS Quantitative Actual	\N	\N	QC
48	US USR	10	Rotation of Slides	178-182	__________	rotations/min	\N	\N	\N	\N	\N	\N	QC
47	US USR	9	Temp of Lab	23-29	__________	degrees C	\N	\N	\N	\N	\N	\N	QC
46	US USR	6	Sterile DI H20	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
45	US USR	8	USR working antigen	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
44	US USR	5	Phosphate Buffer	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
43	US USR	4	EDTA	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
42	US USR	3	Choline Chloride	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
41	US USR	2	VDRL Antigen Kit	Difco	.	.	\N	\N	\N	\N	\N	\N	QC
40	US USR	1	REAGENT	SOURCE	LOT	EXPIRE DATE	\N	\N	\N	\N	\N	\N	QC
52	US USR	14	NC	Difco	.	.	NR	.	.	.	\N	\N	QC
51	US USR	13	LPC	Difco	.	.	WR	.	.	.	\N	\N	QC
50	US USR	12	HPC	Difco	.	.	R	.	.	.	\N	\N	QC
55	HSV TYPING	3	Expiration Date:	________________	Negative Cells:	Lot _____________	\N	\N	\N	\N	\N	\N	QC
54	HSV TYPING	2	Lot:	________________	HSV2 Positive Cells:	Lot _____________	\N	\N	\N	\N	\N	\N	QC
53	HSV TYPING	1	HSV1 / HSV2 Typing Kit:	________________	HSV1 Positive Cells:	Lot _____________	\N	\N	\N	\N	\N	\N	QC
57	HSV TYPING	2	HSV-1 PC	.	.	.	.	.	.	.	\N	\N	RESULT
58	HSV TYPING	3	HSV-1 nc	.	.	.	.	.	.	.	\N	\N	RESULT
60	HSV TYPING	5	HSV-2 nc	.	.	.	.	.	.	.	\N	\N	RESULT
59	HSV TYPING	4	HSV-2 PC	.	.	.	.	.	.	.	\N	\N	RESULT
74	MICRO	6	Pigment	Mannitol	Casein	Gelatin	\N	\N	\N	\N	\N	\N	ANALYTE
73	MICRO	5	Hemolysis	Maltose	Trehalose	Litmus Milk	\N	\N	\N	\N	\N	\N	ANALYTE
72	MICRO	4	O2 Relation	Sucrose	Sorbose	Ornithine	\N	\N	\N	\N	\N	\N	ANALYTE
92	STREP	11	\N	\N	\N	\N	PFGE	\N	\N	\N	\N	\N	ANALYTE
35	US VDRL	8	Rotation of Slides	.	178 - 182	__________	rotation/min	\N	\N	\N	\N	\N	QC
34	US VDRL	7	Temp of Lab	.	23 - 29	__________	degrees C	\N	\N	\N	\N	\N	QC
36	US VDRL	9	CONTROLS	SOURCE	LOT	EXPIRE DATE	RESULT Qualitative Expect	RESULT Qualitative  Actual	RESULT Quantitative Expect	RESULT Quantitative Actual	\N	\N	QC
56	HSV TYPING	1	Specimen ID	Received Date	Source	Pass	HSV1	HSV2	Comments	Mailer	\N	\N	RESULT
27	VI	1	Comments	Results	Interpretation	\N	\N	\N	\N	\N	\N	\N	RESULT
32	US VDRL	5	VDRL working antigen	.	.	.	\N	\N	\N	\N	\N	\N	QC
31	US VDRL	4	10% Saline	.	.	.	\N	\N	\N	\N	\N	\N	QC
30	US VDRL	3	Kahn Saline 0.9%	.	.	.	\N	\N	\N	\N	\N	\N	QC
29	US VDRL	2	VDRL Antigen Kit	Difco	.	.	\N	\N	\N	\N	\N	\N	QC
28	US VDRL	1	REAGENT	SOURCE	LOT	EXPIRE DATE	\N	\N	\N	\N	\N	\N	QC
38	US VDRL	11	LPC	Difco	.	.	WR	.	.	.	\N	\N	QC
37	US VDRL	10	HPC	Difco	.	.	R	.	.	.	\N	\N	QC
39	US VDRL	12	NC	Difco	.	.	NR	.	.	.	\N	\N	QC
61	US USR	1	QL	QN: 2	4	8	16	32	64	128	256	Final	RESULT
68	STREP	6	Factors	PFGE	\N	Catalase	Lact	\N	\N	\N	\N	\N	ANALYTE
67	STREP	5	Group	Latex	Latex	Kilian	Suc	\N	\N	\N	\N	\N	ANALYTE
66	STREP	4	Typing pool	PYR	Hipp	Satellites	Malt	\N	\N	\N	\N	\N	ANALYTE
65	STREP	3	Bile sol	A disc	Camp	X+V	Dex	\N	\N	\N	\N	\N	ANALYTE
64	STREP	2	P disc	GmSt	GmSt	GmSt	GmSt	\N	\N	\N	\N	\N	ANALYTE
63	STREP	1	Strep pneumo	Group A Strep	Group B Strep	H Influenzae	N. meningitidis	\N	\N	\N	\N	\N	ANALYTE
70	MICRO	3	Spores	Lactose	Sorbitol	Arginine	\N	\N	\N	\N	\N	\N	ANALYTE
69	MICRO	2	42	Glucose	Salicin	Lysine	\N	\N	\N	\N	\N	\N	ANALYTE
71	MICRO	1	Colony morphology	.	Gram morphology	.	\N	\N	\N	\N	\N	\N	ANALYTE
79	MICRO	11	MacConkey	Fructose	Motility	Pyruvate	\N	\N	\N	\N	\N	\N	ANALYTE
78	MICRO	10	Serology	Inulin	.	6.5% salt	\N	\N	\N	\N	\N	\N	ANALYTE
77	MICRO	9	Satellites	Cellibiose	Alk Phos	Bile Esculin	\N	\N	\N	\N	\N	\N	ANALYTE
76	MICRO	8	Kilian test	Arabinose	PZA	Esculin	\N	\N	\N	\N	\N	\N	ANALYTE
75	MICRO	7	X+V Req	Xylose	Starch	Cetrimide	\N	\N	\N	\N	\N	\N	ANALYTE
87	MICRO	19	Lecthinase	Ribose	Phenylalanine	.	\N	\N	\N	\N	\N	\N	ANALYTE
86	MICRO	18	Lipase	Rhamnose	Acetate	FREEZE	\N	\N	\N	\N	\N	\N	ANALYTE
85	MICRO	17	.	Raffinose	Citrate	PFGE	\N	\N	\N	\N	\N	\N	ANALYTE
84	MICRO	16	Optochin	MMP	MR         VP	LAP         PYR	\N	\N	\N	\N	\N	\N	ANALYTE
83	MICRO	15	Camp	Melibiose	Indol	Erythromycin	\N	\N	\N	\N	\N	\N	ANALYTE
82	MICRO	14	Bacitracin	Mannose	Nitite	Vancomycin	\N	\N	\N	\N	\N	\N	ANALYTE
81	MICRO	13	Catalase	Glycerol	Nitrate	Coagulase	\N	\N	\N	\N	\N	\N	ANALYTE
80	MICRO	12	Oxidase	Galactose	Urease	MGP	\N	\N	\N	\N	\N	\N	ANALYTE
91	STREP	10	\N	\N	\N	\N	Serology	\N	\N	\N	\N	\N	ANALYTE
90	STREP	9	\N	\N	\N	\N	TM	\N	\N	\N	\N	\N	ANALYTE
89	STREP	8	\N	\N	\N	Serology	Oxidase	\N	\N	\N	\N	\N	ANALYTE
88	STREP	7	\N	\N	\N	Oxidase	Catalase	\N	\N	\N	\N	\N	ANALYTE
94	US USR	7	Kahn's solution	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
1	TB	1	Micro Result	Culture Result	Report Date	\N	\N	\N	\N	\N	\N	\N	RESULT
2	GC	1	12 HOUR	24 HOUR	48 HOUR	\N	\N	\N	\N	\N	\N	\N	RESULT
9	US FTA	7	\N	\N	\N	\N	PBS	.	.	.	-	.	QC
10	US FTA	8	\N	\N	\N	\N	Sorbent	.	.	.	-	.	QC
8	US FTA	6	PBS	.	.	.	Nonspecific Control / Sorbent	.	.	.	-	.	QC
11	US FTA	1	    USR     Repeat Quant	   FTA      Current Run	    FTA     Previous Runs	   FTA         Final	  TPPA       Result	Comments	\N	\N	\N	\N	RESULT
26	US FTA	1	TEST	TESTing	\N	\N	\N	\N	\N	\N	\N	\N	ANALYTE
7	US FTA	5	.	.	.	.	Nonspecific Control/ PBS	.	.	.	>2+	.	QC
6	US FTA	4	FITCconjugate Working Dilution:	.	.	.	Minimally Reactive Control	.	.	.	1+	.	QC
5	US FTA	3	Sorbent	.	.	.	Reactive Control / Sorbent	.	.	.	3-4+	.	QC
4	US FTA	2	T Palladium antigen	.	.	.	Reactive Control/PBS	.	.	.	4+	.	QC
3	US FTA	1	Reagent	Source	Lot	Expire Date	Reagent	Source	Lot	Expires	Expect	Actual	QC
25	F	23	KNO3	RT incubation	.	.	\N	\N	\N	\N	\N	\N	ANALYTE
24	F	22	dulcitol	chlamydospores	.	.	\N	\N	\N	\N	\N	\N	ANALYTE
23	F	21	melibiose	ascospores	NO3	.	\N	\N	\N	\N	\N	\N	ANALYTE
22	F	20	trehalose	pigment	gelatin	.	\N	\N	\N	\N	\N	\N	ANALYTE
21	F	19	xylose	germ tubes	urea	probe (histo)	\N	\N	\N	\N	\N	\N	ANALYTE
20	F	18	inositol	urea	T4	probe (blasto)	\N	\N	\N	\N	\N	\N	ANALYTE
19	F	17	raffinose	OTHER TESTS	T3	probe (cocci)	\N	\N	\N	\N	\N	\N	ANALYTE
18	F	16	cellobiose	galactose	T2	PROBE TESTS	\N	\N	\N	\N	\N	\N	ANALYTE
17	F	15	galactose	trehalose	T1	.	\N	\N	\N	\N	\N	\N	ANALYTE
16	F	14	lactose	lactose	37 degrees	lysozyme	\N	\N	\N	\N	\N	\N	ANALYTE
15	F	13	sucrose	sucrose	RT incubation	xanthine	\N	\N	\N	\N	\N	\N	ANALYTE
14	F	12	maltose	maltose	sabs without CH	tyrosine	\N	\N	\N	\N	\N	\N	ANALYTE
13	F	11	dextrose	dextrose	sabs with CH	casein	\N	\N	\N	\N	\N	\N	ANALYTE
12	F	10	ASSIMILATIONS	FERMENTATIONS	FILAMENTOUS	ACTINOMYCETES	\N	\N	\N	\N	\N	\N	ANALYTE
62	US VDRL	1	QL	QN:2	4	8	16	32	64	128	256	Final	RESULT
93	US USR	15	Titer	.	.	.	.	.	.	.	\N	\N	QC
96	US FTA	10	\N	\N	\N	\N	TPPA - control	.	.	.	-	.	QC
95	US FTA	9	\N	\N	\N	\N	TPPA + control	.	.	.	+	.	QC
49	US USR	11	CONTROLS	SOURCE	LOT	EXPIRE DATE	RESULTS Qualitative Expect	RESULTS Qualitative  Actual	RESULTS Quantitative Expect	RESULTS Quantitative Actual	\N	\N	QC
48	US USR	10	Rotation of Slides	178-182	__________	rotations/min	\N	\N	\N	\N	\N	\N	QC
47	US USR	9	Temp of Lab	23-29	__________	degrees C	\N	\N	\N	\N	\N	\N	QC
46	US USR	6	Sterile DI H20	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
45	US USR	8	USR working antigen	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
44	US USR	5	Phosphate Buffer	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
43	US USR	4	EDTA	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
42	US USR	3	Choline Chloride	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
41	US USR	2	VDRL Antigen Kit	Difco	.	.	\N	\N	\N	\N	\N	\N	QC
40	US USR	1	REAGENT	SOURCE	LOT	EXPIRE DATE	\N	\N	\N	\N	\N	\N	QC
52	US USR	14	NC	Difco	.	.	NR	.	.	.	\N	\N	QC
51	US USR	13	LPC	Difco	.	.	WR	.	.	.	\N	\N	QC
50	US USR	12	HPC	Difco	.	.	R	.	.	.	\N	\N	QC
55	HSV TYPING	3	Expiration Date:	________________	Negative Cells:	Lot _____________	\N	\N	\N	\N	\N	\N	QC
54	HSV TYPING	2	Lot:	________________	HSV2 Positive Cells:	Lot _____________	\N	\N	\N	\N	\N	\N	QC
53	HSV TYPING	1	HSV1 / HSV2 Typing Kit:	________________	HSV1 Positive Cells:	Lot _____________	\N	\N	\N	\N	\N	\N	QC
57	HSV TYPING	2	HSV-1 PC	.	.	.	.	.	.	.	\N	\N	RESULT
58	HSV TYPING	3	HSV-1 nc	.	.	.	.	.	.	.	\N	\N	RESULT
60	HSV TYPING	5	HSV-2 nc	.	.	.	.	.	.	.	\N	\N	RESULT
59	HSV TYPING	4	HSV-2 PC	.	.	.	.	.	.	.	\N	\N	RESULT
74	MICRO	6	Pigment	Mannitol	Casein	Gelatin	\N	\N	\N	\N	\N	\N	ANALYTE
73	MICRO	5	Hemolysis	Maltose	Trehalose	Litmus Milk	\N	\N	\N	\N	\N	\N	ANALYTE
72	MICRO	4	O2 Relation	Sucrose	Sorbose	Ornithine	\N	\N	\N	\N	\N	\N	ANALYTE
92	STREP	11	\N	\N	\N	\N	PFGE	\N	\N	\N	\N	\N	ANALYTE
35	US VDRL	8	Rotation of Slides	.	178 - 182	__________	rotation/min	\N	\N	\N	\N	\N	QC
34	US VDRL	7	Temp of Lab	.	23 - 29	__________	degrees C	\N	\N	\N	\N	\N	QC
36	US VDRL	9	CONTROLS	SOURCE	LOT	EXPIRE DATE	RESULT Qualitative Expect	RESULT Qualitative  Actual	RESULT Quantitative Expect	RESULT Quantitative Actual	\N	\N	QC
56	HSV TYPING	1	Specimen ID	Received Date	Source	Pass	HSV1	HSV2	Comments	Mailer	\N	\N	RESULT
27	VI	1	Comments	Results	Interpretation	\N	\N	\N	\N	\N	\N	\N	RESULT
32	US VDRL	5	VDRL working antigen	.	.	.	\N	\N	\N	\N	\N	\N	QC
31	US VDRL	4	10% Saline	.	.	.	\N	\N	\N	\N	\N	\N	QC
30	US VDRL	3	Kahn Saline 0.9%	.	.	.	\N	\N	\N	\N	\N	\N	QC
29	US VDRL	2	VDRL Antigen Kit	Difco	.	.	\N	\N	\N	\N	\N	\N	QC
28	US VDRL	1	REAGENT	SOURCE	LOT	EXPIRE DATE	\N	\N	\N	\N	\N	\N	QC
38	US VDRL	11	LPC	Difco	.	.	WR	.	.	.	\N	\N	QC
37	US VDRL	10	HPC	Difco	.	.	R	.	.	.	\N	\N	QC
39	US VDRL	12	NC	Difco	.	.	NR	.	.	.	\N	\N	QC
61	US USR	1	QL	QN: 2	4	8	16	32	64	128	256	Final	RESULT
68	STREP	6	Factors	PFGE	\N	Catalase	Lact	\N	\N	\N	\N	\N	ANALYTE
67	STREP	5	Group	Latex	Latex	Kilian	Suc	\N	\N	\N	\N	\N	ANALYTE
66	STREP	4	Typing pool	PYR	Hipp	Satellites	Malt	\N	\N	\N	\N	\N	ANALYTE
65	STREP	3	Bile sol	A disc	Camp	X+V	Dex	\N	\N	\N	\N	\N	ANALYTE
64	STREP	2	P disc	GmSt	GmSt	GmSt	GmSt	\N	\N	\N	\N	\N	ANALYTE
63	STREP	1	Strep pneumo	Group A Strep	Group B Strep	H Influenzae	N. meningitidis	\N	\N	\N	\N	\N	ANALYTE
70	MICRO	3	Spores	Lactose	Sorbitol	Arginine	\N	\N	\N	\N	\N	\N	ANALYTE
69	MICRO	2	42	Glucose	Salicin	Lysine	\N	\N	\N	\N	\N	\N	ANALYTE
71	MICRO	1	Colony morphology	.	Gram morphology	.	\N	\N	\N	\N	\N	\N	ANALYTE
79	MICRO	11	MacConkey	Fructose	Motility	Pyruvate	\N	\N	\N	\N	\N	\N	ANALYTE
78	MICRO	10	Serology	Inulin	.	6.5% salt	\N	\N	\N	\N	\N	\N	ANALYTE
77	MICRO	9	Satellites	Cellibiose	Alk Phos	Bile Esculin	\N	\N	\N	\N	\N	\N	ANALYTE
76	MICRO	8	Kilian test	Arabinose	PZA	Esculin	\N	\N	\N	\N	\N	\N	ANALYTE
75	MICRO	7	X+V Req	Xylose	Starch	Cetrimide	\N	\N	\N	\N	\N	\N	ANALYTE
87	MICRO	19	Lecthinase	Ribose	Phenylalanine	.	\N	\N	\N	\N	\N	\N	ANALYTE
86	MICRO	18	Lipase	Rhamnose	Acetate	FREEZE	\N	\N	\N	\N	\N	\N	ANALYTE
85	MICRO	17	.	Raffinose	Citrate	PFGE	\N	\N	\N	\N	\N	\N	ANALYTE
84	MICRO	16	Optochin	MMP	MR         VP	LAP         PYR	\N	\N	\N	\N	\N	\N	ANALYTE
83	MICRO	15	Camp	Melibiose	Indol	Erythromycin	\N	\N	\N	\N	\N	\N	ANALYTE
82	MICRO	14	Bacitracin	Mannose	Nitite	Vancomycin	\N	\N	\N	\N	\N	\N	ANALYTE
81	MICRO	13	Catalase	Glycerol	Nitrate	Coagulase	\N	\N	\N	\N	\N	\N	ANALYTE
80	MICRO	12	Oxidase	Galactose	Urease	MGP	\N	\N	\N	\N	\N	\N	ANALYTE
91	STREP	10	\N	\N	\N	\N	Serology	\N	\N	\N	\N	\N	ANALYTE
90	STREP	9	\N	\N	\N	\N	TM	\N	\N	\N	\N	\N	ANALYTE
89	STREP	8	\N	\N	\N	Serology	Oxidase	\N	\N	\N	\N	\N	ANALYTE
88	STREP	7	\N	\N	\N	Oxidase	Catalase	\N	\N	\N	\N	\N	ANALYTE
94	US USR	7	Kahn's solution	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
1	TB	1	Micro Result	Culture Result	Report Date	\N	\N	\N	\N	\N	\N	\N	RESULT
2	GC	1	12 HOUR	24 HOUR	48 HOUR	\N	\N	\N	\N	\N	\N	\N	RESULT
9	US FTA	7	\N	\N	\N	\N	PBS	.	.	.	-	.	QC
10	US FTA	8	\N	\N	\N	\N	Sorbent	.	.	.	-	.	QC
8	US FTA	6	PBS	.	.	.	Nonspecific Control / Sorbent	.	.	.	-	.	QC
11	US FTA	1	    USR     Repeat Quant	   FTA      Current Run	    FTA     Previous Runs	   FTA         Final	  TPPA       Result	Comments	\N	\N	\N	\N	RESULT
26	US FTA	1	TEST	TESTing	\N	\N	\N	\N	\N	\N	\N	\N	ANALYTE
7	US FTA	5	.	.	.	.	Nonspecific Control/ PBS	.	.	.	>2+	.	QC
6	US FTA	4	FITCconjugate Working Dilution:	.	.	.	Minimally Reactive Control	.	.	.	1+	.	QC
5	US FTA	3	Sorbent	.	.	.	Reactive Control / Sorbent	.	.	.	3-4+	.	QC
4	US FTA	2	T Palladium antigen	.	.	.	Reactive Control/PBS	.	.	.	4+	.	QC
3	US FTA	1	Reagent	Source	Lot	Expire Date	Reagent	Source	Lot	Expires	Expect	Actual	QC
25	F	23	KNO3	RT incubation	.	.	\N	\N	\N	\N	\N	\N	ANALYTE
24	F	22	dulcitol	chlamydospores	.	.	\N	\N	\N	\N	\N	\N	ANALYTE
23	F	21	melibiose	ascospores	NO3	.	\N	\N	\N	\N	\N	\N	ANALYTE
22	F	20	trehalose	pigment	gelatin	.	\N	\N	\N	\N	\N	\N	ANALYTE
21	F	19	xylose	germ tubes	urea	probe (histo)	\N	\N	\N	\N	\N	\N	ANALYTE
20	F	18	inositol	urea	T4	probe (blasto)	\N	\N	\N	\N	\N	\N	ANALYTE
19	F	17	raffinose	OTHER TESTS	T3	probe (cocci)	\N	\N	\N	\N	\N	\N	ANALYTE
18	F	16	cellobiose	galactose	T2	PROBE TESTS	\N	\N	\N	\N	\N	\N	ANALYTE
17	F	15	galactose	trehalose	T1	.	\N	\N	\N	\N	\N	\N	ANALYTE
16	F	14	lactose	lactose	37 degrees	lysozyme	\N	\N	\N	\N	\N	\N	ANALYTE
15	F	13	sucrose	sucrose	RT incubation	xanthine	\N	\N	\N	\N	\N	\N	ANALYTE
14	F	12	maltose	maltose	sabs without CH	tyrosine	\N	\N	\N	\N	\N	\N	ANALYTE
13	F	11	dextrose	dextrose	sabs with CH	casein	\N	\N	\N	\N	\N	\N	ANALYTE
12	F	10	ASSIMILATIONS	FERMENTATIONS	FILAMENTOUS	ACTINOMYCETES	\N	\N	\N	\N	\N	\N	ANALYTE
62	US VDRL	1	QL	QN:2	4	8	16	32	64	128	256	Final	RESULT
93	US USR	15	Titer	.	.	.	.	.	.	.	\N	\N	QC
96	US FTA	10	\N	\N	\N	\N	TPPA - control	.	.	.	-	.	QC
95	US FTA	9	\N	\N	\N	\N	TPPA + control	.	.	.	+	.	QC
49	US USR	11	CONTROLS	SOURCE	LOT	EXPIRE DATE	RESULTS Qualitative Expect	RESULTS Qualitative  Actual	RESULTS Quantitative Expect	RESULTS Quantitative Actual	\N	\N	QC
48	US USR	10	Rotation of Slides	178-182	__________	rotations/min	\N	\N	\N	\N	\N	\N	QC
47	US USR	9	Temp of Lab	23-29	__________	degrees C	\N	\N	\N	\N	\N	\N	QC
46	US USR	6	Sterile DI H20	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
45	US USR	8	USR working antigen	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
44	US USR	5	Phosphate Buffer	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
43	US USR	4	EDTA	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
42	US USR	3	Choline Chloride	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
41	US USR	2	VDRL Antigen Kit	Difco	.	.	\N	\N	\N	\N	\N	\N	QC
40	US USR	1	REAGENT	SOURCE	LOT	EXPIRE DATE	\N	\N	\N	\N	\N	\N	QC
52	US USR	14	NC	Difco	.	.	NR	.	.	.	\N	\N	QC
51	US USR	13	LPC	Difco	.	.	WR	.	.	.	\N	\N	QC
50	US USR	12	HPC	Difco	.	.	R	.	.	.	\N	\N	QC
55	HSV TYPING	3	Expiration Date:	________________	Negative Cells:	Lot _____________	\N	\N	\N	\N	\N	\N	QC
54	HSV TYPING	2	Lot:	________________	HSV2 Positive Cells:	Lot _____________	\N	\N	\N	\N	\N	\N	QC
53	HSV TYPING	1	HSV1 / HSV2 Typing Kit:	________________	HSV1 Positive Cells:	Lot _____________	\N	\N	\N	\N	\N	\N	QC
57	HSV TYPING	2	HSV-1 PC	.	.	.	.	.	.	.	\N	\N	RESULT
58	HSV TYPING	3	HSV-1 nc	.	.	.	.	.	.	.	\N	\N	RESULT
60	HSV TYPING	5	HSV-2 nc	.	.	.	.	.	.	.	\N	\N	RESULT
59	HSV TYPING	4	HSV-2 PC	.	.	.	.	.	.	.	\N	\N	RESULT
74	MICRO	6	Pigment	Mannitol	Casein	Gelatin	\N	\N	\N	\N	\N	\N	ANALYTE
73	MICRO	5	Hemolysis	Maltose	Trehalose	Litmus Milk	\N	\N	\N	\N	\N	\N	ANALYTE
72	MICRO	4	O2 Relation	Sucrose	Sorbose	Ornithine	\N	\N	\N	\N	\N	\N	ANALYTE
92	STREP	11	\N	\N	\N	\N	PFGE	\N	\N	\N	\N	\N	ANALYTE
35	US VDRL	8	Rotation of Slides	.	178 - 182	__________	rotation/min	\N	\N	\N	\N	\N	QC
34	US VDRL	7	Temp of Lab	.	23 - 29	__________	degrees C	\N	\N	\N	\N	\N	QC
36	US VDRL	9	CONTROLS	SOURCE	LOT	EXPIRE DATE	RESULT Qualitative Expect	RESULT Qualitative  Actual	RESULT Quantitative Expect	RESULT Quantitative Actual	\N	\N	QC
56	HSV TYPING	1	Specimen ID	Received Date	Source	Pass	HSV1	HSV2	Comments	Mailer	\N	\N	RESULT
27	VI	1	Comments	Results	Interpretation	\N	\N	\N	\N	\N	\N	\N	RESULT
32	US VDRL	5	VDRL working antigen	.	.	.	\N	\N	\N	\N	\N	\N	QC
31	US VDRL	4	10% Saline	.	.	.	\N	\N	\N	\N	\N	\N	QC
30	US VDRL	3	Kahn Saline 0.9%	.	.	.	\N	\N	\N	\N	\N	\N	QC
29	US VDRL	2	VDRL Antigen Kit	Difco	.	.	\N	\N	\N	\N	\N	\N	QC
28	US VDRL	1	REAGENT	SOURCE	LOT	EXPIRE DATE	\N	\N	\N	\N	\N	\N	QC
38	US VDRL	11	LPC	Difco	.	.	WR	.	.	.	\N	\N	QC
37	US VDRL	10	HPC	Difco	.	.	R	.	.	.	\N	\N	QC
39	US VDRL	12	NC	Difco	.	.	NR	.	.	.	\N	\N	QC
61	US USR	1	QL	QN: 2	4	8	16	32	64	128	256	Final	RESULT
68	STREP	6	Factors	PFGE	\N	Catalase	Lact	\N	\N	\N	\N	\N	ANALYTE
67	STREP	5	Group	Latex	Latex	Kilian	Suc	\N	\N	\N	\N	\N	ANALYTE
66	STREP	4	Typing pool	PYR	Hipp	Satellites	Malt	\N	\N	\N	\N	\N	ANALYTE
65	STREP	3	Bile sol	A disc	Camp	X+V	Dex	\N	\N	\N	\N	\N	ANALYTE
64	STREP	2	P disc	GmSt	GmSt	GmSt	GmSt	\N	\N	\N	\N	\N	ANALYTE
63	STREP	1	Strep pneumo	Group A Strep	Group B Strep	H Influenzae	N. meningitidis	\N	\N	\N	\N	\N	ANALYTE
70	MICRO	3	Spores	Lactose	Sorbitol	Arginine	\N	\N	\N	\N	\N	\N	ANALYTE
69	MICRO	2	42	Glucose	Salicin	Lysine	\N	\N	\N	\N	\N	\N	ANALYTE
71	MICRO	1	Colony morphology	.	Gram morphology	.	\N	\N	\N	\N	\N	\N	ANALYTE
79	MICRO	11	MacConkey	Fructose	Motility	Pyruvate	\N	\N	\N	\N	\N	\N	ANALYTE
78	MICRO	10	Serology	Inulin	.	6.5% salt	\N	\N	\N	\N	\N	\N	ANALYTE
77	MICRO	9	Satellites	Cellibiose	Alk Phos	Bile Esculin	\N	\N	\N	\N	\N	\N	ANALYTE
76	MICRO	8	Kilian test	Arabinose	PZA	Esculin	\N	\N	\N	\N	\N	\N	ANALYTE
75	MICRO	7	X+V Req	Xylose	Starch	Cetrimide	\N	\N	\N	\N	\N	\N	ANALYTE
87	MICRO	19	Lecthinase	Ribose	Phenylalanine	.	\N	\N	\N	\N	\N	\N	ANALYTE
86	MICRO	18	Lipase	Rhamnose	Acetate	FREEZE	\N	\N	\N	\N	\N	\N	ANALYTE
85	MICRO	17	.	Raffinose	Citrate	PFGE	\N	\N	\N	\N	\N	\N	ANALYTE
84	MICRO	16	Optochin	MMP	MR         VP	LAP         PYR	\N	\N	\N	\N	\N	\N	ANALYTE
83	MICRO	15	Camp	Melibiose	Indol	Erythromycin	\N	\N	\N	\N	\N	\N	ANALYTE
82	MICRO	14	Bacitracin	Mannose	Nitite	Vancomycin	\N	\N	\N	\N	\N	\N	ANALYTE
81	MICRO	13	Catalase	Glycerol	Nitrate	Coagulase	\N	\N	\N	\N	\N	\N	ANALYTE
80	MICRO	12	Oxidase	Galactose	Urease	MGP	\N	\N	\N	\N	\N	\N	ANALYTE
91	STREP	10	\N	\N	\N	\N	Serology	\N	\N	\N	\N	\N	ANALYTE
90	STREP	9	\N	\N	\N	\N	TM	\N	\N	\N	\N	\N	ANALYTE
89	STREP	8	\N	\N	\N	Serology	Oxidase	\N	\N	\N	\N	\N	ANALYTE
88	STREP	7	\N	\N	\N	Oxidase	Catalase	\N	\N	\N	\N	\N	ANALYTE
94	US USR	7	Kahn's solution	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
1	TB	1	Micro Result	Culture Result	Report Date	\N	\N	\N	\N	\N	\N	\N	RESULT
2	GC	1	12 HOUR	24 HOUR	48 HOUR	\N	\N	\N	\N	\N	\N	\N	RESULT
9	US FTA	7	\N	\N	\N	\N	PBS	.	.	.	-	.	QC
10	US FTA	8	\N	\N	\N	\N	Sorbent	.	.	.	-	.	QC
8	US FTA	6	PBS	.	.	.	Nonspecific Control / Sorbent	.	.	.	-	.	QC
11	US FTA	1	    USR     Repeat Quant	   FTA      Current Run	    FTA     Previous Runs	   FTA         Final	  TPPA       Result	Comments	\N	\N	\N	\N	RESULT
26	US FTA	1	TEST	TESTing	\N	\N	\N	\N	\N	\N	\N	\N	ANALYTE
7	US FTA	5	.	.	.	.	Nonspecific Control/ PBS	.	.	.	>2+	.	QC
6	US FTA	4	FITCconjugate Working Dilution:	.	.	.	Minimally Reactive Control	.	.	.	1+	.	QC
5	US FTA	3	Sorbent	.	.	.	Reactive Control / Sorbent	.	.	.	3-4+	.	QC
4	US FTA	2	T Palladium antigen	.	.	.	Reactive Control/PBS	.	.	.	4+	.	QC
3	US FTA	1	Reagent	Source	Lot	Expire Date	Reagent	Source	Lot	Expires	Expect	Actual	QC
25	F	23	KNO3	RT incubation	.	.	\N	\N	\N	\N	\N	\N	ANALYTE
24	F	22	dulcitol	chlamydospores	.	.	\N	\N	\N	\N	\N	\N	ANALYTE
23	F	21	melibiose	ascospores	NO3	.	\N	\N	\N	\N	\N	\N	ANALYTE
22	F	20	trehalose	pigment	gelatin	.	\N	\N	\N	\N	\N	\N	ANALYTE
21	F	19	xylose	germ tubes	urea	probe (histo)	\N	\N	\N	\N	\N	\N	ANALYTE
20	F	18	inositol	urea	T4	probe (blasto)	\N	\N	\N	\N	\N	\N	ANALYTE
19	F	17	raffinose	OTHER TESTS	T3	probe (cocci)	\N	\N	\N	\N	\N	\N	ANALYTE
18	F	16	cellobiose	galactose	T2	PROBE TESTS	\N	\N	\N	\N	\N	\N	ANALYTE
17	F	15	galactose	trehalose	T1	.	\N	\N	\N	\N	\N	\N	ANALYTE
16	F	14	lactose	lactose	37 degrees	lysozyme	\N	\N	\N	\N	\N	\N	ANALYTE
15	F	13	sucrose	sucrose	RT incubation	xanthine	\N	\N	\N	\N	\N	\N	ANALYTE
14	F	12	maltose	maltose	sabs without CH	tyrosine	\N	\N	\N	\N	\N	\N	ANALYTE
13	F	11	dextrose	dextrose	sabs with CH	casein	\N	\N	\N	\N	\N	\N	ANALYTE
12	F	10	ASSIMILATIONS	FERMENTATIONS	FILAMENTOUS	ACTINOMYCETES	\N	\N	\N	\N	\N	\N	ANALYTE
62	US VDRL	1	QL	QN:2	4	8	16	32	64	128	256	Final	RESULT
93	US USR	15	Titer	.	.	.	.	.	.	.	\N	\N	QC
96	US FTA	10	\N	\N	\N	\N	TPPA - control	.	.	.	-	.	QC
95	US FTA	9	\N	\N	\N	\N	TPPA + control	.	.	.	+	.	QC
49	US USR	11	CONTROLS	SOURCE	LOT	EXPIRE DATE	RESULTS Qualitative Expect	RESULTS Qualitative  Actual	RESULTS Quantitative Expect	RESULTS Quantitative Actual	\N	\N	QC
48	US USR	10	Rotation of Slides	178-182	__________	rotations/min	\N	\N	\N	\N	\N	\N	QC
47	US USR	9	Temp of Lab	23-29	__________	degrees C	\N	\N	\N	\N	\N	\N	QC
46	US USR	6	Sterile DI H20	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
45	US USR	8	USR working antigen	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
44	US USR	5	Phosphate Buffer	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
43	US USR	4	EDTA	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
42	US USR	3	Choline Chloride	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
41	US USR	2	VDRL Antigen Kit	Difco	.	.	\N	\N	\N	\N	\N	\N	QC
40	US USR	1	REAGENT	SOURCE	LOT	EXPIRE DATE	\N	\N	\N	\N	\N	\N	QC
52	US USR	14	NC	Difco	.	.	NR	.	.	.	\N	\N	QC
51	US USR	13	LPC	Difco	.	.	WR	.	.	.	\N	\N	QC
50	US USR	12	HPC	Difco	.	.	R	.	.	.	\N	\N	QC
55	HSV TYPING	3	Expiration Date:	________________	Negative Cells:	Lot _____________	\N	\N	\N	\N	\N	\N	QC
54	HSV TYPING	2	Lot:	________________	HSV2 Positive Cells:	Lot _____________	\N	\N	\N	\N	\N	\N	QC
53	HSV TYPING	1	HSV1 / HSV2 Typing Kit:	________________	HSV1 Positive Cells:	Lot _____________	\N	\N	\N	\N	\N	\N	QC
57	HSV TYPING	2	HSV-1 PC	.	.	.	.	.	.	.	\N	\N	RESULT
58	HSV TYPING	3	HSV-1 nc	.	.	.	.	.	.	.	\N	\N	RESULT
60	HSV TYPING	5	HSV-2 nc	.	.	.	.	.	.	.	\N	\N	RESULT
59	HSV TYPING	4	HSV-2 PC	.	.	.	.	.	.	.	\N	\N	RESULT
74	MICRO	6	Pigment	Mannitol	Casein	Gelatin	\N	\N	\N	\N	\N	\N	ANALYTE
73	MICRO	5	Hemolysis	Maltose	Trehalose	Litmus Milk	\N	\N	\N	\N	\N	\N	ANALYTE
72	MICRO	4	O2 Relation	Sucrose	Sorbose	Ornithine	\N	\N	\N	\N	\N	\N	ANALYTE
92	STREP	11	\N	\N	\N	\N	PFGE	\N	\N	\N	\N	\N	ANALYTE
35	US VDRL	8	Rotation of Slides	.	178 - 182	__________	rotation/min	\N	\N	\N	\N	\N	QC
34	US VDRL	7	Temp of Lab	.	23 - 29	__________	degrees C	\N	\N	\N	\N	\N	QC
36	US VDRL	9	CONTROLS	SOURCE	LOT	EXPIRE DATE	RESULT Qualitative Expect	RESULT Qualitative  Actual	RESULT Quantitative Expect	RESULT Quantitative Actual	\N	\N	QC
56	HSV TYPING	1	Specimen ID	Received Date	Source	Pass	HSV1	HSV2	Comments	Mailer	\N	\N	RESULT
27	VI	1	Comments	Results	Interpretation	\N	\N	\N	\N	\N	\N	\N	RESULT
32	US VDRL	5	VDRL working antigen	.	.	.	\N	\N	\N	\N	\N	\N	QC
31	US VDRL	4	10% Saline	.	.	.	\N	\N	\N	\N	\N	\N	QC
30	US VDRL	3	Kahn Saline 0.9%	.	.	.	\N	\N	\N	\N	\N	\N	QC
29	US VDRL	2	VDRL Antigen Kit	Difco	.	.	\N	\N	\N	\N	\N	\N	QC
28	US VDRL	1	REAGENT	SOURCE	LOT	EXPIRE DATE	\N	\N	\N	\N	\N	\N	QC
38	US VDRL	11	LPC	Difco	.	.	WR	.	.	.	\N	\N	QC
37	US VDRL	10	HPC	Difco	.	.	R	.	.	.	\N	\N	QC
39	US VDRL	12	NC	Difco	.	.	NR	.	.	.	\N	\N	QC
61	US USR	1	QL	QN: 2	4	8	16	32	64	128	256	Final	RESULT
68	STREP	6	Factors	PFGE	\N	Catalase	Lact	\N	\N	\N	\N	\N	ANALYTE
67	STREP	5	Group	Latex	Latex	Kilian	Suc	\N	\N	\N	\N	\N	ANALYTE
66	STREP	4	Typing pool	PYR	Hipp	Satellites	Malt	\N	\N	\N	\N	\N	ANALYTE
65	STREP	3	Bile sol	A disc	Camp	X+V	Dex	\N	\N	\N	\N	\N	ANALYTE
64	STREP	2	P disc	GmSt	GmSt	GmSt	GmSt	\N	\N	\N	\N	\N	ANALYTE
63	STREP	1	Strep pneumo	Group A Strep	Group B Strep	H Influenzae	N. meningitidis	\N	\N	\N	\N	\N	ANALYTE
70	MICRO	3	Spores	Lactose	Sorbitol	Arginine	\N	\N	\N	\N	\N	\N	ANALYTE
69	MICRO	2	42	Glucose	Salicin	Lysine	\N	\N	\N	\N	\N	\N	ANALYTE
71	MICRO	1	Colony morphology	.	Gram morphology	.	\N	\N	\N	\N	\N	\N	ANALYTE
79	MICRO	11	MacConkey	Fructose	Motility	Pyruvate	\N	\N	\N	\N	\N	\N	ANALYTE
78	MICRO	10	Serology	Inulin	.	6.5% salt	\N	\N	\N	\N	\N	\N	ANALYTE
77	MICRO	9	Satellites	Cellibiose	Alk Phos	Bile Esculin	\N	\N	\N	\N	\N	\N	ANALYTE
76	MICRO	8	Kilian test	Arabinose	PZA	Esculin	\N	\N	\N	\N	\N	\N	ANALYTE
75	MICRO	7	X+V Req	Xylose	Starch	Cetrimide	\N	\N	\N	\N	\N	\N	ANALYTE
87	MICRO	19	Lecthinase	Ribose	Phenylalanine	.	\N	\N	\N	\N	\N	\N	ANALYTE
86	MICRO	18	Lipase	Rhamnose	Acetate	FREEZE	\N	\N	\N	\N	\N	\N	ANALYTE
85	MICRO	17	.	Raffinose	Citrate	PFGE	\N	\N	\N	\N	\N	\N	ANALYTE
84	MICRO	16	Optochin	MMP	MR         VP	LAP         PYR	\N	\N	\N	\N	\N	\N	ANALYTE
83	MICRO	15	Camp	Melibiose	Indol	Erythromycin	\N	\N	\N	\N	\N	\N	ANALYTE
82	MICRO	14	Bacitracin	Mannose	Nitite	Vancomycin	\N	\N	\N	\N	\N	\N	ANALYTE
81	MICRO	13	Catalase	Glycerol	Nitrate	Coagulase	\N	\N	\N	\N	\N	\N	ANALYTE
80	MICRO	12	Oxidase	Galactose	Urease	MGP	\N	\N	\N	\N	\N	\N	ANALYTE
91	STREP	10	\N	\N	\N	\N	Serology	\N	\N	\N	\N	\N	ANALYTE
90	STREP	9	\N	\N	\N	\N	TM	\N	\N	\N	\N	\N	ANALYTE
89	STREP	8	\N	\N	\N	Serology	Oxidase	\N	\N	\N	\N	\N	ANALYTE
88	STREP	7	\N	\N	\N	Oxidase	Catalase	\N	\N	\N	\N	\N	ANALYTE
94	US USR	7	Kahn's solution	MDH	.	.	\N	\N	\N	\N	\N	\N	QC
\.


--
-- Data for Name: worksheet_item; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY worksheet_item (id, "position", worksheet_id) FROM stdin;
\.


--
-- Data for Name: worksheet_qc; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY worksheet_qc (id, sort_order, wq_type, value, worksheet_analysis_id, qc_analyte_id) FROM stdin;
\.


--
-- Data for Name: worksheets; Type: TABLE DATA; Schema: clinlims; Owner: clinlims
--

COPY worksheets (id, sys_user_id, test_id, created, status, number_format) FROM stdin;
\.


--
-- Name: zip_code_seq; Type: SEQUENCE SET; Schema: clinlims; Owner: clinlims
--

SELECT pg_catalog.setval('zip_code_seq', 1, false);
